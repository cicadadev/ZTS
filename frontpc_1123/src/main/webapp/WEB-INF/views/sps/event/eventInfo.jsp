<%--
	화면명 : 이벤트 상세 - 안내이벤트
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<script type="text/javascript">
$(document).ready(function() {
});

//응모하기
function eventJoin(){
	sps.event.saveJoin('${event.eventId}');
};
</script>

<c:choose>
	<c:when test="${isMobile eq 'true'}">
		<div class="mo_navi">
			<button type="button" class="btn_navi_prev" onclick="parent.history.back();">이전 페이지로..</button>
			<h2>이벤트 상세</h2>
		</div>
	</c:when>
	<c:otherwise>
		<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
			<jsp:param value="이벤트 상세" name="pageNavi"/>
		</jsp:include>
	</c:otherwise>
</c:choose>

<div class="inner">
	<div class="event_cont">
		<c:choose>
			<c:when test="${isMobile eq 'true'}">
				${event.html2}
			</c:when>
			<c:otherwise>
				${event.html1}
			</c:otherwise>
		</c:choose>
	
	</div>
</div>