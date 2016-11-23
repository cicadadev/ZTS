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
			<span class="col1">주문일자/주문번호</span>
			<span class="col2">상품명/옵션정보/수량</span>
			<span class="col3">구매금액</span>
			<span class="col4">진행상태</span>
		</div>
	</div>
	<ul class="div_tb_tbody3">
			
<!-- 주문/배송 내역이 없을 경우 -->
<c:if test="${fn:length(orderList) < 1}">
	<li class="empty">
		<div class="tr_box">
			<div class="td_box col99">
<!-- 				최근 1개월 간 진행중인 주문/배송조회 내역이 없습니다. -->
				주문/배송조회 내역이 없습니다.
			</div>
		</div>
	</li>
</c:if>
<c:if test="${fn:length(orderList) > 0}">
	<c:set var="trIdx" value="0"/>
	<c:forEach var="order" varStatus="idx1" items="${orderList}">
	<li>
	
<c:set var="changeDelivery" value="${true}"/>
<c:forEach var="product" varStatus="idx2" items="${order.omsOrderproducts}">
	<c:if test="${product.deliveryChangeYn != 'Y'}"><c:set var="changeDelivery" value="${false}"/></c:if>
</c:forEach>
<c:forEach var="product" varStatus="idx2" items="${order.omsOrderproducts}">
	<c:if test="${fn:contains('ORDER_PRODUCT_TYPE_CD.GENERAL,ORDER_PRODUCT_TYPE_CD.SET,ORDER_PRODUCT_TYPE_CD.WRAP', product.orderProductTypeCd)}">
		<c:set var="trIdx" value="${trIdx + 1}"/>
		<c:set var="isSet" value="${false}"/>
		<div class="tr_box tr_idx ${trIdx}">
			<!-- 셀 병합이 필요한 경우 rowspan > cell > vAlign 필요 -->
			<div class="col1 rowspan">
				<div class="cell">
					<div class="vAlign orderNum">
						<b>${order.orderDt}</b>
						<b><i>주문번호</i><em>${order.orderId}</em></b>
						<a href="/mms/mypage/order/regular/${order.orderId}">상세보기</a>
						<c:if test="${order.orderTypeCd == 'ORDER_TYPE_CD.REGULARDELIVERY'}">
							<span class="icon_txt1 iconPurple4_1">정기배송</span>
						</c:if>
						<c:if test="${order.orderTypeCd == 'ORDER_TYPE_CD.GIFT'}">
							<span class="icon_txt1 iconPurple4_2">기프티콘</span>
						</c:if>
					</div>
				</div>
			</div>
			<div class="colspan cols2">
				<div class="col2">
					<div class="positionR">
						<c:if test="${product.orderProductTypeCd != 'ORDER_PRODUCT_TYPE_CD.WRAP'}">
							<div class="prod_img">
								<c:if test="${product.orderDeliveryTypeCd == 'ORDER_DELIVERY_TYPE_CD.ORDER'}">
									<a href="/pms/product/detail?productId=${product.productId}">
										<tags:prdImgTag productId="${product.productId}" seq="0" size="90" />
									</a>
								</c:if>
								<c:if test="${product.orderDeliveryTypeCd != 'ORDER_DELIVERY_TYPE_CD.ORDER'}">
									<u class="gift_txt">
										<span class="btn_tb_gift">
											<span class="icon_type1 iconBlue3">${product.orderDeliveryTypeName}</span>
										</span>
									</u>
								</c:if>
							</div>
							<a href="/pms/product/detail?productId=${product.productId}" class="title">${product.productName}</a>
							<c:if test="${product.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SET'}">
								<c:set var="isSet" value="${true}"/>
								<c:forEach var="children" varStatus="idx3" items="${order.omsOrderproducts}">
									<c:if test="${children.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SUB' && children.upperOrderProductNo == product.orderProductNo}">
										<em class="option_txt" 
											data-order-id="${order.orderId}"
											data-order-product-no="${children.orderProductNo}"
											data-product-id="${children.productId}"
											data-saleproduct-id="${children.saleproductId}"
											data-add-sale-price="${children.addSalePrice}"
					
											data-order-qty="${children.orderQty}"
											data-cancel-qty="${children.cancelQty}"
											data-return-qty="${children.returnQty}">
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
								<em class="option_txt"
									data-order-id="${order.orderId}"
									data-order-product-no="${product.orderProductNo}"
									data-product-id="${product.productId}"
									data-saleproduct-id="${product.saleproductId}"
									data-add-sale-price="${product.addSalePrice}"
			
									data-order-qty="${product.orderQty}"
									data-cancel-qty="${product.cancelQty}"
									data-return-qty="${product.returnQty}">
									<i>${product.saleproductName}</i>
								</em>
							</c:if>
						</c:if>
						<c:if test="${product.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.WRAP'}">
							<div class="prod_img">
								<c:if test="${product.orderDeliveryTypeCd == 'ORDER_DELIVERY_TYPE_CD.ORDER'}">
									<tags:prdImgTag productId="${product.productId}" seq="0" size="90" />
								</c:if>
								<c:if test="${product.orderDeliveryTypeCd != 'ORDER_DELIVERY_TYPE_CD.ORDER'}">
									<u class="gift_txt">
										<span class="btn_tb_gift">
											<span class="icon_type1 iconBlue3">${product.orderDeliveryTypeName}</span>
										</span>
									</u>
								</c:if>
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
					<div class="stateBox"
						data-order-id="${order.orderId}"
						data-order-product-no="${product.orderProductNo}"
						data-order-product-type-cd="${product.orderProductTypeCd}"
						data-product-id = "${product.productId}"
						data-product-name = "${product.productName}"
						data-saleproduct-id = "${product.saleproductId}"
						data-saleproduct-name = "${product.saleproductName}"
						data-tr-idx="${trIdx}">
						<strong>
							${product.orderProductStateNote}
							<c:if test="${order.orderTypeCd == 'ORDER_TYPE_CD.GIFT' && order.orderStateCd == 'ORDER_STATE_CD.PAYED'}">
								<i>(배송지입력대기)</i>
							</c:if>
						</strong>
						<c:if test="${product.orderProductTypeCd != 'ORDER_PRODUCT_TYPE_CD.WRAP'}">
							<div class="group_r">
								<c:if test="${product.optionChangeYn == 'Y'}">
									<a href="javascript:void(0);" class="btn_sStyle3 sGray2 btn_tb_option" onclick="$order.change.option($(this));">
										옵션 변경
									</a>
									<div class="ly_box option_box"></div>
								</c:if>
								<c:if test="${changeDelivery}">
	<!-- 										<a href="javascript:void(0);" class="btn_sStyle3 sGray2" onclick="$order.change.delivery();">배송지 변경</a> -->
								</c:if>
								<c:if test="${product.cancelYn == 'Y'}">
									<a href="javascript:void(0);" class="btn_sStyle3 sGray2"
										data-claim-type="cancel"
										data-is-partial="${product.partCancelYn}"
										data-is-set="${isSet}"
										onclick="$claim.request($(this));">주문취소</a>
								</c:if>
								<c:if test="${product.outQty > product.returnQty}">
									<c:if test="${product.trackingYn == 'Y'}">
										<a href="javascript:void(0);" class="btn_sStyle3 sGray2" onclick="$order.layer.delivery($(this));">배송조회</a>
									</c:if>
									<c:if test="${product.reviewYn == 'Y'}">
										<a href="javascript:void(0);" class="btn_sStyle3 sGray2" onclick="$order.update.review($(this));">상품평작성</a>
									</c:if>
									<c:if test="${product.returnYn == 'Y'}">
										<a href="javascript:void(0);" class="btn_sStyle3 sGray2"
											data-claim-type="exchange"
											onclick="$claim.request($(this));">교환신청</a>
										<a href="javascript:void(0);" class="btn_sStyle3 sGray2"
											data-claim-type="return"
											data-is-partial="${product.partCancelYn}"
											data-is-set="${isSet}"
											onclick="$claim.request($(this));">반품신청</a>
									</c:if>
								</c:if>
							</div>
						</c:if>
					</div>
				</div>
			</div>
		</div><!-- end tr_box -->
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

	</li>
	</c:forEach>
</c:if>
			
	</ul>
</div>
<!-- ### //일반구매 ### -->
<script type="text/javascript">

</script>