<%--
	화면명 : 마이페이지 > 나의혜택 > 쿠폰
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<script type="text/javascript">
$(document).ready(function(){
	mypage.coupon.search("coupon", "ONLINE");
});

var chgSelect = function (param) {
	mypage.coupon.search("else",param.value);
}

if(ccs.common.mobilecheck()) {
	/* 모바일 스크롤 제어*/
	$(window).bind("scroll", productListScrollListener);
	
	function productListScrollListener() {
		var rowCount = $(".coupon_list").find("li").length;
		var totalCount = 0;

		if($("select option:selected").val() == '') {
			// 온라인/ 오프라인 쿠폰유형에 따른 전체개수 세팅 (스크롤)
			if($(".sortBox").find('li.active').get(0).id == "ONLINE") {
				totalCount = Number($("[name=item1]").val());
			} else {
				totalCount = Number($("[name=item2]").val());
			}
		} else {
			// 쿠폰 유형별 리스팅은 각 리스트 전체 카운트로 세팅
			totalCount = Number($("[name=eachCount]").val());
		}
		
		var maxPage = Math.ceil(totalCount/10);
		
		var scrollTop = $(window, document).scrollTop();
		var scrollHeight = $(document).height() - $(window).height();
		
		if (scrollTop >= scrollHeight - 200 && scrollHeight != 0) {
			if(rowCount !=0 && (rowCount < totalCount)){
				
				if ($("#lodingBar").length > 0 ) {
					return;
				}
				mypage.coupon.search(null, null, true); 
			}
		}
	}

	function setEvent() {
		if (!$(".coupon_list").eq(0).hasClass("block")) {
			$(".coupon_list").each(function(index) {
				
				if (index != 0) {
					$(this).toggleClass("block");
				}
			});
		}
	}
}
</script>
<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY혜택관리|쿠폰" name="pageNavi"/>
</jsp:include>
<input type="hidden" name="currentPage" value="1" />
<div class="inner">
<div class="layout_type1">
<div class="column">
	<div class="mypage mycoupon">
		<h3 class="title_type1">쿠폰</h3>
		<div class="borderBox">
			<dl>
				<dt>나의 할인쿠폰</dt>
				<dd>
					<strong id="mainCnt">${couponInfo.useCoupon + offshopUseCoupon + 0 }</strong>
					<span>장</span>
				</dd>
			</dl>
			<p><span>7일 이내 종료 예정 쿠폰 <em id="mainEndCnt">${couponInfo.endCoupon + offshopEndCoupon + 0 }</em>장</span></p>
		</div>

		<div class="couponNow">
			<span>이달의 멤버십쿠폰
				<c:if test="${memGradeCd eq 'MEM_GRADE_CD.VIP'}">
					<em>11</em>
				</c:if>
				<c:if test="${memGradeCd eq 'MEM_GRADE_CD.GOLD'}">
					<em>8</em>
				</c:if>
				<c:if test="${memGradeCd eq 'MEM_GRADE_CD.SILVER'}">
					<em>6</em>
				</c:if>
				<c:if test="${memGradeCd eq 'MEM_GRADE_CD.FAMILY'}">
					<em>6</em>
				</c:if>
				<c:if test="${memGradeCd eq 'MEM_GRADE_CD.WELCOME'}">
					<em>4</em>
				</c:if> 
			장</span>
			<a href="javascript:void(0);" class="btn_sStyle1 sWhite2" onclick="javascript:ccs.link.mypage.benefit.membership();">쿠폰받기</a>
		</div>

		<dl class="srchBox">
			<dt>쿠폰 등록</dt>
			<dd>
				<div class="inpBox">
					<div class="inputTxt_place1">
						<label>쿠폰 인증번호를 입력하세요.</label>
						<span>
							<input type="text" id="privateCin" value="" maxlength="14" />
						</span>
					</div>
					<a href="javascript:void(0);" class="btn_sStyle4 sPurple1 btnSrch" onclick="javascript:mypage.coupon.issueCoupon()">등록</a>
				</div>

				<p>발급 받으신 쿠폰 인증번호를 입력하시면, 할인쿠폰 혹은 적립금이 즉시 지급됩니다.</p>
			</dd>
		</dl>
		
		<div class="sortPkg">
			<ul class="sortBox">
<!-- 				<li class="active" onclick="javascript:mypage.coupon.search(this)" value="ALL"> -->
<%-- 					<a href="javascript:void(0);" ><span>전체</span><em id="totalCnt">(${couponInfo.useCoupon + couponInfo.useCouponOff + 0 })</em></a> --%>
<!-- 				</li> -->
				<li class="active" onclick="javascript:mypage.coupon.search(this,'ONLINE')" id="ONLINE" value="ONLINE">
					<a href="javascript:void(0);"><span>온라인쿠폰</span><em id="item1Cnt">(${couponInfo.useCoupon + 0 })</em></a>
				</li>
				<li onclick="javascript:mypage.coupon.search(this,'OFFLINE')" id="OFFLINE" value="OFFLINE">
					<a href="javascript:void(0);"><span>매장쿠폰</span><em id="item2Cnt">(${offshopUseCoupon + 0 })</em></a>
				</li>
			</ul>
			<div class="posR" style="display: none;">
				<div class="select_box1 radiusSel">
					<label id="couponTypeLabel">전체</label>
					<tags:codeList code="COUPON_TYPE_CD" optionHead="전체"/>
				</div>
			</div>
		</div>
		
		<%-- 쿠폰 ajax --%>
		<div id="couponContent">
			<div class="coupon_outer">
				<ul class="coupon_list">
					<c:if test="${!isMobile }">
						<img src="/resources/img/mobile/Loading.gif" alt="" 
						style="display: block; margin-left: auto; margin-right: auto; padding-top: 170px; padding-bottom: 170px;"/>
					</c:if>
				</ul>
			</div>
		</div>
		<%-- // 쿠폰 ajax --%>
		
		<ul class="notice">
			<li>사용 전 각 할인쿠폰의 사용조건을 확인해 주세요.</li>
			<li>주문 취소시 사용하신 쿠폰은 자동복원 처리되나, 쿠폰의 유효기간이 종료 된 경우  소멸 처리되어 복원되지 않습니다.</li>
			<li>쿠폰은 유효기간 종료시  자동소멸 됩니다.</li>
		</ul>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
</div>
</div>

