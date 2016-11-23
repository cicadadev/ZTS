<%--
	화면명 : 쇼킹제로 > 메인
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<script type="text/javascript" src="/resources/js/jquery.countdown.min.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	sps.deal.shockingzero.resorting('popular');
});
</script>

<c:choose>
<c:when test="${isMobile ne 'true' }">
	<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
		<jsp:param value="쇼킹제로" name="pageNavi"/>
	</jsp:include>	
</c:when>
<c:otherwise>	
	<jsp:include page="/WEB-INF/views/gsf/layout/page/mo/title_menu.jsp" flush="false" >
		<jsp:param name="titleMenu" value="2" />
	</jsp:include>
</c:otherwise>	
</c:choose>
	<div class="inner">
		<div class="main_zero">
			<h2 class="title_zero">
				매일 오전 10시! 쇼킹한 가격으로 만나요
			</h2>
			
			<!-- ### 쇼킹제로 비주얼 ### -->
			<c:if test="${not empty shockingMainProducts}">
				<div class="zero_visual">
					<ul class="view">
						<c:forEach items="${shockingMainProducts}" var="mainProduct" varStatus="status">
							<c:choose>
								<c:when test="${status.index == 0}">
									<li class="on">
								</c:when>
								<c:otherwise>
									<li>
								</c:otherwise>
							</c:choose>
							
								<div class="imgBox">
									<a href="#none" onclick="ccs.link.product.detail('${mainProduct.pmsProduct.productId}');">
										<tags:prdImgTag productId="${mainProduct.pmsProduct.productId}" seq="0" size="360" />
										<c:choose>
											<c:when test="${mainProduct.badgeGubun == 1}">
												<span>마감임박</span>
											</c:when>
											<c:when test="${mainProduct.badgeGubun == 2}">
												<span>오늘오픈</span>
											</c:when>
											<c:when test="${mainProduct.badgeGubun == 3}">
												<span>품절임박</span>
											</c:when>
										</c:choose>
									</a>
								</div>
								
								<span class="time">
									<small>남은시간</small>
									<em><div id="mainProduct${status.index}"><script>sps.deal.shockingzero.countdown("mainProduct${status.index}", "${mainProduct.endDt}")</script></div></em>
								</span>
								
								<dl>
									<dt>
										<a href="#none" onclick="ccs.link.product.detail('${mainProduct.pmsProduct.productId}');">
											${mainProduct.pmsProduct.name}
										</a>
									</dt>
									<dd>
										<c:if test="${mainProduct.dealPriceRate > 0}">
											<span class="sale">${mainProduct.dealPriceRate}<i>%</i></span>
										</c:if>
										
										<div class="priceBox">
											<c:if test="${mainProduct.listPrice > mainProduct.salePrice}">
												<span class="ori"><fmt:formatNumber type="currency" value="${mainProduct.listPrice}" pattern="###,###" />원</span>
											</c:if>
											<span class="price"><fmt:formatNumber type="currency" value="${mainProduct.salePrice}" pattern="###,###" /><i>원</i></span>
										</div>
	
										<ul>
											<li>
												남은수량 <em><fmt:formatNumber type="currency" value="${mainProduct.dealStockQty}" pattern="###,###" /></em>개
											</li>
										</ul>
									</dd>
								</dl>
							
							</li>
						</c:forEach>
					</ul>
					
					<ul class="thumb">
						<c:forEach items="${shockingMainProducts}" var="mainProduct" varStatus="status">
							<c:choose>
								<c:when test="${status.index == 0}">
									<li class="on">
								</c:when>
								<c:otherwise>
									<li>
								</c:otherwise>
							</c:choose>
							
								<a href="#none">
									<div class="thumb_img">
										<tags:prdImgTag productId="${mainProduct.pmsProduct.productId}" seq="0" size="72" />
									</div>
																	
									<div class="info">
										<span class="name">
											${mainProduct.pmsProduct.name}
										</span>
										<span class="price"><fmt:formatNumber type="currency" value="${mainProduct.salePrice}" pattern="###,###" />원</span>
									</div>
								</a>
							
							</li>
						</c:forEach>
					</ul>
				</div>			
			</c:if>
			<!-- ### //쇼킹제로 비주얼 ### -->
			
			<c:if test="${!isMobile}">
				<ul class="sortBox1 sortBox1_3ea" id="sortBox">
				
					<li id="popular_li" class="active">
						<a href="#none" onclick="sps.deal.shockingzero.resorting('popular');">
							<span>인기상품순</span>
						</a>
					</li>
					<li id="new_li">
						<a href="#none" onclick="sps.deal.shockingzero.resorting('new');">
							<span>신규오픈순</span>
						</a>
					</li>
					<li id="end_li">
						<a href="#none" onclick="sps.deal.shockingzero.resorting('end');">
							<span>종료임박순</span>
						</a>
					</li>
				</ul>	
			</c:if>			
			<c:if test="${isMobile}">
				<ul class="sortBox1 sortBox1_2ea">
					<li style="width:40%;">
						<h2>진행상품 <span id="txtTotalCnt">0개</span></h2>						
					</li>
					<li style="width:60%;">
						<div class="select_box1">
							<label>인기상품순</label>
							<select id="sortSelect" onchange="sps.deal.shockingzero.sortChange();">
								<option value="popular">인기상품순</option>
								<option value="new">신규오픈순</option>
								<option value="end">종료임박순</option>
							</select>
						</div>
					</li>
				</ul>
			</c:if>
			
			<div class="product_type1 prodType_3ea">
				<ul id="shockingProductArea">
						
				</ul>
			</div>
									
		</div>
	</div>

<style>
.pc .zero_visual .view .imgBox {height:360px;}
.pc .zero_visual .thumb li a .thumb_img {margin-right:7px; vertical-align:middle; display:inline-block; width:72px; height:72px;}
.mobile .sortBox1_2ea {height:50px;}
.mobile .sortBox1_2ea h2 {text-align:left; margin:15px 0 0 5px;}
.mobile .sortBox1_2ea .select_box1 {width:145px; margin:5px 5px 5px 0; float:right;}
</style>