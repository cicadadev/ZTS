<%--
	화면명 : 상품 관리 > 상품 관리 > 상품 상세 팝업 > 단품 옵션 설정 팝업
	작성자 : eddie
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.product.detail.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="wrap_popup" data-ng-app="productDetailApp" data-ng-controller="pmsProductOptionController as ctrl">
<h2 class="sub_title1">옵션등록</h2>

<div class="box_type1 marginT2_1"  ng-repeat="(pIndex, group) in optionGroups">
	<!-- <h3 class="sub_title2">옵션{{$index+1}}</h3> -->
	<!-- <button type="button" class="btn_type2"><b>옵션설정</b></button> -->
 	<div class="btn_alignR">
		<button type="button" class="btn_type2" ng-if="!$first && !isErpProduct && !isApproval" ng-click="ctrl.deleteOptionGroup(pIndex)"><b>옵션삭제</b></button>
	</div>
	<form name="form">
		<table class="tb_type1 marginT1">
			<colgroup>
				<col width="10%">
				<col width="10%">
				<col width="10%">
				<col width="10%">
			</colgroup>
			<thead>
				<tr>
					<th>옵션명</th>
					<th>표준카테고리값 매핑</th>
					<th>옵션값</th>
					<th>표준카테고리값 매핑</th>
				</tr>
			</thead>
            <tbody>
            	<tr ng-repeat="option in group.optionValues">
                    <td ng-if="$first" rowspan="{{group.optionValues.length}}">
                    	<input type="text" ng-readonly="isErpProduct || isApproval" ng-model="group.optionName" style="width:98%" />
                    	<p class="information" ng-show="group.optionName == '' || group.optionName == null">
							필수 입력 항목 입니다.
						</p>
                    </td>
					<td ng-if="$first" rowspan="{{group.optionValues.length}}">
						<select ng-model="group.attributeId" style="width:80%" ng-options="attr.attributeId as attr.name for attr in attributeList" ng-change="selectAttribute(group)">
							<option value="">선택하세요</option>
						</select>
					</td>
					<td>
						<span>
							<input type="text" style="width:70%" ng-model="option.optionValue" ng-readonly="isErpProduct"/>
							<button ng-if="!($first && $last) && !isErpProduct && !isApproval" type="button" class="btn_minus" ng-click="ctrl.deleteOptionValue(pIndex, $index)"></button>
						</span>
						<button ng-if="$last && !isErpProduct" ng-click="ctrl.addOptionValue(pIndex)" type="button" class="btn_plus"></button>
					</td>
					<td><span>
							<select ng-model="option.attributeValue" style="width:80%"  ng-options="value.attributeValue as value.attributeValue for value in group.pmsAttributevalues">
								<option value="">선택하세요</option>
							</select>
						</span>
					</td>
				</tr>
			</tbody>
		</table>	
		</form>	
	</div>


	<div class="btn_alignR marginT2" >
		<button type="button" ng-if="!isErpProduct && !isApproval" class="btn_type1" ng-click="ctrl.addOptionGroup()">
			<b>옵션추가</b>
		</button>
	</div>
	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" data-ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /></b>
		</button>
		<button type="button" class="btn_type3 btn_type3_purple" data-ng-click="ctrl.save()">
			<b><spring:message code="c.common.save" /></b>
		</button>
	</div>	
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>
		