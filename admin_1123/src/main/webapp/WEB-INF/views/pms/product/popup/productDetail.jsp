<%--
	화면명 : 상품 관리 > 상품 관리 > 상품 상세 팝업 > 기본정보 탭
	작성자 : eddie
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/pms.app.product.detail.js"></script>

<style type="text/css">
	table {table-layout:fixed;}
	table button + .gridbox {margin-top:5px; padding:0;}
</style>

<div class="wrap_popup"  data-ng-app="productDetailApp" data-ng-controller="productDetailController as ctrl">

	
	<h2 class="sub_title1">상품상세</h2>
	
	<ul class="tab_type2">
		<li class="on">
			<button type="button">기본정보</button>
		</li>
		<li oncllick="changeTab()">
			<button type="button" ng-click="ctrl.changeTab()">이미지 정보</button>
		</li>
	</ul>	
	<form name="form">
	<div  class="box_type1 marginT2">
		<h3 class="sub_title2">상품 기본 정보</h3>
		<table class="tb_type1">
			<colgroup>
				<col width="14%">
				<col width="34%">
				<col width="14%">
				<col width="*">
			</colgroup>
			<tbody>
				<tr>
					<th>상품번호<i>필수입력</i></th>
					<td>{{pmsProduct.productId}}</td>
					<th>
						상품유형 <i>필수입력</i>
					</th>
					<td>{{isSetProduct?'세트상품':'일반상품'}}</td>			
				</tr>			
				<tr>
					<th>상품명<i>필수입력</i></th>
					<td><input type="text" ng-model="pmsProduct.name" v-key="pmsProduct.name" style="width:98%;"><p style="color:#3575C4">※ 최저가,노마진,특가, 특수기호 등의 문구 사용은 불가합니다</p></td>
					<th>상품강조문구</th>
					<td><input type="text" ng-model="pmsProduct.adCopy" v-key="pmsProduct.adCopy" style="width:98%;"><p style="color:#3575C4">※ % + & 외의 특수기호 사용은 불가합니다</p></td>
				</tr>
				<tr>
					<th>
						판매상태 <i>필수입력</i>
					</th>
					<td>{{isNew?'신규등록':pmsProduct.saleStateName}}</td>
					<th>판매기간</th>
					<td><input type="text" style="width:160px;" ng-model="pmsProduct.saleStartDt" datetime-picker  period-start />-
						<input type="text" style="width:160px;" ng-model="pmsProduct.saleEndDt" datetime-picker  period-end />
						<p class="information" ng-show="pmsProduct.saleStartDt==null || pmsProduct.saleEndDt==null">필수 입력 항목 입니다.</p>
					</td>					
				</tr>
				<tr>
					<th>표준카테고리 <i>필수입력</i></th>
					<td colspan="3">
						<span>*표준카테고리는 상품별 한개만 선택할 수 있습니다.</span><br/>
						<input type="text" ng-model="pmsProduct.pmsCategory.depthFullName" ng-readonly="true" v-key="required" style="width:400px;">
						<button type="button" class="btn_type2" ng-click="ctrl.categorySearch('pms')"><b>표준카테고리 검색</b></button> 
					</td>
				</tr>
				<tr>
					<th>
						전시카테고리 <i>필수입력</i>
					</th>
					<td colspan="3" style="text-align:right">
						<span style="text-align:left">*전시카테고리는 상품별 한 개 이상을 선택해야 합니다.</span>
						<button type="button" class="btn_type2" ng-click="ctrl.categorySearch('dms')"><b>전시카테고리 추가</b></button>
						<button type="button" class="btn_type2" ng-click="dmsCategoryGrid.deleteRow();"><b>삭제</b></button>
						<div  class="gridbox gridbox200" style="text-align:center">
		   				<div class="grid" data-ui-grid="grid1"   
							data-ui-grid-move-columns 
							data-ui-grid-resize-columns 
							data-ui-grid-auto-resize 
							data-ui-grid-selection 
							data-ui-grid-row-edit
							data-ui-grid-cell-nav
							data-ui-grid-exporter
							data-ui-grid-edit 
							data-ui-grid-validate></div>								
						</div>							
						<p style="text-align:left" class="information" ng-show="grid1.data.length==0">필수 입력 항목 입니다.</p>
					</td>
				</tr>
				<tr>
					<th>담당MD</th>
					<td>{{pmsProduct.ccsUserMd.name}}</td>
					<th>브랜드 코드/명 <i>필수입력</i></th>
					<td><input type="text" ng-model="pmsProduct.brandId" ng-readonly="true" v-key="required">
						<input type="text" ng-model="pmsProduct.pmsBrand.name" ng-readonly="true"  style="width:150px" >
						<button type="button" class="btn_type2" ng-click="ctrl.brandSearch()"><b>검색</b></button> 
					</td>	
				</tr>		
				<tr>
					<th>제조사</th>
					<td><input type="text" ng-model="pmsProduct.maker" v-key="pmsProduct.maker" style="width:98%;"></td>
					<th>원산지</th>
					<td><input type="text" ng-model="pmsProduct.origin" v-key="required" style="width:98%;"></td>					
				</tr>
				<tr>
					<th>월령정보<i>필수입력</i></th>
					<td colspan="3">
					<checkbox-list data-ng-model="pmsProduct.ageTypeCds" code-group="AGE_TYPE_CD"></checkbox-list>
					<p>※ 월령정보 선택 시 연속된 기간(최대 3개)을 선택해야 합니다.</p>
					<p class="information" ng-show="common.isEmpty(pmsProduct.ageTypeCds)">필수 입력 항목 입니다.</p>
					</td>
				</tr>
				<tr>
					<th>최대 구매수량</th>
					<td>
						<input type="text" ng-model="pmsProduct.personQty" v-key="pmsProduct.personQty" style="width:50px">
					</td>				
					<th>SALE 뱃지노출여부</th>
					<td ng-init="pmsProduct.dcDisplayYn='N'">
						<input type="radio" ng-model="pmsProduct.dcDisplayYn" value="Y"/><label>노출</label>
						<input type="radio" ng-model="pmsProduct.dcDisplayYn" value="N"/><label>비노출</label>					
					</td>
				</tr>		
				<tr>
					<th>성별 <i>필수입력</i></th>
					<td colspan="3" ng-init="pmsProduct.genderTypeCd = 'GENDER_TYPE_CD.UNISEX'">
					<radio-list ng-model="pmsProduct.genderTypeCd" code-group="GENDER_TYPE_CD" ></radio-list>
					</td>
				</tr>		
				<tr>
					<th>검색어 태그 입력</th>
					<td><input type="text" ng-model="pmsProduct.keyword" v-key="pmsProduct.keyword" style="width:98%;" placeholder="콤마로 구분하여 입력해 주세요."></td>
					<th>검색결과 노출여부</th>
					<td ng-init="pmsProduct.searchExcYn='N'">
						<input type="radio" ng-model="pmsProduct.searchExcYn" value="N"/><label>노출</label>
						<input type="radio" ng-model="pmsProduct.searchExcYn" value="Y"/><label>비노출</label>					
					</td>
				</tr>		
			</tbody>
		</table>
	</div>
	<div class="box_type1 marginT2" ng-show="categoryAttributeList.length > 0">
		<h3 class="sub_title2">상품 속성 정보</h3>
		<div class="tb_util">
			<button type="button" class="btn_type2 btn_type2_gray2">
				<b>상담이관</b>
			</button>
		</div>

		<table class="tb_type1">
			<colgroup>
				<col width="12%">
				<col width="*">
			</colgroup>
			<tbody>
				<tr ng-repeat="attribute in categoryAttributeList">
					<th>{{attribute.name}}</th>
					<td ng-if="attribute.attributeTypeCd=='ATTRIBUTE_TYPE_CD.MULTIPLE'">
						<label ng-repeat="values in attribute.pmsAttributevalues">
					  	<input type="checkbox" checklist-model="attribute.selects" checklist-value="values.attributeValue"> 
					  	<label style="margin:0 14px 0 7px;">{{values.attributeValue}}</label>
						</label>
					</td>
					<td ng-if="attribute.attributeTypeCd=='ATTRIBUTE_TYPE_CD.SINGLE'">
						<label ng-repeat="values in attribute.pmsAttributevalues">
						<input type="radio" ng-model="attribute.value" ng-value="values.attributeValue" />
						<label style="margin:0 14px 0 7px;" for="radio2">{{values.attributeValue}}</label>
						</label>
					</td>		
					<td ng-if="attribute.attributeTypeCd=='ATTRIBUTE_TYPE_CD.INPUT'">
						<input type="text" ng-model="attribute.value" />
					</td>									
				</tr>			
			</tbody>
		</table>
	</div>		
	<div  class="box_type1 marginT2">
		<h3 class="sub_title2">업체/배송 정보</h3>
		<table class="tb_type1">
			<colgroup>
				<col width="14%">
				<col width="34%">
				<col width="14%">
				<col width="*">
			</colgroup>
			<tbody>
				<tr>
					<th>업체코드/명<i>필수입력</i></th>
					<td ng-if="poBusinessId">{{poBusinessId}}/{{pmsProduct.ccsBusiness.name}}
					</td>
					<td ng-if="!poBusinessId"><input type="text" ng-readonly="true" ng-model="pmsProduct.businessId" style="width:50px">
						<input type="text" ng-readonly="true" ng-model="pmsProduct.ccsBusiness.name" style="width:250px" v-key="required">
						<button  ng-show="pmsProduct.approvalYn!='Y' ||  !pmsProduct.businessId" type="button" class="btn_type2" ng-click="ctrl.businessSearch()"><b>검색</b></button> 
					</td>					
					<th>ERP코드/모델명</th>
					<td><input type="text" ng-model="pmsProduct.erpProductId" ng-readonly="true" ng-disabled="!erpProductYn || isSetProduct">
						<button ng-show="pmsProduct.approvalYn!='Y' && !isSetProduct && erpProductYn" type="button" class="btn_type2" ng-click="ctrl.erpProductSearch()"><b>검색</b></button> 
						<button type="button" class="btn_eraser" ng-show="pmsProduct.erpProductId" ng-click="clearErpProductInfo()">지우개</button>
						<p class="information" ng-show="pmsProduct.approvalYn!='Y' && !isSetProduct && erpProductYn && (!pmsProduct.erpProductId || pmsProduct.erpProductId=='')">필수 입력 항목 입니다.</p>
					</td>
				</tr>
				<tr ng-init="pmsProduct.taxTypeCd='TAX_TYPE_CD.TAX'">		
					<th>과세여부</th>
					<td><radio-list ng-model="pmsProduct.taxTypeCd" code-group="TAX_TYPE_CD"></radio-list></td>
					<th>수출용 ERP코드</th>
					<td><input type="text" ng-model="pmsProduct.exportErpProductId" v-key="pmsProduct.exportErpProductId"></td>							
				</tr>
				<tr>
					<th>배송정책</th>
					<td><select v-key="required" ng-change="ctrl.selectDeliverypolicy(pmsProduct.deliveryPolicyNo)" ng-model="pmsProduct.deliveryPolicyNo"  ng-options="policy.deliveryPolicyNo as policy.name for policy in policyList">
							<option value="">선택하세요</option>
						</select>
					</td>			
					<th>택배사</th>
					<td>{{pmsProduct.ccsDeliverypolicy.deliveryServiceName}}</td>						
				</tr>
				<tr>
					<th>배송비</th>
					<td>{{pmsProduct.ccsDeliverypolicy.deliveryFee}}</td>
					<th>출고지</th>
					<td>{{pmsProduct.ccsDeliverypolicy.address1}} {{pmsProduct.ccsDeliverypolicy.address2}}</td>
				</tr>
				<tr>
					<th ng-if="erpProductYn">BOX 구성정보(물류)<i>필수입력</i></th>
					<td ng-if="erpProductYn"><radio-yn ng-model="pmsProduct.boxDeliveryYn" labels="사용,미사용" init-val="N" ></radio-yn>
						<!-- <select ng-show="pmsProduct.boxDeliveryYn=='Y'" ng-model="pmsProduct.boxUnitCd" ng-init="pmsProduct.boxUnitCd='kg'"><option value="kg">kg</option></select> -->
						<span ng-show="pmsProduct.boxDeliveryYn=='Y'"><select-list ng-model="pmsProduct.boxUnitCd" code-group="BOX_UNIT_CD" style="width:100px;"></select-list></span>
						<input ng-show="pmsProduct.boxDeliveryYn=='Y'" type="text" ng-model="pmsProduct.boxUnitQty" v-key="pmsProduct.boxUnitQty"/>
					</td>
					<th>낱개수량<i>필수입력</i></th>
					<td><input type="text" ng-model="pmsProduct.unitQty" v-key="pmsProduct.unitQty" style="width:50px"></td>					
				</tr>
			</tbody>
		</table>	
	</div>		
	
	<div class="box_type1 marginT2">
		<h3 class="sub_title2">추가서비스 정보</h3>
		<table class="tb_type1">
			<colgroup>
				<col width="14%">
				<col width="34%">
				<col width="14%">
				<col width="*">
			</colgroup>
			<tbody>	
 				<tr ng-if="erpProductYn">
					<!-- <th>예약배송 여부<i>필수입력</i></th>
					<td><radio-yn ng-model="pmsProduct.reserveYn" labels="사용,미사용" init-val="N"></radio-yn> 
						<span ng-show="pmsProduct.reserveYn=='Y'">(출고예정일 <input type="text" ng-model="pmsProduct.reserveDeliveryDt"  datetime-picker date-only />)</span></td> -->
					<th>정기배송 여부</th>
					<td colspan="3">
						<input type="radio" ng-model="pmsProduct.regularDeliveryYn" ng-disabled="pmsProduct.reserveYn=='Y'" value="Y" ng-change="changeDeliveryFeeFree(pmsProduct)"/><label>사용</label>
						<input type="radio" ng-model="pmsProduct.regularDeliveryYn" ng-disabled="pmsProduct.reserveYn=='Y'" value="N"/><label>미사용</label>							
						<span ng-show="pmsProduct.regularDeliveryYn=='Y'">&nbsp;&nbsp;
							(&nbsp;신청차수범위 : <input type="text" ng-model="pmsProduct.regularDeliveryMinCnt" number-only style="width:20px" />
							~최대 <input type="text" number-only ng-model="pmsProduct.regularDeliveryMaxCnt"style="width:20px"/> 						
							&nbsp;<input type="checkbox" ng-model="pmsProduct.regularDeliveryFeeFreeYn" ng-true-value="'Y'" ng-false-value="'N'"/>무료배송&nbsp;)
						</span>
						<p class="information" ng-show="pmsProduct.regularDeliveryYn=='Y' && (!pmsProduct.regularDeliveryMinCnt || !pmsProduct.regularDeliveryMaxCnt)">필수 입력 항목 입니다.</p>
					</td>
				</tr>
				<tr>
					<th>픽업가능 여부<i>필수입력</i></th>
					<td>
						<input type="radio" ng-model="pmsProduct.offshopPickupYn" ng-disabled="pmsProduct.reserveYn=='Y' || !(erpProductYn && !isSetProduct)" value="Y"/><label>사용</label>
						<input type="radio" ng-model="pmsProduct.offshopPickupYn" ng-disabled="pmsProduct.reserveYn=='Y' || !(erpProductYn && !isSetProduct)" value="N"/><label>미사용</label>
						<span ng-if="pmsProduct.offshopPickupYn=='Y'">(&nbsp;할인율 : <input type="text" ng-model="pmsProduct.offshopPickupDcRate" number-only style="width:20px" />%&nbsp;) </span>
						<span ng-if="pmsProduct.offshopPickupYn=='N'"><input type="hidden" ng-model="pmsProduct.offshopPickupDcRate" ng-init="pmsProduct.offshopPickupDcRate = 0" number-only style="width:20px" /></span>
						<p class="information" ng-show="pmsProduct.offshopPickupYn=='Y' && !pmsProduct.offshopPickupDcRate">필수 입력 항목 입니다.</p>						
					</td>				
					<th>기프티콘 여부</th>
					<td>
						<input type="radio" ng-model="pmsProduct.giftYn" ng-disabled="pmsProduct.reserveYn=='Y' || (!erpProductYn && isSetProduct)" value="Y"/><label>사용</label>
						<input type="radio" ng-model="pmsProduct.giftYn" ng-disabled="pmsProduct.reserveYn=='Y' || (!erpProductYn && isSetProduct)" value="N"/><label>미사용</label>						
					</td>
				</tr>
				<tr>
					<th>선물포장 여부</th>
					<td>
						<input type="radio" ng-model="pmsProduct.wrapYn" ng-disabled="pmsProduct.reserveYn=='Y' || !(erpProductYn && !isSetProduct)" value="Y"/><label>사용</label>
						<input type="radio" ng-model="pmsProduct.wrapYn" ng-disabled="pmsProduct.reserveYn=='Y' || !(erpProductYn && !isSetProduct)" value="N"/><label>미사용</label>							
						<span ng-if="pmsProduct.wrapYn=='Y'" >(&nbsp;부피 : <input placeholder="부피수치" type="text" ng-model="pmsProduct.wrapVolume"/>&nbsp;)</span>
					<th>선물테마</th>
					<td>
						<radio-list ng-model="pmsProduct.themeCd" code-group="THEME_CD" >
						</radio-list>
						<input type="radio" id="pmsProduct.themeCd4" data-ng-model="pmsProduct.themeCd" value="" class="ng-pristine ng-untouched ng-valid ng-not-empty" name="143">
						<label for="pmsProduct.themeCd4" style="margin:0 14px 0 7px;" class="ng-binding">선택 안함</label>
						<p class="information" ng-show="pmsProduct.giftYn=='Y' && !pmsProduct.themeCd">필수 입력 항목 입니다.</p>
					</td>
											
				</tr>
				<tr>
					<th>해외구매<br/>대행여부<i>필수입력</i></th>
					<td colspan="3">
						<input type="radio" ng-model="pmsProduct.overseasPurchaseYn" ng-disabled="pmsProduct.ccsBusiness.overseasPurchaseYn!='Y' || !(!erpProductYn && !isSetProduct)" value="Y"/><label>사용</label>
						<input type="radio" ng-model="pmsProduct.overseasPurchaseYn" ng-disabled="pmsProduct.ccsBusiness.overseasPurchaseYn!='Y' || !(!erpProductYn && !isSetProduct)" value="N"/><label>미사용</label>			
					</td>
				</tr>
