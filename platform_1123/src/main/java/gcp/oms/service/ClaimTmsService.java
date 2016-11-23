package gcp.oms.service;

import java.math.BigDecimal;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gcp.ccs.model.CcsMessage;
import gcp.ccs.service.BaseService;
import gcp.external.model.TmsQueue;
import gcp.external.service.TmsService;
import gcp.oms.model.OmsClaimWrapper;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.OmsPayment;
import intune.gsf.common.utils.ByteUtil;

/**
 * 
 * @Pagckage Name : gcp.oms.service
 * @FileName : ClaimTmsService.java
 * @author : dennis
 * @date : 2016. 10. 24.
 * @description : 클레임관련 TMS
 */
@Service("claimTmsService")
public class ClaimTmsService extends BaseService {
	private static final Logger logger = LoggerFactory.getLogger(ClaimTmsService.class);

	@Autowired
	private TmsService tmsService;

	/**
	 * 
	 * @Method Name : createClaimTms
	 * @author : victor
	 * @date : 2016. 10. 24.
	 * @description : 클레임관련 SMS
	 * 
	 * <pre>
	 *	103 : 주문취소완료 (현금환불 X) 
	 *	104 : 주문취소완료 (현금환불 O) 
	 *	107 : 픽업취소 (주문자) 
	 *	108 : 픽업취소 (매장직원) 
	 *	121 : 교환접수처리(자사상품) 
	 *	122 : 교환접수처리(업체상품) 
	 *	123 : 반품접수처리(자사상품) 
	 *	124 : 반품접수처리(업체상품) 
	 *	125 : 반품완료(신용카드결제주문) 
	 *	126 : 반품완료(휴대폰결제주문) 
	 *	127 : 반품완료(현금환불) 
	 *	134 : 반품완료(가상계좌/계좌이체주문)
	 *	128 : 재배송접수
	 * </pre>
	 * @param omsClaimWrapper
	 * @param code
	 */
	public CcsMessage createClaimTms(OmsClaimWrapper omsClaimWrapper, String code) {
		CcsMessage ccsmessage = new CcsMessage();
		ccsmessage.setSuccess(true);

		try {

			List<OmsOrderproduct> omsOrderproducts = omsClaimWrapper.getOmsOrderproducts();
			List<OmsPayment> omsPayments = omsClaimWrapper.getOmsPayments();
			String claimType = omsClaimWrapper.getClaimTypeCd();
			String claimStateCd = omsClaimWrapper.getClaimStateCd();

			int productCount = 0;
			String productName = "";
			String orderId = "";
			String phone2 = "";
			
			// 접수일때
			if ("CLAIM_STATE_CD.ACCEPT".equals(claimStateCd)) {
				for (OmsOrderproduct omsOrderproduct : omsOrderproducts) {
					if (omsOrderproduct.getOmsClaimproduct() != null) {
						// OmsClaimproduct omsClaimproduct = omsOrderproduct.getOmsClaimproduct();
						productName = omsOrderproduct.getProductName();
						orderId = omsOrderproduct.getOrderId();
						productCount++;

						phone2 = omsOrderproduct.getOmsOrder().getPhone2();
						if ("ORDER_PRODUCT_TYPE_CD.GENERAL".equals(omsOrderproduct.getOrderProductTypeCd())
								|| "ORDER_PRODUCT_TYPE_CD.SET".equals(omsOrderproduct.getOrderProductTypeCd())) {
							if ("SALE_TYPE_CD.PURCHASE".equals(omsOrderproduct.getSaleTypeCd())) {
								if ("CLAIM_TYPE_CD.EXCHANGE".equals(claimType)) {
									code = "122";
								} else if ("CLAIM_TYPE_CD.RETURN".equals(claimType)) {
									if (!"CLAIM_STATE_CD.COMPLETE".equals(claimStateCd)) {
										code = "124";
									}
								}
							}
						}
					}
				}
			}

			BigDecimal bCancelAmt = BigDecimal.ZERO;
			if ("CLAIM_TYPE_CD.CANCEL".equals(claimType)) {
				if (omsPayments != null) {
					for (OmsPayment omsPayment : omsPayments) {
						if ("PAYMENT_METHOD_CD.CASH".equals(omsPayment.getPaymentMethodCd())) {
							if (omsPayment.getPaymentAmt().compareTo(BigDecimal.ZERO) > 0) {
								bCancelAmt = bCancelAmt.add(omsPayment.getPaymentAmt());
							}
						}
					}
				}
			}

			productName = ByteUtil.getMaxByteString(productName, 32);

			TmsQueue tmsQueue = new TmsQueue();
			tmsQueue.setToPhone(phone2);
			tmsQueue.setMsgCode(code);
			tmsQueue.setMap1(productName);
			tmsQueue.setMap2(productCount > 1 ? " 외 " + (productCount - 1) + " 건" : "");
			tmsQueue.setMap3(orderId);

			switch (code) {
				case "103": // "주문취소 즉시 - 현금환불 발생하지 않는 경우
					tmsQueue.setSubject("[0to7.com 주문취소]");
					break;
				case "104":// "주문취소 즉시 - 현금환불 발생하는 경우
					tmsQueue.setSubject("[0to7.com 주문취소]");
					tmsQueue.setMap4(bCancelAmt.toString());
					break;
				case "107":
					// [0to7.com 픽업신청취소] ▶상품명: OOO 외 0건 ▶신청번호: OOO ▶매장명: OOO
					// [0to7.com 픽업신청취소] ▶상품명: ${MAP1} ${MAP2} ▶신청번호: ${MAP3} ▶매장명: ${MAP4}
					break;
				case "108":
					// [0to7.com 픽업신청취소] 매장픽업주문이 취소되었습니다. ▶상품명: OOO 외 0건 ▶신청번호: OOO ▶매장명: OOO
					// [0to7.com 픽업신청취소] 매장픽업주문이 취소되었습니다. ▶상품명: ${MAP1} ${MAP2} ▶신청번호: ${MAP3} ▶매장명: ${MAP4}
					break;
				case "121": // 자사 교환
					tmsQueue.setSubject("[0to7.com 교환접수]");
					break;
				case "122": // 업체 교환
					tmsQueue.setSubject("[0to7.com 교환접수]");
					break;
				case "123": // 자사반품
					tmsQueue.setSubject("[0to7.com 반품접수]");
					break;
				case "124": // 업체반품
					tmsQueue.setSubject("[0to7.com 반품접수]");
					break;
				case "125": // 반품완료 즉시 - 신용카드 승인취소 (현금환불X)
					tmsQueue.setSubject("[0to7.com 반품완료]");
					break;
				case "126": // 반품완료 즉시 - 휴대폰 승인취소 (현금환불X) - 휴대폰결제 당월 전체환불
					tmsQueue.setSubject("[0to7.com 반품완료]");
					break;
				case "134": // 반품완료 즉시 - 가상계좌/계좌이체 승인취소(현금환불X) - (60일 이내)
					tmsQueue.setSubject("[0to7.com 반품완료]");
					break;
				case "127": // 반품완료 즉시 - 현금 환불 - 휴대폰결제 당월 부분환불, 휴대폰결제 익월 환불 - 가상계좌/계좌이체 입금 후 환불
					tmsQueue.setSubject("[0to7.com 반품완료]");
					break;
				case "128":
					tmsQueue.setSubject("[0to7.com 재배송접수]");
					break;
			}

			tmsService.sendTmsSmsQueue(tmsQueue);
		} catch (Exception e) {
			if (logger.isDebugEnabled()) {
				logger.debug("[SMS 발신에러] : {}", e.getMessage());
			}
			ccsmessage.setSuccess(false);
			ccsmessage.setResultMessage(e.getMessage());
		}
		return ccsmessage;
	}
}
