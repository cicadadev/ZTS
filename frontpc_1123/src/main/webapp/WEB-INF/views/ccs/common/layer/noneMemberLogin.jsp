<%--
	화면명 : 비회원 로그인
	작성자 : eddie
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<script type="text/javascript" src="/resources/js/date.js"></script>
					
<c:if test="${!isMobile}">
	<script type="text/javascript" src="/resources/js/pc.js"></script>
</c:if>
<script>
$(document).ready(function() {

	//달력 초기화
	if('${isMobile}'!='true'){
		initCal("orderDt","orderDt");
	}
	
});

	var login = function(){
		var data = {
				orderId : $("#orderId").val(),
				orderPwd : $("#user_pw").val()
			};
			mms.common.nonMemberLogin(data);
	}

	var findOrderId = function(){
		var phone = $("#user_phone1").val().replace(/(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})/,"$1-$2-$3"); 
		
		var data = {
				name1 : $("#user_nm1").val(),
				phone2 : phone,
				orderPwd : $("#user_pw1").val(),
			};
		mms.common.nonMemberFindOrderId(data);
	}
	
	var findOrderPwd = function(){
		var phone = $("#user_phone2").val().replace(/(^02.{0}|^01.{1}|[0-9]{3})([0-9]+)([0-9]{4})/,"$1-$2-$3"); 
		
		var data = {
				name1 : $("#user_nm2").val(),
				phone2 : phone,
				orderDt : $("#orderDt").val(),
			};
			mms.common.nonMemberFindOrderPwd(data);
	}
	
	$(function(){
		  $('.ui-datepicker-trigger').click(function(e){
			  var divTop = e.clientY; //상단 좌표 위치 안맞을시 e.pageY
			  var divLeft = e.clientX - 200; //좌측 좌표 위치 안맞을시 e.pageX
				
// 				console.log(divTop);
// 				console.log(divLeft);
				
				$('#ui-datepicker-div').css({
				     "top": divTop
				     ,"left": divLeft
				     , "position": "absolute"
				}).show();
				
		  });
		  
		  $('.mobile .nonMemberClose').off("click").on({
				"click" : function() {
					// Roy
					/* $("#noneMemberLoginLayer").remove(); */
					
					$("#noneMemberOrderIdLayer").css("display", "none");
					$("#noneMemberOrderPwdLayer").css("display", "none");
					if ($("#noneMemberLoginLayer").css("display") == 'none'){
						$("#noneMemberLoginLayer").css("display", "block");
					} 
					else {
						$("#noneMemberLoginLayer").css("display", "none");
					}
					
					if(($("#noneMemberLoginLayer").css("display") == 'none') && ($("#noneMemberOrderIdLayer").css("display") == 'none') && ($("#noneMemberLoginLayer").css("display") == 'none')){
						$(".mobile .content").removeClass("content_fixed").show();
						$(".header_mo, .footer, .footer_mo").show();
					}
				}
			});		  
		});
</script>

<style>

.inpCalendar button {
    position: absolute;
    right: 10px;
    top: 7px;
    width: 16px;
    height: 16px;
    font-size: 0;
    background: url(../img/pc/ico/ico_btn.png) no-repeat 0 -100px;
    vertical-align: top;
}


</style>

<!-- 2016.11.03 수정 start -->
<!-- 팝업 - 비회원 주문조회 -->
<div class="pop_wrap ly_orderNum" id="noneMemberLoginLayer" style="display:none">
	<input type="hidden" value="" id="noneMemberPhone">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">비회원 주문조회</h3>
		</div>

		<div class="pop_content">
			<div class="inp_list">
				<div class="inputTxt_place1">
					<label>주문번호</label>
					<span>
						<input type="text" value="" id="orderId">
					</span>
				</div>
				<div class="inputTxt_place1">
					<label>비밀번호</label>
					<span>
						<input type="password" value="" id="user_pw">
					</span>
				</div>
			</div>

			<div class="btn_wrapC btn1ea">
				<a href="#none" onclick="login()" class="btn_mStyle1 sPurple1">조회</a>
			</div>

			<ul class="link">
				<li>
					<a href="#none" class="btn_nonOrderNum">주문번호 찾기</a>
				</li>
				<li>
					<a href="#none" class="btn_nonPw">비밀번호 찾기</a>
				</li>
			</ul>

			<ul class="dot_list">
				<li>주문 시 기재한 이름,휴대폰번호, 비밀번호로 주문번호, 비밀번호 찾기가 가능합니다.</li>
				<li>주문 시 입력 정보가 기억나지 않으실 경우 고객센터(1588-8744)로 문의해 주시기 바랍니다.</li>
			</ul>

			<div class="contArea_ty1">
				<span>매일Family 회원이 되시면 더 많은 혜택이 있습니다.</span>
				<button class="btn_memJoin btn_sStyle3" onclick="mms.common.joinPopup()">회원가입</button>
			</div>
		</div>
		<button type="button" class="btn_x pc_btn_close nonMemberClose">닫기</button>
		
	</div>
