<%--
	화면명 : 스타일상세
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>

<script type="text/javascript" src="/resources/js/mms/mms.mypage.js"></script>
<script type="text/javascript" src="/resources/js/common/common.ui.js"></script>



<script type="text/javascript">
$(document).ready(function() {
	
	swiperCon('styleDetail_prodList_1', '5', 10);
	
	$(".mobile .btn_snsInfo").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .sLayer_sns") );
		}
	});
	
});
</script>
	
	<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
		<jsp:param value="스타일 상세" name="pageNavi"/>
	</jsp:include>	
	

<div class="inner">
	<div class="style_shop_inner">
		<c:choose>
			<c:when test="${not empty styleDetail.themeCd}">
				<h3 class="tit">[${styleDetail.themeCdName}]${styleDetail.title}</h3>
			</c:when>
			<c:otherwise>
				<h3 class="tit">${styleDetail.title}</h3>
			</c:otherwise>
		</c:choose>
		<div class="pop_style_detail">
			<div class="detail"><img src="${_IMAGE_DOMAIN_}${styleDetail.styleImg}" alt=""></div>
			<button type="button" class="btn_snsInfo">sns 공유</button>

			<div class="rolling_box">
				<div class="prodSwiper swiper-container styleDetail_prodList_1">
					<ul class="swiper-wrapper">

					<c:forEach items="${styleDetail.mmsStyleproducts}" var="product" varStatus="i">
						<li class="swiper-slide">
							<a href="javascript:ccs.link.product.detail('${product.pmsProduct.productId}','', '${product.styleNo}');" >
								<div class="img">
									<tags:prdImgTag productId="${product.pmsProduct.productId}" seq="0" size="326"  />
								</div>

								<div class="info">
									<span class="title">${product.pmsProduct.name}</span>
									<div class="etc">
										<span class="price"><fmt:formatNumber type="currency" value="${product.pmsProduct.salePrice}" pattern="###,###" /><i>원</i></i></span>
									</div>
								</div>
							</a>
						</li>
					</c:forEach>
					</ul>
				</div>
<!-- 				<div class="paginate"> -->
<!-- 					<button class="prev">이전</button><button class="next">다음</button> -->
<!-- 				</div>				 -->
			</div>
			<p class="txt_info"> * 코디상품은 실제상품과 차이가 있을 수 있습니다. <br>  상품 상세에서 옵션을 선택해주세요.</p>
		</div>
			
		<div class="style_detail_comment">
			<ul>
				<li>
					<div class="comment_info">
						<c:if test="${loginId ne styleDetail.memberId }">
							<strong class="user">${fn:substring(styleDetail.memberId,0,fn:length(styleDetail.memberId)-3)}***</strong>
						</c:if>
						<p class="tag">${styleDetail.hashTag}</p>
						<p class="info">${fn:replace(styleDetail.detail, newLineChar, '<br/>')}</p>
						<span class="l_box">
							<fmt:parseDate value="${styleDetail.insDt}" var="dateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
							<fmt:formatDate value="${dateFmt}" var="styleInsDt" pattern="yyyy/MM/dd"/>
							<span class="date">${styleInsDt}</span>
							<c:choose>
								<c:when test="${styleDetail.likeYn eq 'Y'}">
									<button type="button" class="btnLike on" onclick="display.style.updateLike(this, '${styleDetail.styleNo}', '${styleDetail.memberId}');">
										<span id="likeCnt_${styleDetail.styleNo}">${styleDetail.styleLikeCnt}</span>
									</button>
								</c:when>
								<c:otherwise>
									<button type="button" class="btnLike" onclick="display.style.updateLike(this, '${styleDetail.styleNo}', '${styleDetail.memberId}');">
										<span id="likeCnt_${styleDetail.styleNo}">${styleDetail.styleLikeCnt}</span>
									</button>
								</c:otherwise>
							</c:choose>
						</span>
						<!-- 16.10.09 : 추가 -->
						<div class="align_r">
							<c:if test="${loginId eq styleDetail.memberId }">
								<c:if test="${isApp}">
									<button type="button" class="btn_edit" onclick="mypage.style.makeAndModifyStyle('${styleDetail.styleNo}', '${loginId}');">수정</button>
								</c:if>
								<button type="button" class="btn_del" onclick="mypage.style.deleteStyle('${styleDetail.styleNo}');">삭제</button>
							</c:if>
						</div>
						<!-- //16.10.09 : 추가 -->
					</div>
				</li>
			</ul>
		</div>
		<div class="btn_wrapC btn1ea">
			<a href="javascript:parent.history.back();" class="btn_bStyle1 sWhite1">목록</a>
		</div>
	</div>
</div>

