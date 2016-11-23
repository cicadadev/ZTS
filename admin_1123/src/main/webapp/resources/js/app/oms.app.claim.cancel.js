// message init
Constants.message_keys = [];

var claimDetailApp = angular.module('claimDetailApp', [
	'ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule', 'ui.grid.pinning'
]);

claimDetailApp.controller('cancelCtrl', function($window, $scope, templateService, commonfunction, gridService, commonService, claimService) {
	var columnDefs = [
			{
				field : 'orderProductTypeName',
				width : '125',
				colKey : 'c.oms.claim.product_type',
				cellTemplate : templateService.tree,
				pinnedLeft:true
			}, {
				field : 'orderProductStateName',
				width : '100',
				colKey : 'c.oms.claim.delivery_status',
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
				colKey : 'c.oms.claim.cancel_qty',
				cellClass : 'alignC'
			}, {
 	 			field : 'omsClaimproduct.claimQty', 
 	 			width : '80', 
 	 			colKey : 'c.oms.claim.claim_qty1', 
 	 			cellClass : 'alignC', 
// 	 			headerCellClass : templateService.claimQty,
 	 			type : 'number', 
 	 			enableCellEdit : true, 
// 	 			enableCellEdit : templateService.claimQty($scope), 
 	 			cellEditableCondition : function($scope) {
// 	 				console.log('$scope : ', $scope);
// 	 				var cancelType = $scope.$parent.$parent.grid.appScope.cancelType;
// 	 				var isRowSelectable = $scope.$parent.$parent.grid.options.isRowSelectable();
 	 				var cancelType = $scope.col.grid.appScope.cancelType;
 	 				var isRowSelectable = $scope.col.grid.options.isRowSelectable($scope.$parent.$parent.row);
 	 				return cancelType != 'CANCELALL' && isRowSelectable ? true : false;
				},
				validators : { required : true }
			}, {
				field : 'omsClaimproduct.claimReasonCd',
				width : '170',
				colKey : 'c.oms.claim.claim_reason1',
				cellClass : 'alignC',
				enableCellEdit : true,
//				editableCellTemplate : 'ui-grid/dropdownEditor',
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
			// filter: {
			// term: 'xx',
			// condition: uiGridConstants.filter.STARTS_WITH,
			// placeholder: 'starts with...',
			// ariaLabel: 'Starts with filter for field1',
			// flags: { caseSensitive: false },
			// type: uiGridConstants.filter.SELECT,
			// selectOptions: [ { value: 1, label: 'male' }, { value: 2, label: 'female' } ],
			// disableCancelFilterButton: true
			// }
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
				// var isRowSelectable = commonfunction.isRowSelectable(row, 'CANCEL');
				// commonfunction.toggleClaimReasonText(row.entity, row.grid.columns);
				// if (row.entity.omsClaimproduct.claimProductStateCd != 'CLAIM_PRODUCT_STATE_CD.REQ') {
				// isRowSelectable = false;
				// this.gridApi.core.setRowInvisible(row);
				// }
				// if (isRowSelectable) {
				// row.cursor = 'pointer';
				// }
				return commonfunction.isRowSelectable('CANCEL', row, 1);
			},
			cellEditableCondition : function($scope) {
				return commonfunction.isRowSelectable('CANCEL', $scope.row);
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
	$window.$scope = $scope; // 현재scope 를 자식에 전달하기 위해
	$scope.search = {};
	angular.element(document).ready(function() {
		$scope.func = commonfunction;
	});
	
	var checkClaimQty = function(omsOrderproducts) {
		var gridApi = $scope['grid_claim'].gridApi;
		var claimRows = gridApi.core.getVisibleRows(gridApi.grid);
		var hasError1 = true;
		var hasError2 = true;
		var hasError3 = false;
		for (var i = 0; i < claimRows.length; i++) {
			var entity = claimRows[i].entity;
			if (entity.omsClaimproduct.claimQty > 0) {
				hasError1 = false;
				if (!common.isEmpty(entity.omsClaimproduct.claimReasonCd)) {
					hasError2 = false;
					omsOrderproducts.push(entity);
				}
			}
			if (entity.omsClaimproduct.claimQty > entity.orderQty) {
				hasError3 = true;
				break;
			}
		}
		if (hasError1) {
			alert('취소 수량을 입력해 주십시오.');
			return false;
		} else if (hasError2) {
			alert('취소 사유를 입력해 주십시오.');
			return false;
		} else if (hasError3) {
			alert('취소 수량은 주문수량 보다 클 수 없습니다.');
			return false;
		}
		return true;
	}
	
	$scope.cancelType = 'CANCEL';
	var module = 'CANCEL';
	this.init = function() {
		// 1. get selected list data
		module = commonfunction.initClaimGridData(module, pScope, myGrid);
		$scope.cancelType = module;

		// 2. init claim data - payment, coupon, present, bankcode
		commonfunction.initClaimData(module, pScope);
	}
	this.insert = function(module1, state) {
		var omsOrderproducts = [];
		if (!checkClaimQty(omsOrderproducts)) {
			return false;
		}
		
		for (var i = 0; i < $scope.tempMethods.length; i++) {
			var omsPayment = $scope.tempMethods[i];
			if (omsPayment.paymentMethodCd == 'PAYMENT_METHOD_CD.CASH') {
				if (!common.isEmpty(omsPayment.refundAmt) && omsPayment.refundAmt > 0) {
					if (common.isEmpty(omsPayment.paymentBusinessCd)) {
						alert('환불계좌[ 은행 ]을 선택해 주십시오');
						return false;
					}
					if (common.isEmpty(omsPayment.refundAccountNo)) {
						alert('환불계좌[ 계좌번호 ]를 입력해 주십시오');
						return false;
					}
					if (common.isEmpty(omsPayment.accountHolderName)) {
						alert('환불계좌[ 예금주 ]를 입력해 주십시오');
						return false;
					}
				}
			}
		}
		
		// 취소할 상품리스트
		// 취소할 쿠폰리스트 - 상품,플러스,주문,배송비,포장비
		// 환불결제 리스트
		var omsClaimWrapper = {
			claimTypeCd : 'CLAIM_TYPE_CD.' + module1,
			claimStateCd : 'CLAIM_STATE_CD.' + state,
			omsPayments : $scope.tempMethods,
			omsOrderproducts : omsOrderproducts,
			omsOrdercoupons : $scope.claimCoupons,
			omsClaimdelivery : $scope.omsClaimdelivery,
			isAllCancel : (module == 'CANCELALL' ? '1' : '0'),
		}
		claimService.insert(omsClaimWrapper, function(response) {
			console.log('response : ', response);
			if (response.success) {
//				상태별로 노출버튼 상이
//				교환접수/교환반려 : 교환신청되어 있는 상태
				
//				교환접수 : BO에서 등록할때
//				추가결제대기 : BO에서 등록할때 추가비용 있으면...
				
//				교환반려, 교환철회: 취소만 노출
//				수거상태가 모두 입고완료, 배송상태가 모두 배송완료: 자동으로 교환완료 상태로 변경
//				수거상태가 입고완료, 배송상태가 모두 배송완료: 교환완료/교환철회

				alert('취소를  완료하였습니다.');
				pScope.ctrl.init();
				$window.close();
			} else {
				alert('취소에러 : ' + response.resultMessage);
			}
		});
	}
});
