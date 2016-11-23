<%--
	화면명 : 프로모션관리 > 이벤트 관리 > 이벤트 목록
	작성자 : peter
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/sps.app.event.list.js"></script>

<article class="con_box" ng-app="eventApp" ng-controller="sps_eventListApp_controller as ctrl">
	<h2 class="sub_title1"><spring:message code="c.sps.event.title" /></h2>

	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col class="col_142" />
				<col class="*" />
			</colgroup>
			<tbody>
				<tr>
					<th><spring:message code="c.sps.event.eventPeriod" /><!-- 기간 --></th>
					<td>
						<input type="text" ng-model="search.startDate" placeholder="" datetime-picker period-start date-only/>
						~
						<input type="text" ng-model="search.endDate" placeholder="" datetime-picker period-end date-only/>
						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0"/>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.sps.event.eventState" /><!-- 이벤트 상태 --></th>
					<td>
						<checkbox-list ng-model="search.eventState" code-group="EVENT_STATE_CD" all-check ></checkbox-list>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.sps.event.eventType" /><!-- 이벤트 유형 --></th>
					<td>
						<checkbox-list ng-model="search.eventType" code-group="EVENT_TYPE_CD" all-check ></checkbox-list>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.sps.event" /><!-- 이벤트 --></th>
					<td>
						<select style="min-width:100px;" ng-model="search.searchKeyword">
							<option value="name"><spring:message code="c.sps.event.name" /></option>
							<option value="eventId"><spring:message code="c.sps.event.eventId" /></option>
						</select>
						<input type="text" ng-model="search.event" value="" placeholder="" style="width:20%;" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" class="btn_type1" ng-click="ctrl.reset()" >
			<b><spring:message code="c.search.btn.reset" /></b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" ng-click="ctrl.getEventList()" >
			<b><spring:message code="c.search.btn.search" /></b>
		</button>
	</div>

	<div class="btn_alignR marginT3">
		<button type="button" class="btn_type1" ng-click="ctrl.insertEventPopup()" fn-id="13_INSERT">
			<b><spring:message code="c.sps.event.btn.insertEvent" /></b>
		</button>
		<button type="button" class="btn_type1" ng-click="ctrl.deleteGridData()" fn-id="13_DELETE">
			<b><spring:message code="c.common.delete" /></b>
		</button>	
	</div>

	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.sps.event.subtitle1" />
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_event.totalItems }}" /></span>
		</h3>
		
		<div class="tb_util tb_util_rePosition">
			<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
		</div>

		<div>
			<div class="gridbox gridbox300" >
				<div class="grid" ui-grid="grid_event"
					ui-grid-move-columns 
					ui-grid-resize-columns 
					ui-grid-pagination
					ui-grid-auto-resize 
					ui-grid-selection 
					ui-grid-row-edit
					ui-grid-cell-nav
					ui-grid-exporter
					ui-grid-edit 
					ui-grid-validate></div>
			</div>
		</div>
	</div>

</article>

<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>