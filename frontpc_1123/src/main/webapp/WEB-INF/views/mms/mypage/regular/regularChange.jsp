<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="func" uri="kr.co.intune.commerce.common.functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<fmt:setLocale value="ko"/>
<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY주문관리|정기배송 설정변경" name="pageNavi"/>
</jsp:include>

<!-- 정기배송 없을 시 정기배송 관리 페이지로 이동 -->
<c:if test="${empty regularList}">
	<div class="inner" onload="ccs.link.go('/mms/mypage/regular/history', CONST.SSL, CONST.LOGIN_CHECK_TYPE_MEMBER);">
	</div>
</c:if>
<div class="inner">
<div class="layout_type1">
<div class="column">
	<div class="myorder regular_setting">
		<c:set var="totalPrice" value = "0" />
		<c:set var="totalDeliveryFee" value = "0" />
		<c:forEach var="regular" varStatus="idx1" items="${regularList}">
		<fmt:parseDate value="${regular.insDt}" var="dateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
		<fmt:formatDate value="${dateFmt}" var="regularInsDt" pattern="yyyy-MM-dd"/>
		<h3 class="title_type1">정기배송 설정변경</h3>	
		<!-- 16.10.08 : 추가 -->
		<div class="borderBox">
			<dl class="order_date">
				<dt>주문일시</dt>
				<dd>
					<b>${regularInsDt}</b> <!-- 16.09.27 : 텍스트수정 -->
					<!-- <b class="time">10:00:00</b> 16.10.14 : 삭제 -->
				</dd>
			</dl>
			<dl>
				<dt>신청번호</dt>
				<dd>
					<b>${regular.regularDeliveryId}</b>
				</dd>
			</dl>
		</div>
		
		<div class="non_info">
			<div class="slide_tit1">
				<a href="#none" class="evt_tit">신청자정보</a>
			</div>
			<dl>
				<dt>${regular.name1}</dt>
				<dd>
					<c:if test="${not empty regular.phone2}">
						<span>${regular.phone2}<c:if test="${not empty regular.phone1}"> / </c:if></span>
					</c:if>
					<c:if test="${not empty regular.phone1}">
						<span>${regular.phone1}</span>
					</c:if>
				</dd>
			</dl>
		</div>

		<div class="non_info non_prod">
			<!-- 16.09.27 : 수정 -->
			<div class="slide_tit1">
				<span class="normal_tit pc_only">배송/상품정보</span>
				<a href="#none" class="evt_tit mo_only">배송/상품정보</a>
			</div>
			<!-- //16.09.27 : 수정 -->
			<dl>
				<dt>${regular.name1}</dt>
				<dd>
					<span>
					<c:if test="${not empty regular.phone2}">
						${regular.phone2} <c:if test="${not empty regular.phone1}"> | </c:if> 
					</c:if>
					${regular.phone1}<br />
					<em></em>(${regular.deliveryZipCd}) ${regular.deliveryAddress1} ${regular.deliveryAddress2} ${regular.deliveryAddress3}<br />
					${regular.note}
					</span>
					<a href="javascript:void(0);" class="btn_sStyle1 btn_changeAddr"
					data-regular-delivery-id = "${regular.regularDeliveryId}" 
					data-name1 = "${regular.name1}" 
					data-phone1 = "${regular.phone1}"
					data-phone2 = "${regular.phone2}"
					data-zip-cd = "${regular.deliveryZipCd}"
					data-address1 = "${regular.deliveryAddress1}"
					data-address2 = "${regular.deliveryAddress2}"
					data-address3 = "${regular.deliveryAddress3}"
					data-note = "${regular.note}" 
					onclick="$regular.delivery($(this));">배송지변경</a>
				</dd>
			</dl>				
				
			<div class="viewTblList">
				<div class="div_tb_thead3">
					<div class="tr_box">
						<span class="col1">상품명/옵션정보/수량</span>
						<span class="col2">결제예정금액</span>
						<span class="col3">관리</span>
					</div>
				</div>
				<ul class="div_tb_tbody3">
					<c:forEach var="product" varStatus="idx2" items="${regular.omsRegulardeliveryproducts}">
					<c:set var= "totalPrice" value="${totalPrice + (product.regularDeliveryPrice * product.orderQty)}"/>
					<c:if test="${product.regularDeliveryPrice * product.orderQty < product.pmsProduct.minDeliveryFreeAmt}">
						<c:set var= "totalDeliveryFee" value="${totalDeliveryFee + product.pmsProduct.deliveryFee}"/>
					</c:if>
					
					<c:forEach var="schedule" varStatus="idx3" items="${product.omsRegulardeliveryschedules}">
					<c:if test="${idx3.first}">
						<li>
							<div class="tr_box tr_idx ${idx2.index}">
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
											<em class="option_txt"
												data-regular-delivery-id ="${regular.regularDeliveryId}" 
												data-delivery-product-no ="${product.deliveryProductNo}" 
												data-regular-delivery-order ="${schedule.regularDeliveryOrder}" 
												data-product-id="${product.pmsProduct.productId}"
												data-product-name="${product.pmsProduct.name}"
												data-saleproduct-id="${product.pmsSaleproduct.saleproductId}"
												data-saleproduct-name="${product.pmsSaleproduct.name}"
												data-regular-delivery-price="${product.regularDeliveryPrice}"
												data-delivery-period-value="${product.deliveryPeriodValue}"
												data-increase-day="0"
												data-regular-delivery-dt="${schedule.regularDeliveryDt}"
												data-delivery-fee="${product.pmsProduct.deliveryFee}"
												data-min-delivery-free-amt="${product.pmsProduct.minDeliveryFreeAmt}"
												data-order-qty="${product.orderQty}">
												<i>${product.pmsSaleproduct.name}</i>
											</em>
										<%-- <c:if test="${product.deliveryProductTypeCd != 'DELIVERY_PRODUCT_TYPE_CD.SET'}">
										</c:if> --%>
										
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
						
								<div class="col3">
									<div class="stateBox" 
										data-product-id="${product.pmsProduct.productId}"
										data-saleproduct-id="${product.pmsSaleproduct.saleproductId}"
										data-qty="${product.orderQty}"
										data-tr-idx="${idx2.index}">
										<div class="btn_wrapC btn3ea">
											<a href="javascript:void(0);" class="btn_sStyle3 sGray2" onclick="$regular.change.option($(this));">옵션/수량변경</a>
											<div class="ly_box option_box" id="optionChangeLayer_${idx2.index}" style="display: none;"></div>
											<a href="#none" class="btn_sStyle3 sGray2" onclick="$regular.change.pass('${idx2.index}');">이번배송건너뛰기</a>
											<a href="#none" class="btn_sStyle3 sGray2" onclick="$regular.change.cancel('${idx2.index}');">정기배송 해지</a>
										</div>
									</div>
								</div>
	
								<dl class="regular_set">
									<dt>배송주기/횟수</dt>
									<dd class="half txt">
										${product.deliveryPeriodName} / ${product.deliveryCnt}회
									</dd>
								</dl>
								<dl class="regular_set">
									<dt>배송요일 </dt>
									<dd>
										<div class="select_box1"
											data-regular-delivery-dt="${schedule.regularDeliveryDt}"
											data-regular-delivery-order=" ${schedule.regularDeliveryOrder}"
											data-delivery-cnt="${product.deliveryCnt}">
											<label></label>
											<select onchange="$regular.change.deliveryDay('${idx2.index}', this.value, $(this));">
												<%-- <option value="2" ${product.deliveryPeriodValue eq 2 ? 'selected' : '' }>월요일</option> --%>
												<option value="3" ${product.deliveryPeriodValue eq 3 ? 'selected' : '' }>화요일</option>
												<option value="4" ${product.deliveryPeriodValue eq 4 ? 'selected' : '' }>수요일</option>
												<option value="5" ${product.deliveryPeriodValue eq 5 ? 'selected' : '' }>목요일</option>
												<option value="6" ${product.deliveryPeriodValue eq 6 ? 'selected' : '' }>금요일</option>
												<option value="7" ${product.deliveryPeriodValue eq 7 ? 'selected' : '' }>토요일</option>
												<%-- <option value="1" ${product.deliveryPeriodValue eq 1 ? 'selected' : '' }>일요일</option> --%>
											</select>
										</div>
									</dd>
								</dl>
	
								<div class="regular_info">
									<div class="regular_info02">
										<ul>
											<fmt:parseDate value="${schedule.scheduleOrderDt}" var="shipDateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
											<fmt:formatDate value="${shipDateFmt}" var="shipDt" pattern="yyyy-MM-dd"/>
											<fmt:formatDate value="${shipDateFmt}" var="shipDay" type="both" pattern="E"/>
											<li class="nextOrderDay">
												<span>결제예정일</span>${shipDt} (${shipDay})  / ${product.deliveryCnt}회중 ${schedule.regularDeliveryOrder}회차
											</li>
											<li class="nextDeliveryDay">
												<span>다음배송 예정일</span>${schedule.regularDeliveryDt} (${fn:substring(product.deliveryDay,0,1) })
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
		</div>
		</c:forEach>
		<div class="payment_info mtoggleBox on" >
			<h4 class="sub_tit1 toggleBtn">결제정보</h4>

			<div class="box toggleCont">
				<div class="columnL">
					<dl class="total">
						<dt>상품금액</dt>
						<dd>${func:price(totalPrice,'')}<i>원</i></dd>
						<dt>배송비</dt>
						<dd>
							<em class="plus">+ ${func:price(totalDeliveryFee,'')}</em><i>원</i>
						</dd>
					</dl>
				</div>

				<div class="columnR">
					<div class="payNpoint">
						<dl class="money">
							<dt>총 결제금액</dt>
							<dd>${func:price(totalPrice + totalDeliveryFee,'')}<i>원</i></dd>
						</dl>
					</div>
					<ul>
						<li>
 							<span id="regularPaymentBusinessNmTxt">${regularPaymentBusinessNm}</span>
							<a href="javascript:void(0);" class="btn_sStyle3" onclick="javascript:auth_LG()" >결제정보 변경</a>
						</li>
 					</ul>
					
				</div>
			</div>
		</div>
		<!-- //16.10.08 : 추가 -->

		<div class="btn_wrapC btn2ea">
			<a href="#none" class="btn_mStyle1 sWhite1">취소</a>
			<a href="#none" class="btn_mStyle1 sPurple1" onclick="javascript:$regular.update.info();">변경완료</a>
		</div>
		
		<!-- 16.11.11 -->
		<div class="btn_wrapC btn1ea">
			<a href="/mms/mypage/regular/history" class="btn_mStyle1 sWhite1 btn_repeat">정기배송 신청 내역</a>
		</div>
		<!-- //16.11.11 -->
		
	</div>
