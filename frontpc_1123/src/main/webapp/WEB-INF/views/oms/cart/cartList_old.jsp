<%--
	화면명 : 장바구니
	작성자 : dennis
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script type="text/javascript">
//계산
var calc = function(){
	
	var chklist = $("input[name=chk]");
	
	var preDeliveryPolicyNo;
	var preMinDeliveryFreeAmt;
	var subTotalSalePrice = 0;	
	var subTotalSaleAmt = 0;
	var totalPrice = 0;
	var totalDeliveryFee = 0;
	var totalAmt = 0;	
	var chkCnt = 0;
	
	var totalDeliveryFeeFreeYn = 'N';
	
	for(var i=0;i<chklist.length;i++){
		var cartProductNo = chklist[i].value;
		var checked = chklist[i].checked;
		var form = "cartForm"+cartProductNo;
		var saleStateCd = $("#"+form+" #saleStateCd").val();				
		var deliveryPolicyNo = $("#"+form+" #deliveryPolicyNo").val();
		var minDeliveryFreeAmt = $("#minDeliveryFreeAmt"+deliveryPolicyNo).val();
		var deliveryFee = $("#deliveryFee"+deliveryPolicyNo).val();

		var deliveryFeeFreeYn = $("#"+form+" #deliveryFeeFreeYn").val();
		
		if(deliveryFeeFreeYn == 'Y'){
			totalDeliveryFeeFreeYn = 'Y';
		}
							
		var salePrice = $("#"+form+" #salePrice").val();
		var addSalePrice = $("#"+form+" #addSalePrice").val();	
		var totalSalePrice =$("#"+form+" #totalSalePrice").val();
		var qty = $("#"+form+" #qty").val();
		var sumSalePrice = Number(totalSalePrice) * Number(qty);
		
		$("#"+form+" #salePriceTxt").html(common.priceFormat(sumSalePrice,true));		
			
	// 		console.log("======================"+i);
	// 		console.log("cartProductNo : "+cartProductNo);
	// 		console.log("salePrice : "+salePrice);
	// 		console.log("addSalePrice : "+addSalePrice);
	// 		console.log("qty : "+qty);		
	// 		console.log("sumSalePrice : "+sumSalePrice);
	// 		console.log("subTotalSalePrice : "+subTotalSalePrice);
	// 		console.log("deliveryPolicyNo : "+deliveryPolicyNo);
	// 		console.log("preDeliveryPolicyNo : "+preDeliveryPolicyNo);				
	// 		console.log("======================"+i);					

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
			subTotalSalePrice = Number(subTotalSalePrice) + Number(sumSalePrice);		
			totalPrice = Number(totalPrice) + Number(sumSalePrice);
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
			$("#subDeliveryFee"+deliveryPolicyNo).html(common.priceFormat(deliveryFee,true));
			var tot = Number(subTotalSalePrice) + Number(deliveryFee);
			$("#subTotalAmt"+deliveryPolicyNo).html(common.priceFormat(tot,true));
			totalDeliveryFee = Number(totalDeliveryFee) + Number(deliveryFee);			
			
			chkCnt = 0;
			subTotalSalePrice = 0;
			
			totalDeliveryFeeFreeYn = 'N';
		}											
	}	
	
	$("#totalPrice").html(common.priceFormat(totalPrice,true,false));
	$("#totalDeliveryFee").html(common.priceFormat(totalDeliveryFee,true,false));
	var totalAmt = totalPrice + totalDeliveryFee;
	$("#totalAmt").html(common.priceFormat(totalAmt,true,false));
	
}


//수량 -
var minQty = function(form){
	
	var qty = $(form).find("#qty");

	if(qty.val() == 1){
		alert("수량은 1이상입니다.");
		return;
	}

	qty.val(Number(qty.val())-1);
	
	saveCart(form);
}
//수량 +
var plusQty = function(form){
	var qty = $(form).find("#qty");
	qty.val(Number(qty.val())+1);
	
	saveCart(form);
}
//수량 수정
var chgQty = function(form,value){
	var qty = $(form).find("#qty");	
	qty.val(value);
	saveCart(form);
}

var _productId;
var _subProductId;
var _value;
var _type;

//단품 변경
var chgSaleproduct = function(form,productId,subProductId,value,type) {
	
	_productId = productId;
	_subProductId = subProductId;
	_value = value;
	_type = type;
	
	//단품 선택
	var newValue = value;
	var orgId = "saleproductId";	
	if(type == 'SUB'){
		orgId = subProductId+"_saleproductId";
	}
	
	var orgValue = $(form).find("#"+orgId).val();
	
	if(newValue != orgValue){
		
		if(type == 'SUB'){
			$(form).find("#"+subProductId+"_newSaleproductId").val(newValue);
		}else{
			$(form).find("#"+productId+"_newSaleproductId").val(newValue);
		}		
	}
	
// 	console.log(productId);
// 	console.log(subProductId);
// 	console.log(newValue);
// 	console.log(type);
// 	console.log(form);
	
	saveCart(form,true);
}

var afterChgSaleprouct = function(form){
	
// 	console.log("============ after");
// 	console.log(_productId);
// 	console.log(_subProductId);
// 	console.log(_value);
// 	console.log(_type);
// 	console.log(form);
	
	var orgId = "saleproductId";
	if(_type == 'SUB'){
		orgId = _subProductId+"_saleproductId";
	}
	
	$(form).find("#"+orgId).val(_value);
	
	if(_type == 'SUB'){
		$(form).find("#"+_subProductId+"_newSaleproductId").val("");
	}else{
		$(form).find("#"+_productId+"_newSaleproductId").val("");		
	}	
	
	var selectTxt = "옵션 : ";
	var select = $(form).find("select");
	
	for(var i=0;i<select.length;i++){
		
		var options = $("#"+select[i].id+" option");
// 		console.log(options);
		for(var j=0;j<options.length;j++){
			var option = options[j].text;
			var value = options[j].value;
						
// 			console.log(option);
// 			console.log(value);

			if(_type != "SUB"){
				$(form).find("#addSalePrice").val(options[j].add);
			}
			
			if(_value == value){
				selectTxt += option + ",";
				break;
			}
		}
			
	}
	
	selectTxt = selectTxt.substr(0,selectTxt.length-1);
	
	$(form).find("#optionTxt").html(selectTxt);
	
	calc();
}

