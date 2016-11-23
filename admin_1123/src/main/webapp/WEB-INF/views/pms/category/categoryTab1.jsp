<%--
	화면명 : 상품 관리 > 표준카테고리 관리 > 표준카테고리 기본정보 탭
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
	
<div class="box_type1">
	<h3 class="sub_title2">
		<spring:message code="pms.category.info.management" />
	</h3>
	<table class="tb_type1">
		<colgroup>
			<col width="18%" />
			<col width="32%" />
			<col width="18%" />
			<col width="*" />
		</colgroup>
		<tbody>
			<tr>
				<th>
					<spring:message code="pmsCategory.categoryId" /> <i><spring:message code="c.input.required" /></i>
				</th>
				<td ng-bind="pmsCategory.categoryId"></td>
				<th>
					<spring:message code="pmsCategory.name" /> <i><spring:message code="c.input.required" /></i>
				</th>
				<td>
					<input type="text" value="" placeholder="" style="width:60%;" ng-model="pmsCategory.name" required />
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="pmsCategory.useYn" /> <i><spring:message code="c.input.required" /></i>
				</th>
				<td>
					<input type="radio" ng-model="pmsCategory.useYn" value="Y" /><label for="radio1"><spring:message code="c.input.radio.useY" /></label>
					<input type="radio" ng-model="pmsCategory.useYn" value="N" /><label for="radio2"><spring:message code="c.input.radio.useN" /></label>
				</td>
				<th>
					<spring:message code="c.pmsCategory.sortNo" /> <i><spring:message code="c.input.required" /></i>
				</th>
				<td colspan="3">
					<input type="text" value="" placeholder="" style="width:20%;" ng-model="pmsCategory.sortNo" required />
				</td>					
			</tr>
			<tr>
				<th>
					<spring:message code="pmsCategory.leafYn" /> <i><spring:message code="c.input.required" /></i>
				</th>
				<td>
					<input type="radio" ng-model="pmsCategory.leafYn" value="Y" /><label for="radio3">Yes</label>
					<input type="radio" ng-model="pmsCategory.leafYn" value="N" /><label for="radio4">No</label>
				</td>
				<th>
					<spring:message code="pmsCategory.erpProductId" />
				</th>
				<td colspan="3">
					<input type="text" value="" placeholder="" style="width:50%;" ng-model="pmsCategory.erpProductId" />							
				</td>
			</tr>
			<tr ng-if="pmsCategory.leafYn == 'Y'">
				<th>
					<spring:message code="c.pmsCategory.md.idnname" /> <i><spring:message code="c.input.required" /></i>
				</th>
				<td>
					<input type="text" placeholder="" style="width:30%;" ng-model="pmsCategory.userId" readonly disabled required />
					<input type="text" placeholder="" style="width:30%;" ng-model="pmsCategory.mdName" readonly disabled required />
					<button type="button" class="btn_type2" ng-click="searchMD()">
						<b>검색</b>
					</button>
					<button type="button" class="btn_eraser {{pmsCategory.userId == null || pmsCategory.userId == ''?'btn_eraser_disabled':''}}" ng-click="eraser('displayCategoryId')"><spring:message code="c.search.btn.eraser"/></button>
				</td>
				<th>
					<spring:message code="pmsCategory.secondApprovalYn" /> <i><spring:message code="c.input.required" /></i>
				</th>
				<td>
					<input type="radio" ng-model="pmsCategory.secondApprovalYn" value="Y" /><label for="radio5"><spring:message code="c.input.radio.useY" /></label>
					<input type="radio" ng-model="pmsCategory.secondApprovalYn" value="N" /><label for="radio6"><spring:message code="c.input.radio.useN" /></label>
				</td>				
			</tr>
			<tr ng-if="pmsCategory.leafYn == 'Y'">
				<th>
					<spring:message code="pmsProducthistory.pointSaveRate" /> <i><spring:message code="c.input.required" /></i>
				</th>
				<td>
					<input type="text" value="" placeholder="" style="width:40%;" ng-model="pmsCategory.pointSaveRate" required />%
				</td>
				<th>
					<spring:message code="pmsCategory.newIconYn" /> <i><spring:message code="c.input.required" /></i>
				</th>
				<td>
					<input type="radio" ng-model="pmsCategory.newIconYn" value="Y" /><label for="radio7"><spring:message code="c.input.radio.useY" /></label>
					<input type="radio" ng-model="pmsCategory.newIconYn" value="N" /><label for="radio8"><spring:message code="c.input.radio.useN" /></label>
				</td>			
			</tr>	
			<tr ng-if="pmsCategory.leafYn == 'Y'">
				<th>
					<spring:message code="pmsCategoryrating.name" />
				</th>
				<td style="text-align:right" colspan="3">
					<button type="button" class="btn_type2" ng-click="addRatingType()"><b>별점항목추가</b></button>
					<%-- <button type="button" class="btn_type2" ng-click="saveCategoryRatingName()"><b><spring:message code="c.pmsCategoryRating.rating.name.update" /></b></button> --%>
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
			<tr ng-if="pmsCategory.leafYn == 'Y'">
				<th>속성정보</th>
				<td style="text-align:right" colspan="3">
					<div class="gridbox gridbox200" >
						<div class="grid" ui-grid="grid_attributeList"   
							ui-grid-move-columns 
							ui-grid-resize-columns 
							ui-grid-pagination
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

<%-- <div ng-if="pmsCategory.leafYn == 'Y'">
	<table class="tb_type1">
		<colgroup>
			<col width="18%">
			<col width="*">
		</colgroup>
		<tbody>
			<tr>
				<th>
					<spring:message code="pmsCategoryrating.name" />
				</th>
				<td style="text-align:right">
					<button type="button" class="btn_type2" ng-click="addRatingType()"><b>별점항목추가</b></button>
					<button type="button" class="btn_type2" ng-click="saveCategoryRatingName()"><b><spring:message code="c.pmsCategoryRating.rating.name.update" /></b></button>
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
</div> --%>

<div class="btn_alignC marginT3">
	<button type="button" class="btn_type3 btn_type3_purple" ng-click="updateCategory()">
		<b><spring:message code="c.common.save" /></b>
	</button>
</div>