// message init
Constants.message_keys = [];

var claimListApp = angular.module('claimListApp', [
		'ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule'
]);

claimListApp.controller('listCtrl', function($window, $scope, $filter, templateService, commonfunction, gridService, commonService, claimService) {
	var columnDefs = [
			{
				field : 'claimNo',
				width : '180',
				colKey : 'c.oms.claim.id',
				cellClass : 'alignC',
				cellTemplate : templateService.claim
			}, {
				field : 'orderId',
				width : '180',
				colKey : 'c.oms.order.id',
				cellClass : 'alignC',
				linkFunction : 'func.popup.order'
			}, {
				field : 'reqDt',
				width : '180',
				colKey : 'c.oms.claim.datetime_request',
				cellClass : 'alignC',
//				cellTemplate : templateService.claimDate('REQ')
			}, {
				field : 'acceptDt',
				width : '180',
				colKey : 'c.oms.claim.datetime_accept',
				cellClass : 'alignC',
//				cellTemplate : templateService.claimDate('ACCEPT')
			}, {
				field : 'claimTypeName',
				 width : '120',
				colKey : 'c.oms.claim.type',
				cellClass : 'alignC'
			}, {
				field : 'claimStateName',
				 width : '120',
				colKey : 'c.oms.claim.status',
				cellClass : 'alignC'
			}, {
				field : 'returnStatus',
				width : '120',
				colKey : 'c.oms.claim.in_status',
				cellClass : 'alignC'
			}, {
				field : 'omsOrder.name1',
				 width : '150',
				colKey : 'c.oms.order.name',
				cellClass : 'alignC',
				cellTemplate : templateService.member('row.entity.omsOrder.memberNo', 'row.entity.omsOrder.memberId')
			}, {
				field : 'completeDt',
				width : '180',
				colKey : 'c.oms.claim.datetime_complete',
				cellClass : 'alignC',
//				cellTemplate : templateService.claimDate('COMPLETE')
				
			}, {
				field : 'insId',
				width : '100',
				colKey : 'c.grid.column.insId',
				cellClass : 'alignC'
			}, {
				field : 'insDt',
				width : '180',
				colKey : 'c.grid.column.insDt ',
				cellClass : 'alignC'
			}, {
				field : 'updId',
				width : '100',
				colKey : 'c.grid.column.updId',
				cellClass : 'alignC'
			}, {
				field : 'updDt',
				width : '180',
				colKey : 'c.grid.column.updDt',
				cellClass : 'alignC'
			}
	];

	var gridParam = {
		scope : $scope,
		gridName : 'grid_claim',
		url : '/api/oms/claim',
		searchKey : 'search',
		columnDefs : columnDefs,
		showGroupPanel : true,
		gridOptions : {
			checkBoxEnable : false,
//			enableRowSelection : true,
//			multiSelect : true,
//			rowSelectionFn : function(row) {
//			}
		},
		callbackFn : function() { // optional
			console.log('####### callbackFn ######');
		}
	};
	var myGrid = new gridService.NgGrid(gridParam);

	$window.$scope = $scope;
	angular.element(document).ready(function() {
		$scope.func = commonfunction;
		commonService.init_search($scope, 'search');
	});
	
	$scope.search = {
		searchId : 'oms.claim.select.list',
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
