<%--
	화면명 : 전시 관리 > 브랜드 컨텐츠 관리 > 브랜드 컨텐츠 상세 팝업 > 이미지 등록 팝업
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/dms.app.catalog.manager.js"></script>
	
<div ng-app="catalogApp" ng-controller="dms_catalogImgInsertPopApp_controller as ctrl" ng-init="ctrl.init()" class="wrap_popup">
	<form name="form">
		<h1 class="sub_title1" ng-show="dmsCatalogimg.catalogImgNo != '' && dmsCatalogimg.catalogImgNo != undefined">컨텐츠 이미지 상세</h1>
		<h1 class="sub_title1" ng-show="dmsCatalogimg.catalogImgNo == '' || dmsCatalogimg.catalogImgNo == undefined">컨텐츠 이미지 등록</h1>

		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="19%" />
					<col width="37%" />
					<col width="13%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							PC 이미지 <i><spring:message code="c.input.required" /></i>
						</th>
						<td colspan="3">
							<div class="input_file">
								<input type="file" id="catalogImg" name="fileModel" image-key="dmsCatalogimg.img1" file-upload/>
								<input type="hidden" id="img1Path" ng-model="dmsCatalogimg.img1" placeholder="" required />
								<button type="button" class="btn_type2 btn_addFile">
									<b><spring:message code="c.common.file.search"/></b>
								</button>
								<span><spring:message code="common.imgUpload.text1"/></span>
							</div>
							<p class="txt_type1">
								<spring:message code="common.imgUpload.text2"/>
								<p class="information" ng-show="dmsCatalogimg.img1 == null">필수 입력 항목 입니다.</p>
							</p>
							<div class="preview" ng-if="dmsCatalogimg.img1 != '' && dmsCatalogimg.img1 != null">
								<img ng-src="dmsCatalogimg.img1" onError="this.src='/resources/img/bg/bg_temp_img.gif';" img-domain alt="" name="img1"/>
								<button type="button" class="btn_file_del" ng-click="ctrl.deleteImage('img1')">파일 삭제</button>
							</div>
						</td>
					</tr>
					<tr>
						<th>
							MOBILE 이미지 <i><spring:message code="c.input.required" /></i>
						</th>
						<td colspan="3">
							<div class="input_file">
								<input type="file" id="catalogImg" name="fileModel" image-key="dmsCatalogimg.img2" file-upload/>
								<input type="hidden" id="img2Path" ng-model="dmsCatalogimg.img2" placeholder="" required />
								<button type="button" class="btn_type2 btn_addFile">
									<b><spring:message code="c.common.file.search"/></b>
								</button>
								<span><spring:message code="common.imgUpload.text1"/></span>
							</div>
							<p class="txt_type1">
								<spring:message code="common.imgUpload.text2"/>
								<p class="information" ng-show="dmsCatalogimg.img2 == null">필수 입력 항목 입니다.</p>
							</p>
							<div class="preview" ng-if="dmsCatalogimg.img2 != '' && dmsCatalogimg.img2 != null">
								<img ng-src="dmsCatalogimg.img2" onError="this.src='/resources/img/bg/bg_temp_img.gif';" img-domain alt="" name="img2"/>
								<button type="button" class="btn_file_del" ng-click="ctrl.deleteImage('img2')">파일 삭제</button>
							</div>
						</td>
					</tr>
					<tr>
						<th>
							이미지타이틀 <i><spring:message code="c.input.required" /></i>
						</th>
						<td colspan="3">
							<input type="text" ng-model="dmsCatalogimg.name" v-key="dmsCatalogimg.name" style="width:170px;" required />
						</td>
					</tr>									
					<tr>
						<th>
							<spring:message code="c.dms.catalogimg.sortNo" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td>
							<input type="number" ng-model="dmsCatalogimg.sortNo" v-key="dmsCatalogimg.sortNo" style="width:130px;" required />
						</td>
						<th>
							<spring:message code="dmsCatalogimg.displayYn" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td ng-init="dmsCatalogimg.displayYn='Y'">
							<input type="radio" ng-model="dmsCatalogimg.displayYn" value="Y" /><label for="radio1"><spring:message code="c.input.radio.displayY" /></label>
							<input type="radio" ng-model="dmsCatalogimg.displayYn" value="N" /><label for="radio2"><spring:message code="c.input.radio.displayN" /></label>
						</td>
					</tr>					
				</tbody>
			</table>
		</div>
	</form>
	
	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /></b>
		</button>
		<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.insertCatalogImg()">
			<b><spring:message code="c.common.save" /></b>		
		</button>
	</div>
		
</div>

<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>