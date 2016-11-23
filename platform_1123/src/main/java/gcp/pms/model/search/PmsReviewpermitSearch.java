package gcp.pms.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
@EqualsAndHashCode
@ToString
@Data
public class PmsReviewpermitSearch extends BaseSearchCondition {
	private String	memStateCds;
	private String	memberTypeCds;
	private String	memGradeCds;
	private String	memIds;
	private String	memName;
	private String	memberNo;
	private String[] productList;
}
