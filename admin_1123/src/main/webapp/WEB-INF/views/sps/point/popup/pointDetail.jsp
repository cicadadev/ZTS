<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/sps.app.point.manager.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="wrap_popup" ng-app="pointManagerApp" ng-controller="sps_pointDetailPopupController as ctrl">
	<h1 class="sub_title1">
		<span ng-show="spsPointsave.pointSaveId != '' && spsPointsave.pointSaveId != null">
			<spring:message code="c.sps.point.detail" /><!-- 타이틀:상세 -->
		</span>
		<span ng-show="spsPointsave.pointSaveId == undefined">
			<spring:message code="c.sps.point.reg" /><!-- 타이틀:등록 -->
		</span>
	</h1>
	<form name="form">
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col class="col_142" />
				<col class="col_442" />
				<col class="col_142" />
				<col class="col_auto" />
			</colgroup>
			<tbody>
				<tr>
					<th>
						<spring:message code="c.sps.point.pop.id" /> <!-- 포인트 적립ID -->
					</th>
					<td>{{spsPointsave.pointSaveId}}</td>
					<th>
					<spring:message code="c.sps.point.pop.name" /> <!-- 포인트 적립명 --> <i/>
					</th>
					<td><input name="name" type="text" ng-model="spsPointsave.name" style="width:50%;" v-key="spsPointsave.name"/></td>
				</tr>
				<tr>
					<th><spring:message code="c.sps.point.pop.state" /> <!-- 포인트 프로모션 상태 --><i/></th>
					<td colspan="3" ng-init="spsPointsave.pointSaveStateCd='POINT_SAVE_STATE_CD.READY'" name="couponState">
						<span ng-show="spsPointsave.pointSaveStateCd == 'POINT_SAVE_STATE_CD.READY'">
							<label for="couponStateCd_rd"><spring:message code="c.sps.common.pop.state.ready" /></label>
						</span>
						<span ng-show="spsPointsave.pointSaveStateCd == 'POINT_SAVE_STATE_CD.RUN' ">
							<label for="couponStateCd_rn"><spring:message code="c.sps.common.pop.state.run" /></label>
						</span>
						<span ng-show="spsPointsave.pointSaveStateCd == 'POINT_SAVE_STATE_CD.STOP' ">
							<label for="couponStateCd_st"><spring:message code="c.sps.common.pop.state.stop" /></label>
						</span>
					</td>
				</tr>
				<tr>
					<th><spring:message code="c.sps.common.period" /> <!-- 포인트 프로모션 기간 --><i/></th>
					<td colspan="3">
						<input type="text" ng-model="spsPointsave.startDt" placeholder="" datetime-picker period-start/>											
						~
						<input type="text" ng-model="spsPointsave.endDt" placeholder=""  datetime-picker period-end/>
						<p class="information" ng-show="!spsPointsave.startDt || !spsPointsave.endDt">필수 입력 항목 입니다.</p>
