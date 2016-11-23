<%--
	화면명 : 기획전 관리 > 구분타이틀 정보 팝업 > 구분타이틀 등록 팝업
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/dms.app.exhibit.manager.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="wrap_popup" ng-app="exhibitApp" data-ng-controller="dms_exhibitDivTitleInsertPopApp_controller as ctrl" data-ng-init="ctrl.init()">
		<h2 class="sub_title1"><spring:message code="c.dmsExhibitgroup.regTitle"/> <!-- 타이틀 등록 --></h2>
		<div class="box_type1">
			
			<form name="divTitleForm">
			<table class="tb_type1">
				<colgroup>
					<col width="18%" />
					<col width="32%" />
					<col width="16%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>
							<spring:message code="c.dmsExhibit.divTitleNo"/><!-- 구분타이틀 번호 -->
						</th>
						<td>
							{{dmsExhibitgroup.groupNo}}
						</td>
						<th>
							<spring:message code="c.dmsExhibitgroup.groupType"/><!-- 타이틀 유형 --><i><spring:message code="c.input.required"/> <!-- 필수입력 --></i>
						</th>
						<td ng-init="dmsExhibitgroup.groupTypeCd='GROUP_TYPE_CD.IMG'">
							<radio-list ng-model="dmsExhibitgroup.groupTypeCd" code-group="GROUP_TYPE_CD" />
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.dmsExhibitgroup.name" /><!-- 타이틀 명 -->
							<i ng-show="dmsExhibitgroup.groupTypeCd == 'GROUP_TYPE_CD.TEXT'">
								<spring:message code="c.input.required"/> <!-- 필수입력 -->
							</i>
						</th>
						<td>
							<input type="text" ng-model="dmsExhibitgroup.name" name="name" style="width:38%;"  ng-required="dmsExhibitgroup.groupTypeCd == 'GROUP_TYPE_CD.TEXT'" />
							<p class="information" ng-show="dmsExhibitgroup.groupTypeCd == 'GROUP_TYPE_CD.TEXT' && (dmsExhibitgroup.name == '' || dmsExhibitgroup.name ==undefined)"> 
								<spring:message code="c.common.invalid.content"/> <!-- 필수 입력 항목 입니다. -->
 							</p>
						</td>
						<th>
							<spring:message code="c.dmsExhibitgroup.sortNo" /><!-- 우선 순위 -->
						</th>
						<td>
							<input type="number" ng-model="dmsExhibitgroup.sortNo" name="sortNo" style="width:38%;" />
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.dmsExhibitgroup.displayYn" /><!-- 전시 여부 --> <i><spring:message code="c.input.required"/> <!-- 필수입력 --></i>
						</th>
						<td ng-init="dmsExhibitgroup.displayYn != null ? dmsExhibitgroup.displayYn : dmsExhibitgroup.displayYn='Y'" colspan="3">
							<input type="radio" value="Y" ng-model="dmsExhibitgroup.displayYn" id="radio3"/>
							<label for="radio3"><spring:message code="c.dmsExhibitgroup.displayY" /><!-- 전시 --></label>
							<input type="radio" value="N" ng-model="dmsExhibitgroup.displayYn" id="radio4"/>
							<label for="radio4"><spring:message code="c.dmsExhibitgroup.displayN" /><!-- 미전시 --></label>
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.dmsExhibitgroup.pcUrlLink.useYn"/><!-- PC URL 링크 사용여부 -->
						</th>
						
						
						<td ng-init="dmsExhibitgroup.pcUrlLinkUseYn='N'">
							<input type="radio" ng-model="dmsExhibitgroup.pcUrlLinkUseYn" value="Y"  />
							<label for="radio3"><spring:message code="c.input.radio.useY"/> <!-- 사용 --></label>
							<input type="text" v-key="dmsExhibitgroup.url1" url-input ng-model="dmsExhibitgroup.url1"  ng-disabled="dmsExhibitgroup.pcUrlLinkUseYn=='N'" name="pcUrllink" style="width:30%;" ng-required="dmsExhibitgroup.pcUrlLinkUseYn == 'Y'"/>
							<input type="radio" ng-model="dmsExhibitgroup.pcUrlLinkUseYn" value="N" />
							<label for="radio4"><spring:message code="c.input.radio.useN"/> <!-- 미사용 --></label>
						</td>
						<th>
							<spring:message code="c.dmsExhibitgroup.productDisplayType1Cd"/><!-- PC 상품 전시개수 --><!-- 타이틀 명 --><i><spring:message code="c.input.required"/> <!-- 필수입력 --></i>
						</th>
						<td ng-init="dmsExhibitgroup.productDisplayType1Cd='PRODUCT_DISPLAY_TYPE1_CD.5'">
							<radio-list ng-model="dmsExhibitgroup.productDisplayType1Cd" code-group="PRODUCT_DISPLAY_TYPE1_CD" />
						</td>
					
					</tr>
					<tr>
						<th>
							<spring:message code="c.dmsExhibitgroup.mbUrlLink.useYn"/><!-- Mobile URL 링크 사용여부 -->
						</th>
						<td ng-init="dmsExhibitgroup.mbUrlLinkUseYn='N'">
							<input type="radio" ng-model="dmsExhibitgroup.mbUrlLinkUseYn" value="Y"  />
							<label for="radio3"><spring:message code="c.input.radio.useY"/> <!-- 사용 --></label>
							<input type="text" v-key="dmsExhibitgroup.url2" url-input ng-model="dmsExhibitgroup.url2"  ng-disabled="dmsExhibitgroup.mbUrlLinkUseYn=='N'" name="mbUrllink" style="width:30%;" ng-required="dmsExhibitgroup.mbUrlLinkUseYn == 'Y'" />
							<input type="radio" ng-model="dmsExhibitgroup.mbUrlLinkUseYn" value="N"  ng-click="dmsExhibitgroup.url2=''"/>
							<label for="radio4"><spring:message code="c.input.radio.useN"/> <!-- 미사용 --></label>
						</td>
						<th>
							<spring:message code="c.dmsExhibitgroup.productDisplayType2Cd"/><!-- MOBILE 상품 전시개수 --><!-- 타이틀 명 --><i><spring:message code="c.input.required"/> <!-- 필수입력 --></i>
						</th>
						<td ng-init="dmsExhibitgroup.productDisplayType2Cd='PRODUCT_DISPLAY_TYPE2_CD.2'"> 
							<radio-list ng-model="dmsExhibitgroup.productDisplayType2Cd" code-group="PRODUCT_DISPLAY_TYPE2_CD" />
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.dmsExhibitgroup.img" /><!-- 구분타이틀 이미지 -->
							<i ng-show="dmsExhibitgroup.groupTypeCd == 'GROUP_TYPE_CD.IMG'">
								<spring:message code="c.input.required"/> <!-- 필수입력 -->
							</i>
						</th>
						<td colspan="3">
							<div class="input_file">
								<input type="file" callback="uploadImageCallback" file-upload>
								<button type="button" class="btn_type2 btn_addFile">
									<b><spring:message code="c.common.file.search"/><!-- 찾아보기 --></b>
								</button>
								<span><spring:message code="c.dmsExhibit.imgSize"/><!-- (이미지 사이즈 : 000*000) --></span>
							</div>

							<p class="txt_type1">
								<spring:message code="c.dmsExhibit.imgType"/><!-- * jpg, png 00MB 이하의 파일만 업로드가 가능합니다. -->
							</p>

							<div class="preview" ng-show="dmsExhibitgroup.img != '' && dmsExhibitgroup.img != null">
								<img img-domain ng-src="dmsExhibitgroup.img" onError="this.src='/resources/img/bg/bg_temp_img.gif';" alt="">
								<button type="button" class="btn_file_del" ng-click="ctrl.deleteImage($index)">파일 삭제</button>
							</div>
							<p class="information" ng-show="dmsExhibitgroup.groupTypeCd == 'GROUP_TYPE_CD.IMG' && (dmsExhibitgroup.img == '' || dmsExhibitgroup.img == undefined)">
								<spring:message code="c.common.invalid.content"/> <!-- 필수 입력 항목 입니다. -->
 							</p>
						</td>
					</tr>
				</tbody>
			</table>
		</form>
	</div>

<div class="btn_alignC marginT3">
	<button type="button" class="btn_type3 btn_type3_gray" data-ng-click="ctrl.close()">
		<b><spring:message code="c.common.close"/> <!-- 취소 --></b>
	</button>
	<button type="button" class="btn_type3 btn_type3_purple" data-ng-click="ctrl.insertDivTitle()">
		<b><spring:message code="c.common.save"/><!-- 저장 --></b>
	</button>
</div>
	
		
</div>

<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>