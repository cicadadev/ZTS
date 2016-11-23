<%--
	화면명 : 마이페이지 > 메인
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%-- <%@ taglib prefix="func" uri="kr.co.intune.commerce.common.functions" %> --%>
<script type="text/javascript" src="/resources/js/common/mypage.ui.js"></script>
<script type="text/javascript">

// $(".pc .rolling_box .paginate button").off("click").on({
// 	"click" : function() {
// 		var num = fnListConEa( $(this).parent().siblings(".product_type1") );

// 		fnListControl( $(this).closest(".rolling_box"), $(this), num );
// 	}
// });

$(document).ready(function(){

	mypage.main.offcouponCnt('${member.cpnIssueCnt}');
	
	if(!${isMobile}) {
		mypage.main.latestOrder();	
		mypage.main.wishList();	
		mypage.main.cartList();	
	} else {
		var name = '${member.mmsMember.memberName }';
		mypage.main.nameLengthAdjust(name, ${isMobile}, ${isApp});	// 고객명 길이 조절
	}
	
});

</script>

	<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
		<jsp:param value="마이쇼핑" name="pageNavi"/>
	</jsp:include>
	
<div class="inner">
<div class="layout_type1 mypageMain">
<div class="column">
	<div class="mypageM">
		<div class="myInfoPkg">
			<div class="inner_box">
				<div class="level">
					<span class="icon_lv">
						<c:choose>
							<c:when test="${isMobile }">
								<c:if test="${member.memGradeCd eq 'MEM_GRADE_CD.VIP'}">
									<img src="/resources/img/mobile/txt/lv_vip.png" alt="VIP" />
								</c:if>
								<c:if test="${member.memGradeCd eq 'MEM_GRADE_CD.GOLD'}">
									<img src="/resources/img/mobile/txt/lv_gold.gif" alt="GOLD" />
								</c:if>
								<c:if test="${member.memGradeCd eq 'MEM_GRADE_CD.SILVER'}">
									<img src="/resources/img/mobile/txt/lv_silver.gif" alt="SILVER" />
								</c:if>
								<c:if test="${member.memGradeCd eq 'MEM_GRADE_CD.FAMILY'}">
									<img src="/resources/img/mobile/txt/lv_family.gif" alt="FAMILY" />
								</c:if>
								<c:if test="${member.memGradeCd eq 'MEM_GRADE_CD.WELCOME'}">
									<img src="/resources/img/mobile/txt/lv_welcome.gif" alt="WELCOME" />
								</c:if>
							</c:when>
							<c:otherwise>
<%-- 								<c:if test="${member.memGradeCd eq 'MEM_GRADE_CD.PRESTAGE'}"> --%>
<!-- 									<img src="/resources/img/pc/txt/lv_prestage.gif" alt="PRESTAGE" /> -->
<%-- 								</c:if> --%>
								<c:if test="${member.memGradeCd eq 'MEM_GRADE_CD.VIP'}">
									<img src="/resources/img/pc/txt/lv_vip.png" alt="VIP" />
								</c:if>
								<c:if test="${member.memGradeCd eq 'MEM_GRADE_CD.GOLD'}">
									<img src="/resources/img/pc/txt/lv_gold.png" alt="GOLD" />
								</c:if>
								<c:if test="${member.memGradeCd eq 'MEM_GRADE_CD.SILVER'}">
									<img src="/resources/img/pc/txt/lv_silver.png" alt="SILVER" />
								</c:if>
								<c:if test="${member.memGradeCd eq 'MEM_GRADE_CD.FAMILY'}">
									<img src="/resources/img/pc/txt/lv_family.png" alt="FAMILY" />
								</c:if>
								<c:if test="${member.memGradeCd eq 'MEM_GRADE_CD.WELCOME'}">
									<img src="/resources/img/pc/txt/lv_welcome.png" alt="WELCOME" />
								</c:if>
							</c:otherwise>
						
						</c:choose>
					</span>
					
					<%-- 회원 이름 --%>
					<span class="txt" id="memberName">
						<em>${member.mmsMember.memberName}</em> 님
					</span>

					<a href="/sps/event/benefit" >혜택보기 &gt;</a>
					<ul class="my_setting mo_only">
						<c:if test="${isApp}">
							<li><a href="javascript:void(0);" class="m1" onclick="common.pageMove('${pageScope.id}','','/mms/mypage/shoppingAlarm')">알림함<span class="circle">${alarmCnt + 0 }</span></a></li>
							<li><a href="app://setting" class="m2">설정</a></li>
						</c:if>
					</ul>
				</div>

				<div class="babyInfo">
					<dl>
						<c:choose>
							<c:when test="${member.babyGenderCd eq 'BABY_GENDER_CD.BOY' }">
								<dt>우리아이 <span class="gender mo_only">남</span></dt>
							</c:when>
							<c:otherwise>
								<dt>우리아이 <span class="gender mo_only">여</span></dt>
							</c:otherwise>
						</c:choose>
						<dd>
							<c:choose>
								<%-- 자녀여부 Y or READY --%>
								<c:when test="${member.babyYnCd ne 'BABY_YN_CD.N' }">
									<c:if test="${member.babyGenderCd eq 'BABY_GENDER_CD.BOY' }">
										<span class="gender pc_only">남</span>
									</c:if>
									<c:if test="${member.babyGenderCd eq 'BABY_GENDER_CD.GIRL' }">
										<span class="gender pc_only">여</span>
									</c:if>
									<fmt:parseDate value="${member.babyBirthday}" var="birthDay" pattern="yyyy-MM-dd HH:mm:ss"/> 
									<span class="birth"><fmt:formatDate value="${birthDay}"  pattern="yyyy/MM/dd"/> ( ${member.babyMonth } )</span>
								</c:when>
								<%-- 자녀여부 N --%>
								<c:otherwise>
									아이정보를 설정해보세요.
								</c:otherwise>
							</c:choose>
						</dd>
					</dl>
					<a href="javascript:void(0);" onclick="javascript:mypage.main.goInterest();" class="btn_sStyle1 sWhite2 pc_only">아이정보 수정</a>
				</div>
				<div class="menuList">
					<ul>
					
						<li><a href="javascript:void(0);" class="family_point">매일 Family 포인트카드</a></li>
