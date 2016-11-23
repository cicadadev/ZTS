<%--
	화면명 : 쿠폰레이어 inner
	작성자 : dennis
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags" %>

<script type="text/javascript">
$(document).ready(function(){
	
	//쿠폰 div 이동
	couponDivMove();
	
	//쿠폰개수
	sumCouponCnt();	
	
	//최적쿠폰 setting
	optimalCouponSet();
	
	//초기 쿠폰 setting
	couponConfirm();
		
})

var couponDivMove = function(){
	if(global.channel.isMobile == "true"){
		var layerP = '<div class="pop_wrap couponDiv" id="couponDiv">'+$("#couponDiv").html()+'</div>';
		$('.wrap').append(layerP);	
		$('.content').find("#couponDiv").remove();
	}
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
				couponSet(false,'product',opn);
			}
			
			var form2 = $("#couponForm_optimalPlus"+opn);
			var plusCouponId = form2.find("#couponId").val();
			var plusCouponIssueNo = form2.find("#couponIssueNo").val();				
			if(common.isNotEmpty(plusCouponIssueNo)){
				couponPopupForm.find("#plus"+opn).val(plusCouponId+"_"+plusCouponIssueNo).prop("selected", true);
				couponSet(false,'plus',opn);
			}
			
		}
		
	}
	
	var orderForm = $("#couponForm_optimalOrder");
	var selOrderCouponId = orderForm.find("#couponId").val();
	var selOrderCouponIssueNo = orderForm.find("#couponIssueNo").val();
	if(common.isNotEmpty(selOrderCouponIssueNo)){
		couponPopupForm.find("#orderCoupon").val(selOrderCouponId+"_"+selOrderCouponIssueNo).prop("selected", true);
		couponSet(false,'order');
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
				couponPopupForm.find("#delivery"+i+"_"+deliveryPolicyNo).val(selDeliveryValue).prop("selected", true);
				couponSet(false,'delivery',i+"_"+deliveryPolicyNo);
			}
			
			var wrapCouponId = deliveryForm.find("#wrapCouponId").val();
			var wrapCouponIssueNo = deliveryForm.find("#wrapCouponIssueNo").val();
			if(common.isNotEmpty(wrapCouponIssueNo)){
				selWrapValue = wrapCouponId+"_"+wrapCouponIssueNo;
			}
			
		}
		
		couponPopupForm.find("#wrap"+i).val(selWrapValue).prop("selected", true);		
		couponSet(false,'wrap',i);
	}
	if(common.isNotEmpty(selOrderCouponIssueNo)){
		couponPopupForm.find("#orderCoupon").val(selOrderCouponId+"_"+selOrderCouponIssueNo).prop("selected", true);
		couponSet(false,'order');
	}else{
		couponPopupForm.find("#orderCoupon").val("").prop("selected", true);
	}
}

//쿠폰 선택 selecbox
var couponSelectBox = function(type,deliveryAddressNo,deliveryPolicyNo){
	var forms = $("form[name=couponForm_"+type+"]");
	var cnt = 0;
	var selectName = "";
	var html = "";
	
// 	console.log("couponSelectBox selCouponIssueNo : "+selCouponIssueNo);

	var id = type+deliveryAddressNo;
	if(common.isNotEmpty(deliveryPolicyNo)){
		id = id + "_" +deliveryPolicyNo;
		deliveryAddressNo = deliveryAddressNo +"_"+deliveryPolicyNo;
	}

	if(common.isNotEmpty(forms) && forms.length > 0){
		    html = '<select name="'+type+'" id="'+id+'" onchange="javascript:couponChg(\''+type+'\',\''+deliveryAddressNo+'\')">';
			html += '<option selected="" value="">선택</option>';
		for(var i=0;i<forms.length;i++){
			var form = $("#"+forms[i].id);
			var value = form.find("#couponId").val();
			var value2 = form.find("#couponIssueNo").val();
			var name = form.find("#name").val();
			
			html += '<option value="'+value+'_'+value2+'">'+name+'</option>';
		}
		html += '</select>';
	}else{
		html = "";
	}
	
	
	return html;
	
}

