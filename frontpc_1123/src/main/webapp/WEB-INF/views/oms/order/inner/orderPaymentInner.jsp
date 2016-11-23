<%--
	화면명 : 주문서
	작성자 : dennis
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags" %>

<script type="text/javascript">

$(document).ready(function(){
	//초기결제수단 SETTING
	setIntitPayment();
})

var paymentData = function(){
	
	var totalOrderAmt = $("#saveOrderForm").find("#totalOrderAmt").val();
	$("#paymentForm0").find("#paymentAmt").val(totalOrderAmt);	//결제금액
	
	var paymentMethodCd = $("input:radio[name=paymentMethodCd]:checked").val();
	
// 		console.log("결제수단 : ",paymentMethodCd);

	var conPay = $("#continuePaymentMethodChk").prop("checked");
	if(conPay){
		$("#paymentForm0").find("#continuePaymentMethod").val("Y");
	}
	
	common.mergeForms("mergeForm","paymentForm0","paymentForm1","paymentForm2","paymentForm3");
}

//초기 결제수단 세팅
var setIntitPayment = function(){
	
	var staticPaymentCard = $("#staticPaymentCard").val();
	$("#cardInfo").hide();
	if(common.isNotEmpty(staticPaymentCard)){
		$("input[name=paymentMethodCd]").val("PAYMENT_METHOD_CD.CARD");
		$("input[name=paymentMethodCd]").prop("disabled",true);
		staticPaymentCard = staticPaymentCard.replace("PAYMENT_BUSINESS_CD.","");
		$("#pg_LGD_CARDTYPE").val(staticPaymentCard);
		$("#pg_LGD_CARDTYPE").prop("disabled",true);
		
		var channelId = $("#channelId").val();
		if("0011" == channelId || "0010" == channelId){	//신한,삼성일때.
			$("#pointUseYn").show();
		}
		
		$("#virtualDiv").hide();
		$("#transferDiv").hide();
		$("#mobileDiv").hide();
		$("#kakaoDiv").hide();
		chgCardType();
		cssChange();		
		$("#cardInfo").show();
	}else{
		var form = $("#initPayment");
		var paymentMethodCd = form.find("#paymentMethodCd").val();
		if(common.isNotEmpty(paymentMethodCd)){
			var paymentBusinessCd = form.find("#paymentBusinessCd").val();
			
			var paymentMethodChk = $("input[name=paymentMethodCd]");
			paymentMethodChk.each(function(){
	// 			console.log(this.value);
	// 			console.log(paymentMethodCd);
				if(this.value == paymentMethodCd){
					$(this).prop("checked",true);
					paymentChg(this);							
				}
			});
			
			var selObj;
			$("#cardInfo").hide();
			
			if(paymentMethodCd == "PAYMENT_METHOD_CD.CARD"){
				selObj = $("#pg_LGD_CARDTYPE");
				paymentBusinessCd = paymentBusinessCd.substr(0,2);			
				if(common.isEmpty(paymentBusinessCd)){
					selObj.val("XX");
				}else{
					selObj.val(paymentBusinessCd);
				}
				$("#cardInfo").show();
				chgCardType();
			}else if(paymentMethodCd == "PAYMENT_METHOD_CD.VIRTUAL"){
				selObj = $("#pg_LGD_BANKCODE");
				selObj.val(paymentBusinessCd);
			}else if(paymentMethodCd == "PAYMENT_METHOD_CD.TRANSFER"){
				selObj = $("#pg_LGD_USABLEBANK");
				selObj.val(paymentBusinessCd);
			}				
			
			cssChange();
		}else{	//default card
			$("#cardInfo").show();
			chgCardType();
		}	
	}	

}

//신용카드 할부개월수 control
//0원결제 결제수단 비노출
var paymentControl = function(){
	
	var totalOrderAmt = $("#saveOrderForm").find("#totalOrderAmt").val();
	
	chgCardType();
	
	if(totalOrderAmt == 0){
		$("[id^=paymentList]").hide();
		$("#cardInfo").hide();
	}else{
		$("[id^=paymentList]").show();
		var paymentMethodCd = $("input:radio[name=paymentMethodCd]:checked").val();
		if(paymentMethodCd == "PAYMENT_METHOD_CD.CARD"){
			$("#cardInfo").show();
		}
	}
	
	cssChange();
}

//카드변경시 무이자 할부 조회
var chgCardType = function(){		
	var paymentMethodCd = $("input:radio[name=paymentMethodCd]:checked").val();
	if(paymentMethodCd == "PAYMENT_METHOD_CD.CARD"){
		var totalOrderAmt = $("#saveOrderForm").find("#totalOrderAmt").val();
		var cardCode = $("#pg_LGD_CARDTYPE").val();
		oms.interestList(cardCode,totalOrderAmt,function(response){
// 			console.log(response);
			$("#pg_LGD_INSTALL").html(response);
			
			if(Number(totalOrderAmt) >= 50000){
				$("#pg_LGD_INSTALL").val("00");
				$("#pg_LGD_INSTALL").prop("disabled",false);
			}else{
				$("#pg_LGD_INSTALL").val("00");
				$("#pg_LGD_INSTALL").prop("disabled",true);
			}
			
			cssChange();
		});		
	}
}
</script>
<!-- 초기선택 결제수단 -->
<form id="initPayment">
	<input type="hidden" id="paymentMethodCd" value="${memberInfo.paymentMethodCd }"/>	
	<input type="hidden" id="paymentBusinessCd" value="${memberInfo.paymentBusinessCd }"/>	
</form>

<!-- 카드사몰 -->
<input type="hidden" id="staticPaymentCard" value="${staticPaymentCard }"/>

<!-- channelId -->
<input type="hidden" id="channelId" value="${channelId }"/>

<!-- 주결제수단 -->
<form name="payemntForm" id="paymentForm0" style="display: none;">
	<input type="text" name="omsPayments[0].paymentMethodCd" id="paymentMethodCd" value="PAYMENT_METHOD_CD.CARD"/>	
	<input type="text" name="omsPayments[0].paymentBusinessCd" id="paymentBusinessCd" value=""/>
	<input type="text" name="omsPayments[0].paymentBusinessNm" id="paymentBusinessNm" value=""/>
	<input type="text" name="omsPayments[0].installmentCnt" id="installmentCnt" value="0"/>
	<input type="text" name="omsPayments[0].interestFreeYn" id="interestFreeYn" value="N"/>
	<input type="text" name="omsPayments[0].escrowYn" id="escrowYn" value="N"/>
	<input type="text" name="omsPayments[0].paymentAmt" id="paymentAmt" value="0"/>
	<input type="text" name="omsPayments[0].paymentFee" id="paymentFee" value="0"/>
	<input type="text" name="omsPayments[0].continuePaymentMethod" id="continuePaymentMethod" value="N"/>
</form>
<!-- POINT -->
<form name="payemntForm" id="paymentForm1" style="display: none;">
	<input type="text" name="omsPayments[1].paymentMethodCd" id="paymentMethodCd" value="PAYMENT_METHOD_CD.POINT"/>
	<input type="text" name="omsPayments[1].paymentBusinessCd" id="paymentBusinessCd" value="POINT"/>
	<input type="text" name="omsPayments[1].paymentBusinessNm" id="paymentBusinessNm" value="POINT"/>
	<input type="text" name="omsPayments[1].interestFreeYn" id="interestFreeYn" value="N"/>
	<input type="text" name="omsPayments[1].escrowYn" id="escrowYn" value="N"/>
	<input type="text" name="omsPayments[1].paymentAmt" id="paymentAmt" value="0"/>
	<input type="text" name="omsPayments[1].paymentFee" id="paymentFee" value="0"/>	
