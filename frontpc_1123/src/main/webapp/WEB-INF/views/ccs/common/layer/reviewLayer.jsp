<%--
	화면명 : 상품평 작성 레이어
	작성자 : roy
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@page import="java.util.*"%>

<%

	response.setContentType("text/html; charset=UTF-8");
	request.setCharacterEncoding("UTF-8");
 
	
%>

<script type="text/javascript" src="/resources/js/common/mypage.ui.js"></script>

<c:if test="${isMobile}"> 
	<script type="text/javascript" src="/resources/js/mo.js"></script>
	<script type="text/javascript" src="/resources/js/mms/mms.mypage.js"></script>	
</c:if>
<c:if test="${!isMobile}">
	<script type="text/javascript" src="/resources/js/pc.js"></script>
	<script type="text/javascript" src="/resources/js/mms/mms.mypage.js"></script>	
</c:if>
<c:if test="${isMobile}"> 
<script type="text/javascript">
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
	
		if(!common.isEmpty($('[name=img1]').val().trim())){
			var html=""; 
			
			html="<img id=\"reviewImageIndex1\" src=\"" + "${_IMAGE_DOMAIN_}" + $('[name=img1]').val() + "\" alt=\"\" style=\"max-height:100%;\"/>"
			+"<a id=\"reviewImageIndex1\" href=\"javascript:mypage.review.imageDelete('1');\" class=\"btn_del\">삭제</a>";
				
			$('#review_img_preview_mo1').append(html);
		}
		
		if(!common.isEmpty($('[name=img2]').val().trim())){
			var html=""; 
			
			html="<img id=\"reviewImageIndex2\" src=\"" + "${_IMAGE_DOMAIN_}" + $('[name=img2]').val() + "\" alt=\"\" style=\"max-height:100%;\"/>"
			+"<a id=\"reviewImageIndex2\" href=\"javascript:mypage.review.imageDelete('2');\" class=\"btn_del\">삭제</a>";
				
			$('#review_img_preview_mo2').append(html);
		}
		
		if(!common.isEmpty($('[name=img3]').val().trim())){
			var html=""; 
			
			html="<img id=\"reviewImageIndex3\" src=\"" + "${_IMAGE_DOMAIN_}" + $('[name=img3]').val() + "\" alt=\"\" style=\"max-height:100%;\"/>"
			+"<a id=\"reviewImageIndex3\" href=\"javascript:mypage.review.imageDelete('3');\" class=\"btn_del\">삭제</a>";
				
			$('#review_img_preview_mo3').append(html);
		}
		
		//ajax 이미지 업로드
		/* common.imageUpload("image", function(uploadPath){
			console.log(uploadPath);
			var imageIndex = '';
			if(common.isEmpty($('[name=img1]').val())){
				$('[name=img1]').val(uploadPath);
				imageIndex = '1';
			}else{
				if(common.isEmpty($('[name=img2]').val())){
					imageIndex = '2';
					$('[name=img2]').val(uploadPath);
				}else if(common.isEmpty($('[name=img3]').val())){
					imageIndex = '3';
					$('[name=img3]').val(uploadPath);
				}else{
					alert('이미지는 3개까지 등록 가능합니다.');
				}			
			}
			
			var html=""; 
			mypage.review.imageDelete('4');
			html="<img id=\"reviewImageIndex" + imageIndex + "\" src=\"" + "${_IMAGE_DOMAIN_}" + uploadPath + "\" alt=\"\" style=\"max-height:100%;\"/>"
				+"<a id=\"reviewImageIndex" + imageIndex + "\" href=\"javascript:mypage.review.imageDelete('" + imageIndex + "');\" class=\"btn_del\">삭제</a>";
				
			if(!common.isEmpty(imageIndex)){
				$('#review_img_preview_mo' + imageIndex).append(html);
			}

		}); */
	});
	
	function onAppImagePicked(name, imageInfoJsonString) {
		var imageInfo = JSON.parse(decodeURIComponent(imageInfoJsonString));
		//alert(imageInfo);
		var imageIndex = '';
		var path = imageInfo.fullPath;
		if(common.isEmpty($('[name=img1]').val())){
			$('[name=img1]').val(path);
			imageIndex = '1';
		}else{
			if(common.isEmpty($('[name=img2]').val())){
				imageIndex = '2';
				$('[name=img2]').val(path);
			}else if(common.isEmpty($('[name=img3]').val())){
				imageIndex = '3';
				$('[name=img3]').val(path);
			}else{
				alert('이미지는 3개까지 등록 가능합니다.');
			}			
		}
		
		var html=""; 
		mypage.review.imageDelete('4');
		html="<img id=\"reviewImageIndex" + imageIndex + "\" src=\"" + "${_IMAGE_DOMAIN_}" + path + "\" alt=\"\" style=\"max-height:100%;\"/>"
			+"<a id=\"reviewImageIndex" + imageIndex + "\" href=\"javascript:mypage.review.imageDelete('" + imageIndex + "');\" class=\"btn_del\">삭제</a>";
			
		if(!common.isEmpty(imageIndex)){
			$('#review_img_preview_mo' + imageIndex).append(html);
		}
	}
	
