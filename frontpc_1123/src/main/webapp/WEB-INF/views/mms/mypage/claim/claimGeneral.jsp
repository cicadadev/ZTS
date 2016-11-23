<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags" %>
<%@ taglib prefix="func" uri="kr.co.intune.commerce.common.functions" %>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY주문관리|취소/반품/교환 조회" name="pageNavi"/>
</jsp:include>
<div class="inner">
<div class="layout_type1">
<div class="column">
<c:set var="refundProductAmt" value="0"/>
<c:set var="refundDeliveryFee" value="0"/>
<c:set var="refundWrapFee" value="0"/>
<c:set var="orderDeliveryFee" value="0"/>
<c:set var="refundProdCouponAmt" value="${claim.productCouponDcCancelAmt + claim.plusCouponDcCancelAmt + claim.orderCouponDcCancelAmt}"/>
<c:set var="refundDelvCouponAmt" value="0"/>
<c:set var="refundWrapCouponAmt" value="0"/>

	<div class="myorder claimApply ${fn:replace(fn:toLowerCase(claim.claimTypeCd), 'claim_type_cd.', '')}">
		<h3 class="title_type1">${claim.claimTypeName} 상세</h3>
		<div class="borderBox">
			<dl class="order_date">
				<dt>주문일시</dt>
				<dd>
					<b>${claim.omsOrder.orderDt}</b>
				</dd>
			</dl>
			<dl>
				<dt>클레임번호</dt>
				<dd>
					<b>${claim.omsOrder.orderId}_${claim.claimNo}</b>
					<c:if test="${claim.omsOrder.orderTypeCd == 'ORDER_TYPE_CD.REGULARDELIVERY' }">
						<span class="icon_txt1 iconPurple4_1">정기배송</span>
					</c:if>
					<c:if test="${claim.omsOrder.orderTypeCd == 'ORDER_TYPE_CD.GIFT'}">
						<span class="icon_txt1 iconPurple4_2">기프티콘</span>
					</c:if>
				</dd>
			</dl>
		</div>
		<div class="non_info">
			<div class="slide_tit1">
				<a href="javascript:void(0);" class="evt_tit">신청자정보</a>
			</div>
			<dl>
				<dt id="orderer_name">${claim.omsOrder.name1}</dt>
				<dd>
					<span>${claim.omsOrder.phone2}</span>
					<span><c:if test="${!empty claim.omsOrder.phone2 && !empty claim.omsOrder.phone1}"><i class="bar">|</i></c:if>${claim.omsOrder.phone1}</span>
					<span><c:if test="${!empty claim.omsOrder.email}"><i class="bar">|</i></c:if>${claim.omsOrder.email}</span>
				</dd>
			</dl>
		</div>
		<div class="non_info non_prod">
			<div class="slide_tit1">
				<span class="normal_tit pc_only">상품 수거지 주소</span>
				<a href="javascript:void(0);" class="evt_tit mo_only">상품 수거지 주소</a>
			</div>
			<c:forEach var="address" varStatus="status" items="${claim.omsClaimdeliverys}">
				<c:if test="${!empty address.returnZipCd}">
					<c:set var="refundDeliveryFee" value="${refundDeliveryFee + address.refundDeliveryFee}"/>
					<c:set var="refundWrapFee" value="${refundWrapFee + address.refundWrapFee}"/>
					<c:set var="orderDeliveryFee" value="${orderDeliveryFee + (address.orderDeliveryFee + address.returnDeliveryFee)}"/>
					<c:set var="refundDelvCouponAmt" value="${refundDelvCouponAmt + address.deliveryCouponDcCancelAmt}"/>
					<c:set var="refundWrapCouponAmt" value="${refundWrapCouponAmt + address.wrapCouponDcCancelAmt}"/>
					
					<dl class="delivery_area">
						<dt>${address.returnName}</dt>
						<dd>
							<span>
								${address.returnPhone2}
								${!empty address.returnPhone1 && !empty address.returnPhone2 ? ' <i class="bar">|</i> ' : ''}
								${address.returnPhone1}<br />
								<c:if test="${!empty address.returnZipCd}">
									<em>(${address.returnZipCd}) ${address.returnAddress1} ${address.returnAddress2}</em><br /> 
								</c:if>
							</span>
						</dd>
					</dl>
				</c:if>
			</c:forEach>
			
			<c:if test="${claim.claimTypeCd == 'CLAIM_TYPE_CD.EXCHANGE' || claim.claimTypeCd == 'CLAIM_TYPE_CD.REDELIVERY'}">
				<div class="slide_tit1" style="margin-top: 25px;">
					<span class="normal_tit pc_only">${claim.claimTypeName}상품 배송지 주소</span>
					<a href="javascript:void(0);" class="evt_tit mo_only">${claim.claimTypeName}상품 배송지 주소</a>
				</div>
				<c:forEach var="cp" varStatus="status" items="${claim.omsClaimproducts}">
					<c:if test="${cp.omsOrderproduct.orderDeliveryTypeCd != 'ORDER_DELIVERY_TYPE_CD.ORDER'}">
						<dl class="delivery_area">
							<dt>${cp.omsOrderproduct.omsDeliveryaddress.name1}</dt>
							<dd>
								<span>
									${cp.omsOrderproduct.omsDeliveryaddress.phone2}
									${!empty cp.omsOrderproduct.omsDeliveryaddress.phone1 && !empty cp.omsOrderproduct.omsDeliveryaddress.phone2 ? ' <i class="bar">|</i> ' : ''}
									${cp.omsOrderproduct.omsDeliveryaddress.phone1}<br />
									<c:if test="${!empty cp.omsOrderproduct.omsDeliveryaddress.zipCd}">
										<em>(${cp.omsOrderproduct.omsDeliveryaddress.zipCd}) 
										${cp.omsOrderproduct.omsDeliveryaddress.address1} 
										${cp.omsOrderproduct.omsDeliveryaddress.address2}</em><br /> 
										${cp.omsOrderproduct.omsDeliveryaddress.note}
									</c:if>
								</span>
							</dd>
							<c:if test="${claim.omsOrder.orderTypeCd == 'ORDER_TYPE_CD.GIFT'}">
								<dt>선물메세지</dt>
								<dd>
									<span>${claim.omsOrder.giftMsg}</span>
								</dd>
							</c:if>
						</dl>
					</c:if>
				</c:forEach>
			</c:if>
			
		<div class="viewTblList column3">
			<div class="div_tb_thead3">
				<div class="tr_box">
					<span class="col1">상품명/옵션정보</span>
					<span class="col2">취소수량</span>
					<span class="col3">진행상태</span>
				</div>
			</div>
			<ul class="div_tb_tbody3">
				<li>
