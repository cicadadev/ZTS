<%--
	화면명 : 공지사항 팝업
	작성자 : roy
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<script type="text/javascript">
	document.title = '${popup.title}';

	function setCookie(name, value, expiredays){
		//console.log("set", name);
		var todayDate = new Date();
		todayDate.setDate(todayDate.getDate() + expiredays);
		document.cookie = name + '=' + escape( value ) + '; path=/; expires=' + todayDate.toGMTString() + ';';
	};
	
	function closePop(param, param1){
		if(document.getElementById("noticePopupCheck").checked){
			setCookie(param, param1, 1);
		}
		window.close();
	};
</script>

		${popup.detail1}
<p align="left"> 
	<span style="font-size:9pt;">&nbsp;<input type="checkbox" id="noticePopupCheck" name="Notice">오늘 하루 이 창을 열지 않음 
	</span>
	<a href="javascript:closePop('${cookieKey}', 'done')">
	<span style="font-size:9pt; border=" 0">[닫기]
	</span>
	</a> 
</p>
