<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags" %>
<%@ taglib prefix="func" uri="kr.co.intune.commerce.common.functions" %>
<fmt:setLocale value="ko"/>
<div class="tab_con tab_01 tab_conOn">
	<div class="auto_payment">
		<dl>
			<dt>자동결제</dt>
			<dd>
				<b>${regularPaymentBusinessNm}</b>
			</dd>
		</dl>
		<a href="javascript:void(0);" class="btn_sStyle3" onclick="javascript:auth_LG()" >결제정보 변경</a>
	</div>
	<div class="non_info non_prod">

	
					
	<c:forEach var="regular" varStatus="idx1" items="${regularList}">
		<div class="viewTblList">
			<div class="regular_info">
				<div class="regular_info01">
					<!-- 16.10.09 : 수정 -->
					<div class="orderNum">
						<fmt:parseDate value="${regular.insDt}" var="dateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
						<fmt:formatDate value="${dateFmt}" var="regularInsDt" pattern="yyyy-MM-dd"/>
						<b>${regularInsDt}</b>
						<b>
							<i>신청번호<em class="colon">:</em></i> <!-- 16.10.14 : 태그(em) 추가 -->
							<em>${regular.regularDeliveryId}</em>
						</b>
					</div>
					<!-- //16.10.09 : 수정 
					<a href="javascript:void(0);" class="btn_sStyle1" 
								onclick="$order.change.regular($(this));"
								data-regular-delivery-id="${regular.regularDeliveryId}"
								data-regular-delivery-order="${regular.omsRegulardeliveryproducts[0].omsRegulardeliveryschedules[0].regularDeliveryOrder}">
								정기배송 설정변경</a>
					-->
					<a href="/mms/mypage/regular/change/${regular.regularDeliveryId}" class="btn_sStyle1">정기배송 설정변경</a>
				</div>
				
			</div>
			
			<div class="div_tb_thead3">
				<div class="tr_box">
					<span class="col1">상품명/옵션정보/수량</span>
					<span class="col2">결제예정금액</span>
				</div>
			</div>
			<ul class="div_tb_tbody3">
				<c:forEach var="product" varStatus="idx2" items="${regular.omsRegulardeliveryproducts}">
				<c:forEach var="schedule" varStatus="idx3" items="${product.omsRegulardeliveryschedules}">
					<c:if test="${idx3.first}">
						<li>
							<div class="tr_box">
								<div class="col1">
									<div class="positionR">
										<c:set var="productId" value="${product.pmsProduct.productId}"/>
										<div class="prod_img">
											<a href="/pms/product/detail?productId=${productId}">
												<tags:prdImgTag productId="${productId}" seq="0" size="90" />
											</a>
										</div>
										<a  href="/pms/product/detail?productId=${productId}" class="title">
											${product.pmsProduct.name}
										</a>
										<c:if test="${product.deliveryProductTypeCd != 'DELIVERY_PRODUCT_TYPE_CD.SET'}">
											<em class="option_txt">
												<i>${product.pmsSaleproduct.name}</i>
											</em>
										</c:if>
										<!-- <strong class="itemPrice">22,500<i>원</i></strong> 16.09.27 : 삭제 -->
										<!-- 16.09.27 : 추가 -->
										<div class="piece">
											<span class="pieceNum">${product.orderQty}개</span>
											<span class="slash">/</span>
											<span class="piecePrice">${func:price(product.regularDeliveryPrice,'')}<i>원</i></span>
										</div>
										<!-- //16.09.27 : 추가 -->
									</div>
								</div>
		
								<div class="col2">
									<span class="price">
										<em>${func:price(product.regularDeliveryPrice * product.orderQty,'')}<i>원</i></em>
									</span>
								</div>
		
								<div class="regular_info">
									<div class="regular_info02">
										<ul>
											<li>
												<span>배송주기/횟수</span> ${product.deliveryPeriodName} / 총 ${product.deliveryCnt}회 발송
		
											</li>
											<li>
												<span>정기배송요일</span> ${product.deliveryDay} 배송
											</li>
											<li>
												<fmt:parseDate value="${schedule.scheduleOrderDt}" var="shipDateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
												<%-- <fmt:formatDate value="${shipDateFmt}" var="shipDay" pattern="e"/> --%>
												<fmt:formatDate value="${shipDateFmt}" var="shipDt" pattern="yyyy-MM-dd"/>
												<fmt:formatDate value="${shipDateFmt}" var="shipDay" type="both" pattern="E"/>
												<span>다음 결제예정일</span> ${shipDt} (${shipDay})  / ${product.deliveryCnt}회중 ${schedule.regularDeliveryOrder}회차
											</li>
											<li>
												<span>다음 배송예정일</span>  ${schedule.regularDeliveryDt} (${fn:substring(product.deliveryDay,0,1) })
											</li>
										</ul>
									</div>
								</div>
							</div>
						</li>
					</c:if>
				</c:forEach>
				</c:forEach>
			</ul>
		</div>
	</c:forEach>
	</div>
