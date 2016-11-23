<%--
	화면명 : 마이페이지 > MY 활동관리 > 최근 본 상품
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<script type="text/javascript">
$(document).ready(function() {
	mypage.latestProduct.search();
});
</script>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY활동관리|최근 본 상품" name="pageNavi"/>
</jsp:include>
<div class="inner">
<div class="layout_type1">
<div class="column">
	<div class="mypage mywish recent"> <!-- 16.09.21 recent 추가 -->
		<h3 class="title_type1">최근 본 상품</h3>

		<div class="sortPkg">
			<ul class="sortBox">
				<li class="active">
					<a href="#none">
						<span>전체</span><em id="totalCnt"></em>
					</a>
				</li>
			</ul>
			<a href="#none" class="btn_sStyle1 sWhite2 posR" onclick="javascript:mypage.latestProduct.deleteLatestProduct();">전체삭제</a>
		</div>

		<div class="tbl_article">
			
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
</div>
</div>