// 스타일샵 화면 모듈
var styleShopDetailApp = angular.module("styleShopDetailApp", ["commonServiceModule", "ccsServiceModule", "dmsServiceModule", "pmsServiceModule", "gridServiceModule", "commonPopupServiceModule",
                                               "ui.date", "ngCkeditor"]);

//메시지
Constants.message_keys = ["common.label.alert.save", "dms.exhibit.oneday.date.validate", "common.label.confirm.save", "common.label.alert.cancel", "common.label.confirm.cancel"];

// 스타일샵 상품 이미지 관리
styleShopDetailApp.controller("dms_styleShopDetailManagerApp_controller", function($compile, $window, $scope, $filter, gridService, commonService, commonPopupService) {
	$window.$scope = $scope;
	$scope.sSearch = {};
	
	var	columnDefs = [
	   	              	{ field: 'styleNo'		, colKey: "c.pms.styleproduct.styleNo"			 								 },
	   	              	{ field: 'styleImg'		, colKey: "mmsStyle.styleImg"			, linkFunction : 'linkFunction'		 },
			            { field: 'title'		, colKey: "c.dms.styledetail.title"												 },
			            { field: 'memberId'		, colKey: "c.mmsMember.memberId"													 },
			            { field: 'memberName'	, colKey: "c.mmsMember.memName"													 },
			            { field: 'insDt'		, colKey: "c.grid.column.insDt"													 },
			            { field: 'displayYn'	, colKey: "c.dms.styledetail.displayYn"		, enableCellEdit:true	, cellFilter : 'displayYnFilter' },
			         ];
	
	var gridParam = {
			scope : $scope, 					//mandatory
			gridName : "grid_styleShopDetail",	//mandatory
			url :  '/api/dms/styleShopDetail',  //mandatory
			searchKey : "search",        		//mandatory
			columnDefs : columnDefs,    		//mandatory
			gridOptions : {             		//optional
			},
			callbackFn : function(){//optional
				
			}
	};
	
	$scope.myGrid = new gridService.NgGrid(gridParam);
	
	angular.element(document).ready(function () {
		commonService.init_search($scope,'search');
	});
	
	$scope.infoType = [
	                    {val : 'ID',text : '회원ID'},
	                    {val : 'NAME',text : '회원명'}
		              ];
	
	// 입점상담 상세 팝업
	$scope.linkFunction = function(field, row) {
		window.open(global.config.imageDomain + row.styleImg,'_blank');
	}
	
	// 검색조건 초기화
	this.reset = function() {
		commonService.reset_search($scope, 'search');
		angular.element(".day_group").find('button:first').addClass("on");
	}
	
	
});