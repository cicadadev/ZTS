<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>		
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	
<%@ page session = "true" %>
<%@ page import="gcp.frontpc.common.contants.Constants" %>
<%@ page import="intune.gsf.common.utils.Config" %>

<c:choose>
	<c:when test="${loginId != '' && loginId != null}">
		<!-- 로그인 후 -->
		<li>
			<span>
				<c:if test="${memGradeCd eq 'MEM_GRADE_CD.VIP'}">
					<img src="/resources/img/pc/ico/lv_vip.png" alt="VIP" />
				</c:if>
				<c:if test="${memGradeCd eq 'MEM_GRADE_CD.GOLD'}">
					<img src="/resources/img/pc/ico/lv_gold.png" alt="GOLD" />
				</c:if>
				<c:if test="${memGradeCd eq 'MEM_GRADE_CD.SILVER'}">
					<img src="/resources/img/pc/ico/lv_silver.png" alt="SILVER" />
				</c:if>
				<c:if test="${memGradeCd eq 'MEM_GRADE_CD.FAMILY'}">
					<img src="/resources/img/pc/ico/lv_family.png" alt="FAMILY" />
				</c:if>
				<c:if test="${memGradeCd eq 'MEM_GRADE_CD.WELCOME'}">
					<img src="/resources/img/pc/ico/lv_welcome.png" alt="WELCOME" />
				</c:if>
				<b>${loginName }(${loginId})</b>님 환영합니다.
			</span>								
			<a href="javascript:void(0);" onclick="ccs.link.logout()">로그아웃</a>
		</li>
		<!-- 로그인 후 비회원 주문조회 비노출-->
		<!-- <li>
			<a href="javascript:void(0);" onclick="mms.common.nonMemberLoginLayer();">비회원 주문조회</a>
		</li> -->					
<!-- 		<li>
			<a href="#none" onclick="mms.common.memberUpdatePopup()">정보수정</a>
		</li> -->
		<!-- 로그인 후 -->
	</c:when>
	<c:otherwise>
		<!-- 로그인 전 -->
		<li>
			<script>
			var loginTemp = function(){

				$.ajax({
					url : "/api/mms/login/temp/ajax",
					type : "POST"		
				}).done(function(response){
					if(response){
						alert("로그인"+response.loginId);
						location.href="/ccs/common/main";
					}else{
						location.href="/ccs/common/main";	
					}
					//alert(msg.response.message);		
				})
			}
			
			</script>
			<a href="javascript:void(0);" onclick="javascript:loginTemp()">임시로그인</a>
			<a href="javascript:void(0);" onclick="ccs.link.login({isReload : '${isReload}'})">로그인</a>
		</li>
		<li>
			<a href="javascript:void(0);" onclick="mms.common.joinPopup()">회원가입</a>
		</li>
		<li>
			<a href="javascript:void(0);" onclick="mms.common.nonMemberLoginLayer()">비회원 주문조회</a>
		</li>						
		<!-- 로그인 전 -->
	</c:otherwise>		
</c:choose>

<li>
	<a href="javascript:ccs.link.custcenter.main();">고객센터</a>
</li>
<li>
	<a href="javascript:ccs.link.order.cart();" class="btn_cart">장바구니 <em></em></a>
</li>
<li class="btn_my">
	<a href="javascript:ccs.link.mypage.main();" >마이쇼핑</a>
	<div class="layer">
		<ul>
			<li>
				<a href="javascript:ccs.link.mypage.order.delivery();" >주문/배송조회</a>
			</li>
			<li>
				<a href="javascript:ccs.link.mypage.benefit.coupon();" >쿠폰</a>
			</li>
			<li>
				<a href="javascript:ccs.link.mypage.activity.review();">상품평</a>
			</li>
<!-- 			<li>
				<a href="javascript:ccs.link.mypage.main();">정보관리</a>
			</li> -->
		</ul>
	</div>
</li>
<script>

$(document).ready(function(){
	dmspc.header.getCartCount();//cart count
});
</script>