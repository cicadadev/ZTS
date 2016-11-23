<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags" %>
<%@ taglib prefix="func" uri="kr.co.intune.commerce.common.functions" %>
<!-- ### 매장픽업 ### -->
<div class="tab_con tab_conOn">
	<div class="column_3set">
		<div class="select_box1">
			<label>'시/도'</label>
			<select id="area1" onchange="$order.search.pickup($(this));">
			<option value="">시/도</option>
				<c:forEach var="area1" varStatus="idx1" items="${pickupAreaList}">
					<option value="${area1.areaDiv1}">
						${area1.areaDiv1}
					</option>
				</c:forEach>
			</select>
		</div>
		<div class="select_box1">
			<label>시/군/구</label>
			<select id="area2" onchange="$order.search.pickup($(this));">
				<option value="">시/군/구</option>
				<c:forEach var="area1" varStatus="idx1" items="${pickupAreaList}">
					<c:forEach var="area2" varStatus="idx2" items="${area1.ccsOffshops}">
						<option value="${area2.areaDiv2}" data-area1="${area1.areaDiv1}">
							${area2.areaDiv2}
						</option>
					</c:forEach>
				</c:forEach>
			</select>
		</div>
		
		<div class="select_box1">
			<label>매장</label>
			<select id="offshopId" onchange="$order.search.pickup($(this));">
				<option value="">매장</option>
				<c:forEach var="area1" varStatus="idx1" items="${pickupAreaList}">
					<c:forEach var="area2" varStatus="idx2" items="${area1.ccsOffshops}">
						<c:forEach var="area3" varStatus="idx3" items="${area2.ccsOffshops}">
							<option value="${area3.offshopId}" data-area1="${area1.areaDiv1}" data-area2="${area2.areaDiv2}">
								${area3.name}
							</option>
						</c:forEach>
					</c:forEach>
				</c:forEach>
			</select>
		</div>
		
	</div>
	
	<div class="div_tb_thead3">
		<div class="tr_box">
			<span class="col1">신청일자/신청번호</span>
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
				매장픽업 취소 내역이 없습니다.
			</div>
		</div>
	</li>
</c:if>
<c:if test="${fn:length(claimList) > 0}">
	<c:forEach var="pickup" varStatus="idx1" items="${claimList}">
	<li>
	
<c:forEach var="product" varStatus="idx2" items="${pickup.omsPickupproducts}">
	<div class="tr_box">
		<!-- 셀 병합이 필요한 경우 rowspan > cell > vAlign 필요 -->
		<div class="col1 rowspan">
			<div class="cell">
				<div class="vAlign orderNum">
					<b>${product.pickupReqDt}</b>
					<b><i>신청번호</i><em>${product.pickupId}</em></b>
					<%--
					<a href="/mms/mypage/claim/pickup/${product.pickupId}/" class="pc_only">상세보기</a>
					--%>
					<span class="icon_txt1 iconPink4">매장픽업</span>
				</div>
			</div>
		</div>
		<div class="colspan cols2">
			<div class="col2">
				<div class="pickup_shop">
					<em>${product.ccsOffshop.name}</em>
					<a href="javascript:void(0);" class="btn_storeMap1" 
						onclick="mypage.offshop.offshopInfo('${product.ccsOffshop.offshopId}')">
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
							${product.pickupProductStateName}
						</strong>
					</div>
				</div>
			</div>
		</div>
	</div>
</c:forEach>

	</li>
	</c:forEach>	
</c:if>
	
	</ul>
</div>
<!-- ### 매장픽업 ### -->
<script type="text/javascript">
<!--
$order.setdata.pickup('${orderSearch.area1}','${orderSearch.area2}','${orderSearch.offshopId}');
//-->	
</script>