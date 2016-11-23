<%--
	화면명 : 프론트 & 모바일 다자녀우대관
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<script type="text/javascript">
$(document).ready(function(){
	
});
</script>

<c:set value="" var="benefitUrl" />
<tags:codeList code="CHILDRENCARD_TYPE_CD" var="childrendCard" tagYn="N"/>
<c:forEach var="card" items="${childrendCard}"> 
	<c:if test="${card.cd eq dealInfo.childrencardTypeCd }">
		<c:set value="${card.note}" var="benefitUrl" />	
	</c:if>
</c:forEach>


<c:choose>
	<c:when test="${dealInfo.childrencardTypeCd eq 'CHILDRENCARD_TYPE_CD.GYEONGGI'}">
		<c:set value="경기도" var="areaName" />
	</c:when>
	<c:when test="${dealInfo.childrencardTypeCd eq 'CHILDRENCARD_TYPE_CD.DAEJEON'}">
		<c:set value="대전" var="areaName" />
	</c:when>
	<c:when test="${dealInfo.childrencardTypeCd eq 'CHILDRENCARD_TYPE_CD.CHUNGNAM'}">
		<c:set value="충남" var="areaName" />
	</c:when>
	<c:when test="${dealInfo.childrencardTypeCd eq 'CHILDRENCARD_TYPE_CD.GWANGJU'}">
		<c:set value="광주" var="areaName" />
	</c:when>
	<c:when test="${dealInfo.childrencardTypeCd eq 'CHILDRENCARD_TYPE_CD.INCHEON'}">
		<c:set value="인천" var="areaName" />
	</c:when>
	<c:when test="${dealInfo.childrencardTypeCd eq 'CHILDRENCARD_TYPE_CD.GANGWON'}">
		<c:set value="강원" var="areaName" />
	</c:when>
	<c:when test="${dealInfo.childrencardTypeCd eq 'CHILDRENCARD_TYPE_CD.GYEONGNAM'}">
		<c:set value="경남" var="areaName" />
	</c:when>
	<c:when test="${dealInfo.childrencardTypeCd eq 'CHILDRENCARD_TYPE_CD.BUSAN'}">
		<c:set value="부산" var="areaName" />
	</c:when>
	<c:when test="${dealInfo.childrencardTypeCd eq 'CHILDRENCARD_TYPE_CD.CHUNGBUK'}">
		<c:set value="충북" var="areaName" />
	</c:when>
