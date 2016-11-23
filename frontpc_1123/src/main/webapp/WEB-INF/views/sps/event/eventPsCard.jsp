<%--
	화면명 : 프론트 메인 > 이벤트&혜택> 제휴카드
	작성자 : stella
 --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript">
$(document).ready(function() {
	sps.event.getCardBenefit("${cardName}");
	
	$(".pscTab li").on("click", function() {
		$(".pscTab").find("li").removeClass("active");
		$(this).addClass("active");
		
		if (ccs.common.mobilecheck()) {
			$("#eventForm #eventBackYn").val("Y");
		}
		
		sps.event.getCardBenefit($(this).find("a").find("[name=hidCardName]").val());
	});
});
</script>

<c:choose>
	<c:when test="${isMobile eq 'true'}">
		<div class="mo_navi">
			<button type="button" class="btn_navi_prev" onclick="parent.history.back();">이전 페이지로..</button>
			<h2>${nameMap[cardName]}</h2>
		</div>
	</c:when>
	<c:otherwise>
		<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
			<jsp:param value="이벤트&혜택" name="pageNavi"/>
		</jsp:include>	
	</c:otherwise>
</c:choose>

<div class="inner eventWrap">

	<div class="psCardWrap">
		<c:if test="${isMobile ne 'true'}">
			<ul class="pscTab" style="display:none;">
				<li class="${cardName eq 'Hana1Q' ? 'active' : ''}">
					<a href="#">
						<img src="/resources/img/pc/event/partnershipCard/psCard_tab01.png" alt="하나카드 하나멤버스 1Q카드">
						<input type="hidden" name="hidCardName" value="Hana1Q" />
					</a>
				</li>
				<li class="${cardName eq 'BCHappy' ? 'active' : ''}">
					<a href="#">
						<img src="/resources/img/pc/event/partnershipCard/psCard_tab02.png" alt="비씨카드 BC국민행복카드">
						<input type="hidden" name="hidCardName" value="BCHappy" />
					</a>
				</li>
				<li class="${cardName eq 'LotteKHappy' ? 'active' : ''}">
					<a href="#">
						<img src="/resources/img/pc/event/partnershipCard/psCard_tab03.png" alt="롯데카드 롯데국민행복카드">
						<input type="hidden" name="hidCardName" value="LotteKHappy" />
					</a>
				</li>
				<li class="${cardName eq 'LotteIHappy' ? 'active' : ''}">
					<a href="#">
						<img src="/resources/img/pc/event/partnershipCard/psCard_tab04.png" alt="롯데카드 롯데아이행복카드">
						<input type="hidden" name="hidCardName" value="LotteIHappy" />
					</a>
				</li>
				<li class="${cardName eq 'LotteClub' ? 'active' : ''}">
					<a href="#">
						<img src="/resources/img/pc/event/partnershipCard/psCard_tab05.png" alt="롯데카드 롯데카드 육아클럽">
						<input type="hidden" name="hidCardName" value="LotteClub" />
					</a>
				</li>
				<li class="${cardName eq 'ShinhanPlus' ? 'active' : ''}">
					<a href="#">
						<img src="/resources/img/pc/event/partnershipCard/psCard_tab06.png" alt="신한카드 신한고운맘/KidsPlus 카드">
						<input type="hidden" name="hidCardName" value="ShinhanPlus" />
					</a>
				</li>
			</ul>
		</c:if>
		
		<div id="psCardInner">
		
		</div>
	</div>
</div>