<%--
	화면명 : 매장픽업관 상품 리스트
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>


<script type="text/javascript">
	$("[name=TOTAL_CNT]").val("${totalCount}");
</script>
<form name="productForm">
	<input type="hidden" name="totalCount" value="${totalCount}"/>
	<input type="hidden" name="currentPage" value="${search.currentPage}"/>
</form>

<c:if test="${not empty categoryList}">
	<ul class="miniTabBox1">
		<c:forEach var="category" items="${categoryList}" varStatus="i">
			<li>
				<div>
					<c:choose>
						<c:when test="${i.first}">
							<a href="#${category.displayCategoryId}" class="on" onclick="javascript:changeGubunTitle(this);">${category.categoryName}</a>
						</c:when>
						<c:otherwise>
							<a href="#${category.displayCategoryId}" onclick="javascript:changeGubunTitle(this);">${category.categoryName}</a>
						</c:otherwise>
					</c:choose>
				</div>
			</li>
		</c:forEach>					
	</ul>
</c:if>

	<div class="list_group">
	
		<c:if test="${not empty categoryList}">
				<c:forEach var="category" items="${categoryList}" varStatus="i">
					<h4 id="${category.displayCategoryId}" class="tit_style1 type1">
						${category.categoryName}
						<a href="#" class="btn_top">top</a>
					</h4>
					<div class="product_type1 prodType_4ea block">
						<ul>
							<c:forEach var="categoryProduct" items="${category.dmsDisplaycategoryproducts}" varStatus="">
								<c:set var="product" value="${categoryProduct.pmsProduct}" scope="request" />
								<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false" />
							</c:forEach>
						</ul>
					</div>
				</c:forEach>
		</c:if>
	
		<c:if test="${not empty cornerPrdList}">
				<div class="product_type1 prodType_4ea block">
					<ul>
						<c:forEach var="pmsProduct" items="${cornerPrdList}" varStatus="">
							<c:set var="product" value="${pmsProduct}" scope="request" />
							<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false" />
						</c:forEach>
					</ul>
				</div>
		</c:if>

		<c:if test="${not empty themeProductList}">
			<div class="product_type1 prodType_4ea block">
				<ul>
					<c:forEach var="pmsProduct" items="${themeProductList}" varStatus="">
						<c:set var="product" value="${pmsProduct}" scope="request" />
						<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false" />
					</c:forEach>
				</ul>
			</div>
		</c:if>
		<c:if test="${empty categoryList && empty cornerPrdList && empty themeProductList}">
			<p class="empty">상품 목록이 없습니다.</p>
		</c:if>
	</div>


<div class="paginateType1">
	<page:paging formId="giftForm" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
			total="${totalCount}" url="/dms/special/giftShop/list/ajax2" type="ajax" callback="special.giftShop.listCallback"/>
</div>