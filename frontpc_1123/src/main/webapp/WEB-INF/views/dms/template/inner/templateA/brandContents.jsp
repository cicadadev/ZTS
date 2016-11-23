<%--
	화면명 : 브랜드관 > 템플릿A
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	pageContext.setAttribute("mainBnr", Config.getString("corner.brand.templA.main") );
	pageContext.setAttribute("lookbookBnr", Config.getString("corner.brand.templA.lookbook") );
	pageContext.setAttribute("videoBnr", Config.getString("corner.brand.templA.video") );
	pageContext.setAttribute("coordiBnr", Config.getString("corner.brand.templA.coordi") );
	pageContext.setAttribute("coordiProduct", Config.getString("corner.brand.templA.coordi.product") );	
	pageContext.setAttribute("storyBnr", Config.getString("corner.brand.templA.story") );
	pageContext.setAttribute("styleBnr", Config.getString("corner.brand.templA.style") );
	pageContext.setAttribute("eventBnr", Config.getString("corner.brand.templA.event") );
%>

<script type="text/javascript" src="/resources/js/common/common.ui.js"></script>
<script type="text/javascript" src="/resources/js/mms/mms.mypage.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	// BO에서 등록한 HTML 제대로 그리기 위해 꼭 필요!
	brand.template.appendHtml();
	
	// 실시간 인기상품
	pms.brand.realtimeProduct('${brandInfo.erpBrandId}');
	
	// 카탈로그/코디북 조회
	if(ccs.common.mobilecheck()) {
			brand.template.itemSearch('STYLE');	// 스타일 조회
			brand.template.itemSearch('EVENT');	// 이벤트&기획전 조회
		
		if (common.isEmpty($("#brandForm #directCatalogYn").val())) {
			brand.template.itemSearch('CATALOGUE');	// 카탈로그/코디북 조회
		}
	}

	// 브랜드 공지사항
	custcenter.notice.brand($("#hid_brandId").val());
	
	// 모바일. 메인 룩북배너 클릭 시, 룩북 리스트 화면으로 바로 이동
	$(".mobile #lookbook_Bnr").on("click", function() {
		mainSwiper_body.slideTo(3, 300);

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
	
	// 스타일 이용안내 팝업 추가(16.11.21)
	$(".mobile .btn_styleHow").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .ly_styleHow") );
		}
	});

});
</script>

