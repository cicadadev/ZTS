<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript">
<!--
$(document).ready(function(){
	var req = location.href;
	req = req.substring(req.indexOf("mypage"));
	var type = req.split("/");
	
	$('.lnb ul li').removeClass('on');
	if ((location.href).indexOf("?") != -1) {
		var temp = '<%=request.getParameter("pageType")%>';
		$("."+temp).addClass("on");
	} else {
		$("."+type[2]).addClass("on");
	}
});
//-->
</script>
<c:if test="${isMobile ne 'true' || param.isMain eq 'Y'}">

<!-- mypage_menu -->
<c:choose>
	<c:when test="${premiumYn eq 'Y' and employeeYn eq 'Y' }">
		<div class="lnb lnb_menu2ea">
	</c:when>
	<c:when test="${premiumYn eq 'N' and employeeYn eq 'N' }">
		<div class="lnb">
	</c:when>
	<c:otherwise>
		<div class="lnb lnb_menu1ea">
	</c:otherwise>
</c:choose>

	<h2>
		<a href="/mms/mypage/main">
			<img src="/resources/img/pc/txt/my0to7.gif" alt="마이쇼핑" />
		</a>
	</h2>
	<dl>
		<dt>
			<a href="javascript:void(0);">MY 주문관리</a>
		</dt>
		<dd>
			<ul>
<!-- 				<li><a href="javascript:void(0);" onclick="ccs.link.go('/mms/mypage/order/history', true, 1);">주문내역</a></li> -->
				<li><a href="/mms/mypage/order/history">주문내역</a></li>
				<li><a href="/mms/mypage/claim/history">취소/반품/교환 신청</a></li>
				<li><a href="/mms/mypage/regular/history">정기배송관리</a></li>
<%-- 				<li><a href="javascript:void(0);" onclick="common.pageMove('${pageScope.id}','','/mms/mypage/')order/addr">배송지관리</a></li> --%>
				<li class="gift"><a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.order.gift();">선물함 관리</a></li>
				<li class="offline"><a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.order.offline();">오프라인 구매내역</a></li>
			</ul>
		</dd>
	</dl>
	<dl>
		<dt>
			<a href="javascript:void(0);">MY 혜택관리</a>
		</dt>
		<dd>
			<ul>
				<li class="membership"><a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.benefit.membership();">멤버십 혜택</a></li>
				<li class="carrot"><a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.benefit.carrot();">당근</a></li>
				<li class="coupon"><a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.benefit.coupon();">쿠폰</a></li>
<%-- 				<li><a href="javascript:void(0);" onclick="common.pageMove('${pageScope.id}','','/mms/mypage/benefit/point')">매일 포인트</a></li> --%>
				<li class="deposit"><a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.benefit.deposit();">예치금</a></li>
			</ul>
		</dd>
	</dl>
	
	<c:if test="${ premiumYn eq 'Y'}">
		<div class="lnb_menu mem">
			<a href="javascript:void(0);" onclick="ccs.link.special.premium();"><em>프리미엄관</em></a>
		</div>
	</c:if>
	<c:if test="${ employeeYn eq 'Y'}">
		<div class="lnb_menu staff">
			<a href="javascript:void(0);" onclick="ccs.link.special.employee();"><em>임직원관</em></a>
		</div>
	</c:if>
	
	<dl>
		<dt>
			<a href="javascript:void(0);">MY 활동관리</a>
		</dt>
		<dd>
			<ul>
				<li class="wishlist" ><a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.activity.wishlist();">쇼핑찜</a></li>
				<c:if test="${!isMobile }">
					<li class="latestProduct" ><a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.activity.latestProduct();">최근 본 상품</a></li>
				</c:if>
				<li class="review" ><a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.activity.review();">상품평</a></li>				
				<li class="MYQA" ><a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.activity.inquiry('MYQA');">1:1 상담내역</a></li>
				<li class="PRODUCT" ><a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.activity.inquiry('PRODUCT');">상품 Q&amp;A</a></li>
				<li class="clothesAs" ><a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.activity.as();">의류 AS현황</a></li>
				<li class="event"><a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.activity.event();">이벤트 참여내역</a></li>
				<li class="style"><a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.activity.style();">스타일 관리</a></li>
			</ul>
		</dd>
	</dl>
	<dl>
		<dt>
			<a href="javascript:void(0);">MY 정보관리</a>
		</dt>
		<dd>
			<ul>
				<li class="offshop"><a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.info.offshop();">관심매장 설정</a></li>
				<li class="interest"><a href="javascript:void(0);" onclick="javascript:mypage.main.goInterest();">아이정보/관심정보 관리</a></li>
				<li class="receipt"><a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.info.receipt();">영수증조회</a></li>
				<li class="refund"><a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.info.refund();">환불계좌관리</a></li>	
				<li class="deliveryAddress"><a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.info.deliveryAddress();">배송지 관리</a></li>
				<li class=""><a href="javascript:void(0);" onclick="mms.common.memberUpdatePopup()" target="_blank">회원정보 수정</a></li>
				<li class=""><a href="javascript:void(0);" onclick="mms.common.memberPwChangePopup()">비밀번호 변경</a></li>
				<li class=""><a href="javascript:void(0);" onclick="mms.common.memberMktAgreePopup()">이메일/문자 수신 관리</a></li>
				<li class=""><a href="javascript:void(0);" onclick="mms.common.memberDisagreePopup()">제로투세븐 회원탈퇴</a></li>
			</ul>
		</dd>
	</dl>
</div>
</c:if>