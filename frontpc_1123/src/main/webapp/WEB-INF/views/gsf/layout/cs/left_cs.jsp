<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<script type="text/javascript" src="/resources/js/common/cust.ui.js"></script>
<script>
$(document).ready(function(){
	var tag = '<%=request.getParameter("faqTypeCd")%>';
	
	if(common.isNotEmpty(tag)){
		$('#' + tag.replace('.', '_')).parent().addClass("on").siblings("li").removeClass("on");
	}
});

</script>

<!-- ### cs 좌측 메뉴 : 2016.08.29 수정 ### -->
<div class="lnb cs_lnb">
	<h2>
		<a href="javascript:ccs.link.custcenter.main();" > <img src="/resources/img/pc/txt/cs.gif" alt="고객센터" />
		</a>
	</h2>
	<dl>
		<dt>
			<a href="javascript:ccs.link.go('/ccs/cs/faq/list', false);" >FAQ</a>
		</dt>
		<dd class="csLeftMenu">
			<ul>
				<li><a href="#" id="FAQ_TYPE_CD_PRODUCT" onclick="javascript:custcenter.faq.go('FAQ_TYPE_CD.PRODUCT', '', '');" >상품</a></li>
				<li><a href="#" id="FAQ_TYPE_CD_BRAND" onclick="javascript:custcenter.faq.go('FAQ_TYPE_CD.BRAND', '', '');" >브랜드</a></li>
				<li><a href="#" id="FAQ_TYPE_CD_SERVICE" onclick="javascript:custcenter.faq.go('FAQ_TYPE_CD.SERVICE', '', '');" >서비스</a></li>
				<li><a href="#" id="FAQ_TYPE_CD_ORDER" onclick="javascript:custcenter.faq.go('FAQ_TYPE_CD.ORDER', '', '');" >주문/결제/취소</a></li>
				<li><a href="#" id="FAQ_TYPE_CD_DELIVERY" onclick="javascript:custcenter.faq.go('FAQ_TYPE_CD.DELIVERY', '', '');" >배송</a></li>
				<li><a href="#" id="FAQ_TYPE_CD_AS" onclick="javascript:custcenter.faq.go('FAQ_TYPE_CD.AS', '', '');" >교환/반품/환불/AS</a></li>
				<li><a href="#" id="FAQ_TYPE_CD_COUPON" onclick="javascript:custcenter.faq.go('FAQ_TYPE_CD.COUPON', '', '');" >쿠폰/포인트/혜택</a></li>
				<li><a href="#" id="FAQ_TYPE_CD_MEMBER" onclick="javascript:custcenter.faq.go('FAQ_TYPE_CD.MEMBER', '', '');" >회원정보/기타</a></li>
				
			</ul>
		</dd>
	</dl>

	<dl>
		<dt>
			<a href="javascript:ccs.link.go('/ccs/cs/event/list', false);" >당첨자발표</a>
		</dt>
		<dd class="hide">하위메뉴 없음</dd>
	</dl>

	<dl>
		<dt>
			<a href="javascript:ccs.link.go('/ccs/cs/notice/list', false);" >공지사항</a>
		</dt>
		<dd class="hide">하위메뉴 없음</dd>
	</dl>

	<dl class="mocs mocs_03">
		<dt>
			<a href="#none" onclick="mms.common.nonMemberLoginLayer()">비회원주문조회</a>
		</dt>
		<dd class="hide">하위메뉴 없음</dd>
	</dl>

	<dl class="mocs mocs_04">
		<dt>
			<a href="#none" onclick="javascript:ccs.link.display.mobileApp();">이용안내</a>
		</dt>
		<dd class="hide">하위메뉴 없음</dd>
	</dl>

	<dl class="mocs mocs_01">
		<dt>
			<a href="javascript:ccs.link.custcenter.qna.go();" >1:1 문의</a>
		</dt>
		<dd class="hide">하위메뉴 없음</dd>
	</dl>

	<dl class="mocs mocs_02">
		<dt>
			<a href="javascript:ccs.link.mypage.activity.inquiry('MYQA');">1:1 답변확인</a>
		</dt>
		<dd class="hide">하위메뉴 없음</dd>
	</dl>

	<a href="tel:1588-8744" class="cs_tel"> <b>고객센터</b> <span
		class="phone">1588-8744</span>
		<p>
			<span>평일 09:00~18:00</span> <span>점심시간 11:30~12:30</span> 
			<span>토/일요일 및 공휴일 휴무</span>
		</p>
	</a>
</div>
<!-- ### //cs 좌측 메뉴 : 2016.08.29 수정 ### -->
