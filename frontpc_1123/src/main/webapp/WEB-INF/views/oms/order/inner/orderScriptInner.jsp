<%--
	화면명 : 주문서 script
	작성자 : dennis
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!-- <script src="https://kmpay.lgcns.com:8443/js/dlp/lib/jquery/jquery-1.11.1.min.js" charset="urf-8"></script> -->
<script type="text/javascript">

//회원yn
var memberYn = "N"; 
var __isMobile = global.channel.isMobile;
$(document).ready(function(){
	
	var checkOrder = $("#checkOrder").val();
	if("true" == checkOrder){
	}else{
		alert("비정상 접근입니다.");
		ccs.link.common.main();		
	}
	
	memberYn = $("#saveOrderForm").find("#memberYn").val();
	
	addrChg(1);

	//주문자 전화번호 setting
	ccs.common.telset('orderPhone2',$("#orderForm #orderPhone2").val());
	
	//email setting
	ccs.common.emailset('orderEmail',$("#orderForm #orderEmail").val());	
	
	//초기배송지세팅.
	setInitDelivery();
	
	if(__isMobile == "true"){
		if(memberYn == 'N'){
			$("#orderSheet").removeClass("inner");
			$("#nonmemberSheet").addClass("inner");
			$("#orderSheet").hide();
			$("#nonmemberSheet").show();		
		}else{
			$("#orderSheet").addClass("inner");
			$("#nonmemberSheet").removeClass("inner");
			$("#orderSheet").show();
			$("#nonmemberSheet").hide();
		}
		
	}
	
	//해외구매대행check
	var overseaFlag = $("#overseaYn").val();
	if(overseaFlag == "true"){
		$("#overseas").show();						
	}
	
	//선물포장
	chgWrap();
	
	//다음 결제수단 default check
	$("#continuePaymentMethodChk").prop("checked",true);	

})
//================================ 디자인 ui.js
function cssChange(){
	fnRadioStyle();
	fnChkStyle();
	fnSelectChange( $('.select_style1 select') );
	fnSelectChange( $('.select_box1 select') );
	fnSelectChange2();
	
	$(".pc .btn_giftInfo").on("click", function(e){
		fnPopPosition( $(".sLayer_gift").not(".layer_style1") );

		e.preventDefault();
	});
	
	$(".pc .btn_gift_packing").on("click", function(e){
		fnPopPosition( $(".sLayer_gift_packing").not(".layer_style1") );

		e.preventDefault();
	});
	
	$(".mobile .orderBox .btn_giftInfo").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .sLayer_gift").not(".pop_wrap") );
		}
	});
	
	$(".mobile .btn_gift_packing").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .sLayer_gift_packing") );
		}
	});
}
/* 레이어 팝업 위치 설정 : 2016.08.09 */
function fnPopPosition(target_pop) {
	$(target_pop).show();
	$(target_pop).height( $(document).height() );

	if(__isMobile == "true"){
		var base_top = ($(window).height() - $(" > .box", target_pop).innerHeight()) / 2;
		$(" > .box", target_pop).css({"marginTop" : base_top + $(window).scrollTop() + "px"});
	}else{
		var base_top = ($(window).height() - $(".pop_inner", target_pop).innerHeight()) / 2;
		if(target_pop == "#couponDiv"){
			base_top = base_top - 200;
		}
		var target_po = base_top + $(window).scrollTop() + $(".pop_inner", target_pop).innerHeight();
		var target_poMin = 10;
		var target_poMax = base_top + $(window).scrollTop() - (target_po - $(document).height() + 10);
	
		if(target_po > $(document).height()){
			$(".pop_inner", target_pop).css({"marginTop" : target_poMax + "px"});
		}else if(base_top + $(window).scrollTop() < 0){
			$(".pop_inner", target_pop).css({"marginTop" : target_poMin + "px"});
		}else{
			$(".pop_inner", target_pop).css({"marginTop" : base_top + $(window).scrollTop() + "px"});
		}
	}
}
/* 작은레이어 팝업 위치 설정 : 2016.08.22 */
function fnLayerPosition(target_pop) {
	$(target_pop).show();
	$(target_pop).height( $(document).height() );

	var base_top = ($(window).height() - $(" > .box", target_pop).innerHeight()) / 2;
	$(" > .box", target_pop).css({"marginTop" : base_top + $(window).scrollTop() + "px"});
}
function fnSelectChange(selectTarget) {
	selectTarget.each(function(){
		var select_name = $(this).children('option:selected').text();		
		$(this).siblings('label').text(select_name);
	});
	selectTarget.change(function(){
		var select_name = $(this).children('option:selected').text();
		$(this).siblings('label').text(select_name);
	});
}
function fnSelectChange2() {
	$(".select_box1 select").each(function() {
		$(this).siblings('label').text( $("option:selected", this).text() );
	});

	$(".wrap .content").off("change").on("change", ".select_box1 select", function() {
		$(this).siblings('label').text( $("option:selected", this).text() );
	});
}
//radio
function fnRadioStyle(){
	$(".pc .radio_style1 input").each(function() {
		if($(this).is(':checked')){
			$(this).parent().addClass("selected");
		}else{
			$(this).parent().removeClass("selected");
		}
	});

	$(".pc .radio_style1 input").off("change").on({
		"change" : function() {
			var this_name = $(this).attr("name");
			$("input[name='" + this_name + "']").parent().removeClass("selected");
			$(this).parent().addClass("selected");
		}
	});
	
	$(".mobile .option_style1 input").each(function() {
		if($(this).is(':checked')){
			$(this).parent().addClass("selected");
		}else{
			$(this).parent().removeClass("selected");
		}
	});
	$(".mobile .option_style1 input").off("change").on({
		"change" : function() {
			if( $(this).attr("type") == "checkbox" ){
				if($(this).is(':checked')){
					$(this).parent().addClass("selected");
				}else{
					$(this).parent().removeClass("selected");
				}
			}else if( $(this).attr("type") == "radio" ){
				var this_name = $(this).attr("name");
				$("input[name='" + this_name + "']").parent().removeClass("selected");
				$(this).parent().addClass("selected");
			}else{
			}
		}
	});
}
//check box
function fnChkStyle(){
	
	$(".pc .chk_style1 input").each(function() {
		if($(this).is(':checked')){
			$(this).parent().addClass("selected");
		}else{
			$(this).parent().removeClass("selected");
		}
	});

	$(".pc .chk_style1 input").on({
		"change" : function() {
			if($(this).is(':checked')){
				$(this).parent().addClass("selected");
			}else{
				$(this).parent().removeClass("selected");
			}
		}
	});
}

//================================ 디자인 ui.js

//배송 개수
var totalCnt = 0;

//복수배송 개수
var getDeliveryCnt = function(){
	var cnt = $("input:radio[name=dsel]:checked").val();
	if(common.isEmpty(cnt)){
		cnt = 1;
	}	
	return cnt;
}
//주문상품일련번호object
var getOrderproductNos = function(){
	var orderproductNos = $("input[name=tempOrderProductNo]");
	return orderproductNos;
}
//배송지분리된 주문상품일련번호object
var getAddrOrderproductNos = function(deliveryAddressNo){
	var orderproductNos = $("#addressForm"+deliveryAddressNo).find("input[name=addrOrderProductNo]");
	return orderproductNos;
}
	
var checkOrderPresent = function(){
	
	var cnt = getDeliveryCnt();
	
	var selectPresent = $("#saveOrderForm").find("#selectPresent").val();
	
	var totalCnt = 0;
	
	//총 주문사은품 idx
	var orderpresentIndex = $("input[name=orderpresentIndex]");

	if(orderpresentIndex.length > 0){
		
		if(cnt == 1){	//배송지 1개일때 setting
			for(var i=0;i<orderpresentIndex.length;i++){
				var presentForm = $("#orderPresentForm"+orderpresentIndex[i].value);
				var productId = presentForm.find("#orderpresentProductId").val();
				var name = presentForm.find("#orderpresentName").val();
				var presentId = presentForm.find("#orderPresentId").val();
				var pre="";
				if(i == 0){
					pre = "||";
				}
				var value = pre + 'DELIVERY_INFO:'+presentId+':'+productId+':'+'0';
				
				selectPresent += value + "||";
				
				totalCnt++;
			}
			selectPresent = selectPresent.substr(0,selectPresent.length-2);
			$("#saveOrderForm").find("#selectPresent").val(selectPresent);
			
		}else{
			var spArr = selectPresent.split("||");
			for(var i=0;i<spArr.length;i++){
				var prArr = spArr[i].split(":");
				var presentTypeCd = prArr[0]
				if(presentTypeCd == "DELIVERY_INFO"){
					totalCnt++;
				}
			}
		}
		
		if(orderpresentIndex.length != totalCnt){
			alert("주문사은품이 모두 선택되지 않습니다.");
			return false;
		}
	}	
// 	console.log("주문사은품정보 : ",true);
	return true;
}