//장바구니저장
var saveCart = function(form,chgSaleproduct){		
	
	var data = $(form).serialize();
	
	$.ajax({
		url : "/api/oms/cart/save",
		type : "POST",
		data : data
	}).done(function(response){
		if(response.RESULT == "SUCCESS"){
			//alert("성공.");
			//window.location.reload();
			if(chgSaleproduct){
				afterChgSaleprouct(form);
			}else{
				calc();
			}
		}else{
			alert(response.MESSAGE);
		}				
	});
}

//checklist
var fn_checklist = function(cartProductNo,strYn,pickupYn,chgYn){
	var chklist=[];
	var chkliststr = "";
	if(cartProductNo != null && cartProductNo != ''){
		var subFix = "";
		if(pickupYn){
			subFix = "_" + $("#cartForm"+cartProductNo).find("select[name=pickupReserveDt] option:selected").val();
		}
		chkliststr += cartProductNo+subFix+",";		
		chklist.push({"cartProductNo":cartProductNo+subFix})		
	}else{
		$("input[name=chk]:checked").each(function(){
			var deliveryPolicyNo = $("#cartForm"+this.value).find("#deliveryPolicyNo").val();
			if(deliveryPolicyNo != '-1'){
				var subFix = "";
				if(pickupYn){
					subFix = "_" + $("#cartForm"+this.value).find("select[name=pickupReserveDt] option:selected").val();
				}
				if(chgYn){	//변경 check
					var changeSalePriceFlag = $("#cartForm"+this.value).find("#changeSalePriceFlag").val();
					if(changeSalePriceFlag == 'Y'){
						chkliststr += this.value+subFix+",";	
						chklist.push({"cartProductNo":this.value+subFix});
					}
				}else{
					chkliststr += this.value+subFix+",";	
					chklist.push({"cartProductNo":this.value+subFix});
				}
			}
		});
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
	var chklist
	if(cartProductNo == 'ALL'){
		chklist = fn_checklist(null,false);
	}else{
		chklist = fn_checklist(cartProductNo);	
		var keepYn = $("#cartForm"+cartProductNo).find("#keepYn").val();
		if(keepYn == "Y"){
			if(!window.confirm("계속보관 상품입니다. 삭제하시겠습니까?")){
				return;
			}
		}
	}
	
	for(var i=0;i<chklist.length;i++){
		$("#cartForm"+chklist[i].cartProductNo).remove();
	}
	
// 	console.log(chklist);
	
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
		}				
	});
}

//주문
var order = function(type,cartProductNo){	
	var chklist;
	
	var cartTypeCd = $("#selCartTypeCd").val();
	
	if(type == "ALL"){
		chklist = "";
		$("input[name=chk]").each(function(){
			var deliveryPolicyNo = $("#cartForm"+$(this).val()).find("#deliveryPolicyNo").val();
			if(deliveryPolicyNo != '-1'){
				chklist += $(this).val()+",";		
			}
		});		
		chklist = chklist.substr(0,chklist.length-1);
	}else if(type == "ONE"){
		chklist = cartProductNo;
	}else if(type == "SELECT"){
		if(cartTypeCd == "CART_TYPE_CD.PICKUP"){
			chklist = fn_checklist("",true,true);
		}else{
			chklist = fn_checklist("",true);
		}
	} 	
	
	if($.trim(chklist.replace(/,/gi,'')) == ''){
		alert("선택된 상품이 없습니다.");
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
	
	$.ajax({
//  		contentType : "application/json; charset=UTF-8",
		url : "/api/oms/order/pickup/save",
		type : "POST",		
		data : form.serialize()
	}).done(function(response){
		if(response.RESULT == "SUCCESS"){
			alert("성공.");
// 			window.location.reload();			
		}else{
			alert(response.MESSAGE);
		}				
	});
}

//탭변경
var chgTab = function(type){
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
				var subWishlist = new mms.Wishlist(value,$("#"+form).find("#"+value+"_saleproductId").val(),null);
				subWishlists.push(subWishlist);
			})
			var mmsWishlist = new mms.Wishlist(productId,saleproductId,subWishlists);
			wishlists.push(mmsWishlist);						
		}else{			
			var mmsWishlist = new mms.Wishlist(productId,saleproductId,null);
			wishlists.push(mmsWishlist);			
		}					
				
	});	

	mms.saveWishlist(wishlists,function(response){
		alert("저장하였습니다.");
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
	
	$.ajax({
 		contentType : "application/json; charset=UTF-8",
		url : "/api/oms/cart/keep",
		type : "POST",		
		data : JSON.stringify(data)
	}).done(function(response){
		if(response.RESULT == "SUCCESS"){
			//alert("저장되었습니다.");
			//window.location.reload();				
		}else{
			alert(response.MESSAGE);
		}				
	});
}

//매장픽업 지도
var openOffshopMap = function(name,addr,latitude,longitude){
	$("#layer_name").html(name);
	$("#layer_addr").html(addr);
}

var linkProduct = function(productId){
	alert("상품 으로 링크 : "+productId);
}

$(document).ready(function(){
	$(".inp_chk").on({"change" : function() {
		calc();
	}});
})

//장바구니 변경사항있을때.
var changeTotalSalePrice = function(){
	
	alert("장바구니 가격이 변경되었습니다.");
	var cartProductNos = fn_checklist(null,true,false,true);
	var data = {"priceChange":true,"cartProductNos" : cartProductNos};		
	
// 	console.log(data);
// 	return;
	$.ajax({
		contentType : "application/json; charset=UTF-8",
		url : "/api/oms/cart/saveList",
		type : "POST",
		data : JSON.stringify(data)
	}).done(function(response){
		if(response.RESULT == "SUCCESS"){
			alert("성공.");
			window.location.reload();
		}else{
			alert(response.MESSAGE);
		}				
	});
}
</script>
<div class="content" style="">
<form action="" name="targetForm" id="targetForm" method="post">
	<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }"/>
	<input type="hidden" name="cartProductNos" id="cartProductNos" value=""/>
