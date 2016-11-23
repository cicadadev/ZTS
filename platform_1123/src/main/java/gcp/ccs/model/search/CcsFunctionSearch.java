package gcp.ccs.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;

@Data
public class CcsFunctionSearch extends BaseSearchCondition {
	private String	menuId;
	private String	roleId;
}
