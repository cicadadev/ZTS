<%--
	화면명 : 기획전 검색 팝업
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="wrap_popup" ng-app="ccsAppPopup" data-ng-controller="dms_exhibitSearchPopApp_controller as ctrl">
	<h2 class="sub_title1"><spring:message code="c.dmsExhibit.search"/><!-- 기획전 검색 --></h2>
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
					<th><spring:message code="c.dmsExhibit.exhibitState" /><!-- 기획전 상태 --></th>
						<td>
						<checkbox-list ng-model="exhibitSearch.exhibitState" code-group="EXHIBIT_STATE_CD" all-check />
					</td>					
				</tr>
				<%-- <tr>
					<th><spring:message code="c.dmsExhibit.exhibitType" /><!-- 기획전 유형 --></th>
						<td>
						<checkbox-list ng-model="exhibitSearch.exhibitType" code-group="EXHIBIT_TYPE_CD" all-check />
					</td>
				</tr> --%>
				<tr>
					<th>상품번호<%-- <spring:message code="c.dmsExhibit.product" /> --%><!-- 상품 --></th>
					<td>
						<!-- <select data-ng-model="exhibitSearch.productInfoType" data-ng-init="exhibitSearch.productInfoType = 'NAME'">
							<option ng-repeat="info in productInfoType" value="{{info.val}}" >{{info.text}}</option>
						</select> -->

						<input type="text" data-ng-model="exhibitSearch.productSearchKeyword" placeholder="" style="width:30%;" data-ng-init="exhibitSearch.productInfoType = 'PRODUCTID'"/>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.dmsExhibit" /><!-- 기획전 --></th>
					<td>
						<select data-ng-model="exhibitSearch.exhibitInfoType" data-ng-init="exhibitSearch.exhibitInfoType = 'NAME'">
							<option ng-repeat="info in exhibitInfoType" value="{{info.val}}" >{{info.text}}</option>
						</select>	
												
						<input type="text" data-ng-model="exhibitSearch.exhibitSearchKeyword" placeholder="" style="width:30%;" />
					</td>
				</tr>
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" class="btn_type1" data-ng-click="ctrl.reset()">
			<b><spring:message code="common.search.btn.init" /></b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" data-ng-click="ctrl.getExhibitList()">
			<b><spring:message code="common.search.btn.search" /></b>
		</button>
	</div>
	
	<!-- ### 기획전 목록 ### -->
	<div class="box_type1 marginT3">
		<h3 class="sub_title2">
			<spring:message code="c.dmsExhibit.exhibitList" /><!-- 기획전 목록 -->
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_exhibit.totalItems }}" /></span>
		</h3>

		<div class="tb_util">
			<button type="button" class="btn_tb_util tb_util1">되돌리기</button>
			<button type="button" class="btn_tb_util tb_util2">엑셀받기</button>
			<button type="button" class="btn_tb_util tb_util3">셀잠그기</button>
			<button type="button" class="btn_tb_util tb_util4">전체체크</button>

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
	
	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.cancel()">
			<b><spring:message code="c.common.close" /><!-- 닫기 --></b>
		</button>
		<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.exhibitSelect()">
			<b><spring:message code="c.common.select" /><!-- 저장 --></b>
		</button>
	</div>
	
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>