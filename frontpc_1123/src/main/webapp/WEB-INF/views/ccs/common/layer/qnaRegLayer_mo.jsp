<%--
	화면명 : 상품QnA 작성 레이어
	작성자 : roy
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>


<c:set var="qnalayerClass" value="ly_qna"/>
<c:if test="${'true' eq isMobile}">
<c:set var="qnalayerClass" value="layer_qa"/>
</c:if>
						
						
<script>
$(document).ready(function(){
	if ('${isApp}'=='true') {
		$('input[type=file]').unbind('click').bind('click', function(){
			var item = {
				"name":$(this).attr('name')
			};

			var method = "fileupload";
			var json = encodeURIComponent(JSON.stringify(item));
			var applink = "app://" + method + "?json=" + json;
			
			//console.log(applink);
			window.location.href = applink;
			return false;
		});
	}
});

function onAppImagePicked(name, imageInfoJsonString) {
	var imageInfo = JSON.parse(decodeURIComponent(imageInfoJsonString));
	//alert(imageInfo);
	$('[name=img]').val(imageInfo.fullPath);
}
</script>							
<div class="pop_wrap ${qnalayerClass}" id="qnaPopup">

	<form id="productQnaForm">
		<input type="hidden" name="productId" value="${product.productId }" />
		<input type="hidden"   name="saleproductId" value="" />
		<input type="hidden"   name="img" value="" />
		<div class="pop_inner">
			<div class="pop_header type1">
				<h3 class="tit">Q&amp;A</h3>
			</div>
			
			<div class="pop_content">
				<strong class="sub_tit">${product.name }</strong>

	<%--옵션목록 조회 --%>
	<c:forEach var="optionName" items="${product.pmsProductoptions }" varStatus="index">
				<div class="select_box1" style="width:100%;margin-left:0%">
					<label>선택하세요</label> 
						<select optionName="${ optionName.optionName}" name="qnaOptions_${index.count }"
							onchange="pms.detail.selectQnaOption(this.name, ${fn:length(product.pmsProductoptions)} ,'${product.productId }')">
						<option value="">선택하세요</option>
						<c:forEach var="option" items="${optionName.optionValues}">
							<option value="${option.optionValue }" ${option.realStockQty==0 ? 'disabled' : '' }>${option.optionValue }</option>
						</c:forEach>
					</select>
				</div>								
	</c:forEach>	
				<div class="inputTxt_place1">
					<label>제목</label>
					<span>
					    <input type="text" name="title" value="">
					</span>
				</div>

				<div class="txtarea_box">
					<textarea rows="5" cols="10" id="qaTxt" name="detail"></textarea>
				</div>

				<div class="secret_chk">
					<label class="chk_style1">
						<em>
							<input type="checkbox" value="Y" name="secretYn" id="secretYn">
						</em>
						<span>비밀글로 문의하기</span>
					</label>
				</div>
<c:if test="${isApp eq 'true' }">
				<div class="addFile_box">
					<div class="add">
						<input type="file" value="" name="fileinput">
						<a href="#none" class="add_photo">첨부파일</a>
					</div>

<!-- 					<div class="imgBox">
						<img src="img/mobile/temp/addPhoto.jpg" alt="">
						<a href="#none" class="btn_del">이미지 삭제</a>
					</div> -->
				</div>
</c:if>
				<ul class="txt_list">
					<li>
						교환/반품에 대한 문의는 고객센터 1:1 문의를 이용해주세요.
					</li>
					<li>
						Q&amp;A에 대한 답변은 마이쇼핑 Q&amp;A메뉴에서 확인 가능합니다.
					</li>
					<li>
						답변이 등록되면 APP푸시로 알려드립니다.<br>
						(푸시알림은 설정&gt;알림받기에서 변경 가능합니다.)
					</li>
				</ul>

				<div class="btn_wrapC btn2ea">
					<a href="#none" class="btn_mStyle1 sWhite1" onclick="closeLayer('qnaPopup');">취소</a> 
					<a href="#none" class="btn_mStyle1 sPurple1" onclick="insertProductQna();">등록</a>				
				</div>
			</div>			
			<button type="button" class="btn_x pc_btn_close">닫기</button>
		</div>
	</form>
</div>