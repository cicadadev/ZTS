<%--
	화면명 : 피킹리스트
	작성자 : brad
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="false" />
<script type="text/javascript" src="/resources/js/app/oms.app.picking.list.js"></script>

<style>
.alignC {
	text-align: center;
}

.alignR {
	text-align: right;
}

.ui-grid-header-cell-row {
	text-align: center;
}

.edit-calendar {
	text-align: center;
	color: #2e3192;
	background-color: #ebecff !important;
}
.tb_util_rePosition {
	right: 10px;
	display: block;
}
label {
	cursor: pointer;
}

input[type="checkbox"] {
	cursor: pointer;
}
@media screen {
    #printSection {
        display: none;
    }
}
@media print {
    body * {
        visibility:hidden;
    }
    #printSection, #printSection * {
        visibility:visible;
    }
    #printSection {
        position:absolute;
        left:0;
        top:0;
    }
}
</style>

<article class="con_box" ng-app="pickingListApp" ng-controller="listCtrl as ctrl" data-ng-init="ctrl.init()">
	<h2 class="sub_title1"><spring:message code="c.oms.logistics.picking.list"><!-- 피킹리스트 --></spring:message></h2>
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col width="12%" />
				<col width="80%" />
				<col width="12%" />
				<col width="*" />
			</colgroup>

			<tbody>
				<tr>
					<th>운송장출력일</th>
					<td colspan="3">
						<input type="text" ng-model="search.startDate" datetime-picker period-start date-only width="150px"/>
						~
						<input type="text" ng-model="search.endDate"  datetime-picker period-end date-only width="150px"/>
						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button="B" init-button="0"/>
					</td>
				</tr>
				<tr>
					<th>주문구분</th>
					<td colspan="3">
						<checkbox-list ng-model="search.orderType" custom="ORDER_TYPE" all-check b2e-option/>
					</td>
				</tr>
				<tr>
					<th>로케이션명</th>
					<td>
						<select style="width: 120px" ng-model="search.locationId" ng-options="location.locationId as location.locationId for (idx, location) in locationList">
							<option value="">전체</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>차수</th>
					<td colspan="3">
						<input type="number" style="width: 120px" ng-model="search.startDeliveryOrder" ng-required/>
						~
						<input type="number" style="width: 120px" ng-model="search.endDeliveryOrder" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="btn_alignR">
		<button type="button" ng-click="ctrl.reset()" class="btn_type1">
			<b><spring:message code="c.search.btn.reset" /></b>
		</button>
		<button type="button" ng-click="ctrl.searchPickingList()" class="btn_type1 btn_type1_purple">
			<b><spring:message code="c.search.btn.search" /></b>
		</button>
	</div>
	<div class="btn_alignR marginT3">
<%-- 		<button type="button" class="btn_type1" ng-print print-element-id="gridbox">
			<b><spring:message code="c.oms.logistics.picking.list.print" /></b>
		</button> --%>
	</div>
	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.oms.logistics.picking.list.name" />
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_pickingList.data.length }}" /></span>
		</h3>
		<div class="tb_util tb_util_rePosition">
			<button type="button" class="btn_tb_util tb_util1" ng-click="myGrid.initGrid()">되돌리기</button>
			<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
		</div>
		<div class="gridbox">
			<div class="grid" data-ui-grid="grid_pickingList" 
				data-ui-grid-move-columns 
				data-ui-grid-resize-columns 
				data-ui-grid-auto-resize 
				data-ui-grid-selection 
				data-ui-grid-row-edit
				data-ui-grid-cell-nav
				data-ui-grid-exporter
				data-ui-grid-edit 
				data-ui-grid-validate>
			</div>	
		</div>
	</div>
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="false" />
