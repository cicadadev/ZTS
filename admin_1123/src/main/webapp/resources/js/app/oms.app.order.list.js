// message init
Constants.message_keys = [];

var orderListApp = angular.module('orderListApp', [
		'ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule'
]);

orderListApp.controller('listCtrl', function($window, $scope, $filter, templateService, commonfunction, gridService, commonService, orderService, logisticsService) {
	
	if (!common.isEmpty($("#pageId").val())) {
		$scope.pageId = $("#pageId").val();
	}
	
	var columnDefs = [
			{
				field : 'orderId',
				width : '150',
				colKey : 'c.oms.order.id',
				cellClass : 'alignC',
				// vKey : 'omsOrder.orderId',
				linkFunction : 'func.popup.order'
			}, {
				field : 'orderDt',
				width : '150',
				colKey : 'c.oms.order.datetime',
				cellClass : 'alignC',
			}, {
				field : 'pgShopId',
				// field : 'omsPayments[0].pgShopId',
				width : '100',
				colKey : 'c.oms.payment.pg_shop_id',
				cellClass : 'alignC'
			}, {
				field : 'memo',
				width : '60',
				colKey : 'c.oms.order.memo',
				cellClass : 'alignC',
				cellTemplate : templateService.memo('row.entity.orderId', 1)
			}, {
				field : 'orderTypeName',
				width : '100',
				colKey : 'c.oms.order.type',
				cellClass : 'alignC'
			// ,select : 'ORDER_TYPE_CD'
			// ,validators : {
			// required : true
			// }
			}, {
				field : 'siteName',
				width : '100',
				colKey : 'c.oms.order.site',
				cellClass : 'alignC'
			}, {
				field : 'name1',
				width : '120',
				colKey : 'c.oms.order.name',
				cellTemplate : templateService.member('row.entity.memberNo', 'row.entity.memberId')
			}, {
				field : 'phone1',
				width : '120',
				colKey : 'c.oms.order.tel',
				cellClass : 'alignC'
			}, {
				field : 'phone2',
				width : '120',
				colKey : 'c.oms.order.mobile',
				cellClass : 'alignC'
			}, {
				field : 'receiver',
				width : '100',
				colKey : 'c.oms.delivery.name',
				cellClass : 'alignC'
			// cellTemplate : bbb
			}, {
				field : 'receiverMobile',
				width : '120',
				colKey : 'c.oms.delivery.mobile'
			}, {
				field : 'orderStateName',
				width : '100',
				colKey : 'c.oms.order.status',
				cellClass : 'alignC'
			// ,select : 'ORDER_STATE_CD'
			}, {
				field : 'orderDeliveryStateName',
				width : '100',
				colKey : 'c.oms.delivery.status',
				cellClass : 'alignC'
			// ,select : 'ORDER_DELIVERY_STATE_CD'
			}, {
				field : 'orderAmt',
				width : '100',
				colKey : 'c.oms.order.amount',
				cellClass : 'alignR',
				cellFilter : 'number'
			}, {
				field : 'dcAmt',
				width : '100',
				colKey : 'c.oms.order.discount',
				cellClass : 'alignR',
				cellFilter : 'number'
			}, {
				field : 'deliveryAmt',
				width : '100',
				colKey : 'c.oms.delivery.amount',
				cellClass : 'alignR',
				cellFilter : 'number'
			}, {
				field : 'wrappingAmt',
				width : '100',
				colKey : 'c.oms.delivery.wrapping',
				cellClass : 'alignR',
				cellFilter : 'number'
			}, {
				field : 'paymentAmt',
				width : '100',
				colKey : 'c.oms.payment.amount',
				cellClass : 'alignR',
				cellFilter : 'number'
			}, {
				field : 'orderQty',
				width : '80',
				colKey : 'c.oms.order.qty',
				cellClass : 'alignC'
			}, {
				field : 'outQty',
				width : '80',
				colKey : 'c.oms.order.out_qty',
				cellClass : 'alignC'
			}, {
				field : 'cancelQty',
				width : '80',
				colKey : 'c.oms.order.cancel_qty',
				cellClass : 'alignC'
			}, {
				field : 'returnQty',
				width : '80',
				colKey : 'c.oms.order.return_qty',
				cellClass : 'alignC'
			}, {
				field : 'exchangeQty',
				width : '80',
				colKey : 'c.oms.order.exchange_qty',
				cellClass : 'alignC'
			}, {
				field : 'deviceTypeName',
				width : '100',
				colKey : 'c.oms.order.device',
				cellClass : 'alignC'
			// ,select : 'DEVICE_TYPE_CD'
			}, {
				field : 'siteOrderId',
				width : '100',
				colKey : 'c.oms.order.site_order_id',
				cellClass : 'alignC'
			}
	];

	var gridParam = {
		scope : $scope,
		gridName : 'grid_order',
		url : '/api/oms/order',
		searchKey : 'search',
		columnDefs : columnDefs,
		showGroupPanel : true,
		gridOptions : {
			checkBoxEnable : false
		},
		callbackFn : function() { // optional
			console.log('####### callbackFn ######');
		}
	};
	var myGrid = new gridService.NgGrid(gridParam);

	$window.$scope = $scope; // 현재scope 를 자식에 전달하기 위해
	angular.element(document).ready(function() {
		$scope.func = commonfunction;
		commonService.init_search($scope, 'search');
		logisticsService.getAllSiteList(function(response) {
			$scope.siteList = response;
		});
	});
	$scope.siteList = [];
	$scope.search = {
		searchId : 'oms.order.select.boList',
		reset : function() {
			commonService.reset_search($scope, 'search');
			angular.element('.day_group').find('button').eq(0).click();
		},
		init : function() {
		}
	}
	$scope.list = {
		search : function() {
			myGrid.loadGridData();
		}
	}
});
