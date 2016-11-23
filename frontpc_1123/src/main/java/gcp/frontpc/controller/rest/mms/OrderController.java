package gcp.frontpc.controller.rest.mms;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsMessage;
import gcp.external.model.TmsQueue;
import gcp.external.service.TmsService;
import gcp.mms.model.search.MmsMemberSearch;
import gcp.oms.model.OmsDeliveryaddress;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.service.DeliveryService;
import gcp.oms.service.OrderService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;

@RestController("mmsOrderRestController")
@RequestMapping({ "api/mms/mypage/order" ,"api/ccs/guest/order" })
public class OrderController {

	@Autowired
	private OrderService orderService;

	@Autowired
	private DeliveryService deliveryService;

	@Autowired
	private TmsService tmsService;

	private final Log logger = LogFactory.getLog(getClass());

	/**
	 * 일단 옵션변경
	 * 
	 * @Method Name : udpate
	 * @author : victor
	 * @date : 2016. 8. 1.
	 * @description :
	 *
	 * @param omsOrderproducts
	 * @return CcsMessage
	 * @throws Exception
	 */
	@RequestMapping(value = "/update/option" ,method = RequestMethod.POST ,produces = "application/json")
	public CcsMessage udpate(@RequestBody List<OmsOrderproduct> omsOrderproducts) throws Exception {
		String userId = SessionUtil.getLoginId();
		CcsMessage ccsMessage = new CcsMessage();
		ccsMessage.setSuccess(true);
		try {
			for (OmsOrderproduct omsOrderproduct : omsOrderproducts) {
				omsOrderproduct.setStoreId(SessionUtil.getStoreId());
				omsOrderproduct.setInsId(userId);
				omsOrderproduct.setUpdId(userId);

				if (orderService.updateOption(omsOrderproduct) < 1) {
					throw new ServiceException("oms.common.error", "주문 상품의 옵션 변경 중 에러가 발생 하였습니다.");
				}
			}
		} catch (ServiceException e) {
			ccsMessage.setSuccess(false);
			ccsMessage.setResultMessage(e.getMessage());
		} catch (Exception e) {
			ccsMessage.setSuccess(false);
			ccsMessage.setResultMessage("주문 상품의 옵션 변경 중 알수 없는 에러가 발생하였습니다.");
		}
		ccsMessage.setReturnObject(omsOrderproducts);
		return ccsMessage;
	}

	@RequestMapping(value = "/update/delivery" ,method = RequestMethod.POST)
	public Object udpate(@ModelAttribute OmsDeliveryaddress address) throws Exception {
		if (deliveryService.update(address) > 0) {
			return address;
		} else {
			return null;
		}
	}

	/**
	 * 
	 * @Method Name : deleveryApproval
	 * @author : intune
	 * @date : 2016. 9. 22.
	 * @description : 선물 배송 요청하기
	 *
	 * @param request
	 * @param omsDeliveryaddress
	 * @throws Exception
	 */
	@RequestMapping(value = "/gift/deleveryApproval" ,method = { RequestMethod.GET ,RequestMethod.POST })
	public void deleveryApproval(HttpServletRequest request, @ModelAttribute OmsDeliveryaddress omsDeliveryaddress) throws Exception {

		logger.debug(omsDeliveryaddress);
		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

		// OMS_ORDER 배송상태코드 - 출고지시대기 상태로 변경
		// OMS_ORDER 주문상태코드 - 주문완료 상태로 변경
		OmsOrder omsOrder = new OmsOrder();
		omsOrder.setStoreId(SessionUtil.getStoreId());
		omsOrder.setOrderId(omsDeliveryaddress.getOrderId());
		omsOrder.setOrderDeliveryStateCd(BaseConstants.ORDER_DELIVERY_STATE_CD_READY);
		omsOrder.setOrderStateCd(BaseConstants.ORDER_STATE_CD_COMPLETE);
		omsOrder.setUpdDt(currentDate);
		if (StringUtils.isNotEmpty(SessionUtil.getLoginId())) {
			omsOrder.setUpdId(SessionUtil.getLoginId());
		} else {
			omsOrder.setUpdId(BaseConstants.NON_MEMBER_ID);
		}
		orderService.updateOrderDeliveryState(omsOrder);

		// OMS_ORDERPRODUCT 주문상품상태코드 - 출고지시대기 상태로 변경
		OmsOrderproduct omsOrderproduct = new OmsOrderproduct();
		omsOrderproduct.setOrderId(omsDeliveryaddress.getOrderId());
		omsOrderproduct.setSaleproductId(omsDeliveryaddress.getSaleProductId());
		omsOrderproduct.setOrderProductStateCd(BaseConstants.ORDER_PRODUCT_STATE_CD_READY);
		omsOrderproduct.setUpdDt(currentDate);
		if (StringUtils.isNotEmpty(SessionUtil.getLoginId())) {
			omsOrderproduct.setUpdId(SessionUtil.getLoginId());
		} else {
			omsOrderproduct.setUpdId(BaseConstants.NON_MEMBER_ID);
		}
		orderService.updateOrderProductState(omsOrderproduct);

		// OMS_DELIVERYADDRESS - 배송지 추가
		omsDeliveryaddress.setZipCd(omsDeliveryaddress.getHidZipCd());
		omsDeliveryaddress.setAddress1(omsDeliveryaddress.getHidAddress1());
		omsDeliveryaddress.setAddress2(omsDeliveryaddress.getHidAddress2());
		omsDeliveryaddress.setNote(omsDeliveryaddress.getNote());
		deliveryService.update(omsDeliveryaddress);
	}

	@RequestMapping(value = "/giftSendAuthSms" ,method = RequestMethod.POST)
	public void sendAuthSms(HttpServletRequest request, @RequestBody MmsMemberSearch search) throws Exception {
		// 인증번호 생성
		String authNumber = CommonUtil.makeRandomNumber(6);
		logger.debug("#### authNumber = " + authNumber);
		// 임시 세션 생성
		request.getSession().setAttribute("authNumber", authNumber);

		// 문자 발송
		TmsQueue tmsQueue = new TmsQueue();
		tmsQueue.setToPhone(search.getGiftPhone());
		tmsQueue.setMsgCode("131");
		tmsQueue.setMap1(authNumber);
		tmsService.sendTmsSmsQueue(tmsQueue);
	}

	@RequestMapping(value = "/giftCheckAuthSms" ,method = RequestMethod.POST)
	public boolean checkAuthSms(HttpServletRequest request, @RequestBody MmsMemberSearch search) throws Exception {
		// 인증번호 체크
		boolean authResult = false;
		if (StringUtils.isNotEmpty(search.getAuthNumber())) {
			String sessionAuthNumber = (String) request.getSession().getAttribute("authNumber");
			if (StringUtils.isNotEmpty(sessionAuthNumber)) {
				if (sessionAuthNumber.equals(search.getAuthNumber())) {
					authResult = true;
				}
			}
		}
		return authResult;
	}

}
