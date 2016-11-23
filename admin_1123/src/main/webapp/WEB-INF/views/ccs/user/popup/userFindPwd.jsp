<%--
	화면명 : 비밀번호 찾기
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.login.js"></script>	

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
	.wrap_popup .appBox .inp_num {float:left; width:100%; height:26px; padding:0; font-size:16px;  color:#2e3192;}
	.wrap_popup .appBox .inp_num + .inp_num {margin-top:5px;}
	.appBox #count {float:right; width:29%; height:48px; font-weight:bold; font-size:16px; color:#fff; text-align:center; background-color:#141759;}
	.appBox .btn_box {clear:both; padding-top:15px; text-align:center;}
	.appBox .btn_box button {width:100%;}
	.app_txt {margin-top:10px; padding-left:17px; text-indent:-17px;}

</style>

<div class="wrap_popup" style="min-width:300px;" ng-app="loginApp" data-ng-controller="findPwdController as ctrl">
	<h1 class="sub_title1">비밀번호 찾기</h1>
	
	<form name="form">
		<div class="appBox" ng-show="search.systemType == 'PO'">
			<input type="text" placeholder="사용자 아이디" name="id" required ng-model="search.userId"  is-value class="inp_num">
			<!-- <input type="text" placeholder="사업자등록번호" name="businessid" required ng-model="search.regNo" value="23642346236" is-value class="inp_num"> -->
			<input type="text" placeholder="영업담당자 휴대폰번호" name="phone" required ng-model="search.phone"  is-value class="inp_num">
		</div>
		<div class="appBox" ng-show="search.systemType == 'BO'">
			<input type="text" placeholder="사용자 아이디" name="id" ng-model="search.userId" class="inp_num">
			<input type="text" placeholder="휴대폰번호" name="phone" ng-model="search.phone" class="inp_num">
		</div>
		
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b><spring:message code="c.common.close"/><!-- 닫기 --></b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.findPwd()">
				<b><spring:message code="c.common.confirm"/><!-- 확인 --></b>
			</button>
		</div>
	</form>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>