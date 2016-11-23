// message init
Constants.message_keys = [
		"common.label.alert.save", "common.label.confirm.save"
];

var orderDetailApp = angular.module('orderDetailApp', [
		'ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule', 'ui.grid.treeView', 'ui.grid.pinning'
]);

orderDetailApp.controller('detailCtrl', function($window, $scope, $filter, $timeout, $templateCache, templateService, commonfunction, commonService, orderService, paymentService, deliveryService, claimService, uiGridSelectionService) {
	var columnDefs = [
			{
				field : 'orderProductTypeName',
				width : '125',
				displayName : '상품유형',
				// cellClass : 'alignC',
				cellTemplate : templateService.tree,
				pinnedLeft:true
			}, {
				field : 'orderProductStateName',
				width : '80',
				displayName : '배송상태',
				cellClass : 'alignC',
				pinnedLeft:true
			}, {
				field : 'productId',
				width : '80',
				displayName : '상품번호',
				cellClass : 'alignC',
				cellTemplate : templateService.product,
				pinnedLeft:true
			}, {
				field : 'productName',
				width : '200',
				displayName : '상품명',
				cellTemplate : templateService.productName
			}, {
				field : 'saleproductName',
				width : '110',
				displayName : '단품명',
			}, {
				field : 'businessName',
				width : '150',
				displayName : '공급업체',
				cellClass : 'alignC',
				cellTemplate : templateService.business
			}, {
				field : 'businessPhone',
				width : '120',
				displayName : '업체연락처',
				cellClass : 'alignC'
			}, {
				field : 'orderDeliveryTypeName',
				width : '75',
				displayName : '발송유형',
				cellClass : 'alignC'
			}, {
				field : 'orderQty',
				width : '75',
				displayName : '주문수량',
				cellClass : 'alignC'
			}, {
				field : 'outQty',
				width : '75',
				displayName : '출고수량',
				cellClass : 'alignC'
			}, {
				field : 'cancelQty',
				width : '75',
				displayName : '취소수량',
				cellClass : 'alignC'
			}, {
				field : 'returnQty',
				width : '75',
				displayName : '반품수량',
				cellClass : 'alignC'
			}, {
				field : 'dealTypeName',
				width : '100',
				displayName : '적용가격유형',
				cellClass : 'alignC'
			}, {
				field : 'totalSalePrice',
				width : '100',
				displayName : '개당판매가',
				cellClass : 'alignR',
				cellFilter : 'number'
			}, {
				field : 'dcAmt',
				width : '100',
				displayName : '전체할인금액',
				cellClass : 'alignR',
				cellFilter : 'number'
			}, {
				field : 'paymentAmt',
				width : '100',
				displayName : '전체판매가',
				cellClass : 'alignR',
				cellFilter : 'number'
			}, {
				field : 'deliveryFeeFreeYn',
				width : '100',
				displayName : '무료배송여부',
				cellClass : 'alignC'
			}, {
				field : 'wrapYn',
				width : '100',
				displayName : '선물포장여부',
				cellClass : 'alignC'
			}, {
				field : 'totalPoint',
				width : '90',
				displayName : '적립포인트',
				cellClass : 'alignR',
				cellFilter : 'number'
			}, {
				field : 'productCouponId',
				width : '110',
				displayName : '적용상품쿠폰',
				cellClass : 'alignC',
				cellTemplate : templateService.coupon
			}, {
				field : 'plusCouponId',
				width : '110',
				displayName : '적용플러스쿠폰',
				cellClass : 'alignC',
				cellTemplate : templateService.coupon
			}, {
				field : 'orderCouponId',
				width : '120',
				displayName : '적용장바구니쿠폰',
				cellClass : 'alignC',
				cellTemplate : templateService.coupon
			}, {
				field : 'deliveryCouponId',
				width : '110',
				displayName : '적용배송비쿠폰',
				cellClass : 'alignC',
				cellTemplate : templateService.coupon
			}, {
				/*20161114:포장지변경				
				field : 'wrapCouponId',
				width : '120',
				displayName : '적용선물포장쿠폰',
				cellClass : 'alignC'
				}, {
				 */
				field : 'omsLogisticss[0].logisticsInoutNo',
				width : '100',
				displayName : '배송번호',
				cellClass : 'alignC'
			}, {
				field : 'omsLogisticss[0].invoiceNo',
				width : '100',
				displayName : '운송장번호',
				cellClass : 'alignC',
				cellTemplate : templateService.deliveryTracking,
			}, {
				field : 'textOptionValue',
				width : '100',
				displayName : '텍스트옵션'
			}
	];

	// 주문 > 배송지 > 배송정책 > 상품
	$scope.addressProducts = function(omsDeliveryAddressList) {
		for (var i = 0; i < omsDeliveryAddressList.length; i++) {
			var address = omsDeliveryAddressList[i];
			address.availableAddress = true;
			for (var j = 0; j < address.omsDeliverys.length; j++) {
				var delivery = address.omsDeliverys[j];
				for (var k = 0; k < delivery.omsOrderproducts.length; k++) {
					var product = delivery.omsOrderproducts[k];
					product.$$treeLevel = product.treeLevel;
					product.policySeq = j;
				}
				delivery.availableCancel = true;
				delivery.availableOption = true;
				delivery.availableReturn = true;
				delivery.availableExchange = true;
				delivery.availableRedelivery = true;
				delivery.gridOptions = {
					data : delivery.omsOrderproducts,
					columnDefs : columnDefs,
					isRowSelectable : function(row) {
						return commonfunction.isRowSelectable('DETAIL', row, 0);
					},
					onRegisterApi : function(gridApi) {
						// gridApi.selection.selectAllRows( $scope.gridApi.grid );
						gridApi.selection.on.rowSelectionChanged($scope, function(row, evt) {
							row.entity.checked = row.isSelected;
							commonfunction.setGridControl(row, gridApi); // 그리드 클릭후 제어
							this.grid.options.selectedItems = gridApi.selection.getSelectedRows();
	
							var isAllChecked = true;
							var allRows = this.grid.rows;
							for (var i = 0; i < allRows.length; i++) {
								var entity = allRows[i].entity;
//								if (entity.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SET' || entity.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.GENERAL') {
								if (entity.orderProductTypeCd != 'ORDER_PRODUCT_TYPE_CD.SUB') {
									if (!allRows[i].isSelected) {
										isAllChecked = false;
										break;
									}
								}
							}
							var checkBoxAll = $(evt.target).closest('.grid').find('.check_all');
							$(checkBoxAll).prop('checked', isAllChecked);
						});
						// $scope.gridApi.core.handleWindowResize();
						// $timeout(function() {
						// $scope.gridApi.core.handleWindowResize();
						// });
						// gridApi.treeBase.on.rowExpanded($scope, function(row) {
						// console.log('row',row);
						// });
						// gridApi.grid.registerDataChangeCallback(function() {
						// gridApi.treeBase.expandAllRows();
						//						});
					},
					/** not default options */
					enableColumnMenus : false,
					enableFullRowSelection : true,
					enableSorting : false,
					enableVerticalScrollbar : 0,
					showTreeRowHeader : false,
					showTreeExpandNoChildren : false,
//					treeIndent : 0.001,
					treeIndent : 10,
					enableExpandableRowHeader : false,

					enableGridMenu : false,
					// all of the items selected in the grid. In single select mode there will only be one item in the array.
					selectedItems : [], // no more in v3.0.+
				// rowSelectionFn : function(row) {
				// console.log('row : ',row);
				// row.entity.checked = row.isSelected;
				// },

				/** initial options default value */
				// aggregationCalcThrottle : 500,//ms
				// appScopeProvider : grid.appScope,
				// columnFooterHeight : ? ,//px
				// columnVirtualizationThreshold : 10,
				// enableColumnMenus : true,
				// enableFiltering : false,
				// enableHorizontalScrollbar : uiGridConstants.scrollbars.ALWAYS ,// 0:disable,1(default):enable,2:enable when needed
				// enableMinHeightCheck : true,
				// enableRowHashing : true,
				// enableSorting : true,
				// enableVerticalScrollbar : uiGridConstants.scrollbars.ALWAYS ,
				// excessColumns : 4,
				// excessRows : 4,
				// excludeProperties : [],//If columnDefs is defined, this will be ignored.
				// flatEntityAccess : false,
				// footerTemplate : ui-grid/ui-grid-footer,
				// gridFooterTemplate : ui-grid/ui-grid-grid-footer,
				// gridMenuCustomItems : ?,
				// gridMenuShowHideColumns : true,
				// gridMenuTitleFilter : $translate,
				// headerTemplate : null,
				// horizontalScrollThreshold : 4,
				// infiniteScrollDown : true,
				// infiniteScrollRowsFromEnd : 20,
				// infiniteScrollUp : false,
				// maxVisibleColumnCount : 200,
				// minRowsToShow : 10,
				// minimumColumnSize : 10,//px
				// rowHeight : 30,
				// rowTemplate : ui-grid/ui-grid-row,// Define a row template to customize output. See github wiki for more details.
				// scrollDebounce : 300,//ms
				// scrollThreshold : 4,
				// showColumnFooter : false,
				// showGridFooter : false,
				// showHeader : true,
				// useExternalFiltering : false,
				// useExternalSorting : false,
				// virtualizationThreshold : 20,
				// wheelScrollThrottle : 70,// ms
				/** selection option default value */
				// enableFooterTotalSelected : true,
				// enableFullRowSelection : false, // <> enableRowHeaderSelection
				// enableRowHeaderSelection : true,
				// enableRowSelection : true,
				// enableSelectAll : true,
				// enableSelection : true,
				// enableSelectionBatchEvent : true,
				// isRowSelectable : ?, //Makes it possible to specify a method that evaluates for each row and sets its "enableSelection" property.
				// modifierKeysToMultiSelect : false,
				// multiSelect : true,
				// noUnselect : false,
				// selectionRowHeaderWidth : 30,//px
				/** edit option default value */
				// cellEditableCondition : ?, // If specified, either a value or function to be used by all columns before editing
				// cellEditableCondition : 'true',
				// editableCellTemplate : ui-grid/cellTextEditor,
				// enableCellEdit : undefined, // editable flag on each individual colDefs
				// enableCellEditOnFocus : false,
				// enableColumnResizing : false,
				// enablePinning : false,
				// headerRowHeight : 30,
				/** tree option default value */
				// enableExpandAll : true,
				// showTreeExpandNoChildren : true,
				// showTreeRowHeader : true,
				// treeCustomAggregations : {},
				// treeIndent : 10,
				// treeRowHeaderAlwaysVisible : true,
				// treeRowHeaderBaseWidth : 30,
				/** expandable option default value */
				// expandableRowHeaderWidth : 40,[
				// enableExpandable : true,
				// enableExpandableRowHeader : true,
				// expandableRowHeight : 150,
				// expandableRowScope : expandableScope,
				// expandableRowTemplate : '' //Mandatory
				}
			}
		}

		$scope.omsDeliveryAddressList = omsDeliveryAddressList;
		// console.log('omsDeliveryAddressList : ', omsDeliveryAddressList);
	};

	var pScope = $window.opener.$scope;// 부모창의 scope
	if (!common.isEmpty(pScope.pageId)) {
		$scope.pageId = '6_order';
	}
	
	$window.$scope = $scope; // 현재scope 를 자식에 전달하기 위해
	angular.element(document).ready(function() {
		$scope.func = commonfunction;
	});
	$templateCache.put('ui-grid/selectionRowHeaderButtons', templateService.rowHeaderButtonTemplate());
	$templateCache.put('ui-grid/selectionSelectAllButtons', templateService.selectAllButtonTemplate());
	
	$scope.search = {};
	$scope.mmsMemberZts = {};
	$scope.selectedItems = [];
	$scope.orderId = pScope.orderId;
	$scope.orgAddress = {};
	$scope.memberNo = pScope.memberNo;
	
	$scope.checkBoxAll = function(grid, evt) {
		var allCheck = $(evt.target).is(":checked");
		var rows = grid.rows;
		for (var i = 0; i < rows.length; i++) {
			var row = rows[i];
			// 구성상품 제외
			if (row.entity.orderProductTypeCd != 'ORDER_PRODUCT_TYPE_CD.SUB') {
				if (grid.options.isRowSelectable(row)) {
					row.entity.checked = allCheck;
					row.isSelected = allCheck;
					grid.api.selection.raise.rowSelectionChanged(row, evt);
				}
			}
		}
	}
	$scope.selectCheck = function(grid, row, evt) {
		evt.stopPropagation();
		uiGridSelectionService.toggleRowSelection(grid, row, evt, (grid.options.multiSelect && !grid.options.modifierKeysToMultiSelect), grid.options.noUnselect);
	}
	$scope.popup = {
		cancelAll : function() {// 전체취소
			var update = true;
			for (var i = 0; i < $scope.omsDeliveryAddressList.length; i++) {
				var address = $scope.omsDeliveryAddressList[i];
				for (var j = 0; j < address.omsDeliverys.length; j++) {
					var delivery = address.omsDeliverys[j];
					if (!$filter('filterClaim')(delivery.omsOrderproducts, 'orderProductStateCd', 'CANCEL')) {
						update = false;
						break;
					}
				}
			}
			if (!update) {
				alert('이미 출고지시된 상품이 존재하여 전체주문 취소가 불가능 합니다.\n상품개별로 취소를 진행하여 주십시오.');
				return false;
			}
			$scope.selectedItems = [{
				orderId : $scope.orderId,
				orderProductNo : ''
			}];
			popupwindow('/oms/claim/popup/cancel', '주문취소', '1024', '768');
		},
		claim : function(gridOptions, module, title) {
			if (gridOptions.selectedItems.length < 1) {
				alert(title + '할 상품을 선택해 주십시오');
				return false;
			} else if (gridOptions.selectedItems.length > 1) {
				if (module == 'OPTION') {
					alert('옵션변경은 하나의 상품만 선택해 주십시오');
					return false;
				}
			}
			if (!$filter('filterClaim')(gridOptions.selectedItems, 'orderProductStateCd', module)) {
				alert(title + '이(가) 불가능 상품이 존재 합니다.');
				return false;
			}
			
			var w = '1024', h = '768';
			if (module == 'OPTION') {
				w = '800', h = '600';
				var entity = gridOptions.selectedItems[0];
				if (entity.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SET') {
					alert('세트 상품은 구성상품 단위로 ' + title + '이 가능합니다.');
					return false;
				}
			}
			$scope.selectedItems = gridOptions.selectedItems;
			for (var i = 0; i < $scope.omsDeliveryAddressList.length; i++) {
				var address = $scope.omsDeliveryAddressList[i];
				if (gridOptions.data['0'].deliveryAddressNo == address.deliveryAddressNo) {
					$scope.orgAddress = address;
					break;
				}
			}
			popupwindow('/oms/claim/popup/' + module.toLowerCase(), title, w, h);
		},
		delivery : function(address) {
			var update = true;
			for (var j = 0; j < address.omsDeliverys.length; j++) {
				var delivery = address.omsDeliverys[j];
				if (!$filter('filterClaim')(delivery.omsOrderproducts, 'orderProductStateCd', 'ADDRESS')) {
					update = false;
					break;
				}
			}
			if (!update) {
				alert('이미 출고지시된 상품이 존재하여 배송지 변경이 불가능 합니다.');
				return false;
			}
			$scope.memberNo = $scope.omsOrder.memberNo;
			$scope.deliveryAddressNo = address.deliveryAddressNo;
			popupwindow('/oms/claim/popup/delivery', '배송지 변경', '1024', '768');
		}
	}

	this.init = function() {
		$scope.methods = [];
		$scope.coupons = [];
		$scope.search['orderId'] = $scope.orderId;

		// 1. order master data
		orderService.selectOne($scope.search, function(response) {
			$scope.omsOrder = response;
		});

		// 2. payment data
		$scope.doPartCancel = true;
		$scope.search['searchId'] = 'oms.payment.selectList';
		paymentService.selectList($scope.search, function(paymentList) {
			// 3. payment code list
			commonService.getCodeList({cdGroupCd : 'PAYMENT_METHOD_CD'}).then(function(data) {
				for (var i = 0; i < data.length; i++) {
					var method = data[i];
					var majorYn = ([1, 2, 3, 4, 5, 6, 7, 8].indexOf(method.sortNo) > -1 ? 'Y' : 'N');
					var omsPayment = {
						paymentMethodCd : method.cd,
						paymentMethodName : method.name,
						majorPaymentYn : majorYn,
						sortNo : method.sortNo,
						paymentAmt : 0
					}
					for (var j = 0; j < paymentList.length; j++) {
						var payment = paymentList[j];
						if (payment.paymentMethodCd == method.cd) {
							if (payment.escrowYn == 'Y' || payment.paymentStateCd == 'PAYMENT_STATE_CD.PAYMENT_READY') {
								$scope.doPartCancel = false;
							}
							omsPayment['paymentAmt'] = payment.paymentAmt;
							omsPayment['paymentBusinessNm'] = payment.paymentBusinessNm;
							omsPayment['escrowYn'] = payment.escrowYn;
							omsPayment['interestFreeYn'] = payment.interestFreeYn;
							omsPayment['installmentCnt'] = payment.installmentCnt;
							break;
						}
					}
					$scope.methods.push(omsPayment);
				}
			});
			
			// 4. coupon list
			$scope.search['searchId'] = 'oms.order.select.couponList';
//			$scope.search['couponStateCd'] = 'COUPON_STATE_CD.APPLY';
			orderService.selectList($scope.search, function(response) {
				$scope.coupons = response;
			});
		});

		// 5. product data
		$timeout(function() {
			$scope.search['searchId'] = 'oms.delivery.selectList';
			deliveryService.selectList($scope.search, function(response) {
				$scope.addressProducts(response);
			});
		}, 0);
		
		// . 진행중인 클레임이 있는지 체크
		claimService.selectOne($scope.search, function(response) {
			$scope.claimCount = Number(response.content);
		});		
	}
});