<%-- <c:forEach var="regular" varStatus="idx1" items="${regularList}">
	<div class="non_info non_prod">
		<div class="slide_tit1">
			<a href="javascript:void(0);" class="evt_tit">정기배송신청번호 : ${regular.regularDeliveryId}</a>
		</div>
		<div class="viewTblList">
			<div class="regular_info">
				<c:forEach var="product" varStatus="idx2" items="${regular.omsRegulardeliveryproducts}">
				<c:if test="${idx2.first}">
					<c:forEach var="schedule" varStatus="idx3" items="${product.omsRegulardeliveryschedules}">
					<c:if test="${idx3.first}">
						<div class="regular_info01">
							<p><span>이번 정기 배송일 <i class="colon">:</i></span>${schedule.regularDeliveryDt} (${product.deliveryDay})</p>
<!-- 							<a href="/mms/mypage/regular/change" class="btn_sStyle1" onclick="">정기배송 설정변경</a> -->
							<a href="javascript:void(0);" class="btn_sStyle1" 
								onclick="$order.change.regular($(this));"
								data-regular-delivery-id="${regular.regularDeliveryId}"
								data-regular-delivery-order="${schedule.regularDeliveryOrder}">
								정기배송 설정변경</a>
						</div>
						<div class="regular_info02">
							<ul>
								<li>
									<span>배송주기/횟수 <i class="colon">:</i></span>${product.deliveryPeriodName} / 총 ${product.deliveryCnt}회
								</li>
								<li>
									<span>정기 배송요일 <i class="colon">:</i></span>${product.deliveryDay} 배송
								</li>
							</ul>
						</div>
					</c:if>
					</c:forEach>
				</c:if>
				</c:forEach>
			</div>
			<div class="div_tb_thead3">
				<div class="tr_box">
					<span class="col1">상품명/옵션정보/수량</span>
					<span class="col2">구매금액</span>
				</div>
			</div>
			<ul class="div_tb_tbody3">
				<c:forEach var="product" varStatus="idx2" items="${regular.omsRegulardeliveryproducts}">			
				<li>
					<div class="tr_box">
						<div class="col1">
							<div class="positionR">
								<c:set var="productId" value="${product.pmsProduct.productId}"/>
								<div class="prod_img">
									<a href="/pms/product/detail?productId=${productId}">
										<tags:prdImgTag productId="${productId}" seq="0" size="90" />
									</a>
								</div>
								<a href="/pms/product/detail?productId=${productId}" class="title">${product.pmsProduct.name}</a>
								
