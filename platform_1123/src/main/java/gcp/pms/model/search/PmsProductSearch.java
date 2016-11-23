package gcp.pms.model.search;

import java.util.List;

import gcp.pms.model.PmsSaleproductoptionvalue;
import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@EqualsAndHashCode
@ToString
@Data
public class PmsProductSearch extends BaseSearchCondition {

	private String	productIds;					//상품IDS
	private String	saleproductIds;				//단품IDS
	private String	dispCategoryId;				//전시카테고리
	private String	dispCategoryName;			//전시카테고리명
	private String	realStockQty;	//실재고
	private String	categoryId;					//표준카테고리
	private String	categoryName;				//표준카테고리명
	private String	brandId;					//브랜드
	private String	saleStateCds;				//상품판매상태
	private String	saleStateCd;				//상품판매상태
	private String	productTypeCds;				//상품유형
	private String	stockControlTypeCds;		//재고제어유형
	private String	priceReserveStateCds;		//가격예약상태	
	private String	productName;				//상품명
	private String	userId;						//담당MD
	private String	userName;					//담당MD명
	private String	productId;
	private String	productNoticeTypeCd;		//상품고시유형
	private String  undType;					//이상,이하
	private String noticeConfirmCds;			//고시확인여부
	private String deviceTypeCd;
	private String barcode;						// 바코드
	private String pickupProduct;              // 픽업매장 옵션조회용
	private String 	productGubun;
	
	private String 	commissionRateYn;
	private String 	priceCompareType;
	private String 	searchPriceCompareKeyword;
	
	private String	priceReserveNo;
	
	// pmsSearch
	private String			brandName;
	private String			displayCategoryId;
	private String			productStateCds;
	private String			saleproductId;
	private String			businessName;
	private String			name;
	private String			useYn;
	private String			useYnCds;
	private String			userMd;
	private List<String>	productIdList;
	private List<String>	saleproductIdList;
	
	private String			infoType;
	private String			searchKeyword;

	//erp 검색
	private String itemid;
	private String itemname;
	private String erpProductId;
	
	// 조회할 옵션명
	private String targetOptionName;
	// 선택한 옵션-값 목록
	private List<PmsSaleproductoptionvalue> selectedOptions;
	private String qty;	//선택된 수량
	
	private String offshopId;
	
	private String dealId;
	private String couponId;
	private String erpBrandId;

}
