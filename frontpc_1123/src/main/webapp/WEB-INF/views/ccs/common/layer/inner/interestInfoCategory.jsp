<%--
	화면명 : 메인&멤버십관 > 관심정보 레이어
	작성자 : stella
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript" src="/resources/js/common/common.ui.js"></script>
<script type="text/javascript">
$(document).ready(function() {

});
</script>

<div class="pop_set_cnt_wrap">
	<p class="title">
		<c:if test="${isMobile ne 'true'}">
			<img src="/resources/img/pc/txt/txt_info_set_03.png" class="pc_only" alt="관심상품이 무엇인가요?">
		</c:if>
		<c:if test="${isMobile eq 'true'}">
			<img src="/resources/img/mobile/txt/txt_info_set_03.png" class="mo_only" style="width:100%;" alt="관심상품이 무엇인가요?">
		</c:if>
	</p>
	<dl class="pop_set_survey">
		<dt>방문동기</dt>
		<dd name="multiChk" id="purposeArea">
			<label class="chk_style1 option_style1">
				<em>
					<input type="checkbox" value="PREPARE" />
				</em>
				<span>출산준비</span>
			</label>
			<label class="chk_style1 option_style1 lh16">
				<em>
					<input type="checkbox" value="PURCHASE" />
				</em>
				<span>우리아이 <br class="mo_only"> 상품구매</span>
			</label>
			<label class="chk_style1 option_style1 lh16">
				<em>
					<input type="checkbox" value="GIFT" />
				</em>
				<span>지인 <br class="mo_only"> 선물구매</span>
			</label>
		</dd>
		<dt>관심 카테고리</dt>
		<dd class="odd_type" name="interestCategory" style="${isMobile eq 'true' ? 'min-width:247px' : ''};">
			<c:forEach items="${categoryList}" var="category1" varStatus="status">
				<label class="chk_style1 option_style1">
					<em id="category1_em${status.index}" name="category1_em" onchange="ccs.layer.interestInfoLayer.chooseCategory(${status.index});">
						<input type="checkbox" name="ra1_3"/>
					</em>
					
					<c:choose>
						<c:when test="${fn:indexOf(category1.name, '/') > 0}">
							<c:set var="tempName1" value="${fn:split(category1.name,'/')[0]}" />
							<c:set var="tempName2" value="${fn:split(category1.name,'/')[1]}" />
							
							<c:choose>
								<c:when test="${isMobile eq 'true'}">
									<c:set var="spanStyle" value="line-height:20px" />
								</c:when>
								<c:otherwise>
									<c:set var="spanStyle" value="line-height:25px" />
								</c:otherwise>
							</c:choose>
									
							<c:choose>
								<c:when test="${fn:length(fn:split(category1.name,'/')) == 3}">
									<span style="${spanStyle}">${tempName1}${tempName2}<br>${fn:split(category1.name,'/')[2]}</span>					
								</c:when>
								<c:otherwise>
									<span style="${spanStyle}">${fn:replace(category1.name,'/', '<br>')}</span>
								</c:otherwise>
							</c:choose>
							
						</c:when>
						<c:otherwise>
							<span style="${spanStyle}">${category1.name}</span>									
						</c:otherwise>
					</c:choose>

				</label>
			</c:forEach>
			<div class="set_sort_outer" style="display:none;">
				<ul class="set_sort_list" id="category2_ul">
					<c:forEach items="${categoryList}" var="category1" varStatus="status">
						<div id="category2_div${status.index}" style="display:none">
							<c:forEach items="${category1.dmsDisplaycategorys}" var="category2" varStatus="category2Status">
								<li>
									<label class="chk_style1">
										<em name="category2_em">
											<input type="checkbox" value="${category2.displayCategoryId}" />
										</em>
										<span>${category2.name}</span>
									</label>
								</li>
							</c:forEach>
						</div>
					</c:forEach>
				</ul>
			</div>
		</dd>
	</dl>
</div>
<div class="btn_wrapC btn2ea">
	<a href="javascript:ccs.layer.interestInfoLayer.prev(3);" class="btn_prev"><span>이전</span></a><a href="javascript:ccs.layer.interestInfoLayer.next(3);" class="btn_next"><span>다음</span></a>
</div>
<div class="btn_wrap_close">
	<a href="javascript:ccs.layer.close('interestInfoLayer');" class="btn_pop_close">닫기</a>
</div>