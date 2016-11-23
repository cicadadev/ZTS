<%--
	화면명 : 상품 관리 > 브랜드 관리 > 브랜드 관리
	작성자 : eddie
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.brand.manager.js"></script>

<article class="con_box" ng-app="brandManagerApp" ng-controller="pms_brandManagerApp_controller as ctrl">
	<h2 class="sub_title1"><spring:message code="pms.brand.manager.title" /></h2>	

	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col class="col_142" />
				<col class="*" />
			</colgroup>
			<tbody>
				<tr>
					<th><spring:message code="c.pmsBrand.insertDt" /><!-- 브랜드 등록일 --></th>
					<td>
						<input type="text" ng-model="search.startDate" value="" placeholder="" datetime-picker period-start date-only/>											
						~
						<input type="text" ng-model="search.endDate" value="" placeholder="" datetime-picker period-end date-only/>
						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0"/>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.pmsBrand.displayYn" /></th>
					<td>
						<checkbox-list ng-model="search.displayYn" custom="DISPLAY_YN" all-check ></checkbox-list> 
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.pmsBrand.templateType" /><!-- 템플릿 유형 --></th>
					<td>
						<select style="min-width:200px;" ng-model="search.templateId" 
							ng-options="template.templateId as template.name for template in templateList">
							<option value="">전체</option>
						</select>
					</td>						
				</tr>
				<tr>
					<th><spring:message code="c.pmsBrand.brand" /><!-- 브랜드 --></th>
					<td>
						<select style="min-width:100px;" ng-model="search.searchKeyword">
							<option value="name"><spring:message code="pmsBrand.name" /></option>
							<option value="brandId"><spring:message code="c.pmsBrand.brandId" /></option>
						</select>
						<input type="text" ng-model="search.brand" value="" placeholder="" style="width:20%;" />
					</td>
				</tr>					
			</tbody>
		</table>	
	</div>
	
	<div class="btn_alignR">
		<button type="button" class="btn_type1" ng-click="ctrl.reset()" >
			<b><spring:message code="c.search.btn.reset" /></b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" ng-click="searchBrandData()" >
			<b><spring:message code="c.search.btn.search" /></b>
		</button>
	</div>
	
	<div class="btn_alignR marginT3">
		<button type="button" class="btn_type1" ng-click="ctrl.openPopupInsert()">
			<b><spring:message code="pms.brand.btn.register" /></b>
		</button>
		<button type="button" class="btn_type1" ng-click="brand_grid.deleteGridData()">
			<b><spring:message code="common.btn.del" /></b>
		</button>		
		<button type="button" class="btn_type1" ng-click="ctrl.saveGridData()">
			<b><spring:message code="common.btn.save" /></b>
		</button>		
	</div>
	
	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.pmsBrand.brandList" />
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_brand.totalItems }}" /></span>
		</h3>
		
		<div class="tb_util tb_util_rePosition">
			<button type="button" class="btn_tb_util tb_util1" ng-click="brand_grid.initGrid()">되돌리기</button>
			<button type="button" class="btn_tb_util tb_util2" ng-click="brand_grid.exportExcel()">엑셀받기</button>
		</div>

		<div class="gridbox gridbox300">
			<div class="grid" ui-grid="grid_brand"
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
	
</article>

<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>