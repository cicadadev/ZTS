<%--
	화면명 : 
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript" src="/resources/js/jquery.countdown.min.js"></script>	
<script type="text/javascript">
$(document).ready(function() {
	if(ccs.common.mobilecheck()) {
		sps.deal.shockingzero.viewMore();
	}
});
</script>

<div>
	<input type="hidden" name="totalCnt" id="totalCnt" value="${totalCount}" />
	<input type="hidden" name="sortType" id="sortType" value="${search.sortType}" />
	<input type="hidden" name="currentPage" id="currentPage" value="${search.currentPage}" />
</div>

<c:forEach items="${shockingProducts}" var="dealProduct" varStatus="status">
	<c:set var="dealProduct" value="${dealProduct}" scope="request" />
	<c:set var="product" value="${dealProduct.pmsProduct}" scope="request" />
	<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
		<jsp:param value="${search.firstRow + status.index}" name="dealProductIndex"/>
		<jsp:param name="type" value="shocking" />
	</jsp:include>
</c:forEach>

