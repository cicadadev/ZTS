<%--
	화면명 : 상품 로케이션 매핑 업데이트 팝업
	작성자 : brad
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/oms.app.location.mapping.js"></script>

<div class="wrap_popup" ng-app="locationMappingApp" data-ng-controller="locationMappingDetailCtrl as ctrl" ng-init="ctrl.init()">

	<form name="form">
		<h2 class="sub_title1"><spring:message code="c.oms.logistics.location.mapping.modify"><!-- 상품로케이션매핑수정 --></spring:message></h2>
		
		<div class="box_type1">
			<h3 class="sub_title2">로케이션 설정</h3>
	
			<table class="tb_type1">
				<colgroup>
					<col width="15%" />
					<col width="15%" />
					<col width="8%" />
					<col width="15%" />
				</colgroup>
				<tbody>
					<tr>
						<th>현재 로케이션명</th>
						<td colspan="3" style="text-align:center">
							<div class="gridbox gridbox200">
			   					<div class="grid" data-ui-grid="saleproduct_grid"   
									data-ui-grid-move-columns 
									data-ui-grid-resize-columns 
									data-ui-grid-auto-resize 
									data-ui-grid-selection 
									data-ui-grid-row-edit
									data-ui-grid-cell-nav
									data-ui-grid-exporter
									data-ui-grid-edit 
									data-ui-grid-validate>
								</div>								
							</div>							
						</td>
					</tr>
					<tr>
						<th>변경 로케이션명</th>
						<td colspan="3">
							<select style="width: 30%" ng-model="locationId" ng-options="location.locationId as location.locationId for (idx, location) in locationList" v-key="required">
								<option value="">전체</option>
							</select>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="btn_alignC">
			<button type="button" data-ng-click="ctrl.close()" class="btn_type3 btn_type3_purple">
				<b>닫기</b>
			</button>
			<button type="button" data-ng-click="ctrl.save()" class="btn_type3 btn_type3_purple">
				<b>저장</b>
			</button>
		</div>
	</form>	

</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>