var productData = function(){
	var orderProductNos = getOrderproductNos();
	var mergeForm = $("#mergeForm");
	
	var overseaCheck = false;	//해외구매대행 check
	
	for(var i=0;i<orderProductNos.length;i++){
		var opn = orderProductNos[i].value;
		
		var form = $("#productForm"+opn).find("input,select option:selected");
		
		form.each(function(){
			var tag = $(this).get(0).tagName;
			var name;
			var id;
			var value;
			if(tag == "OPTION"){
				name = $(this).parent().attr("name");
				id = $(this).parent().attr("id");
				value = $(this).attr("value");	
			}else{
				name = $(this).attr("name");
				id = $(this).attr("id");
				value = $(this).attr("value");				
			}
						
			if(id == "overseasPurchaseYn"){
				if(value == 'Y'){
					overseaCheck = true;
				}
			}
			mergeForm.append($('<input>',{
				'type' : 'hidden',
				'name' : "omsOrderproducts["+i+"]."+name,
				'id' : id,
				'value' : value
			}))
		});
	}
		
	if(overseaCheck){
		var personalCustomsCode = $("#personalCustomsCode").val();
		if(common.isEmpty(personalCustomsCode)){
			alert("해외구매대행 상품이있습니다. 개인통관 부호를 입력해주세요.");
			$("#personalCustomsCode").focus();
			return false;
		}
		
		var agreeCheck = $("input[name=overseaagreeCheck]");
		for(var i=0;i<agreeCheck.length;i++){
			if(!$(agreeCheck[i]).prop("checked")){
				alert($(agreeCheck[i]).prop("value")+"에 동의해야합니다.");
				$(agreeCheck[i]).focus();
				return false;
			}
		}
		
		if(!oms.checkPersonalCustomsCode(personalCustomsCode)){
			return false;
		}
		
		mergeForm.append($('<input>',{
			'type' : 'hidden',
			'name' : "personalCustomsCode",				
			'value' : personalCustomsCode
		}))
		
	}
// 	console.log("상품정보 : ",true);
	return true;
}

//주문을 위한 form setting 및 validation
var formSetting = function(){
	
	var agreeCheck = $("input[name=agreeCheck]");
	for(var i=0;i<agreeCheck.length;i++){
		if(!$(agreeCheck[i]).prop("checked")){
			alert($(agreeCheck[i]).prop("value")+"에 동의해야합니다.");
			$(agreeCheck[i]).focus();
			return false;
		}
	}
	
	$("#mergeForm").html("");	//mergeform 초기화
	
	//주문자정보
	if(!orderData()){
		$("#mergeForm").html("");	//mergeform 초기화
		return false;
	}
	
	//주문상품정보
	if(!productData()){
		$("#mergeForm").html("");	//mergeform 초기화
		return false;
	}
	
	//address
	if(!addressData()){
		$("#mergeForm").html("");	//mergeform 초기화
		return false;
	}
	
	//delivery
	if(!deliveryData()){
		$("#mergeForm").html("");	//mergeform 초기화
		return false;
	}
	
	//주문사은품 check
	if(!checkOrderPresent()){
		$("#mergeForm").html("");	//mergeform 초기화
		return false;
	}
	
	//payment
	paymentData();
	
	common.mergeForms("mergeForm","saveOrderForm");
	
	return true;
}

//주문
var order = function(){
	
	//구분자: ||
	
	//상품사은품 "PRESENT_TYPE_CD.PRODUCT":orderProductNo:presentId:productIds
	//주문사은품 "PRESENT_TYPE_CD.ORDER":presentId:productIds
	
	/*
	*omsOrder - omsDeliveryaddresss - omsDeliverys
	*							   - omsOrderproducts
	*		  - omsPayments
	*/
	
	var mergeForm = $("#mergeForm");
	
// 	console.log("order mergeForm :");
// 	console.log(mergeForm);
		

// 	return;

	var totalOrderAmt = $("#saveOrderForm").find("#totalOrderAmt").val();
	
	var paymentMethodCd = $("#paymentForm0").find("#paymentMethodCd").val();
// 	alert(paymentMethodCd);
	if(__isMobile != "true" || Number(totalOrderAmt) == 0 || paymentMethodCd == "PAYMENT_METHOD_CD.VIRTUAL" || paymentMethodCd == "PAYMENT_METHOD_CD.KAKAO"){
		$.ajax({ 				
			url : oms.url.saveOrder,
			type : "POST",		
			data : mergeForm.serialize()
		}).done(function(response){
			if(response.RESULT == "SUCCESS"){
				//alert("저장되었습니다.");			
				$("#orderCompleteForm").find("#orderId").val(response.orderId);
				$("#checkOrder").val("");
				$("#orderCompleteForm").submit();					
			}else{
// 				console.log(response);
				alert(response.MESSAGE);
// 				closeIframe();
				oms.showOrderLoading(false);
			}			
		}).fail(function (jqXHR, textStatus, errorThrown) { alert("주문저장 실패.");oms.showOrderLoading(false);});
	}else{		
		$.ajax({ 				
			url : oms.url.saveSession,
			type : "POST",		
			data : mergeForm.serialize()
		}).done(function(response){
			if(response.RESULT == "SUCCESS"){
				//인증창 호출
				launchCrossPlatform();									
			}else{
				alert(response.MESSAGE);
// 				closeIframe();				
				oms.showOrderLoading(false);
			}		
		}).fail(function (jqXHR, textStatus, errorThrown) { alert("주문저장 실패.");oms.showOrderLoading(false);});
	}		
}

//주문자 정보 setting
var orderData = function(){
	var orderFormData = orderInfo();
	var name1 = orderFormData["name1"];
	
	var orderPwd = $("#nonMemberOrderPwd").val();
	var orderPwdConfirm = $("#nonMemberOrderPwdConfirm").val();
	
	
	if(common.isEmpty(name1)){
		alert("주문자명을 입력하세요.");
		$("#orderForm #name1").focus();
		return false;
	}
	if(ccs.common.telcheck('orderPhone2','주문자 휴대폰번호',true)){		
		$("#orderForm #orderPhone2_num1").focus();
		return false;
	}
	if(memberYn == 'N'){
		if(common.isEmpty(orderPwd)){
			alert("비회원 주문 비밀번호을 입력하세요.");
			$("#nonMemberOrderPwd").focus();
			return false;
		}
		if(common.isEmpty(orderPwdConfirm)){
			alert("비회원 주문 비밀번호 확인을 입력하세요.");
			$("#nonMemberOrderPwdConfirm").focus();
			return false;
		}
		
		if(orderPwd != orderPwdConfirm){
			alert("비회원 주문 비밀번호가 일치하지 않습니다.");
			return false;
		}
		
		//비밀번호 check
		if(!oms.checkPassword(orderPwd)){
			return false;
		}
		
		
// 		ccs.common.emailDomain('orderEmail');
		if(ccs.common.emailcheck('orderEmail','주문자 Email',true)){
			$("#orderForm #orderEmail_email1").focus();
			return false;
		}
	}

	
	$("#saveOrderForm").find("#name1").val(name1);
// 	$("#saveOrderForm").find("#phone1").val(phone1);
	$("#saveOrderForm").find("#phone2").val($("#orderForm #orderPhone2").val());
	$("#saveOrderForm").find("#email").val($("#orderForm #orderEmail").val());
	
	$("#saveOrderForm").find("#orderPwd").val(orderPwd);
// 	console.log("주문자정보 : ",true);


	var orderTypeCd = $("#saveOrderForm").find("#orderTypeCd").val();
	
	if("ORDER_TYPE_CD.GIFT" == orderTypeCd){
		if(__isMobile == "true"){
			$("#giftOrderName").html(name1);
		}else{
			var giftName = $("#giftForm").find("#giftName1").val();
			var giftImgTypeCd = $("#giftForm").find("input:radio[name=giftImgTypeCd]:checked").val();
			var giftMsg = $("#giftForm").find("#giftMsg").val();
			
			if(common.isEmpty(giftName)){
				alert("기프트콘 수령인명을 입력하세요.");
				$("#giftForm").find("#giftName1").focus();
				return false;
			}
			
			if(ccs.common.telcheck('giftPhoneNum','기프트콘 대상 휴대폰번호',true)){		
				$("#giftForm #giftPhoneNum").focus();
				return false;
			}
			var giftPhone = $("#giftForm").find("#giftPhoneNum").val();
			
			$("#saveOrderForm").find("#giftName").val(giftName);
			$("#saveOrderForm").find("#giftPhone").val(giftPhone);
			$("#saveOrderForm").find("#giftImgTypeCd").val(giftImgTypeCd);
			$("#saveOrderForm").find("#giftMsg").val(giftMsg);
		}
	}
	
	//OK CASHBAG
	var channelId = $("#channelId").val();
	if(channelId == "0007"){
		
		var agreeVal = $("input:radio[name=okcashbagAgree]:checked").val();
		if(agreeVal == "0"){
			alert("OK캐쉬백 포인트적립 동의해야합니다.");
			return false;
		}
		 
		var okcashbagNo1 = $("#okcashbagNo1").val();
		var okcashbagNo2 = $("#okcashbagNo2").val();
		var okcashbagNo3 = $("#okcashbagNo3").val();
		var okcashbagNo4 = $("#okcashbagNo4").val();
		var okcashbagNo = okcashbagNo1 
						+ okcashbagNo2
						+ okcashbagNo3
						+ okcashbagNo4;
		if(okcashbagNo1.length != 4 || okcashbagNo2.length != 4 || okcashbagNo3.length != 4 || okcashbagNo4.length != 4){
			alert("OK캐쉬백 카드번호를 정확히 입력하세요.");
			return false;
		}
		$("#saveOrderForm").find("#okcashbagNo").val(okcashbagNo);
	}
	
	return true;
}

//주문자 정보
var orderInfo = function(){
	var form = $("#orderForm");
	
	var returnObj = new Object();
	
	returnObj["name1"] = $(form).find("#name1").val();		
	returnObj["phone1"] = $(form).find("#orderPhone1").val();
	returnObj["phone2"] = $(form).find("#orderPhone2").val();
	returnObj["email"] = $(form).find("#orderEmail").val();	
		
	return returnObj;
}

