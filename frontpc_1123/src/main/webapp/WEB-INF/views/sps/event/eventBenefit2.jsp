<%--	
	화면명 : 이벤트 제로투세븐 멤버 혜택(인증후 PC화면)
	작성자 : eddie
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ page import="gcp.mms.model.custom.FoLoginInfo" %>
<%@ page import="gcp.common.util.FoSessionUtil" %>
<%
	FoLoginInfo loginInfo = null;
	if(FoSessionUtil.isMemberLogin()){
		loginInfo = (FoLoginInfo)FoSessionUtil.getLoginInfo(); 
	}
	
	String gradeLower = loginInfo.getMemGradeCd().replace("MEM_GRADE_CD.", "").toLowerCase();
	request.setAttribute("gradeLower", gradeLower);
%>
<script type="text/javascript">

	var couponIdStr =  '<%=Config.getString("member.benefit.couponIds."+gradeLower)%>';
	var arrs = couponIdStr.split('|');
	var couponIds = [];
	
	var couponDown = function(){
		sps.event.downloadMemberGradeCoupon(couponIdStr);
	}
	
	var couponDownLogin = function(){
		
		mms.common.loginCheck(function(result){
			if(result){
				couponDown();
			}
		});
	}
	
	var d = new Date();
	var year =  d.getFullYear();
	var month = d.getMonth() + 1;
	var date = d.getDate()
	
	var lastDay = ( new Date( year, month, 0) ).getDate();
	
	var startYear = year;
	var startMmonth = month - 6;
	
	if(startMmonth < 0){
		startYear = year -1;
		
		startMmonth = 12 + startMmonth;
	}
	
	if(startMmonth < 10){
		startMmonth="0"+startMmonth;
	}
	
	if(date < 10){
		date="0"+date;
	}
	
	
	
	$('document').ready(function(){
		//구매기간 : 2015-10-01~2016-02-28
		$('#orderPeriod').html("구매기간 : "+startYear+"-"+startMmonth+"-01~"+year+"-"+month+"-"+date)
		var remainDay = lastDay - date;
		$('#remainDay').html("남은 구매 가능 기간 : "+remainDay+"일");
	
	});

	
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
			<jsp:param value="이벤트" name="pageNavi"/>
		</jsp:include>
	</c:otherwise>
</c:choose>


