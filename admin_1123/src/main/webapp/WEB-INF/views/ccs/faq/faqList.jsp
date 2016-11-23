<%--
	화면명 : 문의관리 화면 > 문의관리 검색 목록 조회
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true" />
<script type="text/javascript" src="/resources/js/app/ccs.app.faq.js"></script>

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

<article class="con_box con_on"  ng-app="faqApp" ng-controller="listCtrl as ctrl">

<!-- 	<form name="form2"> -->
		<h2 class="sub_title1"><spring:message code="c.ccs.faq.mng" /></h2>
		
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
						<th><spring:message code="c.ccs.faq.displayYn" /><!-- 전시여부 --></th>
						<td>
							<checkbox-list ng-model="search.displayYn" custom="DISPLAY_YN" all-check />
							<%-- <input type="checkbox" ng-model="search.displayYn" id="chk1" ng-click="ctrl.displayYnCheck('all')"><label for="chk1"><spring:message code="c.select.all"/><!-- 전체 --></label> 
							<input type="checkbox" ng-model="search.displayY" id="chk2" ng-click="ctrl.displayYnCheck()"><label for="chk2"><spring:message code="c.input.radio.displayY"/><!-- 전시 --></label> 
							<input type="checkbox" ng-model="search.displayN" id="chk3" ng-click="ctrl.displayYnCheck()"><label for="chk3"><spring:message code="c.input.radio.displayN"/><!-- 미전시 --></label> --%> 
						</td>
						<th><spring:message code="c.ccs.faq.insId" /><!-- FAQ등록자 --></th>
						<td>
							<input type="text" style="width:200px;" ng-change="ctrl.change()" ng-model="search.insInfoName" />
							<button type="button" class="btn_type2" ng-click="ctrl.searchUser()"/>
								<b><spring:message code="c.search.btn.search"/></b>
							</button>
							<button type="button" class="btn_eraser" ng-click="ctrl.eraser()">지우개</button>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.ccs.faq.type" /></th>
						<td>
							<select-list ng-model="search.faqTypeCd" code-group="FAQ_TYPE_CD" all-check></select-list>
						</td>
						<th><spring:message code="c.ccs.faq.data" /><!-- 제목/내용 --></th>
						<td>
							<%--
							<select ng-model="search.infoType" ng-options="info.text for info in infoTypes track by info.val" ng-init="search.infoType = infoTypes[0]"></select>
							--%>
							<!-- <select ng-model="search.infoType" ng-init="search.infoType = 'title'">
								<option ng-repeat="info in infoTypes" value="{{info.val}}" >{{info.text}}</option>
							</select> -->
							<input type="text" style="width:200px;" ng-model="search.info" />
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
			<button type="button" class="btn_type1" ng-click="popup.insert()">
				<b>FAQ 등록<%-- <spring:message code="c.ccs.qna.insert" /> --%></b>
			</button>
			<button type="button" class="btn_type1" ng-click="list.remove()">
				<b>FAQ 삭제<%-- <spring:message code="common.btn.del" /> --%></b>
			</button>
			<button type="button" class="btn_type1" ng-click="list.save()">
				<b><spring:message code="common.btn.save" /></b>
			</button>
		</div>

		<%-- 목록 start --%>
		<div class="box_type1 marginT1" >
			<h3 class="sub_title2">
				<spring:message code="c.ccs.faq.list" />
				<span id="totalLen">
					<spring:message code="c.search.totalCount" arguments="{{ grid_faq.totalItems }}" />
				</span>
			</h3>

			<div class="tb_util tb_util_rePosition">
				<button type="button" class="btn_tb_util tb_util1" ng-click="myGrid.initGrid()">되돌리기</button>
				<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
			</div>

			<div class="gridbox">
				<div class="grid" ui-grid="grid_faq"   
						ui-grid-move-columns 
						ui-grid-resize-columns 
						ui-grid-pagination
						ui-grid-auto-resize 
						ui-grid-exporter
						ui-grid-row-edit
						ui-grid-cell-nav
						ui-grid-selection
						ui-grid-edit
						ui-grid-validate>
				</div>
			</div>
		</div>
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true" />