</form>
	<%--location_box--%>
<%-- 	<jsp:include page="/WEB-INF/views/gsf/layout/page/pc/navi_pc.jsp"/> --%>
	
	
	<div class="location_box">
		<div class="location_inner">
			<ul>
				<li class="home"><span class="hide">홈</span></li>
				<li>
					장바구니
				</li>
			</ul>
		</div>
	</div>

	<div class="mo_navi">
		<button type="button" class="btn_navi_prev">이전 페이지로..</button>
		<h2>장바구니</h2>
	</div>
 	
 	
	<div class="inner">
		<div class="cart_box">
			<h3 class="title_type1">
				장바구니
			</h3>

			<div class="tab_box">
				<%--### 탭 버튼 ###--%>
				<ul class="tab tab_menu">
					<input type="hidden" id="selCartTypeCd" value="${cartCnt.cartTypeCd }"/>
					<c:if test="${memberYn == 'Y' }">				
					<li class="<c:if test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.GENERAL' }">on</c:if>" ><a href="#none" onclick="javascript:chgTab('GENERAL')">일반구매<span>(${cartCnt.generalCnt })</span></a></li>					
					<li class="<c:if test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.PICKUP' }">on</c:if>" ><a href="#none"  onclick="javascript:chgTab('PICKUP')">매장픽업<span>(${cartCnt.pickupCnt })</span></a></li>
					<li class="<c:if test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.REGULARDELIVERY' }">on</c:if>" ><a href="#none" onclick="javascript:chgTab('REGULARDELIVERY')">정기배송<span>(${cartCnt.regulardeliveryCnt })</span></a></li>
					</c:if>
				</ul>
				<%--### //탭 버튼 ###--%>

				<%--사용 X
				<div class="login_zone">
					<p>
						로그인하시면 상품을 장바구니에 보관할 수 있습니다.
					</p>

					<a href="#none" class="btn_type9">
						로그인
					</a>
				</div>
				--%>
				
