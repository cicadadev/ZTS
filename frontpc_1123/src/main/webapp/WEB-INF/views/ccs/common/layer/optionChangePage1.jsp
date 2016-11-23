<%--
	화면명 : 세트상품 옵션변경
	작성자 : victor
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<div class="optList" style="text-align: right;">
	<c:forEach var="set" items="${pmsSetproducts}" varStatus="idx1">
		<div>
			<i>${set.name}</i>
			<div class="select_box1">
				<label optionName="${set.name}" id="${set.name}_label" style="text-align: left;">//${set.saleproductName}</label>
				<select name="optionvalue" id="optionvalue_${idx1.count}" onchange="selectSetOption($(this));">
					<option value="">선택하세요</option>
					<c:forEach var="product" varStatus="idx2" items="${set.pmsSaleproducts}">
						<option value="${product.saleproductId}" 
							data-product-id="${set.subProductId}" 
							data-saleproduct-id="${product.saleproductId}"
							data-saleproduct-name="${product.name}" 
							data-add-sale-price="${product.addSalePrice}"
							data-set-name="${set.name}"
							${product.realStockQty < 1 ? ' disabled="disabled" style="color: red"' : ''} 
							${product.saleproductId == set.saleproductId ? ' selected' : ''}>${product.name}${product.realStockQty < 1 ? '(품절)' : ''}</option>
					</c:forEach>
				</select>
			</div>
		</div>
	</c:forEach>
</div>
