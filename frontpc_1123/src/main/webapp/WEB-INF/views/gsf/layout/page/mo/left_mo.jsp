<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ page session="true"%>

<script type="text/javascript">
	$(document).ready(function(){
		if(${isMobile}) {

			var name = '${loginName}'; 
			dmsmb.lnb.nameLengthAdjust(name, ${isMobile}, ${isApp});	// 고객명 길이 조절
			
			if(${isApp}) {	// 앱 쇼핑 알림 보관함 카운트
				dmsmb.lnb.alarmCnt();	
			}
		}
	});
</script>

<!-- ### 모바일 좌측 메뉴 ### -->
<div class="dim_fixed"></div>
<div class="all_menu">
	<div class="box">
		<!-- ### 유저 기본 정보 ### -->
		<div class="user">
			<c:choose>
				<c:when test="${loginId != '' && loginId != null}">
					<!-- 로그인 후 -->
					<div class="level">
<!-- 						<img src="/resources/img/mobile/icon/lv5.png" alt="" /> -->
						<c:if test="${memGradeCd eq 'MEM_GRADE_CD.VIP'}">
							<img src="/resources/img/mobile/icon/lv_vip.png" alt="VIP" />
						</c:if>
						<c:if test="${memGradeCd eq 'MEM_GRADE_CD.GOLD'}">
							<img src="/resources/img/mobile/icon/lv_gold.png" alt="GOLD" />
						</c:if>
						<c:if test="${memGradeCd eq 'MEM_GRADE_CD.SILVER'}">
							<img src="/resources/img/mobile/icon/lv_silver.png" alt="SILVER" />
						</c:if>
						<c:if test="${memGradeCd eq 'MEM_GRADE_CD.FAMILY'}">
							<img src="/resources/img/mobile/icon/lv_family.png" alt="FAMILY" />
						</c:if>
						<c:if test="${memGradeCd eq 'MEM_GRADE_CD.WELCOME'}">
							<img src="/resources/img/mobile/icon/lv_welcome.png" alt="WELCOME" />
						</c:if>
						
						<em id="loginName">${loginName } 님</em>
					</div>
					<div class="alarm_setup">
						<!-- <a href="javascript:void(0);" class="btn_login" onclick="javascript:logout()">로그아웃</a> -->
 						<c:if test="${isApp}">
							<div class="alarm">
								<span onclick="common.pageMove('${pageScope.id}','','/mms/mypage/shoppingAlarm')">새소식</span> <em id="alarmCnt"></em>
							</div>
							<a href="app://setting" class="btn_setup">설정</a>
 						</c:if>
					</div>
					<!-- 로그인 후 -->
				</c:when>
				<c:otherwise>
					<!-- 로그인 전 -->
					<div class="alarm_login">
						<a href="javascript:void(0);" onclick="ccs.link.login()" class="btn_login">로그인</a>
						<script>
						var loginTempMo = function(){
			
							$.ajax({
								url : "/api/mms/login/temp/ajax",
								type : "POST"		
							}).done(function(response){
								if(response){
									alert("로그인"+response.loginId);
									location.href="/ccs/common/main";
								}else{
									location.href="/ccs/common/main";	
								}
								//alert(msg.response.message);		
							})
						}
						</script>
						<a href="javascript:void(0);" onclick="javascript:loginTempMo()" class="btn_login">임시로그인</a>
					</div>
					<div class="alarm_setup">
						<c:if test="${isApp}">
							<a href="app://setting" class="btn_setup">설정</a>
						</c:if>
					</div>
					<!-- 로그인 전 -->
				</c:otherwise>
			</c:choose>
		</div>
		<!-- ### //유저 기본 정보 ### -->

		<!-- ### 카테고리, 월령, 브랜드 메뉴 ### -->
		<ul class="tabBox tp1">
			<li class="on"><a href="#">카테고리</a></li>
			<li ><a href="#" class="btn_brandLeft">브랜드</a></li>
		</ul>
		<!-- ### 카테고리, 월령, 브랜드 메뉴 ### -->

		<!-- ### 카테고리 ### -->
		<div class="tab_con tab_conOn">
			<div class="category">
				<ul class="item" id="mbCategory">
					
				</ul>
				<strong class="tit2">월령별</strong>
				<ul class="month" id="ageCode">
					
				</ul>

				<strong class="tit2">주요서비스</strong>
				<ul class="pro_zone">
					<li>
						<a href="javascript:void(0);" onclick="ccs.link.special.premium();">프리미엄멤버십관</a>
					</li>
					<li>
						<a href="javascript:void(0);" onclick="ccs.link.special.subscription();">정기배송관</a>
					</li>
					<li>
						<a href="javascript:void(0);" onclick="ccs.link.special.giftShop();">기프트샵</a>
					</li>
					<li>
						<a href="javascript:void(0);" onclick="ccs.link.special.birthready();">출산준비관</a>
					</li>
					<li>
						<a href="javascript:void(0);" onclick="ccs.link.special.milkPowder();">분유관</a>
					</li>
					<li>
						<a href="javascript:void(0);" onclick="ccs.link.special.multiChildrenInfo();">다자녀우대관</a>
					</li>
					<c:if test="${ employeeYn eq 'Y'}">
						<li>
							<a href="javascript:void(0);" onclick="ccs.link.special.employee();">임직원관</a>
						</li>
					</c:if>
				</ul>

				<div class="etc_menu">
					<ul>
						<li class="btn_shop">
							<a href="/ccs/offshop/search">
								<span>매장찾기</span>
							</a>
						</li>
						<li class="btn_cs">
							<a href="/ccs/cs/main">
								<span>고객센터</span>
							</a>
						</li>
						<c:choose>
							<c:when test="${loginId != '' && loginId != null}">
								<li class="btn_myorder">
									<a href="javascript:void(0);" onclick="ccs.link.mypage.main()">
										<span>마이쇼핑</span>
									</a>
								</li>
							</c:when>
							<c:otherwise>
								<li class="btn_order">
									<a href="javascript:void(0);" onclick="mms.common.nonMemberLoginLayer()">
										<span>비회원 주문조회</span>
									</a>
								</li>
							</c:otherwise>
						</c:choose>
					</ul>
				</div>
			</div>
		</div>
		<!-- ### //카테고리 ### -->

		<!-- ### 브랜드 ### -->
		<div class="tab_con" id="searchLeftBrand">
			<div class="brand">
				<div class="inputTxt_place1 search" >
					<label>브랜드명을 입력하세요.</label>
					<span>
						<input type="text" id="brandKeyword" value="" />
						<input type="button" value="검색" class="btn_search" onClick="dmsmb.lnb.brand.search('');"/>
					</span>
				</div>

				<div class="choice_brand">
					<a href="javascript:void(0);" class="btn_my_brand btn_brandOn" onClick="dmsmb.lnb.brand.hideSearch();" class="btn_my_brand btn_brandOn">관심 브랜드</a>
					<a href="javascript:void(0);" class="btn_abc">ABC</a>

					<div class="sort_list swiper-container brandMenuSwiper_sortingWord_kr">
						<!-- 한글 자음 -->
						<ul class="sorting swiper-wrapper" id="consonantList">
						
						</ul>
					</div>
					<div class="sort_list swiper-container brandMenuSwiper_sortingWord_en">
						<!-- 영어 스펠링 -->
						<ul class="sorting swiper-wrapper" id="spellingList">
						
						</ul>
					</div>
				</div>
				
				<div class="view_brand like_brand">
					<a href="javascript:void(0);" class="tit1">관심 브랜드</a>
					<ul id="likeBrandResult">
					</ul>
				</div>
				<%-- <span id="likeBrandResult"></span>
				ajax 영역 --%>
		
				<span id="searchBrandResult"></span>
				<%-- ajax 영역 --%>
				
			</div>
		</div>
		<!-- ### //브랜드 ### -->
		<button type="button" class="btn_all_close">전체메뉴 닫기</button>
	</div>
</div>
<!-- ### //모바일 좌측 메뉴 ### -->

