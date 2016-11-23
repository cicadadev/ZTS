<%--
	화면명 : 상품관리 > 상품 승인 관리
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.approval.manager.js"></script>

<article class="con_box" data-ng-app="approvalManagerApp" data-ng-controller="pms_approvalManagerApp_controller as ctrl">
	<h2 class="sub_title1">상품 승인 관리</h2>
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
					<th><spring:message code="c.pms.product.reg.dt"/><!-- 상품등록일 --></th>
					<td colspan="3">						
						<input type="text" data-ng-model="search.startDate" value="" placeholder="" datetime-picker date-only period-start/>											
						~
						<input type="text" data-ng-model="search.endDate" value="" placeholder="" datetime-picker date-only period-end/>			
																										
						
						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0"/>						
					</td>
					<th rowspan="7" class="alignC"><spring:message code="c.pms.product.id" /></th>
					<td rowspan="7">
						<textarea cols="30" rows="7" placeholder="" data-ng-model="search.productId" search-area> </textarea>
					</td>					
				</tr>
				<tr>
					<th><spring:message code="c.dmsDisplaycategory.category"/><!-- 전시카테고리 --></th>
					<td>
						<input type="text" data-ng-model="search.dispCategoryId" value="" placeholder="" style="width:30%;" />
						<input type="text" data-ng-model="search.dispCategoryName" value="" placeholder="" style="width:30%;" />
						<button type="button" class="btn_type2 " data-ng-click="ctrl.openCategoryPopup('dms/displayCategory')">
							<b><spring:message code="c.search.btn.search" /></b>
						</button>
						<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : search.dispCategoryId == null || search.dispCategoryId == ''}"  ng-click="ctrl.eraser('dispCategory')"><spring:message code="c.search.btn.eraser"/></button>
					</td>
					<th>브랜드번호 / 명<%-- <spring:message code="c.pmsBrand.brand"/> --%><!-- 브랜드 --></th>
					<td>
						<input type="text" data-ng-model="search.brandId" value="" placeholder="" style="width:30%;" />
						<input type="text" data-ng-model="search.brandName" value="" placeholder="" style="width:30%;" />						
						<button type="button" class="btn_type2 " data-ng-click="ctrl.brandPopup()">
							<b><spring:message code="c.search.btn.search" /></b>
						</button>
						<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : search.brandId == null || search.brandId == ''}"  ng-click="ctrl.eraser('brand')"><spring:message code="c.search.btn.eraser"/></button>
					</td>					
				</tr>
				<tr>
					<th><spring:message code="c.pmsCategory.category"/><!-- 표준카테고리 --></th>
					<td>
						<input type="text" data-ng-model="search.categoryId" value="" placeholder="" style="width:30%;" />
						<input type="text" data-ng-model="search.categoryName" value="" placeholder="" style="width:30%;" />
						<button type="button" class="btn_type2 " data-ng-click="ctrl.openCategoryPopup('pms/category')">
							<b><spring:message code="c.search.btn.search" /></b>
						</button>			
						<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : search.categoryId == null || search.categoryId == ''}"  ng-click="ctrl.eraser('category')"><spring:message code="c.search.btn.eraser"/></button>			
					</td>					
					<th>담당MD ID/명<%-- <spring:message code="c.pmsCategory.userName"/> --%><!-- 담당MD --></th>
					<td>
						<input type="text" data-ng-model="search.userId" value="" placeholder="" style="width:30%;" />
						<input type="text" data-ng-model="search.userName" value="" placeholder="" style="width:30%;" />						
						<button type="button" class="btn_type2 " data-ng-click="ctrl.userPopup()">
							<b><spring:message code="c.search.btn.search" /></b>
						</button>
						<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : search.userId == null || search.userId == ''}"  ng-click="ctrl.eraser('user')"><spring:message code="c.search.btn.eraser"/></button>					
					</td>
				</tr>
				<tr ng-if="!poBusinessId">
					<th><spring:message code="pmsProduct.productTypeCd"/><!-- 상품유형 --></th>
					<td >
						<checkbox-list ng-model="search.productType" custom="PRODUCT_TYPE" all-check ></checkbox-list>
					</td>
					<th>상품 ERP 코드</th>
					<td>
						<input type="text" value="" ng-model="search.erpProductId" style="width:30%;" />
					</td>
				</tr>
				<tr ng-if="!poBusinessId">
					<th><spring:message code="c.pms.product.gubun"/><!-- 상품구분 --></th>
					<td>
						<checkbox-list ng-model="search.productGubun" custom="PRODUCT_ATTR"></checkbox-list>
					</td>	
					<th>공급업체 번호/명<%-- <spring:message code="c.ccsBusiness.supply"/> --%><!-- 공급업체 --></th>
					<td>
						<input type="text" data-ng-model="search.businessId" value="" placeholder="" style="width:30%;" />								
						<input type="text" data-ng-model="search.businessName" value="" placeholder="" style="width:30%;" />
						<button type="button" class="btn_type2 " data-ng-click="ctrl.businessPopup()" >
							<b><spring:message code="c.search.btn.search" /></b>
						</button>		
						<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : search.businessId == null || search.businessId == ''}"  ng-click="ctrl.eraser('business')"><spring:message code="c.search.btn.eraser"/></button>		
					</td>						
				</tr>	
				<tr>
					<th><spring:message code="c.pms.product.approval.state"/><!-- 승인상태 --></th>
					<td>
						<checkbox-list ng-model="search.saleState" custom="PRODUCT_STATUS2" all-check ></checkbox-list>
					</td>
					<th><spring:message code="pmsProduct.name"/><!-- 상품명 --></th>
					<td>
						<input type="text" data-ng-model="search.productName" value="" placeholder="" style="width:80%;"/>
					</td>				
				</tr>											
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" data-ng-click="ctrl.reset()" class="btn_type1">
			<b><spring:message code="c.search.btn.reset" /></b>
		</button>
		<button type="button" data-ng-click="searchApproval()" class="btn_type1 btn_type1_purple">
			<b><spring:message code="c.search.btn.search" /></b>
		</button>
	</div>	
	<div class="btn_alignR marginT3">
		<!-- <codebutton-list data-ng-click="ctrl.approval" code="altProc.saleState" code-group="SALE_STATE_CD" ></codebutton-list> -->
		<button ng-if="!poBusinessId" type="button" class="btn_type1" ng-click="ctrl.changeState('mdApproval')">
			<b>MD승인</b>
		</button>		
		<button ng-if="!poBusinessId" type="button" class="btn_type1" ng-click="ctrl.openRejectLayer('mdReject');">
			<b>MD반려</b>
		</button>		
		<button ng-if="!poBusinessId" type="button" class="btn_type1" ng-click="ctrl.changeState('qcApproval')">
			<b>QC승인</b>
		</button>	
		<button ng-if="!poBusinessId" type="button" class="btn_type1" ng-click="ctrl.openRejectLayer('qcReject');">
			<b>QC반려</b>
		</button>
		<button ng-if="!poBusinessId" type="button" class="btn_type1" ng-click="ctrl.changeState('saleStart')">
			<b>판매개시</b>
		</button>				
	</div>	

	<!-- ### 상품 목록 ### -->
	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.pmsSaleproduct.saleproductList" /><!-- 단품 목록 -->
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_approval.totalItems }}" /></span>			
		</h3>
		<div class="gridbox">
			<div class="grid" data-ui-grid="grid_approval"
			data-ui-grid-move-columns 
			data-ui-grid-resize-columns 
			data-ui-grid-pagination
			data-ui-grid-auto-resize 
			data-ui-grid-exporter 
			data-ui-grid-selection
			data-ui-grid-edit	
			data-ui-grid-row-edit							
			data-ui-grid-validate></div>
		</div>
	</div>
	<!-- ### //상품 목록 ### -->
	
<!-- ### Layer ### -->
<layerpopup when="rejectLayer">
<div class="layer_box">
	<div class="inner">
		<h3 class="sub_title3"><spring:message code="c.pms.product.reject.reason" /><!-- 반려사유 --></h3>
		<table class="tb_type1">
			<colgroup>
				<col width="20%" />			
				<col width="*" />
			</colgroup>
			<tbody>
				<tr>
					<th><spring:message code="c.pms.product.reject.reason" /><!-- 반려사유 --></th>
					<td><input type="text" data-ng-model="rejectReason" value="" placeholder="" style="width:70%;" /></td>
				</tr>
			</tbody>
		</table>

		<div class="btn_alignC" style="margin-top:20px;">
			<button type="button" class="btn_type3 btn_type3_gray" data-ng-click="closeRejectLayer()">
				<b>닫기</b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" data-ng-click="ctrl.changeState(rejectState)">
				<b><spring:message code="c.common.save"/><!-- 저장 --></b>
			</button>
		</div>

		<button type="button" class="btn_layer_close" data-ng-click="rejectLayer=false">레이어 닫기</button>
	</div>
</div>
</layerpopup>
<!-- ### //Layer ### -->
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>