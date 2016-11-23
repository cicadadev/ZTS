<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="func" uri="kr.co.intune.commerce.common.functions"%>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY주문관리|취소신청" name="pageNavi"/>
</jsp:include>
<div class="inner">
<div class="layout_type1">
<div class="column">
	<div class="myorder claimApply cancel">
		<h3 class="title_type1">취소신청</h3>
		<!-- 16.09.27 : 텍스트수정 -->
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
		</div>
		<div class="non_info">
			<div class="slide_tit1">
				<a href="javascript:void(0);" class="evt_tit">신청자정보</a>
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
			<c:forEach var="address" varStatus="status" items="${order.omsDeliveryaddresss}">
				<dl class="delivery" 
					data-delivery-address-no="${address.deliveryAddressNo}"
					data-delivery-policy-no="${address.omsDeliverys[0].deliveryPolicyNo}"
					data-select-key="cancel"
					>
					<dt>${address.name1}</dt>
					<dd>
						<span>
							${address.phone2}${!empty address.phone1 && !empty address.phone2 ? ' <i class="bar">|</i> ' : ''}${address.phone1}<br />
							<c:if test="${!empty address.zipCd}">
								<em>(${address.zipCd}) ${address.address1} ${address.address2}</em><br /> 
								${address.note}
							</c:if>
						</span>
					</dd>
				</dl>
				<c:if test="${order.orderTypeCd == 'ORDER_TYPE_CD.GIFT'}">
					<dt>선물메세지</dt>
					<dd>
						<span>${order.giftMsg}</span>
					</dd>
				</c:if>				
			</c:forEach>
			
			<div class="viewTblList">
				<div class="div_tb_thead3">
					<div class="tr_box">
						<span class="col1">상품명/옵션정보</span>
						<span class="col2">취소수량</span>
					</div>
				</div>
				<ul class="div_tb_tbody3">
					<li>

<form name="omsClaimWrapper" id="omsClaimWrapper">
<input name="orderId" value="${order.orderId}" type="hidden" style="display: none;">
<input name="claimTypeCd" value="CLAIM_TYPE_CD.CANCEL" type="hidden" style="display: none;">
<input name="claimStateCd" value="CLAIM_STATE_CD.COMPLETE" type="hidden" style="display: none;">
<input name="cancelAll" value="${cancelAll}" type="hidden" style="display: none;">

