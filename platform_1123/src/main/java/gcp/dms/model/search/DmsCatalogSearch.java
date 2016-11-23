package gcp.dms.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;

@Data
public class DmsCatalogSearch extends BaseSearchCondition {
	
	private String catalogId;
	private String displayYn;
	private String displayYnCds;
	private String catalogTypeCd;
	private String catalogTypeCdCds;
	private String catalogImgNo;
	private String brandId;

	// FO
	private String templateType;
	private String seasonCd;
	private String directYn;
	
	private String salesAssist;
}
