<%--
	화면명 : 시스템 관리 > 권한 관리
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.role.manager.js"></script>

<article id="roles" class="con_box con_on" ng-app="roleApp" ng-controller="ccs_roleManagerApp_controller as ctrl">
	<h2 class="sub_title1"><spring:message code="c.ccs.role.mng"/></h2>
	<!-- ### 권한그룹 목록 ### -->
	<div class="category" ng-init="ctrl.getRoleGroup();">
		<button type="button" class="btn_type2 btn_type2_gray2" ng-click="ctrl.roleGroupPopup()">
			<b><spring:message code="c.ccs.role.register"></spring:message></b>
		</button>

		<ul class="list_dep">
			<li ng-repeat="role in roleGroup">
				<a href="#none" ng-click="getRoleDetail($event, role);" id="a_{{role.name}}" class="{{role.active=='Y'?'active':''}}">{{role.name}}</a>
			</li>
		</ul>
	</div>

	<form name="form2">
		<div class="columnR">
 			<span ng-if="detailShow!=true"><spring:message code="ccs.role.init.message" /></span>	
			<div class="box_type1" ng-if="detailShow==true">
				<table class="tb_type1">
					<colgroup>
						<col width="18%" />
						<col width="32%" />
						<col width="18%" />
						<col width="*" />
					</colgroup>
					<tbody>
						<tr>
							<th>
								<spring:message code="c.ccs.role.rolegroupId"><!-- 권한그룹 번호 --></spring:message>
							</th>
								<td ng-bind="search.roleId" readonly></td>
							<th>
								<spring:message code="c.ccs.role.rolegroupName"><i><spring:message code="c.input.required" /></i><!-- 권한그룹명 --></spring:message>
							</th>
							<td>
								<input type="text" placeholder="" style="width:60%;" ng-model="search.name" v-key="ccsNotice.name" required/>
							</td>
						</tr>
						<tr>
							<th><spring:message code="ccsRole.note"><!-- 설명 --></spring:message></th>
							<td colspan="3">
								<textarea cols="30" rows="3" ng-model="search.note" style="margin-left: 1px;"></textarea>
							</td>
						</tr>						
					</tbody>
				</table>
			</div>
	
			<div class="btn_alignC marginT3" ng-if="detailShow==true"">
				<button ng-if="search.roleId == undefined" type="button" ng-click="ctrl.cancelRole()" class="btn_type3 btn_type3_gray">
					<b><spring:message code="c.common.cancel"></spring:message></b>
				</button>
				<button ng-if="search.roleId != undefined" type="button" ng-click="ctrl.deleteRole()" class="btn_type3 btn_type3_gray">
					<b><spring:message code="c.common.delete"></spring:message></b>
				</button>
				<button type="button" ng-click="ctrl.saveRole(search)" class="btn_type3 btn_type3_purple">
					<b><spring:message code="c.common.save"></spring:message></b>
				</button>
			</div>
			
			<div ng-if="gridShow==true" class="btn_alignR marginT3">
				<button type="button" ng-click="ctrl.saveGrid()" class="btn_type1">
					<b><spring:message code="c.common.save"></spring:message></b>
				</button>
			</div>
			
			<!-- 권한 페이지 리스트 -->	
			<div ng-if="gridShow==true" class="box_type1 marginT1">
				<h3 class="sub_title2">
					권한 메뉴 목록
					<span><spring:message code="c.search.totalCount" arguments="{{ grid_role.data.length }}"></spring:message></span>
				</h3>
				<div class="gridbox gridbox300">
				<div class="grid" data-ui-grid="grid_role" 
					data-ui-grid-move-columns 
							data-ui-grid-resize-columns 
							data-ui-grid-auto-resize 
							data-ui-grid-selection 
							data-ui-grid-row-edit
							data-ui-grid-cell-nav
							data-ui-grid-exporter
							data-ui-grid-edit 
							data-ui-grid-validate></div>
				</div>
			</div>
			
			<div ng-if="gridShow2==true" class="btn_alignR marginT3">
				<button type="button" ng-click="ctrl.saveFuncGrid()" class="btn_type1">
					<b><spring:message code="c.common.save"></spring:message></b>
				</button>
			</div>
			<div ng-if="gridShow2==true" class="box_type1 marginT1">
			<h3 class="sub_title2">
				<%-- <spring:message code="c.ccs.code.list" /> --%>메뉴 기능 목록
					<span><spring:message code="c.search.totalCount" arguments="{{ myFuncGrid.data.length }}" /></span>
			</h3>
			<div class="gridbox gridbox300">
				<div class="grid" data-ui-grid="myFuncGrid" 
					data-ui-grid-move-columns data-ui-grid-resize-columns 
					data-ui-grid-auto-resize
					data-ui-grid-selection data-ui-grid-edit 
					data-ui-grid-row-edit data-ui-grid-validate></div>
				</div>					
			</div>
		</div>
	</form>
</article>				
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>