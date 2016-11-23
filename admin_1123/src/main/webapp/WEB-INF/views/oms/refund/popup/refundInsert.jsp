<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true" />
<link rel="stylesheet" type="text/css" href="/resources/css/oms.css" />
<script type="text/javascript" src=/resources/js/app/oms.app.refund.list.js></script>

<div class="wrap_popup" ng-app="refundListApp" ng-controller="regCtrl as ctrl" ng-init="ctrl.init()">
	<!-- ### 1번 탭 ### -->
	<article class="con_box">
		<h2 class="sub_title1">
			환불등록
		</h2>
	
		<div class="box_type1 marginT3">
			<form name="deliveryForm">
				<table class="tb_type1">
					<colgroup>
						<col width="20%" />
						<col width="*" />
					</colgroup>
		
					<tbody>
						<tr>
							<th>예치금 환불 회원<i><spring:message code="c.input.required" /></i></th>
							<td>
								<input type="text" ng-model="omsPayment.mmsDeposit.mmsMember.memberName" required />
								<input type="hidden" ng-model="omsPayment.memberNo" />
								<button type="button" class="btn_type2" ng-click="func.popup.member('')">
									<b><spring:message code="c.search.btn.search"/></b>
								</button>
								<button type="button" class="btn_eraser" ng-click="ctrl.eraser('member')">지우개</button>
							</td>
						</tr>
						<tr>
							<th>환불금액<i><spring:message code="c.input.required" /></i></th>
							<td>
								<input type="text" ng-model="omsPayment.paymentAmt" number-only required />
								&nbsp;&nbsp;&nbsp;보유예치금 : {{ omsPayment.mmsDeposit.balanceAmt | number }}
							</td>
						</tr>
						<tr>
							<th>계좌정보<i><spring:message code="c.input.required" /></i></th>
							<td>
								은행&nbsp;
								<select ng-model="omsPayment.paymentBusinessCd" ng-change="ctrl.setBankName(omsPayment, banks)">
									<option value="">선택하세요</option>
									<option ng-repeat="b in banks" value="{{ b.cd }}">{{ b.name }}</option>
								</select><input type="hidden" ng-model="omsPayment.paymentBusinessNm" />
								&nbsp;&nbsp;&nbsp;
								
								계좌번호&nbsp;
								<input type="text" ng-model="omsPayment.refundAccountNo" number-only style="width: 150px;" />
								&nbsp;&nbsp;&nbsp;
								
								예금주&nbsp;
								<input type="text" ng-model="omsPayment.accountHolderName" style="width: 150px;" />
								
								<button type="button" class="btn_type2 btn_type2 tb_floatR3" ng-click="func.accountCertify(omsPayment, true)" ng-if="!omsPayment.accountAuthDt">
									<b>계좌인증</b>
								</button>
							</td>
						</tr>
					</tbody>
				</table>
			</form>
		</div>
	
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="func.popup.close()">
				<b><spring:message code="c.common.close" /></b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-click="ctrl.save()">
				<b><spring:message code="c.common.save" /></b>
			</button>
		</div>

	</article>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true" />
