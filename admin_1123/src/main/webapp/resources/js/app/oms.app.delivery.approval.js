(function(){
	
	// message init
	Constants.message_keys = [];
	
	var deliveryApprovalApp = angular.module('deliveryApprovalApp', ['ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule', 'ui.grid.pinning']);
	
	deliveryApprovalApp.controller('listCtrl', function($window, $scope, $filter, $timeout, $templateCache, logisticsService, commonService, gridService, restFactory, templateService) {
		
		$templateCache.put('custom/checkBoxHeader', templateService.checkBoxHeader);
		
		var columnDefs = [
			                  { field : 'checked'                                    ,width : '40',  cellTemplate : templateService.checkBox,
			                	headerCellTemplate : "custom/checkBoxHeader"  
			                  }, 
			                  { field : 'orderDate'                                  ,width : '100', colKey : 'c.oms.logistics.order.date'              ,cellClass : 'alignC' }, 
			                  { field : 'orderId'                                    ,width : '150', colKey : 'c.oms.logistics.order_id'                ,cellClass : 'alignC' }, 
			                  { field : 'orderTypeName'                              ,width : '100', colKey : 'c.oms.logistics.order_type'              ,cellClass : 'alignC' }, 
			                  { field : 'siteName'                                   ,width : '120', colKey : 'c.oms.logistics.site'                    ,cellClass : 'alignC' }, 
			                  { field : 'orderDeliveryTypeCd'                        ,width : '100', colKey : 'c.oms.logistics.delivery_type'           ,cellClass : 'alignC'
			                	,cellFilter : 'orderDeliveryTypeFilter'  
			                  }, 
			                  { field : 'orderProductStateCd'                        ,width : '120', colKey : 'c.oms.logistics.cancel_yn'               ,cellClass : 'alignC' 
			                	,cellFilter : 'orderProductStateFilter'   
			                  }, 
			                  { field : 'ordererNm'                                  ,width : '100', colKey : 'c.oms.logistics.orderer_name'            ,cellClass : 'alignC' }, 
			                  { field : 'ordererMobile'                              ,width : '140', colKey : 'c.oms.logistics.orderer_mobile'          ,cellClass : 'alignC' }, 
			                  { field : 'receiverNm'                                 ,width : '120', colKey : 'c.oms.logistics.receiver_name'           ,cellClass : 'alignC' }, 
			                  { field : 'receiverPhone'                              ,width : '140', colKey : 'c.oms.logistics.receiver_tel'            ,cellClass : 'alignC' }, 
			                  { field : 'receiverMobile'                             ,width : '140', colKey : 'c.oms.logistics.receiver_mobile'         ,cellClass : 'alignC' }, 
			                  { field : 'zipCd'                                      ,width : '100', colKey : 'c.oms.logistics.zipCode'                 ,cellClass : 'alignC' }, 
			                  { field : 'deliveryAddress'                            ,width : '300', colKey : 'c.oms.logistics.address'                                       }, 
			                  { field : 'productId'                                  ,width : '100', colKey : 'c.oms.logistics.product_id'              ,cellClass : 'alignC' }, 
			                  { field : 'productName'                                ,width : '300', colKey : 'c.oms.logistics.product_name'                                  }, 
			                  { field : 'saleproductName'                            ,width : '200', colKey : 'c.oms.logistics.saleproduct_name'                              }, 
			                  { field : 'deliveryMethod'                             ,width : '120', colKey : 'c.oms.logistics.delivery_method'         ,cellClass : 'alignC' }, 
			                  { field : 'dualWrapYn'                                 ,width : '100', colKey : 'c.oms.logistics.invoice.double.wrap.yn'  ,cellClass : 'alignC' }, 
			                  { field : 'outReserveQty'                              ,width : '100', colKey : 'c.oms.logistics.delivery_quantity'       ,cellClass : 'alignR' }, 
			                  { field : 'locationId'                                 ,width : '150', colKey : 'c.oms.logistics.product_location_id'     ,cellClass : 'alignC' }, 
			                  { field : 'realStockQty'                               ,width : '100', colKey : 'c.oms.logistics.product_stock_cnt'       ,cellClass : 'alignR' }, 
			                  { field : 'deliveryCancelReasonName'                   ,width : '150', colKey : 'c.oms.logistics.cancel_reason'           ,cellClass : 'alignC' },
			                  { field : 'sendErrorReasonName'                        ,width : '150', colKey : 'c.oms.logistics.send_error_reason'       ,cellClass : 'alignC' }
	                  	 ];
			
		var gridParam = {
			scope : $scope,
			gridName : 'grid_orderProduct',
			url : '/api/oms/logistics',
			searchKey : 'search',
			columnDefs : columnDefs,
			showGroupPanel : true,
			gridOptions : {
				pagination : false,
				checkBoxEnable : false,
				flatEntityAccess : true,
				virtualizationThreshold : 50
       		}
		};
		
		$scope.myGrid = new gridService.NgGrid(gridParam);
		
		this.reset = function() {
			commonService.reset_search($scope, 'search');
			$scope.siteAll = true;
			angular.element('.day_group').find('button').eq(0).click();
		}
		
		this.searchOrderProduct = function() {
			$scope.myGrid.loadGridData();
		}
		
		angular.element(document).ready(function() {
			commonService.init_search($scope, 'search');
		});
		
		this.init = function(){
			$window.$scope = $scope;
			$scope.search = {deliveryType : ''};
			$scope.siteIds = [];
			$scope.siteAll = true;
			$scope.deliveryTypeArray = ['1','2','3','8','9','10','11'];
			$scope.totalOrders = 0;
			$scope.totalProducts = 0;
			$scope.clickNum = 1;
		};
		
		this.deliveryTypeList = [
			                         {key : '',  name : '전체'},
			                         {key : '1',  name : '전체(의류)'},
			                         {key : '2',  name : '전체(의류 포함)'},
			                         {key : '3',  name : '전체(의류 미포함)'},
			                         {key : '4',  name : '0to7(전체)'},
			                         {key : '5',  name : '0to7(의류)'},
			                         {key : '6',  name : '0to7(의류 포함)'},
			                         {key : '7',  name : '0to7(의류 미포함)'},
			                         {key : '8',  name : '제휴사(전체)'},
			                         {key : '9',  name : '제휴사(의류)'},
			                         {key : '10', name : '제휴사(의류 포함)'},
			                         {key : '11', name : '제휴사(의류 미포함)'},
			                         {key : '12', name : '프리미엄멤버십'},
			                         {key : '13', name : '수출(전체)'},
			                         {key : '14', name : '수출(의류)'},
			                         {key : '15', name : '수출(의류 포함)'},
			                         {key : '16', name : '수출(의류 미포함)'},
		                        ];
		
		this.approval = function(type){
			var selectedRows = $scope.myGrid.getSelectedRows();
			var rows = $scope.myGrid.getData();
			
			if(!$scope.myGrid.isChecked()){
				return false;
			}
			
			if(selectedRows.length != rows.length){
				for(var i = 0; i < selectedRows.length; i++){
					var orderId = selectedRows[i].orderId;
					var orderProductNo = selectedRows[i].orderProductNo;
					
					for(var j = 0; j < rows.length; j++){
						if(rows[j].orderId == orderId && rows[j].orderProductNo == orderProductNo && !rows[j].checked){
							alert('선물포장비가 선택되지 않았습니다.');
							return false;
						}
					}
				}
			}
			
			if (window.confirm("저장 하시겠습니까?")) {
				logisticsService.approval(type, selectedRows, this.callBack);
			} else {
				return false;
			}
		}
		
		this.callBack = function(response){
			if (response.success) {
				$scope.myGrid.loadGridData();
				$scope.countOrderData();
				alert(response.resultMessage);
			} else {
				alert(response.resultMessage);
			}
		}
		
		this.checkDeliveryType = function(){
			var deliveryType = $scope.search.deliveryType;
			
			if(!(common.isEmpty(deliveryType) || $scope.deliveryTypeArray.indexOf(deliveryType) > -1)){
				$scope.siteAll = true;
				this.checkAllSite();
			}
		}
		
		this.checkAllSite = function(){
			if($scope.siteAll) {
    			angular.forEach($scope.siteList, function(site, index){
    				site.isSelected = false;
    			});
    		}
			$scope.search.siteIds = '';
		}
		
		this.checkSite = function(){
			var siteIds = $scope.siteIds;
			if(common.isEmpty(siteIds)){
				$scope.search.siteIds = '';
			}else{
				$scope.search.siteIds = "'" + siteIds.join("','") + "'";
			}
		}
		
		this.exportExcel = function(){
			var url = Rest.context.path + "/excel/oms/logistics";
			var data = $scope.search;
			
			restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, function(response){
				document.location='files/[excelDown].xls';
			});
		}
		
		this.checkBoxAll = function(checked) {
			var grid = $scope.grid_orderProduct.gridApi.grid;
			var rows = grid.rows;
			for (var i = 0; i < rows.length; i++) {
				var row = rows[i];
				if (grid.options.isRowSelectable(row)) {
					row.entity.checked = checked;
				}
			}
			if(!checked){
				$scope.clickNum = 1;
			}
			$scope.countOrderData();
		}
		
		this.checkOneHundred = function() {
			var grid = $scope.grid_orderProduct.gridApi.grid;
			var rows = grid.rows;
			
			if(common.isEmpty(rows)){
				return false;
			}
			
			var selectRatio = 100;
			var selectScope = selectRatio * $scope.clickNum;
			var selectNum = selectScope > rows.length ? rows.length : selectScope;
			for (var i = 0; i < selectNum; i++) {
				var row = rows[i];
				if (grid.options.isRowSelectable(row)) {
					row.entity.checked = true;
				}
			}
			$scope.clickNum++;
			$scope.countOrderData();
			
			if(selectScope > rows.length){
				$timeout(function() {
					alert('모두 선택하였습니다.');
				}, 100);
			}
		}
		
		$scope.countOrderData = function(){
			var list = $scope.myGrid.getSelectedRows();
			var orderIds = [];
			
			for (var i = 0; i < list.length; i++) {
				if(orderIds.indexOf(list[i].orderId) < 0){
					orderIds.push(list[i].orderId);
				}
			}
			$scope.totalOrders = orderIds.length;
			$scope.totalProducts = list.length;
		}
		
		logisticsService.getAllSiteList(function(response) {
			for(var i = 0; i < response.length; i++){
				response[i].isSelected = false;
			}
			$scope.siteList = response;
		});
		
		$scope.selectCheck = function(grid, rowEntity){
			var rows = grid.rows;
			for (var i = 0; i < rows.length; i++) {
				if (rowEntity.orderId == rows[i].entity.orderId && rowEntity.orderProductNo == rows[i].entity.orderProductNo) {// find gift wrap
					if(rowEntity.checked){
						rows[i].entity.checked = true;
					}else{
						rows[i].entity.checked = false;
					}
				}
			}
			$scope.countOrderData();
		}
		
		$scope.$on("radioChanged", function(event, args) {
			$scope.$apply(function() {
				var target = args.target;
				
				if(target.value == 'Y'){
					$scope.search.sendErrorReasonCd = '';
				}else if(target.value == '' || target.value == 'ORDER_PRODUCT_STATE_CD.READY' || target.value == 'ORDER_PRODUCT_STATE_CD.PARTIALDELIVERY' || target.value == 'ORDER_PRODUCT_STATE_CD.CANCELDELIVERY'){
					$scope.search.deliveryCancelReasonCd = '';
				}
			});
		});
		
	}).filter('orderDeliveryTypeFilter', function() { 
		
		var comboHash = {
			    'ORDER_DELIVERY_TYPE_CD.ORDER': '주문',
		    	'ORDER_DELIVERY_TYPE_CD.EXCHANGE': '클레임',
	    		'ORDER_DELIVERY_TYPE_CD.REDELIVERY': '클레임'
			};
		return function(input) { return !input ? '' :  comboHash[input]; };
	}).filter('orderProductStateFilter', function() { 
		
		var comboHash = {
		    	'ORDER_PRODUCT_STATE_CD.PARTIALDELIVERY': '부분출고',
		    	'ORDER_PRODUCT_STATE_CD.CANCELDELIVERY': '미출고',
	    		'ORDER_PRODUCT_STATE_CD.CANCELAPPROVAL': '승인취소'
			};
		return function(input) { return !input ? '' :  comboHash[input]; };
	});
	
})();
