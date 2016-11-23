<%--
	화면명 : 제휴사 접근의 게이트웨이
	작성자 : eddie
 --%>
<%@ page language="java" contentType="text/html; charset=euc-kr" pageEncoding="euc-kr"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>    
<%@ page import="gcp.common.util.FoSessionUtil" %>    
    
<%
	//	request.setAttribute("channelId", request.getParameter("channelId"));

	String channelId = (String) request.getSession().getAttribute("CHANNEL_ID");
	//System.out.println("====================channelId=="+channelId);
	request.setAttribute("channelId", channelId);
%>    
    
    

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=euc-kr">
<title>제로투세븐닷컴</title>
</head>


<c:if test="${not empty channelId }">
	<c:if test="${channelId eq '0011' }">
			<frameset rows="120,*" frameborder=0 framespacing=0 border="false">
			<frame noresize name="frame1" src="/affliator/shinhanHeader.jsp" scrolling=no marginwidth='0' marginheight=0 noresize >
			<frame noresize name="frame2" src="/ccs/common/main" scrolling=auto marginheight=0 noresize>
			</frameset>
			<noframes>
			<body>
			</body>
			</noframes>				
	</c:if>		
	<c:if test="${channelId eq '0012' }">
			<frameset rows="120,*" frameborder=0 framespacing=0 border="false">
			<frame noresize name="frame1" src="/affliator/hanaHeader.jsp" scrolling=no marginwidth='0' marginheight=0 noresize >
			<frame noresize name="frame2" src="/ccs/common/main" scrolling=auto marginheight=0 noresize>
			</frameset>
			<noframes>
			<body>
			</body>
			</noframes>			
	</c:if>
	<c:if test="${channelId eq '0013' }">
			<frameset rows="120,*" frameborder=0 framespacing=0 border="false">
			<frame noresize name="frame1" src="/affliator/kbHeader.jsp" scrolling=no marginwidth='0' marginheight=0 noresize >
			<frame noresize name="frame2" src="/ccs/common/main" scrolling=auto marginheight=0 noresize>
			</frameset>
			<noframes>
			<body>
			</body>
			</noframes>					
	</c:if>		
			
	
</c:if>

</html>
