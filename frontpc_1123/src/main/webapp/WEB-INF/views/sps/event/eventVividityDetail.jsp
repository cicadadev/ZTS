<%--
	화면명 : 프론트 메인 > 이벤트&혜택 > 생생테스터 상세
	작성자 : stella
 --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="gcp.common.util.FoSessionUtil"%>
<%
	pageContext.setAttribute("isLogin", FoSessionUtil.isMemberLogin());
%>
<script type="text/javascript">
$(document).ready(function() {
	sps.event.vividity.joinList('${expEvent.eventId}');
	
	// sns 공유용 메타데이터 세팅
	$("meta[property='og:image']").attr("content", "${ogTagImage}");
	$("meta[property='og:url']").attr("content", "${ogTagUrl}");
	$("meta[property='og:title']").attr("content", "${ogTagTitle}");
	
	// SNS LAYER EVENT
	$(".mobile .btn_snsInfo").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .sLayer_sns") );
		}
	});
	// 모바일. SNS공유 레이어팝업 위치 조정
	function fnLayerPosition(target_pop) {
		$(target_pop).show();
		$(target_pop).height( $(document).height() );

		var base_top = ($(window).height() - $(" > .box", target_pop).innerHeight()) / 2;
		$(" > .box", target_pop).css({"marginTop" : base_top + $(window).scrollTop() + "px"});
	}
});
</script>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="이벤트|생생테스터" name="pageNavi"/>
</jsp:include>

<div class="inner">
	<div class="vivid_detail">
		
		<c:if test="${isMobile ne 'true'}">
			<div class="event_reply_img"> <!-- 16.11.08 -->
				<img src="/resources/img/pc/temp/detail_main.gif" alt="생생 TESTER 리플달고 무료로 쓰자! 공짜로 사용해보시고 생생한 후기로 소문내주세요~" />
			</div>
		</c:if>
		
		<fmt:parseDate value="${expEvent.eventJoinStartDt}" var="startDateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
		<fmt:formatDate value="${startDateFmt}" var="expEventStartDt" pattern="yyyy/MM/dd"/>
		
		<fmt:parseDate value="${expEvent.eventJoinEndDt}" var="endDateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
		<fmt:formatDate value="${endDateFmt}" var="expEventEndDt" pattern="yyyy/MM/dd"/>
																
		<fmt:parseDate value="${expEvent.winNoticeDate}" var="noticeDateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
		<fmt:formatDate value="${noticeDateFmt}" var="winNoticeDate" pattern="yyyy/MM/dd"/>
		
		<div class="tester_detail_info">
			<div class="img">
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
			</div>
			<div class="info">
				<string class="tit">${expEvent.name}</string>
							
				<ul>
					<li><span>모집기간 :</span>${expEventStartDt} ~ ${expEventEndDt}</li>
					<li><span>당첨발표 :</span>${winNoticeDate}</li>
					<li><span>당첨인원 :</span><strong>${expEvent.winnerNumber}</strong>명 / 신청 <em>${empty expEvent.joinNumber ? '0' : expEvent.joinNumber}</em>명</li>
				</ul>
			</div>
			
			<button type="button" class="btn_snsInfo mo_only">sns 공유</button>
			<ul class="sns_list pc_only">
				<li>
					<a href="javascript:ccs.sns.share('facebook');" class="face">페이스북</a>
				</li>
				<li>
					<a href="javascript:ccs.sns.share('twitter');" class="twitter">트위터</a>
				</li>
				<c:if test="${isMobile eq 'true'}">
					<li>
						<a href="javascript:ccs.sns.share('kakaoLink');" class="kakao">카카오톡</a>
					</li>
				</c:if>
				<li>
					<a href="javascript:ccs.sns.share('kakaoStory');" class="kakaoStory">카카오스토리</a>
				</li>
				<li>
					<a href="javascript:ccs.sns.share('blog');" class="nblog">blog</a>
				</li>
				<li>
					<a href="javascript:ccs.sns.share('link');" class="url">URL</a>
				</li>
			</ul>
		</div>
		
		<div class="detail_box_wrap">
			<ul>
				<li>
					<span>브랜드/제품 설명 체험단 안내 문구 :</span>${expEvent.detail}
				</li>
				<li>
					<c:choose>
						<c:when test="${isMobile eq 'true'}">
							${expEvent.html2}
						</c:when>
						<c:otherwise>
							${expEvent.html1}
						</c:otherwise>
					</c:choose>
				</li>
			</ul>
		</div>
		
