<%--
	화면명 : 딜 관리 > 딜 상품 관리 팝업 > 구분타이틀 depth 선택 팝업
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/sps.app.deal.list.js"></script>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="wrap_popup" style="min-width:500px;" ng-app="dealApp" ng-controller="sps_dealDivTitleTreePopApp_controller as ctrl" ng-init="ctrl.getDealGroupTree()">
	<h1 class="sub_title1"><spring:message code="c.spsDealproduct.divTitleDepth.choice" /> <!-- 구분타이틀 DEPTH 선택 --></h1>
	<div class="category" style="position:static; width:498px; border-top:none;">
		
		<ul class="list_dep">
			<li class="dep{{dealGroup.depth}} {{dealGroup.leafYn=='Y'?'end':dealGroup.icon }}" ng-repeat="dealGroup in dealGropTree" ng-show="(dealGroup.depth==1 || dealGroup.show=='Y')?true:false">
				<button ng-click="ctrl.openFolder($index, dealGroup.icon);" type="button"></button>
				<span>
					<a href="#none" ng-click="ctrl.selectDepth(dealGroup.depth, dealGroup.dealGroupNo, dealGroup.name, dealGroup.upperDealGroupNo, dealGroup.upperGroupName )">{{dealGroup.name}}</a>
				</span>
<!-- 				<span ng-if="dealGroup.leafYn != 'Y'"> -->
<!-- 					{{dealGroup.name}} -->
<!-- 				</span> -->
			</li>
		</ul>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>