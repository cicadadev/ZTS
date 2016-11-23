package gcp.mms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.mms.model.base.BaseMmsBlacklist;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class MmsBlacklist extends BaseMmsBlacklist {
	private String insName;		// 등록자
	private String updName;		// 수정자

}