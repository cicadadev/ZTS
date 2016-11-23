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

<c:choose>
	<c:when test="${empty search.isScroll}">
		<script>
		 	$(document).ready(function(){
				var totalCount = '${listCount.cntAll}';
				$('#reviewTotalCount1').html('('+totalCount+')');
				$('#reviewTotalCount2').html('('+totalCount+')');
				
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
			<input type="hidden" value="${totalCount}"  	name="reviewTotalCount"/>
		</form>
		<c:choose>
		<c:when test="${not empty list}">
			<c:forEach items="${list}" var="review" varStatus="index">	
				<li>
					<div class="user_option">
						<div class="item_star bg_star">
							<span style="width:${review.rating * 20 }%;">
								<em>${review.rating * 20 }</em>
							</span>
						</div>
				
						<div class="user">
							<span>	
								<c:choose>
									<c:when test="${ memberNo ne review.memberNo }">
										${fn:substring(review.memberId,0,fn:length(review.memberId)-3)}***
									</c:when>
									<c:otherwise>
										${review.memberId }
									</c:otherwise>
								</c:choose>
							</span>
							<em>
								<fmt:parseDate var="dateString" value="${review.insDt}" pattern="yyyy-MM-dd HH:mm:ss" />
								<fmt:formatDate value="${dateString}" pattern="yyyy/MM/dd" />			
							</em>
						</div>
				
						<strong class="title qArea">
							 <!-- 2016.11.04 ���� ���� -->
							 <c:if test="${review.saleproductName != '����' and not empty review.saleproductName}"><p class="optionName">[�ɼǼ���] ${review.saleproductName}</p></c:if>
							 <c:if test="${review.permitYn == 'Y'}">
							 	<span class="tester">ü���</span>
							 </c:if>
							
							<a href="#none">${review.title}</a> 
							<c:if test="${review.imgYn == 'Y'}">
								<span class="icon_img">�̹��� ÷��</span>
							</c:if>
						</strong>
					</div>
				
					<div class="user_txt">
				<c:choose>
					<c:when test="${!empty review.pmsReviewratings}">
						<c:forEach items="${review.pmsReviewratings }" var="rr">	
								<dl>
									<dt>${rr.ratingName }</dt>
									<dd>
										<div class="item_star bg_star1">
											<span style="width:${rr.rating * 20 }%;">
												<em>${rr.rating * 20 }</em>
											</span>
										</div>
									</dd>
								</dl>
						</c:forEach>
						<p>
							${review.detail }
						</p>
					</c:when>
					<c:otherwise>
						<p style="margin-top:0px;padding-top:0px">
							${review.detail }
						</p>
					</c:otherwise>
				</c:choose>
				</li>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<li class="noData_tp2">
				<p class="tit">��ϵ� ��ǰ���� �����ϴ�.</p>
				<p class="explain">��ǰ���� �ۼ��Ͻø� ��������Ʈ�� ����� �����ص帳�ϴ�.</p>
				<ul class="">
					<li>ù��° ��ǰ�� �ۼ� �� <span>��������Ʈ 1000P</span> ����</li>
					<li>�Ϲݻ�ǰ�� �ۼ� �� <span>��������Ʈ 100P + ���500��</span> ����</li>
					<li>�����ǰ�� �ۼ� �� <span>��������Ʈ 100P + ��� 1,000��</span> ����</li>
				</ul>
			</li>
		</c:otherwise>
		</c:choose>
	</c:when>
	<c:otherwise>
		<c:forEach items="${list}" var="review" varStatus="index">	
			<li>
				<div class="user_option">
					<div class="item_star bg_star">
						<span style="width:${review.rating * 20 }%;">
							<em>${review.rating * 20 }</em>
						</span>
					</div>
			
					<div class="user">
						<span>	
							<c:choose>
								<c:when test="${ memberNo ne review.memberNo }">
									${fn:substring(review.memberId,0,fn:length(review.memberId)-3)}***
								</c:when>
								<c:otherwise>
									${review.memberId }
								</c:otherwise>
							</c:choose>
						</span>
						<em>
							<fmt:parseDate var="dateString" value="${review.insDt}" pattern="yyyy-MM-dd HH:mm:ss" />
							<fmt:formatDate value="${dateString}" pattern="yyyy/MM/dd" />			
						</em>
					</div>
			
					<strong class="title qArea">
						<a href="#none">${review.title}</a>
					</strong>
				</div>
			
				<div class="user_txt">
			<c:choose>
				<c:when test="${!empty review.pmsReviewratings}">
					<c:forEach items="${review.pmsReviewratings }" var="rr">	
							<dl>
								<dt>${rr.ratingName }</dt>
								<dd>
									<div class="item_star bg_star1">
										<span style="width:${rr.rating * 20 }%;">
											<em>${rr.rating * 20 }</em>
										</span>
									</div>
								</dd>
							</dl>
					</c:forEach>
					<p>
						${review.detail }
					</p>
					<c:if test="${review.imgYn == 'Y'}">
						<span class="ico_photo">�̹��� ÷��</span>
					</c:if>
				</c:when>
				<c:otherwise>
					<p style="margin-top:0px;padding-top:0px">
						${review.detail }
					</p>
				</c:otherwise>
			</c:choose>
			</li>
		</c:forEach>
	</c:otherwise>
</c:choose>
