<%--
	화면명 : 정기배송신청
	작성자 : dennis
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<script type="text/javascript">
var __isMobile = global.channel.isMobile;
$(document).ready(function(){	
	//배송주기 초기화
	initDeliveryPeriod();
	
	//초기배송지
	addressSelect("DEFAULT");
	
	//배송메시지
	if(__isMobile == "true"){
		getDeliveryMessage_mo("STD");
		getDeliveryMessage_mo("NEW");
	}else{
		getDeliveryMessage();
	}
})
//================================ 디자인 ui.js

function chgSelect(param) {
// 	var labelId = $(param).prev()[0].id;
	
// 	if ((labelId).match(/(_domain)$/)) {
// 		var id = labelId.replace('_domain', '');
		
// 		if(common.isEmpty($('option:selected', param).attr('value'))){
// 			document.getElementById(id + '_email2').readOnly = false;
// 		}else{
// 			document.getElementById(id + '_email2').readOnly = true;
// 		}
		
// 		$('#' + id + '_email2').val($('option:selected', param).attr('value'));
// 	}
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

var addrSet = function(){		
	
	var chkVal = "";
	if(__isMobile == "true"){
		chkVal = $("#ra1_2_1").val();
	}else{
		chkVal = $("input:radio[name=ra1_2_1]:checked").val();
	}
	
	if(chkVal == "STD"){			
		selIdx = 0;
	}else{
		selIdx = 1;
	}
	
	var form = $("#addressForm");
	
	var deliveryName1;
	var phone1;
	var phone2;
	var deliveryZipCd;
	var deliveryAddress1;
	var deliveryAddress2;
	var note;
	
	if(selIdx == 0){
		deliveryName1 = form.find("#stddeliveryName1").val();
		phone1 = form.find("#stdphone1").val();
		phone2 = form.find("#stdphone2").val();
		deliveryZipCd = form.find("#stdzipCd").val();
		deliveryAddress1 = form.find("#stdaddress1").val();
		deliveryAddress2 = form.find("#stdaddress2").val();
		if(__isMobile == "true"){
			note = form.find("#stdnote").val();
		}else{
			note = form.find("#note").val();
		}
		
	}else if(selIdx == 1){
		deliveryName1 = form.find("#deliveryName1").val();
		if(ccs.common.telcheck('newphone2','휴대폰번호',true)){			
			return false;
		}
		if(ccs.common.telcheck('newphone1','전화번호',false)){			
			return false;
		}
		phone1 = form.find("#newphone1").val();
		phone2 = form.find("#newphone2").val();
		deliveryZipCd = form.find("#zipCd").val();
		deliveryAddress1 = form.find("#address1").val();
		deliveryAddress2 = form.find("#address2").val();
		note = form.find("#note").val();
	}
	
	if(common.isEmpty(deliveryName1)){
		alert("수령인을 입력하세요.");
		return false;
	}
	
	
	if(common.isEmpty(deliveryZipCd)){
		alert("우편번호를 입력하세요.");
		return false;
	}
	if(common.isEmpty(deliveryAddress1) || common.isEmpty(deliveryAddress2)){
		alert("주소를 입력하세요.");
		return false;
	}
// 	console.log(form);
	
	$("#saveForm").find("#deliveryName1").val(deliveryName1);
	$("#saveForm").find("#deliveryPhone1").val(phone1);
	$("#saveForm").find("#deliveryPhone2").val(phone2);
	$("#saveForm").find("#deliveryZipCd").val(deliveryZipCd);
	$("#saveForm").find("#deliveryAddress1").val(deliveryAddress1);
	$("#saveForm").find("#deliveryAddress2").val(deliveryAddress2);
	$("#saveForm").find("#note").val(note);
	
	return true;
}

var productSet = function(){

	var mergeForm = $("#mergeForm");
	var tempDeliveryProductNos = $("input[name=tempDeliveryProductNo]");
	
	for(var i=0;i<tempDeliveryProductNos.length;i++){
		var deliveryProductNo = tempDeliveryProductNos[i].value;
		var form = $("#regularForm"+deliveryProductNo).find("input,select option:selected");
		
		var error = false;
		var msg = "";
		
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
			
			if(name == "deliveryCnt"){
				if(common.isEmpty(value) || value == "0"){
					msg = "배송횟수를 선택하세요.";
					error = true;
					return;
				}
			}
			
			if(name == "deliveryPeriodCd"){
				if(common.isEmpty(value) || value == ""){
					msg = "배송주기를 선택하세요.";
					error = true;
					return;
				}
			}
			
			if(name == "deliveryPeriodValue"){
				if(common.isEmpty(value) || value == ""){
					msg = "배송요일을 선택하세요.";
					error = true;
					return;
				}
			}
			
			mergeForm.append($('<input>',{
				'type' : 'hidden',
				'name' : "omsRegulardeliveryproducts["+i+"]."+name,
				'id' : id,
				'value' : value
			}))
		});
		
		if(error){
			alert(msg);
			return false;
		}
	}
	return true;
}

