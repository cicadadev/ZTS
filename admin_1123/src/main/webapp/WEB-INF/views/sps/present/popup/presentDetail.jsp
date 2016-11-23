<%--
	화면명 : 프로모션 관리 > 사은품 프로모션 관리 > 사은품 프로모션 상세
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/sps.app.present.list.js"></script>

<div class="wrap_popup" ng-app="presentPromotionApp" ng-controller="sps_presentPromotionDetailPopApp_controller as ctrl">
	<!-- 사은품 프로모션 상세정보 START -->
	<h2 class="sub_title1">
		<span ng-show="spsPresent.presentId != '' && spsPresent.presentId != null">
			<spring:message code="c.sps.present.detail" /><!-- 타이틀:사은품상세정보 -->
		</span>
		<span ng-show="spsPresent.presentId == undefined">
			<spring:message code="c.sps.present.register" /><!-- 타이틀:사은품등록 -->
		</span>
	</h2>
	<div class="box_type1">
	<form name="form">
			<table class="tb_type1">
				<colgroup>
					<col width="150px;" />
					<col class="col_auto" />
					<col width="150px;" />
					<col class="col_auto" />
				</colgroup>
				<tbody>
					<tr>
						<th><spring:message code="c.sps.present.pop.id" /></th>
						<td>
							{{spsPresent.presentId}}
						</td>
						<th><spring:message code="c.sps.present.pop.name" /><i/></th>
						<td>
							<input name="name" type="text" ng-model="spsPresent.name" value="{{spsPresent.name}}" style="width:190px;" v-key="spsPresent.name"/>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.sps.present.pop.type" /><i/></th>
						<td ng-init="spsPresent.presentTypeCd='PRESENT_TYPE_CD.PRODUCT'">
							<radio-list ng-model="spsPresent.presentTypeCd" code-group="PRESENT_TYPE_CD"></radio-list>
						</td>
						<th><spring:message code="c.sps.present.pop.state" /><i/></th>
						<td ng-init="spsPresent.presentStateCd='PRESENT_STATE_CD.READY'" name="couponState">
							<span ng-show="spsPresent.presentStateCd == 'PRESENT_STATE_CD.READY' ">
								<label for="couponStateCd_rd"><spring:message code="c.sps.common.pop.state.ready" /></label>
							</span>
							<span ng-show="spsPresent.presentStateCd == 'PRESENT_STATE_CD.RUN' ">
								<label for="couponStateCd_rn"><spring:message code="c.sps.common.pop.state.run" /></label>
							</span>
							<span ng-show="spsPresent.presentStateCd == 'PRESENT_STATE_CD.STOP' ">
								<label for="couponStateCd_st"><spring:message code="c.sps.common.pop.state.stop" /></label>
							</span>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.sps.common.period" /><i/></th>
						<td>
							<input type="text" id="startDt" ng-model="spsPresent.startDt" name="startDt" placeholder="" datetime-picker period-start/>
							~
							<input type="text" id="endDt" ng-model="spsPresent.endDt" name="endDt" placeholder="" datetime-picker period-end/>
							<p class="information" ng-show="!spsPresent.startDt || !spsPresent.endDt">필수 입력 항목 입니다.</p>
						</td>
						<th><spring:message code="c.sps.present.pop.order.price" /><i/></th>
						<td ng-if="spsPresent.presentTypeCd=='PRESENT_TYPE_CD.ORDER'">
							<input id="minAmt" type="text" ng-model="spsPresent.minOrderAmt" style="width:100px;"
							ng-disabled="spsPresent.presentTypeCd == 'PRESENT_TYPE_CD.PRODUCT' " price-input ><span><spring:message code="c.common.amt" /></span>
							~
							<input id="maxAmt" type="text" ng-model="spsPresent.maxOrderAmt" style="width:100px;" 
							ng-disabled="spsPresent.presentTypeCd == 'PRESENT_TYPE_CD.PRODUCT' " price-input  ><span><spring:message code="c.common.amt" /></span>
							<p class="information" ng-show="(spsPresent.minOrderAmt =='' || spsPresent.maxOrderAmt =='')">
							<spring:message code="common.require.data"/></p>
						</td>
						<td ng-if="spsPresent.presentTypeCd!='PRESENT_TYPE_CD.ORDER'">
							<input id="minAmt" type="text" ng-model="spsPresent.minOrderAmt" style="width:100px;"
							ng-disabled="spsPresent.presentTypeCd == 'PRESENT_TYPE_CD.PRODUCT' "  ><span><spring:message code="c.common.amt" /></span>
							~
							<input id="maxAmt" type="text" ng-model="spsPresent.maxOrderAmt" style="width:100px;" 
							ng-disabled="spsPresent.presentTypeCd == 'PRESENT_TYPE_CD.PRODUCT' "   ><span><spring:message code="c.common.amt" /></span>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.sps.present.pop.give.cnt" /></th>
						<td>
							<!-- TODO 지급회수 -->
						</td>
						<th><spring:message code="c.sps.common.control.type" /></th><!-- 허용 유형 -->
						<td>
							<control-set model-name="spsPresent" lebels="전체,사용자설정" ></control-set>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.sps.common.deal.apply" /></th><!-- 딜 적용 여부 -->
						<td>
							<radio-yn ng-model="spsPresent.dealApplyYn" labels="적용,미적용" init-val="N"></radio-yn>
							<br/>
							<span ng-hide="spsPresent.dealApplyYn == 'N'">
								<fieldset ng-disabled="spsPresent.presentStateCd != 'PRESENT_STATE_CD.READY'">
									<input id="dealChk" type="checkbox" ng-model="state1" value=false ng-click="ctrl.stateChk('ALL', 'state1')" /> 
									<spring:message code="c.sps.common.pop.target.type.cd.all"/><!-- 전체 -->
									<input type="checkbox" ng-model="state2" value=false ng-click="ctrl.stateChk('DEAL_TYPE_CD.PREMIUM', 'state2')" /> 
									프리미엄관 
									<input type="checkbox" ng-model="state3" value=false ng-click="ctrl.stateChk('DEAL_TYPE_CD.MEMBER', 'state3')" /> 
									<spring:message code="c.sps.common.deal.apply.mem" /> <!-- 멤버쉽관 --> 
									<input type="checkbox" ng-model="state4" value=false ng-click="ctrl.stateChk('DEAL_TYPE_CD.SHOCKDEAL', 'state4')" />
									<spring:message code="c.sps.common.deal.apply.shock" /> <!-- 쇼킹제로관 -->
									<input type="checkbox" ng-model="state5" value=false ng-click="ctrl.stateChk('DEAL_TYPE_CD.EMPLOYEE', 'state5')" /> 
									<spring:message code="c.sps.common.deal.apply.emp" /> <!-- 임직원관 -->
									<input type="checkbox" ng-model="state6" value=false ng-click="ctrl.stateChk('DEAL_TYPE_CD.CHILDREN', 'state6')" /> 
									<spring:message code="c.sps.common.deal.apply.child" /> <!-- 다자녀관 -->
									
									<p class="information" ng-show="spsPresent.dealTypeCds == undefined || spsPresent.dealTypeCds ==''"><spring:message code="common.require.data"/></p>
								</fieldset>
							</span>
						</td>
						<th><spring:message code="c.sps.present.pop.select.cnt" /></th>
						<td ng-init="spsPresent.presentSelectTypeCd='PRESENT_SELECT_TYPE_CD.ALL'">
							<input type="radio" value="PRESENT_SELECT_TYPE_CD.ALL" ng-model="spsPresent.presentSelectTypeCd" />
							<label for="radio5"><spring:message code="c.select.all" /></label>
							<input type="radio" value="PRESENT_SELECT_TYPE_CD.SELECT" ng-model="spsPresent.presentSelectTypeCd" />
							<label for="radio6"><spring:message code="c.input.enter" /></label>
							<input id="inQty" type="tel" ng-model="spsPresent.selectQty" 
							ng-disabled="spsPresent.presentSelectTypeCd=='PRESENT_SELECT_TYPE_CD.ALL'" style="width:45px;" v-key="spsPresent.selectQty"/>
							<span><spring:message code="c.common.unit" /></span>
							<p class="information" ng-show="spsPresent.presentSelectTypeCd!='PRESENT_SELECT_TYPE_CD.ALL' && spsPresent.selectQty == ''"><spring:message code="common.require.data"/></p>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.sps.present.pop.apply.target" /><i/></th>
						<td colspan="3">
							<!-- <radio-list ng-model="spsPresent.ccsApply.targetTypeCd" code-group="TARGET_TYPE_CD"
							 ng-init="spsPresent.ccsApply.targetTypeCd='TARGET_TYPE_CD.ALL'"></radio-list> -->
						 	<span data-ng-repeat="targetCd in targetTypeCds">
								<input type="radio" data-ng-model="spsPresent.ccsApply.targetTypeCd" value="{{targetCd.val}}" 
								ng-show="!(spsPresent.presentTypeCd=='PRESENT_TYPE_CD.PRODUCT' && $index == '0')"/>
								<label style="margin:0 14px 0 7px;" 
								ng-show="!(spsPresent.presentTypeCd=='PRESENT_TYPE_CD.PRODUCT' && $index == '0')">{{targetCd.text}}</label>
							</span>
						</td>
					</tr>
				</tbody>
			</table>
	</form>
	
	<form name="gridForm">
			<table class="tb_type1">
				<colgroup>
					<col width="150px;" />
					<col class="col_auto" />
				</colgroup>
				<tbody>
					<%--1. 적용대상 전체가 아닐때 --%>
						<tr ng-show="div_pr">
							<th>
								<spring:message code="c.sps.common.pop.target.type.cd.product"/><!-- 적용 상품 -->
							</th>
							<td>
							<div class="btn_alignR marginT1" ng-hide="spsPresent.presentStateCd != 'PRESENT_STATE_CD.READY' && changeFlag == false">
								<button type="button" name="targetSave" class="btn_type2" ng-click="ctrl.addExcel('Pr')">
									<b><spring:message code="c.excel.file.upload" /><!-- 일괄등록(엑셀) --></b>
								</button>
								
								<button type="button"  name="targetSave" class="btn_type2" ng-click="ctrl.productPopup()">
									<b><spring:message code="c.sps.present.add.product" /></b>
								</button>
								<button type="button" class="btn_type2" ng-click="ctrl.deleteGridData('applyTargetPrGrid')">
									<b><spring:message code="common.btn.del" /></b>
								</button>					
							</div>
							<div class="box_type1 marginT1" >
								<h3 class="sub_title2">
									<spring:message code="c.sps.present.apply.list2" />
									<span><spring:message code="c.search.totalCount" arguments="{{ applyTargetPr.data.length}}" /></span>
								</h3>
								<div class="gridbox gridbox300">
									<div class="grid" data-ui-grid="applyTargetPr" 
									data-ui-grid-move-columns 
									data-ui-grid-resize-columns 
									data-ui-grid-auto-resize 
									data-ui-grid-selection 
									data-ui-grid-row-edit
									data-ui-grid-cell-nav
									data-ui-grid-exporter
									data-ui-grid-edit 
									data-ui-grid-validate
										>
									</div>
								</div>
								<p class="information" ng-show="applyTargetPr.data.length==0"><spring:message code="common.require.data"/></p>
							</div>
							</td>
						</tr>

						<tr ng-show="div_br">
							<th>
								<spring:message code="c.sps.common.pop.target.type.cd.brand"/><!-- 적용 브랜드 -->
							</th>
							<td>
							<div class="btn_alignR marginT1" ng-hide="spsPresent.presentStateCd != 'PRESENT_STATE_CD.READY' && changeFlag == false">
								<button type="button" name="targetSave" class="btn_type2" ng-click="ctrl.addExcel('Br')" >
									<b><spring:message code="c.excel.file.upload" /></b>
								</button>
								<button type="button"  name="targetSave" class="btn_type2" ng-click="ctrl.brandPopup()">
									<b><spring:message code="c.sps.present.add.brand" /></b>
								</button>
								<button type="button" class="btn_type2" ng-click="ctrl.deleteGridData('applyTargetBrGrid')">
									<b><spring:message code="common.btn.del" /></b>
								</button>					
							</div>
							<div class="box_type1 marginT1" >
								<h3 class="sub_title2">
									<spring:message code="c.sps.present.apply.list4" />
									<span><spring:message code="c.search.totalCount" arguments="{{ applyTargetBr.data.length}}" /></span>
								</h3>
								<div class="gridbox gridbox300">
									<div class="grid" data-ui-grid="applyTargetBr" 
									data-ui-grid-move-columns 
									data-ui-grid-resize-columns 
									data-ui-grid-auto-resize 
									data-ui-grid-selection 
									data-ui-grid-row-edit
									data-ui-grid-cell-nav
									data-ui-grid-exporter
									data-ui-grid-edit 
									data-ui-grid-validate
										>
									</div>
								</div>
								<p class="information" ng-show="applyTargetBr.data.length==0"><spring:message code="common.require.data"/></p>
							</div>
							</td>
						</tr>
						
						<tr ng-show="div_ca">
							<th>
								<spring:message code="c.sps.common.pop.target.type.cd.category"/><!-- 적용 카테고리 -->
							</th>
							<td>
							<div class="btn_alignR marginT1" ng-hide="spsPresent.presentStateCd != 'PRESENT_STATE_CD.READY' && changeFlag == false">
								<button type="button" name="targetSave" class="btn_type2" ng-click="ctrl.addExcel('Ca')">
									<b><spring:message code="c.excel.file.upload" /></b>
								</button>
								<button type="button" name="targetSave"  class="btn_type2" ng-click="ctrl.categoryPopup()">
									<b><spring:message code="c.sps.present.add.category" /></b>
								</button>
								<button type="button" class="btn_type2" ng-click="ctrl.deleteGridData('applyTargetCaGrid')">
									<b><spring:message code="common.btn.del" /></b>
								</button>					
							</div>
							<div class="box_type1 marginT1" >
								<h3 class="sub_title2">
									<spring:message code="c.sps.present.apply.list3" />
									<span><spring:message code="c.search.totalCount" arguments="{{ applyTargetCa.data.length}}" /></span>
								</h3>
								<div class="gridbox gridbox300">
									<div class="grid" data-ui-grid="applyTargetCa" 
									data-ui-grid-move-columns 
									data-ui-grid-resize-columns 
									data-ui-grid-auto-resize 
									data-ui-grid-selection 
									data-ui-grid-row-edit
									data-ui-grid-cell-nav
									data-ui-grid-exporter
									data-ui-grid-edit 
									data-ui-grid-validate
										>
									</div>
								</div>
								<p class="information" ng-show="applyTargetCa.data.length==0"><spring:message code="common.require.data"/></p>
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
							<div class="btn_alignR marginT1" ng-hide="spsPresent.presentStateCd == 'PRESENT_STATE_CD.STOP'">
								<button type="button" ng-click="ctrl.addExcel('ex')" class="btn_type2">
									<b><spring:message code="c.excel.file.upload" /></b>
								</button>
								<button type="button"  class="btn_type2" ng-click="ctrl.productPopup('exclude')">
									<b><spring:message code="c.sps.present.add.product" /></b>
								</button>
								<button type="button" class="btn_type2" ng-click="ctrl.deleteGridData('excludeProductGrid')">
									<b><spring:message code="common.btn.del" /></b>
								</button>					
