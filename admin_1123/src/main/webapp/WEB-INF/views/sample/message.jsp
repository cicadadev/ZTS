<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<body>
<br/><br/><br/>

<spring:message code="test.apple" var="apple" />
<spring:message code="test.orange" var="orange" />

Message Test : "<spring:message code="test.test1" arguments="${apple},${orange}" />"<br/>



<br/><br/><br/><button id="change_eng" onclick="change('en')">영어&nbsp;|&nbsp;</button><button id="change_kor" onclick="change('ko')">한국어</button>



</body>
<script>
function change(locale){
	location.href="/samplePage/message?locale="+locale;
}

</script>