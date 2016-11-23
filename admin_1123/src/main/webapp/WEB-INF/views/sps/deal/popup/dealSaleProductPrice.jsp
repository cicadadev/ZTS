<%--
	화면명 : 딜 관리 > 딜 상품 관리 팝업 > 단품 추가금액 관리 팝업
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/sps.app.deal.list.js"></script>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="wrap_popup" style="min-width:500px;" ng-app="dealApp" ng-controller="sps_dealSaleProductPricePopApp_controller as ctrl" ng-init="ctrl.init()">
	<h1 class="sub_title1"><spring:message code="c.spsDealsaleproductprice.title"/><!-- 단품추가금액관리 --></h1>
	<div class="box_type1">
			
			<form name="salePriceForm"">
			<table class="tb_type1">
				<colgroup>
					<col width="50%" />
					<col width="50%" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							<spring:message code="c.spsDealsaleproductprice.saleProductInfo"/> <!-- 단품정보 -->
						</th>
						<th>
							<spring:message code="c.spsDealsaleproductprice.addPrice"/><!-- 추가금액 -->
						</th>
					</tr>
					<tr ng-repeat="saleProduct in saleProducts" >
						<th>
							{{saleProduct.name}}
						</th>
						<td>
							<input type="text" name="addSalePrice{{$index}}" id="addSalePrice{{$index}}" data-productId="{{saleProduct.saleproductId}}" value="{{spsDealsaleproductprices[$index].addSalePrice}}" number-only/>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
		
		<div class="btn_alignC" style="margin-top:20px;">
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.save()">
				<b><spring:message code="c.common.save" /><!-- 저장 --></b>
			</button>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>