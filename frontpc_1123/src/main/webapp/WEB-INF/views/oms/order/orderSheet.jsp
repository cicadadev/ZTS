<%--
	화면명 : 주문서
	작성자 : dennis
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags" %>
<%@ taglib prefix="func" uri="kr.co.intune.commerce.common.functions" %>

<%@ page import="intune.gsf.common.constants.BaseConstants" %>
<%	
response.setHeader("Cache-Control","no-cache");
response.setHeader("Pragma","no-cache"); 
response.setDateHeader("Expires",0);	
pageContext.setAttribute("checkOrder", session.getAttribute(BaseConstants.SESSION_KEY_CHECK_ORDER));
%>
<script type="text/javascript" src="/resources/js/common/order.ui.js"></script>
<input type="hidden" id="checkOrder" value="${checkOrder}"/>

<script type="text/javascript">

//배송지 상품 template
var productTemplate = function(num){
	
	var cnt = getDeliveryCnt();	
	
	var ops = getOrderproductNos();
	
	var html = '';
	
	var wrapTogetherYn = 'N';
	
	
	html += '<div class="viewTblList">\
				<!-- ### 테이블 헤더 ### -->\
				<div class="div_tb_thead3">\
					<div class="tr_box">\
						<span class="col1">상품/옵션정보</span>\
			\
						<span class="col2">수량</span>\
			\
						<span class="col3">주문금액</span>\
			\
						<span class="col4">배송비</span>\
					</div>\
				</div>\
				<!-- ### //테이블 헤더 ### -->\
				<!-- ### 테이블 바디 ### -->\
				<ul class="div_tb_tbody3">\
		   ';
	var wrapYnCnt = 0;
	var preDeliveryPolicyNo = "";
	for(var i=0;i<ops.length;i++){
		
		var form = "#productForm"+ops[i].value;
		
		var productName = $(form).find("#productName").val();
		
		var saleproductNames = $(form).find("input[name=saleproductName]");
		var orderQty = $(form).find("#orderQty").val();
		var salePrice = $(form).find("#salePrice").val();
		var addSalePrice = $(form).find("#addSalePrice").val();
		var totalSalePrice = Number(salePrice) + Number(addSalePrice);		//총판매가(개당)
		var deliveryFee = $(form).find("#deliveryFee").val();
		var minDeliveryFreeAmt = $(form).find("#minDeliveryFreeAmt").val();
		var deliveryPolicyNo = $(form).find("#deliveryPolicyNo").val();
		var wrapYn = $(form).find("#wrapYn").val();
		var orderDeliveryFee = $(form).find("#orderDeliveryFee"+deliveryPolicyNo).val();		
		var paymentAmt = Number(totalSalePrice) * Number(orderQty);	//최종결제가()
		
		var productId = $(form).find("#productId").val();
		var saleproductId = $(form).find("#saleproductId").val();
		var productImg = $(form).find("#productImg").val();
		
		var offshopId = $(form).find("#offshopId").val();
		var styleNo = $(form).find("#styleNo").val();
		
		if(common.isEmpty(orderDeliveryFee)){
			orderDeliveryFee = 0;
		}
		
		var saleproductNameTxt = "";
		var saleproductName = "";
		for(var j=0;j<saleproductNames.length;j++){
			saleproductNameTxt += '<em class="option_txt"><i>'+saleproductNames[j].value+'</i></em>';
			saleproductName += " " +saleproductNames[j].value;
		}
		
		var orderOptions = "<option selected value='0'>선택</option>";
		for(var j=0;j<Number(orderQty);j++){
			var value = j+1;
			orderOptions += '<option value='+value+'>'+value+'</option>';
		}
		
		if(wrapYn == 'Y'){
			wrapTogetherYn = 'Y';
			wrapYnCnt++;
		}
		
		if(deliveryPolicyNo != preDeliveryPolicyNo){
			html += '<li>';
		}
		
		html += '\
					<div class="tr_box">\
						<div class="col1">\
							<div class="positionR">\
								<div class="prod_img">\
									<a href="#none" onclick="javascript:ccs.link.product.detail('+productId+')">\
										<img src="'+productImg+'" alt="" />\
									</a>\
								</div>\
			\
								<a href="#none" class="title" onclick="javascript:ccs.link.product.detail('+productId+')">\
									'+productName+'\
								</a>\
								'+saleproductNameTxt+'\
								<div class="piece">\
									<span class="pieceNum">1개</span>\
									<span class="slash">/</span>\
									<span class="piecePrice">'+common.priceFormat(totalSalePrice,false)+'<i>원</i></span>\
								</div>\
							</div>\
							';
			
			//사은품		
// 			var presentNames = $(form).find("#presentNames").val();
			
// 			if(common.isNotEmpty(presentNames)){
// 					html += '\
// 							<u class="gift_txt">\
// 								<span class="btn_tb_gift">\
// 									<span class="icon_type1 iconBlue3">사은품</span>'+presentNames+'\
// 								</span>\
// 							</u>\
// 							';
// 			}
			
			var presentNames = $(form).find("input[name=presentNames]");
			
			presentNames.each(function(){
				
					html += '\
							<u class="gift_txt">\
								<span class="btn_tb_gift">\
									<span class="icon_type1 iconBlue3">사은품</span>'+$(this).attr("value")+'\
								</span>\
							</u>\
							';
			
			})
			
						
			
			if(wrapYn == 'Y'){
					html += '\
							<u class="gift_txt bg_none">\
								<span class="btn_tb_gift">\
									<label class="chk_style1">\
										<em>\
											<input type="checkbox" name="checkWrap'+num+'" id="checkWrap_'+num+'_'+ops[i].value+'" value="'+ops[i].value+'" onclick="javascript:chgWrap()"/>\
										</em>\
										<span>선물포장</span>\
									</label>\
									<span class="icon_present">선물포장 신청</span><a href="#none" class="btn_infor btn_giftInfo">안내</a>\
								</span>\
							</u>\
							';
			}
			
				html +='</div>\
						<div class="col2">\
							';
						
			if(Number(cnt) > 1){	//배송지가 여러군데일때 상품 수량 선택
				html += '\
							<div class="slc_quantity">\
								<div class="sel_box select_style1">\
									<label for="selOrderQty'+ops[i].value+'"></label>\
									<select id="selOrderQty'+ops[i].value+'" onchange="javascript:chgDeliveryQty(\''+ops[i].value+'\',\''+orderQty+'\',\''+num+'\',\''+deliveryPolicyNo+'\')">\
										'+orderOptions+'\
									</select>\
								</div>\
								<span class="slash">/</span>\
								'+orderQty+'\
							</div>\
						';
			}else{
				html += '\
							<div class="quantity_result">\
								'+orderQty+'개\
							</div>\
						';
			}
				html += '</div>\
						<div class="col3">\
							<span class="price">\
								<em>'+common.priceFormat(paymentAmt,false)+'<i>원</i></em>\
							</span>\
						</div>\
						<div class="col4 rowspan">\
							<div class="cell">\
								<span class="price">\
									<em>';
									
									if(Number(orderDeliveryFee) == 0){
										html += '무료';
									}else{
										html += common.priceFormat(orderDeliveryFee,false)+'<i>원</i>';
									}
									
							html += '</em>\
								</span>\
							</div>\
						</div>\
					</div>\
					';
					
		if(Number(cnt) > 1){
			html += '\
					<!-- ### //테이블 바디 ### -->\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].orderQty" id="orderQty'+ops[i].value+'" value="0"/>\
					<input type="hidden" name="orderDeliveryFee" id="orderDeliveryFee'+ops[i].value+'" value="0" />\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].paymentAmt" id="paymentAmt'+ops[i].value+'" value="0" />\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].paymentAmt" id="temppaymentAmt'+ops[i].value+'" value="0" />\
					';
		}else{
			html += '\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].orderQty" id="orderQty'+ops[i].value+'" value="'+orderQty+'"/>\
					<input type="hidden" name="orderDeliveryFee" id="orderDeliveryFee'+ops[i].value+'" value="'+orderDeliveryFee+'" />\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].paymentAmt" id="paymentAmt'+ops[i].value+'" value="'+paymentAmt+'" />\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].paymentAmt" id="temppaymentAmt'+ops[i].value+'" value="'+paymentAmt+'" />\
					';
		}
		
		html += '\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].orderProductNo" id="orderProductNo" value="'+ops[i].value+'"/>\
				   	<input type="hidden" name="addrOrderProductNo" id="addrOrderProductNo" value="'+ops[i].value+'"/>\
				   	<input type="hidden" name="deliveryPolicyNo" id="deliveryPolicyNo'+ops[i].value+'" value="'+deliveryPolicyNo+'"/>\
				   	<input type="hidden" name="deliveryFee" id="deliveryFee'+ops[i].value+'" value="'+deliveryFee+'" />\
				   	<input type="hidden" name="minDeliveryFreeAmt" id="minDeliveryFreeAmt'+ops[i].value+'" value="'+minDeliveryFreeAmt+'" />\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].totalSalePrice" id="totalSalePrice'+ops[i].value+'" value="'+totalSalePrice+'" />\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].productCouponIssueNo" id="productCouponIssueNo'+ops[i].value+'" value="" />\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].productCouponDcAmt" id="productCouponDcAmt'+ops[i].value+'" value="0" />\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].tempproductCouponDcAmt" id="tempproductCouponDcAmt'+ops[i].value+'" value="0" />\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].productCouponId" id="productCouponId'+ops[i].value+'" value="" />\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].plusCouponIssueNo" id="plusCouponIssueNo'+ops[i].value+'" value="" />\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].plusCouponDcAmt" id="plusCouponDcAmt'+ops[i].value+'" value="0" />\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].tempplusCouponDcAmt" id="tempplusCouponDcAmt'+ops[i].value+'" value="0" />\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].plusCouponId" id="plusCouponId'+ops[i].value+'" value="" />\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].orderCouponIssueNo" id="orderCouponIssueNo'+ops[i].value+'" value="" />\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].orderCouponDcAmt" id="orderCouponDcAmt'+ops[i].value+'" value="0" />\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].calibrateOrderDcAmt" id="calibrateOrderDcAmt'+ops[i].value+'" value="0" />\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].temporderCouponDcAmt" id="temporderCouponDcAmt'+ops[i].value+'" value="0" />\
					<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].orderCouponId" id="orderCouponId'+ops[i].value+'" value="" />\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].wrapYn" id="wrapYn'+ops[i].value+'" value="N"/>\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].productId" id="productId'+ops[i].value+'" value="'+productId+'" />\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].saleproductId" id="saleproductId'+ops[i].value+'" value="'+saleproductId+'" />\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].productName" id="productName'+ops[i].value+'" value="'+productName+'" />\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].saleproductName" id="saleproductName'+ops[i].value+'" value="'+saleproductName+'" />\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].offshopId" id="offshopId'+ops[i].value+'" value="'+offshopId+'" />\
				   	<input type="hidden" name="omsDeliveryaddresss['+num+'].omsOrderproducts['+i+'].styleNo" id="styleNo'+ops[i].value+'" value="'+styleNo+'" />\
			   ';
				   	
				   	
// 					<input type="hidden" name="deliveryCouponId" id="deliveryCouponId'+ops[i].value+'" value="" />\
// 				   	<input type="hidden" name="deliveryCouponIssueNo" id="deliveryCouponIssueNo'+ops[i].value+'" value="" />\
// 				   	<input type="hidden" name="deliveryCouponDcAmt" id="deliveryCouponDcAmt'+ops[i].value+'" value="0" />\
// 				   	<input type="hidden" name="applyDeliveryFee" id="applyDeliveryFee'+ops[i].value+'" value="0" />\

		if(i+1 == ops.length){
			html += '</li>';
		}else{
			var nextForm = "#productForm"+ops[i+1].value;
			var nextDeliveryPolicyNo = $(nextForm).find("#deliveryPolicyNo").val();
			if(nextDeliveryPolicyNo != deliveryPolicyNo){
				html += '</li>';
			}
		}
		
		preDeliveryPolicyNo = deliveryPolicyNo;
	}
	
	
	html += 	'\
				</ul>\
				<!-- ### //테이블 바디 ### -->\
			</div>\
		  ';
			
	//선물포장
	if(wrapTogetherYn == 'Y'){
		if(wrapYnCnt > 1){	
		html += '\
			<div class="gift_packing" id="giftDiv" style="display: none;">\
				<p><span class="icon_present">선물포장</span> 부피가 작은 상품은 합 포장 신청이 가능합니다. 상품을 함께 포장하시겠습니까?</p>\
				<label class="radio_style1">\
					<em>\
						<input type="radio" name="packing'+num+'" value="N" onclick="javascript:chgWrap()" checked="checked"/>\
					</em>\
					<span>개별포장</span>\
				</label>\
				<label class="radio_style1">\
					<em>\
						<input type="radio" name="packing'+num+'" value="Y" onclick="javascript:chgWrap()"/>\
					</em>\
					<span>합포장</span>\
				</label> <a href="#none" class="btn_infor btn_gift_packing">안내</a>\
			</div>\
				';
		}else{
			html += '<input type="radio" name="packing'+num+'" value="N" style="display:none;" checked="checked"/>';
		}
	}
		
	

	
	return html;
}

