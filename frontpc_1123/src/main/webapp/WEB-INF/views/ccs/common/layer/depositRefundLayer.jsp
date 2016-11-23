<%--
	화면명 : 예치금 환불 신청 레이어
	작성자 : ian
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<script type="text/javascript" src="/resources/js/common/mypage.ui.js"></script>
<script type="text/javascript">
var chgSelect = function (param) {
	$(param).prev().text($('option:selected', param).attr('name'));
}

//select box 라벨링
function fnSelectStyle() {
	$(".select_box1 select").each(function() {
		$(this).siblings('label').text( $("option:selected", this).text() );
	});
	
	$(".pop_wrap .pop_content").on("change", ".select_box1 select", function() {
		$(this).siblings('label').text( $("option:selected", this).text() );
	});
}

//체크박스 체크
function fnChkStyle() {
	$(".chk_style1 input").each(function() {
		if($(this).is(':checked')){
			$(this).parent().addClass("selected");
		}
	});
	
	$(".chk_style1 input").on({
		"change" : function() {
			if($(this).is(':checked')){
				$(this).parent().addClass("selected");
			}else{
				$(this).parent().removeClass("selected");
			}
		}
	});
}

$(document).ready(function(){

	var bankCd = '${member.bankCd}';
 	if(common.isNotEmpty(bankCd)) {
 		$("option[value='"+ bankCd +"']").prop("selected", true);
		fnSelectStyle();
 	}
 	
	var auth = $('#accountAuthDt').val();
 	if(common.isNotEmpty(auth)) {
 		$('input[name="accountAuthDt"]').prop("checked", true).parent().addClass("selected");
		fnChkStyle();
 	}
});
</script>
<div>
	<input type="hidden" id="chkName" value="${member.accountHolderName}" />
	<input type="hidden" id="chkBankCd" value="${member.bankCd}" />
	<input type="hidden" id="chkAccountNo" value="${member.accountNo }" />
	<input type="hidden" id="accountAuthDt" value="${member.accountAuthDt }" />
</div>
<div class="pop_wrap ly_moneyRefund" id="depositRefundLayer" style="display:block;"> 
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">예치금 환불신청</h3>
		</div>
		<div class="pop_content">
			<div class="rw_tbBox write">
				<fieldset disabled="${member.accountHolderName eq '' ? '' : 'disabled'}">
					<ul class="rw_tb_tbody2">
						<li>
							<div class="tr_box">
								<div class="col1">
									<span class="group_inline">예금주</span>
								</div>
								<div class="col2">
									<div class="group_block">
										<div class="inputTxt_place1">
											<span>
												<input type="text" id="name" value="${member.accountHolderName}" placeholder="예금주명"/>
											</span>
										</div>
									</div>
								</div>
							</div>
						</li>
	
						<li>
							<div class="tr_box">
								<div class="col1">
									<span class="group_inline" > 은행선택 </span>
								</div>
								<div class="col2">
									<div class="group_block">
										<div class="select_box1 select">
											<label id="bankName"></label>
											<tags:codeList code="BANK_CD" optionHead="은행선택"/>
										</div>
									</div>
								</div>
							</div>
						</li>
	
						<li>
							<div class="tr_box">
								<div class="col1">
									<span class="group_inline">계좌번호</span>
								</div>
								<div class="col2">
									<div class="group_block">
										<div class="inpBox">
											<div class="inputTxt_place1">
												<span>
													<input type="text" id="accountNo" value="${member.accountNo }" placeholder="계좌번호 ‘-’ 없이 입력해주세요"/>
												</span>
											</div>
											<a href="javascript:void(0);" class="btn_sStyle4 sGray2" onclick=" ${member.accountHolderName eq '' ? 'javascript:mypage.refund.accountCertify();' : ''}"
											 >인증</a>
										</div>
									</div>
								</div>
							</div>
						</li>
					</ul>
				</fieldset>
				<div class="chkBox">
					<label class="chk_style1">
						<em id="autDt">
							<input type="checkbox" value="" name="accountAuthDt" />
						</em>
						<span>환불계좌 수집/설정 동의</span>
					</label>
					<label><a href="javascript:void(0);" class="btn_sStyle4 sGray2" onclick="common.pageMove('${pageScope.id}','','/mms/mypage/info/refund')">환불 계좌 관리</a></label>
				</div>
				<div>
				</div>
			</div>
			<ul class="notice">
				<li>무통장 입금 주문 혹은 전월 주문한 휴대폰 결제 내역을 취소/반품하실 경우, 보유하신 예치금을 환불 신청하실 경우 등록하신 계좌로 입금 처리해 드립니다.</li>
				<li>예금주명과 계좌번호의 정보가 다른 경우 등록/변경되지 않습니다.</li>
				<li>입력하신 환불계좌 정보는 등록일로부터 1년간 환불 기록 및 수정기록이 없을 경우, 금융정보 보호 정책에 따라 자동 삭제 됩니다.</li>
				<li>부정 사용 시 법적인 제재를 받을 수 있습니다.</li>
				<li>그 외 문의사항은 고객센터 1577-8744  또는 1:1 문의를 이용해주시기 바랍니다.</li>
			</ul>

			<div class="btn_wrapC btn2ea">
				<a href="javascript:void(0);" class="btn_mStyle1 sWhite1"  onclick="javascript:mypage.deposit.layerClose();">취소</a>
				<a href="javascript:void(0);" class="btn_mStyle1 sPurple1" onclick="javascript:mypage.deposit.refundRequest();">확인</a>
<!-- 				<a href="javascript:void(0);" class="btn_mStyle1 sPurple1 mo_only">등록</a> -->
			</div>
		</div>
		<button type="button" class="btn_x pc_btn_close" id="layerClose">닫기</button>
	</div>
</div>