var reg = function(){		
	
	var billingKey = $("#saveForm").find("#billingKey").val();
	if(common.isEmpty(billingKey)){
		alert("등록된 카드가 없습니다.");
		
		return false;
	}
	var mergeForm = $("#mergeForm");
	mergeForm.html("");

	if(!addrSet()){
		$("#mergeForm").html("");	//mergeform 초기화
		return false;
	}
		
	if(!productSet()){
		$("#mergeForm").html("");	//mergeform 초기화
		return false;
	}
	
	var agreeCheck = $("input[name=agreeCheck]");
	for(var i=0;i<agreeCheck.length;i++){
		if(!$(agreeCheck[i]).prop("checked")){
			alert($(agreeCheck[i]).prop("value")+"에 동의해야합니다.");
			$(agreeCheck[i]).focus();
			return false;
		}
	}					
	
	
	common.mergeForms("mergeForm","saveForm");
	
// 	console.log("reg mergeForm : ");
// 	console.log(mergeForm);
	saveRegularDelivery();
}

var saveRegularDelivery = function(){
	
	var data = $("#mergeForm").serialize();
	
// 	console.log(data);
// 	return;
	$.ajax({
		url : oms.url.saveRegular,
		type : "POST",
		data : data
	}).done(function(response){
		if(response.RESULT == "SUCCESS"){
// 			alert("성공");
			$("#orderRegularCompleteForm").find("#regularDeliveryId").val(response.regularDeliveryId);
			$("#orderRegularCompleteForm").submit();
		}else{
			alert(response.MESSAGE);
		}				
	});
	
}
var callback_calcDate = function(form,response){
	
	var dateList = response.dateList;
	
	var html = '';
	for(var i=0;i<dateList.length;i++){
		var deliveryCnt = dateList[i].deliveryCnt;
		var deliveryDate = dateList[i].TARGET_DATE;
		html += '<li>- '+(i+1)+'회 : <span>'+deliveryDate+'</span></li>';
	}
	
	form.find("#schedule").html(html);
}

var calcDate = function(form){
	
// 	console.log("calcDate form :");
// 	console.log(form);
	
	var deliveryPeriodCd = form.find("#deliveryPeriodCd option:selected").val();		
	var deliveryPeriodValue = form.find("#deliveryPeriodValue option:selected").val();
	var deliveryCnt = form.find("#deliveryCnt option:selected").val();
	
	var txt = $(form).find("#deliveryPeriodCd option:selected").text()+" /  총"+deliveryCnt+"회";
	
	$(form).find("#regularDeliveryDateTxt").html(txt);
	$(form).find("#regularDeliveryPeriodTxt").html($(form).find("#deliveryPeriodValue option:selected").text());
	
	var data = {deliveryPeriodCd : deliveryPeriodCd, deliveryPeriodValue:Number(deliveryPeriodValue), deliveryCnt:deliveryCnt};
	
	$.ajax({
 		contentType : "application/json; charset=UTF-8",
		url : oms.url.calcRegularDate,
		type : "POST",
		data : JSON.stringify(data)
	}).done(function(response){
		if(response.RESULT == "SUCCESS"){			
			callback_calcDate(form,response);
		}else{
			alert(response.MESSAGE);
		}				
	});
}

