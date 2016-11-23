<%--
	화면명 : 
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>


<script type="text/javascript">
$(document).ready(function() {
	ccs.offshop.setTotalCount();
});
</script>
								
<c:if test="${totalCount == 0}">
	<ul id="null_ul">
		<li>
			<div class="storeAddr"></div>
			<div class="storeMap no_result">
				<div class="txt">
					검색 결과가 없습니다.
				</div>
			</div>
		</li>
	</ul>		
</c:if>
<c:if test="${totalCount > 0}">
	<ul id="ul_offshop">
		<c:forEach items="${offshopList}" var="offshop" varStatus="status">
			<c:set var="index" value="${status.index}" />		
			<li id="li_${index}">
				<div class="storeAddr">
					<div class="storeInner">
						<div class="branch">
							<strong>${offshop.name}</strong>
							<p>${offshop.address1}</p>
						</div>
						
						<div class="storeBrand">
							<span class="icon_type1 iconPurple2">판매브랜드</span>
							<ul>
								<c:forEach items="${offshop.ccsOffshopbrands}" var="brand" varStatus="status">
									<li>${brand.name}</li>
								</c:forEach>
							</ul>
						</div>
						
						<div class="box_01">
							<a href="tel:${offshop.offshopPhone}" class='btn_shop_tel'>${offshop.offshopPhone}</a>
							<c:if test="${offshop.offshopPickupYn == 'Y'}">
								<c:choose>
									<c:when test="${isMobile}">
									
										<a href="/ccs/common/main?crtIndex=6" class="btn_pickup"><span>픽업상품 보기</span></a>
										
									</c:when>
									<c:otherwise>
										<a href="#none" class="btn_pickup" onclick="ccs.offshop.showPickupProduct('${offshop.offshopId}', '${offshop.name}');"><span>픽업상품 보기</span></a>
									</c:otherwise>
								</c:choose>
							</c:if>
							<a href="#none" class="btn_detail" onclick="ccs.offshop.getOffshopDetail('${index}', '${isMobile}')">상세보기</a>
						</div>
						
						<div class="box_02">
							<c:if test="${not empty offshop.distance}">
								<div class="km">${offshop.distance}km</div>
							</c:if>	
								
							<c:choose>
								<c:when test="${offshop.interestYn == 'Y'}">
									<button type="button" class="bookmark checked" id="interestBtn${index}" onclick="ccs.offshop.saveInterestOffshop(this, '${offshop.offshopId}');">관심매장</button>
								</c:when>
								<c:otherwise>
									<button type="button" class="bookmark" id="interestBtn${index}" onclick="ccs.offshop.saveInterestOffshop(this, '${offshop.offshopId}');">관심매장</button>
								</c:otherwise>
							</c:choose>
						</div>					
					</div>
				</div>
				
				<div id="detailDiv${index}" class="storeMap" style="display:none">
					<div class="info">
						<div class="branch">
							<strong>${offshop.name}</strong>
							<p>${offshop.address1}</p>
						</div>
						
						<p>
							<span>${offshop.holidayInfo}</span>
						</p>
						<p class="txt">* 매장에서 진행하는 행사를 PUSH 알림으로 보내드립니다.</p>
						<c:choose>
							<c:when test="${isMobile}">
								<a href="tel:${offshop.offshopPhone}" class="btn_shop_tel">${offshop.offshopPhone}</a>
							</c:when>
							<c:otherwise>
								<a href="#none" class="btn_shop_tel">${offshop.offshopPhone}</a>
							</c:otherwise>
						</c:choose>
					</div>
					
					<div class="map">
						<div id="offshopMap${index}" class="mapArea"><script>ccs.offshop.roadMap("offshopMap${index}", "${offshop.latitude}", "${offshop.longitude}")</script></div>				
					</div>
				</div>							
			</li>
		</c:forEach>
	</ul>	
</c:if>

<input type="hidden" name="hidOffshopTotalCnt" id="hidOffshopTotalCnt" value="${totalCount}" />

	
<style>
.pc .storeList .list>ul>li .storeMap .map .mapArea {width:601px; height:627px; overflow:hidden;}
.pc .storeList .list>ul>li .storeMap .map .mapComment {display:none}
.pc .storeList .list>ul>li .storeAddr .storeBrand>ul>li {color:#2d9bd6;}

.mobile .storeList .list>ul>li .storeMap .map .mapArea {width:400px; height:320px; overflow:hidden;}

#null_ul {border-bottom:1px solid #fff;}
#null_ul>li> .storeAddr {border-left:1px solid #fff; border-right:1px solid #fff;}
</style>		