<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	
<%@ page session = "true" %>

<!-- 페이스북 -->
<%-- <meta property="og:title" content="${styleDetail.title}" />  --%>
<%-- <meta property="og:image" content="${_IMAGE_DOMAIN_}${styleDetail.styleImg}" />  --%>
<%-- <meta property="og:url" content="${_FRONT_DOMAIN_URL_}/dms/display/styleDetail?styleNo=${styleDetail.styleNo}&memberNo=${styleDetail.memberNo}" /> --%>
<!-- <meta property="og:description" content="" /> -->
<script>
/* 	$(document).ready(function(e){
		header_inner_h = $('.header_mo .inner').height(); // 헤더 높이 값
		
	}) */
	
	//검색에서 사용함.
	var _BRAND_ID = '';
</script>
<!-- ### mobile 전용 Header ### -->
<div class="header_mo">
	<div class="inner">
		<button type="button" class="btn_all">전체메뉴</button>
		
		<h1 class="logo">
			<a href="javaScript:common.pageMove('main','','')" >
				<img src="/resources/img/mobile/logo/logo.gif" alt="0to7.com" />
			</a>
		</h1>

		<div class="etc_util">
			<button type="button" class="btn_search">검색</button>
			<button type="button" class="btn_brand">브랜드</button>
			<!-- ### 수량이 없을 시 class : cart_empty ### 
			<button type="button" class="btn_cart">
				장바구니 상품 <em>3</em>
			</button>
			-->
		</div>
	</div>
	<c:if test="${mainStr eq 'main' }">
		<div class="mo_mainNavi">
			<ul class="gnb swiper-wrapper">
				<li class="swiper-slide home on"><a href="#none1">홈</a></li>
				<li class="swiper-slide shockingZero" ><a href="#none2">쇼킹제로</a></li>
				<li class="swiper-slide linkingBest" ><a href="#none3">랭킹베스트</a></li>
				<li class="swiper-slide exhibition" ><a href="#none4">기획전</a></li>
				<li class="swiper-slide event" ><a href="#none5">이벤트&amp;혜택</a></li>
				<li class="swiper-slide shopOn"><a href="#none6">샵ON</a></li>
				<li class="swiper-slide style" ><a href="#none7">스타일</a></li>
			</ul>		
		</div>
	</c:if>
</div>
<!-- ### //mobile 전용 Header ### -->

<jsp:include page="/WEB-INF/views/gsf/layout/page/mo/search_header_mo.jsp" />