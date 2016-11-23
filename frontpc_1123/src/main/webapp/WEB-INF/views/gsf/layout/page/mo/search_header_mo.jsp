<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	

<!-- ### mobile 검색 레이어 : 2016.08.17 수정 ### -->
<div class="layer_mo_search">
	<div class="fnBox">
		<button type="button" class="btn_navi_prev">이전 페이지로..</button>
		<input type="text" value="" class="hd_mo_search" id="header_search" onkeyup ="dmsmb.header.searchKeyUp(this);"/>
		<a href="#none" class="btn_strDel">검색어 지우기</a>
		<input type="submit" class="btn_search" id="submit_btn"/>
	</div>

	<!-- 2016.08.11 : 추가 -->
	<ul class="srchWay">
		<li><a href="app://scan" class="btn_qr"><i>QR/바코드</i>QR/바코드 검색</a></li>
		<li><a href="javascript:ccs.link.go('/ccs/offshop/search', false);" class="btn_store"><i>매장</i>매장찾기</a></li>
	</ul>
	<!-- //2016.08.11 : 추가 -->

	<div class="word">
		<ul class="tabBox tab_style1">
			<li class="on"><a href="#">최근검색어</a></li>
			<li ><a href="#">인기검색어</a></li>
		</ul>

		<div class="tab_con tab_conOn">
			<ul class="last" id="latelySearch">
			</ul>

			<div class="util">
				<ol>
					<!-- <li>
						<a href="#none">자동저장 끄기</a>
					</li> -->
					<li>
						<a href="javascript:dmsmb.header.deleteKeyWord();">검색목록 삭제</a>
					</li>
				</ol>
				<a href="#none" class="btn_close">닫기</a>
			</div>
		</div>

		<div class="tab_con">
			<ol class="rank" id="rankSearch_mb">
				
			</ol>

			<div class="util">
				<a href="#none" class="btn_close">닫기</a>
			</div>
		</div>
	</div>

	<div class="similar" id="autoSearch">
		<ol class="link">
			
		</ol>

		<div class="util">
			<a href="#none" class="btn_close">닫기</a>
		</div>
	</div>
</div>
<!-- ### //mobile 검색 레이어 : 2016.08.17 수정 ### -->