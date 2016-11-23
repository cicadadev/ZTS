<%--
	화면명 : 딜 관리 > 딜 상품 관리 팝업
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/sps.app.deal.list.js"></script>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="wrap_popup"  ng-app="dealApp" ng-controller="sps_dealProductManagerPopApp_controller as ctrl">
		<ul class="tab_type2">
			<li>
				<button type="button" ng-click="ctrl.moveTab('dealDetail')"><spring:message code="c.spsDeal.detail"/> <!-- 딜 상세 --></button>
			</li>
			<li>
				<button type="button" ng-click="ctrl.moveTab('divTitleManager')" fn-id="24_TAP"><spring:message code="c.spsDeal.divTitle.manage"/><!-- 구분타이틀 관리 --></button>
			</li>
			<li class="on">
				<button type="button" ng-click="ctrl.moveTab('dealProduct')"><spring:message code="c.spsDeal.product.manage"/><!-- 상품 관리 --></button>
			</li>
		</ul>
		
		<div class="box_type1 marginT2">
			<table class="tb_type1">
				<colgroup>
					<col width="15%" />
					<col width="40%" />
					<col width="15%" />
					<col width="10%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th><spring:message code="c.spsDealproduct.regDate"/> <!-- 상품 등록일 --></th>
						<td colspan="2">
							<input type="text" id="startDt" ng-model="productSearch.startDate" value="" placeholder="" datetime-picker date-only period-start/>
							~
							<input type="text" id="endDt" ng-model="productSearch.endDate" value="" placeholder="" datetime-picker date-only period-end/>
							<div class="day_group" start-ng-model="productSearch.startDate" end-ng-model="productSearch.endDate" date-only calendar-button  init-button="0"/>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.spsDealproduct.product"/><!-- 상품 --></th>
						<td colspan="2">
							<select data-ng-model="productSearch.productInfoType" data-ng-init="productSearch.productInfoType = 'PRODUCTID'">
								<option ng-repeat="info in productInfoType" value="{{info.val}}" >{{info.text}}</option>
							</select>

							<input type="text" data-ng-model="productSearch.productSearchKeyword" placeholder="" style="width:30%;" />
						</td>
					</tr>
				</tbody>
			</table>
		</div>

		<div class="btn_alignR">
			<button type="button" class="btn_type1" ng-click="ctrl.reset()">
				<b><spring:message code="c.search.btn.reset"/><!-- 초기화 --></b>
			</button>
			<button type="button" class="btn_type1 btn_type1_purple" ng-click="myGrid.loadGridData()">
				<b><spring:message code="c.search.btn.search"/><!-- 검색 --></b>
			</button>
		</div>
		<div class="btn_alignR marginT3">
			<button type="button" class="btn_type1" ng-click="ctrl.batchProductUpload()">
				<b><spring:message code="c.spsDealproduct.regBatchProduct"/><!-- 상품일괄등록 --></b>
			</button>
			<button type="button" class="btn_type1" ng-click="ctrl.searchProduct()">
				<b><spring:message code="c.spsDealproduct.regProduct"/><!-- 상품등록 --></b>
			</button>
			<button type="button" class="btn_type1" ng-click="myGrid.deleteGridData()">
				<b><spring:message code="c.common.delete"/><!-- 삭제 --></b>
			</button>					
			<button type="button" class="btn_type1" ng-click="ctrl.saveDealProduct()">
				<b><spring:message code="c.common.save"/><!-- 저장 --></b>
			</button>
		</div>
		
		<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.spsDealproduct.productList"/><!-- 상품목록 -->
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_product.data.length }}" /></span>
		</h3>

 		<div class="tb_util tb_util_rePosition">
			<button type="button" class="btn_tb_util tb_util1" ng-click="myGrid.initGrid()">되돌리기</button>
		</div>

		<div class="gridbox gridbox300">
			<div class="grid" data-ui-grid="grid_product"   
					data-ui-grid-move-columns
					data-ui-grid-row-edit
					data-ui-grid-resize-columns 
					data-ui-grid-pagination
					data-ui-grid-auto-resize
					data-ui-grid-cell-nav
					data-ui-grid-selection 
					data-ui-grid-exporter
					data-ui-grid-edit
					data-ui-grid-validate></div>
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>