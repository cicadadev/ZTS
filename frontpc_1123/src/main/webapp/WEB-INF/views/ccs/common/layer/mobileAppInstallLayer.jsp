<%--
	화면명 : 모바일앱 설치 URL 문자 받기
	작성자 : roy
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<script type="text/javascript">
function mobileAppPhoneLayer(){
	var phone = $('#mobileAppPhoneLayer').val();
	ccs.common.mobileAppSend.send(phone, function(callback) {
		if(callback == 'SUCCESS'){
			ccs.layer.close("mobileAppLayer");
		}
	});
}
</script>

<div class="pop_wrap ly_winner" id="mobileAppLayer">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">모바일앱 설치 URL 문자 받기</h3>
		</div>
		<div class="pop_content">
			<div class="srchList">
				<div>
					<div class="inputTxt_place1">
						<label></label> <span> 
						<input type="text" id="mobileAppPhoneLayer">
						</span>
					</div>
					<a href="#none" onclick="mobileAppPhoneLayer();" class="btn_sStyle4 sPurple1 btn_winner">동의하고 문자 보내기</a>
				</div>
			</div>
		</div>
		<button type="button" id="closeBtn" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>
