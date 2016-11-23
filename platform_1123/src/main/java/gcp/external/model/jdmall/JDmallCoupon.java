package gcp.external.model.jdmall;

import lombok.Data;

@Data
public class JDmallCoupon {
	//쿠폰유형
	private String	coupon_type;
	//sku_id
	private String	sku_id;
	//쿠폰가격
	private String	coupon_price;
}
