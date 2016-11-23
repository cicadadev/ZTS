<%--
	화면명 : 마이페이지 > MY 활동관리 > 쇼핑찜
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript">
$(document).ready(function() {
// 	$("#list_empty").hide();	
// 	$("[name=TOTAL_CNT]").val("${totalCount}");
	
	mypage.wishlist.search();
});
</script>

<form name="wishlistForm">

</form>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY활동관리|쇼핑찜" name="pageNavi"/>
</jsp:include>
<div class="inner">
<div class="layout_type1">
<div class="column">
	<div class="mypage mywish">
		<h3 class="title_type1">쇼핑찜</h3>

		<div class="sortPkg">
			<ul class="sortBox">
				<li class="active">
					<a href="#none">
						<span>전체</span><em id="totalCnt"></em>
					</a>
				</li>
			</ul>
			<a href="#none" class="btn_sStyle1 sWhite2 posR" onclick="mypage.wishlist.deleteAll();">전체삭제</a>
		</div>

		<div class="tbl_article">
			
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
</div>
</div>