<%--
	화면명 : 브랜드관 > 템플릿A > CATALOG & COORDI LOOK 상세 화면
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript" src="/resources/js/dms/dms.salesAssist.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/css/lightslider.css" />
<script type="text/javascript" src="/resources/js/lightslider.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	// 모바일. 카탈로그 제목으로 SELECTBOX 만들기
	sales.makeTitleSelect("${catalogItem.catalogTypeCd}");
	
	// 카탈로그&룩북 상세 화면에서는 BOTTOM, FOOTER 안 보이게.
	$(".bottom_menu").hide();
	$(".footer_mo").hide();

	
	// 모바일. 카탈로그&코디북 제목 SELECTBOX 선택시 해당 카탈로그&룩북 조회
	$(".mobile #mo_seasonSelect").on("change", function() {
		sales.seasonSelect();
	});
	
	// 카탈로그&코디북 상세에서 상품상세 갔다가 돌아오기 위해 현재 카탈로그 제목 기억
	$("#catalogForm #hid_catalogTitle").val("${catalogItem.name}");

});
</script>

<div class="style_type_book">
	<c:set var="catalogImgs" value="${catalogItem.dmsCatalogimgs}" />
	
	<div class="style_book_cate_mb">
		<div class="select_box1 radiusSel" style="text-align:center;">
			<label>${catalogItem.name}</label>
			<select id="mo_seasonSelect">

			</select>
		</div>
	</div>
	<div class="brand_book_detail" style="margin-top:10px;">
		<div class="item">
			<div class="img_detail clearfix">
				<c:choose>
					<c:when test="${empty catalogImgs}">
						<c:if test="${isMobile eq 'true'}">
							<ul style="line-height:135px;">
						</c:if>
						<c:if test="${isMobile ne 'true'}">
							<ul id="imageGallery">
						</c:if>
						
							<li>
								<div class="empty">이미지가 없습니다.</div>
							</li>
						</ul>
					</c:when>
					<c:otherwise>
						<ul id="imageGallery">
							<c:forEach items="${catalogImgs}" var="catalogImg" varStatus="status1">
								<c:choose>
									<c:when test="${isMobile eq 'true'}">
										<li data-thumb="${_IMAGE_DOMAIN_}${catalogImg.img2}|${catalogItem.catalogId}||${catalogImg.catalogImgNo}">
											<img src="${_IMAGE_DOMAIN_}${catalogImg.img2}" alt="${catalogImg.name}" width="100%"/>
										</li>
									</c:when>
									<c:otherwise>
										<li data-thumb="${_IMAGE_DOMAIN_}${catalogImg.img1}|${catalogItem.catalogId}||${catalogImg.catalogImgNo}">
											<c:if test="${catalogItem.catalogTypeCd eq 'CATALOG_TYPE_CD.LOOKBOOK'}">
												<img src="${_IMAGE_DOMAIN_}${catalogImg.img1}" alt="${catalogImg.name}" style="margin-top:15px;" />
											</c:if>
											
											<c:if test="${catalogItem.catalogTypeCd eq 'CATALOG_TYPE_CD.COORDILOOK'}">
												<img src="${_IMAGE_DOMAIN_}${catalogImg.img1}" alt="${catalogImg.name}" style="margin-top:15px;" />
											</c:if>
										</li>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</ul>
					</c:otherwise>
				</c:choose>
			</div>
		</div>
	</div>
	
	<div class="style_book_list">
		<div class="product_type1 block">
			<div id="lookbookProduct">
				<jsp:include page="/WEB-INF/views/dms/template/inner/salesAssist/salesCatalogueProduct.jsp" />
			</div>
		</div>
		
		<form name="activeImgForm" id="activeImgForm">
			<input type="hidden" name="hid_activeIdx" id="hid_activeIdx" value="1" />
		</form>
	</div>	
</div>
