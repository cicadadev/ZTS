<%--
	화면명 : 스타일샵 -> 등록 / 상세
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.styleShop.manager.js"></script>

<div class="wrap_popup" ng-app="styleShopApp" ng-cloak ng-controller="pms_styleShopPopApp_controller as ctrl">
	<h1 class="sub_title1" ng-show="pmsStyleproduct.styleProductNo == '' || pmsStyleproduct.styleProductNo == null">스타일샵 상품 등록</h1>
	<h1 class="sub_title1" ng-show="pmsStyleproduct.styleProductNo != '' && pmsStyleproduct.styleProductNo != null">스타일샵 상품 상세</h1>
<form name="form">
	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col width="15%" />
				<col width="40%" />
				<col width="15%" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr>
					<th>스타일번호</th>
					<td>
						{{pmsStyleproduct.styleProductNo}}
					</td>
					<th>스타일 분류<i><spring:message code="c.input.required"/><!-- 필수입력 --></i></th>
					<td>
						<select ng-model="pmsStyleproduct.styleProductItemCd" select-code="STYLE_PRODUCT_ITEM_CD" style="width:150px;" v-key="required">
							<option value="">선택하세요</option>
							<option ng-selected="pmsStyleproduct.styleProductItemCd" value="{{ pmsStyleproduct.styleProductItemCd }}"></option>
						</select>
					</td>
				</tr>
				
				
				<tr>
					<th>
						<spring:message code="c.dmsExhibitproduct.mainProduct.productNoNm"/><!-- 상품번호 / 명 --> <i><spring:message code="c.input.required"/><!-- 필수입력 --></i>
					</th>
					<td id="product">
						<input data-ng-model="pmsStyleproduct.productId" id="productId" type="text" name="productId" placeholder="" style="width:20%;" readonly disabled />
						<input data-ng-model="pmsStyleproduct.productName" id="name" type="text" name="name" placeholder="" style="width:40%;" readonly disabled/>
						<button type="button" class="btn_type2" data-ng-click="ctrl.searchProduct()"><b><spring:message code="c.search.btn.search"/><!-- 검색 --></b></button>
						<button type="button" class="btn_eraser" ng-class="{'btn_eraser_disabled' : pmsStyleproduct.productId == null || pmsStyleproduct.productId == ''}" ng-click="ctrl.delText()"></button>
						<p class="information" ng-show="pmsStyleproduct.styleProductItemCd!='STYLE_PRODUCT_ITEM_CD.DECO' && !pmsStyleproduct.productId"><spring:message code="c.common.invalid.content"/> <!-- 필수 입력 항목 입니다. --></p>
					</td>
					<th>사용여부<i><spring:message code="c.input.required"/><!-- 필수입력 --></i></th>
					<td ng-init="pmsStyleproduct.useYn='Y'">
						<input type="radio" ng-model="pmsStyleproduct.useYn" value="Y"/><label>예</label>
						<input type="radio" ng-model="pmsStyleproduct.useYn" value="N"/><label>아니오</label>
					</td>
				</tr>
				
				<tr>
					<th>스타일상품컬러<i><spring:message code="c.input.required"/><!-- 필수입력 --></i></th>
					<td colspan="3">
						<select ng-model="pmsStyleproduct.styleProductColorCd" select-code="STYLE_PRODUCT_COLOR_CD" style="width:150px;" v-key="required">
							<option value="">선택하세요</option>
							<option ng-selected="pmsStyleproduct.styleProductColorCd" value="{{ pmsStyleproduct.styleProductColorCd }}"></option>
						</select>
					</td>
				</tr>
				
				<tr>
					<th>이미지<i><spring:message code="c.input.required"/><!-- 필수입력 --></i></th>
					<td colspan="3">
						<div class="input_file">
							<input type="file" callback="uploadDefaultImageCallback" file-upload>
							<input type="hidden" id="img1Path" ng-model="pmsStyleproduct.img" placeholder="" required />
							<button type="button" class="btn_type2 btn_addFile">
								<b><spring:message code="c.common.file.search"/><!-- 찾아보기 --></b>
							</button>
							<span><spring:message code="c.dmsExhibit.imgSize"/><!-- (이미지 사이즈 : 000*000) --></span>
						</div>

						<p class="txt_type1">
							<spring:message code="c.dmsExhibit.imgType"/><!-- * jpg, png 00MB 이하의 파일만 업로드가 가능합니다. -->
						</p>
						<p class="information" ng-show="pmsStyleproduct.img == null || pmsStyleproduct.img == ''">필수 입력 항목 입니다.</p>
						<div class="preview" ng-show="pmsStyleproduct.img != '' && pmsStyleproduct.img != null">
							<img ng-src="pmsStyleproduct.img" img-domain onError="this.src='/resources/img/bg/bg_temp_img.gif';" alt="">
							<button type="button" class="btn_file_del" ng-click="ctrl.deleteImage()">파일 삭제</button>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</form>
	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray " data-ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /></b>
		</button>
		<button type="button" class="btn_type3 btn_type3_purple" data-ng-click="ctrl.saveStyleShopProduct()">
			<b><spring:message code="c.common.save" /></b>
		</button>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>