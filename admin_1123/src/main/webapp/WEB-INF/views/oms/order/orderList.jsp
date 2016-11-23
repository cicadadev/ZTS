<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true" />
<link rel="stylesheet" type="text/css" href="/resources/css/oms.css" />
<script type="text/javascript" src="/resources/js/app/oms.app.order.list.js"></script>

<article class="con_box con_on" ng-app="orderListApp" ng-controller="listCtrl as ctrl">
	<!-- ### 주문 조회 ### -->
	<h2 class="sub_title1">
		<spring:message code="c.oms.order.search" />
	</h2>
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col width="9%" />
<!-- 				<col width="30%" /> -->
<!-- 				<col width="9%" /> -->
				<col width="*" />
				<col width="9%" />
				<col width="20%" />
			</colgroup>

			<tbody>
				<tr>
					<th>주문일자</th>
					<td>
						<input type="text" ng-model="search.startDate" max-date="{{search.endDate}}" datetime-picker period-start date-only width="150px"/>
						~
						<input type="text" ng-model="search.endDate" min-date="{{search.startDate}}" datetime-picker period-end date-only width="150px"/>
						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button="B" init-button="0"/>
					</td>
					<th rowspan="7"><spring:message code="c.oms.order.id" /></th>
					<td rowspan="7">
						<textarea cols="10" rows="9" ng-model="search.orderId" search-area ></textarea>
					</td>
				</tr>
				<tr>
					<th>주문구분</th>
					<td>
						<form name="test">
							<checkbox-list ng-model="search.orderType" code-group="ORDER_TYPE_CD" all-check></checkbox-list>
						</form>
					</td>
				</tr>
				<tr>
					<th>주문상태</th>
					<td>
						<checkbox-list ng-model="search.orderState" code-group="ORDER_STATE_CD" all-check></checkbox-list>
					</td>
				</tr>
				<tr>
					<th>배송상태</th>
					<td>
						<checkbox-list ng-model="search.orderDeliveryState" code-group="ORDER_DELIVERY_STATE_CD" all-check></checkbox-list>
					</td>
				</tr>
				<tr>
					<th>구매채널</th>
					<td>
						<checkbox-list ng-model="search.deviceType" code-group="DEVICE_TYPE_CD" all-check></checkbox-list>
					</td>
				</tr>
				<tr>
					<th>주문자</th>
					<td>
						<select ng-model="search.ordererType" ng-init="search.ordererType = 'name'">
							<option value="name">이름</option>
							<option value="id" >ID</option>
							<option value="mobile">휴대전화번호</option>
						</select>
						<input type="text" ng-model="search.orderer" />
					</td>
				</tr>
				<tr>
					<th>수취인</th>
					<td>
						<select ng-model="search.receiverType" ng-init="search.receiverType = 'name'">
							<option value="name">이름</option>
							<option value="mobile">휴대전화번호</option>
						</select>
						<input type="text" ng-model="search.receiver" />
					</td>
				</tr>
				<tr>
					<th>사이트</th>
					<td>
						<select ng-model="search.siteId" ng-options="site.siteId as site.name for (idx, site) in siteList">
							<option value="">전체</option>
						</select>
					</td>
					<th>제휴주문번호</th>
					<td>
						<input type="text" ng-model="search.siteOrderId" />
					</td>
				</tr>
				<tr>
					<th>상품번호</th>
					<td>
						<input type="text" ng-model="search.productId" />
					</td>
					<th>상점ID</th>
					<td>
						<input type="text" ng-model="search.pgShopId" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" ng-click="search.reset()" class="btn_type1">
			<b><spring:message code="c.search.btn.reset" /></b>
		</button>
		<button type="button" ng-click="list.search()" class="btn_type1 btn_type1_purple">
			<b><spring:message code="c.search.btn.search" /></b>
		</button>
	</div>
	<!-- ### //주문 조회 ### -->

	<!-- ### 주문 목록 ### -->
	<div class="box_type1 marginT3">
		<h3 class="sub_title2">
			<spring:message code="c.oms.order.list" /><%-- 주문 목록 --%>
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_order.totalItems }}" /></span>
		</h3>
		<div class="gridbox">
			<div class="grid" ui-grid="grid_order" 
				ui-grid-move-columns ui-grid-resize-columns 
				ui-grid-pagination ui-grid-auto-resize 
				ui-grid-selection></div>
		</div>
	</div>
	<!-- ### //주문 목록 ### -->
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="false" />
