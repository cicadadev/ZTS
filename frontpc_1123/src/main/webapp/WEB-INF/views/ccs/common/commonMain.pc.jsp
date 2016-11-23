<%--
	화면명 : 프론트 PC 메인
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ page import="gcp.common.util.FoSessionUtil" %>
<%
	//상단 배너
	pageContext.setAttribute("mainBnr1", Config.getString("corner.main.banner.img.1") );
	pageContext.setAttribute("mainBnr2", Config.getString("corner.main.banner.img.2") );
	pageContext.setAttribute("mainBnr3", Config.getString("corner.main.banner.img.3") );
	pageContext.setAttribute("mainBnr4", Config.getString("corner.main.banner.img.4") );
	pageContext.setAttribute("hotissue5", Config.getString("corner.main.banner.img.5") );
	
	//아이템 카테고리 
	pageContext.setAttribute("itemBnr1", Config.getString("corner.main.ctg.img.1") );
	pageContext.setAttribute("itemBnr2", Config.getString("corner.main.ctg.img.2") );
	pageContext.setAttribute("itemBnr3", Config.getString("corner.main.ctg.img.3") );
	pageContext.setAttribute("itemBnr4", Config.getString("corner.main.ctg.img.4") );
	
	//추천MD 코너
	pageContext.setAttribute("mdPrd", Config.getString("corner.main.md.product.2") );
	pageContext.setAttribute("mdImgBanner", Config.getString("corner.main.md.img.1") );
	
	//추천브랜드 코너
	pageContext.setAttribute("brandText", Config.getString("corner.main.brand.text.1") );
	
	//시즌추천상품 
	pageContext.setAttribute("season1", Config.getString("corner.main.season.img.1") );
	pageContext.setAttribute("season2", Config.getString("corner.main.season.product.1") );
	
	//추천이벤트
	pageContext.setAttribute("event1", Config.getString("corner.main.event.img.1") );
	pageContext.setAttribute("event2", Config.getString("corner.main.event.img.2") );
	
	//브랜드코드
	pageContext.setAttribute("tommeetippe", Config.getString("brand.tommeetippe.code"));
	pageContext.setAttribute("allo", Config.getString("brand.allo.code"));
	pageContext.setAttribute("alfonso", Config.getString("brand.alfonso.code"));
	pageContext.setAttribute("fourlads", Config.getString("brand.fourlads.code"));
	pageContext.setAttribute("skarbarn", Config.getString("brand.skarbarn.code"));
	pageContext.setAttribute("royal", Config.getString("brand.royal.code"));
	pageContext.setAttribute("chooze", Config.getString("brand.chooze.code"));
	pageContext.setAttribute("yvol", Config.getString("brand.yvol.code"));
	
	pageContext.setAttribute("yvol", Config.getString("brand.yvol.code"));
	
	pageContext.setAttribute("babyGenderCd", FoSessionUtil.getRecobellBabyGenderCd());
	pageContext.setAttribute("babyMonthCd", FoSessionUtil.getRecobellBabyMonthCd());
	
	
%>
<script type="text/javascript" src="/resources/js/jquery.countdown.min.js"></script>
<script type="text/javascript" src="/resources/js/common/display.ui.pc.js"></script>

