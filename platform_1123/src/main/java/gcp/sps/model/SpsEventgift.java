package gcp.sps.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.sps.model.base.BaseSpsEventgift;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class SpsEventgift extends BaseSpsEventgift {
	private String insName;		// 등록자
	private String updName;		// 수정자

}