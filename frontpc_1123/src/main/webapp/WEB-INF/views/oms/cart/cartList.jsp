<%--
	화면명 : 장바구니
	작성자 : dennis
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags" %>
<script type="text/javascript" src="/resources/js/mms/mms.mypage.js"></script>
<script type="text/javascript" src="/resources/js/common/order.ui.js"></script>
<script type="text/javascript">
//계산
var calc = function(){
	
	var chklist = $("input[name=chk]");
	
	var preDeliveryPolicyNo;
	var preMinDeliveryFreeAmt;
	var subTotalSalePrice = 0;
	var subTotalDcPrice = 0;	//중간 쿠폰할인금액
	var subTotalSaleAmt = 0;
	var totalPrice = 0;
	var totalDcPrice = 0;
	var totalDeliveryFee = 0;
	var totalAmt = 0;	
	var chkCnt = 0;	
	
	var totalDeliveryFeeFreeYn = 'N';
// 	console.log(chklist);
	for(var i=0;i<chklist.length;i++){
		var cartProductNo = chklist[i].value;
		var checked = chklist[i].checked;
		var form = "cartForm"+cartProductNo;
		var saleStateCd = $("#"+form+" #saleStateCd").val();				
		var deliveryPolicyNo = $("#"+form+" #deliveryPolicyNo").val();
		var minDeliveryFreeAmt = $("#minDeliveryFreeAmt"+deliveryPolicyNo).val();
		var deliveryFee = $("#deliveryFee"+deliveryPolicyNo).val();

		var deliveryFeeFreeYn = $("#"+form+" #deliveryFeeFreeYn").val();			
							
		var salePrice = $("#"+form+" #salePrice").val();
		var addSalePrice = $("#"+form+" #addSalePrice").val();	
		var totalSalePrice =$("#"+form+" #totalSalePrice").val();
		var orgTotalSalePrice =$("#"+form+" #orgTotalSalePrice").val();
		var qty = $("#"+form+" #qty").val();
		var sumOrgSalePrice = Number(orgTotalSalePrice) * Number(qty);
		var sumSalePrice = (Number(salePrice) + Number(addSalePrice)) * Number(qty);
		
		var couponDcAmt = $("#"+form+" #couponDcAmt").val();
		var singleApplyYn = $("#"+form+" #singleApplyYn").val();
		if("Y" == singleApplyYn){
			
		}else{
			couponDcAmt = Number(couponDcAmt) * Number(qty);
		}
		
		$("#"+form+" #salePriceTxt").html(common.priceFormat(sumSalePrice)+" <i>원</i>");		
			
// 			console.log("======================"+i);
// 			console.log("cartProductNo : "+cartProductNo);
// 			console.log("salePrice : "+salePrice);
// 			console.log("addSalePrice : "+addSalePrice);
// 			console.log("totalSalePrice : "+totalSalePrice);
// 			console.log("orgTotalSalePrice : "+orgTotalSalePrice);
// 			console.log("qty : "+qty);
			
// 			console.log("sumOrgSalePrice : "+sumOrgSalePrice);
// 			console.log("sumSalePrice : "+sumSalePrice);
// 			console.log("subTotalSalePrice : "+subTotalSalePrice);
// 			console.log("deliveryPolicyNo : "+deliveryPolicyNo);
// 			console.log("preDeliveryPolicyNo : "+preDeliveryPolicyNo);				
// 			console.log("======================"+i);					

// 			if(chklist.length-1 == i || deliveryPolicyNo == -1){
// 				var id = deliveryPolicyNo;
				
// 				var minDeliveryFreeAmt = $("#minDeliveryFreeAmt"+id).val();
// 				var deliveryFee = $("#deliveryFee"+id).val();
				
// 				if(Number(minDeliveryFreeAmt) <= Number(subTotalSalePrice)){
// 					deliveryFee = 0;	
// 				}
				
// 				$("#subTotalPrice"+id).html(common.priceFormat(subTotalSalePrice,true));
// 				$("#subDeliveryFee"+id).html(common.priceFormat(deliveryFee,true));
// 				var tot = Number(subTotalSalePrice) + Number(deliveryFee);
// 				$("#subTotalAmt"+id).html(common.priceFormat(tot,true));
				
// 				totalDeliveryFee = Number(totalDeliveryFee) + Number(deliveryFee);
// 			}	

		//total
		if(checked && deliveryPolicyNo != -1){
			if(deliveryFeeFreeYn == 'Y'){
				totalDeliveryFeeFreeYn = 'Y';
			}
			subTotalSalePrice = Number(subTotalSalePrice) + Number(sumSalePrice);	//중간 총상품 금액
			subTotalDcPrice = Number(subTotalDcPrice) +  Number(couponDcAmt);	//중간 총 할인금액
			totalPrice = Number(totalPrice) + Number(sumSalePrice);	//총 상품금액
			totalDcPrice = Number(totalDcPrice) +  Number(couponDcAmt);	//총 할인금액
			chkCnt++;
		}
		
		var sum = false;
		
		if(deliveryPolicyNo != -1){
			if(chklist.length-1 == i){
				sum = true;
			}else{
				var form2 = "cartForm"+chklist[i+1].value;
				var nextDeliveryPolicyNo = $("#"+form2+" #deliveryPolicyNo").val();
				
// 				console.log("deliveryPolicyNo : "+deliveryPolicyNo);
// 				console.log("nextDeliveryPolicyNo : "+nextDeliveryPolicyNo);
				
				if(nextDeliveryPolicyNo != deliveryPolicyNo){
					sum = true;
				}
			}
		}
	
		//sub total
		if(sum){
			
// 			console.log("====================");
// 			console.log("minDeliveryFreeAmt : "+minDeliveryFreeAmt);
// 			console.log("subTotalSalePrice : "+subTotalSalePrice);
// 			console.log("deliveryFee : "+deliveryFee);
// 			console.log("totalDeliveryFeeFreeYn : "+totalDeliveryFeeFreeYn);
			
			if(chkCnt==0 || Number(minDeliveryFreeAmt) <= Number(subTotalSalePrice) || totalDeliveryFeeFreeYn == 'Y' ){
				deliveryFee = 0;	
			}
			
			$("#subTotalPrice"+deliveryPolicyNo).html(common.priceFormat(subTotalSalePrice,true));
			$("#subTotalDcPrice"+deliveryPolicyNo).html(common.priceFormat(subTotalDcPrice,true));
			$("#subDeliveryFee"+deliveryPolicyNo).html(common.priceFormat(deliveryFee,true));
			var tot = Number(subTotalSalePrice) - Number(subTotalDcPrice) + Number(deliveryFee);
			$("#subTotalAmt"+deliveryPolicyNo).html(common.priceFormat(tot)+"<i>원</i>");
			totalDeliveryFee = Number(totalDeliveryFee) + Number(deliveryFee);			
			
			chkCnt = 0;
			subTotalSalePrice = 0;
			subTotalDcPrice = 0;
			
			totalDeliveryFeeFreeYn = 'N';
		}											
	}	
	
	$("#totalPrice").html(common.priceFormat(totalPrice,true));
	$("#totalDcPrice").html(common.priceFormat(totalDcPrice,true));
	$("#totalDeliveryFee").html(common.priceFormat(totalDeliveryFee,true));
	var totalAmt = totalPrice - totalDcPrice + totalDeliveryFee;
	$("#totalAmt").html(common.priceFormat(totalAmt)+"<i>원</i>");
	
	allChkcheck();
	
}


//수량 -
var minusQty = function(form){
	
	var qty = $(form).find("#tempQty");

	var minQty = $(form).find("#minQty");
	
	if(qty.val() == 1){
		alert("수량은 1이상입니다.");
		return;
	}else if(qty.val() == minQty.val()){
		alert("주문 최소수량은 "+minQty.val()+"이상입니다.");
		return;
	}

	ccs.common.quantityMinus($(form).find("#tempQty"));
}
//수량 +
var plusQty = function(form){
	var cartTypeCd = $("#selCartTypeCd").val();
	ccs.common.quantityPlus($(form).find("#tempQty"));
	oms.changeOptionLayer.maxStockCheck(form);
}
//수량 수정
var chgQty = function(form,value){
	var qty = $(form).find("#tempQty");	
	var minQty = $(form).find("#minQty");
	if(common.isEmpty(value)){
		alert("수량은 숫자로 입력하셔야합니다.");
		return;
	}
	if(!$.isNumeric(value)){
		qty.val(qty.val());
		return;
	}
	if(qty.val() <= 0){
		alert("수량은 1이상입니다.");
		qty.val("1");
		return;
	}else if(qty.val() < minQty.val()){
		alert("주문 최소수량은 "+minQty.val()+"이상입니다.");
		qty.val(minQty.val());
		return;
	}
	
	oms.changeOptionLayer.maxStockCheck(form);
	
	var cartTypeCd = $("#selCartTypeCd").val();
}

//단품 변경
var chgSaleproduct = function(cartProductNo,productId,value,saleproductName) {
	var form = $("#cartForm"+cartProductNo);
	//단품 선택
	var newValue = value;
	var orgId = "saleproductId";	
	
	var orgValue = form.find("#"+orgId).val();
	
	if(newValue != orgValue){
		form.find("#"+productId+"_newSaleproductId").val(newValue);		
	}
	
// 	console.log(productId);
// 	console.log(subProductId);
// 	console.log(newValue);	
// 	console.log(form);
	
	saveCart(form,saleproductName,value);
}

//set 단품 변경
var chgSaleproductSet = function(formName) {
	var form = "#cartForm"+formName;
	
	var subProducts = $(form+" input[name=subProductId]");
// 	console.log(subProducts);	
	
	for(var i=0;i<subProducts.length;i++){
		var subProductId = subProducts[i].value;
		
// 		console.log(subProductId);
		
		var newValue = $(form).find("#"+subProductId+"_orgSaleproductId").val();
		var orgValue = $(form).find("#"+subProductId+"_saleproductId").val();
		
		if(newValue != orgValue){
			$(form).find("#"+subProductId+"_newSaleproductId").val(newValue);					
		}
// 		console.log(subProductId);
// 		console.log(newValue);
// 		console.log(orgValue);
	}
	
	$(form).find("#qty").val($(form).find("#tempQty").val())
	
	saveCart(form);
}

