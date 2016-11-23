package gcp.external.model.tmall;

import java.util.List;

import lombok.Data;

@Data
public class TmallOrder {
	//주문코드(티몰주문코드)
	private String			tid;
	//주문생성시간(yyyymmddhhiiss)
	private String			created;
	//결제시간(yyyymmddhhiiss)
	private String			pay_time;
	//주문실결제금액: 상품품목실결제금액(관세포함)합계 + 배송비
	private String			payment;
	//배송비: 결제한 배송비
	private String			post_fee;
	//할인금액합계: 상품할인합계(관세할인포함) + 주문할인
	private String			discount_fee;
	//티몰국제상점 주문 관세금액
	private String			order_tax_fee;
	//관세할인금액
	private String			duty_free;
	//주문상품(품목별)
	private List<TmallItem>	items;
}
