<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags" %>
<%@ taglib prefix="func" uri="kr.co.intune.commerce.common.functions" %>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY주문관리|주문내역" name="pageNavi"/>
</jsp:include>
<div class="inner">
<div class="layout_type1">
<div class="column">
	<div class="myorder myorderDetail">
		<h3 class="title_type1">주문/배송 상세</h3>
		<div class="borderBox">
			<dl class="order_date">
				<dt>주문일시</dt>
				<dd>
					<b>${order.orderDt}</b>
				</dd>
			</dl>
			<dl>
				<dt>주문번호</dt>
				<dd>
					<b>${order.orderId}</b>
					<c:if test="${order.orderTypeCd == 'ORDER_TYPE_CD.REGULARDELIVERY'}">
						<span class="icon_txt1 iconPurple4_1">정기배송</span>
					</c:if>
					<c:if test="${order.orderTypeCd == 'ORDER_TYPE_CD.GIFT'}">
						<span class="icon_txt1 iconPurple4_2">기프티콘</span>
					</c:if>
				</dd>
			</dl>
			<c:if test="${order.cancelAllYn == 'Y'}">
				<div class="btn_receipt">
					<a href="javascript:void(0);" class="btn_sStyle1"
						data-order-id="${order.orderId}"
						data-order-product-no=""
						data-claim-type="cancel"
						onclick="$claim.request($(this), true);">주문취소</a>
				</div>
			</c:if>
		</div>
		<div class="non_info">
			<div class="slide_tit1 slideHide">
				<a href="javascript:void(0);" class="evt_tit">주문자정보</a>
			</div>
			<dl>
				<dt id="orderer_name">${order.name1}</dt>
				<dd>
					<span>${order.phone2}</span>
					<span><c:if test="${!empty order.phone2 && !empty order.phone1}"><i class="bar">|</i></c:if>${order.phone1}</span>
					<span><c:if test="${!empty order.email}"><i class="bar">|</i></c:if>${order.email}</span>
				</dd>
			</dl>
		</div>
		<div class="non_info non_prod">
			<div class="slide_tit1">
				<span class="normal_tit pc_only">배송/상품정보</span>
				<a href="javascript:void(0);" class="evt_tit mo_only">배송/상품정보</a>
			</div>
			
<c:set var="changeDelivery" value="${true}"/>
<c:forEach var="product" varStatus="idx2" items="${order.omsOrderproducts}">
	<c:if test="${product.deliveryChangeYn != 'Y'}"><c:set var="changeDelivery" value="${false}"/></c:if>
</c:forEach>
<c:forEach var="address" varStatus="status" items="${order.omsDeliveryaddresss}">
	<dl class="delivery_area">
		<dt>${address.name1}</dt>
		<dd>
			<span>
				${address.phone2}${!empty address.phone1 && !empty address.phone2 ? ' <i class="bar">|</i> ' : ''}${address.phone1}<br />
				<c:if test="${!empty address.zipCd}">
					<em>(${address.zipCd}) ${address.address1} ${address.address2}</em><br /> 
					${address.note}
				</c:if>
			</span>
			<c:if test="${changeDelivery}">
				<a href="javascript:void(0);" class="btn_sStyle1 btn_changeAddr"
					data-order-id = "${address.orderId}" 
					data-delivery-address-no = "${address.deliveryAddressNo}" 
					data-name1 = "${address.name1}" 
					data-phone1 = "${address.phone1}"
					data-phone2 = "${address.phone2}"
					data-email = "${address.email}"
					data-zip-cd = "${address.zipCd}"
					data-address1 = "${address.address1}"
					data-address2 = "${address.address2}"
					data-address3 = "${address.address3}"
					data-note = "${address.note}"
					data-member-id = "${order.memberId}"
					onclick="$order.change.delivery($(this));">배송지변경</a>
			</c:if>
		</dd>
		<c:if test="${order.orderTypeCd == 'ORDER_TYPE_CD.GIFT'}">
			<dt>선물메세지</dt>
			<dd>
				<span>${order.giftMsg}</span>
			</dd>
		</c:if>
	</dl>
