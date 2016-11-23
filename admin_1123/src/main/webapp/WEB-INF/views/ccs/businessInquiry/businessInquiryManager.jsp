<%--
	화면명 : 입점상담 관리 > 입점상담 검색 목록 조회
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.business.inquiry.manager.js"></script>

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

<article class="con_box con_on" data-ng-app="businessInquiryApp" data-ng-controller="businessInquiryListController as ctrl">
	<form name="form2">
			<!-- ### 입점상담 검색 ### -->
			<h2 class="sub_title1">입점상담 관리</h2>
			<div class="box_type1">
				<table class="tb_type1">
					<colgroup>
						<col width="9%" />
						<col width="34%" />
						<col width="9%" />
						<col width="*" />
					</colgroup>
					<tbody>
						<th>상담신청일</th>
						<td colspan="3">
							<input type="text" ng-model="search.startDate" datetime-picker period-start date-only/>										
							~
							<input type="text" ng-model="search.endDate" datetime-picker period-end date-only/>
							<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0"/>
						</td>
						<tr>
							<th>MD ID / 명</th>
							<td colspan="3">
								<input type="text" value="" ng-model="search.mdName" style="width:15%;"/>
								<button type="button" class="btn_type2" ng-click="ctrl.searchMd()">
									<b><spring:message code="c.search.btn.search" /></b>
								</button>
								<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : search.mdName == ''}"  ng-click="ctrl.eraser('md')"><spring:message code="c.search.btn.eraser"/></button>
							</td>
						</tr>
						<tr>
							<th>회사명</th>
							<td colspan="3">
								<input type="text" style="width:15%;" data-ng-model="search.searchKeyword"/>
							</td>						
						</tr>							
					</tbody>
				</table>
			</div>

			<div class="btn_alignR">
				<button type="button" data-ng-click="ctrl.reset()" class="btn_type1">
					<b><spring:message code="c.search.btn.reset" /></b>
				</button>
				<button type="button" data-ng-click="searchGrid()" class="btn_type1 btn_type1_purple">
					<b><spring:message code="c.search.btn.search" /></b>
				</button>
			</div>
			<!-- ### 입점상담 검색 ### -->

			<div class="box_type1 marginT3">
				<h3 class="sub_title2">
					상담 목록
					<span><spring:message code="c.search.totalCount" arguments="{{ gridBusinessInquiry.totalItems }}"/></span>
				</h3>
				
				<div class="tb_util tb_util_rePosition">
					<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
				</div>
				
				<div class="gridbox gridbox500">
					<div class="grid" data-ui-grid="gridBusinessInquiry"   
							data-ui-grid-move-columns 
							data-ui-grid-resize-columns 
							data-ui-grid-pagination
							data-ui-grid-auto-resize 
							data-ui-grid-exporter
							data-ui-grid-edit 
							data-ui-grid-selection
							data-ui-grid-validate
							data-ui-grid-row-edit
							data-ui-grid-cell-nav></div>
				</div>
			</div>
		</form>
	</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>