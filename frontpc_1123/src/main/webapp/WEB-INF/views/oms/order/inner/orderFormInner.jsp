<%--
	화면명 : 주문서 form
	작성자 : dennis
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags" %>
<!-- 배송메시지 -->
<form id="deliveryMessage">
<tags:codeList code="DELIVERY_MESSAGE_CD" var="deliveryMessageList" tagYn="N"/>
<c:forEach items="${deliveryMessageList }" var="dcd">
<input type="hidden" name="deliveryMessageValue" value="${dcd.cd }" codeName="${dcd.name }"/>
</c:forEach>
</form>
<!-- phone email head -->
<form id="phone1Head">
<tags:codeList code="PHONE_NUMBER_CD" var="phone1HeadList" tagYn="N"/>
<input type="hidden" name="phoneHeadValue" value="" codeName="없음"/>
<c:forEach items="${phone1HeadList }" var="ph1Code">
<input type="hidden" name="phoneHeadValue" value="${ph1Code.cd }" codeName="${ph1Code.name }"/>
</c:forEach>
</form>
<form id="phone2Head">
<tags:codeList code="MOBILE_NUMBER_CD" var="phone2HeadList" tagYn="N"/>
<c:forEach items="${phone2HeadList }" var="ph2Code">
<input type="hidden" name="phoneHeadValue" value="${ph2Code.cd }" codeName="${ph2Code.name }"/>
</c:forEach>
</form>
<form id="emailDomain">
<tags:codeList code="EMAIL_DOMAIN_CD" var="emailDomainList" tagYn="N"/>
<input type="hidden" name="emailDomainValue" value="" codeName="직접입력"/>
<c:forEach items="${emailDomainList }" var="emailCode">
<input type="hidden" name="emailDomainValue" value="${emailCode.cd }" codeName="${emailCode.name }"/>
</c:forEach>
</form>
<!-- phone email head -->
<form action="/oms/order/complete" name="orderCompleteForm" id="orderCompleteForm" method="post">
	<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }"/>
	<input type="hidden" name="orderId" id="orderId" value=""/>
</form>
<form action="/api/oms/order/save" name="mergeForm" id="mergeForm">
</form>


