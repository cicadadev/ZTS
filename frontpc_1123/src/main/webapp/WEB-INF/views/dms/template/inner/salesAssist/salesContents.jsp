<%--
	화면명 : 브랜드관 > 템플릿A
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%
	pageContext.setAttribute("lookbookBnr", Config.getString("corner.brand.templA.lookbook") );
	pageContext.setAttribute("coordiBnr", Config.getString("corner.brand.templA.coordi") );
	pageContext.setAttribute("coordiProduct", Config.getString("corner.brand.templA.coordi.product") );	
%>

<script type="text/javascript" src="/resources/js/common/common.ui.js"></script>
<script type="text/javascript" src="/resources/js/dms/dms.salesAssist.js"></script>
<script type="text/javascript" src="/resources/js/mms/mms.mypage.js"></script>
<script type="text/javascript">

$(document).ready(function() {
	// BO에서 등록한 HTML 제대로 그리기 위해 꼭 필요!
	sales.appendHtml();
	
	// 카탈로그/코디북 조회
	if (common.isEmpty($("#brandForm #directCatalogYn").val())) {
		sales.itemSearch('CATALOGUE');
	}
});
</script>

<div class="mainCon brandType ${brandClassName} brandA_01">
	<div class="inner brand_lookbook brandA_03" id="main3" >
		
		<div id="lookbook_list" >
		
		</div>
		<div id="lookbook_detail" style="display:none;">
		
		</div>
	</div>
</div>
