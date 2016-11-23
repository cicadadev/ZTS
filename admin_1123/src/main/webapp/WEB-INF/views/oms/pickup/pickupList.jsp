<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true" />
<link rel="stylesheet" type="text/css" href="/resources/css/oms.css" />
<script type="text/javascript" src="/resources/js/app/oms.app.pickup.list.js"></script>

<article class="con_box con_on" ng-app="pickupListApp" ng-controller="listCtrl as ctrl">
	<!-- ### 주문 조회 ### -->
	<h2 class="sub_title1">
		<spring:message code="c.oms.pickup.search" />
	</h2>
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col width="9%" />
				<col width="*" />
			</colgroup>

			<tbody>
				<tr>
					<th>신청일자</th>
					<td>
						<input type="text" ng-model="search.startDate" max-date="{{search.endDate}}" datetime-picker period-start date-only width="150px"/>
						~
						<input type="text" ng-model="search.endDate" min-date="{{search.startDate}}"  datetime-picker period-end date-only width="150px"/>
						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only max-date="{{search.endDate}}"  calendar-button="B" init-button="0"/>
					</td>
				</tr>
				<tr>
					<th>픽업상태</th>
					<td>
						<checkbox-list ng-model="search.pickupProductState" code-group="PICKUP_PRODUCT_STATE_CD" all-check></checkbox-list>
					</td>
				</tr>
				<tr>
					<th>신청자</th>
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
						<select ng-model="search.offShopType" ng-init="search.offShopType = 'id'">
							<option value="id">매장번호</option>
							<option value="name">매장명</option>
						</select>
						<input type="text" ng-model="search.offShop" />
					</td>
				</tr>
				<tr>
					<th>상품번호</th>
					<td>
						<input type="text" ng-model="search.productId" />
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
	
	<div class="btn_alignR marginT3">
		<button type="button" class="btn_type1 btn_type1_purple" ng-click="list.cancel()">
			<b>신청취소</b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" ng-click="list.save()">
			<b><spring:message code="common.btn.save" /></b>
		</button>
	</div>
	
	<!-- ### 주문 목록 ### -->
	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.oms.pickup.list" /><%-- 주문 목록 --%>
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_pickup.totalItems }}" /></span>
		</h3>
		<div class="gridbox">
			<div class="grid" ui-grid="grid_pickup" 
				ui-grid-move-columns ui-grid-resize-columns 
				ui-grid-pagination ui-grid-auto-resize
				ui-grid-selection ui-grid-edit 
				ui-grid-row-edit ui-grid-validate></div>
		</div>
	</div>
	<!-- ### //주문 목록 ### -->
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="false" />
