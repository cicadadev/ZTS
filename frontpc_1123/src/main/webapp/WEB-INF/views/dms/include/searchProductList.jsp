<%--
	화면명 : 검색 상품목록
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" 	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<script type="text/javascript">

$(document).ready(function(){
	dms.search.orderBy.pageInit();	
	dms.search.list.listCount('${search.totalCount}');
	
});

function getProductPage(productId){
	ccs.link.product.detail(productId,encodeURI(encodeURIComponent($('#searchKeyword').val())))
}
</script>

<form action = "" id="dmsDisplaySearch" name="searchApiReqSearch" method="post">
	<input type="hidden" id="currentPage" 		name="currentPage"			value="${search.currentPage}"/>
	<input type="hidden" id="pageSize"			name="pageSize"				value="${search.pageSize}"/>
	<input type="hidden" id="direction"			name="direction"			value="${search.direction}"/>
	<input type="hidden" id="sort"				name="sort"					value="${search.sort}"/>
	<input type="hidden" id="categoryIds"		name="categoryIds"			value="${search.categoryIdList}"/>
	<input type="hidden" id="depthCategoryIds"	name="depthCategoryIds"		value="${search.depthCategoryIds}"/>
	<input type="hidden" id="brandId"			name="brandId"				value="${search.brandIdList}"/>
	<input type="hidden" id="searchKeyword"		name="searchKeyword"		value="${search.searchKeyword}"/>
	<input type="hidden" id="subKeyword"		name="keyword"				value="${search.keyword}"/>
	<input type="hidden" id="businessId" 		name="businessId" 			value="${search.businessId}"/>
	<input type="hidden" id="optionCtgList" 	name="optionCtgList" 		value="${search.optionCtgStrs}"/>
	<input type="hidden" id="optionBrandSrcYn"	name="optionBrandSrcYn"		value="${search.optionBrandSrcYn}"/>
	<input type="hidden" id="searchViewType" 	name="searchViewType" 		value="${search.searchViewType}"/>
	<input type="hidden" id="ageTypeCode" 		name="ageTypeCodeList" 		value="${search.ageTypeCodeList}"/>
</form>
	<c:if test="${searchApi.viewCount > 0}">
		<c:choose>
			<c:when test="${param.type eq 'subscription'}">
				<div class="product_type1 prodType_4ea block border_none">
			</c:when>
			<c:otherwise>
				<div class="product_type1 prodType_4ea block">
			</c:otherwise>
		</c:choose>
			
			<ul>
				<c:forEach items="${searchApi.productList}" var="list" varStatus="status">
					<c:choose>
						<c:when test="${isMobile}">
							<li onclick="javascript:getProductPage('${list.productId}')">	
						</c:when>
						<c:otherwise>
							<li>
						</c:otherwise>
					</c:choose>
					
						<div>
							<div class="img">
								<c:choose>
									<c:when test="${isMobile}">
										<a href="javascript:void(0);">	
									</c:when>
									<c:otherwise>
										<a href="javascript:getProductPage('${list.productId}')">
									</c:otherwise>
								</c:choose>
								
									<c:choose>
										<c:when test="${isMobile}">
											<tags:prdImgTag productId="${list.productId}" seq="0" size="180"  />
										</c:when>
										<c:otherwise>
											<tags:prdImgTag productId="${list.productId}" seq="0" size="326"  />
										</c:otherwise>
									</c:choose>								
								</a>
							</div>
					
							<div class="info">
								<div class="flag">
									<c:if test="${list.newIconYn eq 'Y'}">
										<span class="icon_type1 new">NEW</span>
									</c:if>
									<c:if test="${list.deliveryFeeFreeYn eq 'Y'}">
										<span class="icon_type1">무료배송</span>
									</c:if>
									<c:if test="${list.couponYn eq 'Y'}">
										<span class="icon_type1">쿠폰</span>
									</c:if>
									<c:if test="${list.pointYn eq 'Y'}">
										<span class="icon_type1 point">포인트</span>
									</c:if>
									<c:if test="${list.presentYn eq 'Y'}">
										<span class="icon_type1">사은품</span>
									</c:if>
								</div>
								<span class="title"><a href="javascript:getProductPage('${list.productId}')"><c:out value="${list.productName}"/></a></span>
								<div class="etc">
									<c:choose>
										<c:when test="${param.type eq 'subscription'}">
											<span class="ori">
												<c:if test="${list.oriSalePrice >  list.regularDeliveryPrice}">	
													<em><fmt:formatNumber type="currency" value="${list.oriSalePrice}" pattern="###,###" /></em>
												</c:if>									
											</span>
												<span class="price"> <em>정기배송가</em> <fmt:formatNumber type="currency" value="${list.regularDeliveryPrice}" pattern="###,###" />
												<i>원</i></span>
												<button type="button" class="btn_blank">새창 열기</button>
										</c:when>
										<c:otherwise>
											<fmt:parseNumber value = "${list.oriSalePrice}" var = "oriSalePrice"/>
											<fmt:parseNumber value = "${list.salePrice}" var = "salePrice"/>
											<span class="ori">
												<c:if test="${oriSalePrice > salePrice}">
													<em><fmt:formatNumber type="currency" value="${oriSalePrice}" pattern="###,###" /></em>
												</c:if>	
											</span>
											
											<span class="price"><fmt:formatNumber type="currency" value="${salePrice}" pattern="###,###" /><i>원</i></span>
											
											<c:if test="${!isMobile}">
												<div class="align_r">
													<button type="button" class="btn_blank" onclick="ccs.link.openWindow('${list.productId}');">새창 열기</button>
												</div>
											</c:if>
										</c:otherwise>
									</c:choose>
								</div>
							</div>
					
							<!-- soldout 일 경우 -->
							<!-- <span class="txt_soldout">
								<em>
									<strong>SOLD OUT</strong>판매가 완료되었습니다
								</em>
							</span> -->
						</div>
					</li>
				</c:forEach>
			</ul>
		</div>
		<!-- //상품 리스트 -->
		
		<!-- paging //-->
		<c:if test="${param.pagingYn eq 'Y' or search.pagingYn eq 'Y'}">
			<div class="paginateType1">
				<page:searchPaging currentPage="${search.currentPage}" pageSize="${search.pageSize}"  total="${search.totalCount}" callback="dms.common.searchPaging"/>
			</div>
		</c:if>
		<!--// paging -->
	</c:if>
	