<form name="saveOrderForm" id="saveOrderForm" method="post">	
<!-- 	//상품사은품 "PRESENT_TYPE_CD.PRODUCT":orderProductNo:presentId:productIds -->
<!-- 	//주문사은품 "PRESENT_TYPE_CD.ORDER":presentId:productIds -->
<!-- 	//주문사은품 배송지 선택 "DELIVERY_INFO":presentId:productId:deliveryAddressNo:deliveryPolicyNo" -->
	<input type="hidden" name="cartProductNos" value="${omsOrder.cartProductNos }"/>	
	<input type="hidden" name="selectPresent" id="selectPresent" value="${omsOrder.selectPresent }"/>	
	<input type="hidden" name="selectCoupon" value=""/>
	<input type="hidden" name="orderStat" value="ORDERSHEET"/>
	
	<input type="hidden" name="memberYn" id="memberYn" value="${memberYn }"/>
	<input type="hidden" name="orderPwd" id="orderPwd" value=""/>
	
	<input type="hidden" name="giftYn" value="${omsOrder.giftYn }"/>	
	<input type="hidden" name="orderTypeCd" id="orderTypeCd" value="${omsOrder.orderTypeCd }"/>
	<input type="hidden" name="orderAmt" id="orderAmt" value=""/>
	<input type="hidden" name="dcAmt" id="dcAmt" value=""/>
	<input type="hidden" name="paymentAmt" id="paymentAmt" value=""/>
	
	<input type="hidden" name="channelId" id="channelId" value="${omsOrder.channelId}"/>
	
	<!-- 주문자정보 -->
	<input type="hidden" name="name1" id="name1" value=""/>
	<input type="hidden" name="phone1" id="phone1" value=""/>
	<input type="hidden" name="phone2" id="phone2" value=""/>
	<input type="hidden" name="email" id="email" value=""/>
	<input type="hidden" name="zipCd" id="zipCd" value=""/>
	<input type="hidden" name="address1" id="address1" value=""/>
	<input type="hidden" name="address2" id="address2" value=""/>
	<input type="hidden" name="giftPhone" id="giftPhone" value=""/>
	<!-- 기프트콘 -->
	<input type="hidden" name="giftName" id="giftName" value=""/>
	<input type="hidden" name="giftImgTypeCd" id="giftImgTypeCd" value=""/>
	<input type="hidden" name="giftMsg" id="giftMsg" value=""/>	
		
	<!-- 금액 정보 -->
	<input type="hidden" name="orgTotalSalePrice" id="orgTotalSalePrice" value="0"/>
	<input type="hidden" name="totalOrderSalePrice" id="totalOrderSalePrice" value="${omsOrder.totalOrderSalePrice }"/>
	<input type="hidden" name="totalDcAmt" id="totalDcAmt" value="0"/>
	<input type="hidden" name="totalDeliveryFee" id="totalDeliveryFee" value="0"/>
	<input type="hidden" name="totalWrapFee" id="totalWrapFee" value="0"/>
	<input type="hidden" name="productCouponAmt" id="productCouponAmt" value="0"/>
	<input type="hidden" name="plusCouponAmt" id="plusCouponAmt" value="0"/>
	<input type="hidden" name="wrapCouponAmt" id="wrapCouponAmt" value="0"/>
	<input type="hidden" name="orderCouponAmt" id="orderCouponAmt" value="0"/>
	<input type="hidden" name="deliveryCouponAmt" id="deliveryCouponAmt" value="0"/>
	<input type="hidden" name="pointAmt" id="pointAmt" value="0"/>
	<input type="hidden" name="depositAmt" id="depositAmt" value="0"/>
	<input type="hidden" name="giftAmt" id="giftAmt" value="0"/>
	<input type="hidden" name="totalOrderAmt" id="totalOrderAmt" value="0"/>

	<!-- 총 적립예정포인트 -->
	<input type="hidden" name="totalPointsave" id="totalPointsave" value="0"/>		
	
	<!-- okcashbag -->
	<input type="hidden" name="okcashbagNo" id="okcashbagNo" value=""/>
					
</form>

