var priceApprovalManagerApp = angular.module("priceApprovalManagerApp", ['commonServiceModule', 'pmsServiceModule', 'gridServiceModule', 'commonPopupServiceModule' , 'ui.date']);

Constants.message_keys = ["common.label.confirm.save"];

priceApprovalManagerApp.controller("pms_priceApprovalManagerApp_controller", function($window, $scope, $filter, commonService, productService, gridService, commonPopupService){
	
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	//******************************** 상품가격변경승인관리
	
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
		             { field: 'productId', 					width:100,			colKey:"pmsPricereserve.productId", linkFunction : "openProductPopup"},		             
		             { field: 'pmsProduct.name',			width:100,			colKey:"pmsProduct.name", linkFunction : "openProductPopup"},
		             { field: 'reserveDt',					width:100,			colKey:"pmsPricereserve.reserveDt"},
		             { field: 'priceReserveStateName',		width:100,				colKey:"pmsPricereserve.priceReserveStateCd"},
		             { field: 'pmsProduct.productTypeName',	width:100,			colKey:"pmsProduct.productTypeCd"},		             
		             { field: 'pmsProduct.pmsBrand.name',	 width:100,   	colKey:"pmsBrand.name"},
		             { field: 'pmsProduct.pmsCategory.ccsUser.name',/*상품MD*/	  width:100,  		                colKey:"c.pmsCategory.userName"},
		             { field: 'pmsProduct.ccsBusiness.name',	width:100,	colKey:"ccsBusiness.name"},
		             { field: 'listPrice',			type : 'number', 		width:100,					colKey:"pmsPricereserve.listPrice"},
		             { field: 'salePrice',			type : 'number', 		width:100,					colKey:"pmsPricereserve.salePrice"},
		             { field: 'supplyPrice',		type : 'number', 		width:100,					colKey:"pmsPricereserve.supplyPrice"},
		             { field: 'pmsProduct.erpProductId',		width:100, 					colKey: "pmsProduct.erpProductId" },
		             { field: 'commissionRate',		type : 'number', 		width:100,					colKey:"pmsPricereserve.commissionRate"},
		             { field: 'regularDeliveryPrice',	width:100,				colKey:"pmsPricereserve.regularDeliveryPrice"},
		             { field: 'rejectReason',		width:100,					colKey:"pmsPricereserve.rejectReason"},
		             { field: 'insId', 				width:100,					colKey: "c.grid.column.insId"  , userFilter :'insId,insName'},
		             { field: 'insDt', 				width:100,					colKey: "c.grid.column.insDt"  },
		             { field: 'updId', 				width:100,					colKey: "c.grid.column.updId"  , userFilter :'updId,updName'},		             
		             { field: 'updDt', 				width:100,					colKey: "c.grid.column.updDt"  }	
		         ];
	
	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : "grid_price_approval",	//mandatory
			url :  '/api/pms/product/priceApproval',  //mandatory
			searchKey : "search",       //mandatory
			columnDefs : columnDefs,    //mandatory
			gridOptions : {             //optional
				checkBoxEnable : true, checkMultiSelect : true
			},
			callbackFn : function(){	//optional
				//myGrid.loadGridData();
			}
	};
	
	//그리드 초기화
	$scope.priceApprovalGrid = new gridService.NgGrid(gridParam);

	
	//=================== search	
	$scope.searchPriceApproval = function(){
		$scope.priceApprovalGrid.loadGridData();
	}
	
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
	}
	//================= 지우개
	this.eraser = function(name){
		$scope.search[name+'Id'] = "";
		$scope.search[name+'Name'] = "";
	}
	
	//================= save

	this.checkStateCd = function(stateCd, gridRow, state){
		
		var gridStateCd = gridRow.priceReserveStateCd;
		
		if(stateCd == "PRICE_RESERVE_STATE_CD.APPROVAL"){
			if(!(gridStateCd == "PRICE_RESERVE_STATE_CD.REQ" 
				|| gridStateCd == "PRICE_RESERVE_STATE_CD.REJECT")){
				state.result = false;
				state.errProduct += gridRow.pmsProduct.name+", ";
//				state.stateName = $scope._auth_function["APPROVAL"];
				state.stateName = '승인';
				state.preStateName = "변경요청 or 반려";
			}
		}else if(stateCd == "PRICE_RESERVE_STATE_CD.REJECT"){
			if(!(gridStateCd == "PRICE_RESERVE_STATE_CD.REQ")){
				state.errProduct += gridRow.pmsProduct.name+", ";
				state.result = false;
				//state.stateName = $scope._auth_function["REJECT"];
				state.stateName = '반려';
				state.preStateName = "변경요청";
			}
		}				
	}
	
	this.checkStateGrid = function(stateCd){
		
		if(!$scope.priceApprovalGrid.isChecked()){
			return false;
		}
		
		var state = {result:true,errProduct:" "};
		
		for(var i=0;i<$scope.grid_price_approval.data.length;i++){
			if($scope.grid_price_approval.data[i].checked){
				this.checkStateCd(stateCd,$scope.grid_price_approval.data[i],state);									
			}
		}
		
		if(!state.result){
			var msg = state.stateName + "은(는) "+state.preStateName+" 상태만 가능합니다.\n오류 상품명:"+state.errProduct;
			alert(msg);
		}		
		
		return state.result;
	}
	
	this.approval = function(priceReserveStateCd){
		
		$scope.altProc = {priceReserveStateCd : priceReserveStateCd};		
					
		if(!this.checkStateGrid(priceReserveStateCd)){return false;}
		
		if($scope.altProc.priceReserveStateCd != "PRICE_RESERVE_STATE_CD.REJECT"){
		
//			for(var i=0;i<$scope.grid_price_approval.data.length;i++){
//				if($scope.grid_price_approval.data[i].checked){
//					$scope.grid_price_approval.data[i].priceReserveStateCd = $scope.altProc.priceReserveStateCd;
//					$scope.grid_price_approval.data[i].priceReserveStateName = commonService.getValueToName($scope,"altProc.priceReserveStateCd");
//				}
//			}
//			$scope.priceApprovalGrid.altData();
			$scope.altProcLayer = false;
			
			this.savePriceApproval(priceReserveStateCd);
			
//			alert("변경되었습니다. 저장하시면 반영됩니다.");
		}else{
			$scope.rejectLayer = true;	
		}	
	}
	
	this.saveAltProc = function(){
			
//		for(var i=0;i<$scope.grid_price_approval.data.length;i++){
//			if($scope.grid_price_approval.data[i].checked){
//				$scope.grid_price_approval.data[i].priceReserveStateCd = $scope.altProc.priceReserveStateCd;
//				$scope.grid_price_approval.data[i].priceReserveStateName = commonService.getValueToName($scope,"altProc.priceReserveStateCd");
//				$scope.grid_price_approval.data[i].rejectReason = $scope.altProc.rejectReason;
//			}
//		}
//		$scope.priceApprovalGrid.altData();
		
		$scope.rejectLayer = false;
		$scope.altProcLayer = false;
		
		this.savePriceApproval($scope.altProc.priceReserveStateCd,$scope.altProc.rejectReason);
		
//		alert("변경되었습니다. 저장하시면 반영됩니다.");
	}
	
	this.savePriceApproval = function(priceReserveStateCd,rejectReason){
//		$scope.priceApprovalGrid.saveGridData(null,function(){
//			$scope.searchPriceApproval();
//		});	
		if (window.confirm($scope.MESSAGES["common.label.confirm.save"])) {
			productService.updatePriceApproval(priceReserveStateCd,rejectReason,$scope.priceApprovalGrid.getSelectedRows(),function(response){
				$scope.priceApprovalGrid.loadGridData();
			});
		}
	}		
	
	//상품 상세 팝업
	$scope.openProductPopup = function(fieldValue, rowEntity){
		commonPopupService.openProductDetailPopup($scope, rowEntity.productId);
	}
		
});