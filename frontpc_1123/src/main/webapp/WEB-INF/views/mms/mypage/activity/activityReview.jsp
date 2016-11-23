<%--
	화면명 : 마이페이지 > 나의활동 > 상품평
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript" src="/resources/js/date.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	//달력 초기화
	initCal("startDate","endDate");
	
	mypage.review.getReviewList();
});

if(ccs.common.mobilecheck()) {
	/* 모바일 스크롤 제어*/
	$(window).bind("scroll", reviewListScrollListener);
		
	function reviewListScrollListener() {
		
		var target;
		var totalCount;
		if($("#mypageReviewTab").find("li:eq(0)").hasClass("on")){
			target = $("#productListDiv .div_tb_tbody3");
			totalCount = Number($("[name=productTotalCount]").val());
		}else if($("#mypageReviewTab").find("li:eq(1)").hasClass("on")){
			target = $("#reviewListDiv .div_tb_tbody3");
			totalCount = Number($("[name=reviewTotalCount]").val());
		}
		var rowCount = target.children("li").length;
		
		var maxPage = Math.ceil(totalCount/10);
		
		var scrollTop = $(window, document).scrollTop();
		var scrollHeight = $(document).height() - $(window).height();
		
		if (scrollTop >= scrollHeight - 200 && scrollHeight != 0) {
			if(rowCount !=0 && (rowCount < totalCount)){
				
				if ($("#tempLoadingBar").length > 0 ) {
					return;
				}
				
				mypage.review.pagingListCallback();
			}
		}
	}
}

</script>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY활동관리|상품평" name="pageNavi"/>
</jsp:include>
<div class="inner">
	<div class="layout_type1">
		<div class="column">
			<input type="hidden" name="currentProductPage" value="1" />
			<input type="hidden" name="currentReviewPage" value="1" />
			<div class="mypage myreview">
				<h3 class="title_type1">상품평</h3>

				<div>
					<ul class="tabBox tp1" id="mypageReviewTab">
						<li class="on"><a href="javascript:void(0);">상품평 쓰기<em>(<label id="productReview"></label>)</em></a></li>
						<li ><a href="javascript:void(0);">내가 쓴 상품평<em>(<label id="myReview"></label>)</em></a></li>
					</ul>
					<!-- 상품평 쓰기 -->
					<div class="tab_con tab_01 tab_conOn">
						<div class="tbl_article">
							<div class="div_tb_thead3">
								<div class="tr_box">
									<span class="col1">주문일시 / 주문번호</span>
									<span class="col2">상품정보</span>
									<span class="col3">상품평 작성 가능일</span>
									<span class="col4">관리</span>
								</div>	
							</div>
							
							<span id="productListDiv"></span>
							<%-- ajax 영역 --%>								
						</div>

						<ul class="notice">
							<li>상품평은 배송완료 상품에 한해 주문일로부터 <strong>90일 이내</strong> 작성 가능합니다.</li>
							<li>체험단 후기는 당첨 상품 수령 후 <strong>90일 이내</strong> 작성 가능합니다.</li>
							<li>상품평 작성에 따라 매일포인트 및 당근은 익일 새벽 자동지급됩니다.<br />
							일반상품평 작성 시 <strong>매일포인트 100P + 당근500개</strong> 지급<br />
							포토상품평 작성 시 <strong>매일포인트 100P + 당근 1,000개</strong> 지급<br />
							첫번째 상품평 작성 시 <strong>매일포인트 1000P</strong></li>
						</ul>
					</div>
					
					<!-- 내가 쓴 상품평 -->
					<div class="tab_con tab_02">
						<div class="periodBox">
							<strong>조회기간</strong>
							<ul class="periodList"></ul>
							<div class="calendarBox">
								<div class="btnR"><a href="javascript:;" class="btn_x btn_close">닫기</a></div>
							 	<span class="inpCalendar">
							 		<input type="text" id="startDate" period-set day="0" />
							 	</span>
							 	<span class="swung">~</span>
							 	<span class="inpCalendar">
							 		<input type="text" id="endDate" period-set />
							 	</span>
							</div>
							<a href="javascript:mypage.review.reviewSearch();" class="btn_sStyle4 sPurple1 btnInquiry" >조회</a>
						</div>

						<div class="tbl_article">
							<input type="hidden" id="review_ImgYn" value=""/>
							<ul class="sortBox">
								<li class="active">
									<a onClick="javascript:mypage.review.imgYnSearch('');">
										<span>전체</span><em>(<label id="myReviewCntAll"></label>)</em>
									</a>
								</li>
								<li>
									<a onClick="javascript:mypage.review.imgYnSearch('Y');">
										<span>포토</span><em>(<label id="myReviewCntImgY"></label>)</em>
									</a>
								</li>
								<li>
									<a onClick="javascript:mypage.review.imgYnSearch('N');">
										<span>일반</span><em>(<label id="myReviewCntImgN"></label>)</em>
									</a>
								</li>
							</ul>
							<div class="div_tb_thead3">
								<div class="tr_box">
									<span class="col1">주문일시 / 주문번호</span>
									<span class="col2">상품정보</span> <!-- 16.11.11 -->
									<span class="col3">내용</span> <!-- 16.11.11 -->
									<span class="col4">작성일</span>
								</div>	
							</div>
							
							<span id="reviewListDiv"></span>
							<%-- ajax 영역 --%>
						</div>

						<ul class="notice">
							<li>다음과 같은 글은 고객 동의 없이 임의로 삭제 될 수 있으니 유의 바랍니다.<br />
							- 타인의 저작권을 침해하는 경우<br />
							- 음란/욕설 등 공공성을 저해하는 경우<br />
							- 개인정보 유출의 위험이 있는 경우<br />
							- 판매/광고/홍보성 글이 등록 된 경우</li>
							<li>작성하신 글의 저작권 침해에 대한 책임은 본인에게 있습니다.</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
		<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
	</div>
</div>
