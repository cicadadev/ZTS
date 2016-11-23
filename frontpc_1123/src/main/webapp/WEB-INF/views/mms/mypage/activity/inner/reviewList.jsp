<%--
	화면명 : 리뷰  - 내가 쓴 상품평 목록
	작성자 : roy
 --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<script>
$(function(){
	$(".mobile .myreview .more_review").off("click").on("click", function(){
		
		$(this).closest("li").toggleClass("open").siblings("li").removeClass("open");
		// 포토 상품평
		swiperCon('photoSwiper_review', '1'); // 포토상품평
	});
	
	$(".pc .myreview .more_review").on("click", function(){
		$(this).closest("li").toggleClass("open").siblings("li").removeClass("open");
	});
});
</script>

<c:choose>
	<c:when test="${empty search.isScroll}">
		<script type="text/javascript">
			
			$(document).ready(function(){
				$(window).bind("scroll", reviewListScrollListener);
				document.getElementById('myReview').innerText = "${listCount.cntAll}";
				
				document.getElementById('myReviewCntAll').innerText = "${listCount.cntAll}";
				document.getElementById('myReviewCntImgY').innerText = "${listCount.cntImgY}";
				document.getElementById('myReviewCntImgN').innerText = "${listCount.cntImgN}";
			});
		</script>
		
		<%-- <c:if test="${isMobile}"> 
			<script type="text/javascript" src="/resources/js/mo.js"></script>
		</c:if>
		<c:if test="${!isMobile}">
			<script type="text/javascript" src="/resources/js/pc.js"></script>
		</c:if>
		<script type="text/javascript" src="/resources/js/common/common.ui.js"></script> --%>

		<form id="reviewSearchForm">
			<input type="hidden" value="${search.imgYn}"  name="imgYn"/>
			<input type="hidden" value="${search.startDate}"  name="startDate"/>
			<input type="hidden" value="${search.endDate}"  name="endDate"/>
			<input type="hidden" value="${totalCount}"  name="reviewTotalCount"/>
		</form>
		
		<div>
			<ul class="div_tb_tbody3">
				<c:choose>
					<c:when test="${!empty list}">
						<c:forEach items="${list}" var="list" varStatus="index">
							<li>
								<div class="tr_box">
									<div class="col1">
										<span class="date">${list.orderDt}</span>
										<span>${list.orderId}</span>
									</div>
		
									 <!-- 16.11.11 -->
									<div class="col2">
										<div class="positionR">
											<div class="prod_img">
												<a href="javascript:ccs.link.product.detail('${list.productId}','');" >
													<img src="${_IMAGE_DOMAIN_}${list.productUrl}" alt="" />
												</a>
											</div>
		
											<a href="javascript:ccs.link.product.detail('${list.productId}','');" class="title">
												${list.productName}
											</a>
		
											<em class="option_txt">
												<i>${list.saleproductName}</i>
											</em>
										</div>
									</div>
		
									<div class="col3">
										<div class="more_review">
											<div class="bg_star">
												<span style="width:${list.rating *20}%;">
													<em>${list.rating *20}</em>
												</span>
											</div>
											<div class="more">
												<p>${list.title}</p>
												<c:if test="${list.imgYn == 'Y'}">
													<span class="icon_img">이미지 첨부</span>
												</c:if>
											</div>
										</div>
									</div>
									 <!-- //16.11.11 -->
		
									<fmt:parseDate value="${list.insDt}" var="dateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
									<fmt:formatDate value="${dateFmt}" var="reviewInsDt" pattern="yyyy-MM-dd"/>
									<div class="col4">
										<span class="date">${reviewInsDt}</span>
									</div>
								</div>
		
								<div class="reviewDetail">
									<c:forEach items="${list.pmsReviewratings}" var="rating">
										<dl>
											<dt>${rating.ratingName }</dt>
											<dd>
												<div class="bg_star">
													<span style="width:${rating.rating *20}%;">
														<em>${rating.rating }</em>
													</span>
												</div>
											</dd>
										</dl>
									</c:forEach>
									
									<div class="viewArea">
										${fn:replace(list.detail, newLineChar, '<br/>')}
										<c:if test="${list.imgYn == 'Y'}">
											<c:if test="${isMobile}">
												<div class="pop_content">
													<div class="swiper_wrap">
														<div class="swiper-container photoSwiper_review">
															<ul class="swiper-wrapper">
																<c:if test="${not empty fn:trim(list.img1)}">
																	<li class="swiper-slide">
																		<img src="${_IMAGE_DOMAIN_}${list.img1}" alt="" />
																	</li class="swiper-slide">
																</c:if>
																<c:if test="${not empty fn:trim(list.img2)}">
																	<li class="swiper-slide">
																		<img src="${_IMAGE_DOMAIN_}${list.img2}" alt="" />
																	</li class="swiper-slide">
																</c:if>
																<c:if test="${not empty fn:trim(list.img3)}">
																	<li class="swiper-slide">
																		<img src="${_IMAGE_DOMAIN_}${list.img3}" alt="" />
																	</li>
																</c:if>
															</ul>
															<!-- Add Pagination -->
															<div class="swiper-pagination tp1"></div>
														</div>
													</div>
												</div>
											</c:if>
											<c:if test="${!isMobile}">
												<div class="photoSwipe">
													<ul>
														<c:if test="${not empty fn:trim(list.img1)}">
															<li>
																<img src="${_IMAGE_DOMAIN_}${list.img1}" alt="" />
															</li>
														</c:if>
														<c:if test="${not empty fn:trim(list.img2)}">
															<li>
																<img src="${_IMAGE_DOMAIN_}${list.img2}" alt="" />
															</li>
														</c:if>
														<c:if test="${not empty fn:trim(list.img3)}">
															<li>
																<img src="${_IMAGE_DOMAIN_}${list.img3}" alt="" />
															</li>
														</c:if>
													</ul>
												</div> 
											</c:if>
										</c:if>
									</div>
		
									<div class="btns">
										<a href="javascript:mypage.review.updateReview('${list.reviewNo}', '${list.productId}');" class="btn_sStyle1 sWhite2">수정</a>
										<!-- <a href="#" class="btn_sStyle1 sWhite2">삭제</a> -->
									</div>
								</div>
							</li>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<li class="noData_tp1">
							작성한 상품평이 없습니다.
						</li>
					</c:otherwise>
				</c:choose>
			</ul>
			<!-- ### PC 페이징 ### -->
			<div class="paginateType1">
				<page:paging formId="reviewSearchForm" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
						total="${totalCount}" url="/mms/mypage/review/list/ajax" type="ajax" callback="mypage.review.listCallbackReview"/>
			</div>
			<!-- ### //PC 페이징 ### -->
		</div>
	</c:when>
	<c:otherwise>
		<c:forEach items="${list}" var="list" varStatus="index">
			<li>
				<div class="tr_box">
					<div class="col1">
						<span class="date">${list.orderDt}</span>
						<span>${list.orderId}</span>
					</div>

					 <!-- 16.11.11 -->
					<div class="col2">
						<div class="positionR">
							<div class="prod_img">
								<a href="javascript:ccs.link.product.detail('${list.productId}','');" >
									<img src="${_IMAGE_DOMAIN_}${list.productUrl}" alt="" />
								</a>
							</div>

							<a href="javascript:ccs.link.product.detail('${list.productId}','');" class="title">
								${list.productName}
							</a>

							<em class="option_txt">
								<i>${list.saleproductName}</i>
							</em>
						</div>
					</div>

					<div class="col3">
						<div class="more_review">
							<div class="bg_star">
								<span style="width:${list.rating *20}%;">
									<em>${list.rating *20}</em>
								</span>
							</div>
							<div class="more">
								<p>${list.title}</p>
								<c:if test="${list.imgYn == 'Y'}">
									<span class="icon_img">이미지 첨부</span>
								</c:if>
							</div>
						</div>
					</div>
					 <!-- //16.11.11 -->
					<fmt:parseDate value="${list.insDt}" var="dateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
					<fmt:formatDate value="${dateFmt}" var="reviewInsDt" pattern="yyyy-MM-dd"/>
					<div class="col4">
						<span class="date">${reviewInsDt}</span>
					</div>
				</div>

				<div class="reviewDetail">
					<c:forEach items="${list.pmsReviewratings}" var="rating">
						<dl>
							<dt>${rating.ratingName }</dt>
							<dd>
								<div class="bg_star">
									<span style="width:${rating.rating *20}%;">
										<em>${rating.rating }</em>
									</span>
								</div>
							</dd>
						</dl>
					</c:forEach>
					
					<div class="viewArea">
						${fn:replace(list.detail, newLineChar, '<br/>')}
						<c:if test="${list.imgYn == 'Y'}">
							<div class="photoSwipe">
								<ul>
									<c:if test="${not empty fn:trim(list.img1)}">
										<li>
											<img src="${_IMAGE_DOMAIN_}${list.img1}" alt="" />
										</li>
									</c:if>
									<c:if test="${not empty fn:trim(list.img2)}">
										<li>
											<img src="${_IMAGE_DOMAIN_}${list.img2}" alt="" />
										</li>
									</c:if>
									<c:if test="${not empty fn:trim(list.img3)}">
										<li>
											<img src="${_IMAGE_DOMAIN_}${list.img3}" alt="" />
										</li>
									</c:if>
								</ul>
		
								<ol class="dot_box">
									<c:if test="${not empty list.img1}">
										<li class="on">
										<button type="button">1</button>
									</li>
									</c:if>
									<c:if test="${not empty list.img2}">
										<li>
										<button type="button">2</button>
									</li>
									</c:if>
									<c:if test="${not empty list.img3}">
										<li>
										<button type="button">3</button>
									</li>
									</c:if>
								</ol>
							</div>
						</c:if>
					</div>

					<div class="btns">
						<a href="javascript:mypage.review.updateReview('${list.reviewNo}', '${list.productId}');" class="btn_sStyle1 sWhite2">수정</a>
						<!-- <a href="#" class="btn_sStyle1 sWhite2">삭제</a> -->
					</div>
				</div>
			</li>
		</c:forEach>
	</c:otherwise>
</c:choose>
	
