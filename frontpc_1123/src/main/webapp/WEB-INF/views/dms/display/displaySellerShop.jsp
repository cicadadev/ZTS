<%--
	화면명 : 판매자매장
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<script type="text/javascript" src="/resources/js/common/display.ui.${_deviceType }.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	dms.search.orderBy.pageInit();
	
	if(ccs.common.mobilecheck()){
		dms.common.searchMobilePage();
	}
});

</script>

	<!-- 네비게이션 -->
	<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
		<jsp:param value="판매자매장" name="pageNavi"/>
	</jsp:include>	
	<div class="inner">
		<div class="cateSub">

			<!-- 옵션필터링 -->
			<jsp:include page="/WEB-INF/views/dms/include/searchOptionFilter.jsp" flush="false"/>
			
			<div class="displayListBox mt">
				
				<div class="tit_style3">
					<strong  class="sort_num"> 
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