var afterChgSaleprouct = function(form,saleproductName,saleproductId){
	
	var cartProductTypeCd = $(form).find("#cartProductTypeCd").val();
	var cartTypeCd = $(form).find("#cartTypeCd").val();

	if("CART_TYPE_CD.PICKUP" == cartTypeCd){
		$(form).find("#orgOffshopId").val($(form).find("#offshopId").val());
	}
	
	var qty = $(form).find("#qty").val();
// 	console.log(form);
// 	console.log(cartProductTypeCd)
	if("CART_PRODUCT_TYPE_CD.SET" == cartProductTypeCd){
		if(common.isNotEmpty(saleproductId)){
			var subProducts = $(form+" input[name=subProductId]");
			
			for(var i=0;i<subProducts.length;i++){
				var subProductId = subProducts[i].value;
				var subProductName = $(form).find("#subProductName"+subProductId).val();
				var setQty = $(form).find("#setQty"+subProductId).val();
				
				var newValue = $(form).find("#"+subProductId+"_orgSaleproductId option:selected").text();			
				var txt = "<b>"+subProductName+" : "+newValue + "</b>("+ setQty+"개)";			
				$(form).find("#optionTxt"+subProductId).html(txt);
				$(form).find("#"+subProductId+"_newSaleproductId").val('');
			} 
		}
		$(form).find("#optionLayer").hide();

		if(global.channel.isMobile == 'true'){
			var cartPrdouctNo = $(form).find("#cartProductNo").val();
			var btnid = "#optionBtn"+cartPrdouctNo;
			var stateBoxid = "#stateBox"+cartPrdouctNo;
			var flag = $(btnid).hasClass("on");				
			
			var optionH = 0;
			$(stateBoxid).attr("style","padding-bottom :"+optionH+"px;");
			$(btnid).removeClass("on");
		}

	}else{
		if(common.isNotEmpty(saleproductId)){
			var productId = $(form).find("#productId").val();
	// 		$(form).find("#addSalePrice").html(addSalePrice);
			$(form).find("#optionTxt").html(saleproductName);
			$(form).find("#saleproductId").val(saleproductId);		
			$(form).find("#"+productId+"_newSaleproductId").val('');
		}
	}
	
	
	$(form).find("#qtyTxt").html(qty+"개");
	
	calc();
}

//장바구니저장
var saveCart = function(form,saleproductName,saleproductId){		
	
	var data = $(form).serialize();
	var cartTypeCd = $(form).find("#cartTypeCd").val();
	
	common.showLoadingBar();
	
	$.ajax({
		url : "/api/oms/cart/save",
		type : "POST",
		data : data
	}).done(function(response){
		if(response.RESULT == "SUCCESS"){
			//alert("성공.");
			if("CART_TYPE_CD.PICKUP" == cartTypeCd){
				window.location.reload();
			}else{
				afterChgSaleprouct(form,saleproductName,saleproductId);			
			}
		}else{
// 			common.hideLoadingBar();
			alert(response.MESSAGE);			
			window.locaion.reload();
		}
		common.hideLoadingBar();
	}).fail(function(){
		common.hideLoadingBar();
	});
}

//checklist
var fn_checklist = function(cartProductNo,strYn,pickupYn,chgYn){
	var chklist=[];
	var chkliststr = "";
	
// 	console.log(cartProductNo);
// 	console.log(chgYn);
	
	if(cartProductNo != null && cartProductNo != ''){
		var subFix = "";
		if(pickupYn){
			subFix = "_" + $("#cartForm"+cartProductNo).find("select[name=pickupReserveDt] option:selected").val();
		}
		chkliststr += cartProductNo+subFix+",";		
		chklist.push({"cartProductNo":cartProductNo+subFix})		
	}else{		
		if(chgYn){	//변경 check
			$("input[name=chk]").each(function(){
				var changeSalePriceFlag = $("#cartForm"+this.value).find("#changeSalePriceFlag").val();
				if(changeSalePriceFlag == 'Y'){
					var subFix = "";
					if(pickupYn){
						subFix = "_" + $("#cartForm"+this.value).find("select[name=pickupReserveDt] option:selected").val();
					}
					chkliststr += this.value+subFix+",";	
					chklist.push({"cartProductNo":this.value+subFix});
				}
// 				console.log(changeSalePriceFlag);
// 				console.log(chkliststr);
			});
		}else{
			$("input[name=chk]:checked").each(function(){
				var deliveryPolicyNo = $("#cartForm"+this.value).find("#deliveryPolicyNo").val();
				
				if(deliveryPolicyNo != '-1'){
					var subFix = "";
					if(pickupYn){
						subFix = "_" + $("#cartForm"+this.value).find("select[name=pickupReserveDt] option:selected").val();
					}
					
					chkliststr += this.value+subFix+",";	
					chklist.push({"cartProductNo":this.value+subFix});
				}
								
// 				console.log(chkliststr);
			});
		}
	}
	if(strYn){		
		return chkliststr.substr(0,chkliststr.length-1);
	}else{
		return chklist;
	}
}

var afterDel = function(){

	var cartProductNos = $("input[name=cartProductNo]");
	
	if(cartProductNos.length == 0){
		
	}else{
		calc();
	}
}

	
//삭제
var deleteCart = function(cartProductNo){
// 	return false;
	var chklist;
	if(cartProductNo == 'ALL'){
		chklist = fn_checklist(null,false);
	}else{
		chklist = fn_checklist(cartProductNo);	
// 		var keepYn = $("#cartForm"+cartProductNo).find("#keepYn").val();
// 		if(keepYn == "Y"){
// 			if(!window.confirm("계속보관 상품입니다. 삭제하시겠습니까?")){
// 				return;
// 			}
// 		}
	}
	
	
// 	console.log(chklist);

	if(confirm("삭제 하시겠습니까?")){
		for(var i=0;i<chklist.length;i++){
			$("#cartForm"+chklist[i].cartProductNo).remove();
		}
	}else{		
		return;
	}
	
	common.showLoadingBar();
	$.ajax({
 		contentType : "application/json; charset=UTF-8",
		url : "/api/oms/cart/delete",
		type : "POST",		
		data : JSON.stringify(chklist)
	}).done(function(response){
		if(response.RESULT == "SUCCESS"){
// 			alert("성공.");			
			window.location.reload();
// 			afterDel();
		}else{
			alert(response.MESSAGE);
			common.hideLoadingBar();
		}
	}).fail(function(){common.hideLoadingBar();	});				
}

//품절상품 전체 삭제
var deleteEmptyCart = function(){
	var chklist = [];
	$("input[name=chk]").each(function(){
		var deliveryPolicyNo = $("#cartForm"+this.value).find("#deliveryPolicyNo").val();
		
		if(deliveryPolicyNo == '-1'){
			chklist.push({"cartProductNo":this.value});
		}
						
//			console.log(chkliststr);
	});
	
// 	console.log(chklist);
// 	return;
	common.showLoadingBar();
	$.ajax({
 		contentType : "application/json; charset=UTF-8",
		url : "/api/oms/cart/delete",
		type : "POST",		
		data : JSON.stringify(chklist)
	}).done(function(response){
		if(response.RESULT == "SUCCESS"){
// 			alert("성공.");			
			window.location.reload();
// 			afterDel();
		}else{
			alert(response.MESSAGE);
			common.hideLoadingBar();
		}		
	}).fail(function(){common.hideLoadingBar();});
}
//주문
var cartOrder = function(type,cartProductNo){	
	var chklist;
	
	var cartTypeCd = $("#selCartTypeCd").val();
	
	var flag = true;
	
	if(type == "ALL"){
		chklist = "";
		$("input[name=chk]").each(function(){
			var deliveryPolicyNo = $("#cartForm"+$(this).val()).find("#deliveryPolicyNo").val();
			if(deliveryPolicyNo != '-1'){
				var subFix = "";
				if(cartTypeCd == "CART_TYPE_CD.PICKUP"){
					var pickupReseveDt = $("#cartForm"+$(this).val()).find("select[name=pickupReserveDt] option:selected").val();
					if(common.isEmpty(pickupReseveDt)){
						alert("픽업 예정일을 선택하세요.");
						$(this).focus();
						flag = false;
						return false;
					}
					subFix = "_" + pickupReseveDt;
				}
				chklist += $(this).val()+subFix+",";		
			}
		});		
		chklist = chklist.substr(0,chklist.length-1);
	}else if(type == "ONE"){
		var subFix = "";
		if(cartTypeCd == "CART_TYPE_CD.PICKUP"){
			var pickupReseveDt = $("#cartForm"+cartProductNo).find("select[name=pickupReserveDt] option:selected").val();
			if(common.isEmpty(pickupReseveDt)){
				alert("픽업 예정일을 선택하세요.");				
				flag = false;
				return false;
			}
			
			subFix = "_" +pickupReseveDt;
		}
		chklist = cartProductNo+subFix;
	}else if(type == "SELECT"){
		if(cartTypeCd == "CART_TYPE_CD.PICKUP"){
			chklist = fn_checklist("",true,true);
		}else{
			chklist = fn_checklist("",true);
		}
	} 	
	
	if(flag && $.trim(chklist.replace(/,/gi,'')) == ''){
		alert("선택된 상품이 없습니다.");
		return;
	}
	if(!flag){
		return;
	}
	
	
// 	console.log(chklist);
	
	$("#targetForm #cartProductNos").val(chklist);
// 	return;
	if(cartTypeCd == "CART_TYPE_CD.GENERAL"){
		oms.cartOrder(chklist);
// 		$("#targetForm").attr("action","/oms/order/sheet");	
// 		$("#targetForm").submit();
	}else if(cartTypeCd == "CART_TYPE_CD.PICKUP"){
		this.savePickup();		
	}else if(cartTypeCd == "CART_TYPE_CD.REGULARDELIVERY"){
		$("#targetForm").attr("action","/oms/order/regulardelivery");
		$("#targetForm").submit();
	}
	 			
}

//매장픽업신청.
var savePickup = function(){
	
	var form = $("#targetForm");
	common.showLoadingBar();
	$.ajax({
//  		contentType : "application/json; charset=UTF-8",
		url : "/api/oms/order/pickup/save",
		type : "POST",		
		data : form.serialize()
	}).done(function(response){
		if(response.RESULT == "SUCCESS"){
// 			alert("성공.");
			$("#orderPickupCompleteForm").find("#pickupId").val(response.pickupId);
			$("#orderPickupCompleteForm").submit();			
		}else{
			alert(response.MESSAGE);
			window.location.reload();
		}		
		common.hideLoadingBar();
	}).fail(function(){common.hideLoadingBar();});
}

//매장변경.
var chgOffshop = function(cartProductNo){
	var params = {};
	var form = $("#cartForm"+cartProductNo);
	var saleproductId = form.find("#saleproductId").val();
	var offshopId = form.find("#offshopId").val();
	
	//params.push({saleproductId : saleproductId, offshopId : offshopId, selectedQty : 0});
	params[saleproductId] = { offshopId : offshopId, selectedQty : $(form).find("#qty").val() };
	ccs.layer.pickupLayer.open(params, function(data){
		//var newOffshopId = data[saleproductId];
		//var newOffshopName = data[saleproductId+"Text"];
		
		var shopObj = data[saleproductId];
// 		console.log(shopObj);
		form.find("#offshopId").val(shopObj.offshopId);
		form.find("#offshopName").html(shopObj.shopName);
		saveCart("#cartForm"+cartProductNo);				
	});
}

