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
	1. 달력팝업
		
		- datetime-picker date-only 추가
		- design에 버튼은 삭제
		- 표시값은 '/' 가 들어가있으나 binding된 parameter는 '/' 없음. 
		
		ex)
			<input type="text" data-ng-model="ctrl.search.startDate" value="" placeholder="" datetime-picker date-only/>											
			~
			<input type="text" data-ng-model="ctrl.search.endDate" value="" placeholder="" datetime-picker date-only />
			
			표시 : 2016/05/01
			binding 값 : 20160501
			
	2. 달력 기간 설정 버튼(1주일,15일,30일,60일)
		
		- div class day_group에 date-only calendar-button 추가
		- design에 버튼은 삭제
		- start-ng-model은 시작 날짜, end-ng-model은 종료 날짜 input box에 설정한 ng-model명을 입력한다.
		
		ex)
			<div class="day_group" start-ng-model="ctrl.search.startDate" end-ng-model="ctrl.search.endDate" date-only calendar-button />	
</xmp>
</p>
</div>

</body>
</html>