//쿠폰 template
var couponTempate = function(type){
	var html = "";
	var subHtml = "";
	var target = "";
	var title = "";
	var cnt = getDeliveryCnt();
	var totalCnt = 0;
	
	for(var i=0;i<cnt;i++){
	
		if(type == "delivery"){
			liId = "deliveryli";
			title = "";
			target = "orderDeliveryFee";
			
			var fee = 0;
			var ads;
			
			if(type == 'delivery'){
				var sumDelivery = $("input[name=sumDeliveryPolicyNo]");
				for(var j=0;j<sumDelivery.length;j++){
					var deliveryPolicyNo = sumDelivery[j].value;
					var deliveryFee = 0;
					
					var orderProductNos = getAddrOrderproductNos(i);
					
					title = "";
					var productCnt = 0;
					//할인금액 분배
					for(var k=0;k<orderProductNos.length;k++){			
						var opn = orderProductNos[k].value;
						var productDeliveryPolicyNo = $("#addressForm"+i).find("#deliveryPolicyNo"+orderProductNos[k].value).val();
						if(productDeliveryPolicyNo == deliveryPolicyNo){
							var productDeliveryFee = $("#addressForm"+i).find("#orderDeliveryFee"+opn).val();
							var productName = $("#productForm"+opn).find("#productName").val();
							deliveryFee += Number(productDeliveryFee);
							if(productCnt == 0){
								title = productName;
							}
							
							productCnt++;
						}
					}
						
					if(productCnt > 1){
						title = title + " 외" +  (Number(productCnt) -1);
					}
// 					console.log("deliveryFee",deliveryFee);
					
					if(deliveryFee > 0){
						var id = i+"_"+deliveryPolicyNo;
						var selectHtml = couponSelectBox(type,id);
						if(selectHtml != ""){
								subHtml += '\
									<li>\
										<div class="left">\
											<span class="dot">'+title+'</span>\
										</div>\
										<div class="right">\
											<div class="select_box1">\
												<label></label>\
												'+selectHtml+'\
											</div>\
											<input type="hidden" name="'+type+'DcAmt" id="'+type+'DcAmt'+id+'" value="0"/>\
											<input type="hidden" name="apply'+type+'CouponId" id="apply'+type+'CouponId'+id+'" value=""/>\
											<input type="hidden" name="apply'+type+'CouponIssueNo" id="apply'+type+'CouponIssueNo'+id+'" value=""/>\
											<input type="hidden" name="temp'+type+'DcAmt" id="temp'+type+'DcAmt'+id+'" value="0"/>\
											<input type="hidden" name="tempapply'+type+'CouponId" id="tempapply'+type+'CouponId'+id+'" value=""/>\
											<input type="hidden" name="tempapply'+type+'CouponIssueNo" id="tempapply'+type+'CouponIssueNo'+id+'" value=""/>\
										</div>\
									</li>\
										';
				// 						<div class="spot">배송지'+(i+1)+'</div>\
				// 						<div class="price">'+common.priceFormat(fee,true,true)+'</div>\
							totalCnt++;
						}
					}
					
				}
			}
			if(totalCnt > 0){
				html = subHtml;
// 				html += '\
// 						<ul class="rw_tb_tbody2">\
// 							<li>\
// 								<div class="tr_box w100">\
// 									<div class="col1">\
// 										<span class="group_inline">배송무료 쿠폰</span>\
// 		// 								<span class="group_inline">'+title+'</span>\
// 		// 								<span class="group_inline sale_pay">할인금액  <strong id="'+type+'TotalCouponDcAmtTxt'+id+'">0</strong>원</span>\
// 									</div>\
// 									<div class="col2">\
// 										<div class="group_block">\
// 											<ul class="couponList">\
// 										'+subHtml+'\
// 											</ul>\
// 										</div>\
// 									</div>\
// 								</div>\
// 							</li>\
// 						</ul>\
// 					';
			}
//		 				<div class="totalBox2" style="display:none;">\
//		 					<strong>할인금액</strong>\
//		 					<em id="'+type+'TotalCouponDcAmtTxt">0원</em>\
//		 				</div>\
			
			totalCnt = 0;
			
		}else if(type == "wrap"){
			liId = "wrapli";
			title = "선물포장 무료쿠폰";
			target = "orderWrapFee";
			
			var fee = 0;
			var ads;
			
			if(type == 'wrap'){
				var sumDelivery = $("input[name=sumDeliveryPolicyNo]");
				for(var j=0;j<sumDelivery.length;j++){
					var wrapFee = $("#deliveryForm_"+i+"_"+sumDelivery[j].value).find("#orderWrapFee").val();
					fee += Number(wrapFee);
				}
			}
			
			if(fee > 0){
				var selectHtml = couponSelectBox(type,i);
				if(selectHtml != ""){
					subHtml += '\
								<div class="select_box1">\
									<label></label>\
									'+selectHtml+'\
								</div>\
								<input type="hidden" name="'+type+'DcAmt" id="'+type+'DcAmt'+i+'" value="0"/>\
								<input type="hidden" name="apply'+type+'CouponId" id="apply'+type+'CouponId'+i+'" value=""/>\
								<input type="hidden" name="apply'+type+'CouponIssueNo" id="apply'+type+'CouponIssueNo'+i+'" value=""/>\
								<input type="hidden" name="temp'+type+'DcAmt" id="temp'+type+'DcAmt'+i+'" value="0"/>\
								<input type="hidden" name="tempapply'+type+'CouponId" id="tempapply'+type+'CouponId'+i+'" value=""/>\
								<input type="hidden" name="tempapply'+type+'CouponIssueNo" id="tempapply'+type+'CouponIssueNo'+i+'" value=""/>\
								';
		// 						<div class="spot">배송지'+(i+1)+'</div>\
		// 						<div class="price">'+common.priceFormat(fee,true,true)+'</div>\
					totalCnt++;
				}
			}
		}			
	}
	
	
	if(totalCnt > 0 && type == "wrap"){

		html = '\
				<ul class="rw_tb_tbody2">\
					<li id="'+liId+'">\
						<div class="tr_box">\
							<div class="col1">\
								<span class="group_inline">'+title+'</span>\
								<span class="group_inline sale_pay">할인금액  <strong id="'+type+'TotalCouponDcAmtTxt">0</strong>원</span>\
							</div>\
							<div class="col2">\
								<div class="group_block">\
								'+subHtml+'\
								</div>\
							</div>\
						</div>\
					</li>\
				</ul>\
				';
// 				<div class="totalBox2" style="display:none;">\
// 					<strong>할인금액</strong>\
// 					<em id="'+type+'TotalCouponDcAmtTxt">0원</em>\
// 				</div>\
	}
	
	
	
		
	return html;
}
//쿠폰 div
var couponSelect = function(){	
	
// 	var totalCouponCnt = $("#totalCouponCnt").val();
	var applyCouponCnt = $("#applyCouponCnt").val();
	
	if(Number(applyCouponCnt) > 0){
		var totalDeliveryFee = $("#saveOrderForm").find("#totalDeliveryFee").val();
		$("#couponDiv").find("#deliveryUl").html("");
		if(Number(totalDeliveryFee) > 0){
			$("#couponDiv").find("#deliveryUl").html(couponTempate("delivery"));
		}else{
			$("#couponDiv").find("#deliveryDiv").hide();	
		}
		
		$("#couponDiv").find("#wrapDiv").html("");
		$("#couponDiv").find("#wrapDiv").html(couponTempate("wrap"));
// 		$("#couponDiv #deliveryCouponDiv").html(couponTempate("delivery"));
// 		$("#couponDiv #wrapCouponDiv").html(couponTempate("wrap"));

		if(global.channel.isMobile == "true"){
			//mobile
			$("#couponDiv").show();
			$(".header_mo, .mobile .content, .footer_mo").hide();
			$(window).scrollTop(0);
		}else{
			//pc
			fnPopPosition("#couponDiv");
		}
	
		couponDefaultSelect();
		
		cssChange();				
				
	}else{
		alert("적용가능한 쿠폰이 없습니다.");
		return;
	}
	
}

//쿠폰 닫기
var couponClose = function(){
// 	$("#couponDiv").find("#deliveryUl").html("");
// 	$("#couponDiv").find("#wrapDiv").html("");
	
	if(global.channel.isMobile == "true"){
		//mobile
		$(this).closest(".pop_wrap").hide();
		$(".mobile .content").removeClass("content_fixed").show();
		$(".header_mo, .footer, .footer_mo").show();
		$(".mobile").removeAttr("style");
	}
	
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
				alert("이미 적용된 쿠폰입니다.");
				$("#"+selectId).val("");	
				return false;
			}
		}
		
	}
	
// 	if(type == 'plus'){
// 		var productCouponFormId = $("#product"+orderProductNo).val();	//현재 selectbox value;
// 		if(common.isEmpty(productCouponFormId)){
// 			alert("상품쿠폰이 선택되지 않았습니다.");
// 			$("#"+selectId).val("");
// 			return false;
// 		}
// 	}
	
	couponSet(false,type,orderProductNo);
	
	//상품쿠폰 없을때 plus쿠폰 초기화
// 	if(type == 'product'){
// 		var plusCouponFormId = $("#plus"+orderProductNo).val();	//현재 selectbox value;
// 		if(couponFormId == ""){			
// 			$("#plus"+orderProductNo).val("");
// 			couponSet(false,'plus',orderProductNo);
			
// 			cssChange();
// 		}
// 	}
	
	couponSet(false,'order');
}
//쿠폰변경(delivery,wrap)
var couponChg = function(type,deliveryAddressNo){
	
	var selectId = type+deliveryAddressNo;
	
	var strKey = deliveryAddressNo.split("_");
	
	var couponFormId = $("#"+selectId).val();	//현재 selectbox value;

	var cnt = getDeliveryCnt();
	
	//coupon check (배송지 loop)
	for(var i=0;i<cnt;i++){
		var loopId = type+i;
		
		if(type == "delivery"){						
			var sumDelivery = $("input[name=sumDeliveryPolicyNo]");
			
			for(var j=0;j<sumDelivery.length;j++){
				var deliveryPolicyNo = sumDelivery[j].value;
				var newloopId = loopId + "_" + deliveryPolicyNo;
				
	//	 		console.log($("#couponPopupForm").find("#"+loopId+" option:selected"));
				var value = $("#couponPopupForm").find("#"+newloopId+" option:selected").val();
// 				console.log("value",value);
// 				console.log("couponFormId",couponFormId);
				if(common.isNotEmpty(couponFormId)){
					if(newloopId != selectId && value == couponFormId){
						alert("이미 선택된 쿠폰입니다.");
						$("#"+selectId).val("");	
						return false;
					}
				}
			}
		}else{
//	 		console.log($("#couponPopupForm").find("#"+loopId+" option:selected"));
			var value = $("#couponPopupForm").find("#"+loopId+" option:selected").val();
			
			if(common.isNotEmpty(couponFormId)){
				if(loopId != selectId && value == couponFormId){
					alert("이미 선택된 쿠폰입니다.");
					$("#"+selectId).val("");	
					return false;
				}
			}			
		}				
	}
	
	couponSet(false,type,deliveryAddressNo);
}

