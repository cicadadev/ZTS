<%--
	화면명 : 전문관 > 임직원관
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>


<script type="text/javascript">
	$('input[name="totalCount"]').val(Number('${totalCount}'));
</script>

<c:if test="${!isMobile }">
	<div class="list_group">
		<div class="product_type1 prodType_4ea block">
</c:if>
 
		<ul>
			<c:forEach var="product" items="${productList}" varStatus="i">
				<c:set var="product" value="${product}" scope="request" />
				<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
					<jsp:param name="type" value="officer" />
					<jsp:param name="dealProductIndex" value="${i.index }" />
				</jsp:include>
			</c:forEach>
<%-- 			<c:if test="${empty productList}"> --%>
<!-- 				<p class="empty">상품 목록이 없습니다.</p> -->
<%-- 			</c:if> --%>
		</ul>

<c:if test="${!isMobile }">
	</div>
	
	<!-- ### PC 페이징 ### -->
	<div class="paginateType1">
		<page:paging formId="valueForm" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
			total="${totalCount}" url="/dms/special/employee/productList/ajax" type="ajax" callback="special.employee.pageCallback"/>
	</div>
	<!-- ### //PC 페이징 ### -->
</div>
</c:if>
	
