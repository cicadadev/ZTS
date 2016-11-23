<%--
	화면명 : 정기배송신청
	작성자 : dennis
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<script type="text/javascript" src="/resources/js/common/order.ui.js"></script>
	<jsp:include page="/WEB-INF/views/oms/order/inner/orderRegularScriptInner.jsp" flush="false" />
	
	<!-- Mobile -->
	<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
		<jsp:param value="주문/결제" name="pageNavi"/>
	</jsp:include>	
	
	<div class="inner">
		<div class="orderBox">
			<div class="step">
				<h3 class="title_type1">
					정기배송신청
				</h3>
				<ol>
					<li><span class="step_01">01</span>장바구니</li>
					<li class="active"><span class="step_02">02</span>정기배송신청</li>
					<li><span class="step_03">03</span>신청완료</li>
				</ol>
			</div>
			<div class="control_box">
					<h3 class="sub_tit1">배송정보</h3>
					<a href="javascript:void(0);" class="btn_box_control">자세히</a>
			</div>
			<div>
				<input type="hidden" id="ra1_2_1" value="STD"/>
				<ul class="tabBox1" id="selDeliveryLi">
					<li class="on" id="std_li"><a href="#none" id="STD" onclick="javascript:chgDeliveryAddr_mo('STD')">기본 배송지</a></li>
					<li id="new_li">
						<a href="#none" id="NEW" onclick="javascript:chgDeliveryAddr_mo('NEW')">새로운 배송지</a>
					</li>
				</ul>
				<form id="addressForm">
				<div class="tab_con tab_conOn" id="stdDelivery">
					<!-- ### 배송지 정보 ### -->
					<div class="order_form tpye2">
						<div class="relBox">
							<div class="rw_tbBox">
								<ul class="rw_tb_tbody2">
									<li class="col_type">
										<div class="tr_box">
											<div class="col1 th_block">
												<span class="group_inline">주문고객</span>
											</div>
											<div class="col2">
												<div class="group_block order_user_info">
													<strong>${memberInfo.mmsMember.memberName}</strong>
												</div>
											</div>
										</div>
									</li>
									<li class="col_type">
										<div class="tr_box">
											<div class="col1 th_block">
												<span class="group_inline">받으실분</span>
											</div>
											<div class="col2">												
												<div class="group_block btn_pos">
													<strong id="deliveryNameTxt"></strong> <a href="#" onclick="javascript:addressOpen()" class="btn_sStyle4 sGray2">배송지 목록</a>
												</div>												
<!-- 												<div class="group_block shipping_form"> -->
<!-- 													<div class="inputTxt_place1"> -->
<!-- 														<label>받으실분</label> -->
<!-- 														<span> -->
<!-- 															<input type="text" value="" /> -->
<!-- 														</span> -->
<!-- 													</div> -->
<!-- 													<a href="#" onclick="javascript:addressOpen()" class="btn_sStyle4 sGray2">배송지 목록</a> -->
<!-- 												</div>												 -->
											</div>
										</div>
									</li>
									<li class="col_type">
										<div class="tr_box">
											<div class="col1 th_block">
												<span class="group_inline th_block">연락처</span>
											</div>
											<div class="col2">
												<div class="group_block" id="phoneTxt">
