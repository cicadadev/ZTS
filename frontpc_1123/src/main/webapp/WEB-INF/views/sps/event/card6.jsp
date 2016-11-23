<%--
	화면명 : 카드사 제휴 - 신한카드 신한고운맘/KidsPlus 카드
	작성자 : eddie
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<c:choose>
	<c:when test="${isMobile eq 'true'}">
		<div class="mo_navi">
			<button type="button" class="btn_navi_prev" onclick="parent.history.back();">이전 페이지로..</button>
			<h2>이벤트</h2>
		</div>
	</c:when>
	<c:otherwise>
		<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
			<jsp:param value="이벤트" name="pageNavi"/>
		</jsp:include>
	</c:otherwise>
</c:choose>
<script>

var shinhanAuth = function(){
	ccs.layer.copyLayerToContentChild("ly_kidsplus");
}

function gounmom(){
	ccs.layer.close('ly_kidsplus-layer');
	var Future = "fullscreen=no,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no,left=0,top=0,width=370px,height=550px";
	window.open("http://www.shinhancard.com/hpp/HPPEXTRN/extBtyMomCardInput.shc?customer_no=20","신한카드인증",Future);
}

function kidsplus(){
	ccs.layer.close('ly_kidsplus-layer');
	var Future = "fullscreen=no,toolbar=no,location=no,directories=no,status=no,menubar=no,scrollbars=no,resizable=no,left=0,top=0,width=370px,height=550px";
	window.open("http://www.shinhancard.com/hpp/HPPEXTRN/extKidPlusCardInput.shc?customer_no=21","신한카드인증",Future);			
}


</script>
<div class="inner eventWrap">
				
	<div class="psCardWrap">
		<ul class="pscTab">
			<li><a href="/sps/event/card?cno=1"><img src="/resources/img/pc/event/partnershipCard/psCard_tab01.png" alt="하나카드 하나멤버스 1Q카드"></a></li>
			<li><a href="/sps/event/card?cno=2" class=""><img src="/resources/img/pc/event/partnershipCard/psCard_tab02.png" alt="비씨카드 BC국민행복카드"></a></li>
			<li><a href="/sps/event/card?cno=3" class=""><img src="/resources/img/pc/event/partnershipCard/psCard_tab03.png" alt="롯데카드 롯데국민행복카드"></a></li>
			<li><a href="/sps/event/card?cno=5" class=""><img src="/resources/img/pc/event/partnershipCard/psCard_tab04.png" alt="롯데카드 롯데아이행복카드"></a></li>
			<li><a href="/sps/event/card?cno=4" class=""><img src="/resources/img/pc/event/partnershipCard/psCard_tab05.png" alt="롯데카드 롯데카드 육아클럽"></a></li>
			<li class="active"><a href="/sps/event/card?cno=6" class=""><img src="/resources/img/pc/event/partnershipCard/psCard_tab06.png" alt="신한카드 신한고운맘/KidsPlus 카드"></a></li>
		</ul>
		<div class="pscCont contArea06">
			<div class="pscTxtD pscTxt01">
				<span class="img_pc"><img src="/resources/img/pc/event/partnershipCard/psCard_img06_01.png" alt="신한 고운맘 카드 &amp; 신한 Kids Plus 카드"></span>
				<span class="img_mc"><img src="/resources/img/mobile/event/partnershipCard/psCard_img06_01.png" alt="신한 고운맘 카드 &amp; 신한 Kids Plus 카드"></span>
				<!-- btn area begin -->
				
				<!-- // btn area end -->
						</div>
						<div class="pscTxtD pscTxt02">
							<span class="img_pc"><img src="/resources/img/pc/event/partnershipCard/psCard_img06_02.png" alt="1. VIP 멤버십 혜택  
2. 신한 고운맘카드 혜택
3. 신한 Kids Plus카드 혜택"></span>
							<span class="img_mc"><img src="/resources/img/mobile/event/partnershipCard/psCard_img06_02.png" alt="1. VIP 멤버십 혜택  
2. 신한 고운맘카드 혜택
3. 신한 Kids Plus카드 혜택"></span>
							<!-- btn area begin -->
				<a href="/sps/event/benefit" class="btnDef btn_psc01" target="blank">
				<span class="img_pc"><img src="/resources/img/pc/event/partnershipCard/pscCont01_btn05.png" alt="VIP 혜택보기"></span>
				<span class="img_mc"><img src="/resources/img/mobile/event/partnershipCard/pscCont01_btn05.png" alt="VIP 혜택보기"></span>
				</a>
				<a href="#none" class="btnDef btn_psc02" onclick="viewLayer(shinhanAuth);">
				<span class="img_pc"><img src="/resources/img/pc/event/partnershipCard/pscCont06_btn01.png" alt="VIP 혜택받기"></span>
				<span class="img_mc"><img src="/resources/img/mobile/event/partnershipCard/pscCont06_btn01.png" alt="VIP 혜택받기"></span>
				</a>
				<!-- // btn area end -->
			</div>
		</div>
	</div>

</div>
<div class="pop_wrap ly_kidsplus" style="display:none;">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">SHINHAN CARD</h3>
		</div>
		<div class="pop_content">
			<dl>
				<dt><strong>신한 고운맘 카드 / 신한 Kids Plus카드<br>회원을 위한 카드 인증</strong></dt>
				<dd>신한 고운맘카드, 신한 Kids카드 회원님은 카드 인증절차를 거친 후 쿠폰 발급이 가능하며, 인증받은 카드로만 결제가 가능합니다.<br>소지하신 카드의 인증버튼을 눌러주세요.</dd>
			</dl>
			<div class="btn_wrapC btn2ea">
				<a href="#none" class="btn_mStyle1 sPurple1"  onclick="gounmom()">신한 고운맘 카드 인증</a>
				<a href="#none" class="btn_mStyle1 sPurple1" onclick="kidsplus()">신한 Kids Plus 카드 인증</a>
			</div>
		</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>

	<jsp:include page="/WEB-INF/views/sps/event/inner/cardNoLayer.jsp" flush="false">
		<jsp:param value="${cno }" name="cardType"/>
	</jsp:include>		