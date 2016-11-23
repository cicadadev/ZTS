<%--
	화면명 : 공통 - 추천상품
	작성자 : eddie
 --%>

<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>

<c:if test="${productsSize gt 0}">
	<h2 class="tit_style2">‘<c:out value="${keyword}"/>’ 연관 상품을 추천합니다.</h2>
	<div class="swiper_wrap">
		<div class="prodSwiper product_type1 prodType_4ea block swiper-container cartSwiper_pordList">
			<ul class="swiper-wrapper">
				<c:forEach var="product" items="${products}" varStatus="status">
					<c:set var="product" value="${product}" scope="request" />
					<c:set var="flagNone" value="style='display:none" scope="request" />
					<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
						<jsp:param name="flagNone" value="style='display:none'"/>
						<jsp:param name="swipe" value="Y"/>
					</jsp:include>
				</c:forEach>
			</ul>
			<!-- Add Arrows -->
			<div class="swiper-button-next btn_tp2"></div>
			<div class="swiper-button-prev btn_tp2"></div>
		</div>
	</div>
</c:if>