</div>
<!-- //팝업 - 비회원 주문조회 -->

<!-- 팝업 - 주문번호 찾기 -->
<div class="pop_wrap ly_orderSearch" id="noneMemberOrderIdLayer">
	<div class="pop_inner">

		<!-- ### 주문번호 찾기 ### -->
		<div class="pop_header type1">
			<h3 class="tit">주문번호 찾기</h3>
		</div>

		<div class="pop_content">
			<div class="inp_list">
				<div class="inputTxt_place1">
					<label>이름</label>
					<span>
						<input type="text" value="" id="user_nm1">
					</span>
				</div>
				<div class="inputTxt_place1">
					<label>휴대폰 번호 ( ‘-’ 없이 입력)</label>
					<span>
						<input type="text" value="" id="user_phone1">
					</span>
				</div>
				<div class="inputTxt_place1">
					<label>비밀번호</label>
					<span>
						<input type="password" value="" id="user_pw1">
					</span>
				</div>
			</div>

			<div class="result" id="orderIdResult">
				<!-- 고객님의 주문번호는 <u>11111111</u> 입니다. -->
			</div>

			<div class="btn_wrapC btn2ea">
				<a href="#none" class="btn_mStyle1 sWhite1 pc_btn_close nonMemberClose">취소</a>
				<a href="#none" class="btn_mStyle1 sPurple1" onclick="findOrderId()">조회</a>
			</div>

			<ul class="dot_list">
				<li>주문 시 기재한 이름,휴대폰번호, 비밀번호로 주문번호, 비밀번호 찾기가 가능합니다.</li>
				<li>주문 시 입력 정보가 기억나지 않으실 경우 고객센터(1588-8744)로 문의해 주시기 바랍니다.</li>
			</ul>
			
		</div>
		<button type="button" class="btn_x pc_btn_close nonMemberClose">닫기</button>

	</div>
</div>
<!-- //팝업 - 주문번호 찾기 -->

<!-- 팝업 - 비밀번호 찾기 -->
<div class="pop_wrap ly_pwSearch" id="noneMemberOrderPwdLayer">
	<div class="pop_inner">

		<div class="pop_header type1">
			<h3 class="tit">비밀번호 찾기</h3>
		</div>

		<div class="pop_content">
			<div class="inp_list">
				<div class="inputTxt_place1">
					<label>이름</label>
					<span>
						<input type="text" value="" id="user_nm2">
					</span>
				</div>
				<div class="inputTxt_place1">
					<label>휴대폰 번호 ( ‘-’ 없이 입력)</label>
					<span>
						<input type="text" value="" id="user_phone2">
					</span>
				</div>
				<div class="inputTxt_place1">
					<div class="group group_date">
						<div class="calendarBox">
							<span class="inpCalendar">
								<input type="text" id="orderDt" name="orderDt"/>
							</span>
						</div>
					</div>
				</div>
			</div>

			<div class="result" id="orderPwdResult">
				<!-- SMS로 비밀번호가 전송되었습니다. -->
			</div>

			<div class="btn_wrapC btn2ea">
				<a href="#none" class="btn_mStyle1 sWhite1 pc_btn_close nonMemberClose">취소</a>
				<a href="#none" class="btn_mStyle1 sPurple1" onclick="findOrderPwd()">조회</a>
			</div>


			<ul class="dot_list">
				<li>주문 시 기재한 이름,휴대폰번호, 비밀번호로 주문번호, 비밀번호 찾기가 가능합니다.</li>
				<li>주문 시 입력 정보가 기억나지 않으실 경우 고객센터(1588-8744)로 문의해 주시기 바랍니다.</li>
			</ul>
			
		</div>
		<button type="button" class="btn_x pc_btn_close nonMemberClose">닫기</button>

	</div>
</div>
<!-- //팝업 - 비밀번호 찾기 -->
<!-- //2016.11.03 수정 end -->
