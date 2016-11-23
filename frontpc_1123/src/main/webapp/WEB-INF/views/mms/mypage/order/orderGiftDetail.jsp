<%--
	화면명 : 선물함 상세
	작성자 : allen
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

<script type="text/javascript" src="/resources/js/pms/pms.common.js"></script>
<%@ page import="gcp.frontpc.common.contants.Constants"%>

<script type="text/javascript">
	
	$(document).ready(function() {
		
		$("input[name=rad1]").off("click").on("click", function(e){
			if ($(this).attr("id") == 'new') {
				$("#newAddress").show();
				$("#defaultAddress").hide();
			} else {
				$("#defaultAddress").show();
				$("#newAddress").hide();
			}
		});
	});
	
	$(window).load(function () {
		ccs.common.telset('deliveryPhone2', $("#deliveryPhone2").val());
		if ($("#deliveryPhone1").val() != '') {
			ccs.common.telset('deliveryPhone1', $("#deliveryPhone1").val());	
		}
	});
	
	function chgSelect(param) {
		$(param).prev().text($('option:selected', param).attr('name'));
	}
	
	
</script>

<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
	<jsp:param value="마이쇼핑|MY주문관리|선물함 관리" name="pageNavi"/>
</jsp:include>
<div class="inner">
<div class="layout_type1">
<jsp:include page="/WEB-INF/views/gsf/layout/mypage/left_mypage.jsp" />
<div class="column">
	<div class="mypage mygiftView">
		<h3 class="title_type1">받은선물</h3>
		 <!-- 카드별 class : type1, type2, type3, type4 -->
		<c:choose>
			<c:when test="${giftOrderDetail.giftImgTypeCd eq 'GIFT_IMG_TYPE_CD.IMG1'}">
				<div class="giftTxt type1">	
			</c:when>
			<c:when test="${giftOrderDetail.giftImgTypeCd eq 'GIFT_IMG_TYPE_CD.IMG2'}">
				<div class="giftTxt type2">	
			</c:when>
			<c:when test="${giftOrderDetail.giftImgTypeCd eq 'GIFT_IMG_TYPE_CD.IMG3'}">
				<div class="giftTxt type3">	
			</c:when>
			<c:when test="${giftOrderDetail.giftImgTypeCd eq 'GIFT_IMG_TYPE_CD.IMG4'}">
				<div class="giftTxt type4">	
			</c:when>
		</c:choose>
		<p class="from"><strong>${giftOrderDetail.name1}</strong> 님께서 선물을 보내셨습니다.</p>
		<div class="giftMsg">
			<div>${giftOrderDetail.giftMsg}</div>
		</div>
	</div>
		
		<!--  출고 지시 전일때 -->
		<c:if test="${giftOrderDetail.orderDeliveryStateCd eq 'ORDER_DELIVERY_STATE_CD.REQ' || giftOrderDetail.orderDeliveryStateCd eq 'ORDER_DELIVERY_STATE_CD.READY'}">
			<fmt:parseDate value="${giftOrderDetail.expireDt}" var="dateFmt2" pattern="yyyy-MM-dd HH:mm:ss"/>
			<fmt:formatDate value="${dateFmt2}" var="expireDt" pattern="yyyy/MM/dd"/>
		
			<div id="giftModify">
				<ul class="giftGuide notice">
					<c:if test="${giftOrderDetail.orderDeliveryStateCd eq 'ORDER_DELIVERY_STATE_CD.REQ'}">
						<li>
							<span><em>${expireDt}일까지</em> 옵션 및 배송지를 입력해주세요.</span>
							<span>기간 경과시 주문이 자동 취소됩니다.</span>
						</li>
					</c:if>
	
					<!-- 로그인 전에만 노출 -->
					<c:if test="${loginId == '' || loginId == null}">
						<li class="mo_only">
							<span><a href="javascript:void(0);" onclick="ccs.link.login()">로그인</a> 하시면 배송지를 더욱 쉽게 선택할 수 있습니다.</span>
						</li>
					</c:if>
				</ul>
							
				<h4 class="sub_tit1 mo_only">
					<span>선물의 옵션과 배송지를 확인해주세요.</span>
				</h4>
				<div class="tbl_gift">
					<ul class="div_tb_tbody3">
						<c:forEach var="product" items="${giftOrderDetail.omsOrderproducts}">
							<c:if test="${product.orderProductTypeCd eq 'ORDER_PRODUCT_TYPE_CD.GENERAL' or product.orderProductTypeCd eq 'ORDER_PRODUCT_TYPE_CD.SET'}">
								<li>
									<div class="tr_box tr_idx">
										<div class="col1">
											<div class="positionR">
												<div class="prod_img">
													<a href="#none">
														<tags:prdImgTag productId="${product.productId}" seq="0" size="326"  />
													</a>
												</div>
			
												<a href="#none" class="title">
			<%-- 										[${product.brandName}]  --%>
													${product.productName }
												</a>
												
												<c:if test="${product.orderProductTypeCd eq 'ORDER_PRODUCT_TYPE_CD.SET'}">
													<c:forEach var="children" varStatus="idx3" items="${giftOrderDetail.omsOrderproducts}">
														<c:if test="${children.orderId eq product.orderId && children.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SUB' && children.upperOrderProductNo == product.orderProductNo}">
															<em class="option_txt" 
																data-order-id="${giftOrderDetail.orderId}"
																data-order-product-no="${giftOrderDetail.omsOrderproducts[0].orderProductNo}"
																data-product-id="${giftOrderDetail.omsOrderproducts[0].productId}"
																data-saleproduct-id="${giftOrderDetail.omsOrderproducts[0].saleproductId}"
																data-add-sale-price="${giftOrderDetail.omsOrderproducts[0].addSalePrice}"
																data-order-qty="${giftOrderDetail.omsOrderproducts[0].orderQty}"
																data-cancel-qty="${giftOrderDetail.omsOrderproducts[0].cancelQty}"
																data-return-qty="${giftOrderDetail.omsOrderproducts[0].returnQty}">
																<i>
																	<b>${children.productName} : <span>${children.saleproductName}</span></b>
																</i>
																<i style="float: right;">(${children.setQty}개)</i>
															</em>
														</c:if>
													</c:forEach>
												</c:if>
												<c:if test="${product.orderProductTypeCd eq 'ORDER_PRODUCT_TYPE_CD.GENERAL'}">
													<em class="option_txt"
														data-order-id="${giftOrderDetail.orderId}"
														data-order-product-no="${giftOrderDetail.omsOrderproducts[0].orderProductNo}"
														data-product-id="${giftOrderDetail.omsOrderproducts[0].productId}"
														data-saleproduct-id="${giftOrderDetail.omsOrderproducts[0].saleproductId}"
														data-add-sale-price="${giftOrderDetail.omsOrderproducts[0].addSalePrice}"
														data-order-qty="${giftOrderDetail.omsOrderproducts[0].orderQty}"
														data-cancel-qty="${giftOrderDetail.omsOrderproducts[0].cancelQty}"
														data-return-qty="${giftOrderDetail.omsOrderproducts[0].returnQty}">
														<c:if test="${not empty product.saleproductName}">
															<i>${product.saleproductName}</i>
														</c:if>
													</em>
												</c:if>
												<div class="piece">
													<span class="pieceNum">${product.orderQty}개</span>
												</div>
											</div>
										</div>
										<div class="col2">
											<div class="stateBox"
												data-order-id="${giftOrderDetail.orderId}"
												data-order-product-no="${giftOrderDetail.omsOrderproducts[0].orderProductNo}"
												data-order-product-type-cd="${giftOrderDetail.omsOrderproducts[0].orderProductTypeCd}"
												data-product-id = "${giftOrderDetail.omsOrderproducts[0].productId}"
												data-product-name = "${giftOrderDetail.omsOrderproducts[0].productName}"
												data-saleproduct-id = "${giftOrderDetail.omsOrderproducts[0].saleproductId}"
												data-saleproduct-name = "${giftOrderDetail.omsOrderproducts[0].saleproductName}"
												data-product-dealId = "${product.dealId}"
												data-tr-idx="1">
											
												<!-- DEAL 상품 아닐때만 옵션 변경 -->
