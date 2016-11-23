<%--
	화면명 : 프론트 & 모바일  상품 정보
	작성자 : emily
	**** 화면별로 노출하는 정보가 다를 경우 아래와 같이 파라미터로 넘겨주세요 , 파라미터는 구분자 용도로 쓰일것들만 사용해주세요*****
	 	- 기획전 : exhibit
	 	- 다자녀 : multichild
	 	- 임직원 : officer
	 	- B2E : b2e
	 	- 쇼킹제로 : shocking
	 	- 전문관 : special
	 	- 맴머십관 : member 
	 	- 베스트 : best
	 	- 프리미엄 멤버십관 : premium
	 	- 브랜드관 메인 : brand
	 	- 스와이퍼 여부 : swiper
	<jsp:param name="type" value="member" />
	
	**** 상품정보는 ${product} 객체로 넘겨주세요 **** 
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false"%>
<%@ page import="gcp.common.util.FoSessionUtil" %>
<%@ page import="java.math.BigDecimal" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%
	pageContext.setAttribute("member", FoSessionUtil.getMemberGradeForPrice());
%>

<%-- soldout -> 프리미엄 멤버십, 임직원, 쇼킹제로 --%>
<c:choose>
	<c:when test="${param.type eq 'officer' and param.type eq 'premium' and param.type eq 'shocking'}">
		<c:if test="${product.spsDealproduct.dealStockQty < 1 }">
			<li class="soldout">
		</c:if>
		<c:if test="${product.spsDealproduct.dealStockQty >= 1 }">
			<li>
		</c:if>
	</c:when>
	<c:when test="${param.type eq 'brand'}">
		<li class="sscc_item0${param.itemIndex}">
	</c:when>
	<c:otherwise>
		<!-- 스와이퍼 일경우 class를  swiper-slide 지정해야 한다. -->
		<li  ${param.swipe == 'Y'? 'class="swiper-slide"' : '' }>
	</c:otherwise>
</c:choose>

<c:choose>
	<c:when test="${isMobile }">
		<div onclick="javascript:ccs.link.product.detail('${product.productId}','');">
				<!-- 이미지 //-->
				<div class="img">
					<a href="javascript:void(0);" >
	</c:when>
	<c:otherwise>
		<div>
				<!-- 이미지 //-->
				<div class="img">
					<a href="javascript:ccs.link.product.detail('${product.productId}','');" >
	</c:otherwise>
