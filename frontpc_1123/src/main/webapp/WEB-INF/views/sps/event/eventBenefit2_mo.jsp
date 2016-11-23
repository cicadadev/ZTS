<%--
	화면명 : 이벤트 제로투세븐 멤버 혜택(인증후 모바일화면)
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
<script>

var couponIdStr =  '<%=Config.getString("member.benefit.couponIds."+gradeLower)%>';
var arrs = couponIdStr.split('|');
var couponIds = [];

var tacControl = function(){
	
	/* 탭 공통 : 2016.07.27 추가 */
	$(".tabBox a, .tabBox1 a, .tabBox2 a").off("click").on("click", function(e){
		var idx = $(this).parent().index();
		var parent_ul;

		if( $(this).closest("ul").hasClass("tabBox") ){
			parent_ul = $(this).closest(".tabBox");
		}else if( $(this).closest("ul").hasClass("tabBox1") ){
			parent_ul = $(this).closest(".tabBox1");
		}else{
			parent_ul = $(this).closest(".tabBox2");
		}

		$(this).parent().addClass("on").siblings("li").removeClass("on");
		$(parent_ul).siblings(".tab_con").eq(idx).addClass("tab_conOn").siblings(".tab_con").removeClass("tab_conOn");

		//e.preventDefault();
	});
	
	
}
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

