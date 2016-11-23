package gcp.oms.model;

import java.math.BigDecimal;

import gcp.oms.model.base.BaseOmsClaimdelivery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsClaimdelivery extends BaseOmsClaimdelivery {
	/**
	 * UUID
	 */
	private static final long serialVersionUID = -5664119839799641753L;

	private String deliveryPolicyNm;
	private String deliveryAddress; // 수거지 주소
	private BigDecimal deliveryFee; // 정책배송비
	private BigDecimal tmpDeliveryFee;
}