//주소정보 validation
var addressVal = function(i){
	
	var orderTypeCd = $("#saveOrderForm").find("#orderTypeCd").val();
	
	if("ORDER_TYPE_CD.GIFT" == orderTypeCd){
		if(__isMobile == "true"){
			var giftName = $("#giftForm").find("#giftName1").val();
			var giftImgTypeCd = $("#giftForm").find("input:radio[name=giftImgTypeCd]:checked").val();
			var giftMsg = $("#giftForm").find("#giftMsg").val();
			
			if(common.isEmpty(giftName)){
				alert("기프트콘 수령인명을 입력하세요.");
				$("#giftForm").find("#giftName1").focus();
				return false;
			}			
			if(ccs.common.telcheck('giftPhoneNum','기프트콘 대상 휴대폰번호',true)){		
				$("#giftForm #giftPhoneNum").focus();
				return false;
			}
			var giftPhone = $("#giftForm").find("#giftPhoneNum").val();
			
			$("#saveOrderForm").find("#giftName").val(giftName);
			$("#saveOrderForm").find("#giftPhone").val(giftPhone);
			$("#saveOrderForm").find("#giftImgTypeCd").val(giftImgTypeCd);
			$("#saveOrderForm").find("#giftMsg").val(giftMsg);
		}		
	}else{
		var address = addressInfo(i);
		
		var name1 = address["deliveryName1"];
		var countryNo = address["countryNo"];
		var zipCd = address["zipCd"];
		var address1 = address["address1"];
		var address2 = address["address2"];
		var phone1 = address["phone1"];
		var phone2 = address["phone2"];
		var email = address["email"];
		
		if(common.isEmpty(name1)){
			alert("수령인을 입력하세요.");
			return false;
		}
		if(common.isEmpty(zipCd)){
			alert("우편번호를 입력하세요.");
			return false;
		}
		if(common.isEmpty(address1) || common.isEmpty(address2)){
			alert("주소를 입력하세요.");
			return false;
		}
		
		if(address['selIdx'] == 1){
			if(ccs.common.telcheck(i+'phone2','휴대폰번호',true)){			
				return false;
			}
	
	
// 			ccs.common.emailDomain(i+'email');
// 			if(ccs.common.emailcheck(i+'email','Email',false)){			
// 				return false;
// 			}
		}
		
	}
	return true;
}

//주소정보 setting
var addressData = function(){
	
	var orderTypeCd = $("#saveOrderForm").find("#orderTypeCd").val();
	
	var cnt = getDeliveryCnt();
	var checkArray = [];
	for(var i=0;i<cnt;i++){
		
		if(!addressVal(i)){
			return false;
		}
		
		var checkData = "";
		
		if("ORDER_TYPE_CD.GIFT" == orderTypeCd){
			var name1 = $("#giftForm").find("#giftName1").val();
			var phone2 = $("#giftForm").find("#giftPhoneNum").val();
			$("#addressForm"+i).find("#dName1").val(name1);
			$("#addressForm"+i).find("#dPhone2").val(phone2);
		}else{
		
			var address = addressInfo(i);
			
			var name1 = address["deliveryName1"];
			var countryNo = address["countryNo"];
			var zipCd = address["zipCd"];
			var address1 = address["address1"];
			var address2 = address["address2"];
			var phone1 = address["phone1"];
			var phone2 = address["phone2"];
			var email = address["email"];
			var note = address["note"];
			
			
			$("#addressForm"+i).find("#dName1").val(name1);
			$("#addressForm"+i).find("#dCountryNo").val(countryNo);
			$("#addressForm"+i).find("#dZipCd").val(zipCd);
			$("#addressForm"+i).find("#dAddress1").val(address1);
			$("#addressForm"+i).find("#dAddress2").val(address2);
			$("#addressForm"+i).find("#dPhone1").val(phone1);
			$("#addressForm"+i).find("#dPhone2").val(phone2);
			$("#addressForm"+i).find("#dEmail").val(email);
			$("#addressForm"+i).find("#dNote").val(note);
			
			checkData += zipCd;
			checkData += address1;
			checkData += address2;
			checkData += name1;
			
			checkArray.push(checkData);
			
	// 		console.log("addressData form : ");
	// 		console.log($("#addressForm"+i));

		}
		
		common.mergeForms("mergeForm","addressForm"+i);
	}
	
	for(var i=0;i<checkArray.length;i++){
		var addr = checkArray[i];
		for(var j=0;j<checkArray.length;j++){
			if(i != j && $.trim(addr) == $.trim(checkArray[j])){
				alert("동일한 배송지가 있습니다.");
				return false;
			}
		}
	}
// 	console.log("배송지정보 : ",true);
		
	return true;
}

//선택주소 정보
var addressInfo = function(deliveryAddressNo){
	var addressForm = $("#addressForm"+deliveryAddressNo);
		
	
	if(memberYn == 'N'){
		selIdx = 1;	
	}else{
		var chkVal = "";
		if(__isMobile == "true"){
			chkVal = addressForm.find("#ra1_2_1").val();
		}else{
			chkVal = addressForm.find("input:radio[name=ra1_2_1]:checked").val();
		}
		if(chkVal == "STD"){			
			selIdx = 0;
		}else{
			selIdx = 1;
		}
	}
	
	var form = addressForm;
	var returnObj = new Object();
	returnObj["selIdx"] = selIdx;
	if(selIdx == 0){
		returnObj["deliveryName1"] = $(form).find("#stddeliveryName1").val();
		returnObj["countryNo"] = $(form).find("#stdcountryNo").val();
		returnObj["zipCd"] = $(form).find("#stdzipCd").val();
		returnObj["address1"] = $(form).find("#stdaddress1").val();
		returnObj["address2"] = $(form).find("#stdaddress2").val();
		returnObj["phone1"] = $(form).find("#stdphone1").val();
		returnObj["phone2"] = $(form).find("#stdphone2").val();
		returnObj["email"] = $(form).find("#stdemail").val();
		returnObj["note"] = $(form).find("#stdnote").val();
	}else if(selIdx == 1){
		returnObj["deliveryName1"] = $(form).find("#deliveryName1").val();
		returnObj["countryNo"] = $(form).find("#countryNo").val();
		returnObj["zipCd"] = $(form).find("#zipCd").val();
		returnObj["address1"] = $(form).find("#address1").val();
		returnObj["address2"] = $(form).find("#address2").val();
		returnObj["phone1"] = $(form).find("#"+deliveryAddressNo+"phone1").val();
		returnObj["phone2"] = $(form).find("#"+deliveryAddressNo+"phone2").val();
		returnObj["email"] = $(form).find("#"+deliveryAddressNo+"email").val();
		returnObj["note"] = $(form).find("#note").val();
	}
	
// 	console.log("addressInfo selIdx : "+selIdx);
// 	console.log("addressInfo form : ");
// 	console.log(form);
	
	
		
	return returnObj;
	
}
//배송object
var deliveryData = function(){
	
	var cnt = getDeliveryCnt();
	
	var addrTotalOrderQty = 0;		
	
	for(var i=0;i<cnt;i++){
		
		var addrOrderProductNos = $("#addressForm"+i).find("#addrOrderProductNo");
		
		for(var j=0;j<addrOrderProductNos.length;j++){
			var opn = addrOrderProductNos[j].value;
			
			var orderQty = $("#addressForm"+i).find("#orderQty"+opn).val();
			
			addrTotalOrderQty += Number(orderQty);
			
// 			if(Number(orderQty) > 0){
				
				var deliveryPolicyNo = $("#addressForm"+i).find("#deliveryPolicyNo"+opn).val();
				
				var name = $("#productForm"+opn).find("#deliveryName").val();
				var deliveryServiceCd = $("#productForm"+opn).find("#deliveryServiceCd").val();				
				var deliveryFee = $("#addressForm"+i).find("#deliveryFee"+opn).val();
				var minDeliveryFreeAmt = $("#addressForm"+i).find("#minDeliveryFreeAmt"+opn).val();
				var orderDeliveryFee = $("#addressForm"+i).find("#orderDeliveryFee"+opn).val();
				var deliveryBurdenCd = $("#addressForm"+i).find("#deliveryBurdenCd"+opn).val();
				
				var form = "#deliveryForm_"+i+"_"+deliveryPolicyNo;

// 				console.log("deliveryData name : "+name);

				var orgDeliveryFee = $(form).find("#deliveryFee").val();
				

				$(form).find("#name").val(name);
				$(form).find("#deliveryServiceCd").val(deliveryServiceCd);
				
// 				console.log("deliveryData deliveryFee : ",deliveryFee);
// 				console.log("deliveryFee",$(form).find("#deliveryFee"));
				if(Number(deliveryFee) != 0){
					$(form).find("#deliveryFee").val(deliveryFee);
				}
				
				$(form).find("#minDeliveryFreeAmt").val(minDeliveryFreeAmt);
				$(form).find("#orderDeliveryFee").val(orderDeliveryFee);
				$(form).find("#deliveryBurdenCd").val(deliveryBurdenCd);
								
// 				common.mergeForms("mergeForm","deliveryForm_"+i+"_"+deliveryPolicyNo);
// 			}
		}
		
			
	}
// 	return;
	var deliveryForm = $("form[name=deliveryForm]");
	for(var i=0;i<deliveryForm.length;i++){
// 		console.log(deliveryForm[i].id);
		common.mergeForms("mergeForm",deliveryForm[i].id);
	}
	
	var totalOrderQty = 0;
	
	var orderProductNos = getOrderproductNos();
	
	for(var i=0;i<orderProductNos.length;i++){
		var opn = orderProductNos[i].value;
		
		var form = "#productForm"+opn;
		
		var orderQty = $(form).find("#orderQty").val();	
		totalOrderQty += Number(orderQty);
	}
// 	console.log("addrTotalOrderQty : "+addrTotalOrderQty);
// 	console.log("totalOrderQty : "+totalOrderQty);
	if(addrTotalOrderQty != totalOrderQty){
		alert("상품수량이 모두 선택되지 않았습니다.");
		return false;
	}
// 	console.log("배송정보 : ",true);
	return true;
	
}

