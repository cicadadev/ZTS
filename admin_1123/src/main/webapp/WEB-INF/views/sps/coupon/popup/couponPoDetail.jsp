<%--
	화면명 : 프로모션 관리 > 쿠폰 관리 > 쿠폰 상세
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/sps.app.coupon.list.js"></script>
	
<div class="wrap_popup" ng-app="couponApp" ng-controller="sps_couponDetailPopApp_controller as ctrl">
		<h2 class="sub_title1">
			<span ng-show="spsCoupon.couponId != '' && spsCoupon.couponId != null">
				<span ng-show="spsCoupon.couponCopy == false">
					<spring:message code="c.sps.coupon.pop.detail.info" /><!-- 타이틀:쿠폰상세기본정보 -->
				</span>
				<span ng-show="spsCoupon.couponCopy == true">
					<spring:message code="c.sps.coupon.create" /><!-- 타이틀:쿠폰등록(복사) -->
				</span>
			</span>
			<span ng-show="spsCoupon.couponId == undefined">
				<spring:message code="c.sps.coupon.create" /><!-- 타이틀:쿠폰등록 -->
			</span>
		</h2>
		<ul class="tab_type2">
			<li id="setTab1" class="on">
				<button type="button" name="insert"><spring:message code="c.sps.coupon.pop.basic.info" /> <!-- 쿠폰 기본정보 설정 --></button>
			</li>
			<li id="issuedTab2" class="">
				<button type="button" ng-click="ctrl.moveTab()"><spring:message code="c.sps.coupon.pop.issued.list" /><!-- 쿠폰 발급 내역 --></button>
			</li>
		</ul>
		<div class="box_type1 marginT2">
			
			<form name="form" >
			<table class="tb_type1">
				<colgroup>
					<col class="col_142" />
					<col class="col_auto" />
					<col class="col_142" />
					<col class="col_auto" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							<spring:message code="c.sps.coupon.coupon.id" /><!-- 쿠폰번호 -->
						</th>
						<td>{{spsCoupon.couponId}}</td>
						<th>
							<spring:message code="spsCoupon.name" /> <!-- 쿠폰명 --><i/>
						</th>
						<td>
							<input type="text" name="couponName" ng-model="spsCoupon.name" v-key="spsCoupon.name" style="width:98%;" />
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.sps.coupon.pop.coupon.type" /><!-- 쿠폰유형 --><i/>
						</th>
						<td ng-init="spsCoupon.couponTypeCd='COUPON_TYPE_CD.PRODUCT'">
							<span>
								<label>상품할인쿠폰</label>
							</span>
						</td>
						<th>
							<spring:message code="c.sps.coupon.pop.coupon.state" /><!-- 쿠폰상태 --><i/>
						</th>
						<td ng-init="spsCoupon.couponStateCd='COUPON_STATE_CD.READY'" name="couponState">
							<span ng-show="spsCoupon.couponStateCd == 'COUPON_STATE_CD.READY'">
								<label for="couponStateCd_rd"><spring:message code="c.sps.common.pop.state.ready" /></label>
							</span>
							<span ng-show="spsCoupon.couponStateCd == 'COUPON_STATE_CD.RUN'">
								<label for="couponStateCd_rn"><spring:message code="c.sps.common.pop.state.run" /></label>
							</span>
							<span ng-show="spsCoupon.couponStateCd == 'COUPON_STATE_CD.STOP'">
								<label for="couponStateCd_st"><spring:message code="c.sps.common.pop.state.stop" /></label>
							</span>
						</td>
					</tr>
					
