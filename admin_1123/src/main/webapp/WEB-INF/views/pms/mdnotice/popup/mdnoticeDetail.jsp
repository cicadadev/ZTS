<%--
	화면명 : 상품관리 > MD상품 공지관리 > MD공지 등록 팝업
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true" />
<script type="text/javascript" src="/resources/js/app/pms.app.mdnotice.list.js"></script>

<div class="wrap_popup" ng-app="mdnoticeManagerApp" ng-controller="pms_mdnoticeDetailPopApp_controller as Ctrl" ng-init="Ctrl.detail()">
	<form name="form2">
		<h2 class="sub_title1"><spring:message code="c.pms.mdnotice.MdnoticeDetail" /></h2>
		<div class="box_type1">
			<!-- <h3 class="sub_title2">공지사항 {{flagTxt}}(공지번호 {{ccsNotice.noticeNo}}번)</h3> -->

			<table class="tb_type1" style="table-layout:fixed;">
				<colgroup>
					<col width="15%" />
					<col width="35%" />
					<col width="15%" />
					<col width="35%" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							<spring:message code="ccsNotice.noticeNo" />
						</th>
						<td>{{ ccsNotice.noticeNo }}</td>
						<th>전시여부<i><spring:message code="c.input.required" /></i></th>
						<td>
							<span ng-repeat="display in displayYns" >
								<input type="radio" id="displayYn{{use.val}}" ng-model="ccsNotice.displayYn" ng-init="ccsNotice.displayYn = 'Y'" ng-value="display.val" style="cursor: pointer;" />
								<label for="displayYn{{display.val}}" style="cursor: pointer;">{{display.text}}&nbsp;</label>
							</span>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.pms.mdnotice.period2" /><i><spring:message code="c.input.required" /></i></th>
						<td colspan="3">
							<input type="text" ng-model="ccsNotice.startDt" datetime-picker style="width:200px"  period-start/>
							~ 
							<input type="text" ng-model="ccsNotice.endDt" datetime-picker style="width:200px"  period-end/>
							<p class="information" ng-show="!ccsNotice.startDt || !ccsNotice.endDt">필수 입력 항목 입니다.</p>
						</td>
					</tr>
					<tr>
						<th><spring:message code="ccsNotice.title" /><i><spring:message code="c.input.required" /></i></th>
						<td colspan="3"><input type="text" name="title" ng-model="ccsNotice.title" style="width: 100%;" v-key="ccsNotice.title" required/></td>
					</tr>
					<tr>
						<th>내용<i><spring:message code="c.input.required" /></i></th>
						<td colspan="3"><textarea cols="50" rows="20" name="detail" v-key="ccsNotice.detail" ckeditor="ckOption" ng-model="ccsNotice.detail" validation></textarea></td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.pms.product.list" />
						</th>
						<td colspan="3">
							<button type="button" class="btn_type2" ng-click="Ctrl.regProductPopup()"><b><spring:message code="c.spsDealproduct.regProduct" /></b></button>
							<button type="button" class="btn_type2" ng-click="Ctrl.deleteMdnoticeGrpGrid()"><b><spring:message code="c.common.delete" /></b></button>
							<div class="gridbox">
							<div class="grid" data-ui-grid="myProductGrid" 
								data-ui-grid-move-columns 
								data-ui-grid-resize-columns 
								data-ui-grid-pagination
								data-ui-grid-auto-resize 
								data-ui-grid-selection 
								data-ui-grid-row-edit
								data-ui-grid-cell-nav
								data-ui-grid-exporter
								data-ui-grid-edit 
								data-ui-grid-validate></div>								
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="Ctrl.close()">
				<b><spring:message code="c.common.close" /></b>
			</button>
			<button ng-show="deleteBtn" type="button" class="btn_type3" data-ng-click="Ctrl.deleteMdnotice()">
				<b><spring:message code="c.common.delete" /></b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="Ctrl.update()" >
				<b><spring:message code="c.common.save" /></b>
			</button>
		</div>
	</form>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true" />