<%--
	화면명 : 팝업 관리 > 팝업 상세 > 미리보기 팝업
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true" />
<script type="text/javascript" src="/resources/js/app/ccs.app.popupnotice.js"></script>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<script type="text/javascript">


</script>

<div class="wrap_popup" data-ng-app="popupnoticeApp" data-ng-controller="popupPreviewController as ctrl">
	<form name="form2">
		<h2>팝업 PC</h2>​
		<div class="box_type1 marginT1" ng-init="ctrl.init()")>
			<iframe name="targetCode" id="targetCode" style="width: 100%; height: 500px;"></iframe></td>
		</div>
		<!-- <h2>팝업 MOBILE</h2>
		<div class="box_type1 marginT1" ng-init="ctrl.init2()")>
			<iframe name="targetCode2" id="targetCode2"></iframe></td>
		</div>
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" data-ng-click="ctrl.close()">
				<b>닫기</b>
			</button>
		</div> -->
		<span style="font-size:9pt;">&nbsp;<input type="checkbox" id="noticePopupCheck" name="Notice">오늘 하루 이 창을 열지 않음
		</span>
		
		<span style="font-size:9pt; border=" 0">
			<button name="mainSave" type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.closePop('done')">
				<b>닫기</b>
			</button>
		</span>
	</form>
</div>			
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>