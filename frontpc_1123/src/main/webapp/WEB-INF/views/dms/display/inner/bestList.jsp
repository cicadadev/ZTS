<%--
	화면명 : 
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">
$(document).ready(function() {
	
});
</script>

<ul>
	<c:forEach items="${products}" var="product" varStatus="status">
		<c:set var="product" value="${product}" scope="request" />
		<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
			<jsp:param value="${status.index}" name="dealProductIndex"/>
			<jsp:param name="type" value="best" />
			<jsp:param name="bestRanking" value="${status.index + 1}"/>
		</jsp:include>				
	</c:forEach>
</ul>
