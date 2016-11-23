<%--
	화면명 : 회원상세 > SMS 수신여부 변경
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/mms.app.member.manager.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<div class="wrap_popup"  ng-app="memberManagerApp" data-ng-controller="mms_memberCarrotPopApp_controller as ctrl">
		<h2 class="sub_title1">B2E 변경 <!-- APP Push 수신여부 변경 --></h2>	
		<div class="box_type1">
			<table class="tb_type1">
				<tbody>
					<tr>
						<th>회원ID<!-- 회원ID --></th>
						<td>
							ZTS
						</td>
						<th>회원명<!-- 회원명 --></th>
						<td>
							박상혁
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="box_type1 marginT1">
			<table class="tb_type1">
				<tbody>
					<tr>
						<th>B2E 회원여부<!-- APP Push 수신여부 --></th>
						<td ng-init="" colspan="3">
							<input type="radio" value="Y" ng-model="" id="radio3"/>
							<label for="radio3">수신<!-- 수신 --></label>
							<input type="radio" value="N" ng-model="" id="radio4"/>
							<label for="radio4">미수신<!-- 미수신 --></label>
						</td>
						<th>사업자 번호<!-- 사업자 번호 --></th>
						<td>
							<input ng-model="" id="name" type="text" name="name" placeholder="" style="width:40%;" required/>
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
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>