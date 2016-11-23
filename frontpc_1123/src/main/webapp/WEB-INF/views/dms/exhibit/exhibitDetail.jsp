<%--
	화면명 : 기획전 상세
	작성자 : ALLEN
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
    <link href="/resources/css/jquery.flipcountdown.css" rel="stylesheet" type="text/css" />
    <script type="text/javascript" src="/resources/js/jquery.flipcountdown.js" ></script>

<script type="text/javascript">
// 	exhibit.main.getExhibitList();

	$(document).ready(function() {
		
		if ("${ccsMessage.success}") {
			alert('${ccsMessage.resultMessage}');
			ccs.link.common.main();
		}
	
		var regiDate = '${exhibitInfo.endDt}';
		if (ccs.common.mobilecheck()) {
			$("#flipcountdownbox1").flipcountdown({
				beforeDateTime:regiDate.replace(/-/g,'/')
				, size:"sm"
			});
		} else {
			$("#flipcountdownbox1").flipcountdown({
				beforeDateTime:regiDate.replace(/-/g,'/')
			});
		}
	});
	
	$(window).load(function() {
		// PC일 경우
		if (!ccs.common.mobilecheck()) {
			var groupNo = '${search.groupNo}';
			if (groupNo != "") {
				// 구분 타이틀 초기화
				$(".miniTabBox1 > li").each(function() { $(this).find("a").removeClass("on") });
				$("#"+groupNo).find("a").addClass("on");
				window.scrollTo(0, $("#group_"+groupNo).offset().top);
			}
		}
	});
	
	
	
	function blockListEvent() {
		if (ccs.common.mobilecheck()) {
			$(".mobile .btnListType").off("click").on("click", function(){
				var listLength;
				var target_tag;
		
				if( $(this).closest(".tit_style3").length ){
					// 제목 태그에 감싸여 있을 경우
					listLength = $(this).closest(".tit_style3").nextUntil(".list_group").length;
					target_tag = $(this).closest(".tit_style3");
				}else{
					// 일반 형제태그로 있을 경우
					listLength = $(this).closest(".sortBoxList").next(".list_group").length;
					target_tag = $(this).closest(".sortBoxList");
				}
// 				$(this).toggleClass("block");
				if(listLength == 0){
					$(target_tag).next(".list_group").find(".product_type1").toggleClass("block");
				} else{
					$(target_tag).nextUntil(".list_group").next(".list_group").find(".product_type1").toggleClass("block");
				}
			});
		}
	}

	
	
</script>
	<c:choose>
		<c:when test="${isMobile}">
			<c:choose>
				<c:when test="${fn:length(exhibitInfo.name) > 10}">
					<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
						<jsp:param value="${fn:substring(exhibitInfo.name, 0, 10)}..." name="pageNavi"/>
					</jsp:include>	
				</c:when>
				<c:otherwise>
					<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
						<jsp:param value="${exhibitInfo.name}" name="pageNavi"/>
					</jsp:include>
				</c:otherwise>
			</c:choose>
		</c:when>
		<c:otherwise>
			<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
				<jsp:param value="${exhibitInfo.name}" name="pageNavi"/>
			</jsp:include>	
		</c:otherwise>
	</c:choose>
	<div class="inner">
		<div class="promotion">
			<c:if test="${exhibitInfo.exhibitTypeCd eq 'EXHIBIT_TYPE_CD.ONEDAY' }">
				<div class="promo_one_section">
					<span class="bg">원데이 기획전</span>
			</c:if>
			<div class="visual_img">
				<c:choose>
					<c:when test="${isMobile}">
						${exhibitInfo.html2}
					</c:when>
					<c:otherwise>
						${exhibitInfo.html1}
					</c:otherwise>
				</c:choose>
				<c:if test="${exhibitInfo.exhibitTypeCd eq 'EXHIBIT_TYPE_CD.TIMESALE' }">
					<dl class="saleTimer">
						<dt>타임세일 남은시간</dt>
						<dd>
							<em id="timesale">
								<div id="flipcountdownbox1"></div>
							</em>
						</dd>
					</dl>
				</c:if>
			</div>
			
			<!-- 원데이 대표 상품 -->
			<c:if test="${exhibitInfo.exhibitTypeCd eq 'EXHIBIT_TYPE_CD.ONEDAY' }">
				<div class="product_type1 prodType_3ea block">
					<ul>
						<c:forEach var="mainProduct" items="${mainProductList}" varStatus="i">
							<c:set var="product" value="${mainProduct.pmsProduct}" scope="request" />
							<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
								<jsp:param name="type" value="exhibit" />
								<jsp:param name="exhibitType" value="oneday" />
							</jsp:include>
						</c:forEach>
						<c:if test="${empty mainProductList}">
							대표상품이 없습니다.
						</c:if>
					</ul>
	            </div>
	           </div>
			</c:if>
			
			<!-- 쿠폰 기획전 - 쿠폰 정보-->
			<c:if test="${exhibitInfo.exhibitTypeCd eq 'EXHIBIT_TYPE_CD.COUPON' }">
				<div class="couponInfoList">
					<ul>
						<c:forEach var="exhibitCoupon" items="${couponList}" varStatus="">
							<li>
								<div class="couponName">
									<c:choose>
										<c:when test="${fn:length(couponList) > 1}">
