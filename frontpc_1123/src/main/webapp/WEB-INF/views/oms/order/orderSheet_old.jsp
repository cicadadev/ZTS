<%--
	화면명 : 주문서
	작성자 : dennis
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script type="text/javascript">

var test = function(){
	if(memberYn == 'N'){
		var orderForm = $("#orderForm");
		
		$(orderForm).find("#name1").val("dennis");		
		$(orderForm).find("#phone1").val("1213131");
		$(orderForm).find("#phone2").val("11111111");
		$(orderForm).find("#email").val("dennis.ryu@intune.co.kr");
	}
		
	var cnt = getDeliveryCnt();
	for(var i=0;i<cnt;i++){
		var addressForm = $("#addressForm"+i);
		
		var lis = addressForm.find("#addressTab li");			
		
		var selIdx = -1;
		
		if(memberYn == 'N'){
			selIdx = 2;	
		}else{
			for(var i=0;i<lis.length;i++){		
				if($(lis[i]).hasClass("on")){
					selIdx = i;
				}
			}
		}
		
		var form;
		
		if(selIdx == 0){
			form = $("#defaultMemberAddress");
		}else if(selIdx == 1){
			var checkVal = addressForm.find("input:radio[name=checkAddress]:checked").val();
			form = $("#memberAddress"+checkVal);
		}else if(selIdx == 2){
			form = addressForm;
		}
		
		$(form).find("#deliveryName1").val("dennis");
		$(form).find("#countryNo").val("82");
		$(form).find("#zipCd").val("11111");
		$(form).find("#address1").val("서울시 구로구");
		$(form).find("#address2").val("집");
		$(form).find("#phone1").val("11111111");
		$(form).find("#phone2").val("11111111");
		$(form).find("#email").val("dennis.ryu@intune.co.kr");
	}
	
	var agreeCheck = $("input[name=agreeCheck]");
	for(var i=0;i<agreeCheck.length;i++){
		$(agreeCheck[i]).prop("checked",true);			
	}
	
	var paymentObj = $("input[name=paymentMethodCd]").eq(4);
	paymentObj.prop("checked",true);
	paymentChg(paymentObj);
	
	
	
	$("#pg_LGD_CARDTYPE").val("51");
	$("#pg_LGD_BANKCODE").val("20");
	
	
	customRadiobox("inp_radio");
	customCheckbox("inp_chk");
	fnSelectChange( $('.selectbox select') );
	fnSelectChange( $('.sel_box select') );
}

//회원yn
var memberYn = "N"; 
	
$(document).ready(function(){
	
	memberYn = $("#saveOrderForm").find("#memberYn").val();
	
	addrChg(1);
	
	//쿠폰개수
	sumCouponCnt();	
	
	//최적쿠폰 setting
	optimalCouponSet();
	
	
	//TEST DATA SET
	test();
})
//================================ 디자인 ui.js
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
//radio
function customRadiobox(radioName){
	var radioBox = $('input.inp_radio');
	$(radioBox).each(function(){
		$(this).unwrap();
		$(this).wrap( "<span class='custom-radio'></span>" );
		if($(this).is(':checked')){
			$(this).parent().addClass("selected");
		}
	});
	$(radioBox).change(function(){
		var this_name = $(this).attr("name");
		$("input[name='" + this_name + "']").parent().removeClass("selected");
		$(this).parent().addClass("selected");
	});
}

