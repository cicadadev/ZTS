<%--
	화면명 : 상품관리 > 사방넷 상품등록 > 상품 목록 검색
	작성자 : peter
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.sendgoods.list.js"></script>

<article class="con_box" ng-app="sendgoodsApp"  ng-controller="pms_sendGoodsListApp_controller as ctrl">
	<h2 class="sub_title1"><spring:message code="c.pms.sendgoods.title" /></h2>

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
			<tbody>
				<tr>
					<th><spring:message code="c.pms.sendgoods.regDate" /></th><!-- 상품등록일 -->
					<td colspan="3">
						<select data-ng-model="search.dateType" data-ng-init="search.dateType = 'REG'">
							<option ng-repeat="info in dateType" value="{{info.val}}" >{{info.text}}</option>
						</select>
						<input type="text" ng-model="search.startDate" placeholder="" datetime-picker date-only period-start/>
						~
						<input type="text" ng-model="search.endDate" placeholder="" datetime-picker date-only period-end/>
						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button="B" init-button="0" />
					</td>
					<th rowspan="5" class="alignL"><spring:message code="c.pms.sendgoods.erpCode" /></th><!-- 상품 ERP 코드 -->
					<td rowspan="5">
						<textarea cols="30" rows="5" placeholder="" ng-model="search.erpProductId" search-area ></textarea>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.pms.sendgoods.sendYn" /></th><!-- 전송여부 -->
					<td >
						<select ng-model="search.outSendYn">		
							<option value=""><spring:message code="c.select.all"/></option><!-- 전체 -->
							<option value="Y"><spring:message code="c.pms.sendgoods.sendYes" /></option><!-- 전송 -->
							<option value="N"><spring:message code="c.pms.sendgoods.sendNo" /></option><!-- 미전송 -->
						</select>
					</td>
					<th><spring:message code="c.pms.sendgoods.mdIdNm" /></th><!-- 담당MD ID/명 -->
					<td >
						<input type="text" ng-model="search.mdId" />
						<input type="text" ng-model="search.mdName" />
						<button type="button" class="btn_type2" ng-click="ctrl.openPopup('md')">
							<b><spring:message code="c.search.btn.search" /></b>
						</button>
						<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled': search.mdId == null || search.mdId == ''}" ng-click="ctrl.eraser('md')"><spring:message code="c.search.btn.eraser"/></button>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.pms.sendgoods.saleState" /></th><!-- 판매상태 -->
					<td >
<%-- 						<input type="checkbox" ng-model="saleStateCdsAll" ng-click="ctrl.checkAll('saleStateCds')"/> <spring:message code="c.select.all"/> --%>
<!-- 						<label ng-repeat="saleState in saleStateCds"> -->
<!-- 							&nbsp;<input type="checkbox" checklist-model="tmp.saleStateCds" checklist-value="saleState.cd" ng-click="ctrl.checked(saleState.cd, 'saleStateCds')"/> {{saleState.name}} -->
<!-- 						</label> -->
						<checkbox-list ng-model="search.saleState" custom="PRODUCT_STATUS" all-check ></checkbox-list>
					</td>
					<th><spring:message code="c.pms.sendgoods.brandIdNm" /></th><!-- 브랜드번호/명 -->
					<td >
						<input type="text" ng-model="search.brandId" />
						<input type="text" ng-model="search.brandName" />
						<button type="button" class="btn_type2" ng-click="ctrl.openPopup('brand')">
							<b><spring:message code="c.search.btn.search" /></b>
						</button>
						<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled': search.brandId == null || search.brandId == ''}" ng-click="ctrl.eraser('brand')"><spring:message code="c.search.btn.eraser"/></button>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.pms.sendgoods.productType" /></th><!-- 상품유형 -->
					<td >
<%-- 						<input type="checkbox" ng-model="productTypeCdsAll" ng-click="ctrl.checkAll('productTypeCds')"/> <spring:message code="c.select.all"/> --%>
<!-- 						<label ng-repeat="productType in productTypeCds"> -->
<!-- 							&nbsp;<input type="checkbox" checklist-model="tmp.productTypeCds" checklist-value="productType.cd" ng-click="ctrl.checked(productType.cd, 'productTypeCds')"/> {{productType.name}} -->
<!-- 						</label> -->
						<checkbox-list ng-model="search.productType" custom="PRODUCT_TYPE" all-check ></checkbox-list>
					</td>
					<th>공급업체 번호/명<%-- <spring:message code="c.pms.sendgoods.bizIdNm" /> --%></th><!-- 업체번호/명 -->
					<td >
						<input type="text" ng-model="search.businessId" />
						<input type="text" ng-model="search.businessName" />
						<button type="button" class="btn_type2" ng-click="ctrl.openPopup('business')">
							<b><spring:message code="c.search.btn.search" /></b>
						</button>
						<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled': search.businessId == null || search.businessId == ''}" ng-click="ctrl.eraser('business')"><spring:message code="c.search.btn.eraser"/></button>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.pms.epexcproduct.divProduct" /></th><!-- 상품 -->
					<td >
						<select data-ng-model="search.infoType" data-ng-init="search.infoType = 'ID'">
							<option ng-repeat="info in infoType" value="{{info.val}}" >{{info.text}}</option>
						</select>
						<input type="text" ng-model="search.searchKeyword" />
					</td>
					<th><spring:message code="pmsProduct.maker" /></th><!-- 제조사 -->
					<td>
						<input type="text" ng-model="search.makerName" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" class="btn_type1" ng-click="ctrl.reset()">
			<b><spring:message code="c.search.btn.reset" /></b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" ng-click="myGrid.loadGridData()">
			<b><spring:message code="c.search.btn.search" /></b>
		</button>
	</div>

	<div class="btn_alignR marginT3">
		<button type="button" class="btn_type1" ng-click="ctrl.sendSabangnet()">
			<b><spring:message code="c.pms.sendgoods.sendProduct" /></b>
		</button>
	</div>

	<!-- ### 상품 목록 ### -->
	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.pms.sendgoods.gridTitle" />
			<!-- <span>(총 <b>{{ grid_sendgoods.totalItems }}</b>건)</span> -->
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_sendgoods.totalItems }}" /></span>
		</h3>

 		<div class="tb_util tb_util_rePosition">
			<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
		</div>

		<div class="gridbox gridbox500">
			<div class="grid" ui-grid="grid_sendgoods"   
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
	<!-- ### //상품 목록 ### -->
</article>

<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>