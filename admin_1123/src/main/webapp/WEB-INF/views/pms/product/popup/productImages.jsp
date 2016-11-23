<%--
	화면명 : 상품 관리 > 상품 관리 > 상품 상세 팝업 > 이미지 탭
	작성자 : eddie
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.product.detail.js"></script>

<div class="wrap_popup"  data-ng-app="productDetailApp" data-ng-controller="productImagesController as ctrl">
	<h2 class="sub_title1">상품상세</h2>
	<ul class="tab_type2">
		<li>
			<button type="button"  ng-click="ctrl.changeTab()">기본정보</button>
		</li>
		<li class="on" oncllick="changeTab()">
			<button type="button">이미지 정보</button>
		</li>
	</ul>
	
	<div class="img_upInfo">
		<strong>* 이미지 등록 시 유의사항</strong>
		<ul>
<!-- 			<li>
				- 이미지 사이즈는 000*000 해상도 이상
			</li> -->
			<li>
				- 이미지 용량은 개당 최대 5M로 제한
			</li>
			<li>
				- 이미지 포맷은 JPG 파일만 등록 가능
			</li>
		</ul>
		
		<span>
		<input type="file" size="30" id="file" style="display:none;" callback="bulkImageUploadCallback" zip-file-upload="{{pmsProduct.productId}}"/>
		<button type="button" class="btn_type1" onclick="document.getElementById('file').click();">
			<b>이미지 일괄 등록</b>
		</button>
	</div>
	
	<div  class="box_type1 marginT2">
		<h3 class="sub_title2">이미지 정보</h3>
		<table class="tb_type1 tb_imgUp">
			<colgroup>
				<col width="13%">
				<col width="*">
			</colgroup>
			<tbody>
				<tr>
					<th>
						대표이미지 <i>필수입력</i>
					</th>
					<td>
						<p class="txt_type1"> * jpg 5MB 이하의 파일만 업로드가 가능합니다. </p>
						<ul class="img_list">
							<li>
								<div class="input_file">
									<input type="file" callback="uploadDefaultImageCallback" file-upload>
									<button type="button" style="width:90px" class="btn_type2 btn_addFile">
										<b>찾아보기</b>
									</button>
									<!-- <button type="button" class="btn_file_del" ng-click="ctrl.deleteDefaultImage()">파일 삭제</button> -->
								</div>
								<div class="preview" ng-if="defaultImage.img">
									<img ng-src="defaultImage.img" img-domain width="180px" style="max-width:180px"  onError="this.src='/resources/img/bg/bg_temp_img.gif';" alt="">
								</div>
								<p class="information" ng-if="!defaultImage.img">필수 항목 입니다.</p>
							</li>
						</ul>
					</td>
				</tr>
				<tr>
					<th>
						매장컷이미지
					</th>
					<td>
						<p class="txt_type1"> * jpg 5MB 이하의 파일만 업로드가 가능합니다. </p>
						<ul class="img_list">
							<li>
								<div class="input_file">
									<input type="file" callback="uploadOffshopImageCallback" file-upload>
									<button type="button" style="width:90px" class="btn_type2 btn_addFile">
										<b>찾아보기</b>
									</button>
									<button type="button" class="btn_file_del" ng-click="ctrl.deleteOffshopImage()">파일 삭제</button>
								</div>
								<div class="preview" ng-if="pmsProduct.offshopImg">
									<img ng-src="pmsProduct.offshopImg" width="180px" style="max-width:180px" img-domain onError="this.src='/resources/img/bg/bg_temp_img.gif';" alt="">
								</div>
							</li>
						</ul>
					</td>
				</tr>				
				<tr class="column2">
					<th>
						일반이미지 <i>필수입력</i>
					</th>
					<td>
						<p class="txt_type1"> * jpg 5MB 이하의 파일만 업로드가 가능합니다. </p>
						<ul class="img_list">
							<li ng-repeat="(pIndex, img) in pmsProduct.pmsProductimgs" ng-show="img.crudType!='D' && img.imgNo!='0'"><!-- ng-if="img.imgNo!=0" -->
								<div class="input_file">
									<input type="file" callback="uploadImageCallback" index="{{pIndex}}" file-upload>
									<button type="button" style="width:90px" class="btn_type2 btn_addFile">
										<b>찾아보기</b>
									</button>
									<button type="button" class="btn_file_del" ng-click="ctrl.deleteImage(pIndex)">파일 삭제</button>
								</div>
								<div class="preview" ng-if="img.img">
									<img ng-src="img.img" img-domain width="180px" style="max-width:180px" onError="this.src='/resources/img/bg/bg_temp_img.gif';" alt="">
									<em>{{pIndex}}</em>
								</div>
							</li>
							<li>
								<div class="input_file">
									<input type="file" callback="uploadImageCallback" index="new" file-upload>
									<button type="button" style="width:90px" class="btn_type2 btn_addFile">
										<b>찾아보기</b>
									</button>
								</div>
								<div class="preview">
									<img src="" alt="">
									<em></em>
								</div>
							</li>							
						</ul>
					</td>
				</tr>
			</tbody>
		</table>			
	</div>
	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /></b>
		</button>
		<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.save()">
			<b><spring:message code="c.common.save" /></b>
		</button>
	</div>		
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>