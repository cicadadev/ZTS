<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="func" uri="kr.co.intune.commerce.common.functions"%>
<link href="https://pg.cnspay.co.kr:443/dlp/css/kakaopayDlp.css" rel="stylesheet" type="text/css" />

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY주문관리|교환신청" name="pageNavi"/>
</jsp:include>
<div class="inner">
<div class="layout_type1">
<div class="column">
	<div class="myorder claimApply exchange">
		<h3 class="title_type1">교환신청</h3>
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
					data-select-key="exchange"
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
			
			<div class="viewTblList prd">
				<div class="div_tb_thead3">
					<div class="tr_box">
						<span class="col1">상품명</span>
						<span class="col2">교환싱픔 옵션</span>
						<span class="col3">수량</span>
					</div>
				</div>
				<ul class="div_tb_tbody3">
					<li>

<form name="omsClaimWrapper" id="omsClaimWrapper">
<input name="claimTypeCd" value="CLAIM_TYPE_CD.EXCHANGE" type="hidden" style="display: none;">
<input name="claimStateCd" value="CLAIM_STATE_CD.REQ" type="hidden" style="display: none;">

<c:set var="exchangeDeliveryFee" value="0"/>
<c:forEach var="address" varStatus="idx1" items="${order.omsDeliveryaddresss}">
	<c:forEach var="delivery" varStatus="idx2" items="${address.omsDeliverys}">
		<c:set var="exchangeDeliveryFee" value="${delivery.deliveryFee * 2}"/>
	</c:forEach>
</c:forEach>

