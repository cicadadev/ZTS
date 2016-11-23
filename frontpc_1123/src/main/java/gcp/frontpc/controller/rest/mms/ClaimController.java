package gcp.frontpc.controller.rest.mms;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsMessage;
import gcp.oms.model.OmsClaimWrapper;
import gcp.oms.model.OmsDelivery;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.OmsPayment;
import gcp.oms.model.OmsPickupproduct;
import gcp.oms.model.search.OmsClaimSearch;
import gcp.oms.model.search.OmsOrderSearch;
import gcp.oms.model.search.OmsPickupSearch;
import gcp.oms.service.ClaimService;
import gcp.oms.service.PaymentService;
import gcp.oms.service.PickupService;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.SessionUtil;

@RestController("mmsClaimRestController")
@RequestMapping({ "api/mms/mypage/claim" ,"api/ccs/guest/claim" })
public class ClaimController {

	// @Autowired
	// private OrderService orderService;

	@Autowired
	private PickupService pickupService;

	@Autowired
	private ClaimService claimService;

	@Autowired
	private PaymentService paymentService;

	@RequestMapping(value = { "/list" ,"/target/list" } ,method = { RequestMethod.GET ,RequestMethod.POST })
	public List<?> selectList(@RequestBody OmsClaimSearch claimSearch) throws Exception {
		claimSearch.setStoreId(SessionUtil.getStoreId());
		// List<?> claimList = claimService.selectList(claimSearch.getSearchId(), claimSearch);
		List<?> claimList = claimService.selectList("oms.claim.select.targetList", claimSearch);
		return claimList;
	}

	@RequestMapping(value = { "/present/list" } ,method = { RequestMethod.GET ,RequestMethod.POST })
	public List<?> presentList(@RequestBody OmsClaimSearch claimSearch) throws Exception {
		claimSearch.setStoreId(SessionUtil.getStoreId());
		List<?> claimList = claimService.selectList("oms.order.select.presentList", claimSearch);
		return claimList;
	}

	@RequestMapping(value = { "/all" } ,method = { RequestMethod.GET ,RequestMethod.POST })
	public Object selectAll(@RequestBody OmsClaimSearch claimSearch) throws Exception {
		claimSearch.setStoreId(SessionUtil.getStoreId());
		List<?> targetList = claimService.selectList("oms.claim.select.targetList", claimSearch);
		List<?> presentList = claimService.selectList("oms.order.select.presentList", claimSearch);

		OmsOrderSearch orderSearch = new OmsOrderSearch();
		orderSearch.setOrderId(claimSearch.getOrderId());
		List<?> couponList = claimService.selectList("oms.order.select.couponList", orderSearch);

		OmsDelivery omsDelivery = new OmsDelivery();
		omsDelivery.setOrderId(claimSearch.getOrderId());
		omsDelivery.setDeliveryAddressNo(claimSearch.getDeliveryAddressNo());
		omsDelivery.setDeliveryPolicyNo(claimSearch.getDeliveryPolicyNo());
		omsDelivery.setSelectKey(claimSearch.getSelectKey());

		OmsDelivery policy = (OmsDelivery) claimService.selectOne("oms.claim.select.delivery", omsDelivery);

		Map<String, Object> returnMap = new HashMap<String, Object>();
		returnMap.put("targetList", targetList);
		returnMap.put("presentList", presentList);
		returnMap.put("couponList", couponList);
		returnMap.put("policy", policy);

		return returnMap;
	}

	/**
	 * 프론트 클레임
	 * 
	 * @Method Name : claim
	 * @author : victor
	 * @date : 2016. 11. 3.
	 * @description :
	 *
	 * @param omsClaimWrapper
	 * @param omsPayment
	 * @param request
	 * @return CcsMessage
	 */
	@RequestMapping(value = "" ,method = RequestMethod.POST)
	public CcsMessage claim(@ModelAttribute OmsClaimWrapper omsClaimWrapper, @ModelAttribute OmsPayment omsPayment, HttpServletRequest request) {
		CcsMessage ccsmessage = new CcsMessage();
		try {
			omsClaimWrapper.setUpdId(SessionUtil.getLoginId());
			BigDecimal memberNo = SessionUtil.getMemberNo();

			if (omsPayment != null && !StringUtils.isEmpty(omsPayment.getPaymentMethodCd())) {
				omsPayment.setPaymentTypeCd("PAYMENT_TYPE_CD.ADDPAYMENT");
				if ("PAYMENT_METHOD_CD.KAKAO".equals(omsPayment.getPaymentMethodCd())) {
					paymentService.createKakaoRequestParam(omsPayment, request);
				} else {
					omsPayment.setPaymentAmt(new BigDecimal(omsPayment.getOmsPaymentif().getLGD_AMOUNT()));
				}
				omsPayment.setUpdId(SessionUtil.getLoginId());
				omsPayment.setInsId(SessionUtil.getLoginId());

				List<OmsPayment> omsPayments = new ArrayList<OmsPayment>();
				omsPayments.add(omsPayment);
				omsClaimWrapper.setOmsPayments(omsPayments);
			}

			List<OmsOrderproduct> omsOrderproducts = omsClaimWrapper.getOmsOrderproducts();
			for (OmsOrderproduct a : omsOrderproducts) {
				System.out.println("getNewSaleProductId()  :" + a.getNewSaleProductId());
				System.out.println("getNewSaleProductNm()  :" + a.getNewSaleProductNm());
			}

			ccsmessage = claimService.makeClaimData(omsClaimWrapper, memberNo);
		} catch (Exception e) {
			ccsmessage.setSuccess(false);
			ccsmessage.setResultCode("-999");
			ccsmessage.setResultMessage(e.getMessage());
		}
		return ccsmessage;
	}

	@RequestMapping(value = "/pickup" ,method = RequestMethod.POST)
	public CcsMessage claim(@RequestBody OmsPickupSearch omsPickupSearch) {
		String userId = SessionUtil.getLoginId();
		CcsMessage ccsMessage = new CcsMessage();
		ccsMessage.setSuccess(true);
		try {

			OmsPickupproduct omsPickupproduct = (OmsPickupproduct) pickupService.selectOne("oms.pickup.selectOne", omsPickupSearch);
			ccsMessage.setReturnObject(omsPickupproduct);

			List<OmsPickupproduct> omsPickupproducts = new ArrayList<OmsPickupproduct>();
			omsPickupproducts.add(omsPickupproduct);
			if (pickupService.cancelPickup(omsPickupproducts, userId) < 1) {
				throw new ServiceException("oms.common.error", "픽업 상품의 취소 중 에러가 발생 하였습니다.");
			}
		} catch (ServiceException e) {
			ccsMessage.setSuccess(false);
			ccsMessage.setResultMessage(e.getMessage());
		} catch (Exception e) {
			ccsMessage.setSuccess(false);
			ccsMessage.setResultMessage("픽업 상품 취소 중 알수 없는 에러가 발생하였습니다.");
		}
		return ccsMessage;
	}

}
