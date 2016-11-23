<%--
	ȭ��� : ��ǰ�� - ��ǰ����
	�ۼ��� : eddie
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
		$('#reviewTotalCount2').html('('+totalCount+'��)');
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
				��ü ��ǰ�� ����Դϴ�. ����, ����, �����, ����� ���� ������ ���� �˴ϴ�.
			</caption>
			<colgroup>
				<col style="width:15%;">
				<col style="width:*">
				<col style="width:10%;">
				<col style="width:10%;">
			</colgroup>
			<thead>
				<tr><th scope="col">����</th>
				<th scope="col">����</th>
				<th scope="col">�����</th>
				<th scope="col">�����</th>
			</tr></thead>
			<tbody>
<c:choose>
<c:when test="${!empty list}">
<c:forEach items="${list}" var="review" varStatus="index">	
				<tr>
					<td class="td_rating">
						<span class="rating"><em style="width:${review.rating * 20 }%;">${review.rating }��</em></span>
						<c:if test="${review.imgYn == 'Y'}">
							<span class="ico_photo">���伧ǰ��</span>
						</c:if>
					</td>
					<td class="subject td_tit">
						<p><c:if test="${review.saleproductName != '����'}">${review.saleproductName}</c:if></p>
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
										<span class="rating"><em style="width:${rr.rating * 20 }%;">${rr.rating} ��</em></span>
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
			<p class="tit">��ϵ� ��ǰ���� �����ϴ�.</p>
			<p class="explain">��ǰ���� �ۼ��Ͻø� ��������Ʈ�� ����� �����ص帳�ϴ�.</p>
			<ul>
				<li>ù��° ��ǰ�� �ۼ� �� <span>��������Ʈ 1000P</span> ����</li>
				<li>�Ϲݻ�ǰ�� �ۼ� �� <span>��������Ʈ 100P + ���500��</span> ����</li>
				<li>�����ǰ�� �ۼ� �� <span>��������Ʈ 100P + ��� 1,000��</span> ����</li>
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