<c:set var="trIdx" value="0"/>
<c:set var="seq" value="0"/>
<c:forEach var="product" varStatus="idx2" items="${order.omsOrderproducts}">
	<c:if test="${fn:contains('ORDER_PRODUCT_TYPE_CD.GENERAL,ORDER_PRODUCT_TYPE_CD.SET,ORDER_PRODUCT_TYPE_CD.WRAP', product.orderProductTypeCd)}">
	<input name="omsOrderproducts[${seq}].orderId" value="${order.orderId}" type="hidden" readonly="readonly">
	<input name="omsOrderproducts[${seq}].orderProductNo" value="${product.orderProductNo}" type="hidden" readonly="readonly">
	<input name="omsOrderproducts[${seq}].productId" value="${product.productId}" type="hidden" readonly="readonly">
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
			<%-- 옵션목록 조회 --%>
			<div class="col2 optList" style="text-align: right;">
				<c:forEach var="option" items="${optionList}" varStatus="index">
					<div>
						<i>${option.optionName}</i>
						<div class="select_box1 type1">
							<label id="${option.optionName}_label">${selected[option.optionName]}</label>
							<select optionName="${option.optionName}" name="options_${index.count }"
								onchange="oms.changeOptionLayer.selectOption(this.name, ${fn:length(optionList)} ,'${productId }')">
								<option value="">선택하세요</option>
								<c:forEach var="value" items="${option.optionValues }">
									<option ${value.optionValue eq selected[option.optionName] ? 'selected' : '' } 
										${value.realStockQty < 1 ? ' disabled="disabled" style="color: red"' : ''}>${value.optionValue}</option>
								</c:forEach>
							</select>
						</div>
					</div>
				</c:forEach>
			</div>
			
			<div class="col3">
				<i class="motit">수량</i>
				<div class="select_box1 type1">
					<c:if test="${product.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SET'}">
						<input type="hidden" name="omsOrderproducts[${seq}].newSaleProductId" readonly="readonly" value="${product.saleproductId}">
						<input type="hidden" name="omsOrderproducts[${seq}].newSaleProductNm" readonly="readonly" value="${product.saleproductName}">
						
						<input type="hidden" name="omsOrderproducts[${seq}].omsClaimproduct.orderId" value="${order.orderId}" readonly="readonly">
						<input type="hidden" name="omsOrderproducts[${seq}].omsClaimproduct.orderProductNo" value="${product.orderProductNo}" readonly="readonly">
						<input type="hidden" name="omsOrderproducts[${seq}].omsClaimproduct.claimNo" value="" readonly="readonly">
						
						<c:forEach var="children" varStatus="idx3" items="${order.omsOrderproducts}">
							<c:if test="${children.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SUB' && children.upperOrderProductNo == product.orderProductNo}">
							
								<input type="hidden" name="omsOrderproducts[${seq + idx3.index}].orderId" value="${order.orderId}" readonly="readonly">
								<input type="hidden" name="omsOrderproducts[${seq + idx3.index}].orderProductNo" value="${children.orderProductNo}" readonly="readonly">
								<input type="hidden" name="omsOrderproducts[${seq + idx3.index}].productId" value="${children.productId}" readonly="readonly">
								<input type="hidden" name="omsOrderproducts[${seq + idx3.index}].setQty" value="${children.setQty}" readonly="readonly">
							
								<input type="hidden" name="omsOrderproducts[${seq + idx3.index}].newSaleProductId" readonly="readonly" value="${children.saleproductId}">
								<input type="hidden" name="omsOrderproducts[${seq + idx3.index}].newSaleProductNm" readonly="readonly" value="${children.saleproductName}">
								
								<input type="hidden" name="omsOrderproducts[${seq + idx3.index}].omsClaimproduct.orderId" value="${order.orderId}" readonly="readonly">
								<input type="hidden" name="omsOrderproducts[${seq + idx3.index}].omsClaimproduct.orderProductNo" value="${children.orderProductNo}" readonly="readonly">
								<input type="hidden" name="omsOrderproducts[${seq + idx3.index}].omsClaimproduct.claimNo" value="" readonly="readonly">
								
								<%--
								<select name="omsOrderproducts[${seq + idx3.index}].omsClaimproduct.claimQty" style="display: none;"
									data-order-id="${order.orderId}"
									data-order-product-no="${children.orderProductNo}"
									data-set-qty="${children.setQty}"
									data-total-sale-price="${children.totalSalePrice}"
									data-wrap-yn="${children.wrapYn}"
									data-wrap-together-yn="${children.wrapTogetherYn}"
									data-wrap-volume="${children.wrapVolume}">
									<option value="${children.setQty}" selected="selected">${children.setQty}</option>
								</select>
								--%>
							</c:if>
						</c:forEach>
						
						<label></label>
						<select name="omsOrderproducts[${seq}].omsClaimproduct.claimQty" onchange="$claim.setdata.claimqty($(this));" 
							data-order-id="${order.orderId}"
							data-order-product-no="${product.orderProductNo}"
							data-total-sale-price="${product.totalSalePrice}"
							data-wrap-yn="${product.wrapYn}"
							data-wrap-together-yn="${product.wrapTogetherYn}"
							data-wrap-volume="${product.wrapVolume}">
							<c:forEach begin="0" end="${product.outQty - product.returnQty}" step="1" var="count">
								<option value="${count}">${count}</option>
							</c:forEach>
						</select>
					</c:if>
					<c:if test="${product.orderProductTypeCd != 'ORDER_PRODUCT_TYPE_CD.SET'}">
						<input type="hidden" name="omsOrderproducts[${seq}].newSaleProductId" readonly="readonly" value="${product.saleproductId}">
						<input type="hidden" name="omsOrderproducts[${seq}].newSaleProductNm" readonly="readonly" value="${product.saleproductName}">
						
						<input type="hidden" name="omsOrderproducts[${seq}].omsClaimproduct.orderId" value="${order.orderId}" readonly="readonly">
						<input type="hidden" name="omsOrderproducts[${seq}].omsClaimproduct.orderProductNo" value="${product.orderProductNo}" readonly="readonly">
						<input type="hidden" name="omsOrderproducts[${seq}].omsClaimproduct.claimNo" value="" readonly="readonly">
						<label></label>
						<select name="omsOrderproducts[${seq}].omsClaimproduct.claimQty" onchange="$claim.setdata.claimqty($(this));" 
							data-order-id="${order.orderId}"
							data-order-product-no="${product.orderProductNo}"
							data-total-sale-price="${product.totalSalePrice}"
							data-wrap-yn="${product.wrapYn}"
							data-wrap-together-yn="${product.wrapTogetherYn}"
							data-wrap-volume="${product.wrapVolume}">
							<c:forEach begin="0" end="${product.outQty - product.returnQty}" step="1" var="count">
								<option value="${count}">${count}</option>
							</c:forEach>
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
								data-claim-type="exchange"
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
									<span>위 상품과 교환사유가 동일합니다.</span>
								</label>
							</div>
						</c:if>
					</div>
				</div>
			</div>
		</div>
		<c:set var="seq" value="${seq + 1}"/>
	</c:if>
