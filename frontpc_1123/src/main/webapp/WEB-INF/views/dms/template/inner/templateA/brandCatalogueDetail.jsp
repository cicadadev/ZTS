<%--
	화면명 : 브랜드관 > 템플릿A > CATALOG & COORDI LOOK 상세 화면
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<link rel="stylesheet" type="text/css" href="/resources/css/lightslider.css" />
<script type="text/javascript" src="/resources/js/lightslider.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	// 모바일. 카탈로그 제목으로 SELECTBOX 만들기
	brand.template.makeTitleSelect("${catalogItem.catalogTypeCd}");
	
	// SNS 공유용 메타 데이터 세팅
	$("meta[property='og:image']").attr("content", "${ogTagImage}");
	$("meta[property='og:url']").attr("content", "${ogTagUrl}");
	$("meta[property='og:title']").attr("content", "${ogTagTitle}");
	
	// 카탈로그&룩북 상세 화면에서는 BOTTOM, FOOTER 안 보이게.
	if (ccs.common.mobilecheck()) {
		$(".bottom_menu").hide();
		$(".footer_mo").hide();
	} else {
		var offset = $(".brand_inner").offset();
		$('html, body').animate({scrollTop : offset.top}, 400);
	}
	
	// 모바일. 카탈로그&코디북 제목 SELECTBOX 선택시 해당 카탈로그&룩북 조회
	$(".mobile #mo_seasonSelect").on("change", function() {
		brand.template.seasonSelect(mainSwiper_body);
	});
	
	// 카탈로그&코디북 상세에서 상품상세 갔다가 돌아오기 위해 현재 카탈로그 제목 기억
	$("#catalogForm #hid_catalogTitle").val("${catalogItem.name}");
	
	// sns 공유
	$(".sns_btn a").on("click", function() {
		brand.template.snsDataSetting("${_FRONT_DOMAIN_URL_}", "${catalogItem.brandId}", "${catalogItem.name}");
		
		var shareType = $(this).attr("class");
		if (shareType == "fbook") {
			ccs.sns.share("facebook");
		} else if (shareType == "kakao") {
			ccs.sns.share("kakaoStory");
		} else if (shareType == "kakaotalk") {
			ccs.sns.share("kakaoLink");
		} else if (shareType == "url") {
			ccs.sns.share("link");
		} else {
			ccs.sns.share(shareType);
		}
	});
	
	// SNS LAYER EVENT
	$(".mobile .btn_snsInfo").off("click").on({
		"click" : function() {
			var name = $("#mo_seasonSelect option:selected").text();
			brand.template.snsDataSetting("${_FRONT_DOMAIN_URL_}", "${catalogItem.brandId}", name);
			
			fnLayerPosition( $(".mobile .sLayer_sns") );
		}
	});
	
	// 모바일. SNS공유 레이어팝업 위치 조정
	function fnLayerPosition(target_pop) {
		$(target_pop).show();
		$(target_pop).height( $(document).height() );

		var base_top = ($(window).height() - $(" > .box", target_pop).innerHeight()) / 2;
		$(" > .box", target_pop).css({"marginTop" : base_top + $(window).scrollTop() + "px"});
	}

});
</script>

<div class="style_type_book">
	<c:set var="catalogImgs" value="${catalogItem.dmsCatalogimgs}" />
	
	<c:if test="${isMobile eq 'true'}">
		<div class="style_book_cate_mb">
			<div class="select_box1 radiusSel" style="text-align:center;">
				<label>${catalogItem.name}</label>
				<select id="mo_seasonSelect">

				</select>
			</div>
		</div>
	</c:if>
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
					
		<button type="button" class="btn_snsInfo">sns 공유</button>
		<div class="sns_btn">
			<a class="fbook">facebook</a>
			<a class="kakao">kakao story</a>
			<c:if test="${isMobile eq 'true'}">
				<a class="kakaotalk">Kakao</a>
			</c:if>		
			<a class="twitter">twitter</a>
			<a class="blog">blog</a>
			<a class="url">URL복사</a>
		</div>
	</div>
	
	<div class="style_book_list ${isMobile ne 'true' ? 'rolling_box' : ''}">
		<div class="product_type1 block">
			<div id="lookbookProduct">
				<jsp:include page="/WEB-INF/views/dms/template/inner/templateA/brandCatalogueProduct.jsp" />
			</div>
		</div>
		
		<c:if test="${isMobile ne 'true'}">
			<div class="paginate">
				<button class="prev" onclick="brand.template.moreProduct('prev');">이전</button>
				<span><em class="current">1</em>/<span class="total">1</span></span>
				<button class="next" onclick="brand.template.moreProduct('next');">다음</button>
			</div>
		</c:if>
		
		<form name="activeImgForm" id="activeImgForm">
			<input type="hidden" name="hid_activeIdx" id="hid_activeIdx" value="1" />
		</form>
	</div>	
</div>

<style>
.pc .brand_book_detail .sns_btn a {display: inline-block; width:20px; height:20px; background:url(/resources/img/pc/ico/snsicon.png) no-repeat 0 0; font-size:0; line-height:0; vertical-align:top;}
.pc .brand_book_detail .sns_btn a.fbook {background-position:0 0;}
.pc .brand_book_detail .sns_btn a.kakao {background-position:-24px 0;}
.pc .brand_book_detail .sns_btn a.kakaotalk {background-position:-48px 0;}
.pc .brand_book_detail .sns_btn a.twitter {background-position:-72px 0;}
.pc .brand_book_detail .sns_btn a.blog {background-position:-96px 0;}
.pc .brand_book_detail .sns_btn a.url {background-position:-120px 0;}
</style>