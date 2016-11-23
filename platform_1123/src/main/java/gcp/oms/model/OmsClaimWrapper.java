package gcp.oms.model;

import java.math.BigDecimal;
import java.util.List;

import intune.gsf.model.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class OmsClaimWrapper extends BaseEntity {

	/**
	 * UUID
	 */
	private static final long serialVersionUID = -8710944734150776631L;

	private String claimTypeCd;

	private String claimStateCd;

	private String claimPath;
	
	private String isAllCancel;
	
	private BigDecimal memberNo;
	
	private OmsClaim omsClaim;

	private List<OmsPayment> omsPayments;

	private List<OmsOrderproduct> omsOrderproducts;

	private List<OmsOrdercoupon> omsOrdercoupons;

	private OmsClaimdelivery omsClaimdelivery; // 반품배송 입고

	private OmsDeliveryaddress omsDeliveryaddress; // 교환,재배송 출고
}