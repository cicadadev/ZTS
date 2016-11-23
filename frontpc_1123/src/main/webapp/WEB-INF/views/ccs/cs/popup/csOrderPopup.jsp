<%--
	화면명 : 고객센터 > 메인
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<script type="text/javascript" src="/resources/js/date.js"></script>
<script type="text/javascript">
	//달력 초기화
	initCal("startDate","endDate");
</script>
<div class="content">
	<jsp:include page="/WEB-INF/views/gsf/layout/page/pc/navi_pc.jsp" />

	<div class="inner csmain">
		<div class="layout_type1">


			<!-- ### mypage 좌측 메뉴 ### -->
			<jsp:include page="/WEB-INF/views/gsf/layout/cs_left.jsp" />
			<!-- ### //mypage 좌측 메뉴 ### -->

			<div class="column">
				<div class="cs">
					<h3 class="title_type1">비회원 주문조회</h3>

					<div class="periodBox">
						<div class="orderNBox">
							<div class="inp_box inp_placeholder">
								<label for="orderN">상품명</label> <input type="text" id="orderN"
									value="">
							</div>
						</div>

						<strong>조회기간</strong>
						<ul class="periodList"></ul>
						<div class="calendarBox">
							<div class="btnR">
								<a href="#" class="btn_x btn_close">닫기</a>
							</div>
							<span class="inpCalendar"> <input type="text"
								value="20160501" /> <a href="#"><img
									src="img/pc/btn/btn_calendar.png" alt="시작일" /></a>
							</span> <span class="swung">~</span> <span class="inpCalendar"> <input
								type="text" value="20160501" /> <a href="#"><img
									src="img/pc/btn/btn_calendar.png" alt="종료일" /></a>
							</span>
						</div>
						<ul class="periodList">
							<li><a href="#" class="btn_day on">1개월</a></li>
							<!-- 선택시 a class="on" 추가 -->
							<li><a href="#" class="btn_day">3개월</a></li>
							<li><a href="#" class="btn_day">6개월</a></li>
						</ul>
						<a href="#" class="btn_style6 btnPeriod">기간설정</a> <a href="#"
							class="btn_style6 btnInquiry">조회</a>
					</div>

					<div class="rw_tbBox">
						<ul class="rw_tb_tbody3">
							<li>
								<div class="order_date">
									<em>2016-05-25</em> <em>주문번호 : 000000000000000</em> <a
										href="#none" class="btn_style7 btn_moreView">상세보기</a>
								</div>

								<div class="tr_box">
									<div class="col1">
										<div class="prod_img">
											<a href="#none"> <img src="img/pc/temp/cart_img1.jpg"
												alt="" />
											</a>
										</div>

										<a href="#none" class="title"> [allo&lugh] thanks cool
											주머니장식배기바지 주머니장식배기바지 주머니장식배기바지 </a> <em> <i>옵션 : Red / 2Y</i>
										</em> <u> [사은품] 턱받침1개 </u> <span class="price"> 25,000원 </span>
									</div>
									<div class="col2">
										<b>결제완료</b>
									</div>
									<div class="col3">
										<a href="#none" class="btn_style7 gray1 btn_cancel">주문취소</a>
									</div>
								</div>
							</li>

							<li>
								<div class="order_date">
									<em>2016-05-25</em> <em>주문번호 : 000000000000000</em> <a
										href="#none" class="btn_style7 btn_moreView">상세보기</a>
								</div>

								<div class="tr_box">
									<div class="col1">
										<div class="prod_img">
											<a href="#none"> <img src="img/pc/temp/cart_img1.jpg"
												alt="" />
											</a>
										</div>

										<a href="#none" class="title"> [allo&lugh] thanks cool
											주머니장식배기바지 주머니장식배기바지 주머니장식배기바지 </a> <em> <i>옵션 : Red / 2Y</i>
										</em> <u> [사은품] 턱받침1개 </u> <span class="price"> 25,000원 </span>
									</div>
									<div class="col2">
										<b>배송완료</b>
									</div>
									<div class="col3">
										<a href="#none" class="btn_style7 gray1">배송조회</a> <a
											href="#none" class="btn_style7 gray1 btn_cancel">주문취소</a> <a
											href="#none" class="btn_style7 gray1">반품신청</a>
									</div>
								</div>
							</li>
						</ul>
					</div>
				</div>
			</div>
		</div>
	</div>
</div>
</div>