<%--
	화면명 : 정기배송상세 팝업  레이어
	작성자 : roy
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="func" uri="kr.co.intune.commerce.common.functions" %>
<%@page import="java.util.*"%>
<!-- ### 16.10.07 : 정기배송상세 팝업 ### -->
<div class="pop_wrap ly_regular_detail" id="regularDetailLayer">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">정기배송 상세</h3>
		</div>
		<div class="pop_content">
		<c:forEach var="regular" varStatus="idx1" items="${regularList}">
		<fmt:parseDate value="${regular.insDt}" var="dateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
		<fmt:formatDate value="${dateFmt}" var="regularInsDt" pattern="yyyy-MM-dd"/>
	
			<div class="borderBox">
				<dl>
					<dt>주문일시</dt>
					<dd>
						<b>${regularInsDt}</b>
					</dd>
				</dl>
				<dl>
					<dt>신청번호</dt>
					<dd>
						<b>${regular.regularDeliveryId}</b>
					</dd>
				</dl>
			</div>
			
			<div class="non_info">
				<div class="slide_tit1">
					<span class="normal_tit">배송지 정보</span>
				</div>
				<dl>
					<dt>${regular.name1}</dt>
					<dd>
						<c:if test="${not empty regular.phone2}">
							${regular.phone2} 
							<c:if test="${not empty regular.phone1}">
							| 
							</c:if>
						</c:if>
						<c:if test="${not empty regular.phone1}">
							${regular.phone1}
						</c:if>
						<br />
						<em>(${regular.deliveryZipCd}) ${regular.deliveryAddress1}</em><br />
						${regular.note}
					</dd>
				</dl>
			</div>
			
			<div class="viewTblList">
				<ul class="div_tb_tbody3">
					<li>
						<div class="tr_box">
							<div class="col1">
								<div class="positionR">
			<c:forEach var="product" varStatus="idx2" items="${regular.omsRegulardeliveryproducts}">
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
									<!-- <strong class="itemPrice">22,500원</strong> 16.10.07 : 삭제 -->
									<!-- 16.10.07 : 추가 -->
									<div class="piece">
										<span class="pieceNum">${product.orderQty}개</span>
										<span class="slash">/</span>
										<span class="piecePrice">${func:price(product.regularDeliveryPrice,'')}<i>원</i></span>
									</div>
									<!-- //16.10.07 : 추가 -->
								</div>
			
								<ul class="regular_list">
	
				<c:forEach var="schedule" varStatus="idx3" items="${shceduleList}">
									<li>
										<div class="left">${schedule.regularDeliveryOrder}회차 (${schedule.regularDeliveryDt})</div>
										<div class="right"><tags:codeName code="${ schedule.deliveryScheduleStateCd }"/></div>
									</li>
				</c:forEach>
			
			</c:forEach>
		</c:forEach>
		
								</ul>
							</div>
						</div>
					</li>
				</ul>
			</div>

			<div class="btn_wrapC btn1ea">
				<a href="#none" class="btn_mStyle1 sPurple1" onclick="javascript:ccs.layer.regularDetailLayer.cancle();">확인</a>
			</div>
		</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>
<!-- ### //16.10.07 : 정기배송상세 팝업 ### -->