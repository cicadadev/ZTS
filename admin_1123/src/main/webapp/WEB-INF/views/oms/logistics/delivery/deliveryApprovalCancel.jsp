<%--
	화면명 : 배송 승인 취소
	작성자 : brad
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="false" />
<script type="text/javascript" src="/resources/js/app/oms.app.delivery.approval.cancel.js"></script>

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
</style>

<article class="con_box" ng-app="deliveryApprovalCancelApp" ng-controller="listCtrl as ctrl" data-ng-init="ctrl.init()">
	<h2 class="sub_title1"><spring:message code="c.oms.logistics.delivery.approval.cancel"><!-- 배송 승인 취소 --></spring:message></h2>
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
					<th>배송승인일</th>
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
				<tr ng-if="!poBusinessId && roleId != '5'">
					<th>배송방법</th>
					<td>
						<checkbox-list ng-model="search.deliveryMethod" custom="DELIVERY_METHOD" all-check />
					</td>
					<th><spring:message code="c.ccsBusiness.supply"/></th>
					<td >
						<input type="text" value="" ng-model="search.businessId" style="width:100px;" />
						<input type="text" value="" ng-model="search.businessName" style="width:100px;" />
						<button type="button" class="btn_type2" ng-click="ctrl.openPopup('business')">
							<b><spring:message code="c.search.btn.search" /></b>
						</button>
						<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : search.businessId == null || search.businessId == ''}"  ng-click="ctrl.eraser('businessId', 'businessName')"><spring:message code="c.search.btn.eraser"/></button>
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
					<th>상품</th>
					<td colspan="3">
						<select ng-model="search.productType" data-ng-options="option.key as option.name for option in productTypeOptions" 
						data-ng-init="productTypeOptions = [{key : 'productName', name : '상품명'}, {key : 'productId', name : '상품번호'}]; search.productType='productName'"></select>
						<input type="text" style="width: 190px;" ng-model="search.product" />
					</td>
				</tr>
				<tr>
					<th>주문번호</th>
					<td>
						<input type="text" style="width: 190px;" ng-model="search.orderId" />
					</td>
					<th>제휴주문번호</th>
					<td>
						<input type="text" style="width: 190px;" ng-model="search.siteOrderId" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="btn_alignR">
		<button type="button" ng-click="ctrl.reset()" class="btn_type1">
			<b><spring:message code="c.search.btn.reset" /></b>
		</button>
		<button type="button" ng-click="ctrl.searchOrderProduct()" class="btn_type1 btn_type1_purple">
			<b><spring:message code="c.search.btn.search" /></b>
		</button>
	</div>
	<div class="btn_alignR marginT3">
		<div class="btn_posL">
			<button type="button" class="btn_type1 btn_type1_purple" ng-click="checkBoxAll(true);">
				<b><spring:message code="c.oms.logistics.select.all" /></b>
			</button>
			<button type="button" class="btn_type1 btn_type1_purple" ng-click="checkBoxAll(false);">
				<b><spring:message code="c.oms.logistics.cancel.select.all" /></b>
			</button>
		</div>
		<button type="button" class="btn_type1" ng-click="ctrl.bulk.upload.excel()">
			<b><spring:message code="c.oms.logistics.register.approval.cancel" /></b>
		</button>
		<button type="button" class="btn_type1" ng-click="ctrl.cancel()">
			<b><spring:message code="c.oms.logistics.delivery.approval.cancel" /></b>
		</button>
	</div>
	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.oms.order.list" />
			<span style="font-weight:bold"><spring:message code="c.search.orderTotalCount" arguments="{{ totalOrders }}, {{ totalProducts }}" /></span>
		</h3>
		<div class="tb_util tb_util_rePosition">
			<button type="button" class="btn_tb_util tb_util1" ng-click="myGrid.initGrid()">되돌리기</button>
			<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()" fn-id="77_EXCEL">엑셀받기</button>
		</div>
		<div class="gridbox">
			<div class="grid" data-ui-grid="grid_cancelApproval" 
				data-ui-grid-move-columns 
				data-ui-grid-resize-columns 
				data-ui-grid-auto-resize 
				data-ui-grid-selection 
				data-ui-grid-row-edit
				data-ui-grid-cell-nav
				data-ui-grid-edit 
				data-ui-grid-validate>
			</div>
		</div>
	</div>
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="false" />