<!-- 													<div> -->
<!-- 														<label class="chk_style1"> -->
<!-- 															<em> -->
<!-- 																<input type="checkbox" value="" /> -->
<!-- 															</em> -->
<!-- 															<span>안심번호 사용</span> <a href="#none" class="btn_infor btn_save_num">안내</a> -->
<!-- 														</label> -->
<!-- 													</div> -->
												</div>
											</div>
										</div>
									</li>
									<li class="col_type">
										<div class="tr_box">
											<div class="col1 th_block">
												<span class="group_inline th_block">배송지</span>
											</div>
											<div class="col2">
												<div class="group_block" id="addressTxt"></div>
											</div>
										</div>
									</li>
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">배송 요청사항</span>
											</div>
											<div class="col2">
												<div class="group_block msgBox">
													<div>
														<div class="sel_box select_style1" id="stdnoteTxt">															
														</div>
													</div>
													<div id="stdnoteDiv" style="display: none;">
														<input type="text" name="stdnote" id="stdnote"  value="배송전 연락주세요." class="input_style2">
													</div>
												</div>
											</div>
										</div>
									</li>											
								</ul>
							</div>
						</div>
					</div>
					<input type="hidden" id="stddeliveryName1" value=""/>
					<input type="hidden" id="stdphone1" value=""/>
					<input type="hidden" id="stdphone2" value=""/>
					<input type="hidden" id="stdzipCd" value=""/>
					<input type="hidden" id="stdaddress1" value=""/>
					<input type="hidden" id="stdaddress2" value=""/>
					<input type="hidden" id="stdcountryNo" value=""/>
					<input type="hidden" id="stdemail" value=""/>
					<!-- ### //배송지 정보 ### -->
				</div>
				<div class="tab_con" id="newDelivery">								
					<!-- ### 배송지 정보 ### -->
					<div class="order_form tpye2">
						<div class="relBox">
							<div class="rw_tbBox">
								<ul class="rw_tb_tbody2">
									<li class="col_type">
										<div class="tr_box">
											<div class="col1 th_block">
												<span class="group_inline ">주문고객</span>
											</div>
											<div class="col2">
												<div class="group_block">													
													<strong class="order_customer">${memberInfo.mmsMember.memberName} <span>( ${memberInfo.mmsMember.phone2 } )</span></strong>
												</div>
											</div>
										</div>
									</li>
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">받으실분</span>
											</div>
											<div class="col2">
												<div class="group_block shipping_form">
													<div class="inputTxt_place1">
														<label>받으실분</label>
														<span>
															<input type="text" id="deliveryName1" value="" />															
														</span>
													</div>
													<a href="#none" class="btn_sStyle4 sGray2" onclick="javascript:addressOpen('NEW')">배송지 목록</a>
												</div>
											</div>
										</div>
									</li>
									<li>
										<div class="tr_box">
											<div class="col1">
												<span class="group_inline">휴대전화</span>
											</div>
											<div class="col2">
												<div class="group_block">
													<div class="inputMix_style2">
														<input type="hidden" name="newphone2" id="newphone2" value=""/>
														<div class="select_box1">
													 		<label id="newphone2_areaCode"></label>
															<tags:codeList code="MOBILE_NUMBER_CD" optionHead="선택" />
														</div>
														<span class="hyphen">-</span>
														<div class="inputTxt_place1">
															<label></label>
															<span>
																<input type="text" value="" id="newphone2_num1" maxlength="4" onblur="ccs.common.fn_press_han(this);"  onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');">
															</span>
														</div>
														<span class="hyphen">-</span>
														<div class="inputTxt_place1">
															<label></label>
															<span>
																<input type="text" value="" id="newphone2_num2" maxlength="4" onblur="ccs.common.fn_press_han(this);"  onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');">
															</span>
														</div>
													</div>
<!-- 													<div class="block_type2"> -->
														<div class="inputMix_style2">
															<input type="hidden" name="newphone1" id="newphone1" value="" />
															<div class="select_box1">
														 		<label id="newphone1_areaCode"></label>
														 		<tags:codeList code="PHONE_NUMBER_CD" optionHead="선택" />
															</div>
															<span class="hyphen">-</span>
															<div class="inputTxt_place1">
																<label></label>
																<span>
																<input type="text" value="" id="newphone1_num1" maxlength="4" onblur="ccs.common.fn_press_han(this);"  onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');">
																</span>
															</div>
															<span class="hyphen">-</span>
															<div class="inputTxt_place1">
																<label></label>
																<span>
																	<input type="text" value="" id="newphone1_num2" maxlength="4" onblur="ccs.common.fn_press_han(this);"  onkeyup="ccs.common.fn_press_han(this);" onkeypress="return ccs.common.fn_press(event, 'number');">
																</span>
															</div>
														</div>
