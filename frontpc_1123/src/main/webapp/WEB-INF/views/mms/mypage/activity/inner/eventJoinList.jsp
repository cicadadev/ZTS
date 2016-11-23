<%--
	화면명 : 
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>


<script type="text/javascript">
	$("[name=TOTAL_CNT]").val("${joinCntInfo.totalCount}");
</script>

<form name="eventJoinForm">

</form>
								
<ul class="div_tb_tbody2">
	<c:if test="${joinCntInfo.totalCount == 0}">
		<li class="empty">
			<div class="tr_box">
				<div class="col99">
					최근 참여하신 내역이 없습니다.
				</div>
			</div>
		</li>
	</c:if>
	
	<c:if test="${joinCntInfo.totalCount > 0}">
		<c:forEach items="${joinEventList}" var="joinEvent" varStatus="status">			
			<li>
				<div class="tr_box">
					<div class="col1">
						<div class="title">
							<div class="state">
								<c:choose>
									<c:when test="${joinEvent.gubun == 'EXP'}">
										<span class="icon_type2 iconPink3">후기등록</span>
									</c:when>
									<c:when test="${joinEvent.gubun == 'RUN'}">
										<span class="icon_type2 iconBlue3">진행중</span>
									</c:when>
									<c:when test="${joinEvent.gubun == 'STOP'}">
										<span class="icon_type2">종료</span>
									</c:when>
								</c:choose>												
							</div>
							<a href="#">${joinEvent.spsEvent.name}</a>
						</div>
					</div>
					<div class="col2">
						<span class="vDate"><i>이벤트 기간</i>${joinEvent.spsEvent.eventStartDt} ~${joinEvent.spsEvent.eventEndDt}</span>
					</div>
					<div class="col3">
						<c:choose>
							<c:when test="${joinEvent.gubun == 'EXP'}">
								<a href="#none" class="btn_sStyle2 sGray2 btnReview">후기등록</a>
							</c:when>
							<c:when test="${joinEvent.gubun == 'RUN'}">
								<span><em>당첨자 발표</em>${joinEvent.spsEvent.winNoticeDate}</span>
							</c:when>
							<c:when test="${joinEvent.gubun == 'STOP'}">
								<c:if test="${joinEvent.winYn == 'Y'}">
									<span class="posR winner">당첨</span>
								</c:if>
								<c:if test="${joinEvent.winYn == 'N'}">
									<span class="posR">미당첨</span>
								</c:if>
							</c:when>
						</c:choose>
					</div>
				</div>
			</li>						
		</c:forEach>
	</c:if>		
</ul>								

<div class="paginateType1">
	<page:paging formId="eventJoinForm" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
			total="${joinCntInfo.totalCount}" url="/mms/mypage/eventjoin/ajax" type="ajax" callback="listCallback"/>
</div>
	
		