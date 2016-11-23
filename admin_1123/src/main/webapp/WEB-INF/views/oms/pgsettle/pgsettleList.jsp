<%--
	화면명 : 주문 관리 > PG사 승인대사
	작성자 : peter
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/oms.app.pgsettle.list.js"></script>

<article class="con_box" ng-app="pgListApp" ng-controller="oms_pgSettleListApp_controller as ctrl">
	<%-- <h2 class="sub_title1"><spring:message code="c.oms.pgsettle.title" /></h2> --%>

	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col class="col_142"/>
				<col class="col_auto"/>
				<col class="col_142"/>
				<col class="col_auto"/>
			</colgroup>
			<tbody>
				<tr>
					<th><spring:message code="c.search.period" /></th><!-- 기간 -->
					<td colspan="3">
						<select ng-model="search.periodType" ng-init="search.periodType = 'APPROVE'">
							<option ng-repeat="item in periodType" value="{{item.val}}" >{{item.text}}</option>
						</select>
						<input type="text" ng-model="search.startDate" placeholder="" datetime-picker period-start date-only />
						~
						<input type="text" ng-model="search.endDate" placeholder="" datetime-picker period-end date-only />
						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0" />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.oms.pgsettle.pgCompany" /></th><!-- PG사 -->
					<td>
						<radio-yn ng-model="search.pgCompany" labels='LG U+,카카오페이' init-val="Y" option-check></radio-yn>
					</td>
					<th><spring:message code="c.oms.pgsettle.pgStoreId" /></th><!-- PG상점ID -->
					<td>
						<select ng-model="search.pgShopId" ng-init="search.pgShopId = ''">
							<option ng-repeat="pgShopId in pgShopIds" value="{{pgShopId.val}}" >{{pgShopId.text}}</option>
						</select>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.oms.pgsettle.errorYn" /></th><!-- 오차여부 -->
					<td colspan="3">
						<checkbox-list ng-model="search.errorYn" custom="ERROR_YN" all-check ></checkbox-list>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.oms.pgsettle.paymentMethod" /></th><!-- 결제수단 -->
					<td colspan="3">
						<checkbox-list ng-model="search.paymentMethod" custom="PAYMENT_METHOD" all-check ></checkbox-list>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.oms.pgsettle.orderId" /></th><!-- 주문번호 -->
					<td colspan="3">
						<input type="text" ng-model="search.orderId" style="width:30%" />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.oms.pgsettle.approvalNo" /></th><!-- 승인번호 -->
					<td colspan="3">
						<input type="text" ng-model="search.approvalNo" style="width:30%;" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" class="btn_type1" ng-click="ctrl.reset()" >
			<b><spring:message code="c.search.btn.reset" /></b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" ng-click="myGrid.loadGridData()" >
			<b><spring:message code="c.search.btn.search" /></b>
		</button>
	</div>

<br/>
<br/>

	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.oms.pgsettle.gridTitle" />
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_pgsettle.totalItems }}" /></span>
		</h3>
		
		<div class="tb_util tb_util_rePosition">
			<!-- <button type="button" class="btn_tb_util tb_util1" ng-click="myGrid.initGrid()">되돌리기</button> -->
			<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel('pgsettle')">엑셀받기</button>
			<!-- <button type="button" class="btn_tb_util tb_util5" ng-click="ctrl.addRow()">행추가</button> -->
		</div>

		<div class="gridbox" >
			<div class="grid" ui-grid="grid_pgsettle"
				ui-grid-validate 
				ui-grid-selection 
		 		ui-grid-exporter 
		 		ui-grid-pagination 
		 		ui-grid-edit 
		 		ui-grid-row-edit 
		 		ui-grid-cell-nav 
		 		ui-grid-auto-resize 
		 		ui-grid-resize-columns
			></div>
		</div>
	</div>

</article>

<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>