<%--				장바구니 탭 시작--%>
				<%--///////////// 장바구니 상품있을때 start ////////////////--%>
				<c:choose>
				<c:when test="${fn:size(cartList) > 0 }">
				<div class="tabcont tabcontShow">
					
					<div class="choice_del">
						<a href="#none" class="btn_style7 btn_choice_del"  onclick="javascript:deleteCart();">선택상품 삭제</a>
						<a href="#none" class="btn_style7 btn_disable_del" onclick="javascript:deleteEmptyCart();">품절상품 삭제</a>
						<input type="checkbox" value="" id="choice_del1" class="btn_jjim" onclick="javascript:saveZzim()"/>
						<label for="choice_del1">선택한 상품 쇼핑찜에 담기</label>
					</div>
					
					<c:set var="subTotalPrice" value="0"/>
					<c:set var="totalPrice" value="0"/>
					<c:set var="subTotalDeliveryFee" value="0"/>
					<c:set var="totalDeliveryFee" value="0"/>
					<c:set var="totalDeliveryFeeFreeYn" value="N"/>
					<c:set var="changeFlag" value="N"/>
					
					
					<%--///////////// 장바구니 loop start ////////////////--%>
					<c:forEach items="${cartList }" var="cart" varStatus="status">
						<%--///////////// 장바구니 form start ////////////////--%>
						<c:if test="${cart.deliveryFeeFreeYn == 'Y' }">
							<c:set var="totalDeliveryFeeFreeYn" value="Y"/>						
						</c:if>
						
						<form name="cartForm" id="cartForm${cart.cartProductNo}">
						<input type="hidden" id="cartTF" name="cartTF" value="true"/><br/>
						<input type="hidden" id="cartId" name="cartId" value="${cart.cartId }"/>
						<input type="hidden" name="cartProductNo" value="${cart.cartProductNo }"/>
						<input type="text" id="productId" name="productId" value="${cart.productId }"/>
						<input type="hidden" id="saleproductId" name="saleproductId" value="${cart.saleproductId }"/>
						<input type="text" id="dealId" name="dealId" value="${cart.dealId }"/>
						<input type="hidden" id="channelId" name="channelId" value="${cart.channelId }"/>
						<input type="hidden" id="cartTypeCd" name="cartTypeCd" value="${cart.cartTypeCd }"/>
						<input type="hidden" id="cartProductTypeCd" name="cartProductTypeCd"  value="${cart.cartProductTypeCd }"/>
						<input type="hidden" id="offshopId" name="offshopId"  value="${cart.offshopId }"/>
						
						<input type="hidden" id="saleStateCd" name="saleStateCd"  value="${cart.saleStateCd }"/>
						<input type="hidden" id="deliveryPolicyNo" name="deliveryPolicyNo"  value="${cart.deliveryPolicyNo }"/>					
						
						<input type="hidden" id="qty" name="qty"  value="${cart.qty }"/>
						<input type="hidden" id="salePrice" name="salePrice"  value="${cart.salePrice}"/>
						<input type="hidden" id="addSalePrice" name="addSalePrice"  value="${cart.addSalePrice}"/>
						<input type="text" id="totalSalePrice" name="totalSalePrice"  value="${cart.totalSalePrice}"/>
						<input type="text" id="cartTotalSalePrice" name="cartTotalSalePrice"  value="${cart.cartTotalSalePrice}"/>
						<input type="text" id="couponId" name="couponId"  value="${cart.couponId}"/>
						<input type="hidden" id="deliveryFeeFreeYn" name="deliveryFeeFreeYn"  value="${cart.deliveryFeeFreeYn}"/>
						
						<c:choose>
						<c:when test="${cart.totalSalePrice != cart.cartTotalSalePrice }">
							<c:set var="changeFlag" value="Y"/>
							<input type="hidden" id="changeSalePriceFlag" name="changeSalePriceFlag"  value="Y"/>
						</c:when>
						<c:otherwise>
							<input type="hidden" id="changeSalePriceFlag" name="changeSalePriceFlag"  value="N"/>
						</c:otherwise>
						</c:choose>						
						
						<c:if test="${cartCnt != 'CART_TYPE_CD.GENERAL' || cart.deliveryPolicyNo != preDeliveryPolicyNo || status.first}">
						
						
							<c:choose>
								<c:when test="${cart.deliveryPolicyNo == -1 }">
									<c:set var="hclass" value="cart_disable_head1"/> 
								</c:when>
								<c:when test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.PICKUP' }">
									<c:set var="hclass" value="shop_add"/> 
								</c:when>
							</c:choose>
							<%--### 테이블 헤더 ###--%>
							<div class="cart_tb_thead1 ${hclass }">
								
								<%--### 매장정보 start ###--%>						
								<c:if test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.PICKUP' && cart.deliveryPolicyNo != -1}">
																		
								<div class="shop">
									<dl>
										<dt>
											${cart.ccsOffshop.name }
										</dt>
										<dd>
											(${cart.ccsOffshop.zipCd }) ${cart.ccsOffshop.address1 }
											<fmt:parseDate pattern="HHmm" value="${cart.ccsOffshop.startTime }" var="startTime"/>
											<fmt:formatDate value="${startTime }" pattern="HH:mm" var="startTime"/>
											<fmt:parseDate pattern="HHmm" value="${cart.ccsOffshop.endTime }" var="endTime"/>
											<fmt:formatDate value="${endTime }" pattern="HH:mm" var="endTime"/>
											<em>|</em> <span>영업시간: ${startTime }시 ~ ${endTime }시</span> 
											<em>|</em> 휴점일: 
											<c:forEach items="${cart.ccsOffshop.ccsOffshopholidays }" var="ho" begin="0" end="0">
											<fmt:parseDate pattern="yyyyMMdd" value="${ho.holiday }" var="holiday"/>
											<fmt:formatDate value="${holiday }" pattern="yyyy-MM-dd" var="holiday"/>
											${holiday }
											</c:forEach> 
										</dd>
									</dl>
									<div class="schedule">
										<strong>픽업예정일</strong>
										<div class="sel_box select_style1">
											<label for="slc_pickup"></label>
											<select id="slc_pickup" name="pickupReserveDt">
												<c:forEach items="${cart.ccsOffshop.openDays }" var="openDay" varStatus="st">
													<option value="${openDay.OPEN_DATE_VAL }" <c:if test="${st.index == 0 }">selected</c:if> >${openDay.OPEN_DATE_WEEK }</option>
												</c:forEach>
											</select>
										</div>
									</div>
								
									<a href="#none" class="btn_shop_map" onclick="javascript:openOffshopMap('${cart.ccsOffshop.name }','(${cart.ccsOffshop.zipCd }) ${cart.ccsOffshop.address1 }','${cart.ccsOffshop.latitude }','${cart.ccsOffshop.longitude }')">매장위치</a>
									<span class="btn_shop_tel">${cart.ccsOffshop.managerPhone}</span>
								</div>
								</c:if>
								<%--### 매장정보 end ###--%>
								
								<span class="col1">
									<c:set var="chk" value=""/>
									<c:if test="${cart.deliveryPolicyNo != -1 }">
										<c:set var="chk" value="checked"/>
									</c:if>
									<input type="checkbox" value="" id="chk_all" class="chk_all inp_chk"  ${chk }/>
									<label for="chk_all">전체선택</label>
									
									<c:choose>
									<c:when test="${cart.deliveryPolicyNo == -1 }">
										<span class="soldout">
											<a href="#none">품절/판매종료상품 <em>(${cart.deliveryCnt })</em></a>
										</span>
										
										<span class="btn_disable_allDel">
											<a href="#none" onclick="javascript:deleteEmptyCart()">전체삭제</a>
										</span>
									</c:when>
									<c:otherwise>
										<!-- mo 전용 버튼 -->
										<div class="mo_util">
											<input type="checkbox" value="" id="choice_del1" class="btn_jjim"  onclick="javascript:saveZzim()"/>
											<label for="choice_del1">선택한 상품 쇼핑찜에 담기</label>
											<a href="#none" class="btn_style7 btn_all_del" onclick="javascript:deleteCart('ALL');">전체삭제</a>
										</div>
										<!-- //mo 전용 버튼 -->
									</c:otherwise>
									</c:choose>
								</span>
		
								<span class="col2">상품/옵션정보</span>
		
								<span class="col3">수량</span>
		
								<span class="col4">상품금액</span>
		
								<span class="col5">선택</span>
							</div>
							<%--### //테이블 헤더 ###--%>
						
						<ul class="cart_tb_tbody1">
							
						</c:if>
						
					
						<c:choose>
							<c:when test="${cart.deliveryPolicyNo == -1 }">
								<li class="disable">
							</c:when>
							<c:otherwise>
								<li>
							</c:otherwise>
						</c:choose>												
						
						<c:set var="preDeliveryPolicyNo" value="${cart.deliveryPolicyNo }"/>
						<c:set var="preMinDeliveryFreeAmt" value="${cart.minDeliveryFreeAmt }"/>
						<c:set var="preDeliveryFee" value="${cart.deliveryFee }"/>
							<div class="tr_box">
								<div class="td_box col1">
									<c:set var="chk" value=""/>
									<c:if test="${cart.deliveryPolicyNo != -1 }">
										<c:set var="chk" value="checked"/>
									</c:if>
									<input type="checkbox" name="chk" value="${cart.cartProductNo }" title="선택" class="inp_chk" ${chk }/>
								</div>

								<div class="td_box col2">
									<div class="prod_img">
										<a href="#none">
											<img src="/resources/img/pc/temp/cart_img1.jpg" alt="" />
											<%--<span class="icon_gift">선물용</span>--%>
										</a>
									</div>

									<a href="#none" class="title" onclick="javascript:linkProduct('${cart.productId}')">
										[${cart.brandName }]${cart.productName }
									</a>
									
									
									
									<c:choose>
									<%--///////////// SET 단품선택 start ////////////////--%>
									<c:when test="${cart.cartProductTypeCd == 'CART_PRODUCT_TYPE_CD.SET' }">
																				 
										<em>
											<i id="optionTxt">옵션 :
											<c:forEach items="${cart.omsCarts }" var="sub" varStatus="stat2"> 
											<c:if test="${stat2.index > 0 }">,</c:if>${sub.saleproductName }
											</c:forEach>
											</i>
											<a href="#none" class="btn_option">옵션설정</a>
										</em>
