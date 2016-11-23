<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<!-- pc 전용 네비 -->
<div class="location_box">
	<div class="location_inner">
		<ul id="categoryInner">
			<!-- 전시카테고리 네비게이션 -->
			<jsp:include page="/WEB-INF/views/dms/include/navi_category_inner.jsp" flush="false"/>
		</ul>
	</div>
</div>