</div>


<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
</div>
</div>
<script type="text/javascript">
<!--
	$(function() {
		$('.lnb ul:eq(0) li:eq(2)').addClass('on');
	});
//-->
</script>
<!-- <script language="javascript" src="http://xpay.lgdacom.net/xpay/js/xpay_crossplatform.js" type="text/javascript" charset="utf-8"></script> -->
<script type="text/javascript">
var __isMobile = global.channel.isMobile;
var pgData = function(){
	var mergeForm = $("#mergeForm");
	var form = $("#LGD_PAYINFO").find("input");
	
	form.each(function(){
		var name = $(this).attr("name");
		var id = $(this).attr("id");
		var value = $(this).attr("value");
		mergeForm.append($('<input>',{
			'type' : 'hidden',
			'name' : "omsPaymentif."+name,
			'id' : id,
			'value' : value
		}))
	});
}

//LG 인증
var auth_LG = function(){		
	
	var form = $("#payparam");
	
	var data = form.serialize();
	
	$.ajax({ 				
		url : oms.url.pgParam,
		type : "POST",		
		data : data
	}).done(function(response){
		if(response.RESULT == "SUCCESS"){
			pay_callback(response.omsPaymentif);	
		}else{
			alert(response.MESSAGE);
		}
		
	});
}

