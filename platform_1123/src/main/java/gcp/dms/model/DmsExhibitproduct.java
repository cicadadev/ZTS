package gcp.dms.model;

import gcp.dms.model.base.BaseDmsExhibitproduct;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class DmsExhibitproduct extends BaseDmsExhibitproduct {
	private String insName;		// 등록자
	private String updName;		// 수정자
	private String rowNumber;

}