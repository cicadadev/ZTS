<%--
	화면명 : 멤버쉽관 > 인증 전
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript" src="/resources/js/jquery.countdown.min.js"></script>
<ul>
	<c:choose>
		<c:when test="${fn:length(productList) > 0}">
			<c:forEach var="product" items="${productList}" varStatus="i">
				<c:set var="product" value="${product}" scope="request" />
				<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
					<jsp:param name="type" value="premium" />
					<jsp:param name="certify" value="false" />
					<jsp:param name="dealProductIndex" value="${i.index }" />
				</jsp:include>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<c:if test="${empty productList}">
				상품이 없습니다.
			</c:if>
		</c:otherwise>
	</c:choose>
</ul>