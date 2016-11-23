<%--
	화면명 : 주문완료
	작성자 : dennis
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ page import="gcp.common.util.FoSessionUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags" %>

<script type="text/javascript">

$(document).ready(function(){
	window._rblqueue = window._rblqueue || [];
	var orderProductNos = $("input[name=orderProductNo]");
// 	console.log(orderProductNos);
	orderProductNos.each(function(){
		var form = $("#productForm"+this.value);		
		var productId = form.find("#productId").val();
		var totalSalePrice = form.find("#totalSalePrice").val();
		var orderQty = form.find("#orderQty").val();		
		_rblqueue.push(['addVar', 'orderItems', {itemId:productId, price:totalSalePrice, quantity:orderQty}]);
	});
	
	var memberNo = $("#memberNo").val();
	var orderAmt = $("#orderAmt").val();
	var orderId = $("#orderId").val();
	var deviceType = $("#deviceTypeCd").val();
	var device;
	if(deviceType == 'DEVICE_TYPE_CD.PC'){
		device = 'PW';
	}else if(deviceType == 'DEVICE_TYPE_CD.APP'){
		device = 'MI';
	}else if (deviceType == 'DEVICE_TYPE_CD.MW'){
		device = 'MW';
	}
		
	_rblqueue.push(['setVar','cuid',global['config'].recobelCuid]);
	_rblqueue.push(['setVar','device',device]);
	_rblqueue.push(['setVar','orderId',orderId]);
	_rblqueue.push(['setVar','orderPrice',orderAmt]);
	_rblqueue.push(['setVar','userId',memberNo]);		// optional
	_rblqueue.push(['setVar','babyGender','<%=FoSessionUtil.getRecobellBabyGenderCd()%>']);
	_rblqueue.push(['setVar','babyAgeInMonths','<%=FoSessionUtil.getRecobellBabyMonthCd()%>']);		
	_rblqueue.push(['track','order']);

	
	setTimeout(function() {
	  (function(s,x){s=document.createElement('script');s.type='text/javascript';
	  s.async=true;s.defer=true;s.src=(('https:'==document.location.protocol)?'https':'http')+
	    '://assets.recobell.io/rblc/js/rblc-apne1.min.js';
	  x=document.getElementsByTagName('script')[0];x.parentNode.insertBefore(s, x);})();
	}, 0);
	
})
</script>

<script type="text/javascript">
 
</script>

<input type="hidden" id="memberNo" value="${omsOrder.memberNo }"/>
<input type="hidden" id="orderId" value="${omsOrder.orderId }"/>
<input type="hidden" id="orderAmt" value="${omsOrder.orderAmt }"/>
<input type="hidden" id="deviceTypeCd" value="${omsOrder.deviceTypeCd }"/>

	<!-- Mobile -->
	
	<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
		<jsp:param value="주문완료" name="pageNavi"/>
	</jsp:include>			
	<div class="inner">
		<div class="orderComplete">
			<div class="step">
				<h3 class="title_type1">
					주문완료
				</h3>
				<ol>
					<li><span class="step_01">01</span>장바구니</li>
					<li><span class="step_02">02</span>주문/결제</li>
					<li class="active"><span class="step_03">03</span>주문완료</li>
				</ol>
			</div>

			<div class="completeWrap">
				<strong class="title">주문이 완료되었습니다.</strong>

				<ul class="completeInfo">
					<li class="first">
						<strong>주문번호</strong>${omsOrder.orderId }
					</li>
					<li>
						<strong>주문일시</strong>${omsOrder.orderDt }
					</li>
				</ul>
			</div>						
			
			<c:if test="${omsOrder.orderTypeCd == 'ORDER_TYPE_CD.GIFT' }">
			<div class="relBox">
				<div class="rw_tbBox">
					<ul class="rw_tb_tbody2 thin">
						<li>
							<div class="tr_box">
								<div class="col1">
									<span class="group_inline">선물 받을 사람</span>
								</div>
								<div class="col2">
									<div class="group_block"><strong class="giftUser">${omsOrder.giftName }<span>${omsOrder.giftPhone }</span></strong></div>
								</div>
							</div>
						</li>
					</ul>
				</div>
			</div>
			</c:if>
			<c:forEach items="${omsOrder.omsDeliveryaddresss }" var="addr">
			<!-- ### 배송정보 ### -->