//탭변경
var chgTab = function(type){
	$(".tabBox a, .tabBox1 a, .tabBox2 a").off("click");
	common.showLoadingBar();
	location.href = "/oms/cart/list?cartTypeCd=CART_TYPE_CD."+type;	
}

//찜
var saveZzim = function(){
	
	var wishlists = [];
	$("input[name=chk]:checked").each(function(){
		
		var cartProductNo = $(this).val();
		var form = "cartForm"+cartProductNo;
		var productId = $("#"+form+" #productId").val();
		var saleproductId = $("#"+form+" #saleproductId").val();
		var cartProductTypeCd = $("#"+form+" #cartProductTypeCd").val();
		var subProductId;
		
// 		console.log(cartProductTypeCd);
		
		if(cartProductTypeCd == 'CART_PRODUCT_TYPE_CD.SET'){
			var subProducts = $("#"+form+" input[name=subProductId]");
			
			var subWishlists = [];
			
// 			console.log(subProducts);			
			subProducts.each(function(){
				var value = $(this).val();
				var subWishlist = new mms.common.Wishlist(value,$("#"+form).find("#"+value+"_saleproductId").val(),null);
				subWishlists.push(subWishlist);
			})
			var mmsWishlist = new mms.common.Wishlist(productId,saleproductId,subWishlists);
			wishlists.push(mmsWishlist);						
		}else{			
			var mmsWishlist = new mms.common.Wishlist(productId,saleproductId,null);
			wishlists.push(mmsWishlist);			
		}					
				
	});	

	mms.common.saveWishlist(wishlists,function(response){
// 		alert("저장하였습니다.");
	});
}

//계속 보관
var keep = function(cartProductNo){
	var keepYn = $("#cartForm"+cartProductNo+" #keepYn").val();
	var text = "";
	if(keepYn == "Y"){
		keepYn = "N";		
	}else{
		keepYn = "Y";
	}
	
	var data = {"cartProductNo" : cartProductNo,"keepYn":keepYn};
	common.showLoadingBar();
	$.ajax({
 		contentType : "application/json; charset=UTF-8",
		url : "/api/oms/cart/keep",
		type : "POST",		
		data : JSON.stringify(data)
	}).done(function(response){
		if(response.RESULT == "SUCCESS"){
			if(keepYn == "Y"){
				$("#cartForm"+cartProductNo).find("#keepText").html("보관취소");
				$("#cartForm"+cartProductNo).find("#keepCs").addClass("sWhite1");
				$("#cartForm"+cartProductNo).find("#keepYn").val("Y");
			}else{
				$("#cartForm"+cartProductNo).find("#keepText").html("계속보관");
				$("#cartForm"+cartProductNo).find("#keepCs").removeClass("sWhite1");
				$("#cartForm"+cartProductNo).find("#keepYn").val("N");
			}			
			//alert("저장되었습니다.");
			//window.location.reload();				
		}else{
			alert(response.MESSAGE);
		}		
		common.hideLoadingBar();
	}).fail(function(){common.hideLoadingBar();});
}

//매장픽업 지도
var openOffshopMap = function(cartProductNo){
	var form  = $("#cartForm"+cartProductNo);		
	
	var html = '\
				<dl>\
					<dt>'+form.find("#offshopName").html()+'</dt>\
					<dd>\
						'+form.find("#offshopAddress1").val()+'<br />\
						'+form.find("#offshopAddress2").val()+'\
					</dd>\
					<dd>\
						휴점일 : '+form.find("#holidayInfo").val()+'\
						<a href="tel:'+form.find("#managerPhone").val()+'" class="btn_imgTel">\
							<em>'+form.find("#managerPhone").val()+'</em>\
						</a>\
					</dd>\
				</dl>\
				\
				<div class="shop_map" id="shop_map" style="">\
				</div>\
				\
				<p class="txt_info">\
				'+form.find("#addressInfo").val()+'\
				</p>\
				';
				
	$("#layer_info").html(html);	
	
	common.map("shop_map",form.find("#latitude").val(), form.find("#longitude").val());			   
}

var linkProduct = function(productId){	
	ccs.link.product.detail(productId)
}

$(document).ready(function(){
	//추천상품
	recommand();
	

	$(".tr_box .chk_all").off();
// 	.on("change", function(e){
// 		var target_tag = $(this).closest(".tr_box").parent().next();

// 		if( $(this).prop("checked") ){
// 			$(" .chk_style1 input", target_tag).each(function() {
// 				$(this).prop("checked", true).parent().addClass("selected");
// 			});
// 		}else{
// 			$(" .chk_style1 input", target_tag).each(function() {
// 				$(this).prop("checked", false).parent().removeClass("selected");
// 			});
// 		}
// 		calc();
// 	});
	
	common.hideLoadingBar();

})

var recommand = function(){
	var productStr = "";
	
	var chklist = $("input[name=chk]");
	
	for(var i=0;i<chklist.length;i++){
		var cartProductNo = chklist[i].value;
		var productId = $("#cartForm"+cartProductNo).find("#productId").val();
		if(i == 0){
			productStr = productId;
		}else{
			productStr += productStr + "," + productId;
		}
	}
	
	var cartTotalCnt = '${cartCnt.generalCnt}';
	var cartTypeCd = $("#selCartTypeCd").val();
	if(cartTypeCd == 'CART_TYPE_CD.REGULARDELIVERY'){
		cartTotalCnt = '${cartCnt.regulardeliveryCnt}';
	}else if(cartTypeCd == 'CART_TYPE_CD.PICKUP'){
		cartTotalCnt = '${cartCnt.pickupCnt}';
	}
	if (Number(cartTotalCnt) > 0) {
		var param = {recType:'a001',size:4,iids:productStr}
	} else {
		var param = {recType:'x',size:4,iids:productStr}
	}
	
	
	// 추천상품
	var swiperFn = function(){
		if(global.channel.isMobile == "true"){
			swiperCon('cartSwiper_pordList', '2'); //	
		}else{
			swiperCon('cartSwiper_pordList', 400, 4, 12, false, true, 4);
		}
	}
	pms.common.getRecommendationProductList($('[name=cartBestArea]'),param, swiperFn);	
}

//장바구니 변경사항있을때.
var changeTotalSalePrice = function(){
	
	alert("장바구니 가격이 변경되었습니다.");
	var cartProductNos = fn_checklist(null,true,false,true);
	var cartTypeCd = $("#selCartTypeCd").val();
	var data = {"cartTypeCd":cartTypeCd,"priceChange":true,"cartProductNos" : cartProductNos};		
	
// 	console.log(data);
// 	return;
	
	common.showLoadingBar();
	$.ajax({
		contentType : "application/json; charset=UTF-8",
		url : "/api/oms/cart/saveList",
		type : "POST",
		data : JSON.stringify(data)
	}).done(function(response){
		if(response.RESULT == "SUCCESS"){
// 			alert("성공.");			
			window.location.reload();
		}else{
			alert(response.MESSAGE);
			common.hideLoadingBar();
		}				
	}).fail(function(){
		common.hideLoadingBar();
	});
}

//장바구니 유형 변경
var transferGeneral = function(cartProductNo){
	
	if(confirm("택배로 받기를 하시면 일반 장바구니로 상품이 이동합니다.\n택배로 받으시겠습니까?")){
// 		var data = {"cartProductNo" : cartProductNo,"cartTypeCd":"CART_TYPE_CD.GENERAL"};
		var data = $("#cartForm"+cartProductNo).serialize(); 
		common.showLoadingBar();
		$.ajax({			
			url : "/api/oms/cart/transfer",
			type : "POST",
			data : data
		}).done(function(response){
			if(response.RESULT == "SUCCESS"){
				//alert("성공.");
				window.location.reload();
			}else{
				alert(response.MESSAGE);
				common.hideLoadingBar();
			}		
		}).fail(function(){
			common.hideLoadingBar();
		});
	}
}

var changeOptionLayer = function(cartProductNo){
	
	var form = $("#cartForm"+cartProductNo);
	
	var productId = form.find("#productId").val();
	var saleproductId = form.find("#saleproductId").val();
	var qty = form.find("#qty").val();
	var cartProductTypeCd = form.find("#cartProductTypeCd").val();
	var pickupProduct = "";
	var offshopId = "";
	
	var cartTypeCd = $("#selCartTypeCd").val();
// 	console.log(cartTypeCd);
	if("CART_TYPE_CD.PICKUP" == cartTypeCd){
		pickupProduct = "Y";
		offshopId = form.find("#offshopId").val();
	}
	
	if("CART_PRODUCT_TYPE_CD.SET" == cartProductTypeCd){
		form.find("#tempQty").val(qty);
		$(".mobile .stateBox .btn_tb_option").off("click");
		if(global.channel.isMobile == "true"){						
			var btnid = "#optionBtn"+cartProductNo;
			var stateBoxid = "#stateBox"+cartProductNo;
			var flag = $(btnid).hasClass("on");

			if(typeof flag == 'undefined'|| !flag){
				form.find("#optionLayer").show();
				
				var optionH = form.find("#optionLayer").outerHeight() + 10;
				$(stateBoxid).attr("style","padding-bottom :"+optionH+"px;");
				$(btnid).addClass("on");
			} else {
				form.find("#optionLayer").hide();
				
				var optionH = 0;
				$(stateBoxid).attr("style","padding-bottom :"+optionH+"px;");
				$(btnid).removeClass("on");
			}										
		}else{
			form.find("#optionLayer").show();
		}
	}else{
		$(".mobile .stateBox .btn_tb_option").off("click");
		oms.changeOptionLayer.open(cartProductNo,productId, saleproductId, qty, pickupProduct, offshopId);
	}
	
}
var chgSelect = function(){}

$(".pc .stateBox .btn_tb_option").off("click").on("click", function(e){
	$(this).next(".ly_box").toggle();
});

var checkAll = function(obj){
	var chk = $(obj).prop("checked");
	
	$(".chk_all").each(function(){	
// 		console.log($(this));
		if(chk){
			$(this).prop("checked", true).parent().addClass("selected");
		}else{
			$(this).prop("checked", false).parent().removeClass("selected");
		}
	})
	$("input[name=chk]").each(function(){
// 		console.log($(this).prop("checked"));		
		if(chk){
			$(this).prop("checked", true).parent().addClass("selected");
		}else{
			$(this).prop("checked", false).parent().removeClass("selected");
		}
	});
	
	calc();
}

var checkAllSub = function(obj){
	
	var target_tag = $(".cart_box");
	if($("#selCartTypeCd").val() == "CART_TYPE_CD.GENERAL"){
		target_tag = $(obj).closest(".tr_box").parent().next();
	}
// console.log(target_tag);
	if( $(obj).prop("checked") ){
		$(" .chk_style1 input", target_tag).each(function() {
			$(this).prop("checked", true).parent().addClass("selected");
		});
		$(obj).parent().addClass("selected");
	}else{
		$(" .chk_style1 input", target_tag).each(function() {
			$(this).prop("checked", false).parent().removeClass("selected");
		});		
		$(obj).parent().removeClass("selected");
	}	
	calc();
}

