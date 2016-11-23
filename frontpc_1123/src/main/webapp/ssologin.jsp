<%-- <%@page import="sso.SSOVO"%>
<%@page import="sso.SSOUtil"%> --%>
<%@page import="jwork.cipher.client.JworkCrypto"%>
<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="jwork.sso.agent.SSOManager"%>
<!-- 
    [ssologin]
    
	이 페이지는 findCookie 를 호출하여 1회용 접속 키를 얻어온 이후
	1회용 접속 키를 이용하여 통합회원 정보를 얻어오는 프로세스를 수행합니다.
    index 페이지에서 설명드린 것처럼 Ajax를 사용하는 경우는 컨트롤러에서 수행하셔도 됩니다.
    
    getSSOMemberInfoByKey API를 사용하여 사용자 정보를 얻어온 이후 해당 사용자의 정보를 이용하여
    자사 로그인 프로세스를 진행합니다. 
 -->

<%
	//index 페이지에서 받은 1회용 접속 키
	String j_sso_q = request.getParameter("j_sso_q");
	String responseMessage =  SSOManager.getSSOMemberInfoByKey(j_sso_q);
	if( SSOManager.isSuccess(responseMessage)){
		//통합회원 서비스 번호
		String USER_NO = SSOManager.getResponseData(responseMessage);
		//System.out.println("@@ USER_NO : "+USER_NO);
		//-----------------------------------------------------------------------------
		// 이부분에서 통합회원 서비스 번호를 가지고 자사 DB에서 기타 회원정보를 
		// 조회하여 로그인 프로세스를 진행합니다.
		// 만일 서비스 이용 약관동의 회원이 아닌 경우는 약관동의 프로세스를 
		// 진행하시기 바랍니다.
		// 자사 DB가 없을시에는 생략
		//-----------------------------------------------------------------------------
		//SSOVO vo  = SSOUtil.getMeberInfoByUserNo(USER_NO);
		//String userId = "ssotest"; // 서비스 번호를 이용 자사 DB에서 회원정보 조회
		//String userId = vo.getUserID();
		
		String userId = USER_NO;
		
		if(userId == null){
			%>
			<script> 
			alert("해당하는 user ID가 존재하지 않습니다.");
			self.location.href="loginForm.jsp";	
			</script>
			<%
		}else{
			//자사 로그인 프로세스 진행
			session.setAttribute("userId", userId);
			session.setAttribute("USER_NO", USER_NO);
			%>
			<script> 
			alert("SSO 쿠키를 이용하여 사용자 정보를 조회하고 로그인하였습니다. index 페이지로 이동합니다.");
			self.location.href="index.jsp";	
			</script>
			<%
		
		}
		
	}else{ //getSSOMemberInfoByKey API가 정상적으로 동작하지 않은 경우
//		System.out.println("[ getSSOMemberInfoByKey fail ]");
//		System.out.println("code    : "+SSOManager.getResponseCode(responseMessage));
//		System.out.println("message : "+SSOManager.getResponseMessage(responseMessage));
		%>
		<script>
		alert("getSSOMemberInfoByKey API가 실패하였습니다. loginForm 페이지로 이동합니다.");
		self.location.href="loginForm.jsp";	
		</script>
		<%
		
	}
%>
ssologin