</script>
</c:if>
<c:if test="${!isMobile}">
<script type="text/javascript">
	$(document).ready(function(){
		
		$("#imageButton").bind("click", function () {
		    $("#imageFile").trigger("click");
		});
		
		if(!common.isEmpty($('[name=img1]').val().trim())){
			var html=""; 
			
			html="<li id=\"reviewImageIndex1\">"
				+"<img src=\"" + "${_IMAGE_DOMAIN_}" + $('[name=img1]').val() + "\" alt=\"\" style=\"max-height:100%;\"/>"
				+"<a href=\"javascript:mypage.review.imageDelete('1');\" class=\"btn_reviewDel\">삭제</a>"
				+"</li>";
				
			$('#review_img_preview').append(html);
		}
		
		if(!common.isEmpty($('[name=img2]').val().trim())){
			var html=""; 
			
			html="<li id=\"reviewImageIndex2\">"
				+"<img src=\"" + "${_IMAGE_DOMAIN_}" + $('[name=img2]').val() + "\" alt=\"\" style=\"max-height:100%;\"/>"
				+"<a href=\"javascript:mypage.review.imageDelete('2');\" class=\"btn_reviewDel\">삭제</a>"
				+"</li>";
				
			$('#review_img_preview').append(html);
		}
		
		if(!common.isEmpty($('[name=img3]').val().trim())){
			var html=""; 
			
			html="<li id=\"reviewImageIndex3\">"
				+"<img src=\"" + "${_IMAGE_DOMAIN_}" + $('[name=img3]').val() + "\" alt=\"\" style=\"max-height:100%;\"/>"
				+"<a href=\"javascript:mypage.review.imageDelete('3');\" class=\"btn_reviewDel\">삭제</a>"
				+"</li>";
				
			$('#review_img_preview').append(html);
		}
		
		//ajax 이미지 업로드
		common.imageUpload("image", function(uploadPath){
			var imageIndex = '';
			if(common.isEmpty($('[name=img1]').val())){
				$('[name=img1]').val(uploadPath);
				imageIndex = '1';
			}else{
				if(common.isEmpty($('[name=img2]').val())){
					imageIndex = '2';
					$('[name=img2]').val(uploadPath);
				}else if(common.isEmpty($('[name=img3]').val())){
					imageIndex = '3';
					$('[name=img3]').val(uploadPath);
				}else{
					alert('이미지는 3개까지 등록 가능합니다.');
				}			
			}
			
			var html=""; 
			mypage.review.imageDelete('4');
			html="<li id=\"reviewImageIndex" + imageIndex + "\">"
				+"<img src=\"" + "${_IMAGE_DOMAIN_}" + uploadPath + "\" alt=\"\" style=\"max-height:100%;\"/>"
				+"<a href=\"javascript:mypage.review.imageDelete('" + imageIndex + "');\" class=\"btn_reviewDel\">삭제</a>"
				+"</li>";
				
			if(!common.isEmpty(imageIndex)){
				$('#review_img_preview').append(html);
			}

		});
	});
	</script>
