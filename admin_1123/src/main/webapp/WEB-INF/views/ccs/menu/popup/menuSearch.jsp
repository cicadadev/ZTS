<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>
<div class="wrap_popup" style="min-width:500px;" data-ng-app="ccsAppPopup" data-ng-controller="categoryCtrl as ctrl" data-ng-init="ctrl.tree('dms')">
	<h1 class="sub_title1">카테고리 선택</h1>
	<div class="category" style="position:static; width:498px; border-top:none;">
		
		<ul class="list_dep">
			<li class="dep{{category.depth}} {{category.leafYn == 'Y' ? 'end' : category.icon }}" 
				data-ng-repeat="category in categories"
				data-ng-show="(category.depth == 1 || category.show == 'Y') ? true : false">
				
				<button data-ng-click="ctrl.openFolder($index, category.icon, 'pms')" type="button"></button>
				<span ng-if="category.leafYn == 'Y'">
					<a href="javascript:void(0);" data-ng-click="ctrl.selectCategory($event, category, 'dms')">{{category.name}}</a>
				</span>
				<span ng-if="category.leafYn != 'Y'">
					{{category.name}}
				</span>
			</li>
		</ul>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>

