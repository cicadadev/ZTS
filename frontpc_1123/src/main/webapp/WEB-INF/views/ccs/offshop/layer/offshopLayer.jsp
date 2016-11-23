<%--
	화면명 : 마이페이지 > 관심매장 설정 > 매장 정보 레이어
	작성자 : stella
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">
$(document).ready(function() {
	if (ccs.common.mobilecheck()) {
		$("#offshopMap").height($(window).width());
	}
});
</script>

<div class="pop_wrap layer_shop" id="offshopLayer">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">매장 정보</h3>
		</div>
		<div class="pop_content">
			<div class="shop_info">
				<dl style="padding-bottom:10px;">
					<dt><span class="brand" id="brandTitle"></span> ${offshopInfo.name}</dt>
					<dd>
						${offshopInfo.address1}
						<c:if test="${empty offshopInfo.holidayInfo}">
							<c:choose>
								<c:when test="${isMobile}">
									<a href="tel:${offshopInfo.offshopPhone}" class="btn_imgTel">
								</c:when>
								<c:otherwise>
									<a href="#none" class="btn_imgTel">
								</c:otherwise>
							</c:choose>
								<em>${offshopInfo.offshopPhone}</em>
							</a>
						</c:if>
					</dd>
					<dd>
						${offshopInfo.holidayInfo}
						<c:if test="${not empty offshopInfo.holidayInfo}">
							<c:choose>
								<c:when test="${isMobile}">
									<a href="tel:${offshopInfo.offshopPhone}" class="btn_imgTel">
								</c:when>
								<c:otherwise>
									<a href="#none" class="btn_imgTel">
								</c:otherwise>
							</c:choose>
								<em>${offshopInfo.offshopPhone}</em>
							</a>
						</c:if>
					</dd>
				</dl>

				<button type="button" class="bookmark checked">관심매장</button>

				<div id="offshopMap" class="shop_map">
					<script>mypage.offshop.roadMap("offshopMap", "${offshopInfo.latitude}", "${offshopInfo.longitude}");</script>
				</div>

				<p class="txt_info">
					${offshopInfo.addressInfo }
				</p>
			</div>
		</div>
		<button type="button" class="btn_x pc_btn_close" onclick="ccs.layer.close('offshopLayer')">닫기</button>
	</div>
</div>

<style>
.pc .layer_shop .shop_info .shop_map {width: 480px; height: 385px; overflow: hidden;}
.pc .layer_shop .shop_info #brandTitle {font-weight: bold; color: #2d9bd6;}

.mobile .layer_shop .shop_info .shop_map {width: 100%; overflow: hidden; margin:0 0 0 0;}
</style>