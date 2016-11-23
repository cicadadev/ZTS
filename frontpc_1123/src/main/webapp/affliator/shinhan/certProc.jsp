<%@page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<script type="text/javascript" src="/resources/js/jquery-1.12.0.min.js"></script>

<%
	String cert_code = request.getParameter("cert_code");
	String cert_no = request.getParameter("cert_no");
	String auth_type = request.getParameter("auth_type");
	
	//System.out.println("cert_code:"+cert_code+",cert_no:"+cert_no+",auth_type:"+auth_type);
	
	request.setAttribute("cert_code", cert_code);
	request.setAttribute("cert_no", cert_no);
	request.setAttribute("auth_type", auth_type);
	
	
%>
<script>

	 //cert_code : 
	 //00(대상카드)
	 //01(대상카드 아님)
	 //02(승인가능카드가 아님)
	 //03(카드번호오류(해당카드 존재하지 않음))
	 //97(카드번호입력오류)
	 //98(시스템오류 (내부오류))
	 //99(시스템오류 (내부오류))
	 
	var cert_code = '${cert_code}';
	var cert_no = '${cert_no}';		//회원번호
	var auth_type = '${auth_type}';	//카드타입(01(고운맘 카드)) 02(KisPlus 카드)
	
	if("00" != cert_code){
		alert("카드번호가 올바르지 않습니다.");
		//window.close();
	}
	else{
		var param = { authType : auth_type};
		
		$.ajax({
			method : "get",
			url : "/api/sps/event/shinhan/save",
		}).done(function(result) {
			
			if(result=='S'){
				alert("등록이 완료되었습니다.");
				window.close();
			}
			
		});
	}
	
</script>