//계산
var calc = function(){
	
	var orgTotalSalePrice=0;
	var totalOrderSalePrice=$("#saveOrderForm").find("#totalOrderSalePrice").val();
	var totalDcAmt=0;
	var totalDeliveryFee=0;
	var totalWrapFee=0;
	var totalPointsave=0;	//적립예상 포인트
	var productCouponAmt=0;
	var plusCouponAmt=0;
	var wrapCouponAmt=0;
	var orderCouponAmt=0;
	var deliveryCouponAmt=0;
	var point=0;
	var deposit=0;
	var gift=0;
	var totalOrderAmt=0;
	
	var orderProductNos = getOrderproductNos();
	
	for(var i=0;i<orderProductNos.length;i++){
		var opn = orderProductNos[i].value;
		
		var form = "#productForm"+opn;
		
		var orgSalePrice = $(form).find("#orgTotalSalePrice").val();
		var salePrice = $(form).find("#salePrice").val();
		var orderQty = $(form).find("#orderQty").val();		
		orgTotalSalePrice += Number(orgSalePrice) * Number(orderQty);
// 		totalSalePrice += Number(salePrice) * Number(orderQty);
	}

	totalDcAmt = Number(orgTotalSalePrice) - Number(totalOrderSalePrice);		
	
	var cnt = getDeliveryCnt();		
	
	for(var i=0;i<cnt;i++){
		var totalorderDeliveryFee = 0;	//배송지별 total 배송비
		var totalorderWrapFee = 0;	//배송지별 total 포장비
		var dmap = [];	//배송정책별 check위해 배열로 담음.
		for(var j=0;j<orderProductNos.length;j++){	//배송지별 상품
			
			//배송정책번호별 1개만 적용.
			var deliveryPolicyNo =  $("#addressForm"+i).find("#deliveryPolicyNo"+orderProductNos[j].value).val();
			var orderWrapFee = $("#deliveryForm_"+i+"_"+deliveryPolicyNo).find("#orderWrapFee").val();
			var orderDeliveryFee = $("#addressForm"+i).find("#orderDeliveryFee"+orderProductNos[j].value).val();
			
// 			console.log("calc =============== ");
// 			console.log("calc orderProductNo :"+orderProductNos[j].value);
// 			console.log("calc deliveryAddressNo : "+i);
// 			console.log("calc deliveryPolicyNo : "+deliveryPolicyNo);
// 			console.log("calc deliveryForm : ");
// 			console.log($("#deliveryForm_"+i+"_"+deliveryPolicyNo));
// 			console.log("calc orderDeliveryFee : "+orderDeliveryFee);
// 			console.log("calc orderWrapFee : "+orderWrapFee);
			
			var ex = false;			
			for(var k=0;k<dmap.length;k++){
				if(dmap[k] == deliveryPolicyNo){
					ex = true;
				}
			}
			if(!ex){
				dmap.push(deliveryPolicyNo);
				totalorderWrapFee += Number(orderWrapFee);
				totalWrapFee += Number(orderWrapFee);
			}
			totalorderDeliveryFee += Number(orderDeliveryFee);
			totalDeliveryFee += Number(orderDeliveryFee);
			
			var curTotalPoint = calcPointsave(i,orderProductNos[j].value);
			totalPointsave = Number(totalPointsave) + Number(curTotalPoint); 
			
		}
		$("#addressForm"+i).find("#totalorderDeliveryFee").val(totalorderDeliveryFee);
		$("#addressForm"+i).find("#totalorderWrapFee").val(totalorderWrapFee);					
		
	}
	
	point = $("#point").val();
	$("#paymentForm1").find("#paymentAmt").val(point);
	deposit = $("#deposit").val();
	$("#paymentForm2").find("#paymentAmt").val(deposit);	
// 	gift = $("#gift").val();	
// 	$("#paymentForm3").find("#paymentAmt").val(gift);
	
	//쿠폰 계산
	var productCouponAmt = calCouponSubAmt("product") + calCouponSubAmt("plus");
	var orderCouponAmt = calCouponSubAmt("order");
	var deliveryCouponAmt = calCouponSubAmt("delivery");
	var wrapCouponAmt = calCouponSubAmt("wrap");
	var applyTotalCouponDcAmtTxt = Number(productCouponAmt)
								  +Number(orderCouponAmt)
								  +Number(deliveryCouponAmt)
								  +Number(wrapCouponAmt);
	
	$("#applyTotalCouponAmt").val(common.priceFormat(applyTotalCouponDcAmtTxt));	
	
	totalOrderAmt = Number(totalOrderSalePrice)				 
				 + Number(totalDeliveryFee)
				 + Number(totalWrapFee)
				 - Number(productCouponAmt)
				 - Number(plusCouponAmt)
				 - Number(wrapCouponAmt)
				 - Number(orderCouponAmt)
				 - Number(deliveryCouponAmt)
				 - Number(point)
				 - Number(deposit)
				 - Number(gift);
	//총할인금액 표기 (쿠폰 + 보조결제)
	var dcAmtTxt = Number(productCouponAmt)
				  +Number(orderCouponAmt)
				  +Number(deliveryCouponAmt)
				  +Number(wrapCouponAmt)
				  + Number(point)
				  + Number(deposit)
				  + Number(gift);
	
	//상품총금액
	var orderAmt = Number(totalOrderSalePrice);				 
// 				 + Number(totalDeliveryFee)
// 				 + Number(totalWrapFee)
				 
	//쿠폰할인금액			  
	var dcAmt = Number(productCouponAmt)
				  +Number(orderCouponAmt)
				  +Number(deliveryCouponAmt)
				  +Number(wrapCouponAmt);
	//결제테이블금액 sum
	var paymentAmt = Number(totalOrderSalePrice)				 
					 + Number(totalDeliveryFee)
					 + Number(totalWrapFee)
					 - Number(productCouponAmt)
					 - Number(plusCouponAmt)
					 - Number(wrapCouponAmt)
					 - Number(orderCouponAmt)
					 - Number(deliveryCouponAmt);
	
	$("#saveOrderForm").find("#orderAmt").val(orderAmt);
	$("#saveOrderForm").find("#dcAmt").val(dcAmt);
	$("#saveOrderForm").find("#paymentAmt").val(paymentAmt);
	
	$("#saveOrderForm").find("#orgTotalSalePrice").val(orgTotalSalePrice);
// 	$("#saveOrderForm").find("#totalSalePrice").val(totalSalePrice);
	$("#saveOrderForm").find("#totalDcAmt").val(totalDcAmt);
	$("#saveOrderForm").find("#totalDeliveryFee").val(totalDeliveryFee);
	$("#saveOrderForm").find("#totalWrapFee").val(totalWrapFee);
	$("#saveOrderForm").find("#productCouponAmt").val(productCouponAmt);
	$("#saveOrderForm").find("#plusCouponAmt").val(plusCouponAmt);
	$("#saveOrderForm").find("#wrapCouponAmt").val(wrapCouponAmt);
	$("#saveOrderForm").find("#orderCouponAmt").val(orderCouponAmt);
	$("#saveOrderForm").find("#deliveryCouponAmt").val(deliveryCouponAmt);
	$("#saveOrderForm").find("#pointAmt").val(point);
	$("#saveOrderForm").find("#depositAmt").val(deposit);
	$("#saveOrderForm").find("#giftAmt").val(gift);
	$("#saveOrderForm").find("#totalOrderAmt").val(totalOrderAmt);
	$("#saveOrderForm").find("#totalPointsave").val(totalPointsave);
	
// 	$("#orgTotalSalePriceTxt").html(common.priceFormat(orgTotalSalePrice));	
	$("#orgTotalSalePriceTxt").html(common.priceFormat(totalOrderSalePrice));	//딜적용 가격.

	$("#totalDcAmtTxt").html("- " +common.priceFormat(dcAmtTxt,true));	//총 할인가(쿠폰할인 + 보조결제수단)
	
	$("#totalDeliveryFeeTxt").html("+ " +common.priceFormat(totalDeliveryFee,true));
	$("#totalWrapFeeTxt").html("+ " +common.priceFormat(totalWrapFee,true));
	
	
// 	$("#dealAmtTxt").html("- " +common.priceFormat(totalDcAmt,true));	//딜할인
	$("#productCouponAmtTxt").html("- " +common.priceFormat(Number(productCouponAmt) + Number(plusCouponAmt),true));
// 	$("#plusCouponAmtTxt").html(plusCouponAmt);
	$("#wrapCouponAmtTxt").html("- " +common.priceFormat(wrapCouponAmt,true));
	$("#orderCouponAmtTxt").html("-" +common.priceFormat(orderCouponAmt,true));
	$("#deliveryCouponAmtTxt").html("-" +common.priceFormat(deliveryCouponAmt,true));
	$("#pointAmtTxt").html("- " +common.priceFormat(point,true));
	$("#depositAmtTxt").html("- " +common.priceFormat(deposit,true));
	$("#giftAmtTxt").html("- " +common.priceFormat(gift,true));
	$("#totalOrderAmtTxt").html(common.priceFormat(totalOrderAmt));
	
	if(__isMobile == "true"){
		$("#totalOrderAmtTxt2").html(common.priceFormat(totalOrderAmt));
	}
	
	$("#totalPointsaveTxt").html("적립예정 매일포인트: "+common.priceFormat(totalPointsave)+"P");
	
	paymentControl();
}

