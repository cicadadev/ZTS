<%--
	화면명 : 사용자 관리 > 사용자 검색 목록 조회
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.user.manager.js"></script>

<article class="con_box con_on" data-ng-app="userApp" data-ng-controller="userListController as ctrl">
	<form name="form2">
			<!-- ### 사용자 검색 ### -->
			<h2 class="sub_title1"><spring:message code="c.ccs.userManeger"></spring:message> <!-- 사용자 관리 --></h2>
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
							<th>MD여부</th>
							<td>
								<checkbox-list ng-model="search.mdYn" custom="YES_NO" all-check ></checkbox-list>
							</td>		
							<th>사용여부</th>
							<td>
								<checkbox-list ng-model="search.userState" code-group="USER_STATE_CD" all-check ></checkbox-list>
							</td>								
						</tr>
						<tr>
							<th>사용자<%-- <spring:message code="c.ccsUser.userSearch"/> --%> <!-- 사용자검색--></th>
							<td colspan="3">
								<select data-ng-model="search.searchType" data-ng-init="search.searchType = 'NAME'">
									<option ng-repeat="info in searchType" value="{{info.val}}" >{{info.text}}</option>
								</select>
								<input type="tel" id="" value="" placeholder="" data-ng-model="search.searchKeyword"/>
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
			<!-- ### //사용자 검색 ### -->

			<!-- ### 사용자등록 ### -->
			<div class="btn_alignR marginT3">
				<button type="button" class="btn_type1" ng-click="ctrl.userInsertPopup()">
					<b>등록<%-- <spring:message code="c.ccsUser.insert"/> --%><!-- 사용자등록 --></b>
				</button>
<!-- 				<button type="button" class="btn_type1" ng-click="myGrid.deleteGridData()">
					<b>삭제</b>
				</button> -->
			</div>

			<div class="box_type1 marginT1">
				<h3 class="sub_title2">
					<spring:message code="c.ccsUser.userList"><!-- 사용자 내역 --> </spring:message>
					<!-- <span id="totalLen">(총 <b>{{qnaListSize}}</b>건)</span> -->
					<span><spring:message code="c.search.totalCount" arguments="{{ grid_user.totalItems }}"></spring:message></span>
				</h3>
		
				<div class="tb_util tb_util_rePosition">
					<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
				</div>
				
				<div class="gridbox">
					<div class="grid" data-ui-grid="grid_user"   
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
		</form>
	</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>