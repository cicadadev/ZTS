<%--
	화면명 : 공통 > ERP 상품 검색 팝업
	작성자 : eddie
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>

<div class="wrap_popup" ng-app="ccsAppPopup" ng-controller="pms_erpSaleProductSearch_controller as ctrl">
	<h1 class="sub_title1">ERP 상품 검색</h1>

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
				<th class="alignC">ERP상품번호</th>
				<td><input type="text"  ng-model="search.itemid" style="width:50%;"/></td>		
			</tr>
			<tr>
				<th class="alignC">ERP상품명</th>
				<td><input type="text"  ng-model="search.itemname" style="width:50%;"/></td>						
			</tr>	
		</tbody>
	</table>
	</div>
	
		<div class="btn_alignR">
		<button type="button" class="btn_type1" ng-click="ctrl.reset()">
			<b>초기화</b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" ng-click="ctrl.search()">
			<b>검색</b>
		</button>
	</div>

	<div class="box_type1 marginT3">
		<h3 class="sub_title2">
			상품 목록
			<span>(총 <b>{{myGrid.totalItems}}</b>건)</span>
		</h3>
		<div class="gridbox gridbox300">
			<div class="grid" data-ui-grid="myGrid"   
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

	<div class="btn_alignC">
		<button type="button" class="btn_type3 btn_type3_gray" data-ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /></b>
		</button>
		<button type="button" class="btn_type3 btn_type3_purple" data-ng-click="ctrl.select()">
			<b><spring:message code="c.common.save" /></b>
		</button>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>