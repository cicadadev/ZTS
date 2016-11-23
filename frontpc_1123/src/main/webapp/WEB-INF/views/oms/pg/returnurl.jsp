<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="intune.gsf.common.utils.Config" %>
<script type="text/javascript" src="/resources/js/jquery-1.12.0.min.js"></script>
<%
// request.setCharacterEncoding("UTF-8");

String LGD_RESPCODE         = request.getParameter("LGD_RESPCODE");
String LGD_RESPMSG          = request.getParameter("LGD_RESPMSG");       	                        
String LGD_NOINT	        = request.getParameter("LGD_NOINT");
String LGD_MID              = request.getParameter("LGD_MID");
String VBV_CAVV             = request.getParameter("VBV_CAVV");
String LGD_EXPMON           = request.getParameter("LGD_EXPMON");
String KVP_GOODNAME         = request.getParameter("KVP_GOODNAME");
String LGD_DELIVERYINFO     = request.getParameter("LGD_DELIVERYINFO");
String LGD_BUYER            = request.getParameter("LGD_BUYER");
String LGD_CARDTYPE         = request.getParameter("LGD_CARDTYPE");
String KVP_CARDCOMPANY      = request.getParameter("KVP_CARDCOMPANY");
String LGD_BUYERID          = request.getParameter("LGD_BUYERID");
String LGD_OID              = request.getParameter("LGD_OID");
String LGD_PAN              = request.getParameter("LGD_PAN");
String KVP_SESSIONKEY       = request.getParameter("KVP_SESSIONKEY");
String LGD_EXPYEAR          = request.getParameter("LGD_EXPYEAR");
String LGD_RECEIVERPHONE    = request.getParameter("LGD_RECEIVERPHONE");
String KVP_QUOTA            = request.getParameter("KVP_QUOTA");
String LGD_CLOSEDATE        = request.getParameter("LGD_CLOSEDATE");
String LGD_TIMESTAMP        = request.getParameter("LGD_TIMESTAMP");
String KVP_NOINT            = request.getParameter("KVP_NOINT");
String LGD_BUYERPHONE       = request.getParameter("LGD_BUYERPHONE");
String LGD_INSTALL          = request.getParameter("LGD_INSTALL");
String LGD_ESCROWYN         = request.getParameter("LGD_ESCROWYN");
String LGD_RETURNURL        = request.getParameter("LGD_RETURNURL");
String KVP_PRICE            = request.getParameter("KVP_PRICE");
String LGD_PAYTYPE          = request.getParameter("LGD_PAYTYPE");
String LGD_AMOUNT           = request.getParameter("LGD_AMOUNT");
String KVP_CONAME           = request.getParameter("KVP_CONAME");
String LGD_BUYERSSN         = request.getParameter("LGD_BUYERSSN");
String LGD_RES_CARDPOINTYN  = request.getParameter("LGD_RES_CARDPOINTYN");
String LGD_CURRENCY         = request.getParameter("LGD_CURRENCY");
String KVP_CARDCODE         = request.getParameter("KVP_CARDCODE");
String LGD_PRODUCTINFO      = request.getParameter("LGD_PRODUCTINFO");
String VBV_JOINCODE         = request.getParameter("VBV_JOINCODE");
String LGD_PRODUCTCODE      = request.getParameter("LGD_PRODUCTCODE");
String VBV_XID              = request.getParameter("VBV_XID");
String LGD_HASHDATA         = request.getParameter("LGD_HASHDATA");
String VBV_ECI              = request.getParameter("VBV_ECI");
String LGD_BUYERADDRESS     = request.getParameter("LGD_BUYERADDRESS");
String LGD_BUYERIP          = request.getParameter("LGD_BUYERIP");
String LGD_RECEIVER         = request.getParameter("LGD_RECEIVER");
String KVP_ENCDATA          = request.getParameter("KVP_ENCDATA");
String KVP_PGID             = request.getParameter("KVP_PGID");
String LGD_BUYEREMAIL       = request.getParameter("LGD_BUYEREMAIL");
String LGD_AUTHTYPE         = request.getParameter("LGD_AUTHTYPE");
String KVP_CURRENCY         = request.getParameter("KVP_CURRENCY");
String LGD_KVPISP_USER		= request.getParameter("LGD_KVPISP_USER");
String LGD_PAYKEY		= request.getParameter("LGD_PAYKEY");

String LGD_BILLKEY 		= request.getParameter("LGD_BILLKEY");		//추후 빌링시 카드번호 대신 입력할 값입니다.
String LGD_PAYDATE 		= request.getParameter("LGD_PAYDATE");		//인증일시
String LGD_FINANCECODE 	= request.getParameter("LGD_FINANCECODE");	//인증기관코드
String LGD_FINANCENAME 	= request.getParameter("LGD_FINANCENAME");	//인증기관이름 

