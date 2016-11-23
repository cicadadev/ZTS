<%--
	화면명 : 상품 관리 > 브랜드 관리 > 브랜드 상세 팝업 > 브랜드 정보 탭
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.brand.detail.js"></script>

<div class="wrap_popup" ng-app="brandDetailApp" ng-controller="pms_brandBasicInfoController as ctrl">
	<h2 class="sub_title1"><spring:message code="c.pmsBrand.detail" /></h2>	
	
	<ul class="tab_type2">
		<li class="on" >
			<button type="button"><spring:message code="c.pmsBrand.basicInfo" /></button>
		</li>
		<li oncllick="changeTab('shop')">
			<button type="button" ng-click="ctrl.changeTab('shop')"><spring:message code="c.pmsBrand.shopInfo" /></button>
		</li>
	</ul>

	<form name="form">
		<div class="box_type1" ng-init="ctrl.getBrand()">
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
							<spring:message code="c.pmsBrand.brandId" />
						</th>
						<td ng-bind="pmsBrand.brandId"></td>
						<th>
							<spring:message code="pmsBrand.name" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td>
							<input type="text" ng-model="pmsBrand.name" v-key="pmsBrand.name" style="width:80%;" required />
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="c.pmsBrand.displayYn" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td>
							<input type="radio" ng-model="pmsBrand.displayYn" value="Y"/><label for="radio1"><spring:message code="c.input.radio.displayY" /></label>
							<input type="radio" ng-model="pmsBrand.displayYn" value="N" /><label for="radio2"><spring:message code="c.input.radio.displayN" /></label>
						</td>					
						<th>
							<spring:message code="c.pmsBrand.sortNo" /> <i><spring:message code="c.input.required" /></i>
						</th>
						<td>
							<input type="text" ng-model="pmsBrand.sortNo" v-key="pmsBrand.sortNo" style="width:30%;" required />
						</td>
					</tr>
					<tr>
						<th>
							<spring:message code="pmsBrand.erpBrandId" /> <%-- <i><spring:message code="c.input.required" /></i> --%>
						</th>
						<td>
							<input type="text" ng-model="pmsBrand.erpBrandId" v-key="pmsBrand.erpBrandId" ng-pattern="/^[a-zA-z0-9]+$/i" style="width:50%;" />
						</td>	
						<th>
							<spring:message code="c.pmsBrand.template" />
						</th>
						<td>
							<select style="min-width:300px;" ng-model="pmsBrand.templateId"  v-key="required"
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
								<input type="text" id="logoImgPath" ng-model="pmsBrand.logoImg" placeholder="" required />
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
	</form>
	
	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /></b>
		</button>
		<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.updateBrandInfo()">
			<b><spring:message code="c.common.save" /></b>
		</button>
	</div>

</div>