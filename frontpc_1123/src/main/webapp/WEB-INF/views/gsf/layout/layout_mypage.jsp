<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!DOCTYPE html>
<html lang="ko">
	<tiles:insertAttribute name="commonHead"/>	<!-- head 태그 -->
	<tiles:insertAttribute name="commonCss"/> <!-- 공통 CSS 선언 -->
		
	<jsp:include page="/WEB-INF/views/gsf/layout/common/commonVariable.jsp" />	
	<jsp:include page="/WEB-INF/views/gsf/layout/common/commonScript.jsp" />
	
	
	<!-- my page전용 스크립트 -->
	<script type="text/javascript" src="/resources/js/mms/mms.mypage.js"></script>	
	<script type="text/javascript" src="/resources/js/mms/mms.order.js"></script>	
	<script type="text/javascript" src="/resources/js/common/mypage.ui.js"></script>	
	<body>
	
	<c:choose>
	<c:when test="${isMobile}">
		<div class="wrap mobile">
			<div class="header_mo">
				<tiles:insertAttribute name="header_mo" />
			</div>
			
			<tiles:insertAttribute name="left_mo" />
			<tiles:insertAttribute name="right_mo" />
						
			<div class="content">
				<tiles:insertAttribute name="body" />
			</div>
			
			<tiles:insertAttribute name="bottom_mo"/>
			<tiles:insertAttribute name="footer_mo" />
		</div>
	</c:when>
	<c:otherwise>
		<div class="wrap pc">
			<div class="header_pc">
				<tiles:insertAttribute name="header_pc"/>		
			</div>
			
			<div class="content">
				<!-- pc 동그란 배너 주석 -->
				<tiles:insertAttribute name="adBanner_pc"/>
					
				<tiles:insertAttribute name="body"/>
			</div>
			
			<tiles:insertAttribute name="sky_pc"/>
			<tiles:insertAttribute name="footer_pc"/>
			
			<!-- 공통 레이어 -->
			<tiles:insertAttribute name="commonLayer_pc"/>
		</div>


	</c:otherwise>		
	</c:choose>
	</body>
	<%--SSO 체크 --%>
	<jsp:include page="/WEB-INF/views/mms/sso/sso.jsp" />
</html>