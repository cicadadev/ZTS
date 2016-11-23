<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true" />
<link rel="stylesheet" type="text/css" href="/resources/css/oms.css" />
<script type="text/javascript" src="/resources/js/app/oms.app.claim.redelivery.js"></script>
<style>
.ui-grid-viewport {
	height: auto !important;
}
</style>
<div class="wrap_popup" ng-app="claimDetailApp" ng-controller="redeliveryCtrl as ctrl" ng-init="ctrl.init()">
	<!-- ### 1번 탭 ### -->
	<article class="con_box">
		<h2 class="sub_title1">재배송 처리</h2>
		
		<div class="box_type1">
			<h3 class="sub_title2">
				[{{omsClaimdelivery.saleTypeCd == 'SALE_TYPE_CD.CONSIGN' ? '업체배송 /' + omsClaimdelivery.businessName : '센터배송'}}]
				&nbsp;{{omsClaimdelivery.deliveryPolicyNm}}
			</h3>
			
			<table class="tb_type1">
				<colgroup>
					<col width="*" />
					<col width="14%" />
					<col width="30%" />
					<col width="14%" />
					<col width="30%" />
				</colgroup>
	
				<tbody>
					<tr>
						<th></th>
						<th class="alignR">재배송상태</th>
						<td>{{omsClaimdelivery.omsClaim.claimStateName}}</td>
						<th class="alignR">정책배송비</th>
						<td>{{omsClaimdelivery.deliveryFee | number:0}}</td>
					</tr>
					<tr>
						<th rowspan="3" class="alignC">배송지정보<i><spring:message code="c.input.required" /></i></th>
						<th class="alignR">이름</th>
						<td colspan="3"><input type="text" ng-model="omsDeliveryaddress.name1" /></td>
					</tr>
					<tr>
						<th class="alignR">휴대전화</th>
						<td><input type="text" ng-model="omsDeliveryaddress.phone2" /></td>
						<th class="alignR">일반전화</th>
						<td><input type="text" ng-model="omsDeliveryaddress.phone1" /></td>
					</tr>
					<tr>
						<th class="alignR">주소</th>
						<td colspan="3">
							<div class="marginT5">
								<input type="tel" ng-model="omsDeliveryaddress.zipCd"/>
								<button type="button" class="btn_type2" ng-click="func.popup.zip(1)">
									<b>우편번호검색</b>
								</button><br/>
							</div>
							<div class="marginT5"><input type="text" ng-model="omsDeliveryaddress.address1" style="width: 95%;"/></div>
							<div class="marginT5"><input type="text" ng-model="omsDeliveryaddress.address2" style="width: 95%;"/></div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>

		<div class="box_type1 marginT1">
			<h3 class="sub_title2">
				<spring:message code="c.pms.product.list" /><%-- 상품 목록 --%>
				<span><spring:message code="c.search.totalCount" arguments="{{ visibleEntities.length }}" /></span>
			</h3>
			<div class="gridbox">
				<div class="grid" 
					ng-style="{height: (visibleEntities.length*30)+30+17+'px'}"
					ui-grid="grid_claim" 
					ui-grid-selection ui-grid-move-columns ui-grid-edit ui-grid-row-edit ui-grid-pinning
					ui-grid-resize-columns ui-grid-auto-resize ui-grid-validate></div>
			</div>
		</div>
		
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="func.popup.close()">
				<b><spring:message code="c.common.close" /></b>
			</button>
			<%--
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.claim('REDELIVERY','ACCEPT')" ng-show="visibleEntities | filterClaim:'orderProductStateCd':'REDELIVERY'">
				<b>재배송접수</b>
			</button>
			--%>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.insert('REDELIVERY','ACCEPT')" ng-if="!omsClaimdelivery.claimNo">
				<b>재배송접수</b>
			</button>
		</div>
		
	</article>
	<!-- ### //1번 탭 ### -->
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true" />