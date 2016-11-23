<%--
	화면명 : 정기배송관 상품 리스트
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<!-- mo 정렬버튼 -->




<div class="tit_style3 mo_only">
	<div class="sortBoxList sort_1ea">
		<ul>
			<li>
				<div class="select_box1">
					
					<tags:codeList code="PRODUCT_SORT_CD" var="productSort" tagYn="N"/>
	
					<c:if test="${displaySearch.sort ne null && displaySearch.sort ne ''}">
						<c:forEach items="${productSort}" var="sort" varStatus="i">
							<c:if test="${sort.cd eq displaySearch.sort}">
								<label>${sort.name}</label>
							</c:if>
						</c:forEach>
					</c:if>
					<c:if test="${displaySearch.sort eq null || displaySearch.sort eq ''}">
						<label>${productSort[0].name}</label>
					</c:if>
				
					<select onchange="javascript:special.subscription.changeSort(this);">
						<c:if test="${displaySearch.sort ne null && displaySearch.sort ne ''}">
							<c:forEach items="${productSort}" var="sort" varStatus="i">
								<c:choose>
									<c:when test="${sort.cd eq displaySearch.sort}">
										<option value="${sort.cd}" selected="selected">${sort.name}</option>
									</c:when>
									<c:otherwise>
										<option value="${sort.cd}">${sort.name}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</c:if>
						<c:if test="${displaySearch.sort eq null || displaySearch.sort eq ''}">
							<c:forEach items="${productSort}" var="sort" varStatus="i">
								<c:choose>
									<c:when test="${i.first}">
										<option value="${sort.cd}" selected="selected">${sort.name}</option>
									</c:when>
									<c:otherwise>
										<option value="${sort.cd}">${sort.name}</option>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</c:if>
					</select>
				</div>
			</li>
			<li>
				<button type="button" class="btnListType">블록형 / 리스트형</button>
			</li>
		</ul>
	</div>
</div>
<!-- //mo 정렬버튼 -->
<div class="list_group">
	<c:if test="${not empty categoryList}">
			<c:forEach var="category" items="${categoryList}" varStatus="i">
				<h4 id="${category.displayCategoryId}" class="tit_style1">
					${category.categoryName}
<!-- 					<a href="#" class="btn_top">top</a> -->
				</h4>
				<div class="product_type1 prodType_4ea block border_none">
					<ul>
						<c:forEach var="categoryProduct" items="${category.dmsDisplaycategoryproducts}" varStatus="">
							<c:set var="product" value="${categoryProduct.pmsProduct}" scope="request" />
							<c:if test="${product.regularDeliveryYn eq 'Y'}">
								<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false" />
							</c:if>
						</c:forEach>
					</ul>
				</div>
			</c:forEach>
	</c:if>
	<c:if test="${not empty cornerPrdList}">
		<div class="product_type1 prodType_4ea block border_none">
			<ul>
				<c:forEach var="corner" items="${cornerPrdList}" varStatus="">
					<c:forEach var="item" items="${corner.dmsDisplayitems}" varStatus="">
						<c:set var="product" value="${item.pmsProduct}" scope="request" />
						<c:if test="${product.regularDeliveryYn eq 'Y'}">
							<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false" >
								<jsp:param name="type" value="subscription" />
							</jsp:include>
						</c:if>
					</c:forEach>
				</c:forEach>
			</ul>
		</div>
	</c:if>
	<!-- 정기배송 상품의 중카테고리 -->
	<c:if test="${displaySearch.displayType eq 'category' }">
<%-- 		<h4 class="tit_style1" id="${displaySearch.displayCategoryId}"> --%>
<%-- 			${displaySearch.name} --%>
<!-- 		<a href="#" class="btn_top">top</a> -->
		</h4>
		<c:set value="${displaySearch.displayCategoryId}" var="categoryId" scope="request" />						
		<c:set value="${search}" var="search" scope="request" />						
		<jsp:include page="/WEB-INF/views/dms/include/searchProductList.jsp" flush="false">
			<jsp:param name="type" value="subscription" />
		</jsp:include>
		
	</c:if>
	<c:if test="${displaySearch.displayType eq 'all' }">
		<c:set value="${search}" var="search" scope="request" />						
		<jsp:include page="/WEB-INF/views/dms/include/searchProductList.jsp" flush="false">
			<jsp:param name="type" value="subscription" />
		</jsp:include>
	</c:if>
	<c:if test="${empty categoryList && empty cornerPrdList && empty searchApi}">
		<p class="empty">상품 목록이 없습니다.</p>
	</c:if>
</div>

<div class="paginateType1">
	<page:paging formId="subscriptionForm" currentPage="${displaySearch.currentPage}" pageSize="${displaySearch.pageSize}" 
			total="${totalCount}" url="/dms/special/subscription/list/ajax" type="ajax" callback="listCallback"/>
</div>
