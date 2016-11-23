<%--
	화면명 : 검색  OrderBy
	작성자 : emily
 --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<c:choose>
<c:when test="${isMobile}">
	<div class="sortBoxList sort_1ea">
</c:when>
<c:otherwise>
	<div class="sortBoxList sort_2ea">
</c:otherwise>
</c:choose>
	<ul>
		<li>
			<div class ="select_box1">
				<label></label>
				<select id="cornerSortSelect">
					<option value="POPULAR">인기상품순</option>
					<option value="REVIEW">상품평순</option>
					<option value="LOWPRICE">낮은가격순</option>
					<option value="HIGHPRICE">높은가격순</option>
					<option value="LATEST">최근등록순</option>
				</select>
			</div>
			<div class="select_box1 viewSet" >
				<label></label>
				<select id="cornerPageSelect" >
					<option value="40">40개씩</option>
					<option value="80">80개씩</option>
					<option value="120">120개씩</option>								
				</select>
			</div>
		</li>
		<li>
			<button type="button" class="btnListType list">블록형 / 리스트형</button>
		</li>
	</ul>
</div>		
	

