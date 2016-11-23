<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<script type="text/javascript" src="/resources/js/mms/mms.mypage.js" ></script>

<script>
$(document).ready(function(){
	//16.10.23 스타일
	$(".mobile .btn_styleHow").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .ly_styleHow") );
		}
	});
	
	
/* 	$(".mobile .option_style1 input").each(function() {
		if($(this).is(':checked')){
			$(this).parent().addClass("selected");
		}
	}); */

	$(".mobile .option_style1 input").off("change").on({
		"change" : function() {
			if( $(this).attr("type") == "checkbox" ){
				if($(this).is(':checked')){
					$(this).parent().addClass("selected");
				}else{
					$(this).parent().removeClass("selected");
				}
				
			}else if( $(this).attr("type") == "radio" ){
				
				var type = $(this).val();
				var this_name = $(this).attr("name");
				
				if(type=='new'){
					$('[name=radio_style_2]').removeClass("selected");
					$('[name=radio_style_1]').addClass("selected");	
				}else{
					$('[name=radio_style_1]').removeClass("selected");
					$('[name=radio_style_2]').addClass("selected");
				}
				
				
			}else{
				
				
			}
		}
	});
	
});

</script>



<div class="style_shop">
	
	<div class="visual style mo_only swiper-container styleSwiper_banner_1">
		<ul class="vImg swiper-wrapper">
			<c:if test="${not empty styleBannerList}">
				<c:forEach var="item" items="${styleBannerList[0].dmsDisplayitems}" varStatus="i">
					<li class="swiper-slide">
						<a href="${item.url2}">
							<img src="${_IMAGE_DOMAIN_}${item.img2}" alt="" />
						</a>
					</li>
				</c:forEach>
			</c:if>
		</ul>
	</div>
	
	
	<c:if test="${isApp && (loginId == null || loginId == '')}">
		<div class="btn_wrapC btn1ea mo_only">
			<a href="#none" class="btn_sStyle3 sWhite1 btn_styleHow">이용안내</a>
		</div>
	</c:if>
	<c:if test="${!isApp}">
		<div class="btn_wrapC btn1ea mo_only">
			<a href="#none" class="btn_sStyle3 sWhite1 btn_styleHow">이용안내</a>
		</div>
	</c:if>
	
	<c:if test="${isApp && loginId != null && loginId != ''}">
		<div class="btn_wrapC btn2ea mo_only">
			<a href="#none" class="btn_sStyle3 sWhite1 btn_styleHow">이용안내</a>
			<a href="javascript:mypage.style.makeAndModifyStyle('', '${loginId}');" class="btn_sStyle3 sPurple1 btn_makeStyle">스타일 만들기</a>
		</div>
	</c:if>
	

	<div class="filterSorting">
		<ul>
			<li class="box_01">
				<div class="select_box1 radiusSel">
					<label>성별</label>
					<tags:codeList code="GENDER_TYPE_CD" var="gender" tagYn="N" />
					<select onchange="javascript:display.style.changeFilter($(this));" id="sel_gender">
						<option value="">성별</option>
						<c:forEach items="${gender}" var="gender">
							<option value="${gender.cd}">${gender.name}</option>
						</c:forEach>
					</select>
				</div>
				<div class="select_box1 radiusSel">
					<label>테마</label>
					<tags:codeList code="THEME_CD" var="theme" tagYn="N"/>
					<select onchange="javascript:display.style.changeFilter($(this));" id="sel_theme">
						<option value="">테마</option>
						<c:forEach items="${theme}" var="theme">
							<c:if test="${theme.cd ne 'THEME_CD.ETC'}">
								<option value="${theme.cd}">${theme.name}</option>
							</c:if>
						</c:forEach>
					</select>
				</div>
				<div class="select_box1 radiusSel">
					<label>브랜드</label>
					<tags:codeList code="BRAND_CD" var="brand" tagYn="N"/>
					<select onchange="javascript:display.style.changeFilter($(this));" id="sel_brand">
						<option value="">브랜드</option>
						<c:forEach items="${brand}" var="brand">
							<option value="${brand.cd}">${brand.name}</option>
						</c:forEach>
					</select>
				</div>
			</li>
			<li class="box_02 pc_only">
				<div class="select_box1 radiusSel">
					<label>인기순</label>
					<select onchange="javascript:display.style.changeFilter($(this));" id="sel_sort">
						<option value="popular">인기순</option>
						<option value="new">최신순</option>
					</select>
				</div>
			</li>
			<li class="box_02 mo_only">
				<label class="radio_style1 option_style1">
					<em name="radio_style_2" class="selected">
						<input type="radio" name="ra1_3" value="popular" checked="checked" onClick="javascript:display.style.changeFilter($(this));">
					</em>
					<span>인기순</span>
				</label>
				<label class="radio_style1 option_style1">
					<em name="radio_style_1" >
						<input type="radio" name="ra1_3" value="new" onClick="javascript:display.style.changeFilter($(this));">
					</em>
					<span>최근등록순</span>
				</label>
			</li>
		</ul>
	</div>
	
	<!-- 스타일 리스트 -->		
	<div id="stylist_div" class="stylist_div">

	</div>
	
</div>

