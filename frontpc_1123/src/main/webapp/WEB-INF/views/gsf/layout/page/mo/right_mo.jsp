<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%
	pageContext.setAttribute("tommeetippe", Config.getString("brand.tommeetippe.code"));
	pageContext.setAttribute("allo", Config.getString("brand.allo.code"));
	pageContext.setAttribute("alfonso", Config.getString("brand.alfonso.code"));
	pageContext.setAttribute("fourlads", Config.getString("brand.fourlads.code"));
	pageContext.setAttribute("skarbarn", Config.getString("brand.skarbarn.code"));
	pageContext.setAttribute("royal", Config.getString("brand.royal.code"));
	pageContext.setAttribute("chooze", Config.getString("brand.chooze.code"));
	pageContext.setAttribute("yvol", Config.getString("brand.yvol.code"));
%>

<!-- ### mobile 자사브랜드 우측메뉴: 2016.08.17 추가 ### -->
<!-- <div class="com_brand">
<div class="box">
		<strong class="tit_com">0to7 브랜드</strong>
		<ul id="rnb_brandList">
			
		</ul>

		<button type="button" class="btn_com_close">전체메뉴 닫기</button>
	</div>
</div> -->

<div class="com_brand">
	<div class="box">
		<strong class="tit_com">브랜드 사이트</strong>	<!-- 0to7 브랜드 -> 브랜드 사이트 로 변경 요청 -->
		<ul>
			<li>
				<a href="javascript:brand.template.main('${allo}');">
					<img src="/resources/img/mobile/logo/brand1.jpg" alt="" />
				</a>
			</li>
			<li>
				<a href="javascript:brand.template.main('${alfonso}');">
					<img src="/resources/img/mobile/logo/brand2.jpg" alt="" />
				</a>
			</li>
			<li>
				<a href="javascript:brand.template.main('${fourlads}');">
					<img src="/resources/img/mobile/logo/brand3.jpg" alt="" />
				</a>
			</li>
			<li>
				<a href="javascript:brand.template.main('${skarbarn}');">
					<img src="/resources/img/mobile/logo/brand4.jpg" alt="" />
				</a>
			</li>
			<li>
				<a href="javascript:brand.template.main('${royal}');">
					<img src="/resources/img/mobile/logo/brand5.jpg" alt="" />
				</a>
			</li>
			<li>
				<a href="javascript:brand.template.main('${tommeetippe}');">
					<img src="/resources/img/mobile/logo/brand6.jpg" alt="" />
				</a>
			</li>
			<li>
				<a href="javascript:brand.template.main('${chooze}');">
					<img src="/resources/img/mobile/logo/brand7.jpg" alt="" />
				</a>
			</li>
			<li>
				<a href="javascript:brand.template.main('${yvol}');">
					<img src="/resources/img/mobile/logo/brand8.jpg" alt="" />
				</a>
			</li>
		</ul>

		<button type="button" class="btn_com_close">전체메뉴 닫기</button>
	</div>
</div>
<!-- ### //mobile 자사브랜드 : 2016.08.17 추가 ### -->