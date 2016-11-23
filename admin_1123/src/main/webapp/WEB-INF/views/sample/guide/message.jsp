<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<script type="text/javascript" src="/resources/js/jquery-1.12.0.min.js"></script>
<script type="text/javascript" src="/resources/js/ui.js"></script>
<title>Admin</title>
<link rel="stylesheet" type="text/css" href="/resources/js/angular-1.5.4/ui-grid.min.css">
<link rel="stylesheet" type="text/css" href="/resources/css/common.css" />
</head>
<body>
<div>
<p>
<xmp>

	1. 메세지 프로퍼티 파일
		- message.properties : 일반 메세지 관리
		- field_message.properties : DB 필드 항목명 관리
		- custom_field_message.properties : 사용자 정의 항목명
		- error.properties : 에레 메세지 관리
		
		- message.properties => default
		- message_ko.properties => 언어별			
		
	2. 다국어 지원
		- url 뒤에 locale 파라메터로 언어 변경 
		- ex ) /samplePage/guide?locale=ko
		
	3. java에서 메세지 조회
		- /intune_gsf/src/main/java/intune/gsf/common/utils/MessageUtil.java
		 ex ) MessageUtil.getMessage("pms.product.reg.error1");
		 
	4. jsp에서 메세지 조회 : 스프링 spring:message tag사용
		jsp 상단에 < %@ taglib uri="http://www.springframework.org/tags" prefix="spring"%> 선언
			    
		<spring:message code="test.apple" var="apple" />
		<spring:message code="test.orange" var="orange" />

		<spring:message code="test.test1" arguments="$ {apple},$ {orange}" />
		
	5. js에서 메세지 사용
		- 화면별 controller.js 상단에 js에서 사용할 메세지 키 정의
		Constants.message_keys = ["SPS.COUPON.REG.INFO", "SPS.COUPON.REG.INFO2"];
					
		- 메세지 목록 조회하여 scope에 담아둔다.			
		commonService.getMessages(function(response){
			$scope.MESSAGES = response;
		});		
		
		==> 추후 수정하여 공지 예정..
					
	6. /gcp2.0_admin/src/main/webapp/WEB-INF/messages/field/field_message.properties
	
		-> DB 필드값을 기준으로 auto generate 하여 생성됨.
		-> 사용할 필드값에 메세지 정의하여 사용
		-> 정의한 문구와 기획 문구가 다른경우 /gcp2.0_admin/src/main/webapp/WEB-INF/messages/message.properties
		   에 재정의 하여 사용. 
		   
	7. 메세지 key naming rule
		- message.properties
			예 ) pms.product.list.title
		- error.properties
			예 ) pms.product.reg.error1
</xmp>
</p>
</div>

</body>
</html>

