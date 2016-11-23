<%--
	화면명 : 시스템 관리 > 메뉴 관리 > 메뉴 등록
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.menu.manager.js"></script>

<div ng-app="menuManagerApp"" ng-controller="insertPopup_controller as ctrl" ng-init="ctrl.initInsertPopup()" id="menuGroupInsertPopup" class="wrap_popup">
<form name="menugroupForm">
	<h1 class="sub_title1"><spring:message code="c.ccs.menu.group.register" /></h1>
	
	<div class="box_type1">
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
						<spring:message code="c.ccs.menu.menugroupid" /> <i><spring:message code="c.input.required" /></i>
					</th>
					<td>
						<input type="text" value="" v-key="ccsMenugroup.menuGroupId" placeholder="" style="width:60%;" ng-model="menuTreeRegisterElements.menuGroupId" />
					</td>
					<th>
						<spring:message code="c.ccs.menu.name" /> <i><spring:message code="c.input.required" /></i>
					</th>
					<td>
						<input type="text" value="" v-key="ccsMenugroup.name" placeholder="" style="width:60%;" ng-model="menuTreeRegisterElements.name" />
					</td>
				</tr>
				<tr>
					<th>
						<spring:message code="c.ccs.menu.sortno" /> <i><spring:message code="c.input.required" /></i>
					</th>
					<td colspan="3">
						<input type="text" value="" v-key="ccsMenugroup.sortNo" placeholder="" style="width:40%;" ng-model="menuTreeRegisterElements.sortNo" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /></b>
		</button>
		<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.insertMenuGroup()">
			<b><spring:message code="c.common.save" /></b>
		</button>
	</div>
</div>
</form>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>