<!-- 				<tr>
					<th>무료배송 여부</th>
					<td><radio-yn ng-model="pmsProduct.deliveryFeeFreeYn" labels="예,아니오" init-val="N"></radio-yn></td>
					<th>지정일배송 여부<i>필수입력</i></th>
					<td ng-init="pmsProduct.fixedDeliveryYn='N'">
						<input type="radio" ng-model="pmsProduct.fixedDeliveryYn" ng-disabled="pmsProduct.fixedDeliveryYn=='Y'" value="Y"/><label>사용</label>
						<input type="radio" ng-model="pmsProduct.fixedDeliveryYn" ng-disabled="pmsProduct.fixedDeliveryYn=='Y'" value="N"/><label>미사용</label>					
					</td>					
				</tr>		 -->
			</tbody>
		</table>
	</div>
	<div class="box_type1 marginT2">
		<h3 class="sub_title2">판매 제한/허용</h3>
		<table class="tb_type1">
			<colgroup>
				<col width="14%">
				<col width="34%">
				<col width="14%">
				<col width="*">
			</colgroup>
			<tbody>
				<tr>
					<th>허용유형</th>
					<td ng-if="erpProductYn">
						<%--제외옵션 :  except="mgrade,mtype,device,channel"  --%>
						<control-set model-name="pmsProduct" lebels="전체,사용자설정" ></control-set>
					</td>		
					<td ng-if="!erpProductYn">전체
					</td>						
					<th>최소구매수량<i>필수입력</i></th>
					<td><input type="text" ng-model="pmsProduct.minQty" v-key="pmsProduct.minQty" style="width:50px"></td>
				</tr>			
			</tbody>
		</table>
	</div>
		
	<div  class="box_type1 marginT2">
		<h3 class="sub_title2">상품 가격 정보</h3>
		<table class="tb_type2">
			<colgroup>
				<col width="10%">
				<col width="10%">
				<col width="10%">
				<col width="10%">
				<col width="10%">
				<col width="10%">
				<col width="10%">
				<col width="10%">
				<col ng-if="pmsProduct.approvalYn=='Y'" width="14%">
				<col ng-if="pmsProduct.approvalYn=='Y'" width="100px">
				<col ng-if="pmsProduct.approvalYn=='Y'" width="100px">
			</colgroup>
			<thead>
				<tr>
					<th>정상가</th>
					<th>공급가</th>
					<th>판매가</th>
					<th>수수료율</th>
					<th>매출 이익액</th>
					<th>적립율</th>
					<th ng-if="erpProductYn">정기배송가</th>
					<th>무료배송</th>
					<th ng-if="pmsProduct.approvalYn=='Y'">가격적용 시작일</th>
					<th ng-if="pmsProduct.approvalYn=='Y'">가격이력</th>
					<th ng-if="pmsProduct.approvalYn=='Y'">가격예약</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-if="pmsProduct.approvalYn=='Y' && isMdLogin!='Y'"><%--판매중 && erp상품 --%>
					<td>{{pmsProduct.listPrice}}</td>
					<td>{{pmsProduct.supplyPrice}}</td>
					<td>{{pmsProduct.salePrice}}</td>
					<td>{{pmsProduct.commissionRate}}</td>
					<td>{{pmsProduct.salePrice-pmsProduct.supplyPrice}}</td>
					<td>{{pmsProduct.pointSaveRate}}</td>
					<td ng-if="erpProductYn">{{pmsProduct.regularDeliveryPrice}}</td>
					<td><input type="checkbox" ng-model="pmsProduct.deliveryFeeFreeYn" ng-true-value="'Y'" ng-false-value="'N'"/></td>
					<td>{{pmsProduct.priceApplyDt}}</td>
					<td><button class="btn_type2" ng-click="ctrl.openPriceHistory()"><b>가격이력</b></button></td>
					<td><button class="btn_type2" ng-click="ctrl.openReservePrice()" fn-Id="3_DETAIL"><b>가격예약</b></button></td>
				</tr>
				<tr ng-if="pmsProduct.approvalYn!='Y' || isMdLogin=='Y'">
					<td ng-if="!erpProductYn">{{pmsProduct.listPrice}}</td>
					<td ng-if="erpProductYn"><input type="text" ng-model="pmsProduct.listPrice" v-key="pmsProduct.listPrice" style="width:90%"/></td>
					<td ng-if="erpProductYn"><input type="text" ng-model="pmsProduct.supplyPrice" v-key="pmsProduct.supplyPrice" ng-change="changePrice(pmsProduct)"  style="width:90%"/></td>
					<td ng-if="!erpProductYn">{{pmsProduct.supplyPrice}}</td>
					<td><input type="text" ng-model="pmsProduct.salePrice" v-key="pmsProduct.salePrice" ng-change="changePrice(pmsProduct)" style="width:90%"/></td>
					<td ng-if="erpProductYn">{{pmsProduct.commissionRate}}</td>
					<td ng-if="!erpProductYn">
							<select ng-model="pmsProduct.commissionRate" ng-change="changePrice(pmsProduct)" ng-options="c.commissionRate as c.commissionRate for c in commissionList" v-key="required">
								<option value="">선택하세요</option>
							</select>
					</td>					
					<td>{{pmsProduct.salePrice-pmsProduct.supplyPrice}}</td>
					<td><input type="text" ng-model="pmsProduct.pointSaveRate" v-key="pmsProduct.pointSaveRate" style="width:90%"/></td>
					<td ng-if="erpProductYn && pmsProduct.regularDeliveryYn == 'Y'"><input type="text" ng-model="pmsProduct.regularDeliveryPrice" v-key="pmsProduct.regularDeliveryPrice" style="width:90%"/></td>
					<td ng-if="erpProductYn && pmsProduct.regularDeliveryYn != 'Y'"><input type="text" ng-model="pmsProduct.regularDeliveryPrice" ng-init="pmsProduct.regularDeliveryPrice = 0" ng-disabled="true" style="width:90%"/></td>
					<td><input type="checkbox" ng-model="pmsProduct.deliveryFeeFreeYn" ng-true-value="'Y'" ng-false-value="'N'"/>
					<td ng-if="pmsProduct.approvalYn=='Y' && isMdLogin=='Y'">{{pmsProduct.priceApplyDt}}</td>
					<td ng-if="pmsProduct.approvalYn=='Y' && isMdLogin=='Y'"><button class="btn_type2" ng-click="ctrl.openPriceHistory()"><b>가격이력</b></button></td>
					<td ng-if="pmsProduct.approvalYn=='Y' && isMdLogin=='Y'"><button class="btn_type2" ng-click="ctrl.openReservePrice()" fn-Id="3_DETAIL"><b>가격예약</b></button></td>					
					</td>
				</tr>
			</tbody>
		</table>		
	</div>
	
	<div class="box_type1 marginT2" >
		<h3 class="sub_title2">상품 옵션 정보</h3>
		<table class="tb_type1">
			<colgroup>
				<col width="12%">
				<col width="*">
			</colgroup>
			<tbody>
				<tr ng-if="!isSetProduct">
					<th>
						옵션사용여부
					</th>
					<td>
						<input type="radio" ng-model="pmsProduct.optionYn" ng-click="ctrl.clickOptionYn('Y')" ng-disabled="pmsProduct.erpProductId || pmsProduct.approvalYn=='Y'"  value="Y"/><label>사용</label>
						<input type="radio" ng-model="pmsProduct.optionYn" ng-click="ctrl.clickOptionYn('N')" ng-disabled="pmsProduct.erpProductId || pmsProduct.approvalYn=='Y'" value="N"/><label>미사용</label>
					</td>					
				</tr>
				<tr>
					<th>텍스트옵션<br/>사용여부<i>필수입력</i></th>
					<td><radio-yn ng-model="pmsProduct.textOptionYn" labels="사용,미사용" ></radio-yn>
						<input type="text" style="width:50%" ng-show="pmsProduct.textOptionYn=='Y'" ng-model="pmsProduct.textOptionName" v-key="pmsProduct.textOptionName"/>
					</td>
				</tr>				
				<tr ng-if="!isSetProduct"> <!-- ng-if="pmsProduct.optionYn=='Y'" -->
					<th>
						단품목록 <i>필수입력</i>
					</th>
					<td style="text-align:right">
						<button type="button" class="btn_type2" ng-if="pmsProduct.optionYn=='Y'"  ng-click="ctrl.openOptionManager()" fn-id="3_DETAIL"><b>옵션설정</b></button>
						<div class="gridbox gridbox200" style="text-align:center">
				   				<div class="grid" data-ui-grid="grid2"   
								data-ui-grid-move-columns 
								data-ui-grid-resize-columns 
								data-ui-grid-auto-resize 
								data-ui-grid-selection 
								data-ui-grid-row-edit
								data-ui-grid-cell-nav
								data-ui-grid-exporter
								data-ui-grid-edit 
								data-ui-grid-validate></div>
						</div>			
						<p style="text-align:left" class="information" ng-show="grid2.data.length==0">필수 입력 항목 입니다.</p>
					</td>				
				</tr>
			</tbody>
		</table>	
	</div>	
	

	<div  class="box_type1 marginT2" ng-if="isSetProduct" >
		<h3 class="sub_title2">세트 상품 구성</h3>
		<table class="tb_type1">
			<colgroup>
				<col width="12%">
				<col width="*">
			</colgroup>
			<tbody>
				<tr>
					<th>
						구성상품 <i>필수입력</i>
					</th>
					<td  style="text-align:right">
						<button type="button" ng-if="pmsProduct.approvalYn!='Y'" class="btn_type2" ng-click="ctrl.productSearch()"><b>상품추가</b></button>
						<button type="button" ng-if="pmsProduct.approvalYn!='Y'" class="btn_type2" ng-click="ctrl.deleteSetProduct()"><b>삭제</b></button>
						<div class="gridbox gridbox200">
				   				<div class="grid" data-ui-grid="grid3"   
								data-ui-grid-move-columns 
								data-ui-grid-resize-columns 
								data-ui-grid-auto-resize 
								data-ui-grid-selection 
								data-ui-grid-row-edit
								data-ui-grid-cell-nav
								data-ui-grid-exporter
								data-ui-grid-edit 
								data-ui-grid-validate></div>
						</div>			
						<p class="information" ng-show="grid3.data.length==0">필수 입력 항목 입니다.</p>
					</td>				
				</tr>
			</tbody>
		</table>   
	</div>	
	<div class="box_type1 marginT2" ng-if="isSetProduct" ng-repeat="setProduct in pmsProduct.pmsSetproducts">
		<h3 class="sub_title2">구성 상품 단품 정보( {{setProduct.name}}/{{setProduct.subProductId}} )</h3>
		<table class="tb_type2">
			<colgroup>
				<col width="10%">
				<col width="20%">
				<col width="10%">
				<col width="10%">
				<col width="10%">
				<col width="10%">
			</colgroup>
			<thead>
				<tr>
					<th>단품번호</th>
					<th>단품명</th>
					<th>바코드</th>
					<th>단품상태</th>
					<th>재고수량</th>
					<th>단품추가금액</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="pmsSaleproduct in setProduct.pmsSaleproducts">
					<td><!-- <input type="text" readonly ng-model="pmsSaleproduct.saleproductId" 			style="width:90%"/> -->{{pmsSaleproduct.saleproductId}}</td>
					<td><!-- <input type="text" readonly ng-model="pmsSaleproduct.name" 					style="width:90%"/> -->{{pmsSaleproduct.name}}</td>
					<td><!-- <input type="text" readonly ng-model="pmsSaleproduct.erpSaleproductId" 		style="width:90%"/> -->{{pmsSaleproduct.erpSaleproductId}}</td>
					<td><!-- <input type="text" readonly ng-model="pmsSaleproduct.saleproductStateName" 	style="width:90%"/> -->{{pmsSaleproduct.saleproductStateName}}</td>
					<td><!-- <input type="text" readonly ng-model="pmsSaleproduct.realStockQty" 			style="width:90%"/> -->{{pmsSaleproduct.realStockQty}}</td>
					<td><!-- <input type="text" readonly ng-model="pmsSaleproduct.addSalePrice" 			style="width:90%"/> -->{{pmsSaleproduct.addSalePrice}}</td>
				</tr>
			</tbody>
		</table>		
	</div>
	
	

