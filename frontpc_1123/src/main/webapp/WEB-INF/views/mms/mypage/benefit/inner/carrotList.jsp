<%--
	화면명 : 마이페이지 > 나의혜택 > 당근
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<div>
	<input type="hidden" id="total" name="total" value="${carrotInfo.plus + carrotInfo.minus }">
	<input type="hidden" id="item1" name="item1" value="${carrotInfo.plus + 0 }">
	<input type="hidden" id="item2" name="item2" value="${carrotInfo.minus + 0 }">
	<input type="hidden" id="totalCount" name="totalCount" value="${totalCount +0}">
</div>
	
	<c:choose>
		<c:when test="${!empty carrotList}">
			<c:forEach var="list" items="${carrotList }">
				<li>
					<div class="tr_box">
						<div class="col1">
							<span class="date">${list.insDt }</span>
						</div>
						<div class="col2">
							<div class="title">
							<c:if test="${list.carrot > 0}">
								<span class="plus">[<tags:codeName code="${list.carrotTypeCd }"/>]</span>
							</c:if>
							<c:if test="${list.carrot < 0}">
								<span class="minus">[<tags:codeName code="${list.carrotTypeCd }"/>]</span>
							</c:if>
							&nbsp;${list.note }
							</div>
						</div>
						<div class="col3">
							<c:if test="${list.carrot > 0}">
								<span class="plus">
							</c:if>
							<c:if test="${list.carrot < 0}">
								<span class="minus">
							</c:if><fmt:formatNumber value="${list.carrot +0}" pattern="###,###" /></span>
						</div>
						<div class="col4">
							<span class="date"><c:if test="${isMobile }">유효기간 : </c:if>${list.term }</span>
						</div>
					</div>
				</li>
			</c:forEach>								
		</c:when>
		<c:otherwise>
			<li class="empty">
				<div class="tr_box">
					<div class="col99">
						최근 당근내역이 없습니다.
					</div>
				</div>
			</li>
		</c:otherwise>
	</c:choose>

	<!-- ### PC 페이징 ### -->
	<div class="pagePkg">
		<div class="paginateType1">
			<page:paging formId="" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
				total="${totalCount}" url="/mms/mypage/carrot/list/ajax" type="ajax" callback="mypage.carrot.carrotCallback"/>
		</div>
	</div>
	<!-- ### //PC 페이징 ### -->
