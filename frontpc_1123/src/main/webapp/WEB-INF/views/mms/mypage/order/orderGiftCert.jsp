<%--
	화면명 : 선물함 상세
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<script>
function chgSelect(param) {
	$(param).prev().text($('option:selected', param).attr('value'));
}

</script>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY주문관리|선물함 관리" name="pageNavi"/>
</jsp:include>
<div class="inner">
<div class="layout_type1">
<div class="column">
	<div class="mypage mycheck">
		<h3 class="title_type1">받은 선물함</h3>

		<div class="giftArea">
			<img src="/resources/img/mobile/bg/gift_namecheck.png" alt="선물이 도착했습니다." class="mo_img">
			<p>선물이 도착했습니다.</p>
			<div class="info">
				휴대폰 본인인증 후 선물함 확인이 가능합니다.
			</div>
		</div>
	
		<div class="write">
			<ul class="rw_tb_tbody2">
				<li>
					<div class="tr_box">
						<div class="col1">
							<span class="group_inline">휴대폰 인증</span>
						</div>
						<div class="col2">
							<div class="group_block mo_certify">
								<div class="inputMix_style2">
									<div class="select_box1">
								 		<label></label>
										<tags:codeList code="MOBILE_NUMBER_CD" optionHead="선택" />
									</div>
									<span class="hyphen">-</span>
									<div class="inputTxt_place1">
										<label></label>
										<span>
											<input type="text" value="" name="cell_num1" id="cell_num1" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');">
										</span>
									</div>
									<span class="hyphen">-</span>
									<div class="inputTxt_place1">
										<label></label>
										<span>
											<input type="text" value="" name="cell_num2" id="cell_num2" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');">
										</span>
									</div>
									<a href="javascript:mypage.gifticon.sendCertNum();" class="btn_sStyle4 sGray2">인증번호발송</a>
								</div>
								<div class="inputTxt_place1">
									<label>인증번호 입력</label>
									<span>
										<input type="text" value="" name="authNumber" id="authNumber">
									</span>
								</div>
							</div>
						</div>
					</div>
				</li>
			</ul>
		</div>
		<div class="btn_wrapC btn1ea">
			<a href="javascript:mypage.gifticon.goToGiftPage();" class="btn_mStyle1 sPurple1">선물함 입장하기</a>
		</div>

		<ul class="notice">
			<li>본인 확인을 위해 휴대폰 본인인증이 필요합니다.</li>
			<li>본인인증 후 선물받은 상품의 옵션 변경 및 배송지 주소를 입력 하실 수 있습니다.</li>
			<li>선물 받을 배송지를 정확히 입력하셔야 배송이 가능합니다.</li>
			<li>잘못된 주소지 입력으로 인한 오배송은 0to7.com에서 책임지지 않습니다.</li>
			<li>선물은 메시지를 받은 날로부터 7일간 유지되며, 유효기간 경과시 주문이 자동 취소됩니다. </li>
		</ul>
		
		
		<form name="giftForm" id="giftForm" method="POST">
			<input type="hidden" name="giftPhone" id="giftPhone" />
		</form>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
</div>
</div>