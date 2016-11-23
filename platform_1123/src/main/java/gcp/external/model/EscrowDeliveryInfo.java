package gcp.external.model;



import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class EscrowDeliveryInfo {

	private String	mid;                 // 상점ID
	private String	oid;                 // 주문번호
	private String	productid;           // 상품ID
	private String	orderdate;           // 주문일자
	private String	dlvtype;             // 등록내용구분
	private String	rcvdate;             // 실수령일자
	private String	rcvname;             // 실수령인명
	private String	rcvrelation;         // 관계
	private String	dlvdate;             // 발송일자
	private String	dlvcompcode;         // 배송회사코드
	private String	dlvcomp;             // 배송회사명
	private String	dlvno;               // 운송장번호
	private String	dlvworker;           // 배송자명
	private String	dlvworkertel;        // 배송자전화번호
	private String	mertkey;             // LG유플러스에서 발급한 상점키
	
}
