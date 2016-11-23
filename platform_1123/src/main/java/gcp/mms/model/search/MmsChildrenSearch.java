package gcp.mms.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;

@Data
public class MmsChildrenSearch extends BaseSearchCondition {
	private String childrencardTypeCd;
	private String cardNos;
	private String accountNo;
	private String memberName;
	
}
