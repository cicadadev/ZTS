<%--
	화면명 : 업체 관리 > 업체 검색 목록 조회
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.business.list.js"></script>

<article class="con_box con_on" data-ng-app="businessApp" data-ng-controller="businessManagerController as ctrl">
	
	<form name="form2">
		<h2 class="sub_title1"><spring:message code="c.ccsBusiness.manager"><!-- 업체 관리 --></spring:message></h2>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="12%" />
					<col width="30%" />
					<col width="12%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>업체등록일</th><!--등록기간 -->
						<td colspan="3">
							<input type="text" ng-model="search.startDate" datetime-picker period-start date-only/>										
							~
							<input type="text" ng-model="search.endDate" datetime-picker period-end date-only/>
							<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0"/>	
						</td>
						<th rowspan="4" class="alignL">업체번호</th>
						<td rowspan="4">
							<textarea cols="30" rows="5" placeholder="" ng-model="search.businessId" search-area ></textarea>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.ccsBusiness.saleType"></spring:message></th><!-- 매입유형 -->
						<td colspan="3">
							<checkbox-list ng-model="search.saleType" code-group="SALE_TYPE_CD" all="true" all-check ></checkbox-list>
						</td>
					</tr>
 					<tr>
						<th><spring:message code="c.ccsBusiness.businessState"/></th><!-- 업체상태 -->
						<td colspan="3">
							<checkbox-list ng-model="search.businessState" code-group="BUSINESS_STATE_CD" all="true" all-check ></checkbox-list>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.ccsBusiness.businessname"/></th><!-- 업체명 -->
						<td colspan="3">
							<input type="text" id="" value="" style="width:200;" ng-model="search.businessName2"/>
						</td>			
					</tr>
				</tbody>
			</table>
		</div>
		
		<div class="btn_alignR">
			
			<button type="button" data-ng-click="ctrl.reset()" class="btn_type1">
				<b><spring:message code="c.search.btn.reset" /></b>
			</button>
			<button type="button" data-ng-click="searchGrid()" class="btn_type1 btn_type1_purple">
				<b><spring:message code="c.search.btn.search" /></b>
			</button>
		</div>
		
		<div class="btn_alignR  marginT3" >
			<button type="button" class="btn_type1" ng-click="ctrl.insertPopup()">
				<b> <spring:message code="c.ccsBusiness.register"></spring:message></b>
			</button>
		</div>
			
		<div class="box_type1 marginT1" >
			<h3 class="sub_title2">
				<spring:message code="c.ccsBusiness.list"/>
				<span><spring:message code="c.search.totalCount" arguments="{{ grid_business.totalItems }}" /></span>
			</h3>
	
			<div class="tb_util tb_util_rePosition">
				<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
			</div>
			
			<div class="gridbox gridbox500">
				<div class="grid" data-ui-grid="grid_business"   
						data-ui-grid-move-columns 
						data-ui-grid-resize-columns 
						data-ui-grid-pagination
						data-ui-grid-auto-resize 
						data-ui-grid-exporter
						data-ui-grid-row-edit
						data-ui-grid-cell-nav
						data-ui-grid-selection
						data-ui-grid-edit
						data-ui-grid-validate></div>
			</div>
		</div>

	</form>

</article>				
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>

