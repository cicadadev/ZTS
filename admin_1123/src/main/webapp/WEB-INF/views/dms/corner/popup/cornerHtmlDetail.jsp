<%--
	화면명 : 전시 관리 > 전시 코너 관리 > HTML타입 전시 코너 상세 팝업
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/dms.app.corner.manager.js"></script>
	
<body data-ng-app="cornerManagerApp" data-ng-controller="htmlDetailController as ctrl" data-ng-init="ctrl.init()">

	<div class="wrap_popup">
	<form name="form">
		<h1 class="sub_title1">HTML {{dmsDisplayitem.displayItemNo=='' || dmsDisplayitem.displayItemNo==null ? '등록' : '상세'}}</h1>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="15%">
					<col width="*">
				</colgroup>
				<tbody>
					<tr>
						<th>
							HTML 설명 <i>필수입력</i>
						</th>
						<td>
							<input type="text" ng-model="dmsDisplayitem.title" v-key="required" style="width:80%;">
						</td>
					</tr>
					<tr>
						<th>PC HTML</th>
						<td>
							<textarea ckeditor="ckPcOption" ng-model="dmsDisplayitem.html1" v-key="required"></textarea>
						</td>
					</tr>
					<tr>
						<th>MOBILE HTML</th>
						<td>
							<textarea ckeditor="ckMobileOption" ng-model="dmsDisplayitem.html2" v-key="dmsDisplayitem.html2"></textarea>
						</td>
					</tr>
					<tr>
						<th>전시기간</th>
						<td><input type="text" ng-model="dmsDisplayitem.startDt"  style="width:180px;" datetime-picker period-start/> ~ <input type="text" datetime-picker ng-model="dmsDisplayitem.endDt"  style="width:180px;" period-end/>
							<p class="information" ng-show="!dmsDisplayitem.startDt || !dmsDisplayitem.endDt">필수 입력 항목 입니다.</p>
						</td>
					</tr>
					<tr>
						<th>우선순위</th>
						<td><input type="text" ng-model="dmsDisplayitem.sortNo" style="width:130px;" v-key="dmsDisplayitem.sortNo"/></td>
					</tr>		
					<tr>
						<th>전시여부</th>
						<td>
							<radio-yn ng-model="dmsDisplayitem.displayYn" labels="전시,미전시" init-val="Y" ></radio-yn>
						</td>						
					</tr>									
				</tbody>
			</table>
		</div>
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b>닫기</b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.save()">
				<b>저장</b>
			</button>
		</div>		
		
		</form>
	</div>


<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>