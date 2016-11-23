package gcp.external.searchApi.api;

import java.util.ArrayList;
import java.util.List;

import gcp.external.searchApi.vo.SearchBenefitList;
import gcp.external.searchApi.vo.SearchBrandList;
import gcp.external.searchApi.vo.SearchCategoryList;
import gcp.external.searchApi.vo.SearchColorList;
import gcp.external.searchApi.vo.SearchData;
import gcp.external.searchApi.vo.SearchMaterial1List;
import gcp.external.searchApi.vo.SearchMaterialList;
import gcp.external.searchApi.vo.SearchProduct;

public class UnifiedSearchApi {
	public String	SEARCH_IP				= "49.50.33.224";																																																																											//검색엔진 IP(실제로는 L4)
	public int		SEARCH_PORT				= 7000;													//검색 엔진 PORT
	public int		MANAGER_PORT			= 7800;													//관리도구 PORT
	public int		SERVER_TIMEOUT			= 10 * 1000;											//서버 TIMEOUT(10초)
	public String	QUERY					= "";
	public String	COLLECTION				= "shopping";
	public int		QUERY_LOG				= 1;
	public int		EXTEND_OR				= 0;
	public int		USE_HIGHLIGHT			= 1;
	public int		USE_SNIPPET				= 0;
	public int		PAGE_START				= 0;
	public int		RESULT_COUNT			= 0;
	public String	SORT_FIELD				= "";
	public String	START_DATE				= "1970/01/01 01:00:00";
	public String	END_DATE				= "2030/12/31 23:59:59";
	public String	SEARCH_FIELD			= "BRAND_NAME,PRODUCT_NAME,KEYWORD,AD_COPY,PRODUCT_ID,ERP_PRODUCT_ID";
	public String	DOCUMENT_FIELD			= "DOCID,STORE_ID,PRODUCT_ID,ERP_PRODUCT_ID,BRAND_ID,BRAND_NAME,PRODUCT_NAME,KEYWORD,AD_COPY,"
			+ "BUSINESS_ID,AGE_TYPE_CD,ORI_SALE_PRICE,DATE,MODIFIED_DATE,GENDER_TYPE_CD,DISPLAY_CATEGORY_ID,CATEGORY_NAME,"
			+ "IMG_URL,OFFSHOP_PICKUP_YN,REGULAR_DELIVERY_YN,NEW_ICON_YN,PRESENT_YN,COLOR,MATERIAL,MATERIAL1,SALE_PRICE,POINT_YN,"
			+ "DELIVERY_FEE_FREE_YN,COUPON_YN,PRESTIGE_SALE_PRICE,PRESTIGE_POINT_YN,PRESTIGE_DELIVERY_FEE_FREE_YN,PRESTIGE_COUPON_YN,"
			+ "VIP_SALE_PRICE,VIP_POINT_YN,VIP_DELIVERY_FEE_FREE_YN,VIP_COUPON_YN,GOLD_SALE_PRICE,GOLD_POINT_YN,"
			+ "GOLD_DELIVERY_FEE_FREE_YN,GOLD_COUPON_YN,SILVER_SALE_PRICE,SILVER_POINT_YN,SILVER_DELIVERY_FEE_FREE_YN,"
			+ "SILVER_COUPON_YN,FAMILY_SALE_PRICE,FAMILY_POINT_YN,FAMILY_DELIVERY_FEE_FREE_YN,FAMILY_COUPON_YN,"
			+ "WELCOME_SALE_PRICE,WELCOME_POINT_YN,WELCOME_DELIVERY_FEE_FREE_YN,WELCOME_COUPON_YN,SEARCH_EXC_YN,"
			+ "SALE_START_DT,SALE_END_DT,DISPLAY_YN,SALE_STATE_CD,ORDER_QTY,RATING,CATEGORY_CATE_INDEX,BRAND_CATE_INDEX,"
			+ "REGULAR_DELIVERY_PRICE,DEAL_LIST_PRICE";
	public String	STATE_PREFIX			= "";
	public String	CATEGORY_PREFIX			= "";
	public String	BRAND_PREFIX			= "";
	public String	AGE_TYPE_PREFIX			= "";
	public String	GENDER_TYPE_PREFIX		= "";
	public String	COLOR_PREFIX			= "";
	public String	MATERIAL_PREFIX			= "";
	public String	MATERIAL1_PREFIX		= "";
	public String	BENEFIT_PREFIX			= "";
	public String	BUSINESS_ID_PREFIX		= "";
	public String	PREFIX_QUERY			= "";
	public String	FILTER_QUERY			= "";
	public String	CATEGORY_INDEX_FIELD	= "";
	public String	CATEGORY_INDEX_DEPTH	= "";

