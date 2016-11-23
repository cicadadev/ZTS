<%--
	화면명 : 배송 승인
	작성자 : brad
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="false" />
<script type="text/javascript" src="/resources/js/app/oms.app.delivery.approval.js"></script>

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

<article class="con_box" ng-app="deliveryApprovalApp" ng-controller="listCtrl as ctrl" data-ng-init="ctrl.init()" >
	<h2 class="sub_title1"><spring:message code="c.oms.logistics.delivery.approval"><!-- 배송 승인 --></spring:message></h2>
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col width="12%" />
				<col width="43%" />
				<col width="12%" />
				<col width="*" />
			</colgroup>

			<tbody>
				<tr>
					<th>주문일</th>
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
					<th>배송구분</th>
					<td>
						<select data-ng-model="search.deliveryType" data-ng-options="deliveryType.key as deliveryType.name for deliveryType in ctrl.deliveryTypeList" ng-change="ctrl.checkDeliveryType()">
						</select>
					</td>
					<th>사이트</th>
					<td>
						<div class="positionR">
							<div class="div_chk">
								전체<input type="checkbox" ng-model="siteAll" ng-change="ctrl.checkAllSite()" ng-disabled="(search.deliveryType==''||deliveryTypeArray.indexOf(search.deliveryType) > -1) ?  false : true" />
							</div>
							<div class="ly_chk" ng-show="!siteAll">
								<label ng-repeat="site in siteList">
									<input type="checkbox" ng-model="site.isSelected" ng-click="ctrl.checkSite()" checklist-model="siteIds" checklist-value="site.siteId" /> {{site.name}}
								</label>
							</div>
						</div>
					</td>
				</tr>
				<tr>
					<th>발송유형</th>
					<td>
						<checkbox-list ng-model="search.orderDeliveryType" custom="ORDER_DELIVERY_TYPE" all-check option-check/>
					</td>
					<th>단품여부</th>
					<td>
						<checkbox-list ng-model="search.orderProductType" custom="ORDER_PRODUCT_TYPE" all-check />
					</td>
				</tr>
				<tr>
					<th>미출고/승인취소</th>
					<td>
						<radio-list data-ng-init="search.orderProductState = ''" ng-model="search.orderProductState" custom="ORDER_PRODUCT_STATE" option-check></radio-list>
						<select-list data-ng-model="search.deliveryCancelReasonCd" code-group="DELIVERY_CANCEL_REASON_CD" all-check ng-disabled="search.orderProductState != 'ORDER_PRODUCT_STATE_CD.CANCELAPPROVAL'"></select-list>
					</td>
					<th>전송오류여부</th>
					<td>
						<radio-yn ng-model="search.normalYn" labels='정상,오류' init-val="Y" option-check></radio-yn>&nbsp;
						<select-list data-ng-model="search.sendErrorReasonCd" code-group="SEND_ERROR_REASON_CD" all-check ng-disabled="search.normalYn != 'N'"></select-list>
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
	<div class="btn_alignR marginT3">
		<div class="btn_posL">
			<button type="button" class="btn_type1 btn_type1_purple" ng-click="ctrl.checkBoxAll(true);">
				<b><spring:message code="c.oms.logistics.select.all" /></b>
			</button>
			<button type="button" class="btn_type1 btn_type1_purple" ng-click="ctrl.checkBoxAll(false);">
				<b><spring:message code="c.oms.logistics.cancel.select.all" /></b>
			</button>
			<button type="button" class="btn_type1 btn_type1_purple" ng-click="ctrl.checkOneHundred();">
				<b><spring:message code="c.oms.logistics.select.one.hundred" /></b>
			</button>
		</div>
 		<button type="button" class="btn_type1" ng-click="ctrl.approval('DAS')" fn-id="76_DAS">
			<b><spring:message code="c.oms.logistics.das.api" /></b>
		</button>
		<button type="button" class="btn_type1" ng-click="ctrl.approval('HANJIN')" >
			<b><spring:message code="c.oms.logistics.hanjin.api" /></b>
		</button>
	</div>
	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.oms.order.list" />
			<span style="font-weight:bold"><spring:message code="c.search.orderTotalCount" arguments="{{ totalOrders }}, {{ totalProducts }}" /></span>
		</h3>
		<div class="tb_util tb_util_rePosition">
			<button type="button" class="btn_tb_util tb_util1" ng-click="myGrid.initGrid()">되돌리기</button>
			<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()" fn-id="76_EXCEL">엑셀받기</button>
		</div>
		<div class="gridbox">
			<div class="grid" ui-grid="grid_orderProduct"
				ui-grid-resize-columns 
				ui-grid-auto-resize 
				ui-grid-pinning>
			</div>	
		</div>
	</div>
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="false" />
