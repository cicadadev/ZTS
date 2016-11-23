<%--
	화면명 : 업체 관리 > 업체 상세 > 업체 기본정보 상세
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ page import="gcp.common.util.BoSessionUtil" %>
<%
	pageContext.setAttribute("businessId", BoSessionUtil.getBusinessId());
%>
<c:choose>
<c:when test="${businessId != '' && businessId != null}">
	<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true"/>
</c:when>
<c:otherwise>
	<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
</c:otherwise>		
</c:choose>
<script type="text/javascript" src="/resources/js/app/ccs.app.business.list.js"></script>

<style type="text/css">
	table {table-layout:fixed;}
	table button + .gridbox {margin-top:5px; padding:0;}
</style>

<c:choose>
<c:when test="${businessId != '' && businessId != null}">
	<article class="con_box con_on" ng-app="businessApp" data-ng-controller="businessDetailController as ctrl" data-ng-init="ctrl.detail()">
</c:when>
<c:otherwise>
	<div id="mydiv" class="wrap_popup" ng-app="businessApp" data-ng-controller="businessDetailController as ctrl" data-ng-init="ctrl.detail()">
</c:otherwise>		
</c:choose>
	<ul class="tab_type2">
		<li class="on">
			<button type="button"><spring:message code="c.ccsBusiness.info1"/></button>
		</li>
		<li ng-class="{'disabled' : insertPageFlag}">
			<button type="button" ng-click="!insertPageFlag ? ctrl.onClickTab('2') : ''"><spring:message code="c.ccsBusiness.info2"/></button>
		</li>
		<li ng-class="{'disabled' : insertPageFlag}">
			<button type="button" ng-click="!insertPageFlag ? ctrl.onClickTab('3') : ''"><spring:message code="c.ccsBusiness.info3"/></button>
		</li>
		<li ng-class="{'disabled' : insertPageFlag}">
			<button type="button" ng-click="!insertPageFlag ? ctrl.onClickTab('4') : ''"><spring:message code="c.ccsBusiness.info4"/></button>
		</li>
	</ul>
	
	<form name="form2">
			<div class="box_type1 marginT2">
				<table class="tb_type1">
					<colgroup>
						<col width="15%" />
						<col width="35%" />
						<col width="15%" />
						<col width="35%" />
					</colgroup>
					<tbody>
						<tr>
							<th>업체번호<%-- <spring:message code="c.ccsBusiness.businessid"/> --%><i><spring:message code="c.input.required"/></i></th>
							<td>{{ccsBusiness.businessId }}</td>
							<th ng-if="poBusinessId"><spring:message code="c.ccsBusiness.businessname"/><i><spring:message code="c.input.required"/></i></th>
							<td ng-if="poBusinessId">{{ccsBusiness.name }}</td>
							<th ng-if="!poBusinessId"><spring:message code="c.ccsBusiness.businessname"/><i><spring:message code="c.input.required"/></i></th>
							<td ng-if="!poBusinessId"><input type="text" ng-model="ccsBusiness.name"  v-key="ccsBusiness.name" placeholder="(사업자등록증상의 법인명)" style="width:50%;"/></td>
						</tr>
						<tr ng-if="poBusinessId">
							<th><spring:message code="c.ccsBusiness.business.repname"/><i><spring:message code="c.input.required"/></i></th>
							<td>{{ccsBusiness.repName }}</td>
							<th><spring:message code="c.ccsBusiness.business.repno"/><i><spring:message code="c.input.required"/></i></th>
							<td>{{ccsBusiness.regNo }}
							</td>
						</tr>
						<tr ng-if="!poBusinessId">
							<th><spring:message code="c.ccsBusiness.business.repname"/><i><spring:message code="c.input.required"/></i></th>
							<td><input type="text" ng-model="ccsBusiness.repName"  v-key="ccsBusiness.repName" style="width:50%;" required/></td>
							<th><spring:message code="c.ccsBusiness.business.repno"/><i><spring:message code="c.input.required"/></i></th>
							<td>
								<input type="text" reg_input ng-model="ccsBusiness.regNo"  v-key="ccsBusiness.regNo" style="width:50%;" required/>
								<p class="txt_type2">ex)123-45-67890</p>
							</td>
						</tr>
						<tr ng-if="poBusinessId">
							<th><spring:message code="c.ccsBusiness.business.condition"/><i><spring:message code="c.input.required"/></i></th>
							<td>{{ccsBusiness.businessCondition}}</td>
							<th><spring:message code="c.ccsBusiness.business.type"/><i><spring:message code="c.input.required"/></i></th>
							<td>{{ccsBusiness.businessType}}</td>
						</tr>
						<tr ng-if="!poBusinessId">
							<th><spring:message code="c.ccsBusiness.business.condition"/><i><spring:message code="c.input.required"/></i></th>
							<td><input type="text" ng-model="ccsBusiness.businessCondition"  v-key="ccsBusiness.businessCondition" style="width:50%;" required/></td>
							<th><spring:message code="c.ccsBusiness.business.type"/><i><spring:message code="c.input.required"/></i></th>
							<td><input type="text" ng-model="ccsBusiness.businessType"  v-key="ccsBusiness.businessType" style="width:50%;" required/></td>
						</tr>
						<tr ng-if="poBusinessId">
							<th><spring:message code="c.ccsBusiness.business.taxtypecd"/><i><spring:message code="c.input.required"/></i></th>
							<td>{{ccsBusiness.businessTaxTypeName }}
							</td>
							<th><spring:message code="c.ccsBusiness.business.statecd"/><i><spring:message code="c.input.required"/></i></th>
							<td>
								{{ccsBusiness.businessStateName}}
							</td>
											
						</tr>
						<tr ng-if="!poBusinessId">
							<th><spring:message code="c.ccsBusiness.business.taxtypecd"/><i><spring:message code="c.input.required"/></i></th>
							<td>
								<radio-list data-ng-init="ccsBusiness.businessTaxTypeCd = 'BUSINESS_TAX_TYPE_CD.CORPORATE'" ng-model="ccsBusiness.businessTaxTypeCd" code-group="BUSINESS_TAX_TYPE_CD" v-key="required"/>
							</td>
							<th><spring:message code="c.ccsBusiness.business.statecd"/><i><spring:message code="c.input.required"/></i></th>
							<td ng-show="ccsBusiness.businessStateCd=='BUSINESS_STATE_CD.READY'" data-ng-init="ccsBusiness.businessStateCd = 'BUSINESS_STATE_CD.READY'" >
								승인전
							</td>
							<td ng-show="ccsBusiness.businessStateCd!='BUSINESS_STATE_CD.READY'">
								{{ccsBusiness.businessStateName}}
							</td>
											
						</tr>
						<tr><!-- 매입유형 -->
							<th> <spring:message code="c.ccsBusiness.business.erpbusinessid"/><i><spring:message code="c.input.required"/></i></th>
							<td ng-if="loginType != 'PO'"> 
								<input type="text" ng-model="ccsBusiness.erpBusinessId" style="width:50%;"  v-key="ccsBusiness.erpBusinessId" required/> <!--  v-key="ccsBusiness.erpBusinessId" -->
								<!-- <p class="information" 
								ng-show="((ccsBusiness.saleTypeCd=='SALE_TYPE_CD.PURCHASE') 
								|| (ccsBusiness.saleTypeCd=='SALE_TYPE_CD.CONSIGN' && ccsBusiness.purchaseYn=='Y')) && !ccsBusiness.erpBusinessId">
								필수 입력 항목 입니다.
								</p> -->
							</td>
							<td ng-if="loginType == 'PO'">
								{{ccsBusiness.erpBusinessId}}
							</td>
							<th>매입유형<i><spring:message code="c.input.required"/></i></th>
							<td ng-if="loginType != 'PO'"> 
								<radio-list ng-init="ccsBusiness.saleTypeCd='SALE_TYPE_CD.CONSIGN'" ng-model="ccsBusiness.saleTypeCd" code-group="SALE_TYPE_CD" required/>
							</td>
							<td ng-if="loginType == 'PO'"> 
								{{ccsBusiness.saleTypeName}}
							</td> 
						</tr>
						<tr>
							<th><spring:message code="c.ccsBusiness.business.supplyitem"/></th>
							<td ng-if="poBusinessId">{{ccsBusiness.supplyItem}}</td>
							<td ng-if="!poBusinessId"><input type="text" ng-model="ccsBusiness.supplyItem"  v-key="ccsBusiness.supplyItem" style="width:50%;"/></td>
							<th>위탁매입유형<i><spring:message code="c.input.required"/></i></th>
							<td data-ng-init="ccsBusiness.purchaseYn = 'Y'" ng-if="loginType != 'PO'"> 
								<input type="radio" ng-model="ccsBusiness.purchaseYn" ng-disabled="ccsBusiness.saleTypeCd=='SALE_TYPE_CD.PURCHASE'" value="N"/>
								<label>수수료 방식 정산</label>
								<input type="radio" ng-model="ccsBusiness.purchaseYn" ng-disabled="ccsBusiness.saleTypeCd=='SALE_TYPE_CD.PURCHASE'" value="Y"/>
								<label>매입매출 방식 정산</label>
								<p class="information" ng-show="ccsBusiness.saleTypeCd=='SALE_TYPE_CD.CONSIGN' && !ccsBusiness.purchaseYn">
									필수 입력 항목 입니다.
								</p>
								<p class="information">※상태변경 시 정산금액이 변경될 수 있습니다.</p>
							</td>
							<td ng-if="loginType == 'PO'"> 
								{{ccsBusiness.purchaseYn == 'N'?'수수료정산방식' :'매입매출 정산방식'}}
							</td>
						</tr>
						<tr>
							<th>해외구매대행허용여부<%-- <spring:message code="c.ccsBusiness.overseasPurchaseYn"/> --%><!-- 해외구매대행여부 --></th>
							<td ng-if="poBusinessId">
								{{ccsBusiness.overseasPurchaseYn == 'N'?'비허용' :'허용'}}
							</td>
							<td ng-if="!poBusinessId" colspan="3" data-ng-init="ccsBusiness.overseasPurchaseYn = 'N'">
								<input type="radio" ng-model="ccsBusiness.overseasPurchaseYn" value="Y"/><label><%-- <spring:message code="c.ccsBusiness.overseasPurchaseYn"/> --%>허용</label>
								<input type="radio" ng-model="ccsBusiness.overseasPurchaseYn" value="N"/><label><%-- <spring:message code="c.ccsBusiness.overseasPurchaseYn"/> --%>비허용</label>
							</td>
						</tr>
						<tr>
							<th>카테고리 수수료율<%-- <spring:message code="c.ccsBusiness.category"/> --%></th>
							<td colspan="3">
								<div class="exPosition" ng-show="loginType != 'PO'">
									<div >
										<label style="text-align:left">※복수의 수수료율 입력 시 ,로 구분</label>
									</div>
									<div class="btn_alignR" style="margin-top:2px;" >
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
							<th><spring:message code="c.ccsBusiness.manager.phone1"/><i><spring:message code="c.input.required"/></i></th>
							<td>
								<input type="text" tel-input ng-model="ccsBusiness.managerPhone1" v-key="ccsBusiness.managerPhone1" style="width:50%;" required/><!--  v-key="ccsBusiness.managerPhone1"  -->
							</td>
							<th><spring:message code="c.ccsBusiness.manager.phone2"/><i><spring:message code="c.input.required"/></i></th>
							<td>
								<input type="text" tel-input ng-model="ccsBusiness.managerPhone2" v-key="ccsBusiness.managerPhone2" style="width:50%;" required/> <!-- v-key="ccsBusiness.managerPhone2"  -->
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
								<input type="text" ng-model="ccsBusiness.salesName"  v-key="ccsBusiness.managerName" style="width:50%;"/>
							</td>
							<th> <spring:message code="c.ccsBusiness.manager.email"/></th>
							<td> 
								<input type="text" ng-model="ccsBusiness.salesEmail" v-key="ccsBusiness.managerEmail" style="width:50%;"/>
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
				<h3 class="sub_title2"> <spring:message code="c.ccsBusiness.title3"/></h3>
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
				<h3 class="sub_title2"> <spring:message code="c.ccsBusiness.title4"/></h3>
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
								<input type="text" ng-model="ccsBusiness.bankName" style="width:55%;"/>
							</td>
							<th> <spring:message code="c.ccsBusiness.depositorname"/></th>
							<td>
								<input type="text" ng-model="ccsBusiness.depositorName" style="width:55%;"/>
							</td>
							
						</tr>
						<tr>
							<th> <spring:message code="c.ccsBusiness.accountno"/></th>
							<td colspan="3">
								<input type="text" ng-model="ccsBusiness.accountNo" style="width:20%;"/>
							</td>
						</tr>

					</tbody>
				</table>
			</div>
			
			<div class="btn_alignC marginT3">
				<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.print()">
					<b><spring:message code="c.common.print"/></b>
				</button>
				<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
					<b><spring:message code="c.common.close"/></b>
				</button>
				<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.changeState('reject')" ng-if="((ccsBusiness.businessStateCd=='BUSINESS_STATE_CD.READY' && !insertPageFlag)) && loginType != 'PO'">
					<b><spring:message code="c.common.decline"/></b>
				</button>
				<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.changeState('approval')" ng-if="(((ccsBusiness.businessStateCd=='BUSINESS_STATE_CD.READY' || ccsBusiness.businessStateCd=='BUSINESS_STATE_CD.REJECT' || ccsBusiness.businessStateCd=='BUSINESS_STATE_CD.STOP') && !insertPageFlag)) && loginType != 'PO'">
					<b>{{ ccsBusiness.businessStateCd=='BUSINESS_STATE_CD.READY' || ccsBusiness.businessStateCd=='BUSINESS_STATE_CD.REJECT'?"승인":"운영"}}</b>
				</button>
				<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.changeState('stop')" ng-if="((ccsBusiness.businessStateCd=='BUSINESS_STATE_CD.RUN' && !insertPageFlag)) && loginType != 'PO'">
					<b><spring:message code="c.common.stop"/></b>
				</button>
				<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.updateBusiness()">
					<b><spring:message code="c.common.save"/></b>
				</button>
			</div>
	</form>
<c:choose>
<c:when test="${businessId != '' && businessId != null}">
	</article>
	<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true"></jsp:include>
</c:when>
<c:otherwise>
	</div>
	<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"></jsp:include>
</c:otherwise>		
</c:choose>