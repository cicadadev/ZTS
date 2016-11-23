// message init
Constants.message_keys = [
		"common.label.alert.save", "common.label.confirm.save"
];

var claimEtcApp = angular.module('claimEtcApp', [
		'ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule'
]);

/** 1. 옵션변경 */
claimEtcApp.controller('optionCtrl', function($window, $scope, templateService, commonfunction, gridService, orderService) {
	var columnDefs = [
			{
				field : 'saleproductId',
				width : '80',
				colKey : 'c.oms.order.saleproduct_id',
				cellClass : 'alignC'
			}, {
				field : 'name',
				// width : '200',
				colKey : 'c.oms.order.saleproduct_name'
			}, {
				field : 'saleproductStateName',
				width : '80',
				colKey : 'c.oms.order.saleproduct_status',
				cellClass : 'alignC'
			}, {
				field : 'realStockQty',
				width : '80',
				colKey : 'c.oms.order.saleproduct_stock',
				cellClass : 'alignC'
			}, {
				field : 'addSalePrice',
				width : '100',
				colKey : 'c.oms.order.saleproduct_addprice',
				cellClass : 'alignR',
				cellFilter : 'number',
				cellTemplate : templateService.addPrice
			}
	];

	var gridParam = {
		scope : $scope,
		gridName : 'grid_option',
		url : '/api/pms/product/stock',
		searchKey : 'search',
		columnDefs : columnDefs,
		showGroupPanel : true,
		gridOptions : {
			enableFullRowSelection : true,
			enableSorting : false,
			enableVerticalScrollbar : 0,
			enableHorizontalScrollbar : 0,
			multiSelect : false,

			enableRowSelection : true,
			checkMultiSelect : false,
			pagination : false,

			isRowSelectable : function(row) {
				var isRowSelectable = true;
				var orderQty = pSelectedItem.orderQty - pSelectedItem.cancelQty - pSelectedItem.returnQty;
				if (orderQty - row.entity.realStockQty > 0) {
					isRowSelectable = false;
				}
				if (row.entity.saleproductStateCd != 'SALEPRODUCT_STATE_CD.SALE') {
					isRowSelectable = false;
				}
				if (pSelectedItem.addSalePrice != row.entity.addSalePrice) {
					isRowSelectable = false;
				}
//				if (pSelectedItem.saleproductId == row.entity.saleproductId) {
//					isRowSelectable = false;
//				}
				if (isRowSelectable) {
					row.cursor = 'pointer';
				}
				return isRowSelectable;
			},
			rowSelectionFn : function(row) {
				var entities = this.data;
				for (var i = 0; i < entities.length; i++) {
					entities[i].checked = false;
				}
				row.entity.checked = row.isSelected;
			}
		},
		callbackFn : function() { // optional
			console.log('####### callbackFn ######');
		}
	};
	var myGrid = new gridService.NgGrid(gridParam);

	var pScope = $window.opener.$scope;// 부모창의 scope
	var pSelectedItem = pScope.selectedItems;
	var onGrid = false;
	if (!common.isEmptyObject(pScope['grid_claim'])) {
		onGrid = true;
		pSelectedItem = pScope.selectedItems;
	} else {
		pSelectedItem = pScope.selectedItems[0];
	}
	angular.element(document).ready(function() {
		$scope.func = commonfunction;
	});
	$scope.search = {};
	$scope.orgAddSalePrice = pSelectedItem.addSalePrice;

	this.init = function() {
		$scope.search.productIds = '\'' + pSelectedItem.productId + '\'';
		myGrid.loadGridData();
	}
	this.save = function() {
		var selectedItems = myGrid.getSelectedRows()[0];

		if (!common.isEmptyObject(selectedItems)) {
			if (onGrid) {
//				pScope.setOption(selectedItems.saleproductId, selectedItems.name);
//				$window.opener.$scope[$window.opener.$scope.searchParam.callback](selectedItems);
				$window.opener.$scope.searchParam.callback(selectedItems);
				$window.close();
			} else {
				var omsOrderProduct = {
					productId : pSelectedItem.productId,
					orderId : pSelectedItem.orderId,
					orderProductNo : pSelectedItem.orderProductNo,
					orderQty : pSelectedItem.orderQty,
					cancelQty : pSelectedItem.cancelQty,
					returnQty : pSelectedItem.returnQty
				};
				
				var orderQty = omsOrderProduct.orderQty - omsOrderProduct.cancelQty - omsOrderProduct.returnQty;
				if (orderQty > selectedItems.realStockQty) {
					alert('선택한 단품은 재고수량이 부족합니다.');
					return false;
				}
				if (selectedItems.saleproductStateCd != 'SALEPRODUCT_STATE_CD.SALE') {
					alert('선택한 단품은 판매중이 아닙니다.');
					return false;
				}
				if (window.confirm('주문단품 정보를 변경 하시겠습니까?')) {
					// omsOrderProduct.addSalePrice = selectedItems.addSalePrice;
					omsOrderProduct.businessSaleproductId = selectedItems.businessSaleproductId;
					omsOrderProduct.erpColorId = selectedItems.erpColorId;
					omsOrderProduct.erpSaleproductId = selectedItems.erpSaleproductId;
					omsOrderProduct.erpSizeId = selectedItems.erpSizeId;
					omsOrderProduct.locationId = selectedItems.locationId;
					omsOrderProduct.saleproductId = selectedItems.saleproductId;
					omsOrderProduct.saleproductName = selectedItems.name;
					omsOrderProduct.warehouseId = selectedItems.warehouseId;

					orderService.update(omsOrderProduct, function(response) {
						if (response.success) {
							alert('주문단품 정보가 성공적으로 변경 되었습니다.');
							pScope.ctrl.init();
							$window.close();
						} else if (!response.success) {
							alert(response.resultMessage);
						} else {
							alert('주문상품 옵션 변경 중 에러가 발생 하였습니다.');
						}
					});
				}
			}
		} else {
			alert('옵션변경 할 단품을 선택해 주십시오.');
			return false;
		}
	}
});