var pay_callback = function(data){
	
	var form = $("#LGD_PAYINFO");
	form.html("");	//form 초기화
	
	common.mergeForms("LGD_PAYINFO","payresult");
	
// 	console.log("pay_callback data : ");
// 	console.log(data);
	$.each(data,function(key,value){
// 		console.log("pay_callback data key : "+key);
// 		console.log("pay_callback data value : "+value);
		key = key.toUpperCase();
		
		if(key == "LGD_WINDOW_TYPE"){
			LGD_window_type = value;
		}
		if(key == "CST_PLATFORM"){
			CST_PLATFORM = value;
		}	
		if(key == "LGD_VERSION"){
			value = "JSP_Non-ActiveX_CardBilling";
		}
// 		if(key == "LGD_RETURNURL" || key == "LGD_PAYWINDOWTYPE" || key == "LGD_WINDOW_TYPE" || key == "LGD_MID" || key == "LGD_VERSION"){
			
			if(common.isNotEmpty(value)){
				form.append($('<input>',{
					'type' : 'hidden',
					'name' : key,
					'id' : key,
					'value' : value
				}))
			}
// 		}
		
	});
	
	if(__isMobile == "true"){
		form.find("#LGD_PAYWINDOWTYPE").val("CardBillingAuth_smartphone");
	}else{
		form.find("#LGD_PAYWINDOWTYPE").val("CardBillingAuth");
	}
	
	form.find("#LGD_CHECKSSNYN").val("N");	//생년월일/사업자번호 일치여부확인.		
	console.log(form);
	pgData();
			
	launchCrossPlatform();	
}

