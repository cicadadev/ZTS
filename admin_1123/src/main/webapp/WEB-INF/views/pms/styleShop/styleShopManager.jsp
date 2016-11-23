<%--
	화면명 : 스타일샵 상품 이미지 관리
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.styleShop.manager.js"></script>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>

<article class="con_box"  ng-app="styleShopApp" data-ng-controller="pms_styleShopManagerApp_controller as ctrl">
	<h2 class="sub_title1">스타일샵 상품 이미지 관리</h2>
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col width="9%" />
				<col width="64%" />
				<col width="9%" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr>
					<th>등록일</th>
					<td>
						<input type="text" data-ng-model="search.startDate" value="" placeholder="" datetime-picker date-only period-start/>											
						~
						<input type="text" data-ng-model="search.endDate" value="" placeholder="" datetime-picker date-only period-end/>

						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button  init-button="0"/>
					</td>
				</tr>
				<tr>
					<th>사용여부</th>
					<td>
						 <checkbox-list ng-model="search.useYn" custom="USE_YN" all-check ></checkbox-list>
					</td>						
				</tr>
				<tr>
					<th>스타일분류코드</th>
					<td>
						<select ng-model="search.styleProductItemCd" select-code="STYLE_PRODUCT_ITEM_CD" style="width:150px;">
							<option value="">선택하세요</option>
						</select>
					</td>
				</tr>
				<tr>
					<th>브랜드번호/명</th>
					<td >
						<input type="text" value="" ng-model="search.brandId" style="width:15%;" />
						<input type="text" value="" ng-model="search.brandName" style="width:15%;" />
						<button type="button" class="btn_type2" ng-click="ctrl.brandSearch()">
							<b><spring:message code="c.search.btn.search" /></b>
						</button>
						<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : search.brandId == null || search.brandId == ''}" ng-click="ctrl.eraser()"><spring:message code="c.search.btn.eraser"/></button>
					</td>
				</tr>
				
				<tr>
					<th><spring:message code="c.dmsExhibit.product" /><!-- 상품 --></th>
					<td>
						<select data-ng-model="search.productInfoType" data-ng-init="search.productInfoType = 'NAME'">
							<option ng-repeat="info in productInfoType" value="{{info.val}}" >{{info.text}}</option>
						</select>

						<input type="text" data-ng-model="search.productSearchKeyword" placeholder="" style="width:30%;" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" class="btn_type1" ng-click="ctrl.reset()">
			<b><spring:message code="c.search.btn.reset" /><!-- 초기화 --></b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" ng-click="myGrid.loadGridData()">
			<b><spring:message code="c.search.btn.search" /><!-- 검색 --></b>
		</button>
	</div>
	
	<div class="btn_alignR marginT3">
		<button type="button" class="btn_type1" ng-click="ctrl.addProductPopup()">
			<b>상품등록</b>
		</button>
		<button type="button" class="btn_type1" ng-click="myGrid.deleteGridData()">
			<b><spring:message code="c.common.delete" /><!-- 삭제 --></b>
		</button>					
	</div>
	

	<!-- ### 기획전 목록 ### -->
	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			상품목록
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_styleShop.totalItems }}" /></span>
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
		
		<div class="tb_util tb_util_rePosition">
<!-- 			<button type="button" class="btn_tb_util tb_util1" ng-click="myGrid.initGrid()">되돌리기</button> -->
			<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
		</div>

		<div class="gridbox">
			<div class="grid" data-ui-grid="grid_styleShop" 
					data-ui-grid-move-columns 
					data-ui-grid-resize-columns 
					data-ui-grid-pagination
					data-ui-grid-auto-resize 
					data-ui-grid-selection 
					data-ui-grid-row-edit
					data-ui-grid-cell-nav
					data-ui-grid-exporter
					data-ui-grid-edit 
					data-ui-grid-validate></div>
		</div>
	</div>
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>