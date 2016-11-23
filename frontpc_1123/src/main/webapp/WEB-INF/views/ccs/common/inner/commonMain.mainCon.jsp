<%--
	화면명 : 프론트 모바일 메인 스와이프때문에 BODY 영역만 Include 파일로 분리함.
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ page import="intune.gsf.common.utils.Config"%>
<%@ page import="gcp.common.util.FoSessionUtil" %>
<%
	//상단 기획전 배너
	pageContext.setAttribute("mainBnr", Config.getString("corner.main.banner.img.6"));
	//추천MD 상품
	pageContext.setAttribute("mdPrd", Config.getString("corner.main.md.product.1"));
	//시즌추천상품 
	pageContext.setAttribute("season1", Config.getString("corner.main.season.img.1"));
	pageContext.setAttribute("season2", Config.getString("corner.main.season.product.1"));
	//추천브랜드 코너
	pageContext.setAttribute("brandText", Config.getString("corner.main.brand.text.1"));
	//추천이벤트
	pageContext.setAttribute("event1", Config.getString("corner.main.event.img.1"));
	//key이벤트
	pageContext.setAttribute("keyvent", Config.getString("corner.main.evnet.key.mb"));
	
%>

<script>

document.addEventListener("DOMContentLoaded", function(event) {
	if(global.channel.isApp=='true'){
		location.href='app://closeintro';
	}
});