</c:if>
	
<!-- 모바일 상품평 등록 레이어 팝업 -->
<c:if test="${isMobile}"> 
	<!-- ### 상품평 쓰기 레이어 팝업 ### -->
	<div class="pop_wrap layer_reWrite" id="reviewLayer">
		<form name="reviewForm" id="reviewForm">
			<input type="hidden" name="reviewNo" value="${review.reviewNo}"/>
			<input type="hidden" name="orderId" value="${review.orderId}"/>
			<input type="hidden" name="productId" value="${review.productId}"/>
			<input type="hidden" name="saleproductId" value="${review.saleproductId}"/>
			<input type="hidden" name="img1" value="${not empty review.reviewNo ? review.img1 : ''}"/>
			<input type="hidden" name="img2" value="${not empty review.reviewNo ? review.img2 : ''}"/>
			<input type="hidden" name="img3" value="${not empty review.reviewNo ? review.img3 : ''}"/>
			
			<div class="pop_inner">
				<div class="pop_header">
					<h3 class="tit">상품평</h3>
				</div>
				<div class="pop_content">
					<strong class="sub_tit">
					<c:if test="${not empty review.productName}">
						${review.productName}
					</c:if>
					</strong>
					<span class="txt_op">
					<c:if test="${not empty review.saleproductName}">
						${review.saleproductName}
					</c:if>
					</span>

					<div class="star_box">
						<dl>
							<c:forEach items="${rating}" var="rating" varStatus="status">
								<dt>${rating.name }</dt>
								<dd>
									<div>
										<span class="ra1">
											<input type="radio" value="1" id="ra1${rating.ratingId }" name="ratingId_${rating.ratingId }" ${rating.rating eq 1 ?'checked':'' }/>
											<label for="ra1${rating.ratingId }">1점</label>
										</span>
										<span class="ra2">
											<input type="radio" value="2" id="ra2${rating.ratingId }" name="ratingId_${rating.ratingId }" ${rating.rating eq 2 ?'checked':'' }/>
											<label for="ra2${rating.ratingId }">2점</label>
										</span>
										<span class="ra3">
											<input type="radio" value="3" id="ra3${rating.ratingId }" name="ratingId_${rating.ratingId }" ${rating.rating eq 3 ?'checked':'' }/>
											<label for="ra3${rating.ratingId }">3점</label>
										</span>
										<span class="ra4">
											<input type="radio" value="4" id="ra4${rating.ratingId }" name="ratingId_${rating.ratingId }" ${rating.rating eq 4 ?'checked':'' }/>
											<label for="ra4${rating.ratingId }">4점</label>
										</span>
										<span class="ra5">
											<input type="radio" value="5" id="ra5${rating.ratingId }" name="ratingId_${rating.ratingId }" ${rating.rating eq 5 or empty rating.rating?'checked':'' }/>
											<label for="ra5${rating.ratingId }">5점</label>
										</span>
									</div>
								</dd>
							</c:forEach>
							<c:if test="${empty rating}">
								상품의 해당되는 카테고리 별점이 존재하지 않습니다.
							</c:if>
						</dl>
					</div>
		
					<div class="txtarea_box">
						<textarea id="reviewDetail" rows="5" cols="10" name="detail" >${review.detail}</textarea>
						<label for="reviewDetail">구매상품평 -  50자 이상<br />첫번째 상품평 -  100자 이상 작성시 혜택이 지급됩니다.<br /><br />(최소 5자  / 최대 2000자까지 등록 가능)</label>
					</div>
					
					<div class="addFile_box" id="review_img_preview">
						<div class="add">
							<input type="file" value="" name="fileinput"/>
							<a href="#none" class="add_photo">첨부파일</a>
						</div>
						
						<div class="imgBox" id="review_img_preview_mo1">
						</div>
						<div class="imgBox" id="review_img_preview_mo2">
						</div>
						<div class="imgBox" id="review_img_preview_mo3">
						</div>
					</div>
		
					<div class="btn_wrapC btn2ea">
						<a href="javascript:ccs.layer.reviewLayer.cancle();" class="btn_mStyle1 sWhite1">취소</a>
						<c:if test="${not empty review.reviewNo}">
							<a href="javascript:ccs.layer.reviewLayer.save('update');" class="btn_mStyle1 sPurple1">수정</a>
						</c:if>
						<c:if test="${empty review.reviewNo}">
							<a href="javascript:ccs.layer.reviewLayer.save('insert');" class="btn_mStyle1 sPurple1">등록</a>
						</c:if>
					</div>
				</div>
				<button type="button" class="btn_x pc_btn_close">닫기</button>
			</div>
		</form>
	</div>
	<!-- ### //상품평 쓰기 레이어 팝업 ### -->
