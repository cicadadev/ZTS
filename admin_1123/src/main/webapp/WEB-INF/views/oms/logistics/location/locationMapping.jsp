<%--
	화면명 : 상품 로케이션 매핑
	작성자 : brad
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="false" />
<script type="text/javascript" src="/resources/js/app/oms.app.location.mapping.js"></script>

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

<article class="con_box" ng-app="locationMappingApp" ng-controller="listCtrl as ctrl" data-ng-init="ctrl.init()">
	<h2 class="sub_title1"><spring:message code="c.oms.logistics.location.mapping"><!-- 상품로케이션매핑 --></spring:message></h2>
	<ul class="tab_type2">
		<li>
			<button type="button" ng-click="ctrl.changeTab()">로케이션관리</button>
		</li>
		<li class="on" >
			<button type="button">상품로케이션매핑</button>
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
					<th><spring:message code="c.ccsBusiness.supply"/></th>
					<td colspan="3">
						<input type="text" value="" ng-model="search.businessId" style="width:100px;"/>
						<input type="text" value="" ng-model="search.businessName" style="width:100px;"/>
						<button type="button" class="btn_type2" ng-click="ctrl.openPopup('business')">
							<b><spring:message code="c.search.btn.search" /></b>
						</button>
						<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : search.businessId == null || search.businessId == ''}"  ng-click="ctrl.eraser('businessId', 'businessName')"><spring:message code="c.search.btn.eraser"/></button>
					</td>
				</tr>	
				<tr>
					<th>로케이션사용여부</th>
					<td colspan="3">
						<checkbox-list ng-model="search.locationUseYn" custom="LOCATION_USE_YN2" all-check />
					</td>
				</tr>
				<tr>
					<th>로케이션명</th>
					<td colspan="3">
						<input type="text" style="width: 190px;" ng-model="search.locationId" />
					</td>
				</tr>
				<tr>
					<th>상품</th>
					<td colspan="3">
						<select ng-model="search.productType" data-ng-options="option.key as option.name for option in productTypeOptions" 
						data-ng-init="productTypeOptions = [{key : 'productId', name : '상품번호'}, {key : 'productName', name : '상품명'}]; search.productType='productId'"></select>
						<input type="text" style="width: 190px;" ng-model="search.product" />
					</td>
				</tr>
			</tbody>
		</table>
		<div class="btn_alignR">
			<button type="button" ng-click="ctrl.reset()" class="btn_type1">
				<b><spring:message code="c.search.btn.reset" /></b>
			</button>
			<button type="button" ng-click="ctrl.searchLocationMappingList()" class="btn_type1 btn_type1_purple">
				<b><spring:message code="c.search.btn.search" /></b>
			</button>
		</div>
	</div>
	<div class="btn_alignR marginT3">
		<button type="button" class="btn_type1" ng-click="ctrl.bulk.upload.excel()">
			<b><spring:message code="c.oms.logistics.location.all.modify" /></b>
		</button>
		<button type="button" class="btn_type1" ng-click="ctrl.popup.update()">
			<b><spring:message code="c.oms.logistics.location.single.modify" /></b>
		</button>
	</div>
	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.oms.logistics.location.list" />
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_locationMapping.totalItems }}" /></span>
		</h3>
		<div class="tb_util tb_util_rePosition">
			<button type="button" class="btn_tb_util tb_util1" ng-click="myGrid.initGrid()">되돌리기</button>
			<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
		</div>
		<div class="gridbox">
			<div class="grid" data-ui-grid="grid_locationMapping" 
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
