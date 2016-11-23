<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>	
<c:if test="${not empty items}">
	<div class="banner" style="background:${items.addValue}">
		<div class="htmlCorner">
			${items.html1}"
		</div>
		
		<div class="btn_close_box">
			<button type="button" class="btn_close">배너창 닫기</button>
		</div>
	</div>
</c:if>