<%--
	화면명 : 업체 관리 > 업체 상세 > 사용자 상세
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.business.list.js"></script>	

<div class="wrap_popup" ng-app="businessApp" data-ng-controller="userDetailController as ctrl" data-ng-init="ctrl.detail()">
	
	<form name="form2">
		<h2 class="sub_title1">사용자 등록 / 상세<!-- 사용자 등록 / 상세 --></h2>
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
						<th>ID <!-- 사용자ID --> </th>
						
						<td colspan="3" ng-if="type == 'I'"> 
							<input type="tel" ng-model="ccsUser.userId"  v-key="ccsUser.userId"/>
							<button type="button" class="btn_type2" ng-click="ctrl.duplicateCheckId()">
								<b>중복확인</b>
							</button>
						</td>
						
						<td colspan="3" ng-if="type == 'D'"> 
							{{ ccsUser.userId }}
						</td>
					</tr>
					<tr>
						<th>사용자 이름<i><spring:message code="c.input.required"/></i></th> <!-- 사용자 이름 -->
						<td>
							<input type="text" ng-model="ccsUser.name"  v-key="ccsUser.name" style="width:80%;"/>
						</td>
						<th>E-mail<i><spring:message code="c.input.required"/></i></th> <!-- email-->
						<td>
							<input type="text" ng-model="ccsUser.email" v-key="ccsUser.email" style="width:80%;" required/>
						</td>
					</tr>

					<!-- 관리자 접속시  -->
					<tr ng-if="(ccsUser.userId != '${loginInfo.loginId}') && (type == 'D')">
						<th><spring:message code="ccsUser.pwd"/></th> <!-- 비밀번호 -->
						<td colspan="3">
							<!-- <input type="password" ng-model="ccsUser.pwd" v-key="ccsUser.pwd"  style="width:30%;" readonly disabled/> -->
							<button type="button"  class="btn_type2" ng-click="ctrl.pwdInit()">
								<b> 초기화 </b>
							</button>
						</td>
					</tr>
					
					<!-- 본인 접속시  -->
					<tr ng-if="(ccsUser.userId == '${loginInfo.loginId}') && (type == 'D')">
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
					<!-- 신규 등록시  -->
					<tr ng-if="type == 'I'">	
						<th><spring:message code="ccsUser.pwd"/><i><spring:message code="c.input.required"/></i></th> <!-- 비밀번호 -->
						<td>
							<input type="password" ng-model="ccsUser.pwd1" v-key="ccsUser.pwd"  style="width:50%;"/>
						</td>
						<th><spring:message code="c.ccs.user.pwdconfirm"/><i><spring:message code="c.input.required"/></i></th> <!-- 비빌번호 확인 -->
						<td>
							<input type="password" ng-model="ccsUser.pwd2" v-key="ccsUser.pwd"  style="width:50%;"/>
						</td>
					</tr>
					<%-- 
					<!-- 관리자 접속시  -->
					<tr ng-if="(ccsUser.userId != '${loginInfo.loginId}') && (type == 'D')">
						<th><spring:message code="ccsUser.pwd"/><i><spring:message code="c.input.required"/></i></th> <!-- 비밀번호 -->
						<td colspan="3">
							<input type="password" ng-model="ccsUser.pwd" v-key="ccsUser.pwd"  style="width:30%;" readonly disabled/>
							<button type="button"  class="btn_type2" ng-click="ctrl.pwdInit()">
								<b> 초기화 </b>
							</button>
						</td>
					</tr>
					
					<!-- 본인 접속시  -->
					<tr ng-if="(ccsUser.userId == '${loginInfo.loginId}') && (type == 'D')">
						<th>현재비밀번호<i><spring:message code="c.input.required"/></i></th> <!-- 현재비밀번호 -->
						<td>
							<input type="password" ng-model="ccsUser.pwd" v-key="ccsUser.pwd"  style="width:50%;"/>
						</td>
						<th>신규비밀번호<i><spring:message code="c.input.required"/></i></th> <!-- 신규비밀번호 -->
						<td>
							<input type="password" ng-model="ccsUser.newPwd1" v-key="ccsUser.pwd"  style="width:50%;"/>
						</td>
					</tr>
					<tr ng-if="(ccsUser.userId == '${loginInfo.loginId}') && (type == 'D')">
						<th>신규비밀번호 확인<i><spring:message code="c.input.required"/></i></th> <!-- 신규비밀번호 확인 -->
						<td colspan="3">
							<input type="password" ng-model="ccsUser.newPwd2" v-key="ccsUser.pwd"  style="width:20%;"/>
						</td>
					</tr>
					<!-- 신규 등록시  -->
					<tr ng-if="type == 'I'"> 
						<th><spring:message code="ccsUser.pwd"/><i><spring:message code="c.input.required"/></i></th> <!-- 비밀번호 -->
						<td>
							<input type="password" ng-model="ccsUser.pwd1" v-key="ccsUser.pwd"  style="width:50%;"/>
						</td>
						<th><spring:message code="c.ccs.user.pwdconfirm"/><i><spring:message code="c.input.required"/></i></th> <!-- 비빌번호 확인 -->
						<td>
							<input type="password" ng-model="ccsUser.pwd2" v-key="ccsUser.pwd"  style="width:50%;"/>
						</td>
					</tr> --%>
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
	</form>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>