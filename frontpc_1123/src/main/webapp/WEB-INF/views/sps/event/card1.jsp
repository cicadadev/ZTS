<%--
	화면명 : 카드사 제휴 - 하나카드 하나멤버스 1Q카드
	작성자 : eddie
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<c:choose>
	<c:when test="${isMobile eq 'true'}">
		<div class="mo_navi">
			<button type="button" class="btn_navi_prev" onclick="parent.history.back();">이전 페이지로..</button>
			<h2>이벤트</h2>
		</div>
	</c:when>
	<c:otherwise>
		<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
			<jsp:param value="이벤트" name="pageNavi"/>
		</jsp:include>
	</c:otherwise>
</c:choose>
<script>

</script>
<div class="inner eventWrap">
				
	<div class="psCardWrap">
		<ul class="pscTab">
			<li class="active"><a href="/sps/event/card?cno=1"><img src="/resources/img/pc/event/partnershipCard/psCard_tab01.png" alt="하나카드 하나멤버스 1Q카드"></a></li>
			<li><a href="/sps/event/card?cno=2" class=""><img src="/resources/img/pc/event/partnershipCard/psCard_tab02.png" alt="비씨카드 BC국민행복카드"></a></li>
			<li><a href="/sps/event/card?cno=3" class=""><img src="/resources/img/pc/event/partnershipCard/psCard_tab03.png" alt="롯데카드 롯데국민행복카드"></a></li>
			<li><a href="/sps/event/card?cno=5" class=""><img src="/resources/img/pc/event/partnershipCard/psCard_tab04.png" alt="롯데카드 롯데아이행복카드"></a></li>
			<li><a href="/sps/event/card?cno=4" class=""><img src="/resources/img/pc/event/partnershipCard/psCard_tab05.png" alt="롯데카드 롯데카드 육아클럽"></a></li>
			<li><a href="/sps/event/card?cno=6" class=""><img src="/resources/img/pc/event/partnershipCard/psCard_tab06.png" alt="신한카드 신한고운맘/KidsPlus 카드"></a></li>
		</ul>
		<div class="pscCont contArea01">
			<div class="pscTxtD pscTxt01">
				<span class="img_pc"><img src="/resources/img/pc/event/partnershipCard/psCard_img01_01.png" alt="하나멤버스 1Q 카드"></span>
				<span class="img_mc"><img src="/resources/img/mobile/event/partnershipCard/psCard_img01_01.png" alt="하나멤버스 1Q 카드"></span>
				<!-- btn area begin -->
				<a href="http://www.hanacard.co.kr/hscroute.jsp?goURL=/PI42000000D.web?_frame=no&CD_PD_SEQ=5082&bansrc=ztosv&ban_nm=living" class="btnDef btn_psc01" target="_blank"><img src="/resources/img/pc/event/partnershipCard/pscCont01_btn01.png" alt="LIVING 카드신청"></a>
				<a href="http://www.hanacard.co.kr/hscroute.jsp?goURL=/PI42000000D.web?_frame=no&CD_PD_SEQ=5083&bansrc=ztosv&ban_nm=shopping" class="btnDef btn_psc02" target="_blank"><img src="/resources/img/pc/event/partnershipCard/pscCont01_btn02.png" alt="SHOPPING 카드신청"></a>
				<a href="http://www.hanacard.co.kr/hscroute.jsp?goURL=/PI42000000D.web?_frame=no&CD_PD_SEQ=5084&bansrc=ztosv&ban_nm=daily" class="btnDef btn_psc03" target="_blank"><img src="/resources/img/pc/event/partnershipCard/pscCont01_btn03.png" alt="DAILY 카드신청"></a>
				<!-- // btn area end -->
						</div>
						<div class="pscTxtD pscTxt02">
							<span class="img_pc"><img src="/resources/img/pc/event/partnershipCard/psCard_img01_02.png" alt="1. 50% 청구할인 혜택
2. VIP 멤버십 혜택
3. 5천 하나머니 적립 혜택"></span>
							<span class="img_mc"><img src="/resources/img/mobile/event/partnershipCard/psCard_img01_02.png" alt="1. 50% 청구할인 혜택
2. VIP 멤버십 혜택
3. 5천 하나머니 적립 혜택"></span>
							<!-- btn area begin -->
				<a href="/sps/event/mobileApp" class="btnDef btn_psc04" target="_self"><img src="/resources/img/pc/event/partnershipCard/pscCont01_btn04.png" alt="앱 다운받기"></a>
				<a href="/sps/event/benefit" class="btnDef btn_psc05" target="blank">
				<span class="img_pc"><img src="/resources/img/pc/event/partnershipCard/pscCont01_btn05.png" alt="VIP 혜택보기"></span>
				<span class="img_mc"><img src="/resources/img/mobile/event/partnershipCard/pscCont01_btn05.png" alt="VIP 혜택보기"></span>
				</a>
				<a href="#none" onclick="viewLayer(openCardLayer);" class="btnDef btn_psc06">
				<span class="img_pc"><img src="/resources/img/pc/event/partnershipCard/pscCont01_btn06.png" alt="VIP 혜택받기"></span>
				<span class="img_mc"><img src="/resources/img/mobile/event/partnershipCard/pscCont01_btn06.png" alt="VIP 혜택받기"></span>
				</a>
				<!-- // btn area end -->
			</div>
		</div>
	</div>
</div>
	<jsp:include page="/WEB-INF/views/sps/event/inner/cardNoLayer.jsp" flush="false">
		<jsp:param value="${cno}" name="cardType"/>
		<jsp:param value="제로투세븐 하나멤버스 1Q카드" name="cardName"/>
	</jsp:include>		
