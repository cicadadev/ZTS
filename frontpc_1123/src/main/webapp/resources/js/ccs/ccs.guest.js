
// 1. 주문
var $order = {
		// 1.1 (order.submit) common submit
		submit : function(method, data, url, callback_success, callback_send, callback_error) {
			$.ajax({ 
				type : method, 
				data : data,
				url : url, 
				beforeSend : function(xhr) {
					if (callback_send != undefined) {
						callback_send(xhr);
					} else {
						common.showLoadingBar();
					}
				} 
			}).done(function(data, status, xhr) {
				common.hideLoadingBar();
				if (callback_success != undefined) {
					callback_success(data);
				}
			}).fail(function(xhr, status, error) {
				common.hideLoadingBar();
				if (callback_error != undefined) {
					callback_error(error);
				}
			}).always(function() {
				common.hideLoadingBar();
			});
		},
		// 1.2 (order.callback.) callback function
		callback : {
			delivery : function(address) {
				$('#layerTitle').text('배송지 변경');
				$('#regBtn, #modifyBtn, .chkBox').remove();
				$('#changeBtn, .rw_tb_tbody2 li:eq(4)').show();
				
				$('[name="deliveryName1"]').attr('name','name1').val(address.name1);
				$('[name="zipCd"]').val(address.zipCd);
				$('[name="address1"]').val(address.address1);
				$('[name="address2"]').val(address.address2);
				$('[name="note"]').val(address.note);
				
				$('#addrInputForm')
				.append($('<input>', { 'name' : 'orderId', 'type' : 'hidden', 'value' : address.orderId }))
				.append($('<input>', { 'name' : 'deliveryAddressNo', 'type' : 'hidden', 'value' : address.deliveryAddressNo }));
				
				ccs.common.telset('tel', address.phone1);
				ccs.common.telset('cell', address.phone2);
				mypage.deliveryAddress.fnPlaceholder();
				mypage.deliveryAddress.fnChkStyle();
			},
			address : function(address) {
				$('[name="name1"]').val(address.deliveryName1);
				$('[name="zipCd"]').val(address.zipCd);
				$('[name="address1"]').val(address.address1);
				$('[name="address2"]').val(address.address2);
				// $('[name="phone1"]').val(address.phone1);
				// $('[name="phone2"]').val(address.phone2);
				ccs.common.telset('tel', address.phone1);
				ccs.common.telset('cell', address.phone2);
			},
			beforesend : function($target) {
				$target.append('<div id="lodingBar" class="loadingBar_mo" align="center"><img src="/resources/img/mobile/Loading.gif" alt="" /></div>');
			}
		},
		// 1.5 (order.setdata.) data setting on the jsp page
		setdata : {
			delivery : function($this) {
				$this.siblings('label').text($('option:selected', $this).text());
				$('[name="note"]').val($('option:selected', $this).text());
				// $this.trigger('change');
			}
		},
		// 1.6 (order.update.) update data		
		update : {
			delivery : function($this) {
				var phone1 = $('#tel_areaCode').text() + '-' + $('input[name=tel_num1]').val() + '-' + $('input[name=tel_num2]').val();
				var phone2 = $('#cell_areaCode').text() + '-' + $('input[name=cell_num1]').val() + '-' + $('input[name=cell_num2]').val();
				
				$('[name="phone1"]').val(phone1);
				$('[name="phone2"]').val(phone2);
				var frmObj = $this.closest('form');
				// if (!mypage.deliveryAddress.formValidation()) {	// TODO
				// return false;
				// }
				
				$order.submit('post', frmObj.serialize(), '/api/ccs/guest/order/update/delivery', function(response) {
					if (!common.isEmptyObject(response)) {
						alert('배송정보가 성공적으로 변경 되었습니다.');
						var address = response;
						$('.btn_changeAddr').data('name1', address.name1);
						$('.btn_changeAddr').data('phone1', address.phone1);
						$('.btn_changeAddr').data('phone2', address.phone2);
						$('.btn_changeAddr').data('zipCd', address.zipCd);
						$('.btn_changeAddr').data('address1', address.address1);
						$('.btn_changeAddr').data('address2', address.address2);
						$('.btn_changeAddr').data('note', address.note);
						
//						var innerHtml = address.phone2 + '&nbsp;&nbsp;|&nbsp;&nbsp;' + address.phone1 + '<br />';
						var innerHtml = address.phone2 + '<i class="bar">|</i>' + address.phone1 + '<br />';
						innerHtml += '<em>(' + address.zipCd + ')&nbsp;' + address.address1 + '&nbsp;';
						innerHtml += address.address2+'&nbsp;</em><br />' + address.note;
						$('.btn_changeAddr').prev().html(innerHtml);

						$('.pc_btn_close').trigger('click');
					} else {
						alert('배송정보 변경 중 에러가 발생 하였습니다.');
					}
				});
			},
			option : function(data) {
				var callback_param = oms.changeOptionLayer.callback_param;
				var $trBox = $('.tr_idx').eq(callback_param.trIdx - 1);
				var $stateBox = $trBox.find('.stateBox');
				var $optionBox = $trBox.find('.option_txt');
				
				if ($optionBox.data('addSalePrice') == data.addSalePrice) {
					if ($optionBox.data('orderQty') > data.realStockQty) {
						alert('선택한 단품은 재고수량이 부족합니다.');
						return false;
					}
					var product = $optionBox.data();
					product.saleproductId = data.saleproductId;
					product.saleproductName = data.name;
					var list = [];
					list.push(product);
					
					mms.common.restfulTrx('/api/ccs/guest/order/update/option', 'post', list, function(response) {
						if (response.success) {
							alert('주문상품 옵션이 성공적으로 변경 되었습니다.');
							var retObj = response.returnObject[0];
							
							$stateBox.data('saleproductId', retObj.saleproductId)
							$optionBox.data('saleproductId', retObj.saleproductId)
							$optionBox.html('<i>' + retObj.saleproductName + '</i>')
							$stateBox.css('padding-bottom', '');
							$('.btn_tb_option').removeClass('on');
							$('.ly_box .btn_close').trigger('click');
						} else if (!response.success) {
							alert(response.resultMessage);
						} else {
							alert('주문상품 옵션 변경 중 에러가 발생 하였습니다.');
						}
					});
				} else {
					alert('가격이 동일한 옵션으로 만 변경이 가능합니다.');
					return false;
				}
			},
			option1 : function(data) {
				var callback_param = oms.changeOptionLayer.callback_param;
				var $trBox = $('.tr_idx').eq(callback_param.trIdx - 1);
				var $stateBox = $trBox.find('.stateBox');
				var $optionBox = $trBox.find('.option_txt');

				var hasError = false;
				var errorProduct = '';
				$trBox.find('.option_txt').each(function(idx1, obj1) {
					$stateBox.find('[name="optionvalue"]').each(function(idx2, obj2){
						$option = $('option:selected', $(obj2));
						if ($(obj1).data('productId') == $option.data('productId')) {
							if ($(obj1).data('addSalePrice') != $option.data('addSalePrice')) {
								errorProduct = $option.data('setName');
								hasError = true;
								return false;
							} else {
								$option.data('orderId', $(obj1).data('orderId'));
								$option.data('orderProductNo', $(obj1).data('orderProductNo'));
								$option.data('orderQty', $(obj1).data('orderQty'));
								$option.data('cancelQty', $(obj1).data('cancelQty'));
								$option.data('returnQty', $(obj1).data('returnQty'));
							}
						}
					});
				});
				
				if (!hasError) {
					var list = [];
					$stateBox.find('[name="optionvalue"]').each(function(idx, obj2){
						$option = $('option:selected', $(obj2));
						var product = $option.data();
						list.push(product);
					});
					
					mms.common.restfulTrx('/api/ccs/guest/order/update/option', 'post', list, function(response) {
						if (response.success) {
							alert('주문상품 옵션이 성공적으로 변경 되었습니다.');
							$trBox.find('.option_txt').each(function(idx1, obj1) {
								$stateBox.find('[name="optionvalue"]').each(function(idx2, obj2) {
									$option = $('option:selected', $(obj2));
									if ($(obj1).data('productId') == $option.data('productId')) {
										$(obj1).data('saleproductId', $option.data('saleproductId'));
										$(obj1).find('span').html($option.data('saleproductName'));
									}
								});
							});
							$stateBox.css('padding-bottom', '');
							$('.btn_tb_option').removeClass('on');
							$('.ly_box .btn_close').trigger('click');
						} else if (!response.success) {
							alert(response.resultMessage);
						} else {
							alert('주문상품 옵션 변경 중 에러가 발생 하였습니다.');
						}
					});
				} else {
					alert(errorProduct + '는 가격이 동일한 옵션으로 만 변경이 가능합니다.');
					return false;
				}
			},
			review : function($this) {
				ccs.layer.reviewLayer.open($this.closest('.stateBox').data(), function() {
					$this.remove();
				});
			}
		},
		// 1.7 (order.change.) change page
		change : {
			delivery : function($this) {
				ccs.layer.deliveryAddressLayer.open($this.data(), $order.callback.delivery);
			},
			option : function($this, heightYn) {
				$(".mobile .stateBox .btn_tb_option").off("click");
				var $stateBox = $this.closest('.stateBox');
				var param = {	// 옵션변경할 상품(단일,세트)
					productId : $stateBox.data('productId'),
					saleproductId : $stateBox.data('saleproductId'),
					trIdx : $stateBox.data('trIdx')
				}
				var isSet = '';
				if ($stateBox.data('orderProductTypeCd') == 'ORDER_PRODUCT_TYPE_CD.SET') {
					isSet = '1';
				}
				var optLayer = $this.next();
				$order.submit('get', param, '/pms/product/option/change/ajax' + isSet, function(data, status, xhr) {
					var layerCon = true;
					if (global.channel.isMobile == "true") {
						var flag = $this.hasClass("on");
			
						if (typeof flag == 'undefined' || !flag) {
							optLayer.html($(data));
							$this.addClass("on");
							if (heightYn == null || heightYn == undefined) {
								var optionH = optLayer.outerHeight() - 40;
								$stateBox.css('padding-bottom', optionH + 'px');
							}
						} else {
							$order.close();
							$stateBox.css('padding-bottom', '');
							$this.removeClass('on');
							layerCon = false;
						}
					} else {
						optLayer.html($(data));
					}
			
					if (layerCon) {
						optLayer.find('.title').html('옵션변경');
						optLayer.find('.user_action dl').remove();
			
						// 추가 금액이 다른 옵션 제거
						var $trBox = $this.closest('.tr_idx');
						$trBox.find('.option_txt').each(function(idx1, obj1) {
//							console.log('obj1.addSalePrice : ', $(obj1).data('addSalePrice'));
							$stateBox.find('[name="optionvalue"]').each(function(idx2, obj2) {
								$(obj2).children().each(function(idx3, obj3) {
									if ($(obj1).data('productId') == $(obj3).data('productId')) {
										if ($(obj1).data('saleproductId') == $(obj3).data('saleproductId')) {
											$(obj3).prop('selected', true);
											$(obj3).parent().trigger('change');
										} else {
											if ($(obj1).data('addSalePrice') != $(obj3).data('addSalePrice')) {
												$(obj3).remove();
											}
										}
									}
								});
							});
						});
			
						oms.changeOptionLayer.callback_param = param; // 콜백함수에 필요한 파라미터
						oms.changeOptionLayer.callback = (isSet != '1' ? $order.update.option : $order.update.option1); // 변경버튼 콜백정의
					}
				}, function(xhr) {
					optLayer.html(ccs.common.onLoadging);
					optLayer.toggle();
				}, function(xhr, status, error) {
					optLayer.toggle();
				});
			},
			// 클레임 교환의 옵션리스트 세팅
			optionpage : function(productId, saleproductId, orderProductTypeCd) {
				var isSet = (orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SET' ? '1' : '');
				var param = {	// 옵션변경할 상품(단일,세트)
					productId : productId,
					saleproductId : saleproductId,
					searchId : '/ccs/common/layer/optionChangePage' + isSet
				}
				$order.submit('get', param, '/pms/product/option/change/ajax' + isSet, function(data, status, xhr) {
					$('.optList').html($(data).filter('.optList').html());
					$('.optList').each(function(idx1, obj1) {
						$(obj1).find('.select_box1').each(function(idx2, obj2) {
							$(obj2).addClass('type1'); // 가운데 정렬
						});
						
						$(obj1).find('[name="optionvalue"]').each(function(idx2, obj2) {
							$(obj2).children().each(function(idx3, obj3) {
								$('.positionR').find('.option_txt').each(function(idx4, obj4){
									if ($(obj4).data('productId') == $(obj3).data('productId')) {
										if ($(obj4).data('saleproductId') == $(obj3).data('saleproductId')) {
											$(obj3).prop('selected', true);
//											$(obj3).parent().trigger('change');
											$(obj3).parent().prev().html($(obj3).data('saleproductName'));
										} else {
											if ($(obj4).data('addSalePrice') != $(obj3).data('addSalePrice')) {
												$(obj3).remove();
											}
										}
									}
								});
							});
						});
						
					});
				});
			}
		},
 		layer : {
 			delivery : function($this) {
				ccs.layer.open('/ccs/guest/order/layer/deliveryTracking', $this.closest('.stateBox').data(), 'deliveryTracking', function(result) {
					$('#deliveryTracking').show();
					$('#deliveryTracking').find('.positionR').html($this.closest('.tr_box').find('.positionR').html());
				});
 			},
 			payment : function($this) {
				ccs.layer.open('/ccs/guest/order/layer/payment', $this.data(), 'addPayment', function(result) {
					$('#addPayment').show();
					$('#addPayment').find('input[name="paymentMethodCd"]').eq(0).trigger('click');
					$.getScript('/resources/js/common/common.ui.js', function(data, textStatus, jqxhr) {
						var importJs = '/resources/js/pc.js';
						if (global.channel.isMobile == "true") {
							importJs = '/resources/js/mo.js';
						}
						$.getScript(importJs, function(data1, textStatus1, jqxhr1) {});
					});
				});
 			},
 			claim : function($this, $stateBox) {
 				ccs.layer.open('/ccs/guest/order/layer/' + $this.data('claimType'), $stateBox.data(), 'orderCancel', function(result) {
 					$('#orderCancel').show();
 					$('#orderCancel').find('.positionR').html($this.closest('.tr_box').find('.positionR').html());
 					$.getScript('/resources/js/common/common.ui.js', function(data, textStatus, jqxhr) {
 						var importJs = '/resources/js/pc.js';
 						if (global.channel.isMobile == "true") {
 							importJs = '/resources/js/mo.js';
 						}
 						$.getScript(importJs, function(data1, textStatus1, jqxhr1) {});
 					});
 				});
 			}
		},
		close : function() {
			$('button.btn_x').trigger('click');
		}
}
// 2. 클레임
var	$claim = {
		request : function($this, isAll) {
			if (common.isEmpty(isAll)) {
				if ($this.data('claimType') == 'cancel' || $this.data('claimType') == 'return') {
					var mod = ($this.data('claimType') == 'cancel' ? '취소가' : '반품이');
					if ($this.data('isPartial') == 'N') {
						alert('부분' +mod+ ' 불가능한 주문입니다.' + ($this.data('claimType') == 'cancel' ? '\n전체취소로 진행해 주십시오.' : '\n고객센터로 문의해 주십시오.'));
						return false;
					}
//					if ($this.data('isSet')) {
//						alert('부분' +mod+ ' 불가능한 상품입니다.\n전체취소로 진행해 주십시오.');
//						return false;
//					}
				}
			}
			common.showLoadingBar();
			var $stateBox = !common.isEmpty(isAll) ? $this : $this.closest('.stateBox');  
//			$('<form>', { 'id':'tmpform', 'action':'/ccs/guest/order/' + $this.data('claimType'), 'method':'post' })
// 			.append($('<input>', { 'name':'orderId', 'value':$stateBox.data('orderId'), 'type':'hidden' }))
// 			.append($('<input>', { 'name':'orderProductNo', 'value':$stateBox.data('orderProductNo'), 'type':'hidden' }))
// 			.appendTo(document.body).submit();
			
			$order.layer.claim($this, $stateBox);
			common.hideLoadingBar();
		},
 		cancel:function($this) {
			if ($this.data('claimType') == 'pickup') {
				mms.common.restfulTrx('/api/ccs/guest/claim/pickup', 'post', $this.data(), function(response) {
					if (response.success) {
						alert('매장픽업 신청이 취소 되었습니다.');
						if ($this.data('page') == 'list') {
							$order.search.pickup($('#area2'));
						} else {
							window.location.reload();
						}
					} else if (!response.success) {
						alert(response.resultMessage);
					} else { 
						alert('매장픽업 신청 중 에러가 발생 하였습니다.');
					}
				});
			} else {
				if (!this.check.qty($this.data('title'))) {
					return;
				}
				$order.submit('post', $('#omsClaimWrapper').serialize(), '/api/ccs/guest/claim', function(response) {
					console.log('response : ', response);
					if (response.success) {
						alert($this.text() + '을(를) 완료 하였습니다.');
						location.href = document.referrer;
					} else {
						alert(response.resultMessage);
					}
				});
			}
		},
  		exchange : function($this) {
			if (!this.check.qty('교환')) {
				return false;
			}
			if (!common.isEmpty($this.data('confirm'))) {
				var confirm = $this.data('confirm');
				if (confirm == '0') {
					alert('추가결제 비용을 확인 해주십시오.');
					returnfalse = true;
				} else if (confirm == '1') { // 결제
					$payment.invoke($this, 'insert', true);
				} else if (confirm == '2') { // 결제없이 바로
					$payment.exchange();
				}
			}
		},
  		check : {
   			qty : function(title) {
				var onClaim = true;
				$('select[name$="].omsClaimproduct.claimReasonCd"]').each(function(idx, obj) {
					if ($(obj).is(':enabled') && common.isEmpty($(obj).val())) {
						onClaim = false;
						return false;
					}
				});
				$('select[name$="].omsClaimproduct.claimQty"]').each(function(idx, obj) {
					if ($(obj).is(':enabled') && (common.isEmpty($(obj).val()) || $(obj).val() < 1)) {
						onClaim = false;
						return false;
					}
				});
				if (!onClaim) {
					alert(title + '수량과 사유를 선택하여 주십시오.');
				}
				return onClaim;
			},
  			penalty : function($this) {
				if (!this.qty($this.data('title'))) {
					return;
				}
		
				$('.payment_info').hide();
				$('#payment_area').hide();
//				$('#omsClaimWrapper').find('[name="addfee"]').remove();
				var hasPenalty = true;
				var showflag = '2';
				$('select[name$="].omsClaimproduct.claimReasonCd"]').each(function(idx, obj) {
					if ($(obj).is(':enabled')) {
						var reasonCd = ($(obj).val()).replace('CLAIM_REASON_CD.', '');
						if ('CHANGE,DESIGN,PRICE,TYPING'.indexOf(reasonCd) < 0) { // 섞이면 구매자귀책
							hasPenalty = false;
						}
					}
				});
				if (hasPenalty) {// 배송비 부과케이스
					var addfee = $this.data('fee');
					if (!common.isEmpty(addfee) && addfee > 0) {
						showflag = '1';
					}
				}
				if (showflag == '1') {
					$('.payment_info').show();
					$('#payment_area').show();
//					$('#omsClaimWrapper').append($('<input>', { 'type' : 'hidden', 'name' : 'addfee', 'value' : addfee }));
		
					$('.toggleCont').find('dd').each(function() {
						$(this).html(common.priceFormat(addfee) + '<i>원</i>');
					});
				}
				$('#claimBtn').data('confirm', showflag);
				$('#claimBtn').show();
			},
			refund : function($this) {
				if (!this.qty($this.data('title'))) {
					return;
				}
				
				$('select[name$="].omsClaimproduct.claimQty"]').each(function(idx, obj) {
					if ($(obj).is(':enabled')) {
						var claimQty = $(obj).val();
						var totalSalePrice = $(obj).data('totalSalePrice');
						var productCouponDcAmt = $(obj).data('productCouponDcAmt');
						var plusCouponDcAmt = $(obj).data('plusCouponDcAmt');
						var orderCouponDcAmt = $(obj).data('orderCouponDcAmt');
						// var totalGoodsDcAmt = productCouponDcAmt * claimQty + plusCouponDcAmt * claimQty + orderCouponDcAmt;//TODO
						var totalGoodsDcAmt = productCouponDcAmt * claimQty + plusCouponDcAmt * claimQty;
						
						$('.refund_layer dd').eq(0).html(common.priceFormat(totalSalePrice * claimQty) + '<i>원</i>');
						
						$detail = $('.refund_layer').next();
						if (totalGoodsDcAmt > 0) {
							$detail.children('dt').eq(0).show();
							$detail.children('dd').eq(0).show().html('<em class="minus">'+common.priceFormat(totalSalePrice * claimQty) + '</em>원');
						}
					}
				});
			}
		},
		setdata : {
			claimreason : function($this) {
				if ($this.val() == 'CLAIM_REASON_CD.TYPING') {
					$this.parents('.reason').find('.inputTxt_place1').show()
					$this.parents('.reason').find('.claim_reason').prop('disabled', false);
				} else {
					$this.parents('.reason').find('.inputTxt_place1').hide();
					$this.parents('.reason').find('.claim_reason').prop('disabled', true);
				}
				if ($this.data('claimType') == 'exchange') {
					$('.payment_info').hide();
					$('#payment_area').hide();
					$('#claimBtn').hide();
					$('#claimBtn').data('confirm', 0);
				}
				this.claimqty($this.closest('.claimReason').prev().find('select'));
			},
			claimequals : function($this) {
				var seq = $this.data('seq');
				var prevReason = '';
				var prevReasonCd = '';
				if ($this.is(':checked')) {
					prevReason = $('input[name="omsOrderproducts[' + (seq-1) + '].omsClaimproduct.claimReason"]').val();
					prevReasonCd = $('select[name="omsOrderproducts[' + (seq-1) + '].omsClaimproduct.claimReasonCd"]').val();
				}
				$('input[name="omsOrderproducts[' + seq + '].omsClaimproduct.claimReason"]').val(prevReason);
				$('select[name="omsOrderproducts[' + seq + '].omsClaimproduct.claimReasonCd"]').val(prevReasonCd).trigger('change');
			},
			claimqty : function($this) {
				// var claimQty = $this.val();
				// var totalSalePrice = $this.data('totalSalePrice');
				
				/** 환불정보세팅 */
				var search = {};
				search['orderId'] = $this.data('orderId');
				search['orderProductNos'] = $this.data('orderProductNo');
				search['deliveryAddressNo'] = $('.delivery').data('deliveryAddressNo');
				search['deliveryPolicyNo'] = $('.delivery').data('deliveryPolicyNo');
				search['selectKey'] = $('.delivery').data('selectKey');
				
				var module = $('.delivery').data('selectKey');
				
				// 전체상품
				var omsProductList, presentList, couponList, policy;
				
				var isFreeDelivery = false;
				var isCancelAll = true;
				// 딜적용 								==> 상품 쿠폰(최대할인), 배송비계산(최소주문금액), 주문사은품(최소주문금액)
				// 딜적용 + 상품쿠폰적용					==> 플러스 쿠폰(최대할인)
				// 딜적용 + 상품쿠폰적용 + 플러스쿠폰적용		==> 장바구니 쿠폰(최소주문금액/최대할인), 포인트적립/추가적립(적립대상)
				var stdAmtDealDelivery = 0;	// 배송비계산(최소주문금액)
				var stdAmtPlusOrderCoupon = 0;// 장바구니 쿠폰(최소주문금액/최대할인)
				
				var refundAmt = 0; // 주문취소금액
				var policyWrapVolume = 0; // 남아있는 포장볼륨(누적된 클레임 포함.)
				var remainWrapVolume = 0; // 현재클레임 이후의 포장볼륨
				var existClaimDeliveryFee = false;// 주문의 클레임들 중에 원배송비가 발생했는지를 체크
				var claimCoupons = [];
				var omsClaimdelivery = {
					orderDeliveryFee : 0,	
					returnDeliveryFee : 0,	
					exchangeDeliveryFee : 0	
				};
				mms.common.restfulTrx('/api/ccs/guest/claim/all', 'post', search, function(response) {
					omsProductList = response.targetList;
					presentList = response.presentList;
					couponList = response.couponList;
					policy = response.policy;
					
					/** 환불정보 개별세팅 */
					var refundProductAmt = 0;
					for (var i = 0; i < omsProductList.length; i++) {
						var allEntity = omsProductList[i];
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
						$('select[name$="].omsClaimproduct.claimQty"]').each(function(idx, obj) {
							if ($(obj).is(':enabled')) {
								if ($(obj).data('orderProductNo') == allEntity.orderProductNo) {
									claimQty = $(obj).val();
								}
							}
						});
						
						var remainQty = availableClaimQty - claimQty;
						var stdAmtDeal = (allEntity.totalSalePrice) * remainQty;// 딜적용 총합 
						var stdAmtProd = (allEntity.totalSalePrice - allEntity.productCouponDcAmt) * remainQty;// 상품 쿠폰까지의 합
						var stdAmtPlus = (allEntity.totalSalePrice - allEntity.productCouponDcAmt - allEntity.plusCouponDcAmt) * remainQty;// 플러스 쿠폰까지의 합
						
						if (remainQty > 0) {
							isCancelAll = false;
						}
						
						/** 3.1. 상품 취소수량에 따른 [상품금액] 환불세팅 */
						refundAmt += (allEntity.totalSalePrice * claimQty);
						
						/** 3.2. 상품 취소수량에 따른 [배송비] 환불세팅 */
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
							for (var j = 0; j < couponList.length; j++) {
								var coupon = couponList[j];
								if (coupon['couponId'] == allEntity['productCouponId'] && coupon['couponIssueNo'] == allEntity['productCouponIssueNo']) {
									if (allEntity['productSingleApplyYn'] == 'Y') {
										if (remainQty < 1) {
											coupon.refundCouponAmt = allEntity.productCouponDcAmt;
											coupon.couponStateCd = 'COUPON_STATE_CD.CANCEL';
											claimCoupons.push(coupon);
										}
									} else {
										coupon.refundCouponAmt = allEntity.productCouponDcAmt * claimQty;
										if (coupon.refundCouponAmt + coupon.couponDcCancelAmt >= coupon.couponDcAmt) {
											coupon.couponStateCd = 'COUPON_STATE_CD.CANCEL';
										}
										claimCoupons.push(coupon);
									}
								} else if (coupon['couponId'] == allEntity['plusCouponId'] && coupon['couponIssueNo'] == allEntity['plusCouponIssueNo']) {
									if (allEntity['plusSingleApplyYn'] == 'Y') {
										if (remainQty < 1) {
											coupon.refundCouponAmt = allEntity.productCouponDcAmt;
											coupon.couponStateCd = 'COUPON_STATE_CD.CANCEL';
											claimCoupons.push(coupon);
										}
									} else {
										coupon.refundCouponAmt = allEntity.plusCouponDcAmt * claimQty;
										if (coupon.refundCouponAmt + coupon.couponDcCancelAmt >= coupon.couponDcAmt) {
											coupon.couponStateCd = 'COUPON_STATE_CD.CANCEL';
										}
										claimCoupons.push(coupon);
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
					for (var i = 0; i < omsProductList.length; i++) {
						var allEntity = omsProductList[i];
						if (allEntity['orderProductTypeCd'] == 'ORDER_PRODUCT_TYPE_CD.WRAP') {
							var claimQty = 0;
							$('select[name$="].omsClaimproduct.claimQty"]').each(function(idx, obj) {
								if ($(obj).is(':enabled')) {
									if ($(obj).data('orderProductNo') == allEntity.orderProductNo) {
										claimQty = $(obj).val();
									}
								}
							});
							
							var availableClaimQty = allEntity.availableClaimQty;
							var remainQty = availableClaimQty - claimQty;
							
							// 포장지 상품 환불비
							refundAmt += (allEntity.totalSalePrice * claimQty);
							
							// 포장지 상품의 포장쿠폰 환불
							if (claimQty > 0) {
								for (var j = 0; j < couponList.length; j++) {
									var coupon = couponList[j];
									if (coupon['couponId'] == allEntity['productCouponId'] && coupon['couponIssueNo'] == allEntity['productCouponIssueNo']) {
										if (allEntity['productSingleApplyYn'] == 'Y') {
											if (remainQty < 1) {
												coupon.refundCouponAmt = allEntity.productCouponDcAmt;
												coupon.couponStateCd = 'COUPON_STATE_CD.CANCEL';
												claimCoupons.push(coupon);
											}
										} else {
											coupon.refundCouponAmt = allEntity.productCouponDcAmt * claimQty;
											if (coupon.refundCouponAmt + coupon.couponDcCancelAmt >= coupon.couponDcAmt) {
												coupon.couponStateCd = 'COUPON_STATE_CD.CANCEL';
											}
											claimCoupons.push(coupon);
										}
									}
								}
							}				
						}
					}
					
					refundProductAmt = refundAmt;
					{
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
						$('select[name$="].omsClaimproduct.claimReasonCd"]').each(function(idx, obj) {
							if ($(obj).is(':enabled')) {
								if (!common.isEmpty($(obj).val())) {
									var reasonCd = ($(obj).val()).replace('CLAIM_REASON_CD.', '');
//										if ('CHANGE,DESIGN,PRICE,TYPING'.indexOf(reasonCd) > -1) {	// 하나라도 귀책사유가 고객이면 부과.
//											omsClaimdelivery.returnDeliveryFee = policy.deliveryFee; // 반품으로 인한 추가배송비 생성
//										}
									if ('CHANGE,DESIGN,PRICE,TYPING'.indexOf(reasonCd) < 0) {	// 귀책사유가 혼재된 경우 판매자 귀책
										hasPenalty = false;
										$('.tr_claimmsg').hide();
									} else {
										$('.tr_claimmsg').show();
									}
								}
							}
						});
						if (module == 'return') {
							omsClaimdelivery.returnDeliveryFee = (hasPenalty ? policy.deliveryFee : 0); // 반품으로 인한 추가배송비 생성
						}						
						
						var refundDeliveryFee = 0;
						var isCancelDeliveryCoupon = false;
						if (!common.isEmpty(policy.deliveryCouponId)) {
							// stdAmtDealDelivery --> 취소후 정책단위 상품들의 총합계
							if (stdAmtDealDelivery > 0) {
								// 여전히 배송비 쿠폰이 적용됨. --> 쿠폰적용가 변동없음.배송비는 여전히 무료
							} else {
								// 배송비 쿠폰 적용 취소(배송정책의 전체상품취소)
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
											if (hasPenalty && !existClaimDeliveryFee) {
												omsClaimdelivery.orderDeliveryFee = policy.deliveryFee; // 취소(반품으)로 인한 추가배송비 생성
											}
										}
									}
								} else {
									// 부과된 배송료 환불세팅
									if (module == 'cancel') {
										refundDeliveryFee = policy.applyDeliveryFee;
									} else {
										if (hasPenalty && !existClaimDeliveryFee) {
											omsClaimdelivery.orderDeliveryFee = policy.deliveryFee; // 취소(반품으)로 인한 추가배송비 생성
										}
										if (isCancelAll) { // 마지막 상품의 취소(반품)이면 주문시 발생한 배송비도 귀책에 관계없이 환불금액에 포함(이후 원배송비로 차감된다...)
											refundDeliveryFee = policy.applyDeliveryFee;
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
							} else {//전체(혹은 포장상품) 취소되어 포장비 없어짐.
								refundWrapFee = beforeWrapFee; // 환불포장비
							}
						}
						refundAmt += refundWrapFee;
						
						for (var j = 0; j < couponList.length; j++) {
							var coupon = couponList[j];
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
									coupon.refundCouponAmt = coupon.couponDcAmt - coupon.couponDcCancelAmt - remainOrderCouponDcAmt;
									claimCoupons.push(coupon);
								} else {
									coupon.refundCouponAmt = coupon.couponDcAmt - coupon.couponDcCancelAmt;
									coupon.couponStateCd = 'COUPON_STATE_CD.CANCEL';
									claimCoupons.push(coupon);
								}
							}
							
							/** 3.6. 상품 취소수량에 따른 [배송비쿠폰,포장비쿠폰] 환불세팅 */
							if (coupon['couponId'] == policy['deliveryCouponId'] && coupon['couponIssueNo'] == policy['deliveryCouponIssueNo']) {
								if (isCancelDeliveryCoupon) {
									coupon.refundCouponAmt = coupon.couponDcAmt;// 적용된 정책의 배송비만큼 할인된 금액
									coupon.couponStateCd = 'COUPON_STATE_CD.CANCEL';
									claimCoupons.push(coupon);
								}
							} else if (coupon['couponId'] == policy['wrapCouponId'] && coupon['couponIssueNo'] == policy['wrapCouponIssueNo']) {
								if (afterWrapFee <= 0) {
									coupon.refundCouponAmt = coupon.couponDcAmt;
									coupon.couponStateCd = 'COUPON_STATE_CD.CANCEL';
									claimCoupons.push(coupon);
								}
							}
						}
						
						var couponHtml = '';
						var refundCouponAmt = 0;
						var refundProdCouponAmt = 0;
						for (var j = 0; j < claimCoupons.length; j++) {
							var claimCoupon = claimCoupons[j];
							refundCouponAmt += claimCoupon.refundCouponAmt;
//							refundAmt -= claimCoupon.refundCouponAmt;
							if (claimCoupon.couponTypeCd == 'COUPON_TYPE_CD.DELIVERY') {
								couponHtml += '<dt>배송비무료쿠폰</dt>';
								couponHtml += ('<dd><em class="minus">' + common.priceFormat(-claimCoupon.refundCouponAmt) + '</em>원</dd>');
//							} else if (claimCoupon.couponTypeCd == 'COUPON_TYPE_CD.WRAP') {
//								couponHtml += '<dt>선물포장무료쿠폰</dt>';
//								couponHtml += ('<dd><em class="minus">' + common.priceFormat(-claimCoupon.refundCouponAmt) + '</em>원</dd>');
							} else {
								refundProdCouponAmt -= claimCoupon.refundCouponAmt;
							}
						}
						if (refundProdCouponAmt < 0) {
							couponHtml = '<dt>상품할인쿠폰</dt><dd><em class="minus">' + common.priceFormat(refundProdCouponAmt) + '</em>원</dd>' + couponHtml;
						}
						var addDeliveryFee = omsClaimdelivery.orderDeliveryFee;
						refundAmt -= refundCouponAmt;
						refundAmt -= omsClaimdelivery.orderDeliveryFee;
						if (module == 'return') {
							refundAmt -= omsClaimdelivery.returnDeliveryFee;
							addDeliveryFee += omsClaimdelivery.returnDeliveryFee;
						} else if (module == 'exchange') {
							refundAmt -= omsClaimdelivery.exchangeDeliveryFee;
							addDeliveryFee += omsClaimdelivery.exchangeDeliveryFee;
						}
						
						$('.detail:eq(1)').html(couponHtml);
						$('.columnL:eq(2) dd:eq(0)').html(common.priceFormat(refundProductAmt) + '<i>원</i>');// 1. 상품금액
						$('.columnL:eq(2) dd:eq(1)').html(common.priceFormat(refundDeliveryFee) + '<i>원</i>');// 2. 배송비
						$('.columnL:eq(2) dd:eq(2)').html(common.priceFormat(-addDeliveryFee) + '<i>원</i>');// 3. 추가발생배송비
//						$('.columnL:eq(2) dd:eq(3)').html(common.priceFormat(refundWrapFee) + '<i>원</i>');// 4. 포장비
						$('.columnL:eq(2) dd:eq(3)').html(common.priceFormat(-refundCouponAmt) + '<i>원</i>');// 5. 쿠폰
						$('.money:eq(1) dd:eq(0)').html(common.priceFormat(refundAmt) + '<i>원</i>');// 6. 총환불금액
						
						if (refundAmt < 0) {
							$('#claimBtn').hide();
						} else {
							$('#claimBtn').show();
						}
					}
					
					// 주문사은품
					$claim.setdata.present($this, refundProductAmt, omsProductList, presentList);
				}, function(xhr) {
					$order.callback.beforesend($('.toggleCont').eq(1));
				});
				
//				$order.callback.beforesend($('.toggleCont').eq(1))
			},
			present : function($this, sumClaimAmt, omsProductList, presentList) {
				var hasPenalty = false;
				var $claimReasonCd = $this.closest('.col2').next().find('select');
				
				if (!common.isEmpty($claimReasonCd.val())) {
					var reasonCd = $claimReasonCd.val().replace('CLAIM_REASON_CD.', '');
					if ('CHANGE,DESIGN,PRICE,TYPING'.indexOf(reasonCd) > -1) {
						hasPenalty = true;
					}
				}
				if (presentList != null && presentList.length > 0) {
					for (var i = 0; i < omsProductList.length; i++) {
						var entity = omsProductList[i];
	
						for (var j = 0; j < presentList.length; j++) {
							var present = presentList[j];
							if (present['presentId'] == entity['presentId']) { // 주문사은품 찾기
								var isShow = false;
								var $promo = $('.promotion');
								if (hasPenalty) {
									if (entity.presentMinOrderAmt > 0) {
										if (present.presentTargetAmt - sumClaimAmt < entity.presentMinOrderAmt) {
											isShow = true;
										}
									} else {
										isShow = true;
									}
								}
								if (isShow) {
									// 주문사은품 show
									if ($promo.data('presentId') == entity['presentId']) {
										$promo.show();
									}
									$promo.find('li').each(function(idx, obj) {
										if ($(this).data('orderProductNo') == entity['orderProductNo']) {
											$(this).show();
											$(this).find('select').prop('disabled', false);
											$(this).find('input').prop('disabled', false);
											// $(this).find('select[name$="claimReasonCd"]').children('option').first().val($this.);
										}
									});
								} else {
									$promo.hide();
									$promo.find('li').each(function(idx, obj) {
										$(this).hide();
										$(this).find('select').prop('disabled', true);
										$(this).find('input').prop('disabled', true);
									});
								}
	
							}
						}
					}
				}
			},
			refund : function($this) {
				$this.next().val($('option:selected', $this).text());
			}
		}
}

// 4. lg
var $payment = {
		type : '',
		setmethod : function($this, evt) {
			$this.off('click');
			evt = evt || window.event;
			evt.stopPropagation();
			// evt.preventDefault();
//			var $selectBoxes = $('.ly_addPayment').find('ul.type1').find('.select_box1');
			var $selectBoxes = $this.closest('.pay_how').find('.select_box1');
			
			$selectBoxes.each(function(idx, obj) {
				$(this).hide();
				if ($(this).data('method') == $this.val()) {
					$(this).show();
				}
			});
		},
  		invoke:function($this, type, exchange) {
			var clicked = $this.data('clicked');
			if (clicked) {
				return false;
			}
			$this.data('clicked', true);
			$payment.type = type;
			if (!common.isEmpty(exchange)) {
				$payment.callback = $payment.exchange;
			} else {
				$payment.callback = $payment.submit;
			}
			var paymethod = $('input:radio[name="paymentMethodCd"]:checked').val();
			if (paymethod == 'PAYMENT_METHOD_CD.KAKAO') {
				kakao.payment(paymethod);
			} else {
				lgu.payment(paymethod);
			}
			$this.data('clicked', false);
		},
		submit : function($frmObj) {
			var url = '/api/ccs/guest/payment/' + $payment.type;
			$order.submit('post', $frmObj.serialize(), url, function(response) {
				if (response.success) {
					alert($('#claim_pay_title').text() + ($payment.type == 'insert' ? '가' : '이') + '완료되었습니다.');
					common.showLoadingBar();
					window.location.reload();
				} else {
					alert(response.resultMessage);
				}
			});
		},
		// 교환신청
		exchange : function(frmObj) {
 			var formData = $('#omsClaimWrapper').serialize();
 			if (typeof frmObj !== 'undefined') {
 				formData = $('#omsClaimWrapper,#submitform').serialize();
 			}
			$order.submit('post', formData, '/api/ccs/guest/claim', function(response) {
				console.log('response : ', response);
				if (response.success) {
					alert('주문 상품 교환신청을 완료하였습니다.');
					common.showLoadingBar();
					location.href = document.referrer;
				} else {
					alert(response.resultMessage);
				}
			});
		},

	}