</c:choose>
					<c:choose>
						<c:when test="${param.type eq 'exhibit' && param.exhibitType eq 'oneday'}">
							<c:choose>
								<c:when test="${member eq 'welcome'}">
									<tags:dcRate stdPrice="${product.listPrice }" dcPrice="${product.pmsProductprice.welcomeSalePrice }" var="rate" />
								</c:when>
								<c:when test="${member eq 'family'}">
									<tags:dcRate stdPrice="${product.listPrice }" dcPrice="${product.pmsProductprice.familySalePrice }"  var="rate"/>
								</c:when>
								<c:when test="${member eq 'silver'}">
									<tags:dcRate stdPrice="${product.listPrice }" dcPrice="${product.pmsProductprice.silverSalePrice }"  var="rate"/>
								</c:when>
								<c:when test="${member eq 'gold'}">
									<tags:dcRate stdPrice="${product.listPrice }" dcPrice="${product.pmsProductprice.goldSalePrice }"  var="rate"/>
								</c:when>
								<c:when test="${member eq 'vip'}">
									<tags:dcRate stdPrice="${product.listPrice }" dcPrice="${product.pmsProductprice.vipSalePrice }"  var="rate"/>
								</c:when>
								<c:when test="${member eq 'prestage'}">
									<tags:dcRate stdPrice="${product.listPrice }" dcPrice="${product.pmsProductprice.prestigeSalePrice}"  var="rate"/>
								</c:when>
								<c:otherwise>
									<tags:dcRate stdPrice="${product.listPrice }" dcPrice="${product.pmsProductprice.salePrice}"  var="rate"/>
								</c:otherwise>
							</c:choose>
							
							<c:if test="${not empty product.mainProductImg}">
								<c:choose>
									<c:when test="${isMobile}">
										<img src="${_IMAGE_DOMAIN_}${product.mainProductImg}" alt="" onerror="this.src='/resources/img/noimage/no_image_180.png';"/>
									</c:when>
									<c:otherwise>
										<img src="${_IMAGE_DOMAIN_}${product.mainProductImg}" alt="" onerror="this.src='/resources/img/noimage/no_image_326.png';"/>
									</c:otherwise>
								</c:choose>
							</c:if>
							<c:if test="${empty product.mainProductImg}">
								<c:choose>
									<c:when test="${isMobile}">
										<tags:prdImgTag productId="${product.productId}" seq="0" size="180"  />
									</c:when>
									<c:otherwise>
										<tags:prdImgTag productId="${product.productId}" seq="0" size="326"  />
									</c:otherwise>
								</c:choose>
							</c:if>
							<c:if test="${rate != 0  && rate != 100}">
								<em class="per_bg">${rate}<i>%</i></em>
							</c:if>
								
						</c:when>
						<c:when test="${param.type eq 'pickup'}">
							<c:if test="${product.offshopImg ne null}">
								<c:set var="img" value="${fn:split(product.offshopImg, '_')[1]}" />
								<c:set var="img" value="${fn:split(img, '.')[0]}" />
								<c:choose>
									<c:when test="${isMobile}">
										<tags:prdImgTag productId="${product.productId}" seq="${img}" size="180"  />
									</c:when>
									<c:otherwise>
										<tags:prdImgTag productId="${product.productId}" seq="${img}" size="326"  />
									</c:otherwise>
								</c:choose>
							</c:if>
							<c:if test="${product.offshopImg eq null}">
								<c:choose>
									<c:when test="${isMobile}">
										<tags:prdImgTag productId="${product.productId}" seq="0" size="180"  />
									</c:when>
									<c:otherwise>
										<tags:prdImgTag productId="${product.productId}" seq="0" size="326"  />
									</c:otherwise>
								</c:choose>
							</c:if>
						</c:when>
						<c:when test="${param.type eq 'brand'}">
							<c:choose>
								<c:when test="${isMobile}">
									<tags:prdImgTag productId="${product.productId}" seq="0" size="180"  />
								</c:when>
								<c:otherwise>
									<tags:prdImgTag productId="${product.productId}" seq="0" size="228"  />
								</c:otherwise>
							</c:choose>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${isMobile}">
									<tags:prdImgTag productId="${product.productId}" seq="0" size="180"  />
								</c:when>
								<c:otherwise>
									<tags:prdImgTag productId="${product.productId}" seq="0" size="326"  />
								</c:otherwise>
							</c:choose>
							
							<%-- soldout -> 프리미엄 멤버십, 임직원, 쇼킹제로 --%>
							<c:choose>
								<c:when test="${param.type eq 'officer' and param.type eq 'premium' and param.type eq 'shocking'}">
									<c:if test="${product.spsDealproduct.dealStockQty < 1 }">
										<span class="txt_soldout">
											<em>
												<strong>SOLD OUT</strong>판매가 완료되었습니다
											</em>
										</span>
									</c:if>
								</c:when>
								<c:otherwise>
								</c:otherwise>
							</c:choose>
							<%-- // soldout 일 경우 --%>
							
								<c:if test="${param.type eq 'best'}">
									<!-- 베스트 : 랭킹 -->
									<fmt:parseNumber value="${param.bestRanking}" var="ranking" />
									<span class="txt_rank ${ranking <= 4 ? 'rank_top' : ''}">${param.bestRanking}</span>
								</c:if>
							
							<!-- 뱃지 //-->
							<c:choose>
								<c:when test="${param.type eq 'shocking'}">
									<!-- 쇼킹제로 : 마감임박, 오늘오픈, 품절임박 -->
									<c:choose>
										<c:when test="${dealProduct.badgeGubun == 1}">
											<span class="txt_fast">마감임박</span>
										</c:when>
										<c:when test="${dealProduct.badgeGubun == 2}">
											<span class="txt_fast">오늘오픈</span>
										</c:when>
										<c:when test="${dealProduct.badgeGubun == 3}">
											<span class="txt_fast">품절임박</span>
										</c:when>
									</c:choose>
									
									<c:if test="${!isMobile}">
										<span class="txt_time pc_only">
											<small>남은시간</small>
											<em><div name="time_countdown" id="dealProduct${param.dealProductIndex}"><script>sps.deal.shockingzero.countdown("dealProduct${param.dealProductIndex}", "${dealProduct.endDt}")</script></div></em>
										</span>
									</c:if>
								</c:when>
								<c:when test="${param.type eq 'exhibit'}">
								<!-- 기획전 : COUPON, 타임세일, 브랜드 -->
								
								</c:when>
								<c:when test="${param.type eq 'special'}">
								<!-- 전문관 : SALE -->
								
								</c:when>
								<c:when test="${param.type eq 'premium'}">
									<c:choose>
										<c:when test="${product.spsDealproduct.dealStockQty < 4}">
											<span class="txt_imminent">품절임박</span>
										</c:when>
										<c:otherwise>
											<c:if test="${product.spsDealproduct.isDeadline eq 'Y' }">
												<span class="txt_imminent">마감임박</span>
											</c:if>
										</c:otherwise>
									</c:choose>
								</c:when>
								<c:otherwise>
								</c:otherwise>
							</c:choose>
							<!--// 뱃지 -->
						</c:otherwise>
					</c:choose>
				</a>
			</div>
			<!--// 이미지 -->
	
			<div class="info">
					<!-- 모바일일 경우  -->
					<c:if test="${isMobile}">
						<c:if test="${param.type eq 'shocking'}">
							<span class="txt_time mo_only">
								<small>남은시간</small>
								<em><div id="dealProduct${param.dealProductIndex}"><script>sps.deal.shockingzero.countdown("dealProduct${param.dealProductIndex}", "${dealProduct.endDt}")</script></div></em>
							</span>
						</c:if>				
					</c:if>
					
					<!-- 아이콘 // -->
					<c:choose>
						<c:when test="${param.type eq 'multichild'}">
