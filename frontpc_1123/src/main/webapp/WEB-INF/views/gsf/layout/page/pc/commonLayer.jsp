<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

	<!-- 비회원 로그인 레이어 -->
	<jsp:include page="/WEB-INF/views/ccs/common/layer/noneMemberLogin.jsp" />
	
<div class="pop_wrap sLayer_chance pcLayer" style="display: none;" id="sLayer_chance">
		<div class="pop_inner">
			<div class="pop_header type1">
				<h3 class="tit">선물포장 무료찬스!</h3>
			</div>

			<div class="pop_content">
				<div>
					<p><strong>선물포장 쿠폰이 발행되었습니다.</strong></p>
					<p>알로앤루, 알퐁소, 포래즈, 섀르반 상품 구매 시 사용 가능하며 여러개 주문 및 부피 초과시 추가비용이 발생할 수 있습니다.</p>
					<span>쿠폰은 GIFT SHOP에서 매월 ID당 1장 제공됩니다</span>
				</div>
				<div class="btn_wrapC btn1ea">
					<a href="#none" class="btn_mStyle1 sPurple1 pc_btn_close">확인</a>
				</div>
			</div>
			<button type="button" class="btn_x pc_btn_close">닫기</button>
		</div>
	</div>