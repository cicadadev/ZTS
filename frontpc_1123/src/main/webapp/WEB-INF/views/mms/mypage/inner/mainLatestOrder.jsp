<%--
	화면명 : 마이페이지 > 메인 > 최근 주문
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="func" uri="kr.co.intune.commerce.common.functions" %>

<h3 class="sub_tit1">최근 주문내역</h3>
<a href="/mms/mypage/order/history" class="btn_all">일반 주문 전체보기</a>
<div class="tbl_article">
	<div class="div_tb_thead3">
		<div class="tr_box">
			<span class="col1">주문일시 / 주문번호</span>
			<span class="col2">상품명</span>
			<span class="col3">결제금액</span>
			<span class="col4">주문 / 배송현황</span>
		</div>
	</div>

	<ul class="div_tb_tbody3">
		<c:choose>
			<c:when test="${fn:length(orderList) < 1}">
			<li class="empty" style="height: 60px;">
				<div class="tr_box">
					<p style="display: block; text-align: center;">
						최근 주문 내역이 없습니다.
					</p>
				</div>
			</li>
			</c:when>
			
			<c:otherwise>
				<c:forEach items="${orderList }" var="order" varStatus="status">
				<li>
					<c:choose>
						<%-- 픽업 --%>
						<c:when test="${order.orderTypeCd eq '' or order.orderTypeCd eq null}">
							<c:forEach items="${order.omsPickupproducts }" var="product" varStatus="status">
								<div class="tr_box">
									<div class="col1 rowspan">
										<div class="cell">
											<div class="vAlign orderNum">
												<span class="icon_txt1 iconPink4">매장픽업</span>
												<b>${order.orderDt }</b>
												<b>
													<i>주문번호</i>
													<em>${order.orderId }</em>
												</b>
											</div>
										</div>
									</div>
									<!-- 셀 병합이 필요한 경우 rowspan > cell > vAlign 필요 -->
									<div class="colspan cols2">
										<div class="col2">
											<div class="pickup_shop">
												<em>${product.offshopName}</em>
												<a href="javascript:void(0);" class="btn_storeMap1" 
													onclick="mypage.offshop.offshopInfo('${product.offshopId}')">
													매장위치
												</a>
											</div>
											<div class="positionR">
												<div class="prod_img">
													<a href="/pms/product/detail?productId=${product.productId}">
														<tags:prdImgTag productId="${product.productId}" seq="0" size="326"  />
													</a>
												</div>
												<a href="/pms/product/detail?productId=${product.productId}" class="title">${product.productName}</a>
												<em class="option_txt">
													<i>${product.saleproductName}</i>
												</em>
												
												<div class="piece">
													<span class="pieceNum">${product.orderQty}개</span>
													<span class="slash">/</span>
													<span class="piecePrice">${func:price(product.orderAmt,'')}<i>원</i></span>
												</div>
											</div>
										</div>
										<div class="col3">
											<div class="cell">
												<span class="vAlign price">
													<em>${func:price(product.orderAmt * product.orderQty,'')}<i>원</i></em>
												</span>
											</div>
										</div>
										<div class="col4">
											<div class="cell">
												<div class="vAlign stateBox">					
													<strong>
														<tags:codeName code="${product.pickupProductStateCd}"/>
														<c:if test="${product.pickupProductStateCd == 'PICKUP_PRODUCT_STATE_CD.REQ' || product.pickupProductStateCd == 'PICKUP_PRODUCT_STATE_CD.READY'}">
															<i>(픽업예정일</i>
															<i>${product.pickupReserveDt})</i>
														</c:if>
														<c:if test="${product.pickupProductStateCd == 'PICKUP_PRODUCT_STATE_CD.DELIVERY'}">
															<i>(${product.pickupDeliveryDt})</i>
														</c:if>
														<c:if test="${product.pickupProductStateCd == 'PICKUP_PRODUCT_STATE_CD.CANCEL'}">
															<i>(${product.pickupCancelDt})</i>
														</c:if>
													</strong>
