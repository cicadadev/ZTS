(function(){
	
	// message init
	Constants.message_keys = [];
	
	var pickingListApp = angular.module('pickingListApp', ['ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule']);
	
	pickingListApp.controller('listCtrl', function($window, $scope, $filter, logisticsService, commonService, gridService) {
		
		var columnDefs = [
			                  { field : 'invoiceDt'        ,width : '100', colKey : 'c.oms.logistics.invoice.datetime'   ,cellClass : 'alignC' }, 
			                  { field : 'locationId'       ,width : '100', colKey : 'c.oms.logistics.locationId'         ,cellClass : 'alignC' }, 
			                  { field : 'productId'        ,width : '100', colKey : 'c.oms.logistics.product_id'         ,cellClass : 'alignC' }, 
			                  { field : 'productName'      , colKey : 'c.oms.logistics.product_name'       ,cellClass : 'alignC' }, 
			                  { field : 'saleproductName'  , colKey : 'c.oms.logistics.saleproduct_name'   ,cellClass : 'alignC' }, 
			                  { field : 'boxUnitQty'       ,width : '100', colKey : 'c.oms.logistics.box.unit_qty'       ,cellClass : 'alignC' }, 
			                  { field : 'quotient'         ,width : '100', colKey : 'c.oms.logistics.box.qty'            ,cellClass : 'alignC' }, 
			                  { field : 'remainder'        ,width : '100', colKey : 'c.oms.logistics.box.ea'             ,cellClass : 'alignC' }, 
			                  { field : 'deliveryOrder'    ,width : '100'  , colKey : 'c.oms.logistics.delivery.order'     ,cellClass : 'alignC' }, 
	                  	 ];
			
		var gridParam = {
			scope : $scope,
			gridName : 'grid_pickingList',
			url : '/api/oms/logistics/picking',
			searchKey : 'search',
			columnDefs : columnDefs,
			showGroupPanel : true,
			gridOptions : {
				pagination : false,
				checkBoxEnable : false
			}
		};
		
		$scope.myGrid = new gridService.NgGrid(gridParam);
		
		this.reset = function() {
			commonService.reset_search($scope, 'search');
			angular.element('.day_group').find('button').eq(0).click();
		}
		
		this.searchPickingList = function() {
			var start = $scope.search.startDeliveryOrder;
			var end = $scope.search.endDeliveryOrder;
			
			if(start != '' && !angular.isUndefined(start) && start != null){
				if(!isNaN(start)){
					if(end == '' || angular.isUndefined(end) || end == null){
						alert("마지막 차수를 입력하십시요.")
						return false;
					}
				}else{
					alert("배송차수는 숫자만 입력하세요.")
					return false;
				}
			}else{
				if(end != '' && !angular.isUndefined(end) && end != null){
					if(!isNaN(end)){
						alert("시작 차수를 입력하십시요.")
						return false;
					}else{
						alert("배송차수는 숫자만 입력하세요.")
						return false;
					}
				}
			}
			
			if(start > end){
				alert("차수를 올바르게 입력하십시요.")
				return false;
			}
			
			$scope.myGrid.loadGridData();
		}
		
		angular.element(document).ready(function() {
			commonService.init_search($scope, 'search');
		});
		
		this.init = function(){
			$window.$scope = $scope;
			$scope.search = {};
		};
			
		logisticsService.getAllLocationList(function(response) {
			$scope.locationList = response;
		});
		
	}).directive('ngPrint', function() {
		
        var printSection = document.getElementById('printSection');

        if (!printSection) {
            printSection = document.createElement('div');
            printSection.id = 'printSection';
            document.body.appendChild(printSection);
        }
        
        function link(scope, element, attrs) {
            element.on('click', function () {
            	
            	var elemToPrint = $("div.ui-grid")[0];
//            	var elemToPrint = document.getElementById(attrs.printElementId);
                if (elemToPrint) {
                    printElement(elemToPrint);
                }
            });
        }
        function printElement(elem) {
            var domClone = elem.cloneNode(true);
            reset();
            printSection.appendChild(domClone);
            window.print();
        }
        function reset(){
        	printSection.innerHTML = '';
        }
        return {
            link: link,
            restrict: 'A'
        };
    });
	
})();
