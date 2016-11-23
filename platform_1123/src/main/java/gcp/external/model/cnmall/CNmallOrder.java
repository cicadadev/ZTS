package gcp.external.model.cnmall;

import lombok.Data;

@Data
public class CNmallOrder {
	//주문번호
	private String	orderId;
	//partner_trans_id
	private String	partnerTransId;
	//alipay_trans_id
	private String	alipayTransId;
	//주문 sequence
	private String	odIx;
	//쇼핑몰명
	private String	mallId;
	//주문날짜
	private String	orderDate;
	//상품이름
	private String	productName;
	//수량
	private String	saleCnt;
	//단품ID
	private String	skuAlias;
	//통화
	private String	currencyCode;
	//외화단가
	private String	currencyPrice;
}
