

var productReviewApp = angular.module("productReviewApp", ['commonServiceModule', 'ccsServiceModule', 'gridServiceModule', 'commonPopupServiceModule' , 'ui.date', 'ngCkeditor']);

Constants.message_keys = ["common.label.alert.save", "common.label.confirm.save", "common.label.alert.cancel", "common.label.confirm.cancel", "common.label.alert.delete", "common.label.confirm.delete"];

productReviewApp.controller("ccs_productReviewListApp_controller", function($window, $scope, $compile, $filter, reviewService, commonService, gridService, commonPopupService,commonFactory){
	
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	$scope.search = {};
	
	$scope.serchType = [
	                    {val : '',text : ''},
	                    {val : '',text : ''},
	                   ];
	
	$scope.memSearchType = [
	                        {val : 'NAME',text : '이름'},
	                        {val : 'ID',text : 'ID'}
	                   ];
	
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});
	
	angular.element(document).ready(function () {
		commonService.init_search($scope,'search');
	});
	
	cellTemplateStr = "<div class=\"ui-grid-cell-contents\"  title=\"\">{{row.entity.permitYn == 'Y'?'체험단':'일반'}}</div>"; 
	
	var columnDefs =  [
	                   { field: 'reviewNo', 			width : '100',			displayName : "상품평번호",		colKey: "pmsReview.reviewNo",			linkFunction : 'linkFunction'},
	                   { field: 'title', 				width : '150',			colKey: "pmsReview.title",	linkFunction : 'linkFunction'},
	                   { field: 'productId', 			width : '100',			displayName : "상품번호",		colKey: "pmsReview.productId", 			linkFunction : 'linkProductFunction'},
	                   { field: 'pmsProduct.name', 		width : '100',			colKey: "pmsProduct.name", 			linkFunction : 'linkProductFunction'},
	                   { field: 'saleproductId', 		width : '100',			displayName : "단품번호" /*colKey: "pmsReview.productId"*/},
	                   { field: 'permitYn', 			width : '100',			displayName : "유형",			colKey: "c.ccs.review.pruductType"		,cellTemplate :cellTemplateStr},
//	                   { field: 'siteName', 					colKey: "ccsSite.name"},
	                   { field: 'memberId', 			userFilter :'mmsMember.memberId,mmsMember.memberName',	width : '100',			colKey: "c.mmsMember.memName"},
//	                   { field: 'memberNo', 					colKey: "pmsReview.memberNo"},
	                   { field: 'insDt', 				displayName : "등록일시",	width : '150',		colKey: "c.grid.column.insDt",			cellFilter: "date:\'yyyy-MM-dd\'"},
	                   { field: 'orderDt', 				width : '150',			displayName : "최종구매일시", /*colKey: "c.grid.column.insDt",*/			cellFilter: "date:\'yyyy-MM-dd\'"},
	                   { field: 'rating', 				displayName : "점수",		width : '100',		colKey: "pmsReview.rating"},
	                   { field: 'displayYn', 			width : '100',			colKey: "pmsReview.displayYn",			enableCellEdit:true,  cellFilter:'displayYnFilter'},
	                   { field: 'bestYn', 				displayName : "우수 상품평",	width : '100',		colKey: "pmsReview.bestYn",				enableCellEdit:true, cellFilter:'yesOrNoFilter'},
	                   { field: 'updId', 				userFilter :'updId,updName',	displayName : "최종수정자",	width : '150',		colKey: "c.grid.column.updDt"},
	                   { field: 'updDt', 				displayName : "최종수정일시",	width : '100',		colKey: "c.grid.column.updId",			cellFilter: "date:\'yyyy-MM-dd\'"}
	       	]
	       	
	       	var gridParam = {
	       		scope : $scope,
	       		gridName : "grid_review",
	       		url : '/api/pms/product/productReview',
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
					
			$scope.reviewNo = '';
			$scope.popup = function(url) {
				popupwindow(url,"reviewDetailPopup", 1100, 650);
			}
			
			
			$scope.linkFunction = function(field, row) {
				$scope.reviewNo = row.reviewNo;
				$scope.productId = row.productId;
				$scope.popup('/ccs/productReview/popup/detail');
			}
			
			$scope.linkProductFunction = function(field, row) {
				commonPopupService.openProductDetailPopup($scope, row.productId);
			}
			
			// 검색 지우기
			this.eraser = function(){
				$scope.search.memberId = "";
				$scope.search.memberName = "";
			}
			
			// 검색어 변경시 초기화
			this.change = function(){
				$scope.search.memberId = "";
			}
			
			// 회원 검색
			this.searchMember = function() {
				commonPopupService.memberPopup($scope,'callback_mem',false);					
			}
			
			$scope.callback_mem = function(data) {
				$scope.search.memberId = data[0].mmsMember.memberId;
				$scope.search.memberName = data[0].mmsMember.memberName;
				$scope.$apply();
			}
			
			this.linkProductFunction = function() {
				commonPopupService.openProductDetailPopup($scope, pmsReview.productId);
			}
			
			
}).controller("ccs_productReviewDetailApp_controller", function($window, $scope, $filter, reviewService, commonService, gridService, commonPopupService){
	
	pScope = $window.opener.$scope;// 부모창의 scope
	$scope.pmsReview = {};

	//message
	commonService.getMessages(function(response){
		$scope.MESSAGES = response;
	});

	
	this.productPopup = function(){
		commonPopupService.openProductDetailPopup($scope, $scope.pmsReview.productId);
	}
	this.detail = function(){
		$scope.pmsReview.reviewNo = pScope.reviewNo;
		$scope.pmsReview.productId = pScope.productId;
		reviewService.getReview($scope.pmsReview, function(response) {
			$scope.pmsReview = response;
			console.log("$scope.pmsReview", $scope.pmsReview);
		});
	}
	
	this.save = function(){
		// 폼 체크
		if (!commonService.checkForm($scope.form2)) {
			return;
		}
		
		// 확인 메세지
		if (confirm($scope.MESSAGES["common.label.confirm.save"])) {
			reviewService.update($scope.pmsReview, function(response) {
				if (response.content == '1') {
					pScope.$apply();
					pScope.myGrid.loadGridData();
					alert($scope.MESSAGES["common.label.alert.save"]);
					$window.close();
				} else {
					alert('상품평 변경시 에러가 발생 하였습니다.');
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
	
});
