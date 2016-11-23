<%--
	화면명 : 사은품 선택
	작성자 : dennis
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!-- ### 팝업 - 사은품 선택 ### -->
<div class="pop_wrap ly_selectGift" style="display:block;" id="presentListDiv">
<script type="text/javascript">


var chgChk = function(idx,subIdx){
	var presentSelectTypeCd = $("#presentSelectTypeCd"+idx).val();
	var selectQty = $("#selectQty"+idx).val();
	var name = $("#name"+idx).val();
	
	if(presentSelectTypeCd == "PRESENT_SELECT_TYPE_CD.SELECT"){
		var selCnt = 0;
		$("input[name=chk"+idx+"]:checked").each(function(){
			selCnt++;
		});
		if(selectQty < selCnt){
			alert("[사은품]"+name+" 선택가능수량은 "+selectQty+"개 입니다.");
			var id = "chk"+idx+subIdx;
			$("#"+id).prop("checked","");
			return false;
		}
	}
	return true;
	
}

var chgChkPr = function(presentId,subIdx){
	
	var productIdx = $("input[name=productIdx]");
	var thisId = "chk"+presentId+subIdx;
	
	var chkFlag = $("#"+thisId).prop("checked");
	
// 	console.log(productIdx);
// 	console.log(chkFlag);
	
	for(var i=0;i<productIdx.length;i++){
		var idx = productIdx[i].value;
		var productPresentId = $("#presentId"+idx).val();
		
// 		console.log(idx);
// 		console.log(productPresentId);
// 		console.log(presentId);
		
		if(productPresentId == presentId){
			var id = "chk"+idx+subIdx;
			if(chkFlag){
				if(chgChk(idx,subIdx)){					
					$("#"+id).prop("checked",true);
				}
			}else{
				$("#"+id).prop("checked",false);
			}
		}
	}
	fnChkStyle();
}

var presentOrder = function(){
	//상품사은품 "PRESENT_TYPE_CD.PRODUCT":orderProductNo:presentId:productIds
	//주문사은품 "PRESENT_TYPE_CD.ORDER":presentId:productIds
	
	var totalCnt = $("#totalCnt").val();	
	var selectPresent="";
	
	for(var i=0;i<Number(totalCnt);i++){
		
		var presentTypeCd = $("#presentTypeCd"+i).val();
		var presentId = $("#presentId"+i).val();
		var productIds="";
		$("input[name=chk"+i+"]:checked").each(function(){			
			productIds += this.value + ",";
		});
		productIds = productIds.substr(0,productIds.length-1);
		
		if(productIds != ''){
			if(presentTypeCd == "PRESENT_TYPE_CD.PRODUCT"){
			
// 				var productId = $("#productId"+i).val();				
// 				var saleproductId = $("#saleproductId"+i).val();
// 				selectPresent += presentTypeCd+":"+productId+"_"+saleproductId+":"+presentId+":"+productIds+"||";
				var orderProductNo = $("#orderProductNo"+i).val();								
				selectPresent += presentTypeCd+":"+orderProductNo+":"+presentId+":"+productIds+"||";
			}else{
				
				selectPresent += presentTypeCd+":"+presentId+":"+productIds+"||";
			}
		}else{
			var presentId = $("#presentId"+i).val();
// 			console.log(presentId);
			if(!$("#chkAllNon_1").prop("checked") && !$("#chkAllNon_2").prop("checked")){
				var chkNonFlag = $("#chkNon_"+presentId).prop("checked");
// 				console.log(chkNonFlag);
				if(!chkNonFlag){
					alert("사은품이 선택되지 않았습니다. 사은품 선택하지 않기에 동의하거나 사은품을 선택해주세요.");
					$("#chkNon_"+presentId).focus();
					return;
				}
			} 
		}
	}
	selectPresent = selectPresent.substr(0,selectPresent.length-2);
// 	console.log(selectPresent);
	//layer일때
	var openerForm = $("#_orderForm");
	
	openerForm.append($('<input>',{
		'name' : 'orderTF',
		'value' : 'true'
	})).append($('<input>',{
		'name' : 'selectPresent',
		'value' : selectPresent
	})).append($('<input>',{
		'name' : 'orderStat',
		'value' : 'ORDERSHEET'
	}))
	
	openerForm.submit();
	
	//popup 일때
// 	var openerForm = $(opener.document).find("#_orderForm");
	
// 	openerForm.append('<input name="orderTF" value="true" />')
// 	.append('<input name="selectPresent" value="'+selectPresent+'" />');
	
// 	openerForm.attr("action","/oms/order/list");
// 	openerForm.attr("target","");

// 	openerForm.submit();

// 	window.close();

	//페이지일때
// 	console.log(selectPresent);
// 	$("#targetForm #selectPresent").val(selectPresent);
	
//  	$("#targetForm").submit();
}


