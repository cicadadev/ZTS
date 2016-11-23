<%--
	화면명 : PO 메인
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

<article class="con_box con_on" ng-app="mainApp" ng-controller="pomainController as ctrl">
<!-- <div id="main" class="header_group" ng-controller="mainController as mainCtrl"> -->
	<div class="division_set2">
		<section>
			<div class="box_type1" ng-init="ctrl.detail()">
				<h3 class="sub_title2">
					공지사항
					<!-- <a href="#none" ng-click="ctrl.openNoticeList()" class="btn_more">more</a> -->
					<a href="#none" menubtn2 data-menu-id="59" data-pagename="공지사항 관리" data-url="/ccs/ponotice/list" class="btn_more">more</a>
				</h3>
				<ul class="list_type1">
					<li ng-repeat="notice in ccsNotices">
						<a href="#none" ng-click="ctrl.detailNotice(notice.noticeNo)">{{notice.title}}</a>
						<span>{{notice.insDt}}</span>
					</li>
				</ul>
			</div>

			<div class="box_type1 marginT2">
				<h3 class="sub_title2">
					상품 Q&amp;A
					<a href="#none" menubtn2 data-menu-id="16" data-pagename="상품Q&A 관리" data-url="/ccs/productQna/list" class="btn_more">more</a>
				</h3>
				
				<ul class="list_type1">
					<li ng-repeat="prdQna in ccsProductQnas">
						<a href="#none" ng-click="ctrl.detailProductqna(prdQna.productQnaNo)">{{prdQna.title}}</a>
						<span>{{prdQna.insDt}}</span>
					</li>
				</ul>
			</div>

			<div class="box_type1 marginT2">
				<h3 class="sub_title2">
					1:1 문의
					<a href="#none" menubtn2 data-menu-id="16" data-pagename="1:1 문의 관리" data-url="/ccs/inquiry/list" class="btn_more">more</a>
				</h3>
				<ul class="list_type1">
					<li ng-repeat="inquiry in ccsInquirys">
						<a href="#none" ng-click="ctrl.detailInquiry(inquiry.inquiryNo)">{{inquiry.title}}</a>
						<span>{{inquiry.insDt}}</span>
					</li>
				</ul>
			</div>
		</section>

		<section>
			<div class="box_type1">
				<h3 class="sub_title2">
					업무처리현황
				</h3>

				<table class="tb_type1">
					<colgroup>
						<col class="col_30p" />
						<col class="col_auto" />
					</colgroup>
					<thead>
						<tr>
							<th>오늘의 주문현황</th>
							<td>
								<em>주문금액</em> : {{ ccsOrderStates.totalOrderAmt | number }}
							</td>
						</tr>
					</thead>
					<tbody>
						<tr class="alignC">
							<th>출고대기</th>
							<td>({{ ccsOrderStates.readyCount }})건</td>
						</tr>
						<tr class="alignC">
							<th>출고지시</th>
							<td>({{ ccsOrderStates.deliveryOrderCount }})건</td>
						</tr>
						<tr class="alignC">
							<th>출고완료</th>
							<td>({{ ccsOrderStates.shipCount }})건</td>
						</tr>
						<tr class="alignC">
							<th>배송완료</th>
							<td>({{ ccsOrderStates.deliveryCount }})건</td>
						</tr>
						<tr class="alignC">
							<th>클레임(입고대기)</th>
							<td>({{ ccsOrderStates.returnOrderCount }})건</td>
						</tr>
					</tbody>
				</table>

				<table class="tb_type1 marginT2">
					<colgroup>
						<col class="col_30p" />
						<col class="col_auto" />
					</colgroup>
					<thead>
						<tr>
							<th>최근 30일 주문현황</th>
							<td>
								<em>주문금액</em> : {{ ccsOrderStates2.totalOrderAmt | number }}
							</td>
						</tr>
					</thead>
					<tbody>
						<tr class="alignC">
							<th>출고대기</th>
							<td>({{ ccsOrderStates2.readyCount }})건</td>
						</tr>
						<tr class="alignC">
							<th>출고지시</th>
							<td>({{ ccsOrderStates2.deliveryOrderCount }})건</td>
						</tr>
						<tr class="alignC">
							<th>출고완료</th>
							<td>({{ ccsOrderStates2.shipCount }})건</td>
						</tr>
						<tr class="alignC">
							<th>배송완료</th>
							<td>({{ ccsOrderStates2.deliveryCount }})건</td>
						</tr>
						<tr class="alignC">
							<th>클레임(입고대기)</th>
							<td>({{ ccsOrderStates2.returnOrderCount }})건</td>
						</tr>
					</tbody>
				</table>
			</div>

			<div class="division_set2 marginT2">
				<section>
					<table class="tb_type1">
						<colgroup>
							<col class="col_50p" />
							<col class="col_auto" />
						</colgroup>
						<thead>
							<tr class="alignC">
								<th colspan="2">최근 30일 상품 Q&amp;A현황</th>
							</tr>
						</thead>
						<tbody>
							<tr class="alignC">
								<th>문의접수</th>
								<td>({{ ccsProductQnaStates.acceptCount }})건</td>
							</tr>
							<tr class="alignC">
								<th>답변중</th>
								<td>({{ ccsProductQnaStates.answerCount }})건</td>
							</tr>
							<tr class="alignC">
								<th>답변완료</th>
								<td>({{ ccsProductQnaStates.completeCount }})건</td>
							</tr>
						</tbody>
					</table>
				</section>

				<section>
					<table class="tb_type1">
						<colgroup>
							<col class="col_50p" />
							<col class="col_auto" />
						</colgroup>
						<thead>
							<tr class="alignC">
								<th colspan="2">최근 30일 고객문의현황</th>
							</tr>
						</thead>
						<tbody>
							<tr class="alignC">
								<th>문의접수</th>
								<td>({{ ccsInquiryStates.acceptCount }})건</td>
							</tr>
							<tr class="alignC">
								<th>답변중</th>
								<td>({{ ccsInquiryStates.answerCount }})건</td>
							</tr>
							<tr class="alignC">
								<th>답변완료</th>
								<td>({{ ccsInquiryStates.completeCount }})건</td>
							</tr>
						</tbody>
					</table>
				</section>
			</div>
		</section>
	</div>

	<div class="box_type1 marginT2">
		<h3 class="sub_title2">
				품절임박 상품 목록(보완 사항)
			<span id="totalLen">
				<spring:message code="c.search.totalCount" arguments="{{ grid_product.totalItems }}" />
			</span>
			</h3>

			<div class="tb_util tb_util_rePosition">
				<button type="button" class="btn_tb_util tb_util2" ng-click="myGrid.exportExcel()">엑셀받기</button>
			</div>

		<div class="gridbox">
			<div class="grid" data-ui-grid="grid_product" 
					data-ui-grid-move-columns 
					data-ui-grid-resize-columns 
					data-ui-grid-pagination 
					data-ui-grid-auto-resize
					data-ui-grid-selection 
					data-ui-grid-exporter 
					data-ui-grid-cell-nav
					data-ui-grid-edit 
					data-ui-grid-row-edit 
					data-ui-grid-validate>
			</div>
		</div>
	</div>
</article>
<jsp:include page="/WEB-INF/views/gsf/layout/pageFooter.jsp" flush="true" />

