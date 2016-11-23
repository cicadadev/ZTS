<%--
	화면명 : 시스템 관리 > 권한 관리 > 권한(그룹) 등록 팝업
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.role.manager.js"></script>

<div ng-app="roleApp" ng-controller="ccs_roleInsertPopApp_controller as ctrl" ng-init="ctrl.initPopup()" id="roleInsertPopup" class="wrap_popup">
	<h1 class="sub_title1"><spring:message code="c.ccs.role.register"></spring:message></h1>
	
	
		<div class="box_type1">
			<form name="form2">
			<table class="tb_type1">
				<colgroup>
					<col width="13%" />
					<col width="37%" />
					<col width="13%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							<spring:message code="c.ccs.role.rolegroupId"><!-- 권한그룹 번호 --></spring:message>
						</th>
						<td ng-bind="search.roleId"></td>
						<th>
							<spring:message code="c.ccs.role.rolegroupName"><!-- 권한그룹명 --></spring:message>
						</th>
						<td>
							<input type="text" placeholder="" style="width:60%;" ng-model="search.name" />
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.ccs.role.rolegroup.groupDiv"><!-- 그룹구분 --></spring:message> <i><spring:message code="c.input.required"><!-- 필수입력 --></spring:message></i>
						</th>
						<td>
							<select-list ng-model="search.groupDiv" style="min-width:40px;" all-check>
								<option value="">전체</option>
							</select-list>
						</td>
					</tr>
					<tr>
						<th><spring:message code="ccsRole.note"><!-- 설명 --></spring:message></th>
						<td colspan="3">
							<textarea cols="30" rows="3" ng-model="search.note" style="margin-left: 1px;" search-area></textarea>
						</td>
					</tr>
				</tbody>
			</table>
			</form>
		</div>
		
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b><spring:message code="c.common.close"></spring:message></b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.registerRoleGroup(search)">
				<b><spring:message code="c.common.save"></spring:message></b>
			</button>
		</div>
	
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>