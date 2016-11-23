<%--
	화면명 : 기획전 메인
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script>
$(document).ready(function() {
	if (ccs.common.mobilecheck()) {
		$(window).bind("scroll", productListScrollListener);
		
		function productListScrollListener() {
			var rowCount = $(".exhibitList_ul").find("li").length;
			var totalCount = Number($("[name=totalCount]").val());
			var maxPage = Math.ceil(totalCount/10);
			
			var scrollTop = $(window, document).scrollTop();
			var scrollHeight = $(document).height() - $(window).height();
			
			if (scrollTop >= scrollHeight - 200 && scrollHeight != 0) {
				if(rowCount !=0 && (rowCount < totalCount)){
					
					if ($("#lodingBar").length > 0 ) {
						return;
					}
					exhibit.main.getExhibitList();
				}
			}
		}
	}
});




</script>


<div class="promotion">


	<!-- mobile -->
	<div class="sortBoxBtn sort_2ea pcHide">
		<ul>
			<li>
				<div class="select_box1 radiusSel">
					<label>카테고리</label>
					<select onchange="javascript:exhibit.main.changeCategoryNbrand(this);" id="select_cate">
						<option value="all">카테고리</option>
						<option value="10001">식품/분유</option>
						<option value="10002">물티슈/기저귀</option>
						<option value="10003">의류/잡화</option>
						<option value="10004">용품/완구/화장품</option>
					</select>
				</div>
			</li>
			<li>
				<div class="select_box1 radiusSel">
					<label>브랜드</label>
					<select onchange="javascript:exhibit.main.changeCategoryNbrand(this);" id="select_brand">
						<option value='all'>브랜드</option>
						<c:forEach var="brand" items="${exhibitBrandList}" varStatus="i">
							<option value="${brand.brandId}">${brand.name}</option>
						</c:forEach>
					</select>
				</div>
			</li>
		</ul>
	</div>
	<!-- //mobile -->
	
	<div class="list_package">
		<div class="list_package_ctrlwap">
			<ul class="sortBox2">
				<li class="on">
					<a href="javascript:changeTab('category');">
						<span>카테고리</span>
					</a>
					<ul class="tab_box_02" id="category_ul">
						<li class="active" id="cate_all"><a href="javascript:changeCate('all');" class="cate1">카테고리</a></li>
						<li id="cate_10001"><a href="javascript:changeCate('10001');" class="cate2">식품/분유</a></li>
						<li id="cate_10002"><a href="javascript:changeCate('10002');" class="cate3">물티슈/기저귀</a></li>
						<li id="cate_10003"><a href="javascript:changeCate('10003');" class="cate4">의류/잡화</a></li>
						<li id="cate_10004"><a href="javascript:changeCate('10004');" class="cate5">용품/완구/화장품</a></li>
					</ul>
				</li>
				<li>
					<a href="javascript:changeTab('brand');">
						<span>브랜드</span>
					</a>
					<!-- Swiper -->
				        <div class="brand_type swipes">
							<ul class="tab_box_02" id="brand_ul">
					            <li class="active" id="brand_all"><a href="javascript:changeBrand('all')">브랜드</a></li>
									<c:forEach var="brand" items="${exhibitBrandList}" varStatus="i">
										<c:choose>
											<c:when test="${i.first }">
												<li id="brand_${brand.brandId}"><a href="javascript:changeBrand('${brand.brandId}')">${brand.name}</a></li>
											</c:when>
											<c:otherwise>
												<li id="brand_${brand.brandId}"><a href="javascript:changeBrand('${brand.brandId}')">${brand.name}</a></li>
											</c:otherwise>
										</c:choose>
									</c:forEach>
							</ul>
				        </div>
				        
				        <c:if test="${fn:length(brandList) > 5 }">
					        <!-- Add Arrows -->
					        <div class="tabNavi_btns">
								<a href="#none" class="prev" id="brand_prev">이전</a>
								<a href="#none" class="next" id="brand_next">다음</a>
							</div>
				        </c:if>
				        
				</li>
			</ul>
		</div>

		<div class="tab_con tab_conOn">
			<div class="promotion_list" id="exhibitListDiv">
				<ul class="exhibitList_ul">
				
				
				</ul>
			</div>
		</div>
	</div>
</div>
