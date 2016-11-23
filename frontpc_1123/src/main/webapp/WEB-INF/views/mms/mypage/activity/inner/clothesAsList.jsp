<%--
	화면명 : 마이페이지 > 나의혜택 > AS list
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
	<c:choose>
		<c:when test="${fn:length(clothesAsList) > 0}">
			<c:forEach var="list" items="${clothesAsList }">
				<li>
					<div class="tr_info bg_gray">
						<span class="date">${list.requestdate }</span>
						<em>${list.ztsCustname }</em>
					</div>
					<div class="tr_box">
						<div class="col1">
							<div class="positionR type1">
								<p class="as_num">접수번호: ${list.repairid }</p>
								<a href="javascript:void(0);" class="title">
									${list.itemname }
								</a>

								<em class="option_txt">
									<i>${list.inventcolorid } / ${list.inventsizeid }</i>
								</em>

								<%--수선구번 : 1 수선 / 2 수선불가 / 3,4,5 교환 --%>
								<p class="txt">
									<c:if test="${list.csordertype == 1 }">
										수선<i class="bar">|</i></span>
										<%-- 유상구분 : 0 유상 / 1 무상 --%>
										<c:if test="${list.ascosttype == 0 }">
											유상 (<fmt:formatNumber value="${list.price }" pattern="###,###"/> 원)
										</c:if>
										<c:if test="${list.ascosttype == 1 }">
											무상
										</c:if>
									</c:if>
									<c:if test="${list.csordertype == 2 }">
										수선 불가
									</c:if>
									<c:if test="${list.csordertype == 3 or list.csordertype == 4 or list.csordertype == 5}">
										교환
									</c:if>
								</p>
							</div>
						</div>

						<div class="col2">
							<div class="stateBox">
								<strong>
									<%-- 수선상태 : 1 매장접수 / 2 본사접수 / 3 수선완료 --%>
									<c:choose>
										<c:when test="${list.repairstatus == 1 }">
											매장접수
										</c:when>
										<c:when test="${list.repairstatus == 2 }">
											본사접수
										</c:when>
										<c:otherwise>
											수선완료 <span class="as_date">수선완료일<i>(유효기간 ~ 2016/06/01)</i></span>
										</c:otherwise>
									</c:choose>
								</strong>
							</div>
						</div>
					</div>
				</li>
			</c:forEach>								
		</c:when>
		<c:otherwise>
			<li class="noData_tp1">
				의류 수선 내역이 없습니다.
			</li>
		</c:otherwise>
	</c:choose>
	
	
<div>
	<input type="hidden" id="total" name="total" value="${totalCount +0 }">
</div>