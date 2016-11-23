<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags" %>
<%@ taglib prefix="func" uri="kr.co.intune.commerce.common.functions" %>

<!-- ### 일반구매 ### -->
<div class="tab_con tab_conOn">
	<div class="div_tb_thead3">
		<div class="tr_box">
			<span class="col1">신청일자/클레임번호</span>
			<span class="col2">상품명/옵션정보/수량</span>
			<span class="col3">구매금액</span>
			<span class="col4">진행상태</span>
		</div>
	</div>
	<ul class="div_tb_tbody3">
			
<!-- 주문/배송 내역이 없을 경우 -->
<c:if test="${fn:length(claimList) < 1}">
	<li class="empty">
		<div class="tr_box">
			<div class="td_box col99">
<!-- 				최근 1개월 간 진행중인 주문/배송조회 내역이 없습니다. -->
				클레임 내역이 없습니다.
			</div>
		</div>
	</li>
</c:if>
<c:if test="${fn:length(claimList) > 0}">
	<c:set var="trIdx" value="0"/>
	<c:forEach var="claim" varStatus="idx1" items="${claimList}">
	<li>
	
<c:forEach var="cp" varStatus="idx2" items="${claim.omsClaimproducts}">
	<c:if test="${fn:contains('ORDER_PRODUCT_TYPE_CD.GENERAL,ORDER_PRODUCT_TYPE_CD.SET,ORDER_PRODUCT_TYPE_CD.SUB,ORDER_PRODUCT_TYPE_CD.WRAP', cp.omsOrderproduct.orderProductTypeCd)}">
		<c:set var="trIdx" value="${trIdx + 1}"/>
		<div class="tr_box tr_idx ${trIdx}">
			<!-- 셀 병합이 필요한 경우 rowspan > cell > vAlign 필요 -->
			<div class="col1 rowspan">
				<div class="cell">
					<div class="vAlign orderNum">
						<b>${claim.reqDt}</b>
						<b><i>클레임번호</i><em>${claim.orderId}_${claim.claimNo}</em></b>
						<a href="/mms/mypage/claim/general/${claim.orderId}/${claim.claimNo}">상세보기</a>
						<c:if test="${claim.omsOrder.orderTypeCd == 'ORDER_TYPE_CD.REGULARDELIVERY' }">
							<span class="icon_txt1 iconPurple4_1">정기배송</span>
						</c:if>
						<c:if test="${claim.omsOrder.orderTypeCd == 'ORDER_TYPE_CD.GIFT'}">
							<span class="icon_txt1 iconPurple4_2">기프티콘</span>
						</c:if>
					</div>
				</div>
			</div>
			<div class="colspan cols2">
				<div class="col2">
					<div class="positionR">
						<div class="prod_img">
							<c:if test="${cp.omsOrderproduct.orderDeliveryTypeCd == 'ORDER_DELIVERY_TYPE_CD.ORDER'}">
								<a href="/pms/product/detail?productId=${cp.omsOrderproduct.productId}">
									<tags:prdImgTag productId="${cp.omsOrderproduct.productId}" seq="0" size="90" />
								</a>
							</c:if>
							<c:if test="${cp.omsOrderproduct.orderDeliveryTypeCd != 'ORDER_DELIVERY_TYPE_CD.ORDER'}">
								<u class="gift_txt">
									<span class="btn_tb_gift">
										<span class="icon_type1 iconBlue3">${cp.omsOrderproduct.orderDeliveryTypeName}</span>
									</span>
								</u>
							</c:if>
						</div>
						<a href="/pms/product/detail?productId=${cp.omsOrderproduct.productId}" class="title">${cp.omsOrderproduct.productName}</a>
						<c:if test="${cp.omsOrderproduct.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SET'}">
							<c:forEach var="children" varStatus="idx3" items="${claim.omsClaimproducts}">
								<c:if test="${children.omsOrderproduct.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SUB' 
									&& children.omsOrderproduct.upperOrderProductNo == cp.omsOrderproduct.orderProductNo}">
									<em class="option_txt">
										<i>
											<b>${children.omsOrderproduct.productName} : <span>${children.omsOrderproduct.saleproductName}</span></b>
										</i>
										<i style="float: right;">(${children.omsOrderproduct.setQty}개)</i>
									</em>
								</c:if>
							</c:forEach>
						</c:if>
						<c:if test="${cp.omsOrderproduct.orderProductTypeCd != 'ORDER_PRODUCT_TYPE_CD.SET'}">
							<em class="option_txt">
								<i>${cp.omsOrderproduct.saleproductName}</i>
							</em>
						</c:if>
						
						<div class="piece">
							<span class="pieceNum">${cp.omsOrderproduct.orderQty}개</span>
							<span class="slash">/</span>
							<span class="piecePrice">${func:price(cp.omsOrderproduct.totalSalePrice,'')}<i>원</i></span>
						</div>
					</div>
					<%-- 상품 사은품 정보 --%>
					<c:forEach var="children" varStatus="idx3" items="${claim.omsClaimproducts}">
						<c:if test="${children.omsOrderproduct.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.PRODUCTPRESENT' 
							&& children.omsOrderproduct.upperOrderProductNo == cp.omsOrderproduct.orderProductNo}">
							<u class="gift_txt">
								<span class="btn_tb_gift">
									<span class="icon_type1 iconBlue3">사은품</span>
									${children.omsOrderproduct.productName}
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
						<em>${func:price(cp.omsOrderproduct.totalSalePrice * cp.omsOrderproduct.orderQty,'')}<i>원</i></em>
					</span>
				</div>
				<div class="col4">
					<div class="stateBox">
						<strong>
							<c:if test="${cp.omsOrderproduct.orderDeliveryTypeCd == 'ORDER_DELIVERY_TYPE_CD.ORDER'}">
								${cp.claimProductStateName}
							</c:if>
							<c:if test="${cp.omsOrderproduct.orderDeliveryTypeCd != 'ORDER_DELIVERY_TYPE_CD.ORDER'}">
								${cp.omsOrderproduct.orderProductStateNote}
							</c:if>
						</strong>
					</div>
				</div>
			</div>
		</div><!-- end tr_box -->
	</c:if>
