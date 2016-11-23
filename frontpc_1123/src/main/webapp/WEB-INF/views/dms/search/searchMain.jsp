<%--
	화면명 : 검색 메인
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"  %>
<%@ taglib prefix="func" uri="kr.co.intune.commerce.common.functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript" src="/resources/js/common/display.ui.${_deviceType }.js"></script>
<script type="text/javascript">

if('${deviceTypeCd}' == 'DEVICE_TYPE_CD.PC'){
	device = 'PW';
}else if('${deviceTypeCd}' == 'DEVICE_TYPE_CD.APP'){
	device = 'MI';
}else if ('${deviceTypeCd}' == 'DEVICE_TYPE_CD.MW'){
	device = 'MW';
}

//추천엔진수집 스크립트
window._rblqueue = window._rblqueue || [];
  _rblqueue.push(['setVar','cuid',global['config'].recobelCuid]);
  _rblqueue.push(['setVar','device',device]);
  _rblqueue.push(['setVar','userId','']);		// optional
  _rblqueue.push(['setVar','searchTerm','${keyword}']);
  _rblqueue.push(['track','search']);	
  setTimeout(function() {
    (function(s,x){s=document.createElement('script');s.type='text/javascript';
    s.async=true;s.defer=true;s.src=(('https:'==document.location.protocol)?'https':'http')+
    '://assets.recobell.io/rblc/js/rblc-apne1.min.js';
    x=document.getElementsByTagName('script')[0];x.parentNode.insertBefore(s, x);})();
  }, 0);

 $(document).ready(function(){
	
	 dms.search.orderBy.pageInit();

	 //결과내재검색
	$('#rebrowsingKey').keyup(function () {
		if(event.keyCode==13){
			reSearchKey();
		}
	});
	 
	if(ccs.common.mobilecheck()){
		dms.common.searchMobilePage();
		swiperCon('searchResultSwiper_relatedSearches', 'auto'); // 뎐관검색어
	}
	
	//추천엔진
	dms.search.getRecommendationProductList('relationProduct', {recType:'s001', size : 8, st:'${keyword}'});
	
	// 검색결과
	
	//swiperCon('searchResultSwiper_searchList_1', '1', 30);
	//swiperCon('searchResultSwiper_searchList_2', '1', 30);
});

function reSearchKey(){
	var param = {'keyword': $('#rebrowsingKey').val()};
	dms.search.option.getProduct(param,"");
}

</script>
	<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
		<jsp:param value="검색결과" name="pageNavi"/>
		<jsp:param value="${not empty brandInfo ? 'brandSearch' : ''}" name="type"/>
	</jsp:include>		
	<div class="inner">
		<div class="srchResult">
			<c:if test="${!isMobile}">
				<!--// 검색어 -->
				<div class="titPkg pc_only">
					<h2 class="tit"><em>${keyword}</em> 검색결과<span><em><fmt:formatNumber type="currency" value="${search.totalCount}" pattern="###,###" /></em>건</span></h2><!-- roy 10-24 1000단위 구분자 넣기 -->
					<div class="srchArea posR">
						<div class="inpBox">
							<div class="inputTxt_place1">
								<label>결과내 재검색</label>
								<span>
									<input type="text" value="${search.keyword}" id="rebrowsingKey"/>
								</span>
							</div>
							<a href="javaScript:reSearchKey();" class="btn_style6 btnSrch">검색</a>
						</div>
					</div>
				</div>
				<!-- 검색어 //-->
			</c:if>
			
			<!--// 연관 검색어 -->
		
			<div class="rw_displayListBox">
				<c:choose>
					<c:when test="${isMobile}">
						<div class="srchKeyword navi_swipe mo_only">
							<div class="tit">연관 검색어</div>
							<div class="swiper_wrap">
								<div class="swiper-container searchResultSwiper_relatedSearches">
									<ul class="swiper-wrapper">
										<c:forEach items="${relation.Word}" var="word">
											<li class="swiper-slide"><a href="javaScript:dms.common.setKeyWord('${word}');"><span><c:out value="${word}"/></span></a></li>
										</c:forEach>
									</ul>
								</div>
							</div>
						</div>
					</c:when>
					<c:otherwise>
						<div class="srchKeyword pc_only">
							<div class="tit">연관 검색어</div>
							<ul>
								<c:forEach items="${relation.Word}" var="word">
									<li><a href="javaScript:dms.common.setKeyWord('${word}');"><span><c:out value="${word}"/></span></a></li>
								</c:forEach>
							</ul>
						</div>
					</c:otherwise>
				</c:choose>
			</div>
			<!-- 연관 검색어  //-->
			
			<!-- 옵션필터링 -->
			<jsp:include page="/WEB-INF/views/dms/include/searchOptionFilter.jsp" flush="false"/>
			
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
				<!-- 상품 리스트 -->
				<div class="list_group" id="productList">
					<c:choose>
						<c:when test="${isMobile}">
							<jsp:include page="/WEB-INF/views/dms/include/searchProductList.jsp" flush="false">
								<jsp:param value="N" name="pagingYn"/>
							</jsp:include>
						</c:when>
						<c:otherwise>
							<c:choose>
								<c:when test="${search.totalCount < 5 }">
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
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			
			<c:if test="${search.totalCount lt 5 }">
				<div class="rolling_box " id="relationProduct">
					
				</div>
			</c:if>
		</div>
	</div>
