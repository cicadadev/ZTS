<%--
	화면명 : 선물함
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script type="text/javascript" src="/resources/js/date.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	mypage.gifticon.getMemberGiftOrderList();
	//달력 초기화
	initCal("startDate","endDate");
});
</script>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY주문관리|선물함 관리" name="pageNavi"/>
</jsp:include>
<div class="inner">
<div class="layout_type1">
<div class="column">
	<div class="mypage mygift">
		<h3 class="title_type1">선물함 관리</h3>

		<div class="giftArea">
			<img src="/resources/img/mobile/bg/gift_main.jpg" alt="선물이 도착했습니다." class="mo_img">
			<p>선물이 도착했습니다.</p>
			<div class="info">
				<dl>
					<dt>미수령 선물</dt>
					<dd>
						<strong>${notReceiveCnt}</strong>개
					</dd>
				</dl>
				<span class="slash">/</span>
				<dl>
					<dt>받은 선물 총</dt>
					<dd>
						<strong>${giftTotalCnt}</strong>개
					</dd>
				</dl>
			</div>
		</div>

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
			<a href="javascript:mypage.gifticon.getMemberGiftOrderSearchList();" class="btn_sStyle4 sPurple1 btnInquiry">조회</a>
		</div>

		<ul class="notice">
			<li>선물은 메시지를 받은 날로부터 7일간 배송지 입력이 가능하며, 7일이 지나면 주문이 자동 취소됩니다.</li>
		</ul>

		<div class="tbl_gift" id="giftOrderDiv">
			
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
</div>
</div>
<input type="hidden" name="giftPhone" id="giftPhone" value="${search.giftPhone}">

</form>
