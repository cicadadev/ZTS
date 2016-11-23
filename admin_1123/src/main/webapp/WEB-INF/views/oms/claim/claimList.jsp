<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true" />
<link rel="stylesheet" type="text/css" href="/resources/css/oms.css" />
<script type="text/javascript" src="/resources/js/app/oms.app.claim.list.js"></script>

<article class="con_box con_on" ng-app="claimListApp" ng-controller="listCtrl as ctrl">
	<!-- ### 주문 조회 ### -->
	<h2 class="sub_title1">
		<spring:message code="c.oms.claim.search" />
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
					<th>클레임신청일</th>
					<td>
						<input type="text" ng-model="search.startDate" max-date="{{search.endDate}}" datetime-picker period-start date-only  width="150px"/>
						~
						<input type="text" ng-model="search.endDate" min-date="{{search.startDate}}" datetime-picker period-end date-only  width="150px"/>
						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only  calendar-button init-button="1"/>
					</td>
					<th rowspan="5"><spring:message code="c.oms.order.id" /></th>
					<td rowspan="5">
						<textarea cols="10" rows="5" ng-model="search.orderId" search-area style="margin-left: 2px;"></textarea>
					</td>					
				</tr>
				<tr>
					<th>클레임유형</th>
					<td>
						<checkbox-list ng-model="search.claimType" custom="CLAIM_TYPE_CD" all-check></checkbox-list>
					</td>
				</tr>
				<tr>
					<th>클레임상태</th>
					<td>
						<checkbox-list ng-model="search.claimState" custom="CLAIM_STATE_CD" all-check></checkbox-list>
					</td>
					<%--
					<th rowspan="2" class="alignC"><spring:message code="c.oms.claim.id" /></th>
					<td rowspan="2">
						<textarea cols="10" rows="3" ng-model="search.claimNo" search-area></textarea>
					</td>						
					--%>
				</tr>
				<tr>
					<th>입고상태</th>
					<td>
<!-- 						<checkbox-list ng-model="search.claimProductState" code-group="CLAIM_PRODUCT_STATE_CD" all-check></checkbox-list> -->
						<checkbox-list ng-model="search.claimProductState" custom="RETURN_STATUS" all-check></checkbox-list>
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
			<spring:message code="c.oms.claim.list" /><%-- 주문 목록 --%>
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_claim.totalItems }}" /></span>
		</h3>
		<div class="gridbox">
			<div class="grid" ui-grid="grid_claim" 
				ui-grid-move-columns ui-grid-resize-columns 
				ui-grid-pagination ui-grid-auto-resize
				ui-grid-selection ui-grid-edit 
				ui-grid-row-edit ui-grid-validate></div>
		</div>
	</div>
	<!-- ### //주문 목록 ### -->
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="false" />
