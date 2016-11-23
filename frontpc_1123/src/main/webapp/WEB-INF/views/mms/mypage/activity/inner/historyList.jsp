<%--
	화면명 : 마이페이지 > 나의활동 > 히스토리 list
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<c:choose>
	<c:when test="${!empty moHistory }">
		<ul>
			<c:forEach items="${moHistory }" var="history" varStatus="status">
				<li>
					<div class="tr_box">
						<div class="col1 timeline">
							<i>timeline</i>
						</div>
						<div class="col2">
							<c:choose>
								<c:when test="${history.type eq 'PRODUCT' }">
									<a href="javascript:void(0);" onclick="javascript:ccs.link.product.detail('${history.id}')">
										<div class="img">
											<tags:prdImgTag productId="${history.id}" size="90" alt="" />
										</div>
									
										<div class="info">
											<span class="title">
												${history.name }
											</span>
											<div class="etc">
												<span class="price"><fmt:formatNumber value="${history.price}" pattern="###,###" /><i>원</i></span>
											</div>
										</div>
									</a>
								</c:when>
								<c:otherwise>
									<c:if test="${history.type eq 'EVENT' }">
										<a href="javascript:ccs.link.go('/sps/event/detail?eventId=${history.id}', CONST.NO_SSL);" >
											<span class="event">[이벤트]</span>
											${history.name }
										</a>
									</c:if>
									<c:if test="${history.type eq 'EXHIBIT' }">
										<a href="javascript:ccs.link.go('/dms/exhibit/detail?exhibitId=${history.id}', CONST.NO_SSL);" >
											<span class="promo">[기획전]</span>
											${history.name }
										</a>
									</c:if>
									<c:if test="${history.type eq 'SEARCH' }">
										<a href="javascript:void(0);" onclick="javascript:dms.common.setKeyWord('${history.id}');">
											<span class="srch">[검색]</span>
											${history.id }
										</a>
									</c:if>
									<c:if test="${history.type eq 'BRAND' }">
										<a href="javascript:void(0);" onclick="javascript:ccs.link.go('/dms/common/templateDisplay?brandId=${history.id}', CONST.NO_SSL);">
											<span class="brand">[브랜드]</span>
											${history.name }
										</a>
									</c:if>
									<c:if test="${history.type eq 'CATEGORY' }">
										<a href="javascript:void(0);" onclick="javascript:ccs.link.go('/dms/common/templateDisplay?dispCategoryId=${history.id}', CONST.NO_SSL);">
											<span class="category">[카테고리]</span>
											${history.name }
										</a>
									</c:if>
								</c:otherwise>
							</c:choose>
							<c:if test="${history.type eq 'EXHIBIT' or history.type eq 'EVENT' }">
								<span class="date">${history.date }</span>
							</c:if>
						</div>
						<div class="col3">
							<a href="javascript:void(0);" class="btnDelete" onclick="javascript:mypage.history.deleteHistory('${history.id}');">삭제</a>
						</div>
					</div>
				</li>			
			</c:forEach>
		</ul>
	</c:when>
	<c:otherwise>
		<p class="empty">최근 히스토리 내역이 없습니다.</p>
	</c:otherwise>
</c:choose>