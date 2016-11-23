<%--
	화면명 : 상품QnA관리 > 상품QnA 검색목록 조회
	작성자 : emily
 --%>
 
 <style>
.alignC {
	text-align: center;
}
.alignR {
	text-align: right;
}
.ui-grid-header-cell-row {
	text-align: center;
}
</style>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.product.qna.list.js"></script>

<article class="con_box con_on" data-ng-app="productQnaManagerApp" data-ng-controller="productQnaListController as ctrl">
	
	<form name="form2">
		<h2 class="sub_title1"><spring:message code="c.pms.productqna"><!--상품QNA --></spring:message></h2>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="9%" />
					<col width="15%" />
					<col width="9%" />
					<col width="15%" />
				</colgroup>
				<tbody ng-show="!poBusinessId"><%--BO 일경우 --%>
					<tr>
						<th>기간<%-- <spring:message code="c.grid.column.insDt"/> --%></th><!--등록일 -->
						<td colspan="4">
							<select ng-model="search.periodType" ng-init="search.periodType = 'QNA_DATE'">
								<option ng-repeat="info in periodType" value="{{info.val}}">{{info.text}}</option>
							</select> 
							<input type="text" style="width: 120px;" ng-model="search.startDate" datetime-picker period-start date-only /> 
							~ <input type="text" style="width: 120px;" ng-model="search.endDate" datetime-picker period-end date-only />
							<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" calendar-button="B" date-only init-button="0"/>
						</td>
					</tr>
					
					<tr>
						<%-- <th>Q&A 유형<spring:message code="c.ccs.productqna.type"></spring:message></th><!-- QNA유형 -->
						<td>
							<checkbox-list ng-model="search.productQnaType" code-group="PRODUCT_QNA_TYPE_CD" all="true" all-check ></checkbox-list>
						</td> --%>
						<th>Q&A 상태<%-- <spring:message code="c.ccs.productqna.state"></spring:message> --%></th><!-- QNA 상태-->
						<td>
							<checkbox-list ng-model="search.productQnaState" code-group="PRODUCT_QNA_STATE_CD" all-check ></checkbox-list>
						</td>
						<th>업체번호 / 명</th>
						<td>
							<input type="text" ng-model="search.businessId" style="width:20%;" />
							<input type="text" ng-model="search.businessName" style="width:20%;" />
							<button type="button" class="btn_type2" ng-click="ctrl.searchBusinessPopup()">
								<b><spring:message code="c.search.btn.search"/><!-- 검색 --></b>
							</button>
							<button type="button" class="btn_eraser" ng-click="ctrl.eraser('business')">지우개</button>
						</td>	
					</tr>
					<tr>
						<th>공개여부</th>
						<td><checkbox-list ng-model="search.displayYn" custom="PUBLIC_YN" all-check ></checkbox-list>
						</td>
						<th>Q&A 등록자<%-- <spring:message code="c.ccs.qna.searchType"/> --%> <!-- 회원검색 --></th>
						<td>
							<input type="text" ng-change="changeMemberLoginId()" data-ng-model="search.memberLoginId" />
							<button type="button" class="btn_type2" ng-click="ctrl.searchMemberPopup('member')">
								<b><spring:message code="c.search.btn.search"/></b>
							</button>
							<button type="button" class="btn_eraser" ng-click="ctrl.eraser('member')">지우개</button>
						</td>
					</tr>
					<tr>
						<th>상품</th>
						<td>
							<select data-ng-model="search.prdSearchType" data-ng-init="search.prdSearchType = 'NAME'">
								<option ng-repeat="info in searchType2" value="{{info.val}}" >{{info.text}}</option>
							</select>
							<input type="text" ng-model="search.searchPrdword" value="" placeholder="" style="width:30%;"/>										
						</td>
						<th>답변등록자<%-- <spring:message code="c.ccs.review.md"/> --%> <!-- 유저 검색--></th>
						<td>
							<input type="text" ng-change="changeAnswererName()" data-ng-model="search.answererName" />
							<button type="button" class="btn_type2" ng-click="ctrl.searchUserPopup()">
								<b><spring:message code="c.search.btn.search"/></b>
							</button>
							<button type="button" class="btn_eraser" ng-click="ctrl.eraser('answer')">지우개</button>
						</td>
						<%-- <th>공개여부<spring:message code="pmsProductqna.displayYn"></spring:message> </th><!-- 전시여부  -->
						<td>
							<radio-yn ng-model="search.displayYn" labels='<spring:message code="c.input.radio.displayY" />,<spring:message code="c.input.radio.displayN" />' init-val="Y"></radio-yn>
						</td>  --%>
						
					</tr>
					<tr>
						<th>제목/내용<%-- <spring:message code="c.ccs.qna.searchType"/> --%> <!-- 검색기준--></th>
						<td colspan="3">
							<input type="text" style="width:20%;" data-ng-model="search.keyword"/>
						</td>
					</tr>
				</tbody>
				<tbody ng-show="poBusinessId"><%--PO 일경우 --%>
					<tr>
						<th>기간<%-- <spring:message code="c.grid.column.insDt"/> --%></th><!--등록일 -->
						<td colspan="4">
							<select ng-model="search.periodType" data-ng-init="search.periodType = 'QNA_DATE'">
								<option ng-repeat="info in periodType" value="{{info.val}}">{{info.text}}</option>
							</select> 
							<input type="text" style="width: 120px;" ng-model="search.startDate" datetime-picker period-start date-only /> 
							~ <input type="text" style="width: 120px;" ng-model="search.endDate" datetime-picker period-end date-only />
							<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" calendar-button="B" date-only init-button="0"/>
						</td>
					</tr>
					
					<tr>
						<th>Q&A 상태<%-- <spring:message code="c.ccs.productqna.state"></spring:message> --%></th><!-- QNA 상태-->
						<td>
							<checkbox-list ng-model="search.productQnaState" code-group="PRODUCT_QNA_STATE_CD" all="true" all-check ></checkbox-list>
						</td>
						<th>상품</th>
						<td>
							<select data-ng-model="search.prdSearchType" data-ng-init="search.prdSearchType = 'NAME'">
								<option ng-repeat="info in searchType2" value="{{info.val}}" >{{info.text}}</option>
							</select>
							<input type="text" ng-model="search.searchPrdword" value="" placeholder="" style="width:30%;"/>										
						</td>
					</tr>
					<tr>
						<th>제목/내용<%-- <spring:message code="c.ccs.qna.searchType"/> --%> <!-- 검색기준--></th>
						<td colspan="3">
							<input type="text" style="width:200px;" data-ng-model="search.keyword"/>
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
			<h3 class="sub_title2"><%-- <spring:message code="c.ccs.productqna.list"/> --%>
				Q&A 목록
				<span><spring:message code="c.search.totalCount" arguments="{{ grid_prdQna.totalItems }}" /></span>
			</h3>
	
			<div class="tb_util tb_util_rePosition">
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
			
			<div class="gridbox gridbox500">
				<div class="grid" data-ui-grid="grid_prdQna"   
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
			
			<!-- <div class="tb_bar">
				<button type="button" class="btn_grid_more" onclick="javascript:moreGrid('grid')">더보기</button>
			</div> -->
		</div>
						
		<!-- <div class="btn_alignR">
			<button type="button" class="btn_type1 btn_type1_purple" data-ng-click="memCtrl.move('/mms/qna/insert')">
				<b>문의등록</b>
			</button>
		</div> -->
	</form>

</article>				
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>

