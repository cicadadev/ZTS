var ccsAppBusiness = angular.module("businessApp", ['commonServiceModule', 'ccsServiceModule', 'gridServiceModule', 'commonPopupServiceModule' , 'ui.date', 'ngCkeditor']);

Constants.message_keys = ["common.label.alert.save", "common.label.confirm.save", "common.label.alert.cancel", "common.label.confirm.cancel", "common.label.alert.delete", "common.label.confirm.delete"];

ccsAppBusiness.controller("businessInsertController", function($window, $scope, $filter, businessService, commonService, gridService, commonPopupService,commonFactory) {
	$window.$scope = $scope;//
	$scope.ccsBusiness = {};
	$scope.ccsBusiness.ccsCommissions =[];
	var pScope = $scope;// 부모창의 scope
	var columnDefs =  [{ field: 'categoryId', 					cellClass : 'alignC',		displayName:"표준카테고리번호", 	colKey: "ccsCommission.businessId"},
	                   { field: 'pmsCategory.depthFullName', 	cellClass : 'alignC',		displayName:"표준카테고리명",		colKey: "pmsCategory.name"},
	                   { field: 'userId', 						cellClass : 'alignC',		displayName:"담당 MD",			colKey: "pmsCategory.name"}
	   ];

	$scope.saleTypes = [
	                    {val : 'SALE_TYPE_CD.CONSIGN',text : '위탁'},
	                    {val : 'SALE_TYPE_CD.PURCHASE',text : '직매입(사입)'}
	                   ];
	
	$scope.taxTypes = [
	                    {val : 'BUSINESS_TAX_TYPE_CD.CORPORATE',text : '법인과세자'},
	                    {val : 'BUSINESS_TAX_TYPE_CD.GENERAL',text : '일반과세자'},
	                    {val : 'BUSINESS_TAX_TYPE_CD.SIMPLE',text : '간이과세자'},
	                    {val : 'BUSINESS_TAX_TYPE_CD.FREE',text : '면세사업자'}
	                   ];
	
	$scope.taxYns = [
	                    {val : 'Y',text : '과세'},
	                    {val : 'N',text : '면세'}
	                   ];
	
	var gridParam = {
			scope : $scope,
			gridName : "grid_commission",
			url : '/api/ccs/business/commission',
			searchKey : "search",
			columnDefs : columnDefs,
			gridOptions : { pagination : false 
			},
			callbackFn : function(){	//optional,
//				common.resizeIframe();
			}
	};

	$scope.commissionGrid = new gridService.NgGrid(gridParam);
	
	
//	angular.element(document).ready(function () {
//		$scope.ccsBusiness.contractStartDt = $filter('date')(new Date(), Constants.date_format_1);
//		$scope.ccsBusiness.contractEndDt = $filter('date')(new Date(), Constants.date_format_1);
//	});
	
	//수정
	this.updateBusiness = function(){
		//폼 체크
		if(!confirm("저장 하시겠습니까?")){
			return;
		}
		if(!commonService.checkForm($scope.form2)){
			return;
		}
		
		var newPwd = $scope.ccsBusiness.pwd1;
		var comfPwd = $scope.ccsBusiness.pwd2;
		
		
		// 사용자가 새로운 비밀번호를 입력하였을 경우
		if (angular.isDefined(newPwd) && angular.isDefined(comfPwd)) {
			if (newPwd == comfPwd) {
				$scope.ccsBusiness.reqUserPwd = newPwd;
			} else {
				alert('비밀번호를 다시 확인해주세요.');
				return;
			}
		}
		
		
//		// 매입유형이 위탁이고 위탁매입유형이 'N'일 경우
//		if ($scope.grid_commission.data.length == 0 && ($scope.ccsBusiness.saleTypeCd=='SALE_TYPE_CD.CONSIGN' && $scope.ccsBusiness.purchaseYn=='N')) {
//			alert("업체 카테고리를 등록 해주세요");
//			return;
//		}
//		
		//수수료 그리드 Data setting
		$scope.ccsBusiness.ccsCommissions = $scope.commissionGrid.getData();

		$scope.ccsBusiness.businessTypeCd = 'BUSINESS_TYPE_CD.SELLER';

		// 정산방식 기본값 세팅
		$scope.ccsBusiness.saleTypeCd=='SALE_TYPE_CD.CONSIGN';
		$scope.ccsBusiness.purchaseYn = 'N';	
		
		businessService.saveBusiness($scope.ccsBusiness, function(response) {
			alert("신청되었습니다.");
			$window.close();
		});
	}
	
	this.pmsCategoryPopup = function(){
		$scope.search.categoryRootId = global.config.rootCategoryId;
		commonPopupService.categoryPopup($scope, "callback_category", false, '/pms/category/popup/search');
	}
	
	// 카테고리 팝업
	$scope.callback_category = function(data) {
		var flag = true;
		for (var i = 0; i < $scope.grid_commission.data.length; i++) {
			if ($scope.grid_commission.data[i].categoryId == data.categoryId) {
				flag = false;
				break;
			}
		}
		if (flag) {
			//console.log(data)
			$scope.commissionGrid.addRow({
				businessId : pScope.businessId,
				commissionId : "",
				categoryId : data.categoryId,
				pmsCategory :{depthFullName : data.depthFullName},
				userId : data.ccsUser.name,
				strCommissionRate: "0",
				insDt : "",
				insId : ""
			});
		}
	}
	
	this.close = function(){
		if(!confirm("취소하시겠습니까?")){
			return;
		}
		alert('취소되었습니다.');
		$window.close();
//		location.href="/ccs/loginpo";
	}
	
	// 주소검색
	this.searchAddress = function() {
		commonPopupService.openAddressPopup($scope, 'calback_address');
	}
	 
	$scope.calback_address = function(data) {
		$scope.ccsBusiness.zipCd = data.postNo; 
		$scope.ccsBusiness.address1 = data.address1;
		$scope.ccsBusiness.address2 = data.address2;
		common.safeApply($scope);
	}
	
	this.eraser = function() {
		$scope.ccsBusiness.zipCd = ""; 
		$scope.ccsBusiness.address1 = "";
		$scope.ccsBusiness.address2 = "";
	}
	
}).controller("businessManagerController", function($window, $scope, $filter, businessService, commonService, gridService, commonPopupService,commonFactory) {
	
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	$scope.search = {};
	
	angular.element(document).ready(function () {
		commonService.init_search($scope,'search');
	});
	
	$scope.searchType = [
		                    {val : 'ID',text : '사용자ID'},
		                    {val : 'NAME',text : '사용자이름'}
		                   ];
	
	var columnDefs =  [
	                   { field: 'businessId', 					colKey: "c.ccsBusiness.id",			linkFunction : 'linkFunction'},
	                   { field: 'name', 						colKey: "ccsBusiness.name",			linkFunction : 'linkFunction'},
	                   { field: 'businessStateName', 			colKey: "c.ccsBusiness.business.statecd"}, //업체상태
	                   { field: 'businessType',					colKey: "c.ccsBusiness.business.businessType"},//공급품목
	                   { field: 'saleTypeName', 				colKey: "c.ccsBusiness.business.saletypecd"}, //매입유형
	                   { field: 'contractStartDt', 				colKey: "c.ccsBusiness.startDt",		cellFilter: "date:\'yyyy-MM-dd\'"},
	                   { field: 'contractEndDt', 				colKey: "c.ccsBusiness.endDt",			cellFilter: "date:\'yyyy-MM-dd\'"},
	                   { field: 'insId', 						userFilter :'insId,insName',			colKey: "c.grid.column.insId"},
	                   { field: 'insDt', 						displayName : "등록일시", 					colKey: "c.grid.column.insDt",			cellFilter: "date:\'yyyy-MM-dd\'"},
	                   { field: 'updId', 						userFilter :'updId,updName',			colKey: "c.grid.column.updId"},
	                   { field: 'updDt', 						displayName : "최종수정일시", 				colKey: "c.grid.column.updDt",			cellFilter: "date:\'yyyy-MM-dd\'"}
	       	]
	
	       	var gridParam = {
	       		scope : $scope,
	       		gridName : "grid_business",
	       		url : '/api/ccs/business',
	       		searchKey : "search",
	       		columnDefs : columnDefs,
	       		gridOptions : {
	       		},
	       		callbackFn : function() {
	       		//myGrid.loadGridData();
	       		} 
	       	};
			
			$scope.myGrid = new gridService.NgGrid(gridParam);	
			
			//=================== search	
			$scope.searchGrid = function(){
				
				$scope.myGrid.loadGridData();
			}
			
			//================= reset
			this.reset = function(){		
				commonService.reset_search($scope,'search');
				//angular.element(".day_group").find('button:first').addClass("on");
				angular.element('.day_group').find('button').eq(0).click();
			}
			
			//검색 popup
			this.searchPopup = function(type) {
				
				if(type === 'md'){
					commonPopupService.userPopup($scope,"callback_md",false,"USER_TYPE_CD.MD");
					
				}else if(type === 'business'){
					commonPopupService.businessPopup($scope,"callback_business",false);
				}
			}
			
			$scope.callback_md = function(data) {
				$scope.search.mdId = data[0].userId;
				$scope.search.mdName = data[0].name;
				$scope.$apply();
			}
			
			$scope.callback_business = function(data) {
				$scope.search.businessId = data[0].businessId;
				$scope.search.businessName = data[0].name;
				$scope.$apply();
			}
			
			this.eraser = function(val1, val2) {
				$scope.search[val1] = "";
				
				if(angular.isDefined(val2)) {
					$scope.search[val2] = "";
				}
			}
			
			$scope.popup = function(url) {
				popupwindow(url,"businessDetailPopup", 1200, 650);
			}
			
			$scope.linkFunction = function(field, row) {
				$scope.businessId = row.businessId;
				popupwindow('/ccs/business/popup/detail',"businessDetailPopup", 1300, 650);		
			}
			
			this.insertPopup = function(){
				$scope.businessId = undefined;
				popupwindow('/ccs/business/popup/detail',"businessInsertPopup", 1300, 650);
			}
	
}).controller("businessDetailController", function($window, $scope, $filter, businessService, userService, commonService, gridService, commonPopupService,commonFactory) {
	var pScope;
	$scope.loginType;
	if($window.opener != null){
		pScope = $window.opener.$scope;// 부모창의 scope
	}
	else if(parent.$scope != null){
		pScope = parent.$scope;	// 부모 iframe scope
		$scope.loginType = 'PO';
	}
	$scope.ccsBusiness = {};
	$scope.ccsBusiness.ccsCommissions =[];
	$scope.search={};
	$scope.user={};
	$scope.insertPageFlag = false;
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.

	// PO 경우
	var poBusinessId = global.session.businessId;
	$scope.poBusinessId = poBusinessId;
	
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	
	$scope.texType = [
		                    {val : 'Y',text : '과세'},
		                    {val : 'N',text : '면세'}
		                   ];
	
	var columnDefs =  [];
	columnDefs.push({ field: 'categoryId', 					cellClass : 'alignC',		displayName:"표준카테고리번호", colKey: "ccsCommission.businessId"});
	columnDefs.push({ field: 'depthFullName', 	cellClass : 'alignC',		displayName:"표준카테고리명",colKey: "pmsCategory.name"});
	columnDefs.push({ field: 'ccsUser.name', 						cellClass : 'alignC',		displayName:"담당 MD",colKey: "pmsCategory.name"});
	
	if(!global.session.businessId){
		columnDefs.push({ field: 'strCommissionRate', 			cellClass : 'alignC',		displayName:"수수료율%",colKey: "ccsCommission.commissionRate", enableCellEdit:true});
	}else{
		columnDefs.push({ field: 'strCommissionRate', 			cellClass : 'alignC',		displayName:"수수료율%",colKey: "ccsCommission.commissionRate"});
	}
       	
	var gridParam = {
			scope : $scope,
			gridName : "grid_commission",
			url : '/api/ccs/business/commission',
			searchKey : "search",
			columnDefs : columnDefs,
			gridOptions : { pagination : false 
			}
	};

	$scope.commissionGrid = new gridService.NgGrid(gridParam);
	
//	this.saleTypeCdClick = function() {
//		if ($scope.ccsBusiness.saleTypeCd=='SALE_TYPE_CD.CONSIGN' && $scope.ccsBusiness.purchaseYn=='Y') {
//			$scope.ccsBusiness.erpBusinessId='';
//		} 
//	}
//	
//	this.purchaseYnClick = function(){
//		if ($scope.ccsBusiness.saleTypeCd=='SALE_TYPE_CD.CONSIGN' && $scope.ccsBusiness.purchaseYn=='Y') {
//			$scope.ccsBusiness.erpBusinessId='';
//		}
//	}
	
//	this.initErpCode = function(){
//		if ($scope.ccsBusiness.saleTypeCd=='SALE_TYPE_CD.CONSIGN' && $scope.ccsBusiness.purchaseYn=='N') {
//			$scope.ccsBusiness.erpBusinessId='';
//		}else{
//			
//		}
//	}
	
	this.MDeraser = function(val1, val2) {
		$scope.search[val1] = "";
		
		if (angular.isDefined(val2)) {
			$scope.search[val2] = "";
		}
	}
	
	this.detail = function() {
		if (pScope != null && !common.isEmpty(pScope.businessId)) {
			$scope.search.businessId = pScope.businessId;
			$scope.ccsBusiness.businessId = pScope.businessId;
			businessService.getBusiness($scope.ccsBusiness, function(response) {
				$scope.ccsBusiness = response;
//				$scope.commissionGrid.loadGridData();
				$scope.commissionGrid.setData($scope.ccsBusiness.pmsCategorys, false);
				
				$scope.user ={
						userId : response.reqUserId,
						userTypeCd : "USER_TYPE_CD.BUSINESS",
						name : response.managerName,
						pwd : response.reqUserPwd,
						mdYn : "N",
						userStateCd : "USER_STATE_CD.USE",
						storId : response.storeId,
						businessId : response.businessId,
						email : response.managerEmail,
						phone1 : response.managerPhone1,
						phone2 : response.managerPhone2
					};
			});
		} else {
			$scope.insertPageFlag = true;
		}
	}
	
	//수정
	this.updateBusiness = function(){
		
		//폼 체크
		if(!commonService.checkForm($scope.form2)){
			return;
		}
		
		if((($scope.ccsBusiness.saleTypeCd=='SALE_TYPE_CD.PURCHASE') 
				|| ($scope.ccsBusiness.saleTypeCd=='SALE_TYPE_CD.CONSIGN' && $scope.ccsBusiness.purchaseYn=='Y')) && !$scope.ccsBusiness.erpBusinessId){
			alert("유효하지 않은 항목이 존재합니다.");
			return;
		}
		// 수수료를 등록 안했을 경우 
		for (var i = 0; i < $scope.grid_commission.data.length; i++) {
			var commissionRate = $scope.grid_commission.data[i].strCommissionRate;
			if ( commissionRate=="" ) {
				alert("업체 수수료를 등록 해주세요");
				return;
			}
		}
		// 매입유형이 위탁이고 위탁매입유형이 'N'일 경우
		/*if ($scope.grid_commission.data.length == 0 && ($scope.ccsBusiness.saleTypeCd=='SALE_TYPE_CD.CONSIGN' && $scope.ccsBusiness.purchaseYn=='N')) {
			alert("업체 수수료를 등록 해주세요");
			return;
		} else {
		}*/
		
		//수수료 그리드 Data setting
		$scope.ccsBusiness.ccsCommissions = $scope.commissionGrid.getData();
		
		if (pScope.businessId != undefined) {

			// 확인 메세지
			if (!confirm($scope.MESSAGES["common.label.confirm.save"])) {
				return;
			}

			businessService.saveBusiness($scope.ccsBusiness, function(response) {
				alert($scope.MESSAGES["common.label.alert.save"]);
				$scope.commissionGrid.loadGridData();
			});
		} else {
			
			// 확인 메세지
			if(!confirm("업체를 생성 하시겠습니까?")){
				return;
			}
			
			$scope.ccsBusiness.businessTypeCd = 'BUSINESS_TYPE_CD.SELLER';
			businessService.saveBusiness($scope.ccsBusiness, function(response) {
				alert('업체가 생성되었습니다.');
				$scope.commissionGrid.loadGridData();
				$window.close();
			});
		}
	}
	
	this.onClickTab = function(tab){
		if (angular.isUndefined(pScope.businessId) || pScope.businessId==null) {
			alert("업체 기본정보 저장후 등록 가능합니다.");
			return;
		}
		
		if(confirm("저장하지 않은 정보는 사라집니다. 이동 하시겠습니까?")){
			if(tab === '2'){
				$window.location.href="/ccs/business/popup/userList";
			}else if(tab === '3'){
				$window.location.href="/ccs/business/popup/deliveryList";
			}else if(tab === '4'){
				$window.location.href="/ccs/business/popup/holidayList";
			}else if(tab === '5'){
				$window.location.href="/ccs/business/popup/commissionList";
			}
		}
	}

	this.close = function() {
		if (!confirm($scope.MESSAGES["common.label.confirm.cancel"])) {
			return;
		}

		$window.close();
	}
	this.print = function() {
		$window.print();
	}
	this.inputNumber = function(name){
		
		var str = $scope.ccsBusiness[name];
		
		if(str != null){
			str = str.replace(/[^0-9]/g, '');
			$scope.ccsBusiness[name] = str;
		}
	}
	this.changeState = function(param) {
		var message = '';     // 상태 변경 완료 메세지
		var message2 = '';	  // 상태 변경 확인 메세지
		
		$scope.business = {};	// 상태 변경을 위한 업체 생성
		$scope.business.businessId = $scope.ccsBusiness.businessId;
		
		// 상태에 따른 메세지 설정
		if (param == 'reject') {
			$scope.business.businessStateCd='BUSINESS_STATE_CD.REJECT';
			message = '반려 상태로 변경되었습니다.';
			message2 = $scope.ccsBusiness.businessStateName + '를 반려 상태로 변경하시겠습니까?';
		} else if (param == 'approval') {
			$scope.business.businessStateCd='BUSINESS_STATE_CD.RUN';
			message = '운영 상태로 변경되었습니다.';
			message2 = $scope.ccsBusiness.businessStateName + '를 운영 상태로 변경하시겠습니까?';
		} else if (param == 'stop') {
			$scope.business.businessStateCd='BUSINESS_STATE_CD.STOP';
			message = '미운영 상태로 변경되었습니다.';
			message2 = $scope.ccsBusiness.businessStateName + '를 미운영 상태로 변경하시겠습니까?';
		}

		if(confirm(message2)){
			if(param == 'approval'){
				// 운영 상태로 변경 시 계정 생성
				if($scope.user.userId != null && $scope.user.userId != '' && $scope.user.pwd != null && $scope.user.pwd != ''){
					userService.insertUser($scope.user, function(response) {
						if (response.content == 'success') {
						} else {
							alert("요청 LoginID가 중복되어 계정 생성 실패하였습니다.");
						}
					});
				}
			}
			console.log($scope.business);
			businessService.changeState($scope.business, function(response) {
				// 상태 셋팅
				businessService.getBusiness($scope.business, function(response) {
					alert(message);
					$scope.ccsBusiness.businessStateCd = response.businessStateCd;
					$scope.ccsBusiness.businessStateName = response.businessStateName; 
				});
			});
		}
	}
	this.searchUser = function(type) {
		commonPopupService.userPopup($scope,"callback_md",false,"USER_TYPE_CD.MD");
	}
	
	$scope.callback_md = function(data) {
		$scope.ccsBusiness.userId = data[0].userId;
		$scope.ccsBusiness.userName = data[0].userName;
		$scope.$apply();
	}
	
	this.pmsCategoryPopup = function(){
		commonPopupService.categoryPopup($scope, "callback_category", false, '/pms/category/popup/search');
	}
	
	$scope.callback_category = function(data) {
		var flag = true;
		for (var i = 0; i < $scope.grid_commission.data.length; i++) {
			if ($scope.grid_commission.data[i].categoryId == data.categoryId) {
				flag = false;
				break;
			}
		}
		if (flag) {
			$scope.commissionGrid.addRow({
				businessId : pScope.businessId,
				commissionId : "",
				categoryId : data.categoryId,
				pmsCategory :{depthFullName : data.depthFullName},
				strCommissionRate:"",
				userId : data.userId,
				insDt : "",
				insId : "",
			});
		}
	}
	
	this.autoHypenPhone = function(type){		
		var str = $scope.ccsBusiness[type];
		var a = $filter('auto')($scope.ccsBusiness[type]);
        str = str.replace(/[^0-9]/g, '');
        var tmp = '';
        if( str.length < 4){
            return;
        }else if(str.length < 7){
            tmp += str.substr(0, 3);
            tmp += '-';
            tmp += str.substr(3);
        }else if(str.length < 11){
            tmp += str.substr(0, 3);
            tmp += '-';
            tmp += str.substr(3, 3);
            tmp += '-';
            tmp += str.substr(6);
        }else{              
            tmp += str.substr(0, 3);
            tmp += '-';
            tmp += str.substr(3, 4);
            tmp += '-';
            tmp += str.substr(7);
        }
        $scope.ccsBusiness[type] = tmp;
    }
	
	// 주소검색
	this.searchAddress = function() {
		commonPopupService.openAddressPopup($scope, 'calback_address');
	}
	 
	$scope.calback_address = function(data) {
		$scope.ccsBusiness.zipCd = data.postNo; 
		$scope.ccsBusiness.address1 = data.address1;
		$scope.ccsBusiness.address2 = data.address2;
		common.safeApply($scope);
	}
	
	this.eraser = function() {
		$scope.ccsBusiness.zipCd = ""; 
		$scope.ccsBusiness.address1 = "";
		$scope.ccsBusiness.address2 = "";
	}
	
}).controller("userListController", function($window, $scope, $filter, userService, commonService, gridService, commonPopupService,commonFactory){
	
	var pScope;
	$scope.loginType;
	if($window.opener != null){
		pScope = $window.opener.$scope;// 부모창의 scope
	}
	else if(parent.$scope != null){
		pScope = parent.$scope;	// 부모 iframe scope
		$scope.loginType = 'PO';
	}
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	//사용자정보 그리드
	var columnDefs =  [
	                   { field: 'userId', 					displayName:"Login ID",	colKey: "ccsUser.userId"},
	                   { field: 'name', 					displayName:"사용자",		colKey: "ccsUser.name",					linkFunction : 'userlinkFunction'},
	                   { field: 'email', 					displayName:"E-mail",	colKey: "ccsUser.email"},
	                   { field: 'useYn',					displayName:"사용여부",		cellFilter: "useYnFilter", enableCellEdit:true }
	       	]
	       	
   	var gridParam = {
   		scope : $scope,
   		gridName : "grid_user",
   		url : '/api/ccs/user',
   		searchKey : "search",
   		columnDefs : columnDefs,
   		gridOptions : {
   		},
   		callbackFn : function() {
   		}
   	};
	
	$scope.userGrid = new gridService.NgGrid(gridParam);
	
	$scope.popup = function(url, name) {
		popupwindow(url,name, 1100, 330);
	}
	
	$scope.userlinkFunction = function(field, row) {
		$scope.userId = row.userId;
		$scope.businessId = '';
		$scope.type = 'D';
		$scope.popup('/ccs/business/popup/userDetail',"userDetailPopup");
	}
	
	this.userInsertPopup = function(){	
		$scope.userId = '';
		$scope.businessId = pScope.businessId;
		$scope.type = 'I';
		//$scope.popup('/ccs/business/popup/userInsert',"userInsertPopup");
		popupwindow('/ccs/business/popup/userDetail',"userDetailPopup",1100, 350);
	}
	
	this.userGridInit = function(){
		$scope.search.businessId = pScope.businessId;
		$scope.search.userTypeCd = 'USER_TYPE_CD.BUSINESS';
		$scope.userGrid.loadGridData();
		/*userService.getUserList($scope.search, function(response) {
			$scope.userGrid.loadGridData();
		});*/
	}
	
	this.onClickTab = function(tab){
		
		if(confirm("저장하지 않은 정보는 사라집니다. 이동 하시겠습니까?")){
			if(tab === '1'){
				$window.location.href="/ccs/business/popup/detail";
			}else if(tab === '3'){
				$window.location.href="/ccs/business/popup/deliveryList";
			}else if(tab === '4'){
				$window.location.href="/ccs/business/popup/holidayList";
			}else if(tab === '5'){
				$window.location.href="/ccs/business/popup/commissionList";
			}
		}
	}
	
	
}).controller("deliveryListController", function($window, $scope, $filter, businessService, commonService, gridService, commonPopupService){
	
	var pScope;
	$scope.loginType;
	if($window.opener != null){
		pScope = $window.opener.$scope;// 부모창의 scope
	}
	else if(parent.$scope != null){
		pScope = parent.$scope;	// 부모 iframe scope
		$scope.loginType = 'PO';
	}
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	
	//배송정책 그리드
	var columnDefs =  [
		                   { field: 'deliveryPolicyNo', 					colKey: "c.ccsBusiness.deliveryPolicyNo",				linkFunction : 'dellinkFunction'},
		                   { field: 'name', 								colKey: "c.ccsBusiness.deliveryPolicyName",				linkFunction : 'dellinkFunction'},
		                   { field: 'deliveryServiceName', 					colKey: "c.ccsBusiness.deliveryServiceName"},
		                   { field: 'address', 								colKey: "c.ccsBusiness.deliveryPolicy.address"},
		                   { field: 'deliveryFee', 							colKey: "ccsDeliverypolicy.deliveryFee"},
		                   { field: 'minDeliveryFreeAmt', 					colKey: "c.ccsBusiness.minDeliveryFreeAmt"},
		                   { field: 'insId', 								userFilter :'insId,insName',			colKey: "c.grid.column.insId"},
		                   { field: 'insDt', 								displayName : "등록일시",	colKey: "c.grid.column.insDt",			cellFilter: "date:\'yyyy-MM-dd\'"},
		                   { field: 'updId', 								userFilter :'updId,updName',			colKey: "c.grid.column.updId"},
		                   { field: 'updDt', 								displayName : "최종수정일시",	colKey: "c.grid.column.updDt",			cellFilter: "date:\'yyyy-MM-dd\'"}
		                   
		       	]
	var gridParam = {
		scope : $scope,
		gridName : "grid_delivery",
		url : '/api/ccs/business/delivery',
		searchKey : "search",
		columnDefs : columnDefs,
		gridOptions : {
		},
		callbackFn : function() {
		}
	};
	
	$scope.deliveryGrid = new gridService.NgGrid(gridParam);
	
	this.deliveryGridInit = function(){
		$scope.search.businessId = pScope.businessId;
		$scope.deliveryGrid.loadGridData();
		/*businessService.getDeliveryList($scope.search, function(response) {
			$scope.deliveryGrid.loadGridData();
		});*/
	}
	
	$scope.popup = function(url) {
		popupwindow(url,"deliveryDetailPopup", 1100, 400);
	}
	
	$scope.dellinkFunction = function(field, row) {
		$scope.deliveryPolicyNo = row.deliveryPolicyNo;
		$scope.popup('/ccs/business/popup/deliveryDetail'); 
	}
	
	this.deInsertPopup = function(){
		$scope.businessId = pScope.businessId;
		$scope.deliveryPolicyNo = "";
		popupwindow('/ccs/business/popup/deliveryDetail',"deliveryInsertPopup", 1100, 450);
	}
	
	this.saveDelevery = function(){
		var gridApi = $scope.grid_delivery.gridApi;
		var dirtyRows;
		if (gridApi && gridApi.rowEdit) {
			dirtyRows = gridApi.rowEdit.getDirtyRows();
		}
		if(dirtyRows.length > 0){
			
			// 확인 메세지
			if (!confirm($scope.MESSAGES["common.label.confirm.save"])) {
				return;
			}

			businessService.updateDeliverypolicy($scope.search, function(response) {
				if (response.content === 'success') {
					alert($scope.MESSAGES["common.label.alert.save"]);
					$window.close();
				} else {
					alert('변경시 에러가 발생 하였습니다.');
				}
			});
		}else{
			alert('수정한 항목이 없습니다.');
		}
	}
	
	this.onClickTab = function(tab){
		
		if(confirm("저장하지 않은 정보는 사라집니다. 이동 하시겠습니까?")){
			if(tab === '1'){
				$window.location.href="/ccs/business/popup/detail";
			}else if(tab === '2'){
				$window.location.href="/ccs/business/popup/userList";
			}else if(tab === '4'){
				$window.location.href="/ccs/business/popup/holidayList";
			}else if(tab === '5'){
				$window.location.href="/ccs/business/popup/commissionList";
			}
		}
	}
	
}).controller("holidayListController", function($window, $scope, $filter, businessService, commonService, gridService, commonPopupService){
	
	var pScope;
	$scope.loginType;
	if($window.opener != null){
		pScope = $window.opener.$scope;// 부모창의 scope
	}
	else if(parent.$scope != null){
		pScope = parent.$scope;	// 부모 iframe scope
		$scope.loginType = 'PO';
	}
	
	var template = {
			dateClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
				var cellcss = 'alignC';
				if (!grid.options.isRowHidetable(row)) {
					cellcss = 'alignC hide-date';
				}
				return cellcss;
			}
		};
	
	//휴일 그리드
	var columnDefs =  [
	                   { field: 'holiday'	, colKey: "c.ccsBusinessholiday.holiday", validators:{required:true}, 
	                	   enableCellHiddenInputEdit: true,
	                	   cellClass : template.dateClass,
	       				   headerCellClass : 'edit_column',
	       				   type : 'date'},
	                   { field: 'insId'		, userFilter :'insId,insName',			colKey: "c.grid.column.insId", cellClass : 'alignC'},
	                   { field: 'insDt'		, colKey: "c.grid.column.insDt", 		cellClass : 'alignC'},
	                   { field: 'updId'		, userFilter :'updId,updName',			colKey: "c.grid.column.updId", cellClass : 'alignC'},
	                   { field: 'updDt'		, colKey: "c.grid.column.updDt", 		cellClass : 'alignC'}
	       	];
		       	
	var gridParam = {
		scope : $scope,
		gridName : "grid_holiday",
		url : '/api/ccs/business/holiday',
		searchKey : "search",
		columnDefs : columnDefs,
		showGroupPanel : true,
		gridOptions : {
			isRowHidetable : function(row) {
				var hideable = true;
				if (row.entity.insId != null && row.entity.insId != '') {
					hideable = false;
				}
				if (hideable) {
					row.cursor = 'pointer';
				}
				return hideable;
			}
		},
		callbackFn : function() {
		}
	};
	
	$scope.holidayGrid = new gridService.NgGrid(gridParam);
	
	this.holidayGridInit = function(){
		$scope.search.businessId = pScope.businessId;
		$scope.holidayGrid.loadGridData();
		/*businessService.getBusinessholidayList($scope.search, function(response) {
			$scope.holidayGrid.loadGridData();
		});*/
	}
	
	//휴일 일괄 등록
	this.holidayBulkAddRow = function(){
		commonPopupService.gridbulkuploadPopup($scope,"excelUpload_callback","holiday");
	}
	
	$scope.excelUpload_callback = function(response) {
		var data = response.resultList;
		var successFlag = true;
		
		for (var i=0; i < data.length; i++) {
			var insertFlag = true;
			for (var j=0; j < $scope.grid_holiday.data.length; j++) {
				if (data[i].ccsBusinessholiday.holiday == $scope.grid_holiday.data[j].holiday) {
					insertFlag = false;
					successFlag = false;
				}
			}
			if (insertFlag) {
				$scope.holidayGrid.addRow({
					businessId : pScope.businessId,
					holiday : data[i].ccsBusinessholiday.holiday,
					insDt : "",
					insId : "",
					updDt : "",
					updId : ""
				});
			}
		}
		/*if(successFlag){
			alert('그리드에 추가되었습니다.');
		}else{
			alert('이미 등록된 상품을 제외하고 추가 되었습니다.');
		}*/
		$scope.$apply();
	}
	
	
	//그리드에 추가
	this.holidayAddRow = function(){
		$scope.holidayGrid.addRow({
			businessId : pScope.businessId,
			holiday : "",
			insDt : "",
			insId : "",
			updDt : "",
			updId : ""
		});
	}
	
	/*this.saveHolidayGrid = function(){
		$scope.holidayGrid.saveGridData(null,function(){
			$scope.holydayGrid.loadGridData();
			$window.close();
		});			
	}*/
	
	// 휴일 등록
	this.saveHolidayGrid = function(){
		$scope.holidayGrid.saveGridData(null, function(data){
			$scope.holidayGrid.loadGridData();
		});
	}
	
	
	this.onClickTab = function(tab){
		
		if(confirm("저장하지 않은 정보는 사라집니다. 이동 하시겠습니까?")){
			if(tab === '1'){
				$window.location.href="/ccs/business/popup/detail";
			}else if(tab === '2'){
				$window.location.href="/ccs/business/popup/userList";
			}else if(tab === '3'){
				$window.location.href="/ccs/business/popup/deliveryList";
			}else if(tab === '5'){
				$window.location.href="/ccs/business/popup/commissionList";
			}
		}
	}
	
}).controller("commissionListController", function($window, $scope, $filter, businessService, commonService, gridService, commonPopupService){
	
	var pScope;
	$scope.loginType;
	if($window.opener != null){
		pScope = $window.opener.$scope;// 부모창의 scope
	}
	else if(parent.$scope != null){
		pScope = parent.$scope;	// 부모 iframe scope
		$scope.loginType = 'PO';
	}
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	cellTemplateStr = "<div class=\"ui-grid-cell-contents\"  title=\"\">{{row.entity.categoryId}}<button type='btn_type2' style=\"margin-left:80;\" class='btn_type2' ng-click=\"grid.appScope.categoryPopup(row.entity)\">"
				+"<b>검색</b></button></div>"; 
	var columnDefs =  [
		                   { field: 'businessId', 					colKey: "ccsCommission.businessId"},
		                   { field: 'commissionId', 				colKey: "ccsCommission.commissionId"},
		                   { field: 'categoryId', 					colKey: "ccsCommission.categoryId" , cellTemplate :cellTemplateStr},
		                   { field: 'commissionRate', 				colKey: "ccsCommission.commissionRate", enableCellEdit:true, vKey:"ccsCommission.commissionRate"},
		                   { field: 'insDt', 						colKey: "c.grid.column.insDt"},
		                   { field: 'insId', 						userFilter :'insId,insName',		colKey: "c.grid.column.insId"}
		       	]
		       	
	var gridParam = {
		scope : $scope,
		gridName : "grid_commission",
		url : '/api/ccs/business/commission',
		searchKey : "search",
		columnDefs : columnDefs,
		gridOptions : {
		},
		callbackFn : function() {
		}
	};
	
	$scope.commissionGrid = new gridService.NgGrid(gridParam);
	
	this.commissionGridInit = function(){
		$scope.search.businessId = pScope.businessId;
		$scope.commissionGrid.loadGridData();
	}
	
	//그리드에 추가
	this.commissionAddRow = function(){
		$scope.commissionGrid.addRow({
			businessId : pScope.businessId,
			commissionId : "",
			categoryId : "",
			categoryName : "",
			commissionRate:"",
			insDt : "",
			insId : ""
		});
	}
	
	$scope.categoryPopup = function(row){
		var index = $scope.grid_commission.data.indexOf(row);
		$scope.tableIndex = index;
		commonPopupService.categoryPopup($scope, "callback_pmsCategory", false, '/pms/category/popup/search');
	}
	
	$scope.callback_pmsCategory = function(data) {
		//console.log(data.categoryId);
		//$scope.grid_commission.data[$scope.tableIndex].categoryId = data.categoryId;
		$scope.commissionGrid.changeData($scope.tableIndex, "categoryId", data.categoryId);
	}
	
	this.onClickTab = function(tab){
		
		if(confirm("저장하지 않은 정보는 사라집니다. 이동 하시겠습니까?")){
			if(tab === '1'){
				$window.location.href="/ccs/business/popup/detail";
			}else if(tab === '2'){
				$window.location.href="/ccs/business/popup/userList";
			}else if(tab === '3'){
				$window.location.href="/ccs/business/popup/deliveryList";
			}else if(tab === '4'){
				$window.location.href="/ccs/business/popup/holidayList";
			}
		}
	}
}).controller("userDetailController", function($window, $scope, $filter, userService, commonService, gridService, commonPopupService,commonFactory){
	
	var pScope = $window.opener.$scope;// 부모창의 scope
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	$scope.ccsUser = {};
	$scope.ccsUser.ordPwd;
	$scope.ccsUser.pwdResetYn="N";	//	패스워드 리셋 여부
	$scope.ccsUser.idCheckYN ='N';	//	패스워드 중복 체크 여부
	$scope.type = pScope.type;
	
	
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	this.detail = function(){
		$scope.ccsUser.userId = pScope.userId;
		
		// 수정일 경우 사용자 상세 정보 조회
		if($scope.type == 'D'){
			userService.getUser($scope.ccsUser, function(response) {
				$scope.ccsUser = response;
				$scope.ccsUser.pwdModifyYn="N";
				$scope.ccsUser.userId = pScope.userId;
			});
		}
	}

	this.update = function(){
		
		//폼 체크
		if(!commonService.checkForm($scope.form2)){
			return;
		}
		

		// 사용자 등록
		if($scope.type == 'I'){
			var idCheckYn = $scope.ccsUser.idCheckYN;
			
			if(idCheckYn === 'Y'){
				var pwd1 = $scope.ccsUser.pwd1;
				var pwd2 = $scope.ccsUser.pwd2;
				
				if ($scope.ccsUser.userId != $scope.checkedId) {
					alert("사용자 ID를 중복체크 해주세요.");
					return;
				}
				
				if(pwd1 === pwd2){
					$scope.ccsUser.pwd = pwd1;
					$scope.ccsUser.userTypeCd = 'USER_TYPE_CD.BUSINESS';
					$scope.ccsUser.userStateCd='USER_STATE_CD.USE';
					$scope.ccsUser.businessId= pScope.businessId;
					$scope.ccsUser.mdYn='N';
					
					// 확인 메세지
					if(!confirm("사용자를 생성하시겠습니까?")){
						return;
					}
					
					userService.insertUser($scope.ccsUser, function(response) {
						if (response.content === 'success') {
							alert('사용자가 생성되었습니다.');
							if(pScope.userGrid != undefined){
								pScope.userGrid.loadGridData();
							}
							$window.close();
						} else {
							alert('사용자가 생성중 에러가 발생 하였습니다.');
						}
					});
				}else{
					alert('비밀번호를 다시 확인해주세요.');
				}
			}else{
				alert('사용자 ID를 중복체크 해주세요.');
			}
		// 사용자 수정
		}else if($scope.type == 'D'){
			var newPwd = $scope.ccsUser.newPwd1;
			var comfPwd = $scope.ccsUser.newPwd2;
			var orgPwd = $scope.ccsUser.pwd;
			
			//  관리자가 비밀번호 초기화 하였을경우
			if ($scope.ccsUser.pwdResetYn == 'Y') {
				$scope.ccsUser.pwdModifyYn = "Y";
				$scope.ccsUser.newPwd = $scope.ccsUser.pwd;
			}
			
			// 사용자가 새로운 비밀번호를 입력하였을 경우
			if (angular.isDefined(newPwd) && angular.isDefined(comfPwd)) {
				if (newPwd == comfPwd) {
					$scope.ccsUser.pwdModifyYn = "Y";
					$scope.ccsUser.newPwd = newPwd;
				} else {
					alert('비밀번호를 다시 확인해주세요.');
					return;
				}
			}
			
			// 확인 메세지
			if (!confirm($scope.MESSAGES["common.label.confirm.save"])) {
				return;
			}
			
			userService.updateUser($scope.ccsUser, function(response) {
				$window.opener = $scope;
				if (response.content === 'success') {
					alert($scope.MESSAGES["common.label.alert.save"]);
					if(pScope.userGrid != undefined){
						pScope.userGrid.loadGridData();
					}
					$window.close();
				} else {
					alert('변경시 에러가 발생 하였습니다.');
				}
			});
		}
	}
	
	this.close = function() {
		if (!confirm($scope.MESSAGES["common.label.confirm.cancel"])) {
			return;
		}

		$window.close();
	}
	
	this.eraser = function(name){
		$scope.ccsUser[name+'Id'] = "";
	}
	
	$scope.popup = function(url,name) {
		popupwindow(url,name, 1000, 480);
	}
	
	// 비밀번호 변경 팝업 호출
	this.changePwd = function(){
		var winName='비밀번호변경';
		var winURL = Rest.context.path +"/ccs/user/popup/pwdModify";
		popupwindow(winURL,winName,340,260);
		
	}
	
	this.pwdInit = function(){
		
		if (confirm("비밀번호를 초기화 하시겠습니까?")) {
			var param = {userId : $scope.ccsUser.userId, systemType : 'PO'};
			userService.findPwd(param, function(response) {
				if(response.content=='success'){
					alert("영업담당자 휴대폰 번호로 비밀번호가 전송되었습니다.");
				}else{
					alert("영업담당자 휴대폰 번호가 없습니다.");
				}
			});
		}
	}
	this.duplicateCheckId = function(userId){
		var id = $scope.ccsUser.userId
		
		if(id !== undefined && id !== ''){
			userService.getUserIdDuplicate(id, function(response) {
				resultCnt = Number(response.content);
				
				if (resultCnt >0 ) {
					alert('사용중인 ID 입니다. 다시 입력해주세요.');
					$scope.ccsUser['userId'] = "";
				} else {
					alert('사용가능한 ID 입니다.');
					$scope.ccsUser.idCheckYN ='Y';
					$scope.checkedId = angular.copy($scope.ccsUser.userId);
				}
			});

		}else{
			alert('사용자ID를 입력해주세요.');
		}
	}
	
}).controller("deliveryDetailController", function($window, $scope, $filter, businessService, commonService, gridService, commonPopupService,commonFactory) {
	var pScope = $window.opener.$scope;// 부모창의 scope
	//$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	$scope.ccsDeliverypolicy={};
	$scope.ccsDeliverypolicy.deliveryPolicyNo = pScope.deliveryPolicyNo;
	$scope.ccsDeliverypolicy.businessId = pScope.businessId;
	
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	this.detail = function(){
		if (pScope.deliveryPolicyNo != '') {
			businessService.getDeliverypolicy($scope.ccsDeliverypolicy, function(response) {
				$scope.ccsDeliverypolicy = response;
			});
		}
	}
	
	this.update = function(){
		//폼 체크
		if(!commonService.checkForm($scope.form2)){
			return;
		}
		
		$scope.ccsDeliverypolicy.deliveryFeeTypeCd = 'DELIVERY_FEE_TYPE_CD.SINGLE';
		if (pScope.deliveryPolicyNo != '') {
			
			// 확인 메세지
			if (!confirm($scope.MESSAGES["common.label.confirm.save"])) {
				return;
			}

			businessService.updateDeliverypolicy($scope.ccsDeliverypolicy, function(response) {
				if (response.content === 'success') {
					alert($scope.MESSAGES["common.label.alert.save"]);
					pScope.deliveryGrid.loadGridData();
					$window.location.reload();
				} else {
					alert('변경시 에러가 발생 하였습니다.');
				}
			});
		} else {
			
			// 확인 메세지
			if(!confirm("업체 배송을 생성하시겠습니까?")){
				return;
			}
			
			businessService.insertDelivery($scope.ccsDeliverypolicy, function(response) {
				if (response.content === 'success') {
					alert('업체 배송이 생성되었습니다.');
					pScope.deliveryGrid.loadGridData();
					$window.close();
				} else {
					alert('변경시 에러가 발생 하였습니다.');
				}
			});
		}
	}
	
	this.close = function() {
		if (!confirm($scope.MESSAGES["common.label.confirm.cancel"])) {
			return;
		}

		$window.close();
	}
	
	// 주소검색
	this.searchAddress = function() {
		commonPopupService.openAddressPopup($scope, 'calback_address');
	}
	 
	$scope.calback_address = function(data) {
		$scope.ccsDeliverypolicy.zipCd = data.postNo; 
		$scope.ccsDeliverypolicy.address1 = data.address1;
		$scope.ccsDeliverypolicy.address2 = data.address2;
		common.safeApply($scope);
	}
	
	this.eraser = function() {
		$scope.ccsDeliverypolicy.zipCd = ""; 
		$scope.ccsDeliverypolicy.address1 = "";
		$scope.ccsDeliverypolicy.address2 = "";
	}

}).controller("bulkUploadCtrl", function($window, $scope, productService, commonService) {
	pScope = $window.opener.$scope;// 부모창의 scope
	$scope.bulkType = pScope.bulkType;
	var upPath = '';
	var fiileName = '';
	$scope.bulkTypeTxt = '휴일 목록 변경';
	upPath = 'holyday';
	fiileName += '휴일목록변경_템플릿.xlsx';
	
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	$scope.files = [];
	// listen for the file selected event
	$scope.$on("fileSelected", function(event, args) {
		$scope.$apply(function() {
			// add the file object to the scope's files collection
//			var ele = event.target.id;
//			if (ele == 'img') {
//				if (!/(\.zip|\.png|\.jpg|\.jpeg)$/i.test((args.file.name).toLowerCase())) {
//					alert("파일형식은 .zip.png.jpg.jpeg만 지원됩니다.");
//					$scope.filePath = "";
//				} else {
//					$scope.files.push(args.file);
//				}
//			} else {
//				if (!/(\.xlsx|\.xls)$/i.test(args.file.name)) {
//					alert("엑셀 파일이 아닙니다.");
//					$scope.filePath = "";
//				} else {
//					$scope.files.push(args.file);
//				}
//			}
			$scope.files.push(args.file);
		});
	});

	// 팝업 닫기
	this.close = function() {
		if (!confirm($scope.MESSAGES["common.label.confirm.cancel"])) {
			return;
		}

		$window.close();
	}

	// 파일 경로 지우기
	this.eraser = function() {
		$scope.filePath = "";
	}

	// 파일 일괄 업로드
	uploadfile = function(file, url){
		commonService.uploadFileToUrl(file, null, url, function(response) {
			var result = JSON.parse(response);
			$scope.totalCnt = result.totalCnt;
			$scope.successCnt = result.successCnt;
			$scope.failCnt = result.failCnt;
			$scope.excelPath = result.excelPath;
			
			if ($scope.$$phase != '$apply' && $scope.$$phase != '$digest') {
				$scope.$apply();
			}
		});
	}
	this.checkExcel = function() {
		var file = $scope.files[0];
		if (file != null) {
			if (!/(\.xlsx|\.xls)$/i.test(file.name)) {
				$scope.filePath = "";
				alert("엑셀 파일이 아닙니다.");
				return false;
			}
			var url = Rest.context.path + '/api/pms/product/bulk/' + upPath;
			uploadfile(file, url);
		}
	}
	this.checkImg = function() {
		var file = $scope.files[0];
		if (file != null) {
			if (!/(\.zip|\.png|\.jpg|\.jpeg)$/i.test((file.name).toLowerCase())) {
				$scope.filePath = "";
				alert("파일형식은 .zip.png.jpg.jpeg만 지원됩니다.");
				return false;
			}
			var url = Rest.context.path + '/api/pms/product/bulk/img';
			uploadfile(file, url);
		}
	}

	// 엑셀템플릿 다운로드 
	this.downTemplate = function() {
		
		commonService.getConfig("excel.download.path.template", function(response) {
			var fullPath = response.content + '/' + fiileName;
			$window.location = fullPath;
//			$window.location = Rest.context.path + "/api/ccs/common/downTemplate?templateName=" + fullPath;
		});
//		$.ajax({
//			type : 'get',
//			url : 'C:/ZTS/project/gcp2.0_admin/src/main/resources/config/system/properties.xml',
//			dataType : 'xml',
//			success : function(data){
//				$(data).find('entry').each(function(){
//					if($(this).attr('key') == 'excel.upload.path.error'){
//						console.log('>>>>>' + $(this).text());
//						alert('excel.upload.path.error : ' + $(this).text());
//					}
//				});
//			},
//			error : function(xhr, status, error){
//				alert('error : ' + error);
//			}
//		});
	}

	// 오류데이터 다운로드
	this.downFailDataExcel = function() {
		if ($scope.excelPath) {
			$window.location = Rest.context.path + "/api/ccs/common/downTemplate?templateName=" + $scope.excelPath;
		}
	}
});