var allChkcheck = function(){
	var chk = true;
	$("input[name=chk]").each(function(){
// 		console.log($(this).prop("checked"));	
		var cNo = $(this).val();
		var dNo = $("#cartForm"+cNo).find("#deliveryPolicyNo").val();
		if(dNo != '-1'){
			if(!$(this).prop("checked")){
				chk = false;
			}
		}
	});
// 	console.log(chk);
	if(chk){
		$("#chk_all_1").prop("checked",true).parent().addClass("selected");
		$("#chk_all_2").prop("checked",true).parent().addClass("selected");
		$("#chk_all_3").prop("checked",true).parent().addClass("selected");
	}else{
		$("#chk_all_1").prop("checked",false).parent().removeClass("selected");
		$("#chk_all_2").prop("checked",false).parent().removeClass("selected");
		$("#chk_all_3").prop("checked",false).parent().removeClass("selected");		
	}
	
}

var checkSingle = function(obj){
	var cartProductNo = $(obj).val();
	var deliveryPolicyNo = $("#cartForm"+cartProductNo).find("#deliveryPolicyNo").val();

	var chk = true;
	
	if(!$(obj).prop("checked")){
		chk = false;
	}else{
		
		$("input[name=chk]").each(function(){		
			var cNo = $(this).val();
			var dNo = $("#cartForm"+cNo).find("#deliveryPolicyNo").val();
			if(dNo != '-1'){
				if($("#selCartTypeCd").val() == "CART_TYPE_CD.GENERAL"){
					if(!$(this).prop("checked") && dNo == deliveryPolicyNo){
						chk = false;
					}
				}else{				
					if(!$(this).prop("checked")){
						chk = false;
					}
				}
			}
		});
		
	}
	
	if($("#selCartTypeCd").val() == "CART_TYPE_CD.GENERAL"){
		if(chk){
			$("#chk_all"+deliveryPolicyNo).prop("checked",true).parent().addClass("selected");
		}else{
			$("#chk_all"+deliveryPolicyNo).prop("checked",false).parent().removeClass("selected");
		}		
	}else{
		$(".chk_all").each(function(){
			if(chk){
				$(this).prop("checked",true).parent().addClass("selected");
			}else{
				$(this).prop("checked",false).parent().removeClass("selected");
			}	
		});
	}
		
	calc();
}
</script>

<form action="/oms/order/pickupComplete" name="orderPickupCompleteForm" id="orderPickupCompleteForm" method="post">
	<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }"/>
	<input type="hidden" name="pickupId" id="pickupId" value=""/>
</form>
<form action="" name="targetForm" id="targetForm" method="post">
	<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }"/>
	<input type="hidden" name="cartProductNos" id="cartProductNos" value=""/>
</form>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="장바구니" name="pageNavi"/>
</jsp:include>
<div class="inner">
	<div class="cart_box">
		<div class="step">
			<h3 class="title_type1">
				장바구니
			</h3>
			<ol>
				<li class="active" ><span class="step_01">01</span>장바구니</li>
				<li ><span class="step_02">02</span>주문/결제</li>
				<li><span class="step_03">03</span>주문완료</li>
			</ol>
		</div>

		<!-- ### 탭 버튼 : 2016.08.24 수정 ### -->
			<input type="hidden" id="selCartTypeCd" value="${cartCnt.cartTypeCd }"/>
		<ul class="tabBox tab3ea tp1">
			<li class="<c:if test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.GENERAL' }">on</c:if>" ><a href="#none" onclick="javascript:chgTab('GENERAL')">일반구매<em>(${cartCnt.generalCnt })</em></a></li>					
			<c:if test="${memberYn == 'Y' }">				
			<li class="<c:if test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.REGULARDELIVERY' }">on</c:if>" ><a href="#none" onclick="javascript:chgTab('REGULARDELIVERY')">정기배송<em>(${cartCnt.regulardeliveryCnt })</em></a></li>
			<li class="<c:if test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.PICKUP' }">on</c:if>" ><a href="#none"  onclick="javascript:chgTab('PICKUP')">매장픽업<em>(${cartCnt.pickupCnt })</em></a></li>
			</c:if>			
		</ul>
		<!-- ### //탭 버튼 : 2016.08.24 수정 ### -->
		
		<%--///////////// 장바구니 상품없을때 start ////////////////--%>
		<c:set var="emptyFlag" value="false"/>
		<c:if test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.GENERAL' && cartCnt.generalCnt == 0}">
			<c:set var="emptyFlag" value="true"/>
		</c:if>
		<c:if test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.REGULARDELIVERY' && cartCnt.regulardeliveryCnt == 0}">
			<c:set var="emptyFlag" value="true"/>
		</c:if>
		<c:if test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.PICKUP' && cartCnt.pickupCnt == 0}">
			<c:set var="emptyFlag" value="true"/>
		</c:if>
		<c:if test="${emptyFlag == 'true' }">
		<div class="tab_con tab_conOn">
			<!-- ### 테이블 헤더 ### -->
			<div class="div_tb_thead3">
				<div class="tr_box">
					<span class="col1">					
						<label class="chk_style1">
							<em>
								<input type="checkbox" id="chk_all" value="" class="chk_all" ${chk }/>
							</em>
							<span>전체선택</span>
						</label>
					</span>
					<span class="col2 <c:if test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.PICKUP' }">col2_1</c:if>">상품/옵션정보</span>
<!-- 					<span class="col3">수량</span> -->
					<span class="col4">상품금액</span>
					<c:if test="${cartCnt.cartTypeCd != 'CART_TYPE_CD.PICKUP' }">
					<span class="col5">배송비</span>
					</c:if>
					<span class="col6">선택</span>
				</div>
			</div>
			<!-- ### //테이블 헤더 ### -->

			<!-- ### 테이블 바디 ### -->
			<ul class="div_tb_tbody3">
				<li class="empty">
					<div class="tr_box">
						<div class="td_box col99">
							장바구니에 담긴 상품이 없습니다.<br />
							<a href="#none" class="btn_sStyle1" onclick="javascript:ccs.link.common.main()">계속쇼핑하기</a>
						</div>
					</div>
				</li>
			</ul>
			<!-- ### //테이블 바디 ### -->
		</div>
		</c:if>
		<%--///////////// 장바구니 상품없을때 end ////////////////--%>	
		
		<c:if test="${cartList != null && fn:length(cartList) > 0 }">
		<!-- ### 일반구매 : 2016.08.24 수정 ### -->
		<div class="tab_con tab_conOn">
		
			<c:if test="${emptyFlag == 'false' }">
			<!-- ### 상품 삭제 버튼 : 2016.08.24 수정 ### -->
			<div class="choice_del">
				<div class="pc_only">
					<label class="chk_style1">
						<em>
							<input type="checkbox" value="" id="chk_all_1" class="chk_all" checked="checked" onclick="javascript:checkAll(this)"/>
						</em>
						<span>전체선택</span>
					</label>
					<a href="#none" class="btn_sStyle3 btn_choice_del" onclick="javascript:deleteCart();">선택상품 삭제</a>
					<!-- <a href="#none" class="btn_sStyle3 btn_disable_del" onclick="javascript:deleteEmptyCart();">품절상품 삭제</a> -->
					<label class="btn_jjim">
						<input type="checkbox" value="" onclick="javascript:saveZzim()"/>
						<em>찜</em>
					</label>
				</div>
	
				<div class="mo_only">
					<div class="floatL">
						<label class="chk_style1">
							<em>
								<input type="checkbox" value="" id="chk_all_2" class="chk_all" checked="checked" onclick="javascript:checkAll(this)"/>
							</em>
							<span>전체선택</span>
						</label>
					</div>
	
					<div class="floatR">
						<label class="btn_jjim">
							<input type="checkbox" value="" onclick="javascript:saveZzim()"/>
							<em>찜</em>
						</label>
	
						<a href="#none" class="btn_sStyle3 btn_allDel" onclick="javascript:deleteCart()">선택 삭제</a>
					</div>
				</div>
			</div>
			<!-- ### //상품 삭제 버튼 : 2016.08.24 수정 ### -->
			</c:if>
			
		<c:set var="subTotalPrice" value="0"/>
		<c:set var="subTotalDcPrice" value="0"/>
		<c:set var="totalPrice" value="0"/>
		<c:set var="totalDcPrice" value="0"/>
		<c:set var="subTotalDeliveryFee" value="0"/>
		<c:set var="totalDeliveryFee" value="0"/>
		<c:set var="totalDeliveryFeeFreeYn" value="N"/>
		<c:set var="changeFlag" value="N"/>
		<c:set var="businessId" value=""/>
		
		<c:forEach items="${cartList }" var="cart" varStatus="status">			
			<c:if test="${cart.deliveryFeeFreeYn == 'Y' }">
				<c:set var="totalDeliveryFeeFreeYn" value="Y"/>						
			</c:if>
			
			<c:set var="headerFlag" value="N"/>						
			
			<c:if test="${cartCnt.cartTypeCd != 'CART_TYPE_CD.GENERAL' || cart.deliveryPolicyNo != preDeliveryPolicyNo || status.first}">
					
			<c:if test="${cart.deliveryPolicyNo == -1 && cart.deliveryPolicyNo != preDeliveryPolicyNo}"><!-- 품절 header -->
			<div class="soldOut">
				<div class="slide_tit slideHide">
					<a href="#none" class="evt_tit">품절/판매종료 상품</a>
					<a href="#none" class="btn_sStyle1 btn_right btn_disable_allDel" onclick="javascript:deleteEmptyCart();">전체삭제</a>
				</div>				
			</c:if>							
			
			<c:set var="chk" value=""/>
			<c:if test="${cart.deliveryPolicyNo != -1 }">
				<c:set var="chk" value="checked"/>
			</c:if>

			<c:if test="${status.first || cartCnt.cartTypeCd == 'CART_TYPE_CD.GENERAL' }">
			<!-- ### 테이블 헤더 ### -->
			<div class="div_tb_thead3">
				<div class="tr_box">
					<span class="col1">					
						<label class="chk_style1">
							<em>
								<input type="checkbox" id="chk_all${cart.deliveryPolicyNo }" value="" onclick="javascript:checkAllSub(this)" class="chk_all" ${chk }/>
							</em>
							<span>전체선택</span>
						</label>
					</span>
					<span class="col2 <c:if test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.PICKUP' }">col2_1</c:if>">상품/옵션정보</span>