//배송지별 상품수량변경
var chgDeliveryQty = function(paramOrderProductNo,maxQty,num,paramDeliveryPolicyNo){
	
// 	console.log(paramOrderProductNo);
// 	console.log(maxQty);
// 	console.log(num);
// 	console.log(paramDeliveryPolicyNo);
	
	//배송지 개수
	var cnt = getDeliveryCnt();
// 	console.log(cnt);

	//전체 선택 수량
	var inOrderQty = 0;
	
	for(var i=0;i<cnt;i++){				
		inOrderQty += Number($("#addressForm"+i).find("#selOrderQty"+paramOrderProductNo+" option:selected").val());			
	}
	
// 	console.log(inOrderQty);
	
	if(inOrderQty > maxQty){
		alert("더이상 선택하실수 없습니다.");
		var curQty = $("#addressForm"+num).find("#orderQty"+paramOrderProductNo).val();
		$("#addressForm"+num).find("#selOrderQty"+paramOrderProductNo).val("0");
		$("#addressForm"+num).find("#orderQty"+paramOrderProductNo).val("0");
		return;
	}else{
		$("#addressForm"+num).find("#orderQty"+paramOrderProductNo).val($("#addressForm"+num).find("#selOrderQty"+paramOrderProductNo).val());
	}
	
	var orderProductNos = getAddrOrderproductNos(num);
	
	var orderPrice = 0;
	var min = 0;
	var fee = 0;
	
	//배송비 check
	for(var i=0;i<orderProductNos.length;i++){
		var orderProductNo = orderProductNos[i].value;
		var deliveryPolicyNo = $("#addressForm"+num).find("#deliveryPolicyNo"+orderProductNo).val();
		var minDeliveryFreeAmt = $("#addressForm"+num).find("#minDeliveryFreeAmt"+orderProductNo).val();
		var deliveryFee = $("#addressForm"+num).find("#deliveryFee"+orderProductNo).val();
		var totalSalePrice = $("#addressForm"+num).find("#totalSalePrice"+orderProductNo).val();
		var orderQty = $("#addressForm"+num).find("#orderQty"+orderProductNo).val();
		
		var deliveryFeeFreeYn = $("#productForm"+orderProductNo).find("#deliveryFeeFreeYn").val();
		
		if(deliveryFeeFreeYn == 'Y'){
			deliveryFee = 0;
		}
		
		if(deliveryPolicyNo == paramDeliveryPolicyNo){	//배송정책번호 같은것
			orderPrice += Number(totalSalePrice) * Number(orderQty);
			min = minDeliveryFreeAmt;
			fee = deliveryFee;			
		}
	}
	
	//console.log(orderPrice);
	
	if(min <= orderPrice){
		fee	= 0;
	}
	
	//배송정책단위 배송비 setting
	for(var i=0;i<orderProductNos.length;i++){
		var orderProductNo = orderProductNos[i].value;
		var deliveryPolicyNo = $("#addressForm"+num).find("#deliveryPolicyNo"+orderProductNo).val();
		
		if(deliveryPolicyNo == paramDeliveryPolicyNo){	//배송정책번호 같은것 동일배송비처리
			var qty = $("#addressForm"+num).find("#orderQty"+orderProductNo).val();
			if(qty > 0){	//수량있는것
				$("#addressForm"+num).find("#orderDeliveryFee"+orderProductNo).val(fee);
			}else{
				$("#addressForm"+num).find("#orderDeliveryFee"+orderProductNo).val(0);
			}
		}
	}
	
	calc();
}

var memberAddressList = function(){
	var html = "";
	
	var forms = $("form[name=memberAddress]");
	for(var i=0;i<forms.length;i++){
		
		var form = $("#memberAddress"+i);
		var addressNo = form.find("#addressNo").val();		
		var defaultYn = form.find("#defaultYn").val();
		var dName = form.find("#name").val();
		var dDeliveryName = form.find("#deliveryName1").val();
		var dCountryNo = form.find("#countryNo").val();
		var dPhone1 = form.find("#phone1").val();
		var dPhone2 = form.find("#phone2").val();
		var dZipCd = form.find("#zipCd").val();
		var dAddress1 = form.find("#address1").val();
		var dAddress2 = form.find("#address2").val();
		var dAddress = dAddress1 + " " + dAddress2;
		var dEmail = form.find("#email").val();
		var dHtml = "";
		if(defaultYn == 'Y'){
			dHtml = '<span class="default">기본 배송지</span>';
		}
		
		html += '<li>\
					<div class="tr_box">\
					<div class="td_box col1">\
						<input type="radio" name="checkAddress" class="inp_radio" value="'+i+'" />\
					</div>\
					<div class="td_box col2">\
						'+dHtml+'\
						<span>'+dName+'</span>\
					</div>\
					<div class="td_box col3">\
						<span>'+dDeliveryName+'</span>\
					</div>\
					<div class="td_box col4 alignL">\
						<p><span>('+dZipCd+') '+dAddress+'</span></p>\
					</div>\
					<div class="td_box col5">\
						<div>'+dPhone2+'<br />/ '+dPhone2+'</div>\
					</div>\
					<div class="td_box col6 btns">\
						<a href="#none" class="btn_style7 gray1">수정</a>\
						<a href="#none" class="btn_style7 gray1">삭제</a>\
					</div>\
				</div>\
				</li>\
				';
				
	}
	
	return html;
}

var chgDeliveryAddr = function(obj){
	var type = $(obj).val();
	$(obj.form).find("#stdDelivery").hide();
	$(obj.form).find("#newDelivery").hide();
	if(type == 'STD'){
		$(obj.form).find("#stdDelivery").show();
	}else{
		$(obj.form).find("#newDelivery").show();
	}
		
	fnChkStyle();
	
}

var chgDeliveryAddr_mo = function(num,type){	
	var obj = $("#addressForm"+num);
	obj.find("#stdDelivery").removeClass("tab_conOn");
	obj.find("#newDelivery").removeClass("tab_conOn");
	obj.find("#std_li").removeClass("on");
	obj.find("#new_li").removeClass("on");
		
	if(type == 'STD'){
		obj.find("#stdDelivery").addClass("tab_conOn");
		obj.find("#std_li").addClass("on");
	}else{
		obj.find("#newDelivery").addClass("tab_conOn");
		obj.find("#new_li").addClass("on");
	}
	
	obj.find("#ra1_2_1").val(type);
		
	fnChkStyle();
	
}

//배송 template
var deliveryFormTempate	= function(totalCnt,num,parentNum,deliveryPolicyNo){
							return '\
									<form name="deliveryForm" id="deliveryForm_'+parentNum+'_'+deliveryPolicyNo+'">\
										<input type="hidden" name="omsDeliveryaddresss['+parentNum+'].omsDeliverys['+num+'].deliveryAddressNo" id="deliveryAddressNo" value="'+parentNum+'"/>\
										<input type="hidden" name="omsDeliveryaddresss['+parentNum+'].omsDeliverys['+num+'].deliveryPolicyNo" id="deliveryPolicyNo" value="'+deliveryPolicyNo+'"/>\
										<input type="hidden" name="omsDeliveryaddresss['+parentNum+'].omsDeliverys['+num+'].name" id="name" value=""/>\
										<input type="hidden" name="omsDeliveryaddresss['+parentNum+'].omsDeliverys['+num+'].deliveryServiceCd" id="deliveryServiceCd" value=""/>\
										<input type="hidden" name="omsDeliveryaddresss['+parentNum+'].omsDeliverys['+num+'].deliveryFee" id="deliveryFee" value="0"/>\
										<input type="hidden" name="omsDeliveryaddresss['+parentNum+'].omsDeliverys['+num+'].minDeliveryFreeAmt" id="minDeliveryFreeAmt" value="0"/>\
										<input type="hidden" name="omsDeliveryaddresss['+parentNum+'].omsDeliverys['+num+'].orderDeliveryFee" id="orderDeliveryFee" value="0"/>\
										<input type="hidden" name="omsDeliveryaddresss['+parentNum+'].omsDeliverys['+num+'].deliveryCouponId" id="deliveryCouponId" value=""/>\
										<input type="hidden" name="omsDeliveryaddresss['+parentNum+'].omsDeliverys['+num+'].deliveryCouponIssueNo" id="deliveryCouponIssueNo" value=""/>\
										<input type="hidden" name="omsDeliveryaddresss['+parentNum+'].omsDeliverys['+num+'].deliveryCouponDcAmt" id="deliveryCouponDcAmt" value="0"/>\
										<input type="hidden" name="omsDeliveryaddresss['+parentNum+'].omsDeliverys['+num+'].applyDeliveryFee" id="applyDeliveryFee" value="0"/>\
										<input type="hidden" name="omsDeliveryaddresss['+parentNum+'].omsDeliverys['+num+'].wrapTogetherYn" id="wrapTogetherYn" value="N"/>\
										<input type="hidden" name="omsDeliveryaddresss['+parentNum+'].omsDeliverys['+num+'].orderWrapFee" id="orderWrapFee" value="0"/>\
										<input type="hidden" name="omsDeliveryaddresss['+parentNum+'].omsDeliverys['+num+'].wrapCouponId" id="wrapCouponId" value=""/>\
										<input type="hidden" name="omsDeliveryaddresss['+parentNum+'].omsDeliverys['+num+'].wrapCouponIssueNo" id="wrapCouponIssueNo" value=""/>\
										<input type="hidden" name="omsDeliveryaddresss['+parentNum+'].omsDeliverys['+num+'].wrapCouponDcAmt" id="wrapCouponDcAmt" value="0"/>\
										<input type="hidden" name="omsDeliveryaddresss['+parentNum+'].omsDeliverys['+num+'].applyWrapFee" id="applyWrapFee" value="0"/>\
									</form>';
}

var copyOrderAddr = function(no){
	var form = $("#orderForm");
	
	$("#addressForm"+no).find("#deliveryName1").val($(form).find("#name1").val());
	$("#addressForm"+no).find("#"+no+"phone2").val($(form).find("#orderPhone2").val());
	$("#addressForm"+no).find("#"+no+"phone2_areaCode").val($(form).find("#orderPhone2_areaCode").val());
	$("#addressForm"+no).find("#"+no+"phone2_num1").val($(form).find("#orderPhone2_num1").val());
	$("#addressForm"+no).find("#"+no+"phone2_num2").val($(form).find("#orderPhone2_num2").val());
	$("#addressForm"+no).find("#address1").val($(form).find("#address1").val());
	$("#addressForm"+no).find("#address2").val($(form).find("#address2").val());
	$("#addressForm"+no).find("#zipCd").val($(form).find("#zipCd").val());
	$("#addressForm"+no).find("#"+no+"email").val($(form).find("#orderEmail").val());
	$("#addressForm"+no).find("#"+no+"email_email1").val($(form).find("#orderEmail_email1").val());
	$("#addressForm"+no).find("#"+no+"email_email2").val($(form).find("#orderEmail_email2").val());
	$("#addressForm"+no).find("#"+no+"email_domain").val($(form).find("#orderEmail_domain").val());
	
	cssChange();
}