<!-- 						<li><a href="javascript:void(0);" onclick="mms.common.memberUpdatePopup()" class="m1">아이정보 수정</a></li> -->
						<li><a href="javascript:void(0);" onclick="javascript:mypage.main.goInterest()" class="m1">아이정보 수정</a></li>
						<li><a href="javascript:void(0);" class="m2" onclick="common.pageMove('${pageScope.id}','','/mms/mypage/info/myMenu')">마이메뉴 설정</a></li>
					</ul>
				</div>
			</div>
		</div>

		<div class="delivery">
			<h3 class="sub_tit1">주문/배송현황</h3>
			
			<div class="box">
				<c:forEach items="${delivery }" var="delivery" varStatus="status">
					<c:if test="${delivery.deliveryStep eq 'STEP1_ORDER_REQ'}">
						<c:set var="step1" value="${delivery.stepCnt +0}" />
					</c:if>
					<c:if test="${delivery.deliveryStep eq 'STEP2_PAYED'}">
						<c:set var="step2" value="${delivery.stepCnt +0}" />
					</c:if>
					<c:if test="${delivery.deliveryStep eq 'STEP3_PRODUCT_READY'}">
						<c:set var="step3" value="${delivery.stepCnt +0}" />
					</c:if>
					<c:if test="${delivery.deliveryStep eq 'STEP4_DELIVERY'}">
						<c:set var="step4" value="${delivery.stepCnt +0}" />
					</c:if>
					<c:if test="${delivery.deliveryStep eq 'STEP5_DELIVERY_COMPLETE'}">
						<c:set var="step5" value="${delivery.stepCnt +0}" />
					</c:if>
				</c:forEach>
				<div class="step1">
					<a href="/mms/mypage/order/history">
						<span class="${step1 > 0 ? 'on' : '' }">${step1 + 0 }</span>
						<strong>주문접수</strong>
					</a>
				</div>
				<div class="step2">
					<a href="/mms/mypage/order/history">
						<span class="${step2 > 0 ? 'on' : '' }">${step2 + 0 }</span>
						<strong>결제완료</strong>
					</a>
				</div>
				<div class="step3">
					<a href="/mms/mypage/order/history">
						<span class="${step3 > 0 ? 'on' : '' }">${step3 + 0 }</span>
						<strong>상품준비중</strong>
					</a>
				</div>
				<div class="step4">
					<a href="/mms/mypage/order/history">
						<span class="${step4 > 0 ? 'on' : '' }">${step4 + 0 }</span>
						<strong>배송중</strong>
					</a>
				</div>
				<div class="step5">
					<a href="/mms/mypage/order/history">
						<span class="${step5 > 0 ? 'on' : '' }">${step5 + 0 }</span>
						<strong>배송완료</strong>
					</a>
				</div>
			</div>
			<a href="/mms/mypage/order/history" class="btn_more">전체보기</a>
		</div>

		<div class="benefitMenu">
			<dl class="m1">
				<dt>매일포인트</dt>
				<dd>
					<a>
						<span>
							<em><fmt:formatNumber value="${memberpoint.rmndPint + 0}" pattern="###,###" /></em> P
						</span>
					</a>
				</dd>
			</dl>

			<dl class="m2">
				<dt>예치금</dt>
				<dd >
					<a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.benefit.deposit();">
						<span >
							<em><fmt:formatNumber value="${member.depositBalanceAmt +0}" pattern="###,###" /></em> 원
						</span>
					</a>
				</dd>
			</dl>

			<dl class="m3">
				<dt>쿠폰</dt>
				<dd>
					<a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.benefit.coupon();">
						<span id="couponTotal">
						<c:if test="${isMobile }">
							<img src="/resources/img/mobile/Loading.gif" alt="" height="25px;" width="25px;" 
								style="display: block; margin-left: auto; margin-right: auto; padding-bottom: 9px;" />
						</c:if>
						<c:if test="${!isMobile }">
							<img src="/resources/img/mobile/Loading.gif" alt="" height="25px;" width="25px;" 
								style="display: block; margin-left: auto; margin-right: auto; padding-top: 10px;" />
						</c:if>
						</span>
					</a>
				</dd>
			</dl>

			<dl class="m4">
				<dt>당근</dt>
				<dd >
					<a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.benefit.carrot();">
						<span >
							<em><fmt:formatNumber value="${member.carrotBalanceAmt +0}" pattern="###,###" /></em> 개
						</span>
					</a>
				</dd>
			</dl>
		</div>

		<ul class="menuList2">
			<li class="m1">
				<a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.activity.wishlist();">쇼핑찜</a>
			</li>
			<li class="m2">
				<a href="javascript:void(0);" onclick="javascript:ccs.link.mypage.activity.history();">히스토리</a>
			</li>
		</ul>

		<%-- 최근 7일 주문내역 --%>
		<div class="recentList">
			<h3 class="sub_tit1">최근 7일 주문내역</h3>
			<a href="/mms/mypage/order/history" class="btn_all">전체보기</a>
			<div class="tbl_article">
				<div class="div_tb_thead3">
					<div class="tr_box">
						<span class="col1">주문일시 / 주문번호</span>
						<span class="col2">상품명</span>
						<span class="col3">결제금액</span>
						<span class="col4">주문 / 배송현황</span>
					</div>
				</div>
				<ul class="div_tb_tbody3">
					<li class="empty" style="height: 60px;">
						<div class="tr_box">
							<img src="/resources/img/mobile/Loading.gif" alt="" height="50px;" width="50px;" 
							style="display: block; margin-left: auto; margin-right: auto;"/>
						</div>
					</li>
				</ul>
			</div>
		</div>
		<%-- // 최근 주문내역 --%>

		<div class="listArea">
			<%-- 쇼핑찜 --%>
			<div class="rolling_box" id="areaWishList">
				<h3 class="sub_tit1">쇼핑찜 <em>(0)</em></h3>
				<a href="javascript:void(0);" class="btn_all" >더보기</a>
				<p class="empty">
					<img src="/resources/img/mobile/Loading.gif" alt="" height="50px;" width="50px;"
					style="display: block; margin-left: auto; margin-right: auto; padding-top: 100px;"/>
				</p>
			</div>
			<%-- // 쇼핑찜 --%>

			<%-- 장바구니 --%>
			<div class="rolling_box" id="areaCartList">
				<h3 class="sub_tit1">장바구니 담은 상품 <em>(0)</em></h3>
				<a href="javascript:void(0);" class="btn_all" >더보기</a>
				<p class="empty">
					<img src="/resources/img/mobile/Loading.gif" alt="" height="50px;" width="50px;" 
					style="display: block; margin-left: auto; margin-right: auto; padding-top: 100px;"/>
				</p>
			</div>
			<%-- // 장바구니 --%>
		</div>
	</div>
</div>
<!-- 마이페이지 메뉴 -->
<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" >
	<jsp:param value="Y" name="isMain"/>
</jsp:include>
</div>
</div>

<!-- ### 16.09.02 팝업 - 매일포인트 바코드 ### -->
<div class="layer_style1 sLayer_maeilpoint">
	<div class="box">
		<div class="conArt">
			<strong class="title"><img src="/resources/img/mobile/bg/bg_point.png" style="width:27px"> 매일 Family 포인트 카드</strong>
			<div class="conBox">
				<div class="maeilpointCard">
					<div class="cardBarcode">
						<img alt="" src="/api/ccs/common/barcode/8753010224196484" />
						<p class="mCardNum">8753 0102 2419 6484</p>
					</div>
					<dl class="cardPoint">
						<dt>사용가능 매일포인트</dt>
						<dd>
							<span>
								<em><fmt:formatNumber value="${memberpoint.rmndPint + 0}" pattern="###,###" /></em>P
							</span>
						</dd>
					</dl>
				</div>
			</div>
		</div>
		<button type="button" class="btn_close">레이어팝업 닫기</button>
	</div>
</div>
<!-- ### //16.09.02 팝업 - 매일포인트 바코드 ### -->
