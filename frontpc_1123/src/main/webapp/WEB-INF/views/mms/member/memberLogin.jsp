<%--
	화면명 : 로그인
	작성자 : dennis
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%
	pageContext.setAttribute("logingImg", Config.getString("common.corner.logingImg") );
%>
<script src="//developers.kakao.com/sdk/js/kakao.min.js"></script>
<script type="text/javascript">

$(document).ready(function(){
	var id = $.cookie("loginid-ztsfo");	
	if(id != undefined){
		$("#user_id").val(id);
		$("#id_save").prop("checked",true);
	}
})




var login = function(system,type){
	
	var data = {};
	
	if(system == "PC"){
		if(type == "MEMBER"){
			data = {
					j_username:'M_'+$("#user_id").val(),
					j_password:$("#user_pw").val(),
					_spring_security_remember_me:$("#auto_login").checked
					};
		}else{
			data = {j_username:'N_'+$("#nonmem_num3").val(),
					j_password:$("#nonmem_pw3").val()
					};	
		}		
	}else{
		if(type == "MEMBER"){
			data = {
					j_username:'M_'+$("#user_id").val(),
					j_password:$("#user_pw").val(),
					_spring_security_remember_me:$("#auto_login").checked
					};
		}else{
			data = {j_username:'N_'+$("#nonmem_order").val(),
					j_password:$("#nonmem_pw").val()
					};	
		}
	}
	
	if($("#id_save").prop("checked")){		
		$.cookie('loginid-ztsfo',$("#user_id").val(),{expires:30});	
	}else{
		$.removeCookie('loginid-ztsfo');
	}
	
	$.ajax({
		url : "/j_spring_security_check",
		type : "POST",
		data : data
	}).done(function(result){		
		if(result.response.error){
			//alert(result.response.message);
			//$(".pc .layer_box").show();
			 var msg = "일치하는 주문내역이 없습니다.  다시 한 번 확인해 주시기 바랍니다.";
			 if(type == "MEMBER"){
				 msg = "일치하는 아이디/비밀번호가 없습니다.  다시 한 번 확인해 주시기 바랍니다.";
			 }
			alert(msg);
		}else{
// 			console.log(msg);
// 			if(msg.response.redirectUrl){
// 				location.href=msg.response.redirectUrl;
// 			}else{
				login_callback();		
// 			}
		}				
	});
			
}

var login_callback = function(){
	
	//비회원 장바구니 merge
	$.ajax({
		url : "/api/oms/cart/merge",
		type : "POST"
// 		data : data
	}).done(function(response){		
		if(response.RESULT == "SUCCESS"){
			location.href="/ccs/common/main";
		}else{
			alert(response.MESSAGE);
		}				
	});
}

var search_order = function(type){
	
	var data = {};
	
	if(type == "ID"){
		data = {
				"name1":$("#mem_name4").val(),
				"phone2":$("#mem_phone4").val(),
				"orderPwd":$("#pw4").val()
				};			
	}else if(type == "PWD"){
		data = {
				"name1":$("#nonmem_name2").val(),
				"phone2":$("#nonmem_phone2").val(),
				"orderDt":$("#orderday").val()
				};			
		
	}
	
	$.ajax({
 		contentType : "application/json; charset=UTF-8",
		url : "/api/oms/order/search",
		type : "POST",
		data : JSON.stringify(data)
	}).done(function(response){		
		if(response.RESULT == "FAIL"){
			alert(response.MESSAGE);			
		}else{
			if(type == "ID"){
				$("#txt4").show();
				$("#txt4_order").html(response.omsOrder.orderId);
			}else if(type == "PWD"){
				alert(response.omsOrder.orderPwd);
			}
		}				
	});
}
</script>

<script>
function btnFaceBookLoginClick(){
/* facebook init */
        $.ajaxSetup({ cache: true });
        $.getScript('//connect.facebook.net/en_UK/all.js', function(){
            FB.init({
                appId: '1763733237229832',
                status     : true, // check login status
                cookie     : true, // enable cookies to allow the server to access the session
                xfbml      : true  // parse XFBML
            });

            //페이지 로드 했을시 호출 (접속자의 상태 확인)
            FB.getLoginStatus(function(response) {

                if (response.status === 'connected') {
                    FaceBookLogin();

                } else if (response.status === 'not_authorized') {
                    // the user is logged in to Facebook, 
                    // but has not authenticated your app
                    //사용자 로그인
                	FB.login(function(response){
                		statusChangeCallback(response);
                	});
                } else {
                    // the user isn't logged in to Facebook.
                    //사용자 로그인 
	               	FB.login(function(response){
	               		statusChangeCallback(response);
	               	});
                }
            });
        }); /* facebook init */
}

