// channel 화면 모듈
var channelApp = angular.module("channelApp", ["commonServiceModule", "ccsServiceModule", "gridServiceModule","ui.date"]);
//메시지
Constants.message_keys = ["common.label.alert.save", "common.label.confirm.save", "common.label.alert.cancel", "common.label.confirm.cancel", "common.label.alert.delete", "common.label.confirm.delete"];

channelApp.controller("ccs_channelManagerApp_controller", function($window, $scope, $filter, ccsService, gridService, commonService) {
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	var	columnDefs = [
			           { field: 'channelId'			, width: '10%'	, colKey: "c.ccsChannel.channelId"	 , enableCellEdit:false			  , linkFunction:'channelDetail'										},
			           { field: 'name'				, width: '15%'	, colKey: "c.ccsChannel.channelName" , vKey: "ccsChannel.name" , linkFunction:'channelDetail' , enableCellEdit:false													},
			           { field: 'channelTypeCd'		, width: '10%'	, colKey: "c.ccsChannel.channelType" , vKey: "ccsChannel.channelTypeCd" , enableCellEdit:false	, dropdownCodeEditor : "CHANNEL_TYPE_CD", cellFilter :'channelTypeFilter'	, validators:{required:true}	},
			           { field: 'channelStateCd'	, width: '10%'	, colKey: "c.ccsChannel.channelState", enableCellEdit:false , dropdownCodeEditor : "CHANNEL_STATE_CD", cellFilter :'channelStateFilter'	, validators:{required:true}					},
			           { field: 'insId'				, width: '10%'	, userFilter :'insId,insName',	colKey: "c.grid.column.insId"		 , enableCellEdit:false																					},
			           { field: 'insDt'				, width: '15%'	, displayName : "등록일시", 		colKey: "c.grid.column.insDt"		 , enableCellEdit:false			  , cellFilter: "date:\'yyyy-MM-dd\'"									},
			           { field: 'updId'				, width: '10%'	, userFilter :'updId,updName',	colKey: "c.grid.column.updId"		 , enableCellEdit:false																					},
			           { field: 'updDt'				, width: '15%'	, displayName : "최종수정일시", 		colKey: "c.grid.column.updDt"		 , enableCellEdit:false			  , cellFilter: "date:\'yyyy-MM-dd\'"									}
		           ];
	
	var gridParam = {
			scope : $scope, 					//mandatory
			gridName : "grid_channel",			//mandatory
			url :  '/api/ccs/common/channel',   //mandatory
			searchKey : "channelSearch",        //mandatory
			columnDefs : columnDefs,    		//mandatory
			gridOptions : {             		//optional
			},
			callbackFn : function(){			//optional
			}
	};
	
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});
	
	$scope.businessList = [];
	
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	angular.element(document).ready(function () {	
		commonService.init_search($scope, 'channelSearch');
		ccsService.getChannelBusiness(function(response) {
			for (var i=0; i < response.length; i++) {
				if (response[i].businessId != 'undefined') {
					$scope.businessList.push({
						"id": response[i].businessId,
						"name": response[i].name
					});
				}
			}
		});
	});
	
	
	$scope.reset = function() {
		/* search Data 초기화 */
		commonService.reset_search($scope, 'channelSearch');
		angular.element(".day_group").find('button:first').addClass("on");
		$scope.channelSearch.startDt ="";
		$scope.channelSearch.endDt = "";
	}
	
	 $scope.channelDetail = function(field, row){
		  var winName='channelModiFy';
		  $scope.channelId = {channelId : row.channelId};
		  
		  var winURL = Rest.context.path +"/ccs/channel/popup/detail";
		  popupwindow(winURL,winName,1100,385);
	 };
	 
	 // 채널 등록 팝업
	 this.regChannel = function() {
		 $scope.channelId="";
	     var winName='channelRegister';
	     var winURL = Rest.context.path +"/ccs/channel/popup/detail";
	     popupwindow(winURL,winName,1100,430);
	 };
	 
	// 채널 삭제
	this.deleteChannel = function() {
		var checkList = [];
		var checkList = $scope.myGrid.getSelectedRows();
		var delFlag = true;
		console.log(checkList);
		for (var i=0; i < checkList.length; i++) {
			if (checkList[i].channelStateCd != 'CHANNEL_STATE_CD.READY') {
				delFlag = false;
				break;
			}
		}
		
		if (delFlag) {
			$scope.myGrid.deleteGridData();
		} else {
			alert("대기 상태가 아닌 채널이 포함되어 삭제가 불가합니다.");
		}
	}
	 
}).filter('channelTypeFilter', function() {// CHANNEL_TYPE_CD 
	
	var comboHash = {
			'CHANNEL_TYPE_CD.AD': '제휴',
			'CHANNEL_TYPE_CD.BANNER': '배너',
			'CHANNEL_TYPE_CD.EMAIL': '이메일',
			'CHANNEL_TYPE_CD.KEYWORD': '키워드'
	};
	
	return function(input) { return !input ? '' :  comboHash[input]; };		
}).filter('channelStateFilter', function() {// CHANNEL_TYPE_CD 
	
	var comboHash = {
			'CHANNEL_STATE_CD.READY': '대기',
			'CHANNEL_STATE_CD.RUN': '진행중',
			'CHANNEL_STATE_CD.STOP': '종료',
	};
	
	return function(input) { return !input ? '' :  comboHash[input]; };		
		
}).controller("ccs_channelDetailPopApp_controller", function($window, $scope, ccsService, commonService) {
	
	this.close = function(){
		$window.close();
	}
	
	$scope.search = {};
	
	// 채널 상세 Init
	var pScope = $window.opener.$scope;
	
	$scope.businessList = pScope.businessList;
	
	$scope.init = function() {
		if (pScope.channelId != '' && pScope.channelId != 'undefined') {
			ccsService.getChannelDetail(pScope.channelId, function(response) {
				$scope.channelDetail = response;
			});
		}
	};
	
	// 채널 수정
	this.updateChannel = function() {
		
		//폼 체크
		if (!commonService.checkForm($scope.channelForm)) {
			return;
		}
		if (window.confirm(pScope.MESSAGES["common.label.confirm.save"])) {
			
			if (pScope.channelId != '' && pScope.channelId != 'undefined') {
				$scope.channelDetails = [];
				$scope.channelDetails.push($scope.channelDetail);
				ccsService.updateChannel($scope.channelDetails, function(response) {
					pScope.myGrid.loadGridData();
					alert(pScope.MESSAGES["common.label.alert.save"]);
					$window.close();
				});
				
			} else {
				$scope.channelDetail.channelStateCd = 'CHANNEL_STATE_CD.READY';
				ccsService.insertChannel($scope.channelDetail, function(response) {
					pScope.myGrid.loadGridData();
					alert(pScope.MESSAGES["common.label.alert.save"]);
					$window.close();
				});
			}
		}
	}
	
	// 기획전 상태 변경
	this.changeState = function(param) {
		$scope.ccsChannel = {};
		$scope.ccsChannel.channelId = $scope.channelDetail.channelId;
		var msg = "";
		var preState = "";
		var afterState = "";
		if (param == 'run') {
			$scope.ccsChannel.channelStateCd = 'CHANNEL_STATE_CD.RUN';
			preState = "대기";
			afterState = "진행";
		} else if (param == 'stop') {
			$scope.ccsChannel.channelStateCd = 'CHANNEL_STATE_CD.STOP';
			preState = "진행";
			afterState = "중지";
		}
		
		msg = "'"+ preState +"상태'를 '" + afterState + "상태' (으)로 변경하시겠습니까?";
		
		if (confirm(msg)) {
			ccsService.updateChannelStatus($scope.ccsChannel, function() {
				alert("'"+afterState+"상태'(으)로 변경되었습니다.");
				$window.location.reload();
			});
		}
	}
	
});

