<%--
	화면명 : 프로모션 관리 > 당근조정
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/sps.app.carrot.manager.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="wrap_popup" ng-app="carrotManagerApp" ng-controller="sps_carrotManagerDetailPopApp_controller as ctrl">
	<h1 class="sub_title1"><spring:message code="c.sps.carrot.adjust" /> <!-- 당근 조정--></h1>
	<form name="form">
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col class="col_142" />
				<col class="col_auto" />
			</colgroup>
			<tbody>
				<tr>
					<th>
						<spring:message code="c.sps.common.tag.member.info"/><!-- 회원ID / 명 -->
					</th>
					<td>
						<input type="text" value="" ng-model="memberId" style="width:15%;" readonly="readonly" ng-init="userid=''" required/>
						<input type="text" value="" ng-model="memberName" style="width:15%;" readonly="readonly"/>
						<button type="button" class="btn_type2" ng-click="ctrl.memberPopup()">
							<b><spring:message code="c.search.btn.search" /></b>
						</button>
						<button type="button" class="btn_eraser"  ng-class="{'btn_eraser_disabled' : memberId == null || memberId == ''}" 
						ng-click="ctrl.eraser('memberId', 'memberName')"><spring:message code="c.search.btn.eraser"/></button>
						<p class="information" ng-show="memberId == undefined || memberId==''"><spring:message code="common.require.data"/></p>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<br/><br/>
	
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col class="col_142" />
				<col class="col_auto" />
			</colgroup>
			<tbody>
				<tr>
					<th colspan="2"><b><spring:message code="c.sps.carrot.adjust" /> <!-- 당근 조정--></b></th>
				</tr>
				<tr>
					<th><spring:message code="c.sps.carrot.adjust2" /><!-- 조정당근 --></th>
					<td>
						<select ng-model="spsCarrot.infoType" ng-init="spsCarrot.infoType = 'EVENT'">
							<option ng-repeat="info in infoType" value="{{info.val}}" >{{info.text}}</option>
						</select>
						<input type="text" ng-model="spsCarrot.carrot" style="width:22.5%" ng-init="spsCarrot.carrot = ''" v-key="mmsCarrot.carrot" required/>
<%-- 						<p class="information" ng-show="spsCarrot.carrot == ''"><spring:message code="common.require.data"/></p> --%>
					</td>
				</tr>				
				<tr>
					<th><spring:message code="c.sps.carrot.note" /> <!-- 사유 --></th>
					<td>
						<input type="text" value="" ng-model="spsCarrot.note" style="width:50%;" v-key="mmsCarrot.note" required/>
					</td>
				</tr>				
			</tbody>
		</table>
	</div>

	</form>
	
	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
			<b><spring:message code="c.common.close"/><!-- 닫기 --></b>
		</button>
		<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.adjustCarrot()">
			<b><spring:message code="c.common.save"/><!-- 저장 --></b>
		</button>
	</div>

</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>
		