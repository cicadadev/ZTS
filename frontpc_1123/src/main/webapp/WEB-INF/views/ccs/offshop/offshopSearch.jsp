<%--
	화면명 : 매장 찾기
	작성자 : stella
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%
	pageContext.setAttribute("allo", Config.getString("brand.allo.code") );
	pageContext.setAttribute("alfonso", Config.getString("brand.alfonso.code") );
	pageContext.setAttribute("fourlads", Config.getString("brand.fourlads.code") );
	pageContext.setAttribute("skarbarn", Config.getString("brand.skarbarn.code") );
	pageContext.setAttribute("royal", Config.getString("brand.royal.code") );	
	pageContext.setAttribute("tommeetippe", Config.getString("brand.tommeetippe.code") );
	pageContext.setAttribute("chooze", Config.getString("brand.chooze.code") );
	pageContext.setAttribute("yvol", Config.getString("brand.yvol.code") );
%>

<script type="text/javascript">
$(document).ready(function() {
	ccs.offshop.setCurrentPosition('', '', '${brandName}');
});	
</script>

	<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
		<jsp:param value="매장찾기" name="pageNavi"/>
	</jsp:include>	
	
	<div class="inner">
		<div class="srchStore">
			<h3><img src="/resources/img/pc/txt/tit_search_store.gif" alt="매장찾기"></h3>
			
			<div class="brandLogo">
				<ul id="brandUl">
					<li>
						<a href="javascript:ccs.offshop.getOffshopByBrand('${allo}');">
							<img src="/resources/img/pc/txt/brandlogo_01.jpg" alt="allo&amp;lugh" />
						</a>
					</li>
					<li>
						<a href="javascript:ccs.offshop.getOffshopByBrand('${alfonso}');">
							<img src="/resources/img/pc/txt/brandlogo_02.jpg" alt="alfonso" />
						</a>
					</li>
					<li>
						<a href="javascript:ccs.offshop.getOffshopByBrand('${fourlads}');">
							<img src="/resources/img/pc/txt/brandlogo_03.jpg" alt="fourlads" />
						</a>
					</li>
					<li>
						<a href="javascript:ccs.offshop.getOffshopByBrand('${skarbarn}');">
							<img src="/resources/img/pc/txt/brandlogo_04.jpg" alt="skarbarn" />
						</a>
					</li>
					<li>
						<a href="javascript:ccs.offshop.getOffshopByBrand('${royal}');">
							<img src="/resources/img/pc/txt/brandlogo_05.jpg" alt="궁중비책" />
						</a>
					</li>
					<li>
						<a href="javascript:ccs.offshop.getOffshopByBrand('${tommeetippe}');">
							<img src="/resources/img/pc/txt/brandlogo_06.jpg" alt="토미티피" />
						</a>
					</li>
					<li>
						<a href="javascript:ccs.offshop.getOffshopByBrand('${chooze}');">
							<img src="/resources/img/pc/txt/brandlogo_07.jpg" alt="chooze" />
						</a>
					</li>
					<li>
						<a href="javascript:ccs.offshop.getOffshopByBrand('${yvol}');">
							<img src="/resources/img/pc/txt/brandlogo_08.jpg" alt="와이볼루션" />
						</a>
					</li>
				</ul>
			</div>

			<div class="storeList">
				<div class="srchPack">
					<div class="srchOptionBox">
						<div class="srchType">
							<div class="select_box1">
								<label id="selectBrandLabel"></label>
								<select id="selectBrand" onchange="ccs.offshop.changeBrand();">
									<option value="">브랜드</option>
									<c:forEach items="${brandCodeList}" var="brand" varStatus="brand_status">
										<option value="${brand.brandId}">${brand.name}</option>
									</c:forEach>
								</select>
							</div>
						</div>

						<div class="srchType half">
							<div class="select_box1">
								<label></label>
								<select id="selectArea1" onchange="ccs.offshop.getArea2List();">
									<option value="">시/도</option>
									<c:forEach items="${area1List}" var="area1" varStatus="area1_status">
										<option>${area1.areaDiv1}</option>
									</c:forEach>
								</select>
							</div>
						</div>
						
						<div class="srchType half">
							<div class="select_box1">
								<label></label>
								<select id="selectArea2">
									<option value="">시/군/구</option>
								</select>
							</div>
						</div>

						<div class="inpBox">
							<div class="inputTxt_place1">
								<label>검색어를 입력하세요</label>
								<span>
									<input type="text" value="" id="searcyKeyword"/>
								</span>
							</div>
							<a href="javascript:ccs.offshop.setCurrentPosition();" class="btn_sStyle4 sPurple1 btnSrch">검색</a>
						</div>
					</div>

					<a href="javascript:ccs.offshop.getCloseOffshop();" class="near">주변매장보기</a>
				</div>

				<div class="titPkg">
					<h4>전체<span id="offshopCount"></span></h4>
					<div class="chkBox posR">
						<label class="chk_style1">
							<em onchange="ccs.offshop.searchPickupOffshop();">
								<input type="checkbox" value="" id="pickupCheckbox" />
							</em>
							<span>매장픽업 가능 매장만 보기</span>
						</label>
					</div>
				</div>

				<div class="list" id="offshopInfoArea">
				
				</div>
				
				<div class="btn_store_list">
					<a href="javascript:ccs.link.mypage.info.offshop();" class="btn_mStyle1 sWhite1">관심매장 목록 보기</a>
				</div>
			</div>
		</div>
	</div>
	
