<%--
	화면명 : 딜 관리
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/sps.app.deal.list.js"></script>

<article class="con_box" ng-app="dealApp" ng-controller="sps_dealListApp_controller as ctrl">
	<h2 class="sub_title1">딜 관리</h2>
	<div class="box_type1">
		<h3 class="sub_title2">
			<spring:message code="c.spsDeal.list"/> <!-- 딜 목록 -->
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_deal.data.length }}" /></span>
		</h3>
		
		<div class="gridbox" >
			<div class="grid" ui-grid="grid_deal"
				data-ui-grid-move-columns 
					data-ui-grid-resize-columns 
					data-ui-grid-auto-resize 
					data-ui-grid-selection 
					data-ui-grid-row-edit
					data-ui-grid-cell-nav
					data-ui-grid-exporter
					data-ui-grid-edit 
					data-ui-grid-validate
			></div>
		</div>
	</div>
</article>

<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>