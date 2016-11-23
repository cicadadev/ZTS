// load library
//$('head').append('<script language="javascript" src="http://xpay.lgdacom.net/xpay/js/xpay_crossplatform.js" type="text/javascript" charset="utf-8"></script>');
/*
 * 인증결과 처리
 */
function payment_return() {
	lgu.payment_return();
}

var lgu = {
	callback : '',
	/*
	 * FORM 명만 수정 가능
	 */
	getFormObject : function() {
		return document.getElementById('LGD_PAYINFO');
	},
	/*
	 * 인증결과 처리
	 */
	payment_return : function() {
		var fDoc;
		fDoc = lgdwin.contentWindow || lgdwin.contentDocument;
		
		if (fDoc.document.getElementById('LGD_RESPCODE').value == '0000') {
			$('#submitform').remove();
			
			var frmObj = this.getFormObject();
			var frmDoc = fDoc.document.getElementById('LGD_RETURNINFO');
			
			$(frmDoc).find('input').each(function() {
				var element = $(this).attr('id');
				if (!$(frmObj).find('[name="' + element + '"]').length) {
					$(frmObj).append($('<input>', { 'type':'hidden', 'name':element, 'value':$(this).val() }));
				} else {
					if (!common.isEmpty($(this).val())) {
						$(frmObj).find('input[name="' + element + '"]').val($(this).val());
					}
				}
			});
			
			// make form
			$('<form>', { 'name':'submitform', 'id':'submitform', 'method':'post' })//
			.append($('<input>', { 'type':'hidden', 'name':'paymentMethodCd', 'value':frmObj.paymentMethodCd.value }))//
			.append($('<input>', { 'type':'hidden', 'name':'claimNo', 'value':frmObj.claimNo.value }))//
			.appendTo(document.body);
	
			// form rebuild
			$.each(frmObj, function(key, value) {
				var name = $(this).attr("name");
				var value = $(this).attr("value");
				$('#submitform').append($('<input>', { 'type':'hidden', 'name':'omsPaymentif.' + name, 'value':value }));
			});
			$payment.callback($('#submitform'));
		} else {
			alert(fDoc.document.getElementById('LGD_RESPMSG').value);
			console.log('LGD_RESPCODE (결과코드) : ' + fDoc.document.getElementById('LGD_RESPCODE').value + '\n' + 'LGD_RESPMSG (결과메시지): ' + fDoc.document.getElementById('LGD_RESPMSG').value);
		}
		$('.change_method').data('clicked', false);
		closeIframe();
	},
	/*
	 * iframe으로 결제창을 호출하시기를 원하시면 iframe으로 설정 (변수명 수정 불가)
	 */
	launchCrossPlatform : function(CST_PLATFORM, LGD_WINDOW_TYPE) {
		// lgdwin = openXpay(getFormObject(), '<%= CST_PLATFORM %>', LGD_WINDOW_TYPE, null, '', '');
		if (global.channel.isMobile == 'true') {
			lgdwin = open_paymentwindow(this.getFormObject(), CST_PLATFORM, LGD_WINDOW_TYPE);
		} else {
			lgdwin = openXpay(this.getFormObject(), CST_PLATFORM, LGD_WINDOW_TYPE, null, '', '');
		}
//	return lgdwin
	},
	setdata : function(paymethod) {
		$option = $('.tr_idx').find('.option_txt');
		var productInfo = $option.eq(0).prev().text();
		if ($option.length > 1) {
			productInfo = productInfo + ' 외 ' + $option.length + '건';
		}
		var usablePay = '';
		var lgdversion = '';
		if (paymethod == 'PAYMENT_METHOD_CD.CARD') {
			usablePay = 'SC0010';
			if (global.channel.isMobile == 'true') {
				$('input[name="LGD_VERSION"]').val('JSP_NonActiveX_Mobile_CardApp');
			} else {
				$('input[name="LGD_VERSION"]').val('JSP_Non-ActiveX_CardApp');
			}
		} else if (paymethod == 'PAYMENT_METHOD_CD.VIRTUAL') {
			usablePay = 'SC0040';
			if (global.channel.isMobile == 'true') {
				$('input[name="LGD_VERSION"]').val('JSP_Non-ActiveX_SmartXPay');
			} else {
				$('input[name="LGD_VERSION"]').val('JSP_Non-ActiveX_CardApp');
			}
		} else if (paymethod == 'PAYMENT_METHOD_CD.TRANSFER') {
			usablePay = 'SC0030';
			$('input[name="LGD_CUSTOM_PROCESSTYPE"]').val('TWOTR');	
			$('input[name="LGD_SELF_CUSTOM"]').val('N');
			
		} else if (paymethod == 'PAYMENT_METHOD_CD.MOBILE') {
			usablePay = 'SC0060';
			$('input[name="LGD_CUSTOM_PROCESSTYPE"]').val('TWOTR');	
			$('input[name="LGD_SELF_CUSTOM"]').val('N');	
			if (global.channel.isMobile == 'true') {
				$('input[name="LGD_VERSION"]').val('JSP_Non-ActiveX_SmartXPay');
			}
		}
		
		$('input[name="paymentMethodCd"][type="hidden"]').val(paymethod);
		$('input[name="LGD_BUYER"]').val($('#orderer_name').text());
		$('input[name="LGD_PRODUCTINFO"]').val(productInfo);
		$('input[name="LGD_OID"]').val($('.change_method').data('paymentNo'));
		$('input[name="LGD_CUSTOM_USABLEPAY"]').val(usablePay);
		$('input[name="LGD_CARDTYPE"]').val($('select[name="LGD_CARDTYPE"]').val());
		$('input[name="LGD_INSTALL"]').val($('select[name="LGD_INSTALL"]').val());
		$('input[name="LGD_NOINT"]').val('0');// TODO
		$('input[name="LGD_SP_CHAIN_CODE"]').val($('select[name="LGD_SP_CHAIN_CODE"]').val());
		$('input[name="LGD_SP_ORDER_USER_ID"]').val('');// TODO
		$('input[name="LGD_POINTUSE"]').val($('select[name="LGD_POINTUSE"]').val());
		$('input[name="LGD_USABLEBANK"]').val($('select[name="LGD_USABLEBANK"]').val());
		$('input[name="LGD_CASHRECEIPTYN"]').val($('select[name="LGD_CASHRECEIPTYN"]').val());
		$('input[name="LGD_AMOUNT"]').val($('.change_method').data('fee'));
		$('input[name="orderId"]').val($option.eq(0).data('orderId'));
		$('input[name="claimNo"]').val($option.eq(0).data('claimNo'));
		$('input[name="selectKey"]').val($('.change_method').data('callback'));
		
		var frmObj = this.getFormObject();
		$order.submit('post', $(frmObj).serialize(), '/api/oms/pg/param', function(response) {
			console.log('response : ', response);
			if (response.RESULT == 'SUCCESS') {
				var returnData = response.omsPaymentif;
				$.each(returnData, function(key, value) {
					var element = key.toUpperCase();
					if (!$(frmObj).find('[name="' + element + '"]').length) {
						if (value != null && value != undefined) {
							$(frmObj).append($('<input>', { 'type':'hidden', 'name':element, 'value':value }));
						}
					} else {
						if (value != null && value != undefined) {
							$('[name="' + element + '"]').val(value); // 갱신
						}
					}
				});
				// lgdwin = launchCrossPlatform(frmObj.CST_PLATFORM.value, frmObj.LGD_WINDOW_TYPE.value);
				// console.log('frmObj.CST_PLATFORM.value', frmObj.CST_PLATFORM.value);
				// console.log('frmObj.LGD_WINDOW_TYPE.value', frmObj.LGD_WINDOW_TYPE.value);
				lgu.launchCrossPlatform(frmObj.CST_PLATFORM.value, frmObj.LGD_WINDOW_TYPE.value);
			} else {
				alert(response.MESSAGE);
				// $('#payBtn').show();
			}
			$('.change_method').data('clicked', false);
			common.hideLoadingBar();
		}, function() {// before send
			common.showLoadingBar();
		}, function(jqXHR, textStatus, errorThrown) {// fail
			common.hideLoadingBar();
			alert(errorThrown);
		});
	},
	buildform : function() {
		$('<form>', { 'name':'LGD_PAYINFO', 'id':'LGD_PAYINFO', 'action':'', 'method':'post' })//
		.append($('<input>', { 'type':'hidden', 'name':'paymentMethodCd' })) // 결제방법
		.append($('<input>', { 'type':'hidden', 'name':'orderId'}))//
		.append($('<input>', { 'type':'hidden', 'name':'claimNo'}))//
		.append($('<input>', { 'type':'hidden', 'name':'selectKey'}))//
		.append($('<input>', { 'type':'hidden', 'name':'LGD_OID' })) // 결제번호
		.append($('<input>', { 'type':'hidden', 'name':'LGD_BUYER' })) // 구매자
		.append($('<input>', { 'type':'hidden', 'name':'LGD_PRODUCTINFO' })) // 상품정보
		.append($('<input>', { 'type':'hidden', 'name':'LGD_AMOUNT' })) // 결제금액
		.append($('<input>', { 'type':'hidden', 'name':'LGD_BUYEREMAIL' })) // 구매자 이메일
		
		.append($('<input>', { 'type':'hidden', 'name':'LGD_WINDOW_VER' })) // 결제창버전정보 (삭제하지 마세요)
		.append($('<input>', { 'type':'hidden', 'name':'LGD_PAYKEY' })) // LG유플러스 PAYKEY(인증후 자동셋팅)
		.append($('<input>', { 'type':'hidden', 'name':'LGD_VERSION', 'value':'JSP_Non-ActiveX_Standard' })) // 버전정보 (삭제하지 마세요)
		.append($('<input>', { 'type':'hidden', 'name':'LGD_CUSTOM_USABLEPAY' })) // 결제수단
		.append($('<input>', { 'type':'hidden', 'name':'LGD_CARDTYPE' }))// 카드사 번호
		.append($('<input>', { 'type':'hidden', 'name':'LGD_INSTALL' })) // 안심클릭 할부 개월
		.append($('<input>', { 'type':'hidden', 'name':'LGD_SP_CHAIN_CODE' })) // 간편결제사용여부
		.append($('<input>', { 'type':'hidden', 'name':'LGD_SP_ORDER_USER_ID' })) // 삼성카드 간편결제 쇼핑몰 KEY_ID
		// .append($('<input>', { 'type':'hidden', 'name':'LGD_NOINT' })) // 상점부담무이자할부 적용여부
		.append($('<input>', { 'type':'hidden', 'name':'LGD_POINTUSE' })) // 포인트 사용여부
		.append($('<input>', { 'type':'hidden', 'name':'LGD_CUSTOM_PROCESSTYPE', 'value':'AUTHTR' })) // 트랜잭션 처리방식 (자체창 : AUTHTR, 일반 : TWOTR)
		.append($('<input>', { 'type':'hidden', 'name':'LGD_SELF_CUSTOM', 'value':'Y' })) // 자체창 사용여부
		
		.appendTo(document.body);
	},
	valid : function(paymethod) {
		var errmsg = '';
		
//		var $selectBoxes = $('.ly_addPayment').find('ul.type1').find('.select_box1');
		var $selectBoxes = $('.pay_how').find('.select_box1');
		$selectBoxes.each(function(idx, obj) {
			if ($(this).data('method') == paymethod) {
				if (common.isEmpty($(this).children('select').val())) {
					errmsg = $(this).children('label').text();
					return false;
				}
			}
		});
		
		return errmsg;
	},
// LG 결제
	payment : function(paymethod) {
		var errormsg = this.valid(paymethod)
		if (!common.isEmpty(errormsg)) {
			alert(errormsg + '를 선택해 주십시오.');
			return false;
		}
		$('#LGD_PAYINFO').remove();// 이전값 제거
		$('#payForm').remove();// 이전값 제거
		$('#submitform').remove();// 이전값 제거
		this.buildform();
		this.setdata(paymethod);
	}
	
}