var chkNon = function(presentId,type,obj){
	
	var chkFlag = $(obj).prop("checked");		
	
	if(presentId == "ALL"){
		var totalCnt = $("#totalCnt").val();				
		
		for(var i=0;i<Number(totalCnt);i++){
			
			var presentId = $("#presentId"+i).val();
			
			$("input[name=chk"+presentId+"]").each(function(){			
				$(this).prop("checked",false);				
				$(this).prop("disabled",chkFlag);		
			})
			$("input[name=chk"+i+"]").each(function(){			
				$(this).prop("checked",false);				
				$(this).prop("disabled",chkFlag);		
			})
		}
	}else{
		if(type == "PR"){
			$("input[name=chk"+presentId+"]").each(function(){	
// 				console.log($(this));
				$(this).prop("checked",false);
				$(this).prop("disabled",chkFlag);
			});
									
		}
		
		var totalCnt = $("#totalCnt").val();				
		
		for(var i=0;i<Number(totalCnt);i++){
			if(presentId == $("#presentId"+i).val()){
				$("input[name=chk"+i+"]").each(function(){			
					$(this).prop("checked",false);
					$(this).prop("disabled",chkFlag);
				});
			}
		}
	}
	
	fnChkStyle();
}

</script>	

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
						<input type="checkbox" id="chk${idx}${subIdx.index}" name="chk${idx }" value="${ops.productId }" />
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
					<dl>
						<dt>
							<p>${osp.name }</p>
							<c:choose>
							<c:when test="${isMobile }">
							<div>
								${osp.minOrderAmt }원 이상 구매한 모든 고객에게 사은품을 드립니다.
								<span>(${osp.startDt } ~ ${osp.endDt })</span>
							</div>
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
							<div>
								${osp.minOrderAmt }원 이상 구매한 모든 고객에게 사은품을 드립니다.
								<span>(${osp.startDt } ~ ${osp.endDt })</span>
							</div>
							</c:otherwise>
							</c:choose>
							<ul>
								<c:forEach items="${osp.spsPresentproducts }" var="ops" varStatus="subIdx">
								<li>
									<label class="chk_style1">
										<em>
											<input type="checkbox" id="chk${osp.presentId}${subIdx.index}" name="chk${osp.presentId }" value="${ops.productId }" onclick="javascript:chgChkPr('${osp.presentId}','${subIdx.index}')" />
										</em>
										<span>
											<i>
												<img src="/resources/img/pc/temp/cart_img3.jpg" alt="">
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
						${op.minOrderAmt }원 이상 구매한 모든 고객에게 사은품으로 모자를 드립니다.
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
						${op.minOrderAmt }원 이상 구매한 모든 고객에게 사은품으로 모자를 드립니다.
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
										<img src="/resources/img/pc/temp/cart_img3.jpg" alt="">
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
						<input type="checkbox" id="chkAllNon_2" value=""  onclick="javascript:chkNon('ALL')"/>
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
			</form>
		</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>

<script>

var presentListClose = function(){	
	$("#presentListDiv").remove();
}

$(".pc .pop_wrap .pc_btn_close").off("click").on("click", function(e){
	presentListClose();
	//e.preventDefault();
});

function fnChkStyle(){
	$(".pc .chk_style1 input").each(function() {
		if($(this).is(':checked')){
			$(this).parent().addClass("selected");
		}else{
			$(this).parent().removeClass("selected");
		}
	});

	$(".pc .chk_style1 input").on({
		"change" : function() {
			if($(this).is(':checked')){
				$(this).parent().addClass("selected");
			}else{
				$(this).parent().removeClass("selected");
			}
		}
	});
}
fnChkStyle();
</script>
</div>
<!-- ### //팝업 - 사은품 선택 ### -->