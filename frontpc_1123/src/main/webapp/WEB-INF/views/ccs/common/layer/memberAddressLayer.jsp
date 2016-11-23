<%--
	화면명 : 상품평 작성 레이어
	작성자 : roy
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<script>
var addressSelect = function(param, idx){
		
	var form;
	if (param == 'addr') {
		if(idx == "DEFAULT"){
			form = $("#defaultMemberAddress").find("input");
		}else{
			form = $("#memberAddress"+idx).find("input");
		}
	} else {
		form = $("#recentAddress"+idx).find("input");
	}
	
	var data = {};
	form.each(function(){
		var id = $(this).attr("id");
		var value = $(this).val();		
		data[id] = value;
	})		
	
	ccs.layer.memberAddressLayer.select(data);
	ccs.layer.memberAddressLayer.close();
}

$(".mobile .pop_wrap .btn_x").off("click").on({
	"click" : function() {
		$(this).closest(".pop_wrap").hide();
		$(".mobile .content").removeClass("content_fixed").show();
		$(".header_mo, .footer, .footer_mo").show();
		$(".mobile").removeAttr("style");
	}
});
</script>
<!-- ### 배송지 주소록 팝업 ### -->
<div class="pop_wrap ly_address" id="deliveryAddressList">	
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">배송지 목록</h3>
		</div>
		<div class="pop_content">

			<div class="tbl_article addrBook">
				<div class="div_tb_thead2">
					<div class="tr_box">
						<span class="col1">나의 배송지</span>
						<span class="col2">삭제/선택</span>
					</div>
				</div>

				<ul class="div_tb_tbody2">
					<c:if test="${empty memberAddressList}">
						<p class="empty">나의 배송지 목록이 없습니다.</p>
					</c:if>
					
				
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
						</form>
							<li id="${ma.addressNo}">
								<div class="tr_box">
									<div class="col1">
										<div class="address">
											<strong>${ma.deliveryName1 }</strong>
											<span class="default icon_type2 iconPink3">기본 배송지</span>
											<ul>
												<li class="phone">
													<b id="phone2_b"><script>ccs.common.phone_format("phone2_b", "${ma.phone2}")</script></b> /
													<b id="phone1_b"><script>ccs.common.phone_format("phone1_b", "${ma.phone1}")</script></b>
												</li>
												<li>
													(${ma.zipCd }) ${ma.address1 } ${ma.address2 }
												</li>
											</ul>
										</div>
									</div>
									<div class="col2">
										<div class="btns">
											<a href="#none" class="btn_sStyle1 sWhite2" onclick="javascript:addressSelect('addr', 'DEFAULT')">선택</a>
										</div>
									</div>
								</div>
							</li>					
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
						<li id="${ma.addressNo}">
							<div class="tr_box">
								<div class="col1">
									<div class="address">
										<strong>${ma.deliveryName1 }</strong>
										<ul>
											<li class="phone">
												<b id="phone2_b${status.index }"><script>ccs.common.phone_format("phone2_b${status.index }", "${ma.phone2}")</script></b> /
												<b id="phone1_b${status.index }"><script>ccs.common.phone_format("phone1_b,${status.index }", "${ma.phone1}")</script></b>
											</li>
											<li>
												(${ma.zipCd }) ${ma.address1 } ${ma.address2 }
											</li>
										</ul>
									</div>
								</div>
								<div class="col2">
									<div class="btns">
										<a href="#none" class="btn_sStyle1 sWhite2" onclick="javascript:ccs.layer.memberAddressLayer.addressDelete('${ma.addressNo}')">삭제</a>
										<a href="#none" class="btn_sStyle1 sWhite2" onclick="javascript:addressSelect('addr', '${ma.addressNo}')">선택</a>
									</div>
								</div>
							</div>
						</li>
					</c:otherwise>
					</c:choose>		
					</c:forEach>			
				</ul>
			</div>
			
			<div class="tbl_article addrBook">
				<div class="div_tb_thead2">
					<div class="tr_box">
						<span class="col1">최근 배송지</span>
						<span class="col2">선택</span>
					</div>
				</div>

				<ul class="div_tb_tbody2">
					<c:if test="${empty memberRecentAddressList}">
						<p class="empty">최근 배송지 목록이 없습니다.</p>
					</c:if>
					<c:forEach items="${memberRecentAddressList}" var="address" varStatus="i">
						<form name="memberAddress" id="recentAddress${address.rownumber }">
							<input type="hidden" id="addressNo" value="${address.deliveryAddressNo }"/>
							<input type="hidden" id="deliveryName1" value="${address.name1 }"/>
							<input type="hidden" id="phone1" value="${address.phone1 }"/>
							<input type="hidden" id="phone2" value="${address.phone2 }"/>
							<input type="hidden" id="zipCd" value="${address.zipCd }"/>
							<input type="hidden" id="address1" value="${address.address1 }"/>
							<input type="hidden" id="address2" value="${address.address2 }"/>
						</form>
						<li>
							<div class="tr_box">
								<div class="col1">
									<div class="address">
										<strong>${address.name1}</strong>
										<ul>
											<li class="phone">
												<b id="recent_phone2_b${i.index }"><script>ccs.common.phone_format("recent_phone2_b${i.index }", "${address.phone2}")</script></b> /
												<b id="recent_phone1_b${i.index }"><script>ccs.common.phone_format("recent_phone1_b${i.index }", "${address.phone1}")</script></b>
											</li>
											<li>
												(${address.zipCd }) ${address.address1 } ${address.address2 }
											</li>
										</ul>
									</div>
								</div>
								<div class="col2">
									<div class="btns">
										<a href="#none" class="btn_sStyle1 sWhite2" onclick="javascript:addressSelect('recent','${address.rownumber}')">선택</a>
									</div>
								</div>
							</div>
						</li>
					</c:forEach>
					
				</ul>
			</div>

			<div class="btn_wrapC btn1ea">
				<a href="javascript:ccs.layer.memberAddressLayer.close()" class="btn_mStyle1 sWhite1">취소</a>
			</div>
		</div>
		<button type="button" class="btn_x pc_btn_close" onclick="javascript:ccs.layer.memberAddressLayer.close()">닫기</button>
	</div>
</div>
<!-- ### //배송지 주소록 팝업 ### -->