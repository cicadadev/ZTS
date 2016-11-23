var stockManagerApp = angular.module("stockManagerApp", ['commonServiceModule', 'pmsServiceModule', 'gridServiceModule', 'commonPopupServiceModule'
                                                       , 'ui.date']);
Constants.message_keys = ["common.label.confirm.save"];

stockManagerApp.controller("pms_stockManagerApp_controller", function($window, $scope, $filter, commonService, productService, gridService, commonPopupService){
	
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	//******************************** 상품재고관리
	
	$scope.search = {};	
	
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});
	//document
	angular.element(document).ready(function () {		
		
		commonService.init_search($scope,'search');
	
	});	
 	
	//================ grid	
	var columnDefs = [
		             { field: 'pmsProduct.productId', 				width:100,		colKey:"pmsProduct.productId", linkFunction : "openProductPopup", type : "number"},
		             { field: 'pmsProduct.name', 					width:100,		colKey:"pmsProduct.name", linkFunction : "openProductPopup"},
		             { field: 'saleproductId',						width:100,		colKey:"pmsSaleproduct.saleproductId"},
		             { field: 'name',								width:100,		colKey:"pmsSaleproduct.name"},
		             { field: 'pmsProduct.productTypeName', 		width:100,		colKey:"pmsProduct.productTypeCd" },
		             { field: 'pmsProduct.saleStateName',	    	width:100,		colKey:"pmsProduct.saleStateCd"},
		             { field: 'pmsProduct.pmsBrand.name',	    	width:100,		colKey:"pmsBrand.name"},
		             { field: 'pmsProduct.erpProductId',			width:100, 		colKey: "pmsProduct.erpProductId" },
//		             { field: 'safeStockQty',	       	colKey:"pmsSaleproduct.safeStockQty", enableCellEdit:true},
//		             { field: 'pmsProduct.stockControlTypeName',	width:100,    	colKey:"pmsProduct.stockControlTypeCd"},
//		             { field: 'pmsProduct.stockControlTypeCd',	    	colKey:"pmsProduct.stockControlTypeCd"},
		             { field: 'realStockQty', name:'realStockQty',	width:100,       	colKey:"pmsSaleproduct.realStockQty", enableCellEdit:true , type : "number"},
		             { field: 'omsOrderproducts[0].orderQty',  	width:100,	colKey:"omsOrderproduct.orderQty", type : "number"},
		             { field: 'pmsProduct.baseCcsBusiness.name',	    width:100,    colKey:"ccsBusiness.name"},
		             { field: 'pmsProduct.pmsCategory.ccsUser.name',/*상품MD*/ width:100, colKey:"c.pmsCategory.userName"},
		             { field: 'insId', 									width:100,	userFilter :'insId,insName',	colKey: "c.grid.column.insId"  },
		             { field: 'insDt', 									width:100,	colKey: "c.grid.column.insDt"  },
		             { field: 'updId', 									width:100,	userFilter :'updId,updName',	colKey: "c.grid.column.updId"  },
		             { field: 'updDt', 									width:100,	colKey: "c.grid.column.updDt"  }	
		             
		         ];
	
	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : "grid_stock",	//mandatory
			url :  '/api/pms/product/stock',  //mandatory
			searchKey : "search",       //mandatory
			columnDefs : columnDefs,    //mandatory
			gridOptions : {             //optional
				checkBoxEnable : false				
			},
			callbackFn : function(){	//optional
				//myGrid.loadGridData();
			}
	};
	
	//그리드 초기화
	$scope.stockGrid = new gridService.NgGrid(gridParam);

	
	//=================== search	
	$scope.searchStock = function(){
		$scope.stockGrid.loadGridData();		
	}
	
//	var allCheck = false;
//	this.selectGubunAll = function(){
//		if(allCheck){
//			allCheck = false;
//		}else{
//			allCheck = true;
//		}
//		
//		$scope.search.normal = allCheck;
//		$scope.search.reserveYn = allCheck;
//		$scope.search.offshopPickupYn = allCheck;
//		$scope.search.regularDeliveryYn = allCheck;
//		$scope.search.fixedDeliveryYn = allCheck;
//		$scope.search.giftYn = allCheck;
//		$scope.search.wrapYn = allCheck;			
//	}
	
	this.businessPopup = function(){
		commonPopupService.businessPopup($scope,"callback_business",false);
	}
	$scope.callback_business = function(data){
		$scope.search.businessId = data[0].businessId;
		$scope.search.businessName = data[0].name;
		$scope.$apply();		
	}
	
	
	this.brandPopup = function(){
		commonPopupService.brandPopup($scope,"callback_brand",false);
	}
	$scope.callback_brand = function(data){
		$scope.search.brandId = data[0].brandId;
		$scope.search.brandName = data[0].name;
		$scope.$apply();		
	}
	
	// 카테고리 검색 팝업
	this.openCategoryPopup = function(flag) {
		var url = '/' + flag + '/popup/search';
		commonPopupService.categoryPopup($scope, "callback_category", false, url);
	}
	// 카테고리 callback function
	$scope.callback_category = function(data, flag) {
		if (flag == 'pms') {
			$scope.search.categoryId = data.categoryId;
			$scope.search.categoryName = data.name;
		} else {
			$scope.search.dispCategoryId = data.displayCategoryId;
			$scope.search.dispCategoryName = data.name;
		}
		$scope.$apply();
	}
	
	this.userPopup = function(){
		commonPopupService.userPopup($scope,"callback_user",false,"USER_TYPE_CD.MD");
	}
	$scope.callback_user = function(data){
		$scope.search.userId = data[0].userId;
		$scope.search.userName = data[0].name;
		$scope.$apply();		
	}	
	
	//================= reset
	this.reset = function(){		
		commonService.reset_search($scope,'search');
		angular.element(".day_group").find('button:first').addClass("on");
		$scope.tmp = {};	// checkbox 임시 저장값 초기화
	}
	//================= 지우개
	this.eraser = function(name){
		$scope.search[name+'Id'] = "";
		$scope.search[name+'Name'] = "";
	}
	
	//================= save
	this.saveStock = function(){
		$scope.stockGrid.saveGridData(null,function(){
			$scope.searchStock();
		});
		
//		if(window.confirm($scope.MESSAGES["common.label.confirm.save"])){
//			productService.updateStock($scope.stockGrid.getSelectedRows(),function(response){
//				this.searchStock
//			});	
//		}		
	}
	
	//상품 상세 팝업
	$scope.openProductPopup = function(fieldValue, rowEntity){
		commonPopupService.openProductDetailPopup($scope, rowEntity.productId);
	}
	
});
