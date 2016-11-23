package gcp.admin.controller.rest.oms;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import gcp.ccs.model.CcsMessage;
import gcp.oms.model.OmsPayment;
import gcp.oms.model.search.OmsPaymentSearch;
import gcp.oms.service.PaymentService;
import intune.gsf.common.utils.DateUtil;
import intune.gsf.common.utils.SessionUtil;

@RestController
@RequestMapping("api/oms")
public class PaymentController {

	@Autowired
	private PaymentService paymentService;
	
	@RequestMapping(value = { "/payment/list" ,"/refund/list" } ,method = { RequestMethod.GET ,RequestMethod.POST })
	public List<OmsPayment> selectList(@RequestBody OmsPaymentSearch omsPaymentSearch) throws Exception {
		omsPaymentSearch.setStoreId(SessionUtil.getStoreId());
//		List<OmsPayment> paymentList = paymentService.selectList(omsPaymentSearch.getSearchId(), omsPaymentSearch);
//		return paymentList;
		return paymentService.selectList(omsPaymentSearch.getSearchId(), omsPaymentSearch);
	}

//	@RequestMapping(value = "/payment" ,method = { RequestMethod.GET })
//	public Object selectOne(@RequestBody OmsPaymentSearch omsPaymentSearch) throws Exception {
//		return paymentService.selectOne(omsPaymentSearch.getSearchId(), omsPaymentSearch);
//	}

	@RequestMapping(value = "/payment" ,method = { RequestMethod.GET })
	public OmsPayment selectOne(@RequestParam("orderIds") String orderIds) throws Exception {
		return paymentService.selectOne(orderIds);
	}

	@RequestMapping(value = { "/payment" ,"/refund" } ,method = RequestMethod.POST)
	public CcsMessage insert(@RequestBody OmsPayment omsPayment) throws Exception {
		CcsMessage ccsmessage = new CcsMessage();
		try {
			int updCnt = paymentService.insertRefundByDeposit(omsPayment);
			if (updCnt > 0) {
				ccsmessage.setSuccess(true);
				ccsmessage.setResultMessage("예치금 환불을 성공적으로 등록하였습니다.");
			} else {
				ccsmessage.setSuccess(false);
				ccsmessage.setResultMessage("예치금 환불 등록 중 에러가 발생 하였습니다.");
			}
		} catch (Exception e) {
			ccsmessage.setSuccess(false);
			ccsmessage.setResultMessage(e.getMessage());
		}
		
		return ccsmessage;
	}

	@RequestMapping(value = { "/payment" ,"/refund" } ,method = RequestMethod.PUT)
	public CcsMessage udpate(@RequestBody List<OmsPayment> omsPayments) throws Exception {
		String userId = SessionUtil.getLoginId();
		String currentDate = DateUtil.getCurrentDate(DateUtil.FORMAT_1);

		CcsMessage ccsmessage = new CcsMessage();
		try {
			int updCnt = 0;
			for (OmsPayment payment : omsPayments) {
				if ("PAYMENT_STATE_CD.REFUND_CANCEL".equals(payment.getPaymentStateCd())) {
					payment.setCancelDt(currentDate);
				} else if ("PAYMENT_STATE_CD.REFUND".equals(payment.getPaymentStateCd())) {
					payment.setPaymentDt(currentDate);
				}
//				if (payment.getOmsOrder() != null && !StringUtils.isEmpty(payment.getOmsOrder().getMemberNo())) {
//					payment.setMemberNo(payment.getOmsOrder().getMemberNo());
//				}
				payment.setUpdDt(currentDate);
				payment.setUpdId(userId);

				updCnt += paymentService.update(payment);
			}
			if (updCnt > 0) {
				ccsmessage.setSuccess(true);
			} else {
				ccsmessage.setSuccess(false);
				ccsmessage.setResultMessage("처리 중 에러가 발생 하였습니다.");
			}
		} catch (Exception e) {
			ccsmessage.setSuccess(false);
			ccsmessage.setResultMessage(e.getMessage());
		}
		return ccsmessage;
	}
	// @RequestMapping(value = "/refund" ,method = RequestMethod.PUT)
	// public int update(@RequestBody OmsPayment omsPayment) throws Exception {
	// omsPayment.setUpdDt(DateUtil.getCurrentDate(DateUtil.FORMAT_1));
	// omsPayment.setUpdId(SessionUtil.getLoginId());
	// return paymentService.update("oms.payment.updatePayment", omsPayment);
	// }
	
	@RequestMapping(value = "/payment/account/certify" ,method = { RequestMethod.POST })
	public CcsMessage accountCertify(@RequestBody OmsPayment omsPayment) throws Exception {
		return paymentService.accountCertify(omsPayment);
	}

}
