<%--
	화면명 : 브랜드관 > 템플릿A > 이벤트 화면
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript">
$(document).ready(function() {
	brand.template.contentsInit();
	
	// 이벤트 모바일 이미지 resizing
	$(".mobile .brand_event_list img").css("width", "100%");
});
</script>

<div class="b_event">
	<ul class="tabBox tp1">
		<li id="event_tabB_li" class="on">
			<a>이벤트</a>
		</li>
		<li>
			<a>기획전</a>
		</li>
	</ul>
	<div class="tab_con tab_conOn">
		<div class="brand_event_list">
			<ul>
				<c:choose>
					<c:when test="${empty eventList}">
						<li class="empty">진행중인 이벤트가 없습니다.</li>
					</c:when>
					<c:otherwise>
						<c:forEach items="${eventList}" var="event" varStatus="status1">
							<fmt:parseDate value="${event.eventStartDt}" var="startDateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
							<fmt:formatDate value="${startDateFmt}" var="eventStartDt" pattern="yyyy.MM.dd"/>
							
							<fmt:parseDate value="${event.eventEndDt}" var="endDateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
							<fmt:formatDate value="${endDateFmt}" var="eventEndt" pattern="MM.dd"/>
							
							<li>
								<a href="javascript:brand.template.goEventDetail('event', '${event.eventId}');">
									<c:choose>
										<c:when test="${isMobile eq 'true'}">
											<img src="${_IMAGE_DOMAIN_}${event.img2}" alt="${event.text2}" />
										</c:when>
										<c:otherwise>
											<img src="${_IMAGE_DOMAIN_}${event.img1}" alt="${event.text1}" width="330px" height="133px"/>
										</c:otherwise>
									</c:choose>
								</a>
								<span>행사기간 : ${eventStartDt} ~ ${eventEndt}</span>
							</li>
						</c:forEach>
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
	</div>
</div>
