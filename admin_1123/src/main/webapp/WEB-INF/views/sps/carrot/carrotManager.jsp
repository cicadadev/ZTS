<%--
	화면명 : 프로모션 관리 > 당근관리
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/sps.app.carrot.manager.js"></script>

<article class="con_box" ng-app="carrotManagerApp" ng-controller="sps_carrotManagerApp_controller as ctrl">
	<h2 class="sub_title1"><spring:message code="c.sps.carrot.manager" /></h2><!--당근관리-->
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
						<input type="text" id="startDt" ng-model="search.startDate" value="" placeholder="" datetime-picker period-start date-only/>
						~
						<input type="text" id="endDt" ng-model="search.endDate" value="" placeholder="" datetime-picker period-end date-only/>
						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button="B" init-button="0"/>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.sps.carrot.type" /><!-- 유형 --></th>
					<td>
						<checkbox-list ng-model="search.carrotType" code-group="CARROT_TYPE_CD" all-check />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.sps.carrot.note" /><!-- 사유 --></th>
					<td>
						<input type="text" ng-model="search.note" value="" placeholder="" style="width:22.5%;" />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.mmsMemberZts.memGradeCd" /><!-- 회원등급 --></th>
					<td>
						<checkbox-list ng-model="search.memGrade" code-group="MEM_GRADE_CD" all-check />
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
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" ng-click="ctrl.resetData()" class="btn_type1">
			<b><spring:message code="common.search.btn.init"/></b>
		</button>
		<button type="button" ng-click="searchCarrot()" class="btn_type1 btn_type1_purple">
			<b><spring:message code="common.search.btn.search"/></b>
		</button>
	</div>

	<div style="height:72px;">
		<table class="tb_type3" >
			<colgroup>
					<col style="width:70px;" />				
					<col style="width:50px;" />				
					<col style="width:50px;" />				
					<col style="width:50px;" />				
					<col style="width:50px;" />				
			</colgroup>
			<tbody ng-show="summry.length > 0">
				<tr>
					<th>유형</th>
					<th>적립자수</th>
					<th>적립당근</th>
					<th>차감자수</th>
					<th>차감당근</th>
				</tr>
				<tr ng-repeat="carrot in summry">
					<td>{{carrot.carrotTypeName}}</td>
					<td >{{carrot.plusMem}}</td>
					<td style="color: blue;">{{carrot.plusCarrot}}</td>
					<td >{{carrot.minusMem}}</td>
					<td style="color: red;">{{carrot.minusCarrot}}</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="btn_alignR marginT1">
		<button type="button" class="btn_type1 " ng-click="ctrl.carrotAdjust()" fn-id="43_UPDATE">
			<b><spring:message code="c.sps.carrot.adjust" /><!-- 당근조정 --></b>
		</button>
		<button type="button" class="btn_type1 " ng-click="ctrl.adjustCarrotExcel()" fn-id="43_UPDATE">
			<b><spring:message code="c.sps.carrot.batch.adjust" /><!-- 당근일괄조정 --></b>
		</button>
	</div>

	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.sps.carrot.list" /><!-- 당근내역 -->
			<span><spring:message code="c.search.totalCount" arguments="{{ carrot_data.totalItems}}" /></span>
			<div class="tb_util tb_util_rePosition">
<!--  				<button type="button" class="btn_tb_util tb_util1" ng-click="carrot_grid.initGrid()">되돌리기</button> -->
				<button type="button" class="btn_tb_util tb_util2" ng-click="carrot_grid.exportExcel()" fn-id="EXCEL">엑셀받기</button>
			</div>
		</h3>

		<div class="gridbox" >
			<div class="grid" data-ui-grid="carrot_data"
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