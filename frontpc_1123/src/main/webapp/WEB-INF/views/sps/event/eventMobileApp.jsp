<%--
	화면명 : 프론트 메인 > 모바일 앱
	작성자 : roy
 --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ page import="intune.gsf.common.utils.Config" %>
<%
	pageContext.setAttribute("tommeetippe", Config.getString("brand.tommeetippe.code"));
	pageContext.setAttribute("allo", Config.getString("brand.allo.code"));
	pageContext.setAttribute("alfonso", Config.getString("brand.alfonso.code"));
	pageContext.setAttribute("fourlads", Config.getString("brand.fourlads.code"));
	pageContext.setAttribute("skarbarn", Config.getString("brand.skarbarn.code"));
	pageContext.setAttribute("royal", Config.getString("brand.royal.code"));
	pageContext.setAttribute("chooze", Config.getString("brand.chooze.code"));
	pageContext.setAttribute("yvol", Config.getString("brand.yvol.code"));
%>
<script type="text/javascript">
$(document).ready(function() {

});

function mobileAppSend(){
	var phone = $('#mobileAppPhone').val();
	ccs.common.mobileAppSend.send(phone, function(callback) {
		
	});
}

</script>
	
<c:choose>
	<c:when test="${isMobile ne 'true'}">
		<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
			<jsp:param value="이벤트" name="pageNavi"/>
		</jsp:include>	
	</c:when>
	<c:otherwise>	
		<jsp:include page="/WEB-INF/views/gsf/layout/page/mo/title_menu.jsp" flush="false" >
			<jsp:param name="titleMenu" value="6" />
		</jsp:include>
	</c:otherwise>	
</c:choose>

<!-- <div class="inner"> -->
	<!-- newOpenEvent_wrap begin -->
	<div class="newOpenEvent_wrap">
		<div class="section_wrap">
			<div class="section eventHeader">
				0to7.com이 완전~새로워졌습니다! 더 빨라지고 더 편리해진 쇼핑서비스를 즐겨보세요~!
			</div>
			<div class="section eventList_wrap">
				<ul class="eventList">
					<li class="no1">
						<p class="txt_info">분유, 기저귀 정기적으로 배송 받기! 정기배송관</p>
						<a href="javascript:ccs.link.special.subscription();" class="btn_shortcuts"><img src="/resources/img/pc/event/openEvent/btn_newService_02.png" alt="바로가기"></a>
					</li>
					<li class="no2">
						<p class="txt_info">온/오프라인 내 맘대로 이용~! 매장픽업관</p>
						<a href="javascript:ccs.link.special.pickup();" class="btn_shortcuts"><img src="/resources/img/pc/event/openEvent/btn_newService_03.png" alt="바로가기"></a>
					</li>
					<li class="no3">
						<p class="txt_info">이모/삼촌도 손쉽게 선물사기~! 기프트샵</p>
						<a href="javascript:ccs.link.special.giftShop();" class="btn_shortcuts"><img src="/resources/img/pc/event/openEvent/btn_newService_04.png" alt="바로가기"></a>
					</li>
					<li class="no4">
						<p class="txt_info">출산준비 필수템 모임! 출산준비관</p>
						<a href="javascript:ccs.link.special.birthready();" class="btn_shortcuts"><img src="/resources/img/pc/event/openEvent/btn_newService_05.png" alt="바로가기"></a>
					</li>
					<li class="no5">
						<p class="txt_info">우리아이에게 맞는 분유는 뭐? 분유관</p>
						<a href="javascript:ccs.link.special.milkPowder();" class="btn_shortcuts"><img src="/resources/img/pc/event/openEvent/btn_newService_06.png" alt="바로가기"></a>
					</li>
					<li class="no6">
						<p class="txt_info">우리아이 스타일 꾸미기! 스타일샵</p>
						<a href="javascript:ccs.link.display.style();" class="btn_shortcuts"><img src="/resources/img/pc/event/openEvent/btn_newService_07.png" alt="바로가기"></a>
					</li>
					<li class="no7">
						<p class="txt_info">제로투세븐 인기브랜드가 한눈에~! 브랜드관</p>
						<!-- 1114 -->
						<a href="javascript:brand.template.main('${allo}');" class="btn_bg btn_brand01">알로앤루</a>
						<a href="javascript:brand.template.main('${alfonso}');" class="btn_bg btn_brand02">알퐁소</a>
						<a href="javascript:brand.template.main('${royal}');" class="btn_bg btn_brand03">궁중비책</a>
						<a href="javascript:brand.template.main('${tommeetippe}');" class="btn_bg btn_brand04">토미티피</a>
						<a href="javascript:brand.template.main('${fourlads}');" class="btn_bg btn_brand05">포레즈</a>
						<a href="javascript:brand.template.main('${skarbarn}');" class="btn_bg btn_brand06">skarbarn</a>
						<a href="javascript:brand.template.main('${chooze}');" class="btn_bg btn_brand07">츄즈</a>
						<a href="javascript:brand.template.main('${yvol}');" class="btn_bg btn_brand08">Y볼루션</a>
				
						<!-- <a href="#none" class="btn_shortcuts"><img src="/resources/img/pc/event/openEvent/btn_newService_08.png" alt="바로가기"></a> -->
					</li>
				</ul>
			</div>
			<div class="section onlyMobile">오직 모바일에서만! 모바일 APP전용 Welcome 장바구니 제공</div>
			<div class="section nowGoGo">
				<!-- APP 다룬로드 문자 받기 -->
				<div class="appDownload">
					<div class="input_wrap"><input type="text" id="mobileAppPhone"></div>
					<div class="btn_wrap"><button onclick="mobileAppSend();"><img src="/resources/img/pc/event/openEvent/btn_smsSend.png" alt="문자보내기"></button></div>
				</div>
				<!-- //APP 다룬로드 문자 받기 -->

				<!-- QR코드 -->
				<div class="qrCode"><img src="/resources/img/pc/event/openEvent/qrcode_01.png" alt="QR코드"></div>
				<!-- //QR코드 -->
			</div>
		</div>
		
	</div>
	<!-- // newOpenEvent_wrap end -->
	
<!-- </div> -->