<%-- PO 설정 area START--%>
					<tr>
						<input type="hidden" ng-init="spsCoupon.affiliateYn='Y'"/><%--제휴 Y --%>
						<input type="hidden" ng-init="spsCoupon.publicCin='N'"/><%--인증번호사용 N --%>
						<th>
							<spring:message code="c.sps.coupon.pop.downlaod.show" /><!-- 상품상세 다운로드 버튼 노출 여부-->
						</th>
						<td ng-init="spsCoupon.downShowYn='Y'">
							<label ><spring:message code="c.sps.coupon.pop.downlaod.show.yes" /></label>
						</td>
						<th>
							<spring:message code="c.sps.coupon.pop.issue.term" /> <!-- 발급기간 --><i/>
						</th>
						<td>
				            <input type="text" value="" placeholder="" ng-model="spsCoupon.issueStartDt" datetime-picker period-start/>
							~
							<input type="text" value="" placeholder="" ng-model="spsCoupon.issueEndDt" datetime-picker period-end/>
						</td>
					</tr>
					
					<tr>
						<th>
							<spring:message code="c.sps.coupon.pop.dc" /> <!-- 할인구분 --><i/>
						</th>
						<td ng-init="spsCoupon.dcApplyTypeCd='DC_APPLY_TYPE_CD.AMT'" >
							<span class="line_group"><!-- 정액 -->
							   <input type="radio" value="DC_APPLY_TYPE_CD.AMT" name="dcApplyTypeCd" ng-model="spsCoupon.dcApplyTypeCd" ng-click="valueInital()"
							   ng-disabled="spsCoupon.couponTypeCd =='COUPON_TYPE_CD.WRAP' || spsCoupon.couponTypeCd =='COUPON_TYPE_CD.DELIVERY'" />
								<label><spring:message code="c.sps.coupon.pop.dc.apply.type.amt" /></label>
								<input type="text" id="inAmt" ng-model="spsCoupon.dcValueAMT" style="width:111px;" number-only
								ng-disabled="spsCoupon.dcApplyTypeCd == 'DC_APPLY_TYPE_CD.RATE' 
								|| spsCoupon.couponTypeCd =='COUPON_TYPE_CD.WRAP' || spsCoupon.couponTypeCd =='COUPON_TYPE_CD.DELIVERY'"/>
								<span><spring:message code="c.sps.coupon.pop.dc.amt"/><!-- 원 할인 --></span>
							</span>
							<span ng-hide="spsCoupon.couponTypeCd =='COUPON_TYPE_CD.WRAP' || spsCoupon.couponTypeCd =='COUPON_TYPE_CD.DELIVERY'">
							<p class="information" ng-show="spsCoupon.dcApplyTypeCd=='DC_APPLY_TYPE_CD.AMT' && (spsCoupon.dcValueAMT==undefined || spsCoupon.dcValueAMT=='')"  ><spring:message code="common.require.data"/></p>
							</span>
							<span class="line_group"><!-- 정율 -->
								<input type="radio" value="DC_APPLY_TYPE_CD.RATE" name="dcApplyTypeCd" ng-model="spsCoupon.dcApplyTypeCd" ng-click="valueInital()"
								ng-disabled="spsCoupon.couponTypeCd =='COUPON_TYPE_CD.WRAP' || spsCoupon.couponTypeCd =='COUPON_TYPE_CD.DELIVERY'"/>
								<label><spring:message code="c.sps.coupon.pop.dc.apply.type.rate" /></label>
								<input type="text" id="inRate" style="width:111px;" ng-model="spsCoupon.dcValueRATE" number-only maxlength="3"
								ng-disabled="spsCoupon.dcApplyTypeCd == 'DC_APPLY_TYPE_CD.AMT'
								|| spsCoupon.couponTypeCd =='COUPON_TYPE_CD.WRAP' || spsCoupon.couponTypeCd =='COUPON_TYPE_CD.DELIVERY'"/>
								<span><spring:message code="c.sps.coupon.pop.dc.rate"/><!-- % 할인 --></span>
							</span>
							<span ng-hide="spsCoupon.couponTypeCd =='COUPON_TYPE_CD.WRAP' || spsCoupon.couponTypeCd =='COUPON_TYPE_CD.DELIVERY'">
							<p class="information" ng-show="spsCoupon.dcApplyTypeCd=='DC_APPLY_TYPE_CD.RATE' && (spsCoupon.dcValueRATE==undefined || spsCoupon.dcValueRATE=='')" ><spring:message code="common.require.data"/></p>
							</span>
						</td>
						<th>
							<spring:message code="c.sps.coupon.pop.burden.rate" /><!-- 비용부담 율 -->
						</th>
						<td>
							<label>업체 {{spsCoupon.businessBurdenRate}} % </label>
						</td>
					</tr>
					
