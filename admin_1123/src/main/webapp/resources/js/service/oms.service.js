var omsServiceModule = angular.module('omsServiceModule', [
	'ngResource', 'commonPopupServiceModule'
]);
//1. 주문상세 클레임버튼 제어 필터
omsServiceModule.filter('filterClaim', function() {
	// 1. 배송지 변경 : 주문상태가 [주문접수/결제완료/주문완료]이고, 배송상태가 출고대기(배송승인이전)인 경우에만 가능 ==> 출고지시, 출고완료, 배송완료만 불가능
	var claimDeliveryStatus = [
			'ORDER_PRODUCT_STATE_CD.REQ', 'ORDER_PRODUCT_STATE_CD.READY', 'ORDER_PRODUCT_STATE_CD.CANCELAPPROVAL', 'ORDER_PRODUCT_STATE_CD.CANCELDELIVERY', 'ORDER_PRODUCT_STATE_CD.CANCEL'
	];

	// 2. 취소, 옵션변경 가능한 상태 : 주문상태가 [주문접수/결제완료/주문완료]이고, 배송상태가 출고대기(배송승인이전)인 경우에만 가능 ==> 출고지시, 출고완료, 배송완료, 주문취소만 불가능
	var claimCancelStatus = [
			'ORDER_PRODUCT_STATE_CD.REQ', 'ORDER_PRODUCT_STATE_CD.READY', 'ORDER_PRODUCT_STATE_CD.CANCELAPPROVAL', 'ORDER_PRODUCT_STATE_CD.CANCELDELIVERY', 'ORDER_PRODUCT_STATE_CD.PARTIALDELIVERY'
	];

	// 3. 반품,교환,재배송 가능한 상태 : 주문상태가[주문완료]이고, 배송상태가 출고완료 이후 가능 ==>
	var claimLogisticsStatus = [
			'ORDER_PRODUCT_STATE_CD.SHIP', 'ORDER_PRODUCT_STATE_CD.DELIVERY'
	];
	
	
	

	// 4. 옵션변경 가능한 상품
	var claimOptionType = [
			'ORDER_PRODUCT_TYPE_CD.GENERAL', 'ORDER_PRODUCT_TYPE_CD.SUB'
	];
	
	// 5. 취소,반품 버튼가능한 상품
	var claimCancelType = [
			'ORDER_PRODUCT_TYPE_CD.GENERAL', 'ORDER_PRODUCT_TYPE_CD.SET', 'ORDER_PRODUCT_TYPE_CD.WRAP'
	];

	// 6. 교환,재배송 버튼가능한 상품
	var claimRedeliveryType = [
			'ORDER_PRODUCT_TYPE_CD.GENERAL', 'ORDER_PRODUCT_TYPE_CD.SET', 'ORDER_PRODUCT_TYPE_CD.ORDERPRESENT', 'ORDER_PRODUCT_TYPE_CD.PRODUCTPRESENT', 'ORDER_PRODUCT_TYPE_CD.WRAP'
	];

	return function(data, key, module) {
		var update = true;
		var filter = [];
		if (key == 'orderProductStateCd') {
			switch (module) {
				case 'ADDRESS':
					filter = claimDeliveryStatus;
					break;
				case 'CANCEL':
					filter = claimCancelStatus;
					break;
				case 'OPTION':
					filter = claimCancelStatus;
					break;
				default:
					filter = claimLogisticsStatus;
				break;
			}
		} else {
			switch (module) {
				case 'OPTION':
					filter = claimOptionType;
					break;
				case 'EXCHANGE':
					filter = claimRedeliveryType;
					break;
				case 'REDELIVERY':
					filter = claimRedeliveryType;
					break;
				default:
					filter = claimCancelType;
				break;
			}
		}
		if (!common.isEmptyObject(data)) {
			for (var i = 0; i < data.length; i++) {
				if (filter.indexOf(data[i][key]) == -1) {
					update = false;
					break;
				}
			}
		}
		return update;
	}
});
// 2. 중복제거 필터
omsServiceModule.filter('unique', function() {
	return function(data, keyname) {
		var output = [], keys = [];

		angular.forEach(data, function(item) {
			var key = item[keyname];
			if (keys.indexOf(key) === -1) {
				keys.push(key);
				output.push(item);
			}
		});

		return output;
	};
});

//3. 가격합산 제어 필터
omsServiceModule.filter('filterSum', function() {
	var methodSub = [
		'PAYMENT_METHOD_CD.DEPOSIT', 'PAYMENT_METHOD_CD.VOUCHER', 'PAYMENT_METHOD_CD.POINT'
	];
	var couponEtc = [
	   //20161114:포장지변경
//     'COUPON_TYPE_CD.PRODUCT', 'COUPON_TYPE_CD.ORDER', 'COUPON_TYPE_CD.PLUS'
		'COUPON_TYPE_CD.PRODUCT', 'COUPON_TYPE_CD.ORDER', 'COUPON_TYPE_CD.PLUS', 'COUPON_TYPE_CD.WRAP'
	];
	var couponDelivery = [
		'COUPON_TYPE_CD.DELIVERY'
	];
	var couponWrapping = [
		'COUPON_TYPE_CD.WRAP'
	];
	return function(data, key1, key2, flag, negative) {
		var sum = 0;
		if (typeof (data) === 'undefined' || typeof (key1) === 'undefined' || typeof (key2) === 'undefined') {
			return sum;
		}
		var filter = [];
		switch (flag) {
			case 1:
//				filter = methodAll;
				break;
			case 2:
				filter = methodSub;
				break;
			case 3:
				filter = couponEtc;
				break;
			case 4:
				filter = couponDelivery;
				break;
			case 5:
				filter = couponWrapping;
				break;
		}
		for (var i = 0; i < data.length; i++) {
			if (flag == 1) {
				sum += Number(data[i][key1]);
			} else if (flag == 2) {
				if (filter.indexOf(data[i][key2]) > -1) {
					sum += Number(data[i][key1]);
				}
			} else {
				if (negative != undefined) {
					if (filter.indexOf(data[i][key2]) > -1) {
						sum -= Number(data[i][key1]);	// 쿠폰은 -로 표시
					}
				} else {
					if (filter.indexOf(data[i][key2]) > -1) {
						sum += Number(data[i][key1]);	// 쿠폰은 -로 표시
					}
				}
			}
		}
		return sum;
	}
});

//4. 쿠폰표시 제어 필터
omsServiceModule.filter('filterCoupon', function() {
	var couponEtc = [
     //20161114:포장지변경
//     'COUPON_TYPE_CD.PRODUCT', 'COUPON_TYPE_CD.ORDER', 'COUPON_TYPE_CD.PLUS'
		'COUPON_TYPE_CD.PRODUCT', 'COUPON_TYPE_CD.ORDER', 'COUPON_TYPE_CD.PLUS', 'COUPON_TYPE_CD.WRAP'
	];
	var couponDelivery = [
		'COUPON_TYPE_CD.DELIVERY'
	];
	var couponWrap = [
		'COUPON_TYPE_CD.WRAP'
	];
	return function(data, flag) {
		var update = [];
		var filter = [];
		switch (flag) {
			case 'delivery':
				filter = couponDelivery;
				break;
			case 'wrap':
				filter = couponWrap;
				break;
			default:
				filter = couponEtc;
				break;
		}
		for (var i = 0; i < data.length; i++) {
			if (filter.indexOf(data[i]['couponTypeCd']) > -1) {
				update.push(data[i]);
			}
		}
		return update;
	}
});

//5. 그리드 dropdown 제어필터
omsServiceModule.filter('griddropdown', function() {
	return function(input, context, initial) {
		var map = context.col.colDef.editDropdownOptionsArray;
		var idField = context.col.colDef.editDropdownIdLabel;
		var valueField = context.col.colDef.editDropdownValueLabel;
		// var initial = context.row.entity[context.col.field];
		if (!common.isEmptyObject(map)) {
			for (var i = 0; i < map.length; i++) {
				if (map[i][idField] == input) {
					return map[i][valueField];
				}
			}
		} else {
			return !common.isEmpty(initial) ? initial : '선택해 주십시오';
		}
		return input;
	};
});

