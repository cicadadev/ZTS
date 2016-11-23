<%--
	화면명 : 기획전 관리 > 구분타이틀 정보 팝업
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/dms.app.exhibit.manager.js"></script>

<div class="wrap_popup"  ng-app="exhibitApp" data-ng-controller="dms_exhibitDivTitleInfoPopApp_controller as ctrl">
		<h1 class="sub_title1"><spring:message code="c.dmsExhibit.divTitleInfo"/><!-- 구분타이틀정보 --></h1>
		<ul class="tab_type2">
			<li class="">
				<button type="button" ng-click="ctrl.moveTab($event, 'detail')" name="detail"><spring:message code="c.dmsExhibit.basicInfo"/><!-- 기획전 기본정보 --></button>
			</li>
			<li class="on">
				<button type="button" ng-click="ctrl.moveTab($event, 'title')" name="title"><spring:message code="c.dmsExhibit.divTitleInfo"/><!-- 구분타이틀정보 --></button>
			</li>
		</ul>
	
		<div id="divTitle" ng-show="divTitle=true">
			<div class="btn_alignR marginT3">
				<button type="button" class="btn_type1" data-ng-click="ctrl.insertDivTitle()">
					<b><spring:message code="c.dmsExhibit.regDivTitle"/><!-- 구분 타이틀 등록 --></b>
				</button>
				<button type="button" class="btn_type1" data-ng-click="ctrl.deleteDivTitle()">
					<b><spring:message code="c.common.delete" /><!-- 삭제 --></b>
				</button>					
				<button type="button" class="btn_type1" data-ng-click="myGrid1.saveGridData()">
					<b><spring:message code="c.common.save" /><!-- 저장 --></b>
				</button>
			</div>
		
			<div class="box_type1 marginT1">
				<h3 class="sub_title2">
					<spring:message code="c.dmsExhibit.divTitle.list" /><!-- 구분타이틀 목록 -->
<!-- 					<button type="button"  ng-click="changeMode(grid_divTitle.enableRowSelection)" ng-class="{'btn_type5' : grid_divTitle.enableRowSelection, 'btn_type6' : !grid_divTitle.enableRowSelection}"> -->
<!-- 						<span ng-show="grid_divTitle.enableRowSelection"> -->
<%-- 							<b><spring:message code="c.grid.change.modifyMode"/><!-- 편집모드전환 --></b> --%>
<!-- 						</span> -->
<!-- 						<span ng-show="!grid_divTitle.enableRowSelection"> -->
<%-- 							<b><spring:message code="c.grid.change.selectionMode"/><!-- 선택모드전환 --></b> --%>
<!-- 						</span> -->
<!-- 					</button> -->
					<span><spring:message code="c.search.totalCount" arguments="{{ grid_divTitle.data.length }}" /></span>
				</h3>
		
				<div class="tb_util">
		
					<select style="width:105px;">
						<option value="">200건</option>
					</select>
		
					<span class="page">
						<button type="button" class="btn_prev">이전</button>
						<input type="text" value="1" placeholder="" />
						<u>/</u><i>24</i>
						<button type="button" class="btn_next">다음</button>
					</span>
		
					<button type="button" class="btn_type2">
						<b>이동</b>
					</button>
				</div>
				
				<div class="tb_util tb_util_rePosition2">
					<button type="button" class="btn_tb_util tb_util1" ng-click="myGrid1.initGrid()">되돌리기</button>
				</div>
		
				<div class="gridbox gridbox200">
					<div class="grid" data-ui-grid="grid_divTitle" 
								data-ui-grid-move-columns 
								data-ui-grid-resize-columns 
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
		
		<div id="product" ng-show="product==true" style="margin-top:49px;"><!-- class="division_set2" -->
			<section>
				<div class="btn_alignR marginT3">
					<button type="button" class="btn_type1" data-ng-click="ctrl.batchProduct()">
						<b><spring:message code="c.dmsExhibit.divTitle.regBatchProduct" /><!-- 상품일괄 등록 --></b>
					</button>
					<button type="button" class="btn_type1" data-ng-click="ctrl.searchProduct()">
						<b><spring:message code="c.dmsExhibit.divTitle.regProduct" /><!-- 상품등록 --></b>
					</button>
					<button type="button" class="btn_type1" data-ng-click="myGrid2.deleteGridData()">
						<b><spring:message code="c.common.delete" /><!-- 삭제 --></b>
					</button>					
					<button type="button" class="btn_type1" data-ng-click="myGrid2.saveGridData(null, myGrid2.loadGridData)">
						<b><spring:message code="c.common.save" /><!-- 저장 --></b>
					</button>
				</div>
				
				<div class="box_type1 marginT1">
					<h3 class="sub_title2">
						<spring:message code="c.dmsExhibit.divTitle"/><!-- 구분 타이틀 --> <spring:message code="c.dmsExhibit.divTitle.productList" /><!-- 상품 목록 -->
						<span><spring:message code="c.search.totalCount" arguments="{{ grid_product.data.length }}" /></span>
					</h3>
		
					<div class="tb_util">
			
						<select style="width:105px;">
							<option value="">200건</option>
						</select>
			
						<span class="page">
							<button type="button" class="btn_prev">이전</button>
							<input type="text" value="1" placeholder="" />
							<u>/</u><i>24</i>
							<button type="button" class="btn_next">다음</button>
						</span>
			
						<button type="button" class="btn_type2">
							<b>이동</b>
						</button>
					</div>
				
					<div class="tb_util tb_util_rePosition2">
						<button type="button" class="btn_tb_util tb_util1" ng-click="myGrid2.initGrid()">되돌리기</button>
						<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid2.exportExcel()">엑셀받기</button>
					</div>
		
					<div class="gridbox">
						<div class="grid" data-ui-grid="grid_product" 
								data-ui-grid-move-columns 
								data-ui-grid-resize-columns 
								data-ui-grid-auto-resize 
								data-ui-grid-selection 
								data-ui-grid-row-edit
								data-ui-grid-cell-nav
								data-ui-grid-exporter
								data-ui-grid-edit 
								data-ui-grid-validate></div>
					</div>
				</div>	
			</section>
		</div>
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b><spring:message code="c.common.close" /><!-- 닫기 --></b>
			</button>
		</div>
</div>

<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>