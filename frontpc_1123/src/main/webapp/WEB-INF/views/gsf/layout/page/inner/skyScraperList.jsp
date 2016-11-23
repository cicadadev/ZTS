<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="gcp.mms.model.custom.FoLoginInfo" %>
<%@ page import="gcp.common.util.FoSessionUtil" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%
	FoLoginInfo loginInfo = (FoLoginInfo)FoSessionUtil.getLoginInfo(); 
	if (loginInfo != null) {
		pageContext.setAttribute("isLogin", true);	
	} else {
		pageContext.setAttribute("isLogin", false);	
	}
%>

	<li>
		<a href="#none" class="btn_show" id="latest">최근본상품</a>
		<c:choose>
			<c:when test="${!empty latestProductList }">
				<div class="showroom" id="latestPrShow">
					<ul>
						<c:forEach items="${latestProductList }" var="item" varStatus="status">
							<li class="${status.index < 2 ? 'on' : '' }">
								<a href="#none" onclick="javascript:ccs.link.product.detail('${item.productId}')">
									<tags:prdImgTag productId="${item.productId}" size="60" alt="${status.index}번 상품" />
									<span>
									<c:choose>
										<c:when test="${fn:length(item.name) >37 }">
											<u>${fn:substring(item.name,0,37) }...</u>
										</c:when>
										<c:otherwise>
											<u>${item.name }</u>
										</c:otherwise>
									</c:choose>
										<br/><fmt:formatNumber value="${item.salePrice}" pattern="###,###" /> 원
									</span>
								</a>
							</li>
						</c:forEach>
					</ul>
					<div class="control">
						<button type="button" class="btn_prev">이전</button>
						<span id="latestPrSize">
							<i>1</i> / <em><fmt:formatNumber value="${(latestProductListSize/2)+(1-((latestProductListSize/2)%1))%1}" type="number" pattern="#"/></em>
						</span>
						<button type="button" class="btn_next">다음</button>
					</div>
				</div>
			</c:when>
		</c:choose>
	</li>
	<li>
		<a href="#none" class="btn_show" id="wish" onclick="ccs.link.skyScraper.isLogin(${isLogin})">찜한상품</a>
		<c:choose>
			<c:when test="${!empty wishList }">
				<div class="showroom" id="wiShow">
					<ul>
						<c:forEach items="${wishList }" var="item" varStatus="status">
							<li class="${status.index < 2 ? 'on' : '' }">
								<a href="#none" onclick="javascript:ccs.link.product.detail('${item.productId}')">
									<tags:prdImgTag productId="${item.productId}" size="60" alt="${status.index}번 상품" />
									<span>
									<c:choose>
										<c:when test="${fn:length(item.name) >37 }">
											<u>${fn:substring(item.name,0,37) }...</u>
										</c:when>
										<c:otherwise>
											<u>${item.name }</u>
										</c:otherwise>
									</c:choose>
										<br/><fmt:formatNumber value="${item.salePrice}" pattern="###,###" /> 원
									</span>
								</a>
							</li>
						</c:forEach>
					</ul>
					<div class="control">
						<button type="button" class="btn_prev">이전</button>
						<span id="latestPrSize">
							<i>1</i> / <em><fmt:formatNumber value="${(wishCnt/2)+(1-((wishCnt/2)%1))%1}" type="number" pattern="#"/></em>
						</span>
						<button type="button" class="btn_next">다음</button>
					</div>
				</div>
			</c:when>
		</c:choose>
	</li>