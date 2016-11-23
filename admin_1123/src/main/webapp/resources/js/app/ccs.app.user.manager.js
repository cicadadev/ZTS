// USER 화면 모듈
var userApp = angular.module("userApp", ['commonServiceModule', 'ccsServiceModule', 'gridServiceModule', 'commonPopupServiceModule' , 'ui.date', 'ngCkeditor']);

Constants.message_keys = ["common.label.alert.save", "common.label.confirm.save", "common.label.alert.cancel", "common.label.confirm.cancel", "common.label.alert.delete", "common.label.confirm.delete"];

userApp.controller("userListController", function($window, $scope, $filter, userService, commonService, gridService, commonPopupService) {

	//data model
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	$scope.search = {};
	
	angular.element(document).ready(function () {		
		$scope.search.startDate = $filter('date')(new Date(), Constants.date_format_2);
		$scope.search.endDate = $filter('date')(new Date(), Constants.date_format_2);
		
		commonService.init_search($scope,'search');
	});
	
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	
	$scope.searchType = [   
		                    {val : 'NAME',text : '사용자명'},
		                    {val : 'ID',text : '사용자ID'}
		                   ];
	
	var columnDefs =  [
	                   { field: 'userId'		, colKey: "c.ccs.user.userId"	, linkFunction : 'linkFunction'		},
	                   { field: 'name'			, colKey: "c.ccs.user.name"		, linkFunction : 'linkFunction'		},
	                   { field: 'mdYn'			, colKey: "c.ccs.mdYn"			, cellFilter : "mdYnFilter"	},
	                   { field: 'userStateName'	, colKey: "c.ccs.user.popup.useYn"},
	                   { field: 'ccsRole.name'	, colKey: "c.ccs.user.roleName"										},
	                   { field: 'email'			, colKey: "c.ccs.user.email"										},
	                   { field: 'insId'			, userFilter :'insId,insName',	colKey: "c.grid.column.insId"										},
	                   { field: 'insDt'			, displayName : "등록일시", 		colKey: "c.grid.column.insDt"	, cellFilter: "date:\'yyyy-MM-dd\'"	},
	                   { field: 'updId'			, userFilter :'updId,updName',	colKey: "c.grid.column.updId"										},
	                   { field: 'updDt'			, displayName : "최종수정일시", 		colKey: "c.grid.column.updDt"	, cellFilter: "date:\'yyyy-MM-dd\'"	}
	                   
	                  ]
	       	
   	var gridParam = {
   		scope : $scope,
   		gridName : "grid_user",
   		url : '/api/ccs/user',
   		searchKey : "search",
   		columnDefs : columnDefs,
   		gridOptions : {
   			checkBoxEnable : false
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
	}
	
	$scope.userId = '';
	
	$scope.linkFunction = function(field, row) {
		$scope.userId = row.userId;
		popupwindow('/ccs/user/popup/detail',"userDetailPopup", 1100, 510);
	}
	
	this.userInsertPopup = function(){
		$scope.userId = '';
		popupwindow('/ccs/user/popup/detail',"userInsertPopup", 1100, 630);
	}
	
	this.eraser = function(val1, val2) {
		$scope.search[val1] = "";
		
		if(angular.isDefined(val2)) {
			$scope.search[val2] = "";
		}
	}
	
	// 각 검색 팝업
	this.openPopup = function(kindOf) {
		if(kindOf == 'business') {
			commonPopupService.businessPopup($scope,"callback_business",false);
			$scope.callback_business = function(data){
				$scope.search.businessId = data[0].businessId;
				$scope.search.businessName = data[0].name;
				$scope.$apply();		
			}
		}
	}
}).filter('mdYnFilter', function() { 
	
	var comboHash = {
			'Y': '예',
			'N': '아니요'
	};
	
	return function(input) { return !input ? '' :  comboHash[input]; };			
}).controller("userDetailController", function($window, $scope, $filter, userService, commonService, gridService, commonPopupService){
	
	pScope = $window.opener.$scope;// 부모창의 scope
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	$scope.ccsUser = {};
	$scope.mainDetail;

	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	
	$scope.ccsUser.ccsRole = {};
	$scope.param={};
	
	$scope.ccsUser.idCheckYN ='N';
	$scope.type = '';
	this.detail = function(){
		$scope.ccsUser.userId = pScope.userId;
		$scope.mainDetail = pScope.mainDetail;
		if(pScope.userId != ''){
			$scope.type = 'D';
			userService.getUser($scope.ccsUser, function(response) {
				$scope.ccsUser = response;
				$scope.ccsUser.pwdModifyYn="N";
			});
		}
		else{
			$scope.type = 'I';
		}
	}
	
	// 비밀번호 변경 팝업 호출
	this.changePwd = function(){
		var winName='비밀번호변경';
		var winURL = Rest.context.path +"/ccs/user/popup/pwdModify";
		popupwindow(winURL,winName,340,260);
		
	}
	this.update = function(){
		if(!commonService.checkForm($scope.form2)){
			return;
		}
		
		if($scope.type == 'D'){
			var newPwd = $scope.ccsUser.newPwd1;
			var comfPwd = $scope.ccsUser.newPwd2;
			var orgPwd = $scope.ccsUser.pwd;
			
			if (!confirm($scope.MESSAGES["common.label.confirm.save"])) {
				return;
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
			
			//TODO 코드 삭제 예정
			$scope.ccsUser.userTypeCd = "USER_TYPE_CD.MD";
			
			userService.updateUser($scope.ccsUser, function(response) {
				if (response.content === 'success') {
//				pScope.myGrid.loadGridData();
					alert($scope.MESSAGES["common.label.alert.save"]);
					$window.close();
				} else {
					alert('변경시 에러가 발생 하였습니다.');
				}
			});
		}else{
			
			var idCheckYn = $scope.ccsUser.idCheckYN;
			
			if(idCheckYn === 'Y'){
				
				if ($scope.ccsUser.userId != $scope.checkedId) {
					alert("사용자 ID를 중복체크 해주세요.");
					return;
				}
				
				var pwd1 = $scope.ccsUser.pwd1;
				var pwd2 = $scope.ccsUser.pwd2;
				
				if( pwd1 != pwd2) {
					alert("패스워드가 일치하지 않습니다.");
					return false;
				}
				
				
//			if (!isChkUserPwd(pwd1, pwd2)) {
//				return;
//			}
//			if (!chkSeqUserId(pwd1, $scope.ccsUser.userId)) {
//				return;
//			}
//			if (!chkSeqQwer(pwd1)) {
//				return;
//			}
//			if (!chkNumber($scope.ccsUser.phone2, pwd1)) {
//				return;
//			}
				if (!confirm("사용자를 생성하시겠습니까?")) {
					return;
				}
				
				$scope.ccsUser.pwd = pwd1;
				
				//TODO 코드 삭제 예정
				$scope.ccsUser.userTypeCd = "USER_TYPE_CD.MD";
				
				userService.insertUser($scope.ccsUser, function(response) {
					if (response.content === 'success') {
						pScope.myGrid.loadGridData();
						alert('사용자가 생성되었습니다..');
						$window.close();
					} else {
						alert('변경시 에러가 발생 하였습니다.');
					}
				});
				
			} else {
				alert('사용자 ID를 중복체크 해주세요.');
			}
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
		if(name == 'role'){
			$scope.ccsUser.ccsRole.name = "";
		}else if(name == 'business'){
			$scope.ccsUser.ccsBusiness.name = "";
		}
	}
	
	this.pwdInit = function(){
		
/*		if(common.isEmpty($scope.ccsUser.phone2)){
			alert("입력된 휴대폰번호가 없습니다.");
			return;
		}*/
		if (confirm("비밀번호를 초기화 하시겠습니까?")) {
			
			var param = {userId : $scope.ccsUser.userId, systemType : 'BO'}
			userService.findPwd(param, function(response) {
				if(response.content=='success'){
					alert("휴대폰 번호로 비밀번호가 전송되었습니다.");
				}else{
					alert("입력된 휴대폰번호가 없습니다.");
				}
				
			});
		}
	}
	
	$scope.popup = function(url,name) {
		popupwindow(url,name, 1000, 480);
	}
	
	
	this.searchPopup = function(type) {
		
		if(type === 'role'){
			$scope.param = {callback : "callback_role", multi:false};
			$scope.popup(Rest.context.path +"/ccs/roles/popup/list",'roleListPopup', 700, 700);
			
		}else if(type === 'business'){
			commonPopupService.businessPopup($scope,"callback_business",false);
			
		}else if(type === 'offshop'){
			$scope.param = {callback : "callback_offshop", multi:false};
			$scope.popup(Rest.context.path +"/ccs/offshop/popup/list",'offshopPopup');
		}
	}
	
	$scope.callback_business = function(data) {
		$scope.ccsUser.ccsBusiness ={};
		$scope.ccsUser.businessId = data[0].businessId;
		$scope.ccsUser.ccsBusiness.name = data[0].name;
		$scope.$apply();
	}
	
	$scope.callback_role = function(data) {
		$scope.ccsUser.roleId = data[0].roleId;
		$scope.ccsUser.ccsRole.name = data[0].name;
		$scope.$apply();
	}
	
	$scope.callback_offshop = function(data){
		$scope.ccsUser.ccsOffshop ={};
		$scope.ccsUser.offshopId = data[0].offshopId;
		$scope.ccsUser.ccsOffshop.name = data[0].name;
		$scope.$apply();
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
	
}).controller("modifyPwdController", function($window, $scope, $filter, roleService, commonService, gridService, userService){	
	pScope = $window.opener.$scope;// 부모창의 scope
	
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});
	
	// 닫기
	this.close = function() {
		if (!confirm($scope.MESSAGES["common.label.confirm.cancel"])) {
			return;
		}

		$window.close();
	}
	
	// 비밀번호 변경
	this.modifyPwd = function() {
		if(!commonService.checkForm($scope.form)){
			return;
		}
		
		$scope.ccsUser.userId = pScope.ccsUser.userId;
		
		if( $scope.ccsUser.newPwd != $scope.ccsUser.newPwd2) {
			alert("패스워드가 일치하지 않습니다.");
			return false;
		}
//		if (!isChkUserPwd($scope.ccsUser.newPwd, $scope.ccsUser.newPwd2)) {
//			return;
//		}
//		if (!chkSeqUserId($scope.ccsUser.newPwd, $scope.ccsUser.userId)) {
//			return;
//		}
//		if (!chkSeqQwer($scope.ccsUser.newPwd)) {
//			return;
//		}
//		if (!chkNumber(pScope.ccsUser.phone2, $scope.ccsUser.newPwd)) {
//			return;
//		}
		
		
		userService.checkUserInfo($scope.ccsUser, function(response) {
			if (response.content == 'true') {
				// 회원비밀 번호 변경
				$scope.ccsUser.pwdModifyYn="Y";
				userService.updateUser($scope.ccsUser, function(response) {
					if (response.content === 'success') {
						alert('성공적으로 변경 되었습니다.');
						$window.close();
					} else {
						alert('변경시 에러가 발생 하였습니다.');
					}
				});
			} else {
				alert("현재 비밀번호를 잘못입력하였습니다.");
			}
		});
	}
	
}).controller("roleListPopupController", function($window, $scope, $filter, roleService, commonService,gridService){
	pScope = $window.opener.$scope;// 부모창의 scope
	$scope.search = {};
	angular.element(document).ready(function () {
		userGrid.loadGridData();
	});						
	
	$scope.searchType = [
		                    {val : 'ID',text : '상점ID'},
		                    {val : 'NAME',text : '상점명'}
		                   ];
	
	var columnDefs =  [
	                   { field: 'roleId', 			colKey: "c.ccs.user.popup.roleId"},
	                   { field: 'name', 			colKey: "c.ccs.user.popup.roleName"},	                 
	                   { field: 'useYn', 			colKey: "c.ccs.user.popup.useYn", cellFilter: "useYnFilter"},
	       	]	
	
	var gridParam = {
			scope : $scope, 
			gridName : "grid_user",
			url :  "/api/ccs/user/role", 
			searchKey : "search", 
			columnDefs : columnDefs,			
			gridOptions : {checkMultiSelect : $window.opener.$scope.param.multi}
	};
	
	
	var userGrid = new gridService.NgGrid(gridParam);
	//=================== search	
	
	this.searchGrid = function(){		
		userGrid.loadGridData();		
	}
	
	//================= reset
	this.reset = function(){		
		commonService.reset_search($scope,'search');
	}
	
	this.selectUser = function(){
		$window.opener.$scope[$window.opener.$scope.param.callback](userGrid.getSelectedRows());
		window.close();			
	}
	
	this.close = function(){
		window.close();
	}
});