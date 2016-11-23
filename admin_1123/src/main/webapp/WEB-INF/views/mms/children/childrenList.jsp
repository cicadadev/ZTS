<%--
	화면명 : 다자녀 회원 관리
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/mms.app.children.manager.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<article class="con_box"  ng-app="childrenManagerApp" data-ng-controller="mms_childrenManagerApp_controller as ctrl">
	<h2 class="sub_title1">다자녀 회원 관리</h2>
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col width="15%" />
				<col width="50%" />
				<col width="15%" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr>
					<th>등록기간<!-- 등록기간 --></th>
					<td>
						<input type="text" data-ng-model="childrenSearch.startDate" value="" datetime-picker date-only period-start/>											
						~
						<input type="text" data-ng-model="childrenSearch.endDate" value="" datetime-picker date-only period-end/>

						<div class="day_group" start-ng-model="childrenSearch.startDate" end-ng-model="childrenSearch.endDate" date-only calendar-button init-button="0"/>
					</td>
					<th rowspan="2" class="alignC">다자녀카드 번호</th>
					<td rowspan="2">
						<textarea cols="30" rows="6" ng-model="childrenSearch.cardNos" placeholder="" style="height:80px;" search-area></textarea>
					</td>
				</tr>
				<tr>
					<th>다자녀 카드 유형</th>
					<td>
						<select ng-model="childrenSearch.childrencardTypeCd" select-code="CHILDRENCARD_TYPE_CD">
							<option value="">전체</option>
						</select>
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" class="btn_type1" data-ng-click="reset()">
			<b><spring:message code="common.search.btn.init" /></b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" ng-click="myGrid.loadGridData()">
			<b><spring:message code="common.search.btn.search" /></b>
		</button>
	</div>
	
	<div class="btn_alignR marginT3">
		<button type="button" class="btn_type1" ng-click="ctrl.addRow()">
			<b>행추가</b>
		</button>
		<button type="button" class="btn_type1" ng-click="ctrl.batchChildrencardUpload()">
			<b>엑셀 업로드</b>
		</button>
		<button type="button" class="btn_type1" ng-click="myGrid.saveGridData(null, myGrid.loadGridData)">
			<b><spring:message code="c.common.save" /><!-- 저장 --></b>
		</button>
	</div>


	<!-- ### 회원 목록 ### -->
	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			목록
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_children.totalItems }}" /></span>
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
			<button type="button" class="btn_tb_util tb_util1" ng-click="myGrid.initGrid()">되돌리기</button>
			<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
		</div>

		<div class="gridbox gridbox200">
			<div class="grid" data-ui-grid="grid_children"
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