<!-- 											<label class="chk_style1"> -->
<!-- 						                           <em> -->
<%-- 						                              <input type="checkbox" value="" id="${exhibitCoupon.spsCoupon.couponId}"> --%>
<!-- 						                           </em> -->
					                           <span>
													<c:choose>
														<c:when test="${fn:length(exhibitCoupon.name) > 100}">
															${fn:substring(exhibitCoupon.name, 0, 98)}...
														</c:when>
														<c:otherwise>
															${exhibitCoupon.name}
														</c:otherwise>
													</c:choose>
					                           </span>
<!-- 					                        </label> -->
										</c:when>
										<c:otherwise>
											<c:choose>
												<c:when test="${fn:length(exhibitCoupon.name) > 100}">
													${fn:substring(exhibitCoupon.name, 0, 98)}...
												</c:when>
												<c:otherwise>
													${exhibitCoupon.name}
												</c:otherwise>
											</c:choose>
										</c:otherwise>
									</c:choose>	
								</div>
		                        
	                        	<c:choose>
	                        		<c:when test="${exhibitCoupon.spsCoupon.couponTypeCd eq 'COUPON_TYPE_CD.PRODUCT' || exhibitCoupon.spsCoupon.couponTypeCd eq 'COUPON_TYPE_CD.PLUS'}">
										<div class="couponImg coupon1">
											<c:choose>
												<c:when test="${exhibitCoupon.spsCoupon.couponTypeCd eq 'COUPON_TYPE_CD.PRODUCT'}">
													<img src="/resources/img/pc/bg/coupon1.png" alt="coupon"> <!-- coupon -->	
												</c:when>
												<c:otherwise>
													<img src="/resources/img/pc/bg/coupon1_02.png" alt="coupon"> <!-- plus coupon -->
												</c:otherwise>
											</c:choose>
			                            	
			                            	<c:if test="${exhibitCoupon.spsCoupon.dcApplyTypeCd eq 'DC_APPLY_TYPE_CD.AMT'}">
			                            		<strong>
									   				<em><fmt:formatNumber value="${exhibitCoupon.spsCoupon.dcValue }" pattern="###,###" />원</em>
												</strong>
			                            	</c:if>
			                            	<c:if test="${exhibitCoupon.spsCoupon.dcApplyTypeCd eq 'DC_APPLY_TYPE_CD.RATE'}">
			                            		<strong>
									   				<em>${exhibitCoupon.spsCoupon.dcValue}%</em>
												</strong>
			                            	</c:if>
			                            </div>
	                        		</c:when>
	                        		<c:when test="${exhibitCoupon.spsCoupon.couponTypeCd eq 'COUPON_TYPE_CD.ORDER'}">
	                        			<div class="couponImg coupon3">
											<img src="/resources/img/pc/bg/coupon3.png" alt="coupon"> <!-- 장바구니 -->	
		                        			<c:if test="${exhibitCoupon.spsCoupon.dcApplyTypeCd eq 'DC_APPLY_TYPE_CD.AMT'}">
			                            		<strong>
									   				<em>${exhibitCoupon.spsCoupon.dcValue}원</em>
												</strong>
			                            	</c:if>
			                            	<c:if test="${exhibitCoupon.spsCoupon.dcApplyTypeCd eq 'DC_APPLY_TYPE_CD.RATE'}">
			                            		<strong>
									   				<em>${exhibitCoupon.spsCoupon.dcValue}%</em>
												</strong>
			                            	</c:if>
	                        			</div>
	                        		</c:when>
	                        		<c:when test="${exhibitCoupon.spsCoupon.couponTypeCd eq 'COUPON_TYPE_CD.DELIVERY' || exhibitCoupon.spsCoupon.couponTypeCd eq 'COUPON_TYPE_CD.WRAP'}">
	                        			<div class="couponImg coupon2">
	                        				<c:choose>
	                        					<c:when test="${exhibitCoupon.spsCoupon.couponTypeCd eq 'COUPON_TYPE_CD.DELIVERY'}">
				                        			<img src="/resources/img/pc/bg/coupon2.png" alt="coupon"> <!-- 검은색 -->
				                        			<strong>
											  			<em>무료배송</em>
													</strong>
	                        					</c:when>
	                        					<c:when test="${exhibitCoupon.spsCoupon.couponTypeCd eq 'COUPON_TYPE_CD.WRAP'}">
	                        						<img src="/resources/img/pc/bg/coupon2_03.png" alt="coupon"> <!-- 검은색 -->
				                        			<strong>
									   					<em>${exhibitCoupon.spsCoupon.dcValue}원</em>
													</strong>
	                        					</c:when>
	                        				</c:choose>
	                        			</div>
	                        		</c:when>
	                        	</c:choose>
	
		                        <div class="couponInfo">
		                           <div>
		                           		<c:if test="${not empty exhibitCoupon.spsCoupon.minOrderAmt}">
											<p>
												사용조건 : <fmt:formatNumber value="${exhibitCoupon.spsCoupon.minOrderAmt }" pattern="###,###" />원 이상 
												<c:if test="${exhibitCoupon.spsCoupon.maxDcAmt == 0 || exhibitCoupon.spsCoupon.maxDcAmt eq null}">
													할인
												</c:if>
												<c:if test="${exhibitCoupon.spsCoupon.maxDcAmt > 0}">
													최대 <fmt:formatNumber value="${exhibitCoupon.spsCoupon.maxDcAmt }" pattern="###,###" />원 할인
												</c:if>
											</p>
		                           		</c:if>
		                           		
		                           		<c:choose>
		                           			<c:when test="${exhibitCoupon.spsCoupon.termTypeCd eq 'TERM_TYPE_CD.DAYS'}">
		                           				<c:if test="${exhibitCoupon.spsCoupon.termDays ne '0'}">
		                           					<p>사용기간 : ~ 발급일로 부터 <fmt:formatNumber value="${exhibitCoupon.spsCoupon.termDays}" pattern="###,###" />일 </p>
		                           				</c:if>
		                           				<c:if test="${exhibitCoupon.spsCoupon.termDays eq '0'}">
		                           					<p>사용기간 : 당일 </p>
		                           				</c:if>
		                                		
		                           			</c:when>
		                           			<c:when test="${exhibitCoupon.spsCoupon.termTypeCd eq 'TERM_TYPE_CD.TERM'}">
		                           				<p>사용기간 : ~ ${exhibitCoupon.spsCoupon.termEndDt}</p>
		                           			</c:when>
		                           			<c:when test="${exhibitCoupon.spsCoupon.termTypeCd eq 'TERM_TYPE_CD.LASTDAY'}">
		                           				<p>사용기간 : ~ 발급일 기준 월말까지</p>
		                           			</c:when>
		                           			<c:when test="${exhibitCoupon.spsCoupon.termTypeCd eq 'TERM_TYPE_CD.WEEK'}">
		                           				<p>사용기간 : ~ 발급일 기준 일요일까지</p>
		                           			</c:when>
		                           		</c:choose>
		                           		
									</div>
									<c:if test="${exhibitCoupon.exceedYn eq 'Y'}">
										<a href="#none" class="btn_download" style="background-color:#E6E6FA"><em >선착순 마감</em></a>
									</c:if>
									<c:if test="${exhibitCoupon.exceedYn eq 'N'}">
		                            	<a href="javascript:exhibit.detail.couponDnLoginCheck('single', '${exhibitCoupon.spsCoupon.couponId}')" class="btn_download"><em>쿠폰<i>다운로드</i>받기</em></a>
									</c:if>
		                        </div>
	                     	</li>
						</c:forEach>
					</ul>
				</div>
               
