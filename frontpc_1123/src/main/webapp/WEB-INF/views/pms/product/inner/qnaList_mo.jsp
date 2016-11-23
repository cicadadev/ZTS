<%--
	화면명 : 상품상세 - QnA목록
	작성자 : eddie
 --%>

<%@ page language="java" contentType="text/html; charset=EUC-KR"
    pageEncoding="EUC-KR"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>    
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>

<script>
	$(".qArea").find("a").off("click").on("click", function(){
		if($(this).hasClass("on")){
			$(this).removeClass("on");
			$(this).closest(".tr_box").next(".aArea").hide();
		} else {
			$(".qArea").find(">a").removeClass("on");
			$(".aArea").hide();
			$(this).addClass("on");
			$(this).closest(".tr_box").next(".aArea").show();
		}
	});
</script>

<c:choose>
	<c:when test="${empty search.isScroll}">
		<form id="qnaSearchForm">
			<input type="hidden" value="${search.productId }" name="productId"/>
			<input type="hidden" value="${search.secretYn }" name="secretYn"/>
			<input type="hidden" value="${totalCount}"  	name="qnaTotalCount"/>
		</form>
		<c:choose>
		<c:when test="${not empty list}">
			<c:forEach var="qna" items="${list}">
				<li>
					<div class="tr_box">
						<div class="col1">
							<!-- 상태값 -->
							<c:choose>
								<c:when test="${ qna.productQnaStateCd eq 'PRODUCT_QNA_STATE_CD.COMPLETE'}">
									<c:set var="statusClass" value="iconPink3"/>
								</c:when>
								<c:when test="${ qna.productQnaStateCd eq 'PRODUCT_QNA_STATE_CD.ANSWER'}">
									<c:set var="statusClass" value="iconBlue3"/>
								</c:when>
								<c:when test="${ qna.productQnaStateCd eq 'PRODUCT_QNA_STATE_CD.ACCEPT'}">
									<c:set var="statusClass" value="iconPurple3"/>
								</c:when>
							</c:choose>
							<span class="icon_type2 ${statusClass }"><tags:codeName code="${ qna.productQnaStateCd }"/></span>
						</div>
						
						<div class="col2">
							<div class="title qArea">
								<span class="type"><i>[</i><tags:codeName code="${qna.productQnaTypeCd }"/><i>]</i></span>
								<c:if test="${qna.secretYn == 'Y'}">	<%--비밀글 --%>	
									<a href="#none"><span class="ico_pw">반품을 하고 싶습니다.</span></a>
								</c:if>
								<c:if test="${qna.secretYn == 'N'}">
									<a href="#none">${ qna.title }</a> <!-- 클릭시 a class="on" 추가 -->
								</c:if>
							</div>
						</div>
						<!-- 2016.11.04 수정 시작 -->
						<div class="col3 interval">
							<span class="user">
								<c:choose><c:when test="${ memberNo ne qna.memberNo }">${fn:substring(qna.memberId,0,fn:length(qna.memberId)-3)}***</c:when><c:otherwise>${qna.memberId }</c:otherwise></c:choose>
							</span>
							<span class="date">
								<fmt:parseDate var="dateString" value="${qna.insDt}" pattern="yyyy-MM-dd HH:mm:ss" />
								<fmt:formatDate value="${dateString}" pattern="yyyy/MM/dd" />
							</span>
						</div>
						<!-- //2016.11.04 수정 끝 -->
					</div>
					<div class="txt_group aArea" style="display: none;">
						<div class="qa_outer">
							<div class="q">
								${ qna.detail } 
							</div>
							<c:if test="${not empty qna.answer  }">	<%--답변 --%>		
								<div class="a">
									<p class="tit">[제로투세븐]</p>
									${qna.answer}
									<span class="date">
										<fmt:parseDate var="dateString" value="${ qna.answerDt }" pattern="yyyy-MM-dd HH:mm:ss" />
										<fmt:formatDate value="${dateString}" pattern="yyyy/MM/dd" />
									</span>
								</div>
							</c:if>	
						</div>
					</div>
				</li>
			</c:forEach>
		</c:when>
		<c:otherwise>
			<li class="empty">
				<div class="tr_box">
					<div class="col99">등록된 Q&amp;A가 없습니다.</div>
				</div>
			</li>
		</c:otherwise>
		</c:choose>
		
		<script>
			$('[name=qnaTotal]').html("(${totalCount})");
		</script>
	</c:when>
	<c:otherwise>
		<c:forEach var="qna" items="${list}">
			<li>
				<div class="tr_box">
					<div class="col1">
						<!-- 상태값 -->
						<c:choose>
							<c:when test="${ qna.productQnaStateCd eq 'PRODUCT_QNA_STATE_CD.COMPLETE'}">
								<c:set var="statusClass" value="iconPink3"/>
							</c:when>
							<c:when test="${ qna.productQnaStateCd eq 'PRODUCT_QNA_STATE_CD.ANSWER'}">
								<c:set var="statusClass" value="iconBlue3"/>
							</c:when>
							<c:when test="${ qna.productQnaStateCd eq 'PRODUCT_QNA_STATE_CD.ACCEPT'}">
								<c:set var="statusClass" value="iconPurple3"/>
							</c:when>
						</c:choose>
						<span class="icon_type2 ${statusClass }"><tags:codeName code="${ qna.productQnaStateCd }"/></span>
					</div>
					
					<div class="col2">
						<div class="title qArea">
							<span class="type"><i>[</i><tags:codeName code="${qna.productQnaTypeCd }"/><i>]</i></span>
							<a href="#none">${ qna.title }</a> <!-- 클릭시 a class="on" 추가 -->
						</div>
					</div>
					<!-- 2016.11.04 수정 시작 -->
					<div class="col3 interval">
						<span class="user">
							<c:choose><c:when test="${ memberNo ne qna.memberNo }">${fn:substring(qna.memberId,0,fn:length(qna.memberId)-3)}***</c:when><c:otherwise>${qna.memberId }</c:otherwise></c:choose>
						</span>
						<span class="date">
							<fmt:parseDate var="dateString" value="${qna.insDt}" pattern="yyyy-MM-dd HH:mm:ss" />
							<fmt:formatDate value="${dateString}" pattern="yyyy/MM/dd" />
						</span>
					</div>
					<!-- //2016.11.04 수정 끝 -->
				</div>
				<div class="txt_group aArea" style="display: none;">
					<div class="qa_outer">
						<div class="q">
							${ qna.detail } 
						</div>
						<c:if test="${not empty qna.answer  }">	<%--답변 --%>		
							<div class="a">
								<p class="tit">[제로투세븐]</p>
								${qna.answer}
								<span class="date">
									<fmt:parseDate var="dateString" value="${ qna.answerDt }" pattern="yyyy-MM-dd HH:mm:ss" />
									<fmt:formatDate value="${dateString}" pattern="yyyy/MM/dd" />
								</span>
							</div>
						</c:if>	
					</div>
				</div>
			</li>
		</c:forEach>
	</c:otherwise>
</c:choose>