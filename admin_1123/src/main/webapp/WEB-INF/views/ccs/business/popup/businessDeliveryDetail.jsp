<%--
	화면명 : 업체 관리 > 업체 상세 > 업체 배송정책 상세
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.business.list.js"></script>	

<div class="wrap_popup"  ng-app="businessApp" data-ng-controller="deliveryDetailController as ctrl" data-ng-init="ctrl.detail()">
	
	<form name="form2">
		<h2 class="sub_title1">배송정책 등록 / 상세 </h2>
		<div class="box_type1 marginT2">
	
			<table class="tb_type1">
				<colgroup>
					<col width="15%" />
					<col width="35%" />
					<col width="15%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th><spring:message code="c.ccsBusiness.deliveryPolicyNo"/></th>
						<td>{{ ccsDeliverypolicy.deliveryPolicyNo }}</td>
						<th><spring:message code="c.ccsBusiness.deliveryPolicyName"/><!-- 정책명 --><i><spring:message code="c.input.required"></spring:message></i></th>
						<td><input type="tel" ng-model="ccsDeliverypolicy.name"  v-key="ccsDeliverypolicy.name"/></td>
					</tr>
					<tr>
						<th><spring:message code="c.ccsBusiness.deliveryServiceName"/><i><spring:message code="c.input.required"/></i></th>
						<td colspan="3">
							<select ng-model="ccsDeliverypolicy.deliveryServiceCd" select-code="DELIVERY_SERVICE_CD" style="width:150px;"  v-key="required">
								<option value="">선택하세요</option>
								<option ng-selected="ccsDeliverypolicy.deliveryServiceCd" value="{{ ccsDeliverypolicy.deliveryServiceCd }}"></option>
							</select>		
						</td>
					</tr>
					<tr>
						<th>배송비<i><spring:message code="c.input.required"/></i></th>
						<td>
							<input type="text" ng-model="ccsDeliverypolicy.deliveryFee" name="deliveryFee" v-key="ccsDeliverypolicy.deliveryFee" style="width:50%;" required/>
						</td>
						<th>무료배송최소금액</th>
						<td>
							<input type="text" ng-init="ccsDeliverypolicy.minDeliveryFreeAmt='0'" ng-model="ccsDeliverypolicy.minDeliveryFreeAmt"  v-key="ccsDeliverypolicy.minDeliveryFreeAmt" style="width:50%;" required
							ng-disabled="ccsDeliverypolicy.deliveryFee==null || ccsDeliverypolicy.deliveryFee == '0' || ccsDeliverypolicy.deliveryFee == ''"/>
						</td>
					</tr>
					
					<tr>
						<th rowspan="4">반품배송지<i><spring:message code="c.input.required"/></i></th>
					</tr>
					<tr>
						<td colspan="3">
							<input type="text" ng-model="ccsDeliverypolicy.zipCd"  v-key="ccsDeliverypolicy.zipCd" style="width:30%;" readonly required/>
							<button type="button" class="btn_type2" ng-click="ctrl.searchAddress()">
								<b><spring:message code="c.ccsBusiness.zipcd"/></button>
							<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : ccsDeliverypolicy.zipCd == null || ccsDeliverypolicy.zipCd == ''}" ng-click="ctrl.eraser()">지우개</button>
						</td>
					</tr>		
					<tr>
						<td colspan="3">
							<input type="text" ng-model="ccsDeliverypolicy.address1"  v-key="ccsDeliverypolicy.address1" style="width:30%;" readonly/>
						</td>
					</tr>
					<tr>
						<td colspan="3">
							<input type="text" ng-model="ccsDeliverypolicy.address2"  v-key="ccsDeliverypolicy.address2" style="width:30%;" readonly/>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b>닫기</b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.update()">
				<b>저장</b>
			</button>
		</div>
	</form>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>