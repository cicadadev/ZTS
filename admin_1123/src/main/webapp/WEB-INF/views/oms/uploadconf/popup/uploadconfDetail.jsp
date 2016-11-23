<%--
	화면명 : 주문 관리 > 주문업로드설정 > 주문업로드설정 상세 팝업
	작성자 : peter
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/oms.app.uploadconf.list.js"></script>

<div class="wrap_popup" ng-app="uploadConfApp" ng-cloak ng-controller="oms_uploadConfUpdPopupApp_controller as ctrl" ng-init="ctrl.detInit()">
	<h1 class="sub_title1"><spring:message code="c.oms.uploadconf.detailTitle" /></h1>
<form name="frmUploadconfDetail">
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col width="10%" />
				<col width="23%" />
				<col width="10%" />
				<col width="23%" />
				<col width="12%" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr>
					<th><spring:message code="c.oms.uploadconf.siteId" /> <i><spring:message code="c.input.required" /></i></th><!-- 사이트코드 -->
					<td>
						<span ng-model="omsUploadconf.siteId">{{ omsUploadconf.siteId }}</span>
					</td>
					<th><spring:message code="omsUploadconf.titleRow" /> <i><spring:message code="c.input.required" /></i></th><!-- 제목행 -->
					<td>
						<input type="text" ng-model="omsUploadconf.titleRow" style="width:90%;" v-key="required" />
					</td>
					<th><spring:message code="omsUploadconf.dataRow" /> <i><spring:message code="c.input.required" /></i></th><!-- 데이터행 -->
					<td>
						<input type="text" ng-model="omsUploadconf.dataRow" style="width:90%;" v-key="required" />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.oms.uploadconf.siteOrderId" /> <i><spring:message code="c.input.required" /></i></th><!-- 외부몰주문번호 -->
					<td>
						<input type="text" ng-model="omsUploadconf.siteOrderId" style="width:90%;" v-key="required" />
					</td>
					<th><spring:message code="c.oms.uploadconf.saleproductId1" /> <i><spring:message code="c.input.required" /></i></th><!-- 단품코드1 -->
					<td>
						<input type="text" ng-model="omsUploadconf.saleproductId1" style="width:90%;" v-key="required" />
					</td>
					<th><spring:message code="c.oms.uploadconf.saleproductId2" /></th><!-- 단품코드2 -->
					<td>
						<input type="text" ng-model="omsUploadconf.saleproductId2" style="width:90%;" />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.oms.uploadconf.saleproductId3" /></th><!-- 단품코드3 -->
					<td>
						<input type="text" ng-model="omsUploadconf.saleproductId3" style="width:90%;" />
					</td>
					<th><spring:message code="c.oms.uploadconf.saleproductId4" /></th><!-- 단품코드4 -->
					<td>
						<input type="text" ng-model="omsUploadconf.saleproductId4" style="width:90%;" />
					</td>
					<th><spring:message code="c.oms.uploadconf.saleproductId5" /></th><!-- 단품코드5 -->
					<td>
						<input type="text" ng-model="omsUploadconf.saleproductId5" style="width:90%;" />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.oms.uploadconf.salePrice" /></th><!-- 판매단가 -->
					<td>
						<input type="text" ng-model="omsUploadconf.salePrice" style="width:90%;" />
					</td>
					<th><spring:message code="c.oms.uploadconf.orderQty" /> <i><spring:message code="c.input.required" /></i></th><!-- 수량 -->
					<td>
						<input type="text" ng-model="omsUploadconf.orderQty" style="width:90%;" v-key="required" />
					</td>
					<th></th>
					<td></td>
				</tr>
				<tr>
					<th><spring:message code="omsUploadconf.zipCd" /> <i><spring:message code="c.input.required" /></i></th><!-- 우편번호 -->
					<td>
						<input type="text" ng-model="omsUploadconf.zipCd" style="width:90%;" v-key="required" />
					</td>
					<th><spring:message code="omsUploadconf.address1" /> <i><spring:message code="c.input.required" /></i></th><!-- 주소1 -->
					<td>
						<input type="text" ng-model="omsUploadconf.address1" style="width:90%;" v-key="required" />
					</td>
					<th><spring:message code="omsUploadconf.address2" /></th><!-- 주소2 -->
					<td>
						<input type="text" ng-model="omsUploadconf.address2" style="width:90%;" />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.oms.uploadconf.phone1" /> <i><spring:message code="c.input.required" /></i></th><!-- 전화번호 -->
					<td>
						<input type="text" ng-model="omsUploadconf.phone1" style="width:90%;" v-key="required" />
					</td>
					<th><spring:message code="c.oms.uploadconf.phone2" /> <i><spring:message code="c.input.required" /></i></th><!-- 휴대폰번호 -->
					<td>
						<input type="text" ng-model="omsUploadconf.phone2" style="width:90%;" v-key="required" />
					</td>
					<th><spring:message code="c.oms.uploadconf.name" /> <i><spring:message code="c.input.required" /></i></th><!-- 수취인명 -->
					<td>
						<input type="text" ng-model="omsUploadconf.name" style="width:90%;" v-key="required" />
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.oms.uploadconf.note" /></th><!-- 배송메시지 -->
					<td>
						<input type="text" ng-model="omsUploadconf.note" style="width:90%;" />
					</td>
					<th><spring:message code="c.oms.uploadconf.lpNo" /></th><!-- LP_NO -->
					<td>
						<input type="text" ng-model="omsUploadconf.lpNo" style="width:90%;" />
					</td>
					<th><spring:message code="c.oms.uploadconf.bondYn" /></th><!-- 보세여부 -->
					<td>
						<input type="text" ng-model="omsUploadconf.localDelivery" style="width:90%;" />
					</td>
				</tr>
				<tr>
					<th><spring:message code="omsUploadconf.currencyCd" /></th><!-- 통화코드 -->
					<td>
						<input type="text" ng-model="omsUploadconf.currencyCd" style="width:90%;" />
					</td>
					<th><spring:message code="c.oms.uploadconf.currencyPrice" /></th><!-- 외화단가 -->
					<td>
						<input type="text" ng-model="omsUploadconf.currencyPrice" style="width:90%;" />
					</td>
					<th></th>
					<td></td>
				</tr>
			</tbody>
		</table>
	</div>
</form>
<br />
※ 판매단가의 경우 제휴사 주문항목에 판매단가와 정확하게 매핑되는 항목이 없는 경우 사칙연산을 통해 단가를 표현해야 합니다. (예: 판매금액/수량)<br />
※ 전화번호, 휴대폰번호, 우편번호와 같이 한 개인 항목이 제휴사에서는 두 개 이상의 항목으로 관리되는 경우 ‘항목1&항목2’로 표현해야 합니다.<br />

	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" data-ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /></b>
		</button>
		<button type="button" class="btn_type3 btn_type3_purple" data-ng-click="ctrl.saveUploadconf()">
			<b><spring:message code="c.common.save" /></b>
		</button>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>