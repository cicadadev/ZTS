<%--
	화면명 : SNS공유 레이어
	작성자 : roy
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<!-- ### sns 레이어 : 2016.08.22 수정 ### -->
<div class="layer_style1 sLayer_sns">
	<div class="box">
		<div class="conArt">
			<strong class="title">SNS공유</strong>

			<div class="conBox">
				<ul>
					<li class="kakaoStory">
						<a href="#none" onclick="ccs.sns.share('kakaoStory')">카카오스토리</a>
					</li>
					<li class="kakao">
						<a href="#none" onclick="ccs.sns.share('kakaoLink')">카카오톡</a>
					</li>
					<li class="twitter">
						<a href="#none" onclick="ccs.sns.share('twitter')">트위터</a>
					</li>
					<li class="face">
						<a href="#none" onclick="ccs.sns.share('facebook')">페이스북</a>
					</li>
					<li class="sms">
						<a href="#none" onclick="ccs.sns.share('sms')">SMS문자</a>
					</li>
					<li class="nblog">
						<a href="#none" onclick="ccs.sns.share('blog')">blog</a>
					</li>
					<li class="url">
						<a href="#none" onclick="ccs.sns.share('link')">URL복사</a>
					</li>
				</ul>
			</div>
		</div>
		<button type="button" class="btn_close">레이어팝업 닫기</button>
	</div>
</div>
<!-- ### //sns 레이어 : 2016.08.22 수정 ### -->
