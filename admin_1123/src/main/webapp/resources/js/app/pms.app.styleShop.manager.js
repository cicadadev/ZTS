// 스타일샵 화면 모듈
var styleShopApp = angular.module("styleShopApp", ["commonServiceModule", "ccsServiceModule", "dmsServiceModule", "pmsServiceModule", "gridServiceModule", "commonPopupServiceModule",
                                               "ui.date", "ngCkeditor"]);

//메시지
Constants.message_keys = ["common.label.alert.save", "dms.exhibit.oneday.date.validate", "common.label.confirm.save", "common.label.alert.cancel", "common.label.confirm.cancel"];

// 스타일샵 상품 이미지 관리
styleShopApp.controller("pms_styleShopManagerApp_controller", function($compile, $window, $scope, $filter, gridService, commonService, commonPopupService) {
	$window.$scope = $scope;
	$scope.sSearch = {};
	
	var	columnDefs = [
	   	              	{ field: 'styleProductNo'		, width: '10%'	, colKey: "c.pms.styleproduct.styleNo"			, linkFunction:'styleShopDetail' 				},
			            { field: 'productId'			, width: '10%'	, colKey: "c.pms.product.productId"				, linkFunction:'styleShopDetail'							},
			            { field: 'productName'			, width: '15%'	, colKey: "c.pms.styleproduct.product.name"														},
			            { field: 'brandId'				, width: '10%'	, colKey: "c.pmsBrand.brandId"																	},
			            { field: 'brandName'			, width: '15%'	, colKey: "c.pmsBrand.brand"																	},
			            { field: 'erpProductId'			, width: '10%'	, colKey: "pmsProduct.erpProductId" 															},
			            { field: 'styleProductColorName'	, width: '15%'	, displayName : "스타일상품컬러"																		},
			            { field: 'styleProductItemName'	, width: '10%'	, colKey: "c.pms.styleproduct.productItem"														},
			            { field: 'useYn'				, width: '10%'	, colKey: "c.ccs.code.useyn"					, cellFilter: "useYnFilter"						},
			            { field: 'insDt'				, width: '15%'	, colKey: "c.grid.column.insDt"																	},
			            { field: 'insId'				, width: '10%'	, colKey: "c.grid.column.insId"					, userFilter :'insId,insName'					},
			            { field: 'updDt'				, width: '15%'	, colKey: "c.grid.column.updDt"																	},
			            { field: 'updId'				, width: '10%'	, colKey: "c.grid.column.updId"					, userFilter :'updId,updName'					}
			         ];
	
	var gridParam = {
			scope : $scope, 					//mandatory
			gridName : "grid_styleShop",			//mandatory
			url :  '/api/pms/styleShop',  		//mandatory
			searchKey : "search",        		//mandatory
			columnDefs : columnDefs,    		//mandatory
			gridOptions : {             		//optional
			},
			callbackFn : function(){//optional
				
			}
	};
	
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	angular.element(document).ready(function () {
		commonService.init_search($scope,'search');
	});
	
	// 검색조건 초기화
	this.reset = function() {
		commonService.reset_search($scope, 'search');
		angular.element(".day_group").find('button:first').addClass("on");
	}
	
	$scope.productInfoType = [
	  	                   	{val : 'NAME',text : '상품명'},
	  	                    {val : 'PRODUCTID',text : '상품번호'}
	  		              ];
	
	
	// 브랜드 검색 팝업
	this.brandSearch = function() {
		commonPopupService.brandPopup($scope, "callback_brand", false);
	}
	
	$scope.callback_brand = function(data){
		$scope.search.brandId = data[0].brandId;
		$scope.search.brandName = data[0].name;
		$scope.$apply();		
	}
	
	this.eraser = function() {
		$scope.search.brandId = "";
		$scope.search.brandName = "";
	}
	
	// 상품 등록 팝업
	this.addProductPopup = function() {
		$scope.styleProductNo = "";
		var winURL = Rest.context.path +"/pms/styleShop/popup/detail";
		popupwindow(winURL,"스타일상품등록팝업",1000,650);
	}
	
	// 스타일샵상품 상세
	$scope.styleShopDetail = function(field, row) {
		$scope.styleProductNo = row.styleProductNo;
		var winURL = Rest.context.path +"/pms/styleShop/popup/detail";
		popupwindow(winURL,"스타일상품상세팝업",1000,650);
	}
	
	
}).controller("pms_styleShopPopApp_controller", function($window, $scope, $compile, commonPopupService, commonService, gridService, styleProductService) {
	var pScope = $window.opener.$scope;
	$scope.pmsStyleproduct = {};
	$scope.pmsStyleproduct.img = "";
	
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	// 등록 / 상세 팝업 닫기
	this.close = function(){
		if (!confirm($scope.MESSAGES["common.label.confirm.cancel"])) {
			return;
		}

		$window.close();
	}
	
	angular.element(document).ready(function () {
		if (pScope.styleProductNo != '') {
			styleProductService.getStyleProductDetail(pScope.styleProductNo, function(data) {
				console.log(data);
				$scope.pmsStyleproduct = data;
				window.resizeBy(0, 100);
			});
		} 
	});
	
	$scope.uploadDefaultImageCallback = function(path) {
		if ($scope.pmsStyleproduct.img != 'undefined' && $scope.pmsStyleproduct.img != '' && $scope.pmsStyleproduct.img != undefined) {
			$scope.ctrl.deleteImage(function() {
				$scope.pmsStyleproduct.img = path;		
			});
		} else {
			$scope.pmsStyleproduct.img = path;
		}
	}
	
	// 이미지 삭제
	this.deleteImage = function() {
		//신규 템프 파일은 바이너리 바로 삭제
		commonService.deleteFile($scope.pmsStyleproduct.img, function(data){
			//모델에서 삭제
			$scope.pmsStyleproduct.img = "";
	    });
	}
	
	// 상품 검색 팝업
	this.searchProduct = function() {
		commonPopupService.productPopup($scope, "callback_product", true);
	}
	
	$scope.callback_product = function(data) {
		$scope.pmsStyleproduct.productId = data[0].productId;
		$scope.pmsStyleproduct.productName = data[0].name;
		common.safeApply($scope);
	}
	
	// 상품Id, 상품명 삭제
	this.delText = function() {
		$scope.pmsStyleproduct.productId = ""; 
		$scope.pmsStyleproduct.productName = ""; 
	}
	// 스타일 상품 저장
	this.saveStyleShopProduct = function() {
		
		//폼 체크
		if (!commonService.checkForm($scope.form)) {
			return;
		}
		if ($scope.pmsStyleproduct.styleProductItemCd!='STYLE_PRODUCT_ITEM_CD.DECO') {
			if ($scope.pmsStyleproduct.productId == '' || $scope.pmsStyleproduct.productId == undefined) {
				alert("스타일분류가 꾸미기 이외에는 상품을 등록하여야 합니다.");
				return;
			}
		}
		
		
		if (pScope.styleProductNo != ''&& pScope.styleProductNo != undefined) {
			if (!confirm($scope.MESSAGES["common.label.confirm.save"])) {
				return;
			}
			styleProductService.saveStyleProduct($scope.pmsStyleproduct, function(response) {
				alert($scope.MESSAGES["common.label.alert.save"]);
				pScope.myGrid.loadGridData();
				$window.close();
			});
		} else {
			if (!confirm("스타일 상품을 생성하시겠습니까?")) {
				return;
			}
			$scope.pmsStyleproduct.crudType = "C";
			styleProductService.saveStyleProduct($scope.pmsStyleproduct, function(response) {
				alert("스타일 상품을 생성하였습니다.");
				pScope.myGrid.loadGridData();
				$window.close();
			});
		}

	}
	
});