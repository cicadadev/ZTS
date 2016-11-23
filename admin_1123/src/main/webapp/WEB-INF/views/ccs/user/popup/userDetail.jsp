<%--
	화면명 : 사용자 관리 > 사용자 상세, 수정
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.user.manager.js"></script>	

<div class="wrap_popup" ng-app="userApp" data-ng-controller="userDetailController as ctrl" data-ng-init="ctrl.detail()">
	
	<form name="form2">
		<h2 class="sub_title1"><spring:message code="c.ccsUser.detail"/><!-- 사용자 상세 --></h2>
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
						<th><spring:message code="c.ccs.user.empId"/><!-- 사번 --></th>
						<td> {{ ccsUser.empId }}</td>
						<th><spring:message code="c.ccs.user.userId"/><!-- Login-id --></th>
						<td ng-if="type=='D'"> {{ ccsUser.userId }}</td>
						<td ng-if="type=='I'">
							<input type="text" ng-model="ccsUser.userId"  v-key="ccsUser.userId"/>
							<button type="button" class="btn_type2" ng-click="ctrl.duplicateCheckId()">
								<b>중복확인</b>
							</button>
						</td>
					</tr>
					<tr>
						<th>사용여부</th>
						<td ng-if="type=='D'">
							<radio-list ng-model="ccsUser.userStateCd" code-group="USER_STATE_CD" required/>
						</td>
						<td ng-if="type=='I'" ng-init="ccsUser.userStateCd='USER_STATE_CD.USE'">
							<radio-list ng-model="ccsUser.userStateCd" code-group="USER_STATE_CD" required/>
						</td>
						<th>
							MD여부<i><spring:message code="c.input.required"/></i>
						</th>
						<td ng-init="ccsUser.mdYn='Y'">
							<input type="radio" value="Y" data-ng-model="ccsUser.mdYn" id="radio1"/>
							<label for="radio1">예</label>
							<input type="radio" value="N" data-ng-model="ccsUser.mdYn" id="radio1"/>
							<label for="radio2">아니오</label>
						</td>
					</tr>
					<tr>
						<th><spring:message code="ccsUser.name"/><i><spring:message code="c.input.required"/></i></th> <!-- 사용자 이름 -->
						<td> <input type="tel" ng-model="ccsUser.name"  v-key="ccsUser.name"/></td>
						<th>E-mail<i><spring:message code="c.input.required"/></i></th> <!-- email-->
						<td><input type="text" ng-model="ccsUser.email" v-key="ccsUser.email" required style="width:55%;"/></td>
					</tr>
					<!-- 관리자 접속시  -->
					<tr ng-if="ccsUser.userId != '${loginInfo.loginId}' && type=='D'">
						<th><spring:message code="ccsUser.pwd"/></th> <!-- 비밀번호 -->
						<td colspan="3">
							<!-- <input type="password" ng-model="ccsUser.pwd" v-key="ccsUser.pwd"  style="width:30%;" readonly disabled/> -->
							<button type="button"  class="btn_type2" ng-click="ctrl.pwdInit()">
								<b> 초기화 </b>
							</button>
						</td>
					</tr>
					
					<!-- 본인 접속시  -->
					<tr ng-if="ccsUser.userId == '${loginInfo.loginId}' && type=='D'" >
						<th>비밀번호 설정</th> <!-- 현재비밀번호 -->
<!-- 							<td colspan="3" ng-if="changePwdMode"> -->
<!-- 								현재 비밀번호   : <input type="password" ng-model="ccsUser.pwd"  style="width:150px;" required/><br/> -->
<!-- 								신규 비밀번호   : <input type="password" ng-model="ccsUser.newPwd1" style="width:150px;" required/><br/> -->
<!-- 								신규 비밀번호 확인 : <input type="password" ng-model="ccsUser.newPwd2" style="width:150px;" required/> -->
<!-- 								<p ng-if="!ccsUser.pwd || !ccsUser.newPwd1 || !ccsUser.newPwd2" class="information">현재비밀번호, 신규비밀번호, 신규비밀번호확인을 입력해 주세요.</p> -->
<!-- 							</td> -->
							<td colspan="3">
								<button type="button"  class="btn_type2" ng-click="ctrl.changePwd()">
									<b>비밀번호 변경</b>
								</button>
							</td>							
					</tr>
<%-- 					<tr ng-if="ccsUser.userId == '${loginInfo.loginId}'">
						<th>신규비밀번호</th> <!-- 신규비밀번호 -->
							<td>
								<input type="password" ng-model="ccsUser.newPwd1" style="width:52%;"/>
							</td>
						<th>신규비밀번호 확인</th> <!-- 신규비밀번호 확인 -->
						<td colspan="3">
							<input type="password" ng-model="ccsUser.newPwd2" style="width:52%;"/>
						</td>
					</tr> --%>
					<!-- 신규 등록시  -->
					<tr ng-if="type=='I'">	
						<th><spring:message code="ccsUser.pwd"/><i><spring:message code="c.input.required"/></i></th> <!-- 비밀번호 -->
						<td>
							<input type="password" ng-model="ccsUser.pwd1" v-key="ccsUser.pwd"  style="width:50%;"/>
						</td>
						<th><spring:message code="c.ccs.user.pwdconfirm"/><i><spring:message code="c.input.required"/></i></th> <!-- 비빌번호 확인 -->
						<td>
							<input type="password" ng-model="ccsUser.pwd2" v-key="ccsUser.pwd"  style="width:50%;"/>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.ccs.user.phone1"></spring:message></th> <!-- 전호번호-->
						<td><input type="tel" ng-model="ccsUser.phone1" tel-input /></td>
						<th>Fax 번호<%-- <spring:message code="c.ccs.user.phone1"></spring:message> --%></th> <!-- 전호번호-->
						<td><input type="tel" ng-model="ccsUser.fax" fax-input/></td>
						
					</tr>
					<tr>
						<th>
							<spring:message code="c.ccs.user.phone2" /><i><spring:message code="c.input.required"/></i><!-- 핸드폰번호-->
						</th>
						<td colspan="3">
							<input type="tel" ng-model="ccsUser.phone2" tel-input v-key="ccsUser.phone2" required/>
						</td>
					</tr>
					<!-- 메인 정보 변경시 권한 영역 숨김 -->
					<tr ng-if="mainDetail != 'Y'">
						<th><spring:message code="c.ccs.user.role"></spring:message><i><spring:message code="c.input.required"/></i></th> <!-- 역할ID-->
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
				<b><spring:message code="c.common.close"/><!-- 닫기 --></b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.update()">
				<b><spring:message code="c.common.save"/><!-- 저장 --></b>
			</button>
		</div>
	</form>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>