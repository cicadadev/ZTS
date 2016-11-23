// message init
Constants.message_keys = [
		"common.label.alert.save", "common.label.confirm.save"
];

var orderEtcApp = angular.module('orderEtcApp', [
		'ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule'
]);

/** 1. 메모 */
orderEtcApp.controller('memoCtrl', function($window, $scope, gridService, commonService, orderService) {

	var pScope = $window.opener.$scope;// 부모창의 scope
	
	$scope.omsOrderMemo = {};
	$scope.memoList = [];
	$scope.orderId = pScope.orderId;

	this.insert = function() {
		// 폼 체크
		if (!commonService.checkForm($scope.memoForm)) {
			return;
		}
		orderService.insertMemo($scope.omsOrderMemo, function(response) {
			if (response.content == '1') {
				// $scope.$apply();
				alert('[주문메모]가 정상적으로 등록 되었습니다.');
				// $scope.detail();
				pScope.list.search();
				$window.close();
			} else {
				alert('[주문메모] 등록시 에러가 발생 하였습니다.');
			}
		});
	};
	this.init = function() {
		$scope.omsOrderMemo.orderId = pScope.orderId;
		$scope.omsOrderMemo.orderProductNo = pScope.orderProductNo;
		orderService.selectMemo($scope.omsOrderMemo, function(response) {
			$scope.memoList = response;
		});
	};

});

/** 2. 쿠폰목록 */
orderEtcApp.controller('couponCtrl', function($window, $scope, templateService, commonfunction, gridService, orderService) {
	var columnDefs = [ 
			{ field : 'couponId', 		colKey : 'c.oms.coupon.id', 		width : '80', cellClass : 'alignC', cellTemplate : templateService.coupon }, 
			{ field : 'name', 			colKey : 'c.oms.coupon.name' 		},
			{ field : 'couponTypeName', colKey : 'c.oms.coupon.type', 		width : '190', cellClass : 'alignC', cellTemplate : templateService.dctype }, 
			{ field : 'singleApplyYn', 	colKey : 'c.oms.coupon.single_yn', 	width : '80', cellClass : 'alignR', cellFilter : 'number' }, 
			{ field : 'couponDcAmt', 	colKey : 'c.oms.coupon.discount', 	width : '70', cellClass : 'alignC', cellFilter : 'number' }, 
			{ field : 'period', 		colKey : 'c.oms.coupon.period', 	width : '180', cellClass : 'alignC' }
	];
	var gridParam = {
		scope : $scope,
		gridName : 'grid_coupon',
		url : '/api/oms/order',
		searchKey : 'search',
		columnDefs : columnDefs,
		showGroupPanel : true,
		gridOptions : {
			checkBoxEnable : false,
			enableSorting : false,
			enableVerticalScrollbar : 0,
			enableHorizontalScrollbar : 0,
			pagination : false,
		}
	};
	var myGrid = new gridService.NgGrid(gridParam);

	var pScope = $window.opener.$scope;// 부모창의 scope
	$window.$scope = $scope; // 현재scope 를 자식에 전달하기 위해
	angular.element(document).ready(function() {
		$scope.func = commonfunction;
	});

	$scope.search = {};
	$scope.orderId = pScope.orderId;

	this.init = function() {
		// console.log('pScope.searchCoupons 1 : ',pScope.searchCoupons);
		// console.log('pScope.searchCoupons 2 : ',JSON.stringify(pScope.searchCoupons));
		$scope.search['orderId'] = pScope.orderId;
		$scope.search['couponTypeCds'] = pScope.couponTypeCds;
//		$scope.search['couponStateCd'] = 'COUPON_STATE_CD.APPLY'; // 취소된 것도 보이게 수정
		$scope.search['searchId'] = 'oms.order.select.couponList';
		// $scope.search['searchCoupons'] = pScope.searchCoupons;
		if (!common.isEmptyObject(pScope.searchCoupons)) {
			$scope.search.searchCoupons = [].concat(pScope.searchCoupons);
		}

		myGrid.loadGridData();
	}
});

