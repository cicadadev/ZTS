<%--
	화면명 : 마이페이지 > 메인 > 장바구니 상품
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript">
	$(document).ready(function(){
		swiperCon('mypageSwiper_wishProd_1', 800, 2, 12, false, true, 2, 'fraction');		
	});
</script>

	<c:set var="wishListSize" value="${fn:length(wishList)}" />
	<h3 class="sub_tit1">쇼핑찜 <em>(${wishListSize })</em></h3>
	<a href="javascript:void(0);" class="btn_all" onclick="javascript:ccs.link.mypage.activity.wishlist();">더보기</a>
<!-- 	<div class="product_type1 prodType_2ea block"> -->
	<div class="swiper_wrap">
		<div class="product_type1 prodType_2ea">
		<div class="swiper-container mypageSwiper_wishProd_1">
		<c:choose>
			<c:when test="${!empty wishList}">
			<ul class="swiper-wrapper">
				<c:forEach items="${wishList}" var="item" varStatus="status" end="5">
					<li class="swiper-slide">
						<a href="javascript:void(0);" >
						<div class="img">
							<a href="javascript:void(0);" onclick="javascript:ccs.link.product.detail('${item.productId}')">
								<tags:prdImgTag productId="${item.productId}" size="186" alt="${item.name}" />
							</a>
						</div>

							<div class="info">
								<div class="flag">
									<c:if test="${item.freeDeliveryYn eq 'Y'}">
										<span class="icon_type1">무료배송</span>
									</c:if>
									<c:if test="${item.couponYn eq 'Y'}">
										<span class="icon_type1">쿠폰</span>
									</c:if>
									<c:if test="${item.pointSaveYn eq 'Y'}">
										<span class="icon_type1">사은품</span>
									</c:if>
								</div>
								<span class="title">${item.name}</span>
								<div class="etc">
									<span class="price"><fmt:formatNumber value="${item.salePrice}" pattern="###,###" /><i>원</i></span>
								</div>
<!-- 								<div class="btns" style="text-align: right;"> -->
<%-- 									<button type="button" class="btn_sStyle1" onclick="mypage.wishlist.openOptionLayer('cart', '${status.index}');" >장바구니</button> --%>
<%-- 									<button type="button" class="btn_sStyle1 sPurple1" onclick="mypage.wishlist.openOptionLayer('order', '${status.index}');" >바로구매</button> --%>
<!-- 								</div> -->
							</div>
<%-- 							<input type="hidden" name="hidProductId${status.index}" id="hidProductId${status.index}" value="${item.productId}" /> --%>
<!-- 							<input type="hidden" name="hidCallbackPart" id="hidCallbackPart" value="" /> -->
						</a>
					</li>
				</c:forEach>
			</ul>
			
			<!-- Add Pagination -->
			<div class="swiper-pagination"></div>
	
			<!-- Add Arrows -->
			<div class="swiper-button-next btn_tp4"></div>
			<div class="swiper-button-prev btn_tp4"></div>
			
			</c:when>
			<c:otherwise>
				<p class="empty">
					최근 찜한 상품 목록이 없습니다.
				</p>
			</c:otherwise>
		</c:choose>
		</div>
		</div>
	</div>
	
<%-- 	<c:if test="${wishListSize > 0 }"> --%>
<!-- 		<div class="paginate"> -->
<!-- 			<button class="prev">이전</button><button class="next">다음</button> -->
<!-- 		</div> -->
<%-- 	</c:if> --%>
