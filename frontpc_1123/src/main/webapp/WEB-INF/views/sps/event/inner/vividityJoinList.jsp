<%--
	화면명 : 프론트 메인 > 이벤트&혜택 > 생생테스터 상세
	작성자 : stella
 --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript">
$(document).ready(function() {

});
</script>

<div class="personally_list_th">
	<span class="user_id">아이디</span>
	<span class="info">회원 한마디</span>
	<span class="date">신청일</span>
</div>

<ul class="personally_list">
	<c:set var="s_memberNo" value="${search.memberNo}" />
	<c:set var="e_memberNo" value="${myEventJoin.memberNo}" />
		
	<c:forEach items="${expJoinList}" var="myEventJoin" varStatus="status1">
		<c:if test="${s_memberNo eq e_memberNo}">
			<fmt:parseDate value="${myEventJoin.insDt}" var="insDtFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
			<fmt:formatDate value="${insDtFmt}" var="joinDt" pattern="yyyy/MM/dd HH24:MI"/>
			
			<li class="user">
				<strong class="user_id">${myEventJoin.memName}</strong>
				<div class="info">
					<p>${myEventJoin.detail}</p>
					<div class="btn">
						<a href="#" class="btn_sStyle1 sWhite2">수정</a>
						<a href="#" class="btn_sStyle1 sWhite2">삭제</a>
					</div>
				</div>
				<span class="date">${joinDt}</span>
			</li>
		</c:if>
	</c:forEach>
		
	<c:forEach items="${expJoinList}" var="expEventJoin" varStatus="status2">	
		<c:if test="${s_memberNo ne e_memberNo}">		
			<fmt:parseDate value="${expEventJoin.insDt}" var="insDtFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
			<fmt:formatDate value="${insDtFmt}" var="joinDt" pattern="yyyy/MM/dd HH:MM"/>
			
			<li>
				<strong class="user_id">${expEventJoin.memName}</strong>
				<div class="info">
					<p>${expEventJoin.detail}</p>
				</div>
				<span class="date">${joinDt}</span>
			</li>
		</c:if>
	</c:forEach>
</ul>

<div class="paginateType1">
	<page:paging formId="styleForm" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
		total="${expJoinList[0].totalCount}" url="/sps/event/vividity/join/ajax" type="ajax" callback="sps.event.vividity.joinAjaxCallback" />
</div>