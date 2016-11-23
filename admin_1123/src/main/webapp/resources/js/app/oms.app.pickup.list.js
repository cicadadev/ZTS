// message init
Constants.message_keys = [];

var pickupListApp = angular.module('pickupListApp', [
		'ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule'
]);

pickupListApp.controller('listCtrl', function($window, $scope, $filter, templateService, commonfunction, gridService, commonService, pickupService) {
	var columnDefs = [
			{
				field : 'pickupId',
				width : '150',
				colKey : 'c.oms.pickup.id',
				cellClass : 'alignC',
			}, {
				field : 'pickupReqDt',
				width : '150',
				colKey : 'c.oms.pickup.datetime_request',
				cellClass : 'alignC',
			}, {
				field : 'omsPickup.name1',
				width : '150',
				colKey : 'c.oms.pickup.name',
				cellClass : 'alignC',
				cellTemplate : templateService.member('row.entity.omsPickup.memberNo', 'row.entity.omsPickup.memberId')
			}, {
				field : 'omsPickup.phone1',
				width : '120',
				colKey : 'c.oms.pickup.tel',
				cellClass : 'alignC'
			}, {
				field : 'omsPickup.phone2',
				width : '120',
				colKey : 'c.oms.pickup.mobile',
				cellClass : 'alignC'
			}, {
				field : 'pickupProductStateName',
				width : '90',
				colKey : 'c.oms.pickup.status',
				cellClass : 'alignC'
			}, {
				field : 'ccsOffshop.offshopId',
				width : '95',
				colKey : 'c.oms.pickup.offshop_id',
				cellClass : 'alignC',
//				linkFunction : 'func.popup.offshop'
			}, {
				field : 'ccsOffshop.name',
				width : '120',
				colKey : 'c.oms.pickup.offshop_name'
			}, {
				field : 'pickupReserveDt',
				width : '130',
				colKey : 'c.oms.pickup.datetime_reserve',
				// cellClass : 'alignC',
				cellClass : templateService.dateClass,
				headerCellClass : 'edit_column',
				type : 'date',
				maxDate : '{{row.entity.pickupMaxDt}}',
				minDate : '{{row.entity.pickupMinDt}}'
			}, {
				field : 'pickupDeliveryDt',
				width : '150',
				colKey : 'c.oms.pickup.datetime_complete',
				cellClass : 'alignC'
			}, {
				field : 'productId',
				width : '80',
				colKey : 'c.oms.pickupproduct.product_id',
				cellClass : 'alignC',
				cellTemplate : templateService.product
			}, {
				field : 'productName',
				width : '200',
				colKey : 'c.oms.pickupproduct.product_name'
			}, {
				field : 'saleproductName',
				width : '110',
				colKey : 'c.oms.pickupproduct.saleproduct_name'
			}, {
				field : 'orderQty',
				width : '80',
				colKey : 'c.oms.pickupproduct.qty',
				cellClass : 'alignC'
			}, {
				field : 'orderAmt',
				width : '90',
				colKey : 'c.oms.pickupproduct.amount',
				cellClass : 'alignR',
				cellFilter : 'number'
			}, {
				field : 'pickupCancelDt',
				width : '150',
				colKey : 'c.oms.pickupproduct.datetime_cancel',
				cellClass : 'alignC'
			}, {
				field : 'updId',
				width : '90',
				colKey : 'c.oms.pickupproduct.name_cancel',
				cellClass : 'alignC',
				cellTemplate : templateService.updCancel
			}
	];

	var gridParam = {
		scope : $scope,
		gridName : 'grid_pickup',
		url : '/api/oms/pickup',
		searchKey : 'search',
		columnDefs : columnDefs,
		showGroupPanel : true,
		gridOptions : {
			checkboxCellTemplate : false,
			enableRowSelection : true,
			multiSelect : true,
			// checkMultiSelect : false,
			displaySelectionCheckbox : true,
			// selectedItems: $scope.mySelections,
			noUnselect : false,
			isRowSelectable : function(row) {
				var isRowSelectable = true;
				if (row.entity.pickupProductStateCd != 'PICKUP_PRODUCT_STATE_CD.REQ' && row.entity.pickupProductStateCd != 'PICKUP_PRODUCT_STATE_CD.READY') {
					isRowSelectable = false;
				}
				if (isRowSelectable) {
					row.cursor = 'pointer';
				}
				return isRowSelectable;
			},
			rowSelectionFn : function(row) {
				// var entities = this.data;
				// for (var i = 0; i < entities.length; i++) {
				// entities[i].checked = false;
				// }
				row.entity.checked = row.isSelected;
			}
		}
	};
	var myGrid = new gridService.NgGrid(gridParam);

	$window.$scope = $scope;
	angular.element(document).ready(function() {
		$scope.func = commonfunction;
		commonService.init_search($scope, 'search');
	});
	$scope.search = {
		searchId : 'oms.pickup.selectList',
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
		},
		save : function() {
			/*
			 * var gridApi = $scope.grid_pickup.gridApi; if (gridApi && gridApi.rowEdit) { var dirtyRows = gridApi.rowEdit.getDirtyRows();
			 * 
			 * var cancelPickup = true; for (var i = 0; i < dirtyRows.length; i++) { var entity = dirtyRows[i].entity; if (entity.pickupProductStateCd != 'PICKUP_PRODUCT_STATE_CD.REQ' && entity.pickupProductStateCd != 'PICKUP_PRODUCT_STATE_CD.READY') { entity.checked = false;
			 * dirtyRows[i].isSelected = false cancelPickup = false; } } if (!cancelPickup) { var rows = dirtyRows.map(function(gridRow) { return gridRow.entity; }); alert("변경 불가능한 주문이 있습니다."); gridApi.rowEdit.setRowsClean(rows); return false; } }
			 * myGrid.saveGridData();
			 */
			var selectedItems = myGrid.getSelectedRows();
			if (selectedItems.length == 0) {
				alert("픽업예정일을 변경할 신청목록을  선택해 주십시오.");
				return false;
			} else {
				var gridApi = $scope.grid_pickup.gridApi;
				var dirtyRows = gridApi.rowEdit.getDirtyRows();
				var rows = dirtyRows.map(function(gridRow) {
					return gridRow.entity;
				});
				// gridApi.rowEdit.setRowsClean(rows);

				if (rows == 0) {
					alert("픽업예정일을 변경한 신청목록이 없습니다.");
					return false;
				}
			}
			pickupService.update(selectedItems, function(response) {
				if (response.content > 0) {
					$scope.list.search();
					alert('픽업예정일 변경이 완료되었습니다.');
				} else {
					alert('픽업예정일 변경 중 에러가 발생 하였습니다.');
				}
			});
		},
		cancel : function() {
			var selectedItems = myGrid.getSelectedRows();
			if (selectedItems.length == 0) {
				alert("취소할 주문 데이터를 선택해 주십시오.");
				return false;
			}
			pickupService.cancel(selectedItems, function(response) {
				if (response.success) {
					// myGrid.loadGridData();
					$scope.list.search();
					alert('매장픽업 주문이 취소 되었습니다.');
				} else if (!response.success) {
					alert(response.resultMessage);
				} else {
					alert('매장픽업 주문 취소 중 에러가 발생 하였습니다.');
				}
			});
		}
	}
	$scope.checkBoxAll = function(grid, evt) {
		var allCheck = $(evt.target).is(":checked");
		var rows = grid.rows;
		for (var i = 0; i < rows.length; i++) {
			var row = rows[i];
			if (grid.options.isRowSelectable(row)) {
				row.entity.checked = allCheck;
				row.isSelected = allCheck;
			}
		}
	}
});