%>
<jsp:include page="/WEB-INF/views/gsf/layout/common/commonVariable.jsp" />
<script type="text/javascript" src="/resources/js/oms/oms.common.js"></script>
<html>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<head>
	<script type="text/javascript">
	
		function setLGDResult(isMobile) {						
			try {
// 				alert(isMobile);
				if(isMobile == "true"){
					var data = $("#LGD_RETURNINFO");
					var result = data.find("#LGD_RESPCODE").val();
					
					if(result == "0000"){
						$.ajax({ 				
							url : oms.url.saveMobile,
							type : "POST",		
							data : data.serialize()
						}).done(function(response){
// 							console.log(response);
							if(response.RESULT == "SUCCESS"){								
								$("#orderCompleteForm").find("#orderId").val(response.orderId);								
								$("#orderCompleteForm").submit();	
							}else{
								alert(response.MESSAGE);
							}
							
						});
						
					}else{
						alert(data.find("#LGD_RESPMSG").val());						
					}
				}else{
					parent.payment_return();
				}
				
			} catch (e) {				
// 				alert(e.message);
				alert("결제가 취소되었습니다.");
				closeIframe();
// 				var element = document.getElementById(popupName); // TODO - alternative
// 				element.parentNode.removeChild(element);				
				oms.showOrderLoading(false);
			}
		}
		
	</script>
