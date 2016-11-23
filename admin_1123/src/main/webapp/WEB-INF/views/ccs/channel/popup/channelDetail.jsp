<%--
	화면명 : 채널관리 > 채널상세 팝업
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.channel.manager.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="wrap_popup" ng-app="channelApp" data-ng-controller="ccs_channelDetailPopApp_controller as ctrl" data-ng-init="init()">
		<h2 class="sub_title1"><spring:message code="c.ccsChannel.channelDetail" /><!-- 채널 상세 --></h2>
		<div class="box_type1">
			
			<form name="channelForm"">
			<table class="tb_type1">
				<colgroup>
					<col width="16%" />
					<col width="32%" />
					<col width="16%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							<spring:message code="c.ccsChannel.channelId" /><!-- 채널 번호 -->
						</th>
						<td>{{channelDetail.channelId}}</td>
						<th>
							<spring:message code="c.ccsChannel.channelName" /><!-- 채널 명 -->
						</th>
						<td>
							<input type="text" v-key="ccsChannel.name" data-ng-model="channelDetail.name" name="name" style="width:98%;" required/>
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.ccsChannel.state" /><!-- 상태 -->
						</th>
						<td ng-if="channelDetail.channelStateCd != '' && channelDetail.channelStateCd != undefined">
							{{channelDetail.channelStateName}}
						</td>
						<td ng-if="channelDetail.channelStateCd == '' || channelDetail.channelStateCd == undefined">
							대기
						</td>
						<th>
							<spring:message code="c.ccsChannel.channelNote" /><!-- 채널 설명 -->
						</th>
						<td>
							<input type="text" data-ng-model="channelDetail.note" value="" style="width:98%;" />
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.ccsChannel.choice.business" /><!-- 제휴 업체 선택 -->
						</th>
						<td>
							<select data-ng-model="channelDetail.businessId" style="min-width:100px;" v-key="required">
								<option value="">선택하세요</option>
								<option ng-repeat="business in businessList" title="{{business.name}}" ng-selected="{{business.id == channelDetail.businessId}}" value="{{business.id}}">{{business.name}}</option>
							</select>
							
						</td>
						<th rowspan="2">
							<spring:message code="c.ccsChannel.channelType" /><!-- 채널 유형 -->
						</th>
							<td rowspan="2" ng-init="channelDetail.channelTypeCd = 'CHANNEL_TYPE_CD.AD'">
								<radio-list ng-model="channelDetail.channelTypeCd" code-group="CHANNEL_TYPE_CD" />
							</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.ccsChannel.gateUtl" /><!-- GATE URL -->
						</th>
						<td ng-if="channelDetail.gateUrl != '' && channelDetail.gateUrl != undefined">
							{{channelDetail.gateUrl}}{{channelDetail.channelId}}
						</td>
						<td ng-if="channelDetail.gateUrl == '' || channelDetail.gateUrl == undefined">
							
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.ccsChannel.pcReturnUrl" /><!-- PC RETURN URL -->
						</th>
						<td><input type="text" v-key="ccsChannel.pcUrl" url-input data-ng-model="channelDetail.pcUrl" value="" style="width:98%;" required/></td>
						<th>
							<spring:message code="c.ccsChannel.mbReturnUrl" /><!-- MOBILE RETURN URL -->
						</th>
						<td>
							<input type="text" v-key="ccsChannel.mobileUrl" url-input data-ng-model="channelDetail.mobileUrl" value="" style="width:98%;" required/>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>

<div class="btn_alignC marginT3">
	<button type="button" class="btn_type3 btn_type3_gray" data-ng-click="ctrl.close()">
		<b><spring:message code="c.common.close" /><!-- 취소 --></b>
	</button>
	<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.changeState('stop')" ng-show="channelDetail.channelStateCd == 'CHANNEL_STATE_CD.RUN'">
		<b><spring:message code="c.ccsChannel.state.stop" /><!-- 중지 --></b>
	</button>
	<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.changeState('run')" ng-show="channelDetail.channelStateCd == 'CHANNEL_STATE_CD.READY'">
		<b><spring:message code="c.ccsChannel.state.run" /><!-- 진행 --></b>
	</button>
	<button type="submit" class="btn_type3 btn_type3_purple" data-ng-click="ctrl.updateChannel()" ng-show="channelDetail.channelStateCd != 'CHANNEL_STATE_CD.STOP'">
		<b><spring:message code="c.common.save" /><!-- 저장 --></b>
	</button>
	
			
</div>
</div>

<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>