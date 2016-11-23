<%--
	화면명 : 딜 관리 > 딜 상세 팝업
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/sps.app.deal.list.js"></script>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="wrap_popup"  ng-app="dealApp" ng-controller="sps_dealDetailPopApp_controller as ctrl" ng-init="ctrl.init()">
		<ul class="tab_type2">
			<li class="on">
				<button type="button"><spring:message code="c.spsDeal.detail"/> <!-- 딜 상세 --></button>
			</li>
			<li>
				<button type="button" ng-click="ctrl.moveTab('divTitleManager')" fn-id="24_TAP"><spring:message code="c.spsDeal.divTitle.manage"/><!-- 구분타이틀 관리 --></button>
			</li>
			<li>
				<button type="button" ng-click="ctrl.moveTab('dealProduct')"><spring:message code="c.spsDeal.product.manage"/><!-- 상품 관리 --></button>
			</li>
		</ul>
		<div class="box_type1 marginT2">
			<form name="spsDealForm">
				<table class="tb_type1">
					<colgroup>
						<col width="18%" />
						<col width="38%" />
						<col width="13%" />
						<col width="*" />
					</colgroup>
					<tbody>
						<tr>
							<th>
								<spring:message code="c.spsDeal.deal.id" /> <!-- 딜 번호 -->
							</th>
							<td>
								{{spsDeal.dealId}}
							</td>
							<th>
								<spring:message code="c.spsDeal.deal.name" /><!-- 딜명 --> <i><spring:message code="c.input.required" /> <!-- 필수입력 --></i>
							</th>
							<td>
								<input data-ng-model="spsDeal.name" id="name" type="text" name="name" placeholder="" style="width:40%;" v-key="spsDeal.name" required/>
							</td>
						</tr>
						<tr>
							<th>
								<spring:message code="c.spsDeal.deal.displayYn" /><!-- 전시 여부 --> <i><spring:message code="c.input.required" /> <!-- 필수입력 --></i>
							</th>
							<td ng-init="spsDeal.displayYn='Y'">
								<input type="radio" value="Y" data-ng-model="spsDeal.displayYn" id="radio1"/>
								<label for="radio1"><spring:message code="c.input.radio.displayY" /> <!-- 전시 --></label>
								<input type="radio" value="N" data-ng-model="spsDeal.displayYn" id="radio1"/>
								<label for="radio2"><spring:message code="c.input.radio.displayN" /> <!-- 미전시 --></label>
							</td>
							<th>
								<spring:message code="c.spsDeal.deal.sortNo" /><!-- 우선 순위 --> <i><spring:message code="c.input.required" /> <!-- 필수입력 --></i>
							</th>
							<td>
								<input data-ng-model="spsDeal.sortNo" id="sortNo" type="number" name="sortNo" placeholder="" style="width:30%;" v-key="spsDeal.sortNo" required/>
							</td>
						</tr>
<!-- 						<tr> -->
<!-- 							<th> -->
<%-- 								<spring:message code="c.spsDeal.deal.restrictType" /><!-- 제한 유형 --> <i><spring:message code="c.input.required" /> <!-- 필수입력 --></i> --%>
<!-- 							</th> -->
<!-- 							<td colspan="3"> -->
<%-- 								제외옵션 :  except="mgrade,mtype,device,channel"  --%>
<!-- 								<control-set model-name="spsDeal" lebels="전체,사용자설정" except="channel"></control-set> -->
<!-- 							</td> -->
<!-- 						</tr> -->
					</tbody>
				</table>
			</form>
		</div>

		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b><spring:message code="c.common.close"/> <!-- 취소 --></b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.save()">
				<b><spring:message code="c.common.save"/> <!-- 저장 --></b>
			</button>
		</div>
</div>

<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>

