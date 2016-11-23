<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<jsp:include page="/WEB-INF/views/gsf/layout/popupHeader.jsp" flush="true" />
<link rel="stylesheet" type="text/css" href="/resources/css/oms.css" />
<script type="text/javascript" src="/resources/js/app/oms.app.claim.return.js"></script>
<style>
.ui-grid-viewport {
	height: auto !important;
}
</style>
<div class="wrap_popup" ng-app="claimDetailApp" ng-controller="returnCtrl as ctrl" ng-init="ctrl.init()">
	<!-- ### 1번 탭 ### -->
	<article class="con_box">
		<h2 class="sub_title1">반품 처리</h2>
		
		<div class="box_type1">
			<h3 class="sub_title2">
				[{{ omsClaimdelivery.saleTypeCd == 'SALE_TYPE_CD.CONSIGN' ? '업체배송 /' + omsClaimdelivery.businessName : '센터배송' }}]
				&nbsp;{{ omsClaimdelivery.deliveryPolicyNm }}
			</h3>
			
			<table class="tb_type1">
				<colgroup>
					<col width="*" />
					<col width="14%" />
					<col width="30%" />
					<col width="14%" />
					<col width="30%" />
				</colgroup>
	
				<tbody>
					<tr>
						<th></th>
						<th class="alignR">반품상태</th>
						<td>{{ omsClaimdelivery.omsClaim.claimStateName }}</td>
						<th class="alignR">정책배송비</th>
						<td>{{ omsClaimdelivery.deliveryFee | number:0 }}</td>
					</tr>
					<tr>
						<th rowspan="3" class="alignC">수거지정보<i><spring:message code="c.input.required" /></i></th>
						<th class="alignR">이름</th>
						<td colspan="3"><input type="text" ng-model="omsClaimdelivery.returnName" /></td>
					</tr>
					<tr>
						<th class="alignR">휴대전화</th>
						<td><input type="text" ng-model="omsClaimdelivery.returnPhone2" /></td>
						<th class="alignR">일반전화</th>
						<td><input type="text" ng-model="omsClaimdelivery.returnPhone1" /></td>
					</tr>
					<tr>
						<th class="alignR">주소</th>
						<td colspan="3">
							<div class="marginT5">
								<input type="tel" ng-model="omsClaimdelivery.returnZipCd"/>
								<button type="button" class="btn_type2" ng-click="func.popup.zip(2)">
									<b>우편번호검색</b>
								</button><br/>
							</div>
							<div class="marginT5"><input type="text" ng-model="omsClaimdelivery.returnAddress1" style="width: 95%;"/></div>
							<div class="marginT5"><input type="text" ng-model="omsClaimdelivery.returnAddress2" style="width: 95%;"/></div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>

		<div class="box_type1 marginT1">
			<h3 class="sub_title2">
				<spring:message code="c.pms.product.list" /><%-- 상품 목록 --%>
				<span><spring:message code="c.search.totalCount" arguments="{{ grid_claim.gridApi.core.getVisibleRows(grid).length }}" /></span>
			</h3>
			<div class="gridbox">
				<div class="grid" 
					ng-style="{height: (grid_claim.gridApi.core.getVisibleRows(grid).length*30)+30+17+'px'}"
					ui-grid="grid_claim" 
					ui-grid-selection ui-grid-move-columns ui-grid-edit ui-grid-row-edit ui-grid-pinning
					ui-grid-resize-columns ui-grid-auto-resize ui-grid-validate></div>
			</div>
		</div>
		<div class="btn_alignC">
			<button type="button" class="btn_type1" id="myselector" ng-click="func.setRefund('RETURN','반품')" ng-if="omsClaimdelivery.omsClaim.claimStateCd != 'CLAIM_STATE_CD.COMPLETE'">
				<b>환불계산</b>
			</button>
		</div>
		
		<!-- ### 환불 정보 ### -->
		<div class="box_type1 marginT2">
			<div class="sub_title2 sub_title2_borderB" style="padding: 0 !important;"></div>
			<table class="tb_type1 tb_floatL3 marginT1 width57p">
				<colgroup>
					<col width="24%" />
					<col width="24%" />
					<col width="28%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr class="alignC">
						<th colspan="4">환불목록</th>
					</tr>
					<tr class="alignC">
						<th>결제수단</th>
						<th>결제금액</th>
						<th>환불금액<i><spring:message code="c.input.required" /></i></th>
						<th>처리결과</th>
					</tr>
					<tr class="order_info" ng-repeat="method in tempMethods track by $index">
						<td>
							{{ (method.paymentMethodCd).indexOf('POINT') > -1 ? '매일' : ((method.paymentMethodCd).indexOf('VOUCHER') > -1 ? '매일 모바일' : '') }}
							{{ method.paymentMethodName }}
							{{ (method.paymentMethodCd).indexOf('KAKAO') > -1 ? ' 신용카드' : '' }}
						</td>
						<td>
							<b>{{ method.paymentAmt | number:0 }}</b>
						</td>
						<td>
							<span ng-if="method.paymentMethodCd != 'PAYMENT_METHOD_CD.CREADITSALE'">
								<input type="text" ng-model="method.refundAmt" format="number" class="alignR paddingR5" ng-disabled="method.paymentAmt == 0 && method.majorPaymentYn == 'Y'"/>
							</span>
							<span ng-if="method.paymentMethodCd == 'PAYMENT_METHOD_CD.CREADITSALE'">
								{{ method.refundAmt | number:0 }}&nbsp;
							</span>
						</td>
						<td style="text-align: center;">{{ method.paymentStateName }}</td>
					</tr>
					
					<tr class="order_info">
						<td>할인(상품)</td>
						<td>
							<button type="button" class="btn_type2 tb_floatL3" ng-click="func.popup.coupon('list','etc')" ng-show="filteredEtc.length">
								<b>상세</b>
							</button>
							<div class="ui-grid-invisible" ng-repeat="coupon in coupons | unique:'couponId' | filterCoupon:'etc' as filteredEtc">
								<a href="javascript:void(0);" ng-click="func.popup.coupon('unit',coupon.couponId)">{{ coupon.couponId }}</a>
							</div>
							<b>{{ (coupons | filterSum:'couponDcAmt':'couponTypeCd':3) - sumCancelCouponProduct | number:0 }}</b>
						</td>
						<td ng-if="omsClaimdelivery.omsClaim.claimStateCd == 'CLAIM_STATE_CD.COMPLETE'">{{ claimCancelCouponProduct | number:0 }}&nbsp;</td>
						<td ng-if="omsClaimdelivery.omsClaim.claimStateCd != 'CLAIM_STATE_CD.COMPLETE'">{{ claimCoupons | filterSum:'refundCouponAmt':'couponTypeCd':3 | number:0 }}&nbsp;</td>
						<td>{{ method.process }}</td>
					</tr>
					<tr class="order_info">
						<td>할인(배송비)</td>
						<td>
							<button type="button" class="btn_type2 tb_floatL3" ng-click="func.popup.coupon('list','delivery')" ng-show="filteredDelivery.length">
								<b>상세</b>
							</button>
							<div class="ui-grid-invisible" ng-repeat="coupon in coupons | unique:'couponId' | filterCoupon:'delivery' as filteredDelivery">
								<a href="javascript:void(0);" ng-click="func.popup.coupon('unit',coupon.couponId)">{{ coupon.couponId }}</a>
							</div>
							<b>{{ (coupons | filterSum:'couponDcAmt':'couponTypeCd':4) - sumCancelCouponDelivery | number:0 }}</b>
						</td>
						<td ng-if="omsClaimdelivery.omsClaim.claimStateCd == 'CLAIM_STATE_CD.COMPLETE'">{{ calimCancelCouponDelivery | number:0 }}&nbsp;</td>
						<td ng-if="omsClaimdelivery.omsClaim.claimStateCd != 'CLAIM_STATE_CD.COMPLETE'">{{ claimCoupons | filterSum:'refundCouponAmt':'couponTypeCd':4 | number:0 }}&nbsp;</td>
						<td>{{ method.process }}</td>
					</tr>
					<%--20161114:포장지변경
					<tr class="order_info">
						<td>할인(선물포장비)</td>
						<td>
							<button type="button" class="btn_type2 tb_floatL3" ng-click="func.popup.coupon('list','wrap')" ng-show="filteredWrap.length">
								<b>상세</b>
							</button>
							<div class="ui-grid-invisible" ng-repeat="coupon in coupons | unique:'couponId' | filterCoupon:'wrap' as filteredWrap">
								<a href="javascript:void(0);" ng-click="func.popup.coupon('unit',coupon.couponId)">{{ coupon.couponId }}</a>
							</div>
							<b>{{ (coupons | filterSum:'couponDcAmt':'couponTypeCd':5) - sumCancelCouponWrap | number:0 }}</b>
						</td>
						<td ng-if="omsClaimdelivery.omsClaim.claimStateCd == 'CLAIM_STATE_CD.COMPLETE'">{{ claimCancelCouponWrap | number:0 }}&nbsp;</td>
						<td ng-if="omsClaimdelivery.omsClaim.claimStateCd != 'CLAIM_STATE_CD.COMPLETE'">{{ claimCoupons | filterSum:'refundCouponAmt':'couponTypeCd':5 | number:0 }}&nbsp;</td>
						<td>{{ method.process }}</td>
					</tr>
					--%>
					<tr class="order_info">
						<th><b>총금액</b></th>
						<th>
							<b ng-if="omsClaimdelivery.omsClaim.claimStateCd == 'CLAIM_STATE_CD.COMPLETE'">
								{{ (tempMethods | filterSum:'paymentAmt':'paymentMethodCd':1) + (coupons | filterSum:'couponDcAmt':'couponTypeCd':1) - (sumCancelCouponProduct + sumCancelCouponDelivery + sumCancelCouponWrap) | number:0 }}
							</b>
							<b ng-if="omsClaimdelivery.omsClaim.claimStateCd != 'CLAIM_STATE_CD.COMPLETE'">
								{{ (tempMethods | filterSum:'paymentAmt':'paymentMethodCd':1) + (coupons | filterSum:'couponDcAmt':'couponTypeCd':1) - (coupons | filterSum:'couponDcCancelAmt':'couponTypeCd':1) | number:0 }}
							</b>
						</th>
						<th style="padding-right: 6px;">
							<b ng-if="omsClaimdelivery.omsClaim.claimStateCd == 'CLAIM_STATE_CD.COMPLETE'">
								{{ (tempMethods | filterSum:'refundAmt':'paymentMethodCd':1)  + (claimCancelCouponProduct + calimCancelCouponDelivery + claimCancelCouponWrap) | number:0 }}
							</b>
							<b ng-if="omsClaimdelivery.omsClaim.claimStateCd != 'CLAIM_STATE_CD.COMPLETE'">
								{{ (tempMethods | filterSum:'refundAmt':'paymentMethodCd':1)  + (claimCoupons | filterSum:'refundCouponAmt':'couponTypeCd':1) | number:0 }}
							</b>&nbsp;
						</th>
						<th></th>
					</tr>
					<%--
					<tr>
						<td colspan="4"></thtd>
					</tr>
					<tr class="alignC">
						<th colspan="4">추가비용</th>
					</tr>
					<tr class="order_info">
						<td>원배송비</td>
						<td></td>
						<td><b><input type="text" ng-model="omsClaimdelivery.orderDeliveryFee" format="number" class="alignR paddingR5"/></b></td>
						<td></td>
					</tr>
					<tr class="order_info">
						<td>반품배송비</td>
						<td></td>
						<td><b><input type="text" ng-model="omsClaimdelivery.returnDeliveryFee" format="number" class="alignR paddingR5"/></b></td>
						<td></td>
					</tr>
					<tr class="order_info">
						<th><b>총금액</b></th>
						<th></th>
						<th><b>{{ -(omsClaimdelivery.orderDeliveryFee + omsClaimdelivery.returnDeliveryFee) | number:0 }}</b>&nbsp;</th>
						<th></th>
					</tr>
					<tr>
						<td colspan="4"></thtd>
					</tr>
					<tr class="alignC">
						<th colspan="4">총 환불금액</th>
					</tr>
					<tr class="order_info">
						<th><b>총금액</b></th>
						<th></th>
						<th style="padding-right: 6px;">
							<b ng-if="omsClaimdelivery.omsClaim.claimStateCd == 'CLAIM_STATE_CD.COMPLETE'">
								{{ (tempMethods | filterSum:'refundAmt':'paymentMethodCd':1)  + (claimCancelCouponProduct + calimCancelCouponDelivery + claimCancelCouponWrap) 
									- (omsClaimdelivery.orderDeliveryFee + omsClaimdelivery.returnDeliveryFee)
								| number:0 }}
							</b>
							<b ng-if="omsClaimdelivery.omsClaim.claimStateCd != 'CLAIM_STATE_CD.COMPLETE'">
								{{ (tempMethods | filterSum:'refundAmt':'paymentMethodCd':1)  + (claimCoupons | filterSum:'refundCouponAmt':'couponTypeCd':1) 
									- (omsClaimdelivery.orderDeliveryFee + omsClaimdelivery.returnDeliveryFee)
								| number:0 }}
							</b>&nbsp;
						</th>						
						<th></th>
					</tr>
					--%>
				</tbody>
			</table>
			
			<table class="tb_type1 tb_floatL3 marginT1 width37p">
				<colgroup>
					<col width="40%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr>
						<th colspan="2" class="paddingL24">
							환불계좌
							<button type="button" class="btn_type2 btn_type2 tb_floatR3" ng-click="func.accountCertify(tempMethods[9])" ng-if="!tempMethods[9].accountAuthDt">
								<b>계좌인증</b>
							</button>
						</th>
					</tr>
					<tr class="order_info">
						<td>은행</td>
						<td>
							<select ng-model="tempMethods[9].paymentBusinessCd" ng-change="func.setBankName(tempMethods[9], banks);">
								<option value="">선택하세요</option>
								<option ng-repeat="b in banks" value="{{ b.cd }}">{{ b.name }}</option>
							</select>
							<input type="hidden" ng-model="tempMethods[9].paymentBusinessNm" /><!-- ng-value="bank.name" -->
						</td>
					</tr>
					<tr class="order_info">
						<td>계좌번호</td>
						<td><b><input type="text" ng-model="tempMethods[9].refundAccountNo" number-only class="alignR paddingR5"/></b></td>
					</tr>
					<tr class="order_info">
						<td>예금주</td>
						<td><b><input type="text" ng-model="tempMethods[9].accountHolderName" class="alignR paddingR5"/></b></td>
					</tr>
				</tbody>
			</table>
			
			<table class="tb_type1 tb_floatL3 marginT1 width37p">
				<colgroup>
					<col width="40%" />
					<col width="*" />
				</colgroup>
				<tbody>
					<tr class="alignC">
						<th colspan="2">추가비용</th>
					</tr>
					<tr class="order_info">
						<td>원배송비</td>
						<td><b><input type="text" ng-model="omsClaimdelivery.orderDeliveryFee" format="number" class="alignR paddingR5" ng-change="func.setAddDelvFee()"/></b></td>
					</tr>
					<tr class="order_info">
						<td>반품배송비</td>
						<td><b><input type="text" ng-model="omsClaimdelivery.returnDeliveryFee" format="number" class="alignR paddingR5" ng-change="func.setAddDelvFee()"/></b></td>
					</tr>
					<tr class="order_info">
						<th><b>총금액</b></th>
						<th><b>{{ (+omsClaimdelivery.orderDeliveryFee) + (+omsClaimdelivery.returnDeliveryFee) | number:0 }}</b>&nbsp;</th>
					</tr>
				</tbody>
			</table>
		</div>
		
		<div class="btn_alignC marginT3">
			<button type="button" class="btn_type3 btn_type3_gray" ng-click="func.popup.close()">
				<b><spring:message code="c.common.close" /></b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-show="showButton == 1" ng-click="ctrl.insert('RETURN','ACCEPT')" ng-if="!omsClaimdelivery.claimNo">
				<b>반품접수</b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-show="showButton == 1" ng-click="ctrl.update('RETURN','ACCEPT','접수')" ng-if="omsClaimdelivery.claimNo && omsClaimdelivery.omsClaim.claimStateCd == 'CLAIM_STATE_CD.REQ'">
				<b>반품접수</b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-show="showButton == 1" ng-click="ctrl.update('RETURN','REJECT','반려')" ng-if="omsClaimdelivery.claimNo && omsClaimdelivery.omsClaim.claimStateCd == 'CLAIM_STATE_CD.REQ'">
				<b>반품반려</b>
			</button>
			
			<button type="button" class="btn_type3 btn_type3_purple" ng-show="showButton == 1" ng-click="ctrl.update('RETURN','COMPLETE','완료')" ng-if="returnCompleted == '1' && omsClaimdelivery.omsClaim.claimStateCd == 'CLAIM_STATE_CD.ACCEPT'">
				<b>반품완료</b>
			</button>
			<button type="button" class="btn_type3 btn_type3_purple" ng-show="showButton == 1" ng-click="ctrl.update('RETURN','WITHDRAW','철회')" ng-if="returnStatus == '-1' && omsClaimdelivery.omsClaim.claimStateCd == 'CLAIM_STATE_CD.ACCEPT'">
				<b>반품철회</b>
			</button>
		</div>
		
	</article>
	<!-- ### //1번 탭 ### -->
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/popupFooter.jsp" flush="true" />