var calcPeriod = function(form){
// 	var deliveryPeriodCd = form.find("#deliveryPeriodCd option:selected").val();
// 	var html = "";
// 	if(deliveryPeriodCd.indexOf("WEEK") > -1){
// 		html = '<option value="2" >월</option>\
// 				<option value="3" >화</option>\
// 				<option value="4" >수</option>\
// 				<option value="5" >목</option>\
// 				<option value="6" >금</option>\
// 			';
				
// 	}else if(deliveryPeriodCd.indexOf("MONTH") > -1){
// 		for(var i=1;i<31;i++){
// 			html += '<option value="'+i+'">'+i+'일</option>';
// 		}
// 	}
	
// 	form.find("#deliveryPeriodValue").html(html);
// 	form.find("#deliveryPeriodValue option:eq(0)").attr("selected","selected");
	
// 	fnSelectChange( $('.selectbox select') );
// 	fnSelectChange( $('.sel_box select') );
	
	calcDate(form);
}

var initDeliveryPeriod = function(){
	var tempDeliveryProductNos = $("input[name=tempDeliveryProductNo]");
// 	console.log("initDeliveryPeriod tempDeliveryProductNos.length : "+tempDeliveryProductNos.length);
	
	for(var i=0;i<tempDeliveryProductNos.length;i++){
		var deliveryProductNo = tempDeliveryProductNos[i].value;
		var form = $("#regularForm"+deliveryProductNo);
		
		calcDate(form);		
	}
	
}

var chgDeliveryAddr = function(obj){
	var type = $(obj).val();
	$("#stdDelivery").hide();
	$("#newDelivery").hide();
	
	if(type == 'STD'){
		$("#stdDelivery").show();
	}else{
		$("#newDelivery").show();
	}
		
// 	fnChkStyle();
	
}

var chgDeliveryAddr_mo = function(type){	
	
	$("#stdDelivery").removeClass("tab_conOn");
	$("#newDelivery").removeClass("tab_conOn");
	$("#std_li").removeClass("on");
	$("#new_li").removeClass("on");
		
	if(type == 'STD'){
// 		console.log($("#stdDelivery"));
// 		console.log($("#stdDelivery"));
		$("#stdDelivery").addClass("tab_conOn");
		$("#std_li").addClass("on");
	}else{
		$("#newDelivery").addClass("tab_conOn");
		$("#new_li").addClass("on");
	}
	
	$("#ra1_2_1").val(type);
	
}

//우편번호 팝업
var openAddressPopup = function(){
	ccs.layer.searchAddressLayer.open(null,function(data){		
		$("#addressForm").find("#zipCd").val(data.zipCd);
		$("#addressForm").find("#address1").val(data.address1);
		$("#addressForm").find("#address2").val(data.address2);
	});
}

var getDeliveryMessage = function(){
	
	var tag = "";
	
	var cd = $("#deliveryMessage").find("input[name=deliveryMessageValue]");
	
	if(common.isNotEmpty(cd)){
		tag = '<label></label><select id="selnote" onchange="javascript:chgDelMsg()">';
		cd.each(function(){
			var value = $(this).attr("value");
			var codeName = $(this).attr("codeName");
			tag += '<option value="'+value+'">'+codeName+"</option>";			
		});
		tag += '<option value="">직접입력</option>';
		tag += "</select>";
	}
	
	$("#noteTxt").html(tag);
	fnSelectChange( $('.sel_box select') );
}

var getDeliveryMessage_mo = function(type){
	
	var tag = "";
	
	var cd = $("#deliveryMessage").find("input[name=deliveryMessageValue]");
	
	if(common.isNotEmpty(cd)){
		tag = '<label></label><select id="selnote'+type+'" onchange="javascript:chgDelMsg(\''+type+'\')">';
		cd.each(function(){
			var value = $(this).attr("value");
			var codeName = $(this).attr("codeName");
			tag += '<option value="'+value+'">'+codeName+"</option>";			
		});
		tag += '<option value="">직접입력</option>';
		tag += "</select>";
	}
	if(type == "STD"){
		$("#stdnoteTxt").html(tag);
	}else{
		$("#noteTxt").html(tag);
	}
	fnSelectChange( $('.sel_box select') );
}

var getDeliveryMessage_mo = function(type){
	
	var tag = "";
	
	var cd = $("#deliveryMessage").find("input[name=deliveryMessageValue]");
	
	if(common.isNotEmpty(cd)){
		tag = '<label></label><select id="selnote'+type+'" onchange="javascript:chgDelMsg_mo(\''+type+'\')">';
		cd.each(function(){
			var value = $(this).attr("value");
			var codeName = $(this).attr("codeName");
			tag += '<option value="'+value+'">'+codeName+"</option>";			
		});
		tag += '<option value="">직접입력</option>';
		tag += "</select>";
	}
	if(type == "STD"){
		$("#stdnoteTxt").html(tag);
	}else{
		$("#noteTxt").html(tag);
	}
	fnSelectChange( $('.sel_box select') );
}

