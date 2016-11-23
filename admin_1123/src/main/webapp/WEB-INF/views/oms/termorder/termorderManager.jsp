<%--
	화면명 : 주문 관리 > 사방넷 주문 미처리 관리
	작성자 : peter
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/oms.app.termorder.manager.js"></script>

<article class="con_box" ng-app="termOrderApp" ng-controller="oms_termOrderManagerApp_controller as ctrl">
	<div class="wrap_popup">
		<%-- <h1 class="sub_title1"><spring:message code="c.pmsAttribute.title1" /></h1> --%>

		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col class="col_142" />
					<col class="col_auto" />
				</colgroup>
				<tbody>
					<tr>
						<th><spring:message code="c.oms.termorder.orderDt" /></th><!-- 주문일 -->
						<td>
							<input type="text" ng-model="search.startDate" placeholder="" datetime-picker date-only period-start/>
							~
							<input type="text" ng-model="search.endDate" placeholder="" datetime-picker date-only period-end/>
							<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button="B" init-button="0" />
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.oms.termorder.site" /></th><!-- 사이트 -->
						<td>
							<select ng-model="search.siteId" ng-options="site.siteId as site.name for (idx, site) in siteList track by site.siteId">
								<option value=""><spring:message code="c.oms.termorder.siteAll" /></option><!-- 전체 -->
							</select>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.oms.termorder.processYn" /></th><!-- 처리여부 -->
						<td>
							<select ng-model="search.processYn" ng-init="search.processYn = 'N'" >
								<option value=""><spring:message code="c.select.all" /></option>
								<option value="N"><spring:message code="c.oms.termorder.processN" /></option>
								<option value="Y"><spring:message code="c.oms.termorder.processY" /></option>
							</select>
						</td>
					</tr>
				</tbody>
			</table>
		</div>

		<div class="btn_alignR">
			<button type="button" class="btn_type1" ng-click="ctrl.reset()">
				<b><spring:message code="c.search.btn.reset" /></b>
			</button>
			<button type="button" class="btn_type1 btn_type1_purple" ng-click="myGrid.loadGridData()">
				<b><spring:message code="c.search.btn.search" /></b>
			</button>
		</div>

		<div class="btn_alignR marginT3" >
			<div class="btn_posL">
				<button type="button" class="btn_type1" ng-click="ctrl.fromSbnToTerm()" fn-id="9_TERMINAL">
					<b><spring:message code="c.oms.termorder.btnSbnToTerm" /></b>
				</button>
				<button type="button" class="btn_type1" ng-click="ctrl.fromTermSbnToBo()">
					<b><spring:message code="c.oms.termorder.btnTermSbnToBo" /></b>
				</button>
<%-- 				<button type="button" class="btn_type1" ng-click="ctrl.fromTmallToTerm()">
					<b><spring:message code="c.oms.termorder.btnTmallToTerm" /></b>
				</button>
				<button type="button" class="btn_type1" ng-click="ctrl.fromTermTmallToBo()">
					<b><spring:message code="c.oms.termorder.btnTermTmallToBo" /></b>
				</button> --%>
			</div>
			<button type="button" class="btn_type1" ng-click="ctrl.deleteExcelOrder()">
				<b><spring:message code="c.common.delete" /></b>
			</button>
			<button type="button" class="btn_type1" ng-click="ctrl.saveExcelOrder()">
				<b><spring:message code="c.common.save" /></b>
			</button>
		</div>

		<div class="box_type1 marginT1">
			<h3 class="sub_title2">
				<spring:message code="c.oms.termorder.gridTitle" />
				<span><spring:message code="c.search.totalCount" arguments="{{ grid_termorder.totalItems }}" /></span>
			</h3>
			
			<div class="tb_util tb_util_rePosition">
				<!-- <button type="button" class="btn_tb_util tb_util1" ng-click="myGrid.initGrid()">되돌리기</button> -->
				<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
				<!-- <button type="button" class="btn_tb_util tb_util5" ng-click="ctrl.addRow()">행추가</button> -->
			</div>

			<div class="gridbox" >
				<div class="grid" ui-grid="grid_termorder"
					ui-grid-validate 
					ui-grid-selection 
			 		ui-grid-exporter 
			 		ui-grid-pagination 
			 		ui-grid-cell-nav 
			 		ui-grid-auto-resize 
			 		ui-grid-resize-columns
			 		ui-grid-row-edit 
			 		ui-grid-edit 
				></div>
			</div>
		</div>
		
	</div>
</article>

<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>