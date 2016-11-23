<%--
	화면명 : 고객센터 > FAQ
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>

<script type="text/javascript">
	
	$(document).ready(function(){
		// 검색어로 링크 연결시 검색어 설정
		if($("#faqForm").find("#info").val() != null && $("#faqForm").find("#info").val() != ''){
			custcenter.faq.select_word($("#faqForm").find("#info").val());
		}else{
			custcenter.faq.listCall("/ccs/cs/faq/list/ajax");
		}
		//custcenter.setCsLayoutType("csfaq");
		
		// FAQ TYPE 링크 연결시에 선택된 TYPE 버튼 활성화( 메인, 좌측메뉴 )
		if($("#faqForm").find("#faqTypeCd").val() != null && $("#faqForm").find("#faqTypeCd").val() != ''){
			var typeTag = '#' + $("#faqForm").find("#faqTypeCd").val();
			var str = typeTag.replace("FAQ_TYPE_CD.", "faqTypeCd_");
			$(str).click();
		}
	});
	
	// 메인에서 클릭한 FAQ 내용 활성화
	var openFaqNo = function(){
		if($("#faqForm").find("#faqNo").val() != null && $("#faqForm").find("#faqNo").val() != ''){
			var typeTag = '#faqNo' + $("#faqForm").find("#faqNo").val();
			$(typeTag).click();
		}
	}
	
	// Qna ajax 리스트 콜백
	var listCallback = function(html) {
		$("#faqDiv").html(html);
		$('#faqDiv tr').on("click", function(e) {
			if ($(this).next(".tr_cont").hasClass("tr_cont_hide")) {
				$(this).siblings(".tr_cont").addClass("tr_cont_hide");
				$(this).next(".tr_cont").removeClass("tr_cont_hide");
			} else {
				$(this).next(".tr_cont").addClass("tr_cont_hide");
			}
			//e.preventDefault();
		});
	}
	
	if(ccs.common.mobilecheck()) {
		/* 모바일 스크롤 제어*/
		$(window).bind("scroll", faqListScrollListener);
		
		function faqListScrollListener() {
			
			var rowCount = $(".div_tb_tbody4").children("li").length;
			var totalCount = Number($("[name=totalCount]").val());
			var maxPage = Math.ceil(totalCount/10);
			
			var scrollTop = $(window, document).scrollTop();
			var scrollHeight = $(document).height() - $(window).height();
			
			if (scrollTop >= scrollHeight - 200 && scrollHeight != 0) {
				if(rowCount !=0 && (rowCount < totalCount)){
					
					if ($("#tempLoadingBar").length > 0 ) {
						return;
					}
					custcenter.faq.listCall("/ccs/cs/faq/list/ajax", true);
				}
			}
		}
	}
</script>
			
			
<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="/ccs/cs/main" name="url"/>
	<jsp:param value="고객센터|FAQ" name="pageNavi"/>
</jsp:include>

<div class="inner">
<div class="layout_type1 csfaq">
<div class="column">
	<form name="faqForm" id="faqForm">
		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }" /> 
		<input type="hidden" name="info" id="info" value="${search.info}" /> 
		<input type="hidden" name="faqTypeCd" id="faqTypeCd" value="${search.faqTypeCd}" />
		<input type="hidden" name="faqNo" id="faqNo" value="${search.faqNos}" />
		<input type="hidden" name="currentPage" value="1" />
	</form>
	<div class="csBox">
		<h3 class="title_type1">FAQ</h3>
		<div class="borderBox">
			<dl class="csSearch">
				<dt>FAQ 검색</dt>
				<dd>
					<div class="search_box">
						<input type="text" id="searchKeyword" class="inp_search" onkeyup="javascript:custcenter.faq.searchKeyUp(this);"> 
						<input type="button" value="검색" class="btn_search" onclick="javascript:custcenter.faq.search_word();">
					</div>
					<ul class="keywordList">
						<li><a href="#" onclick="javascript:custcenter.faq.select_word('SEARCH.ORDER');">주문</a></li>
						<li><a href="#" onclick="javascript:custcenter.faq.select_word('SEARCH.DELIVERY');">배송</a></li>
						<li><a href="#" onclick="javascript:custcenter.faq.select_word('SEARCH.CANCLE');">취소</a></li>
						<li><a href="#" onclick="javascript:custcenter.faq.select_word('SEARCH.REFUND');">반품</a></li>
						<li><a href="#" onclick="javascript:custcenter.faq.select_word('SEARCH.EXCHANGE');">교환</a></li>
						<li><a href="#" onclick="javascript:custcenter.faq.select_word('SEARCH.RETURN');">환불</a></li>
					</ul>
				</dd>
			</dl>
		</div>
		<div class="faqCateBox">
			<ul>
				<li class="on"><a href="#" id="faqTypeCd_ALL" onclick="javascript:custcenter.faq.select_type();">전체</a></li>
				<li><a href="#" id="faqTypeCd_PRODUCT" onclick="javascript:custcenter.faq.select_type('FAQ_TYPE_CD.PRODUCT');">상품</a></li>
				<li><a href="#" id="faqTypeCd_BRAND"   onclick="javascript:custcenter.faq.select_type('FAQ_TYPE_CD.BRAND');">브랜드</a></li>
				<li><a href="#" id="faqTypeCd_SERVICE" onclick="javascript:custcenter.faq.select_type('FAQ_TYPE_CD.SERVICE');">서비스</a></li>
				<li><a href="#" id="faqTypeCd_ORDER"   onclick="javascript:custcenter.faq.select_type('FAQ_TYPE_CD.ORDER');">주문/결제/취소</a></li>
				<li><a href="#" id="faqTypeCd_DELIVERY" onclick="javascript:custcenter.faq.select_type('FAQ_TYPE_CD.DELIVERY');">배송</a></li>
				<li><a href="#" id="faqTypeCd_AS"  onclick="javascript:custcenter.faq.select_type('FAQ_TYPE_CD.AS');">교환/반품/환불/AS</a></li>
				<li><a href="#" id="faqTypeCd_COUPON"  onclick="javascript:custcenter.faq.select_type('FAQ_TYPE_CD.COUPON');">쿠폰/포인트/혜택</a></li>
				<li><a href="#" id="faqTypeCd_MEMBER"  onclick="javascript:custcenter.faq.select_type('FAQ_TYPE_CD.MEMBER');">회원정보/기타</a></li>
			</ul>
		</div>
		
		<span id="faqDiv"></span>
		<%-- ajax 영역 --%>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/cs/left_cs.jsp" />
</div>
</div>