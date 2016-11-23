<%--
	화면명 : 로케이션 관리
	작성자 : brad
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="false" />
<script type="text/javascript" src="/resources/js/app/oms.app.location.list.js"></script>

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
label {
	cursor: pointer;
}
input[type="checkbox"] {
	cursor: pointer;
}
</style>

<article class="con_box" ng-app="locationApp" ng-controller="listCtrl as ctrl" data-ng-init="ctrl.init()">
	<h2 class="sub_title1"><spring:message code="c.oms.logistics.location.management"><!-- 로케이션 관리 --></spring:message></h2>
	<ul class="tab_type2">
		<li class="on" >
			<button type="button">로케이션관리</button>
		</li>
		<li oncllick="changeTab()">
			<button type="button" ng-click="ctrl.changeTab()">상품로케이션매핑</button>
		</li>
	</ul>
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col width="12%" />
				<col width="40%" />
				<col width="12%" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr>
					<th>로케이션사용여부</th>
					<td colspan="3">
						<checkbox-list ng-model="search.locationUseYn" custom="LOCATION_USE_YN" all-check />
					</td>
				</tr>
				<tr>
					<th>로케이션명</th>
					<td colspan="3">
						<input type="text" style="width: 190px;" ng-model="search.locationId" />
					</td>
				</tr>
			</tbody>
		</table>
		<div class="btn_alignR">
			<button type="button" ng-click="ctrl.reset()" class="btn_type1">
				<b><spring:message code="c.search.btn.reset" /></b>
			</button>
			<button type="button" ng-click="ctrl.searchLocationList()" class="btn_type1 btn_type1_purple">
				<b><spring:message code="c.search.btn.search" /></b>
			</button>
		</div>
	</div>
<!-- 	<div style="height:72px;" class="marginT2"> -->
<!-- 		<table class="tb_type4"> -->
<!-- 			<colgroup> -->
<!-- 					<col style="width:100px;" />				 -->
<!-- 					<col style="width:200px;" />				 -->
<!-- 			</colgroup> -->
<!-- 			<tbody> -->
<!-- 				<tr> -->
<!-- 					<th>창고명</th> -->
<!-- 					<th>창고주소</th> -->
<!-- 				</tr> -->
<!-- 				<tr ng-repeat="warehouse in warehouseList"> -->
<!-- 					<td>{{warehouse.name}}</td> -->
<!-- 					<td >{{}}</td> -->
<!-- 				</tr> -->
<!-- 			</tbody> -->
<!-- 		</table> -->
<!-- 	</div> -->
	<div class="btn_alignR marginT3">
		<button type="button" class="btn_type1" ng-click="ctrl.addRow()">
			<b><spring:message code="c.grid.add.row" /></b>
		</button>
		<button type="button" class="btn_type1" ng-click="ctrl.saveLocationGrid()">
			<b><spring:message code="c.common.save" /></b>
		</button>
	</div>
	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.oms.logistics.location.list.name" />
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_location.totalItems }}" /></span>
		</h3>
		<div class="tb_util tb_util_rePosition">
			<button type="button" class="btn_tb_util tb_util1" ng-click="myGrid.initGrid()">되돌리기</button>
			<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
		</div>
		<div class="gridbox">
			<div class="grid" data-ui-grid="grid_location" 
				data-ui-grid-move-columns 
				data-ui-grid-resize-columns 
				data-ui-grid-pagination
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
