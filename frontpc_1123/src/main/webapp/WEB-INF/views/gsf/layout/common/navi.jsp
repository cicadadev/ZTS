<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"  uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript">
$(function(){
	if('${isMobile}'=='true'){
		
		$('.btn_navi_prev').click(function(){
// 			window.location.href='${befUrl}';
			parent.history.back();
		});	
			
	}
});
</script>

<c:set var="name1" value="${fn:split(param.pageNavi,'|')[0]}" />
<c:set var="name2" value="${fn:split(param.pageNavi,'|')[1]}" />
<c:set var="name3" value="${fn:split(param.pageNavi,'|')[2]}" />
				
<c:choose>
	<c:when test="${isMobile ne 'true'}">
		<!-- pc 전용 네비 -->
		<div class="location_box">
			<div class="location_inner">
				<ul>
					<li id="home">홈</li>
					<c:choose>
						<c:when test="${name1 eq '전문관'}">
							<li>
								<a href="${param.url}"><c:out value="${name1}"/></a>
							</li>

							<c:if test="${not empty name2}">
								<li>
									<div class="select_box1">
										<label><c:out value="${name2}"/></label>
										<select onchange="ccs.link.special.url(this.options[this.selectedIndex].value);">
											<option ${name2 == '프리미엄멤버십관' ? 'selected="selected"' : '' } value="/dms/special/premium" >프리미엄 멤버십관</option>
											<option ${name2 == '정기배송관' ? 'selected="selected"' : '' } value="/dms/special/subscription">정기배송관</option>
											<option ${name2 == '매장픽업관' ? 'selected="selected"' : '' } value="/dms/special/pickup">매장픽업관</option>
											<option ${name2 == '기프트샵' ? 'selected="selected"' : '' } value="/dms/special/giftShop">기프트샵</option>
											<option ${name2 == '분유관' ? 'selected="selected"' : '' } value="/dms/special/milkPowder">분유관</option>
											<option ${name2 == '출산준비관' ? 'selected="selected"' : '' } value="/dms/special/birthready">출산준비관</option>
											<option ${name2 == '다자녀우대관' ? 'selected="selected"' : '' } value="/dms/special/multiChildrenInfo">다자녀우대관</option>
											<c:if test="${not empty loginId}">
												<option ${name2 == '임직원관' ? 'selected="selected"' : '' } value="/dms/special/employee">임직원관</option>
											</c:if>
										</select>
									</div>
								</li>
							</c:if>
						</c:when>
						<c:when test="${name1 eq '마이쇼핑'}">
							<li>
								<c:out value="${name1}"/>
							</li>
							<c:if test="${not empty name2}">
								<li>
									<c:out value="${name2}"/>
								</li>
							</c:if>
							<c:if test="${not empty name3}">
								<li>
									<c:out value="${name3}"/>
								</li>
							</c:if>
						</c:when>
						<c:otherwise>
							<li>
								<a href="${param.url}"><c:out value="${name1}"/></a>
							</li>
							<c:if test="${not empty name2}">
								<li>
									<c:out value="${name2}"/>
								</li>
							</c:if>
						</c:otherwise>
					</c:choose>
				</ul>
			</div>
		</div>
	</c:when>
	<c:otherwise>
	
		<!-- mobile 전용 네비 -->
		<div class="mo_navi">
			<c:if test="${param.type ne 'brandSearch'}">
				<button type="button" class="btn_navi_prev">이전 페이지로..</button>
			</c:if>
			<c:choose>
				<c:when test="${param.type eq 'category'}">
					<!-- 전시카테고리 네비게이션 -->
					<h2>
						<a href="#none" ${param.depth == 3? 'class="rw_btnMCate"': 'class="rw_btnMCate active"'}><c:out value="${param.name}"/></a>
					</h2>
				</c:when>
				<c:when test="${param.type eq 'productDetail'}"><!-- 상품상세 -->
					<h2>
						<a href="#none" class="btn_location">${param.pageNavi}</a>
						<a href="javaScript:common.pageMove('cart','','')" class="btn_cart_top">장바구니</a> <!-- 16.09.28 : 장바구니 버튼 추가 -->
					</h2>
					<!-- 상품상세에서만 노출 됨 -->
					<div class="layer_location">
						<span>
							<a href="#none">홈</a>
						</span>
						<c:if test="${not empty currentCategory }">
							<c:forEach begin="1" end="${currentCategory.depth-1}" step="1" var="depth">
								<c:set var="name" value="${fn:split(currentCategory.depthFullName,'|')[depth]}" />
								<c:set var="id" value="${fn:split(currentCategory.depthDisplayCategoryId,'|')[depth]}" />
								<c:choose>
									<c:when test="${depth eq 1}">
										<span>
											<a href="#none">${name}</a>
										</span>
									</c:when>
									<c:otherwise>
										<span>
											<a href="javascript:ccs.link.display.dispTemplate(${id})">${name}</a>
										</span>
									</c:otherwise>
								</c:choose>
							</c:forEach>
						</c:if>
					</div>
				</c:when>				
				<c:otherwise>
					<!-- 일반 페이지 네비게이션 -->
					<c:choose>
						<c:when test="${not empty name3}">
							<h2><c:out value="${name3}"/></h2>
						</c:when>
						<c:when test="${not empty name2}">
							<h2><c:out value="${name2}"/></h2>
						</c:when>
						<c:otherwise>
							<h2><c:out value="${name1}"/></h2>
						</c:otherwise>
					</c:choose>
				</c:otherwise>
			</c:choose>
		</div>	
	</c:otherwise>
</c:choose>