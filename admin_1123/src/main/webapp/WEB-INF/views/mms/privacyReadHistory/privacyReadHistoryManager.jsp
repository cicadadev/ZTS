<%--
	화면명 : 개인정보 조회 이력 관리
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/mms.app.privacyReadHistory.manager.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<article class="con_box"  ng-app="readHistoryManagerApp" data-ng-controller="mms_privacyReadHistoryManagerApp_controller as ctrl">
	<h2 class="sub_title1">개인정보 조회 이력 관리</h2>
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col width="15%" />
				<col width="60%" />
				<col width="9%" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr>
					<th>조회 기간</th>
					<td>
						<input type="text" data-ng-model="search.startDate" value="" placeholder="" datetime-picker date-only period-start/>											
						~
						<input type="text" data-ng-model="search.endDate" value="" placeholder="" datetime-picker date-only period-end/>

						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0"/>
					</td>
					<th rowspan="2" class="alignC">사용자 ID</th>
					<td rowspan="2">
						<textarea cols="30" rows="6" ng-model="search.userId" placeholder="" style="height:80px;"></textarea>
					</td>
				</tr>
				<tr>
					<th>사용자 명</th>
					<td>
						<input type="text" ng-model="search.name" value="" placeholder="" style="width:30%;" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" class="btn_type1" data-ng-click="ctrl.reset()">
			<b><spring:message code="common.search.btn.init" /></b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" data-ng-click="ctrl.search()">
			<b><spring:message code="common.search.btn.search" /></b>
		</button>
	</div>
	

	<!-- ### 블랙리스트 목록 ### -->
	<div class="box_type1 marginT3">
		<h3 class="sub_title2">
			조회이력 목록
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_readHistory.totalItems }}" /></span>
		</h3>

		<div class="tb_util">
			<select style="width:105px;">
				<option value="">200건</option>
			</select>

			<span class="page">
				<button type="button" class="btn_prev">이전</button>
				<input type="text" value="1" placeholder="" />
				<u>/</u><i>24</i>
				<button type="button" class="btn_next">다음</button>
			</span>

			<button type="button" class="btn_type2">
				<b>이동</b>
			</button>
		</div>
		
		<div class="tb_util tb_util_rePosition">
<!-- 			<button type="button" class="btn_tb_util tb_util1" ng-click="myGrid.initGrid()">되돌리기</button> -->
			<button type="button" class="btn_tb_util tb_util2" fn-id="40_EXCEL" ng-click="myGrid.exportExcel()">엑셀받기</button>
		</div>

		<div class="gridbox gridbox200">
			<div class="grid" data-ui-grid="grid_readHistory"
					data-ui-grid-move-columns
					data-ui-grid-row-edit
					data-ui-grid-resize-columns 
					data-ui-grid-pagination
					data-ui-grid-auto-resize
					data-ui-grid-cell-nav
					data-ui-grid-selection 
					data-ui-grid-exporter
					data-ui-grid-edit
					data-ui-grid-validate></div>
		</div>
	</div>
	
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>