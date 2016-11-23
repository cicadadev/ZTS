<%--
	화면명 : 입점 제휴 문의 - 셀렉터
	작성자 : roy
 --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>

<c:if test="${isMobile}"> 
	<script type="text/javascript" src="/resources/js/mo.js"></script>
</c:if>
<c:if test="${!isMobile}">
	<script type="text/javascript" src="/resources/js/pc.js"></script>
</c:if>
<script type="text/javascript" src="/resources/js/common/common.ui.js"></script>


	<div class="group_block">
		<div class="category_wrap">
			<span>선택1</span>
			<div class="select_box1">
				<label>2017</label>
				<select name="categoryId_1" id="alliance_category1_1" onchange="javascript:ccs.common.alliance.changeSelector('1');javascript:ccs.common.alliance.selectorCall();" value="">
					<option value="">선택</option>
					<c:forEach var="dir" items="${category}" varStatus="status">
						<c:if test="${dir.depth eq 2}">
							<option value="${dir.categoryId}"  <c:if test="${search.category1 eq dir.categoryId}">selected</c:if>>${dir.name}</option>
						</c:if>
					</c:forEach>
				</select>
				
			</div>
			<div class="select_box1">
				<label>선택</label>
				<select name="categoryId_2" id="alliance_category1_2" onchange="javascript:ccs.common.alliance.selectorCall();" value="">
					<option value="">선택</option>
					<c:forEach var="dir" items="${category}" varStatus="status">
						<c:if test="${dir.depth eq 3 and dir.upperCategoryId eq search.category1}">
							<option value="${dir.categoryId}"  <c:if test="${search.category2 eq dir.categoryId}">selected</c:if>>${dir.name}</option>
						</c:if>
					</c:forEach>
				</select>
			</div>
			<div class="select_box1">
				<label>선택</label>
				<select name="categoryId_3" id="alliance_category1_3" onchange="javascript:ccs.common.alliance.selectorCall();" value="">
					<option value="">선택</option>
					<c:forEach var="dir" items="${category}" varStatus="status">
						<c:if test="${dir.depth eq 4 and dir.upperCategoryId eq search.category2 and not empty search.category1}">
							<option value="${dir.categoryId}"  <c:if test="${search.category3 eq dir.categoryId}">selected</c:if>>${dir.name}</option>
						</c:if>
					</c:forEach>
				</select>
			</div>
		</div>
	</div>
	<div class="group_block">
		<div class="category_wrap">
			<span>선택2</span>
			<div class="select_box1">
				<label>2017</label>
				<select name="categoryId_4" id="alliance_category2_1" onchange="javascript:ccs.common.alliance.changeSelector('2');javascript:ccs.common.alliance.selectorCall();">
					<option value="">선택</option>
					<c:forEach var="dir" items="${category}" varStatus="status">
						<c:if test="${dir.depth eq 2}">
							<option value="${dir.categoryId}"  <c:if test="${search.category4 eq dir.categoryId}">selected</c:if>>${dir.name}</option>
						</c:if>
					</c:forEach>
				</select>
			</div>
			<div class="select_box1">
				<label>2017</label>
				<select name="categoryId_5" id="alliance_category2_2" onchange="javascript:ccs.common.alliance.selectorCall();">
					<option value="">선택</option>
					<c:forEach var="dir" items="${category}" varStatus="status">
						<c:if test="${dir.depth eq 3 and dir.upperCategoryId eq search.category4}">
							<option value="${dir.categoryId}"  <c:if test="${search.category5 eq dir.categoryId}">selected</c:if>>${dir.name}</option>
						</c:if>
					</c:forEach>
				</select>
			</div>
			<div class="select_box1">
				<label>2017</label>
				<select name="categoryId_6" id="alliance_category2_3" onchange="javascript:ccs.common.alliance.selectorCall();">
					<option value="">선택</option>
					<c:forEach var="dir" items="${category}" varStatus="status">
						<c:if test="${dir.depth eq 4 and dir.upperCategoryId eq search.category5 and not empty search.category4}">
							<option value="${dir.categoryId}"  <c:if test="${search.category6 eq dir.categoryId}">selected</c:if>>${dir.name}</option>
						</c:if>
					</c:forEach>
				</select>
			</div>
		</div>
	</div>