</c:forEach>
			
			<div class="div_tb_thead3">
				<div class="tr_box">
					<span class="col2">상품명/옵션정보/수량</span>
					<span class="col3">구매금액</span>
					<span class="col4">진행상태</span>
				</div>
			</div>
			<ul class="div_tb_tbody3">
				<li>
<c:set var="trIdx" value="0"/>
<c:forEach var="product" varStatus="idx2" items="${order.omsOrderproducts}">
	<c:if test="${fn:contains('ORDER_PRODUCT_TYPE_CD.GENERAL,ORDER_PRODUCT_TYPE_CD.SET,ORDER_PRODUCT_TYPE_CD.WRAP', product.orderProductTypeCd)}">
		<c:set var="trIdx" value="${trIdx + 1}"/>
		<c:set var="isSet" value="${false}"/>
		<div class="tr_box tr_idx ${trIdx}">
			<!-- 셀 병합이 필요한 경우 rowspan > cell > vAlign 필요 -->
			<div class="colspan cols2">
				<div class="col2">
					<div class="positionR">
						<c:if test="${product.orderProductTypeCd != 'ORDER_PRODUCT_TYPE_CD.WRAP'}">
							<div class="prod_img">
								<c:if test="${product.orderDeliveryTypeCd == 'ORDER_DELIVERY_TYPE_CD.ORDER'}">
									<a href="/pms/product/detail?productId=${product.productId}">
										<tags:prdImgTag productId="${product.productId}" seq="0" size="90" />
									</a>
								</c:if>
								<c:if test="${product.orderDeliveryTypeCd != 'ORDER_DELIVERY_TYPE_CD.ORDER'}">
									<u class="gift_txt">
										<span class="btn_tb_gift">
											<span class="icon_type1 iconBlue3">${product.orderDeliveryTypeName}</span>
										</span>
									</u>
								</c:if>
							</div>
							<a href="/pms/product/detail?productId=${product.productId}" class="title">${product.productName}</a>
							<c:if test="${product.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SET'}">
								<c:set var="isSet" value="${true}"/>
								<c:forEach var="children" varStatus="idx3" items="${order.omsOrderproducts}">
									<c:if test="${children.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SUB' && children.upperOrderProductNo == product.orderProductNo}">
										<em class="option_txt" 
											data-order-id="${order.orderId}"
											data-order-product-no="${children.orderProductNo}"
											data-product-id="${children.productId}"
											data-saleproduct-id="${children.saleproductId}"
											data-add-sale-price="${children.addSalePrice}"
					
											data-order-qty="${children.orderQty}"
											data-cancel-qty="${children.cancelQty}"
											data-return-qty="${children.returnQty}">
											<i>
												<b>
													<c:if test="${children.orderDeliveryTypeCd != 'ORDER_DELIVERY_TYPE_CD.ORDER'}">
														<span style="color: red;">(${children.orderDeliveryTypeName})</span> 
													</c:if>
													${children.productName} : <span>${children.saleproductName}</span>
												</b>
											</i>
											<i style="float: right;">(${children.setQty}개)</i>
										</em>
									</c:if>
								</c:forEach>
							</c:if>
							<c:if test="${product.orderProductTypeCd != 'ORDER_PRODUCT_TYPE_CD.SET'}">
								<em class="option_txt"
									data-order-id="${order.orderId}"
									data-order-product-no="${product.orderProductNo}"
									data-product-id="${product.productId}"
									data-saleproduct-id="${product.saleproductId}"
									data-add-sale-price="${product.addSalePrice}"
			
									data-order-qty="${product.orderQty}"
									data-cancel-qty="${product.cancelQty}"
									data-return-qty="${product.returnQty}">
									<i>${product.saleproductName}</i>
								</em>
							</c:if>
						</c:if>
						<c:if test="${product.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.WRAP'}">
							<div class="prod_img">
								<c:if test="${product.orderDeliveryTypeCd == 'ORDER_DELIVERY_TYPE_CD.ORDER'}">
									<tags:prdImgTag productId="${product.productId}" seq="0" size="90" />
								</c:if>
								<c:if test="${product.orderDeliveryTypeCd != 'ORDER_DELIVERY_TYPE_CD.ORDER'}">
									<u class="gift_txt">
										<span class="btn_tb_gift">
											<span class="icon_type1 iconBlue3">${product.orderDeliveryTypeName}</span>
										</span>
									</u>
								</c:if>
							</div>
							선물포장지
						</c:if>
						<div class="piece">
							<span class="pieceNum">${product.orderQty}개</span>
							<span class="slash">/</span>
							<span class="piecePrice">${func:price(product.totalSalePrice,'')}<i>원</i></span>
						</div>
					</div>
					<%-- 상품 사은품 정보 --%>
					<c:forEach var="children" varStatus="idx3" items="${order.omsOrderproducts}">
						<c:if test="${children.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.PRODUCTPRESENT' && children.upperOrderProductNo == product.orderProductNo}">
							<u class="gift_txt">
								<span class="btn_tb_gift">
									<span class="icon_type1 iconBlue3">사은품</span>
									${children.productName}
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
				<div class="col3">
					<span class="price">
						<em>${func:price(product.totalSalePrice * product.orderQty,'')}<i>원</i></em>
					</span>
				</div>
				<div class="col4">
					<div class="stateBox"
						data-order-id="${order.orderId}"
						data-order-product-no="${product.orderProductNo}"
						data-order-product-type-cd="${product.orderProductTypeCd}"
						data-product-id = "${product.productId}"
						data-product-name = "${product.productName}"
						data-saleproduct-id = "${product.saleproductId}"
						data-saleproduct-name = "${product.saleproductName}"
						data-tr-idx="${trIdx}">
						<strong>
							${product.orderProductStateNote}
							<c:if test="${order.orderTypeCd == 'ORDER_TYPE_CD.GIFT' && order.orderStateCd == 'ORDER_STATE_CD.PAYED'}">
								<i>(배송지입력대기)</i>
							</c:if>
						</strong>
						<c:if test="${product.orderProductTypeCd != 'ORDER_PRODUCT_TYPE_CD.WRAP'}">
							<div class="group_r">
								<c:if test="${product.optionChangeYn == 'Y'}">
									<a href="javascript:void(0);" class="btn_sStyle3 sGray2 btn_tb_option" onclick="$order.change.option($(this));">
										옵션 변경
									</a>
									<div class="ly_box option_box"></div>
								</c:if>
								<c:if test="${product.cancelYn == 'Y'}">
									<a href="javascript:void(0);" class="btn_sStyle3 sGray2"
										data-claim-type="cancel"
										data-is-partial="${partCancelYn}"
										data-is-set="${isSet}"
										onclick="$claim.request($(this));">주문취소</a>
								</c:if>
								<c:if test="${product.outQty > product.returnQty}">
									<c:if test="${product.trackingYn == 'Y'}">
										<a href="javascript:void(0);" class="btn_sStyle3 sGray2" onclick="$order.layer.delivery($(this));">배송조회</a>
									</c:if>
									<c:if test="${product.reviewYn == 'Y'}">
										<a href="javascript:void(0);" class="btn_sStyle3 sGray2" onclick="$order.update.review($(this));">상품평작성</a>
									</c:if>
									<c:if test="${product.returnYn == 'Y'}">
										<a href="javascript:void(0);" class="btn_sStyle3 sGray2"
											data-claim-type="exchange"
											onclick="$claim.request($(this));">교환신청</a>
										<a href="javascript:void(0);" class="btn_sStyle3 sGray2"
											data-claim-type="return"
											data-is-partial="${partCancelYn}"
											data-is-set="${isSet}"
											onclick="$claim.request($(this));">반품신청</a>
									</c:if>
								</c:if>
							</div>
						</c:if>
					</div>
				</div>
			</div>
		</div><!-- end tr_box -->
	</c:if>
