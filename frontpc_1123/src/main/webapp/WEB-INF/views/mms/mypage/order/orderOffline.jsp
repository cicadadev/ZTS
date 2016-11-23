<%--
	화면명 : 마이페이지 > 나의혜택 > 멤버쉽관 고객인증
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="/resources/js/date.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	var regionListData = "${regionList}";
	if (regionListData != "[]") {
		$("#TOT_CNT").text("(" + "${regionList[0].totalCount}" + ")");
		$("#TOT_AMT").html("${regionList[0].totalAmt}" + "<i>원</i>");
	} else {
		$("#TOT_CNT").text("(0)");
		$("#accruePrice").css("display", "none");
		
	}
	
	mypage.offlineOrder.getOffOrderList();

	//달력 초기화
	initCal("startDate","endDate");
	
});
</script>
<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY주문관리|오프라인 구매내역" name="pageNavi"/>
</jsp:include>
<div class="inner">
<div class="layout_type1">
<div class="column">
	<div class="mypage myoffline">
		<h3 class="title_type1">오프라인 구매내역</h3>

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
			<a href="javascript:void(0);" class="btn_sStyle4 sPurple1 btnInquiry" onclick="javascript:mypage.offlineOrder.getOffOrderSearchList()">조회</a>
		</div>
		

		<div class="positionBox">
			<div class="select_box1">
				<label></label>
				<select onChange="mypage.offlineOrder.changeRegion(this);">
					<option>지역</option>
					<c:forEach items="${regionList}" var="region">
						<option>${region.areaDiv1}</option>
					</c:forEach>
				</select>
			</div>
	
			<div class="select_box1" >
				<label id="branchLabel"></label>
				<select id="selBranch" onchange="mypage.offlineOrder.changeBranch(this);">
					<option>지점</option>
				</select>
			</div>
		</div>

		<div class="sortPkg">
			<ul class="sortBox">
				<li class="active">
					<a href="#none">
						<span>전체</span><em id="TOT_CNT"></em>
					</a>
				</li>
			</ul>
			<div class="accrue posR" id="accruePrice">
				구매누적금액
				<span class="price">
					<em id="TOT_AMT"></em>
				</span>
			</div>
		</div>

		<div class="tbl_article" id="offOrderListDiv">
			
	
			
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
</div>
</div>