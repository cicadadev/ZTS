<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true" />
<link rel="stylesheet" type="text/css" href="/resources/css/oms.css" />
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>

<style>
input[type="text"] {
	height: 16px !important;
}
textarea {
	margin-left: 0px;
	padding-top: 4px;
}
</style>
<div class="wrap_popup" ng-app="ccsAppPopup" ng-controller="orderPopupCtrl as ctrl">
	<!-- ### 1번 탭 ### -->
	<article class="con_box">
		<form name="searchForm">
		<!-- ### 주문 조회 ### -->
		<h2 class="sub_title1">
			주문상품검색
		</h2>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="9%" />
					<col width="*" />
					<col width="9%" />
					<col width="20%" />
				</colgroup>
	
				<tbody>
					<tr>
						<th>상품주문일<i><spring:message code="c.input.required" /></i></th>
						<td>
							<input type="text" ng-model="search.startDate" max-date="{{search.endDate}}" datetime-picker period-start date-only />
							~
							<input type="text" ng-model="search.endDate" min-date="{{search.startDate}}" datetime-picker period-end date-only  required/>
							<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0"/>
						</td>
						<th rowspan="4" class="alignC"><spring:message code="c.oms.order.id" /></th>
						<td rowspan="4">
							<textarea cols="10" rows="7" ng-model="search.orderId" search-area></textarea>
						</td>
					</tr>
					<%--
					<tr>
						<th>회원정보</th>
						<td>
							<input type="text" id="" value="" placeholder="" ng-model="search.memSearchWord" />
							<button type="button" class="btn_type2" ng-click="ctrl.searchUser('member')">
								<b><spring:message code="c.search.btn.search" /></b>
							</button>
							<button type="button" class="btn_eraser" ng-click="ctrl.eraser('memSearchWord')">지우개</button>
						</td>
					</tr>
					<tr>
						<th>주문번호</th>
						<td><input type="text" ng-model="search.orderId" /></td>
					</tr>
					--%>
					<tr>
						<th>주문자<i><spring:message code="c.input.required" /></i></th>
						<td>
							<select ng-model="search.ordererType" ng-init="search.ordererType = 'name'">
								<option value="name">이름</option>
								<option value="id" >ID</option>
								<option value="mobile">휴대전화번호</option>
							</select>
							<input type="text" ng-model="search.orderer" required />
<!-- 							<input type="text" ng-model="search.orderer" /> -->
						</td>
					</tr>
					<tr>
						<th>상품정보</th>
						<td>
							<select ng-model="search.productType" ng-init="search.productType = 'name'">
								<option value="name">상품명</option>
								<option value="id">상품번호</option>
							</select>
							<input type="text" ng-model="search.product" />
						</td>
					</tr>
					<tr>
						<th>단품정보</th>
						<td>
							<select ng-model="search.saleProductType" ng-init="search.saleProductType = 'name'">
								<option value="name">단품명</option>
								<option value="id">단품번호</option>
							</select>
							<input type="text" ng-model="search.saleProduct" />
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
		</form>
	
		<!-- ### 주문 목록 ### -->
		<div class="box_type1 marginT3">
			<h3 class="sub_title2">
				주문상품목록
				<span><spring:message code="c.search.totalCount" arguments="{{ grid_order.totalItems }}" /></span>
			</h3>
			<div class="gridbox gridbox300">
				<div class="grid" ui-grid="grid_order" ui-grid-move-columns ui-grid-resize-columns ui-grid-auto-resize ui-grid-selection></div>
			</div>
		</div>
		<!-- ### //주문 목록 ### -->
	
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="func.popup.close()">
				<b><spring:message code="c.common.close" /></b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.select()">
				<b><spring:message code="c.common.select" /></b>
			</button>
		</div>

	</article>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true" />
