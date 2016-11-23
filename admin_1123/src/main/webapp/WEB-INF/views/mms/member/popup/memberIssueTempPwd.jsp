<%--
	화면명 : 회원상세 > SMS 수신여부 변경
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/mms.app.member.manager.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<div class="wrap_popup"  ng-app="memberManagerApp" data-ng-controller="mms_memberTempPwdPopApp_controller as ctrl">
		<h2 class="sub_title1">임시 비밀번호 발급 <!-- 임시 비밀번호 발급 --></h2>	
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="15%" />
					<col width="35%" />
					<col width="15%" />
					<col width="35%" />
				</colgroup>
				<tbody>
					<tr>
						<th>회원ID<!-- 회원ID --></th>
						<td>
							{{tempPwd.userid}}
						</td>
						<th>회원명<!-- 회원명 --></th>
						<td>
							{{tempPwd.name}}
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="box_type1 marginT1">
			<table class="tb_type1">
				<colgroup>
					<col width="15%" />
					<col width="35%" />
					<col width="15%" />
					<col width="35%" />
				</colgroup>
				<tbody>
					<tr>
						<th>휴대폰<!-- 휴대폰 --></th>
						<td colspan="3">
							<input ng-model="tempPwd.cellno" id="cellno" type="text" name="cellno" placeholder="" style="width:40%;" required/>
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
				<b>패스워드 발급</b>
			</button>
		</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>