<!-- 					<span class="col3">수량</span> -->
					<span class="col4">상품금액</span>
					<c:if test="${cartCnt.cartTypeCd != 'CART_TYPE_CD.PICKUP' }">
					<span class="col5">배송비</span>
					</c:if>
					<span class="col6">선택</span>
				</div>
			</div>
			<!-- ### //테이블 헤더 ### -->
			</c:if>
			
			<!-- ### 테이블 바디 ### -->
			<ul class="div_tb_tbody3">								
				<li >	
			<c:set var="headerFlag" value="Y"/>
			</c:if>
			<c:if test="${headerFlag != 'Y' && cart.deliveryPolicyNo == -1 }">
			<li >
			</c:if>
			
			<%--///////////// 장바구니 form start ////////////////--%>
			<form name="cartForm" id="cartForm${cart.cartProductNo}">
			<input type="hidden" id="cartTF" name="cartTF" value="true"/>
			<input type="hidden" id="cartId" name="cartId" value="${cart.cartId }"/>
			<input type="hidden" name="cartProductNo" id="cartProductNo" value="${cart.cartProductNo }"/>
			<input type="hidden" id="productId" name="productId" value="${cart.productId }"/>
			<input type="hidden" id="saleproductId" name="saleproductId" value="${cart.saleproductId }"/>
			<input type="hidden" id="channelId" name="channelId" value="${cart.channelId }"/>
			<input type="hidden" id="cartTypeCd" name="cartTypeCd" value="${cart.cartTypeCd }"/>
			<input type="hidden" id="cartProductTypeCd" name="cartProductTypeCd"  value="${cart.cartProductTypeCd }"/>
			<input type="hidden" id="orgOffshopId" name="orgOffshopId"  value="${cart.offshopId }"/>
			<input type="hidden" id="offshopId" name="offshopId"  value="${cart.offshopId }"/>
			<input type="hidden" id="styleNo" name="styleNo"  value="${cart.styleNo }"/>
			
			<input type="hidden" id="saleStateCd" name="saleStateCd"  value="${cart.saleStateCd }"/>
			<input type="hidden" id="deliveryPolicyNo" name="deliveryPolicyNo"  value="${cart.deliveryPolicyNo }"/>					
			
			<input type="hidden" id="qty" name="qty"  value="${cart.qty }"/>
			<!-- 원가격 -->
			<input type="hidden" id="orgTotalSalePrice" name="orgTotalSalePrice"  value="${cart.orgTotalSalePrice}"/>
			<!-- 원가격 -->
			<!-- 딜적용된가격 -->
			<input type="hidden" id="salePrice" name="salePrice"  value="${cart.salePrice}"/>
			<input type="hidden" id="addSalePrice" name="addSalePrice"  value="${cart.addSalePrice}"/>
			<!-- 딜적용된가격 -->
			<!-- 딜 및 쿠폰적용된가격 -->
			<input type="hidden" id="totalSalePrice" name="totalSalePrice"  value="${cart.totalSalePrice}"/>
			<input type="hidden" id="cartTotalSalePrice" name="cartTotalSalePrice"  value="${cart.cartTotalSalePrice}"/>
			<!-- 딜 및 쿠폰적용된가격 -->
			
			<input type="hidden" id="dealId" name="dealId" value="${cart.dealId }"/>
			<input type="hidden" id="couponId" name="couponId"  value="${cart.couponId}"/>
			<input type="hidden" id="couponDcAmt" name="couponDcAmt"  value="${cart.couponDcAmt}"/>	
			<input type="hidden" id="singleApplyYn" name="singleApplyYn"  value="${cart.singleApplyYn}"/>					
			<input type="hidden" id="deliveryFeeFreeYn" name="deliveryFeeFreeYn"  value="${cart.deliveryFeeFreeYn}"/>
			
			<input type="hidden" id="minQty" name="minQty"  value="${cart.minQty}"/>
						
			<input type="hidden" id="offshopAddress1" value="${cart.ccsOffshop.address1 }"/>
			<input type="hidden" id="offshopAddress2" value="${cart.ccsOffshop.address2 }"/>
			<input type="hidden" id="latitude" value="${cart.ccsOffshop.latitude }"/>
			<input type="hidden" id="longitude" value="${cart.ccsOffshop.longitude }"/>
			<input type="hidden" id="longitude" value="${cart.ccsOffshop.longitude }"/>
			<input type="hidden" id="addressInfo" value="${cart.ccsOffshop.addressInfo }"/>
			<input type="hidden" id="managerPhone" value="${cart.ccsOffshop.managerPhone }"/>
			<input type="hidden" id="holidayInfo" value="${cart.ccsOffshop.holidayInfo }"/>
			
			<input type="hidden" id="maxStockQty" value="${cart.maxStockQty }">
			
			<c:set var="businessId" value="${cart.businessId }"/>
			
			<c:choose>
			<c:when test="${cart.totalSalePrice != cart.cartTotalSalePrice }">
				<c:set var="changeFlag" value="Y"/>
				<input type="hidden" id="changeSalePriceFlag" name="changeSalePriceFlag"  value="Y"/>
			</c:when>
			<c:otherwise>
				<input type="hidden" id="changeSalePriceFlag" name="changeSalePriceFlag"  value="N"/>
			</c:otherwise>
			</c:choose>	
						
			
			<c:set var="preMinDeliveryFreeAmt" value="${cart.minDeliveryFreeAmt }"/>
			<c:set var="preDeliveryFee" value="${cart.deliveryFee }"/>
						
					<div class="tr_box">
						<c:if test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.REGULARDELIVERY' }">
						<div class="colspan cols1">
						</c:if>
						
						<c:set var="chk" value=""/>
						<c:if test="${cart.deliveryPolicyNo != -1 }">
							<c:set var="chk" value="checked"/>							
						</c:if>
						<div class="col1">
							<label class="chk_style1 <c:if test="${cart.deliveryPolicyNo == -1 }">this_disabled</c:if>">
								<em>
									<input type="checkbox" name="chk" value="${cart.cartProductNo }" title="선택" ${chk }  onclick="javascript:checkSingle(this)"/>
								</em>
							</label>
						</div>

						<div class="col2 <c:if test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.PICKUP' }">col2_1</c:if>">
							<div class="positionR">
								<div class="prod_img" style="height: 90px;">
									<a href="#none" onclick="javascript:linkProduct('${cart.productId}')">
<!-- 										<img src="/resources/img/pc/temp/cart_img1.jpg" alt="" /> -->
										<tags:prdImgTag productId="${cart.productId}" size="90" alt="${cart.productName }"/>
										<c:if test="${cart.saleStateCd != 'SALE_STATE_CD.SALE' }">
										<span class="txt_soldout">
											<em>SOLD<br>OUT</em>
										</span>
										</c:if>
									</a>
								</div>

								<a href="#none" class="title" onclick="javascript:linkProduct('${cart.productId}')">
									<c:if test="${cart.totalSalePrice != cart.cartTotalSalePrice }">
									<span class="icon_type1 iconPurple1">가격변동</span>
									</c:if>
									<c:if test="${cart.brandName != null && fn:length(cart.brandName) > 0 }">
									[${cart.brandName }]
									</c:if>
									${cart.productName }
								</a>
								
								<c:set var="optionCnt" value="0"/>
								<c:choose>
								<%--///////////// SET 단품 start ////////////////--%>
								<c:when test="${cart.cartProductTypeCd == 'CART_PRODUCT_TYPE_CD.SET' }">
								<input type="hidden" id="setQty${sub.productId }" value="${sub.setQty }"/>
								<c:forEach items="${cart.omsCarts }" var="sub" varStatus="stat2">
								<input type="hidden" id="subProductName${sub.productId }" value="${sub.productName }"/>									
									<em class="option_txt">
										<i id="optionTxt${sub.productId }"><b>${sub.productName } : ${sub.saleproductName }</b>(${sub.setQty }개)</i>
									</em>
									<c:if test="${sub.optionYn == 'Y' }">
										<c:set var="optionCnt" value="${optionCnt + 1 }"/>
									</c:if>
								</c:forEach>
								</c:when>
								<%--///////////// SET 단품 end ////////////////--%>
								
								<%--///////////// 일반 단품 start ////////////////--%>
								<c:otherwise>
								
								<input type="hidden" id="${cart.productId }_newSaleproductId" name="newSaleproductId" value=""/>
								<input type="hidden" id="${cart.productId }_orgSaleproductId" name="orgSaleproductId" vlaue="${cart.saleproductId }">
								<c:if test="${cart.optionYn == 'Y' }">
								<c:set var="optionCnt" value="${optionCnt + 1 }"/>
								<em class="option_txt">
									<i id="optionTxt">${cart.saleproductName }</i>
								</em>
								</c:if>
								</c:otherwise>
								<%--///////////// 일반 단품 end ////////////////--%>
								</c:choose>
								
								<div class="piece">
									<span class="pieceNum" id="qtyTxt">${cart.qty }개</span>
									<span class="slash">/</span>
									<span class="piecePrice"><fmt:formatNumber value="${cart.totalSalePrice }" pattern="#,###"/><i>원</i></span>
								</div>
								
							</div>
						<c:if test="${cart.deliveryPolicyNo != -1 }">
							<%--///////////// 사은품 start ////////////////--%>
							<c:if test="${fn:length(cart.spsPresents) > 0 }">
