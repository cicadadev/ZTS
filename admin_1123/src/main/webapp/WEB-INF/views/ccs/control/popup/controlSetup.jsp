<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>

<div class="wrap_popup" ng-app="ccsAppPopup" ng-controller="ccs_restrictPopApp_controller as ctrl">
	<h1 class="sub_title1"><spring:message code="c.ccs.control.title" /></h1>

<form id="frmRestrict" >
	<fieldset ng-disabled="isRun">
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="20%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr ng-show="trShowYn.type"><%--허용 회원 유형 --%>
						<th><spring:message code="c.ccs.control.type" /> <i>필수입력</i></th>
						<td>
							<checkbox-list ng-model="ccsControl.memberTypes" code-group="MEMBER_TYPE_CD" all-check ></checkbox-list>
						</td>
					</tr>
					<tr ng-show="trShowYn.grade"><%--허용 회원 등급 --%>
						<th><spring:message code="c.ccs.control.grade" /> <i>필수입력</i></th>
						<td>
							<!-- <checkbox-list ng-model="ccsControl.memGrades" code-group="MEM_GRADE_CD" ></checkbox-list> -->
							<checkbox-list ng-model="ccsControl.memGrades" custom="MEM_GRADE" all-check />
						</td>
					</tr>
					<tr ng-show="trShowYn.channel"><%--허용 유입경로 --%>
						<th><spring:message code="c.ccs.control.channel" /> <i>필수입력</i></th>
						<td>
							<radio-list ng-model="ccsControl.channelControlCd" code-group="CHANNEL_CONTROL_CD" ></radio-list>
	
							<select style="min-width:100px;" ng-model="channelId" class="ad_select"
									ng-disabled="ccsControl.channelControlCd != 'CHANNEL_CONTROL_CD.CHANNEL'"
									ng-options="channel.channelId as channel.name for (idx, channel) in channelList track by channel.channelId">
									<option value="">-- 광고 채널 선택 --</option>
							</select>
							<span class="sp_position">
								<button type="button" class="btn_type2" ng-click="ctrl.selectChannel()">
									<b><spring:message code="c.common.add" /></b>
								</button>
							</span>
							<div class="ad_box" id="adBox">
							</div>
						</td>
					</tr>
					<tr ng-show="trShowYn.device"><%--허용 디바이스 --%>
						<th><spring:message code="c.ccs.control.device" /> <i>필수입력</i></th>
						<td>
							<checkbox-list ng-model="ccsControl.deviceTypes" code-group="DEVICE_TYPE_CD" ></checkbox-list>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</fieldset>
</form>

	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /></b>
		</button>
		<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.saveRestrict()" >
			<b><spring:message code="c.common.save" /></b>
		</button>
	</div>
</div>

<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>