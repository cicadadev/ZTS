<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	
<%@ page session = "true" %>

<script>

	//검색에서 사용함.
	var _BRAND_ID = '${brandInfo.brandId}';

	$(document).ready(function(e){
		//header_inner_h = $('.header_mo .inner').height(); // 헤더 높이 값
		
		// sns 공유용 메타데이터 세팅
		$("meta[property='og:image']").attr("content", "${ogTagImage}");
		$("meta[property='og:url']").attr("content", "${ogTagUrl}");
		$("meta[property='og:title']").attr("content", "${ogTagTitle}");
		
		// 이전 페이지 없을 경우, '뒤로 가기' 버튼 숨김
		var historyUrl = document.referrer;
		if (common.isEmpty(historyUrl)) {
			$("#btn_back").hide();
		} else {
			$("#btn_back").show();
		}
	});
</script>
<!-- ### mobile 전용 Header ### -->
<div class="header_mo">
	<div class="inner">
		<button type="button" class="btn_navi_prev" id="btn_back">이전 페이지로..</button>

		<h1 class="logo" style="padding-top:7px;" onclick="brand.template.main('${brandInfo.brandId}');">
			<a href="#">
				<img src="/resources/img/mobile/logo/logo_${brandInfo.brandId}.jpg" alt="${brandInfo.name}" />
			</a>
		</h1>

		<div class="etc_util" style="top:9px;">
			<button type="button" class="btn_search">검색</button>
			<button type="button" class="btn_brand">브랜드</button>
		</div>
	</div>
	
	<c:if test="${brandClassName eq 'brand1' || brandClassName eq 'brand5' || brandClassName eq 'brand6'
					|| brandClassName eq 'brand7' || brandClassName eq 'brand8'}">	<!-- templateA: brand1, brand5, brand6, brand7, brand8  -->
	 	<div class="mo_mainNavi brandNavi ${brandClassName} brandNaviTypeB">
			<ul class="gnb swiper-wrapper">
				<li class="swiper-slide home on"><a href="#none1">HOME</a></li>
				<li class="swiper-slide story"><a href="#none2">STORY</a></li>
				<li class="swiper-slide lookbook"><a href="#none3">CATALOGUE</a></li>
				<li class="swiper-slide style"><a href="#none4">STYLE</a></li>
				<li class="swiper-slide event"><a href="#none5">EVENT</a></li>
			</ul>
		</div>
	</c:if>
	
	<c:if test="${brandClassName eq 'brand2' || brandClassName eq 'brand3' || brandClassName eq 'brand4'}">	<!-- templateB: brand2, brand3, brand4 -->
		<div class="mo_mainNavi brandNavi ${brandClassName}">
			<ul class="gnb swiper-wrapper">
				<li class="swiper-slide home on"><a href="#none1">HOME</a></li>
				<li class="swiper-slide story"><a href="#none2">STORY</a></li>
				<li class="swiper-slide products"><a href="#none4">PRODUCTS</a></li>
				<li class="swiper-slide event"><a href="#none5">EVENT</a></li>		
			</ul>
		</div>	
	</c:if>
	
</div>
<!-- ### //mobile 전용 Header ### -->
<jsp:include page="/WEB-INF/views/gsf/layout/page/mo/search_header_mo.jsp" />
