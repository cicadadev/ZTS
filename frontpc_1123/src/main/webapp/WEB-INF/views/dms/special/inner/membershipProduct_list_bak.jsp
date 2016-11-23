<%--
	화면명 : 멤버쉽관 > 인증 후
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ page import="gcp.frontpc.common.contants.Constants"%>
<%
	pageContext.setAttribute("exhibitAfter", Config.getString("common.special.membership.bnr.exhibit.after"));
%>
<script type="text/javascript" src="/resources/js/jquery.countdown.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
// 	special.membership.refresh('${fn:length(bottomBnr)-1}');
});
</script>
<%-- <c:set var="exhibitBnr"  value="${cornerMap[exhibitAfter]}"/> --%>
<div class="cateBnr">
	<ul>
		<li><a href="javascript:void(0);"><img src="/resources/img/pc/temp/temp_bnr_cate.jpg" alt="궁중비책 물티슈 &amp;스킨케어 한정특가" /></a></li>
		<li><a href="javascript:void(0);"><img src="/resources/img/pc/temp/temp_bnr_cate.jpg" alt="궁중비책 물티슈 &amp;스킨케어 한정특가" /></a></li>
		<li><a href="javascript:void(0);"><img src="/resources/img/pc/temp/temp_bnr_cate.jpg" alt="궁중비책 물티슈 &amp;스킨케어 한정특가" /></a></li>
	</ul>
</div>
<div class="productListBox type1">
	<ul>
		<c:forEach var="product" items="${productList}" varStatus="i">
			<c:set var="product" value="${product}" scope="request" />
			<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
				<jsp:param name="type" value="member" />
				<jsp:param name="dealProductIndex" value="${i.index }" />
			</jsp:include>
		</c:forEach>
		<c:if test="${empty productList}">
			상품이 없습니다.
		</c:if>
	</ul>
</div>