	//기본검색
	public SearchData runKeywordSearch(String query, int startCount, int viewResultCount, String sort, String keywordYn,
			List<String> categoryIdList, List<String> brandIdList, List<String> ageTypeCodeList, String genderTypeCode,
			int lowPrice, int highPrice, List<String> colorList, List<String> materialList, List<String> material1List,
			SearchBenefitList benefitList,
			String businessId, String division) {

		QUERY = query;
		PAGE_START = startCount;
		RESULT_COUNT = viewResultCount;
		SORT_FIELD = sort + ",RANK/DESC";
		CATEGORY_INDEX_DEPTH = "1/SC";

		if (division.equals("dev")) {
			COLLECTION = "shopping_dev";
		} else {
			COLLECTION = "shopping";
		}

		// ********** PREFIX CONDITION SETTING START **********
		//state
		STATE_PREFIX = "(<SEARCH_EXC_YN:contains:N> <DISPLAY_YN:contains:Y> <SALE_STATE_CD:contains:SALE>)";

		//brand
		if (brandIdList.size() != 0) {
			BRAND_PREFIX += "(";
			for (int b = 0; b < brandIdList.size(); b++) {
				BRAND_PREFIX += "<BRAND_ID:contains:";
				BRAND_PREFIX += brandIdList.get(b);
				BRAND_PREFIX += ">";
				if (b != brandIdList.size() - 1) {
					BRAND_PREFIX += "|";
				}
			}
			BRAND_PREFIX += ")";
		}

		//category
		if (categoryIdList.size() != 0) {
			CATEGORY_PREFIX += "(";
			for (int c = 0; c < categoryIdList.size(); c++) {
				CATEGORY_PREFIX += "<DISPLAY_CATEGORY_ID:contains:";
				CATEGORY_PREFIX += categoryIdList.get(c);
				CATEGORY_PREFIX += ">";
				if (c != categoryIdList.size() - 1) {
					CATEGORY_PREFIX += "|";
				}
			}
			CATEGORY_PREFIX += ")";
		}

		//age type
		if (ageTypeCodeList.size() != 0) {
			AGE_TYPE_PREFIX += "(";
			for (int d = 0; d < ageTypeCodeList.size(); d++) {
				AGE_TYPE_PREFIX += "<AGE_TYPE_CD:contains:";
				AGE_TYPE_PREFIX += ageTypeCodeList.get(d);
				AGE_TYPE_PREFIX += ">";
				if (d != ageTypeCodeList.size() - 1) {
					AGE_TYPE_PREFIX += "|";
				}
			}
			AGE_TYPE_PREFIX += ")";
		}

		//gender type
		if (!"".equals(genderTypeCode)) {
			GENDER_TYPE_PREFIX += "<GENDER_TYPE_CD:contains:";
			GENDER_TYPE_PREFIX += genderTypeCode;
			GENDER_TYPE_PREFIX += ">";
		}

		//color
		if (colorList.size() != 0) {
			COLOR_PREFIX += "(";
			for (int e = 0; e < colorList.size(); e++) {
				COLOR_PREFIX += "<COLOR:contains:";
				COLOR_PREFIX += colorList.get(e);
				COLOR_PREFIX += ">";
				if (e != colorList.size() - 1) {
					COLOR_PREFIX += "|";
				}
			}
			COLOR_PREFIX += ")";
		}

		//material(필드값 한글로 그대로 쓰기로)
		if (materialList.size() != 0) {
			MATERIAL_PREFIX += "(";
			for (int f = 0; f < materialList.size(); f++) {
				MATERIAL_PREFIX += "<MATERIAL:contains:";
				MATERIAL_PREFIX += materialList.get(f);
				MATERIAL_PREFIX += ">";
				if (f != materialList.size() - 1) {
					MATERIAL_PREFIX += "|";
				}
			}
			MATERIAL_PREFIX += ")";
		}

		//material1(필드값 한글로 그대로 쓰기로)
		if (material1List.size() != 0) {
			MATERIAL1_PREFIX += "(";
			for (int g = 0; g < material1List.size(); g++) {
				MATERIAL1_PREFIX += "<MATERIAL1:contains:";
				MATERIAL1_PREFIX += material1List.get(g);
				MATERIAL1_PREFIX += ">";
				if (g != material1List.size() - 1) {
					MATERIAL1_PREFIX += "|";
				}
			}
			MATERIAL1_PREFIX += ")";
		}

		//benefit
		if ("Y".equals(benefitList.getCouponYn()) || "Y".equals(benefitList.getDeliveryFreeYn())
				|| "Y".equals(benefitList.getPointSaveYn()) || "Y".equals(benefitList.getPresentYn())
				|| "Y".equals(benefitList.getRegularDeliveryYn()) || "Y".equals(benefitList.getOffshopPickupYn())) {

			BENEFIT_PREFIX += "(";

			if ("Y".equals(benefitList.getCouponYn())) {
				BENEFIT_PREFIX += "<COUPON_YN:contains:Y>|";
			}

			if ("Y".equals(benefitList.getDeliveryFreeYn())) {
				BENEFIT_PREFIX += "<DELIVERY_FEE_FREE_YN:contains:Y>|";
			}

			if ("Y".equals(benefitList.getPointSaveYn())) {
				BENEFIT_PREFIX += "<POINT_YN:contains:Y>|";
			}

			if ("Y".equals(benefitList.getPresentYn())) {
				BENEFIT_PREFIX += "<PRESENT_YN:contains:Y>|";
			}

			if ("Y".equals(benefitList.getRegularDeliveryYn())) {
				BENEFIT_PREFIX += "<REGULAR_DELIVERY_YN:contains:Y>|";
			}

			if ("Y".equals(benefitList.getOffshopPickupYn())) {
				BENEFIT_PREFIX += "<OFFSHOP_PICKUP_YN:contains:Y>|";
			}

			BENEFIT_PREFIX = BENEFIT_PREFIX.substring(0, BENEFIT_PREFIX.lastIndexOf("|"));
			BENEFIT_PREFIX += ")";
		}
		
		//businessId(묶음상품 업체코드)
		if (!"".equals(businessId)) {
			BUSINESS_ID_PREFIX += "<BUSINESS_ID:contains:";
			BUSINESS_ID_PREFIX += businessId;
			BUSINESS_ID_PREFIX += ">";
		}

		/*System.out.println(BRAND_PREFIX);
		System.out.println(CATEGORY_PREFIX);
		System.out.println(AGE_TYPE_PREFIX);
		System.out.println(GENDER_TYPE_PREFIX);
		System.out.println(COLOR_PREFIX);
		System.out.println(MATERIAL_PREFIX);
		System.out.println(BENEFIT_PREFIX);*/

		PREFIX_QUERY = STATE_PREFIX + BRAND_PREFIX + CATEGORY_PREFIX + AGE_TYPE_PREFIX + GENDER_TYPE_PREFIX + COLOR_PREFIX
				+ MATERIAL_PREFIX + MATERIAL1_PREFIX + BENEFIT_PREFIX + BUSINESS_ID_PREFIX;

		//System.out.println(PREFIX_QUERY);

		// ********** PREFIX CONDITION SETTING END **********

		// ********** FILTER CONDITION SETTING START **********
		if (highPrice != 0) {
			FILTER_QUERY += "<SALE_PRICE:gte:" + lowPrice + "> " + "<SALE_PRICE:lte:" + highPrice + ">";
		}
		// ********** FILTER CONDITION SETTING END **********

		//System.out.println(FILTER_QUERY);

		SearchData data = new SearchData();
		List<SearchProduct> productList = new ArrayList<SearchProduct>();
		List<SearchCategoryList> categoryCountLists = new ArrayList<SearchCategoryList>();
		List<SearchBrandList> brandCountLists = new ArrayList<SearchBrandList>();
		List<SearchColorList> colorCountLists = new ArrayList<SearchColorList>();
		List<SearchMaterialList> materialCountLists = new ArrayList<SearchMaterialList>();
		List<SearchMaterial1List> material1CountLists = new ArrayList<SearchMaterial1List>();

		@SuppressWarnings("unused")
		int ret;
		QueryAPI530.Search search = new QueryAPI530.Search();

		ret = search.w3SetCodePage("EUC-KR");
		ret = search.w3SetQueryLog(QUERY_LOG);
		ret = search.w3SetCommonQuery(QUERY, EXTEND_OR);
		ret = search.w3AddCollection(COLLECTION);
		ret = search.w3SetQueryAnalyzer(COLLECTION, 1, 1, 1, 1);
		ret = search.w3SetPageInfo(COLLECTION, PAGE_START, RESULT_COUNT);
		ret = search.w3SetSortField(COLLECTION, SORT_FIELD);
		ret = search.w3SetSearchField(COLLECTION, SEARCH_FIELD);
		ret = search.w3SetDocumentField(COLLECTION, DOCUMENT_FIELD);
		ret = search.w3SetHighlight(COLLECTION, USE_HIGHLIGHT, USE_SNIPPET);
		if ("N".equals(keywordYn)) {
			ret = search.w3SetDateRange(COLLECTION, START_DATE, END_DATE);
		}
		ret = search.w3SetPrefixQuery(COLLECTION, PREFIX_QUERY, 1);
		ret = search.w3SetFilterQuery(COLLECTION, FILTER_QUERY);
		//ret = search.w3AddCategoryGroupBy(COLLECTION, "DISPLAY_CATEGORY_ID", CATEGORY_INDEX_DEPTH);
		//ret = search.w3AddCategoryGroupBy(COLLECTION, "CATEGORY_NAME", CATEGORY_INDEX_DEPTH);
		ret = search.w3AddCategoryGroupBy(COLLECTION, "CATEGORY_CATE_INDEX", CATEGORY_INDEX_DEPTH);
		ret = search.w3AddCategoryGroupBy(COLLECTION, "BRAND_ID", CATEGORY_INDEX_DEPTH);
		ret = search.w3AddCategoryGroupBy(COLLECTION, "BRAND_NAME", CATEGORY_INDEX_DEPTH);
		ret = search.w3AddCategoryGroupBy(COLLECTION, "COLOR", CATEGORY_INDEX_DEPTH);
		ret = search.w3AddCategoryGroupBy(COLLECTION, "MATERIAL", CATEGORY_INDEX_DEPTH);
		ret = search.w3AddCategoryGroupBy(COLLECTION, "MATERIAL1", CATEGORY_INDEX_DEPTH);
		ret = search.w3SetSpellCorrectionQuery(QUERY, 0);
		ret = search.w3ConnectServer(SEARCH_IP, SEARCH_PORT, SERVER_TIMEOUT);
		ret = search.w3ReceiveSearchQueryResult(3);

		if (search.w3GetError() != 0) {
			System.out.println("ERROR INFO : " + search.w3GetErrorInfo());
		}

		int totalCount = search.w3GetResultTotalCount(COLLECTION);
		int resultCount = search.w3GetResultCount(COLLECTION);

		int categoryGroupCount = search.w3GetCategoryCount(COLLECTION, "CATEGORY_CATE_INDEX", 1);
		int brandGroupCount = search.w3GetCategoryCount(COLLECTION, "BRAND_ID", 1);
		int colorGroupCount = search.w3GetCategoryCount(COLLECTION, "COLOR", 1);
		int materialGroupCount = search.w3GetCategoryCount(COLLECTION, "MATERIAL", 1);
		int material1GroupCount = search.w3GetCategoryCount(COLLECTION, "MATERIAL1", 1);
		//System.out.println("검색결과 : " + resultCount + "건 / 전체 : " + totalCount + "건");


		//오타교정
		String suggestedQuery = search.w3GetSuggestedQuery().equals(query) ? "" : search.w3GetSuggestedQuery();

		//category
		for (int i = 0; i < categoryGroupCount; i++) {
			String categoryIndex = search.w3GetCategoryName(COLLECTION, "CATEGORY_CATE_INDEX", 1, i);
			//System.out.println(categoryIndex);

			String name = "";
			String id = "";

			name = categoryIndex.substring(categoryIndex.indexOf("^") + 1);
			int count = search.w3GetDocumentCountInCategory(COLLECTION, "CATEGORY_CATE_INDEX", 1, i);
			if (categoryIndex.indexOf("^") != -1) {
				id = categoryIndex.substring(0, categoryIndex.indexOf("^"));
			} else {
				id = "99999";
			}
			SearchCategoryList categoryList = new SearchCategoryList();
			categoryList.setCategoryName(name);
			categoryList.setCategoryCount(count);
			categoryList.setCategoryId(id);

			categoryCountLists.add(categoryList);
		}

		if (categoryGroupCount > 0) {
			data.setCategoryList(categoryCountLists);
		}

		//brand
		for (int j = 0; j < brandGroupCount; j++) {
			String name = search.w3GetCategoryName(COLLECTION, "BRAND_NAME", 1, j);
			int count = search.w3GetDocumentCountInCategory(COLLECTION, "BRAND_ID", 1, j);
			String id = search.w3GetCategoryName(COLLECTION, "BRAND_ID", 1, j);

			SearchBrandList brandList = new SearchBrandList();
			brandList.setBrandName(name);
			brandList.setBrandCount(count);
			brandList.setBrandId(id);

			brandCountLists.add(brandList);
		}

		if (brandGroupCount > 0) {
			data.setBrandList(brandCountLists);
		}

		//color
		for (int k = 0; k < colorGroupCount; k++) {
			String name = search.w3GetCategoryName(COLLECTION, "COLOR", 1, k);
			int count = search.w3GetDocumentCountInCategory(COLLECTION, "COLOR", 1, k);

			SearchColorList colorNameList = new SearchColorList();
			colorNameList.setColorName(name);
			colorNameList.setColorCount(count);

			colorCountLists.add(colorNameList);
		}

		if (colorGroupCount > 0) {
			data.setColorList(colorCountLists);
		}

		//material(일반소재)
		for (int l = 0; l < materialGroupCount; l++) {
			String name = search.w3GetCategoryName(COLLECTION, "MATERIAL", 1, l);
			int count = search.w3GetDocumentCountInCategory(COLLECTION, "MATERIAL", 1, l);

			SearchMaterialList materialNameList = new SearchMaterialList();
			materialNameList.setMaterialName(name);
			materialNameList.setMaterialCount(count);

			materialCountLists.add(materialNameList);
		}

		if (materialGroupCount > 0) {
			data.setMaterialList(materialCountLists);
		}

		//material1(젖병소재)
		for (int m = 0; m < material1GroupCount; m++) {
			String name = search.w3GetCategoryName(COLLECTION, "MATERIAL1", 1, m);
			int count = search.w3GetDocumentCountInCategory(COLLECTION, "MATERIAL1", 1, m);

			SearchMaterial1List material1NameList = new SearchMaterial1List();
			material1NameList.setMaterial1Name(name);
			material1NameList.setMaterial1Count(count);

			material1CountLists.add(material1NameList);
		}

		if (material1GroupCount > 0) {
			data.setMaterial1List(material1CountLists);
		}

		//samll category
		/*for (int k = 0; k < smallCategoryGroupCount; k++) {
			String name = search.w3GetCategoryName(COLLECTION, "SMALL_CATEGORY_NAME", 1, k);
			int count = search.w3GetDocumentCountInCategory(COLLECTION, "SMALL_DISPLAY_CATEGORY_ID", 1, k);
			String id = search.w3GetCategoryName(COLLECTION, "SMALL_DISPLAY_CATEGORY_ID", 1, k);
		
			SearchSmallCategoryList smallCategoryList = new SearchSmallCategoryList();
			smallCategoryList.setSmallCategoryName(name);
			smallCategoryList.setSmallCategoryCount(count);
			smallCategoryList.setSmallCategoryId(id);
		
			smallCategoryCountLists.add(smallCategoryList);
		}
		
		if (smallCategoryGroupCount > 0) {
			data.setSmallCategoryList(smallCategoryCountLists);
		}*/

		//product
		for (int k = 0; k < resultCount; k++) {

			SearchProduct product = new SearchProduct();
			product.setStoreId(search.w3GetField(COLLECTION, "STORE_ID", k));
			product.setProductId(search.w3GetField(COLLECTION, "PRODUCT_ID", k));
			product.setBrandId(search.w3GetField(COLLECTION, "BRAND_ID", k));
			product.setBrandName(search.w3GetField(COLLECTION, "BRAND_NAME", k));
			product.setProductName(search.w3GetField(COLLECTION, "PRODUCT_NAME", k));
			product.setKeyword(search.w3GetField(COLLECTION, "KEYWORD", k));
			product.setAdCopy(search.w3GetField(COLLECTION, "AD_COPY", k));
			product.setBusinessId(search.w3GetField(COLLECTION, "BUSINESS_ID", k));
			product.setAgeTypeCd(search.w3GetField(COLLECTION, "AGE_TYPE_CD", k));
			product.setOriSalePrice(search.w3GetField(COLLECTION, "ORI_SALE_PRICE", k));
			product.setDate(search.w3GetField(COLLECTION, "DATE", k));
			product.setModifiedDate(search.w3GetField(COLLECTION, "MODIFIED_DATE", k));
			product.setGenderTypeCd(search.w3GetField(COLLECTION, "GENDER_TYPE_CD", k));
			product.setDisplayCategoryId(search.w3GetField(COLLECTION, "DISPLAY_CATEGORY_ID", k));
			product.setCategoryName(search.w3GetField(COLLECTION, "CATEGORY_NAME", k));
			product.setImgUrl(search.w3GetField(COLLECTION, "IMG_URL", k));
			product.setOffshopPickupYn(search.w3GetField(COLLECTION, "OFFSHOP_PICKUP_YN", k));
			product.setRegularDeliveryYn(search.w3GetField(COLLECTION, "REGULAR_DELIVERY_YN", k));
			product.setNewIconYn(search.w3GetField(COLLECTION, "NEW_ICON_YN", k));
			product.setPresentYn(search.w3GetField(COLLECTION, "PRESENT_YN", k));
			product.setColor(search.w3GetField(COLLECTION, "COLOR", k));
			product.setMaterial(search.w3GetField(COLLECTION, "MATERIAL", k));
			product.setMaterial_1(search.w3GetField(COLLECTION, "MATERIAL1", k));
			product.setSalePrice(search.w3GetField(COLLECTION, "SALE_PRICE", k));
			product.setPointYn(search.w3GetField(COLLECTION, "POINT_YN", k));
			product.setDeliveryFeeFreeYn(search.w3GetField(COLLECTION, "DELIVERY_FEE_FREE_YN", k));
			product.setCouponYn(search.w3GetField(COLLECTION, "COUPON_YN", k));
			product.setPrestigeSalePrice(search.w3GetField(COLLECTION, "PRESTIGE_SALE_PRICE", k));
			product.setPrestigePointYn(search.w3GetField(COLLECTION, "PRESTIGE_POINT_YN", k));
			product.setPrestigeDeliveryFeeFreeYn(search.w3GetField(COLLECTION, "PRESTIGE_DELIVERY_FEE_FREE_YN", k));
			product.setPrestigeCouponYn(search.w3GetField(COLLECTION, "PRESTIGE_COUPON_YN", k));
			product.setVipSalePrice(search.w3GetField(COLLECTION, "VIP_SALE_PRICE", k));
			product.setVipPointYn(search.w3GetField(COLLECTION, "VIP_POINT_YN", k));
			product.setVipDeliveryFeeFreeYn(search.w3GetField(COLLECTION, "VIP_DELIVERY_FEE_FREE_YN", k));
			product.setVipCouponYn(search.w3GetField(COLLECTION, "VIP_COUPON_YN", k));
			product.setGoldSalePrice(search.w3GetField(COLLECTION, "GOLD_SALE_PRICE", k));
			product.setGoldPointYn(search.w3GetField(COLLECTION, "GOLD_POINT_YN", k));
			product.setGoldDeliveryFeeFreeYn(search.w3GetField(COLLECTION, "GOLD_DELIVERY_FEE_FREE_YN", k));
			product.setGoldCouponYn(search.w3GetField(COLLECTION, "GOLD_COUPON_YN", k));
			product.setSilverSalePrice(search.w3GetField(COLLECTION, "SILVER_SALE_PRICE", k));
			product.setSilverPointYn(search.w3GetField(COLLECTION, "SILVER_POINT_YN", k));
			product.setSilverDeliveryFeeFreeYn(search.w3GetField(COLLECTION, "SILVER_DELIVERY_FEE_FREE_YN", k));
			product.setSilverCouponYn(search.w3GetField(COLLECTION, "SILVER_COUPON_YN", k));
			product.setFamilySalePrice(search.w3GetField(COLLECTION, "FAMILY_SALE_PRICE", k));
			product.setFamilyPointYn(search.w3GetField(COLLECTION, "FAMILY_POINT_YN", k));
			product.setFamilyDeliveryFeeFreeYn(search.w3GetField(COLLECTION, "FAMILY_DELIVERY_FEE_FREE_YN", k));
			product.setFamilyCouponYn(search.w3GetField(COLLECTION, "FAMILY_COUPON_YN", k));
			product.setWelcomeSalePrice(search.w3GetField(COLLECTION, "WELCOME_SALE_PRICE", k));
			product.setWelcomePointYn(search.w3GetField(COLLECTION, "WELCOME_POINT_YN", k));
			product.setWelcomeDeliveryFeeFreeYn(search.w3GetField(COLLECTION, "WELCOME_DELIVERY_FEE_FREE_YN", k));
			product.setWelcomeCouponYn(search.w3GetField(COLLECTION, "WELCOME_COUPON_YN", k));
			product.setSearchExcYn(search.w3GetField(COLLECTION, "SEARCH_EXC_YN", k));
			product.setSaleStartDt(search.w3GetField(COLLECTION, "SALE_START_DT", k));
			product.setSaleEndDt(search.w3GetField(COLLECTION, "SALE_END_DT", k));
			product.setDisplayYn(search.w3GetField(COLLECTION, "DISPLAY_YN", k));
			product.setSaleStateCd(search.w3GetField(COLLECTION, "SALE_STATE_CD", k));
			product.setOrderQty(search.w3GetField(COLLECTION, "ORDER_QTY", k));
			product.setRating(search.w3GetField(COLLECTION, "RATING", k));
			product.setRegularDeliveryPrice(search.w3GetField(COLLECTION, "REGULAR_DELIVERY_PRICE", k));
			product.setDealListPrice(search.w3GetField(COLLECTION, "DEAL_LIST_PRICE", k));
			
			productList.add(product);
		}

		data.setQuery(query);
		data.setSuggestedQuery(suggestedQuery);
		data.setTotalCount(totalCount);
		data.setViewCount(resultCount);

		if (resultCount > 0) {
			data.setProductList(productList);
		}

		search.w3CloseServer();
		//Gson gson = new GsonBuilder().create();
		//System.out.println(gson.toJson(data));

		//return gson.toJson(data);
		return data;
	}

}
