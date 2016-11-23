package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.oms.model.OmsClaim;
import gcp.oms.model.OmsOrder;
import java.math.BigDecimal;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsPayment extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private BigDecimal paymentNo; //결제번호		[primary key, primary key, primary key, not null]
	private String orderId; //주문ID		[null]
	private String paymentMethodCd; //결제수단코드		[not null]
	private String paymentTypeCd; //결제유형코드		[not null]
	private String refundReasonCd; //환불사유코드		[null]
	private String majorPaymentYn; //주결제수단여부		[not null]
	private String paymentStateCd; //결제상태코드		[not null]
	private BigDecimal memberNo; //회원번호		[null]
	private BigDecimal depositNo; //예치금번호		[null]
	private String paymentBusinessCd; //결제사코드		[null]
	private String paymentBusinessNm; //결제사명		[null]
	private String accountNo; //계좌번호		[null]
	private String accountHolderName; //계좌주명		[null]
	private String depositorName; //입금자명		[null]
	private String refundAccountNo; //환불계좌번호		[null]
	private String creditcardNo; //신용카드번호		[null]
	private BigDecimal installmentCnt; //할부개월수		[null]
	private String interestFreeYn; //무이자여부		[not null]
	private String escrowYn; //에스크로여부		[not null]
	private String cashReceiptApprovalNo; //현금영수증승인번호		[null]
	private String cashReceiptTypeCd; //현금영수증종류		[null]
	private String mobilePhone; //결제휴대폰번호		[null]
	private String partialCancelYn; //부분취소가능여부		[not null]
	private BigDecimal paymentAmt; //결제금액||세금포함		[not null]
	private BigDecimal paymentFee; //결제수수료		[not null]
	private String paymentDt; //결제일시||환불완료일시		[null]
	private String cancelDt; //취소일시		[null]
	private String pgShopId; //PG상점ID		[null]
	private String pgApprovalNo; //PG승인번호		[null]
	private String pgCancelNo; //PG취소승인번호		[null]
	private BigDecimal claimNo; //클레임번호		[null]
	private String virtualAccountDepositOrder; //가상계좌입금순서		[null]
	private String virtualAccountDepositEndDt; //가상계좌입금마감일		[null]
	private String escrowIfYn; //에스크로연동여부		[null]
	private String escrowIfResult; //에스크로연동결과		[null]

	private OmsClaim omsClaim;
	private OmsOrder omsOrder;

	public String getPaymentMethodName(){
			return CodeUtil.getCodeName("PAYMENT_METHOD_CD", getPaymentMethodCd());
	}

	public String getPaymentTypeName(){
			return CodeUtil.getCodeName("PAYMENT_TYPE_CD", getPaymentTypeCd());
	}

	public String getRefundReasonName(){
			return CodeUtil.getCodeName("REFUND_REASON_CD", getRefundReasonCd());
	}

	public String getPaymentStateName(){
			return CodeUtil.getCodeName("PAYMENT_STATE_CD", getPaymentStateCd());
	}

	public String getPaymentBusinessName(){
			return CodeUtil.getCodeName("PAYMENT_BUSINESS_CD", getPaymentBusinessCd());
	}

	public String getCashReceiptTypeName(){
			return CodeUtil.getCodeName("CASH_RECEIPT_TYPE_CD", getCashReceiptTypeCd());
	}
}