package gcp.external.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SweetTrackerResult {
	//성공여부(true, false)
	private boolean success;
	//운송장 번호
	private String num;
	//식별 값
	private String	fid;
	//에러메시지 코드
	private String e_code;
	//에러메시지
	private String	e_message;
}
