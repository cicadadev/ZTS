<%--
	화면명 : 멤버쉽관 > 인증 후
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ page import="gcp.frontpc.common.contants.Constants"%>
<%
	pageContext.setAttribute("topAfter", Config.getString("corner.special.mem.img.1"));
// 	pageContext.setAttribute("exhibitAfter", Config.getString("common.special.membership.bnr.exhibit.after"));
%>
<script type="text/javascript" src="/resources/js/jquery.countdown.min.js"></script>
<script type="text/javascript">
$(document).ready(function(){
	$('.rw_certifyMemBox').removeClass('before');
	$('.rw_certifyMemBox').addClass('after');
});
</script>
<c:set var="topBnr"  value="${cornerMap[topAfter]}"/>
<%-- <c:set var="exhibitBnr"  value="${cornerMap[exhibitAfter]}"/> --%>
<div class="memVisual">
	<c:forEach var="item" items="${topBnr.dmsDisplayitems}" varStatus="status">
		<li ${status.index ==0 ? 'class="on"' : '' }>
			<c:choose>
				<c:when test="${isMobile}">
					<a href="${item.url1}"><img src="${_IMAGE_DOMAIN_}${item.img2}" alt="${item.text2}" /></a>
				</c:when>
				<c:otherwise>
					<a href="${item.url1}"><img src="${_IMAGE_DOMAIN_}${item.img1}" alt="${item.text1}" /></a>		
				</c:otherwise>
			</c:choose>
		</li>
	</c:forEach>
	
<!-- 	<div class="boxInner"> -->
<!-- 		<div class="memImgWrap"> -->
<!-- 			<div class="memImgL"> -->
<!-- 				<img src="/resources/img/pc/txt/txt_certificationAfter01.png" alt="제로투세븐 회원전용 - 멤버십관  -->
<!-- 				멤버십관 혜택안내 -->
<!-- 				·유아 필수템 깜짝 오픈 한정수량 특가찬스 -->
<!-- 				·알로앤루, 알퐁소, 포래즈, 섀르반 신상품 ~20% 할인혜택 -->
<!-- 				·전 상품 무료배송 (도서, 산간 지역 도선료 별도 부과)" > -->
<!-- 			</div> -->
<!-- 			<div class="memImgR"> -->
<!-- 				<a href="javascript:void(0);" class="btn_vip"><img src="/resources/img/pc/txt/txt_certificationAfter02_01.png" alt="VIP 등급혜택받기" /></a> -->
<!-- 				<span class="txt_mir"><img src="/resources/img/pc/txt/txt_certificationAfter02_02.png" alt="매월1일오픈! 회원쿠폰 한번 더 혜택! 릴레이쿠폰" /></span> -->
<!-- 				<a href="javascript:void(0);" class="btn_coupon">COUPON</a> -->
<!-- 			</div> -->
<!-- 		</div> -->
<!-- 	</div> -->
</div>

<div class="certifyMem">
	<h3>회원인증!</h3>
	<ul>
		<li class="level">
			<span class="myLevel">나의 등급 : <strong>FAMILY</strong></span>
			<span>고객정보 인증 회원전용입니다.</span>
		</li>
		<li class="btns">
			<div>
				<button type="button" class="rw_btn_02">멤버십관 혜택안내</button>
				<button type="button" class="rw_btn_02 white">등급혜택 받기</button>
			</div>
		</li>
	</ul>
</div>

<div class="memCate listOuterBox">
	<!-- mo 상품정렬, 상품뷰형태 -->
	<c:if test="${ isMobile }">
		<input type="hidden" class="dept1_Id" />
		<input type="hidden" class="dept2_Id" />
		<div class="mo_sortBox">
			<ul>
				<li class="on"><a href="javascript:void(0);"  onclick="javascript:special.membership.depthClick(this, '', '', '', 'MO')">전체</a></li>
				<c:forEach var="depth1" items="${depthList }" varStatus="status">
					<c:set var="listSize" value="${fn:length(depth1.spsDealgroups) }"/>
					<li>
						<a href="javascript:void(0);" onclick="javascript:special.membership.depthClick(this, '${depth1.dealGroupNo}', '', '${listSize }', 'MO')">${depth1.name }</a>
					</li>
				</c:forEach>
			</ul>
			<ul class="selectArea">
				<c:forEach var="depth1" items="${depthList }" varStatus="status">
					<li class="selectBox_${depth1.dealGroupNo}" style="display: none;">
						<select onchange="javascript:special.membership.moSelect('${depth1.dealGroupNo}',this.value)">
							<option value="">테마선택</option>
							<c:forEach var="depth2" items="${depth1.spsDealgroups }" varStatus="status2">
								<option value="${depth2.dealGroupNo}">${depth2.name }</option>
							</c:forEach>
						</select>
					</li>
				</c:forEach>
				<li class="productSort" style="display: none;">
					<tags:codeList code="PRODUCT_SORT_CD" var="productSort" tagYn="N"/>
					<select class="productSortType" onchange="javascript:special.membership.sortType(this.value)">
						<c:forEach items="${productSort}" var="sort">
							<option value="${sort.cd}">${sort.name}</option>
