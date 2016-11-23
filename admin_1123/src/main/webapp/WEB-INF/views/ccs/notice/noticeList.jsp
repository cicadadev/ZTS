<%--
	화면명 : 공지사항관리 > 공지사항 목록 조회
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true" />
<script type="text/javascript" src="/resources/js/app/ccs.app.notice.js"></script>

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
</style>

<article class="con_box con_on" ng-app="noticeApp" ng-controller="listCtrl as ctrl">

 	<form name="form2">
		<h2 class="sub_title1"><spring:message code="c.ccs.notice.mng" /></h2>
		
		<%-- 검색 start --%>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="9%" />
					<col width="34%" />
					<col width="9%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>공지사항 등록일<%-- <spring:message code="c.search.period" /> --%></th>
						<td colspan="3">
							<input type="text" style="width:120px;" ng-model="search.startDate" datetime-picker period-start date-only />										
							~
							<input type="text" style="width:120px;" ng-model="search.endDate" datetime-picker period-end date-only />
							<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0"/>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.ccs.notice.type" /><!-- 공지유형 --></th>
						<td>
							<checkbox-list ng-model="search.noticeType" custom="NOTICE_TYPE" all-check />
						</td>
						<th>공지등록자<!-- 공지등록자 유저검색 --></th>	
						<td>
							<input type="text" style="width:15%;" ng-change="ctrl.change()" ng-model="search.insInfoName" />
							<button type="button" class="btn_type2" ng-click="ctrl.searchUser()"/>
								<b><spring:message code="c.search.btn.search"/></b>
							</button>
							<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : search.insInfoName == null || search.insInfoName == ''}"  ng-click="ctrl.eraser()"><spring:message code="c.search.btn.eraser"/></button>
						</td>
					</tr>
					<tr>
						<th>전시여부<%-- <spring:message code="c.ccs.notice.displayYn" /> --%><!-- 사용여부 --></th>
						<td>
							<checkbox-list ng-model="search.displayYn" custom="DISPLAY_YN" all-check />
						</td>
						<th><spring:message code="c.ccs.notice.data" /><!-- 제목/내용 --></th>	
						<td>
							<input type="text" style="width: 15%;" ng-model="search.title" />
						</td>
					</tr>
					<tr>
						<th>TOP 노출여부</th>
						<td colspan="3">
							<checkbox-list ng-model="search.topNoticeYn" custom="YES_NO" all-check />
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<div class="btn_alignR">
			<button type="button" ng-click="search.reset()" class="btn_type1">
				<b><spring:message code="c.search.btn.reset" /></b>
			</button>
			<button type="button" ng-click="list.search()" class="btn_type1 btn_type1_purple">
				<b><spring:message code="c.search.btn.search" /></b>
			</button>
		</div>
		<%-- 검색 end --%>
		
		<div class="btn_alignR marginT3">
			<button type="button" class="btn_type1" ng-click="popup.insert1()">
				<b>FO일반공지 등록<%-- <spring:message code="c.ccs.notice.insert.front" /> --%><!-- FO 공지 등록 --></b>
			</button>
			<button type="button" class="btn_type1" ng-click="popup.insert2()">
				<b>당첨자 공지 등록<%-- <spring:message code="c.ccs.notice.insert.event" /> --%><!-- FO 이벤트 등록 --></b>
			</button>
			<button type="button" class="btn_type1" ng-click="popup.insert3()">
				<b>BO 공지 등록<%-- <spring:message code="c.ccs.notice.insert.back" /> --%><!-- BO 공지 등록 --></b>
			</button>
			<button type="button" class="btn_type1" ng-click="popup.insert4()">
				<b>PO 공지 등록<%-- <spring:message code="c.ccs.notice.insert.partner" /> --%><!-- PO 공지 등록 --></b>
			</button>
			<button type="button" class="btn_type1" ng-click="list.remove()">
				<b>삭제</b>
			</button>
		</div>

		<%-- 목록 start --%>
		<div class="box_type1 marginT1">
			<h3 class="sub_title2">
				<spring:message code="c.ccs.notice.list" />
				<span id="totalLen">
					<spring:message code="c.search.totalCount" arguments="{{ grid_notice.totalItems }}" />
				</span>
			</h3>

			<div class="tb_util tb_util_rePosition">
				<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
			</div>

			<div class="gridbox">
				<div class="grid" data-ui-grid="grid_notice" 
					data-ui-grid-move-columns 
					data-ui-grid-resize-columns 
					data-ui-grid-pagination 
					data-ui-grid-auto-resize
					data-ui-grid-selection 
					data-ui-grid-exporter 
					data-ui-grid-cell-nav
					data-ui-grid-edit 
					data-ui-grid-row-edit 
					data-ui-grid-validate>
				</div>
			</div>

		</div>
		<%-- 목록 end --%>
 	</form>

</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true" />

