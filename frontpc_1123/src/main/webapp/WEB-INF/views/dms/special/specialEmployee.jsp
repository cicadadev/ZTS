<%--
	화면명 : 프론트 & 모바일  임직원관
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ page import="intune.gsf.common.utils.Config" %>

<%
	pageContext.setAttribute("top", Config.getString("corner.special.emp.img.1"));
%>

<c:choose>
	<c:when test="${isMobile}">
		<script type="text/javascript" src="/resources/js/common/display.ui.mo.js"></script>
	</c:when>
	<c:otherwise>
		<script type="text/javascript" src="/resources/js/common/display.ui.pc.js"></script>
	</c:otherwise>
</c:choose>

<script type="text/javascript">
$(document).ready(function(){

	$('div.sortBoxList > ul > li').hide();
	$('.productSort').show();
	$('.listType').show();

	if("<c:out value='${!isMobile}' />") {
		$('.pageSize').show();	
	}
	
	//옵션 선택 박스 이벤트
	$(".optionBox .colorList").find("a").off("click").on("click", function(e){
		$(this).toggleClass("active");
	});

	if(ccs.common.mobilecheck()) {

		var _length = $("#depth1title > li").length;
		if(_length > 6) {
			_length = 5;
		} else if(_length < 0) {
			_length = 1;
		}
		
		swiperCon('employeeSwiper_categoryList', _length); //배너
	} else {
		swiperCon('employeeSwiper_categoryList', 400, 6, 0, false, false);
	}

});

if(ccs.common.mobilecheck()) {
	
	/* 모바일 스크롤 제어*/
	$(window).bind("scroll", productListScrollListener);
	
	function productListScrollListener() {
		var rowCount = $(".product_type1").find("li").length;
		var totalCount = Number($("[name=totalCount]").val());
		var maxPage = Math.ceil(totalCount/10);
		
		var scrollTop = $(window, document).scrollTop();
		var scrollHeight = $(document).height() - $(window).height();

		if (scrollTop >= scrollHeight - 200 && scrollHeight != 0) {
			if(rowCount !=0 && (rowCount < totalCount)){
				
				if ($("#tempLoadingBar").length > 0 ) {
					return;
				}
				special.employee.loadDepthProduct(null, null, null, true); 
			}
		}
	}
}

</script>
<c:set var="topBnr"  value="${cornerMap[top]}"/>
	<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
		<jsp:param value="전문관|임직원관" name="pageNavi"/>
	</jsp:include>
	
	<form id="valueForm" name="valueForm">
		<input type="hidden" id="dept1_Id" name="upperDealGroupNo" value=""/>
		<input type="hidden" id="dept2_Id" name="dealGroupNo" value=""/>
		<input type="hidden" id="sortType" name="sortType" value="${productSortTypeCd }"/>
	</form>
	<input type="hidden" id="pageSize" name="pageSize" value=""/>
	<input type="hidden" name="totalCount" value="${totalCount}"/>
	<input type="hidden" name="currentPage" value="1" />
	
	<div class="inner">
		<div class="pro_box pro_proBox1">
		
			<!-- 16.10.05 : 상단 비주얼 -->
			<div class="visual one">
				<ul>
					<c:forEach var="item" items="${topBnr.dmsDisplayitems}" varStatus="status">
<%-- 						<li ${status.index ==0 ? 'class="on"' : '' }> --%>
						<li class="on">
							<span class="bg" style="background-color:#8bd7ff;">임직원관</span>  <!-- 16.10.22 : bg 추가 -->
							<div class="img_outer">
							<c:choose>
								<c:when test="${isMobile}">
									<img src="${_IMAGE_DOMAIN_}${item.img2}" alt="${item.text2}" />
								</c:when>
								<c:otherwise>
									<img src="${_IMAGE_DOMAIN_}${item.img1}" alt="${item.text1}" />		
								</c:otherwise>
							</c:choose>
							</div>
						</li>
					</c:forEach>
				</ul>
