<%--
	화면명 : 마이페이지 > 마이 메뉴
	작성자 : ian
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<script type="text/javascript">
	$(document).ready(function() {
		mypage.myMenu.clickMenuEvent();
	});
</script>

<style>
.checked {
    border: none;
    background-image: url("../img/mobile/bg/m_checkbox.gif");
    background-size: 22px 22px;
}

</style>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY정보관리|마이메뉴" name="pageNavi"/>
</jsp:include>
<div class="inner">
	<div class="quick_setup">
		<p class="txt_guide">
			원하는 메뉴를 선택해 주세요.<br />최대 5개 까지 선택하여 순서를 정하실 수 있습니다. <!-- 16.11.08 : 수정 -->
		</p>

		<!-- 16.11.08 -->
		<div class="menu_sort_box">
			<ul class="menu_sort" id="selectMenu_ul">
				<!-- 메뉴 업을 경우 -->
				<c:if test="${empty memberMenuList}">
					<li class="noData_tp2" >
						선택한 메뉴가 없습니다.
					</li>
				</c:if>
				<!-- //메뉴 없을 경우 -->
				
				<c:if test="${not empty memberMenuList}">
					<c:forEach items="${memberMenuList}" var="menu">
						<li>
							<input type="checkbox" value="" title="선택" name="select_${menu.menuId}" id="select_${menu.menuId}" class="inp_chk" checked="checked" onclick="mypage.myMenu.deleteMymenu(this)";/>
							<input type="hidden" name="select_url_${menu.menuId}" value="${menu.url}"/>
							<label for="select_${menu.menuId}">${menu.name}</label>
							<button type="button" class="btn_move">이동</button>
						</li>
					</c:forEach>
				</c:if>
			</ul>
		</div>
		<!-- //16.11.08 -->
		
		<div id="menu_ul_List">
			<h3>마이쇼핑</h3>
			<ul class="menu_sort">
				<c:forEach items="${menuList}" var="menu">
					<c:set var="check" value="fasle" />
					<c:forEach items="${memberMenuList}" var="memberMenu">
						<c:if test="${menu.menuId eq memberMenu.menuId }">
							<c:set var="check" value="true" />
						</c:if>
					</c:forEach>
					<c:choose>
						<c:when test="${check}">
							<li>
								<input type="checkbox" value="" title="선택" name="${menu.menuId}" id="${menu.menuId}" class="inp_chk .checked" checked="checked" />
								<input type="hidden" name="url_${menu.menuId}" value="${menu.url}"/>
								<label for="${menu.menuId}">${menu.name}</label>
							</li>
						</c:when>
						<c:otherwise>
							<li>
								<input type="checkbox" value="" title="선택" name="${menu.menuId}" id="${menu.menuId}" class="inp_chk"/>
								<input type="hidden" name="url_${menu.menuId}" value="${menu.url}"/>
								<label for="${menu.menuId}">${menu.name}</label>
							</li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</ul>
	
			<h3>전문관</h3>
			<ul class="menu_sort">
				<c:forEach items="${dealList}" var="deal">
					<c:set var="check" value="fasle" />
					<c:forEach items="${memberMenuList}" var="memberMenu">
						<c:if test="${deal.menuId eq memberMenu.menuId }">
							<c:set var="check" value="true" />
						</c:if>
					</c:forEach>
					<c:choose>
						<c:when test="${check}">
							<li>
								<input type="checkbox" value="" title="선택" name="${deal.menuId}" id="${deal.menuId}" class="inp_chk .checked" checked="checked" />
								<input type="hidden" name="url_${deal.menuId}" value="${deal.url}"/>
								<label for="${deal.menuId}">${deal.name}</label>
							</li>
						</c:when>
						<c:otherwise>
							<li>
								<input type="checkbox" value="" title="선택" name="${deal.menuId}" id="${deal.menuId}" class="inp_chk"/>
								<input type="hidden" name="url_${deal.menuId}" value="${deal.url}"/>
								<label for="${deal.menuId}">${deal.name}</label>
							</li>
						</c:otherwise>
					</c:choose>
				</c:forEach>
			</ul>
		</div>

		<div class="btn_boxC">
			<a href="javascript:mypage.myMenu.save();" class="btn_style11 purple1">
				<em>저장하기</em>
			</a>
		</div>
	</div>
</div>
