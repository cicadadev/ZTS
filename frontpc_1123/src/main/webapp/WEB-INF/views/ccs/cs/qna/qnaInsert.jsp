<%--
	화면명 : 고객센터 > 1:1 문의
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@page import="gcp.common.util.FoSessionUtil"%>
<%@page import="gcp.frontpc.common.util.FrontUtil"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="intune.gsf.common.utils.Config" %>

<%
	String deviceType = FoSessionUtil.getDeviceTypeCd(request);
	pageContext.setAttribute("deviceType", deviceType );
%>
<script type="text/javascript">
	
	function chgSelect(param) {
		//console.log($('option:selected', param).attr('value'));
		//$(param).prev().text($('option:selected', param).attr('value'));
		
		
		var labelId = $(param).prev()[0].id;
		if ((labelId).match(/(_domain)$/)) {
			var id = labelId.replace('_domain', '');
			
			if(common.isEmpty($('option:selected', param).attr('value'))){
				document.getElementById(id + '_email2').readOnly = false;
			}else{
				document.getElementById(id + '_email2').readOnly = true;
			}
			
			$('#' + id + '_email2').val($('option:selected', param).attr('value'));
		}else if((labelId).match(/(_brandId)$/)){
			$("#qnaForm").find("#brandId").val($('option:selected', param).attr('value'));
		}else if((labelId).match(/(_inquiryTypeCd)$/)){
			$("#qnaForm").find("#inquiryTypeCd").val($('option:selected', param).attr('value'));
			custcenter.qna.selectChange();
		}
	}
	
	$(document).ready(function(){
		//custcenter.setCsLayoutType("csmyqa");
		ccs.common.emailset('email', '${fn:trim(member.mmsMember.email)}');
		
		//ajax 이미지 업로드
		common.imageUpload("image", function(uploadPath){
			$('[name=img]').val(uploadPath);
		});
		
		// 브랜드관 1:1 문의
		if ("${brandId}" != "") {
			custcenter.qna.setBrandQna("${brandId}");
		}
		
	});
</script>
<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="/ccs/cs/main" name="url"/>
	<jsp:param value="고객센터|1:1 문의" name="pageNavi"/>
</jsp:include>

