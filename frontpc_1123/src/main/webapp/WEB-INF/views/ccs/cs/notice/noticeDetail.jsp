<%--
	화면명 : 고객센터 > 공시사항 > 상세
	작성자 : roy
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<script type="text/javascript">
/* 	$(document).ready(function(){
		custcenter.setCsLayoutType("csnotice");
	}); */
</script>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="/ccs/cs/main" name="url"/>
	<jsp:param value="고객센터|공지사항" name="pageNavi"/>
</jsp:include>

<style>

.maxW img {height:100%;width:100%;}

</style>

<div class="inner">
<div class="layout_type1 csnotice">
<div class="column">
	<div class="csBox">
		<h3 class="title_type1">공지사항</h3>
		<div class="listDetailBox">
			<div class="title">
				${notice.title}
				<span class="tit_date"></span>
			</div>
			<div class="listDetail">
				<div class="date">
					<span>
						<strong>등록일</strong><em>${notice.insDt}</em>
					</span>
					<span>
						<strong>조회수</strong><em>${notice.readCnt}</em>
					</span>
				</div>
				<div class="cont maxW"> 
					${notice.detail}
				</div>
			</div>
		</div>
		<div class="btn_wrapC btn1ea">
			<a href="#none" class="btn_mStyle1 sWhite1" onclick="common.pageMove('${pageScope.id}','','/ccs/cs/notice/list')">목록</a>
		</div>
	</div>
</div>
<jsp:include page="/WEB-INF/views/gsf/layout/cs/left_cs.jsp" />
</div>
</div>
