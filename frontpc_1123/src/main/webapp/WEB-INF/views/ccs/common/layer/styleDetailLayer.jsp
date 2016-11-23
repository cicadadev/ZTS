<%--
	화면명 : 스타일 레이어
	작성자 : allen
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<% pageContext.setAttribute("newLineChar", "\n"); %>
<script type="text/javascript">

$(document).ready(function() {
	$("meta[property='og:title']").attr('content', '${styleDetail.title}');
	$("meta[property='og:image']").attr('content', '${_IMAGE_DOMAIN_}${styleDetail.styleImg}');
	$("meta[property='og:url']").attr('content', '${_FRONT_DOMAIN_URL_}/dms/display/styleDetail?styleNo=${styleDetail.styleNo}&memberNo=${memberNo}');
	$("meta[property='og:description']").attr('content', '');
	
	
	$(".pc .rolling_box .paginate button").off("click").on({
		"click" : function() {
			var num = fnListConEa( $(this).parent().siblings(".product_type1") );

			fnListControl( $(this).closest(".rolling_box"), $(this), num );
		}
	});
	
	
});	

	/* 목록이 몇개 단위인지 체크하기 : 2016.08.18 */
	function fnListConEa(target_name) {
		var num = 3;
	
		if( $(target_name).hasClass("prodType_5ea") ){
			num = 5;
		}else if( $(target_name).hasClass("prodType_4ea") ){
			num = 4;
		}else if( $(target_name).hasClass("prodType_2ea") ){
			num = 2;
		}
	
		return num;
	}


	/* 상품 목록 컨트롤러 (클릭) : 2016.08.09 */
	function fnListControl(targetList, btn_this, num) {
		var target_list = $(".product_type1", targetList);
		var list_max = $("li", target_list).length / num;

		if( $(btn_this).hasClass("prev") ){
			for(var i = 0; i < num; i++){
				$("ul", target_list).prepend( $("li:last", target_list) );
			}

			var now_num = parseInt( $(".current", targetList).text() ) - 1;
			if(now_num == 0){
				now_num = list_max;
			}

			$(".current", targetList).text(now_num);
		}else{
			for(var i = 0; i < num; i++){
				$("ul", target_list).append( $("li:first", target_list) );
			}

			var now_num = parseInt( $(".current", targetList).text() ) + 1;
			if(now_num > list_max){
				now_num = 1;
			}

			$(".current", targetList).text(now_num);
		}
	}




</script>
<div class="pop_wrap sLayer_style" id="styleLayer">
	<div class="pop_inner">
		<div class="pop_header type1">
			<c:choose>
				<c:when test="${not empty styleDetail.themeCd}">
					<h3 class="tit">[${styleDetail.themeCdName}]${styleDetail.title}</h3>
				</c:when>
				<c:otherwise>
					<h3 class="tit">${styleDetail.title}</h3>
				</c:otherwise>
			</c:choose>
		</div>
		<div class="pop_content">
			<div class="pop_style_detail">
				<div class="detail"><img src="${_IMAGE_DOMAIN_}${styleDetail.styleImg}" alt=""></div>
				<div class="sns_btn">
					<!-- 16.10.09 : sns 추가 -->
					<a href="#none" class="fbook" onclick="ccs.sns.share('facebook')">페이스북</a>
					<a href="#none" class="kakao" onclick="ccs.sns.share('kakaoStory')">카카오스토리</a>
<!-- 					<a href="#none" class="kakaotalk" onclick="ccs.sns.share('kakaoLink')">카카오톡</a> -->
					<a href="#none" class="twitter" onclick="ccs.sns.share('twitter')">트위터</a>
<!-- 				<a href="#none" onclick="ccs.sns.share('sms', '')">SMS문자</a> -->
					<a href="#none" class="blog" onclick="ccs.sns.share('blog')">blog</a>
					<a href="#none" class="url" onclick="ccs.sns.share('link')">URL</a>
					
				</div>
				
				<div class="rolling_box">
					<div class="product_type1">
						<ul>
							<c:forEach items="${styleDetail.mmsStyleproducts}" var="product">
								<li>
									<a href="javascript:ccs.link.product.detail('${product.pmsProduct.productId}','', '${product.styleNo}');" >
										<div class="img">
											<tags:prdImgTag productId="${product.pmsProduct.productId}" seq="0" size="326"  />
										</div>

										<div class="info">
											<span class="title">${product.pmsProduct.name}</span>
											<div class="etc">
												<span class="price">
													<fmt:formatNumber type="currency" value="${product.pmsProduct.salePrice}" pattern="###,###" /><i>원</i>
												</span>
											</div>
										</div>
									</a>
								</li>
							</c:forEach>
						</ul>
					</div>

					<div class="paginate">
						<button class="prev">이전</button><button class="next">다음</button>
					</div>
				</div>
				
				<p class="txt_info"> * 코디상품은 실제상품과 차이가 있을 수 있습니다.  상품 상세에서 옵션을 선택해주세요.</p>
			</div>
			<div class="style_detail_comment">
				<ul>
					<li>
						<div class="comment_info">
							<c:if test="${styleDetail.idShowYn eq 'Y' }">
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
								<c:if test="${loginId eq styleDetail.memberId}">
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
		</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>