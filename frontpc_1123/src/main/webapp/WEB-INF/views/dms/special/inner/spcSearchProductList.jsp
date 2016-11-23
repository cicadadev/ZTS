<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<input type="hidden" id="displayCategoryId" 		name="displayCategoryId" 		value="${categorySearch.displayCategoryId}">
<input type="hidden" id="upperDisplayCategoryId" 	name="upperDisplayCategoryId" 	value="${categorySearch.upperDisplayCategoryId}">
<input type="hidden" id="categoryViewType" 			name="categoryViewType"			value="${categorySearch.categoryViewType}"	/>	
<input type="hidden" id="eventType"					name="eventType" 				value="${categorySearch.linkType}"/>

<c:choose>
	<c:when test="${categorySearch.upperDisplayCategoryId eq 'ALL'}">
			<!-- //상품 리스트  : 검색 상품 목록-->
			<jsp:include page="/WEB-INF/views/dms/include/searchProductList.jsp" flush="false">
				<jsp:param value="Y" name="pagingYn"/>
			</jsp:include>
			<!-- //상품 리스트  : 검색 상품 목록 -->
	</c:when>
	<c:otherwise>
		<c:if test="${not empty category}">
			<!-- pc 전용 카테고리  //-->
			<ul class="miniTabBox1">
				<c:forEach var="list" items="${category.dmsDisplaycategorys}">
					<li>
						<div id="miniTabCtg_${list.displayCategoryId}">
							<a href="#tab_${list.displayCategoryId}">
								<c:out value="${list.name}"/>
							</a>
						</div>
					</li>
				</c:forEach>
			</ul>
			<c:if test="${categorySearch.categoryViewType ne 'MILK' && categorySearch.categoryViewType ne 'BIRTH'}">
				<button type="button" class="btnMore">카테고리 열림/닫힘</button>
			</c:if>
			<!-- //pc 전용 카테고리 -->	
		</c:if>
		<c:forEach var="list" items="${searchPrd}" varStatus="status">
			<c:set var="infoMap"  value="${cornerMap[list.categoryList[0].categoryId]}"/>
			
			<c:choose>
				<c:when test="${not empty infoMap}">
					<!-- 카테고리 명 //-->	
					<h4 id="tab_${list.categoryList[0].categoryId}" class="tit_style1">
						<strong>${list.categoryList[0].categoryName}</strong>
						<a href="#" class="btn_top">top</a>
					</h4>
					<!--// 카테고리 명 -->
					<!-- 브랜드 소개 코너 // -->
					<div class="milk">
						<div class="img">
							<img src="${_IMAGE_DOMAIN_}${infoMap.img1}" alt="" />
						</div>
					</div>
					<!-- //브랜드 소개 코너  -->
				</c:when>
				<c:otherwise>
					<!-- 카테고리 명 //-->	
					<h4 id="tab_${list.categoryList[0].categoryId}" class="tit_style1">
						<strong>${list.categoryList[0].categoryName}</strong>
						<a href="#" class="btn_top">top</a>
					</h4>
					<!--// 카테고리 명 -->
				</c:otherwise>
			</c:choose>
			<!-- //상품 리스트  : 검색 상품 목록-->
			<c:set var="searchApi" value="${list}" scope="request" />
			<jsp:include page="/WEB-INF/views/dms/include/searchProductList.jsp" flush="false">
				<jsp:param value="N" name="pagingYn"/>
			</jsp:include>
			<!-- //상품 리스트  : 검색 상품 목록 -->
		</c:forEach>
	</c:otherwise>
</c:choose>