<!-- 													</div>	 -->
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
												<div class="group_block addrBox">
													<div class="zip">
													<div class="inputTxt_place1">
														<label></label>
														<span>
															<input type="text" id="zipCd" value="" />
														</span>
													</div>
														<a href="#none" onclick="javascript:openAddressPopup()" class="btn_sStyle4 sGray2">우편번호검색</a>
													</div>
													<div>
														<div class="inputTxt_place1">
															<label>상세주소 1</label>
															<span>
																<input type="text" id="address1" value="" readonly="readonly"/>
															</span>
														</div>
														<div class="inputTxt_place1">
															<label>상세주소 2</label>
															<span>
																<input type="text" id="address2" value="" />
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
												<span class="group_inline">배송 요청사항</span>
											</div>
											<div class="col2">
												<div class="group_block msgBox">
													<div>
														<div class="sel_box select_style1" id="noteTxt">															
														</div>														
													</div>
													<div id="noteDiv" style="display:none;">
														<input type="text" name="note" id="note" value="배송전 연락주세요." class="input_style2">
													</div>
												</div>
											</div>
										</div>
									</li>
								</ul>
							</div>
						</div>
					</div>
					<!-- ### //배송지 정보 ### -->
				</div>
				</form>
			</div>
			
			<c:set var="totalRegularDeliveryPrice" value="0"/>
			<c:set var="totalSalePrice" value="0"/>
			<c:set var="totalDeliveryFee" value="0"/>
			<c:set var="totalPointsave" value="0"/>
			
			<c:forEach items="${regular.omsRegulardeliveryproducts }" var="rg">
			<form name="regularForm" id="regularForm${rg.deliveryProductNo }">
			<input type="hidden" name="tempDeliveryProductNo" value="${rg.deliveryProductNo }"/>
			<input type="hidden" name="productId" id="productId" value="${rg.productId }"/>
			<input type="hidden" name="saleproductId" id="saleproductId" value="${rg.saleproductId }"/>						
			<input type="hidden" name="orderQty" id="orderQty" value="${rg.orderQty }"/>
			<input type="hidden" name="regularDeliveryPrice" id="regularDeliveryPrice" value="${rg.regularDeliveryPrice }"/>
			<input type="hidden" name="salePrice" id="salePrice" value="${rg.salePrice }"/>