</script>
<div class="main">
	<!-- ### 비주얼 ### -->
	<c:set var="mainBnrMap" value="${cornerMap[mainBnr]}" />
	<c:if test="${mainBnrMap.totalCount > 0}">
		<div class="mainVisual">
			<div class="swiper-container mainBanner_swiper_1">
				<ul class="swiper-wrapper">
					<c:forEach var="item" items="${mainBnrMap.dmsDisplayitems}" varStatus="status">
						<li  class="swiper-slide" ${status.index ==0 ? 'class="on"' : '' }>
							<a href="${item.url2}"> 
								<img src="${_IMAGE_DOMAIN_}${item.img2}" alt="" />
							</a>
						</li>
					</c:forEach>
				</ul>
				<div class="pageInner_wrap">
					<div class="pageInner">
				        <div class="swiper-pagination"></div>
				        <button type="button" onclick="ccs.layer.copyLayerToContentChild('ly_visual')">전체보기</button>
			        </div>
				</div>
			</div>
		</div>
	</c:if>					
	<!-- ### //비주얼 ### -->

	<!-- ### 전문관 ### -->
	<div class="proBox">
		<ul>
			<li class="on"><a href="javascript:ccs.link.special.birthready();" class="item1">출산준비관</a></li>
			<li><a href="javascript:ccs.link.special.milkPowder();" class="item2">분유관</a></li>
			<li><a href="javascript:ccs.link.special.subscription();" class="item3">정기배송관</a></li>
			<!-- <li>
					<a href="javascript:ccs.link.special.pickup();" class="item4">매장픽업관</a>
				</li>	 -->
			<li><a href="javascript:ccs.link.special.giftShop();" class="item4">기프트샵</a></li>
			<li><a href="javascript:ccs.link.special.premium();" class="item5">프리미엄<br> 멤버십관</a></li>
		</ul>
	</div>
	<!-- ### //전문관 ### -->

	<!-- ### MD상품 ### -->
	<c:set var="mdPrdMap" value="${cornerMap[mdPrd]}" />
		<c:if test="${not empty mdPrdMap.dmsDisplayitems}">
			<div class="MdBox">
			<div class="rolling_box">
				<div class="mTitWrap">
					<h2 class="tit_style2"><strong class="point">MD</strong> 특가</h2>
					<span class="txt_style1"><c:out value="${mdPrdMap.name}"/></span>
				</div>
			
				<div class="prodSwiper swiper-container main_prodSwiper_1">
					<ul class="swiper-wrapper">
						<c:forEach var="item" items="${mdPrdMap.dmsDisplayitems}" varStatus="status">
							<c:set var="product" value="${item.pmsProduct}" scope="request" />
							<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
								<jsp:param name="swipe" value="Y"/>
							</jsp:include>
						</c:forEach>
					</ul>
				</div>
				<!-- // mod : 2016.10.10 strong end -->
			</div>
		</div>
	</c:if>
	<!-- ### //MD상품 ### -->

	<!-- ### 브릿즈 배너 ### -->
	<c:if test="${not empty cornerMap[keyvent].dmsDisplayitems}">
		<c:set var="keyventMap" value="${cornerMap[keyvent]}" />
		<div class="bridgeBox">
			<div class="bridgeCon">
				<a href="${keyventMap.dmsDisplayitems['0'].url2}">
					<img src="${_IMAGE_DOMAIN_}${keyventMap.dmsDisplayitems['0'].img2}" alt="" />
				</a>
			</div>
		</div>
	</c:if>
	<!-- ### //브릿즈 배너 ### -->

	<!-- ### 이런상품어때? ### -->
	<div class="howAboutBox">
		<div class="rolling_box">
			<c:choose>
				<c:when test="${loginId != '' && loginId != null}">
					<!-- 로그인 후 -->
					<h2 class="tit_style2">
						<c:choose>
							<c:when test="${fn:length(loginName) > 4 }">
								<strong class="point">${fn:substring(loginName,0,3) }...님!</strong> 이런 상품 어떠세요?
							</c:when>
							<c:otherwise>
								<strong class="point">${loginName}님!</strong> 이런 상품 어떠세요?
							</c:otherwise>
						</c:choose>
					</h2>
					<!-- 로그인 후 -->
				</c:when>
				<c:otherwise>
					<!-- 로그인 전 -->
					<h2 class="tit_style2">이런 상품 어떠세요?</h2>
					<!-- 로그인 전 -->
				</c:otherwise>
			</c:choose>
			<div class="prodSwiper swiper-container main_prodSwiper_2">
				<ul  class="swiper-wrapper" name="clickProduct">
				
				</ul>
			</div>
		</div>
	</div>	
	<!-- ### 계절상품 ### -->
	<c:if test="${not empty cornerMap[season1].dmsDisplayitems and not empty cornerMap[season2].dmsDisplayitems}">
		<div class="seasonBox">
			<div class="rolling_box">
				<h2 class="tit_style2"><c:out value="${cornerMap[season1].name}"/></h2>
				<c:if test="${not empty cornerMap[season1].dmsDisplayitems}">
					<c:set var="season1Map" value="${cornerMap[season1]}" />
					<div class="collection">
						<a href="${season1Map.dmsDisplayitems['0'].url2}"> <img src="${_IMAGE_DOMAIN_}${season1Map.dmsDisplayitems['0'].img2}" alt="" />
						</a>
					</div>
				</c:if>
				<div class="prodSwiper swiper-container main_prodSwiper_3">
					<c:if test="${not empty cornerMap[season2].dmsDisplayitems}">
						<c:set var="season2Map" value="${cornerMap[season2]}" />
						<ul class="swiper-wrapper">
							<c:forEach var="item" items="${season2Map.dmsDisplayitems}" varStatus="status">
								<c:set var="product" value="${item.pmsProduct}" scope="request" />
								<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
									<jsp:param name="swipe" value="Y"/>
								</jsp:include>
							</c:forEach>
						</ul>
					</c:if>
				</div>
			</div>
		</div>
	</c:if>	
	<!-- ### //계절상품 ### -->

	<!-- ### 브랜드 슬라이드 ### -->
	<c:if test="${not empty cornerMap[brandText].dmsDisplayitems}">
		<c:set var="brandMap" value="${cornerMap[brandText]}" />
		<div class="brandBox">
			<div class="show_room swiper-container main_brandSwiper">
				<ul class="swiper-wrapper">
					<c:forEach var="item" items="${brandMap.dmsDisplayitems}" varStatus="status">
						<li class="swiper-slide">
							<a href="${item.url2}"> <img src="${_IMAGE_DOMAIN_}${item.img2}" alt="" /></a>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</c:if>	
	<!-- ### //브랜드 슬라이드 ### -->

	<!-- ### 힛트다 히트! ### -->
	<div class="hitBox">
		<h2 class="tit_hit">
			<a href="javascript:mainSwiper_body.slideTo(3, 300);" class="btn_more">더보기</a>
			<strong><i>히트다 히트!</i> <span>실시간 인기상품</span></strong>
		</h2>
		
		<div class="mainSwiper_tab">
			<div class="tab_outer swiper-container mainSwiper_category">
				<ul class="tabBox tab_outer swiper-wrapper">
					<li class="swiper-slide on">
						<a href="javascript:dms.main.makeParam('')" class="on theme1">전체</a>
					</li>
				<c:forEach var="depth1" items="${ctgList}" varStatus="status">
					<li class="swiper-slide">
						<a href="javascript:dms.main.makeParam('${depth1.displayCategoryId}');" class="category_${depth1.displayCategoryId}">
							<c:out value="${depth1.name}" />
						</a>
					</li>
				</c:forEach>
				</ul>
			</div>
		</div>
		<div class="product_type1 bestListArea">
			
		</div>
	</div>	
	<!-- ### //힛트다 히트! ### -->

	<!-- ### 배너 영역 ### -->
	<c:if test="${not empty cornerMap[event1].dmsDisplayitems}">
		<c:set var="event1Map" value="${cornerMap[event1]}" />
		<div class="swiper-container mainBanner_swiper_2">
			<ul class="swiper-wrapper">
			<c:forEach var="item" items="${event1Map.dmsDisplayitems}" varStatus="status">
				<li class="swiper-slide">
					<a href="${item.url2}"> <img src="${_IMAGE_DOMAIN_}${item.img2}" alt="" /></a>
				</li>
			</c:forEach>	
			</ul>
			<!-- Add Pagination -->
   			<div class="swiper-pagination"></div>
		</div>		
	</c:if>	
	<!-- ### //배너 영역 ### -->

	<!-- ### 알림영역 ### -->
	<div class="txt_alarm">
		<p>
			<a href="javascript:custcenter.notice.detail('${noticeList['0'].noticeNo}', '${noticeList['0'].readCnt}');"><c:out
					value="${noticeList['0'].title}" /></a>
		</p>
	</div>
	<!-- ### //알림영역 ### -->
</div>
<!-- ### 메인 이미지 레이어 ### -->
<div class="pop_wrap ly_visual" id="ly_visual" >
	<div class="pop_inner">
		<c:set var="mainBnrIm" value="${cornerMap[mainBnr]}" />
		<c:if test="${mainBnrIm.totalCount > 0}">
			<div class="pop_header type1">
				<h3>전체보기</h3>
			</div>
			<div class="pop_content">
				<ul>
					<c:forEach var="item" items="${mainBnrIm.dmsDisplayitems}" varStatus="status">
						<li>
							<a href="${item.url2}"> 
								<img src="${_IMAGE_DOMAIN_}${item.img2}" alt="" />
							</a>
						</li>
					</c:forEach>
				</ul>
			</div>
			<button type="button" class="btn_x pc_btn_close">닫기</button>
		</c:if>
	</div>
</div>