</c:choose>



	<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
		<jsp:param value="전문관|다자녀우대관" name="pageNavi"/>
	</jsp:include>
	<div class="multichild_main_top_wrap">
		<div class="multichild_main_top type2">
			<div class="mcTopArea">
				<img src="/resources/img/pc/bg/bg_multichild_main_top_02.jpg" alt="다자녀 우대관 아이가 행복한 세상, 매일유업과 제로투세븐이 경기 i PLUS 카드와 함께 만들어 갑니다." class="pc_only" /> <!-- 16.10.27 : 추가 -->
				<dl class="pc_only mct_info">
					<dt>${areaName} 다자녀 가정에 드리는 <br>특별한 혜택!</dt>
					<dd>
						매일유업과 제로투세븐이 함께합니다.
					</dd>
				</dl>
			</div>
			
			<div class="multichild_card">
				<div class="card">
					<div class="image">
						<c:choose>
							<c:when test="${dealInfo.childrencardTypeCd eq 'CHILDRENCARD_TYPE_CD.GYEONGGI'}">
								<img src="/resources/img/pc/temp/card_img_01.gif" alt="${dealInfo.childrenCardName} 이미지">
							</c:when>
							<c:when test="${dealInfo.childrencardTypeCd eq 'CHILDRENCARD_TYPE_CD.DAEJEON'}">
								<img src="/resources/img/pc/temp/card_img_02.gif" alt="${dealInfo.childrenCardName} 이미지">
							</c:when>
							<c:when test="${dealInfo.childrencardTypeCd eq 'CHILDRENCARD_TYPE_CD.CHUNGNAM'}">
								<img src="/resources/img/pc/temp/card_img_03.gif" alt="${dealInfo.childrenCardName} 이미지">
							</c:when>
							<c:when test="${dealInfo.childrencardTypeCd eq 'CHILDRENCARD_TYPE_CD.GWANGJU'}">
								<img src="/resources/img/pc/temp/card_img_04.gif" alt="${dealInfo.childrenCardName} 이미지">
							</c:when>
							<c:when test="${dealInfo.childrencardTypeCd eq 'CHILDRENCARD_TYPE_CD.INCHEON'}">
								<img src="/resources/img/pc/temp/card_img_05.gif" alt="${dealInfo.childrenCardName} 이미지">
							</c:when>
							<c:when test="${dealInfo.childrencardTypeCd eq 'CHILDRENCARD_TYPE_CD.GANGWON'}">
								<img src="/resources/img/pc/temp/card_img_06.gif" alt="${dealInfo.childrenCardName} 이미지">
							</c:when>
							<c:when test="${dealInfo.childrencardTypeCd eq 'CHILDRENCARD_TYPE_CD.GYEONGNAM'}">
								<img src="/resources/img/pc/temp/card_img_07.gif" alt="${dealInfo.childrenCardName} 이미지">
							</c:when>
							<c:when test="${dealInfo.childrencardTypeCd eq 'CHILDRENCARD_TYPE_CD.BUSAN'}">
								<img src="/resources/img/pc/temp/card_img_08.gif" alt="${dealInfo.childrenCardName} 이미지">
							</c:when>
							<c:when test="${dealInfo.childrencardTypeCd eq 'CHILDRENCARD_TYPE_CD.CHUNGBUK'}">
								<img src="/resources/img/pc/temp/card_img_09.gif" alt="${dealInfo.childrenCardName} 이미지">
							</c:when>
						</c:choose>
					</div>
					<div class="info">
						<strong class="mo_only">${dealInfo.childrenCardName}</strong>
						<p class="mo_only">${areaName} 다자녀 가정에 드리는 <br>특별한 혜택! <br>매일유업과 제로투세븐이 함께합니다.</p> <!-- 16.10.27 : br 삭제 -->
						<a href="${benefitUrl}" target="_blank" class="pc_only">카드혜택 자세히 보기 &gt;</a>
					</div>
				</div>
				<div class="bene_info">
					<!-- add : 20161028 begin -->
					<strong class="bene_info_tit01">${dealInfo.childrenCardName} 혜택 안내</strong>
					<strong class="bene_info_tit02">매일분유 할인혜택</strong>
					<!-- // add : 20161028 end -->
					<ul>
						
						<li>매일분유 15%할인 (소비지가 대비 할인 적용)</li>
						<li>매월 1회 6캔까지만 구매가능</li>
						<li>매일유업 앱솔루트 명작, 궁 6캔 제품에 한해 할인제공</li>
						<li>본 상품은 ${dealInfo.childrenCardName} 로만 결제 가능합니다.</li>
					</ul>
					<!-- //16.10.27 : strong 태그 삭제 및 텍스트 수정 -->
				</div>
			</div>
		</div>
	</div>
	<div class="inner">
		<div class="multichild_box">
			
			<!-- 16.10.27 : div(list_group) 추가 -->
			<c:forEach var="depth1" items="${depthList}" varStatus="i">
				<div class="list_group">
			
					<h4 class="tit_style1">${depth1.name}</h4> <!-- 16.10.27 : class 수정 -->
				
					<c:forEach var="depth2" items="${depth1.spsDealgroups}" varStatus="k">
						<div class="product_type1 prodType_4ea">
<%-- 							<c:if test="${not empty depth2.name}"> --%>
<%-- 								<h4>${depth2.name}</h4> --%>
<%-- 							</c:if> --%>
<%-- 							<c:if test="${empty depth2.name}"> --%>
<%-- 								<h4>${depth1.name}</h4> --%>
<%-- 							</c:if> --%>
							<ul>
								<c:forEach items="${productList}" var="dealProduct" varStatus="j">
								<c:set var="product" value="${dealProduct}" scope="request" />
									<c:if test="${dealProduct.dealGroupNo eq depth2.dealGroupNo || dealProduct.dealGroupNo eq depth1.dealGroupNo}">
										<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
											<jsp:param name="type" value="multichild" />
										</jsp:include>
									</c:if>
								</c:forEach>
							</ul>
						</div>
					</c:forEach>	
				</div>
			</c:forEach>
		</div>
	</div>

		