<%--
	화면명 : 상품상세 - 상품평목록
	작성자 : eddie
 --%>

<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<script>
	$(document).ready(function(){
		var totalCount = '${listCount.cntAll}';
		$('#reviewTotalCount1').html('('+totalCount+')');
		$('#reviewTotalCount2').html('('+totalCount+'건)');
		$('#reviewTotalCount3').html('('+totalCount+')');
		
		if(Number(totalCount) == 0){
			$('#reviewEmptyNotice').show();
			$('#reviewEmptyTit').hide();
			$('#reviewEmptyRating').hide();
			
		}
		
		var imgCount = '${listCount.cntImgY}';
		var permitCount = '${listCount.cntPermitY}';
		
		$('#reviewPhotoCount').html('('+imgCount+')');
		$('#reviewRepermitCount').html('('+permitCount+')');
			
	});
</script>
<form id="searchForm">
	<input type="hidden" value="${search.productId }" name="productId"/>
	<input type="hidden" value="${empty search.orderBy ? '01' :search.orderBy }" name="orderBy"/>
	<input type="hidden" value="${search.type}" 	name="type"/>	
</form>
	<div class="tbl_box2">
		<table>
			<caption>
				전체 상품평 목록입니다. 평점, 내용, 등록자, 등록일 등의 정보가 제공 됩니다.
			</caption>
			<colgroup>
				<col style="width:15%;">
				<col style="width:*">
				<col style="width:10%;">
				<col style="width:10%;">
			</colgroup>
			<thead>
				<tr><th scope="col">평점</th>
				<th scope="col">내용</th>
				<th scope="col">등록자</th>
				<th scope="col">등록일</th>
			</tr></thead>
			<tbody>
<c:choose>
<c:when test="${!empty list}">
<c:forEach items="${list}" var="review" varStatus="index">	
				<tr>
					<td class="td_rating">
						<span class="rating"><em style="width:${review.rating * 20 }%;">${review.rating }점</em></span>
						<c:if test="${review.imgYn == 'Y'}">
							<span class="ico_photo">포토샹품평</span>
						</c:if>
					</td>
					<td class="subject td_tit">
						<p><c:if test="${review.saleproductName != '없음'}">${review.saleproductName}</c:if></p>
						<c:if test="${review.categoryId != '0033' and review.categoryId != '0038'}">
							<p class="txt_ellipsis"><a href="#none">${review.title}</a></p>
						</c:if>
						
					</td>
					<td>
						<c:choose>
							<c:when test="${ memberNo ne review.memberNo }">
								${fn:substring(review.memberId,0,fn:length(review.memberId)-3)}***
							</c:when>
							<c:otherwise>
								${review.memberId }
							</c:otherwise>
						</c:choose>
					</td>
					<td class="date">
						<fmt:parseDate var="dateString" value="${review.insDt}" pattern="yyyy-MM-dd HH:mm:ss" />
						<fmt:formatDate value="${dateString}" pattern="yyyy/MM/dd" />
					</td>
				</tr>		
				<tr class="tr_cont review_cont tr_cont_hide">
					<td colspan="4">
						<c:if test="${not empty review.pmsReviewratings}">
							<div class="rating_list">
							<c:forEach items="${review.pmsReviewratings }" var="rr">
								<dl>
									<dt>${rr.ratingName }</dt>
									<dd>
										<span class="rating"><em style="width:${rr.rating * 20 }%;">${rr.rating} 점</em></span>
									</dd>
								</dl>
							</c:forEach>
							</div>
						</c:if>
						<c:if test="${review.categoryId != '0033' and review.categoryId != '0038'}">
							<div class="maxW">
								${fn:replace(review.detail, newLineChar, '<br/>')}
								<c:if test="${review.imgYn == 'Y'}">
									<c:if test="${not empty fn:trim(review.img1)}">
										<img src="${_IMAGE_DOMAIN_}${review.img1}" alt="" />
									</c:if>
								</c:if>
							</div>
						</c:if>
					</td>
				</tr>
	</c:forEach>
</c:when>
<c:otherwise>
	<tr>
		<td colspan="4" class="noData_tp2">
			<p class="tit">등록된 상품평이 없습니다.</p>
			<p class="explain">상품평을 작성하시면 매일포인트와 당근을 지급해드립니다.</p>
			<ul>
				<li>첫번째 상품평 작성 시 <span>매일포인트 1000P</span> 지급</li>
				<li>일반상품평 작성 시 <span>매일포인트 100P + 당근500개</span> 지급</li>
				<li>포토상품평 작성 시 <span>매일포인트 100P + 당근 1,000개</span> 지급</li>
			</ul>
		</td>
	</tr>
</c:otherwise>
</c:choose>
</tbody>
</table>
</div>
<c:if test="${!empty list}">
	<div class="paginateType1">
		<page:paging formId="searchForm" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
				total="${totalCount}" url="/pms/product/reviews/list/ajax" type="ajax" callback="reviewCallback"/>
	</div>
</c:if>

