<%--
	화면명 : 회원관리 > 포인트 내역
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/mms.app.member.manager.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<div class="wrap_popup"  ng-app="memberManagerApp" data-ng-controller="mms_memberPointPopApp_controller as ctrl">
		<jsp:include page="/WEB-INF/views/mms/member/popup/memberTab.jsp" flush="true"/>
	
		<div class="box_type1 marginT3">
			<table class="tb_type1">
				<colgroup>
					<col width="15%" />
					<col width="*%" />
				</colgroup>
				<tbody>
					<tr>
						<th>사용가능 포인트<!-- 사용가능 포인트 --></th>
						<td>
							1000
						</td>
					</tr>
				</tbody>
			</table>
		</div>

	
		<div class="btn_alignR marginT3">
			<button type="button" class="btn_type1" ng-click="">
				<b>포인트 조정</b>
			</button>
		</div>
	

		<!-- ### 포인트 내역 ### -->
		<div class="box_type1 marginT1">
			<h3 class="sub_title2">
				포인트 내역<!-- 포인트 내역 -->
				<span><spring:message code="c.search.totalCount" arguments="{{ grid_memberPoint.totalItems }}" /></span>
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
				<div class="grid" data-ui-grid="grid_memberPoint"
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