<%--
	화면명 : 마이페이지 > 쇼핑찜 > 옵션선택 레이어
	작성자 : stella
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">
$(document).ready(function() {
	
});
</script>

<div>
	<input type="hidden" name="optionCnt" id="optionCnt" value="${optionCnt}" />
	<input type="hidden" name="selectedProduct" id="selectedProduct" value="${optionList[0].productId}" />
</div>

<c:choose>
	<c:when test="${isMobile eq 'true'}">
		<div class="layer_style1 sLayer_sns" id="optionSelectLayer" style="display:none;">
			<div class="box">
				<div class="conArt">
					<strong class="title">옵션선택</strong>
		
					<div class="conBox">
						<div id="optionSelectDiv">
							<c:forEach var="option" items="${optionList}" varStatus="status">
								<dl>
									<dt>${option.optionName}</dt>
									<dd>
										<div class="select_box1">
											<label>선택</label>
											<select id="optionSelect${status.index}" onchange="mypage.wishlist.optionOnchange(${status.index});">
												<option>선택</option>
												<c:forEach var="value" items="${option.optionValues}">
													<option>${value.optionValue}</option>
												</c:forEach>
											</select>
										</div>
									</dd>
								</dl>
							</c:forEach>
						</div>
					</div>
					
					<div class="btn_wrapC btn2ea ">
						<a href="#none" class="btn_mStyle1 sWhite1" onclick="ccs.layer.close('optionSelectLayer');">취소</a>
						<a href="#none" class="btn_mStyle1 sPurple1" onclick="mypage.wishlist.optionChoiceComp('${optionList[0].productId}');">선택</a>
					</div>
				</div>
				<button type="button" class="btn_close" onclick="ccs.layer.close('optionSelectLayer');">레이어팝업 닫기</button>
			</div>
		</div>
	</c:when>
	<c:otherwise>
		<div class="pop_wrap ly_order_cancel ly_orcan" style="display:none;" id="optionSelectLayer">
			<div class="pop_inner">
				<div class="pop_header type1">
					<h3 class="tit">옵션선택</h3>
				</div>
				<div class="pop_content">
		
					<div class="viewTblList">
						<ul class="div_tb_tbody3">
							<li>
								<div>
									<div class="change_return" id="optionSelectDiv">
										<c:forEach var="option" items="${optionList}" varStatus="status">
											<dl>
												<dt>${option.optionName}</dt>
												<dd>						
													<div class="select_box1">
														<label>선택</label>
														<select id="optionSelect${status.index}" onchange="mypage.wishlist.optionOnchange(${status.index});">
															<option>선택</option>
															<c:forEach var="value" items="${option.optionValues}">
																<option>${value.optionValue}</option>
															</c:forEach>
														</select>
													</div>
												</dd>
											</dl>
										</c:forEach>
									</div>
								</div>
							</li>
						</ul>
					</div>
		
					<div class="btn_wrapC btn2ea ">
						<a href="#none" class="btn_mStyle1 sWhite1" onclick="ccs.layer.close('optionSelectLayer');">취소</a>
						<a href="#none" class="btn_mStyle1 sPurple1" onclick="mypage.wishlist.optionChoiceComp('${optionList[0].productId}');">선택</a>
					</div>
					<button type="button" class="btn_x pc_btn_close" onclick="ccs.layer.close('optionSelectLayer');">닫기</button>
				</div>
			</div>
		</div>
	</c:otherwise>
</c:choose>


<style>
	.pc .viewTblList {margin-top:0px;}
	.pc .div_tb_tbody3 {border-top:1px solid #ffffff;}
	.pc .ly_order_cancel .viewTblList {margin-top:0px;}
	.pc .ly_order_cancel .viewTblList .div_tb_tbody3 .change_return {border-top:1px solid #ffffff;}
	.pc .ly_order_cancel .viewTblList .div_tb_tbody3 .change_return dl {margin-top:10px;}

	.mobile #optionSelectDiv {padding:10px 0;}
	.mobile #optionSelectDiv dl {width:100%; height:40px; margin-top:5px;}
	.mobile #optionSelectDiv dl dt {float:left; width:30%; line-height:40px; margin-left:5px;}
	.mobile #optionSelectDiv dl dd {float:right; width:60%; margin-right:5px;}
	.mobile #optionSelectDiv dl dd .select_box1 {width:100%;}
</style>
