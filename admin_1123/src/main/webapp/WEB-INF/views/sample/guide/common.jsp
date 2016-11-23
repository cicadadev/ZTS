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

	*java 유틸
	1. CommonUtil.java : StringUtils.java를 extends하여 주로 문자열 관련 기능을 제공.	
	2. MessageUtil.java : properties 로 부터 메세지 조회하는 기능.	
	3. SesstionUtil.java : 세션 관련 기능.
	4. DateUtil.java : 날짜 관련 기능.
	
	*js 유틸 : 추후 제공 예정..
	
	*상수 관리
	/intune_gsf/src/main/java/intune/gsf/common/constants/BaseConstants.java : 공통 상수
	/gcp2.0_admin/src/main/java/gcp/admin/common/constants/Constants.java : 채널별 상수 BaseConstants.java를 상속
	
	*config 값 관리
	/gcp2.0_admin/src/main/resources/config/system/properties.xml : 채널별 config값 관리. 
	
	
	*js, *jsp에서 코드명 조회 
	 - Base model에 코드값 조회 대상(db 컬럼 post fix 가 '_CD' 인 것)에 대해서 getter 자동 생성
	
		예 ) BasePmsProduct.java
		
		private String productStateCd;
		
		public String getProductStateName(){
			return CodeUtil.getCodeName("PRODUCT_STATE_CD", getProductStateCd());
		}
		
		productStateCd 일 경우 -> 화면에서 poductStateName 사용
</xmp>
</p>
</div>

</body>
</html>

