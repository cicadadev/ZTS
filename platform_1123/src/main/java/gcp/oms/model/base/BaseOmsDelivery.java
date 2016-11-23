package gcp.oms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.oms.model.OmsClaimdelivery;
import gcp.oms.model.OmsErpif;
import gcp.oms.model.OmsOrderproduct;
import gcp.oms.model.OmsDeliveryaddress;
import gcp.oms.model.OmsOrdercoupon;
import java.math.BigDecimal;
import java.util.List;
import gcp.common.util.CodeUtil;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BaseOmsDelivery extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String orderId; //주문ID		[primary key, primary key, primary key, not null]
	private BigDecimal deliveryAddressNo; //배송지번호		[primary key, primary key, primary key, not null]
	private BigDecimal deliveryPolicyNo; //배송정책번호		[primary key, primary key, primary key, not null]
	private String storeId; //상점ID		[null]
	private String name; //배송정책명		[not null]
	private String deliveryServiceCd; //택배사코드		[not null]
	private BigDecimal deliveryFee; //배송비		[not null]
	private BigDecimal minDeliveryFreeAmt; //배송비무료최소주문금액		[null]
	private BigDecimal orderDeliveryFee; //주문배송비		[not null]
	private String deliveryCouponId; //배송비쿠폰ID		[null]
	private BigDecimal deliveryCouponIssueNo; //배송비쿠폰발행번호		[null]
	private BigDecimal deliveryCouponDcAmt; //배송비쿠폰할인금액		[not null]
	private BigDecimal applyDeliveryFee; //적용배송비		[not null]
	private String wrapTogetherYn; //합포장여부		[null]
	private BigDecimal orderWrapFee; //주문포장비		[not null]
	private String wrapCouponId; //포장비쿠폰ID		[null]
	private BigDecimal wrapCouponIssueNo; //포장비쿠폰발행번호		[null]
	private BigDecimal wrapCouponDcAmt; //포장비쿠폰할인금액		[null]
	private BigDecimal applyWrapFee; //적용포장비		[not null]

	private List<OmsClaimdelivery> omsClaimdeliverys;
	private List<OmsErpif> omsErpifs;
	private List<OmsOrderproduct> omsOrderproducts;
	private OmsDeliveryaddress omsDeliveryaddress;
	private OmsOrdercoupon omsOrdercoupon;

	public String getDeliveryServiceName(){
			return CodeUtil.getCodeName("DELIVERY_SERVICE_CD", getDeliveryServiceCd());
	}
}