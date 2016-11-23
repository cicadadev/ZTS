<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags" %>
<%@ taglib prefix="func" uri="kr.co.intune.commerce.common.functions" %>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY주문관리|취소/반품/교환 조회" name="pageNavi"/>
</jsp:include>
<div class="inner">
<div class="layout_type1">
<div class="column">
	<div class="myorder myorderDetail">
		<h3 class="title_type1">클레임 상세</h3>
		<div class="borderBox">
			<dl class="order_date">
				<dt>주문일시</dt>
				<dd>
					<b>${pickup.pickupReqDt}</b>
				</dd>
			</dl>
			<dl>
				<dt>신청번호</dt>
				<dd>
					<b>${pickup.pickupId}</b>
					<span class="icon_txt1 iconPink4">매장픽업</span>
				</dd>
			</dl>
		</div>
		<div class="non_info">
			<div class="slide_tit1 slideHide">
				<a href="javascript:void(0);" class="evt_tit">주문자정보</a>
			</div>
			<dl>
				<dt>${pickup.name1}</dt>
				<dd>
					<span>${pickup.phone2}</span>
					<span>${pickup.phone1}</span>
				</dd>
			</dl>
		</div>
		<div class="non_info non_prod">
			<div class="slide_tit1">
				<span class="normal_tit pc_only">상품정보</span>
				<a href="javascript:void(0);" class="evt_tit mo_only">상품정보</a>
			</div>

			<div class="div_tb_thead3">
				<div class="tr_box">
					<span class="col2">상품명/옵션정보/수량</span>
					<span class="col3">구매금액</span>
					<span class="col4">진행상태</span>
				</div>
			</div>
			<ul class="div_tb_tbody3">
				<li>
				
<c:forEach var="product" varStatus="idx2" items="${pickup.omsPickupproducts}">
	<div class="tr_box">
		<!-- 셀 병합이 필요한 경우 rowspan > cell > vAlign 필요 -->
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
					</div>
				</div>
			</div>
		</div>
	</div>
</c:forEach>

				</li>
			</ul>
		</div>
		<div class="btn_wrapC btn1ea">
			<a href="javascript:void(0);" class="btn_mStyle1 sPurple1" onclick="$order.search.history(2, 'claim');">취소/반품/교환 조회</a>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
</div>
</div>
<!-- ### //일반구매 ### -->
<script type="text/javascript">
<!--
	$(function() {
		$('.lnb ul:eq(0) li:eq(1)').addClass('on');
	});
//-->	
</script>