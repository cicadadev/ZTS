<%--
	화면명 : 업체 관리 > 업체 상세 > 업체 휴일정보 목록 조회
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.business.list.js"></script>


<div class="wrap_popup"  data-ng-app="businessApp" data-ng-controller="commissionListController as ctrl" data-ng-init="ctrl.commissionGridInit()">
	<h2 class="sub_title1"><spring:message code="c.ccsBusiness.detail"/> <!-- 업체 상세 --></h2>
		<ul class="tab_type2">
				<li>
					<button type="button" ng-click="ctrl.onClickTab('1')">기본정보</button>
				</li>
				<li >
					<button type="button" ng-click="ctrl.onClickTab('2')">사용자 정보</button>
				</li>
				<li>
					<button type="button" ng-click="ctrl.onClickTab('3')">배송 정보</button>
				</li>
				<li>
					<button type="button" ng-click="ctrl.onClickTab('4')">휴일 정보</button>
				</li>
				<li  class="on">
					<button type="button">수수료 정보</button>
				</li>
			</ul>
			
	<form name="form2">
		<!-- 업체 휴일 정보 -->
		<div  class="box_type1">
			<div class="btn_alignR"  style="margin-top:40px;">
				<button type="button" class="btn_type1 btn_type1_purple" ng-click="ctrl.commissionAddRow()">
					<b>업체수수료 등록</b>
				</button>
				<button type="button" class="btn_type1 btn_type1_purple" ng-click="commissionGrid.saveGridData()">
					<b>저장</b>
				</button>
				<button type="button" class="btn_type1 btn_type1_purple" ng-click="commissionGrid.deleteGridData()">
					<b>삭제</b>
				</button>
			</div>
			
			<div class="box_type1">
				<h3 class="sub_title2"> 업체 수수료 내역
					<span><spring:message code="c.search.totalCount" arguments="{{ grid_commission.totalItems }}"></spring:message></span>
				</h3>
		
				<div class="tb_util">
	
				</div>
				
				<div class="gridbox">
					<div class="grid" data-ui-grid="grid_commission"   
							data-ui-grid-move-columns 
							data-ui-grid-resize-columns 
							data-ui-grid-pagination
							data-ui-grid-auto-resize 
							data-ui-grid-exporter
							data-ui-grid-edit 
							data-ui-grid-selection
							data-ui-grid-validate
							data-ui-grid-row-edit
							data-ui-grid-cell-nav></div>
				</div>
			</div>
		</div> 
	</form>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>