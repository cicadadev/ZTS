<%--
	화면명 : salesAssist lookbook
	작성자 : ian
 --%>

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<jsp:include page="/WEB-INF/views/gsf/layout/common/commonVariable.jsp" />	
<jsp:include page="/WEB-INF/views/gsf/layout/common/commonScript.jsp" />

<script type="text/javascript" src="/resources/js/dms/dms.salesAssist.js"></script>
<script type="text/javascript">
$(document).ready(function() {

});

function goBack() {
	$("#lookbook_detail").hide();
	$("#lookbook_list").show();
}

function closeSalesAssist() {
	window.close();
}
</script>

<style>
.mobile .content { padding-top: 50px; }
</style>

<div>
	<input type="hidden" name="hid_brandId" id="hid_brandId" value="${brandInfo.brandId}" />
	<input type="hidden" name="hid_brandName" id="hid_brandName" value="${brandInfo.name}" />
	<input type="hidden" name="hid_templateType" id="hid_templateType" value="A" />
	<input type="hidden" name="hid_brandClassName" id="hid_brandClassName" value="${brandClassName}" />
</div>

<form name="brandForm" id="brandForm">
	<input type="hidden" name="hidCurrentPage" id="hidCurrentPage" value="" />
	<input type="hidden" name="hidCurrentTab" id="hidCurrentTab" value="" />
	<input type="hidden" name="catalogDetailYn" id="catalogDetailYn" value="" />
	<input type="hidden" name="directCatalogYn" id="directCatalogYn" value="" />
</form>

<form name="catalogForm" id="catalogForm">
	<input type="hidden" name="hid_catalogId" id="hid_catalogId" value="" />
	<input type="hidden" name="hid_catalogImgNo" id="hid_catalogImgNo" value="" />
	<input type="hidden" name="hid_catalogTitle" id="hid_catalogTitle" value="" />
</form>

<form name="pagingForm" id="realtime_pagingForm">
	<input type="hidden" name="hid_current" id="hid_current" value="" />
	<input type="hidden" name="hid_totalCnt" id="hid_totalCnt" value="" />
</form>


<div style="text-align: center; overflow: hidden;">
	<img src="/resources/img/mobile/btn/header_navi_prev.gif" onclick="goBack();" 
	style="width: 38px; height: 38px; float: left">
	<img src="/resources/img/mobile/txt/txt_brand_${brandInfo.brandId}.jpg" alt="" height="38px;" align="center"/>
	<img src="/resources/img/mobile/btn/layer_close.png" onclick="javascript:closeSalesAssist();"
	 style="width: 15px; height: 15px; float: right; padding: 11px 10px 15px 0px;">
</div>

<div class="mainCon brandType ${brandClassName}">
	<div class="inner brand_lookbook brandA_03" id="main3" >
		
		<div id="lookbook_list" >
		
		</div>
		<div id="lookbook_detail" style="display:none;">
		
		</div>
	</div>
</div>

<jsp:include page="/WEB-INF/views/dms/template/inner/salesAssist/salesContents.jsp" />