var chgDelMsg = function(){
	var note = $("#selnote option:selected").text();	
	var noteval = $("#selnote option:selected").val();
	var flag = true;
	if(common.isEmpty(noteval)){
		flag = false;
		note = "";
	}
	$("#addressForm").find("#note").val(note);
	if(flag){
		$("#addressForm").find("#noteDiv").hide();
	}else{
		$("#addressForm").find("#noteDiv").show();
	}
	$("#addressForm").find("#note").val(note);
}

var chgDelMsg_mo = function(type){

	var note = $("#selnote"+type+" option:selected").text();
	var noteval = $("#selnote"+type+" option:selected").val();
	var flag = true;
	if(common.isEmpty(noteval)){
		flag = false;
		note = "";
	}
	if(type == "STD"){
		$("#addressForm").find("#stdnote").val(note);
		if(flag){
			$("#addressForm").find("#stdnoteDiv").hide();
		}else{
			$("#addressForm").find("#stdnoteDiv").show();
		}
	}else{
		$("#addressForm").find("#note").val(note);
		if(flag){
			$("#addressForm").find("#noteDiv").hide();
		}else{
			$("#addressForm").find("#noteDiv").show();
		}
	}
}

</script>
<!-- 배송메시지 -->
<form id="deliveryMessage">
<tags:codeList code="DELIVERY_MESSAGE_CD" var="deliveryMessageList" tagYn="N"/>
<c:forEach items="${deliveryMessageList }" var="dcd">
<input type="hidden" name="deliveryMessageValue" value="${dcd.cd }" codeName="${dcd.name }"/>
</c:forEach>
</form>

<form action="/oms/order/regularComplete" name="orderRegularCompleteForm" id="orderRegularCompleteForm" method="post">
	<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }"/>
	<input type="hidden" name="regularDeliveryId" id="regularDeliveryId" value=""/>
</form>
<form name="mergeForm" id="mergeForm"></form>
<form name="saveForm" id="saveForm">
	<input type="hidden" name="cartProductNos" value="${regular.cartProductNos }"/>
	<input type="hidden" name="billingKey" id="billingKey" value="${memberInfo.billingKey }"/>
	<input type="hidden" name="regularPaymentBusinessCd" value="${memberInfo.regularPaymentBusinessCd }"/>
	<input type="hidden" name="regularPaymentBusinessNm" value="${memberInfo.regularPaymentBusinessNm}"/>
	
	<input type="hidden" name="name1" id="name1" value="${memberInfo.mmsMember.memberName }"/>
	<input type="hidden" name="countryNo" id="countryNo" value="82"/>
	<input type="hidden" name="phone1" id="phone1" value="${memberInfo.mmsMember.phone1 }"/>
	<input type="hidden" name="phone2" id="phone2" value="${memberInfo.mmsMember.phone2 }"/>		
	<input type="hidden" name="orderEmail" id="orderEmail" value="${memberInfo.mmsMember.email }"/>
	
	<input type="hidden" name="deliveryName1" id="deliveryName1" value=""/>
	<input type="hidden" name="deliveryCountryNo" id="deliveryCountryNo" value="82"/>
	<input type="hidden" name="deliveryPhone1" id="deliveryPhone1" value=""/>
	<input type="hidden" name="deliveryPhone2" id="deliveryPhone2" value=""/>
	<input type="hidden" name="deliveryZipCd" id="deliveryZipCd" value=""/>
	<input type="hidden" name="deliveryAddress1" id="deliveryAddress1" value=""/>
	<input type="hidden" name="deliveryAddress2" id="deliveryAddress2" value=""/>
	<input type="hidden" name="note" id="note" value=""/>
	
	<input type="hidden" id="addressNoIdx" value=""/>			
</form>

