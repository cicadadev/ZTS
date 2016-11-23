<%--
	화면명 : 마이페이지 > 나의활동 > 이벤트 참여내역
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="/resources/js/date.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	mypage.event.search();
	//달력 초기화
	initCal("startDate","endDate");
});
</script>

<div>
	<input type="hidden" id="total" value="${joinCntInfo.totalCount}">
	<input type="hidden" id="item1" value="${joinCntInfo.runEventCount + 0 }">
	<input type="hidden" id="item2" value="${joinCntInfo.stopEventCount + 0 }">
</div>


<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY활동관리|이벤트 참여내역" name="pageNavi"/>
</jsp:include>
<div class="inner">
<div class="layout_type1">
<div class="column">
	<div class="mypage myevent">
		<h3 class="title_type1">이벤트 참여내역</h3>
		
		<div class="periodBox">
			<strong>조회기간</strong>
			<ul class="periodList"></ul>
			<div class="calendarBox">
				<div class="btnR"><a href="#" class="btn_x btn_close">닫기</a></div>
				<span class="inpCalendar">
					<input type="text" id="startDate" period-set day="0" />
				</span>
				<span class="swung">~</span>
				<span class="inpCalendar">
					<input type="text" id="endDate" period-set />
				</span>
			</div>
			<a href="#" class="btn_sStyle4 sPurple1 btnInquiry">조회</a>
		</div>
	
		<div class="sortPkg">
			<ul class="sortBox">
				<li class="active" onclick="javascript:mypage.event.search(this)">
					<a href="#none"><span>전체</span><em id="totalCnt"></em></a>
				</li>
				<li onclick="javascript:mypage.event.search(this,'RUN')">
					<a href="#none"><span>진행중</span><em id="item1Cnt"></em></a>
				</li>
				<li onclick="javascript:mypage.event.search(this,'STOP')">
					<a href="#none"><span>종료</span><em id="item2Cnt"</em></a>
				</li>
			</ul>
			<a href="#none" class="btn_sStyle1 sPurple1 posR" onclick="custcenter.event.searchWinnerList();">당첨자 발표보기</a>
		</div>
	
		<div class="tbl_article">
			<div class="div_tb_thead2">
				<div class="tr_box">
					<span class="col1">발표여부</span>
					<span class="col2">이벤트명</span>
					<span class="col3">이벤트 기간</span>
					<span class="col4">당첨여부</span>
				</div>
			</div>

			<div id="eventJoinHistoryDiv">
			
			</div>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
</div>
</div>
