<%--
	화면명 : 
	작성자 : ALLEN
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

	
<c:if test="${not empty exhibitDetail.dmsExhibitgroups}">
	<c:forEach var="group" items="${exhibitDetail.dmsExhibitgroups}" varStatus="i">
		<c:if test="${not empty group.groupNo}">
			<c:if test="${exhibitInfo.exhibitTypeCd ne 'EXHIBIT_TYPE_CD.TIMESALE'}">
					<c:if test="${group.groupTypeCd eq 'GROUP_TYPE_CD.TEXT'}">
						<h4 class="tit_style1 type1">
						<c:if test="${isMobile}">
							<c:choose>
								<c:when test="${group.url2 ne null && group.url2 ne ''}">
									<a id="${group.groupNo}" href="${group.url2}">${group.name}</a>
								</c:when>
								<c:otherwise>
									<a id="${group.groupNo}">${group.name}</a>
								</c:otherwise>
							</c:choose>
						</c:if>
						<c:if test="${!isMobile}">
							<c:choose>
								<c:when test="${group.url1 ne null && group.url1 ne ''}">
									<a id="${group.groupNo}" href="${group.url1}" target="_blank">${group.name}</a>
								</c:when>
								<c:otherwise>
									<a id="${group.groupNo}">${group.name}</a>
								</c:otherwise>
							</c:choose>
						</c:if>
					</c:if>
					<c:if test="${group.groupTypeCd eq 'GROUP_TYPE_CD.IMG'}">
						<h4 class="tit_style1">
						<c:if test="${isMobile}">
							<c:choose>
								<c:when test="${group.url2 ne null && group.url2 ne ''}">
									<a id="${group.groupNo}" href="${group.url2}">
										<img src="${_IMAGE_DOMAIN_}${group.img}" alt=""/>
									</a>
								</c:when>
								<c:otherwise>
									<a id="${group.groupNo}">
										<img src="${_IMAGE_DOMAIN_}${group.img}" alt=""/>
									</a>
								</c:otherwise>
							</c:choose>
						</c:if>
						<c:if test="${!isMobile}">
							<c:choose>
								<c:when test="${group.url1 ne null && group.url1 ne ''}">
									<a id="${group.groupNo}" href="${group.url1}" target="_blank">
										<img src="${_IMAGE_DOMAIN_}${group.img}" alt=""/>
									</a>
								</c:when>
								<c:otherwise>
									<a id="${group.groupNo}">
										<img src="${_IMAGE_DOMAIN_}${group.img}" alt=""/>
									</a>
								</c:otherwise>
							</c:choose>
						</c:if>
					</c:if>
					<a href="#home" class="btn_top">TOP</a>
				</h4>
			</c:if>
			<c:choose>
				<c:when test="${isMobile}">
					<c:set var="productDisplayTypeVal" value="${group.productDisplayType2Cd}" />
				</c:when>
				<c:otherwise>
					<c:set var="productDisplayTypeVal" value="${group.productDisplayType1Cd}" />
				</c:otherwise>
			</c:choose>
		
			<div class="product_type1 prodType_<tags:codeName code="${productDisplayTypeVal}"/>ea block" id="div_${group.groupNo}">
				<ul>
					<c:forEach var="exhibitProduct" items="${group.dmsExhibitproducts}" varStatus="">
						<c:set var="product" value="${exhibitProduct.pmsProduct}" scope="request" />
						<c:if test="${product.saleStateCd eq 'SALE_STATE_CD.SALE'}">
							<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
								<jsp:param name="type" value="exhibit" />
							</jsp:include>
						</c:if>
					</c:forEach>
					<c:if test="${empty group.dmsExhibitproducts}">
						상품이 없습니다.				
					</c:if>
				</ul>
			</div>
		</c:if>
	</c:forEach>
</c:if>
