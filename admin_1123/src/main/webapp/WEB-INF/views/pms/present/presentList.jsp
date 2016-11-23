<%--
	화면명 : 상품관리 > 사은품 관리
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.present.list.js"></script>

<article class="con_box" ng-app="presentApp" ng-controller="pms_presentListApp_controller as ctrl">
	<h2 class="sub_title1"><spring:message code="c.pms.present.manager" /></h2>
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col class="col_142" />
				<col class="col_auto" />
				<col class="col_142" />
				<col class="col_auto" />
			</colgroup>
			<tbody>
				<tr>
					<th><spring:message code="c.pms.present.reg.days"/></th>
					<td colspan="3">
						<input type="text" id="startDt" ng-model="search.startDate" value="" placeholder="" datetime-picker date-only period-start/>
						~
						<input type="text" id="endDt" ng-model="search.endDate" value="" placeholder="" datetime-picker date-only period-end />
						<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0"/>
					</td>
<%-- 					<th rowspan="3" class="alignC"><spring:message code="c.pms.present.pop.id" /></th>
					<td rowspan="3">
						<textarea cols="30" rows="3" placeholder="" ng-model="search.productId" search-area> </textarea>
					</td> --%>
				</tr>
				<tr>
					<th><spring:message code="pmsProduct.useYn" /></th>
					<td>
						<checkbox-list ng-model="search.useYn" custom="USE_YN" all-check ></checkbox-list>
					</td>
					<th>상품 ERP 코드</th>
					<td>
						<input type="text" value="" ng-model="search.erpProductId" style="width:30%;" />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.pms.present.name" /></th>
					<td colspan="3"><select ng-model="search.searchType" ng-init="search.searchType='1'">
							<option value="1">사은품 번호</option>
							<option value="2">사은품명</option>
						</select>
						<input type="text" ng-model="search.keyword" style="width:22.5%;" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
<!-- 		<button type="button" ng-click="ctrl.testExcel()" class="btn_type1"> -->
<!-- 			<b>Excel Export</b> -->
<!-- 		</button> -->
		<button type="button" ng-click="ctrl.resetData()" class="btn_type1">
			<b><spring:message code="common.search.btn.init"/></b>
		</button>
		<button type="button" ng-click="searchPresent()" class="btn_type1 btn_type1_purple">
			<b><spring:message code="common.search.btn.search"/></b>
		</button>
	</div>

	<div class="btn_alignR marginT3">
		<button type="button" class="btn_type1" ng-click="openPresentPopup()">
			<b><spring:message code="c.pms.present.reg" /></b>
		</button>
	</div>

	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.pms.present.product.list" />
			<span><spring:message code="c.search.totalCount" arguments="{{ present.totalItems}}" /></span>
			<div class="tb_util tb_util_rePosition">
 				<button type="button" class="btn_tb_util tb_util1" ng-click="presentGrid.initGrid()">되돌리기</button>
				<button type="button" class="btn_tb_util tb_util2" ng-click="presentGrid.exportExcel(present)">엑셀받기</button>
			</div>
		</h3>

		<div class="gridbox" >
			<div class="grid" data-ui-grid="present"
			 data-ui-grid-resize-columns 
			 data-ui-grid-auto-resize
			 data-ui-grid-cell-nav
			 data-ui-grid-selection
			 data-ui-grid-exporter 
			 data-ui-grid-pagination
			 data-ui-grid-validate
			 ></div>
		</div>
	</div>
</div>
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>