<div class="mainCon brandType ${brandClassName} brandA_01">
		<div class="swiper-wrapper allSwiper" id="mainSwiperWrapper">
			<!-- 
			***************************************************************
			 * 메인
			*************************************************************** 
			-->
			<div class="swiper-slide inner brand_home">
			<%-- <div class="inner brand_home brandA_01" id="main1" data-index="1" data-type="brandA" ${isMobile ne 'true'? 'style="display:block;"' : ''}> --%>
			
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
										<c:when test="${brandClassName eq 'brand1'}">	<!-- 알로앤루 -->
											<div class="swiper-pagination ${isMobile ne 'true' ? 'tp2' : ''}"></div>
										</c:when>
										<c:when test="${brandClassName eq 'brand5'}">	<!-- 섀르반 -->
											<div class="swiper-pagination ${isMobile ne 'true' ? 'tp6' : ''}"></div>
										</c:when>
										<c:when test="${brandClassName eq 'brand6'}">	<!-- 알퐁소 -->
											<div class="swiper-pagination ${isMobile ne 'true' ? 'tp7' : ''}"></div>
										</c:when>
										<c:when test="${brandClassName eq 'brand7'}">	<!-- 츄즈 -->
											<div class="swiper-pagination ${isMobile ne 'true' ? 'tp8' : ''}"></div>
										</c:when>
										<c:when test="${brandClassName eq 'brand8'}">	<!-- 포래즈 -->
											<div class="swiper-pagination ${isMobile ne 'true' ? 'tp9' : ''}"></div>
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
				</c:if>
		
				<div class="bnrBrand_list">
					<c:set var="lookbookBnr" value="${cornerMap[lookbookBnr]}"/>
					<c:set var="videoBnr" value="${cornerMap[videoBnr]}"/>
					<c:set var="eventBnr" value="${cornerMap[eventBnr]}"/>
					
					<ul>
						<c:if test="${not empty lookbookBnr}">
							<li id="lookbook_Bnr">	<!-- LOOKBOOK 배너(1개) -->
								<c:choose>
									<c:when test="${isMobile eq 'true'}">
										<a>
											<img src="${_IMAGE_DOMAIN_}${lookbookBnr.dmsDisplayitems[0].img2}" alt="${lookbookBnr.dmsDisplayitems[0].text2}"/>
										</a>
									</c:when>
									<c:otherwise>
										<a href="javascript:brand.template.cornerItem('CATALOGUE');">
											<img src="${_IMAGE_DOMAIN_}${lookbookBnr.dmsDisplayitems[0].img1}" alt="${lookbookBnr.dmsDisplayitems[0].text1}" width="332px" height="192px"/>
										</a>
									</c:otherwise>
								</c:choose>
							</li>
						</c:if>
						
						<c:if test="${not empty videoBnr}">
							<li class="video" id="videoHtmlDiv">	<!-- 동영상(1개) -->
							
							</li>
							<c:choose>
								<c:when test="${isMobile eq 'true'}">
									<div id="tempVideoHtml_mo" style="display:none;">
										${videoBnr.dmsDisplayitems[0].html2}
									</div>
								</c:when>
								<c:otherwise>
									<div id="tempVideoHtml_pc" style="display:none;">
										${videoBnr.dmsDisplayitems[0].html1}
									</div>
								</c:otherwise>
							</c:choose>
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
		
				<c:set var="coordiBnr" value="${cornerMap[coordiBnr]}"/>
				<c:set var="coordiProduct" value="${cornerMap[coordiProduct]}"/>
				<c:if test="${not empty coordiBnr}">
					<div class="ss_collection">
						<h3 class="tit_style2">${coordiBnr.name}</h3>
						<div class="product_type1">
							<ul>
								<c:choose>
									<c:when test="${isMobile eq 'true'}">
										<c:if test="${not empty coordiBnr.dmsDisplayitems[0].img2}">	<!-- 코디 배너(1개) -->
											<li class="sscc_item01">
												<div>
													<div class="img">
														<a>
															<img src="${_IMAGE_DOMAIN_}${coordiBnr.dmsDisplayitems[0].img2}" alt="${coordiBnr.dmsDisplayitems[0].text2}" class="img_mc" />
														</a>
													</div>
												</div>
											</li>
										</c:if>
									</c:when>
									<c:otherwise>
										<c:if test="${not empty coordiBnr.dmsDisplayitems[0].img1}">	<!-- 코디 배너(1개) -->
											<li class="sscc_item01">
												<div>
													<div class="img">
														<a>
															<img src="${_IMAGE_DOMAIN_}${coordiBnr.dmsDisplayitems[0].img1}" alt="${coordiBnr.dmsDisplayitems[0].text1}" class="img_pc" style="height:680px;" />
														</a>
													</div>
												</div>
											</li>
										</c:if>
									</c:otherwise>
								</c:choose>
								
								<c:if test="${not empty coordiProduct.dmsDisplayitems}">		<!-- 코디 상품(4개) -->
									<c:forEach var="coordi_product" items="${coordiProduct.dmsDisplayitems}" varStatus="cp_status">
										<c:if test="${cp_status.index < 4}">
											<c:set var="product" value="${coordi_product.pmsProduct}" scope="request" />
											<c:set var="flagNone" value="style='display:none" scope="request" />
											<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp">
												<jsp:param name="type" value="brand"/>
												<jsp:param name="flagNone" value="style='display:none'"/>
												<jsp:param name="itemIndex" value="${cp_status.index + 2}"/>
											</jsp:include>
										</c:if>
									</c:forEach>
								</c:if>
								
							</ul>
						</div>
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
							<c:choose>
								<c:when test="${brandClassName eq 'brand7'}">
									<strong class="pc_tel">1588 - 7601</strong>	<!-- 츄즈는 템플릿A 쓰지만 전화번호는 템플릿B 따라감 -->
								</c:when>
								<c:otherwise>
									<strong class="pc_tel">080 - 740 - 3100</strong>
								</c:otherwise>
							</c:choose>
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
			<%-- <div class="inner brand_story brandA_02" id="main2" data-index="2" data-type="brandA" ${isMobile ne 'true'? 'style="display:none;"' : ''}> --%>
			
				<c:set var="storyBnr" value="${cornerMap[storyBnr]}"/>
				<c:if test="${not empty storyBnr}">
					<div class="storyBox">				
						<c:choose>
							<c:when test="${isMobile}">
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
			 * LOOKBOOK & COORDIBOOK
			*************************************************************** 
			-->
			<div class="swiper-slide inner brand_lookbook brandA_03" ${isMobile ne 'true'? 'style="display:none;"' : ''}>
			<%-- <div class="inner brand_lookbook brandA_03" id="main3" data-index="3" data-type="brandA" ${isMobile ne 'true'? 'style="display:none;"' : ''}> --%>
				
				<div id="lookbook_list" class="lookbook">
				
				</div>
				<div id="lookbook_detail" style="display:none;">
				
				</div>
			</div>
		
			<!--
			***************************************************************
			 * 스타일
			*************************************************************** 
			-->
			<div class="swiper-slide inner brand_style brandA_04" ${isMobile ne 'true'? 'style="display:none;"' : ''}>
			<%-- <div class="inner brand_style brandA_04" id="main4" data-index="4" data-type="brandA" ${isMobile ne 'true'? 'style="display:none;"' : ''}> --%>
			
				<!-- 상단 비주얼 -->
				<c:choose>
					<c:when test="${isApp eq 'true' && loginId != null && loginId != ''}">
						<div class="btn_wrapC btn2ea mo_only">
							<a href="#none" class="btn_sStyle3 sWhite1 btn_styleHow">이용안내</a>
							<a href="javascript:mypage.style.makeAndModifyStyle('', '${loginId}');" class="btn_sStyle3 sPurple1 btn_makeStyle"><span>스타일 만들기</span></a>
						</div>
					</c:when>
					<c:otherwise>
						<div class="btn_wrapC btn1ea mo_only">
							<a href="#none" class="btn_sStyle3 sWhite1 btn_styleHow">이용안내</a>
						</div>
					</c:otherwise>
				</c:choose>
			
				<c:if test="${isMobile ne 'true'}">
					<c:set var="styleBnr" value="${cornerMap[styleBnr]}"/>
					<c:if test="${not empty styleBnr}">
						<div class="visual">
							<div class="styleBanner">
								<img src="${_IMAGE_DOMAIN_}${styleBnr.dmsDisplayitems[0].img1}" alt="${styleBnr.dmsDisplayitems[0].text1}" />
							</div>
						</div>
					</c:if>
		
					<div class="step styleshopHow pc_only">
						<h3 class="slide_tit1 slideHide"><span class="evt_tit">스타일샵은 어떻게 이용하나요?</span></h3>
						<div class="stepCont">
							<ul class="notice">
								<li>스타일 만들기는 APP에서 가능합니다.</li>
								<li>마음에 드는 회원의 코디에 좋아요를 눌러서 공감해주세요.</li>
								<li>완료된 스타일은 친구, 지인에게 SNS로 공유하여 추천해보아요.</li>
							</ul>
	
							<ul class="stepTxt">
								<li>
									<span class="step01">Step 01</span>
									<strong>스타일 만들기</strong>
									<em>월령, 성별로 대상 모델을<br />선택하여 주세요.</em>
								</li>
								<li>
									<span class="step02">Step 02</span>
									<strong>아이템 가져오기</strong>
									<em>카테고리, 선호하는 브랜드, 꾸미기 등 다양한 분류로 마음에 드는 아이템을 선택하여 코디해보세요.</em>
								</li>
								<li>
									<span class="step03">Step 03</span>
									<strong>등록하기</strong>
									<em>코디팁을 작성하여 등록합니다.<br />상세분류 지정이 가능합니다. 강조하고 싶은 코디 내용을 작성하여 공유해보세요.</em>
								</li>
								<li>
									<span class="step04">Step 04</span>
									<strong>혜택받기</strong>
									<em>나만의 코디를 만들어 공유하고 이를 통해 고객판매가 이루어질 시 혜택을 드립니다. 다양한 이벤트, 기획전에 참여해 보세요. 선물이 기다립니다.</em>
								</li>
							</ul>
						</div>
					</div>
				</c:if>
				
				<div id="styleArea"></div>
				
				<form name="styleForm" id="styleForm">
					<input type="hidden" value="" name="genderTypeCd" id="genderTypeCd" />
					<input type="hidden" value="" name="genderTypeName" id="genderTypeName" />
					<input type="hidden" value="" name="themeCd" id="themeCd" />
					<input type="hidden" value="" name="themeName" id="themeName" />
					<input type="hidden" value="" name="sortKeyword" id="sortKeyword" />
					<input type="hidden" value="" name="sortKeywordName" id="sortKeywordName" />
				</form>
			</div>
			
			<!-- 
			***************************************************************
			 * 이벤트 & 기획전
			*************************************************************** 
			-->
			<div class="swiper-slide inner brand_event" ${isMobile ne 'true'? 'style="display:none;"' : ''}>
			<%-- <div class="inner brand_event brandA_05" id="main5" data-index="5" data-type="brandA" ${isMobile ne 'true'? 'style="display:none;"' : ''}> --%>
			
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

