<%--
	화면명 : 우편번호 검색 리스트
	작성자 : ALLEN
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>

<ul class="div_tb_tbody2" id="addrlist">
<c:if test="${not empty addressList}">
	<c:forEach items="${addressList}" var="address" varStatus="i">
		<li>
			<div class="tr_box">
				<div class="col1">
					<span>${address.bsizonno}</span>
				</div>
				<div class="col2">
					<c:if test="${search.addrType eq 'jibun'}">
						<a href="javascript:ccs.layer.searchAddressLayer.clickAddrRow(${i.index})">
							${address.sido} ${address.gungu} ${address.dong} &nbsp; 
							 <c:if test="${address.bunji != 0}">
							 	${address.bunji} 
							 </c:if>
							 <c:if test="${address.ho != 0}">
							  - ${address.ho}
							 </c:if>
						</a>
					</c:if>
					<c:if test="${search.addrType eq 'road'}">
						<a href="javascript:ccs.layer.searchAddressLayer.clickAddrRow(${i.index})">
							${address.sido} ${address.gungu} ${address.roadnm} ${address.buildmn} &nbsp;
							<c:if test="${address.buildnm != ''}">
								${address.buildnm}
							</c:if>
							<c:if test="${address.buildsn != 0}">
							  - ${address.buildsn}
							</c:if>
						</a>
					</c:if>
				</div>
			</div>
		</li>
		<input type="hidden" name="zipCd_${i.index}" value="${address.bsizonno}" />
		<input type="hidden" name="jibun_${i.index}" value="${address.sido} ${address.gungu} ${address.dong}" />
		<input type="hidden" name="bunji_${i.index}" value="${address.bunji}" />
		<input type="hidden" name="ho_${i.index}" value="${address.ho}" />
		<input type="hidden" name="road_${i.index}" value="${address.sido} ${address.gungu} ${address.roadnm}" />
		<input type="hidden" name="buildmn_${i.index}" value="${address.buildmn}" />
		<input type="hidden" name="buildnm_${i.index}" value="${address.buildnm}" />
		<input type="hidden" name="buildsn_${i.index}" value="${address.buildsn}" />
	</c:forEach>
</c:if>
</ul>
<c:if test="${empty addressList}">
	<p class="empty">검색 결과가 없습니다.</p>
</c:if>

<div class="paginateType1">
	<page:paging formId="addressForm" currentPage="${search.currentPage}" pageSize="${search.pageSize}" 
			total="${totalCount}" url="/ccs/common/searchAddress/list/ajax" type="ajax" callback="ccs.layer.searchAddressLayer.listCallback"/>
			<input type="hidden" name="addressTotalCnt" value="${totalCount}"/>
</div>
	
		