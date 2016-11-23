<%--
	화면명 : 베스트매장
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">
$(document).ready(function() {
	display.bestShop.main();
});
</script>
	
	<c:choose>
	<c:when test="${isMobile ne 'true' }">
		<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
			<jsp:param value="베스트" name="pageNavi"/>
		</jsp:include>	
	</c:when>
	<c:otherwise>	
		<jsp:include page="/WEB-INF/views/gsf/layout/page/mo/title_menu.jsp" flush="false" >
			<jsp:param name="titleMenu" value="3" />
		</jsp:include>
	</c:otherwise>	
	</c:choose>
	
	<div class="inner">
		<div class="main_best">
			<h2 class="tit_best">
				<img src="/resources/img/pc/txt/best.gif" alt="" />
				<span id="infoTxt" style="font-size:12pt;">* 고객님들이 많이 클릭한 상품순입니다.</span>
	
				<div class="sortBoxList sort_2ea">
					<ul>
						<li>
							<div class="select_box1">
								<label style="font-size:10pt;">월령</label>
								<select id="ageSelectbox" onchange="display.bestShop.changeAgeOrGender();">
									<option>전체</option>
								</select>
							</div>
						</li>
						<li>
							<div class="select_box1">
								<label style="font-size:10pt;">성별</label>
								<select id="genderSelectbox" onchange="display.bestShop.changeAgeOrGender();">
									<option value="">전체</option>
									<option value="B">여아</option>
									<option value="A">남아</option>
								</select>
							</div>
						</li>
						<c:if test="${isMobile eq 'true'}">
							<li class="mo_only">
								<button type="button" class="btnListType block">블록형 / 리스트형</button>
							</li>
						</c:if>
						
					</ul>
				</div>
			</h2>
	
			<div class="best_tab">
				<ul class="best_type">
					<li class="on">
						<a href="#none" class="real" onclick="display.bestShop.makeParam('click', 'Y');">클릭 BEST</a>
	
						<ol id="categoryArea">
							<li class="item1 on" id="default1">
								<a href="#none">전체</a>
							</li>
							<c:forEach items="${categoryList}" var="category1" varStatus="category1_status">
								<c:if test="${category1_status.index < 4}"> <!-- 임시 -->
									<li class="item${category1_status.index + 2}">
										<a href="#none">${category1.name}</a>
										<input type="hidden" name="hid_category1" id="hid_category1" value="${category1.displayCategoryId}" />
									</li>
								</c:if>
							</c:forEach>
						</ol>
					</li>
					<li>
						<a href="#none" class="week" onclick="display.bestShop.makeParam('order', 'Y');">판매 BEST</a>

						<ol id="categoryArea2" style="display:none;">
							<li class="item1" id="default2">
								<a href="#none">전체</a>
							</li>
							<c:forEach items="${categoryList}" var="category1" varStatus="category1_status2">
								<c:if test="${category1_status2.index < 4}"> <!-- 임시 -->
									<li class="item${category1_status2.index + 2}">
										<a href="#none">${category1.name}</a>
										<input type="hidden" name="hid_category1" id="hid_category1" value="${category1.displayCategoryId}" />
									</li>
								</c:if>
							</c:forEach>
						</ol>
					</li>
				</ul>
			</div>
			
			<div id="category2_Area">
				<c:choose>
					<c:when test="${isMobile eq 'true'}">
						<ul class="sortBox sortBox1_4ea" id="default3" style="height:0;">
							<li class="active">
								<a href="#none">
									<span></span>
								</a>
							</li>
						</ul>
					</c:when>
					<c:otherwise>
						<ul class="miniTabBox1 pc_only" id="default3" style="border:none;">
							<li>
								<div style="border:none;">
									<a href="#none"></a>
								</div>
							</li>
						</ul>
					</c:otherwise>
				</c:choose>
				
				<c:forEach items="${categoryList}" var="category1" varStatus="category1_status3">
					<c:choose>
						<c:when test="${isMobile eq 'true'}">
							<ul class="sortBox sortBox1_4ea" style="display:none;">
								<li class="active" onclick="display.bestShop.setSearchParam('all');">
									<a>
										<span>전체</span>
									</a>
								</li>
								<c:forEach items="${category1.dmsDisplaycategorys}" var="category2" varStatus="category2_status">
									<li onclick="display.bestShop.setSearchParam('${category2.displayCategoryId}');">
										<a>
											<span>${category2.name}</span>
											<input type="hidden" name="hid_category2" id="hid_category2" value="${category2.displayCategoryId}" />
										</a>
									</li>
								</c:forEach>
							</ul>
						</c:when>
						<c:otherwise>
							<ul class="miniTabBox1 pc_only" style="display:none;">
								<li onclick="display.bestShop.setSearchParam('all');">
									<div>
										<a class="on">전체</a>
									</div>
								</li>
								<c:forEach items="${category1.dmsDisplaycategorys}" var="category2" varStatus="category2_status">
									<li onclick="display.bestShop.setSearchParam('${category2.displayCategoryId}');">
										<div>
											<a>${category2.name}</a>
											<input type="hidden" name="hid_category2" id="hid_category2" value="${category2.displayCategoryId}" />
										</div>
									</li>
								</c:forEach>
							</ul>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</div>
	
			<div class="list_group" style="${isMobile ne 'true' ? 'border-top:1px dashed #e7e7e7;' : ''}">
				<div class="product_type1 prodType_4ea block" id="bestListArea">
				</div>
			</div>
		</div>
	</div>