</c:forEach>
<c:set var="promotionIds" value="" />
<c:set var="orderWrapFee" value="0"/>
<c:forEach var="product2" varStatus="idx2" items="${order.omsOrderproducts}">
	<c:if test="${product2.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.WRAP'}"><c:set var="orderWrapFee" value="${orderWrapFee + product2.totalSalePrice * product2.orderQty}"/></c:if>
	<c:if test="${product2.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.ORDERPRESENT'}">
		<c:if test="${!fn:contains(promotionIds, product2.presentId) && !empty product2.presentId}">
			<c:set var="promotionIds">${promotionIds},${product2.presentId}</c:set>
			<div class="tr_box tr_promo">
				<div class="promotion full">
					<strong>${product2.presentName}</strong>
					<ul>
						<c:forEach var="product3" varStatus="idx3" items="${order.omsOrderproducts}">
							<c:if test="${product2.presentId == product3.presentId}">
								<li>
									<tags:prdImgTag productId="${product3.productId}" seq="0" size="60"  />
									<span>
										<em>${product3.productName}</em>
									</span>
								</li>
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
		<div class="payment_info">
			<div class="slide_tit1">
				<span class="normal_tit pc_only">결제정보</span>
				<a href="javascript:void(0);" class="evt_tit mo_only">결제정보</a>
			</div>
			<div class="box">
				<div class="columnL">
					<c:set var="totPayAmt" value="0"/>
					<c:forEach var="payment" varStatus="status" items="${paymentList}">
						<c:if test="${payment.majorPaymentYn == 'Y' && payment.paymentStateCd != 'PAYMENT_STATE_CD.CANCEL'}">
							<c:set var="totPayAmt" value="${totPayAmt + payment.paymentAmt}"/>
						</c:if>
					</c:forEach>
				
					<dl>
						<dt>상품금액</dt>
						<dd>${func:price(order.orderAmt + orderWrapFee, '')}<i>원</i></dd>
						<dt>배송비</dt>
						<dd><em class="plus">${func:price(delivery.orderDeliveryFee, '+')}</em><i>원</i></dd>
						<%--
						<dt>선물포장비</dt>
						<dd><em class="plus">${func:price(orderWrapFee, '+')}</em><i>원</i></dd>
						--%>
						<dt>총 할인금액</dt>
						<dd><em class="minus">${func:price(order.orderAmt + delivery.orderDeliveryFee + orderWrapFee - totPayAmt, '-')}</em><i>원</i></dd>
					</dl>
					<dl class="detail">
						<c:forEach var="coupon" varStatus="status" items="${couponList}">
							<dt>${coupon.couponTypeCd == 'DELIVERY' ? '배송비무료쿠폰' : '상품할인쿠폰'}</dt>
							<dd><em class="minus">${func:price(coupon.couponDcAmt, '-')}</em><i>원</i></dd>
						</c:forEach>
						<c:forEach var="payment" varStatus="status" items="${paymentList}">
							<c:if test="${payment.paymentMethodCd == 'PAYMENT_METHOD_CD.POINT' || payment.paymentMethodCd == 'PAYMENT_METHOD_CD.DEPOSIT'}">
								<dt>${payment.paymentMethodName}</dt>
								<dd><em class="minus">${func:price(payment.paymentAmt, '-')}</em>${payment.paymentMethodCd == 'PAYMENT_METHOD_CD.POINT' ? 'P' : '원'}</dd>
							</c:if>
						</c:forEach>
					</dl>
				</div>
				<div class="columnR">
					<div class="payNpoint">
						<dl class="money">
							<dt>총 결제금액</dt>
							<dd>
								${func:price(totPayAmt, '')}<i>원</i>
							</dd>
						</dl>
					</div>
					
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
									<c:if test="${payment.paymentMethodCd == 'PAYMENT_METHOD_CD.VIRTUAL'}">
										<a href="javascript:void(0);" class="btn_sStyle1 change_method" 
											data-callback="update"
											data-payment-no="${payment.paymentNo}"
											data-title="결제수단변경"
											onclick="$order.layer.payment($(this));">결제수단변경</a>
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
		<div class="btn_wrapC btn1ea">
			<a href="javascript:void(0);" class="btn_mStyle1 sPurple1" onclick="$order.search.history(1, 'order');">주문/배송 조회</a>
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
		$('.lnb ul:eq(0) li:eq(0)').addClass('on');
	});
//-->
</script>