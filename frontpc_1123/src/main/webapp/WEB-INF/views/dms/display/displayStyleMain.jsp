<%--
	화면명 : 스타일메인
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script type="text/javascript" src="/resources/js/mms/mms.mypage.js"></script>

<script type="text/javascript">
$(document).ready(function() {
	$(".evt_tit").off("click").on("click", function(e){
		$(this).parent().toggleClass("slideHide");
	});
	display.style.getStyleList();
	
	var styleNo = '${search.styleNo}';
	var memberNo = '${search.memberNo}';
	
	if (styleNo != '' && styleNo != null) {
		if (ccs.common.mobilecheck()) {
			location.href = "/dms/display/styleDetail?styleNo="+memberNo + "&memberNo=" + memberNo;
		} else {
			display.style.styleDetailLayer(styleNo, memberNo);
		}
	}
	
	$("input[name=ra1_3]").off("click").on({
		"click" : function() {
			display.style.getStyleList("","","",$(this).val());
		}
	});
	
	// SNS 링크일경우 
	var layerStyleNo = '${styleDetail.styleNo}';
	if (layerStyleNo != undefined && layerStyleNo != '' && layerStyleNo != null) {
		display.style.styleDetailLayer(layerStyleNo, '${style.memberNo}');
	}
	
});
</script>
	
	<c:choose>
	<c:when test="${isMobile ne 'true' }">
		<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
			<jsp:param value="스타일" name="pageNavi"/>
		</jsp:include>	
	</c:when>
	<c:otherwise>	
		<jsp:include page="/WEB-INF/views/gsf/layout/page/mo/title_menu.jsp" flush="false" >
			<jsp:param name="titleMenu" value="8" />
		</jsp:include>
	</c:otherwise>	
	</c:choose>