//checkbox
function customCheckbox(checkboxName){
	var checkBox = $('input.inp_chk');
	$(checkBox).each(function(){
		$(this).unwrap();
		$(this).wrap( "<span class='custom-checkbox'></span>" );
		if($(this).is(':checked')){
			$(this).parent().addClass("selected");
		}
	});
	$(checkBox).change(function(){
		if( $(this).prop("checked") ){
			$(this).parent().addClass("selected");
		}else{
			$(this).parent().removeClass("selected");
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
		}else{
			mergeForm.append($('<input>',{
				'type' : 'hidden',
				'name' : "personalCustomsCode",				
				'value' : personalCustomsCode
			}))
		}
	}
	
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
var order = function(orderId){
	
	//구분자: ||
	
	//상품사은품 "PRESENT_TYPE_CD.PRODUCT":orderProductNo:presentId:productIds
	//주문사은품 "PRESENT_TYPE_CD.ORDER":presentId:productIds
	
	/*
	*omsOrder - omsDeliveryaddresss - omsDeliverys
	*							   - omsOrderproducts
	*		  - omsPayments
	*/
	
	var mergeForm = $("#mergeForm");
	
	mergeForm.append($('<input>',{
		'type' : 'hidden',
		'name' : "orderId",
		'id' : "orderId",
		'value' : orderId
	}))
	
// 	console.log("order mergeForm :");
// 	console.log(mergeForm);
		

// 	return;

	$.ajax({ 				
		url : "/api/oms/order/save",
		type : "POST",		
		data : mergeForm.serialize()
	}).done(function(response){
		if(response.RESULT == "SUCCESS"){
			//alert("저장되었습니다.");			
			$("#orderCompleteForm").find("#orderId").val(orderId);
			$("#orderCompleteForm").submit();					
		}else{
			alert(response.MESSAGE);
		}
		
	});
}

//주문자 정보 setting
var orderData = function(){
	var orderFormData = orderInfo();
	var name1 = orderFormData["name1"];
	var phone1 = orderFormData["phone1"];
	var phone2 = orderFormData["phone2"];
	var email = orderFormData["email"];
	
	var orderPwd = $("#nonMemberOrderPwd").val();	
	
	if(memberYn == 'N'){
		if(common.isEmpty(orderPwd)){
			alert("비회원 주문 비밀번호을 입력하세요.");
			$("#nonMemberOrderPwd").focus();
			return false;
		}
	}
	
	if(common.isEmpty(name1)){
		alert("주문자명을 입력하세요.");
		$("#orderForm #name1").focus();
		return false;
	}
	if(common.isEmpty(phone2)){
		alert("주문자 휴대폰번호를 입력하세요.");
		$("#orderForm #phone2").focus();
		return false;
	}
	if(common.isEmpty(phone1)){
		alert("주문자 전화번호를 입력하세요.");
		$("#orderForm #phone1").focus();
		return false;
	}
	if(common.isEmpty(email)){
		alert("주문자 Email을 입력하세요.");
		$("#orderForm #email").focus();
		return false;
	}
	
	$("#saveOrderForm").find("#name1").val(name1);
	$("#saveOrderForm").find("#phone1").val(phone1);
	$("#saveOrderForm").find("#phone2").val(phone2);
	$("#saveOrderForm").find("#email").val(email);
	
	$("#saveOrderForm").find("#orderPwd").val(orderPwd);
	
	return true;
}

//주문자 정보
var orderInfo = function(){
	var form = $("#orderForm");
	
	var returnObj = new Object();
	
	returnObj["name1"] = $(form).find("#name1").val();		
	returnObj["phone1"] = $(form).find("#phone1").val();
	returnObj["phone2"] = $(form).find("#phone2").val();
	returnObj["email"] = $(form).find("#email").val();	
		
	return returnObj;
}

//주소정보 validation
var addressVal = function(i){
	
	var orderTypeCd = $("#saveOrderForm").find("#orderTypeCd").val();
	
	if("ORDER_TYPE_CD.GIFT" == orderTypeCd){
		var giftPhone = $("#giftPhoneNum").val();
		var giftName = $("#giftName1").val();
		if(common.isEmpty(giftPhone)){
			alert("기프트콘 대상 전화번호를 입력하세요.");
			return false;
		}
		if(common.isEmpty(giftName)){
			alert("기프트콘 수령인명을 입력하세요.");
			return false;
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
		if(common.isEmpty(phone1)){
			alert("전화번호를 입력하세요.");
			return false;
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
			var name1 = $("#giftName1").val();
			var phone1 = $("#giftPhoneNum").val();
			$("#addressForm"+i).find("#dName1").val(name1);
			$("#addressForm"+i).find("#dPhone1").val(phone1);
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
			
			
			$("#addressForm"+i).find("#dName1").val(name1);
			$("#addressForm"+i).find("#dCountryNo").val(countryNo);
			$("#addressForm"+i).find("#dZipCd").val(zipCd);
			$("#addressForm"+i).find("#dAddress1").val(address1);
			$("#addressForm"+i).find("#dAddress2").val(address2);
			$("#addressForm"+i).find("#dPhone1").val(phone1);
			$("#addressForm"+i).find("#dPhone2").val(phone2);
			$("#addressForm"+i).find("#dEmail").val(email);
			
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
		
	return true;
}

//선택주소 정보
var addressInfo = function(deliveryAddressNo){
	var addressForm = $("#addressForm"+deliveryAddressNo);
		
	var lis = addressForm.find("#addressTab li");
		
	
// 	console.log("addressInfo lis : ");
// 	console.log(lis);
	
	var selIdx = -1;
	
	if(memberYn == 'N'){
		selIdx = 2;	
	}else{
		for(var i=0;i<lis.length;i++){		
			if($(lis[i]).hasClass("on")){
				selIdx = i;
			}
		}
	}
	
	var form;
	
	if(selIdx == 0){
		form = $("#defaultMemberAddress");
	}else if(selIdx == 1){
		var checkVal = addressForm.find("input:radio[name=checkAddress]:checked").val();
		form = $("#memberAddress"+checkVal);
	}else if(selIdx == 2){
		form = addressForm;
	}
	
// 	console.log("addressInfo selIdx : "+selIdx);
// 	console.log("addressInfo form : ");
// 	console.log(form);
	
	var returnObj = new Object();
	
	returnObj["deliveryName1"] = $(form).find("#deliveryName1").val();
	returnObj["countryNo"] = $(form).find("#countryNo").val();
	returnObj["zipCd"] = $(form).find("#zipCd").val();
	returnObj["address1"] = $(form).find("#address1").val();
	returnObj["address2"] = $(form).find("#address2").val();
	returnObj["phone1"] = $(form).find("#phone1").val();
	returnObj["phone2"] = $(form).find("#phone2").val();
	returnObj["email"] = $(form).find("#email").val();
		
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
				var deliveryFeeTypeCd = $("#productForm"+opn).find("#deliveryFeeTypeCd").val();
				var deliveryFee = $("#addressForm"+i).find("#deliveryFee"+opn).val();
				var minDeliveryFreeAmt = $("#addressForm"+i).find("#minDeliveryFreeAmt"+opn).val();
				var orderDeliveryFee = $("#addressForm"+i).find("#orderDeliveryFee"+opn).val();
				var deliveryBurdenCd = $("#addressForm"+i).find("#deliveryBurdenCd"+opn).val();
				
				var form = "#deliveryForm_"+i+"_"+deliveryPolicyNo;

// 				console.log("deliveryData name : "+name);

				$(form).find("#name").val(name);
				$(form).find("#deliveryServiceCd").val(deliveryServiceCd);
				$(form).find("#deliveryFeeTypeCd").val(deliveryFeeTypeCd);
				$(form).find("#deliveryFee").val(deliveryFee);
				$(form).find("#minDeliveryFreeAmt").val(minDeliveryFreeAmt);
				$(form).find("#orderDeliveryFee").val(orderDeliveryFee);
				$(form).find("#deliveryBurdenCd").val(deliveryBurdenCd);
								
// 				common.mergeForms("mergeForm","deliveryForm_"+i+"_"+deliveryPolicyNo);
// 			}
		}
		
			
	}
	
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
	return true;
	
}

var paymentData = function(){
	
	var totalOrderAmt = $("#saveOrderForm").find("#totalOrderAmt").val();
	$("#paymentForm0").find("#paymentAmt").val(totalOrderAmt);	//결제금액
	
	common.mergeForms("mergeForm","paymentForm0","paymentForm1","paymentForm2","paymentForm3");
}

//계산
var calc = function(){
	
	var orgTotalSalePrice=0;
	var totalOrderSalePrice=$("#saveOrderForm").find("#totalOrderSalePrice").val();
	var totalDcAmt=0;
	var totalDeliveryFee=0;
	var totalWrapFee=0;
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
			
//			console.log("calc =============== ");
//			console.log("calc orderProductNo :"+orderProductNos[j].value);
//			console.log("calc deliveryAddressNo : "+i);
//			console.log("calc deliveryPolicyNo : "+deliveryPolicyNo);
// 			console.log("calc deliveryForm : ");
// 			console.log($("#deliveryForm_"+i+"_"+deliveryPolicyNo));
//			console.log("calc orderDeliveryFee : "+orderDeliveryFee);
			
			var ex = false;			
			for(var k=0;k<dmap.length;k++){
				if(dmap[k] == deliveryPolicyNo){
					ex = true;
				}
			}
			if(!ex){
				dmap.push(deliveryPolicyNo);
			}
			totalorderDeliveryFee += Number(orderDeliveryFee);
			totalDeliveryFee += Number(orderDeliveryFee);
			
			totalorderWrapFee += Number(orderWrapFee);
			totalWrapFee += Number(orderWrapFee);
			
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
	
	var orderAmt = Number(totalOrderSalePrice)				 
				 + Number(totalDeliveryFee)
				 + Number(totalWrapFee)
				 
	var dcAmt = Number(productCouponAmt)
				  +Number(orderCouponAmt)
				  +Number(deliveryCouponAmt)
				  +Number(wrapCouponAmt);
	
	$("#saveOrderForm").find("#orderAmt").val(orderAmt);
	$("#saveOrderForm").find("#dcAmt").val(dcAmt);
	$("#saveOrderForm").find("#paymentAmt").val(totalOrderAmt);
	
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
	
	$("#orgTotalSalePriceTxt").html(common.priceFormat(orgTotalSalePrice));
	$("#totalDcAmtTxt").html("- " +common.priceFormat(totalDcAmt));
	$("#totalDeliveryFeeTxt").html("+ " +common.priceFormat(totalDeliveryFee,true));
	$("#totalWrapFeeTxt").html("+ " +common.priceFormat(totalWrapFee,true));
	$("#productCouponAmtTxt").html("- " +common.priceFormat(Number(productCouponAmt) + Number(plusCouponAmt),true));
// 	$("#plusCouponAmtTxt").html(plusCouponAmt);
	$("#wrapCouponAmtTxt").html("- " +common.priceFormat(wrapCouponAmt,true));
	$("#orderCouponAmtTxt").html("-" +common.priceFormat(orderCouponAmt,true));
	$("#deliveryCouponAmtTxt").html("-" +common.priceFormat(deliveryCouponAmt,true));
	$("#pointAmtTxt").html("- " +common.priceFormat(point,true));
	$("#depositAmtTxt").html("- " +common.priceFormat(deposit,true));
	$("#giftAmtTxt").html("- " +common.priceFormat(gift,true));
	$("#totalOrderAmtTxt").html(common.priceFormat(totalOrderAmt));
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

//배송지 상품 template
var productTemplate = function(num){
	
	var cnt = getDeliveryCnt();	
	
	var ops = getOrderproductNos();
	
	var html = '';
	
	if(Number(cnt) > 1){	//복수배송지일때.
		html += '\
				<div class="order_tbl">\
				<!-- ### 테이블 헤더 ### -->\
				<div class="cart_tb_thead1 bd_none">\
					<span class="col1">상품/옵션정보</span>\
		\
					<span class="col2">수량</span>\
		\
					<span class="col3">상품금액</span>\
		\
					<span class="col4">배송비</span>\
				</div>\
				<!-- ### //테이블 헤더 ### -->\
				<!-- ### 테이블 바디 ### -->\
			    <ul class="cart_tb_tbody1">\
			   ';
	}
	for(var i=0;i<ops.length;i++){
		
		var form = "#productForm"+ops[i].value;
		
		var productName = $(form).find("#productName").val();
		
		var saleproductNames = $(form).find("input[name=saleproductName]");
		var orderQty = $(form).find("#orderQty").val();
		var salePrice = $(form).find("#salePrice").val();
		var addSalePrice = $(form).find("#addSalePrice").val();
		var totalSalePrice = Number(salePrice) + Number(addSalePrice);		//총판매가(개당)
		var deliveryFee = $(form).find("#deliveryFee").val();
		var minDeliveryFreeAmt = $(form).find("#minDeliveryFreeAmt").val();
		var deliveryPolicyNo = $(form).find("#deliveryPolicyNo").val();
		var wrapYn = $(form).find("#wrapYn").val();
		var orderDeliveryFee = $(form).find("#orderDeliveryFee"+deliveryPolicyNo).val();
		var paymentAmt = Number(totalSalePrice) * Number(orderQty);	//최종결제가()
		
		var productId = $(form).find("#productId").val();
		var saleproductId = $(form).find("#saleproductId").val();		
		
		if(common.isEmpty(orderDeliveryFee)){
			orderDeliveryFee = 0;
		}
		
		var saleproductNameTxt = "";
		var saleproductName = "";
		for(var j=0;j<saleproductNames.length;j++){
			saleproductNameTxt += '<em><i>옵션 : '+saleproductNames[j].value+'</i></em>';
			saleproductName += " " +saleproductNames[j].value;
		}
		
		var orderOptions = "<option selected value='0'>선택</option>";
		for(var j=0;j<Number(orderQty);j++){
			var value = j+1;
			orderOptions += '<option value='+value+'>'+value+'</option>';
		}
		
		if(Number(cnt) > 1){	//배송지가 여러군데일때 상품 수량 선택.
		
		html += '\
				<li>\
					<div class="tr_box">\
						<div class="td_box col1 td_prdInfo">\
							<div class="prod_img">\
								<a href="#none">\
									<img src="/resources/img/pc/temp/cart_img1.jpg" alt="" />\
								</a>\
							</div>\
		\
							<a href="#none" class="title">\
								'+productName+'\
							</a>\
		\
							'+saleproductNameTxt+'\
						</div>\
		\
						<div class="td_box col2">\
							<div class="slc_quantity">\
								<div class="sel_box select_style1">\
									<label for="selOrderQty'+ops[i].value+'"></label>\
									<select id="selOrderQty'+ops[i].value+'" onchange="javascript:chgDeliveryQty(\''+ops[i].value+'\',\''+orderQty+'\',\''+num+'\',\''+deliveryPolicyNo+'\')">\
										'+orderOptions+'\
									</select>\
								</div>\
								<span class="slash">/</span>\
								'+orderQty+'\
							</div>\
						</div>\
		\
						<div class="td_box col3">\
							<span class="price">'+common.priceFormat(totalSalePrice,true)+'</span>\
						</div>\
						\
						<div class="td_box col4">\
							<span class="price">'+common.priceFormat(deliveryFee,true)+'</span>\
						</div>\
					</div>\
				</li>\
			<!-- ### //테이블 바디 ### -->\
			<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].orderQty" id="orderQty'+ops[i].value+'" value="0"/>\
			<input type="hidden" name="orderDeliveryFee" id="orderDeliveryFee'+ops[i].value+'" value="0" />\
			<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].paymentAmt" id="paymentAmt'+ops[i].value+'" value="0" />\
			';
		}else{
			html += '\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].orderQty" id="orderQty'+ops[i].value+'" value="'+orderQty+'"/>\
					<input type="hidden" name="orderDeliveryFee" id="orderDeliveryFee'+ops[i].value+'" value="'+orderDeliveryFee+'" />\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].paymentAmt" id="paymentAmt'+ops[i].value+'" value="'+paymentAmt+'" />\
					';
		}
		
		html += '\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].orderProductNo" id="orderProductNo" value="'+ops[i].value+'"/>\
				   	<input type="hidden" name="addrOrderProductNo" id="addrOrderProductNo" value="'+ops[i].value+'"/>\
				   	<input type="hidden" name="deliveryPolicyNo" id="deliveryPolicyNo'+ops[i].value+'" value="'+deliveryPolicyNo+'"/>\
				   	<input type="hidden" name="deliveryFee" id="deliveryFee'+ops[i].value+'" value="'+deliveryFee+'" />\
				   	<input type="hidden" name="minDeliveryFreeAmt" id="minDeliveryFreeAmt'+ops[i].value+'" value="'+minDeliveryFreeAmt+'" />\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].totalSalePrice" id="totalSalePrice'+ops[i].value+'" value="'+totalSalePrice+'" />\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].productCouponIssueNo" id="productCouponIssueNo'+ops[i].value+'" value="" />\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].productCouponDcAmt" id="productCouponDcAmt'+ops[i].value+'" value="0" />\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].tempproductCouponDcAmt" id="tempproductCouponDcAmt'+ops[i].value+'" value="0" />\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].productCouponId" id="productCouponId'+ops[i].value+'" value="" />\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].plusCouponIssueNo" id="plusCouponIssueNo'+ops[i].value+'" value="" />\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].plusCouponDcAmt" id="plusCouponDcAmt'+ops[i].value+'" value="0" />\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].tempplusCouponDcAmt" id="tempplusCouponDcAmt'+ops[i].value+'" value="0" />\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].plusCouponId" id="plusCouponId'+ops[i].value+'" value="" />\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].orderCouponIssueNo" id="orderCouponIssueNo'+ops[i].value+'" value="" />\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].orderCouponDcAmt" id="orderCouponDcAmt'+ops[i].value+'" value="0" />\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].temporderCouponDcAmt" id="temporderCouponDcAmt'+ops[i].value+'" value="0" />\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].orderCouponId" id="orderCouponId'+ops[i].value+'" value="" />\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].wrapYn" id="wrapYn'+ops[i].value+'" value="N"/>\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].productId" id="productId'+ops[i].value+'" value="'+productId+'" />\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].saleproductId" id="saleproductId'+ops[i].value+'" value="'+saleproductId+'" />\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].productName" id="productName'+ops[i].value+'" value="'+productName+'" />\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].saleproductName" id="saleproductName'+ops[i].value+'" value="'+saleproductName+'" />\
			   ';
				   	
				   	
// 					<input type="hidden" name="deliveryCouponId" id="deliveryCouponId'+ops[i].value+'" value="" />\
// 				   	<input type="hidden" name="deliveryCouponIssueNo" id="deliveryCouponIssueNo'+ops[i].value+'" value="" />\
// 				   	<input type="hidden" name="deliveryCouponDcAmt" id="deliveryCouponDcAmt'+ops[i].value+'" value="0" />\
// 				   	<input type="hidden" name="applyDeliveryFee" id="applyDeliveryFee'+ops[i].value+'" value="0" />\
		
	}
	
	if(Number(cnt) > 1){	//복수배송지일때
		html += 	'\
					</ul>\
					<!-- ### //테이블 바디 ### -->\
				</div>\
			  ';
	
		var orderpresentIndex = $("input[name=orderpresentIndex]");
		
		if(orderpresentIndex.length > 0){
		
			var presentNames = "";
			for(var i=0;i<orderpresentIndex.length;i++){
				var presentForm = $("#orderPresentForm"+orderpresentIndex[i].value);
				var productId = presentForm.find("#orderpresentProductId").val();
				var name = presentForm.find("#orderpresentName").val();
				var presentId = presentForm.find("#orderPresentId").val();
				presentNames += '\
								<li>\
									<img src="/resources/img/pc/temp/cart_img3.jpg" alt="" />\
									<label><input type="checkbox" class="inp_chk" value="'+num+'" onclick="javascript:chkOrderpresent($(this),\''+presentId+'\',\''+productId+'\')"/><span>'+name+'</span></label>\
								</li>\
								';		
			}
			
			html += '\
					<div class="giftList">\
						<dl>\
							<dd>\
								<ul>\
								'+presentNames+'\
								</ul>\
							</dd>\
						</dl>\
					</div>\
					';
		}
	}
	
	return html;
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

//배송지 template
var addressFormTemplate = function(num){

	var form = $("#defaultMemberAddress");
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
	
	var cnt = getDeliveryCnt();
	
	var orderTypeCd = $("#saveOrderForm").find("#orderTypeCd").val();
	
	var showCls = "";
	var	html = "";
		
				//START ORDER TYPE CD
				if("ORDER_TYPE_CD.GIFT" == orderTypeCd){
					html += '\
							<form name="addressForm" id="addressForm'+num+'">\
							<input type="hidden" name="omsDeliveryaddresss['+num+'].deliveryAddressNo" id="deliveryAddressNo" value="'+num+'"/>\
							';
				}else{
						html += '<dl class="deliveryList">';
	
						if(cnt > 1){
							html +='	<dt>배송지 '+(num+1)+'</dt>';
						}	
							
							html +='\
										<dd>\
										<form name="addressForm" id="addressForm'+num+'">\
											<input type="hidden" name="omsDeliveryaddresss['+num+'].deliveryAddressNo" id="deliveryAddressNo" value="'+num+'"/>\
											<div class="tab_box">\
										';
											
						if(memberYn == 'Y'){		
							html +='		<!-- ### 탭 버튼 ### -->\
											<ul class="tab tab_menu" id="addressTab">\
												<li class="on"><a href="#none">기본 배송지</a></li>\
												<li><a href="#none">배송지 주소록</a></li>\
												<li><a href="#none">새로운 배송지</a></li>\
											</ul>\
											<!-- ### //탭 버튼 ### -->\
											<!-- ### 기본 배송지 ### -->\
											<div class="tabcont tabcontShow">\
												<div class="rw_tbBox">\
													<ul class="rw_tb_tbody2 type1">\
														<li>\
															<div class="tr_box">\
																<div class="col1">\
																	<span class="group_inline">수령인</span>\
																</div>\
																<div class="col2">\
																	<div class="group_block">\
																		'+dDeliveryName+'\
																	</div>\
																</div>\
															</div>\
														</li>\
														<li>\
															<div class="tr_box">\
																<div class="col1">\
																	<span class="group_inline">배송지</span>\
																</div>\
																<div class="col2">\
																	<div class="group_block">\
																		('+dZipCd+') '+dAddress+'\
																	</div>\
																</div>\
															</div>\
														</li>\
														<li class="last">\
															<div class="tr_box">\
																<div class="col1">\
																	<span class="group_inline">연락처</span>\
																</div>\
																<div class="col2">\
																	<div class="group_block">\
																		'+dPhone2+' / '+dPhone1+'\
																	</div>\
																</div>\
															</div>\
														</li>\
													</ul>\
												</div>\
											</div>\
											<!-- ### //기본 배송지 ### -->\
											<!-- ### 배송지 주소록 ### -->\
											<div class="tabcont">\
												<div class="rw_tblBox addrBook">\
													<!-- ### 테이블 바디 ### -->\
													<ul class="rw_tb_tbody1">\
														'+memberAddressList()+'\
													</ul>\
													<!-- ### //테이블 바디 ### -->\
												</div>\
											</div>\
											<!-- ### //배송지 주소록 ### -->\
								';
						}else{							
							showCls = "tabcontShow";
						}
											
							
						html +='			<!-- ### 새로운 배송지 ### -->\
											<div class="tabcont '+showCls+'">\
												<div class="rw_tbBox">\
													<ul class="rw_tb_tbody2">\
														<li>\
															<div class="tr_box">\
																<div class="col1">\
																	<span class="group_inline">수령인</span>\
																</div>\
																<div class="col2">\
																	<div class="group_block">\
																		<input type="text" id="deliveryName1" value="" class="input_style2">\
																	</div>\
																</div>\
															</div>\
														</li>\
														<li>\
															<div class="tr_box">\
																<div class="col1">\
																	<span class="group_inline">휴대전화</span>\
																</div>\
																<div class="col2">\
																	<div class="group_block">\
																		<div class="phoneBox">\
																			<div class="sel_box select_style1">\
																				<label for="slc_optionPhone3"></label>\
																				<select id="slc_optionPhone3">\
																					<option selected>010</option>\
																				</select>\
																			</div>\
																			<div class="inp_box inp_placeholder"><input type="text" value="" id="phone2" class="input_style2"><label for="inp_phone3">‘-’없이 입력하세요</label></div>\
																		</div>\
																	</div>\
																</div>\
															</div>\
														</li>\
														<li>\
															<div class="tr_box">\
																<div class="col1">\
																	<span class="group_inline">전화번호</span>\
																</div>\
																<div class="col2">\
																	<div class="group_block">\
																		<div class="phoneBox">\
																			<div class="sel_box select_style1">\
																				<label for="slc_optionTel3"></label>\
																				<select id="slc_optionTel3">\
																					<option selected>02</option>\
																				</select>\
																			</div>\
																			<div class="inp_box inp_placeholder"><input type="text" value="" id="phone1" class="input_style2"><label for="inp_tel3">‘-’없이 입력하세요</label></div>\
																		</div>\
																	</div>\
																</div>\
															</div>\
														</li>\
														<li>\
															<div class="tr_box">\
																<div class="col1">\
																	<span class="group_inline">이메일</span>\
																</div>\
																<div class="col2">\
																	<div class="group_block">\
																		<input type="text" id="email" value="" class="input_style2">\
																	</div>\
																</div>\
															</div>\
														</li>\
														<li>\
															<div class="tr_box">\
																<div class="col1">\
																	<span class="group_inline">주소</span>\
																</div>\
																<div class="col2">\
																	<div class="group_block addrBox">\
																		<div class="zip">\
																			<input type="text" id="zipCd" value="" class="input_style2">\
																			<a href="#none" class="btn_style6">우편번호조회</a>\
																		</div>\
																		<div>\
																			<input type="text" id="address1" value="" class="input_style2">\
																		</div>\
																		<div>\
																			<input type="text" id="address2" value="" class="input_style2">\
																		</div>\
																		<label><input type="checkbox" id="defaultChk" value="" class="inp_chk">기본 배송지로 설정</label>\
																	</div>\
																</div>\
															</div>\
														</li>\
													</ul>\
												</div>\
											</div>\
											<!-- ### //새로운 배송지 ### -->\
											<!-- ### 배송 요청사항 ### -->\
											<div class="rw_tbBox">\
												<ul class="rw_tb_tbody2 bd_none">\
													<li>\
														<div class="tr_box">\
															<div class="col1">\
																<span class="group_inline">배송 요청사항</span>\
															</div>\
															<div class="col2">\
																<div class="group_block msgBox">\
																	<div>\
																		<div class="sel_box select_style1">\
																			<label for="slc_deliveryMsg2"></label>\
																			<select id="slc_deliveryMsg2">\
																				<option>부재시 경비실에 맡겨주세요</option>\
																			</select>\
																		</div>\
																	</div>\
																	<div>\
																		<input type="text" name="omsDeliveryaddresss['+num+'].note" id="note" class="input_style2">\
																	</div>\
																</div>\
															</div>\
														</div>\
													</li>\
												</ul>\
											</div>\
											';
				}	//END ORDER TYPE CD
							html += 	'<!-- ### //배송 요청사항 ### -->\
										    <input type="hidden" name="omsDeliveryaddresss['+num+'].name1" id="dName1" value=""/>\
											<input type="hidden" name="omsDeliveryaddresss['+num+'].countryNo" id="dCountryNo" value=""/>\
											<input type="hidden" name="omsDeliveryaddresss['+num+'].phone1" id="dPhone1" value=""/>\
											<input type="hidden" name="omsDeliveryaddresss['+num+'].phone2" id="dPhone2" value=""/>\
											<input type="hidden" name="omsDeliveryaddresss['+num+'].zipCd" id="dZipCd" value=""/>\
											<input type="hidden" name="omsDeliveryaddresss['+num+'].address1" id="dAddress1" value=""/>\
											<input type="hidden" name="omsDeliveryaddresss['+num+'].address2" id="dAddress2" value=""/>\
											<input type="hidden" name="omsDeliveryaddresss['+num+'].email" id="dEmail" value=""/>\
											<input type="hidden" name="omsDeliveryaddresss['+num+'].totalorderDeliveryFee" id="totalorderDeliveryFee" value="0"/>\
											<input type="hidden" name="omsDeliveryaddresss['+num+'].totalorderWrapFee" id="totalorderWrapFee" value="0"/>\
											';
											
// 											<input type="hidden" name="omsDeliveryaddresss['+num+'].note" id="note" value=""/>\
	
									html += '\
											'+productTemplate(num)+'\
											';
				if("ORDER_TYPE_CD.GIFT" == orderTypeCd){
						html += '\
							<input type="hidden" name="omsDeliveryaddresss['+num+'].note" id="note" />\
							</form>\
							';
				}else{
									
									html += '\
										</form>\
									   	</div>\
										</dd>\
									</dl>\
									<!-- ### //배송지 1 ### -->\
									';
				}
	
	return html;									
}
//배송 template
var deliveryFormTempate	= function(totalCnt,num,parentNum,deliveryPolicyNo){
							return '\
									<form name="deliveryForm" id="deliveryForm_'+parentNum+'_'+deliveryPolicyNo+'">\
										<input type="hidden" name="omsDeliveryaddresss['+parentNum+'].omsDeliverys['+num+'].deliveryAddressNo" id="deliveryAddressNo" value="'+parentNum+'"/>\
										<input type="hidden" name="omsDeliveryaddresss['+parentNum+'].omsDeliverys['+num+'].deliveryPolicyNo" id="deliveryPolicyNo" value="'+deliveryPolicyNo+'"/>\
										<input type="hidden" name="omsDeliveryaddresss['+parentNum+'].omsDeliverys['+num+'].name" id="name" value=""/>\
										<input type="hidden" name="omsDeliveryaddresss['+parentNum+'].omsDeliverys['+num+'].deliveryServiceCd" id="deliveryServiceCd" value=""/>\
										<input type="hidden" name="omsDeliveryaddresss['+parentNum+'].omsDeliverys['+num+'].deliveryFeeTypeCd" id="deliveryFeeTypeCd" value=""/>\
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
	
	customRadiobox("inp_radio");
	customCheckbox("inp_chk");
	
	fnSelectChange( $('.selectbox select') );
	fnSelectChange( $('.sel_box select') );
	
	// 탭 - 로그인, 상품 상세, 장바구니 등..
	$(".tab_box").find(".tab li").on("click", function(e){
		e.preventDefault();
		var idx = $(this).index();

		$(this).addClass("on").siblings("li").removeClass("on");
		$(this).closest(".tab_box").find(".tabcont").eq(idx).addClass("tabcontShow").siblings(".tabcont").removeClass("tabcontShow");
	});
	
	calc();
}

//할인적용
var dcChg = function(type){
	if(type == 'deposit'){
		var maxAmt = $("#balanceAmt").val();
		var curAmt = $("#deposit").val();
		if(Number(curAmt) > Number(maxAmt)){
			alert("보유 예치금을 초과하였습니다.");
			$("#deposit").val("0");
			return false;
		}
	}else if(type == 'point'){
		var maxAmt = $("#totalPointAmt").val();
		var curAmt = $("#point").val();
		if(Number(curAmt) > Number(maxAmt)){
			alert("보유 포인트를 초과하였습니다.");
			$("#point").val("0");
			return false;
		}
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

var optimalCouponSet = function(){
	
	var couponPopupForm = $("#couponPopupForm");
	
	var cnt = getDeliveryCnt();
	for(var i=0;i<cnt;i++){
		var addrOrderProductNos = getAddrOrderproductNos(i);
		
		for(var j=0;j<addrOrderProductNos.length;j++){
			var opn = addrOrderProductNos[j].value;		
			var form = $("#couponForm_optimalProduct"+opn);
			var productCouponId = form.find("#couponId").val();
			var productCouponIssueNo = form.find("#couponIssueNo").val();
			if(common.isNotEmpty(productCouponIssueNo)){
				couponPopupForm.find("#product"+opn).val(productCouponId+"_"+productCouponIssueNo).prop("selected", true);
				couponSet(true,'product',opn);
				var form2 = $("#couponForm_optimalPlus"+opn);
				var plusCouponId = form2.find("#couponId").val();
				var plusCouponIssueNo = form2.find("#couponIssueNo").val();				
				if(common.isNotEmpty(plusCouponIssueNo)){
					couponPopupForm.find("#plus"+opn).val(plusCouponId+"_"+plusCouponIssueNo).prop("selected", true);
					couponSet(true,'plus',opn);
				}
			}
			
		}
		
	}
	
	var orderForm = $("#couponForm_optimalOrder");
	var selOrderCouponId = orderForm.find("#couponId").val();
	var selOrderCouponIssueNo = orderForm.find("#couponIssueNo").val();
	if(common.isNotEmpty(selOrderCouponIssueNo)){
		couponPopupForm.find("#orderCoupon").val(selOrderCouponId+"_"+selOrderCouponIssueNo).prop("selected", true);
		couponSet(true,'order');
	}
	
	calc();
}

//쿠폰 선택된것
var couponDefaultSelect = function(){
	
	var selOrderCouponId = "";
	var selOrderCouponIssueNo = "";
	
	var couponPopupForm = $("#couponPopupForm");
	
	var cnt = getDeliveryCnt();
	for(var i=0;i<cnt;i++){
		var addrOrderProductNos = getAddrOrderproductNos(i);
		
		for(var j=0;j<addrOrderProductNos.length;j++){
			var opn = addrOrderProductNos[j].value;						
			
			var productCouponId = $("#addressForm"+i).find("#productCouponId"+opn).val();
			var productCouponIssueNo = $("#addressForm"+i).find("#productCouponIssueNo"+opn).val();
// 			console.log("couponDefaultSelect productCouponIssueNo : "+productCouponIssueNo);
// 			console.log(couponPopupForm.find("#product"+opn));
			var selValue = "";
			if(common.isNotEmpty(productCouponIssueNo)){
				selValue = productCouponId+"_"+productCouponIssueNo;
			}
			
			couponPopupForm.find("#product"+opn).val(selValue).prop("selected", true);
			//계산을 위한 임시가격에 실할인가격을 담는다.
			$("#addressForm"+i).find("#tempproductCouponDcAmt").val($("#addressForm"+i).find("#productCouponDcAmt").val());
			couponSet(false,'product',opn);
			
			var plusCouponId = $("#addressForm"+i).find("#plusCouponId"+opn).val();
			var plusCouponIssueNo = $("#addressForm"+i).find("#plusCouponIssueNo"+opn).val();
// 			console.log("couponDefaultSelect plusCouponIssueNo : "+plusCouponIssueNo);

			selValue = "";
			if(common.isNotEmpty(plusCouponIssueNo)){
				selValue = plusCouponId+"_"+plusCouponIssueNo;
			}
			couponPopupForm.find("#plus"+opn).val(selValue).prop("selected", true);
			$("#addressForm"+i).find("#tempplusCouponDcAmt").val($("#addressForm"+i).find("#plusCouponDcAmt").val());
			couponSet(false,'plus',opn);
			
			var orderCouponIssueNo = $("#addressForm"+i).find("#orderCouponIssueNo"+opn).val();
			if(common.isNotEmpty(orderCouponIssueNo)){
				selOrderCouponId = $("#addressForm"+i).find("#orderCouponId"+opn).val();
				selOrderCouponIssueNo = orderCouponIssueNo;
			}
									
			$("#addressForm"+i).find("#temporderCouponDcAmt").val($("#addressForm"+i).find("#orderCouponDcAmt").val());
		}
		
		var sumDelivery = $("input[name=sumDeliveryPolicyNo]");
		
		var selDeliveryValue = "";
		var selWrapValue = "";
		for(var j=0;j<sumDelivery.length;j++){
			var deliveryPolicyNo = sumDelivery[j].value;
			var deliveryForm = $("#deliveryForm_"+i+"_"+deliveryPolicyNo);
			
			var deliveryCouponId = deliveryForm.find("#deliveryCouponId").val();
			var deliveryCouponIssueNo = deliveryForm.find("#deliveryCouponIssueNo").val();
			
			if(common.isNotEmpty(deliveryCouponIssueNo)){
				selDeliveryValue = deliveryCouponId+"_"+deliveryCouponIssueNo;
			}
			
			var wrapCouponId = deliveryForm.find("#wrapCouponId").val();
			var wrapCouponIssueNo = deliveryForm.find("#wrapCouponIssueNo").val();
			if(common.isNotEmpty(wrapCouponIssueNo)){
				selWrapValue = wrapCouponId+"_"+wrapCouponIssueNo;
			}
			
		}
		
		couponPopupForm.find("#delivery"+i).val(selDeliveryValue).prop("selected", true);
		couponPopupForm.find("#wrap"+i).val(selWrapValue).prop("selected", true);
		couponSet(false,'delivery',i);
		couponSet(false,'wrap',i);
	}
	
	couponPopupForm.find("#orderCoupon").val(selOrderCouponId+"_"+selOrderCouponIssueNo).prop("selected", true);
	couponSet(false,'order');
}

//쿠폰 선택 selecbox
var couponSelectBox = function(type,deliveryAddressNo){
	var form = $("form[name=couponForm_"+type+"]");
	var cnt = 0;
	var selectName = "";
	
// 	console.log("couponSelectBox selCouponIssueNo : "+selCouponIssueNo);
	
	var html = '<select name="'+type+'" id="'+type+deliveryAddressNo+'" onchange="javascript:couponChg(\''+type+'\',\''+deliveryAddressNo+'\')">';
		html += '<option selected="" value="">선택</option>';
	for(var i=0;i<form.length;i++){
		var value = form.find("#couponId").val();
		var value2 = form.find("#couponIssueNo").val();
		var name = form.find("#name").val();
		
		html += '<option value="'+value+'_'+value2+'">'+name+'</option>';
	}
	html += '</select>';
	
	
	return html;
	
}

//쿠폰 template
var couponTempate = function(type){
	var html = "";
	var subHtml = "";
	var target = "";
	var title = "";
	if(type == "delivery"){
		title = "배송 무료쿠폰";
		target = "orderDeliveryFee";
	}else if(type == "wrap"){
		title = "선물포장 무료쿠폰";
		target = "orderWrapFee";
	}
	
	var cnt = getDeliveryCnt();
	
	var totalCnt = 0;
	for(var i=0;i<cnt;i++){
		var fee = 0;
		var ads;
		
		if(type == 'delivery'){
			ads = $("#addressForm"+i).find("input[name="+target+"]");
			for(var j=0;j<ads.length;j++){
				fee += Number(ads[j].value); 
			}
		}
		
		if(type == 'wrap'){
			var sumDelivery = $("input[name=sumDeliveryPolicyNo]");
			for(var j=0;j<sumDelivery.length;j++){
				var wrapFee = $("#deliveryForm_"+i+"_"+sumDelivery[j].value).find("#orderWrapFee").val();
				fee += Number(wrapFee);
			}
		}
		
		if(fee > 0){
			subHtml += '\
						<li>\
							<div class="spot">배송지'+(i+1)+'</div>\
							<div class="price">'+common.priceFormat(fee,true,true)+'</div>\
							<div class="coupon">\
								<div class="sel_box select_style1">\
									<label for="slc_coupon4"></label>\
									'+couponSelectBox(type,i)+'\
								</div>\
							</div>\
						</li>\
						<input type="hidden" name="'+type+'DcAmt" id="'+type+'DcAmt'+i+'" value="0"/>\
						<input type="hidden" name="apply'+type+'CouponId" id="apply'+type+'CouponId'+i+'" value=""/>\
						<input type="hidden" name="apply'+type+'CouponIssueNo" id="apply'+type+'CouponIssueNo'+i+'" value=""/>\
						';
				
			totalCnt++;
		}
		
	}
	
	if(totalCnt > 0){		
		html = '\
				<dl>\
				<dt>'+title+'</dt>\
				<dd>\
					<ul>\
					'+subHtml+'\
					</ul>\
					</dd>\
				</dl>\
				<div class="totalBox2">\
					<strong>할인금액</strong>\
					<em id="'+type+'TotalCouponDcAmtTxt">0원</em>\
				</div>\
				';
	}
	
		
	return html;
}
//쿠폰 div
var couponSelect = function(){	
	
	var totalCouponCnt = $("#totalCouponCnt").val();
	
	if(Number(totalCouponCnt) > 0){
		$("#couponDiv #deliveryCouponDiv").html(couponTempate("delivery"));
		$("#couponDiv #wrapCouponDiv").html(couponTempate("wrap"));
		$("#couponDiv").show();
		
		couponDefaultSelect();
		
		customRadiobox("inp_radio");
		customCheckbox("inp_chk");
		
		fnSelectChange( $('.selectbox select') );
		fnSelectChange( $('.sel_box select') );				
				
	}else{
		alert("적용가능한 쿠폰이 없습니다.");
		return;
	}
	
}

//쿠폰 닫기
var couponClose = function(){
	$("#couponDiv").hide();
}

//쿠폰변경(order)
var couponChgOrder = function(){
	couponSet(false,'order');
}
//쿠폰변경(product,plus)
var couponChgPr = function(type,orderProductNo){
	var selectId = type+orderProductNo;		
	var couponFormId = $("#"+selectId).val();	//현재 selectbox value;
	var couponOrderProducts = $("#couponPopupForm").find("input[name=couponOrderProductNo]");
// 	console.log(couponOrderProducts);
	for(var i=0;i<couponOrderProducts.length;i++){
		var loopId = type + couponOrderProducts[i].value;
		var value = $("#couponPopupForm").find("#"+loopId+" option:selected").val();
		
// 		console.log("couponChgPr selectId : "+selectId);
// 		console.log("couponChgPr loopId : "+loopId);
// 		console.log("couponChgPr value : "+value);
// 		console.log("couponChgPr couponFormId : "+couponFormId);
		
		if(common.isNotEmpty(couponFormId)){
			if(loopId != selectId && value == couponFormId){
				alert("이미 선택된 쿠폰입니다.");
				$("#"+selectId).val("");	
				return false;
			}
		}
		
	}
	
	if(type == 'plus'){
		var productCouponFormId = $("#product"+orderProductNo).val();	//현재 selectbox value;
		if(common.isEmpty(productCouponFormId)){
			alert("상품쿠폰이 선택되지 않았습니다.");
			$("#"+selectId).val("");
			return false;
		}
	}
	
	couponSet(false,type,orderProductNo);
	
	//상품쿠폰 없을때 plus쿠폰 초기화
	if(type == 'product'){
		var plusCouponFormId = $("#plus"+orderProductNo).val();	//현재 selectbox value;
		if(couponFormId == ""){			
			$("#plus"+orderProductNo).val("");
			couponSet(false,'plus',orderProductNo);
			
			fnSelectChange( $('.selectbox select') );
			fnSelectChange( $('.sel_box select') );	
		}
	}
	
	couponSet(false,'order');
}
//쿠폰변경(delivery,wrap)
var couponChg = function(type,deliveryAddressNo){
	
	var selectId = type+deliveryAddressNo;	
	
	var couponFormId = $("#"+selectId).val();	//현재 selectbox value;

	var cnt = getDeliveryCnt();
	
	//coupon check (배송지 loop)
	for(var i=0;i<cnt;i++){
		var loopId = type+i; 
// 		console.log($("#couponPopupForm").find("#"+loopId+" option:selected"));
		var value = $("#couponPopupForm").find("#"+loopId+" option:selected").val();
		
		if(common.isNotEmpty(couponFormId)){
			if(loopId != selectId && value == couponFormId){
				alert("이미 선택된 쿠폰입니다.");
				$("#"+selectId).val("");	
				return false;
			}
		}
	}
	
	couponSet(false,type,deliveryAddressNo);
}

applyTotalCouponCnt = 0;
applyTotalCouponAmt = 0;

//쿠폰 선택된 금액 sum 계산
var calCouponSubAmt = function(type){
	var form = "#couponPopupForm";
	var sumAmt = 0;
	var dcAmt = $(form).find("input[name="+type+"DcAmt]");
	for(var i=0;i<dcAmt.length;i++){
		sumAmt += Number(dcAmt[i].value);
	}
	var apply = $(form).find("input[name=apply"+type+"CouponIssueNo]");
	for(var i=0;i<apply.length;i++){
		var couponIssueNo = apply[i].value;
		if(common.isNotEmpty(couponIssueNo)){
			applyTotalCouponCnt++;
		}
	}
	return sumAmt;
}

//쿠폰 setting
var couponSet = function(confirm,type,no){
	
	var selectId = "";
	var applyId = "";
	if(type == 'order'){
		selectId = type+"Coupon";
		applyId = "#apply"+type+"CouponId";
		applyId2 = "#apply"+type+"CouponIssueNo";
	}else{
		selectId = type+no;
		applyId = "#apply"+type+"CouponId"+no;
		applyId2 = "#apply"+type+"CouponIssueNo"+no;
	}
	
	var selValue = $("#couponPopupForm").find("#"+selectId+" option:selected").val();	//현재 selectbox value;	
		
	if(common.isNotEmpty(selValue)){
		var selValueArr = selValue.split("_");
		id = selValueArr[0];
		issueNo = selValueArr[1];
		$("#couponPopupForm").find(applyId).val(id);
		$("#couponPopupForm").find(applyId2).val(issueNo);
	}else{
		$("#couponPopupForm").find(applyId).val('');
		$("#couponPopupForm").find(applyId2).val('');
	}
		
	
	
	var couponFormId = "#couponForm_"+type+"_"+selValue;
	
// 	console.log($(couponFormId));
	
	//쿠폰 조건
	var couponId = common.nvl($(couponFormId).find("#couponId").val(),'');
	var couponIssueNo = common.nvl($(couponFormId).find("#couponIssueNo").val(),'');
	var dcApplyTypeCd = $(couponFormId).find("#dcApplyTypeCd").val();
	var dcValue = $(couponFormId).find("#dcValue").val();
	var maxDcAmt = $(couponFormId).find("#maxDcAmt").val();
	var minOrderAmt = $(couponFormId).find("#minOrderAmt").val();
	
// 	console.log("couponSet no : "+no);
// 	console.log("couponSet couponId : "+couponId);
// 	console.log("couponSet couponIssueNo : "+couponIssueNo);
// 	console.log("couponSet type : "+type);
// 	console.log("couponSet dcApplyTypeCd : "+dcApplyTypeCd);
// 	console.log("couponSet dcValue : "+dcValue);
// 	console.log("couponSet maxDcAmt : "+maxDcAmt);
// 	console.log("couponSet minOrderAmt : "+minOrderAmt);	
	
	//할인금액
	var dcAmt = 0;
	
	
	if(type == 'delivery' || type == 'wrap'){		//배송비,  포장비
		
		var deliveryAddressNo = no;
	
		var orderFeeId = "";
		if(type == 'delivery'){
			orderFeeId = "orderDeliveryFee";		
		}else if(type == 'wrap'){
			orderFeeId = "orderWrapFee";
		}
		var addressForm = "#addressForm"+deliveryAddressNo;
		
// 		console.log($(addressForm).find("#total"+orderFeeId));
		var totalFee = $(addressForm).find("#total"+orderFeeId).val();	//할인 대상금액
		
// 		if(dcApplyTypeCd == 'DC_APPLY_TYPE_CD.AMT'){	//정액
// 			if(Number(totalFee) >= Number(dcValue)){
// 				dcAmt = dcValue;
// 			}else{
// 				dcAmt = totalFee;
// 			}
// 		}else if(dcApplyTypeCd == 'DC_APPLY_TYPE_CD.RATE'){	//정률
// 			dcAmt = Number(totalFee) * Number(dcValue) / 100;
// 			if(Number(dcAmt) > Number(maxDcAmt)){
// 				dcAmt = maxDcAmt;
// 			}
// 		}
		
// 		console.log(dcAmt);

		
		var addressForm = "#addressForm"+deliveryAddressNo;	
// 		//주문상품번호(배송지)
		var orderProductNos = getAddrOrderproductNos(deliveryAddressNo);
		
		//할인금액 분배
		for(var i=0;i<orderProductNos.length;i++){
			var applyId = "";
			var opn = orderProductNos[i].value;	//주문상품번호
			var deliveryPolicyNo = 	$(addressForm).find("#deliveryPolicyNo"+opn).val();
			var deliveryForm = "#deliveryForm_"+deliveryAddressNo+"_"+deliveryPolicyNo;
			var orderFee = 0;			
			if(type == 'delivery'){
				applyId = "applyDeliveryFee";
				orderFee = $(addressForm).find("#orderDeliveryFee"+opn).val();	//주문배송비				
			}else if(type == 'wrap'){
				applyId = "applyWrapFee";
				orderFee = $(deliveryForm).find("#orderWrapFee").val();	//주문포장비				
			}
			
			if(orderFee > 0){
// 				var couponDcAmt = Number(dcAmt) * Number(orderFee) / Number(totalFee);	//포장비쿠폰할인금액
				
				var couponDcAmt = 0;
				
				if(common.isNotEmpty(couponId)){
					couponDcAmt = orderFee;	
				}
				
				dcAmt += Number(couponDcAmt);
				
				var applyFee = Number(orderFee) - Number(couponDcAmt);	//적용포장
				
				if(confirm){
					$(deliveryForm).find("#"+type+"CouponId").val(couponId);
					$(deliveryForm).find("#"+type+"CouponIssueNo").val(couponIssueNo);
					$(deliveryForm).find("#"+type+"CouponDcAmt").val(couponDcAmt);
					$(deliveryForm).find("#"+applyId).val(applyFee);						
				}
			}
		}	
		//배송지별 할인금액 setting
		$("#"+type+"DcAmt"+deliveryAddressNo).val(dcAmt);		

	}else if(type == 'product' || type == 'plus'){		//상품, plus
		
		var orderProductNo = no;
		var cnt = getDeliveryCnt();
	
		var salePrice = $("#productForm"+orderProductNo).find("#salePrice").val();
		var addSalePrice = $("#productForm"+orderProductNo).find("#addSalePrice").val();
		var totOrderQty = $("#productForm"+orderProductNo).find("#orderQty").val();
 		var totalOrderAmt = (Number(salePrice) + Number(addSalePrice)) * Number(totOrderQty)	//상품당 총주문금액
 		var plusCouponDcAmt = common.nvl($("#addressForm"+i).find("#tempproductCouponDcAmt"+orderProductNo).val(),0);
 		if(type == 'product'){
//  			for(var i=0;i<cnt;i++){
//  				totalOrderAmt -= common.nvl($("#addressForm"+i).find("#plusCouponDcAmt"+orderProductNo).val(),0);
//  			}
 		}else{
 			for(var i=0;i<cnt;i++){
 				totalOrderAmt -= plusCouponDcAmt;
 			}
 		}
//  		console.log(totalOrderAmt);
		
 		if(dcApplyTypeCd == 'DC_APPLY_TYPE_CD.AMT'){	//정액
			if(Number(totalOrderAmt) >= Number(dcValue)){
				dcAmt = dcValue;
			}else{
				dcAmt = totalOrderAmt;
			}
		}else if(dcApplyTypeCd == 'DC_APPLY_TYPE_CD.RATE'){	//정률
			dcAmt = Math.round(Number(totalOrderAmt) * Number(dcValue) / 100);
			if(Number(dcAmt) > Number(maxDcAmt)){
				dcAmt = maxDcAmt;
			}
		}
 		
 		//할인금액 setting
		$("#"+type+"DcAmt"+orderProductNo).val(Number(dcAmt) * Number(totOrderQty));
//  		console.log(dcAmt);
		
		for(var i=0;i<cnt;i++){
			var addressForm = "#addressForm"+i;
			var orderQty = $(addressForm).find("#orderQty"+orderProductNo).val();
			
			if(orderQty > 0){
				
		 		var totalFee = (Number(salePrice) + Number(addSalePrice)) * Number(orderQty);	//분배 대상 금액
		 		
		 		//분배일때.
// 		 		var couponDcAmt = (Number(dcAmt) * Number(totalFee) / Number(totalOrderAmt)) / Number(orderQty);	//쿠폰할인가(개당)
		 		
		 		//모두적용.
		 		var couponDcAmt = dcAmt;
		 		
				if(confirm){
					$(addressForm).find("#"+type+"CouponId"+orderProductNo).val(couponId);
					$(addressForm).find("#"+type+"CouponIssueNo"+orderProductNo).val(couponIssueNo);
					$(addressForm).find("#"+type+"CouponDcAmt"+orderProductNo).val(couponDcAmt);
					
					var productCouponDcAmt = $(addressForm).find("#productCouponDcAmt"+orderProductNo).val();
					var plusCouponDcAmt = $(addressForm).find("#plusCouponDcAmt"+orderProductNo).val();
					var orderCouponDcAmt = $(addressForm).find("#orderCouponDcAmt"+orderProductNo).val();
				
// 					console.log("product");
// 					console.log(totalFee);
// 					console.log(productCouponDcAmt);
// 					console.log(plusCouponDcAmt);
// 					console.log(orderCouponDcAmt);
					
					var paymentAmt = Number(totalFee) - (Number(productCouponDcAmt) * Number(orderQty)) - (Number(plusCouponDcAmt) * Number(orderQty)) - Number(orderCouponDcAmt);
// 					console.log(paymentAmt);
					$(addressForm).find("#paymentAmt"+orderProductNo).val(paymentAmt);		//최종결제가(상품당)					
				}else{
					$(addressForm).find("#temp"+type+"CouponId"+orderProductNo).val(couponId);
					$(addressForm).find("#temp"+type+"CouponIssueNo"+orderProductNo).val(couponIssueNo);
					$(addressForm).find("#temp"+type+"CouponDcAmt"+orderProductNo).val(couponDcAmt);
					
					var productCouponDcAmt = $(addressForm).find("#tempproductCouponDcAmt"+orderProductNo).val();
					var plusCouponDcAmt = $(addressForm).find("#tempplusCouponDcAmt"+orderProductNo).val();
					var orderCouponDcAmt = $(addressForm).find("#temporderCouponDcAmt"+orderProductNo).val();
				
// 					console.log("product");
// 					console.log(totalFee);
// 					console.log(productCouponDcAmt);
// 					console.log(plusCouponDcAmt);
// 					console.log(orderCouponDcAmt);
					
					var paymentAmt = Number(totalFee) - (Number(productCouponDcAmt) * Number(orderQty)) - (Number(plusCouponDcAmt) * Number(orderQty)) - Number(orderCouponDcAmt);
// 					console.log(paymentAmt);
					$(addressForm).find("#temppaymentAmt"+orderProductNo).val(paymentAmt);		//최종결제가(상품당)	
				}
			}
		}
	}else if(type == 'order'){
		
		var totalFee = 0;	//할인대상 금액 (주무쿠폰 적용대상인 상품 할인적용전 총판매가) 상품,플러스쿠폰적용
		var totalFee2 = 0;	//할인대상 금액 (주무쿠폰 적용대상인 상품 할인적용전 총판매가)
		var applyProductIds = $("#couponForm_order_"+couponId+"_"+couponIssueNo+" input[name=productId]");	//주문쿠폰적용상품목록
// 		console.log(applyProductIds);
		
		var orderApplyProducts = [];
		
		var cnt = getDeliveryCnt(); 
		for(var i=0;i<cnt;i++){
			var addressForm = "#addressForm"+i;
			var opns = getAddrOrderproductNos(i);
			for(var j=0;j<opns.length;j++){
				var orderProductNo = opns[j].value;
				var productId = $("#productForm"+orderProductNo).find("#productId").val();
				var salePrice = $("#productForm"+orderProductNo).find("#salePrice").val();
				var addSalePrice = $("#productForm"+orderProductNo).find("#addSalePrice").val();
				var orderQty = $(addressForm).find("#orderQty"+orderProductNo).val();
				
				var productCouponDcAmt = $(addressForm).find("#tempproductCouponDcAmt"+orderProductNo).val();
				var plusCouponDcAmt = $(addressForm).find("#tempplusCouponDcAmt"+orderProductNo).val();
				
// 				console.log("productCouponDcAmt : "+productCouponDcAmt);
// 				console.log("plusCouponDcAmt : "+plusCouponDcAmt);
				var applyFlag = false;
				for(var k=0;k<applyProductIds.length;k++){
// 	 				console.log(productId);
// 	 				console.log(applyProductIds[k].value);
					if(applyProductIds[k].value == productId){
						applyFlag = true;
					}
				}
				if(applyFlag){
					orderApplyProducts.push(orderProductNo);	//주문쿠폰 적용 상품.
					totalFee += (Number(salePrice) + Number(addSalePrice) - Number(productCouponDcAmt) - Number(plusCouponDcAmt)) * Number(orderQty) ; 
					totalFee2 += (Number(salePrice) + Number(addSalePrice)) * Number(orderQty) ;
				}

			}				
		}
		
// 		console.log("===== totalFee");
// 		console.log(totalFee);
// 		console.log(totalFee2);
		
		
		if(dcApplyTypeCd == 'DC_APPLY_TYPE_CD.AMT'){	//정액
			if(Number(totalFee) >= Number(dcValue)){
				dcAmt = dcValue;
			}else{
				dcAmt = totalFee;
			}
		}else if(dcApplyTypeCd == 'DC_APPLY_TYPE_CD.RATE'){	//정률
			dcAmt = Number(totalFee) * Number(dcValue) / 100;
			if(Number(dcAmt) > Number(maxDcAmt)){
				dcAmt = maxDcAmt;
			}
		}

// 		console.log("dcAmt : " + dcAmt);
// 		console.log("totalFee : " +totalFee);
		
		//할인금액 setting
		$("#orderDcAmt").val(dcAmt);
		
		var remainOrderAmt = dcAmt;
		
		var cnt = getDeliveryCnt(); 
		for(var i=0;i<cnt;i++){
			var addressForm = "#addressForm"+i;
			var opns = orderApplyProducts;
			for(var j=0;j<opns.length;j++){
				var orderProductNo = opns[j];
				var orderQty = $(addressForm).find("#orderQty"+orderProductNo).val();
				
				var productId = $("#productForm"+orderProductNo).find("#productId").val();
				
// 				//주문쿠폰 적용상품check
// 				var applyFlag = false;
// 				for(var k=0;k<applyProductIds.length;k++){
// 					if(applyProductIds[k].value == productId){
// 						applyFlag = true;
// 					}
// 				}
				
// 				if(!applyFlag) continue;
				
				var salePrice = $("#productForm"+orderProductNo).find("#salePrice").val();
				var addSalePrice = $("#productForm"+orderProductNo).find("#addSalePrice").val();					
				
				//선택되어있는 상품,plus 쿠폰
				var productCouponDcAmt = $(addressForm).find("#tempproductCouponDcAmt"+orderProductNo).val();
				var plusCouponDcAmt = $(addressForm).find("#tempplusCouponDcAmt"+orderProductNo).val();
				
		 		var applyFee = (Number(salePrice) + Number(addSalePrice) - Number(productCouponDcAmt) - Number(plusCouponDcAmt)) * Number(orderQty);	//분배대상 금액

		 		var rate = Math.round(Number(applyFee) * 1000 / Number(totalFee));
		 		var couponDcAmt = Math.round(Number(dcAmt) * rate / 1000);
		 		
		 		remainOrderAmt -= couponDcAmt;
// 		 		console.log("=========================");
// 		 		console.log("totalFee : ",totalFee);
// 		 		console.log("applyFee : ",applyFee);
// 		 		console.log("totalDcAmt : ",dcAmt);
// 				console.log("productCouponDcAmt : ",productCouponDcAmt);
// 				console.log("plusCouponDcAmt : ",plusCouponDcAmt);
// 		 		console.log("couponDcAmt : ",couponDcAmt);
// 		 		console.log("rate : ",rate);
// 		 		console.log("remainOrderAmt : ",remainOrderAmt);
		 		
		 		if(remainOrderAmt > 0 && i+1 == cnt.length && j+1 == opns.length){
		 			couponDcAmt += remainOrderAmt;
// 		 			console.log("last couponDcAmt : ",couponDcAmt);
		 		} else if (remainOrderAmt < 0){
// 		 			console.log("minus remainOrderAmt : ",remainOrderAmt);
		 			couponDcAmt -= Math.abs(remainOrderAmt);
		 		}
		 		
				if(confirm){
															
					$(addressForm).find("#"+type+"CouponId"+orderProductNo).val(couponId);
					$(addressForm).find("#"+type+"CouponIssueNo"+orderProductNo).val(couponIssueNo);
					$(addressForm).find("#"+type+"CouponDcAmt"+orderProductNo).val(couponDcAmt);
								
					var productCouponDcAmt = $(addressForm).find("#productCouponDcAmt"+orderProductNo).val();
					var plusCouponDcAmt = $(addressForm).find("#plusCouponDcAmt"+orderProductNo).val();
					var orderCouponDcAmt = $(addressForm).find("#orderCouponDcAmt"+orderProductNo).val();
					
// 					console.log("order");
// 					console.log(totalFee);
// 					console.log(productCouponDcAmt);
// 					console.log(plusCouponDcAmt);
// 					console.log(orderCouponDcAmt);
					
					var paymentAmt = Number(applyFee) - (Number(productCouponDcAmt) * Number(orderQty)) - (Number(plusCouponDcAmt) * Number(orderQty)) - Number(orderCouponDcAmt);
					
// 					console.log(paymentAmt);
					
					$(addressForm).find("#paymentAmt"+orderProductNo).val(paymentAmt);		//최종결제가(상품당)
				}else{
					$(addressForm).find("#temp"+type+"CouponId"+orderProductNo).val(couponId);
					$(addressForm).find("#temp"+type+"CouponIssueNo"+orderProductNo).val(couponIssueNo);
					$(addressForm).find("#temp"+type+"CouponDcAmt"+orderProductNo).val(couponDcAmt);
								
					var productCouponDcAmt = $(addressForm).find("#tempproductCouponDcAmt"+orderProductNo).val();
					var plusCouponDcAmt = $(addressForm).find("#tempplusCouponDcAmt"+orderProductNo).val();
					var orderCouponDcAmt = $(addressForm).find("#temporderCouponDcAmt"+orderProductNo).val();
					
// 					console.log("order");
// 					console.log(totalFee);
// 					console.log(productCouponDcAmt);
// 					console.log(plusCouponDcAmt);
// 					console.log(orderCouponDcAmt);
					
					var paymentAmt = Number(applyFee) - (Number(productCouponDcAmt) * Number(orderQty)) - (Number(plusCouponDcAmt) * Number(orderQty)) - Number(orderCouponDcAmt);
					
// 					console.log(paymentAmt);
					
					$(addressForm).find("#temppaymentAmt"+orderProductNo).val(paymentAmt);		//최종결제가(상품당)
				}
		 		
			}
		}
	}
	
	if(confirm){						
		
	}else{

		//쿠폰 레이어 합계금액 세팅
		var couponPopupForm = "#couponPopupForm";
		applyTotalCouponCnt = 0;		
		
		var productTotalCouponDcAmtTxt = calCouponSubAmt("product") + calCouponSubAmt("plus");
		var orderTotalCouponDcAmtTxt = calCouponSubAmt("order");
		var deliveryTotalCouponDcAmtTxt = calCouponSubAmt("delivery");
		var wrapTotalCouponDcAmtTxt = calCouponSubAmt("wrap");
		var applyTotalCouponDcAmtTxt = Number(productTotalCouponDcAmtTxt)
									  +Number(orderTotalCouponDcAmtTxt)
									  +Number(deliveryTotalCouponDcAmtTxt)
									  +Number(wrapTotalCouponDcAmtTxt);
		
		//할인금액 txt
		$("#productTotalCouponDcAmtTxt").html(common.priceFormat(productTotalCouponDcAmtTxt,true));
		$("#orderTotalCouponDcAmtTxt").html(common.priceFormat(orderTotalCouponDcAmtTxt,true));
		$("#deliveryTotalCouponDcAmtTxt").html(common.priceFormat(deliveryTotalCouponDcAmtTxt,true));
		$("#wrapTotalCouponDcAmtTxt").html(common.priceFormat(wrapTotalCouponDcAmtTxt,true));				
		$("#applyTotalCouponDcAmtTxt").html(common.priceFormat(applyTotalCouponDcAmtTxt,true));
		$("#applyTotalCouponCnt").html(applyTotalCouponCnt+'개');		
	}
}

//쿠폰 확인
var couponConfirm = function(){		
	
	var cnt =getDeliveryCnt();	
	
	for(var i=0;i<cnt;i++){
		var  orderProductNos = getAddrOrderproductNos(i);
		
		for(var j=0;j<orderProductNos.length;j++){
			couponSet(true,'product',orderProductNos[j].value);
			couponSet(true,'plus',orderProductNos[j].value);
		}
		
		couponSet(true,'delivery',i);
		couponSet(true,'wrap',i);
		
	}		
	couponSet(true,'order');
	
	calc();
	
	$("#couponDiv").hide();	
}

//포장 template
var wrapTempate = function(){
	var html = "";
	
	var cnt = getDeliveryCnt();
	
	for(var i=0;i<cnt;i++){
		
		if(!addressVal(i)){
			return "false";
		}
		
		var ads = getAddrOrderproductNos(i);
		
		var totalCnt = 0;
		var subHtml = "";
		for(var j=0;j<ads.length;j++){
			var opn = ads[j].value;
			var wrapYn = $("#productForm"+opn).find("#wrapYn").val();
			if(wrapYn == 'Y'){
				
				var productName = $("#productForm"+opn).find("#productName").val();
				var saleproductNames = $("#productForm"+opn).find("input[name=saleproductName]");
				var orderQty = $("#addressForm"+i).find("#orderQty"+opn).val();
				var totalSalePrice = $("#addressForm"+i).find("#totalSalePrice"+opn).val();
				
				var saleproductName = "";
				for(var k=0;k<saleproductNames.length;k++){
					saleproductName += '<em><i>옵션 : '+saleproductNames[k].value+'</i></em>';  
				}
				
				if(Number(orderQty) > 0){
					subHtml += '\
								<li>\
									<div class="tr_box">\
										<div class="td_box col1 td_prdInfo">\
											<div class="prod_img">\
												<a href="#none">\
													<img src="/resources/img/pc/temp/cart_img1.jpg" alt="">\
												</a>\
											</div>\
			\
											<a href="#none" class="title">\
											'+productName+'\
											</a>\
											'+saleproductName+'\
										</div>\
			\
										<div class="td_box col2">\
											<span>'+orderQty+'개</span>\
										</div>\
			\
										<div class="td_box col3">\
											<span class="price">'+common.priceFormat(totalSalePrice,true)+'</span>\
										</div>\
			\
										<div class="td_box col4">\
											<label><input type="checkbox" class="inp_chk" name="checkWrap'+i+'" id="checkWrap_'+i+'_'+opn+'" value="'+opn+'" checked></label>\
										</div>\
									</div>\
								</li>\
								';																			
					totalCnt++;
				}
						
			}
		}
		
		if(totalCnt > 0){
			var address = addressInfo(i);
			var name1 = address["deliveryName1"];
			var zipCd = address["zipCd"];
			var address1 = address["address1"];
			var address2 = address["address2"];
			var phone1 = address["phone1"];
			var phone2 = address["phone2"];
			
			html += '\
					<div class="order_tbl">\
						<div class="deliveryInfo">\
						<ul>\
							<li class="spot"><strong>배송지 '+(i+1)+'</strong></li>\
							<li><strong>'+name1+'</strong></li>\
							<li>\
								<p>('+zipCd+') '+address1+'	'+address2+'</p>\
								<span class="phone">'+phone1+'/'+phone2+'</span>\
							</li>\
						</ul>\
						</div>\
						<!-- ### 테이블 바디 ### -->\
						<ul class="cart_tb_tbody1">\
						'+subHtml+'\
				  		</ul>\
						<div class="giftQ">\
							<p>※ 부피가 작은 상품의 선물 포장을 하실 경우 함께 포장하시면 선물 포장비를 절약하실 수 있습니다.</p>\
							<div>\
								<span>상품을 함께 포장하시겠습니까?</span>\
								<ul>\
									<li><label><input type="radio" value="N" name="packing'+i+'" class="inp_radio" checked>개별포장</label></li>\
									<li><label><input type="radio" value="Y" name="packing'+i+'" class="inp_radio">합포장</label></li>\
								</ul>\
							</div>\
						</div>\
						<!-- ### //테이블 바디 ### -->\
				  </div>';
			
		}
		
	}	
	
	return html;
}

var wrapConfirm = function(){
	
	var cnt = getDeliveryCnt();
	
	for(var i=0;i<cnt;i++){
		
		var wrapTogetherYn = $("input:radio[name=packing"+i+"]:checked").val();
		
		var ads = getAddrOrderproductNos(i);
		
		var totalCntObj = {};
		var togetherCnt = 0;
		
		for(var j=0;j<ads.length;j++){
			var opn = ads[j].value;
			var wrapYn = $("#productForm"+opn).find("#wrapYn").val();
// 			console.log("wrapYn : "+wrapYn);
			
			if(wrapYn == 'Y'){
				var checkBox = $("#checkWrap_"+i+"_"+opn+":checked");
// 				console.log(checkBox);
				if(checkBox){										
					
					var orderQty = $("#addressForm"+i).find("#orderQty"+opn).val();
					var wrapVolume = $("#productForm"+opn).find("#wrapVolume").val();
					var deliveryPolicyNo = $("#addressForm"+i).find("#deliveryPolicyNo"+opn).val();
					
					var wrapCnt = Number(wrapVolume) * Number(orderQty);
					var calcWrapCnt = Math.ceil(wrapCnt);
					
					if(wrapTogetherYn == "Y"){
						
					}else{
						wrapCnt = calcWrapCnt;
					}
					
					var totalCnt = totalCntObj[deliveryPolicyNo];
					if(common.isNotEmpty(totalCnt)){
						totalCntObj[deliveryPolicyNo] = Number(totalCnt) + Number(wrapCnt);
					}else{
					 	totalCntObj[deliveryPolicyNo] = wrapCnt;
					}
					
// 					console.log("wrapConfirm wrapTogetherYn : " + wrapTogetherYn);
// 					console.log("wrapConfirm wrapVolume : " +wrapVolume);
// 					console.log("wrapConfirm wrapCnt : "+wrapCnt);
// 					console.log("wrapConfirm calcWrapCnt : "+calcWrapCnt);
// 					console.log(totalCntObj);
// 					console.log("==============");
						
					
					
					$("#addressForm"+i).find("#wrapYn"+opn).val("Y");	//포장 check
				}
			}
		}
		
		for(var j=0;j<ads.length;j++){
			var opn = ads[j].value;
			var wrapYn = $("#addressForm"+i).find("#wrapYn"+opn).val();
			
			if(wrapYn == 'Y'){
				var deliveryPolicyNo = $("#addressForm"+i).find("#deliveryPolicyNo"+opn).val();
				
				var wrapCnt = totalCntObj[deliveryPolicyNo];
				
// 				console.log("wrapConfirm tot wrapCnt : "+ wrapCnt);
				
				var orderWrapFee = Math.ceil(wrapCnt) * 1000;
				
// 				console.log("wrapConfirm tot orderWrapFee : "+ orderWrapFee);
// 				console.log("==============");
				
				$("#deliveryForm_"+i+"_"+deliveryPolicyNo).find("#wrapTogetherYn").val(wrapTogetherYn);
				$("#deliveryForm_"+i+"_"+deliveryPolicyNo).find("#orderWrapFee").val(orderWrapFee);
				$("#deliveryForm_"+i+"_"+deliveryPolicyNo).find("#applyWrapFee").val(orderWrapFee);
			}
		}		
	}
	calc();
	$("#wrapDiv").hide();
}

var sumCouponCnt = function(){ 
	
	var couponIds = $("input[name=couponId]");
	var arrCoupon = [];
	for(var i=0;i<couponIds.length;i++){
		var couponId = couponIds[i].value;
		var ex = false;
		for(var j=0;j<arrCoupon.length;j++){
			if(couponId == arrCoupon[j]){
				ex = true;
			}
		}
		if(!ex){
			arrCoupon.push(couponId);
		}
	}
	
	var totalCouponCnt = arrCoupon.length;
		
	$("#totalCouponCnt").val(totalCouponCnt);
	$("#applyCouponCnt").html(totalCouponCnt);
}

//포장선택
var wrapSelect = function(){
	var html = wrapTempate();
	if(html != "false"){
		$("#wrapDiv #deliveryDiv").html(html);
		$("#wrapDiv").show();
		customRadiobox("inp_radio");
		customCheckbox("inp_chk");
	}
}
//포장선택 닫기
var wrapClose = function(){
	$("#wrapDiv").hide();
}

//예치금 모두사용
var depositAll = function(){	
	$("#deposit").val($("#balanceAmt").val());
}

//현금영수증
var receiptUseChg = function(obj){
	var receiptUse = $(obj).val();
	if(receiptUse == "0"){
		$("#receiptUseInfo").hide();
	} else {
		$("#receiptUseInfo").show();
	}
}
</script>	
<div class="content">
	<!-- pc 전용 네비 -->
	<div class="location_box">
		<div class="location_inner">
			<ul>
				<li class="home"><span class="hide">홈</span></li>
				<li>
					주문/결제
				</li>
			</ul>
		</div>
	</div>

	<div class="inner">
		<div class="orderBox">
			<div class="step">
				<h3 class="title_type1">
					주문/결제
				</h3>
				<ol>
					<li><span class="step_01">01</span>장바구니</li>
					<li class="active"><span class="step_02">02</span>주문/결제</li>
					<li><span class="step_03">03</span>주문완료</li>
				</ol>
			</div>
			
<c:forEach items="${memberAddressList }" var="ma" varStatus="status">
<c:if test="${ma.addressNo == memberInfo.addressNo }">
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
</c:if>
<form name="memberAddress" id="memberAddress${status.index }">
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
</c:forEach>

<form action="/oms/order/complete" name="orderCompleteForm" id="orderCompleteForm" method="post">
	<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }"/>
	<input type="hidden" name="orderId" id="orderId" value=""/>
</form>
<form action="/api/oms/order/save" name="mergeForm" id="mergeForm">
</form>
<form name="saveOrderForm" id="saveOrderForm" method="post">	
<!-- 	//상품사은품 "PRESENT_TYPE_CD.PRODUCT":orderProductNo:presentId:productIds -->
<!-- 	//주문사은품 "PRESENT_TYPE_CD.ORDER":presentId:productIds -->
<!-- 	//주문사은품 배송지 선택 "DELIVERY_INFO":presentId:productId:deliveryAddressNo:deliveryPolicyNo" -->
	<input type="hidden" name="cartProductNos" value="${omsOrder.cartProductNos }"/>	
	<input type="hidden" name="selectPresent" id="selectPresent" value="${omsOrder.selectPresent }"/>	
	<input type="hidden" name="selectCoupon" value=""/>
	<input type="hidden" name="orderStat" value="ORDERSHEET"/>
	
	<input type="hidden" name="memberYn" id="memberYn" value="${memberYn }"/>
	<input type="hidden" name="orderPwd" id="orderPwd" value=""/>
	
	<input type="hidden" name="giftYn" value="${omsOrder.giftYn }"/>	
	<input type="hidden" name="orderTypeCd" id="orderTypeCd" value="${omsOrder.orderTypeCd }"/>
	<input type="hidden" name="orderAmt" id="orderAmt" value=""/>
	<input type="hidden" name="dcAmt" id="dcAmt" value=""/>
	<input type="hidden" name="paymentAmt" id="paymentAmt" value=""/>
	
	<input type="hidden" name="channelId" id="channelId" value="${omsOrder.channelId}"/>
	
	<!-- 주문자정보 -->
	<input type="hidden" name="name1" id="name1" value=""/>
	<input type="hidden" name="phone1" id="phone1" value=""/>
	<input type="hidden" name="phone2" id="phone2" value=""/>
	<input type="hidden" name="email" id="email" value=""/>
	<input type="hidden" name="zipCd" id="zipCd" value="1111"/>
	<input type="hidden" name="address1" id="address1" value="TEST"/>
	<input type="hidden" name="address2" id="address2" value="TEST"/>
	<input type="hidden" name="giftPhone" id="giftPhone" value=""/>
		
	<!-- 금액 정보 -->
	<input type="hidden" name="orgTotalSalePrice" id="orgTotalSalePrice" value="0"/>
	<input type="hidden" name="totalOrderSalePrice" id="totalOrderSalePrice" value="${omsOrder.totalOrderSalePrice }"/>
	<input type="hidden" name="totalDcAmt" id="totalDcAmt" value="0"/>
	<input type="hidden" name="totalDeliveryFee" id="totalDeliveryFee" value="0"/>
	<input type="hidden" name="totalWrapFee" id="totalWrapFee" value="0"/>
	<input type="hidden" name="productCouponAmt" id="productCouponAmt" value="0"/>
	<input type="hidden" name="plusCouponAmt" id="plusCouponAmt" value="0"/>
	<input type="hidden" name="wrapCouponAmt" id="wrapCouponAmt" value="0"/>
	<input type="hidden" name="orderCouponAmt" id="orderCouponAmt" value="0"/>
	<input type="hidden" name="deliveryCouponAmt" id="deliveryCouponAmt" value="0"/>
	<input type="hidden" name="pointAmt" id="pointAmt" value="0"/>
	<input type="hidden" name="depositAmt" id="depositAmt" value="0"/>
	<input type="hidden" name="giftAmt" id="giftAmt" value="0"/>
	<input type="hidden" name="totalOrderAmt" id="totalOrderAmt" value="0"/>			
					
</form>
			<div class="order_tbl">
				<!-- ### 테이블 헤더 ### -->
				<div class="cart_tb_thead1">
					<span class="col1">상품/옵션정보</span>

					<span class="col2">수량</span>

					<span class="col3">상품금액</span>

					<span class="col4">배송비</span>

					<span class="col5">주문금액</span>
				</div>
				<!-- ### //테이블 헤더 ### -->

				<!-- ### 테이블 바디 ### -->
				<ul class="cart_tb_tbody1">
					<c:set var="productQtyCnt" value="0"/>
					<c:set var="subTotalSalePrice" value="0"/>
					<c:set var="subDeliveryFeeFreeYn" value="N"/>
					<c:forEach items="${omsOrder.omsOrderproducts }" var="os" varStatus="status">
					<c:if test="${os.overseasPurchaseYn == 'Y' }">
						<c:set var="overseaYn" value="true"/>
					</c:if>
					<c:if test="${os.wrapYn == 'Y' }">
						<c:set var="wrapYn" value="true"/>
					</c:if>
					<li>
						<div class="tr_box">
						<form name="productForm" id="productForm${os.orderProductNo }">
						<input type="hidden" name="tempOrderProductNo" value="${os.orderProductNo }"/>
						<input type="hidden" name="productId" id="productId" value="${os.productId}"/>
						<input type="hidden" name="saleproductId" id="saleproductId" value="${os.saleproductId}"/>
						<input type="hidden" name="dealId" id="dealId" value="${os.dealId}"/>
						<input type="hidden" name="orderProductTypeCd" id="orderProductTypeCd" value="${os.orderProductTypeCd}"/>
						
						<input type="hidden" name="orderQty" id="orderQty" value="${os.orderQty}"/>
						<input type="hidden" name="overseasPurchaseYn" id="overseasPurchaseYn" value="${os.overseasPurchaseYn }"/>
						<input type="hidden" name="deliveryAddressNo" id="deliveryAddressNo" value="1"/>
						<input type="hidden" name="deliveryPolicyNo" id="deliveryPolicyNo" value="${os.deliveryPolicyNo }"/>
						<input type="hidden" name="deliveryFee" id="deliveryFee" value="${os.ccsDeliverypolicy.deliveryFee }"/>
						<input type="hidden" name="deliveryName" id="deliveryName" value="${os.ccsDeliverypolicy.name }"/>
						<input type="hidden" name="deliveryServiceCd" id="deliveryServiceCd" value="${os.ccsDeliverypolicy.deliveryServiceCd }"/>
						<input type="hidden" name="deliveryFeeTypeCd" id="deliveryFeeTypeCd" value="${os.ccsDeliverypolicy.deliveryFeeTypeCd }"/>
						<input type="hidden" name="minDeliveryFreeAmt" id="minDeliveryFreeAmt" value="${os.ccsDeliverypolicy.minDeliveryFreeAmt }"/>
						<input type="hidden" name="wrapYn" id="wrapYn" value="${os.wrapYn }"/>
						<input type="hidden" name="wrapVolume" id="wrapVolume" value="${os.wrapVolume }"/>
						<input type="hidden" name="boxDeliveryYn" id="boxDeliveryYn" value="${os.boxDeliveryYn }"/>
						<input type="hidden" name="boxUnitCd" id="boxUnitCd" value="${os.boxUnitCd }"/>
						<input type="hidden" name="boxUnitQty" id="boxUnitQty" value="${os.boxUnitQty }"/>
						<input type="hidden" id="productName" value="${os.productName }" />
						<input type="hidden" name="orgTotalSalePrice" id="orgTotalSalePrice" value="${os.orgTotalSalePrice }" />
						<input type="hidden" name="salePrice" id="salePrice" value="${os.salePrice }" />
						<input type="hidden" name="addSalePrice" id="addSalePrice" value="${os.addSalePrice }" />
						<input type="hidden" name="deliveryFeeFreeYn" id="deliveryFeeFreeYn" value="${os.deliveryFeeFreeYn }" />
						
						<c:set var="productQtyCnt" value="${productQtyCnt + os.orderQty}"/>												
						
						<c:set var="subTotalSalePrice" value="${(os.salePrice + os.addSalePrice) * os.orderQty }"/>
						
						<c:if test="${os.deliveryFeeFreeYn  == 'Y' }">
							<c:set var="subDeliveryFeeFreeYn" value="Y"/>
						</c:if>
												
						<c:set var="orderDeliveryFee" value="0"/>
						<c:if test="${status.last || os.deliveryPolicyNo != omsOrder.omsOrderproducts[status.index+1].deliveryPolicyNo }">
							<input type="hidden" name="sumDeliveryPolicyNo" value="${os.deliveryPolicyNo }"/>
							<c:choose>
							<c:when test="${os.ccsDeliverypolicy.minDeliveryFreeAmt > subTotalSalePrice && subDeliveryFeeFreeYn == 'N' }">
								<c:set var="orderDeliveryFee" value="${os.ccsDeliverypolicy.deliveryFee }"/>
							</c:when>
							<c:otherwise>
								<c:set var="orderDeliveryFee" value="0"/>
							</c:otherwise>
							</c:choose>
							<input type="hidden" name="orderDeliveryFee" id="orderDeliveryFee${os.deliveryPolicyNo }" value="${orderDeliveryFee }"/>
							<c:set var="subTotalSalePrice" value="0"/>
							<c:set var="subDeliveryFeeFreeYn" value="N"/>
						</c:if>
							<div class="td_box col1 td_prdInfo">
								<div class="prod_img">
									<a href="#none">
										<img src="/resources/img/pc/temp/cart_img1.jpg" alt="" />
									</a>
								</div>

								<a href="#none" class="title">
									${os.productName }
								</a>
								
								<c:choose>
								<c:when test="${os.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SET' }">
									<c:forEach items="${os.omsOrderproducts }" var="oss" varStatus="st">
										<input type="hidden" name="omsOrderproducts[${st.index }].productId" id="productId" value="${oss.productId }"/>
										<input type="hidden" name="omsOrderproducts[${st.index }].saleproductId" id="saleproductId" value="${oss.saleproductId }"/>
										<input type="hidden" name="saleproductName" value="${oss.saleproductName }" />
										<em>
											<i>옵션 : ${oss.saleproductName }</i>
										</em>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<input type="hidden" name="saleproductName" value="${os.saleproductName }" />
									<em>
										<i>옵션 : ${os.saleproductName }</i>
									</em>
								</c:otherwise>
								</c:choose>

								<c:forEach items="${os.spsPresents }" var="osp">
								<c:if test="${osp.spsPresentproducts.size() > 0 }">
								<u>
									<a href="#none" class="btn_gift_view">[사은품] ${osp.name }</a>
								</u>
								<div class="option_box gift_box">
									<ul>									
										<c:forEach items="${osp.spsPresentproducts }" var="osps" varStatus="subIdx">
											<li>
												<img src="/resources/img/pc/temp/cart_img3.jpg" alt="" />
												<span>${osps.pmsProduct.name }</span>
												
												<c:forEach items="${osps.pmsProduct.pmsSaleproducts }" var="ps" varStatus="subIdx">
												
												<c:if test="${ps.realStockQty < os.orderQty }" >
													${ps.realStockQty }
												</c:if>
												</c:forEach>
											</li>																
										</c:forEach>				
														
									</ul>

									<a href="#none" class="btn_close">옵션창 닫기</a>
								</div>
								</c:if>
								</c:forEach>								
							</div>

							<div class="td_box col2">
								<span>${os.orderQty }개</span>
							</div>

							<div class="td_box col3">
								<span class="price"><fmt:formatNumber value="${(os.salePrice + os.addSalePrice)}" pattern="#,###" />원</span>
							</div>
							
							<div class="td_box col4">
								<span class="price"><fmt:formatNumber value="${orderDeliveryFee }" pattern="#,###" />원</span>
							</div>

							<div class="td_box col5">
								<span class="price"><fmt:formatNumber value="${(os.salePrice + os.addSalePrice) * os.orderQty + orderDeliveryFee }" pattern="#,###" />원</span>
							</div>
						</form>
						</div>
					</li>
					</c:forEach>																		
				</ul>
				<!-- ### //테이블 바디 ### -->
			</div>
			<input type="hidden" id="deliveryCnt" value="${deliveryCnt }"/>
			
			
			<c:forEach items="${omsOrder.spsPresents }" var="op">
			<c:if test="${op.spsPresentproducts.size() > 0 }">
			<div class="giftList">
				<dl>
					<dt>
						<div>
							<strong>${op.name }</strong>
							<span>${op.startDt } ~ ${op.endDt }</span>
						</div>
					</dt>
					<dd>
						<ul>
							<c:forEach items="${op.spsPresentproducts }" var="ops" varStatus="subIdx">
							<li>
								<img src="/resources/img/pc/temp/cart_img3.jpg" alt="" />
								<span>${ops.pmsProduct.name }</span>
								<form name="orderPresentForm" id="orderPresentForm${subIdx.index}">
								<input type="hidden" name="orderpresentIndex" id="orderpresentIndex" value="${subIdx.index }"/>							
								<input type="hidden" name="orderpresentProductId" id="orderpresentProductId" value="${ops.pmsProduct.productId }"/>
								<input type="hidden" name="orderpresentName" id="orderpresentName" value="${ops.pmsProduct.name }"/>
								<input type="hidden" name="orderPresentId" id="orderPresentId" value="${op.presentId }"/>
								</form>
							</li>
							</c:forEach>							
						</ul>
					</dd>
				</dl>
			</div>
			</c:if>
			</c:forEach>

			<c:if test="${overseaYn }">
			<div class="overseas">
				<div class="left">
					<dl>
						<dt>주문 상품 중 해외구매대행 상품이 포함되어 있습니다.</dt>
						<dd>
							<p>해외구매대행 상품의 경우 200$ 이상 구매 시 혹은 해외분유 및 일부 상품 구매 시 관세청 통관업무를 위해 개인통관고유부호를 반드시 입력해주셔야 합니다.<br />개인통관고유부호 미입력시 결제가 이루어지지 않으며, 잘못된 개인통관부호 입력 시 배송이 지연될 수 있습니다.</p>
							<p>* 개인통관 고유부호: 관세청 통관업무에 주민등록번호 대신 사용하는 제도입니다.</p>
							<a href="#">관세청 개인통관부호 신청, 조회 바로가기</a>
						</dd>
					</dl>
				</div>
				<div class="right">
					<strong>개인통관 고유부호</strong>
					<div class="inp_box"><input type="text" id="personalCustomsCode" value=""/></div>
				</div>
			</div>
			<div class="chkAgree"><label><input type="checkbox" name="agreeCheck" value="개인통관 고유부호 정보 제공" class="inp_chk">입력하신 개인통관 고유부호 정보는 판매자에게 제공되며, 이에 동의합니다.</label></div>
			</c:if>

			<div class="orderInfoBox">
				<!-- ### orderInfoL ### -->
				<div class="orderInfoL">
					<div class="orderInner">
						<!-- ### 주문자 정보 ### -->
						<form id="orderForm">
						<div class="relBox">
							<h3 class="sub_tit1">주문자 정보</h3>
							<div class="rw_tbBox">
								<ul class="rw_tb_tbody2">
<!-- 									<li> -->
<!-- 										<div class="tr_box"> -->
<!-- 											<div class="col1"> -->
<!-- 												<span class="group_inline">인증방법</span> -->
<!-- 											</div> -->
<!-- 											<div class="col2"> -->
<!-- 												<div class="group_block"> -->
<!-- 													<a href="#none" class="btn_style6">휴대폰 인증</a> -->
<!-- 													<a href="#none" class="btn_style6">I-PIN 인증</a> -->
<!-- 												</div> -->
<!-- 											</div> -->
<!-- 										</div> -->
<!-- 									</li> -->
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">이름</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<input type="text" id="name1" value="${memberInfo.mmsMember.memberName}" class="input_style2">
												</div>
											</div>
										</div>
									</li>
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">휴대전화</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<div class="phoneBox">
														<div class="sel_box select_style1">
															<label for="slc_optionPhone"></label>															
															<select id="slc_optionPhone">
																<option selected>010</option>																
															</select>
														</div>
														<div class="inp_box inp_placeholder"><input type="text" value="${memberInfo.mmsMember.phone2 }" id="phone2" class="input_style2"><label for="inp_phone">‘-’없이 입력하세요</label></div>
													</div>
												</div>
											</div>
										</div>
									</li>
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">전화번호</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<div class="phoneBox">
														<div class="sel_box select_style1">
															<label for="slc_optionTel"></label>
															<select id="slc_optionTel">
																<option selected>02</option>
															</select>
														</div>
														<div class="inp_box inp_placeholder"><input type="text" value="${memberInfo.mmsMember.phone1 }" id="phone1" class="input_style2"><label for="inp_tel">‘-’없이 입력하세요</label></div>
													</div>
												</div>
											</div>
										</div>
									</li>
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">이메일</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<input type="text" id="email" value="${memberInfo.mmsMember.email }" class="input_style2">
												</div>
											</div>
										</div>
									</li>									
								</ul>
							</div>
						</div>
						</form>
						<!-- ### //주문자 정보 ### -->

						<c:choose>
						<c:when test="${omsOrder.orderTypeCd == 'ORDER_TYPE_CD.GIFT' }">
						<div class="relBox">
							<h3 class="sub_tit1">선물받으실분 정보</h3>
							<div class="rw_tbBox">
								<ul class="rw_tb_tbody2">
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">선물받을 휴대폰 번호</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<div class="phoneBox">
														<div class="sel_box select_style1">
															<label for="slc_optionPhone5"></label>
															<select id="slc_optionPhone5">
																<option selected>010</option>
															</select>
														</div>
														<div class="inp_box inp_placeholder"><input type="text" value="" name="giftPhoneNum" id="giftPhoneNum" class="input_style2"><label for="inp_phone5">‘-’없이 입력하세요</label></div>
													</div>
													<p>배송받을 주소 입력이 가능한 url이 발송됩니다.<br />받는 분의 통신 상태에 따라 메시지가 발송되지 않거나, 확인이 어려울 수 있습니다. 이 경우 결제 후 8일째에 자동 환불됩니다.</p>
												</div>
											</div>
										</div>
									</li>
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">이름</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<input type="text" name="giftName1" id="giftName1" value="" class="input_style2">
												</div>
											</div>
										</div>
									</li>
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">선물메세지</span>
											</div>
											<div class="col2">
												<div class="group_block msgBox">
													<div>
														<div class="sel_box select_style1">
															<label for="slc_gift"></label>
															<select id="slc_gift">
																<option>생일 축하해~</option>
															</select>
														</div>
													</div>
													<div class="txtarea_box">
														<textarea rows="5" cols="10" id="blrablra"></textarea>
													</div>
												</div>
											</div>
										</div>
									</li>
								</ul>
							</div>
						</div>
						<div id="addressDiv"></div>
						<div id="deliveryDiv"></div>
						</c:when>
						<c:otherwise>						
						<!-- ### 배송지 정보 ### -->
						<div class="relBox">
							<h3 class="sub_tit1">배송지 정보</h3>
							<c:if test="${productQtyCnt > 1 }">
							<div class="chkList">
								<div class="qMemo"><span>상품을 여러 군데로 배송하시겠습니까?</span></div>
								<ul>
									<li>
										<label><input type="radio" name="dsel" id="ra1" class="inp_radio" value="1" onclick="javascript:addrChg(1)" checked="checked">1군데</label>

									</li>
									<li>
										<label><input type="radio" name="dsel" id="ra2" class="inp_radio" value="2" onclick="javascript:addrChg(2)">2군데</label>

									</li>
									<c:if test="${productQtyCnt > 2 }">
									<li>
										<label><input type="radio" name="dsel" id="ra3" class="inp_radio" value="3" onclick="javascript:addrChg(3)">3군데</label>
									</li>
									</c:if>
								</ul>
							</div>
							</c:if>
							
							<div id="addressDiv"></div>
							<div id="deliveryDiv"></div>
													
						</div>
						<!-- ### //배송지 정보 ### -->												
						
						<c:if test="${wrapYn }">
						<!-- ### 선물 포장 ### -->
						<div class="giftPacking">
							<div>선물포장이 가능한 상품이 있습니다. 선물포장 하시겠습니까?</div>
							<a href="#none" class="btn_style6 btnPacking" onclick="javascript:wrapSelect()">선물포장 하러가기</a>
						</div>
						<!-- ### //선물 포장 ### -->
						</c:if>
						</c:otherwise>
						</c:choose>

						<c:choose>
						<c:when test="${memberYn == 'N' }">
<!-- 						<input type="hidden" id="applyTotalCouponAmt" value="0"/> -->
						<input type="hidden" id="point" value="0"/>
						<input type="hidden" id="deposit" value="0"/>
						<input type="hidden" id="gift" value="0"/>
						</c:when>
						<c:otherwise>
						<div class="relBox">
							<h3 class="sub_tit1">할인정보</h3>
							<div class="rw_tbBox">
								<ul class="rw_tb_tbody2">
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">쿠폰</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<div>
														<div class="unitBox">
															<input type="text" id="applyTotalCouponAmt" value="0" class="input_style2" readonly="readonly">
															<span class="unit">원</span>
														</div>
														<a href="#none" class="btn_style6 btnCoupon" onclick="javascript:couponSelect()">쿠폰조회</a>
													</div>
													<p class="detailTxt">( 적용가능 쿠폰: <span id="applyCouponCnt">0</span> / 보유 쿠폰: <span id="memberCouponTotal">${memberCouponTotal }</span> )</p>
												</div>
											</div>
										</div>
									</li>
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">매일 포인트</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<div>
														<div class="unitBox">
															<input type="text" name="point" id="point" value="0" onchange="javascript:dcChg('point')" class="input_style2">
															<span class="unit">P</span>
														</div>
														<label><input type="checkbox" value="" class="inp_chk">모두사용</label>
													</div>
													<input type="hidden" id="totalPointAmt" value="${memberPoint.rmnd_pint }"/>
													<p class="detailTxt">( 보유 매일 포인트: <span><fmt:formatNumber value="${memberPoint.rmnd_pint }" pattern="#,###"/>P</span> /  <span>10P</span> 이상 사용가능)</p>
												</div>
											</div>
										</div>
									</li>
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">예치금</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<div>
														<div class="unitBox">
															<input type="text" name="deposit" id="deposit" value="0" onchange="javascript:dcChg('deposit')" class="input_style2">
															<span class="unit">원</span>
														</div>
														<label><input type="checkbox" value="" class="inp_chk" onclick="javascript:depositAll()">모두사용</label>
													</div>
													<input type="hidden" id="balanceAmt" value="${memberDeposit.balanceAmt }"/>
													<p class="detailTxt">( 보유 예치금: <span><fmt:formatNumber value="${memberDeposit.balanceAmt }" pattern="#,###"/>원</span> )</p>
												</div>
											</div>
										</div>
									</li>
<!-- 									<li> -->
<!-- 										<div class="tr_box"> -->
<!-- 											<div class="col1"> -->
<!-- 												<span class="group_inline">모바일상품권</span> -->
<!-- 											</div> -->
<!-- 											<div class="col2"> -->
<!-- 												<div class="group_block"> -->
<!-- 													<div> -->
<!-- 														<div class="unitBox"> -->
<!-- 															<input type="text" name="gift" id="gift" value="0" onchange="javascript:dcChg('gift')" class="input_style2"> -->
<!-- 															<span class="unit">원</span> -->
<!-- 														</div> -->
<!-- 														<a href="#none" class="btn_style6 btnCoupon">상품권조회</a> -->
<!-- 													</div> -->
<!-- 												</div> -->
<!-- 											</div> -->
<!-- 										</div> -->
<!-- 									</li> -->
								</ul>
							</div>
						</div>
						</c:otherwise>
						</c:choose>
						<!-- ### 결제수단 ### -->
						<div class="relBox paymentList">
							<h3 class="sub_tit1">결제수단</h3>
							<div class="rw_tbBox">
								<ul class="rw_tb_tbody2">
									<li>
										<div class="tr_box">
											<div class="col">
												<ul>
													<li><label><input type="radio" name="paymentMethodCd" value="PAYMENT_METHOD_CD.CARD" class="inp_radio" onclick="javascript:paymentChg(this)" checked>신용카드</label></li>
													<li><label><input type="radio" name="paymentMethodCd" value="PAYMENT_METHOD_CD.VIRTUAL" class="inp_radio" onclick="javascript:paymentChg(this)">무통장입금</label></li>
													<li><label><input type="radio" name="paymentMethodCd" value="PAYMENT_METHOD_CD.TRANSFER" class="inp_radio" onclick="javascript:paymentChg(this)">실시간 계좌이체</label></li>
													<li><label><input type="radio" name="paymentMethodCd" value="PAYMENT_METHOD_CD.MOBILE" class="inp_radio" onclick="javascript:paymentChg(this)">휴대폰결제</label></li>
													<li><label class="kakaopay"><input type="radio" name="paymentMethodCd" value="PAYMENT_METHOD_CD.KAKAO" class="inp_radio" onclick="javascript:paymentChg(this)"><em>kakaopay</em></label></li>
<!-- 													<li><label class="payco"><input type="radio" name="paymentMethodCd" value="PAYMENT_METHOD_CD.PAYCO" class="inp_radio" onclick="javascript:paymentChg(this)"><em>PAYCO</em></label></li> -->
												</ul>
											</div>
										</div>
									</li>
								</ul>
								<!-- 카드 -->
								<form id="pgCardForm">
								<ul class="rw_tb_tbody2 bd_none">
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">카드종류</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<div class="sel_box select_style1">
														<label for="slc_card"></label>
														<select id="pg_LGD_CARDTYPE">
															<option value="XX">선택</option>  
									                		<option value="11">국민</option>
															<option value="21">외환</option>
															<option value="31">비씨</option>
															<option value="41">신한</option>
															<option value="51">삼성</option>
															<option value="61">현대</option>
															<option value="71">롯데</option>
															<option value="91">NH</option>
															<option value="36">씨티</option>
															<option value="32">하나</option>
															<option value="33">우리</option>
															<option value="42">제주</option>
															<option value="34">수협</option>
															<option value="35">전북</option>
															<option value="46">광주</option>
															<option value="37">우체국체크</option>
															<option value="38">MG새마을체크</option>
															<option value="39">저축은행체크</option>
															<option value="62">신협체크</option>
<!-- 															<option value="29">산은캐피탈</option> -->
<!-- 															<option value="37">우체국체크</option> -->
<!-- 															<option value="38">새마을금고체크</option> -->
<!-- 															<option value="62">신협체크</option> -->
<!-- 															<option value="4V">해외VISA</option> -->
<!-- 															<option value="4M">해외MASTER</option> -->
<!-- 															<option value="4J">해외JCB</option> -->
<!-- 															<option value="6D">해외DINERS CLUB</option> -->
														</select>
													</div>
												</div>
											</div>
										</div>
									</li>
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">할부개월</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<div class="sel_box select_style1">
														<label for="slc_card2"></label>
														<select id="pg_LGD_INSTALL">
															<option value="0">일시불</option>
															<option value="2">2개월</option>
															<option value="3">3개월</option>
															<option value="4">4개월</option>
															<option value="5">5개월</option>
															<option value="6">6개월</option>
															<option value="7">7개월</option>
															<option value="8">8개월</option>
															<option value="9">9개월</option>
															<option value="10">10개월</option>
															<option value="11">11개월</option>
															<option value="12">12개월</option>
														</select>
													</div>
												</div>
											</div>
										</div>
									</li>
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">간편결제사용여부</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<div class="sel_box select_style1">
														<label for="slc_card2"></label>
														<select id="pg_LGD_SP_CHAIN_CODE">
															<option value="0">사용안함</option>
															<option value="1">사용함</option>
															<option value="3">국민카드 앱카드바로사용</option>
															<option value="4">국민카드 앱카드선택사용</option>
														</select>
													</div>
												</div>
											</div>
										</div>
									</li>
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">포인트사용여부</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<div class="sel_box select_style1">
														<label for="slc_card2"></label>
														<select id="pg_LGD_POINTUSE">
															<option value="Y">사용함</option>
															<option value="N" selected="selected">사용안함</option>
														</select>
													</div>
												</div>
											</div>
										</div>
									</li>
								</ul>
								</form>
								<form id="pgVirtualForm" style="display: none;">
								<!-- 무통장입금 -->
								<ul class="rw_tb_tbody2 bd_none">
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">입금은행</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<div class="sel_box select_style1">
														<label for="slc_card"></label>
														<select id="pg_LGD_BANKCODE">
															<option value="">원하시는 은행 선택</option>													
															<option value="03">기업</option>												
															<option value="05">외환</option>													
															<option value="06">국민</option>												
															<option value="11">농협</option>	
															<option value="07">수협</option>												
															<option value="81">하나</option>												
															<option value="20">우리</option>												
															<option value="26">신한</option>
															<option value="39">경남</option>
															<option value="71">우체국</option>
															<option value="32">부산</option>
															<option value="31">대구</option>
														</select>
													</div>
												</div>
											</div>
										</div>
									</li>
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">현금영수증 발행여부</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<div class="sel_box select_style1">
														<label for="slc_card"></label>
														<select id="pg_LGD_CASHRECEIPTUSE" onchange="javascript:receiptUseChg($(this))">
															<option value="0">미발행</option>
															<option value="1">소득공제</option>
															<option value="2">지출증빙</option>
														</select>
													</div>
												</div>
											</div>
										</div>
									</li>
									<li id="receiptUseInfo" style="display: none;">
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">현금영수증발급번호(현금영수증카드번호,주민번호,휴대폰번호)</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<input type="text" id="pg_LGD_CASHCARDNUM" value="" class="input_style2">
												</div>
											</div>
										</div>
									</li>
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">에스크로 사용여부</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<div class="sel_box select_style1">
														<label for="slc_card"></label>
														<select id="pg_LGD_ESCROW_USEYN">
															<option value="N">미사용</option>
															<option value="Y">사용</option>															
														</select>
													</div>
												</div>
											</div>
										</div>
									</li>									
								</ul>
								</form>
								<form id="pgTransferForm">
								<ul class="rw_tb_tbody2 bd_none">
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">현금영수증 발행여부</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<div class="sel_box select_style1">
														<label for="slc_card"></label>
														<select id="pg_LGD_CASHRECEIPTYN">
															<option value="N">미발행</option>
															<option value="Y">발행</option>															
														</select>
													</div>
												</div>
											</div>
										</div>
									</li>
								</ul>									
								</form>
								<div class="chkAgree">
									<label><input type="checkbox" value="" class="inp_chk">지금 선택하신 결제수단을 다음에도 사용</label>
								</div>
								<div class="cartBenefit" id="cardInfo">
									<dl class="txt">
										<dt>무이자 카드 혜택 안내</dt>
										<dd>
											부문무이자의 경우 아래와 같이 일부 수수료는 고객님이 부담하게 됩니다.<br />
											4~6개월 부문무이자: 1회차 수수료 고객 부담<br />
											9,10개월:1,2회차 수수료 고객 부담<br />
											12개월: 1,2,3회차 수수료 고객 부담
										</dd>
									</dl>
									<ul class="notice">
										<li>삼성카드  / 5만원 이상 / 부문무이자 (6/10/12개월)</li>
										<li>롯데카드 /  5만원 이상 / 부문무이자 (6/10개월)</li>
										<li>국민카드 / 5만원 이상 / 부문무이자 (6/10개월)</li>
										<li>하나(BC제외) / 5만원 이상 / 부문무이자 (6/10개월)</li>
									</ul>
									<div class="txt">무이자 혜택은 쿠폰 및 포인트 결제를 제외한 최종결제금액이 기준금액 미만이거나, 무이자가 적용되지 않는 카드로 결제시 적용되지 않습니다.</div>
								</div>	
							</div>
						</div>
						<!-- ### //결제수단 ### -->
					</div>
				</div>
				<!-- ### //orderInfoL ### -->
				
				<!-- ### orderInfoR ### -->
				<div class="orderInfoR">
					<c:if test="${memberYn == 'N' }">
					<div class="personalBox">
						<dl>
							<dt>비회원 구매 개인정보취급방침</dt>
							<dd class="personalH">
								<div>
									수집하는 개인정보 항목<br>
									- 성명, 주문비밀번호, 전화번호, 휴대폰 <br>
									번호, e-mail, 주소<br>
									- 결제수단 선택에 따라 신용카드 번호, <br>
									은행계좌번호, 환불계좌번호<br><br>

									수집이용목적<br>
									- 성명, 주문비밀번호, 전화번호, 휴대폰 <br>
									번호<br>
								</div>
							</dd>
						</dl>
						<div class="chkAgree"><label><input type="checkbox" name="agreeCheck" value="비회원 구매 개인정보취급방침" class="inp_chk">동의합니다.</label></div>
					</div>
					비회원 주문 비밀번호 <input type="text" id="nonMemberOrderPwd" value=""/>
					</c:if>
					<div class="payDetail">
						<ul class="payDetail_01">
							<li>
								<div class="left">상품금액</div>
								<div class="right"><em class="price" id="orgTotalSalePriceTxt">0</em>원</div>
							</li>
							<li>
								<div class="left">할인금액</div>
								<div class="right"><em class="discount" id="totalDcAmtTxt">0</em>원</div>
							</li>
							<li>
								<div class="left">배송비</div>
								<div class="right" id="totalDeliveryFeeTxt">0원</div>
							</li>
							<li>
								<div class="left">선물포장비</div>
								<div class="right" id="totalWrapFeeTxt">0원</div>
							</li>
						</ul>
						<c:if test="${memberYn == 'Y' }">
						<ul class="payDetail_02">
							<li>
								<div class="left">상품할인쿠폰</div>
								<div class="right" id="productCouponAmtTxt">0원</div>
							</li>
							<li>
								<div class="left">주문할인쿠폰</div>
								<div class="right" id="orderCouponAmtTxt">0원</div>
							</li>
							<li>
								<div class="left">배송비무료쿠폰</div>
								<div class="right" id="deliveryCouponAmtTxt">0원</div>
							</li>
							<li>
								<div class="left">선물포장무료쿠폰</div>
								<div class="right" id="wrapCouponAmtTxt">0원</div>
							</li>
							<li>
								<div class="left">매일 포인트</div>
								<div class="right" id="pointAmtTxt">0P</div>
							</li>
							<li>
								<div class="left">예치금</div>
								<div class="right" id="depositAmtTxt">0원</div>
							</li>
<!-- 							<li> -->
<!-- 								<div class="left">모바일상품권</div> -->
<!-- 								<div class="right" id="giftAmtTxt">0원</div> -->
<!-- 							</li> -->
						</ul>
						</c:if>
						<dl class="payTotal">
							<dt>총 결제예정금액</dt>
							<dd><em id="totalOrderAmtTxt">0</em>원</dd>
						</dl>
					</div>

					<div class="personalBox2">
						<div class="agreeCont">
							주문할 상품의 상품명, 상품가격, 배송
							정보를 확인하였으며, 구매에 동의하시
							겠습니까? (전자상거래법 제 8조 2항)
						</div>
						<div class="chkAgree"><label><input type="checkbox" name="agreeCheck" value="주문정보 확인" class="inp_chk">동의합니다.</label></div>
					</div>
					<div class="btnBox">
						<a href="#none" class="btn_style purple btnPayment" onclick="javascript:pay()"><span>결제하기</span></a>
					</div>
				</div>
				<!-- ### //orderInfoR ### -->

			</div>
		</div>
	</div>
</div>

<c:set var="productIdx" value="0"/>
<c:set var="plusIdx" value="0"/>
<c:set var="orderIdx" value="0"/>
<c:set var="deliveryIdx" value="0"/>
<c:set var="wrapIdx" value="0"/>
<c:forEach items="${omsOrder.omsOrderproducts }" var="os">
	<c:if test="${os.omsOrdercoupons.size() > 0 }">
		<c:forEach items="${os.omsOrdercoupons }" var="oc">
			<c:if test="${oc.couponTypeCd == 'COUPON_TYPE_CD.PRODUCT' }">
				<form name="couponForm_product" id="couponForm_product_${oc.couponId }_${oc.couponIssueNo }">
					<input type="hidden" name="couponId" id="couponId" value="${oc.couponId }"/>
					<input type="hidden" name="couponIssueNo" id="couponIssueNo" value="${oc.couponIssueNo }"/>
					<input type="hidden" name="name" id="name" value="${oc.name }"/>
					<input type="hidden" name="dcApplyTypeCd" id="dcApplyTypeCd" value="${oc.dcApplyTypeCd }"/>
					<input type="hidden" name="dcValue" id="dcValue" value="${oc.dcValue }"/>
					<input type="hidden" name="maxDcAmt" id="maxDcAmt" value="${oc.maxDcAmt }"/>
					<input type="hidden" name="minOrderAmt" id="minOrderAmt" value="${oc.minOrderAmt }"/>
				</form>
				<c:set var="productIdx" value="${productIdx+1 }"/>
			</c:if>
			<c:if test="${oc.couponTypeCd == 'COUPON_TYPE_CD.PLUS' }">
				<form name="couponForm_plus" id="couponForm_plus_${oc.couponId }_${oc.couponIssueNo }">
					<input type="hidden" name="couponId" id="couponId" value="${oc.couponId }"/>
					<input type="hidden" name="couponIssueNo" id="couponIssueNo" value="${oc.couponIssueNo }"/>
					<input type="hidden" name="name" id="name" value="${oc.name }"/>
					<input type="hidden" name="dcApplyTypeCd" id="dcApplyTypeCd" value="${oc.dcApplyTypeCd }"/>
					<input type="hidden" name="dcValue" id="dcValue" value="${oc.dcValue }"/>
					<input type="hidden" name="maxDcAmt" id="maxDcAmt" value="${oc.maxDcAmt }"/>
					<input type="hidden" name="minOrderAmt" id="minOrderAmt" value="${oc.minOrderAmt }"/>
				</form>
				<c:set var="plusIdx" value="${plusIdx+1 }"/>
			</c:if>
		</c:forEach>
	</c:if>
	<c:if test="${os.optimalProductCoupon != null }">
		<form name="couponForm_optimalProduct" id="couponForm_optimalProduct${os.orderProductNo }" style="display: none;">
			<input type="text" name="orderProductNo" id="orderProductNo" value="${os.orderProductNo }"/>
			<input type="text" name="couponId" id="couponId" value="${os.optimalProductCoupon.couponId }"/>
			<input type="text" name="couponIssueNo" id="couponIssueNo" value="${os.optimalProductCoupon.couponIssueNo  }"/>
		</form>
	</c:if>
	<c:if test="${os.optimalPlusCoupon != null }">
		<form name="couponForm_optimalPlus" id="couponForm_optimalPlus${os.orderProductNo }" style="display: none;">
			<input type="text" name="orderProductNo" id="orderProductNo" value="${os.orderProductNo }"/>
			<input type="text" name="couponId" id="couponId" value="${os.optimalPlusCoupon.couponId }"/>
			<input type="text" name="couponIssueNo" id="couponIssueNo" value="${os.optimalPlusCoupon.couponIssueNo  }"/>
		</form>
	</c:if>
</c:forEach>
<c:if test="${omsOrder.optimalCoupon != null }">
	<form name="couponForm_optimalOrder" id="couponForm_optimalOrder" style="display: none;">		
		<input type="text" name="couponId" id="couponId" value="${omsOrder.optimalCoupon.couponId }"/>
		<input type="text" name="couponIssueNo" id="couponIssueNo" value="${omsOrder.optimalCoupon.couponIssueNo  }"/>
	</form>
</c:if>
<c:forEach items="${omsOrder.omsOrdercoupons }" var="oc" varStatus="status">
	<c:if test="${oc.couponTypeCd == 'COUPON_TYPE_CD.ORDER' }">
	<form name="couponForm_order" id="couponForm_order_${oc.couponId }_${oc.couponIssueNo }">
		<input type="hidden" name="couponId" id="couponId" value="${oc.couponId }"/>
		<input type="hidden" name="couponIssueNo" id="couponIssueNo" value="${oc.couponIssueNo }"/>
		<input type="hidden" name="name" id="name" value="${oc.name }"/>
		<input type="hidden" name="dcApplyTypeCd" id="dcApplyTypeCd" value="${oc.dcApplyTypeCd }"/>
		<input type="hidden" name="dcValue" id="dcValue" value="${oc.dcValue }"/>
		<input type="hidden" name="maxDcAmt" id="maxDcAmt" value="${oc.maxDcAmt }"/>
		<input type="hidden" name="minOrderAmt" id="minOrderAmt" value="${oc.minOrderAmt }"/>
		<c:forEach items="${oc.omsOrderproducts }" var="oap">
			<input type="hidden" name="productId" value="${oap.productId }"/>
		</c:forEach>
	</form>
	<c:set var="orderIdx" value="${orderIdx+1 }"/>	
	</c:if>
	<c:if test="${oc.couponTypeCd == 'COUPON_TYPE_CD.DELIVERY' }">
	<form name="couponForm_delivery" id="couponForm_delivery_${oc.couponId }_${oc.couponIssueNo }">
		<input type="hidden" name="couponId" id="couponId" value="${oc.couponId }"/>
		<input type="hidden" name="couponIssueNo" id="couponIssueNo" value="${oc.couponIssueNo }"/>
		<input type="hidden" name="name" id="name" value="${oc.name }"/>
		<input type="hidden" name="dcApplyTypeCd" id="dcApplyTypeCd" value="${oc.dcApplyTypeCd }"/>
		<input type="hidden" name="dcValue" id="dcValue" value="${oc.dcValue }"/>
		<input type="hidden" name="maxDcAmt" id="maxDcAmt" value="${oc.maxDcAmt }"/>
		<input type="hidden" name="minOrderAmt" id="minOrderAmt" value="${oc.minOrderAmt }"/>
	</form>		
	<c:set var="deliveryIdx" value="${deliveryIdx+1 }"/>
	</c:if>
	<c:if test="${oc.couponTypeCd == 'COUPON_TYPE_CD.WRAP' }">
	<form name="couponForm_wrap" id="couponForm_wrap_${oc.couponId }_${oc.couponIssueNo }">
		<input type="hidden" name="couponId" id="couponId" value="${oc.couponId }"/>
		<input type="hidden" name="couponIssueNo" id="couponIssueNo" value="${oc.couponIssueNo }"/>
		<input type="hidden" name="name" id="name" value="${oc.name }"/>
		<input type="hidden" name="dcApplyTypeCd" id="dcApplyTypeCd" value="${oc.dcApplyTypeCd }"/>
		<input type="hidden" name="dcValue" id="dcValue" value="${oc.dcValue }"/>
		<input type="hidden" name="maxDcAmt" id="maxDcAmt" value="${oc.maxDcAmt }"/>
		<input type="hidden" name="minOrderAmt" id="minOrderAmt" value="${oc.minOrderAmt }"/>
	</form>
	<c:set var="wrapIdx" value="${wrapIdx+1 }"/>
	</c:if>
</c:forEach>
<input type="hidden" id="productCouponCnt" value="${productIdx }"/>
<input type="hidden" id="plusCouponCnt" value="${plusIdx }"/>		
<input type="hidden" id="orderCouponCnt" value="${orderIdx }"/>
<input type="hidden" id="deliveryCouponCnt" value="${deliveryIdx }"/>
<input type="hidden" id="wrapCouponCnt" value="${wrapIdx }"/>
<input type="hidden" id="totalCouponCnt" value=""/>

<!-- ### 쿠폰조회 팝업 ### -->
<div class="pop_wrap ly_coupon" id="couponDiv" style="display:none;">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">쿠폰조회</h3>
		</div>
		<form id="couponPopupForm">
		<div class="pop_content" style="overflow-x: none;overflow-y: scroll; height: 650px">
			<ul class="notice">
				<li>1개의 쿠폰을 여러 상품에 중복 적용할 수 없습니다.</li>
				<li>이미 사용한 쿠폰, 사용기간이 지난 쿠폰은 출력되지 않습니다.</li>
				<li>한 개의 주문에 여러 상품이 있을 경우, 적용된 할인쿠폰과 포인트는 분할 적용됩니다.</li>
			</ul>
			<c:if test="${productIdx > 0 || plusIdx > 0 }">
			<div class="order_tbl coupon">
				<!-- ### 테이블 헤더 ### -->
				<div class="cart_tb_thead1">
					<span class="col1">상품할인 쿠폰</span>
				</div>
				<!-- ### //테이블 헤더 ### -->
				
				<!-- ### 테이블 바디 ### -->
				<ul class="cart_tb_tbody1">
					<c:forEach items="${omsOrder.omsOrderproducts }" var="os">
					<c:if test="${os.omsOrdercoupons.size() > 0 }">
					<input type="hidden" name="couponOrderProductNo" value=${os.orderProductNo } />
					<input type="hidden" name="productDcAmt" id="productDcAmt${os.orderProductNo }" value="0"/>
					<input type="hidden" name="plusDcAmt" id="plusDcAmt${os.orderProductNo }" value="0"/>
					<input type="hidden" name="applyproductCouponId" id="applyproductCouponId${os.orderProductNo }" value=""/>
					<input type="hidden" name="applyproductCouponIssueNo" id="applyproductCouponIssueNo${os.orderProductNo }" value=""/>
					<input type="hidden" name="applyplusCouponId" id="applyplusCouponId${os.orderProductNo }" value=""/>
					<input type="hidden" name="applyplusCouponIssueNo" id="applyplusCouponIssueNo${os.orderProductNo }" value=""/>
					<li>
						<div class="tr_box">
							<div class="td_box col1 td_prdInfo">
								<div class="prod_img">
									<a href="#none">
										<img src="/resources/img/pc/temp/cart_img1.jpg" alt="" />
									</a>
								</div>

								<a href="#none" class="title">
									${os.productName }
								</a>
								
								<c:choose>
								<c:when test="${os.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SET' }">
									<c:forEach var="oss" items="${os.omsOrderproducts }">
									<em>
										<i>옵션 : ${oss.saleproductName }</i>
									</em>
									</c:forEach>
								</c:when>
								<c:otherwise>
									<em>
										<i>옵션 : ${os.saleproductName }</i>
									</em>
								</c:otherwise>
								</c:choose>
								
								<div class="quantity">
									<span class="price"><fmt:formatNumber value="${(os.salePrice + os.addSalePrice) * os.orderQty }" pattern="#,###"/>원</span>
								</div>
							</div>

							<div class="td_box col2">
								<ul class="couponList">
									<li>
										<strong>상품 쿠폰</strong>
										<div class="sel_box select_style1">
											<label for="slc_coupon"></label>
											<select id="product${os.orderProductNo }" onchange="javascript:couponChgPr('product','${os.orderProductNo }')">
												<option value="" selected="">선택</option>
												<c:forEach items="${os.omsOrdercoupons }" var="osc">
												<c:if test="${osc.couponTypeCd == 'COUPON_TYPE_CD.PRODUCT' }">
												<option value="${osc.couponId }_${osc.couponIssueNo }">${osc.name } ${osc.couponDcAmt }</option>
								<%-- 					${osc.couponTypeCd } --%>
								<%-- 					${osc.dcApplyTypeCd } --%>
								<%-- 					${osc.dcValue } --%>
								<%-- 					${osc.maxDcAmt } --%>
								<%-- 					${osc.minOrderAmt } --%>
												</c:if>
												</c:forEach>
											</select>
										</div>
									</li>
									<li>
										<strong>플러스쿠폰</strong>
										<div class="sel_box select_style1">
											<label for="slc_coupon2"></label>
											<select id="plus${os.orderProductNo }" onchange="javascript:couponChgPr('plus','${os.orderProductNo }')">
												<option value="" selected="">선택</option>
												<c:forEach items="${os.omsOrdercoupons }" var="osc">
												<c:if test="${osc.couponTypeCd == 'COUPON_TYPE_CD.PLUS' }">
												<option value="${osc.couponId }_${osc.couponIssueNo }">${osc.name } ${osc.couponDcAmt }</option>
								<%-- 					${osc.couponTypeCd } --%>
								<%-- 					${osc.dcApplyTypeCd } --%>
								<%-- 					${osc.dcValue } --%>
								<%-- 					${osc.maxDcAmt } --%>
								<%-- 					${osc.minOrderAmt } --%>
												</c:if>
												</c:forEach>
											</select>
										</div>
									</li>
								</ul>
							</div>
						</div>
					</li>
					</c:if>
					</c:forEach>
				</ul>
				<!-- ### //테이블 바디 ### -->
				<div class="totalBox2">
					<strong>할인금액</strong>					
					<em id="productTotalCouponDcAmtTxt">0원</em>
				</div>
			</div>
			</c:if>

			<c:if test="${orderIdx > 0 }">
			<input type="hidden" name="orderDcAmt" id="orderDcAmt" value="0"/>
			<input type="hidden" name="applyorderCouponId" id="applyorderCouponId" value=""/>
			<input type="hidden" name="applyorderCouponIssueNo" id="applyorderCouponIssueNo" value=""/>
			<div class="coupon_tbl cart">
				<dl>
					<dt>장바구니 쿠폰</dt>
					<dd>
						<ul>
							<li>
								<div class="coupon">
									<div class="sel_box select_style1">
										<label for="slc_coupon3"></label>
										<select id="orderCoupon" onchange="javascript:couponChgOrder()">
											<option value="">선택</option>
											<c:forEach items="${omsOrder.omsOrdercoupons }" var="oc">
												<c:if test="${oc.couponTypeCd == 'COUPON_TYPE_CD.ORDER' }">
												<option value="${oc.couponId }_${oc.couponIssueNo }">${oc.name } ${oc.couponDcAmt }</option>
									<%-- 			${oc.couponTypeCd } --%>
									<%-- 			${oc.dcApplyTypeCd } --%>
									<%-- 			${oc.dcValue } --%>
									<%-- 			${oc.maxDcAmt } --%>
									<%-- 			${oc.minOrderAmt } --%>
												</c:if>
											</c:forEach>
										</select>
									</div>
								</div>
							</li>
						</ul>
					</dd>
				</dl>
				<div class="totalBox2">
					<strong>할인금액</strong>					
					<em id="orderTotalCouponDcAmtTxt">0원</em>
				</div>
			</div>
			</c:if>
			
			<c:if test="${deliveryIdx > 0 }">
			<div class="coupon_tbl" id="deliveryCouponDiv"></div>
			</c:if>

			<c:if test="${wrapIdx > 0 }">
			<div class="coupon_tbl" id="wrapCouponDiv"></div>
			</c:if>

			<div class="totalBox">
				<div class="left">
					<strong>선택한 쿠폰</strong>
					<em id="applyTotalCouponCnt">0개</em>
				</div>
				<div class="right">
					<strong>할인금액</strong>
					<em id="applyTotalCouponDcAmtTxt">0원</em>
				</div>
			</div>

			<div class="btn_box">
				<a href="#none" class="btn_style white" onclick="javascript:couponClose()"><span>취소</span></a>
				<a href="#none" class="btn_style purple" onclick="javascript:couponConfirm()"><span>신청</span></a>
			</div>
		</div>
		</form>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>
<!-- ### //쿠폰조회 팝업 ### -->
<!-- ### 선물포장 팝업 ### -->
<div class="pop_wrap ly_gift_packing ly_order_search" id="wrapDiv" style="display:none;">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">선물포장</h3>
		</div>
		<div class="pop_content">
			<ul class="notice">
				<li>선물포장은 상품 <span class="emph">1건당 1,000원</span>의 비용이 책정됩니다.</li>
				<li>부피에 따라 상품 2개를 포장 1개로 진행할 수 있습니다.</li>
			</ul>
			<div class="giftPackingBox" id="deliveryDiv">
			</div>
			<div class="btn_box">
				<a href="#none" class="btn_style white" onclick="javascript:wrapClose()"><span>취소</span></a>
				<a href="#none" class="btn_style purple" onclick="javascript:wrapConfirm()"><span>신청</span></a>
			</div>
		</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>
<!-- ### //선물포장 팝업 ### -->


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
<script language="javascript" src="http://xpay.lgdacom.net/xpay/js/xpay_crossplatform.js" type="text/javascript" charset="utf-8"></script><!-- crossplatform -->
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
var pgData = function(){
	
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
	
// 	var totalOrderAmt = $("#saveOrderForm").find("#totalOrderAmt").val();
// 	$("#paymentForm0").find("#paymentAmt").val(totalOrderAmt);	//결제금액
	
// 	common.mergeForms("mergeForm","paymentForm0");

// 	console.log("pgData payForm : ");
// 	console.log(payForm);
}
//////////////////////////////////////// 자체창 /////////////////////////////////////////////

//////////////////////////////////////// 자체창 /////////////////////////////////////////////

//////////////////////////////////////// crossplatform //////////////////////////////////////////
/*
* iframe으로 결제창을 호출하시기를 원하시면 iframe으로 설정 (변수명 수정 불가)
*/
var LGD_window_type = '';
var CST_PLATFORM = '';
/*
* 수정불가
*/
function launchCrossPlatform(){
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
// 			console.log("PG return param success!!!!!!!");
// 			console.log(resultForm);						
			
			var orderIds = resultForm.find("#LGD_OID").val().split("_");
			order(orderIds[0]);
	
	} else {
		alert("LGD_RESPCODE (결과코드) : " + fDoc.document.getElementById('LGD_RESPCODE').value + "\n" + "LGD_RESPMSG (결과메시지): " + fDoc.document.getElementById('LGD_RESPMSG').value);
		closeIframe();
	}

}
////////////////////////////////////////crossplatform //////////////////////////////////////////

var pay = function(){
	
	if(formSetting()){
		var paymentMethodCd = $("input[name=paymentMethodCd]:checked").val();
				
		if(paymentMethodCd == 'PAYMENT_METHOD_CD.CARD' || paymentMethodCd == 'PAYMENT_METHOD_CD.VIRTUAL'
		|| paymentMethodCd == 'PAYMENT_METHOD_CD.TRANSFER' || paymentMethodCd == 'PAYMENT_METHOD_CD.MOBILE'){
			pay_LG(paymentMethodCd);
		}else if(paymentMethodCd == 'PAYMENT_METHOD_CD.KAKAO'){
			pay_KAKAO();
		}else if(paymentMethodCd == 'PAYMENT_METHOD_CD.PAYCO'){
			
		}
	}
		
}

//LG 결제 
var pay_LG = function(paymentMethodCd){
	
	var form = $("#payparam");
	
	var orderFormData = orderInfo();
	var orderProductNos = getOrderproductNos();
	var productInfo = "";
	
	for(var i=0;i<orderProductNos.length;i++){
		if(i == 0){
			productInfo = $("#productForm"+orderProductNos[i].value).find("#productName").val();
		}
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
		
		if(LGD_CARDTYPE == 'XX'){
			alert("카드종류를 선택하세요.");
			return false;
		}
		
		form.find("#LGD_CARDTYPE").val(LGD_CARDTYPE);	
		form.find("#LGD_INSTALL").val(LGD_INSTALL);
		form.find("#LGD_SP_CHAIN_CODE").val(LGD_SP_CHAIN_CODE);
		form.find("#LGD_POINTUSE").val(LGD_POINTUSE);
		
		form.find("#LGD_CUSTOM_PROCESSTYPE").val("AUTHTR");
		form.find("#LGD_SELF_CUSTOM").val("Y");		//자체창 사용여부
		form.find("#LGD_VERSION").val("JSP_Non-ActiveX_CardApp");
	}	
	//계좌이체
	else if(paymentMethodCd == 'PAYMENT_METHOD_CD.TRANSFER'){
		usablepay = "SC0030";
		
		var pgTransferForm = $("#pgTransferForm");
		var LGD_CASHRECEIPTYN = pgTransferForm.find("#pg_LGD_CASHRECEIPTYN option:selected").val();
		
		form.find("#LGD_CASHRECEIPTYN").val(LGD_CASHRECEIPTYN);	//현금영수증발급여부				
		form.find("#LGD_CUSTOM_PROCESSTYPE").val("TWOTR");
		form.find("#LGD_SELF_CUSTOM").val("N");		//자체창 사용여부
		form.find("#LGD_VERSION").val("JSP_Non-ActiveX_Standard");
	}
	//가상계좌
	else if(paymentMethodCd == 'PAYMENT_METHOD_CD.VIRTUAL'){
		usablepay = "SC0040";
		var pgVirtualForm = $("#pgVirtualForm");
		var LGD_BANKCODE = pgVirtualForm.find("#pg_LGD_BANKCODE option:selected").val();
		var LGD_CASHRECEIPTUSE = pgVirtualForm.find("#pg_LGD_CASHRECEIPTUSE option:selected").val();
		var LGD_CASHCARDNUM = pgVirtualForm.find("#pg_LGD_CASHCARDNUM").val();
		var LGD_ACCOUNTOWNER = orderFormData["name1"];	//입금자명
		var LGD_BUYERPHONE = orderFormData["phone2"];	//구매자휴대폰
		var LGD_ESCROW_USEYN = pgVirtualForm.find("#pg_LGD_ESCROW_USEYN option:selected").val();
// 		console.log(LGD_BANKCODE);
		if(common.isEmpty(LGD_BANKCODE)){
			alert("입금은행을 선택하세요.");
			return false;
		}
		
		if(LGD_CASHRECEIPTUSE != "0"){
			if(common.isEmpty(LGD_CASHCARDNUM)){
				alert("현금영수증발급번호를 입력하세요.");
				return false;
			}else{
				LGD_CASHRECEIPTYN = "Y";
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
		form.find("#LGD_VERSION").val("JSP_Non-ActiveX_CardApp");
	}
	//휴대폰
	else if(paymentMethodCd == 'PAYMENT_METHOD_CD.MOBILE'){
		usablepay = "SC0060";
		form.find("#LGD_CUSTOM_PROCESSTYPE").val("TWOTR");
		form.find("#LGD_SELF_CUSTOM").val("N");		//자체창 사용여부
		form.find("#LGD_VERSION").val("JSP_Non-ActiveX_Standard");
	}
	
	form.find("#LGD_BUYER").val(orderFormData["name1"]);
	form.find("#LGD_PRODUCTINFO").val(productInfo);
	form.find("#LGD_AMOUNT").val(totalOrderAmt);
	form.find("#LGD_BUYEREMAIL").val(orderFormData["email"]);
	form.find("#LGD_CUSTOM_USABLEPAY").val(usablepay);	
	
// 	console.log("pay_cross form : ");
// 	console.log(form);
	
	var data = form.serialize();
	
	$.ajax({ 				
		url : "/api/oms/pg/param",
		type : "POST",		
		data : data
	}).done(function(response){
		if(response.RESULT == "SUCCESS"){
			//alert("저장되었습니다.");
// 			console.log(response);
			pay_callback(paymentMethodCd,response.omsPaymentif,response.orderId);	
		}else{
			alert(response.MESSAGE);
		}
		
	});
}

var pay_callback = function(paymentMethodCd,data,orderId){
	
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
		if(common.isNotEmpty(value)){
			form.append($('<input>',{
				'type' : 'hidden',
				'name' : key,
				'id' : key,
				'value' : value
			}))
		}
		
	});
	
	
// 	form.append($('<input>',{
// 		'type' : 'hidden',
// 		'name' : 'LGD_OID',
// 		'id' : 'LGD_OID',
// 		'value' : orderId
// 	}))
	
// 	$("saveOrderForm")
	form.find("#LGD_OID").val(orderId+"_0");	//pg에 넘기는 주문번호는 paymentNo를 붙여서 넘긴다.		
	
// 	console.log("pay_callback form :");
// 	console.log(form);	
	
	//PG DATA 처리
	pgData();
	
	if(paymentMethodCd == 'PAYMENT_METHOD_CD.VIRTUAL'){
		order(orderId);
	}else{
		launchCrossPlatform();
	}
	
}


</script>
<form method="post" name="payparam" id="payparam" style="display: none;">
	<input type="hidden" name="LGD_VERSION" id="LGD_VERSION" value="JSP_Non-ActiveX_Standard"/>
	<input type="hidden" name="LGD_BUYER" id="LGD_BUYER" value=""/>			<!-- 구매자 이름 -->
<!-- 	<input type="text" name="LGD_BUYERID" id="LGD_BUYERID" value=""/>		구매자 ID -->
	<input type="hidden" name="LGD_PRODUCTINFO" id="LGD_PRODUCTINFO" value=""/>	<!-- 상품정보 -->
	<input type="hidden" name="LGD_AMOUNT" id="LGD_AMOUNT" value=""/>			<!-- 결제금액 -->
	<input type="hidden" name="LGD_BUYEREMAIL" id="LGD_BUYEREMAIL" value=""/>		<!-- 구매자 이메일 -->
	<input type="hidden" name="LGD_OID" id="LGD_OID" value=""/>			<!-- 주문번호 -->
	<input type="hidden" name="LGD_CUSTOM_USABLEPAY" id="LGD_CUSTOM_USABLEPAY" value=""/><!-- 초기결제수단 -->
	
	<input type="hidden" name="LGD_CUSTOM_PROCESSTYPE" id="LGD_CUSTOM_PROCESSTYPE" value=""/><!-- 결제 프로세스유형 (자체창 :  AUTHTR, 일반 : TWOTR-->	
	
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
</form>
<form method="post" name="LGD_PAYINFO" id="LGD_PAYINFO"></form>


<!-- KAKAO -->
<%String webPath = "https://kmpay.lgcns.com:8443/"; %>
<!-- OpenSource Library -->
<script src="https://pg.cnspay.co.kr:443/dlp/scripts/lib/easyXDM.min.js" type="text/javascript"></script>
<script src="https://pg.cnspay.co.kr:443/dlp/scripts/lib/json3.min.js" type="text/javascript"></script>

<!-- JQuery에 대한 부분은 site마다 버전이 다를수 있음 -->
<script src="<%=webPath %>/js/dlp/lib/jquery/jquery-1.11.1.min.js" charset="urf-8"></script>

<!-- DLP창에 대한 KaKaoPay Library -->
<script src="<%=webPath %>/js/dlp/client/kakaopayDlpConf.js" charset="utf-8"></script>
<script src="<%=webPath %>/js/dlp/client/kakaopayDlp.min.js" charset="utf-8"></script>

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
            
            kakaopayDlp.setChannelType('WPM', 'TMS'); // PC결제
            //kakaopayDlp.setChannelType('MPM', 'WEB'); // 모바일 웹(브라우저)결제
            //kakaopayDlp.addRequestParams({ MOBILE_NUM : '010-1234-5678'}); // 초기값 세팅
            
            kakaopayDlp.callDlp('kakaopay_layer', document.payForm, submitFunc);
            
        } else {
            alert('[RESULT_CODE] : ' + document.payForm.resultCode.value + '\n[RESULT_MSG] : ' + document.payForm.resultMsg.value);
        }
        
    }
    
    function getTxnId(){
    	document.payForm.acceptCharset = "utf-8";
    	var data = $("#payForm");
    	$.ajax({ 				
    		url : "/api/oms/pg/kakao/txnid",
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
        	var orderIds = $("#payForm").find("#merchantTxnNum").val().split("_");
			order(orderIds[0]);        	
        	
        } else if(data.RESULT_CODE === 'KKP_SER_002') {
            // X버튼 눌렀을때의 이벤트 처리 코드 등록
            alert('[RESULT_CODE] : ' + data.RESULT_CODE + '\n[RESULT_MSG] : ' + data.RESULT_MSG);
        } else {
            alert('[RESULT_CODE] : ' + data.RESULT_CODE + '\n[RESULT_MSG] : ' + data.RESULT_MSG);
        }
        
    };
    
    var orderTest = function(){
    	$.ajax({ 				
    		url : "/api/oms/order/save",
    		type : "POST",		
    		data : $("#mergeForm").serialize()
    	}).done(function(response){
    		if(response.RESULT == "SUCCESS"){
    			//alert("저장되었습니다.");			
    			$("#orderCompleteForm").find("#orderId").val(orderId);
    			$("#orderCompleteForm").submit();					
    		}else{
    			alert(response.MESSAGE);
    		}
    		
    	});
    }
    
</script>
<form name="payForm" id="payForm" method="post" accept-charset = "utf-8" style="display: none;">
<!-- 결제 파라미터 목록 -->
        <b>결제 변수 목록(매뉴얼 참조)</b><br  />
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
        <br />
        
        
        <!-- 인증 파라미터 목록 -->
        <b>인증 변수 목록(매뉴얼 참조)</b><br />
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
            <input type="text" name="prType" value="WPM"/> 
<!--             <select name ="prType"> -->
<!--                 <option value="MPM">MPM</option> -->
<!--                 <option value="WPM" selected="selected">WPM</option> -->
<!--             </select> -->
            <br />MPM : 모바일결제, WPM : PC결제
            </li>
            <li>
            (*)채널타입 :
            <input type="text" name="channelType" value="4"/>  
<!--             <select name ="channelType"> -->
<!--                 <option value="2">2</option> -->
<!--                 <option value="4" selected="selected">4</option> -->
<!--             </select> -->
            <br />2: 모바일결제, 4: PC결제
            </li>
            <li>(*)가맹점 거래번호 : <input type="text" name="merchantTxnNum" id="merchantTxnNum" value="" /></li>
            
        </ul>
        <br />
        
        <!-- 인증 파라미터 중 할부결제시 사용하는 파라미터 목록 -->
        <!-- 파라미터 입력형태는 매뉴얼 참조  -->
        <b>할부결제시 선택변수 목록</b><br />
        - 옳은 값들을 넣지 않으면 무이자를 사용하지 않는것으로 한다.<br />
        
        <b>카드코드(매뉴얼 참조)</b><br />
        - 비씨:01, 국민:02, 외환:03, 삼성:04, 신한:06, 현대:07, 롯데:08, 한미:11, 씨티:11, <br /> 
        NH채움(농협):12, 수협:13, 신협:13, 우리:15, 하나SK:16, 주택:18, 조흥(강원):19, <br />
        광주:21, 전북:22, 제주:23, 해외비자:25, 해외마스터:26, 해외다이너스:27, <br />
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
                    
        
        <br />
        <br />
        <br />
        ======================================================================<br />
        <b>getTxnId 응답</b><br />
        resultcode<input id="resultCode" type="text" value=""/><br/>
        resultmsg<input id="resultMsg" type="text" value=""/><br/>
        txnId<input id="txnId" type="text" value=""/><br/>
        prDt<input id="prDt" type="text" value=""/><br/>
        <br/>
        <br/>
        <!-- DLP호출에 대한 응답 -->
        <b>DLP 응답</b><br />
        SPU : <input type="text" name="SPU" id="SPU" value=""/><br/>
        SPU_SIGN_TOKEN : <input type="text" name="SPU_SIGN_TOKEN" id="SPU_SIGN_TOKEN" value=""/><br/>
        MPAY_PUB : <input type="text" name="MPAY_PUB" id="MPAY_PUB" value=""/><br/>
        NON_REP_TOKEN : <input type="text" name="NON_REP_TOKEN" id="NON_REP_TOKEN" value=""/><br/>
</form>
<!-- TODO :  LayerPopup의 Target DIV 생성 -->
<div id="kakaopay_layer"  style="display: none"></div>
<!-- <div class="btns"> -->
<!--     <a href="javascript:orderTest();">결제 요청하기</a>     -->
<!-- </div> -->