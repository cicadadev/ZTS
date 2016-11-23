<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page session = "true" %>
<%@ page import="gcp.frontpc.common.contants.Constants" %>
<%@ page import="intune.gsf.common.utils.Config" %>

<!-- 페이스북 -->
<!-- <meta property="fb:app_id" content="1763733237229832" /> -->
<!-- <meta property="og:title" content="제로투세븐" />  -->
<!-- <meta property="og:image" content="" />  -->
<!-- <meta property="og:url" content="" /> -->
<!-- <meta property="og:type" content="website"> -->
<!-- <meta property="og:description" content="" /> -->

<%
	pageContext.setAttribute("headerImg", Config.getString("corner.common.banner.img.1") );
	pageContext.setAttribute("ageImg", Config.getString("corner.common.ctg.img.5") );
	pageContext.setAttribute("searchWord", Config.getString("corner.etc.search") );
	
	pageContext.setAttribute("channelId", session.getAttribute(Constants.SESSION_KEY_CHANNEL));
	
	pageContext.setAttribute("tommeetippe", Config.getString("brand.tommeetippe.code"));
	pageContext.setAttribute("allo", Config.getString("brand.allo.code"));
	pageContext.setAttribute("alfonso", Config.getString("brand.alfonso.code"));
	pageContext.setAttribute("fourlads", Config.getString("brand.fourlads.code"));
	pageContext.setAttribute("skarbarn", Config.getString("brand.skarbarn.code"));
	pageContext.setAttribute("royal", Config.getString("brand.royal.code"));
	pageContext.setAttribute("chooze", Config.getString("brand.chooze.code"));
	pageContext.setAttribute("yvol", Config.getString("brand.yvol.code"));
%>

<script>
var logout = function(){	
	$.ajax({
		url : "/j_spring_security_logout",
		type : "POST"		
	}).done(function(msg){
		if(msg.response.error){
			alert("로그아웃 실패");	
		}else{
			//location.href="/ccs/common/main";
			common.pageMove('main','','');
		}
		//alert(msg.response.message);		
	});
}

$(document).ready(function(){
	ccs.layer.noticePopupLayer.open();
});

//검색에서 사용함.
var _BRAND_ID = '';

</script>
<!-- ### 상단 배너 ### -->
<jsp:include page="/dms/corner/cornerItem/view" flush="false">
	<jsp:param value="${headerImg}" name="displayId"/>
</jsp:include>
<!-- ### //상단 배너 ### -->