<!-- 					<tr ng-if="isPo == true" > -->
<!-- 						<th> -->
<%-- 							<spring:message code="c.sps.coupon.pop.max.dc.amt" /> <!-- 최대 할인금액 --> --%>
<!-- 						</th> -->
<!-- 						<td> -->
<!-- 							<input id="maxDcAmt" type="tel" ng-model="spsCoupon.maxDcAmt" style="width:111px;" v-key="spsCoupon.maxDcAmt" -->
<!-- 							ng-disabled="spsCoupon.dcApplyTypeCd=='DC_APPLY_TYPE_CD.AMT' || spsCoupon.couponTypeCd=='COUPON_TYPE_CD.DELIVERY' || spsCoupon.couponTypeCd=='COUPON_TYPE_CD.WRAP'"/> -->
<%-- 							<span>&nbsp;<spring:message code="c.common.amt" /><!-- 원 --></span> --%>
<!-- 							<span ng-hide="spsCoupon.couponTypeCd =='COUPON_TYPE_CD.WRAP' || spsCoupon.couponTypeCd =='COUPON_TYPE_CD.DELIVERY'"> -->
<%-- 								<p class="information" ng-show="spsCoupon.dcApplyTypeCd=='DC_APPLY_TYPE_CD.RATE' && spsCoupon.maxDcAmt=='' "><spring:message code="common.require.data"/></p> --%>
<!-- 							</span> -->
<!-- 						</td> -->
<!-- 					</tr> -->
<%-- PO 설정 area END --%>

					<tr>
						<th>
							<spring:message code="c.sps.coupon.pop.max.coupon.issue" /> <!-- 최대 발급 매수 --><i/>
						</th>
					    <td>
					    	<input type="text" name="maxIssueQty" id="maxIssueQty" ng-model="spsCoupon.maxIssueQty" maxlength="14" v-key="spsCoupon.maxIssueQty"/>