var applyTotalCouponCnt = 0;
var applyTotalCouponAmt = 0;

//쿠폰 선택된 금액 sum 계산
var calCouponSubAmt = function(type,confirm){
	var temp = "";
	if(common.isNotEmpty(confirm) && !confirm){
		temp = "temp";
	}
// 	console.log(temp);
	var form = "#couponPopupForm";
	var sumAmt = 0;
	var dcAmt = $(form).find("input[name="+temp+type+"DcAmt]");
	for(var i=0;i<dcAmt.length;i++){
		sumAmt += Number(dcAmt[i].value);
	}
	var apply = $(form).find("input[name="+temp+"apply"+type+"CouponIssueNo]");
	for(var i=0;i<apply.length;i++){
		var couponIssueNo = apply[i].value;		
		if(common.isNotEmpty(couponIssueNo)){
			applyTotalCouponCnt++;
		}
	}
	return sumAmt;
}

var copyTempCoupon = function(){
	copyTempCouponSub("product");
	copyTempCouponSub("plus");
	copyTempCouponSub("order");
	copyTempCouponSub("delivery");
	copyTempCouponSub("wrap");
}

var copyTempCouponSub = function(type){
	var form = "#couponPopupForm";
	var sumAmt = 0;
	var dcAmt = $(form).find("input[name="+type+"DcAmt]");
	var tempdcAmt = $(form).find("input[name=temp"+type+"DcAmt]");
	for(var i=0;i<dcAmt.length;i++){
		dcAmt[i].value = tempdcAmt[i].value; 
	}
	var apply = $(form).find("input[name=apply"+type+"CouponIssueNo]");
	var tempapply = $(form).find("input[name=tempapply"+type+"CouponIssueNo]");
	for(var i=0;i<apply.length;i++){
		apply[i].value = tempapply[i].value;		
	}
}

//쿠폰 정률/정액 계산
var couponDcAmtCalc = function(dcApplyTypeCd,totalOrderAmt, dcValue, maxDcAmt, type){
	
	var dcAmt = 0;	//할인금액
	
	if(dcApplyTypeCd == 'DC_APPLY_TYPE_CD.AMT'){	//정액
		if(Number(totalOrderAmt) >= Number(dcValue)){
			dcAmt = dcValue;
		}else{
			dcAmt = totalOrderAmt;
		}
	}else if(dcApplyTypeCd == 'DC_APPLY_TYPE_CD.RATE'){	//정률
		dcAmt = Math.round(Number(totalOrderAmt) * Number(dcValue) / 1000) * 10;
		if(type == "order"){
			if(Number(dcAmt) > Number(maxDcAmt)){
				dcAmt = maxDcAmt;
			}
		}
	}
	return dcAmt;		
}

var removeCouponCalc = function(type,orderProductNo){
	if(type == "order"){
		$("#couponPopupForm").find("#orderCoupon").val("");
	}else{
		$("#couponPopupForm").find("#"+type+orderProductNo).val("");
	}
	
	var applyId = "";
	var applyId2 = "";
	
	if(type == 'order'){
		applyId = "#tempapply"+type+"CouponId";
		applyId2 = "#tempapply"+type+"CouponIssueNo";
	}else{
		applyId = "#tempapply"+type+"CouponId"+orderProductNo;
		applyId2 = "#tempapply"+type+"CouponIssueNo"+orderProductNo;
	}
	
	
	$("#couponPopupForm").find(applyId).val("");
	$("#couponPopupForm").find(applyId2).val("");
	
	cssChange();
}

