<%--
	화면명 : 
	작성자 : ALLEN
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript">
	$("[name=TOTAL_CNT]").val("${totalCount}");
</script>
<form name="orderForm">


</form>

<c:forEach items="${orderList}"  var="order" varStatus="i">

<fmt:parseDate value="${order.orderDt}" var="dateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
<fmt:formatDate value="${dateFmt}" var="orderDt" pattern="yyyy/MM/dd"/>

<ul class="div_tb_tbody3">
		<li onClick="javascript:mypage.offlineOrder.offOrderDetail('${order.orderId}');">
			<div class="tr_info">
				<span class="date">${orderDt}</span>
				<span class="st">
					${order.ccsOffshop.name}
				</span>
		
				<div class="colR">
					<span class="price">
						<fmt:formatNumber type="currency" value="${order.orderAmt}" pattern="###,###" /><i>원</i>
					</span>
				</div>
			</div>
			<c:forEach items="${order.omsPosorderproducts}" var="product" varStatus="j">
				<div class="tr_box">
					<div class="col1">
						<div class="positionR">
							<a href="#none" class="title">
<%-- 								[${product.brandName}]  --%>
								${product.productName}
							</a>
			
<!-- 							<em class="option_txt"> -->
<!-- 								<i>Red / 2Y</i> -->
<!-- 							</em> -->
			
							<span class="number">${product.orderQty}개</span>
						</div>
					</div>
			
					<div class="col2">
						<span class="price">
							<fmt:formatNumber type="currency" value="${product.salePrice}" pattern="###,###" /><i>원</i>
						</span>
					</div>
				</div>
			</c:forEach>
		</li>
</ul>
</c:forEach>
<c:if test="${empty orderList}">
	<p class="empty">오프라인 구매내역이 없습니다.</p>
</c:if>
<div class="paginateType1">
	<page:paging formId="orderForm" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
			total="${totalCount}" url="/mms/mypage/offlineOrder/list/ajax" type="ajax" callback="listCallback"/>
</div>
	
		