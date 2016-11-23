<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%-- <%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags" %> --%>
<!-- <link href="//netdna.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet"> -->
<!-- <link href="/resources/css/bootstram-3.3.6.css" rel="stylesheet"> -->

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true" />
<link rel="stylesheet" type="text/css" href="/resources/css/oms.css" />
<script type="text/javascript" src=/resources/js/app/oms.app.order.etc.js></script>
<!-- 820 * 640 -->
<div class="wrap_popup" ng-app="orderEtcApp" ng-controller="deliveryTrackingCtrl as ctrl" ng-init="ctrl.init()">
	<h1 class="sub_title1">배송조회</h1>
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col width="90px" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr ng-repeat="product in omsOrderproducts">
					<td class="img_prd">
<%-- 						<tags:prdImgTag productId="{{ product.productId }}" seq="0" size="90"/> --%>
						<img ng-src="product.imgUrl" img-domain style="width: 180px; height: 180px;" />						
					</td>
					<td>
						<p>{{ product.productName }}{{product.productId}}</p>
						<span ng-if="product.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SET'">
							<span ng-repeat="children in omsOrderproducts">
								<div class="txt_opt" ng-if="children.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SUB' && children.upperOrderProductNo == product.orderProductNo">
									<p>
										{{ children.productName }} : {{ children.saleproductName }}
										<i style="float: right;">({{ children.setQty }}개)</i>
									</p>
								</div>
							</span>
						</span>
						<span ng-if="product.orderProductTypeCd != 'ORDER_PRODUCT_TYPE_CD.SET'">
							<div class="txt_opt">
								<p>{{ product.saleproductName }}</p>
							</div>
						</span>
						<p>
							{{ product.orderQty }}개 / <strong>{{ product.totalSalePrice | number }}</strong>
						</p>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	<div class="box_type1" style="margin-top: 20px;">
		<table class="tb_type1">
			<colgroup>
				<col width="142px" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr>
					<th>택배사</th>
					<td>{{ logistics.deliveryServiceName }}</td>
				</tr>
				<tr>
					<th>송장번호</th>
					<td>{{ logistics.invoiceNo }}</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<!-- ### 1번 탭 ### -->
	<article class="con_box">
		<h2 class="sub_title1">
<!-- 			배송정보 -->
		</h2>
	
		<!-- ### 추적 목록 ### -->
		<div ng-repeat="logistics in logisticsList | limitTo:1">
			<div class="box_type1 marginT3">
				<h3 class="sub_title2">
					배송정보
				</h3>
				<%--
				<div class="gridbox">
					<div class="grid" ui-grid="grid_tracking" 
						ng-style="{height: (grid_tracking.totalItems*30)+30+'px'}"
						ui-grid-move-columns 
						ui-grid-resize-columns 
						ui-grid-auto-resize ></div>
				--%>
				<div class="gridbox">
					<div class="grid" 
						ng-style="{height: (logistics.omsDeliverytrackings.length*30)+30+17+'px'}"
						ui-grid="logistics.gridOptions" 
						ui-grid-move-columns 
						ui-grid-resize-columns 
						ui-grid-auto-resize ></div>
				</div>
			</div>
		</div>
		<!-- ### //추적 목록 ### -->
		
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="func.popup.close()">
				<b><spring:message code="c.common.close" /></b>
			</button>
		</div>

	</article>
	
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true" />