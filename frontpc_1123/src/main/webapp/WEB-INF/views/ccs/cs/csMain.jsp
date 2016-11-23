<%--
	화면명 : 고객센터 > 메인
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script type="text/javascript">

/* 	$(document).ready(function(){
		custcenter.setCsLayoutType("csmain");
	}); */
</script>


<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="고객센터" name="pageNavi"/>
</jsp:include>

<div class="inner">
<div class="layout_type1 csmain">

<!-- CS 메뉴 -->
<jsp:include page="/WEB-INF/views/gsf/layout/cs/left_cs.jsp" />
<div class="column">
	<div class="csBox">
		<!-- ### 퀵 메뉴 : 2016.08.29 추가 ### -->
		<div class="quick">
			<strong>Quick Menu</strong>
			<ul>
				<li class="quick1"><a href="/mms/mypage/order/history"">주문/배송 확인</a></li>
				<li class="quick2"><a href="#none" onclick="javascript:ccs.link.mypage.activity.review();">상품평 작성</a></li>
				<li class="quick3"><a href="#none" onclick="javascript:ccs.link.custcenter.qna.go();">1:1 문의</a></li>
				<li class="quick4"><a href="#none" onclick="javascript:ccs.link.mypage.benefit.membership();">회원등급혜택</a></li>
				<li class="quick5"><a href="#none" onclick="javascript:mms.common.nonMemberLoginLayer();">비회원주문조회</a></li>
				<li class="quick6"><a href="#none" onclick="javascript:ccs.link.mypage.activity.inquiry('MYQA');">1:1 답변확인</a></li>
			</ul>
		</div>
		<!-- ### //퀵 메뉴 : 2016.08.29 추가 ### -->
		
		<div class="borderBox">
			<dl class="csSearch">
				<dt>자주하는 질문</dt>
				<dd>
					<div class="search_box">
						<input type="text" id="searchWord" value="" class="inp_search"> 
						<input type="button" value="검색" class="btn_search" onclick="javascript:custcenter.main.faq_search('${pageScope.id}');">
					</div>
					<ul class="keywordList">
						<li><a href="javascript:custcenter.faq.go('', 'SEARCH.ORDER', '');">주문</a></li>
						<li><a href="javascript:custcenter.faq.go('', 'SEARCH.DELIVERY', '');">배송</a></li>
						<li><a href="javascript:custcenter.faq.go('', 'SEARCH.CANCLE', '');">취소</a></li>
						<li><a href="javascript:custcenter.faq.go('', 'SEARCH.REFUND', '');">반품</a></li>
						<li><a href="javascript:custcenter.faq.go('', 'SEARCH.EXCHANGE', '');">교환</a></li>
						<li><a href="javascript:custcenter.faq.go('', 'SEARCH.RETURN', '');">환불</a></li>
					</ul>
				</dd>
			</dl>
		</div>

		<!-- ### FAQ BEST5 ### -->
		<div class="positionR">
			<div class="package">
				<h3 class="sub_tit1">FAQ BEST 5</h3>
			</div>
			<div class="div_tb_thead4">
				<div class="tr_box">
					<span class="col1">번호</span>
					<span class="col2">분류</span>
					<span class="col3">제목</span>
				</div>
			</div>
			<ul class="div_tb_tbody4">
				<c:forEach items="${faq}" var="faq" varStatus="status">
					<li>
						<div class="tr_box">
							<div class="col1">
								<span class="num">${status.index+1}</span>
							</div>
	
							<div class="col2">
								<span class="category">${faq.faqTypeName}</span>
							</div>
	
							<div class="col3">
								<div class="text_indent">
									<c:if test="${isMobile}"> 
										<a href="javascript:custcenter.faq.go('${faq.faqTypeCd}', '', '${faq.faqNo}');" class="name btn_answer mo_noEvent" >								
									</c:if>
									<c:if test="${!isMobile}">
										<a href="javascript:custcenter.faq.go('${faq.faqTypeCd}', '', '${faq.faqNo}');" class="name btn_answer pc_noEvent" > <em></em>
									</c:if>
										<i>
											${faq.title}
										</i>
									</a>
								</div>
							</div>
						</div>
					</li>
				</c:forEach>
			</ul>
			<a href="javascript:ccs.link.go('/ccs/cs/faq/list', false);" class="btn_moreView" >전체보기</a>
		</div>
		<!-- ### //FAQ BEST5 ### -->

		<ul class="tabBox tp1">
			<li class="on"><a href="#none">공지사항</a></li>
			<li><a href="#none">당첨자발표</a></li>
		</ul>

		<!-- ### 공지사항 : pc는 2개, mobile은 4개 ### -->
		<div class="positionR tab_con tab_conOn">
			<div class="package">
				<h3 class="sub_tit1">공지사항</h3>
			</div>
			<ul class="div_tb_tbody4">
				<c:forEach items="${notice}" var="notice">
					<div class="tr_box">
						<div class="col4">
							<div class="text_indent">
								<a href="javascript:custcenter.notice.detail('${notice.noticeNo}', '${notice.readCnt}');" class="name" > 
									<c:if test="${notice.topNoticeYn == 'Y'}">
										<span class="icon_type1">공지</span>
									</c:if>
									<i>
									${notice.title}
									</i>
									<c:if test="${notice.newYn == 'Y'}">
										<img src="/resources/img/pc/ico/ico_new.gif" alt="" />
									</c:if>
								</a>
							</div>
						</div>

						<div class="col5">
							<span class="date">${notice.insDt}</span>
						</div>
					</div>
				</c:forEach>
			</ul>
			<a href="javascript:ccs.link.go('/ccs/cs/notice/list', '1');" class="btn_moreView" >전체보기</a>
		</div>
		<!-- ### //공지사항 ### -->

		<!-- ### 당첨자 발표 : pc는 2개, mobile은 4개 ### -->
		<div class="positionR tab_con ">
			<div class="package">
				<h3 class="sub_tit1">당첨자 발표</h3>
			</div>
			<ul class="div_tb_tbody4">
				<c:forEach items="${event}" var="event">
					<div class="tr_box">
						<div class="col4">
							<div class="text_indent">
								<a href="javascript:custcenter.event.detail('${event.noticeNo}', '${event.readCnt}');" class="name" >
									<c:if test="${event.topNoticeYn == 'Y'}">
										<span class="icon_type1">공지</span>
									</c:if>
									<i>
									${event.title}
									</i>
									<c:if test="${event.newYn == 'Y'}">
										<img src="/resources/img/pc/ico/ico_new.gif" alt="" />
									</c:if>
								</a>
							</div>
						</div>

						<div class="col5">
							<span class="date">${event.insDt}</span>
						</div>
					</div>
				</c:forEach>
			</ul>
			<a href="javascript:ccs.link.go('/ccs/cs/event/list', false);" class="btn_moreView" >전체보기</a>
		</div>
		<!-- ### //당첨자 발표 ### -->
	</div>
</div>
</div>
</div>