<%-- 						<input type="hidden" id="addSalePrice" value="${rg.addSalePrice }" /> --%>
			<input type="hidden" id="totalPoint" value="${rg.totalPoint }"/>
			
			<c:set var="totalRegularDeliveryPrice" value="${totalRegularDeliveryPrice + (rg.regularDeliveryPrice * rg.orderQty) }"/>
			<c:set var="totalSalePrice" value="${totalSalePrice + (rg.salePrice * rg.orderQty) }"/>
			<c:set var="totalPointsave" value="${totalPointsave + (rg.totalPoint * rg.orderQty) }"/>
			
			<div class="viewTblList">
				<!-- ### 테이블 헤더 ### -->
				<div class="div_tb_thead3">
					<div class="tr_box">

						<span class="col1">상품/옵션정보</span>

						<span class="col2">수량</span>

						<span class="col3">배송비</span>

						<span class="col4">주문금액</span>
					</div>
				</div>
				<!-- ### //테이블 헤더 ### -->
				
				<!-- ### 테이블 바디 ### -->
				<ul class="div_tb_tbody3">
					<li >
						<div class="tr_box">
							<div class="col1">
								<div class="positionR">
									<div class="prod_img">
										<a href="#none" onclick="javascript:ccs.link.product.detail('${rg.productId}')">
											<tags:prdImgTag productId="${rg.productId}" size="90" alt="${rg.pmsProduct.name }"/>											
										</a>
									</div>
									<a href="#none" class="title" onclick="javascript:ccs.link.product.detail('${rg.productId}')">
										${rg.pmsProduct.name }
									</a>
				
									<c:choose>
									<c:when test="${rg.deliveryProductTypeCd == 'DELIVERY_PRODUCT_TYPE_CD.SET' }">
										<c:forEach items="${rg.omsRegulardeliveryproducts }" var="rgs" varStatus="st">
											<input type="hidden" name="omsRegulardeliveryproducts[${st.index }].productId" id="productId" value="${rgs.productId }"/>
											<input type="hidden" name="omsRegulardeliveryproducts[${st.index }].saleproductId" id="saleproductId" value="${rgs.saleproductId }"/>
											<em class="option_txt">
												<i>${rgs.pmsProduct.name } : ${rgs.pmsSaleproduct.name }</i>
											</em>	
										</c:forEach>											
									</c:when>
									<c:otherwise>
										<em class="option_txt">
											<i>옵션 : ${rg.pmsSaleproduct.name }</i>
										</em>
									</c:otherwise>
									</c:choose>
									<strong class="itemPrice"><fmt:formatNumber value="${rg.regularDeliveryPrice }" pattern="#,###"/>원</strong>
								</div>
							</div>
							<div class="col2">
								<div class="quantity_result">
									${rg.orderQty }개
								</div>
							</div>
							<div class="col3">
								<c:set var="deliveryFee" value="0"/>
								<c:choose>
								<c:when test="${rg.deliveryFeeFreeYn == 'Y' }">
									<c:set var="deliveryFee" value="0"/>
								</c:when>
								<c:otherwise>
									<c:if test="${rg.pmsProduct.ccsDeliverypolicy.minDeliveryFreeAmt >= (rg.regularDeliveryPrice * rg.orderQty)  }">
										<c:set var="deliveryFee" value="${rg.pmsProduct.ccsDeliverypolicy.deliveryFee }"/>	
									</c:if>
								</c:otherwise>
								</c:choose>
								
								<c:set var="totalDeliveryFee" value="${totalDeliveryFee + deliveryFee }"/>
								<span class="price">
									<em>
									
									<c:choose>
									<c:when test="${deliveryFee == 0 }">
										무료
									</c:when>
									<c:otherwise>
									<fmt:formatNumber value="${deliveryFee }" pattern="#,###"/> <i>원</i>
									</c:otherwise>
									</c:choose>																		
									</em>
								</span>
							</div>
							<div class="col4">
								<span class="price">
									<em><fmt:formatNumber value="${rg.regularDeliveryPrice * rg.orderQty + deliveryFee}" pattern="#,###"/> <i>원</i></em>
								</span>
							</div>
							
							<dl class="regular_set">
								<dt>배송주기/횟수</dt>
								<dd class="half">
									<div class="select_box1">
										<label>배송주기</label>
										<select name="deliveryPeriodCd" id="deliveryPeriodCd" onchange="javascript:calcDate('#regularForm${rg.deliveryProductNo }')"  disabled="disabled">
											<option value="DELIVERY_PERIOD_CD.1WEEK" <c:if test="${rg.deliveryPeriodCd == 'DELIVERY_PERIOD_CD.1WEEK' }">selected="selected"</c:if> >1주에 한번</option>
											<option value="DELIVERY_PERIOD_CD.2WEEK" <c:if test="${rg.deliveryPeriodCd == 'DELIVERY_PERIOD_CD.2WEEK' }">selected="selected"</c:if> >2주에 한번</option>
											<option value="DELIVERY_PERIOD_CD.3WEEK" <c:if test="${rg.deliveryPeriodCd == 'DELIVERY_PERIOD_CD.3WEEK' }">selected="selected"</c:if> >3주에 한번</option>
											<option value="DELIVERY_PERIOD_CD.4WEEK" <c:if test="${rg.deliveryPeriodCd == 'DELIVERY_PERIOD_CD.4WEEK' }">selected="selected"</c:if> >4주에 한번</option>
										</select>
									</div>
									<div class="select_box1">
										<label>배송횟수</label>
										<select name="deliveryCnt" id="deliveryCnt" onchange="javascript:calcDate('#regularForm${rg.deliveryProductNo }')"  disabled="disabled">
											<c:forEach begin="${rg.regularDeliveryMinCnt }" end="${rg.regularDeliveryMaxCnt }" varStatus="st">
												<option value="${st.index }" <c:if test="${rg.deliveryCnt == st.index }">selected="selected"</c:if> >${st.index }회</option>
											</c:forEach>
										</select>
									</div>
								</dd>
							</dl>		
							<dl class="regular_set">
								<dt>정기배송일</dt>
								
								<c:set var="select3" value=""/>
								<c:set var="select4" value=""/>
								<c:set var="select5" value=""/>
								<c:set var="select6" value=""/>
								<c:set var="select7" value=""/>									
								<c:set var="selectTxt" value=""/>
								<c:if test="${rg.deliveryPeriodValue == 3 }">
									<c:set var="select3" value="selected='selected'"/>
									<c:set var="selectTxt" value="화요일"/>
								</c:if>
								<c:if test="${rg.deliveryPeriodValue == 4 }">
									<c:set var="select4" value="selected='selected'"/>
									<c:set var="selectTxt" value="수요일"/>
								</c:if>
								<c:if test="${rg.deliveryPeriodValue == 5 }">
									<c:set var="select5" value="selected='selected'"/>
									<c:set var="selectTxt" value="목요일"/>
								</c:if>
								<c:if test="${rg.deliveryPeriodValue == 6 }">
									<c:set var="select6" value="selected='selected'"/>
									<c:set var="selectTxt" value="금요일"/>
								</c:if>
								<c:if test="${rg.deliveryPeriodValue == 7 }">
									<c:set var="select7" value="selected='selected'"/>
									<c:set var="selectTxt" value="토요일"/>
								</c:if>									
								<c:if test="${rg.deliveryPeriodValue == 1 }">
									<c:set var="select1" value="selected='selected'"/>
									<c:set var="selectTxt" value="일요일"/>
								</c:if>						
								
								<dd>
									<div class="select_box1">
										<label>${selectTxt }</label>
										<select name="deliveryPeriodValue" id="deliveryPeriodValue" onchange="javascript:calcDate('#regularForm${rg.deliveryProductNo }')"  disabled="disabled">
											<option value="3" ${select3 }>화요일</option>
											<option value="4" ${select4 }>수요일</option>
											<option value="5" ${select5 }>목요일</option>
											<option value="6" ${select6 }>금요일</option>
											<option value="7" ${select7 }>토요일</option>
