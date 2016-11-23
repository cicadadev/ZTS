<%--
	화면명 : 전시 관리 > 브랜드 컨텐츠 관리 > 브랜드 컨텐츠 등록(상세) 팝업 > 컨텐츠 이미지 정보 탭
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/dms.app.catalog.manager.js"></script>

<div ng-app="catalogApp" ng-controller="dms_catalogImgInfoPopApp_controller as ctrl" class="wrap_popup">
	<h1 class="sub_title1"><spring:message code="dms.catalog.detail.title" /></h1>
	<ul class="tab_type2">
		<li>
			<button type="button" ng-click="ctrl.moveTab($event, 'detail')" name="detail"><spring:message code="c.dms.catalog.basicInfo"/><!-- 기본정보 --></button>
		</li>
		<li class="on">
			<button type="button" ng-click="ctrl.moveTab($event, 'title')" name="title"><spring:message code="c.dms.catalog.imgInfo"/><!-- 컨텐츠 정보 --></button>
		</li>
	</ul>
	
	<!-- ### 이미지 목록 그리드 ### -->
	<div class="btn_alignR marginT3" ng-if="gridShow1 == true">
		<input type="file" size="30" id="file" style="display:none;" callback="dmsCatalogimgCallback" zip-file-upload/>
		<button type="button" class="btn_type1" onclick="document.getElementById('file').click();">
			<b>이미지 일괄 등록(테스트)</b>
		</button>
		<button type="button" class="btn_type1" ng-click="ctrl.openItemRegPopup('img')">
			<b><spring:message code="dms.catalog.img.btn.register" /></b>
		</button>
		<button type="button" class="btn_type1" ng-click="ctrl.imgGridData()">
			<b><spring:message code="c.common.delete" /></b>
		</button>
		<button type="button" class="btn_type1" ng-click="catalogImg_grid.saveGridData(null, catalogImg_grid.loadGridData)">
			<b><spring:message code="c.common.save" /></b>
		</button>
	</div>
	
	<div class="box_type1 marginT1" ng-if="gridShow1 == true">
		<h3 class="sub_title2">
			<spring:message code="dms.catalog.img.list" />
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_catalogImg.data.length }}" /></span>
		</h3>
		
		<div class="tb_util tb_util_rePosition2">
			<button type="button" class="btn_tb_util tb_util1" ng-click="catalogImg_grid.initGrid()">되돌리기</button>
		</div>

		<div class="gridbox gridbox200">
			<div class="grid" ui-grid="grid_catalogImg"   
				ui-grid-move-columns 
				ui-grid-resize-columns 
				ui-grid-auto-resize 
				ui-grid-selection 
				ui-grid-row-edit
				ui-grid-cell-nav
				ui-grid-exporter
				ui-grid-edit 
				ui-grid-validate></div>				
		</div>
	</div>
	
	<!-- ### 상품 목록 그리드 ### -->
	<div class="btn_alignR marginT3" ng-if="gridShow2 == true">
		<button type="button" class="btn_type1" ng-click="ctrl.searchProduct()">
			<b><spring:message code="dms.catalog.product.btn.register" /></b>
		</button>
		<button type="button" class="btn_type1" ng-click="catalogProduct_grid.deleteGridData()">
			<b><spring:message code="c.common.delete" /></b>
		</button>
		<button type="button" class="btn_type1" ng-click="catalogProduct_grid.saveGridData(null, catalogProduct_grid.loadGridData)">
			<b><spring:message code="c.common.save" /></b>
		</button>
	</div>

	<div class="box_type1 marginT1" ng-if="gridShow2 == true">
		<h3 class="sub_title2">
			<spring:message code="dms.catalog.product.list" />
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_catalogProduct.data.length }}" /></span>
		</h3>

		<div class="tb_util tb_util_rePosition2">
			<button type="button" class="btn_tb_util tb_util1" ng-click="catalogProduct_grid.initGrid()">되돌리기</button>
		</div>

		<div class="gridbox gridbox200">
			<div class="grid" ui-grid="grid_catalogProduct"   
				ui-grid-move-columns 
				ui-grid-resize-columns 
				ui-grid-auto-resize 
				ui-grid-selection 
				ui-grid-row-edit
				ui-grid-cell-nav
				ui-grid-exporter
				ui-grid-edit 
				ui-grid-validate></div>				
		</div>
	</div>
	
	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /></b>
		</button>
	</div>
	
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>