<div class="inner">
				
	<!-- 제로투세븐 회원 혜택 -->
	<div class="mem_benefit_top">
		<span class="bg">제로투세븐 회원 혜택</span>
		<div class="mem_benefit_inner after">
			<div class="benefit_month"><span><script>document.write('' + (d.getFullYear()));</script></span>년 <span><script>document.write('' + (d.getMonth() + 1));</script></span>월</div>
			<div class="mybenefit_txt">
				<dl>
					<dt>	
						<span class="${gradeLower }"></span>
					</dt>
					<dd>
						<div class="my_current">
							<p><strong>${loginName }</strong> 님의 등급은 <strong><tags:codeName code="${memGradeCd }"/></strong>입니다.</p>
							<ul>
								<li id="orderPeriod"></li>
								<li>구매 누적 금액 : <strong><fmt:formatNumber value="${totalAmt }" groupingUsed="true" />원</strong></li>
							</ul>
							<span>* 구매 누적 금액은 오프라인 의류 매장에서 구매한 내역까지 합산한 금액입니다.</span>
						</div>
						<div class="my_next">
							<p>다음달 예상 등급은  <strong><tags:codeName code="${pGradeCd }"/> </strong>입니다.</p>
						<c:choose>
						<c:when test="${nextGradeCd ne myGradeCd and nextGradeCd eq 'MEM_GRADE_CD.FAMILY' }"><%--다음 예상등급이 현재보다 낮을경우 --%>
							<p>다음달<strong><tags:codeName code="${nextGradeCd }"/></strong> 등급이 되시려면</p>
							<ul>
								<li id="remainDay"></li>
								<li>구매이력이 필요합니다.</li>
							</ul>									
						</c:when>				
						<c:when test="${nextGradeCd ne myGradeCd and remainAmt > 0 }"><%--다음 안내 등급이 현재보다 높을경우 --%>
							<p>다음달<strong><tags:codeName code="${nextGradeCd }"/></strong>  등급이 되시려면</p> 
							<ul>
								<li id="remainDay"></li>
								<li>남은 구매 금액 : <fmt:formatNumber value="${remainAmt }" groupingUsed="true" />원</li>
							</ul>							
						</c:when>
						<c:when test="${nextGradeCd eq myGradeCd and remainAmt > 0}"><%--다음 안내 등급이 현재와 같거나 낮을 경우 --%>
							<p>다음달 <strong><tags:codeName code="${memGradeCd }"/></strong> 등급을 유지하시려면</p>
							<ul>
								<li id="remainDay"></li>
								<li>남은 구매 금액 : <fmt:formatNumber value="${remainAmt }" groupingUsed="true" />원</li>
							</ul>							
						</c:when>
						</c:choose>

						</div>
					</dd>
				</dl>
			</div>
		</div>
	</div>
	<div class="mem_benefit_info">
		<div class="blind">
			<dl>
				<dt>등급 : VIP</dt>
				<dd>최근 6개월 구매누적금액 : 60만원 이상 ~</dd>
				<dd>할인쿠폰 : 10% coupon / 1,000 plus coupon / APP전용 5% plus coupon / 2,500 배송비할인 / 오프라인매장전용 10% coupon</dd>
				<dd>릴레이쿠폰 : APP전용 10% coupon 2장 / APP전용 1,000 plus coupon / 2,500 배송비할인 2장</dd>
				<dd>멤버십관 혜택</dd>
				<dd>우수고객 혜택 : 의류 AS 무료 제공</dd>
			</dl>
			<dl>
				<dt>등급 : GOLD</dt>
				<dd>최근 6개월 구매누적금액 : 30만원 이상 ~ 60만원 미만</dd>
				<dd>할인쿠폰 : 10% coupon / 1,000 plus coupon / APP전용 5% plus coupon / 오프라인매장전용 10% coupon</dd>
				<dd>릴레이쿠폰 : APP전용 10% coupon / APP전용 1,000 plus coupon / 2,500 배송비할인</dd>
				<dd>멤버십관 혜택</dd>
				<dd>우수고객 혜택 : 의류 AS 무료 제공</dd>
			</dl>
			<dl>
				<dt>등급 : SILVER</dt>
				<dd>최근 6개월 구매누적금액 : 10만원 이상 ~ 30만원 미만</dd>
				<dd>할인쿠폰 : 10% coupon / 1,000 plus coupon / APP전용 5% plus coupon / 오프라인매장전용 10% coupon</dd>
				<dd>릴레이쿠폰 : 2,500 배송비할인</dd>
				<dd>멤버십관 혜택 : </dd>
				<dd>우수고객 혜택 : </dd>
			</dl>
			<dl>
				<dt>등급 : FAMILY</dt>
				<dd>최근 6개월 구매누적금액 : ~ 10만원 미만</dd>
				<dd>할인쿠폰 : 10% coupon / 1,000 plus coupon / APP전용 5% plus coupon / 오프라인매장전용 10% coupon</dd>
				<dd>릴레이쿠폰 : APP전용 1,000 plus coupon</dd>
				<dd>멤버십관 혜택 : </dd>
				<dd>우수고객 혜택 : </dd>
			</dl>
			<dl>
				<dt>등급 : WELCOME</dt>
				<dd>최근 6개월 구매누적금액 : 구매이력 없음</dd>
				<dd>할인쿠폰 : 10% coupon / 1,000 plus coupon / APP전용 5% plus coupon / 오프라인매장전용 10% coupon</dd>
				<dd>릴레이쿠폰 : </dd>
				<dd>멤버십관 혜택 : </dd>
				<dd>우수고객 혜택 : </dd>
			</dl>
		</div>
		`
		<!-- 한번에 받기 -->
		<div class="btn_dwn_all ${gradeLower }"> <!-- 등급별 class 추가(vip, gold, silver, family, welcome) -->
			<a href="#none" onclick="couponDownLogin();">한번에 받기</a>
		</div>
<!-- 
		릴레이 쿠폰받기
		<div class="btn_dwn_relay vip"> 등급별 class 추가(vip, gold, silver, family, welcome)
			<a href="#none">릴레이 쿠폰받기</a>
		</div> -->
	</div>

	<div class="mem_coupon_exp">
		<ul class="blind">
			<li>첫구매 감사 쿠폰 : 첫구매 고객에게 구매상품 배송완료 후 자동 발급됩니다.</li>
			<li>기념일 축하 쿠폰 : 아기백일, 아기생일, 본인생일 축하 쿠폰이 기념일 7일전 안내메일과 함께 자동 발급 됩니다.</li>
			<li>모바일 APP 첫만남 쿠폰 : 첫구매 고객에게 구매상품 배송완료 후 자동 발급됩니다.</li>
			<li>주말 쿠폰 : 금/토/일 딱! 3일간 3종 알뜰쿠폰 드립니다.</li>
		</ul>
	</div>

	<div class="benefit_notice">
		<strong>0to7 회원<br>등급선정 및 쿠폰 안내</strong>
		<ul>
			<li>0to7회원은 최근 6개월간 온, 오프라인 구매 누적 금액을 기준으로 매월 1일에 선정됩니다. (오프라인 결제완료, 온라인 주문 배송완료 기준/취소, 반품 주문 제외)</li>
			<li>구매누적금액은 할인쿠폰 적용금액을 제외한 결제금액을 기준으로 합니다. (포인트+예치금+실결제금액)</li>
			<li>회원등급이 처리되는 매월 1일 00시~03시 사이에는 쿠폰이 다운로드 되지 않습니다.</li>
			<li>모든 쿠폰은 제로투세븐닷컴 직접 방문인 경우에만 사용가능하며, APP전용쿠폰은 모바일APP설치 후 APP에서만 사용가능합니다.</li>
			<li>모든 쿠폰은 쇼킹제로, 멤버십관 상품에는 적용되지 않으며 일부 행사상품 및 특가상품에도 적용됟지 않을 수 있습니다.</li>
			<li>쿠폰별 사용조건 및 쿠폰 유효기간은 마이쇼핑&gt; 쿠폰내역에서 확인 가능합니다.</li>
			<li>모든 혜택은 당사 사정에 의해 사전 예고 없이 변경 또는 중지 될 수 있으며, 부당한 방법으로 획득한 고객등급은 심사 후 재조정될 수 있습니다.</li>
		</ul>
	</div>
	<!-- //제로투세븐 회원 혜택 -->


</div>