/** 3. 배송추적 */
orderEtcApp.controller('deliveryTrackingCtrl', function($window, $scope, $timeout, orderService, commonfunction, gridService, deliveryService) {
	var columnDefs = [
//          { field : 'omsDeliverytracking.deliveryServiceTime', 	width : '150', 	colKey : 'c.oms.delivery.date', 	cellClass : 'alignC',	cellTemplate : templateService.coupon }, 
//          { field : 'omsDeliverytracking.deliveryLocation', 		width : '150',	colKey : 'c.oms.delivery.location',	cellClass : 'alignC',},
//          { field : 'omsDeliverytracking.senderName', 			width : '150', 	colKey : 'c.oms.delivery.status', 	cellClass : 'alignC' },
//          { field : 'omsDeliverytracking.deliveryStepName', 		width : '150', 	colKey : 'c.oms.delivery.status', 	cellClass : 'alignR',	cellFilter : 'number', cellTemplate : templateService.dctype },
//          { field : 'omsDeliverytracking.deliverymanMobileNo', 	width : '150', 	colKey : 'c.oms.delivery.phone2', 	cellClass : 'alignR',	cellFilter : 'number', cellTemplate : templateService.dctype },
			{ field : 'deliveryServiceTime', 	/*width : '200',*/ 	displayName : '날짜', 	cellClass : 'alignC' }, 
			{ field : 'deliveryLocation', 		width : '180',	displayName : '위치',		cellClass : 'alignC' },
			{ field : 'senderName', 			width : '150', 	displayName : '담당자', 	cellClass : 'alignC' },
			{ field : 'deliveryStepName', 		width : '150', 	displayName : '배송상태', 	cellClass : 'alignC' },
			{ field : 'deliverymanMobileNo', 	width : '180', 	displayName : '전화번호', 	cellClass : 'alignC' },
	];
//	var gridParam = {
//			scope : $scope,
//			gridName : 'grid_tracking',
//			url : '/api/oms/delivery/tracking',
//			searchKey : 'search',
//			columnDefs : columnDefs,
//			showGroupPanel : true,
//			gridOptions : {
//				checkBoxEnable : false,
//				enableSorting : false,
//				enableVerticalScrollbar : 0,
//				enableHorizontalScrollbar : 0,
//				pagination : false,
//			}
//	};
//	var myGrid = new gridService.NgGrid(gridParam);
//	
	$scope.setLogistics = function(logisticsList) {
		for (var i = 0; i < logisticsList.length; i++) {
			var logistics = logisticsList[i];
			
			logistics.gridOptions = {
				data : logistics.omsDeliverytrackings,
				columnDefs : columnDefs,
				isRowSelectable : function(row) {
					return commonfunction.isRowSelectable('DETAIL', row, 0);
				},
				enableColumnMenus : false,
				enableFullRowSelection : true,
				enableSorting : false,
				enableVerticalScrollbar : 0,
				showTreeRowHeader : false,
				showTreeExpandNoChildren : false,
				enableExpandableRowHeader : false,
				
				enableGridMenu : false,
			}
		}
		$scope.logisticsList = logisticsList;
		$scope.logistics = logisticsList[0];
	}	
	
	var pScope = $window.opener.$scope;// 부모창의 scope
	$window.$scope = $scope; // 현재scope 를 자식에 전달하기 위해
	angular.element(document).ready(function() {
		$scope.func = commonfunction;
	});
	
	$scope.search = {};
	$scope.omsOrderproducts = [];
	$scope.orderId = pScope.orderId;
	
	this.init = function() {
		$scope.search['orderId'] = pScope.orderId;
		$scope.search['orderProductNo'] = pScope.orderProductNo;
		$scope.search['searchId'] = 'oms.order.select.products';
		
		orderService.selectList($scope.search, function(response1) {
			$scope.omsOrderproducts = response1;
			
			$timeout(function() {
				$scope.search['searchId'] = 'oms.delivery.tracking';
				deliveryService.selectList($scope.search, function(response2) {
					if (!common.isEmptyObject(response2)) {
						$scope.setLogistics(response2);
					}
				});
			}, 0);
		});
		
		// myGrid.loadGridData();
	}
});