<%-- 											<c:if test="${sort.cd eq search.sort}"> --%>
<%-- 												<option value="${sort.cd}" selected="selected">${sort.name}</option> --%>
<%-- 											</c:if> --%>
<%-- 											<c:if test="${sort.cd ne search.sort}"> --%>
<%-- 												<option value="${sort.cd}">${sort.name}</option> --%>
<%-- 											</c:if> --%>
						</c:forEach>
					</select>
				</li>
				<li class="listType" style="display: none;"><button type="button" class="btnListType">블록형 / 리스트형</button></li>
			</ul>
		</div>
	
		<div class="bnr">
			<a href="javascript:void(0);">
				<span>
					<img src="/resources/img/pc/temp/temp_bnr_membership2.jpg" alt="" />
					<i class="ribbon">membership</i>
				</span>
			</a>
		</div>
	</c:if>

	<div class="tab_box2">
		<%-- PC --%>
		<c:if test="${ !isMobile }">
			<ul class="tab tab_menu">
				<li class="on"><a href="javascript:void(0);"  onclick="javascript:special.membership.depthClick(this)">전체</a></li>
				<c:forEach var="depth1" items="${depthList }" varStatus="status">
					<c:choose>
						<c:when test="${empty depth1.spsDealgroups}">
							<c:set var="listSize" value="0"/>
						</c:when>
						<c:otherwise>
							<c:set var="listSize" value="${fn:length(depth1.spsDealgroups) }"/>
						</c:otherwise>
					</c:choose>
					<li>
						<a href="javascript:void(0);" onclick="javascript:special.membership.depthClick(this, '${depth1.dealGroupNo}', '', '${listSize }')">${depth1.name }</a>
						<ul class="subtab${status.index }">
							<c:forEach var="depth2" items="${depth1.spsDealgroups }" varStatus="status2">
								<li><a href="javascript:void(0);" 
								onclick="javascript:special.membership.depthClick(this, '${depth1.dealGroupNo}', '${depth2.dealGroupNo}', '${status.index }')">${depth2.name }</a></li>
							</c:forEach>
						</ul>
					</li>
				</c:forEach>
			</ul>
		</c:if>
		
		<div class="tabcont">
			<c:if test="${ !isMobile }">
				<div class="cateBnr">
					<ul>
						<li><a href="javascript:void(0);"><img src="/resources/img/pc/temp/temp_bnr_cate.jpg" alt="궁중비책 물티슈 &amp;스킨케어 한정특가" /></a></li>
						<li><a href="javascript:void(0);"><img src="/resources/img/pc/temp/temp_bnr_cate.jpg" alt="궁중비책 물티슈 &amp;스킨케어 한정특가" /></a></li>
						<li><a href="javascript:void(0);"><img src="/resources/img/pc/temp/temp_bnr_cate.jpg" alt="궁중비책 물티슈 &amp;스킨케어 한정특가" /></a></li>
					</ul>
				</div>
			</c:if>
			<div class="productListBox type1">
				<ul>
					<c:forEach var="product" items="${productList}" varStatus="i">
						<c:set var="product" value="${product}" scope="request" />
						<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
							<jsp:param name="type" value="member" />
							<jsp:param name="dealProductIndex" value="${i.index }" />
						</jsp:include>
					</c:forEach>
					<c:if test="${empty productList}">
						상품이 없습니다.
					</c:if>
				</ul>
			</div>
		</div>
	</div>
</div>