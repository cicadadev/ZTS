<%--
	화면명 : 전시 관리 > 전시카테고리 관리 > 전시카테고리 등록 팝업
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/dms.app.display.category.manager.js"></script>

<div ng-app="displayCategoryApp" ng-controller="dms_displayCategoryinsertPopApp_controller as ctrl" ng-init="ctrl.initInsertPopup()" id="displayCategoryInsertPopup" class="wrap_popup">
	<h1 class="sub_title1"><spring:message code="dms.displaycategory.register" /></h1>
	
	
		<div class="box_type1">
		<form name="form">
			<table class="tb_type1">
				<colgroup>
					<col width="13%" />
					<col width="37%" />
					<col width="13%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							<spring:message code="c.dmsDisplaycategory.upperCategoryId" />
						</th>
						<td ng-bind="popupDisplayCategoryInfo.upperDisplayCategoryId"></td>
						<th>
							<spring:message code="c.dmsDisplaycategory.upperCategory.name" />
						</th>
						<td>
							<input type="text" value="" placeholder="" style="width:98%;" 
								ng-model="popupDisplayCategoryInfo.upperDisplayCategoryName" readonly />
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.dmsDisplaycategory.categoryId" />
						</th>
						<td ng-bind="popupDisplayCategoryInfo.displayCategoryId"></td>
						<th>
							<spring:message code="dmsDisplaycategory.name" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td>
							<input type="text" value="" placeholder="" style="width:98%;" 
								ng-model="popupDisplayCategoryInfo.name" required />
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="dmsDisplaycategory.displayYn" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td>
							<radio-yn ng-model="popupDisplayCategoryInfo.displayYn" labels='<spring:message code="c.input.radio.displayY" />,<spring:message code="c.input.radio.displayN" />' init-val="Y"></radio-yn>
						</td>
						<th>
							<spring:message code="c.dmsDisplaycategory.sortNo" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td>
							<input type="text" value="" placeholder="" style="width:98%;" 
								ng-model="popupDisplayCategoryInfo.sortNo" required />
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="dmsDisplaycategory.leafYn" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td colspan="3">
							<radio-yn ng-model="popupDisplayCategoryInfo.leafYn" labels='Yes,No' init-val="N"/>
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.dmsDisplaycategory.template" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td colspan="3">
							<select style="min-width:260px;" ng-model="popupDisplayCategoryInfo.templateId" 
								ng-options="template.templateId as template.name for template in templateList">
								<option value=""><spring:message code="common.search.commbo.default" /></option>
							</select>
						</td>
								
					</tr>
				</tbody>
			</table>
			</form>
		</div>
	
	
	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
			<b>닫기</b>
		</button>
		<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.insertDisplayCategory()">
			<b>저장</b>
		</button>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>