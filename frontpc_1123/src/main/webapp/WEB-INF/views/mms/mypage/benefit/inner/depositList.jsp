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
	<input type="hidden" id="total" value="${depositInfo.plus + depositInfo.minus }">
	<input type="hidden" id="item1" value="${depositInfo.plus + 0 }">
	<input type="hidden" id="item2" value="${depositInfo.minus + 0 }">
	<input type="hidden" id="totalCount" name="totalCount" value="${totalCount +0}">
</div>

		<c:choose>
			<c:when test="${!empty depositList}">
				<c:forEach var="list" items="${depositList }">
					<li>
						<div class="tr_box">
							<div class="col1">
								<span class="date">${list.insDt }</span>
							</div>
							<div class="col2">
								<div class="title">
								<c:if test="${list.depositAmt > 0}">
									<span class="plus">[<tags:codeName code="${list.depositTypeCd }"/>]</span>
								</c:if>
								<c:if test="${list.depositAmt < 0}">
									<span class="minus">[<tags:codeName code="${list.depositTypeCd }"/>]</span>
								</c:if>
								<c:choose>
									<c:when test="${list.depositTypeCd eq 'DEPOSIT_TYPE_CD.PAYMENT' 
									or list.depositTypeCd eq 'DEPOSIT_TYPE_CD.CLAIMREFUND' }">
										<c:if test="${list.depositTypeCd eq 'DEPOSIT_TYPE_CD.PAYMENT' 
										and (list.orderId ne null and list.orderId ne '')}">
										&nbsp;<b>주문번호</b> : ${list.orderId }&nbsp;
										</c:if>
										<c:if test="${list.depositTypeCd eq 'DEPOSIT_TYPE_CD.CLAIMREFUND'
										and (list.claimNo ne null and list.claimNo ne '')}">
										&nbsp;<b>클레임번호</b> : ${list.claimNo }&nbsp;
										</c:if>
										<c:if test="${list.note ne null and list.note ne ''}">
										${list.note }
										</c:if>
									</c:when>
									<c:otherwise>
									&nbsp;${list.note }
									</c:otherwise>
								</c:choose>
								</div>
							</div>
							<div class="col3">
								<c:if test="${list.depositAmt > 0}">
									<span class="plus">
								</c:if>
								<c:if test="${list.depositAmt < 0}">
									<span class="minus">
								</c:if><fmt:formatNumber value="${list.depositAmt + 0}" pattern="###,###" /></span>
							</div>
						</div>
					</li>
				</c:forEach>								
			</c:when>
			<c:otherwise>
				<li class="empty">
					<div class="tr_box">
						<div class="col99">
							최근 예치금 내역이 없습니다.
						</div>
					</div>
				</li>
			</c:otherwise>
		</c:choose>			

<c:if test="${!isMobile }">
	<!-- ### PC 페이징 ### -->
	<div class="pagePkg">
		<div class="paginateType1">
			<page:paging formId="" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
				total="${totalCount}" url="/mms/mypage/deposit/list/ajax" type="ajax" callback="mypage.deposit.depositCallback"/>
		</div>
		<a href="#none" class="btn_sStyle4 sPurple1 btnRefund" onclick="javascript:mypage.deposit.refund();">예치금 환불신청</a>
	</div>
	<!-- ### //PC 페이징 ### -->
</c:if>
