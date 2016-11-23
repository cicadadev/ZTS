<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<link rel="stylesheet" type="text/css" href="/resources/css/jquery-ui.css" />.
<%
boolean isSsecure = request.isSecure();
request.setAttribute("_httpUrl", "http://");
if(isSsecure){
	request.setAttribute("_httpUrl", "https://");
}
%>
<link rel="stylesheet" type="text/css" href="/resources/css/nanumgothic.css" />
<link rel="stylesheet" type="text/css" href="/resources/css/common.css" />
<link rel="stylesheet" href="/resources/css/swiper.min.css" />
	

