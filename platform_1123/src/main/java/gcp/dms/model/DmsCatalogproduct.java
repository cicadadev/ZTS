package gcp.dms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.dms.model.base.BaseDmsCatalogproduct;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class DmsCatalogproduct extends BaseDmsCatalogproduct {
	private String displayItemId;
	private String insName;		// 등록자
	private String updName;		// 수정자
}