<c:set var="trIdx" value="0"/>
<c:forEach var="cp" varStatus="idx2" items="${claim.omsClaimproducts}">
	<c:if test="${fn:contains('ORDER_PRODUCT_TYPE_CD.GENERAL,ORDER_PRODUCT_TYPE_CD.SET,ORDER_PRODUCT_TYPE_CD.SUB,ORDER_PRODUCT_TYPE_CD.WRAP', cp.omsOrderproduct.orderProductTypeCd)}">
		<c:set var="trIdx" value="${trIdx + 1}"/>
		<div class="tr_box tr_idx ${trIdx}">
			<!-- 셀 병합이 필요한 경우 rowspan > cell > vAlign 필요 -->
			<div class="col1">
				<div class="positionR">
					<div class="prod_img">
						<c:if test="${cp.omsOrderproduct.orderDeliveryTypeCd == 'ORDER_DELIVERY_TYPE_CD.ORDER'}">
							<a href="/pms/product/detail?productId=${cp.omsOrderproduct.productId}">
								<tags:prdImgTag productId="${cp.omsOrderproduct.productId}" seq="0" size="90" />
							</a>
						</c:if>
						<c:if test="${cp.omsOrderproduct.orderDeliveryTypeCd != 'ORDER_DELIVERY_TYPE_CD.ORDER'}">
							<u class="gift_txt">
								<span class="btn_tb_gift">
									<span class="icon_type1 iconBlue3">${cp.omsOrderproduct.orderDeliveryTypeName}</span>
								</span>
							</u>
						</c:if>
					</div>
					<a href="/pms/product/detail?productId=${cp.omsOrderproduct.productId}" class="title">${cp.omsOrderproduct.productName}</a>
					<c:if test="${cp.omsOrderproduct.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SET'}">
						<c:forEach var="children" varStatus="idx3" items="${claim.omsClaimproducts}">
							<c:if test="${children.omsOrderproduct.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SUB' 
								&& children.omsOrderproduct.upperOrderProductNo == cp.omsOrderproduct.orderProductNo}">
								<em class="option_txt"
									data-claim-no = "${claim.claimNo}"
									data-order-id = "${claim.orderId}">
									<i>
										<b>${children.omsOrderproduct.productName} : <span>${children.omsOrderproduct.saleproductName}</span></b>
									</i>
									<i style="float: right;">(${children.omsOrderproduct.setQty}개)</i>
								</em>
							</c:if>
						</c:forEach>
					</c:if>
					<c:if test="${cp.omsOrderproduct.orderProductTypeCd != 'ORDER_PRODUCT_TYPE_CD.SET'}">
						<em class="option_txt"
							data-claim-no = "${claim.claimNo}"
							data-order-id = "${claim.orderId}">
							<i>${cp.omsOrderproduct.saleproductName}</i>
						</em>
					</c:if>
					
					<div class="piece">
						<span class="pieceNum">1개</span>
						<span class="slash">/</span>
						<span class="piecePrice">${func:price(cp.omsOrderproduct.totalSalePrice,'')}<i>원</i></span>
					</div>
				</div>
				<%-- 상품 사은품 정보 --%>
				<c:forEach var="children" varStatus="idx3" items="${claim.omsClaimproducts}">
					<c:if test="${children.omsOrderproduct.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.PRODUCTPRESENT' 
						&& children.omsOrderproduct.upperOrderProductNo == cp.omsOrderproduct.orderProductNo}">
						<u class="gift_txt">
							<span class="btn_tb_gift">
								<span class="icon_type1 iconBlue3">사은품</span>
								${children.omsOrderproduct.productName}
							</span>
						</u>
					</c:if>
				</c:forEach>
				<%-- 선물포장 정보 --%>
				<c:if test="${product.wrapYn == 'Y' && product.orderProductTypeCd != 'ORDER_PRODUCT_TYPE_CD.WRAP'}">
					<u class="gift_txt">
						<span class="btn_tb_gift">
							<span class="icon_gift">선물포장</span>
							<small>신청</small>
						</span>
					</u>
				</c:if>
			</div>
			<div class="col2">
				<span class="price">
					<em>${cp.claimQty}</em>
				</span>
			</div>
			<div class="col3">
				<div class="stateBox">
					<strong>
						<c:if test="${cp.omsOrderproduct.orderDeliveryTypeCd == 'ORDER_DELIVERY_TYPE_CD.ORDER'}">
							${cp.claimProductStateName}
						</c:if>
						<c:if test="${cp.omsOrderproduct.orderDeliveryTypeCd != 'ORDER_DELIVERY_TYPE_CD.ORDER'}">
							${cp.omsOrderproduct.orderProductStateNote}
						</c:if>
					</strong>
				</div>
			</div>
			
			<c:set var="showClaimReason" value="${false}"/>
			<c:if test="${claim.claimTypeCd == 'CLAIM_TYPE_CD.EXCHANGE'}">
				<c:if test="${cp.omsOrderproduct.orderDeliveryTypeCd != 'ORDER_DELIVERY_TYPE_CD.ORDER'}">
					<c:set var="showClaimReason" value="${true}"/>
				</c:if>
			</c:if>
			<c:if test="${claim.claimTypeCd != 'CLAIM_TYPE_CD.EXCHANGE'}">
				<c:if test="${cp.omsOrderproduct.orderDeliveryTypeCd == 'ORDER_DELIVERY_TYPE_CD.ORDER'}">
					<c:set var="showClaimReason" value="${true}"/>
				</c:if>
			</c:if>
			
			<c:if test="${showClaimReason}">
				<div class="claimReason read">
					<div class="mo_only">
						<i class="motit">사유</i>
						<div class="reason">
							<c:if test="${cp.claimReasonCd != 'CLAIM_REASON_CD.TYPING'}">
								<p>${cp.claimReasonName}</p>
							</c:if>
							<c:if test="${cp.claimReasonCd == 'CLAIM_REASON_CD.TYPING'}">
								<p>${cp.claimReason}</p>
							</c:if>
						</div>
					</div>
					<p class="pc_only">사유 : 
						<c:if test="${cp.claimReasonCd != 'CLAIM_REASON_CD.TYPING'}">
							${cp.claimReasonName}
						</c:if>
						<c:if test="${cp.claimReasonCd == 'CLAIM_REASON_CD.TYPING'}">
							${cp.claimReason}
						</c:if>
					</p>
				</div>
			</c:if>
		</div><!-- end tr_box -->
		<c:if test="${claim.claimStateCd == 'CLAIM_STATE_CD.ACCEPT'}">
			<ul class="pc_only notice">
				<li>
					${claim.claimTypeName} 신청 접수가 정상 처리 되었습니다. <br/>
					<c:if test="${claim.claimTypeCd == 'CLAIM_TYPE_CD.EXCHANGE'}">
					고객센터에서 신청 사유 확인 후 확인 문자가 발송될 예정이며, 추가비용이 발생할 수 있습니다.
					</c:if>
					<c:if test="${claim.claimTypeCd == 'CLAIM_TYPE_CD.RETURN'}">
					상품 수거지 주소로 택배 방문 예정입니다.
					</c:if>
				</li>
			</ul>
		</c:if>
	</c:if>
