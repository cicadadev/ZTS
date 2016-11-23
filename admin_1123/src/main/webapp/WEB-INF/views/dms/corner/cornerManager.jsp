<%--
	화면명 : 전시 관리 > 전시 코너 관리
	작성자 : eddie
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/dms.app.corner.manager.js"></script>

<article class="con_box con_on" data-ng-app="cornerManagerApp" data-ng-controller="dms_cornerManagerApp_controller as ctrl">
	<h2 class="sub_title1">전시코너 관리</h2>
	<div class="category">
		<button type="button" class="btn_type2 btn_type2_gray2" ng-click="ctrl.register();">
			<b>전시코너 등록</b>
		</button>
		
		<ul class="list_dep">
			<li class="dep{{category.depth}} {{category.lastNodeYn=='Y'?'end':category.depth==1 ? 'on' : category.icon }}" ng-repeat="category in trees" ng-show="(category.depth <= 1 || category.show=='Y')?true:false">
			<button ng-click="ctrl.openFolder($index, category.icon);" type="button"></button>
			<a href="javascript:void(0)" class="{{category.active}}" ng-click="getCornerDetail($event, category.displayId );">{{category.name}}</a>
			</li>
		</ul>
	</div>
	<form name="form2">
		<div class="columnR" ng-show="true">
		<span ng-if="!dmsDisplay">전시코너를 선택하세요.</span>
		<div class="box_type1" ng-if="dmsDisplay">
			<h3 class="sub_title2">
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
							전시코너번호 <i>필수입력</i>
						</th>
						<td>{{dmsDisplay.displayId}}</td>
						<th>
							전시코너명 <i>필수입력</i>
						</th>
						<td>
							<input type="text" name="v_name" ng-model="dmsDisplay.name" v-key="dmsDisplay.name" style="width:80%;" />
						</td>
					</tr>
					<tr>
						<th>
							전시여부 <i>필수입력</i>
						</th>
						<td>
							<input type="radio" ng-model="dmsDisplay.displayYn" value="Y"/>
							<label for="radio3">전시</label>
							<input type="radio" ng-model="dmsDisplay.displayYn" value="N" />
							<label for="radio4">미전시</label>
						</td>	
						<th>
							우선순위
						</th>
						<td>
							<input type="text" name="v_sortNo" ng-model="dmsDisplay.sortNo" v-key="dmsDisplay.sortNo" style="width:30%;"/>
						</td>
					</tr>
					<tr>
						<th>
							전시대상유형 <i>필수입력</i>
						</th>
						<td ng-if="dmsDisplay.displayId">{{dmsDisplay.displayItemTypeName}}</td>
						<td ng-if="!dmsDisplay.displayId">
							<select ng-model="dmsDisplay.displayItemTypeCd" style="width:150px;" select-code="DISPLAY_ITEM_TYPE_CD" v-key="dmsDisplay.displayItemTypeCd" required>
								<option value="">선택하세요</option>
							</select>
						</td>						
						<th>
							Leaf여부 <i>필수입력</i>
						</th>
						<td>
							<input type="radio" ng-model="dmsDisplay.leafYn" value="Y"/>
							<label for="radio1">Yes</label>
							<input type="radio" ng-model="dmsDisplay.leafYn" value="N"/>
							<label for="radio2">No</label>
						</td>						
					</tr>	
					<tr>
						<th>
							전시유형 <i>필수입력</i>
						</th>
						<td ng-if="dmsDisplay.displayId">{{dmsDisplay.displayTypeName}}</td>
						<td ng-if="!dmsDisplay.displayId">					
							<select ng-model="dmsDisplay.displayTypeCd" style="width:150px;" select-code="DISPLAY_TYPE_CD" v-key="dmsDisplay.displayTypeCd" required>
								<option value="">선택하세요</option>
							</select>
						</td>	
					</tr>				
				</tbody>
			</table>
			
		</div>

		<div class="btn_alignC marginT3" ng-if="dmsDisplay">
			<!-- <button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.checkDeleteCorner(dmsDisplay.displayId)" ng-if="dmsDisplay.displayId">
				<b>삭제</b>
			</button> -->
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="cancelReg()" ng-if="!dmsDisplay.displayId">
				<b><spring:message code="c.common.cancel" /></b><%-- 취소 --%>
			</button>				
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.updateCorner()">
				<b>저장</b>
			</button>
		</div>
		<!-- ### //전시카테고리 기본정보 설정 ### -->


		<span ng-if="girdView && dmsDisplay.displayId!='' && dmsDisplay.displayId!=null">
		<!-- ### 코너 목록 ### -->
		<div class="btn_alignR marginT3">
		
			<button ng-show="dmsDisplay.displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.PRODUCT'" type="button" class="btn_type1" ng-click="ctrl.openItemRegPopup('product')">
				<b>상품등록</b>
			</button>		
			<button ng-show="dmsDisplay.displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.HTML'" type="button" class="btn_type1" ng-click="ctrl.openItemRegPopup('html')">
				<b>HTML등록</b>
			</button>		
			<button ng-show="dmsDisplay.displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.EXHIBIT'" type="button" class="btn_type1" ng-click="ctrl.openItemRegPopup('exhibit')">
				<b>기획전 등록</b>
			</button>			
			<button ng-show="dmsDisplay.displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.IMG'" type="button" class="btn_type1" ng-click="ctrl.openItemRegPopup('img')">
				<b>배너등록</b>
			</button>
			<button ng-show="dmsDisplay.displayItemTypeCd=='DISPLAY_ITEM_TYPE_CD.TEXT'" type="button" class="btn_type1" ng-click="ctrl.openItemRegPopup('text')">
				<b>텍스트등록</b>
			</button>						
			<button type="button" class="btn_type1" ng-click="myGrid.deleteGridData()">
				<b>삭제</b>
			</button>
			<button type="button" class="btn_type1" ng-click="saveGridData()">
				<b>저장</b>
			</button>
<!-- 			<button type="button" class="btn_type1" ng-click="myGrid.loadGridData()">
				<b>조회</b>
			</button> -->
		</div>

		<div class="box_type1 marginT1">
			<h3 class="sub_title2">
				아이템 목록
				<span>(총 <b>{{grid_cornerItemList.totalItems}}</b>건)</span>
			</h3>

			<div class="tb_util tb_util_rePosition">
 				<button type="button" class="btn_tb_util tb_util1" ng-click="myGrid.initGrid()">되돌리기</button>
<!-- 				<button type="button" class="btn_tb_util tb_util2">엑셀받기</button>
				<button type="button" class="btn_tb_util tb_util5" ng-click="ctrl.addRow()">행추가</button> -->
			</div>

			<div class="gridbox gridbox200">
   				<div class="grid" data-ui-grid="grid_cornerItemList"   
					data-ui-grid-move-columns 
					data-ui-grid-resize-columns
					data-ui-grid-pagination
					data-ui-grid-auto-resize
					data-ui-grid-selection 
					data-ui-grid-row-edit
					data-ui-grid-cell-nav
					data-ui-grid-edit 
					data-ui-grid-validate></div>
			</div>
		</div>
 		</span>
		<!-- ### //코너 목록 ### -->
	</div>
	</form>
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>