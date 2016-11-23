<%--
	화면명 : 마이페이지 > 나의혜택 > 쿠폰
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ page import="gcp.frontpc.common.contants.Constants"%>
<%
	pageContext.setAttribute("giftShop", Config.getString("giftshop.coupon.id"));
%>


<script type="text/javascript">

// 	$(document).ready(function() {
// 		$(".mobile .layer_style1 .btn_close").off("click").on({
// 			"click" : function() {
// 				$(this).closest(".layer_style1").hide();
// 			}
// 		});
			
// 	});

// 	function openBarcode(index) {
// 		fnLayerPosition($(".mobile .layer_style1.sLayer_maeilpoint_"+index));
// 	}
// 	function fnLayerPosition(target_pop) {
// 		$(target_pop).show();
// 		$(target_pop).height( $(document).height() );
// 		var base_top = ($(window).height() - $(" > .box", target_pop).innerHeight()) / 2;
// 		$(" > .box", target_pop).css({"marginTop" : base_top + $(window).scrollTop() + "px"});
// 	}
</script>

<div>
	<input type="hidden" id="total" name="total" value="${couponInfo.useCoupon + offshopUseCoupon + 0 }">
	<input type="hidden" id="item1" name="item1" value="${couponInfo.useCoupon + 0 }">
	<input type="hidden" id="item2" name="item2" value="${offshopUseCoupon + 0 }">
	<input type="hidden" id="endCnt" name="endCnt" value="${couponInfo.endCoupon + offshopEndCoupon + 0 }">
	<input type="hidden" id="eachCount" name="eachCount" value="${eachCount + 0}">
