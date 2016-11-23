<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="jwork.sso.agent.SSOHealthChecker"%>
<%@page import="jwork.sso.agent.SSOManager"%>
<%@page import="jwork.sso.agent.SSOGlobals"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="intune.gsf.common.utils.SessionUtil" %>
<!--
	통합 로그아웃 하는 페이지 입니다.
-->

<%
	//session.invalidate();
	SessionUtil.removeLoginInfo(request);
	
	if(SSOHealthChecker.isAlived()) {
		String j_sso_q = SSOManager.encryptForCookieRemove(request , SSOGlobals.SST_CD);
		%>
		<script>
		function _jssoCompleted(data, code){
			//alert(" SSO 로그아웃을 완료하였습니다.");
			self.location.href="index.jsp";
		}
		</script>
		<script type="text/javascript" src="<%=SSOGlobals.REMOVE_URL%>?j_sso_q=<%=URLEncoder.encode(j_sso_q)%>"></script>
		<%			
		
	}else{
		%>
		<script>
		//alert(" SSO 서버가 죽어있으므로 자사 세션만 로그아웃 시키고 loginForm 페이지로 이동합니다.  ");
		self.location.href="index.jsp"; 
		</script>
		<%		
	}

%>

<script>
window.onload = function pageOnload(){
	//페이지를 전부 로딩한 시점에 SSO 서버장애로 인하여 removeCookie를 받지 못한는 경우입니다. 
	if( (typeof _VjssoLoadCheck) == "undefined"){
		//alert("SSO 서버가 죽어 있으므로 세션만 로그아웃 시키고 loginForm 페이지로 이동합니다.");
		self.location.href="index.jsp";
	}
}
</script>