<c:set var="trIdx" value="0"/>
<c:set var="seq" value="0"/>
<c:forEach var="product" varStatus="idx2" items="${order.omsOrderproducts}">
	<c:if test="${fn:contains('ORDER_PRODUCT_TYPE_CD.GENERAL,ORDER_PRODUCT_TYPE_CD.SET,ORDER_PRODUCT_TYPE_CD.WRAP', product.orderProductTypeCd)}">
	<input name="omsOrderproducts[${seq}].orderId" value="${order.orderId}" type="hidden" readonly="readonly">
	<input name="omsOrderproducts[${seq}].orderProductNo" value="${product.orderProductNo}" type="hidden" readonly="readonly">
		<c:set var="trIdx" value="${trIdx + 1}"/>
		<div class="tr_box tr_idx ${trIdx}">
			<div class="col1">
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
							<c:forEach var="children" varStatus="idx3" items="${order.omsOrderproducts}">
								<c:if test="${children.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SUB' && children.upperOrderProductNo == product.orderProductNo}">
									<em class="option_txt"
										data-order-id="${order.orderId}"
										data-order-product-no="${children.orderProductNo}"
										data-product-id="${children.productId}"
										data-saleproduct-id="${children.saleproductId}"
										data-add-sale-price="${children.addSalePrice}"
										>
										<i>
											<b>
												<c:if test="${children.orderDeliveryTypeCd != 'ORDER_DELIVERY_TYPE_CD.ORDER'}">
													<span style="color: red;">(${children.orderDeliveryTypeName})</span> 
												</c:if>
												${children.productName} : ${children.saleproductName}
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
								data-order-product-no="${children.orderProductNo}"
								data-product-id="${children.productId}"
								data-saleproduct-id="${children.saleproductId}"
								data-add-sale-price="${children.addSalePrice}"
								>
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
						<span class="pieceNum">1개</span>
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
							<span class="icon_gift iconBlue3">선물포장</span>
							<small>신청</small>
						</span>
					</u>
				</c:if>
			</div>
			<div class="col2">
				<i class="motit">취소수량</i>
				<div class="select_box1 type1">
					<input type="hidden" name="omsOrderproducts[${seq}].omsClaimproduct.orderId" value="${order.orderId}" readonly="readonly">
					<input type="hidden" name="omsOrderproducts[${seq}].omsClaimproduct.orderProductNo" value="${product.orderProductNo}" readonly="readonly">
					<input type="hidden" name="omsOrderproducts[${seq}].omsClaimproduct.claimNo" value="" readonly="readonly">
					<c:if test="${!cancelAll}">
						<label></label>
						<select name="omsOrderproducts[${seq}].omsClaimproduct.claimQty" onchange="$claim.setdata.claimqty($(this));" 
							data-order-id="${order.orderId}"
							data-order-product-no="${product.orderProductNo}"
							data-total-sale-price="${product.totalSalePrice}"
							data-wrap-yn="${product.wrapYn}"
							data-wrap-together-yn="${product.wrapTogetherYn}"
							data-wrap-volume="${product.wrapVolume}">
							<c:forEach begin="0" end="${product.orderQty - product.cancelQty}" step="1" var="count">
								<option value="${count}">${count}</option>
							</c:forEach>
						</select>
					</c:if>
					<c:if test="${cancelAll}">
						<label style="background: none;">${product.orderQty - product.cancelQty}</label>
						<select name="omsOrderproducts[${seq}].omsClaimproduct.claimQty" onchange="$claim.setdata.claimqty($(this));" style="display: none;"
							data-order-id="${order.orderId}"
							data-order-product-no="${product.orderProductNo}"
							data-total-sale-price="${product.totalSalePrice}"
							data-wrap-yn="${product.wrapYn}"
							data-wrap-together-yn="${product.wrapTogetherYn}"
							data-wrap-volume="${product.wrapVolume}">
							<option value="${product.orderQty - product.cancelQty}" selected="selected">${product.orderQty - product.cancelQty}</option>
						</select>
					</c:if>
				</div>
			</div>
			<div class="claimReason">
				<div>
					<i class="motit">사유</i>
					<div class="reason">
						<div class="select_box1 type1">
							<label></label>
							<select name="omsOrderproducts[${seq}].omsClaimproduct.claimReasonCd"
								data-order-id="${order.orderId}"
								data-order-product-no="${product.orderProductNo}"
								data-claim-type="cancel"
								onchange="$claim.setdata.claimreason($(this));">
								<option value="">선택</option>
								<c:forEach var="reason" varStatus="idx3" items="${func:getCodeList('CLAIM_REASON_CD')}">
									<c:if test="${reason.sortNo != 10 && reason.sortNo != 11 && reason.sortNo != 12}">
										<option value="${reason.cd}">${reason.name}</option>
									</c:if>
								</c:forEach>
							</select>
						</div>
						<div class="inputTxt_place1" style="display: none;">
							<label></label>
							<span>
								<input type="text" name="omsOrderproducts[${seq}].omsClaimproduct.claimReason" class="claim_reason" disabled="disabled" />
							</span>
						</div>
						<c:if test="${seq != 0}">
							<div class="chkBox">
								<label class="chk_style1" style="cursor: pointer;">
									<em><input type="checkbox" onclick="$claim.setdata.claimequals($(this));" data-seq="${seq}" /></em>
									<span>위 상품과 취소사유가 동일합니다.</span>
								</label>
							</div>
						</c:if>
					</div>
				</div>
			</div>
		</div><!-- end tr_box -->
		<c:set var="seq" value="${seq + 1}"/>
	</c:if>
</c:forEach>
</form>
					</li>
				</ul>
			</div>
			
<c:set var="promotionIds" value="" />
<c:set var="orderWrapFee" value="0"/>
<c:forEach var="product2" varStatus="idx2" items="${order.omsOrderproducts}">
	<c:if test="${product2.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.WRAP'}"><c:set var="orderWrapFee" value="${orderWrapFee + product2.totalSalePrice * product2.orderQty}"/></c:if>
	<c:if test="${product2.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.ORDERPRESENT'}">
		<c:if test="${!fn:contains(promotionIds, product2.presentId) && !empty product2.presentId}">
			<c:set var="promotionIds">${promotionIds},${product2.presentId}</c:set>
			<div class="promotion full"
				data-present-id="${product2.presentId}" 
				style="display: none;">
				<strong><span class="icon_type1 iconPurple2">취소사은품</span>${product2.presentName}</strong>
				<ul>
					<c:forEach var="product3" varStatus="idx3" items="${order.omsOrderproducts}">
						<c:if test="${product2.presentId == product3.presentId}">
							<li data-order-product-no="${product3.orderProductNo}"
								style="display: none;">
								<select name="omsOrderproducts[${seq}].omsClaimproduct.claimQty" disabled="disabled" style="display: none;">
									<option value="${product3.orderQty}" selected="selected">${product3.orderQty}</option>
								</select> 
								<select name="omsOrderproducts[${seq}].omsClaimproduct.claimReasonCd" disabled="disabled" style="display: none;">
									<option value="" selected="selected">selected</option>
								</select>
								<input name="omsOrderproducts[${seq}].omsClaimproduct.claimReason" disabled="disabled" style="display: none;">
								
								<tags:prdImgTag productId="${product3.productId}" seq="0" size="60"  />
								<span>
									<em>${product3.productName}</em>
								</span>
								<c:set var="seq" value="${seq + 1}"/>
							</li>
						</c:if>
					</c:forEach>
				</ul>
			</div>
		</c:if>
	</c:if>