</div>

		<c:choose>
			<c:when test="${!empty couponList}">
				<c:set var="listSize" value="${fn:length(couponList)}" />
				<c:forEach items="${couponList }" var="list" varStatus="status">
					<c:choose>
						<c:when test="${isMobile }">
							<li>
						</c:when>
						<c:otherwise>
							<c:if test="${status.index % 2 eq 0 }">
								<li>
							</c:if>
						</c:otherwise>
					</c:choose>

						<%-- type div start --%>
						<c:choose>
							<c:when test="${list.spsCoupon.couponTypeCd eq 'COUPON_TYPE_CD.ORDER' }">
								<div class="type1">
							</c:when>
							<c:when test="${list.spsCoupon.couponTypeCd eq 'COUPON_TYPE_CD.PRODUCT' or list.spsCoupon.couponTypeCd eq 'COUPON_TYPE_CD.PLUS'}">
								<div class="type2">
							</c:when>
							<c:otherwise>
								<div class="type3">
							</c:otherwise>
						</c:choose>
						
						<strong class="couponImg">
							<c:choose>
								<c:when test="${list.spsCoupon.couponTypeCd eq 'COUPON_TYPE_CD.DELIVERY' }">
									<em class="won">FREE</em>
								</c:when>
								<c:otherwise>
									<c:choose>
										<c:when test="${list.spsCoupon.dcApplyTypeCd eq 'DC_APPLY_TYPE_CD.RATE' }">
											<em>${list.spsCoupon.dcValue }<i> %</i></em>
										</c:when>
										<c:otherwise>
											<em class="won"><fmt:formatNumber value="${list.spsCoupon.dcValue }" pattern="###,###" /><i> 원</i></em>
										</c:otherwise>
									</c:choose>
								</c:otherwise>
							</c:choose>
							<span>
								<c:choose>
									<c:when test="${list.spsCoupon.couponTypeCd eq 'COUPON_TYPE_CD.PRODUCT' }">
										COUPON
									</c:when>
									<c:when test="${list.spsCoupon.couponTypeCd eq 'COUPON_TYPE_CD.ORDER' }">
										장바구니
									</c:when>
									<c:when test="${list.spsCoupon.couponTypeCd eq 'COUPON_TYPE_CD.DELIVERY' }">
										배송비할인
									</c:when>
									<c:when test="${list.spsCoupon.couponTypeCd eq 'COUPON_TYPE_CD.WRAP' }">
										선물포장
									</c:when>
									<c:when test="${list.spsCoupon.couponTypeCd eq 'COUPON_TYPE_CD.PLUS' }">
										PLUS COUPON
									</c:when>
								</c:choose>
							</span>
							
							<c:if test="${list.isApp eq 'Y'}">
								<%-- APP전용 일 경우 --%>
								<span class="coupon_badge">
									<b><i>APP전용</i></b>
								</span>
							</c:if>
							
						</strong>
							
							<%-- 쿠폰 정보 --%>
							<div class="couponInfo">
								<div class="name">${list.spsCoupon.name }</div>
								
								<p>
									<c:if test="${list.spsCoupon.minOrderAmt > 0 }">
										<em><fmt:formatNumber value="${list.spsCoupon.minOrderAmt }" pattern="###,###" /><i></i></em> 이상 구매 시
										&nbsp;
									</c:if>
									
									<c:if test="${list.spsCoupon.maxDcAmt > 0}">
										최대 <em><fmt:formatNumber value="${list.spsCoupon.maxDcAmt }" pattern="###,###" /></em> 할인
									</c:if>
								</p>
																
								<%-- 배송 쿠폰 --%>
								<c:if test="${list.spsCoupon.couponTypeCd eq 'COUPON_TYPE_CD.DELIVERY' }">
									<p>단, 배송지 <em>하나</em>에 해당함.</p>
								</c:if>									


								<c:choose>
									<c:when test="${giftShop eq list.couponId }">
										<p>본 쿠폰 유효기간은 발급받은 해당월 말일까지만 사용 가능합니다.</p>
									</c:when>
									<c:otherwise>
										<span class="date">${list.useStartDt } ~ ${list.useEndDt }</span>
									</c:otherwise>
								</c:choose>
								<c:if test="${list.deadLine eq 'Y' }">
									<span class="icon_type2 iconPink3">마감임박</span>
								</c:if>
							</div>
							<%-- // 쿠폰 정보 --%>
						</div>
					
					<c:choose>
						<c:when test="${isMobile }">
							</li>
						</c:when>
						<c:otherwise>
							<c:if test="${listSize > 1 }">
								<c:if test="${status.index % 2 eq '1' }">
									</li>
								</c:if>
							</c:if>
							<c:if test="${listSize == 1 }">
								</li>
							</c:if>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</c:when>
			
			<%--매장 쿠폰 --%>
			<c:when test="${!empty offshopCouponList }">
				<c:set var="listSize" value="${fn:length(offshopCouponList)}" />
				<c:forEach items="${offshopCouponList }" var="list" varStatus="status">
					<c:choose>
						<c:when test="${isMobile }">
							<li>
						</c:when>
						<c:otherwise>
							<c:if test="${status.index % 2 eq 0 }">
								<li>
							</c:if>
						</c:otherwise>
					</c:choose>

						<div class="type3">
							<strong class="couponImg">
								<em class="won"><fmt:formatNumber value="${list.dcValue }" pattern="###,###" /><i> 원</i></em>
								<span>매장쿠폰</span>
								<span class="coupon_badge">
									<b><i>오프라인매장전용</i></b>
								</span>
							</strong>
							
							<%-- 쿠폰 정보 --%>
							<div class="couponInfo">
								<div class="name">${list.name }</div>
								
								<p>
									<c:if test="${list.minOrderAmt > 0 }">
										<em><fmt:formatNumber value="${list.minOrderAmt }" pattern="###,###" /><i></i></em> 이상 구매 시
										&nbsp;
									</c:if>
									
									<c:if test="${list.maxDcAmt > 0}">
										최대 <em><fmt:formatNumber value="${list.maxDcAmt }" pattern="###,###" /></em> 할인
									</c:if>
								</p>
								

								<span class="date">${list.useStartDt } ~ ${list.useEndDt }</span>
								<c:if test="${list.deadLine eq 'Y' }">
									<span class="icon_type2 iconPink3">마감임박</span>
								</c:if>
								
							</div>
							<%-- // 쿠폰 정보 --%>
							
							<c:if test="${isMobile }">
								<c:if test="${list.couponIssueNo ne '' or list.couponIssueNo ne null }">
									<div class="barcode">
										<a href="javascript:void(0);">
											<img alt="" src="/api/ccs/common/barcode/${list.couponIssueNo}" />
										</a>  
									</div>
								</c:if>
							</c:if>
						</div>
					
					<c:choose>
						<c:when test="${isMobile }">
							</li>
						</c:when>
						<c:otherwise>
							<c:if test="${listSize > 1 }">
								<c:if test="${status.index % 2 eq '1' }">
									</li>
								</c:if>
							</c:if>
							<c:if test="${listSize == 1 }">
								</li>
							</c:if>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</c:when>
			<%-- // 매장 쿠폰 --%>

			<c:otherwise>
				<li class="empty">
					사용가능 쿠폰이 없습니다.
				</li>
			</c:otherwise>
		</c:choose>
	
<%-- <c:if test="${isMobile }"> --%>
<%-- 	<c:if test="${!empty offshopCouponList}"> --%>
<%-- 		<c:forEach items="${offshopCouponList }" var="list" varStatus="status"> --%>
<!-- 			<!-- ### 16.09.02 팝업 - 픽업쿠폰 바코드 ### --> 
<%-- 			<div class="layer_style1 sLayer_maeilpoint_${status.index}"> --%>
<!-- 				<div class="box"> -->
<!-- 					<div class="conArt"> -->
<%-- 						<strong class="title">${list.couponIssueNo}</strong> --%>
<!-- 						<div class="conBox"> -->
<!-- 							<div class="maeilpointCard"> -->
<!-- 								<div class="cardBarcode"> -->
<%-- 									<img alt="" src="/api/ccs/common/barcode/${list.couponIssueNo}" height="60" width="324"/>  --%>
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 						</div> -->
<!-- 					</div> -->
<!-- 					<button type="button" class="btn_close">레이어팝업 닫기</button> -->
<!-- 				</div> -->
<!-- 			</div> -->
<!-- 			<!-- ### //16.09.02 팝업 - 픽업쿠폰 바코드 ### --> 
<%-- 		</c:forEach> --%>
<%-- 	</c:if> --%>
<%-- </c:if> --%>

