package gcp.pms.model.base;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import intune.gsf.model.BaseEntity;
import gcp.pms.model.PmsProduct;
import java.math.BigDecimal;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class BasePmsProductprice extends BaseEntity{
	private static final long serialVersionUID = 1L;

	private String storeId; //상점ID		[primary key, primary key, primary key, not null]
	private String productId; //상품ID		[primary key, primary key, primary key, not null]
	private BigDecimal salePrice; //판매가		[not null]
	private String pointYn; //포인트여부		[not null]
	private String deliveryFeeFreeYn; //배송비무료여부		[not null]
	private String couponId; //쿠폰ID		[null]
	private BigDecimal prestigeSalePrice; //프리스티지판매가		[not null]
	private String prestigePointYn; //프리스티지포인트여부		[not null]
	private String prestigeDeliveryFeeFreeYn; //프리스티지배송비무료여부		[not null]
	private String prestigeCouponId; //프리스티지쿠폰ID		[null]
	private BigDecimal vipSalePrice; //VIP판매가		[not null]
	private String vipPointYn; //VIP포인트여부		[not null]
	private String vipDeliveryFeeFreeYn; //VIP배송비무료여부		[not null]
	private String vipCouponId; //VIP쿠폰ID		[null]
	private BigDecimal goldSalePrice; //GOLD판매가		[not null]
	private String goldPointYn; //GOLD포인트여부		[not null]
	private String goldDeliveryFeeFreeYn; //GOLD배송비무료여부		[not null]
	private String goldCouponId; //GOLD쿠폰ID		[null]
	private BigDecimal silverSalePrice; //실버판매가		[not null]
	private String silverPointYn; //실버포인트여부		[not null]
	private String silverDeliveryFeeFreeYn; //실버배송비무료여부		[not null]
	private String silverCouponId; //실버쿠폰ID		[null]
	private BigDecimal familySalePrice; //패밀리판매가		[not null]
	private String familyPointYn; //패밀리포인트여부		[not null]
	private String familyDeliveryFeeFreeYn; //패밀리배송비무료여부		[not null]
	private String familyCouponId; //패밀리쿠폰ID		[null]
	private BigDecimal welcomeSalePrice; //웰컴판매가		[not null]
	private String welcomePointYn; //웰컴포인트여부		[not null]
	private String welcomeDeliveryFeeFreeYn; //웰컴배송비무료여부		[not null]
	private String welcomeCouponId; //웰컴쿠폰ID		[null]

	private PmsProduct pmsProduct;
}