<div class="inner">
	<!-- ### 유저 설정 ### -->
	<div class="user_setup">
		<ul class="visited">
			<li>
				<a href="javascript:void(0);" class="btn_favorites" onclick="dmspc.header.addFavorite();">즐겨찾기</a>
			</li>
			<li>
				<a href="javascript:ccs.link.display.mobileApp();" class="btn_app">모바일앱</a>
			</li>
			<li class="btn_baro">
				<a href="javascript:void(0);">바로방문 
						<c:if test="${empty channelId}">
							<em>ON</em>
						</c:if>
						<c:if test="${not empty channelId}">
							<i>OFF</i>
						</c:if>
				</a>
	
				<div class="layer">
					<strong>
						현재 바로방문
						<c:if test="${empty channelId}">
							<em>ON</em>
						</c:if>
						<c:if test="${not empty channelId}">
							<i>OFF</i>
						</c:if>
						상태입니다.
					</strong>
	
					<!-- OFF 
					<dl class="dl_type1">
						<dt>바로방문의 특별한 혜택</dt>
						<dd>
							<b>최대5%</b>
							구매금액 포인트적립
						</dd>
						<dd>
							<b>3%</b>
							할인쿠폰
						</dd>
					</dl>
					-->
					<!-- ON -->
					<dl class="dl_type1 dl_type1on">
						<dt>바로방문으로 쿠폰 혜택 받으세요!</dt>
						<dd>
							1.멤버십 등급별 쿠폰 사용<br />
							2.주말쿠폰 사용<br />
							3.첫 구매, 기념일 제공 쿠폰 사용
						</dd>
					</dl>
	
					<dl class="dl_type2">
						<dt>바로방문 하는 방법</dt>
						<dd>
							<a href="javascript:void(0);" class="item1" onclick="dmspc.header.addFavorite();"><span>즐겨찾기등록</span></a>
							<!-- <a href="javascript:void(0);"><span>시작페이지설정</span></a> --><!-- 10.28 보안상 시작페이지 설정 삭제 -->
							<a href="javascript:void(0);" class="item3" onclick=""><span>이메일 접속</span></a>
							<a href="javascript:void(0);" class="item4" onclick="dmspc.header.copy_clipboard(); return false;"><span>URL 직접입력</span></a>
							<a href="javascript:void(0);" class="item5" onclick="ccs.layer.mobileAppLayer.open();"><span>모바일앱 설치</span></a>
						</dd>
					</dl>
				</div>
			</li>
		</ul>

		<ul class="info" id="loginBtnBox">
			<jsp:include page="/WEB-INF/views/ccs/common/inner/loginBtnbox.jsp" />
		</ul>
	</div>
	<!-- ### //유저 설정 ### -->

	<!-- ### 검색 및 전문메뉴 ### -->
	<div class="etc_group">
		<a href="javascript:ccs.link.common.main()" class="logo">
			<img src="/resources/img/pc/logo/logo.jpg" alt="0to7.com" />
		</a>
		<div class="search_box"  id="corner${searchWord}">
			<input type="hidden" id="isTextBanner" value=""/>
			<input type="hidden" id="textBannerUrl" value=""/>
			<label for="header_search" > </label>
			<input type="text" value="" id="header_search" onkeyup="dmspc.header.searchKeyUp(this);"/>
			<a href="javascript:void(0);" class="btn_strDel">검색어 지우기</a>
			<input type="image" src="/resources/img/pc/btn/header_search.gif" alt="" class="btn_search" onclick="dmspc.header.searchLink();"/>
	
			<div class="word">
				<ul>
					<li class="on">
						<button type="button">최근 검색어</button>
	
						<div class="box">
							<ul class="last" id="latelySearch">
								
							</ul>
	
							<div class="util">
								<ol>
									<!-- <li>
										<a href="javascript:void(0);">자동저장 끄기</a>
									</li> -->
									<li>
										<a href="javascript:dmspc.header.deleteKeyWord();">검색목록 삭제</a>
									</li>
								</ol>
								<a href="javascript:void(0);" class="btn_close">닫기</a>
							</div>
						</div>
					</li>
	
					<li>
						<button type="button">인기 검색어</button>
	
						<div class="box">
							<ol class="rank" id="rankSearch_pc"></ol>
	
							<div class="util">
								<a href="javascript:void(0);" class="btn_close">닫기</a>
							</div>
						</div>
					</li>
				</ul>
			</div>
	
			<div class="similar" id="autoSearch">
				<ol class="link"></ol>
				
				<div id="recommend" style="display:none;">
					<strong class="sub">추천 카테고리 / 기획전 / 브랜드관</strong>
					<ul class="like">
						<li id="getRecommendCategory"></li>
						<li id="getRecommendExhibit"></li>
						<li id="getRecommendBrand"></li>
					</ul>
				</div>
	
				<div class="util">
					<a href="javascript:void(0);" class="btn_close">닫기</a>
				</div>
			</div>
		</div>
	
		<ul class="navi_pro">
			<li>
				<a href="javascript:ccs.link.display.style();">스타일</a>
			</li>
			<li>
				<a href="javascript:ccs.link.display.bestShop();">베스트</a>
			</li>
			<li>
				<a href="javascript:ccs.link.display.exhibit();">기획전</a>
			</li>
			<li>
				<a href="javascript:ccs.link.display.event();">이벤트&amp;혜택</a>
			</li>
		</ul>
	</div>
	<!-- ### //검색 및 전문메뉴 ### -->

	<!-- ### Gnb ### -->
	<div class="gnb">
		<div class="all">
			<a href="javascript:void(0);" class="btn_all">전체 카테고리</a>
						
			<!--// mouseClick 전체 카테고리 -->
			<div class="click" >
				
				<div class="menu">
				
					<div class="scroll_box">
						<dl id="pcCategory_1"></dl>
						<dl id="pcCategory_2"></dl>
						<dl id="pcCategory_3"></dl>
						<dl id="pcCategory_4"></dl>
									
						<dl>
							<dt id="corner${ageImg}"></dt>
							<dd id="ageCode"></dd>
						</dl>
					</div>

					<ul class="point">
						<!-- <li>
							<a href="javascript:ccs.link.special.membership();">멤버십관</a>
						</li> -->
						<li>
							<a href="javascript:ccs.link.special.premium();">프리미엄 멤버십관</a>
						</li>
						<li>
							<a href="javascript:ccs.link.special.subscription();" >정기배송관</a>
						</li>
						<li>
							<a href="javascript:ccs.link.special.pickup();">매장픽업관</a>
						</li>
						<li>
							<a href="javascript:ccs.link.special.giftShop();">기프트샵</a>
						</li>
						<li>
							<a href="javascript:ccs.link.special.milkPowder();">분유관</a>
						</li>
						<li>
							<a href="javascript:ccs.link.special.birthready();">출산준비관</a>
						</li>
						<li>
							<a href="javascript:ccs.link.special.multiChildrenInfo();">다자녀우대관</a>
						</li>
						<c:if test="${ employeeYn eq 'Y'}">
						<li>
							<a href="javascript:ccs.link.special.employee();">임직원관</a>
						</li>
						</c:if>
					</ul>
					<!-- // mod : 2016.10.11 end -->
					<a href="#none" class="btn_close">전체메뉴 닫기</a>
				</div>
			</div>
		</div>
		<!-- mouseClick 전체 카테고리 //-->
		
		<div class="group">
			<ul>
				<li class="gr1">
					<a href="javascript:ccs.link.special.premium();">프리미엄 멤버십관</a>
				</li>
				<li class="gr9">
					<a href="javascript:ccs.link.display.shockingzero();">
						<span class="icon">
							<img src="/resources/img/pc/ico/gnb_hot.png" alt="hot" />
						</span>
						쇼킹제로
					</a>
				</li>
				<li class="gr2">
					<a href="javascript:ccs.link.special.subscription();">정기배송관</a>
				</li>
				<li class="gr10">
					<a href="javascript:ccs.link.special.pickup();">
						<span class="icon">
							<img src="/resources/img/pc/ico/gnb_new.png" alt="new" />
						</span>
						매장픽업관
					</a>
				</li>
				<li class="gr5">
					<a href="javascript:ccs.link.special.giftShop();">기프트샵</a>
				</li>
				<li class="gr3">
					<a href="javascript:ccs.link.special.milkPowder();">분유관</a>
				</li>
				<li class="gr4">
					<a href="javascript:ccs.link.special.birthready();" >출산준비관</a>
				</li>
			</ul>
		</div>			
		
		<ul class="navi">
			<!-- 브랜드 목록 //-->
			<li>
				<a href="javascript:void(0);">브랜드</a>

				<!-- <div class="info" id="brendList">
					<ul>
						스크립트 HTML 
					</ul>
				</div> -->
				
				<div class="info">
					<ul class="js_list2n">
						<li>
							<a href="javascript:brand.template.main('${allo}');">
								<span class="img"><img src="/resources/img/pc/logo/brand1.jpg" alt="알로&amp;루" /></span>
