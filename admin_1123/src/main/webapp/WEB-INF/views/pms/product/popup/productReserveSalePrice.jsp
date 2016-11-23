<%--
	화면명 : 상품 관리 > 상품 관리 > 상품 상세 팝업 > 단품 가격 예약 팝업
	작성자 : eddie
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.product.detail.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<div class="wrap_popup" data-ng-app="productDetailApp" data-ng-controller="pmsSaleProductReservepriceController as ctrl">
<form name="form">
					
	<h1 class="sub_title1">가격변경예약</h1>
	<div class="box_type1">
		<table class="tb_type2">
			<colgroup>
				<col width="*">
				<col width="10%">
				<col width="20%">
				<col width="10%">
				<col width="10%">
			</colgroup>
			<thead>
				<tr>
					<th class="ng-binding">단품옵션정보</th>
					<th>현 추가금액</th>
					<th>변경 추가금액</th>
					<th>등록자</th>
					<th>등록일</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="reserve in reserveList" ng-init="reserve.addSalePrice = !reserve.addSalePrice?reserve.pmsSaleproduct.addSalePrice:reserve.addSalePrice">
					<td class="ng-binding">{{reserve.pmsSaleproduct.name}}</td>
					<td class="ng-binding">{{reserve.pmsSaleproduct.addSalePrice}}</td>
					<td class="ng-binding"><input type="text" ng-model="reserve.addSalePrice" number-only style="width:90%"/></td>
					<td class="ng-binding">{{reserve.insId}}</td>
					<td class="ng-binding">{{reserve.insDt}}</td>
				</tr>
			</tbody>
		</table>		
	</div>	
	
	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" data-ng-click="ctrl.close()">
			<b><spring:message code="c.common.close"/></b>
		</button>	
		<button type="button" class="btn_type3 btn_type3_purple" data-ng-click="ctrl.save()">
			<b>저장</b>
		</button>	
	</div>
</form>	
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>
		