<!-- 								<em class="option_txt"> <i>5단계</i> <small>(1개)</small></em> -->
								<c:if test="${product.deliveryProductTypeCd == 'DELIVERY_PRODUCT_TYPE_CD.SET'}">
									
									<c:forEach var="children" varStatus="idx3" items="${order.omsOrderproducts}">
										<c:if test="${children.deliveryProductTypeCd == 'DELIVERY_PRODUCT_TYPE_CD.SUB' && children.upperOrderProductNo == product.orderProductNo}">
											<em class="option_txt">
												<i>
													<b>${children.productName} : ${children.saleproductName}</b>
												</i>
												<i style="float: right;">(${children.setQty}개)</i>
											</em>
										</c:if>
									</c:forEach>
									
								</c:if>
								<c:if test="${product.deliveryProductTypeCd != 'DELIVERY_PRODUCT_TYPE_CD.SET'}">
									<em class="option_txt">
										<i>${product.pmsSaleproduct.name}</i>
									</em>
								</c:if>
								
								<div class="piece">
									<span class="pieceNum">${product.orderQty}개</span>
									<span class="slash">/</span>
									<span class="piecePrice">${func:price(product.regularDeliveryPrice,'')}<i>원</i></span>
								</div>
							</div>
						</div>
						<div class="col2">
							<span class="price">
								<em>${func:price(product.regularDeliveryPrice * product.orderQty,'')}<i>원</i></em>
							</span>
						</div>
					</div>
				</li>
				</c:forEach>
			</ul>
		</div>
	</div>
</c:forEach>	 --%>
</div>

<!-- <script language="javascript" src="http://xpay.lgdacom.net/xpay/js/xpay_crossplatform.js" type="text/javascript" charset="utf-8"></script> -->
<script type="text/javascript">
<!--
	var pgData = function() {
		var mergeForm = $("#mergeForm");
		var form = $("#LGD_PAYINFO").find("input");
		form.each(function() {
			var name = $(this).attr("name");
			var id = $(this).attr("id");
			var value = $(this).attr("value");
			mergeForm.append($('<input>', {
				'type' : 'hidden',
				'name' : "omsPaymentif." + name,
				'id' : id,
				'value' : value
			}))
		});
	}
	// LG 인증
	var auth_LG = function() {
		var form = $("#payparam");
		var data = form.serialize();
		$.ajax({
			url : "/api/oms/pg/param",
			type : "POST",
			data : data
		}).done(function(response) {
			if (response.RESULT == "SUCCESS") {
				pay_callback(response.omsPaymentif);
			} else {
				alert(response.MESSAGE);
			}
		});
	}
	var pay_callback = function(data) {
		var form = $("#LGD_PAYINFO");
		form.html(""); // form 초기화
		common.mergeForms("LGD_PAYINFO", "payresult");
		$.each(data, function(key, value) {
			// console.log("pay_callback data key : "+key);
			// console.log("pay_callback data value : "+value);
			key = key.toUpperCase();
			if (key == "LGD_WINDOW_TYPE") {
				LGD_window_type = value;
			}
			if (key == "CST_PLATFORM") {
				CST_PLATFORM = value;
			}
			if (key == "LGD_VERSION") {
				value = "JSP_Non-ActiveX_CardBilling";
			}
			if (common.isNotEmpty(value)) {
				form.append($('<input>', {
					'type' : 'hidden',
					'name' : key,
					'id' : key,
					'value' : value
				}))
			}
		});
		form.find("#LGD_CHECKSSNYN").val("N"); // 생년월일/사업자번호 일치여부확인.
		pgData();
		launchCrossPlatform();
	}
	var callback_saveBilling = function(response) {
		var card = response.cardData;
		$(".auto_payment").find('b').html(card.regularPaymentBusinessNm);
	}
	var saveBilling = function() {
		var form = $("#payresult");
		var data = form.serialize();
		$.ajax({
			url : "/api/oms/order/billing/save",
			type : "POST",
			data : data
		}).done(function(response) {
			if (response.RESULT == "SUCCESS") {
				callback_saveBilling(response);
			} else {
				alert(response.MESSAGE);
			}
		});
	}
	// //////////////////////////////////////////// PG /////////////////////////////////////////////////////
	/*
	 * 수정불가.
	 */
	var LGD_window_type = "";
	var CST_PLATFORM = '';
	/*
	 * 수정불가
	 */
	function launchCrossPlatform() {
		lgdwin = openXpay(document.getElementById('LGD_PAYINFO'), CST_PLATFORM, LGD_window_type, null, "", "");
	}
	/*
	 * FORM 명만 수정 가능
	 */
	function getFormObject() {
		return document.getElementById("LGD_PAYINFO");
	}
	/*
	 * 인증결과 처리
	 */
	function payment_return() {
		var fDoc;
		fDoc = lgdwin.contentWindow || lgdwin.contentDocument;
		var resultForm = $("#payresult");
		if (fDoc.document.getElementById('LGD_RESPCODE').value == "0000") {
			resultForm.find("#LGD_RESPCODE").val(fDoc.document.getElementById('LGD_RESPCODE').value);
			resultForm.find("#LGD_RESPMSG").val(fDoc.document.getElementById('LGD_RESPMSG').value);
			resultForm.find("#LGD_BILLKEY").val(fDoc.document.getElementById('LGD_BILLKEY').value);
			resultForm.find("#LGD_PAYTYPE").val(fDoc.document.getElementById('LGD_PAYTYPE').value);
			resultForm.find("#LGD_PAYDATE").val(fDoc.document.getElementById('LGD_PAYDATE').value);
			resultForm.find("#LGD_FINANCECODE").val(fDoc.document.getElementById('LGD_FINANCECODE').value);
			resultForm.find("#LGD_FINANCENAME").val(fDoc.document.getElementById('LGD_FINANCENAME').value);
			// document.getElementById("LGD_PAYINFO").target = "_self";
			// document.getElementById("LGD_PAYINFO").action = "CardBillingAuth_Res.jsp";
			// document.getElementById("LGD_PAYINFO").submit();
			saveBilling();
			closeIframe();
		} else {
			alert("LGD_RESPCODE (결과코드) : " + fDoc.document.getElementById('LGD_RESPCODE').value + "\n" + "LGD_RESPMSG (결과메시지): " + fDoc.document.getElementById('LGD_RESPMSG').value);
			closeIframe();
		}
	}
