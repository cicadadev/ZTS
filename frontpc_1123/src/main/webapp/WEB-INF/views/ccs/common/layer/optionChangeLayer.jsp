<%--
	화면명 : 장바구니 > 옵션변경 레이어
	작성자 : eddie
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<div class="con">
	<strong class="title">옵션/수량변경</strong>
	<c:if test="${fn:length(optionList) > 0 }">
	<dl>
		<c:forEach var="option" items="${optionList}" varStatus="index">
			<dt>${option.optionName }</dt>
			<dd>
				<div class="select_box1">
					<label id="${option.optionName}_label">${selected[option.optionName]}</label>
					<select optionName="${option.optionName}" name="options_${index.count }"
						onchange="oms.changeOptionLayer.selectOption(this.name, ${fn:length(optionList)} ,'${productId }')">
						<option value="">선택하세요</option>
						<c:forEach var="value" items="${option.optionValues }">
							<option ${value.optionValue eq selected[option.optionName] ? 'selected' : '' } 
								${value.realStockQty < 1 ? ' disabled="disabled" style="color: red"' : ''} 
								value="${value.optionValue}">${value.optionDispValue}</option>
						</c:forEach>
					</select>
				</div>
			</dd>
		</c:forEach>
	</dl>
	</c:if>
	<div class="user_action">
		<dl>
			<dt>수량</dt>
			<dd>
				<div class="quantity">
					<button type="button" class="btn_minus" onclick="oms.changeOptionLayer.minusQty()">수량빼기</button>
					<input type="text" id="tempQty" value="${selected.qty }" onchange="oms.changeOptionLayer.chgQty(this.value)"onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');"onblur="ccs.common.fn_press_han(this);"/>
					<button type="button" class="btn_plus" onclick="oms.changeOptionLayer.plusQty()">수량추가</button>
				</div>
			</dd>
		</dl>
<%-- 		<a href="javascript:void(0);" class="btn_lyBox1" onclick="oms.changeOptionLayer.change('${productId }','options');">변경</a> --%>
	</div>
	<div class="btn_wrapC btn1ea">
		<a href="javascript:void(0);" class="btn_sStyle3 sPurple1" onclick="oms.changeOptionLayer.change('${productId }','options')">변경</a>
	</div>
	<a href="javascript:void(0);" class="btn_close">옵션창 닫기</a>
</div>
<script type="text/javascript">
<!--
	$(function() {
		$(".option_box .btn_close").off("click").on({
			"click" : function(e) {
				if ($(this).closest(".option_box").hasClass("gift_box")) {
					$(this).closest(".option_box").prev().removeClass("on");
				}
				$(this).closest(".option_box").hide();
				$(this).closest(".option_box").html('');
			}
		});
	});
//-->
</script>
