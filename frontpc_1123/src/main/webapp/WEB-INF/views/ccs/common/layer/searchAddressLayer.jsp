<%--
	화면명 : 우편번호 검색 레이어
	작성자 : allen
--%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<script type="text/javascript">

//TODO : 임시 모바일 페이징 처리

function onKeyDown()
{
     if(event.keyCode == 13)
     {
         alert("1111");
     }
}


$(".lucas_scroll").bind("scroll", productListScrollListener);

var temp = 0;
function productListScrollListener(e){
	
	var rowCount = 0;
	var totalCount = Number($("input[name=addressTotalCnt]").val());
	if ("jibun" == $("input[name=addrType]").val()) {
		rowCount = $("#jibunAddrResult").children().find("li").length;
	} else {
		rowCount = $("#roadAddrResult").children().find("li").length;
	}
	
	var max_scroll = $(".conHeight", this).innerHeight();
	var now_scroll = $(this).innerHeight() + $(this).scrollTop();
	console.log("max_scroll", max_scroll -200);
	console.log("now_scroll", now_scroll);
    if (now_scroll >= max_scroll - 200 && max_scroll != 0) {
    	if (rowCount != totalCount) {
	    	if (temp == 0) {
	    		temp++;
				// TODO : 상품 가져오는중인지 체크하여 return;
	    		ccs.layer.searchAddressLayer.moreAddressList();
				setTimeout(function(){ temp = 0}, 1000);
	    	}
    	}
    }
}

</script>

<!-- ### 우편번호 찾기 팝업 ### -->
<div class="pop_wrap ly_zip" id="searchAddressLayer">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">우편번호 찾기</h3>
		</div>
		<div class="pop_content">
			<div>
				<ul class="tabBox tp1">
					<li class="on" id="jibun"><a href="javascript:ccs.layer.searchAddressLayer.changeTab('jibun');">지번 주소</a></li>
					<li id="road"><a href="javascript:ccs.layer.searchAddressLayer.changeTab('road');">도로명 주소</a></li>
				</ul>
				<div class="tab_con tab_conOn">
					<div class="addr_search">
						<div class="inpBox">
							<div class="inputTxt_place1">
								<label></label>
								<span>
									<input type="text" value="" name="jibunSearchKeyword">
								</span>
							</div>
							<a href="javascript:ccs.layer.searchAddressLayer.search()" class="btn_sStyle4 sGray2" onkeydown="javascript:onKeyDown();">검색</a>
						</div>
					</div>

					<ul class="txt_guide">
						<li>
							찾고 싶으신 주소의 동(읍/면) 이름을 입력해 주세요.<br />
							예) 망원동, 상암동, 수지읍
						</li>
					</ul>

					<div class="tbl_article" id="jibunResultDiv" style="display:none">
						<div class="div_tb_thead2">
							<div class="tr_box">
								<span class="col1">우편번호</span>
								<span class="col2">주소</span>
							</div>
						</div>
						
						<div class="lucas_scroll">
							<div class="conHeight">
								<div id="jibunAddrResult">
									
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="tab_con">
					<div class="select_2ea">
						<div class="select_box1">
							<label id="districtLabel"></label>
							<select onchange="javascript:ccs.layer.searchAddressLayer.changeDistrict(this)">
								<option value="">시/도</option>
								<c:forEach items="${districtList}" var="district">
									<option value="${district.regioncd}">${district.regionnm}</option>
								</c:forEach>
							</select>
						</div>
						<div class="select_box1">
							<label id="sigunguLabel"></label>
							<select id="sigunguSelect" onchange="javascript:ccs.layer.searchAddressLayer.changeSigungu(this)">
								<option>시/군/구</option>
							</select>
						</div>
					</div>

					<div class="addr_search">
						<div class="inpBox">
							<div class="inputTxt_place1">
								<label></label>
								<span>
									<input type="text" value="" name="roadSearchKeyword">
								</span>
							</div>
							<a href="javascript:ccs.layer.searchAddressLayer.search()" class="btn_sStyle4 sGray2" onkeydown="javascript:onKeyDown();">검색</a>
						</div>
					</div>

					<ul class="txt_guide">
						<li>
							찾고 싶으신 도로명 또는 건물명을 입력해 주세요.<br />
							예) 반포대로1, 세종대로 23, 성산로 11
						</li>
					</ul>

					<div class="tbl_article" id="roadResultDiv" style="display:none">
						<div class="div_tb_thead2">
							<div class="tr_box">
								<span class="col1">우편번호</span>
								<span class="col2">주소</span>
							</div>
						</div>


						<div class="lucas_scroll">
							<div class="conHeight">
								<div id="roadAddrResult">
									
								</div>
							</div>
						</div>
					</div>
				</div>

				<div class="zip_result" style="display:none">
					<div class="inputTxt_place1 place_disabled addr01">
						<label></label>
						<span>
							<input type="text" value="" disabled="disabled" name="clickZipCd"/>
						</span>
					</div>
					<div class="inputTxt_place1 place_disabled addr02">
						<label></label>
						<span>
							<input type="text" value="" disabled="disabled" name="clickAddr1"/>
						</span>
					</div>
					<div class="inputTxt_place1 inp01 addr03">
						<label></label>
						<span>
							<input type="text" value="" name="clickAddr2"/>
						</span>
					</div>
				</div>

				<div class="btn_wrapC" style="display:none">
					<a href="javascript:ccs.layer.searchAddressLayer.confirm()" class="btn_mStyle1 sPurple1">확인</a>
				</div>

				<div class="zip_result2" id="zip_result2" style="display:none">
					<dl>
						<dt>지번 주소</dt>
						<dd>
							<p>
								<span id="selectJibunZipCd">03926</span>&nbsp;&nbsp;<span id="selectJibunAddr">서울특별시 마포구 상암동 YTN 뉴스퀘어 17/18층</span>
							</p>
							<a href="javascript:ccs.layer.searchAddressLayer.selectAddr('jibun');" class="btn_sStyle1">선택</a>
						</dd>
					</dl>
					<dl>
						<dt>도로명 주소</dt>
						<dd>
							<p>
								<span id="selectRoadZipCd">03926</span>&nbsp;&nbsp;<span id="selectRoadAddr">서울특별시 마포구 상암산로 76 YTN 뉴스퀘어 17/18층</span>
							</p>
							<a href="javascript:ccs.layer.searchAddressLayer.selectAddr('road');" class="btn_sStyle1">선택</a>
						</dd>
					</dl>
				</div>
			</div>
		</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>
<!-- ### //우편번호 찾기 팝업 ### -->
<script type="text/javascript">
	
</script>
<form name="addressForm" id="addressForm">
	<input type="hidden" name="sidoName" value="" />
	<input type="hidden" name="sigunguName" value="" />
	<input type="hidden" name="searchKeyword" value="" />
	<input type="hidden" name="addrType" value="" />
	<input type="hidden" name="currentPage" value="1" />
</form>