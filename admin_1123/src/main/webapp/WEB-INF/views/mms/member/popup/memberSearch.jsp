<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="wrap_popup" data-ng-app="ccsAppPopup" data-ng-controller="ccs_memberSearchApp_controller as ctrl">
	<h1 class="sub_title1"><spring:message code="c.mmsMember.memberSearch"/></h1><!-- 회원 검색 -->

	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col width="13%" />
				<col width="25%" />
				<col width="13%" />
				<col width="25%" />
				<col width="10%" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr ng-if="isPeriod == true">
					<th>가입기간<!-- 가입기간 --></th>
					<td colspan="3">
						<input type="text" data-ng-model="search.startDate" value="" placeholder="" datetime-picker date-only period-start/>											
						~
						<input type="text" data-ng-model="search.endDate" value="" placeholder="" datetime-picker date-only period-end/>

						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0"/>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.mmsMember.memberType"/></th>
						<td colspan="3">
							<input type="checkbox" ng-model="search.membershipYn" id="membershipYn"><label for="membershipYn">멤버십</label>
							<input type="checkbox" ng-model="search.premiumYn" id="premiumYn"><label for="premiumYn">프리미엄</label>
							<input type="checkbox" ng-model="search.employeeYn" id="employeeYn"><label for="employeeYn">임직원</label>
							<input type="checkbox" ng-model="search.childrenYn" id="childrenYn"><label for="childrenYn">다자녀</label>
							<input type="checkbox" ng-model="search.b2eYn" id="b2eYn"><label for="b2eYn">B2E</label>
						</td>
				</tr>
				<tr>
					<th><spring:message code="c.mmsMember.memberGrade"/></th>
					<td colspan="3">
						<!-- <checkbox-list ng-model="search.memGrade" code-group="MEM_GRADE_CD" all-check /> -->
						<checkbox-list ng-model="search.memGrade" custom="MEM_GRADE" all-check />
					</td>
				</tr>
				<%-- <tr>
					<th>회원상태</th>
					<td>
						<checkbox-list ng-model="search.memState" code-group="MEM_STATE_CD" all-check />
					</td>
					<th>블랙리스트 여부</th>
					<td>
						<input type="checkbox" ng-model="search.blacklistYn" id="chk1" ng-click="ctrl.blacklistYnCheck('all')"><label for="chk1"><spring:message code="c.select.all"/><!-- 전체 --></label> 
						<input type="checkbox" ng-model="search.blacklistY" id="chk2" ng-click="ctrl.blacklistYnCheck()"><label for="chk2">Y<!-- Y --></label> 
						<input type="checkbox" ng-model="search.blacklistN" id="chk3" ng-click="ctrl.blacklistYnCheck()"><label for="chk3">N<!-- N --></label>
					</td>
				</tr>
				<tr>
					<th>회원 휴대폰 번호</th><!-- 회원연락처 -->
					<td>
						<input type="text" data-ng-model="search.cellNo" placeholder="" style="width:100%;" />
					</td>
					<th><spring:message code="c.mmsMember.email" /></th>
					<td>
						<input type="text" ng-model="search.email" value="" placeholder="" style="width:100%;" />
					</td>
				</tr> --%>
				<tr>
					<th><spring:message code="c.mmsMember.info"/></th><!-- 회원정보 -->
					<td colspan="3">
						<select data-ng-model="search.infoType" data-ng-init="search.infoType = 'USERID'">
							<option ng-repeat="info in infoType" value="{{info.val}}" >{{info.text}}</option>
						</select>

						<input type="text" data-ng-model="search.searchKeyword" placeholder="" style="width:45%;" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" class="btn_type1" data-ng-click="ctrl.reset()">
			<b><spring:message code="c.search.btn.reset"/><!-- 초기화 --></b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" data-ng-click="ctrl.searchMember()">
			<b><spring:message code="c.search.btn.search"/><!-- 검색 --></b>
		</button>
	</div>

	<div class="box_type1 marginT3">
		<h3 class="sub_title2">
			<spring:message code="c.mmsMember.memberList"/><!-- 회원 목록 -->
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_member.totalItems }}" /></span>
		</h3>
		
		<div class="gridbox gridbox200">
			<div class="grid" data-ui-grid="grid_member" 
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
		<button type="button" class="btn_type3 btn_type3_gray" data-ng-click="ctrl.close()">
			<b><spring:message code="c.common.close"/><!-- 닫기 --></b>
		</button>
		<button type="button" class="btn_type3 btn_type3_purple" data-ng-click="ctrl.selectMember()">
			<b><spring:message code="c.common.select"/><!-- 선택 --></b>
		</button>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>
		