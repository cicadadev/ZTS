// message init
Constants.message_keys = [];

var claimDetailApp = angular.module('claimDetailApp', [
	'ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule', 'ui.grid.pinning'
]);

claimDetailApp.controller('exchangeCtrl', function($window, $scope, templateService, commonfunction, gridService, commonService, claimService) {
	var columnDefs = [
			{
				field : 'orderProductTypeName',
				width : '125',
				colKey : 'c.oms.claim.product_type',
				cellTemplate : templateService.tree,
				pinnedLeft:true
			}, {
				field : 'omsClaimproduct.claimProductStateName',
				width : '80',
				colKey : 'c.oms.claim.return_status',
				cellClass : 'alignC',
				pinnedLeft:true
			}, {
				field : 'productId',
				width : '80',
				colKey : 'c.oms.claim.product_id',
				cellClass : 'alignC',
				pinnedLeft:true
			}, {
				field : 'productName',
				width : '200',
				colKey : 'c.oms.claim.product_name'
			}, {
				field : 'saleproductName',
				width : '110',
				colKey : 'c.oms.claim.saleproduct_name'
			}, {
				field : 'availableClaimQty',
				width : '100',
				colKey : 'c.oms.claim.exchange_qty',
				cellClass : 'alignC'
			}, {
				field : 'omsClaimproduct.claimQty',
				width : '80',
				colKey : 'c.oms.claim.claim_qty2',
				cellClass : 'alignC',
				type : 'number',
				// cellFilter : 'number',
				enableCellEdit : true,
				validators : {
					required : true
				}
			}, {
				field : 'newSaleProductNm',
				width : '200',
				colKey : 'c.oms.claim.exchange_product',
				headerCellClass : 'edit_column',
				cellTemplate : templateService.option,
//				validators : {
//					required : true
//				}
			}, {
				field : 'omsClaimproduct.claimReasonCd',
				width : '150',
				colKey : 'c.oms.claim.claim_reason3',
				cellClass : 'alignC',
				enableCellEdit : true,
				editableCellTemplate : templateService.dropdown(),
				editDropdownIdLabel : 'cd',
				editDropdownValueLabel : 'name',
				editDropdownOptionsArray : [],
				editDropdownOptionsFunction : function(rowEntity, colDef) {
					colDef.editDropdownOptionsArray = $scope.claimReasons;
					return $scope.claimReasons;
				},
				cellFilter : 'griddropdown:this:row.entity.omsClaimproduct.claimReasonName',
				validators : {
					required : true
				}
			}, {
				field : 'omsClaimproduct.claimReason',
				width : '250',
				colKey : 'c.oms.claim.claim_reason',
				cellClass : 'alignC',
				enableCellEdit : true,
				visible : false
			}, {
				name : '사유동일',
				width : '105',
				cellClass : 'alignC',
				cellTemplate : templateService.sameReason()
			}, {
				field : 'dealTypeName',
				width : '100',
				colKey : 'c.oms.claim.deal_type',
				cellClass : 'alignC'
			}, {
				field : 'totalSalePrice',
				width : '100',
				colKey : 'c.oms.claim.saleprice_unit',
				cellClass : 'alignR',
				cellFilter : 'number'
			}, {
				field : 'dcAmt',
				width : '100',
				colKey : 'c.oms.claim.discount_amt',
				cellClass : 'alignR',
				cellFilter : 'number'
			}, {
				field : 'paymentAmt',
				width : '100',
				colKey : 'c.oms.claim.order_amt',
				cellClass : 'alignR',
				cellFilter : 'number'
			}, {
				field : 'deliveryFeeFreeYn',
				width : '100',
				colKey : 'c.oms.claim.delivery_free_yn',
				cellClass : 'alignC'
			}, {
				field : 'wrapYn',
				width : '100',
				colKey : 'c.oms.claim.wrapping_yn',
				cellClass : 'alignC'
			}, {
				field : 'totalPoint',
				width : '100',
				colKey : 'c.oms.claim.save_point',
				cellClass : 'alignR',
				cellFilter : 'number'
			}
	];
	// grid.getCellValue(row ,col)
	var gridParam = {
		scope : $scope,
		gridName : 'grid_claim',
		url : '/api/oms/claim/target',
		searchKey : 'search',
		columnDefs : columnDefs,
		showGroupPanel : true,
		gridOptions : {
			enableVerticalScrollbar : 0,
			checkBoxEnable : false,
			pagination : false,
			enableCellEditOnFocus : true,
			treeIndent : 10,
			// enableFiltering : true,
			isRowSelectable : function(row) {
				return commonfunction.isRowSelectable('EXCHANGE', row, 1);
//				return true;
			},
			cellEditableCondition : function($scope) {
				return commonfunction.isRowSelectable('EXCHANGE', $scope.row);
			},
			afterCellEdit : function(rowEntity, colDef, newValue, oldValue) {
				commonfunction.setRelatedRows(rowEntity, colDef, newValue, oldValue);
			},
			onRegisterApi : function(gridApi) {
				console.log('gridApi : ',gridApi);
				console.log('myGrid : ',myGrid);
			}
		}
	};
	var myGrid = new gridService.NgGrid(gridParam);

	var pScope = $window.opener.$scope;// 부모창의 scope
//	var pSelectedItems = (!common.isEmptyObject(pScope.selectedItems) ? pScope.selectedItems : []);
	$window.$scope = $scope; // 현재scope 를 자식에 전달하기 위해
	$scope.search = {};
	angular.element(document).ready(function() {
		$scope.func = commonfunction;
	});
	
	var checkClaimQty = function(omsOrderproducts) {
		var gridApi = $scope['grid_claim'].gridApi;
		var claimRows = gridApi.core.getVisibleRows(gridApi.grid);
		var hasError0 = true;
		var hasError1 = true;
		var hasError2 = true;
		var hasError3 = false;
		for (var i = 0; i < claimRows.length; i++) {
			var entity = claimRows[i].entity;
			if (entity.omsClaimproduct.claimQty > 0) {
				hasError1 = false;
				if (!common.isEmpty(entity.omsClaimproduct.claimReasonCd)) {
					hasError2 = false;
					if (!common.isEmpty(entity['newSaleProductId'])) {
						omsOrderproducts.push(entity);
						hasError0 = false;
					}
				}
			}
			if (entity.omsClaimproduct.claimQty > entity.outQty) {
				hasError3 = true;
				break;
			}
		}
		if (hasError0) {
			alert('변경단품을 선택해 주십시오.');
			return false;
		} else if (hasError1) {
			alert('교환 수량을 입력해 주십시오.');
			return false;
		} else if (hasError2) {
			alert('교환 사유를 입력해 주십시오.');
			return false;
		} else if (hasError3) {
			alert('교환 수량은 출고수량 보다 클 수 없습니다.');
			return false;
		}
		return true;
	}
	
	this.init = function() {
		// 1. get selected list data
		var module = commonfunction.initClaimGridData('EXCHANGE', pScope, myGrid);

		// 2. init claim data - payment, coupon, present, bankcode
		commonfunction.initClaimData(module, pScope);
	}
	
	this.insert = function(module, state) {
		var omsOrderproducts = [];
		if (!checkClaimQty(omsOrderproducts)) {
			return false;
		}
		
		// 취소할 상품리스트
		// 취소할 쿠폰리스트 - 상품,플러스,주문,배송비,포장비
		// 환불결제 리스트
//		if ($scope.omsClaimdelivery.tmpDeliveryFee > 0) {	// 20161105 - 배송출고테이블에 넣지 않고 입고배송에 다 넣음.
//			$scope.omsDeliveryaddress.omsDeliverys[0].applyDeliveryFee = $scope.omsClaimdelivery.exchangeDeliveryFee;
//			$scope.omsClaimdelivery.exchangeDeliveryFee  = $scope.omsClaimdelivery.tmpDeliveryFee * 2;
//		}
		var omsClaimWrapper = {
			claimTypeCd : 'CLAIM_TYPE_CD.' + module,
			claimStateCd : 'CLAIM_STATE_CD.' + state,
			omsPayments : $scope.tempMethods,
			omsOrderproducts : omsOrderproducts,
			omsOrdercoupons : $scope.claimCoupons,
			omsClaimdelivery : $scope.omsClaimdelivery,
			omsDeliveryaddress : $scope.omsDeliveryaddress
		}
		claimService.insert(omsClaimWrapper, function(response) {
			console.log('response : ', response);
			if (response.success) {
				alert('교환접수를  완료하였습니다.');
				pScope.ctrl.init();
				$window.close();
			} else {
				alert(response.resultMessage);
			}
		});
	}
	this.update = function(module, state, title) {
		var omsOrderproducts = [];
		if (!checkClaimQty(omsOrderproducts)) {
			return false;
		}
		
		if (state == 'REJECT' || state == 'ACCEPT' || state == 'WITHDRAW') {
			var omsClaimWrapper = { 
					claimTypeCd : 'CLAIM_TYPE_CD.' + module, 
					claimStateCd : 'CLAIM_STATE_CD.' + state, 
					omsPayments : $scope.tempMethods,
					omsOrderproducts : omsOrderproducts,
					omsOrdercoupons : $scope.claimCoupons,
					omsClaimdelivery : $scope.omsClaimdelivery,
					omsDeliveryaddress : $scope.omsDeliveryaddress
			}
			claimService.update(omsClaimWrapper, function(response) {
				console.log('response : ', response);
				if (response.success) {
					alert('교환 신청을 ' + title + '하였습니다.');
					pScope.list.search();
					$window.close();
				} else {
					alert(response.resultMessage);
				}
			});
		} else if (state == 'COMPLETE') {
			var omsClaimWrapper = { 
					claimTypeCd : 'CLAIM_TYPE_CD.' + module, 
					claimStateCd : 'CLAIM_STATE_CD.' + state, 
					omsPayments : $scope.tempMethods,
					omsOrderproducts : omsOrderproducts,
					omsOrdercoupons : $scope.claimCoupons,
					omsClaimdelivery : $scope.omsClaimdelivery,
					omsDeliveryaddress : $scope.omsDeliveryaddress
			}
			claimService.complete(omsClaimWrapper, function(response) {
				console.log('response : ', response);
				if (response.success) {
					alert('교환 신청을 ' + title + '하였습니다.');
					pScope.list.search();
					$window.close();
				} else {
					alert(response.resultMessage);
				}
			});
		}
	}
});
