<%--
	화면명 : 카드사 제휴 - 하나카드
	작성자 : eddie
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<script>

function fnPopPosition(target_pop) {
	$(target_pop).show();
 	$(target_pop).height( $(document).height() );

	var base_top = ($(window).height() - $(".pop_inner", target_pop).innerHeight()) / 2;
	var target_po = base_top + $(window).scrollTop() + $(".pop_inner", target_pop).innerHeight();
	var target_poMin = 10;
	var target_poMax = base_top + $(window).scrollTop() - (target_po - $(document).height() + 10);

	if(target_po > $(document).height()){
		$(".pop_inner", target_pop).css({"marginTop" : target_poMax + "px"});
	}else if(base_top + $(window).scrollTop() < 0){
		$(".pop_inner", target_pop).css({"marginTop" : target_poMin + "px"});
	}else{
		$(".pop_inner", target_pop).css({"marginTop" : base_top + $(window).scrollTop() + "px"});
	}
}

/* 작은레이어 팝업 위치 설정 : 2016.08.22 */
function fnLayerPosition(target_pop) {
	$(target_pop).show();
	$(target_pop).height( $(document).height() );

	var base_top = ($(window).height() - $(" > .box", target_pop).innerHeight()) / 2;
	$(" > .box", target_pop).css({"marginTop" : base_top + $(window).scrollTop() + "px"});
}


var viewLayer = function(callback){
	//$(".layer_style1").show();
	mms.common.loginCheck(function(isLogin){
		
		if(isLogin){
			
			// 존재여부 체크
			checkExist(function(result){
				
				if(result=='S'){
					callback();
				}else {
					alert("고객님은 이미 혜택을 받고 계십니다.");
				}
			
			});
		}
	});
}


var openCardLayer = function(){
	
	if(global.channel.isMobile!='true'){
		//fnPopPosition( $(".card_reg").not(".layer_style1") );
		ccs.layer.copyLayerToContentChild("card_reg");
	}else{
		fnLayerPosition( $(".mobile .card_reg") );
	}
	
}

var close1 = function(){
	$(".card_reg").hide();
	
}

var checkExist = function(callback){
	
	$.ajax({
		method : "POST",
		url : "/api/sps/event/card/check"
	}).done(function(result) {
		callback(result);
	});
}

var save = function(){
	
	var s = '';
	$('.psc_register input').each(function(){
		s += $(this).val();
	});
	
	if(s=='' || s.length < 8){
		alert("카드번호를 입력해 주세요.");
		return false;
	}
	var affiliatecardCd = "";
	if('${param.cardType}'=='1'){
		affiliatecardCd = "AFFILIATECARD_CD.HANA";
	}else if('${param.cardType}'=='2'){
		affiliatecardCd = "AFFILIATECARD_CD.BC";
	}else if('${param.cardType}'=='3'){
		affiliatecardCd = "AFFILIATECARD_CD.LOTTE1";
	}else if('${param.cardType}'=='4'){
		affiliatecardCd = "AFFILIATECARD_CD.LOTTE2";
	}else if('${param.cardType}'=='5'){
		affiliatecardCd = "AFFILIATECARD_CD.LOTTE3";
	}else if('${param.cardType}'=='6'){
		affiliatecardCd = "AFFILIATECARD_CD.SHINHAN";
	}
	var p = {affiliatecardNo : s, affiliatecardCd : affiliatecardCd};
	
		
		$.ajax({
			method : "POST",
			url : "/api/sps/event/card/save",
			data : {affiliatecardNo : s, affiliatecardCd : affiliatecardCd}
		}).done(function(result) {
			
			if(result=='S'){
				alert("등록이 완료되었습니다.");
				close1();
				window.location.reload();
			}else if(result=='F'){
				alert("카드번호 확인 후 다시 입력해주세요.");
			}
			
		});

}
</script>
<c:if test="${isMobile!='true'}">					
<div class="pop_wrap card_reg" style="display:none;">
	<div class="pop_inner" style="width:388px;">
		<div class="pop_header type1">
			<h3 class="tit">제로투세븐닷컴 VIP 등록</h3>
		</div>
		<div class="pop_content">
</c:if>	
<c:if test="${isMobile=='true'}">
<div class="layer_style1 card_reg"  style="display:none;">
	<div class="box register width388">
		<div class="conArt">
			<div class="lsTitleWrap">
				<strong class="title">제로투세븐닷컴 VIP 등록</strong>
			</div>
			<div class="conBox">
</c:if>				
			<div class="psc_register">
				<div class="pscr_txt01">
					<strong class="point">${param.cardName}</strong> 번호 앞 8자리를 입력하세요.
				</div>
				
				<div class="pscr_input">
				
					<div class="inputTxt_place1">
						<span>
							<input type="password" maxlength="1" value="" title="번호 첫번째자리">
						</span>
					</div>
					<div class="inputTxt_place1">
						<span>
							<input type="password" maxlength="1" value="" title="번호 두번째자리">
						</span>
					</div>
					<div class="inputTxt_place1">
						<span>
							<input type="password" maxlength="1" value="" title="번호 세번째자리">
						</span>
					</div>
					<div class="inputTxt_place1">
						<span>
							<input type="password" maxlength="1" value="" title="번호 네번째자리">
						</span>
					</div>
					<span class="hyphen">-</span>
					<div class="inputTxt_place1">
						<label></label>
						<span>
							<input type="password" maxlength="1" value="">
						</span>
					</div>
					<div class="inputTxt_place1">
						<label></label>
						<span>
							<input type="password" maxlength="1" value="">
						</span>
					</div>
					<div class="inputTxt_place1">
						<label></label>
						<span>
							<input type="password" maxlength="1" value="">
						</span>
					</div>
					<div class="inputTxt_place1">
						<label></label>
						<span>
							<input type="password" maxlength="1" value="">
						</span>
					</div>
				</div>
				<div class="pscr_txt02">
					※ 입력하신 카드번호는 저장되지 않습니다. <br>
					※ 제로투세븐닷컴 VIP혜택 문의: 1588-8744
				</div>
			</div>
<c:if test="${isMobile=='true'}">
		</div>
		</div>
		<div class="btn_wrapC btn2ea">
			<a href="#none" onclick="close1()" class="btn_mStyle1 sGray1">취소</a>
			<a href="#none" onclick="save()" class="btn_mStyle1 sPurple1">등록</a>
		</div>
		<button type="button" class="btn_close">레이어팝업 닫기</button>
	</div>
</div>
</c:if>

<c:if test="${isMobile!='true'}">			
			<div class="btn_wrapC btn2ea">
				<a href="#none" class="btn_mStyle1 sWhite1" onclick="close1()">취소</a>
				<a href="#none" class="btn_mStyle1 sPurple1" onclick="save()">등록</a>
			</div>
		</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>
</c:if>
