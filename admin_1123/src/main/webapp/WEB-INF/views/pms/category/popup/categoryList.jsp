<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%-- <jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true" /> --%>
<!-- <script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script> -->

<style>
.align-center {
	text-align: center;
}
</style>

<div class="wrap_popup" 
	data-ng-app="ccsAppPopup" 
	data-ng-controller="categoryListCtrl as ctrl"
	>
<!-- 	data-ng-init="ctrl.list()" -->

	<form name="form2">
		<h2 class="sub_title1"><spring:message code="c.pms.category.search" /></h2>
		
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
						<th><spring:message code="c.search.period" /></th>
						<td>
							<input type="text" data-ng-model="search.startDate" value="" placeholder="" datetime-picker date-only /> 
							~ 
							<input type="text" data-ng-model="search.endDate" value="" placeholder="" datetime-picker date-only />
							<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button />
						</td>
					</tr>
					<tr>
						<th><spring:message code="pmsCategory.useYn" /></th>
						<td>
							<input type="checkbox" value="" id="chk1" data-ng-init="search.qnaTypeCd='all'" data-ng-model="search.qnaTypeCd" data-ng-true-value="'all'" data-ng-false-value="''" /><label for="chk1">전체</label>
							<input type="checkbox" value="" id="chk2" data-ng-model="search.qnaTypeCd" data-ng-true-value="'directQna'" data-ng-false-value="''" /><label for="chk2">사용</label>
							<input type="checkbox" value="" id="chk3" data-ng-model="search.qnaTypeCd" data-ng-true-value="'callQna'" data-ng-false-value="''" /><label for="chk3">미사용</label>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.pms.category.depth" /></th>
						<td>
							<input type="checkbox" value="" id="chk1" data-ng-init="search.qnaTypeCd='all'" data-ng-model="search.qnaTypeCd" data-ng-true-value="'all'" data-ng-false-value="''" /><label for="chk1">전체</label>
							<input type="checkbox" value="" id="chk2" data-ng-model="search.qnaTypeCd" data-ng-true-value="'directQna'" data-ng-false-value="''" /><label for="chk2">사용</label>
							<input type="checkbox" value="" id="chk3" data-ng-model="search.qnaTypeCd" data-ng-true-value="'callQna'" data-ng-false-value="''" /><label for="chk3">미사용</label>
							<input type="checkbox" value="" id="chk3" data-ng-model="search.qnaTypeCd" data-ng-true-value="'callQna'" data-ng-false-value="''" /><label for="chk3">미사용</label>
						</td>
					</tr>
					<tr>
						<th><spring:message code="pmsCategory.leafYn" /></th>
						<td>
							<input type="checkbox" value="" id="chk1" data-ng-init="search.qnaTypeCd='all'" data-ng-model="search.qnaTypeCd" data-ng-true-value="'all'" data-ng-false-value="''" /><label for="chk1">전체</label>
							<input type="checkbox" value="" id="chk2" data-ng-model="search.qnaTypeCd" data-ng-true-value="'directQna'" data-ng-false-value="''" /><label for="chk2">사용</label>
							<input type="checkbox" value="" id="chk3" data-ng-model="search.qnaTypeCd" data-ng-true-value="'callQna'" data-ng-false-value="''" /><label for="chk3">미사용</label>
						</td>
					</tr>
					<tr>
						<th><spring:message code="pmsCategory.name" /></th>
						<td>
							<input type="text" data-ng-model="search.name" value="" />
						</td>
					</tr>
					<tr>
						<th><spring:message code="pmsCategory.categoryId" /></th>
						<td>
							<input type="text" data-ng-model="search.categoryId" value="" />
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<div class="btn_alignR">
			<button type="button" data-ng-click="ctrl.reset()" class="btn_type1">
				<b><spring:message code="c.search.btn.reset" /></b>
			</button>
			<button type="button" data-ng-click="ctrl.search()" class="btn_type1 btn_type1_purple">
				<b><spring:message code="c.search.btn.search" /></b>
			</button>
		</div>
		<%-- 검색 end --%>
		
		<div class="btn_alignR" style="margin-top: 50px;">
			<button type="button" class="btn_type1 btn_type1_purple" data-ng-click="ctrl.del('/mms/qna/insert')">
				<b><spring:message code="c.common.delete" /></b>
			</button>
			<button type="button" class="btn_type1 btn_type1_purple" data-ng-click="ctrl.add('/mms/qna/insert')">
				<b><spring:message code="c.common.add" /></b>
			</button>
		</div>

		<%-- 목록 start --%>
		<div class="box_type1" style="margin-top: 10px;">
			<h3 class="sub_title2">
				<spring:message code="c.pms.category.list" />
				<span id="totalLen">
					<spring:message code="c.search.totalCount" arguments="{{ totalCount }}" />
				</span>
			</h3>

			<div class="tb_util">
			</div>

			<div class="gridbox">
				<div class="grid" data-ui-grid="grid_category" 
					data-ui-grid-move-columns data-ui-grid-resize-columns data-ui-grid-pagination data-ui-grid-auto-resize
					data-ui-grid-selection data-ui-grid-exporter data-ui-grid-edit data-ui-grid-validate>
				</div>
			</div>
			<div class="tb_bar">
				<button type="button" class="btn_grid_more" onclick="javascript:moreGrid('grid')"><spring:message code="c.search.btn.more" /></button>
			</div>
		</div>
		<%-- 목록 end --%>

		<div class="btn_alignR">
			<button type="button" class="btn_type1 btn_type1_purple" data-ng-click="ctrl.del('/mms/qna/insert')">
				<b><spring:message code="c.common.delete" /></b>
			</button>
			<button type="button" class="btn_type1 btn_type1_purple" data-ng-click="ctrl.add('/mms/qna/insert')">
				<b><spring:message code="c.common.add" /></b>
			</button>
		</div>
	</form>

</div>