//쿠폰 setting
var couponSet = function(confirm,type,no){
	
	var selectId = "";
	var applyId = "";
	var temp = "";
	if(confirm){
		
	}else{
		temp = "temp";
	}
	if(type == 'order'){
		selectId = type+"Coupon";
		applyId = "#"+temp+"apply"+type+"CouponId";
		applyId2 = "#"+temp+"apply"+type+"CouponIssueNo";
	}else{
		selectId = type+no;
		applyId = "#"+temp+"apply"+type+"CouponId"+no;
		applyId2 = "#"+temp+"apply"+type+"CouponIssueNo"+no;
	}
	
	var selValue = $("#couponPopupForm").find("#"+selectId+" option:selected").val();	//현재 selectbox value;
	var emptyFlag = false;
	if(common.isEmpty(selValue)){
		emptyFlag = true;
	}
		
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
// 	console.log(selValue);
// 	console.log($(couponFormId));
	
	//쿠폰 조건
	var couponId = common.nvl($(couponFormId).find("#couponId").val(),'');
	var couponIssueNo = common.nvl($(couponFormId).find("#couponIssueNo").val(),'');
	var dcApplyTypeCd = $(couponFormId).find("#dcApplyTypeCd").val();
	var dcValue = $(couponFormId).find("#dcValue").val();
	var maxDcAmt = $(couponFormId).find("#maxDcAmt").val();
	var minOrderAmt = $(couponFormId).find("#minOrderAmt").val();
	var couponName = $(couponFormId).find("#name").val();
	
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
	
	
	if(type == 'delivery'){		//배송비
		var strKey = no.split("_");
		var deliveryAddressNo = strKey[0];
		var deliveryPolicyNo = strKey[1];		
	
		var orderFeeId  = "orderDeliveryFee";		
		var addressForm = "#addressForm"+deliveryAddressNo;
		
// 		console.log("deliveryAddressNo",deliveryAddressNo);
// 		console.log("deliveryPolicyNo",deliveryPolicyNo);
// 		console.log($(addressForm).find("#total"+orderFeeId));
		
		var totalFee = $(addressForm).find("#total"+orderFeeId).val();	//할인 대상금액
		
		var addressForm = "#addressForm"+deliveryAddressNo;	
// 		//주문상품번호(배송지)
		var orderProductNos = getAddrOrderproductNos(deliveryAddressNo);
		
		//원배송비
		var orderDeliveryFee = 0;							
		var couponDcAmt = 0;
		var totalOrderAmt = 0;
		
		//할인금액 계산
		for(var i=0;i<orderProductNos.length;i++){
			var applyId = "";
			var opn = orderProductNos[i].value;	//주문상품번호
			var productDeliveryPolicyNo = 	$(addressForm).find("#deliveryPolicyNo"+opn).val();
			
			applyId = "applyDeliveryFee";
			orderFee = $(addressForm).find("#orderDeliveryFee"+opn).val();	//주문배송비			
			
// 			console.log("orderFee",orderFee);						
			
// 			if(orderFee > 0){
					
				if(productDeliveryPolicyNo == deliveryPolicyNo){
					
					orderDeliveryFee += Number(orderFee);
					
					if(common.isNotEmpty(couponId)){
						couponDcAmt += Number(orderFee);
					}else{
// 						couponDcAmt = 0;
					}
					var salePrice = $("#productForm"+opn).find("#salePrice").val();
					var addSalePrice = $("#productForm"+opn).find("#addSalePrice").val();
					var orderQty = $(addressForm).find("#orderQty"+opn).val();
			 		var totalSalePrice = (Number(salePrice) + Number(addSalePrice)); //* Number(totOrderQty)	//상품 개당 총주문금액
					var productCouponDcAmt = $(addressForm).find("#tempproductCouponDcAmt"+opn).val();
					var plusCouponDcAmt = $(addressForm).find("#tempplusCouponDcAmt"+opn).val();
					totalOrderAmt += (Number(totalSalePrice) - Number(productCouponDcAmt) - Number(plusCouponDcAmt)) * Number(orderQty);
				}																			
// 			}
			
		}
// 		console.log("totalOrderAmt",totalOrderAmt);
// 		console.log("minOrderAmt",minOrderAmt);
		if(Number(totalOrderAmt) < Number(minOrderAmt)){
			alert(couponName+"은 상품 주문금액 "+common.priceFormat(minOrderAmt)+"원 이상 사용 가능합니다."); 				
			removeCouponCalc(type,no);
			couponIssueNo = "";
			couponId = "";
			couponDcAmt = 0;
		}
		
		var deliveryForm = "#deliveryForm_"+deliveryAddressNo+"_"+deliveryPolicyNo;
		var applyFee = Number(orderDeliveryFee) - Number(couponDcAmt);
		
		//배송지,정책별 할인금액 setting
// 		console.log(dcAmt);
// 		console.log(applyFee);
 		if(confirm){
 			$(deliveryForm).find("#"+type+"CouponId").val(couponId);
			$(deliveryForm).find("#"+type+"CouponIssueNo").val(couponIssueNo);
			$(deliveryForm).find("#"+type+"CouponDcAmt").val(couponDcAmt);
			$(deliveryForm).find("#"+applyId).val(applyFee);	
//  			$("#"+type+"DcAmt"+no).val(couponDcAmt);
 		}else{
 			$("#temp"+type+"DcAmt"+no).val(couponDcAmt);
// 	 		$("#deliveryTotalCouponDcAmtTxt"+no).html(common.priceFormat(couponDcAmt));
 		}
				

	}else if(type == 'wrap'){		//포장비
		
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
		
// 		console.log(dcAmt);

		
		var addressForm = "#addressForm"+deliveryAddressNo;	
// 		//주문상품번호(배송지)
		var orderProductNos = getAddrOrderproductNos(deliveryAddressNo);
		
		var dmap = [];
		
		//할인금액 분배
		for(var i=0;i<orderProductNos.length;i++){
			var applyId = "";
			var opn = orderProductNos[i].value;	//주문상품번호
			var deliveryPolicyNo = 	$(addressForm).find("#deliveryPolicyNo"+opn).val();
			var deliveryForm = "#deliveryForm_"+deliveryAddressNo+"_"+deliveryPolicyNo;
			var orderFee = 0;			
			
			applyId = "applyWrapFee";
			orderFee = $(deliveryForm).find("#orderWrapFee").val();	//주문포장비				
									
						
			if(orderFee > 0){
// 				var couponDcAmt = Number(dcAmt) * Number(orderFee) / Number(totalFee);	//포장비쿠폰할인금액
				
				var couponDcAmt = 0;
				
				if(common.isNotEmpty(couponId)){
					if(type == "wrap"){
						couponDcAmt = 1000;
					}else{
						couponDcAmt = orderFee;	
					}
				}
				
				var ex = false;			
				for(var k=0;k<dmap.length;k++){
					if(dmap[k] == deliveryPolicyNo){
						ex = true;
					}
				}
					
				if(!ex){
					dmap.push(deliveryPolicyNo);
					dcAmt += Number(couponDcAmt);
				}
									
// 				dcAmt += Number(couponDcAmt);
				
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
		var typedcAmt = Number(dcAmt) * Number(totOrderQty);
 		if(confirm){
 			$("#"+type+"DcAmt"+deliveryAddressNo).val(dcAmt);
 		}else{
 			$("#temp"+type+"DcAmt"+deliveryAddressNo).val(dcAmt);
 		}
				

	}else if(type == 'product' || type == 'plus'){		//상품, plus
		
		//1개적용유무
		var singleApplyYn = common.nvl($(couponFormId).find("#singleApplyYn").val(),'N'); 
		
		var orderProductNo = no;
		var cnt = getDeliveryCnt();
	
		var salePrice = $("#productForm"+orderProductNo).find("#salePrice").val();
		var addSalePrice = $("#productForm"+orderProductNo).find("#addSalePrice").val();
		var totOrderQty = $("#productForm"+orderProductNo).find("#orderQty").val();
 		var totalOrderAmt = (Number(salePrice) + Number(addSalePrice)); //* Number(totOrderQty)	//상품 개당 총주문금액

 		var notapply = false;
 		if(type == 'product'){
 			if(Number(minOrderAmt) > Number(totalOrderAmt)){
 				alert(couponName+"은 상품 주문금액 "+common.priceFormat(minOrderAmt)+"원 이상 사용 가능합니다.");
 				removeCouponCalc(type,orderProductNo);
 				couponIssueNo = "";
 				couponId = "";
 				dcAmt = 0;
 				notapply = true;
 			} 			
 			 			
// 		 	console.log(selValue);
// 		 	console.log($(couponFormId));
 			
 		}else{ 			
 			for(var i=0;i<cnt;i++){
				var productCouponDcAmt = common.nvl($("#addressForm"+i).find("#tempproductCouponDcAmt"+orderProductNo).val(),0);
// 				console.log("productCouponDcAmt",productCouponDcAmt);
				totalOrderAmt -= productCouponDcAmt;
 			}
 			
//  			console.log("minOrderAmt",minOrderAmt);
//  			console.log("totalOrderAmt",totalOrderAmt);
 			if(Number(minOrderAmt) > Number(totalOrderAmt)){
 				alert(couponName+"은 상품 주문금액 "+common.priceFormat(minOrderAmt)+"원 이상 사용 가능합니다."); 				
 				removeCouponCalc(type,orderProductNo);
 				couponIssueNo = "";
 				couponId = "";
 				dcAmt = 0;
 				notapply = true;
 			} 			
 		}
//  		console.log(totalOrderAmt);
 		dcAmt = couponDcAmtCalc(dcApplyTypeCd, totalOrderAmt, dcValue, maxDcAmt, type);
 		
 		//할인금액 setting
 		var typedcAmt = 0;
 		if("Y" == singleApplyYn){	//1개적용일때.
 			typedcAmt = Number(dcAmt);
 		}else{
 			typedcAmt = Number(dcAmt) * Number(totOrderQty);
 		}
 		
 		if(notapply){
 			dcAmt = 0;
 			typedcAmt = 0;
 		}
//  		console.log(typedcAmt);
		if(Number(maxDcAmt) < Number(typedcAmt)){
			alert(couponName+"은 최대할인금액 "+common.priceFormat(maxDcAmt)+"원이하 일때 사용 가능합니다.");			
			removeCouponCalc(type,orderProductNo);
			couponIssueNo = "";
			couponId = "";
			dcAmt = 0;
			typedcAmt = 0;
		}
 		
 		if(confirm){
			$("#"+type+"DcAmt"+orderProductNo).val(typedcAmt);
 		}else{
			$("#temp"+type+"DcAmt"+orderProductNo).val(typedcAmt);
 		}
//  		console.log("===========");
//  		console.log(Number(totalOrderAmt));
//  		console.log(Number(totalOrderAmt) * Number(dcValue));
//  		console.log(Math.round((Number(totalOrderAmt) * Number(dcValue)) / 1000) * 10);
//  		console.log(dcAmt);

		//plus 최소 주문금액 제한시 plus 쿠폰 제거
		//plus 최대할인금액 제한시 plus 쿠폰 제거
// 		var plusEmptyFlag = false;
// 		var recalc = false;
// 		if(type == 'product'){
// 	 		var selPlusValue = $("#couponPopupForm").find("#plus"+orderProductNo+" option:selected").val();	//현재 selectbox value;
// 	 		console.log("selPlusValue : ",selPlusValue);
// 			if(common.isNotEmpty(selPlusValue)){
// 				var plusCouponFormId = "#couponForm_plus_"+selPlusValue;
// 				var plusMinOrderAmt = $(plusCouponFormId).find("#minOrderAmt").val();
// 				var plusMaxDcAmt = $(plusCouponFormId).find("#maxDcAmt").val();
			
// 				console.log("plusCouponFormId : ",plusCouponFormId);
// 				console.log("plusMinOrderAmt : ",plusMinOrderAmt);
// 				console.log("plusMaxDcAmt : ",plusMaxDcAmt);
				
// 				var objAmt = Number(totalOrderAmt) - Number(typedcAmt);
// 				console.log(objAmt);

				//plus 쿠폰 최소주문금액.
// 				if(Number(plusMinOrderAmt) > Number(objAmt)){
// // 					console.log(plusMinOrderAmt);
// 					plusEmptyFlag = true;
// 					removeCouponCalc("plus",orderProductNo);					
// 	 			}else{
// 	 				//plus 쿠폰 최대할인금액 제한.
// 					//상품쿠폰 적용뒤 plus 할인금액 재계산.
// 					var plusDcAmt = couponDcAmtCalc(dcApplyTypeCd, objAmt, dcValue, maxDcAmt, type);
					
// 					typedcAmt = Number(plusDcAmt) * Number(totOrderQty);
					
// 					if(Number(maxDcAmt) < Number(typedcAmt)){
// 						plusEmptyFlag = true;
// 						$("#tempplusDcAmt"+orderProductNo).val("0");
// 						$("#couponPopupForm").find("#plus"+orderProductNo).val("");	
// 						cssChange();
// 					}
// 	 			}
				
								
// 				recalc = true;
// 			}
// 		}
		
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
					
					var productCouponId = $(addressForm).find("#productCouponId"+orderProductNo).val();
					var productCouponIssueNo = $(addressForm).find("#productCouponIssueNo"+orderProductNo).val();
					var productCouponSingleYn = $("#couponForm_product_"+productCouponId+"_"+productCouponIssueNo).val();
					var productCouponDcAmt = $(addressForm).find("#productCouponDcAmt"+orderProductNo).val();
					if("Y" == productCouponSingleYn){
						
					}else{
						productCouponDcAmt = Number(productCouponDcAmt) * Number(orderQty);
					}
					
					var plusCouponId = $(addressForm).find("#plusCouponId"+orderProductNo).val();
					var plusCouponIssueNo = $(addressForm).find("#plusCouponIssueNo"+orderProductNo).val();
					var plusCouponSingleYn = $("#couponForm_plus_"+productCouponId+"_"+productCouponIssueNo).val();					
					var plusCouponDcAmt = $(addressForm).find("#plusCouponDcAmt"+orderProductNo).val();
					if("Y" == plusCouponSingleYn){
						
					}else{
						plusCouponDcAmt = Number(plusCouponDcAmt) * Number(orderQty);
					}
					
					var orderCouponDcAmt = $(addressForm).find("#orderCouponDcAmt"+orderProductNo).val();
				
// 					console.log("product");
// 					console.log(totalFee);
// 					console.log(productCouponDcAmt);
// 					console.log(plusCouponDcAmt);
// 					console.log(orderCouponDcAmt);
					
					
					var paymentAmt = Number(totalFee) - Number(productCouponDcAmt) - Number(plusCouponDcAmt) - Number(orderCouponDcAmt);
// 					console.log(paymentAmt);
					$(addressForm).find("#paymentAmt"+orderProductNo).val(paymentAmt);		//최종결제가(상품당)					
				}else{
// 					console.log("plusEmptyFlag : ",plusEmptyFlag);
// 					var plusCouponDcAmt = $(addressForm).find("#tempplusCouponDcAmt"+orderProductNo).val();
// 					if(plusEmptyFlag){
// 						$(addressForm).find("#tempplusCouponId"+orderProductNo).val("");
// 						$(addressForm).find("#tempplusCouponIssueNo"+orderProductNo).val("");
// 						$(addressForm).find("#tempplusCouponDcAmt"+orderProductNo).val("0");						
// 					}						
					
					
					$(addressForm).find("#temp"+type+"CouponId"+orderProductNo).val(couponId);
					$(addressForm).find("#temp"+type+"CouponIssueNo"+orderProductNo).val(couponIssueNo);
					$(addressForm).find("#temp"+type+"CouponDcAmt"+orderProductNo).val(couponDcAmt);
					
					var productCouponId = $(addressForm).find("#tempproductCouponId"+orderProductNo).val();
					var productCouponIssueNo = $(addressForm).find("#tempproductCouponIssueNo"+orderProductNo).val();
					var productCouponSingleYn = $("#couponForm_product_"+productCouponId+"_"+productCouponIssueNo).val();
					var productCouponDcAmt = $(addressForm).find("#tempproductCouponDcAmt"+orderProductNo).val();
					if("Y" == productCouponSingleYn){
						
					}else{
						productCouponDcAmt = Number(productCouponDcAmt) * Number(orderQty);
					}
					
					var plusCouponId = $(addressForm).find("#tempplusCouponId"+orderProductNo).val();
					var plusCouponIssueNo = $(addressForm).find("#tempplusCouponIssueNo"+orderProductNo).val();
					var plusCouponSingleYn = $("#couponForm_plus_"+productCouponId+"_"+productCouponIssueNo).val();					
					var plusCouponDcAmt = $(addressForm).find("#tempplusCouponDcAmt"+orderProductNo).val();
					if("Y" == plusCouponSingleYn){
						
					}else{
						plusCouponDcAmt = Number(plusCouponDcAmt) * Number(orderQty);
					}
					
					var orderCouponDcAmt = $(addressForm).find("#temporderCouponDcAmt"+orderProductNo).val();
				
// 					console.log("product");
// 					console.log(totalFee);
// 					console.log(productCouponDcAmt);
// 					console.log(plusCouponDcAmt);
// 					console.log(orderCouponDcAmt);
					
					var paymentAmt = Number(totalFee) - Number(productCouponDcAmt) - Number(plusCouponDcAmt) - Number(orderCouponDcAmt);
// 					console.log(paymentAmt);
					$(addressForm).find("#temppaymentAmt"+orderProductNo).val(paymentAmt);		//최종결제가(상품당)	
					
					if(type=='product'){
						couponSet(false,"plus",orderProductNo);
					}
					var sumDelivery = $("input[name=sumDeliveryPolicyNo]");
					
					for(var j=0;j<sumDelivery.length;j++){
						var deliveryPolicyNo = sumDelivery[j].value;
						couponSet(false,"delivery",i+"_"+deliveryPolicyNo);
					}
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
				if(confirm){
					productCouponDcAmt = $(addressForm).find("#productCouponDcAmt"+orderProductNo).val();
					plusCouponDcAmt = $(addressForm).find("#plusCouponDcAmt"+orderProductNo).val();
				}
				
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

				if(emptyFlag){	//선택안함일경우 적용 상품모두
					orderApplyProducts.push(orderProductNo);
				}
			}				
		}
		
// 		console.log("===== totalFee");
// 		console.log(totalFee);
// 		console.log(totalFee2);
// 		console.log(minOrderAmt);
		
		
		
		dcAmt = couponDcAmtCalc(dcApplyTypeCd, totalFee, dcValue, maxDcAmt, type);
		
// 		if(dcApplyTypeCd == 'DC_APPLY_TYPE_CD.AMT'){	//정액
// 			if(Number(totalFee) >= Number(dcValue)){
// 				dcAmt = dcValue;
// 			}else{
// 				dcAmt = totalFee;
// 			}
// 		}else if(dcApplyTypeCd == 'DC_APPLY_TYPE_CD.RATE'){	//정률
// 			dcAmt = Math.round(Number(totalFee) * Number(dcValue) / 1000) * 10;
// 			if(Number(dcAmt) > Number(maxDcAmt)){
// 				dcAmt = maxDcAmt;
// 			}
// 		}

// 		console.log("dcAmt : " + dcAmt);
// 		console.log("totalFee : " +totalFee);

		if(Number(minOrderAmt) > totalFee){
			alert(couponName+"은 상품 주문금액 "+common.priceFormat(minOrderAmt)+"원 이상 사용 가능합니다."); 				
			removeCouponCalc(type,orderProductNo);
			couponIssueNo = "";
			couponId = "";
			dcAmt = 0;
		}
		
		//할인금액 setting
		if(confirm){
			$("#orderDcAmt").val(dcAmt);
		}else{
			$("#temporderDcAmt").val(dcAmt);
		}
		
		var remainOrderAmt = dcAmt;
		var calibrateOrderDcAmt = 0;
		
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

		 		var rate = Number(applyFee) / Number(totalFee);
				if(totalFee == 0){	//분모가 0일때 비율은 0
		 			rate = 0;
		 		}
		 		var couponDcAmt = Math.round(Number(dcAmt) * rate * 0.1) * 10;
		 		
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
		 		
// 		 		console.log("i : ",i);
// 		 		console.log("cnt.length : ",cnt);
// 		 		console.log("j : ",j);
// 		 		console.log("opns.length : ",opns.length);
		 		if(i+1 == cnt && j+1 == opns.length){
		 			couponDcAmt += remainOrderAmt;
		 			calibrateOrderDcAmt = remainOrderAmt;
// 		 			console.log("last couponDcAmt : ",couponDcAmt);
		 		} 
// 		 		console.log("couponDcAmt2 : ",couponDcAmt);
		 		
		 		var totalFeeUnit = (Number(salePrice) + Number(addSalePrice)) * Number(orderQty);	//paymentAmt 계산을 위한 총금액
		 		
				if(confirm){
															
					$(addressForm).find("#"+type+"CouponId"+orderProductNo).val(couponId);
					$(addressForm).find("#"+type+"CouponIssueNo"+orderProductNo).val(couponIssueNo);
					$(addressForm).find("#"+type+"CouponDcAmt"+orderProductNo).val(couponDcAmt);	//짜투리 금액 포함된 할인금액
					$(addressForm).find("#calibrateOrderDcAmt"+orderProductNo).val(calibrateOrderDcAmt);	//짜투리 금액 (service에서 쿠폰 할인금액에서 빼고 따로 넣는다.)
								
					var productCouponDcAmt = $(addressForm).find("#productCouponDcAmt"+orderProductNo).val();
					var plusCouponDcAmt = $(addressForm).find("#plusCouponDcAmt"+orderProductNo).val();
					var orderCouponDcAmt = $(addressForm).find("#orderCouponDcAmt"+orderProductNo).val();
					
// 					console.log("order");
// 					console.log("totalFeeUnit",totalFeeUnit);
// 					console.log("productCouponDcAmt",productCouponDcAmt);
// 					console.log("plusCouponDcAmt",plusCouponDcAmt);
// 					console.log("orderCouponDcAmt",orderCouponDcAmt);
// 					console.log("calibrateOrderDcAmt",calibrateOrderDcAmt);
					
					var paymentAmt = Number(totalFeeUnit) - (Number(productCouponDcAmt) * Number(orderQty)) - (Number(plusCouponDcAmt) * Number(orderQty)) - Number(orderCouponDcAmt);
					
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
// 					console.log(totalFeeUnit);
// 					console.log(productCouponDcAmt);
// 					console.log(plusCouponDcAmt);
// 					console.log(orderCouponDcAmt);
					
					var paymentAmt = Number(totalFeeUnit) - (Number(productCouponDcAmt) * Number(orderQty)) - (Number(plusCouponDcAmt) * Number(orderQty)) - Number(orderCouponDcAmt);
					
// 					console.log(paymentAmt);
					
					$(addressForm).find("#temppaymentAmt"+orderProductNo).val(paymentAmt);		//최종결제가(상품당)
				}
		 		
			}
		}
	}
	
	if(confirm){						
		copyTempCoupon();
	}else{

		//쿠폰 레이어 합계금액 세팅
		var couponPopupForm = "#couponPopupForm";
		applyTotalCouponCnt = 0;		
		
		var productTotalCouponDcAmtTxt = calCouponSubAmt("product",false) + calCouponSubAmt("plus",false);
		var orderTotalCouponDcAmtTxt = calCouponSubAmt("order",false);
		var deliveryTotalCouponDcAmtTxt = calCouponSubAmt("delivery",false);
		var wrapTotalCouponDcAmtTxt = calCouponSubAmt("wrap",false);
		var applyTotalCouponDcAmtTxt = Number(productTotalCouponDcAmtTxt)
									  +Number(orderTotalCouponDcAmtTxt)
									  +Number(deliveryTotalCouponDcAmtTxt)
									  +Number(wrapTotalCouponDcAmtTxt);
		
		//할인금액 txt
		$("#productTotalCouponDcAmtTxt").html(common.priceFormat(productTotalCouponDcAmtTxt,true));
		$("#orderTotalCouponDcAmtTxt").html(common.priceFormat(orderTotalCouponDcAmtTxt));
		$("#deliveryTotalCouponDcAmtTxt").html(common.priceFormat(deliveryTotalCouponDcAmtTxt));
		$("#wrapTotalCouponDcAmtTxt").html(common.priceFormat(wrapTotalCouponDcAmtTxt));				
		$("#applyTotalCouponDcAmtTxt").html(common.priceFormat(applyTotalCouponDcAmtTxt));
		$("#applyTotalCouponCnt").html(applyTotalCouponCnt);		
	}
}

