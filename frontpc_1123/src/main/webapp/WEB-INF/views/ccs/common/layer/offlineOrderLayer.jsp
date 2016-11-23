<%--
	화면명 : 오프라인 구매내역 레이어
	작성자 : allen
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript">

</script>
<div class="pop_wrap ly_offline receiptType" id="offlineOrder">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">오프라인 구매내역 상세</h3>
		</div>
			<div class="pop_content">
				<div class="rInfo type01">
					<dl>
						<dt>
							<div>${orderDetail.ccsOffshop.name}</div>
						</dt>
						<dd>
							<ul class="infoList">
								<li>${orderDetail.ccsOffshop.address1}</li>
								<li>TEL : ${orderDetail.ccsOffshop.offshopPhone}</li>
								<li>${orderDetail.orderDt}</li>
								<li>고객센터 : 1588-8744  www.0to7.com</li>
							</ul>
						</dd>
					</dl>
				</div>
				<div class="receipt_tbl">
					<table>
						<colgroup>
							<col style="width: 46%">
							<col style="width: 18%">
							<col style="width: *">
							<col style="width: 18%">
						</colgroup>
						<thead>
							<tr>
								<th scope="col">상품명</th>
								<th scope="col">단가</th>
								<th scope="col">수량</th>
								<th scope="col">금액</th>
							</tr>
						</thead>
						<tbody>
							<c:forEach items="${orderDetail.omsPosorderproducts}" var="product" varStatus="i">
								<tr>
									<td>
										<div class="prdInfo alignL">
											<i>${i.index +1}.</i>
											<p>${product.productName}</p>
										</div>
									</td>
									<td>
										<div>
											<strong><fmt:formatNumber type="currency" value="${product.salePrice}" pattern="###,###" /><i>원</i></strong>
										</div>
									</td>
									<td>
										<div>
											<strong>${product.orderQty}개</strong>
										</div>
									</td>
									<td>
										<div>
											<strong><fmt:formatNumber type="currency" value="${product.orderAmt}" pattern="###,###" /><i>원</i></strong>
										</div>
									</td>
								</tr>
							</c:forEach>
						</tbody>
					</table>
					<ul>
						<li>
							<div>
								<strong>과세합계</strong>
							</div> <span>0</span>
						</li>
						<li>
							<div>
								<strong>부가세</strong>
							</div> <span>0</span>
						</li>
						<li>
							<div>
								<strong>면세합계</strong>
							</div> <span>0</span>
						</li>
					</ul>
					<div class="total">
						<div>
							<strong>판매계</strong>
						</div>
						<span><fmt:formatNumber type="currency" value="${orderDetail.orderAmt}" pattern="###,###" /><i>원</i></span>
						
					</div>
				</div>
				<h4 class="tit_receipt">${loginName}님 매일포인트 내역</h4>
				<div class="receipt_tbl">
					<ul>
						<li>
							<div>
								<strong>멤버십번호</strong>
							</div> <span>${orderDetail.cardIssueNo}</span>
						</li>
						<li>
							<div>
								<strong>적립포인트</strong>
							</div> <span>${orderDetail.savePoint}</span>
						</li>
						<li>
							<div>
								<strong>가용포인트</strong>
							</div> <span>${orderDetail.availPoint}</span>
						</li>
					</ul>
				</div>
				<p class="exp">자세한 포인트 내역은 매일FAMILY 멤버십 참조</p>
				
				<div style="text-align: center;">
					<img alt="" src="/api/ccs/common/barcode/${orderDetail.orderId}" style="width:100%; height:60px"/>
				</div>
				
				<ul class="notice">
					<li>교환/환불은 7일 내 정상 상품에 한해 결제카드와 영수증 지참 후 방문</li>
				</ul>
			</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>