<!-- 													<span> -->
<%-- 														<c:if test="${product.pickupProductStateCd == 'PICKUP_PRODUCT_STATE_CD.REQ' || product.pickupProductStateCd == 'PICKUP_PRODUCT_STATE_CD.READY'}"> --%>
<!-- 															<a href="javascript:void(0);" class="btn_sStyle3 sGray2" -->
<%-- 																data-pickup-id="${pickup.pickupId}" --%>
<!-- 																data-claim-type="pickup" -->
<!-- 																onclick="$claim.cancel($(this));">신청취소</a> -->
<%-- 														</c:if> --%>
<%-- 														<c:if test="${product.pickupProductStateCd == 'PICKUP_PRODUCT_STATE_CD.DELIVERY'}"> --%>
<!-- 															<a href="javascript:void(0);" class="btn_sStyle3 sGray2" onclick="$order.update.review($(this));">상품평작성</a> -->
<%-- 														</c:if>									 --%>
<!-- 													</span> -->
												</div>
											</div>
										</div>
									</div>
								</div>
							</c:forEach>
						</c:when>
						<%-- // 픽업 --%>
						<%-- 일반, 정기배송 --%>
						<c:otherwise>
							<c:forEach items="${order.omsOrderproducts }" var="product" varStatus="status">
								<c:if test="${fn:contains('ORDER_PRODUCT_TYPE_CD.GENERAL,ORDER_PRODUCT_TYPE_CD.SET,ORDER_PRODUCT_TYPE_CD.WRAP', product.orderProductTypeCd)}">
								<div class="tr_box">
									<div class="col1 rowspan">
										<div class="cell">
											<div class="vAlign orderNum">
												<c:if test="${order.orderTypeCd == 'ORDER_TYPE_CD.REGULARDELIVERY'}">
													<span class="icon_txt1 iconPurple4_1">정기배송</span>
												</c:if>
												<c:if test="${order.orderTypeCd == 'ORDER_TYPE_CD.GIFT'}">
													<span class="icon_txt1 iconPurple4_2">기프티콘</span>
												</c:if>
												<b>${order.orderDt }</b>
												<b>
													<i>주문번호</i>
													<em>${order.orderId }</em>
												</b>
											</div>
										</div>
									</div>
									<div class="colspan cols2">
										<div class="col2">
											<div class="positionR">
												<c:if test="${product.orderProductTypeCd != 'ORDER_PRODUCT_TYPE_CD.WRAP'}">
													<div class="prod_img">
														<a href="javascript:void(0);">
															<tags:prdImgTag productId="${product.productId}" size="90" alt="" />
														</a>
													</div>
													<a href="javascript:void(0);" class="title">${product.productName}</a>
													<c:if test="${product.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SET'}">
														<c:forEach var="children" varStatus="idx3" items="${order.omsOrderproducts }">
															<c:if test="${children.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SUB' && children.upperOrderProductNo == product.orderProductNo}">
																<em class="option_txt">
																	<i>
																		<b>
																			<c:if test="${children.orderDeliveryTypeCd != 'ORDER_DELIVERY_TYPE_CD.ORDER'}">
																				<span style="color: red;">(${children.orderDeliveryTypeName})</span> 
																			</c:if>
																			${children.productName} : <span>${children.saleproductName}</span>
																		</b>
																	</i>
																	<i style="float: right;">(${children.setQty}개)</i>
																</em>
															</c:if>
														</c:forEach>
													</c:if>
													<c:if test="${product.orderProductTypeCd != 'ORDER_PRODUCT_TYPE_CD.SET'}">
														<em class="option_txt">
															<i>${product.saleproductName}</i>
														</em>
													</c:if>
												</c:if>
												<c:if test="${product.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.WRAP'}">
													<div class="prod_img">
														<tags:prdImgTag productId="${product.productId}" size="90" alt="" />
													</div>
													선물포장지
												</c:if>												
		
		
												<div class="piece">
													<span class="pieceNum">${product.orderQty}개</span>
													<span class="slash">/</span>
													<span class="piecePrice">${func:price(product.totalSalePrice,'')}<i>원</i></span>
												</div>
											</div>
											<%-- 상품 사은품 정보 --%>
											<c:forEach var="children" varStatus="idx3" items="${order.omsOrderproducts}">
												<c:if test="${children.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.PRODUCTPRESENT' && children.upperOrderProductNo == product.orderProductNo}">
													<u class="gift_txt">
														<span class="btn_tb_gift">
															<span class="icon_type1 iconBlue3">사은품</span>
															${children.productName}
														</span>
													</u>
												</c:if>
											</c:forEach>
											<%-- 선물포장 정보 --%>
											<c:if test="${product.wrapYn == 'Y' && product.orderProductTypeCd != 'ORDER_PRODUCT_TYPE_CD.WRAP'}">
												<u class="gift_txt">
													<span class="btn_tb_gift">
														<span class="icon_gift">선물포장</span>
														<small>신청</small>
													</span>
												</u>
											</c:if>
										</div>
										<div class="col3">
											<span class="price">
												<em>${func:price(product.totalSalePrice * product.orderQty,'')}<i>원</i></em>
											</span>
										</div>
										<div class="col4">
											<div class="stateBox">
												<strong>${product.orderProductStateName}</strong>
											</div>
										</div>
									</div>
								</div>
							</c:if>
							</c:forEach>
							<c:set var="promotionIds" value="" />
							<c:set var="orderWrapFee" value="0"/>
							<c:forEach var="product2" varStatus="idx2" items="${order.omsOrderproducts}">
								<c:if test="${product2.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.WRAP'}"><c:set var="orderWrapFee" value="${orderWrapFee + product2.totalSalePrice * product2.orderQty}"/></c:if>
								<c:if test="${product2.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.ORDERPRESENT'}">
									<c:if test="${!fn:contains(promotionIds, product2.presentId) && !empty product2.presentId}">
										<c:set var="promotionIds">${promotionIds},${product2.presentId}</c:set>
										<div class="tr_box tr_promo">
											<div class="promotion">
												<strong>${product2.presentName}</strong>
												<ul>
													<c:forEach var="product3" varStatus="idx3" items="${order.omsOrderproducts}">
														<c:if test="${product2.presentId == product3.presentId}">
															<li>
																<tags:prdImgTag productId="${product3.productId}" seq="0" size="60"  />
																<span>
																	<em>${product3.productName}</em>
																</span>
															</li>
														</c:if>
													</c:forEach>
												</ul>
											</div>
										</div>
									</c:if>
								</c:if>
							</c:forEach>
						</c:otherwise>
						<%-- // 일반 --%>
					</c:choose>
					</li>
				</c:forEach><%-- // 최근 7일 주문 마스터 리스트 --%>
			</c:otherwise>
			
		</c:choose>
	</ul>
</div>