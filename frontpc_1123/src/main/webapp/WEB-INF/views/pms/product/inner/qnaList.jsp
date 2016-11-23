<%--
	화면명 : 상품상세 - QnA목록
	작성자 : eddie
 --%>

<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<form id="qnaSearchForm">
	<input type="hidden" value="${search.productId }" name="productId"/>
	<input type="hidden" value="${search.secretYn }" name="secretYn"/>
</form>
<div class="tbl_box2"  style="margin-top:10px">
	<table>
		<caption>Q&amp;A 목록 입니다. 답변여부, 내용, 등록자, 등록일 등의 정보를 제공 합니다.</caption>
		<colgroup>
			<col style="width:10%;">
			<col style="width:*">
			<col style="width:10%;">
			<col style="width:10%;">
		</colgroup>
		<thead>
			<tr>
				<th>답변여부</th>
				<th>내용</th>
				<th>등록자</th>
				<th>등록일</th>
			</tr>
		</thead>
		<tbody>
<c:choose>
<c:when test="${not empty list}">
<c:forEach var="qna" items="${list}">
			<tr onclick="openQna(this)">
				<td>
					<c:choose>
					<c:when test="${ qna.productQnaStateCd eq 'PRODUCT_QNA_STATE_CD.COMPLETE'}">
						<c:set var="statusClass" value="iconPink3"/>
					</c:when>
					<c:when test="${ qna.productQnaStateCd eq 'PRODUCT_QNA_STATE_CD.ANSWER'}">
						<c:set var="statusClass" value="iconBlue3"/>
					</c:when>
					<c:when test="${ qna.productQnaStateCd eq 'PRODUCT_QNA_STATE_CD.ACCEPT'}">
						<c:set var="statusClass" value="iconPurple3"/>
					</c:when>
					</c:choose>
					<span class="icon_type2 ${statusClass }"><tags:codeName code="${ qna.productQnaStateCd }"/></span>
				</td>
				<td class="subject td_tit">
					<div class="positionR">
						<a href="#none">${ qna.title }</a>
						<c:if test="${qna.secretYn eq 'Y' }">
						<span class="ico_lock">비밀글</span>
						</c:if>
					</div>
				</td>
				<td><c:choose><c:when test="${ memberNo ne qna.memberNo }">${fn:substring(qna.memberId,0,fn:length(qna.memberId)-3)}***</c:when><c:otherwise>${qna.memberId }</c:otherwise></c:choose></td>
				<td class="date">
				<fmt:parseDate var="dateString" value="${qna.insDt}" pattern="yyyy-MM-dd HH:mm:ss" />
				<fmt:formatDate value="${dateString}" pattern="yyyy/MM/dd" />
				</td>
			</tr>
<c:if test="${qna.secretYn eq 'N' || (memberNo eq qna.memberNo && not empty memberNo)}">			
			<tr class="tr_cont qa_cont tr_cont_hide">
				<td colspan="4">
					<div class="q">
						${fn:replace(qna.detail, newLineChar, '<br/>')}
					</div>
<c:if test="${not empty qna.answer  }">	<%--답변 --%>				
					<div class="a">
						<strong>제로투세븐</strong>
						<div>
							${fn:replace(qna.answer, newLineChar, '<br/>')}
						</div>
						<div class="date">
							<fmt:parseDate var="dateString" value="${ qna.answerDt }" pattern="yyyy-MM-dd HH:mm:ss" />
							<fmt:formatDate value="${dateString}" pattern="yyyy/MM/dd" />
						</div> 
					</div>
</c:if>					
				</td>
			</tr>
</c:if>			
</c:forEach>
</c:when>
<c:otherwise>
	<tr>
		<td class="noData_tp1" colspan="4">등록된 Q&amp;A가 없습니다.</td>
		</td>
	</tr>
</c:otherwise>
</c:choose>					
		</tbody>
	</table>
</div>
<c:if test="${not empty list}">
	<div class="paginateType1">
		<page:paging formId="qnaSearchForm" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
				total="${totalCount}" url="/pms/product/qna/list/ajax" type="ajax" callback="listCallback"/>
	</div>
</c:if>
<script>
	$('[name=qnaTotal]').html("(${totalCount})");
	$('[name=qnaTotal2]').html("(${totalCount})");
</script>