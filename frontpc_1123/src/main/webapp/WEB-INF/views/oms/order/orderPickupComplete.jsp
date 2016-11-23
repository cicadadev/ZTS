<%--
	화면명 : 주문완료
	작성자 : dennis
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<script type="text/javascript">

//매장픽업 지도
var openOffshopMap = function(productNo){
	var form  = $("#pickupForm"+productNo);
	
	var html = '\
				<dl>\
					<dt>'+form.find("#offshopName").html()+'</dt>\
					<dd>\
						'+form.find("#offshopAddress1").val()+'<br />\
						'+form.find("#offshopAddress2").val()+'\
					</dd>\
					<dd>\
						휴점일 : '+form.find("#holidayInfo").val()+'\
						<a href="tel:'+form.find("#managerPhone").val()+'" class="btn_imgTel">\
							<em>'+form.find("#managerPhone").val()+'</em>\
						</a>\
					</dd>\
				</dl>\
				\
				<div class="shop_map" id="shop_map">\
				</div>\
				\
				<p class="txt_info">\
				'+form.find("#addressInfo").val()+'\
				</p>\
				';
				
	$("#layer_info").html(html);
	
	common.map("shop_map",form.find("#latitude").val(), form.find("#longitude").val());			   
}

</script>
	<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
		<jsp:param value="신청완료" name="pageNavi"/>
	</jsp:include>	
	<div class="inner">

		<div class="orderComplete">
			<!-- 160926 수정 -->
			<div class="step">
				<h3 class="title_type1">
					신청완료
				</h3>
				<ol>
					<li><span class="step_01">01</span>장바구니</li>
					<li class="active"><span class="step_03">02</span>신청완료</li>
				</ol>
			</div>
			<!-- //160926 수정 -->
			<div class="completeWrap">
				<strong class="title">매장픽업 신청이 정상적으로 완료되었습니다.</strong>

				<ul class="completeInfo">
					<li class="first">
						<strong>신청번호</strong>${omsPickup.pickupId }
					</li>
					<li>
						<strong>신청일시</strong>${omsPickup.pickupReqDt }
					</li>
				</ul>
			</div>
			<div class="viewTblList sp_pickup_type">
				<!-- ### 테이블 헤더 ### -->
				<div class="div_tb_thead3">
					<div class="tr_box">

						<span class="col1">상품/옵션정보</span>

						<span class="col2">수량</span>

						<span class="col3">상품금액</span>
					</div>
				</div>
				<!-- ### //테이블 헤더 ### -->

				<!-- ### 테이블 바디 ### -->
				<ul class="div_tb_tbody3">
					<c:forEach items="${omsPickup.omsPickupproducts }" var="pp">
					<form id="pickupForm${pp.productNo }">
					<li >
						<div class="tr_box">
							<div class="col1">
								<div class="positionR">
									<div class="prod_img">
										<a href="#none">
											<tags:prdImgTag productId="${pp.productId}" size="90" alt="${pp.pmsProduct.name }"/>											
										</a>
									</div>
									<a href="#none" class="title" onclick="javascript:ccs.link.product.detail('${pp.productId}')">
										[${pp.pmsProduct.brandName }] ${pp.pmsProduct.name }
									</a>
									<em class="option_txt">
										<i>옵션 :${pp.pmsSaleproduct.name }</i>
									</em>
								</div>
<!-- 								<u class="gift_txt"> -->
<!-- 									<span class="btn_tb_gift"> -->
<!-- 										<span class="icon_type1 iconBlue3">사은품</span> 사은품1, 사은품2, 사은품3, 사은품4, 사은품5, 사은품6, 사은품7, 사은품8, 사은품9, 사은품10 -->
<!-- 									</span> -->
<!-- 								</u> -->
<!-- 								<u class="gift_txt"> -->
<!-- 									<span class="btn_tb_gift"> -->
<!-- 										<span class="icon_type1 iconBlue3">선물포장</span> 신청 -->
<!-- 									</span> -->
<!-- 								</u> -->
							</div>

							<div class="col2">
								<div class="quantity_result">
									${pp.orderQty }개
								</div>
							</div>

							<div class="col3">
								<span class="price">
									<em><fmt:formatNumber value="${pp.totalSalePrice }" pattern="#,###"/> <i>원</i></em>
								</span>
							</div>

							<input type="hidden" id="offshopName" value="${pp.ccsOffshop.name }"/>
							<input type="hidden" id="offshopAddress1" value="${pp.ccsOffshop.address1 }"/>
							<input type="hidden" id="offshopAddress2" value="${pp.ccsOffshop.address2 }"/>
							<input type="hidden" id="holidayInfo" value="${pp.ccsOffshop.holidayInfo }"/>
							<input type="hidden" id="managerPhone" value="${pp.ccsOffshop.managerPhone }"/>
							<input type="hidden" id="addressInfo" value="${pp.ccsOffshop.addressInfo }"/>
							<input type="hidden" id="latitude" value="${pp.ccsOffshop.latitude }"/>
							<input type="hidden" id="longitude" value="${pp.ccsOffshop.longitude }"/>
							<div class="special sp_pickup">
								<dl>
									<dt>픽업매장 :</dt>
									<dd>
										${pp.ccsOffshop.name }
										<a href="#none" class="btn_storeMap1 btn_shopPosition" onclick="javascript:openOffshopMap('${pp.productNo}')">매장위치</a>
									</dd>
								</dl>
								<dl>
									<dt>픽업예정일 : </dt>
									<dd>${pp.pickupReserveDt } (${pp.pickupReserveDy }요일)</dd>
								</dl>
								<label class="chk_style1">
									<em>
										<input type="checkbox" value="" onclick="javascript:ccs.offshop.saveInterestOffshop(this,'${pp.offshopId}')"/>
									</em>
									<span>관심 매장으로 저장</span>
								</label>
							</div>
						</div>
					</li>
					</form>
					</c:forEach>					
				</ul>
				<!-- ### //테이블 바디 ### -->
			</div>

			<div class="btn_wrapC btn2ea">
				<a href="#none" onclick="javascript:ccs.link.common.main()" class="btn_bStyle1 sWhite1">계속 쇼핑하기</a>
				<a href="/mms/mypage/order/pickup/${omsPickup.pickupId }" class="btn_bStyle1 sPurple1">매장픽업 신청내역</a>
			</div>
		</div>
	</div>

<!-- ### 매장 위치 레이어 : 2016.08.28 수정 ### -->
<div class="pop_wrap layer_shop">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">매장 위치</h3>
		</div>
		<div class="pop_content">
			<div class="shop_info" id="layer_info">
				
			</div>
		</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>
<!-- ### //매장 위치 레이어 : 2016.08.28 수정 ### -->