//복수배송지
var addrChg = function(cnt){
	
	totalCnt = 0;
	
	var sumDelivery = $("input[name=sumDeliveryPolicyNo]");
	
	var html = "";
	var deliveryHtml = "";		
	
	for(var i=0;i<cnt;i++){		
		html += addressFormTemplate(i);
// 		console.log("addrChg sumDelivery.length : "+sumDelivery.length);
		for(var j=0;j<sumDelivery.length;j++){
			deliveryHtml += deliveryFormTempate(totalCnt,j,i,sumDelivery[j].value);
			totalCnt++;
		}		
	}
	$("#addressDiv").html(html);
	$("#deliveryDiv").html(deliveryHtml);		
	
	// 탭 - 로그인, 상품 상세, 장바구니 등..
	$(".tab_box").find(".tab li").on("click", function(e){
		e.preventDefault();
		var idx = $(this).index();

		$(this).addClass("on").siblings("li").removeClass("on");
		$(this).closest(".tab_box").find(".tabcont").eq(idx).addClass("tabcontShow").siblings(".tabcont").removeClass("tabcontShow");
	});
	
	calc();
	
	cssChange();
		
}

var clearSubPaymentCheck = function(type){
	if(__isMobile == "true"){
		
	}else{			
		$("#"+type+"Checkb").prop("checked",false);		
		fnChkStyle();
	}	
}

//할인 클릭시
var dcClick = function(type){
	$("#"+type).val("");
	calc();
}
//할인 onblur시
var dcBlur = function(type){
	
	ccs.common.fn_press_han($("#"+type)[0]);
	
	var curAmt = $("#"+type).val();
	
	if(common.isEmpty(curAmt)){
		$("#"+type).val("0");
		calc();
	} else {
		if(type == "point"){
			if(Number(curAmt) > 0 && !oms.checkTen(curAmt)){
				$("#point").val("0");
				alert("10포인트 단위로 사용가능합니다.");
				calc();
			}
		}	
	}
}

//할인적용
var dcChg = function(type){
	
	ccs.common.fn_press_han($("#"+type)[0]);
	
	if(!$.isNumeric($("#"+type).val())){
		$("#"+type).val("");
		return;
	}		
	
	var totalOrderAmt = $("#saveOrderForm").find("#totalOrderAmt").val();
	
	clearSubPaymentCheck(type);
	
	if(type == 'deposit'){
		var maxAmt = $("#balanceAmt").val();
		var curAmt = Number($("#deposit").val());
		var orgAmt = $("#paymentForm2").find("#paymentAmt").val();	//이전 예치금
		totalOrderAmt = Number(totalOrderAmt) + Number(orgAmt);
		
		if(!$.isNumeric(curAmt)){
			$("#deposit").val("0");
			alert("숫자아님");
			curAmt = 0;
		}
		if(Number(curAmt) > Number(maxAmt)){
			$("#deposit").val("0");
			alert("보유 예치금을 초과하였습니다.");
			curAmt = 0;
		}
		
		if(Number(curAmt) > Number(totalOrderAmt)){
			$("#deposit").val(totalOrderAmt);
		}else{
			$("#deposit").val(curAmt);
		}
		
	}else if(type == 'point'){
		var maxAmt = $("#totalPointAmt").val();
		var curAmt = Number($("#point").val());
		var orgAmt = $("#paymentForm1").find("#paymentAmt").val();	//이전 포인트
		totalOrderAmt = Number(totalOrderAmt) + Number(orgAmt);
		
		if(!$.isNumeric(curAmt)){
			curAmt = 0;
		}
		
		if(curAmt != 0){			
			if(Number(curAmt) > Number(maxAmt)){
				$("#point").val("0");
				alert("보유 포인트를 초과하였습니다.");
				curAmt = 0;
			}
		}
		if(Number(curAmt) > Number(totalOrderAmt)){
			$("#point").val(totalOrderAmt);
		}else{
			$("#point").val(curAmt);
		}		
	}	
	calc();
}


//포인트 모두사용
var pointAll = function(obj){
	var point = $("#point").val();
	var chkFlag = false;
	if(__isMobile == "true"){
		if(Number(point) == 0){
			chkFlag = true;
		}
	}else{
		chkFlag = $(obj).prop("checked");
	}
	var totalOrderAmt = $("#saveOrderForm").find("#totalOrderAmt").val();
	var orgAmt = $("#paymentForm1").find("#paymentAmt").val();//이전 포인트
	totalOrderAmt = Number(totalOrderAmt) + Number(orgAmt);
	
	var totalPoint = $("#totalPointAmt").val();
	
	if(chkFlag){
		if(Number(totalOrderAmt) < 10 || Number(totalPoint) < 10){
			alert("10 포인트이상 사용가능합니다.");
			$("#point").val("0");
			clearSubPaymentCheck("point");
			return;
		}
		if(Number(totalPoint) > Number(totalOrderAmt) ){
			$("#point").val(totalOrderAmt);
		}else{
			$("#point").val(totalPoint);
		}
	}else{
		$("#point").val("0");
	}
	calc();
}


//예치금 모두사용
var depositAll = function(obj){	
	var deposit = $("#deposit").val();
	var chkFlag = false;
	if(__isMobile == "true"){
		if(Number(deposit) == 0){
			chkFlag = true;
		}
	}else{
		chkFlag = $(obj).prop("checked");
	}
	var totalOrderAmt = $("#saveOrderForm").find("#totalOrderAmt").val();
	var orgAmt = $("#paymentForm2").find("#paymentAmt").val();	//이전 예치금
	totalOrderAmt = Number(totalOrderAmt) + Number(orgAmt);
	
	var totalDeposit = $("#balanceAmt").val();
	
	if(chkFlag){
		if(Number(totalDeposit) > Number(totalOrderAmt) ){
			$("#deposit").val(totalOrderAmt);
		}else{
			$("#deposit").val(totalDeposit);
		}
	}else{
		$("#deposit").val("0");
	}
	calc();
}

//주문사은품 배송 선택
var chkOrderpresent = function(obj,presentId,productId){
	var newSelectPresent = "";
	var selectPresent = $("#saveOrderForm").find("#selectPresent").val();		
	
	var deliveryAddressNo = $(obj).prop("value");
	var chk = $(obj).prop("checked");
	
	var spArr = selectPresent.split("||");
	
	var ex = false;
	
	for(var i=0;i<spArr.length;i++){
		var prArr = spArr[i].split(":");
		var presentTypeCd = prArr[0]
		if(presentTypeCd == "DELIVERY_INFO"){
			if(prArr[1] == presentId && prArr[2] == productId){
				//배송지번호가 선택되어있고, 현재배송지와 다르고, check 되었을때.
				if(common.isNotEmpty(prArr[3]) && prArr[3] != deliveryAddressNo && chk){
					alert("다른 배송지에서 선택하였습니다.");
					$(obj).prop("checked",false);
					return;
				}else{
					ex = true;
					if(chk){
						var value = 'DELIVERY_INFO:'+presentId+':'+productId+':'+deliveryAddressNo;
						newSelectPresent += value + "||";		
					}
				}
			}
		}else{
			newSelectPresent += spArr[i] + "||";
		}
	}
	if(chk && !ex){
		var value = 'DELIVERY_INFO:'+presentId+':'+productId+':'+deliveryAddressNo;
		newSelectPresent += value + "||";
	}
	newSelectPresent = newSelectPresent.substr(0,newSelectPresent.length-2);
	$("#saveOrderForm").find("#selectPresent").val(newSelectPresent);
}

