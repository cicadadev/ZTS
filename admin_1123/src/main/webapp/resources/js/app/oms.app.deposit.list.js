//message init
Constants.message_keys = ["common.label.alert.save", "common.label.confirm.delete", "common.label.alert.fail"
                          ,"common.label.confirm.save"
                          ];

var depositListApp = angular.module("depositListApp", [	'commonServiceModule', 'gridServiceModule', "commonPopupServiceModule",
                                               	'omsServiceModule', 'ccsServiceModule',
                                               	'ui.date'
                                               	]);
depositListApp.controller("oms_depositListApp_controller", function($window, $scope, $filter, commonService, gridService, commonPopupService, depositService) {
	
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	$scope.search = {};
	
	$scope.infoType = [
	                    {val : 'ID',text : '회원ID'},
	                    {val : 'NAME',text : '회원명'}
		              ];
	
	// 초기화
	angular.element(document).ready(function () {		
		commonService.init_search($scope,'search');
	});			
	
	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});
	
	var columnDefs = [
	                 { field: 'memberNo'		, width:'100', colKey: "mmsDeposit.memberNo"		, linkFunction:"memberDetail" , type:"number"},
	                 { field: 'memberInfo'			, width:'100', colKey: "c.mmsMember.memName"	, linkFunction:"memberDetail" },
	                 { field: 'depositTypeName'	, width:'100', colKey: "c.oms.deposit.type"		},
	                 { field: 'depositAmt'			, width:'100', colKey: "c.oms.deposit.adjust2"	},
	                 { field: 'balanceAmt'		, width:'100', colKey: "c.oms.deposit.own"		},
	                 { field: 'insDt'			, width:'100', colKey: "c.oms.deposit.reg.date"	, cellFilter: "date:\'yyyy-MM-dd\'" },
	                 { field: 'note'			, width:'100', colKey: "c.oms.deposit.note"		},
	                 { field: 'orderId'			, width:'100', colKey: "c.oms.order.id"		},
	                 { field: 'claimNo'			, width:'100', colKey: "c.oms.claim.id"		},
	                 { field: 'updId'			, width:'100', colKey: "c.grid.column.updId"	, userFilter :'updId,updName'},
	                 { field: 'updDt'			, width:'100', colKey: "c.grid.column.updDt"	, cellFilter: "date:\'yyyy-MM-dd\'" }
		            ];
	
	var gridParam = {
			scope : $scope,
			gridName : "deposit_data",
			url :  '/api/oms/deposit',
			searchKey : "search",
			columnDefs : columnDefs,
			gridOptions : {
				checkBoxEnable : false
			}
	};
	
	//그리드 초기화
	$scope.deposit_grid = new gridService.NgGrid(gridParam);

	$scope.searchDeposit = function() {
		$scope.deposit_grid.loadGridData();
	};
	
	this.resetData = function() {
		/* search Data 초기화 */
//		angular.element(".day_group").find('button').removeClass("on");
		commonService.reset_search($scope,'search');
		angular.element(".day_group").find('button:first').addClass("on");
	}
	
	// 등록 팝업 호출
	this.depositAdjust = function(){
		url = "/oms/deposit/popup/detail";
		popupwindow(url, "당근조정",820,346);
	}
	
	this.adjustDepositExcel = function(){
		var param = "deposit";
		commonPopupService.gridbulkuploadPopup($scope,"callback_grid",param);
	}
	
	$scope.callback_grid = function(response){
		$scope.deposit_grid.loadGridData();
	}

	$scope.memberDetail = function(field, row) {
		$scope.memberNo = row.memberNo;
		var winName='회원상세';
		var winURL = Rest.context.path +"/mms/member/popup/detail";
		popupwindow(winURL,winName,1200,1000);
	}
	
}).controller("oms_depositListDetailPopApp_controller", function($window, $scope, depositService, commonService, commonPopupService) {
/* ****************** detail POPUP Controller *************************/
	// 팝업에서 부모 scope 접근하기 위함.		
	pScope = $window.opener.$scope;
	$window.$scope = $scope;
	
	$scope.mmsDeposit = {};
	$scope.mmsDeposit.mmsMember = {};
	$scope.saveFlag = false;
	
	angular.element(document).ready(function () {
		
	});

	this.adjustDeposit = function() {
		
		if(!$scope.paramCheck()) return false;
		
		if (confirm(pScope.MESSAGES["common.label.confirm.save"])) {
			depositService.saveDeposit($scope.mmsDeposit, function(res){
				if(res.content == "success") {
					$scope.saveFlag = true; 
					pScope.searchDeposit();
					window.close();
				} else {
					alert(res.content);
				}
			});
		} else {
			return false;
		}

	}
	
	$scope.paramCheck = function() {
		
		//폼 체크
		if(!commonService.checkForm($scope.form)){
			return false;
		}
		
		if($scope.userid == null || $scope.userid == '') {
			alert("회원을 선택해주세요");
			return false;			
		}
		
		if($scope.mmsDeposit.depositTypeCd == null || $scope.mmsDeposit.depositTypeCd == '') {
			alert("조정예치금 적용항목을 선택해주세요");
			return false;
		}
		
		return true;
	}
	
	//취소
	this.close = function(){
		if($scope.saveFlag){
			$window.opener.$scope.deposit_grid.loadGridData();	
		}		
		window.close();
	}
	
	this.memberPopup = function(){
		$scope.isPeriod = false;
		commonPopupService.memberPopup($scope,'callback_member',false);
	}
	$scope.callback_member = function(data){
		$scope.userid = data[0].mmsMember.memberId;
		$scope.username = data[0].mmsMember.memberName
		$scope.mmsDeposit.memberNo = data[0].mmsMember.memberNo;
		common.safeApply($scope);
	}
	
	this.eraser = function(val1, val2) {
		$scope[val1] = "";
		
		if(angular.isDefined(val2)) {
			$scope[val2] = "";
		}
		$scope.mmsDeposit.memberNo = "";
	}

});