</form>
<!-- 예치금 -->
<form name="payemntForm" id="paymentForm2" style="display: none;">
	<input type="text" name="omsPayments[2].paymentMethodCd" id="paymentMethodCd" value="PAYMENT_METHOD_CD.DEPOSIT"/>
	<input type="text" name="omsPayments[2].paymentBusinessCd" id="paymentBusinessCd" value="DEPOSIT"/>
	<input type="text" name="omsPayments[2].paymentBusinessNm" id="paymentBusinessNm" value="DEPOSIT"/>
	<input type="text" name="omsPayments[2].interestFreeYn" id="interestFreeYn" value="N"/>
	<input type="text" name="omsPayments[2].escrowYn" id="escrowYn" value="N"/>
	<input type="text" name="omsPayments[2].paymentAmt" id="paymentAmt" value="0"/>
	<input type="text" name="omsPayments[2].paymentFee" id="paymentFee" value="0"/>	
</form>
<!-- 모바일상품권 -->
<form name="payemntForm" id="paymentForm3" style="display: none;">
	<input type="text" name="omsPayments[3].paymentMethodCd" id="paymentMethodCd" value="PAYMENT_METHOD_CD.VOUCHER"/>
	<input type="text" name="omsPayments[3].paymentBusinessCd" id="paymentBusinessCd" value="VOUCHER"/>
	<input type="text" name="omsPayments[3].paymentBusinessNm" id="paymentBusinessNm" value="VOUCHER"/>
	<input type="text" name="omsPayments[3].interestFreeYn" id="interestFreeYn" value="N"/>
	<input type="text" name="omsPayments[3].escrowYn" id="escrowYn" value="N"/>
	<input type="text" name="omsPayments[3].paymentAmt" id="paymentAmt" value="0"/>
	<input type="text" name="omsPayments[3].paymentFee" id="paymentFee" value="0"/>	
</form>

<!-- PG -->

<script type="text/javascript">
var paymentChg = function(obj){
	
	var paymentMethodCd = $(obj).val();
	
	$("#pgCardForm").hide();
	$("#cardInfo").hide();
	$("#pgVirtualForm").hide();
	$("#pgTransferForm").hide();
	
	if(paymentMethodCd == 'PAYMENT_METHOD_CD.CARD'){
		$("#pgCardForm").show();
		$("#cardInfo").show();
	}else if(paymentMethodCd == 'PAYMENT_METHOD_CD.VIRTUAL'){
		$("#pgVirtualForm").show();
	}else if(paymentMethodCd == 'PAYMENT_METHOD_CD.TRANSFER'){
		$("#pgTransferForm").show();
	}
	
	$("#paymentForm0").find("#paymentMethodCd").val(paymentMethodCd);
}
var pgData = function(paymentNo){
	
// 	var payForm = $("#paymentForm0");
	var mergeForm = $("#mergeForm");
	var pgForm = $("#LGD_PAYINFO").find("input");
	
	pgForm.each(function(){
		var name = $(this).attr("name");
		var id = $(this).attr("id");
		var value = $(this).attr("value");
		mergeForm.append($('<input>',{
			'type' : 'hidden',
			'name' : "omsPayments[0].omsPaymentif."+name,
			'id' : id,
			'value' : value
		}))
	});
	
	//결제번호세팅.
	mergeForm.append($('<input>',{
		'type' : 'hidden',
		'name' : "omsPayments[0].omsPaymentif.paymentNo",
		'id' : 'paymentNo',
		'value' : paymentNo
	})).append($('<input>',{
		'type' : 'hidden',
		'name' : "omsPayments[0].paymentNo",
		'id' : 'paymentNo',
		'value' : paymentNo
	}))
	
// 	var totalOrderAmt = $("#saveOrderForm").find("#totalOrderAmt").val();
// 	$("#paymentForm0").find("#paymentAmt").val(totalOrderAmt);	//결제금액
	
// 	common.mergeForms("mergeForm","paymentForm0");

// 	console.log("pgData payForm : ");
// 	console.log(payForm);
}

/*
* iframe으로 결제창을 호출하시기를 원하시면 iframe으로 설정 (변수명 수정 불가)
*/
var LGD_window_type = '';
var CST_PLATFORM = '';
/*
* 수정불가
*/
function launchCrossPlatform(){
	if(global.channel.isMobile == "true"){
		lgdwin = open_paymentwindow(document.getElementById('LGD_PAYINFO'), CST_PLATFORM, LGD_window_type);
	}else{
		lgdwin = openXpay(document.getElementById('LGD_PAYINFO'), CST_PLATFORM, LGD_window_type, null, "", "");
	}
	oms.showOrderLoading(false);
}
/*
* FORM 명만  수정 가능
*/
function getFormObject() {
        return document.getElementById("LGD_PAYINFO");
}

/*
 * 인증결과 처리
 */
