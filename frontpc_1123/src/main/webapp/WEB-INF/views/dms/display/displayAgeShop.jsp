<%--
	화면명 : 프론트  월령 상품목록
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>

<% 
	String befUrl = request.getHeader("referer");
	pageContext.setAttribute("befUrl", befUrl );

%>
<script type="text/javascript" src="/resources/js/common/display.ui.${_deviceType }.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	dms.search.orderBy.pageInit();
	
	if(ccs.common.mobilecheck()){
		dms.common.searchMobilePage();
	}
	
	if('${isMobile}'=='true'){
			
		$('.btn_navi_prev').click(function(){
			window.location.href='${befUrl}';
		});	
				
	}else{
		//전시카테고리 네비게이션 이벤트
		$('#ageSelect select').change(function(){
			  //var dipCtgId= $("select[name=fntcategorySelect]").val();
		  	var ageCode= $(this).val();
		  	if(ageCode !== ''){
		  		
		  		var code = ageCode.split(".")[1];
				window.location.href='/dms/display/ageShop?ageCode='+code;
		  	}
		});	
	}
});


</script>

<c:choose>
	<c:when test="${isMobile}">
		<div class="mo_navi">
				<button type="button" class="btn_navi_prev">이전 페이지로..</button>
				<h2>
					<!-- 소카테고리가 호출 할 경우 -->
					<c:out value="${currentCode.name}"/>
				</h2>
			</div>
	</c:when>
	<c:otherwise>
		<div class="location_box">
			<div class="location_inner">
				<ul>
					<li>홈</li>
					<li>
						<div class="select_box1" id="ageSelect">
							<label><c:out value="${currentCode.name}"/> </label>
							<select>
								<c:forEach var="list" items="${ageCodeList}" >
									<option ${currentCode.cd == list.cd ? 'selected="selected"' : '' } value="${list.cd}"><c:out value="${list.name}"/> </option>
								</c:forEach>
							</select>
						</div>
					</li>
				</ul>
			</div>
		</div>
	</c:otherwise> 
</c:choose>

	<%-- <c:if test="${isMobile}">
		<div class="tab_outer swipeMenu txtType mo_only">
			<ul class="miniTabBox1">
				<c:forEach var="list" items="${ageCodeList}" varStatus="status">
					<li>
						<div>
							<!-- 줄바꿈하지마세요!! 퍼블리싱 깨집니다 -->
							<a href="/dms/display/ageShop?ageCode='" ${currentCode.cd == list.cd ? 'class="on"' :''}><c:out value="${list.name}"/></a>
						</div>
					</li>
				</c:forEach>
			</ul>
		</div>
	</c:if> --%>
			
<div class="inner">
	<div class="cateSub">
		<!-- 옵션필터링 -->
		<jsp:include page="/WEB-INF/views/dms/include/searchOptionFilter.jsp" flush="false">
			<jsp:param name="type" 	value="age" />
		</jsp:include>
		
		<div class="displayListBox mt">
			<div class="tit_style3">
				<strong class="sort_num"> <%-- <c:out value="${currentCode.name}"/> --%>
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