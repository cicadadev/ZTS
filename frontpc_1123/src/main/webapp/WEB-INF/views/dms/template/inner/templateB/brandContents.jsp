<%--
	화면명 : 브랜드관 > 템플릿B
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	pageContext.setAttribute("mainBnr", Config.getString("corner.brand.templB.main") );
	pageContext.setAttribute("recommendBnr", Config.getString("corner.brand.templB.recommend") );
	pageContext.setAttribute("recommendProduct", Config.getString("corner.brand.templB.rec.product") );
	pageContext.setAttribute("videoBnr", Config.getString("corner.brand.templB.video") );
	pageContext.setAttribute("storyBnr", Config.getString("corner.brand.templB.story") );
	pageContext.setAttribute("productBnr", Config.getString("corner.brand.templB.product") );
	pageContext.setAttribute("eventBnr", Config.getString("corner.brand.templB.event") );
	pageContext.setAttribute("storyBnr2", Config.getString("corner.brand.templB.storyB") );
%>

<script type="text/javascript" src="/resources/js/common/common.ui.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	// BO에서 등록한 HTML 제대로 그리기 위해 꼭 필요!
	brand.template.appendHtml();
	
	pms.brand.realtimeProduct('${brandInfo.erpBrandId}');	// 실시간 인기상품
	pms.brand.best("${brandInfo.brandId}");	// 베스트 상품
	
	if(ccs.common.mobilecheck()) {
		brand.template.itemSearch('EVENT');	// 이벤트&기획전 조회
	}
	
	// 브랜드 공지사항
	custcenter.notice.brand($("#hid_brandId").val());
	
	// 모바일. 메인 스토리배너 클릭 시, 스토리 화면으로 바로 이동
	$(".mobile #story_Bnr").on("click", function() {
		mainSwiper_body.slideTo(2, 300);
	});
	
	// 메인 배너 1개 이상일때만 자동 롤링(16.11.22) 	
	if(ccs.common.mobilecheck()) {
		swiperCon('brandSwiper_banner_1', '1', 0, 'fraction'); //배너
	} else {
		if ($("#mainBnr_ul").find("li").length > 1) {
			swiperCon('brandSwiper_banner_1', 400, 1, 0, 2000, true);// 메인 배너 스와이프
		} else {
			swiperCon('brandSwiper_banner_1', 400, 1, 0, 2000, false);// 메인 배너 스와이프
		}
	}
});
</script>

