<%--
	화면명 : 마이페이지 > 나의활동 > 1:1문의 list
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<div>
	<input type="hidden" id="hPageType" value="${pageType }">

	<input type="hidden" id="total" value="${info.completeCount + info.answerCount }">
	<input type="hidden" id="item1" value="${info.completeCount + 0}">
	<input type="hidden" id="totalCount" name="totalCount" value="${totalCount + 0}">
</div>
	
	<div class="div_tb_thead3">
		<div class="tr_box">
			<span class="col1">답변여부</span>
			<span class="col2">분류</span>
			<span class="col3">제목</span>
			<span class="col4">작성일</span>
		</div>	
	</div>
	
	<div>
		<ul class="div_tb_tbody3" id="myqaUl">
			<c:choose>
				<c:when test="${!empty myQaList}">
					<c:forEach var="list" items="${myQaList }">
						<li>
							<div class="tr_box">
								<div class="col1">
									<%-- 
									문의중 icon_type2 
									답변완료 icon_type2 iconPink3 
									답변중 icon_type2 iconBlue3
									--%>
									<c:if test="${list.inquiryStateCd eq 'INQUIRY_STATE_CD.ACCEPT' }">
										<span class="icon_type2 ">
									</c:if>
									<c:if test="${list.inquiryStateCd eq 'INQUIRY_STATE_CD.ANSWER' }">
										<span class="icon_type2 iconBlue3">
									</c:if>
									<c:if test="${list.inquiryStateCd eq 'INQUIRY_STATE_CD.COMPLETE' }">
										<span class="icon_type2 iconPink3">
									</c:if>
										<tags:codeName code="${ list.inquiryStateCd }"/>
										</span>
								</div>
								
								<div class="col2">
									<div class="title qArea">
										<span class="type">
										<i>[</i>
											<tags:codeName code="${ list.inquiryTypeCd }"/>
										<i>]</i></span>
										<a href="#none">${list.title }</a>
									</div>
								</div>
								<div class="col3">
									<span class="date">${list.insDt }</span>
								</div>
							</div>
							<div class="txt_group aArea">
								<div class="qa_outer">
									<div class="q">
										${list.detail }
									</div>
									<c:if test="${list.inquiryStateCd eq 'INQUIRY_STATE_CD.COMPLETE' }" >
										<div class="a">
											${list.answer }
											<span class="date">${list.answerDt }</span>
										</div>
										<div class="btns">
<!-- 											<a href="#" class="btn_sStyle1 sWhite2">수정</a>	TODO -->
											<a href="#none" class="btn_sStyle1 sWhite2" onclick="javascript:mypage.inquiry.deleteInquiry('MYQA','${list.inquiryNo }')">삭제</a>
										</div>
									</c:if>
								</div>
							</div>
						</li>
					</c:forEach>
				</c:when>
				
				<c:otherwise>
					<li class="noData_tp1">
						최근 상담하신 내역이 없습니다.
					</li>
				</c:otherwise>
			
			</c:choose>

		</ul>
		<!-- ### //테이블 바디 ### -->
		
		<!-- ### PC 페이징 ### -->
		<div class="pagePkg">
			<div class="paginateType1">
				<page:paging formId="" currentPage="${search.currentPage}" pageSize="${search.pageSize}"
					total="${totalCount}" url="/mms/mypage/inquiry/myqa/list/ajax?pageType" type="ajax" callback="mypage.inquiry.inquiryCallback"/>
			</div>
		</div>
		<!-- ### //PC 페이징 ### -->

	</div>