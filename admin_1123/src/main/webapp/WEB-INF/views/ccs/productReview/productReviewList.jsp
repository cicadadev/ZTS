<%--
	화면명 : 상품평관리 > 상품평 검색 목록 조회
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.review.manager.js"></script>

<article class="con_box con_on" data-ng-app="productReviewApp" data-ng-controller="ccs_productReviewListApp_controller as ctrl">
	
	<form name="form2">
		<h2 class="sub_title1"><spring:message code="c.ccs.reviewInfo"><!--상품평 관리 --></spring:message></h2>
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
						<th><spring:message code="c.grid.column.insDt"></spring:message></th><!--등록일 -->
						<td colspan="3">
							<input type="text" style="width:120px;" ng-model="search.startDate" datetime-picker period-start date-only/>										
							~
							<input type="text" style="width:120px;" ng-model="search.endDate" datetime-picker period-end date-only/>
							<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0"/>	
							
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.ccs.reviewType"/> <!-- 상품평 유형--></th>
						<td colspan="3">
							<checkbox-list ng-model="search.permitYn" custom="REVIEW_TYPE" all-check />
						</td>
					</tr>
					<tr>
						<th><spring:message code="pmsReview.displayYn"></spring:message> </th><!-- 전시여부  -->
						<td colspan="3">
							<checkbox-list ng-model="search.displayYn" custom="DISPLAY_YN" all-check />
						</td> 
					</tr>
					<tr>
						<th>회원ID<%-- <spring:message code="c.mmsMember.memberSearch"/> --%> <!-- 회원검색--></th>
						<td>
							<input type="text" style="width:20%" data-ng-model="search.memberId"/>
							<button type="button" class="btn_type2" ng-click="ctrl.searchMember()"/>
								<b><spring:message code="c.search.btn.search"/></b>
							</button>
							<button type="button" class="btn_eraser" ng-click="ctrl.eraser()">지우개</button>
						</td>
					</tr>
					<tr>
						<th>상품번호<%-- <spring:message code="pmsProduct.name"/> --%></th><!--상품명 -->
						<td colspan="3">
							<input type="text" id="" value="" placeholder="" style="width:20%" data-ng-model="search.productId"/>										
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
			<button type="button" class="btn_type1" ng-click="myGrid.saveGridData()">
				<b><spring:message code="c.common.save" ></spring:message></b>
			</button>
		</div>
			
		<div class="box_type1 marginT1">
			<h3 class="sub_title2">
				<spring:message code="c.ccs.review.list" ></spring:message>
				<!-- <span id="totalLen">(총 <b>{{qnaListSize}}</b>건)</span> -->
				<span><spring:message code="c.search.totalCount" arguments="{{ grid_review.totalItems }}" /></span>
			</h3>
	
			<div class="tb_util tb_util_rePosition">
				<button type="button" class="btn_tb_util tb_util1" ng-click="myGrid.initGrid()">되돌리기</button>
				<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
	<!-- 			<button type="button" class="btn_tb_util tb_util1">되돌리기</button> -->
	<!-- 			<button type="button" class="btn_tb_util tb_util2">엑셀받기</button> -->
	<!-- 			<button type="button" class="btn_tb_util tb_util3">셀잠그기</button> -->
	<!-- 			<button type="button" class="btn_tb_util tb_util4">전체체크</button> -->
	
	<!-- 			<select style="width:105px;"> -->
	<!-- 				<option value="">200건</option> -->
	<!-- 			</select> -->
	
	<!-- 			<span class="page"> -->
	<!-- 				<button type="button" class="btn_prev">이전</button> -->
	<!-- 				<input type="text" value="1" placeholder="" /> -->
	<!-- 				<u>/</u><i>24</i> -->
	<!-- 				<button type="button" class="btn_next">다음</button> -->
	<!-- 			</span> -->
				
	<!-- 			<button type="button" class="btn_type2"> -->
	<!-- 				<b>이동</b> -->
	<!-- 			</button> -->
			</div>
			
			<div class="gridbox" style="text-align: center;">
				<div class="grid" data-ui-grid="grid_review"   
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
			<!-- <div class="tb_bar">
				<button type="button" class="btn_grid_more" onclick="javascript:moreGrid('grid')">더보기</button>
			</div> -->
		</div>

	</form>

</article>				
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>