<!-- 			<div class="relBox"> -->
<!-- 				<h3 class="sub_tit1">배송정보</h3> -->
<!-- 				<div class="rw_tbBox"> -->
<!-- 					<ul class="rw_tb_tbody2 thin"> -->
<!-- 						<li> -->
<!-- 							<div class="tr_box"> -->
<!-- 								<div class="col1"> -->
<!-- 									<span class="group_inline">받는사람</span> -->
<!-- 								</div> -->
<!-- 								<div class="col2"> -->
<%-- 									<div class="group_block">${addr.name1 }</div> --%>
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 						</li> -->
<!-- 						<li> -->
<!-- 							<div class="tr_box"> -->
<!-- 								<div class="col1"> -->
<!-- 									<span class="group_inline">연락처</span> -->
<!-- 								</div> -->
<!-- 								<div class="col2"> -->
<%-- 									<div class="group_block">${addr.phone2 } / ${addr.phone1 }</div> --%>
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 						</li> -->
<!-- 						<li> -->
<!-- 							<div class="tr_box"> -->
<!-- 								<div class="col1"> -->
<!-- 									<span class="group_inline">주소</span> -->
<!-- 								</div> -->
<!-- 								<div class="col2"> -->
<%-- 									<div class="group_block">(${addr.zipCd}) ${addr.address1 } ${addr.address2 }</div> --%>
<!-- 								</div> -->
<!-- 							</div> -->
<!-- 						</li> -->
<!-- 					</ul> -->
<!-- 				</div> -->
<!-- 			</div> -->
			<!-- ### //배송정보 ### -->
			
			<c:set var="couponDcAmt" value="0"/>
			<div class="viewTblList">
				<!-- ### 테이블 헤더 ### -->
				<div class="div_tb_thead3">
					<div class="tr_box">
						<span class="col1">상품/옵션정보</span>
	
						<span class="col2">수량</span>
	
						<span class="col3">주문금액</span>
	
						<span class="col4">배송비</span>
					</div>
				</div>
				<!-- ### //테이블 헤더 ### -->

				<!-- ### 테이블 바디 ### -->
				<ul class="div_tb_tbody3">
									
						<c:forEach items="${addr.omsDeliverys }" var="del">
							<c:forEach items="${del.omsOrderproducts }" var="os" varStatus="status">
								<c:if test="${os.orderProductTypeCd != 'ORDER_PRODUCT_TYPE_CD.ORDERPRESENT' }">
								<form id="productForm${os.orderProductNo }">
									<input type="hidden" name="orderProductNo" value="${os.orderProductNo }"/>
									<input type="hidden" id="totalSalePrice" value="${os.totalSalePrice }"/>
									<input type="hidden" id="orderQty" value="${os.orderQty }"/>
									<input type="hidden" id="productId" value="${os.productId }"/>
								</form>
								
								<c:if test="${preDeliveryPolicyNo != os.deliveryPolicyNo }">
								<li>
								</c:if>
								
									<div class="tr_box">
										<div class="col1">
											<div class="positionR">
												<div class="prod_img">
													<a href="#none" onclick="javascript:ccs.link.product.detail('${os.productId}')">
														<tags:prdImgTag productId="${os.productId}" size="90" alt="${os.productName }"/>
													</a>
												</div>
												<a href="#none" class="title" onclick="javascript:ccs.link.product.detail('${os.productId}')">
													${os.productName }
												</a>
												
												<c:if test="${os.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.GENERAL' }">
												<em class="option_txt">
													<i>${os.saleproductName }</i>
												</em>
												</c:if>																						
												<c:set var="presentNames" value=""/>
												<c:forEach items="${os.omsOrderproducts }" var="oss">
													<c:if test="${oss.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SUB' }">
														<em class="option_txt">
															<i>${oss.productName } : ${oss.saleproductName }</i>
														</em>
													</c:if>
													<c:if test="${oss.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.PRODUCTPRESENT' }">
														<c:set var="presentNames" value="${presentNames }
															<u class=\"gift_txt\">
																<span class=\"btn_tb_gift\">
																	<span class=\"icon_type1 iconBlue3\">사은품</span> ${oss.productName } 
																</span>
															</u>															
														"/>
													</c:if>
													
												</c:forEach>
												<div class="piece">
													<span class="piecePrice"><fmt:formatNumber value="${os.totalSalePrice }" pattern="#,###"/><i>원</i></span>
												</div>																		
											</div>
											${presentNames }
