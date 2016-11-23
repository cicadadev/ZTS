<%--
	화면명 : 주문상품 검색 리스트
	작성자 : 로이
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>

<dl>
	<c:forEach items="${list}" var="gift">
		<dt>${gift.name }</dt>
		<dd>
			<ul>
				<c:forEach items="${gift.spsEventjoins}" var="winner">
					<li><span>${winner.memName}</span></li>
				</c:forEach>
			</ul>
		</dd>
	</c:forEach>
</dl>


		