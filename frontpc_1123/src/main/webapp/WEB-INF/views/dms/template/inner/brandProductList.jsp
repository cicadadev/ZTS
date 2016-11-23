<%--
	화면명 : 브랜드관 > 실시간 인기상품
	작성자 : stella
 --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>


<c:if test="${not empty bestProductList}">		<!-- 베스트 상품(템플릿B) -->
	<c:forEach var="products" items="${bestProductList}" varStatus="best_status">
		<c:set var="product" value="${products}" scope="request" />
		
		<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp">
			<jsp:param name="swipe" value="Y"/>
		</jsp:include>
	</c:forEach>
</c:if>

<c:if test="${not empty realtimeProductList}">		<!-- 실시간 인기상품 -->
	<c:forEach var="products" items="${realtimeProductList}" varStatus="rt_status">	
		<c:set var="product" value="${products}" scope="request" />
		
		<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp">
			<jsp:param name="swipe" value="Y"/>
		</jsp:include>		
	</c:forEach>
</c:if>

