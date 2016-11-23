<%--
	화면명 : 블랙리스트 관리 > 블랙리스트 상세 팝업
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/mms.app.blacklist.manager.js"></script>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%> 

<div class="wrap_popup"  ng-app="blacklistManagerApp" data-ng-controller="mms_blacklistDetailPopApp_controller as ctrl">
		<h1 class="sub_title1">블랙리스트 상세<!-- 블랙리스트 등록 --></h1>
		
		<div class="box_type1">
		<form name="blacklistInsertForm">
			<table class="tb_type1">
				<colgroup>
					<col width="16%" />
					<col width="45%" />
					<col width="16%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							블랙리스트 번호<!-- 블랙리스트 번호 -->
						</th>
						<td>
							{{mmsBlacklist.blacklistNo}}
						</td>
						<th>
							블랙리스트 상태<!-- 블랙리스트 상태 -->
						</th>
						<td>
							<radio-list ng-model="mmsBlacklist.blacklistStateCd" code-group="BLACKLIST_STATE_CD" />
						</td>
					</tr>
					<tr>
						<th>
							회원ID / 명<!-- 회원ID / 명 --> <i><spring:message code="c.input.required"/><!-- 필수입력 --></i>
						</th>
						<td>
							<input data-ng-model="mmsBlacklist.userid" id="userid" type="text" name="userid" placeholder="" style="width:20%;" readonly disabled required/>
							<input data-ng-model="mmsBlacklist.name" id="name" type="text" name="name" placeholder="" style="width:20%;" readonly disabled />
						</td>
						<th>
							회원상태<!-- 회원상태 -->
						</th>
						<td>
							{{mmsBlacklist.status}}
						</td>
						
					</tr>
					<tr>
						<th>
							블랙리스트 유형<!-- 블랙리스트 유형 --> <i><spring:message code="c.input.required"/><!-- 필수입력 --></i>
						</th>
						<td colspan="3">
							<select ng-model="mmsBlacklist.blacklistTypeCd" select-code="BLACKLIST_TYPE_CD" style="width:150px;"  v-key="required">
								<option value="">선택하세요</option>
							</select>
						</td>
					</tr>
					<tr>
						<th>
							블랙리스트 기간<!-- 블랙리스트 기간 --> <i><spring:message code="c.input.required"/><!-- 필수입력 --></i>
						</th>
						<td colspan="3">
							<input ng-model="mmsBlacklist.startDt" id="startDt" type="text" value="" placeholder="" datetime-picker period-start/>
							~
							<input ng-model="mmsBlacklist.endDt" id="endDt" type="text" value="" placeholder="" datetime-picker period-end/>
							<p class="information" ng-show="!mmsBlacklist.startDt || !mmsBlacklist.endDt"><spring:message code="c.common.invalid.content"/> <!-- 필수 입력 항목 입니다. --></p>
						</td>
					</tr>
					<tr>
						<th>
							사유<!-- 사유 --> <i><spring:message code="c.input.required"/><!-- 필수입력 --></i>
						</th>
						<td colspan="3">
							<textarea cols="10" rows="5" placeholder="" ng-model="mmsBlacklist.blacklistReason"></textarea>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
	
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_white" ng-click="ctrl.close()">
				<b><spring:message code="c.common.close" /><!-- 닫기 --></b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.saveBlacklist()">
				<b><spring:message code="c.common.save" /><!-- 저장 --></b>
			</button>
		</div>
</div>

<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>