function statusChangeCallback(response) {
    console.log('statusChangeCallback');
    console.log(response);
    // The response object is returned with a status field that lets the
    // app know the current login status of the person.
    // Full docs on the response object can be found in the documentation
    // for FB.getLoginStatus().
    if (response.status === 'connected') {
      // Logged into your app and Facebook.
      FaceBookLogin();
    } else if (response.status === 'not_authorized') {
      // The person is logged into Facebook, but not your app.
      
    } else {
      // The person is not logged into Facebook, so we're not sure if
      // they are logged into this app or not.
    }
  }

function FaceBookLogin(){
    FB.api('/me', function(user) {
        if (user) {
			console.log(user);
        }
    });
}

// 사용할 앱의 JavaScript 키를 설정해 주세요.
Kakao.init('21a0c8a9ac1cf3d700ba80a460cbe280');
function loginWithKakao() {
  // 로그인 창을 띄웁니다.
  Kakao.Auth.login({
    success: function(authObj) {
//       alert(JSON.stringify(authObj));
      getKakaoMemInfo(authObj);
    },
    fail: function(err) {
      alert(JSON.stringify(err));
    }
  });
};
function logOutWithKakao() {
  // 로그인 창을 띄웁니다.
  Kakao.Auth.logout();
};

function getKakaoMemInfo(data) {
	Kakao.Auth.setAccessToken(data.access_token);
	Kakao.API.request({
        url: '/v1/user/me',
        success: function(res) {
          alert(JSON.stringify(res));
        },
        fail: function(error) {
          alert(JSON.stringify(error))
        }
    });

}
</script>		
<div class="content">
<%-- 	<jsp:include page="/WEB-INF/views/gsf/layout/page/pc/navi_pc.jsp"/> --%>
	<!-- location_box -->
	<!-- <div class="location_box">
		<div class="location_inner">
			<ul>
				<li class="home"><span class="hide">홈</span></li>
				<li>
					<div class="selectbox type1">
						<label for="slc_cate">[해외]생활/주방/가족용품/파티</label>
						<select id="slc_cate">
							<option selected>[해외]생활/주방/가족용품/파티</option>
							<option>유아동 의류/ 잡화</option>
						</select>
					</div>
				</li>
				<li>
					<div class="selectbox type1">
						<label for="slc_cate2">유아동 의류/ 잡화</label>
						<select id="slc_cate2">
							<option selected>유아동 의류/ 잡화</option>
						</select>
					</div>
				</li>
			</ul>
		</div>
	</div> -->
	
	<jsp:include page="/WEB-INF/views/gsf/layout/page/pc/navi_pc.jsp" flush="false">
		<jsp:param name="pageId"  value="로그인|csCenter" />
	</jsp:include>
	
	
	<div class="mo_navi">
		<button type="button" class="btn_navi_prev">이전 페이지로..</button>
		<h2>로그인</h2>
	</div>
	<div class="inner">	
		<!-- login -->
		<div class="login_box">
			<h2>매일 Family 로그인</h2>
			<div class="section_01">
				<div class="tab_box">
					<!-- mo 전용 탭 -->
					<ul class="tab tab_menu">
						<li class="on"><a href="#">로그인</a></li> <!-- 활성화시 li class="on" 추가 -->
						<li><a href="#">비회원 주문조회</a></li>
					</ul>
					<!-- //mo 전용 탭 -->
					
					<div class="tabcont tabcont_01 tabcontShow">
		
						<div class="inp_box inp_placeholder"><input type="text" id="user_id" name="j_username" value="allen" /><label for="user_id">아이디</label></div>
						<div class="inp_box inp_placeholder"><input type="password" id="user_pw" name="j_password" value="1111" /><label for="user_pw">비밀번호</label></div>
						<div class="rel_box">
							<div class="chk_box">
								<input type="checkbox" id="auto_login" name="_spring_security_remember_me" class="inp_chk" />
								<label for="chk_login">자동 로그인</label>
								
								<input type="checkbox" id="id_save" class="inp_chk" />
								<label for="id_save">아이디 저장</label>
		
								<button type="button" class="btn_keyboard">한글 자판 보기</button>
								<div class="keyboard"><img src="/resources/img/mobile/bg/keyboard.gif" alt="한글 자판 이미지" /></div>
							</div>
						</div>
						
						<a href="#none" class="btn_style btn_login" onclick="javascript:login('PC','MEMBER')"><span>로그인</span></a>
										
						<ul class="util_link">
							<li><a href="#">아이디 찾기</a></li>
							<li><a href="#" class="srch_pw">비밀번호 찾기</a></li>
							<li><a href="#">회원가입</a></li>
						</ul>
										
						<div class="sns_login">
							<strong>
								<em>간편로그인</em>
							</strong>
		
							<ul>
								<li class="btn_face">
									<a href="javascript:btnFaceBookLoginClick();">
										<em>페이스북 로그인</em>
									</a>
								</li>
								<li class="btn_kaka">
									<a href="javascript:loginWithKakao();">
										<em>카카오톡 로그인</em>
									</a>
								</li>
								<li class="btn_naver">
									<a href="javascript:logOutWithKakao();">
										<em>네이버 로그인</em>
									</a>
								</li>
							</ul>
						</div>
		
						<div class="non_member">
							<a href="#none">비회원 주문</a>
		
							<div class="util">
								<a href="#none" class="btn_style2">비회원 구매하기</a>
								<a href="#none" class="btn_style2 srch_ordernum">비회원 주문조회</a>
							</div>
						</div>
						
						
		<!-- 			<div class="error" style="display: none;">일치하는 아이디/비밀번호가 없습니다.  다시 한 번 확인해 주시기 바랍니다.</div> -->
						
					</div>
					<div class="tabcont tabcont_02">
						<div class="inp_box inp_placeholder"><input type="text" id="nonmem_order" value="0000000042"/><label for="nonmem_order">주문번호</label></div>
						<div class="inp_box inp_placeholder"><input type="password" id="nonmem_pw" value="1111"/><label for="nonmem_pw">비밀번호</label></div>
						
						<a href="#none" class="btn_style btn_login"  onclick="javascript:login('MO','NONMEMBER')"><span>조회</span></a>
						
						<!--
						<div class="error">일치하는 주문내역이 없습니다.  다시 한 번 확인해 주시기 바랍니다.</div>
						-->
		
						<ul class="non_memberFind">
							<li>
								<a href="#none" class="btn_nonOrderNum">주문번호 찾기</a>
							</li>
							<li>
								<a href="#none" class="btn_nonPw">비밀번호 찾기</a>
							</li>
						</ul>
		
						<div class="dsc_box">
							<em>비회원 이용안내</em>
							<p>비회원으로 구매하실 경우에는 적립금, 프로모션 쿠폰, 이벤트참여 등 회원혜택을 적용 받으실 수 없습니다.</p>
						</div>
					</div>
				</div>
				
			</div>
		</div>
		<!-- //login -->
	</div>
