<%--
	화면명 : 상품 관리 > 외부 비노출 관리 > 상품 목록
	작성자 : peter
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.epexcproduct.list.js"></script>

<article class="con_box" ng-app="epexcApp" ng-controller="pms_epexcProductListApp_controller as ctrl">
	<h2 class="sub_title1"><spring:message code="c.pms.epexcproduct.title" /></h2>

	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col class="col_142"/>
				<col class="col_auto"/>
				<col class="col_142"/>
				<col class="col_auto"/>
<%-- 				<col style="width:80px;"/> --%>
<%-- 				<col class="col_auto"/> --%>
			</colgroup>
			<tbody>
				<tr>
					<th><spring:message code="c.pms.epexcproduct.period" /></th><!-- 등록일 -->
					<td colspan="3">
						<input type="text" ng-model="search.startDate" placeholder="" datetime-picker period-start date-only />
						~
						<input type="text" ng-model="search.endDate" placeholder="" datetime-picker period-end date-only />
						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0" />
					</td>
<%-- 					<th rowspan="5" class="alignC"><spring:message code="c.pms.epexcproduct.productNo" /></th><!-- 상품번호 --> --%>
<!-- 					<td rowspan="5"> -->
<!-- 						<textarea cols="30" rows="5" placeholder="" ng-model="search.productId" search-area> </textarea> -->
<!-- 					</td> -->
				</tr>
				<tr>
					<th>브랜드번호 / 명<%-- <spring:message code="c.pms.epexcproduct.brand" /> --%></th><!-- 브랜드 -->
					<td>
						<input type="text" ng-model="search.brandId" style="width:22.5%;"/> <input type="text" ng-model="search.brandName" style="width:22.5%;"/> 
						<button type="button" class="btn_type2" ng-click="ctrl.openPopup('brand')"><b><spring:message code="c.search.btn.search"/></b></button>
						<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : search.brandId == null || search.brandId == ''}" ng-click="ctrl.eraser('brand')"><spring:message code="c.search.btn.eraser"/></button>
					</td>
					<th><spring:message code="c.pms.epexcproduct.divType" /></th><!-- 구분 -->
					<td>
						<select ng-model="search.excProductTypeCd" select-code="EXC_PRODUCT_TYPE_CD" style="width:150px;" >		
							<option value=""><spring:message code="c.select.all"/></option><!-- 전체 -->
						</select>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.pms.epexcproduct.saleState" /></th><!-- 판매상태 -->
					<td>
<%-- 						<input type="checkbox" ng-model="saleStateCdsAll" ng-click="ctrl.checkAll('saleStateCds')"/> <spring:message code="c.select.all"/> --%>
<!-- 						<label ng-repeat="saleState in saleStateCds"> -->
<!-- 							&nbsp;<input type="checkbox" checklist-model="tmp.saleStateCds" checklist-value="saleState.cd" ng-click="ctrl.checked(saleState.cd, 'saleStateCds')"/> {{saleState.name}} -->
<!-- 						</label> -->
						<checkbox-list ng-model="search.saleState" custom="PRODUCT_STATUS" all-check ></checkbox-list>
					</td>
					<th>공급업체 번호/명<%-- <spring:message code="c.pms.epexcproduct.businessInfo" /> --%></th><!-- 공급업체번호/명 -->
					<td >
						<input type="text" ng-model="search.businessId" style="width:30%;"  /> <input type="text" ng-model="search.businessName" style="width:30%;"  />
						<button type="button" class="btn_type2" ng-click="ctrl.openPopup('business')">
							<b><spring:message code="c.search.btn.search" /></b>
						</button>
						<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled': search.businessId == null || search.businessId == ''}" ng-click="ctrl.eraser('business')"><spring:message code="c.search.btn.eraser"/></button>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.pms.epexcproduct.productType" /></th><!-- 상품유형 -->
					<td>
<%-- 						<input type="checkbox" ng-model="productTypeCdsAll" ng-click="ctrl.checkAll('productTypeCds')"/> <spring:message code="c.select.all"/> --%>
<!-- 						<label ng-repeat="productType in productTypeCds"> -->
<!-- 							&nbsp;<input type="checkbox" checklist-model="tmp.productTypeCds" checklist-value="productType.cd" ng-click="ctrl.checked(productType.cd, 'productTypeCds')"/> {{productType.name}} -->
<!-- 						</label> -->
						<checkbox-list ng-model="search.productType" custom="PRODUCT_TYPE" all-check ></checkbox-list>
					</td>
					<th>상품 ERP 코드</th>
					<td>
						<input type="text" value="" ng-model="search.erpProductId" style="width:30%;" />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.pms.epexcproduct.divProduct" /></th><!-- 상품 -->
					<td colspan="3">
						<select data-ng-model="search.infoType" data-ng-init="search.infoType = 'ID'">
							<option ng-repeat="info in infoType" value="{{info.val}}" >{{info.text}}</option>
						</select>
						<input type="text" ng-model="search.searchKeyword" style="width:22.5%"/>
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" class="btn_type1" ng-click="ctrl.reset()" >
			<b><spring:message code="c.search.btn.reset" /></b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" ng-click="myGrid.loadGridData()" >
			<b><spring:message code="c.search.btn.search" /></b>
		</button>
	</div>

	<div class="btn_alignR marginT3">
		<button type="button" class="btn_type1" ng-click="ctrl.regSellerPopup()">
			<b><spring:message code="c.pms.epexcproduct.btn.regSeller" /></b>
		</button>
		<button type="button" class="btn_type1" ng-click="ctrl.regProductPopup()">
			<b><spring:message code="c.pms.epexcproduct.btn.regProduct" /></b>
		</button>
		<button type="button" class="btn_type1" ng-click="myGrid.deleteGridData()">
			<b><spring:message code="c.common.delete" /></b>
		</button>
<%-- 		<button type="button" class="btn_type1" ng-click="ctrl.saveEpexcproduct()">
			<b><spring:message code="c.common.save" /></b>
		</button> --%>
	</div>

	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.pms.epexcproduct.gridTitle" />
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_epexc.totalItems }}" /></span>
		</h3>
		
		<div class="tb_util tb_util_rePosition">
			<!-- <button type="button" class="btn_tb_util tb_util1" ng-click="myGrid.initGrid()">되돌리기</button> -->
			<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
			<!-- <button type="button" class="btn_tb_util tb_util5" ng-click="ctrl.addRow()">행추가</button> -->
		</div>

		<div class="gridbox" >
			<div class="grid" ui-grid="grid_epexc"
				ui-grid-validate 
				ui-grid-selection 
		 		ui-grid-exporter 
		 		ui-grid-pagination 
		 		ui-grid-edit 
		 		ui-grid-row-edit 
		 		ui-grid-cell-nav 
		 		ui-grid-auto-resize 
		 		ui-grid-resize-columns
			></div>
		</div>
	</div>

</article>

<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>