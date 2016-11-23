<%--
	화면명 : 상품관리 > 상품목록 > PO용 검색조건 영역
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>	
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<tr>
	<th><spring:message code="c.pms.product.reg.dt"/><!-- 상품등록일 --></th>
	<td colspan="3">
		<input type="text" id="startDt" ng-model="search.startDate" value="" placeholder="" datetime-picker date-only period-start/>
		~
		<input type="text" id="endDt" ng-model="search.endDate" value="" placeholder="" datetime-picker date-only period-end/>
		<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0"/>
	</td>
	<th rowspan="2" class="alignL"><spring:message code="pms.product.list.productid" /></th>
	<td rowspan="2">
		<textarea cols="30" rows="4" placeholder="" ng-model="search.productId" search-area ></textarea>
	</td>
</tr>
<tr>
	<th>브랜드번호 / 명</th>
	<td >
		<input type="text" value="" ng-model="search.brandId" style="width:30%;" />
		<input type="text" value="" ng-model="search.brandName" style="width:30%;" />
		<button type="button" class="btn_type2" ng-click="ctrl.openPopup('brand')">
			<b><spring:message code="c.search.btn.search" /></b>
		</button>
		<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : search.brandId == null || search.brandId == ''}" ng-click="ctrl.eraser('brandId', 'brandName')"><spring:message code="c.search.btn.eraser"/></button>
	</td>
	<th><spring:message code="c.dmsDisplaycategory.category"/><!-- 전시카테고리 --></th>
	<td>
		<input type="text" ng-model="search.dispCategoryName">
		<button type="button" class="btn_type2" ng-click="ctrl.openPopup('dms/displayCategory')">
			<b><spring:message code="c.search.btn.search" /></b>
		</button>
		<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : search.dispCategoryId == null || search.dispCategoryId == ''}" ng-click="ctrl.eraser('dispCategoryId', 'dispCategoryName')"><spring:message code="c.search.btn.eraser"/></button>
	</td>

</tr>
<tr>
	<th>판매상태</th>
	<td>
		<checkbox-list ng-model="search.saleState" custom="PRODUCT_STATUS" all-check ></checkbox-list>
	</td>
	<th><spring:message code="c.pmsCategory.category"/><!-- 표준카테고리 --></th>
	<td>
		<input type="text" ng-model="search.categoryName">
		<button type="button" class="btn_type2" ng-click="ctrl.openPopup('pms/category')">
			<b><spring:message code="c.search.btn.search" /></b>
		</button>
		<button type="button" class="btn_eraser"  ng-class="{'btn_eraser_disabled' : search.categoryId == null || search.categoryId == ''}" ng-click="ctrl.eraser('categoryId', 'categoryName')"><spring:message code="c.search.btn.eraser"/></button>
	</td>
	<th rowspan="2" class="alignL"><spring:message code="c.pms.sendgoods.saleproductNo" /><%--단품ID --%></th>
	<td rowspan="2">
		<textarea cols="30" rows="4" placeholder="" ng-model="search.saleproductId" search-area style="height:auto;"></textarea>
	</td>
</tr>
<tr>
	<th>담당MD ID/명</th>
	<td >
		<input type="text" value="" ng-model="search.userId" style="width:30%;" />
		<input type="text" value="" ng-model="search.userName" style="width:30%;" />
		<button type="button" class="btn_type2" ng-click="ctrl.openPopup('md')">
			<b><spring:message code="c.search.btn.search" /></b>
		</button>
		<button type="button" class="btn_eraser"  ng-class="{'btn_eraser_disabled' : search.userId == null || search.userId == ''}" ng-click="ctrl.eraser('userId', 'userName')"><spring:message code="c.search.btn.eraser"/></button>
	</td>
	<th><spring:message code="pmsProduct.name"/><!-- 상품명 --></th>
	<td>
		<input type="text" ng-model="search.name" style="width:80%;"/>
	</td>	
</tr>
<tr>

</tr>