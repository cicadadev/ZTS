<%--
	화면명 : 
	작성자 : ALLEN
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<script type="text/javascript" src="/resources/js/jquery.countdown.min.js"></script>
<script>
function countdown(divName, endDt) {
	$("#"+divName).countdown(endDt, function(event) {
		$(this).text(
	      event.strftime('%D일 %H:%M:%S')
	    );
	});
}


</script>
<form name="exhibitListForm">
	<input type="hidden" name="totalCount" value="${totalCount}"/>
	<input type="hidden" name="currentPage" value="${search.currentPage}"/>
</form>
<c:if test="${!isMobile}">
	<ul class="exhibitList_ul">
</c:if>
						
						
	<c:forEach var="exhibit" items="${exhibitList}" varStatus="i">
		<c:choose>
			<c:when test="${exhibit.exhibitTypeCd eq 'EXHIBIT_TYPE_CD.COUPON'}">
				<li>
					<a href="/dms/exhibit/detail?exhibitId=${exhibit.exhibitId}">
						<span class="tag">
							<img src="/resources/img/pc/ico/tag_coupon.png" alt="coupon" />
						</span>
						<img src="${_IMAGE_DOMAIN_}${exhibit.img1}" alt=""/>
						
						<div class="info">
							<div class="tit">${exhibit.name}</div>
							<div class="dsc"> 
								<c:choose>
									<c:when test="${not empty exhibit.subtitle}">
										${exhibit.subtitle}
									</c:when>
									<c:otherwise>
										<c:forEach var="coupon" items="${exhibit.dmsExhibitcoupons}">
											<c:if test="${coupon.spsCoupon.couponTypeCd eq 'COUPON_TYPE_CD.DELIVERY'}">
												coupon<strong>무료배송</strong>
											</c:if>
											<c:if test="${coupon.spsCoupon.couponTypeCd ne 'COUPON_TYPE_CD.DELIVERY'}">
												<c:choose>
													<c:when test="${coupon.spsCoupon.dcApplyTypeCd eq 'DC_APPLY_TYPE_CD.AMT'}">
														coupon<strong>${coupon.spsCoupon.dcValue}</strong>원
													</c:when>
													<c:when test="${coupon.spsCoupon.dcApplyTypeCd eq 'DC_APPLY_TYPE_CD.RATE'}">
														coupon<strong>${coupon.spsCoupon.dcValue}</strong>%
													</c:when>
												</c:choose>
											</c:if>
										</c:forEach>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</a>
				</li>
			</c:when>
			
			<c:when test="${exhibit.exhibitTypeCd eq 'EXHIBIT_TYPE_CD.TIMESALE'}">
				<li>
					<a href="/dms/exhibit/detail?exhibitId=${exhibit.exhibitId}">
						<span class="tag">
						</span>
							<img src="${_IMAGE_DOMAIN_}${exhibit.img1}" alt=""/>
						<div class="info">
							<div class="tit">${exhibit.name}</div>
								<c:choose>
									<c:when test="${not empty exhibit.subtitle}">
									<div class="dsc">
										${exhibit.subtitle}
									</div>
									</c:when>
									<c:otherwise>
									<div class="dsc emph">
										<span>남은시간</span> 
										<em id="timesale_${i.index}"><script>countdown("timesale_${i.index}", "${exhibit.endDt}")</script></em>
									</div>
									</c:otherwise>
								</c:choose>
						</div>
					</a>
				</li>
			</c:when>
			
			<c:otherwise>
				<li>
					<a href="/dms/exhibit/detail?exhibitId=${exhibit.exhibitId}">
						<img src="${_IMAGE_DOMAIN_}${exhibit.img1}" alt=""/>
			
						<div class="info">
							<div class="tit">${exhibit.name}</div>
							<div class="dsc">${exhibit.subtitle}</div>
						</div>
					</a>
				</li>
			</c:otherwise>
		</c:choose>
	</c:forEach>
	<c:if test="${empty exhibitList}">
		<p class="empty">기획전 목록이 없습니다.</p>
	</c:if>
<c:if test="${!isMobile}">
	</ul>
</c:if>
<div class="paginateType1">
	<page:paging formId="exhibitForm" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
			total="${totalCount}" url="/dms/exhibit/list/ajax" type="ajax" callback="exhibit.main.listCallback"/>
</div>
