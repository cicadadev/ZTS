package gcp.external.model.tmall;

import lombok.Data;

@Data
public class TmallItem {
	//주문품목코드(티몰주문품목코드)
	private String	oid;
	//상품품목코드(업체상품품목코드)
	private String	sku_code;
	//상품명
	private String title;
	//판매가(할인전,관세미포함)단가
	private String	price;
	//상품품목실결제금액(관세포함)
	private String	payment;
	//상품품목수량
	private String	num;
	//상품할인금액(관세할인포함)
	private String	discount_fee;
	//배송유형(ICB/보세)
	private String	store_type;
	//관세(행우세)
	private String	sub_order_tax_fee;
	//관세세율
	private String	sub_order_tax_rate;
}
