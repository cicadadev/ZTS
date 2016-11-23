<%--
	화면명 : 프로모션 관리 > 쿠폰 관리 > 쿠폰 검색 팝업
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>

<div ng-app="ccsAppPopup" class="wrap_popup" ng-controller="sps_couponSearchPopApp_controller as ctrl">
	<h2 class="sub_title1"><spring:message code="c.sps.coupon.search" /></h2>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col class="col_142" />
					<col class="col_auto" />
<!-- 					<col width="9%" /> -->
<!-- 					<col width="*" /> -->
				</colgroup>
				<tbody>
					<tr>
						<th><spring:message code="c.search.period" /></th>
						<td>
							<input type="text" ng-model="search.startDate" id="termStartDt" value="" placeholder="" datetime-picker date-only period-start/>
								~ 
							<input type="text" ng-model="search.endDate" id="termEndDt" value="" placeholder="" datetime-picker date-only period-end/>
							<div class="day_group" start-ng-model="search.startDate" end-ng-model="search.endDate" date-only calendar-button  init-button="0"/>
						</td>
<%-- 						<th rowspan="2" class="alignC"><spring:message code="c.sps.coupon.coupon.id" /></th> --%>
<!-- 						<td rowspan="2"> -->
<!-- 							<textarea cols="30" rows="3" placeholder="" ng-model="search.couponId" search-area > </textarea> -->
<!-- 						</td> -->
					</tr>
					<tr>
						<th><spring:message code="c.sps.coupon.pop.coupon.type" /></th>
						<td>
							<checkbox-list ng-model="search.couponType" code-group="COUPON_TYPE_CD" all-check></checkbox-list>
							
						</td>
<%-- 						<th rowspan="2" class="alignC"><spring:message code="c.sps.coupon.product.id" /></th> --%>
<!-- 						<td rowspan="2"> -->
<!-- 							<textarea cols="30" rows="3" placeholder="" ng-model="search.productId" search-area > </textarea> -->
<!-- 						</td> -->
					</tr>
 					<tr>
						<th><spring:message code="c.sps.coupon.pop.coupon.state" /></th>
						<td>
							<checkbox-list ng-model="search.couponState" code-group="COUPON_STATE_CD" all-check></checkbox-list>
 						</td>
					</tr>
					<tr>
						<th><spring:message code="c.sps.coupon.product.id" /></th><!-- 상품번호 -->
						<td>
							<input type="text" ng-model="search.productId" style="width:34%"/>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.sps.coupon.search.info" /></th><!-- 쿠폰정보 -->
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
			<button type="button" ng-click="ctrl.searchCoupon()" class="btn_type1 btn_type1_purple">
				<b><spring:message code="c.search.btn.search" /></b>
			</button>
		</div>

		<div class="box_type1 marginT3">
			<h3 class="sub_title2">
				<spring:message code="c.sps.coupon.list" />
				<span><spring:message code="c.search.totalCount" arguments="{{ gridOptions.totalItems }}" /></span>
			</h3>
				
			<div class="gridbox gridbox300" >
				<div class="grid" data-ui-grid="gridOptions" 
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
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b><spring:message code="c.common.close" /></b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.addCoupon()">
				<b><spring:message code="c.common.select" /></b>
			</button>
		</div>
	</div>
	
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>