function payment_return() {
	var fDoc;
	
// 	console.log("payment_return lgdwin : ");
// 	console.log(lgdwin);
	
	fDoc = lgdwin.contentWindow || lgdwin.contentDocument;
	
// 	console.log("payment_return fDoc : ");
// 	console.log(fDoc);
	
	var resultForm = $("#mergeForm");
	
	if (fDoc.document.getElementById('LGD_RESPCODE').value == "0000") {
		
			var paymentMethodCd = $("input[name=paymentMethodCd]:checked").val();
			
			if(paymentMethodCd == 'PAYMENT_METHOD_CD.CARD' || paymentMethodCd == 'PAYMENT_METHOD_CD.VIRTUAL'){
			
				//자체창				
				resultForm.find("#LGD_AUTHTYPE").val(fDoc.document.getElementById('LGD_AUTHTYPE').value);
				
				if (resultForm.find("#LGD_AUTHTYPE").val() != "ISP") {
					resultForm.find("#VBV_XID").val(fDoc.document.getElementById('VBV_XID').value);
					resultForm.find("#VBV_ECI").val(fDoc.document.getElementById('VBV_ECI').value);
					resultForm.find("#VBV_CAVV").val(fDoc.document.getElementById('VBV_CAVV').value);
					if (resultForm.find("#VBV_JOINCODE") != null) {
						resultForm.find("#VBV_JOINCODE").val(fDoc.document.getElementById('VBV_JOINCODE').value);
					}
					resultForm.find("#LGD_PAN").val(fDoc.document.getElementById('LGD_PAN').value);
					resultForm.find("#LGD_EXPYEAR").val(fDoc.document.getElementById('LGD_EXPYEAR').value);
					resultForm.find("#LGD_EXPMON").val(fDoc.document.getElementById('LGD_EXPMON').value);
					resultForm.find("#LGD_INSTALL").val(fDoc.document.getElementById('LGD_INSTALL').value);
					resultForm.find("#LGD_NOINT").val(fDoc.document.getElementById('LGD_NOINT').value);
				} else {
					resultForm.find("#KVP_QUOTA").val(fDoc.document.getElementById('KVP_QUOTA').value);
					resultForm.find("#KVP_NOINT").val(fDoc.document.getElementById('KVP_NOINT').value);
					resultForm.find("#KVP_CARDCODE").val(fDoc.document.getElementById('KVP_CARDCODE').value);
					resultForm.find("#KVP_SESSIONKEY").val(fDoc.document.getElementById('KVP_SESSIONKEY').value);
					resultForm.find("#KVP_ENCDATA").val(fDoc.document.getElementById('KVP_ENCDATA').value);
					if (fDoc.document.getElementById('LGD_KVPISP_USER') != null) {
						resultForm.find("#LGD_KVPISP_USER").val(fDoc.document.getElementById('LGD_KVPISP_USER').value);
					}
				}

// 				document.getElementById("LGD_AUTHTYPE").value = fDoc.document.getElementById('LGD_AUTHTYPE').value;
// 				if (document.getElementById("LGD_AUTHTYPE").value != "ISP") {
// 					document.getElementById("VBV_XID").value = fDoc.document.getElementById('VBV_XID').value;
// 					document.getElementById("VBV_ECI").value = fDoc.document.getElementById('VBV_ECI').value;
// 					document.getElementById("VBV_CAVV").value = fDoc.document.getElementById('VBV_CAVV').value;
// 					if (document.getElementById("VBV_JOINCODE") != null) {
// 						document.getElementById("VBV_JOINCODE").value = fDoc.document.getElementById('VBV_JOINCODE').value;
// 					}
// 					document.getElementById("LGD_PAN").value = fDoc.document.getElementById('LGD_PAN').value;
// 					document.getElementById("LGD_EXPYEAR").value = fDoc.document.getElementById('LGD_EXPYEAR').value;
// 					document.getElementById("LGD_EXPMON").value = fDoc.document.getElementById('LGD_EXPMON').value;
// 					document.getElementById("LGD_INSTALL").value = fDoc.document.getElementById('LGD_INSTALL').value;
// 					document.getElementById("LGD_NOINT").value = fDoc.document.getElementById('LGD_NOINT').value;
// 				} else {
// 					document.getElementById("KVP_QUOTA").value = fDoc.document.getElementById('KVP_QUOTA').value;
// 					document.getElementById("KVP_NOINT").value = fDoc.document.getElementById('KVP_NOINT').value;
// 					document.getElementById("KVP_CARDCODE").value = fDoc.document.getElementById('KVP_CARDCODE').value;
// 					document.getElementById("KVP_SESSIONKEY").value = fDoc.document.getElementById('KVP_SESSIONKEY').value;
// 					document.getElementById("KVP_ENCDATA").value = fDoc.document.getElementById('KVP_ENCDATA').value;
// 					if (fDoc.document.getElementById('LGD_KVPISP_USER') != null) {
// 						document.getElementById("LGD_KVPISP_USER").value = fDoc.document.getElementById('LGD_KVPISP_USER').value;
// 					}
// 				}
		// 		document.getElementById("LGD_PAYINFO").target = "_self";
		// 		document.getElementById("LGD_PAYINFO").action = "payres.jsp";
		// 		document.getElementById("LGD_PAYINFO").submit();					
			}else{
				//일반
				resultForm.find("#LGD_PAYKEY").val(fDoc.document.getElementById('LGD_PAYKEY').value);								
			}
			resultForm.find("#LGD_ESCROWYN").val(fDoc.document.getElementById('LGD_ESCROWYN').value);
			
// 			console.log("PG return param success!!!!!!!");
// 			console.log(resultForm);						
						
			order();
	
	} else {
// 		alert("LGD_RESPCODE (결과코드) : " + fDoc.document.getElementById('LGD_RESPCODE').value + "\n" + "LGD_RESPMSG (결과메시지): " + fDoc.document.getElementById('LGD_RESPMSG').value);
		alert(fDoc.document.getElementById('LGD_RESPMSG').value);
		closeIframe();
		oms.showOrderLoading(false);
	}

}
////////////////////////////////////////crossplatform //////////////////////////////////////////

var pay = function(){
	
	if(formSetting()){
		var totalOrderAmt = $("#saveOrderForm").find("#totalOrderAmt").val();
		if(Number(totalOrderAmt) == 0){
			$("#paymentForm0").find("#paymentMethodCd").val("");
			
			var data;
			oms.showOrderLoading(true);
			$.ajax({ 				
				url : oms.url.pgParam,
				type : "POST",		
				data : data
			}).done(function(response){
				if(response.RESULT == "SUCCESS"){
					//alert("저장되었습니다.");
//		 			console.log(response);
					order();	
				}else{
					alert(response.MESSAGE);
					oms.showOrderLoading(false);
				}				
			});			
		}else{
			var paymentMethodCd = $("input:radio[name=paymentMethodCd]:checked").val();
			
			oms.showOrderLoading(true);
					
			if(paymentMethodCd == 'PAYMENT_METHOD_CD.CARD' || paymentMethodCd == 'PAYMENT_METHOD_CD.VIRTUAL'
			|| paymentMethodCd == 'PAYMENT_METHOD_CD.TRANSFER' || paymentMethodCd == 'PAYMENT_METHOD_CD.MOBILE'){
				pay_LG(paymentMethodCd);
			}else if(paymentMethodCd == 'PAYMENT_METHOD_CD.KAKAO'){
				pay_KAKAO();
			}else if(paymentMethodCd == 'PAYMENT_METHOD_CD.PAYCO'){
				
			}
		}
	}
		
}

