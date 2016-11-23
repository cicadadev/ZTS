<%--
	화면명 : 상품 관리 > 표준카테고리 관리 > 속성 등록 팝업
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.category.manager.js"></script>

<div ng-app="categoryApp" ng-controller="pms_categoryAttributeInsertPopApp_controller as ctrl" ng-init="ctrl.initPopup()" id="categoryAttributeInsertPopup" class="wrap_popup">
	<h1 class="sub_title1">속성 검색</h1>
	
	<div class="box_type1">
		<form name="form2">
			<table class="tb_type1">
				<colgroup>
					<col width="142px" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th><spring:message code="c.pmsAttribute.type" /></th>
						<td>
							<checkbox-list ng-model="search.attributeType" code-group="ATTRIBUTE_TYPE_CD" all-check ></checkbox-list>
						</td>
					</tr>
					<tr>
						<th><spring:message code="pmsAttribute.name" /></th>
						<td>
							<input type="text" ng-model="search.name" />
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>

	<div class="btn_alignR">
		<button type="button" class="btn_type1" ng-click="ctrl.reset()" >
			<b><spring:message code="c.search.btn.reset" /></b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" ng-click="ctrl.searchAttribute(search.attributeType)" >
			<b><spring:message code="c.search.btn.search" /></b>
		</button>
	</div>
		
	<div class="box_type1 marginT3">
		<h3 class="sub_title2">
			<spring:message code="c.pmsAttribute.title2" />
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_attr.totalItems }}" /></span>
		</h3>
	
		<div class="gridbox gridbox200">
			<div class="grid" ui-grid="grid_attr"
				ui-grid-validate 
				ui-grid-selection 
		 		ui-grid-exporter 
		 		ui-grid-pagination 
		 		ui-grid-cell-nav 
		 		ui-grid-auto-resize 
		 		ui-grid-resize-columns></div>
		</div>
	</div>
	
	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /></b>
		</button>
		<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.saveCategoryAttribute()">
			<b><spring:message code="c.common.select" /></b>
		</button>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>