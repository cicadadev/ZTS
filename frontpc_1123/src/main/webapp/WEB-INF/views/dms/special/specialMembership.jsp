<%--
	화면명 : 프론트 & 모바일 멤버십관
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<script type="text/javascript">
$(document).ready(function(){
	special.membership.init();
});
</script>
<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="전문관|프리미엄멤버십관" name="pageNavi"/>
</jsp:include>
<div class="inner">
	<%-- 인증전 : rw_certifyMemBox // 인증후 : rw_certifyMemBox after --%>
	<div class="rw_certifyMemBox before">
	
	</div>
	<!-- //rw_certifyMemBox -->
</div>
