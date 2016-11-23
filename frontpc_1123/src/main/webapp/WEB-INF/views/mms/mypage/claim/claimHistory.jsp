<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags" %>
<script type="text/javascript" src="/resources/js/date.js"></script>
<script type="text/javascript">
$(document).ready(function(){
// 	mypage.deposit.search();
	//달력 초기화
	initCal("startDate","endDate");
});
</script>
<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY주문관리|취소/반품/교환 조회" name="pageNavi"/>
</jsp:include>
<div class="inner">
<div class="layout_type1">
<div class="column">
	<div class="myorder myorderSrch">
		<form method="post" name="orderSearch">
			<h3 class="title_type1">취소/반품/교환 조회</h3>
			<div class="periodBox">
				<div class="group group_date">
					<strong>조회기간</strong>
					<ul class="periodList"></ul>
					<div class="calendarBox">
						<div class="btnR">
							<a href="javascript:void(0);" class="btn_x btn_close">닫기</a>
						</div>
						<span class="inpCalendar">
							<input type="text" id="startDate" name="startDate" period-set day="0" />
						</span>
						<span class="swung">~</span>
						<span class="inpCalendar">
							<input type="text" id="endDate" name="endDate" period-set />
						</span>
					</div>
				</div>
				<div class="group group_prod">
					<strong>상품명</strong>
					<input type="hidden" name="productType" value="name"/>
					<input type="text" name="product" class="inputTxt_style1" />
					<a href="javascript:void(0);" onclick="$order.search.list($(this));" class="btn_sStyle4 sPurple1 btnInquiry">조회</a>
				</div>
			</div>
			<ul class="tabBox tab3ea tp1">
				<li onclick="$order.onTab($(this));" data-tab="general" data-menu="claim">
					<a href="javascript:void(0);">일반구매</a>
				</li>
				<li onclick="$order.onTab($(this));" data-tab="regular" data-menu="claim">
					<a href="javascript:void(0);">정기배송</a>
				</li>
				<li onclick="$order.onTab($(this));" data-tab="pickup" data-menu="claim">
					<input name="area1" type="hidden">
					<input name="area2" type="hidden">
					<a href="javascript:void(0);">매장픽업</a>
				</li>
			</ul>
		</form>
		<%-- contents layer --%>
		<span id="innerTab"></span>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
</div>
</div>
<script type="text/javascript">
<!--
	// 최초 로딩시만 실행.
	$(function() {
		$('.myorder').find('.tabBox li:eq(${tabIdx})').trigger("click");
		$('.lnb ul:eq(0) li:eq(1)').addClass('on');
	});
//-->
</script>
