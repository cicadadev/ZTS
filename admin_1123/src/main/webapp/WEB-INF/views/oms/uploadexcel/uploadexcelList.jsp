<%--
	화면명 : 주문 관리 > 주문업로드
	작성자 : peter
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<!-- <link rel="stylesheet" type="text/css" href="/resources/css/oms.css" /> -->
<script type="text/javascript" src="/resources/js/app/oms.app.uploadexcel.list.js"></script>

<article class="con_box" ng-app="uploadExcelApp" ng-controller="oms_uploadExcelListApp_controller as ctrl">
	<div class="wrap_popup">
		<%-- <h1 class="sub_title1"><spring:message code="c.pmsAttribute.title1" /></h1> --%>

		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col style="width:140px;" />
					<col style="width:300px;" />
					<col class="col_442" />
					<col class="col_auto" />
				</colgroup>
				<tbody>
					<tr>
						<th><spring:message code="c.oms.uploadexcel.searchTitle" /></th><!-- 일괄등록 파일선택 -->
						<td>
							<label><spring:message code="ccsSite.name" /></label><!-- 사이트명 -->
							<select ng-model="search.siteId" ng-options="site.siteId as site.name for (idx, site) in siteList track by site.siteId">
								<option value=""><spring:message code="c.select.all" /></option><!-- 전체 -->
							</select>
						</td>
						<td>
							<div class="input_file">
								<input type="file" excel-upload id="excel" />
								<input type="text" placeholder="업로드할 파일을 선택해 주세요." ng-model="filePath" style="width:300px;"/>
								<button type="button" class="btn_type2 btn_addFile">
									<b><spring:message code="c.common.file.search" /></b><!-- 찾아보기 -->
								</button>
								<button type="button" class="btn_eraser" ng-click="ctrl.eraser()" />
							</div>
						</td>
						<td>
							<!-- <div class="btn_alignC" style="margin-top: 20px;"> -->
								<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.uploadExcel()">
									<b><spring:message code="c.pmsProductreserve.batchUpload" /></b><!-- 일괄 업로드 -->
								</button>
							<!-- </div> -->
						</td>
					</tr>
				</tbody>
			</table>
		</div>

		<div class="btn_alignR marginT3" >
			<button type="button" class="btn_type1" ng-click="ctrl.saveExcelOrder()">
				<b><spring:message code="c.common.save" /></b>
			</button>
		</div>

		<div class="box_type1 marginT1">
			<h3 class="sub_title2">
				<spring:message code="c.oms.uploadexcel.gridTitle" />
				<span><spring:message code="c.search.totalCount" arguments="{{ grid_uploadexcel.totalItems }}" /></span>
			</h3>
			
			<div class="tb_util tb_util_rePosition">
				<!-- <button type="button" class="btn_tb_util tb_util1" ng-click="myGrid.initGrid()">되돌리기</button> -->
				<!-- <button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel('attribute')">엑셀받기</button> -->
				<!-- <button type="button" class="btn_tb_util tb_util5" ng-click="ctrl.addRow()">행추가</button> -->
			</div>

			<div class="gridbox" >
				<div class="grid" ui-grid="grid_uploadexcel"
					ui-grid-validate 
					ui-grid-selection 
			 		ui-grid-exporter 
			 		ui-grid-pagination 
			 		ui-grid-cell-nav 
			 		ui-grid-auto-resize 
			 		ui-grid-resize-columns
			 		ui-grid-row-edit 
			 		ui-grid-edit 
				></div>
			</div>
		</div>
		
	</div>
</article>

<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>