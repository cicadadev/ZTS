<%--
	화면명 : 주소팝업
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/ccs.app.popup.js"></script>
<link rel="stylesheet" type="text/css" href="/resources/css/address/bootstrap.min.css">
<link rel="stylesheet" type="text/css" href="/resources/css/address/bootstrap-theme.min.css">
<link rel="stylesheet" type="text/css" href="/resources/css/address/styles.css">
<style type="text/css">
	.tab_type2 li {width:50%;}
	.tab_type2 li button {width:100%;}
	.tb_type1 select {height:24px; font-size:12px;}
	.tb_type1 input[type="text"] {height:22px;}
	.tb_type1 .on td {background-color:#2e3192; color:#fff;}
	.tb_type1 .positionR {position:relative;}
	.tb_type1 .positionR button {position:absolute; right:0; top:7px;}
	.pagination {text-align:center; display:block;}
	.pagination li a {float:none;}
	.search_box {position:relative; padding-right:82px;}
	.search_box button {position:absolute; right:0; top:0; width:72px; height:26px; color:#fff; background-color:#2e3192;}
</style>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%> 

<div class="wrap_popup"  ng-app="ccsAppPopup" data-ng-controller="addressPopupController as ctrl" style="min-width:580px;">
		<h1 class="sub_title1">우편번호 검색</h1>
		<ul class="tab_type2">
			<li ng-class="{'on' : jibunTab}">
				<button type="button" ng-click="ctrl.moveTab('jibun')" name="detail">지번 검색</button>
			</li>
			<li ng-class="{'on' : roadTab}">
				<button type="button" ng-click="ctrl.moveTab('road')" name="title">도로명 검색</button>
			</li>
		</ul>
		
			
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="50%" />
					<col width="50%" />
				</colgroup>
				<tbody>
					<tr>
						<td>
							<select ng-model="search.district" style="width:100%;" ng-change="ctrl.changeDistrict(search.district)" 
												data-ng-options="district as district.regionnm for district in districtList">
								<option value="">시/도 선택</option>
							</select>
						</td>
						<td>
							<select ng-model="search.sigungu" style="width:100%;" ng-change="ctrl.changeSigungu(search.sigungu)" 
												data-ng-options="sigungu as sigungu.regionnm for sigungu in sigunguList">
								<option value="">시/군/구 선택</option>
							</select>
						</td>
					</tr>
					<tr>
						<td colspan="2">
							<div class="search_box">
								<input type="text" ng-model="search.searchKeyword" style="width:100%;" placeholder="검색어 입력" ng-keypress="ctrl.onKeyDown($event)"/>
								<button type="button" class="" ng-click="ctrl.searchAddress()">
									<b>검색</b>
								</button>
							</div>
						</td>
					</tr>
				</tbody>
			</table>
			
			<div class="marginT2" ng-if="jibunTab">
				<p>찾고 싶으신 주소의 동(읍/면) 이름을 입력해 주세요.<br>
				예) 망원동, 상암동, 수지읍</p>
			</div>
			<div class="marginT2" ng-if="roadTab">
				<p>찾고 싶으신 도로명 또는 건물명을 입력해 주세요.<br>
					예) 반포대로1, 세종대로 23, 성산로 11</p>
			</div>
		
	</div>
	
	<div class="box_type1 marginT2" ng-show="searchResult">
		<table class="tb_type1">
			<colgroup>
				<col width="30%" />
				<col width="*%" />
			</colgroup>
			<tbody id="row">
				<tr>
					<th class="alignC">우편번호</th>
					<th class="alignC">주소</th>
				</tr>
				<tr ng-if="searchResultList.length ==0">
					<td colspan="2" class="alignC">검색 결과가 없습니다.</td>
				</tr>
				<tr dir-paginate="result in ctrl.addressList |itemsPerPage:ctrl.itemsPerPage" total-items="ctrl.total_count" ng-click="ctrl.addressClick($index)">
					<td class="alignC">{{result.bsizonno}}</td>
					<td class="">
						<span ng-show='jibunTab'>
							{{result.sido}} {{result.gungu}} {{result.dong}} 
							<span ng-if="result.bunji != 0">{{result.bunji}}</span>
							<span ng-if="result.ho != 0 && result.ho != ''">- {{result.ho}}</span>
						</span>
						<span ng-show='roadTab'>
							{{result.sido}} {{result.gungu}} {{result.roadnm}} 
							<span ng-if="result.buildmn != 0">{{result.buildmn}}</span>
							<span ng-if="result.buildsn != 0 && result.buildsn != ''">- {{result.buildsn}}</span>
						</span>
					</td>
				</tr>
			</tbody>
		</table>
			<dir-pagination-controls
                   max-size="10"
                   direction-links="true"
                   boundary-links="true"
                   on-page-change="ctrl.getData(newPageNumber)" >
             </dir-pagination-controls>
		 
	</div>
	
	<div class="box_type1 marginT3" ng-show="detailInput">
		<table class="tb_type1">
			<colgroup>
				<col width="30%" />
				<col width="*" />
			</colgroup>
			<tbody>
				<tr>
					<td class="alignC">
						{{search.clickPostNo}}
					</td>
					<td>
						{{search.clickAddress}}					
					</td>
				</tr>
				<tr>
					<td colspan="2">
						<div class="search_box">
							<input type="text" data-ng-model="search.detailAddress" name="searchKeyword" style="width:100%;" placeholder="상세주소입력"/>
							<button type="button" class="" ng-click="ctrl.selectAddress()">
								<b>확인</b>
							</button>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	<div class="box_type1 marginT3" ng-show="searchSelect">
		<table class="tb_type1">
			<colgroup>
				<col width="50%" />
				<col width="50%" />
			</colgroup>
			<tbody>
				<tr>
					<td style="width:100%">
						<div class="positionR search_box">
							지번주소<br>
							{{search.selectJibunAddrText}}
							<button type="button" class="" ng-click="ctrl.confirm('jibun')">
								<b>선택</b>
							</button>
						</div>
					</td>
				</tr>
				<tr>
					<td>
						<div class="positionR search_box">
							도로명주소<br>
							{{search.selectRoadAddrText}}
							<button type="button" class="" ng-click="ctrl.confirm('road')">
								<b>선택</b>
							</button>
						</div>
					</td>
				</tr>
			</tbody>
		</table>
	</div>
	
	
	<div class="btn_alignC" style="margin-top:20px;">
		<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
			<b><spring:message code="c.common.close" /><!-- 닫기 --></b>
		</button>
	</div>
	
</div>

<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>