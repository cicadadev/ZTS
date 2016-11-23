<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true" />
<link rel="stylesheet" type="text/css" href="/resources/css/oms.css" />
<script type="text/javascript" src=/resources/js/app/oms.app.claim.etc.js></script>

<div class="wrap_popup" ng-app="claimEtcApp" ng-controller="deliveryCtrl as ctrl" ng-init="ctrl.init()">
	<!-- ### 1번 탭 ### -->
	<article class="con_box">
		<h2 class="sub_title1">
			배송지 변경
		</h2>
	
		<!-- ### 배송지 목록 ### -->
		<div class="box_type1 marginT3">
			<h3 class="sub_title2">
				배송지 목록
				<span><spring:message code="c.search.totalCount" arguments="{{ grid_address.totalItems }}" /></span>
			</h3>
			<div class="gridbox">
				<div class="grid" ui-grid="grid_address" 
					ng-style="{height: (grid_address.totalItems*30+(grid_address.totalItems == 0 ? 30 : 0))+30+17+'px'}"
					ui-grid-move-columns 
					ui-grid-resize-columns 
					ui-grid-auto-resize 
					ui-grid-selection></div>
			</div>
		</div>
		<!-- ### //배송지 목록 ### -->
		<div class="btn_alignC marginT1">
			<button type="button" class="btn_type1" ng-click="ctrl.select()">
				<b><spring:message code="c.common.select" /></b>
			</button>
		</div>
		
		<div class="box_type1 marginT3">
			<form name="deliveryForm">
				<table class="tb_type1">
					<colgroup>
						<col width="15%" />
						<col width="18%" />
						<col width="15%" />
						<col width="18%" />
						<col width="15%" />
						<col width="*" />
					</colgroup>
		
					<tbody>
						<tr>
							<th>수취인<i><spring:message code="c.input.required" /></i></th>
							<td>
								<input type="text" ng-model="omsDeliveryaddress.name1" required />
							</td>
							<th>휴대전화번호<i><spring:message code="c.input.required" /></i></th>
							<td>
								<input type="text" ng-model="omsDeliveryaddress.phone2" required />
							</td>
							<th>전화번호</th>
							<td>
								<input type="text" ng-model="omsDeliveryaddress.phone1" />
							</td>
						</tr>
						<tr>
							<th>배송지<i><spring:message code="c.input.required" /></i></th>
							<td colspan="5">
								<div class="marginT5">
									<input type="tel" ng-model="omsDeliveryaddress.zipCd" v-key="zipCd" />
									<button type="button" class="btn_type2" ng-click="func.popup.zip(1)">
										<b>우편번호검색</b>
									</button>
								</div>
								<div class="marginT5">
									<%-- <button type="button" class="btn_eraser" ng-click="ctrl.eraser()">지우개</button><br/> --%>
									<input type="text" ng-model="omsDeliveryaddress.address1" style="width: 95%;" />
								</div>
								<div class="marginT5">
									<input type="text" ng-model="omsDeliveryaddress.address2" style="width: 95%;" />
								</div>
							</td>
						</tr>
						<tr>
							<th>메시지</th>
							<td colspan="5">
								<%--
								<select id="noteType" ng-model="noteType" ng-init="noteType = '2'" style="width: 24%;height: 24px;" ng-change="ctrl.setMessage(noteType);">
									<option value="">배송 메세지를 선택해 주세요</option>
									<option ng-repeat="option in deliveryMessageCds" value="{{option.cd}}">{{option.name}}</option>
									<option value="">배송 메세지를 선택해 주세요</option>
									<option value="1">부재시 경비실에 맡겨주세요</option>
									<option value="2">배송전 전화주세요</option>
									<option value="3">없으면 도로 가져가세요~</option>
								</select>
								--%>
								<select id="noteType" ng-model="noteType" ng-init="noteType = ''" style="width: 24%;height: 24px;" ng-change="ctrl.setMessage(noteType);">
									<option value="" selected="selected">배송 메세지를 선택해 주세요</option>
									<option ng-repeat="option in deliveryMessageCds" value="{{option.cd}}">{{option.name}}</option>
								</select>
								<input type="text" ng-model="omsDeliveryaddress.note" style="width:70%;"/>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
	
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="func.popup.close()">
				<b><spring:message code="c.common.close" /></b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.save()">
				<b><spring:message code="c.common.save" /></b>
			</button>
		</div>

	</article>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true" />
