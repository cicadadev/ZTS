package gcp.ccs.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class CcsAccessmenuSearch extends BaseSearchCondition {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String roleId;
	private String menuId;
	private String url;
	
	private String 	systemTypeCd;  // 메뉴 시스템 유형
}
