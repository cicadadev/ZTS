<%--
	화면명 : 메인&멤버십관 > 관심정보 레이어 오픈 링크(APP용)
	작성자 : stella
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="/WEB-INF/views/gsf/layout/common/commonScript.jsp" />
<script type="text/javascript">

	mms.common.isLogin(function(result) {
		if (result == 1) {
			ccs.link.go("/ccs/common/main", CONST.NO_SSL);
		} else {
			ccs.link.login({returnUrl:global.config.domainUrl + "/ccs/common/interestInfo"});
		}
	});

</script>