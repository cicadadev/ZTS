<%--
	화면명 : 카드사할인관리 > 카드사목록
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/sps.app.creditcard.list.js"></script>

<article class="con_box con_on" ng-app="creditcardApp" ng-controller="sps_creditcardListApp_controller as ctrl">
	
	<form name="form2">
		<h2 class="sub_title1"><spring:message code="sps.cardpromotion.title" /></h2>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col class="col_142" />
					<col class="*" />
				</colgroup>
				<tbody>
					<tr>
						<th><spring:message code="c.sps.cardpromotion.period" /></th>
						<td>
							<input type="text" ng-model="search.startDate" placeholder="" datetime-picker period-start date-only/>
							~
							<input type="text" ng-model="search.endDate" placeholder="" datetime-picker period-end date-only/>
							<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0"/>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.sps.cardpromotion.state" /></th>
						<td>
							<checkbox-list ng-model="search.cardPromotionStateCd" code-group="CARD_PROMOTION_STATE_CD" all-check ></checkbox-list>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.sps.cardpromotion.name" /></th>
						<td>
							<input type="text" ng-model="search.name" style="width:22.5%;" />
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<div class="btn_alignR">			
			<button type="button" ng-click="ctrl.reset()" class="btn_type1">
				<b><spring:message code="c.search.btn.reset" /></b>
			</button>
			<button type="button" ng-click="searchGrid()" class="btn_type1 btn_type1_purple">
				<b><spring:message code="c.search.btn.search" /></b>
			</button>
		</div>
		
		<div class="btn_alignR marginT3" >
			<button type="button" class="btn_type1" ng-click="ctrl.insertPopup()">
				<b><spring:message code="sps.cardpromotion.btn.register" /></b>
			</button>
			<button type="button" class="btn_type1" ng-click="ctrl.deleteCardPromotion()">
				<b><spring:message code="c.common.delete" /></b>
			</button>
		</div>
			
		<div class="box_type1 marginT1" >
			<h3 class="sub_title2">
				<spring:message code="sps.cardpromotion.title.list" />
				<span><spring:message code="c.search.totalCount" arguments="{{ grid_card.totalItems }}" /></span>
			</h3>
			
			<div class="tb_util tb_util_rePosition">
				<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
			</div>

			<div class="gridbox gridbox300">
				<div class="grid" ui-grid="grid_card"
						ui-grid-move-columns 
						ui-grid-resize-columns 
						ui-grid-pagination
						ui-grid-auto-resize 
						ui-grid-exporter
						ui-grid-row-edit
						ui-grid-cell-nav
						ui-grid-selection
						ui-grid-edit
						ui-grid-validate></div>
			</div>
		</div>
	</form>

</article>				
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>

