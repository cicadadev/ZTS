<%--
	화면명 : 공통 > 상품 검색 팝업
	작성자 : eddie
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<div class="wrap_popup" data-ng-app="ccsAppPopup"  data-ng-controller="pms_productSearch_controller as ctrl">
<form name="form">
	<h1 class="sub_title1">상품조회</h1>

	<div class="box_type1">
	<table class="tb_type1">
		<colgroup>
			<col width="11%" />
			<col width="64%" />
			<col width="9%" />
			<col width="*" />
		</colgroup>
		<tbody>
			<tr>
				<th>상품등록일</th>
				<td>
					<input type="text" id="startDt" ng-model="search.startDate" value="" placeholder="" datetime-picker date-only period-start/>
					~
					<input type="text" id="endDt" ng-model="search.endDate" value="" placeholder="" datetime-picker date-only period-end/>
					<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0"/>
				</td>				
				<%-- <th rowspan="4" class="alignC"><spring:message code="pms.product.list.productid" /></th>
				<td rowspan="4">
					<textarea cols="30" rows="5" ng-model="search.productId" placeholder="" style="height:106px;" search-area></textarea>
				</td> --%>
			</tr>
			<%-- <tr>
				<th>브랜드</th>
				<td>
					<input type="text"  style="width:150px" ng-model="search.brandId"/>
					<input style="width:150px" type="text" ng-model="search.brandName"/>
					<button type="button" class="btn_type2" ng-click="ctrl.brandSearch()"><b>검색</b></button>
					<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : search.brandId == null || search.brandId == ''}"  ng-click="ctrl.eraser('brandId', 'brandName')"><spring:message code="c.search.btn.eraser"/></button>
				</td>
			</tr> --%>
			<tr>
				<th><spring:message code="pmsProduct.saleStateCd" /></th>
				<td><checkbox-list ng-model="search.saleState" code-group="SALE_STATE_CD" all-check />
				</td>						
			</tr>
			<tr>
				<th>표준카테고리</th>
				<td>
					<input type="text" ng-model="search.categoryId" >
					<button type="button" class="btn_type2" ng-click="ctrl.openPopup('pms/category')">
						<b>검색</b>
					</button>
					<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : search.categoryId == null || search.categoryId == ''}" ng-click="ctrl.eraser('categoryId')"><spring:message code="c.search.btn.eraser"/></button>
				</td>	
			</tr>					
			<tr>
				<th>전시카테고리</th>
				<td>
					<input type="text" ng-model="search.dispCategoryId" >
					<button type="button" class="btn_type2" ng-click="ctrl.openPopup('dms/displayCategory')">
						<b>검색</b>
					</button>
					<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : search.dispCategoryId == null || search.dispCategoryId == ''}"  ng-click="ctrl.eraser('dispCategoryId')"><spring:message code="c.search.btn.eraser"/></button>
				</td>
				<!-- <th rowspan="4" class="alignC">단품ID</th>
				<td rowspan="4">
					<textarea cols="30" rows="5" ng-model="search.saleproductId" search-area style="height:50px;"></textarea>
				</td> -->				
			</tr>
			<tr ng-if="!poBusinessId">
				<th><spring:message code="c.pms.product.gubun"/><!-- 상품구분 --></th>
				<td >
					<checkbox-list ng-model="search.productGubun" custom="PRODUCT_ATTR"></checkbox-list>
				</td>
			</tr>
			<%-- <tr>
				<th><spring:message code="pmsProduct.productTypeCd" /></th>상품유형
				<td><checkbox-list ng-model="search.productType" code-group="PRODUCT_TYPE_CD" all-check />
				</td>						
			</tr> --%>
			<tr ng-if="!poBusinessId">
				<th>수수료율</th>
				<td>
					<checkbox-list ng-model="search.commissionRateYn" custom="COMMISSION_RATE" all-check ></checkbox-list>
					
				</td>
			</tr>
			<tr ng-if="!poBusinessId">
				<th>공급업체코드 / 명</th>
				<td>
					<input type="text" ng-model="search.businessId" style="width:20%;"/>
					<input type="text" ng-model="search.businessName" style="width:20%;"/>
					<button type="button" class="btn_type2" ng-click="ctrl.openPopup('business')">
						<b><spring:message code="c.search.btn.search"/><!-- 검색 --></b>
					</button>
					<button type="button" class="btn_eraser" ng-click="ctrl.eraser('businessId', 'businessName')">지우개</button>
				</td>
			</tr>
			<tr>
				<th>담당MD ID / 명</th>
				<td>
					<input type="text" ng-model="search.userId" style="width:20%;"/>
					<input type="text" ng-model="search.userName" style="width:20%;"/>
					<button type="button" class="btn_type2" ng-click="ctrl.openPopup('md')">
						<b><spring:message code="c.search.btn.search"/></b>
					</button>
					<button type="button" class="btn_eraser" ng-click="ctrl.eraser('userId', 'userName')">지우개</button>
				</td>
			</tr>
			<tr ng-if="!poBusinessId">
				<th>상품 ERP 코드</th>
				<td colspan="3">
					<input type="text" value="" ng-model="search.erpProductId" style="width:30%;" />
				</td>
			</tr>
			<tr>
				<th>상품가격(판매가)</th>
				<td colspan="3">
					<select data-ng-model="search.priceCompareType" data-ng-init="search.priceCompareType = 'DOWN'">
						<option ng-repeat="info in priceCompareType" value="{{info.val}}" >{{info.text}}</option>
					</select>

					<input type="text" data-ng-model="search.searchPriceCompareKeyword" placeholder="" style="width:20%;" />
				</td>
				<!-- <td><input type="text"  ng-model="search.name" style="width:50%;"/>
				</td> -->						
			</tr>
			
			<tr>
				<th>상품<%-- <spring:message code="pmsProduct.name" /> --%></th>
				<td colspan="3">
					<select data-ng-model="search.infoType" data-ng-init="search.infoType = 'ID'">
						<option ng-repeat="info in infoType" value="{{info.val}}" >{{info.text}}</option>
					</select>

					<input type="text" data-ng-model="search.searchKeyword" placeholder="" style="width:45%;" />
				</td>
				<!-- <td><input type="text"  ng-model="search.name" style="width:50%;"/>
				</td> -->						
			</tr>
		</tbody>
	</table>
	</div>

	<div class="btn_alignR">
		<button type="button" class="btn_type1 btn_type1" ng-click="ctrl.resetData()">
			<b>초기화</b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" ng-click="ctrl.getProductList()">
			<b>검색</b>
		</button>
	</div>

	<div class="box_type1 marginT3">
		<h3 class="sub_title2">
			상품 목록
			<span>(총 <b>{{productSearchGrid.totalItems}}</b>건)</span>
		</h3>
		<div class="gridbox gridbox300">
			<div class="grid" data-ui-grid="productSearchGrid"   
				data-ui-grid-move-columns
				data-ui-grid-row-edit
				data-ui-grid-resize-columns 
				data-ui-grid-pagination
				data-ui-grid-auto-resize
				data-ui-grid-cell-nav
				data-ui-grid-selection 
				data-ui-grid-exporter
				data-ui-grid-edit
				data-ui-grid-validate>
			</div>
		</div>

	</div>

	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" data-ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /></b>
		</button>
		<button type="button" class="btn_type3 btn_type3_purple" data-ng-click="ctrl.select()">
			<b><spring:message code="c.common.select" /></b>
		</button>
	</div>
	</form>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>
