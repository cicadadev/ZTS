<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ page import="gcp.frontpc.common.contants.Constants"%>
<%
	pageContext.setAttribute("skyScraperLeft", Config.getString("corner.common.banner.img.2") );
%>
<!-- ### pc 동그란 배너 : 2016.08.17 추가 ### -->
<!-- pc 동그란 배너 주석해제  -->
<div class="ad_banner" id="corner${skyScraperLeft}">
	<!-- 스크립트 HTML -->
</div>
<!-- ### //pc 동그란 배너 : 2016.08.17 추가 ### -->