package gcp.ccs.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode(callSuper=true)
@ToString(callSuper=true)
@Data
public class CcsBusinessinquirySearch extends BaseSearchCondition {
	private String  mdId;
	private String 	mdName;
	private String 	searchKeyword;
	
/*	private String 	startDate;
	private String 	endDate;*/
}
