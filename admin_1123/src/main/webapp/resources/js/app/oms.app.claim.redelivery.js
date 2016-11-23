// message init
Constants.message_keys = [];

var claimDetailApp = angular.module('claimDetailApp', [
	'ui.date', 'commonServiceModule', 'omsServiceModule', 'gridServiceModule', 'ui.grid.pinning'
]);

claimDetailApp.controller('redeliveryCtrl', function($window, $scope, templateService, commonfunction, gridService, commonService, orderService, paymentService, claimService) {
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
				colKey : 'c.oms.claim.redelivery_qty',
				cellClass : 'alignC'
			}, {
				field : 'omsClaimproduct.claimQty',
				width : '80',
				colKey : 'c.oms.claim.claim_qty4',
				cellClass : 'alignC',
				type : 'number',
				// cellFilter : 'number',
				enableCellEdit : true,
				validators : {
					required : true
				}
			}, {
				field : 'omsClaimproduct.claimReasonCd',
				width : '150',
				colKey : 'c.oms.claim.claim_reason4',
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
				return commonfunction.isRowSelectable('REDELIVERY', row, 1);
			},
			cellEditableCondition : function($scope) {
				return commonfunction.isRowSelectable('REDELIVERY', $scope.row);
			},
			afterCellEdit : function(rowEntity, colDef, newValue, oldValue) {
//				commonfunction.setRelatedRows(rowEntity, colDef, newValue, oldValue); - 재배송은 연관상품을 같이 세팅하지 않음.
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
			alert('재배송 수량을 기입해 주십시오.');
			return false;
		} else if (hasError2) {
			alert('재배송 사유를 기입해 주십시오.');
			return false;
		} else if (hasError3) {
			alert('재배송 수량은 출고수량 보다 클 수 없습니다.');
			return false;
		}
		return true;
	}
	
	this.init = function() {
		// 1. get selected list data
		var module = commonfunction.initClaimGridData('REDELIVERY', pScope, myGrid);

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
//			omsPayments : $scope.methods,
			omsOrderproducts : omsOrderproducts,
//			omsOrdercoupons : $scope.claimCoupons,
			omsClaimdelivery : $scope.omsClaimdelivery,
			omsDeliveryaddress : $scope.omsDeliveryaddress
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

				alert('재배송 접수를  완료하였습니다.');
				pScope.ctrl.init();
				$window.close();
			} else {
				alert(response.resultMessage);
			}
		});
	}
});
