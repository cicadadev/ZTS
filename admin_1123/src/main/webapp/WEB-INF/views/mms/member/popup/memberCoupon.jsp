<%--
	화면명 : 회원관리 > 쿠폰 내역
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/mms.app.member.manager.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<div class="wrap_popup"  ng-app="memberManagerApp" data-ng-controller="mms_memberCouponPopApp_controller as ctrl">
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
						<th>발급일<!-- 발급일 --></th>
						<td>
							<input type="text" data-ng-model="search.startDate" value="" datetime-picker date-only period-start/>											
							~
							<input type="text" data-ng-model="search.endDate" value="" datetime-picker date-only period-end/>
	
							<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="1"/>
						</td>
					</tr>
					<tr>
						<th>발급상태</th>
						<td>
							<checkbox-list ng-model="search.couponIssueState" code-group="COUPON_ISSUE_STATE_CD" all-check></checkbox-list>
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
					<col width="30%" />
					<col width="20%" />
					<col width="30%" />
				</colgroup>
				<tbody>
					<tr>
						<th>총 쿠폰 수<!-- 총 쿠폰 수 --></th>
						<td>
							{{grid_memberCoupon.data[0].couponTotalCount}}
						</td>
						<th>사용 가능 쿠폰 수<!-- 사용 가능 쿠폰 수 --></th>
						<td>
							{{grid_memberCoupon.data[0].noUseCount}}
						</td>
					</tr>
				</tbody>
			</table>
		</div>

	
		<div class="btn_alignR marginT3">
			<button type="button" class="btn_type1" ng-click="ctrl.couponUseStop()" fn-id="31_STOP">
				<b>사용 중지</b>
			</button>
			<button type="button" class="btn_type1" ng-click="ctrl.manualIssueCoupon()" fn-id="31_ISSUE">
				<b>수동 발급</b>
			</button>
		</div>
	

		<!-- ### 보유 쿠폰 내역 ### -->
		<div class="box_type1 marginT1">
			<h3 class="sub_title2">
				보유 쿠폰 내역<!-- 보유 쿠폰 내역 -->
				<span><spring:message code="c.search.totalCount" arguments="{{grid_memberCoupon.totalItems}}" /></span>
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
				<div class="grid" data-ui-grid="grid_memberCoupon"
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