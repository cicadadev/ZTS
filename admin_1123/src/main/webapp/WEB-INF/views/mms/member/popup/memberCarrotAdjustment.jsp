<%--
	화면명 : 회원 관리 > 당근내역 > 당근 조정 팝업 
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/mms.app.member.manager.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="wrap_popup" ng-app="memberManagerApp" ng-controller="mms_carrotAdjustmentPopApp_controller as ctrl">
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
					<th>조정당근</th>
					<td>
						<select-list ng-model="mmsCarrot.carrotTypeCd" code-group="CARROT_TYPE_CD" ng-init="mmsCarrot.carrotTypeCd =''"></select-list>
						<input type="number" ng-model="mmsCarrot.carrot" style="width:22.5%" ng-init="mmsCarrot.carrot=''"/>
						<p class="information" ng-show="mmsCarrot.carrot == '' || mmsCarrot.carrotTypeCd == ''">필수 입력 항목 입니다.</p>
					</td>
				</tr>				
				<tr>
					<th><spring:message code="c.sps.carrot.note" /> <!-- 사유 --></th>
					<td>
						<input type="text" value="" ng-model="mmsCarrot.note" style="width:50%;"/>
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
		<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.saveCarrot()">
			<b><spring:message code="c.common.save"/><!-- 저장 --></b>
		</button>
	</div>

</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>
		