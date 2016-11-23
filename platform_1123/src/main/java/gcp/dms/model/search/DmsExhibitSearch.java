package gcp.dms.model.search;

import java.util.List;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;

@Data
public class DmsExhibitSearch extends BaseSearchCondition {
	
	private String exhibitIds;
	private String exhibitId;
	private String displayY;
	private String displayN;
	private String exhibitStateCds;
	private String name;
	private String startDt;
	private String endDt;
	private String exhibitTypeCds;
	
	private String groupNo;
	private String productId;
	private String[] productIds;
	private String[] couponIds;
	private String	productInfoType;
	private String	exhibitInfoType;
	
	private String exhibitSearchKeyword;
	private String productSearchKeyword;
	
	private String brandId;
	private String displayCategoryId;
	private String mobileYn;
	List<String> exhibitIdList;
}
