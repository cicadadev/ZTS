<%--
	화면명 : 샵온
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>


<script>
$(document).ready(function() {
});


function blockNListView() {
	//16.10.23 PICKUP 이용방법
	$(".mobile .btn_pickupHow").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .ly_pickupHow") );
		}
	});
	$(".evt_tit").off("click").on("click", function(e){
		$(this).parent().toggleClass("slideHide");
	});
	
	btnListTypeEvt();
	
	$(".tabBox a, .tabBox1 a, .tabBox2 a").off("click").on("click", function(e){
		var idx = $(this).parent().index();
		var parent_ul;

		if( $(this).closest("ul").hasClass("tabBox") ){
			parent_ul = $(this).closest(".tabBox");
		}else if( $(this).closest("ul").hasClass("tabBox1") ){
		
			parent_ul = $(this).closest(".tabBox1");
		}else{
			parent_ul = $(this).closest(".tabBox2");
		}

		$(this).parent().addClass("on").siblings("li").removeClass("on");
		$(parent_ul).siblings(".tab_con").eq(idx).addClass("tab_conOn").siblings(".tab_con").removeClass("tab_conOn");

		// swiperCon('brandMenuSwiper_sortingWord', 'auto'); //left 메뉴 - 브랜드명 sorting
	});
}


</script>

