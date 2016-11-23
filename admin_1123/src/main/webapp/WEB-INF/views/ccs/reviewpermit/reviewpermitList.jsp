<%--
	화면명 : 체험관리 화면 > 체험관리 검색 목록 조회
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.reviewpermit.list.js"></script>

<article class="con_box con_on" data-ng-app="reviewpermitApp" data-ng-controller="reviewpermitListController as ctrl">
	
	<form name="form2">
		<h2 class="sub_title1"><spring:message code="c.ccs.review.title"/></h2>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="9%" />
					<col width="34%" />
					<col width="9%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th><spring:message code="c.ccs.review.join"/></th>
						<td colspan="4">
							<input type="text" size="10" ng-model="search.startDate" value="" placeholder="" datetime-picker period-start/>										
							~
							<input type="text" size="10" ng-model="search.endDate" value="" placeholder="" datetime-picker period-end/>
							<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" calendar-button />	
							
						</td>
						<th rowspan="5" class="alignC"><spring:message code="c.ccs.review.memid"/></th><!-- 회원ID -->
						<td rowspan="5">
							<textarea cols="30" rows="5" placeholder="" style="height:100px;" ng-model="search.memId" search-area> </textarea>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.ccs.review.memstate"/></th>
						<td>
							<checkbox-list ng-model="search.memState" code-group="MEM_STATE_CD" all="true" all-check ></checkbox-list>
						</td>
						<th><spring:message code="c.ccs.review.memname"/></th>
						<td>
							<input type="text" id="" value="" placeholder="" style="width:80%;" data-ng-model="search.memName"/>	
						</td>
					</tr>
 					<tr>
						<!-- <th>회원 유형</th>
						<td>
							<checkbox-list ng-model="search.memberType" code-group="MEMBER_TYPE_CD" all="true" all-check ></checkbox-list>
						</td> -->
						<th><spring:message code="c.ccs.review.memgrade"/></th>
						<td colspan="4">
							<checkbox-list ng-model="search.memGrade" code-group="MEM_GRADE_CD" all="true" all-check ></checkbox-list>
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
		
		<div class="btn_alignR marginT3">
			<button type="button" class="btn_type1" ng-click="myGrid.deleteGridData()">
					<b><spring:message code="c.common.delete"/></b>
				</button>
				<button type="button" class="btn_type1" ng-click="ctrl.insertPopup()">
					<b><spring:message code="c.ccs.review.insert"/></b>
				</button>
		</div>
			
		<div class="box_type1 marginT1">
			<h3 class="sub_title2">
				<spring:message code="c.ccs.list" /><!--내역 -->
				<!-- <span id="totalLen">(총 <b>{{qnaListSize}}</b>건)</span> -->
				<span><spring:message code="c.search.totalCount" arguments="{{ grid_reviewpermit.totalItems }}" /></span>
			</h3>
	
			<div class="tb_util tb_util_rePosition">
				<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
			</div>
			
			<div class="gridbox gridbox500">
				<div class="grid" data-ui-grid="grid_reviewpermit"   
						data-ui-grid-move-columns 
							data-ui-grid-resize-columns 
							data-ui-grid-pagination
							data-ui-grid-auto-resize 
							data-ui-grid-exporter
							data-ui-grid-edit 
							data-ui-grid-selection
							data-ui-grid-validate
							data-ui-grid-row-edit
							data-ui-grid-cell-nav></div>
			</div>
			<!-- <div class="tb_bar">
				<button type="button" class="btn_grid_more" onclick="javascript:moreGrid('grid')">더보기</button>
			</div> -->
		</div>

	</form>

</article>				
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>

