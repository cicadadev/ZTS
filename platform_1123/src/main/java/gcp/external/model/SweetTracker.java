package gcp.external.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SweetTracker {
	//운송장번호(공백또는 “-” 제거)
	private String	num;
	//택배사 코드
	private String code;
	//해당 건의 결과 전송에 쓰이는 식별 값
	private String	fid;
	//결과를 전달받을 URL
	private String callback_url;
	//발급받은 tier
	private String tier;
	//발급받은 key
	private String	key;
}
