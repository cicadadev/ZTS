<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tiles" uri="http://tiles.apache.org/tags-tiles"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ page session="true"%>
<%@ page import="gcp.common.util.FoSessionUtil" %>
<%@ page import="gcp.mms.model.custom.FoLoginInfo"%>
<%@ page import="gcp.frontpc.common.contants.Constants"%>
<%

if(FoSessionUtil.isMemberLogin()){
	FoLoginInfo loginInfo = (FoLoginInfo)FoSessionUtil.getLoginInfo(); 
	pageContext.setAttribute("memberMenus", loginInfo.getMemberMenus() );	
}	

%>
<!-- ### mobile 하단 고정 메뉴 : 2016.08.17 추가 ### -->
<div class="bottom_menu">
<ul>
	<li>
		<a href="/mms/mypage/order/history" class="btn_bot1">
			주문배송
		</a>
	</li>
	<li>
		<a href="javaScript:ccs.link.mypage.main()" class="btn_bot2">
			마이쇼핑
		</a>
	</li>
	<li>
		<a href="javaScript:ccs.link.common.main()" class="btn_bot3">
			홈
		</a>
	</li>
	<li>
		<a href="javaScript:common.pageMove('cart','','')" class="btn_bot4">
			장바구니 <em>0</em>
		</a>
	</li>
	<li>
		<a href="#none" class="btn_bot5 btn_setup">
			더보기
		</a>

		<ul class="setup_menu">
			<li>
				<a href="#none" onClick="javaScript:ccs.link.mypage.info.mymenu()">
					<c:choose>
						<c:when test="${fn:length(memberMenus) < 5}">
							<b>+메뉴추가</b>
						</c:when>
						<c:otherwise>
							<b>+메뉴변경</b>							
						</c:otherwise>
					</c:choose>
				</a>
			</li>
			<c:if test="${not empty memberMenus}">
				<c:forEach items="${memberMenus}" var="menu">
					<li>
						<a href="${menu.url}">${menu.name}</a>
					</li>
				</c:forEach>
			</c:if>
		</ul>
	</li>
</ul>
</div>
<!-- ### //mobile 하단 고정 메뉴 : 2016.08.17 추가 ### -->