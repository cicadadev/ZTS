<%--
	화면명 : 마이페이지 > 나의혜택 > AS
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!-- <script type="text/javascript" src="/resources/js/jquery-ui.min.js"></script> -->
<script type="text/javascript" src="/resources/js/date.js"></script>
	
<script type="text/javascript">
$(document).ready(function(){
	mypage.as.search();
	
	//달력 초기화
	initCal("startDate","endDate");
});

if(ccs.common.mobilecheck()) {
	/* 모바일 스크롤 제어*/
	$(window).bind("scroll", productListScrollListener);
	
	function productListScrollListener() {
		
		var rowCount = $(".div_tb_tbody3").find("li").length;
		var totalCount = Number($("[name=total]").val());
		var maxPage = Math.ceil(totalCount/10);
		
		var scrollTop = $(window, document).scrollTop();
		var scrollHeight = $(document).height() - $(window).height();
		
		if (scrollTop >= scrollHeight - 200 && scrollHeight != 0) {
			if(rowCount !=0 && (rowCount < totalCount)){
				
				if ($("#lodingBar").length > 0 ) {
					return;
				}
				mypage.as.search(true); 
			}
		}
	}

	function setEvent() {
		if (!$(".div_tb_tbody3").eq(0).hasClass("block")) {
			$(".div_tb_tbody3").each(function(index) {
				
				if (index != 0) {
					$(this).toggleClass("block");
				}
			});
		}
	}
}

</script>
<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY활동관리|의류AS현황" name="pageNavi"/>
</jsp:include>
<input type="hidden" name="currentPage" value="1" />
<div class="inner">
	<div class="layout_type1">
		<div class="column">
			<div class="mypage">
				<h3 class="title_type1">의류AS현황</h3>

		<div class="periodBox">
			<strong>조회기간</strong>
			<ul class="periodList"></ul>
			<div class="calendarBox">
				<div class="btnR"><a href="#" class="btn_x btn_close">닫기</a></div>
			 	<span class="inpCalendar">
			 		<input type="text" id="startDate" period-set day="0" callback="mypage.as.search"/>
			 	</span>
			 	<span class="swung">~</span>
			 	<span class="inpCalendar">
			 		<input type="text" id="endDate" period-set />
			 	</span>
			</div>
			<a href="javascript:void(0);" class="btn_sStyle4 sPurple1 btnInquiry" onclick="javascript:mypage.as.search()">조회</a>
		</div>

		<div class="sortPkg">
			<ul class="sortBox">
				<li class="active" id="" >
					<a href="javascript:void(0);" ><span>전체</span><em id="totalCnt"></em></a>
				</li>
			</ul>
		</div>

				<div class="tbl_gift">
					<ul class="div_tb_tbody3">

					</ul>
				</div>

				<ul class="notice">
					<li>수선기간은 1주~2주 정도 소요되며 부자재 공급을 받아야 하는 경우 더 소요될 수 있습니다.</li>
					<li>수선이 완료된 수선품은 2~3일 후에 매장에 도착하며 매장에서 안내문자를 발송해 드릴 예정입니다.</li>
					<li>유모차, 카시트, 보행기의 AS는 본사를 거치지 않고 수선업체에서 직배되므로 조회된 내용과 상이할 수 있습니다.</li>
					<li>유상수선의 경우 수선비는 현금결제만 가능하며 현금영수증 발행은 되지 않습니다.</li>
				</ul>
			</div>
		</div>
		<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
	</div>
</div>