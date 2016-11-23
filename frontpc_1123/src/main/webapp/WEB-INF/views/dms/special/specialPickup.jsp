<%--
	화면명 : 매장픽업관
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script type="text/javascript" src="/resources/js/common/display.ui.pc.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	
	var offshopId = '${search.offshopId}';
	var name = '${search.name}';
	
	if (offshopId) {
		$("#search_div").css("display", "block");
		$("#searchKeyword").val(name);
		ccs.common.fnPlaceholder();
	}
	
	special.pickup.getAllPickupProductList('all', offshopId, name);
});
	function chgSelect(param) {
		
	}
	
	function changeGubunTitle(param) {
		$(".miniTabBox1").each(function() {
			$(this).find("a").removeClass("on");
		});
		$(param).addClass("on");
	}
</script>

	<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
		<jsp:param value="전문관|매장픽업관" name="pageNavi"/>
	</jsp:include>
	<div class="inner">
		<div class="special st_pickup">
			<!-- 16.10.05 : 상단 비주얼 -->
			<div class="visual one">
				<ul class="">
					<c:forEach items="${keyVisualBanner}" var="banner" varStatus="i">
						<c:forEach items="${banner.dmsDisplayitems}" var="item" varStatus="j">
							<c:choose>
								<c:when test="${isMobile}">
									<li class="on">
										<span class="bg" style="background-color:#55c87e;">매장픽업관</span>
										<div class="img_outer">
											${item.html2}
										</div>
									</li>
								</c:when>
								<c:otherwise>
									<li class="on">
										<span class="bg" style="background-color:#55c87e;">매장픽업관</span>
										<div class="img_outer">
											${item.html1}
										</div>
									</li>
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</c:forEach>
				</ul>
			</div>
			<!-- //16.10.05 : 상단 비주얼 -->
	
			<div class="step pickupHow">
				<h3 class="slide_tit1 slideHide"><span class="evt_tit">매장픽업 어떻게 이용하나요?</span></h3>
				<div class="stepCont">
					<ul class="notice">
						<li>픽업 당일 상품을 찾아가지 않으실 경우 픽업예정일 다음날  자동취소 됩니다.</li>
						<li>상품 픽업 전에는 온라인에서 주문취소가 가능합니다.</li>
					</ul>
	
					<ul class="stepTxt">
						<li>
							<span class="step01">Step 01</span>
							<strong>상품구매</strong>
							<em>방문날짜를 선택 후 원하는<br /> 상품을 픽업 신청합니다.</em>
						</li>
						<li>
							<span class="step02">Step 02</span>
							<strong>SMS받기</strong>
							<em>SMS로 픽업안내 메시지를 받습니다.</em>
						</li>
						<li>
							<span class="step03">Step 03</span>
							<strong>매장방문</strong>
							<em>예약하신 날짜에 매장을 방문합니다.</em>
						</li>
						<li>
							<span class="step04">Step 04</span>
							<strong>상품픽업</strong>
							<em>매장에서 쿠폰을 제시하고<br />상품을 구매하세요.</em>
						</li>
					</ul>
				</div>
			</div>
			<c:if test="${isMobile}">
				<div class="tabFixW">
					<div class="tabFix">
			</c:if>
					
					<div class="swiper_wrap">
						<div class="tab_outer swipeMenu swiper-container storePicupSwiper_categoryList">
							<ul class="tabBox swiper-wrapper">
								<li class="swiper-slide first on" id="all">
									<a href="#none" class="theme1" onclick="javascript:special.pickup.getAllPickupProductList('all');">전체</a>
								</li>
								<c:forEach var="brand" items="${brandList}" varStatus="i">
									<li id="${brand.brandId}">
										<a href="#none" class="theme_${brand.brandId}" onclick="javascript:special.pickup.getPickupPrdList('${brand.brandId}');">${brand.name}</a>
									</li>
								</c:forEach>
							</ul>
							<!-- Add Arrows -->
							<c:if test="${fn:length(brandList) > 6}">
							    <div class="swiper-button-next btn_tp6"></div>
							    <div class="swiper-button-prev btn_tp6"></div>
							</c:if>
						</div>
					</div>
					<div class="btnSrchDetail">
						<a href="#none">매장별 픽업상품 검색<span>열기</span></a>
					</div>
			
					<!-- pc 상세 옵션 -->
					<div class="srch_pickup detailOptBox" id="search_div"> <!-- 16.10.05 : class(detailOptBox) 추가 -->
						<div class="srchTit">
							<strong>내가 원하는 상품만 딱! 매장별 픽업상품 검색</strong>
						</div>
						<div class="srchCont">
							<div class="select_box1">
								<label id="brand_label"></label>
								<tags:codeList code="BRAND_CD" var="brandCd" tagYn="N"/>
								<select id="select_brand">
									<option value="">브랜드</option>
									<c:forEach var="brand" items="${brandList}" varStatus="i">
										<option value="${brand.brandId}">${brand.name}</option>
									</c:forEach>
								</select>
							</div>
							<div class="select_box1">
								<label></label>
								<select onchange="special.pickup.changeSido(this);" id="offshopArea1">
									<option>시/도</option>
								</select>
							</div>
		
							<div class="select_box1">
								<label></label>
								<select id="offshopArea2">
									<option>시/군/구</option>
								</select>
							</div>
		
			
							<div class="inputTxt_place1">
								<label>매장명을 입력하세요 ex)홈플러스 OO점</label>
								<span>
									<input type="text" value="" name="searchKeyword" id="searchKeyword"/>
								</span>
							</div>
							<a href="#none" onclick="javascript:special.pickup.searchOffshop();" class="btn_sStyle4 sPurple1">검색</a>
						</div>
						<a href="#none" class="btnDetailClose">닫기</a>
					</div>
					
				<c:if test="${isMobile}">
					</div>
				</div>
				</c:if>	
	
			<div id="categoryPrdList_div">
				

			</div>
		</div>
	</div>
