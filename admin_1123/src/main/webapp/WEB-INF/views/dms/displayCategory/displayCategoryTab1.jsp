<%--
	화면명 : 전시 관리 > 전시 카테고리 관리 > 전시 카테고리 기본정보 탭
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<!-- ### 전시카테고리 기본정보 설정 ### -->
<div class="box_type1">
	<h3 class="sub_title2">
		<spring:message code="dms.displaycategory.info.management" />
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
					<spring:message code="dmsDisplaycategory.displayCategoryId" /> <i><spring:message code="c.input.required" /></i>
				</th>
				<td ng-bind="dmsDisplaycategory.displayCategoryId"></td>
				<th>
					<spring:message code="dmsDisplaycategory.name" /> <i><spring:message code="c.input.required" /></i>
				</th>
				<td>
					<input type="text" style="width:60%;" ng-model="dmsDisplaycategory.name" v-key="dmsDisplaycategory.name"/>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="dmsDisplaycategory.leafYn" /> <i><spring:message code="c.input.required" /></i>
				</th>
				<td><%--리프여부 --%>
					<input type="radio" ng-model="dmsDisplaycategory.leafYn" value="Y" /><label for="radio1">Yes</label>
					<input type="radio" ng-model="dmsDisplaycategory.leafYn" value="N" /><label for="radio2">No</label>
				</td>
				<th>
					<spring:message code="c.dmsDisplaycategory.sortNo" />
				</th>
				<td><%--우선순위 --%>
					<input type="text" style="width:40%;" ng-model="dmsDisplaycategory.sortNo" v-key="dmsDisplaycategory.sortNo"/>
				</td>
			</tr>
			<tr>
				<th>
					<spring:message code="c.dmsDisplaycategory.template" />
				</th>
				<td><%--카테고리 템플릿 --%>
					<select id="templateSelect" style="min-width:240px;" ng-model="dmsDisplaycategory.templateId" 
						ng-options="template.templateId as template.name for template in templateList">
						<option value="">선택하세요</option>
					</select>
				</td>
				<th>
					<spring:message code="dmsDisplaycategory.displayYn" /> <i><spring:message code="c.input.required" /></i>
				</th>
				<td><%--전시여부 --%>
					<input type="radio" ng-model="dmsDisplaycategory.displayYn" value="Y" /><label for="radio3"><spring:message code="c.input.radio.displayY" /></label>
					<input type="radio" ng-model="dmsDisplaycategory.displayYn" value="N" /><label for="radio4"><spring:message code="c.input.radio.displayN" /></label>
				</td>							
			</tr>
		</tbody>
	</table>
</div>

<div class="btn_alignC marginT3" ng-if="dmsDisplaycategory">
	<button type="button" class="btn_type3 btn_type3_gray" ng-click="deleteCategory()" ng-if="dmsDisplaycategory.displayCategoryId">
		<b><spring:message code="c.common.delete" /></b>
	</button>
	<button type="button" class="btn_type3 btn_type3_gray" ng-click="cancelReg()" ng-if="!dmsDisplaycategory.displayCategoryId">
		<b><spring:message code="c.common.cancel" /></b><%-- 취소 --%>
	</button>			
	<button type="button" class="btn_type3 btn_type3_purple" ng-click="updateCategory()">
		<b><spring:message code="c.common.save" /></b>
	</button>
</div>