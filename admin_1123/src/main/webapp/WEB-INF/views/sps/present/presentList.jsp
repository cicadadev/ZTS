<%--
	화면명 : 프로모션 관리 > 사은품 프로모션 관리
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/sps.app.present.list.js"></script>

<article class="con_box" ng-app="presentPromotionApp" ng-controller="sps_presentPromotionListApp_controller as ctrl">
	<h2 class="sub_title1"><spring:message code="c.sps.present.manager" /></h2>
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
					<col class="col_142" />
					<col class="col_auto" />
<%-- 					<col style="width:80px;" />				 --%>
<%-- 					<col class="col_auto" /> --%>
			</colgroup>
			<tbody>
				<tr>
					<th><spring:message code="c.sps.common.period"/><!-- 프로모션 기간 --></th>
					<td>
						<input type="text" id="startDt" ng-model="search.startDate" value="" placeholder="" datetime-picker period-start date-only/>
						~
						<input type="text" id="endDt" ng-model="search.endDate" value="" placeholder="" datetime-picker period-end date-only/>
						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0"/>
					</td>
<%-- 					<th rowspan="5" class="alignC"><spring:message code="c.sps.present.pop.id" /></th> --%>
<!-- 					<td rowspan="5"> -->
<!-- 						<textarea cols="30" rows="5" placeholder="" ng-model="search.presentId" search-area> </textarea> -->
<!-- 					</td> -->
				</tr>
				<tr>
					<th><spring:message code="c.sps.present.state" /></th>
					<td>
						<checkbox-list ng-model="search.presentState" code-group="PRESENT_STATE_CD" all-check />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.sps.present.pop.type" /></th>
					<td>
						<checkbox-list ng-model="search.presentType" code-group="PRESENT_TYPE_CD" all-check />
					</td>
				</tr>
<!-- 				<tr> -->
<%-- 					<th><spring:message code="c.sps.present.name" /></th> --%>
<!-- 					<td> -->
<!-- 						<input type="text" ng-model="search.name" value="" placeholder="" style="width:22.5%;" /> -->
<!-- 					</td> -->
<!-- 				</tr> -->
				<tr>
				<th>프로모션</th>
				<td>
					<select data-ng-model="search.infoType" data-ng-init="search.infoType = 'ID'">
						<option ng-repeat="info in infoType" value="{{info.val}}" >{{info.text}}</option>
					</select>
					<input type="text" ng-model="search.searchKeyword" style="width:22.5%"/>
				</td>
				</tr>
				<tr>
					<th ><spring:message code="c.sps.coupon.product.id" /><!-- 상품번호 --></th>
					<td >
						<input type="text" ng-model="search.productId" style="width:22.5%;" />
					</td>
				</tr>
<!-- 				<tr> -->
<!-- 					<td colspan="4"> -->
<!-- 						<textarea ckeditor="ckOption" ng-model="ckEditor"> -->
						
<!-- 						</textarea> -->
<!-- 					</td> -->
<!-- 				</tr> -->
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" ng-click="ctrl.resetData()" class="btn_type1">
			<b><spring:message code="common.search.btn.init"/></b>
		</button>
		<button type="button" ng-click="searchPresent()" class="btn_type1 btn_type1_purple">
			<b><spring:message code="common.search.btn.search"/></b>
		</button>
	</div>

	<div class="btn_alignR marginT3">
		<button type="button" class="btn_type1 " ng-click="ctrl.openPopupInsert()">
			<b><spring:message code="c.sps.present.register" /></b>
		</button>
		<button type="button" ng-click="ctrl.deletePresent()" class="btn_type1">
			<b><spring:message code="c.common.delete" /></b>
		</button>
	</div>

	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.sps.present.list" />
			<span><spring:message code="c.search.totalCount" arguments="{{ gridOpt.totalItems}}" /></span>
			<div class="tb_util tb_util_rePosition">
<!--  				<button type="button" class="btn_tb_util tb_util1" ng-click="gridOptions.initGrid()">되돌리기</button> -->
				<button type="button" class="btn_tb_util tb_util2" ng-click="gridOptions.exportExcel()">엑셀받기</button>
			</div>
		</h3>

		<div class="gridbox" >
			<div class="grid" data-ui-grid="gridOpt"
				data-ui-grid-resize-columns 
				data-ui-grid-auto-resize 
				data-ui-grid-pagination
				data-ui-grid-selection
				data-ui-grid-row-edit 
				data-ui-grid-cell-nav
				data-ui-grid-exporter
				data-ui-grid-edit 
				data-ui-grid-validate></div>
		</div>
	</div>
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>