var callback_saveBilling = function(response){
// 	console.log("saveBilling",response.cardData);
	var card = response.cardData;
	$("#saveForm").find("#billingKey").val(card.billingKey);
	$("#saveForm").find("#regularPaymentBusinessCd").val(card.regularPaymentBusinessCd);
	$("#saveForm").find("#regularPaymentBusinessNm").val(card.regularPaymentBusinessNm);
	$("#regularPaymentBusinessNmTxt").html(card.regularPaymentBusinessNm);
	$("#btnTxt_pay").html("변경");
	//결제정보세팅
}

var saveBilling = function(){
	var form = $("#payresult");
	
	var data = form.serialize();
	
	$.ajax({ 				
		url : "/api/oms/order/billing/save",
		type : "POST",		
		data : data
	}).done(function(response){
		if(response.RESULT == "SUCCESS"){			
			callback_saveBilling(response);	
		}else{
			alert(response.MESSAGE);
		}
		
	});
}
////////////////////////////////////////////// PG /////////////////////////////////////////////////////
/*
* 수정불가.
*/
	var LGD_window_type = "";
	var CST_PLATFORM = '';
/*
* 수정불가
*/
function launchCrossPlatform(){	
	lgdwin = openXpay(document.getElementById('LGD_PAYINFO'), CST_PLATFORM, LGD_window_type, null, "", "");
}
/*
* FORM 명만  수정 가능
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

// 			document.getElementById("LGD_PAYINFO").target = "_self";
// 			document.getElementById("LGD_PAYINFO").action = "CardBillingAuth_Res.jsp";
// 			document.getElementById("LGD_PAYINFO").submit();

			saveBilling();
		closeIframe();
	} else {
		alert("LGD_RESPCODE (결과코드) : " + fDoc.document.getElementById('LGD_RESPCODE').value + "\n" + "LGD_RESPMSG (결과메시지): " + fDoc.document.getElementById('LGD_RESPMSG').value);
		closeIframe();
	}
}

$(function(){
    $('div[onload]').trigger('onload');
});

</script>
<form method="post" name="payparam" id="payparam" style="display: none;">
	<input type="text" name="LGD_BUYERSSN" id="LGD_BUYERSSN" value=""/>			<!-- 생년월일 6자리 or 사업자번호 -->
	<input type="text" name="LGD_CHECKSSNYN" id="LGD_CHECKSSNYN" value=""/>	<!-- 생년월일/사업자번호 일치 여부 확인 -->
	<input type="text" name="LGD_PAYWINDOWTYPE" id="LGD_PAYWINDOWTYPE" value="CardBillingAuth"/><!-- 인증요청구분 (수정불가)  -->
	<input type="text" name="LGD_VERSION" id="LGD_VERSION" value="JSP_Non-ActiveX_CardBilling"/>	<!-- 사용타입 정보(수정 및 삭제 금지): 이 정보를 근거로 어떤 서비스를 사용하는지 판단할 수 있습니다.-->
	<input tyep="text" name="orderTypeCd" value="ORDER_TYPE_CD.REGULARDELIVERY"/>			 	
</form>
<form id="payresult" style="display: none;">
	<!-- result -->
	<input type="text" name="LGD_RESPCODE" id="LGD_RESPCODE" value=""/>
	<input type="text" name="LGD_RESPMSG" id="LGD_RESPMSG" value=""/>
	<input type="text" name="LGD_BILLKEY" id="LGD_BILLKEY" value=""/>	
	<input type="text" name="LGD_PAYTYPE" id="LGD_PAYTYPE" value=""/>    
    <input type="text" name="LGD_PAYDATE" id="LGD_PAYDATE" value=""/>
    <input type="text" name="LGD_FINANCECODE" id="LGD_FINANCECODE" value=""/>
    <input type="text" name="LGD_FINANCENAME" id="LGD_FINANCENAME" value=""/>    
</form>
<form method="post" name="LGD_PAYINFO" id="LGD_PAYINFO"></form>