</c:forEach>
<c:set var="promotionIds" value="" />
<c:forEach var="cp2" varStatus="idx2" items="${claim.omsClaimproducts}">
	<c:if test="${fn:contains('ORDER_PRODUCT_TYPE_CD.GENERAL,ORDER_PRODUCT_TYPE_CD.SUB', cp.omsOrderproduct.orderProductTypeCd)}">
		<c:set var="refundProductAmt" value="${refundProductAmt + cp.omsOrderproduct.totalSalePrice * cp.claimQty}"/>
	</c:if>
	<c:if test="${fn:contains('ORDER_PRODUCT_TYPE_CD.WRAP', cp.omsOrderproduct.orderProductTypeCd)}">
		<c:set var="refundWrapFee" value="${refundWrapFee + cp.omsOrderproduct.totalSalePrice * cp.claimQty}"/>
	</c:if>
	<c:if test="${cp2.omsOrderproduct.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.ORDERPRESENT'}">
		<c:if test="${!fn:contains(promotionIds, cp2.omsOrderproduct.presentId) && !empty cp2.omsOrderproduct.presentId}">
			<c:set var="promotionIds">${promotionIds},${cp2.omsOrderproduct.presentId}</c:set>
			<div class="tr_box tr_promo">
				<div class="promotion full">
					<strong><span class="icon_type1 iconPurple2">${claim.claimTypeName}사은품</span>${cp2.omsOrderproduct.presentName}</strong>
					<ul>
						<c:forEach var="cp3" varStatus="idx3" items="${claim.omsClaimproducts}">
							<c:if test="${cp2.omsOrderproduct.presentId == cp3.omsOrderproduct.presentId}">
								<c:if test="${(claim.claimTypeCd == 'CLAIM_TYPE_CD.CANCEL' && cp3.omsOrderproduct.cancelQty > 0) 
											|| (claim.claimTypeCd == 'CLAIM_TYPE_CD.RETURN' && cp3.omsOrderproduct.returnQty > 0)}">
									<li>
										<tags:prdImgTag productId="${cp3.omsOrderproduct.productId}" seq="0" size="60"  />
										<span>
											<em>${cp3.omsOrderproduct.productName}</em>
										</span>
									</li>
								</c:if>
							</c:if>
						</c:forEach>
					</ul>
				</div>
			</div>
		</c:if>
	</c:if>
