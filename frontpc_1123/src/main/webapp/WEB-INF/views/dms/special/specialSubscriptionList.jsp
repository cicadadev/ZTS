<%--
	화면명 : 정기배송관
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script type="text/javascript" src="/resources/js/common/display.ui.pc.js"></script>

<script type="text/javascript">

$(document).ready(function(){
	if (ccs.common.mobilecheck()) {
		swiperCon('mainSwiper_category', $(".swiper-wrapper > li").length); //배너
	}
	special.subscription.init();
	
	$(".mobile .btn_rgHow").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .ly_regularHow") );
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

function setEvent() {
	if (ccs.common.mobilecheck()) {
		$(".mobile .btnListType").off("click").on("click", function(){
			var listLength;
			var target_tag;
	
			if( $(this).closest(".tit_style3").length ){
				// 제목 태그에 감싸여 있을 경우
				listLength = $(this).closest(".tit_style3").nextUntil(".list_group").length;
				target_tag = $(this).closest(".tit_style3");
			}else{
				// 일반 형제태그로 있을 경우
				listLength = $(this).closest(".sortBoxList").nextUntil(".list_group").length;
				target_tag = $(this).closest(".sortBoxList");
			}
			$(this).toggleClass("block");
	
			if(listLength == 0){
				$(target_tag).next(".list_group").find(".product_type1").toggleClass("block");
			} else{
				$(target_tag).nextUntil(".list_group").next(".list_group").find(".product_type1").toggleClass("block");
			}
		});
	}
	
}
</script>

	<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
		<jsp:param value="전문관|정기배송관" name="pageNavi"/>
	</jsp:include>

	<div class="inner">
		<div class="special rg_delivery">
			<!-- 상단 비주얼 -->
			<div class="visual one">
				<ul>
					<c:forEach var="corner" items="${cornerList}" varStatus="i">
						<c:choose>
							<c:when test="${i.first}">
								<li class="on">
							</c:when>
							<c:otherwise>
								<li>
							</c:otherwise>
						</c:choose>
						<c:forEach var="item" items="${corner.dmsDisplayitems}" varStatus="j">
							<c:choose>
								<c:when test="${isMobile}">
									<div class="img_outer">
										${item.html2}
									</div>		
								</c:when>
								<c:otherwise>
									<span class="bg" style="background-color:#eb6900;">정기배송관</span>
									<div class="img_outer">
										${item.html1}
									</div>
								</c:otherwise>
							</c:choose>		
						</c:forEach>
						</li>					
					</c:forEach>
				</ul>
			</div>
			<!-- // 상단 비주얼 -->

			<div class="step regularHow">
				<h3 class="slide_tit1 slideHide"><span class="evt_tit">정기배송 어떻게 이용하나요?</span></h3>
				<div class="stepCont">
					<ul class="notice">
						<li>정기배송은 자주 구매하는 상품을 배송주기, 횟수, 요일을 선택해서 주문해 두면,  지정한 날에 자동결제, 배송되는 서비스입니다.</li>
						<li>정기배송 신청 시 배송일 3일전 오전에 결제 예정 안내 문자가  발송되고,  해당일  16시 이후 자동결제가 진행됩니다.</li>
						<li>상품가격은 결제 시점 상품가격이 반영되므로, 주문시 금액과 실 결제금액이 상이할 수 있습니다.</li>
						<li>주문취소 및 신청사항에 대한 변경은 자동결제 진행 전까지 가능하며, 이후에는 고객센터 1588-8744로 문의 부탁드립니다.</li>
					</ul>

					<ul class="stepTxt">
						<li>
							<span class="step01">Step 01</span>
							<strong>정기배송 상품선택</strong>
							<em>신청상품의 배송 주기/ 횟수 선택 후<br /> 정비배송 장바구니에 담아주세요.</em>
						</li>
						<li>
							<span class="step02">Step 02</span>
							<strong>정기배송 장바구니</strong>
							<em>정기배송 장바구니에서 상품의<br /> 배송주기/횟수/요일을 확인해주세요.</em>
						</li>
						<li>
							<span class="step03">Step 03</span>
							<strong>자동결제 신청</strong>
							<em>지정한 날에 자동결제 및 배송될<br /> 자동결제카드를 등록해주세요.</em>
						</li>
						<li>
							<span class="step04">Step 04</span>
							<strong>결제진행 상품배송</strong>
							<em>배송요청일 3일전 자동결제가 진행되고<br /> 지정한 배송일에 상품이 배송됩니다.</em>
						</li>
					</ul>
				</div>
			</div>
			
			
			<c:set value="0" var="cnt1"></c:set>
			<c:set value="${fn:length(twoDepthCateList)}" var="cnt2"></c:set>
			
			<c:if test="${isMobile}">
				<div class="tabFixW">
					<div class="tabFix">
			</c:if>
			
				<div class="tab_outer swiper-container mainSwiper_category">
					<ul class="tabBox swiper-wrapper">
						<li class="swiper-slide on" id="all">
							<a href="#none" onclick="javascript:special.subscription.changeTab('all');" class="theme1 on">전체</a>
						</li>
						
						<!-- 패키지 코너 -->
						<c:forEach var="corner" items="${packageConerList}" varStatus="i">
							<c:if test="${not empty corner.dmsDisplayitems}">
								<c:set value="${cnt1 + 1}" var="cnt1" />
								<li id="${corner.displayId}">
									<a href="javascript:special.subscription.changeTab('corner', '${corner.displayId}');" class='theme11'>${corner.name}</a>
								</li>
							</c:if>
						</c:forEach>
						
						<!-- 중카테고리 -->
						<c:forEach var="category" items="${twoDepthCateList}" varStatus="i">
							<li id="${category.upperDisplayCategoryId}">
								<a href="#none" onclick="javascript:special.subscription.changeTab('category', '${category.upperDisplayCategoryId}', '${category.categoryName}');" class="on category_${category.upperDisplayCategoryId}">${category.categoryName}</a>
							</li>
						</c:forEach>
					</ul>
					
					<c:if test="${cnt1 + cnt2 > 6 }">
						<div class="tabNavi_btns">
							<a href="#none" class="prev">이전</a>
							<a href="#none" class="next on">다음</a>
						</div>
					</c:if>
				</div>
					
				<c:if test="${isMobile}">
						</div>
					</div>
				</c:if>	
			
			<div id="productList_div">
				
				
				
				
			</div>
		</div>
	</div>
	
	<form name="subscriptionForm" id="subscriptionForm">
		<input type="hidden" name="currentDisplayId" id="hidCurrentDisplayId" value="${packageConerList[0].displayId}" />
		<input type="hidden" name="currentDisplayCategoryId" id="hidCurrentDisplayCategoryId" value="" />
		<input type="hidden" name="currentDisplayType" id="hidCurrentDisplayType" value="all" />
		<input type="hidden" name="currentDisplayCategoryName" id="hidCurrentDisplayCategoryName" value="all" />
		<input type="hidden" name="currentSort" id="hidCurrentSort" value="" />
	</form>
	
	
	<div class="layer_style1 ly_regularHow">
		<div class="box">
			<div class="conArt">
				<strong class="title">정기배송 이용방법</strong>
				<div class="stepImg"><img src="/resources/img/mobile/txt/step_regular.jpg" alt="정기배송 서비스 안내"></div>
				<ul class="notice">
					<li>정기배송은 배송일 2일 전 결제금액을 문자로 안내해 드리고 해당가격으로 자동 결제 진행합니다.</li>
					<li>상품 가격은 변동될 수 있으며, 결제시점 가격이 반영되므로 실결제금액은 변동될 수 있습니다.</li>
					<li>상품변경 및 추가는 배송 3일 이전에 가능합니다.</li>
				</ul>
			</div>
			<button type="button" class="btn_x btn_close">닫기</button>
		</div>
	</div>