</c:if>

<!-- PC 상품평 등록 레이어 팝업 -->
<c:if test="${!isMobile}">
	<!-- ### 상품평 작성 팝업 : 2016.08.19 수정 ### -->
	<div class="pop_wrap ly_review" id="reviewLayer">
		<form name="reviewForm" id="reviewForm">
			<input type="hidden" name="reviewNo" value="${review.reviewNo}"/>
			<input type="hidden" name="orderId" value="${review.orderId}"/>
			<input type="hidden" name="productId" value="${review.productId}"/>
			<input type="hidden" name="saleproductId" value="${review.saleproductId}"/>
			
			<input type="hidden" name="img1" value="${not empty review.reviewNo ? review.img1 : ''}"/>
			<input type="hidden" name="img2" value="${not empty review.reviewNo ? review.img2 : ''}"/>
			<input type="hidden" name="img3" value="${not empty review.reviewNo ? review.img3 : ''}"/>
			
			<div class="pop_inner">
				<div class="pop_header type1">
					<h3 class="tit">상품평</h3>
				</div>
				<div class="pop_content">
					<ul class="notice">
						<li>일반상품평 혜택 : 매일포인트 100점 + 당근 500개 지급</li>
						<li>포토상품평 혜택 : 매일포인트 100점 + 당근 1,000개</li>
						<li>첫번째 상품평  혜택 :  매일포인트 1,000원 (단, 상품평 글자수 100자 이상만 적립</li>
					</ul>
		
					<div class="tbl_box">
						<table summary="상품평 쓰기 - 상품명, 별점, 제목, 내용, 첨부파일">
							<caption>상품평 쓰기</caption>
							<colgroup>
								<col style="width:20%;">
								<col style="width:80%;">
							</colgroup>
							<tr class="valignT">
								<th scope="row">상품명</th>
								<td>
									<c:if test="${not empty review.productName}">
										${review.productName}
									</c:if>
									<p class="txt_gray">
										<c:if test="${not empty review.saleproductName}">
											${review.saleproductName}
										</c:if>
									</p>
								</td>
							</tr>
		
							<tr>
								<th scope="row">별점</th>
								<td>
									<div class="rating_list">
										<c:forEach items="${rating}" var="rating" varStatus="status">
											<dl>
												<dt>${rating.name }</dt>
												<dd>
													<div class="star_box">
														<span class="ra1">
															<input type="radio" value="1" id="ra1${rating.ratingId }" name="ratingId_${rating.ratingId }" ${rating.rating eq 1 ?'checked':'' }/>
															<label for="ra1${rating.ratingId }">1점</label>
														</span>
														<span class="ra2">
															<input type="radio" value="2" id="ra2${rating.ratingId }" name="ratingId_${rating.ratingId }" ${rating.rating eq 2 ?'checked':'' }/>
															<label for="ra2${rating.ratingId }">2점</label>
														</span>
														<span class="ra3">
															<input type="radio" value="3" id="ra3${rating.ratingId }" name="ratingId_${rating.ratingId }" ${rating.rating eq 3 ?'checked':'' }/>
															<label for="ra3${rating.ratingId }">3점</label>
														</span>
														<span class="ra4">
															<input type="radio" value="4" id="ra4${rating.ratingId }" name="ratingId_${rating.ratingId }" ${rating.rating eq 4 ?'checked':'' }/>
															<label for="ra4${rating.ratingId }">4점</label>
														</span>
														<span class="ra5">
															<input type="radio" value="5" id="ra5${rating.ratingId }" name="ratingId_${rating.ratingId }" ${rating.rating eq 5 or empty rating.rating?'checked':'' }/>
															<label for="ra5${rating.ratingId }">5점</label>
														</span>
													</div>
												</dd>
											</dl>
										</c:forEach>
										<c:if test="${empty rating}">
											상품의 해당되는 카테고리 별점이 존재하지 않습니다.
										</c:if>
									</div>
								</td>
							</tr>
		
							<tr class="valignT">
								<th scope="row">내용</th>
								<td>
									<div class="txtarea_box">
										<textarea id="reviewDetail" rows="5" cols="10" name="detail" >${review.detail}</textarea>
										<label for="reviewDetail">구매상품평 -  50자 이상<br />첫번째 상품평 -  100자 이상 작성시 혜택이 지급됩니다.<br /><br />(최소 5자  / 최대 2000자까지 등록 가능)</label>
									</div>
								</td>
							</tr>
			
							<tr class="valignT">
								<th scope="row">첨부파일</th>
								<td>
									<div class="inputFile_style2">
										<div>
											<input type="text" name="imgName" value="이미지명" class="file_url" />
											<input type="file" name="image" id="imageFile" value="" />
										</div>
										<button type="button" class="btn_file" id="imageButton">찾아보기</button>
									</div>
		
									<ul class="review_img" id="review_img_preview">
										<!-- <li>
											<img src="img/pc/temp/ly_review_img.jpg" alt="" />
											<a href="#none" class="btn_reviewDel">삭제</a>
										</li>
										<li>
											<img src="img/pc/temp/ly_review_img.jpg" alt="" />
											<a href="#none" class="btn_reviewDel">삭제</a>
										</li>
										<li>
											<img src="img/pc/temp/ly_review_img.jpg" alt="" />
											<a href="#none" class="btn_reviewDel">삭제</a>
										</li> -->
									</ul>
								</td>
							</tr>
						</table>
					</div>
		
					<ul class="notice">
						<li>
							포토 상품평 작성시 블로그나 카페에서 글과 이미지를 복사에서 붙일 경우 상품평에 제대로 등록되지 않습니다.
						</li>
						<li>
							게재하신 상품평의 저작권은 제로투세븐에 귀속되며, 상품과 관련 없는 내용은 임의 삭제될 수 있습니다
						</li>
					</ul>
		
					<div class="btn_box">
						<a href="javascript:ccs.layer.reviewLayer.cancle();" class="btn_style mid white pc_btn_close"><span>취소</span></a>
						<c:if test="${not empty review.reviewNo}">
							<a href="javascript:ccs.layer.reviewLayer.save('update');" class="btn_style mid purple"><span>수정</span></a>
						</c:if>
						<c:if test="${empty review.reviewNo}">
							<a href="javascript:ccs.layer.reviewLayer.save('insert');" class="btn_style mid purple"><span>등록</span></a>
						</c:if>
					</div>
				</div>
				<button type="button" class="btn_x pc_btn_close">닫기</button>
			</div>
		</form>
	</div>
	<!-- ### //상품평 작성 팝업 : 2016.08.19 수정 ### -->
</c:if>