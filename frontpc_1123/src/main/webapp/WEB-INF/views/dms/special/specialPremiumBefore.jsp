<%--
	화면명 : 프론트 & 모바일 프리미엄 멤버십관 인증 전
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ page import="gcp.frontpc.common.contants.Constants"%>
<%
	pageContext.setAttribute("bnr", Config.getString("corner.special.primem.img.1"));
%>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="전문관|프리미엄멤버십관" name="pageNavi"/>
</jsp:include>

<script type="text/javascript" src="/resources/js/jquery.countdown.min.js"></script>

<script type="text/javascript">
$(document).ready(function(){
	if(ccs.common.mobilecheck()) {
		//멤버쉽관
		swiperCon('premiumSwiper', '1'); //배너
	} else {
		
	}
	special.premium.getBeforeProductList();
});

var regPremium = function () {
	if('${isMobile}') {
		ccs.go_url('http://m.maeili.com/mMaeilIFront/front/premembership/premembership.maeili');
	} else {
		ccs.go_url('http://family.maeili.com/index.jsp');
	}
}
	
</script>

<input type="hidden" name="currentPage" value="1" />

<div class="inner">
	<div class="rw_certifyMemBox premium_before">
	
		<c:set var="bnr"  value="${cornerMap[bnr]}"/>
		<div class="memVisual">
			<div class="boxInner">
				<div class="memCont">
					<h2 class="memcTit">
						<c:choose>
							<c:when test="${isMobile}">
								<img src="/resources/img/mobile/txt/txt_certificationBefor01.png" alt="매일 프리미엄 멤버십이란? - 매일유업과 함께하는 차별화된 서비스 가입비 5만원으로 3년간의 혜택! 매일유업과 제휴업체에서 제공하는 푸짐한 혜택을 이용하실 수 있습니다." class="img_mc">
							</c:when>
							<c:otherwise>
								<img src="/resources/img/pc/txt/txt_certificationBefor01.png" alt="매일 프리미엄 멤버십이란? - 매일유업과 함께하는 차별화된 서비스 가입비 5만원으로 3년간의 혜택! 매일유업과 제휴업체에서 제공하는 푸짐한 혜택을 이용하실 수 있습니다." class="img_pc">
							</c:otherwise>
						</c:choose>
					</h2>
					<!-- 상단 비주얼 -->
					<div class="premiumSwiper_wrap">
						<div class="visual swiper-container premiumSwiper">
							<ul class="swiper-wrapper">
								<c:choose>
									<c:when test="${isMobile}">
										<c:forEach var="item" items="${bnr.dmsDisplayitems}" varStatus="status">
											<li class="swiper-slide">
												<a href="javascript:void(0);">
													<img src="${_IMAGE_DOMAIN_}${item.img2}" alt="${item.text2}" class="img_mc" />
												</a>
											</li>									
										</c:forEach>
									</c:when>
									<c:otherwise>
										<li class="swiper-slide">
											<c:forEach var="item" items="${bnr.dmsDisplayitems}" varStatus="status" end="4">
												<a href="javascript:void(0);">
													<img src="${_IMAGE_DOMAIN_}${item.img1}" alt="${item.text1}" class="img_pc" />
												</a>									
											</c:forEach>
										</li>
									</c:otherwise>
								</c:choose>
							</ul>
							<!-- Add Pagination -->
      							<div class="swiper-pagination tp1"></div>
						</div>
						<!-- //상단 비주얼 -->
						<div class="memcbtnWrap">
							<a href="javascript:void(0);" class="btn_prev01" onclick="javascript:regPremium();"><span>프리미엄 멤버십 가입</span></a>
							<a href="javascript:void(0);" class="btn_prev02" onclick="javascript:special.premium.enter('${loginId}');"><span>프리미엄 멤버십관 입장</span></a>
						</div>
					</div>
				</div>
			</div>
		</div>
		
		
		<div class="list_group">
			<h3 class="sub_tit1" style="text-align: center;"> 할인상품 맛보기! 지금 프리미엄 멤버십관에서는…</h3>
			<div class="swiper_wrap">
				<div class="swiper-container prodSwiper product_type1 prodType_4ea block premiumSwiper_prodList_1">
					<ul class="swiper-wrapper" id="beforeProductList">
						<img src="/resources/img/mobile/Loading.gif" alt="" style="display: block; margin-left: auto; margin-right: auto; padding-top: 100px;"/>
					</ul>

					<!-- Add Arrows -->
					<div class="swiper-button-next btn_tp2"></div>
					<div class="swiper-button-prev btn_tp2"></div>

				</div>
			</div>
		</div>
		
<!-- 		<div class="certifyMem"> -->
<!-- 			<h3 class="sub_tit1" style="text-align: center;"> 할인상품 맛보기! 지금 프리미엄 멤버십관에서는…</h3> -->
<!-- 		</div> -->
		
<!-- 		<div class="product_type1 prodType_4ea block" id="beforeProductList"> -->
<!-- 			<img src="/resources/img/mobile/Loading.gif" alt="" style="display: block; margin-left: auto; margin-right: auto; padding-top: 100px;"/> -->
<!-- 		</div> -->
		
	</div>
</div>
