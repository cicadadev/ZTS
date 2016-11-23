<%--
	화면명 : 고객센터 > 당첨자 리스트
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript">
	
	$(document).ready(function(){
		custcenter.notice.listCall("/ccs/cs/event/notice/list/ajax");
		/* custcenter.setCsLayoutType("csnotice"); */
	});
	
	if(ccs.common.mobilecheck()) {
		/* 모바일 스크롤 제어*/
		$(window).bind("scroll", eventListScrollListener);
		
		function eventListScrollListener() {
			
			var rowCount = $(".div_tb_tbody4").find("li").length;
			var totalCount = Number($("[name=totalCount]").val());
			var maxPage = Math.ceil(totalCount/10);
			
			var scrollTop = $(window, document).scrollTop();
			var scrollHeight = $(document).height() - $(window).height();
			
			if (scrollTop >= scrollHeight - 200 && scrollHeight != 0) {
				if(rowCount !=0 && (rowCount < totalCount)){
					
					if ($("#tempLoadingBar").length > 0 ) {
						return;
					}
					custcenter.notice.listCall("/ccs/cs/event/list/ajax", true);
				}
			}
		}
	}
</script>


<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="/ccs/cs/main" name="url"/>
	<jsp:param value="고객센터|당첨자발표" name="pageNavi"/>
</jsp:include>

<div class="inner">
<div class="layout_type1 csnotice">
<div class="column">
	<form action="/ccs/cs/notice" name="noticeForm" id="noticeForm"	method="post">
		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" /> 
		<input type="hidden" name="word" id="word" value="" /> 
		<input type="hidden" name="type" id="type" value="" />
		<input type="hidden" name="currentPage" value="1" />
	</form>
	<div class="csBox">
		<h3 class="title_type1">당첨자 발표</h3>

		<div class="srchList">
			<div>
				<div class="select_box1">
					<label></label>
					<select id="typeSelect">
						<option value=""  selected="selected">전체</option>
						<option value="EVENT_TARGET_DIV_CD.GENERAL">이벤트</option>
						<option value="EVENT_TARGET_DIV_CD.EXP">생생테스터</option>
					</select>
				</div>
				<input id="searchWord" type="text" class="inputTxt_style1" onkeyup="javascript:custcenter.event.searchKeyUp(this);">
				<a href="#" onclick="javascript:custcenter.notice.listCall('/ccs/cs/event/notice/list/ajax');" class="btn_sStyle4 sPurple1 btnInquiry">조회</a>
			</div>
		</div>
				
		<span id="noticeDiv"></span>
		<%-- ajax 영역 --%>
				
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/cs/left_cs.jsp" />
</div>
</div>