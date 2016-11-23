<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ page import="gcp.frontpc.common.contants.Constants"%>
<%
	pageContext.setAttribute("skyScraperRgt", Config.getString("corner.common.banner.img.3") );
%>

<!-- ### pc 전용 sky 메뉴 ### -->
<div class="sky_scraper">
	<ul class="list">
	
	</ul>

	<div class="rolling_banner sky_banner" id="corner${skyScraperRgt}">
		<div class="swiper-container comSwiper_quickBanner">
			<ul class="swiper-wrapper img">
			</ul>
			
			<!-- Add Pagination -->
			<div class="swiper-pagination tp1 al_c"></div>
		</div>
		
		<ol class="control" id="corner${skyScraperRgt}Paging">
		</ol>
	</div>

	<a href="#none" class="btn_up">맨 위로..</a> 
	<a href="#none" class="btn_down">맨 아래로..</a>
</div>
<!-- ### //pc 전용 sky 메뉴 ### -->