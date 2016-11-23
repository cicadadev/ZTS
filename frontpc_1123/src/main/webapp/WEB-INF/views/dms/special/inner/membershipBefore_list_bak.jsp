<%--
	화면명 : 멤버쉽관 > 인증 전
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
	pageContext.setAttribute("topBefore", Config.getString("corner.special.mem.img.1"));
%>
<script type="text/javascript">
$(document).ready(function(){
// 	special.membership.refresh('${fn:length(bottomBnr)-1}');
});
</script>
<c:set var="topBnr"  value="${cornerMap[topBefore]}"/>
<div class="memVisual">
	<div class="boxInner">
		<div class="memTxt">
			<h2>멤버십관</h2>
			<span class="left">제로투세븐 회원전용</span>
			<span class="right">저렴한 가격과 엄선된 상품만을 제공해 드립니다</span>
		</div>
		<div class="memImg">
			<c:forEach var="item" items="${topBnr.dmsDisplayitems}" varStatus="status">
				<li ${status.index ==0 ? 'class="on"' : '' }>
					<c:choose>
						<c:when test="${isMobile}">
							<img src="${_IMAGE_DOMAIN_}${item.img2}" alt="${item.text2}" />
						</c:when>
						<c:otherwise>
							<img src="${_IMAGE_DOMAIN_}${item.img1}" alt="${item.text1}" />		
						</c:otherwise>
					</c:choose>
				</li>
			</c:forEach>
			<i class="ribbon">membership</i>
		</div>
	</div>
	
<!-- 	<div class="boxInner"> -->
<!-- 		<div class="memCont"> -->
<!-- 			<h2 class="memcTit"><img src="/resources/img/pc/txt/txt_mem01.png" alt="회원전용 한정특가 - 멤버십관을 아시나요?" /></h2> -->
<!-- 			<div class="memcListWrap"> -->
<!-- 				<ul class="info_list"> -->
<!-- 					<li>제로투세븐 회원전용</li> -->
<!-- 					<li>저렴한 가격과 엄선된 상품만을 제공해 드립니다</li> -->
<!-- 				</ul> -->
<!-- 			</div> -->
<!-- 			<p class="memcTxt"><img src="/resources/img/pc/txt/txt_mem02.png" alt="지금 회원가입 후 우리아이 맞춤정보 를 설정 하시면 회원 전용 멤버십관에 바로 입장하실 수 있습니다."></p> -->
<!-- 			<div class="memcbtnWrap"> -->
<%-- 				<a href="javascript:void(0);" class="btn_prev01" onclick="javascript:special.membership.move('${loginId}');"><span>관심정보 설정하기</span></a> --%>
<%-- 				<a href="javascript:void(0);" class="btn_prev02" onclick="javascript:special.membership.move('${loginId}','${membershipYn}');"><span>멤버십관 입장하기</span></a> --%>
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 	</div> -->
	
</div>

<div class="certifyMem">
	<h3 class="sub_tit1">멤버십관 맛보기! 이런 행사가 진행중입니다.</h3>
	
	<!-- 상단 비주얼 -->
	<div class="visual type1">
		<ul class="vImg">
			<li class="on"><a href="#none"><img src="/resources/img/pc/temp/img_eventBanner01.jpg" alt="우리아이와 가을데이트룩 어때요?" /></a></li>
			<li><a href="#none"><img src="/resources/img/pc/temp/img_eventBanner02.jpg" alt="우리아이와 가을데이트룩 어때요?" /></a></li>
		</ul>
		<div class="control">
			<div class="dots hide">
				<span class="on"></span>
				<span></span>
			</div>
			<div class="btns">
				<button type="button" class="prev">이전</button>
				<button type="button" class="next">다음</button>
			</div>
		</div>
	</div>
	<!-- //상단 비주얼 -->
</div>

<%-- 		<c:forEach var="item" items="${exhibitBnr.dmsDisplayitems}" varStatus="status"> --%>
<%-- 			<li ${status.index ==0 ? 'class="on"' : '' }> --%>
<%-- 				<c:choose> --%>
<%-- 					<c:when test="${isMobile}"> --%>
<%-- 						<img src="${_IMAGE_DOMAIN_}${item.img2}" alt="${item.text2}" /> --%>
<%-- 					</c:when> --%>
<%-- 					<c:otherwise> --%>
<%-- 						<img src="${_IMAGE_DOMAIN_}${item.img1}" alt="${item.text1}" />		 --%>
<%-- 					</c:otherwise> --%>
<%-- 				</c:choose> --%>
<!-- 			</li> -->
<%-- 		</c:forEach> --%>
<!-- 		<i class="ribbon">membership</i> -->


<div class="product_type1 prodType_4ea block">
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