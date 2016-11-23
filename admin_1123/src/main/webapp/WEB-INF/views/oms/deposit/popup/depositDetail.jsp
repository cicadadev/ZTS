<%--
	화면명 : 주문 관리 > 예치금관리 > 예치금 조정
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/oms.app.deposit.list.js"></script>

<div class="wrap_popup"  ng-app="depositListApp" ng-controller="oms_depositListDetailPopApp_controller as ctrl">
	<h2 class="sub_title1"><spring:message code="c.oms.deposit.adjust" /> <!-- 예치금 조정 --></h2>
	<form name="form">
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col class="col_142" />
				<col class="col_auto" />
			</colgroup>
			<tbody>
				<tr>
					<th>
						회원ID / 명
					</th>
					<td>
						<input type="text" value="" ng-model="userid" style="width:15%;" readonly="readonly" ng-init="userid=''"/>
						<input type="text" value="" ng-model="username" style="width:15%;" readonly="readonly"/>
						<button type="button" class="btn_type2" ng-click="ctrl.memberPopup()">
							<b><spring:message code="c.search.btn.search" /></b>
						</button>
						<button type="button" class="btn_eraser"  ng-class="{'btn_eraser_disabled' : userid == null || userid == ''}" 
						ng-click="ctrl.eraser('userid', 'username')"><spring:message code="c.search.btn.eraser"/></button>
						<p class="information" ng-show="userid==''">필수 입력 항목 입니다.</p>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<br/><br/>	
	
	<div class="box_type1 marginT1">
		<table class="tb_type1">
			<colgroup>
				<col class="col_142" />
				<col class="col_auto" />
			</colgroup>
			<tbody>
			</tr>
				<tr>
					<th><spring:message code="c.oms.deposit.adjust2" /><!-- 조정 예치금 --></th>
					<td>
						<select-list ng-model="mmsDeposit.depositTypeCd" code-group="DEPOSIT_TYPE_CD" ng-init="mmsDeposit.depositTypeCd =''"></select-list>
						<input type="number" ng-model="mmsDeposit.depositAmt" style="width:22.5%" ng-init="mmsDeposit.depositAmt=''" v-key="mmsDeposit.depositAmt" required />
<!-- 							<p class="information" ng-show="mmsDeposit.depositAmt == '' || mmsDeposit.depositTypeCd == ''">필수 입력 항목 입니다.</p> -->
					</td>
				</tr>
				<tr>
				<th><spring:message code="c.sps.carrot.note" /> <!-- 사유 --></th>
				<td>
					<input type="text" value="" ng-model="mmsDeposit.note" style="width:50%;"/>
				</td>
			</tr>
			</tbody>
		</table>
	</div>

	</form>
	
	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
		<b><spring:message code="c.common.close"/><!-- 닫기 --></b>
	</button>
		<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.adjustDeposit()">
		<b><spring:message code="c.common.save"/><!-- 저장 --></b>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>