<div class="mainCon">
	<div class="swiper-wrapper">
		<div class="inner main5">
			<div class="style_shop">
				<!-- 16.10.23 -->
				<div class="visual one">
					<ul>	
						<li>
							<!-- pc 이미지 -->
							<img src="/resources/img/pc/bg/bg_style_shop.jpg" alt="스타일샵 이젠 입혀보고 사자! 고민되는 순간 눈으로 확인하세요!" class="pc_only" />

							<!-- mobile 이미지 -->
							<div class="img_outer mo_only">
								<img src="/resources/img/mobile/bg/bg_style_shop.jpg" alt="스타일샵" />
									<c:if test="${isApp && loginId != null && loginId != ''}">
										<a href="javascript:mypage.style.makeAndModifyStyle('', '${loginId}');" class="link_styleshop">스타일 만들기</a>
									</c:if>
								<span class="link_how left">
									<a href="#none" class="btn_styleHow">스타일 어떻게 이용하나요?</a>
								</span>
							</div>
						</li>
					</ul>
				</div>

				<div class="step styleshopHow pc_only">
					<h3 class="slide_tit1 slideHide"><span class="evt_tit">스타일샵은 어떻게 이용하나요?</span></h3>
					<div class="stepCont">
						<ul class="notice">
							<li>스타일 만들기는 APP에서 가능합니다.</li>
							<li>마음에 드는 회원의 코디에 좋아요를 눌러서 공감해주세요.</li>
							<li>완료된 스타일은 친구, 지인에게 SNS로 공유하여 추천해보아요.</li>
						</ul>

						<ul class="stepTxt">
							<li>
								<span class="step01">Step 01</span>
								<strong>스타일 만들기</strong>
								<em>월령, 성별로 대상 모델을<br />선택하여 주세요.</em>
							</li>
							<li>
								<span class="step02">Step 02</span>
								<strong>아이템 가져오기</strong>
								<em>카테고리, 선호하는 브랜드, 꾸미기 등 <br />다양한 분류로 마음에 드는 아이템을 <br />선택하여 코디해보세요.</em>
							</li>
							<li>
								<span class="step03">Step 03</span>
								<strong>등록하기</strong>
								<em>코디팁을 작성하여 등록합니다.<br />상세분류 지정이 가능합니다. <br>강조하고 싶은 코디 내용을 작성하여<br />공유 해보세요.</em>
							</li>
							<li>
								<span class="step04">Step 04</span>
								<strong>혜택받기</strong>
								<em>나만의 코디를 만들어 공유하고<br /> 이를 통해 고객판매가 이루어질 시 <br />혜택을 드립니다. 다양한 이벤트, <br />기획전에 참여해 보세요.<br /> 선물이 기다립니다.</em>
							</li>
						</ul>
					</div>
				</div>

				<!-- ### 배너 영역 ### -->
				<div class="bnr_mid">
					<div class="bnr_mid_owl">
						<c:if test="${not empty bannerList[0].dmsDisplayitems[0].img1}">
							<c:forEach var="item" items="${bannerList[0].dmsDisplayitems}" varStatus="i">
								<div class="item">
									<a href="${item.url1}">
										<img src="${_IMAGE_DOMAIN_}${item.img1}" alt="" class="img_pc" />
									</a>
								</div>
							</c:forEach>
						</c:if>
					</div>
				</div>
				<!-- ### //배너 영역 ### -->
				<!-- //16.10.23 -->

				<div class="filterSorting">
					<ul>
						<li class="box_01">
							<div class="select_box1">
								<label>성별</label>
								<tags:codeList code="GENDER_TYPE_CD" var="gender" tagYn="N" />
								<select onchange="javascript:display.style.changeFilter($(this));" id="sel_gender">
									<option value="">성별</option>
									<c:forEach items="${gender}" var="gender">
										<option value="${gender.cd}">${gender.name}</option>
									</c:forEach>
								</select>
							</div>
							<div class="select_box1">
								<label>테마</label>
								<tags:codeList code="THEME_CD" var="theme" tagYn="N"/>
								<select onchange="javascript:display.style.changeFilter($(this));" id="sel_theme">
									<option value="">테마</option>
									<c:forEach items="${theme}" var="theme">
										<c:if test="${theme.cd ne 'THEME_CD.ETC'}">
											<option value="${theme.cd}">${theme.name}</option>
										</c:if>
									</c:forEach>
								</select>
							</div>
							<div class="select_box1">
								<label>브랜드</label>
								<tags:codeList code="BRAND_CD" var="brand" tagYn="N"/>
								<select onchange="javascript:display.style.changeFilter($(this));" id="sel_brand">
									<option value="">브랜드</option>
									<c:forEach items="${brand}" var="brand">
										<option value="${brand.cd}">${brand.name}</option>
									</c:forEach>
								</select>
							</div>
						</li>
						<li class="box_02 pc_only">
							<div class="select_box1 radiusSel">
								<label>인기순</label>
								<select onchange="javascript:display.style.changeFilter($(this));" id="sel_sort">
									<option value="popular">인기순</option>
									<option value="new">최신순</option>
								</select>
							</div>
						</li>
						<li class="box_02 mo_only">
							<label class="radio_style1 option_style1">
								<em class="selected">
									<input type="radio" name="ra1_3" value="popular" checked="checked">
								</em>
								<span>인기순</span>
							</label>
							<label class="radio_style1 option_style1">
								<em>
									<input type="radio" name="ra1_3" value="new">
								</em>
								<span>최근등록순</span>
							</label>
						</li>
					</ul>
				</div>
				
				<!-- 스타일 리스트 -->		
				<div id="stylist_div" class="stylist_div">
		
				</div>
				
			</div>
		</div>
	</div>
</div>	
<script>
//16.10.23 메인 스타일 중간 배너 스와이프
if($('.pc .bnr_mid_owl').length>0){
	$('.bnr_mid_owl').owlCarousel({
		items:1,
		loop:true,
		dots:false,
		nav:true,
		margin:0
	})
}


</script>