//배송지 template
var addressFormTemplate = function(num){

	var form = $("#defaultMemberAddress");
	var dName = form.find("#name").val();
	var dDeliveryName = form.find("#deliveryName1").val();
	var dCountryNo = form.find("#countryNo").val();
	var dPhone1 = form.find("#phone1").val();
	var dPhone2 = form.find("#phone2").val();
	var dZipCd = form.find("#zipCd").val();
	var dAddress1 = form.find("#address1").val();
	var dAddress2 = form.find("#address2").val();
	var dAddress = dAddress1 + " " + dAddress2;
	var dEmail = form.find("#email").val();
	
	var cnt = getDeliveryCnt();
	
	var orderTypeCd = $("#saveOrderForm").find("#orderTypeCd").val();
	
	var orderName = $("#orderForm").find("#name1").val(); 
		
	var showCls = "";
	
	var phHead1 = getPhoneHead("1");
	var phHead2 = getPhoneHead("2");
	var emailDomain = getEmailDomain();
	
	
	var	html = "";
		
				//START ORDER TYPE CD
				if("ORDER_TYPE_CD.GIFT" == orderTypeCd){
					html += '\
							<form name="addressForm" id="addressForm'+num+'">\
							<input type="hidden" name="omsDeliveryaddresss['+num+'].deliveryAddressNo" id="deliveryAddressNo" value="'+num+'"/>\
							';
				}else{
						html += '<form name="addressForm" id="addressForm'+num+'">\
								 <div class="order_form">\
									<div class="relBox">\
									';
									
									if(cnt>1){
										html +=	'<h3 class="sub_tit1">배송지/주문정보 '+(num+1)+'</h3>';
									}else{
										html +=	'<h3 class="sub_tit1">배송지/주문정보</h3>';
									}										
							html += '	<input type="hidden" name="omsDeliveryaddresss['+num+'].deliveryAddressNo" id="deliveryAddressNo" value="'+num+'"/>\
										<div class="rw_tbBox">\
										';
											
						if(memberYn == 'Y'){		
							html +='			<input type="hidden" id="addressNoIdx" value="DEFAULT"/>\
												<ul class="rw_tb_tbody2">\
													<li>\
														<div class="tr_box">\
															<div class="col1">\
																<span class="group_inline">주문고객</span>\
															</div>\
															<div class="col2">\
																<div class="group_block">\
																	'+orderName+'\
																</div>\
															</div>\
														</div>\
													</li>\
													<li id="selDeliveryLi">\
														<div class="tr_box">\
															<div class="col1">\
																<span class="group_inline">배송지 선택</span>\
															</div>\
															<div class="col2">\
																<div class="group_block">\
																	<label class="radio_style1">\
																		<em>\
																			<input type="radio" name="ra1_2_1" value="STD"  onclick="javascript:chgDeliveryAddr(this)" checked="checked"/>\
																		</em>\
																		<span>기본 배송지</span>\
																	</label>\
																	<label class="radio_style1">\
																		<em>\
																			<input type="radio" name="ra1_2_1" value="NEW" onclick="javascript:chgDeliveryAddr(this)"/>\
																		</em>\
																		<span>새로운 배송지</span>\
																	</label>\
																</div>\
															</div>\
														</div>\
													</li>\
													<div id="stdDelivery">\
													<li>\
														<div class="tr_box">\
															<div class="col1">\
																<span class="group_inline">이름</span>\
															</div>\
															<style>\
																.pc .rw_tb_tbody2 li .tr_box > div .group_block .btn_sStyle4{margin-left:14px;}\
															</style>\
															<div class="col2">\
																<div class="group_block"  id="deliveryNameTxt">\
																'+dDeliveryName+'<a href="#none" class="btn_sStyle4 sGray2" onclick="javascript:addressOpen(\''+num+'\')"><strong>배송지 목록</strong></a>\
																</div>\
															</div>\
														</div>\
													</li>\
													<li>\
														<div class="tr_box">\
															<div class="col1">\
																<span class="group_inline">연락처</span>\
															</div>\
															<div class="col2">\
																<div class="group_block" id="phoneTxt">'+dPhone2+' / '+dPhone1+'</div>\
															</div>\
														</div>\
													</li>\
													<li>\
														<div class="tr_box">\
															<div class="col1">\
																<span class="group_inline">배송지</span>\
															</div>\
															<div class="col2">\
																<div class="group_block" id="addressTxt">('+dZipCd+') '+dAddress+'</div>\
															</div>\
														</div>\
													</li>\
													<li>\
														<div class="tr_box">\
															<div class="col1">\
																<span class="group_inline">배송 요청사항</span>\
															</div>\
															<div class="col2">\
																<div class="group_block msgBox">\
																	<div>\
																		<div class="sel_box select_style1">\
																			<label for="slc_deliveryMsg"></label>\
																			'+getDeliveryMessage(num,"STD")+'\
																		</div>\
																	</div>\
																	<div id="stdnoteDiv"  style="display: none;">\
																		<input type="text" id="stdnote" value="배송전 연락주세요." class="input_style2" maxlength="2000"/>\
																	</div>\
																</div>\
															</div>\
														</div>\
													</li>\
													<input type="hidden" id="stddeliveryName1" value="'+dDeliveryName+'"/>\
													<input type="hidden" id="stdphone1" value="'+dPhone1+'"/>\
													<input type="hidden" id="stdphone2" value="'+dPhone2+'"/>\
													<input type="hidden" id="stdzipCd" value="'+dZipCd+'"/>\
													<input type="hidden" id="stdaddress1" value="'+dAddress1+'"/>\
													<input type="hidden" id="stdaddress2" value="'+dAddress2+'"/>\
													<input type="hidden" id="stdcountryNo" value="'+dCountryNo+'"/>\
													<input type="hidden" id="stdemail" value="'+dEmail+'"/>\
													</div>\
													<div id="newDelivery" style="display:none;">\
													<li>\
														<div class="tr_box">\
															<div class="col1">\
																<span class="group_inline"><span class="ico_requisite">필수요소</span> 이름</span>\
															</div>\
															<div class="col2">\
																<div class="group_block">\
																	<input type="text" id="deliveryName1" value="" class="input_style2" maxlength="10"> <a href="#none" class="btn_sStyle4 sGray2" onclick="javascript:addressOpen(\''+num+'\',\'NEW\')"><strong>배송지 목록</strong></a>\
																</div>\
															</div>\
														</div>\
													</li>\
													<li>\
														<div class="tr_box">\
															<div class="col1">\
																<span class="group_inline"><span class="ico_requisite">필수요소</span> 휴대전화</span>\
															</div>\
															<div class="col2">\
																<div class="group_block">\
																	<div class="inputMix_style2">\
																		<input type="hidden" id="'+num+'phone2" value="" />\
																		<div class="select_box1">\
																			<label id="'+num+'phone2_areaCode">010</label>\
																	 		'+phHead2+'\
																		</div>\
																		<span class="hyphen">-</span>\
																		<div class="inputTxt_place1">\
																			<label></label>\
																			<span>\
																			<input type="text" value="" id="'+num+'phone2_num1" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, \'number\');"onblur="ccs.common.fn_press_han(this);">\
																			</span>\
																		</div>\
																		<span class="hyphen">-</span>\
																		<div class="inputTxt_place1">\
																			<label></label>\
																			<span>\
																				<input type="text" value="" id="'+num+'phone2_num2" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, \'number\');"onblur="ccs.common.fn_press_han(this);">\
																			</span>\
																		</div>\
																	</div>\
																	<div class="block_type2">\
																		<div class="inputMix_style2">\
																			<input type="hidden" id="'+num+'phone1" value="" />\
																			<div class="select_box1">\
																		 		<label id="'+num+'phone1_areaCode">02</label>\
																		 		'+phHead1+'\
																			</div>\
																			<span class="hyphen">-</span>\
																			<div class="inputTxt_place1">\
																				<label></label>\
																				<span>\
																				<input type="text" value="" id="'+num+'phone1_num1" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, \'number\');"onblur="ccs.common.fn_press_han(this);">\
																				</span>\
																			</div>\
																			<span class="hyphen">-</span>\
																			<div class="inputTxt_place1">\
																				<label></label>\
																				<span>\
																					<input type="text" value="" id="'+num+'phone1_num2" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, \'number\');"onblur="ccs.common.fn_press_han(this);">\
																				</span>\
																			</div>\
																		</div>\
																	</div>\
																</div>\
															</div>\
														</div>\
													</li>\
													<li>\
														<div class="tr_box">\
															<div class="col1">\
																<span class="group_inline"><span class="ico_requisite">필수요소</span> 배송지</span>\
															</div>\
															<div class="col2">\
																<div class="group_block addrBox">\
																	<div class="zip">\
																		<input type="text" id="zipCd" value="" readonly="readonly" class="input_style2">\
																		<a href="#none" class="btn_sStyle4 sGray2" onclick="javascript:openAddressPopup(\''+num+'\')"><strong>주소검색</strong></a>\
																	</div>\
																	<div>\
																		<input type="text" id="address1" value="" readonly="readonly" class="input_style2">\
																		<input type="text" id="address2" value="" class="input_style2" maxlength="100">\
																	</div>\
																</div>\
															</div>\
														</div>\
													</li>\
													<li>\
														<div class="tr_box">\
															<div class="col1">\
																<span class="group_inline">배송 요청사항</span>\
															</div>\
															<div class="col2">\
																<div class="group_block msgBox">\
																	<div>\
																		<div class="sel_box select_style1">\
																			<label></label>\
																			'+getDeliveryMessage(num,"NEW")+'\
																		</div>\
																	</div>\
																	<div id="noteDiv"  style="display: none;">\
																		<input type="text" id="note" value="배송전 연락주세요." class="input_style2" maxlength="2000"/>\
																	</div>\
																</div>\
															</div>\
														</div>\
													</li>\
													</div>\
												</ul>\
										';
						}else{							
								html += '		<ul class="rw_tb_tbody2">\
													<li>\
														<div class="tr_box">\
															<div class="col1">\
																<span class="group_inline"><span class="ico_requisite">필수요소</span> 받으실분</span>\
															</div>\
															<div class="col2">\
																<div class="group_block">\
																	<label class="chk_style1">\
																		<em>\
																			<input type="checkbox" value="" onclick="javascript:copyOrderAddr(\''+num+'\')"/>\
																		</em>\
																		<span>주문자와 동일</span>\
																	</label>\
																</div>\
															</div>\
														</div>\
													</li>\
													<li>\
														<div class="tr_box">\
															<div class="col1">\
																<span class="group_inline"><span class="ico_requisite">필수요소</span> 이름</span>\
															</div>\
															<div class="col2">\
																<div class="group_block">\
																	<input type="text" id="deliveryName1" value="" class="input_style2"  maxlength="10">\
																</div>\
															</div>\
														</div>\
													</li>\
													<li>\
														<div class="tr_box">\
															<div class="col1">\
																<span class="group_inline"><span class="ico_requisite">필수요소</span> 휴대전화</span>\
															</div>\
															<div class="col2">\
																<div class="group_block">\
																	<div class="inputMix_style2">\
																		<input type="hidden" id="'+num+'phone2" value="" />\
																		<div class="select_box1">\
																	 		<label id="'+num+'phone2_areaCode">010</label>\
																	 		'+phHead2+'\
																		</div>\
																		<span class="hyphen">-</span>\
																		<div class="inputTxt_place1">\
																			<label></label>\
																			<span>\
																			<input type="text" value="" id="'+num+'phone2_num1" maxlength="4" onblur="ccs.common.fn_press_han(this);"  onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, \'number\');">\
																			</span>\
																		</div>\
																		<span class="hyphen">-</span>\
																		<div class="inputTxt_place1">\
																			<label></label>\
																			<span>\
																				<input type="text" value="" id="'+num+'phone2_num2" maxlength="4" onblur="ccs.common.fn_press_han(this);"  onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, \'number\');">\
																			</span>\
																		</div>\
																	</div>\
																</div>\
															</div>\
														</div>\
													</li>\
													<li>\
														<div class="tr_box">\
															<div class="col1">\
																<span class="group_inline"><span class="ico_requisite">필수요소</span> 이메일</span>\
															</div>\
															<div class="col2">\
																<div class="group_block">\
																	<div class="mailBox">\
																		<input type="hidden" id="'+num+'email" value=""/>\
																		<div class="inputTxt_place1">\
																			<label class="mo_only">이메일</label>\
																			<span>\
																				<input type="text" id="'+num+'email_email1" value="" maxlength="100"/>\
																			</span>\
																		</div>\
																		<span class="at">@</span>\
																		<div class="inputTxt_place1">\
																			<label></label>\
																			<span>\
																				<input type="text" id="'+num+'email_email2" value="" maxlength="100"/>\
																			</span>\
																		</div>\
																		<div class="select_box1">\
																	 		<label id="'+num+'email_domain"></label>\
																	 		'+emailDomain+'\
																		</div>\
																	</div>\
																</div>\
															</div>\
														</div>\
													</li>\
													<li>\
														<div class="tr_box">\
															<div class="col1">\
																<span class="group_inline"><span class="ico_requisite">필수요소</span> 배송지</span>\
															</div>\
															<div class="col2">\
																<div class="group_block addrBox">\
																	<div class="zip">\
																		<input type="text" id="zipCd" value="" readonly="readonly" class="input_style2">\
																		<a href="#none" class="btn_sStyle4 sGray2" onclick="javascript:openAddressPopup(\''+num+'\')"><strong>주소검색</strong></a>\
																	</div>\
																	<div>\
																		<input type="text" id="address1" value="" readonly="readonly" class="input_style2">\
																		<input type="text" id="address2" value="" class="input_style2">\
																	</div>\
																</div>\
															</div>\
														</div>\
													</li>\
													<li>\
														<div class="tr_box">\
															<div class="col1">\
																<span class="group_inline">배송 요청사항</span>\
															</div>\
															<div class="col2">\
																<div class="group_block msgBox">\
																	<div>\
																		<div class="sel_box select_style1">\
																			<label></label>\
																			'+getDeliveryMessage(num,"NEW")+'\
																		</div>\
																	</div>\
																	<div id="noteDiv"  style="display: none;">\
																		<input type="text" id="note" value="배송전 연락주세요." class="input_style2" maxlength="2000"/>\
																	</div>\
																</div>\
															</div>\
														</div>\
													</li>\
												</ul>\
												';
						}
											
					html += '			</div>\
									</div>\
								</div>\
							';			
								
				}	//END ORDER TYPE CD
							html += 	'<!-- ### //배송 요청사항 ### -->\
										    <input type="hidden" name="omsDeliveryaddresss['+num+'].name1" id="dName1" value=""/>\
											<input type="hidden" name="omsDeliveryaddresss['+num+'].countryNo" id="dCountryNo" value=""/>\
											<input type="hidden" name="omsDeliveryaddresss['+num+'].phone1" id="dPhone1" value=""/>\
											<input type="hidden" name="omsDeliveryaddresss['+num+'].phone2" id="dPhone2" value=""/>\
											<input type="hidden" name="omsDeliveryaddresss['+num+'].zipCd" id="dZipCd" value=""/>\
											<input type="hidden" name="omsDeliveryaddresss['+num+'].address1" id="dAddress1" value=""/>\
											<input type="hidden" name="omsDeliveryaddresss['+num+'].address2" id="dAddress2" value=""/>\
											<input type="hidden" name="omsDeliveryaddresss['+num+'].email" id="dEmail" value=""/>\
											<input type="hidden" name="omsDeliveryaddresss['+num+'].totalorderDeliveryFee" id="totalorderDeliveryFee" value="0"/>\
											<input type="hidden" name="omsDeliveryaddresss['+num+'].totalorderWrapFee" id="totalorderWrapFee" value="0"/>\
											<input type="hidden" name="omsDeliveryaddresss['+num+'].note" id="dNote" value=""/>\
											';
											
		
				//상품정보.
				html += productTemplate(num);
				
				
				html += '\
					</form>\
					';
			
	return html;									
}

