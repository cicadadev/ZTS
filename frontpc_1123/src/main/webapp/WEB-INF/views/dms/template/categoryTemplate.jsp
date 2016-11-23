<%--
	화면명 : 프론트 & 모바일  대,중 카테고리매장 템플릿
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ page import="gcp.common.util.FoSessionUtil" %>
<%

	boolean isMobile = FoSessionUtil.isMobile(request);

	if(isMobile){
		pageContext.setAttribute("mainBnr", Config.getString("corner.ctg.2depth.exhibit.1"));
	}else{
		pageContext.setAttribute("mainBnr", Config.getString("corner.ctg.2depth.img.1"));
	}
	

	pageContext.setAttribute("brandBnr", Config.getString("corner.ctg.2depth.text.1") );
	pageContext.setAttribute("mdPrd", Config.getString("corner.ctg.2depth.product.1") );
%>

<script type="text/javascript" src="/resources/js/common/display.ui.${_deviceType }.js"></script>
<!-- 코너번호 -->
<script type="text/javascript">

$(document).ready(function(){
	dms.search.orderBy.pageInit();
	var depthCategory = '${depthCategory}';
	if(depthCategory != null && depthCategory != ''){
		//pms.common.getRecommendationProductList('timeProduct',  {recType:'c001', size : 15, cids:'${depthCategory}'});
	}
	
	if(ccs.common.mobilecheck()){		
		dms.common.searchMobilePage();
		swiperCon('displaySwiper_prodList_1', '3'); //중카테고리 - MD 추천
		swiperCon('displayCategorySwiper_banner_1', '1', 0, 'fraction'); //배너
	}else{
		swiperCon('displayCategorySwiper_banner_1', 400, 1, 0, false, true); //중 카테고리 - 상단 큰 배너
		swiperCon('displaySwiper_prodList_1', 400, 4, 12, false, true, 4); //메인 - MD 특가
	}
		// 중카테고리
		swiperCon('displayCategorySwiper_cetegory', '1', 0, 'fraction'); //카테고리
		//swiperCon('displayCategorySwiper_banner_1', '1', 0, 'fraction'); //배너
		
		
});
</script>

<!-- 네비게이션 -->
<c:choose>
	<c:when test="${!isMobile}">
		<jsp:include page="/dms/category/navi" flush="false">
			<jsp:param name="categoryId" 	value="${dispCategoryId}" />
		</jsp:include>
	</c:when>
	<c:otherwise>
		<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
			<jsp:param value="${category.name}" name="name"/>
			<jsp:param value="${category.depth}" name="depth"/>
			<jsp:param value="category" name="type"/>
		</jsp:include>	
	</c:otherwise>
</c:choose>
	
