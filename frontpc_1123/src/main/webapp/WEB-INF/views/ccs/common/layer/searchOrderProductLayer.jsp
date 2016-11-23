<%--
	화면명 : 주문상품 검색 레이어
	작성자 : roy
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<script type="text/javascript" src="/resources/js/date.js"></script>
<script type="text/javascript">
	//달력 초기화
	initCal("startDate","endDate");
</script>

<!-- ### 주문상품 조회 팝업 ### -->
<div class="pop_wrap ly_order_search" id="searchOrderProductLayer">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">주문상품 조회</h3>
		</div>
		<div class="pop_content">
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
				<a href="javascript:ccs.layer.orderProductLayer.search();" class="btn_sStyle4 sPurple1 btnInquiry" >조회</a>
			</div>
			
			<div id="orderProductResult" class="tbl_gift">
							
			</div>
		</div>
		<button type="button" class="btn_x pc_btn_close" id="orderPopupClose">닫기</button>
	</div>
</div>
<!-- ### //주문상품 조회 팝업 ### -->
