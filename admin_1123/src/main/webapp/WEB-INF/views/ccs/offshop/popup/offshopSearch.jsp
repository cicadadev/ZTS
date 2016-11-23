<%--
	화면명 : 사용자 관리 > 매장검색 팝업
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>

<div class="wrap_popup" data-ng-app="ccsAppPopup" data-ng-controller="ccs_offshopListPopupController as ctrl">
	<form name="form2">
		<h2 class="sub_title1">오프라인 매장 검색</h2>
		
			<div class="box_type1">
				<table class="tb_type1">
					<colgroup>
						<col width="9%" />
						<col width="34%" />
						<col width="9%" />
						<col width="*" />
					</colgroup>
					<tbody>
						<tr>
							<th>매장유형</th>
							<td>
								<select ng-model="search.offshopTypeCd" select-code="OFFSHOP_TYPE_CD" style="width:150px;">
									<option value="">선택하세요</option>
									<option ng-selected="search.offshopTypeCd" value="{{ search.offshopTypeCd }}"></option>
								</select>
							</td>
						</tr>
						<tr>
							<th>매장명</th>
							<td colspan="4"><input type="tel" id="" value="" placeholder="" data-ng-model="search.offName"/></td>
						</tr>						
					</tbody>
				</table>
			</div>

			<div class="btn_alignR">
				<button type="button" data-ng-click="ctrl.reset()" class="btn_type1">
					<b><spring:message code="c.search.btn.reset" /></b>
				</button>
				<button type="button" data-ng-click="ctrl.searchGrid()" class="btn_type1 btn_type1_purple">
					<b><spring:message code="c.search.btn.search" /></b>
				</button>
			</div>
			
		<div class="box_type1 marginT3">
			<h3 class="sub_title2">
				오프라인매장 목록
				<span><spring:message code="c.search.totalCount" arguments="{{ grid_offshop.totalItems }}"></spring:message></span>
			</h3>
			
			<div class="gridbox">
				<div class="grid" data-ui-grid="grid_offshop"   
						data-ui-grid-move-columns 
						data-ui-grid-resize-columns 
						data-ui-grid-pagination
						data-ui-grid-auto-resize 
						data-ui-grid-exporter 
						data-ui-grid-selection></div>
				</div>
		</div>
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" data-ng-click="ctrl.close()">
				<b>닫기</b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" data-ng-click="ctrl.selectOffshop()">
				<b><spring:message code="c.common.select"/><!-- 선택 --></b>
			</button>
		</div>
	</form>
</div>				
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>