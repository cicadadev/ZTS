<%--
	화면명 : 모바일  카테고리 selectBox 목록
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<script type="text/javascript">
$(document).ready(function(){
	special.common.category.orderBy.pageInit($('#displayCategoryId').val());
});
</script>
<input type="hidden" id="displayCategoryId" 		name="displayCategoryId" 		value="${categorySearch.displayCategoryId}">
<input type="hidden" id="upperDisplayCategoryId" 	name="upperDisplayCategoryId" 	value="${categorySearch.upperDisplayCategoryId}">
<input type="hidden" id="categoryName" 				name="categoryName" 			value="${categorySearch.name}"/>
<input type="hidden" id="eventType"					name="eventType" 				value="${categorySearch.linkType}"/>
<input type="hidden" id="categoryViewType" 			name="categoryViewType"			value="${categorySearch.categoryViewType}"	/>

<c:if test="${not empty category.dmsDisplaycategorys}">
	<label></label>
	<select id ="birthSelect">
		<c:forEach var="list" items="${category.dmsDisplaycategorys}" varStatus="status">
			<option value="${list.displayCategoryId}"><c:out value="${list.name}"/> </option>
		</c:forEach>
	</select>
</c:if>
