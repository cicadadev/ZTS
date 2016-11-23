<%--
	화면명 : 브랜드관 > 템플릿A > CATALOG & COORDI LOOK 화면
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<script type="text/javascript" src="/resources/js/dms/dms.salesAssist.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	sales.contentsInit();
	
	// 코디북 이미지 사이즈 조정
	for (var i=0; i<$(".mobile .coordiBox .lookbook_list .img").length; i++) {
		var catalogueImg = $(".coordiBox .lookbook_list .img:eq(" + i + ")").find("img");
		
		catalogueImg.height( catalogueImg.width() );
		catalogueImg.parent().find("div:eq(" + i + ")").height( catalogueImg.width() );
	}
	
	// 카탈로그 상세 이동 및 상세화면 내 스와이프 및 이미지 슬라이더 조정
	$("[name=lookbook_detailLink]").on("click", function() {
		var id = $(this).find("#hid_lookbook_id").val();
		var name = $(this).find("#hid_lookbook_name").val();
		
		sales.lookbookDetail(id, name);
	});
	
	// 코디북 상세 이동 및 상세화면 내 스와이프 및 이미지 슬라이더 조정
	$("[name=coordi_detailLink]").on("click", function() {
		var id = $(this).find("#hid_coordi_id").val();
		var name = $(this).find("#hid_coordi_name").val();
		
		sales.lookbookDetail(id, name);
	});
});
</script>

<div class="lookbook">
	<ul class="tabBox tp1">
		<li class="on">
			<a>COLLECTION</a>
		</li>
		<li>
			<a>COORDI BOOK</a>
		</li>
	</ul>
	<c:if test="${isMobile ne 'true'}">
		<div class="sortingR">
			<div class="select_box1">
				<label>시즌/년도</label>
				<select id="seasonSelect" onchange="sales.seasonSelect();">
					<option value="all">시즌/년도</option>
					<c:forEach items="${codeList}" var="code" varStatus="status">
						<option value="${code.cd}">${code.name}</option>
					</c:forEach>
				</select>
			</div>
		</div>	
	</c:if>
	<div class="lookbookBox">
		<div class="lookbook_list">
			<ul class="list_grid">
				<c:choose>
					<c:when test="${empty lookbookList}">
						<div class="empty">LOOK BOOK이 없습니다.</div>
					</c:when>
					<c:otherwise>
						<c:forEach items="${lookbookList}" var="lookbook" varStatus="status">
							<li id="lookbook_detailLink_${status.index}" name="lookbook_detailLink">
								<a>
									<div class="img">
										<c:choose>
											<c:when test="${isMobile eq 'true'}">
												<img src="${_IMAGE_DOMAIN_}${lookbook.img2}" alt="${lookbook.name}" width="50%;" height:auto;/>
											</c:when>
											<c:otherwise>
												<img src="${_IMAGE_DOMAIN_}${lookbook.img1}" alt="${lookbook.name}" width="324px" height:auto;/>
											</c:otherwise>
										</c:choose>
										<input type="hidden" name="hid_lookbook_id" id="hid_lookbook_id" value="${lookbook.catalogId}" />
										<input type="hidden" name="hid_lookbook_name" id="hid_lookbook_name" value="${lookbook.name}" />							
									</div>
									<div class="info_pack">
										<div class="title">${lookbook.name}</div>
									</div>
								</a>
							</li>
						</c:forEach>							
					</c:otherwise>
				</c:choose>
			</ul>
		</div>
	</div>
	<div class="coordiBox" style="display:none;">
		<div class="lookbook_list">
			<ul>
				<c:choose>
					<c:when test="${empty coordibookList}">
						<div class="empty">COORDI BOOK이 없습니다.</div>
					</c:when>
					<c:otherwise>
						<c:forEach items="${coordibookList}" var="coordibook" varStatus="status1">
							<li id="coordi_detailLink_${status1.index}" name="coordi_detailLink">
								<a>
									<div class="img">
										<c:choose>
											<c:when test="${isMobile eq 'true'}">
												<img src="${_IMAGE_DOMAIN_}${coordibook.img2}" alt="${coordibook.name}" width="50%;"/>
											</c:when>
											<c:otherwise>
												<img src="${_IMAGE_DOMAIN_}${coordibook.img1}" alt="${coordibook.name}" width="324px;" height="324px;"/>
											</c:otherwise>
										</c:choose>
										<input type="hidden" name="hid_coordi_id" id="hid_coordi_id" value="${coordibook.catalogId}" />
										<input type="hidden" name="hid_coordi_name" id="hid_coordi_name" value="${coordibook.name}" />										
									</div>
									<div class="info_pack">
										<div class="title">${coordibook.name}</div>
									</div>
								</a>
							</li>
						</c:forEach>							
					</c:otherwise>
				</c:choose>					
			</ul>
		</div>
	</div>

</div>
