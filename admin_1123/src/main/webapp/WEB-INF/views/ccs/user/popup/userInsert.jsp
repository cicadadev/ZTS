<%--
	화면명 : 사용자 관리 > 사용자 등록 화면
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.user.manager.js"></script>	

<div class="wrap_popup"  ng-app="userApp" data-ng-controller="userInsertController as ctrl" >
	
	<form name="form2">
		<h2 class="sub_title1"><spring:message code="c.ccsUser.insert"/> <!-- 사용자 등록 --></h2>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="9%" />
					<col width="15%" />
					<col width="9%" />
					<col width="15%" />
				</colgroup>
				<tbody>
					<tr>
						<th>사번</th>
						<td> 
							<input type="tel" ng-model="ccsUser.empId"/>
						</td>
						<th>Login-id<%-- <spring:message code="ccsUser.userId"></spring:message> --%> <i><spring:message code="c.input.required"></spring:message></i></th>  <!-- 사용자ID -->
						<td> 
							<input type="text" ng-model="ccsUser.userId"  v-key="ccsUser.userId"/>
							<button type="button" class="btn_type2" ng-click="ctrl.duplicateCheckId()">
								<b>중복확인</b>
							</button>
						</td>
					</tr>
					<tr>
						<th>사용여부</th>
						<td ng-init="ccsUser.userStateCd='USER_STATE_CD.USE'">
							<radio-list ng-model="ccsUser.userStateCd" code-group="USER_STATE_CD" required/>
						</td>
						<th>
							MD여부<i><spring:message code="c.input.required"/></i>
						</th>
						<td ng-init="ccsUser.mdYn='Y'">
							<input type="radio" value="Y" data-ng-model="ccsUser.mdYn" id="radio1"/>
							<label for="radio1">예</label>
							<input type="radio" value="N" data-ng-model="ccsUser.mdYn" id="radio1"/>
							<label for="radio2">아니오<!-- 미전시 --></label>
						</td>
					</tr>
					<tr>
						<th><spring:message code="ccsUser.name"/><i><spring:message code="c.input.required"/></i></th> <!-- 사용자 이름 -->
						<td> <input type="text" ng-model="ccsUser.name"  v-key="ccsUser.name"/></td>
						<th>E-mail<i><spring:message code="c.input.required"/></i></th> <!-- email-->
						<td><input type="text" ng-model="ccsUser.email" v-key="ccsUser.email" required style="width:55%;"/></td>
					</tr>
					<tr>
						<th><spring:message code="ccsUser.pwd"/><i><spring:message code="c.input.required"/></i></th> <!-- 비밀번호 -->
						<td>
							<input type="password" ng-model="ccsUser.pwd1" v-key="ccsUser.pwd" style="width:54%;"/>
						</td>
						<th>
							비밀번호 확인<i><spring:message code="c.input.required"/></i>
						</th>
						<td><input type="password" ng-model="ccsUser.pwd2" v-key="ccsUser.pwd" style="width:54%;"/></td>
					</tr>
					
					<tr>
						<th><spring:message code="c.ccs.user.phone1"></spring:message></th> <!-- 전호번호-->
						<td><input type="tel" ng-model="ccsUser.phone1" tel-input /></td>
						<th>Fax 번호<%-- <spring:message code="c.ccs.user.phone1"></spring:message> --%></th> <!-- 전호번호-->
						<td><input type="tel" ng-model="ccsUser.fax" fax-input/></td>
						
					</tr>
					<tr>
						<th>
							<spring:message code="c.ccs.user.phone2"/><!-- 핸드폰번호--><i><spring:message code="c.input.required"/></i>
						</th>
						<td colspan="3">
							<input type="tel" ng-model="ccsUser.phone2" tel-input v-key="ccsUser.phone2" required/>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.ccs.user.role"/><i><spring:message code="c.input.required"/></i></th> <!-- 역할ID-->
						<td colspan="3">
							<input type="text" ng-model="ccsUser.ccsRole.name" v-key="ccsRole.name" style="width:54%;" required disabled readonly/>
							<button type="button" class="btn_type2" ng-click="ctrl.searchPopup('role')">
								<b>검색</b>
							</button>
							<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : ccsUser.ccsRole.name == null || ccsUser.ccsRole.name == ''}" ng-click="ctrl.eraser('role')">지우개</button>
						</td>
					</tr>
					<tr>
						<th>기타설명
						</th>
						<td colspan="3">
							<textarea cols="10" rows="5" placeholder="" ng-model="ccsUser.note"></textarea>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b>닫기</b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.saveUser()">
				<b>저장</b>
			</button>
		</div>
	</form>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>