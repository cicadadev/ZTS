<%--
	화면명 : 마이페이지 > 나의 활동 > 내가 문의한 글
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="/resources/js/date.js"></script>

<script type="text/javascript">
$(document).ready(function() {
	mypage.inquiry.search();
	//달력 초기화
	initCal("startDate","endDate");
});

if(ccs.common.mobilecheck()) {
	/* 모바일 스크롤 제어*/
	$(window).bind("scroll", productListScrollListener);

	function target() {
		var here = "";
		var pageType = $('#pageType').val();
		if (pageType == "MYQA") {
			here = ".tbl_article .div_tb_tbody3";
		} else if (pageType == "PRODUCT") {
			here = ".tbl_qa .div_tb_tbody3";
		}
		return here;		
	}
	
	function productListScrollListener() {
		var here = target();
		var rowCount = $(here).children("li").length;
		var totalCount = Number($("[name=totalCount]").val());
		var maxPage = Math.ceil(totalCount/10);
		
		var scrollTop = $(window, document).scrollTop();
		var scrollHeight = $(document).height() - $(window).height();
		
		if (scrollTop >= scrollHeight - 200 && scrollHeight != 0) {
			if(rowCount !=0 && (rowCount < totalCount)){
				
				if ($("#lodingBar").length > 0 ) {
					return;
				}
				mypage.inquiry.search(null, null, true); 
			}
		}
	}

	function setEvent() {
		var here = target();
		if (!$(here).eq(0).hasClass("block")) {
			$(here).each(function(index) {
				
				if (index != 0) {
					$(this).toggleClass("block");
				}
			});
		}
	}
}
</script>

<input type="hidden" value="${pageType }" id="pageType" />
<input type="hidden" name="currentPage" value="1" />

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY활동관리|나의 문의" name="pageNavi"/>
</jsp:include>
<div class="inner">
<div class="layout_type1">
<div class="column">
	<div class="mypage myqa">
		<h3 class="title_type1">나의 문의</h3>
		<div>
			<ul class="tabBox tp1" id="inquiryTab">
				<li ><a href="javascript:void(0);" onclick="javacript:mypage.inquiry.tab('MYQA')">1:1 문의</a></li>
				<li ><a href="javascript:void(0);" onclick="javacript:mypage.inquiry.tab('PRODUCT')">상품 Q&amp;A</a></li>
			</ul>
			
			<div class="tab_con">
				<div class="periodBox">
					<strong>조회기간</strong>
					<ul class="periodList"></ul>
					<div class="calendarBox">
						<div class="btnR"><a href="javascript:void(0);" class="btn_x btn_close">닫기</a></div>
					 	<span class="inpCalendar">
					 		<input type="text" id="startDate" period-set day="0" />
					 	</span>
					 	<span class="swung">~</span>
					 	<span class="inpCalendar">
					 		<input type="text" id="endDate" period-set />
					 		<div></div>
					 	</span>
					</div>
					<a href="javascript:void(0);" class="btn_sStyle4 sPurple1 btnInquiry" onclick="javacript:mypage.inquiry.search()">조회</a>
				</div>

				<div class="sortPkg">
					<ul class="sortBox">
						<li class="active" onclick="javascript:mypage.inquiry.search(this)" >
							<a href="javascript:void(0);" ><span>전체</span><em id="totalCnt"></em></a>
						</li>
						<li onclick="javascript:mypage.inquiry.search(this, 'COMPLETE')" >
							<a href="javascript:void(0);"><span>답변완료</span><em id="item1Cnt"></em></a>
						</li>
			<!-- 			<li id="sortM"> -->
			<%-- 				<a href="javascript:void(0);" onclick="javascript:mypage.inquiry.search('${pageType}','ANSWER')"> --%>
			<!-- 					<span>답변준비중</span> -->
			<%-- 					<em>(${info.answerCount })</em> --%>
			<!-- 				</a> -->
			<!-- 			</li> -->
					</ul>
					
					<a id="qaBtn" href="javascript:void(0);" class="btn_sStyle1 sPurple1 posR" onclick="javascript:ccs.link.custcenter.qna.go();">1:1 문의하기</a>
				</div>
				
				<!-- 문의(1:1 문의, 상품Q&A) list Ajax 영역 -->
				<div class="tbl_article"></div>
				<div class="tbl_qa"></div>
				<!-- // list Ajax 영역 -->
	
				<ul class="notice">
					<li>상품상세페이지에서 등록한 상품문의에 대한 답변을 확인하실 수 있습니다.</li>
					<li>Q&amp;A는 각 상품의 상세페이지에서 작성하실 수 있습니다.</li>
				</ul>
			</div>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
</div>
</div>
