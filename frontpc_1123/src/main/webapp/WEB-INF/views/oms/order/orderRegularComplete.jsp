<%--
	화면명 : 주문완료
	작성자 : dennis
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<script type="text/javascript">

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
					<li><span class="step_02">02</span>정기배송신청</li>
					<li class="active"><span class="step_03">03</span>신청완료</li>
				</ol>
			</div>
			<!-- //160926 수정 -->
			<div class="completeWrap">
				<strong class="title">정기배송 신청이 정상적으로 완료되었습니다.</strong>
	
				<ul class="completeInfo">
					<li class="first">
						<strong>신청번호</strong>${omsRegulardelivery.regularDeliveryId }
					</li>
					<li>
						<strong>신청일시</strong>${omsRegulardelivery.insDt }
					</li>
				</ul>
			</div>
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
					<c:forEach items="${omsRegulardelivery.omsRegulardeliveryproducts }" var="rp">
					<li >
						<div class="tr_box">
							<div class="col1">
								<div class="positionR">
									<div class="prod_img">
										<a href="#none" onclick="javascript:ccs.link.product.detail('${rp.productId}')">
											<tags:prdImgTag productId="${rp.productId}" size="90" alt="${rp.pmsProduct.name }"/>
										</a>
									</div>
									<a href="#none" class="title" onclick="javascript:ccs.link.product.detail('${rp.productId}')">
										[${rp.pmsProduct.brandName }] ${rp.pmsProduct.name }
									</a>
									<c:choose>
									<c:when test="${rp.deliveryProductTypeCd == 'DELIVERY_PRODUCT_TYPE_CD.SET' }">
										<c:forEach items="${rp.omsRegulardeliveryproducts }" var="rpp">
											<em class="option_txt">
												<i>${rpp.pmsProduct.name } : ${rpp.pmsSaleproduct.name }</i>
											</em>	
										</c:forEach>
									</c:when>
									<c:otherwise>
									<em class="option_txt">
										<i>옵션 : ${rp.pmsSaleproduct.name }</i>
									</em>
									</c:otherwise>
									</c:choose>
									<strong class="itemPrice"><fmt:formatNumber value="${rp.regularDeliveryPrice }" pattern="#,###"/>원</strong>
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
									${rp.orderQty }개
								</div>
							</div>
	
							<div class="col3">
								<c:set var="deliveryFee" value="0"/>
								<c:choose>
								<c:when test="${rp.deliveryFeeFreeYn == 'Y' }">
									<c:set var="deliveryFee" value="0"/>
								</c:when>
								<c:otherwise>
									<c:if test="${rp.pmsProduct.ccsDeliverypolicy.minDeliveryFreeAmt >= (rp.regularDeliveryPrice * rp.orderQty)  }">
										<c:set var="deliveryFee" value="${rp.pmsProduct.ccsDeliverypolicy.deliveryFee }"/>	
									</c:if>
								</c:otherwise>
								</c:choose>
								
								<span class="price">
									<em>
									<c:choose>
									<c:when test="${deliveryFee == 0}">
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
									<em><fmt:formatNumber value="${rp.regularDeliveryPrice * rp.orderQty }" pattern="#,###"/> <i>원</i></em>
								</span>
							</div>
							<div class="tbl_txt_info">
								<dl>
									<dt>배송주기/횟수 :</dt>
									<dd>${ rp.deliveryPeriodName } /  총${rp.deliveryCnt }회</dd>
									<dt>정기배송요일 :</dt>
									<dd>
										<c:if test="${rp.deliveryPeriodValue == 3 }">화요일</c:if>
										<c:if test="${rp.deliveryPeriodValue == 4 }">수요일</c:if>
										<c:if test="${rp.deliveryPeriodValue == 5 }">목요일</c:if>
										<c:if test="${rp.deliveryPeriodValue == 6 }">금요일</c:if>
										<c:if test="${rp.deliveryPeriodValue == 7 }">토요일</c:if>
										<c:if test="${rp.deliveryPeriodValue == 1 }">일요일</c:if>
									</dd>
								</dl>
							</div>
						</div>
					</li>
					</c:forEach>					
				</ul>
				<!-- ### //테이블 바디 ### -->
			</div>
			<!-- ### 배송정보 ### -->
			<div class="relBox">
				<div class="rw_tbBox">
					<ul class="rw_tb_tbody2 thin">
						<li>
							<div class="tr_box">
								<div class="col1">
									<div class="cell">
										<span class="group_inline">배송정보</span>
									</div>
								</div>
								<div class="col2">
									<div class="group_block">
										<dl class="delivery_user_info">
											<dt>${omsRegulardelivery.deliveryName1 }</dt>
											<dd>${omsRegulardelivery.deliveryPhone2 }
<%-- 											<span class="email">${omsRegulardelivery.deliveryEmail }</span> --%>
											</dd>
											<dd>(${omsRegulardelivery.deliveryZipCd }) ${omsRegulardelivery.deliveryAddress1 } ${omsRegulardelivery.deliveryAddress2 }</dd>
										</dl>
									</div>
								</div>
							</div>
						</li>
					</ul>
				</div>
			</div>
			<!-- ### //배송정보 ### -->
	
			<!-- ### 결제정보 ### -->
			<div class="relBox">
				<div class="rw_tbBox">
					<ul class="rw_tb_tbody2 thin">
						<li>
							<div class="tr_box">
								<div class="col1">
									<div class="cell">
										<span class="group_inline">결제정보</span>
									</div>
								</div>
								<div class="col2">
									<div class="group_block">
										<ul class="payListInfo">
											<li><strong>${omsRegulardelivery.mmsMemberZts.regularPaymentBusinessNm }</strong></li>
										</ul>
									</div>
								</div>
							</div>
						</li>
					</ul>
				</div>
			</div>
			<!-- ### //결제정보 ### -->
	
				<div class="btn_wrapC btn2ea">
					<a href="#none" onclick="javascript:ccs.link.common.main()" class="btn_bStyle1 sWhite1">계속 쇼핑하기</a>
					<a href="/mms/mypage/regular/change/${omsRegulardelivery.regularDeliveryId}" class="btn_bStyle1 sPurple1">정기배송 신청내역</a>
<!-- 					<a href="/mms/mypage/regular/history" class="btn_bStyle1 sPurple1">정기배송 신청내역</a> -->
				</div>
			</div>
		</div>
