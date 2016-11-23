package gcp.pms.model.search;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;

@Data
public class PmsCategorySearch extends BaseSearchCondition {

	private String	categoryId;
	
	// FO 
	
	private String category1; // catagory 셀럭터 + index
	private String category2;
	private String category3;
	private String category4;
	private String category5;
	private String category6;
	
	
	private String categoryRootId;
}
