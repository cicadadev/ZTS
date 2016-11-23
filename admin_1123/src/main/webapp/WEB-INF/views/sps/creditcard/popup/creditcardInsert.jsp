<%--
	화면명 : 카드사 할인정보 관리 > 카드사할인 상세& 등록
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/sps.app.creditcard.list.js"></script>

<div class="wrap_popup" ng-app="creditcardApp" ng-controller="sps_creditcardDetailApp_controller as ctrl" ng-init="ctrl.detail()">
	
	<form name="form2">
		<h2 class="sub_title1"><spring:message code="sps.cardpromotion.title.detail" /></h2>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="15%" />
					<col width="40%" />
					<col width="15%" />
					<col width="40%" />
				</colgroup>
				<tbody>
					<tr>
						<th><spring:message code="c.sps.cardpromotion.no" /></th>
						<td> 
							<div ng-if="type=='D'">{{ spsCardpromotion.cardPromotionNo }}</div>
						</td>
						<th><spring:message code="c.sps.cardpromotion.name" /> <i><spring:message code="c.input.required" /></i></th>
						<td>
							<input type="text" value="" placeholder="" style="width:50%;" ng-model="spsCardpromotion.name" v-key="spsCardpromotion.name" required/>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.sps.cardpromotion.period" /> <i><spring:message code="c.input.required" /></i></th>
						<td>
							<input type="text" ng-model="spsCardpromotion.startDt" placeholder="" datetime-picker period-start required/>
							~
							<input type="text" ng-model="spsCardpromotion.endDt" placeholder="" datetime-picker period-end required/>
							<p class="information" ng-show="!spsCardpromotion.startDt || !spsCardpromotion.endDt">필수 입력 항목 입니다.</p>
						</td>
						<th><spring:message code="c.sps.cardpromotion.state" /> <i><spring:message code="c.input.required" /></i></th>
						<td>
							<div>{{ spsCardpromotion.cardPromotionStateName }}</div>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.sps.cardpromotion.html1" /> <i><spring:message code="c.input.required" /></i></th>
						<td colspan="4"> 
							<textarea ckeditor="ckPcOption" ng-model="spsCardpromotion.html1" v-key="spsCardpromotion.html1" required></textarea>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.sps.cardpromotion.html2" /> <i><spring:message code="c.input.required" /></i></th>
						<td colspan="4"> 
							<textarea ckeditor="ckMobileOption" ng-model="spsCardpromotion.html2" v-key="spsCardpromotion.html2" required></textarea>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b><spring:message code="c.common.close" /></b>
			</button>
			<button ng-if="type=='D' && spsCardpromotion.cardPromotionStateCd=='CARD_PROMOTION_STATE_CD.READY'" type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.updatePromotionState('run')">
				<b><spring:message code="c.sps.event.btn.run" /></b>
			</button>
			<button ng-if="type=='D' && spsCardpromotion.cardPromotionStateCd=='CARD_PROMOTION_STATE_CD.RUN'" type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.updatePromotionState('stop')">
				<b><spring:message code="c.sps.event.btn.stop" /></b>
			</button>
			<button ng-if="spsCardpromotion.cardPromotionStateCd!='CARD_PROMOTION_STATE_CD.STOP'" type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.save()">
				<b><spring:message code="c.common.save" /></b>
			</button>
		</div>
	</form>

</div>				
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>