<!-- ### //배송지팝업 -->
<script type="text/javascript">
var addressOpen = function(type){
	
	ccs.layer.memberAddressLayer.open(function(data){
		
		if(type == "NEW"){
			$("#addressForm").find("#deliveryName1").val(data.deliveryName1);
			$("#addressForm").find("#address1").val(data.address1);
			$("#addressForm").find("#address2").val(data.address2);
			$("#addressForm").find("#zipCd").val(data.zipCd);
			
			$("#addressForm").find("#newphone2").val(data.phone2);
			ccs.common.telset("newphone2",data.phone2);
			
			$("#addressForm").find("#newphone1").val(data.phone1);
			ccs.common.telset("newphone1",data.phone1);
			
		}else{
			if(__isMobile == "true"){
				deliveryNameTxt = data.deliveryName1;
			}else{
				deliveryNameTxt = data.deliveryName1+' <a href="#" onclick="javascript:addressOpen()" class="btn_sStyle4 sGray2">배송지 목록</a>';
			}
			phoneTxt = data.phone2 + " / " + data.phone1;
			addressTxt = "("+data.zipCd+") " + data.address1 +" "+ data.address2;
			addressNo = data.addressNo; 
			
			$("#deliveryNameTxt").html(deliveryNameTxt);
			$("#phoneTxt").html(phoneTxt);
			$("#addressTxt").html(addressTxt);	
			
			$("#saveForm").find("#addressNoIdx").val(addressNo);
			
			$("#stddeliveryName1").val(data.deliveryName1);
			$("#stdphone1").val(data.phone1);
			$("#stdphone2").val(data.phone2);
			$("#stdaddress1").val(data.address1);
			$("#stdaddress2").val(data.address2);
			$("#stdzipCd").val(data.zipCd);
		}
				
	});
}

var addressSelect = function(idx){
	
	var stdDeliveryFlag = false;
	var defaultAddressNo = $("#defaultMemberAddress").find("#addressNo").val();
	if(common.isEmpty(defaultAddressNo)){
		stdDeliveryFlag = true;
	}
	
	if(stdDeliveryFlag){				
		
		if(__isMobile == "true"){
			$("#ra1_2_1").val("NEW");			
			$("#selDeliveryLi").hide();
			
			$("#stdDelivery").removeClass("tab_conOn");
			$("#std_li").removeClass("on");
			
			$("#newDelivery").addClass("tab_conOn");
			$("#new_li").addClass("on");	
		}else{
			$("input:radio[name=ra1_2_1]").val("NEW");			
			$("#selDeliveryLi").hide();
			$("#stdDelivery").hide();
			$("#newDelivery").show();
		}
							
	}else{
		
		var form;
		
		if(idx == "DEFAULT"){
			form = $("#defaultMemberAddress");
		}else{
			form = $("#memberAddress"+idx);
		}

		if(__isMobile == "true"){
			deliveryNameTxt = form.find("#deliveryName1").val();
		}else{
			deliveryNameTxt = form.find("#deliveryName1").val()+' <a href="#" onclick="javascript:addressOpen()" class="btn_sStyle4 sGray2">배송지 목록</a>';
		}
		phoneTxt = form.find("#phone2").val() + " / " + form.find("#phone1").val();
		addressTxt = "("+form.find("#zipCd").val()+") " + form.find("#address1").val() +" "+ form.find("#address2").val(); 
		
		$("#deliveryNameTxt").html(deliveryNameTxt);
		$("#phoneTxt").html(phoneTxt);
		$("#addressTxt").html(addressTxt);	
		
		$("#stddeliveryName1").val(form.find("#deliveryName1").val());
		$("#stdphone1").val(form.find("#phone1").val());
		$("#stdphone2").val(form.find("#phone2").val());
		$("#stdaddress1").val(form.find("#address1").val());
		$("#stdaddress2").val(form.find("#address2").val());
		$("#stdzipCd").val(form.find("#zipCd").val());
		
		$("#saveForm").find("#addressNoIdx").val(idx);		
	}
	
}
</script>
<c:forEach items="${memberAddressList }" var="ma" varStatus="status">
<c:choose>
<c:when test="${ma.addressNo == memberInfo.addressNo }">
	<form id="defaultMemberAddress">
	<input type="hidden" id="addressNo" value="${ma.addressNo }"/>
	<input type="hidden" id="name" value="${ma.name }"/>
	<input type="hidden" id="deliveryName1" value="${ma.deliveryName1 }"/>
	<input type="hidden" id="countryNo" value="${ma.countryNo }"/>
	<input type="hidden" id="phone1" value="${ma.phone1 }"/>
	<input type="hidden" id="phone2" value="${ma.phone2 }"/>
	<input type="hidden" id="zipCd" value="${ma.zipCd }"/>
	<input type="hidden" id="address1" value="${ma.address1 }"/>
	<input type="hidden" id="address2" value="${ma.address2 }"/>
	</form>