<!-- 							<u class="gift_txt"> -->
<!-- 								<span class="btn_tb_gift"> -->
<!-- 									<span class="icon_type1 iconBlue3">사은품</span> -->
<%-- 									<c:forEach items="${cart.spsPresents }" var="sps" varStatus="stat">  --%>
<%-- 										<c:forEach items="${sps.spsPresentproducts }" var="spp"> --%>
<%-- 											<c:if test="${stat.index > 0 }">,</c:if>${spp.pmsProduct.name } --%>
<%-- 										</c:forEach> --%>
<%-- 									</c:forEach> --%>
<!-- 								</span> -->
<!-- 							</u> -->
								<c:forEach items="${cart.spsPresents }" var="sps" varStatus="stat"> 
									<c:forEach items="${sps.spsPresentproducts }" var="spp">
										<u class="gift_txt">
											<span class="btn_tb_gift">
												<span class="icon_type1 iconBlue3">사은품</span> ${spp.pmsProduct.name }
											</span>
										</u>
									</c:forEach>
								</c:forEach>
							</c:if>
							<c:if test="${cart.wrapYn == 'Y' }">
							<u class="gift_txt">
								<span class="btn_tb_gift">
									<span class="icon_type1 iconBlue3">선물포장</span> 신청가능상품 
									
									<!-- 16.11.01 : 팝업 - 선물포장 안내 -->
									<div class="sm_layer">
										<a href="#none" class="btn_infor btn_giftInfo">?</a>
										<!-- pc 전용 -->
										<div class="pop_wrap_sm sLayer_gift pc_only">
											<div class="pop_inner">
												<div class="pop_header type1">
													<h3 class="tit">선물포장 안내</h3>
												</div>
												<div class="pop_content">
													<ul class="notice notice_bgWhite">
														<li>
															선물포장은 포장 1건당 1,000원의 비용이 책정되는 유료서비스입니다.
														</li>
														<li>
															선물포장은 부피 기준으로 포장되므로,부피가 크거나 개별 케이스가 있어 포장이 용이하지 않은 상품에는 제공되지 않습니다.
														</li>
														<li>
															선물포장비는 박스당 비용이 부과되므로 여러 개 주문으로 기준부피 초과시 2개의 박스로 분리 포장되며, 포장비가 추가로 발생 할 수 있습니다.
														</li>
													</ul>

													<div class="exGift">
														<img src="/resources/img/pc/bg/sLayer_gift.jpg" alt="" />
													</div>
												</div>
												<button type="button" class="btn_x pc_btn_close">닫기</button>
											</div>
										</div>
										<!-- //pc 전용 -->
									</div>
									<!-- //16.11.01 : 팝업 - 선물포장 안내 -->
								</span>
							</u>
							</c:if>
						</c:if>
												
						</div>
						
					<c:choose>
						<%--///////////// 가격,버튼 노출(판매중) start ////////////////--%>
						<c:when test="${cart.saleStateCd == 'SALE_STATE_CD.SALE' }">
							<div class="col4">
								<span class="price">
									<em id="salePriceTxt"><fmt:formatNumber value="${cart.totalSalePrice * cart.qty }" pattern="#,###" /> <i>원</i></em>
								</span>
							</div>
							
							<c:if test="${isMobile && cartCnt.cartTypeCd == 'CART_TYPE_CD.PICKUP' && cart.deliveryPolicyNo != -1}">
								<div class="special sp_pickup mo_only">
									<dl>
										<dt>픽업매장</dt>
										<dd>
											<p id="offshopName">${cart.ccsOffshop.name }</p> 
											<a href="#none" class="btn_sStyle1" onclick="javascript:chgOffshop('${cart.cartProductNo}')">매장변경</a>
											<a href="#none" class="btn_storeMap1" onclick="javascript:mypage.offshop.offshopInfo('${cart.offshopId}')">매장위치</a>
		<%-- 								<a href="#none" class="btn_storeMap1 btn_shopPosition" onclick="javascript:openOffshopMap('${cart.cartProductNo}')">매장위치</a> --%>										
										</dd>
									</dl>
									<c:if test="${cart.deliveryPolicyNo > -1 }">
									<dl>
										<dt>픽업예정일</dt>
										<dd>
											<div class="select_box1">
												<label>선택</label>
												<select id="slc_pickup" name="pickupReserveDt">
													<option value="" selected>선택</option>
													<c:forEach items="${cart.ccsOffshop.openDays }" var="openDay" varStatus="st">
														<option value="${openDay.OPEN_DATE_VAL }">${openDay.OPEN_DATE_WEEK }</option>
													</c:forEach>
												</select>
											</div>
										</dd>
									</dl>
									</c:if>
								</div>
							</c:if>
						
							<c:if test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.REGULARDELIVERY' && cart.deliveryPolicyNo != -1 }">
							<div class="regular_set_box">							
								<dl class="regular_set">
									<dt>배송주기/횟수</dt>
									<dd class="half">
										<div class="select_box1">
											<label>2주에 한번</label>
											<select name="deliveryPeriodCd" id="deliveryPeriodCd" onchange="javascript:saveCart('#cartForm${cart.cartProductNo}')">											
												<option value="DELIVERY_PERIOD_CD.1WEEK" <c:if test="${cart.deliveryPeriodCd == 'DELIVERY_PERIOD_CD.1WEEK' }">selected="selected"</c:if> >1주에 한번</option>
												<option value="DELIVERY_PERIOD_CD.2WEEK" <c:if test="${cart.deliveryPeriodCd == 'DELIVERY_PERIOD_CD.2WEEK' }">selected="selected"</c:if> >2주에 한번</option>
												<option value="DELIVERY_PERIOD_CD.3WEEK" <c:if test="${cart.deliveryPeriodCd == 'DELIVERY_PERIOD_CD.3WEEK' }">selected="selected"</c:if> >3주에 한번</option>
												<option value="DELIVERY_PERIOD_CD.4WEEK" <c:if test="${cart.deliveryPeriodCd == 'DELIVERY_PERIOD_CD.4WEEK' }">selected="selected"</c:if> >4주에 한번</option>											
											</select>
										</div>
										<div class="select_box1">
											<label>${cart.deliveryCnt }회</label>
											<select name="deliveryCnt" id="deliveryCnt" onchange="javascript:saveCart('#cartForm${cart.cartProductNo}')">
												<c:forEach begin="${cart.regularDeliveryMinCnt }" end="${cart.regularDeliveryMaxCnt }" varStatus="st">
													<option value="${st.index }" <c:if test="${cart.deliveryCnt == st.index }">selected="selected"</c:if> >${st.index }회</option>
												</c:forEach>
											</select>
										</div>
									</dd>
								</dl>
								<dl class="regular_set">
									<dt>정기배송요일</dt>
									<dd>
										<div class="select_box1">
											<label>화요일</label>
											<select name="deliveryPeriodValue" id="deliveryPeriodValue" onchange="javascript:saveCart('#cartForm${cart.cartProductNo}')">
												<option value="3" <c:if test="${cart.deliveryPeriodValue == 3 }">selected="selected"</c:if> >화요일</option>
												<option value="4" <c:if test="${cart.deliveryPeriodValue == 4 }">selected="selected"</c:if>>수요일</option>
												<option value="5" <c:if test="${cart.deliveryPeriodValue == 5 }">selected="selected"</c:if>>목요일</option>
												<option value="6" <c:if test="${cart.deliveryPeriodValue == 6 }">selected="selected"</c:if>>금요일</option>
												<option value="7" <c:if test="${cart.deliveryPeriodValue == 7 }">selected="selected"</c:if>>토요일</option>
<%-- 												<option value="1" <c:if test="${cart.deliveryPeriodValue == 1 }">selected="selected"</c:if>>일요일</option> --%>
											</select>
										</div>
									</dd>
								</dl>
<!-- 								<dl class="count"> -->
<!-- 									<dt>배송횟수</dt> -->
<!-- 									<dd> -->
<!-- 										<div class="select_box1"> -->
<%-- 											<label>${cart.deliveryCnt }회</label> --%>
<!-- 											<select name="deliveryCnt" id="deliveryCnt"> -->
<%-- 												<c:forEach begin="${cart.regularDeliveryMinCnt }" end="${cart.regularDeliveryMaxCnt }" varStatus="st"> --%>
<%-- 													<option value="${st.index }" <c:if test="${cart.deliveryCnt == st.index }">selected="selected"</c:if> >${st.index }회</option> --%>
<%-- 												</c:forEach> --%>
<!-- 											</select> -->
<!-- 										</div> -->
<!-- 									</dd> -->
<!-- 								</dl> -->
<%-- 								<a href="#none" class="btn_sStyle1" onclick="javascript:saveCart('#cartForm${cart.cartProductNo}')">변경</a>  --%>
							</div>
							</c:if>
												
							<c:if test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.REGULARDELIVERY' }">
								</div>
							</c:if>
							
							<c:set var="subTotalPrice" value="${subTotalPrice + (cart.totalSalePrice * cart.qty)}"/>
							<c:set var="couponDcAmt" value="0"/>
							<c:choose>
							<c:when test="${cart.singleApplyYn == 'Y' }">
								<c:set var="couponDcAmt" value="${cart.couponDcAmt}"/>
							</c:when>
							<c:otherwise>
								<c:set var="couponDcAmt" value="${cart.couponDcAmt * cart.qty }"/>
							</c:otherwise>
							</c:choose>
							<c:set var="subTotalDcPrice" value="${subTotalDcPrice + couponDcAmt}"/>
						
							<c:if test="${cartCnt.cartTypeCd != 'CART_TYPE_CD.PICKUP' }">
								<!-- 셀 병합이 필요한 경우 rowspan > cell > vAlign 필요 -->
								<c:choose>
								<c:when test="${headerFlag == 'Y' && cartCnt.cartTypeCd != 'CART_TYPE_CD.REGULARDELIVERY'}">
								<div class="col5 rowspan">
									<div class="cell">
										<span class="vAlign delivery_price">
											<b>
<%-- 												${cart.minDeliveryFreeAmt} --%>
<%-- 												${cart.policyTotalPrice } --%>
<%-- 												${cart.policyDeliveryFeeFreeYn}  --%>
<%-- 												${cart.deliveryFee } --%>
												<c:choose>
												<c:when test="${cart.minDeliveryFreeAmt <= cart.policyTotalPrice || cart.policyDeliveryFeeFreeYn == 'Y' || cart.deliveryFee == 0}">
												무료
												</c:when>
												<c:otherwise>
												<fmt:formatNumber value="${cart.deliveryFee }" pattern="#,###"/>원
												</c:otherwise>
												</c:choose>
											</b>
											<c:if test="${!empty businessId}">									
											<a href="#none" onclick="javascript:ccs.link.display.sellerShop('${businessId}')">묶음배송 상품보기 &gt;</a>
											</c:if>									
										</span>
									</div>
								</div>
								</c:when>
								<c:otherwise>
								<div class="col5">
									<span class="delivery_price">
										<em>
											<b> 