<!-- 								<span class="name">알로&amp;루</span> -->
							</a>
						</li>
						<li>
							<a href="javascript:brand.template.main('${alfonso}');">
								<span class="img"><img src="/resources/img/pc/logo/brand2.jpg" alt="알퐁소" /></span>
<!-- 								<span class="name">알퐁소</span> -->
							</a>
						</li>
						<li>
							<a href="javascript:brand.template.main('${fourlads}');">
								<span class="img"><img src="/resources/img/pc/logo/brand3.jpg" alt="포래즈:" /></span>
<!-- 								<span class="name">포래즈:</span> -->
							</a>
						</li>
						<li>
							<a href="javascript:brand.template.main('${skarbarn}');">
								<span class="img"><img src="/resources/img/pc/logo/brand4.jpg" alt="섀르반" /></span>
<!-- 								<span class="name">섀르반</span> -->
							</a>
						</li>
						<li>
							<a href="javascript:brand.template.main('${royal}');">
								<span class="img"><img src="/resources/img/pc/logo/brand5.jpg" alt="궁중비책" /></span>
<!-- 								<span class="name">궁중비책</span> -->
							</a>
						</li>
						<li>
							<a href="javascript:brand.template.main('${tommeetippe}');">
								<span class="img"><img src="/resources/img/pc/logo/brand6.jpg" alt="토미티피" /></span>
<!-- 								<span class="name">토미티피</span> -->
							</a>
						</li>
						<li>
							<a href="javascript:brand.template.main('${chooze}');">
								<span class="img"><img src="/resources/img/pc/logo/brand7.jpg" alt="츄즈" /></span>
<!-- 								<span class="name">츄즈</span> -->
							</a>
						</li>
						<li>
							<a href="javascript:brand.template.main('${yvol}');">
								<span class="img"><img src="/resources/img/pc/logo/brand8.jpg" alt="Y-VOLUTION" /></span>
<!-- 								<span class="name">와이볼루션</span> -->
							</a>
						</li>
					</ul>
				</div>
			</li>
			<!-- //브랜드 목록 -->
		</ul>
	</div>
	<!-- ### //Gnb ### -->
</div>
