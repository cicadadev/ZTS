/**
 * 체험단 관리 APP
 */

var reviewpermitApp = angular.module("reviewpermitApp", ['commonServiceModule', 'ccsServiceModule', 'gridServiceModule', 'commonPopupServiceModule' , 'ui.date', 'ngCkeditor']);

reviewpermitApp.controller("reviewpermitListController", function($window, $scope, $compile, $filter, reviewpermitService, commonService, gridService, commonPopupService,commonFactory){
	
	$window.$scope = $scope;// 팝업에서 부모 scope 접근하기 위함.
	
	$scope.search = {};
	
	angular.element(document).ready(function () {		
		$scope.search.startDate = $filter('date')(new Date(), Constants.date_format_2);
		$scope.search.endDate = $filter('date')(new Date(), Constants.date_format_2);
		
		commonService.init_search($scope,'search');
	});
	
	var columnDefs =  [
	                   { field: 'permitNo', 							colKey: "pmsReviewpermit.permitNo"},
	                   { field: 'memberNo', 							colKey: "pmsReviewpermit.memberNo"},
	                   { field: 'mmsMember.userid', 					colKey: "mmsMember.userid"},
	                   { field: 'mmsMember.mmsCustomer.customername', 	colKey: "c.mmsMember.memName"},
	                  // { field: 'mmsMember.memberTypeName', 			colKey: "mmsMember.memberTypeCd"},
	                   { field: 'mmsMember.mmsMemberZts.memGradeName', 	colKey: "c.mmsMember.memberGrade"},
	                   { field: 'mmsMember.cellno', 					colKey: "c.mmsMember.phone1"},
	                   { field: 'productId', 							colKey: "pmsReviewpermit.productId"},
	                   { field: 'pmsProduct.name', 						colKey: "pmsProduct.name"},
	                   { field: 'mmsMember.status', 					colKey: "c.mmsMember.memberState"},
	                  // { field: 'mmsMember.insDt', 						colKey: "mmsMember.regDt"},
	                   { field: 'insDt', 								colKey: "c.grid.column.insDt"},
	                   { field: 'insId', 								colKey: "c.grid.column.insId"},
	                   { field: 'updDt', 								colKey: "c.grid.column.updDt"},
	                   { field: 'updId', 								colKey: "c.grid.column.updId"}
	       	]
	       	
	       	var gridParam = {
	       		scope : $scope,
	       		gridName : "grid_reviewpermit",
	       		url : '/api/ccs/user/reviewpermit',
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
			
			this.reset = function(){		
				commonService.reset_search($scope,'search');
			}
			
			this.insertPopup = function(){
				popupwindow('/ccs/reviewpermit/popup/detail',"reviewpermitInsertPopup", 1200, 700);
			}
	
}).controller("reviewpermitDetailController", function($window, $scope, $compile, $filter, reviewpermitService, commonService, gridService, commonPopupService,commonFactory){
	var pScope = $window.opener.$scope;// 부모창의 scope
	
	$scope.search = {};
	$scope.prdSearch = {};
	//console.log(pScope);
	
	angular.element(document).ready(function () {		
		$scope.search.regStartDt = $filter('date')(new Date(), Constants.date_format_2);
		$scope.search.regEndDt = $filter('date')(new Date(), Constants.date_format_2);
		
		commonService.init_search($scope,'search');
	});
	
	var columnDefs =  [
	                   { field: 'memberNo', 					colKey: "mmsMember.memberNo"},
	                   { field: 'userid', 						colKey: "mmsMember.userid"},
	                   { field: 'mmsCustomer.customername', 	colKey: "mmsCustomer.customername"},
	                   { field: 'mmsMemberZts.memGradeName', 	colKey: "c.ccs.qna.member.class"},
	                   { field: 'cellno', 						colKey: "c.mmsMember.phone1"},
	                   { field: 'status', 						colKey: "mmsMember.status"}
	                  // { field: 'regDt', 						colKey: "mmsMember.regDt"}
	       	]
	       	
	       	var gridParam = {
	       		scope : $scope,
	       		gridName : "grid_member",
	       		url : '/api/mms/member/reviewpermit',
	       		searchKey : "search",
	       		columnDefs : columnDefs,
	       		gridOptions : {
	       		// row 선택시 호출 함수 정의
					rowSelectionFn : function(row){
						$scope.prdList(row.entity);
					},
	 				enableRowSelection: true,
	 				noUnselect : true
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
			
	var columnDefs1 =  [
	                    { field: 'memberNo', 							colKey: "pmsReviewpermit.memberNo"},	
	                   { field: 'productId', 							colKey: "pmsProduct.productId"},
	                   { field: 'pmsProduct.name', 						colKey: "pmsProduct.name"},
	                   { field: 'pmsProduct.productTypeName', 			colKey: "pmsProduct.productTypeCd"},
	                   { field: 'pmsProduct.saleStateName', 			colKey: "pmsProduct.saleStateCd"},
	                   { field: 'pmsProduct.pmsBrand.name', 			colKey: "pmsBrand.name"},
	                  // { field: 'pmsProduct.dmsCategoryName', 			colKey: "dmsDisplaycategorylang.name"},
	                   { field: 'pmsProduct.pmsCategory.name', 			colKey: "pmsCategory.name"},
	                   { field: 'pmsProduct.saleStartDt', 				colKey: "pmsProduct.saleStartDt"},
	                   { field: 'pmsProduct.saleEndDt', 				colKey: "pmsProduct.saleEndDt"},
	                   { field: 'pmsProduct.insDt', 					colKey: "c.grid.column.insDt"},
	                   { field: 'pmsProduct.insId', 					colKey: "c.grid.column.insId"},
	                   { field: 'pmsProduct.updDt', 					colKey: "c.grid.column.updDt"},
	                   { field: 'pmsProduct.updId', 					colKey: "c.grid.column.updId"}
	       	]
	       	
	       	var gridParam1 = {
	       		scope : $scope,
	       		gridName : "grid_product",
	       		url : '/api/ccs/user/reviewpermit/product',
	       		searchKey : "prdSearch",
	       		columnDefs : columnDefs1,
	       		gridOptions : {
	       		},
	       		callbackFn : function() {
	       		//myGrid.loadGridData();
	       		}
	       	};
	
			$scope.prdGrid = new gridService.NgGrid(gridParam1);	
			
			//=================== search	
			$scope.prdSearchGrid = function(){
				$scope.prdGrid.loadGridData();
			}
			
			$scope.changeMode = function(param) {
				if (param) {
					$scope.product = false;
					$scope.grid_product.data = [];
				}
				$scope.myGrid.toggleRowSelection();
			}
			
			this.reset = function(){		
				commonService.reset_search($scope,'search');
				$scope.product = false;
			}
			
			$scope.prdList = function(row){
			  	$scope.product = true;
				$scope.prdSearch.memberNo = row.memberNo;
				$scope.prdSearchGrid();
				/*reviewpermitService.getReviewpermiProductList($scope.search, function(response) {
					$scope.prdGrid.loadGridData();
				});*/
				
			}
			
			this.searchPopup = function() {
				commonPopupService.productPopup($scope,"callback_product",true);
			}
			
			$scope.callback_product = function(data) {
				if(data.length > 0){
					for (var i=0; i<data.length; i++ ) {
						
						var duplicate = false;
						for (var j = 0; j < $scope.grid_product.data.length; j++) {
							
							if(data[i].productId === $scope.grid_product.data[j].productId){
								duplicate = true;
								break;
							}
						}
						console.log(" $scope.prdSearch.memberNo:"+ $scope.prdSearch.memberNo);	
						if(!duplicate){
							$scope['prdGrid'].addRow({
								memberNo		: $scope.prdSearch.memberNo,
								productId 	: data[i].productId,
								pmsProduct 	: { name					: data[i].name,	
												productTypeName			: data[i].productTypeName,
												saleStateName			: data[i].saleStateName,
												pmsBrand 				: { name : data[i].pmsBrand.name},
												pmsCategory 			: { name : data[i].pmsCategory.name},
												saleStartDt				: data[i].saleStartDt, 	
												saleEndDt				: data[i].saleEndDt,
												insDt					: data[i].insDt,
												insId					: data[i].insId,
												updDt					: data[i].updDt,
												updId					: data[i].updId	
											}
								});
							
						}
					}
					
					setTimeout(function(){$scope.prdGrid.saveGridData();},1000);
					
				}
				
			}
			this.close = function() {
				$window.close();
			}
});