<!-- 상품정보 -->
<c:set var="productQtyCnt" value="0"/>
<c:set var="subTotalSalePrice" value="0"/>
<c:set var="subDeliveryFeeFreeYn" value="N"/>
<c:set var="totalWrapYn" value="false"/>
<c:set var="totalDeliveryFee" value="0"/>
<c:forEach items="${omsOrder.omsOrderproducts }" var="os" varStatus="status">
	<c:if test="${os.overseasPurchaseYn == 'Y' }">		
		<c:set var="overseaYn" value="true"/>
	</c:if>
	<c:if test="${os.wrapYn == 'Y' }">
		<c:set var="wrapYn" value="true"/>
		<c:set var="totalWrapYn" value="true"/>
	</c:if>

	<form name="productForm" id="productForm${os.orderProductNo }">
		<input type="hidden" name="tempOrderProductNo" value="${os.orderProductNo }"/>
		<input type="hidden" name="productId" id="productId" value="${os.productId}"/>
		<input type="hidden" name="saleproductId" id="saleproductId" value="${os.saleproductId}"/>
		<input type="hidden" name="dealId" id="dealId" value="${os.dealId}"/>
		<input type="hidden" name="orderProductTypeCd" id="orderProductTypeCd" value="${os.orderProductTypeCd}"/>
		
		<input type="hidden" name="orderQty" id="orderQty" value="${os.orderQty}"/>
		<input type="hidden" name="overseasPurchaseYn" id="overseasPurchaseYn" value="${os.overseasPurchaseYn }"/>
		<input type="hidden" name="deliveryAddressNo" id="deliveryAddressNo" value="1"/>
		<input type="hidden" name="deliveryPolicyNo" id="deliveryPolicyNo" value="${os.deliveryPolicyNo }"/>
		<input type="hidden" name="deliveryFee" id="deliveryFee" value="${os.ccsDeliverypolicy.deliveryFee }"/>
		<input type="hidden" name="deliveryName" id="deliveryName" value="${os.ccsDeliverypolicy.name }"/>
		<input type="hidden" name="deliveryServiceCd" id="deliveryServiceCd" value="${os.ccsDeliverypolicy.deliveryServiceCd }"/>
		<input type="hidden" name="minDeliveryFreeAmt" id="minDeliveryFreeAmt" value="${os.ccsDeliverypolicy.minDeliveryFreeAmt }"/>
		<input type="hidden" name="wrapYn" id="wrapYn" value="${os.wrapYn }"/>
		<input type="hidden" name="wrapVolume" id="wrapVolume" value="${os.wrapVolume }"/>
		<input type="hidden" name="boxDeliveryYn" id="boxDeliveryYn" value="${os.boxDeliveryYn }"/>
		<input type="hidden" name="boxUnitCd" id="boxUnitCd" value="${os.boxUnitCd }"/>
		<input type="hidden" name="boxUnitQty" id="boxUnitQty" value="${os.boxUnitQty }"/>
		<input type="hidden" id="productName" value="${os.productName }" />
		<input type="hidden" name="orgTotalSalePrice" id="orgTotalSalePrice" value="${os.orgTotalSalePrice }" />
		<input type="hidden" name="salePrice" id="salePrice" value="${os.salePrice }" />
		<input type="hidden" name="addSalePrice" id="addSalePrice" value="${os.addSalePrice }" />
		<input type="hidden" name="deliveryFeeFreeYn" id="deliveryFeeFreeYn" value="${os.deliveryFeeFreeYn }" />
		
		<input type="hidden" id="pointSaveRate" value="${os.pointSaveRate }"/>
		<input type="hidden" id="productPoint" value="${os.productPoint }"/>
		<!-- 포인트 프로모션 정보 -->
		<input type="hidden" id="pointSaveId" value="${os.spsPointsave.pointSaveId }"/>
		<input type="hidden" id="pointValue" value="${os.spsPointsave.pointValue }"/>
		<input type="hidden" id="pointSaveTypeCd" value="${os.spsPointsave.pointSaveTypeCd }"/>		
		<!-- 포인트 프로모션 정보 -->
		
		<input type="hidden" name="offshopId" id="offshopId" value="${os.offshopId }" />
		<input type="hidden" name="styleNo" id="styleNo" value="${os.styleNo }" />
	
		<c:set var="productQtyCnt" value="${productQtyCnt + os.orderQty}"/>												
	
		<c:set var="subTotalSalePrice" value="${subTotalSalePrice + (os.salePrice + os.addSalePrice) * os.orderQty }"/>
	
		<c:if test="${os.deliveryFeeFreeYn  == 'Y' }">
			<c:set var="subDeliveryFeeFreeYn" value="Y"/>
		</c:if>
					
		<c:set var="orderDeliveryFee" value="0"/>
		<c:if test="${status.last || os.deliveryPolicyNo != omsOrder.omsOrderproducts[status.index+1].deliveryPolicyNo }">
			<input type="hidden" name="sumDeliveryPolicyNo" value="${os.deliveryPolicyNo }"/>
			<c:choose>
				<c:when test="${(os.ccsDeliverypolicy.minDeliveryFreeAmt != null && os.ccsDeliverypolicy.minDeliveryFreeAmt <= subTotalSalePrice) || subDeliveryFeeFreeYn == 'Y' }">
					<c:set var="orderDeliveryFee" value="0"/>
				</c:when>
				<c:otherwise>
					<c:set var="orderDeliveryFee" value="${os.ccsDeliverypolicy.deliveryFee }"/>
				</c:otherwise>
			</c:choose>
			<input type="hidden" name="orderDeliveryFee" id="orderDeliveryFee${os.deliveryPolicyNo }" value="${orderDeliveryFee }"/>
			<c:set var="subTotalSalePrice" value="0"/>
			<c:set var="subDeliveryFeeFreeYn" value="N"/>
			<c:set var="totalDeliveryFee" value="${totalDeliveryFee + orderDeliveryFee }"/>
		</c:if>
								
		<c:choose>
		<c:when test="${os.orderProductTypeCd == 'ORDER_PRODUCT_TYPE_CD.SET' }">			
			<c:forEach items="${os.omsOrderproducts }" var="oss" varStatus="st">
				<input type="hidden" name="omsOrderproducts[${st.index }].productId" id="productId" value="${oss.productId }"/>
				<input type="hidden" name="omsOrderproducts[${st.index }].saleproductId" id="saleproductId" value="${oss.saleproductId }"/>
				<input type="hidden" name="saleproductName" value="${oss.productName } : ${oss.saleproductName }" />										
			</c:forEach>
		</c:when>
		<c:otherwise>
			<input type="hidden" name="saleproductName" value="${os.saleproductName }" />									
		</c:otherwise>
		</c:choose>
		<c:set var="presentNames" value=""/>
		<c:if test="${fn:length(os.spsPresents) > 0 }">
			<c:forEach items="${os.spsPresents }" var="osp">
				<c:forEach items="${osp.spsPresentproducts }" var="ops" varStatus="subIdx">
					<c:set var="presentNames" value="${presentNames }, ${ops.pmsProduct.name }"/>
					<input type="hidden" name="presentNames" value="${ops.pmsProduct.name }"/>		
				</c:forEach>							
			</c:forEach>
		</c:if>
