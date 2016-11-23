<%--
	화면명 : 고객센터 > 당첨자발표
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!-- <script type="text/javascript">
	$(document).ready(function(){
		custcenter.setCsLayoutType("csdetail");
	});
</script> -->

<style>

.maxW img {height:100%;width:100%;}

</style>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="/ccs/cs/main" name="url"/>
	<jsp:param value="고객센터|당첨자발표" name="pageNavi"/>
</jsp:include>

<div class="inner">
<div class="layout_type1 csdetail">

<!-- CS 메뉴 -->
<jsp:include page="/WEB-INF/views/gsf/layout/cs/left_cs.jsp" />

<div class="column">
	<div class="csBox">
		<input type="hidden" id="eventId" value="${notice.eventId }"/>
		<h3 class="title_type1">당첨자 발표</h3>
		<div class="listDetailBox">
			<div class="title">
				<c:if test="${not empty notice.eventName}">
					<span class="eventN">[${notice.eventName}]</span>
				</c:if>
					${notice.title}
				<span class="tit_date"></span>
			</div>
			<div class="listDetail">
				<div class="date">
					<span>
						<strong>등록일</strong><em>${notice.insDt}</em>
					</span>
					<span>
						<strong>조회수</strong><em>${notice.readCnt}</em>
					</span>
				</div>
				<div class="cont maxW">
				
				${notice.detail}
				<br />
				</div>
			</div>
		</div>
		<div class="btn_wrapC btn1ea">
			<a href="#none" class="btn_mStyle1 sWhite1" onclick="common.pageMove('${pageScope.id}','','/ccs/cs/event/list')">목록</a>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/cs/left_cs.jsp" />
</div>
</div>