<!-- 					    	<p class="information" ng-show="spsCoupon.maxIssueQty == ''"><spring:message code="common.require.data"/></p> -->
					    </td>
					    <th>
							<spring:message code="c.sps.coupon.pop.multi.coupon.issue" /> <!-- 복수 발급 매수 --><i/>
						</th>
					    <td>
					    	<radio-list ng-model="spsCoupon.memIssueBasisCd" code-group="MEM_ISSUE_BASIS_CD" ng-init="spsCoupon.memIssueBasisCd='MEM_ISSUE_BASIS_CD.PERIOD'"></radio-list>
					    	<input type="text" name="maxMemIssueQty" id="maxMemIssueQty" ng-model="spsCoupon.maxMemIssueQty" v-key="spsCoupon.maxMemIssueQty" style="width:50px;" maxlength="6" required/>
					    </td>
					</tr>
					<tr>
						<th>쿠폰 발급 현황</th>
						<td >
							<span ng-hide="spsCoupon.couponId == '' || spsCoupon.couponId == null">
							<spring:message code="c.sps.coupon.pop.total.coupon.total" /><!-- 총 발급매수 --> : {{totalCnt}} /
							<span ng-if="spsCoupon.publicCin == 'Y'">
							<spring:message code="c.sps.coupon.pop.total.coupon.issue" /><!-- 미등록 --> : {{issueCnt}} /
							<spring:message code="c.sps.coupon.pop.total.coupon.reg" /><!-- 등록 --> : {{regCnt}} /
							</span>
							<spring:message code="c.sps.coupon.pop.total.coupon.use" /><!-- 사용 --> : {{useCnt}} /
							<span ng-if="spsCoupon.publicCin == 'Y'">
							<spring:message code="c.sps.coupon.pop.total.coupon.stop" /><!-- 정지 --> : {{stopCnt}} /
							</span>
							<spring:message code="c.sps.coupon.pop.total.coupon.per" /><!-- 사용율 --> : {{useCnt / totalCnt * 100 | number:1 }} %
						</span>
						</td>
						
						<th>개당 적용 여부</th>
						<td>
							<fieldset ng-disabled="true">
								<radio-yn ng-model="spsCoupon.singleApplyYn" labels='적용,미적용' init-val="N"></radio-yn>
							</fieldset>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.sps.common.control.type"/><!-- 허용 유형 --></th>
						<td>
							<%--제외옵션 :  except="mgrade,mtype,device,channel"  --%>
							<label>전체</label>
						</td>
						<th rowspan="2">
						<spring:message code="c.sps.common.deal.apply" /><!-- 딜 적용 여부 --><i/>
						</th>
						<td rowspan="2" ng-init="spsCoupon.dealApplyYn='N'">
							<fieldset ng-disabled="spsCoupon.couponTypeCd != 'COUPON_TYPE_CD.PRODUCT' && spsCoupon.couponTypeCd != 'COUPON_TYPE_CD.PLUS'">
								<span>
									<label for="radio3"><spring:message code="c.input.rado.applyN" /></label>
								</span>
							</fieldset>
						</td>
					</tr>
					<tr>
						<th>수수료10%미만적용여부 <!-- 수수료10%미만 사용여부 --><i/></th>
						<td >
							<fieldset ng-disabled="spsCoupon.couponTypeCd != 'COUPON_TYPE_CD.PRODUCT' && spsCoupon.couponTypeCd != 'COUPON_TYPE_CD.PLUS'">
								<span>
									<span ng-init="spsCoupon.feeLimitApplyYn='Y'"></span>
									<label for="">적용</label>
								</span>
							</fieldset>
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.sps.coupon.pop.valid.term"/> <!-- 유효 기간 --><i/>
						</th>
						<td colspan="3" ng-init="spsCoupon.termTypeCd='TERM_TYPE_CD.DAYS'">
							<input type="radio" value="TERM_TYPE_CD.DAYS" ng-model="spsCoupon.termTypeCd" />
							<span>
								<spring:message code="c.sps.coupon.pop.term.type.days"/> <!-- 발급일로부터 -->
							</span>
							<input type="text" style="width:50px;" ng-model="spsCoupon.termDays" v-key="spsCoupon.termDays"
							ng-disabled="spsCoupon.termTypeCd!='TERM_TYPE_CD.DAYS'" maxlength="5"/>
							<span>
								<spring:message code="c.sps.coupon.pop.term.type.term"/> <!-- 일 까지 -->
							</span>
							
							<input type="radio" value="TERM_TYPE_CD.LASTDAY" ng-model="spsCoupon.termTypeCd" />
							<span>
								<spring:message code="c.sps.coupon.pop.term.type.lastday"/> <!-- 발급 당월 말일 -->
							</span>
							
							<input type="radio" value="TERM_TYPE_CD.WEEK" ng-model="spsCoupon.termTypeCd" />
							<span>
								<spring:message code="c.sps.coupon.pop.term.type.week"/> <!-- 발급 당주 -->
							</span>
							
							<input type="radio" value="TERM_TYPE_CD.TERM" style="margin-left:28px;" ng-model="spsCoupon.termTypeCd" />
							<span>
								<spring:message code="c.sps.coupon.pop.term.type.term.enter"/> <!-- 기간 입력 -->
							</span>
							<span ng-show="spsCoupon.termTypeCd=='TERM_TYPE_CD.TERM'"> 
								<input type="text" style="width:160px;" ng-model="spsCoupon.termStartDt" datetime-picker period-start/>
								~
								<input type="text" style="width:160px;" ng-model="spsCoupon.termEndDt" datetime-picker period-end/>
							</span>
 							<span ng-show="spsCoupon.termTypeCd=='TERM_TYPE_CD.DAYS'">
 								<span ng-show="spsCoupon.termDays==''">
	 								<span ng-hide="spsCoupon.termDays == 0">
	 									<p class="information" "><spring:message code="common.require.data"/></p>
	 								</span>
 								</span>
 							</span>
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.sps.coupon.pop.target.type.cd"/> <!-- 쿠폰사용 대상 --><i/>
						</th>
						<td colspan="3" >
							<span ng-init="spsCoupon.ccsApply.targetTypeCd = 'TARGET_TYPE_CD.PRODUCT'">
								<spring:message code="c.sps.common.pop.target.type.cd.product"/><!-- 적용 상품 -->
							</span>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
		
		<form name="gridForm" >
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
							<div class="btn_alignR marginT1" >
								<span ng-hide="spsCoupon.couponStateCd != 'COUPON_STATE_CD.READY' && changeFlag == false">
									<button type="button" name="targetSave" class="btn_type2" ng-click="ctrl.addExcel('Pr')">
										<b ><spring:message code="c.excel.file.upload" /></b>
									</button>
								</span>
								<span ng-hide="spsCoupon.couponStateCd != 'COUPON_STATE_CD.READY' && changeFlag == false">
									<button type="button" name="targetSave" class="btn_type2" ng-click="ctrl.productPopup()">
										<b><spring:message code="c.sps.present.add.product" /></b>
									</button>
								</span>
								<button type="button" class="btn_type2" ng-click="ctrl.deleteGridData('applyTargetPrGrid')">
									<b><spring:message code="common.btn.del" /></b>
								</button>					
							</div>
							<div class="box_type1 marginT1" >
								<h3 class="sub_title2">
									<spring:message code="c.sps.coupon.apply.list2" />
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
						
					<%--1. 적용대상 그리드 END --%>
					<%--2. 제외대상. 적용대상이 상품이 아닐때 --%>
						<tr ng-show="div_ex">
							<th>
								<spring:message code="c.sps.common.pop.excproduct"/> <!-- 제외 상품 -->
							</th>
							<td>
							<div class="btn_alignR marginT1" ng-hide="spsCoupon.couponStateCd == 'COUPON_STATE_CD.STOP'">
								<button type="button"  ng-click="ctrl.addExcel('ex')" class="btn_type2">
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
									<spring:message code="c.sps.coupon.pop.excproductlist" />
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
				</tbody>
			</table>
		</form>
	</div>

	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.closePopup()">
			<b><spring:message code="c.common.close"/></b>
		</button>
		<span ng-show="spsCoupon.couponStateCd == 'COUPON_STATE_CD.RUN' ">
			<button name="mainStop" type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.couponStop()">
				<b><spring:message code="c.sps.common.pop.state.stop"/></b>
			</button>
		</span>
		<span ng-show="spsCoupon.couponId!=undefined && spsCoupon.couponId != '' && spsCoupon.couponStateCd == 'COUPON_STATE_CD.READY' ">
			<button name="mainRun" type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.couponRun()">
				<b><spring:message code="c.sps.common.pop.state.btn.run"/></b>
			</button>
		</span>
		<span ng-show="spsCoupon.couponStateCd != 'COUPON_STATE_CD.STOP' ">
			<button name="mainSave" type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.updateCouponPromotion()">
				<b><spring:message code="c.common.save"/></b>
			</button>
		</span>
	</div>

</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>