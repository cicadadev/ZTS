<%--
	화면명 : 회원상세 > SMS 수신여부 변경
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/mms.app.member.manager.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<div class="wrap_popup"  ng-app="memberManagerApp" data-ng-controller="mms_changeAppPushReceivePopApp_controller as ctrl">
		<h2 class="sub_title1">APP Push 수신여부 변경 <!-- APP Push 수신여부 변경 --></h2>	
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="20%" />
					<col width="30%" />
					<col width="20%" />
					<col width="30%" />
				</colgroup>
				<tbody>
					<tr>
						<th>회원ID<!-- 회원ID --></th>
						<td>
							{{appPushReceipt.memberId}}
						</td>
						<th>회원명<!-- 회원명 --></th>
						<td>
							{{appPushReceipt.name}}
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="box_type1 marginT1">
			<table class="tb_type1">
				<colgroup>
					<col width="20%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>APP Push 수신여부<!-- APP Push 수신여부 --></th>
						<td ng-init="appPushReceipt.appPushYn" colspan="3">
							<input type="radio" value="Y" ng-model="appPushReceipt.appPushYn" id="radio3"/>
							<label for="radio3">수신<!-- 수신 --></label>
							<input type="radio" value="N" ng-model="appPushReceipt.appPushYn" id="radio4"/>
							<label for="radio4">미수신<!-- 미수신 --></label>
						</td>
					</tr>
				</tbody>
			</table>
		</div>

	
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b>닫기</b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.updateReceipt()">
				<b>저장</b>
			</button>
		</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>