omsServiceModule.service('commonfunction', function($window, $timeout, $interval, $filter, orderService, paymentService, claimService, commonService, commonPopupService) {
	return {
//		reset : function() {
//			commonService.reset_search($scope, 'search');
//			angular.element('.day_group').find('button').eq(0).click();
//		},
		accountCertify : function(omsPayment, checkMember) {
			if (common.isEmpty(omsPayment['paymentBusinessCd'])) {
				alert('은행을 선택하여 주십시오');
				return false;
			} else if (common.isEmpty(omsPayment['refundAccountNo'])) {
				alert('계좌번호를 기입하여 주십시오');
				return false;
			} else if (common.isEmpty(omsPayment['accountHolderName'])) {
				alert('예금주를 기입하여 주십시오');
				return false;
			} 
			
			if (!common.isEmpty(checkMember) && common.isEmpty(omsPayment.memberNo)) {
				alert('회원 정보를 확인하여 주십시오');
				return false;
			}
			
			var payParam = {
					paymentBusinessCd : omsPayment.paymentBusinessCd,
					paymentBusinessNm : omsPayment.paymentBusinessNm,
					refundAccountNo : omsPayment.refundAccountNo,
					accountHolderName : omsPayment.accountHolderName,
					memberNo : omsPayment.memberNo
			}
			
//			paymentService.accountCertify(omsPayment, function(response) {
			paymentService.accountCertify(payParam, function(response) {
				
//				alert('[Result Code] : ' + response.resultCode + '\n[Result Message] : ' + response.resultMessage);
				alert(response.resultMessage);
				if (response.success) {
					var mmsMemberZts = response.returnObject;
					omsPayment['accountAuthDt'] = mmsMemberZts.accountAuthDt;
				}
			});
		},
		setBankName : function(omsPayment, banks) {
			for (var i = 0; i < banks.length; i++) {
				var bank = banks[i];
				if (omsPayment['paymentBusinessCd'] == bank['cd']) {
					omsPayment['paymentBusinessNm'] = bank['name'];
				}
			}
		},
		toggleClaimReasonText : function(entity, columns) {
			for (var i = 0; i < columns.length; i++) {
				if (columns[i].field == 'omsClaimproduct.claimReason') {
					columns[i].visible = (entity.omsClaimproduct.claimReasonCd != 'CLAIM_REASON_CD.TYPING' ? false : true);
					columns[i].colDef.visible = columns[i].visible;
				}
			}
		},
		isRowSelectable : function(module, row, flag) {
			var isAvailable = true;
			// selectable, editable 
			if (row.entity.orderProductTypeCd != 'ORDER_PRODUCT_TYPE_CD.GENERAL' && row.entity.orderProductTypeCd != 'ORDER_PRODUCT_TYPE_CD.WRAP') {
				if (module == 'CANCEL' || module == 'RETURN') {// 취소,교환
//					if (row.entity.orderProductTypeCd != 'ORDER_PRODUCT_TYPE_CD.SET' && row.entity.orderProductTypeCd != 'ORDER_PRODUCT_TYPE_CD.SUB') {
					if (row.entity.orderProductTypeCd != 'ORDER_PRODUCT_TYPE_CD.SET') {
						isAvailable = false;
					}
				} else if (module == 'DETAIL') {
//					if (row.entity.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.ORDERPRESENT' || row.entity.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.PRODUCTPRESENT') {
//						isAvailable = false;
//					} // redmine/issues/2730 ,2879 에 의해 open
				} else {// 반품,재배송
					if (row.entity.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SET') {//세트만 클릭 불가하게 수정
						isAvailable = false;
					}
				}
			}

			// selectable 만
			switch (flag) {
				case 0:
					if (row.entity.orderProductStateCd == 'ORDER_PRODUCT_STATE_CD.CANCEL') {
						isAvailable = false;
					}
					isAvailable ? row.cursor = 'pointer' : '';
					break;
				case 1:
					// this.toggleClaimReasonText(row.entity, row.grid.columns);

					// if (row.entity.omsClaimproduct.claimProductStateCd != 'CLAIM_PRODUCT_STATE_CD.REQ') {
					if (common.isEmpty(row.entity.omsClaimproduct.claimProductStateCd)) {
						isAvailable = false;
						row.grid.api.core.setRowInvisible(row);
					}
					isAvailable ? row.cursor = 'pointer' : '';
					break;
				default:
					break;
			}
			return isAvailable;
		},
		initClaimGridData : function(module, pScope, myGrid) {
			var pSelectedItems = pScope.selectedItems;
			var orderProductNos = '';
			if (!common.isEmptyObject(pSelectedItems)) {
				for (var i = 0; i < pSelectedItems.length; i++) {
					orderProductNos += (i != 0 ? ',' : '') + pSelectedItems[i].orderProductNo;
					if (common.isEmpty(pSelectedItems[i].orderProductNo)) {
						module = 'CANCELALL'; break;
					}
				}
			}

			// 1. get selected list data
			$scope.search['searchId'] = 'oms.claim.select.targetList';
			$scope.search['orderId'] = pScope.orderId;
			$scope.search['orderProductNos'] = orderProductNos;
			$scope.search['claimNo'] = pScope.claimNo;
			myGrid.loadGridData();
			
			return module;
		},
		/** 클레임[1] - 클레임 init data */
		initClaimData : function(module, pScope) {
			var $scope = $window.$scope;
			
			$scope.showButton = 1;
//			$scope.refund = {};
			$scope.omsClaimdelivery = {};
			$scope.omsDeliveryaddress = {};
			
			$scope.claimReasons = [];
			$scope.methods = [];
			$scope.tempMethods = [];
			$scope.coupons = [];
			$scope.presents = [];
			$scope.claimCoupons = [];
			$scope.orderId = pScope.orderId;

			// 1. claim reason code list
			commonService.getCodeList({ cdGroupCd : 'CLAIM_REASON_CD' }).then(function(data) {
				for (var i = 0; i < data.length; i++) {
					if (module == 'REDELIVERY') {
						data[i].sortNo > 9 ? $scope.claimReasons.push(data[i]) : '';
					} else {
						data[i].sortNo < 10 || data[i].sortNo == 13 ? $scope.claimReasons.push(data[i]) : '';
					}
				}
				// $scope.claimReasons = data;
			});
			
			if (module == 'CANCEL' || module == 'CANCELALL' || module == 'RETURN') {	// 취소, 반품
				// 2. bank code list
				commonService.getCodeList({cdGroupCd : 'BANK_CD'}).then(function(data) {
					$scope.banks = data;
				});
				$timeout(function() {
					$scope.search['searchId'] = 'oms.payment.claimList';
					$scope.search['claimNo'] = pScope.claimNo;
					$scope.search['selectKey'] = 'page';
					paymentService.selectList($scope.search, function(paymentList) {
						// 4. payment code list
						commonService.getCodeList({cdGroupCd : 'PAYMENT_METHOD_CD'}).then(function(data) {
							for (var i = 0; i < data.length; i++) {
								var method = data[i];
								var majorYn = ([1, 2, 3, 4, 5, 6, 7, 8].indexOf(method.sortNo) > -1 ? 'Y' : 'N');
								var omsPayment = {
									memberNo : pScope.memberNo,
									paymentTypeCd : 'PAYMENT_TYPE_CD.REFUND',
									paymentMethodCd : method.cd,
									paymentMethodName : method.name,
									refundReasonCd : 'REFUND_REASON_CD.' + module,
									majorPaymentYn : majorYn,
									sortNo : method.sortNo,
									paymentAmt : 0,
									refundAmt : 0
								}

								for (var j = 0; j < paymentList.length; j++) {
									var payment = paymentList[j];
									if (method['cd'] == payment.paymentMethodCd) {
										if (!common.isEmpty(pScope.claimNo)) {// from 클레임조회
											if (payment.paymentStateCd.indexOf('PAYMENT_STATE_CD.REFUND') > -1) {
												if (paymentList.length > 2) {// 환불 완료가 아닌 프로세스가 있을때
													omsPayment.refundAmt = 0;
													if (j == 0) {
														omsPayment.paymentStateCd = payment.paymentStateCd;// 반품 에서 pg 취소시사용
														omsPayment.paymentStateName = payment.paymentStateName;// 반품 에서 pg 취소시사용
													}
												} else {// 환불완료
													omsPayment.refundAmt = -payment.paymentAmt;
													omsPayment.paymentStateCd = payment.paymentStateCd;// 반품 에서 pg 취소시사용
													omsPayment.paymentStateName = payment.paymentStateName;// 반품 에서 pg 취소시사용
												}
												omsPayment.paymentNo = payment.paymentNo;
											} else {
												omsPayment.paymentAmt += payment.paymentAmt;
												omsPayment.pgShopId = payment.pgShopId; // 반품 에서 pg 취소시사용
												omsPayment.pgApprovalNo = payment.pgApprovalNo;// 반품 에서 pg 취소시사용
											}
										} else { // from 주문상세
											omsPayment.refundAmt = 0;
											omsPayment.paymentAmt += payment.paymentAmt;
											omsPayment.paymentStateCd = '';// 반품 에서 pg 취소시사용
											omsPayment.paymentStateName = '';// 반품 에서 pg 취소시사용
											omsPayment.paymentNo = payment.paymentNo;
											omsPayment.pgShopId = payment.pgShopId; // 반품 에서 pg 취소시사용
											omsPayment.pgApprovalNo = payment.pgApprovalNo;// 반품 에서 pg 취소시사용
										}
									}
								}

								// 3. member bank account info
//								if (method['cd'] == 'PAYMENT_METHOD_CD.VIRTUAL' && !common.isEmpty(pScope.memberNo)) {
								if (method['cd'] == 'PAYMENT_METHOD_CD.CASH') {
									if (!common.isEmpty(pScope.memberNo)) {
										// [가상계좌(결제완료 이후), 현금]에서만 환불계좌 필요
										var mmsMemberZts = pScope.omsOrder.mmsMemberZts;
										omsPayment.accountAuthDt = mmsMemberZts.accountAuthDt;
										omsPayment.memberNo = mmsMemberZts.memberNo;
										omsPayment.paymentBusinessCd = mmsMemberZts.bankCd;
										omsPayment.paymentBusinessNm = mmsMemberZts.bankName;
										omsPayment.refundAccountNo = mmsMemberZts.accountNo;
										omsPayment.accountHolderName = mmsMemberZts.accountHolderName;
									} else {// 비회원
										for (var j = 0; j < paymentList.length; j++) {
											var payment = paymentList[j];
											if (payment.paymentMethodCd == 'PAYMENT_METHOD_CD.VIRTUAL' || payment.paymentMethodCd == 'PAYMENT_METHOD_CD.TRANSFER') {
												if (!common.isEmpty(payment.paymentAmt) && payment.paymentAmt && payment.paymentTypeCd == 'PAYMENT_TYPE_CD.PAYMENT') {
													omsPayment.paymentBusinessCd = payment.paymentBusinessCd;
													omsPayment.paymentBusinessNm = payment.paymentBusinessNm;
													omsPayment.refundAccountNo = payment.refundAccountNo;
													omsPayment.accountHolderName = payment.accountHolderName;
												}
											}
										}
									}
								}

								$scope.methods.push(omsPayment);
//								var newObject = JSON.parse(JSON.stringify(omsPayment));
								var newObject = jQuery.extend(true, {}, omsPayment);
								$scope.tempMethods.push(newObject);
							}
						});

						// 5. order coupon list
						$scope.search['searchId'] = 'oms.order.select.couponList';
//						$scope.search['couponStateCd'] = 'COUPON_STATE_CD.APPLY'; // 취소된 것도 보이게 수정
						orderService.selectList($scope.search, function(response) {
							$scope.coupons = response;

							// 6.order present list
							$scope.search['searchId'] = 'oms.order.select.presentList';
							orderService.selectList($scope.search, function(response) {
								$scope.presents = response;
							});
						});
					});
				}, 0);
			}
			
			// 그리드 데이터가 로딩 된 수 주소데이터 세팅
			var setInteval = $interval(function() {
				loadGridData();
			}, 100);
			var loadGridData = function() {
				var gridData = $scope['grid_claim'].data;
//				if (!common.isEmptyObject(claimRows)) {
				if (!common.isEmptyObject(gridData)) {
					var gridApi = $scope['grid_claim'].gridApi;
					var claimRows = gridApi.core.getVisibleRows(gridApi.grid);

					// 7. 입고상품 배송지
					$scope.search['searchId'] = 'oms.claim.select.claimdelivery';
					// $scope.search['orderId'] = pScope.orderId;
					// $scope.search['orderProductNos'] = orderProductNos;
					// $scope.search['claimNo'] = pScope.claimNo;
					claimService.selectList($scope.search, function(response) {
						$scope.omsClaimdelivery = response[0]; // 전체취소의 경우 암거나 하나만 세팅
						$scope.omsClaimdelivery.tmpDeliveryFee = 0;

//						// 10. 진행중인 클레임이 있는지 체크
//						$scope.search['searchId'] = 'oms.claim.select.count';
//						claimService.selectOne($scope.search, function(response) {
//							console.log('count : ', response);
//						});
						if (!common.isEmpty($scope.omsClaimdelivery.claimNo)) {
							if ($scope.omsClaimdelivery.omsClaim.claimStateCd != 'CLAIM_STATE_CD.COMPLETE') {
								if (module == 'RETURN') {
									setTimeout(function() {
										$scope.$apply(function() {
											// commonfunction.setRefund("RETURN", '반품');
											$scope.func.setRefund("RETURN", '반품');
										});
									}, 1000);
								}
							}
						}
						
						$scope.search['searchId'] = 'oms.claim.select.couponHistory';
						$scope.search['orderId'] = pScope.orderId;
						$scope.search['claimNo'] = $scope.omsClaimdelivery.claimNo;
						
						$scope.sumCancelCouponProduct = 0;
						$scope.sumCancelCouponDelivery = 0;
						$scope.sumCancelCouponWrap = 0;
						
						$scope.claimCancelCouponProduct = 0;
						$scope.calimCancelCouponDelivery = 0;
						$scope.claimCancelCouponWrap = 0;
						
						claimService.selectList($scope.search, function(response) {
							for (var i = 0; i < response.length; i++) {
								var omsClaim = response[i];
								if (!common.isEmpty($scope.omsClaimdelivery.claimNo)) {
									if (i == 0) {
										$scope.claimCancelCouponProduct = (omsClaim.productCouponDcCancelAmt + omsClaim.plusCouponDcCancelAmt + omsClaim.orderCouponDcCancelAmt);
										$scope.calimCancelCouponDelivery = omsClaim.omsClaimdelivery.deliveryCouponDcCancelAmt;
										$scope.claimCancelCouponWrap = omsClaim.omsClaimdelivery.wrapCouponDcCancelAmt;
										continue;
									}
								}
								$scope.sumCancelCouponProduct += (omsClaim.productCouponDcCancelAmt + omsClaim.plusCouponDcCancelAmt + omsClaim.orderCouponDcCancelAmt);
								$scope.sumCancelCouponDelivery += omsClaim.omsClaimdelivery.deliveryCouponDcCancelAmt;
								$scope.sumCancelCouponWrap += omsClaim.omsClaimdelivery.wrapCouponDcCancelAmt;
							}
						});
					});
					
					// 8. 출고상품 배송지
					if (module == 'EXCHANGE' || module == 'REDELIVERY') {
						$scope.omsDeliveryaddress = claimRows[0].entity.omsDeliveryaddress;
						if (module == 'EXCHANGE') {
							$scope.omsDeliveryaddress.omsDeliverys[0].applyDeliveryFee = 0;
							$scope.omsDeliveryaddress.omsDeliverys[0].applyWrapFee = 0;
							$scope.omsDeliveryaddress.omsDeliverys[0].deliveryAddressNo = '';
							$scope.omsDeliveryaddress.omsDeliverys[0].deliveryCouponDcAmt = 0;
							$scope.omsDeliveryaddress.omsDeliverys[0].deliveryCouponId = '';
							$scope.omsDeliveryaddress.omsDeliverys[0].deliveryCouponIssueNo = '';
							$scope.omsDeliveryaddress.omsDeliverys[0].insDt = '';
							$scope.omsDeliveryaddress.omsDeliverys[0].insId = '';
							$scope.omsDeliveryaddress.omsDeliverys[0].orderDeliveryFee = $scope.omsDeliveryaddress.omsDeliverys[0].deliveryFee;
							$scope.omsDeliveryaddress.omsDeliverys[0].orderWrapfee = 0;
							$scope.omsDeliveryaddress.omsDeliverys[0].updDt = '';
							$scope.omsDeliveryaddress.omsDeliverys[0].updId = '';
							$scope.omsDeliveryaddress.omsDeliverys[0].wrapCouponDcAmt = 0;
							$scope.omsDeliveryaddress.omsDeliverys[0].wrapCouponId = '';
							$scope.omsDeliveryaddress.omsDeliverys[0].wrapCouponIssueNo = '';
							$scope.omsDeliveryaddress.omsDeliverys[0].wrapTogetherYn = '';
						}
					}
					
					// 9. visible rows setting
					$scope.visibleEntities = [];
					$scope.returnCompleted = '1';	// 0 : 입고안된 상품 있음, 1 : 전체입고
					$scope.outCompleted = '1';		// 0 : 출고안된 상품 있음, 1 : 전체출고
					
					$scope.returnStatus = '-1';		// -1 : 입고 하나도 안됨, 0 : 입고중, 1 : 전체입고완료
					$scope.outStatus = '-1';		// -1 : 입고 하나도 안됨, 0 : 입고중, 1 : 전체입고완료
					var claimNo = null;
					var rows = gridApi.grid.rows; // 전체rows
					for (var i = 0; i < claimRows.length; i++) {
						var omsOrderproduct = claimRows[i].entity;
						$scope.visibleEntities.push(omsOrderproduct);
						
						// for 교환, 재배송
						for (var j = 0; j < gridData.length; j++) {
							var oProduct = gridData[j];
							if (omsOrderproduct.orderProductNo == oProduct.originOrderProductNo) {	// 클레임 신청[교환,재배송의 출고상품]이 진행된 경우
								$scope.omsDeliveryaddress = oProduct.omsDeliveryaddress;// 교환,재배송의 출고 배송지
							}
						}
						
						if (!common.isEmpty(omsOrderproduct.newOrderProductNo)) {// 교환,재배송용 상품중...
							for (var i = 0; i < rows.length; i++) {
								var allEntity = rows[i].entity;
								if (allEntity['orderProductNo'] == omsOrderproduct['newOrderProductNo']) {
									if (allEntity['orderProductStateCd'] != 'ORDER_PRODUCT_STATE_CD.DELIVERY') {// 배송완료가 아닌 것이 있으면
										$scope.outCompleted = '0';
										if (allEntity['orderProductStateCd'] == 'ORDER_PRODUCT_STATE_CD.READY' || allEntity['orderProductStateCd'] == 'ORDER_PRODUCT_STATE_CD.CANCELDELIVERY') {
											$scope.outStatus = '-1';
										} else {
											$scope.outStatus = '0';
										}
									} else {
										$scope.outStatus = '0';
									}
								}
							}
						}
						
						// for 교환, 반품
						claimNo = omsOrderproduct.omsClaimproduct.claimNo;
						if (omsOrderproduct.omsClaimproduct.claimProductStateCd != 'CLAIM_PRODUCT_STATE_CD.RETURN') {//입고완료가 아니면,,,
							$scope.returnCompleted = '0';
						} else {
							$scope.returnStatus = '0';
						}
					}
					if ($scope.returnCompleted == '1') {
						$scope.returnStatus = '1';
					}
					if ($scope.outCompleted == '1') {
						$scope.outStatus = '1';
					}
					
					$interval.cancel(setInteval); // cancel repeate
				}
			}
		},
		/** 클레임[2] - 클레임 row 수정 */
		setRelatedRows : function(selectedEntity, colDef, newValue, oldValue) {
			var $scope = $window.$scope;
			
			// var claimQty = selectedEntity.omsClaimproduct.claimQty;
			var gridApi = $scope['grid_claim'].gridApi;
			var claimRows = gridApi.core.getVisibleRows(gridApi.grid);

			for (var i = 0; i < claimRows.length; i++) {
				var claimEntity = claimRows[i].entity;

				/** 2.1. 상품 클레임수량에 따른 [자식상품] 클레임수량 세팅 */
				if (claimEntity['upperOrderProductNo'] == selectedEntity['orderProductNo']) {// 수정된 row의 자식row
					if (colDef['field'] == 'omsClaimproduct.claimQty') {
						claimEntity.omsClaimproduct.claimQty = newValue * claimEntity.setQty;
					} else if (colDef['field'] == 'omsClaimproduct.claimReasonCd') {
						claimEntity.omsClaimproduct.claimReasonCd = newValue;
					}
				}

				// 주문사은품의 대상상품의 취소금액합계
				for (var j = 0; j < $scope.presents.length; j++) {// 주문에 걸린  [주문프로모션]의 갯수 => 주문사은품의 갯수가 아님!!
					var present = $scope.presents[j];
					if (i == 0) { // presentClaimAmt reset
						present.presentClaimAmt = 0;
					}
					// find 프로모션 대상상품
					if (present['orderProductNos'].indexOf(claimEntity['orderProductNo']) > -1) {
						// present.presentTargetAmt -= (claimEntity.totalSalePrice * claimEntity.omsClaimproduct.claimQty);
						present.presentClaimAmt += (claimEntity.totalSalePrice * claimEntity.omsClaimproduct.claimQty);
					}
				}
			}
			
			var hasPenalty = false;
			if (!common.isEmpty(selectedEntity.omsClaimproduct.claimReasonCd)) {
				var reasonCd = (selectedEntity.omsClaimproduct.claimReasonCd).replace('CLAIM_REASON_CD.', '');
				if ('CHANGE,DESIGN,PRICE,TYPING'.indexOf(reasonCd) > -1) {
					hasPenalty = true;
				}
			}

			/** 2.2. 상품 취소수량에 따른 [주문사은품] 클레임수량 세팅 */
			var rows = gridApi.grid.rows; // 전체rows
			var claimAll = true;
			for (var i = 0; i < rows.length; i++) {// 주문사은품이 아닌 것 상품에 대하여 남아있는 상품이 있는지 체크 TODO - 취소와 반품이 다를 수 있다.
				var allEntity = rows[i].entity;
				if (allEntity['orderProductTypeCd'] != 'ORDER_PRODUCT_TYPE_CD.ORDERPRESENT') {
					var orderQty = allEntity.orderQty;
					for (var j = 0; j < claimRows.length; j++) {
						var claimEntity = claimRows[j].entity;
						if (claimEntity.orderProductNo == allEntity.orderProductNo) {
							orderQty = orderQty - claimEntity.omsClaimproduct.claimQty;
						}
					}
					if (orderQty > 0) {
						claimAll = false;
					}
				}
			}
			for (var i = 0; i < rows.length; i++) {
				var row = rows[i], entity = row.entity;

				for (var j = 0; j < $scope.presents.length; j++) {// 주문에 걸린 [주문프로모션]의 갯수 => 주문사은품의 갯수가 아님!!
					var present = $scope.presents[j];
					if (present['presentId'] == entity['presentId']) { // 주문사은품 찾기
						var isShow = claimAll ? true : false;
						if (hasPenalty) {
							if (entity['presentMinOrderAmt'] > 0) {
								if (present['presentTargetAmt'] - present['presentClaimAmt'] < entity['presentMinOrderAmt']) {
									isShow = true;
								}
							} else {
								isShow = true;
							}
						}

						if (isShow) {
							// 주문사은품 show
							// if (colDef['field'] == 'omsClaimproduct.claimQty') {
							entity.omsClaimproduct.claimQty = entity.orderQty;
							entity.omsClaimproduct.claimReasonCd = selectedEntity.omsClaimproduct.claimReasonCd;
							// } else if (colDef['field'] == 'omsClaimproduct.claimReasonCd') {
							// entity.omsClaimproduct.claimQty = entity.orderQty;
							// entity.omsClaimproduct.claimReasonCd = newValue;
							// }
							entity.omsClaimproduct.claimProductStateCd = 'CLAIM_PRODUCT_STATE_CD.REQ';
							gridApi.core.clearRowInvisible(row); // 보여주기
						} else {
							entity.omsClaimproduct.claimQty = 0;
							entity.omsClaimproduct.claimReasonCd = '';
							entity.omsClaimproduct.claimProductStateCd = '';
							gridApi.core.setRowInvisible(row); // 숨기기
						}
					}
				}
			}
		},
		setClaim : {
			exchange : function(module, title) {
				var $scope = $window.$scope;
				var allEntities = $scope['grid_claim'].data;
				var gridApi = $scope['grid_claim'].gridApi;
				var claimRows = gridApi.core.getVisibleRows(gridApi.grid);
				
				var showAlert1 = true;
				var showAlert2 = true;
				var showAlert3 = false;
				var showAlert4 = false;
				
				for (var i = 0; i < allEntities.length; i++) {
					var allEntity = allEntities[i];
					var availableClaimQty = allEntity.availableClaimQty;
					var claimQty = 0;
					var claimProductStateCd = allEntity.omsClaimproduct.claimProductStateCd;
					if (claimProductStateCd == 'CLAIM_PRODUCT_STATE_CD.REQ') {
						claimQty = allEntity.omsClaimproduct.claimQty;
					}
					if (claimQty > 0) {
						showAlert1 = false;
						if (!common.isEmpty(allEntity.omsClaimproduct.claimReasonCd)) {
							showAlert2 = false;
						}
						if (claimQty > availableClaimQty) {
							showAlert3 = true;
							break;
						}
					} else if (claimQty < 0) {
						showAlert4 = true;
						break;
					}
				}
				if (showAlert1) {
					alert(title + '수량을 기입해 주십시오.');
					return false;
				} else if (showAlert2) {
					alert(title + '사유를 기입해 주십시오.');
					return false;
				} else if (showAlert3) {
					alert(title + '수량은 주문수량 보다 클 수 없습니다.');
					return false;
				} else if (showAlert4) {
					alert(title + '수량은 0 보다 작을 수 없습니다.');
					return false;
				}
				
				var hasError = true;
				var hasPenalty = false;
				for (var i = 0; i < claimRows.length; i++) {
					var claimEntity = claimRows[i].entity;
					// change,design,price,typing
					if (!common.isEmpty(claimEntity.omsClaimproduct.claimReasonCd)) {
						var reasonCd = (claimEntity.omsClaimproduct.claimReasonCd).replace('CLAIM_REASON_CD.', '');
						if ('CHANGE,DESIGN,PRICE,TYPING'.indexOf(reasonCd) > -1) {
							hasPenalty = true;
						}
					}
					if (!common.isEmpty(claimEntity['newSaleProductId'])) {
						hasError = false;
					}
				}
				if (hasError) {
					alert('적어도 한 row 의 변경단품을 선택해 주십시오.');
					return false;
				}

				if (hasPenalty) {// 배송비 부과케이스
					$scope.omsClaimdelivery.tmpDeliveryFee = $scope.omsDeliveryaddress.omsDeliverys[0].deliveryFee;
					$scope.omsClaimdelivery.exchangeDeliveryFee = $scope.omsDeliveryaddress.omsDeliverys[0].deliveryFee * 2;
//					$scope.omsDeliveryaddress.omsDeliverys[0].applyDeliveryFee = $scope.omsDeliveryaddress.omsDeliverys[0].deliveryFee;
				} else {
					$scope.omsClaimdelivery.tmpDeliveryFee = 0;
					$scope.omsClaimdelivery.exchangeDeliveryFee = 0;
//					$scope.omsDeliveryaddress.omsDeliverys[0].applyDeliveryFee = 0;
				}
				
				// row save
				var dirtyRows = gridApi.rowEdit.getDirtyRows();
				var rows = dirtyRows.map(function(gridRow) {
					return gridRow.entity;
				});
//				for (var i = 0; i < rows.length; i++) {
//					var entity = rows[i];
//				}
//				if (!myGrid.validate(rows)) {
//					return;
//				}
			}
		},
		/** 클레임[3] - 환불계산 버튼 */
		setRefund : function(module, title) {
			var $scope = $window.$scope;
			var allEntities = $scope['grid_claim'].data;
			var gridApi = $scope['grid_claim'].gridApi;
			var claimRows = gridApi.core.getVisibleRows(gridApi.grid);
			var policy = claimRows[0].entity.omsDeliveryaddress.omsDeliverys[0];
			$scope.claimCoupons = [];
			$scope.omsClaimdelivery.orderDeliveryFee = 0;
			$scope.omsClaimdelivery.returnDeliveryFee = 0;
			$scope.omsClaimdelivery.exchangeDeliveryFee = 0;
			
			var showAlert1 = true;
			var showAlert2 = true;
			var showAlert3 = false;
			var showAlert4 = false;
			var showAlert5 = false;
			
//			var hasOrderPromotion = false;
			var isFreeDelivery = false;
			
			// 딜적용 								==> 상품 쿠폰(최대할인), 배송비계산(최소주문금액), 주문사은품(최소주문금액)
			// 딜적용 + 상품쿠폰적용					==> 플러스 쿠폰(최대할인)
			// 딜적용 + 상품쿠폰적용 + 플러스쿠폰적용		==> 장바구니 쿠폰(최소주문금액/최대할인), 포인트적립/추가적립(적립대상)
			var stdAmtDealDelivery = 0;	// 배송비계산(최소주문금액)
			var stdAmtPlusOrderCoupon = 0;// 장바구니 쿠폰(최소주문금액/최대할인)
			
			var refundAmt = 0; // 주문취소금액
			var policyWrapVolume = 0; // 남아있는 포장볼륨(누적된 클레임 포함.)
			var remainWrapVolume = 0; // 현재클레임 이후의 포장볼륨
			var existClaimDeliveryFee = false;// 주문의 클레임들 중에 원배송비가 발생했는지를 체크
			
			for (var i = 0; i < allEntities.length; i++) {
				var allEntity = allEntities[i];
				if (allEntity['orderProductTypeCd'] != 'ORDER_PRODUCT_TYPE_CD.GENERAL' && allEntity['orderProductTypeCd'] != 'ORDER_PRODUCT_TYPE_CD.SET') {
					continue;
				}
				if (allEntity.claimDeliveryFee > 0) {
					existClaimDeliveryFee = true;
				}
				var allDelivery = allEntity.omsDeliveryaddress.omsDeliverys[0];
				var availableClaimQty = allEntity.availableClaimQty;
				var claimProductStateCd = allEntity.omsClaimproduct.claimProductStateCd;
				
				var claimQty = 0;
				for (var j = 0; j < claimRows.length; j++) {
					var claimEntity = claimRows[j].entity;
					if (claimEntity.orderProductNo == allEntity.orderProductNo) {
						claimQty = claimEntity.omsClaimproduct.claimQty;
					}
				}
				
				var remainQty = availableClaimQty - claimQty;
				var stdAmtDeal = (allEntity.totalSalePrice) * remainQty;// 딜적용 총합 
				var stdAmtProd = (allEntity.totalSalePrice - allEntity.productCouponDcAmt) * remainQty;// 상품 쿠폰까지의 합
				var stdAmtPlus = (allEntity.totalSalePrice - allEntity.productCouponDcAmt - allEntity.plusCouponDcAmt) * remainQty;// 플러스 쿠폰까지의 합
				
				if (claimQty > 0) {
					showAlert1 = false;
				}
				if (claimQty > -1) {
					if (!common.isEmpty(allEntity.omsClaimproduct.claimReasonCd)) {
						showAlert2 = false;
					}
					if (claimQty > availableClaimQty) {
						showAlert3 = true;
						break;
					}
					if (module == 'CANCEL' && allEntity['orderProductTypeCd'] == 'ORDER_PRODUCT_TYPE_CD.SET') {
//						if (remainQty > 0) { TODO
//							showAlert5 = true;
//							break;
//						}
					}
				} else if (claimQty < 0) {
					showAlert4 = true;
					break;
				}
				
				/** 3.1. 상품 취소수량에 따른 [상품금액] 환불세팅 */
//				refundAmt += (allEntity.paymentAmt * claimQty);
				refundAmt += (allEntity.totalSalePrice * claimQty);
				
				/** 3.2. 상품 취소수량에 따른 [배송비] 환불세팅 */
				// 1. 배송단위에 무료배송상품이 존재하면 무료
				// 2. 배송비쿠폰 적용시 무료
				// 3. (판매가+추가판매가)*주문수량의 합 >= 최소주문금액=0
				// 4. (판매가+추가판매가)*주문수량의 합<최소주문금액=배송비
				if (allDelivery['deliveryAddressNo'] == policy['deliveryAddressNo']) {
					if (allDelivery['deliveryPolicyNo'] == policy['deliveryPolicyNo']) {
						stdAmtDealDelivery += stdAmtDeal;
						if (remainQty > 0) {
							if (allEntity.deliveryFeeFreeYn == 'Y') {
								isFreeDelivery = true;
							}
						}
						
						/** 3.3. 상품 취소수량에 따른 [포장비] 환불세팅 - 20161112:포장지상품으로변경
						if (allEntity.wrapYn == 'Y') {
							var togetherYn = allDelivery.wrapTogetherYn;
							if (togetherYn == 'Y') {
								policyWrapVolume += allEntity.wrapVolume * availableClaimQty;
								remainWrapVolume += allEntity.wrapVolume * remainQty;
							} else {
								policyWrapVolume += Math.ceil(allEntity.wrapVolume * availableClaimQty);
								remainWrapVolume += Math.ceil(allEntity.wrapVolume * remainQty);
							}
						}
						*/
					}
				}
				/** 3.4. 상품 취소수량에 따른 [상품,플러스쿠폰] 환불세팅 */
				// 발급은 상품단위, 사용은 단품단위
				// 취소된 상품에 연결된 상품,플러스
				if (claimQty > 0) {
					for (var j = 0; j < $scope.coupons.length; j++) {
						var coupon = $scope.coupons[j];
						if (coupon['couponId'] == allEntity['productCouponId'] && coupon['couponIssueNo'] == allEntity['productCouponIssueNo']) {
							if (allEntity['productSingleApplyYn'] == 'Y') {
								if (remainQty < 1) {
									coupon.refundCouponAmt = allEntity.productCouponDcAmt;
									coupon.couponStateCd = 'COUPON_STATE_CD.CANCEL';
									$scope.claimCoupons.push(coupon);
								}
							} else {
								coupon.refundCouponAmt = allEntity.productCouponDcAmt * claimQty;
								if (coupon.refundCouponAmt + coupon.couponDcCancelAmt >= coupon.couponDcAmt) {
									coupon.couponStateCd = 'COUPON_STATE_CD.CANCEL';
								}
								$scope.claimCoupons.push(coupon);
							}
						} else if (coupon['couponId'] == allEntity['plusCouponId'] && coupon['couponIssueNo'] == allEntity['plusCouponIssueNo']) {
							if (allEntity['plusSingleApplyYn'] == 'Y') {
								if (remainQty < 1) {
									coupon.refundCouponAmt = allEntity.productCouponDcAmt;
									coupon.couponStateCd = 'COUPON_STATE_CD.CANCEL';
									$scope.claimCoupons.push(coupon);
								}
							} else {
								coupon.refundCouponAmt = allEntity.plusCouponDcAmt * claimQty;
								if (coupon.refundCouponAmt + coupon.couponDcCancelAmt >= coupon.couponDcAmt) {
									coupon.couponStateCd = 'COUPON_STATE_CD.CANCEL';
								}
								$scope.claimCoupons.push(coupon);
							}
						}
					}
				}
				
				/** 3.5. 상품 취소수량에 따른 [주문쿠폰] 환불세팅 */
				if (!common.isEmpty(allEntity['orderCouponId'])) {
					stdAmtPlusOrderCoupon += stdAmtPlus;
				}
			}
			
			/** 20161112:포장지상품으로변경 */
			for (var i = 0; i < allEntities.length; i++) {
				var allEntity = allEntities[i];
				if (allEntity['orderProductTypeCd'] == 'ORDER_PRODUCT_TYPE_CD.WRAP') {
					var claimQty = 0;
					for (var j = 0; j < claimRows.length; j++) {
						var claimEntity = claimRows[j].entity;
						if (claimEntity.orderProductNo == allEntity.orderProductNo) {
							claimQty = claimEntity.omsClaimproduct.claimQty;
						}
					}
					
					var availableClaimQty = allEntity.availableClaimQty;
					var remainQty = availableClaimQty - claimQty;
					
					if (claimQty > 0) {
						showAlert1 = false;
					}
					if (claimQty > -1) {
						if (!common.isEmpty(allEntity.omsClaimproduct.claimReasonCd)) {
							showAlert2 = false;
						}
						if (claimQty > availableClaimQty) {
							showAlert3 = true;
							break;
						}
					} else if (claimQty < 0) {
						showAlert4 = true;
						break;
					}
					// 포장지 상품 환불비
					refundAmt += (allEntity.totalSalePrice * claimQty);
					
					// 포장지 상품의 포장쿠폰 환불
					if (claimQty > 0) {
						for (var j = 0; j < $scope.coupons.length; j++) {
							var coupon = $scope.coupons[j];
							if (coupon['couponId'] == allEntity['productCouponId'] && coupon['couponIssueNo'] == allEntity['productCouponIssueNo']) {
								if (allEntity['productSingleApplyYn'] == 'Y') {
									if (remainQty < 1) {
										coupon.refundCouponAmt = allEntity.productCouponDcAmt;
										coupon.couponStateCd = 'COUPON_STATE_CD.CANCEL';
										$scope.claimCoupons.push(coupon);
									}
								} else {
									coupon.refundCouponAmt = allEntity.productCouponDcAmt * claimQty;
									if (coupon.refundCouponAmt + coupon.couponDcCancelAmt >= coupon.couponDcAmt) {
										coupon.couponStateCd = 'COUPON_STATE_CD.CANCEL';
									}
									$scope.claimCoupons.push(coupon);
								}
							}
						}
					}					
				}
			}
			
			if (showAlert1) {
				alert(title + '수량을 기입해 주십시오.');
				return false;
			} else if (showAlert2) {
				alert(title + '사유를 기입해 주십시오.');
				return false;
			} else if (showAlert3) {
				alert(title + '수량은 주문수량 보다 클 수 없습니다.');
				return false;
			} else if (showAlert4) {
				alert(title + '수량은 0 보다 작을 수 없습니다.');
				return false;
			} else if (showAlert5) {
				alert('세트 상품은 취소가능 수량 전체를 취소하여야 합니다.');
				return false;
			}
			

			/** 3.2.1 상품 취소수량에 따른 [배송비] 환불세팅 */
			/**
			 * 배송비 부과케이스 
			 * 	- 무료 >> 클레임 후 >>> 무료 : 가격변동없음 
			 * 		1) 쿠폰사용
			 * 		2) 무료상품이 있을때
			 * 		3) 최소주문금액만족
			 * 	- 무료 >> 클레임 후 >>> 유료 : 추가된 배송비 부과시킴 (부분취소나 반품에서 발생)
			 * 		1) 무료상품이 취소되었을때
			 * 		2) 최소주문금액 만족이 없어졌을때 
			 * 	- 유료 >> 클레임 후 >>> 무료 : 배송비 환불
			 * 		1) 부과된 배송비가 정책상품들 전체 취소로 환불될때 
			 * 	- 유료 >> 클레임 후 >>> 유료 : 배송비 환불(부분적으로 배송비의 변동이 발생할 수 있음.)
			 * 		1) 정책단위의 배송비만 취소되고 나머지 정책의 배송비는 존재할때
			 * 		2) 정책단위의 배송비가 변동이 없을때
			 */
			var hasPenalty = true;
			for (var i = 0; i < claimRows.length; i++) {
				var claimEntity = claimRows[i].entity;
				if (!common.isEmpty(claimEntity.omsClaimproduct.claimReasonCd)) {
					if (claimEntity['orderProductTypeCd'] == 'ORDER_PRODUCT_TYPE_CD.GENERAL' 
						|| claimEntity['orderProductTypeCd'] == 'ORDER_PRODUCT_TYPE_CD.SET'
						|| claimEntity['orderProductTypeCd'] == 'ORDER_PRODUCT_TYPE_CD.WRAP') {
						var reasonCd = (claimEntity.omsClaimproduct.claimReasonCd).replace('CLAIM_REASON_CD.', '');
//							if ('CHANGE,DESIGN,PRICE,TYPING'.indexOf(reasonCd) > -1) {	// 하나라도 귀책사유가 고객이면 부과.
//								$scope.omsClaimdelivery.returnDeliveryFee = policy.deliveryFee; // 반품으로 인한 추가배송비 생성
//							}
						if ('CHANGE,DESIGN,PRICE,TYPING'.indexOf(reasonCd) < 0) {	// 귀책사유가 혼재된 경우 판매자 귀책
							hasPenalty = false;
						}
					}
				}
			}
			if (module == 'RETURN') {
				$scope.omsClaimdelivery.returnDeliveryFee = (hasPenalty ? policy.deliveryFee : 0); // 반품으로 인한 추가배송비 생성
			}
			
			var refundDeliveryFee = 0;
			var isCancelDeliveryCoupon = false;
			if (!common.isEmpty(policy.deliveryCouponId)) {
				// stdAmtDealDelivery --> 취소후 정책단위 상품들의 총합계
				if (stdAmtDealDelivery > 0) {
					// 여전히 배송비 쿠폰이 적용됨. --> 쿠폰적용가 변동없음.배송비는 여전히 무료
				} else {
					// 배송비 쿠폰 적용 취소(배송정책의 전체상품취소)
					// refundDeliveryFee = policy.deliveryCouponDcAmt;
					refundDeliveryFee = policy.orderDeliveryFee; // 쿠폰할인이 있으면 아래 쿠폰단위에서 [-]처리해줌.(그래서 적용배송비가 아니라주문배송비)
					isCancelDeliveryCoupon = true;
				}
			} else {
				if (!isFreeDelivery) {
					if (stdAmtDealDelivery > 0) {
						if (stdAmtDealDelivery < policy.minDeliveryFreeAmt) {// 배송비 부과케이스
							if (policy.applyDeliveryFee > 0) { // 최초 배송료 있었음.
							} else {// 최초부터 무료
								// 무료 ==> 유료
//								if (policy.applyDeliveryFee < policy.deliveryFee && !existClaimDeliveryFee) {
								if (hasPenalty && !existClaimDeliveryFee) {
									$scope.omsClaimdelivery.orderDeliveryFee = policy.deliveryFee; // 취소(반품으)로 인한 추가배송비 생성
								}
							}
						}
					} else {
						// 부과된 배송료 환불세팅
						if (module == 'CANCEL') {
							refundDeliveryFee = policy.applyDeliveryFee;
						} else {
							/*
							if (policy.applyDeliveryFee > 0) { // 최초 배송료 있었음.
							} else {// 최초부터 무료
								// 무료 ==> 유료
//								if (policy.applyDeliveryFee < policy.deliveryFee && !existClaimDeliveryFee) {
//								if (hasPenalty && !existClaimDeliveryFee) {
//									$scope.omsClaimdelivery.orderDeliveryFee = policy.deliveryFee; // 취소(반품으)로 인한 추가배송비 생성
//								}
							}
							*/
							// http://192.2.72.118/redmine/issues/2924
							if (hasPenalty && !existClaimDeliveryFee) {
								$scope.omsClaimdelivery.orderDeliveryFee = policy.deliveryFee; // 취소(반품으)로 인한 추가배송비 생성
							}
						}
					}
				}
			}
			refundAmt += refundDeliveryFee;
			
			/** 3.3.1 상품 취소수량에 따른 [포장비] 환불세팅 */
			var beforeWrapFee = Math.ceil(policyWrapVolume) * 1000;// TODO - 하드코딩 제거.;
			var afterWrapFee = Math.ceil(remainWrapVolume) * 1000;
			var refundWrapFee = 0;//포장비 환불
			
			if (policyWrapVolume > 0) {	// 포장비 존재할 경우
				if (remainWrapVolume > 0) {
					var claimWrapFee = beforeWrapFee - afterWrapFee;//환불되는 포장비
					refundWrapFee = claimWrapFee; // 환불포장비
//					if (!common.isEmpty(policy.wrapCouponId)) {
//						if (afterWrapFee < policy.wrapCouponDcAmt) {
//							// 포장비 쿠폰 복원 케이스
//							refundWrapFee = claimWrapFee - policy.wrapCouponDcAmt;// 환불포장비
//						}
//					}
				} else {//전체(혹은 포장상품) 취소되어 포장비 없어짐.
					refundWrapFee = beforeWrapFee; // 환불포장비
//					if (!common.isEmpty(policy.wrapCouponId)) {
//						refundWrapFee = beforeWrapFee - policy.wrapCouponDcAmt;// 환불포장비
//					}
				}
			}
			refundAmt += refundWrapFee;

			for (var j = 0; j < $scope.coupons.length; j++) {
				var coupon = $scope.coupons[j];
				/** 3.5. 상품 취소수량에 따른 [주문쿠폰] 환불세팅 */
				if (coupon['couponTypeCd'] == 'COUPON_TYPE_CD.ORDER') {
					if (stdAmtPlusOrderCoupon >= coupon['minOrderAmt']) {
						// 주문쿠폰 금액 재분배
						var remainOrderCouponDcAmt = 0;
						if (coupon['dcApplyTypeCd'] == 'DC_APPLY_TYPE_CD.AMT') {// 정액
							remainOrderCouponDcAmt = coupon.couponDcAmt;
						} else {// 정율
							remainOrderCouponDcAmt = Math.round(stdAmtPlusOrderCoupon * coupon.dcValue * 0.01 * 0.1) * 10;
						}
						if (remainOrderCouponDcAmt > coupon.maxDcAmt) {
							remainOrderCouponDcAmt = coupon.maxDcAmt;
						}

						// 실제로는 취소/반품 상품에 걸려있지 않은 쿠폰이나 가격만 계산(안걸려잇으면 refundcouponamt는 0)
						coupon.refundCouponAmt = coupon.couponDcAmt - coupon.couponDcCancelAmt - remainOrderCouponDcAmt;
						$scope.claimCoupons.push(coupon);
					} else {
						// 주문쿠폰 제거
						coupon.refundCouponAmt = coupon.couponDcAmt - coupon.couponDcCancelAmt;
						coupon.couponStateCd = 'COUPON_STATE_CD.CANCEL';
						$scope.claimCoupons.push(coupon);
					}
				}
				
				/** 3.6. 상품 취소수량에 따른 [배송비쿠폰,포장비쿠폰] 환불세팅 */
				if (coupon['couponId'] == policy['deliveryCouponId'] && coupon['couponIssueNo'] == policy['deliveryCouponIssueNo']) {
					if (isCancelDeliveryCoupon) {
						coupon.refundCouponAmt = coupon.couponDcAmt;// 적용된 정책의 배송비만큼 할인된 금액
						coupon.couponStateCd = 'COUPON_STATE_CD.CANCEL';
						$scope.claimCoupons.push(coupon);
					}
				} else if (coupon['couponId'] == policy['wrapCouponId'] && coupon['couponIssueNo'] == policy['wrapCouponIssueNo']) {
					if (afterWrapFee <= 0) {
						coupon.refundCouponAmt = coupon.couponDcAmt;
						coupon.couponStateCd = 'COUPON_STATE_CD.CANCEL';
						$scope.claimCoupons.push(coupon);
					}
				}
			}

			// 상품의 총합 -> 상품쿠폰 -> 플러스쿠폰 -> 장바구니쿠폰 -> 포인트사용 -> 예치금사용
			for (var j = 0; j < $scope.claimCoupons.length; j++) {
				var claimCoupon = $scope.claimCoupons[j];
				refundAmt -= claimCoupon.refundCouponAmt;
			}

			/** 환불금액 세팅 (우선순위: 주결제수단 > 매일모바일상품권 > 예치금 > 매일포인트 > 쿠폰) */
			var totalAddtionalAmt =  $scope.omsClaimdelivery.orderDeliveryFee + $scope.omsClaimdelivery.returnDeliveryFee;
			for (var i = 0; i < $scope.methods.length; i++) {
				var method = $scope.methods[i];
				// if (method.majorPaymentYn == 'Y') {
				if (method.paymentAmt > 0) {
					var tempMethod = $scope.tempMethods[i];

					tempMethod.refundAmt = (tempMethod.paymentAmt > refundAmt ? refundAmt : tempMethod.paymentAmt);
					refundAmt = refundAmt - tempMethod.refundAmt;
					if (totalAddtionalAmt > 0) {
						if (tempMethod.refundAmt > totalAddtionalAmt) {
							tempMethod.refundAmt = tempMethod.refundAmt - totalAddtionalAmt;
							totalAddtionalAmt = 0;
						} else {
							totalAddtionalAmt = totalAddtionalAmt - tempMethod.refundAmt;
							tempMethod.refundAmt = 0;
						}
					}
				}
				// }
			}
			
			if (totalAddtionalAmt > 0) {
				alert('환불금액보다 추가금액이 [ ' + totalAddtionalAmt + '원 ] 더 발생하여 반품접수가 불가능합니다.');
				$scope.showButton = 0;
				refundAmt = refundAmt - totalAddtionalAmt;
				return false;
			} else {
				$scope.showButton = 1;
			}
			
			// row save
			var dirtyRows = gridApi.rowEdit.getDirtyRows();
			var rows = dirtyRows.map(function(gridRow) {
				return gridRow.entity;
			});
		},
		/** 주문상세[1] - 그리드 row 제어, 클레임버튼 제어 */
		setGridControl : function(row, gridApi) {
			// 1.1 selection 제어
			// 주문취소/반품/재배송 : 세트단위 
			// 옵션변경/교환 : 구성상품단위
			var gridRows = gridApi.selection.getSelectedGridRows();
			if (row.isSelected) {
				if (row.entity['orderProductTypeCd'] == 'ORDER_PRODUCT_TYPE_CD.SET') {	// 구성상품제어
					for (var i = 0; i < gridRows.length; i++) {
						var gridRow = gridRows[i];
						if (gridRow.entity['upperOrderProductNo'] == row.entity['orderProductNo']) {
							gridRow.isSelected = !row.isSelected;
							gridRow.entity.checked = !row.isSelected;
						}
					}
				} else if (row.entity['orderProductTypeCd'] == 'ORDER_PRODUCT_TYPE_CD.SUB') {	// 세트상품과 구성상품제어
					for (var i = 0; i < gridRows.length; i++) {
						var gridRow = gridRows[i];
						if (gridRow.entity['orderProductNo'] == row.entity['upperOrderProductNo']) {
							gridRow.isSelected = !row.isSelected;
							gridRow.entity.checked = !row.isSelected;
						}
						if (gridRow.entity['orderProductNo'] != row.entity['orderProductNo'] && gridRow.entity['upperOrderProductNo'] == row.entity['upperOrderProductNo']) {
							gridRow.isSelected = !row.isSelected;
							gridRow.entity.checked = !row.isSelected;
						}
					}
				}
			}

			// 1.2 클레임 버튼 disabled/enabled 처리
			var gridEntities = gridApi.selection.getSelectedRows();
			var modules = {
				'OPTION' : gridEntities.length > 1 ? false : true,
				'CANCEL' : true,
				'RETURN' : true,
				'EXCHANGE' : true,
				'REDELIVERY' : true
			};
			for (key in modules) {
				if (modules[key]) {
					if ($filter('filterClaim')(gridEntities, 'orderProductStateCd', key)) {// 1.2.1 상품상태제어
						modules[key] = $filter('filterClaim')(gridEntities, 'orderProductTypeCd', key);// 1.2.2 상품유형제어
					} else {
						modules[key] = false;
					}
				}
			}
			
			var hasClaim = false;
			var $scope = $window.$scope;
			if ($scope.claimCount > 0) {
				console.log('$scope.claimCount : ' , $scope.claimCount);
				hasClaim = true;
			}
			var addresses = $scope.omsDeliveryAddressList;
			for (var i = 0; i < addresses.length; i++) {
				var address = addresses[i];
				if (address.deliveryAddressNo == row.entity['deliveryAddressNo']) {
					// var availableAddress = $filter('filterClaim')(gridEntities, 'orderProductStateCd', 'ADDRESS');
					// address.availableAddress = availableAddress;

					for (var j = 0; j < address.omsDeliverys.length; j++) {
						var delivery = address.omsDeliverys[j];
						if (delivery.deliveryPolicyNo == row.entity['deliveryPolicyNo']) {
							delivery.availableOption = !hasClaim && modules['OPTION'];
							delivery.availableCancel = !hasClaim && modules['CANCEL'] && $scope.doPartCancel;
							delivery.availableReturn = !hasClaim && modules['RETURN'];
							delivery.availableExchange = !hasClaim && modules['EXCHANGE'];
							delivery.availableRedelivery = !hasClaim && modules['REDELIVERY'];
						}
					}
				}
			}
		},
		setAddDelvFee : function() {
			var applyAddDeliveryFee = false;
			for (var i = 0; i < $scope.methods.length; i++) {
				var method = $scope.methods[i];
//				if (method.majorPaymentYn == 'Y') {
					if (method.paymentAmt > 0) {
						if (!applyAddDeliveryFee) {
							var tempMethod = $scope.tempMethods[i];
							tempMethod.refundAmt = (method.refundAmt - $scope.omsClaimdelivery.orderDeliveryFee - $scope.omsClaimdelivery.returnDeliveryFee);
							applyAddDeliveryFee = true;
						}
					}
//				}
			}
		},
		sameReason : function(entity, grid) {
			var claimReasonCd = entity.omsClaimproduct.claimReasonCd;
			var claimReason = entity.omsClaimproduct.claimReason;
			if (!common.isEmpty(claimReasonCd)) {
				var claimRows = grid.api.core.getVisibleRows(grid);
				for (var i = 0; i < claimRows.length; i++) {
					var claimEntity = claimRows[i].entity;
					claimEntity.omsClaimproduct.claimReasonCd = claimReasonCd;
					claimEntity.omsClaimproduct.claimReason = claimReason;
				}
			}
		},
		popup : {
			order : function(field, entity) { // linkfunction
				$scope.orderId = entity.orderId;
				$scope.memberNo = entity.memberNo;
				// popupwindow('/oms/order/popup/detail', '주문상세', 1500, 850);
				popupwindow('/oms/order/popup/detail', '주문상세', 1024, 600);
			},
			memo : function(orderId, orderProductNo) { // template
				$scope.orderId = orderId;
				$scope.orderProductNo = orderProductNo;
				popupwindow('/oms/order/popup/memo', '주문메모', 1024, 400);
			},
			coupon : function(module, id) {
				if (module == 'unit') {
					$scope.couponId = id;
					popupwindow('/sps/coupon/popup/detail', '쿠폰상세', '1024', '768');
				} else {
					if (id == 'wrap') { // from 주문상세
						$scope.couponTypeCds = "'COUPON_TYPE_CD.WRAP'";
					} else if (id == 'delivery') { // from 주문상세
						$scope.couponTypeCds = "'COUPON_TYPE_CD.DELIVERY'";
					} else if (id == 'etc') { // from 주문상세
						//20161114:포장지변경
//						$scope.couponTypeCds = "'COUPON_TYPE_CD.PRODUCT', 'COUPON_TYPE_CD.ORDER', 'COUPON_TYPE_CD.PLUS'";
						$scope.couponTypeCds = "'COUPON_TYPE_CD.PRODUCT', 'COUPON_TYPE_CD.ORDER', 'COUPON_TYPE_CD.PLUS', 'COUPON_TYPE_CD.WRAP'";
					} else { // from 클레임(취소,반품)
						$scope.searchCoupons = [];
						for (var i = 0; i < $scope.claimCoupons.length; i++) {
							var coupon = $scope.claimCoupons[i];
							$scope.searchCoupons.push("'" + coupon.couponId + "'," + coupon.couponIssueNo);
						}
					}
					popupwindow('/oms/order/popup/coupon', '쿠폰목록', '820', '400');
				}
			},
			member : function(memberNo) { // template
				if (!common.isEmpty(memberNo)) {
					commonPopupService.openMemberDetailPopup($scope, memberNo);
				} else {	// 환불관리 > 환불등록에서 사용
					$scope.callback_member = function(data) {
						
						if (!common.isEmpty(data[0].memberNo)) {
							var search = {};
							search.searchId = 'mms.member.getLatestDepositBalanceAmt';
							search.memberNo = data[0].memberNo;
							commonService.selectValue(search, '/api/oms/deposit/amt', function(response) {
								if (!common.isEmpty(response.content)) {
									$scope.omsPayment.mmsDeposit.balanceAmt = response.content;
								} else {
									$scope.omsPayment.mmsDeposit.balanceAmt = 0;
								}
							});
							
							$scope.omsPayment.accountAuthDt = data[0].accountAuthDt;
							$scope.omsPayment.memberNo = data[0].memberNo;
							$scope.omsPayment.paymentBusinessCd = data[0].bankCd;
							$scope.omsPayment.paymentBusinessNm = data[0].bankName;
							$scope.omsPayment.refundAccountNo = data[0].accountNo;
							$scope.omsPayment.accountHolderName = data[0].accountHolderName;						
							$scope.omsPayment.mmsDeposit.mmsMember = data[0].mmsMember;
							
							common.safeApply($scope);
						}
						
					}
					commonPopupService.memberPopup($scope, 'callback_member', false);
				}
			},
			product : function(productId) {// template
				commonPopupService.openProductDetailPopup($scope, productId);
			},
			productName : function(productId) {// template
				window.open(global.config.domainUrlFront + '/pms/product/detail?productId=' + productId);
			},
			business : function(businessId) {// template
				$scope.businessId = businessId;
				popupwindow('/ccs/business/popup/detail', "businessDetailPopup", 1200, 650);
			},
			offshop : function(field, row) { // linkfunction
				$scope.offshopId = row.offshopId;
				// popupwindow('/oms/order/popup/detail', '매장상세', 1100, 850);
				alert('담당자~매장상세 팝업 만들어 주세요~');
			},
			claim : function(entity) {
//				$scope.search['searchId'] = 'oms.claim.select.productList';
//				$scope.search['orderId'] = entity.orderId;
//				$scope.search['claimNo'] = entity.claimNo;
//				claimService.selectList($scope.search, function(response) {
//					$scope.selectedItems = response;
//				});
				
				$scope.orderId = entity.orderId;
				$scope.claimNo = entity.claimNo;
				$scope.memberNo = entity.omsOrder.memberNo;
				$scope.omsOrder = entity.omsOrder;
				
				var claimTypeCd = entity.claimTypeCd;
				if (common.isEmpty(entity.claimTypeCd)) {
					claimTypeCd = entity.refundReasonCd;
				}
				var module = (claimTypeCd.substring(claimTypeCd.indexOf('.') + 1)).toLowerCase();
				popupwindow('/oms/claim/popup/' + module, '클레임상세', '1024', '768');
			},
			registration : function() {
				popupwindow('/oms/refund/popup/insert', '환불등록', '1024', '300');
			},
			option : function(row) {
				$scope.selectedItems = row.entity;
				$scope.searchParam = {
					callback : function(data) {
						$scope.selectedItems['newSaleProductId'] = data.saleproductId;
						$scope.selectedItems['newSaleProductNm'] = data.name;
						common.safeApply($scope, function() {
							var gridApi = $scope['grid_claim'].gridApi;
							gridApi.rowEdit.setRowsDirty([$scope.selectedItems]);
						});
					}
				}
				popupwindow('/oms/claim/popup/option', '옵션변경', '800', '600');
			},			
			zip : function(callbackType, row) {
				if (callbackType == 1) { // 주문배송
					$scope.callback_address = function(data) {
						$scope.omsDeliveryaddress.zipCd = data.postNo;
						$scope.omsDeliveryaddress.address1 = data.address1;
						$scope.omsDeliveryaddress.address2 = data.address2;
						common.safeApply($scope);
					}
				} else if (callbackType == 2) { // 클레임배송
					$scope.callback_address = function(data) {
						$scope.omsClaimdelivery.returnZipCd = data.postNo;
						$scope.omsClaimdelivery.returnAddress1 = data.address1;
						$scope.omsClaimdelivery.returnAddress2 = data.address2;
						common.safeApply($scope);
					}
				} else if (callbackType == 3) { // 정기배송
					$scope.selectedItems = row.entity;
					$scope.callback_address = function(data) {
						$scope.selectedItems.omsRegulardelivery.deliveryZipCd = data.postNo;
						$scope.selectedItems.omsRegulardelivery.deliveryAddress1 = data.address1;
						$scope.selectedItems.omsRegulardelivery.deliveryAddress2 = data.address2;
						common.safeApply($scope, function() {
							var gridApi = $scope['grid_regular'].gridApi;
							gridApi.rowEdit.setRowsDirty([$scope.selectedItems]);
						});
					}
				}
				commonPopupService.openAddressPopup($scope, 'callback_address');
			},
			deliveryTracking : function(entity) {
//				entity.orderId
				$scope.orderProductNo = entity.orderProductNo;
				popupwindow('/oms/delivery/popup/tracking', '배송추적', '900', '600');
			},
			close : function() {
				$window.close();
			}
		}
	}
});
// 템플릿 서비스
omsServiceModule.service('templateService', function() {
	return {
		rowTemplate : function() {	// $templateCache.put('ui-grid/ui-grid-row', templateService.rowTemplate());
			var retHtml = '';
			retHtml += '<div ng-repeat="(colRenderIndex, col) in colContainer.renderedColumns track by col.uid" ui-grid-one-bind-id-grid="rowRenderIndex + \'-\' + col.uid + \'-cell\'" ';
			retHtml += '	 ng-style="{ \'cursor\': row.cursor }" class="ui-grid-cell" ';
			retHtml += '	 ng-class="{ \'ui-grid-row-header-cell\': col.isRowHeader }" role="{{col.isRowHeader ? \'rowheader\' : \'gridcell\'}}" ui-grid-cell>'
			retHtml += '</div>'
			return retHtml;
		},
		rowHeaderButtonTemplate : function() {	// $templateCache.put('ui-grid/selectionRowHeaderButtons', templateService.rowHeaderButtonTemplate());
			var retHtml = '';
			retHtml += '<div class="check_header">';
			retHtml += '	<input type="checkbox" ng-click="grid.appScope.selectCheck(grid, row, $event)" ng-model="row.entity.checked"';
			retHtml += '		   ng-style="{\'cursor\': row.cursor }" ng-disabled="!grid.options.isRowSelectable(row)">';
			retHtml += '</div>';
			return retHtml;
		},
		selectAllButtonTemplate : function() {	// $templateCache.put('ui-grid/selectionSelectAllButtons', templateService.selectAllButtonTemplate());
			var retHtml = '';
			retHtml += '<div class="check_header">';
			retHtml += '	<input class="check_all" type="checkbox" ng-click="grid.appScope.checkBoxAll(grid, $event)" ng-style="{\'cursor\': \'pointer\' }"/>';
			retHtml += '</div>';
			return retHtml;
		},
		memo : function(orderId, orderProductNo) {
			var retHtml = '';
			retHtml += '<div ng-class="{invalid:grid.validate.isInvalid(row.entity,col.colDef)}" class="ui-grid-cell-contents" title="TOOLTIP"> ';
			retHtml += '	<a href="javascript:void(0);" ng-click="grid.appScope.func.popup.memo(' + orderId + ', ' + orderProductNo + ')" ng-class="{underline:true,hasmemo:row.entity.memoCnt > 0}">';
			retHtml += '		메모';
			retHtml += '	</a>';
			retHtml += '</div>';
			return retHtml;
		},
		coupon : function() {
			var retHtml = '';
			retHtml += '<div ng-class="{invalid:grid.validate.isInvalid(row.entity,col.colDef)}" class="ui-grid-cell-contents" title="TOOLTIP"> ';
			retHtml += '	<a href="javascript:void(0);" ng-click="grid.appScope.func.popup.coupon(\'unit\', COL_FIELD)" ng-class="{underline:true}">';
			retHtml += '		{{COL_FIELD CUSTOM_FILTERS}}';
			retHtml += '	</a>';
			retHtml += '</div>';
			return retHtml;
		},
		product : function() {
			var retHtml = '';
			retHtml += '<div ng-class="{invalid:grid.validate.isInvalid(row.entity,col.colDef)}" class="ui-grid-cell-contents" title="TOOLTIP"> ';
			retHtml += '	<a href="javascript:void(0);" ng-click="grid.appScope.func.popup.product(COL_FIELD)" ng-class="{underline:true}">';
			retHtml += '		{{COL_FIELD CUSTOM_FILTERS}}';
			retHtml += '	</a>';
			retHtml += '</div>';
			return retHtml;
		},
		productName : function() {
			var retHtml = '';
			retHtml += '<div ng-class="{invalid:grid.validate.isInvalid(row.entity,col.colDef)}" class="ui-grid-cell-contents" title="TOOLTIP"> ';
			retHtml += '	<a href="javascript:void(0);" ng-click="grid.appScope.func.popup.productName(row.entity.productId)" ng-class="{underline:true}">';
			retHtml += '		{{COL_FIELD CUSTOM_FILTERS}}';
			retHtml += '	</a>';
			retHtml += '</div>';
			return retHtml;
		},
		business : function() {
			var retHtml = '';
			retHtml += '<div ng-class="{invalid:grid.validate.isInvalid(row.entity,col.colDef)}" class="ui-grid-cell-contents" title="TOOLTIP"> ';
			retHtml += '	<a href="javascript:void(0);" ng-click="grid.appScope.func.popup.business(row.entity.businessId)" ng-class="{underline:true}">';
			retHtml += '		{{COL_FIELD CUSTOM_FILTERS}}';
			retHtml += '	</a>';
			retHtml += '</div>';
			return retHtml;
		},
		deliveryTracking : function() {
			var retHtml = '';
			retHtml += '<div ng-class="{invalid:grid.validate.isInvalid(row.entity,col.colDef)}" class="ui-grid-cell-contents" title="TOOLTIP"> ';
			retHtml += '	<a href="javascript:void(0);" ng-click="grid.appScope.func.popup.deliveryTracking(row.entity)" ng-class="{underline:true}">';
			retHtml += '		{{COL_FIELD CUSTOM_FILTERS}}';
			retHtml += '	</a>';
			retHtml += '</div>';
			return retHtml;
		},
		member : function(memberNo, memberId) {
			var retHtml = '';
			retHtml += '<div ng-class="{invalid:grid.validate.isInvalid(row.entity,col.colDef)}" class="ui-grid-cell-contents" title="TOOLTIP"> ';
			retHtml += '	<span ng-if="' + memberNo + '">';
			retHtml += '		<a href="javascript:void(0);" ng-click="grid.appScope.func.popup.member(' + memberNo + ')" ng-class="{underline:true}">';
			retHtml += '			{{COL_FIELD CUSTOM_FILTERS}}';
			retHtml += '			({{ ' + memberId + ' }})';
			retHtml += '		</a>';
			retHtml += '	</span>';
			retHtml += '	<span ng-if="!' + memberNo + '">';
			retHtml += '		{{COL_FIELD CUSTOM_FILTERS}}';
			retHtml += '	</span>';
			retHtml += '</div>';
			return retHtml;
		},
		date : function() {
			var retHtml = '';
			retHtml += '<div ng-class="{invalid:grid.validate.isInvalid(row.entity,col.colDef)}" class="ui-grid-cell-contents" title="TOOLTIP"';
			retHtml += '	row-idx="{{grid.options.getRowIdentity(row.entity)}}" ng-model="row.entity.omsRegulardeliveryschedules[0].regularDeliveryDt"';
			retHtml += '	date-format="yyyy-MM-dd" datetime-picker grid date-only regular-date min-date="{{row.entity.minDate}}" max-date="{{row.entity.maxDate}}">';
			retHtml += '</div>';
			return retHtml;
		},
		dateClass : function(grid, row, col, rowRenderIndex, colRenderIndex) {
			var cellcss = 'alignC';
			if (!grid.options.isRowSelectable(row)) {
				cellcss = 'alignC hide-date';
			}
			return cellcss;
		},
		// claimDate : function(module) {
		// var retHtml = '';
		// retHtml += '<div ng-class="{invalid:grid.validate.isInvalid(row.entity,col.colDef)}" class="ui-grid-cell-contents" title="TOOLTIP"> ';
		// retHtml += ' <a href="javascript:void(0);" ng-click="grid.appScope.func.popup.claim(row.entity)" ng-class="{underline:true}">';
		// retHtml += ' <span ng-if="row.entity.claimStateCd == \'CLAIM_STATE_CD.' + module + '\'">{{COL_FIELD CUSTOM_FILTERS}}</span>';
		// retHtml += ' </a>';
		// retHtml += '</div>';
		// return retHtml;
		// },
		claim : function() {
			var retHtml = '';
			retHtml += '<div ng-class="{invalid:grid.validate.isInvalid(row.entity,col.colDef)}" class="ui-grid-cell-contents" title="TOOLTIP"> ';
			retHtml += '	<a href="javascript:void(0);" ng-click="grid.appScope.func.popup.claim(row.entity)" ng-if="row.entity.claimNo && row.entity.orderId" ng-class="{underline:true}">';
			retHtml += '		{{row.entity.orderId + "_" + row.entity.claimNo}}';
			retHtml += '	</a>';			
			retHtml += '</div>';
			return retHtml;
		},
		address : function() {
			var retHtml = '';
			retHtml += '<div ng-class="{invalid:grid.validate.isInvalid(row.entity,col.colDef)}" class="ui-grid-cell-contents" title="TOOLTIP"> ';
			retHtml += '	<span class="address_overflow" ng-if="row.entity.omsRegulardelivery.deliveryZipCd">';
			retHtml += '		({{row.entity.omsRegulardelivery.deliveryZipCd}})';
			retHtml += '		{{row.entity.omsRegulardelivery.deliveryAddress1}} {{row.entity.omsRegulardelivery.deliveryAddress2}}';
			retHtml += '	</span>';
			retHtml += '	<button ng-click="grid.appScope.func.popup.zip(3,row)" type="btn_type2" style="float:right; position:absolute;" class="btn_type2" ';
//			retHtml += '			ng-class="{btn_disabled:!grid.options.isRowSelectable(row)}"';
//			retHtml += '			ng-disabled="!grid.options.isRowSelectable(row)">';
			retHtml += '			ng-if="grid.options.isRowSelectable(row)">';
			retHtml += '		<b>우편번호검색</b>';
			retHtml += '	</button>';
			retHtml += '</div>';
			return retHtml;
		},
		option : function() {
			var retHtml = '';
			retHtml += '<div ng-class="{invalid:grid.validate.isInvalid(row.entity,col.colDef)}" class="ui-grid-cell-contents" title="TOOLTIP"> ';
			retHtml += '	<span class="address_overflow">{{COL_FIELD CUSTOM_FILTERS}}</span>';
//			retHtml += '	<button type="btn_type2" style="margin-left:80;" class="btn_type2" ng-click="grid.appScope.func.popup.option(row.entity, grid.renderContainers.body.visibleRowCache.indexOf(row))"><b>옵션선택</b></button>';
			retHtml += '	<button ng-click="grid.appScope.func.popup.option(row)" type="btn_type2" style="float:right;" class="btn_type2" ';
			retHtml += '			ng-if="grid.options.isRowSelectable(row) && row.entity.orderProductTypeCd != \'ORDER_PRODUCT_TYPE_CD.SET\'">';
			retHtml += '		<b>옵션선택</b>';
			retHtml += '	</button>';
			retHtml += '</div>';
			return retHtml;
		},
		// tree : function() {
		// var retHtml = '';
		// retHtml += '<div class="ui-grid-cell-contents" style="padding-left:20px;" title="TOOLTIP">';
		// retHtml += ' <div style="float:left;" class="ui-grid-tree-base-row-header-buttons" ng-class="{\'ui-grid-tree-base-header\': row.entity.treeLevel > -1 }" ng-click="grid.appScope.toggleRow(row,evt)">';
		// retHtml += ' <i ng-class="{\'ui-grid-icon-plus-squared\': row.entity.treeLevel > 0}" ';
		// retHtml += ' ng-style="{\'padding-left\': grid.options.treeIndent * row.entity.treeLevel + \'px\'}"></i>';
		// retHtml += ' </div>';
		// retHtml += ' {{COL_FIELD CUSTOM_FILTERS}}';
		// retHtml += '</div> ';
		// return retHtml;
		// },
		tree : function() {
			var retHtml = '';
			retHtml += '<div class="ui-grid-cell-contents" style="padding-left:20px;" title="TOOLTIP">';
			retHtml += '	<div style="float:left;" class="ui-grid-tree-base-row-header-buttons" ng-class="{\'ui-grid-tree-base-header\': row.entity.treeLevel > -1 }" ng-click="grid.appScope.toggleRow(row,evt)">';
			// retHtml += ' <i ng-class="{\'ui-grid-icon-plus-squared\': row.entity.treeLevel > 0 && row.entity.orderProductStateCd != \'ORDER_PRODUCT_STATE_CD.CANCEL\', ';
			// retHtml += ' \'ui-grid-icon-minus-squared\': row.entity.treeLevel > 0 && row.entity.orderProductStateCd == \'ORDER_PRODUCT_STATE_CD.CANCEL\'}" ';
			retHtml += '		<i ng-class="{\'ui-grid-icon-plus-squared\': row.entity.treeLevel > 0 ,\'ui-grid-icon-minus-squared\': row.entity.treeLevel > 0 && row.entity.orderProductStateCd == \'ORDER_PRODUCT_STATE_CD.CANCEL\'}" ';
			retHtml += '			ng-style="{\'padding-left\': grid.options.treeIndent * row.entity.treeLevel + \'px\'}"></i>';
			retHtml += '	</div>';
			retHtml += '	{{COL_FIELD CUSTOM_FILTERS}}';
			retHtml += '</div> ';
			return retHtml;
		},
		updCancel : function() {
			var retHtml = '';
			retHtml += '<div ng-class="{invalid:grid.validate.isInvalid(row.entity,col.colDef)}" class="ui-grid-cell-contents" title="TOOLTIP"> ';
			retHtml += '	{{row.entity.pickupProductStateCd == "PICKUP_PRODUCT_STATE_CD.CANCEL" ? COL_FIELD : ""}}';
			retHtml += '</div>';
			return retHtml;			
		},
		addPrice : function() {
			return '<div class="ui-grid-cell-contents" title="TOOLTIP">{{COL_FIELD - grid.appScope.orgAddSalePrice CUSTOM_FILTERS}}</div>';
		},
		//20161114:포장지변경
		dctype : function() {// row.getProperty(col.field)
			var retHtml = '';
			retHtml += '<div class="ui-grid-cell-contents" title="TOOLTIP">';
			retHtml += '	<span style="float: left;">';
			retHtml += '		{{COL_FIELD}} ';
			retHtml += '	</span>';
			retHtml += '	<span style="float: right; margin-left:5px;">';
			retHtml += '		({{row.entity.dcApplyTypeName}}<b>:</b>';
			retHtml += '		{{row.entity.dcValue| number:0}}{{row.entity.dcApplyTypeCd == "DC_APPLY_TYPE_CD.AMT" ? "원": "%"}})';
			retHtml += '	</span>';
			retHtml += '</div>';
			return retHtml;
		},
//  		claimQty : function(a) {
////			var retHtml = '';
////			retHtml += '<div ng-class="{invalid:grid.validate.isInvalid(row.entity,col.colDef)}" class="ui-grid-cell-contents" title="TOOLTIP"> ';
////			retHtml += ' {{row.entity.orderProductTypeCd == "ORDER_PRODUCT_TYPE_CD.GENERAL" ? COL_FIELD : ""}}';
////			retHtml += '</div>';
////			return retHtml;
//  			console.log('appScope : ', a);
//  			console.log('appScope.cancelType : ', a.$parent.$parent.grid.appScope.cancelType);
//  			return a.cancelType != 'CANCELALL' ? true : false;
////  			 return true;
//		},
		dropdown : function() {
			var retHtml = '';
			retHtml += '<div>';
			retHtml += '	<form name="inputForm">';
			retHtml += '		<select ng-class="\'colt\' + col.uid" ui-grid-edit-dropdown ng-model="MODEL_COL_FIELD" ';
			retHtml += '				ng-change="grid.appScope.func.toggleClaimReasonText(row.entity,row.grid.columns)" ';
//			retHtml += '				ng-change="grid.appScope.func.toggleClaimReasonText(row,row.entity,col,col.colDef)" ';
			retHtml += '				ng-options="field[editDropdownIdLabel] as field[editDropdownValueLabel] CUSTOM_FILTERS for field in editDropdownOptionsArray">';
			retHtml += '		</select>';
			retHtml += '	</form>';
			retHtml += '</div>';
			return retHtml;
		},
		sameReason : function() {
			var retHtml = '';
			retHtml += '<div class="ui-grid-cell-contents" title="TOOLTIP">';
			retHtml += '	<button ng-click="grid.appScope.func.sameReason(row.entity, grid)" type="btn_type2" class="btn_type2 alignC" ng-if="grid.options.isRowSelectable(row)">';
			retHtml += '		<b>전체사유동일</b>';
			retHtml += '	</button>';
			retHtml += '</div>';
			return retHtml;
		},
		checkBox : function() {
			var retHtml = '';
			retHtml += '<div style="text-align:center;">';
			retHtml += '	<input type="checkbox" ng-model="row.entity.checked" ng-click="grid.appScope.selectCheck(grid,row.entity)"';
			retHtml += '		    ng-disabled="!grid.options.isRowSelectable(row)" ng-style="{\'cursor\': row.cursor,\'margin\':\'9px 0 0 0\'}">';
			retHtml += '</div>';
			return retHtml;
		},
		checkBoxHeader : function() {
			var retHtml = "";
			retHtml += "<div role=\"columnheader\" ng-class=\"{ 'sortable': sortable }\" ui-grid-one-bind-aria-labelledby-grid=\"col.uid + '-header-text ' + col.uid + '-sortdir-text'\" aria-sort=\"{{col.sort.direction == asc ? 'ascending' : ( col.sort.direction == desc ? 'descending' : (!col.sort.direction ? 'none' : 'other'))}}\" ng-style=\"{'position': 'relative'}\">";
//			retHtml += "      <div class=\"check_header\" ng-style=\"{'position': 'absolute', 'left' : '16px'}\">";
//			retHtml += "            <input type=\"checkbox\" ng-click=\"grid.appScope.checkBoxAll(grid, $event)\" ng-style=\"{'cursor': 'pointer'}\"/>";
//			retHtml += "      </div>";
			retHtml += "      <div ng-style=\"{'text-align': 'right'}\" role=\"button\" tabindex=\"0\" class=\"ui-grid-cell-contents ui-grid-header-cell-primary-focus\" col-index=\"renderIndex\" title=\"TOOLTIP\">";
			retHtml += "            <span ui-grid-one-bind-id-grid=\"col.uid + '-sortdir-text'\" ui-grid-visible=\"col.sort.direction\" aria-label=\"{{getSortDirectionAriaLabel()}}\">";
			retHtml += "                  <i ng-class=\"{ 'ui-grid-icon-up-dir': col.sort.direction == asc, 'ui-grid-icon-down-dir': col.sort.direction == desc, 'ui-grid-icon-blank': !col.sort.direction }\" title=\"{{isSortPriorityVisible() ? i18n.headerCell.priority + ' ' + ( col.sort.priority + 1 )  : null}}\" aria-hidden=\"true\"></i>";
			retHtml += "                  <sub ui-grid-visible=\"isSortPriorityVisible()\" class=\"ui-grid-sort-priority-number\">{{col.sort.priority + 1}}</sub>";
			retHtml += "            </span>";
			retHtml += "      </div>";
			retHtml += "      <div role=\"button\" tabindex=\"0\" ui-grid-one-bind-id-grid=\"col.uid + '-menu-button'\" class=\"ui-grid-column-menu-button\" ng-if=\"grid.options.enableColumnMenus && !col.isRowHeader  && col.colDef.enableColumnMenu !== false\" ng-click=\"toggleMenu($event)\" ng-class=\"{'ui-grid-column-menu-button-last-col': isLastCol}\" ui-grid-one-bind-aria-label=\"i18n.headerCell.aria.columnMenuButtonLabel\" aria-haspopup=\"true\">";
			retHtml += "      		 <i class=\"ui-grid-icon-angle-down\" aria-hidden=\"true\">&nbsp;</i></div><div ui-grid-filter>";
			retHtml += "      </div>";
			retHtml += "</div>";
			return retHtml;
		}
	}
});
// 주문 서비스
omsServiceModule.service('orderService', function(restFactory) {
	// var param = {id: '@orderId'};
	var param = null;

	return {
		selectList : function(data, callback) {
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, '/api/oms/order/list', param, data, callback);
		},
		selectOne : function(data, callback) {
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, '/api/oms/order', param, data, callback);
		},
		insert : function(data, callback) {
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, '/api/oms/order', param, data, callback);
		},
		update : function(data, callback) {
//			return restFactory.transaction(Rest.method.PUT, Rest.responseType.TEXT, '/api/oms/order', param, data, callback);
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.SINGLE, '/api/oms/order', param, data, callback);
		},
		selectMemo : function(data, callback) {
			return restFactory.transaction(Rest.method.GET, Rest.responseType.MULTI, '/api/oms/order/memo', param, data, callback);
		},
		insertMemo : function(data, callback) {
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, '/api/oms/order/memo', param, data, callback);
		}
	}
});
// 배송 서비스
omsServiceModule.service('deliveryService', function(restFactory) {
	// var param = {id: '@orderId'};
	var param = null;

	return {
		selectList : function(data, callback) {
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, '/api/oms/delivery/list', param, data, callback);
		},
		selectOne : function(data, callback) {
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, '/api/oms/delivery', param, data, callback);
		},
		update : function(data, callback) {
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.TEXT, '/api/oms/delivery', param, data, callback);
		}
	}
});
// 결제 서비스
omsServiceModule.service('paymentService', function(restFactory) {
	// var param = {id: '@orderId'};
	var param = null;

	return {
		selectList : function(data, callback) {
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, '/api/oms/payment/list', param, data, callback);
		},
		selectOne : function(data, callback) {
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, '/api/oms/payment', param, data, callback);
		},
		insert : function(data, callback) {
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, '/api/oms/payment', param, data, callback);
		},
		update : function(data, callback) {
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.SINGLE, '/api/oms/payment', param, data, callback);
		},
		accountCertify : function(data, callback) {
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, '/api/oms/payment/account/certify', param, data, callback);
		}
	}
});
// 픽업 서비스
omsServiceModule.service('pickupService', function(restFactory) {
	// var param = {id: '@orderId'};
	var param = null;

	return {
		selectOne : function(data, callback) {
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, '/api/oms/pickup', param, data, callback);
		},
		// insert : function(data, callback) {
		// return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
		// },
		update : function(data, callback) {
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.TEXT, '/api/oms/pickup', param, data, callback);
		},
		cancel : function(data, callback) {
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.SINGLE, '/api/oms/pickup/cancel', param, data, callback);
		}
	}
});
// 정기배송 서비스
omsServiceModule.service('regularService', function(restFactory) {
	// var param = {id: '@orderId'};
	var param = null;

	return {
		selectOne : function(data, callback) {
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, '/api/oms/regular', param, data, callback);
		},
		// insert : function(data, callback) {
		// return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, '/api/oms/regular', param, data, callback);
		// },
		update : function(data, callback) {
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.TEXT, '/api/oms/regular', param, data, callback);
		},
		cancel : function(data, callback) {
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.TEXT, '/api/oms/regular/cancel', param, data, callback);
		}
	}
});
// 클레임 서비스
omsServiceModule.service('claimService', function(restFactory) {
	// var param = {id: '@orderId'};
	var param = null;

	return {
 		selectList:function(data, callback) {
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, '/api/oms/claim/list', param, data, callback);
		},
		selectOne : function(data, callback) {
			return restFactory.transaction(Rest.method.GET, Rest.responseType.TEXT, '/api/oms/claim', param, data, callback);
		},
		insert : function(data, callback) {
			// 취소,교환,반품,재배송,옵션변경
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, '/api/oms/claim', param, data, callback);
		},
		update : function(data, callback) {
			// 취소,교환,반품,재배송,옵션변경
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.SINGLE, '/api/oms/claim', param, data, callback);
		},
		complete : function(data, callback) {
			// 취소,교환,반품,재배송,옵션변경
			return restFactory.transaction(Rest.method.POST, Rest.responseType.SINGLE, '/api/oms/claim/complete', param, data, callback);
		}
	}
});
//주문업로드설정 서비스
omsServiceModule.service('uploadConfService', function(restFactory) {
	var param = null;
	return {
		//외부몰,중국몰 사이트 조회
		getExternalSiteList : function(callback){
			var url = Rest.context.path + "/api/oms/uploadconf/siteList";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, param, null, callback);
		},
		//업로드설정 상세팝업
		openUploadconfDetailPopup : function() {
			var url = Rest.context.path + "/oms/uploadconf/popup/detail";
			popupwindow(url, "Uploadconfig Detail", 1200, 500);
		},
		//업로드설정 상세정보
		getUploadconfDetail : function(siteId, callback) {
			var param = { siteId : siteId };
			var url = Rest.context.path + "/api/oms/uploadconf/popup/detail/:siteId";
			return restFactory.transaction(Rest.method.GET, Rest.responseType.SINGLE, url, param, null, callback);
		},
		//업로드설정 변경
		updateUploadconf : function(data, callback) {
			var url = Rest.context.path + "/api/oms/uploadconf/popup/update";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback);
		},
		//중복여부 체크
		checkDuplication : function(data, callback) {
			var url = Rest.context.path + "/api/oms/uploadconf/popup/checkDup";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback);
		},
		//업로드설정 등록
		insertUploadconf : function(data, callback) {
			var url = Rest.context.path + "/api/oms/uploadconf/popup/insert";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback);
		}
	}
});
//주문업로드 서비스
omsServiceModule.service('uploadExcelService', function(restFactory) {
	var param = null;
	return {
		//엑셀주문 등록
		insertExcelOrder : function(data, callback) {
			var url = Rest.context.path + "/api/oms/uploadexcel/insertOrder";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, null, data, callback);
		}
	}
});
//사방넷 주문 미처리 관리 서비스
omsServiceModule.service('termOrderService', function(restFactory) {
	var param = null;
	return {
		//사방넷, 중국몰 사이트 리스트
		getExternalSiteList : function(callback) {
			var url = Rest.context.path + "/api/oms/termorder/siteList";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, param, null, callback);
		},
		//사방넷->터미널
		receiveSbnOrder : function(data, callback) {
			var url = Rest.context.path + "/api/oms/termorder/receiveSbn";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
		},
		//터미널(사방넷)-> BO
		sbnOrderToBo : function(callback) {
			var url = Rest.context.path + "/api/oms/termorder/insertSbnOrder";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, null, callback);
		},
		//Tmall -> 터미널
		receiveTmallOrder : function(data, callback) {
			var url = Rest.context.path + "/api/oms/termorder/receiveTmall";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
		},
		//터미널(Tmall)-> BO
		tmallOrderToBo : function(callback) {
			var url = Rest.context.path + "/api/oms/termorder/insertTmallOrder";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, null, callback);
		},
		//선택한 Grid 삭제
		deleteReceiveOrder : function(data, callback) {
			var url = Rest.context.path + "/api/oms/termorder/delete";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
		},
		//선택한 Grid 저장
