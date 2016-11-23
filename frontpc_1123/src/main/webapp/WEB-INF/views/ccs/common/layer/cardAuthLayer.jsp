<%--
	화면명 : 다자녀우대관 인증 레이어
	작성자 : roy
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@page import="java.util.*"%>

	<script type="text/javascript" src="/resources/js/common/common.ui.js"></script>

<c:if test="${isMobile}"> 
	<script type="text/javascript" src="/resources/js/mo.js"></script>
</c:if>
<c:if test="${!isMobile}">
	<script type="text/javascript" src="/resources/js/pc.js"></script>
</c:if>

<c:if test="${isMobile}"> 
	<div class="layer_style1 sLayer_gift" id="cardAuthLayer">
		<form name="cardAuthForm" id="cardAuthForm">
		<input type="hidden" name="childrencardTypeCd" value="${search.childrencardTypeCd}"/>
		<div class="box">
			<div class="conArt">
				<strong class="title" id="cardTitle"></strong>

				<div class="conBox">
					<div class="pop_multi_child">
						<div class="relBox">
							<div class="rw_tbBox">
								<ul class="rw_tb_tbody2">
									<li>
										<div class="tr_box">
											<div class="col1">
												<div class="cell">
													<span class="group_inline">고객명</span>
												</div>
											</div>
											<div class="col2">
												<div class="group_block">
													<div class="inputTxt_place1">
														<label></label>
														<span>
															<input type="text" id="memberName" name="memberName" value="" />
														</span>
													</div>
												</div>
											</div>
										</div>
									</li>
									<li>
										<div class="tr_box">
											<div class="col1">
												<div class="cell">
													<span class="group_inline">카드번호</span>
												</div>
											</div>
											<div class="col2">
												<div class="group_block multi_card_num">
													<div class="inputTxt_place1">
														<label></label>
														<span>
															<input type="text" id="accountNo1" name="accountNo1" value="" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');"/>
														</span>
													</div>
													<div class="inputTxt_place1">
														<label></label>
														<span>
															<input type="text" id="accountNo2" name="accountNo2" value="" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');"/>
														</span>
													</div>
													<div class="inputTxt_place1">
														<label></label>
														<span>
															<input type="text" id="accountNo3" name="accountNo3" value="" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');"/>
														</span>
													</div>
													<div class="inputTxt_place1">
														<label></label>
														<span>
															<input type="text" id="accountNo4" name="accountNo4" value="" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');"/>
														</span>
													</div>
												</div>
											</div>
										</div>
									</li>
								</ul>
							</div>
						</div>
					</div>
				</div>
				<div class="btn_wrapC btn1ea">
					<a href="#none" class="btn_mStyle1 sPurple1">인증</a>
				</div>
			</div>
			<button type="button" class="btn_close">레이어팝업 닫기</button>
		</div>
		</form>
	</div>
</c:if>

<c:if test="${!isMobile}"> 
<div class="pop_wrap sLayer_gift" id="cardAuthLayer">
	<form name="cardAuthForm" id="cardAuthForm">
		<input type="hidden" name="childrencardTypeCd" value="${search.childrencardTypeCd}"/>
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit" id="cardTitle"></h3>
		</div>
		<div class="pop_content">
			<div class="pop_multi_child">
				<div class="relBox">
					<div class="rw_tbBox">
						<ul class="rw_tb_tbody2">
							<li>
								<div class="tr_box">
									<div class="col1">
										<div class="cell">
											<span class="group_inline">고객명</span>
										</div>
									</div>
									<div class="col2">
										<div class="group_block">
											<div class="inputTxt_place1">
												<label></label>
												<span>
													<input type="text" id="memberName" name="memberName" value="" />
												</span>
											</div>
										</div>
									</div>
								</div>
							</li>
							<li>
								<div class="tr_box">
									<div class="col1">
										<div class="cell">
											<span class="group_inline">카드번호</span>
										</div>
									</div>
									<div class="col2">
										<div class="group_block multi_card_num">
											<div class="inputTxt_place1">
												<label></label>
												<span>
													<input type="text" id="accountNo1" name="accountNo1" value="" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');"/>
												</span>
											</div>
											<div class="inputTxt_place1">
												<label></label>
												<span>
													<input type="text" id="accountNo2" name="accountNo2" value="" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');"/>
												</span>
											</div>
											<div class="inputTxt_place1">
												<label></label>
												<span>
													<input type="text" id="accountNo3" name="accountNo3" value="" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');"/>
												</span>
											</div>
											<div class="inputTxt_place1">
												<label></label>
												<span>
													<input type="text" id="accountNo4" name="accountNo4" value="" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');"/>
												</span>
											</div>
										</div>
									</div>
								</div>
							</li>
						</ul>
					</div>
				</div>
			</div>
			<div class="btn_wrapC btn1ea">
				<a href="javascript:ccs.layer.cardAuthLayer.auth();" class="btn_mStyle1 sPurple1">인증</a>
			</div>
		</div>
		<button type="button" onclick="javascript:ccs.layer.cardAuthLayer.cancle();" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>
</c:if>