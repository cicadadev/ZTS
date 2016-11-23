<%--
	화면명 : 멤버쉽관 > 인증 전
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags" %>

<script type="text/javascript" src="/resources/js/jquery.countdown.min.js"></script>
<script type="text/javascript">
//리미엄멤버십관
$(document).ready(function(){

	if(ccs.common.mobilecheck()) {
		swiperCon('premiumSwiper_prodList_1', '3'); // 상품리스트
	} else {
		swiperCon('premiumSwiper_prodList_1', 400, 4, 12, false, true, 4);
	}
	
});
</script>

	<c:choose>
		<c:when test="${fn:length(productList) > 0}">
			<c:forEach var="product" items="${productList}" varStatus="i">
				<c:set var="product" value="${product}" scope="request" />
					<li class="swiper-slide">
						<a href="javascript:ccs.link.product.detail('${product.productId}','');" >
							<div class="img">
								<c:choose>
									<c:when test="${isMobile}">
										<tags:prdImgTag productId="${product.productId}" seq="0" size="180"  />
									</c:when>
									<c:otherwise>
										<tags:prdImgTag productId="${product.productId}" seq="0" size="326"  />
									</c:otherwise>
								</c:choose>
							</div>

							<div class="info">
								<span class="title" id="deleteArea" style="height: 1px;">
									<a href="javascript:ccs.link.product.detail('${product.productId}','');" >
										${product.name}
									</a>
								</span>
								<div class="etc">
									<button type="button" class="btn_blank">새창 열기</button>
								</div>
							</div>
						</a>
					</li>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<c:if test="${empty productList}">
				상품이 없습니다.
			</c:if>
		</c:otherwise>
	</c:choose>