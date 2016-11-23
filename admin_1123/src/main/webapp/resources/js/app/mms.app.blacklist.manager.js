
var blacklistManagerApp = angular.module("blacklistManagerApp", ['commonServiceModule', 'mmsServiceModule', 'gridServiceModule', 'commonPopupServiceModule' , 'ui.date', 'ngCkeditor']);

blacklistManagerApp.controller("mms_blacklistManagerApp_controller", function($window, $scope, $filter, commonService, gridService, commonPopupService, blacklistService) {
	
$scope.memberSearch = {};
$window.$scope = $scope;
	var	columnDefs = [
	   	              	{ field: 'blacklistNo'										, width: '100'	, colKey: "c.mmsBlacklist.blacklistNo"		, enableCellEdit:false	, linkFunction:'blacklistDetail'},
	   	              	{ field: 'memberNo'											, width: '100'	, colKey: "c.mmsMember.memberNo"			, enableCellEdit:false	, linkFunction:'memberDetail'   },
	   	              	{ field: 'mmsMemberZts.mmsMember.mmsCustomer.customername'	, width: '100'	, colKey: "c.mmsCustomer.customer.name"		, enableCellEdit:false	, linkFunction:'memberDetail'   },
	   	              	{ field: 'mmsMemberZts.mmsMember.mmsCustomer.ssnbirthday'	, width: '100'	, colKey: "c.mmsCustomer.ssnbirthday"		, enableCellEdit:false	 								},
	   	              	{ field: 'mmsMemberZts.mmsMember.mmsCustomer.sex'			, width: '100'	, colKey: "c.mmsCustomer.sex"				, enableCellEdit:false	 								},
	   	              	{ field: 'mmsMemberZts.mmsMember.email'						, width: '100'	, colKey: "c.mmsMember.email"				, enableCellEdit:false	 								},
	   	              	{ field: 'mmsMemberZts.memGradeCd'							, width: '100'	, colKey: "c.mmsMemberZts.memGradeCd"		, enableCellEdit:false	
 	              																	, dropdownCodeEditor : "MEM_GRADE_CD", cellFilter :'memGradeFilter'	, validators:{required:true}		 			},
 	              		{ field: 'mmsMemberZts.mmsMember.mmsCustomer.staffYn'		, width: '100'	, colKey: "c.mmsCustomer.classId"			, enableCellEdit:false			 						},
 	              		{ field: 'mmsMemberZts.membershipYn'						, width: '100'	, colKey: "c.mmsMemberZts.membershipYn"		, enableCellEdit:false			 						},
 	              		{ field: 'mmsMemberZts.childrenYn'							, width: '100'	, colKey: "c.mmsMemberZts.childrenCardYn"	, enableCellEdit:false			 						},
 	              		{ field: 'mmsMemberZts.b2eYn'								, width: '100'	, colKey: "c.mmsMemberZts.b2eYn"			, enableCellEdit:false			 						},
 	              		{ field: 'mmsMemberZts.mmsMember.mmsCustomer.premiumYn'		, width: '100'	, colKey: "c.mmsCustomer.premiumYn"			, enableCellEdit:false			 						},
 	              		{ field: 'mmsMemberZts.mmsMember.status'					, width: '100'	, colKey: "c.mmsMember.status"				, enableCellEdit:false
 	              																	, dropdownCodeEditor : "STATUS", cellFilter :'statusFilter'	, validators:{required:true}							},
 	              		{ field: 'blacklistTypeCd'									, width: '100'	, colKey: "c.mmsBlacklist.blacklistType"	, enableCellEdit:false
 	              																	, dropdownCodeEditor : "BLACKLIST_TYPE_CD", cellFilter :'blacklistTypeFilter'	, validators:{required:true}			},
 	              		{ field: 'blacklistStateCd'									, width: '100'	, colKey: "c.mmsBlacklist.blacklistState"	, enableCellEdit:false
 	              																	, dropdownCodeEditor : "BLACKLIST_STATE_CD", cellFilter :'blacklistStateFilter'	, validators:{required:true}			},
  						{ field: 'startDt'											, width: '100'	, colKey: "mmsBlacklist.startDt"			, enableCellEdit:false									},
  						{ field: 'endDt'											, width: '100'	, colKey: "mmsBlacklist.endDt"				, enableCellEdit:false									},				
  						{ field: 'mmsMemberZts.mmsMember.cellno'					, width: '100'	, colKey: "c.mmsCustomer.cellno"			, enableCellEdit:false			 						},
  						{ field: 'insDt'											, width: '100'	, colKey: "c.grid.column.insDt"				, enableCellEdit:false									},
  						{ field: 'updDt'											, width: '100'	, colKey: "c.grid.column.updDt"				, enableCellEdit:false									},
  						{ field: 'updId'											, width: '100'	, colKey: "c.grid.column.updId"				, enableCellEdit:false	, userFilter :'updId,updName'	}
  					];
	
	var gridParam = {
			scope : $scope, 					//mandatory
			gridName : "grid_blacklist",		//mandatory
			url :  '/api/mms/blacklist',  		//mandatory
			searchKey : "blacklistSearch",      //mandatory
			columnDefs : columnDefs,    		//mandatory
			gridOptions : {             		//optional
			},
			callbackFn : function(){//optional
				
			}
	};
	
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	angular.element(document).ready(function () {
		commonService.init_search($scope,'blacklistSearch');
	});
	
	// 검색조건 초기화
	this.reset = function() {
		commonService.reset_search($scope, 'blacklistSearch');
		angular.element(".day_group").find('button:first').addClass("on");
		$scope.blacklistSearch.startDt ="";
		$scope.blacklistSearch.endDt = "";
	}
	
	$scope.infoType = [
	                    {val : 'USERID',text : '회원ID'},
	                    {val : 'NAME',text : '회원명'}
		              ];
	
	$scope.phoneType = [
						 {val : 'TEL',text : '전화번호'},
						 {val : 'CELL',text : '휴대폰번호'}
	                   ];
	$scope.memberType = [
		                    {val : 'TEL',text : '전화번호'},
		                    {val : 'CELL',text : '휴대폰번호'}
	                    ];
	
	// 블랙리스트 등록 팝업
	$scope.insBlacklistPop = function() {
		var winName='블랙리스트등록';
		var winURL = Rest.context.path +"/mms/blacklist/popup/insert";
		popupwindow(winURL,winName,900,460);
	}
	
	// 회원상세 팝업
	$scope.memberDetail = function(field, row) {
		$scope.memberNo = row.memberNo;
		var winName='회원상세';
		var winURL = Rest.context.path +"/mms/member/popup/detail";
		popupwindow(winURL,winName,1200,800);
	}
	
	// 블랙리스트 상세 팝업
	$scope.blacklistDetail = function(field, row) {
		console.log(row);
		$scope.blacklistNo = row.blacklistNo;
		$scope.blacklistUserId = row.mmsMemberZts.mmsMember.userid;
		$scope.blacklistName = row.mmsMemberZts.mmsMember.mmsCustomer.customername;
		$scope.blacklistStatus = row.mmsMemberZts.mmsMember.status;
		var winName='블랙리스트상세';
		var winURL = Rest.context.path +"/mms/blacklist/popup/detail";
		popupwindow(winURL,winName,900,450);
	}
	
	// 블랙리스트 해제
	$scope.cancelBlacklist = function() {
		var checkList = [];
		var checkList = $scope.myGrid.getSelectedRows();
		for (var i = 0; i < checkList.length; i++) {
			checkList[i].blacklistStateCd = "BLACKLIST_STATE_CD.CANCEL";
		}
		console.log(checkList);
		
		blacklistService.updateBlacklist(checkList, function() {
			alert("수정되었습니다.");
			$scope.myGrid.loadGridData();
//			$scope.myGrid.cleanDirtyRows();
		});
	}

}).filter('memGradeFilter', function() {// MEM_GRADE_CD 
	
	var comboHash = {
		    'MEM_GRADE_CD.VIP': 'VIP',
		    'MEM_GRADE_CD.GOLD': '골드',
		    'MEM_GRADE_CD.SILVER': '실버',
		    'MEM_GRADE_CD.FAMILY': '패밀리',
		    'MEM_GRADE_CD.WELCOME': '웰컴'
		};
	
	return function(input) { return !input ? '' :  comboHash[input]; };	
}).filter('statusFilter', function() {// MEM_GRADE_CD 
	
	var comboHash = {
			'1': '일반',
			'3': '준회원',
			'9': '휴면',
			'0': '탈퇴'
	};
	
	return function(input) { return !input ? '' :  comboHash[input]; };	
}).filter('blacklistTypeFilter', function() {// BLACKLIST_TYPE_CD 
	
	var comboHash = {
			'BLACKLIST_TYPE_CD.CS' : 'CS',
			'BLACKLIST_TYPE_CD.DELIVERY' : '정기배송',
			'BLACKLIST_TYPE_CD.EVENT' : '이벤트'
	};
	
	return function(input) { return !input ? '' :  comboHash[input]; };	
}).filter('blacklistStateFilter', function() {// BLACKLIST_TYPE_CD 
	
	var comboHash = {
			'BLACKLIST_STATE_CD.REG' : '등록',
			'BLACKLIST_STATE_CD.CANCEL' : '해제'
	};
	
	return function(input) { return !input ? '' :  comboHash[input]; };	
	
}).controller("mms_blacklistInsertPopApp_controller", function($window, $scope, $filter, commonService, gridService, commonPopupService, blacklistService) {
	
	$scope.searchMember = function() {
		commonPopupService.memberPopup($scope,"callback_member",false);
	}
	
	$scope.callback_member = function(data){
		console.log(data);
		$scope.mmsBlacklist.userid = data[0].mmsMember.userid;
		$scope.mmsBlacklist.memberNo = data[0].mmsMember.memberNo;
		$scope.mmsBlacklist.name = data[0].mmsMember.mmsCustomer.customername;
		
		if ("1" == data[0].mmsMember.status) {
			$scope.mmsBlacklist.status = "일반";	
		} else if ("3" == data[0].mmsMember.status) {
			$scope.mmsBlacklist.status = "준회원";
		} else if ("9" == data[0].mmsMember.status) {
			$scope.mmsBlacklist.status = "휴면";
		} else if ("0" == data[0].mmsMember.status) {
			$scope.mmsBlacklist.status = "탈퇴";
		}
		
		$scope.$apply();
	}
	
	// 회원번호, 회원명 삭제
	this.delText = function() {
		$scope.mmsBlacklist.userid = ""; 
		$scope.mmsBlacklist.name = ""; 
	}
	
	// 블랙리스트 저장
	this.saveBlacklist = function() {
		//폼 체크
		if(!$scope.mmsBlacklist.startDt || !$scope.mmsBlacklist.endDt){
			alert("기간을 입력해 주세요.");
			return;
		}
		
		if (!commonService.checkForm($scope.blacklistInsertForm)) {
			return;
		}
		
		blacklistService.saveBlacklist($scope.mmsBlacklist, function() {
			alert("저장되었습니다.");
			$window.close();
		});
	}
	
	// 블랙리스트 팝업 닫기
	this.close = function() {
		$window.close();
	}
	
}).controller("mms_blacklistDetailPopApp_controller", function($window, $scope, $filter, commonService, gridService, commonPopupService, blacklistService) {	
	var pScope = $window.opener.$scope;// 부모창의 scope
	$scope.mmsBlacklist = {};
	var statusName = "";
	angular.element(document).ready(function () {
		// 블랙리스트 조회
		blacklistService.getBlacklistDetail(pScope.blacklistNo, function(data) {
			$scope.mmsBlacklist = data;
			$scope.mmsBlacklist.name = pScope.blacklistName;
			$scope.mmsBlacklist.userid = pScope.blacklistUserId;
			if ("1" == pScope.blacklistStatus) {
				$scope.mmsBlacklist.status = "일반";	
			} else if ("3" == pScope.blacklistStatus) {
				$scope.mmsBlacklist.status = "준회원";
			} else if ("9" == pScope.blacklistStatus) {
				$scope.mmsBlacklist.status = "휴면";
			} else if ("0" == pScope.blacklistStatus) {
				$scope.mmsBlacklist.status = "탈퇴";
			}
		});
	});
	
	// 블랙리스트 상세 팝업 닫기
	this.close = function() {
		$window.close();
	}
	
	// 블랙리스트 수정
	this.saveBlacklist = function() {
		var blacklist = [];
		blacklist[0] = $scope.mmsBlacklist;
		blacklistService.updateBlacklist(blacklist, function() {
			alert("수정되었습니다.");
			pScope.myGrid.loadGridData();
			$window.close();
		});
	}
});
