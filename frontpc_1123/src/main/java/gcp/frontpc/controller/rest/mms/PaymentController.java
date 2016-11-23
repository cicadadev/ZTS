package gcp.frontpc.controller.rest.mms;

import java.math.BigDecimal;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsMessage;
import gcp.common.util.FoSessionUtil;
import gcp.oms.model.OmsPayment;
import gcp.oms.service.ClaimService;
import gcp.oms.service.PaymentService;
import intune.gsf.common.exceptions.ServiceException;
import intune.gsf.common.utils.SessionUtil;

@RestController("mmsPaymentRestController")
@RequestMapping({ "api/mms/mypage/payment" ,"api/ccs/guest/payment" })
public class PaymentController {

	@Autowired
	private PaymentService paymentService;

	@Autowired
	private ClaimService claimService;

	/**
	 * 결제수단 변경
	 * 
	 * @Method Name : update
	 * @author : victor
	 * @date : 2016. 11. 3.
	 * @description :
	 *
	 * @param omsPayment
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/update" ,method = RequestMethod.POST)
	public CcsMessage update(@ModelAttribute OmsPayment omsPayment, HttpServletRequest request) throws Exception {
		CcsMessage ccsmessage = new CcsMessage();
		ccsmessage.setSuccess(false);
		ccsmessage.setResultCode("-999");
		ccsmessage.setResultMessage("결제 타입변경 중 알수 없는 에러가 발생하였습니다.");

		String deviceTypeCd = FoSessionUtil.getDeviceTypeCd(request); // 디바이스유형
		// omsOrder.setDeviceTypeCd(deviceTypeCd);
		// CcsUtil.setSessionLoginInfo(omsOrder, true);

		if (!FoSessionUtil.isMemberLogin()) {
			// String orderPwd = omsOrder.getOrderPwd();
			// omsOrder.setMemberId(null);
			// if (CommonUtil.isEmpty(orderPwd)) {
			// result.put(BaseConstants.RESULT_MESSAGE, "비회원 비밀번호가 없습니다.");
			// return result;
			// } else { // 비회원 비밀번호 암호화.
			// ShaPasswordEncoder encoder = new ShaPasswordEncoder();
			// omsOrder.setOrderPwd(encoder.encodePassword(orderPwd, null));
			// }
		}

		// 주문 생성
		try {
			if ("PAYMENT_METHOD_CD.KAKAO".equals(omsPayment.getPaymentMethodCd())) {
				paymentService.createKakaoRequestParam(omsPayment, request);
				omsPayment.setPaymentAmt(new BigDecimal(omsPayment.getKakao().getAmt()));
				omsPayment.setPaymentNo(new BigDecimal(omsPayment.getKakao().getMerchantTxnNum()));
			} else {
				omsPayment.setOrderId(omsPayment.getOmsPaymentif().getOrderId());
				omsPayment.setPaymentAmt(new BigDecimal(omsPayment.getOmsPaymentif().getLGD_AMOUNT()));
				omsPayment.setPaymentNo(new BigDecimal(omsPayment.getOmsPaymentif().getLGD_OID()));
			}
			omsPayment.setUpdId(SessionUtil.getLoginId());
			ccsmessage = paymentService.updatePaymentmethod(omsPayment);
		} catch (ServiceException se) {
			ccsmessage.setSuccess(false);
			ccsmessage.setResultMessage(se.getMessage());
		} catch (Exception e) {
			ccsmessage.setSuccess(false);
		}
		return ccsmessage;
	}

	/**
	 * 교환상품 추가결제
	 * 
	 * @Method Name : insert
	 * @author : victor
	 * @date : 2016. 11. 3.
	 * @description :
	 *
	 * @param omsPayment
	 * @param request
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/insert" ,method = RequestMethod.POST)
	public CcsMessage insert(@ModelAttribute OmsPayment omsPayment, HttpServletRequest request) throws Exception {
		CcsMessage ccsmessage = new CcsMessage();
		ccsmessage.setSuccess(false);
		ccsmessage.setResultCode("-999");
		ccsmessage.setResultMessage("추가결제 중 알수 없는 에러가 발생하였습니다.");

		// 주문 생성
		try {
			if ("PAYMENT_METHOD_CD.KAKAO".equals(omsPayment.getPaymentMethodCd())) {
				paymentService.createKakaoRequestParam(omsPayment, request);
				omsPayment.setPaymentAmt(new BigDecimal(omsPayment.getKakao().getAmt()));
				omsPayment.setPaymentNo(new BigDecimal(omsPayment.getKakao().getMerchantTxnNum()));
			} else {
				omsPayment.setOrderId(omsPayment.getOmsPaymentif().getOrderId());
				omsPayment.setPaymentAmt(new BigDecimal(omsPayment.getOmsPaymentif().getLGD_AMOUNT()));
				omsPayment.setPaymentNo(new BigDecimal(omsPayment.getOmsPaymentif().getLGD_OID()));
			}

			omsPayment.setMemberNo(SessionUtil.getMemberNo());
			omsPayment.setUpdId(SessionUtil.getLoginId());
			omsPayment.setPaymentTypeCd("PAYMENT_TYPE_CD.ADDPAYMENT");// 결제유형(추가결제)
			ccsmessage = claimService.updateExchange(omsPayment);
		} catch (ServiceException se) {
			ccsmessage.setSuccess(false);
			ccsmessage.setResultMessage(se.getMessage());
		} catch (Exception e) {
			ccsmessage.setSuccess(false);
		}
		return ccsmessage;
	}
	
	/**
	 * @Method Name : accountCertify
	 * @author : ian
	 * @date : 2016. 9. 12.
	 * @description : 환불 계좌 인증
	 *
	 * @param omsPayment
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "/account/certify" ,method = { RequestMethod.POST })
	public CcsMessage accountCertify(@RequestBody OmsPayment omsPayment) throws Exception {
		return paymentService.accountCertify(omsPayment);
	}	
}