<%-- 												<option value="1" ${select1 }>일요일</option>												 --%>
										</select>
									</div>
<!-- 										<a href="#none;" class="btn_sStyle4 sGray2">변경</a> -->
								</dd>
							</dl>												
						</div>
					</li>
				</ul>
				<!-- ### //테이블 바디 ### -->
			</div>
			<ul class="notice">
				<li>선택한 배송일이 공휴일인 경우 다음날 배송됩니다.</li>
				<li>설,추석과 같이 명절을 앞둔 경우나 폭우, 폭설과 같은 기상이변 발생시 배송은 지연될 수 있습니다.</li>
			</ul>
<!-- 			<div class="shipping_info"> -->
<!-- 				<strong>나의 정기배송일</strong> -->
<!-- 				<p> -->
<%-- 					<span>배송주기/횟수 : <em id="regularDeliveryDateTxt">${rg.deliveryPeriodName} /  총${rg.deliveryCnt }회</em></span> --%>
<%-- 					<span>배송요일 : <em id="regularDeliveryPeriodTxt">${selectTxt }</em></span> --%>
<!-- 				</p> -->
<!-- 			</div> -->
<!-- 			<p class="pay_txt_info">단, 선택일이 주말, 공휴일인 경우 상품은 다음날 배송됩니다.</p> -->
			</form>
			</c:forEach>
								
			<div class="payDetailWrap">
				<div class="control_box total_price">
					<h3 class="sub_tit1">총 결제금액</h3>
					<strong class="price"><fmt:formatNumber value="${totalRegularDeliveryPrice + totalDeliveryFee }" pattern="#,###"/></em>원</strong>
