<%--
	화면명 : 딜 관리 > 딜 상품 관리 팝업 > 등급별 혜택 관리 팝업
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/sps.app.deal.list.js"></script>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="wrap_popup" style="min-width:500px;" ng-app="dealApp" ng-controller="sps_dealGradeBenefitPopApp_controller as ctrl" ng-init="ctrl.init()">
	<h1 class="sub_title1"><spring:message code="c.spsDealmember.benefitSetting" /><!-- 등급별 혜택 설정 --></h1>
	<div class="box_type1">
			
			<form name="gradeForm"">
			<table class="tb_type1">
				<colgroup>
					<col width="24%" />
					<col width="38%" />
					<col width="38%" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							<spring:message code="c.spsDealmember.memberGrade" /><!-- 회원등급 -->
						</th>
						<th>
							<spring:message code="c.spsDealmember.preOpenDays" /><!-- 우선노출 적용 시작일 -->
						</th>
						<th>
							<spring:message code="c.spsDealmember.addPriceManager" /><!-- 추가 금액 관리 -->
						</th>
					</tr>
<!-- 					<tr ng-repeat="memGradeCd in memGradeCds" >
						<th>
							{{memGradeCd.name}}
						</th>
						<td>
							<input type="text" name="preOpenDays{{$index}}" id="preOpenDays{{$index}}" data-cd="{{memGradeCd.cd}}" value="{{spsDealmember[$index].preOpenDays}}" number-only/>
						</td>
						<td>
							(-)<input type="text" name="addSalePrice{{$index}}" id="addSalePrice{{$index}}" data-cd="{{memGradeCd.cd}}" value="{{spsDealmember[$index].addSalePrice}}" number-only="price"/>
						</td>
					</tr> -->
					<tr ng-repeat="gradePrice in spsDealmember" >
						<th>
							{{gradePrice.memGradeName}}
						</th>
						<td>
							<input type="text" ng-model="gradePrice.preOpenDays" number-only/>
						</td>
						<td>
							(-)<input type="text" ng-model="gradePrice.addSalePrice" v-key="spsDealmember.addSalePrice" number-only="price"/>
						</td>
					</tr>					
				</tbody>
			</table>
		</form>
		
		<div class="btn_alignC" style="margin-top:20px;">
		<button type="button" class="btn_type3 btn_type3_white" ng-click="ctrl.cancel()">
			<b><spring:message code="c.common.close" /><!-- 닫기 --></b>
		</button>
		<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.save()">
			<b><spring:message code="c.common.save" /><!-- 저장 --></b>
		</button>
	</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>