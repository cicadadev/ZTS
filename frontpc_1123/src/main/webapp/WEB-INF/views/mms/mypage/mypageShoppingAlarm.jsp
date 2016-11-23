<%--
	화면명 : 마이페이지 > 메인
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script type="text/javascript">
$(document).ready(function(){
	
});
</script>

	<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
		<jsp:param value="쇼핑알림보관함" name="pageNavi"/>
	</jsp:include>
	
<div class="inner">
	<div class="layout_type1">
		<div class="column">
			<div class="mypage mynotice">
				<h3 class="title_type1">쇼핑알림 보관함</h3>
				<div class="shopping_notice">
					<p>수신된 알림메시지는 <em>7</em>일간 보관 후 자동 삭제됩니다.</p>
					<c:if test="${isApp}">
						<a href="app://setting">알림설정</a>
					</c:if>
				</div>
				<div class="historyBox">
					<c:choose>
						<c:when test="${!empty alarmList }">
							<ul class="rw_tb_tbody3">
							<c:forEach items="${alarmList }" var="list" varStatus="status">
								<li>
									<div class="tr_box">
										<div class="col1 timeline">
											<i>timeline</i>
										</div>
										<div class="col2">
											<span class="date">${list.regDate }</span>
											<div class="tit_pkg">
												<a href="javascript:void(0);">
													${list.pushTitle }
													<c:if test="${list.isNew eq 'Y' }">
														<span class="icon_new">NEW</span>
													</c:if>
												</a>
												<button type="button" class="btn_detail">내용보기</button>
											</div>
											<div class="detail_cont">${list.pushMsg }</div>
										</div>
									</div>
								</li>
							</c:forEach>
							</ul>
						</c:when>
						<c:otherwise>
							<p class="empty">알림 메세지 내역이 없습니다.</p>
						</c:otherwise>
					</c:choose>
				</div>
			</div>
		</div>
	</div>
</div>
