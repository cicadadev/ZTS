package gcp.ccs.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;

@Data
public class CcsMenuSearch extends BaseSearchCondition {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String	menuGroupId;
	
	private String	searchCodeType;
	private String	searchGroupCode;
	private String	searchGroupName;
	
	private String 	systemTypeCd;  // 메뉴 시스템 유형
}
