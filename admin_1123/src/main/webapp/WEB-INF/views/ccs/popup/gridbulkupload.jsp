<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true" />
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="wrap_popup con_box" ng-app="ccsAppPopup" data-ng-controller="ccs_gridbulkupload_controller as ctrl">
	<h2 class="sub_title1">{{bulkTypeTxt}}</h2>
	<div class="btn_alignR" style="margin-top: 20px;">
		<button type="button" class="btn_type2 btn_type2_white" data-ng-click="ctrl.downTemplate()">
			<b><spring:message code="c.excel.templateDownload" /></b><%-- 엑셀템플릿 다운로드 --%>
		</button>
	</div>
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col width="16%" />
				<col width="*%" />
			</colgroup>
			<tbody>
				<tr>
					<th><spring:message code="c.pmsProductreserve.choiceRegFile" /></th><%-- 등록 파일선택 --%>
					<td>
						<div class="input_file">
							<input type="file" excel-upload />
							<input type="text" placeholder="업로드할 파일을 선택해 주세요." ng-model="filePath" width="250px;" name="filePath"/>
							<button type="button" class="btn_type2 btn_addFile">
								<b><spring:message code="c.common.file.search" /></b><%-- 찾아보기 --%>
							</button>
							<button type="button" class="btn_eraser" ng-click="ctrl.eraser()" />
						</div>
					</td>
				</tr>
			</tbody>
		</table>
		<div class="btn_alignC" style="margin-top: 20px;">
			<button type="button" class="btn_type3 btn_type3_purple" data-ng-click="ctrl.uploadExcel()">
				<b><spring:message code="c.pmsProductreserve.batchUpload" /></b><%-- 일괄 업로드 --%>
			</button>
		</div>
	</div>
	<div class="box_type1" style="margin-top: 50px;">
		<table class="tb_type1">
			<colgroup>
				<col width="16%" />
				<col width="16%" />
				<col width="16%" />
			</colgroup>
			<tbody>
				<tr>
					<th class="alignC"><spring:message code="c.pmsProductreserve.totalCount" /></th><%-- 총항목 수 --%>
					<th class="alignC"><spring:message code="c.pmsProductreserve.uploadSuccess" /></th><%-- 업로드 성공 --%>
					<th class="alignC"><spring:message code="c.pmsProductreserve.uploadFail" /></th><%-- 업로드 실패 --%>
				</tr>
				<tr>
					<td class="alignC">{{totalCnt}}</td>
					<td class="alignC">{{successCnt}}</td>
					<td class="alignC">{{failCnt}}</td>
				</tr>
			</tbody>
		</table>
		<div class="btn_alignR" style="margin-top: 10px;">
			<button type="button" class="btn_type2 btn_type2_white" data-ng-click="ctrl.downFailDataExcel()">
				<b><spring:message code="c.excel.failDataDownload" /></b><%-- 오류데이터 다운로드 --%>
			</button>
		</div>
	</div>

	<div class="btn_alignC" style="margin-top: 20px;">
		<button type="button" class="btn_type3 btn_type3_purple" data-ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /></b><%-- 닫기 --%>
		</button>
	</div>

</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true" />