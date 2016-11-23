package gcp.ccs.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;

@Data
public class CcsCodeSearch extends BaseSearchCondition {
	private String	cdGroupCd;
	private String	cd;
	
	private String	searchCodeType;
	private String	searchGroupCode;
	private String	searchGroupName;
	
	private String  searchType;
	private String	name;
}
