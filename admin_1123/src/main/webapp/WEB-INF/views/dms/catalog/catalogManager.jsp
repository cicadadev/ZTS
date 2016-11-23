<%--
	화면명 : 전시 관리 > 브랜드 컨텐츠 관리
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/dms.app.catalog.manager.js"></script>

<article class="con_box con_on" ng-app="catalogApp" ng-controller="dms_catalogManagerApp_controller as ctrl">
	<h2 class="sub_title1"><spring:message code="dms.catalog.manager.title" /></h2>
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col class="col_142" />
				<col class="*" />
			</colgroup>
			<tbody>
				<tr>
					<th><spring:message code="c.dms.catalog.register.period"/></th>
					<td>
						<input type="text" style="width:120px;" ng-model="search.startDate" datetime-picker period-start date-only/>										
							~
						<input type="text" style="width:120px;" ng-model="search.endDate" datetime-picker period-end date-only/>
						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0"/>
					</td>
				</tr>
				<tr>
					<th><spring:message code="dmsCatalog.displayYn"/></th>
					<td>
						<checkbox-list ng-model="search.displayYn" custom="DISPLAY_YN" all-check ></checkbox-list>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.dms.catalog.catalogTypeCd" /></th>
					<td>
						<checkbox-list ng-model="search.catalogTypeCd" code-group="CATALOG_TYPE_CD" all-check ></checkbox-list>
					</td>						
				</tr>
				<tr>
					<th><spring:message code="c.dms.catalog.brand.noname" /></th>
					<td>
						<input type="text" placeholder="" ng-model="search.brandId" required />
						<input type="text" placeholder="" ng-model="search.brandName" required />
						<button type="button" class="btn_type2" ng-click="ctrl.brandSearch()">
							<b><spring:message code="c.search.btn.search" /></b>
						</button>
						<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : search.brandId == null || search.brandName == null}" ng-click="ctrl.eraseBrand()"><spring:message code="c.search.btn.eraser" /></button>
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" class="btn_type1" ng-click="reset()">
			<b><spring:message code="common.search.btn.init" /></b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" ng-click="catalog_grid.loadGridData()">
			<b><spring:message code="common.search.btn.search" /></b>
		</button>
	</div>
	
	<div class="btn_alignR marginT3">
		<button type="button" class="btn_type1" ng-click="ctrl.registerCatalog()">
			<b><spring:message code="dms.catalog.btn.register" /></b>
		</button>
		<button type="button" class="btn_type1" ng-click="catalog_grid.deleteGridData()">
			<b><spring:message code="common.btn.del" /></b>
		</button>					
		<button type="button" class="btn_type1" ng-click="ctrl.saveGridData()">
			<b><spring:message code="common.btn.save" /></b>
		</button>
	</div>
	
	<!-- ### 컨텐츠 목록 ### -->
	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="dms.catalog.list.title" />
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_catalog.totalItems }}" /></span>
		</h3>
		
		<div class="tb_util tb_util_rePosition">
			<button type="button" class="btn_tb_util tb_util1" ng-click="catalog_grid.initGrid()">되돌리기</button>
			<button type="button" class="btn_tb_util tb_util2" ng-click="catalog_grid.exportExcel()">엑셀받기</button>
		</div>

		<div class="gridbox gridbox300">
			<div class="grid" ui-grid="grid_catalog"
				ui-grid-move-columns
				ui-grid-row-edit
				ui-grid-resize-columns 
				ui-grid-pagination
				ui-grid-auto-resize
				ui-grid-cell-nav
				ui-grid-selection 
				ui-grid-exporter
				ui-grid-edit
				ui-grid-validate></div>
		</div>		
	</div>

</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>