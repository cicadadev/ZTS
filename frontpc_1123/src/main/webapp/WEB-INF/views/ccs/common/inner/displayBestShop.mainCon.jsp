<%--
	화면명 : 베스트매장
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
$(document).ready(function() {
	swiperCon('linkBestSwiper_category', 'auto'); //베스트링크 - 실시간 클릭 BEST
});
</script>

<div class="main_best">
	<h2 class="tit_best">
		<img src="/resources/img/pc/txt/best.gif" alt="" />
		<span id="infoTxt">* 고객님들이 많이 클릭한 상품순입니다.</span>

		<div class="sortBoxList sort_2ea">
			<ul>
				<li>
					<div class="select_box1">
						<label>월령</label>
						<select id="ageSelectbox" onchange="display.bestShop.changeAgeOrGender();">
							<option>전체</option>
						</select>
					</div>
				</li>
				<li>
					<div class="select_box1">
						<label>성별</label>
						<select id="genderSelectbox" onchange="display.bestShop.changeAgeOrGender();">
							<option value="">전체</option>
							<option value="B">여아</option>
							<option value="A">남아</option>
						</select>
					</div>
				</li>
				<c:if test="${isMobile}">
					<li class="mo_only">
						<button type="button" class="btnListType block">블록형 / 리스트형</button>
					</li>
				</c:if>
				
			</ul>
		</div>
	</h2>

	<div class="best_tab">
		<ul class="best_type">
			<li class="on">
				<a href="#none" class="real" onclick="display.bestShop.makeParam('click', 'Y');">클릭 BEST</a>
				<div class="tab_outer swiper-container mainSwiper_category">
					<ul class="tabBox tab_outer swiper-wrapper" id="categoryArea" name="categoryTab">
						<li class="swiper-slide on" id="default1">
							<a href="#none" class="theme1">전체</a>
						</li>
						<c:forEach items="${ctgList}" var="category1" varStatus="category1_status">
							<c:if test="${category1_status.index < 4}"> <!-- 임시 -->
								<li class="swiper-slide">
									<a href="#none" class="category_${category1.displayCategoryId}">${category1.name}</a>
									<input type="hidden" name="hid_category1" id="hid_category1" value="${category1.displayCategoryId}" />
								</li>
							</c:if>
						</c:forEach>
					</ul>
				</div>
			</li>
			<li>
				<a href="#none" class="week" onclick="display.bestShop.makeParam('order', 'Y');">판매 BEST</a>
				<div class="tab_outer swiper-container mainSwiper_category">
					<ul class="tabBox tab_outer swiper-wrapper" id="categoryArea2" name="categoryTab" style="display:none;">
						<li class="swiper-slide on" id="default2">
							<a href="#none" class="theme1">전체</a>
						</li>
						<c:forEach items="${ctgList}" var="category1" varStatus="category1_status2">
							<c:if test="${category1_status2.index < 4}"> <!-- 임시 -->
								<li class="swiper-slide">
									<a href="#none" class="category_${category1.displayCategoryId}">${category1.name}</a>
									<input type="hidden" name="hid_category1" id="hid_category1" value="${category1.displayCategoryId}" />
								</li>
							</c:if>
						</c:forEach>
					</ul>
				</div>
			</li>
		</ul>
	</div>
	
	<div class="swiper-container linkBestSwiper_category" id="category2_Area">
		<ul class="sortBox swiper-wrapper" id="default3" style="height:2px;">		<!-- '전체' 대카에 속하는 중카 영역 -->
			<li class="swiper-slid active">
				<a href="#none">
					<span></span>
				</a>
			</li>
		</ul>
		<c:forEach items="${ctgList}" var="category1" varStatus="category1_status3">
			<ul class="sortBox swiper-wrapper" style="display:none;">		<!-- '전체' 아닌 대카에 속하는 중카 영역 -->
				<li class="swiper-slid active" onclick="display.bestShop.setSearchParam('all');">
					<a href="#none">전체</a>
				</li>
				<c:forEach items="${category1.dmsDisplaycategorys}" var="category2" varStatus="category2_status">
					<li class="swiper-slide" onclick="display.bestShop.setSearchParam('${category2.displayCategoryId}');">
						<a href="#none" >${category2.name}</a>
						<input type="hidden" name="hid_category2" id="hid_category2" value="${category2.displayCategoryId}" />
					</li>
				</c:forEach>
			</ul>
		</c:forEach>
	</div>
	

	<div class="list_group" style="border-top:none;">
		<div class="product_type1 prodType_4ea block ${isMobile eq 'true' ? 'bestListArea' : ''}" ${isMobile ne 'true' ? 'id=\"bestListArea\"' : ''}>
<%-- 			<jsp:include page="/WEB-INF/views/dms/display/inner/bestList.jsp" /> --%>
		</div>
	</div>
</div>
