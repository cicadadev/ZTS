<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="func" uri="kr.co.intune.commerce.common.functions"%>
<!-- ### 배송조회 팝업 ### -->
<div class="pop_wrap ly_delivery" style="display: none;" id="deliveryTracking">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">배송조회</h3>
		</div>
		<div class="pop_content">
			<div class="viewTblList">
				<ul class="div_tb_tbody3">
					<li>
						<div class="tr_box">
							<div class="col1">
								<div class="positionR"></div>
							</div>
						</div>
					</li>
				</ul>
			</div>
			<div class="rw_tbBox">
				<ul class="rw_tb_tbody2">
					<li>
						<div class="tr_box">
							<div class="col1">
								<span class="group_inline">택배사</span>
							</div>
							<div class="col2">
								<div class="group_block">${logistics.deliveryServiceName}</div>
							</div>
						</div>
					</li>
					<li>
						<div class="tr_box">
							<div class="col1">
								<span class="group_inline">송장번호</span>
							</div>
							<div class="col2">
								<div class="group_block">${logistics.invoiceNo}</div>
							</div>
						</div>
					</li>
				</ul>
			</div>
			<h4 class="ly_tit1">배송정보</h4>
			<div class="tbl_article">
				<div class="div_tb_thead2">
					<div class="tr_box">
						<span class="col1">날짜</span>
						<span class="col2">위치</span>
						<span class="col3">담당자</span>
						<span class="col4">배송상태</span>
						<span class="col5">전화번호</span>
					</div>
				</div>
				<ul class="div_tb_tbody2">
					<c:choose>
						<c:when test="${!empty logistics && !empty logistics.omsDeliverytrackings}">
							<c:forEach var="tracking" varStatus="" items="${logistics.omsDeliverytrackings}">
								<li>
									<div class="tr_box">
										<div class="col1">
											<span>${tracking.deliveryServiceTime}</span>
										</div>
										<div class="col2">
											<span>${tracking.deliveryLocation}</span>
										</div>
										<div class="col3">
											<span>${tracking.senderName}</span>
										</div>
										<div class="col4">
											<span>${tracking.deliveryStepName}</span>
										</div>
										<div class="col5">
											<span>${tracking.deliverymanMobileNo}</span>
										</div>
									</div>
								</li>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<li>
								<div class="tr_box" style="text-align: center;">
									배송정보가 없습니다.
								</div>
							</li>
						</c:otherwise>
					</c:choose>
				</ul>
			</div>
		</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>
<!-- ### //배송조회 팝업 ### -->
