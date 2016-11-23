<%--
	화면명 : PO 공지사항관리 > 공지사항 목록 조회
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

<article class="con_box con_on" ng-app="noticeApp" ng-controller="polistCtrl as ctrl" style="height:450px">

 	<form name="form2">
		<%-- <h2 class="sub_title1"><spring:message code="c.ccs.notice.mng" /></h2> --%>
		
		<div class="box_type1" ng-init="ctrl.detail()">
			<h3 class="sub_title2">
				공지사항
				<!-- <a href="#none" ng-click="ctrl.openNoticeList()" class="btn_more">more</a> -->
			</h3>
			<ul class="list_type1">
				<li ng-repeat="notice in ccsNotices">
					<a href="#none" ng-click="ctrl.detailNotice(notice.noticeNo)">{{notice.title}}</a>
					<span>{{notice.insDt}}</span>
				</li>
			</ul>
		</div>
		<br/>
		
		<div class="box_type1 adp-month" style="text-align: center; font-size:20">
			<button type="button" class="adp-prevYear" ng-click="ctrl.pageMove('first')">&laquo;</button>
			<button type="button" class="adp-prev" ng-click="ctrl.pageMove('prev')">&lsaquo;</button>
			<a href="#none" ng-click="ctrl.pageMove(page.page)" ng-repeat="page in paging">{{page.page}}
			</a>
			<!-- <a href="#none" ng-click="ctrl.pageMove()"></a> -->
			<button type="button" class="adp-next" ng-click="ctrl.pageMove('next')">&rsaquo;</button>
			<button type="button" class="adp-nextYear" ng-click="ctrl.pageMove('last')">&raquo;</button>
		</div>
			
		<%-- 검색 start --%>
		<%-- <div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="80%" />
					<col width="20%" />
				</colgroup>
				<tbody>
					<tr>
						<th>등록일<spring:message code="c.search.period" /></th>
						<td colspan="3">
							<input type="text" style="width:120px;" ng-model="search.startDate" datetime-picker period-start date-only/>										
							~
							<input type="text" style="width:120px;" ng-model="search.endDate" datetime-picker period-end date-only/>
							<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" calendar-button init-button="0"/>
						</td>
					</tr>
				</tbody>
			</table>
		</div> --%>
		
		<!-- <div class="btn_alignC">
			<button type="button" ng-click="ctrl.close();" class="btn_type1">
				<b>확인</b>
			</button>
		</div> -->
		<%-- 검색 end --%>
	
		
		<%-- 목록 end --%>
 	</form>

</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true" />

