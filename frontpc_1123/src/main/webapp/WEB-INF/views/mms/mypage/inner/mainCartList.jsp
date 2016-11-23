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
		swiperCon('mypageSwiper_cartProd_1', 800, 2, 12, false, true, 2, 'fraction');

		//장바구니 카운트
		$("#cartCnt").text($('.btn_cart > em').text());		
	});
</script>

	<h3 class="sub_tit1">장바구니 담은 상품 <em  id="cartCnt"></em></h3>
	<a href="javascript:ccs.link.order.cart();" class="btn_all">더보기</a>
	<div class="swiper_wrap">
		<div class="product_type1 prodType_2ea">
		<div class="swiper-container mypageSwiper_cartProd_1" >
		<c:choose>
			<c:when test="${fn:length(cartList) > 0 }">
			<ul class="swiper-wrapper">
				<c:forEach items="${cartList }" var="cart" varStatus="status" end="5">
					<li class="swiper-slide">
						<a href="javascript:void(0);">
						<div class="img">
							<a href="javascript:void(0);" onclick="javascript:ccs.link.product.detail('${cart.productId}')">
								<tags:prdImgTag productId="${cart.productId}" size="186" alt="${cart.productName }" />
							</a>
						</div>

							<div class="info">
								<div class="flag">
									<c:if test="${cart.minDeliveryFreeAmt <= cart.policyTotalPrice || cart.policyDeliveryFeeFreeYn == 'Y' || cart.deliveryFee == 0}">
										<span class="icon_type1">무료배송</span>
									</c:if>
									<c:if test="${cart.couponId ne '' || cart.couponId ne null || !empty cart.couponId }">
										<span class="icon_type1">쿠폰</span>
									</c:if>
								</div>
								<span class="title">${cart.productName }</span>
								<div class="etc">
									<span class="price"><fmt:formatNumber value="${cart.salePrice + cart.addSalePrice }" pattern="###,###"/><i>원</i></span>
								</div>
<!-- 								<div class="btns" style="text-align: right;"> -->
<%-- 									<button type="button" class="btn_sStyle1 sPurple1" onclick="mypage.wishlist.openOptionLayer('order', '${status.index}');">바로구매</button> --%>
<%-- 									<input type="hidden" name="hidProductId${status.index}" id="hidProductId${status.index}" value="${cart.productId}" /> --%>
<!-- 									<input type="hidden" name="hidCallbackPart" id="hidCallbackPart" value="" /> -->
<!-- 								</div> -->
							</div>
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
					장바구니 상품 목록이 없습니다.
				</p>
			</c:otherwise>
		</c:choose>
		</div>
		</div>
	</div>
	
<%-- 	<c:if test="${fn:length(cartList) > 0 }"> --%>
<!-- 		<div class="paginate"> -->
<!-- 			<button class="prev">이전</button><button class="next">다음</button> -->
<!-- 		</div> -->
<%-- 	</c:if> --%>
