<html>
<head>
<jsp:include page="/WEB-INF/views/gsf/commonScript.jsp" flush="true"/>
<script>
__pageId = '${pageId}';
angular.element(document).ready(function(){
	if (__pageId != "") {
		$("#"+__pageId, parent.document).height($(".wrap").height());
	}
});
</script>
</head>
<body>
<div class="wrap wrap_iframe" >
<section class="content">
<div class="inner">
