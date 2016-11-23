package gcp.external.model.jdmall;

import java.util.List;

import lombok.Data;

@Data
public class JDmallOrder {
	//수정시간
	private String				modified;
	//고객명
	private String				customs;
	//오더번호
	private String				order_id;
	//상가ID
	private String				vender_id;
	//오더총금액
	private String				order_total_price;
	//오더금액-상가할인금액(seller_discount)
	private String				order_seller_price;
	//지불금액
	private String				order_payment;
	//운비
	private String				freight_price;
	//할인금액
	private String				seller_discount;
	//오더상태
	private String				order_state;
	//오더생성시간
	private String				order_start_time;
	//주문상품리스트
	private List<JDmallItem>	item_info_list;
	//쿠폰리스트
	private List<JDmallCoupon> coupon_detail_list;
}
