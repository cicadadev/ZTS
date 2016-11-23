<%--
	화면명 : 업체 관리 > 업체 상세 > 업체 배송정책 목록 조회
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page import="gcp.common.util.BoSessionUtil" %>
<%
	pageContext.setAttribute("businessId", BoSessionUtil.getBusinessId());
%>
<c:choose>
<c:when test="${businessId != '' && businessId != null}">
	<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
</c:when>
<c:otherwise>
	<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
</c:otherwise>		
</c:choose>

<script type="text/javascript" src="/resources/js/app/ccs.app.business.list.js"></script>

<c:choose>
<c:when test="${businessId != '' && businessId != null}">
	<article class="con_box con_on" data-ng-app="businessApp" data-ng-controller="deliveryListController as ctrl" data-ng-init="ctrl.deliveryGridInit()">
</c:when>
<c:otherwise>
	<div class="wrap_popup"  data-ng-app="businessApp" data-ng-controller="deliveryListController as ctrl" data-ng-init="ctrl.deliveryGridInit()">
</c:otherwise>		
</c:choose>

	<ul class="tab_type2">
		<li>
			<button type="button" ng-click="ctrl.onClickTab('1')"><spring:message code="c.ccsBusiness.info1"/></button>
		</li>
		<li >
			<button type="button" ng-click="ctrl.onClickTab('2')"><spring:message code="c.ccsBusiness.info2"/></button>
		</li>
		<li  class="on">
			<button type="button"><spring:message code="c.ccsBusiness.info3"/></button>
		</li>
		<li>
			<button type="button" ng-click="ctrl.onClickTab('4')"><spring:message code="c.ccsBusiness.info4"/></button>
		</li>
	</ul>
		<form name="form2">
				<div class="btn_alignR marginT2" >
					<button type="button" class="btn_type1" ng-click="ctrl.deInsertPopup()">
						<b>배송정책 추가</b>
					</button>
					<button type="button" class="btn_type1" ng-click="deliveryGrid.deleteGridData()">
						<b>삭제</b>
					</button>
				</div>
				<div class="box_type1 marginT1">
					<h3 class="sub_title2"> 배송정책 목록
						<span><spring:message code="c.search.totalCount" arguments="{{ grid_delivery.totalItems }}"></spring:message></span>
					</h3>
			
					<div class="tb_util">
		
					</div>
					
					<div class="gridbox gridbox400">
						<div class="grid" data-ui-grid="grid_delivery"   
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
<!-- 				<div class="btn_alignC marginT3"> -->
<!-- 					<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()"> -->
<%-- 						<b><spring:message code="c.common.close"/></b> --%>
<!-- 					</button> -->
<!-- 					<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.updateDelivery()"> -->
<%-- 						<b><spring:message code="c.common.save"/></b> --%>
<!-- 					</button> -->
<!-- 				</div> -->
		</form>
</div>
<c:choose>
<c:when test="${businessId != '' && businessId != null}">
	<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>
</c:when>
<c:otherwise>
	<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>
</c:otherwise>		
</c:choose>
	