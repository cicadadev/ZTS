<%--
	화면명 : 마이페이지 > MY 활동관리 > 스타일관리
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<script type="text/javascript">
$(document).ready(function() {

	mypage.style.getMemberStyleList();
});
</script>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY활동관리|스타일 관리" name="pageNavi"/>
</jsp:include>
<div class="inner">
<div class="layout_type1">
<div class="column">
	<div class="mypage style_shop">
		<h3 class="title_type1">스타일 관리</h3>
		
		<c:if test="${isApp}">
			<div class="btn_wrapC mo_only">
				<a href="javascript:mypage.style.makeAndModifyStyle('', '${loginId}');" class="btn_sStyle3 sPurple1 btn_makeStyle"><span>스타일 만들기</span></a>
			</div>
		</c:if>
		<div class="filterSorting">
			<ul class="sortBox">
				<li class="active">
					<a href="#none">
						<span>전체</span><em>(${styleTotalCnt})</em>
					</a>
				</li>
			</ul>
			<ul class="select_boxWrap">
				<li class="box_01">
					<div class="select_box1">
						<label></label>
						<select onChange="mypage.style.changeSort(this);">
							<option value="popular">인기순</option>
							<option value="new">최신순</option>
						</select>
					</div>
				</li>
			</ul>
		</div>
		
		<div id="styleList">
		
		
		
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
</div>
</div>