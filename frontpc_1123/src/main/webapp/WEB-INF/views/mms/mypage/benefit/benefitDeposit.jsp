<%--
	화면명 : 마이페이지 > 나의혜택 > 예치금
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript" src="/resources/js/date.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	mypage.deposit.search();
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
				mypage.deposit.search(null, null, true); 
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
		<jsp:param value="마이쇼핑|MY혜택관리|예치금" name="pageNavi"/>
	</jsp:include>
<input type="hidden" name="currentPage" value="1" />
<div class="inner">
<div class="layout_type1">	
<div class="column">
	<div class="mypage mymoney">
		<h3 class="title_type1">예치금</h3>
		<div class="borderBox">
			<dl>
				<dt>사용가능 예치금</dt>
				<dd>
					<strong id="customerDepositAmt"><fmt:formatNumber value="${depositInfo.balanceAmt + 0}" pattern="###,###" /></strong>
					<span>원</span>
				</dd>
			</dl>
		</div>
		
		<div class="periodBox">
			<strong>조회기간</strong>
			<ul class="periodList"></ul>
			<div class="calendarBox">
				<div class="btnR"><a href="javascript:void(0);" class="btn_x btn_close">닫기</a></div>
			 	<span class="inpCalendar">
			 		<input type="text" id="startDate" period-set day="0" callback="mypage.deposit.search" />
			 	</span>
			 	<span class="swung">~</span>
			 	<span class="inpCalendar">
			 		<input type="text" id="endDate" period-set />
			 	</span>
			</div>
			<a href="javascript:void(0);" class="btn_sStyle4 sPurple1 btnInquiry" onclick="javascript:mypage.deposit.search()">조회</a>
		</div>

		<div class="sortPkg">
			<ul class="sortBox">
				<li class="active" onclick="javascript:mypage.deposit.search(this)" id="">
					<a href="javascript:void(0);" ><span>전체</span><em id="totalCnt"></em></a>
				</li>
				<li onclick="javascript:mypage.deposit.search(this,'PLUS')" id="PLUS">
					<a href="javascript:void(0);" ><span>적립</span><em id="item1Cnt"></em></a>
				</li>
				<li onclick="javascript:mypage.deposit.search(this,'MINUS')" id="MINUS">
					<a href="javascript:void(0);" ><span>차감</span><em id="item2Cnt"></em></a>
				</li>
			</ul>
		</div>

		<%-- deposit list ajax 영역 --%>
		<div class="tbl_article">
			<div class="div_tb_thead2">
				<div class="tr_box">
					<span class="col1">날짜</span>
					<span class="col2">사유</span>
					<span class="col3">적립 / 차감</span>
				</div>
			</div>
			
			<div>
				<ul class="div_tb_tbody2">
					<c:if test="${!isMobile }">
						<img src="/resources/img/mobile/Loading.gif" alt="" 
						style="display: block; margin-left: auto; margin-right: auto; padding-top: 170px; padding-bottom: 170px;"/>
					</c:if>
				</ul>
				<c:if test="${isMobile }">
					<div class="pagePkg">
						<a href="javascript:void(0);" class="btn_sStyle4 sPurple1 btnRefund" onclick="javascript:mypage.deposit.refund();">예치금 환불신청</a>
					</div>
				</c:if>
			</div>
		</div>
		<%-- //deposit list ajax 영역 --%>

		<div id="refundLayer"></div>

		<ul class="notice">
			<li>예치금이란  주문취소, 반품으로 발생한 환불금액을 보관해 두는 서비스로 제로투세븐닷컴에서 상품 구매시 현금과 동일하게 사용됩니다.</li>
			<li>예치금은 현금 환불신청 가능하며, 본인명의계좌로 신청가능 합니다.</li>
			<li>환불 신청시 즉시 사용 처리 되고, 익일 오후 입금 됩니다.(단, 주말/공휴일 제외)</li>
			<li>환불신청 철회는 고객센터 1588-8744로 문의 주시기 바랍니다.</li>
		</ul>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
</div>
</div>

