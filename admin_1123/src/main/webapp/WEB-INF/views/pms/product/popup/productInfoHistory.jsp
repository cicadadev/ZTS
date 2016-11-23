<%--
	화면명 : 상품 상세 > 상상품 정보 예약 변경 내역 팝업
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.product.detail.js"></script>

<div class="wrap_popup" data-ng-app="productDetailApp" data-ng-controller="pmsProductHistoryController as ctrl">		
	<form name="form2">
		<h2 class="sub_title1">상품 정보 예약 변경 내역</h2>
		
		<div class="box_type1 marginT1">
			<h3 class="sub_title2">
				<span><spring:message code="c.search.totalCount" arguments="{{ grid1.totalItems }}"></spring:message></span>
			</h3>
			
			<div class="gridbox">
				<div class="grid" data-ui-grid="grid1"   
						data-ui-grid-move-columns 
						data-ui-grid-resize-columns 
						data-ui-grid-pagination
						data-ui-grid-auto-resize 
						data-ui-grid-exporter 
						data-ui-grid-edit 
						data-ui-grid-selection
						data-ui-grid-validate></div>
				</div>
		</div>
	</form>
</div>				
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>