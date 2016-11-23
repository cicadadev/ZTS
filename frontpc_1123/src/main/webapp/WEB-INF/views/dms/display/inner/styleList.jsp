<%--
	화면명 : 스타일 리스트
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript">
$(document).ready(function() {
	
});
</script>

<form id = "styleForm">

</form>


<div class="styleType_list">
	<div class="product_type1 prodType_4ea block">
		<ul>
			<c:forEach items="${styleList}" var="style" varStatus="i">
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
								<c:choose>
									<c:when test="${isMobile}">
										<a href="/dms/display/styleDetail?styleNo=${style.styleNo}&memberNo=${style.memberNo}">						
									</c:when>
									<c:otherwise>
										<a href="javascript:display.style.styleDetailLayer('${style.styleNo}', '${style.memberNo}');">
									</c:otherwise>
								</c:choose>
								<c:choose>
									<c:when test="${not empty style.themeCd}">
										<h3 class="tit">[${style.themeCdName}]${style.title}</h3>
									</c:when>
									<c:otherwise>
										<h3 class="tit">${style.title}</h3>
									</c:otherwise>
								</c:choose>
								</a>
							</span>
							<div class="etc2">
								<span>${fn:substring(style.memberId,0,fn:length(style.memberId)-3)}***</span>
								<span>${styleInsDt}</span>
								<c:choose>
									<c:when test="${style.likeYn eq 'Y'}">
										<button type="button" class="btnLike on" onclick="display.style.updateLike(this, '${style.styleNo}', '${style.memberId}');">
											<span id="listlikeCnt_${style.styleNo}">${style.styleLikeCnt}</span>
										</button>
									</c:when>
									<c:otherwise>
										<button type="button" class="btnLike" onclick="display.style.updateLike(this, '${style.styleNo}', '${style.memberId}');">
											<span id="listlikeCnt_${style.styleNo}">${style.styleLikeCnt}</span>
										</button>
									</c:otherwise>
								</c:choose>
							</div>
						</div>
					</div>
				</li>
			</c:forEach>
		</ul>
	</div>
	<c:if test="${empty styleList}">
		<p class="empty">스타일 목록이 없습니다.</p>
	</c:if>
</div>


<div class="paginateType1">
	<page:paging formId="styleForm" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
			total="${totalCount}" url="/dms/display/style/list/ajax" type="ajax" callback="listCallback"/>
</div>