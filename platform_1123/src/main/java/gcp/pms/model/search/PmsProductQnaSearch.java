package gcp.pms.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = false)
@Data
public class PmsProductQnaSearch extends BaseSearchCondition {

	private String	periodType;
	private String	productQnaStateCds;
	private String	displayYnCds;
	private String	searchPrdword;
	private String	prdSearchType;
	private String	businessId;

	private String	answerId;
	private String	answererName;
	private String memberNo;
	private String memberLoginId;
	private String keyword;
	private String productId;
	private String displayYn;
	private String secretYn;
	
	private String isScroll;

}
