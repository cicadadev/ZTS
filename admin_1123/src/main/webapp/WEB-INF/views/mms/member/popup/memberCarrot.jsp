<%--
	화면명 : 회원관리 > 당근 내역
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/mms.app.member.manager.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<div class="wrap_popup"  ng-app="memberManagerApp" data-ng-controller="mms_memberCarrotPopApp_controller as ctrl">
	<jsp:include page="/WEB-INF/views/mms/member/popup/memberTab.jsp" flush="true"/>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="15%" />
					<col width="60%" />
					<col width="9%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>적용일<!-- 적용일 --></th>
						<td>
							<input type="text" data-ng-model="search.startDate" value="" datetime-picker date-only period-start/>											
							~
							<input type="text" data-ng-model="search.endDate" value="" datetime-picker date-only period-end/>
	
							<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="1"/>
						</td>
					</tr>
					<tr>
						<th>유형</th>
						<td>
							<checkbox-list ng-model="search.carrotType" code-group="CARROT_TYPE_CD" all-check></checkbox-list>
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
	
		<div class="box_type1 marginT3">
			<table class="tb_type1">
				<colgroup>
					<col width="20%" />
					<col width="*%" />
				</colgroup>
				<tbody>
					<tr>
						<th>사용가능 당근<!-- 사용가능 당근 --></th>
						<td>
							{{latestBalanceAmt}}
						</td>
					</tr>
					<tr>
						<th>기간내 사용현황<!-- 기간내 사용현황 --></th>
						<td>
							이벤트 적립 : <span ng-if="!grid_memberCarrot.data[0].eventPlusAmt">0</span>
										  <span ng-if="grid_memberCarrot.data[0].eventPlusAmt">{{grid_memberCarrot.data[0].eventPlusAmt}}</span>&nbsp;&nbsp;
							이벤트 사용 : <span ng-if="!grid_memberCarrot.data[0].eventMinusAmt">0</span>
										  <span ng-if="grid_memberCarrot.data[0].eventMinusAmt">{{grid_memberCarrot.data[0].eventMinusAmt}}</span>&nbsp;&nbsp;
							CS적립 : <span ng-if="!grid_memberCarrot.data[0].csPlusAmt">0</span>
									 <span ng-if="grid_memberCarrot.data[0].csPlusAmt">{{grid_memberCarrot.data[0].csPlusAmt}}</span>&nbsp;&nbsp;
							CS사용 : <span ng-if="!grid_memberCarrot.data[0].csMinusAmt">0</span>
									 <span ng-if="grid_memberCarrot.data[0].csMinusAmt">{{grid_memberCarrot.data[0].csMinusAmt}}</span>&nbsp;&nbsp;
							만료 : 	<span ng-if="!grid_memberCarrot.data[0].expiredAmt">0</span>
									<span ng-if="grid_memberCarrot.data[0].expiredAmt">{{grid_memberCarrot.data[0].expiredAmt}}</span>
						</td>
					</tr>
				</tbody>
			</table>
		</div>

	
		<div class="btn_alignR marginT3">
			<button type="button" class="btn_type1" ng-click="carrotAdjustment()" fn-id="31_CARROT">
				<b>당근 조정</b>
			</button>
		</div>
	

		<!-- ### 당근 내역 ### -->
		<div class="box_type1 marginT1">
			<h3 class="sub_title2">
				당근 내역<!-- 당근 내역 -->
				<span><spring:message code="c.search.totalCount" arguments="{{ grid_memberCarrot.totalItems }}" /></span>
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
	
			<div class="gridbox gridbox300">
				<div class="grid" data-ui-grid="grid_memberCarrot"
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
	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /><!-- 닫기 --></b>
		</button>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>