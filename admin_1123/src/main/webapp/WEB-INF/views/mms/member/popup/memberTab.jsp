<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<ul class="tab_type2">
	<li ng-class="{'on' : memberDetailTab}">
		<button type="button" ng-click="ctrl.moveTab($event, 'detail')" name="detail">기본정보<!-- 기본정보 --></button>
	</li>
<!-- 	<li ng-class="{'on' : memberPointTab}"> -->
<!-- 		<button type="button" ng-click="ctrl.moveTab($event, 'point')" name="point">매일포인트 내역매일포인트 내역</button> -->
<!-- 	</li> -->
	<li ng-class="{'on' : memberCarrotTab}">
		<button type="button" ng-click="ctrl.moveTab($event, 'carrot')" name="carrot">당근내역<!-- 당근 내역 --></button>
	</li>
	<li ng-class="{'on' : memberCouponTab}">
		<button type="button" ng-click="ctrl.moveTab($event, 'coupon')" name="coupon">쿠폰내역<!-- 쿠폰 내역 --></button>
	</li>
	<li ng-class="{'on' : memberDepositTab}">
		<button type="button" ng-click="ctrl.moveTab($event, 'deposit')" name="deposit">예치금 내역<!-- 예치금 내역 --></button>
	</li>
	<li ng-class="{'on' : memberOnlineOrderTab}">
		<button type="button" ng-click="ctrl.moveTab($event, 'onlineOrder')" name="onlineOrder">온라인 주문내역<!-- 온라인 주문내역 --></button>
	</li>
	<li ng-class="{'on' : memberOfflineOrderTab}">
		<button type="button" ng-click="ctrl.moveTab($event, 'offlineOrder')" name="offlineOrder">오프라인 주문내역<!-- 오프라인 주문내역 --></button>
	</li>
</ul>
		
		