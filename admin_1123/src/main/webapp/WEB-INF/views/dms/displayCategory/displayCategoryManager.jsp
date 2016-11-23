<%--
	화면명 : 전시 관리 > 전시 카테고리 관리
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/dms.app.display.category.manager.js"></script>

<article class="con_box con_on" ng-app="displayCategoryApp" ng-controller="dms_displayCategoryManagerApp_controller as dpCtrl">
	<!-- ### //카테고리 목록 ### -->
	<h2 class="sub_title1">전시카테고리 관리</h2>
	<div class="category">
		<button type="button" class="btn_type2 btn_type2_gray2" ng-click="dpCtrl.register()">
			<b><spring:message code="c.dmsDisplaycategory.insert" /></b>
		</button>

		<ul class="list_dep">
			<li class="dep{{category.depth}} {{category.lastNodeYn=='Y'?'end': category.icon }}" ng-repeat="category in trees" ng-show="(category.depth <=1 || category.show=='Y')?true:false">
				<button ng-click="openFolder($index, category.icon)" type="button"></button>
				<a href="javascript:void(0)" class="{{category.active}}" ng-click="getCategoryDetail($event, category);">{{category.name}}</a>
			</li>
		</ul>
	</div>

	<div class="columnR"><%-- tab영역 --%>
		<ul class="tab_type2" ng-if="selectedCategory">
			<li ng-class="tab1"><a href="javascript:void(0)" ng-click="tabClick('tab1')"><button type="button"><spring:message code="c.pmsCategory.basicInfo"/></button></a>
			</li>
			<li ng-class="tab2" ng-show="selectedCategory.templateId"><a href="javascript:void(0)" ng-click="tabClick('tab2')"><button type="button"><spring:message code="c.dmsDisplaycategory.cornerInfo"/></button></a>
			</li>
		</ul>
		
		<form name="form">
			<ng-view></ng-view>	
		</form>
	
	</div>
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>