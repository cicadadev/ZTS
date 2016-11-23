(function(){
	
	// message init
	Constants.message_keys = [];
	
	var locationApp = angular.module('locationApp', ['ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule', 'commonPopupServiceModule']);
	
	locationApp.controller('listCtrl', function($window, $scope, $filter, logisticsService, commonService, gridService, commonPopupService) {
		
		var columnDefs = [
			                  { field : 'warehouseName'    ,width : '200', colKey : 'c.oms.logistics.warehouse.name'     ,cellClass : 'alignC' },                                                        // 창고명
		                	  { field : 'locationId'       ,width : '200', colKey : 'c.oms.logistics.location.name'      ,enableCellEdit : true, 
			                	cellEditableCondition: function( $scope ) { return $scope.row.entity.insDt == null; }  ,vKey : 'pmsWarehouselocation.locationId'           ,validators : {required : true}
		                	  },                                                                                                                                                                         // 로케이션명
		                	  { field : 'locationUseYn'    ,width : '150', colKey : 'c.oms.logistics.location.use.yn'    ,cellClass : 'alignC'     
		                		,enableCellEdit : true     ,cellFilter : 'useYnFilter'                                  ,vKey : 'pmsWarehouselocation.locationUseYn'         ,validators : {required : true}
		                	  },                                                                                                                                                                         // 로케이션사용여부
		                	  { field : 'insId'            ,width : '150', colKey : 'c.grid.column.insId'                ,cellClass : 'alignC' },                                                        // 등록자
		                	  { field : 'insDt'            ,width : '200', colKey : 'c.grid.column.insDt'                ,cellClass : 'alignC' },                                                        // 등록일시
		                	  { field : 'updId'            ,width : '150', colKey : 'c.grid.column.updId'                ,cellClass : 'alignC' },                                                        // 최종수정자
		                	  { field : 'updDt'                          , colKey : 'c.grid.column.updDt'                ,cellClass : 'alignC' },                                                        // 최종수정일시
                  	    ];
			
		var gridParam = {
			scope : $scope,
			gridName : 'grid_location',
			url : '/api/oms/logistics/location',
			searchKey : 'search',
			columnDefs : columnDefs,
			showGroupPanel : true,
			gridOptions : {
				checkBoxEnable : false,
			}
		};
		
		$scope.myGrid = new gridService.NgGrid(gridParam);
		
		this.reset = function() {
			commonService.reset_search($scope, 'search');
			angular.element('.day_group').find('button').eq(0).click();
		}
		
		this.searchLocationList = function() {
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
			var gridApi = $scope.grid_location.gridApi;
			var dirtyRows;
			
			if (gridApi && gridApi.rowEdit) {
				dirtyRows = gridApi.rowEdit.getDirtyRows();
			}
			
			if(dirtyRows.length > 0){
				if(confirm("저장하지 않은 정보는 사라집니다. 이동 하시겠습니까?")){
					$window.location.href="/oms/logistics/location/mapping";
				}
			}else{
				$window.location.href="/oms/logistics/location/mapping";
			}
		}
		
		this.addRow = function() {
			$scope.myGrid.addRow({
				crudType  : 'I'
			});
		}
		
//		logisticsService.getAllWarehouseList(function(response) {
//			var warehouseIds = [];
//			for(var i = 0; i < response.length; i++){
//				warehouseIds[i] = response[i].warehouseId;
//			}
//			
//			if(!common.isEmpty(warehouseIds)){
//				$scope.search.warehouseId = warehouseIds;
//			}
//			$scope.warehouseList = response;
//		});
		
		this.saveLocationGrid = function(){
			$scope.myGrid.saveGridData(null, function(data){
				$scope.myGrid.loadGridData();
			});
		}

	});
	
})();
