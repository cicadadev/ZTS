<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="gcp.mms.model.custom.FoLoginInfo" %>
<%@ page import="gcp.common.util.FoSessionUtil" %>
<%@ page import="gcp.frontpc.common.contants.Constants"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>


<div class="footer_mo">
	<!-- 모바일웹에서만 노출 -->
	<c:if test="${isApp eq 'true'}">
		<a href="#none" class="app_down">
			적립금 받는 제로투세븐 앱 다운받기
		</a>
	</c:if>	
		<ul class="policy">
			<li>
				<a href="/ccs/cs/main">고객센터</a>
			</li>
			<li>
				<a href="/ccs/common/service">이용약관</a>
			</li>
			<li>
				<a href="/ccs/common/privacy">개인정보 취급방침</a>
			</li>
			<li>
				<c:choose>
					<c:when test="${loginId != '' && loginId != null}">
						<a href="#none" onclick="ccs.link.logout()">로그아웃</a>
					</c:when>
					<c:otherwise>
						<a href="#none" onclick="ccs.link.login()">로그인</a>
					</c:otherwise>
				</c:choose>
			</li>
		</ul>	
		<div class="inner">
			<div class="address">
				<em>(주) 제로투세븐</em>
				<a href="#none">사업자확인</a>
		
				<p>
					(03926) 서울시 마포구 상암산로76 (상암동, YTN뉴스퀘어 17층/ 18층)<br />
					대표이사 김정민, 조성철  /  TEL 1588-8744<br />
					사업자 등록번호 220-81-76684  /  통신판매업신고번호 제 01-50
				</p>
		
				<small>
					Copyright ⓒ zero to seven. All right Reserved.
				</small>
			</div>
		
			<div class="family">
				<div class="select_box1">
					<label>Family Site</label>
					<select onchange="ccs.go_url(this.options[this.selectedIndex].value);">
						<option selected="selected">Family Site</option>
						<option value="http://www.maeil.com">매일유업</option>
						<option value="http://www.maeili.com">매일아이</option>
						<option value="http://www.sanghafarm.co.kr">상하농원</option>
						<option value="http://absolute.maeili.com/absolute/index.jsp">앱솔루트태교</option>
						<option value="">맘스마일</option>
						<option value="">맘마밀</option>
						<option value="http://direct.maeil.com">가정배달</option>
						<option value="http://www.sanghacheese.co.kr">상하치즈</option>
						<option value="http://sanghafarm.maeil.com">상하목장</option>
					</select>
				</div>
		
				<div class="sns">
					<a href="https://m.facebook.com/zerotoseven07" target="_blank" class="btn_face">페이스북</a>
					<a href="https://story.kakao.com/ch/zerotoseven" target="_blank" class="btn_kakao">카카오스토리</a>
					<a href="http://zerotoseven.tistory.com" target="_blank" class="btn_tstory">티스토리</a>
				</div>
			</div>
		</div>
</div>		