<!-- 				<div class="control"> -->
<!-- 					<div class="dots"> -->
<!-- 						<span class="on"></span> -->
<!-- 						<span></span> -->
<!-- 					</div> -->
<!-- 					<div class="btns"> -->
<!-- 						<button type="button" class="prev">이전</button> -->
<!-- 						<button type="button" class="next">다음</button> -->
<!-- 					</div> -->
<!-- 				</div> -->
				<!-- <div class="mb_page">
					<div class="pageInner">
						<i>1</i> / <em>0</em>
						<button type="button">전체보기</button>
					</div>
				</div> -->
			</div>
			<!-- //16.10.05 : 상단 비주얼 -->
		
			<ul class="txt_pro">
				<li>
					레뱅드 매일 와인 주문 및 상품문의는  02-2112-2935 전화 부탁 드립니다.
				</li>
			</ul>

			<div class="tabFixW">
				<div class="tabFix">
					<div class="mainSwiper_tab">
						<div class="swiper_wrap">
							<div class="tab_outer swipeMenu swiper-container employeeSwiper_categoryList">
								<ul class="tabBox swiper-wrapper" id="depth1title">
									<li class="swiper-slide on"><a href="javascript:void(0);" class="theme1" id="all" onclick="javascript:special.employee.depthClick(this)">전체</a></li>
									<c:forEach var="depth1" items="${depthList }" varStatus="status">
										<li class="swiper-slide">
											<a href="javascript:void(0);" class="theme${status.index+2 }" name="${depth1.name }" onclick="javascript:special.employee.depthClick(this, '${depth1.dealGroupNo}', '', '${status.index }','${fn:length(depth1.spsDealgroups) }');">${depth1.name }</a>
										</li>
									</c:forEach>
								</ul>
								<c:if test="${!isMobile }">
									<!-- Add Arrows -->
								    <div class="swiper-button-next btn_tp6"></div>
								    <div class="swiper-button-prev btn_tp6"></div>
								</c:if>
							</div>
						</div>
					</div>				
				</div>
			</div>
			
			<div class="btnSrchDetail">
				<a href="javascript:void(0);">맞춤정보 상세검색 <span>열기</span></a>
			</div>
			
			<c:choose>
			
				<c:when test="${ !isMobile }">
					<%-- pc 상세 옵션 --%>
					<div class="optionBox detailOptBox"> <!-- 16.10.05 : class(detailOptBox) 추가 -->
				</c:when>
				
				<c:otherwise>
					<%-- mo 상세 옵션 --%>
					<div class="mo_optionSrch">
						<div class="slide_tit1 slideHide">
							<a href="javascript:void(0);" class="evt_tit">맞춤정보 상세검색</a>
						</div>
						<div class="optionBox">
				</c:otherwise>
				
			</c:choose>
			
					<div class="optionItemBox open">
						<div class="optionItem optDetail age">
							<div class="optionTit"><strong>월령</strong></div>
							<div class="optionCont">
								<ul class="txtList">
									<tags:codeList code="AGE_TYPE_CD" var="ageCdList" tagYn="N"/>
									<c:forEach items="${ageCdList}" var="age">
										<li>
											<label class="chk_style1 option_style1">
											<em><input type="checkbox" value="${age.cd}"></em>
											<span>${age.name}</span>
											</label>
										</li>
									</c:forEach>
								</ul>
							</div>
						</div>

						<div class="optionItem optDetail gender">
							<div class="optionTit"><strong>성별</strong></div>
							<div class="optionCont">
								<ul class="txtList">
									<tags:codeList code="GENDER_TYPE_CD" var="genderCdList" tagYn="N"/>
									<c:forEach items="${genderCdList}" var="gender">
										<li>
											<label class="radio_style1 option_style1">
												<em>
													<c:choose>
														<c:when test="${gender.cd eq 'GENDER_TYPE_CD.UNISEX'}">
															<input type="radio" name="inpGender" value="${gender.cd}" checked="checked"/>
														</c:when>
														<c:otherwise>
															<input type="radio" name="inpGender" value="${gender.cd}" />
														</c:otherwise>
													</c:choose>
												</em>
												<span>${gender.name}</span>
											</label>
										</li>
									</c:forEach>
								</ul>
							</div>
						</div>

						<div class="optionItem optDetail price">
							<div class="optionTit"><strong>가격</strong></div>
							<div class="optionCont">
								<div class="select_box1">
									<input type="text" id="prePrice" name="prePrice">
								</div>
								<span class="swung">~</span>
								<div class="select_box1">
									<input type="text" id="postPrice" name="postPrice">
								</div>
							</div>
						</div>

						<div class="optionItem optDetail color">
							<div class="optionTit"><strong>컬러</strong></div>
							<div class="optionCont">
								<ul class="colorList">
									<!-- 160901 : 컬러칩 클래스 추가 -->
									<li><a href="javascript:void(0);" class="black"><span>black</span></a></li>
									<li><a href="javascript:void(0);" class="gray"><span>gray</span></a></li>
									<li><a href="javascript:void(0);" class="white"><span>white</span></a></li>
									<li><a href="javascript:void(0);" class="red"><span>red</span></a></li> <!-- 선택한 경우 a class="active" -->
									<li><a href="javascript:void(0);" class="orange"><span>orange</span></a></li>
									<li><a href="javascript:void(0);" class="yellow"><span>yellow</span></a></li>
									<li><a href="javascript:void(0);" class="green"><span>green</span></a></li>
									<li><a href="javascript:void(0);" class="beige"><span>beige</span></a></li>
									<li><a href="javascript:void(0);" class="blue"><span>blue</span></a></li>
									<li><a href="javascript:void(0);" class="pink"><span>pink</span></a></li>
									<!-- //160901 : 컬러칩 클래스 추가 -->
								</ul>
							</div>
						</div>

						<div class="optionItem optDetail material">
							<div class="optionTit"><strong>소재</strong></div>
							<div class="optionCont">
								<ul class="txtList col">
									<li>
										<label class="chk_style1 option_style1">
											<em>
												<input type="checkbox" value="">
											</em>
											<span>면</span>
										</label>
									</li>
									<li>
										<label class="chk_style1 option_style1">
											<em>
												<input type="checkbox" value="" />
											</em>
											<span>합성섬유</span>
										</label>
									</li>
									<li>
										<label class="chk_style1 option_style1">
											<em>
												<input type="checkbox" value="" />
											</em>
											<span>가죽</span>
										</label>
									</li>
									<li>
										<label class="chk_style1 option_style1">
											<em>
												<input type="checkbox" value="" />
											</em>
											<span>울</span>
										</label>
									</li>
									<li>
										<label class="chk_style1 option_style1">
											<em>
												<input type="checkbox" value="" />
											</em>
											<span>린넨</span>
										</label>
									</li>
									<li>
										<label class="chk_style1 option_style1">
											<em>
												<input type="checkbox" value="" />
											</em>
											<span>실크</span>
										</label>
									</li>
									<li>
										<label class="chk_style1 option_style1">
											<em>
												<input type="checkbox" value="" />
											</em>
											<span>스웨이드</span>
										</label>
									</li>
									<li>
										<label class="chk_style1 option_style1">
											<em>
												<input type="checkbox" value="" />
											</em>
											<span>기타</span>
										</label>
									</li>
								</ul>
							</div>
						</div>

						<div class="optionItem optDetail benefit">
							<div class="optionTit"><strong>혜택</strong></div>
							<div class="optionCont">
								<ul class="txtList col">
									<li>
										<label class="chk_style1 option_style1">
											<em>
												<input type="checkbox" value="selCoupon">
											</em>
											<span>쿠폰할인</span>
										</label>
									</li>
									<li>
										<label class="chk_style1 option_style1">
											<em>
												<input type="checkbox" value="deliveryFeeFreeYn">
											</em>
											<span>무료배송</span>
										</label>
									</li>
									<li>
										<label class="chk_style1 option_style1">
											<em>
												<input type="checkbox" value="selPoint">
											</em>
											<span>포인트</span>
										</label>
									</li>
									<li>
										<label class="chk_style1 option_style1">
											<em>
												<input type="checkbox" value="selPresent">
											</em>
											<span>사은품</span>
										</label>
									</li>
									<li>
										<label class="chk_style1 option_style1">
											<em>
												<input type="checkbox" value="regularDeliveryYn">
											</em>
											<span>정기배송</span>
										</label>
									</li>
									<li>
										<label class="chk_style1 option_style1">
											<em>
												<input type="checkbox" value="offshopPickupYn">
											</em>
											<span>매장픽업</span>
										</label>
									</li>
								</ul>
							</div>
						</div>

						<div class="btnBox">
							<a href="javascript:void(0);" class="btn_sStyle4 sPurple1" 
							onclick="special.employee.detailSearch();">검색</a>
						</div>
		
				<c:if test="${ !isMobile }">
						<button type="button" class="btnMore">전체목록 보기</button>
					</div>
				</c:if>
			
			<c:choose>
				<c:when test="${ !isMobile }">
						<a href="javascript:void(0);" class="btnDetailClose">닫기</a>
					</div>
					<%-- // pc 상세 옵션 --%>
				</c:when>
				<c:otherwise>
					<%-- // mo 상세 옵션 --%>
							</div>
						</div>
					</div>
				</c:otherwise>
			</c:choose>
			
			<div class="tab_con tab_conOn" id="list">

				<c:if test="${ !isMobile }">
					<c:forEach var="depth1" items="${depthList }" varStatus="status">
						<ul class="miniTabBox1" id="subtab${status.index }" style="display:none;">
							<c:forEach var="depth2" items="${depth1.spsDealgroups }" varStatus="status2">
								<li><div>
									<a href="javascript:void(0);" name="${depth2.name }"
									 onclick="javascript:special.employee.depthClick(this, '${depth1.dealGroupNo}', '${depth2.dealGroupNo}', '${status.index }')">${depth2.name }</a>
								</div></li>
							</c:forEach>
						</ul>
					</c:forEach>
				</c:if>

				<div class="tit_style3 mo_only">
					<div class="sortBoxList sort_1ea">
						<ul>
							<%-- 1depth 테마 selectbox --%>
							<c:if test="${isMobile }">
								<p id="is2depth">
								<c:forEach var="depth1" items="${depthList }" varStatus="status">
									<c:set var="moIndex" value="${status.index }" />
										<li class="selectBox_${depth1.dealGroupNo}" style="display: block;">
									<c:if test="${not empty depth1.spsDealgroups[0].name }">
											<div class="select_box1" id="">
												<label></label>
												<select id="depth2_${moIndex }" class="selBox_${depth1.dealGroupNo}" 
												onchange="javascript:special.employee.moSelect('${depth1.dealGroupNo}',this.value)">
													<option value="">전체</option>
													<c:forEach var="depth2" items="${depth1.spsDealgroups }" varStatus="status2">
														<option value="${depth2.dealGroupNo}">${depth2.name }</option>
													</c:forEach>
												</select>
											</div>
									</c:if>
										</li>
								</c:forEach>
								</p>
							</c:if>
							
							<%-- 상품 정렬 selectbox --%>
							<li class="productSort">
								<div class="select_box1">
									<label></label>
									<tags:codeList code="PRODUCT_SORT_CD" var="productSort" tagYn="N"/>
									<select class="productSortType" onchange="javascript:special.employee.sortType(this.value)">
										<c:forEach items="${productSort}" var="sort">
											<option value="${sort.cd}">${sort.name}</option>
										</c:forEach>
									</select>
								</div>
							</li>
							
							<%-- 상품 페이징 selectbox --%>
							<c:if test="${!isMobile }">
								<li class="pageSize">
									<div class="select_box1">
										<label></label>
										<select class="productPageSize" onchange="javascript:special.employee.pageSize(this.value)">
											<option value="40">40개씩</option>
											<option value="80">80개씩</option>
											<option value="120">120개씩</option>
										</select>
									</div>
								</li>
							</c:if>
							<li class="listType"><button type="button" class="btnListType list">블록형 / 리스트형</button></li>
						</ul>
					</div>
					<strong class="depthName"></strong>
				</div>

				<!-- 상품 리스트 -->
				<div class="list_group">
					<div class="product_type1 prodType_4ea block">
						<ul class="productSize${status.index }" id="moProductArea">
							<c:forEach var="product" items="${productList}" varStatus="i">
								<c:set var="product" value="${product}" scope="request" />
								<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
									<jsp:param name="type" value="officer" />
									<jsp:param name="dealProductIndex" value="${i.index }" />
								</jsp:include>
							</c:forEach>
						</ul>
					</div>
				
					<div class="paginateType1">
						<page:paging formId="valueForm" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
							total="${totalCount}" url="/dms/special/employee/productList/ajax" type="ajax" callback="special.employee.pageCallback"/>
					</div>
					
				</div>
				<!-- //상품 리스트 -->
			</div>

		</div>
	</div>
