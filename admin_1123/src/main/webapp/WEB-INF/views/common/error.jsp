<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib prefix="spring" uri="http://www.springframework.org/tags"%>
<%
	// ajax 가 아닐경우 에러페이지 로딩
	if(!"Y".equals(request.getHeader("Ajax"))){
		response.setStatus(200);
%>

<!DOCTYPE html>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Error</title>
</head>
<body>
	<h3>Error</h3>
	<div>message : <c:out value="${requestScope['javax.servlet.error.message']}"/></div>
	<br/>
	<input type="button" value="back" onclick="javascript:history.back();" />
</body>
</html>
<%
	}
%>
