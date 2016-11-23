<%--
	화면명 : 회원상세 > 예치금 > 예치금 조정
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/mms.app.member.manager.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<div class="wrap_popup"  ng-app="memberManagerApp" data-ng-controller="mms_depostAdjustmentPopApp_controller as ctrl">
		<h2 class="sub_title1">예치금 조정 <!-- 예치금 조정 --></h2>	
		<div class="box_type1 marginT1">
			<table class="tb_type1">
				<colgroup>
					<col class="col_142" />
					<col class="col_auto" />
				</colgroup>
				<tbody>
					<tr>
						<th>조정 예치금<!-- 조정 예치금 --></th>
						<td>
							관리자 조정
							<input type="number" ng-model="mmsDeposit.depositAmt" style="width:22.5%" ng-init="mmsDeposit.depositAmt=''"/>
							<p class="information" ng-show="mmsDeposit.depositAmt == '' || mmsDeposit.depositTypeCd == ''">필수 입력 항목 입니다.</p>
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

	
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b>닫기</b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.saveDeposit()">
				<b>저장</b>
			</button>
		</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>