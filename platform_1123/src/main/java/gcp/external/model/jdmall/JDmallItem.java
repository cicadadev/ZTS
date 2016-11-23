package gcp.external.model.jdmall;

import lombok.Data;

@Data
public class JDmallItem {
	//JD sku ID
	private String	sku_id;
	//단품번호
	private String	outer_sku_id;
	//단품명
	private String	sku_name;
	//판매단가
	private String	jd_price;
	//적립포인트
	private String	gift_point;
	//JD 상품ID
	private String	ware_id;
	//주문수량
	private String	item_total;
}
