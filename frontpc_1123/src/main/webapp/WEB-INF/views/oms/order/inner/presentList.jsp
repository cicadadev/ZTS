<%--
	화면명 : 사은품 선택
	작성자 : dennis
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags" %>
<!-- ### 팝업 - 사은품 선택 ### -->
<div class="pop_wrap ly_selectGift" style="display:block;" id="presentListDiv">

	<jsp:include page="/WEB-INF/views/oms/order/inner/presentListScriptInner.jsp" flush="false" />

	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">사은품 선택</h3>
		</div>
		<div class="pop_content">

			<div class="chkBox">
				<label class="chk_style1">
					<em>
						<input type="checkbox" id="chkAllNon_1" value="" onclick="javascript:chkNon('ALL','',this)"/>
					</em>
					<span>전체 사은품 선택하지 않기</span>
				</label>
			</div>
			
			<div class="selectGiftList">
			
			<c:set var="idx" value="0"/>
			<c:forEach items="${omsOrder.omsOrderproducts }" var="os">
			<c:if test="${fn:length(os.spsPresents) > 0}">
				<input type="hidden" id="productId${idx }" value="${os.productId }"/>
				<input type="hidden" id="saleproductId${idx }" value="${os.saleproductId }"/>
				<c:forEach items="${os.spsPresents }" var="osp">
					<c:if test="${ osp.presentSelectTypeCd == 'PRESENT_SELECT_TYPE_CD.SELECT'  }">
					<input type="hidden" name="productIdx" value="${idx }"/>					
					<input type="hidden" id="orderProductNo${idx }" value="${os.orderProductNo }"/>
					<input type="hidden" id="presentTypeCd${idx }" value="PRESENT_TYPE_CD.PRODUCT"/>
					<input type="hidden" id="presentSelectTypeCd${idx }" value="${osp.presentSelectTypeCd }"/>
					<input type="hidden" id="selectQty${idx }" value="${osp.selectQty }"/>
					<input type="hidden" id="name${idx }" value="${osp.name }"/>
					<input type="hidden" id="presentId${idx }" value="${osp.presentId }"/>
					<c:forEach items="${osp.spsPresentproducts }" var="ops" varStatus="subIdx">
<!-- 						<label class="chk_style1"> -->
<!-- 						<em> -->
						<input type="checkbox" id="chk${idx}${subIdx.index}" name="chk${idx }" value="${ops.productId }" style="display: none;"/>
<!-- 						</em> -->
<!-- 						</label> -->
<%-- 						${osp.name } --%>
					</c:forEach>				
											
					<c:set var="idx" value="${idx+1 }"/>
					</c:if>
				</c:forEach>							
			</c:if>
			</c:forEach>
			
			
			<c:set var="presentIds" value=""/>
			<c:forEach items="${omsOrder.omsOrderproducts }" var="os">
			
				<c:if test="${fn:length(os.spsPresents) > 0}">
				<c:forEach items="${os.spsPresents }" var="osp">
					<c:if test="${fn:indexOf(presentIds,osp.presentId) < 0 && osp.presentSelectTypeCd == 'PRESENT_SELECT_TYPE_CD.SELECT'}">
					<input type="hidden" id="subpresentSelectTypeCd${osp.presentId }" value="${osp.presentSelectTypeCd }"/>
					<input type="hidden" id="subselectQty${osp.presentId }" value="${osp.selectQty }"/>
					<input type="hidden" id="subname${osp.presentId }" value="${osp.name }"/>
					<dl>
						<dt>
							<p>${osp.name }</p>
							<c:choose>
							<c:when test="${isMobile }">
<!-- 							<div> -->
<%-- 								${osp.minOrderAmt }원 이상 구매한 모든 고객에게 사은품을 드립니다. --%>
<%-- 								<span>(${osp.startDt } ~ ${osp.endDt })</span> --%>
<!-- 							</div> -->
							</c:when>
							<c:otherwise>
							<div class="posR">
								<label class="chk_style1">
									<em>
										<input type="checkbox" id="chkNon_${osp.presentId }" value="" onclick="javascript:chkNon('${osp.presentId}','PR',this)"/>
									</em>
									<span>사은품 선택하지 않고 주문</span>
								</label>
							</div>
							</c:otherwise>
							</c:choose>
						</dt>
						<dd>
							<c:choose>
							<c:when test="${isMobile }">
							<div class="posR">
								<label class="chk_style1">
									<em>
										<input type="checkbox" id="chkNon_${osp.presentId }" value="" onclick="javascript:chkNon('${osp.presentId}','PR',this)"/>
									</em>
									<span>사은품 선택하지 않고 주문</span>
								</label>
							</div>
							</c:when>
							<c:otherwise>
