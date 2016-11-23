<%--
	화면명 : 스타일 상품 리스트
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<style>
 .tit {
     
  }


</style>

<script type="text/javascript">
	$("[name=TOTAL_CNT]").val("${totalCount}");
</script>
<form id="styleForm">


</form>


<div class="styleType_list">
	<div class="product_type1 block">
		<ul>
			<c:forEach var="style" items="${memberStyleList}" varStatus="i">
				<fmt:parseDate value="${style.insDt}" var="dateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
				<fmt:formatDate value="${dateFmt}" var="styleInsDt" pattern="yyyy/MM/dd"/>
				<li>
					<div>
						<div class="img">
							<c:choose>
								<c:when test="${isMobile}">
									<a href="/dms/display/styleDetail?styleNo=${style.styleNo}&memberNo=${style.memberNo}">						
								</c:when>
								<c:otherwise>
									<a href="javascript:display.style.styleDetailLayer('${style.styleNo}', '${style.memberNo}');">
								</c:otherwise>
							</c:choose>
									<img src="${_IMAGE_DOMAIN_}${style.styleImg}" alt="" />
								</a>
						</div>

						<div class="info">
							<span class="title">
								<c:if test="${style.styleStateCd eq 'STYLE_STATE_CD.READY'}">
									<i class="secret">비밀글</i>
								</c:if>
								<c:choose>
									<c:when test="${not empty style.themeCd}">
										<a href="#none">[${style.themeName}]${style.title}</a></span>
									</c:when>
									<c:otherwise>
										<a href="#none">${style.title}</a></span>
									</c:otherwise>
								</c:choose>
							<div class="etc2">
								<span>${styleInsDt}</span>
								<button type="button" class="btnLike" onclick="display.style.updateLike(this, '${style.styleNo}', '${style.memberId}');">
									<span id="likeCnt_${style.styleNo}">${style.likeCnt}</span>
								</button>
							</div>
							
						</div>
						<c:if test="${isApp}">
							<button type="button" class="btn_edit" onclick="mypage.style.modifyStyle('${style.styleNo}', '${loginId}');">수정</button>
						</c:if>
						<button type="button" class="btn_del" onclick="mypage.style.deleteStyle('${style.styleNo}');">삭제</button>
					</div>
				</li>
			</c:forEach>
		</ul>
		<c:if test="${empty memberStyleList}">
			<p class="empty">스타일 목록이 없습니다.</p>
		</c:if>
	</div>
	
</div>




<div class="paginateType1">
	<page:paging formId="styleForm" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
			total="${totalCount}" url="/dms/special/giftShop/list/ajax" type="ajax" callback="listCallback"/>
</div>