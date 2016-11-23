<%--
	화면명 : 회원관리
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/mms.app.member.manager.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<article class="con_box"  ng-app="memberManagerApp" data-ng-controller="mms_memberManagerApp_controller as ctrl">
	<h2 class="sub_title1"><spring:message code="c.mmsMember.manager" /></h2>
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
				<tr>
					<th>가입기간<!-- 가입기간 --></th>
					<td colspan="3">
						<input type="text" data-ng-model="memberSearch.startDate" value="" placeholder="" datetime-picker date-only period-start/>											
						~
						<input type="text" data-ng-model="memberSearch.endDate" value="" placeholder="" datetime-picker date-only period-end/>

						<div class="day_group" start-ng-model="memberSearch.startDate" end-ng-model="memberSearch.endDate" date-only calendar-button init-button="0"/>
					</td>
					<th rowspan="7" class="alignL">회원ID</th>
					<td rowspan="7">
						<textarea cols="30" rows="5" placeholder="" ng-model="memberSearch.memberId" search-area ></textarea>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.mmsMember.memberType"/></th>
						<td colspan="3">
							<input type="checkbox" ng-model="memberSearch.membershipYn" id="membershipYn"><label for="membershipYn">멤버십</label>
							<input type="checkbox" ng-model="memberSearch.premiumYn" id="premiumYn"><label for="premiumYn">프리미엄</label>
							<input type="checkbox" ng-model="memberSearch.employeeYn" id="employeeYn"><label for="employeeYn">임직원</label>
							<input type="checkbox" ng-model="memberSearch.childrenYn" id="childrenYn"><label for="childrenYn">다자녀</label>
							<input type="checkbox" ng-model="memberSearch.b2eYn" id="b2eYn"><label for="b2eYn">B2E</label>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.mmsMember.memberGrade"/></th>
					<td colspan="3">
						<!-- <checkbox-list ng-model="memberSearch.memGrade" code-group="MEM_GRADE_CD" all-check /> -->
						<checkbox-list ng-model="memberSearch.memGrade" custom="MEM_GRADE" all-check />
					</td>
				</tr>
				<tr>
					<th>회원상태</th>
					<td colspan="3">
						<checkbox-list ng-model="memberSearch.memState" code-group="MEM_STATE_CD" all-check />
					</td>
					<%-- <th>블랙리스트 여부</th>
					<td>
						<input type="checkbox" ng-model="memberSearch.blacklistYn" id="chk1" ng-click="ctrl.blacklistYnCheck('all')"><label for="chk1"><spring:message code="c.select.all"/><!-- 전체 --></label> 
						<input type="checkbox" ng-model="memberSearch.blacklistY" id="chk2" ng-click="ctrl.blacklistYnCheck()"><label for="chk2">Y<!-- Y --></label> 
						<input type="checkbox" ng-model="memberSearch.blacklistN" id="chk3" ng-click="ctrl.blacklistYnCheck()"><label for="chk3">N<!-- N --></label>
					</td> --%>
				</tr>
				<tr>
					<th>회원명</th>
					<td colspan="3">
						<input type="text" data-ng-model="memberSearch.name" placeholder="" style="width:80%;" />
					</td>
				</tr>
				<tr>
					<th>회원 휴대폰 번호</th><!-- 회원연락처 -->
					<td colspan="3">
						<input type="text" data-ng-model="memberSearch.cellNo" placeholder="" style="width:80%;" />
					</td>
				</tr>
				<tr>
					<th>회원 E-mail<%-- <spring:message code="c.mmsMember.email" /> --%></th>
					<td colspan="3">
						<input type="text" ng-model="memberSearch.email" value="" placeholder="" style="width:80%;" />
					</td>
				</tr>
				<%-- <tr>
					<th><spring:message code="c.mmsMember.info"/></th><!-- 회원정보 -->
					<td colspan="3">
						<select data-ng-model="memberSearch.infoType" data-ng-init="memberSearch.infoType = 'USERID'">
							<option ng-repeat="info in infoType" value="{{info.val}}" >{{info.text}}</option>
						</select>

						<input type="text" data-ng-model="memberSearch.searchKeyword" placeholder="" style="width:25%;" />
					</td>
				</tr> --%>
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" class="btn_type1" data-ng-click="reset()">
			<b><spring:message code="common.search.btn.init" /></b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" ng-click="myGrid.loadGridData()">
			<b><spring:message code="common.search.btn.search" /></b>
		</button>
	</div>
	


	<!-- ### 회원 목록 ### -->
	<div class="box_type1 marginT3">
		<h3 class="sub_title2">
			<spring:message code="c.mmsMember.memberList" />
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_member.totalItems }}" /></span>
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
		
		<div class="tb_util tb_util_rePosition">
<!-- 			<button type="button" class="btn_tb_util tb_util1" ng-click="myGrid.initGrid()">되돌리기</button> -->
			<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()" fn-id="31_EXCEL">엑셀받기</button>
		</div>

		<div class="gridbox gridbox200">
			<div class="grid" data-ui-grid="grid_member"
					data-ui-grid-move-columns
					data-ui-grid-row-edit
					data-ui-grid-resize-columns 
					data-ui-grid-pagination
					data-ui-grid-auto-resize
					data-ui-grid-cell-nav
					data-ui-grid-selection 
					data-ui-grid-exporter
					data-ui-grid-edit
					data-ui-grid-validate></div>
		</div>
	</div>
	
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>