</script>
	
	<jsp:include page="/WEB-INF/views/oms/order/inner/orderScriptInner.jsp" flush="false" />

	<jsp:include page="/WEB-INF/views/oms/order/inner/orderFormInner.jsp" flush="false" />

	<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
		<jsp:param value="주문/결제" name="pageNavi"/>
	</jsp:include>
	<div class="inner">
		<div class="orderBox">
			<div class="step">
				<h3 class="title_type1">
					주문/결제
				</h3>
				<ol>
					<li><span class="step_01">01</span>장바구니</li>
					<li class="active"><span class="step_02">02</span>주문/결제</li>
					<li><span class="step_03">03</span>주문완료</li>
				</ol>
			</div>		


<form id="orderForm">
<c:choose>
<c:when test="${memberYn == 'N' }">
<div class="order_form">	
	<div class="relBox">
		<h3 class="sub_tit1">주문자 정보</h3>
		<div class="rw_tbBox">
			<ul class="rw_tb_tbody2">
				<li>
					<div class="tr_box">
						<div class="col1">
							<span class="group_inline"><span class="ico_requisite">필수요소</span> 이름</span>
						</div>
						<div class="col2">
							<div class="group_block">
								<input type="text" id="name1" value="" class="input_style2" maxlength="10">
							</div>
						</div>
					</div>
				</li>
				<li>
					<div class="tr_box">
						<div class="col1">
							<span class="group_inline"><span class="ico_requisite">필수요소</span> 휴대전화</span>
						</div>						
						<div class="col2">
							<div class="group_block">
								<div class="inputMix_style2">
									<input type="hidden" id="orderPhone2" value=""/>
									<div class="select_box1">
								 		<label id="orderPhone2_areaCode">010</label>
										<tags:codeList code="MOBILE_NUMBER_CD" optionHead="선택" />
									</div>
									<span class="hyphen">-</span>
									<div class="inputTxt_place1">
										<label></label>
										<span>
											<input type="text" value="" id="orderPhone2_num1" maxlength="4" onblur="ccs.common.fn_press_han(this);"  onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');">
										</span>
									</div>
									<span class="hyphen">-</span>
									<div class="inputTxt_place1">
										<label></label>
										<span>
											<input type="text" value="" id="orderPhone2_num2" maxlength="4" onblur="ccs.common.fn_press_han(this);"  onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');">
										</span>
									</div>
								</div>
							</div>
						</div>
					</div>
				</li>
				<li>
					<div class="tr_box">
						<div class="col1">
							<span class="group_inline"><span class="ico_requisite">필수요소</span> 이메일</span>
						</div>
						<div class="col2">
							<div class="group_block">
								<div class="mailBox">
									<input type="hidden" id="orderEmail" value=""/>
									<div class="inputTxt_place1">
										<label class="mo_only">이메일</label>
										<span>
											<input type="text" id="orderEmail_email1" value="" maxlength="100"/>
										</span>
									</div>
									<span class="at">@</span>
									<div class="inputTxt_place1">
										<label></label>
										<span>
											<input type="text" id="orderEmail_email2" value="" maxlength="100"/>
										</span>
									</div>
									<div class="select_box1">
								 		<label id="orderEmail_domain"></label>
										<tags:codeList code="EMAIL_DOMAIN_CD" optionHead="직접입력" />
									</div>
								</div>								
							</div>
						</div>
					</div>
				</li>
				<li>
					<div class="tr_box">
						<div class="col1">
							<span class="group_inline"><span class="ico_requisite">필수요소</span> 비밀번호</span>
						</div>
						<div class="col2">
							<div class="group_block">
								<div class="inp_placeholder"><input type="password" value="" id="nonMemberOrderPwd" class="input_style2" maxlength="8"><label for="inp_ps">4~8자 숫자, 영문</label></div>
							</div>
						</div>
					</div>
				</li>
				<li>
					<div class="tr_box">
						<div class="col1">
							<span class="group_inline"><span class="ico_requisite">필수요소</span> 비밀번호 확인</span>
						</div>
						<div class="col2">
							<div class="group_block">
								<div class="inp_placeholder"><input type="password" value="" id="nonMemberOrderPwdConfirm" class="input_style2" maxlength="8"><label for="inp_ps2">4~8자 숫자, 영문</label></div>
							</div>
						</div>
					</div>
				</li>				
			</ul>
		</div>
	</div>
