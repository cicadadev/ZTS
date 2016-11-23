<%--
	화면명 : 프론트 & 브랜드유형C, 브랜드 상품목록
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%
	pageContext.setAttribute("tommeetippe", Config.getString("brand.tommeetippe.code"));
	pageContext.setAttribute("allo", Config.getString("brand.allo.code"));
	pageContext.setAttribute("alfonso", Config.getString("brand.alfonso.code"));
	pageContext.setAttribute("fourlads", Config.getString("brand.fourlads.code"));
	pageContext.setAttribute("skarbarn", Config.getString("brand.skarbarn.code"));
	pageContext.setAttribute("royal", Config.getString("brand.royal.code"));
	pageContext.setAttribute("chooze", Config.getString("brand.chooze.code"));
	pageContext.setAttribute("yvol", Config.getString("brand.yvol.code"));
	
	String befUrl = request.getHeader("referer");
	pageContext.setAttribute("befUrl", befUrl );
%>
<script type="text/javascript" src="/resources/js/common/display.ui.${_deviceType }.js"></script>

<script type="text/javascript">

$(document).ready(function(){
	$(".mobile .content").removeAttr("style").css("padding", "50px 0 347px 0");
	
	dms.search.orderBy.pageInit();
	
	if(ccs.common.mobilecheck()){
		dms.common.searchMobilePage();
		swiperCon('brandSwiper_category', '5', 'auto');
	}
	
	if('${isMobile}'=='true'){
		
		$('.btn_navi_prev').click(function(){
			window.location.href='${befUrl}';
		});	
	}
	
	//카테고리 네비게이션 이벤트
	$('#dispSelect select').change(function(){
	  	var dipCtgId= $(this).val();
	  	if(dipCtgId !== ''){
	  		ccs.link.product.brandProdutList('CATEGORY','${brandInfo.brandId}',dipCtgId,'${rootCategoryId}')
	  	}
	});
});
</script>

<c:choose>
	<c:when test="${isMobile}">
		<!-- mobile 전용 네비 -->
<!-- 		<div class="mo_navi"> -->
<!-- 			<button type="button" class="btn_navi_prev">이전 페이지로..</button> -->
<!-- 			<h2> -->
<!-- 				제목만 노출 할 경우 -->
<%-- <%-- 				<a href="javascript:brand.template.main('${brandId}');"> --%>
<%-- 					<img src="/resources/img/mobile/txt/txt_brand_${brandInfo.brandId}.png" alt="" width="118.5" onclick="brand.template.main('${brandId}');" style="padding:0 0 10px 0;"/> --%>
<%-- 					<img src="/resources/img/pc/txt/txt_brand_${brandInfo.brandId}.png" alt="${brandInfo.name}" /> --%>
<!-- <!-- 				</a> -->
<!-- 			</h2> -->
<!-- 		</div> -->
 	
	<div class="swiper_wrap">
		<div class="mo_mainNavi brandNavi brandSwiper_category tab_outer"> <!-- 브랜드관일 경우 div class="brandNavi" 추가 -->
			<ul class="gnb swiper-wrapper">
				<c:forEach var="list" items="${category}" varStatus="status">
					<li class="swiper-slide">
<a href="javascript:ccs.link.product.brandProdutList('CATEGORY','${brandInfo.brandId}','${list.displayCategoryId}','${rootCategoryId}');" ${list.displayCategoryId == dispCategoryId ? 'class="on"' :''}><c:out value="${list.name}"/></a>
					</li>
				</c:forEach>
			</ul>
		</div>
	</div>
	</c:when>
	<c:otherwise>
		
		<div class="location_box">
			<div class="location_inner">
				<ul>
					<li>홈</li>
					<li>
						<a href="javascript:brand.template.main('${brandId}');">${brandInfo.name}</a>
					</li>
					<li>
						<a href="javascript:brand.template.main('${brandId}');">${currentCategory.name}</a>
					</li>
					<li>
						<div class="select_box1" id="dispSelect">
							<label></label>
							<select>
								<c:forEach var="list" items="${category}" >
									<option ${list.displayCategoryId == dispCategoryId ? 'selected="selected"' : '' } value="${list.displayCategoryId}"><c:out value="${list.name}"/></option>
								</c:forEach>
							</select>
						</div>
					</li>
					<%-- <c:forEach var="list" items="${category}" varStatus="">
						<c:if test="${list.displayCategoryId == dispCategoryId }">
							<li>
								<c:out value="${list.name}"/>
							</li>
						</c:if>
					</c:forEach> --%>
				</ul>
			</div>
		</div>
		
		<div class="brand_menu pc_only">
			<div class="brand_inner">
				<div class="brand_logo">
					<a href="javascript:brand.template.main('${brandId}');">
						<img src="/resources/img/pc/txt/txt_brand_${brandInfo.brandId}.png" alt="${brandInfo.name}" />
					</a>
				</div>
			</div>
		</div>
	</c:otherwise>
</c:choose>

<!-- 
	알로앤루일 경우 div class="brand1" / 
	궁중비책 경우 div class="brand2" /
	토미티피 경우 div class="brand3" /
	Y-VOLUTION 경우 div class="brand4" /
	섀르반 경우 div class="brand5" /
	알퐁소 경우 div class="brand6" /
	츄즈 경우 div class="brand7" /
	포래즈 경우 div class="brand8" /
-->
	
<c:if test="${brandId == allo}">
	<div class="brandType brand1 brandB_03">
</c:if>	
<c:if test="${brandId == royal}">
	<div class="brandType brand2 brandB_03">
</c:if>	
<c:if test="${brandId == tommeetippe}">
	<div class="brandType brand3 brandB_03">
</c:if>	
<c:if test="${brandId == yvol}">
	<div class="brandType brand4 brandB_03">
</c:if>	
<c:if test="${brandId == skarbarn}">
	<div class="brandType brand5 brandB_03">
</c:if>	
<c:if test="${brandId == alfonso}">
	<div class="brandType brand6 brandB_03">
</c:if>	
<c:if test="${brandId == chooze}">
	<div class="brandType brand7 brandB_03">
</c:if>	
<c:if test="${brandId == fourlads}">
	<div class="brandType brand8 brandB_03">
</c:if>	
	
	<div class="inner brand_products">

	<%-- 	<c:if test="${isMobile}">
			<div class="tab_outer swipeMenu txtType mo_only">
				<ul class="miniTabBox1">
					<c:forEach var="list" items="${category}" varStatus="status">
						<li>
							<div>
<!-- 줄바꿈하지마세요!! 퍼블리싱 깨집니다 -->
<a href="javascript:ccs.link.product.brandProdutList('CATEGORY','${brandInfo.brandId}','${list.displayCategoryId}','${rootCategoryId}');" ${list.displayCategoryId == dispCategoryId ? 'class="on"' :''}><c:out value="${list.name}"/></a>
							</div>
						</li>
					</c:forEach>
				</ul>
			</div>
		</c:if> --%>

		<jsp:include page="/WEB-INF/views/dms/include/searchOptionFilter.jsp" flush="false">
			<jsp:param name="type" 	value="brand" />
		</jsp:include>

		<div class="displayListBox">				
			
			<!-- 정렬 //-->
			<c:choose>
				<c:when test="${isMobile}">
					<div class="tit_style3 mo_only">	
				</c:when>
				<c:otherwise>
					<div class="tit_style3 pc_only">
				</c:otherwise>
			</c:choose>
			
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
</div>
