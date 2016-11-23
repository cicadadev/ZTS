<%--
	화면명 : 프론트 & 모바일  대,중 카테고리 코너 상품 정보
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<script type="text/javascript">

$(document).ready(function(){
	//dms.search.orderBy.pageInit();
	special.common.category.orderBy.cornerPageInit();
	$('#cornerSearchCount').empty();
	$('#cornerSearchCount').append('${csearch.totalCount}');
	
});

</script>
<form action = "" id="dmsDisplaySearch" name="dmsDisplaySearch" method="post">
	<input type="hidden" id="displayId" 			name="displayId" 			value="${csearch.displayId}"/>
	<input type="hidden" id="currentPage" 			name="currentPage"			value="${csearch.currentPage}"/>
	<input type="hidden" id="pageSize"				name="pageSize"				value="${csearch.pageSize}"/>
	<input type="hidden" id="cornerSort"			name="sort"					value="${csearch.sort}"/>
</form>

<c:if test="${not empty prdList}">
	<div class="product_type1 prodType_4ea block">
		<ul>
			<c:forEach var="product" items="${prdList}" varStatus="status">
				<c:set var="product" value="${product}" scope="request" />
				<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false"/>
			</c:forEach>
		</ul>
	</div>
	
	<div class="paginateType1">
		<page:paging formId="dmsDisplaySearch" currentPage="${csearch.currentPage}" pageSize="${csearch.pageSize}" 
			total="${csearch.totalCount}" callback="special.common.category.cornerCallback" type="ajax" url="/dms/corner/productList?displayId=${csearch.displayId}"/>
	</div>
</c:if>
