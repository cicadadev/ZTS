var pmsServiceModule = angular.module("pmsServiceModule", ['ngResource']);

pmsServiceModule.service('productService',function(restFactory, $http){
	
	var url = Rest.context.path+"/api/pms/product:id";
	var param = null;
	
	return {
		//상품 목록 조회
		getProductList : function(data, callback){
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI,url, null, data, callback);	
		}, 
		//상품목록(재고정보 포함)
		getProductListWithSaleStock : function(data, callback) {
			var url = Rest.context.path+"/api/pms/product/productListWithSaleStock";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI,url, null, data, callback);
		},
		//상품 상세 저장
		saveProduct : function(data, callback){
			var url = Rest.context.path+"/api/pms/product/detail";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback);
		},
		//상품 저장(pmsProduct 테이블 변경)
		savePmsProduct : function(data, callback){
			var url = Rest.context.path+"/api/pms/product";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.VOID, url, null, data, callback);
		},
		//상품 조회
		getProductDetail : function(productId, callback){
			var param = {productId : productId}
			var url = Rest.context.path+"/api/pms/product/:productId";
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, param, null, callback);
		},
		// 상품 승인/반려
		updateApproval : function(data, callback){
			var url = Rest.context.path+"/api/pms/product/approval/save";
//			var param = {"saleStateCd":saleStateCd,"rejectReason" : rejectReason}
			return restFactory.transaction(Rest.method.POST, Rest.responseType.VOID,url, null, data, callback);
		},
		updatePriceApproval : function(priceReserveStateCd,rejectReason,data, callback){
			var url = Rest.context.path+"/api/pms/product/priceApproval/save";
			var param = {"priceReserveStateCd":priceReserveStateCd,"rejectReason" : rejectReason}
			return restFactory.transaction(Rest.method.POST, Rest.responseType.VOID,url, param, data, callback);
		},
		getProductnoticefieldList : function(data,callback){
			var url = Rest.context.path+"/api/pms/product/noticefield/list";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		},
		//상품 고시목록 조회
		getNoticeList : function(data,callback){
			var url = Rest.context.path+"/api/pms/product/notice/listdetail";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		},
		// 상품 고시정보 승인/반려
		updateNoticeConfirm : function(data, callback){
			var url = Rest.context.path+"/api/pms/product/notice/save";
//			var param = {"saleStateCd":saleStateCd,"rejectReason" : rejectReason}
			return restFactory.transaction(Rest.method.POST, Rest.responseType.VOID,url, null, data, callback);
		},		
		//erp 에서 상품 정보 조회
		getErpItem : function(itemid, callback){
			var param = {itemid : itemid}
			var url = Rest.context.path+"/api/pms/product/erp/item/:itemid";
			return restFactory.transaction(Rest.method.GET, Rest.responseType.MULTI, url, param, null, callback);
		},
		//상품 이미지 저장
		saveProductImages : function(data, callback){
			var url = Rest.context.path+"/api/pms/product/images";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.VOID,url, null, data, callback);
		},
		//상품 이미지 삭제
		deleteProductImg : function(imagepath, callback){
			var param = {imagepath : imagepath}
			var url = Rest.context.path+"/api/ccs/common/images/:imagepath";
			return restFactory.transaction(Rest.method.DELETE, Rest.responseType.VOID, url, param, null,  callback);
		},
		//상품 이미지 조회
		getProductImages : function(productId, callback){
			var param = {productId : productId}
			var url = Rest.context.path+"/api/pms/product/images/:productId";
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, param, null, callback);
		},
		// 상품의 단품 목록 조회
		getSaleproductList : function(productId, callback){
			var param = {productId : productId}
			var url = Rest.context.path+"/api/pms/product/saleproduct/:productId";
			return restFactory.transaction(Rest.method.GET, Rest.responseType.MULTI, url, param, null, callback);
		},
		// 세트 구성상품 목록 조회
		getSetProductList: function(productId, callback){
			var param = {productId : productId}
			var url = Rest.context.path+"/api/pms/product/setproduct/:productId";
			return restFactory.transaction(Rest.method.GET, Rest.responseType.MULTI, url, param, null, callback);
		},
		// 상품 정보 예약 목록 조회
		getProductReserveList: function(data, callback){
			var param = {productId : productId}
			var url = Rest.context.path+"/api/pms/product/reserve";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		}
		
	}
	 	
}).service('brandService',function(restFactory){ // 브랜드 서비스
	
	var baseUrl = Rest.context.path+"/api/pms/brand/:brandId";
	
	return {
		getBrand: function(brandId, callback){ //브랜드 상세 조회
			var param = {brandId : brandId}
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, baseUrl, param, null, callback);
		},
		insertBrand : function(data, callback){ //브랜드 등록
			var url = Rest.context.path+"/api/pms/brand";
			restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback);
		},
		updateBrand : function(data, callback){ // 브랜드 수정
			var url = Rest.context.path + "/api/pms/brand/update";
			restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, null, data, callback);
		},
		getBrandList: function(callback){ // 브랜드 목록 조회
			var url = Rest.context.path+"/api/pms/brand/list";
			return restFactory.transaction(Rest.method.GET, Rest.responseType.MULTI,url, null, null, callback);
		},
		getBrandShopDetail: function(data, callback) {	// 브랜드샵 정보 조회
			var url = Rest.context.path+"/api/pms/brand/shop/detail";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, null, data, callback);
		}
		
	}
	 	
}).service('categoryService',function(restFactory){// 표준카테고리 서비스
	return {
		// 표준카테고리 트리 조회
		getCategories : function (search, callback) {
			var url = Rest.context.path + "/api/pms/category/tree";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, search, callback);
		},
		// 표준카테고리 상세 조회
		getCategoryDetail : function (data, callback) {
			var url = Rest.context.path + "/api/pms/category/detail";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, null, data, callback);
		},
		// 표준카테고리 등록/수정
		updateCategoryInfo : function (data, callback) {
			var url = Rest.context.path + "/api/pms/category/update";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback);
		},
		// 표준 카테고리 삭제
		deleteCategory : function(data, callback) {
			var url = Rest.context.path + "/api/pms/category/delete";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, null, data, callback);
		}
	}
	 	
}).service('attributeService',function(restFactory){	
	return {
		getAttributeDetail : function(data, callback) {
			var url = Rest.context.path + "/api/pms/attribute/popup/detail";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, null, data, callback);
		},
		updateAttribute : function(data, callback) {
			var url = Rest.context.path + "/api/pms/attribute/popup/update";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback);
		},
		insertAttribute : function(data, callback) {
			var url = Rest.context.path + "/api/pms/attribute/popup/insert";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback);
		},
		// 표준카테고리에 해당하는 속성 기준정보 조회 : categoryId, attributeTypeCd(SALEPRODUCT/ATTR) 필수
		getCategoryAttributeList : function(data, callback){
			var url = Rest.context.path+"/api/pms/attribute/list/category";
			return restFactory.transaction2(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		},
		// 속성 상세 팝업
		openAttributeDetailPopup : function() {
			var url = Rest.context.path + '/pms/attribute/popup/detail';
			popupwindow(url, "Attribute Detail", 1200, 275);
		}
		
		
	}
	 	
}).service('epexcService',function(restFactory){	
	return {
		//업체정보 조회
		getBusinessInfo : function(businessId, callback) {
			var param = {businessId : businessId}
			var url = Rest.context.path + "/api/pms/epexcproduct/:businessId";
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, param, null, callback);
		},
		//중복여부 체크
		checkDuplication : function(data, callback) {
			var url = Rest.context.path + "/api/pms/epexcproduct/checkDup";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback);
		},
		//외부 비노출 항목 등록
		insertEpexcproduct : function(data, callback) {
			var url = Rest.context.path + "/api/pms/epexcproduct/insert";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback);
		}
	}
	 	
}).service('sendgoodsService',function(restFactory){	
	return {
		insertSendgoods : function(data, callback) {
			var url = Rest.context.path + "/api/pms/sendgoods/insert";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback);
		}
	}
	 	
}).service('presentService',function(restFactory){
	
	var url = Rest.context.path+"/api/pms/product:id";
	var param = null;
	
	return {
		getProductList : function(data, callback){
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI,url, null, data, callback);	
		}, 
		getProductListWithSaleStock : function(data, callback) {
			var url = Rest.context.path+"/api/pms/product/productListWithSaleStock";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI,url, null, data, callback);
		},
		savePresent : function(data, callback) {
			url = Rest.context.path+"/api/pms/present/popup/save";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback);
		},
		deletePresent : function(data, callback) {
			url = Rest.context.path+"/api/pms/present/popup/delete";
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.VOID, url, null, data, callback);
		},
		getPresentProductDetail : function(data, callback) {
			url = Rest.context.path+"/api/pms/present/popup/detail";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, null, data, callback);
		}
	}
}).service('mdnoticeService',function(restFactory){
	var param = {};
	return {
		deleteCheck : function(data, callback){
			var url = Rest.context.path + "/api/pms/mdnotice/product/check";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE,url, param, data, callback);	
		},
		deleteNoticeproduct : function(data, callback){
			var url = Rest.context.path + "/api/pms/mdnotice/product/delete";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE,url, param, data, callback);	
		},
		saveNoticeproduct : function(data, callback) {
			var url = Rest.context.path + "/api/pms/mdnotice/product/save";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, param, data, callback);
		}
	}
}).service('priceService',function(restFactory){
	var param = {};
	
	return{
		getProductPriceHistoryList : function(data, callback){
			var url = Rest.context.path + "/api/pms/product/productPriceHistory/list";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI,url, null, data, callback);	
		},
		getItemPriceHistoryList : function(data, callback){
			var url = Rest.context.path + "/api/pms/product/itemPriceHistory/list";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI,url, null, data, callback);
		},
//		//상품 가격 예약 목록
//		selectProductPriceReserveList : function(productId, callback){
//			var param = {productId : productId}
//			var url = Rest.context.path+"/api/pms/product/price/reserve/:productId";
//			return restFactory.transaction(Rest.method.GET, Rest.responseType.MULTI, url, param, null, callback);
//		},
		//단품 가격 예약 목록 조회
		getSaleproductPriceReserveList : function(data, callback){
			var url = Rest.context.path+"/api/pms/product/salePriceReserve/list";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, null, data, callback);
		}
	}
	
}).service('styleProductService',function(restFactory){	
	return {
		getStyleProductDetail : function(styleProductNo, callback) {
			var param = {styleProductNo : styleProductNo}
			var url = Rest.context.path + "/api/pms/styleShop/styleProduct/:styleProductNo";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, param, null, callback);
		},
		saveStyleProduct : function(data, callback) {
			var url = Rest.context.path + "/api/pms/styleShop/save";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, url, null, data, callback);
		}
	}
});