<%-- 											[${sub.brandName }]${sub.productName } ${sub.saleproductName }<br/>					 --%>
											
										<div class="option_box opt_box">
											<div class="con">
												<strong class="logo">제로투세븐</strong>
	
												<dl>
													<c:forEach items="${cart.omsCarts }" var="sub" varStatus="stat2">
													<input type="hidden" name="subProductId" value="${sub.productId }"/>													
													<input type="hidden" id="${sub.productId }_saleproductId"  name="omsCarts[${stat2.index }].saleproductId" value="${sub.saleproductId }"/>
													<input type="hidden" id="${sub.productId }_SetProductId"  name="omsCarts[${stat2.index }].productId" value="${sub.productId }"/><br/>
													<input type="hidden" id="${sub.productId }_newSaleproductId" name="omsCarts[${stat2.index }].newSaleproductId" value=""/>
												
													<dt>옵션${stat2.index+1 })</dt>
													<dd>
														<div class="selectbox">
															<label for="slc_option"></label>
															<select id="${sub.productId }_orgSaleproductId" name="omsCarts[${stat2.index }].orgSaleproductId" onchange="javascript:chgSaleproduct($(this.form),'${cart.productId }','${sub.productId }',this.value,'SUB')">
																<c:forEach items="${sub.pmsSaleproducts }" var="subSel">
																	<option value="${subSel.saleproductId }" <c:if test="${sub.saleproductId == subSel.saleproductId }"> selected="selected"</c:if>>${subSel.name }</option>
																</c:forEach>
															</select>
														</div>
													</dd>
													</c:forEach> 												
												</dl>
	
	<%--											<div class="user_action">--%>
	<%--												<a href="#none" class="btn_style8">변경</a>--%>
	<%--											</div>--%>
	
												<a href="#none" class="btn_close">옵션창 닫기</a>
											</div>
										</div>
										
									</c:when>
									<%--///////////// SET 단품선택 end ////////////////--%>
									
									<%--///////////// 일반 단품선택 start ////////////////--%>
									<c:otherwise>
									
									<input type="hidden" id="${cart.productId }_newSaleproductId" name="newSaleproductId" value=""/>
									<em>
										<i id="optionTxt">옵션 : ${cart.saleproductName }</i>
										
										<c:if test="${cart.saleStateCd == 'SALE_STATE_CD.SALE' }">
										<a href="#none" class="btn_option">옵션설정</a>
										</c:if>										
									</em>		
									<div class="option_box opt_box">
										<div class="con">
											<strong class="logo">제로투세븐</strong>

											<dl>
												<dt>옵션</dt>
												<dd>
													<div class="selectbox">
														<label for="slc_option"></label>
														<select id="${cart.productId }_orgSaleproductId" name="orgSaleproductId" onchange="javascript:chgSaleproduct($(this.form),'${cart.productId }','${cart.productId }',this.value,'')">
															<c:forEach items="${cart.pmsSaleproducts }" var="subSel">
															<option value="${subSel.saleproductId }" add="${subSel.addSalePrice }" <c:if test="${cart.saleproductId == subSel.saleproductId }"> selected="selected"</c:if>>${subSel.name }</option>
															</c:forEach>
														</select>
													</div>
												</dd>												
											</dl>

