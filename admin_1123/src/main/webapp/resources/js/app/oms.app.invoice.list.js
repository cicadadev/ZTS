(function(){
	
	// message init
	Constants.message_keys = [];
	
	var deliveryApprovalApp = angular.module('invoiceApp', ['ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule']);
	
	deliveryApprovalApp.controller('listCtrl', function($window, $scope, $filter, logisticsService, commonService, gridService) {
		
		$scope.interfaceType = 'DELIVERY_IF_TYPE_CD.DAS';
		
		var columnDefs = [
			                  { field : 'logisticsInoutNo'                                   ,width : '100', colKey : 'c.oms.logistics.delivery.No'                  ,cellClass : 'alignC' }, 
			                  { field : 'insDt'                                              ,width : '150', colKey : 'c.oms.logistics.invoice.datetime_approval'    ,cellClass : 'alignC' }, 
			                  { field : 'sendYn'                                             ,width : '100', colKey : 'c.oms.logistics.invoice.sendData.yn'          ,cellClass : 'alignC', 
		                	    cellFilter : 'sendYnFilter'  
			                  }, 
			                  { field : 'omsOrderproduct.orderId'                            ,width : '150', colKey : 'c.oms.logistics.order_id'                     ,cellClass : 'alignC' }, 
			                  { field : 'omsOrderproduct.omsOrder.orderTypeName'             ,width : '100', colKey : 'c.oms.logistics.order_type'                   ,cellClass : 'alignC' }, 
			                  { field : 'omsOrderproduct.siteName'                           ,width : '100', colKey : 'c.oms.logistics.site'                         ,cellClass : 'alignC' }, 
			                  { field : 'omsOrderproduct.omsOrder.name1'                     ,width : '120', colKey : 'c.oms.logistics.orderer_name'                 ,cellClass : 'alignC' }, 
			                  { field : 'omsOrderproduct.omsOrder.phone2'                    ,width : '140', colKey : 'c.oms.logistics.orderer_mobile'               ,cellClass : 'alignC' }, 
			                  { field : 'omsOrderproduct.omsDeliveryaddress.name1'           ,width : '120', colKey : 'c.oms.logistics.receiver_name'                ,cellClass : 'alignC' }, 
			                  { field : 'omsOrderproduct.omsDeliveryaddress.phone1'          ,width : '140', colKey : 'c.oms.logistics.receiver_tel'                 ,cellClass : 'alignC' }, 
			                  { field : 'omsOrderproduct.omsDeliveryaddress.phone2'          ,width : '140', colKey : 'c.oms.logistics.receiver_mobile'              ,cellClass : 'alignC' }, 
			                  { field : 'omsOrderproduct.omsDeliveryaddress.zipCd'           ,width : '120', colKey : 'c.oms.logistics.zipCode'                      ,cellClass : 'alignC' }, 
			                  { field : 'omsOrderproduct.omsDeliveryaddress.deliveryAddress' ,width : '300', colKey : 'c.oms.logistics.address'                                            }, 
			                  { field : 'omsOrderproduct.productId'                          ,width : '120', colKey : 'c.oms.logistics.product_id'                   ,cellClass : 'alignC' }, 
			                  { field : 'omsOrderproduct.productName'                        ,width : '300', colKey : 'c.oms.logistics.product_name'                                       }, 
			                  { field : 'omsOrderproduct.saleproductName'                    ,width : '200', colKey : 'c.oms.logistics.saleproduct_name'                                   }, 
			                  { field : 'omsOrderproduct.dualWrapYn'                         ,width : '100', colKey : 'c.oms.logistics.invoice.double.wrap.yn'       ,cellClass : 'alignC' }, // 이중포장여부 
			                  { field : 'outReserveQty'                                      ,width : '100', colKey : 'c.oms.logistics.delivery_quantity'            ,cellClass : 'alignR' }, // 배송수량
	                  	 ];
			
		var gridParam = {
			scope : $scope,
			gridName : 'grid_invoice',
			url : '/api/oms/logistics/invoice',
			searchKey : 'search',
			columnDefs : columnDefs,
			showGroupPanel : true,
			gridOptions : {
				checkBoxEnable : false,
				pagination : false,
				virtualizationThreshold : 50
			}
		};
		
		$scope.myGrid = new gridService.NgGrid(gridParam);
		
		this.reset = function() {
			commonService.reset_search($scope, 'search');
			angular.element('.day_group').find('button').eq(0).click();
		}
		
		this.searchInvoiceList = function() {
			$scope.last_search = angular.copy($scope.search);
			$scope.myGrid.loadGridData();
			$scope.interfaceType = $scope.search.logisticsIfType;
		}
		
		this.showButton = function(type){
			if($scope.interfaceType === 'DELIVERY_IF_TYPE_CD.DAS'){
				if(type === 'DAS'){
					return true;
				}else if(type === 'HANJIN'){
					return false;
				}
			}else{
				if(type === 'DAS'){
					return false;
				}else if(type === 'HANJIN'){
					return true;
				}
			}
		}
		
		angular.element(document).ready(function() {
			commonService.init_search($scope, 'search');
		});
		
		this.init = function(){
			$window.$scope = $scope;
			$scope.search = {};
			$scope.last_search = {};
			$scope.totalOrders = 0;
			$scope.totalProducts = 0;
		};
		
		logisticsService.getAllSiteList(function(response) {
			$scope.siteList = response;
		});
		
		this.logisticsIfTypeOptions = [
	                                	{key : 'DELIVERY_IF_TYPE_CD.DAS', name : 'DAS연동'},
	                                	{key : 'DELIVERY_IF_TYPE_CD.HANJIN', name : '한진연동'}
		                             ];
		
		this.download = function(type){
			
			if (window.confirm(type + "쪽에서의 데이타 다운로드입니다. 원복불가입니다. 계속 하시겠습니까?")){
				if (window.confirm("정말로 원복불가입니다. 계속 하시겠습니까?")){
					logisticsService.download(type, this.callBack);
				} else {
					return false;
				}
			} else {
				return false;		
			}
			
		}
		
		this.sendData = function(){
			
			var rowsCount = $scope.grid_invoice.data.length;
			
			if(rowsCount < 1){
				alert("조회 데이터가 없습니다.");
				return false;
			}
			
			if (window.confirm("HanJin쪽으로 데이타 전송입니다. 원복불가입니다. 계속 하시겠습니까?")){
				if (window.confirm("정말로 원복불가입니다. 계속 하시겠습니까?")){
					var data = $scope.myGrid.getData();
					
					logisticsService.sendData(data, this.callBack);
				} else {
					return false;
				}
			} else {
				return false;		
			}
			
		}
		
		this.callBack = function(response) {
			if (response.success) {
				$scope.myGrid.loadGridData();
				alert(response.resultMessage);
			} else {
				alert(response.resultMessage);
			}
		}
		
	}).filter('sendYnFilter', function() { 
		
		var comboHash = {
			    'Y': '전송',
		    	'N': '미전송'
			};
		return function(input) { return !input ? '' :  comboHash[input]; };
	});
})();
