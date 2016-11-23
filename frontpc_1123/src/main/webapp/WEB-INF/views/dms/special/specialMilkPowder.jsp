<%--
	화면명 : 프론트 & 모바일  출산준비관
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%
	pageContext.setAttribute("imgBanner", Config.getString("corner.special.milk1.img.1"));
	pageContext.setAttribute("prd", Config.getString("common.special.milkpowder.prd"));
	pageContext.setAttribute("info", Config.getString("corner.special.milk1.img.2"));


%>
<script type="text/javascript" src="/resources/js/common/display.ui.${_deviceType }.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	special.common.category.orderBy.pageInit();
	//special.common.category.orderBy.cornerPageInit();
	//$("#miniTabCtg_"+$('#categoryIds').val()).find("a").addClass("on");
	if(ccs.common.mobilecheck()){	
		swiperCon('mainSwiper_category', '5'); //메인 - 카테고리 탭
	}else{
		swiperCon('mainSwiper_category', 400, 6, 0, false, true); //프리미엄 멤버쉽 베너
		
	}
});
</script>
	
<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="전문관|분유관" name="pageNavi"/>
</jsp:include>

<div class="inner">
	<div class="pro_box pro_proBox2">
		<!-- 16.10.05 : 상단 비주얼 -->
		<c:set var="mainImgMap"  value="${cornerMap[imgBanner]}"/>
		<c:if test="${not empty mainImgMap.dmsDisplayitems}">
			<div class="visual one">  <!-- 16.10.22 : class(dotType) 삭제 -->
				<ul>  <!-- 16.10.22 : class(vImg) 삭제 -->
					<!-- pc -->
					<c:forEach var="item" items="${mainImgMap.dmsDisplayitems}" varStatus="status">
						<c:choose>
							<c:when test="${isMobile}">
								<li ${status.index ==0 ? 'class="on"':''}>
									<span class="bg" style="background-color:#5cbbda;">분유관</span>
									<div class="img_outer">
										<img src="${_IMAGE_DOMAIN_}${item.img2}" alt="${item.text2}" />
										<a href="javascript:special.layer.tipLayer();" class="link">분유 선택 노하우 TIP - 월령별 섭취량 - 분유타는법 - 분유교체법 - 특수분유안내</a>
									</div>
								</li>
							</c:when>
							<c:otherwise>
								<li ${status.index ==0 ? 'class="on"':''}>
									<span class="bg" style="background-color:#5cbbda;">분유관</span>
									<div class="img_outer">
										<img src="${_IMAGE_DOMAIN_}${item.img1}" alt="${item.text1}" />
										<a href="javascript:special.layer.tipLayer();" class="link">분유 선택 노하우 TIP - 월령별 섭취량 - 분유타는법 - 분유교체법 - 특수분유안내</a>
									</div>
								</li>
							</c:otherwise>
						</c:choose>
					</c:forEach>
				</ul>
			</div>
		</c:if>

		<c:choose>
			<c:when test="${isMobile}">
			
				<!-- 카테고리 //-->
				<div class="tabFixW">
					<div class="tabFix">
						<div class="tab_outer swiper-container mainSwiper_category">
							<ul class="tabBox swiper-wrapper">
								<li class="swiper-slide">
									<a href="#none" onclick="special.common.category.getSpecialCategory('all','ALL');" class="theme1">전체</a>
								</li>
								<c:forEach var="corner" items="${prdCornerList}" varStatus="status">
									<li class="swiper-slide">
										<a href="#none" onclick="special.common.category.getCornerPrd('${corner.displayId}');" class="theme20"><c:out value="${corner.name}"/> </a>	
									</li>
								</c:forEach>
								<c:if test="${not empty depth1}">
									<c:forEach var="depth1" items="${depth1}" varStatus="status">
										<li class="swiper-slide">
											<a href="#none" onclick="special.common.category.getSpecialCategory('category','${depth1.displayCategoryId}');" class="category_${depth1.displayCategoryId}"><c:out value="${depth1.name}"/></a> 
										</li>
									</c:forEach>
								</c:if>
							</ul>
						</div>
					</div>
				</div>
				<!--// 카테고리 -->
				
				<!-- 정렬 //-->
				<div class="tit_style3 mo_only" id="moSortBoxList" style="display:bolck;">
					<strong class="sort_num" id="moAllCount">
						<span>
							총 <em id="searchCount">${search.totalCount}</em>건
						</span>
					</strong>	
					<div class="sortBoxList sort_2ea" >
						<ul>
							<li>
								<div class="select_box1"  id="moSpeCtgList">
									<jsp:include page="/WEB-INF/views/dms/include/categorySelete.jsp" flush="false"/>
								</div>
							</li>
							<li>
								<div class="select_box1">
									<label></label>
									<select id="sortSelect">
										<option value="ORDER_QTY">인기상품순</option>
										<option value="RATING">상품평순</option>
										<option value="LOW_PRICE">낮은가격순</option>
										<option value="SALE_PRICE">높은가격순</option>
										<option value="DATE">최근등록순</option>
									</select>
								</div>
							</li>
							<li>
								<button type="button" class="btnListType list">블록형 / 리스트형</button>
							</li>
						</ul>
					</div>
				</div>
				<!--// 정렬 -->
				
				<!-- 코너 상품 정렬 //-->
				<div class="tit_style3 mo_only" id="cornerOrderByDiv" style="display:none;">
					<strong class="sort_num">
						<span>
							총 <em id="cornerSearchCount"></em>건
						</span>
					</strong>	
					<jsp:include page="/WEB-INF/views/dms/include/cornerSearchOrderby.jsp" flush="false"/>
				</div>
				<!-- //코너 상품 정렬 -->
				
				<!-- 브랜드 소개 코너 // -->
				<div  id="brandInfoCorner" style="display:none;"> </div>
				<!-- //브랜드 소개 코너  -->
				
				<!-- 상품 리스트  : 검색 상품 목록 //-->
				<div class="list_group" id="productList" style="display:block;">
					<jsp:include page="/WEB-INF/views/dms/include/searchProductList.jsp" flush="false">
						<jsp:param value="N" name="pagingYn"/>
					</jsp:include>
				</div>
				<!-- //상품 리스트  : 검색 상품 목록 -->
				
			</c:when>
			<c:otherwise>
			
				<!-- 카테고리 //-->
				<div class="swiper_wrap">
					<div class="tab_outer swiper-container mainSwiper_category">
						<ul class="tabBox swiper-wrapper">
							<li class="swiper-slide">
								<a href="#none" onclick="special.common.category.getPcCategoryPrd('MILK','ALL');" class="theme1">전체</a>
							</li>
							<c:forEach var="corner" items="${prdCornerList}" varStatus="status">
								<li class="swiper-slide">
									<a href="#none" onclick="special.common.category.getCornerPrd('${corner.displayId}');" class="theme20"><c:out value="${corner.name}"/> </a>
								</li>
							</c:forEach>
							<c:if test="${not empty depth1}">
								<c:forEach var="depth1" items="${depth1}" varStatus="status">
									<li class="swiper-slide">
										<a href="#none" onclick="special.common.category.getPcCategoryPrd('MILK','${depth1.displayCategoryId}');" class="category_${depth1.displayCategoryId}"><c:out value="${depth1.name}"/></a>
									</li>
								</c:forEach>
							</c:if>
						</ul>
					    <div class="swiper-button-next btn_tp6"></div>
					    <div class="swiper-button-prev btn_tp6"></div>
					</div>
				</div>
				<!-- //카테고리 -->
				
				<!-- 전체 상품 정렬 //-->
				<c:if test="${categorySearch.upperDisplayCategoryId eq 'ALL' }">
					<div class="tit_style3 pc_only" id="allOrderByDiv" style="display:block;">
						<strong class="sort_num">
							<span>
								총 <em id="searchCount">${search.totalCount}</em>건
							</span>
						</strong>	
						<jsp:include page="/WEB-INF/views/dms/include/searchOrderby.jsp" flush="false"/>
					</div>
				</c:if>
				<!-- //전체 상품 정렬 -->
				
				<!-- 코너 상품 정렬 //-->
				<div class="tit_style3 pc_only" id="cornerOrderByDiv" style="display:none;">
					<strong class="sort_num">
						<span>
							총 <em id="cornerSearchCount"></em>건
						</span>
					</strong>	
					<jsp:include page="/WEB-INF/views/dms/include/cornerSearchOrderby.jsp" flush="false"/>
				</div>
				<!-- //코너 상품 정렬 -->
				
				<!-- 상품 리스트  : 검색 상품 목록 //-->
				<div class="list_group" id="productList" style="display:block;">  
					<jsp:include page="/WEB-INF/views/dms/special/inner/spcSearchProductList.jsp" flush="false"/>
				</div>
				<!-- //상품 리스트  : 검색 상품 목록-->
				
			</c:otherwise>
		</c:choose>
		
		<!-- 상품 리스트 : 코너 상품 목록-->
		<div class="list_group" id="cornerProduct" style="display:none;">
			<jsp:include page="/WEB-INF/views/dms/include/cornerProductList.jsp" flush="false"/>
		</div>
		<!-- //상품 리스트 : 코너 상품 목록 -->
	</div>
</div>			
			
