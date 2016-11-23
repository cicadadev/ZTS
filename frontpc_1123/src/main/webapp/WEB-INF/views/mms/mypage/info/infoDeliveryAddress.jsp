<%--
	화면명 : 마이페이지 > MY 정보관리 > 배송지관리
	작성자 : ALLEN
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<script type="text/javascript" src="/resources/js/mms/mms.common.js" ></script>
<script type="text/javascript">

$(document).ready(function(){
	mypage.deliveryAddress.getAddressList();
});

	
</script>
<form name="deliveryAddrForm">
	<input type="hidden" name="TOTAL_CNT" />
</form>
<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY정보관리|배송지관리" name="pageNavi"/>
</jsp:include>
<div class="inner">
<div class="layout_type1">
<div class="column">
	<div class="mypage myaddr">
		<h3 class="title_type1">배송지관리</h3>
			<div class="managed">
				<c:if test="${not empty basicAddress}">
					<strong>${basicAddress.name}</strong>
					<span class="default icon_type2 iconPink3">기본 배송지</span>
					<ul>
						<li>
							<span>연락처 :</span>
								<span id="phone2_span"><script>ccs.common.phone_format("phone2_span", "${basicAddress.phone2}")</script></span> /
								<span id="phone1_span"><script>ccs.common.phone_format("phone1_span", "${basicAddress.phone1}")</script></span>
						</li>
						<li>
							<span>주소 :</span>(${basicAddress.zipCd}) ${basicAddress.address1} ${basicAddress.address2}
						</li>
					</ul>
				</c:if>
				<c:if test="${empty basicAddress}">
					등록된 기본 배송지 정보가 없습니다.
				</c:if>
			</div>
		
		<div class="tbl_article">
			<div class="div_tb_thead2">
				<div class="tr_box">
					<span class="col1"></span>
					<span class="col2">이름</span>
					<span class="col3">주소</span>
					<span class="col4">연락처</span>
					<span class="col6">관리</span>
				</div>
			</div>
			<div id="address">
				
			</div>
		</div>
	
		<div class="btn_wrapC btn2ea">
			<a href="javascript:mypage.deliveryAddress.addNewDeliveryAddr();" class="btn_mStyle1 sWhite1">새 배송지 추가</a>
			<a href="javascript:mypage.deliveryAddress.setBasicDeliveryAddr()" class="btn_mStyle1 sPurple1">기본 배송지로 설정</a>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
</div>
</div>
