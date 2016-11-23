<%--
	화면명 : 기획전 관리
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/dms.app.exhibit.manager.js"></script>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>

<article class="con_box"  ng-app="exhibitApp" data-ng-controller="dms_exhibitManagerApp_controller as ctrl">
	<h2 class="sub_title1"><spring:message code="c.dmsExhibit.manager" /> <!-- 기획전 관리 --></h2>
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col width="9%" />
				<col width="64%" />
				<col width="9%" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr>
					<th><spring:message code="c.dmsExhibit.period" /><!-- 기획전 기간 --></th>
					<td>
						<input type="text" data-ng-model="exhibitSearch.startDate" value="" placeholder="" datetime-picker date-only period-start/>											
						~
						<input type="text" data-ng-model="exhibitSearch.endDate" value="" placeholder="" datetime-picker date-only period-end/>

						<div class="day_group" start-ng-model="exhibitSearch.startDate" end-ng-model="exhibitSearch.endDate" date-only calendar-button  init-button="0"/>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.dmsExhibit.exhibitState" /><!-- 전시여부 --></th>
					<td>
						 <checkbox-list ng-model="exhibitSearch.exhibitState" code-group="EXHIBIT_STATE_CD" all-check />
					</td>						
				</tr>
				<tr>
					<th><spring:message code="c.dmsExhibit.exhibitType" /><!-- 기획전 유형 --></th>
					<td>
						<checkbox-list ng-model="exhibitSearch.exhibitType" code-group="EXHIBIT_TYPE_CD" all-check />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.dmsExhibit" /><!-- 기획전 --></th>
					<td>
						<select data-ng-model="exhibitSearch.exhibitInfoType" data-ng-init="exhibitSearch.exhibitInfoType = 'EXHIBITID'">
							<option ng-repeat="info in exhibitInfoType" value="{{info.val}}" >{{info.text}}</option>
						</select>	
												
						<input type="text" data-ng-model="exhibitSearch.exhibitSearchKeyword" placeholder="" style="width:30%;" />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.dmsExhibit.product" /><!-- 상품 --></th>
					<td>
						<select data-ng-model="exhibitSearch.productInfoType" data-ng-init="exhibitSearch.productInfoType = 'PRODUCTID'">
							<option ng-repeat="info in productInfoType" value="{{info.val}}" >{{info.text}}</option>
						</select>

						<input type="text" data-ng-model="exhibitSearch.productSearchKeyword" placeholder="" style="width:30%;" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" class="btn_type1" ng-click="ctrl.reset()">
			<b><spring:message code="c.search.btn.reset" /><!-- 초기화 --></b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" ng-click="myGrid.loadGridData()">
			<b><spring:message code="c.search.btn.search" /><!-- 검색 --></b>
		</button>
	</div>
	
	<div class="btn_alignR marginT3">
		<button type="button" class="btn_type1" ng-click="ctrl.addExhibit()">
			<b><spring:message code="c.dmsExhibit.regExhibit" /><!-- 기획전 등록 --></b>
		</button>
		<button type="button" class="btn_type1" ng-click="ctrl.deleteExhibit()">
			<b><spring:message code="c.common.delete" /><!-- 삭제 --></b>
		</button>					
		<button type="button" class="btn_type1" ng-click="myGrid.saveGridData()">
			<b><spring:message code="c.common.save" /><!-- 저장 --></b>
		</button>
	</div>
	

	<!-- ### 기획전 목록 ### -->
	<div class="box_type1 marginT1">
		<h3 class="sub_title2">
			<spring:message code="c.dmsExhibit.exhibitList" /><!-- 기획전 목록 -->
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_exhibit.totalItems }}" /></span>
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
		</div>

		<div class="gridbox">
			<div class="grid" data-ui-grid="grid_exhibit" 
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
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>