<%--
	화면명 : 마이페이지 > MY 정보관리 > 관심매장 설정
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<script>
$(document).ready(function() {
	$("[name=bfEdit]").show();
	$("[name=ingEdit]").hide();
});
</script>
<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY정보관리|관심매장" name="pageNavi"/>
</jsp:include>

<div class="inner">
	<div class="layout_type1">
		<div class="column">
			<div class="mypage mystore">
				<h3 class="title_type1">관심매장</h3>
		
				<div class="sortPkg">
					<ul class="sortBox">
						<li class="active">
							<a href="#none">
								<span>전체</span><em>(${totalCnt})</em>
							</a>
						</li>
					</ul>
					<a href="javascript:mypage.offshop.editOffshop();" class="btn_sStyle1 sWhite1 posR btnChange" name="bfEdit">편집</a>
					<a href="javascript:mypage.offshop.saveOffshop();" class="btn_sStyle1 sWhite1 posR btnChange" name="ingEdit">편집완료</a>		
				</div>
		
				<div class="storeList type1">
					<div class="list">
						<c:if test="${totalCnt <= 0}">
							<div style="text-align:center">
								<b>등록된 관심 매장이 없습니다.</b><br />
								매장찾기에서 추가하실 수 있습니다.
							</div>
						</c:if>
						<c:if test="${totalCnt > 0}">
							<ul class="default_store">
								<c:forEach items="${topOffshop}" var="offshop" varStatus="status">
									<li id="store_li${status.index}">
										<div class="storeAddr">
											<div class="storeInner">
												<div class="branch">
													<strong id="offshopName">${offshop.ccsOffshop.name}</strong>
													<span class="default icon_type2 iconPink3">대표매장</span>
													<p>
														<c:if test="${isMobile ne 'true'}">
															<a href="#none" class="btn_shop_tel">${offshop.ccsOffshop.offshopPhone}</a>
														</c:if>
														<span>${offshop.ccsOffshop.address1}</span>
													</p>
												</div>
				
												<div class="storeBrand">
													<span class="icon_type1 iconPurple2">판매브랜드</span>
													<ul>
														<c:forEach items="${offshop.ccsOffshop.ccsOffshopbrands}" var="brand" varStatus="status">
															<li>${brand.name}</li>
														</c:forEach>
													</ul>
												</div>
												
												<div class="box_01" ${isMobile eq 'true' ? 'style=\"height:25px\"' : ''}>
													<c:if test="${isMobile eq 'true' && not empty offshop.ccsOffshop.offshopPhone}">
														<a href="tel:${offshop.ccsOffshop.offshopPhone}" class="btn_shop_tel" style="width:80px;">${offshop.ccsOffshop.offshopPhone}</a>
													</c:if>
													<c:if test="${offshop.ccsOffshop.offshopPickupYn == 'Y'}">
														<c:choose>
															<c:when test="${isMobile}">
																<!-- 샵온 개발시에 주석 풀기로! -->
																<a href="/ccs/common/main#5" class="btn_sStyle1 sPurple1 btn_pick" style="margin:0 0 0 105px;">픽업가능 상품보기</a>
															</c:when>
															<c:otherwise>
																<a href="#none" class="btn_sStyle1 sPurple1 btn_pick" onclick="ccs.offshop.showPickupProduct('${offshop.ccsOffshop.offshopId}', '${offshop.ccsOffshop.name}');">픽업가능 상품보기</a>
															</c:otherwise>
														</c:choose>
													</c:if>
												</div>
				
												<div class="box_02">
													<a href="javascript:mypage.offshop.offshopInfo('${offshop.ccsOffshop.offshopId}');" class="btn_storeMap1 btn_shopPosition">매장위치</a>
													<button type="button" class="bookmark checked" onclick="mypage.offshop.deleteInterested('${offshop.ccsOffshop.offshopId}');">관심매장</button>
												</div>
											</div>
										</div>
										<c:if test="${not empty offshop.ccsOffshop.dmsExhibitoffshops}">
											<c:set var="exhibit" value="${offshop.ccsOffshop.dmsExhibitoffshops[0]}" />
											<div class="txt_alarm">
												<p>
													<a href="javascript:ccs.link.go('/dms/exhibit/detail?exhibitId=${exhibit.dmsExhibit.exhibitId}');">${exhibit.dmsExhibit.name}</a>
												</p>
											</div>
										</c:if>
									</li>
								</c:forEach>
							</ul>
							
							<ul class="favorite_store">
								<c:forEach items="${offshopList}" var="offshop" varStatus="status">
									<li id="store_li${status.index + 1}">
										<div class="storeAddr">
											<div class="storeInner">
												<div class="branch">
													<strong id="offshopName">${offshop.ccsOffshop.name}</strong>
													<p>
														<c:if test="${isMobile ne 'true'}">
															<a href="#none" class="btn_shop_tel">${offshop.ccsOffshop.offshopPhone}</a>
														</c:if>
														<span>${offshop.ccsOffshop.address1}</span>
													</p>
												</div>
				
												<div class="storeBrand">
													<span class="icon_type1 iconPurple2">판매브랜드</span>
													<ul>
														<c:forEach items="${offshop.ccsOffshop.ccsOffshopbrands}" var="brand" varStatus="status">
															<li>${brand.name}
														</c:forEach>
													</ul>
												</div>		
												
												<div class="box_01" ${isMobile eq 'true' ? 'style=\"height:25px\"' : ''}>
													<c:if test="${isMobile eq 'true' && not empty offshop.ccsOffshop.offshopPhone}">
														<a href="tel:${offshop.ccsOffshop.offshopPhone}" class="btn_shop_tel" style="width:80px;">${offshop.ccsOffshop.offshopPhone}</a>
													</c:if>
													<c:if test="${offshop.ccsOffshop.offshopPickupYn == 'Y'}">
														<c:choose>
															<c:when test="${isMobile}">
																<!-- 샵온 개발시에 주석 풀기로! -->
																<a href="/ccs/common/main#5" class="btn_sStyle1 sPurple1 btn_pick" style="margin:0 0 0 105px;">픽업가능 상품보기</a>
															</c:when>
															<c:otherwise>
																<a href="#none" class="btn_sStyle1 sPurple1 btn_pick" onclick="ccs.offshop.showPickupProduct('${offshop.ccsOffshop.offshopId}', '${offshop.ccsOffshop.name}');">픽업가능 상품보기</a>
															</c:otherwise>
														</c:choose>
													</c:if>
													<a href="javascript:mypage.offshop.setTopOffshop();" class="btn_sStyle1 sPink1 btn_default" name="ingEdit">대표매장설정</a>
													<div class="btn_sorting" name="ingEdit">
														<a href="#none" class="btn_up">위로</a>
														<a href="#none" class="btn_dw">아래로</a>
													</div>
												</div>
				
												<div class="box_02">
													<a href="javascript:mypage.offshop.offshopInfo('${offshop.ccsOffshop.offshopId}');" class="btn_storeMap1 btn_shopPosition">매장위치</a>
													<button type="button" class="bookmark checked" onclick="mypage.offshop.deleteInterested('${offshop.ccsOffshop.offshopId}')">관심매장</button>
												</div>
											</div>
										</div>
										<c:if test="${not empty offshop.ccsOffshop.dmsExhibitoffshops}">
											<c:set var="exhibit" value="${offshop.ccsOffshop.dmsExhibitoffshops[0]}" />
											<div class="txt_alarm">
												<p>
													<a href="javascript:ccs.link.go('/dms/exhibit/detail?exhibitId=${exhibit.dmsExhibit.exhibitId}');">${exhibit.dmsExhibit.name}</a>
												</p>
											</div>
										</c:if>
									</li>
								</c:forEach>
							</ul>				
						</c:if>
					</div>
				</div>
				<div class="btn_wrapC btn1ea">
					<a href="javascript:ccs.link.offshop();" class="btn_mStyle1 sPurple1">관심매장 추가</a>
				</div>
			</div>
		</div>
	
		<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
	</div>
</div>