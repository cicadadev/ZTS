<%--
	화면명 : 마이페이지 > MY 정보관리 > 영수증 조회
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<div>
	<input type="hidden" id="total" name="total" value="${totalCount + 0 }">
</div>

		<c:choose>
			<c:when test="${!empty orderList  }">
				<c:forEach var="list" items="${orderList }">
					<c:set var="payListSize" value="${fn:length(list.omsPayments)}" />
						<c:if test="${payListSize == 1}">
							<li>
								<div class="tr_box">
									<div class="col1">
										<span class="date">${list.orderDt }</span>
									</div>
			
									<div class="col2">
										<span><i>주문번호</i>${list.orderId }</span>
									</div>
			
									<div class="col3">
										<div class="title">
											<a href="javascript:void(0);">
												<c:choose>
													<c:when test="${fn:length(list.omsOrderproduct.productName) > 18 }">
														${fn:substring(list.omsOrderproduct.productName,0,17) } ...
													</c:when>
													<c:otherwise>
														${list.omsOrderproduct.productName }
													</c:otherwise>
												</c:choose>
												<c:if test="${list.omsOrderproduct.productCnt > 1 }" >
													외 ${list.omsOrderproduct.productCnt - 1 } 건 
												</c:if>
											</a>
										</div>
									</div>
			
									<div class="col4">
										<span class="price">
											<em><fmt:formatNumber value="${list.omsPayments[0].paymentAmt}" pattern="###,###" /></em>
										</span>
									</div>
			
									<div class="col5">
										<span><tags:codeName code="${list.omsPayments[0].paymentMethodCd }"/></span>
									</div>
			
									<div class="col6">
										<c:if test="${list.omsPayments[0].paymentMethodCd eq 'PAYMENT_METHOD_CD.VIRTUAL' or list.omsPayments[0].paymentMethodCd eq 'PAYMENT_METHOD_CD.TRANSFER'}">
											<c:if test="${!empty list.omsPayments[0].cashReceiptApprovalNo}">
												<a href="javascript:void(0);" class="btn_sStyle1" onclick="javascript:oms.printCashReceipt('${list.omsOrderproduct.orderId}','${list.omsPayments[0].pgApprovalNo }')">
													현금영수증
												</a>
											</c:if>
										</c:if>
										<c:if test="${list.omsPayments[0].paymentMethodCd eq 'PAYMENT_METHOD_CD.CARD' or list.omsPayments[0].paymentMethodCd eq 'PAYMENT_METHOD_CD.MOBILE'}">
											<a href="javascript:void(0);" class="btn_sStyle1" onclick="javascript:oms.printReceipt('${list.omsOrderproduct.orderId}','${list.omsPayments[0].pgApprovalNo }')">
												영수증
											</a>
										</c:if>
										<c:if test="${list.omsPayments[0].paymentMethodCd eq 'PAYMENT_METHOD_CD.KAKAO'}">
											<a href="javascript:void(0);" class="btn_sStyle1" onclick="javascript:oms.printReceiptKakao('${list.omsPayments[0].pgApprovalNo }')">
												카카오전표
											</a>
										</c:if>
									</div>
								</div>
							</li>
						</c:if>
						
						<c:if test="${payListSize > 1 }">
							<c:forEach var="payList" items="${list.omsPayments }" varStatus="status">
								<li>
									<c:if test="${status.index eq 0 }">
										<div class="tr_box">
											<!-- 셀 병합이 필요한 경우 rowspan > cell > vAlign 필요 -->
											<div class="col1 rowspan">
												<div class="cell">
													<span class="vAlign">
														<span class="date">${list.orderDt }</span>
													</span>
												</div>
											</div>
				
											<div class="col2 rowspan">
												<div class="cell">
													<span class="vAlign">
														<span><i>주문번호</i>${list.orderDt }</span>
													</span>
												</div>
											</div>
											
											<div class="col3 rowspan">
												<div class="cell">
													<span class="vAlign">
														<div class="title">
															<a href="javascript:void(0);">
																<c:choose>
																	<c:when test="${fn:length(list.omsOrderproduct.productName) > 20 }">
																		${fn:substring(list.omsOrderproduct.productName,0,19) } ㆍㆍㆍ  
																	</c:when>
																	<c:otherwise>
																		${list.omsOrderproduct.productName }
																	</c:otherwise>
																</c:choose>
																<c:if test="${list.omsOrderproduct.productCnt > 1}">
																	외 ${list.omsOrderproduct.productCnt - 1 } 건 
																</c:if>
															</a>
														</div>
													</span>
												</div>
											</div>
				
											<div class="col4">
												<span class="price">
													<em><fmt:formatNumber value="${payList.paymentAmt}" pattern="###,###" /></em>
												</span>
											</div>
				
											<div class="col5">
												<span><tags:codeName code="${payList.paymentMethodCd }"/></span>
											</div>
				
											<div class="col6">
												<a href="javascript:void(0);" class="btn_sStyle1"><tags:codeName code="${payList.paymentMethodCd }"/></a>
											</div>
										</div>
									</c:if>
			
									<c:if test="${status.index > 0 }">
										<div class="tr_box">
											<div class="col1">
											</div>
				
											<div class="col2">
											</div>
				
											<div class="col3">
											</div>
				
											<div class="col4">
												<span class="price">
													<em><fmt:formatNumber value="${payList.paymentAmt}" pattern="###,###" /></em>
												</span>
											</div>
				
											<div class="col5">
												<span><tags:codeName code="${payList.paymentMethodCd }"/></span>
											</div>
				
											<div class="col6">
												<a href="javascript:void(0);" class="btn_sStyle1"><tags:codeName code="${payList.paymentMethodCd }"/></a>
											</div>
										</div>
									</c:if>
								</li>
							</c:forEach>
						</c:if>
				</c:forEach>	
			</c:when>
			<c:otherwise>
				<li class="empty">
					<div class="tr_box">
						<div class="col99">
							영수증 내역이 없습니다.
						</div>
					</div>
				</li>
			</c:otherwise>
		</c:choose>
		
		<c:if test="${!isMobile }">
			<div class="pagePkg">
				<div class="paginateType1">
					<page:paging formId="" currentPage="${search.currentPage}" pageSize="${search.pageSize}"
						total="${totalCount}" url="/mms/mypage/receipt/list/ajax" type="ajax" callback="mypage.receipt.receiptCallback"/>
				</div>
			</div>
		</c:if>