<%-- 												<c:if test="${product.dealId eq '' || product.dealId eq null}"> --%>
													<!-- pc 버튼 -->
													<a href="#none" onclick="$order.change.option($(this));" class="btn_sStyle3 sGray2 btn_tb_option pc_only">옵션변경</a>
													<a href="javascript:void(0);" class="btn_sStyle3 sGray2 btn_tb_option mo_only" onclick="$order.change.option($(this), 'N');">옵션변경</a>
													
													<div class="ly_box option_box"></div>
													<!-- mo 버튼 -->
<%-- 												</c:if> --%>
											</div>
										</div>
									</div>
								</li>
							</c:if>
						</c:forEach>
					</ul>
				</div>
			
				<div class="write mtoggleBox on">
					<h4 class="sub_tit1 toggleBtn">
						<span>배송지 정보</span>
					</h4>
					
					<ul class="rw_tb_tbody2 toggleCont">
						<!-- 로그인 되어 있을경우 배송지 선택 버튼 노출 -->
						<c:if test="${loginId != '' && loginId != null}">
							<c:if test="${giftOrderDetail.orderDeliveryStateCd eq 'ORDER_DELIVERY_STATE_CD.REQ' || giftOrderDetail.orderDeliveryStateCd eq 'ORDER_DELIVERY_STATE_CD.READY'}">
								<li>
									<div class="tr_box">
										<div class="col1">
											<span class="group_inline">배송지 선택</span>
										</div>
										<div class="col2">
											<div class="group_block">
												<label class="radio_style1">
													<em>
														<input type="radio" name="rad1" value="" checked="checked" id="default"/>
													</em>
													<span>기본 배송지</span>
												</label>
												<label class="radio_style1">
													<em>
														<input type="radio" name="rad1" value="" id="new" />
													</em>
													<span>새로운 배송지</span>
												</label>
											</div>
										</div>
									</div>
								</li>
							</c:if>
						</c:if>
						
						<!-- 주문접수 일때 : Strart -->
						<c:if test="${giftOrderDetail.orderDeliveryStateCd eq 'ORDER_DELIVERY_STATE_CD.REQ'}">
							<!-- 기본배송지 정보가 있을 경우 -->
							<c:if test="${basicAddress != null }">
								<div id="defaultAddress">
									<li>
										<div class="tr_box txt_box recipient">
											<div class="col1">
												<span class="group_inline">받으실 분</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<span>${basicAddress.deliveryName1 }</span>
													<a href="javascript:mypage.gifticon.deliveryAddress();" class="btn_sStyle4 sGray2 btnAddrList">배송지 목록</a>
												</div>
											</div>
										</div>
									</li>
									<li>
										<div class="tr_box txt_box">
											<div class="col1">
												<span class="group_inline">연락처</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<span class="phoneNum">${basicAddress.phone2}  /  ${basicAddress.phone1}</span>
												</div>
											</div>
										</div>
									</li>
									<li>
										<div class="tr_box txt_box">
											<div class="col1">
												<span class="group_inline">배송지</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<span>(${basicAddress.zipCd}) ${basicAddress.address1} ${basicAddress.address2}</span>
												</div>
											</div>
										</div>
									</li>
								</div>
								
								<div id="newAddress" style="display:none">
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">받으실 분</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<div class="inpBox">
														<div class="inputTxt_place1">
															<label class="mo_only">받으실 분</label>
															<span>
																<input type="text" value="" id="addr_name" />
															</span>
														</div>
														<c:if test="${loginId != '' && loginId != null}">
															<a href="javascript:mypage.gifticon.deliveryAddress();" class="btn_sStyle4 sGray2 btnAddrList">배송지 목록</a>
														</c:if>
													</div>
												</div>
											</div>
										</div>
									</li>
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">연락처</span>
											</div>
											<div class="col2">
												<div class="group_block phone">
													<div class="inputMix_style2">
														<div class="select_box1">
													 		<label id="phoneNumLabel">선택</label>
															<tags:codeList code="MOBILE_NUMBER_CD" optionHead="선택" />
														</div>
														<span class="hyphen">-</span>
														<div class="inputTxt_place1">
															<label></label>
															<span>
																<input type="text" value="" name="phone2" id="deliveryPhone2_num1" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');">
															</span>
														</div>
														<span class="hyphen">-</span>
														<div class="inputTxt_place1">
															<label></label>
															<span>
																<input type="text" value="" name="phone2" id="deliveryPhone2_num2" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');">
															</span>
														</div>
													</div>
													<div class="inputMix_style2">
														<div class="select_box1">
													 		<label id="areaNumLabel">선택</label>
															<tags:codeList code="PHONE_NUMBER_CD" optionHead="선택"/>
														</div>
														<span class="hyphen">-</span>
														<div class="inputTxt_place1">
															<label></label>
															<span>
																<input type="text" value="" id="deliveryPhone1_num1" name="phone1" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');">
															</span>
														</div>
														<span class="hyphen">-</span>
														<div class="inputTxt_place1">
															<label></label>
															<span>
																<input type="text" value="" id="deliveryPhone1_num1" name="phone1" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');">
															</span>
														</div>
													</div>
												</div>
											</div>
										</div>
									</li>
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">배송지</span>
											</div>
											<div class="col2">
												<div class="group_block adr">
													<div class="inpBox">
														<div class="inputTxt_place1">
															<label></label>
															<span>
																<input type="text" value="" name="zipCd" id="addr_zipCd" readonly>
															</span>
														</div>
														<a href="javascript:mypage.deliveryAddress.searchAddress();" class="btn_sStyle4 sGray2">우편번호검색</a>
													</div>
													<div class="inputTxt_place1">
														<label>상세주소 1</label>
														<span>
															<input type="text" value="" name="address1" id="addr_address1" readonly>
														</span>
													</div>
													<div class="inputTxt_place1">
														<label>상세주소 2</label>
														<span>
															<input type="text" value="" name="address2" id="addr_address2" readonly>
														</span>
													</div>
												</div>
											</div>
										</div>
									</li>
								</div>
							</c:if>
						
							<!-- 기본배송지 정보가 없을 경우 -->
							<c:if test="${basicAddress == null }">
								<li>
									<div class="tr_box">
										<div class="col1">
											<span class="group_inline">받으실 분</span>
										</div>
										<div class="col2">
											<div class="group_block">
												<div class="inpBox">
													<div class="inputTxt_place1">
														<label class="mo_only">받으실 분</label>
														<span>
															<input type="text" value="" id="addr_name" />
														</span>
													</div>
													<c:if test="${loginId != '' && loginId != null}">
														<a href="javascript:mypage.gifticon.deliveryAddress();" class="btn_sStyle4 sGray2 btnAddrList">배송지 목록</a>
													</c:if>
												</div>
											</div>
										</div>
									</div>
								</li>
								<li>
									<div class="tr_box">
										<div class="col1">
											<span class="group_inline">연락처</span>
										</div>
										<div class="col2">
											<div class="group_block phone">
												<div class="inputMix_style2">
													<div class="select_box1">
												 		<label id="deliveryPhone2_areaCode">선택</label>
														<tags:codeList code="MOBILE_NUMBER_CD" optionHead="선택" />
													</div>
													<span class="hyphen">-</span>
													<div class="inputTxt_place1">
														<label></label>
														<span>
															<input type="text" value="" name="phone2" id="deliveryPhone2_num1" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');">
														</span>
													</div>
													<span class="hyphen">-</span>
													<div class="inputTxt_place1">
														<label></label>
														<span>
															<input type="text" value="" name="phone2" id="deliveryPhone2_num2" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');">
														</span>
													</div>
												</div>
												<div class="inputMix_style2">
													<div class="select_box1">
												 		<label id="deliveryPhone1_areaCode">선택</label>
														<tags:codeList code="PHONE_NUMBER_CD" optionHead="선택"/>
													</div>
													<span class="hyphen">-</span>
													<div class="inputTxt_place1">
														<label></label>
														<span>
															<input type="text" value="" id="deliveryPhone1_num1" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');">
														</span>
													</div>
													<span class="hyphen">-</span>
													<div class="inputTxt_place1">
														<label></label>
														<span>
															<input type="text" value="" id="deliveryPhone1_num2" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');">
														</span>
													</div>
												</div>
											</div>
										</div>
									</div>
								</li>
								<li>
									<div class="tr_box">
										<div class="col1">
											<span class="group_inline">배송지</span>
										</div>
										<div class="col2">
											<div class="group_block adr">
												<div class="inpBox">
													<div class="inputTxt_place1">
														<label></label>
														<span>
															<input type="text" value="" name="zipCd" id="addr_zipCd" readonly>
														</span>
													</div>
													<a href="javascript:mypage.deliveryAddress.searchAddress();" class="btn_sStyle4 sGray2">우편번호검색</a>
												</div>
												<div class="inputTxt_place1">
													<label>상세주소 1</label>
													<span>
														<input type="text" value="" name="address1" id="addr_address1" readonly>
													</span>
												</div>
												<div class="inputTxt_place1">
													<label>상세주소 2</label>
													<span>
														<input type="text" value="" name="address2" id="addr_address2" readonly>
													</span>
												</div>
											</div>
										</div>
									</div>
								</li>
							</c:if>
						</c:if>
						<!-- 주문접수 일때 : End -->
						
	
						<!-- 출고지시 대기 일때 : Strart -->
						<c:if test="${giftOrderDetail.orderDeliveryStateCd eq 'ORDER_DELIVERY_STATE_CD.READY'}">
							<c:forEach items="${giftOrderDetail.omsDeliveryaddresss}" var="deliveryAddress">
								<li>
									<div class="tr_box">
										<div class="col1">
											<span class="group_inline">받으실 분</span>
										</div>
										<div class="col2">
											<div class="group_block">
												<div class="inpBox">
													<div class="inputTxt_place1">
														<label class="mo_only">받으실 분</label>
														<span>
															<input type="text" value="${deliveryAddress.name1}" id="addr_name" />
														</span>
													</div>
													<c:if test="${loginId != '' && loginId != null}">
														<a href="javascript:mypage.gifticon.deliveryAddress();" class="btn_sStyle4 sGray2 btnAddrList">배송지 목록</a>
													</c:if>
												</div>
											</div>
										</div>
									</div>
								</li>
								<li>
									<div class="tr_box">
										<div class="col1">
											<span class="group_inline">연락처</span>
										</div>
										<div class="col2">
											<div class="group_block phone">
												<div class="inputMix_style2">
													<div class="select_box1">
												 		<label id="deliveryPhone2_areaCode">선택</label>
														<tags:codeList code="MOBILE_NUMBER_CD" optionHead="선택"/>
													</div>
													<span class="hyphen">-</span>
													<div class="inputTxt_place1">
														<label></label>
														<span>
															<input type="text" value="" name="deliveryPhone2_num1" id="deliveryPhone2_num1" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');">
														</span>
													</div>
													<span class="hyphen">-</span>
													<div class="inputTxt_place1">
														<label></label>
														<span>
															<input type="text" value="" name="deliveryPhone2_num2" id="deliveryPhone2_num2" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');">
														</span>
													</div>
												</div>
												<div class="inputMix_style2">
													<div class="select_box1">
												 		<label id="deliveryPhone1_areaCode">선택</label>
														<tags:codeList code="PHONE_NUMBER_CD" optionHead="선택"/>
													</div>
													<span class="hyphen">-</span>
													<div class="inputTxt_place1">
														<label></label>
														<span>
															<input type="text" value="" name="deliveryPhone1_num1" id="deliveryPhone1_num1" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');">
														</span>
													</div>
													<span class="hyphen">-</span>
													<div class="inputTxt_place1">
														<label></label>
														<span>
															<input type="text" value="" name="deliveryPhone1_num2" id="deliveryPhone1_num2" maxlength="4" onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');">
														</span>
													</div>
												</div>
											</div>
										</div>
									</div>
								</li>
								<li>
									<div class="tr_box">
										<div class="col1">
											<span class="group_inline">배송지</span>
										</div>
										<div class="col2">
											<div class="group_block adr">
												<div class="inpBox">
													<div class="inputTxt_place1">
														<label></label>
														<span>
															<input type="text" value="${deliveryAddress.zipCd}" name="zipCd" id="addr_zipCd" readonly>
														</span>
													</div>
													<a href="javascript:mypage.deliveryAddress.searchAddress();" class="btn_sStyle4 sGray2">우편번호검색</a>
												</div>
												<div class="inputTxt_place1">
													<label>상세주소 1</label>
													<span>
														<input type="text" value="${deliveryAddress.address1}" name="address1" id="addr_address1" readonly>
													</span>
												</div>
												<div class="inputTxt_place1">
													<label>상세주소 2</label>
													<span>
														<input type="text" value="${deliveryAddress.address2}" name="address2" id="addr_address2" readonly>
													</span>
												</div>
											</div>
										</div>
									</div>
								</li>
							</c:forEach>
						</c:if>	
						<!-- 출고지시 일때 : End -->
						
						<li>
							<div class="tr_box">
								<div class="col1">
									<span class="group_inline">배송메세지</span>
								</div>
								<div class="col2">
									<div class="group_block msg">
										<div class="select_box1">
											<label>${giftOrderDetail.omsDeliveryaddresss[0].note}</label>
											<tags:codeList code="DELIVERY_MESSAGE_CD" var="deliveryMsgCd" tagYn="N"/>
											<select onChange="javascript:mypage.gifticon.setDeliveryMsg(this);">
												<c:forEach items="${deliveryMsgCd}" var="msg" varStatus="i">
													<c:if test="${not empty giftOrderDetail.omsDeliveryaddresss[0].note}">
														<c:if test="${msg.name eq giftOrderDetail.omsDeliveryaddresss[0].note}">
															<option value="${msg.cd}" selected>${msg.name}</option>
														</c:if>
														<c:if test="${msg.name ne giftOrderDetail.omsDeliveryaddresss[0].note}">
															<option value="${msg.cd}">${msg.name}</option>
														</c:if>
													</c:if>
													
													<c:if test="${empty giftOrderDetail.omsDeliveryaddresss[0].note}">
														<c:if test="${i.first}">
															<option value="${msg.cd}" selected>${msg.name}</option>
														</c:if>
														<c:if test="${!i.first}">
															<option value="${msg.cd}">${msg.name}</option>
														</c:if>
													</c:if>
												</c:forEach>
												<option value="">직접입력</option>
												
											</select>
										</div>
										<div class="inputTxt_place1" style="display:none">
											<label></label>
											<span>
												<input type="text" value="${giftOrderDetail.omsDeliveryaddresss[0].note}" id="deliveryMsg" />
											</span>
										</div>
									</div>
								</div>
							</div>
						</li>
					</ul>
				</div>
			
		<c:if test="${loginId == '' || loginId == null}">
			<c:if test="${giftOrderDetail.orderDeliveryStateCd eq 'ORDER_DELIVERY_STATE_CD.REQ'}">
				<div class="orderBox2">
					<!-- 1114 -->
					<div class="personalBox">
						<dl>
							<!-- 1114 -->
							<dt>
								<span class="mo_only">비회원 구매 개인정보취급방침</span>
								<p class="chkAgree pc_only">
									<label class="chk_style1">
										<em>
											<input type="checkbox" name="pcAgreeCheck" value="">
										</em>
										<span>비회원 구매 개인정보취급방침</span>
									</label>
								</p>
							</dt>

							<dd class="personalH">
								<div>
									<strong>수집하는 개인정보 항목</strong>
									<ul>
										<li>- 성명, 주문비밀번호, 전화번호, 휴대폰
									번호, e-mail, 주소</li>
										<li>- 결제수단 선택에 따라 신용카드 번호,
									은행계좌번호, 환불계좌번호</li>
									</ul>

									<strong>수집이용목적</strong>
									<ul>
										<li>- 성명, 주문비밀번호, 전화번호,휴대폰번호, e-mail: 고지의 전달, 불만처리, 주문/배송정보 안내 등 원활한 의사소통 경로의 확보</li>
										<li>- 주소: 고지의 전달, 청구서, 정확한 상품 배송지의 확보</li>
										<li>- 신용카드번호, 은행계좌번호, 환불계좌번호: 상품 구매에 대한 결제 및 환불 시 계좌 입금처 확보</li>
									</ul>
									<strong>수집하는 개인정보의 보유기간</strong>
									<ul>
										<li>- 계약 또는 청약철회 등에 관한 기록: 5년 (전자상거래 등에서의 소비자보호에 관한 법률)</li>
										<li>- 소비자의 불만 또는 분쟁처리에 관한 기록: 3년 (전자상거래 등에서의 소비자보호에 관한 법률)</li>
										<li>- 대금결제 및 재화 등의 공급에 관한 기록: 5년 (전자상거래 등에서의 소비자보호에 관한 법률)</li>
									</ul>
								</div>
							</dd>
							<!-- //1114 -->

						</dl>
					</div>
					<p class="chkAgree mo_only">
						<label class="chk_style1">
							<em>
								<input type="checkbox" value="" name="moAgreeCheck">
							</em>
							<span>동의합니다.</span>
						</label>
					</p>
				</div>
			</c:if>
		</c:if>
			
		<c:if test="${giftOrderDetail.orderDeliveryStateCd eq 'ORDER_DELIVERY_STATE_CD.REQ'}">
			<div class="btn_wrapC btn1ea">
				<a href="javascript:mypage.gifticon.deliveryApproval('${loginId}');" class="btn_mStyle1 sPurple1">배송 요청하기</a>
			</div>
		</c:if>
		<c:if test="${giftOrderDetail.orderDeliveryStateCd eq 'ORDER_DELIVERY_STATE_CD.READY'}">
			<div class="btn_wrapC btn1ea">
				<a href="javascript:mypage.gifticon.deliveryApproval('${loginId}');" class="btn_mStyle1 sPurple1">수정 하기</a>
			</div>
		</c:if>
		</div>
		
		</c:if>
		
		<c:if test="${giftOrderDetail.orderDeliveryStateCd ne 'ORDER_DELIVERY_STATE_CD.REQ' 
							&& giftOrderDetail.orderDeliveryStateCd ne 'ORDER_DELIVERY_STATE_CD.READY'}">
			<fmt:parseDate value="${giftOrderDetail.omsOrderproducts[0].deliveryDt}" var="dateFmt" pattern="yyyy-MM-dd HH:mm:ss"/>
			<fmt:formatDate value="${dateFmt}" var="deliveryDt" pattern="yyyy/MM/dd"/>
			<div id="giftFinish">
				<c:if test="${giftOrderDetail.orderDeliveryStateCd eq 'ORDER_PRODUCT_STATE_CD.DELIVERY'}">
					<ul class="giftGuide notice pc_only">
						<li>
							<strong class="finish">${deliveryDt}일 선물 배송 완료</strong>
						</li>
					</ul>
					<h4 class="sub_tit1 finish mo_only">
						<span>배송완료</span>
					</h4>
				</c:if>
				
				<div class="tbl_gift">
					<ul class="div_tb_tbody3">
						<c:forEach var="product" items="${giftOrderDetail.omsOrderproducts}">
							<li>
								<div class="tr_box">
									<div class="col1">
										<div class="positionR">
											<div class="prod_img">
												<a href="#none">
													<tags:prdImgTag productId="${product.productId}" size="326"  />
												</a>
											</div>
			
											<a href="#none" class="title">
												[${product.brandName}] ${product.productName }
											</a>
			
											<em class="option_txt">
												<c:if test="${not empty product.erpColorId && not empty product.erpSizeId}">
													<i>${product.erpColorId} / ${product.erpSizeId}</i>
												</c:if>
											</em>
			
											<span class="number">${product.orderQty}개</span>
										</div>
									</div>
								</div>
							</li>
						</c:forEach>
					</ul>
				</div>
	
				<div class="write mtoggleBox on">
					<h4 class="sub_tit1 toggleBtn">
						<span>배송지 정보</span>
					</h4>
					
					<ul class="rw_tb_tbody2 toggleCont">
						<c:forEach items="${giftOrderDetail.omsDeliveryaddresss}" var="deliveryAddress">
							<li>
								<div class="tr_box txt_box recipient">
									<div class="col1">
										<span class="group_inline">받으실 분</span>
									</div>
									<div class="col2">
										<div class="group_block">
											<span>${deliveryAddress.name1}</span>
										</div>
									</div>
								</div>
							</li>
							<li>
								<div class="tr_box txt_box">
									<div class="col1">
										<span class="group_inline">연락처</span>
									</div>
									<div class="col2">
										<div class="group_block">
											<span class="phoneNum">${deliveryAddress.phone2} /  ${deliveryAddress.phone1}</span>
										</div>
									</div>
								</div>
							</li>
							<li>
								<div class="tr_box txt_box">
									<div class="col1">
										<span class="group_inline">배송지</span>
									</div>
									<div class="col2">
										<div class="group_block">
											<span>(${deliveryAddress.zipCd}) ${deliveryAddress.address1} ${deliveryAddress.address2}</span>
										</div>
									</div>
								</div>
							</li>
			
							<li>
								<div class="tr_box txt_box">
									<div class="col1">
										<span class="group_inline">배송메세지</span>
									</div>
									<div class="col2">
										<div class="group_block">
											<span>${deliveryAddress.note}</span>
										</div>
									</div>
								</div>
							</li>
						
						</c:forEach>
					</ul>
				</div>
			</div>
		</c:if>

		<dl class="giftNotice mtoggleBox">
			<dt class="toggleBtn">받은 선물 취소/환불 안내</dt>
			<dd class="toggleCont">
				<p>- 선물 주문은 선물 받으신 분이 배송지 입력을 하셔야 <br>&nbsp;&nbsp;주문이 완료되어 배송이 시작됩니다. </p>
				<p>- 선물 주문은 메시지를 받은 날로부터 7일간 유지되며, <br>&nbsp;&nbsp;유효기간 경과 시 주문이 자동 취소됩니다.</p>
				<p>- 선물 주문은 선물 보낸 날과, 선물 받으신 분의 배송지 <br>&nbsp;&nbsp;입력 시점차이로 상품이 품절될 수 있으며, 상품 품절 시 <br>&nbsp;&nbsp;주문은 자동 취소됩니다.</p>
				<p>- 선물 주문의 경우 교환/반품은 선물 보내신 분을 통해 <br>&nbsp;&nbsp;신청 가능합니다.</p>
				<p>- 주문취소/반품 처리시 선물하신 분에게 자동 환불처리 <br>&nbsp;&nbsp;됩니다.</p>
				<p>- 선물하기에 대한 궁금한 사항은 고객센터 1:1문의 또는 <br>&nbsp;&nbsp;고객센터 1588-8744로 연락 부탁 드립니다.</p>
			</dd>
		</dl>
	</div>
