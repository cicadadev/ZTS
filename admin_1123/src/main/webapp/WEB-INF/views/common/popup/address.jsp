<%--
	화면명 : 주소팝업
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%> 

<div class="wrap_popup"  ng-app="ccsAppPopup" data-ng-controller="addressPopupController as ctrl">
		<h1 class="sub_title1">우편번호 검색</h1>
		
		<div class="box_type1">
		<form name="mainProductInsertForm">
			<table class="tb_type1">
				<colgroup>
					<col width="18%" />
					<col width="34%" />
					<col width="13%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							<spring:message code="c.dmsExhibitproduct.mainProduct.productNoNm"/><!-- 상품번호 / 명 --> <i><spring:message code="c.input.required"/><!-- 필수입력 --></i>
						</th>
						<td colspan="3">
							<input data-ng-model="dmsExhibitMainProduct.productId" id="productId" type="text" name="productId" placeholder="" style="width:10%;" readonly disabled required/>
							<input data-ng-model="dmsExhibitMainProduct.productName" id="name" type="text" name="name" placeholder="" style="width:30%;" readonly disabled/>
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.dmsExhibitproduct.mainProduct.name"/><!-- 대표상품 명 --> <i><spring:message code="c.input.required"/><!-- 필수입력 --></i>
						</th>
						<td colspan="3">
							<input data-ng-model="dmsExhibitMainProduct.name" id="mainProductName" type="text" name="mainProductName" placeholder="" style="width:50%;" v-key="dmsExhibitMainProduct.name" required/>
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.dmsExhibitgroup.sortNo"/><!-- 우선 순위 --> <i><spring:message code="c.input.required"/><!-- 필수입력 --></i>
						</th>
						<td colspan="3">
							<input data-ng-model="dmsExhibitMainProduct.sortNo" id="sortNo" type="number" name="sortNo" placeholder="" style="width:50%;" v-key="dmsExhibitMainProduct.sortNo" required/>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
	
		<div class="btn_alignC" style="margin-top:20px;">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b><spring:message code="c.common.close" /><!-- 닫기 --></b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.saveMainProduct()">
				<b><spring:message code="c.common.save" /><!-- 저장 --></b>
			</button>
		</div>
	
</div>

<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>