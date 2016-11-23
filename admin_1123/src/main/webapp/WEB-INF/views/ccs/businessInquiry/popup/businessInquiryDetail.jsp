 <%--
	화면명 : 입점상담 관리 > 입점상담 상세
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true" />
<script type="text/javascript" src="/resources/js/app/ccs.app.business.inquiry.manager.js"></script>

<div class="wrap_popup" ng-app="businessInquiryApp" data-ng-controller="businessInquiryDetailController as ctrl" ng-init="ctrl.detail()">
 
 	
	<form name="form2">
		<h2 class="sub_title1">상담신청서</h2>
		<div class="box_type1">
			<table class="tb_type1">
				<colgroup>
					<col width="25%" />
					<col width="41%" />
					<col width="9%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>회사명</th>
						<td colspan="3">{{ ccsBusinessinquiry.name }}</td>
					</tr>
					<tr>
						<th>전화번호</th>
						<td colspan="3">
							{{ ccsBusinessinquiry.phone | tel }}
						</td>
					</tr>
					<tr>
						<th>주소</th>
						<td colspan="3">({{ ccsBusinessinquiry.zipCd }}){{ ccsBusinessinquiry.address1 }} {{ ccsBusinessinquiry.address2 }}</td>
					</tr>
					<tr>
						<th>홈페이지 주소</th>
						<td colspan="3">
						<a style="text-decoration: underline" href="javascript:void(0);" data-ng-click="ctrl.linkUrl('www.naver.com')">{{ ccsBusinessinquiry.homepageUrl }}</a>
						</td>
					</tr>
					<tr>
						<th>담당부서</th>
						<td colspan="3">{{ ccsBusinessinquiry.depName }}</td>
					</tr>
					<tr>
						<th>담당자 이름</th>
						<td colspan="3">{{ ccsBusinessinquiry.managerName }}</td>
					</tr>
					<tr>
						<th>담당자 연락처</th>
						<td colspan="3">{{ ccsBusinessinquiry.managerPhone1 | tel}}</td>
					</tr>
					<tr>
						<th>이메일</th>
						<td colspan="3">{{ ccsBusinessinquiry.managerEmail }}</td>
					</tr>
					<tr>
						<th>내용(회사소개/제품소개)</th>
						<td colspan="3">{{ ccsBusinessinquiry.detail }}</td>
					</tr>
					<tr>
						<th>카테고리</th>
						<td colspan="3">{{ ccsBusinessinquiry.categoryNames }}</td>
					</tr>
					<tr>
						<th>첨부이미지</th>
						<td colspan="3" style="height:100px;" >
							<img ng-src="ccsBusinessinquiry.img" img-domain onError="this.src='/resources/img/bg/bg_temp_img.gif';" alt=""  style=" max-width: 100%;"> 
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="ctrl.close()">
				<b><spring:message code="c.common.close"/></b>
			</button>
		</div>
	</form>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true"/>