<%-- 		<input type="hidden" id="presentNames" value="${fn:substring(presentNames,fn:indexOf(presentNames,",")+1, fn:length(presentNames)) }"/> --%>
		<tags:prdImg productId="${os.productId}" size="90" var="productImgUrl"/>
		<input type="hidden" id="productImg" value="${productImgUrl }"/>
	</form>
</c:forEach>
<input type="hidden" id="overseaYn" value="${overseaYn }"/>																							
<input type="hidden" id="deliveryCnt" value="${deliveryCnt }"/>

<c:forEach items="${memberAddressList }" var="ma" varStatus="status">
<c:choose>
<c:when test="${ma.addressNo == memberInfo.addressNo }">
	<form id="defaultMemberAddress">
	<input type="hidden" id="addressNo" value="${ma.addressNo }"/>
	<input type="hidden" id="name" value="${ma.name }"/>
	<input type="hidden" id="deliveryName1" value="${ma.deliveryName1 }"/>
	<input type="hidden" id="countryNo" value="${ma.countryNo }"/>
	<input type="hidden" id="phone1" value="${ma.phone1 }"/>
	<input type="hidden" id="phone2" value="${ma.phone2 }"/>
	<input type="hidden" id="zipCd" value="${ma.zipCd }"/>
	<input type="hidden" id="address1" value="${ma.address1 }"/>
	<input type="hidden" id="address2" value="${ma.address2 }"/>
	</form>
</c:when>
<c:otherwise>
	<form name="memberAddress" id="memberAddress${ma.addressNo }">
		<input type="hidden" id="defaultYn" value="<c:if test="${ma.addressNo == memberInfo.addressNo }">Y</c:if> "/>
		<input type="hidden" id="addressNo" value="${ma.addressNo }"/>
		<input type="hidden" id="name" value="${ma.name }"/>
		<input type="hidden" id="deliveryName1" value="${ma.deliveryName1 }"/>
		<input type="hidden" id="countryNo" value="${ma.countryNo }"/>
		<input type="hidden" id="phone1" value="${ma.phone1 }"/>
		<input type="hidden" id="phone2" value="${ma.phone2 }"/>
		<input type="hidden" id="zipCd" value="${ma.zipCd }"/>
		<input type="hidden" id="address1" value="${ma.address1 }"/>
		<input type="hidden" id="address2" value="${ma.address2 }"/>
	</form>
</c:otherwise>
</c:choose>
</c:forEach>
<input type="hidden" id="couponTotalDeliveryFee" value="${totalDeliveryFee }"/>
<input type="hidden" id="couponTotalWrapYn" value="${totalWrapYn }"/>