<%-- 						<p class="information" ng-show="!spsPointsave.startDt.$valid || !spsPointsave.endDt.$valid"><spring:message code="c.common.invalid.content" /></p> --%>
					</td>
				</tr>				
				<tr>
					<th><spring:message code="c.sps.point.pop.give" /> <!-- 지급 포인트 --><i/></th>
					<td colspan="3">
						<input type="radio" ng-model="spsPointsave.pointSaveTypeCd" value="POINT_SAVE_TYPE_CD.MULTIPLY" ng-init="spsPointsave.pointSaveTypeCd = 'POINT_SAVE_TYPE_CD.MULTIPLY'"/> 
						<spring:message code="c.sps.point.pop.multi.save" /> <!-- 배수적립 -->
						<input type="text" ng-model="spsPointsave.pointValueM" name="pointValueM" placeholder="" style="width:50;" 
						ng-required="spsPointsave.pointSaveTypeCd == 'POINT_SAVE_TYPE_CD.MULTIPLY'" ng-disabled="spsPointsave.pointSaveTypeCd == 'POINT_SAVE_TYPE_CD.ADD'" v-key="spsPointsave.pointValue"/> 
						<spring:message code="c.sps.point.pop.multi" /> <!-- 배수 -->
						<input type="radio" ng-model="spsPointsave.pointSaveTypeCd" value="POINT_SAVE_TYPE_CD.ADD" /> 
						<spring:message code="c.sps.point.pop.add.save" /> <!-- 추가적립 --><!-- 포인트 -->
						<input type="text" ng-model="spsPointsave.pointValueA" name="pointValueA" placeholder="" style="width:50;" 
						ng-required="spsPointsave.pointSaveTypeCd == 'POINT_SAVE_TYPE_CD.ADD'" ng-disabled="spsPointsave.pointSaveTypeCd == 'POINT_SAVE_TYPE_CD.MULTIPLY'" v-key="spsPointsave.pointValue"/>
						<span><spring:message code="c.sps.point.pop.point" /></span>
					</td>
				</tr>				
				<tr>
					<th><spring:message code="c.sps.common.control.type" /> <!-- 허용 유형 --></th>
					<td colspan="3">
						<%-- flag="spsPointsave.pointSaveStateCd == 'POINT_SAVE_STATE_CD.READY' ? true : false" --%>
						<control-set model-name="spsPointsave" lebels="전체,사용자설정" ></control-set>
					</td>
				</tr>				
				<tr>
					<th><spring:message code="c.sps.point.pop.apply.target" /> <!-- 프로모션 적용대상 --><i/></th>
					<td colspan="3">
						<radio-list ng-init="spsPointsave.ccsApply.targetTypeCd='TARGET_TYPE_CD.ALL'" ng-model="spsPointsave.ccsApply.targetTypeCd" code-group="TARGET_TYPE_CD"></radio-list>
					</td>
				</tr>															
			</tbody>
		</table>
	</div>
	</form>
	
	<form name="gridForm">
		<table class="tb_type1">
			<colgroup>
				<col class="col_142" />
				<col class="col_auto" />
			</colgroup>
			<tbody>
				<%--1. 적용대상 전체가 아닐때 --%>
					<tr ng-show="div_pr">
						<th>
							<spring:message code="c.sps.common.pop.target.type.cd.product"/><!-- 적용 상품 -->
						</th>
						<td>
						<div class="btn_alignR marginT1" ng-hide="spsPointsave.pointsaveStateCd != 'POINT_SAVE_STATE_CD.READY' && changeFlag == false">
							<button type="button" name="targetSave" class="btn_type2" ng-click="ctrl.addExcel('pr')">
								<b><spring:message code="c.excel.file.upload" /></b>
							</button>
							<button type="button" name="targetSave" class="btn_type2" ng-click="ctrl.addPopup('pr')">
								<b><spring:message code="c.common.add" /></b>
							</button>
							<button type="button" class="btn_type2" ng-click="ctrl.deleteGridData('pr')">
								<b><spring:message code="c.common.delete" /></b>
							</button>	
						</div>
						<div class="box_type1 marginT1">
							<h3 class="sub_title2">
								<spring:message code="c.sps.point.apply.list2" /> <!-- 적용대상상품 목록 -->
								<span><spring:message code="c.search.totalCount" arguments="{{ grid_pr.data.length }}" /></span>
							</h3>
							
							<div class="gridbox gridbox300">
								<div class="grid" data-ui-grid="grid_pr"
								data-ui-grid-move-columns data-ui-grid-resize-columns
								data-ui-grid-auto-resize data-ui-grid-exporter
								data-ui-grid-row-edit data-ui-grid-edit
								></div>
							</div>
					 		<p class="information" ng-show="grid_pr.data.length==0"><spring:message code="common.require.data"/></p>
						</div>
						</td>
					</tr>
	
					<tr ng-show="div_br">
						<th>
							<spring:message code="c.sps.common.pop.target.type.cd.brand"/><!-- 적용 브랜드 -->
						</th>
						<td>
						<div class="btn_alignR marginT1" ng-hide="spsPointsave.pointsaveStateCd != 'POINT_SAVE_STATE_CD.READY' && changeFlag == false">
							<button type="button" name="targetSave" class="btn_type2" ng-click="ctrl.addExcel('br')">
								<b><spring:message code="c.excel.file.upload" /></b>
							</button>
							<button type="button" name="targetSave" class="btn_type2" ng-click="ctrl.addPopup('br')">
								<b><spring:message code="c.common.add" /></b>
							</button>
							<button type="button" class="btn_type2" ng-click="ctrl.deleteGridData('br')">
								<b><spring:message code="c.common.delete" /></b>
							</button>	
						</div>
						<div class="box_type1 marginT1">
							<h3 class="sub_title2">
								<spring:message code="c.sps.point.apply.list4" /> <!-- 적용대상 브랜드 목록 -->
								<span><spring:message code="c.search.totalCount" arguments="{{ grid_br.data.length }}" /></span>
							</h3>
							
							<div class="gridbox gridbox300">
								<div class="grid" data-ui-grid="grid_br"
								data-ui-grid-move-columns data-ui-grid-resize-columns
								data-ui-grid-auto-resize data-ui-grid-exporter
								data-ui-grid-edit data-ui-grid-row-edit
								></div>
							</div>
							<p class="information" ng-show="grid_br.data.length==0"><spring:message code="common.require.data"/></p>
						</div>
						</td>
					</tr>
					
					<tr ng-show="div_ca">
						<th>
							<spring:message code="c.sps.common.pop.target.type.cd.category"/><!-- 적용 카테고리 -->
						</th>
						<td>
						<div class="btn_alignR marginT1" ng-hide="spsPointsave.pointsaveStateCd != 'POINT_SAVE_STATE_CD.READY' && changeFlag == false">
							<button type="button" name="targetSave" class="btn_type2" ng-click="ctrl.addExcel('ca')" >
								<b><spring:message code="c.excel.file.upload" /></b>
							</button>
							<button type="button" name="targetSave" class="btn_type2" ng-click="ctrl.addPopup('ca')">
								<b><spring:message code="c.common.add" /></b>
							</button>
							<button type="button" class="btn_type2" ng-click="ctrl.deleteGridData('ca')">
								<b><spring:message code="c.common.delete" /></b>
							</button>	
						</div>
						<div class="box_type1 marginT1">
							<h3 class="sub_title2">
								<spring:message code="c.sps.point.apply.list3" /> <!-- 적용대상 전시카테고리 목록 -->
								<span><spring:message code="c.search.totalCount" arguments="{{ grid_ca.data.length }}" /></span>
							</h3>
					
							<div class="gridbox gridbox300">
								<div class="grid" data-ui-grid="grid_ca"
								data-ui-grid-move-columns data-ui-grid-resize-columns
								data-ui-grid-auto-resize data-ui-grid-exporter
								data-ui-grid-edit data-ui-grid-row-edit
								></div>
							</div>
							<p class="information" ng-show="grid_ca.data.length==0"><spring:message code="common.require.data"/></p>
						</div>
						</td>
					</tr>
				<%--1. 적용대상 그리드 END --%>
				<%--2. 제외대상. 적용대상이 상품이 아닐때 --%>
					<tr ng-show="div_ex">
						<th>
							<spring:message code="c.sps.common.pop.excproduct"/> <!-- 제외 상품 -->
						</th>
						<td>
						<div class="btn_alignR marginT1" ng-hide="spsPointsave.pointsaveStateCd == 'POINT_SAVE_STATE_CD.STOP'">
							<button type="button" ng-click="ctrl.addExcel('ex')" class="btn_type2">
								<b><spring:message code="c.excel.file.upload" /></b>
							</button>
							<button type="button"  class="btn_type2" ng-click="ctrl.addPopup('ex')">
								<b><spring:message code="c.common.add" /></b>
							</button>
							<button type="button" class="btn_type2" ng-click="ctrl.deleteGridData('ex')">
								<b><spring:message code="c.common.delete" /></b>
							</button>	
						</div>
						<div class="box_type1 marginT1">
							<h3 class="sub_title2">
								<spring:message code="c.sps.point.excproduct.list" /> <!-- 제외상품 목록 -->
								<span><spring:message code="c.search.totalCount" arguments="{{ grid_ex.data.length }}" /></span>
							</h3>
							
							<div class="gridbox gridbox300">
								<div class="grid" data-ui-grid="grid_ex"
								data-ui-grid-move-columns data-ui-grid-resize-columns
								data-ui-grid-auto-resize data-ui-grid-exporter
								data-ui-grid-row-edit data-ui-grid-edit
								></div>
							</div>
					
						</div>
						</td>
					</tr>
				<%--2. 제외대상 그리드 END --%>	
			</tbody>
		</table>
	</form>
	
	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
			<b><spring:message code="c.common.close"/><!-- 닫기 --></b>
		</button>
		<span ng-show="spsPointsave.pointSaveStateCd == 'POINT_SAVE_STATE_CD.RUN'">
			<button name="mainStop" type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.pointStop()">
				<b><spring:message code="c.sps.common.pop.state.stop"/></b>
			</button>
		</span>
		<span ng-show="spsPointsave.pointSaveId!=undefined && spsPointsave.pointSaveId != '' && spsPointsave.pointSaveStateCd == 'POINT_SAVE_STATE_CD.READY'">
			<button name="mainRun" type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.pointRun()">
				<b><spring:message code="c.sps.common.pop.state.btn.run"/></b>
			</button>
		</span>
		<span ng-show="spsPointsave.pointSaveStateCd != 'POINT_SAVE_STATE_CD.STOP'">
			<button type="button" name="mainSave" class="btn_type3 btn_type3_purple" ng-click="ctrl.savePoint()">
				<b><spring:message code="c.common.save"/><!-- 저장 --></b>
			</button>
		</span>
	</div>

</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>
		