</c:forEach>
<c:set var="promotionIds" value="" />
<c:forEach var="cp2" varStatus="idx2" items="${claim.omsClaimproducts}">
	<c:if test="${cp2.omsOrderproduct.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.ORDERPRESENT'}">
		<c:if test="${!fn:contains(promotionIds, cp2.omsOrderproduct.presentId) && !empty cp2.omsOrderproduct.presentId}">
			<c:set var="promotionIds">${promotionIds},${cp2.omsOrderproduct.presentId}</c:set>
			<div class="tr_box tr_promo">
				<div class="promotion">
					<strong><span class="icon_type1 iconPurple2">${claim.claimTypeName}사은품</span>${cp2.omsOrderproduct.presentName}</strong>
					<ul>
						<c:forEach var="cp3" varStatus="idx3" items="${claim.omsClaimproducts}">
							<c:if test="${cp2.omsOrderproduct.presentId == cp3.omsOrderproduct.presentId}">
								<c:if test="${(claim.claimTypeCd == 'CLAIM_TYPE_CD.CANCEL' && cp3.omsOrderproduct.cancelQty > 0) 
											|| (claim.claimTypeCd == 'CLAIM_TYPE_CD.RETURN' && cp3.omsOrderproduct.returnQty > 0)}">
									<li>
										<tags:prdImgTag productId="${cp3.omsOrderproduct.productId}" seq="0" size="60"  />
										<span>
											<em>${cp3.omsOrderproduct.productName}</em>
										</span>
									</li>
								</c:if>
							</c:if>
						</c:forEach>
					</ul>
				</div>
			</div>
		</c:if>
	</c:if>
</c:forEach>

	</li>
	</c:forEach>
</c:if>
			
	</ul>
</div>
<!-- ### //일반구매 ### -->
<script type="text/javascript">
</script>