<%--											<div class="user_action">--%>
<%--												<a href="#none" class="btn_style8">변경</a>--%>
<%--											</div>--%>

											<a href="#none" class="btn_close">옵션창 닫기</a>
										</div>
									</div>
									</c:otherwise>
									<%--///////////// 일반 단품선택 end ////////////////--%>
									</c:choose>
									
									<%--///////////// 사은품 start ////////////////--%>
									<c:if test="${cart.spsPresent != null }">										
									<u>
										<a href="#none" class="btn_gift_view">[사은품] ${cart.spsPresent.name }</a>
									</u>
									</c:if>
									<%--///////////// 사은품 end ////////////////--%>
									
									<%--///////////// 사은품 목록 layer start ////////////////--%>
									<div class="option_box gift_box">
										<ul>
											<c:forEach items="${cart.spsPresent.spsPresentproducts }" var="spp"> 
											<li>
												<img src="/resources/img/pc/temp/cart_img3.jpg" alt="" />
												<span>${spp.pmsProduct.name }</span>
											</li>
											</c:forEach>											
										</ul>

										<a href="#none" class="btn_close">옵션창 닫기</a>
									</div>
									<%--///////////// 사은품 목록 layer end////////////////--%>
								</div>

								<c:choose>
								
								<%--///////////// 가격,버튼 노출(판매중) start ////////////////--%>
								<c:when test="${cart.saleStateCd == 'SALE_STATE_CD.SALE' }">									
									<div class="td_box col3">
										<div class="quantity_box">
											<button type="button" class="btn_minus" onclick="javascript:minQty($(this.form))">수량빼기</button>
											<input type="text" id="tempQty" value="${cart.qty }" onchange="javascript:chgQty($(this.form),this.value)"/>
											<button type="button" class="btn_plus" onclick="javascript:plusQty($(this.form))">수량추가</button>
										</div>
									</div>									
	
									<div class="td_box col4" id="salePriceTxt">
										<fmt:formatNumber value="${cart.totalSalePrice * cart.qty }" pattern="#,###" /><span>원</span>									</div>
									<c:set var="subTotalPrice" value="${subTotalPrice + (cart.totalSalePrice * cart.qty)}"/>																	
									<input type="hidden" id="keepYn" value="${cart.keepYn}"/>
									<div class="td_box col5">
										<c:if test="${cartCnt.cartTypeCd != 'CART_TYPE_CD.PICKUP' }">
											<a href="#none" class="btn_style9" onclick="javascript:order('ONE','${cart.cartProductNo}');">바로구매</a>
											<c:set var="keepCs" value=""/>
											<c:set var="keepText" value=""/>
											<c:choose>
												<c:when test="${cart.keepYn == 'Y' }">
												<c:set var="keepCs" value="save_on"/>
												<c:set var="keepText" value="보관취소"/>
												</c:when>
												<c:otherwise>
												<c:set var="keepCs" value=""/>
												<c:set var="keepText" value="계속보관"/>
												</c:otherwise>
											</c:choose>
											<a href="#none" class="btn_style7 gray1 btn_save ${keepCs }" onclick="javascript:keep('${cart.cartProductNo}')">${keepText }</a>
										</c:if>
										<a href="#none" class="btn_style7 gray1" onclick="javascript:deleteCart('${cart.cartProductNo}');">삭제</a>
									</div>
									
								</c:when>
								<%--///////////// 가격,버튼 노출(판매중) end ////////////////--%>
								
								<%--///////////// 가격,버튼 노출(품절) start ////////////////--%>
								<c:otherwise>
								
									<div class="td_box col3">
										&nbsp;
									</div>
									<div class="td_box col4">
									${cart.saleStateName }
									</div>
									<div class="td_box col5">
										<a href="#none" class="btn_style7 gray1" onclick="javascript:deleteCart('${cart.cartProductNo}');">삭제</a>
									</div>
									
								</c:otherwise>
								<%--///////////// 가격,버튼 노출(품절) end ////////////////--%> 
								</c:choose>																												
							</div>	
						</li>	
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
					
					<%-- sub sum start --%>
					<c:if test="${sum}">
					</ul>
					<%--### //테이블 바디 ###--%>
					
						
						<%--### 장바구니 중간 금액 ###--%>
						<div class="cart_medium">
						<c:if test="${cart.deliveryPolicyNo > -1 }">
						<input type="hidden" id="minDeliveryFreeAmt${cart.deliveryPolicyNo }"  value="${cart.minDeliveryFreeAmt}"/>
						<input type="hidden" id="deliveryFee${cart.deliveryPolicyNo }"  value="${cart.deliveryFee }"/>
							<dl>
								<dt>
									<span class="pc_only">총 상품금액</span>
									<span class="mo_only">상품금액</span>
								</dt>
								<dd id="subTotalPrice${cart.deliveryPolicyNo }"><fmt:formatNumber value="${subTotalPrice }" pattern="#,###" />원</dd>													
							</dl>
							<c:if test="${cartCnt.cartTypeCd != 'CART_TYPE_CD.PICKUP' }">
							<em>+</em>
							<dl>
								<dt>
									<span class="pc_only">총 배송비</span>
									<span class="mo_only">배송비</span>
								</dt>
								<c:choose>
								<c:when test="${cart.minDeliveryFreeAmt <= subTotalPrice || totalDeliveryFeeFreeYn == 'Y'}">
									<c:set var="subTotalDeliveryFee" value="0"/>
								</c:when>
								<c:otherwise>
									<c:set var="subTotalDeliveryFee" value="${cart.deliveryFee }"/>
								</c:otherwise>
								</c:choose>
								<dd id="subDeliveryFee${cart.deliveryPolicyNo }"><fmt:formatNumber value="${subTotalDeliveryFee }" pattern="#,###" />원</dd>	
							</dl>
							<em>=</em>
							<dl>
								<dt class="pc_only">
									총 주문예상금액
								</dt>
								<dd>
									<b id="subTotalAmt${cart.deliveryPolicyNo }"><fmt:formatNumber value="${subTotalPrice + subTotalDeliveryFee }" pattern="#,###" />원</b>
								</dd>
							</dl>
							</c:if>
							<c:set var="totalDeliveryFee" value="${totalDeliveryFee + subTotalDeliveryFee }"/>
							<c:set var="totalPrice" value="${totalPrice + subTotalPrice}"/>
							<c:set var="subTotalPrice" value="0"/>
							<c:set var="subTotalDeliveryFee" value="0"/>
							<c:set var="totalDeliveryFeeFreeYn" value="N"/>
						</c:if>
						</div>
						<%--### //장바구니 중간 금액 ###--%>
					</c:if>
					<%-- sub sum end --%>
					
					</c:forEach>
					<%--///////////// 장바구니 loop end ////////////////--%>
					
					<%--### 장바구니 총 금액 ###--%>
					<div class="cart_total">
						<div class="columnL">
						<c:if test="${cartCnt.cartTypeCd != 'CART_TYPE_CD.PICKUP' }">
							<dl>
								<dt>총 상품금액</dt>
								<dd id="totalPrice"><fmt:formatNumber value="${totalPrice }" pattern="#,###" />원</dd>
							</dl>
							
							<span>+</span>

							<dl>
								<dt>총 배송비</dt>
								<dd id="totalDeliveryFee"><fmt:formatNumber value="${totalDeliveryFee }" pattern="#,###" />원</dd>
							</dl>
						</c:if>
						</div>

						<div class="columnR">
