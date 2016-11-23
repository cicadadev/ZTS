<%--
	화면명 : 공통 > 브랜드 검색 팝업
	작성자 : eddie
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="wrap_popup" data-ng-app=ccsAppPopup data-ng-controller="pms_brandListApp_controller as ctrl">
	<h1 class="sub_title1"><spring:message code="c.pmsBrand.brandSearch"/><!-- 브랜드 검색 --></h1>

	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col width="140" />
				<col width="50%" />
				<col width="140" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr>
					<th>브랜드번호<!-- <spring:message code="c.pmsBrand.brandInfo"/> --><!-- 브랜드정보 --></th>
					<td colspan="3">
						<!-- <select data-ng-model="search.type" ng-init="brandType = [{val : 'NAME', name : '브랜드명'},{val : 'ID', name : '브랜드ID'}];search.type='NAME'">		
							<option value="{{info.val}}" ng-repeat="info in brandType">{{info.name}}</option>
						</select> -->

						<input type="text" data-ng-model="search.searchBrandId" placeholder="" style="width:50%;" />
					</td>
					<%-- <th>브랜드상태</th>
					<td>						
						<select data-ng-model="search.displayYn">		
							<option value=""><spring:message code="c.select.all"/></option>					
							<option value="Y"><spring:message code="c.input.radio.displayY"/></option>
							<option value="N"><spring:message code="c.input.radio.displayN"/></option>							
						</select>						
					</td> --%>
				</tr>
				<tr>
					<th>브랜드명</th>
					<td colspan="3">
						<input type="text" data-ng-model="search.searchName" placeholder="" style="width:50%;" />
					</td>
				</tr>				
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" class="btn_type1" data-ng-click="ctrl.reset()">
			<b><spring:message code="c.search.btn.reset"/><!-- 초기화 --></b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" data-ng-click="ctrl.searchBrand()">
			<b><spring:message code="c.search.btn.search"/><!-- 검색 --></b>
		</button>
	</div>

	<div class="box_type1 marginT3">
		<h3 class="sub_title2">
			<spring:message code="c.pmsBrand.brandList" />
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_brand.totalItems }}" /></span>
		</h3>
		
		<div class="gridbox">
			<div class="grid gridbox300" data-ui-grid="grid_brand"
			data-ui-grid-move-columns data-ui-grid-resize-columns data-ui-grid-pagination
			data-ui-grid-auto-resize data-ui-grid-exporter data-ui-grid-selection
			></div>
		</div>

	</div>

	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" data-ng-click="ctrl.close()">
			<b><spring:message code="c.common.close"/><!-- 취소 --></b>
		</button>
		<button type="button" class="btn_type3 btn_type3_purple" data-ng-click="ctrl.selectBrand()">
			<b><spring:message code="c.common.select"/><!-- 선택 --></b>
		</button>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>
		