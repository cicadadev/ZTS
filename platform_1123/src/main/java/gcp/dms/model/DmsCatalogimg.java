package gcp.dms.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import gcp.dms.model.base.BaseDmsCatalogimg;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class DmsCatalogimg extends BaseDmsCatalogimg {
	private String insName;		// 등록자
	private String updName;		// 수정자
}