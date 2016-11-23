// message init
Constants.message_keys = [];

var regularListApp = angular.module('regularListApp', [
		'ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule', 'ui.grid.treeView'
]);

regularListApp.controller('listCtrl', function($window, $scope, $filter, templateService, commonfunction, gridService, commonService, regularService) {
	var columnDefs = [
			{
				field : 'regularDeliveryId',
				width : '130',
				colKey : 'c.oms.regular.id',
				cellClass : 'alignC',
			}, {
				field : 'omsRegulardelivery.name1',
				width : '150',
				colKey : 'c.oms.regular.name',
				cellClass : 'alignC',
				cellTemplate : templateService.member('row.entity.memberNo', 'row.entity.memberId')
			}, {
				field : 'omsRegulardelivery.phone1',
				width : '120',
				colKey : 'c.oms.regular.tel',
				cellClass : 'alignC'
			}, {
				field : 'omsRegulardelivery.phone2',
				width : '120',
				colKey : 'c.oms.regular.mobile',
				cellClass : 'alignC'
			}, {
				field : 'deliveryPeriodName',
				width : '90',
				colKey : 'c.oms.regular.period',
				cellClass : 'alignC'
			}, {
				field : 'deliveryDay',
				width : '95',
				colKey : 'c.oms.regular.day',
				cellClass : 'alignC'
			}, {
				field : 'omsRegulardeliveryschedules[0].regularDeliveryDt',
				width : '150',
				colKey : 'c.oms.regular.delivery_date',
				cellClass : templateService.dateClass,
				headerCellClass : 'edit_column',
				// type : 'date'
				cellTemplate : templateService.date
			}, {
				field : 'deliverySchedule',
				width : '100',
				colKey : 'c.oms.regular.delivery_cnt',
				cellClass : 'alignC',
				cellTemplate : ''
			}, {
				field : 'address',
				width : '350',
				colKey : 'c.oms.regular.delivery_address',
				headerCellClass : 'edit_column',
				cellTemplate : templateService.address
			}, {
				field : 'omsRegulardelivery.regularPaymentBusinessNm',
				width : '100',
				colKey : 'c.oms.regular.card_name',
				cellClass : 'alignC'
			}, {
				field : 'deliveryProductTypeName',
				width : '125',
				colKey : 'c.oms.regularproduct.product_type',
//				cellClass : 'alignC',
				cellTemplate : templateService.tree
			}, {
				field : 'pmsProduct.productId',
				width : '80',
				colKey : 'c.oms.regularproduct.product_id',
				cellClass : 'alignC',
				cellTemplate : templateService.product
			}, {
				field : 'pmsProduct.name',
				width : '200',
				colKey : 'c.oms.regularproduct.product_name'
			}, {
				field : 'pmsSaleproduct.name',
				width : '110',
				colKey : 'c.oms.regularproduct.saleproduct_name'
			}, {
				field : 'orderQty',
				width : '80',
				colKey : 'c.oms.regularproduct.qty',
				cellClass : 'alignC'
			}, {
				field : 'regularDeliveryPrice',
				width : '90',
				colKey : 'c.oms.regularproduct.amount',
				cellClass : 'alignR',
				cellFilter : 'number'
			}, {
				field : 'memo',
				width : '90',
				colKey : 'c.oms.regularproduct.memo',
				cellClass : 'alignC',
				cellTemplate : templateService.memo('row.entity.regularDeliveryId', 'row.entity.deliveryProductNo')
			}
	];

	var gridParam = {
		scope : $scope,
		gridName : 'grid_regular',
		url : '/api/oms/regular',
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
//			treeIndent : 30,
			isRowSelectable : function(row) {
				var isRowSelectable = true;
				if (row.entity.omsRegulardeliveryschedules[0].regularDeliveryDt < row.entity.minDate) {
					isRowSelectable = false;
				}
				if (isRowSelectable) {
					row.cursor = 'pointer';
				}
				return isRowSelectable;
			},
			rowSelectionFn : function(row) {
				row.entity.checked = row.isSelected;
			}
		}
	};
	var myGrid = new gridService.NgGrid(gridParam);

	$window.$scope = $scope; // 현재scope 를 자식에 전달하기 위해
	angular.element(document).ready(function() {
		$scope.func = commonfunction;
		commonService.init_search($scope, 'search');
	});
	
	$scope.search = {
		searchId : 'oms.regular.select.productList',
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
			var selectedRows = myGrid.getSelectedRows();
			if (selectedRows.length == 0) {
				alert("변경할 정기배송정보를  선택해 주십시오.");
				return false;
			} else {
				var gridApi = $scope.grid_regular.gridApi;
				var dirtyRows = gridApi.rowEdit.getDirtyRows();
				var rows = dirtyRows.map(function(gridRow) {
					return gridRow.entity;
				});
				// gridApi.rowEdit.setRowsClean(rows);

				if (rows == 0) {
					alert("변경된 정기배송정보가 없습니다.");
					return false;
				}
			}
			regularService.update(selectedRows, function(response) {
				if (response.content > 0) {
					$scope.list.search();
					alert('정기배송정보 변경이 완료되었습니다.');
				} else {
					alert('정기배송정보 변경 중 에러가 발생 하였습니다.');
				}
			});
		},
		cancel : function() {
			var selectedRows = myGrid.getSelectedRows();
			if (selectedRows.length == 0) {
				alert("취소할 주문 데이터를 선택해 주십시오.");
				return false;
			}
			regularService.cancel(selectedRows, function(response) {
				if (response.content > 0) {
					// myGrid.loadGridData();
					$scope.list.search();
					alert('정기배송 주문이 취소 되었습니다.');
				} else {
					alert('정기배송 주문 취소시 에러가 발생 하였습니다.');
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
