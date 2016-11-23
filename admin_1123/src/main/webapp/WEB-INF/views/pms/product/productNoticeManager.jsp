<%--
	화면명 : 상품관리 > 상품품목정보관리
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.notice.manager.js"></script>

<article class="con_box" data-ng-app="noticeManagerApp" data-ng-controller="pms_noticeManagerApp_controller as ctrl">
	<h2 class="sub_title1"><spring:message code="c.pms.product.notice.manager"/><!-- 상품품목정보관리 --></h2>
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col class="col_142" />
				<col class="col_auto" />
				<col style="width:80px;" />				
				<col class="col_auto" />
			</colgroup>
			<tbody>
				<tr>
					<th><spring:message code="pmsProductnotice.productNoticeTypeCd"/><!-- 품목선택 --></th>
					<td>						
						<select data-ng-model="search.productNoticeTypeCd"  select-code="PRODUCT_NOTICE_TYPE_CD" required></select>						
					</td>
<%-- 					<th rowspan="5" class="alignC"><spring:message code="c.pms.product.id" /></th>
					<td rowspan="5">
						<textarea cols="30" rows="5" placeholder="" data-ng-model="search.productId" search-area> </textarea>
					</td> --%>					
				</tr>
				<tr>
					<th><spring:message code="pmsProduct.saleStateCd"/><!-- 판매상태 --></th>
					<td>
						<checkbox-list data-ng-model="search.saleState" code-group="SALE_STATE_CD" all-check></checkbox-list>
					</td>					
				</tr>
				<tr>
					<th>품목정보 확인 여부<!-- 확인여부 --></th>
					<td>
						<checkbox-list data-ng-model="search.noticeConfirm" custom="PMS_CONFIRM_YN" all-check></checkbox-list>
					</td>					
				</tr>				
				<tr>
					<th>담당MD ID/명<%-- <spring:message code="c.pmsCategory.userName"/> --%><!-- 담당MD --></th>
					<td>
						<input type="text" data-ng-model="search.userId" value="" placeholder="" style="width:22.5%;" />						
						<input type="text" data-ng-model="search.userName" value="" placeholder="" style="width:22.5%;" />
						<button type="button" class="btn_type2 " data-ng-click="ctrl.openPopup('md')">
							<b><spring:message code="c.search.btn.search" /></b>
						</button>					
						<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : search.userId == null || search.userId == ''}" ng-click="ctrl.eraser('user')"><spring:message code="c.search.btn.eraser"/></button>
					</td>
				</tr>
				<tr>
					<th>브랜드번호 / 명</th>
					<td >
						<input type="text" value="" ng-model="search.brandId" style="width:22.5%;" />
						<input type="text" value="" ng-model="search.brandName" style="width:22.5%;" />
						<button type="button" class="btn_type2" ng-click="ctrl.openPopup('brand')">
							<b><spring:message code="c.search.btn.search" /></b>
						</button>
						<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : search.brandId == null || search.brandId == ''}" ng-click="ctrl.eraser('brand')"><spring:message code="c.search.btn.eraser"/></button>
					</td>					
				</tr>
				<tr>
					<th><spring:message code="pmsProduct.name"/><!-- 상품명 --></th>
					<td>
						<input type="text" data-ng-model="search.productName" value="" placeholder="" style="width:22.5%;" />
					</td>		
				</tr>
				<tr>
					<th>상품번호</th>
					<td>
						<input type="text" data-ng-model="search.productId" value="" placeholder="" style="width:22.5%;" />
					</td>				
				</tr>
				<tr ng-if="!poBusinessId">
					<th>상품 ERP 코드</th>
					<td>
						<input type="text" value="" ng-model="search.erpProductId" style="width:22.5%;" />
					</td>				
				</tr>
				<tr ng-if="!poBusinessId">
					<th>업체 ID</th>
					<td>
						<input type="text" data-ng-model="search.businessId" value="" placeholder="" style="width:22.5%;" />
					</td>				
				</tr>
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" data-ng-click="ctrl.reset()" class="btn_type1">
			<b><spring:message code="c.search.btn.reset" /></b>
		</button>
		<button type="button" data-ng-click="searchNotice()" class="btn_type1 btn_type1_purple">
			<b><spring:message code="c.search.btn.search" /></b>
		</button>
	</div>	
	<div class="btn_alignR marginT3">
		<!-- <codebutton-list data-ng-click="ctrl.reject" code="altProc.saleState" code-group="SALE_STATE_CD" ></codebutton-list> -->
		<button type="button" class="btn_type1" ng-click="ctrl.bulk.upload.excel(5)">
			<b><spring:message code="c.pms.product.notice.reg.xls" /><!-- 품목정보 일괄등록 --></b>
		</button>
		<button ng-if="!poBusinessId" type="button" class="btn_type1" data-ng-click="ctrl.saveNotice()">
			<b>품목정보확인</b>
		</button>
		<button ng-if="!poBusinessId" type="button" class="btn_type1" ng-click="ctrl.openRejectLayer();">
			<b>반려</b>
		</button>
	</div>

	<!-- ### 상품 목록 ### -->
	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.pms.product.notice.list" /><!-- 품목정보목록 -->
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_notice.totalItems }}" /></span>			
		</h3>
		<div class="gridbox">
			<div class="grid" data-ui-grid="grid_notice"
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
			<button type="button" class="btn_type3 btn_type3_purple" data-ng-click="ctrl.reject()">
				<b><spring:message code="c.common.save"/><!-- 저장 --></b>
			</button>
		</div>

		<button type="button" class="btn_layer_close" data-ng-click="rejectLayer=false">레이어 닫기</button>
	</div>
</div>
</layerpopup>	
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>