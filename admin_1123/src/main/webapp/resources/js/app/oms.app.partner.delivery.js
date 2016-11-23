(function(){
	
	// message init
	Constants.message_keys = [];
	
	var partnerDeliveryApp = angular.module('partnerDeliveryApp', ['ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule', 'commonPopupServiceModule']);
	
	partnerDeliveryApp.controller('listCtrl', function($window, $scope, $filter, logisticsService, commonService, gridService, commonPopupService) {
		
		// PO일경우 업체ID
		var poBusinessId = global.session.businessId;
		$scope.poBusinessId = poBusinessId;
		
		var columnDefs = [
			                  { field : 'deliveryReserveDt'                         ,width : '150', colKey : 'c.oms.logistics.datetime_order'             ,cellClass : 'alignC' },    // 주문일시
			                  { field : 'businessName'                              ,width : '200', colKey : 'c.oms.logistics.business_name'              ,cellClass : 'alignC' },    // 공급업체
			                  { field : 'orderId'                                   ,width : '120', colKey : 'c.oms.logistics.order_id'                   ,cellClass : 'alignC' },    // 주문번호
//			                  { field : 'omsLogistics.logisticsInoutNo'             ,width : '100', colKey : 'c.oms.logistics.delivery.No'                ,cellClass : 'alignC' },    // 배송번호
//			                  { field : 'omsLogistics.insDt'                        ,width : '120', colKey : 'c.oms.logistics.delivery.ins_dt'            ,cellClass : 'alignC' },    // 배송승인일시  
			                  { field : 'orderDeliveryTypeCd'                       ,width : '120', colKey : 'c.oms.logistics.delivery_type'              ,cellClass : 'alignC' 
		                	    ,cellFilter : 'orderDeliveryTypeFilter'  
			                  },                                                                                                                                                      // 발송유형
			                  { field : 'orderProductStateCd'                       ,width : '120', colKey : 'c.oms.logistics.cancel_yn'                  ,cellClass : 'alignC' 
			                	,cellFilter : 'orderProductStateFilter'   
			                  }, 
			                  { field : 'omsOrder.name1'                            ,width : '120', colKey : 'c.oms.logistics.orderer_name'               ,cellClass : 'alignC' }, 
			                  { field : 'omsOrder.phone2'                           ,width : '140', colKey : 'c.oms.logistics.orderer_mobile'             ,cellClass : 'alignC' }, 
			                  { field : 'omsDeliveryaddress.name1'                  ,width : '120', colKey : 'c.oms.logistics.receiver_name'              ,cellClass : 'alignC' }, 
			                  { field : 'omsDeliveryaddress.phone1'                 ,width : '140', colKey : 'c.oms.logistics.receiver_tel'               ,cellClass : 'alignC' }, 
			                  { field : 'omsDeliveryaddress.phone2'                 ,width : '140', colKey : 'c.oms.logistics.receiver_mobile'            ,cellClass : 'alignC' }, 
			                  { field : 'omsDeliveryaddress.zipCd'                  ,width : '100', colKey : 'c.oms.logistics.zipCode'                    ,cellClass : 'alignC' }, 
			                  { field : 'omsDeliveryaddress.deliveryAddress'        ,width : '300', colKey : 'c.oms.logistics.address'                                          }, 
			                  { field : 'productId'                                 ,width : '100', colKey : 'c.oms.logistics.product_id'                 ,cellClass : 'alignC' }, 
			                  { field : 'productName'                               ,width : '300', colKey : 'c.oms.logistics.product_name'                                     }, 
			                  { field : 'saleproductName'                           ,width : '200', colKey : 'c.oms.logistics.saleproduct_name'                                 }, 
			                  { field : 'outReserveQty'                             ,width : '100', colKey : 'c.oms.logistics.delivery_quantity'          ,cellClass : 'alignR' },     // 배송수량 
			                  { field : 'omsDeliveryaddress.note'                   ,width : '200', colKey : 'c.oms.logistics.delivery.memo'              ,cellClass : 'alignC' },     // 배송메시지
			                  { field : 'textOptionValue'                           ,width : '150', colKey : 'c.oms.logistics.text.option'                ,cellClass : 'alignC' },     // 텍스트옵션
			                  { field : 'personalCustomsCode'                       ,width : '150', colKey : 'c.oms.logistics.overseas.purchase.gubun.no' ,cellClass : 'alignC' },     // 해외구매대행식별번호
			                  { field : 'deliveryCancelReasonName'                  ,width : '150', colKey : 'c.oms.logistics.cancel_reason'              ,cellClass : 'alignC' }      // 배송승인취소사유
	                  	 ];
			
		var gridParam = {
			scope : $scope,
			gridName : 'grid_partnerDelivery',
			url : '/api/oms/logistics/partner/delivery',
			searchKey : 'search',
			columnDefs : columnDefs,
			showGroupPanel : true,
			gridOptions : {
//				enableRowSelection : true,
//				multiSelect : true,
//				rowSelectionFn : function(row) {
//					row.entity.checked = row.isSelected;
//				},
//				isRowSelectable : function(row) {
//					var isRowSelectable = true;
//					if (row.entity.orderProductStateCd == 'ORDER_PRODUCT_STATE_CD.DELIVERY_ORDER' || row.entity.orderProductStateCd == 'ORDER_PRODUCT_STATE_CD.SHIP' 
//						|| row.entity.orderProductStateCd == 'ORDER_PRODUCT_STATE_CD.SHIP' || row.entity.orderProductStateCd == 'ORDER_PRODUCT_STATE_CD.CONFIRMED') {
//						isRowSelectable = false;
//					}
//					if (isRowSelectable) {
//						row.cursor = 'pointer';
//					}
//					return isRowSelectable;
//				}
				pagination : false
			}
		};
		
		$scope.myGrid = new gridService.NgGrid(gridParam);
		
		this.reset = function() {
			commonService.reset_search($scope, 'search');
			angular.element('.day_group').find('button').eq(0).click();
		}
		
		this.init = function(){
			$window.$scope = $scope;
			$scope.search = {};
		};
		
		this.searchOrderProduct = function() {
			$scope.myGrid.loadGridData();
		}
		
		angular.element(document).ready(function() {
			commonService.init_search($scope, 'search');
		});
		
		this.approval = function(type){
			
			if(!$scope.myGrid.isChecked()){
				return false;
			}
			
			if (window.confirm("저장 하시겠습니까?")) {
					var selectedRows = $scope.myGrid.getSelectedRows();
					
					logisticsService.partnerApproval(selectedRows, this.callBack);
			} else {
				return false;
			}
		}
		
		this.callBack = function(response) {
			if (response.success) {
				if($scope.allCheck == true){
					$scope.allCheck = false;
				}
				$scope.myGrid.loadGridData();
				alert(response.resultMessage);
			} else {
				alert(response.resultMessage);
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
		
		this.eraser = function() {
			$scope.filePath = "";
		}
		
		this.eraser = function(val1, val2) {
			$scope.search[val1] = "";
			
			if(angular.isDefined(val2)) {
				$scope.search[val2] = "";
			}
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
