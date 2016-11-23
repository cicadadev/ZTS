<%--
	화면명 : 프론트 & 모바일  브랜드 템플릿B
	작성자 : emily
 --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<style>
.pc .mainCon {border:1px solid #fff}
.pc .tit_style2 {border-bottom:none;}

/* .mobile .mo_mainNavi .gnb li {margin-right:0}
.mobile .mo_mainNavi .gnb li:first-child {margin-left:0} */
</style>

<script type="text/javascript" src="/resources/js/common/brand.ui.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	// EVENT, EXHIBIT 등 페이지 이동 후, BACK 했을 때 보던 페이지 유지하기 위해.
	if(ccs.common.mobilecheck()) {
		brand.template.backPage(mainSwiper_body);
	} else {
		brand.template.backPage();
	}
	
	// 대카 마지막 'last' class 추가(16.11.23)
	$(".brand_category > li:last").addClass("last");
});
</script>

<div>
	<input type="hidden" name="hid_brandId" id="hid_brandId" value="${brandInfo.brandId}" />
	<input type="hidden" name="hid_brandName" id="hid_brandName" value="${brandInfo.name}" />
	<input type="hidden" name="hid_templateType" id="hid_templateType" value="B" />
	<input type="hidden" name="hid_brandClassName" id="hid_brandClassName" value="${brandClassName}" />
</div>

<form name="brandForm" id="brandForm">
	<input type="hidden" name="hidCurrentPage" id="hidCurrentPage" value="" />
	<input type="hidden" name="hidCurrentTab" id="hidCurrentTab" value="" />
</form>

<form name="pagingForm" id="best_pagingForm">
	<input type="hidden" name="hid_current" id="hid_current" value="" />
	<input type="hidden" name="hid_pageSize" id="hid_pageSize" value="" />
	<input type="hidden" name="hid_totalCnt" id="hid_totalCnt" value="" />
</form>

<form name="pagingForm" id="realtime_pagingForm">
	<input type="hidden" name="hid_current" id="hid_current" value="" />
	<input type="hidden" name="hid_totalCnt" id="hid_totalCnt" value="" />
</form>

<c:choose>
	<c:when test="${isMobile eq 'true'}">
<%-- 		<div class="mo_navi">
			<button type="button" class="btn_navi_prev" onclick="javascript:history.back();">이전 페이지로..</button>
			<h2>
				<!-- 제목만 노출 할 경우 -->
				<a href="javascript:brand.template.main('${brandInfo.brandId}');">
					<img src="/resources/img/mobile/txt/txt_brand_${brandInfo.brandId}.jpg" alt="" width="118.5" onclick="brand.template.main('${brandInfo.brandId}');" style="padding:0 0 10px 0;"/>
<!-- 				</a> -->
			</h2>
		</div> --%>
	</c:when>
	<c:otherwise>
		<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
			<jsp:param value="${brandInfo.name}" name="pageNavi"/>
			<jsp:param value="/dms/common/templateDisplay?brandId=${brandInfo.brandId}" name="url" />
		</jsp:include>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${isMobile eq 'true'}">
<%-- 		<div class="mo_mainNavi brandNavi ${brandClassName}">
			<ul class="gnb swiper-wrapper">
				<li class="inner" style="width:25%;">
					<a href="javascript:brand.template.main('${brandInfo.brandId}');">HOME</a>
				</li>
				<li class="inner" style="width:25%;">
					<a href="javascript:brand.template.cornerItem('STORY');">STORY</a>
				</li>
				<li class="inner" style="width:25%;">
					<a href="javascript:brand.template.cornerItem('PRODUCTS');">PRODUCTS</a>
				</li>
				<li class="inner" style="width:25%;">
					<a href="javascript:brand.template.cornerItem('EVENT');">EVENT</a>
				</li>
			</ul>
		</div> --%>
	</c:when>			
	<c:otherwise>
		<div class="brand_menu pc_only">
			<div class="brand_inner">
				<div class="brand_logo">
					<div onclick="ccs.link.go('/dms/common/templateDisplay?brandId=${brandInfo.brandId}', CONST.NO_SSL);">
						<img src="/resources/img/pc/txt/txt_brand_${brandInfo.brandId}.png" alt="${brandInfo.name}" />
					</div>
				</div>
				<div class="brand_outer">
					<ul class="brand_section">
						<li>
							<a href="javascript:brand.template.cornerItem('STORY');">STORY</a>
						</li>
						<li>
							<a href="javascript:brand.template.cornerItem('PRODUCTS');">PRODUCTS</a>
						</li>
						<li>
							<a href="javascript:brand.template.cornerItem('EVENT');">EVENT</a>
						</li>
					</ul>
					
					<ul class="brand_category">
						<c:forEach items="${brandCategory}" var="category1" varStatus="status1">
							<li>
								<a>${category1.name}</a>
								<ul>
									<c:forEach items="${category1.dmsDisplaycategorys}" var="category2" varStatus="status2">
										<li>
											<c:choose>
												<c:when test="${brandClassName == 'brand4'}">
													<a href="javascript:ccs.link.product.detail('${category2.dmsDisplaycategoryproducts[0].productId}');">${category2.name}</a>
												</c:when>
												<c:otherwise>
													<a href="javascript:ccs.link.product.brandProdutList('CATEGORY','${brandInfo.brandId}','${category2.displayCategoryId}','${category1.displayCategoryId}');">${category2.name}</a>
												</c:otherwise>
											</c:choose>										
										</li>
									</c:forEach>
								</ul>
							</li>
						</c:forEach>
						<c:if test="${brandClassName == 'brand4'}">
							<li>
								<a href="http://yvolution.co.kr/free-warranty/">정품 인증</a>
							</li>
						</c:if>
					</ul>
				</div>
			</div>
		</div>			
	</c:otherwise>
</c:choose>	

<jsp:include page="/WEB-INF/views/dms/template/inner/templateB/brandContents.jsp" />