<!-- 							<p class="child_date">월 태어나서 ~ 100일까지</p> -->
						</c:when>
						<c:otherwise>
							
							<div class="flag" ${param.flagNone}>
								<c:choose>
									<c:when test="${param.type eq 'b2e'}">
										<c:if test="${product.pmsProductprice.deliveryFeeFreeYn eq 'Y'}">
											<span class="icon_type1">무료배송</span>
										</c:if>
										<c:if test="${not empty product.pmsProductprice.couponId}">
											<span class="icon_type1">쿠폰</span>
										</c:if>
										<c:if test="${product.pmsProductprice.pointYn eq 'Y'}">
											<span class="icon_type1 point">포인트</span>
										</c:if>
										
										<c:if test="${product.newIconYn eq 'Y'}">
											<span class="icon_type1 new">NEW</span>
										</c:if>
										<!-- <span class="icon_type1">사은품</span> -->
									</c:when>
									<c:when test="${param.type eq 'officer'}">
										<c:if test="${product.pmsProductprice.deliveryFeeFreeYn eq 'Y'}">
											<span class="icon_type1">무료배송</span>
										</c:if>
										<c:if test="${not empty product.pmsProductprice.couponId}">
											<span class="icon_type1">쿠폰</span>
										</c:if>
										<c:if test="${product.spsDealproduct.pointSaveRate > 0}">
											<span class="icon_type1 point">포인트</span>
										</c:if>
										
										<c:if test="${product.newIconYn eq 'Y'}">
											<span class="icon_type1 new">NEW</span>
										</c:if>
										<!-- <span class="icon_type1">사은품</span> -->
									</c:when>
									<c:when test="${param.type eq 'premium'}">
										<c:if test="${product.spsDealproduct.deliveryFeeFreeYn eq 'Y'}">
											<span class="icon_type1">무료배송</span>
										</c:if>
										<c:if test="${not empty product.spsDealproduct.couponId}">
											<span class="icon_type1">쿠폰</span>
										</c:if>
										<c:if test="${product.spsDealproduct.pointSaveRate > 0}">
											<span class="icon_type1 point">포인트</span>
										</c:if>
										<c:if test="${not empty product.spsDealproduct.presentProductId}">
											<span class="icon_type1">사은품</span>
										</c:if>
									</c:when>
									<c:when test="${param.type eq 'shocking'}">
										<c:if test="${product.pmsProductprice.deliveryFeeFreeYn eq 'Y'}">
											<span class="icon_type1">무료배송</span>
										</c:if>
										<c:if test="${not empty dealProduct.spsCoupons[0].couponId}">
											<span class="icon_type1">쿠폰</span>
										</c:if>
										<c:if test="${product.pmsProductprice.pointYn eq 'Y'}">
											<span class="icon_type1 point">포인트</span>
										</c:if>
									</c:when>									
									<c:otherwise>
										<c:choose>
											<c:when test="${member eq 'welcome'}">
												<c:if test="${product.pmsProductprice.welcomeDeliveryFeeFreeYn eq 'Y'}">
													<span class="icon_type1">무료배송</span>
												</c:if>
												<c:if test="${not empty product.pmsProductprice.welcomeCouponId}">
													<span class="icon_type1">쿠폰</span>
												</c:if>
												<c:if test="${product.pmsProductprice.welcomePointYn eq 'Y'}">
													<span class="icon_type1 point">포인트</span>
												</c:if>
											</c:when>
											<c:when test="${member eq 'family'}">
												<c:if test="${product.pmsProductprice.familyDeliveryFeeFreeYn eq 'Y'}">
													<span class="icon_type1">무료배송</span>
												</c:if>
												<c:if test="${not empty product.pmsProductprice.familyCouponId}">
													<span class="icon_type1">쿠폰</span>
												</c:if>
												<c:if test="${product.pmsProductprice.familyPointYn eq 'Y'}">
													<span class="icon_type1 point">포인트</span>
												</c:if>
											</c:when>
											<c:when test="${member eq 'silver'}">
												<c:if test="${product.pmsProductprice.silverDeliveryFeeFreeYn eq 'Y'}">
													<span class="icon_type1">무료배송</span>
												</c:if>
												<c:if test="${not empty product.pmsProductprice.silverCouponId}">
													<span class="icon_type1">쿠폰</span>
												</c:if>
												<c:if test="${product.pmsProductprice.silverPointYn eq 'Y'}">
													<span class="icon_type1 point">포인트</span>
												</c:if>
											</c:when>
											<c:when test="${member eq 'gold'}">
												<c:if test="${product.pmsProductprice.goldDeliveryFeeFreeYn eq 'Y'}">
													<span class="icon_type1">무료배송</span>
												</c:if>
												<c:if test="${not empty product.pmsProductprice.goldCouponId}">
													<span class="icon_type1">쿠폰</span>
												</c:if>
												<c:if test="${product.pmsProductprice.goldPointYn eq 'Y'}">
													<span class="icon_type1 point">포인트</span>
												</c:if>
											</c:when>
											<c:when test="${member eq 'vip'}">
												<c:if test="${product.pmsProductprice.vipDeliveryFeeFreeYn eq 'Y'}">
													<span class="icon_type1">무료배송</span>
												</c:if>
												<c:if test="${not empty product.pmsProductprice.vipCouponId}">
													<span class="icon_type1">쿠폰</span>
												</c:if>
												<c:if test="${product.pmsProductprice.vipPointYn eq 'Y'}">
													<span class="icon_type1 point">포인트</span>
												</c:if>
											</c:when>
											<c:when test="${member eq 'prestage'}">
												<c:if test="${product.pmsProductprice.prestigeDeliveryFeeFreeYn eq 'Y'}">
													<span class="icon_type1">무료배송</span>
												</c:if>
												<c:if test="${not empty product.pmsProductprice.prestigeCouponId}">
													<span class="icon_type1">쿠폰</span>
												</c:if>
												<c:if test="${product.pmsProductprice.prestigePointYn eq 'Y'}">
													<span class="icon_type1 point">포인트</span>
												</c:if>
											</c:when>
											<c:otherwise>
												<c:if test="${product.pmsProductprice.deliveryFeeFreeYn eq 'Y'}">
													<span class="icon_type1">무료배송</span>
												</c:if>
												<c:if test="${not empty product.pmsProductprice.couponId}">
													<span class="icon_type1">쿠폰</span>
												</c:if>
												<c:if test="${product.pmsProductprice.pointYn eq 'Y'}">
													<span class="icon_type1 point">포인트</span>
												</c:if>
											</c:otherwise>
										</c:choose>
										
										<c:if test="${product.newIconYn eq 'Y'}">
											<span class="icon_type1 new">NEW</span>
										</c:if>
										
										<!-- <span class="icon_type1">사은품</span> -->
										
									</c:otherwise>
								</c:choose>
							</div> 
						</c:otherwise>
					</c:choose>
				<!--// 아이콘  -->
				
				<!-- 상품 명 // -->
				<span class="title">
					<a href="javascript:ccs.link.product.detail('${product.productId}','');" >
						<c:if test="${param.type eq 'exhibit' && param.exhibitType eq 'oneday'}">
							<strong class="tag">${product.adCopy}</strong>
						</c:if>
						<c:out value="${product.name}"/>
					</a>
				</span>
				<!--// 상품 명  -->
				
				<!-- 가격 // -->
				<div class="etc">
					<c:choose>
						<c:when test="${param.type eq 'shocking'}">
							
								<span class="ori">
								<c:if test="${dealProduct.listPrice > dealProduct.salePrice}">
									<em><fmt:formatNumber type="currency" value="${dealProduct.listPrice}" pattern="###,###" /></em>
								</c:if>		
								</span>
												
							<span class="price"><fmt:formatNumber type="currency" value="${dealProduct.salePrice}" pattern="###,###" /><i>원</i></span>
							<c:if test="${dealProduct.dealPriceRate > 0}">
								<em class="per">${dealProduct.dealPriceRate}<i>%</i></em>
							</c:if>						
						</c:when>
						<c:when test="${param.type eq 'b2e'}">
								<span class="ori">
								<c:if test="${product.listPrice >  product.pmsProductprice.salePrice}">
								<em><fmt:formatNumber type="currency" value="${product.listPrice}" pattern="###,###" /></em>
								</c:if>	
								</span>
							<span class="price"><fmt:formatNumber type="currency" value="${product.pmsProductprice.salePrice}" pattern="###,###" /><i>원</i></span>
						</c:when>
						<c:when test="${param.type eq 'premium'}">
							<c:if test="${param.certify eq true }">
								<span class="ori"><em><fmt:formatNumber type="currency" value="${product.listPrice}" pattern="###,###" /></em>원</span>
								<span class="price"><fmt:formatNumber type="currency" value="${product.pmsProductprice.salePrice}" pattern="###,###" /><i>원</i></span>
								<tags:dcRate stdPrice="${product.listPrice }" dcPrice="${product.pmsProductprice.salePrice }"  var="rate"/>
								<c:if test="${rate != 0  && rate != 100}">
									<div class="align_r blockType">
										<button type="button" class="btn_blank" onclick="ccs.link.openWindow('${product.productId}');">새창 열기</button>
										<em class="per">${rate }<i>%</i></em>
									</div>
								</c:if>
							</c:if>
						</c:when>
						<c:when test="${param.type eq 'multichild' || param.type eq 'officer' || param.type eq 'pickup' || param.type eq 'subscription'}">
							
							<span class="ori">
								<c:if test="${param.type eq 'multichild' or param.type eq 'officer' or param.type eq 'subscription' or param.type eq 'pickup'}">
									<em>
										<fmt:formatNumber type="currency" value="${product.listPrice}" pattern="###,###" />
									</em>
								</c:if>
							</span>
							<span class="price"> 
								<c:if test="${param.type eq 'multichild'}">
									<span>다자녀우대가</span>
									<fmt:formatNumber type="currency" value="${product.pmsProductprice.salePrice}" pattern="###,###" /><i>원</i>
								</c:if>
								<c:if test="${param.type eq 'officer'}">
									<em>임직원가</em>
									<fmt:formatNumber type="currency" value="${product.pmsProductprice.salePrice}" pattern="###,###" /><i>원</i>
								</c:if>
								<c:if test="${param.type eq 'pickup'}">
									<em>매장픽업가</em>
									<fmt:formatNumber type="currency" value="${product.salePrice}" pattern="###,###" /><i>원</i>
								</c:if>
								<c:if test="${param.type eq 'subscription'}">
									<em>정기배송가</em>
									<fmt:formatNumber type="currency" value="${product.regularDeliveryPrice}" pattern="###,###" /><i>원</i>
									<button type="button" class="btn_blank">새창 열기</button>
								</c:if>
							</span>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${member eq 'welcome'}">
									<tags:dcRate stdPrice="${product.salePrice }" dcPrice="${product.pmsProductprice.welcomeSalePrice }"  var="discount"/>
									
										<span class="ori">
										<c:if test="${product.listPrice > product.pmsProductprice.welcomeSalePrice}">
											<em><fmt:formatNumber type="currency" value="${product.listPrice}" pattern="###,###" /></em>
										</c:if>
										</span>
									
									<span class="price"><fmt:formatNumber type="currency" value="${product.pmsProductprice.welcomeSalePrice}" pattern="###,###" /><i>원</i></span>
									<c:if test="${param.type eq 'exhibit' && product.salePrice ne product.pmsProductprice.welcomeSalePrice && param.exhibitType ne 'oneday'}">
										<c:if test="${discount != 0 && discount != 100 }">
											<em class="per">${discount }<i>%</i></em>
										</c:if>
									</c:if>
								</c:when>
								<c:when test="${member eq 'family'}">
									<tags:dcRate stdPrice="${product.salePrice }" dcPrice="${product.pmsProductprice.familySalePrice }"  var="discount"/>
									
										<span class="ori">
										<c:if test="${product.listPrice > product.pmsProductprice.familySalePrice}">
											<em><fmt:formatNumber type="currency" value="${product.listPrice}" pattern="###,###" /></em>
										</c:if>
										</span>
									
									<span class="price"><fmt:formatNumber type="currency" value="${product.pmsProductprice.familySalePrice}" pattern="###,###" /><i>원</i></span>
									<c:if test="${param.type eq 'exhibit' && product.salePrice ne product.pmsProductprice.familySalePrice && param.exhibitType ne 'oneday'}">
										<c:if test="${discount != 0 && discount != 100 }">
											<em class="per">${discount }<i>%</i></em>
										</c:if>
									</c:if>
								</c:when>
								<c:when test="${member eq 'silver'}">
									<tags:dcRate stdPrice="${product.salePrice }" dcPrice="${product.pmsProductprice.silverSalePrice }"  var="discount"/>
									<c:if test="${product.salePrice ne  product.pmsProductprice.silverSalePrice}">
										<span class="ori"><em><fmt:formatNumber type="currency" value="${product.salePrice}" pattern="###,###" /></em></span>
									</c:if>
									<span class="price"><fmt:formatNumber type="currency" value="${product.pmsProductprice.silverSalePrice}" pattern="###,###" /><i>원</i></span>
									<c:if test="${param.type eq 'exhibit' && product.salePrice ne product.pmsProductprice.silverSalePrice && param.exhibitType ne 'oneday'}">
										<c:if test="${discount != 0 && discount != 100 }">
											<em class="per">${discount }<i>%</i></em>
										</c:if>
									</c:if>
								</c:when>
								<c:when test="${member eq 'gold'}">
									<tags:dcRate stdPrice="${product.salePrice }" dcPrice="${product.pmsProductprice.goldSalePrice }"  var="discount"/>
									<c:if test="${product.salePrice ne  product.pmsProductprice.goldSalePrice}">
										<span class="ori"><em><fmt:formatNumber type="currency" value="${product.salePrice}" pattern="###,###" /></em></span>
									</c:if>
									<span class="price"><fmt:formatNumber type="currency" value="${product.pmsProductprice.goldSalePrice}" pattern="###,###" /><i>원</i></span>
									<c:if test="${param.type eq 'exhibit' && product.salePrice ne product.pmsProductprice.goldSalePrice && param.exhibitType ne 'oneday'}">
										<c:if test="${discount != 0 && discount != 100 }">
											<em class="per">${discount }<i>%</i></em>
										</c:if>
									</c:if>
								</c:when>
								<c:when test="${member eq 'vip'}">
									<tags:dcRate stdPrice="${product.salePrice }" dcPrice="${product.pmsProductprice.vipSalePrice }"  var="discount"/>
									<c:if test="${product.salePrice ne  product.pmsProductprice.vipSalePrice}">
										<span class="ori"><em><fmt:formatNumber type="currency" value="${product.salePrice}" pattern="###,###" /></em></span>
									</c:if>
									<span class="price"><fmt:formatNumber type="currency" value="${product.pmsProductprice.vipSalePrice}" pattern="###,###" /><i>원</i></span>
									<c:if test="${param.type eq 'exhibit' && product.salePrice ne product.pmsProductprice.vipSalePrice && param.exhibitType ne 'oneday'}">
										<c:if test="${discount != 0 && discount != 100 }">
											<em class="per">${discount }<i>%</i></em>
										</c:if>
									</c:if>
								</c:when>
								<c:when test="${member eq 'prestage'}">
									<tags:dcRate stdPrice="${product.salePrice }" dcPrice="${product.pmsProductprice.prestigeSalePrice }"  var="discount"/>
									<c:if test="${product.salePrice ne  product.pmsProductprice.prestigeSalePrice}">
										<span class="ori"><em><fmt:formatNumber type="currency" value="${product.salePrice}" pattern="###,###" /></em></span>
									</c:if>
									<span class="price"><fmt:formatNumber type="currency" value="${product.pmsProductprice.prestigeSalePrice}" pattern="###,###" /><i>원</i></span>
									<c:if test="${param.type eq 'exhibit' && product.salePrice ne product.pmsProductprice.prestigeSalePrice && param.exhibitType ne 'oneday'}">
										<c:if test="${discount != 0 && discount != 100 }">
											<em class="per">${discount }<i>%</i></em>
										</c:if>
									</c:if>
								</c:when>
								<c:otherwise>
										<span class="ori">
										<c:if test="${ product.listPrice > product.pmsProductprice.salePrice}">
											<em><fmt:formatNumber type="currency" value="${product.listPrice}" pattern="###,###" /></em>
										</c:if>
										</span>
									<span class="price"><fmt:formatNumber type="currency" value="${product.pmsProductprice.salePrice}" pattern="###,###" /><i>원</i></span>
								</c:otherwise>
							</c:choose>
							
						</c:otherwise>
					</c:choose>
							
					<%-- 할인율 : 임직원, 멤버십, 쇼킹, 다자녀--%>
					<c:choose>
						<c:when test="${param.type eq 'multichild' or param.type eq 'officer'}">
							<div class="align_r">
								<tags:dcRate stdPrice="${product.listPrice }" dcPrice="${product.pmsProductprice.salePrice }"  var="rate" />
								<c:if test="${rate != 0  && rate != 100}">
									<em class="per">${rate }<i>%</i></em>
								</c:if>
							</div>
						</c:when>
						<c:otherwise>
						</c:otherwise>
					</c:choose>
					<%--// 할인율 --%>
					
					<c:if test="${param.type ne 'shocking' and param.type ne 'premium' and param.type ne 'officer' and !isMobile}">
						<div class="align_r">
							<button type="button" class="btn_blank" onclick="ccs.link.openWindow('${product.productId}');">새창 열기</button>
						</div>
					</c:if>
				</div>
				<!--// 가격  -->
				
				<%-- 프리미엄 멤버십관 한정수량 // 마감임박 --%>
				<c:if test="${param.type eq 'premium'}">
					<p class="quantity">
						<fmt:formatNumber value="${product.spsDealproduct.totalDealStockQty }" pattern="###,###"/> 개 한정  |  
						남은수량 <span class="point_pink"><fmt:formatNumber value="${product.spsDealproduct.dealStockQty }" pattern="###,###"/></span>개
					</p>
					<c:if test="${param.certify eq true and product.spsDealproduct.isCount eq 'Y'}">
						<span class="txt_time2">
							<small>남은시간</small>
							<i id="dealProduct${param.dealProductIndex}day"><script>special.premium.countdown("dealProduct${param.dealProductIndex}day", "${product.spsDealproduct.endDt}", "day")</script></i>
							<em id="dealProduct${param.dealProductIndex}time"><script>special.premium.countdown("dealProduct${param.dealProductIndex}time", "${product.spsDealproduct.endDt}", "time")</script></em>
						</span>
					</c:if>
				</c:if>
				<%-- // 프리미엄 멤버십관 한정수량 --%>
				
				<!-- 쇼킹제로 // -->
				<c:if test="${param.type eq 'shocking'}">
					<div class="stock">
						<span class="num">
							남은수량 <em><fmt:formatNumber value="${dealProduct.dealStockQty}" pattern="###,###" /></em>개
						</span>
						<button type="button" class="btn_blank" onclick="ccs.link.openWindow('${product.productId}');">새창 열기</button>
					</div>
				</c:if>
				<!--// 쇼킹제로  -->
			</div><%--// info end --%>
	</div>
</li>
