package gcp.dms.model.search;

import java.math.BigDecimal;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;

@Data
public class DmsDisplaySearch extends BaseSearchCondition {

	private String	displayId;
	private String	name;
	private String	displayTypeCd;
	private String	displayItemTypeCd;
	private String	leafYn;
	private String	displayYn;
	private String	useYn;
	private String	insId;

	private String	templateTypeCd;
	private String	templateId;
	private String	loginId;
	
	private String	displayItemDivId;
	private String	displayItemNo;
	
	private String displayCategoryId;
	private String upperDisplayCategoryId;
	private String depth;
	private String mobilePageYn;
	private String rootCategoryId;
	private String displayItemId;
	private int 	rownum;
	private String linkType;
	private String categoryViewType;
	private String displayShopType;
	private String brandId;
	private String areaDiv1;
	private String areaDiv2;
	private String offshopTypeCd;
	private String dateLimit;
	private String offshopId;
	
	// FO
	private String sectionName;
	private String brandName;
	private String themeCd;
	private String displayType;
	private String minPrice;
	private String maxPrice;
	private String genderTypeCd;
	private String[] ageTypeCds;
	private String memGradeCd;
	
	private String viewName;
	private String mobileSort;
	private String catalogueSeason;
	private String interestOffshopPrdView;
	
	private BigDecimal memberNo;
	private String addCategoryId;
	private String directYn;
	private String exhibitStateCd;
	
	private String sort;
	private String regularDeliveryYn;

	private String salesAssist;
	
	//PO
	private String categoryRootId;
	/* paging */
	private int			cornerFirstRow ;
	private int			cornerLastRow;
	private int 		currentPage;
	private int 		pageSize;

}