</div>
</c:when>
<c:otherwise>
	<input type="hidden" id="name1" value="${memberInfo.mmsMember.memberName}" />
	<input type="hidden" id="orderPhone2" value="${memberInfo.mmsMember.phone2 }" />
	<input type="hidden" id="orderPhone2_areaCode" value="" />
	<input type="hidden" id="orderPhone2_num1" value="" />
	<input type="hidden" id="orderPhone2_num2" value="" />
	<input type="hidden" id="orderEmail" value="${memberInfo.mmsMember.email }" />
	<input type="hidden" id="orderEmail_email1" value="" />
	<input type="hidden" id="orderEmail_email2" value="" />
	<input type="hidden" id="orderEmail_domain" value="" />
</c:otherwise>
</c:choose>
</form>

<c:if test="${omsOrder.orderTypeCd == 'ORDER_TYPE_CD.GIFT' }">
<!-- ### 배송지 정보 ### -->
<form id="giftForm">
<div class="order_form">
	<div class="relBox">
		<h3 class="sub_tit1">배송지/주문정보</h3>
		<div class="rw_tbBox">
			<ul class="rw_tb_tbody2">
				<li>
					<div class="tr_box">
						<div class="col1">
							<span class="group_inline">주문고객</span>
						</div>
						<div class="col2">
							<div class="group_block">${memberInfo.mmsMember.memberName }</div>
						</div>
					</div>
				</li>
				<li>
					<div class="tr_box">
						<div class="col1">
							<span class="group_inline">받으실 분</span>
						</div>
						<div class="col2">
							<div class="group_block">
								<input type="text" name="giftName1" id="giftName1" value="" class="input_style2"  maxlength="10">
							</div>
						</div>
					</div>
				</li>
				<li>
					<div class="tr_box">
						<div class="col1">
							<span class="group_inline two_line">선물 받을<br>휴대폰번호</span>
						</div>
						<div class="col2">
							<div class="group_block">
								<input type="hidden" id="giftPhoneNum" value=""/>
								<div class="inputMix_style2">
									<div class="select_box1">
								 		<label id="giftPhoneNum_areaCode">010</label>
										<tags:codeList code="MOBILE_NUMBER_CD" optionHead="선택" />
									</div>
									<span class="hyphen">-</span>
									<div class="inputTxt_place1">
										<label></label>
										<span>
											<input type="text" name="giftPhoneNum_num1" id="giftPhoneNum_num1" value=""  maxlength="4" />
										</span>
									</div>
									<span class="hyphen">-</span>
									<div class="inputTxt_place1">
										<label></label>
										<span>
											<input type="text" name="giftPhoneNum_num2" id="giftPhoneNum_num2" value=""   maxlength="4" />
										</span>
									</div>
								</div>								
								<ul class="bl_list">
									<li>배송 받을 주소 입력이 가능한 url이 문자로 발송됩니다.</li>
									<li>받는 분의 통신 상태에 따라 메시지 발송지 지연되거나 발송되지 않거나, 스팸으로 처리될 수 있습니다.</li>
									<li>7일 이내  배송지 등록이 안될 경우 결제는 자동취소 처리됩니다.</li>
								</ul>
							</div>
						</div>
					</div>
				</li>
				<li>
					<div class="tr_box">
						<div class="col1">
							<span class="group_inline">선물메세지</span>
						</div>
						<div class="col2">
							<div class="group_block giftBox">
								<ul class="gift_list_msg">
									<tags:codeList code="GIFT_IMG_TYPE_CD" var="codeList" tagYn="N"/>
									<c:forEach var="cd" items="${codeList }" varStatus="cstat">									
									<li>
										<div><img id="giftImgTypeCd_${cstat.index}" src="/resources/img/pc/${cd.note }" alt="${cd.name } 이미지"/></div>
										<label class="radio_style1">
											<em>
												<input type="radio" name="giftImgTypeCd" id="giftImgTypeCd" value="${cd.cd }" <c:if test="${cstat.index == 0 }">checked="checked"</c:if> onchange="javascript:giftMsgChg('${cstat.index}')"/>
											</em>
											<span>${cd.name }</span>
										</label>
									</li>
									</c:forEach>									
								</ul>
								<div>
									<tags:codeList code="GIFT_MSG_TYPE_CD" var="codeList2" tagYn="N"/>
									<div class="sel_box select_style1">
										<label></label>
										<select id="giftMsgSelect" onchange="javascript:giftMsgChg()">
											<option value="" select="selected">선택</option>
											<c:forEach var="cd2" items="${codeList2 }" varStatus="cstat">
											<option value="${cd2.cd }" >${cd2.note }</option>
											</c:forEach>
											<option value="NONE">직접입력</option>
										</select>
									</div>
								</div>
								<!-- 160926 수정 -->
								<div class="p_message type1" id="p_message">

									<!-- ### 카드별 class ###
										축하합니다 : type1
										감사합니다 : type2
										행복하세요 : type3
										힘내세요 : type4
									 -->

									<div class="txtarea_box">
										<textarea rows="5" cols="10" id="giftMsg" disabled="disabled" maxlength="2000"></textarea>
									</div>
								</div>								
								<!-- 160926 수정 -->
