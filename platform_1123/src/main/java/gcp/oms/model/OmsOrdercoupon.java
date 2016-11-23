package gcp.oms.model;

import java.math.BigDecimal;

import gcp.oms.model.base.BaseOmsOrdercoupon;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsOrdercoupon extends BaseOmsOrdercoupon {
	/**
	 * UUID
	 */
	private static final long serialVersionUID = 1L;
	private String couponIssueStateCd;
	private String dealApplyYn;	//딜적용가능여부
	private BigDecimal applyDcAmt;	//할인적용금액
	private BigDecimal controlNo;
	private BigDecimal refundCouponAmt; // 쿠폰환불금액
	
	private String insName;		// 등록자
	private String updName;		// 수정자
		
	private String period; // 쿠폰유효기간
	private String dealId;	//적용쿠폰딜
}