//선물포장
var chgWrap = function(){
	
	var cnt = getDeliveryCnt();
	
	for(var i=0;i<cnt;i++){
		
		var wrapTogetherYn = $("input:radio[name=packing"+i+"]:checked").val();
		
		var ads = getAddrOrderproductNos(i);
		
		var totalCntObj = {};
		var togetherCnt = 0;	
		
		var tempWrapCnt = 0;
		var tempCalcWrapCnt = 0;
		var totalSelCnt = 0;
		
		for(var j=0;j<ads.length;j++){
			var opn = ads[j].value;
			var wrapYn = $("#productForm"+opn).find("#wrapYn").val();
// 			console.log("wrapYn : "+wrapYn);
			
			if(wrapYn == 'Y'){
				var checkBox = $("#checkWrap_"+i+"_"+opn).prop("checked");
				var deliveryPolicyNo = $("#addressForm"+i).find("#deliveryPolicyNo"+opn).val();				
				
				var orderQty = $("#addressForm"+i).find("#orderQty"+opn).val();
				var wrapVolume = $("#productForm"+opn).find("#wrapVolume").val();
				
				var wrapCnt = Number(wrapVolume) * Number(orderQty);
				
				//합포장아닐때 0.5는 올림.
				var calcWrapCnt = Math.ceil(wrapCnt);
																
				if(wrapTogetherYn == "Y"){
					
				}else{
					wrapCnt = calcWrapCnt;
				}
				
				var totalCnt = totalCntObj[deliveryPolicyNo];
				if(common.isEmpty(totalCnt)){
					totalCntObj[deliveryPolicyNo] = 0;
				}								
						
				if(checkBox){					
					
					totalSelCnt++;
					
					tempWrapCnt = Number(tempWrapCnt) + (Number(wrapVolume) * Number(orderQty));
					tempCalcWrapCnt = Number(tempCalcWrapCnt) + Number(calcWrapCnt);
					
					$("#addressForm"+i).find("#wrapYn"+opn).val("Y");	//포장 check
					totalCntObj[deliveryPolicyNo] = Number(totalCntObj[deliveryPolicyNo]) + Number(wrapCnt);
				}else{
					$("#addressForm"+i).find("#wrapYn"+opn).val("N");	//포장 안함.
				}
				
// 				console.log("wrapConfirm checkBox : "+checkBox);
// 				console.log("wrapConfirm wrapTogetherYn : " + wrapTogetherYn);
// 				console.log("wrapConfirm wrapVolume : " +wrapVolume);
// 				console.log("wrapConfirm wrapCnt : "+wrapCnt);
// 				console.log("wrapConfirm calcWrapCnt : "+calcWrapCnt);
// 				console.log("wrapConfirm deliveryPolicyNo : "+deliveryPolicyNo);
// 				console.log("==============");
			}
		}
		
// 		console.log(totalCntObj);
		
		for(var deliveryPolicyNo in totalCntObj){
			var wrapCnt = totalCntObj[deliveryPolicyNo];
			var orderWrapFee = Math.ceil(wrapCnt) * 1000;
// 			console.log("wrapConfirm tot wrapCnt : "+ wrapCnt);
// 			console.log("wrapConfirm orderWrapFee : "+ orderWrapFee);
					
			$("#deliveryForm_"+i+"_"+deliveryPolicyNo).find("#wrapTogetherYn").val(wrapTogetherYn);
			$("#deliveryForm_"+i+"_"+deliveryPolicyNo).find("#orderWrapFee").val(orderWrapFee);
			$("#deliveryForm_"+i+"_"+deliveryPolicyNo).find("#applyWrapFee").val(orderWrapFee);
			
			//적용 포장쿠폰 초기화
			$("#deliveryForm_"+i+"_"+deliveryPolicyNo).find("#wrapCouponId").val("");
			$("#deliveryForm_"+i+"_"+deliveryPolicyNo).find("#wrapCouponIssueNo").val("");
			$("#deliveryForm_"+i+"_"+deliveryPolicyNo).find("#wrapCouponDcAmt").val("0");			
// 			$("#deliveryForm_"+i+"_"+deliveryPolicyNo).find("#applyWrapFee").val();
			
			couponInit("wrap");
			
// 			couponSet(true,"wrap",deliveryPolicyNo);
		}		
				
		
// 		console.log("tempWrapCnt",tempWrapCnt);
// 		console.log("tempCalcWrapCnt",tempCalcWrapCnt);
// 		console.log("totalSelCnt",totalSelCnt);
		
		if(tempWrapCnt != tempCalcWrapCnt && totalSelCnt > 1){
			$("#addressForm"+i).find("#giftDiv").show();
		}else{
			$("#addressForm"+i).find("#giftDiv").hide();
			 $("input:radio[name=packing"+i+"]").each(function(){
				 if($(this).val() == "N"){
					 $(this).prop("checked",true); 
				 }
			 });
		}
		
	}
	//포인트 예치금 초기화
	subPaymentInit();
	
	calc();	
}

var subPaymentInit = function(){
	$("#point").val(0);
	$("#deposit").val(0);
}


//현금영수증
var receiptUseChg = function(obj){
	$("#receiptUseInfoSel").hide();
	$("#receiptUseInfo").hide();
	
	var receiptUse = $(obj).val();
	if(receiptUse == "0"){
		$("#receiptUseInfoSel").hide();
		$("#receiptUseInfo").hide();
	} else if(receiptUse == "1"){
		maxlengthNum("0");
		$("#receiptUseInfoSel").show();
		$("#receiptUseInfo").show();
	} else if(receiptUse == "2"){						
		maxlengthNum("3");	
		$("#receiptUseInfo").show();
	}
}

//현금영수증
var receiptUseChgSel = function(obj){
	
	var receiptUse = $(obj).val();
	
	maxlengthNum(receiptUse);	
}

var maxlengthNum = function(num){
	
	$("#cartnum1").hide();
	$("#cartnum2").hide();
	$("#cartnum3").hide();
	$("#cartnum4").hide();
	
	$("#pg_LGD_CASHCARDNUM1").val("");
	$("#pg_LGD_CASHCARDNUM2").val("");
	$("#pg_LGD_CASHCARDNUM3").val("");
	$("#pg_LGD_CASHCARDNUM4").val("");
	
	if(num == "0"){ //휴대폰
		$("#cartnum1").show();
		$("#cartnum2").show();
		$("#cartnum3").show();
		
		$("#pg_LGD_CASHCARDNUM1").attr("maxLength",3);
		$("#pg_LGD_CASHCARDNUM2").attr("maxLength",4);
		$("#pg_LGD_CASHCARDNUM3").attr("maxLength",4);
	}else if(num == "1"){ //신용카드번호
		$("#cartnum1").show();
		$("#cartnum2").show();
		$("#cartnum3").show();
		$("#cartnum4").show();
		
		$("#pg_LGD_CASHCARDNUM1").attr("maxLength",4);
		$("#pg_LGD_CASHCARDNUM2").attr("maxLength",4);
		$("#pg_LGD_CASHCARDNUM3").attr("maxLength",4);
		$("#pg_LGD_CASHCARDNUM4").attr("maxLength",4);
	}else if(num == "2"){ //현금영수증카드
		$("#cartnum1").show();
		$("#cartnum2").show();
		$("#cartnum3").show();
		$("#cartnum4").show();
		
		$("#pg_LGD_CASHCARDNUM1").attr("maxLength",4);
		$("#pg_LGD_CASHCARDNUM2").attr("maxLength",4);
		$("#pg_LGD_CASHCARDNUM3").attr("maxLength",4);
		$("#pg_LGD_CASHCARDNUM4").attr("maxLength",6);
		
	}else if(num == "3"){	//사업자등록번호
		$("#cartnum1").show();
		$("#cartnum2").show();
		$("#cartnum3").show();
		$("#cartnum4").hide();
		
		$("#pg_LGD_CASHCARDNUM1").attr("maxLength",3);
		$("#pg_LGD_CASHCARDNUM2").attr("maxLength",2);
		$("#pg_LGD_CASHCARDNUM3").attr("maxLength",5);
		$("#pg_LGD_CASHCARDNUM4").attr("maxLength",0);
	}
}

//우편번호 팝업
var openAddressPopup = function(no){
	ccs.layer.searchAddressLayer.open(null,function(data){
		$("#addressForm"+no).find("#zipCd").val(data.zipCd);
		$("#addressForm"+no).find("#address1").val(data.address1);
		$("#addressForm"+no).find("#address2").val(data.address2);
	});
}

//gift 메시지 변경
var giftMsgChg = function(idx){
	var val = $("#giftForm").find("#giftMsgSelect option:selected").val();
	var txt = $("#giftForm").find("#giftMsgSelect option:selected").text();
	if(common.isEmpty(val)){
		$("#giftForm").find("#giftMsg").prop("disabled",true);
		$("#giftForm").find("#giftMsg").val("");
	}else{
		
		if(val != "NONE"){
			$("#giftForm").find("#giftMsg").prop("disabled",false);
			$("#giftForm").find("#giftMsg").val(txt);
			
		}else{
			$("#giftForm").find("#giftMsg").prop("disabled",false);
			$("#giftForm").find("#giftMsg").val("");		
		}
	}
	
	var giftImgTypeCd = $("#giftForm").find("input:radio[name=giftImgTypeCd]:checked").val();							
	var no;
	
	if("GIFT_IMG_TYPE_CD.IMG1" == giftImgTypeCd){
		no = 1;
	}else if("GIFT_IMG_TYPE_CD.IMG2" == giftImgTypeCd){
		no = 2;
	}else if("GIFT_IMG_TYPE_CD.IMG3" == giftImgTypeCd){
		no = 3;
	}else if("GIFT_IMG_TYPE_CD.IMG4" == giftImgTypeCd){
		no = 4;
	}
	
	$("#giftForm").find("#p_message").removeClass();
	$("#giftForm").find("#p_message").addClass("p_message type"+no);
}

//적립예정 포인트(상품당)
var calcPointsave = function(no,opn){
	var curPoint = 0;
	var form = "#productForm"+opn;
	var pointSaveRate = $(form).find("#pointSaveRate").val();
	var orderQty = $("#addressForm"+no).find("#orderQty"+opn).val();
	var totalSalePrice = $("#addressForm"+no).find("#totalSalePrice"+opn).val();
	//TODO 1개적용쿠폰 금액제외.
	var productCouponDcAmt = $("#addressForm"+no).find("#productCouponDcAmt"+opn).val();
	var plusCouponDcAmt = $("#addressForm"+no).find("#plusCouponDcAmt"+opn).val();
	
	totalSalePrice = Number(totalSalePrice) - Number(productCouponDcAmt) -Number(plusCouponDcAmt);
	pointSave = Math.round(Number(totalSalePrice) * Number(pointSaveRate) / 100);	//상품적립예상포인트
	
	curPoint = pointSave;
	
	//포인트프로모션
	var pointSaveId = $(form).find("#pointSaveId").val();
	var pointSaveTypeCd = $(form).find("#pointSaveTypeCd").val();
	var pointValue = $(form).find("#pointValue").val();
	
	var productPoint = $(form).find("#productPoint").val();
	var addPoint = 0;
	var minPoint = 0;
	
	if(common.isNotEmpty(pointSaveId)){
		if("POINT_SAVE_TYPE_CD.MULTIPLY" == pointSaveTypeCd){
			minPoint = Number(pointSave) * Number(pointValue);
		}else if("POINT_SAVE_TYPE_CD.ADD" == pointSaveTypeCd){
			minPoint = Number(pointSave) + Number(pointValue);
		}
		addPoint = Number(minPoint) - Number(curPoint);
	}
	
// 	console.log("==============");
// 	console.log("no : "+no);
// 	console.log("opn : "+opn);
// 	console.log("orderQty : "+orderQty);
// 	console.log("curPoint : "+curPoint);
// 	console.log("minPoint : "+minPoint);
// 	console.log("addPoint : "+addPoint);
// 	console.log("==============");
	return ((Number(curPoint) + Number(addPoint)) * Number(orderQty));
}

