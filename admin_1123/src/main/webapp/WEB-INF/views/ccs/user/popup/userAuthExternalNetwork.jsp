<%--
	화면명 :  로그인 > 외부망 로그인 보인인증
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.login.js"></script>
<script type="text/javascript" src="/resources/lib/countdown/jquery.countdown.min.js"></script>	
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<script>

// $(document).ready(function(){
// 	var date = new Date().getTime() + (1000 * 60 * 3);
// 	$('#count').countdown(date)
// 	.on('update.countdown', function(event) {
// 	  var format = '%M : %S';
	 
// 	  $(this).html(event.strftime(format));
// 	})
// 	.on('finish.countdown', function(event) {
// 	  alert("시간이 만료되었습니다.");
// 	  window.close();
	  
// 	});
// });


</script>
<style type="text/css">
	.appBox {margin-top:10px; zoom:1;}
	.appBox:after {clear:both; content:""; display:block;}
	.wrap_popup .appBox .inp_num {float:left; width:69%; height:46px; padding:0; font-size:16px; line-height:46px; color:#2e3192;}
	.appBox #count {float:right; width:29%; height:48px; font-weight:bold; font-size:16px; line-height:48px; color:#fff; text-align:center; background-color:#141759;}
	.appBox .btn_box {clear:both; padding-top:15px; text-align:center;}
	.appBox .btn_box button {width:100%;}
	.app_txt {margin-top:10px; padding-left:17px; text-indent:-17px;}
</style>
<div class="wrap_popup"  ng-app="loginApp" data-ng-controller="authExternalNetworkController as ctrl" style="min-width:200px; width:318px;">
	<h1 class="sub_title1">외부망 로그인 본인인증</h1>
	
	<p class="app_txt">
		※ 등록된 휴대폰 번호로 인증번호가 발송되었습니다.    전송받은 인증번호를 입력해 주세요. 
	</p>
	
	<div class="appBox">
		<input type="text" name="authNo"  placeholder="인증번호" class="inp_num" ng-model="authNumber"/>
		<div id="count">

		</div>
		<br>
		<div class="btn_box">
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.auth()">
				<b>인증</b>
			</button>
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>