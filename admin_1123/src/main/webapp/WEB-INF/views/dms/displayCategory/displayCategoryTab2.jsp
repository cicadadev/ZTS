<%--
	화면명 : 전시 관리 > 전시 카테고리 관리 > 전시 카테고리 코너정보 탭
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!-- ### 코너 목록 ### -->
<div class="box_type1 marginT3">
	<h3 class="sub_title2">
		<spring:message code="dms.displaycategory.corner.list" />
		<span><spring:message code="c.search.totalCount" arguments="{{ grid_displayCategoryCornerList.totalItems }}" /></span>
	</h3>

	<div class="gridbox gridbox200">
		<div class="grid" ui-grid="grid_displayCategoryCornerList"   
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
<!-- ### 상품 목록 ### -->
<div class="btn_alignR marginT3" ng-if="itemGridShow">
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
	<button ng-if="search.displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.TEXT'" type="button" class="btn_type1" ng-click="openItemRegPopup('text')">
		<b><spring:message code="dms.displayitem.text.register" /></b>
	</button>	
	<button type="button" class="btn_type1" ng-click="deleteGridData()">
		<b><spring:message code="c.common.delete" /></b>
	</button>
	<button type="button" class="btn_type1" ng-click="saveGridData()">
		<b><spring:message code="c.common.save" /></b>
	</button>			
</div>

<div class="box_type1 marginT1" ng-if="itemGridShow">
	<h3 class="sub_title2" ng-if="search.displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.PRODUCT'">
		<spring:message code="dms.displaycategory.corneritem.product.list" />
		<span><spring:message code="c.search.totalCount" arguments="{{ grid_cornerItemList.totalItems }}" /></span>
	</h3>
	<h3 class="sub_title2" ng-if="search.displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.HTML'">
		<spring:message code="dms.displaycategory.corneritem.html.list" />
		<span><spring:message code="c.search.totalCount" arguments="{{ grid_cornerItemList.totalItems }}" /></span>
	</h3>					
	<h3 class="sub_title2" ng-if="search.displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.EXHIBIT'">
		<spring:message code="dms.displaycategory.corneritem.exhibit.list" />
		<span><spring:message code="c.search.totalCount" arguments="{{ grid_cornerItemList.totalItems }}" /></span>
	</h3>					
	<h3 class="sub_title2" ng-if="search.displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.IMG'">
		<spring:message code="dms.displaycategory.corneritem.img.list" />
		<span><spring:message code="c.search.totalCount" arguments="{{ grid_cornerItemList.totalItems }}" /></span>
	</h3>
	<h3 class="sub_title2" ng-if="search.displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.TEXT'">
		<spring:message code="dms.displaycategory.corneritem.text.list" />
		<span><spring:message code="c.search.totalCount" arguments="{{ grid_cornerItemList.totalItems }}" /></span>
	</h3>	
	<div class="tb_util tb_util_rePosition">
			<button type="button" class="btn_tb_util tb_util1" ng-click="cornerItem_grid.initGrid()">되돌리기</button>
		<!-- <button type="button" class="btn_tb_util tb_util2" ng-click="exportCornerItemExcel()">엑셀받기</button> -->
	</div>

	<div class="gridbox gridbox200">
		<div class="grid" ui-grid="grid_cornerItemList"   
			ui-grid-move-columns
			ui-grid-row-edit
			data-ui-grid-pagination
			ui-grid-resize-columns 
			ui-grid-auto-resize
			ui-grid-cell-nav
			ui-grid-selection 
			ui-grid-exporter
			ui-grid-edit
			ui-grid-validate></div>
	</div>
</div>
