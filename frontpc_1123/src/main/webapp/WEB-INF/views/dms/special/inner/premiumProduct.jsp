<%--
	화면명 : 멤버쉽관 > 인증 후
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>

<!-- <script type="text/javascript" src="/resources/js/jquery.countdown.min.js"></script> -->
<script type="text/javascript">
$(document).ready(function(){

});

$('input[name="totalCount"]').val(Number('${totalCount}'));

</script>

<c:choose>
	<c:when test="${isMobile }">
		<ul class="productSize${status.index }">
			<c:forEach var="product" items="${productList}" varStatus="i">
				<c:set var="product" value="${product}" scope="request" />
				<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
					<jsp:param name="type" value="premium" />
					<jsp:param name="certify" value="true" />
					<jsp:param name="dealProductIndex" value="${i.index }" />
				</jsp:include>
			</c:forEach>
		</ul>
	</c:when>
			
	<c:otherwise>
		<c:choose>
			<c:when test="${not empty search.upperDealGroupNo }">
				<c:forEach var="depth1" items="${depthList }" varStatus="status">
						<c:if test="${search.upperDealGroupNo eq depth1.dealGroupNo }">
						<c:forEach var="depth2" items="${depth1.spsDealgroups }" varStatus="status2">
							<a name="anchor_${depth2.dealGroupNo }" />
							<div class="list_group" >
								<h4 id="title1" class="tit_style1 pc_only">
									<span class="depthName">${depth2.name }</span>
									<a href="#" class="btn_top">top</a> <!-- 16.10.17 : top 추가 -->
								</h4>
								<div class="product_type1 prodType_4ea timeType">
									<ul class="productSize${status.index }">
										<c:forEach var="product" items="${productList}" varStatus="i">
											<c:if test="${product.dealGroupNo eq depth2.dealGroupNo}">
											<c:set var="product" value="${product}" scope="request" />
											<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
												<jsp:param name="type" value="premium" />
												<jsp:param name="certify" value="true" />
												<jsp:param name="dealProductIndex" value="${i.index }" />
											</jsp:include>
											</c:if>		<%-- // 해당 depth2 상품 출력 --%>
										</c:forEach>
									</ul>
								</div>
							</div>
						</c:forEach>	<%-- // depth2 --%>
						</c:if>			<%-- // 해당 1depth 추출 --%>
				</c:forEach>	<%-- // depth1  --%>
			</c:when>
			
			<c:otherwise>
				<div class="list_group">
					<h4 id="title1" class="tit_style1 pc_only">
						<span class="depthName"></span>
					</h4>
					<div class="product_type1 prodType_4ea timeType">
						<ul class="productSize${status.index }">
							<c:forEach var="product" items="${productList}" varStatus="i">
								<c:set var="product" value="${product}" scope="request" />
								<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
									<jsp:param name="type" value="premium" />
									<jsp:param name="certify" value="true" />
									<jsp:param name="dealProductIndex" value="${i.index }" />
								</jsp:include>
							</c:forEach>
						</ul>
					</div>
				</div>
			</c:otherwise>
		</c:choose>
	</c:otherwise>
</c:choose>