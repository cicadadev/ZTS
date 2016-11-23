<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html>
<html>
<head>
<jsp:include page="/WEB-INF/views/gsf/commonScript.jsp" flush="true"/>
</head>
<script type="text/javascript" src="/resources/js/app/ccs.app.login.js"></script>
<body ng-app="loginApp" ng-controller="loginPoController as ctrl">
	<div class="wrap lnb_hide" style="height:100%;">
		<!-- ### header : 2016.04.27 추가 ### -->
		<header>
			<h1 class="po_logo">
				<a href="#none">PARTNER OFFICE</a>
			</h1>
		</header>
		<!-- ### //header : 2016.04.27 추가 ### -->

		<!-- ### content ### -->
			<div class="login_box">
				<div class="login_inner">
					<div class="box">
						<form method="post" name="frm" method="post">
						<h2>Log in</h2>
						<div><input type="text" ng-model="ctrl.loginData.j_username" name="j_username" value="" title="id" placeholder="ID" is-value /></div>						
						<div><input type="password" ng-model="ctrl.loginData.j_password" name="j_password" value="" title="password" placeholder="PW" is-value /></div>
						<a href="#" class="btn_login" data-ng-click="ctrl.login()">로그인</a>
						<div class="alignR">
							<a href="#" ng-click="ctrl.findPwd()">비밀번호찾기</a>
							<a href="#" ng-click="ctrl.openRegBusiness()">입점신청</a>
						</div>
						</form>
					</div>
	
					<footer class="login_footer">
						Copyright © 2016 INTUNE CS. All rights reserved
					</footer>
				</div>
			</div>
		<!-- ### //content ### -->
	</div>
</body>
</html>