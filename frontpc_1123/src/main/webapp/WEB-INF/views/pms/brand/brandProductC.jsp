<%--
	화면명 : 프론트 & 브랜드유형C, 브랜드 상품목록
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<%
	pageContext.setAttribute("mainBnr", Config.getString("corner.brand.templC.intro") );
%>
<script type="text/javascript" src="/resources/js/common/display.ui.${_deviceType }.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	dms.search.orderBy.pageInit();
	
	if(ccs.common.mobilecheck()){
		dms.common.searchMobilePage();
	}
	
});
</script>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
<jsp:param value="상품상세|${brandInfo.name}" name="pageNavi"/>
</jsp:include>		
	
<div class="brandType brandC_01">
	<div class="inner brand_home">
		<!-- 상단 비주얼 -->
	<c:set var="mainBnrMap"  value="${cornerMap[mainBnr]}"/>
	<c:if test="${not empty mainBnrMap.dmsDisplayitems}">
		<div class="visual type1">
			<ul class=""> <!-- 16.10.01 : class(vImg) 삭제 -->
			<c:forEach var="item" items="${mainBnrMap.dmsDisplayitems}" varStatus="status">
				<li class="on">
					<a href="#">
						<c:choose>
							<c:when test="${isMobile}">
								<!-- pc 이미지 1000x505 -->
								<img src="${_IMAGE_DOMAIN_}${item.img2}" alt="${item.img2}" />
							</c:when>
							<c:otherwise>
								<img src="${_IMAGE_DOMAIN_}${item.img1}" alt="${item.img1}" />
							</c:otherwise>
						</c:choose>	
					</a>
				</li>
				<!-- <li><a href="#"><img src="img/pc/temp/temp_brand3_02.jpg" alt="" /></a></li> 16.10.01 : 삭제 -->
			</c:forEach>
			</ul>
		</div>
	</c:if>
	<!-- // 상단 비주얼 -->

	<!-- 옵션필터링 -->
	<jsp:include page="/WEB-INF/views/dms/include/searchOptionFilter.jsp" flush="false">
		<jsp:param name="type" 	value="brand" />
	</jsp:include>

	<div class="displayListBox">
	
		<c:choose>
			<c:when test="${isMobile}">
				<div class="tit_style3 mo_only">	
			</c:when>
			<c:otherwise>
				<div class="tit_style3 pc_only">
			</c:otherwise>
		</c:choose>

			<!-- 정렬 //-->
			<jsp:include page="/WEB-INF/views/dms/include/searchOrderby.jsp" flush="false"/>
			
			<strong class="sort_num">
				<span>
					총 <em id="searchCount">${search.totalCount}</em>건
				</span>
			</strong>
		</div>
		<!-- 상품 리스트 -->
		<div class="list_group" id="productList">
			<c:choose>
				<c:when test="${isMobile}">
					<jsp:include page="/WEB-INF/views/dms/include/searchProductList.jsp" flush="false">
						<jsp:param value="N" name="pagingYn"/>
					</jsp:include>
				</c:when>
				<c:otherwise>
					<jsp:include page="/WEB-INF/views/dms/include/searchProductList.jsp" flush="false">
						<jsp:param value="Y" name="pagingYn"/>
					</jsp:include>
				</c:otherwise>
			</c:choose>
		</div>
	</div>
</div>