</c:when>
<c:otherwise>
	<form name="memberAddress" id="memberAddress${ma.addressNo }">
		<input type="hidden" id="defaultYn" value="<c:if test="${ma.addressNo == memberInfo.addressNo }">Y</c:if> "/>
		<input type="hidden" id="addressNo" value="${ma.addressNo }"/>
		<input type="hidden" id="name" value="${ma.name }"/>
		<input type="hidden" id="deliveryName1" value="${ma.deliveryName1 }"/>
		<input type="hidden" id="countryNo" value="${ma.countryNo }"/>
		<input type="hidden" id="phone1" value="${ma.phone1 }"/>
		<input type="hidden" id="phone2" value="${ma.phone2 }"/>
		<input type="hidden" id="zipCd" value="${ma.zipCd }"/>
		<input type="hidden" id="address1" value="${ma.address1 }"/>
		<input type="hidden" id="address2" value="${ma.address2 }"/>
	</form>
</c:otherwise>
</c:choose>
</c:forEach>

<script type="text/javascript">
var pgData = function(){
	var mergeForm = $("#mergeForm");
	var form = $("#LGD_PAYINFO").find("input");
	
	form.each(function(){
		var name = $(this).attr("name");
		var id = $(this).attr("id");
		var value = $(this).attr("value");
		mergeForm.append($('<input>',{
			'type' : 'hidden',
			'name' : "omsPaymentif."+name,
			'id' : id,
			'value' : value
		}))
	});
}

//LG 인증
var auth_LG = function(){		
	
	var form = $("#payparam");
	
	var data = form.serialize();
	
	$.ajax({ 				
		url : oms.url.pgParam,
		type : "POST",		
		data : data
	}).done(function(response){
		if(response.RESULT == "SUCCESS"){
			pay_callback(response.omsPaymentif);	
		}else{
			alert(response.MESSAGE);
		}
		
	});
}

var pay_callback = function(data){
	
	var form = $("#LGD_PAYINFO");
	form.html("");	//form 초기화
	
	common.mergeForms("LGD_PAYINFO","payresult");
	
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
		if(key == "LGD_VERSION"){
			if(__isMobile == "true"){
				value = "JSP_SmartXPay_CardBilling";
			}else{
				value = "JSP_Non-ActiveX_CardBilling";
			}
		}
// 		if(key == "LGD_RETURNURL" || key == "LGD_PAYWINDOWTYPE" || key == "LGD_WINDOW_TYPE" || key == "LGD_MID" || key == "LGD_VERSION"){
			
			if(common.isNotEmpty(value)){
				form.append($('<input>',{
					'type' : 'hidden',
					'name' : key,
					'id' : key,
					'value' : value
				}))
			}
// 		}
		
	});
	
	if(__isMobile == "true"){
		form.find("#LGD_PAYWINDOWTYPE").val("CardBillingAuth_smartphone");
	}else{
		form.find("#LGD_PAYWINDOWTYPE").val("CardBillingAuth");
	}
	
	form.find("#LGD_CHECKSSNYN").val("N");	//생년월일/사업자번호 일치여부확인.		
// 	console.log(form);
	pgData();
			
	launchCrossPlatform();	
}

var callback_saveBilling = function(response){
// 	console.log("saveBilling",response.cardData);
	var card = response.cardData;
	$("#saveForm").find("#billingKey").val(card.billingKey);
	$("#saveForm").find("#regularPaymentBusinessCd").val(card.regularPaymentBusinessCd);
	$("#saveForm").find("#regularPaymentBusinessNm").val(card.regularPaymentBusinessNm);
	$("#regularPaymentBusinessNmTxt").html(card.regularPaymentBusinessNm);
	$("#regularPaymentBusinessNmTxt2").html("");
	$("#regularPaymentBusinessNmTxt3").html("");
	$("#btnTxt_pay").html("변경");
	//결제정보세팅
}

