<%--
	화면명 : 옵션변경 의 콤보박스를 페이지에 박을때 처리
	작성자 : eddie
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<div class="col2 optList">
	<c:forEach var="option" items="${optionList}" varStatus="index">
		<div>
			<i>${option.optionName}</i>
			<div class="select_box1">
				<label id="${option.optionName}_label">${selected[option.optionName]}</label>
				<select optionName="${option.optionName}" name="prdOption_${index.count}"
					onchange="pms.detail.selectProductOption(this.name, ${fn:length(optionList)} ,'${productId }');">
					<option value="">선택하세요</option>
					<c:forEach var="value" items="${option.optionValues}">
						<option ${value.optionValue eq selected[option.optionName] ? 'selected' : ''}
							${value.realStockQty < 1 ? ' disabled="disabled" style="color: red"' : ''}>${value.optionValue}</option>
					</c:forEach>
				</select>
			</div>
		</div>
	</c:forEach>
	<script type="text/javascript">
	<!--
	__price_mode__ = 1; //1: 일반, 2:정기배송, 3: 픽업
	var selectSaleproduct = function(saleproduct) {
		saleproduct.qty = 1;// 기본수량 1
// 		if (!pms.detail.addSaleproductOption(saleproduct)) {
// 			return;
// 		}
// 		$('[name="newSaleProductId"]').val(saleproduct.saleproductId); 
// 		$('[name="newSaleProductNm"]').val(saleproduct.name); 
		$('[name="omsOrderproducts[0].newSaleProductId"]').val(saleproduct.saleproductId); 
		$('[name="omsOrderproducts[0].newSaleProductNm"]').val(saleproduct.name); 
	}
	//-->
</script>
</div>