<%--
	화면명 : 상품 관리 > 표준카테고리 관리 > 표준카테고리 등록 팝업
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.category.manager.js"></script>

<div ng-app="categoryApp" ng-controller="pms_categoryInsertPopApp_controller as ctrl" ng-init="ctrl.initInsertPopup()" id="categoryInsertPopup" class="wrap_popup">
	<h1 class="sub_title1"><spring:message code="pms.category.register" /></h1>
	
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
							<spring:message code="c.pmsCategory.upperCategoryId" />
						</th>
						<td ng-bind="popupCategoryInfo.upperCategoryId"></td>
						<th>
							<spring:message code="c.pms.category.upper.name" />
						</th>
						<td>
							<input type="text" value="" placeholder="" style="width:50%;" 
								ng-model="popupCategoryInfo.upperCategoryName" readonly />
						</td>
					</tr>					
					<tr>
						<th>
							<spring:message code="c.pmsCategory.categoryId" />
						</th>
						<td ng-bind="popupCategoryInfo.categoryId"></td>
						<th>
							<spring:message code="pmsCategory.name" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td>
							<input type="text" value="" placeholder="" style="width:50%;" 
								ng-model="popupCategoryInfo.name" required />
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="pmsCategory.useYn" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td>
							<input type="radio" ng-model="popupCategoryInfo.useYn" value="Y" /><label for="radio1"><spring:message code="c.input.radio.useY" /></label>
							<input type="radio" ng-model="popupCategoryInfo.useYn" value="N" /><label for="radio2"><spring:message code="c.input.radio.useN" /></label>
						</td>
						<th>
							<spring:message code="c.pmsCategory.sortNo" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td colspan="3">
							<input type="text" value="" placeholder="" style="width:20%;" 
								ng-model="popupCategoryInfo.sortNo" required />
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="pmsCategory.leafYn" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td>
							<input type="radio" ng-model="popupCategoryInfo.leafYn" value="Y" /><label for="radio3">Yes</label>
							<input type="radio" ng-model="popupCategoryInfo.leafYn" value="N" /><label for="radio4">No</label>
						</td>
						<th>
							<spring:message code="pmsCategory.erpProductId" />
						</th>
						<td>
							<input type="text" value="" placeholder="" style="width:50%;" ng-model="categoryInfo.erpProductId" />							
						</td>											
					</tr>
					<tr ng-if="popupCategoryInfo.leafYn == 'Y'">
						<th>
							<spring:message code="c.pmsCategory.md.idnname" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td>
							<input type="text" placeholder="" style="width:30%;" ng-model="popupCategoryInfo.userId" readonly disabled required />
							<input type="text" placeholder="" style="width:30%;" ng-model="popupCategoryInfo.mdName" readonly disabled required />
							<button type="button" class="btn_type2" ng-click="ctrl.searchMD()">
								<b>검색</b>
							</button>
							<button type="button" class="btn_eraser {{popupCategoryInfo.userId == null || popupCategoryInfo.userId == ''?'btn_eraser_disabled':''}}"  ng-click="ctrl.eraser()"><spring:message code="c.search.btn.eraser"/></button>
						</td>
						<th>
							<spring:message code="pmsCategory.secondApprovalYn" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td>
							<input type="radio" ng-model="popupCategoryInfo.secondApprovalYn" value="Y" /><label for="radio5"><spring:message code="c.input.radio.useY" /></label>
							<input type="radio" ng-model="popupCategoryInfo.secondApprovalYn" value="N" /><label for="radio6"><spring:message code="c.input.radio.useN" /></label>
						</td>
					</tr>
					<tr ng-if="popupCategoryInfo.leafYn == 'Y'">
						<th>
							<spring:message code="pmsProducthistory.pointSaveRate" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td>
							<input type="text" value="" placeholder="" style="width:40%;" ng-model="popupCategoryInfo.pointSaveRate" required />%
						</td>
						<th>
							<spring:message code="pmsCategory.newIconYn" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td>
							<input type="radio" ng-model="popupCategoryInfo.newIconYn" value="Y" /><label for="radio7"><spring:message code="c.input.radio.useY" /></label>
							<input type="radio" ng-model="popupCategoryInfo.newIconYn" value="N" /><label for="radio8"><spring:message code="c.input.radio.useN" /></label>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
	
	<div ng-if="popupCategoryInfo.leafYn == 'Y'">
		<table class="tb_type1">
			<colgroup>
				<col width="13%">
				<col width="*">
			</colgroup>
			<tbody>
				<tr>
					<th>
						<spring:message code="pmsCategoryrating.name" />
					</th>
					<td style="text-align:right">
						<button type="button" class="btn_type2" ng-click="ctrl.saveCategoryRating()"><b><spring:message code="c.pmsCategoryRating.rating.register" /></b></button>
						<div class="gridbox gridbox200">
			   				<div class="grid" ui-grid="grid_categoryRating"   
								ui-grid-move-columns 
								ui-grid-resize-columns
								ui-grid-auto-resize 
								ui-grid-selection 
								ui-grid-row-edit
								ui-grid-cell-nav
								ui-grid-exporter
								ui-grid-edit 
								ui-grid-validate></div>
						</div>
					</td>							
				</tr>										
			</tbody>
		</table>	
	</div>	

	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /></b>
		</button>
		<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.insertCategory()">
			<b><spring:message code="c.common.save" /></b>
		</button>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>