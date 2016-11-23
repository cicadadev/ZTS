<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true" />
<link rel="stylesheet" type="text/css" href="/resources/css/oms.css" />
<script type="text/javascript" src="/resources/js/app/oms.app.refund.list.js"></script>

<article class="con_box con_on" ng-app="refundListApp" ng-controller="listCtrl as ctrl">
	<!-- ### 주문 조회 ### -->
	<h2 class="sub_title1">
		<spring:message code="c.oms.refund.search" />
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
					<th>환불접수일</th>
					<td>
						<input type="text" ng-model="search.startDate" max-date="{{search.endDate}}" datetime-picker period-start date-only  width="150px"/>
						~
						<input type="text" ng-model="search.endDate" min-date="{{search.startDate}}" datetime-picker period-end date-only  width="150px"/>
						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0"/>
					</td>
					<th rowspan="5"><spring:message code="c.oms.order.id" /></th>
					<td rowspan="5">
						<textarea cols="10" rows="6" ng-model="search.orderId" search-area></textarea>
					</td>
				</tr>
				<tr>
					<th>환불상태</th>
					<td>
						<checkbox-list ng-model="search.paymentState" code-group="PAYMENT_STATE_CD" show-only="5,6,7" all-check></checkbox-list>
					</td>
				</tr>
				<tr>
					<th>환불사유</th>
					<td>
						<checkbox-list ng-model="search.refundReason" code-group="REFUND_REASON_CD" all-check></checkbox-list>
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
					<th>환불계좌</th>
					<td>
						<select ng-model="search.accountType" ng-init="search.accountType = 'name'">
							<option value="name">예금주</option>
							<option value="bank" >환불은행</option>
							<option value="number">환불계좌</option>
						</select>
						<input type="text" ng-model="search.account" />
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
		<button type="button" class="btn_type1 " ng-click="func.popup.registration()">
			<b>환불등록</b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" ng-click="list.update('REFUND_CANCEL','환불취소')">
			<b>환불취소</b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" ng-click="list.update('REFUND','환불완료')" fn-id="54_COMPLETE">
			<b>환불완료</b>
		</button>
	</div>
	
	<!-- ### 주문 목록 ### -->
	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.oms.refund.list" /><%-- 주문 목록 --%>
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_refund.totalItems }}" /></span>
		</h3>
		<div class="gridbox">
			<div class="grid" ui-grid="grid_refund" 
				ui-grid-move-columns ui-grid-resize-columns 
				ui-grid-pagination ui-grid-auto-resize
				ui-grid-selection></div>
		</div>
	</div>
	<!-- ### //주문 목록 ### -->
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="false" />