</c:forEach>
</form>

<c:forEach var="address" varStatus="status1" items="${order.omsDeliveryaddresss}">
	<c:forEach var="policy" varStatus="status2" items="${address.omsDeliverys}">
		<div class="tr_box tr_claimmsg" style="display: none;">
			<div class="claim_msg">
				<span><i>*</i>본 상품은 판매업체로 직접 반송처리 해주셔야 합니다.</span>
				<span class="address">
					<i>반품지 주소 :</i>
						<c:if test="${!empty policy.ccsDeliverypolicy.zipCd}">
						<em>(${policy.ccsDeliverypolicy.zipCd}) ${policy.ccsDeliverypolicy.address1} ${policy.ccsDeliverypolicy.address2}</em>
					</c:if>
				</span>
			</div>
		</div>
	</c:forEach>							
</c:forEach>
					</li>
				</ul>
			</div>
			
			<c:if test="${empty order.omsClaims[0].claimNo 
						|| (order.omsClaims[0].claimStateCd != 'CLAIM_STATE_CD.REQ'
							&& order.omsClaims[0].claimStateCd != 'CLAIM_STATE_CD.PAYMENT_READY'
							&& order.omsClaims[0].claimStateCd != 'CLAIM_STATE_CD.ACCEPT')}">
				<div class="btn_check">
					<a href="javascript:void(0);" class="btn_sStyle4 sPurple1" data-fee="${exchangeDeliveryFee}" data-title="교환" onclick="$claim.check.penalty($(this));">추가결제 비용 확인</a>
				</div>
			</c:if>
			
			
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

			<div id="payment_area" style="display: none;">
				<ul class="notice2">
					<li>
						<strong><i>*</i>교환 진행을 위한 배송비가 발생하였습니다. 추가결제를 진행해 주시면 교환이 진행됩니다.</strong>
					</li>
					<li>
						<strong><i>*</i>배송지 변경이나 추가 문의사항은 고객센터 1588-8744로 문의 부탁드립니다.</strong>
					</li>
				</ul>

				<h4 class="sub_tit1">결제수단</h4>
				<div class="ways pay_how">
					<ul class="payment_list">
						<c:forEach var="method" varStatus="idx" items="${func:getCodeList('PAYMENT_METHOD_CD')}">
							<c:if test="${fn:indexOf('1,3,4,5', method.sortNo) > -1}">
								<li>
									<label class="radio_style1 option_style1 ${method.sortNo == 5 ? 'kakaopay' : ''}" style="cursor: pointer;">
										<em><input type="radio" name="paymentMethodCd" value="${method.cd}" onclick="$payment.setmethod($(this), event);"/></em>
										<span>${method.name}</span>
									</label>
								</li>
							</c:if>
						</c:forEach>
						<li>
							<div class="select_box1" data-method="PAYMENT_METHOD_CD.CARD">
								<label>카드종류</label>
								<select name="LGD_CARDTYPE">
									<option value="" selected="selected">카드종류</option>
									<c:forEach var="card" varStatus="idx" items="${func:getCodeList('PAYMENT_BUSINESS_CD')}">
										<option value="${fn:replace(card.cd,'PAYMENT_BUSINESS_CD.','')}">${card.name}</option>
									</c:forEach>
								</select>
							</div>
							<div class="select_box1" data-method="PAYMENT_METHOD_CD.CARD">
								<label>할부개월</label>
								<select name="LGD_INSTALL">
									<option value="0" selected="selected">일시불</option>
									<c:forEach begin="2" end="12" step="1" var="cnt">
										<option value="${cnt}">${cnt}개월</option>
									</c:forEach>
								</select>
							</div>
							<%--
							<div class="select_box1" data-method="PAYMENT_METHOD_CD.CARD">
								<label>간편결제사용여부</label>
								<select name="LGD_SP_CHAIN_CODE">
									<option value="" selected="selected">간편결제사용여부</option>
									<option value="0">사용안함</option>
									<option value="1">사용함</option>
									<option value="3">국민카드 앱카드바로사용</option>
									<option value="4">국민카드 앱카드선택사용</option>
								</select>
							</div>
							<div class="select_box1" data-method="PAYMENT_METHOD_CD.CARD">
								<label>포인트사용여부</label>
								<select name="LGD_POINTUSE">
									<option value="" selected="selected">포인트사용여부</option>
									<option value="N">사용안함</option>
									<option value="Y">사용함</option>
								</select>
							</div>
							--%>
							<%-- 계좌이체 --%>
							<div class="select_box1" data-method="PAYMENT_METHOD_CD.TRANSFER">
								<label>출금은행</label>
								<select name="LGD_USABLEBANK">
									<option value="" selected="selected">입금은행</option>
									<c:forEach var="bank" varStatus="idx" items="${func:getCodeList('BANK_CD')}">
										<option value="${fn:replace(bank.note,'BANK_CD.','')}">${bank.name}</option>
									</c:forEach>
								</select>
							</div>
							<div class="select_box1" data-method="PAYMENT_METHOD_CD.TRANSFER">
								<label>현금영수증발행여부</label>
								<select name="LGD_CASHRECEIPTYN">
									<option value="" selected="selected">현금영수증발행여부</option>
									<option value="N">미발행</option>
									<option value="Y">발행</option>
								</select>
							</div>
						</li>
					</ul>
				</div>
			</div>
		</div>
		<div class="payment_info mtoggleBox on" style="display: none;">
			<h4 class="sub_tit1 toggleBtn">추가결제 정보</h4>
			<div class="box toggleCont">
				<div class="columnL">
					<dl>
						<dt>교환왕복배송비</dt>
						<dd>0<i>원</i></dd>
					</dl>
				</div>
				<div class="columnR">
					<div class="payNpoint">
						<dl class="money">
							<dt>총 결제금액</dt>
							<dd>0<i>원</i></dd>
						</dl>
					</div>
				</div>
			</div>
		</div>
		<c:choose>
			<c:when test="${empty order.omsClaims[0].claimNo 
						|| (order.omsClaims[0].claimStateCd != 'CLAIM_STATE_CD.REQ'
							&& order.omsClaims[0].claimStateCd != 'CLAIM_STATE_CD.PAYMENT_READY'
							&& order.omsClaims[0].claimStateCd != 'CLAIM_STATE_CD.ACCEPT')}">
				<div class="btn_wrapC btn2ea">
					<a href="javascript:void(0);" class="btn_mStyle1 sWhite1" onclick="location.href=document.referrer">취소</a>
					<%--
					<a href="javascript:void(0);" class="btn_mStyle1 sPurple1 change_method" onclick="$payment.invoke($(this), 'insert', true);"
						id="claimBtn" style="display: none;" data-clicked="${false}" data-fee="${exchangeDeliveryFee}" data-confirm="0">교환신청</a>
					--%>
					<a href="javascript:void(0);" class="btn_mStyle1 sPurple1 change_method" onclick="$claim.exchange($(this));" 
						id="claimBtn" style="display: none;" 
						data-claim-type="exchange" 
						data-clicked="${false}" 
						data-fee="${exchangeDeliveryFee}" 
						data-confirm="0">교환신청</a>
				</div>
				<%--
				<ul class="notice">
					<li>문구필요</li>
					<li>문구필요</li>
				</ul>
				--%>
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
		$order.change.optionpage('${order.omsOrderproducts[0].productId}', '${order.omsOrderproducts[0].saleproductId}', '${order.omsOrderproducts[0].orderProductTypeCd}');
		$('.payment_list').find('input[name="paymentMethodCd"]').eq(0).trigger('click');
		
	});
	function selectSetOption($this) {
		var $selOpt = $('option:selected', $this);
		var selectedIdx;
		$('input[name$="].productId"]').each(function(idx, obj) {
			if ($(obj).val() == $selOpt.data('productId')) {
				selectedIdx = idx;
				return false;
			}
		});
		$('[name="omsOrderproducts['+selectedIdx+'].newSaleProductId"]').val($selOpt.val()); 
		$('[name="omsOrderproducts['+selectedIdx+'].newSaleProductNm"]').val($selOpt.text().trim()); 

// 		var subClaimQty = $('[name="omsOrderproducts['+selectedIdx+'].omsClaimproduct.claimQty"]);
// 		$('[name="omsOrderproducts['+selectedIdx+'].omsClaimproduct.claimQty"] option:selected').val();
		
	}
//-->
</script>
<%-- LG U+ --%>
<script type="text/javascript" src="/resources/js/common/common.lgu.js"></script>
<%-- KAKAO --%>
<script type="text/javascript" src="/resources/js/common/common.kakao.js"></script>