<!-- 								<div> -->
<!-- 									<div class="sel_box select_style1"> -->
<!-- 										<label for="slc_deliveryMsg"></label> -->
<!-- 										<select id="slc_deliveryMsg"> -->
<!-- 											<option>..</option> -->
<!-- 										</select> -->
<!-- 									</div> -->
<!-- 								</div> -->
<!-- 								<div> -->
<!-- 									<input type="text" id="giftMsg" value="임신을 진심으로 축하합니다. 순산하세요!" class="input_style2" disabled="disabled"> -->
<!-- 								</div> -->
							</div>
						</div>
					</div>
				</li>
			</ul>
		</div>
	</div>
</div>
</form>
<!-- ### //배송지 정보 ### -->
</c:if>
<div id="addressDiv"></div>
<div id="deliveryDiv"></div>

<c:forEach items="${omsOrder.spsPresents }" var="op">
<c:if test="${fn:length(op.spsPresentproducts) > 0 }">
<div class="giftList">
	<dl>
		<dt>
			<div>
				<strong>${op.name }</strong>
<%-- 				<p>${op.minOrderAmt }원 이상 구매한 모든 고객에게 사은품을 드립니다.</p> --%>
<%-- 				<span>${op.startDt } ~ ${op.endDt }</span> --%>
			</div>
		</dt><dd>
			<ul>
				<c:forEach items="${op.spsPresentproducts }" var="ops" varStatus="subIdx">
				<li>
					<tags:prdImgTag productId="${ops.productId }" size="90" alt="${ops.pmsProduct.name }"/>
					<span>${ops.pmsProduct.name }</span>					
					<form name="orderPresentForm" id="orderPresentForm${subIdx.index}">
						<input type="hidden" name="orderpresentIndex" id="orderpresentIndex" value="${subIdx.index }"/>							
						<input type="hidden" name="orderpresentProductId" id="orderpresentProductId" value="${ops.pmsProduct.productId }"/>
						<input type="hidden" name="orderpresentName" id="orderpresentName" value="${ops.pmsProduct.name }"/>
						<input type="hidden" name="orderPresentId" id="orderPresentId" value="${op.presentId }"/>
					</form>				
				</li>
				</c:forEach>							
			</ul>
		</dd>
	</dl>
