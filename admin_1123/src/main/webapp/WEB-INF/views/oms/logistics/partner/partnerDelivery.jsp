<%--
	화면명 : 배송의뢰서_협력사
	작성자 : brad
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="false" />
<script type="text/javascript" src="/resources/js/app/oms.app.partner.delivery.js"></script>

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

<article class="con_box" ng-app="partnerDeliveryApp" ng-controller="listCtrl as ctrl" data-ng-init="ctrl.init()">
	<h2 class="sub_title1"><spring:message code="c.oms.logistics.partner.delivery"><!-- 배송의뢰서_협력사 --></spring:message></h2>
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
					<th>주문일</th>
					<td colspan="3">
<!-- 						<select ng-model="search.dateType" data-ng-options="option.key as option.name for option in dateTypeOptions"  -->
<!-- 						data-ng-init="dateTypeOptions = [{key : 'orderDt', name : '주문일'}, {key : 'approvalDt', name : '배송승인일'}]; search.dateType='orderDt'"></select> -->
						<input type="text" ng-model="search.startDate" datetime-picker period-start date-only width="150px"/>
						~
						<input type="text" ng-model="search.endDate"  datetime-picker period-end date-only width="150px"/>
						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button="B" init-button="0"/>
					</td>
				</tr>
				<tr ng-if="!poBusinessId">
					<th><spring:message code="c.ccsBusiness.supply"/></th>
					<td colspan="3">
						<input type="text" value="" ng-model="search.businessId" style="width:100px;" />
						<input type="text" value="" ng-model="search.businessName" style="width:100px;" />
						<button type="button" class="btn_type2" ng-click="ctrl.openPopup('business')">
							<b><spring:message code="c.search.btn.search" /></b>
						</button>
						<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : search.businessId == null || search.businessId == ''}"  ng-click="ctrl.eraser('businessId', 'businessName')"><spring:message code="c.search.btn.eraser"/></button>
					</td>
				</tr>
				<tr>
					<th>미출고/승인취소</th>
					<td colspan="3">
						<radio-list data-ng-init="search.orderProductState = ''" ng-model="search.orderProductState" custom="ORDER_PRODUCT_STATE" option-check></radio-list>
						<select-list data-ng-model="search.deliveryCancelReasonCd" code-group="DELIVERY_CANCEL_REASON_CD" all-check ng-disabled="search.orderProductState != 'ORDER_PRODUCT_STATE_CD.CANCELAPPROVAL'"></select-list>
					</td>
				</tr>
				<tr>
					<th>발송유형</th>
					<td colspan="3">
						<checkbox-list ng-model="search.orderDeliveryType" custom="ORDER_DELIVERY_TYPE" all-check />
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
					<td colspan="3">
						<input type="text" style="width: 190px;" ng-model="search.orderId" />
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
	<div class="marginT1">
		<p style="margin-left:10px; color:red;"><b>*배송의뢰건 확인 후 반드시 '배송승인'을 처리하시어 주문을 확정하시기 바랍니다.</b></p>
		<p style="margin-left:10px; color:red;"><b>*'배송승인'을 처리하지 않은 배송의뢰건은 고객에 의해 임의 취소 및 변경이 될수 있으며, 이로 인해 발생하는 불이익에 대해서는 제로투세븐에서 책임지지 않습니다.</b></p>
	</div>
	<div class="btn_alignR marginT3">
<!-- 		<button type="button" class="btn_type1" ng-click=""> -->
<%-- 			<b><spring:message code="c.oms.logistics.shipping.list.print" /></b> --%>
<!-- 		</button> -->
		<button type="button" class="btn_type1" ng-click="ctrl.approval()">
			<b><spring:message code="c.oms.logistics.delivery.approval" /></b>
		</button>
	</div>
	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.oms.logistics.shipping.ready.list" />
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_partnerDelivery.data.length }}" /></span>
		</h3>
		<div class="tb_util tb_util_rePosition">
			<button type="button" class="btn_tb_util tb_util1" ng-click="myGrid.initGrid()">되돌리기</button>
			<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()" fn-id="83_EXCEL">엑셀받기</button>
		</div>
		<div class="gridbox">
			<div class="grid" data-ui-grid="grid_partnerDelivery" 
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
