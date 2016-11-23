package gcp.dms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.dms.model.base.BaseDmsCatalog;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class DmsCatalog extends BaseDmsCatalog {
	private String insName;		// 등록자
	private String updName;		// 수정자
	private String brandName;
}