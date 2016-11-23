<%--
	화면명 : 상품 관리 > 표준카테고리 관리 > 표준카테고리 속성정보 탭
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!-- ### 속성 목록 ### -->
<div class="btn_alignR marginT3">
	<button type="button" class="btn_type1" ng-click="registerCategoryAttribute()">
		<b><spring:message code="c.pmsCategory.attribute.insert" /></b>
	</button>
</div>

<div class="box_type1 marginT1">
	<h3 class="sub_title2">
		<spring:message code="pms.category.attribute.list" />
		<span><spring:message code="c.search.totalCount" arguments="{{ grid_attributeList.totalItems }}" /></span>
	</h3>

	<div class="tb_util tb_util_rePosition">
			<button type="button" class="btn_tb_util tb_util1" ng-click="grid_attributeList.initGrid()">되돌리기</button>
		<button type="button" class="btn_tb_util tb_util2" ng-click="grid_attributeList.exportExcel()">엑셀받기</button>
	</div>

	<div class="gridbox gridbox200" >
		<div class="grid" ui-grid="grid_attributeList"   
			ui-grid-move-columns 
			ui-grid-resize-columns 
			ui-grid-pagination
			ui-grid-auto-resize 
			ui-grid-selection 
			ui-grid-row-edit
			ui-grid-cell-nav
			ui-grid-exporter
			ui-grid-edit 
			ui-grid-validate></div>
	</div>						
</div>

<!-- ### 속성값 목록 ### -->
	<div class="btn_alignR marginT3">
	<button type="button" class="btn_type1" ng-click="ctrl.saveGridData()">
		<b><spring:message code="c.common.save" /></b>
	</button>
</div>

<div class="box_type1 marginT1">
	<h3 class="sub_title2">
		<spring:message code="pms.category.attributevalue.list" />
		<span><spring:message code="c.search.totalCount" arguments="{{ grid_attributevalueList.totalItems }}" /></span>
	</h3>

	<div class="tb_util tb_util_rePosition">
			<button type="button" class="btn_tb_util tb_util1" ng-click="grid_attributevalueList.initGrid()">되돌리기</button>
		<button type="button" class="btn_tb_util tb_util2" ng-click="grid_attributevalueList.exportExcel()">엑셀받기</button>
	</div>

	<div class="gridbox gridbox200">
		<div class="grid" ui-grid="grid_attributevalueList"   
			ui-grid-move-columns 
			ui-grid-resize-columns 
			ui-grid-pagination
			ui-grid-auto-resize 
			ui-grid-selection 
			ui-grid-row-edit
			ui-grid-cell-nav
			ui-grid-exporter
			ui-grid-edit 
			ui-grid-validate></div>
	</div>
</div>    