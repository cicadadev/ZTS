<%--
	화면명 : 템플릿A FAQ Layer
	작성자 : stella
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<div class="pop_wrap ly_brand_faq" style="display:block;" id="templateA_faqLayer">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">FAQ</h3>
		</div>
		<div class="pop_content">
			<ul class="brandFaq">
				<li>
					<div class="q">
						제품구입은 어디서 하나요?
					</div>
					<div class="a">
						전국 (브랜드명) 매장에서 구입이 가능하며, 당사 쇼핑몰 www.0to7.com 을 통해서도 구입 가능합니다. <br />
						* 중고 직거래 사이트 및 온라인 커뮤니티 등 정식 유통 경로가 아닌 곳에서 판매되는 제품은 자사 정품이 아니니 유의하시기 바랍니다.
					</div>
				</li>
				<li class="qa_outer">
					<div class="q">
						제품을 교환하고 싶어요
					</div>
					<div class="a">
						교환은 제품 포장 상태가 양호*한 경우 구입일로부터 7일 내에 가까운 (브랜드명) 매장에서 가능합니다. (상설매장 제외)<br />
						교환은 동일 시즌*에 한해 가능하며, 용품은 용품으로만 의류는 의류로만 교환 가능합니다.<br />
						* 포장 상태 양호 기준: 제품에 오염 및 훼손이 없고, 가격택이 달려 있으며, 상품 박스가 훼손되지 않은 경우<br />
						* 동일 시즌에 한해 교환: ex) 여름 의류는 여름 의류로만 교환 가능
					</div>
				</li>
				<li class="qa_outer">
					<div class="q">
						제품을 환불하고 싶어요.
					</div>
					<div class="a">
						환불은 구입일로부터 7일 이내 영수증 지참 시 구입처에서만 가능합니다.
					</div>
				</li>
				<li class="qa_outer">
					<div class="q">
						A/S는 어떻게 하나요?
					</div>
					<div class="a">
						A/S문의는 고객센터 > 1:1문의 > AS 에서 문의 부탁 드립니다.
					</div>
				</li>
			</ul>
		</div>
		<button type="button" class="btn_x pc_btn_close" onclick="ccs.layer.close('templateA_faqLayer');">닫기</button>
	</div>
</div>