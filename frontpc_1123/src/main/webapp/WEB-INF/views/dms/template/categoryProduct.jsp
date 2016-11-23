<%--
	화면명 : 프론트  카테고리매장 리프카테고리의 상품리스트
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<script type="text/javascript" src="/resources/js/common/display.ui.${_deviceType }.js"></script>
<script type="text/javascript">
//$("span#"+sort).closest( "li" ).addClass("active");
//console.log($("span#"+sort).closest( "li" ));

$(document).ready(function(){
	dms.search.orderBy.pageInit();
	
	if(ccs.common.mobilecheck()){
		
		dms.common.searchMobilePage();
	}
	
});

</script>
	
<!-- 네비게이션 -->
<c:choose>
	<c:when test="${!isMobile}">
		<jsp:include page="/dms/category/navi" flush="false">
			<jsp:param name="categoryId" 	value="${dispCategoryId}" />
		</jsp:include>			
	</c:when>
	<c:otherwise>
		<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
			<jsp:param value="${currentCategory.name}" name="name"/>
			<jsp:param value="${currentCategory.depth}" name="depth"/>
			<jsp:param value="category" name="type"/>
		</jsp:include>
	</c:otherwise>
</c:choose>


<div class="inner">
	<div class="cateSub">

		<!-- 소카테고리 목록 -->
		<c:choose>
			<c:when test="${isMobile}">
				<div class="categoryS">
					<jsp:include page="/WEB-INF/views/dms/include/categoryList.jsp" flush="false"/>
				</div>
			</c:when>
			<c:otherwise>
				<div class="pc_categoryS">
					<jsp:include page="/WEB-INF/views/dms/include/categoryList.jsp" flush="false"/>
				</div>
			</c:otherwise>
		</c:choose>
		
		<!-- 옵션필터링 -->
		<jsp:include page="/WEB-INF/views/dms/include/searchOptionFilter.jsp" flush="false">
			<jsp:param name="type" 	value="category" />
		</jsp:include>
		
		<div class="displayListBox mt">
			
			<c:choose>
				<c:when test="${isMobile}">
					<div class="tit_style3 mo_only">
				</c:when>
				<c:otherwise>
					<div class="tit_style3 pc_only">
				</c:otherwise>
			</c:choose>
			
			<strong class="sort_num">
				<span>
					총 <em id="searchCount">${search.totalCount}</em>건
				</span>
			</strong>
					
				<!-- 정렬 //-->
				<jsp:include page="/WEB-INF/views/dms/include/searchOrderby.jsp" flush="false"/>
			</div>
			<!-- 상품 리스트 //-->
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
</div>
