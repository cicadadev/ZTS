<%--
	화면명 : 고객센터 > 공지사항
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript">
	
	$(document).ready(function(){
		if (common.isEmpty("${brandId}")) {
			custcenter.notice.listCall("/ccs/cs/notice/list/ajax");
			//custcenter.setCsLayoutType("csnotice");
		} else {
			custcenter.notice.listCall("/ccs/cs/brand/notice/list/ajax?brandId=${brandId}");
		}
	});
	
	if(ccs.common.mobilecheck()) {
		/* 모바일 스크롤 제어*/
		$(window).bind("scroll", noticeListScrollListener);
		
		function noticeListScrollListener() {
			
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
					if (common.isEmpty("${brandId}")) {
						custcenter.notice.listCall("/ccs/cs/notice/list/ajax", true);
						//custcenter.setCsLayoutType("csnotice");
					} else {
						custcenter.notice.listCall("/ccs/cs/brand/notice/list/ajax?brandId=${brandId}", true);
					}
				}
			}
		}
	}
	
</script>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="/ccs/cs/main" name="url"/>
	<jsp:param value="고객센터|공지사항" name="pageNavi"/>
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
		<h3 class="title_type1">공지사항</h3>

		<div class="srchList">
			<div>
				<div class="select_box1">
					<label></label>
					<select id="typeSelect">
						<option value=""  selected="selected">전체</option>
						<option value="TITLE">제목</option>
						<option value="DETAIL">내용</option>
					</select>
				</div>
				<input id="searchWord" type="text" class="inputTxt_style1" onkeyup="javascript:custcenter.notice.searchKeyUp(this);">
				<a href="javascript:custcenter.notice.listCall('/ccs/cs/notice/list/ajax');"  class="btn_sStyle4 sPurple1 btnInquiry">조회</a>
			</div>
		</div>
				
		<span id="noticeDiv"></span>
		<%-- ajax 영역 --%>
				
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/cs/left_cs.jsp" />
</div>
</div>