</div>
</div>



<form name="deliveryForm" id="deliveryForm">
	<input type="hidden" name="orderId" id="" value="${giftOrderDetail.orderId}"/> 
	<input type="hidden" name="productId" id="" value="${giftOrderDetail.omsOrderproducts[0].productId}"/> 
	<input type="hidden" name="saleProductId" id="" value="${giftOrderDetail.omsOrderproducts[0].saleproductId}"/> 
	<input type="hidden" name="name1" id="" value="${giftOrderDetail.omsDeliveryaddresss[0].name1}"/> 
	<input type="hidden" name="phone1" id="deliveryPhone1" value="${giftOrderDetail.omsDeliveryaddresss[0].phone1}"/> 
	<input type="hidden" name="phone2" id="deliveryPhone2" value="${giftOrderDetail.omsDeliveryaddresss[0].phone2}"/> 
	<input type="hidden" name="hidZipCd" id="hidZipCd" value="${giftOrderDetail.omsDeliveryaddresss[0].zipCd}"/> 
	<input type="hidden" name="hidAddress1" id="hidAddress1" value="${giftOrderDetail.omsDeliveryaddresss[0].address1}"/> 
	<input type="hidden" name="hidAddress2" id="hidAddress2" value="${giftOrderDetail.omsDeliveryaddresss[0].address2}"/> 
	<input type="hidden" name="note" id="hidNote" value=""/> 
	<input type="hidden" name="deliveryAddressNo" id="" value="${giftOrderDetail.omsDeliveryaddresss[0].deliveryAddressNo}"/> 
</form>

