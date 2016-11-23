//message init
Constants.message_keys = ["common.label.alert.save", "common.label.confirm.delete", "common.label.alert.fail"
                          ,"common.label.confirm.save"
                          ];

var carrotManagerApp = angular.module("carrotManagerApp", [	'commonServiceModule', 'gridServiceModule', "commonPopupServiceModule",
                                               	'spsServiceModule', 'ccsServiceModule',
                                               	'ui.date'
                                               	]);
carrotManagerApp.controller("sps_carrotManagerApp_controller", function($window, $scope, $filter, commonService, gridService, commonPopupService, carrotService) {
	
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	$scope.search = {};
	$scope.summry = [];
	
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
	                 { field: 'memberNo'		, width:'100', colKey: "mmsCarrot.memberNo"		, linkFunction:"memberDetail" , type:"number"},
	                 { field: 'memberInfo'			, width:'100', colKey: "c.mmsMember.memName"	, linkFunction:"memberDetail" },
	                 { field: 'carrot'			, width:'100', colKey: "c.sps.carrot.adjust2"	},
	                 { field: 'balanceAmt'		, width:'100', colKey: "c.sps.carrot.own"		},
	                 { field: 'carrotTypeName'	, width:'100', colKey: "c.sps.carrot.type"		},
	                 { field: 'note'			, width:'100', colKey: "c.sps.carrot.note"		},
	                 { field: 'expireDt'			, width:'100', colKey: "mmsCarrot.expireDt"		},
	                 { field: 'insDt'			, width:'100', colKey: "c.sps.carrot.reg.date"	, cellFilter: "date:\'yyyy-MM-dd\'" },
	                 { field: 'updId'			, width:'100', colKey: "c.grid.column.updId"	, userFilter :'updId,updName'},
	                 { field: 'updDt'			, width:'100', colKey: "c.grid.column.updDt"	, cellFilter: "date:\'yyyy-MM-dd\'" }
		            ];
	
	var gridParam = {
			scope : $scope,
			gridName : "carrot_data",
			url :  '/api/sps/carrot',
			searchKey : "search",
			columnDefs : columnDefs,
			gridOptions : {
				checkBoxEnable : false
			}
	};
	
	//그리드 초기화
	$scope.carrot_grid = new gridService.NgGrid(gridParam);

	$scope.searchCarrot = function() {
		carrotService.getCarrotSummry($scope.search, function(res){
			$scope.summry = [];
			angular.element(".carrotSummryData").remove();
			for(var i=0; i<res.length; i++) {
				$scope.summry.push(res[i]);
			}
			
//			common.safeApply($scope);
			$scope.carrot_grid.loadGridData();
		})
	};
	
	this.resetData = function() {
		/* search Data 초기화 */
//		angular.element(".day_group").find('button').removeClass("on");
		commonService.reset_search($scope,'search');
		angular.element(".day_group").find('button:first').addClass("on");
	}
	
	// 등록 팝업 호출
	this.carrotAdjust = function(){
		url = "/sps/carrot/popup/detail";
		popupwindow(url, "당근조정",820,346);
	}
	
	this.adjustCarrotExcel = function(){
		var param = "carrot";
		commonPopupService.gridbulkuploadPopup($scope,"callback_grid",param);
	}
	
	$scope.callback_grid = function(response){
		$scope.carrot_grid.loadGridData();
		carrotService.getCarrotSummry($scope.search, function(res){
			$scope.summry = [];
			angular.element(".carrotSummryData").remove();
			for(var i=0; i<res.length; i++) {
				$scope.summry.push(res[i]);
			}
		})
	}

	$scope.memberDetail = function(field, row) {
		$scope.memberNo = row.memberNo;
		var winName='회원상세';
		var winURL = Rest.context.path +"/mms/member/popup/detail";
		popupwindow(winURL,winName,1200,1000);
	}
	
}).controller("sps_carrotManagerDetailPopApp_controller", function($window, $scope, carrotService, commonService, commonPopupService) {
/* ****************** detail POPUP Controller *************************/
	// 팝업에서 부모 scope 접근하기 위함.		
	pScope = $window.opener.$scope;
	$window.$scope = $scope;
	
	$scope.spsCarrot = {};
	$scope.spsCarrot.mmsMember = {};
	$scope.saveFlag = false;
	
	$scope.infoType = [
	                    {val : 'EVENT'	, text : '이벤트 적립차감'},
	                    {val : 'CS'		, text : 'CS 적립차감'},
		              ];
	
	angular.element(document).ready(function () {
		
	});

	this.adjustCarrot = function() {
		
		if(!$scope.paramCheck()) return false;
		
		if (confirm(pScope.MESSAGES["common.label.confirm.save"])) {
			carrotService.saveCarrot($scope.spsCarrot, function(res){
				console.log(res);
				if(res.resultCode == "SUCCESS") {
					alert(res.resultMsg);
					$scope.saveFlag = true; 
					pScope.searchCarrot();
					window.close();
				} else {
					alert(res.resultMsg);
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
		
		if($scope.spsCarrot.infoType == "EVENT") {
			$scope.spsCarrot.carrotTypeCd = "CARROT_TYPE_CD.EVENT";
		} else if($scope.spsCarrot.infoType == "CS") {
			$scope.spsCarrot.carrotTypeCd = "CARROT_TYPE_CD.CS";
		}
		
		return true;
	}
	
	//취소
	this.close = function(){
		if($scope.saveFlag){
//			$window.opener.$scope.carrot_grid.loadGridData();	
		}		
		window.close();
	}
	
	this.memberPopup = function(){
		$scope.isPeriod = false;
		commonPopupService.memberPopup($scope,'callback_member',false);
	}
	$scope.callback_member = function(data){
		console.log(data);
		$scope.memberId = data[0].mmsMember.memberId;
		$scope.memberName = data[0].mmsMember.memberName;
		$scope.spsCarrot.memberNo = data[0].memberNo;
		common.safeApply($scope);
	}
	
	this.eraser = function(val1, val2) {
		$scope[val1] = "";
		
		if(angular.isDefined(val2)) {
			$scope[val2] = "";
		}
		$scope.spsCarrot.memberNo = "";
	}

});
