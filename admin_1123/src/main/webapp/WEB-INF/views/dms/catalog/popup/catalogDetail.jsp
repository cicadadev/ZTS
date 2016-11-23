<%--
	화면명 : 상품 관리 > 브랜드 관리 > 브랜드 상세 팝업 > 브랜드샵 정보 탭 > 컨텐츠 상세 팝업
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/dms.app.catalog.manager.js"></script>

<div ng-app="catalogApp" ng-controller="dms_catalogDetailPopApp_controller as ctrl" ng-init="ctrl.init()" class="wrap_popup">
	<h1 class="sub_title1" ng-if="dmsCatalog.catalogId == '' || dmsCatalog.catalogId == undefined"><spring:message code="dms.catalog.btn.register" /></h1>
	<h1 class="sub_title1" ng-if="dmsCatalog.catalogId != '' && dmsCatalog.catalogId != undefined"><spring:message code="dms.catalog.detail.title" /></h1>
	
	
	<ul class="tab_type2" ng-if="dmsCatalog.catalogId != '' && dmsCatalog.catalogId != undefined">
		<li class="on">
			<button type="button" ng-click="ctrl.moveTab($event, 'detail')" name="detail"><spring:message code="c.dms.catalog.basicInfo"/><!-- 기본정보 --></button>
		</li>
		<li>
			<button type="button" ng-click="ctrl.moveTab($event, 'img')" name="title"><spring:message code="c.dms.catalog.imgInfo"/><!-- 컨텐츠 정보 --></button>
		</li>
	</ul>
	
	<ul class="tab_type2" ng-if="dmsCatalog.catalogId == '' || dmsCatalog.catalogId == undefined">
		<li class="on">
			<button type="button" name="detail"><spring:message code="c.dms.catalog.basicInfo"/><!-- 기본정보 --></button>
		</li>
		<li class="disabled">
			<button type="button" name="title"><spring:message code="c.dms.catalog.imgInfo"/><!-- 컨텐츠 정보 --></button>
		</li>
	</ul>
	
	<div class="box_type1">
		<form name="form2">
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
							<spring:message code="c.dms.catalog.catalogNo" />
						</th>
						<td colspan="3" ng-bind="dmsCatalog.catalogId"></td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.dms.catalog.catalog.name" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td>
							<input type="text" value="" placeholder="" style="width:98%;" ng-model="dmsCatalog.name" v-key="dmsCatalog.name" required/>
						</td>
						<th>
							<spring:message code="c.dms.catalog.seasonCd" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td id="seasonSelect">
							<select-list ng-model="dmsCatalog.seasonCd" code-group="SEASON_CD" required></select-list>
						</td>
					</tr>					
					<tr>
						<th>
							<spring:message code="dmsCatalog.displayYn" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td ng-init="dmsCatalog.displayYn = 'Y'">
							<input type="radio" ng-model="dmsCatalog.displayYn" value="Y" /><label for="radio1"><spring:message code="c.input.radio.displayY" /></label>
							<input type="radio" ng-model="dmsCatalog.displayYn" value="N" /><label for="radio2"><spring:message code="c.input.radio.displayN" /></label>
						</td>
						<th>
							<spring:message code="c.dms.catalog.sortNo" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td>
							<input type="number" value="" placeholder="" style="width:50%;" ng-model="dmsCatalog.sortNo" v-key="dmsCatalog.sortNo" required/>
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.dms.catalog.catalogType" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td ng-init="dmsCatalog.catalogTypeCd = 'CATALOG_TYPE_CD.LOOKBOOK'">
							<input type="radio" ng-model="dmsCatalog.catalogTypeCd" value="CATALOG_TYPE_CD.LOOKBOOK" /><label for="radio3"><spring:message code="c.dms.catalog.catalogType.lookbook" /></label>
							<input type="radio" ng-model="dmsCatalog.catalogTypeCd" value="CATALOG_TYPE_CD.COORDILOOK" /><label for="radio4"><spring:message code="c.dms.catalog.catalogType.coordiLook" /></label>
						</td>
						<th><spring:message code="c.dms.catalog.brand.noname" /> <i><spring:message code="c.input.required" /></i></th>
						<td>
							<input type="text" placeholder="" ng-model="dmsCatalog.brandId" readonly disabled required />
							<input type="text" placeholder="" ng-model="dmsCatalog.brandName" readonly disabled required />
							<button type="button" class="btn_type2" ng-click="ctrl.brandSearch()">
								<b><spring:message code="c.search.btn.search" /></b>
							</button>
							<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : dmsCatalog.brandId == null || dmsCatalog.brandName == null}" ng-click="ctrl.eraseBrand()"><spring:message code="c.search.btn.eraser" /></button>
							<p class="information" ng-show="dmsCatalog.brandId == null || dmsCatalog.brandName == null">필수 입력 항목입니다.</p>
						</td>						
					</tr>
					<tr class="column2">
						<th>
							<spring:message code="c.dms.catalog.pcBannerImg"/> <i><spring:message code="c.input.required" /></i>				
						</th>
						<td>
							<div class="input_file">
								<input type="file" id="pcCatalogBanner" name="pcfileModel" image-key="dmsCatalog.img1" file-upload />
								<input type="hidden" id="img1Path" ng-model="dmsCatalog.img1" placeholder="" required />
								<button type="button" class="btn_type2 btn_addFile">
									<b><spring:message code="c.common.file.search"/></b>
								</button>
								<span><spring:message code="common.imgUpload.text1"/></span>
							</div>
							<p class="txt_type1">
								<spring:message code="common.imgUpload.text2"/>
							</p>
							<p class="information" ng-show="dmsCatalog.img1 == null">필수 입력 항목 입니다.</p>
							<div class="preview" ng-show="dmsCatalog.img1 != null && dmsCatalog.img1 != ''">
								<img ng-src="dmsCatalog.img1" onError="this.src='/resources/img/bg/bg_temp_img.gif';" img-domain alt="" name="pcCatalogBanner"/>
								<button type="button" class="btn_file_del" ng-click="ctrl.deletePcImage('pcCatalogBanner')">파일 삭제</button>
							</div>
						</td>
						
						<th>
							<spring:message code="c.dms.catalog.mbBannerImg"/> <i><spring:message code="c.input.required" /></i>
						</th>
						<td>
							<div class="input_file">
								<input type="file" id="mbCatalogBanner" name="mofileModel" image-key="dmsCatalog.img2" file-upload/>
								<input type="hidden" id="img2Path" ng-model="dmsCatalog.img2" placeholder="" required />
								<button type="button" class="btn_type2 btn_addFile">
									<b><spring:message code="c.common.file.search"/></b>
								</button>
								<span><spring:message code="common.imgUpload.text1"/></span>
							</div>
							<p class="txt_type1">
								<spring:message code="common.imgUpload.text2"/>
							</p>
							<p class="information" ng-show="dmsCatalog.img2 == null">필수 입력 항목 입니다.</p>
							<div class="preview" ng-show="dmsCatalog.img2 != null && dmsCatalog.img2 != ''">
								<img ng-src="dmsCatalog.img2" onError="this.src='/resources/img/bg/bg_temp_img.gif';" img-domain alt="" name="mbCatalogBanner"/>
								<button type="button" class="btn_file_del" ng-click="ctrl.deleteMbImage('mbCatalogBanner')">파일 삭제</button>
							</div>
						</td>
					</tr>				
				</tbody>
			</table>
		</form>
	</div>
	
	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /></b>
		</button>
		<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.insertCatalog()">
			<b><spring:message code="c.common.save" /></b>
		</button>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>