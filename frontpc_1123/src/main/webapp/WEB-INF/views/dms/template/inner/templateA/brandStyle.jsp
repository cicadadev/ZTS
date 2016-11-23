<%--
	화면명 : 브랜드관 > 템플릿A > STYLE 화면
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
	
<div class="filterSorting">
	<ul>
		<li class="box_01">
			<div class="select_box1 radiusSel">
				<label>성별</label>
				<tags:codeList code="GENDER_TYPE_CD" var="genderCd" tagYn="N" />				
				<select onchange="brand.template.styleFiltering('GENDER');" id="gender_select">
					<option value="GENDER_TYPE_CD.ALL">전체</option>
					<c:forEach items="${genderCd}" var="gender" varStatus="gender_status">
						<option value="${gender.cd}">${gender.name}</option>
					</c:forEach>
				</select>
			</div>
			<div class="select_box1 radiusSel">
				<label>테마</label>
				<tags:codeList code="THEME_CD" var="themeCd" tagYn="N" />
				<select onchange="brand.template.styleFiltering('THEME');" id="theme_select">
					<option value="THEME_CD.ALL">전체</option>
					<c:forEach items="${themeCd}" var="theme" varStatus="theme_status">
						<c:if test="${theme.cd ne 'THEME_CD.ETC'}">
							<option value="${theme.cd}">${theme.name}</option>
						</c:if>
					</c:forEach>
				</select>
			</div>
			<div class="select_box1 radiusSel">
				<label>인기순</label>
				<select onchange="brand.template.styleFiltering('SORT');" id="sort_select">
					<option value="popular">인기순</option>
					<option value="new">최신순</option>
				</select>	
			</div>
		</li>
	</ul>
</div>

<div class="styleType_list">
	<div class="product_type1 prodType_4ea block">
		<ul>
			<c:choose>
				<c:when test="${empty styleList}">
					<div class="empty">스타일이 없습니다.</div>
				</c:when>
				<c:otherwise>
					<c:forEach items="${styleList}" var="style" varStatus="status">
						<fmt:parseDate value="${style.insDt}" var="dateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
						<fmt:formatDate value="${dateFmt}" var="styleInsDt" pattern="yyyy/MM/dd"/>
						
						<li>
							<a>
								<div class="img" onclick="brand.template.goStyleDetail('${style.styleNo}' , '${style.memberNo}');">
									<img src="${_IMAGE_DOMAIN_}${style.styleImg}" alt="${style.title}" />
								</div>
								
								<div class="info">
									<c:set var="theme" value="${style.mmsStyleproducts[0].themeName}" />									
									<c:choose>
										<c:when test="${not empty theme}">
											<span class="title" onclick="brand.template.goStyleDetail('${style.styleNo}' , '${style.memberNo}');">[${theme}] ${style.title}</span>
										</c:when>
										<c:otherwise>
											<span class="title" onclick="brand.template.goStyleDetail('${style.styleNo}' , '${style.memberNo}');">${style.title}</span>
										</c:otherwise>
									</c:choose>
							
									<div class="etc2">
										<span>${fn:substring(style.memberName, 0, fn:length(style.memberName)-3)}***</span>
										<span>${styleInsDt}</span>
										
										<c:choose>
											<c:when test="${not empty style.likeYn && style.likeYn eq 'Y'}">
												<button type="button" class="btnLike on" onclick="display.style.updateLike(this, '${style.styleNo}', '${style.memberNo}');">
											</c:when>
											<c:otherwise>
												<button type="button" class="btnLike" onclick="display.style.updateLike(this, '${style.styleNo}', '${style.memberNo}');">
											</c:otherwise>
										</c:choose>											
													<span id="listlikeCnt_${style.styleNo}">${style.likeCnt}</span>
												</button>
									</div>
								</div>
							</a>
						</li>	
					</c:forEach>
				</c:otherwise>
			</c:choose>		
		</ul>
	</div>
	
	<div class="paginateType1">
		<page:paging formId="styleForm" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
			total="${totalCount}" url="/mms/member/style/list/ajax?brandId=${brandInfo.brandId}" type="ajax" callback="brand.template.styleCallback" />
	</div>

</div>