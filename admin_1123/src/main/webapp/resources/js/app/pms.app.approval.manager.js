var approvalManagerApp = angular.module("approvalManagerApp", ['commonServiceModule', 'pmsServiceModule', 'gridServiceModule', 'commonPopupServiceModule'
                                                       , 'ui.date']);
Constants.message_keys = ["common.label.confirm.save"];

approvalManagerApp.controller("pms_approvalManagerApp_controller", function($window, $scope, $filter, commonService, productService, gridService, commonPopupService){		
	
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	//******************************** 상품승인관리
	
	$scope.search = {};
	
	// PO로그인일경우 업체ID
	var poBusinessId = global.session.businessId=='null' ? null : global.session.businessId;
	$scope.poBusinessId = poBusinessId;
	
	
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
		             { field: 'productId', 								width:100, colKey:"pmsProduct.productId", linkFunction : "openProductPopup"},
//		             { field: 'insDt', 									width:100, colKey:"c.grid.column.insDt"},
		             { field: 'name', 									width:100, colKey:"pmsProduct.name", linkFunction : "openProductPopup"},
		             { field: 'productTypeName',						width:100, colKey:"pmsProduct.productTypeCd"},		             
		             { field: 'pmsBrand.name',	    					width:100, colKey:"pmsBrand.name"},
		             { field: 'saleStateName',							width:100, colKey:"pmsProduct.saleStateCd"},
		             { field: 'noticeConfirmYn',						width:100, colKey:"pmsProduct.noticeConfirmYn"},
		             { field: 'rejectReason',							width:100, colKey:"pmsProduct.rejectReason"},
		             { field: 'ccsBusiness.name',						width:100, colKey:"ccsBusiness.name"},
		             { field: 'pmsCategory.ccsUser.name',/*상품MD*/	    width:100, 		                colKey:"c.pmsCategory.userName"},
		             { field: 'pmsCategory.secondApprovalYn',	    	width:100, 	                colKey:"pmsCategory.secondApprovalYn"},
		             { field: 'listPrice',								type : 'number', 		width:100, colKey:"pmsProduct.listPrice"},
		             { field: 'salePrice',								type : 'number', 		width:100, colKey:"pmsProduct.salePrice"},
		             { field: 'supplyPrice',							type : 'number', 		width:100, colKey:"pmsProduct.supplyPrice"},
		             { field: 'erpProductId',							width:100, colKey: "pmsProduct.erpProductId" },
		             { field: 'commissionRate',							type : 'number', 		width:100, colKey:"pmsProduct.commissionRate"},
		             { field: 'deliveryFeeFreeYn',						width:100, colKey:"pmsProduct.deliveryFeeFreeYn"},
		             { field: 'insId', 									width:100, userFilter :'insId,insName',	colKey: "c.grid.column.insId"  },
		             { field: 'insDt', 									width:100, colKey: "c.grid.column.insDt"  },
		             { field: 'updId', 									width:100, userFilter :'updId,updName',	colKey: "c.grid.column.updId"  },
		             { field: 'updDt', 									width:100, colKey: "c.grid.column.insDt"  }	
		             
		         ];
	
	var gridParam = {
			scope : $scope, 			//mandatory
			gridName : "grid_approval",	//mandatory
			url :  '/api/pms/product/approval',  //mandatory
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
	$scope.approvalGrid = new gridService.NgGrid(gridParam);

	
	//=================== search	
	$scope.searchApproval = function(){
		$scope.approvalGrid.loadGridData();
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
	
	// 판매중 이후 상태 체크
	this.isSaleState = function(state){
		if(state=="SALE_STATE_CD.SALE" || state=="SALE_STATE_CD.SOLDOUT" 
			|| state=="SALE_STATE_CD.STOP" || state=="SALE_STATE_CD.MDSTOP"
			|| state=="SALE_STATE_CD.END"){
			
			return true;
		}
		return false;
	}
	
	// 변경 전 상태 체크
	this.checkStateCd = function(productId, nextState, gridRow){
		
		var result = {
				success : true,
				failMessages : "",
				saveObj : { productId : productId, nextStateCd : '', rejectReason : ''}
			};
		
		var targetState = gridRow.saleStateCd;							//상품의 상태
		var secondApprovalYn = gridRow.pmsCategory.secondApprovalYn;	//2차승인 여부
		var noticeConfirmYn = gridRow.noticeConfirmYn;					//품목확인여부
		
		// 판매중 이후 상태는 불가
		if(this.isSaleState(targetState)){
			result.success = false;
			result.failMessages = productId + " : 변경 불가 상태(판매중)";
			
			return result;
		}
		//case 1 : MD승인
		if(nextState=='mdApproval'){
			
			if(targetState=='SALE_STATE_CD.APPROVAL1' ){
				result.success = false;
				result.failMessages = productId + " : 변경 불가 상태(QC검수요청)";
			}
			else if(targetState=='SALE_STATE_CD.APPROVAL2'){
				result.success = false;
				result.failMessages = productId + " : 변경 불가 상태(QC검수완료)";
			}else if(targetState=='SALE_STATE_CD.REJECT' ){
				result.success = false;
				result.failMessages = productId + " : 변경 불가 상태(반려)";
			}else if(noticeConfirmYn != 'Y'){
				result.success = false;
				result.failMessages = productId + " : 품목정보확인 미완료";
			}else{
				// 다음상태 진행
				if(secondApprovalYn=='Y'){
					// 2차승인이면 qc검수요청상태
					result.saveObj.nextStateCd = "SALE_STATE_CD.APPROVAL1";
				}else{
					// 1차 승인이면 판매중상태
					result.saveObj.nextStateCd = "SALE_STATE_CD.SALE";
				}
			}
			
		}
		
		//case 2 : MD반려
		if(nextState=='mdReject'){
			if(targetState=='SALE_STATE_CD.REJECT' ){
				result.success = false;
				result.failMessages = productId + " : 변경 불가 상태(반려)";
			}else{
				result.saveObj.nextStateCd = "SALE_STATE_CD.REJECT";
				result.saveObj.rejectReason =  $scope.rejectReason; 
			}
		}
		
		//case 3 : QC승인
		if(nextState=='qcApproval'){
			
			if(targetState=='SALE_STATE_CD.REQ' ){
				result.success = false;
				result.failMessages = productId + " : 변경 불가 상태(승인전)";
			}else if(targetState=='SALE_STATE_CD.REJECT' ){
				result.success = false;
				result.failMessages = productId + " : 변경 불가 상태(반려)";
			}
			else if(targetState=='SALE_STATE_CD.APPROVAL2' ){
				result.success = false;
				result.failMessages = productId + " : 변경 불가 상태(QC검수완료)";
			}else{
				result.saveObj.nextStateCd = "SALE_STATE_CD.APPROVAL2";
			}
		}
		
		
		//case 4 : QC반려
		if(nextState=='qcReject'){
			
			if(targetState=='SALE_STATE_CD.REQ' ){
				result.success = false;
				result.failMessages = productId + " : 변경 불가 상태(승인전)";
			}else if(targetState=='SALE_STATE_CD.REJECT' ){
				result.success = false;
				result.failMessages = productId + " : 변경 불가 상태(반려)";
			}
			else if(targetState=='SALE_STATE_CD.APPROVAL2' ){
				result.success = false;
				result.failMessages = productId + " : 변경 불가 상태(QC검수완료)";
			}else{
				result.saveObj.nextStateCd = "SALE_STATE_CD.REJECT";
				result.saveObj.rejectReason =  $scope.rejectReason;
			}
		}
		
		
		//case 5 : 판매개시
		if(nextState=='saleStart'){
			if(targetState=='SALE_STATE_CD.REQ' ){
				result.success = false;
				result.failMessages = productId + " : 변경 불가 상태(승인전)";
			}else if(targetState=='SALE_STATE_CD.REJECT' ){
				result.success = false;
				result.failMessages = productId + " : 변경 불가 상태(반려)";
			}
			else if(targetState=='SALE_STATE_CD.APPROVAL1' ){
				result.success = false;
				result.failMessages = productId + " : 변경 불가 상태(QC검수요청)";
			}else{
				result.saveObj.nextStateCd = "SALE_STATE_CD.SALE";
			}
		}
		
		return result;
	}
	// 반려 레이어 오픈
	this.openRejectLayer = function(nextState){
		
		if(!$scope.approvalGrid.isChecked()){
			return false;
		}
		
		$scope.rejectLayer = true;
		$scope.rejectReason = '';
		$scope.rejectState = nextState;
	}
	
	$scope.closeRejectLayer = function(){
		
		$scope.rejectLayer = false;
		$scope.rejectReason = '';
		$scope.rejectState = '';
	}
	
	// 상태 변경
	this.changeState = function(nextState){
		
		if(!$scope.approvalGrid.isChecked()){
			return false;
		}
		
		if(!confirm("저장 하시겠습니까?")){
			return;
		}
		
		var failMessages = "다음과 같은 사유로 상태변경 불가합니다.\n\n";
		var failCnt = 0;
		var productList = [];
		var datas = $scope.grid_approval.data;
		for(var i=0;i< datas.length;i++){
			
			if(datas[i].checked){
				var productId =  datas[i].productId;
				var result = this.checkStateCd(productId, nextState, datas[i]);				
				
				if(!result.success){
					failCnt++;
					failMessages += result.failMessages+"\n";
				}else{
					var updateInfo = {productId : productId, saleStateCd : result.saveObj.nextStateCd, rejectReason : $scope.rejectReason };
					productList.push(updateInfo);
				}
			}
		}
		
		if(failCnt > 0){
			alert(failMessages);
		}else{
			productService.updateApproval(productList, function(response){
				alert("저장 하였습니다.");
				$scope.approvalGrid.loadGridData();
			});
		}
		
		$scope.closeRejectLayer();
	}

	//상품 상세 팝업
	$scope.openProductPopup = function(fieldValue, rowEntity){
		commonPopupService.openProductDetailPopup($scope, rowEntity.productId);
	}
	
});