<!--  	<div  class="box_type1 marginT2">
		<h3 class="sub_title2">상품재고정보</h3>
		<table class="tb_type1">
			<colgroup>
				<col width="12%">
				<col width="*">
			</colgroup>
			<tbody>
				<tr>
					<th>상품재고제어방식</th>
					<td><radio-list ng-model="pmsProduct.stockControlTypeCd" code-group="STOCK_CONTROL_TYPE_CD"/>
					</td>
				</tr>
			</tbody>
		</table>	
	</div> -->
	
	<div  class="box_type1 marginT2">
		<h3 class="sub_title2">상품셀링포인트</h3>
		<table class="tb_type1">
			<colgroup>
				<col width="12%">
				<col width="*">
			</colgroup>
			<tbody>
				<tr>
					<th>상품셀링포인트</th>
					<td><textarea ckeditor="ckOption" height="300px"  ng-model="pmsProduct.sellingPoint" v-key="pmsProduct.sellingPoint"></textarea>
					</td>
				</tr>
			</tbody>
		</table>	
	</div>		
	<div class="box_type1 marginT2">
		<h3 class="sub_title2">상품 품목 정보</h3>
		<div class="tb_util">
			<button type="button" class="btn_type2 btn_type2_gray2">
				<b>상담이관</b>
			</button>
		</div>

		<table class="tb_type1">
			<colgroup>
				<col width="12%">
				<col width="*">
			</colgroup>
			<tbody>
				<tr>
					<th>
						상품고시정보선택 <i>필수입력</i>
					</th>
					<td>
						<select-list ng-model="pmsProduct.productNoticeTypeCd" code-group="PRODUCT_NOTICE_TYPE_CD" v-key="required" style="width:250px;"></select-list>
					</td>
				</tr>			
				<tr ng-repeat="notice in pmsProduct.pmsProductnotices">
					<th>
						{{notice.pmsProductnoticefield.title}}
					</th>
					<td ng-if="notice.pmsProductnoticefield.title.indexOf('KC 인증') >= 0 ">
						<textarea title="{{notice.pmsProductnoticefield.title}}" cols="30" rows="4" ng-model="notice.detail" placeholder="{{notice.pmsProductnoticefield.note}}" >
						</textarea>
						<!-- <p>{{notice.pmsProductnoticefield.note}}</p> -->
					</td>
					<td ng-if="notice.pmsProductnoticefield.title.indexOf('KC 인증') < 0 ">
						<textarea title="{{notice.pmsProductnoticefield.title}}" cols="30" rows="4" ng-model="notice.detail" placeholder="{{notice.pmsProductnoticefield.note}}" v-key="required">
						</textarea>
						<!-- <p>{{notice.pmsProductnoticefield.note}}</p> -->
					</td>					
				</tr>
			</tbody>
		</table>
	</div>	
	
	<div  class="box_type1 marginT2">
		<h3 class="sub_title2">상품기술서</h3>
		<table class="tb_type1">
			<colgroup>
				<col width="12%">
				<col width="*">
			</colgroup>
			<tbody>
				<tr>
					<th>기술서 상세</th>
					<td><textarea ng-model="pmsProduct.detail" v-key="pmsProduct.detail" ckeditor="ckOption" height="300px"></textarea>
					</td>
				</tr>
