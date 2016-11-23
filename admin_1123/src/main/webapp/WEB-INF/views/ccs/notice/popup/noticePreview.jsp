<%--
	화면명 : 공지 관리 > 공지 상세 > 미리보기 팝업
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true" />
<script type="text/javascript" src="/resources/js/app/ccs.app.notice.js"></script>

<div class="wrap_popup" data-ng-app="noticeApp" data-ng-controller="noticePreviewController as ctrl">
	<form name="form2">
		<h2>공지사항</h2>​
		<div class="box_type1 marginT1" ng-init="ctrl.init()">
			<iframe name="targetCode" id="targetCode"></iframe></td>
		</div>
		
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" data-ng-click="ctrl.close()">
				<b>닫기</b>
			</button>
		</div>
	</form>
</div>				
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>