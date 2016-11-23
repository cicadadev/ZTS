// message init
Constants.message_keys = [];

var refundListApp = angular.module('refundListApp', [
		'ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule'
]);

refundListApp.controller('listCtrl', function($window, $scope, $filter, templateService, commonfunction, commonService, gridService, paymentService) {
	var columnDefs = [
			{
				field : 'paymentNo',
				width : '120',
				colKey : 'c.oms.refund.id',
				cellClass : 'alignC',
			}, {
				field : 'claimNo',
				width : '150',
				colKey : 'c.oms.claim.id',
				cellClass : 'alignC',
				cellTemplate : templateService.claim
			}, {
				field : 'insDt',
				width : '150',
				colKey : 'c.oms.refund.datetime_accept',
				cellClass : 'alignC'
			}, {
				field : 'paymentStateName',
				width : '100',
				colKey : 'c.oms.refund.status',
				cellClass : 'alignC'
			}, {
				field : 'orderId',
				width : '150',
				colKey : 'c.oms.order.id',
				cellClass : 'alignC',
				linkFunction : 'func.popup.order'
			}, {
				field : 'omsOrder.name1',
				width : '150',
				colKey : 'c.oms.order.name',
				cellClass : 'alignC',
				cellTemplate : templateService.member('row.entity.omsOrder.memberNo', 'row.entity.omsOrder.memberId')
			}, {
				field : 'paymentAmt',
				width : '100',
				colKey : 'c.oms.refund.amount',
				cellClass : 'alignR',
				cellFilter : 'number'
			}, {
				field : 'refundReasonName',
				width : '120',
				colKey : 'c.oms.refund.reason',
				cellClass : 'alignC'
			}, {
				field : 'paymentBusinessNm',
				width : '120',
				colKey : 'c.oms.refund.bank',
				cellClass : 'alignC'
			}, {
				field : 'refundAccountNo',
				width : '150',
				colKey : 'c.oms.refund.account',
				cellClass : 'alignC'
			}, {
				field : 'accountHolderName',
				width : '100',
				colKey : 'c.oms.refund.name',
				cellClass : 'alignC'
			}, {
				field : 'insId',
				width : '120',
				colKey : 'c.oms.refund.accepter',
				cellClass : 'alignC'
			}, {
				field : 'updId',
				width : '120',
				colKey : 'c.oms.refund.rejecter',
				cellClass : 'alignC'
			}, {
				field : 'paymentDt',
				width : '150',
				colKey : 'c.oms.refund.datetime_complete',
				cellClass : 'alignC'
			}
	];

	var gridParam = {
		scope : $scope,
		gridName : 'grid_refund',
		url : '/api/oms/refund',
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
				if (row.entity.paymentStateCd == 'PAYMENT_STATE_CD.REFUND_CANCEL' || row.entity.paymentStateCd == 'PAYMENT_STATE_CD.REFUND') {
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
//		},
//		callbackFn : function() { // optional
//			console.log('####### callbackFn ######');
		}
	};
	var myGrid = new gridService.NgGrid(gridParam);

	$window.$scope = $scope;
	angular.element(document).ready(function() {
		$scope.func = commonfunction;
		commonService.init_search($scope, 'search');
	});
	
	$scope.search = {
		searchId : 'oms.payment.selectRefundList',
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
  		update : function(status, title) {
			var entities = myGrid.getSelectedRows();
			if (entities.length == 0) {
				alert(title + " 할  목록을 선택해 주십시오.");
				return false;
			}
			for (var i = 0; i < entities.length; i++) {
				console.log('checkedList : ', entities);
				var entity = entities[i];
				entity.paymentStateCd = 'PAYMENT_STATE_CD.' + status;
			}
			paymentService.update(entities, function(response) {
				if (response.success) {
					$scope.list.search();
					alert(title + '가 처리 되었습니다.');
				} else {
					alert(title + ' ' + response.resultMessage);
				}
			});
		}
	}
});

refundListApp.controller('regCtrl', function($window, $scope, commonfunction, commonService, paymentService) {
	
	var pScope = $window.opener.$scope;// 부모창의 scope
	$window.$scope = $scope; // 현재scope 를 자식에 전달하기 위해
	angular.element(document).ready(function() {
		$scope.func = commonfunction;
	});
	
	$scope.banks = [];
	$scope.omsPayment = {
		mmsDeposit : {
			mmsMember : {}
		}
	};
	this.init = function() {
		commonService.getCodeList({cdGroupCd : 'BANK_CD'}).then(function(data) {
			$scope.banks = data;
		});
	}
	this.save = function() {
		// 폼 체크
		if (!commonService.checkForm($scope.omsPayment)) {
			return false;
		}
		if (common.isEmpty($scope.omsPayment.memberNo)) {
			alert('환불받을 회원을 설정해 주십시오');
			return false;
		} else if (common.isEmpty($scope.omsPayment.paymentAmt)) {
			alert('환불금액을 설정해 주십시오');
			return false;
		} else if (common.isEmpty($scope.omsPayment.paymentBusinessCd) 
				|| common.isEmpty($scope.omsPayment.paymentBusinessCd) 
				|| common.isEmpty($scope.omsPayment.accountHolderName)) {
			alert('환불계좌 정보를 설정해 주십시오');
			return false;
		} else if (common.isEmpty($scope.omsPayment.accountAuthDt)) {
			alert('환불계좌 인증을 먼저 실행해 주십시오');
			return false;
		}
		
		if (Number($scope.omsPayment.paymentAmt) > Number($scope.omsPayment.mmsDeposit.balanceAmt)) {
			alert('환불금액은 보유예치금 보다 클 수 없습니다.');
			return false;
		}
		
		if (!common.isEmpty($scope.omsPayment)) {
			$scope.omsPayment['paymentStateCd'] = 'PAYMENT_STATE_CD.REFUND_READY';//환불대기
			$scope.omsPayment['paymentMethodCd'] = 'PAYMENT_METHOD_CD.CASH';
			$scope.omsPayment['paymentTypeCd'] = 'PAYMENT_TYPE_CD.REFUND';
			$scope.omsPayment['refundReasonCd'] = 'REFUND_REASON_CD.REFUNDDEPOSIT';
			
			paymentService.insert($scope.omsPayment, function(response) {
				alert(response.resultMessage);
				if (response.success) {
					pScope.$apply();
					pScope.search.refundReason = 'REFUND_REASON_CD.REFUNDDEPOSIT'
					pScope.list.search();
					$window.close();
				}
			});
		} else {
			alert('환불정보를 입력하여 주십시오.');
			return false;
		}
	}
});