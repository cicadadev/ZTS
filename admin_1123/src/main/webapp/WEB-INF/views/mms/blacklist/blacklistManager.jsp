<%--
	화면명 : 블랙리스트 관리
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/mms.app.blacklist.manager.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<article class="con_box"  ng-app="blacklistManagerApp" data-ng-controller="mms_blacklistManagerApp_controller as ctrl">
	<h2 class="sub_title1">블랙리스트 관리</h2>
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col width="14%" />
				<col width="24%" />
				<col width="14%" />
				<col width="24%" />
				<col width="10%" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr>
					<th>블랙리스트 등록기간</th>
					<td colspan="3">
						<input type="text" data-ng-model="blacklistSearch.startDate" value="" placeholder="" datetime-picker date-only period-start/>											
						~
						<input type="text" data-ng-model="blacklistSearch.endDate" value="" placeholder="" datetime-picker date-only period-end/>

						<div class="day_group" start-ng-model="blacklistSearch.startDate" end-ng-model="blacklistSearch.endDate" date-only calendar-button init-button="0"/>
					</td>
					<th rowspan="8" class="alignC"><spring:message code="c.mmsMember.memberNo" /></th>
					<td rowspan="8">
						<textarea cols="30" rows="6" ng-model="blacklistSearch.memberNos" placeholder="" style="height:180px;"></textarea>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.mmsMember.memberType"/></th>
					<td colspan="3">
						<checkbox-list ng-model="blacklistSearch.memberType" code-group="MEMBER_TYPE_CD" all-check />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.mmsMember.memberGrade"/></th>
					<td colspan="3">
						<checkbox-list ng-model="blacklistSearch.memGrade" code-group="MEM_GRADE_CD" all-check />
					</td>
				</tr>
				<tr>
					<th>회원 상태</th>
					<td>
						<checkbox-list ng-model="blacklistSearch.memState" code-group="MEM_STATE_CD" all-check />
					</td>
					<th>블랙리스트 유형</th>
					<td>
						<checkbox-list ng-model="blacklistSearch.blacklistType" code-group="BLACKLIST_TYPE_CD" all-check />
					</td>
				</tr>
				<tr>
					<th>휴대폰 번호</th><!-- 회원연락처 -->
					<td>
						<input type="text" data-ng-model="blacklistSearch.cellNo" placeholder="" style="width:100%;" />
					</td>
					<th>회원이메일</th>
					<td>
						<input type="text" ng-model="blacklistSearch.email" value="" placeholder="" style="width:100%;" />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.mmsMember.info"/></th><!-- 회원정보 -->
					<td colspan="3">
						<select data-ng-model="blacklistSearch.infoType" data-ng-init="blacklistSearch.infoType = 'USERID'">
							<option ng-repeat="info in infoType" value="{{info.val}}" >{{info.text}}</option>
						</select>

						<input type="text" data-ng-model="blacklistSearch.searchKeyword" placeholder="" style="width:45%;" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" class="btn_type1" data-ng-click="ctrl.reset()">
			<b><spring:message code="common.search.btn.init" /></b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" ng-click="myGrid.loadGridData()">
			<b><spring:message code="common.search.btn.search" /></b>
		</button>
	</div>
	
	<div class="btn_alignR marginT3">
		<button type="button" class="btn_type1" ng-click="insBlacklistPop();">
			<b>블랙리스트 등록</b>
		</button>
		<button type="button" class="btn_type1" ng-click="cancelBlacklist();">
			<b>블랙리스트 해제</b>
		</button>
	</div>
	

	<!-- ### 블랙리스트 목록 ### -->
	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			블랙리스트 회원목록
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_blacklist.totalItems }}" /></span>
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
			<button type="button" class="btn_tb_util tb_util1" ng-click="myGrid.initGrid()">되돌리기</button>
			<button type="button" class="btn_tb_util tb_util2">엑셀받기</button>
		</div>

		<div class="gridbox gridbox200">
			<div class="grid" data-ui-grid="grid_blacklist"
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