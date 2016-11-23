<%--
	화면명 : 마이페이지 > 나의활동 > 상품 Q&A list
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<div>
	<input type="hidden" id="hPageType" value="${pageType }">
	
	<input type="hidden" id="total" value="${info.completeCount + info.answerCount }">
	<input type="hidden" id="item1" value="${info.completeCount + 0}">
	<input type="hidden" id="totalCount" name="totalCount" value="${totalCount + 0}">
</div>

	<div class="div_tb_thead3">
		<div class="tr_box">
			<span class="col1">답변여부</span>
			<span class="col2">내용</span>
			<span class="col3">작성일</span>
		</div>	
	</div>
	
	<div>
		<ul class="div_tb_tbody3" id="productUl">
			<c:choose>
				<c:when test="${!empty productQnaList}">
					<c:forEach var="list" items="${productQnaList }">
						<li>
							<div class="tr_box">
								<div class="col1">
									<c:if test="${list.productQnaStateCd eq 'PRODUCT_QNA_STATE_CD.ACCEPT' }">
										<span class="icon_type2 ">
									</c:if>
									<c:if test="${list.productQnaStateCd eq 'PRODUCT_QNA_STATE_CD.ANSWER' }">
										<span class="icon_type2 iconBlue3">
									</c:if>
									<c:if test="${list.productQnaStateCd eq 'PRODUCT_QNA_STATE_CD.COMPLETE' }">
										<span class="icon_type2 iconPink3">
									</c:if>
										<tags:codeName code="${ list.productQnaStateCd }"/>
										</span>
								</div>
								
								<div class="col2">
									<div class="title qArea">
										<span class="type"><tags:codeName code="${ list.productQnaTypeCd }"/></span>
										<a href="#none">${list.title }</a>
									</div>
									<div class="positionR">
										<%--TODO 상품명 클릭시 상품사아세로 이동 --%>
										<a href="#none" class="title">
											${ list.pmsProduct.name}
										</a>
			
										<em class="option_txt">
											<i>${ list.pmsSaleproduct.name}</i>
										</em>
									</div>
								</div>
			
								<div class="col3">
									<span class="date">${list.insDt }</span>
								</div>
							</div>
							<div class="txt_group aArea">
								<div class="qa_outer">
									<div class="q">
										${ list.detail }
										<tags:prdImgTag productId="${list.productId}" size="500" alt="" />
									</div>
									<c:if test="${list.productQnaStateCd eq 'PRODUCT_QNA_STATE_CD.COMPLETE' }" >
										<div class="a">
											${list.answer }
											<span class="date">${list.answerDt }</span>
										</div>
									</c:if>
									<c:if test="${list.productQnaStateCd ne 'PRODUCT_QNA_STATE_CD.COMPLETE' }" >
										<div class="a">
											아직 등록된 답변 내역이 없습니다.
										</div>
									</c:if>
									<div class="btns">
										<c:if test="${list.productQnaStateCd eq 'PRODUCT_QNA_STATE_CD.ACCEPT' }" >
											<%-- TODO 수정 클릭시 상품문의게시판 이동? --%>
											<a href="#none" class="btn_sStyle1 sWhite2">수정</a>
										</c:if>
										<c:if test="${list.productQnaStateCd ne 'PRODUCT_QNA_STATE_CD.ANSWER' }" >
											<a href="#none" class="btn_sStyle1 sWhite2" onclick="javascript:mypage.inquiry.deleteInquiry('PRODUCT','${list.productQnaNo }')">삭제</a>
										</c:if>	
									</div>
								</div>
							</div>
						</li>
					</c:forEach>
				</c:when>
				
				<c:otherwise>
					<li class="noData_tp1">
						최근 문의하신 내역이 없습니다.
					</li>
				</c:otherwise>
			
			</c:choose>
		</ul>

		<!-- ### PC 페이징 ### -->
		<div class="pagePkg">
			<div class="paginateType1">
				<page:paging formId="" currentPage="${search.currentPage}" pageSize="${search.pageSize}"
					total="${totalCount}" url="/mms/mypage/inquiry/product/list/ajax" type="ajax" callback="mypage.inquiry.inquiryCallback"/>
			</div>
		</div>
		<!-- ### //PC 페이징 ### -->

	</div>
<!-- ### //테이블 바디 ### -->


	