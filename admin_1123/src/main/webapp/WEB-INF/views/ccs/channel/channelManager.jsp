<%--
	화면명 : 채널관리
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.channel.manager.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<article class="con_box"  ng-app="channelApp" data-ng-controller="ccs_channelManagerApp_controller as ctrl">
	<h2 class="sub_title1"><spring:message code="ccs.channel.list.title" /></h2>
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
					<th><spring:message code="c.grid.column.insDt"/><!-- 등록일 --></th>
					<td>
						<input type="text" data-ng-model="channelSearch.startDate" value="" placeholder="" datetime-picker date-only period-start/>											
						~
						<input type="text" data-ng-model="channelSearch.endDate" value="" placeholder="" datetime-picker date-only period-end/>

						<div class="day_group" start-ng-model="channelSearch.startDate" end-ng-model="channelSearch.endDate" date-only calendar-button init-button="0"/>
					</td>
				</tr>
				<tr>
					<th><spring:message code="ccsChannel.channelTypeCd"/></th>
						<td>
							<checkbox-list ng-model="channelSearch.channelType" code-group="CHANNEL_TYPE_CD" all-check />
						</td>
				</tr>
				<tr>
					<th>채널상태코드</th>
					<td>
						<checkbox-list ng-model="channelSearch.channelState" code-group="CHANNEL_STATE_CD" all-check />
					</td>				
				</tr>
				<tr>
					<th><spring:message code="ccsChannel.name" /></th>
					<td>
						<input type="text" ng-model="channelSearch.name" value="" placeholder="" style="width:30%;" />
					</td>
				</tr>
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
	
	<div class="btn_alignR marginT3">
		<button type="button" class="btn_type1" ng-click="ctrl.regChannel()">
			<b><spring:message code="ccs.channel.btn.register" /></b>
		</button>
		<button type="button" class="btn_type1" ng-click="ctrl.deleteChannel()">
			<b><spring:message code="common.btn.del" /></b>
		</button>					
<!-- 		<button type="button" class="btn_type1" ng-click="myGrid.saveGridData()"> -->
<%-- 			<b><spring:message code="common.btn.save" /></b> --%>
<!-- 		</button> -->
	</div>
	

	<!-- ### 채널 목록 ### -->
	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="ccs.channel.list" />
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_channel.totalItems }}" /></span>
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
			<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
<!-- 			<button type="button" class="btn_tb_util tb_util5" ng-click="ctrl.addRow()">행추가</button> -->
		</div>

		<div class="gridbox">
			<div class="grid" data-ui-grid="grid_channel"
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