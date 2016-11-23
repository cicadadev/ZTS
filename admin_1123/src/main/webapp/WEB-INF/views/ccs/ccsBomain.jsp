<%--
	화면명 : BO 메인
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/pageHeader.jsp" flush="true" />
<script type="text/javascript" src="/resources/js/app/ccs.app.main.js"></script>

<style>
.alignC {
	text-align: center;
}
.alignR {
	text-align: right;
}
.ui-grid-header-cell-row {
	text-align: center;
}
</style>

<article class="con_box con_on" ng-app="mainApp" ng-controller="bomainController as ctrl">
<!-- <div id="main" class="header_group" ng-controller="mainController as mainCtrl"> -->
	<div class="division_set2">
		<section>
			<div class="box_type1" ng-init="ctrl.detail()">
				<h3 class="sub_title2">
					공지사항
					<a href="#none" menubtn2 data-menu-id="16" data-pagename="공지사항 관리" data-url="/ccs/notice/list" class="btn_more">more</a>
				</h3>
				<ul class="list_type1">
					<li ng-repeat="notice in ccsNotices">
						<a href="#none" ng-click="ctrl.detailNotice(notice.noticeNo)">{{notice.title}}</a>
						<span>{{notice.insDt}}</span>
					</li>
				</ul>
			</div>
		</section>

		<section>
			<div class="box_type1">
				<h3 class="sub_title2">
					업무처리현황 
				</h3>
				<ul class="list_type1">
					<li style="overflow:inherit;">
						<span style="right:0px;position:relative">Q&A 미처리</span>
						<a style="position: absolute;right: 10px;" href="#none" menubtn2 data-menu-id="9" data-pagename="상품Q&A 관리" data-url="/ccs/productQna/list" class="btn_more">({{ccsProductQnaStates.uncompleteCount}})건</a>
					</li>
					<li style="overflow:inherit;">
						<span style="right:0px;position:relative">1:1상담 미처리</span>
						<a style="position: absolute;right: 10px;" href="#none" menubtn2 data-menu-id="26" data-pagename="문의 관리" data-url="/ccs/inquiry/list" class="btn_more">({{ccsInquiryStates.uncompleteCount}})건</a>
					</li>
				</ul>
			</div>
			<br/>
			
			<div class="box_type1">
				
				<h3 class="sub_title2">
					바로가기
				</h3>
				
				<table class="tb_type1">
					<colgroup>
						<col style="width:30%" />
						<col style="width:30%"/>
						<col style="width:30%"/>
					</colgroup>
					<thead>
						<tr>
							<th style="text-align:center"><a style="text-decoration:none;" href="#none" menubtn2 data-menu-id="6" data-pagename="주문조회" data-url="/oms/order/list" class="btn_more">주문조회</a></th>
							<th style="text-align:center"><a style="text-decoration:none;" href="#none" menubtn2 data-menu-id="31" data-pagename="회원정보관리" data-url="/mms/member/list" class="btn_more">회원정보관리</a></th>
							<th style="text-align:center"><a style="text-decoration:none;" href="#none" menubtn2 data-menu-id="76" data-pagename="배송 승인" data-url="/oms/logistics/delivery/approval" class="btn_more">배송승인</a></th>
						</tr>
					</thead>
				</table>
			</div>
		</section>
	</div>

	<div class="box_type1 marginT2"  style="min-height:400px">
		<h3 class="sub_title2">
				업무 담당자 정보
		</h3>
		<table class="tb_type1">
			<colgroup>
				<col class="col_auto" />
				<col class="col_auto" />
				<col class="col_auto" />
				<col class="col_auto" />
			</colgroup>
			<thead>
				<tr>
					<th style="text-align:center;border-width: 0 1px 1px 0;">
						업무영역
					</th>
					<th style="text-align:center;border-width: 0 1px 1px 0;">
						담당자
					</th >
					<th style="text-align:center;border-width: 0 1px 1px 0;">
						전화번호
					</th>
					<th style="text-align:center;border-width: 0 1px 1px 0;">
						E-mail
					</th>
				</tr>
			</thead>
			<tbody>
				<tr ng-repeat="md in ccsMds">
					<td style="text-align:center;">{{md.categoryNames.length < 15 || md.categoryNames == null ? md.categoryNames:md.categoryNames.substr(0, 15) + "..."}}
					</td>
					<td style="text-align:center;">{{md.name}}
					</td>
					<td style="text-align:center;">{{md.phone2 | tel}}
					</td>
					<td style="text-align:center;">{{md.email}}
					</td>
				</tr>
			</tbody>
		</table>
	</div>
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true" />

