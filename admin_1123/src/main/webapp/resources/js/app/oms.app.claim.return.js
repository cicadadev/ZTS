// message init
Constants.message_keys = [];

var claimDetailApp = angular.module('claimDetailApp', [
	'ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule', 'ui.grid.pinning'
]);

claimDetailApp.controller('returnCtrl', function($window, $scope, templateService, commonfunction, gridService, commonService, claimService) {
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
				colKey : 'c.oms.claim.return_qty',
				cellClass : 'alignC'
			}, {
				field : 'omsClaimproduct.claimQty',
				width : '80',
				colKey : 'c.oms.claim.claim_qty3',
				cellClass : 'alignC',
				type : 'number',
				enableCellEdit : true,
				validators : {
					required : true
				}
			}, {
				field : 'omsClaimproduct.claimReasonCd',
//				category:"people",
				width : '170',
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
			
//		    headerTemplate: templateService.aaa,
//		    category: [{name: 'people', visible: true}],
			
			
			// enableFiltering : true,
			isRowSelectable : function(row) {
				return commonfunction.isRowSelectable('RETURN', row, 1);
			},
			cellEditableCondition : function($scope) {
				return commonfunction.isRowSelectable('RETURN', $scope.row);
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
			if (entity.omsClaimproduct.claimQty > entity.outQty) {
				hasError3 = true;
				break;
			}
		}
		if (hasError1) {
			alert('반품 수량을 기입해 주십시오.');
			return false;
		} else if (hasError2) {
			alert('반품 사유를 기입해 주십시오.');
			return false;
		} else if (hasError3) {
			alert('반품 수량은 출고수량 보다 클 수 없습니다.');
			return false;
		}
		return true;
	}
	
	this.init = function() {
		// 1. get selected list data
		var module = commonfunction.initClaimGridData('RETURN', pScope, myGrid);

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
		var omsClaimWrapper = {
			claimTypeCd : 'CLAIM_TYPE_CD.' + module,
			claimStateCd : 'CLAIM_STATE_CD.' + state,
			omsPayments : $scope.tempMethods,
			omsOrderproducts : omsOrderproducts,
			omsOrdercoupons : $scope.claimCoupons,
			omsClaimdelivery : $scope.omsClaimdelivery
		}
		claimService.insert(omsClaimWrapper, function(response) {
			console.log('response : ', response);
			if (response.success) {
				alert('반품접수를  완료하였습니다.');
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
		
		var paymentNo = '';
		for (var i = 0; i < $scope.tempMethods.length; i++) {
			var omsPayment = $scope.tempMethods[i];
			if (!common.isEmpty(omsPayment.paymentNo)) {
				paymentNo = omsPayment.paymentNo;
			}
		}
		for (var i = 0; i < $scope.tempMethods.length; i++) {
			var omsPayment = $scope.tempMethods[i];
			omsPayment.paymentNo = paymentNo;
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
		
		if (state == 'REJECT' || state == 'ACCEPT' || state == 'WITHDRAW') {
			var omsClaimWrapper = { 
					claimTypeCd : 'CLAIM_TYPE_CD.' + module, 
					claimStateCd : 'CLAIM_STATE_CD.' + state, 
					omsPayments : $scope.tempMethods,
					omsOrderproducts : omsOrderproducts 
			}
			claimService.update(omsClaimWrapper, function(response) {
				console.log('response : ', response);
				if (response.success) {
					alert('반품 신청을 ' + title + '하였습니다.');
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
					omsClaimdelivery : $scope.omsClaimdelivery
			}
			claimService.complete(omsClaimWrapper, function(response) {
				console.log('response : ', response);
				if (response.success) {
					alert('반품 신청을 ' + title + '하였습니다.');
					pScope.list.search();
					$window.close();
				} else {
					alert(response.resultMessage);
				}
			});
		}
	}
});