<div class="mainCon brandType ${brandClassName} brandB_01">
	<div class="swiper-wrapper allSwiper" id="mainSwiperWrapper">

		<!-- 
		***************************************************************
		 * 메인
		*************************************************************** 
		-->
		<div class="swiper-slide inner brand_home">
		<%-- <div class="inner brand_home brandB_01" id="main1" data-index="1" data-type="brandB" ${isMobile ne 'true'? 'style="display:block;"' : ''}> --%>
		
			<!-- 상단 비주얼 -->
			<c:set var="mainBnr" value="${cornerMap[mainBnr]}"/>
			<c:if test="${mainBnr.totalCount > 0}">
			
				<div class="swiper_wrap">
					<div class="swiper-container brandSwiper_banner_1">
						<ul class="swiper-wrapper" id="mainBnr_ul">
							<c:forEach items="${mainBnr.dmsDisplayitems}" var="mainItem" varStatus="main_status">
								<c:choose>
									<c:when test="${isMobile eq 'true'}">
										<li class="swiper-slide">
											<a href="${mainItem.url2}">
												<img src="${_IMAGE_DOMAIN_}${mainItem.img2}" alt="" />
											</a>
										</li>
									</c:when>
									<c:otherwise>
										<li class="swiper-slide">
											<a href="${mainItem.url1}">
												<img src="${_IMAGE_DOMAIN_}${mainItem.img1}" alt="" />
											</a>
										</li>
									</c:otherwise>
								</c:choose>	
							</c:forEach>	
						</ul>

						<!-- Add Arrows -->
						<div class="swiper-button-next btn_tp5"></div>
						<div class="swiper-button-prev btn_tp5"></div>

						<!-- Add Pagination 모바일용-->
						<div class="pageInner_wrap">
							<div class="pageInner">
						       	<c:choose>
									<c:when test="${brandClassName eq 'brand2'}">	<!-- 궁중비책 -->
										<div class="swiper-pagination ${isMobile ne 'true' ? 'tp3' : ''}"></div>
									</c:when>
									<c:when test="${brandClassName eq 'brand3'}">	<!-- 토미티피 -->
										<div class="swiper-pagination ${isMobile ne 'true' ? 'tp4' : ''}"></div>
									</c:when>
									<c:when test="${brandClassName eq 'brand4'}">	<!-- 와이볼루션 -->
										<div class="swiper-pagination ${isMobile ne 'true' ? 'tp5' : ''}"></div>
									</c:when>
								</c:choose>
						        <button type="button" class="bnt_allView" onclick="ccs.layer.copyLayerToContentChild('ly_visual')">전체보기</button>
					        </div>
						</div>

					</div>
				</div>
			</c:if>
			
			<!-- mobile 브랜드 대카 -->
			<c:if test="${isMobile eq 'true'}">
				<ul class="brand_category mo_only" id="mo_brandCategory">
					<c:forEach items="${brandCategory}" var="category1" varStatus="status1">
						<li>
							<a>${category1.name}</a>
						</li>
					</c:forEach>
				</ul>
				<c:if test="${brandClassName == 'brand4'}">
					<ul class="expt_brand_category">
						<li>
							<a href="http://yvolution.co.kr/free-warranty/">정품 인증</a>
						</li>
					</ul>
				</c:if>
			</c:if>
	
			<c:set var="storyBnr2" value="${cornerMap[storyBnr2]}"/>
			<c:set var="videoBnr" value="${cornerMap[videoBnr]}"/>
			<c:set var="eventBnr" value="${cornerMap[eventBnr]}"/>
			<div class="bnrBrand_list">
				<ul>
					<c:if test="${not empty storyBnr2}">
						<li id="story_Bnr">	<!-- 브랜드 스토리 배너(1개) -->
							<c:choose>
								<c:when test="${isMobile eq 'true'}">
									<a>
										<img src="${_IMAGE_DOMAIN_}${storyBnr2.dmsDisplayitems[0].img2}" alt="${storyBnr2.dmsDisplayitems[0].text2}"/>
									</a>
								</c:when>
								<c:otherwise>
									<a href="javascript:brand.template.cornerItem('STORY');">
										<img src="${_IMAGE_DOMAIN_}${storyBnr2.dmsDisplayitems[0].img1}" alt="${storyBnr2.dmsDisplayitems[0].text1}" width="332px" height="192px"/>
									</a>
								</c:otherwise>
							</c:choose>
						</li>						
					</c:if>
					
					<c:if test="${isMobile ne 'true' && not empty videoBnr}">
						<li class="video" id="videoHtmlDiv">	<!-- 동영상(1개) -->
						
						</li>
						<div id="tempVideoHtml_pc" style="display:none;">
							${videoBnr.dmsDisplayitems[0].html1}
						</div>
					</c:if>
					
					<c:if test="${not empty eventBnr}">	<!-- 이벤트 배너(PC_1개/MO_최대 3개) -->
						<c:choose>
							<c:when test="${isMobile eq 'true'}">
								<c:forEach items="${eventBnr.dmsDisplayitems}" var="eventItem" varStatus="event_status">
									<li>						
										<a href="${eventItem.url2}">
											<img src="${_IMAGE_DOMAIN_}${eventItem.img2}" alt="${eventItem.text2}" />
										</a>
								</c:forEach>								
							</c:when>
							<c:otherwise>
								<li>
									<a href="${eventBnr.dmsDisplayitems[0].url1}">
										<img src="${_IMAGE_DOMAIN_}${eventBnr.dmsDisplayitems[0].img1}" alt="${eventBnr.dmsDisplayitems[0].text1}" width="332px" height="192px"/>
									</a>
								</li>
							</c:otherwise>
						</c:choose>
					</c:if>
					
				</ul>
			</div>
			
			<!-- 베스트 상품 -->
			<div class="brand_md rolling_box">
				<h3 class="tit_style2">베스트상품</h3>
				<div class="swiper_wrap">
					<div class="swiper-container prodSwiper product_type1 prodType_4ea block brandSwiper_realProdList_2">
						<ul class="swiper-wrapper" name="bestProduct">
						</ul>
						<div class="swiper-button-next btn_tp2"></div>
						<div class="swiper-button-prev btn_tp2"></div>
					</div>
				</div>
			</div>
						
			<!-- 추천상품 -->
			<c:set var="recommendBnr" value="${cornerMap[recommendBnr]}"/>
			<c:set var="recommendProduct" value="${cornerMap[recommendProduct]}"/>
			<c:if test="${not empty recommendBnr}">
				<div class="ss_collection">
					<h3 class="tit_style2">${recommendBnr.name}</h3>
					<div class="product_type1">
						<ul>
							<c:choose>
								<c:when test="${isMobile eq 'true'}">
									<c:if test="${not empty recommendBnr.dmsDisplayitems[0].img2}">	<!-- 코디 배너(1개) -->
										<li class="sscc_item01">
											<div>
												<div class="img">
													<a>
														<img src="${_IMAGE_DOMAIN_}${recommendBnr.dmsDisplayitems[0].img2}" alt="${recommendBnr.dmsDisplayitems[0].text2}" class="img_mc" />
													</a>
												</div>
											</div>
										</li>
									</c:if>
								</c:when>
								<c:otherwise>
									<c:if test="${not empty recommendBnr.dmsDisplayitems[0].img1}">
										<li class="sscc_item01">	<!-- 코디 배너(1개) -->
											<div>
												<div class="img">
													<a>
														<img src="${_IMAGE_DOMAIN_}${recommendBnr.dmsDisplayitems[0].img1}" alt="${recommendBnr.dmsDisplayitems[0].text1}" class="img_pc" style="height:680px;" />
													</a>
												</div>
											</div>
										</li>
									</c:if>
								</c:otherwise>
							</c:choose>
					
							<c:if test="${not empty recommendProduct.dmsDisplayitems}">	<!-- 코디 상품(4개) -->
								<c:forEach var="rec_product" items="${recommendProduct.dmsDisplayitems}" varStatus="rp_status">
									<c:if test="${rp_status.index < 4}">
										<c:set var="product" value="${rec_product.pmsProduct}" scope="request" />
										<c:set var="flagNone" value="style='display:none" scope="request" />
										<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp">
											<jsp:param name="type" value="brand"/>
											<jsp:param name="flagNone" value="style='display:none'"/>
											<jsp:param name="itemIndex" value="${rp_status.index + 2}"/>
										</jsp:include>
									</c:if>
								</c:forEach>
							</c:if>
							
						</ul>
					</div>
				</div>	
			</c:if>
			
			<!-- 동영상(1개) -->
			<c:if test="${isMobile eq 'true'}">
				<div class="bnrBrand_one video" style="border-top:1px solid #eee;">
					<c:if test="${not empty videoBnr}">
						<div id="videoHtmlDiv">
						
						</div>
						<div id="tempVideoHtml_mo" style="display:none;">
							${videoBnr.dmsDisplayitems[0].html2}
						</div>
					</c:if>
				</div>
			</c:if>
					
			
			<!-- 실시간 인기 상품 -->		
			<div class="brand_realtime rolling_box">
				<h3 class="tit_style2">실시간 인기 상품</h3>
				<div class="swiper_wrap">
					<div class="swiper-container prodSwiper product_type1 prodType_4ea block brandSwiper_realProdList_1">
						<ul class="swiper-wrapper" name="realtimeProduct">
						</ul>
						<div class="swiper-button-next btn_tp2"></div>
						<div class="swiper-button-prev btn_tp2"></div>
					</div>
				</div>
			</div>	

			<div class="brand_btm">
				<dl class="menuGo notice">
					<dt style="color:#fff; font-size:1.25em;">${isMobile ne 'true' ? '공지사항' : ''}</dt>
					<div id="noticeArea">
					
					</div>
				</dl>
				<div class="menuGo" onclick="ccs.link.go('/ccs/cs/qna/insert?brandId=${brandInfo.brandId}', CONST.NO_SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);">
					<a href="#" style="${isMobile ne 'true' ? 'color:#fff; font-size:1.25em;' : ''}">1:1 문의</a>
				</div>
				<div class="menuGo" onclick="brand.template.getFaq();">
					<a href="#" style="${isMobile ne 'true' ? 'color:#fff; font-size:1.25em;' : ''}">FAQ</a>
				</div>
				<div class="menuGo" onclick="brand.template.searchOffshop();">
					<a href="#" style="${isMobile ne 'true' ? 'color:#fff; font-size:1.25em;' : ''}">매장찾기</a>
				</div>
				
				<div class="cs_info" style="${isMobile eq 'true' ? 'padding-bottom:15px;' : ''}">
					<div>
						<strong class="mo_tel">${brandInfo.name} 고객센터</strong>
						<strong class="pc_tel">1588 - 7601</strong>
						<span>평일 09:00~18:00, 점심시간 11:30~12:30</span>
						<span>( 토/일요일 및 공휴일 휴무 )</span>
					</div>
				</div>
			</div>
		</div>
		
		<!-- 
		***************************************************************
		 * 브랜드 스토리
		*************************************************************** 
		-->
		<div class="swiper-slide inner brand_story" ${isMobile ne 'true'? 'style="display:none;"' : ''}>
		<%-- <div class="inner brand_story brandB_02" id="main2" data-index="2" data-type="brandB" ${isMobile ne 'true'? 'style="display:none;"' : ''}> --%>
			
			<c:set var="storyBnr" value="${cornerMap[storyBnr]}"/>
			<c:if test="${not empty storyBnr}">
				<div class="storyBox">				
					<c:choose>
						<c:when test="${isMobile eq 'true'}">
							${storyBnr.dmsDisplayitems[0].html2}
						</c:when>
						<c:otherwise>
							${storyBnr.dmsDisplayitems[0].html1}
						</c:otherwise>
					</c:choose>
				</div>		
			</c:if>
		</div>
		
		<!-- 
		***************************************************************
		 * PRODUCT
		*************************************************************** 
		-->	
		<div class="swiper-slide inner brand_products" ${isMobile ne 'true'? 'style="display:none;"' : ''}>
		<%-- <div class="inner brand_products brandB_03" id="main3" data-index="3" data-type="brandB" ${isMobile ne 'true'? 'style="display:none;"' : ''}> --%>
			<div class="brand_product">
				<c:set var="productBnr" value="${cornerMap[productBnr]}"/>
				
				<c:if test="${not empty productBnr}">
					<div class="productBox">
						<c:forEach items="${productBnr.dmsDisplayitems}" var="productItem" varStatus="product_status">
							<c:choose>
								<c:when test="${isMobile eq 'true'}">
									${productItem.html2}
								</c:when>
								<c:otherwise>
									${productItem.html1}
								</c:otherwise>
							</c:choose>
						</c:forEach>
					</div>			
				</c:if>
			</div>
		</div>
		
		<!-- 
		***************************************************************
		 * 이벤트 & 기획전
		*************************************************************** 
		-->
		<div class="swiper-slide inner brand_event" ${isMobile ne 'true'? 'style="display:none;"' : ''}>
		<%-- <div class="inner brand_event brandB_04" id="main4" data-index="4" data-type="brandB" ${isMobile ne 'true'? 'style="display:none;"' : ''}> --%>
		
		</div>
		
	</div>
