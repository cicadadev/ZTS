<%--
	화면명 : 이벤트 상세 - 댓글이벤트
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<script type="text/javascript">
$(document).ready(function() {
});

//응모하기
function eventJoin(){
	sps.event.saveJoin('${event.eventId}');
};
</script>

<c:choose>
	<c:when test="${isMobile eq 'true'}">
		<div class="mo_navi">
			<button type="button" class="btn_navi_prev" onclick="parent.history.back();">이전 페이지로..</button>
			<h2>이벤트 상세</h2>
		</div>
	</c:when>
	<c:otherwise>
		<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
			<jsp:param value="이벤트 상세" name="pageNavi"/>
		</jsp:include>
	</c:otherwise>
</c:choose>

<div class="inner">
	<div class="vivid_detail">
	
		<c:choose>
			<c:when test="${isMobile eq 'true'}">
				${event.html2}
			</c:when>
			<c:otherwise>
				${event.html1}
			</c:otherwise>
		</c:choose>
	
		<div class="comment_form_wrap">
			<h3 class="title">새로워진 궁중비책 홈페이지 감상평&amp;공유링크 남기기!</h3>
			<div class="cmt_inner">
				<div class="sns_txt_wrap">
					<ul class="txt_info">
						<li>블로그 및 SNS 주소를 넣어주세요.</li>
						<li><strong>동일한 URL은 중복 등록하실 수 없습니다.</strong></li>
					</ul>
				</div>
				<ul class="url_list">
					<li><input type="text" value="http://" class="inputTxt_style1" /></li>
				</ul>

				<p class="ps">광고성/스팸성/비방성 댓글은 삭제됩니다.</p>
				<div class="comment_form">
					<div class="txtarea_box">
						<textarea rows="5" cols="10"></textarea>
						<label>로그인 후 이용해 주세요. (600자 이내)</label>
					</div>
					<a href="#">등록하기</a>
					<!-- 1120 -->
					<dl>
						<dt>공개여부 설정 </dt>
						<dd>
							<label class="radio_style1">
								<em class="selected">
									<input type="radio" name="ra1_3" value="" checked="checked">
								</em>
								<span>공개</span>
							</label>
							<label class="radio_style1">
								<em>
									<input type="radio" name="ra1_2" value="">
								</em>
								<span>비공개</span>
							</label>
						</dd>
					</dl>
					<!-- //1120 -->
				</div>
			</div>
		</div>
		<div class="personally_list_wrap">
			
		</div>

	</div>
</div>