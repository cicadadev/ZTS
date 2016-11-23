<%--
	화면명 : 검색 상품목록
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" 	uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
	<input type="hidden" id="currentPage" 		name="currentPage"			value="${apiParam.currentPage}"/>
	<input type="hidden" id="pageSize"			name="pageSize"				value="${apiParam.pageSize}"/>
	<input type="hidden" id="direction"			name="direction"			value="${apiParam.direction}"/>
	<input type="hidden" id="sort"				name="sort"					value="${apiParam.sort}"/>
	<input type="hidden" id="categoryIds"		name="categoryIds"			value="${apiParam.categoryIdList}"/>
	<input type="hidden" id="brandId"			name="brandId"				value="${apiParam.brandIdList}"/>
	<input type="hidden" id="searchKeyword"		name="searchKeyword"		value="${apiParam.searchKeyword}"/>
	<input type="hidden" id="subKeyword"		name="keyword"				value="${apiParam.keyword}"/>
	<input type="hidden" id="businessId" 		name="businessId" 			value="${apiParam.businessId}"/>
	<input type="hidden" id="optionCtgList" 	name="optionCtgList" 		value="${apiParam.optionCtgStrs}"/>
	<input type="hidden" id="optionBrandSrcYn"	name="optionBrandSrcYn"		value="${apiParam.optionBrandSrcYn}"/>
	<input type="hidden" id="searchViewType" 	name="searchViewType" 		value="${apiParam.searchViewType}">
</form>


<c:forEach items="${searchApi.productList}" var="list" varStatus="status">
	<c:set value="${list.displayCategoryId}" var="displayCategoryId" />
	<c:if test="${fn:contains(displayCategoryId, categoryId)}">
		<li>
			<div>
				<a href="javascript:getProductPage('${list.productId}')">
					<div class="img">
						<tags:prdImgTag productId="${list.productId}" seq="0" size="326"  />
					</div>

					<div class="info">
						<div class="flag">
							<c:if test="${list.newIconYn eq 'Y'}">
								<span class="icon_type1">NEW</span>
							</c:if>
							<c:if test="${list.deliveryFeeFreeYn eq 'Y'}">
								<span class="icon_type1">무료배송</span>
							</c:if>
							<c:if test="${list.couponYn eq 'Y'}">
								<span class="icon_type1">쿠폰</span>
							</c:if>
							<c:if test="${list.pointYn eq 'Y'}">
								<span class="icon_type1">포인트</span>
							</c:if>
							<c:if test="${list.presentYn eq 'Y'}">
								<span class="icon_type1">사은품</span>
							</c:if>
						</div>
						<span class="title"><c:out value="${list.productName}"/></span>
						<div class="etc">
							
								<span class="ori">
								<c:if test="${list.oriSalePrice >  list.regularDeliveryPrice}">	
									<em><fmt:formatNumber type="currency" value="${list.oriSalePrice}" pattern="###,###" /></em>
								</c:if>									
								</span>
							<span class="price"> <em>정기배송가</em> <fmt:formatNumber type="currency" value="${list.regularDeliveryPrice}" pattern="###,###" />
							<i>원</i></span>
							<button type="button" class="btn_blank">새창 열기</button>
						</div>
					</div>
				</a>
			</div>
		</li>
	</c:if>
</c:forEach>

<!-- paging //-->
<div class="paginateType1">
	<page:searchPaging currentPage="${search.currentPage}" pageSize="${search.pageSize}"  total="${search.totalCount}" callback="dms.common.searchPaging"/>
</div>
<!--// paging -->
