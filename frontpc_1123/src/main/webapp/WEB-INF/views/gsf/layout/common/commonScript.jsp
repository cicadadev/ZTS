<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<% 
	pageContext.setAttribute("receipt_test_url",Config.getString("pg.receipt.script.test"));
%>
	<%-- script --%>
	<script type="text/javascript" src="/resources/js/jquery-1.12.0.min.js"></script>
	<script type="text/javascript" src="/resources/js/jquery.cookie.js"></script>
	<%-- datepicker --%>
	<script type="text/javascript" src="/resources/js/jquery-ui.min.js"></script>
	
	<script type="text/javascript" src="/resources/js/swiper.min.js"></script>
	<script type="text/javascript" src="/resources/js/jquery.transit.js"></script>		
	<script type="text/javascript" src="/resources/js/common/common.ui.js"></script>
	<script type="text/javascript" src="/resources/js/common/common.ui.${_deviceType }.js"></script>
	<!-- <script type="text/javascript" src="/resources/js/ui.js"></script> -->
	
	<script type="text/javascript" src="/resources/js/gsf.common.js"></script>
	
	<script type="text/javascript" src="/resources/js/sps/sps.common.js"></script>
	<script type="text/javascript" src="/resources/js/dms/dms.common.js"></script>
	<script type="text/javascript" src="/resources/js/dms/dms.exhibit.js"></script>
	<script type="text/javascript" src="/resources/js/dms/dms.special.js"></script>
	<script type="text/javascript" src="/resources/js/dms/dms.display.js"></script>
	<script type="text/javascript" src="/resources/js/dms/dms.brandTemplate.js"></script>
	<script type="text/javascript" src="/resources/js/ccs/ccs.common.js"></script>
	<script type="text/javascript" src="/resources/js/ccs/ccs.custcenter.js"></script>
	<script type="text/javascript" src="/resources/js/pms/pms.common.js"></script>
	<script type="text/javascript" src="/resources/js/oms/oms.common.js"></script>
	<script type="text/javascript" src="/resources/js/mms/mms.common.js"></script>
	
	<script type="text/javascript" src="/resources/js/${_deviceType }.js"></script>
	<script type="text/javascript" src="/resources/js/dms/dms.common.${_deviceType }.js"></script>
	
	<!-- 현금영수증 -->	
	<!-- TEST -->	
	<c:if test="${!empty receipt_test_url}">
	<script type="text/javascript" src="${receipt_test_url }"></script>
	</c:if>
	<!-- LG uplus -->
	<script type="text/javascript" src="//openapi.map.naver.com/openapi/v3/maps.js?clientId=<%=Config.getString("map.clientId")%>"></script>
	<script type="text/javascript" src="<%=Config.getString("pg.receipt.script")%>"></script>
	<script language="javascript" src="//xpay.lgdacom.net/xpay/js/xpay_crossplatform.js" type="text/javascript" charset="utf-8"></script>
	
	<!-- 카카오톡링크 / 카카오스토리공유 -->
	<script src="//developers.kakao.com/sdk/js/kakao.min.js"></script>
	<script>
		Kakao.init('21a0c8a9ac1cf3d700ba80a460cbe280');
	</script>
	
	<!-- KAKAO pay -->
		
	<!-- OpenSource Library -->
	<script src="https://pg.cnspay.co.kr:443/dlp/scripts/lib/easyXDM.min.js" type="text/javascript"></script>
	<script src="https://pg.cnspay.co.kr:443/dlp/scripts/lib/json3.min.js" type="text/javascript"></script>
	
	<!-- DLP창에 대한 KaKaoPay Library -->
	<script src="https://kmpay.lgcns.com:8443/js/dlp/client/kakaopayDlpConf.js" charset="utf-8"></script>
	<script src="https://kmpay.lgcns.com:8443/js/dlp/client/kakaopayDlp.min.js" charset="utf-8"></script>
	<div id="kakaopay_layer"  style="display: none"></div>
