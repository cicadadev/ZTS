<%--
	화면명 : FAQ - FAQ 목록
	작성자 : roy
 --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>    
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>

<script type="text/javascript">
$(".mobile .csBox .btn_answer:not('.mo_noEvent')").off("click").on("click", function(e){
	$(this).closest("li").toggleClass("on").siblings("li").removeClass("on");
});

$(".pc .csBox .btn_answer:not('.pc_noEvent')").off("click").on("click", function(e){
	$(this).closest("li").toggleClass("on").siblings("li").removeClass("on");
});
</script>
 <c:choose>
 	<c:when test="${not empty search.isScroll}">
 		<c:forEach items="${list}" var="list" varStatus="status">
			<li>
				<div class="tr_box">
					<div class="col1">
						<span class="num">${status.index+1}</span>
					</div>
					<div class="col2">
						<b class="category">${list.faqTypeName }</b>
					</div>
					<div class="col3">
						<div class="text_indent maxW">
							<a id="faqNo${list.faqNo}" href="#none" class="name btn_answer">
								<em class="point mo_only">[${list.faqTypeName}]</em>
								${list.title}
							</a>
						</div>
					</div>
				</div>
				<div class="answer maxW" style="line-height: normal;">
					<span class="logo_0to7">0to7</span>
					<p >
						${list.detail}
					</p>
				</div>
			</li>
		</c:forEach>
 	</c:when>
 	<c:otherwise>
 		<style>

		.maxW img {max-width:100%;}
		
		</style>
		<script type="text/javascript">
			$(document).ready(function(){
				parent.openFaqNo(); 
			});
		</script>

 		<form id="faqForm">
			<input type="hidden" value="${search.info}"  name="info"/>
			<input type="hidden" value="${search.faqTypeCd}"  name="faqTypeCd"/>
			<input type="hidden" value="${totalCount}"  name="totalCount"/>
		</form>
		<div class="list_group" style="margin-top:20px">
			<div class="div_tb_thead4">
				<div class="tr_box">
					<span class="col1">번호</span> 
					<span class="col2">분류</span> 
					<span class="col3">제목</span>
				</div>
			</div>
			<!-- ### //테이블 헤더 ### -->
		
			<!-- ### 테이블 바디 ### -->
			
			<ul class="div_tb_tbody4">
				<c:choose>
					<c:when test="${not empty list}">
						<c:forEach items="${list}" var="list" varStatus="status">
							<li>
								<div class="tr_box">
									<div class="col1">
										<span class="num">${status.index+1}</span>
									</div>
									<div class="col2">
										<b class="category">${list.faqTypeName }</b>
									</div>
									<div class="col3">
										<div class="text_indent maxW">
											<a id="faqNo${list.faqNo}" href="#none" class="name btn_answer">
												<em class="point mo_only">[${list.faqTypeName}]</em>
												${list.title}
											</a>
										</div>
									</div>
								</div>
								<div class="answer maxW" style="line-height: normal;">
									<span class="logo_0to7">0to7</span>
									<p >
										${list.detail}
									</p>
								</div>
							</li>
						</c:forEach>
					</c:when>
					<c:otherwise>
						<li class="noData_tp1">
							검색 조건에 맞는 FAQ가 없습니다.
						</li>
					</c:otherwise>
				</c:choose>
			</ul>
			<!-- ### //테이블 바디 ### -->
		
			<!-- ### PC 페이징 ### -->
			<div class="paginateType1">
				<page:paging formId="faqForm" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
						total="${totalCount}" url="/ccs/cs/faq/list/ajax" type="ajax" callback="custcenter.faq.listCallback"/>
			</div>
			<!-- ### //PC 페이징 ### -->
			<!-- ### MOBILE 더보기 ### -->
		</div>
 	</c:otherwise>
 </c:choose>
