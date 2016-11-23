<%--
	화면명 : 프론트 & 모바일  브랜드 템플릿A
	작성자 : emily
 --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
	
<style>
.pc .mainCon {border:1px solid #fff}
.pc .tit_style2 {border-bottom:none;}
</style>

<script type="text/javascript" src="/resources/js/common/brand.ui.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	var direct = "${direct}";
	
	// CATALOG, EVENT, EXHIBIT 등 페이지 이동 후, BACK 했을 때 보던 페이지 유지하기 위해.
	if(ccs.common.mobilecheck()) {
		if (direct != "") {
			if (direct == "catalogDetail") {
				brand.template.backPage(mainSwiper_body, direct, '${brandInfo.brandId}', '${catalogId}', '${catalogImgNo}');
			}
		} else {
			brand.template.backPage(mainSwiper_body);
		}
	} else {
		if (direct != "") {
			if (direct == "catalogDetail") {
				brand.template.backPage(null, direct, '${brandInfo.brandId}', '${catalogId}', '${catalogImgNo}');
			}
		} else {
			brand.template.backPage();
		}				
	}
	
	// 카탈로그&코디북 상세 화면에서 뒤로 가기
	$("#btn_back").on("click", function() {
		if ($("#catalogDetailYn").val() == 'Y') {
			$("#lookbook_detail").hide();
			
			if (common.isNotEmpty($("#brandForm #directCatalogYn").val())) {
				brand.template.itemSearch('CATALOGUE');
			} else {
				$("#lookbook_list").show();
				ccs.mainSwipe.calculateHeight();
			}
			
			mainSwiper_body.unlockSwipes();	// 막았던 스와이프 다시 동작하도록.			
			$(".mo_mainNavi").show();
			$(".mobile .content").removeAttr("style").css("padding", "91px 0 347px 0");	// 상세화면에서 안 보이던 gnb 다시 보이도록.
			
			$(".bottom_menu").show();
			$(".footer_mo").show();
			
			$("#catalogDetailYn").val('N');
		} else {
			history.back();
		}
	});
	
	// 대카 마지막 'last' class 추가(16.11.23)
	$(".brand_category > li:last").addClass("last");

});

</script>

<div>
	<input type="hidden" name="hid_brandId" id="hid_brandId" value="${brandInfo.brandId}" />
	<input type="hidden" name="hid_brandName" id="hid_brandName" value="${brandInfo.name}" />
	<input type="hidden" name="hid_templateType" id="hid_templateType" value="A" />
	<input type="hidden" name="hid_brandClassName" id="hid_brandClassName" value="${brandClassName}" />
</div>

<form name="brandForm" id="brandForm">
	<input type="hidden" name="hidCurrentPage" id="hidCurrentPage" value="" />
	<input type="hidden" name="hidCurrentTab" id="hidCurrentTab" value="" />
	<input type="hidden" name="catalogDetailYn" id="catalogDetailYn" value="" />
	<input type="hidden" name="directCatalogYn" id="directCatalogYn" value="" />
</form>

<form name="catalogForm" id="catalogForm">
	<input type="hidden" name="hid_catalogId" id="hid_catalogId" value="" />
	<input type="hidden" name="hid_catalogImgNo" id="hid_catalogImgNo" value="" />
	<input type="hidden" name="hid_catalogTitle" id="hid_catalogTitle" value="" />
</form>

<form name="pagingForm" id="realtime_pagingForm">
	<input type="hidden" name="hid_current" id="hid_current" value="" />
	<input type="hidden" name="hid_totalCnt" id="hid_totalCnt" value="" />
</form>

<c:if test="${isMobile ne 'true'}">

	<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
		<jsp:param value="${brandInfo.name}" name="pageNavi"/>
		<jsp:param value="/dms/common/templateDisplay?brandId=${brandInfo.brandId}" name="url" />
	</jsp:include>

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
						<a href="javascript:brand.template.cornerItem('CATALOGUE');">CATALOGUE</a>
					</li>
					<li>
						<a href="javascript:brand.template.cornerItem('STYLE');">STYLE</a>
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
										<a href="javascript:ccs.link.product.brandProdutList('CATEGORY','${brandInfo.brandId}','${category2.displayCategoryId}','${category1.displayCategoryId}');">${category2.name}</a>
									</li>
								</c:forEach>	
							</ul>
						</li>
					</c:forEach>
				</ul>
			</div>
		</div>
	</div>
</c:if>
	
<jsp:include page="/WEB-INF/views/dms/template/inner/templateA/brandContents.jsp" />
