<%--
	화면명 : 이벤트 상세 - 댓글 이벤트 - 댓글 목록
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<script type="text/javascript">
$(document).ready(function() {
	
});

</script>

<c:choose>
	<c:when test="${empty search.isScroll}">
		<form id="replayListForm">
			<input type="hidden" value="${search.type}"  name="type"/>
			<input type="hidden" value="${search.info}"  name="info"/>
			<input type="hidden" value="${totalCount}"   name="totalCount"/>
		</form>
		
		<div class="personally_list_th">
			<span class="user_id">아이디</span>
			<span class="info">회원 한마디</span>
			<span class="date">신청일</span>
		</div>
		<ul class="personally_list">
			<li class="user">
				<strong class="user_id">CA***</strong>
				<div class="info">
					<p class="url"><a href="#none">http://url</a></p>
					<p>디자인도 깔끔하고 너무 예쁘네요! 순한 성분이 예민한 우리 아이 피부를 보호해줄 것 같아 꼭 사용해보고 싶어 신청합니다.<span class="ico_lock">비밀글</span></p> <!-- 1120 -->
					<div class="btn">
						<a href="#" class="btn_sStyle1 sWhite2">수정</a>
						<a href="#" class="btn_sStyle1 sWhite2">삭제</a>
					</div>
				</div>
				<span class="date">2016/05/09 23:50</span>
			</li>
			<c:choose>
				<c:when test="${!empty list}">
					<c:forEach items="${list}" var="list">
						<li>
							<strong class="user_id">CA***</strong>
							<div class="info">
								<p class="url"><a href="#none">http://url</a></p>
								<p>디자인도 깔끔하고 너무 예쁘네요!</p>
							</div>
							<span class="date">2016/05/09 23:50</span>
						</li>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<li class="noData_tp1">
						댓글이 존재하지 않습니다.
					</li>
				</c:otherwise>
			</c:choose>
		</ul>
		
		<!-- ### PC 페이징 ### -->
		<div class="paginateType1">
			<page:paging formId="replayListForm" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
					total="${totalCount}" url="/sps/event/reply/list/ajax" type="ajax" callback="sps.event.reply.listCallback"/>
		</div>
		<!-- ### //PC 페이징 ### -->
	</c:when>
	<c:otherwise>
		<c:forEach items="${list}" var="list">
			<li>
				<strong class="user_id">CA***</strong>
				<div class="info">
					<p class="url"><a href="#none">http://url</a></p>
					<p>디자인도 깔끔하고 너무 예쁘네요!</p>
				</div>
				<span class="date">2016/05/09 23:50</span>
			</li>
		</c:forEach>
	</c:otherwise>
</c:choose>