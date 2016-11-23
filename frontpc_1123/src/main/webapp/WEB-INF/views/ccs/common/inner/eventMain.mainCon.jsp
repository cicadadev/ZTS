<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript">
$(document).ready(function() {
	sps.event.vividity.tabEffect();
});
</script>

<form name="eventForm" id="eventForm">
	<input type="hidden" name="eventBackYn" id="eventBackYn" value="" />
</form>

<div class="main">

<!-- <div class="mainCon"> -->
	<div class="swiper-wrapper">
		<div class="inner main7">
		
			<div class="promotion_evnet">
				<p class="event_check">
					<span class="ec_txt">이벤트 당첨 확인하세요!</span><a href="javascript:ccs.link.go('/ccs/cs/event/list', CONST.NO_SSL, CONST.LOGIN_CHECK_TYPE_NONE);" class="btn_lyBox1">당첨자 발표  <span>&gt;</span></a>
				</p>
				
				<ul class="tabBox tp1">
					<li class="on">
						<a href="#">이벤트</a>
					</li>
					<li>
						<a href="#">생생 테스터</a>
					</li>
				</ul>
				
				<!-- 이벤트 -->
				<div class="tab_con tab_conOn" id="eventArea">
					<!-- <div class="banner_event">
						<a href="#">
							<img src="/resources/img/pc/temp/lineBanner.jpg" alt="롸잇나우! 지금 제로투세븐닷컴에서 드리는 혜택 ">
						</a>
					</div> -->
		
					<c:choose>
						<c:when test="${empty eventList}">
							<div class="event_empty">진행중인 이벤트가 없습니다.</div>
						</c:when>
						<c:otherwise>
							<ul class="event_list_type01">
								<c:forEach items="${eventList}" var="event" varStatus="status1">
									<fmt:parseDate value="${event.eventStartDt}" var="startDateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
									<fmt:formatDate value="${startDateFmt}" var="eventStartDt" pattern="yyyy.MM.dd"/>
									
									<fmt:parseDate value="${event.eventEndDt}" var="endDateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
									<fmt:formatDate value="${endDateFmt}" var="eventEndt" pattern="MM.dd"/>
									
									<li>
										<a href="javascript:ccs.link.go('/sps/event/detail?eventId=${event.eventId}');">
											<div class="img">
												<c:choose>
													<c:when test="${isMobile eq 'true'}">
														<img src="${_IMAGE_DOMAIN_}${event.img2}" alt="${event.text2}" />
													</c:when>
													<c:otherwise>
														<img src="${_IMAGE_DOMAIN_}${event.img1}" alt="${event.text1}" width="330px;" height="133px;"/>
													</c:otherwise>
												</c:choose>									
											</div>
											<dl class="info">
												<dt>${event.name}</dt>
												<dd>행사기간 : ${eventStartDt} ~ ${eventEndt}</dd>
											</dl>
										</a>
										<span></span>
									</li>
								</c:forEach>
							</ul>
						</c:otherwise>
					</c:choose>
		
					<h3 class="tit_style1">제휴카드 혜택</h3>
					<ul class="event_list_type02">
						<li>
							<a href="javascript:ccs.link.go('/sps/event/card?cno=3', CONST.NO_SSL, CONST.LOGIN_CHECK_TYPE_NONE);">
								<div class="img">
									<img src="/resources/img/pc/temp/temp_332x178_01.jpg" alt="">
								</div>
								<p class="info">롯데 국민 행복카드</p>
							</a>
						</li>
						<li>
							<a href="javascript:ccs.link.go('/sps/event/card?cno=6', CONST.NO_SSL, CONST.LOGIN_CHECK_TYPE_NONE);">
								<div class="img">
									<img src="/resources/img/pc/temp/temp_332x178_02.jpg" alt="">
								</div>
								<p class="info">신한 고운맘/Kids Plus카드</p>
							</a>
						</li>
						<li>
							<a href="javascript:ccs.link.go('/sps/event/card?cno=5', CONST.NO_SSL, CONST.LOGIN_CHECK_TYPE_NONE);">
								<div class="img">
									<img src="/resources/img/pc/temp/temp_332x178_03.jpg" alt="">
								</div>
								<p class="info">롯데 아이 행복카드</p>
							</a>
						</li>
						<li>
							<a href="javascript:ccs.link.go('/sps/event/card?cno=4', CONST.NO_SSL, CONST.LOGIN_CHECK_TYPE_NONE);">
								<div class="img">
									<img src="/resources/img/pc/temp/temp_332x178_04.jpg" alt="">
								</div>
								<p class="info">롯데카드 육아클럽</p>
							</a>
						</li>
						<li>
							<a href="javascript:ccs.link.go('/sps/event/card?cno=2', CONST.NO_SSL, CONST.LOGIN_CHECK_TYPE_NONE);">
								<div class="img">
									<img src="/resources/img/pc/temp/temp_332x178_05.jpg" alt="">
								</div>
								<p class="info">BC 국민행복카드</p>
							</a>
						</li>
						<li>
							<a href="javascript:ccs.link.go('/sps/event/card?cno=1', CONST.NO_SSL, CONST.LOGIN_CHECK_TYPE_NONE);">
								<div class="img">
									<img src="/resources/img/pc/temp/temp_332x178_06.jpg" alt="">
								</div>
								<p class="info">하나멤버스 1Q카드</p>
							</a>
						</li>
					</ul>
				</div>
				<!-- //이벤트 -->
				
				<!-- 생생테스터 -->
				<div class="tab_con" id="vividityArea">
					<!-- <div class="banner_event">
						<a href="#">
							<img src="/resources/img/pc/temp/temp_event_banner.jpg" alt="생생 TESTER 리플달고 무료로 쓰자! 공짜로 사용해보시고 생생한 후기로 소문내주세요~">
						</a>
					</div> -->
					
					<ul class="event_list_type03">
						<c:choose>
							<c:when test="${empty expEventList}">
								<li class="noData_tp1">진행중인 이벤트가 없습니다.</li>
							</c:when>
							<c:otherwise>
								<c:forEach items="${expEventList}" var="expEvent" varStatus="status2">
									<fmt:parseDate value="${expEvent.eventJoinStartDt}" var="startDateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
									<fmt:formatDate value="${startDateFmt}" var="expEventStartDt" pattern="yyyy/MM/dd"/>
									
									<fmt:parseDate value="${expEvent.eventJoinEndDt}" var="endDateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
									<fmt:formatDate value="${endDateFmt}" var="expEventEndDt" pattern="yyyy/MM/dd"/>
									
									<fmt:parseDate value="${expEvent.winNoticeDate}" var="noticeDateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
									<fmt:formatDate value="${noticeDateFmt}" var="winNoticeDate" pattern="yyyy/MM/dd"/>
									
									<li>
										<dl class="item_info">
											<dt onclick="ccs.link.go('/sps/event/vividity/detail?eventId=${expEvent.eventId}', CONST.NO_SSL);">
												<span class="tag">
													<c:choose>
														<c:when test="${expEvent.expBadge eq 'NEW'}">
															<span class="icon_type4">NEW</span>
														</c:when>
														<c:when test="${expEvent.expBadge eq 'PSING'}">
															<span class="icon_type4 col1">후기등록</span>
														</c:when>
														<c:when test="${expEvent.expBadge eq 'JOINEND'}">
															<span class="icon_type4 col2">모집마감</span>
														</c:when>
														<c:otherwise>
														
														</c:otherwise>
													</c:choose>
												</span>
												<c:choose>
													<c:when test="${isMobile eq 'true'}">
														<img src="${_IMAGE_DOMAIN_}${expEvent.img2}" alt="${expEvent.text2}" />
													</c:when>
													<c:otherwise>
														<img src="${_IMAGE_DOMAIN_}${expEvent.img1}" alt="${expEvent.text1}" width="200" height="200px;"/>
													</c:otherwise>
												</c:choose>
											</dt>
											<c:choose>
												<c:when test="${not empty expEvent.spsEventbrands}">
													<dd class="title">[${expEvent.spsEventbrands[0].brandName}] ${expEvent.name} (${expEvent.pmsProduct.name})</dd>
												</c:when>
												<c:otherwise>
													<dd class="title">${expEvent.name} (${expEvent.pmsProduct.name})</dd>
												</c:otherwise>
											</c:choose>
											<dd class="info"><span>기간 :</span>${expEventStartDt} ~ ${expEventEndDt}</dd>
											<dd class="info"><span>발표 :</span>${winNoticeDate}</dd>
											<dd class="info"><span>당첨 :</span><strong>${expEvent.winnerNumber}</strong>명 / 신청 <em>${empty expEvent.joinNumber? '0' : expEvent.joinNumber}</em>명</dd>
										</dl>
									</li>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					</ul>
					
					<div class="paginateType1">
						<page:paging formId="styleForm" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
							total="${totalCount}" url="/mms/member/style/list/ajax?brandId=${brandInfo.brandId}" type="ajax" callback="brand.template.styleCallback" />
					</div>
				</div>
				<!-- //생생테스터 -->
			</div>
		
		</div>
	</div>
<!-- </div> -->

</div>

<style>
	.mobile .event_empty {width:100%; line-height:129px; text-align:center;}
</style>