<%--
	화면명 : 비밀번호 변경
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.user.manager.js"></script>	

<style>
::-webkit-input-placeholder {

   text-align: center;

}

:-moz-placeholder { /* Firefox 18- */

   text-align: center;

}

::-moz-placeholder {  /* Firefox 19+ */

   text-align: center;

}

:-ms-input-placeholder {  

   text-align: center;

}

	.appBox {margin-top:10px; zoom:1;}
	.appBox:after {clear:both; content:""; display:block;}
	.wrap_popup .appBox .inp_num {float:left; width:100%; height:26px; padding:0; font-size:16px; color:#2e3192;}
	.wrap_popup .appBox .inp_num + .inp_num {margin-top:5px;}
	.appBox #count {float:right; width:29%; height:48px; font-weight:bold; font-size:16px; color:#fff; text-align:center; background-color:#141759;}
	.appBox .btn_box {clear:both; padding-top:15px; text-align:center;}
	.appBox .btn_box button {width:100%;}
	.app_txt {margin-top:10px; padding-left:17px; text-indent:-17px;}

</style>

<div class="wrap_popup" style="min-width:300px;" ng-app="userApp" data-ng-controller="modifyPwdController as ctrl">
	<h1 class="sub_title1">비밀번호 변경</h1>
	
	<form name="form">
		<div class="appBox">
			<input type="password" placeholder="현재 비밀번호" name="pwd" ng-model="ccsUser.pwd" class="inp_num">
			<input type="password" placeholder="신규 비밀번호" name="newPwd" ng-model="ccsUser.newPwd"  class="inp_num">
			<input type="password" placeholder="신규 비밀번호 확인" name="newPwd2" ng-model="ccsUser.newPwd2"  class="inp_num">
		</div>
	
		
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b><spring:message code="c.common.close"/><!-- 닫기 --></b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.modifyPwd()">
				<b><spring:message code="c.common.confirm"/><!-- 확인 --></b>
			</button>
		</div>
	</form>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>