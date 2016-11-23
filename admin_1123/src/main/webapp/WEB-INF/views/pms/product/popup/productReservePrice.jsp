<%--
	화면명 : 상품 가격 예약 팝업
	작성자 : eddie
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.product.detail.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<div class="wrap_popup" data-ng-app="productDetailApp" data-ng-controller="pmsProductReservepriceController as ctrl">
<h1 class="sub_title1">가격변경예약</h1>



<div class="btn_alignR marginT2">
	<button type="button" class="btn_type1" data-ng-click="ctrl.addRow()">
		<b>예약변경 추가</b>
	</button>
	<button type="button" class="btn_type1" data-ng-click="ctrl.deleteGrid()">
		<b>신청내역 삭제</b>
	</button>					
	<button type="button" class="btn_type1" data-ng-click="ctrl.saveGrid()">
		<b><spring:message code="c.common.save" /><!-- 저장 --></b>
	</button>
</div>
<div class="box_type1 marginT1">
	<h3 class="sub_title2">가격 예약 목록
	</h3>	
	<div  class="gridbox gridBox300" id="gridArea">
		<div class="grid" data-ui-grid="myGrid"   
			data-ui-grid-move-columns 
			data-ui-grid-resize-columns 
			data-ui-grid-auto-resize 
			data-ui-grid-selection 
			data-ui-grid-row-edit
			data-ui-grid-cell-nav
			data-ui-grid-exporter
			data-ui-grid-edit 
			data-ui-grid-validate></div>
	</div>
</div>	
	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" data-ng-click="ctrl.close()">
			<b><spring:message code="c.common.close"/><!-- 확인 --></b>
		</button>
	</div>	
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>
		