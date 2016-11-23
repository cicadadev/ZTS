package gcp.ccs.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;

@Data
public class CcsRoleSearch extends BaseSearchCondition {
	private String searchType;
	private String roleId;
	private String roleName;
}