<%--                <c:if test="${not empty couponList && fn:length(couponList) > 1}"> --%>
<!-- 	               <div class="btnCoupons"> -->
<!-- 	                  <a href="javascript:exhibit.detail.couponDnLoginCheck('all', '')" class="btn_downAll">쿠폰 한번에 다운받기</a> -->
<!-- 	                  <a href="javascript:exhibit.detail.couponDnLoginCheck('multi', '')" class="btn_downSelect">쿠폰 선택 다운받기</a> -->
<!-- 	               </div> -->
<%--                </c:if> --%>
				<c:choose>
					<c:when test="${isMobile}">
						<c:if test="${not empty exhibitInfo.subHtml2}">
							<div class="couponDsc">
								${exhibitInfo.subHtml2}
							</div>
						</c:if>
					</c:when>
					<c:otherwise>
							<c:if test="${not empty exhibitInfo.subHtml1}">
								<div class="couponDsc">
									${exhibitInfo.subHtml1}
								</div>
							</c:if>
					</c:otherwise>
				</c:choose>
			</c:if>
			
			<!-- 구분 타이틀 -->
			<c:if test="${exhibitInfo.exhibitTypeCd ne 'EXHIBIT_TYPE_CD.TIMESALE' }">
				<c:if test="${fn:length(exhibitInfo.dmsExhibitgroups) > 1}">
					<ul class="miniTabBox1">
						<c:forEach var="group" items="${exhibitInfo.dmsExhibitgroups}" varStatus="i">
							<li id="${group.groupNo}">
								<div>
									<c:choose>
										<c:when test="${i.first}">
											<a href="#group_${group.groupNo}" class="on">${group.name}</a>
										</c:when>
										<c:otherwise>
											<a href="#group_${group.groupNo}">${group.name}</a>
										</c:otherwise>
									</c:choose>
								</div>
							</li>
						</c:forEach>
					</ul>
				</c:if>
			</c:if>
			<div class="rw_displayListBox">
				<c:if test="${not empty exhibitInfo.dmsExhibitgroups}">
					<c:if test="${exhibitInfo.exhibitTypeCd ne 'EXHIBIT_TYPE_CD.TIMESALE'}">
							<div class="sortBoxList">
						<c:if test="${fn:length(exhibitInfo.dmsExhibitgroups) > 1}">
								<ul>
									<li>
										<div class="select_box1">
											<c:if test="${not empty search.groupNo}">
												<c:forEach var="group" items="${exhibitInfo.dmsExhibitgroups}" varStatus="i">
													<c:if test="${search.groupNo eq group.groupNo}">
														<label>${group.name}</label>
													</c:if>
												</c:forEach>
											</c:if>
											<c:if test="${empty search.groupNo}">
												<label>전체보기</label>
											</c:if>
											
											<select onchange="javascript:exhibit.detail.changeGubunTitle(this);">
												<option value="전체보기">전체보기</option>
												<c:forEach var="group" items="${exhibitInfo.dmsExhibitgroups}" varStatus="i">
													
													<c:choose>
														<c:when test="${search.groupNo eq group.groupNo}">
															<option value="${group.groupNo}" selected>${group.name}</a>
														</c:when>
														<c:otherwise>
															<option value="${group.groupNo}">${group.name}</a>
														</c:otherwise>
													</c:choose>
												</c:forEach>
											</select>
										</div>
									</li>
								   <li>
								      <button type="button" class="btnListType block">블록형 / 리스트형</button>
								   </li>
								</ul>
						</c:if>
							</div>
					</c:if>
				</c:if>
				
				<div id="exhibitProduct_div" class="list_group">
				<c:if test="${not empty exhibitInfo.dmsExhibitgroups}">
					<c:forEach var="group" items="${exhibitDetail.dmsExhibitgroups}" varStatus="i">
						<c:if test="${not empty group.groupNo}">
							<c:if test="${fn:length(exhibitInfo.dmsExhibitgroups) > 1}">
								<c:if test="${exhibitInfo.exhibitTypeCd ne 'EXHIBIT_TYPE_CD.TIMESALE'}">
										<c:if test="${group.groupTypeCd eq 'GROUP_TYPE_CD.TEXT'}">
											<h4 class="tit_style1 type1">
											<c:if test="${isMobile}">
												<c:choose>
													<c:when test="${group.url2 ne null && group.url2 ne ''}">
														<a id="group_${group.groupNo}" href="${group.url2}">${group.name}</a>
													</c:when>
													<c:otherwise>
														<a id="group_${group.groupNo}">${group.name}</a>
													</c:otherwise>
												</c:choose>
											</c:if>
											<c:if test="${!isMobile}">
												<c:choose>
													<c:when test="${group.url1 ne null && group.url1 ne ''}">
														<a id="group_${group.groupNo}" href="${group.url1}" target="_blank">${group.name}</a>
													</c:when>
													<c:otherwise>
														<a id="group_${group.groupNo}">${group.name}</a>
													</c:otherwise>
												</c:choose>
											</c:if>
										</c:if>
										<c:if test="${group.groupTypeCd eq 'GROUP_TYPE_CD.IMG'}">
											<h4 class="tit_style1">
											<c:if test="${isMobile}">
												<c:choose>
													<c:when test="${group.url2 ne null && group.url2 ne ''}">
														<a id="group_${group.groupNo}" href="${group.url2}">
															<img src="${_IMAGE_DOMAIN_}${group.img}" alt=""/>
														</a>
													</c:when>
													<c:otherwise>
														<a id="group_${group.groupNo}">
															<img src="${_IMAGE_DOMAIN_}${group.img}" alt=""/>
														</a>
													</c:otherwise>
												</c:choose>
											</c:if>
											<c:if test="${!isMobile}">
												<c:choose>
													<c:when test="${group.url1 ne null && group.url1 ne ''}">
														<a id="group_${group.groupNo}" href="${group.url1}" target="_blank">
															<img src="${_IMAGE_DOMAIN_}${group.img}" alt=""/>
														</a>
													</c:when>
													<c:otherwise>
														<a id="group_${group.groupNo}">
															<img src="${_IMAGE_DOMAIN_}${group.img}" alt=""/>
														</a>
													</c:otherwise>
												</c:choose>
											</c:if>
										</c:if>
										<a href="#home" class="btn_top">TOP</a>
									</h4>
								</c:if>
							</c:if>
							<c:choose>
								<c:when test="${isMobile}">
									<c:set var="productDisplayTypeVal" value="${group.productDisplayType2Cd}" />
								</c:when>
								<c:otherwise>
									<c:set var="productDisplayTypeVal" value="${group.productDisplayType1Cd}" />
								</c:otherwise>
							</c:choose>
						
							<div class="product_type1 prodType_<tags:codeName code="${productDisplayTypeVal}"/>ea block" id="div_${group.groupNo}">
								<ul>
									<c:forEach var="exhibitProduct" items="${group.dmsExhibitproducts}" varStatus="">
										<c:set var="product" value="${exhibitProduct.pmsProduct}" scope="request" />
										<c:if test="${product.saleStateCd eq 'SALE_STATE_CD.SALE'}">
											<jsp:include page="/WEB-INF/views/dms/include/displayProductInfo.jsp" flush="false">
												<jsp:param name="type" value="exhibit" />
											</jsp:include>
										</c:if>
									</c:forEach>
									<c:if test="${empty group.dmsExhibitproducts}">
										상품이 없습니다.				
									</c:if>
								</ul>
							</div>
						</c:if>
					</c:forEach>
				</c:if>
			</div>
			</div>
		</div>
	</div>
<form>
	<input type="hidden" name="exhibitId" value="${exhibitDetail.exhibitId}"/>
	<input type="hidden" name="currentPage" value="1"/>
	<input type="hidden" name="totalCount" value="${exhibitDetail.totalCount}"/>
</form>