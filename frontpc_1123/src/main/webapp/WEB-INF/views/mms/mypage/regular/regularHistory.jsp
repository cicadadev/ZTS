<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY주문관리|정기배송 관리" name="pageNavi"/>
</jsp:include>
<div class="inner">
<div class="layout_type1">
<div class="column">
	<div class="myorder myrepeat">
		<h3 class="title_type1">정기배송 관리</h3>
		<div>
			<form method="post" name="regularSearch">
				<ul class="tabBox tp1">
					<li onclick="$order.onTab($(this));" data-tab="request" data-menu="regular">
						<a href="javascript:void(0);">정기배송 신청내역</a>
					</li>
					<li onclick="$order.onTab($(this));" data-tab="canceled" data-menu="regular">
						<a href="javascript:void(0);">정기배송 종료/해지내역</a>
					</li>
				</ul>
			</form>
			<%-- contents layer --%>
			<span id="innerTab"></span>
<!-- 			<div class="tab_con tab_conOn"></div> -->
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
</div>
</div>
<script type="text/javascript">
<!--
	// 최초 로딩시만 실행.
	$(function() {
		$('.myorder').find('.tabBox li:eq(${tabIdx})').trigger("click");
		$('.lnb ul:eq(0) li:eq(2)').addClass('on');
	});
//-->	
</script>