//LG 결제 
var pay_LG = function(paymentMethodCd){
	
	var form = $("#payparam");
	
	var orderFormData = orderInfo();
	var orderProductNos = getOrderproductNos();
	var productInfo = "";
	var productCode = "";
	
	for(var i=0;i<orderProductNos.length;i++){
		if(i == 0){
			productCode = $("#productForm"+orderProductNos[i].value).find("#productId").val();
			productInfo = $("#productForm"+orderProductNos[i].value).find("#productName").val();
		}
	}
	
	if(orderProductNos.length > 1){
		productInfo = productInfo + "외 "+ (Number(orderProductNos.length)-1) + "건";
	}
	
	var totalOrderAmt = $("#saveOrderForm").find("#totalOrderAmt").val();
	
	var usablepay = "";
	
	//카드
	if(paymentMethodCd == 'PAYMENT_METHOD_CD.CARD'){
		usablepay = "SC0010";
		var pgCardForm = $("#pgCardForm");
		var LGD_CARDTYPE = pgCardForm.find("#pg_LGD_CARDTYPE option:selected").val();
		var LGD_INSTALL = pgCardForm.find("#pg_LGD_INSTALL option:selected").val();
		var LGD_SP_CHAIN_CODE = pgCardForm.find("#pg_LGD_SP_CHAIN_CODE option:selected").val();
		var LGD_POINTUSE = pgCardForm.find("#pg_LGD_POINTUSE option:selected").val();
		
		LGD_SP_CHAIN_CODE = "0";
			
		if(LGD_CARDTYPE == 'XX'){
			alert("카드종류를 선택하세요.");
			oms.showOrderLoading(false);
			return false;
		}
		if(common.isEmpty(LGD_INSTALL)){
			alert("할부개월수를 선택하세요.");
			oms.showOrderLoading(false);
			return false;
		}
		if(common.isEmpty(LGD_POINTUSE)){
			alert("포인트사용여부를 선택하세요.");
			oms.showOrderLoading(false);
			return false;
		}
		if(common.isEmpty(LGD_SP_CHAIN_CODE)){
			alert("간편결제사용여부를 선택하세요.");
			oms.showOrderLoading(false);
			return false;
		}
		
		form.find("#LGD_CARDTYPE").val(LGD_CARDTYPE);	
		form.find("#LGD_INSTALL").val(LGD_INSTALL);
		form.find("#LGD_SP_CHAIN_CODE").val(LGD_SP_CHAIN_CODE);
		form.find("#LGD_POINTUSE").val(LGD_POINTUSE);
		
		form.find("#LGD_CUSTOM_PROCESSTYPE").val("AUTHTR");
		form.find("#LGD_SELF_CUSTOM").val("Y");		//자체창 사용여부
		
		if(global.channel.isMobile == "true"){
			form.find("#LGD_VERSION").val("JSP_NonActiveX_Mobile_CardApp");
		}else{
			form.find("#LGD_VERSION").val("JSP_Non-ActiveX_CardApp");
		}
		
	}	
	//계좌이체
	else if(paymentMethodCd == 'PAYMENT_METHOD_CD.TRANSFER'){
		usablepay = "SC0030";
		
		var pgTransferForm = $("#pgTransferForm");
		var LGD_CASHRECEIPTYN = pgTransferForm.find("#pg_LGD_CASHRECEIPTYN option:selected").val();
		var LGD_USABLEBANK = pgTransferForm.find("#pg_LGD_USABLEBANK option:selected").val();
		var LGD_ESCROW_USEYN = pgTransferForm.find("#pg_LGD_ESCROW_USEYN option:selected").val();
		
		if(__isMobile != "true" && common.isEmpty(LGD_USABLEBANK)){
			alert("출금은행을 선택하세요.");
			oms.showOrderLoading(false);
			return false;
		}
		
		if(common.isEmpty(LGD_ESCROW_USEYN)){
			alert("에스크로사용여부를 선택하세요.");
			oms.showOrderLoading(false);
			return false;
		}
		
		if(common.isEmpty(LGD_CASHRECEIPTYN)){
			alert("현금영수증발급여부를 선택하세요.");
			oms.showOrderLoading(false);
			return false;
		}
		
		form.find("#LGD_USABLEBANK").val(LGD_USABLEBANK);	//은행
		form.find("#LGD_CASHRECEIPTYN").val(LGD_CASHRECEIPTYN);	//현금영수증발급여부				
		form.find("#LGD_CUSTOM_PROCESSTYPE").val("TWOTR");
		form.find("#LGD_SELF_CUSTOM").val("N");		//자체창 사용여부
		form.find("#LGD_VERSION").val("JSP_Non-ActiveX_Standard");		
		form.find("#LGD_ESCROW_USEYN").val(LGD_ESCROW_USEYN);	//에스크로사용여부		
	}
	//가상계좌
	else if(paymentMethodCd == 'PAYMENT_METHOD_CD.VIRTUAL'){
		usablepay = "SC0040";
		var pgVirtualForm = $("#pgVirtualForm");
		var LGD_BANKCODE = pgVirtualForm.find("#pg_LGD_BANKCODE option:selected").val();
		var LGD_CASHRECEIPTUSE = pgVirtualForm.find("#pg_LGD_CASHRECEIPTUSE option:selected").val();
		var LGD_CASHRECEIPTUSE_SEL = pgVirtualForm.find("#pg_LGD_CASHRECEIPTUSE_SEL option:selected").val();
		
		var cashnum1 = pgVirtualForm.find("#pg_LGD_CASHCARDNUM1").val();
		var cashnum2 = pgVirtualForm.find("#pg_LGD_CASHCARDNUM2").val();
		var cashnum3 = pgVirtualForm.find("#pg_LGD_CASHCARDNUM3").val();
		var cashnum4 = pgVirtualForm.find("#pg_LGD_CASHCARDNUM4").val();
		var cashnum = 	cashnum1 + cashnum2 + cashnum3 + cashnum4; 
		
		pgVirtualForm.find("#pg_LGD_CASHCARDNUM").val(cashnum);
		
		var LGD_CASHCARDNUM = pgVirtualForm.find("#pg_LGD_CASHCARDNUM").val();
		var LGD_ACCOUNTOWNER = orderFormData["name1"];	//입금자명
		var LGD_BUYERPHONE = orderFormData["phone2"];	//구매자휴대폰
		var LGD_ESCROW_USEYN = pgVirtualForm.find("#pg_LGD_ESCROW_USEYN option:selected").val();
// 		console.log(LGD_BANKCODE);
		if(common.isEmpty(LGD_BANKCODE)){
			alert("입금은행을 선택하세요.");
			oms.showOrderLoading(false);
			return false;
		}
		
		if(common.isEmpty(LGD_ESCROW_USEYN)){
			alert("에스크로사용여부를 선택하세요.");
			oms.showOrderLoading(false);
			return false;
		}
		
		if(common.isEmpty(LGD_CASHRECEIPTUSE)){
			alert("현금영수증발급여부를 선택하세요.");
			oms.showOrderLoading(false);
			return false;
		}
		
		if(LGD_CASHRECEIPTUSE != "0"){
			if(common.isEmpty(LGD_CASHCARDNUM)){
				if(LGD_CASHRECEIPTUSE == "1"){
					if(LGD_CASHRECEIPTUSE_SEL == "0"){
						alert("현금영수증 휴대폰번호를 입력하세요.");	
					}else if(LGD_CASHRECEIPTUSE_SEL == "1"){
						alert("신용카드번호를 입력하세요.");
					}else if(LGD_CASHRECEIPTUSE_SEL == "2"){
						alert("현금영수증 카드번호를 입력하세요.");
					}
				} else {
					alert("사업자 등록번호를 입력하세요.");
				}
				oms.showOrderLoading(false);
				return false;
			}else{
				var checkFlag = true;
				if(LGD_CASHRECEIPTUSE == "1"){	
					if(LGD_CASHRECEIPTUSE_SEL == "0"){ //휴대폰 번호 check
						if(!oms.validatePhoneNum(cashnum)){
							alert("현금영수증 휴대폰번호를 정확히 입력하세요.");
							checkFlag = false;
						}	
					}else if(LGD_CASHRECEIPTUSE_SEL == "1"){	//신용카드 번호 check
						if(!oms.validateCardNum(cashnum)){
							alert("신용카드번호를 정확히 입력하세요.");
							checkFlag = false;
						}
					}else if(LGD_CASHRECEIPTUSE_SEL == "2"){	//현금영수증 카드번호 check
						if(cashnum1.length < 4 || cashnum2.length < 4 || cashnum3.length < 4 || cashnum4.length < 6){
							alert("현금영수증 카드번호를 정확히 입력하세요.");
							checkFlag = false;
						}
					}					
				}else if(LGD_CASHRECEIPTUSE == "2"){	//사업자등록번호check					
					if(!oms.validateBizNum(cashnum)){
						alert("사업자 등록번호를 정확히 입력하세요.");
						checkFlag = false;
					}
				}
				
				if(checkFlag) { 
					LGD_CASHRECEIPTYN = "Y";
				}else{
					oms.showOrderLoading(false);
					return false;
				}
					
			}
		}
		
		form.find("#LGD_BANKCODE").val(LGD_BANKCODE);		//은행
		form.find("#LGD_CASHRECEIPTYN").val(LGD_CASHRECEIPTYN);	//현금영수증발급여부
		form.find("#LGD_CASHRECEIPTUSE").val(LGD_CASHRECEIPTUSE);	//현금영수증
		form.find("#LGD_CASHCARDNUM").val(LGD_CASHCARDNUM);	//현금영수증번호
		form.find("#LGD_ACCOUNTOWNER").val(LGD_ACCOUNTOWNER);	//입금자명
		form.find("#LGD_BUYERPHONE").val(LGD_BUYERPHONE);	//휴대폰
		form.find("#LGD_ESCROW_USEYN").val(LGD_ESCROW_USEYN);	//에스크로사용여부		
		
		form.find("#LGD_CUSTOM_PROCESSTYPE").val("AUTHTR");
		form.find("#LGD_SELF_CUSTOM").val("Y");		//자체창 사용여부
		form.find("#LGD_METHOD").val("ASSIGN");		//요청기능(할당)
		if(global.channel.isMobile == "true"){
			form.find("#LGD_VERSION").val("JSP_Non-ActiveX_SmartXPay");
		}else{
			form.find("#LGD_VERSION").val("JSP_Non-ActiveX_CardApp");
		}
	}
	//휴대폰
	else if(paymentMethodCd == 'PAYMENT_METHOD_CD.MOBILE'){
		usablepay = "SC0060";
		form.find("#LGD_CUSTOM_PROCESSTYPE").val("TWOTR");
		form.find("#LGD_SELF_CUSTOM").val("N");		//자체창 사용여부
		if(global.channel.isMobile == "true"){
			form.find("#LGD_VERSION").val("JSP_Non-ActiveX_SmartXPay");
		}else{
			form.find("#LGD_VERSION").val("JSP_Non-ActiveX_Standard");
		}
	}
	
	form.find("#LGD_BUYER").val(orderFormData["name1"]);
	form.find("#LGD_PRODUCTCODE").val(productCode);
	form.find("#LGD_PRODUCTINFO").val(productInfo);
	form.find("#LGD_AMOUNT").val(totalOrderAmt);
	form.find("#LGD_BUYEREMAIL").val(orderFormData["email"]);
	form.find("#LGD_CUSTOM_USABLEPAY").val(usablepay);
	
// 	console.log("pay_cross form : ");
// 	console.log(form);
	
	var data = form.serialize();
	
	$.ajax({ 				
		url : oms.url.pgParam,
		type : "POST",		
		data : data
	}).done(function(response){
		if(response.RESULT == "SUCCESS"){
			//alert("저장되었습니다.");
// 			console.log(response);
			pay_callback(paymentMethodCd,response.omsPaymentif);	
		}else{
			alert(response.MESSAGE);
			oms.showOrderLoading(false);
		}
		
	});
}

