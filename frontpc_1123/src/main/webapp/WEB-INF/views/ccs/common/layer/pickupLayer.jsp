<%--
	화면명 : 매장픽업 레이어
	작성자 : eddie
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<div class="pop_wrap ly_storePickup ly_pickup1 layer_pick_up" id="pickupLayer">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">픽업매장 선택하기</h3>
		</div>
		<div class="pop_content">
			<ul class="list">
<c:forEach var="saleproduct" items="${saleproductList }">			
				<li>
					<div class="goods">
						<div class="prod_img">
							<a href="#none">
								<tags:prdImgTag productId="${saleproduct.productId}" size="60" className="" seq="0" alt="${saleproduct.productName }" />
							</a>
						</div>

						<a href="#none" class="title">
							${saleproduct.productName }
						</a>

						<em class="option_txt">
							<i>옵션 : ${saleproduct.saleproductName }</i>
						</em>
					</div>

					<div class="column_2set">
						<div class="select_box1">
							<label id="offshopArea1Label_${saleproduct.saleproductId}">
							 ${ not empty saleproduct.ccsOffshop.areaDiv1 ? saleproduct.ccsOffshop.areaDiv1 : '시/도' }</label>
							<select id="offshopArea1_${saleproduct.saleproductId}" onchange="ccs.layer.pickupLayer.selectAreaDiv1('${saleproduct.saleproductId }')">
							<option value="">시/도</option>
							<c:forEach var="area" items="${saleproduct.areaDiv1s }">
								<option ${saleproduct.ccsOffshop.areaDiv1 eq area.areaDiv1 ? 'selected' : '' }>${area.areaDiv1}</option>
							</c:forEach>
							</select>
						</div>

						<div class="select_box1">
							<label id="offshopArea2Label_${saleproduct.saleproductId}">
							${ not empty saleproduct.ccsOffshop.areaDiv2 ? saleproduct.ccsOffshop.areaDiv2 : '시/군/구' }
							</label>
							<select id="offshopArea2_${saleproduct.saleproductId}" onchange="ccs.layer.pickupLayer.selectAreaDiv2('${saleproduct.saleproductId }')">
								<option value="">시/군/구</option>
							<c:forEach var="area" items="${saleproduct.areaDiv2s }">
								<option ${saleproduct.ccsOffshop.areaDiv2 eq area.areaDiv2 ? 'selected' : '' }>${area.areaDiv2}</option>
							</c:forEach>								
							</select>
						</div>
					</div>

					<div class="select_box1">
						<label id="offshopIdLabel_${saleproduct.saleproductId}">
						${ not empty saleproduct.ccsOffshop.name ? saleproduct.ccsOffshop.name : '지점' }
						</label>
						<select id="offshopId_${saleproduct.saleproductId}" data-saleproduct="${saleproduct.saleproductId }" onchange="ccs.layer.pickupLayer.selectOffShop('${saleproduct.saleproductId }')">
							<option value="">지점</option>
							<c:forEach var="shop" items="${saleproduct.shopList }">
								<option>${shop.name}</option>
							</c:forEach>							
						</select>
					</div>
				</li>
</c:forEach>
			</ul>
		
			<div class="btn_box">
				<a href="#none" class="btn_mStyle1 sWhite1" onclick="ccs.layer.pickupLayer.cancel()">취소</a>
				<a href="#none" class="btn_mStyle1 sPurple1" onclick="return ccs.layer.pickupLayer.confirm()">확인</a>
			</div>
		</div>
<c:if test="${isMobile ne 'true'}">		
		<button type="button" class="btn_x pc_btn_close">닫기</button>
</c:if>		
<c:if test="${isMobile eq 'true'}">
<!-- 		<button type="button" class="btn_navi_prev">이전 페이지로..</button> -->
		<button type="button" class="btn_x pc_btn_close">닫기</button>
</c:if>		
	</div>
</div>