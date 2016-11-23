<%--
	화면명 : 리뷰  - 상품평 쓰기 목록
	작성자 : roy
 --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>

		
<c:choose>
	<c:when test="${empty search.isScroll}">
		<c:if test="${isMobile}"> 
			<script type="text/javascript" src="/resources/js/mo.js"></script>
		</c:if>
		<c:if test="${!isMobile}">
			<script type="text/javascript" src="/resources/js/pc.js"></script>
		</c:if>
		<script type="text/javascript" src="/resources/js/common/common.ui.js"></script>

		<script type="text/javascript">
			
			$(document).ready(function(){
				document.getElementById('productReview').innerText = "${totalCount}";
			});
		</script>
		<form id="productSearchForm">
			<input type="hidden" value="${totalCount}"  name="productTotalCount"/>
		</form>
		<div>
			<ul class="div_tb_tbody3">
				<c:choose>
					<c:when test="${!empty list}">
						<c:forEach items="${list}" var="list">
							<li>
								<div class="tr_box">
									<div class="col1">
										<span class="date">${list.orderDt}</span>
										<span>${list.orderId}</span>
									</div>
									
									<div class="col2">
										<div class="positionR">
											<div class="prod_img">
												<a href="javascript:ccs.link.product.detail('${list.productId}','');" >
													<tags:prdImgTag productId="${list.productId}" seq="0" size="90" />
													<!-- <img src="img/mobile/temp/list_img1.jpg" alt="" /> -->
													<%-- <tags:prdImgTag productId="" path="" size="90" className="imgT1" alt="상품상품" style="width:500px"/> --%>
												</a>
											</div>
											
											<a href="javascript:ccs.link.product.detail('${list.productId}','');"  class="title">
												${list.productName}
											</a>
											
											<em class="option_txt">
												<i>${list.saleproductName}</i>
											</em>
										</div>
									</div>
					
									<div class="col3">
										<span><i>작성가능 기간</i>~${list.reviewAbleDt}</span>
									</div>
									<div class="col4">
										<a href="javascript:mypage.review.insertReview({orderId : '${list.orderId }', productId : '${list.productId }', productName : '${list.productName }', saleproductId : '${list.saleproductId }', saleproductName : '${list.saleproductName }'});" class="btn_sStyle3 sGray2" >상품평 쓰기</a>
									</div>
								</div>
							</li>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<li class="noData_tp1">
							작성 가능한 상품평이 없습니다.
						</li>
					</c:otherwise>
				</c:choose>
			</ul>
			<!-- ### PC 페이징 ### -->
			<div class="paginateType1">
				<page:paging formId="productSearchForm" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
						total="${totalCount}" url="/mms/mypage/product/list/ajax" type="ajax" callback="mypage.review.listCallbackProduct"/>
			</div>
			<!-- ### //PC 페이징 ### -->
		
		</div>
	</c:when>
	<c:otherwise>
		<c:forEach items="${list}" var="list">
			<li>
				<div class="tr_box">
					<div class="col1">
						<span class="date">${list.orderDt}</span>
						<span>${list.orderId}</span>
					</div>
					
					<div class="col2">
						<div class="positionR">
							<div class="prod_img">
								<a href="javascript:ccs.link.product.detail('${list.productId}','');" >
									<tags:prdImgTag productId="${list.productId}" seq="0" size="90" />
									<!-- <img src="img/mobile/temp/list_img1.jpg" alt="" /> -->
									<%-- <tags:prdImgTag productId="" path="" size="90" className="imgT1" alt="상품상품" style="width:500px"/> --%>
								</a>
							</div>
							
							<a href="javascript:ccs.link.product.detail('${list.productId}','');"  class="title">
								${list.productName}
							</a>
							
							<em class="option_txt">
								<i>${list.saleproductName}</i>
							</em>
						</div>
					</div>
	
					<div class="col3">
						<span><i>작성가능 기간</i>~${list.reviewAbleDt}</span>
					</div>
					<div class="col4">
						<a href="javascript:mypage.review.insertReview({orderId : '${list.orderId }', productId : '${list.productId }', productName : '${list.productName }', saleproductId : '${list.saleproductId }', saleproductName : '${list.saleproductName }'});" class="btn_sStyle3 sGray2" >상품평 쓰기</a>
					</div>
				</div>
			</li>
		</c:forEach>
	</c:otherwise>
</c:choose>