//쿠폰 확인
var couponConfirm = function(){
	
	var childrenDealYn = $("#childrenDealYn").val();
	if(childrenDealYn == "Y"){
		$("#orderCouponDiv").hide();
	}
	
	var cnt =getDeliveryCnt();	
	
	for(var i=0;i<cnt;i++){
		var  orderProductNos = getAddrOrderproductNos(i);
		
		for(var j=0;j<orderProductNos.length;j++){
			couponSet(true,'product',orderProductNos[j].value);
			couponSet(true,'plus',orderProductNos[j].value);
		}
		
		var sumDelivery = $("input[name=sumDeliveryPolicyNo]");
		
		for(var j=0;j<sumDelivery.length;j++){
			var deliveryPolicyNo = sumDelivery[j].value;			
			couponSet(true,'delivery',i+"_"+deliveryPolicyNo);			
		}
		
		
		couponSet(true,'wrap',i);
		
	}		
	couponSet(true,'order');
	
	$("#point").val("0");
	$("#deposit").val("0");
	clearSubPaymentCheck("point");
	clearSubPaymentCheck("deposit");
	
	calc();
	
	couponClose();
}
var couponInit = function(type){
	var form = "#couponPopupForm";
	var dcAmt = $(form).find("input[name="+type+"DcAmt]");
	for(var i=0;i<dcAmt.length;i++){
		dcAmt[i].value = "0";
	}
	var apply = $(form).find("input[name=apply"+type+"CouponIssueNo]");
	for(var i=0;i<apply.length;i++){
		apply[i].value = "";		
	}	
}


