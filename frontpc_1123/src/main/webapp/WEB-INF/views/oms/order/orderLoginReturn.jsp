<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%

%>
<!DOCTYPE html>
<html lang="ko">
<jsp:include page="/WEB-INF/views/gsf/layout/common/commonVariable.jsp" />	
<jsp:include page="/WEB-INF/views/gsf/layout/common/commonScript.jsp" />
<head>
<script type="text/javascript">
$(document).ready(function(){		
	
	//페이지에서 이동시.
// 	oms.submitOrder();
	
	//팝업에서 이동시.
	if(global.channel.isMobile == "true"){
		oms.submitOrder();
	}else{
		window.opener.oms.submitOrder();
		window.close();
	}

})
</script>
</head>
<body>
<form id="_orderForm" action="/oms/order/sheet" method="post">
<input type="hidden" id="orderLoginReturn" name="orderLoginReturn" value="true"/>
</form>

</body>
</html>