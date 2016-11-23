<%--
	화면명 : 샵온
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script type="text/javascript" src="/resources/js/mms/mms.mypage.js"></script>

<script type="text/javascript">
$(document).ready(function() {
	
	special.pickup.getAllPickupProductList('all');
	
/* 	$(".mobile .btnListType").off("click").on("click", function(){
		$(this).toggleClass("block");
		$(".product_type1").toggleClass("block");
	}); */
	
	//16.10.18 샵온 매장 스와이프
/* 	if($('.mobile .storeOn').length>0){
		$('.storeOn').owlCarousel({
			items:1,
			loop:true,
			dots:false,
			margin:0
		})
	} */

	//16.10.18 owl-carousel 사용시 화면 스와이프 막기
/* 	if($('.owl-carousel').length>0){
		$('.owl-carousel').on("touchstart mousedown", function(e) {
			// Prevent carousel swipe
			e.stopPropagation();
		})
	} */

	
});

</script>
	
		
<jsp:include page="/WEB-INF/views/gsf/layout/page/mo/title_menu.jsp" flush="false" >
	<jsp:param name="titleMenu" value="7" />
</jsp:include>
	

<!-- mod : 20161017 begin -->
<div class="mainCon">
	<div class="swiper-wrapper">
		<div class="inner main3">
			<div class="main_shop">
				<!-- ### 16.10.18 : 샵 정보 ### -->
				<div class="storeOn">
					<c:forEach items="${interestOffshopList}" var="iterestOffshop" varStatus="i">
						<div class="item">
							<div class="storeAddr">
								<div class="storeInner">
									<div class="branch">
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
						</div>
					</c:forEach>
				</div>
				
				<!-- ### //16.10.18 : 샵 정보 ### -->


				<!-- ### 배너 영역 ### -->
				<div class="banner_zone">
					<!-- 16.10.18 -->
					<div class="banner_list_owl">
						<c:forEach var="exhibit" items="${exhibitList}" varStatus="i">
							<div class="item">
								<a href="/dms/exhibit/detail?exhibitId=${exhibit.exhibitId}">
									<img src="${_IMAGE_DOMAIN_}${exhibit.img2}" alt="1" />
								</a>
							</div>
						</c:forEach>
						
						<div class="item">
							<a href="#none">
								<img src="img/mobile/temp/banner3.jpg" alt="1" />
							</a>
						</div>
					</div>
					<!-- //16.10.18 -->
				<!-- ### //배너 영역 ### -->
					
					<script>
					
					$('.banner_list_owl').owlCarousel({
						items:1,
						loop:true,
						dots:true,
						margin:0
					})
					
					
					</script>

	
				<!-- ### 매장 픽업 BEST 상품 ### -->
				<div class="best_box">
					<h3 class="tit">매장 픽업 BEST 상품</h3>

					<!-- 16.10.18 -->
					<div class="tab_outer swipeMenu">
						<ul class="tabBox">
							<li class="first on" id="all">
								<a href="#none" class="theme1" onclick="javascript:special.pickup.getAllPickupProductList('all');">전체</a>
							</li>
							<c:forEach var="brand" items="${brandList}" varStatus="i">
								<li id="${brand.brandId}">
									<a href="#none" class="theme_${brand.brandId}" onclick="javascript:special.pickup.getPickupPrdList('${brand.brandId}');">${brand.name}</a>
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
						<a href="#none" class="evt_tit">매장별 상품 검색</a>
					</h3>

					<div class="srch_box">
						<div class="column_2set">
							<div class="select_box1">
								<label></label>
								<select onchange="special.pickup.changeSido(this);" id="offshopArea1">
									<option value="">시/도</option>
									<c:forEach var="area1" items="${area1List}">
										<option value="${area1.areaDiv1}">${area1.areaDiv1}</option>
									</c:forEach>
								</select>
							</div>

							<div class="select_box1">
								<label></label>
								<select id="offshopArea2">
									<option value="">시/군/구</option>
								</select>
							</div>
						</div>

						<div class="select_box1">
							<label id="offshopType_label"></label>
							<tags:codeList code="OFFSHOP_TYPE_CD" var="offshopTypeCd" tagYn="N"/>
							<select id="select_offshopType">
								<option value="">매장유형</option>
								<c:forEach items="${offshopTypeCd}" var="offshopType">
									<option value="${offshopType.cd}">${offshopType.name}</option>
								</c:forEach>
							</select>
						</div>

						<div class="srch_form">
							<div class="inputTxt_place1" >
								<label>검색어를 입력하세요</label>
								<span>
									<input type="text" value="" name="searchKeyword" id="searchKeyword"/>
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
									<label>전체</label>
									<select onchange="display.shopOn.searchPickupPrdList();" id="select_cate">
										<option value="">전체</option>
										<c:forEach var="category" items="${categoryList}" varStatus="i">
											<option value="${category.displayCategoryId}">${category.categoryName}</option>
										</c:forEach>
									</select>
								</div>
							</li>
							<li>
								<div class="select_box1">
									<label></label>
									<tags:codeList code="PRODUCT_SORT_CD" var="productSort" tagYn="N"/>
									<select onchange="display.shopOn.searchPickupPrdList();" id="select_sort">
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
								<button type="button" class="btnListType block">블록형 / 리스트형</button>
							</li>
						</ul>
					</div>

					<div id="categoryPrdList_div">
				
				
					</div>
				</div>
				<!-- ### ### -->
			</div>
		</div>
	</div>
</div>
<!-- // mod : 20161017 end -->

