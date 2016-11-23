<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/sps.app.event.list.js"></script>

<div class="wrap_popup" ng-app="eventApp" ng-controller="sps_eventJoinHistoryPopApp_controller as ctrl">
	<div class="box_type1 marginT1">
		<h3 class="sub_title2"><spring:message code="c.sps.eventJoin.joinHist" /></h3>
<!-- 		<div class="tb_type1">
		</div> -->
		<table class="tb_type1">
			<colgroup>
				<col width="25%" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr>
					<th>블로그주소</th>
					<td>{{ spsEventjoin.url }}</td>
				</tr>
				<tr>
					<th>댓글내역</th>
					<td>{{ spsEventjoin.detail }}</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_white" ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /></b>
		</button>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>