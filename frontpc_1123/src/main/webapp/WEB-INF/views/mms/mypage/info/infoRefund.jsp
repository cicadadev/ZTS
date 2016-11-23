<%--
	화면명 : 마이페이지 > MY 정보관리 > 환불계좌 관리
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%-- <%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%> --%>
<script type="text/javascript">
$(document).ready(function(){
	if('${member.bankCd}' != '') {
		$("option[value=\""+'${member.bankCd}'+"\"").attr("selected", "selected");
		$("#bankName").text('${member.bankName}');
	}

	if('${member.accountAuthDt}' != '') {
		$('input[name="accountAuthDt"]').prop("checked", true).parent().addClass("selected");
		$('#isAuth').val('0000');
	}
});

var chgSelect = function (param) {
	// noting
}
</script>

<input type="hidden" id="chkName" value="${member.accountHolderName}" />
<input type="hidden" id="chkBankCd" value="${member.bankCd}" />
<input type="hidden" id="chkAccountNo" value="${member.accountNo }" />
<input type="hidden" id="isAuth" value="" />

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY정보관리|환불계좌관리" name="pageNavi"/>
</jsp:include>
<div class="inner">
<div class="layout_type1">
<div class="column">
	<div class="mypage myrefund">
		<h3 class="title_type1">환불계좌관리</h3>
		<h4 class="sub_tit1">환불계좌 정보</h4>
		<div class="rw_tbBox type1">
			<ul class="rw_tb_tbody2">
				<li>
					<div class="tr_box">
						<div class="col1">
							<span class="group_inline">예금주</span>
						</div>
						<div class="col2">
							<div class="group_block">
								<div class="inputTxt_place1">
									<label class="mo_only">예금주명</label>
									<span>
										<input type="text" id="name" value="${member.accountHolderName}" />
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
								<div class="select_box1">
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
										<label> <i>계좌번호 (</i>‘-’ 없이 입력해주세요<i>)</i></label>
										<span>
											<input type="text" id="accountNo" value="${member.accountNo }" style="ime-mode:disabled;"
											onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');"/>
										</span>
									</div>
									<a href="#none" class="btn_sStyle4 sGray2" onclick="javascript:mypage.refund.accountCertify();">인증</a>
								</div>
							</div>
						</div>
					</div>
				</li>
			</ul>
			<div class="chkBox">
				<label class="chk_style1">
					<em>
						<input type="checkbox" value="" name="accountAuthDt" />
					</em>
					<span>환불계좌 수집/설정 동의</span>
				</label>
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
			<a href="javascript:void(0);" class="btn_mStyle1 sWhite1" onclick="javascript:ccs.link.go('/mms/mypage/main', true, 1);">취소</a>
			<a href="javascript:void(0);" class="btn_mStyle1 sPurple1 pc_only" onclick="javascript:mypage.refund.regRefundAccount();">등록</a>
			<a href="javascript:void(0);" class="btn_mStyle1 sPurple1 mo_only" onclick="javascript:mypage.refund.regRefundAccount();">등록</a>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
</div>
</div>