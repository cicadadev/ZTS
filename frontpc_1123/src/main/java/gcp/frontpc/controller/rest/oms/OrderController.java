package gcp.frontpc.controller.rest.oms;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import gcp.common.util.CcsUtil;
import gcp.common.util.FoSessionUtil;
import gcp.frontpc.common.util.FrontUtil;
import gcp.oms.model.OmsCart;
import gcp.oms.model.OmsOrder;
import gcp.oms.model.OmsPayment;
import gcp.oms.model.OmsPaymentif;
import gcp.oms.model.OmsPickup;
import gcp.oms.model.OmsRegulardelivery;
import gcp.oms.service.OrderService;
import gcp.oms.service.PickupService;
import gcp.oms.service.RegularService;
import intune.gsf.common.constants.BaseConstants;
import intune.gsf.common.utils.CommonUtil;
import intune.gsf.common.utils.Config;
import intune.gsf.common.utils.SessionUtil;
import kr.co.lgcns.module.lite.CnsPayWebConnector;

@RestController
@RequestMapping("api/oms/order")
public class OrderController {
	private static final Logger	logger	= LoggerFactory.getLogger(OrderController.class);
	@Autowired
	private OrderService orderService;

	@Autowired
	private RegularService		regularService;

	@Autowired
	private PickupService		pickupService;

	@RequestMapping(value = "/search", method = RequestMethod.POST)
	public Map<String, Object> orderSearch(@RequestBody OmsOrder omsOrder) {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
		omsOrder = orderService.getOrderLoginSearch(omsOrder);
		if (omsOrder == null) {
			result.put(BaseConstants.RESULT_MESSAGE, "주문정보가 없습니다.");
		} else {
			result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
			result.put("omsOrder", omsOrder);
		}
		return result;
	}

	@RequestMapping(value = "/checkOrder", method = RequestMethod.POST)
	public Map<String, Object> checkOrder(@ModelAttribute OmsOrder omsOrder, HttpServletRequest request) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
		
		String giftYn = omsOrder.getGiftYn();
		if ("Y".equals(giftYn)) {
			if (!FoSessionUtil.isMemberLogin()) {
				result.put(BaseConstants.RESULT_MESSAGE, "선물하기는 회원만 가능합니다.");
				return result;
			}
		}

		SessionUtil.setSession(request, BaseConstants.SESSION_KEY_CHECK_ORDER, "true");

		String orderLoginReturn = omsOrder.getOrderLoginReturn();
		if ("true".equals(orderLoginReturn)) {
			OmsOrder omsOrderSession = (OmsOrder) SessionUtil.getSession(request, BaseConstants.SESSION_KEY_MOBILE_ORDER);
			if (CommonUtil.isNotEmpty(omsOrderSession)) {
				omsOrder = omsOrderSession;
			}
		}

		CcsUtil.setSessionLoginInfo(omsOrder, true);
		BigDecimal memberNo = omsOrder.getMemberNo();

		String paramCartProductNos = omsOrder.getCartProductNos();
		String orderStat = omsOrder.getOrderStat();
		String paramSelectPresent = omsOrder.getSelectPresent();

		if (CommonUtil.isNotEmpty(paramCartProductNos)) {
			String cartProductNos = CommonUtil.convertInParam(paramCartProductNos);
			OmsCart omsCart = new OmsCart();
			omsCart.setCartProductNos(cartProductNos);
			omsOrder = orderService.getOmsOrderByCart(omsCart);

		} else {
			/*
			 * omsOrder - omsOrderproducts - omsOrderproducts
			 */
		}

		omsOrder.setMemberNo(memberNo);
		omsOrder.setDeviceTypeCd(FoSessionUtil.getDeviceTypeCd(request));
		omsOrder.setOrderStat(orderStat);
		try {
			omsOrder = orderService.getOrderList(omsOrder);
		} catch (Exception e) {
			e.printStackTrace();
			result.put(BaseConstants.RESULT_MESSAGE, e.getMessage());
			return result;
		}

		omsOrder.setCartProductNos(paramCartProductNos);
		omsOrder.setSelectPresent(paramSelectPresent);

