package gcp.dms.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;

@Data
public class DmsStyleShopDetailSearch extends BaseSearchCondition {
	
	private String displayYnCds;
	private String infoType;
	private String styleNo;
	private String memberName;
	
}
