package gcp.frontpc.controller.view.mms;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import gcp.oms.model.search.OmsClaimSearch;
import gcp.oms.model.search.OmsOrderSearch;
import gcp.oms.service.ClaimService;
import gcp.oms.service.OrderService;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.SessionUtil;

@Controller("mmsClaimViewController")
@RequestMapping("mms/mypage/claim")
public class ClaimController {

	// private static final Logger logger = LoggerFactory.getLogger(OrderController.class);

	@Autowired
	private OrderService orderService;
	@Autowired
	private ClaimService claimService;

	@RequestMapping(value = { "/history" } ,method = { RequestMethod.GET ,RequestMethod.POST })
	public String makeView(Model model, HttpServletRequest request) throws Exception {
		model.addAttribute("tabIdx", CommonUtil.replaceNull(request.getParameter("tabIdx"), "0"));
		return CommonUtil.makeJspUrl(request);
	}

	@RequestMapping(value = "/inner/{tabType}" ,method = { RequestMethod.POST })
	public String selectList(Model model, HttpServletRequest request, @ModelAttribute OmsOrderSearch orderSearch, @PathVariable String tabType)
			throws Exception {
		orderSearch.setStoreId(SessionUtil.getStoreId());
		orderSearch.setMemberNo(SessionUtil.getMemberNo());
		orderSearch.setSearchId("oms.claim.select.foList");

		if ("pickup".equals(tabType)) {
			// 1. 지역,지점 검색
			orderSearch.setOrderer(SessionUtil.getMemberNo().toString());
			orderSearch.setOrdererType("number");
			orderSearch.setPickupProductStateCd("PICKUP_PRODUCT_STATE_CD.CANCEL");
			
			orderSearch.setSearchId("oms.pickup.selectAreaList");
			List<?> pickupAreaList = orderService.selectList(orderSearch.getSearchId(), orderSearch);
			
			model.addAttribute("pickupAreaList", pickupAreaList);
			model.addAttribute("orderSearch", orderSearch);
			
			// 2. 픽업상품 검색
			orderSearch.setSearchId("oms.pickup.selectFoList");
		} else if ("regular".equals(tabType)) {
			orderSearch.setOrderTypeCds("ORDER_TYPE_CD.REGULARDELIVERY");
		} else if ("general".equals(tabType)) {
		}
		List<?> claimList = orderService.selectList(orderSearch.getSearchId(), orderSearch);
		model.addAttribute("claimList", claimList);

		return CommonUtil.makeJspUrl(request);
	}

	@RequestMapping(value = "/{orderType}/{orderId}/{claimNo}" ,method = RequestMethod.GET)
	public String selectOne(Model model, @PathVariable String orderType, @PathVariable String orderId,  @PathVariable String claimNo) throws Exception {
		OmsOrderSearch orderSearch = new OmsOrderSearch();
		orderSearch.setOrderId(orderId);
		if ("pickup".equals(orderType)) {
			orderSearch.setStoreId(SessionUtil.getStoreId());
			orderSearch.setSearchId("oms.pickup.selectFoList");

			List<?> orderList = orderService.selectList(orderSearch.getSearchId(), orderSearch);
			model.addAttribute("pickup", orderList.get(0));
		} else {
			OmsClaimSearch claimSearch = new OmsClaimSearch();
			claimSearch.setOrderId(orderId);
			claimSearch.setClaimNo(claimNo);
			
			Object omsClaim = claimService.selectOne("oms.claim.select.fodetail", claimSearch);
			Object omsDelivery = orderService.selectOne("oms.delivery.selectAmt", orderId);
			List<?> couponList = orderService.selectList("oms.order.select.coupon", orderId);
			List<?> paymentList = orderService.selectList("oms.payment.selectList", claimSearch);

			model.addAttribute("claim", omsClaim);
			model.addAttribute("delivery", omsDelivery);
			model.addAttribute("couponList", couponList);
			model.addAttribute("paymentList", paymentList);
		}

		return "/mms/mypage/claim/claim" + StringUtils.capitalize(orderType);
	}

//	@RequestMapping(value = "" ,method = RequestMethod.POST)
//	public CcsMessage claim(@ModelAttribute("claimForm") OmsClaim omsClaim) {
//		CcsMessage result = new CcsMessage();
//		try {
//			String memberNo = SessionUtil.getLoginId();
//			result = claimService.createClaim(omsClaim, memberNo);
//		} catch (Exception e) {
//			result.setSuccess(false);
//			result.setResultCode("-999");
//			result.setResultMessage(e.getMessage());
//		}
//		return result;
//	}

}
