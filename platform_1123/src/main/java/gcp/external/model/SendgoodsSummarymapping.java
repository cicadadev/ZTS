package gcp.external.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SendgoodsSummarymapping {
	//일련번호
	private int seq;
	//상품명
	private String	goodsNm;
	//자체코드
	private String	companyGoodsCd;
	//상품상태
	private String	status;
	//원가
	private String	goodsCost;
	//판매가
	private String	goodsPrice;
	//TAG(소비자가)
	private String	goodsConsumerPrice;
	//옵션내역
	private String	char1Val;
}
