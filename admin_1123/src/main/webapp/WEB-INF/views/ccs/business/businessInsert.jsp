<%--
	화면명 : PO 메인 > 업체 등록 (입점 신청)
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.business.list.js"></script>

<article class="con_box"ng-app="businessApp" data-ng-controller="businessInsertController as ctrl">
	<form name="form2">
		<h2 class="sub_title1">입점신청</h2>
			<div class="box_type1 marginT2">
			<h3 class="sub_title2">협력사 기본정보</h3>
				<table class="tb_type1">
					<colgroup>
						<col class="col_142" />
						<col class="col_auto" />
					</colgroup>
					<tbody>
						<tr>
							<th>업체번호<i><spring:message code="c.input.required"/></i></th>
							<td>{{ccsBusiness.businessId }}</td>
							<th><spring:message code="c.ccsBusiness.businessname"/><i><spring:message code="c.input.required"/></i></th>
							<td colspan="3"> <input type="text" ng-model="ccsBusiness.name"  v-key="ccsBusiness.name" placeholder="(사업자등록증상의 법인명)" style="width:70%;"/></td>
						</tr>
						<tr>
							<th><spring:message code="c.ccsBusiness.business.repname"/><i><spring:message code="c.input.required"/></i></th>
							<td><input type="text" ng-model="ccsBusiness.repName"  v-key="ccsBusiness.repName"  style="width:50%;"required/></td>
							<th><spring:message code="c.ccsBusiness.business.repno"/><i><spring:message code="c.input.required"/></i></th>
							<td colspan="3">
								<input type="text" name="regno_field" reg_input ng-model="ccsBusiness.regNo" ng-pattern="/^\d{3}-?\d{2}-?\d{5}/i" style="width:70%;" required/>
								<p class="txt_type2">ex)123-45-67890</p> 
								<p class="information" ng-show="form2.regno_field.$error.pattern">유효하지 않은 형식입니다.</p>
								<p class="information" ng-show="form2.regno_field.$error.required">필수 입력 항목 입니다.</p>
							</td>
						</tr>
						<tr>
							<th><spring:message code="c.ccsBusiness.business.condition"/><i><spring:message code="c.input.required"/></i></th>
							<td><input type="text" ng-model="ccsBusiness.businessCondition"  v-key="ccsBusiness.businessCondition"   style="width:70%;" required/></td>
							<th><spring:message code="c.ccsBusiness.business.type"/><i><spring:message code="c.input.required"/></i></th>
							<td colspan="3"><input type="text" ng-model="ccsBusiness.businessType" v-key="ccsBusiness.businessType"  style="width:70%;" required/>
							
							</td>
						</tr>
						
						<tr>
							<th><spring:message code="c.ccsBusiness.business.taxtypecd"/><i><spring:message code="c.input.required"/></i></th>
							<td>
								<span data-ng-repeat="tax in taxTypes" data-ng-init="ccsBusiness.businessTaxTypeCd='BUSINESS_TAX_TYPE_CD.CORPORATE'">
									<input type="radio" data-ng-model="ccsBusiness.businessTaxTypeCd" value="{{tax.val}}" required/>
									<label style="margin:0 14px 0 7px;">{{tax.text}}</label>
								</span>
								<!-- <radio-list data-ng-init="ccsBusiness.businessTaxTypeCd = 'BUSINESS_TAX_TYPE_CD.CORPORATE'" ng-model="ccsBusiness.businessTaxTypeCd" code-group="BUSINESS_TAX_TYPE_CD" required/> -->
							</td>
							<th><spring:message code="c.ccsBusiness.business.statecd"/><i><spring:message code="c.input.required"/></i></th>
							<td colspan="3" data-ng-init="ccsBusiness.businessStateCd = 'BUSINESS_STATE_CD.READY'">
								승인전
							</td>											
						</tr>
						<tr>
							<th><spring:message code="c.ccsBusiness.business.supplyitem"/><i><spring:message code="c.input.required"/></i></th>
							<td><input type="text" ng-model="ccsBusiness.supplyItem" v-key="ccsBusiness.supplyItem" style="width:50%;" required/></td>
							<%-- <th><spring:message code="c.ccsBusiness.business.saletypecd"/><i><spring:message code="c.input.required"/></i></th>
							<td > 
								<span data-ng-repeat="sale in saleTypes" data-ng-init="ccsBusiness.saleTypeCd='SALE_TYPE_CD.CONSIGN'">
									<input type="radio" data-ng-model="ccsBusiness.saleTypeCd" value="{{sale.val}}" name="response" required/>
									<label style="margin:0 14px 0 7px;">{{sale.text}}</label>
								</span>
								
								<!-- <radio-list ng-init="ccsBusiness.saleTypeCd='SALE_TYPE_CD.CONSIGN'" ng-change="ctrl.saleTypeCdClick()" ng-model="ccsBusiness.saleTypeCd" code-group="SALE_TYPE_CD" required/> -->
							</td>  --%>
						</tr>
					<%-- 	<tr>
							<th>위택매입유형<i><spring:message code="c.input.required"/></i></th>
							<td data-ng-init="ccsBusiness.purchaseYn = 'Y'"> 
								<input ng-change="ctrl.purchaseYnClick()" type="radio" ng-model="ccsBusiness.purchaseYn" ng-disabled="ccsBusiness.saleTypeCd=='SALE_TYPE_CD.PURCHASE'" value="Y"/>
								<label>Y <em>(수수료 방식 정산)</em></label>
								<input type="radio" ng-model="ccsBusiness.purchaseYn" ng-disabled="ccsBusiness.saleTypeCd=='SALE_TYPE_CD.PURCHASE'" value="N"/>
								<label>N <em>(매입매출 방식 정산)</em></label>
								<p class="information" ng-show="ccsBusiness.saleTypeCd=='SALE_TYPE_CD.CONSIGN' && !ccsBusiness.purchaseYn">
									필수 입력 항목 입니다.
								</p>
								<p class="information">※상태변경 시 정산금액이 변경될 수 있습니다.</p>
							</td>
						</tr> --%>
						<tr>
							<th>업체카테고리</th>
							<td colspan="3">
								<div class="exPosition">
									<div class="btn_alignR" style="margin-top:2px;">
										<button type="button" class="btn_type2 " ng-click="ctrl.pmsCategoryPopup()"><b>카테고리 추가<%-- <spring:message code="c.ccsBusiness.category.insert"/> --%></b></button>
										<button type="button" class="btn_type2"  ng-click="commissionGrid.deleteRow();"><b><spring:message code="c.common.delete"/></b></button>
									</div>
								</div>
								<div class="box_type1" >
									<div class="gridbox gridbox200">
										<div class="grid" data-ui-grid="grid_commission"   
													data-ui-grid-move-columns 
													data-ui-grid-resize-columns 
													data-ui-grid-auto-resize 
													data-ui-grid-exporter
													data-ui-grid-edit 
													data-ui-grid-selection
													data-ui-grid-validate
													data-ui-grid-row-edit
													data-ui-grid-cell-nav></div>
										</div>
									</div>
								<!-- <p class="information" ng-show="grid_commission.data.length==0 && (ccsBusiness.saleTypeCd=='SALE_TYPE_CD.CONSIGN' && ccsBusiness.purchaseYn=='N')">필수 입력 항목 입니다.</p> -->
							</td>
						</tr>
						<tr>
							<th><spring:message code="c.ccsBusiness.business.note"/></th>
							<td colspan="3">
								<textarea cols="10" rows="5" placeholder="" ng-model="ccsBusiness.note"></textarea>
							</td>								
						</tr>
					</tbody>
				</table>
			</div>
			
			<div class="box_type1 marginT2">
				<h3 class="sub_title2">영업 담당자 정보<%-- <spring:message code="c.ccsBusiness.title2"/> --%></h3>
				<table class="tb_type1">
					<colgroup>
						<col width="9%" />
						<col width="15%" />
						<col width="9%" />
						<col width="15%" />
					</colgroup>
					<tbody>
						<tr>
							<th>사용자 ID<i><spring:message code="c.input.required"/></i></th>
							<td colspan="3">
								<input type="text" ng-model="ccsBusiness.reqUserId" style="width:25%;" v-key="ccsBusiness.reqUserId" required placeholder="(영문, 숫자혼합 3자리 이상)"/>
							</td>
						</tr>
						<tr>
							<th><spring:message code="ccsUser.pwd"/><i><spring:message code="c.input.required"/></i></th> <!-- 비밀번호 -->
							<td>
								<input type="password" ng-model="ccsBusiness.pwd1" v-key="ccsUser.pwd"  style="width:50%;" placeholder="(영문, 숫자혼합 3자리 이상)"/>
							</td>
							<th><spring:message code="c.ccs.user.pwdconfirm"/><i><spring:message code="c.input.required"/></i></th> <!-- 비빌번호 확인 -->
							<td>
								<input type="password" ng-model="ccsBusiness.pwd2" v-key="ccsUser.pwd"  style="width:50%;" placeholder="(영문, 숫자혼합 3자리 이상)"/>
							</td>
						</tr>
						<tr>
							<th> <spring:message code="c.ccsBusiness.manager.name"/><i><spring:message code="c.input.required"/></i></th>
							<td>
								<input type="text" ng-model="ccsBusiness.managerName"  v-key="ccsBusiness.managerName" style="width:50%;" required/>
							</td>
							<th> <spring:message code="c.ccsBusiness.manager.email"/><i><spring:message code="c.input.required"/></i></th>
							<td> 
								<input type="text" ng-model="ccsBusiness.managerEmail"  v-key="ccsBusiness.managerEmail" style="width:50%;" required/>
							</td>
						</tr>
						<tr>
							<th><spring:message code="c.ccsBusiness.manager.phone1"/></th>
							<td>
								<input type="text" tel-input ng-model="ccsBusiness.managerPhone1" style="width:50%;" v-key="ccsBusiness.managerPhone1" /><!--  v-key="ccsBusiness.managerPhone1"  -->
							</td>
							<th><spring:message code="c.ccsBusiness.manager.phone2"/><i><spring:message code="c.input.required"/></i></th>
							<td>
								<input type="text" tel-input ng-model="ccsBusiness.managerPhone2" style="width:50%;" v-key="ccsBusiness.managerPhone2" required/> <!-- v-key="ccsBusiness.managerPhone2"  -->
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<div class="box_type1 marginT2">
				<h3 class="sub_title2">정산 담당자 정보<%-- <spring:message code="c.ccsBusiness.title2"/> --%></h3>
				<table class="tb_type1">
					<colgroup>
						<col width="9%" />
						<col width="15%" />
						<col width="9%" />
						<col width="15%" />
					</colgroup>
					<tbody>
						<tr>
							<th> <spring:message code="c.ccsBusiness.manager.name"/></th>
							<td>
								<input type="text" ng-model="ccsBusiness.salesName"  v-key="ccsBusiness.salesName" style="width:50%;" />
							</td>
							<th> <spring:message code="c.ccsBusiness.manager.email"/></th>
							<td> 
								<input type="text" ng-model="ccsBusiness.salesEmail" v-key="ccsBusiness.salesEmail" style="width:50%;"/>
							</td>
						</tr>
						<tr>
							<th><spring:message code="c.ccsBusiness.manager.phone1"/></th>
							<td>
								<input type="text" tel-input ng-model="ccsBusiness.salesPhone1" v-key="ccsBusiness.salesPhone1" style="width:50%;"/>
							</td>
							<th><spring:message code="c.ccsBusiness.manager.phone2"/></th>
							<td>
								<input type="text" tel-input ng-model="ccsBusiness.salesPhone2" v-key="ccsBusiness.salesPhone2" style="width:50%;"/>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
			
			<div class="box_type1 marginT2">
				<h3 class="sub_title2">협력사 소재지정보</h3>
				<table class="tb_type1">
					<colgroup>
						<col width="9%" />
						<col width="15%" />
						<col width="9%" />
						<col width="15%" />
					</colgroup>
					<tbody>
						<tr>
							<th> <spring:message code="c.ccsBusiness.address"/> </th>
							<td  colspan="3">
								<input type="text" ng-model="ccsBusiness.zipCd"  v-key="ccsBusiness.zipCd" style="width:20%;" readonly/>
								<button type="button" class="btn_type2" ng-click="ctrl.searchAddress()">
									<b><spring:message code="c.ccsBusiness.zipcd"/></b>
								</button>
								<button type="button" class="btn_eraser" ng-click="ctrl.eraser()">지우개</button>
								<br>
								<input type="text" ng-model="ccsBusiness.address1" style="width:20%;" readonly/>
								<input type="text" ng-model="ccsBusiness.address2" style="width:20%;" readonly/>
							</td>
							
						</tr>
						<tr>
							<th> <spring:message code="c.ccsBusiness.phone"/></th>
							<td> 
								<input type="text" tel-input ng-model="ccsBusiness.phone" v-key="ccsBusiness.phone" style="width:50%;"/>
							</td>
							<th><spring:message code="c.ccsBusiness.fax"/></th>
							<td>
								<input type="text" fax-input ng-model="ccsBusiness.fax" v-key="ccsBusiness.fax" style="width:50%;"/>
							</td>
						</tr>
						<tr>
							<th><spring:message code="c.ccsBusiness.memo"/></th>
							<td colspan="3">
								<textarea cols="10" rows="5" placeholder="" ng-model="ccsBusiness.memo"></textarea>
							</td>								
						</tr>
					</tbody>
				</table>
			</div>
			
			<div class="box_type1 marginT2">
				<h3 class="sub_title2">협력사 거래정보</h3>
				<table class="tb_type1">
					<colgroup>
						<col width="9%" />
						<col width="15%" />
						<col width="9%" />
						<col width="15%" />
					</colgroup>
					<tbody>
						<tr>
							<th> <spring:message code="c.ccsBusiness.bankname"/></th>
							<td>
								<input type="text" ng-model="ccsBusiness.bankName" style="width:55%;" v-key="ccsBusiness.bankName"/>
							</td>
							<th> <spring:message code="c.ccsBusiness.depositorname"/></th>
							<td>
								<input type="text" ng-model="ccsBusiness.depositorName" style="width:55%;" v-key="ccsBusiness.depositorName"/>
							</td>
							
						</tr>
						<tr>
							<th> <spring:message code="c.ccsBusiness.accountno"/></th>
							<td>
								<input type="text" ng-model="ccsBusiness.accountNo" style="width:55%;" v-key="ccsBusiness.accountNo"/>
							</td>
							<!-- <th>계약기간</th>
							<td>
								<input type="text" ng-model="ccsBusiness.contractStartDt" datetime-picker period-start/>										
								~
								<input type="text" ng-model="ccsBusiness.contractEndDt" datetime-picker period-end/>
								<p class="information" ng-show="!ccsBusiness.contractStartDt || !ccsBusiness.contractEndDt">필수 입력 항목 입니다.</p>
							</td> -->
						</tr>
					</tbody>
				</table>
			</div>
			
			<div class="btn_alignC marginT3">
				<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
					<b><spring:message code="c.common.close"/></b>
				</button>
				<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.updateBusiness()">
					<b><spring:message code="c.common.save"/></b>
				</button>
			</div>
	</form>
</article>

<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"/>