</c:forEach>

		</div>
		
		<div class="payment_info mtoggleBox on">
			<c:set var="showRefund" value="${false}"/>
			<c:set var="totPayAmt" value="0"/>
			<c:forEach var="payment" varStatus="status" items="${paymentList}">
				<c:if test="${payment.majorPaymentYn == 'Y' && payment.paymentStateCd != 'PAYMENT_STATE_CD.CANCEL'}">
					<c:set var="totPayAmt" value="${totPayAmt + payment.paymentAmt}"/>
					<c:if test="${payment.paymentMethodCd == 'PAYMENT_METHOD_CD.VIRTUAL'}">
						<c:set var="showRefund" value="${true}"/>
					</c:if>
				</c:if>
			</c:forEach>
			<h4 class="sub_tit1 toggleBtn">결제정보</h4>
			<div class="box toggleCont">
				<div class="columnL">
					<dl>
						<dt>상품금액</dt>
						<dd>${func:price(order.orderAmt + orderWrapFee, '')}<i>원</i></dd>
						<dt>배송비</dt>
						<dd><em class="plus">${func:price(delivery.orderDeliveryFee, '+')}</em><i>원</i></dd>
						<%--
						<dt>선물포장비</dt>
						<dd><em class="plus">${func:price(delivery.orderWrapFee, '+')}</em><i>원</i></dd>
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
									<!-- 계좌이체 -->
									<c:if test="${payment.paymentMethodCd == 'PAYMENT_METHOD_CD.TRANSFER'}">
										${payment.paymentBusinessNm}
									</c:if>
									<!-- 휴대전화 -->
									<c:if test="${payment.paymentMethodCd == 'PAYMENT_METHOD_CD.MOBILE'}">
										${payment.mobilePhone}
									</c:if>
								</li>
								<!-- 가상계좌 -->
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
			
		<div class="payment_info mtoggleBox on">			
			<h4 class="sub_tit1 toggleBtn" style="margin-top: 25px;">환불정보</h4>
			<div class="box toggleCont">
				<div class="columnL">
					<dl id="refund_layer">
						<dt>상품금액 <em>(환불)</em></dt>
						<dd>${cancelAll ? func:price(order.orderAmt + orderWrapFee, '') : 0}<i>원</i></dd>
						<dt>배송비 <em>(환불)</em></dt>
						<dd><em class="plus">${cancelAll ? func:price(delivery.orderDeliveryFee, '+') : 0}</em><i>원</i></dd>
						<dt>배송비 <em>(추가)</em></dt>
						<dd><em class="plus">0</em><i>원</i></dd>
						<%--
						<dt>선물포장비 <em>(환불)</em></dt>
						<dd><em class="plus">${cancelAll ? func:price(orderWrapFee, '+') : 0}</em><i>원</i></dd>
						--%>
						<dt>총 할인금액 <em>(복원)</em></dt>
						<dd><em class="minus">${cancelAll ? func:price(order.orderAmt + delivery.orderDeliveryFee + orderWrapFee - totPayAmt, '-') : 0}</em><i>원</i></dd>
					</dl>
					<dl class="detail">
						<c:if test="${cancelAll}">
							<c:forEach var="coupon" varStatus="status" items="${couponList}">
								<dt>${coupon.couponTypeCd == 'DELIVERY' ? '배송비무료쿠폰' : '상품할인쿠폰'}</dt>
								<dd><em class="minus">${func:price(coupon.couponDcAmt, '-')}</em><i>원</i></dd>
							</c:forEach>
						</c:if>
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
							<dt>총 환불금액</dt>
							<dd>${cancelAll ? func:price(totPayAmt, '') : 0}<i>원</i></dd>
						</dl>
					</div>
				</div>
			</div>
		</div>
		<c:if test="${showRefund}">
			<h4 class="sub_tit1">환불계좌 정보</h4>
			<div class="rw_tbBox">
				<form name="refundAccount" method="post">
				<input type="hidden" name="memberNo" value="${order.memberNo}" />
				<ul class="rw_tb_tbody2">
					<li>
						<div class="tr_box">
							<div class="col1">
								<span class="group_inline">은행선택</span>
							</div>
							<div class="col2">
								<div class="group_block">
									<c:if test="${!empty order.mmsMemberZts.accountAuthDt}">
										<div class="inputTxt_place1">
											<input type="text" name="paymentBusinessNm" value="${order.mmsMemberZts.bankName}" readonly="readonly"/>
											<input type="hidden" name="paymentBusinessCd" value="${order.mmsMemberZts.bankCd}" />
										</div>
									</c:if>
									<c:if test="${empty order.mmsMemberZts.accountAuthDt}">
										<div class="select_box1">
											<label></label>
											<select name="paymentBusinessCd" onchange="$claim.setdata.refund($(this));">
												<option value="">은행선택</option>
												<c:forEach var="bank" varStatus="idx3" items="${func:getCodeList('BANK_CD')}">
													<option value="${bank.cd}" ${order.mmsMemberZts.bankCd == bank.cd ? 'selected' : ''}>${bank.name}</option>
												</c:forEach>
											</select>
											<input type="hidden" name="paymentBusinessNm">
										</div>
									</c:if>
								</div>
							</div>
						</div>
					</li>
					<li>
						<div class="tr_box">
							<div class="col1">
								<span class="group_inline">계좌번호</span>
							</div>
							<div class="col2">
								<div class="group_block">
									<div class="inputTxt_place1">
										<c:if test="${!empty order.mmsMemberZts.accountAuthDt}">
											<input type="text" name="refundAccountNo" value="${order.mmsMemberZts.accountNo}" readonly="readonly"/>
										</c:if>
										<c:if test="${empty order.mmsMemberZts.accountAuthDt}">
											<label>‘-’ 없이 입력해주세요</label>
											<span>
												<input type="text" name="refundAccountNo" value="${order.mmsMemberZts.accountNo}" />
											</span>
										</c:if>
									</div>
								</div>
							</div>
						</div>
					</li>
					<li>
						<div class="tr_box">
							<div class="col1">
								<span class="group_inline">예금주</span>
							</div>
							<div class="col2">
								<div class="group_block">
									<c:if test="${!empty order.mmsMemberZts.accountAuthDt}">
										<div class="inputTxt_place1">
											<input type="text" name="refundAccountNo" value="${order.mmsMemberZts.accountHolderName}" readonly="readonly" />
										</div>
									</c:if>
									<c:if test="${empty order.mmsMemberZts.accountAuthDt}">
										<div class="inpBox">
											<div class="inputTxt_place1">
												<label class="mo_only">예금주명</label>
												<span>
													<input type="text" name="accountHolderName" value="${order.mmsMemberZts.accountHolderName}" />
												</span>
											</div>
											<a href="javascript:void(0);" class="btn_sStyle4 sGray2" onclick="mypage.refund.accountCertify($(this));">인증</a>
										</div>
									</c:if>
								</div>
							</div>
						</div>
					</li>
				</ul>
				<c:if test="${empty order.mmsMemberZts.accountAuthDt}">
					<div class="chkBox">
						<label class="chk_style1" style="cursor: pointer;">
							<em>
								<input type="checkbox" name="accountAuthDt" value="${order.mmsMemberZts.accountAuthDt}" />
							</em>
							<span>환불계좌 수집/설정 동의</span>
						</label>
					</div>
				</c:if>
				</form>
			</div>
		</c:if>
		<c:choose>
			<c:when test="${empty order.omsClaims[0].claimNo 
						|| (order.omsClaims[0].claimStateCd != 'CLAIM_STATE_CD.REQ'
							&& order.omsClaims[0].claimStateCd != 'CLAIM_STATE_CD.PAYMENT_READY'
							&& order.omsClaims[0].claimStateCd != 'CLAIM_STATE_CD.ACCEPT')}">
				<div class="btn_wrapC btn2ea">
					<a href="javascript:void(0);" class="btn_mStyle1 sWhite1" onclick="location.href=document.referrer">취소</a>
					<a href="javascript:void(0);" class="btn_mStyle1 sPurple1" onclick="$claim.cancel($(this));" 
						id="claimBtn" 
						data-title="취소"
						data-claim-type="cancel"
						>취소신청</a>
				</div>
				<ul class="notice">
					<li>무통장 입금 혹은 휴대폰 전월 주문내역을 취소할 경우 등록하신 계좌로 환불해드립니다</li>
					<li>환불계좌 등록 후, 환불대기중의 상품은 등록하신 계좌로 변경되어 환불됩니다.</li>
					<li>그 외 문의는 고객센터(1588-8744) 또는 1:1문의를 이용해주시기 바랍니다.</li>
				</ul>
			</c:when>
			<c:otherwise>
				<ul class="notice2" style="text-align: center;">
					<strong><li>이미 진행중인 클레임이 있습니다.</li></strong>
				</ul>
			</c:otherwise>
		</c:choose>
		
		<div class="btn_wrapC btn1ea">
			<a href="javascript:void(0);" class="btn_mStyle1 sPurple1" onclick="$order.search.history(0, 'order');">주문/배송 조회</a>
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