</div>

<!-- layer - 비밀번호 찾기:회원 -->
<div class="pop_wrap ly_pw">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">비밀번호 찾기</h3>
		</div>

		<div class="pop_content">
			<div class="dsc_box">
				<p>
					주문 시 기재하신 이름, 연락처, 비밀번호로 조회 가능합니다.
				</p>
			</div>

			<div class="inp_list">
				<div class="inp_box inp_placeholder"><input type="text" id="mem_name1" value=""/><label for="mem_name1">이름</label></div>
				<div class="inp_box inp_placeholder"><input type="tel" id="mem_phone1" value=""/><label for="mem_phone1">휴대폰 ( ‘-’ 없이 입력)</label></div>
				<div class="inp_box inp_placeholder">
					<!-- <input type="text" id="pw1" value=""/><label for="pw1">비밀번호</label> -->
					<button type="button" class="btn_cal"><span>달력</span></button>
				</div>
			</div>

			<p class="txt_align">
				SMS로 비밀번호가 전송되었습니다.
			</p>

			<div class="btn_box btn_w100">
				<a href="#" class="btn_style purple"><span>확인</span></a>
			</div>
		</div>

		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>
<!-- //layer - 비밀번호 찾기:회원 -->


<!-- layer - 비회원 주문조회 -->
<div class="pop_wrap ly_orderNum">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">비회원 주문조회</h3>
		</div>

		<div class="pop_content">
			<div class="inp_list">
				<div class="inp_box inp_placeholder"><input type="text" id="nonmem_num3" value="0000000042"/><label for="nonmem_num3">주문번호</label></div>
				<div class="inp_box inp_placeholder"><input type="password" id="nonmem_pw3" value="1111"/><label for="nonmem_pw3">비밀번호</label></div>
			</div>

			<ul class="link">
				<li>
					<a href="#none" class="btn_nonOrderNum">주문번호 찾기</a>
				</li>
				<li>
					<a href="#none" class="btn_nonPw">비밀번호 찾기</a>
				</li>
			</ul>

			<div class="btn_box">
				<a href="#" class="btn_style purple" onclick="javascript:login('PC','NONMEMBER')"><span>조회</span></a>
			</div>
		</div>

		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>
