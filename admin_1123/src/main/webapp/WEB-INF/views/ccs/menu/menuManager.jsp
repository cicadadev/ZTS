<%--
	화면명 : 시스템 관리 > 메뉴 관리
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.menu.manager.js"></script>

<article id="menu" class="con_box con_on" ng-app="menuManagerApp" ng-controller="ccs_menuManagerApp_controller as Ctrl">
	<h2 class="sub_title1"><spring:message code="c.ccs.menu.mng"/></h2>
	<!-- ### //메뉴 목록 ### -->
	<div class="category" ng-init="Ctrl.getMenuGroupTree()">
		<button type="button" class="btn_type2 btn_type2_gray2" ng-click="Ctrl.register()">
			<b><spring:message code="c.ccs.menu.group.register" /></b>
		</button>

		<ul class="list_dep">
			<li ng-repeat="menu in MenuGroupTree">
				<!-- <button ng-click="Ctrl.openFolder($index, category.name)" type="button"></button> -->
				<a href="#none" ng-click="Ctrl.getMenuGroup($event, menu);" id="a_{{menu.name}}" class="{{menu.active=='Y'?'active':''}}">{{menu.name}}</a>
			</li>
		</ul>
	</div>

	<form name="form2">
		<div class="columnR">
			<!-- ### 메뉴 기본정보 설정 ### -->
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
								<spring:message code="c.ccs.menu.menugroupno" />
							</th>
							<td ng-bind="menuInfo.menuGroupId"></td>
							<th>
								<spring:message code="c.ccs.menu.groupname" /> <i><spring:message code="c.input.required" /></i>
							</th>
							<td>
								<input type="text" value="" v-key="ccsMenugroup.name" placeholder="" style="width:60%;" 
									ng-model="menuInfo.name" />
							</td>
						</tr>
						<tr>
							<th>
								<spring:message code="c.ccs.menu.sortno" /> <i><spring:message code="c.input.required" /></i>
							</th>
							<td colspan="3">
								<input type="text" value="" v-key="ccsMenugroup.sortNo"  placeholder="" style="width:20%;" ng-model="menuInfo.sortNo" />
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</form>
	
			<div class="btn_alignC marginT3" ng-if="detailShow==true"">
				<button ng-if="menuInfo.menuGroupId == undefined"type="button" ng-click="Ctrl.canceMenuGroup()" class="btn_type3 btn_type3_gray">
					<b><spring:message code="c.common.cancel"></spring:message></b>
				</button>
				<button ng-if="menuInfo.menuGroupId != undefined" type="button" class="btn_type3 btn_type3_gray" ng-click="Ctrl.deleteMenuGroup(menuInfo)">
					<b><spring:message code="c.common.delete" /></b>
				</button>
				<%-- <button type="button" class="btn_type1 btn_type1_gray">
					<b><spring:message code="c.common.cancel" /></b>
				</button> --%>
				<button type="button" class="btn_type3 btn_type3_purple" ng-click="Ctrl.updateMenuGroup(menuInfo)">
					<b><spring:message code="c.common.save" /></b>
				</button>
			</div>
			
			<div ng-if="gridShow==true" class="btn_alignR marginT3">
				<button type="button" class="btn_type1" data-ng-click="Ctrl.addMenuRow()">
					<b><spring:message code="c.common.reg" /></b>
				</button>
				<button type="button" class="btn_type1" data-ng-click="Ctrl.deleteMenuGrpGrid()">
					<b><spring:message code="c.common.delete" /></b>
				</button>
				<button type="button" class="btn_type1" data-ng-click="Ctrl.saveMenuGrpGrid()">
					<b><spring:message code="common.btn.save" /></b>
				</button>
			</div>
			
			<div ng-if="gridShow==true" class="box_type1 marginT1">
				<h3 class="sub_title2">
					<spring:message code="c.ccs.menu.list" /><!-- 메뉴목록 -->
					<span id="totalLen">
						<span><spring:message code="c.search.totalCount" arguments="{{ myMenuGrid.totalItems }}" /></span>
					</span>
				</h3>
				
				<div class="tb_util">

					<select style="width:105px;">
						<option value="">200건</option>
					</select>
		
					<span class="page">
						<button type="button" class="btn_prev">이전</button>
						<input type="text" value="1" placeholder="" />
						<u>/</u><i>24</i>
						<button type="button" class="btn_next">다음</button>
					</span>
		
					<button type="button" class="btn_type2">
						<b>이동</b>
					</button>
				</div>
			
				<div ng-if="gridShow==true" class="tb_util tb_util_rePosition">
		 				<button type="button" class="btn_tb_util tb_util1" ng-click="menuGrid.initGrid()">되돌리기</button>
						<button type="button" class="btn_tb_util tb_util2" ng-click="menuGrid.exportExcel()">엑셀받기</button>
				</div>
				<div class="gridbox gridbox400" id="gridArea">
					<div class="grid" data-ui-grid="myMenuGrid"   
						data-ui-grid-move-columns 
						data-ui-grid-resize-columns 
						data-ui-grid-auto-resize
						data-ui-grid-selection
						data-ui-grid-exporter 
						data-ui-grid-edit 
						data-ui-grid-row-edit
						data-ui-grid-cell-nav
						data-ui-grid-validate>
					</div>
				</div>
			</div>
		</div>
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>