//-->
</script>
<form method="post" name="payparam" id="payparam" style="display: none;">
	<input tyep="hidden" name="orderTypeCd" value="ORDER_TYPE_CD.REGULARDELIVERY" />
	<input type="hidden" name="LGD_BUYERSSN" id="LGD_BUYERSSN" value="" /><%-- 생년월일 6자리 or 사업자번호 --%>
	<input type="hidden" name="LGD_CHECKSSNYN" id="LGD_CHECKSSNYN" value="" /><%-- 생년월일/사업자번호 일치 여부 확인 --%>
	<c:if test="${!isMobile}">
		<input type="hidden" name="LGD_PAYWINDOWTYPE" id="LGD_PAYWINDOWTYPE" value="CardBillingAuth" /><%-- 인증요청구분 (수정불가)  --%>
		<input type="hidden" name="LGD_VERSION" id="LGD_VERSION" value="JSP_Non-ActiveX_CardBilling" /><%-- 사용타입 정보(수정 및 삭제 금지): 이 정보를 근거로 어떤 서비스를 사용하는지 판단할 수 있습니다.--%>
	</c:if>
	<c:if test="${isMobile}">
		<input type="hidden" name="LGD_PAYWINDOWTYPE" id="LGD_PAYWINDOWTYPE" value="CardBillingAuth_smartphone"/><!-- 인증요청구분 (수정불가)  -->
		<input type="hidden" name="LGD_VERSION" id="LGD_VERSION" value="JSP_SmartXPay_CardBilling"/>	<!-- 사용타입 정보(수정 및 삭제 금지): 이 정보를 근거로 어떤 서비스를 사용하는지 판단할 수 있습니다.-->
	</c:if>
</form>
<form id="payresult" style="display: none;">
	<%-- result --%>
	<input type="hidden" name="LGD_RESPCODE" id="LGD_RESPCODE" value="" />
	<input type="hidden" name="LGD_RESPMSG" id="LGD_RESPMSG" value="" />
	<input type="hidden" name="LGD_BILLKEY" id="LGD_BILLKEY" value="" />
	<input type="hidden" name="LGD_PAYTYPE" id="LGD_PAYTYPE" value="" />
	<input type="hidden" name="LGD_PAYDATE" id="LGD_PAYDATE" value="" />
	<input type="hidden" name="LGD_FINANCECODE" id="LGD_FINANCECODE" value="" />
	<input type="hidden" name="LGD_FINANCENAME" id="LGD_FINANCENAME" value="" />
</form>
<form method="post" name="LGD_PAYINFO" id="LGD_PAYINFO"></form>