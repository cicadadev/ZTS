<%--
	화면명 : 프론트 메인 > 모바일 앱
	작성자 : roy
 --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@page import="gcp.common.util.FoSessionUtil"%>
<%@page import="gcp.frontpc.common.util.FrontUtil"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="intune.gsf.common.utils.Config" %>

<%
	String deviceType = FoSessionUtil.getDeviceTypeCd(request);
	pageContext.setAttribute("deviceType", deviceType );

	// AppStore URL
	pageContext.setAttribute("androidAppStore", Config.getString("android.app.url") );
	pageContext.setAttribute("iosAppStore", Config.getString("ios.app.url") );
%>
	
<script type="text/javascript">
$(document).ready(function() {

});

function appInstallLink() {
	if ('${deviceType}' == 'DEVICE_TYPE_CD.MW'){
		if(navigator.userAgent.toLowerCase().indexOf("android") > -1){
		    window.location.href = '${androidAppStore}';
		}
		if(navigator.userAgent.toLowerCase().indexOf("iphone") > -1){
		    window.location.href = '${iosAppStore}';
		}
	}
}

</script>
	
<c:choose>
	<c:when test="${isMobile eq 'true'}">
		<script type="text/javascript" src="/resources/js/mms/mms.mypage.js"></script>	
		<div class="mo_navi">
			<button type="button" class="btn_navi_prev" onclick="parent.history.back();">이전 페이지로..</button>
			<h2>모바일 이용안내</h2>
		</div>
	</c:when>
	<c:otherwise>
		<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
			<jsp:param value="이벤트" name="pageNavi"/>
		</jsp:include>
	</c:otherwise>
</c:choose>

<div class="inner eventWrap">
	<!-- eventWrap begin -->
	<div class="inner eventWrap">
		
		<div class="psCardWrap">
			<div class="pscCont contArea01">
				<div class="pscTxtD pscTxt01">
					<span class="img_mc"><img src="/resources/img/mobile/event/openEvent/mo_banner_01.png" alt="더 빨라지고 더 편리해진 Oto7.com 을 즐겨보세요~!"></span>
				</div>
				<div class="pscTxtD pscTxt02">
					<span class="img_mc"><img src="/resources/img/mobile/event/openEvent/openEvent_img01_01.png" alt="새단장! Welcome 장바구니 쿠폰 혜택"></span>
<!-- TODO 맞춤설정 링크달기, 이미지 교체 -->
<c:choose>
	<c:when test="${deviceType == 'DEVICE_TYPE_CD.MW'}">
					<!-- btn area begin -->
					<a href="javascript:mypage.main.goInterest();" class="btnDef btn_oet01" target="_self">
						<span class="img_mc"><img src="/resources/img/mobile/event/openEvent/oetCont01_btn01.png" alt="맞춤정보 설정하기"></span>
					</a>
					<!-- // btn area end -->
					<a href="javascript:appInstallLink();" class="btnDef btn_oet02" target="_self">
						<span class="img_mc"><img src="/resources/img/mobile/event/openEvent/oetCont01_btn02.png" alt="APP 설치하기"></span>
					</a>
	</c:when>
	<c:otherwise>
					<a href="javascript:mypage.main.goInterest();" class="btnDef btn_oet03" >
						<img src="/resources/img/mobile/event/openEvent/openEvent_img01_03.png" alt="맞춤정보 설정하기">
					</a>
	</c:otherwise>
</c:choose>
					
				</div>
				
				<div class="newServiceList_wrap">
					<h3><img src="/resources/img/mobile/event/openEvent/h3_newService.png" alt="0to7.com 새로운 서비스 둘러보기!" /></h3>

					<ul class="newServiceList">
						<li><a href="javascript:ccs.link.special.subscription();"><img src="/resources/img/mobile/event/openEvent/h3_newService_01_n.png" alt="정기배송관"></a></li>
						<li><a href="/ccs/common/main#5"><img src="/resources/img/mobile/event/openEvent/h3_newService_02_n.png" alt="샵ON"></a></li>
						<li><a href="javascript:ccs.link.special.giftShop();"><img src="/resources/img/mobile/event/openEvent/h3_newService_03_n.png" alt="기프트샵"></a></li>
						<li><a href="javascript:ccs.link.special.birthready();"><img src="/resources/img/mobile/event/openEvent/h3_newService_04_n.png" alt="출산준비관"></a></li>
						<li><a href="javascript:ccs.link.special.milkPowder();"><img src="/resources/img/mobile/event/openEvent/h3_newService_05_n.png" alt="분유관"></a></li>
						<li><a href="/ccs/common/main#6"><img src="/resources/img/mobile/event/openEvent/h3_newService_06_n.png" alt="스타일샵"></a></li>
						<li><a href="/dms/common/templateDisplay?brandId=500359"><img src="/resources/img/mobile/event/openEvent/h3_newService_07_n.png" alt="브랜드관"></a></li>
					</ul>
				</div>
			</div>
		</div>
	</div>
	<!-- // eventWrap end -->

</div>