<script>
$(document).ready(function(){
	// 로그인 팝업 띄우기
	if('${login}'=='Y'){
		var url = "${returnUrl}";
		ccs.link.login({returnUrl:url});
	}
	
	var recobellParam = {recType:'p001', size : 12, pcid:'${RB_PCID}'};
	pms.common.getRecommendationProductList($('[name=clickProduct]'), recobellParam, function(){
		swiperCon('mainSwiper_recoProducts_1', 400, 4, 12, false, true, 4);   //이런상품 어때
	});
	
	dms.main.makeParam('');
	
	swiperCon('mainSwiper_brand', 400, 6, 4,'',true); //브랜드 배너
	swiperCon('mainSwiper_recoProducts_1', 800, 4, 12, false, true, 4);                      //추천 상품
	swiperCon('mainSwiper_recoProducts_2', 800, 5, 12, false, true, 5);//MD 특가 상품
	swiperCon('mainSwiper_popularItem.marketPicUp', 800, 2, 12, false, true, 2, 'fraction'); // 매장 픽업
	swiperCon('mainSwiper_popularItem.giftShop', 800, 2, 12, false, true, 2, 'fraction'); //기프트샵상품
	
	swiperCon('mainSwiper_bigBanner_2', 400, 1, 0, 2000, true);
	swiperCon('mainSwiper_bigBanner_1', 400, 1, 0, 2000, true); 

});
</script>
	
	<div class="mainCon">
		<div class="swiper-wrapper">
			<div class="inner main1">
				<div class="main">
					<!-- ### 메인 비주얼 ### -->
					<div class="main_visual">
						<div class="category">
							<dl>
								<dt>아이템별</dt>
								<dd>
									<ul>
										<c:forEach var="depth1" items="${ctgList}" varStatus="">
											<li>
												<a href="#none"><c:out value="${depth1.name}"/></a>
		
												<div class="subCate">
													<strong><c:out value="${depth1.name}"/></strong>
													<ul>
														<c:forEach var="depth2" items="${depth1.dmsDisplaycategorys}">
															<li>
																<a href="javascript:ccs.link.display.dispTemplate('${depth2.displayCategoryId}');"><c:out value="${depth2.name}"/></a>
															</li>
														</c:forEach>
													</ul>
													
													<div class="bnr">
															<c:if test="${depth1.displayCategoryId =='10001' }">
																<c:set var="itemBnr1"  value="${cornerMap[itemBnr1]}"/>
																<a href="${itemBnr1.dmsDisplayitems['0'].url1}">
																	<img src="${_IMAGE_DOMAIN_}${itemBnr1.dmsDisplayitems['0'].img1}" alt="${itemBnr1.dmsDisplayitems['0'].img1}" />
																</a>
															</c:if>
															<c:if test="${depth1.displayCategoryId =='10002' }">
																<c:set var="itemBnr2"  value="${cornerMap[itemBnr2]}"/>
																<a href="${itemBnr2.dmsDisplayitems['0'].url1}">
																	<img src="${_IMAGE_DOMAIN_}${itemBnr2.dmsDisplayitems['0'].img1}" alt="${itemBnr2.dmsDisplayitems['0'].img1}" />
																</a>
															</c:if>
															<c:if test="${depth1.displayCategoryId =='10003' }">
																<c:set var="itemBnr3"  value="${cornerMap[itemBnr3]}"/>
																	<a href="${itemBnr3.dmsDisplayitems['0'].url1}">
																<img src="${_IMAGE_DOMAIN_}${itemBnr3.dmsDisplayitems['0'].img1}" alt="${itemBnr3.dmsDisplayitems['0'].img1}" />
																</a>
															</c:if>
															<c:if test="${depth1.displayCategoryId =='10004' }">
																<c:set var="itemBnr4"  value="${cornerMap[itemBnr4]}"/>
																<a href="${itemBnr4.dmsDisplayitems['0'].url1}">
																	<img src="${_IMAGE_DOMAIN_}${itemBnr4.dmsDisplayitems['0'].img1}" alt="${itemBnr4.dmsDisplayitems['0'].img1}" />
																</a>
															</c:if>	
														
													</div>
													<!-- <a href="#none" class="subCate_close">닫기</a> roy 11.01 닫기버튼 삭제-->
												</div>
											</li>
										</c:forEach>
									</ul>
								</dd>
							</dl>
	
							<c:if test="${not empty ageCodeList}">
								<dl>
									<dt>월령별</dt>
									<dd>
										<ul>
											<c:forEach var="ageCodeList" items="${ageCodeList}">
												<c:set var="code" value="${fn:split(ageCodeList.cd,'.')[1]}" />
												<li>
													<a href="/dms/display/ageShop?ageCode=${code}"><c:out value="${ageCodeList.name}"/></a>
												</li>
											</c:forEach>
										</ul>
									</dd>
								</dl>
							</c:if>
						</div>
						
						<c:set var="hotMap"  value="${cornerMap[hotissue5]}"/>
						<c:set var="main1Map"  value="${cornerMap[mainBnr1]}"/>
						<c:set var="main2Map"  value="${cornerMap[mainBnr2]}"/>
						<c:set var="main3Map"  value="${cornerMap[mainBnr3]}"/>
						<c:set var="main4Map"  value="${cornerMap[mainBnr4]}"/>
						
						<ul class="slide">
							<li class="on">
								<span>
									구매하신 분께 핫 신상 에코백을 증정합니다!
								</span>
								<a href="#none">
									<img src="" />
								</a>
							</li>
						</ul>
	
						<div class="control">
							<a href="#none" class="btn_prev">이전</a>
							<ol>
								<c:if test="${not empty hotMap.dmsDisplayitems}">
									<li class="on">
										<span>핫이슈</span>
										<ul>
											<c:forEach var="item1" items="${hotMap.dmsDisplayitems}" varStatus="status">
												<li ${status.count ==1?'class="on"':"" }>
													<a href="${_IMAGE_DOMAIN_}${item1.img1}" alt="${item1.img1}" data-color="${item1.addValue}" data-url="${item1.url1}">${status.count}</a>
												</li>
											</c:forEach>
										</ul>
									</li>
								</c:if>
								<c:if test="${not empty main1Map.dmsDisplayitems}">
									<li>
										<span>식품/분유</span>
										<ul>
											<c:forEach var="item2" items="${main1Map.dmsDisplayitems}" varStatus="status">
												<li>
													<a href="${_IMAGE_DOMAIN_}${item2.img1}" alt="${item2.img1}" data-color="${item2.addValue}" data-url="${item2.url1}">${status.count}</a>
												</li>
											</c:forEach>
										</ul>
									</li>
								</c:if>
								
								<c:if test="${not empty main2Map.dmsDisplayitems}">
									<li>
										<span>물티슈/기저귀</span>
										<ul>
											<c:forEach var="item3" items="${main2Map.dmsDisplayitems}" varStatus="status">
												<li>
													<a href="${_IMAGE_DOMAIN_}${item3.img1}" alt="${item3.img1}" data-color="${item3.addValue}" data-url="${item3.url1}">${status.count}</a>
												</li>
											</c:forEach>
										</ul>
									</li>
								</c:if>
								
								<c:if test="${not empty main3Map.dmsDisplayitems}">
									<li>
										<span>의류/잡화</span>
										<ul>
											<c:forEach var="item4" items="${main3Map.dmsDisplayitems}" varStatus="status">
												<li>
													<a href="${_IMAGE_DOMAIN_}${item4.img1}" alt="${item4.img1}" data-color="${item4.addValue}" data-url="${item4.url1}">${status.count}</a>
												</li>
											</c:forEach>
										</ul>
									</li>
								</c:if>
								
								<c:if test="${not empty main4Map.dmsDisplayitems}">
									<li>
										<span>용품/완구/화장품</span>
										<ul>
											<c:forEach var="item5" items="${main4Map.dmsDisplayitems}" varStatus="status">
												<li>
													<a href="${_IMAGE_DOMAIN_}${item5.img1}" alt="${item5.img1}" data-color="${item5.addValue}" data-url="${item5.url1}">${status.count}</a>
												</li>
											</c:forEach>
										</ul>
									</li>
								</c:if>
							</ol>
							<a href="#none" class="btn_next">다음</a>
						</div>
					</div>
					<!-- ### //메인 비주얼 ### -->
	
					<!-- ### 브랜드 : 2016.09.30 수정 ### -->
					<c:if test="${not empty cornerMap[brandText].dmsDisplayitems}">
					<c:set var="brandMap"  value="${cornerMap[brandText]}"/>
						<div class="brandBox swiper_wrap">
							<div class="swiper-container mainSwiper_brand">
								<ul class="swiper-wrapper">
									<c:forEach var="item" items="${brandMap.dmsDisplayitems}" varStatus="status">
										<li class="swiper-slide">
											<a href="${item.url1}">
												<img src="${_IMAGE_DOMAIN_}${item.img1}" alt="" />
											</a>
										</li>
									</c:forEach>
								</ul>
								<!-- Add Arrows -->
						        <div class="swiper-button-next btn_tp1"></div>
						        <div class="swiper-button-prev btn_tp1"></div>
							</div>
						</div>
					</c:if>	
					
					
					<!-- ### //브랜드 ### -->
	
					<!-- ### 쇼킹제로 ### -->
					<c:if test="${not empty shockingProducts}">
						<div class="zeroBox">
							<div class="titWrap">
								<h2>
									<img src="/resources/img/pc/txt/main_zero.png" alt="쇼킹제로" />
								</h2>
								<a href="javascript:ccs.link.display.shockingzero();" class="btn_more">더보기</a>
							</div>
							<div class="product_type1 prodType_3ea">
								<ul>
									<c:forEach items="${shockingProducts}" var="dealProduct" varStatus="status">
										<c:set var="dealProduct" value="${dealProduct}" scope="request" />
										<c:set var="product" value="${dealProduct.pmsProduct}" scope="request" />
										<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
											<jsp:param value="${search.firstRow + status.index}" name="dealProductIndex"/>
											<jsp:param name="type" value="shocking" />
										</jsp:include>
									</c:forEach>
								</ul>
							</div>
						</div>
							
					</c:if>
					<!-- ### 쇼킹제로 ### -->
					
					<!-- ### 이런상품어때? ### -->
					<div class="howAboutBox">
						<div class="rolling_box">
							<c:choose>
								<c:when test="${loginId != '' && loginId != null}">
									<!-- 로그인 후 -->
									<h2 class="tit_style2">${loginName}님! 이런 상품 어떠세요?</h2>
									<!-- 로그인 후 -->
								</c:when>
								<c:otherwise>
									<!-- 로그인 전 -->
									<h2 class="tit_style2">이런 상품 어떠세요?</h2>													
									<!-- 로그인 전 -->
								</c:otherwise>		
							</c:choose>
							<div class="swiper_wrap">
								<div class="swiper-container product_type1 prodType_4ea block mainSwiper_recoProducts_1">
									<ul class="swiper-wrapper" name="clickProduct"> 
									
									</ul>
									<!-- Add Arrows -->
									<div class="swiper-button-next btn_tp2"></div>
									<div class="swiper-button-prev btn_tp2"></div>
								</div>
									
							</div>
						</div>
					</div>
					<!-- ### //이런상품어때? ### -->
					
					<!-- ### MD상품 ### -->
					<c:if test="${not empty cornerMap[mdPrd].dmsDisplayitems}">
						<c:set var="mdPrdMap"  value="${cornerMap[mdPrd]}"/>
						<div class="MdBox">
							<div class="rolling_box">
								<div class="mTitWrap">
									<h2 class="tit_style2">MD 특가</h2><!-- mod : 2016.10.09 텍스트변경 begin --><!-- mod_roy : 2016.10.21 텍스트변경 begin -->
									<span class="txt_style1"><c:out value="${mdPrdMap.name}"/></span><!-- add : 2016.10.11 begin --><!-- mod_roy : 2016.10.21 텍스트변경 begin -->
								</div>
								<div class="swiper_wrap">
									<div class="swiper-container product_type1 prodType_5ea block mainSwiper_recoProducts_2">
										<ul  class="swiper-wrapper">
											<c:forEach var="item" items="${mdPrdMap.dmsDisplayitems}" varStatus="status">
												<c:set var="product" value="${item.pmsProduct}" scope="request" />
												<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
													<jsp:param name="swipe" value="Y"/>
												</jsp:include>
											</c:forEach>
										</ul>
										<c:if test="${ mdPrdMap.totalCount gt 5}">
											<!-- Add Arrows -->
											<div class="swiper-button-next btn_tp3"></div>
											<div class="swiper-button-prev btn_tp3"></div>
										</c:if>
									</div>
								</div>
							</div>
							
							
							<c:if test="${not empty cornerMap[mdImgBanner]}">
								<c:set var="mdImagMap"  value="${cornerMap[mdImgBanner]}"/>
								<c:if test="${not empty mdImagMap.dmsDisplayitems[0].img1}">
									<div class="bg_MdBox" style="background-image:url(${_IMAGE_DOMAIN_}${mdImagMap.dmsDisplayitems[0].img1});"></div><!-- add : 2016.10.11 begin --><!-- mod_roy : 2016.10.21 텍스트변경 begin -->
								</c:if>
							</c:if>
						</div>
					</c:if>
					
					<!-- ### //MD상품 ### -->
	
					<!-- ### 계절상품 ### -->
					<c:if test="${not empty cornerMap[season1].dmsDisplayitems and not empty cornerMap[season2].dmsDisplayitems}">
						<div class="seasonBox">
							<div class="rolling_box">
								<h2 class="tit_style2"><c:out value="${cornerMap[season1].name}"/></h2>
		
								<div class="product_type1 prodType_5ea block">
									
									<c:if test="${not empty cornerMap[season1].dmsDisplayitems}">
										<c:set var="season1Map"  value="${cornerMap[season1]}"/>
										<div class="collection">
											<a href="${season1Map.dmsDisplayitems['0'].url1}">
												<img src="${_IMAGE_DOMAIN_}${season1Map.dmsDisplayitems['0'].img1}" alt="" />
											</a>
										</div>
									</c:if>
		
									<c:if test="${not empty cornerMap[season2].dmsDisplayitems}">
										<c:set var="season2Map"  value="${cornerMap[season2]}"/>
										<ul>
											<c:forEach var="item" items="${season2Map.dmsDisplayitems}" varStatus="status">
												<c:set var="product" value="${item.pmsProduct}" scope="request" />
												<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false"/>
											</c:forEach>
										</ul>
									</c:if>
								</div>
		
								<!-- <div class="paginate">
									<button class="prev">이전</button><span><em class="current">1</em>/<span class="total">0</span></span><button class="next">다음</button>
								</div> -->
							</div>
						</div> 
					</c:if>
					
					<!-- ### //계절상품 ### -->
	
					<!-- ### 힛트다 히트! ### -->
					<div class="hitBox">
						<h2 class="tit_hit">
							<img src="/resources/img/pc/txt/hit.png" alt="" />
							<b>실시간 인기상품</b>
							<a href="javascript:ccs.link.display.bestShop();" class="btn_more">더보기</a>
						</h2>
						<div class="tab_outer srchType">
							<ul class="tabBox ea5">
								<li class="on">
									<a href="javascript:dms.main.makeParam('')" class="on theme1">전체</a>
								</li>
								
								<c:forEach var="depth1" items="${ctgList}" varStatus="status">
									<c:if test="${depth1.leafYn eq 'N'}">
										<li class="">
											<a href="javascript:dms.main.makeParam('${depth1.displayCategoryId}')" class="category_${depth1.displayCategoryId}"><c:out value="${depth1.name}"/></a>
										</li>
									</c:if>
								</c:forEach>
							</ul>
						</div>
	
						<div class="product_type1 prodType_5ea block" id="bestListArea">
							<!-- <ul id="bestListArea"></ul> -->
						</div>
					</div>
					<!-- ### //힛트다 히트! ### -->
	
					<!-- ### 인기상품 ### -->
					<div class="bestBox">
						<c:if test="${pickupTotal gt 0 }">
							<div class="column rolling_box"> <!-- 16.10.01 : class(rolling_box) 추가 -->
								<strong><em class="iconPickup">매장픽업</em> 인기상품</strong>
		
								<div class="swiper_wrap">
									<div class="product_type1 prodType_2ea">
										 <div class="swiper-container mainSwiper_popularItem marketPicUp">
											<ul class="swiper-wrapper">
												<c:forEach var="product" items="${pickup}" varStatus="status">
													<c:set var="product" value="${product}" scope="request" />
													<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
														<jsp:param name="type" value="pickup" />
														<jsp:param name="swipe" value="Y"/>
													</jsp:include>
												</c:forEach>
											</ul>
											
											<!-- Add Pagination -->
											<div class="swiper-pagination"></div>
	
											<!-- Add Arrows -->
											<div class="swiper-button-next btn_tp4"></div>
											<div class="swiper-button-prev btn_tp4"></div>
																			 
										 </div>
									</div>
								</div>
								
							</div>
						</c:if> 
						
						<c:if test="${giftTotal gt 0 }">
							<div class="column rolling_box">
								<strong><em class="iconGift">기프트샵</em> 인기상품</strong>
									<div class="swiper_wrap">
										<div class="product_type1 prodType_2ea">
											<div class="swiper-container mainSwiper_popularItem giftShop">
												<ul class="swiper-wrapper">
													<c:forEach var="product" items="${giftList}" varStatus="status">
														<c:set var="product" value="${product}" scope="request" />
														<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false" >
															<jsp:param name="swipe" value="Y"/>
														</jsp:include>
													</c:forEach>
												</ul>
							
												<!-- Add Pagination -->
 												<div class="swiper-pagination"></div>

												<!-- Add Arrows -->
												<div class="swiper-button-next btn_tp4"></div>
												<div class="swiper-button-prev btn_tp4"></div>
												
											</div>
										</div>
									</div>
							</div>
						</c:if>
						
					</div>
					<!-- ### //인기상품 ### -->
	
					<!-- mod : 2016.10.09 banner수정 begin -->
					<div class="bnr_area">
						<c:if test="${not empty cornerMap[event1].dmsDisplayitems}">
						<c:set var="event1Map"  value="${cornerMap[event1]}"/>
							<div class="rolling_banner bnrLeft swiper_wrap">
								<div class="swiper-container mainSwiper_bigBanner_1">
									<ul class="swiper-wrapper">
										<c:forEach var="item" items="${event1Map.dmsDisplayitems}" varStatus="status">
											<li ${i==1? 'class="on"' : ''}>
												<a href="${item.url1}">
													<img src="${_IMAGE_DOMAIN_}${item.img1}" alt="">
												</a>
											</li>
									</c:forEach>
									</ul>
									<!-- Add Pagination -->
										<div class="swiper-pagination tp1"></div>
								</div>
							</div>
						</c:if>
	
						<c:if test="${not empty cornerMap[event2].dmsDisplayitems}">
						<c:set var="event2Map"  value="${cornerMap[event2]}"/>
							<div class="rolling_banner bnrRight swiper_wrap">
								<div class="swiper-container mainSwiper_bigBanner_2">
									<ul class="swiper-wrapper">
										<c:forEach var="item" items="${event2Map.dmsDisplayitems}" varStatus="status">
											<li class="swiper-slide ${i==1? 'on' : ''}">
												<a href="#none">
													<img src="${_IMAGE_DOMAIN_}${item.img1}" alt="" />
												</a>
											</li>
										</c:forEach>
									</ul>
									<!-- Add Pagination -->
										<div class="swiper-pagination tp1"></div>
								</div>
							</div>
						</c:if>
					</div>
					<!-- // mod : 2016.10.09 banner수정 end -->
							
					<div class="mainAll_btm">
						<dl class="menuGo">
							<dt>공지사항</dt>
							<dd>
								<span><a href="#none">
									<c:out value="${noticeList['0'].title}"/></a></span>
								<a href="/ccs/cs/notice/list" class="btn_moreView">더보기</a>
							</dd>
						</dl>
	
						<div class="menuGoBox">
							<dl class="menuGo_01">
								<dt>전문관</dt>
								<dd>
									<ul>
										<!-- mod : 2016.10.09 텍스트변경 begin -->
										<li><a href="javascript:ccs.link.special.subscription();">정기배송관</a></li>
										<li><a href="javascript:ccs.link.special.pickup();">매장픽업관</a></li>
										<li><a href="javascript:ccs.link.special.giftShop();">기프트샵</a></li>
										<li><a href="javascript:ccs.link.special.milkPowder();">분유관</a></li>
										<li><a href="javascript:ccs.link.special.birthready();">출산용품관</a></li>
										<li><a href="javascript:ccs.link.special.multiChildrenInfo();">다자녀우대관</a></li>
										<c:if test="${ employeeYn eq 'Y'}">
											<li>
												<a href="javascript:ccs.link.special.employee();">임직원관</a>
											</li>
										</c:if>
										<!-- // mod : 2016.10.09 텍스트변경 end -->
									</ul>
								</dd>
							</dl>
							<dl class="menuGo_02">
								<dt>제휴몰</dt>
								<dd>
									<ul>
										<li><a href="">	YES24</a></li>
										<li><a href="">알라딘</a></li>
									</ul>
								</dd>
							</dl>
							<dl class="menuGo_03">
								<dt>브랜드</dt>
								<dd>
									<ul>
										<li><a href="javascript:brand.template.main('${allo}');">알로앤루</a></li>
										<li><a href="javascript:brand.template.main('${alfonso}');">알퐁소</a></li>
										<li><a href="javascript:brand.template.main('${fourlads}');">포래즈</a></li>
										<li><a href="javascript:brand.template.main('${skarbarn}');">섀르반</a></li>
										<li><a href="javascript:brand.template.main('${royal}');">궁중비책</a></li>
										<li><a href="javascript:brand.template.main('${tommeetippe}');">토미티피</a></li>
										<li><a href="javascript:brand.template.main('${chooze}');">츄즈</a></li>
										<li><a href="javascript:brand.template.main('${yvol}');">Y볼루션</a></li>
									</ul>
								</dd>
							</dl>
							<dl class="menuGo_04">
								<dt>이벤트&amp;혜택</dt>
								<dd>
									<ul>
										<li><a href="javascript:ccs.link.display.event();">진행중인 이벤트</a></li>
										<li><a href="javascript:ccs.link.go('/sps/event/benefit', CONST.NO_SSL, CONST.LOGIN_CHECK_TYPE_NONE);">맴버십 혜택</a></li>
										<li><a href="javascript:ccs.link.go('/sps/event/card?cno=1', CONST.NO_SSL, CONST.LOGIN_CHECK_TYPE_NONE);">제휴카드혜택</a></li>
									</ul>
								</dd>
							</dl>
							<dl class="menuGo_05">
								<dt>고객센터</dt>
								<dd>
									<ul>
										<li>
											<span>1588-8744</span>
											<p>평일 09:00~18:00<br />점심 11:30~12:30<br />휴무 토/일요일 및 공휴일</p>
											<a href="javascript:ccs.link.custcenter.qna.go();" class="btnQnA">1:1 문의</a>
										</li>
									</ul>
								</dd>
							</dl>
						</div>
					</div>
					<!-- //16.10.01 -->
				</div>
			</div>
		</div>
	</div>