<!-- 
***************************************************************
 * 팝업 : 스타일샵 안내
*************************************************************** 
-->
<div class="layer_style1 ly_styleHow">
	<div class="box">
		<div class="conArt">
			<strong class="title">스타일샵 이용방법</strong>
			<div class="stepImg"><img src="/resources/img/mobile/txt/styleshop_step.jpg" alt="스타일샵 이용방법" /></div>
			<ul class="notice">
				<li>마음에 드는 회원의 코디에 좋아요를 눌러서 공감해주세요.</li>
				<li>완료된 스타일은 친구, 지인에게 SNS로 공유하여 추천해보아요.</li>
			</ul>
		</div>
		<button type="button" class="btn_x btn_close">닫기</button>
	</div>
</div>

<!-- <style>	** 꼭 적용되어야 하는 CSS. common.css에 없으면 추가해주세요.
	.pc .brand1 .brand_btm .menuGo {background:#c51123;}
	.pc .brand5 .brand_btm .menuGo {background:#88ac2e;}
	.pc .brand6 .brand_btm .menuGo {background:#323e48;}
	.pc .brand7 .brand_btm .menuGo {background:#01b2b4;}
	.pc .brand8 .brand_btm .menuGo {background:#1c2b39;}
	
	.pc .brand1 .brand_style .slide_tit1 {border-bottom:1px solid #c51123;}
	.pc .brand5 .brand_style .slide_tit1 {border-bottom:1px solid #88ac2e;}
	.pc .brand6 .brand_style .slide_tit1 {border-bottom:1px solid #323e48;}
	.pc .brand7 .brand_style .slide_tit1 {border-bottom:1px solid #01b2aa;}
	.pc .brand8 .brand_style .slide_tit1 {border-bottom:1px solid #1c2b39;}
</style> -->