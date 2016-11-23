<%--
	화면명 : 쇼킹제로 > 메인
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script type="text/javascript">
$(document).ready(function() {
	exhibit.main.getExhibitList();

});
	
	// 카테고리, 브랜드 	
	function changeTab(param) {
		if (param == "category") {
			$(".list_package_ctrlwap .sortBox2 > li").eq(1).removeClass("on");
			$(".list_package_ctrlwap .sortBox2 > li").eq(0).addClass("on");
			
			$("#category_ul >li").each(function() {
				if ($(this).hasClass("active")) {
					var cateId = $(this).attr("id").substring($(this).attr("id").indexOf("_")+1, $(this).attr("id").length);
					if (cateId == 'all') {
						exhibit.main.getExhibitList();
					} else {
						exhibit.main.getExhibitList(cateId, "");
					}
				}
			});
		} else if (param == "brand") {
			$(".list_package_ctrlwap .sortBox2 > li").eq(0).removeClass("on");
			$(".list_package_ctrlwap .sortBox2 > li").eq(1).addClass("on");
			
			$("#brand_ul > li").each(function() {
				if ($(this).hasClass("active")) {
					var brandId = $(this).attr("id").substring($(this).attr("id").indexOf("_")+1, $(this).attr("id").length);
					if (brandId == 'all') {
						exhibit.main.getExhibitList();
					} else {
						exhibit.main.getExhibitList("", brandId);
					}
				}
			});
		}
	}
	
	// 카테고리
	function changeCate(cateId) {
		$("#category_ul > li").each(function() {
			$(this).removeClass("active");
		});
		$("#cate_"+cateId).addClass("active");
		if (cateId == 'all') {
			exhibit.main.getExhibitList();
		} else {
			exhibit.main.getExhibitList(cateId, "");
		}
	}
	
	// 브랜드
	function changeBrand(brandId) {
		$("#brand_ul > li").each(function() {
			$(this).removeClass("active");
		});
		$("#brand_"+brandId).addClass("active");
		if (brandId == 'all') {
			exhibit.main.getExhibitList();
		} else {
			exhibit.main.getExhibitList("", brandId);
		}
	}