var sumCouponCnt = function(){ 
	
	var arrCoupon = [];
	var arrApplyCoupon = [];
	
	sumCouponCntSub("product",arrCoupon,arrApplyCoupon);
// 	if(arrApplyCoupon.length > 0){
		sumCouponCntSub("plus",arrCoupon,arrApplyCoupon);
// 	}
	sumCouponCntSub("order",arrCoupon,arrApplyCoupon);
	sumCouponCntSub("delivery",arrCoupon,arrApplyCoupon);
	sumCouponCntSub("wrap",arrCoupon,arrApplyCoupon);
	
	var totalCouponCnt = arrCoupon.length;
	var applyCouponCnt = arrApplyCoupon.length;
	
	$("#totalCouponCnt").val(totalCouponCnt);
	$("#applyCouponCnt").val(applyCouponCnt);
	$("#applyCouponCntTxt").html(applyCouponCnt);
}

var sumCouponCntSub = function(type,arrCoupon,arrApplyCopuon){
	var form = $("form[name=couponForm_"+type+"]");
	
	for(var i=0;i<form.length;i++){
		var id = form[i].id;
	
		var couponId = $("#"+id).find("#couponId").val();
		
		var ex = false;
		for(var j=0;j<arrCoupon.length;j++){
			if(couponId == arrCoupon[j]){
				ex = true;
			}
		}
		if(!ex){
			arrCoupon.push(couponId);
			
			if(type == "delivery"){
				var deliveryFee = $("#couponTotalDeliveryFee").val();
				if(Number(deliveryFee) > 0){
					arrApplyCopuon.push(couponId);	
				}
			}else if(type == "wrap"){
				var wrapYn = $("#couponTotalWrapYn").val();
				if(wrapYn == "true"){
					arrApplyCopuon.push(couponId);	
				}
			}else{
				arrApplyCopuon.push(couponId);
			}
		}
	}
// 	console.log("type",type);
// 	console.log("arrCoupon",arrCoupon);
// 	console.log("arrApplyCopuon",arrApplyCopuon);
}

