package gcp.oms.model;

import java.math.BigDecimal;

import gcp.common.util.CodeUtil;
import intune.gsf.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsPaymentsettle extends BaseEntity {
	/**
	 * UUID
	 */
	private static final long serialVersionUID = 8524159653195628619L;

	//PG사
	private String pgCompany;
	//PG 상점ID
	private String pgShopId;
	//주문ID
	private String orderId;
	//결제수단코드
	private String paymentMethodCd;
	//결제일시, 환불완료일시
	private String paymentDt;
	//거래유형코드
	private String transTypeCd;
	//결제금액
	private BigDecimal paymentAmt;
	//수수료(VAT 포함)
	private BigDecimal paymentFee;
	//지급일시
	private String settleDt;
	//취소일시
	private String cancelDt;

	//승인번호
	private String pgApprovalNo;
	//주문일시
	private String orderDt;
	//결제금액: 주문결제데이터
	private BigDecimal approvalAmt;
	//입금예정금액: 결제금액 - 수수료
	private BigDecimal depositAmt;
	//오차금액
	private BigDecimal errorAmt;

	public String getPaymentMethodName(){
		return CodeUtil.getCodeName("PAYMENT_METHOD_CD", getPaymentMethodCd());
	}
}
