<%--
	화면명 : 마이페이지 > 나의활동 > 쇼핑찜 list
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<div>
	<input type="hidden" id="total" value="${totalCount + 0 }">
</div>
	
	<ul class="div_tb_tbody3">
		<c:choose>
			<c:when test="${!empty wishList }">
				<c:forEach items="${wishList}" var="item" varStatus="status">
					<li id="list_notempty">
						<div class="tr_box">
							<div class="col1">
								<div class="positionR">
									<div class="prod_img">
										<a href="javascript:void(0);" onclick="javascript:ccs.link.product.detail('${item.productId}')">																				
											<tags:prdImgTag productId="${item.productId}" size="90" alt="" />				
										</a>
									</div>
	
									<div class="flag">
										<c:if test="${item.freeDeliveryYn eq 'Y'}">
											<span class="icon_type1">무료배송</span>
										</c:if>
										<c:if test="${item.couponYn eq 'Y'}">
											<span class="icon_type1">쿠폰</span>
										</c:if>
										<c:if test="${item.pointSaveYn eq 'Y'}">
											<span class="icon_type1">사은품</span>
										</c:if>
									</div>
	
									<a href="javascript:void(0);" class="title" onclick="javascript:ccs.link.product.detail('${item.productId}')">
										${item.name}
									</a>
								</div>
							</div>
	
							<div class="col2">
								<span class="price">
									<em><fmt:formatNumber value="${item.salePrice}" pattern="###,###" /> <i>원</i></em>
								</span>
							</div>
	
							<div class="col3">
								<a href="#none" class="btn_sStyle3 sGray2 btn_del" onclick="mypage.wishlist.deleteItem('${status.index}');">삭제</a>
								<c:if test="${item.realStockQty > 0}">
									<a href="#none" class="btn_sStyle3 sGray2" onclick="mypage.wishlist.openOptionLayer('cart', '${status.index}');">장바구니</a>
									<a href="#none" class="btn_sStyle3 sPurple1" onclick="mypage.wishlist.openOptionLayer('order', '${status.index}');">바로구매</a>
								</c:if>							
							</div>

							<!-- SOLD OUT -->
							<c:if test="${item.realStockQty == 0}">
								<span class="soldoutTxt">
									<em>
										<strong>SOLD OUT</strong>판매가 완료되었습니다
									</em>
								</span>								
							</c:if>
							
						</div>
						<input type="hidden" name="hidProductId${status.index}" id="hidProductId${status.index}" value="${item.productId}" />
						<input type="hidden" name="hidCallbackPart" id="hidCallbackPart" value="" />
					</li>				
				</c:forEach>					
			</c:when>
			<c:otherwise>
				<li class="empty" id="list_empty">
					<div class="tr_box">
						<div class="col99">
							최근 찜한 상품 목록이 없습니다.
						</div>
					</div>
				</li>
			</c:otherwise>
		</c:choose>

	</ul>
	
	<!-- ### //테이블 바디 ### -->
	
	<!-- ### PC 페이징 ### -->
	<div class="pagePkg">
		<div class="paginateType1">
			<page:paging formId="" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
					total="${totalCount}" url="/mms/mypage/wishlist/ajax" type="ajax" callback="mypage.wishlist.wishlistCallback"/>
		</div>
	</div>
	<!-- ### //PC 페이징 ### -->
<!-- <div> -->

<!-- </div> -->