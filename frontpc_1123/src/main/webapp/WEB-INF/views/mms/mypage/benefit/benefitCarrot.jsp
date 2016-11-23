<%--
	화면명 : 마이페이지 > 나의혜택 > 당근
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- <script type="text/javascript" src="/resources/js/jquery-ui.min.js"></script> -->
<script type="text/javascript" src="/resources/js/date.js"></script>
	
<script type="text/javascript">
$(document).ready(function(){
	mypage.carrot.search();
	
	//달력 초기화
	initCal("startDate","endDate");
});

if(ccs.common.mobilecheck()) {
	/* 모바일 스크롤 제어*/
	$(window).bind("scroll", productListScrollListener);
	
	function productListScrollListener() {
		
		var rowCount = $(".div_tb_tbody2").find("li").length;
		var totalCount = Number($("[name=totalCount]").val());
		var maxPage = Math.ceil(totalCount/10);
		
		var scrollTop = $(window, document).scrollTop();
		var scrollHeight = $(document).height() - $(window).height();
		
		if (scrollTop >= scrollHeight - 200 && scrollHeight != 0) {
			if(rowCount !=0 && (rowCount < totalCount)){
				
				if ($("#lodingBar").length > 0 ) {
					return;
				}
				mypage.carrot.search(null, null, true); 
			}
		}
	}

	function setEvent() {
		if (!$(".div_tb_tbody2").eq(0).hasClass("block")) {
			$(".div_tb_tbody2").each(function(index) {
				
				if (index != 0) {
					$(this).toggleClass("block");
				}
			});
		}
	}
}

</script>
<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY혜택관리|당근" name="pageNavi"/>
</jsp:include>
<input type="hidden" name="currentPage" value="1" />
<div class="inner">
<div class="layout_type1">
<div class="column">
	<div class="mypage carrot">
		<h3 class="title_type1">당근</h3>
		<div class="borderBox">
			<dl>
				<dt>사용가능 당근</dt>
				<dd>
					<strong><fmt:formatNumber value="${carrotInfo.balanceAmt + 0 }" pattern="###,###" /></strong>
					<span>개</span>
				</dd>
			</dl>
			<p><span>이번달 소멸예정 당근 <em><fmt:formatNumber value="${carrotInfo.endCarrot + 0 }" pattern="###,###" /></em>개</span></p>
		</div>
		
		<div class="periodBox">
			<strong>조회기간</strong>
			<ul class="periodList"></ul>
			<div class="calendarBox">
				<div class="btnR"><a href="#" class="btn_x btn_close">닫기</a></div>
			 	<span class="inpCalendar">
			 		<input type="text" id="startDate" period-set day="0" callback="mypage.carrot.search"/>
			 	</span>
			 	<span class="swung">~</span>
			 	<span class="inpCalendar">
			 		<input type="text" id="endDate" period-set />
			 	</span>
			</div>
			<a href="javascript:void(0);" class="btn_sStyle4 sPurple1 btnInquiry" onclick="javascript:mypage.carrot.search()">조회</a>
		</div>

		<div class="sortPkg">
			<ul class="sortBox">
				<li class="active" id="" onclick="javascript:mypage.carrot.search(this)">
					<a href="javascript:void(0);" ><span>전체</span><em id="totalCnt"></em></a>
				</li>
				<li id="PLUS" onclick="javascript:mypage.carrot.search(this,'PLUS')">
					<a href="javascript:void(0);" ><span>적립</span><em id="item1Cnt"></em></a>
				</li>
				<li id="MINUS" onclick="javascript:mypage.carrot.search(this,'MINUS')">
					<a href="javascript:void(0);" ><span>차감</span><em id="item2Cnt"></em></a>
				</li>
			</ul>
		</div>

		<%-- carrot list ajax 영역 --%>
		<div class="tbl_article">
			<!-- ### 테이블 헤더 ### -->
			<div class="div_tb_thead2">
				<div class="tr_box">
					<span class="col1">날짜</span>
					<span class="col2">사유</span>
					<span class="col3">적립 / 차감</span>
					<span class="col4">유효기간</span>
				</div>
			</div>
			<!-- ### //테이블 헤더 ### -->
			
			<ul class="div_tb_tbody2">
				<c:if test="${!isMobile }">
					<img src="/resources/img/mobile/Loading.gif" alt="" 
					style="display: block; margin-left: auto; margin-right: auto; padding-top: 170px; padding-bottom: 170px;"/>
				</c:if>
			</ul>
			
		</div>
		<%-- //carrot list ajax 영역 --%>
		
		<div class="bnrArea"><a href="javascript:void(0);"><img src="/resources/img/pc/temp/bnr_carrot.jpg" alt="당근 사용하기" /></a></div>
		
		<ul class="notice">
			<li>당근이란 제로투세븐닷컴에서 제공하는 비현금성 마일리지로 상품평 작성, 이메일 수신, 이벤트 참여 등의 활동을 통해 적립되며 쿠폰 교환 및 이벤트 응모에 사용하실 수 있습니다.</li>
			<li>당근은 적립일로부터 6개월간 유효하며, 기간 종료시 자동소멸 됩니다.</li>
		</ul>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
</div>
</div>