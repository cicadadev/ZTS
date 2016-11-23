package gcp.pms.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;

@Data
public class BrandSearch extends BaseSearchCondition {
	private String brandId;
	private String insName;
	private String updName;
	private String loginId;
	
	private String erpBrandId;
	private String displayYn;
	private String displayYnCds;
	private String templateId;
	private String brand;
	private String brandIds;
	
	// FO MOBILE 좌측 브랜드 검색
	private String unicodeBefore;
	private String unicodeAfter;
	private String searchKeyword;
	private String consonant;
}
