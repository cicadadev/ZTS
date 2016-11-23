package gcp.oms.model.search;

import java.math.BigDecimal;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString
@Data
public class OmsPaymentSearch extends BaseSearchCondition {

	private static final long serialVersionUID = -6576315707762558071L;

	private String orderId; // 주문번호
	private String orderIds; // 주문번호
	private String orderProductNo; // 주문상품
	private String paymentStateCds; // 결제상태(환불상태)
	private String refundReasonCds; // 환불사유

	private String ordererType; // 주문자
	private String orderer;
	private String accountType; // 환불계좌
	private String account;

	private String callback;
	private String title;
	private String layer;
	private BigDecimal claimNo;
}
