<%--
	화면명 : 상품관리 > 사은품 관리 상세
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.present.list.js"></script>

<div class="wrap_popup" ng-app="presentApp" ng-controller="pms_presentRegPopApp_controller as ctrl">
<!-- 사은품 프로모션 상세정보 START -->
	<form name="form">
		<h2 class="sub_title1"><spring:message code="c.pms.present.pop.title" /></h1>
		<div class="box_type1">
			<h3 class="sub_title2"><spring:message code="c.pms.present.pop.info" /></h3>
			<table class="tb_type1">
				<colgroup>
					<col class="col_142" />
					<col class="col_auto" />
					<col class="col_142" />
					<col class="col_auto" />
				</colgroup>
				<tbody>
					<tr>
						<th><spring:message code="c.pms.present.name" /><i/></th>
						<td>
							<input type="text" name="name" ng-model="pmsProduct.name" v-key="pmsProduct.name" style="width:80%;" />
						</td>
						<th><spring:message code="c.pms.present.pop.detail" /><i/></th>
						<td>
							<input type="text" name="explain" ng-model="pmsProduct.detail" style="width:80%;" />
						<!--	<p class="information" ng-show="pmsProduct.detail == undefined || pmsProduct.detail==''"><spring:message code="common.require.data"/></p> -->
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.pms.present.pop.erpId" /> / <spring:message code="c.pms.present.pop.barcode" /></th>
						<td>
							<input type="text" ng-model="pmsProduct.erpProductId" style="width:120px;" readonly="readonly" v-key="required"/> / 
							<input type="text" ng-model="pmsProduct.pmsSaleproduct.erpSaleproductId" style="width:120px;" />
							<button type="button" class="btn_type2" ng-click="ctrl.erpProductSearch()">
								<b><spring:message code="c.search.btn.search" /></b>
							</button>
						</td>
						<th><spring:message code="c.pms.present.pop.use.yn" /><i/></th>
						<td>
							<radio-yn ng-model="pmsProduct.useYn" labels="사용,미사용" init-val="Y" ></radio-yn>
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.pms.present.pop.stock.qty" /></th>
						<td colspan="3">
							<input type="text" ng-model="pmsProduct.pmsSaleproduct.realStockQty" style="width:20%;"/>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="box_type1 marginT2">
			<h3 class="sub_title2"><spring:message code="c.pms.present.pop.img.info" /></h3>
			<table class="tb_type1">
				<colgroup>
					<col class="col_142" />
					<col class="col_auto" />
				</colgroup>
				<tbody>			
					<tr>
						<th><spring:message code="c.pms.present.pop.img" /></th>
						<td colspan="3">
							<div class="input_file">
								<input type="file" callback="uploadImageCallback" file-upload/>
								<button type="button" class="btn_type2 btn_addFile">
									<b><spring:message code="c.common.file.search" /></b>
								</button>
								<span>(이미지 사이즈 : 000*000)</span>
							</div>

							<p class="txt_type1">
								* jpg, png 00MB 이하의 파일만 업로드가 가능합니다.
							</p>
							<div class="preview" ng-show="pmsProduct.pmsProductimg.img != '' && pmsProduct.pmsProductimg.img != null">
								<img ng-src="pmsProduct.pmsProductimg.img"img-domain  onError="this.src='/resources/img/bg/bg_temp_img.gif';" alt="" />
								<button type="button" class="btn_file_del" ng-click="ctrl.deleteImage()">파일 삭제</button>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</form>
	<div class="btn_alignC marginT3">
		<button type="button" ng-click="ctrl.closePopup()" class="btn_type3 btn_type3_gray">
			<b><spring:message code="c.common.close" /></b>
		</button>
		<button type="button" ng-click="ctrl.savePresent()" class="btn_type3 btn_type3_purple" >
			<b><spring:message code="c.common.save" /></b>
		</button>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>