<div class="inner">
<div class="layout_type1 csmyqa">
<div class="column">
	<div class="csBox">
		<h3 class="title_type1">1:1 문의</h3>
		
		<form name="qnaForm" id="qnaForm">
		<input type="hidden" name="brandId" id="brandId"/>
		<input type="hidden" name="inquiryTypeCd" id="inquiryTypeCd"/>
		<input type="hidden" name="img"/>
		<div class="rw_tbBox">
			<ul class="rw_tb_tbody2">
				<li>
					<div class="tr_box">
						<div class="col1">
							<span class="group_inline">문의유형</span>
						</div>
						<div class="col2">
							<div class="group_block">
								<div class="select_box1 brand_add" id="inquiry_selectBox">
									<label id="select_inquiryTypeCd">30자 이내로 적어주세요</label>
									<tags:codeList code="INQUIRY_TYPE_CD" optionHead="선택" />
								</div>
								<div class="select_box1 brand_add" style="display:none;" id="brand_selectBox">
									<label id="select_brandId"></label>
									<tags:codeList code="BRAND_CD" optionHead="선택" />
								</div>
							</div>
						</div>
					</div>
				</li>
				
				<li style="display:none;">
					<div class="tr_box">
						<div class="col1">
							<span class="group_inline">문의상품</span>
						</div>
						<div class="col2">
							<div class="group_block moPath1">
								<input type="text" name="productName" id="qna_order1"  value="" class="inputTxt_style1 w476" placeholder="상품명" readonly/>
								<a href="#none" onClick="javascript:custcenter.qna.searchOrderProduct();" class="btn_sStyle4 sPurple1 btn_order_search">주문상품조회</a>
							</div>
							<div class="group_block column_2set">
								<input type="text" 	 name="saleproductName"     id="qna_order2"  value="" class="inputTxt_style1 w290" placeholder="옵션" readonly/>
								<input type="text"   name="orderId" 			id="qna_order3"  value="" class="inputTxt_style1 w160" placeholder="주문번호" readonly/>
								<input type="hidden" name="productId" 			id="qna_order4"  value="" class="inputTxt_style1 w160" />
								<input type="hidden" name="saleproductId" 		id="qna_order5"  value="" class="inputTxt_style1 w160" />
							</div>
						</div>
					</div>
				</li>
				
				<li>
					<div class="tr_box">
						<div class="col1">
							<span class="group_inline">문의제목</span>
						</div>
						<div class="col2">
							<div class="group_block">
								<div class="inputTxt_place1 w498">
									<label>30자 내로 적어주세요</label>
									<span>
										<input type="text" name="title" value="" />
									</span>
								</div>
							</div>
						</div>
					</div>
				</li>
				
				<li>
					<div class="tr_box">
						<div class="col1">
							<span class="group_inline">문의내용</span>
						</div>
						<div class="col2">
							<div class="group_block">
								<div class="txtarea_box w476 h200">
									<textarea name="detail"  rows="5" cols="10"></textarea>
								</div>
							</div>
						</div>
					</div>
				</li>							

				<c:if test="${deviceType != 'DEVICE_TYPE_CD.MW'}">
					<li>
						<div class="tr_box">
							<div class="col1">
								<span class="group_inline">이미지 첨부</span>
							</div>
							<div class="col2">
								<div class="group_block">
									<div class="inputFile_style1 w350">
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
				</c:if>

				<li>
					<div class="tr_box">
						<div class="col1 moShow">
							<span class="group_inline linexp_w40">알리미 서비스</span>
						</div>
						<div class="col2">
							<div class="group_block">
								<span class="needful">
									이메일<i>(필수)</i>
								</span>
								<!-- <input type="text" value="" class="inputTxt_style1 w202" /> 16.09.22 삭제 -->
								<!-- 16.09.22 : 메일 영역 수정 -->
								<div class="mailBox inlineblock">
									<input type="hidden" id="email" name="email" />
									<div class="inputTxt_place1">
										<label class="mo_only"></label>
										<span>
											<input type="text" id="email_email1"/>
										</span>
									</div>
											
									<span class="at">@</span>
									<div class="inputTxt_place1">
										<label></label>
										<span>
											<input type="text" id="email_email2"/>
										</span>
									</div>
									<div class="select_box1">
								 		<label id="email_domain" ></label>
										<tags:codeList code="EMAIL_DOMAIN_CD" optionHead="직접입력" />
										 		
										<!-- <select id="email_domain" onchange="javascript:ccs.common.emailDomain('email');" disabled>
											<option value="">직접입력</option>
											<option>nate.com</option>
											<option>naver.com</option>
											<option>daum.net</option>
											<option>zts.com</option> -->
										</select>
									</div>
								</div>
								<!-- //16.09.22 : 메일 영역 수정 -->
							</div>
							<div class="group_block">
								<label class="chk_style1">
									<em>
										<input type="checkbox" name="smsYn" id="qna_smsYn" value="Y" onClick="javascript:custcenter.qna.checkSmsYn('${member.mmsMember.phone2}');"/>
									</em>
									<span class="min_w46">SMS</span>
								</label>
								<!-- <input type="text" value="" class="inputTxt_style1 w202" /> 16.09.22 삭제 -->
								<!-- 16.09.22 : 연락처 영역 수정 -->
								<div class="inputMix_style2 inlineblock">
									<input type="hidden" id="phone" name="phone"/>
									<div class="select_box1">
								 		<label id="phone_areaCode">010</label>
								 		<tags:codeList code="MOBILE_NUMBER_CD" optionHead="선택" />
										<!-- <select id="phone_areaCode">
											<option value="">선택</option>
											<option>02</option>
											<option selected="selected">010</option>
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
							<%-- <div class="group_block">
								<span class="needful">
									이메일<i>(필수)</i>
								</span>
								<input type="text" name="email" value="${member.mmsMember.email}" class="inputTxt_style1 w202" />
							</div>
	
							<div class="group_block">
								<label class="chk_style1">
									<em>
										<input type="checkbox" name="smsYn" id="qna_smsYn" value="Y" onClick="javascript:custcenter.qna.checkSmsYn('${member.mmsMember.phone1}');"/>
									</em>
									<span class="min_w46">SMS</span>
								</label>
								<input type="text" name="phone" id="qna_phone" value="" class="inputTxt_style1 w202" />
							</div> --%>
						</div>
					</div>
				</li>
			</ul>
			
			<ul class="txt_notice">
				<li>
					문의하신 내용에 대한 답변은 <em>My 0to7 1:1 상담내역</em> 메뉴와 이메일을 통해 확인하실 수 있습니다.
				</li>
				<li >
					답변이 등록되면 APP푸시나 SMS로 알려드립니다.<br />
					(푸시알림은 APP > 설정>알림받기에서 변경 가능합니다.)
				</li>
			</ul>

			<div class="btn_wrapC btn2ea">
				<a href="javascript:custcenter.qna.clear();" class="btn_mStyle1 sWhite1">
					<em>취소</em>
				</a>
				<a href="javascript:custcenter.qna.save();" class="btn_mStyle1 sPurple1">
					<em>등록</em>
				</a>
			</div>
		</div>
		</form>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/cs/left_cs.jsp" />
</div>
</div>			


									