<%--
	화면명 : 출고/미출고 리스트
	작성자 : brad
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="false" />
<script type="text/javascript" src="/resources/js/app/oms.app.shipping.list.js"></script>

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
    #printSection .ui-grid-render-container-body .ui-grid-viewport {overflow:visible !important;}
}
</style>

<article class="con_box" ng-app="shippingListApp" ng-controller="listCtrl as ctrl" data-ng-init="ctrl.init()">
	<h2 class="sub_title1"><spring:message code="c.oms.logistics.shipping.list"><!-- 출고/미출고 리스트 --></spring:message></h2>
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
					<th>출고/미출고처리일</th>
					<td colspan="3">
						<input type="text" ng-model="search.startDate" datetime-picker period-start date-only width="150px"/>
						~
						<input type="text" ng-model="search.endDate"  datetime-picker period-end date-only width="150px"/>
						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button="B" init-button="0"/>
					</td>
				</tr>
				<tr ng-if="!poBusinessId && roleId != '5'">
					<th>배송방법</th>
					<td>
						<checkbox-list ng-model="search.deliveryMethod" custom="DELIVERY_METHOD" all-check />
					</td>
					<th><spring:message code="c.ccsBusiness.supply"/></th>
					<td >
						<input type="text" value="" ng-model="search.businessId" style="width:100px;" />
						<input type="text" value="" ng-model="search.businessName" style="width:100px;"/>
						<button type="button" class="btn_type2" ng-click="ctrl.openPopup('business')">
							<b><spring:message code="c.search.btn.search" /></b>
						</button>
						<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : search.businessId == null || search.businessId == ''}"  ng-click="ctrl.eraser('businessId', 'businessName')"><spring:message code="c.search.btn.eraser"/></button>
					</td>
				</tr>
				<tr>
					<th>출고구분</th>
					<td>
						<checkbox-list ng-model="search.confirmYn" custom="CONFIRM_YN" all-check />
					</td>
					<th>사이트</th>
					<td>
						<select ng-model="search.siteId" ng-options="site.siteId as site.name for (idx, site) in siteList">
							<option value="">전체</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>수취인</th>
					<td colspan="3">
						<select ng-model="search.receiverType" data-ng-options="option.key as option.name for option in receiverTypeOptions" 
						data-ng-init="receiverTypeOptions = [{key : 'name', name : '이름'}, {key : 'mobile', name : '휴대폰번호'}]; search.receiverType='name'"></select>
						<input type="text" style="width: 190px;" ng-model="search.receiver" />
					</td>
				</tr>
				<tr>
					<th>주문자</th>
					<td colspan="3">
						<select ng-model="search.ordererType" data-ng-options="option.key as option.name for option in ordererTypeOptions" 
						data-ng-init="ordererTypeOptions = [{key : 'name', name : '이름'}, {key : 'id', name : '아이디'}, {key : 'mobile', name : '휴대폰번호'}]; search.ordererType='name'"></select>
						<input type="text" style="width: 190px;" ng-model="search.orderer" />
					</td>
				</tr>
				<tr>
					<th>주문번호</th>
					<td colspan="{{poBusinessId ? 3 : 0}}">
						<input type="text" style="width: 190px;" ng-model="search.orderId" />
					</td>
					<th ng-if="!poBusinessId">LP_NO</th>
					<td ng-if="!poBusinessId">
						<input type="text" style="width: 190px;" ng-model="search.lpNo" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="btn_alignR">
		<button type="button" ng-click="ctrl.reset()" class="btn_type1">
			<b><spring:message code="c.search.btn.reset" /></b>
		</button>
		<button type="button" ng-click="ctrl.searchShippingList()" class="btn_type1 btn_type1_purple">
			<b><spring:message code="c.search.btn.search" /></b>
		</button>
	</div>
	<div class="btn_alignR marginT3">
 		<button type="button" class="btn_type1" ng-click="myGrid.saveGridData()" ng-if="roleId !=='5'">
			<b><spring:message code="c.oms.logistics.save.shipping.list" /></b>
		</button>
	</div>
	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.oms.logistics.shipping.list.name" />
			<span style="font-weight:bold"><spring:message code="c.search.orderTotalCount" arguments="{{ totalOrders }}, {{ totalProducts }}" /></span>
		</h3>
		<div class="tb_util tb_util_rePosition">
			<button type="button" class="btn_tb_util tb_util1" ng-click="myGrid.initGrid()">되돌리기</button>
			<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
		</div>
		<div class="gridbox">
			<div class="grid" data-ui-grid="grid_shippingList" 
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
