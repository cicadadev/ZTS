<%--
	화면명 : 사은품 선택
	작성자 : dennis
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags" %>
<!-- ### 팝업 - 사은품 선택 ### -->

<script type="text/javascript">

$(document).ready(function(){
	fnChkStyle();
})

var chgChk = function(idx,subIdx){
	
	$(".pc .chk_style1 input").off();
	
	var presentSelectTypeCd = $("#presentSelectTypeCd"+idx).val();
	var selectQty = $("#selectQty"+idx).val();
	var name = $("#name"+idx).val();
	
	var id = "chk"+idx+subIdx;
	
	var chkFlag = $("#"+id).prop("checked");
	
	if(presentSelectTypeCd == "PRESENT_SELECT_TYPE_CD.SELECT"){
		if(chkFlag){
			var selCnt = 0;
			$("input[name=chk"+idx+"]:checked").each(function(){			
				selCnt++;
			});
						
			if(selectQty < selCnt){
				alert(name+" 선택가능수량은 "+selectQty+"개 입니다.");
				$("#"+id).prop("checked",false).parent().removeClass("selected");						
			}else{
				$("#"+id).prop("checked",true).parent().addClass("selected");
			}
		}else{
			$("#"+id).prop("checked",false).parent().removeClass("selected");
		}
	}		
}

var chgChk2 = function(presentId){
	var presentSelectTypeCd = $("#subpresentSelectTypeCd"+presentId).val();
	var selectQty = $("#subselectQty"+presentId).val();
	var name = $("#subname"+presentId).val();
	
	if(presentSelectTypeCd == "PRESENT_SELECT_TYPE_CD.SELECT"){
		var selCnt = 0;
		$("input[name=subchk"+presentId+"]:checked").each(function(){			
			selCnt++;
		});
		if(selectQty < selCnt){
			alert(name+" 선택가능수량은 "+selectQty+"개 입니다.");					
			return false;
		}
	}
	return true;
	
}

var chgChkPr = function(presentId,subIdx){
	
	$(".pc .chk_style1 input").off();
	
	var productIdx = $("input[name=productIdx]");
	var thisId = "subchk"+presentId+subIdx;
	
	var chkFlag = $("#"+thisId).prop("checked");
	
	if(chkFlag){
		if(chgChk2(presentId)){
			chkFlag = true;	
			$("#"+thisId).prop("checked",true).parent().addClass("selected");
		}else{
			chkFlag = false;
			$("#"+thisId).prop("checked",false).parent().removeClass("selected");
		}		
	}else{
		chkFlag = false;
		$("#"+thisId).prop("checked",false).parent().removeClass("selected");
	}
	
	for(var i=0;i<productIdx.length;i++){
		var idx = productIdx[i].value;
		var productPresentId = $("#presentId"+idx).val();
		
		if(productPresentId == presentId){
			var id = "chk"+idx+subIdx;
			if(chkFlag){
				$("#"+id).prop("checked",true);
			}else{
				$("#"+id).prop("checked",false);
			}
		}
	}		
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
	common.showLoadingBar();
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
			
			$("input[name=subchk"+presentId+"]").each(function(){			
				$(this).prop("checked",false);				
				$(this).prop("disabled",chkFlag);		
			})
			$("input[name=chk"+i+"]").each(function(){			
				$(this).prop("checked",false);				
				$(this).prop("disabled",chkFlag);		
			})
			
			$('[id^=chkNon_]').each(function(){
				$(this).prop("checked",chkFlag);				
				$(this).prop("disabled",chkFlag);
			})
			$("#chkAllNon_1").prop("checked",chkFlag);
			$("#chkAllNon_2").prop("checked",chkFlag);
		}
	}else{
		if(type == "PR"){
			$("input[name=subchk"+presentId+"]").each(function(){	
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

var presentListClose = function(){	
	var orderLoginReturn = $("#orderLoginReturn").val();
	if(orderLoginReturn == "true"){
		history.back();
	}else{
		$("#presentListDiv").remove();
	}
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

</script>