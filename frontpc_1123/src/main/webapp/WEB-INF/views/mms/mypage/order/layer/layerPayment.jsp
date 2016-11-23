<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="func" uri="kr.co.intune.commerce.common.functions" %>
<link href="https://pg.cnspay.co.kr:443/dlp/css/kakaopayDlp.css" rel="stylesheet" type="text/css" />

<div class="pop_wrap ly_addPayment" style="display: none;" id="addPayment">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit" id="claim_pay_title">${omsPaymemtSearch.title}</h3>
		</div>
		<div class="pop_content">
			<div class="orderBox">
				<!-- ### 결제 수단 ### -->
				<div class="form_style1">
					<h3 class="sub_tit1">결제수단</h3>
					<div class="pay_how">
						<ul class="payment">
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
						</ul>
						<div class="pay_con on">
							<div class="how">
								<ul class="type1">
									<%-- 신용카드 --%>
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
									</li>
									<%-- 계좌이체 --%>
									<li>
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
				</div>
				<!-- ### //결제 수단 ### -->
			</div>
			<div class="btn_wrapC btn2ea">
				<a href="javascript:void(0);" class="btn_mStyle1 sWhite1" onclick="$order.close();">취소</a>
				<a href="javascript:void(0);" class="btn_mStyle1 sPurple1" data-clicked="${false}" onclick="$payment.invoke($(this), '${omsPaymemtSearch.callback}');">확인</a>
	<%-- 			<a href="javascript:void(0);" class="btn_mStyle1 sPurple1" data-clicked="${false}" data-layer="${omsPaymemtSearch.layer}" onclick="$payment.${omsPaymemtSearch.callback}($(this))">확인</a> --%>
			</div>
		</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
	<%-- LG U+ --%>
	<script type="text/javascript" src="/resources/js/common/common.lgu.js"></script>
	<%-- KAKAO --%>
	<script type="text/javascript" src="/resources/js/common/common.kakao.js"></script>
</div>	