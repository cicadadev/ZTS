<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<!-- <link href="//netdna.bootstrapcdn.com/bootstrap/3.3.6/css/bootstrap.min.css" rel="stylesheet"> -->
<!-- <link href="/resources/css/bootstram-3.3.6.css" rel="stylesheet"> -->

<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true" />
<link rel="stylesheet" type="text/css" href="/resources/css/oms.css" />
<script type="text/javascript" src="/resources/js/app/oms.app.order.detail.js"></script>

<div class="wrap_popup" ng-app="orderDetailApp" ng-controller="detailCtrl as ctrl" ng-init="ctrl.init()">

	<!-- ### 1번 탭 ### -->
	<article class="con_box">
		<!-- ### 주문자 정보 ### -->
		<h2 class="sub_title1">주문 상세</h2>
		<div class="box_type1">
			<h3 class="sub_title2">
				주문자 정보
			</h3>
			<table class="tb_type1 order_info">
				<colgroup>
					<col width="15%" />
					<col width="18%" />
					<col width="15%" />
					<col width="18%" />
					<col width="15%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>회원구분</th>
						<td>{{ omsOrder.memberNo ? '회원' : '비회원' }}</td>
						<th>주문자ID</th>
						<td><a href="javascript:void(0);" ng-click="func.popup.member(omsOrder.memberNo);">{{ omsOrder.memberId }}</a></td>
						<th>주문자명</th>
						<td>{{ omsOrder.name1 }}</td>
					</tr>
					<tr>
						<th>회원등급</th>
						<td>{{ omsOrder.mmsMemberZts.memGradeName }}</td>
						<th>임직원 여부</th>
						<td>{{ omsOrder.mmsMember.employeeYn }}</td>
						<th>멤버십회원 여부</th>
						<td>{{ omsOrder.mmsMemberZts.membershipYn }}</td>
					</tr>
					<tr>
						<th>프리미엄 멤버십회원 여부</th>
						<td>{{ omsOrder.mmsMember.premiumYn }}</td>
						<th>타사B2E회원 여부</th>
						<td>{{ omsOrder.mmsMemberZts.b2eYn }}</td>
						<th>다자녀카드회원 여부</th>
						<td>{{ omsOrder.mmsMemberZts.childrenYn }}</td>
					</tr>
					<tr>
						<th>전화번호</th>
						<td>{{ omsOrder.phone1 }}</td>
						<th>휴대폰번호</th>
						<td>{{ omsOrder.phone2 }}</td>
						<th>E-mail</th>
						<td>{{ omsOrder.email }}</td>
					</tr>
					<tr ng-if="omsOrder.zipCd">
						<th>주소</th>
						<td colspan="5">
							({{ omsOrder.zipCd }}) {{ omsOrder.address1 }} {{ omsOrder.address2 }}
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- ### //주문자 정보 ### -->

		<!-- ### 주문 정보 ### -->
		<div class="box_type1" style="margin-top:20px;">
			<h3 class="sub_title2">
				주문 정보
			</h3>
			<div class="tb_util" style="display: block;">
				<button type="button" class="btn_type2 btn_type2_gray2" ng-click="popup.cancelAll()" ng-if="omsOrder.orderStateCd != 'ORDER_STATE_CD.CANCEL'" fn-id="6_CANCEL"> 
					<b>전체취소</b>
				</button>
			</div>
			<table class="tb_type1 order_info">
				<colgroup>
					<col width="15%" />
					<col width="18%" />
					<col width="15%" />
					<col width="18%" />
					<col width="15%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th>주문번호</th>
						<td>{{ omsOrder.orderId }}</td>
						<th>주문일시</th>
						<td>{{ omsOrder.orderDt }}</td>
						<th>사이트</th>
						<td>{{ omsOrder.siteName }}</td>
					</tr>
					<tr>
						<th>주문구분</th>
						<td>{{ omsOrder.orderTypeName }}</td>
						<th>주문상태</th>
						<td>{{ omsOrder.orderStateName }}</td>
						<th>제휴주문번호</th>
						<td>{{ omsOrder.siteOrderId }}</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- ### //주문 정보 ### -->

		<!-- ### 결제 정보 ### -->
		<div class="box_type1 marginT2">
			<div class="sub_title2 sub_title2_borderB">
				<h3>결제정보</h3>
				<dl class="total_money">
					<dt>결제대상금액</dt>
					<dd><i>{{ omsOrder.paymentAmt | number:0 }}</i>원</dd>
					<dt>기결제금액</dt>
					<dd><i>{{ omsOrder.orderStateCd == 'ORDER_STATE_CD.REQ' ? (omsPayments | filterSum:'paymentAmt':'paymentMethodCd':2) : 0 | number:0 }}</i>원</dd>
					<dt>미결제금액</dt>
					<dd><i>{{ omsOrder.orderStateCd == 'ORDER_STATE_CD.REQ' ? (omsOrder.paymentAmt - (omsPayments | filterSum:'paymentAmt':'paymentMethodCd':2)) : 0 | number:0 }}</i>원</dd>
				</dl>
				<div class="tb_util" style="display: block;">
					<b>총 적립포인트 : {{ omsOrder.totalPoint | number:0 }}</b>
				</div>
			</div>

			<table class="tb_type1 tb_floatL3 marginT1 width35p">
				<colgroup>
					<col width="33%" />
					<col width="30%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr class="alignC">
						<th colspan="3">결제상세</th>
					</tr>
					<tr class="order_info">
						<th>총 상품금액</th>
						<td>
							<b>{{ omsOrder.orderAmt + omsOrder.wrappingAmt | number:0 }}</b>
						</td>
						<td>&nbsp;</td>
					</tr>
					<tr class="order_info">
						<th>할인금액</th>
						<td>
							<b>{{ coupons | filterSum:'couponDcAmt':'couponTypeCd':3:'negative' | number:0 }}</b>
						</td>
						<td>
							<button type="button" class="btn_type2 tb_floatL3" ng-click="func.popup.coupon('list','etc')" ng-show="filteredEtc.length">
								<b>상세</b>
							</button>
							<div class="link_block" ng-repeat="coupon in coupons | unique:'couponId' | filterCoupon:'etc' as filteredEtc">
								<a href="javascript:void(0);" ng-click="func.popup.coupon('unit',coupon.couponId)">{{ coupon.couponId }}</a>
							</div>
						</td>
					</tr>
					<tr class="order_info">
						<th>배송비</th>
						<td>
							<b>{{ omsOrder.deliveryAmt | number:0 }}</b>
						</td>
						<td>&nbsp;</td>
					</tr>
					<tr class="order_info">
						<th>배송비쿠폰</th>
						<td>
							<b>{{ coupons | filterSum:'couponDcAmt':'couponTypeCd':4:'negative' | number:0 }}</b>
						</td>
						<td>
							<button type="button" class="btn_type2 tb_floatL3" ng-click="func.popup.coupon('list','delivery')" ng-show="filteredDelivery.length">
								<b>상세</b>
							</button>
							<div class="link_block" ng-repeat="coupon in coupons | unique:'couponId' | filterCoupon:'delivery' as filteredDelivery">
								<a href="javascript:void(0);" ng-click="func.popup.coupon('unit',coupon.couponId)">{{ coupon.couponId }}</a>
							</div>
						</td>
					</tr>
					<%--
					<tr class="order_info">
						<th>선물포장비</th>
						<td>
							<b>{{ omsOrder.wrappingAmt | number:0 }}</b>
						</td>
						<td>&nbsp;</td>
					</tr>
					<tr class="order_info">
						<th>선물포장비 쿠폰</th>
						<td>
							<b>{{ coupons | filterSum:'couponDcAmt':'couponTypeCd':5:'negative' | number:0 }}</b>
						</td>
						<td>
							<button type="button" class="btn_type2 tb_floatL3" ng-click="func.popup.coupon('list','wrap')" ng-show="filteredWrap.length">
								<b>상세</b>
							</button>
							<div class="link_block" ng-repeat="coupon in coupons | unique:'couponId' | filterCoupon:'wrap' as filteredWrap">
								<a href="javascript:void(0);" ng-click="func.popup.coupon('unit',coupon.couponId)">{{ coupon.couponId }}</a>
							</div>
						</td>
					</tr>
					--%>
				</tbody>
			</table>

			<table class="tb_type1 tb_floatL3 marginT1 width24p">
				<colgroup>
					<col width="57%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr class="alignC">
						<th colspan="2">보조결제수단</th>
					</tr>
					<tr class="order_info" ng-repeat="method in methods" ng-if="method.majorPaymentYn != 'Y' && method.paymentMethodCd != 'PAYMENT_METHOD_CD.CASH'">
						<th>
							{{ (method.paymentMethodCd).indexOf('POINT') > -1 ? '매일' : ((method.paymentMethodCd).indexOf('VOUCHER') > -1 ? '매일 모바일' : '') }}
							{{ method.paymentMethodName }}
							{{ (method.paymentMethodCd).indexOf('KAKAO') > -1 ? ' 신용카드' : '' }}
						</th>
						<td><b>{{ method.paymentAmt | number:0 }}</b></td>
					</tr>
				</tbody>
			</table>

			<table class="tb_type1 tb_floatL3 marginT1 width37p">
				<colgroup>
					<col width="38%" />
					<col width="22%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr class="alignC">
						<th colspan="3">주결제수단</th>
					</tr>
					<%--
					<tr class="order_info" ng-repeat="method in payMethods" ng-if="method.majorPaymentYn == 'Y'">
						<th>{{ method.name }}{{ (method.cd).indexOf('KAKAO') > -1 ? ' 신용카드' : '' }}</th>
						<td><b><span ng-repeat="pay in omsPayments" ng-if="pay.paymentMethodCd==method.cd">{{ pay.paymentAmt | number:0 }}</span></b></td>
						<td>
							<span ng-repeat="pay in omsPayments" ng-if="pay.paymentMethodCd==method.cd">
								{{ pay.paymentBusinessNm }}
								{{ pay.escrowYn == 'Y' ? ' / 에스크로' : '' }}
								{{ pay.interestFreeYn == 'Y' ? ' / 무이자' : '' }}
								{{ pay.installmentCnt > 1 ? ' / ' + pay.installmentCnt + ' 개월' : '' }}
							</span>
						</td>
					</tr>
					--%>
					<tr class="order_info" ng-repeat="method in methods" ng-if="method.majorPaymentYn == 'Y'">
						<th>
							{{ (method.paymentMethodCd).indexOf('POINT') > -1 ? '매일' : ((method.paymentMethodCd).indexOf('VOUCHER') > -1 ? '매일 모바일' : '') }}
							{{ method.paymentMethodName }}
							{{ (method.paymentMethodCd).indexOf('KAKAO') > -1 ? ' 신용카드' : '' }}
						</th>
						<td><b>{{ method.paymentAmt | number:0 }}</b></td>
						<td>
							{{ method.paymentBusinessNm }}
							{{ method.escrowYn == 'Y' ? ' / 에스크로' : '' }}
							{{ method.interestFreeYn == 'Y' ? ' / 무이자' : '' }}
							<span ng-if="method.paymentAmt > 0 && (method.paymentMethodCd == 'PAYMENT_METHOD_CD.KAKAO' || method.paymentMethodCd == 'PAYMENT_METHOD_CD.CARD')">
								{{ method.installmentCnt > 0 ? ' / ' + method.installmentCnt + '개월' : '일시불' }}
							</span>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
		<!-- ### //결제 정보 ### -->

		<!-- ### 주문 정보 ### -->
