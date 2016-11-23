<%--
	화면명 : 주문 관리 > 예치금관리
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/oms.app.deposit.list.js"></script>

<article class="con_box" ng-app="depositListApp" ng-controller="oms_depositListApp_controller as ctrl">
	<h2 class="sub_title1"><spring:message code="c.oms.deposit.manager" /></h2><!--예치금관리-->
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
					<col style="width:100px;" />
					<col class="col_auto" />
					<col style="width:80px;" />				
					<col class="col_auto" />
			</colgroup>
			<tbody>
				<tr>
					<th><spring:message code="c.sps.common.apply.date"/><!-- 적용일 --></th>
					<td>
						<input type="text" id="startDt" ng-model="search.startDate" datetime-picker period-start date-only/>
						~
						<input type="text" id="endDt" ng-model="search.endDate" datetime-picker period-end date-only/>
						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0"/>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.oms.deposit.type" /><!-- 유형 --></th>
					<td>
						<checkbox-list ng-model="search.depositType" code-group="DEPOSIT_TYPE_CD" all-check />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.sps.coupon.issued.mem" /><!-- 회원 --></th>
					<td>
						<select data-ng-model="search.infoType" data-ng-init="search.infoType = 'ID'">
							<option ng-repeat="info in infoType" value="{{info.val}}" >{{info.text}}</option>
						</select>
						<input type="text" ng-model="search.searchKeyword" style="width:22.5%"/>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.oms.order.id" /><!-- 주문번호 --></th>
					<td>
						<input type="text" ng-model="search.orderId" value="" placeholder="" style="width:22.5%;" />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.oms.claim.id" /><!-- 클레임번호 --></th>
					<td>
						<input type="text" ng-model="search.claimNo" value="" placeholder="" style="width:22.5%;" number-only />
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" ng-click="ctrl.resetData()" class="btn_type1">
			<b><spring:message code="common.search.btn.init"/></b>
		</button>
		<button type="button" ng-click="searchDeposit()" class="btn_type1 btn_type1_purple">
			<b><spring:message code="common.search.btn.search"/></b>
		</button>
	</div>

	<div class="btn_alignR marginT3">
		<button type="button" class="btn_type1 " ng-click="ctrl.adjustDepositExcel()">
			<b><spring:message code="c.oms.deposit.batch.adjust" /><!-- 예치금일괄조정 --></b>
		</button>
		<button type="button" class="btn_type1 " ng-click="ctrl.depositAdjust()">
			<b><spring:message code="c.oms.deposit.adjust" /><!-- 예치금조정 --></b>
		</button>
	</div>

	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.oms.deposit.list" /><!-- 예치금내역 -->
			<span><spring:message code="c.search.totalCount" arguments="{{ deposit_data.totalItems}}" /></span>
			<div class="tb_util tb_util_rePosition">
<!--  				<button type="button" class="btn_tb_util tb_util1" ng-click="deposit_grid.initGrid()">되돌리기</button> -->
				<button type="button" class="btn_tb_util tb_util2" ng-click="deposit_grid.exportExcel()">엑셀받기</button>
			</div>
		</h3>

		<div class="gridbox" >
			<div class="grid" data-ui-grid="deposit_data"
			 data-ui-grid-resize-columns 
			 data-ui-grid-auto-resize
			 data-ui-grid-cell-nav
			 data-ui-grid-exporter 
			 data-ui-grid-pagination
			 data-ui-grid-validate
			 ></div>
		</div>
	</div>
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>