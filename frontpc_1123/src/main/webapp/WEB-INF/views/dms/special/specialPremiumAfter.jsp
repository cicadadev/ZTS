<%--
	화면명 : 프론트 & 모바일 프리미엄 멤버십관 인증 후
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="전문관|프리미엄멤버십관" name="pageNavi"/>
</jsp:include>

<c:if test="${isMobile}">
	<script type="text/javascript" src="/resources/js/common/display.ui.mo.js"></script>
</c:if>

<script type="text/javascript" src="/resources/js/jquery.countdown.min.js"></script>
<script type="text/javascript">
	$(document).ready(function(){
		$('.rw_certifyMemBox').removeClass('premium_before');
		$('.rw_certifyMemBox').addClass('premium_after');
	
		$('div.sortBoxList > ul > li').hide();
		$('.productSort').show();
		$('.listType').show();

		if(ccs.common.mobilecheck()) {
			swiperCon('premium_after_1depth', 5); //배너
		} else {
// 			swiperCon('premium_after_1depth', 400, $(".tabBox > li").length, 0, false, true); //배너
			swiperCon('premium_after_1depth', 400, 5, 0, false, true); //배너
		}
		
	});
	
	function premiumInfo() {
		if('${isMobile}') {
			ccs.go_url('http://m.maeili.com/mMaeilIFront/front/premembership/premembership.maeili');
		} else {
			ccs.go_url('http://family.maeili.com/index.jsp');
		}
	}
	
	function benefitInfo() {
		ccs.link.go('/sps/event/benefit', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);
	}
	
	if(ccs.common.mobilecheck()) {
		/* 모바일 스크롤 제어*/
		$(window).bind("scroll", productListScrollListener);
		
		function productListScrollListener() {
			var rowCount = $(".product_type1").find("li").length;
			var totalCount = Number($("[name=totalCount]").val());
			
			var scrollTop = $(window, document).scrollTop();
			var scrollHeight = $(document).height() - $(window).height();
			
			if (scrollTop >= scrollHeight - 200 && scrollHeight != 0) {
				if(rowCount !=0 && (rowCount < totalCount)){

					console.log($("#tempLoadingBar").length);
					
					if ($("#tempLoadingBar").length > 0 ) {
						return;
					}
					special.premium.loadDepthProduct(null, true); 
				}
			}
		}

		/* 셀렉트박스 : 2016.07.26 추가 */
		function fnSelectStyle(){
			$(".select_box1 select").each(function() {
				$(this).siblings('label').text( $("option:selected", this).text() );
			});

			$(".wrap .content").off("change").on("change", ".select_box1 select", function() {
				$(this).siblings('label').text( $("option:selected", this).text() );
			});
		}
		fnSelectStyle();
		
// 		function setEvent() {
// 			if (!$(".product_type1").eq(0).hasClass("block")) {
// 				$(".product_type1").each(function(index) {
					
// 					if (index != 0) {
// 						$(this).toggleClass("block");
// 					}
// 				});
// 			}
// 		}
	}
	
</script>

<div>
	<input type="hidden" id="dept1_Id" name="upperDealGroupNo" value=""/>
	<input type="hidden" id="dept2_Id" name="dealGroupNo" value=""/>
	<input type="hidden" id="sortType" name="sortType" value="${productSortTypeCd }"/>
	<input type="hidden" name="totalCount" value="${totalCount}"/>
	<input type="hidden" name="currentPage" value="1" />
</div>

<div class="inner">
	<div class="rw_certifyMemBox premium_after">
		
		<div class="memVisual pc_only">
			<div class="boxInner">
				<div class="memImgWrap">
					<div class="memImgL">
						<h3 class="premium_tit">
							<c:choose>
								<c:when test="${isMobile}">
									<img src="/resources/img/mobile/txt/txt_certificationAfter03.png" alt="매일프리미엄 멤버십 전용 - 프리미엄 멤버십관" class="img_mc">
								</c:when>
								<c:otherwise>
									<img src="/resources/img/pc/txt/txt_certificationAfter03.png" alt="매일프리미엄 멤버십 전용 - 프리미엄 멤버십관" class="img_pc">
								</c:otherwise>
							</c:choose>
						</h3>
					</div>
					<dl class="memImgC">
						<dt>프리미엄 멤버십 혜택안내</dt>
						<dd>
							<ul class="info_list">
								<li>차별화된 서비스! 푸짐한 혜택!</li>
								<li>프리미엄 멤버십 회원전용 서비스!</li>
							</ul>
						</dd>
					</dl>
					<div class="memImgR">
						<a href="javascript:void(0);" class="btn" onclick="premiumInfo();">프리미엄 멤버십 안내</a><br>
						<a href="javascript:void(0);" class="btn mt10" onclick="benefitInfo();">0to7.com 혜택안내</a>
					</div>
				</div>
			</div>
		</div>
		
		<!-- mo만 적용 -->
		<div class="visual mo_only">
			<div class="img_outer">
				<p><img src="/resources/img/mobile/txt/bnr_premium.jpg" alt="매일프리미엄 멤버십 전용 프리미엄 멤버십관 차별화된 서비스! 푸짐한 혜택! 프리미엄 멤버십 회원전용 서비스" /></p>
				<div class="chance_box">
					<a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.benefit.membership();">
						<img src="/resources/img/mobile/txt/bnr_premium2.jpg" alt="0to7.com 회원 혜택 받기" />
					</a>
				</div>
			</div>
		</div>
				
		<div class="tabFixW">
			<div class="tabFix">
			<div class="swiper_wrap">
				<div class="tab_outer swiper-container premium_after_1depth">
					<ul class="tabBox swiper-wrapper">
						<li class="first swiper-slide on">
							<a href="javascript:void(0);" class="theme1 on" id="all" onclick="javascript:special.premium.depthClick(this)">전체</a></li>
						</li>
						<c:forEach var="depth1" items="${depthList }" varStatus="status">
							<li class="swiper-slide">
								<a href="javascript:void(0);" class="premium0${status.index+1 }" name="${depth1.name }" onclick="javascript:special.premium.depthClick(this, '${depth1.dealGroupNo}', '', '${status.index }','${fn:length(depth1.spsDealgroups) }');">${depth1.name }</a>
							</li>
						</c:forEach>
					</ul>
					<c:if test="${!isMobile }">
