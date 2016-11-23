package gcp.external.model.search;

import java.util.ArrayList;
import java.util.List;

import intune.gsf.model.BaseSearchCondition;
import lombok.Data;
 
@Data
public class SearchApiSearch extends BaseSearchCondition{
	
	private static final long serialVersionUID = 6265413192400463759L;
	
	//request Prama 
	private String categoryId; 
	//request Prama 
	private String businessId;
	//결과내 재검색
	private String keyword;
	//성별
	private String genderTypeCod;
	//시작 검색가격
	private int lowPrice = 0;
	//끝 검색가격
	private int highPrice = 0;
	//카테고리ID목록
	private List<String> categoryIdList = new ArrayList<String>();
	//브랜드목록
	private List<String> brandIdList = new ArrayList<String>();
	//월령코드목록
	private List<String> ageTypeCodeList = new ArrayList<String>();
	//컬러목록
	private List<String> colorList = new ArrayList<String>();
	//소재목록
	private List<String> materialList = new ArrayList<String>();
	//젖병소재
	private List<String> material1List = new ArrayList<String>();
	//쿠폰할인
	private String	couponYn;
	//무료배송
	private String	deliveryFreeYn;
	//포인트
	private String	pointSaveYn;
	//사은품
	private String	presentYn;
	//정기배송
	private String	regularDeliveryYn;
	//매장픽업
	private String	offshopPickupYn;
	//검색화면Type
	private String searchType;
	//인기검색어의 기간
	private String range;
	//검색API type :자동검색, 인기검색, 연관검색 구분
	private String apiType;
	//카테고리매장의 검색경우
	//private String dispCtgShopYn;
	//option 카테고리목록
	private String optionCtgStrs;
	//Option 브랜드검색여부
	private String optionBrandSrcYn;
	//검색엔진을 사용하는 화면 구분
	private String searchViewType;
	//현재카테고리명
	private String categoryName;
	//depth카테고리
	private List<String> depthCategoryIds = new ArrayList<String>(); 
	//history back
	//private String historyYn;
	
	//브랜드관 SHOP_ID (브랜드ID)
	private String brandShopId;
	
	
}
