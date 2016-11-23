package gcp.external.model;



import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class DeliveryTracking {
	//인증키(현재사용안함)
	private String	secret_value;
	//운송장번호
	private String	fid;
	//결과를 전달받을 URL
	private String invoice_no;
	//배송단계(1~6단계)
	private String level;
	//택배사 처리시간
	private String time_trans;
	//스윗트래커 등록시간
	private String time_sweet;
	//택배 위치
	private String where;
	//사업소 기반 전화번호
	private String telno_office;
	//배송기사 전화번호
	private String telno_man;
	//배송상세 정보
	private String details;
	//수취인 주소
	private String recv_addr;
	//수취인 이름
	private String recv_name;
	//발신인 이름
	private String send_name;
}