		result.put("omsOrder", omsOrder);
		String viewName = "";
		if ("PRESENT".equals(omsOrder.getOrderStat())) {	//사은품이 있으면 사은품 선택 페이지로 간다.
			viewName = "presentList";
		} else {
			viewName = "sheet";
		}
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		result.put("view", viewName);
		return result;
	}

	@RequestMapping(value = "/save", method = RequestMethod.POST)
	public Map<String, Object> saveOrder(@ModelAttribute OmsOrder omsOrder, HttpServletRequest request) throws Exception {

		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		String deviceTypeCd = FoSessionUtil.getDeviceTypeCd(request);	//디바이스유형
		omsOrder.setDeviceTypeCd(deviceTypeCd);
		CcsUtil.setSessionLoginInfo(omsOrder, true);

		String orderTypeCd = omsOrder.getOrderTypeCd();

		if (!FoSessionUtil.isMemberLogin() ) {

			if ("ORDER_TYPE_CD.GIFT".equals(orderTypeCd)) {
				result.put(BaseConstants.RESULT_MESSAGE, "선물하기는 회원만 가능합니다.");
				return result;
			}

			String orderPwd = omsOrder.getOrderPwd();
			omsOrder.setMemberId(null);
			if (CommonUtil.isEmpty(orderPwd)) {
				result.put(BaseConstants.RESULT_MESSAGE, "비회원 비밀번호가 없습니다.");
				return result;
			} else {	//비회원 비밀번호 암호화.
				ShaPasswordEncoder encoder = new ShaPasswordEncoder();
				omsOrder.setOrderPwd(encoder.encodePassword(orderPwd, null));
			}
		}

		//주문 생성
		try {

			createKakaoRequestParam(omsOrder, request);

			result = orderService.createOrder(omsOrder);

			String resultFlag = (String) result.get(BaseConstants.RESULT_FLAG);
			if (BaseConstants.RESULT_FLAG_SUCCESS.equals(resultFlag)) {
				//ERPIF											
				orderService.insertOmsERPIFNewTx(omsOrder);
			}

		} catch (Exception e) {
			e.printStackTrace();
			result.put(BaseConstants.RESULT_MESSAGE, e.getMessage());
		}
		return result;
	}

	@RequestMapping(value = "/saveMobile", method = RequestMethod.POST)
	public Map<String, Object> saveMobileOrder(@ModelAttribute OmsPaymentif omsPaymentif) throws Exception {

		HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		OmsOrder omsOrder = (OmsOrder) SessionUtil.getSession(request, BaseConstants.SESSION_KEY_MOBILE_ORDER);

		String deviceTypeCd = FoSessionUtil.getDeviceTypeCd(request);	//디바이스유형
		omsOrder.setDeviceTypeCd(deviceTypeCd);
		CcsUtil.setSessionLoginInfo(omsOrder, true);
		OmsPaymentif orgPi = omsOrder.getOmsPayments().get(0).getOmsPaymentif();

		String CST_MID = orgPi.getCST_MID();
		omsPaymentif.setCST_MID(CST_MID);

		omsOrder.getOmsPayments().get(0).setOmsPaymentif(omsPaymentif);

		if (!FoSessionUtil.isMemberLogin()) {
			String orderPwd = omsOrder.getOrderPwd();
			omsOrder.setMemberId(null);
			if (CommonUtil.isEmpty(orderPwd)) {
				result.put(BaseConstants.RESULT_MESSAGE, "비회원 비밀번호가 없습니다.");
				return result;
			} else {	//비회원 비밀번호 암호화.
				ShaPasswordEncoder encoder = new ShaPasswordEncoder();
				omsOrder.setOrderPwd(encoder.encodePassword(orderPwd, null));
			}
		}

		//주문 생성
		try {
			result = orderService.createOrder(omsOrder);
		} catch (Exception e) {
			result.put(BaseConstants.RESULT_MESSAGE, e.getMessage());
		}

		SessionUtil.removeSession(request, BaseConstants.SESSION_KEY_MOBILE_ORDER);

		return result;
	}

	@RequestMapping(value = "/saveSession", method = RequestMethod.POST)
	public Map<String, Object> saveSessionOrder(@ModelAttribute OmsOrder omsOrder, HttpServletRequest request) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
		SessionUtil.setSession(request, BaseConstants.SESSION_KEY_MOBILE_ORDER, omsOrder);
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		return result;
	}

	@RequestMapping(value = "/removeSession", method = RequestMethod.POST)
	public Map<String, Object> removeSessionOrder(@ModelAttribute OmsOrder omsOrder, HttpServletRequest request)
			throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);
		SessionUtil.removeSession(request, BaseConstants.SESSION_KEY_MOBILE_ORDER);
		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		return result;
	}

	@RequestMapping(value = "/pickup/save", method = RequestMethod.POST)
	public Map<String, Object> savePickup(@ModelAttribute OmsPickup omsPickup) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		CcsUtil.setSessionLoginInfo(omsPickup);

		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_FAIL);

		try {
			result = pickupService.savePickup(omsPickup);
		} catch (Exception e) {
			e.printStackTrace();
			result.put(BaseConstants.RESULT_MESSAGE, e.getMessage());
			return result;
		}
		
		if (!BaseConstants.RESULT_FLAG_SUCCESS.equals(result.get(BaseConstants.RESULT_FLAG))) {
			return result;
		}

		result.put(BaseConstants.RESULT_FLAG, BaseConstants.RESULT_FLAG_SUCCESS);
		return result;
	}

	@RequestMapping(value = "/billing/save", method = RequestMethod.POST)
	public Map<String, Object> saveBilling(@ModelAttribute OmsPaymentif omsPaymentif, HttpServletRequest request)
			throws Exception {
		BigDecimal memberNo = SessionUtil.getMemberNo();
		omsPaymentif.setDeviceTypeCd(FoSessionUtil.getDeviceTypeCd(request));
		return regularService.saveBilling(memberNo, omsPaymentif);
	}

	@RequestMapping(value = "/regular/save", method = RequestMethod.POST)
	public Map<String, Object> saveRegulardelivery(@ModelAttribute OmsRegulardelivery omsRegulardelivery) throws Exception {
		Map<String, Object> result = new HashMap<String, Object>();
		CcsUtil.setSessionLoginInfo(omsRegulardelivery);
		omsRegulardelivery.setRegularDeliveryId(orderService.getNewOrderId());
		try {

			result = regularService.saveRegulardelivery(omsRegulardelivery);

		} catch (Exception e) {
			e.printStackTrace();
			result.put(BaseConstants.RESULT_MESSAGE, e.getMessage());
		}
		return result;
	}


	@RequestMapping(value = "/calcRegularDate", method = RequestMethod.POST)
	public Map<String, Object> calcRegularDate(@RequestBody Map map) {
		return regularService.calcRegularDate(map);
	}

	private void createKakaoRequestParam(OmsOrder omsOrder, HttpServletRequest request) throws Exception {
		//결제
		for (OmsPayment orderPay : omsOrder.getOmsPayments()) {
			if ("PAYMENT_METHOD_CD.KAKAO".equals(orderPay.getPaymentMethodCd())) {

				CnsPayWebConnector connector = new CnsPayWebConnector();
//				Kakao kakao = orderPay.getKakao();

//				BeanInfo beanInfo = Introspector.getBeanInfo(Kakao.class);
//				for (PropertyDescriptor propertyDesc : beanInfo.getPropertyDescriptors()) {
//					String propertyName = propertyDesc.getName();
//					Object value = propertyDesc.getReadMethod().invoke(kakao);
//					request.getParameterMap().put(propertyName, new String[] { (String) value });
//				}
				// 1. 로그 디렉토리 생성 : cnsPayHome/log 로 생성
				connector.setLogHome(Config.getString("kakao.log.path"));
				connector.setCnsPayHome(Config.getString("kakao.config.path"));

				Map map = FrontUtil.getParameterMap(request);
//				orderPay.getKakao().setRequestParam(map.toString());
				// 2. 요청 페이지 파라메터 셋팅 (controller에서 setting)
				connector.setRequestData(request);
				String paymentNo = (String) map.get("merchantTxnNum");
//				logger.debug("orderPay paymentNo : "+paymentNo);
				orderPay.setPaymentNo(new BigDecimal(paymentNo));
				orderPay.setConnector(connector);

			}
		}

	}

}
