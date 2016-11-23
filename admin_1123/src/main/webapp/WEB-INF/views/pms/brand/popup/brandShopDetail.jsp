<%--
	화면명 : 상품 관리 > 브랜드 관리 > 브랜드 상세 팝업 > 브랜드샵 정보 탭
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.brand.detail.js"></script>

<div class="wrap_popup" ng-app="brandDetailApp" ng-controller="pms_brandShopInfoController as ctrl">
	<h2 class="sub_title1"><spring:message code="c.pmsBrand.detail" /></h2>	
	
	<ul class="tab_type2">
		<li oncllick="changeTab('brand')">
			<button type="button" ng-click="ctrl.changeTab('brand')"><spring:message code="c.pmsBrand.basicInfo" /></button>
		</li>
		<li class="on">
			<button type="button"><spring:message code="c.pmsBrand.shopInfo" /></button>
		</li>
	</ul>
	
	<span>
		<div class="box_type1 marginT3">
			<h3 class="sub_title2">
				<spring:message code="dms.displaycategory.corner.list"/>
				<span><spring:message code="c.search.totalCount" arguments="{{ grid_corner.totalItems }}" /></span>
			</h3>
			<div  class="gridbox gridbox200">
   				<div class="grid" ui-grid="grid_corner"   
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
	</span>
	
	<span ng-if="cornerGrid == true">
		<div class="btn_alignR marginT3">
			<button ng-if="search.displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.PRODUCT'" type="button" class="btn_type1" ng-click="openItemRegPopup('product')">
				<b><spring:message code="dms.catalog.product.btn.register" /></b>
			</button>
			<button ng-if="search.displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.HTML'" type="button" class="btn_type1" ng-click="openItemRegPopup('html')">
				<b><spring:message code="dms.displayitem.html.register" /></b>
			</button>
			<button ng-if="search.displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.EXHIBIT'" type="button" class="btn_type1" ng-click="openItemRegPopup('exhibit')">
				<b><spring:message code="c.dmsExhibit.regExhibit" /></b>
			</button>						
			<button ng-if="search.displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.IMG'" type="button" class="btn_type1" ng-click="openItemRegPopup('img')">
				<b><spring:message code="dms.displayitem.imgbanner.register" /></b>
			</button>
			<button type="button" class="btn_type1" ng-click="cornerItem_grid.deleteGridData()">
				<b><spring:message code="c.common.delete" /></b>
			</button>
			<button type="button" class="btn_type1" ng-click="cornerItem_grid.saveGridData()">
				<b><spring:message code="c.common.save" /></b>
			</button>
		</div>

		<div class="box_type1 marginT1">
			<h3 class="sub_title2">
				아이템 목록
				<span><spring:message code="c.search.totalCount" arguments="{{ grid_cornerItem.totalItems }}" /></span>
			</h3>

			<div class="tb_util tb_util_rePosition">
				<button type="button" class="btn_tb_util tb_util1" ng-click="cornerItem_grid.initGrid()">되돌리기</button>
			</div>

			<div class="gridbox gridbox200">
   				<div class="grid" ui-grid="grid_cornerItemList"   
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
	</span>
	
<%-- 	<span ng-if="cornerGrid == true">
		<div class="btn_alignR marginT3">						
			<button type="button" class="btn_type1" ng-click="openItemRegPopup('img')">
				<b><spring:message code="dms.displayitem.imgbanner.register" /></b>
			</button>
			<button type="button" class="btn_type1" ng-click="banner_grid.deleteGridData()">
				<b><spring:message code="c.common.delete" /></b>
			</button>
			<button type="button" class="btn_type1" ng-click="banner_grid.saveGridData()">
				<b><spring:message code="c.common.save" /></b>
			</button>
		</div>

		<div class="box_type1 marginT1">
			<h3 class="sub_title2">
				<spring:message code="pms.brand.banner.list.title" />
				<span><spring:message code="c.search.totalCount" arguments="{{ grid_banner.totalItems }}" /></span>
			</h3>

			<div class="tb_util tb_util_rePosition">
				<button type="button" class="btn_tb_util tb_util1" ng-click="banner_grid.initGrid()">되돌리기</button>
				<button type="button" class="btn_tb_util tb_util2" ng-click="banner_grid.exportExcel()">엑셀받기</button>
			</div>

			<div  class="gridbox gridbox200">
   				<div class="grid" ui-grid="grid_banner"   
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
	</span> --%>

</div>