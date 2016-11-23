<%--
	화면명 : 업체 관리 > 업체 상세 > 업체 배송정책 등록
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.business.list.js"></script>	

<div class="wrap_popup" ng-app="businessApp" data-ng-controller="deliveryInsertController as ctrl" data-ng-init="ctrl.detail()">
	
	<form name="form2">
		<h2 class="sub_title1"> 배송정책 등록</h2>
		<div class="box_type1">
	
			<table class="tb_type1">
				<colgroup>
					<col width="15%" />
					<col width="41%" />
					<col width="15%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th><spring:message code="ccsDeliverypolicy.name"/><i><spring:message code="c.input.required"></spring:message></i></th>
						<td><input type="tel" ng-model="ccsDeliverypolicy.name"  v-key="ccsDeliverypolicy.name"/></td>
						<th><spring:message code="ccsDeliverypolicy.deliveryFeeTypeCd"/><i><spring:message code="c.input.required"></spring:message></i></th>
						<td>
							<select-List ng-model="ccsDeliverypolicy.deliveryFeeTypeCd" code-group="DELIVERY_FEE_TYPE_CD" style="width:150px;" v-key="required"></select-List>
						</td>
					</tr>
					<tr>
						<th><spring:message code="ccsDeliverypolicy.deliveryFee"/></th>
						<td><input type="tel" ng-model="ccsDeliverypolicy.deliveryFee"  v-key="ccsDeliverypolicy.deliveryFee"/></td>
						<th><spring:message code="ccsDeliverypolicy.deliveryServiceCd"/><i><spring:message code="c.input.required"></spring:message></i></th>
						<td>
							<select ng-model="ccsDeliverypolicy.deliveryServiceCd" select-code="DELIVERY_SERVICE_CD" style="width:150px;"  v-key="required">
								<option value="">선택하세요</option>
								<option ng-selected="ccsDeliverypolicy.deliveryServiceCd"></option>
							</select>	
							
							
						</td>
					</tr>
					<tr ng-hide="ccsDeliverypolicy.deliveryFeeTypeCd == 'DELIVERY_FEE_TYPE_CD.FREE'">
						<th><spring:message code="ccsDeliverypolicy.minDeliveryFreeAmt"/></th>
						<td><input type="tel" ng-model="ccsDeliverypolicy.minDeliveryFreeAmt"  v-key="ccsDeliverypolicy.minDeliveryFreeAmt"/></td>
						<th><spring:message code="ccsDeliverypolicy.zipCd"/></th>
						<td><input type="tel" ng-model="ccsDeliverypolicy.zipCd"  v-key="ccsDeliverypolicy.zipCd"/></td>
					</tr>
					<tr ng-hide="ccsDeliverypolicy.deliveryFeeTypeCd == 'DELIVERY_FEE_TYPE_CD.FREE'">
						<th><spring:message code="ccsDeliverypolicy.address1"/></th>
						<td><input type="tel" ng-model="ccsDeliverypolicy.address1"  v-key="ccsDeliverypolicy.address1"/></td>
						<th><spring:message code="ccsDeliverypolicy.address2"/></th>
						<td><input type="tel" ng-model="ccsDeliverypolicy.address2"  v-key="ccsDeliverypolicy.address2"/></td>
					</tr>
					<th>배송정보안내</th>
						<td colspan="4">
							<textarea cols="10" rows="5" placeholder="" ng-model="ccsDeliverypolicy.deliveryInfo"></textarea>
						</td>	
					</tr>

					<!-- <tr>
						<th>첨부파일</th>
						<td colspan="3">
							{{ pmsProductqna.img }}
							<button type="button" class="btn_type2">
								<b>파일다운로드</b>
							</button>
						</td>
					</tr> -->
				</tbody>
			</table>
		</div>
	
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b>닫기</b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.insert()">
				<b>저장</b>
			</button>
		</div>
	</form>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>