<!-- 					<a href="javascript:void(0);" class="btn_box_control">자세히</a>					 -->
				</div>		
				<div>
					<div class="payDetail">
						<ul class="payDetail_01">
							<li>
								<div class="left">상품금액</div>
								<div class="right"><fmt:formatNumber value="${totalRegularDeliveryPrice }" pattern="#,###"/>원</div>
							</li>
							<li>
								<div class="left">배송비</div>
								<div class="right"><fmt:formatNumber value="${totalDeliveryFee }" pattern="#,###"/>원</div>
							</li>
<!-- 							<li class="sale"> -->
<!-- 								<div class="left">총 할인금액</div> -->
<%-- 								<div class="right"><fmt:formatNumber value="${totalSalePrice - totalRegularDeliveryPrice }" pattern="#,###"/>원</div> --%>
<!-- 							</li> -->
						</ul>
	<!-- 					<ul class="payDetail_02"> -->
	<!-- 						<li> -->
	<!-- 							<div class="left">상품할인쿠폰</div> -->
	<!-- 							<div class="right">- 5,000원</div> -->
	<!-- 						</li> -->
	<!-- 						<li> -->
	<!-- 							<div class="left">선물포장무료쿠폰</div> -->
	<!-- 							<div class="right">- 1,000원</div> -->
	<!-- 						</li> -->
	<!-- 						<li> -->
	<!-- 							<div class="left">매일 포인트</div> -->
	<!-- 							<div class="right">- 1,000P</div> -->
	<!-- 						</li> -->
	<!-- 						<li> -->
	<!-- 							<div class="left">예치금</div> -->
	<!-- 							<div class="right">- 7,000원</div> -->
	<!-- 						</li> -->
	<!-- 						<li>
	<!-- 							<div class="left">모바일상품권</div> -->
	<!-- 							<div class="right">- 10,000원</div> -->
	<!-- 						</li> -->
	<!-- 					</ul> -->
						<dl class="payTotal">
							<dt>총 결제예정금액</dt>
							<dd><em><fmt:formatNumber value="${totalRegularDeliveryPrice + totalDeliveryFee }" pattern="#,###"/></em>원</dd>
							<dd class="point">적립예정 매일포인트 : <fmt:formatNumber value="${totalPointsave }" pattern="#,###"/>P</dd>
						</dl>
					</div>		
				</div>				
			</div>
			<div class="order_form sale_info">
				<div class="control_box">
					<h3 class="sub_tit1">자동결제정보등록</h3>
				</div>
				<div class="relBox auto_registration">
					<c:if test="${memberInfo.regularPaymentBusinessCd eq null ||  memberInfo.regularPaymentBusinessCd eq ''}">
						<span id="regularPaymentBusinessNmTxt2">정기배송에 사용하실 결제 정보를 등록해 주세요.<br/></span>
						<span id="regularPaymentBusinessNmTxt3">신용카드만 등록이 가능합니다.</span>
					</c:if>
					<p class="card_info_ch"><span id="regularPaymentBusinessNmTxt">${memberInfo.regularPaymentBusinessNm }</span>
					<a href="#none" onclick="javascript:auth_LG()" class="btn_sStyle3" id="btnTxt_pay">
					<c:choose>
					<c:when test="${memberInfo.regularPaymentBusinessCd != null &&  memberInfo.regularPaymentBusinessCd != ''}">
					변경
					</c:when>
					<c:otherwise>
					카드등록
					</c:otherwise>
					</c:choose>
					</a></p>
