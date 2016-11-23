<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true" />
<link rel="stylesheet" type="text/css" href="/resources/css/oms.css" />
<script type="text/javascript" src=/resources/js/app/oms.app.claim.etc.js></script>

<div class="wrap_popup" ng-app="claimEtcApp" ng-controller="optionCtrl as ctrl" ng-init="ctrl.init()">
	<!-- ### 1번 탭 ### -->
	<article class="con_box">
		<h2 class="sub_title1">
			옵션 변경
		</h2>
	
		<!-- ### 배송지 목록 ### -->
		<div class="box_type1 marginT3">
			<h3 class="sub_title2">
				단품 목록
				<span><spring:message code="c.search.totalCount" arguments="{{ grid_option.totalItems }}" /></span>
			</h3>
			<div class="gridbox">
				<div class="grid" ui-grid="grid_option" 
					ng-style="{height: (grid_option.totalItems*30)+30+'px'}"
					ui-grid-move-columns 
					ui-grid-resize-columns 
					ui-grid-auto-resize 
					ui-grid-selection></div>
			</div>
		</div>
		<!-- ### //배송지 목록 ### -->
		
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="func.popup.close()">
				<b><spring:message code="c.common.close" /></b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.save()">
				<b><spring:message code="c.common.select" /></b>
			</button>
<!-- 			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.save()"> -->
<%-- 				<b><spring:message code="c.common.save" /></b> --%>
<!-- 			</button> -->
		</div>

	</article>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true" />