<%-- 						<c:choose> --%>
						<c:if test="${cartCnt.cartTypeCd != 'CART_TYPE_CD.PICKUP' }">							
							<span>=</span>
						</c:if>
<%-- 						<c:otherwise> --%>
 
<%-- 						</c:otherwise> --%>
<%-- 						</c:choose> --%>
							<dl>
								<dt>총 결제예상금액</dt>
								<dd id="totalAmt"><fmt:formatNumber value="${totalPrice + totalDeliveryFee }" pattern="#,###" />원</dd>
							</dl>
						</div>
					</div>
					<%--### //장바구니 총 금액 ###--%>

					<div class="choice_del">
						<a href="#none" class="btn_style7 btn_choice_del" onclick="javascript:deleteCart();">선택상품 삭제</a>
						<a href="#none" class="btn_style7 btn_disable_del" onclick="javascript:deleteEmptyCart();">품절상품 삭제</a>
						<input type="checkbox" value="" id="choice_del1" class="btn_jjim" onclick="javascript:saveZzim()"/>
						<label for="choice_del1">선택한 상품 쇼핑찜에 담기</label>
					</div>

					<div class="cart_order">
						<a href="#none" class="btn_style10 btn_choice_del" onclick="javascript:deleteCart();">
							<span>선택 삭제</span>
						</a>
						
						<c:choose>
						<c:when test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.GENERAL' }">
							<a href="#none" class="btn_style10 btn_chk_order" onclick="javascript:order('SELECT');">
								<span>선택<em class="pc_only">상품</em> 주문하기</span>
							</a>
							<a href="#none" class="btn_style10 btn_all_order" onclick="javascript:order('ALL');">
								<span>전체<em class="pc_only">상품</em> 주문하기</span>
							</a>
						</c:when>
						<c:otherwise>
							<a href="#none" class="btn_style10 btn_chk_order" >
								<span>상품 추가하기</span>
							</a>
							<a href="#none" class="btn_style10 btn_all_order" onclick="javascript:order('SELECT');">
								<c:choose>
								<c:when test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.PICKUP' }">
									<span>매장픽업 신청</span>
								</c:when>
								<c:otherwise>
									<span>정기배송 신청</span>
								</c:otherwise>
								</c:choose>
							</a>
						</c:otherwise>
						</c:choose>
					</div>																						
				</div>
				</c:when>
				<%--///////////// 장바구니 상품있을때 end ////////////////--%>
					
				<%--///////////// 장바구니 상품없을때 start ////////////////--%>
				<c:otherwise>
				<div class="tabcont tabcontShow">
					<ul class="cart_tb_tbody1">
						<li class="empty">
							<div class="tr_box">
								<div class="td_box col99">
									장바구니에 담긴 상품이 없습니다.
								</div>
							</div>
						</li>
					</ul>
				</div>
				</c:otherwise>
				<%--///////////// 장바구니 상품없을때 end ////////////////--%>
			</c:choose>
<%--				////////////////// 장바구니,매장픽업,정기배송 탭 끝 //////////////////--%>
		</div>