//		updateReceiveOrder : function(data, callback) {
//			var url = Rest.context.path + "/api/oms/termorder/update";
//			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
//		}
	}
});
// 배송관리 서비스
omsServiceModule.service('logisticsService', function(restFactory) {

	var url = Rest.context.path + '/api/oms/logistics';
	var param = null;

	return {
		getAllLocationList : function(callback) {
			var url = Rest.context.path + "/api/oms/logistics/location/allList";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, param, null, callback);
		},
		getAllSiteList : function(callback) {
			var url = Rest.context.path + "/api/oms/logistics/siteList";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, param, null, callback);
		},
		getAllWarehouseList : function(callback) {
			var url = Rest.context.path + "/api/oms/logistics/warehouseList";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, param, null, callback);
		},
		approval : function(type, data, callback) {
			var url = Rest.context.path + '/api/oms/logistics/approval/' + type;
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.SINGLE, url, param, data, callback);
		},
		partnerApproval : function(data, callback) {
			var url = Rest.context.path + '/api/oms/logistics/partner/approval';
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.SINGLE, url, param, data, callback);
		},
		download : function(type, callback) {
			var url = Rest.context.path + '/api/oms/logistics/invoice/' + type;
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.SINGLE, url, param, null, callback);
		},
		sendData : function(data, callback) {
			var url = Rest.context.path + '/api/oms/logistics/sendData';
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.SINGLE, url, param, data, callback);
		},
		cancel : function(data, callback) {
			var url = Rest.context.path + '/api/oms/logistics/cancel';
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.SINGLE, url, param, data, callback);
		},
		updateLocationId : function(data, callback) {
			var url = Rest.context.path + '/api/oms/logistics/location/mapping/update';
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.SINGLE, url, param, data, callback);
		},
		shippingConfirm : function(data, callback) {
			var url = Rest.context.path + '/api/oms/logistics/shipping/confirm/';
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.SINGLE, url, param, data, callback);
		},
		returnConfirm : function(data, callback) {
			var url = Rest.context.path + '/api/oms/logistics/return/confirm';
			return restFactory.transaction(Rest.method.PUT, Rest.responseType.SINGLE, url, param, data, callback);
		}
	}
});
// 예치금관리 서비스
omsServiceModule.service('depositService', function(restFactory) {
	var param = null;
	return {
		saveDeposit : function(data, callback) {
			var url = Rest.context.path + "/api/oms/deposit/save";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.TEXT, url, param, data, callback);
		}
	}
});
//매출정산 서비스
omsServiceModule.service('settleService', function(restFactory) {
	var param = null;
	return {
		//채널리스트
		getAllChannelList : function(callback) {
			var url = Rest.context.path + "/api/oms/settle/channelList";
			return restFactory.transaction(Rest.method.POST, Rest.responseType.MULTI, url, param, null, callback);
		}
	}
});