</div>
</c:if>

</c:forEach>

<div class="overseas" id="overseas" style="display: none;">
	<div class="left">
		<dl>
			<dt>주문 상품 중 해외구매대행 상품이 포함되어 있습니다.</dt>
			<dd>
				<p>해외구매대행 상품의 경우 200$ 이상 구매 시 혹은 해외분유 및 일부 상품 구매 시 관세청 통관업무를 위해 개인통관고유부호를 반드시 입력해주셔야 합니다. 개인통관고유부호 미입력시 결제가 이루어지지 않으며, 잘못된 개인통관부호 입력 시 배송이 지연될 수 있습니다.</p>
				<p>* 개인통관 고유부호: 관세청 통관업무에 주민등록번호 대신 사용하는 제도입니다.</p>
				<a href="#" onclick="javascript:oms.openPrivateUnipass()">관세청 개인통관부호 신청, 조회 바로가기</a>
			</dd>
		</dl>
		<p class="chkAgree">
			<label class="chk_style1">
				<em>
					<input type="checkbox" name="overseaagreeCheck" value="개인통관 고유부호 정보 제공" />
				</em>
				<span>입력하신 개인통관 고유부호 정보는 판매자에게 제공되며, 이에 동의합니다.</span>
			</label>
		</p>
	</div>
	<div class="right">
		<strong>개인통관 고유부호</strong>
		<div class="inp_box"><input type="text" id="personalCustomsCode" value="" maxlength="12"/></div>
	</div>
</div>			

	<div class="orderInfoBox">
		<div class="orderInfoL">
			<div class="order_form sale_info">																	
				<c:choose>
				<c:when test="${memberYn == 'N' }">
	<!-- 						<input type="hidden" id="applyTotalCouponAmt" value="0"/> -->
				<input type="hidden" id="point" value="0"/>
				<input type="hidden" id="deposit" value="0"/>
				<input type="hidden" id="gift" value="0"/>
				
				</c:when>
				<c:otherwise>
				
				<div class="control_box">
					<h3 class="sub_tit1">할인정보</h3>
					<a href="#" class="btn_box_control">자세히</a>
				</div>
				<div class="relBox">							
					<div class="rw_tbBox">
						<ul class="rw_tb_tbody2">
							<li id="orderCouponDiv">
								<div class="tr_box">
									<div class="col1">
										<span class="group_inline">쿠폰</span>
									</div>
									<div class="col2">
										<div class="group_block">
											<div class="inputTxt_style2">
												<label>원</label>
												<input type="text" id="applyTotalCouponAmt" value="0" value="" readonly="readonly"/>
											</div> <a href="#none" class="btn_sStyle4 sGray2" onclick="javascript:couponSelect()"><strong>쿠폰적용</strong></a>
											<p class="txt_type2">( 적용가능 쿠폰: <strong id="applyCouponCntTxt">0</strong> / 보유 쿠폰: <em id="memberCouponTotal">${memberCouponTotal }</em> )</p>											
										</div>
									</div>
								</div>
							</li>
							<style>
								.someinput::-ms-clear,
								.someinput::-ms-reveal {
								  display: none;
								}
							</style>
							<li>
								<div class="tr_box">
									<div class="col1">
										<span class="group_inline">매일 포인트</span>
									</div>
									<div class="col2">
										<div class="group_block">
											<div class="inputTxt_style2">
												<label>P</label>
												<input type="text" name="point" id="point" value="0" class="someinput"	
																										onclick="javscript:dcClick('point')"
																									 	onblur="javascript:dcBlur('point')"																									 	
																										onkeyup="javascript:dcChg('point')"																										
																									 />
											</div>
											<label class="chk_style1">
												<em>
													<input type="checkbox" id="pointCheckb" value="" onclick="javascript:pointAll(this)"/>
												</em>
												<span>모두사용</span>
											</label>
											<input type="hidden" id="totalPointAmt" value="${memberPoint.rmndPint }"/>
											<p class="txt_type2">( 보유 매일포인트: <strong><fmt:formatNumber value="${memberPoint.rmndPint }" pattern="#,###"/>P</strong> /  <em>10P</em> 이상 사용가능)</p>
										</div>
									</div>
								</div>
							</li>
							<li>
								<div class="tr_box">
									<div class="col1">
										<span class="group_inline">예치금</span>
									</div>
									<div class="col2">
										<div class="group_block">
											<div class="inputTxt_style2">
												<label>원</label>
												<input type="text" name="deposit" id="deposit" value="0" class="someinput"																																																						
																										 	onclick="javscript:dcClick('deposit')"
																										 	onblur="javascript:dcBlur('deposit')"
																										 	onkeyup="javascript:dcChg('deposit')"																										 	
																										 />
											</div>
											<label class="chk_style1">
												<em>
													<input type="checkbox"  id="depositCheckb" value="" onclick="javascript:depositAll(this)"/>
												</em>
												<span>모두사용</span>
											</label>
											<input type="hidden" id="balanceAmt" value="${memberDeposit.balanceAmt }"/>
											<p class="txt_type2">( 보유 예치금: <strong><fmt:formatNumber value="${memberDeposit.balanceAmt }" pattern="#,###"/></strong>원 )</p>																																
										</div>
									</div>
								</div>
							</li>
						</ul>
					</div>
				</div>
				</c:otherwise>
				</c:choose>
				
				
				<!-- ### 결제수단 ### -->
				<div class="paymentList"  id="paymentList">
					<div class="control_box">
						<h3 class="sub_tit1">결제수단</h3>
<!-- 						<a href="#" class="btn_box_control">자세히</a> -->
					</div>
					<div class="payment_way">
						<ul class="payment_way_list">
							<li class="patment_sort">
								<label class="radio_style1 option_style1">
									<em>
										<input type="radio" name="paymentMethodCd" value="PAYMENT_METHOD_CD.CARD" onclick="javascript:paymentChg(this)" checked="checked" />
									</em>
									<span>신용카드</span>
								</label>
								<label class="radio_style1 option_style1" id="virtualDiv">
									<em>
										<input type="radio" name="paymentMethodCd" value="PAYMENT_METHOD_CD.VIRTUAL"  onclick="javascript:paymentChg(this)"/>
									</em>
									<span>무통장입금</span>
								</label>
								<label class="radio_style1 option_style1" id="transferDiv">
									<em>
										<input type="radio" name="paymentMethodCd" value="PAYMENT_METHOD_CD.TRANSFER"  onclick="javascript:paymentChg(this)"/>
									</em>
									<span>실시간 계좌이체</span>
								</label>
								<label class="radio_style1 option_style1" id="mobileDiv">
									<em>
										<input type="radio" name="paymentMethodCd" value="PAYMENT_METHOD_CD.MOBILE"  onclick="javascript:paymentChg(this)"/>
									</em>
									<span>휴대폰결제</span>
								</label>
								<label class="radio_style1 option_style1 kakaopay" id="kakaoDiv">
									<em>
										<input type="radio" name="paymentMethodCd" value="PAYMENT_METHOD_CD.KAKAO"  onclick="javascript:paymentChg(this)"/>
									</em>
									<span>kakaopay</span>
								</label>
							</li>
							<!-- 카드 -->
							<form id="pgCardForm">								
							<li>
								<div class="select_box1">
									<label>카드종류</label>
									<select id="pg_LGD_CARDTYPE" onchange="javascript:chgCardType()">
										<option value="XX" selected="selected">카드종류</option>
										<c:forEach var="card" varStatus="idx" items="${func:getCodeList('PAYMENT_BUSINESS_CD')}">
											<option value="${fn:replace(card.cd,'PAYMENT_BUSINESS_CD.','')}">${card.name}</option>
										</c:forEach>
									</select>
								</div>
								<div class="select_box1">
									<label>할부개월</label>
									<select id="pg_LGD_INSTALL">
									</select>
								</div>
							</li>
							<li>
