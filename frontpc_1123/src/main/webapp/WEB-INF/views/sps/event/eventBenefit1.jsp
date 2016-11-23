<%--
	화면명 : 이벤트 제로투세븐 멤버 혜택(인증전화면)
	작성자 : eddie
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<c:choose>
	<c:when test="${isMobile eq 'true'}">
		<div class="mo_navi">
			<button type="button" class="btn_navi_prev" onclick="parent.history.back();">이전 페이지로..</button>
			<h2>이벤트 상세</h2>
		</div>
	</c:when>
	<c:otherwise>
		<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
			<jsp:param value="이벤트" name="pageNavi"/>
		</jsp:include>
	</c:otherwise>
</c:choose>
<script>
var d = new Date();
</script>
<div class="inner">

		<!-- 제로투세븐 회원 혜택 -->
		<div class="mem_benefit_top">
			<span class="bg">제로투세븐 회원 혜택</span>
			<div class="mem_benefit_inner before">
				<p class="blind">제로투세븐 회원 혜택 0to7회원만의 혜택! 매월1일 제로투세븐닷컴, 오프라인 의류 매장에서 사용 가능한 다양한 쿠폰이 제공됩니다.</p>
				<div class="benefit_month"><span><script>document.write('' + (d.getFullYear()));</script></span>년 <span><script>document.write('' + (d.getMonth() + 1));</script></span>월</div>
				<div class="benefit_txt">
					<p>고객님의 등급확인 및 이번 달 받을 수 있는 쿠폰이 궁금하시다면 로그인해주세요.<br>아직 회원 가입 전이라면~ 가입 후 푸짐한 혜택 받으세요~!</p>
					<span>
						<a href="#none" onclick="mms.common.joinPopup()" class="btn_mStyle1 sWhite1">회원가입</a>
						<a href="#none" onclick="ccs.link.login()" class="btn_mStyle1 sPurple1">로그인</a>
					</span>
				</div>
			</div>
		</div>
		<div class="mem_benefit_info before">
			<div class="blind">
				<dl>
					<dt>등급 : VIP</dt>
					<dd>최근 6개월 구매누적금액 : 60만원 이상 ~</dd>
					<dd>할인쿠폰 : 10% coupon / 1,000 plus coupon / APP전용 5% plus coupon / 2,500 배송비할인 / 오프라인매장전용 10% coupon</dd>
					<dd>릴레이쿠폰 : APP전용 10% coupon 2장 / <ahref="#none"> 2,500 배송비할인 2장</a></dd>
				</dl>
				<dl>
					<dt>등급 : GOLD</dt>
					<dd>최근 6개월 구매누적금액 : 30만원 이상 ~ 60만원 미만</dd>
					<dd>할인쿠폰 : 10% coupon / 1,000 plus coupon / APP전용 5% plus coupon / 오프라인매장전용 10% coupon</dd>
					<dd>릴레이쿠폰 : APP전용 10% coupon / APP전용 1,000 plus coupon / 2,500 배송비할인</dd>
				</dl>
				<dl>
					<dt>등급 : SILVER</dt>
					<dd>최근 6개월 구매누적금액 : 10만원 이상 ~ 30만원 미만</dd>
					<dd>할인쿠폰 : 10% coupon / 1,000 plus coupon / APP전용 5% plus coupon / 오프라인매장전용 10% coupon</dd>
				</dl>
				<dl>
					<dt>등급 : FAMILY</dt>
					<dd>최근 6개월 구매누적금액 : ~ 10만원 미만</dd>
					<dd>할인쿠폰 : 10% coupon / 1,000 plus coupon / APP전용 5% plus coupon / 오프라인매장전용 10% coupon</dd>
				</dl>
				<dl>
					<dt>등급 : WELCOME</dt>
					<dd>최근 6개월 구매누적금액 : 구매이력 없음</dd>
					<dd>할인쿠폰 : 10% coupon / 1,000 plus coupon / APP전용 5% plus coupon / 오프라인매장전용 10% coupon</dd>
				</dl>
			</div>
		</div>
		<!-- //제로투세븐 회원 혜택 -->

	</div>
			
