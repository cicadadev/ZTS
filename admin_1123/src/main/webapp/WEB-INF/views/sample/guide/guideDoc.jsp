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

	1. GCP 기본 컨셉
		- AngularJS + Rest API(Ajax) 로 페이지 구현
		- Angular Grid 사용
		- Table 스키마와 동일한 Model 구조
		- 패키지 구조 :
			gcp2.0_admin : 어드민
			gcp2.0_frontmobile : 프론트 Mobile
			gcp2.0_frontpc : 프론트 PC
			gcp2.0_platform : 업무 영역(service, model, sql) 
			intune_gsf : 공통서비스 ( 공통 코드, 유틸, tool.. )
			gcp2.0_batch : 배치
	
	2. 쿼리 
		- 포메팅 : 쿼리 전체 대문자로 토드 포메터 사용
		- 주석 : 쿼리 상단
		- 되도록 테이블 컬럼을 가공하지 않은 형태로 리턴하고 Service Layer에서 비즈니스 로직으로 구현 => 쿼리 단순화 및 재사용 증가
		
		/* [order.getOrderList][dennis][2016. 4. 19.] */	
		SELECT T1.*, T2.NAME CATEGORY_NAME
		  FROM PMS_PRODUCT T1, PMS_CATEGORY T2
		 WHERE T1.CATEGORY_ID = T2.CATEGORY_ID AND T1.STORE_ID = # {storeId}
		   AND ROWNUM < 10
		   	
	3. Model
		- 테이블 스키마와 1:1로 Base모델, 확장모델 존재함 ( Generator 로 자동생성 )
		- Base모델은 수정 불가. 확장모델에 필요한 컬럼 추가하여 사용
		- 필요에 따라 그리드 조회용 모델 별도 작성 ( /grid/XxxxGrid.java ) 
		- 조회용 모델 별도 작성 ( /search/XxxxSearch.java )
		- /intune_gsf/src/main/java/intune/gsf/model/BaseEntity.java :  Base 모델의 부모 클래스
		- /intune_gsf/src/main/java/intune/gsf/model/BaseSearchCondition.java : Search 모델의 부모 클래스
		
	4. JAVA 코딩
		- QueryService : Mybatis 연동하여 쿼리 호출( dao 미존재 )
		- Controller : 페이지 이동을 위한 View 컨트롤러와 Rest API를 위한 Rest 컨트롤러 존재함. QueryService로 쿼리 호출 가능.
		  /intune_gsf/src/main/java/intune/gsf/controller/BaseController.java : Controller 의 부모 클래스
		- Service : 비즈니스 로직 구현, 트랜잭션 처리됨. QueryService로 쿼리호출.
		- 주석 : 이클립스 코드템플릿으로 자동생성됨. ( class코멘트, method 코멘트)
		- 포메팅 : 이클립스 formatter 설정. 
	
	5. JSP, JS 코딩
	 - 기본 작성 Rule
	  jsp : WEB-INF/views/ccs/testList.jsp
	        WEB-INF/views/ccs/popup/testPopup.jsp
	        WEB-INF/views/ccs/layer/testLayer.jsp
	  js : resources/js/ccs/testListController.js => 컨트롤러는 jsp와 1:1 로 생성
	  	   resources/js/ccs/ccsService.js => service는 대 업무별 하나만 생성
	  	   
	 - javascript는 되도록이면 js파일에 구현
	 - 코드명, 코드목록은 커스텀 태그 사용.
	
	10. 기타

	11. 신규 투입 개발 패키지
		- 개발압축파일 ( ZTS_2016{MMDD}.zip )을 C:\ root에 압축해제
		
		
</xmp>
</p>
</div>

</body>
</html>