var saveBilling = function(){
	var form = $("#payresult");
	
	var data = form.serialize();
	
	$.ajax({ 				
		url : oms.url.saveBilling,
		type : "POST",		
		data : data
	}).done(function(response){
		if(response.RESULT == "SUCCESS"){			
			callback_saveBilling(response);	
		}else{
			alert(response.MESSAGE);
		}
		
	});
}
////////////////////////////////////////////// PG /////////////////////////////////////////////////////
/*
* 수정불가.
*/
	var LGD_window_type = "";
	var CST_PLATFORM = '';
/*
* 수정불가
*/
function launchCrossPlatform(){	
// 	console.log(CST_PLATFORM);
// 	console.log(LGD_window_type);
	lgdwin = openXpay(document.getElementById('LGD_PAYINFO'), CST_PLATFORM, LGD_window_type, null, "", "");
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
	
	fDoc = lgdwin.contentWindow || lgdwin.contentDocument;	
	
	var resultForm = $("#payresult");
	
	if (fDoc.document.getElementById('LGD_RESPCODE').value == "0000") {
			
		resultForm.find("#LGD_RESPCODE").val(fDoc.document.getElementById('LGD_RESPCODE').value);
		resultForm.find("#LGD_RESPMSG").val(fDoc.document.getElementById('LGD_RESPMSG').value);
		resultForm.find("#LGD_BILLKEY").val(fDoc.document.getElementById('LGD_BILLKEY').value);
		resultForm.find("#LGD_PAYTYPE").val(fDoc.document.getElementById('LGD_PAYTYPE').value);
		resultForm.find("#LGD_PAYDATE").val(fDoc.document.getElementById('LGD_PAYDATE').value);
		resultForm.find("#LGD_FINANCECODE").val(fDoc.document.getElementById('LGD_FINANCECODE').value);
		resultForm.find("#LGD_FINANCENAME").val(fDoc.document.getElementById('LGD_FINANCENAME').value);

// 			document.getElementById("LGD_PAYINFO").target = "_self";
// 			document.getElementById("LGD_PAYINFO").action = "CardBillingAuth_Res.jsp";
// 			document.getElementById("LGD_PAYINFO").submit();

			saveBilling();
		closeIframe();
	} else {
		alert("LGD_RESPCODE (결과코드) : " + fDoc.document.getElementById('LGD_RESPCODE').value + "\n" + "LGD_RESPMSG (결과메시지): " + fDoc.document.getElementById('LGD_RESPMSG').value);
		closeIframe();
	}
}

</script>
<form method="post" name="payparam" id="payparam" style="display: none;">
	<input type="text" name="LGD_BUYERSSN" id="LGD_BUYERSSN" value=""/>			<!-- 생년월일 6자리 or 사업자번호 -->
	<input type="text" name="LGD_CHECKSSNYN" id="LGD_CHECKSSNYN" value=""/>	<!-- 생년월일/사업자번호 일치 여부 확인 -->
	<input type="text" name="LGD_PAYWINDOWTYPE" id="LGD_PAYWINDOWTYPE" value="CardBillingAuth"/><!-- 인증요청구분 (수정불가)  -->
	<input type="text" name="LGD_VERSION" id="LGD_VERSION" value="JSP_Non-ActiveX_CardBilling"/>	<!-- 사용타입 정보(수정 및 삭제 금지): 이 정보를 근거로 어떤 서비스를 사용하는지 판단할 수 있습니다.-->
	<input tyep="text" name="orderTypeCd" value="ORDER_TYPE_CD.REGULARDELIVERY"/>			 	
</form>
<form id="payresult" style="display: none;">
	<!-- result -->
	<input type="text" name="LGD_RESPCODE" id="LGD_RESPCODE" value=""/>
	<input type="text" name="LGD_RESPMSG" id="LGD_RESPMSG" value=""/>
	<input type="text" name="LGD_BILLKEY" id="LGD_BILLKEY" value=""/>	
	<input type="text" name="LGD_PAYTYPE" id="LGD_PAYTYPE" value=""/>    
    <input type="text" name="LGD_PAYDATE" id="LGD_PAYDATE" value=""/>
    <input type="text" name="LGD_FINANCECODE" id="LGD_FINANCECODE" value=""/>
    <input type="text" name="LGD_FINANCENAME" id="LGD_FINANCENAME" value=""/>    
</form>
<form method="post" name="LGD_PAYINFO" id="LGD_PAYINFO"></form>