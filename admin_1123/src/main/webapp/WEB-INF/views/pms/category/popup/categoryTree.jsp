<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<article class="con_box con_on" data-ng-app="ccsAppPopup" data-ng-controller="categoryTreeCtrl as ctrl" data-ng-init="ctrl.tree()">

	<div class="category">
		<ul class="list_dep">
			<li class="dep{{category.depth}} {{category.leafYn == 'Y' ? 'end' : category.icon }}" data-ng-repeat="category in categories"
				data-ng-show="(category.depth == 1 || category.show == 'Y') ? true : false">
				<button data-ng-click="ctrl.openFolder($index, category.icon)" type="button"></button>
				<a href="#none" data-ng-click="ctrl.getCategoryAttributes($event, category)">{{category.name}}</a>
			</li>
		</ul>
	</div>

</article>

