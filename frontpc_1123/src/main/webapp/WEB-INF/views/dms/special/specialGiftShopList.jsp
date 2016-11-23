<%--
	화면명 : 프론트 & 모바일  기프트샵
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<script type="text/javascript" src="/resources/js/common/display.ui.pc.js"></script>
<%
	pageContext.setAttribute("giftshopCouponId", Config.getString("giftshop.coupon.id") );
%>


<script type="text/javascript">

$(document).ready(function(){
	if(ccs.common.mobilecheck()) {
		swiperCon('mainSwiper_category', $(".swiper-wrapper > li").length); //배너
	}
	
	$(".mobile .btn_giftHow").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .ly_giftHow") );
		}
	});
	
	function fnLayerPosition(target_pop) {
		$(target_pop).show();
		$(target_pop).height( $(document).height() );

		var base_top = ($(window).height() - $(" > .box", target_pop).innerHeight()) / 2;
		$(" > .box", target_pop).css({"marginTop" : base_top + $(window).scrollTop() + "px"});
	}
	
	$(".evt_tit").off("click").on("click", function(e){
		$(this).parent().toggleClass("slideHide");
	});
});

function changeGubunTitle(param) {
	$(".miniTabBox1").each(function() {
		$(this).find("a").removeClass("on");
	});
	$(param).addClass("on");
}

if (ccs.common.mobilecheck()) {
	$(window).bind("scroll", productListScrollListener);
	
	function productListScrollListener() {
		var rowCount = $(".product_type1").find("li").length;
		var totalCount = Number($("[name=totalCount]").val());
		var maxPage = Math.ceil(totalCount/10);
		
		var scrollTop = $(window, document).scrollTop();
		var scrollHeight = $(document).height() - $(window).height();
		
		if (scrollTop >= scrollHeight - 200 && scrollHeight != 0) {
			if(rowCount !=0 && (rowCount < totalCount)){
				
				if ($("#lodingBar").length > 0 ) {
					return;
				}
				special.giftShop.getAllGiftshopProductList('all'); 
			}
		}
	}
}


function setEvent() {
	if (!$("#productList_div").find(".product_type1").eq(0).hasClass("block")) {
		$("#productList_div").find(".product_type1").each(function(index) {
			
			if (index != 0) {
				$(this).toggleClass("block");
			}
		});
	}
}

function loginCheck() {
	mms.common.loginCheck(function(isLogin){
		if (isLogin) {
			special.giftShop.receiveCoupon('${giftshopCouponId}');
		}
	});
}

$(document).ready(function() {
	$("#hidCurrentPage").val(0);
	if ($(".tabBox > li").length < 7) {
		$(".tabNavi_btns").css("display", "none");
	}
	
	$(".mobile .btnListType").off("click").on("click", function(){
		$(this).toggleClass("block");
		$(".product_type1").toggleClass("block");
	});
	
	
	if ($("#hidCurrentDisplayType").val() != 'all') {
		
		$(".tabBox > li").each(function() {
			$(this).removeClass("on");
		});
		
		if ($("#hidCurrentThemeCd").val() != '') {
			$("li[id='"+$("#hidCurrentThemeCd").val()+"']").addClass("on");
			special.giftShop.changeTab('theme', $("#hidCurrentThemeCd").val());
		}
		if ($("#hidCurrentDisplayId").val() != '') {
			$("#"+$("#hidCurrentDisplayId").val()).addClass("on");
			special.giftShop.getAllGiftshopProductList('corner', $("#hidCurrentDisplayId").val());
		}
	} else {
		special.giftShop.getAllGiftshopProductList('all', "");
		
	}
	
});

