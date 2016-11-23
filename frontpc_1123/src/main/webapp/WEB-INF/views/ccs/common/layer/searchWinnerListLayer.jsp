<%--
	화면명 : 당첨자 목록 검색 레이어
	작성자 : roy
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<script type="text/javascript" src="/resources/js/date.js"></script>
<script type="text/javascript">
	
</script>

<div class="pop_wrap ly_winner" id="searchWinnerListLayer">
	<input type="hidden" id="eventId" value="${search.eventId }"/>
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">당첨자 명단</h3>
		</div>
		<div class="pop_content">
			<div class="srchList">
				<div>
					<div class="inputTxt_place1">
						<label>ID를 입력하세요</label> <span> 
						<input type="text" id="searchKeyword" value="" />
						</span>
					</div>
					<a href="#none" class="btn_sStyle4 sPurple1 btnInquiry" onclick="javascript:custcenter.event.search();">조회</a>
				</div>
			</div>
			
			<div id="winnerListResult" class="winnerList">
							
			</div>
			
			<div class="btn_wrapC">
				<a href="#none" class="btn_mStyle1 sPurple1" onclick="javascript:close_popup();">확인</a>
			</div>
		</div>
		<button type="button" id="closeBtn" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>
