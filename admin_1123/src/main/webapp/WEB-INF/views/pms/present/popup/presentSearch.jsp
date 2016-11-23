<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>

<div class="wrap_popup" ng-app="ccsAppPopup" ng-controller="pms_presentSearch_controller as ctrl">
	<h1 class="sub_title1">사은품조회</h1>

	<div class="box_type1">
	<table class="tb_type1">
		<colgroup>
			<col width="11%" />
			<col width="64%" />
			<col width="9%" />
			<col width="*" />
		</colgroup>
		<tbody>
			<tr>
				<th>사은품 등록일</th>
				<td colspan="3">
					<input type="text" ng-model="search.startDate"  datetime-picker date-only period-start/>
					~
					<input type="text" ng-model="search.endDate" datetime-picker date-only period-end/>
					<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0"/>
				</td>				
				<%-- <th rowspan="3" class="alignC"><spring:message code="pms.product.list.productid" /></th>
				<td rowspan="3">
					<textarea cols="30" rows="5" ng-model="search.productId" placeholder="" style="height:106px;" search-area></textarea>
				</td> --%>
			</tr>
			<tr>
				<th>사용여부</th>
				
				<td colspan="3">
					<checkbox-list ng-model="search.useYn" custom="USE_YN" all-check ></checkbox-list>
					<!-- <select ng-model="search.useYn">
						<option value="">전체</option>
						<option value="Y">사용</option>
						<option value="N">미사용</option>
					</select> -->
				</td>
			</tr>			
			<%-- <tr>
				<th><spring:message code="pmsProduct.name" /></th>
				<td><input type="text"  ng-model="search.name" style="width:50%;"/>
				</td>						
			</tr> --%>
			<tr>
				<th>사은품</th>
				<td colspan="3">
					<select data-ng-model="search.infoType" data-ng-init="search.infoType = 'NAME'">
						<option ng-repeat="info in infoType" value="{{info.val}}" >{{info.text}}</option>
					</select>

					<input type="text" data-ng-model="search.searchKeyword" placeholder="" style="width:45%;" />
				</td>
			</tr>	
		</tbody>
	</table>
	</div>

	<div class="btn_alignR">
		<button type="button" class="btn_type1 btn_type1" ng-click="ctrl.reset()">
			<b>초기화</b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" ng-click="ctrl.search()">
			<b>검색</b>
		</button>
	</div>

	<div class="box_type1" style="margin-top:20px;">
		<h3 class="sub_title2">
			사은품 목록
			<span>(총 <b>{{myGrid.totalItems}}</b>건)</span>
		</h3>
		<div class="gridbox  gridbox300">
			<div class="grid" data-ui-grid="myGrid"   
				data-ui-grid-move-columns
				data-ui-grid-row-edit
				data-ui-grid-resize-columns 
				data-ui-grid-pagination
				data-ui-grid-auto-resize
				data-ui-grid-cell-nav
				data-ui-grid-selection 
				data-ui-grid-exporter
				data-ui-grid-edit
				data-ui-grid-validate>
			</div>
		</div>

	</div>

	<div class="btn_alignC" style="margin-top:39px;">
		<button type="button" class="btn_type3 btn_type3_gray" data-ng-click="ctrl.close()">
			<b>취소</b>
		</button>
		<button type="button" class="btn_type3 btn_type3_purple" data-ng-click="ctrl.select()">
			<b>선택</b>
		</button>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>