<%-- 											<c:if test="${presentNames != '' }"> --%>
<!-- 											<u class="gift_txt"> -->
<!-- 												<span class="btn_tb_gift"> -->
<%-- 													<span class="icon_type1 iconBlue3">사은품</span> ${fn:substring(presentNames,fn:indexOf(presentNames,",")+1, fn:length(presentNames))}  --%>
<!-- 												</span> -->
<!-- 											</u> -->
<%-- 											</c:if> --%>
											<c:if test="${os.wrapYn == 'Y' }">
											<u class="gift_txt">
												<span class="btn_tb_gift">
													<span class="icon_gift">선물포장</span> 
													<small>신청</small>
												</span>
											</u>
											</c:if>
										</div>			
										<div class="col2">
											<div class="quantity_result">
												${os.orderQty }개
											</div>
										</div>
										
										<div class="col3">
											<span class="price">
												<em><fmt:formatNumber value="${(os.totalSalePrice * os.orderQty) + del.orderDeliveryFee }" pattern="#,###"/><i>원</i></em>
											</span>
										</div>
										
										<div class="col4 rowspan">
											<div class="cell">
												<span class="vAlign price">
													<em>
													<c:choose>
													<c:when test="${del.orderDeliveryFee == 0}">
														무료
													</c:when>
													<c:otherwise>
														<fmt:formatNumber value="${del.orderDeliveryFee }" pattern="#,###"/><i>원</i>
													</c:otherwise>
													</c:choose>
													</em>
												</span>
											</div>
										</div>
										
<%-- 										<c:set var="couponDcAmt" value="${couponDcAmt + os.productCouponDcAmt + os.plusCouponDcAmt }" />										 --%>
									</div>
								
								<c:if test="${del.omsOrderproducts[status.index+1].deliveryPolicyNo != os.deliveryPolicyNo || status.last}">
								</li>					
								</c:if>
								
								<c:set var="preDeliveryPolicyNo" value="${os.deliveryPolicyNo }"/>
																
								</c:if>								
							</c:forEach>
						</c:forEach>
													
				</ul>
				<!-- ### //테이블 바디 ### -->
			</div>
				<c:forEach items="${addr.omsDeliverys }" var="del">
					<c:forEach items="${del.omsOrderproducts }" var="os">					
						<c:if test="${os.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.ORDERPRESENT' }">
							<c:if test="${fn:indexOf(presentIds,os.presentId) < 0 }">
							<div class="giftList">
								<dl>
									<dt>
										<div>
											<strong>${os.presentName }</strong>