<!-- 							<div> -->
<%-- 								${osp.minOrderAmt }원 이상 구매한 모든 고객에게 사은품을 드립니다. --%>
<%-- 								<span>(${osp.startDt } ~ ${osp.endDt })</span> --%>
<!-- 							</div> -->
							</c:otherwise>
							</c:choose>
							<ul>
								<c:forEach items="${osp.spsPresentproducts }" var="ops" varStatus="subIdx">
								<li>
									<label class="chk_style1">
										<em>
											<input type="checkbox" id="subchk${osp.presentId}${subIdx.index}" name="subchk${osp.presentId }" value="${ops.productId }" onclick="javascript:chgChkPr('${osp.presentId}','${subIdx.index}')" />
										</em>
										<span>
											<i>
												<tags:prdImgTag productId="${ops.productId}" size="90" alt="${ops.pmsProduct.name }"/>														
											</i>
											<strong>${ops.pmsProduct.name }</strong>
										</span>
									</label>
								</li>
								</c:forEach>							
							</ul>
						</dd>
					</dl>
					<c:set var="presentIds" value="${presentIds}:${osp.presentId }" />			
					</c:if>			
				</c:forEach>
				</c:if>
			</c:forEach>
			
			<c:forEach items="${omsOrder.spsPresents }" var="op">
			<c:if test="${ op.presentSelectTypeCd == 'PRESENT_SELECT_TYPE_CD.SELECT' }">
			<dl>
				<dt>
					<p>${op.name }</p>
					<c:choose>
					<c:when test="${isMobile }">
					<div>
						${op.minOrderAmt }원 이상 구매한 모든 고객에게 사은품을 드립니다.
						<span>(${op.startDt } ~ ${op.endDt })</span>
					</div>
					</c:when>
					<c:otherwise>
					<div class="posR">
						<label class="chk_style1">
							<em>
								<input type="checkbox" id="chkNon_${op.presentId }"  value="" onclick="javascript:chkNon('${op.presentId}','',this)"/>
							</em>
							<span>사은품 선택하지 않고 주문</span>
						</label>
					</div>
					</c:otherwise>
					</c:choose>
				</dt>
				<dd>
					<c:choose>
					<c:when test="${isMobile }">
					<div class="posR">
						<label class="chk_style1">
							<em>
								<input type="checkbox" id="chkNon_${op.presentId }"  value="" onclick="javascript:chkNon('${op.presentId}','',this)"/>
							</em>
							<span>사은품 선택하지 않고 주문</span>
						</label>
					</div>
					</c:when>
					<c:otherwise>
					<div>
						${op.minOrderAmt }원 이상 구매한 모든 고객에게 사은품을 드립니다.
						<span>(${op.startDt } ~ ${op.endDt })</span>
					</div>	
					</c:otherwise>
					</c:choose>
					<ul>
						<input type="hidden" id="presentTypeCd${idx }" value="PRESENT_TYPE_CD.ORDER"/>
						<input type="hidden" id="presentSelectTypeCd${idx }" value="${op.presentSelectTypeCd }"/>
						<input type="hidden" id="selectQty${idx }" value="${op.selectQty }"/>
						<input type="hidden" id="name${idx }" value="${op.name }"/>
						<input type="hidden" id="presentId${idx }" value="${op.presentId }"/>
						<c:forEach items="${op.spsPresentproducts }" var="ops" varStatus="subIdx">
						<li>
							<label class="chk_style1">
								<em>
									<input type="checkbox" id="chk${idx}${subIdx.index}" name="chk${idx }" value="${ops.productId }" onclick="javascript:chgChk('${idx}','${subIdx.index}')" />
								</em>
								<span>
									<i>
										<tags:prdImgTag productId="${ops.productId}" size="90" alt="${ops.pmsProduct.name }"/>
									</i>
									<strong>${ops.pmsProduct.name }</strong>
								</span>
							</label>
						</li>
						</c:forEach>
						<c:set var="idx" value="${idx+1 }"/>
					</ul>
				</dd>
			</dl>
			</c:if>
			</c:forEach>
			</div>
			
			
			<c:if test="${isMobile }">
			<ul class="notice">
				<li>사은품 선택하지 않고 주문/결제 시 사은품 추가 발송은 불가합니다.</li>
				<li>본 사은품은 상품 반품 시 함께 반송 처리하셔야 하며, 반품 시 사은품 누락 시 반품처리가 되지 않습니다.</li>
			</ul>
			</c:if>
			
			<div class="chkBox">
				<label class="chk_style1">
					<em>
						<input type="checkbox" id="chkAllNon_2" value=""  onclick="javascript:chkNon('ALL','',this)"/>
					</em>
					<span>전체 사은품 선택하지 않기</span>
				</label>
			</div>
			
			<div class="btn_wrapC btn2ea">
				<input type="hidden" id="totalCnt" value="${idx }"/>
				<a href="#none" class="btn_mStyle1 sWhite1" onclick="javascript:presentListClose()">취소</a>
				<a href="#none" class="btn_mStyle1 sPurple1" onclick="javascript:presentOrder()">선택완료</a>				
			</div>
			<c:if test="${!isMobile }">
			<ul class="notice">
				<li>사은품 선택하지 않고 주문/결제 시 사은품 추가 발송은 불가합니다.</li>
				<li>본 사은품은 상품 반품 시 함께 반송 처리하셔야 하며, 반품 시 사은품 누락 시 반품처리가 되지 않습니다.</li>
			</ul>
			</c:if>
			<form action="/oms/order/list" name="targetForm" id="targetForm" method="post">
				<input type="hidden" name="${_csrf.parameterName }" value="${_csrf.token }"/>
				<input type="hidden" name="cartProductNos" value="${omsOrder.cartProductNos }"/>	
				<input type="hidden" name="selectPresent" id="selectPresent" value=""/>
				<input type="hidden" name="orderStat" value="ORDERSHEET"/>
				<input type="hidden" name="orderLoginReturn" id="orderLoginReturn" value="${omsOrder.orderLoginReturn }"/>
			</form>
		</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>

</div>
<!-- ### //팝업 - 사은품 선택 ### -->