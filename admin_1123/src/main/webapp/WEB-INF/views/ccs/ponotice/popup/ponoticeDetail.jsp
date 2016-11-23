<%--
	화면명 : PO 메인  > PO 공지 상세, PO 공지사항 > 상세
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true" />
<script type="text/javascript" src="/resources/js/app/ccs.app.notice.js"></script>

<div class="wrap_popup" data-ng-app="noticeApp" data-ng-controller="ponoticeDetailController as ctrl">

	<form name="form2">
		<h2 class="sub_title1"><spring:message code="c.ccs.notice.title" /> {{flagTxt}}</h2>
		<div class="box_type1" ng-init="ctrl.detail()">

			<table class="tb_type1">
				<colgroup>
					<col width="12%" />
					<col width="41%" />
					<col width="12%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th><spring:message code="ccsNotice.title" /></th>
						<td colspan="3">
						{{ ccsNotice.title }}
						</td>
					</tr>
					<tr>
						<th><spring:message code="c.ccs.notice.detail" /></th><!-- 내용 -->
						<td colspan="3">
							<iframe name="targetCode" id="targetCode" style="width:100%; height:500px"></iframe>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b><spring:message code="c.common.close" /></b>
			</button>
		</div>
	</form>
</div>				
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>