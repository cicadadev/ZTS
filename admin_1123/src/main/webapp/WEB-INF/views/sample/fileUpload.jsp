<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<script type="text/javascript" src="/resources/js/dms/fileDirective.js"></script>
<script type="text/javascript" src="/resources/js/dms/fileService.js"></script>
<script type="text/javascript" src="/resources/js/dms/fileController.js"></script>
<article class="con_box">
	<div class="wrap_popup" ng-controller="FileController">
		<h1 class="sub_title1">파일 업/다운로드  ( 사이즈 대/중/소 변환 )</h1>
		<br/>
		
		<div class="box_type1">
			<input type="file" file-model="myImgFile"/>
		</div>

		<div class="btn_alignC" style="margin-top:20px;">
			<button type="button" class="btn_type3 btn_type3_white" ng-click="uploadImgFile()">
				<b>이미지 업로드</b>
			</button>
<!-- 			<button type="button" class="btn_type3 btn_type3_purple"> -->
<!-- 				<b>이미지 다운로드</b> -->
<!-- 			</button> -->
		</div>
		
		</hr>
		<br/>
		<h4> 가능 확장자(jpg, gif, png, jpeg, bmp, txt, doc, ppt, xls, docx, pptx, xlsx, zip)  </h4>
		<div class="box_type1">
			<input type="file" file-model="myFile"/>
		</div>
		<div class="btn_alignC" style="margin-top:20px;">
			<button type="button" class="btn_type3 btn_type3_white" ng-click="uploadFile()">
				<b>파일 업로드</b>
			</button>
<!-- 			<button type="button" class="btn_type3 btn_type3_purple"> -->
<!-- 				<b>파일 다운로드</b> -->
<!-- 			</button> -->
		</div>
		
	</div>
</article>