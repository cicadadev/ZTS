<%--
	화면명 : 상품 관리 > 속성 관리 > 속성 상세정보 팝업
	작성자 : peter
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.attribute.list.js"></script>

<div class="wrap_popup" ng-app="attrApp" ng-cloak ng-controller="pms_attrDetailPopApp_controller as ctrl" ng-init="ctrl.detInit()">
	<h1 class="sub_title1"><spring:message code="c.pmsAttribute.title3" /></h1>
<!-- <form id="frmAttrUpdate"> -->
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col width="10%" />
				<col width="40%" />
				<col width="10%" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr>
					<th><spring:message code="pmsAttribute.name" /> <i><spring:message code="c.input.required" /></i></th>
					<td>
						<input type="text" ng-model="pmsAttribute.name" style="width:34%;" />
					</td>
					<th><spring:message code="c.pmsAttribute.id" /></th>
					<td>
						<span ng-model="pmsAttribute.attributeId">{{ pmsAttribute.attributeId }}</span>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.pmsAttribute.type" /> <i><spring:message code="c.input.required" /></i></th>
					<td>
						<!-- <radio-list ng-model="pmsAttribute.attributeTypeCd" code-group="ATTRIBUTE_TYPE_CD" ></radio-list> -->
						<radio ng-model="pmsAttribute.attributeTypeCd">{{pmsAttribute.attributeTypeName}}</radio>
					</td>
					<th><spring:message code="pmsAttribute.useYn" /> <i><spring:message code="c.input.required" /></i></th>
					<td>
<!-- 						<input type="radio" value="Y" name="radio_set1" id="radio1" ng-model="pmsAttribute.useYn" class="ad_disabled" />
						<label for="radio1">사용</label>
						<input type="radio" value="N" name="radio_set1" id="radio2" ng-model="pmsAttribute.useYn" class="ad_disabled" />
						<label for="radio2">미사용</label> -->
						<radio-yn ng-model="pmsAttribute.useYn" labels="사용,미사용" ></radio-yn>
					</td>
				</tr>
				<tr ng-if="pmsAttribute.attributeTypeCd != 'ATTRIBUTE_TYPE_CD.INPUT'"> 
					<th><spring:message code="c.pmsAttribute.value" /> <i><spring:message code="c.input.required" /></i></th>
					<td colspan="3">
						<ul>
							<li ng-repeat="value in oldAttributeValueArr">
								<input type="text" value="{{ value }}" readonly style="width:34%;" />
							</li>
						</ul>
						<div ng-repeat="value in pmsAttribute.pmsAttributevalues">
							<input type="text" ng-model="value.attributeValue" style="width:34%;" />
							<button type="button" class="btn_minus" ng-show="!($first && $last)" ng-click="ctrl.delAttrValue($index)"><spring:message code="c.common.delete" /></button>
							<button type="button" class="btn_plus" ng-show="$last" ng-click="ctrl.addAttrValue($index)"><spring:message code="c.common.add" /></button>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
<!-- </form> -->
	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray " data-ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /></b>
		</button>
		<button type="button" ng-if="saveBtn" class="btn_type3 btn_type3_purple" data-ng-click="ctrl.saveAttribute()">
			<b><spring:message code="c.common.save" /></b>
		</button>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>