/** 2. 배송지 변경 */
claimEtcApp.controller('deliveryCtrl', function($window, $scope, commonfunction, gridService, commonService, deliveryService) {
	var columnDefs = [
			{
				field : 'addressNo',
				width : '80',
				colKey : 'c.mms.address.seq',
				cellClass : 'alignC'
			}, {
				field : 'name',
				width : '100',
				colKey : 'c.mms.address.name',
				cellClass : 'alignC'
			}, {
				field : 'deliveryName1',
				width : '90',
				colKey : 'c.mms.address.delivery_name',
				cellClass : 'alignC'
			}, {
				field : 'phone2',
				width : '120',
				colKey : 'c.mms.address.mobile',
				cellClass : 'alignC'
			}, {
				field : 'phone1',
				width : '120',
				colKey : 'c.mms.address.tel',
				cellClass : 'alignC'
			}, {
				field : 'zipCd',
				width : '80',
				colKey : 'c.mms.address.zipcd',
				cellClass : 'alignC'
			}, {
				field : 'address1',
				// width : '200',
				colKey : 'c.mms.address.address1'
			}, {
				field : 'address2',
				// width : '200',
				colKey : 'c.mms.address.address2'
			}
	];

	var gridParam = {
		scope : $scope,
		gridName : 'grid_address',
		url : '/api/mms/member/address',
		searchKey : 'search',
		columnDefs : columnDefs,
		showGroupPanel : true,
		gridOptions : {
			// checkBoxEnable : false,
			checkMultiSelect : false,
			enableRowSelection : true,
			enableSelectAll : false,
			enableVerticalScrollbar : 0,
			multiSelect : false,
			// pagination : false,
			isRowSelectable : function(row) {
				var isRowSelectable = this.enableRowSelection;
				if (isRowSelectable) {
					row.cursor = 'pointer';
				}
				return isRowSelectable;
			},
			rowSelectionFn : function(row) {
				var entities = this.data;
				for (var i = 0; i < entities.length; i++) {
					entities[i].checked = false;
				}
				row.entity.checked = row.isSelected;
			}

		},
		callbackFn : function() { // optional
			console.log('####### callbackFn ######');
		}
	};
	var myGrid = new gridService.NgGrid(gridParam);
	var pScope = $window.opener.$scope;// 부모창의 scope
	$window.$scope = $scope; // 현재scope 를 자식에 전달하기 위해
	angular.element(document).ready(function() {
		$scope.func = commonfunction;
	});
	
	$scope.search = {
		orderId : pScope.orderId,
		deliveryAddressNo : pScope.deliveryAddressNo,
		memberNo : pScope.memberNo
	}
	
	this.init = function() {
		// 1. address list
		myGrid.loadGridData();

		// 2. order delivery
		deliveryService.selectOne($scope.search, function(response) {
			$scope.omsDeliveryaddress = response;
		});
		
		// 3. delivery message
		commonService.getCodeList({cdGroupCd : 'DELIVERY_MESSAGE_CD'}).then(function(data) {
			$scope.deliveryMessageCds = data;
		});
		
	}
	this.select = function() {
		var selectedItems = myGrid.getSelectedRows()[0];
		if (!selectedItems) {
			alert('배송지 목록 중 하나의 배송지를 선택해 주십시오.');
			return false;
		} else {
			$scope.omsDeliveryaddress.name1 = selectedItems.deliveryName1;
			$scope.omsDeliveryaddress.phone1 = selectedItems.phone1;
			$scope.omsDeliveryaddress.phone2 = selectedItems.phone2;
			$scope.omsDeliveryaddress.zipCd = selectedItems.zipCd;
			$scope.omsDeliveryaddress.address1 = selectedItems.address1;
			$scope.omsDeliveryaddress.address2 = selectedItems.address2;
		}
	}
	this.setMessage = function(noteType) {
		if ($('#noteType option:selected').val() != '') {
			$scope.omsDeliveryaddress.note = $('#noteType option:selected').text();
		}
	}
	this.save = function() {
		// 폼 체크
		if (!commonService.checkForm($scope.deliveryForm)) {
			return false;
		}
		// if (window.confirm(pScope.MESSAGES['common.label.confirm.save'])) {
		if (window.confirm('배송정보를 수정하시겠습니까?')) {
			if (!common.isEmpty($scope.omsDeliveryaddress.deliveryAddressNo)) {
				deliveryService.update($scope.omsDeliveryaddress, function(response) {
					if (response.content == '1') {
						alert('배송정보가 성공적으로 변경 되었습니다.');
						// 부모창 배송정보 갱신
						for (var i = 0; i < pScope.omsDeliveryAddressList.length; i++) {
							var oldAddress = pScope.omsDeliveryAddressList[i];
							var newAddress = $scope.omsDeliveryaddress;
							if (oldAddress.deliveryAddressNo == newAddress.deliveryAddressNo) {
								// address = newAddress;
								oldAddress.name1 = newAddress.name1;
								oldAddress.phone1 = newAddress.phone1;
								oldAddress.phone2 = newAddress.phone2;
								oldAddress.zipCd = newAddress.zipCd;
								oldAddress.address1 = newAddress.address1;
								oldAddress.address2 = newAddress.address2;
								oldAddress.note = newAddress.note;
								pScope.$apply();
							}
						}

						$window.close();
					} else {
						alert('배송정보 변경 중 에러가 발생 하였습니다.');
					}
				});
			} else {
				alert('배송번호가 지정되지 않았습니다.');
				return false;
			}
		}
	}
});