<!-- 						Add Arrows -->
						<div class="swiper-button-next btn_tp6"></div>
						<div class="swiper-button-prev btn_tp6"></div>
					</c:if>
				</div>
			</div>
				
			</div>
		</div>

		<!-- mo 정렬버튼 -->
		<div class="tit_style3 mo_only">
			<div class="sortBoxList sort_1ea">
				<ul>
					<%-- 1depth 테마 selectbox --%>
					<c:forEach var="depth1" items="${depthList }" varStatus="status">
						<c:set var="moIndex" value="${status.index }" />
						<li class="selectBox_${depth1.dealGroupNo}" style="display: block;">
							<div class="select_box1">
								<label></label>
								<select class="selBox_${depth1.dealGroupNo}" onchange="javascript:special.premium.moSelect('${depth1.dealGroupNo}',this.value)">
									<option value="" selected="selected">전체</option>
									<c:forEach var="depth2" items="${depth1.spsDealgroups }" varStatus="status2">
										<option value="${depth2.dealGroupNo}">${depth2.name }</option>
									</c:forEach>
								</select>
							</div>
						</li>
					</c:forEach>
				
					<%-- 상품 정렬 selectbox --%>
					<li class="productSort">
						<div class="select_box1">
							<label></label>
							<tags:codeList code="PRODUCT_SORT_CD" var="productSort" tagYn="N"/>
							<select class="productSortType" onchange="javascript:special.premium.sortType(this.value)">
								<c:forEach items="${productSort}" var="sort">
									<option value="${sort.cd}">${sort.name}</option>
								</c:forEach>
							</select>
						</div>
					</li>
					<li class="listType"><button type="button" class="btnListType list">블록형 / 리스트형</button></li>			
				</ul>
			</div>
		</div>
		<!-- //mo 정렬버튼 -->
			
		<!-- pc 미니탭 -->
		<c:if test="${ !isMobile }">
			<c:forEach var="depth1" items="${depthList }" varStatus="status">
				<ul class="miniTabBox1 pc_only" id="subtab${status.index }" style="display:none;">
					<c:forEach var="depth2" items="${depth1.spsDealgroups }" varStatus="status2">
						<li><div>
							<a href="#anchor_${depth2.dealGroupNo }" onclick="javascript:special.premium.depthClick(this, '${depth1.dealGroupNo}', '${depth2.dealGroupNo}', '${status.index }')">${depth2.name }</a>
						</div></li>
					</c:forEach>
				</ul>
			</c:forEach>
		</c:if>
		<!-- //pc 미니탭 -->
		
		<div class="certifyMem pc_only">
			<div class="swiper_wrap">
				<div class="visual  type1 swiper-container premiumMember_afterBanner_1"  style="display: none;">
					<ul class="vImg swiper-wrapper">
						<c:forEach var="depth1" items="${depthList }" varStatus="status">
							<c:set var="moIndex" value="${status.index }" />
							<c:forEach var="depth2" items="${depth1.spsDealgroups }" varStatus="status2">
								<c:if test="${not empty depth2.img }">
									<li class="selectMa_${depth1.dealGroupNo}_sub_${depth2.dealGroupNo}" style="display: none;">
										<img src="${_IMAGE_DOMAIN_}${depth2.img}"/>
									</li>
								</c:if>
							</c:forEach>
						</c:forEach>
					</ul>
				</div>
			</div>
		</div>
		
		<%-- 상품영역 --%>
		<c:choose>
			<c:when test="${isMobile }">
				<div class="list_group">
					<h4 id="title1" class="tit_style1 pc_only">
						<span class="depthName"></span>
					</h4>
					<div class="product_type1 prodType_4ea block">
						<ul class="productSize${status.index }" id="moProductArea">
							<c:forEach var="product" items="${productList}" varStatus="i">
								<c:set var="product" value="${product}" scope="request" />
								<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
									<jsp:param name="type" value="premium" />
									<jsp:param name="certify" value="true" />
									<jsp:param name="dealProductIndex" value="${i.index }" />
								</jsp:include>
							</c:forEach>
						</ul>
					</div>
				</div>
			</c:when>
			
			<c:otherwise>
				<div id="pcConentHere">
					<div class="list_group">
						<h4 id="title1" class="tit_style1 pc_only">
							<span class="depthName"></span>
						</h4>
						<div class="product_type1 prodType_4ea block">
							<ul class="productSize${status.index }">
								<c:forEach var="product" items="${productList}" varStatus="i">
									<c:set var="product" value="${product}" scope="request" />
									<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
										<jsp:param name="type" value="premium" />
										<jsp:param name="certify" value="true" />
										<jsp:param name="dealProductIndex" value="${i.index }" />
									</jsp:include>
								</c:forEach>
							</ul>
						</div>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
		<%-- // 상품영역 --%>
		
	</div>
</div>
