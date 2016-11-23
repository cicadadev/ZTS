<%--
	화면명 : 브랜드 검색 리스트
	작성자 : 로이
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%> 
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%@ taglib prefix="page" uri="kr.co.intune.commerce.common.paging"%>

<script type="text/javascript">
	
	$(document).ready(function(){

		var brandChk=JSON.parse(localStorage.getItem('brandIds'));
		//console.log(JSON.stringify(brandChk));
	    for(i in brandChk){
	    	//console.log(brandChk[i].brandId);
	    	$('#searchBrand'+brandChk[i].brandId).attr('checked', true);
	    }
	    
	});
</script>

<div class="view_brand sc_brand">
	<strong class="tit1">${title }</strong>
	<ul>
		<c:forEach items="${list}" var="list">
			<li><!-- roy 10.28 브랜드 링크 -->
				<a href="javascript:ccs.link.product.brandProdutList('BRAND','${list.brandId }','','');">${list.name }</a>
				<label class="chk_style1">
					<em>
						<input type="checkbox" id="searchBrand${list.brandId }" value="" onClick="dmsmb.lnb.brand.change('${list.name }', '${list.brandId}', '');"/>
					</em>
					<span>즐겨찾기</span>
				</label>
			</li>
		</c:forEach>
	</ul>
</div>