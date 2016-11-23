<%--
	화면명 : 기획전 관리 > 기획전 상세 > 대표 상품 상세
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/dms.app.exhibit.manager.js"></script>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%> 

<div class="wrap_popup"  ng-app="exhibitApp" data-ng-controller="dms_exhibitMainProductDetailPopApp_controller as ctrl" ng-init="ctrl.init()">
		<h1 class="sub_title1"><spring:message code="c.dmsExhibitproduct.mainProduct.detail" /><!-- 대표상품 상세 --></h1>
		
		<div class="box_type1">
		<form name="mainProductInsertForm">
			<table class="tb_type1">
				<colgroup>
					<col width="18%" />
					<col width="34%" />
					<col width="13%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							<spring:message code="c.dmsExhibitproduct.mainProduct.productNoNm"/><!-- 상품번호 / 명 --> <i><spring:message code="c.input.required"/><!-- 필수입력 --></i>
						</th>
						<td colspan="3">
							<input data-ng-model="dmsExhibitMainProduct.productId" id="productId" type="text" name="productId" placeholder="" style="width:10%;" readonly disabled required/>
							<input data-ng-model="dmsExhibitMainProduct.productName" id="name" type="text" name="name" placeholder="" style="width:30%;" readonly disabled/>
<%-- 							<button type="button" class="btn_type2" data-ng-click="ctrl.searchProduct()"><b><spring:message code="c.search.btn.search"/><!-- 검색 --></b></button> --%>
<!-- 							<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : dmsExhibitMainProduct.productId == null || dmsExhibitMainProduct.productId == ''}" ng-click="ctrl.delText()"></button> -->
<!-- 							<p class="information" ng-show="mainProductInsertForm.productId.$error.required">필수 입력 항목 입니다.</p> -->
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.dmsExhibitproduct.mainProduct.name"/><!-- 대표상품 명 --> <i><spring:message code="c.input.required"/><!-- 필수입력 --></i>
						</th>
						<td colspan="3">
							<input data-ng-model="dmsExhibitMainProduct.name" id="mainProductName" type="text" name="mainProductName" placeholder="" style="width:50%;" v-key="dmsExhibitMainProduct.name" required/>
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.dmsExhibitgroup.sortNo"/><!-- 우선 순위 --> <i><spring:message code="c.input.required"/><!-- 필수입력 --></i>
						</th>
						<td colspan="3">
							<input data-ng-model="dmsExhibitMainProduct.sortNo" id="sortNo" type="number" name="sortNo" placeholder="" style="width:50%;" v-key="dmsExhibitMainProduct.sortNo" required/>
						</td>
					</tr>
					
					<tr class="column2">
						<th>
							<spring:message code="c.dmsExhibitproduct.mainProduct.img"/><!-- 대표 상품 이미지 -->
						</th>
						<td colspan="3">
							<div class="input_file">
								<input type="file" callback="uploadDefaultImageCallback" file-upload>
								<button type="button" class="btn_type2 btn_addFile">
									<b><spring:message code="c.common.file.search"/><!-- 찾아보기 --></b>
								</button>
								<span><spring:message code="c.dmsExhibit.imgSize"/><!-- (이미지 사이즈 : 000*000) --></span>
							</div>

							<p class="txt_type1">
								<spring:message code="c.dmsExhibit.imgType"/><!-- * jpg, png 00MB 이하의 파일만 업로드가 가능합니다. -->
							</p>

							<div class="preview" ng-show="dmsExhibitMainProduct.img != '' && dmsExhibitMainProduct.img != null">
								<img img-domain ng-src="dmsExhibitMainProduct.img" onError="this.src='/resources/img/bg/bg_temp_img.gif';" alt="">
								<button type="button" class="btn_file_del" ng-click="ctrl.deleteImage()">파일 삭제</button>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>
	
		<div class="btn_alignC" style="margin-top:20px;">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b><spring:message code="c.common.close" /><!-- 닫기 --></b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.saveMainProduct()">
				<b><spring:message code="c.common.save" /><!-- 저장 --></b>
			</button>
		</div>
	
</div>

<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>