<%-- 											<p>${os.presentMinOrderAmt }만원 이상 구매한 모든 고객에게 사은품을 드립니다.</p> --%>
<%-- 											<span>${os.startDt } ~ ${os.endDt }</span> --%>
										</div>
									</dt><dd>
										<ul>
							</c:if>
											<li>
												<tags:prdImgTag productId="${os.productId}" size="90" alt="${os.productName }"/>
												<span>${os.productName }</span>
											</li>
							<c:if test="${fn:indexOf(presentIds,os.presentId) < 0 }">											
										</ul>
									</dd>
								</dl>								
							</div>
							<c:set var="presentIds" value="${presentIds}:${os.presentId }" />
							</c:if>
						</c:if>
					</c:forEach>					
				</c:forEach>											
			</c:forEach>	
			
			
					
			<!-- ### 쿠폰정보 ### -->
			<c:forEach items="${omsOrder.omsOrdercoupons }" var="coupon">
				<c:set var="couponDcAmt" value="${couponDcAmt + coupon.couponDcAmt }" />							
			</c:forEach>
			<!-- ### 쿠폰정보 ### -->
			
			<c:set var="dcAmt" value="0"/>
			<c:if test="${couponDcAmt > 0 }">
				<c:set var="dcAmt" value="${couponDcAmt }"/>
			</c:if>
			
			<c:set var="pointDcAmt" value="0"/>
			<c:set var="depositDcAmt" value="0"/>
			<!-- ### 결제정보 ### -->
			<div class="relBox">
				<div class="rw_tbBox">
					<ul class="rw_tb_tbody2 thin">
						<li>
							<div class="tr_box">
								<div class="col1">
									<div class="cell">
										<span class="group_inline">결제정보</span>
									</div>
								</div>
								<div class="col2">
									<div class="group_block">
										<ul class="payListInfo">											
											<c:forEach items="${omsOrder.omsPayments }" var="pay" varStatus="st">
												<c:choose>
												<c:when test="${pay.paymentMethodCd == 'PAYMENT_METHOD_CD.CARD' }">
													<li>${pay.paymentMethodName } <strong><fmt:formatNumber value="${pay.paymentAmt }" pattern="#,###"/>원</strong></li>
													<li><strong>${pay.creditcardNo } ${pay.paymentBusinessNm }</strong></li>
													<li>할부개월 :
														<c:choose>
														<c:when test="${pay.installmentCnt == 0 }">
														일시불
														</c:when>
														<c:otherwise>
														${pay.installmentCnt }개월
														</c:otherwise>
														</c:choose> 
													</li>
													<li>에스크로여부 : ${pay.escrowYn }</li>													
												</c:when>
												<c:when test="${pay.paymentMethodCd == 'PAYMENT_METHOD_CD.VIRTUAL' }">
													<li>${pay.paymentMethodName } <strong><fmt:formatNumber value="${pay.paymentAmt }" pattern="#,###"/>원</strong></li>
													<li><strong>입금계좌 : ${pay.accountNo} ${pay.paymentBusinessNm }</strong></li>
													<li>입금자명 : ${pay.depositorName }</li>
													<li>입금기한 : ${pay.virtualInPeriod }</li>													
												</c:when>
												<c:when test="${pay.paymentMethodCd == 'PAYMENT_METHOD_CD.TRANSFER'}">
													<li>${pay.paymentMethodName } <strong><fmt:formatNumber value="${pay.paymentAmt }" pattern="#,###"/>원</strong></li>
													<li><strong>출금은행 : ${pay.paymentBusinessNm }</strong></li>													
												</c:when>
												<c:when test="${pay.paymentMethodCd == 'PAYMENT_METHOD_CD.MOBILE' }">
													<li>${pay.paymentMethodName } <strong><fmt:formatNumber value="${pay.paymentAmt }" pattern="#,###"/>원</strong></li>
													<li><strong>휴대폰번호 : ${pay.mobilePhone } ${pay.paymentBusinessNm }</strong></li>													
												</c:when>
												<c:when test="${pay.paymentMethodCd == 'PAYMENT_METHOD_CD.KAKAO' }">
													<li>${pay.paymentMethodName } <strong><fmt:formatNumber value="${pay.paymentAmt }" pattern="#,###"/>원</strong></li>
													<li><strong>${pay.creditcardNo } ${pay.paymentBusinessNm }</strong></li>
													<li>할부개월 :
														<c:choose>
														<c:when test="${pay.installmentCnt == 0 }">
														일시불
														</c:when>
														<c:otherwise>
														${pay.installmentCnt }개월
														</c:otherwise>
														</c:choose> 
													</li>													
												</c:when>
												<c:when test="${pay.paymentMethodCd == 'PAYMENT_METHOD_CD.DEPOSIT' }">
													<c:set var="dcAmt" value="${dcAmt + pay.paymentAmt }"/>
													<c:set var="depositDcAmt" value="${pay.paymentAmt }"/>
												</c:when>
												<c:when test="${pay.paymentMethodCd == 'PAYMENT_METHOD_CD.POINT' }">
													<c:set var="dcAmt" value="${dcAmt + pay.paymentAmt }"/>
													<c:set var="pointDcAmt" value="${pay.paymentAmt }"/>
												</c:when>	
												</c:choose>
											</c:forEach>												
											<c:if test="${dcAmt > 0 }">										
												<li><strong>총 할인금액 : <fmt:formatNumber value="${dcAmt }" pattern="#,###"/>원</strong> 
												(
												<c:if test="${couponDcAmt > 0 }">
													쿠폰 <fmt:formatNumber value="${couponDcAmt }" pattern="#,###"/>원
												</c:if>
												<c:if test="${pointDcAmt > 0 }">
													<c:if test="${couponDcAmt > 0 }">
													,
													</c:if>
													매일포인트 <fmt:formatNumber value="${pointDcAmt }" pattern="#,###"/>원
												</c:if>
												<c:if test="${depositDcAmt > 0 }">
													<c:if test="${couponDcAmt > 0 || pointDcAmt > 0}">
													,
													</c:if>
													예치금 <fmt:formatNumber value="${depositDcAmt }" pattern="#,###"/>원
												</c:if>												
												)</li>
											</c:if>
										</ul>
									</div>
								</div>
							</div>
						</li>
					</ul>
				</div>
			</div>
			<!-- ### //결제정보 ### -->
										
			<div class="btn_wrapC btn2ea">
				<a href="#none" onclick="javascript:ccs.link.common.main()" class="btn_bStyle1 sWhite1">계속 쇼핑하기</a>
				<a href="/mms/mypage/order/general/${omsOrder.orderId}" class="btn_bStyle1 sPurple1">주문내역확인</a>
			</div>
						
		</div>
