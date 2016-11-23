<%--
	화면명 : 회원관리 > 오프라인 주문 정보
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/mms.app.member.manager.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="wrap_popup"  ng-app="memberManagerApp" data-ng-controller="mms_memberOfflineOrderPopApp_controller as ctrl">
	<jsp:include page="/WEB-INF/views/mms/member/popup/memberTab.jsp" flush="true"/>

	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col width="15%" />
				<col width="*%" />
			</colgroup>
			<tbody>
				<tr>
					<th>주문일자</th><!-- 주문일자 -->
					<td>		
						<input type="text" data-ng-model="search.startDate" value="" placeholder="" datetime-picker date-only period-start/>											
						~
						<input type="text" data-ng-model="search.endDate" value="" placeholder="" datetime-picker date-only period-end/>
						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0" />										
					</td>
				</tr>
				<tr>
					<th>매장명</th>
					<td>
						<input type="text" data-ng-model="search.offshopName" placeholder="" style="width:20%;" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" class="btn_type1" data-ng-click="ctrl.reset()">
			<b><spring:message code="c.search.btn.reset"/><!-- 초기화 --></b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" data-ng-click="myGrid.loadGridData()">
			<b><spring:message code="c.search.btn.search"/><!-- 검색 --></b>
		</button>
	</div>

	<div class="box_type1 marginT3">
		<h3 class="sub_title2">
			오프라인 구매내역
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_memberOfflineOrder.totalItems }}" /></span>
		</h3>
		
		<div class="gridbox gridbox300">
			<div class="grid" data-ui-grid="grid_memberOfflineOrder" 
					data-ui-grid-move-columns 
					data-ui-grid-resize-columns 
					data-ui-grid-pagination
					data-ui-grid-auto-resize 
					data-ui-grid-selection 
					data-ui-grid-row-edit
					data-ui-grid-cell-nav
					data-ui-grid-exporter
					data-ui-grid-edit 
					data-ui-grid-validate></div>
		</div>
	</div>
	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /><!-- 닫기 --></b>
		</button>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>