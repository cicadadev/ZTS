<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<script type="text/javascript" src="/resources/js/common/common.ui.js"></script>
<script type="text/javascript">
//우편번호 팝업
var openAddressPopup = function(){
	ccs.layer.searchAddressLayer.open(null,function(data){		
		$("#zipCd").val(data.zipCd);
		$("#address1").val(data.address1);
		$("#address2").val(data.address2);
	});
}
$(document).ready(function(){
	ccs.common.alliance.selectorCall();	
	common.imageUpload("image", function(uploadPath){
		$('[name=img]').val(uploadPath);
	});
});

function chgSelect(param) {
	var labelId = $(param).prev()[0].id;
	if ((labelId).match(/(_domain)$/)) {
		var id = labelId.replace('_domain', '');
		
		if(common.isEmpty($('option:selected', param).attr('value'))){
			document.getElementById(id + '_email2').readOnly = false;
		}else{
			document.getElementById(id + '_email2').readOnly = true;
		}
		
		$('#' + id + '_email2').val($('option:selected', param).attr('value'));
	}
}

</script>

	<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
		<jsp:param value="입점제휴문의" name="pageNavi"/>
	</jsp:include>
	
	<div class="inner">
		<form name="businessInquiryForm" id="businessInquiryForm" >
		<input type="hidden" name="img"/>
		<div class="policy_box">
			<h3 class="title_type1">
				입점제휴 문의
			</h3>

			<div class="affiliate">
				<h4>제로투세븐과 함께 유,아동 문화를 이끌어갈 개인 및 업체를 모십니다.</h4>
				<div class="rw_tbBox">
					<ul class="rw_tb_tbody2">
						<li>
							<div class="tr_box">
								<div class="col1">
									<span class="group_inline">회사명</span>
								</div>
								<div class="col2">
									<div class="group_block">
										<input type="text" name="name" value="" class="inputTxt_style1" maxlength="50"/>
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
									<div class="group_block">
										<input type="hidden" id="phone" name="phone"/>
										<div class="inputMix_style2">
											<div class="select_box1">
										 		<label id="phone_areaCode">02</label>
										 		<tags:codeList code="PHONE_NUMBER_CD" optionHead="선택" />
												<!-- <select id="phone_areaCode">
													<option>02</option>
													<option selected="selected">032</option>
													<option>010</option>
													<option>011</option>
													<option>012</option>
												</select> -->
											</div>
											<span class="hyphen">-</span>
											<div class="inputTxt_place1">
												<label></label>
												<span>
													<input type="text" id="phone_num1" style="ime-mode:disabled;" maxlength="4" 
													onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');"/>
												</span>
											</div>
											<span class="hyphen">-</span>
											<div class="inputTxt_place1">
												<label></label>
												<span>
													<input type="text" id="phone_num2" style="ime-mode:disabled;" maxlength="4" 
													onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');"/>
												</span>
											</div>
										</div>
										<!-- //16.09.22 : 연락처 영역 수정 -->
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
									<div class="group_block">
										<div class="address1">
											<input type="text" id="zipCd" name="zipCd" value="" class="inputTxt_style1" readonly/>
											<a href="javascript:openAddressPopup();" class="btn_sStyle4 sGray2">우편번호검색</a>
										</div>
									</div>
									<div class="group_block">
										<div class="address2">
											<input type="text" id="address1"  name="address1" value="" class="inputTxt_style1" readonly/>
											<input type="text" id="address2"  name="address2" value="" class="inputTxt_style1" readonly/>
										</div>
									</div>
								</div>
							</div>
						</li>
						<li>
							<div class="tr_box">
								<div class="col1">
									<span class="group_inline">홈페이지 주소</span>
								</div>
								<div class="col2">
									<div class="group_block">
										<div class="inputTxt_place1 url" >
											<label>http://</label>
											<span>
												<input type="text" id="alliance_homepageUrl" name="homepageUrl" value="" maxlength="100"/>
											</span>
										</div>
									</div>
								</div>
							</div>
						</li>
						<li>
							<div class="tr_box">
								<div class="col1">
									<span class="group_inline">담당부서</span>
								</div>
								<div class="col2">
									<div class="group_block">
										<input type="text" name="depName" value="" class="inputTxt_style1" maxlength="50"/>
									</div>
								</div>
							</div>
						</li>
						<li>
							<div class="tr_box">
								<div class="col1">
									<span class="group_inline">담당자 이름</span>
								</div>
								<div class="col2">
									<div class="group_block">
										<input type="text" name="managerName" value="" class="inputTxt_style1" maxlength="50"/>
									</div>
								</div>
							</div>
						</li>
						<li>
							<div class="tr_box">
								<div class="col1">
									<span class="group_inline">담당자 연락처</span>
								</div>
								<div class="col2">
									<div class="group_block">
										<input type="hidden" id="managerPhone1" name="phone"/>
										<div class="inputMix_style2">
											<div class="select_box1">
										 		<label id="managerPhone1_areaCode"></label>
										 		<tags:codeList code="MOBILE_NUMBER_CD" optionHead="선택" />
												<!-- <select id="managerPhone1_areaCode">
													<option>02</option>
													<option selected="selected">032</option>
													<option>010</option>
													<option>011</option>
													<option>012</option>
												</select> -->
											</div>
											<span class="hyphen">-</span>
											<div class="inputTxt_place1">
												<label></label>
												<span>
													<input type="text" id="managerPhone1_num1" style="ime-mode:disabled;" maxlength="4" 
													onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');"/>
												</span>
											</div>
											<span class="hyphen">-</span>
											<div class="inputTxt_place1">
												<label></label>
												<span>
													<input type="text" id="managerPhone1_num2" style="ime-mode:disabled;" maxlength="4" 
													onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');"/>
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
									<span class="group_inline">이메일</span>
								</div>
								<div class="col2">
									<div class="group_block">
										<div class="mailBox">
											<input type="hidden" id="managerEmail" name="managerEmail" />
											<div class="inputTxt_place1">
												<span>
													<input type="text" id="managerEmail_email1"  value="" />
												</span>
											</div>
											<span class="at">@</span>
											<div class="inputTxt_place1">
												<label></label>
												<span>
													<input type="text" id="managerEmail_email2"  value="" />
												</span>
											</div>
											<div class="select_box1">
										 		<label id="managerEmail_domain" ></label>
										 		<tags:codeList code="EMAIL_DOMAIN_CD" optionHead="선택" />
										 		<!-- 
												<select id="managerEmail_domain" onchange="javascript:ccs.common.emailDomain('managerEmail');">
													<option value="">직접입력</option>
													<option>nate.com</option>
													<option>naver.com</option>
													<option>daum.net</option>
												</select> -->
											</div>
										</div>
										<!-- <div class="email_wrap">
											<input type="text" id="alliance_email1" name="email1" value="" class="inputTxt_style1" maxlength="50"/>
											<span>@</span>
											<div class="select_box1">
												<label>naver.com</label>
												<select id="alliance_email2" name="email2">
													<option>nate.com</option>
													<option selected="selected">naver.com</option>
													<option>daum.net</option>
												</select>
											</div>
										</div> -->
									</div>
								</div>
							</div>
						</li>
						<li>
							<div class="tr_box">
								<div class="col1">
									<span class="group_inline">내용<br />(회사소개/제품소개)</span>
								</div>
								<div class="col2">
									<div class="group_block">
										<div class="txtarea_box">
											<textarea rows="5" name="detail" cols="10" maxlength="4000"></textarea>
										</div>
									</div>
								</div>
							</div>
						</li>
						<li>
							<div class="tr_box">
								<div class="col1">
									<span class="group_inline">카테고리</span>
								</div>
								<%-- ajax 영역 --%>
								<div class="col2" id="allianceSelectorDiv">
								</div>
							</div>
						</li>
						<li>
							<div class="tr_box">
								<div class="col1">
									<span class="group_inline">이미지 첨부</span>
								</div>
								<div class="col2">
									<div class="group_block">
										<div class="inputFile_style1" >
											<div>
												<input type="text" name="imgName" value="이미지명" class="file_url" />
												<input type="file" name="image" value="" />
											</div>
											<a href="#none" class="btn_file">파일첨부</a>
										</div>
									</div>
								</div>
							</div>
						</li>
						<li>
							<div class="tr_box">
								<div class="col1">
									<span class="group_inline">동의사항</span>
								</div>
								<div class="col2">
									<div class="group_block">
										<label class="chk_style1">
											<em>
												<input type="checkbox" id="agreeChk" name="agreeChk" />
											</em>
											<span>약관에 동의합니다.</span>
										</label>
										<a href="javascript:ccs.go_url('/ccs/common/privacy?footer_pc_column=2');" class="btn_agree">개인정보 수집 이용에 대한 동의</a>
									</div>
								</div>
							</div>
						</li>
					</ul>
				</div>

				<div class="btn_wrapC">
					<a href="javascript:ccs.common.alliance.cancle();" class="btn_mStyle1 sWhite1">취소</a>
					<a href="javascript:ccs.common.alliance.insert();" class="btn_mStyle1 sPurple1">신청</a>
				</div>
			</div>
		</div>
		</form>
	</div>

