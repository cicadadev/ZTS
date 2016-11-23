<%--
	화면명 : 체험관리 화면 > 체험관리 등록
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.reviewpermit.list.js"></script>

<div class="wrap_popup" data-ng-app="reviewpermitApp" data-ng-controller="reviewpermitDetailController as ctrl">
	
	<form name="form2">
		<h2 class="sub_title1"><spring:message code="c.ccs.review.insert"/></h2>
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
							<input type="text" size="10" ng-model="search.regStartDt" value="" placeholder="" datetime-picker period-start/>										
							~
							<input type="text" size="10" ng-model="search.regEndDt" value="" placeholder="" datetime-picker period-end/>
							<div class="day_group" start-ng-model="search.regStartDt" end-ng-model="search.regEndDt" calendar-button />	
							
						</td>
						<th rowspan="5" class="alignC"><spring:message code="c.ccs.review.memno"/></th><!-- 회원ID -->
						<td rowspan="5">
							<textarea cols="30" rows="5" placeholder="" style="height:100px;" ng-model="search.memNo" search-area> </textarea>
						</td>
					</tr>
 					<!-- <tr>
						<th>회원 유형</th>
						<td colspan="4">
							<checkbox-list ng-model="search.memBerType" code-group="MEMBER_TYPE_CD" all="true" all-check ></checkbox-list>
						</td>
					</tr> -->
					<tr>
						<th><spring:message code="c.ccs.review.memgrade"/></th>
						<td colspan="4">
							<checkbox-list ng-model="search.memGrade" code-group="MEM_GRADE_CD" all="true" all-check ></checkbox-list>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.ccs.review.memstate"/></th>
						<td colspan="4"> 
							<checkbox-list ng-model="search.memState" code-group="MEM_STATE_CD" all="true" all-check ></checkbox-list>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.ccs.review.memname"/></th>
						<td colspan="4">
							<input type="text" id="" value="" placeholder="" style="width:80%;" data-ng-model="search.name"/>	
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

		<div class="box_type1 marginT3">
			<h3 class="sub_title2">
				<spring:message code="c.ccs.review.memlist"/>
				<span><spring:message code="c.search.totalCount" arguments="{{ grid_member.totalItems }}" /></span>
			</h3>
	
			<div class="tb_util">
	
			</div>
			
			<div class="gridbox gridbox300">
				<div class="grid" data-ui-grid="grid_member"   
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
		</div>
		
		<div ng-show="product==true" style="margin-top:40px;">
			<div class="btn_alignR marginT3">
				<button type="button" class="btn_type1" ng-click="ctrl.searchPopup()">
					<b><spring:message code="c.spsPresent.add.product"/></b>
				</button>
				<button type="button" class="btn_type1" ng-click="prdGrid.deleteRow()">
					<b><spring:message code="c.common.delete"/></b>
				</button>
			</div>
			<div class="box_type1 marginT1">
				<h3 class="sub_title2">
					<spring:message code="c.spsDealproduct.productList"/>
					<span><spring:message code="c.search.totalCount" arguments="{{ grid_product.totalItems }}" /></span>
				</h3>
		
				<div class="tb_util">
		
				</div>
				
				<div class="gridbox gridbox300">
					<div class="grid" data-ui-grid="grid_product"   
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
			</div>
		</div>
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b><spring:message code="c.common.close"/></b>
			</button>
		</div>
	</form>

</div>				
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>