<!-- 					<div class="rw_tbBox"> -->
<!-- 						<ul class="rw_tb_tbody2"> -->
<!-- 							<li> -->
<!-- 								<div class="tr_box"> -->
<!-- 									<div class="col1"> -->
<!-- 										<span class="group_block">카드사</span> -->
<!-- 									</div> -->
<!-- 									<div class="col2"> -->
<!-- 										<div class="group_block"> -->
<!-- 											<div class="select_box1"> -->
<!-- 												<label>카드종류</label> -->
<!-- 												<select> -->
<!-- 													<option>카드선택</option> -->
<!-- 													<option>신한</option> -->
<!-- 													<option>비씨</option> -->
<!-- 													<option>KB국민</option> -->
<!-- 													<option>현대</option> -->
<!-- 													<option>삼성</option> -->
<!-- 													<option>롯데</option> -->
<!-- 													<option>하나(외환)</option> -->
<!-- 													<option>NH채움</option> -->
<!-- 													<option>하나</option> -->
<!-- 													<option>우리</option> -->
<!-- 													<option>수협</option> -->
<!-- 													<option>씨티</option> -->
<!-- 													<option>광주</option> -->
<!-- 													<option>전북</option> -->
<!-- 													<option>제주</option> -->
<!-- 													<option>신협체크</option> -->
<!-- 													<option>우체국카드</option> -->
<!-- 													<option>MG새마을체크</option> -->
<!-- 													<option>저축은행체크</option> -->
<!-- 													<option>KDB산업은행</option> -->
<!-- 												</select> -->
<!-- 											</div>  -->
<!-- 											<a href="#none" onclick="javascript:auth_LG()" class="btn_sStyle4 sGray2">카드등록</a> -->
<!-- 										</div> -->
<!-- 									</div> -->
<!-- 								</div> -->
<!-- 							</li> -->
<!-- 						</ul> -->
<!-- 					</div> -->
<!-- 					<p class="pay_txt_info">정기배송 자동결제는 신용카드만 등록 가능합니다.</p> -->
									
				</div>
			</div>
			<!-- ### 결제수단 ### -->
			<div class="personalBox2">
				<dl>
					<dt class="control_box">
						<label class="chk_style1">
							<em>
								<input type="checkbox" name="agreeCheck" value="정기배송 서비스" />
							</em>
							<span>정기배송 서비스 동의</span>
						</label>
						<a href="javascript:void(0);" class="btn_box_control">자세히</a>
					</dt>
					<dd>
						<p class="total_agree">본인은 위의 내용을 모두 읽어보았으며 이에 전체 동의합니다. </p>
						<ul class="agree_list">
							<li>전자금융거래 이용약관 <a href="#none" class="btn_sStyle3" onclick="javascript:clauseInfo('5')">자세히 보기</a></li>
							<li>개인정보 수집 및 이용 <a href="#none" class="btn_sStyle3" onclick="javascript:clauseInfo('1')">자세히 보기</a></li>
							<li>개인정보수집 및 위탁동의 <a href="#none" class="btn_sStyle3" onclick="javascript:clauseInfo('2')">자세히 보기</a></li>									
							<li>자동승인(정기결제) 이용약관 <a href="#none" class="btn_sStyle3" onclick="javascript:clauseInfo('4')">자세히 보기</a></li>							
						</ul>
					</dd>
					<dt class="control_box">
						<label class="chk_style1">
							<em>
								<input type="checkbox" name="agreeCheck" value="주문 내역" />
							</em>
							<span>주문 내역 동의</span>
						</label>
						<a href="javascript:void(0);" class="btn_box_control">자세히</a>
					</dt>
					<dd class="total_agree">
						<p>주문할 상품의 상품명, 상품가격, 배송정보를 확인하였습니다. (전자상거래법 제 8조 2항)</p>
						<strong>구매에 동의하시겠습니까?</strong>
					</dd>
				</dl>
			</div>
			<div class="btn_wrapC btn1ea">
				<a href="#none" onclick="javascript:reg()" class="btn_mStyle1 sPurple1"><span>정기배송 신청</span></a>
			</div>
			<ul class="notice">
				<li>정기배송은 신청시점과 결제시점의 상품가격이 다를 수 있으며, 자동결제시점의 판매금액으로 결제가 진행됩니다.</li>
				<li>카드한도 초과 등 자동결제 실패시 오후 7시까지 재결제가 진행 되지 않을 경우 해당 회차 주문은 자동취소, 배송되지 않습니다.</li>
			</ul>
		</div>
	</div>
	
	<jsp:include page="/WEB-INF/views/oms/order/inner/orderLayerInner.jsp" flush="false"/>