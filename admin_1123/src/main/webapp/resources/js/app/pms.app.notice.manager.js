var noticeManagerApp = angular.module("noticeManagerApp", ['commonServiceModule', 'pmsServiceModule', 'gridServiceModule', 'commonPopupServiceModule'
                                                       , 'ui.date']);
Constants.message_keys = ["common.label.confirm.save"];

noticeManagerApp.controller("pms_noticeManagerApp_controller", function($window, $scope, $filter, commonService, productService, gridService, commonPopupService){
	//******************************** 상품품목정보관리
	$scope.search = {};
	
	//=============== init
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});
	
	//document
	angular.element(document).ready(function () {		
		$scope.search.productNoticeTypeCd = "1";
		
		commonService.init_search($scope,'search');
	
	});	
	
	// PO로그인일경우 업체ID
	var poBusinessId = global.session.businessId=='null' ? null : global.session.businessId;
	$scope.poBusinessId = poBusinessId;
	
	//================ grid	
	$scope.makeGrid = function(colData){
		var columnDefs = [
							{ field: 'productId', 				width:100,				colKey:"pmsPricereserve.productId", linkFunction : "openProductPopup" },		             
							{ field: 'name',					width:100,	colKey:"pmsProduct.name", linkFunction : "openProductPopup"},
							{ field: 'saleStateName',			width:100,		colKey:"pmsProduct.saleStateCd"},
							{ field: 'noticeConfirmYn',		width:100,			colKey:"pmsProduct.noticeConfirmYn"},
							{ field: 'rejectReason',			width:100,		colKey:"pmsProduct.rejectReason"},
							{ field: 'erpProductId',			width:100,		colKey: "pmsProduct.erpProductId" },
							{ field: 'productNoticeTypeName',			width:100,		colKey:"pmsProduct.productNoticeTypeCd"}
                  	]

		for(var i=0;i<colData.length;i++){
			columnDefs.push({ field: 'detail' + (i+1),/*품목*/width:100,	displayName:colData[i].title});
		}
		
		
		columnDefs.push({ field: 'pmsCategory.ccsUser.name',/*상품MD*/width:100,	colKey:"c.pmsCategory.userName"});
		columnDefs.push({ field: 'insId', 			width:100,						colKey: "c.grid.column.insId"  , userFilter :'insId,insName'});
		columnDefs.push({ field: 'insDt', 			width:100,						colKey: "c.grid.column.insDt"  });
		columnDefs.push({ field: 'updId', 			width:100,						colKey: "c.grid.column.updId"  , userFilter :'updId,updName'});
		columnDefs.push({ field: 'updDt', 			width:100,						colKey: "c.grid.column.updDt"  });

		var gridParam = {
				scope : $scope, 			//mandatory
				gridName : "grid_notice",	//mandatory
				url :  '/api/pms/product/notice',  //mandatory
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
		$scope.noticeGrid = new gridService.NgGrid(gridParam);
	}
	var columnDefs = [
		             { field: 'productId', 			width:100,					colKey:"pmsPricereserve.productId"},		             
		             { field: 'name',				width:100,				colKey:"pmsProduct.name"},
		             { field: 'saleStateName',		width:100,				colKey:"pmsProduct.saleStateCd"},
		             { field: 'noticeConfirmYn',		width:100,			colKey:"pmsProduct.noticeConfirmYn"},
		             { field: 'rejectReason',		width:100,				colKey:"pmsProduct.rejectReason"},
		             { field: 'productNoticeTypeName',	width:100,				colKey:"pmsProduct.productNoticeTypeCd"},
		             { field: 'pmsCategory.ccsUser.name',/*상품MD*/width:100,	colKey:"c.pmsCategory.userName"},
		             { field: 'insId', 				width:100,					colKey: "c.grid.column.insId"  , userFilter :'insId,insName'},
		             { field: 'insDt', 				width:100,					colKey: "c.grid.column.insDt"  },
		             { field: 'updId', 				width:100,					colKey: "c.grid.column.updId"  , userFilter :'updId,updName'},
		             { field: 'updDt', 				width:100,					colKey: "c.grid.column.updDt"  }	
		             
		         ];
	
	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : "grid_notice",	//mandatory
			url :  '/api/pms/product/notice',  //mandatory
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
	$scope.noticeGrid = new gridService.NgGrid(gridParam);
	

	$scope.$watch('search.productNoticeTypeCd',function(value){
		
		if(!(angular.isUndefined(value) || value == '')){
			productService.getProductnoticefieldList($scope.search,function(response){
				$scope.makeGrid(response);				
			});
		}
	});
	
	//=================== search	
	$scope.searchNotice = function(){
		$scope.noticeGrid.loadGridData();
	}
	
	// 각 검색 팝업
	this.openPopup = function(kindOf) {
		if(kindOf == 'brand') {
			commonPopupService.brandPopup($scope,"callback_brand", false);
			$scope.callback_brand = function(data){
				$scope.search.brandId = data[0].brandId;
				$scope.search.brandName = data[0].name;
				
				common.safeApply($scope);
			}
		}else if(kindOf == 'md'){
			commonPopupService.userPopup($scope,"callback_user",false,"USER_TYPE_CD.MD");
			$scope.callback_user = function(data){
				$scope.search.userId = data[0].userId;
				$scope.search.userName = data[0].name;
				common.safeApply($scope);		
			}
		}
	}
	
	//================= reset
	this.reset = function(){		
		commonService.reset_search($scope,'search');
	}
	
	//================= 지우개
	this.eraser = function(name){
		$scope.search[name+'Id'] = "";
		$scope.search[name+'Name'] = "";
	}
	
	//================= save

	
	// 변경 전 상태 체크
	this.checkStateCd = function(gridRow){
		
		var success = true;
		
		var targetState = gridRow.saleStateCd;//상품의 상태
		
		if(targetState=='SALE_STATE_CD.REQ' && gridRow.noticeConfirmYn!='Y' ){
			success = true;
		}else{
			success = false;
		}
		return success;
	}
	
	
	this.saveNotice = function(){
		
		
		if(!$scope.noticeGrid.isChecked()){
			return false;
		}
		if(!confirm("저장 하시겠습니까?")){
			return;
		}
		var productList = [];
		var datas = $scope.noticeGrid.getData();
		
		var failMessages = "품목정보확인 불가한 상태의 상품들이 존재합니다.\n\n";
		var failCnt = 0;
		for(var i=0;i< datas.length;i++){
			
			var productId = datas[i].productId;
			
			if(datas[i].checked){
				
				var success = this.checkStateCd(datas[i]);				
				
				if(!success){
					failCnt++;
					failMessages += productId + "\n";
				}else{
					var updateInfo = {productId : productId, noticeConfirmYn : 'Y'};
					productList.push(updateInfo);
				}
				
			}
		}
		if(failCnt==0){
			productService.updateNoticeConfirm(productList, function(){
				alert("저장 하였습니다.");
				$scope.noticeGrid.loadGridData();
				
			});
		}else{
			alert(failMessages);
		}
	}
	
	// 반려 레이어 오픈
	this.openRejectLayer = function(){
		
		if(!$scope.noticeGrid.isChecked()){
			return false;
		}
		
		$scope.rejectLayer = true;
		$scope.rejectReason = '';
	}
	
	$scope.closeRejectLayer = function(){
		
		$scope.rejectLayer = false;
		$scope.rejectReason = '';
	}
	
	
	this.reject = function(){
		
		if(!$scope.noticeGrid.isChecked()){
			return false;
		}
		
		if(!confirm("저장 하시겠습니까?")){
			return;
		}
		
		var failMessages = "반려 처리 불가한 상품들이 존재합니다.\n상품의 상태가 승인전이고, 품목정보확인 처리가 안된 상품만 반려처리 가능합니다.\n\n";
		var failCnt = 0;
		var productList = [];
		var datas = $scope.noticeGrid.getData();
		for(var i=0;i< datas.length;i++){
			
			if(datas[i].checked){
				var productId =  datas[i].productId;
				
				var success = this.checkStateCd(datas[i]);
				
				if(!success){
					failCnt++;
					failMessages += productId + "\n";
				}else{
					var updateInfo = {productId : productId, saleStateCd : "SALE_STATE_CD.REJECT", rejectReason : $scope.rejectReason };
					productList.push(updateInfo);
				}
			}
		}
		
		if(failCnt>0){
			alert(failMessages);
		}else{
			productService.updateApproval(productList, function(response){
				alert("저장 하였습니다.");
				$scope.noticeGrid.loadGridData(); 
			});
		}		
		
		$scope.closeRejectLayer();
	}
	
	// 상품일괄 (품록정보)
	$scope.bulkType = '';
	this.bulk = {
		download : {
//			console.log("########### download ########");
//			return alert('download');
		},
		upload : {
			excel : function(flag) {
				$scope.bulkType = flag;
				var winName = 'Product Bulk Upload';
				var winURL = Rest.context.path + "/pms/product/popup/bulkupload";

				switch (flag) {
					case 5:
						winName += ' (품목정보 일괄등록)';
						break;
				}
				$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
				popupwindow(winURL, winName, 1100, 400);
			}
		}
	}

	//상품 상세 팝업
	$scope.openProductPopup = function(fieldValue, rowEntity){
		commonPopupService.openProductDetailPopup($scope, rowEntity.productId);
	}
	
});
