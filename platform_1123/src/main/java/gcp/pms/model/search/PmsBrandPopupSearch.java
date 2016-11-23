package gcp.pms.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
@Data
public class PmsBrandPopupSearch extends BaseSearchCondition {
	
	private String type;
	private String displayYn;
	private String searchName;
	private String searchBrandId;
	

}
