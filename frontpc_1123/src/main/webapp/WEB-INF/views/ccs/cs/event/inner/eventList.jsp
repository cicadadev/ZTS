<%--
	화면명 : 담청자 발표 - 담청자 공지 목록
	작성자 : roy
 --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>

 <c:choose>
 	<c:when test="${empty search.isScroll}">
		<form id="noticeSearchForm">
			<input type="hidden" value="${search.type }"  name="type"/>
			<input type="hidden" value="${search.info }"  name="info"/>
			<input type="hidden" value="${totalCount}"    name="totalCount"/>
		</form>
		
		<div class="list_group">
			<!-- ### 테이블 헤더 ### -->
			<div class="div_tb_thead4">
				<div class="tr_box">
					<span class="col1">번호</span>
					<span class="col2">제목</span>
					<span class="col3">등록일</span>
					<span class="col4">조회수</span>
				</div>
			</div>
			<!-- ### //테이블 헤더 ### -->
			
			<!-- ### 테이블 바디 ### -->
			<ul class="div_tb_tbody4">
				<c:choose>
					<c:when test="${!empty list}">
						<c:forEach items="${list}" var="list">
							<c:if test="${list.topNoticeYn == 'Y'}">
								<li class="notice">
							</c:if>
							<c:if test="${list.topNoticeYn == 'N'}">
								<li>
							</c:if>
								<div class="tr_box">
									<div class="col1">
										<c:if test="${list.topNoticeYn == 'Y'}">
											<span class="icon_type1 iconPurple2">공지</span>
										</c:if>
										<c:if test="${list.topNoticeYn == 'N'}">
											<span class="num">${list.noticeNo}</span>
										</c:if>
									</div>
					
									<div class="col2">
										<div class="text_indent">
											<a href="javascript:custcenter.event.detail('${list.noticeNo}', '${list.readCnt}');" class="name" >
												<i>
													<c:if test="${list.topNoticeYn == 'Y'}">
														<span class="icon_type1 iconPurple2 mo_only">공지</span>
													</c:if>
													<c:if test="${list.eventTargetDivCd == 'EVENT_TARGET_DIV_CD.EXP'}">
														<b class="point">[생생 테스터]</b>
													</c:if>
													<c:if test="${list.eventTargetDivCd == 'EVENT_TARGET_DIV_CD.GENERAL'}">
														<b class="point">[이벤트]</b>
													</c:if>
													<c:if test="${not empty list.eventName}">
														<b class="point">[${list.eventName}]</b>
													</c:if>
													${list.title}
												</i>
												<c:if test="${list.newYn == 'Y'}">
													<img src="/resources/img/pc/ico/ico_new.gif" alt="" />
												</c:if>
											</a>
										</div>
									</div>
					
									<div class="col3">
										<span class="date">${list.insDt}</span>
									</div>
					
									<div class="col4">
										<span class="count">${list.readCnt}</span>
									</div>
								</div>
							</li>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<li class="noData_tp1">
							검색 결과가 없습니다.
						</li>
					</c:otherwise>
				</c:choose>
			</ul>
			<!-- ### //테이블 바디 ### -->
							
			<!-- ### PC 페이징 ### -->
			<div class="paginateType1">
				<page:paging formId="noticeSearchForm" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
						total="${totalCount}" url="/ccs/cs/event/notice/list/ajax" type="ajax" callback="custcenter.notice.listCallback"/>
			</div>
			<!-- ### //PC 페이징 ### -->
		</div>				
 	</c:when>
 	<c:otherwise>
 		<c:forEach items="${list}" var="list">
			<c:if test="${list.topNoticeYn == 'Y'}">
				<li class="notice">
			</c:if>
			<c:if test="${list.topNoticeYn == 'N'}">
				<li>
			</c:if>
				<div class="tr_box">
					<div class="col1">
						<c:if test="${list.topNoticeYn == 'Y'}">
							<span class="icon_type1 iconPurple2">공지</span>
						</c:if>
						<c:if test="${list.topNoticeYn == 'N'}">
							<span class="num">${list.noticeNo}</span>
						</c:if>
					</div>
	
					<div class="col2">
						<div class="text_indent">
							<a href="javascript:custcenter.event.detail('${list.noticeNo}', '${list.readCnt}');" class="name" >
								<i>
									<c:if test="${list.topNoticeYn == 'Y'}">
										<span class="icon_type1 iconPurple2 mo_only">공지</span>
									</c:if>
									<c:if test="${list.eventTargetDivCd == 'EVENT_TARGET_DIV_CD.EXP'}">
										<b class="point">[생생 테스터]</b>
									</c:if>
									<c:if test="${list.eventTargetDivCd == 'EVENT_TARGET_DIV_CD.GENERAL'}">
										<b class="point">[이벤트]</b>
									</c:if>
									<c:if test="${not empty list.eventName}">
										<b class="point">[${list.eventName}]</b>
									</c:if>
									${list.title}
								</i>
								<c:if test="${list.newYn == 'Y'}">
									<img src="/resources/img/pc/ico/ico_new.gif" alt="" />
								</c:if>
							</a>
						</div>
					</div>
	
					<div class="col3">
						<span class="date">${list.insDt}</span>
					</div>
	
					<div class="col4">
						<span class="count">${list.readCnt}</span>
					</div>
				</div>
			</li>
		</c:forEach>
 	</c:otherwise>
</c:choose>
					