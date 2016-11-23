<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@page import="jwork.sso.agent.SSOHealthChecker"%>
<%@page import="jwork.sso.agent.SSOGlobals"%>
<%@page import="jwork.sso.agent.SSOManager"%>
<%@page import="gcp.common.util.FoSessionUtil"%>
<%@page import="gcp.frontpc.common.util.FrontUtil"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="intune.gsf.common.utils.Config" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!--
	[index 페이지]
	
	이 페이지는 서비스 사이트의 index페이지에 해당합니다.
	또한 사용자가 SSO 로그인 되어 있는경우 자동 로그인하는
	페이지로 동작합니다. (findCookie.jsp 호출)  
-->
<%
	String sessionUserId = FoSessionUtil.getLoginId();
	pageContext.setAttribute("type", request.getAttribute("type"));
	pageContext.setAttribute("returnUrl", request.getAttribute("url"));
	pageContext.setAttribute("loginSso", request.getAttribute("loginSso"));
	
	String deviceType = FoSessionUtil.getDeviceTypeCd(request);
	pageContext.setAttribute("deviceType", deviceType );
	
	System.out.println("@@@sessionUserId : " + sessionUserId == null);
	System.out.println("@@@SSOHealthChecker.isAlived() : " + SSOHealthChecker.isAlived());

	//사용자 세션이 없고 SSO 서버가 살아있으면 
	if (sessionUserId == null && SSOHealthChecker.isAlived()) {
		String j_sso_q = SSOManager.encryptForCookieValidation(request, SSOGlobals.SST_CD);
%>

	
<script type="text/javascript">

	if('${deviceType}' == 'DEVICE_TYPE_CD.PC'){
		device = 'PW';
	}else if('${deviceType}' == 'DEVICE_TYPE_CD.APP'){
		device = 'MI';
	}else if ('${deviceType}' == 'DEVICE_TYPE_CD.MW'){
		device = 'MW';
	}

	window._rblqueue = window._rblqueue || [];

	function recobellUser() {
	   (function(s,x){s=document.createElement('script');s.type='text/javascript'
	   s.async=true;s.defer=true;s.src=(('https:'==document.location.protocol)?'https':'http')+
	   '://assets.recobell.io/rblc/js/rblc-apne1.min.js'
	   x=document.getElementsByTagName('script')[0];x.parentNode.insertBefore(s, x);})();
	}
 
</script>


		<script>
		
		//data : 1회용 접속 코드 
		function _jssoCompleted(data, code){
			
			if(data !=""){
				
				//alert("SSO 쿠키가 존재하여 확인하러 갑니다.");
				
				var param = { j_sso_q : data};


				
				
				// SSO 로그인 처리
				$.ajax({
					  method: "post",
					  url: "/api/mms/login/sso/ajax",
					  contentType:"application/json; charset=UTF-8",
					  data: data
					}).done(function( data ) {
						// 이페이지가 팝업인지, 바닥페이지인지에 따라 분기
						var parent = window;
						var isPopup = false;
						
						if(device == 'PW'){// pc일때
							if(parent.opener){
								isPopup = true;
								parent = parent.opener;
							}
						}else{
							
						}
						
						var closePopup = function(){
							// 로그인 창을 통해 호출된 경우임.
			 				if(isPopup && '${type}' != "ORDER"){
								window.close();
							} 
						}
						
						
						if(data!=null && data!='null' && data!='' && data.loginId){
							
						
							function redirect(parent, url){
								
								// mms.LOGINCALLBACKFN 이 존재할 경우 화면을 리로드 하지 않고 콜백스크립트 함수를 수행한다.	(PC전용)
								if(parent && parent.mms && parent.mms.LOGINCALLBACKFN){
									
									// 상단 로그인 영역 refresh
									$.get("/mms/login/refresh").done(function(html){
										
										parent.$('#loginBtnBox').html(html);
										parent.mms.LOGINCALLBACKFN(true);
										parent.mms.LOGINCALLBACKFN = null;
										
										closePopup();
										
									});
											
		
								}
								else if(url!=''){
									parent.location.href=url;
									closePopup();
								}else{
									parent.location.reload();
									closePopup();
								}
								
								
							}
							
							if('${type}' == "ORDER"){
								location.href = "/oms/order/login/return";
							}else{
								
								var myAge = Number(data.myAge);
								if(myAge==-1){
									myAge = '';
								}
								
								_rblqueue.push(['setVar','cuid', '<%=Config.getString("recobell.cuid")%>' ]);
								_rblqueue.push(['setVar','device',device]);
								_rblqueue.push(['setVar','userId', data.memberNo]);
								_rblqueue.push(['setVar','gender', data.recobellGenderCd]);
								_rblqueue.push(['setVar','age', myAge]);
								_rblqueue.push(['setVar','babyGender', data.recobellBabyGenderCd ]);
								_rblqueue.push(['setVar','babyAgeInMonths', data.recobellBabyMonthCd ]);
								_rblqueue.push(['track','user']);  
								
								//console.log(_rblqueue);
								
							    //레코벨 회원정보 전송
								recobellUser();
							    //리다이렉트
								redirect(parent, '${returnUrl}');
							}

						}else{
							//sso 세션 존재하지만 0to7계정 없음
							
							if( '${ callbackFlag }'!=''){
								alert("로그인 정보가 존재하지 않습니다.[!]");
								history.go(-1);
							}
						}
						

						
							
					});
			}else{//end if(data !=""){
				// 쿠키 없음
				return;
			}
			
		}
		</script>
		<c:if test="${ not empty callbackFlag }">
		<script type="text/javascript" src="/resources/js/jquery-1.12.0.min.js"></script>
		<script type="text/javascript" src="/resources/js/gsf.common.js"></script>
		</c:if>
		<script type="text/javascript" src="<%=SSOGlobals.FIND_URL%>?j_sso_q=<%=URLEncoder.encode(j_sso_q)%>"></script>
<%
	}
%>