</head>
<body onload="setLGDResult('${isMobile}')">
<!-- <p><h1>RETURN_URL (인증결과)</h1></p> -->
<!-- <div> -->
<%-- <p>LGD_RESPCODE (결과코드) : <%= LGD_RESPCODE %></p> --%>
<%-- <p>LGD_RESPMSG (결과메시지): <%= LGD_RESPMSG %></p> --%>
	<form method="post" name="LGD_RETURNINFO" id="LGD_RETURNINFO" style="display: none;">		
		LGD_RESPCODE:<input type="text" name="LGD_RESPCODE" id="LGD_RESPCODE" value='<%= LGD_RESPCODE %>' /><br/>
		LGD_RESPMSG:<input type="text" name="LGD_RESPMSG" id="LGD_RESPMSG" value='<%= LGD_RESPMSG %>' /><br/>		
		<!-- billing -->
		LGD_BILLKEY:<input type="text" name="LGD_BILLKEY" id="LGD_BILLKEY" value='<%= LGD_BILLKEY %>' /><br/>
		<input type='text' name='LGD_PAYDATE' id='LGD_PAYDATE' value='<%= LGD_PAYDATE %>'/><br/>
		<input type='text' name='LGD_FINANCECODE' id='LGD_FINANCECODE' value='<%= LGD_FINANCECODE %>'/><br/>
		<input type='text' name='LGD_FINANCENAME' id='LGD_FINANCENAME' value='<%= LGD_FINANCENAME %>'/><br/>
		<!-- 일반결제 -->
		LGD_PAYKEY:<input type="text" name="LGD_PAYKEY" id="LGD_PAYKEY" value='<%= LGD_PAYKEY %>' /><br/>
		
		LGD_NOINT:<input type='text' name='LGD_NOINT' id='LGD_NOINT'value='<%= LGD_NOINT %>'/><br/>
		LGD_MID:<input type='text' name='LGD_MID' id='LGD_MID' value='<%= LGD_MID %>'/><br/>
		<input type='text' name='LGD_BUYERADDRESS' id='LGD_BUYERADDRESS' value='<%= LGD_BUYERADDRESS %>'/><br/>
		<input type='text' name='LGD_BUYERIP' id='LGD_BUYERIP' value='<%= LGD_BUYERIP %>'/><br/>
		<input type='text' name='LGD_RECEIVER' id='LGD_RECEIVER' value='<%= LGD_RECEIVER %>'/><br/>
		LGD_EXPMON:<input type='text' name='LGD_EXPMON' id='LGD_EXPMON' value='<%= LGD_EXPMON %>'/><br/>
		<input type='text' name='LGD_DELIVERYINFO' id='LGD_DELIVERYINFO' value='<%= LGD_DELIVERYINFO %>'/><br/>
		<input type='text' name='LGD_BUYER' id='LGD_BUYER' value='<%= LGD_BUYER %>'/><br/>
		<input type='text' name='LGD_CARDTYPE' id='LGD_CARDTYPE' value='<%= LGD_CARDTYPE %>'/><br/>
		<input type='text' name='LGD_BUYERID' id='LGD_BUYERID' value='<%= LGD_BUYERID %>'/><br/>
		LGD_OID:<input type='text' name='LGD_OID' id='LGD_OID' value='<%= LGD_OID %>'/><br/>
		LGD_PAN:<input type='text' name='LGD_PAN' id='LGD_PAN' value='<%= LGD_PAN %>'/><br/>
		LGD_EXPYEAR:<input type='text' name='LGD_EXPYEAR' id='LGD_EXPYEAR' value='<%= LGD_EXPYEAR %>'/><br/>
		<input type='text' name='LGD_RECEIVERPHONE' id='LGD_RECEIVERPHONE' value='<%= LGD_RECEIVERPHONE %>'/><br/>
		<input type='text' name='LGD_CLOSEDATE' id='LGD_CLOSEDATE' value='<%= LGD_CLOSEDATE %>'/><br/>
		<input type='text' name='LGD_TIMESTAMP' id='LGD_TIMESTAMP' value='<%= LGD_TIMESTAMP %>'/><br/>
		<input type='text' name='LGD_BUYERPHONE' id='LGD_BUYERPHONE' value='<%= LGD_BUYERPHONE %>'/><br/>
		LGD_INSTALL:<input type='text' name='LGD_INSTALL' id='LGD_INSTALL' value='<%= LGD_INSTALL %>'/><br/>
		<input type='text' name='LGD_ESCROWYN' id='LGD_ESCROWYN' value='<%= LGD_ESCROWYN %>'/><br/>
		<input type='text' name='LGD_RETURNURL' id='LGD_RETURNURL' value='<%= LGD_RETURNURL %>'/><br/>
		<input type='text' name='LGD_PAYTYPE' id='LGD_PAYTYPE' value='<%= LGD_PAYTYPE %>'/><br/>
		
		<input type='text' name='LGD_AMOUNT' id='LGD_AMOUNT' value='<%= LGD_AMOUNT %>'/><br/>
		<input type='text' name='LGD_BUYERSSN' id='LGD_BUYERSSN' value='<%= LGD_BUYERSSN %>'/><br/>
		<input type='text' name='LGD_RES_CARDPOINTYN' id='LGD_RES_CARDPOINTYN' value='<%= LGD_RES_CARDPOINTYN %>'/><br/>
		<input type='text' name='LGD_CURRENCY' id='LGD_CURRENCY' value='<%= LGD_CURRENCY %>'/><br/>
		<input type='text' name='LGD_PRODUCTINFO' id='LGD_PRODUCTINFO' value='<%= LGD_PRODUCTINFO %>'/><br/>
		<input type='text' name='LGD_PRODUCTCODE' id='LGD_PRODUCTCODE' value='<%= LGD_PRODUCTCODE %>'/><br/>
		<input type='text' name='LGD_BUYEREMAIL' id='LGD_BUYEREMAIL' value='<%= LGD_BUYEREMAIL %>'/><br/>
		LGD_AUTHTYPE:<input type='text' name='LGD_AUTHTYPE' id='LGD_AUTHTYPE' value='<%= LGD_AUTHTYPE %>'/><br/>
		LGD_HASHDATA:<input type='text' name='LGD_HASHDATA' id='LGD_HASHDATA' value='<%= LGD_HASHDATA %>'/><br/>
		KVP_QUOTA:<input type='text' name='KVP_QUOTA' id='KVP_QUOTA' value='<%= KVP_QUOTA %>'/><br/>
		KVP_NOINT:<input type='text' name='KVP_NOINT' id='KVP_NOINT' value='<%= KVP_NOINT %>'/><br/>
		<input type='text' name='KVP_PRICE' id='KVP_PRICE' value='<%= KVP_PRICE %>'/><br/>
		<input type='text' name='KVP_CONAME' id='KVP_CONAME' value='<%= KVP_CONAME %>'/><br/>
		KVP_CARDCODE:<input type='text' name='KVP_CARDCODE' id='KVP_CARDCODE' value='<%= KVP_CARDCODE %>'/><br/>
		KVP_SESSIONKEY:<input type='text' name='KVP_SESSIONKEY' id='KVP_SESSIONKEY' value='<%= KVP_SESSIONKEY %>'/><br/>
		<input type='text' name='KVP_CARDCOMPANY' id='KVP_CARDCOMPANY' value='<%= KVP_CARDCOMPANY %>'/><br/>
		KVP_ENCDATA:<input type='text' name='KVP_ENCDATA' id='KVP_ENCDATA' value='<%= KVP_ENCDATA %>'/><br/>
		<input type='text' name='KVP_PGID' id='KVP_PGID' value='<%= KVP_PGID %>'/><br/>
		<input type='text' name='KVP_CURRENCY' id='KVP_CURRENCY' value='<%= KVP_CURRENCY %>'/><br/>
		<input type='text' name='KVP_GOODNAME' id='KVP_GOODNAME' value='<%= KVP_GOODNAME %>'/><br/>
		VBV_CAVV:<input type='text' name='VBV_CAVV' id='VBV_CAVV' value='<%= VBV_CAVV %>'/><br/>
		VBV_ECI:<input type='text' name='VBV_ECI' id='VBV_ECI' value='<%= VBV_ECI %>'/><br/>
		VBV_JOINCODE:<input type='text' name='VBV_JOINCODE' id='VBV_JOINCODE' value='<%= VBV_JOINCODE %>'/><br/>
		VBV_XID:<input type='text' name='VBV_XID' id='VBV_XID' value='<%= VBV_XID %>'/><br/>
		LGD_KVPISP_USER:<input type='text' name='LGD_KVPISP_USER' id='LGD_KVPISP_USER' value='<%= LGD_KVPISP_USER  %>'/><br/>
	</form>
	<form action="/oms/order/complete" name="orderCompleteForm" id="orderCompleteForm" method="post" style="display: none;">
		<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }"/>
		<input type="hidden" name="orderId" id="orderId" value=""/>
	</form>
</body>
</html>