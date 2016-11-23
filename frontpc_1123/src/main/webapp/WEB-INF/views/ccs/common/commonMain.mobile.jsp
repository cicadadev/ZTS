<%--
	화면명 : 프론트 모바일 메인
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ page import="intune.gsf.common.utils.Config"%>
<%@ page import="intune.gsf.common.utils.CookieUtil"%>
<%@page import="intune.gsf.common.utils.CommonUtil"%>

<script type="text/javascript" src="/resources/js/ccs/ccs.mo.main.js"></script>
<script type="text/javascript" src="/resources/js/common/display.ui.mo.js"></script>


<%
	//상단 기획전 배너
	pageContext.setAttribute("mainBnr", Config.getString("corner.main.banner.img.6"));

	//관심정보 팝업 레이어(App 이용안내 마지막 페이지에서 연결)
	pageContext.setAttribute("linkYn", CookieUtil.getCookieValue(request, "linkYn") );
	
	//pageContext.setAttribute("RB_PCID", CookieUtil.getCookieValue(request, "RB_PCID"));
	
%>

<div class="mainCon">
	<div class="swiper-wrapper" id="mainSwiperWrapper">
		<%-- 홈 메인 --%>
		<div class="swiper-slide inner main1" data-hash="0">
			<jsp:include page="/WEB-INF/views/ccs/common/inner/commonMain.mainCon.jsp"/>
		</div>
		<%-- 쇼킹제로 메인 --%>
		<div class="swiper-slide inner main2" id="shockingzeroMainDiv" data-hash="1"></div>
		
		<%-- 베스트 메인 --%>
		<div class="swiper-slide inner main3" data-hash="2">
			<jsp:include page="/WEB-INF/views/ccs/common/inner/displayBestShop.mainCon.jsp"/>
		</div>		
		
		<%-- 기획전 메인 --%>
		<div class="swiper-slide inner main4" id="exhibitionMainDiv" data-hash="3"></div>
		
		<%-- 이벤트/혜택 메인 --%>
		<div class="swiper-slide inner main5" id="eventMainDiv" data-hash="4"></div>
		
		<%-- 샵온 메인 --%>
		<div class="swiper-slide inner main6" id="shopOnMainDiv" data-hash="5"></div>	
		
		<%-- 스타일 메인 --%>
		<div class="swiper-slide inner main7" name="styleMainDiv" data-hash="6"></div>

	</div>
</div>

<div>
	<input type="hidden" name="cookieLinkYn" id="cookieLinkYn" value="${linkYn}" />
</div>

<!-- ### 16.10.23  mobile 팝업 - 선물하기 이용방법 ### -->
<div class="layer_style1 ly_pickupHow">
	<div class="box">
		<div class="conArt">
			<strong class="title">매장픽업 이용방법</strong>
			<div class="stepImg"><img src="/resources/img/mobile/txt/step_pickup.png" alt="선물하기 이용방법" /></div> <!-- 16.11.14 -->
		</div>
		<button type="button" class="btn_x btn_close">닫기</button>
	</div>
</div>
<!-- ### //16.10.23  mobile 팝업 - 스타일샵 안내 ### -->

<!-- ### 16.10.23  mobile 팝업 - 스타일샵 안내 ### -->
<div class="layer_style1 ly_styleHow">
	<div class="box">
		<div class="conArt">
			<strong class="title">스타일샵 이용방법</strong>
			<div class="stepImg"><img src="/resources/img/mobile/txt/styleshop_step.jpg" alt="스타일샵 이용방법" /></div>
			<ul class="notice">
				<li>마음에 드는 회원의 코디에 좋아요를 눌러서 공감해주세요.</li>
				<li>완료된 스타일은 친구, 지인에게 SNS로 공유하여 추천해보아요.</li>
			</ul>
		</div>
		<button type="button" class="btn_x btn_close">닫기</button>
	</div>
</div>
<script>

mo.main.naviSwiper();

</script>