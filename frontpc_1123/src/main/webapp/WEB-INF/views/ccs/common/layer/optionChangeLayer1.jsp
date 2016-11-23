<%--
	화면명 : 세트상품 옵션변경
	작성자 : victor
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<div class="con">
	<strong class="title">옵션변경</strong>
	<dl>
		<c:forEach var="set" items="${pmsSetproducts}" varStatus="idx1">
			<dt>${set.name}</dt>
			<dd>
<%-- 				<p>${set.pmsProduct.name}</p> : 삭제요청에 의한 삭제--%>
				<div class="select_box1">
					<label optionName="${set.name}" id="${set.name}_label">${set.saleproductName}</label>
					<select name="optionvalue" id="optionvalue_${idx1.count}">
						<option value="">선택하세요</option>
						<c:forEach var="product" varStatus="idx2" items="${set.pmsSaleproducts}">
							<option value="${product.saleproductId}" 
								data-product-id="${set.subProductId}"
								data-saleproduct-id="${product.saleproductId}"
								data-saleproduct-name="${product.name}"
								data-add-sale-price="${product.addSalePrice}"
<%-- 								data-total-sale-price="${product.totalSalePrice}" --%>
								data-set-name="${set.name}"
								${product.realStockQty < 1 ? ' disabled="disabled" style="color: red"' : ''}
								${product.saleproductId == set.saleproductId ? ' selected' : ''}>
								${product.name}${product.realStockQty < 1 ? '(품절)' : ''}
							</option>
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
					<button type="button" class="btn_minus">수량빼기</button>
					<input type="text" value="1">
					<button type="button" class="btn_plus">수량추가</button>
				</div>
			</dd>
		</dl>		
	</div>
	<div class="btn_wrapC btn1ea">		
		<a href="javascript:void(0);" class="btn_sStyle3 sPurple1" onclick="oms.changeOptionLayer.callback();">변경</a>
	</div>
	<a href="javascript:void(0);" class="btn_close">옵션창 닫기</a>
</div>
<script type="text/javascript">
<!--
	$(function() {
		$(".option_box .btn_close").off("click").on({
			"click" : function(e) {
				if ($(this).closest(".option_box").hasClass("gift_box")) {
					$(this).closest(".option_box").prev().removeClass("on");
				}
				$(this).closest(".option_box").hide();
				$(this).closest(".option_box").html('');
			}
		});
	});
//-->
</script>
