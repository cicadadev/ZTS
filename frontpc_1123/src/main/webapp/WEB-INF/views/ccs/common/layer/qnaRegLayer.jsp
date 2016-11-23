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
							
<div class="pop_wrap ${qnalayerClass}" id="qnaPopup">

	<form id="productQnaForm">
		<input type="hidden" name="productId" value="${product.productId }" />
		<input type="hidden"   name="saleproductId" value="" />
		<div class="pop_inner">
			<div class="pop_header type1">
				<h3 class="tit">Q&amp;A</h3>
			</div>
			<div class="pop_content">
				<div class="tbl_box">
					<table>
						<caption>Q&amp;A 쓰기</caption>
						<colgroup>
							<col style="width: 27%;">
							<col style="width: 73%;">
						</colgroup>
						<tbody>
							<tr>
								<th scope="row">상품명</th>
								<td>${product.name }</td>
							</tr>
							<tr>
								<th scope="row">옵션 선택</th>
								<td>
									<div class="column_2set">
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
									</div>
								</td>
							</tr>
							<tr>
								<th scope="row">제목</th>
								<td>
									<div class="inp_box">
										<input type="text" name="title" value="" class="inputTxt_style1">
									</div>
								</td>
							</tr>
							<tr class="valignT">
								<th scope="row">내용</th>
								<td>
									<div class="txtarea_box">
										<textarea rows="5" cols="10" id="qaTxt" name="detail"></textarea>
									</div>
								</td>
							</tr>
							<tr>
								<th scope="row">비밀글</th>
								<td><label class="chk_style1"> 
									<em> 
										<input type="checkbox" value="Y" name="secretYn" id="secretYn">
									</em> 
									<span>비밀글로 문의하기</span>
								</label></td>
							</tr>
						</tbody>
					</table>
				</div>
				<ul class="notice">
					<li>교환 반품에 대한 문의는 고객센터 1:1 문의를 이용해주세요.</li>
					<li>Q&amp;A에 대한 답변은 마이쇼핑 Q&amp;A메뉴에서 확인 가능합니다.</li>
					<li>답변이 등록되면 APP푸시로 알려드립니다. (푸시알림은 모바일 &gt; 설정 &gt; 알림받기에서 변경 가능합니다.)</li>
				</ul>
				<div class="btn_box">
					<a href="#none" class="btn_mStyle1 sWhite1" onclick="closeLayer('qnaPopup');">취소</a> 
					<a href="#none" class="btn_mStyle1 sPurple1" onclick="insertProductQna();">등록</a>
				</div>
			</div>
			<button type="button" class="btn_x pc_btn_close">닫기</button>
		</div>
	</form>
</div>