</script>	

<input type="hidden" id="childrenDealYn" value="${childrenDealYn }" />

<c:set var="productIdx" value="0"/>
<c:set var="plusIdx" value="0"/>
<c:set var="orderIdx" value="0"/>
<c:set var="deliveryIdx" value="0"/>
<c:set var="wrapIdx" value="0"/>
<c:forEach items="${omsOrder.omsOrderproducts }" var="os">
	<c:if test="${fn:length(os.omsOrdercoupons) > 0 }">
		<c:forEach items="${os.omsOrdercoupons }" var="oc">
			<c:if test="${oc.couponTypeCd == 'COUPON_TYPE_CD.PRODUCT' }">
				<form name="couponForm_product" id="couponForm_product_${oc.couponId }_${oc.couponIssueNo }" style="display: none;">
					<input type="text" name="couponId" id="couponId" value="${oc.couponId }"/>
					<input type="text" name="couponIssueNo" id="couponIssueNo" value="${oc.couponIssueNo }"/>
					<input type="text" name="name" id="name" value="${oc.name }"/>
					<input type="text" name="dcApplyTypeCd" id="dcApplyTypeCd" value="${oc.dcApplyTypeCd }"/>
					<input type="text" name="dcValue" id="dcValue" value="${oc.dcValue }"/>
					<input type="text" name="maxDcAmt" id="maxDcAmt" value="${oc.maxDcAmt }"/>
					<input type="text" name="minOrderAmt" id="minOrderAmt" value="${oc.minOrderAmt }"/>
					<input type="text" name="singleApplyYn" id="singleApplyYn" value="${oc.singleApplyYn}"/>
				</form>
				<c:set var="productIdx" value="${productIdx+1 }"/>
			</c:if>
			<c:if test="${oc.couponTypeCd == 'COUPON_TYPE_CD.PLUS' }">
				<form name="couponForm_plus" id="couponForm_plus_${oc.couponId }_${oc.couponIssueNo }" style="display: none;">
					<input type="text" name="couponId" id="couponId" value="${oc.couponId }"/>
					<input type="text" name="couponIssueNo" id="couponIssueNo" value="${oc.couponIssueNo }"/>
					<input type="text" name="name" id="name" value="${oc.name }"/>
					<input type="text" name="dcApplyTypeCd" id="dcApplyTypeCd" value="${oc.dcApplyTypeCd }"/>
					<input type="text" name="dcValue" id="dcValue" value="${oc.dcValue }"/>
					<input type="text" name="maxDcAmt" id="maxDcAmt" value="${oc.maxDcAmt }"/>
					<input type="text" name="minOrderAmt" id="minOrderAmt" value="${oc.minOrderAmt }"/>
					<input type="text" name="singleApplyYn" id="singleApplyYn" value="${oc.singleApplyYn}"/>
				</form>
				<c:set var="plusIdx" value="${plusIdx+1 }"/>
			</c:if>
		</c:forEach>
	</c:if>
	<c:if test="${os.optimalProductCoupon != null && childrenDealYn != 'Y'}">
		<form name="couponForm_optimalProduct" id="couponForm_optimalProduct${os.orderProductNo }" style="display: none;">
			<input type="text" name="orderProductNo" id="orderProductNo" value="${os.orderProductNo }"/>
			<input type="text" name="couponId" id="couponId" value="${os.optimalProductCoupon.couponId }"/>
			<input type="text" name="couponIssueNo" id="couponIssueNo" value="${os.optimalProductCoupon.couponIssueNo  }"/>
		</form>
	</c:if>
	<c:if test="${os.optimalPlusCoupon != null && childrenDealYn != 'Y' }">
		<form name="couponForm_optimalPlus" id="couponForm_optimalPlus${os.orderProductNo }" style="display: none;">
			<input type="text" name="orderProductNo" id="orderProductNo" value="${os.orderProductNo }"/>
			<input type="text" name="couponId" id="couponId" value="${os.optimalPlusCoupon.couponId }"/>
			<input type="text" name="couponIssueNo" id="couponIssueNo" value="${os.optimalPlusCoupon.couponIssueNo  }"/>
		</form>
	</c:if>
</c:forEach>
<c:if test="${omsOrder.optimalCoupon != null && childrenDealYn != 'Y' }">
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
<input type="hidden" id="totalCouponCnt" value="0"/>
<input type="hidden" id="applyCouponCnt" value="0"/>