<!--         <div num-elementos="{{ dataGrids.length }}" class="contenedor"> -->
		<%-- // Start Address Loop --%>
		<div ng-repeat="address in omsDeliveryAddressList" class="marginT100">
			<div class="box_type1">
				<h3 class="sub_title2">
					배송 정보 {{ $index + 1 }}
				</h3>
				<div class="tb_util" style="display: block;">
					<button type="button" class="btn_type2 btn_type2_gray2" ng-click="popup.delivery(address)"  
						ng-if="omsOrder.orderStateCd != 'ORDER_STATE_CD.CANCEL'"  fn-id="6_DELIVERY"
						<%--
						ng-disabled="!delivery.availableAddress"
						ng-class="{btn_disabled:!delivery.availableAddress}"
						--%> 
						>
						<b>배송지 변경</b>
					</button>
				</div>
				<table class="tb_type1 order_info">
					<colgroup>
						<col width="15%" />
						<col width="18%" />
						<col width="15%" />
						<col width="18%" />
						<col width="15%" />
						<col width="*" />
					</colgroup>
					<tbody>
						<tr>
							<th>수취인</th>
							<td>{{ address.name1 }}</td>
							<th>수취인 휴대폰번호</th>
							<td>{{ address.phone2 }}</td>
							<th>수취인 전화번호</th>
							<td>{{ address.phone1 }}</td>
						</tr>
						<tr>
							<th>배송지</th>
							<td colspan="5">({{ address.zipCd }}) {{ address.address1 }} {{ address.address2 }}</td>
						</tr>
						<tr>
							<th>배송 메시지</th>
							<td colspan="5">{{ address.note }}</td>
						</tr>
					</tbody>
				</table>
			</div>
			<!-- ### //주문 정보 ### -->
	
			<div ng-repeat="delivery in address.omsDeliverys">
				<div class="btn_alignR marginT2" ng-if="omsOrder.orderStateCd != 'ORDER_STATE_CD.CANCEL'">
					<button type="button" class="btn_type1" ng-click="popup.claim(delivery.gridOptions,'CANCEL','주문취소')" ng-disabled="!delivery.availableCancel" ng-class="{btn_disabled:!delivery.availableCancel}" fn-id="6_BUTTON1">
						<b>주문취소</b>
					</button>
					<button type="button" class="btn_type1" ng-click="popup.claim(delivery.gridOptions,'OPTION','옵션변경')" ng-disabled="!delivery.availableOption" ng-class="{btn_disabled:!delivery.availableOption}" fn-id="6_BUTTON2">
						<b>옵션변경</b>
					</button>
					<button type="button" class="btn_type1" ng-click="popup.claim(delivery.gridOptions,'RETURN','반품')" ng-disabled="!delivery.availableReturn" ng-class="{btn_disabled:!delivery.availableReturn}" fn-id="6_BUTTON3">
						<b>반품</b>
					</button>
					<button type="button" class="btn_type1" ng-click="popup.claim(delivery.gridOptions,'EXCHANGE','교환')" ng-disabled="!delivery.availableExchange" ng-class="{btn_disabled:!delivery.availableExchange}" fn-id="6_BUTTON4">
						<b>교환</b>
					</button>
					<button type="button" class="btn_type1" ng-click="popup.claim(delivery.gridOptions,'REDELIVERY','재배송')" ng-disabled="!delivery.availableRedelivery" ng-class="{btn_disabled:!delivery.availableRedelivery}" fn-id="6_BUTTON5">
						<b>재배송</b>
					</button>
				</div>

				<div class="box_type1 marginT1">
					<h3 class="sub_title2">
						[{{ delivery.omsOrderproducts[0].saleTypeCd == 'SALE_TYPE_CD.CONSIGN' ? '업체배송 /' + delivery.omsOrderproducts[0].businessName : '센터배송' }}]
						&nbsp;{{ delivery.name }} - 배송비 : {{ (delivery.orderDeliveryFee > 0 ? (delivery.orderDeliveryFee | number:0) + ' 원' : '무료') }}
						
						<span><spring:message code="c.search.totalCount" arguments="{{ delivery.omsOrderproducts.length }}" /></span>
					</h3>
					<div class="gridbox">
						<div class="grid" 
							ng-style="{height: (delivery.omsOrderproducts.length*30)+30+17+'px'}"	<%-- row + header layer + scroll layer--%>
							ui-grid="delivery.gridOptions" 
							ui-grid-selection 
							ui-grid-auto-resize
							ui-grid-move-columns 
							ui-grid-resize-columns
							ui-grid-pinning
							></div>
							<!-- ui-grid-tree-view
							ui-grid-tree-base-expand-all-buttons -->
					</div>
				</div>
			</div>
		</div>
		<%-- // End Address Loop --%>
		
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="func.popup.close()">
				<b><spring:message code="c.common.close" /></b>
			</button>
		</div>

	</article>
	<!-- ### //1번 탭 ### -->
</div>

<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true" />