<%--				////////////////// 추천상품 영역 시작 //////////////////--%>			
		<!-- 추천 상품 -->
		<div class="item_list_box2 prodGood">
			<h2>추천 상품</h2>
			<div class="item_list">
				<ul>
					<li>
						<a href="#none1">
							<div class="img"><img src="/resources/img/pc/temp/temp_list_01.jpg" alt="" /></div>
							<div class="info">
								<div class="flag">
									<span class="ico_flag best">best</span>
									<span class="ico_flag new">new</span>
								</div>
								<p class="name">[allo&amp;lugh] 루포인트 레인코트1111111111111</p>
								<div class="price">
									<u>17,900</u>
									<span>33,900원</span>

									<button type="button" class="rw_btnWish "><span>찜</span></button>
								</div>
							</div>
						</a>

						<a href="#none2" class="mo_only">
							<div class="img"><img src="/resources/img/pc/temp/temp_list_01.jpg" alt="" /></div>
							<div class="info">
								<div class="flag">
									<span class="ico_flag best">best</span>
									<span class="ico_flag new">new</span>
								</div>
								<p class="name">[allo&amp;lugh] 루포인트 레인코트1111111111111</p>
								<div class="price">
									<u>17,900</u>
									<span>33,900원</span>

									<button type="button" class="rw_btnWish "><span>찜</span></button>
								</div>
							</div>
						</a>
					</li>

					<li>
						<a href="#none3">
							<div class="img"><img src="/resources/img/pc/temp/temp_list_02.jpg" alt="" /></div>
							<div class="info">
								<div class="flag">
									<span class="ico_flag best">best</span>
									<span class="ico_flag new">new</span>
								</div>
								<p class="name">[allo&amp;lugh] 루포인트 레인코트11</p>
								<div class="price">
									<u>17,900</u>
									<span>33,900원</span>

									<button type="button" class="rw_btnWish "><span>찜</span></button>
								</div>
							</div>
						</a>

						<a href="#none4" class="mo_only">
							<div class="img"><img src="/resources/img/pc/temp/temp_list_01.jpg" alt="" /></div>
							<div class="info">
								<div class="flag">
									<span class="ico_flag best">best</span>
									<span class="ico_flag new">new</span>
								</div>
								<p class="name">[allo&amp;lugh] 루포인트 레인코트1111111111111</p>
								<div class="price">
									<u>17,900</u>
									<span>33,900원</span>

									<button type="button" class="rw_btnWish "><span>찜</span></button>
								</div>
							</div>
						</a>
					</li>

					<li>
						<a href="#none5">
							<div class="img"><img src="/resources/img/pc/temp/temp_list_01.jpg" alt="" /></div>
							<div class="info">
								<div class="flag">
									<span class="ico_flag best">best</span>
									<span class="ico_flag new">new</span>
								</div>
								<p class="name">[allo&amp;lugh] 루포인트 레인코트11</p>
								<div class="price">
									<u>17,900</u>
									<span>33,900원</span>

									<button type="button" class="rw_btnWish "><span>찜</span></button>
								</div>
							</div>
						</a>

						<a href="#none6" class="mo_only">
							<div class="img"><img src="/resources/img/pc/temp/temp_list_01.jpg" alt="" /></div>
							<div class="info">
								<div class="flag">
									<span class="ico_flag best">best</span>
									<span class="ico_flag new">new</span>
								</div>
								<p class="name">[allo&amp;lugh] 루포인트 레인코트1111111111111</p>
								<div class="price">
									<u>17,900</u>
									<span>33,900원</span>

									<button type="button" class="rw_btnWish "><span>찜</span></button>
								</div>
							</div>
						</a>
					</li>

					<li>
						<a href="#none7">
							<div class="img"><img src="/resources/img/pc/temp/temp_list_02.jpg" alt="" /></div>
							<div class="info">
								<div class="flag">
									<span class="ico_flag best">best</span>
									<span class="ico_flag new">new</span>
								</div>
								<p class="name">[allo&amp;lugh] 루포인트 레인코트11</p>
								<div class="price">
									<u>17,900</u>
									<span>33,900원</span>

									<button type="button" class="rw_btnWish "><span>찜</span></button>
								</div>
							</div>
						</a>

						<a href="#none8" class="mo_only">
							<div class="img"><img src="/resources/img/pc/temp/temp_list_01.jpg" alt="" /></div>
							<div class="info">
								<div class="flag">
									<span class="ico_flag best">best</span>
									<span class="ico_flag new">new</span>
								</div>
								<p class="name">[allo&amp;lugh] 루포인트 레인코트1111111111111</p>
								<div class="price">
									<u>17,900</u>
									<span>33,900원</span>

									<button type="button" class="rw_btnWish "><span>찜</span></button>
								</div>
							</div>
						</a>
					</li>
				</ul>

				<ol class="list_dot mo_only">
					<li class="on">
						<button type="button">1</button>
					</li>
					<li>
						<button type="button">2</button>
					</li>
					<li>
						<button type="button">3</button>
					</li>
					<li>
						<button type="button">4</button>
					</li>
				</ol>
			</div>
		</div>
		<!-- //추천 상품 -->
<%--				////////////////// 추천상품 영역 끝 //////////////////--%>
		
		<div class="txt_info">
			<ul>
				<li>
					장바구니 담은 상품의 보관기간은 30일입니다.
				</li>
				<li>
					무이자 할부 개월수가 다른 상품을 같이 주문하면 할부 개월수가 낮은 상품기준으로 할부가 적용됩니다. 해당 상품만 별도 주문하면 상품에 적용된 무이자 할부 혜택을 받으실 수 있습니다.
				</li>
				<li>
					계속 보관을 선택하시면 결제 후에도 상품이 장바구니에 계속 보관됩니다.
				</li>
			</ul>
		</div>

<%--				////////////////// mobile 고정버튼 영역 시작 //////////////////--%>
			<%--### 고정 버튼 ###--%>
			<%--
			<div class="cart_fixed">
				<dl>
					<dt>총 
					<c:if test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.GENERAL' }">
					${cartCnt.generalCnt }
					</c:if>
					<c:if test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.PICKUP' }">
					${cartCnt.pickupCnt }
					</c:if> 
					<c:if test="${cartCnt.cartTypeCd == 'CART_TYPE_CD.REGULARDELIVERY' }">
					${cartCnt.regulardeliveryCnt }
					</c:if>  
					개</dt>
					<dd>
						<fmt:formatNumber value="${totalPrice + totalDeliveryFee }" pattern="#,###"/><em>원</em>
					</dd>
				</dl>

				<div class="alignC">
					<button type="button" class="btn_cartType1 btn_choice_del" onclick="javascript:deleteCart();">선택 삭제하기</button>
					<button type="button" class="btn_cartType2" onclick="javascript:order('SELECT');">선택 주문하기</button>
					<button type="button" class="btn_cartType3" onclick="javascript:order('ALL');">전체 주문하기</button>
				</div>
			</div>
			 --%>
			<%--### //고정 버튼 ###--%>
<%--				////////////////// mobile 고정버튼 영역 끝 //////////////////--%>
		
	</div>
	<!-- cart_box -->
</div>
<!-- inner -->

<%--### 매장 위치 레이어 ###--%>
<div class="layer_type1 layer_shop">
	<div class="layer_inner">
		<strong class="title">매장정보</strong>

		<div class="inner">
			<div class="box">
				<dl>
					<dt id="layer_name"></dt>
					<dd id="layer_addr">						
					</dd>
				</dl>

				<div class="shop_map">
					<img src="/resources/img/pc/temp/shop_map.jpg" alt="" />					
				</div>
			</div>
		</div>

		<button type="button" class="btn_close">창 닫기</button>
	</div>
</div>
<%--### //매장 위치 레이어 ###--%>
<c:if test="${changeFlag == 'Y' && cartCnt.cartTypeCd == 'CART_TYPE_CD.GENERAL'}">
<script type="text/javascript">
changeTotalSalePrice();
</script>
</c:if>