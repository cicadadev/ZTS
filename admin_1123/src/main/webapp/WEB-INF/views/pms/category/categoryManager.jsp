<%--
	화면명 : 상품 관리 > 표준카테고리 관리
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.category.manager.js"></script>

<article class="con_box con_on" ng-app="categoryApp"  ng-controller="pms_categoryManagerApp_controller as ctrl">
	<h2 class="sub_title1">표준카테고리 관리</h2>
	<!-- ### //카테고리 목록 ### -->
	<div class="category">
		<button type="button" class="btn_type2 btn_type2_gray2" ng-click="ctrl.register()">
			<b><spring:message code="c.pmsCategory.category.insert" /></b>
		</button>
		
		<ul class="list_dep">
			<li class="dep{{category.depth}} {{category.lastNodeYn=='Y'?'end':category.icon }}" ng-repeat="category in trees" ng-if="(category.depth <= 1 || category.show=='Y')?true:false">
				<button ng-click="openFolder($index, category.icon)" type="button"></button>
				<a href="javascript:void(0)" class="{{category.active}}" ng-click="getCategoryDetail($event, category)">{{category.name}}</a>
			</li>
		</ul>		
	</div>

	<div class="columnR">
<%-- 		<ul class="tab_type2" ng-if="pmsCategory">
			<li ng-class="tab1"><a href="#tab1"><button type="button"><spring:message code="c.pmsCategory.basicInfo"/></button></a>
			</li>
			<li ng-class="tab2"><a href="#tab2"><button type="button"><spring:message code="c.pmsCategory.attributeInfo"/></button></a>
			</li>
		</ul>	 --%>	
		
		<form name="form">
		    <div ng-if="!pmsCategory">표준카테고리를 선택해 주세요.</div>
			<div class="box_type1" ng-if="pmsCategory">
				<h3 class="sub_title2" ng-if="pmsCategory.categoryId"><spring:message code="pms.category.info.management" /></h3>
				<h3 class="sub_title2" ng-if="!pmsCategory.categoryId">표준카테고리 등록</h3>
				<table class="tb_type1">
					<colgroup>
						<col width="15%" />
						<col width="30%" />
						<col width="15%" />
						<col width="*" />
					</colgroup>
					<tbody>
						<tr>
							<th>
								표준카테고리번호<i><spring:message code="c.input.required" /></i>
							</th>
							<td ng-bind="pmsCategory.categoryId"></td>
							<th>
								<spring:message code="pmsCategory.name" /> <i><spring:message code="c.input.required" /></i>
							</th>
							<td>
								<input type="text" style="width:60%;" ng-model="pmsCategory.name" v-key="pmsCategory.name" />
							</td>
						</tr>
						<tr>
<%-- 							<th>
								<spring:message code="pmsCategory.useYn" /> <i><spring:message code="c.input.required" /></i>
							</th>
							<td>
								<input type="radio" ng-model="pmsCategory.useYn" value="Y" /><label for="radio1"><spring:message code="c.input.radio.useY" /></label>
								<input type="radio" ng-model="pmsCategory.useYn" value="N" /><label for="radio2"><spring:message code="c.input.radio.useN" /></label>
							</td> --%>
							<th>
								<spring:message code="c.pmsCategory.sortNo" />
							</th>
							<td>
								<input type="text"  maxlength="3" style="width:150px;" ng-model="pmsCategory.sortNo" v-key="pmsCategory.sortNo" />
							</td>		
							<th>
								<spring:message code="pmsCategory.leafYn" /> <i><spring:message code="c.input.required" /></i>
							</th>
							<td>
								<input type="radio" ng-model="pmsCategory.leafYn" value="Y" /><label for="radio3">Yes</label>
								<input type="radio" ng-model="pmsCategory.leafYn" value="N" /><label for="radio4">No</label>
							</td>										
						</tr>
						<tr ng-if="pmsCategory.leafYn == 'Y'">
							<th>
								<spring:message code="c.pmsCategory.md.idnname" /> <i><spring:message code="c.input.required" /></i>
							</th>
							<td>
								<input type="text"  style="width:30%;" ng-model="pmsCategory.userId" v-key="required" />
								<input type="text"  style="width:30%;" ng-model="pmsCategory.mdName" />
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
								<spring:message code="pmsCategory.pointSaveRate" /> <i><spring:message code="c.input.required" /></i>
							</th>
							<td>
								<input type="text" value="" placeholder="" maxlength="3" style="width:30px;" ng-model="pmsCategory.pointSaveRate" v-key="pmsCategory.pointSaveRate" /><span>%</span>
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
								<spring:message code="pmsCategory.erpProductId" />
							</th>
							<td colspan="3">
								<input type="text" style="width:150px" ng-model="pmsCategory.erpProductId" v-key="pmsCategory.erpProductId"/>							
							</td>
						</tr>						
						<tr ng-if="pmsCategory.leafYn == 'Y'">
							<th>
								<spring:message code="pmsCategoryrating.name" />
							</th>
							<td style="text-align:right">
								<button type="button" class="btn_type2" data-ng-click="ctrl.deleteRatingType()" ng-if="!pmsCategory.categoryId">
									<b><spring:message code="c.common.delete" /></b>
								</button>
								<button type="button" class="btn_type2" ng-click="addRatingType()"><b>별점항목추가</b></button>
								<div class="gridbox gridbox200" style="margin-top:-26px">
					   				<div class="grid" ui-grid="grid_categoryRating"   
										ui-grid-move-columns 
										ui-grid-resize-columns
										ui-grid-selection 
										ui-grid-row-edit
										ui-grid-cell-nav
										ui-grid-edit 
										ui-grid-validate></div>
								</div>
							</td>		
							<th>속성정보</th>
							<td style="text-align:right" >
								<button type="button" class="btn_type2" data-ng-click="ctrl.deleteCategoryAttribute()" ng-if="!pmsCategory.categoryId">
									<b><spring:message code="c.common.delete" /></b>
								</button>
								<button type="button" class="btn_type2" ng-click="registerCategoryAttribute()">
									<b><spring:message code="c.pmsCategory.attribute.insert" /></b>
								</button>
								<div class="gridbox gridbox200" style="margin-top:-26px">
									<div class="grid" ui-grid="grid_attributeList"   
										ui-grid-move-columns 
										ui-grid-resize-columns 
										ui-grid-selection 
										ui-grid-row-edit
										ui-grid-cell-nav
										ui-grid-edit 
										ui-grid-validate></div>
								</div>	
							</td>													
						</tr>		
					</tbody>
				</table>
			</div>
			
			<div class="btn_alignC marginT3"  ng-if="pmsCategory">
				<button type="button" class="btn_type3 btn_type3_gray" ng-click="deleteCategory()" ng-if="pmsCategory.categoryId">
					<b><spring:message code="c.common.delete" /></b><%-- 삭제 --%>
				</button>			
				<button type="button" class="btn_type3 btn_type3_gray" ng-click="cancelReg()" ng-if="!pmsCategory.categoryId">
					<b><spring:message code="c.common.cancel" /></b><%-- 취소 --%>
				</button>					
				<button type="button" class="btn_type3 btn_type3_purple" ng-click="updateCategory()">
					<b><spring:message code="c.common.save" /></b><%-- 저장 --%>
				</button>
			</div>
		
		</form>
	</div>	

</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>