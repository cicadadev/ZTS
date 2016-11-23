<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags" %>
<%@ taglib prefix="func" uri="kr.co.intune.commerce.common.functions" %>

<div class="tab_con tab_02 tab_conOn">
	<div class="viewTblList">
		<div class="div_tb_thead3">
			<div class="tr_box">
				<span class="col1">신청일시/신청번호</span> <!-- 16.10.08 : 텍스트변경 -->
				<span class="col2">상품명/옵션정보/수량</span>
				<span class="col3">상세</span>
			</div>
		</div>
		<ul class="div_tb_tbody3">

<c:choose>
 	<c:when test="${not empty regularList}">
		<c:forEach var="regular" varStatus="idx1" items="${regularList}">
			<fmt:parseDate value="${regular.insDt}" var="dateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
			<fmt:formatDate value="${dateFmt}" var="regularInsDt" pattern="yyyy-MM-dd"/>
					
			<li>
				<c:forEach var="product" varStatus="idx2" items="${regular.omsRegulardeliveryproducts}">
		
				<c:forEach var="schedule" varStatus="idx3" items="${product.omsRegulardeliveryschedules}">
					<c:if test="${idx3.first}">
						<div class="tr_box">
							<!-- 셀 병합이 필요한 경우 rowspan > cell > vAlign 필요 -->
							<div class="col1 rowspan">
								<div class="cell">
									<div class="vAlign orderNum">
										
										<b>${regularInsDt}</b>
										<b>
											<i>신청번호</i>
											<em>${regular.regularDeliveryId}</em>
										</b>
									</div>
								</div>
							</div>
							<c:if test="${fn:length(regular.omsRegulardeliveryproducts) > 1 }">
								<div class="colspan cols2">
							</c:if>
								<div class="col2">
									<div class="positionR">
										<c:set var="productId" value="${product.pmsProduct.productId}"/>
										<div class="prod_img">
											<a href="/pms/product/detail?productId=${productId}">
												<tags:prdImgTag productId="${productId}" seq="0" size="90" />
											</a>
										</div>
										<a  href="/pms/product/detail?productId=${productId}" class="title">
											${product.pmsProduct.name}
										</a>
										
										<c:if test="${product.deliveryProductTypeCd != 'DELIVERY_PRODUCT_TYPE_CD.SET'}">
											<em class="option_txt">
												<i>${product.pmsSaleproduct.name}</i>
											</em>
										</c:if>
						
										<div class="piece">
											<span class="pieceNum">${product.orderQty}개</span>
											<span class="slash">/</span>
											<span class="piecePrice">${func:price(product.regularDeliveryPrice,'')}<i>원</i></span>
										</div>
									</div>
								</div>
								<div class="col3">
									<a href="javascript:void(0);" class="btn_regularDetail"
										onclick="$regular.regularDetailLayer.open($(this));"
										data-regular-delivery-id = "${regular.regularDeliveryId}" 
										data-delivery-product-no = "${product.deliveryProductNo}" >
										회차별 내역 보기 &gt;</a>
									<%-- <a href="javascript:void(0);" class="btn_sStyle1" 
										onclick="myorder.order.change.regular($(this));"
										data-regular-delivery-id="${regular.regularDeliveryId}"
										data-regular-delivery-order="${regular.omsRegulardeliveryproducts[0].omsRegulardeliveryschedules[0].regularDeliveryOrder}">
										정기배송 설정변경</a> --%>
								</div>
							<c:if test="${fn:length(regular.omsRegulardeliveryproducts) > 1 }">
								</div>
							</c:if>
						</div>
					</c:if>
				</c:forEach>
				</c:forEach>
			</li>
		</c:forEach>
 	</c:when>
 	<c:otherwise>
 		<li class="noData_tp1">
			검색 결과가 없습니다.
		</li>
 	</c:otherwise>
</c:choose>		

		</ul>
	</div>
</div>