<!-- ### 쿠폰조회 팝업 ### -->
<div class="pop_wrap ly_coupon" id="couponDiv" style="display:none;">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">쿠폰적용</h3>
		</div>
		<form id="couponPopupForm">
		<div class="pop_content">
			
			<c:if test="${productIdx > 0 || plusIdx > 0}">
			<div class="write wType">
				<ul class="rw_tb_tbody2">
					<li>
						<div class="tr_box w100">
							<div class="col1">
								<span class="group_inline">상품할인 쿠폰</span>
							</div>
							<div class="col2">
								<div class="group_block">
									<ul class="couponList">
										<c:forEach items="${omsOrder.omsOrderproducts }" var="os">
										<c:if test="${fn:length(os.omsOrdercoupons) > 0 }">
										<input type="hidden" name="couponOrderProductNo" value=${os.orderProductNo } />
										<input type="hidden" name="productDcAmt" id="productDcAmt${os.orderProductNo }" value="0"/>
										<input type="hidden" name="plusDcAmt" id="plusDcAmt${os.orderProductNo }" value="0"/>
										<input type="hidden" name="applyproductCouponId" id="applyproductCouponId${os.orderProductNo }" value=""/>
										<input type="hidden" name="applyproductCouponIssueNo" id="applyproductCouponIssueNo${os.orderProductNo }" value=""/>
										<input type="hidden" name="applyplusCouponId" id="applyplusCouponId${os.orderProductNo }" value=""/>
										<input type="hidden" name="applyplusCouponIssueNo" id="applyplusCouponIssueNo${os.orderProductNo }" value=""/>
										<input type="hidden" name="tempproductDcAmt" id="tempproductDcAmt${os.orderProductNo }" value="0"/>
										<input type="hidden" name="tempplusDcAmt" id="tempplusDcAmt${os.orderProductNo }" value="0"/>
										<input type="hidden" name="tempapplyproductCouponId" id="tempapplyproductCouponId${os.orderProductNo }" value=""/>
										<input type="hidden" name="tempapplyproductCouponIssueNo" id="tempapplyproductCouponIssueNo${os.orderProductNo }" value=""/>
										<input type="hidden" name="tempapplyplusCouponId" id="tempapplyplusCouponId${os.orderProductNo }" value=""/>
										<input type="hidden" name="tempapplyplusCouponIssueNo" id="tempapplyplusCouponIssueNo${os.orderProductNo }" value=""/>
										<li>
											<div class="left">
												<span class="dot">${os.productName }</span>
											</div>
											<div class="right">
												<c:if test="${productIdx > 0 && os.productCouponCnt > 0}">
												<div class="select_box1">
													<label></label>
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
												</c:if>
												<c:if test="${plusIdx > 0  && os.plusCouponCnt > 0}">
												<div class="select_box1">
													<label></label>
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
												</c:if>
											</div>
										</li>
										</c:if>
										</c:forEach>
									</ul>
								</div>
							</div>
						</div>
					</li>
				</ul>
				<dl class="dcInfo">
					<dt>
						<span>할인금액</span>
					</dt>
					<dd>
						<strong id="productTotalCouponDcAmtTxt">0<i>원</i></strong>
					</dd>
				</dl>			
			</div>
			</c:if>
			
			<c:if test="${deliveryIdx > 0}">
			<div class="write wType mt" id="deliveryDiv">
				<ul class="rw_tb_tbody2">
					<li>
						<div class="tr_box w100">
							<div class="col1">
								<span class="group_inline">배송무료 쿠폰</span>
							</div>
							<div class="col2">
								<div class="group_block">
									<ul class="couponList" id="deliveryUl">
									</ul>
								</div>
							</div>
						</div>
					</li>
				</ul>
				<dl class="dcInfo">
					<dt>
						<span>할인금액</span>
					</dt>
					<dd>
						<strong id="deliveryTotalCouponDcAmtTxt">0</strong><i>원</i>
					</dd>
				</dl>
			</div>
			</c:if>
			<c:if test="${wrapIdx > 0}">
			<div class="write wType mt" id="wrapDiv"></div>
			</c:if>			
			<c:if test="${orderIdx > 0}">
			<div class="write wType mt">
				<ul class="rw_tb_tbody2">
					<c:if test="${orderIdx > 0}">
					<input type="hidden" name="orderDcAmt" id="orderDcAmt" value="0"/>
					<input type="hidden" name="applyorderCouponId" id="applyorderCouponId" value=""/>
					<input type="hidden" name="applyorderCouponIssueNo" id="applyorderCouponIssueNo" value=""/>
					<input type="hidden" name="temporderDcAmt" id="temporderDcAmt" value="0"/>
					<input type="hidden" name="tempapplyorderCouponId" id="tempapplyorderCouponId" value=""/>
					<input type="hidden" name="tempapplyorderCouponIssueNo" id="tempapplyorderCouponIssueNo" value=""/>
					<li>
						<div class="tr_box">
							<div class="col1">
								<span class="group_inline">장바구니 쿠폰</span>
								<span class="group_inline sale_pay">할인금액  <strong id="orderTotalCouponDcAmtTxt">0</strong>원</span>
							</div>
							<div class="col2">
								<div class="group_block">
									<div class="select_box1">
										<label></label>
										<select id="orderCoupon" onchange="javascript:couponChgOrder()">
											<option value="" selected>선택</option>
											<c:forEach items="${omsOrder.omsOrdercoupons }" var="oc">
												<c:if test="${oc.couponTypeCd == 'COUPON_TYPE_CD.ORDER' }">
												<option value="${oc.couponId }_${oc.couponIssueNo }">${oc.name }</option><!-- <fmt:formatNumber value="${oc.couponDcAmt }" pattern="#,###"/> -->
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
							</div>
						</div>
					</li>
					</c:if>
<%-- 					<c:if test="${deliveryIdx > 0 }"> --%>
<!-- 					<div id="deliveryCouponDiv"></div> -->
<%-- 					</c:if> --%>
		
<%-- 					<c:if test="${wrapIdx > 0 }"> --%>
<!-- 					<div id="wrapCouponDiv"></div> -->
<%-- 					</c:if>					 --%>
				</ul>
			</div>					
<!-- 			<div class="totalBox2" style="display: none;"> -->
<!-- 				<strong>할인금액</strong>					 -->
<!-- 				<em id="orderTotalCouponDcAmtTxt">0원</em> -->
<!-- 			</div> -->
			</c:if>			
			<div class="totalBox">
				<div class="left">
					<strong>선택한 쿠폰 <em id="applyTotalCouponCnt">0</em>개</strong>
				</div>
				<div class="right">
					<strong>할인금액 <em id="applyTotalCouponDcAmtTxt">0</em>원</strong>
				</div>
			</div>

			<div class="btn_wrapC btn2ea">
				<a href="#none" class="btn_mStyle1 sWhite1" onclick="javascript:couponClose()"><span>취소</span></a>
				<a href="#none" class="btn_mStyle1 sPurple1" onclick="javascript:couponConfirm()"><span>적용</span></a>
			</div>
			
			<ul class="notice">
				<li>사용 가능한 쿠폰만 노출됩니다.</li>
				<li>이미 적용한 쿠폰은 다른 상품에 중복 적용되지 않습니다.</li>
			</ul>
		</div>
		</form>
		<button type="button" class="btn_x pc_btn_close" onclick="javascript:couponClose()">닫기</button>
	</div>
</div>
<!-- ### //쿠폰조회 팝업 ### -->