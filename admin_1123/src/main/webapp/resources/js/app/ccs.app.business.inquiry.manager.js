

var businessInquiryApp = angular.module("businessInquiryApp", ['commonServiceModule', 'ccsServiceModule', 'gridServiceModule', 'commonPopupServiceModule' , 'ui.date', 'ngCkeditor']);

businessInquiryApp.controller("businessInquiryListController", function($window, $scope, $compile, $filter, businessinquiryService, commonService, gridService, commonPopupService,commonFactory){
	
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.	
	$scope.search = {};
	
	angular.element(document).ready(function () {
		/*var date = new Date();
		$scope.search.startDate = $filter('date')(date.setDate(date.getDate()-7), Constants.date_format_2);
		$scope.search.endDate = $filter('date')(new Date(), Constants.date_format_2);*/
		
		commonService.init_search($scope,'search');
	});
	
	cellTelStr1 = "<div class=\"ui-grid-cell-contents\"  title=\"\">{{row.entity.phone | tel }}</div>";
	cellTelStr2 = "<div class=\"ui-grid-cell-contents\"  title=\"\">{{row.entity.managerPhone1 | tel }}</div>";
	
	var columnDefs =  [
	                   { field: 'businessInquiryNo', 	width : '10%',		cellClass : 'alignC',		displayName : "상담번호",			colKey: "businessInquiry.businessInquiryNo",	linkFunction : 'linkFunction'},
	                   { field: 'name', 				width : '10%',		cellClass : 'alignC',		displayName : "회사명",			colKey: "businessInquiry.name",					linkFunction : 'linkFunction'},
	                   { field: 'phone', 				width : '10%',		cellClass : 'alignC',		displayName : "전화번호",			colKey: "businessInquiry.phone", 			cellTemplate :cellTelStr1},
	                   { field: 'managerName', 			width : '15%',		cellClass : 'alignC',		displayName : "영업담당자이름",		colKey: "businessInquiry.managerName"},
	                   { field: 'managerPhone1', 		width : '15%',		cellClass : 'alignC',		displayName : "영업담당자전화번호", 	colKey: "businessInquiry.managerPhone1", 	cellTemplate :cellTelStr2},
	                   { field: 'managerEmail', 		width : '15%',		cellClass : 'alignC',		displayName : "영업담당자 E-mail",	colKey: "businessInquiry.managerEmail"},
	                   { field: 'mdInfo',	 			width : '10%',		cellClass : 'alignC',		displayName : "담당 MD"},
	                   { field: 'insDt',		cellClass : 'alignC', 				/*width : '15%',*/	displayName : "신청일시",			colKey: "c.grid.column.insDt",			cellFilter: "date:\'yyyy-MM-dd\'"}
	       	]
	       	
	       	var gridParam = {
	       		scope : $scope,
	       		gridName : "gridBusinessInquiry",
	       		url : '/api/ccs/businessinquiry',
	       		searchKey : "search",
	       		columnDefs : columnDefs,
	       		gridOptions : {
	       			checkBoxEnable: false
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
				angular.element(".day_group").find('button:first').addClass("on");
			}
			
			this.eraser = function(name){
				if(name === 'md'){
//					$scope.search[name+'Id'] = "";
					$scope.search[name+'Name'] = "";
				}else{
					$scope.search[name] = "";
				}
			}
			
			$scope.popup = function(url) {
				popupwindow(url,"BusinessinquiryPopup", 1100, 600);
			}
			
			// 입점상담 상세 팝업
			$scope.linkFunction = function(field, row) {
				$scope.businessInquiryNo = row.businessInquiryNo;
				$scope.popup('/ccs/businessInquiry/popup/detail');
			}
			
			// MD 검색
			this.searchMd = function() {
				commonPopupService.userPopup($scope,"callback_md",false,"USER_TYPE_CD.MD");
			}			
			$scope.callback_md = function(data) {
//				$scope.search.mdId = data[0].userId;
				$scope.search.mdName = data[0].name;
				$scope.$apply();
			}
}).controller("businessInquiryDetailController", function($window, $scope, $filter, businessinquiryService, commonService, gridService, commonPopupService){
	
	pScope = $window.opener.$scope;// 부모창의 scope
	$scope.ccsBusinessinquiry = {};
	
	this.detail = function(){
		$scope.ccsBusinessinquiry.businessInquiryNo = pScope.businessInquiryNo;
		
		businessinquiryService.getBusinessInquiry($scope.ccsBusinessinquiry, function(response) {
			$scope.ccsBusinessinquiry = response;
		});
	}
	
	// 홈페이지 URL 링크
	this.linkUrl = function() {
		$window.open($scope.ccsBusinessinquiry.homepageUrl);
	}
	
	this.close = function() {
		$window.close();
	}
	
});