var pay_callback = function(paymentMethodCd,data){
	
	var form = $("#LGD_PAYINFO");
	form.html("");	//form 초기화
	
	common.mergeForms("LGD_PAYINFO","payresult");
	
	var escrowYn = "N";
// 	console.log("pay_callback data : ");
// 	console.log(data);
	$.each(data,function(key,value){
// 		console.log("pay_callback data key : "+key);
// 		console.log("pay_callback data value : "+value);
		key = key.toUpperCase();
		
		if(key == "LGD_WINDOW_TYPE"){
			LGD_window_type = value;
		}
		if(key == "CST_PLATFORM"){
			CST_PLATFORM = value;
		}	
		if(common.isNotEmpty(value)){
			form.append($('<input>',{
				'type' : 'hidden',
				'name' : key,
				'id' : key,
				'value' : value
			}))
		}
		
		if(key == "LGD_ESCROW_USEYN"){
			escrowYn = value;	
		}
		
	});
	
	if(escrowYn == "Y"){
		escrowData();
	}	
	
	var paymentNo = form.find("#LGD_OID").val();
// 	console.log(paymentNo);			
// 	console.log("pay_callback form :");
// 	console.log(form);	
	
	//PG DATA 처리
	pgData(paymentNo);
	
	if(paymentMethodCd == 'PAYMENT_METHOD_CD.VIRTUAL' || global.channel.isMobile == "true"){
		order();
	}else{
		launchCrossPlatform();
	}		
}

var escrowData = function(){
	var escrowArr = [];
	var cnt = getDeliveryCnt();
	var productCnt = 0;
	for(var i=0;i<cnt;i++){
		var address = addressInfo(i);
		var name1 = address["deliveryName1"];
		var countryNo = address["countryNo"];
		var zipCd = address["zipCd"];
		var address1 = address["address1"];
		var address2 = address["address2"];
		var phone1 = address["phone1"];
		var phone2 = address["phone2"];
		var email = address["email"];
		
		var addrOrderProductNos = $("#addressForm"+i).find("#addrOrderProductNo");
		for(var j=0;j<addrOrderProductNos.length;j++){
			var escrowObj = {LGD_ESCROW_GOODID:''
							,LGD_ESCROW_GOODNAME:''
							,LGD_ESCROW_GOODCODE:''
							,LGD_ESCROW_UNITPRICE:''
							,LGD_ESCROW_QUANTITY:''
							,LGD_ESCROW_ZIPCODE:''
							,LGD_ESCROW_ADDRESS1:''
							,LGD_ESCROW_ADDRESS2:''
							,LGD_ESCROW_BUYERPHONE:''
							};
			var opn = addrOrderProductNos[j].value;
			var orderQty = $("#addressForm"+i).find("#orderQty"+opn).val();
			if(Number(orderQty) > 0){
				var productId = $("#productForm"+opn).find("#productId").val();
				var name = $("#productForm"+opn).find("#deliveryName").val();
				var totalSalePrice = $("#addressForm"+i).find("#totalSalePrice"+opn).val();
// 				console.log(escrowObj);
				escrowObj.LGD_ESCROW_GOODID = opn;
				escrowObj.LGD_ESCROW_GOODNAME = name;
				escrowObj.LGD_ESCROW_GOODCODE = productId;
				escrowObj.LGD_ESCROW_UNITPRICE = totalSalePrice;
				escrowObj.LGD_ESCROW_QUANTITY = orderQty;
				escrowObj.LGD_ESCROW_ZIPCODE = zipCd;
				escrowObj.LGD_ESCROW_ADDRESS1 = address1;
				escrowObj.LGD_ESCROW_ADDRESS2 = address2;
				escrowObj.LGD_ESCROW_BUYERPHONE = phone2;
				
				escrowArr.push(escrowObj);
			}
			productCnt++;
		}
	}
	
	var form = $("#LGD_PAYINFO");
// 	for(var i=0;i<escrowArr.length;i++){
// 		var ec = escrowArr[i];
// 		for(key in ec){
// 			form.append($('<input>',{
// 				'type' : 'hidden',
// // 				'name' : "omsPaymentEscrows["+i+"]."+key,
// 				'name' : key,
// 				'id' : key,
// 				'value' : ec[key]
// 			}))
// 		}
// 	}

	if(escrowArr.length > 0){
		var ec = escrowArr[0];
		for(key in ec){
			var value = ec[key];
			if("LGD_ESCROW_GOODNAME" == key && productCnt > 1){
				value = value + "외 "+(Number(productCnt)-1)+"건";
			}
			form.append($('<input>',{
				'type' : 'hidden',
// 				'name' : "omsPaymentEscrows["+i+"]."+key,
				'name' : key,
				'id' : key,
				'value' : value
			}))
		}
	}
}

