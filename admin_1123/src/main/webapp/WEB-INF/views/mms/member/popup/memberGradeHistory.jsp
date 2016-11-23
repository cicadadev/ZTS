<%--
	화면명 : 회원상세 > 회원 등급 변경 이력 
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"  pageEncoding="UTF-8"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true"/>
<script type="text/javascript" src="/resources/js/app/mms.app.member.manager.js"></script>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>


<div class="wrap_popup"  ng-app="memberManagerApp" data-ng-controller="mms_memberGradeHistory_controller as ctrl">
		<h2 class="sub_title1">회원등급 변경이력 <!-- 회원등급 변경이력 --></h2>	
		<div class="box_type1">
			<table class="tb_type2">
				<colgroup>
					<col width="20%" />
					<col width="20%" />
					<col width="60%" />
				</colgroup>
				<tbody>
					<tr>
						<th>변경 후 등급<!-- 변경 후 등급 --></th>
						<th>변경 전 등급<!-- 변경 전 등급 --></th>
						<th>적용 일시<!-- 적용 일시 --></th>
					</tr>
					
					<tr ng-repeat="history in mmsMemberZtsHistorys">
						<td>
							{{history.memGradeName}}
						</td>
						<td>
							{{history.preMemGradeName}}
						</td>
						<td>
							{{history.applyStartDt}}
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		

	
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b>닫기</b>
			</button>
		</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>