<!-- 				<tr>
					<th>배송정보 안내</th>
					<td><textarea ng-model="pmsProduct.deliveryInfo" v-key="pmsProduct.deliveryInfo" rows="4"></textarea>
					</td>
				</tr> -->
				<tr>
					<th>반품,교환, 환불 정보 안내</th>
					<td><textarea ng-model="pmsProduct.claimInfo" v-key="pmsProduct.claimInfo" ckeditor="ckOption" height="300px"></textarea>
					</td>
				</tr>								
			</tbody>
		</table>	
	</div>		

	
	<div class="btn_alignC marginT3">
<!-- 		<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.reserveHistory()">
			<b>정보예약목록</b>
		</button>	 -->
		<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /></b>
		</button>	

		
		<button ng-if="pmsProduct.saleStateCd=='SALE_STATE_CD.SALE' || pmsProduct.saleStateCd=='SALE_STATE_CD.STOP' || !poBusinessId && pmsProduct.saleStateCd=='SALE_STATE_CD.MDSTOP'" 
			type="button" class="btn_type3 btn_type3_gray" ng-click="saveStatus('1')">
			<b>품절</b>
		</button>
		<button ng-if="pmsProduct.saleStateCd=='SALE_STATE_CD.SALE' || pmsProduct.saleStateCd=='SALE_STATE_CD.SOLDOUT' || !poBusinessId && pmsProduct.saleStateCd=='SALE_STATE_CD.MDSTOP'" 
			type="button" class="btn_type3 btn_type3_gray" ng-click="saveStatus('2')">
			<b>일시정지</b>
		</button>
		<button ng-if="!poBusinessId && (pmsProduct.saleStateCd=='SALE_STATE_CD.SALE' || pmsProduct.saleStateCd=='SALE_STATE_CD.SOLDOUT' || pmsProduct.saleStateCd=='SALE_STATE_CD.STOP')" 
			type="button" class="btn_type3 btn_type3_gray" ng-click="saveStatus('4')" fn-id="3_DETAIL">
			<b>MD정지</b>
		</button>			
		<button ng-if="pmsProduct.saleStateCd=='SALE_STATE_CD.SALE' || pmsProduct.saleStateCd=='SALE_STATE_CD.SOLDOUT' || pmsProduct.saleStateCd=='SALE_STATE_CD.STOP' || !poBusinessId && pmsProduct.saleStateCd=='SALE_STATE_CD.MDSTOP'" 
			type="button" class="btn_type3 btn_type3_gray" ng-click="saveStatus('5')" fn-id="3_DETAIL">
			<b>영구종료</b>
		</button>				
		<button ng-if="pmsProduct.saleStateCd=='SALE_STATE_CD.SOLDOUT' || pmsProduct.saleStateCd=='SALE_STATE_CD.STOP' || !poBusinessId && pmsProduct.saleStateCd=='SALE_STATE_CD.MDSTOP'" 
			type="button" class="btn_type3 btn_type3_gray" ng-click="saveStatus('3')">
			<b>판매</b>
		</button>	
		<button ng-if="pmsProduct.saleStateCd=='SALE_STATE_CD.REJECT'" 
			type="button" class="btn_type3 btn_type3_gray" ng-click="saveStatus('6')">
			<b>승인요청</b>
		</button>			
		<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.saveProduct()" fn-id="3_SAVE">
			<b><spring:message code="c.common.save" /></b>
		</button>				
	</div>	
	</form>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>