</script>
<form method="post" name="payparam" id="payparam" style="display: none;">	
	<input type="hidden" name="LGD_VERSION" id="LGD_VERSION" value="JSP_Non-ActiveX_Standard"/>
	<input type="hidden" name="LGD_BUYER" id="LGD_BUYER" value=""/>			<!-- 구매자 이름 -->
<!-- 	<input type="text" name="LGD_BUYERID" id="LGD_BUYERID" value=""/>		구매자 ID -->
	<input type="hidden" name="LGD_PRODUCTCODE" id="LGD_PRODUCTCODE" value=""/>	<!-- 상품ID -->
	<input type="hidden" name="LGD_PRODUCTINFO" id="LGD_PRODUCTINFO" value=""/>	<!-- 상품정보 -->
	<input type="hidden" name="LGD_AMOUNT" id="LGD_AMOUNT" value=""/>			<!-- 결제금액 -->
	<input type="hidden" name="LGD_BUYEREMAIL" id="LGD_BUYEREMAIL" value=""/>		<!-- 구매자 이메일 -->
	<input type="hidden" name="LGD_OID" id="LGD_OID" value=""/>			<!-- 결제번호 -->
	<input type="hidden" name="LGD_CUSTOM_USABLEPAY" id="LGD_CUSTOM_USABLEPAY" value=""/><!-- 초기결제수단 -->
	
	<input type="hidden" name="LGD_CUSTOM_PROCESSTYPE" id="LGD_CUSTOM_PROCESSTYPE" value=""/><!-- 결제 프로세스유형 (자체창 :  AUTHTR, 일반 : TWOTR-->	
	
	<input type="text" name="LGD_USABLEBANK" id="LGD_USABLEBANK" value=""/><!-- 계좌이체은행 -->
	
	<!-- 자체창(카드) -->
	
	<input type="text" name="LGD_SELF_CUSTOM" id="LGD_SELF_CUSTOM" value=""/><!-- 자체창 사용여부 -->
	
	<input type="text" name="LGD_CARDTYPE" id="LGD_CARDTYPE" value=""/><!-- 카드 -->	
	<input type="text" name="LGD_INSTALL" id="LGD_INSTALL" value=""/><!-- 할부개월수 -->
	<input type="text" name="LGD_SP_CHAIN_CODE" id="LGD_SP_CHAIN_CODE" value=""/><!-- 간편결제사용여부 -->
	<input type="text" name="LGD_POINTUSE" id="LGD_POINTUSE" value=""/><!-- 포인트사용여부 -->	
	<input type="text" name="LGD_NOINT" id="LGD_NOINT" value="0"/><!-- 무이자여부 -->
	<!-- 삼성카드 간편결제 key id -->
	<input type="text" name="LGD_SP_ORDER_USER_ID" id="LGD_SP_ORDER_USER_ID" value=""/><!-- 삼성카드 간편결제는 일부 허용한 가맹점만 가능하고, KEY_ID 를 생성해서 보내야함-->	
	
	<!-- 자체창(무통장) -->
	<input type="text" name="LGD_BANKCODE" id="LGD_BANKCODE" value=""/><!-- 입금계좌 -->
	<input type="text" name="LGD_CASHRECEIPTYN" id="LGD_CASHRECEIPTYN" value="N"/><!-- 현금영수증 발급여부 -->
	<input type="text" name="LGD_CASHRECEIPTUSE" id="LGD_CASHRECEIPTUSE" value=""/><!-- 현금영수증 종류 -->
	<input type="text" name="LGD_CLOSEDATE" id="LGD_CLOSEDATE" value=""/><!-- 입금기한 -->
	<input type="text" name="LGD_TAXFREEAMOUNT" id="LGD_TAXFREEAMOUNT" value="0"/><!-- 면세금액 -->
	
	<input type="text" name="LGD_CASHCARDNUM" id="LGD_CASHCARDNUM" value=""/><!-- 현금영수증발급번호 -->
	<input type="text" name="LGD_ACCOUNTOWNER" id="LGD_ACCOUNTOWNER" value=""/><!-- 입금자명 -->
	<input type="text" name="LGD_ACCOUNTPID" id="LGD_ACCOUNTPID" value=""/><!-- 구매자 개인식별변호 (6자리~13자리)(옵션) -->
	<input type="text" name="LGD_BUYERPHONE" id="LGD_BUYERPHONE" value=""/><!-- 구매자휴대폰번호-->
	<input type="text" name="LGD_METHOD" id="LGD_METHOD" value=""/><!-- 요청기능 -->
	<input type="text" name="LGD_ESCROW_USEYN" id="LGD_ESCROW_USEYN" value="N"/><!-- 에스크로사용여부 -->
	<!--
		<option value="ASSIGN">할당</option>
		<option value="CHANGE">변경</option> 
	 -->	 		
	 
	<input type="text" name="LGD_DISABLE_AGREE" id="LGD_DISABLE_AGREE" value="Y" /><!-- 약관동의미표시여부 -->
	<input type="text" name="LGD_OSTYPE_CHECK" id="LGD_OSTYPE_CHECK" value="P" /><!-- M:모바일 P:PC -->
</form>
<form id="payresult" style="display: none;">
<!-- result -->
	<input type="hidden" name="LGD_PAYKEY" id="LGD_PAYKEY" value=""/>	
	 
    <input type="text" name="LGD_AUTHTYPE" id="LGD_AUTHTYPE" value=""/>
    <input type="text" name="LGD_CURRENCY" id="LGD_CURRENCY" value="410"/>
    <input type="text" name="KVP_CURRENCY" id="KVP_CURRENCY" value=""/>
    <input type="text" name="KVP_OACERT_INF" id="KVP_OACERT_INF" value=""/>
    <input type="text" name="KVP_RESERVED1" id="KVP_RESERVED1" value=""/>
    <input type="text" name="KVP_RESERVED2" id="KVP_RESERVED2" value=""/>
    <input type="text" name="KVP_RESERVED3" id="KVP_RESERVED3" value=""/>
    <input type="text" name="KVP_GOODNAME" id="KVP_GOODNAME" value=""/>
    <input type="text" name="KVP_CARDCOMPANY" id="KVP_CARDCOMPANY" value=""/>
    <input type="text" name="KVP_PRICE" id="KVP_PRICE" value=""/>
    <input type="text" name="KVP_PGID" id="KVP_PGID" value=""/>
    <input type="text" name="KVP_QUOTA" id="KVP_QUOTA" value=""/>
    <input type="text" name="KVP_NOINT" id="KVP_NOINT" value=""/> 
	<input type="hidden" name="KVP_SESSIONKEY" id="KVP_SESSIONKEY"  value=""/> 
	<input type="hidden" name="KVP_ENCDATA" id="KVP_ENCDATA" value=""/> 
	<input type="text" name="KVP_CARDCODE" id="KVP_CARDCODE" value=""/> 
	<input type="text" name="KVP_CONAME" id="KVP_CONAME" value=""/> 
	<input type="text" name="LGD_PAN" id="LGD_PAN"  value=""/>
	<input type="text" name="VBV_ECI" id="VBV_ECI" value=""/>
	<input type="text" name="VBV_CAVV" id="VBV_CAVV"  value=""/>
	<input type="text" name="VBV_XID" id="VBV_XID"  value=""/>
	<input type="text" name="VBV_JOINCODE" id="VBV_JOINCODE"  value=""/>
	<input type="text" name="LGD_EXPYEAR" id="LGD_EXPYEAR"  value=""/>
	<input type="text" name="LGD_EXPMON" id="LGD_EXPMON"  value=""/>
	<input type="text" name="LGD_LANGUAGE" id="LGD_LANGUAGE"  value=""/>
	<input type="text" name="LGD_ESCROWYN" id="LGD_ESCROWYN"  value="N"/>
<!-- 	<input type="text" name="LGD_ESCROW_USEYN" id="LGD_ESCROW_USEYN"  value="N"/> -->
</form>
<form method="post" name="LGD_PAYINFO" id="LGD_PAYINFO"></form>


<!-- KAKAO -->


<link href="https://pg.cnspay.co.kr:443/dlp/css/kakaopayDlp.css" rel="stylesheet" type="text/css" />

<script type="text/javascript">
var kakaoData = function(){
	
	var mergeForm = $("#mergeForm");
	var payForm = $("#payForm").find("input");
	
	payForm.each(function(){
		var name = $(this).attr("name");
		var id = $(this).attr("id");
		var value = $(this).attr("value");
		mergeForm.append($('<input>',{
			'type' : 'hidden',
			'name' : "omsPayments[0].kakao."+name,
			'id' : id,
			'value' : value
		}))
	});
	
	common.mergeForms('mergeForm','payForm');

}
var pay_KAKAO = function(){
	
	var form = $("#payForm");
	
	var orderFormData = orderInfo();
	var orderProductNos = getOrderproductNos();
	var productInfo = "";	
	
	for(var i=0;i<orderProductNos.length;i++){
		if(i == 0){
			productInfo = $("#productForm"+orderProductNos[i].value).find("#productName").val();
		}
	}
	
	if(orderProductNos.length > 1){
		productInfo = productInfo + "외 "+ (Number(orderProductNos.length)-1) + "건";
	}
	
	var totalOrderAmt = $("#saveOrderForm").find("#totalOrderAmt").val();
// 	console.log(form.find("#buyerName"));
	form.find("#buyerName").val(orderFormData["name1"]);
	form.find("#goodsName").val(productInfo);
	form.find("#amt").val(totalOrderAmt);
	form.find("#buyerEmail").val(orderFormData["email"]);	
	
// 	console.log("pay_KAKAO form : ");
// 	console.log(form);

// 	kakaoData();        	
//     order("test");

	getTxnId();
}

    /**
    cnspay  를 통해 결제를 시작합니다.
    */
    function cnspay() {
        
        // TO-DO : 가맹점에서 해줘야할 부분(TXN_ID)과 KaKaoPay DLP 호출 API
        // 결과코드가 00(정상처리되었습니다.)
        if(document.payForm.resultCode.value == '00') {
            // TO-DO : 가맹점에서 해줘야할 부분(TXN_ID)과 KaKaoPay DLP 호출 API
            kakaopayDlp.setTxnId(document.payForm.txnId.value);
            
    		if(global.channel.isMobile == "true"){
    			kakaopayDlp.setChannelType('MPM', 'WEB'); // 모바일 웹(브라우저)결제
	            //kakaopayDlp.addRequestParams({ MOBILE_NUM : '010-1234-5678'}); // 초기값 세팅
    		}else{
	            kakaopayDlp.setChannelType('WPM', 'TMS'); // PC결제
    		}
            
            kakaopayDlp.callDlp('kakaopay_layer', document.payForm, submitFunc);
            
        } else {
//             alert('[RESULT_CODE] : ' + document.payForm.resultCode.value + '\n[RESULT_MSG] : ' + document.payForm.resultMsg.value);
			alert(document.payForm.resultMsg.value);
			oms.showOrderLoading(false);
        }
        
    }
    
    function getTxnId(){
    	document.payForm.acceptCharset = "utf-8";
    	var data = $("#payForm");
    	$.ajax({ 				
    		url : oms.url.kakaoTxnid,
    		type : "POST",		
    		data : data.serialize()
    	}).done(function(response){
//     		console.log(response);
    		if(response.resultCode == "00"){
    			
    			data.find("#mid").val(response.mid);
    			data.find("#ediDate").val(response.ediDate);
    			data.find("#encryptData").val(response.hash_string);
    			data.find("#merchantEncKey").val(response.merchantEncKey);
    			data.find("#merchantHashKey").val(response.merchantHashKey);
//     			data.find("#requestDealApproveUrl").val(response.requestDealApproveUrl);    			
    			
    			data.find("#resultCode").val(response.resultCode);
    			data.find("#resultMsg").val(response.resultMsg);
    			data.find("#txnId").val(response.txnId);
    			data.find("#merchantTxnNum").val(response.merchantTxnNum);
    			data.find("#prDt").val(response.prDt); 
//     			var formInput = data.find('input');
//     			formInput.each(function(){
//     				var name;
//     				var id;
//     				var value;
    				
//    					name = $(this).attr("name");
//    					id = $(this).attr("id");
//    					value = $(this).attr("value");				
   					
// //    					console.log("id",id);
// //    					console.log("response",response[id]);
//    					if(common.isEmpty(value)){
// 	    				data.find("#"+id).val(response[id]);
//    					}
//     			});
				document.payForm.acceptCharset = "euc-kr";
    			cnspay();    			
    		}else{
    			alert(response.resultMsg);
    			oms.showOrderLoading(false);
    		}
    		
    	});
    	
    	
    	  // 카카오페이는 인증요청시 UTF-8, 결제요청시 euc-kr을 사용하며 해당 인코딩값이 맞지않을시 한글이 깨질 수 있습니다.
        
        // form에 iframe 주소 세팅
//         document.payForm.target = "txnIdGetterFrame";
//         document.payForm.action = "getTxnId.jsp";
//         document.payForm.acceptCharset = "utf-8";
//         if (payForm.canHaveHTML) { // detect IE
//             document.charset = payForm.acceptCharset;
//         }
        
//         // post로 iframe 페이지 호출
//         document.payForm.submit();
        
        // payForm의 타겟, action을 수정한다
//         document.payForm.target = "";
//         document.payForm.action = "kakaopayLiteResult.jsp";
//         document.payForm.acceptCharset = "euc-kr";
//         if (payForm.canHaveHTML) { // detect IE
//             document.charset = payForm.acceptCharset;
//         }
        // getTxnId.jsp의 onload 이벤트를 통해 cnspay() 호출
        
    }
    
    var submitFunc = function cnspaySubmit(data){
        
        if(data.RESULT_CODE === '00') {
            
            // 매뉴얼 참조하여 부인방지코드값 관리
            
//             document.payForm.submit();
        	//kakao DATA 처리
        	kakaoData();
			order();        	
        	
        } else if(data.RESULT_CODE === 'KKP_SER_002') {
            // X버튼 눌렀을때의 이벤트 처리 코드 등록
//             alert('[RESULT_CODE] : ' + data.RESULT_CODE + '\n[RESULT_MSG] : ' + data.RESULT_MSG);
            alert(data.RESULT_MSG);
            oms.showOrderLoading(false);
        } else {
//             alert('[RESULT_CODE] : ' + data.RESULT_CODE + '\n[RESULT_MSG] : ' + data.RESULT_MSG);
			alert(data.RESULT_MSG);
			oms.showOrderLoading(false);
        }
        
    };
        
</script>
<form name="payForm" id="payForm" method="post" accept-charset = "utf-8" style="display: none;">
<!-- 결제 파라미터 목록 -->
        <b>결제 변수 목록(매뉴얼 참조)</b>
        (*) 필수
        <ul>
            <li>(*)결제수단 : <input type="checkbox" name="PayMethod" id="payMethod" value="KAKAOPAY" checked="checked"/>KAKAOPAY 고정</li>
            <li>(*)상품명 : <input name="GoodsName" id="goodsName" type="text" value=""/></li>
            <li>(*)상품가격 : <input name="Amt" id="amt" type="text" value=""/></li>
            <li>공급가액 : <input name="SupplyAmt" id="supplyAmt" type="text" value="0"/></li>
            <li>부가세 : <input name="GoodsVat" id="goodsVat" type="text" value="0"/></li>
            <li>봉사료 : <input name="ServiceAmt" id="serviceAmt" type="text" value="0"/></li>
            <li>(*)상품갯수 : <input name="GoodsCnt" id="goodsCnt" type="text"  value="1" readonly="readonly" style="background-color: #e2e2e2;" />고정</li>
            <li>(*)가맹점ID : <input name="MID" id="mid" type="text" value="" /></li>
            <li>(*)인증플래그 : <input name="AuthFlg" id="authFlg" type="text" value="10" readonly="readonly" style="background-color: #e2e2e2;" /> 고정</li>
            <li>(*)EdiDate : <input name="EdiDate" id="ediDate" type="text" value="" readonly="readonly" style="background-color: #e2e2e2;"/></li>
            <li>(*)EncryptData : <input name="EncryptData" id="encryptData" type="text" value="" readonly="readonly" style="background-color: #e2e2e2;"/></li>
            <li>구매자 이메일 : <input name="BuyerEmail" id="buyerEmail" type="text" value=""/></li>
            <li>(*)구매자명 : <input name="BuyerName" id="buyerName" type="text" value=""/></li>
        </ul>
        
        
        
        <!-- 인증 파라미터 목록 -->
        <b>인증 변수 목록(매뉴얼 참조)</b>
        <ul>
            <li>상품제공기간 플래그 : <input name="offerPeriodFlag" id="offerPeriodFlag" type="text" value="Y"/></li>
            <li>상품제공기간 : <input name="offerPeriod" id="offerPeriod" type="text" value="제품표시일까지"/></li>
            <li>(*)인증구분 : <input type="text" name="certifiedFlag" id="certifiedFlag" value="CN" readonly="readonly" style="background-color: #e2e2e2;" /> 고정</li>
            <li>(*)거래통화 : <input type="text" name="currency" id="currency" value="KRW" readonly="readonly" style="background-color: #e2e2e2;" /> 고정</li>
            <li>(*)가맹점 암호화키 : <input type="text" name="merchantEncKey" id="merchantEncKey" value="" /></li>
            <li>(*)가맹점 해쉬키 : <input type="text" name="merchantHashKey" id="merchantHashKey" value="" /></li>
            <li>(*)TXN_ID 요청URL : <input type="text" name="requestDealApproveUrl" id="requestDealApproveUrl" value="" /></li>
            <li>
            (*)결제요청타입 :
            <c:choose>
            <c:when test="${isMobile }"> 
            	<input type="text" name="prType" value="MPM"/>
            	<input type="text" name="channelType" value="2"/>
            </c:when> 
            <c:otherwise>
            	<input type="text" name="prType" value="WPM"/>
            	<input type="text" name="channelType" value="4"/>
            </c:otherwise>
            </c:choose>
<!--             <select name ="prType"> -->
<!--                 <option value="MPM">MPM</option> -->
<!--                 <option value="WPM" selected="selected">WPM</option> -->
<!--             </select> -->
            MPM : 모바일결제, WPM : PC결제
            </li>
            <li>
            (*)채널타입 :
<!--             <input type="text" name="channelType" value="4"/>   -->
<!--             <select name ="channelType"> -->
<!--                 <option value="2">2</option> -->
<!--                 <option value="4" selected="selected">4</option> -->
<!--             </select> -->
            2: 모바일결제, 4: PC결제
            </li>
            <li>(*)가맹점 거래번호 : <input type="text" name="merchantTxnNum" id="merchantTxnNum" value="" /></li>
            
        </ul>
        
        
        <!-- 인증 파라미터 중 할부결제시 사용하는 파라미터 목록 -->
        <!-- 파라미터 입력형태는 매뉴얼 참조  -->
        <b>할부결제시 선택변수 목록</b>
        - 옳은 값들을 넣지 않으면 무이자를 사용하지 않는것으로 한다.
        
        <b>카드코드(매뉴얼 참조)</b>
        - 비씨:01, 국민:02, 외환:03, 삼성:04, 신한:06, 현대:07, 롯데:08, 한미:11, 씨티:11,  
        NH채움(농협):12, 수협:13, 신협:13, 우리:15, 하나SK:16, 주택:18, 조흥(강원):19, 
        광주:21, 전북:22, 제주:23, 해외비자:25, 해외마스터:26, 해외다이너스:27, 
        해외AMX:28, 해외JCB:29, 해외디스커버:30, 은련:34
        <ul>
            <li>카드선택 : <input type="text" name="possiCard" id="possiCard" value="" /> ex) 06</li>
            <li>할부개월 : <input type="text" name="fixedInt" id="fixedInt" value="" /> ex) 03</li>
            <li>최대할부개월 : <input type="text" name="maxInt" id="maxInt" value="" /> ex) 24</li>
            <li>
            무이자 사용여부 :
            <select class="require" name="noIntYN" onchange="javascript:noIntYNonChange();">
                <option value="N">사용안함</option>
                <option value="Y">사용</option>
            </select>
            </li>
            <!-- 결제수단코드 + 카드코드 + - + 무이자 개월 ex) CC01-02:03:05:09 -->
            <li>무이자옵션 : <input type="text" name="noIntOpt" id="noIntOpt" value="" /> ex) CC01-02:03:05</li>
            <li>
            카드사포인트사용여부 : 
            <select name ="pointUseYn">
                <option value="N">카드사 포인트 사용안함</option>
                <option value="Y">카드사 포인트 사용</option>
            </select>
            </li>
            <li>금지카드설정 : <input type="text" name="blockCard" id="blockCard" value=""/> ex) 01:04:11</li>
            <li>특정제한카드 BIN : <input type="text" name="blockBin" id="blockBin" value=""/></li>
            </ul>
                    
        
        
        
        
        ======================================================================
        <b>getTxnId 응답</b>
        resultcode<input id="resultCode" type="text" value=""/>
        resultmsg<input id="resultMsg" type="text" value=""/>
        txnId<input id="txnId" type="text" value=""/>
        prDt<input id="prDt" type="text" value=""/>
        
        
        <!-- DLP호출에 대한 응답 -->
        <b>DLP 응답</b>
        SPU : <input type="text" name="SPU" id="SPU" value=""/>
        SPU_SIGN_TOKEN : <input type="text" name="SPU_SIGN_TOKEN" id="SPU_SIGN_TOKEN" value=""/>
        MPAY_PUB : <input type="text" name="MPAY_PUB" id="MPAY_PUB" value=""/>
        NON_REP_TOKEN : <input type="text" name="NON_REP_TOKEN" id="NON_REP_TOKEN" value=""/>
</form>
<!-- TODO :  LayerPopup의 Target DIV 생성 -->

<!-- <div class="btns"> -->
<!--     <a href="javascript:orderTest();">결제 요청하기</a>     -->
<!-- </div> -->