<!-- //layer - 비회원 주문조회 -->

<!-- layer - 주문번호 찾기:비회원 -->
<div class="pop_wrap ly_findNum">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">주문번호 찾기</h3>
		</div>

		<div class="pop_content">
			<div class="dsc_box">
				<p>
					주문 시 기재하신 이름, 연락처, 비밀번호로 조회 가능합니다.
				</p>
			</div>

			<div class="inp_list">
				<div class="inp_box inp_placeholder"><input type="text" id="mem_name4" value="귝쐦눬"/><label for="mem_name4">이름</label></div>
				<div class="inp_box inp_placeholder"><input type="number" id="mem_phone4" value="08183406103"/><label for="mem_phone4">휴대폰 ( ‘-’ 없이 입력)</label></div>
				<div class="inp_box inp_placeholder">
					<input type="text" id="pw4" value="1111" /><label for="pw4">비밀번호</label>
					<!-- <button type="button" class="btn_cal"><span>달력</span></button> -->
				</div>
			</div>

			<p class="txt_align" id="txt4" style="display: none;">
				고객님의 주문번호는 <u id="txt4_order"></u> 입니다.
			</p>

			<div class="btn_box btn_w100">
				<a href="#none" class="btn_style purple" onclick="javascript:search_order('ID')"><span>확인</span></a>
			</div>
		</div>

		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>
<!-- //layer - 주문번호 찾기:비회원 -->

<!-- layer - 비밀번호 찾기:비회원 -->
<div class="pop_wrap ly_nonPw">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">비밀번호 찾기</h3>
		</div>

		<div class="pop_content">
			<div class="dsc_box">
				<p>
					주문 시 기재하신 이름, 연락처, 주문일로 조회 가능합니다.<br />
					비회원 주문 시 등록하신 정보가 기억나지 않으실 경우 <br />
					고객센터 <b>1588-8744</b>로 문의하여 주시기 바랍니다.
				</p>
			</div>

			<div class="inp_list">
				<div class="inp_box inp_placeholder"><input type="text" id="nonmem_name2" value="귝쐦눬"/><label for="nonmem_name2">이름</label></div>
				<div class="inp_box inp_placeholder"><input type="number" id="nonmem_phone2" value="08183406103"/><label for="nonmem_phone2">휴대폰 ( ‘-’ 없이 입력)</label></div>
				<div class="inp_box inp_placeholder">
					<input type="text" id="orderday" value="2016-06-30"/><label for="orderday">주문일</label>
					<button type="button" class="btn_cal"><span>달력</span></button>
				</div>
			</div>

			<div class="btn_box btn_w50">
				<a href="#" class="btn_style white pc_btn_close"><span>취소</span></a>
				<a href="#none" class="btn_style purple" onclick="javascript:search_order('PWD')"><span>확인</span></a>
			</div>
		</div>

		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>
<!-- //layer - 비밀번호 찾기 -->

<!-- layer - 내역없음 -->
<div class="layer_box layer_nodetail">
	<div class="inner">
		<div class="article">
			<strong class="logo">제로투세븐</strong>

			<b class="title">
				일치하는 주문내역이 없습니다.
			</b>

			<div class="box">
				<span>다시 한 번 확인해 주시기 바랍니다.</span>
			</div>
		</div>
		<button type="button" class="btn_style5 btn_submit">
			<em>확인</em>
		</button>
		<button type="button" class="btn_close">레이어팝업 닫기</button>
	</div>
</div>
<!-- //layer - 내역없음 -->