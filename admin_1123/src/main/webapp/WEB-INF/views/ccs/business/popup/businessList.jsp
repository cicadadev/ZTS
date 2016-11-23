<%--
	화면명 : 업체 관리 > 업체 목록 검색 팝업
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<div class="wrap_popup" data-ng-app="ccsAppPopup" data-ng-controller="ccs_businessListApp_controller as ctrl">
	<h1 class="sub_title1">업체 검색</h1>

	<div class="box_type1">
		<table class="tb_type1">
			<colgroup>
				<col width="140" />
				<col width="35%" />
				<col width="140" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr>
					<th>업체번호<%-- <spring:message code="ccsBusiness.businessTypeCd"/> --%><!-- 거래처유형 --></th>
					<td colspan="3">	
						<input type="text" data-ng-model="search.searchBusinessId" placeholder="" style="width:50%;" />	
					</td>
					<%-- <th><spring:message code="ccsBusiness.businessStateCd"/><!-- 거래처사용여부 --></th>
					<td>						
						<select-list data-ng-model="search.businessStateCd" code-group="BUSINESS_STATE_CD" all-check ></select-list>						
					</td> --%>
				</tr>
				<tr>
					<th>업체명<%-- <spring:message code="ccsBusiness.businessTypeCd"/> --%><!-- 거래처유형 --></th>
					<td colspan="3">		
						<input type="text" data-ng-model="search.searchBusinessName" placeholder="" style="width:50%;" />										
					</td>
				</tr>
				<tr>
					<th>ERP업체ID</th><!-- ERP업체ID -->
					<td colspan="3">		
						<input type="text" data-ng-model="search.erpBusinessId" placeholder="" style="width:50%;" />										
					</td>
				</tr>				
				<%-- <tr>
					<th><spring:message code="c.ccsBusiness.businessInfo"/><!-- 거래처정보 --></th>
					<td colspan="3">
						<select data-ng-model="search.type">		
							<option value=""><spring:message code="c.select.all"/><!-- 전체 --></option>					
							<option value="NAME"><spring:message code="ccsBusiness.name"/><!-- 거래처명 --></option>
							<option value="ID"><spring:message code="ccsBusiness.businessId"/><!-- 거래처ID --></option>							
						</select>

						<input type="text" data-ng-model="search.searchKeyword" placeholder="" style="width:50%;" />
					</td>
				</tr>	 --%>			
			</tbody>
		</table>
	</div>

	<div class="btn_alignR">
		<button type="button" class="btn_type1" data-ng-click="ctrl.reset()">
			<b><spring:message code="c.search.btn.reset"/><!-- 초기화 --></b>
		</button>
		<button type="button" class="btn_type1 btn_type1_purple" data-ng-click="ctrl.searchBusiness()">
			<b><spring:message code="c.search.btn.search"/><!-- 검색 --></b>
		</button>
	</div>

	<div class="box_type1 marginT3">
		<h3 class="sub_title2">
			업체 목록
			<span><spring:message code="c.search.totalCount" arguments="{{ grid_business.totalItems }}" /></span>
		</h3>
		
		<div class="gridbox">
			<div class="grid gridbox300" data-ui-grid="grid_business"
			data-ui-grid-move-columns data-ui-grid-resize-columns data-ui-grid-pagination
			data-ui-grid-auto-resize data-ui-grid-exporter data-ui-grid-selection
			></div>
		</div>

	</div>

	<div class="btn_alignC marginT3">
		<button type="button" class="btn_type3 btn_type3_gray" data-ng-click="ctrl.close()">
			<b><spring:message code="c.common.close"/><!-- 취소 --></b>
		</button>
		<button type="button" class="btn_type3 btn_type3_purple" data-ng-click="ctrl.selectBusiness()">
			<b><spring:message code="c.common.select"/><!-- 선택 --></b>
		</button>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>
		