<%-- 											${cart.minDeliveryFreeAmt} --%>
<%-- 												${cart.policyTotalPrice } --%>
<%-- 												${cart.policyDeliveryFeeFreeYn}  --%>
<%-- 												${cart.deliveryFee } --%>
												<c:choose>
												<c:when test="${cart.minDeliveryFreeAmt <= cart.policyTotalPrice || cart.policyDeliveryFeeFreeYn == 'Y' || cart.deliveryFee == 0}">
												무료
												</c:when>
												<c:otherwise>
												<fmt:formatNumber value="${cart.deliveryFee }" pattern="#,###"/>원
												</c:otherwise>
												</c:choose>
											</b>
											<c:if test="${!empty businessId}">
											<a href="#none" onclick="javascript:ccs.link.display.sellerShop('${businessId}')">묶음배송 상품보기 &gt;</a>
											</c:if>
										</em>
									</span>
								</div>
								</c:otherwise>
								</c:choose>
							</c:if>
						
							<div class="col6">
								<!-- 16.10.20 : div(stateBox) 추가 -->
								<div class="stateBox" id="stateBox${cart.cartProductNo }" style="">
									<!-- 16.09.26 : 옵션 버튼 추가 -->
									<div class="group_r">
								
										<c:choose>
										<c:when test="${cart.cartProductTypeCd == 'CART_PRODUCT_TYPE_CD.SET' }">
											<a href="#none" id="optionBtn${cart.cartProductNo }" class="btn_sStyle3 sGray2 btn_tb_option" onclick="javascript:changeOptionLayer('${cart.cartProductNo}')">옵션/수량변경</a>
											<div class="ly_box option_box" id="optionLayer" style="display: none;">
												<div class="con">
													<strong class="title">옵션/수량변경</strong>
													<c:set var="hidden" value="none"/>
													<c:if test="${optionCnt > 0 }">
														<c:set var="hidden" value="block"/>	
													</c:if>
													<dl style="display: ${hidden}">
														<c:forEach items="${cart.omsCarts }" var="sub" varStatus="stat2">
														
														<input type="hidden" name="subProductId" value="${sub.productId }"/>													
														<input type="hidden" id="${sub.productId }_saleproductId"  name="omsCarts[${stat2.index }].saleproductId" value="${sub.saleproductId }"/>
														<input type="hidden" id="${sub.productId }_SetProductId"  name="omsCarts[${stat2.index }].productId" value="${sub.productId }"/>
														<input type="hidden" id="${sub.productId }_newSaleproductId" name="omsCarts[${stat2.index }].newSaleproductId" value=""/>
													
														<dt>상품${stat2.index+1 }</dt>
														<dd>
															<p>${sub.productName }</p>
															<div class="select_box1">
																<label></label>
																<select id="${sub.productId }_orgSaleproductId" name="omsCarts[${stat2.index }].orgSaleproductId">
																	<c:forEach items="${sub.pmsSaleproducts }" var="subSel">
																		<option value="${subSel.saleproductId }" <c:if test="${sub.saleproductId == subSel.saleproductId }"> selected="selected"</c:if>>${subSel.name }</option>
																	</c:forEach>
																</select>
															</div>
														</dd>
														
														</c:forEach> 																																								
													</dl>
													
													<div class="user_action">
														<dl>
															<dt>수량</dt>
															<dd>
																<div class="quantity">
																	<button type="button" class="btn_minus" onclick="javascript:minusQty($(this.form))">수량빼기</button>
																	<input type="text" id="tempQty" value="" onchange="javascript:chgQty($(this.form),this.value)" 
																	onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');"
																	onblur="ccs.common.fn_press_han(this);"/>
																	<button type="button" class="btn_plus" onclick="javascript:plusQty($(this.form))">수량추가</button>								
																</div>
															</dd>
														</dl>
<%-- 														<a href="#none" class="btn_lyBox1" onclick="javascript:chgSaleproductSet('${cart.cartProductNo }')">변경</a> --%>
													</div>
													
													<div class="btn_wrapC btn1ea">	
														<a href="#none" class="btn_sStyle3 sPurple1" onclick="javascript:chgSaleproductSet('${cart.cartProductNo }')">변경</a>
													</div>
			
													<a href="#none" class="btn_close">옵션창 닫기</a>
												</div>
											</div>
										
										</c:when>
										<c:otherwise>
											<a href="#none" id="optionBtn${cart.cartProductNo }" class="btn_sStyle3 sGray2 btn_tb_option" onclick="javascript:changeOptionLayer('${cart.cartProductNo}')">옵션/수량변경</a>
											<div class="ly_box option_box" id="optionChangeLayer_${cart.cartProductNo }" style="display: none;"></div>
										
										</c:otherwise>
										</c:choose>
										
										<c:choose>
										<c:when test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.PICKUP' }">
											<a href="#none" class="btn_sStyle1 sPurple1" onclick="javascript:cartOrder('ONE','${cart.cartProductNo}');">픽업신청</a>
										</c:when>
										<c:otherwise>
											<a href="#none" class="btn_sStyle1 sPurple1 btn_baro" onclick="javascript:cartOrder('ONE','${cart.cartProductNo}');">바로구매</a>
										</c:otherwise>
										</c:choose>
									</div>
								
									<!-- //16.09.26 : 옵션 버튼 추가 -->							
									<span class="group">
										<c:choose>																																						
										<c:when test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.PICKUP' }">	<!-- 택배로받기 -->
											<c:choose>								
											<c:when test="${cart.realStockQty == 0 }">
												<a href="#none" class="btn_sStyle1 btn_parcel" onclick="javascript:alert('재고가 없습니다.');">택배로받기</a>
											</c:when>
											<c:otherwise>
												<a href="#none" class="btn_sStyle1 btn_parcel" onclick="javascript:transferGeneral('${cart.cartProductNo}')">택배로받기</a>
											</c:otherwise>
											</c:choose>
										</c:when>
										<c:otherwise><!-- 보관 -->
											<c:set var="keepCs" value=""/>							
											<c:set var="keepText" value=""/>
											<c:choose>							
												<c:when test="${cart.keepYn == 'Y' }">
												<c:set var="keepCs" value="sWhite1"/>								
												<c:set var="keepText" value="보관취소"/>
												</c:when>
												<c:otherwise>
												<c:set var="keepCs" value=""/>								
												<c:set var="keepText" value="계속보관"/>
												</c:otherwise>
											</c:choose>
											<label id="keepCs" class="btn_sStyle1 btn_save ${keepCs }">
												<input type="hidden" id="keepYn" value="${cart.keepYn}"/>
												<i id="keepText">${keepText }</i> <input type="checkbox" onchange="javascript:keep('${cart.cartProductNo}')" value="${cart.keepYn}" <c:if test="${cart.keepYn == 'Y' }">checked="checked"</c:if> />
											</label>
										</c:otherwise>
										</c:choose>
										<a href="#none" class="btn_sStyle1 btn_del" onclick="javascript:deleteCart('${cart.cartProductNo}');">삭제</a>
									</span>	
								</div>													
							</div>												
					</c:when>
					<%--///////////// 가격,버튼 노출(판매중) end ////////////////--%>
					<%--///////////// 가격,버튼 노출(품절) start ////////////////--%>
					<c:otherwise>				
<!-- 						<div class="col3"> -->
							
<!-- 						</div> -->
						<div class="col4">
							<span class="price">${cart.saleStateName }</span>
						</div>
						
						<c:if test="${cartCnt.cartTypeCd != 'CART_TYPE_CD.PICKUP' }">
						<div class="col5">
							
						</div>
						</c:if>
						<div class="col6">
							<a href="#none" class="btn_sStyle1" onclick="javascript:deleteCart('${cart.cartProductNo}');">삭제</a>
						</div>
						<!-- 16.09.26 : sold out -->
<!-- 						<span class="soldoutTxt"> -->
<!-- 							<em> -->
<!-- 								<strong>SOLD OUT</strong>판매가 완료되었습니다 -->
<!-- 							</em> -->
<!-- 						</span> -->
						<!-- //16.09.26 : sold out -->						
					</c:otherwise>
					<%--///////////// 가격,버튼 노출(품절) end ////////////////--%>
					</c:choose>
					
					 
					<c:if test="${!isMobile && cartCnt.cartTypeCd == 'CART_TYPE_CD.PICKUP' && cart.deliveryPolicyNo != -1}">
						<div class="special sp_pickup pc_only">
							<dl>
								<dt>픽업매장</dt>
								<dd>
									<p id="offshopName">${cart.ccsOffshop.name }</p> 
									<a href="#none" class="btn_sStyle1" onclick="javascript:chgOffshop('${cart.cartProductNo}')">매장변경</a>
									<a href="#none" class="btn_storeMap1" onclick="javascript:mypage.offshop.offshopInfo('${cart.offshopId}')">매장위치</a>
<%-- 								<a href="#none" class="btn_storeMap1 btn_shopPosition" onclick="javascript:openOffshopMap('${cart.cartProductNo}')">매장위치</a> --%>										
								</dd>
							</dl>
							<c:if test="${cart.deliveryPolicyNo > -1 }">
							<dl>
								<dt>픽업예정일</dt>
								<dd>
									<div class="select_box1">
										<label>선택</label>
										<select id="slc_pickup" name="pickupReserveDt">
											<option value="" selected>선택</option>
											<c:forEach items="${cart.ccsOffshop.openDays }" var="openDay" varStatus="st">
												<option value="${openDay.OPEN_DATE_VAL }">${openDay.OPEN_DATE_WEEK }</option>
											</c:forEach>
										</select>
									</div>
								</dd>
							</dl>
							</c:if>
						</div>
					</c:if>
					
					</div>	
			</form>
			<%--///////////// 장바구니 form end ////////////////--%>
					
			<%--sub sub 노출 조건 --%>
			<c:set var="sum" value="false"/>
			<c:choose>
				<c:when test="${status.last }">
					<c:set var="sum" value="true"/>								
				</c:when>
				<c:otherwise>
					<c:if test="${cartList[status.index+1].deliveryPolicyNo != cart.deliveryPolicyNo }">
						<c:set var="sum" value="true"/>
					</c:if>
				</c:otherwise>
			</c:choose>
			<%--sub sub 노출 조건 --%>
			<c:if test="${sum}">				
				</li>
			</ul>
			</c:if>
			<c:if test="${!sum && cart.deliveryPolicyNo == -1 }">
				</li>
			</c:if>
			<!-- ### //테이블 바디 ### -->
			
			
			
			<%-- sub sum start --%>
			<input type="hidden" id="deliveryFee${cart.deliveryPolicyNo }"  value="${cart.deliveryFee }"/>				
			<input type="hidden" id="minDeliveryFreeAmt${cart.deliveryPolicyNo }"  value="${cart.minDeliveryFreeAmt}"/>
			<c:choose>
			<c:when test="${cart.deliveryPolicyNo == -1 || cart.minDeliveryFreeAmt <= subTotalPrice || totalDeliveryFeeFreeYn == 'Y'}">
				<c:set var="subTotalDeliveryFee" value="0"/>
			</c:when>
			<c:otherwise>
				<c:set var="subTotalDeliveryFee" value="${cart.deliveryFee }"/>
			</c:otherwise>
			</c:choose>
			<c:if test="${sum && cartCnt.cartTypeCd == 'CART_TYPE_CD.GENERAL'}">
			<!-- ### 장바구니 중간 금액 ### -->
			<div class="total_medium">
			<c:if test="${cart.deliveryPolicyNo > -1 }">
				<dl>
					<dt>
						<span>총 상품금액</span>
					</dt>
					<dd id="subTotalPrice${cart.deliveryPolicyNo }"><fmt:formatNumber value="${subTotalPrice }" pattern="#,###" />원</dd>				
				</dl>				
				<em>+</em>
				<dl>
					<dt>
						<span>총 배송비</span>
						<c:if test="${!empty businessId}">		
						<a href="#none" class="btn_sStyle1 btn_bundle" onclick="javascript:ccs.link.display.sellerShop('${businessId}')">묶음배송 상품보기</a>
						</c:if>
					</dt>
