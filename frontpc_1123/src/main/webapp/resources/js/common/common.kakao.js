// load library
$('head').append('<script src="https://pg.cnspay.co.kr:443/dlp/scripts/lib/easyXDM.min.js" type="text/javascript"></script>');
$('head').append('<script src="https://pg.cnspay.co.kr:443/dlp/scripts/lib/json3.min.js" type="text/javascript"></script>');
$('head').append('<script src="https://kmpay.lgcns.com:8443/js/dlp/lib/jquery/jquery-1.11.1.min.js" charset="urf-8"></script>');
$('head').append('<script src="https://kmpay.lgcns.com:8443/js/dlp/client/kakaopayDlpConf.js" charset="utf-8"></script>');
$('head').append('<script src="https://kmpay.lgcns.com:8443/js/dlp/client/kakaopayDlp.min.js" charset="utf-8"></script>');

var kakao = {
	callback : '',		
	getFormObject : function() {
		return document.getElementById("payForm");
	},
	cnspaySubmit : function(data) {
		if (data.RESULT_CODE == '00') {
			// 매뉴얼 참조하여 부인방지코드값 관리
			var frmObj = kakao.getFormObject();
			
			// make form
			$('<form>', { 'name':'submitform', 'id':'submitform', 'method':'post' })//
			.appendTo(document.body);
	
			// form rebuild
			$.each(frmObj, function(key, value) {
				var name = $(this).attr("name");
				var value = $(this).attr("value");
				// 중복방지
				if (!$('#submitform').find('[name="' + name + '"]').length) {
					$('#submitform').append($('<input>', { 'type':'hidden', 'name': name, 'value':value }));
					$('#submitform').append($('<input>', { 'type':'hidden', 'name': 'kakao.' + name, 'value':value }));
				}
			});
			$payment.callback($('#submitform'));
		} else if (data.RESULT_CODE == 'KKP_SER_002') {
			// X버튼 눌렀을때의 이벤트 처리 코드 등록
			console.log('[RESULT_CODE] : ' + data.RESULT_CODE + '\n[RESULT_MSG] : ' + data.RESULT_MSG);
			alert(data.RESULT_MSG);
		} else {
			console.log('[RESULT_CODE] : ' + data.RESULT_CODE + '\n[RESULT_MSG] : ' + data.RESULT_MSG);
			alert(data.RESULT_MSG);
		}
	},
	/**
	 * cnspay 를 통해 결제를 시작합니다.
	 */
	cnspay : function() {
		var frmObj = this.getFormObject();
		var frmData = $(frmObj).serialize();
		frmObj.acceptCharset = 'euc-kr';
		
		if (frmObj.resultCode.value == '00') {
			kakaopayDlp.setTxnId(frmObj.txnId.value);
			if(global.channel.isMobile == "true"){
				kakaopayDlp.setChannelType('MPM', 'WEB'); // MB결제
			} else {
				kakaopayDlp.setChannelType('WPM', 'TMS'); // PC결제
			}
			// kakaopayDlp.addRequestParams({ MOBILE_NUM : '010-1234-5678'}); // 초기값 세팅
			kakaopayDlp.callDlp('kakaopay_layer', frmObj, kakao.cnspaySubmit);
		} else {
			console.log('[RESULT_CODE] : ' + frmObj.resultCode.value + '\n[RESULT_MSG] : ' + frmObj.resultMsg.value);
			alert(frmObj.resultMsg.value);
		}
	},
	setdata : function(paymethod) {
		$option = $('.tr_idx').find('.option_txt');
		var productInfo = $option.eq(0).prev().text();
		if ($option.length > 1) {
			productInfo = productInfo + ' 외 ' + $option.length + '건';
		}
		if (global.channel.isMobile == 'true') {
			$('input[name="prType"]').val('MPM');
			$('input[name="channelType"]').val('2');
		} else {
			$('input[name="prType"]').val('WPM');
			$('input[name="channelType"]').val('4');
		}	
		
		$('input[name="paymentMethodCd"][type="hidden"]').val(paymethod);
		$('input[name="GoodsName"]').val(productInfo);
		$('input[name="GoodsCnt"]').val($option.length);
		$('input[name="BuyerName"]').val($('#orderer_name').text());
		$('input[name="BuyerEmail"]').val($('#orderer_name').next().children('span:eq(2)').text());
		$('input[name="merchantTxnNum"]').val($('.change_method').data('paymentNo'));
		$('input[name="Amt"]').val($('.change_method').data('fee'));
		$('input[name="orderId"]').val($option.eq(0).data('orderId'));
		$('input[name="claimNo"]').val($option.eq(0).data('claimNo'));
		$('input[name="selectKey"]').val($('.change_method').data('callback'));
		
		var frmObj = this.getFormObject();
		frmObj.acceptCharset = 'utf-8';
		
		$order.submit('post', $(frmObj).serialize(), '/api/oms/pg/kakao/txnid', function(response) {
			if (response.resultCode == '00') {
				$.each(response, function(name, value) {
					if (!$(frmObj).find('[name="' + name + '"]').length && !$(frmObj).find('[id="' + name + '"]').length) {
						if (!common.isEmpty(value)) {
							$(frmObj).append($('<input>', { 'type':'hidden', 'name':name, 'id':name, 'value':value }));
						}
					} else {
						if (!common.isEmpty(value)) {
							$('input[id="' + name + '"]').val(value);
						}
					}
				});
				kakao.cnspay();
			} else {
				alert(response.resultMsg);
			}
			$('.change_method').data('clicked', false);
		});
	},
	buildform : function() {
		$('<form>', { 'name':'payForm', 'id':'payForm', 'action':'', 'method':'post', 'accept-charset':'utf-8' })//
		
		.append($('<input>', { 'type':'hidden', 'name':'paymentMethodCd' })) // 결제방법
		.append($('<input>', { 'type':'hidden', 'name':'orderId'}))// 
		.append($('<input>', { 'type':'hidden', 'name':'claimNo'}))// 
		.append($('<input>', { 'type':'hidden', 'name':'selectKey'}))// 
		
		.append($('<input>', { 'type':'hidden', 'name':'PayMethod', 'id':'payMethod', 'value':'KAKAOPAY' }))// (*)결제수단
		.append($('<input>', { 'type':'hidden', 'name':'GoodsName', 'id':'goodsName' }))// (*)상품명
		.append($('<input>', { 'type':'hidden', 'name':'Amt', 'id':'amt' }))// (*)상품가격
		.append($('<input>', { 'type':'hidden', 'name':'SupplyAmt', 'id':'supplyAmt', 'value':'0' }))// 공급가액
		.append($('<input>', { 'type':'hidden', 'name':'GoodsVat', 'id':'goodsVat', 'value':'0' }))// 부가세
		.append($('<input>', { 'type':'hidden', 'name':'ServiceAmt', 'id':'serviceAmt', 'value':'0' }))// 봉사료
		.append($('<input>', { 'type':'hidden', 'name':'GoodsCnt', 'id':'goodsCnt' }))// (*)상품갯수
		.append($('<input>', { 'type':'hidden', 'name':'BuyerEmail', 'id':'buyerEmail' }))// 구매자 이메일
		.append($('<input>', { 'type':'hidden', 'name':'BuyerName', 'id':'buyerName' }))// (*)구매자명
		.append($('<input>', { 'type':'hidden', 'name':'MID', 'id':'mid' }))// (*)가맹점ID
		.append($('<input>', { 'type':'hidden', 'name':'AuthFlg', 'id':'authFlg', 'value':'10' }))// (*)인증플래그
		.append($('<input>', { 'type':'hidden', 'name':'EdiDate', 'id':'ediDate' }))// (*)EdiDate
		.append($('<input>', { 'type':'hidden', 'name':'EncryptData', 'id':'encryptData' }))// (*)EncryptData
		
		// 인증 변수 목록(매뉴얼 참조)
		.append($('<input>', { 'type':'hidden', 'name':'offerPeriodFlag', 'id':'offerPeriodFlag', 'value':'Y' }))// 상품제공기간 플래그
		.append($('<input>', { 'type':'hidden', 'name':'offerPeriod', 'id':'offerPeriod', 'value':'제품표시일까지' }))// 상품제공기간
		.append($('<input>', { 'type':'hidden', 'name':'certifiedFlag', 'id':'certifiedFlag', 'value':'CN' }))// (*)인증구분
		.append($('<input>', { 'type':'hidden', 'name':'currency', 'id':'currency', 'value':'KRW' }))// (*)거래통화
		.append($('<input>', { 'type':'hidden', 'name':'merchantEncKey', 'id':'merchantEncKey' }))// (*)가맹점 암호화키
		.append($('<input>', { 'type':'hidden', 'name':'merchantHashKey', 'id':'merchantHashKey' }))// (*)가맹점 해쉬키
		.append($('<input>', { 'type':'hidden', 'name':'requestDealApproveUrl', 'id':'requestDealApproveUrl' }))// (*)TXN_ID 요청URL
		.append($('<input>', { 'type':'hidden', 'name':'prType', 'id':'prType', 'value':'MPM' }))// (*)결제요청타입 :
		.append($('<input>', { 'type':'hidden', 'name':'channelType', 'id':'channelType', 'value':'2' }))// (*)채널타입 :
		.append($('<input>', { 'type':'hidden', 'name':'merchantTxnNum', 'id':'merchantTxnNum'}))// (*)가맹점 거래번호 :
		
		// <!-- 인증 파라미터 중 할부결제시 사용하는 파라미터 목록 -->
		// <b>할부결제시 선택변수 목록</b><br />
		// - 옳은 값들을 넣지 않으면 무이자를 사용하지 않는것으로 한다.<br />
		// <b>카드코드(매뉴얼 참조)</b><br />
		.append($('<input>', { 'type':'hidden', 'name':'possiCard', 'id':'possiCard' }))// 카드선택
		.append($('<input>', { 'type':'hidden', 'name':'fixedInt', 'id':'fixedInt' }))// 할부개월
		.append($('<input>', { 'type':'hidden', 'name':'maxInt', 'id':'maxInt' }))// 최대할부개월
		.append($('<input>', { 'type':'hidden', 'name':'noIntYN', 'id':'noIntYN' }))// 무이자 사용여부
		.append($('<input>', { 'type':'hidden', 'name':'noIntOpt', 'id':'noIntOpt' }))// 무이자옵션
		.append($('<input>', { 'type':'hidden', 'name':'pointUseYn', 'id':'pointUseYn' }))// 카드사포인트사용여부
		.append($('<input>', { 'type':'hidden', 'name':'blockCard', 'id':'blockCard' }))// 금지카드설정
		.append($('<input>', { 'type':'hidden', 'name':'blockBin', 'id':'blockBin' }))// 특정제한카드 BIN
		
		// <b>getTxnId 응답</b><br />
		.append($('<input>', { 'type':'hidden', 'name':'resultCode', 'id':'resultCode' }))// resultcode
		.append($('<input>', { 'type':'hidden', 'name':'resultMsg', 'id':'resultMsg' }))// resultmsg
		.append($('<input>', { 'type':'hidden', 'name':'txnId', 'id':'txnId' }))// txnId
		.append($('<input>', { 'type':'hidden', 'name':'prDt', 'id':'prDt' }))// prDt
		// <!-- DLP호출에 대한 응답 -->
		// <b>DLP 응답</b><br />
		.append($('<input>', { 'type':'hidden', 'name':'SPU', 'id':'SPU' }))// SPU
		.append($('<input>', { 'type':'hidden', 'name':'SPU_SIGN_TOKEN', 'id':'SPU_SIGN_TOKEN' }))// SPU_SIGN_TOKEN
		.append($('<input>', { 'type':'hidden', 'name':'MPAY_PUB', 'id':'MPAY_PUB' }))// MPAY_PUB
		.append($('<input>', { 'type':'hidden', 'name':'NON_REP_TOKEN', 'id':'NON_REP_TOKEN' }))// NON_REP_TOKEN
		
		.appendTo(document.body);
	},
	// kakao 결제
	payment : function(paymethod) {
		$('#LGD_PAYINFO').remove();// 이전값 제거
		$('#payForm').remove();// 이전값 제거
		this.buildform();
		this.setdata(paymethod);
	}
}

