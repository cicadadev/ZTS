
var readHistoryManagerApp = angular.module("readHistoryManagerApp", ['commonServiceModule', 'mmsServiceModule', 'gridServiceModule', 'commonPopupServiceModule' , 'ui.date', 'ngCkeditor']);

readHistoryManagerApp.controller("mms_privacyReadHistoryManagerApp_controller", function($window, $scope, $filter, commonService, gridService, commonPopupService) {
	
$scope.memberSearch = {};
$window.$scope = $scope;
	var	columnDefs = [
  						{ field: 'userId'	, width: '33%'	, colKey: "c.mms.readhistory.name"		, enableCellEdit:false	},								
  						{ field: 'detail'	, width: '33%'	, colKey: "c.mms.readhistory.detail"		, enableCellEdit:false	},
  						{ field: 'insDt'	, width: '33%'	, colKey: "c.mmsReadHistory.readDate"	, enableCellEdit:false	}
  					];
	var gridParam = {
			scope : $scope, 					//mandatory
			gridName : "grid_readHistory",		//mandatory
			url :  '/api/mms/history',  		//mandatory
			searchKey : "search",      			//mandatory
			columnDefs : columnDefs,    		//mandatory
			gridOptions : {             		//optional
			},
			callbackFn : function(){//optional
				
			}
	};
	
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	this.search = function() {
		$scope.myGrid.loadGridData();
	}
	angular.element(document).ready(function () {
		commonService.init_search($scope,'search');
	});
	
	// 검색조건 초기화
	this.reset = function() {
		commonService.reset_search($scope, 'search');
		angular.element(".day_group").find('button:first').addClass("on");
	}


});