<!-- mod : 20161017 begin -->
<div class="main_shop">
	<div class="storeOn swiper-container shopOnSwiper_shopList">
		<div class="swiper-wrapper">
			<c:forEach items="${interestOffshopList}" var="iterestOffshop" varStatus="i">
				<li class="swiper-slide">
					<div class="storeAddr">
						<div class="storeInner">
							<div class="branch">
								<span class="brand">allo&amp;lugh</span>
								<strong>${iterestOffshop.ccsOffshop.name}</strong>
								<c:if test="${iterestOffshop.topYn eq 'Y'}">
									<span class="default icon_type2 iconPink3">대표매장</span>
								</c:if>
								<p>
									<a href="tel:${iterestOffshop.ccsOffshop.managerPhone}" class="btn_shop_tel">${iterestOffshop.ccsOffshop.managerPhone}</a>
									<span>${iterestOffshop.ccsOffshop.address1}</span>
								</p>
							</div>
	
							<div class="storeBrand">
								<span class="icon_type1 iconPurple2">판매브랜드</span>
								<ul>
									<c:forEach var="brand" items="${iterestOffshop.ccsOffshop.ccsOffshopbrands}" varStatus="j">
										<li>${brand.name}</li>
									</c:forEach>
								</ul>
							</div>
	
							<div class="box_01">
								<c:if test="${iterestOffshop.ccsOffshop.offshopPickupYn eq 'Y'}">
									<a href="javascript:display.shopOn.getPickupProductList('${iterestOffshop.offshopId}', '${iterestOffshop.ccsOffshop.name}')" class="btn_sStyle1 sPurple1 btn_pick">픽업가능 상품보기</a>
								</c:if>
							</div>
	
							<div class="box_02">
								<a href="#none" class="btn_storeMap1 btn_shopPosition" onclick="mypage.offshop.offshopInfo('${iterestOffshop.offshopId}')">매장위치</a>
								<button type="button" class="bookmark checked" id="interestBtn${i.index}" onclick="ccs.offshop.saveInterestOffshop(this, '${iterestOffshop.offshopId}');">관심매장</button>
							</div>
						</div>
					</div>
				</li>
			</c:forEach>
		</div>
	</div>
	<!-- ### //16.10.18 : 샵 정보 ### -->


	<!-- ### 배너 영역 ### -->
	
	<div class="banner_zone">
		<div class="swiper-container type1 mainShopOn_banner_1">
			<ul class="swiper-wrapper">
				<c:forEach var="exhibit" items="${shopOnExhibitList}" varStatus="status">
					<li class="swiper-slide">
						<a href="/dms/exhibit/detail?exhibitId=${exhibit.exhibitId}">
							<img src="${_IMAGE_DOMAIN_}${exhibit.img2}" alt="1" />
						</a>
					</li>
				</c:forEach>
			</ul>

			<!-- Add Pagination -->
   				<div class="swiper-pagination tp2"></div>
		</div>
	</div>
	
	
	
	<!-- ### //배너 영역 ### -->

	<!-- ### 매장 픽업 BEST 상품 ### -->
	<div class="best_box">
		<h3 class="tit">매장 픽업 BEST 상품<a href="#none" class="btn_infor btn_pickupHow">?</a></h3>

		<!-- 16.10.18 -->
		<div class="tab_outer swiper-container shopOnSwiper_bestProd">
			<ul class="tabBox swiper-wrapper">
				<li class="first swiper-slide on"  id="all">
					<a href="#none" class="theme1" onclick="javascript:display.shopOn.getAllPickupProductList('all');">전체</a>
				</li>
				<c:forEach var="brand" items="${shopOnbrandList}" varStatus="i">
					<li class="swiper-slide" id="${brand.brandId}">
						<a href="#none" class="theme_${brand.brandId}" onclick="javascript:display.shopOn.getPickupPrdList('${brand.brandId}');">${brand.name}</a>
					</li>
				</c:forEach>
			</ul>
		</div>
		<!-- //16.10.18 -->

	</div>
	<!-- ### //매장 픽업 BEST 상품 ### -->

	<!-- ### 매장별 상품 검색 ### -->
	<div class="shop_search" id="shop_search">
		<h3 class="tit">
			<a href="#none" class="evt_tit">매장별 픽업상품 검색</a>
		</h3>

		<div class="srch_box">
			<div class="select_box1">
				<label id="brand_label">브랜드</label>
				<tags:codeList code="BRAND_CD" var="brandCd" tagYn="N"/>
				
				<select id="shopon_select_brand">
					<option value="">브랜드</option>
					<c:forEach var="brand" items="${shopOnbrandList}" varStatus="i">
						<option value="${brand.brandId}">${brand.name}</option>
					</c:forEach>
				</select>
			</div>
			<div class="column_2set">
				<div class="select_box1">
					<label>시/도</label>
					<select onchange="special.pickup.changeSido(this);" id="offshopArea1">
						<option value="">시/도</option>
						<c:forEach var="area1" items="${area1List}">
							<option value="${area1.areaDiv1}">${area1.areaDiv1}</option>
						</c:forEach>
					</select>
				</div>

				<div class="select_box1">
					<label>시/군/구</label>
					<select id="offshopArea2">
						<option value="">시/군/구</option>
					</select>
				</div>
			</div>


			<div class="srch_form">
				<div class="inputTxt_place1" >
					<label>매장명을 입력하세요</label>
					<span>
						<input type="text" value="" name="searchKeyword" id="shopon_searchKeyword"/>
					</span>
				</div>
				<a href="#none" onclick="javascript:display.shopOn.searchPickupPrdList();" class="btn_srch">검색</a>
			</div>
		</div>
	</div>
	<!-- ### //매장별 상품 검색 ### -->
	<c:if test="${not empty interestOffshopList}">
		<div class="like_shop_ck">
			<label class="chk_style1">
				<em>
					<input type="checkbox" value="" name="interestOffshopCheckbox" onclick="javascript:display.shopOn.searchPickupPrdList();"/>
				</em>
				<span>관심매장 상품만 보기</span>
			</label>
		</div>
	</c:if>

	<!-- ### 검색 결과 상품 목록 ### -->
	<div class="result_list">
		<div class="sortBoxList sort_2ea">
			<ul>
				<li>
					<div class="select_box1">
						<label>카테고리</label>
						<select onchange="display.shopOn.searchPickupPrdList();" id="shopon_select_cate">
							<option value="">카테고리</option>
							<c:forEach var="category" items="${brandCategoryList}" varStatus="i">
								<option value="${category.displayCategoryId}">${category.categoryName}</option>
							</c:forEach>
						</select>
					</div>
				</li>
				<li>
					<div class="select_box1">
						<label>정렬</label>
						<tags:codeList code="PRODUCT_SORT_CD" var="productSort" tagYn="N"/>
						<select onchange="display.shopOn.searchPickupPrdList();" id="shopon_select_sort">
							<option value="">정렬</option>
							<c:forEach items="${productSort}" var="sort">
								<c:if test="${sort.cd eq search.sort}">
									<option value="${sort.cd}" selected="selected">${sort.name}</option>
								</c:if>
								<c:if test="${sort.cd ne search.sort}">
									<option value="${sort.cd}">${sort.name}</option>
								</c:if>
							</c:forEach>
						</select>
					</div>
				</li>
				<li>
					<button type="button" class="btnListType list">블록형 / 리스트형</button>
				</li>
			</ul>
		</div>

		<div id="categoryPrdList_div">
	
	
		</div>
	</div>
	<!-- ### ### -->
</div>

<!-- // mod : 20161017 end -->

