<%--
	화면명 : 배송지 추가/수정 레이어
	작성자 : allen
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="func" uri="kr.co.intune.commerce.common.functions" %>
<script type="text/javascript">
	function chgSelect(param) {
		$(param).prev().text($('option:selected', param).attr('name'));
	}
	
</script>

<div class="pop_wrap ly_add_address" id="deliveryAddressLayer">
	<form name="addrInputForm" id="addrInputForm">
		<div class="pop_inner">
			<div class="pop_header type1">
				<h3 class="tit" id="layerTitle">새 배송지 추가</h3>
			</div>
			<div class="pop_content">
				<div class="rw_tbBox write">
					<ul class="rw_tb_tbody2">
						<li>
							<div class="tr_box">
								<div class="col1">
									<span class="group_inline">받으실분</span>
								</div>
								<div class="col2">
									<div class="group_block adr">
										<div class="inputTxt_place1 ">
											<label class="mo_only" style="display: inline-block;">이름</label>
											<span>
												<input type="text" value="" name="deliveryName1" id="deliveryName1">
											</span>
										</div>
									</div>
								</div>
							</div>
						</li>
						<li>
							<div class="tr_box">
								<div class="col1">
									<span class="group_inline">휴대폰번호</span>
								</div>
								<div class="col2">
									<div class="group_block phone">
										<div class="inputMix_style2">
											<div class="select_box1">
										 		<label id="cell_areaCode">선택</label>
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
										</div>
									</div>
								</div>
							</div>
						</li>
						<li>
							<div class="tr_box">
								<div class="col1">
									<span class="group_inline">전화번호</span>
								</div>
								<div class="col2">
									<div class="group_block phone">
										<div class="inputMix_style2">
											<div class="select_box1">
										 		<label id="tel_areaCode">선택</label>
												<tags:codeList code="PHONE_NUMBER_CD" optionHead="선택" />
											</div>
											<span class="hyphen">-</span>
											<div class="inputTxt_place1">
												<label></label>
												<span>
													<input type="text" value="" name="tel_num1" id="tel_num1" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');">
												</span>
											</div>
											<span class="hyphen">-</span>
											<div class="inputTxt_place1">
												<label></label>
												<span>
													<input type="text" value="" name="tel_num2" id="tel_num2" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');">
												</span>
											</div>
										</div>
									</div>
								</div>
							</div>
						</li>
						<li>
							<div class="tr_box">
								<div class="col1">
									<span class="group_inline">주소</span>
								</div>
								<div class="col2">
									<div class="group_block adr">
										<div class="inpBox">
											<div class="inputTxt_place1">
												<label></label>
												<span>
													<input type="text" value="" name="zipCd" id="zipCd" readonly>
												</span>
											</div>
											<a href="javascript:mypage.deliveryAddress.searchAddress();" class="btn_sStyle4 sGray2">우편번호검색</a>
										</div>
										<div class="inputTxt_place1">
											<label>상세주소 1</label>
											<span>
												<input type="text" value="" name="address1" id="address1" readonly>
											</span>
										</div>
										<div class="inputTxt_place1">
											<label>상세주소 2</label>
											<span>
												<input type="text" value="" name="address2" id="address2" readonly>
											</span>
										</div>
									</div>
								</div>
							</div>
						</li>
						<li style="display: none;">
							<div class="tr_box">
								<div class="col1">
									<span class="group_inline">배송메세지</span>
								</div>
								<div class="col2">
									<div class="group_block msg">
										<div class="select_box1">
											<label>배송시 요청사항 선택</label>
											<select name="message_code" onchange="$order.setdata.delivery($(this));">
												<option value="">배송시 요청사항 선택</option>
												<c:forEach var="message" varStatus="idx1" items="${func:getCodeList('DELIVERY_MESSAGE_CD')}">
													<option value="${message.cd}">${message.name}</option>
												</c:forEach>
											</select>
										</div>
										<div class="inputTxt_place1">
											<label></label>
											<span>
												<input type="text" name="note" />
											</span>
										</div>
									</div>
								</div>
							</div>
						</li>
					</ul>
					<div class="chkBox">
						<label class="chk_style1">
							<em>
								<input type="checkbox" value="" name="basicYn">
							</em>
							<span>기본 배송지로 설정</span>
						</label>
					</div>
				</div>
	
				<div class="btn_wrapC btn2ea">
					<a href="javascript:ccs.layer.deliveryAddressLayer.cancel();" class="btn_mStyle1 sWhite1">취소</a>
					<a href="javascript:mypage.deliveryAddress.saveDeliveryAddr('update')" class="btn_mStyle1 sPurple1" style="display:none" id="modifyBtn">수정</a>
					<a href="javascript:mypage.deliveryAddress.saveDeliveryAddr('insert')" class="btn_mStyle1 sPurple1" id="regBtn">등록</a>
					<a href="javascript:void(0);" class="btn_mStyle1 sPurple1" onclick="$order.update.delivery($(this));" style="display:none" id="changeBtn">변경</a>
					<a href="javascript:void(0);" class="btn_mStyle1 sPurple1" onclick="$regular.update.delivery($(this));" style="display:none" id="changeBtn2">변경</a>
				</div>
			</div>
			<button type="button" class="btn_x pc_btn_close">닫기</button>
		</div>
		<input type="hidden" name="crudType" id="crudType"/>
		<input type="hidden" name="addressNo" id="addressNo"/>
		<input type="hidden" name="phone1" id="phone1"/>
		<input type="hidden" name="phone2" id="phone2"/>
	</form>
</div>