<%--
	화면명 : 프로모션관리 > 이벤트 관리 > 이벤트 응모정보 팝업
	작성자 : peter
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/sps.app.event.list.js"></script>

<div class="wrap_popup" ng-app="eventApp" ng-controller="sps_eventJoinlistPopApp_controller as ctrl">
	<ul class="tab_type2">
		<li class="">
			<button type="button" ng-click="ctrl.moveTab($event, 'detail')" name="detail"><spring:message code="c.sps.event.subtitle2" /></button>
		</li>
		<li class="on">
			<button type="button" ng-click="ctrl.moveTab($event, 'joinlist')" name="joinlist"><spring:message code="c.sps.event.subtitle5" /></button>
		</li>
	</ul>

	<div class="btn_alignR marginT2">
		<button type="submit" class="btn_type1" ng-click="ctrl.batchWinnerUpload()" fn-id="13_UPLOAD">
			<b><spring:message code="c.sps.eventJoin.btn.allUpload" /></b>
		</button>
		<button type="submit" class="btn_type1" ng-click="myGrid.initGrid()">
			<b><spring:message code="c.common.cancel" /></b>
		</button>
		<button type="submit" class="btn_type1" ng-click="ctrl.saveGridData()" fn-id="13_UPDATE">
			<b><spring:message code="c.common.save" /></b>
		</button>
	</div>

	<div class="box_type1 marginT1">
		<div class="tb_type1">
			<h3 class="sub_title2">
				<spring:message code="c.sps.event.subtitle6" />
				<span><spring:message code="c.search.totalCount" arguments="{{ grid_joinEvent.totalItems }}" /></span>
			</h3>

			<div class="tb_util tb_util_rePosition">
				<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()" fn-id="13_EXCEL1">엑셀받기</button>
			</div>

			<div class="gridbox">
				<div class="grid" ui-grid="grid_joinEvent" 
					ui-grid-validate 
					ui-grid-selection 
			 		ui-grid-exporter 
			 		ui-grid-pagination 
			 		ui-grid-edit 
			 		ui-grid-row-edit 
			 		ui-grid-cell-nav 
			 		ui-grid-auto-resize 
			 		ui-grid-resize-columns></div>
			</div>
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>