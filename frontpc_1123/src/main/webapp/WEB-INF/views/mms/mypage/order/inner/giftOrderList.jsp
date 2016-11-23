<%--
	화면명 : 선물 리스트
	작성자 : ALLEN
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript">
	$("[name=TOTAL_CNT]").val("${totalCount}");
</script>
<form name="orderForm">
</form>

<ul class="div_tb_tbody3">
<c:forEach items="${giftOrderList}"  var="order" varStatus="i">
	<fmt:parseDate value="${order.orderDt}" var="dateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
	<fmt:formatDate value="${dateFmt}" var="orderDt" pattern="yyyy/MM/dd"/>
	
	<fmt:parseDate value="${order.expireDt}" var="dateFmt2" pattern="yyyy-MM-dd HH:mm:ss"/>
	<fmt:formatDate value="${dateFmt2}" var="expireDt" pattern="yyyy/MM/dd"/>
	<li>
		<div class="tr_info">
			<span class="date">${orderDt}</span>
			<span>
				<a href="javascript:mypage.gifticon.giftOrderDetail('${order.orderId}');"><em>선물한 분</em> ${order.name1}(${order.memberId})</a>
			</span>
		</div>
		
		<c:forEach var="product" items="${order.omsOrderproducts}" varStatus="j">
			<c:if test="${product.orderProductTypeCd eq 'ORDER_PRODUCT_TYPE_CD.GENERAL' or product.orderProductTypeCd eq 'ORDER_PRODUCT_TYPE_CD.SET'}">
				<div class="tr_box">
					<div class="col1">
						<div class="positionR">
							<div class="prod_img">
								<a href="javascript:void(0);" onclick="javascript:ccs.link.product.detail('1164369','');">
									<tags:prdImgTag productId="${product.productId}" size="326"  />
								</a>
							</div>
		
							<a href="javascript:void(0);" onclick="javascript:ccs.link.product.detail('1164369','');" class="title">
	<%-- 							 [${product.brandName}]  --%>
								 ${product.productName}
							</a>
							
							
							<c:if test="${product.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SET'}">
								<c:forEach var="children" varStatus="idx3" items="${order.omsOrderproducts}">
									<c:if test="${children.orderId eq product.orderId && children.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SUB' && children.upperOrderProductNo == product.orderProductNo}">
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
												<b>${children.productName} : <span>${children.saleproductName}</span></b>
											</i>
											<i style="float: right;">(${children.setQty}개)</i>
										</em>
									</c:if>
								</c:forEach>
							</c:if>
							
		
							<em class="option_txt">
								<c:if test="${not empty product.saleproductName}">
									<i>${product.saleproductName}</i>
								</c:if>
							</em>
							
							<div class="piece">
								<span class="pieceNum">${product.orderQty}개</span>
							</div>
							<c:if test="${empty order.deliveryZipCd && product.orderProductStateCd ne 'ORDER_PRODUCT_STATE_CD.CANCEL'}">
								<p class="txt pc_only">선물 받을 배송지주소를 입력해주세요.</p>
							</c:if>
						</div>
					</div>
		
					<c:choose>
						<c:when test="${product.orderProductStateCd eq 'ORDER_PRODUCT_STATE_CD.CANCEL'}">
							<div class="col2">
								<div class="stateBox">
									<strong>주문취소</strong>
								</div>
							</div>
						</c:when>
						
						<c:when test="${product.orderProductStateCd eq 'ORDER_PRODUCT_STATE_CD.SHIP'}">
							<div class="col2">
								<div class="stateBox"
								data-order-id="${order.orderId}"
								data-order-product-no="1" 
								data-product-id="${product.productId}" 
								data-product-name="${product.productName}" 
								data-saleproduct-id="${product.saleproductId}"
								data-saleproduct-name="${product.saleproductName}">
									<strong>배송중</strong>
									<div class="group_r">
										<c:if test="${product.reviewYn eq 'N'}">
											<a href="javascript:void(0);" onclick="$order.update.review($(this));" class="btn_sStyle3 sGray2">상품평작성</a>
										</c:if>
										<a href="javascript:void(0);" class="btn_sStyle3 sGray2" onclick="$order.layer.delivery($(this));">배송조회</a>
									</div>
								</div>
							</div>
						</c:when>
						
						<c:when test="${product.orderProductStateCd eq 'ORDER_PRODUCT_STATE_CD.DELIVERY'}">
							<div class="col2">
								<div class="stateBox"
								data-order-id="${order.orderId}"
								data-order-product-no="1" 
								data-product-id="${product.productId}" 
								data-product-name="${product.productName}" 
								data-saleproduct-id="${product.saleproductId}"
								data-saleproduct-name="${product.saleproductName}">
									<strong>배송완료</strong>
									<div class="group_r">
										<c:if test="${product.reviewYn eq 'N'}">
											<a href="javascript:void(0);" onclick="$order.update.review($(this));" class="btn_sStyle3 sGray2">상품평작성</a>
										</c:if>
										<a href="javascript:void(0);" class="btn_sStyle3 sGray2" onclick="$order.layer.delivery($(this));">배송조회</a>
									</div>
								</div>
							</div>
						</c:when>
						
						<c:when test="${product.orderProductStateCd eq 'ORDER_PRODUCT_STATE_CD.DELIVERY_ORDER'}">
							<div class="col2">
								<div class="stateBox">
									<strong>상품준비중</strong>
									<div class="group_r">
										<a href="javascript:mypage.gifticon.giftOrderDetail('${order.orderId}');" class="btn_sStyle3 sGray2">배송지수정</a>
									</div>
								</div>
							</div>
						</c:when>
						<c:when test="${product.orderProductStateCd eq 'ORDER_PRODUCT_STATE_CD.CANCELAPPROVAL'}">
							<div class="col2">
								<div class="stateBox">
									<strong>주문완료</strong>
								</div>
							</div>
						</c:when>
						<c:when test="${product.orderProductStateCd eq 'ORDER_PRODUCT_STATE_CD.CANCELDELIVERY'}">
							<div class="col2">
								<div class="stateBox">
									<strong>상품준비중</strong>
								</div>
							</div>
						</c:when>
						<c:otherwise>
						</c:otherwise>
					</c:choose>
					<c:if test="${empty order.deliveryZipCd && product.orderProductStateCd ne 'ORDER_PRODUCT_STATE_CD.CANCEL'}">
						<div class="col2">
							<div class="stateBox">
								<strong>배송지입력대기
								<span>(유효기간 ~ ${expireDt})</span></strong>
								<p class="txt mo_only">선물 받을 배송지주소를 입력해주세요.</p>
								
									<a href="javascript:mypage.gifticon.giftOrderDetail('${product.orderId}');" class="btn_sStyle3 sGray2 btnDelivery">배송지 입력</a>
								</div>
						</div>
					</c:if>
				</div>
			</c:if>
		</c:forEach>
	</li>
</c:forEach>
<c:if test="${empty giftOrderList}">
	<p class="empty">받으신 선물이 없습니다.</p>
</c:if>
</ul>
<div class="paginateType1">
	<page:paging formId="orderForm" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
			total="${totalCount}" url="/mms/mypage/giftOrder/list/ajax" type="ajax" callback="listCallback"/>
</div>

<form>
