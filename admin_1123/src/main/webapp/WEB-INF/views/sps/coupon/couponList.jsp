<%--
	화면명 : 프로모션 관리 > 쿠폰 관리
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/sps.app.coupon.list.js"></script>

<article data-app-key="sps_couponListApp" ng-app="couponApp" class="con_box con_on" ng-controller="sps_couponListApp_controller as ctrl">
<form name="form">
	<h2 class="sub_title1"><spring:message code="c.sps.coupon.list.title" /></h2>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col class="col_142" />
					<col class="col_auto" />
<!-- 					<col style="width:80px;" />				 -->
<!-- 					<col class="col_auto" /> -->
				</colgroup>
				<tbody>
					<tr>
						<th><spring:message code="c.sps.coupon.pop.issue.term" /><!-- 발급기간 --></th>
						<td>
							<input type="text" ng-model="search.startDate" id="termStartDt" value="" placeholder="" datetime-picker period-start date-only/>
								~ 
							<input type="text" ng-model="search.endDate" id="termEndDt" value="" placeholder="" datetime-picker period-end date-only/>
							<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button init-button="0"></div>
						</td>
<%-- 						<th rowspan="5" class="alignC"><spring:message code="c.sps.coupon.coupon.id" /><!-- 쿠폰번호 --></th> --%>
<!-- 						<td rowspan="5"> -->
<!-- 							<textarea cols="30" rows="5" placeholder="" ng-model="search.couponId" search-area > </textarea> -->
<!-- 						</td> -->
					</tr>
					<tr>
						<th><spring:message code="c.sps.coupon.pop.coupon.state" /><!-- 쿠폰상태 --></th>
						<td>
							<checkbox-list ng-model="search.couponState" code-group="COUPON_STATE_CD" all-check></checkbox-list>
						</td>
					</tr>
					<tr ng-show="isPo == false">
						<th><spring:message code="c.sps.coupon.pop.coupon.type" /><!-- 쿠폰유형 --></th>
						<td>
							<checkbox-list ng-model="search.couponType" code-group="COUPON_TYPE_CD" all-check></checkbox-list>
							
						</td>
					</tr>
					<tr ng-show="isPo == false">
						<th>공급업체 번호/명<%-- <spring:message code="c.common.tag.business" /> --%><!-- 업체번호 / 명 --></th>
						<td>
							<input type="text" value="" ng-model="search.businessId" style="width:22.5%;" />
						<button type="button" class="btn_type2" ng-click="ctrl.businessPop()">
							<b><spring:message code="c.search.btn.search" /></b>
						</button>
						<button type="button" class="btn_eraser" ng-click="ctrl.businessEraser()" 
						ng-class="{'btn_eraser_disabled' : search.businessId == null || search.businessId == ''}"></button>
						</td>
					</tr>
					<tr>
						<th ><spring:message code="c.sps.coupon.product.id" /><!-- 상품번호 --></th>
						<td >
							<input type="text" ng-model="search.productId" style="width:22.5%;" />
						</td>
					</tr>
					<tr>
					<th><spring:message code="c.sps.coupon.promotion" /><!-- 쿠폰 프로모션 --></th>
					<td>
						<select data-ng-model="search.infoType" data-ng-init="search.infoType = 'ID'">
							<option ng-repeat="info in infoType" value="{{info.val}}" >{{info.text}}</option>
						</select>
						<input type="text" ng-model="search.searchKeyword" style="width:22.5%"/>
					</td>
					</tr>
				</tbody>
			</table>
		</div>

		<div class="btn_alignR">
			<button type="button" ng-click="ctrl.resetCoupon()" class="btn_type1">
				<b><spring:message code="c.search.btn.reset" /></b>
			</button>
			<button type="button" ng-click="searchCoupon()" class="btn_type1 btn_type1_purple">
				<b><spring:message code="c.search.btn.search" /></b>
			</button>
		</div>

		<div class="btn_alignR marginT3">
			<button type="button" class="btn_type1 " ng-click="savePopupOpen()" fn-id="7_INSERT">
				<b><spring:message code="c.sps.coupon.create" /></b>
			</button>
			<button type="button" ng-click="ctrl.deleteCoupon()" ng-show="isPo == false" class="btn_type1" fn-id="7_DELETE">
				<b><spring:message code="c.common.delete" /></b>
			</button>
		</div>
		
		<div class="box_type1 marginT1">
			<h3 class="sub_title2">
				<spring:message code="c.sps.coupon.list" /><!-- 쿠폰 목록 -->
				<span><spring:message code="c.search.totalCount" arguments="{{ gridOptions.totalItems }}" /></span>
			</h3>
		
			<div class="tb_util tb_util_rePosition">
				<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()" fn-id="7_EXCEL">엑셀받기</button>
			</div>
				
			<div class="gridbox" >
				<div class="grid" data-ui-grid="gridOptions" 
					data-ui-grid-resize-columns 
					data-ui-grid-auto-resize 
					data-ui-grid-pagination
					data-ui-grid-selection
					data-ui-grid-row-edit 
					data-ui-grid-cell-nav
					data-ui-grid-exporter
					data-ui-grid-edit 
					data-ui-grid-validate></div>
			</div>
		</div>
</form>
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>