<!-- 		<div class="detail_box_wrap"> (16.11.20) 상품 기술서 삭제-->
<!-- 			<h3>상품상세정보</h3> -->
<!-- 			<div class="detail_box"> -->
<%-- 				${expEvent.pmsProduct.detail} --%>
<!-- 			</div> -->
<!-- 		</div> -->
		
		<ul class="detail_notice">
			<li>당첨되신 분께서는 <strong>수령 후 3주 이내 '상품평'을 반드시 작성</strong>하셔야 합니다.</li>
			<li>‘상품평' 미작성 시 <strong>3개월간 모든 이벤트 당첨 대상에서 제외</strong>됩니다.</li>
			<li>정확한 배송을 위해 <strong>'개인정보'에서 '배송지'를 확인</strong>해주세요. <span><a href="javascript:ccs.link.mypage.info.deliveryAddress();" class="btn_sStyle3">배송지 확인</a></span></li>
		</ul>
		
		<div class="comment_form_wrap">
			<h3 class="title">신청할래요!</h3> <!-- 16.11.08 -->
			<div class="sns_txt_wrap">
				<ul class="txt_info">
					<li>블로그 및 SNS 주소를 넣어주세요.</li>
					<li><strong>동일한 URL은 중복 등록하실 수 없습니다.</strong></li>
				</ul>
				<div class="sns">
					<a href="javascript:sps.event.urlDomainSet('naver');" class="ico_sns naver">네이버 블로그</a>
					<a href="javascript:sps.event.urlDomainSet('instagram');" class="ico_sns insta">인스타그램</a>
					<a href="javascript:sps.event.urlDomainSet('facebook');" class="ico_sns face">페이스북</a>
					<a href="javascript:sps.event.urlDomainSet('kakao');" class="ico_sns kakao">카카오 스토리</a>
					<a href="javascript:sps.event.urlDomainSet('etc');" class="ico_sns etc">기타</a>
				</div>
			</div>
			<ul class="url_list" id="joinUrl_ul">
				<li>
					<input type="text" value="SNS를 선택해주세요." class="inputTxt_style1" onfocus="if (this.value =='SNS를 선택해주세요.') {this.value='';}" onblur="if (this.value =='') {this.value='SNS를 선택해주세요.';}" />
					<a href="javascript:sps.event.vividity.addUrl();" class="btn_add">추가</a>
				</li>
<!-- 				<li><input type="text" value="SNS를 선택해주세요." class="inputTxt_style1" /><a href="#" class="btn_del2">삭제</a></li> -->
			</ul>
			<strong class="title2">신청 댓글 작성</strong>
			<div class="comment_form">
				<div class="txtarea_box">
					<textarea rows="5" cols="10"></textarea>
					<c:choose>
						<c:when test="${isLogin eq 'true'}">
							<label>(600자 이내)</label>
						</c:when>
						<c:otherwise>
							<label>로그인 후 이용해 주세요. (600자 이내)</label>
						</c:otherwise>
					</c:choose>					
				</div>
				<span>0/600자</span>
				<a href="javascript:sps.event.vividity.saveJoinUrl('${expEvent.eventId}');">등록하기</a>
			</div>
		</div>
		
		<div class="personally_list_wrap" id="vividityJoinList_Area">
			
		</div>
	</div>
</div>
