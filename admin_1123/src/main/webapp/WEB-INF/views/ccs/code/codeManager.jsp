<%--
	화면명 : 시스템 관리 > 코드 관리
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.code.manager.js"></script>

<article id="code" class="con_box con_on" data-ng-app="codeManagerApp" data-ng-controller="ccs_codeManagerApp_controller as ctrl">
	<form name="form2">
		<h2 class="sub_title1"><spring:message code="c.ccs.code.mng"><!--코드 관리 --></spring:message></h2>
		<div class="box_type1" ng-init="initGrid();">
			<table class="tb_type1">
				<colgroup>
					<col width="20%" />
					<col width="*%" />
				</colgroup>
				<tbody>
					<tr>
						<th><spring:message code="c.ccs.code.group.name"/> <!-- 코드그룹명--></th>
						<td>
							<input type="text" id="" value="" placeholder="" style="width:30%;" data-ng-model="search.searchGroupName"/>
						</td>
					</tr>				
					<tr>
						<th><spring:message code="c.ccs.code.group.code"/> <!-- 코드그룹코드--></th>
						<td>
							<input type="text" id="" value="" placeholder="" style="width:30%;" data-ng-model="search.searchGroupCode"/>
						</td>
					</tr>

				</tbody>
			</table>
		</div>
		
		<div class="btn_alignR">
			
			<button type="button" data-ng-click="ctrl.reset()" class="btn_type1">
				<b><spring:message code="c.search.btn.reset" /></b>
			</button>
			<button type="button" data-ng-click="searchCodeGrpGrid()" class="btn_type1 btn_type1_purple">
				<b><spring:message code="c.search.btn.search" /></b>
			</button>
		</div>
		
		<div class="btn_alignR marginT3">
			 <button type="button" class="btn_type1" data-ng-click="ctrl.addCodeGrpRow()">
				<b><spring:message code="c.ccs.codegrp.btn.register" /></b>
			</button>
			<button type="button" class="btn_type1" data-ng-click="ctrl.deleteCodeGrpGrid()">
				<b><spring:message code="c.common.delete" /></b>
			</button>
			<button type="button" class="btn_type1" data-ng-click="ctrl.saveCodeGrpGrid()">
				<b><spring:message code="c.common.save" /></b>
			</button>
		</div>
	
		<div class="box_type1 marginT1">
			<h3 class="sub_title2">
				<spring:message code="c.ccs.code.group.list" /><!-- 코드그룹목록 -->
				<span id="totalLen">
					<span><spring:message code="c.search.totalCount" arguments="{{ myCodeGrpGrid.totalItems }}" /></span>
				</span>
			</h3>
			
			<div class="tb_util tb_util_rePosition">
	 				<button type="button" class="btn_tb_util tb_util1" ng-click="codeGrpGrid.initGrid()">되돌리기</button>
<!-- 					<button type="button" class="btn_tb_util tb_util2" ng-click="ctrl.exportCodeGrpExcel()">엑셀받기</button> -->
			</div>
			<div  class="gridbox gridbox300" id="gridArea">
			<div class="grid" data-ui-grid="myCodeGrpGrid"   
				data-ui-grid-move-columns 
				data-ui-grid-resize-columns 
				data-ui-grid-pagination
				data-ui-grid-auto-resize 
				data-ui-grid-selection 
				data-ui-grid-row-edit
				data-ui-grid-cell-nav
				data-ui-grid-exporter
				data-ui-grid-edit 
				data-ui-grid-validate></div>
			</div>
		</div>
		
		<div id="codeGrid" ng-if="codeGridList==true">
			<div class="btn_alignR marginT3">
				 <button type="button" class="btn_type1" data-ng-click="ctrl.addCodeRow()">
					<b><spring:message code="c.ccs.code.btn.register" /></b>
				</button>
				<button type="button" class="btn_type1" data-ng-click="ctrl.deleteCodeGrid()">
					<b><spring:message code="c.common.delete" /></b>
				</button>
				<button type="button" class="btn_type1" data-ng-click="ctrl.saveCodeGrid()">
					<b><spring:message code="c.common.save" /></b>
				</button>
			</div>
		
			<div class="box_type1 marginT1">
				<h3 class="sub_title2">
					<spring:message code="c.ccs.code.list" /><!-- 코드목록 -->
					<span id="totalLen">
						<span><spring:message code="c.search.totalCount" arguments="{{ myCodeGrid.totalItems }}" /></span>
					</span>
				</h3>
				
				<div class="tb_util tb_util_rePosition">
		 				<button type="button" class="btn_tb_util tb_util1" ng-click="codeGrid.initGrid()">되돌리기</button>
				</div>
				
				<div  class="gridbox gridbox300" id="gridArea">
					<div class="grid" data-ui-grid="myCodeGrid"   
					data-ui-grid-move-columns 
					data-ui-grid-resize-columns 
					data-ui-grid-pagination
					data-ui-grid-auto-resize 
					data-ui-grid-selection 
					data-ui-grid-row-edit
					data-ui-grid-cell-nav
					data-ui-grid-exporter
					data-ui-grid-edit 
					data-ui-grid-validate></div>
				</div>
			</div>
		</div>
	</form>

</article>				
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>