<%-- 					<dd><small id="subDeliveryFee${cart.deliveryPolicyNo }"><i>+</i><fmt:formatNumber value="${subTotalDeliveryFee }" pattern="#,###" /></small>원</dd> --%>
					<dd id="subDeliveryFee${cart.deliveryPolicyNo }"><i>+</i><fmt:formatNumber value="${subTotalDeliveryFee }" pattern="#,###" />원</dd>
				</dl>
				<em>-</em>
				<dl>
					<dt>
						<span>총 할인금액</span>
					</dt>
					<dd id="subTotalDcPrice${cart.deliveryPolicyNo }"><i>-</i><fmt:formatNumber value="${subTotalDcPrice }" pattern="#,###" />원</dd>
				</dl>
				<em>=</em>
				<dl class="sum">
					<dt>
						<span>총 주문예상금액</span>
					</dt>
					<dd>
						<b id="subTotalAmt${cart.deliveryPolicyNo }"><fmt:formatNumber value="${subTotalPrice - subTotalDcPrice + subTotalDeliveryFee }" pattern="#,###" /><i>원</i></b>
					</dd>
				</dl>				
			</c:if>
			</div>
			<!-- ### //장바구니 중간 금액 ### -->			
			</c:if>
			
			<c:if test="${sum}">
			<c:set var="totalDeliveryFee" value="${totalDeliveryFee + subTotalDeliveryFee }"/>
			<c:set var="totalPrice" value="${totalPrice + subTotalPrice}"/>
			<c:set var="totalDcPrice" value="${totalDcPrice + subTotalDcPrice}"/>
			<c:set var="subTotalPrice" value="0"/>
			<c:set var="subTotalDcPrice" value="0"/>
			<c:set var="subTotalDeliveryFee" value="0"/>
			<c:set var="totalDeliveryFeeFreeYn" value="N"/>
			<c:set var="businessId" value=""/>
			</c:if>
			<%-- sub sum end --%>
			
			<c:if test="${cart.deliveryPolicyNo == -1 && status.last}"><!-- 품절 header -->
			</div>				
			</c:if>
			
<%-- 			<c:if test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.PICKUP'  --%>
<%-- 						&&   --%>
<%-- 						(  (cartList[status.index+1].deliveryPolicyNo != cart.deliveryPolicyNo && cartList[status.index+1].deliveryPolicyNo == -1)  --%>
<%-- 						|| (status.last && cart.deliveryPolicyNo != -1) --%>
<%-- 						 ) }"> --%>
<!-- 			<ul class="txt_list"> -->
<!-- 				<li> -->
<!-- 					매장픽업 안내 및 혜택 안내 영역 추가 -->
<!-- 				</li> -->
<!-- 			</ul> -->
<%-- 			</c:if> --%>
			
			<c:set var="preDeliveryPolicyNo" value="${cart.deliveryPolicyNo }"/>	
			
			<c:set var="totalSum" value="false"/>
			<c:choose>
				<c:when test="${cart.deliveryPolicyNo != -1 && status.last }">
					<c:set var="totalSum" value="true"/>								
				</c:when>
				<c:otherwise>
					<c:if test="${cart.deliveryPolicyNo != -1 && cartList[status.index+1].deliveryPolicyNo == -1 }">
						<c:set var="totalSum" value="true"/>
					</c:if>
				</c:otherwise>
			</c:choose>
			
			<c:if test="${totalSum }">
			<!-- ### 총 금액 ### -->
			<div class="total_money">
				<div class="columnL">
				<c:if test="${cartCnt.cartTypeCd != 'CART_TYPE_CD.PICKUP' }">
					<dl>
						<dt>총 상품금액</dt>
						<dd id="totalPrice"><fmt:formatNumber value="${totalPrice }" pattern="#,###" />원</dd>
					</dl>

					<span class="plus">+</span>

					<dl>
						<dt>총 배송비</dt>
						<dd id="totalDeliveryFee"><fmt:formatNumber value="${totalDeliveryFee }" pattern="#,###" />원</dd>
					</dl>				

					<span class="minus">-</span>

					<dl>
						<dt>총 할인금액</dt>
						<dd id="totalDcPrice"><fmt:formatNumber value="${totalDcPrice }" pattern="#,###" />원</dd>
					</dl>
				</c:if>
				</div>

				
				<c:choose>
				<c:when test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.PICKUP' }">
				<div class="column">				
				</c:when>
				<c:otherwise>
				<div class="columnR">							
					<span>=</span>
				</c:otherwise>
				</c:choose>

					<dl class="sum">
						<dt>총 결제예상금액</dt>
						<dd><b id="totalAmt"><fmt:formatNumber value="${totalPrice - totalDcPrice + totalDeliveryFee }" pattern="#,###" /><i>원</i></b></dd>
					</dl>
				</div>
			</div>
			<!-- ### //총 금액 ### -->

			<!-- ### 상품 삭제 버튼 : 2016.08.24 수정 ### -->
			<div class="choice_del">
				<!-- 16.11.01 -->
				<label class="chk_style1">
					<em>
						<input type="checkbox" value="" id="chk_all_3" class="chk_all" checked="checked" onclick="javascript:checkAll(this)"/>
					</em>
					<span>전체선택</span>
				</label>
				<!-- //16.11.01 -->
				<a href="#none" class="btn_sStyle3 btn_choice_del" onclick="javascript:deleteCart();">선택상품 삭제</a>
				<!-- <a href="#none" class="btn_sStyle3 btn_disable_del" onclick="javascript:deleteEmptyCart();">품절상품 삭제</a> -->
				<label class="btn_jjim">
					<input type="checkbox" value="" onclick="javascript:saveZzim()" />
					<em>찜</em>
				</label>
			</div>
			<!-- ### //상품 삭제 버튼 : 2016.08.24 수정 ### -->
			<div class="btn_wrapC btn3ea">
			<c:set var="btnName" value="주문"/>					
			<c:choose>
			<c:when test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.PICKUP' }">
				<c:set var="btnName" value="신청"/>
			</c:when>
			<c:otherwise>
				<c:set var="btnName" value="주문"/>
			</c:otherwise>
			</c:choose>
			<a href="#none" class="btn_bStyle1 sWhite1 btn_choice_del" onclick="javascript:deleteCart();">선택삭제</a>
			<a href="#none" class="btn_bStyle1" onclick="javascript:cartOrder('SELECT');">선택${btnName }</a>
			<a href="#none" class="btn_bStyle1 sPurple1" onclick="javascript:cartOrder('ALL');">전체${btnName }</a>					
			</div>
			</c:if>					
			</c:forEach>
			<%--///////////// 장바구니 loop end ////////////////--%>
									
		</div>
		<!-- ### //일반구매 : 2016.08.24 수정 ### -->	
		</c:if>
		<%--///////////// 장바구니 상품있을때 end ////////////////--%>
					
<%--				////////////////// 장바구니,매장픽업,정기배송 탭 끝 //////////////////--%>

<%--				////////////////// 추천상품 영역 시작 //////////////////--%>
		<!-- ### 추천상품 : 2016.08.24 수정 ### -->
		
		<div class="rolling_box"> <!-- 16.11.01 : style 삭제 -->
			<h2 class="tit_style2">추천 상품</h2> <!-- 1118 -->
			<div class="swiper_wrap">
				<div class="prodSwiper product_type1 prodType_4ea block swiper-container cartSwiper_pordList">
					<ul class="swiper-wrapper" name="cartBestArea">
					</ul>
					<div class="swiper-button-next btn_tp2"></div>
					<div class="swiper-button-prev btn_tp2"></div>
				</div>
			</div>
		</div>
		<!-- ### //추천상품 : 2016.08.24 수정 ### -->

		<!-- ### 주의사항 : 2016.08.24 수정 ### -->
		<ul class="txt_list">
			<c:if test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.GENERAL' ||  cartCnt.cartTypeCd == 'CART_TYPE_CD.REGULARDELIVERY'}">
			<li>
				일반구매 장바구니 담은 상품의 보관기간은 30일입니다.
			</li>
			<li>
				계속보관을 선택 시 보관기간 30일 및 주문 후에도 상품이 장바구니에 계속 보관됩니다. (단, 매장픽업 장바구니는 계속보관 서비스가 지원하지 않습니다.)
			</li>
			<li>
				무이자 할부 개월수가 다른 상품을 같이 주문하면, 할부 개월수가 낮은 상품 기준으로 할부가 적용됩니다. 
			</li>
			<li>
				상품 무이지 해당 상품만 별도 주문하면, 상품에 적용된 무이자 할부 혜택을 받으실 수 있습니다.
			</li>
			<li>
				매장픽업의 경우 신청 매장에서 결제 및 수령이 가능하여, 0to7에서 진행되는 할인 및 무이자 혜택을 받으실 수 없습니다.
			</li>
			<li>
				정기배송의 경우 신청 주기에 따라 결제 후 배송되므로 결제일에 따라 무이자 및 할인 혜택이 다를 수 있습니다.
			</li>
			</c:if>
			<c:if test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.PICKUP' }">
			<li>
				매장픽업신청은 픽업 예약만 진행되며, 결제는 픽업매장에서 진행됩니다.
			</li>
			<li>
				픽업 신청 후 3시간 내 상품준비완료 안내 메시지가 발송되며 문자를 받으시고 매장을 방문하시면 상품 픽업이 가능합니다.
			</li>
			<li>
				16시 이전에 신청하시면, 당일 픽업이 가능합니다. 
			</li>
			<li>
				픽업예정일에 상품을 픽업하지 않으실 경우, 이틀후 픽업신청은 자동 취소됩니다.
			</li>
			</c:if>
		</ul>
		<!-- ### //주의사항 : 2016.08.24 수정 ### -->
		</div>
	</div>


<!-- ### 매장 위치 레이어 : 2016.08.28 수정 ### -->
<div class="pop_wrap layer_shop">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">매장 위치</h3>
		</div>
		<div class="pop_content">
			<div class="shop_info" id="layer_info">
				
			</div>
		</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>
<!-- ### //매장 위치 레이어 : 2016.08.28 수정 ### -->

<!-- ### 선물포장 안내 레이어 : 2016.08.26 추가 ### -->
<!-- mo 전용 -->
<div class="layer_style1 sLayer_gift">
	<div class="box">
		<div class="conArt">
			<strong class="title">선물포장 안내</strong>

			<div class="conBox">
				<ul class="txt_list txt_list_bgWhite">
					<li>
						선물포장은 포장 1건당 1,000원의 비용이 책정되는 유료서비스입니다.
					</li>
					<li>
						선물포장은 부피 기준으로 포장되므로,부피가 크거나 개별 케이스가 있어 포장이 용이하지 않은 상품에는 제공되지 않습니다.
					</li>
					<li>
						선물포장비는 박스당 비용이 부과되므로 여러 개 주문으로 기준부피 초과시 2개의 박스로 분리 포장되며, 포장비가 추가로 발생 할 수 있습니다.
					</li>
				</ul>
				<div class="exGift">
					<img src="/resources/img/mobile/bg/sLayer_gift.jpg" alt="" />
				</div>
			</div>
		</div>
		<button type="button" class="btn_close">레이어팝업 닫기</button>
	</div>
</div>

<!-- ### //선물포장 안내 레이어 : 2016.08.26 추가 ### -->

<c:if test="${changeFlag == 'Y'}">
<script type="text/javascript">
changeTotalSalePrice();
</script>
</c:if>