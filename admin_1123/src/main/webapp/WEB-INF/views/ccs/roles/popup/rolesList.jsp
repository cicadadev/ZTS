<%--
	화면명 : 권한관리 > 권한 그룹 팝업 (사용자 등록시 사용)
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.user.manager.js"></script>

<div class="wrap_popup" data-ng-app="userApp" data-ng-controller="roleListPopupController as ctrl">
	<form name="form2">
		<h2 class="sub_title1"><spring:message code="c.ccs.user.role"><!--  --></spring:message></h2>
		<div class="box_type1 marginT1">
			<%-- <h3 class="sub_title2">
				<span><spring:message code="c.search.totalCount" arguments="{{ grid_user.totalItems }}"></spring:message></span>
			</h3> --%>
			
			<div class="gridbox gridbox300">
				<div class="grid" data-ui-grid="grid_user"   
						data-ui-grid-move-columns data-ui-grid-resize-columns 
			data-ui-grid-auto-resize data-ui-grid-exporter data-ui-grid-selection></div>
				</div>
		</div>
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" data-ng-click="ctrl.close()">
				<b>닫기</b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" data-ng-click="ctrl.selectUser()">
				<b>확인</b>
			</button>
		</div>
	</form>
</div>				
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>