</c:forEach>

				</li>
			</ul>
		</div>
	</div>
	<c:if test="${claim.claimTypeCd == 'CLAIM_TYPE_CD.EXCHANGE'}">
<%-- 		<c:set var="totRefundAmt" value="0"/> --%>
<%-- 		<c:forEach var="payment" varStatus="status" items="${paymentList}"> --%>
<%-- 			<c:if test="${payment.majorPaymentYn == 'Y' && payment.paymentStateCd != 'PAYMENT_STATE_CD.CANCEL'}"> --%>
<%-- 				<c:set var="totRefundAmt" value="${totRefundAmt + payment.paymentAmt}"/> --%>
<%-- 			</c:if> --%>
<%-- 		</c:forEach> --%>
		<c:if test="${claim.claimStateCd == 'CLAIM_STATE_CD.PAYMENT_READY'}">
			<ul class="notice2">
				<li>
					<strong><i>*</i>추가 결제가 필요합니다.</strong>
				</li>
			</ul>
		</c:if>
		<c:if test="${claim.omsClaimdeliverys[0].exchangeDeliveryFee > 0}">
			<div class="payment_info mtoggleBox on">
				<h4 class="sub_tit1 toggleBtn">결제정보</h4>
	
				<div class="box toggleCont">
					<div class="columnL">
						<dl>
							<dt>교환왕복배송비</dt>
							<dd>${func:price(claim.omsClaimdeliverys[0].exchangeDeliveryFee, '+')}<i>원</i></dd>
						</dl>
					</div>
	
					<div class="columnR">
						<div class="payNpoint">
							<dl class="money">
								<dt>총 결제금액</dt>
								<dd>
									${func:price(claim.omsClaimdeliverys[0].exchangeDeliveryFee, '+')}<i>원</i><br />
									<c:if test="${claim.claimStateCd == 'CLAIM_STATE_CD.PAYMENT_READY'}">
										<a href="javascript:void(0);" class="btn_sStyle1 sPurple1 btn_addPay change_method" 
											data-callback="insert"
											data-payment-no="${payment.paymentNo}"
											data-title="추가결제"
											onclick="$order.layer.payment($(this));">추가결제</a>
									</c:if>
								</dd>
							</dl>
						</div>
						<c:forEach var="payment" varStatus="status" items="${paymentList}">
							<c:if test="${payment.paymentTypeCd == 'PAYMENT_TYPE_CD.ADDPAYMENT'}">
								<ul>
									<li>
										${payment.paymentMethodName}
										<!-- 신용카드 -->
										<c:if test="${payment.paymentMethodCd == 'PAYMENT_METHOD_CD.CARD' || payment.paymentMethodCd == 'PAYMENT_METHOD_CD.KAKAO'}">
											(${payment.paymentBusinessNm}<i class="bar">  |  </i><c:if test="${payment.installmentCnt < 1}">일시불</c:if><c:if test="${payment.installmentCnt > 0}">${payment.installmentCnt}개월</c:if>)
										</c:if>
										<c:if test="${payment.paymentMethodCd == 'PAYMENT_METHOD_CD.TRANSFER'}">
											${payment.paymentBusinessNm}
										</c:if>
										<c:if test="${payment.paymentMethodCd == 'PAYMENT_METHOD_CD.MOBILE'}">
											${payment.mobilePhone}
										</c:if>
									</li>
									<c:if test="${payment.paymentMethodCd == 'PAYMENT_METHOD_CD.VIRTUAL'}">
										<li>
											<span>${payment.paymentBusinessNm} : ${payment.accountNo}</span>
										</li>
										<li>
											<span>입금자 ${payment.depositorName}&nbsp;&nbsp;|&nbsp;&nbsp;입금마감일 ${payment.virtualAccountDepositEndDt}</span>
										</li>
									</c:if>
								</ul>
							</c:if>
						</c:forEach>
					</div>
				</div>
			</div>
		</c:if>
	</c:if>
	<c:if test="${claim.claimTypeCd == 'CLAIM_TYPE_CD.CANCEL' || claim.claimTypeCd == 'CLAIM_TYPE_CD.RETURN'}">
		<div class="payment_info mtoggleBox on">
			<div class="slide_tit1">
				<%--
				<span class="normal_tit pc_only">환불정보</span>
				<a href="javascript:void(0);" class="evt_tit mo_only">환불정보</a>
				--%>
				<h4 class="sub_tit1 toggleBtn">환불정보</h4>
			</div>
			<div class="box toggleCont">
				<div class="columnL">
					<c:set var="showRefund" value="${false}"/>
					<c:set var="paymentBusinessNm" value=""/>
					<c:set var="refundAccountNo" value=""/>
					<c:set var="ccountHolderName" value=""/>
					<c:set var="totRefundAmt" value="0"/>
					<c:forEach var="payment" varStatus="status" items="${paymentList}">
						<%--
						<c:if test="${payment.majorPaymentYn == 'Y' && payment.paymentStateCd != 'PAYMENT_STATE_CD.CANCEL'}">
							<c:set var="totRefundAmt" value="${totRefundAmt + payment.paymentAmt}"/>
							<c:if test="${payment.paymentMethodCd == 'PAYMENT_METHOD_CD.VIRTUAL'}">
								<c:set var="paymentBusinessNm" value="payment.paymentBusinessNm"/>
								<c:set var="refundAccountNo" value="payment.refundAccountNo"/>
								<c:set var="ccountHolderName" value="apayment.ccountHolderName"/>
								
								<c:set var="showRefund" value="${true}"/>
							</c:if>
						</c:if>
						--%>
						
						<c:if test="${payment.claimNo == claim.claimNo}">
							<c:set var="totRefundAmt" value="${totRefundAmt + payment.paymentAmt}"/>
						</c:if>
					</c:forEach>
				
					<dl>
						<dt>상품금액 <em>(환불)</em></dt>
						<dd>${func:price(refundProductAmt + refundWrapFee, '')}<i>원</i></dd>
						<dt>배송비 <em>(환불)</em></dt>
						<dd><em class="plus">${func:price(refundDeliveryFee, '+')}</em><i>원</i></dd>
						<dt>배송비 <em>(추가)</em></dt>
						<dd><em class="plus">${func:price(orderDeliveryFee, '-')}</em><i>원</i></dd>
						<%--
						<dt>선물포장비 <em>(환불)</em></dt>
						<dd><em class="plus">${func:price(refundWrapFee, '+')}</em><i>원</i></dd>
						--%>
						<dt>총 할인금액 <em>(복원)</em></dt>
						<dd><em class="minus">${func:price(refundProdCouponAmt + refundDelvCouponAmt + refundWrapCouponAmt, '-')}</em><i>원</i></dd>
					</dl>
					<dl class="detail">
						<c:if test="${refundProdCouponAmt > 0}">
							<dt>상품할인쿠폰</dt>
							<dd><em class="minus">${func:price(refundProdCouponAmt, '-')}</em><i>원</i></dd>
						</c:if>
						<c:if test="${refundDelvCouponAmt > 0}">
							<dt>배송비무료쿠폰</dt>
							<dd><em class="minus">${func:price(refundDelvCouponAmt, '-')}</em><i>원</i></dd>
						</c:if>
						<%--
						<c:if test="${refundWrapCouponAmt > 0}">
							<dt>선물포장무료쿠폰</dt>
							<dd><em class="minus">${func:price(refundWrapCouponAmt, '-')}</em><i>원</i></dd>
						</c:if>
						--%>
						<c:forEach var="payment" varStatus="status" items="${paymentList}">
							<c:if test="${payment.claimNo == claim.claimNo}">
								<c:if test="${payment.paymentMethodCd == 'PAYMENT_METHOD_CD.POINT' || payment.paymentMethodCd == 'PAYMENT_METHOD_CD.DEPOSIT'}">
									<dt>${payment.paymentMethodName}</dt>
									<dd><em class="minus">${func:price(payment.paymentAmt, '-')}</em>${payment.paymentMethodCd == 'PAYMENT_METHOD_CD.POINT' ? 'P' : '원'}</dd>
								</c:if>
							</c:if>
						</c:forEach>
					</dl>
				</div>
				<div class="columnR">
					<div class="payNpoint">
						<dl class="money">
							<dt>총 환불금액</dt>
							<dd>${func:price(totRefundAmt, '')}<i>원</i></dd>
						</dl>
						<c:forEach var="payment" varStatus="status" items="${paymentList}">
							<c:if test="${payment.majorPaymentYn == 'Y' && payment.paymentStateCd != 'PAYMENT_STATE_CD.CANCEL'}">
								<ul>
									<li>
										${payment.paymentMethodName}
										<!-- 신용카드 -->
										<c:if test="${payment.paymentMethodCd == 'PAYMENT_METHOD_CD.CARD' || payment.paymentMethodCd == 'PAYMENT_METHOD_CD.KAKAO'}">
											(${payment.paymentBusinessNm}<i class="bar">  |  </i><c:if test="${payment.installmentCnt < 1}">일시불</c:if><c:if test="${payment.installmentCnt > 0}">${payment.installmentCnt}개월</c:if>)
										</c:if>
										<c:if test="${payment.paymentMethodCd == 'PAYMENT_METHOD_CD.TRANSFER'}">
											${payment.paymentBusinessNm}
										</c:if>
										<c:if test="${payment.paymentMethodCd == 'PAYMENT_METHOD_CD.MOBILE'}">
											${payment.mobilePhone}
										</c:if>
									</li>
									<c:if test="${payment.paymentMethodCd == 'PAYMENT_METHOD_CD.VIRTUAL'}">
										<li>
											<span>${payment.paymentBusinessNm} : ${payment.accountNo}</span>
										</li>
										<li>
											<span>입금자 ${payment.depositorName}&nbsp;&nbsp;|&nbsp;&nbsp;입금마감일 ${payment.virtualAccountDepositEndDt}</span>
										</li>
									</c:if>
								</ul>
							</c:if>
						</c:forEach>
					</div>
				</div>
			</div>
		</div>
	
		<c:if test="${showRefund}">
			<h4 class="sub_tit1">환불계좌 정보</h4>
			<ul class="txt_refund_info">
				<li>
					<strong>은행명</strong>
					<span>${paymentBusinessNm}</span>
				</li>
				<li>
					<strong>계좌번호</strong>
					<span>${refundAccountNo}</span>
				</li>
				<li>
					<strong>예금주</strong>
					<span>${ccountHolderName}</span>
				</li>
			</ul>
		</c:if>
	</c:if>
	<div class="btn_wrapC btn1ea">
		<a href="javascript:void(0);" class="btn_mStyle1 sPurple1" onclick="$order.search.history(0, 'claim');">취소/반품/교환 조회</a>
	</div>
</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
</div>
</div>
<!-- ### //일반구매 ### -->
<script type="text/javascript">
<!--
	$(function() {
		$('.lnb ul:eq(0) li:eq(1)').addClass('on');
	});
//-->
</script>