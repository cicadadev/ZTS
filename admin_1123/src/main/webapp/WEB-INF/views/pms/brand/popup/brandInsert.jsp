<%--
	화면명 : 상품 관리 > 브랜드 관리 > 브랜드 등록 팝업
	작성자 : eddie
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.brand.manager.js"></script>
	
<div class="wrap_popup" ng-app="brandManagerApp" ng-controller="insertPopup_controller as ctrl">
	<form name="form">
		<h1 class="sub_title1"><spring:message code="pms.brand.btn.register" /></h1>
	
		<div class="box_type1" ng-init="ctrl.registerInit()">
			<table class="tb_type1">
				<colgroup>
					<col width="13%">
					<col width="37%">
					<col width="13%">
					<col width="*">
				</colgroup>
				<tbody>
					<tr>
						<th>
							<spring:message code="c.pmsBrand.brandId" />
						</th>
						<td ng-bind="pmsBrand.brandId"></td>
						<th>
							<spring:message code="pmsBrand.name" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td>
							<input type="text" v-key="pmsBrand.name" ng-model="pmsBrand.name" style="width:80%;" required />
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="pmsBrand.displayYn" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td>
							<input type="radio" ng-model="pmsBrand.displayYn" value="Y"/><label for="radio1"><spring:message code="c.input.radio.displayY" /></label>
							<input type="radio" ng-model="pmsBrand.displayYn" value="N" /><label for="radio2"><spring:message code="c.input.radio.displayN" /></label>
						</td>	
						<th>
							<spring:message code="pmsBrand.sortNo" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td>
							<input type="text" v-key="pmsBrand.sortNo" ng-model="pmsBrand.sortNo" style="width:80%;" required />
						</td>	
					</tr>
					<tr>
						<th>
							<spring:message code="pmsBrand.erpBrandId" /> <%-- <i><spring:message code="c.input.required" /></i> --%>
						</th>
						<td>
							<input type="text" ng-model="pmsBrand.erpBrandId" v-key="pmsBrand.erpBrandId" ng-pattern="/^[a-zA-z0-9]+$/i" style="width:30%;" />
						</td>					
						<th>
							<spring:message code="c.pmsBrand.template" />
						</th>
						<td>
							<select style="min-width:200px;" ng-model="pmsBrand.templateId" v-key="required" 
								ng-options="template.templateId as template.name for template in templateList">
								<option value="">선택하세요</option>
							</select>
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.pmsBrand.logoImg" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td colspan="3">
							<div class="input_file">
								<input type="file" id="logoImg" name="fileModel" image-key="pmsBrand.logoImg" file-upload />
								<input type="text" id="logoImgPath" placeholder="" required />
								<button type="button" class="btn_type2 btn_addFile">
									<b><spring:message code="c.common.file.search" /></b>
								</button>
								<span><spring:message code="common.imgUpload.text1"/></span>
								<p class="information" ng-show="pmsBrand.logoImg == null">필수 입력 항목 입니다.</p>
							</div>
	
							<p class="txt_type1">
								<spring:message code="common.imgUpload.text2"/>
							</p>
	
							<div class="preview" ng-show="pmsBrand.logoImg != '' && pmsBrand.logoImg != null">
								<img ng-src="pmsBrand.logoImg" onError="this.src='/resources/img/bg/bg_temp_img.gif';" img-domain name="logoImg" alt="" />
								<button type="button" class="btn_file_del" ng-click="ctrl.deletelogoImg('logoImg')"><spring:message code="c.sps.event.deleteFile" /></button>
							</div>
						</td>
					</tr>					
				</tbody>
			</table>
		</div>
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b><spring:message code="c.common.close" /></b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.insertBrand()">
				<b><spring:message code="c.common.save" /></b>
			</button>
		</div>
		
	</form>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>