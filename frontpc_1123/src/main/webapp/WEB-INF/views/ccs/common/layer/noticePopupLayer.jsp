<%--
	화면명 : 공지사항 팝업 레이어
	작성자 : roy
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@page import="java.util.*"%>

<!-- <script type="text/javascript" src="/resources/js/common/common.ui.js"></script> -->

<!-- <script type="text/javascript" src="/resources/js/mo.js"></script> -->

<!-- /*cookieKey : cookieKey,
		popupNo : response[i].popupNo,
		title : response[i].title,
		detail2 : response[i].detail2,*/ -->
<script type="text/javascript">
	function setCookie(name, value, expiredays){
		//console.log("set", name);
		var todayDate = new Date();
		todayDate.setDate(todayDate.getDate() + expiredays);
		document.cookie = name + '=' + escape( value ) + '; path=/; expires=' + todayDate.toGMTString() + ';';
	};
	
	function closePop(param, target){
		/* if(document.getElementById("noticePopupCheck" + target).checked){
		} */
		setCookie(param, 'done', 1);
		$("#noticePopupLayer" + target).remove();
	};
	function closePop2(target){
		$("#noticePopupLayer" + target).remove();
	};
</script>
		
<style>

.maxW img {max-width:100%;}

</style>
								
<div class="layer_style1 noticePopup" id="noticePopupLayer${search.popupNo}" style="display:block;top:200px;">
	<div class="box">
		<div class="conArt">
			<%-- <strong class="title">${search.title}</strong> --%>

			<div class="conBox maxW">
				${popup.detail2}
			</div>
			<%-- <div>
				<label class="chk_style1">
					<em>
						<input type="checkbox" name="noticePopupCheck" id="noticePopupCheck${search.popupNo}" />
					</em>
					<span>오늘 하루 이 창을 열지 않음</span> 
				</label>
			</div> --%>
			<div class="btn_wrapC btn2ea">
				<a href="#none" class="btn_mStyle1 sPurple1" onclick="javascript:closePop('${search.cookieKey}', '${search.popupNo}')">오늘 다시 보지 않기</a>
				<a href="#none" class="btn_mStyle1 sPurple1" onclick="javascript:closePop2('${search.popupNo}')">닫기</a>
			</div>
		</div>
	</div>
</div>
