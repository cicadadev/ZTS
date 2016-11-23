<%--
	화면명 : 상품관리 > 상품관리
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.product.list.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<article class="con_box" ng-app="productListApp"  ng-controller="pms_productListApp_controller as ctrl">
	<h2 class="sub_title1"><spring:message code="c.pms.product.manager" /><!-- 상품관리 --></h2>
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col class="col_142"/>
				<col class="col_auto"/>
				<col class="col_142"/>
				<col class="col_auto"/>
				<col style="width:80px;"/>
				<col class="col_auto"/>
			</colgroup>
			<tbody  ng-if="poBusinessId" >
			<jsp:include page="/WEB-INF/views/pms/product/inner/searchPo.jsp" flush="true"/>
			</tbody>
			<tbody ng-if="!poBusinessId" >
			<jsp:include page="/WEB-INF/views/pms/product/inner/searchBo.jsp" flush="true"/>
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" class="btn_type1" ng-click="ctrl.resetData()">
			<b><spring:message code="c.search.btn.reset" /></b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" ng-click="ctrl.getProductList()">
			<b><spring:message code="c.search.btn.search" /></b>
		</button>
<!-- 		<button type="button" class="btn_type1 btn_type1_purple" ng-click="saveGrid()"> -->
<!-- 			<b>그리드 저장</b> -->
<!-- 		</button> -->
		
	</div>
	<div class="btn_alignR marginT3">
		<button type="button" class="btn_type1" ng-click="ctrl.bulk.upload.excel(1)" fn-id="3_BUTTON1">
			<b><spring:message code="c.pms.product.price.info.reserve.chg" /><!-- 가격정보 예약변경 --></b>
		</button>
		<button type="button" class="btn_type1" ng-click="ctrl.bulk.upload.excel(2)" fn-id="3_BUTTON2">
			<b><spring:message code="c.pms.product.info.reserve.change" /><!-- 상품정보 예약변경 --></b>
		</button>
<%-- 		<button type="button" class="btn_type1" ng-click="ctrl.bulk.upload.excel(3)">
			<b><spring:message code="c.pms.product.all.reg" /><!-- 상품 일괄등록 --></b>
		</button> --%>
		<button type="button" class="btn_type1" ng-click="ctrl.bulk.upload.excel(4)" fn-id="3_BUTTON3">
			<b><spring:message code="c.pms.product.all.chg" /><!-- 상품 일괄변경 --></b>
		</button>
		<button type="button" class="btn_type1" ng-click="openNewProductPopup(false);" fn-id="3_INSERT1">
			<b><spring:message code="c.pms.product.normal.reg" /><!-- 일반상품등록 --></b>
		</button>
		<button type="button" class="btn_type1" ng-if="!poBusinessId" ng-click="openNewProductPopup(true);" fn-id="3_INSERT2">
			<b><spring:message code="c.pms.product.set.reg" /><!-- 세트상품등록 --></b>
		</button>		
	</div>

	<!-- ### 주문 목록 ### -->
	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.pms.product.list" /> <!-- 상품 목록 -->
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_product.totalItems}}" /></span>
		</h3>

 		<div class="tb_util tb_util_rePosition">
			<button type="button" class="btn_tb_util tb_util1" ng-click="myGrid.initGrid()">되돌리기</button>
			<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()" fn-id="3_EXCEL">엑셀받기</button>
		</div>

		<div class="gridbox">
			<div class="grid" data-ui-grid="grid_product"   
					data-ui-grid-resize-columns 
					data-ui-grid-auto-resize
					data-ui-grid-pagination
					data-ui-grid-cell-nav
					data-ui-grid-selection 
					data-ui-grid-exporter
					data-ui-grid-edit
					data-ui-grid-validate></div>
		</div>
	</div>
	<!-- ### //주문 목록 ### -->
</article>

<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>
