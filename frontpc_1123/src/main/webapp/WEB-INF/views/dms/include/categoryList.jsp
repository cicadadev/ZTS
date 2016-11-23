<%--
	화면명 : 프론트 & 모바일  카테고리매장  상단 카테고리 목록
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<input type="hidden" id="displayCategoryId" 		name="displayCategoryId" 		value="${categorySearch.displayCategoryId}">
<input type="hidden" id="upperDisplayCategoryId" 	name="upperDisplayCategoryId" 	value="${categorySearch.upperDisplayCategoryId}">
<input type="hidden" id="categoryViewType" 			name="categoryViewType"			value="${categorySearch.categoryViewType}"	/>	
<input type="hidden" id="categoryName" 				name="categoryName" 			value="${categorySearch.name}"/>

<c:if test="${not empty category}">
	<c:choose>
		<c:when test="${isMobile}">
		<!-- mobile 전용 카테고리 //-->
		<div class="categoryS swiper-container displayCategorySwiper_cetegory">
			<ul class="miniTabBox1 swiper-wrapper">
				<c:forEach var="list" items="${category.dmsDisplaycategorys}" varStatus="status">
					<c:if test="${status.count == 1}">
						<li class="swiper-slide">
					</c:if>
					<c:forEach begin="1" end="${category.pageSize}" step="1" var="pageNo">
						<c:if test="${status.count == (pageNo * 6)+1}">
							<li class="swiper-slide">
						</c:if>
					</c:forEach>
					<div>
						<a href="javascript:ccs.link.display.dispTemplate('${list.displayCategoryId}');"><c:out value="${list.name}"/></a>
					</div>
					
					<c:if test="${status.count %6 == 0 || status.count == category.totalCount}">
						</li>
					</c:if>
				</c:forEach>	
			</ul>
			<c:if test="${category.pageSize gt 1}">
				<!-- Add Pagination -->
        		<div class="swiper-pagination tp3"></div>
			</c:if>
		</div>
		<!--// mobile 전용 카테고리 -->
		</c:when>
		<c:otherwise>
		<!-- pc 전용 카테고리  //-->
			<ul class="miniTabBox1">
				<c:forEach var="list" items="${category.dmsDisplaycategorys}">
					<li>
						<div id="miniTabCtg_${list.displayCategoryId}">
							<a href="javascript:ccs.link.display.dispTemplate('${list.displayCategoryId}');">
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
		</c:otherwise>
	</c:choose>
</c:if>	
