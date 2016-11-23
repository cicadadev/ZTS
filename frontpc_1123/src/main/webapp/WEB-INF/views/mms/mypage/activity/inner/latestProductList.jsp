<%--
	화면명 : 마이페이지 > 나의활동 > 최근 본 상품 list
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<script type="text/javascript">

	$(".pc .recent .btn_morePrd").off("click").on("click", function(){
		$(this).toggleClass("on")
		$(this).closest(".tr_box").children(".morePrd_list").toggle();
	});

	function recommandPr(name, productId) {
		pms.common.getRecommendationProductList($('[name=\"'+name+'\"]'),  {recType:'a002', size : 5, iids :productId });
	}
	
</script>

<div>
	<input type="hidden" id="total" value="${totalCount + 0 }">
</div>
	
<div>
	<ul class="div_tb_tbody3">
	
		<c:choose>
			<c:when test="${!empty latestProductList }">
				<c:forEach items="${latestProductList}" var="item" varStatus="status">
				<c:set var="index" value="${status.index }"/>
					<li>
						<div class="tr_box">
							<div class="col1">
								<div class="positionR">
									<div class="prod_img">
										<a href="javascript:void(0);" onclick="javascript:ccs.link.product.detail('${item.productId}')">
											<tags:prdImgTag productId="${item.productId}" size="90" alt="" />
										</a>
									</div>
									
									<div class="flag">
										<c:if test="${item.freeDeliveryYn eq 'Y'}">
											<span class="icon_type1">무료배송</span>
										</c:if>
										<c:if test="${item.couponYn eq 'Y'}">
											<span class="icon_type1">쿠폰</span>
										</c:if>
										<c:if test="${item.pointSaveYn eq 'Y'}">
											<span class="icon_type1">사은품</span>
										</c:if>
									</div>
									
									<a href="javascript:void(0);" class="title" onclick="javascript:ccs.link.product.detail('${item.productId}')">
										${item.name}
									</a>
									
									<!-- 16.09.21 -->
									<div>
										<a href="javascript:void(0);" class="btn_morePrd" onclick="javascript:recommandPr('otherMomsShow_${index }','${item.productId}')">다른 엄마가 본 상품</a>
									</div>
									<!-- //16.09.21 -->
									
								</div>
							</div>
			
							<div class="col2">
								<span class="price">
									<em><fmt:formatNumber value="${item.salePrice}" pattern="###,###" /> <i>원</i></em>
								</span>
							</div>
			
							<div class="col3">
								<a href="#none" class="btn_sStyle3 sGray2 btn_del" onclick="javascript:mypage.latestProduct.deleteLatestProduct('${item.productId}');">삭제</a>
							</div>
							
							<!-- 16.09.21 : 다른 엄마가 본 상품 리스트 -->
							<%--TODO 엄마가 본 상품 --%>
							<div class="morePrd_list" >
								<ul name="otherMomsShow_${index }"></ul>
							</div>
							<!-- //16.09.21 : 다른 엄마가 본 상품 리스트 -->
							
							<!-- SOLD OUT -->
							<c:if test="${item.realStockQty == 0}">
								<span class="soldoutTxt">
									<em>
										<strong>SOLD OUT</strong>판매가 완료되었습니다
									</em>
								</span>								
							</c:if>
							
						</div>
					</li>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<li class="empty" id="list_empty">
					<div class="tr_box">
						<div class="col99">
							최근 본 상품 내역이 없습니다.
						</div>
					</div>
				</li>
			</c:otherwise>
		</c:choose>

	</ul>
	
	<!-- ### //테이블 바디 ### -->
	
	<!-- ### PC 페이징 ### -->
	<div class="pagePkg">
		<div class="paginateType1">
			<page:paging formId="" currentPage="${search.currentPage}" pageSize="${search.pageSize}"
				total="${totalCount}" url="/mms/mypage/latestProduct/list/ajax" type="ajax" callback="mypage.latestProduct.latestProductCallback"/>
		</div>
	</div>
	<!-- ### //PC 페이징 ### -->

</div>