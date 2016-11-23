<%--
	화면명 : 마이페이지 > 나의활동 > 히스토리
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<script type="text/javascript">
	mypage.history.search();
</script>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY활동관리|히스토리" name="pageNavi"/>
</jsp:include>
<div class="inner">
<div class="layout_type1">
<div class="column">
	<div class="mypage myhistory">
		<h3 class="title_type1">히스토리</h3>
		<div class="historyBox">
			<div class="posR">	
				<a href="javascript:void(0);" class="btn_sStyle1 sWhite2" onclick="javascript:mypage.history.deleteHistory();">전체삭제</a>
			</div>
			<div class="rw_tb_tbody3">
				
			</div>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
</div>
</div>