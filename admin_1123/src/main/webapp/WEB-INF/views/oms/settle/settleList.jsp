<%--
	화면명 : 주문 관리 > 매출원장
	작성자 : peter
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<link rel="stylesheet" type="text/css" href="/resources/css/oms.css" />
<script type="text/javascript" src="/resources/js/app/oms.app.settle.list.js"></script>

<article class="con_box" ng-app="settleApp" ng-controller="oms_settleListApp_controller as ctrl">
	<%-- <h2 class="sub_title1"><spring:message code="c.oms.pgsettle.title" /></h2> --%>

	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col style="width:15%;"/>
				<col style="width:35%;"/>
				<col style="width:15%;"/>
				<col style="width:35%;"/>
			</colgroup>
			<tbody>
				<tr>
					<th><spring:message code="c.oms.settle.salePeriod" /></th><!-- 매출기간 -->
					<td colspan="3">
						<input type="text" ng-model="search.startDate" placeholder="" datetime-picker period-start date-only />
						~
						<input type="text" ng-model="search.endDate" placeholder="" datetime-picker period-end date-only />
						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0" />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.oms.settle.orderType" /></th><!-- 주문구분 -->
					<td>
						<select ng-model="search.orderType" ng-init="search.orderType = 'ALL'">
							<option ng-repeat="item in orderType" value="{{item.val}}" >{{item.text}}</option>
						</select>
					</td>
					<th><spring:message code="c.oms.settle.site" /></th><!-- 사이트 -->
					<td>
						<select ng-model="search.siteId" ng-options="site.siteId as site.name for (idx, site) in siteList track by site.siteId">
							<option value=""><spring:message code="c.select.all" /></option><!-- 전체 -->
						</select>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.oms.settle.settleType" /></th><!-- 정산유형 -->
					<td>
						<checkbox-list ng-model="search.settleType" custom="SETTLE_TYPE" all-check ></checkbox-list>
					</td>
					<th><spring:message code="c.oms.settle.channel" /></th><!-- 채널 -->
					<td>
						<select ng-model="search.channelId" ng-options="channel.channelId as channel.name for (idx, channel) in channelList track by channel.channelId">
							<option value=""><spring:message code="c.select.all" /></option><!-- 전체 -->
						</select>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.pms.epexcproduct.businessInfo" /></th><!-- 공급업체번호/명 -->
					<td>
						<input type="text" ng-model="search.businessId" style="width:25%;" />
						<input type="text" ng-model="search.businessName" style="width:40%;" />
						<button type="button" class="btn_type2" ng-click="ctrl.searchBusiness()">
							<b><spring:message code="c.search.btn.search" /></b>
						</button>
						<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled': search.businessId == null || search.businessId == ''}" ng-click="ctrl.eraser('business')"><spring:message code="c.search.btn.eraser"/></button>
					</td>
					<th><spring:message code="c.oms.settle.erpBizId" /></th><!-- ERP 업체코드 -->
					<td>
						<input type="text" ng-model="search.erpBusinessId" style="width:70%;" />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.oms.settle.productId" /></th><!-- 상품번호 -->
					<td>
						<input type="text" ng-model="search.productId" style="width:73%;" />
					</td>
					<th><spring:message code="c.oms.settle.saleproductId" /></th><!-- 단품번호 -->
					<td>
						<input type="text" ng-model="search.saleproductId" style="width:73%;" />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.oms.settle.orderId" /></th><!-- 주문번호 -->
					<td colspan="3">
						<input type="text" ng-model="search.orderId" />
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
			<spring:message code="c.oms.settle.gridTitle" />
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_settle.totalItems }}" /></span>
		</h3>
		
		<div class="tb_util tb_util_rePosition">
			<!-- <button type="button" class="btn_tb_util tb_util1" ng-click="myGrid.initGrid()">되돌리기</button> -->
			<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
			<!-- <button type="button" class="btn_tb_util tb_util5" ng-click="ctrl.addRow()">행추가</button> -->
		</div>

		<div class="gridbox" >
			<div class="grid" ui-grid="grid_settle"
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