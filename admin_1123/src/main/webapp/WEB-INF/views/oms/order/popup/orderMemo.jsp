<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%-- <%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %> --%>
<%-- <%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %> --%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true" />
<script type="text/javascript" src="/resources/js/app/oms.app.order.etc.js"></script>

<div class="wrap_popup" ng-app="orderEtcApp" ng-controller="memoCtrl as ctrl" ng-init="ctrl.init()">
	<h1 class="sub_title1">주문메모</h1>

	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col width="15%" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr>
					<th>주문번호</th>
					<th><p>{{orderId}}</p></th>
				</tr>
			</tbody>
		</table>
		<table class="tb_type1" style="table-layout: fixed;word-break:break-all;">
			<colgroup>
				<col width="15%" />
				<col width="32%" />
				<col width="15%" />
				<col width="*" />
			</colgroup>
			<tbody ng-repeat="memo in memoList track by $index">
				<tr>
					<th>담당자</th>
					<td>{{memo.insName}}&nbsp;( ID : {{memo.insId}} )</td>
					<th>등록일시</th>
					<td>{{memo.insDt}}</td>
				</tr>
				<tr>
					<th>처리사항</th>
					<td colspan="3">
						<pre>{{memo.detail}}</pre>
<!-- 						<xmp>{{memo.detail}}</xmp> -->
<!-- 						{{memo.detail}} -->
					</td>
				</tr>
				</div>
			</tbody>
			<tbody>
				<form name="memoForm">
					<tr>
						<th>담당자</th>
						<td>${loginInfo.loginName}&nbsp;( ID : ${loginInfo.loginId} )</td>
						<th>등록일시</th>
						<td></td>
					</tr>
					<tr>
						<th>처리사항</th>
						<td colspan="3">
							<textarea rows="5" cols="40" ng-model="omsOrderMemo.detail" style="margin-top: 1px;"></textarea>
<!-- 							<textarea rows="5" cols="40" ng-model="omsOrderMemo.detail"></textarea> -->
						</td>
					</tr>
				</form>
			</tbody>
		</table>
	</div>

	<div class="btn_alignC" style="margin-top:20px;">
		<button type="button" class="btn_type1" ng-click="func.popup.close()">
			<b>취소</b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" ng-click="ctrl.insert()" >
			<b>저장</b>
		</button>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true" />