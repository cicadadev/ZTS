var epexcApp = angular.module("epexcApp", [ 'commonServiceModule', 'commonPopupServiceModule', 'gridServiceModule', 'pmsServiceModule',
                                           	'ui.date' ]);
//메시지
Constants.message_keys = ["pms.epexc.register.already"];

epexcApp.controller("pms_epexcProductListApp_controller", function($window, $scope, $filter, commonService, commonPopupService, gridService, epexcService) {
	//부모 scope 팝업에서 사용가능하도록 설정
	$window.$scope = $scope;

	$scope.search = {};
	
	$scope.infoType = [
	                    {val : 'ID',text : '상품번호'},
	                    {val : 'NAME',text : '상품명'}
		              ];
	
	angular.element(document).ready(function () {
		commonService.init_search($scope, 'search');
	});

	var	columnDefs = [
		                 { field: 'excProductTypeName'	, width: '6%',	colKey: 'c.pms.epexcproduct.divType' },
		                 { field: 'businessInfo'		, width: '20%',	colKey: 'c.pms.epexcproduct.business' },
		                 { field: 'productId'			, width: '10%',	colKey: 'c.pms.epexcproduct.productNo', linkFunction: 'getProductNo' },
		                 { field: 'pmsProduct.name'				, width: '15%',	colKey: 'pmsProduct.name', linkFunction: 'getProductNo' },
		                 { field: 'pmsProduct.saleStateName'	, width: '10%',	colKey: 'c.pms.epexcproduct.saleState' },
		                 { field: 'pmsProduct.productTypeName'	, width: '10%',	colKey: 'c.pms.epexcproduct.productType' },
		                 { field: 'insId'				, width: '10%',	colKey: 'c.grid.column.insId', userFilter: 'insId,insName' },
		                 { field: 'insDt'				, width: '10%',	colKey: 'c.grid.column.insDt', cellFilter: "date:\'yyyy-MM-dd\'" },
		                 { field: 'updId'				, width: '10%',	colKey: 'c.grid.column.updId', userFilter: 'updId,updName' },
		                 { field: 'updDt'				, width: '10%',	colKey: 'c.grid.column.updDt', cellFilter: "date:\'yyyy-MM-dd\'" }
		             ];

	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : "grid_epexc",	//mandatory
			url :  '/api/pms/epexcproduct',  //mandatory
			searchKey : "search",     //mandatory
			columnDefs : columnDefs,    //mandatory
			gridOptions : {             //optional
				enableCellEdit : false	//셀 수정 가능여부
				//enableHorizontalScrollbar : 2
				//enableSorting : false
			},
			callbackFn : function() {	//optional
			}
	};

	//그리드 객체생성
	$scope.myGrid = new gridService.NgGrid(gridParam);

	//message 정의
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	this.reset = function() {
		commonService.reset_search($scope, 'search');
//		console.log("scope==> ", $scope);
		angular.element(".day_group").find('button:first').addClass("on");
	}

	//각 검색 팝업
	this.openPopup = function(kindOf) {
		if(kindOf == 'business') {
			//업체 검색 팝업
			commonPopupService.businessPopup($scope,"callback_business", false);
			$scope.callback_business = function(data) {
				$scope.search.businessId = data[0].businessId;
				$scope.search.businessName = data[0].name;
				$scope.$apply();		
			}
		} else if(kindOf == 'brand') {
			//브랜드 검색 팝업
			commonPopupService.brandPopup($scope,"callback_brand", false);
			$scope.callback_brand = function(data) {
				$scope.search.brandId = data[0].brandId;
				$scope.search.brandName = data[0].name;
				$scope.$apply();		
			}
		}
	}

	//지우개
	this.eraser = function(name) {
		$scope.search[name+'Id'] = "";
		$scope.search[name+'Name'] = "";
	}

//	//Grid: product ID 선택 호출
//	$scope.getProductNo = function(fieldValue, rowEntity) {
//		if (rowEntity.productId == '-') {
//			return;
//		}
////		$scope.productId = { productId : rowEntity.productId };
////		console.log($scope.productId);
//		$scope.selectedProductId = rowEntity.productId;
//		
//		var url = Rest.context.path + '/pms/product/popup/detail';
//		popupwindow(url, "Product Detail", 1300, 800);
//	}

	//상품 상세 팝업
	$scope.getProductNo = function(fieldValue, rowEntity){
		commonPopupService.openProductDetailPopup($scope, rowEntity.productId);
	}
	
	//업체등록을 위한 업체검색 공통 팝업 호출
	this.regSellerPopup = function(){
		commonPopupService.businessPopup($scope, "callback_business", false);
	}
	//업체 검색 후 DB에 등록
	$scope.callback_business = function(data) {
//		console.log("biz: ", data[0]);
		saveEpexcproduct('EXC_PRODUCT_TYPE_CD.BUSINESS', data[0].businessId, null);
	}

	//상품등록을 위한 상품검색 공통 팝업 호출
	this.regProductPopup = function(){
		commonPopupService.productPopup($scope, "callbackProductPopup", false);
	}
	//상품 검색 후 DB에 등록
	$scope.callbackProductPopup = function(data){
//		console.log("product: ", data[0]);
		saveEpexcproduct('EXC_PRODUCT_TYPE_CD.PRODUCT', data[0].ccsBusiness.businessId, data[0].productId);
	}

	//그리드 항목 삭제: 삭제버튼 클릭시 DB 바로 삭제로 변경
//	this.deleteGridRows = function() {
//		$scope.myGrid.deleteRow();
//		console.log("grid: ", $scope.grid_epexc);
//	}
	
	//외부 비노출 정보 등록
	saveEpexcproduct = function(type, bizId, prdId) {
		var paramData;
		if (type == 'EXC_PRODUCT_TYPE_CD.BUSINESS') {
			paramData = { excProductTypeCd: type, searchId: bizId };
		} else if (type == 'EXC_PRODUCT_TYPE_CD.PRODUCT') {
			paramData = { excProductTypeCd: type, searchId: prdId };
		}

		//테이블에서 중복 검색
		epexcService.checkDuplication(paramData, function(response) {
			console.log("response: ", response);
			if ("Y" == response.content) {
				alert($scope.MESSAGES["pms.epexc.register.already"]);
				return;
			} else {
				//중복 없으면 등록
				$scope.pmsEpexcproduct = {
					excProductTypeCd : type,
					businessId : bizId,
					productId : prdId
				};
				console.log("save_model: ", $scope.pmsEpexcproduct);
				epexcService.insertEpexcproduct($scope.pmsEpexcproduct, function(response) {
//					console.log("response: ", response);
					
					$scope.myGrid.loadGridData();
				});
			}
		});
	}

});