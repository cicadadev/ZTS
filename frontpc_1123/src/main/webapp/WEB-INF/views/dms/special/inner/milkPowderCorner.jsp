<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%
	pageContext.setAttribute("info", Config.getString("corner.special.milk1.img.2"));
%>

<c:set var="infoMap"  value="${cornerMap[info]}"/>
<c:if test="${infoMap.totalCount > 0}">
	<div class="milk">
		<div class="img">
		<c:choose>
			<c:when test="${isMobile}">
				<img src="${_IMAGE_DOMAIN_}${infoMap.dmsDisplayitems['0'].img2}" alt="" />
			</c:when>
			<c:otherwise>
				<img src="${_IMAGE_DOMAIN_}${infoMap.dmsDisplayitems['0'].img1}" alt="" />
			</c:otherwise>
		</c:choose>
		</div>
	
		<!-- <div class="txt">
			<strong>앱솔루트 명작 : 1/2/3/4단계</strong>
			<p>
				내 아이를 향한 엄마의 마음으로, 매일 모유연구소에서 직접 엄마의 모유 분석을 통해 영양성분, 원재료 하나하나 맞춤형으로 엄마에 가깝게 설계한 분유의 명작.
			</p>
		</div> -->
	
	</div>
</c:if>