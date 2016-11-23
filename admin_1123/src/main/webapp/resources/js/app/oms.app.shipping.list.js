(function(){
	
	// message init
	Constants.message_keys = [];
	
	var shippingListApp = angular.module('shippingListApp', ['ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule', 'commonPopupServiceModule']);
	
	shippingListApp.controller('listCtrl', function($window, $scope, $filter, logisticsService, commonService, gridService, commonPopupService) {
		
		// PO일경우 업체ID
		var poBusinessId = global.session.businessId;
		$scope.poBusinessId = poBusinessId;
		
		// 유저의 권한ID
		var roleId = global.session.roleId;
		$scope.roleId = roleId;
		
		var invoiceNoDef, pageSize, pageSizes;
		// 운송장 번호 columnDef
		if(roleId === '5'){ // 물류권한
			invoiceNoDef = { field : 'invoiceNo', width : '120', colKey : 'c.oms.logistics.invoiceNo', cellClass : 'alignC' };
		}else{
			invoiceNoDef = { field : 'invoiceNo', width : '120', colKey : 'c.oms.logistics.invoiceNo', cellClass : 'alignC', enableCellEdit : true, validators : {required : true}, vKey : "omsLogistics.invoiceNo"};
		}
		// 페이지 사이즈
		if(common.isEmpty(poBusinessId)){ // BO
			pageSize = 5000;
			pageSizes = [2500, 5000, 7500];
		}else{ // PO
			pageSize = 100;
			pageSizes = [100, 200, 500];
		}
		
		var columnDefs = [
			                  { field : 'omsOrderproduct.orderDate'                            ,width : '100', colKey : 'c.oms.logistics.datetime_order'                 ,cellClass : 'alignC' },    // 주문일시
		                	  { field : 'completeDt'                                           ,width : '130', colKey : 'c.oms.logistics.shipping.process.datetime'      ,cellClass : 'alignC' },    // 출고/미출고처리일
		                	  { field : 'orderId'                                              ,width : '150', colKey : 'c.oms.logistics.order_id'                       ,cellClass : 'alignC' },    // 주문번호
		                	  { field : 'omsOrderproduct.siteName'                             ,width : '100', colKey : 'c.oms.logistics.site'                           ,cellClass : 'alignC' },    // 사이트
		                	  { field : 'omsOrderproduct.omsOrder.name1'                       ,width : '100', colKey : 'c.oms.logistics.orderer_name'                   ,cellClass : 'alignC' }, 
		                	  { field : 'omsOrderproduct.omsOrder.phone2'                      ,width : '140', colKey : 'c.oms.logistics.orderer_mobile'                 ,cellClass : 'alignC' }, 
		                	  { field : 'omsOrderproduct.omsDeliveryaddress.name1'             ,width : '100', colKey : 'c.oms.logistics.receiver_name'                  ,cellClass : 'alignC' }, 
		                	  { field : 'omsOrderproduct.omsDeliveryaddress.phone1'            ,width : '140', colKey : 'c.oms.logistics.receiver_tel'                   ,cellClass : 'alignC' }, 
		                	  { field : 'omsOrderproduct.omsDeliveryaddress.phone2'            ,width : '140', colKey : 'c.oms.logistics.receiver_mobile'                ,cellClass : 'alignC' }, 
		                	  { field : 'omsOrderproduct.omsDeliveryaddress.zipCd'             ,width : '100', colKey : 'c.oms.logistics.zipCode'                        ,cellClass : 'alignC' }, 
		                	  { field : 'omsOrderproduct.omsDeliveryaddress.deliveryAddress'   ,width : '300', colKey : 'c.oms.logistics.address'                        					   }, 
		                	  { field : 'omsOrderproduct.deliveryMethod'                       ,width : '100', colKey : 'c.oms.logistics.delivery_method'                ,cellClass : 'alignC' },    // 배송방법
		                	  invoiceNoDef                                                                                                                                                      ,    // 운송장번호
		                	  { field : 'deliveryServiceName'                                  ,width : '120', colKey : 'c.oms.logistics.delivery.service.biz.name'      ,cellClass : 'alignC' },    // 배송업체
		                	  { field : 'omsOrderproduct.businessName'                         ,width : '200', colKey : 'c.oms.logistics.business_name'                  ,cellClass : 'alignC' },    // 공급업체
		                	  { field : 'omsOrderproduct.productId'                            ,width : '100', colKey : 'c.oms.logistics.product_id'                     ,cellClass : 'alignC' },    // 상품번호
		                	  { field : 'omsOrderproduct.productName'                          ,width : '300', colKey : 'c.oms.logistics.product_name'                   					   },    // 상품명
		                	  { field : 'omsOrderproduct.saleproductName'                      ,width : '200', colKey : 'c.oms.logistics.saleproduct_name'               					   },    // 단품명
		                	  { field : 'omsOrderproduct.erpSaleproductId'                     ,width : '120', colKey : 'c.oms.logistics.erp.barcode'                    ,cellClass : 'alignC' },    // ERP바코드
		                	  { field : 'outReserveQty'                                        ,width : '100', colKey : 'c.oms.logistics.order.qty'                      ,cellClass : 'alignR' },    // 주문수량
		                	  { field : 'outQty'                                               ,width : '100', colKey : 'c.oms.logistics.shipping.qty'                   ,cellClass : 'alignR' },    // 출고수량
		                	  { field : 'cancelDeliveryQty'                                    ,width : '100', colKey : 'c.oms.logistics.cancel.shipping.qty'            ,cellClass : 'alignR' },    // 미출고수량
		                	  { field : 'omsOrderproduct.orderProductStateCd'                  ,width : '100', colKey : 'c.oms.logistics.shipping.state'                 ,cellClass : 'alignC' 
	                		    ,cellFilter : 'orderProductStateFilter'  
	                	  },    																																									 // 출고여부
		                	  { field : 'cancelDeliveryReasonName'                             ,width : '120', colKey : 'c.oms.logistics.shipping.cancel_reason'         ,cellClass : 'alignC' },    // 미출고사유
		                	  { field : 'omsOrderproduct.personalCustomsCode'                  ,width : '150', colKey : 'c.oms.logistics.overseas.purchase.gubun.no'     ,cellClass : 'alignC' },    // 해외구매대행식별번호
		                	  { field : 'omsOrderproduct.lpNo'                                 ,width : '150', colKey : 'c.oms.logistics.lp_no'                          ,cellClass : 'alignC' }     // lpNo
                  	    ];
			
		var gridParam = {
			scope : $scope,
			gridName : 'grid_shippingList',
			url : '/api/oms/logistics/shipping',
			searchKey : 'search',
			columnDefs : columnDefs,
			showGroupPanel : true,
			gridOptions : {
				checkBoxEnable : false,
				paginationPageSizes: pageSizes,		
		 		paginationPageSize: pageSize,
		 		cellEditableCondition: function( $scope ) {
					return ($scope.row.entity.saleTypeCd == 'SALE_TYPE_CD.CONSIGN' && $scope.row.entity.outQty > 0); 
				}
			}
		};
		
		$scope.myGrid = new gridService.NgGrid(gridParam);
		
		this.reset = function() {
			commonService.reset_search($scope, 'search');
			angular.element('.day_group').find('button').eq(0).click();
		}
		
		this.searchShippingList = function() {
			if(!common.isEmpty(poBusinessId)){
				$scope.search.deliveryMethod = 'CONSIGN';
			}else if(common.isEmpty(poBusinessId) && roleId === '5'){
				$scope.search.deliveryMethod = 'PURCHASE';
			}
			$scope.myGrid.loadGridData();
		}
		
		angular.element(document).ready(function() {
			commonService.init_search($scope, 'search');
		});
		
		this.init = function(){
			$window.$scope = $scope;
			$scope.search = {};
			$scope.totalOrders = 0;
			$scope.totalProducts = 0;
		};
		
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
		
		logisticsService.getAllSiteList(function(response) {
			$scope.siteList = response;
		});
		
	}).filter('orderProductStateFilter', function() { 
		
		var comboHash = {
				'ORDER_PRODUCT_STATE_CD.SHIP': '출고완료',
				'ORDER_PRODUCT_STATE_CD.PARTIALDELIVERY': '부분출고',
		    	'ORDER_PRODUCT_STATE_CD.CANCELDELIVERY': '미출고'
			};
		return function(input) { return !input ? '' :  comboHash[input]; };
	});
	
})();
