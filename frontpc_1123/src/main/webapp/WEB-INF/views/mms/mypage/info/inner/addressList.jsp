<%--
	화면명 : 마이페이지 > MY 정보관리 > 배송지관리
	작성자 : ALLEN
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>
<script type="text/javascript">
	$("[name=TOTAL_CNT]").val("${totalCount}");
</script>
<form name="addressForm">


</form>

<ul class="div_tb_tbody2">
	<c:if test="${not empty addressList}">
		<c:forEach items="${addressList}" var="address" varStatus="i">
			<li id="${address.addressNo}">
				<div class="tr_box">
						<div class="col1">
							<c:if test="${address.defaultAddrNo ne address.addressNo}">
								<label class="radio_style1">
									<em>
											<input type="radio" name="radAddr" value="">
									</em>
								</label>
							</c:if>
						</div>
					<div class="col2">
						<span>${address.deliveryName1}</span>
						<c:if test="${address.defaultAddrNo eq address.addressNo}">
							<span class="default"><i>[</i>기본 배송지<i>]</i></span>
						</c:if>
					</div>
					<div class="col3">
						<span class="address">
							<i>주소</i>
							<em>(${address.zipCd})</em>
							<em>${address.address1} ${address.address2}</em>
						</span>
					</div>
					<div class="col4">
						<span class="tel">
							<i>연락처</i>
							<em id="phone2_em_${i.index}"><script>ccs.common.phone_format("phone2_em_${i.index}", "${address.phone2}")</script></em>
							<em id="phone1_em_${i.index}"><script>ccs.common.phone_format("phone1_em_${i.index}", "${address.phone1}")</script></em>
						</span>
					</div>
					<div class="col6">
						<a href="javascript:mypage.deliveryAddress.modifyAddress(${address.addressNo})" class="btn_sStyle1 sWhite2">수정</a>
						<c:if test="${address.defaultAddrNo ne address.addressNo}">
							<a href="javascript:mypage.deliveryAddress.deleteAddress(${address.addressNo})" class="btn_sStyle1 sWhite2 btnChange">삭제</a>
						</c:if>
					</div>
				</div>
			</li>
		</c:forEach>
		<c:forEach items="${addressList}" var="address" varStatus="status">
			<input type="hidden" name="addressNo_${address.addressNo}" value="${address.addressNo}" />
			<input type="hidden" name="deliveryName1_${address.addressNo}" value="${address.deliveryName1}" />
			<input type="hidden" name="phone1_${address.addressNo}" value="${address.phone1}" />
			<input type="hidden" name="phone2_${address.addressNo}" value="${address.phone2}" />
			<input type="hidden" name="zipCd_${address.addressNo}" value="${address.zipCd}" />
			<input type="hidden" name="address1_${address.addressNo}" value="${address.address1}" />
			<input type="hidden" name="address2_${address.addressNo}" value="${address.address2}" />
		</c:forEach>
	</c:if>
</ul>
<c:if test="${empty addressList}">
	<p class="empty">등록된 배송지 정보가 없습니다.</p>
</c:if>
<div class="paginateType1">
	<page:paging formId="addressForm" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
			total="${totalCount}" url="/mms/mypage/deliveryAddress/list/ajax" type="ajax" callback="mypage.deliveryAddress.listCallback"/>
</div>
	
		