<!-- 								<div class="select_box1"> -->
<!-- 									<label>간편결제사용여부</label> -->
<!-- 									<select id="pg_LGD_SP_CHAIN_CODE"> -->
<!-- 										<option value=""  selected="selected">간편결제사용여부</option> -->
<!-- 										<option value="0">사용안함</option> -->
<!-- 										<option value="1">사용함</option> -->
<!-- 										<option value="3">국민카드 앱카드바로사용</option> -->
<!-- 										<option value="4">국민카드 앱카드선택사용</option> -->
<!-- 									</select>										 -->
<!-- 								</div> -->
								<div class="select_box1" id="pointUseYn" style="display: none;">
									<label>포인트사용여부</label>
									<select id="pg_LGD_POINTUSE">
										<option value="">포인트사용여부</option>
										<option value="N"  selected="selected">사용안함</option>
										<option value="Y">사용함</option>
									</select>								
								</div>
							</li>								
							</form>
							<form id="pgVirtualForm" style="display: none;">
							<!-- 무통장입금 -->
							<li>
								<div class="select_box1">
									<label>입금은행</label>
									<select id="pg_LGD_BANKCODE">
										<option value=""  selected="selected">입금은행</option>
										<c:forEach var="vbank" varStatus="idx" items="${func:getCodeList('VIRTUAL_BANK_CD')}">
											<option value="${fn:replace(vbank.cd,'VIRTUAL_BANK_CD.','')}">${vbank.name}</option>
										</c:forEach>										
									</select>								
								</div>
								<div class="select_box1">
									<label>에스크로사용여부</label>
									<select id="pg_LGD_ESCROW_USEYN">
										<option value=""  selected="selected">에스크로사용여부</option>
										<option value="N">미사용</option>
										<option value="Y">사용</option>															
									</select>								
								</div>
							</li>															
							<li>
								<div class="select_box1">
									<label>현금영수증발행여부</label>
									<select id="pg_LGD_CASHRECEIPTUSE" onchange="javascript:receiptUseChg($(this))">
										<option value=""  selected="selected">현금영수증발행여부</option>
										<option value="0">현금영수증 미발행</option>
										<option value="1">소득공제(개인)</option>
										<option value="2">지출증빙(사업자 등록번호)</option>
									</select>										
								</div>
								<div class="select_box1" id="receiptUseInfoSel" style="display: none;">
									<label>휴대전화번호</label>
									<select id="pg_LGD_CASHRECEIPTUSE_SEL" onchange="javascript:receiptUseChgSel($(this))">										
										<option value="0" selected="selected">휴대전화번호</option>
										<option value="1">신용카드번호</option>
										<option value="2">현금영수증 카드</option>
									</select>										
								</div>		
								<div class="inputMix_style3" id="receiptUseInfo" style="margin-top:10px; display: none;">
									<input type="text" id="pg_LGD_CASHCARDNUM" value="" style="display: none;"/>
									<div class="inputTxt_place1" id="cartnum1"> 
										<label></label>
										<span>			
											<input type="text" id="pg_LGD_CASHCARDNUM1" value="" onblur="ccs.common.fn_press_han(this);"  onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');" maxlength="4"/>
										</span>						
									</div>
									<div class="inputTxt_place1" id="cartnum2"> 
										<label></label>
										<span>
											<input type="text" id="pg_LGD_CASHCARDNUM2" value="" onblur="ccs.common.fn_press_han(this);"  onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');" maxlength="4"/>
										</span>						
									</div>
									<div class="inputTxt_place1" id="cartnum3"> 
										<label></label>
										<span>
											<input type="text" id="pg_LGD_CASHCARDNUM3" value="" onblur="ccs.common.fn_press_han(this);"  onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');" maxlength="4"/>
										</span>						
									</div>
									<div class="inputTxt_place1" id="cartnum4" style="display: none;"> 
										<label></label>
										<span>
											<input type="text" id="pg_LGD_CASHCARDNUM4" value="" onblur="ccs.common.fn_press_han(this);" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');"  maxlength="6"/>
										</span>						
									</div>
								</div>								
							</li>
							</form>
							<form id="pgTransferForm" style="display: none;">
							<li>
								<div class="select_box1">
									<label>출금은행</label>
									<select id="pg_LGD_USABLEBANK">
										<option value=""  selected="selected">입금은행</option>
										<c:forEach var="bank" varStatus="idx" items="${func:getCodeList('BANK_CD')}">											
											<option value="${bank.note}">${bank.name}</option>
										</c:forEach>
