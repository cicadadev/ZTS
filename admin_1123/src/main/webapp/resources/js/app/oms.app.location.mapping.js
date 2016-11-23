(function(){
	
	// message init
	Constants.message_keys = [];
	
	var locationMappingApp = angular.module('locationMappingApp', ['ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule', 'commonPopupServiceModule']);
	
	locationMappingApp.controller('listCtrl', function($window, $scope, $filter, logisticsService, commonService, gridService, commonPopupService) {
		
		var columnDefs = [
			                  { field : 'businessName'                        ,width : '150', colKey : 'c.pms.epexcproduct.business'                ,cellClass : 'alignC' },                              // 공급업체
		                	  { field : 'pmsProduct.productId'                ,width : '150', colKey : 'c.pms.epexcproduct.productNo'               ,cellClass : 'alignC' },                              // 상품번호
		                	  { field : 'pmsProduct.name'                     ,width : '300', colKey : 'c.pms.epexcproduct.productNm'                                     },                              // 상품명
		                	  { field : 'name'                                ,width : '200', colKey : 'c.oms.order.saleproduct_name'                                     },                              // 단품명
		                	  { field : 'erpSaleproductId'                    ,width : '150', colKey : 'c.oms.logistics.erp.barcode'                ,cellClass : 'alignC' },                              // ERP바코드
		                	  { field : 'locationId'                          ,width : '120', colKey : 'c.oms.logistics.location.name'                                    },                              // 로케이션명
		                	  { field : 'pmsWarehouselocation.locationUseYn'  ,width : '150', colKey : 'c.oms.logistics.location.use.yn'            ,cellClass : 'alignC',    cellFilter : 'useYnFilter'},// 로케이션사용여부
		                	  { field : 'deliveryTogetherQty'                 ,width : '150', colKey : 'c.pms.saleproduct.delivery.together.qty'    ,cellClass : 'alignC' },                              // 배송제한수량
		                	  { field : 'insId'                               ,width : '120', colKey : 'c.grid.column.insId'                        ,cellClass : 'alignC' },                              // 등록자
		                	  { field : 'insDt'                               ,width : '150', colKey : 'c.grid.column.insDt'                        ,cellClass : 'alignC' },                              // 등록일시
		                	  { field : 'updId'                               ,width : '120', colKey : 'c.grid.column.updId'                        ,cellClass : 'alignC' },                              // 최종수정자
		                	  { field : 'updDt'                               ,width : '150', colKey : 'c.grid.column.updDt'                        ,cellClass : 'alignC' },                              // 최종수정일시
                  	    ];
			
		var gridParam = {
			scope : $scope,
			gridName : 'grid_locationMapping',
			url : '/api/oms/logistics/location/mapping',
			searchKey : 'search',
			columnDefs : columnDefs,
			showGroupPanel : true,
			gridOptions : {
				checkBoxEnable : true
			},
		};
		
		$scope.myGrid = new gridService.NgGrid(gridParam);
		
		this.reset = function() {
			commonService.reset_search($scope, 'search');
			angular.element('.day_group').find('button').eq(0).click();
		}
		
		this.searchLocationMappingList = function() {
			$scope.myGrid.loadGridData();
		}
		
		angular.element(document).ready(function() {
			commonService.init_search($scope, 'search');
		});
		
		this.init = function(){
			$window.$scope = $scope;
			$scope.search = {};
		};
		
		// 탭전환
		this.changeTab = function(){
			var gridApi = $scope.grid_locationMapping.gridApi;
			var dirtyRows;
			
			if (gridApi && gridApi.rowEdit) {
				dirtyRows = gridApi.rowEdit.getDirtyRows();
			}
			
			if(dirtyRows.length > 0){
				if(confirm("저장하지 않은 정보는 사라집니다. 이동 하시겠습니까?")){
					$window.location.href="/oms/logistics/location/list";
				}
			}else{
				$window.location.href="/oms/logistics/location/list";
			}
		}
		
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
		
		this.eraser = function(val1, val2) {
			$scope.search[val1] = "";
			
			if(angular.isDefined(val2)) {
				$scope.search[val2] = "";
			}
		}
		
		this.popup = {
			update : function(field, row){
				if(!$scope.myGrid.isChecked()){
					return false;
				}
				$scope.selectedRows = $scope.myGrid.getSelectedRows();
				
				var winName = 'location Update Popup';
				var winURL = Rest.context.path + '/oms/logistics/location/popup/mappingDetail';
				
				popupwindow(winURL, winName, 1000, 600);
			}
		}
		
		this.bulk = {
			upload : {
				excel : function() {
					var winName = 'location Bulk Upload';
					var winURL = Rest.context.path + "/oms/logistics/location/popup/bulkupload";

					popupwindow(winURL, winName, 1100, 400);
				}
			}
		}
		
	}).controller("locationMappingDetailCtrl", function($window, $scope, $filter, logisticsService, commonService, gridService, commonPopupService){
		
		Constants.message_keys = ["common.label.confirm.save"];
		
		var pScope = $window.opener.$scope;
		var selectedRows = pScope.selectedRows
		
		var columnDefs = [
		                  	  { field : 'productName'     ,width : '300', colKey : 'c.oms.order.product_name'                                         },    // 상품명
		                  	  { field : 'name'            ,width : '300', colKey : 'c.oms.order.saleproduct_name'                                     },    // 단품명
			                  { field : 'locationId'                    , colKey : 'c.oms.logistics.product_location_id'        ,cellClass : 'alignC' },    // 로케이션명
	              	     ];
		
		var gridParam = {
			scope : $scope,
			gridName : 'saleproduct_grid',
			url : '/api/oms/logistics/location/mapping/saleproduct',
			searchKey : 'search',
			columnDefs : columnDefs,
			showGroupPanel : true,
			gridOptions : {
				checkBoxEnable : false,
				pagination : false 
			}
		};
		
		$scope.myGrid = new gridService.NgGrid(gridParam);
		
		logisticsService.getAllLocationList(function(response) {
			$scope.locationList = response;
		});
		
		$scope.checkBoxAll = function(grid, evt) {
			var allCheck = $(evt.target).is(":checked");
			var rows = grid.rows;
			for (var i = 0; i < rows.length; i++) {
				var row = rows[i];
				if (grid.options.isRowSelectable(row)) {
					row.entity.checked = allCheck;
				}
			}
		}
		
		this.save = function(){
			
			if(!commonService.checkForm($scope.form)){
				return;
			}
			
			if(!confirm("저장 하시겠습니까?")){
				return;
			}
			
			var saleproductList = $scope.myGrid.getData();
			if(saleproductList.length > 0){
				
				for(var i=0; i < saleproductList.length; i++){
					saleproductList[i].locationId = $scope.locationId;
				};
				
				logisticsService.updateLocationId(saleproductList, function(response) {
					if (response.success) {
						if ($scope.$$phase != '$apply' && $scope.$$phase != '$digest') {
							$scope.$apply();
						}
						$scope.myGrid.loadGridData();
						pScope.myGrid.loadGridData();
						alert("로케이션 정보를 수정하였습니다.");
					} else {
						alert(response.resultMessage);
					}
				});
				
			}else{
				alert("변경할 단품정보가 없습니다.");
				return;
			}
		}
		
		this.close = function() {
			$window.close();
		}
		
		this.init = function(){
			$window.$scope = $scope;
			$scope.search = {'pmsSaleproductList' : selectedRows};
			
			$scope.myGrid.loadGridData();
		};
		
	}).controller("bulkUploadCtrl", function($window, $scope, logisticsService, commonService) {
		
		var pScope = $window.opener.$scope;
		
		var upPath = 'location';
		var fileName = '상품로케이션일괄변경_템플릿.xlsx';

		$scope.files = [];
		// listen for the file selected event
		$scope.$on("fileSelected", function(event, args) {
			$scope.$apply(function() {
				$scope.files.push(args.file);
			});
		});

		// 팝업 닫기
		this.close = function() {
			$window.close();
		}

		// 파일 경로 지우기
		this.eraser = function() {
			$scope.filePath = "";
		}

		// 파일 일괄 업로드
		uploadfile = function(file, url){
			commonService.uploadFileToUrl(file, null, url, function(response) {
				var result = JSON.parse(response);
				$scope.totalCnt = result.totalCnt;
				$scope.successCnt = result.successCnt;
				$scope.failCnt = result.failCnt;
				$scope.excelPath = result.excelPath;
				
				alert('업로드를 완료하였습니다.');
				
				if ($scope.$$phase != '$apply' && $scope.$$phase != '$digest') {
					$scope.$apply();
				}
				pScope.myGrid.loadGridData();
			});
		}
		this.checkExcel = function() {
			var file = $scope.files[0];
			if (file != null) {
				if (!/(\.xlsx|\.xls)$/i.test(file.name)) {
					$scope.filePath = "";
					alert("엑셀 파일이 아닙니다.");
					return false;
				}
				var url = Rest.context.path + '/api/oms/logistics/bulk/' + upPath;
				uploadfile(file, url);
			}
		}

		// 엑셀템플릿 다운로드 
		this.downTemplate = function() {
			commonService.getConfig("excel.download.path.template", function(response) {
				var fullPath = response.content + '/' + fileName;
				
//				$window.location = Rest.context.path + "/api/ccs/common/downTemplate?templateName=" + fullPath;
				$window.location = fullPath;
			});
		}

		// 오류데이터 다운로드
		this.downFailDataExcel = function() {
			if ($scope.excelPath) {
				$window.location = Rest.context.path + "/api/ccs/common/downTemplate?templateName=" + $scope.excelPath;
			}
		}
		
	});
	
})();