var setInitDelivery = function(){
	var stdDeliveryFlag = false;
	var defaultAddressNo = $("#defaultMemberAddress").find("#addressNo").val();
	if(common.isEmpty(defaultAddressNo)){
		stdDeliveryFlag = true;
	}
	
	if(__isMobile == "true"){
		if(memberYn == 'N' || (memberYn == 'Y' && stdDeliveryFlag)){
			
			var cnt = getDeliveryCnt();
			
			for(var i=0;i<cnt;i++){
				var form = $("#addressForm"+i);
				
				form.find("#ra1_2_1").val("NEW");			
				form.find("#selDeliveryLi").hide();
				
				form.find("#stdDelivery").removeClass("tab_conOn");
				form.find("#std_li").removeClass("on");
				
				form.find("#newDelivery").addClass("tab_conOn");
				form.find("#new_li").addClass("on");
			
			}
						
		}
	}else{
		if(memberYn == 'Y' && stdDeliveryFlag){
			
			var cnt = getDeliveryCnt();
			
			for(var i=0;i<cnt;i++){
				var form = $("#addressForm"+i);
				
				form.find("input:radio[name=ra1_2_1]").val("NEW");			
				form.find("#selDeliveryLi").hide();
				form.find("#stdDelivery").hide();
				form.find("#newDelivery").show();
			}
						
		}
	}
	cssChange();
}

var getPhoneHead = function(type){
	
	var tag = "";
	
	var cd;
	if(type == "1"){
		cd = $("#phone1Head").find("input[name=phoneHeadValue]");
	}else if(type == "2"){
		cd = $("#phone2Head").find("input[name=phoneHeadValue]");
	}else if(type == "ALL"){
		cd = $("input[name=phoneHeadValue]");
	}
	
	if(common.isNotEmpty(cd)){
		tag = "<select>";
		cd.each(function(){
			var value = $(this).attr("value");
			var codeName = $(this).attr("codeName");
			tag += '<option value="'+value+'">'+codeName+"</option>";			
		});
		tag += "</select>";
	}
	
	return tag;
	
}

var getDeliveryMessage = function(deliveryAddressNo,type){
	
	var tag = "";
	
	var cd = $("#deliveryMessage").find("input[name=deliveryMessageValue]");
	
	if(common.isNotEmpty(cd)){
		tag = '<select id="selnote'+type+'" onchange="javascript:chgDelMsg(\''+deliveryAddressNo+'\',\''+type+'\')">';
		cd.each(function(){
			var value = $(this).attr("value");
			var codeName = $(this).attr("codeName");
			tag += '<option value="'+value+'">'+codeName+"</option>";			
		});
		tag += '<option value="">직접입력</option>';
		tag += "</select>";
	}
	
	return tag;
}


var chgDelMsg = function(deliveryAddressNo,type){
	
	var addressForm = $("#addressForm"+deliveryAddressNo);
	var note = $(addressForm).find("#selnote"+type+" option:selected").text();
	var noteval = $(addressForm).find("#selnote"+type+" option:selected").val();
	var flag = true;
	if(common.isEmpty(noteval)){
		flag = false;
		note = "";
	}
// 	console.log(note);
	if(type == "NEW"){
		$(addressForm).find("#note").val(note);
		if(flag){
			$(addressForm).find("#noteDiv").hide();
		}else{
			$(addressForm).find("#noteDiv").show();
		}
	}else if(type == "STD"){
		$(addressForm).find("#stdnote").val(note);
		if(flag){
			$(addressForm).find("#stdnoteDiv").hide();			
		}else{
			$(addressForm).find("#stdnoteDiv").show();
		}
	}
}

var getEmailDomain = function(){
	
	var tag = "";
	
	var cd = $("#emailDomain").find("input[name=emailDomainValue]");
	
	if(common.isNotEmpty(cd)){
		tag = "<select onchange='javascript:chgSelect(this)'>";
		cd.each(function(){
			var value = $(this).attr("value");
			var codeName = $(this).attr("codeName");
			tag += '<option value="'+value+'">'+codeName+"</option>";			
		});
		tag += "</select>";
	}
	
	return tag;
	
}

function chgSelect(param) {
	var labelId = $(param).prev()[0].id;
	
	if ((labelId).match(/(_domain)$/)) {
		var id = labelId.replace('_domain', '');
		
		if(common.isEmpty($('option:selected', param).attr('value'))){
			document.getElementById(id + '_email2').readOnly = false;
		}else{
			document.getElementById(id + '_email2').readOnly = true;
		}
		
		$('#' + id + '_email2').val($('option:selected', param).attr('value'));
	}
}


var addressOpen = function(targetNo, type){
		
	ccs.layer.memberAddressLayer.open(function(data){
		
		if(__isMobile == "true"){
			deliveryNameTxt = data.deliveryName1;
			phoneTxt = data.phone2 + " / " + data.phone1;
			addressTxt = "("+data.zipCd+") " + data.address1 +" "+ data.address2;
			addressNo = data.addressNo; 
			email = data.email;
			
			if(type == "NEW"){
				$("#addressForm"+targetNo).find("#deliveryName1").val(data.deliveryName1);
				$("#addressForm"+targetNo).find("#address1").val(data.address1);
				$("#addressForm"+targetNo).find("#address2").val(data.address2);
				$("#addressForm"+targetNo).find("#zipCd").val(data.zipCd);
				$("#addressForm"+targetNo).find("#"+targetNo+"phone2").val(data.phone2);
				$("#addressForm"+targetNo).find("#"+targetNo+"phone1").val(data.phone1);
				$("#addressForm"+targetNo).find("#"+targetNo+"email").val(email);
				
				ccs.common.telset(targetNo+"phone2",data.phone2);				
// 				ccs.common.telset(targetNo+"phone1",data.phone1);
				ccs.common.emailset(targetNo+"email",email);
				
			}else{
				$("#addressForm"+targetNo).find("#deliveryNameTxt").html(deliveryNameTxt);
				$("#addressForm"+targetNo).find("#phoneTxt").html(phoneTxt);
				$("#addressForm"+targetNo).find("#addressTxt").html(addressTxt);
				$("#addressForm"+targetNo).find("#addressNoIdx").val(addressNo);
				
				$("#addressForm"+targetNo).find("#stddeliveryName1").val(data.deliveryName1);
				$("#addressForm"+targetNo).find("#stdaddress1").val(data.address1);
				$("#addressForm"+targetNo).find("#stdaddress2").val(data.address2);
				$("#addressForm"+targetNo).find("#stdzipCd").val(data.zipCd);
			}
		}else{
			
			deliveryNameTxt = data.deliveryName1+'<a href="#none" class="btn_sStyle4 sGray2" onclick="javascript:addressOpen(\''+targetNo+'\')"><strong>배송지 목록</strong></a>';
			phoneTxt = data.phone2 + " / " + data.phone1;
			addressTxt = "("+data.zipCd+") " + data.address1 +" "+ data.address2;
			addressNo = data.addressNo; 
			email = data.email;
			
			if(type == "NEW"){
					
				$("#addressForm"+targetNo).find("#deliveryName1").val(data.deliveryName1);
				$("#addressForm"+targetNo).find("#"+targetNo+"phone2").val(data.phone2);
				$("#addressForm"+targetNo).find("#"+targetNo+"phone1").val(data.phone1);
				$("#addressForm"+targetNo).find("#address1").val(data.address1);
				$("#addressForm"+targetNo).find("#address2").val(data.address2);
				$("#addressForm"+targetNo).find("#zipCd").val(data.zipCd);
				$("#addressForm"+targetNo).find("#"+targetNo+"email").val(email);
				
				ccs.common.telset(targetNo+"phone2",data.phone2);
				ccs.common.telset(targetNo+"phone1",data.phone1);
				ccs.common.emailset(targetNo+"email",email);
				
			}else{
				$("#addressForm"+targetNo).find("#deliveryNameTxt").html(deliveryNameTxt);
				$("#addressForm"+targetNo).find("#phoneTxt").html(phoneTxt);
				$("#addressForm"+targetNo).find("#addressTxt").html(addressTxt);
				$("#addressForm"+targetNo).find("#addressNoIdx").val(addressNo);	
				
				$("#addressForm"+targetNo).find("#stddeliveryName1").val(data.deliveryName1);
				$("#addressForm"+targetNo).find("#stdaddress1").val(data.address1);
				$("#addressForm"+targetNo).find("#stdaddress2").val(data.address2);
				$("#addressForm"+targetNo).find("#stdzipCd").val(data.zipCd);
			}
		}
	});
}

//앱에서 연락처 열기
var openContact = function(){
	window.location.href = "app://contact";
}
//앱에서 연락처 받기
var OnAppContactSelected = function(phone){
	$("#giftForm").find("#giftPhoneNum").val(phone);
	ccs.common.telset("giftPhoneNum",phone);
}

//비회원 정보 입력후 주문페이지
var goOrder = function(){
	if(!orderData()){		
		return;
	}
	var chk = $("input[name=agreeCheckNonMember]").prop("checked");
	var value = $("input[name=agreeCheckNonMember]").val();
	if(!chk){
		alert(value+"에 동의해야합니다.");
		return;
	}
	
	
	
	var cnt = getDeliveryCnt();
	var orderName = $("#orderForm").find("#name1").val();
	var phone2 = $("#orderForm").find("#orderPhone2").val();
	var orderNameTxt = orderName +"<span>("+phone2+")</span>";
			
	for(var i=0;i<cnt;i++){
		$("#orderName"+i).html(orderNameTxt);
	}
	
	$("#nonmemberSheet").removeClass("inner");
	$("#nonmemberSheet").hide();
	$("#orderSheet").addClass("inner");
	$("#orderSheet").show();
}
</script>