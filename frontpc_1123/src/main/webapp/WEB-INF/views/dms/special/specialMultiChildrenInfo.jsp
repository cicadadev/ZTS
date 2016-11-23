<%--
	화면명 : 프론트 & 모바일 다자녀우대관
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<script type="text/javascript">
$(document).ready(function(){
	
});


function loginCheck(cd, name) {
	mms.common.loginCheck(function(isLogin){
		if (isLogin) {
			ccs.layer.cardAuthLayer.open(cd, name);
		}
	});
}
	

</script>
<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="전문관|다자녀우대관" name="pageNavi"/>
</jsp:include>


<c:if test="${isMobile}"> 
	<div class="inner">
		<div class="multichild_box2">
			<h4>
				<strong>다자녀우대카드</strong>
				<span>알고 계시나요?</span>
				<p>쇼핑, 문화놀이시설, 교육비 등 다양한 할인 혜택의 다자녀 우대카드 제로투세븐닷컴에서도 분유 할인 혜택을 드립니다.</p>
			</h4>
			<ul class="multichild_card_list">
				<tags:codeList code="CHILDRENCARD_TYPE_CD" var="childrenCardCdList" tagYn="N"/>
				<c:forEach items="${childrenCardCdList}" var="childrenCardCd" varStatus="i">
					<li>
						<a href="#none" class="btn_sStyle4 sGray2" onClick="javascript:loginCheck('${childrenCardCd.cd}', '${childrenCardCd.name}');">${childrenCardCd.name}</a>
					</li>
				</c:forEach>
			</ul>
			<div class="multichild_txt_info">
				<ul>
					<li>다자녀 우대카드는 지역마다 발급 조건이 다르오니, 자세한 내용은 각 카드별 신청자격 및 혜택 내용 참고하세요.</li>
					<li>PC에서 확인 시 카드사별 안내페이지로 연결 가능합니다.</li>
				</ul>
			</div>
		</div>
	</div>
</c:if>
			
<c:if test="${!isMobile}"> 
	<div class="multichild_main_top_wrap">
		<div class="multichild_main_top">
			<img src="/resources/img/pc/bg/bg_multichild_main_top_01.jpg" alt="다자녀 우대관 다자녀 우대카드 알고 계시나요? 쇼핑, 문화놀이시설, 교육비 등 다양한 할인 혜택의 다자녀 우대카드 제로투세븐닷컴에서도 분유 할인 혜택을 드립니다." /> <!-- 16.10.27 : 추가 -->
			<div class="multi_sale_notice">
				<strong>다자녀 할인혜택</strong>
				<ul>
					<li>매일분유 매월 1회 할인 혜택 제공 (매일유업 앱솔루트 명작, 궁 6캔 제품에 한해 할인제공)</li>
					<li>다자녀 우대관은 지역별 다자녀 카드로 인증 후 진입 가능하며, 다자녀 카드로만 결제 가능합니다.</li>
				</ul>
			</div>
		</div>
	</div>
	
	<div class="inner">
		<div class="multichild_box">
			<h3 class="sub_title">다자녀우대관 인증</h3>
			<ul class="multichild_card_list">
				<tags:codeList code="CHILDRENCARD_TYPE_CD" var="childrenCardCdList" tagYn="N"/>
				<c:forEach items="${childrenCardCdList}" var="childrenCardCd" varStatus="i">
					<li>
						<dl class="multichild_card_info">
							<dt><img src="/resources/img/pc/temp/card_img_0${i.index + 1}.gif" alt="${childrenCardCd.name}"></dt>
							<dd class="title">${childrenCardCd.name}</dd>
							<dd><a href="#none" onClick="javascript:loginCheck('${childrenCardCd.cd}', '${childrenCardCd.name}');" class="btn_sStyle4 sGray2">인증하고 할인 받기</a></dd>
							<dd><a href="${childrenCardCd.note}" class="card_link" target="_blank">카드혜택 자세히 보기 &gt;</a></dd>
						</dl>
					</li>
				</c:forEach>
			</ul>
			<div class="multichild_txt_info">
				<p>다자녀 우대카드는 지역마다 발급 조건이 다르오니, 자세한 내용은 각  카드혜택 자세히 보기 내용을 참고하세요.</p>
			</div>
		</div>
	</div>
</c:if>		