</script>
	
	<c:choose>
	<c:when test="${isMobile ne 'true' }">
		<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
			<jsp:param value="기획전" name="pageNavi"/>
		</jsp:include>	
	</c:when>
	<c:otherwise>	
		<jsp:include page="/WEB-INF/views/gsf/layout/page/mo/title_menu.jsp" flush="false" >
			<jsp:param name="titleMenu" value="4" />
		</jsp:include>
	</c:otherwise>	
	</c:choose>	
	<div class="inner">
		<div class="promotion">
			<!-- 상단 비주얼 -->
				<c:if test="${isMobile}">
					<c:if test="${not empty mbBigBannerList }">
						<div class="visual">
							<ul class="vImg">
								<c:forEach var="corner" items="${mbBigBannerList}" varStatus="status">
									<c:forEach var="item" items="${corner.dmsDisplayitems}" varStatus="status">
										<li>
											<a href="${item.url2}">
												<img src="${_IMAGE_DOMAIN_}${item.img2}" alt=""/>
												<span class="brand_visual_info mo_only">
													<span>${item.text2}</span>
												</span>
											</a>
										</li>
									</c:forEach>
								</c:forEach>
							</ul>
							<div class="mb_page">
								<div class="pageInner">
									<i>1</i> / <em>0</em>
									<button type="button">전체보기</button>
								</div>
							</div>
						</div>
					</c:if>
				</c:if>
				<c:if test="${!isMobile}">
					<c:if test="${not empty corner[0].dmsDisplayitems}">
						<div class="visual">
							<ul class="vImg">
								<c:forEach var="corner" items="${cornerList}" varStatus="j"> 
									<c:forEach var="item" items="${corner.dmsDisplayitems}" varStatus="i"> 
										<c:choose>
											<c:when test="${i.first}">
												<li class="on">
											</c:when>
											<c:otherwise>
												<li>
											</c:otherwise>
										</c:choose>
											<c:choose>
												<c:when test="${isMobile}">
													<a href="${item.url2}">
														<img src="${_IMAGE_DOMAIN_}${item.img2}" alt=""/>
														<span class="brand_visual_info mo_only">
														<span>${item.text2}</span>
														</span>
													</a>
												</c:when>
												<c:otherwise>
													<a href="${item.url1}">
														<img src="${_IMAGE_DOMAIN_}${item.img1}" alt=""/>
													</a>
												</c:otherwise>
											</c:choose>	
										</li>
									</c:forEach>
								</c:forEach>
							</ul>
							<div class="control">
								<div class="dots">
									<c:forEach var="corner" items="${cornerList}" varStatus="j"> 
										<c:forEach var="item" items="${corner.dmsDisplayitems}" varStatus="i"> 
											<c:choose>
												<c:when test="${i.first}">
													<span class="on"></span>
												</c:when>
												<c:otherwise>
													<span></span>
												</c:otherwise>
											</c:choose>
										</c:forEach>
									</c:forEach>
								</div>
								<div class="btns">
									<button type="button" class="prev">이전</button>
									<button type="button" class="next">다음</button>
								</div>
							</div>
						</div>
					</c:if>
				</c:if>
			<!-- //상단 비주얼 -->

			<!-- mobile -->
			<div class="sortBoxBtn sort_2ea pcHide">
				<ul>
					<li>
						<div class="select_box1 radiusSel">
							<label></label>
							<select onchange="javascript:exhibit.main.changeCategoryNbrand(this);" id="select_cate">
								<option value="all">카테고리</option>
								<option value="10001">식품/분유</option>
								<option value="10002">물티슈/기저귀</option>
								<option value="10003">의류/잡화</option>
								<option value="10004">용품/완구/화장품</option>
							</select>
						</div>
					</li>
					<li>
						<div class="select_box1 radiusSel">
							<label></label>
							<select onchange="javascript:exhibit.main.changeCategoryNbrand(this);" id="select_brand">
								<option value='all'>브랜드</option>
								<c:forEach var="brand" items="${brandList}" varStatus="i">
									<option value="${brand.brandId}">${brand.name}</option>
								</c:forEach>
							</select>
						</div>
					</li>
				</ul>
			</div>
			<!-- //mobile -->
			
			<div class="list_package">
				<div class="list_package_ctrlwap">
					<ul class="sortBox2">
						<li class="on">
							<a href="javascript:changeTab('category');">
								<span>카테고리</span>
							</a>
							<ul class="tab_box_02" id="category_ul">
								<li class="active" id="cate_all"><a href="javascript:changeCate('all');" class="cate1">전체</a></li>
								<li id="cate_10001"><a href="javascript:changeCate('10001');" class="cate2">식품/분유</a></li>
								<li id="cate_10002"><a href="javascript:changeCate('10002');" class="cate3">물티슈/기저귀</a></li>
								<li id="cate_10003"><a href="javascript:changeCate('10003');" class="cate4">의류/잡화</a></li>
								<li id="cate_10004"><a href="javascript:changeCate('10004');" class="cate5">용품/완구/화장품</a></li>
							</ul>
						</li>
						<li>
							<a href="javascript:changeTab('brand');">
								<span>브랜드</span>
							</a>
							<!-- Swiper -->
					        <div class="swiper_wrap">
								<div class="swiper-container promotionSwiper_brandCategory">
									<ul class="swiper-wrapper" id="brand_ul">
										<li class="swiper-slide" id="brand_all"><a href="javascript:changeBrand('all')">전체</a></li>
										<c:forEach var="brand" items="${brandList}" varStatus="i">
											<li class="swiper-slide" id="brand_${brand.brandId}"><a href="javascript:changeBrand('${brand.brandId}')">${brand.name}</a></li>
										</c:forEach>
									</ul>
									<!-- Add Arrows -->
									<c:if test="${fn:length(brandList) > 5 }">
								    	<div class="swiper-button-next btn_tp6"></div>
								    	<div class="swiper-button-prev btn_tp6"></div>
									</c:if>

								</div>
							</div>
						</li>
					</ul>
				</div>

				<div class="tab_con tab_conOn">
					<div class="promotion_list exhibitList_ul" id="exhibitListDiv">
					</div>
				</div>
			</div>
		</div>
	</div>

	<!-- mobile 팝업 : 전체보기 -->
<c:if test="${isMobile}">
	<div class="pop_wrap ly_visual">
		<div class="pop_inner">
			<div class="pop_header type1">
				<h3>전체보기</h3>
			</div>
			<div class="pop_content">
				<ul>
					<c:forEach var="corner" items="${mbBigBannerList}" varStatus="status">
						<c:forEach var="item" items="${corner.dmsDisplayitems}" varStatus="status">
							<li>
								<a href="/dms/exhibit/detail?exhibitId=${item.dmsExhibit.exhibitId}">
									<img src="${_IMAGE_DOMAIN_}${item.dmsExhibit.img2}" alt="" />
									<div class="vImginfo">
										<span class="vii_tit"><c:out value="${item.dmsExhibit.name}" /></span> 
										<span class="vii_txt"><c:out value="${item.dmsExhibit.subtitle}" /></span>
									</div>
								</a>
							</li>
						</c:forEach>
					</c:forEach>
				</ul>
			</div>
			<button type="button" class="btn_x pc_btn_close">닫기</button>
		</div>
	</div>
</c:if>
<!-- //mobile 팝업 : 전체보기 -->