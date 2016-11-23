
// 공통 서비스 선언
var commonPopupServiceModule = angular.module("commonPopupServiceModule", []);

/***********************************************************************/
/** 공통 Service                                                      **/
/***********************************************************************/

commonPopupServiceModule.service('commonPopupService',   
		function($window) {
			return {
				//거래처 팝업
				businessPopup : function(scope,callback,multi){
					$window.$scope = scope;
					$window.$scope._business_param = {callback : callback, multi:multi};					
					var winName='businessPopup';		
					var url = Rest.context.path + '/ccs/business/popup/list';
					popupwindow(url,winName,1100,600);	
				},
				//회원 팝업
				memberPopup : function(scope,callback,multi){
					$window.$scope = scope;
					$window.$scope._member_param = {callback : callback, multi:multi};					
					var winName='memberPopup';		
					var url = Rest.context.path + '/mms/member/popup/search';
					popupwindow(url,winName,1100,600);	
				}, 
				// 기획전 팝업
				exhibitPopup : function(scope,callback,multi) {
					$window.$scope = scope;
					$window.$scope._exhibit_param = {callback : callback, multi:multi};					
					var winName='exhibitPopup';		
					var url = Rest.context.path + '/dms/exhibit/popup/search';
					popupwindow(url,winName,1100,600);
				},
				//브랜드 팝업
				brandPopup : function(scope,callback,multi){
					$window.$scope = scope;
					$window.$scope._brand_param = {callback : callback, multi:multi};					
					var winName='brandPopup';		
					var url = Rest.context.path + '/pms/brand/popup/list';
					popupwindow(url,winName,1100,600);	
				},
				//오프라인 매장 팝업
				offshopPopup : function(scope,callback,multi){
					$window.$scope = scope;
					$window.$scope._offshop_param = {callback : callback, multi:multi};					
					var winName='offshopPopup';		
					var url = Rest.context.path + '/ccs/offshop/popup/search';
					popupwindow(url,winName,1100,600);	
				},
				// 제한설정 팝업
				restrictPopup : function(scope, type, grade, channel, device, flag) {
					$window.$scope = scope;
//					$window.$scope._control_model = controlModel;
					$window.$scope._control_param = {
//						callback : callback,
						type : type,
						grade: grade,
						channel: channel,
						device: device,
						disableFlag :flag
					};
					var winName = 'restrictPopup';		
					var url = Rest.context.path + '/ccs/control/popup/setup';
					popupwindow(url,winName,1200,400);
				},
				//카테고리 검색 팝업
				categoryPopup : function(scope, callback, multi, url) {
					$window.$scope = scope;
					$window.$scope._category_param = {
						callback : callback,
						multi : multi
					};
					var winName = 'categoryPopup';
					var resturl = Rest.context.path + url;
					popupwindow(resturl, winName, 560, 600);
				},
				//사용자 검색 팝업
				userPopup : function(scope, callback, multi, userTypeCd) {
					$window.$scope = scope;
					$window.$scope._user_param = {callback : callback,multi : multi, userTypeCd : userTypeCd};
					var winName = 'userPopup';
					var resturl = Rest.context.path + '/ccs/user/popup/list';
					popupwindow(resturl, winName, 1100, 600);
				},
				//상품 검색 팝업
				productPopup : function(scope, callback, multi) {
					$window.$scope = scope;
					$window.$scope.searchParam = {callback : callback, multi : multi};
					var winName = 'productPopup';
					var resturl = Rest.context.path + '/pms/product/popup/search';
					
					popupwindow(resturl, winName, 1100, 600);
				},				
				//쿠폰 검색 팝업
				couponPopup : function(scope, callback, multi) {
					$window.$scope = scope;
					$window.$scope.searchParam = {callback : callback, multi : multi};
					var winName = 'couponPopup';
					var resturl = Rest.context.path + '/sps/coupon/popup/search';
					popupwindow(resturl, winName, 1100, 600);
				},				
				//사은품 검색 팝업
				presentPopup : function(scope, callback, multi) {
					$window.$scope = scope;
					$window.$scope.searchParam = {callback : callback, multi : multi};
					var winName = 'presentPopup';
					var resturl = Rest.context.path + '/pms/present/popup/search';
					popupwindow(resturl, winName, 1100, 600);
				},
				//grid bulk upload 팝업
				gridbulkuploadPopup : function(scope, callback, bulkType) {
					$window.$scope = scope;
					$window.$scope._gridbulkupload_Param = {callback : callback, bulkType : bulkType};
					var winName = 'gridbulkupload';
					var resturl = Rest.context.path + '/ccs/popup/gridbulkupload';
					popupwindow(resturl, winName, 1100, 600);
				},				
				//ERP 상품 검색 팝업
				erpProductSearchPopup : function(scope, callback, multi) {
					$window.$scope = scope;
					$window.$scope.searchParam = {callback : callback, multi : multi};
					var winName = 'ERP 상품 검색';
					var resturl = Rest.context.path + '/pms/product/popup/erpitemSearch';
					popupwindow(resturl, winName, 1100, 630);
				},
				//ERP 단품 검색 팝업
				erpProductSearchPopup : function(scope, callback, multi) {
					$window.$scope = scope;
					$window.$scope.searchParam = {callback : callback, multi : multi};
					var winName = 'ERP단품검색';
					var resturl = Rest.context.path + '/pms/product/popup/erpSaleProductSearch';
					popupwindow(resturl, winName, 1100, 600);
				},
				//상품 상세 팝업
				openProductDetailPopup : function(scope, productId, isProductCopy) {
					
					scope.selectedProductId = productId;
					
					if(isProductCopy){
						scope.productCopy = true;
					}else{
						scope.productCopy = false;
					}
					
					$window.$scope = scope;
					popupwindow("/pms/product/popup/detail", "상품상세",  1300, 600);
				},
				//회원 상세 팝업
				openMemberDetailPopup : function(scope, memberNo) {
					scope.memberNo = memberNo;
					$window.$scope = scope;
					popupwindow("/mms/member/popup/detail", "회원상세",  1300, 600);
				},
				//주소 검색 팝업
				openAddressPopup : function(scope, callback) {
					$window.$scope = scope;
					$window.$scope.searchParam = {callback : callback};
					popupwindow("/common/address/popup/search", "주소검색",  640, 350);
				},
				//주문검색 팝업
				orderPopup : function(scope, callback, multi) {
//					scope.search.memberNo = '123123';
//					scope.search.orderer = '김김김';
//					scope.search.ordererType = 'name';
					$window.$scope = scope;
					$window.$scope.searchParam = {
						callback : callback,
						multi : multi
					};
					popupwindow('/oms/order/popup/search', '주문상품조회', 1300, 600);
				},
				openBrandDetailPopup : function(scope, brandId) {
					scope.search.brandId = brandId;
					
					var winURL = Rest.context.path + "/pms/brand/popup/detail";
					popupwindow(winURL, "브랜드 상세", 960, 550);
				}
			}
		}
	);