<%--
	화면명 : 마이페이지 > MY 정보관리 > 영수증 조회
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%-- <%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%> --%>
<script type="text/javascript" src="/resources/js/date.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	mypage.receipt.search();
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
				mypage.receipt.search(true); 
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
	<jsp:param value="마이쇼핑|MY정보관리|영수증조회" name="pageNavi"/>
</jsp:include>

<input type="hidden" name="currentPage" value="1" />

<div class="inner">
<div class="layout_type1">

<div class="column">
	<div class="mypage myreceipt">
		<h3 class="title_type1">영수증조회</h3>
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
			<a href="javascript:void(0);" class="btn_sStyle4 sPurple1 btnInquiry" onclick="javascript:mypage.receipt.search()">조회</a>
		</div>
		<div class="periodSrchBox">
			<strong>주문번호</strong>
			<div class="inputTxt_place1">
				<label class="mo_only">주문번호</label>
				<span>
					<input type="text" id="orderId" value="" />
				</span>
			</div>
		</div>

		<div class="tbl_receipt">
			<ul class="sortBox">
				<li class="active">
					<a href="javascript:void(0);">
						<span>전체</span><em id="totalCnt"></em>
					</a>
				</li>
			</ul>
			
			<div class="div_tb_thead3">
				<div class="tr_box">
					<span class="col1">주문일</span>
			
					<span class="col2">주문번호</span>
			
					<span class="col3">상품명</span>
			
					<span class="col4">금액</span>
			
					<span class="col5">결제수단</span>
			
					<span class="col6">영수증 출력</span>
				</div>
			</div>
			
			<div>
				<ul class="div_tb_tbody3">
					<c:if test="${!isMobile }">
						<img src="/resources/img/mobile/Loading.gif" alt="" 
						style="display: block; margin-left: auto; margin-right: auto; padding-top: 170px; padding-bottom: 170px;"/>
					</c:if>
				</ul>
			</div>
		</div>

		<ul class="notice">
			<li>현금영수증 신청은 주문한 상품의 배송상태가 발송완료일 경우 신청이 가능합니다.</li>
			<li>현금영수증 자진발급 건에 대해 현금영수증 신청을 원하실 경우, 국세청 홈페이지에 가셔서 가맹점 사업자번호, 금액, 승인번호, 거리일자를 등록하셔야 합니다.</li>
		</ul>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
</div>
</div>