</script>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="전문관|기프트샵" name="pageNavi"/>
</jsp:include>

	<div class="inner">
		<div class="pro_box pro_giftBox">
			<!-- 16.10.05 : 상단 비주얼 -->
			<div class="visual one">
				<ul class="">
  					<c:forEach items="${keyVisualBanner}" var="banner" varStatus="i">
						<c:forEach items="${banner.dmsDisplayitems}" var="item" varStatus="j">
							<c:choose>
								<c:when test="${isMobile}">
									<li class="on">
										<span class="bg" style="background-color:#7f00d0;">기프트샵</span>
										<div class="img_outer">
											${item.html2}
											<div class="chance_box">
												<img src="/resources/img/pc/temp/giftshopCouponBanner.jpg" alt="선물포장 서비스 쿠폰을 드립니다. 쿠폰받기">
												<a href="javascript:loginCheck();" class="btn_chance">쿠폰받기</a>
											</div>
										</div>
									</li>
								</c:when>
								<c:otherwise>
									<li class="on">
										<span class="bg" style="background-color:#7f00d0;">기프트샵</span>
										<div class="img_outer">
											${item.html1}
											<a href="javascript:loginCheck();" class="link">선물포장 무료 쿠폰증정 알로앤루, 알퐁소, 포래즈, 섀르반 상품 구매시 사용가능</a>
										</div>
									</li>
								</c:otherwise>
							</c:choose>	
						</c:forEach>
					</c:forEach>
				</ul>
			</div>
			<!-- //16.10.05 : 상단 비주얼 -->

			<div class="step pickupHow">
				<h3 class="slide_tit1 slideHide"><span class="evt_tit">선물하기 어떻게 이용하나요?</span></h3>
				<div class="stepCont">
					<p>
						선물 결제만하면 끝! 옵션선택과 배송지는 선물받는 사람이 직접!
					</p>
					<ul class="stepTxt">
						<li>
							<span class="step01">Step 01</span>
							<strong>상품선택</strong>
							<em class="alignC">아이연령, 성별, 가격대에 맞는 상품을 <br>선택해주세요.</em>
						</li>
						<li>
							<span class="step02">Step 02</span>
							<strong>상품주문</strong>
							<em class="alignC">주문과 함께 선물 받는 분께 <br>보낼 메시지를 작성해주세요.</em>
						</li>
						<li>
							<span class="step03">Step 03</span>
							<strong>선물받은 사람 메시지 확인</strong>
							<em class="alignC">선물메시지를 통해 본인인증 후 옵션선택 및 배송 받을 주소지를 입력해주세요.</em>
						</li>
						<li>
							<span class="step04">Step 04</span>
							<strong>선물배송</strong>
							<em class="alignC">입력하신 배송지로 선물이 배송됩니다.</em>
						</li>
					</ul>
				</div>
			</div>
			
			<c:if test="${isMobile}">
			<div class="tabFixW">
				<div class="tabFix">
			</c:if>
			
					<div class="tab_outer swiper-container mainSwiper_category">
						<ul class="tabBox swiper-wrapper">
							<li class="swiper-slide on" id="all">
								<a href="#none" onclick="javascript:special.giftShop.changeTab('all');" class="theme1 on">전체</a>
							</li>
							<c:forEach items="${seasonPrdConerList}" var="season" varStatus="J">
								<c:if test="${not empty season.dmsDisplayitems}">
									<li id="${season.displayId}">
										<a href="#none" onclick="javascript:special.giftShop.changeTab('corner','${season.displayId}');" class="theme6">${season.name}</a>
									</li>
								</c:if>
							</c:forEach>
							
							<tags:codeList code="THEME_CD" var="themeList" tagYn="N"/>
							<c:forEach items="${themeList}" var="theme" varStatus="i">
								<li id="${theme.cd}">
									<a href="#none" onclick="javascript:special.giftShop.changeTab('theme','${theme.cd}');" class="theme${i.index+7}">${theme.name}</a>
								</li>
							</c:forEach>
						</ul>
						<div class="tabNavi_btns">
							<a href="#none" class="prev">이전</a>
							<a href="#none" class="next on">다음</a>
						</div>
					</div>
			
					<div class="btnSrchDetail">
						<a href="#none">맞춤정보 상세검색 <span>열기</span></a>
					</div>
				
				<c:if test="${isMobile}">
					</div>
				</div>
			</c:if>
					
					<!-- pc 상세 옵션 -->
					<c:if test="${!isMobile}">
						<div class="optionBox detailOptBox"> <!-- 16.10.05 : class(detailOptBox) 추가 -->
							<div class="optionItemBox open">
								<div class="optionItem optDetail age">
									<div class="optionTit"><strong>월령</strong></div>
									<div class="optionCont">
										<ul class="txtList" id="PC_AGE_UL">
											<tags:codeList code="AGE_TYPE_CD" var="ageTypeCd" tagYn="N"/>
											<c:forEach items="${ageTypeCd}" var="age" varStatus="i">
												<li>
													<label class="chk_style1 option_style1">
														<em>
															<input type="checkbox" value="${age.cd}" name="${age.cd}">
														</em>
														<span>${age.name}</span>
													</label>
												</li>
											</c:forEach>
										</ul>
									</div>
								</div>
			
								<div class="optionItem optDetail gender">
									<div class="optionTit"><strong>성별</strong></div>
									<div class="optionCont">
										<ul class="txtList" id="PC_GENDER_UL">
											<tags:codeList code="GENDER_TYPE_CD" var="genderTypeCd" tagYn="N"/>
											<c:forEach items="${genderTypeCd}" var="gender" varStatus="i">
												<li>
													<label class="radio_style1 option_style1">
														<em>
															<c:choose>
																<c:when test="${i.first}">
																	<input type="radio" name="${gender.cdGroupCd}" value="${gender.cd}" checked="checked" />
																</c:when>
																<c:otherwise>
																	<input type="radio" name="${gender.cdGroupCd}" value="${gender.cd}"/>
																</c:otherwise>
															</c:choose>
														</em>
														<span>${gender.name}</span>
													</label>
												</li>
											</c:forEach>
										</ul>
									</div>
								</div>
			
								<div class="optionItem optDetail price">
									<div class="optionTit"><strong>가격</strong></div>
									<div class="optionCont">
										<input type="text" name="pc_minPrice" value="" class="inputTxt_style1" onkeyup="this.value=this.value.replace(/[^0-9]/g,'')"/>
										<span class="swung">~</span>
										<input type="text" name="pc_maxPrice" value="" class="inputTxt_style1" onkeyup="this.value=this.value.replace(/[^0-9]/g,'')"/>
									</div>
								</div>
			
								<div class="btnBox">
									<a href="#none" onclick="javascript:special.giftShop.detailSearch('pc');" class="btn_sStyle4 sPurple1">검색</a>
								</div>
			
								<button type="button" class="btnMore">전체목록 보기</button>
							</div>
							<a href="#none" class="btnDetailClose">닫기</a>
						</div>
					</c:if>
					<!-- //pc 상세 옵션 -->

					<!-- mo 상세 옵션 -->
					<c:if test="${isMobile}">
						<div class="mo_optionSrch">
							<div class="slide_tit1 slideHide">
								<a href="#none" class="evt_tit">맞춤정보 상세검색</a>
							</div>
			<!-- 				<form id="searchForm"> -->
								<div class="optionBox">
									<div class="optionItemBox">
										<div class="optionItem optDetail age">
											<div class="optionTit"><strong>월령</strong></div>
											<div class="optionCont">
												<ul class="txtList" id="MO_AGE_UL">
													<tags:codeList code="AGE_TYPE_CD" var="ageTypeCd" tagYn="N"/>
													<c:forEach items="${ageTypeCd}" var="age" varStatus="i">
														<li>
															<label class="chk_style1 option_style1">
																<em>
																	<input type="checkbox" value="${age.cd}" name="${age.cd}">
																</em>
																<span>${age.name}</span>
															</label>
														</li>
													</c:forEach>
												</ul>
											</div>
										</div>
				
										<div class="optionItem optDetail gender">
											<div class="optionTit"><strong>성별</strong></div>
											<div class="optionCont">
												<ul class="txtList" id="MO_GENDER_UL">
													<tags:codeList code="GENDER_TYPE_CD" var="genderTypeCd" tagYn="N"/>
													<c:forEach items="${genderTypeCd}" var="gender" varStatus="i">
														<li>
															<label class="radio_style1 option_style1">
																<em>
																	<c:choose>
																		<c:when test="${i.first}">
																			<input type="radio" name="${gender.cdGroupCd}" value="${gender.cd}" checked="checked" />
																		</c:when>
																		<c:otherwise>
																			<input type="radio" name="${gender.cdGroupCd}" value="${gender.cd}"/>
																		</c:otherwise>
																	</c:choose>
																</em>
																<span>${gender.name}</span>
															</label>
														</li>
													</c:forEach>
												</ul>
											</div>
										</div>
				
										<div class="optionItem optDetail price">
											<div class="optionTit"><strong>가격</strong></div>
											<div class="optionCont">
												<input type="text" class="inputTxt_style1" name="mo_minPrice" value="" onkeyup="this.value=this.value.replace(/[^0-9]/g,'')"/>
												<span class="swung">~</span>
												<input type="text" class="inputTxt_style1" name="mo_maxPrice" value="" onkeyup="this.value=this.value.replace(/[^0-9]/g,'')"/>
											</div>
										</div>
				
										<div class="btnBox">
											<a href="#none" onclick="javascript:special.giftShop.detailSearch('mo');" class="btn_sStyle4 sPurple1">검색</a>
										</div>
									</div>
								</div>
							
							
			<!-- 				</form> -->
						</div>
					
					</c:if>
					<!-- //mo 상세 옵션 -->
			
			
					<!-- mo 정렬버튼 -->
					
					<div class="tit_style3 mo_only">
						<div class="sortBoxList sort_1ea">
							<ul>
								<li>
									<div class="select_box1">
										<label></label>
										<tags:codeList code="PRODUCT_SORT_CD" var="productSort" tagYn="N"/>
										<select onchange="javascript:special.giftShop.changeSort(this);" id="select_sort">
											<c:forEach items="${productSort}" var="sort">
												<c:if test="${sort.cd eq search.sort}">
													<option value="${sort.cd}" selected="selected">${sort.name}</option>
												</c:if>
												<c:if test="${sort.cd ne search.sort}">
													<option value="${sort.cd}">${sort.name}</option>
												</c:if>
											</c:forEach>
										</select>
									</div>
								</li>
								<li>
									<button type="button" class="btnListType list">블록형 / 리스트형</button>
								</li>
							</ul>
						</div>
					</div>
					<!-- //mo 정렬버튼 -->
			<div id="productList_div">
				
			</div>
		</div>
	</div>
	
	<form name="giftForm" id="giftForm">
		<input type="hidden" name="currentPage" id="hidCurrentPage" value="0" />
		<input type="hidden" name="minPrice" id="hidMinPrice" value="" />
		<input type="hidden" name="maxPrice" id="hidMaxPrice" value="" />
		<input type="hidden" name="ageTypeCds" id="hidAgeTypeCds" value="" />
		<input type="hidden" name="genderTypeCd" id="hidGenderTypeCd" value="" />
		<input type="hidden" name="currentDisplayId" id="hidCurrentDisplayId" value="" />
		<input type="hidden" name="currentThemeCd" id="hidCurrentThemeCd" value="" />
		<input type="hidden" name="currentDisplayType" id="hidCurrentDisplayType" value="all" />
	</form>
	
	
	<div class="layer_style1 ly_giftHow" style="display: none; height: 3175px;">
		<div class="box" style="margin-top: 60.5px;">
			<div class="conArt">
				<strong class="title">선물하기 이용방법</strong>
				<div class="stepImg"><img src="/resources/img/mobile/txt/step_gift.jpg" alt="선물하기 이용방법"></div>
			</div>
			<button type="button" class="btn_x btn_close">닫기</button>
		</div>
	</div>
	
	<div class="layer_style1 sLayer_chance" style="display: none; height: 3175px;">
		<div class="box" style="margin-top: 137px;">
			<div class="conArt">
				<strong class="title">선물포장 무료찬스!</strong>

				<div class="conBox">
					<p><strong>선물포장 쿠폰이 발행되었습니다.</strong></p>
					<p>알로앤루, 알퐁소, 포래즈, 섀르반 상품 구매 시 사용 가능하며 여러개 주문 및 부피 초과시 추가비용이 발생할 수 있습니다.</p>
					<span>쿠폰은 GIFT SHOP에서 매월 ID당 1장 제공됩니다</span>
				</div>
			</div>
			<div class="btn_wrapC btn1ea">
				<a href="#none" class="btn_mStyle1 sPurple1 btn_close">확인</a>
			</div>

			<button type="button" class="btn_close">레이어팝업 닫기</button>
		</div>
	</div>
