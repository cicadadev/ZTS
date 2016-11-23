<%--
	화면명 : 프로모션 관리 > 쿠폰 관리 > 쿠폰 상세 > 쿠폰 발급내역
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/sps.app.coupon.list.js"></script>

<div ng-app="couponApp" class="wrap_popup" ng-controller="sps_couponIssuedPopApp_controller as ctrl" >
	<h2 class="sub_title1"><spring:message code="c.sps.coupon.pop.issue" /></h2>
	<ul class="tab_type2">
		<li class="">
			<button type="button" ng-click="ctrl.moveTab()" >
				<spring:message code="c.sps.coupon.pop.basic.info" /><!-- 쿠폰 기본정보 설정 -->
			</button>
		</li>
		<li class="on">
			<button type="button" name="title">
				<spring:message code="c.sps.coupon.pop.issued.list" /><!-- 쿠폰 발급 내역 -->
			</button>
		</li>
	</ul>
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col class="col_142" />
				<col class="col_auto" />
<!-- 				<col class="col_142" /> -->
<!-- 				<col class="col_auto" /> -->
			</colgroup>
			<tbody>
				<tr>
					<th><spring:message code="c.sps.coupon.issue.reg" /><!-- 쿠폰발급일 --></th>
					<td>
						<input type="text" ng-model="search.startDate" id="termStartDt" value="" placeholder="" datetime-picker period-start date-only/>
							~ 
						<input type="text" ng-model="search.endDate" id="termEndDt" value="" placeholder="" datetime-picker period-end date-only />
						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0"></div>
					</td>
<%-- 					<th rowspan="5" class="alignC"><spring:message code="c.sps.coupon.issued.mem.id" /><!-- 회원번호 --></th> --%>
<!-- 					<td rowspan="5"> -->
<!-- 						<textarea cols="30" rows="5" placeholder="" ng-model="search.memberNo" search-area > </textarea> -->
<!-- 					</td> -->
				</tr>
				<tr>
					<th><spring:message code="c.sps.coupon.issued.state" /><!-- 발급상태 --></th>
					<td>
						<checkbox-list ng-model="search.couponIssueState" code-group="COUPON_ISSUE_STATE_CD" all-check />
<!-- 						<input type="checkbox" id="state1" ng-click="ctrl.stateChk('ALL', 'state1')"  -->
<%-- 						ng-model="state1" value=false /> <spring:message code="c.sps.common.pop.target.type.cd.all"/><!-- 전체 -->  --%>
<!-- 						<input type="checkbox" id="state2" ng-click="ctrl.stateChk('COUPON_ISSUE_STATE_CD.ISSUE', 'state2')"  -->
<%-- 						ng-model="state2" value=false /> <spring:message code="c.sps.coupon.issue.type.issue"/><!-- 미등록 -->  --%>
<!-- 						<input type="checkbox" id="state3" ng-click="ctrl.stateChk('COUPON_ISSUE_STATE_CD.REG', 'state3')"  -->
<%-- 						ng-model="state3" value=false /> <spring:message code="c.sps.coupon.issue.type.reg"/><!-- 미사용 -->  --%>
<!-- 						<input type="checkbox" id="state4" ng-click="ctrl.stateChk('COUPON_ISSUE_STATE_CD.USE', 'state4')"  -->
<%-- 						ng-model="state4" value=false /> <spring:message code="c.sps.coupon.issue.type.use"/><!-- 사용 -->  --%>
<!-- 						<input type="checkbox" id="state5" ng-click="ctrl.stateChk('COUPON_ISSUE_STATE_CD.STOP', 'state5')"  -->
<%-- 						ng-model="state5" value=false /> <spring:message code="c.sps.coupon.issue.type.stop"/><!-- 정지 -->  --%>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.mmsMemberZts.memGradeCd" /><!-- 회원등급 --></th>
					<td>
						<checkbox-list ng-model="search.memGrade" code-group="MEM_GRADE_CD" all-check />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.sps.coupon.issued.mem" /><!-- 회원이름 --></th>
					<td>
						<select data-ng-model="search.infoType" data-ng-init="search.infoType = 'ID'">
							<option ng-repeat="info in infoType" value="{{info.val}}" >{{info.text}}</option>
						</select>
						<input type="text" ng-model="search.searchKeyword" style="width:22.5%"/>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.sps.coupon.pop.coupon.issue.type.cd" /><!-- 쿠폰 인증 코드 --></th>
					<td>
						<input type="text" ng-model="search.privateCin" style="width:22.5%"/>
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" ng-click="ctrl.resetIssuedCoupon()" class="btn_type1">
			<b><spring:message code="c.search.btn.reset" /></b>
		</button>
		<button type="button" ng-click="ctrl.searchIssuedCoupon()" class="btn_type1 btn_type1_purple">
			<b><spring:message code="c.search.btn.search" /></b>
		</button>
	</div>

	<div class="btn_alignR marginT3">
		<button type="button" class="btn_type1" ng-click="ctrl.stopIssuedCoupon()" fn-id="7_STOP">
			<b><spring:message code="c.sps.coupon.issued.stop" /></b>
		</button>
		<button type="button" class="btn_type1" ng-show="search.publicCin!='Y'" ng-click="ctrl.addExcel()" ng-disabled="{{issueStop}}" fn-id="7_ISSUE">
			<b><spring:message code="c.excel.file.upload" /></b>
		</button>
		<button type="button" class="btn_type1" ng-show="search.publicCin!='Y'" ng-click="ctrl.issueCoupon()" ng-disabled="{{issueStop}}">
			<b><spring:message code="c.sps.coupon.manual.issue" /></b>
		</button>
	</div>
	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.sps.coupon.pop.issued.list" />
			<span><spring:message code="c.search.totalCount" arguments="{{ gridIssuedCoupon.totalItems }}" /></span>
		</h3>
		
		<div class="tb_util tb_util_rePosition">
			<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()" >엑셀받기</button>
		</div>

		<div class="gridbox">
			<div class="grid" data-ui-grid="gridIssuedCoupon" 
				data-ui-grid-move-columns 
				data-ui-grid-resize-columns 
				data-ui-grid-pagination
				data-ui-grid-auto-resize 
				data-ui-grid-selection 
				data-ui-grid-row-edit
				data-ui-grid-cell-nav
				data-ui-grid-exporter
				data-ui-grid-edit 
				data-ui-grid-validate></div>
		</div>

	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>