<!-- 								<button type="button" class="btn_type2" ng-click="ctrl.saveGridData('excludeProductGrid')"> -->
<%-- 									<b><spring:message code="common.btn.save" /></b> --%>
<!-- 								</button> -->
							</div>
							<div class="box_type1 marginT1">
								<h3 class="sub_title2">
									<spring:message code="c.sps.present.unproduct.list" />
									<span><spring:message code="c.search.totalCount" arguments="{{ excludeProduct.data.length}}" /></span>
								</h3>
								<div class="gridbox gridbox300" >
									<div class="grid" data-ui-grid="excludeProduct" 
									data-ui-grid-move-columns 
									data-ui-grid-resize-columns 
									data-ui-grid-auto-resize 
									data-ui-grid-selection 
									data-ui-grid-row-edit
									data-ui-grid-cell-nav
									data-ui-grid-exporter
									data-ui-grid-edit 
									data-ui-grid-validate
									></div>
								</div>
							</div>
							</td>
						</tr>
					<%--2. 제외대상 그리드 END --%>
					<%--3. 제공 사은품 목록 --%>
						<tr>
							<th>
								<spring:message code="c.sps.present.give.product" /><!-- 제공 사은품 -->
							</th>
							<td>
							<div class="btn_alignR marginT1" ng-hide="spsPresent.presentStateCd != 'PRESENT_STATE_CD.READY' && changeFlag == false">
								<button type="button"  class="btn_type2" ng-click="ctrl.presentPopup()">
									<b><spring:message code="c.sps.present.add.present" /></b>
								</button>
								<button type="button" class="btn_type2" ng-click="ctrl.deleteGridData('presentProductGrid')">
									<b><spring:message code="common.btn.del" /></b>
								</button>					
							</div>
							<div class="box_type1 marginT1">
								<h3 class="sub_title2">
									<spring:message code="c.sps.present.give.product.list" />
									<span><spring:message code="c.search.totalCount" arguments="{{ presentProduct.data.length}}" /></span>
								</h3>
								<div class="gridbox gridbox300" >
									<div class="grid" data-ui-grid="presentProduct" 
										data-ui-grid-move-columns 
										data-ui-grid-resize-columns
										data-ui-grid-auto-resize 
										data-ui-grid-selection 
										data-ui-grid-row-edit
										data-ui-grid-cell-nav
										data-ui-grid-exporter
										data-ui-grid-edit 
										data-ui-grid-validate
										>
									</div>
								</div>
								<p class="information" ng-show="presentProduct.data.length==0"><spring:message code="common.require.data"/></p>
							</div>
							</td>
						</tr>
						<%--3. 제공 사은품 목록 END--%>
				</tbody>
			</table>
		</form>
	</div>
	
	<div class="btn_alignC marginT3">
		<button type="button" ng-click="ctrl.cancelPopup()" class="btn_type3 btn_type3_gray">
			<b><spring:message code="c.common.close"/></b>
		</button>
		<span ng-show="spsPresent.presentStateCd == 'PRESENT_STATE_CD.RUN' ">
			<button name="mainStop" type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.presentStop()">
				<b><spring:message code="c.sps.common.pop.state.stop"/></b>
			</button>
		</span>
		<span ng-show="spsPresent.presentId != undefined && spsPresent.presentId != '' && spsPresent.presentStateCd == 'PRESENT_STATE_CD.READY' ">
			<button name="mainRun" type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.presentRun()">
				<b><spring:message code="c.sps.common.pop.state.btn.run"/></b>
			</button>
		</span>
		<span ng-show="spsPresent.presentStateCd != 'PRESENT_STATE_CD.STOP' ">
			<button type="button" name="mainSave" ng-click="ctrl.updatePresentPromotion()" class="btn_type3 btn_type3_purple" >
				<b><spring:message code="c.common.save"/></b>
			</button>
		</span>
	</div>
					
<%-- 프로모션 등록 완료후 그리드 Show --%>
	
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>