var infoLayer = function(){
	ccs.layer.copyLayerToContentChild("ly_memBenefit");
	tacControl();
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
<script>
var d = new Date();
</script>

<div class="inner">
	<!-- 멤버십 혜택 -->
	<div class="mem_benefit_section">
		<div class="mybenefit_txt">
			<div class="grade_info">
				<!-- 등급별
					<span class="mem_grade vip">VIP</span>
					<span class="mem_grade gold">GOLD</span>
					<span class="mem_grade silver">SILVER</span>
					<span class="mem_grade family">FAMILY</span>
					<span class="mem_grade welcome">WELCOME</span>
				 -->
				<span class="mem_grade ${gradeLower }"></span>
				<div>
					<p><script>document.write('' + (d.getMonth() + 1));</script>월 ${loginName } 님의 회원등급</p>
					<strong><tags:codeName code="${memGradeCd }"/></strong>					
				</div>
			</div>
		
			<div class="grade_info2">
				<p>최근 6개월 구매금액 :  <span><fmt:formatNumber value="${totalAmt }" groupingUsed="true" /></span>원<br>(오프라인 매장에서 구매한 내역까지 합산한 금액입니다.)</p>
				<p>다음달 예상 등급은  <tags:codeName code="${pGradeCd }"/> 입니다.</p>
				<c:choose>
				<c:when test="${nextGradeCd ne myGradeCd and nextGradeCd eq 'MEM_GRADE_CD.FAMILY' }"><%--다음 예상등급이 현재보다 낮을경우 --%>
					<p>다음달'FAMILY' 등급이 되시려면 구매이력이 필요합니다.</p>
				</c:when>				
				<c:when test="${nextGradeCd ne myGradeCd and remainAmt > 0 }"><%--다음 안내 등급이 현재보다 높을경우 --%>
					<p>다음달 <tags:codeName code="${nextGradeCd }"/> 등급이 되시려면 남은 구매금액: <span><fmt:formatNumber value="${remainAmt }" groupingUsed="true" /></span>원 </p> 
				</c:when>
				<c:when test="${nextGradeCd eq myGradeCd and remainAmt > 0}"><%--다음 안내 등급이 현재와 같거나 낮을 경우 --%>
					<p>다음달 <tags:codeName code="${memGradeCd }"/> 등급을 유지하시려면 <span><fmt:formatNumber value="${remainAmt }" groupingUsed="true" /></span>원 더 구매하셔야 합니다.</p>
				</c:when>

				</c:choose>
			</div>
			
			
		</div>

		<div class="btn_wrapC btn2ea">
			<a href="#none" class="btn_sStyle3 sWhite1" onclick="infoLayer();">등급별 혜택 자세히보기</a>
			<a href="#none" onclick="ccs.link.special.premium()" class="btn_sStyle3 sPurple1">멤버십관 입장하기</a>
		</div>
		
		<div class="mem_coupon_box">
			<h3><tags:codeName code="${memGradeCd }"/> 회원 쿠폰 혜택</h3>
			<div class="couponImg">
				<img src="/resources/img/mobile/txt/coupon_${gradeLower }.gif" alt="10% coupon / 1,000 plus coupon / APP전용 5% plus coupon / free 배송비 할인">
			</div>
			<!-- 2016.11.02 수정 start(태그추가) -->
			<div class="couponImg offline">
				<img src="/resources/img/mobile/txt/coupon_${gradeLower }_offLine.gif" alt="오프라인매장전용 10% coupon / 오프라인매장전용 10% coupon/">
			</div>
			<!-- //2016.11.02 수정 end (태그추가) -->
			<div class="dwn_conpon">
				<span>사용기간 : 2016/8/1 ~ 2016/8/31</span>
				<a href="#none" class="btn_sStyle3 sPurple1" onclick="couponDownLogin()";>쿠폰 전체 받기 &gt;</a>
			</div>
		</div>

		<!-- 2016.11.02 수정 start -->
		<div class="mem_coupon_box relay">

			<h3>한번 더 혜택! 릴레이쿠폰</h3>
			<ul class="mem_coupon">
				<li class="on">
					<!-- 오픈전  -->
					<div class="coupon_openBefore">
						<div class="txtInfo">
							<span class="fcolor">매월 둘째주 월요일 10시!</span> 
							<span>우수회원을 위한 릴레이 쿠폰이 오픈됩니다.</span>
						</div>
					</div>
					<!-- //오픈전  -->
					<p class="tit">
						<strong>1차 OPEN</strong>
						<span>VIP , GOLD 우수회원에게만 한번 더 드립니다.</span>
					</p>
					<div class="couponImg">
						<img src="/resources/img/mobile/txt/coupon_${gradeLower }_relay1.gif" alt="APP전용 1,000 plus coupon">
					</div>
					<div class="dwn_conpon">
						<span>사용기간 : 2016/8/1 ~ 2016/8/31</span>
						<a href="#" class="btn_sStyle3 sPurple1">쿠폰 받기 &gt;</a>
					</div>
				</li>
				<li class="on">
					<!-- 오픈전  -->
					<div class="coupon_openBefore">
						<div class="txtInfo">
							<span class="fcolor">셋째주 월요일 10시!</span> 
							<span>2차 쿠폰이 오픈됩니다.</span>
						</div>
					</div>
					<!-- //오픈전  -->

					<p class="tit">
						<strong>2차 OPEN</strong>
						<span>8월 중 구매 고객에게 드립니다.</span>
					</p>
					<div class="couponImg">
						<img src="/resources/img/mobile/txt/coupon_${gradeLower }_relay2.gif" alt="APP전용 1,000 plus coupon / 2,500 배송비할인">
					</div>
					<div class="dwn_conpon">
						<span>사용기간 : 2016/8/1 ~ 2016/8/31</span>
						<a href="#" class="btn_sStyle3 sPurple1">쿠폰 받기 &gt;</a>
					</div>
				</li>
				<li class="on last">
					<!-- 오픈전  -->
					<div class="coupon_openBefore">
						<div class="txtInfo">
							<span class="fcolor">넷째주 월요일 10시!</span> 
							<span>3차 쿠폰이 오픈됩니다.</span>
						</div>
					</div>
					<!-- //오픈전  -->

					<p class="tit">
						<strong>3차 OPEN</strong>
						<span>8월 중 구매 고객에게 드립니다.</span>
					</p>
					<div class="couponImg">
						<img src="/resources/img/mobile/txt/coupon_${gradeLower }_relay3.gif" alt="APP전용 2,000 plus coupon / 2,500 배송비할인">
					</div>
					<div class="dwn_conpon">
						<span>사용기간 : 2016/8/1 ~ 2016/8/31</span>
						<a href="#" class="btn_sStyle3 sPurple1">쿠폰 받기 &gt;</a>
					</div>
				</li>
			</ul>

		</div>
		<!-- //2016.11.02 수정 end -->

		<div class="benefit_notice">
			<strong>0to7 회원 등급선정 및 쿠폰 안내</strong>
			<ul>
				<li>0to7회원은 최근 6개월간 온, 오프라인 구매 누적 금액을 기준으로 매월 1일에 선정됩니다. (오프라인 결제완료, 온라인 주문 배송완료 기준 / 취소, 반품 주문 제외)</li>
				<li>구매누적금액은 할인쿠폰 적용금액을 제외한 결제금액을 기준으로 합니다. (포인트+예치금+실결제금액)</li>
				<li>회원등급이 처리되는 매월 1일 00시~03시 사이에는 쿠폰이 다운로드 되지 않습니다.</li>
				<li>모든 쿠폰은 제로투세븐닷컴 직접 방문인 경우에만 사용가능하며, APP전용 쿠폰은 모바일APP설치 후 APP에서만 사용가능합니다.</li>
				<li>모든 쿠폰은 쇼킹제로, 멤버십관 상품에는 적용되지 않으며 일부 행사상품 및 특가상품에도 적용되지 않을 수 있습니다.</li>
				<li>쿠폰별 사용조건 및 쿠폰 유효기간은 마이쇼핑&gt; 쿠폰내역에서 확인 가능합니다.</li>
				<li class="emph">모든 혜택은 당사 사정에 의해 사전 예고 없이 변경 또는 중지 될 수 있으며, 부당한 방법으로 획득한 고객등급은 심사 후 재조정될 수 있습니다.</li>
			</ul>
		</div>

	</div>
	<!-- //멤버십 혜택 -->

</div>

<div class="pop_wrap ly_memBenefit" style="display:none;">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">제로투세븐 등급별 혜택 보기</h3>
		</div>
		<div class="pop_content">
			<ul class="tabBox2">
				<li class="on"><a href="#none">VIP</a></li>
				<li><a href="#none">GOLD</a></li>
				<li><a href="#none">SILVER</a></li>
				<li><a href="#none">FAMILY</a></li>
				<li><a href="#none">WELCOME</a></li>
			</ul>
			<div class="tab_con tab_conOn">
				<div class="mybenefit_txt">
					<div class="grade_info">
						<!-- 등급별
							<span class="mem_grade vip">VIP</span>
							<span class="mem_grade gold">GOLD</span>
							<span class="mem_grade silver">SILVER</span>
							<span class="mem_grade family">FAMILY</span>
							<span class="mem_grade welcome">WELCOME</span>
						 -->
						<span class="mem_grade vip">VIP</span>
						<div>
							<strong>VIP</strong>
							<p>최근 6개월 누적구매금액 60만원 이상<br>(쇼핑몰, 오프라인 매장 구매금액 합산)</p>
						</div>
					</div>
				</div>

				<div class="mem_coupon_box">
					<h3>VIP 회원 쿠폰 혜택</h3>
					<div class="couponImg">
						<img src="/resources/img/mobile/txt/coupon_vip_pop.gif" alt="">
					</div>
				</div>

				<div class="mem_coupon_box">
					<h3>구매 후 받기! 릴레이 쿠폰 혜택</h3>
					<p>구매 후 한번 더 할인! 구매고객님께 한번 더 드립니다.</p>
					<div class="couponImg">
						<img src="/resources/img/mobile/txt/coupon_vip_relay_pop.gif" alt="">
					</div>
				</div>
			</div>
			<div class="tab_con">
				<div class="mybenefit_txt">
					<div class="grade_info">
						<span class="mem_grade gold">GOLD</span>
						<div>
							<strong>GOLD</strong>
							<p>최근 6개월 누적구매금액 30만원 이상~ 60만원 미만 (쇼핑몰, 오프라인 매장 구매금액 합산)</p>
						</div>
					</div>
				</div>

				<div class="mem_coupon_box">
					<h3>GOLD 회원 쿠폰 혜택</h3>
					<div class="couponImg">
						<img src="/resources/img/mobile/txt/coupon_gold_pop.gif" alt="">
					</div>
				</div>

				<div class="mem_coupon_box">
					<h3>구매 후 받기! 릴레이 쿠폰 혜택</h3>
					<p>구매 후 한번 더 할인! 구매고객님께 한번 더 드립니다.</p>
					<div class="couponImg">
						<img src="/resources/img/mobile/txt/coupon_gold_relay_pop.gif" alt="">
					</div>
				</div>
			</div>
			<div class="tab_con">
				<div class="mybenefit_txt">
					<div class="grade_info">
						<span class="mem_grade silver">SILVER</span>
						<div>
							<strong>SILVER</strong>
							<p>최근 6개월 누적구매금액 30만원 이상~ 60만원 미만 (쇼핑몰, 오프라인 매장 구매금액 합산)</p>
						</div>
					</div>
				</div>

				<div class="mem_coupon_box">
					<h3>SILVER 회원 쿠폰 혜택</h3>
					<div class="couponImg">
						<img src="/resources/img/mobile/txt/coupon_gold_pop.gif" alt="SILVER 회원 쿠폰 혜택">
					</div>
				</div>

				<div class="mem_coupon_box">
					<h3>구매 후 받기! 릴레이 쿠폰 혜택</h3>
					<p>구매 후 한번 더 할인! 구매고객님께 한번 더 드립니다.</p>
					<div class="couponImg">
						<img src="/resources/img/mobile/txt/coupon_gold_relay_pop.gif" alt="SILVER 회원 릴레이 쿠폰 혜택">
					</div>
				</div>
			</div>

			<div class="tab_con">
				<div class="mybenefit_txt">
					<div class="grade_info">
						<span class="mem_grade family">FAMILY</span>
						<div>
							<strong>FAMILY</strong>
							<p>최근 6개월 누적구매금액 30만원 이상~ 60만원 미만 (쇼핑몰, 오프라인 매장 구매금액 합산)</p>
						</div>
					</div>
				</div>

				<div class="mem_coupon_box">
					<h3>FAMILY 회원 쿠폰 혜택</h3>
					<div class="couponImg">
						<img src="/resources/img/mobile/txt/coupon_family_pop.gif" alt="FAMILY 회원 쿠폰 혜택">
					</div>
				</div>

				<div class="mem_coupon_box">
					<h3>구매 후 받기! 릴레이 쿠폰 혜택</h3>
					<p>구매 후 한번 더 할인! 구매고객님께 한번 더 드립니다.</p>
					<div class="couponImg">
						<img src="/resources/img/mobile/txt/coupon_family_relay_pop.gif" alt="FAMILY 회원 릴레이 쿠폰 혜택">
					</div>
				</div>
			</div>

			<div class="tab_con">
				<div class="mybenefit_txt">
					<div class="grade_info">
						<span class="mem_grade welcome">WELCOME</span>
						<div>
							<strong>WELCOME</strong>
							<p>최근 6개월 누적구매금액 30만원 이상~ 60만원 미만 (쇼핑몰, 오프라인 매장 구매금액 합산)</p>
						</div>
					</div>
				</div>

				<div class="mem_coupon_box">
					<h3>WELCOME 회원 쿠폰 혜택</h3>
					<div class="couponImg">
						<img src="/resources/img/mobile/txt/coupon_welcome_pop.gif" alt="WELCOME 회원 쿠폰">
					</div>
				</div>
			</div>
		</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>