</div>

<!-- 
***************************************************************
 * 팝업 : 전체보기
*************************************************************** 
-->
<div class="pop_wrap ly_visual">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3>전체보기</h3>
		</div>
		<div class="pop_content">
			<ul>
				<c:if test="${not empty mainBnr}">
					<c:forEach items="${mainBnr.dmsDisplayitems}" var="mainItem" varStatus="main_status">				
						<li>
							<a href="${mainItem.url2}">
								<img src="${_IMAGE_DOMAIN_}${mainItem.img2}" alt="" />
							</a>
						</li>
					</c:forEach>				
				</c:if>
			</ul>
		</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>

<!-- 
***************************************************************
 * 팝업 : 모바일 카테고리 선택
*************************************************************** 
-->
<div class="layer_style1 brand_menu">
	<div class="box brand_inner">
		<div class="conArt">
			<strong class="title">카테고리 선택</strong>
			<ul class="brand_category" id="layer_brandCategory">
				<c:forEach items="${brandCategory}" var="category1" varStatus="status2">
					<li name="brand_category1">
						<a href="javascript:brand.template.moChooseCate1('category1Link${status2.index}');" id="category1Link${status2.index}">${category1.name}</a>
						<ul> 
							<c:forEach items="${category1.dmsDisplaycategorys}" var="category2" varStatus="status3">
								<li>
									<a href="javascript:ccs.link.product.brandProdutList('CATEGORY','${brandInfo.brandId}','${category2.displayCategoryId}','${category1.displayCategoryId}');">- ${category2.name}</a>
								</li>
							</c:forEach>	
						</ul>
					</li>
				</c:forEach>
			</ul>
		</div>
		<button type="button" class="btn_close">레이어팝업 닫기</button>
	</div>
</div>

<!-- 
***************************************************************
 * 팝업 : 브랜드 FAQ
*************************************************************** 
-->
<div id="brandFaq_Area"></div>

<!-- <style>	** 꼭 적용되어야 하는 CSS. common.css/mobile.css에 없으면 추가해주세요.
	.pc .brand2 .brand_btm .menuGo {background:#55bec0;}
	.pc .brand3 .brand_btm .menuGo {background:#4d4d4f;}
	.pc .brand4 .brand_btm .menuGo {background:#231f20;}
	
	.mobile .mainCon .brand_home .expt_brand_category li>a {display:block; position:relative; height:40px; padding:0 20px; color:#333; font-size:14px; text-align:center; line-height:38px; border-bottom:1px dotted #ddd;}
	.mobile .mainCon .brand_home .expt_brand_category li>a:after{content:""; display:inline-block; position:absolute; right:20px; top:50%; margin-top:-5px; width:6px; height:10px; background:url(/resources/img/mobile/bg/bg_brand_menu.png) no-repeat 0 0; background-size:cover;}
</style> -->