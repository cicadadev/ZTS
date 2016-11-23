<%--
	화면명 : 브랜드관 > 템플릿A > CATALOG & COORDI LOOK 상세 상품 화면
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="catalogImgs" value="${catalogItem.dmsCatalogimgs[0]}" />
<c:if test="${not empty catalogImgs}">
	<input type="hidden" name="productCnt" value="${totalCount}" />
	
	<c:choose>
		<c:when test="${empty catalogImgs.dmsCatalogproducts}">
			<div class="empty">상품이 없습니다.</div>
		</c:when>
		<c:otherwise>
			<ul id="productGallery">
				<c:forEach items="${catalogImgs.dmsCatalogproducts}" var="product" varStatus="prod_status">
					<li>
						<a href="#none" onclick="javascript:sales.productDetail('${product.catalogId}', '${product.pmsProduct.productId}');">
							<div class="img">
								<img src="${_IMAGE_DOMAIN_}${product.pmsProduct.pmsProductimgs[0].img}" alt="${product.pmsProduct.name}"/>
							</div>
							
							<div class="info">
								<div class="etc">
									<span class="price"><fmt:formatNumber type="currency" value="${product.pmsProduct.salePrice}" pattern="###,###" /><i>원</i></span>
								</div>
							</div>
						</a>
					</li>
				</c:forEach>
			</ul>
		</c:otherwise>
	</c:choose>
	
	<form name="pagingForm" id="pagingForm">
		<input type="hidden" name="hid_current" id="hid_current" value="${search.currentPage}" />
		<input type="hidden" name="hid_pageSize" id="hid_pageSize" value="${search.pageSize}" />
	</form>
</c:if>

<style>
.pc .product_type1 ul li {width:239px;}
</style>