<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script type="text/javascript">
var direct_saveCart = function(){
		
	var data = $("#cartFormNew");
	
	var productId = data.find("#productId").val();
	var saleproductId = data.find("#saleproductId").val();
	
	var subProducts = data.find("input[name^='omsCarts']");
	var subSaleproducts = data.find("select[name^='omsCarts'] option:selected");
	var cartTypeCd = data.find("select[name^='cartTypeCd'] option:selected").val();
	var offshopId = data.find("#offshopId").val();
	var dealId = data.find("#dealId").val();
	var channelId = data.find("#channelId").val();
	
	var omsCarts = [];
	
	for(var i=0;i<subProducts.length;i++){
		subProductId = subProducts[i].value;
		subSaleproductId = subSaleproducts[i].value;
		omsCarts.push({productId : subProductId,saleproductId:subSaleproductId});
	}
	
	var cart = new oms.Cart(productId,saleproductId,omsCarts,cartTypeCd,offshopId,null,dealId,channelId);
	
// 	console.log(cart);
	
	oms.saveCart(cart,function(response){
		alert("성공");
	});
}

var direct_saveCartList = function(){

	var paramArray = [];
	
	for(var k=0;k<3;k++){
		var data = $("#cartFormNew");
		
		var productId = data.find("#productId").val();
		var saleproductId = data.find("#saleproductId").val();
		
		var subProducts = data.find("input[name^='omsCarts']");
		var subSaleproducts = data.find("select[name^='omsCarts'] option:selected");
		var cartTypeCd = data.find("select[name^='cartTypeCd'] option:selected").val();
		var offshopId = data.find("#offshopId").val();
		var dealId = data.find("#dealId").val();
		var channelId = data.find("#channelId").val();
		
		var omsCarts = [];
		
		for(var i=0;i<subProducts.length;i++){
			subProductId = subProducts[i].value;
			subSaleproductId = subSaleproducts[i].value;
			omsCarts.push({productId : subProductId,saleproductId:subSaleproductId});
		}

		var cart = new oms.Cart(productId,saleproductId,omsCarts,null,offshopId,null,dealId,channelId);
// 		console.log(cart);
		paramArray.push(cart);
	}
	
	console.log(paramArray);
	
	oms.saveCartList(paramArray,function(response){
		alert("성공");
	});
}

var direct_order = function(){
	var data = $("#cartFormNew");
	
	var productId = data.find("#productId").val();
	var saleproductId = data.find("#saleproductId").val();
	
	var subProducts = data.find("input[name^='omsCarts']");
	var subSaleproducts = data.find("select[name^='omsCarts'] option:selected");
	var cartTypeCd = data.find("select[name^='cartTypeCd'] option:selected").val();
	var offshopId = data.find("#offshopId").val();
	var dealId = data.find("#dealId").val();
	var channelId = data.find("#channelId").val();
	var giftYn = data.find("#giftYn").val();
	
	var qty = data.find("#qty").val();
	
	var omsOrderproducts = [];
	
	for(var i=0;i<subProducts.length;i++){
		subProductId = subProducts[i].value;
		subSaleproductId = subSaleproducts[i].value;
		omsOrderproducts.push({productId : subProductId,saleproductId:subSaleproductId});
	}
	
	var order = new oms.Order(productId,saleproductId,omsOrderproducts,qty,dealId,channelId,giftYn);
	
	oms.directOrder(order);
}

var direct_orderList = function(){
	
	var orderArr = [];
	for(var k=0;k<4;k++){
		
		var data = $("#cartFormNew");
		
		var productId = data.find("#productId").val();
		var saleproductId = data.find("#saleproductId").val();
		
		var subProducts = data.find("input[name^='omsCarts']");
		var subSaleproducts = data.find("select[name^='omsCarts'] option:selected");
		var cartTypeCd = data.find("select[name^='cartTypeCd'] option:selected").val();
		var offshopId = data.find("#offshopId").val();
		var dealId = data.find("#dealId").val();
		var channelId = data.find("#channelId").val();
		var giftYn = data.find("#giftYn").val();
		
		var qty = data.find("#qty").val();
		
		var omsOrderproducts = [];
		
		for(var i=0;i<subProducts.length;i++){
			subProductId = subProducts[i].value;
			subSaleproductId = subSaleproducts[i].value;
			omsOrderproducts.push({productId : subProductId,saleproductId:subSaleproductId});
		}
		
		var order = new oms.Order(productId,saleproductId,omsOrderproducts,qty,dealId,channelId,giftYn);
		orderArr.push(order);
	}
	oms.directOrderList(orderArr);
}
</script>

<div align="center" class="content">
		<form name="cartFormNew" id="cartFormNew">		
		<input type="text" name="keepYn" value="N"/>
		<input type="text" name="offshopId" id="offshopId" value="CAB314"/>	<br/>	
		
		PRODUCT_ID : <input type="text" id="productId" name="productId" value="118"/><br/>
		SALEPRODUCT_ID : <input typ="text" id="saleproductId" name="saleproductId" value="163"/><br/>
		GIFT_YN : <input typ="text" id="giftYn" name="giftYn" value="N"/><br/>
		QTY : <input type="text" id="qty" name="qty" value="1"/><br/>
		DEAL ID : <input type="text" id="dealId" name="dealId" value=""/><br/>
		CHANNEL ID : <input type="text" id="channelId" name="channelId" value=""/><br/>
		CART_TYPE_CD :	<select id="cartTypeCd" name="cartTypeCd">
							<option value="CART_TYPE_CD.GENERAL" selected="selected">일반</option>
							<option value="CART_TYPE_CD.PICKUP">매장픽업</option>
							<option value="CART_TYPE_CD.REGULARDELIVERY">정기배송</option>
						</select> 
<%--		<input type="text" id="cartTypeCd" name="cartTypeCd" value="CART_TYPE_CD.GENERAL"/><br/>--%>
		<br/>
		
		PRODUCT_ID : <input typ="text" id="1_productId"  name="omsCarts[0].productId" value="2458419"/><br/>
		SALEPRODUCT ID : 
		<select id="1_saleproductId" name="omsCarts[0].saleproductId">
			<option value="10588845">단품1</option>
<!-- 			<option value="2">단품2</option>		 -->
		</select><br/><br/>
		
		PRODUCT_ID : <input typ="text" id="118_productId"  name="omsCarts[1].productId" value="2458420"/><br/>
		SALEPRODUCT ID : 
		<select id="118_saleproductId" name="omsCarts[1].saleproductId">
			<option value="10588874">단품1</option>
<!-- 			<option value="157">단품2</option> -->
<!-- 			<option value="158">단품3</option> -->
<!-- 			<option value="159">단품4</option> -->
<!-- 			<option value="160">단품5</option> -->
<!-- 			<option value="161">단품6</option> -->
<!-- 			<option value="162">단품7</option> -->
<!-- 			<option value="163">단품8</option> -->
<!-- 			<option value="164">단품9</option>		 -->
		</select><br/>
		<input type="button" onclick="javascript:direct_saveCart();" value="입력"/>
		<input type="button" onclick="javascript:direct_saveCartList();" value="입력Multi"/>
		<input type="button" onclick="javascript:direct_order();" value="바로구매"/>
		<input type="button" onclick="javascript:direct_orderList();" value="바로구매Multi"/>
		
		</form>
</div>