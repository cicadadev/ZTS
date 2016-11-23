<%--
	화면명 : 주문상품 검색 리스트
	작성자 : 로이
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<ul class="div_tb_tbody3" id="orderlist">
<c:choose>
	<c:when test="${!empty list}">
		<c:forEach items="${list}" var="list">
		<li>
			<div class="tr_info">
				<span class="date"> <em>주문일 : </em>${list.orderDt }
				</span> <span> <em>주문번호 : </em>${list.orderId }
				</span>
			</div>
			<div class="tr_box">
				<div class="col1">
					<div class="positionR">
						<div class="prod_img">
							<a href="#none" onclick="javascript:ccs.link.product.detail('${list.productId}')">
								<tags:prdImgTag productId="${list.productId}" seq="0" size="326"  />
							</a>
						</div>
						
						<a href="#none" class="title"> ${list.productName }</a>
					</div>
				</div>

				<div class="col2">
					<div class="stateBox">
						<strong><tags:codeName code="${ list.orderStateCd }"/></strong> 
						<a href="javascript:ccs.layer.orderProductLayer.select('${list.orderId}', '${list.productId}', '${list.productName}', '${list.saleproductId}', '${list.saleproductName}');" class="btn_sStyle3 sGray2">선택</a>
					</div>
				</div>

				<div class="col3">
					<span class="price"> <em>${list.orderId } <i>원</i></em>
					</span>
				</div>
			</div>
		</li>
	</c:forEach>
	</c:when>
	<c:otherwise>
		<li class="empty">
			<div class="tr_box">
				<div class="col99">
					검색 조건에 맞는 주문상품이 없습니다.
				</div>
			</div>
		</li>
	</c:otherwise>
</c:choose>
</ul>
		