<!-- 										<option value=""  selected="selected">입금은행</option> -->
<!-- 										<option value="02">산업</option>													 -->
<!-- 										<option value="03">기업</option>												 -->
<!-- 										<option value="06">국민</option>												 -->
<!-- 										<option value="07">수협</option>												 -->
<!-- 										<option value="11">농협</option>	 -->
<!-- 										<option value="20">우리</option>												 -->
<!-- 										<option value="23">SC제일</option> -->
<!-- 										<option value="27">씨티</option> -->
<!-- 										<option value="31">대구</option> -->
<!-- 										<option value="32">부산</option> -->
<!-- 										<option value="34">광주</option> -->
<!-- 										<option value="35">제주</option> -->
<!-- 										<option value="37">전북</option> -->
<!-- 										<option value="39">경남</option> -->
<!-- 										<option value="45">새마을금고</option> -->
<!-- 										<option value="48">신협</option> -->
<!-- 										<option value="71">우체국</option> -->
<!-- 										<option value="81">KEB하나</option>												 -->
<!-- 										<option value="88">신한</option> -->
									</select>								
								</div>
								<div class="select_box1">
									<label>에스크로사용여부</label>
									<select id="pg_LGD_ESCROW_USEYN">
										<option value=""  selected="selected">에스크로사용여부</option>
										<option value="N">미사용</option>
										<option value="Y">사용</option>															
									</select>								
								</div>
							</li>
							<li>
								<div class="select_box1">
									<label>현금영수증발행여부</label>
									<select id="pg_LGD_CASHRECEIPTYN">
										<option value=""  selected="selected">현금영수증발행여부</option>
										<option value="N">미발행</option>
										<option value="Y">발행</option>															
									</select>
								</div>
							</li>								
							</form>
						</ul>
						<p class="pay_txt_info">
							<label class="chk_style1">
								<em>
									<input type="checkbox" id="continuePaymentMethodChk" value="" />
								</em>
								<span>지금 선택하신 결제수단을 다음에도 사용</span>
							</label>
						</p>
						<c:if test="${channelId == '0007' }">
						<!-- 16.10.27 : OK캐쉬백 -->
						<dl class="okcashbag">
							<dt><strong>카드번호입력</strong> OK캐쉬백 적립을 위해 고객님의 캐쉬백포인트 카드번호를 입력해 주시기 바랍니다.</dt>
							<dd>
								<div class="cashbag_guide">
									<p><strong>OK캐쉬백 적립을 위해 고객님의 카드번호, 성명, 결제금액, 주문번호 정보가 SK플래닛으로 제공되며, 적립 후 고객문의 발생시 응대를 위해 2년 보관 후 자동 폐기됩니다. 이에 동의하십니까?</strong></p>
									<p>＊고객님께서는 동의거부 권리가 있으며, 동의하지 않을 경우 포인트 적립이 불가합니다.</p>
									<div class="agree_cashbag">
										<label class="radio_style1">
											<em>
												<input type="radio" name="okcashbagAgree" value="1">
											</em>
											<span>동의함</span>
										</label>
										<label class="radio_style1">
											<em>
												<input type="radio" name="okcashbagAgree" value="0"  checked="checked">
											</em>
											<span>동의하지 않음</span>
										</label>
									</div>
								</div>
								<div class="cashbag_info">
									<dl>
										<dt>카드번호입력</dt>
										<dd>
											<input type="text" id="okcashbagNo1" value="" class="inputTxt_style1" maxlength="4" onblur="ccs.common.fn_press_han(this);"  onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');"/>
											<input type="text" id="okcashbagNo2" value="" class="inputTxt_style1" maxlength="4" onblur="ccs.common.fn_press_han(this);"  onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');"/>
											<input type="text" id="okcashbagNo3" value="" class="inputTxt_style1" maxlength="4" onblur="ccs.common.fn_press_han(this);"  onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');"/>
											<input type="text" id="okcashbagNo4" value="" class="inputTxt_style1" maxlength="4" onblur="ccs.common.fn_press_han(this);"  onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');"/>
										</dd>
									</dl>
									<ul>
										<li>카드번호를 입력하지 않으시거나 잘못된 번호를 입력하신 경우, OK캐쉬백 포인트 적립이 불가하오니 주의하여 입력해 주시기 바랍니다.</li>
										<li>포인트 적립은 배송완료 후 익월 말에 적립됩니다.</li>
									</ul>
								</div>
							</dd>
						</dl>
						<!-- //16.10.27 : OK캐쉬백 -->
						</c:if>
						<div id="cardInfo" class="maxW">
							<c:forEach items="${cardPromotionList }" var="cp">
							${cp.html1 }														
							</c:forEach>									
						</div>
					</div><!-- payment_way -->
				</div><!-- paymentList -->
				<!-- ### //결제수단 ### -->
				<!-- ### 결제수단 ### -->
			</div>
		</div>			
			
			
		<!-- ### orderInfoR ### -->
		<div class="orderInfoR">
			<c:if test="${memberYn == 'N' }">	
				<div class="personalBox">
					<dl>
						<dt>
							<label class="chk_style1">
								<em>
									<input type="checkbox" name="agreeCheck" value="비회원 구매 개인정보취급방침" />
								</em>
								<span>비회원 구매 개인정보취급방침</span>
							</label>
						</dt>
						<dd class="personalH">
							<div>
								<strong>수집하는 개인정보 항목</strong>
								<ul>
									<li>- 성명, 주문비밀번호, 전화번호, 휴대폰
								번호, e-mail, 주소</li>
									<li>결제수단 선택에 따라 신용카드 번호,
								은행계좌번호, 환불계좌번호</li>
								</ul>

								<strong>수집이용목적</strong>
								<ul>
									<li>- 성명, 주문비밀번호, 전화번호,휴대폰번호, e-mail: 고지의 전달, 불만처리, 주문/배송정보 안내 등 원활한 의사소통 경로의 확보</li>
									<li>- 주소: 고지의 전달, 청구서, 정확한 상품 배송지의 확보</li>
									<li>- 신용카드번호, 은행계좌번호, 환불계좌번호: 상품 구매에 대한 결제 및 환불 시 계좌 입금처 확보</li>
								</ul>
								<strong>수집하는 개인정보의 보유기간</strong>
								<ul>
									<li>- 계약 또는 청약철회 등에 관한 기록: 5년 (전자상거래 등에서의 소비자보호에 관한 법률)</li>
									<li>- 소비자의 불만 또는 분쟁처리에 관한 기록: 3년 (전자상거래 등에서의 소비자보호에 관한 법률)</li>
									<li>- 대금결제 및 재화 등의 공급에 관한 기록: 5년 (전자상거래 등에서의 소비자보호에 관한 법률)</li>
								</ul>
							</div>
						</dd>
					</dl>					
				</div>
				</c:if>
				<div class="payDetail">
					<ul class="payDetail_01">
						<li>
							<div class="left">상품금액</div>
							<div class="right"><em class="price" id="orgTotalSalePriceTxt">0</em>원</div>
						</li>
						<li>
							<div class="left">배송비</div>
							<div class="right" id="totalDeliveryFeeTxt">0원</div>
						</li>
						<li>
							<div class="left">선물포장비</div>
							<div class="right" id="totalWrapFeeTxt">0원</div>
						</li>
						<li class="sale">
							<div class="left">총 할인금액</div>
							<div class="right" id="totalDcAmtTxt">0원</div>
						</li>
					</ul>
					<c:if test="${memberYn == 'Y' }">
					<ul class="payDetail_02">
<!-- 						<li> -->
<!-- 							<div class="left">딜 할인</div> -->
<!-- 							<div class="right" id="dealAmtTxt">0원</div> -->
<!-- 						</li> -->
						<li>
							<div class="left">상품할인쿠폰</div>
							<div class="right" id="productCouponAmtTxt">0원</div>
						</li>
						<li>
							<div class="left">주문할인쿠폰</div>
							<div class="right" id="orderCouponAmtTxt">0원</div>
						</li>
						<li>
							<div class="left">배송비무료쿠폰</div>
							<div class="right" id="deliveryCouponAmtTxt">0원</div>
						</li>
						<li>
							<div class="left">선물포장무료쿠폰</div>
							<div class="right" id="wrapCouponAmtTxt">0원</div>
						</li>
						<li>
							<div class="left">매일 포인트</div>
							<div class="right" id="pointAmtTxt">0P</div>
						</li>
						<li>
							<div class="left">예치금</div>
							<div class="right" id="depositAmtTxt">0원</div>
						</li>
<!-- 							<li> -->
<!-- 								<div class="left">모바일상품권</div> -->
<!-- 								<div class="right" id="giftAmtTxt">0원</div> -->
<!-- 							</li> -->
					</ul>
					</c:if>
					<dl class="payTotal">
						<dt>총 결제예정금액</dt>
						<dd><em id="totalOrderAmtTxt">0</em>원</dd>
						<dd class="point" id="totalPointsaveTxt">적립예정 매일포인트: 0P</dd>
					</dl>
				</div>
				<div class="personalBox2">
					<dl>
						<dt class="control_box">
							<label class="chk_style1">
								<em>
									<input type="checkbox" name="agreeCheck" value="결제대행서비스" />
								</em>
								<span>결제대행서비스 표준이용약관 동의</span>
							</label>
							<a href="#" class="btn_box_control">자세히</a>
						</dt>
						<dd>
							<p class="total_agree">본인은 위의 내용을 모두 읽어보았으며 이에 전체 동의합니다. </p>
							<ul class="agree_list">
								<li>전자금융거래 이용약관 <a href="#none" class="btn_sStyle3" onclick="javascript:clauseInfo('5')">자세히 보기</a></li>
								<li>고유식별정보 수집 및 동의 <a href="#none" class="btn_sStyle3" onclick="javascript:clauseInfo('3')">자세히 보기</a></li>
								<li>개인정보수집 및 이용동의 <a href="#none" class="btn_sStyle3" onclick="javascript:clauseInfo('1')">자세히 보기</a></li>
								<li>개인정보수집 및 위탁동의 <a href="#none" class="btn_sStyle3" onclick="javascript:clauseInfo('2')">자세히 보기</a></li>
								<li>통신과금 서비스 이용약관 <a href="#none" class="btn_sStyle3" onclick="javascript:clauseInfo('6')">자세히 보기</a></li>
							</ul>
						</dd>
						<dt class="control_box">
							<label class="chk_style1">
								<em>
									<input type="checkbox" name="agreeCheck" value="주문 내역" />
								</em>
								<span>주문 내역 동의</span>
							</label>
							<a href="#" class="btn_box_control">자세히</a>
						</dt>
						<dd class="total_agree">
							<p>주문할 상품의 상품명, 상품가격, 배송정보를 확인하였습니다. (전자상거래법 제 8조 2항)</p>
							<strong>구매에 동의하시겠습니까?</strong>
						</dd>
					</dl>
				</div>
				<div class="btn_wrapC">
					<a href="#none" id="payBtn" class="btn_mStyle1 sPurple1" onclick="javascript:pay()"><span>결제하기</span></a>
				</div>
			</div>
			<!-- ### orderInfoR ### -->
						
		</div>
		<!-- orderInfoBox -->
	</div>
	<!-- orderBox -->
</div>
<!-- inner -->

<jsp:include page="/WEB-INF/views/oms/order/inner/orderLayerInner.jsp" flush="false"/>

<jsp:include page="/WEB-INF/views/oms/order/inner/orderCouponInner.jsp" flush="false"/>

<jsp:include page="/WEB-INF/views/oms/order/inner/orderPaymentInner.jsp" flush="false"/>