<!-- 
<div class="content" style="">
	<div class="location_box">
		<div class="location_inner">
			<ul>
				<li>홈</li>
				<li>
					입점제휴안내
				</li>
			</ul>
		</div>
	</div>

	<div class="mo_navi">
		<button type="button" class="btn_navi_prev">이전 페이지로..</button>
		<h2>입점제휴안내</h2>
	</div>

	<div class="inner">
		<div class="policy_box">
			<h3 class="title_type1">
				입점제휴안내
			</h3>

			<div class="alliance_note">
				<strong>제로투세븐과 함께 유,아동 문화를 이끌어갈 개인 및 업체를 모십니다</strong>
				<p>
					해당 카테고리 담당자에게 연락 주시면 검토 후 회신 드리도록 하겠습니다.
				</p>
			</div>

			<div class="policy_info">
				<table class="tb_type3">
					<colgroup>
						<col class="col1" />
						<col class="col2" />
						<col class="col99" />
					</colgroup>
					<thead>
						<tr>
							<th>카테고리</th>
							<th>담당자</th>
							<th>이메일</th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td>
								<span>유아동 의류/잡화/임부복/여성패션</span>
							</td>
							<td>이도단 MD</td>
							<td>
								<a href="mailto:ldd0929@maeil.com">ldd0929@maeil.com</a>
							</td>
						</tr>
						<tr>
							<td>
								<span>완구/도서/이동/외출/발육용품</span>
							</td>
							<td>마하영 MD</td>
							<td>
								<a href="mailto:mahayoung@maeil.com">mahayoung@maeil.com</a>
							</td>
						</tr>
						<tr>
							<td>
								<span>출산/수유/기저귀/물티슈</span>
							</td>
							<td>정미린 MD</td>
							<td>
								<a href="mailto:jalfls@maeil.com">jalfls@maeil.com</a>
							</td>
						</tr>
						<tr>
							<td>
								<span>분유/유아식품/일반식품</span>
							</td>
							<td>김자해 MD</td>
							<td>
								<a href="mailto:kjh0725@maeil.com">kjh0725@maeil.com</a>
							</td>
						</tr>
						<tr>
							<td>
								<span>화장품/목욕/위생/생활/가구/침구</span>
							</td>
							<td>김수진 MD</td>
							<td>
								<a href="mailto:kimsu072@maeil.com">kimsu072@maeil.com</a>
							</td>
						</tr>
						<tr>
							<td>
								<span>행사/이벤트 문의</span>
							</td>
							<td>이지희 과장</td>
							<td>
								<a href="mailto:ssen03@maeil.com">ssen03@maeil.com</a>
							</td>
						</tr>
						<tr>
							<td>
								<span>마케팅/제휴 문의</span>
							</td>
							<td>최혁중 과장</td>
							<td>
								<a href="mailto:hyukx@maeil.com">hyukx@maeil.com</a>
							</td>
						</tr>
						<tr>
							<td>
								<span>중국 제휴 문의</span>
							</td>
							<td>곽대영 과장</td>
							<td>
								<a href="mailto:daeyoung@maeil.com">daeyoung@maeil.com</a>
							</td>
						</tr>
					</tbody>
				</table>
			</div>
		</div>
	</div>
</div>

 -->
