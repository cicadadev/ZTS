<%--
	화면명 : 기획전 관리 > 기획전 등록 팝업
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>    
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/dms.app.exhibit.manager.js"></script>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>

<div class="wrap_popup"  ng-app="exhibitApp" data-ng-controller="dms_exhibitInsertPopApp_controller as ctrl" data-ng-init="init()">
		<h1 class="sub_title1"><spring:message code="c.dmsExhibit.regExhibit"/><!-- 기획전 등록 --></h1>
		<ul class="tab_type2">
			<li class="on">
				<button type="button"><spring:message code="c.dmsExhibit.basicInfo"/><!-- 기획전 기본정보 --></button>
			</li>
			<li class="disabled">
				<button type="button"><spring:message code="c.dmsExhibit.divTitleInfo"/><!-- 구분타이틀정보 --></button>
			</li>
		</ul>
		<div class="box_type1 marginT2">
		<form name="exhibitInsertForm">
			<table class="tb_type1">
				<colgroup>
					<col class="col_142" />
					<col class="col_auto" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							<spring:message code="c.dmsExhibit.exhibitNo"/><!-- 기획전번호 -->
						</th>
						<td colspan="3">
						</td>
					<tr>
					<tr>
						<th>
							<spring:message code="c.dmsExhibit.exhibitName"/><!-- 기획전명 --> <i><spring:message code="c.input.required"/><!-- 필수입력 --></i>
						</th>
						<td>
							<input data-ng-model="dmsExhibit.name" id="name" type="text" name="name" placeholder="" style="width:40%;" v-key="dmsExhibit.name" required/>
						</td>
						<th>
							기획전 부제
						</th>
						<td>
							<input data-ng-model="dmsExhibit.subtitle" id="subtitle" type="text" name="subtitle" placeholder="" style="width:40%;" v-key="dmsExhibit.subtitle"/>
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.dmsExhibitgroup.sortNo"/><!-- 우선 순위 --> <i><spring:message code="c.input.required"/><!-- 필수입력 --></i>
						</th>
						<td>
							<input data-ng-model="dmsExhibit.sortNo" id="sortNo" type="number" name="sortNo" placeholder="" style="width:30%;" v-key="dmsExhibit.sortNo" required/>
						</td>
						<th>
							<spring:message code="c.dmsExhibit.exhibitType"/><!-- 기획전 유형 --> <i><spring:message code="c.input.required"/><!-- 필수입력 --></i>
						</th>
						<td ng-init="dmsExhibit.exhibitTypeCd='EXHIBIT_TYPE_CD.EXHIBIT'">
							<radio-list ng-model="dmsExhibit.exhibitTypeCd" code-group="EXHIBIT_TYPE_CD" />
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.dmsExhibit.exhibitPeriod"/><!-- 기획전기간 --> <i><spring:message code="c.input.required"/><!-- 필수입력 --></i>
						</th>
						<td>
							<input data-ng-model="dmsExhibit.startDt" id="startDt" type="text" value="" placeholder="" datetime-picker v-key="dmsExhibit.startDt" period-start/>
							~
							<input data-ng-model="dmsExhibit.endDt" id="endDt" type="text" value="" placeholder="" datetime-picker v-key="dmsExhibit.endDt" period-end/>
							<p class="information" ng-show="!dmsExhibit.startDt || !dmsExhibit.endDt"><spring:message code="c.common.invalid.content"/> <!-- 필수 입력 항목 입니다. --></p>
							</br>
							<div id="daysweek">
								
								<input type="checkbox" ng-model="dmsExhibit.daysWeek1" id="all" name="all" ng-click="ctrl.daysweekClick($event)">
								<label for="all"><spring:message code="c.select.all"/><!-- 전체 --></label> 
								<input type="checkbox" ng-model="dmsExhibit.daysWeek2" id="monday" name="monday" ng-click="ctrl.daysweekClick($event)">
									<label for="monday"><spring:message code="c.dmsExhibit.monday"/><!-- 월 --></label> 
								<input type="checkbox" ng-model="dmsExhibit.daysWeek3" id="tuesday" name="tuesday" ng-click="ctrl.daysweekClick($event)">
									<label for="tuesday"><spring:message code="c.dmsExhibit.tuesday"/><!-- 화 --></label> 
								<input type="checkbox" ng-model="dmsExhibit.daysWeek4" id="wednesday" name="wednesday" ng-click="ctrl.daysweekClick($event)">
									<label for="wednesday"><spring:message code="c.dmsExhibit.wednesday"/><!-- 수 --></label> 
								<input type="checkbox" ng-model="dmsExhibit.daysWeek5" id="thursday" name="thursday" ng-click="ctrl.daysweekClick($event)">
									<label for="thursday"><spring:message code="c.dmsExhibit.thursday"/><!-- 목 --></label> 
								<input type="checkbox" ng-model="dmsExhibit.daysWeek6" id="friday" name="friday" ng-click="ctrl.daysweekClick($event)">
									<label for="friday"><spring:message code="c.dmsExhibit.friday"/><!-- 금 --></label> 
								<input type="checkbox" ng-model="dmsExhibit.daysWeek7" id="saturday" name="saturday" ng-click="ctrl.daysweekClick($event)">
									<label for="saturday"><spring:message code="c.dmsExhibit.saturday"/><!-- 토 --></label> 
								<input type="checkbox" ng-model="dmsExhibit.daysWeek8" id="sunday" name="sunday" ng-click="ctrl.daysweekClick($event)">
									<label for="sunday"><spring:message code="c.dmsExhibit.sunday"/><!-- 일 --></label>
								
							</div> 
						</td>
						<th>
							<spring:message code="c.dmsExhibit.exposure.target"/><!-- 노출 대상 --> <i><spring:message code="c.input.required"/><!-- 필수입력 --></i>
						</th>
						<td>
							<%--제외옵션 :  except="mgrade,mtype,device,channel"  --%>
							<control-set model-name=dmsExhibit lebels="전체,일부"></control-set>
						</td>
					</tr>
					<tr ng-show="dmsExhibit.exhibitTypeCd=='EXHIBIT_TYPE_CD.ONEDAY'">
						<th><spring:message code="c.dmsExhibitproduct.mainProduct" /><!-- 대표 상품 --></th>
							
						<td colspan="3">
							<div class="btn_alignR marginT1">
								<button type="button" class="btn_type2" data-ng-click="ctrl.addMainProduct()">
									<b><spring:message code="c.dmsExhibitproduct.regMainProduct" /><!-- 대표상품 등록 --></b>
								</button>
								<button type="button" class="btn_type2" data-ng-click="myGrid2.deleteRow();">
									<b><spring:message code="c.common.delete" /><!-- 삭제 --></b>
								</button>					
							</div>
							<div class="box_type1 marginT1" >
								<h3 class="sub_title2">
									<spring:message code="c.dmsExhibitproduct.mainProduct" />
									<span><spring:message code="c.search.totalCount" arguments="{{ grid_mainProduct.data.length}}" /></span>
								</h3>
								<div  class="gridbox gridbox200">
					   				<div class="grid" data-ui-grid="grid_mainProduct"   
										data-ui-grid-move-columns 
										data-ui-grid-resize-columns 
										data-ui-grid-auto-resize 
										data-ui-grid-selection 
										data-ui-grid-row-edit
										data-ui-grid-cell-nav
										data-ui-grid-exporter
										data-ui-grid-edit 
										data-ui-grid-validate>
									</div>								
								</div>
								<p class="information" ng-show="grid_mainProduct.data.length==0">필수 입력 항목 입니다.</p>							
							</div>
						</td>
					</tr>
					
					<tr ng-show="dmsExhibit.exhibitTypeCd=='EXHIBIT_TYPE_CD.COUPON'">
						<th><spring:message code="c.dmsExhibit.coupon" /><!-- 쿠폰 --></th>
							
						<td colspan="3">
							<div class="btn_alignR marginT1">
								<button type="button" class="btn_type2" data-ng-click="ctrl.addCoupon()">
									<b><spring:message code="c.dmsExhibit.add.coupon" /><!-- 쿠폰 추가 --></b>
								</button>
								<button type="button" class="btn_type2" data-ng-click="myGrid1.deleteRow();">
									<b><spring:message code="c.common.delete" /><!-- 삭제 --></b>
								</button>					
							</div>
							<div class="box_type1 marginT1" >
								<h3 class="sub_title2">
									<spring:message code="c.dmsExhibit.coupon" />
									<span><spring:message code="c.search.totalCount" arguments="{{ grid_coupon.data.length}}" /></span>
								</h3>
								<div  class="gridbox gridbox300">
					   				<div class="grid" data-ui-grid="grid_coupon"   
										data-ui-grid-move-columns 
										data-ui-grid-resize-columns 
										data-ui-grid-auto-resize 
										data-ui-grid-selection 
										data-ui-grid-row-edit
										data-ui-grid-cell-nav
										data-ui-grid-exporter
										data-ui-grid-edit 
										data-ui-grid-validate>
									</div>								
								</div>
								<p class="information" ng-show="grid_coupon.data.length==0">필수 입력 항목 입니다.</p>								
							</div>
						</td>
					</tr>
					<tr ng-show="dmsExhibit.exhibitTypeCd=='EXHIBIT_TYPE_CD.OFFSHOP'">
						<th>오프라인 매장 등록</th>
							
						<td colspan="3">
							<div class="btn_alignR marginT1">
								<button type="button" class="btn_type2" data-ng-click="ctrl.batchOffshop()">
									<b>오프라인매장 일괄등록</b>
								</button>
								<button type="button" class="btn_type2" data-ng-click="ctrl.addOffshop()">
									<b>오프라인매장등록</b>
								</button>					
								<button type="button" class="btn_type2" data-ng-click="myGrid1.deleteRow();">
									<b>삭제</b>
								</button>					
							</div>
							<div class="box_type1 marginT1" >
								<h3 class="sub_title2">
									오프라인 매장 등록
									<span><spring:message code="c.search.totalCount" arguments="{{ grid_offshop.data.length}}" /></span>
								</h3>
								<div class="gridbox gridbox200">
					   				<div class="grid" data-ui-grid="grid_offshop"   
										data-ui-grid-move-columns 
										data-ui-grid-resize-columns 
										data-ui-grid-auto-resize 
										data-ui-grid-selection 
										data-ui-grid-row-edit
										data-ui-grid-cell-nav
										data-ui-grid-exporter
										data-ui-grid-edit 
										data-ui-grid-validate>
									</div>								
								</div>
								<p class="information" ng-show="grid_offshop.data.length==0">필수 입력 항목 입니다.</p>								
							</div>
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.dmsExhibit.exhibitStateCd"/><!-- 진행여부 --> <i><spring:message code="c.input.required"/><!-- 필수입력 --></i>
						</th>
						<td>
							대기
						</td>
						<th>
							<spring:message code="c.dmsDisplaycategory.category"/><!-- 전시카테고리 --> <i><spring:message code="c.input.required"/><!-- 필수입력 --></i>
						</th>
						<td>
							<input type="text" data-ng-model="dmsExhibit.displayCategoryName"  name="displayCategoryName" style="width:60%;" readonly disabled required/>
							<button type="button" class="btn_type2" ng-click="ctrl.categorySearch()"><b><spring:message code="c.search.btn.search" /><!-- 검색 --></b></button>
							<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : dmsExhibit.displayCategoryName == null || dmsExhibit.displayCategoryName == ''}" ng-click="ctrl.deleteCategory()"></button>
							<p class="information" ng-show="exhibitInsertForm.displayCategoryName.$error.required"><spring:message code="c.common.invalid.content"/> <!-- 필수 입력 항목 입니다. --></p>
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.pmsBrand.brand"/><!-- 브랜드 --> <i><spring:message code="c.input.required"/><!-- 필수입력 --></i>
						</th>
						<td id="td_brand">
							<div>
								<input type="text" data-ng-model="dmsExhibit.brandId0" name="brandId" style="width:29%;" readonly disabled required/> <input type="text" data-ng-model="dmsExhibit.brandName0" style="width:30%;" readonly disabled/> 
								<button type="button" class="btn_type2" ng-click="ctrl.brandSearch($event)" data-index="0"><b><spring:message code="c.search.btn.search" /><!-- 검색 --></b></button>
								<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : dmsExhibit.brandId0 == null || dmsExhibit.brandId0 == ''}" ng-click="ctrl.deleteBrand(0)"></button>
								<button type="button" class="btn_plus" ng-click="ctrl.addBrand(0)"><spring:message code="c.common.add" /><!-- 추가 --></button>
								<p class="information" ng-show="exhibitInsertForm.brandId.$error.required"><spring:message code="c.common.invalid.content"/> <!-- 필수 입력 항목 입니다. --></p>
							</div>
						</td>
						<th>
							노출 여부<!-- 노출 여부 --> <i><spring:message code="c.input.required"/><!-- 필수입력 --></i>
						</th>
							<td ng-init="dmsExhibit.displayYn='Y'">
								<input type="radio" value="Y" data-ng-model="dmsExhibit.displayYn" id="radio11"/>
								<label for="radio11">노출<!-- 노출 --></label>
								<input type="radio" value="N" data-ng-model="dmsExhibit.displayYn" id="radio12"/>
								<label for="radio12">비노출<!-- 비노출 --></label>
							</td>
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.dmsExhibit.pcTopExhibit"/><!-- PC 기획전 TOP -->
						</th>
						<td colspan="3">
							<textarea ckeditor="ckOption" ng-model="dmsExhibit.html1" v-key="dmsExhibit.html1" height="300px" required></textarea>
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.dmsExhibit.mbTopExhibit"/><!-- MOBILE 기획전 TOP -->
						</th>
						<td colspan="3">
							<textarea ckeditor="ckOption" ng-model="dmsExhibit.html2" v-key="dmsExhibit.html2" height="300px" required></textarea>
						</td>
					</tr>
					<tr class="column2">
						<th>
							<spring:message code="c.dmsExhibit.pcExhibitBanner"/><!-- PC<br />기획전 배너 이미지 -->
							
						</th>
						<td>
							<div class="input_file">
								<input type="file" callback="uploadPcImageCallback" file-upload>
								<button type="button" class="btn_type2 btn_addFile">
									<b><spring:message code="c.common.file.search"/><!-- 찾아보기 --></b>
								</button>
								<span><spring:message code="c.dmsExhibit.imgSize"/><!-- (이미지 사이즈 : 000*000) --></span>
							</div>

							<p class="txt_type1">
								<spring:message code="c.dmsExhibit.imgType"/><!-- * jpg, png 00MB 이하의 파일만 업로드가 가능합니다. -->
							</p>

							<div class="preview" ng-show="dmsExhibit.img1 != '' && dmsExhibit.img1 != null">
								<img ng-src="dmsExhibit.img1" img-domain onError="this.src='/resources/img/bg/bg_temp_img.gif';" alt="">
								<button type="button" class="btn_file_del" ng-click="ctrl.deletePcImage($index)">파일 삭제</button>
							</div>
						</td>
						
						<th>
							<spring:message code="c.dmsExhibit.mbExhibitBanner"/><!-- MOBILE<br />기획전 배너 이미지 -->
						</th>
						<td>
							<div class="input_file">
								<input type="file" callback="uploadMbImageCallback" file-upload>
								<button type="button" class="btn_type2 btn_addFile">
									<b><spring:message code="c.common.file.search"/><!-- 찾아보기 --></b>
								</button>
								<span><spring:message code="c.dmsExhibit.imgSize"/><!-- (이미지 사이즈 : 000*000) --></span>
							</div>

							<p class="txt_type1">
								<spring:message code="c.dmsExhibit.imgType"/><!-- * jpg, png 00MB 이하의 파일만 업로드가 가능합니다. -->
							</p>

							<div class="preview" ng-show="dmsExhibit.img2 != '' && dmsExhibit.img2 != null">
								<img ng-src="dmsExhibit.img2" img-domain onError="this.src='/resources/img/bg/bg_temp_img.gif';" alt="">
								<button type="button" class="btn_file_del" ng-click="ctrl.deleteMbImage($index)">파일 삭제</button>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
			</form>
		</div>

		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b><spring:message code="c.common.close" /><!-- 닫기 --></b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.saveExhibit()">
				<b><spring:message code="c.common.save" /><!-- 저장 --></b>
			</button>
		</div>
</div>

<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>