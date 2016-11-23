package gcp.dms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.dms.model.base.BaseDmsExhibitgroup;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class DmsExhibitgroup extends BaseDmsExhibitgroup {
	private String insName;		// 등록자
	private String updName;		// 수정자
	private int groupProductCount;
}