<div class="inner">
	<!-- 카테고리  //-->
	<div class="cateMain">
		<h2 class="tit_style2 mt pc_only"><c:out value="${category.name}"/></h2>

		<c:if test="${not empty category.dmsDisplaycategorys}">
			<c:choose>
				<c:when test="${isMobile}">
					<div class="categoryS">
						<jsp:include page="/WEB-INF/views/dms/include/categoryList.jsp" flush="false"/>
					</div>
				</c:when>
				<c:otherwise>
					<div class="pc_categoryS">
						<jsp:include page="/WEB-INF/views/dms/include/categoryList.jsp" flush="false"/>
					</div>
				</c:otherwise>
			</c:choose>
		</c:if>
		<!--// 카테고리  -->
		
		<!-- 상단 비주얼 // -->
		<c:set var="mainBnrMap"  value="${cornerMap[mainBnr]}"/>
		<c:if test="${not empty mainBnrMap.dmsDisplayitems}">
			<div class="swiper_wrap">
				<div class="visual type1 swiper-container displayCategorySwiper_banner_1">
					<ul class="vImg swiper-wrapper">
						<!-- <li class="swiper-slide on"><a href="#"><img src="img/pc/temp/temp_promo_01.jpg" alt="" /></a></li> -->
						<c:forEach var="item" items="${mainBnrMap.dmsDisplayitems}" varStatus="status">
							<li class="swiper-slide ${status.index ==0 ? 'on' : '' }">
								<c:choose>
									<c:when test="${isMobile}">
											<a href="/dms/exhibit/detail?exhibitId=${item.dmsExhibit.exhibitId}">
												<img src="${_IMAGE_DOMAIN_}${item.dmsExhibit.img2}" alt="${item.dmsExhibit.img2}" />
												<div class="vImginfo">
													<span class="vii_tit"><c:out value="${item.dmsExhibit.name}" /></span> 
													<span class="vii_txt"><c:out value="${item.dmsExhibit.subtitle}" /></span>
												</div>
											</a>
										
									</c:when>
									<c:otherwise>
										<c:if test="${not empty item.url1  }">
											<a href="${item.url1 }">
										</c:if>	
											<img src="${_IMAGE_DOMAIN_}${item.img1}" alt="${item.img1}" />
										<c:if test="${not empty item.url1  }">
											</a>
										</c:if>
									</c:otherwise>
								</c:choose>
							</li>
						</c:forEach>
					</ul>
					<c:choose>
						<c:when test="${isMobile}">
							<!-- Mobile -->
							<div class="pageInner_wrap">
								<div class="pageInner">
							        <div class="swiper-pagination tp4"></div>
							        <button type="button" class="bnt_allView" onclick="ccs.layer.copyLayerToContentChild('ly_visual')">전체보기</button>
						        </div>
							</div>
							<!-- Mobile -->
						</c:when>
						<c:otherwise>
							<!-- PC -->
							<div class="swiper-button-next btn_tp7"></div>
							<div class="swiper-button-prev btn_tp7"></div>
							<!-- PC -->
						</c:otherwise>
					</c:choose>
				</div>
			</div>
					
		</c:if>
		
		<!--// 상단 비주얼 --> 
		
		<!-- 브랜드 배너 //-->
		<c:set var="brandBnrMap"  value="${cornerMap[brandBnr]}"/>
		<c:if test="${brandBnrMap.totalCount > 0}">
			<div class="bnrBrand"> 
				<h3 class="tit_style2">추천브랜드</h3>
				<div class="brandList">
					<ul>
						<c:forEach var="item" items="${brandBnrMap.dmsDisplayitems}" varStatus="status">
							<li><a href="${item.url1}"><img src="${_IMAGE_DOMAIN_}${item.pmsBrand.logoImg}" alt="${item.pmsBrand.logoImg}" /></a></li>
						</c:forEach>
					</ul>
				</div>
				<c:if test="${!isMobile}">
					<div class="btnMore">
						<button type="button">more</button>
					</div>
				</c:if>
			</div>
		</c:if>
		<!--// 브랜드 배너--> 	
		
		<!-- MD추천상품 //-->
		<c:set var="mdPrdMap"  value="${cornerMap[mdPrd]}"/>
		<c:if test="${ not empty mdPrdMap.dmsDisplayitems}">
			<div class="rolling_box md">
				<h3 class="tit_style2">MD 추천<span class="pc_only">상품</span></h3>
				<div class="swiper_wrap">
					<div class="swiper-container prodSwiper product_type1 prodType_4ea block displaySwiper_prodList_1">
						<ul class="swiper-wrapper">
							<c:forEach var="item" items="${mdPrdMap.dmsDisplayitems}" varStatus="status">
								<c:set var="product" value="${item.pmsProduct}" scope="request" />
								<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
									<jsp:param name="swipe" value="Y"/>
								</jsp:include>
							</c:forEach>
						</ul>
						<!-- Add Arrows -->
						<div class="swiper-button-next btn_tp2"></div>
						<div class="swiper-button-prev btn_tp2"></div>

					</div>
				</div>
			</div>
		</c:if>
		<!--// MD추천상품 -->
		
		<!-- PC 코너  //-->
		<c:if test="${!isMobile}">
			<!-- 기획전 배너 //-->
			<div class="promotion_list pc_only">
				<c:if test="${not empty exhibitList}">
					<ul>
						<c:forEach var="item" items="${exhibitList}" varStatus="status">
							<li>
								<a href="/dms/exhibit/detail?exhibitId=${item.exhibitId}">
									<img src="${_IMAGE_DOMAIN_}${item.img1}" alt="" />
									<div class="info">
										<div class="tit">${item.name}</div>
										<div class="dsc">${item.subtitle}</div>
									</div>
								</a>
							</li>
						</c:forEach>
					</ul>
				</c:if>
			</div>
			<!--// 기획전 배너 -->
		</c:if>
		<!--// PC 대카테고리 코너  -->
		
		<!-- mobile //-->
		<c:if test="${isMobile}">
			<!-- 옵션필터링 -->
			<jsp:include page="/WEB-INF/views/dms/include/searchOptionFilter.jsp" flush="false">
				<jsp:param name="type" 	value="category" />
			</jsp:include>	
			
			<div class="displayListBox mt">
				
				<div class="tit_style3">
					<strong> 
						<%-- <c:out value="${currentCategory.name}"/> --%>
						총 <span>
							 <em id="searchCount">${search.totalCount}</em>건 
						</span>
					</strong>
					
					<!-- 정렬 //-->
					<jsp:include page="/WEB-INF/views/dms/include/searchOrderby.jsp" flush="false"/>
				</div>
				<!-- 상품목록 //-->
				<div class="list_group" id="productList">
					<jsp:include page="/WEB-INF/views/dms/include/searchProductList.jsp" flush="false">
						<jsp:param value="N" name="pagingYn"/>
					</jsp:include>
				</div>
			</div><!--// 상품목록 -->
		</c:if>
	</div> <!--// cateMain -->
</div> <!--// inner  -->

<!-- mobile 팝업 : 전체보기 -->
<c:if test="${isMobile}">
	<div class="pop_wrap ly_visual">
		<div class="pop_inner">
			<c:set var="mainBnrIm"  value="${cornerMap[mainBnr]}"/>
			<c:if test="${mainBnrIm.totalCount > 0}">
				<div class="pop_header type1">
					<h3>전체보기</h3>
				</div>
				<div class="pop_content">
					<ul>
						<c:forEach var="item" items="${mainBnrIm.dmsDisplayitems}" varStatus="status">
							<li><%-- <a href="${item.url2}"><img src="${_IMAGE_DOMAIN_}${item.img2}" alt="" /></a> --%>
								<a href="/dms/exhibit/detail?exhibitId=${item.dmsExhibit.exhibitId}">
								<img src="${_IMAGE_DOMAIN_}${item.dmsExhibit.img2}" alt="${item.dmsExhibit.img2}" />
								<div class="vImginfo">
									<span class="vii_tit"><c:out value="${item.dmsExhibit.name}" /></span> 
									<span class="vii_txt"><c:out value="${item.dmsExhibit.subtitle}" /></span>
								</div>		
							</li>								
						</c:forEach>
					</ul>
				</div>
				<button type="button" class="btn_x pc_btn_close">닫기</button>
			</c:if>
		</div>
	</div>
</c:if>
<!-- //mobile 팝업 : 전체보기 -->