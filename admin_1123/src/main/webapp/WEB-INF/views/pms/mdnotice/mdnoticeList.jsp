<%--
	화면명 : 상품관리 > MD상품 공지관리
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true" />
<script type="text/javascript" src="/resources/js/app/pms.app.mdnotice.list.js"></script>

<style>
.align-center {
	text-align: center;
}
</style>

<article class="con_box con_on" ng-app="mdnoticeManagerApp" ng-controller="pms_mdNoticeListApp_controller as Ctrl">
	<form name="form2">
		<h2 class="sub_title1">MD상품 공지관리</h2>
		<%-- 검색 start --%>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col class="col_142" />
					<col class="col_auto" />
					<col style="width:80px;" />				
					<col class="col_auto" />
				</colgroup>
				<tbody>
					<tr>
						<th><spring:message code="c.pms.mdnotice.period" /></th>
						<td colspan="3">
							<input type="text" data-ng-model="search.startDate" value="" placeholder="" datetime-picker date-only period-start/>											
							~
							<input type="text" data-ng-model="search.endDate" value="" placeholder="" datetime-picker date-only period-end/>			
							<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" init-button="0" date-only calendar-button />
						</td>
						<%-- <th rowspan="3" class="alignC"><spring:message code="c.pms.mdnotice.noticeNo" /></th>
						<td rowspan="3" ><textarea cols="30" rows="3" ng-model="search.noticeNo" search-area></textarea></td> --%>
					</tr>
					<tr>
						<th>전시여부</th>
						<td colspan="3">
							<checkbox-list ng-model="search.displayYn" custom="DISPLAY_YN" all-check /> 
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.ccs.notice.data" /><!-- 제목/내용 --></th>	
						<td colspan="3">
							<input type="text" style="width: 15%;" ng-model="search.title" />
						</td>
					</tr>
					<%-- <tr>
						<th><spring:message code="c.pms.mdnotice.name" /></th>
						<td colspan="3">
							<input type="text" style="width: 22.5%;" ng-model="search.title" />
						</td>
					</tr> --%>
				</tbody>
			</table>
		</div>
		
		<div class="btn_alignR">
			<button type="button" ng-click="Ctrl.reset()" class="btn_type1">
				<b><spring:message code="c.search.btn.reset" /></b>
			</button>
			<button type="button" ng-click="loadGrid()" class="btn_type1 btn_type1_purple">
				<b><spring:message code="c.search.btn.search" /></b>
			</button>
		</div>
		<%-- 검색 end --%>
		
		<div class="btn_alignR marginT3">
			<button type="button" class="btn_type1" ng-click="Ctrl.regNoticePopup()">
				<b><spring:message code="c.pms.mdnotice.btn.regNotice" /></b>
			</button>
			<!-- <button type="button" class="btn_type1" data-ng-click="Ctrl.deleteMdnoticeGrpGrid()">
				<b>삭제</b>
			</button> -->
			<%-- <button type="button" class="btn_type1 btn_type1_purple" data-ng-click="Ctrl.saveMenuGrpGrid()">
				<b><spring:message code="common.btn.save" /></b>
			</button> --%>
		</div>

		<%-- MD공지 start --%>
		<div class="box_type1 marginT1">
			<h3 class="sub_title2">
				<spring:message code="c.pms.mdnotice.title2" />
				<span id="totalLen">
					<spring:message code="c.search.totalCount" arguments="{{ myGrid.totalItems }}" />
				</span>
			</h3>

			<div class="tb_util tb_util_rePosition">
	 				<!-- <button type="button" class="btn_tb_util tb_util1" ng-click="mdnoticeGrid.initGrid()">되돌리기</button> -->
					<button type="button" class="btn_tb_util tb_util2" ng-click="Ctrl.exportMdnoticeExcel()">엑셀받기</button>
			</div>
			<div class="gridbox">
				<div class="grid" data-ui-grid="myGrid" 
					data-ui-grid-move-columns data-ui-grid-resize-columns data-ui-grid-pagination data-ui-grid-auto-resize
					data-ui-grid-selection data-ui-grid-exporter data-ui-grid-edit data-ui-grid-validate>
				</div>
			</div>
			<%-- <div class="tb_bar">
				<button type="button" class="btn_grid_more" onclick="javascript:moreGrid('grid')"><spring:message code="c.search.btn.more" /></button>
			</div> --%>
		</div>
		<%-- MD공지 리스트 end --%>

		<%-- <div class="btn_alignR">
			<button type="button" class="btn_type1 btn_type1_purple" ng-click="popup('/ccs/notice/popup/insert', 'reg')">
				<b><spring:message code="c.common.add" /></b>
			</button>
			<button type="button" class="btn_type1 btn_type1_purple" ng-click="ctrl.move('/mms/qna/insert')">
				<b><spring:message code="c.common.save" /></b>
			</button>
		</div> --%>
	</form>

</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true" />

