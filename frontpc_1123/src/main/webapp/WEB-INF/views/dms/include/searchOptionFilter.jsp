<%--
	화면명 : 검색필터
	작성자 : emily
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<script type="text/javascript">

$(document).ready(function(){
	dms.search.option.pageInit();
});


//가격 입력시 화폐단위 자동으로 입력 
function strip_comma(data){
	var flag = 1;
	var valid = "1234567890";
	var output = '';
	if (data.charAt(0) == '-'){
	    flag = 0;
	    data = data.substring(1);
	}
	
	for (var i=0; i<data.length; i++){
	    if (valid.indexOf(data.charAt(i)) != -1)
	        output += data.charAt(i)
	}
	
	if (flag == 1){
	    return output;
	}else if (flag == 0){
	    return ('-' + output);
	}
}
  
function add_comma(what){
    var flag = 1;
    var data = what;
    var len = data.length;
    
    if (data.charAt(0) == '-'){
        flag = 0;
        data = data.substring(1);
    }
    
    if (data.charAt(0) == '0' && data.charAt(1) == '-'){
        flag = 0;
        data = data.substring(2);
    }
    
    var number = strip_comma(data);
    number = '' + number;
    if (number.length > 3){
        var mod = number.length % 3;
        var output = (mod > 0 ? (number.substring(0,mod)) : '');
        for (i=0; i<Math.floor(number.length/3); i++){
            if ((mod == 0) && (i == 0)){
                output += number.substring(mod+3*i, mod+3*i+3);
            }else{
                output += ',' + number.substring(mod+3*i, mod+3*i+3);
            }
        }
        
        if (flag == 0){
            return ('-' + output);
        }else{
            return (output);
        }
    }else{
        if (flag == 0){
            return ('-' + number);
        }else{
            return (number);
        }
    }
}
  
function replace(str, original, replacement){
    var result;
    result = "";
    while(str.indexOf(original) != -1){
        if (str.indexOf(original) >= 0){
            result = result + str.substring(0, str.indexOf(original)) + replacement;
        }else{
            result = result + replacement;
            str = str.substring(str.indexOf(original) + original.length, str.length);
        }
    }
    return result + str;
}
  
function comma(what){
    var data = what.value;
    
    if ((event.keyCode == 107) || (event.keyCode == 187)){
        if ((data == "+") || (data == "0+") || (Math.floor(replace((replace(data,"+","")),",","")) == 0)){
            dataval = "";
        }else{
            var dataval = data + '000';
            dataval = replace(dataval,"+","");
        }
    }else{
        /* if (Math.floor(data) == 0){
            dataval = "";
        }else{
            var dataval = data;
        } */
    	var dataval = data;
    }
    
    what.value = add_comma(dataval);
} 

</script>

<div class="srchMore">
						
	<c:choose>
		<c:when test="${param.type eq 'category'}">
			<ul class="srchBtns srch_2ea mo_only">
				<li><a href="#none" class="btnBrand"><span>브랜드</span></a></li>
				<li><a href="#none" class="btnDetail"><span>상세검색</span></a></li>
			</ul>
			
			<!-- 옵션 필터링 //-->
			<div class="optionBox">
				<div class="optionItemBox">
					<div class="optionItem brand first">
						<div class="optionTit"><strong>브랜드 선택</strong></div>
						<div class="optionCont">
							<%-- <div class="resultCount"> 총 <em><c:out value="${searchApi.brdtotalCount}"/></em>개 브랜드</div> --%>
							<ul class="txtList">
								<c:forEach var="brand" items="${searchApi.brandList}">
									<li>
										<label class="chk_style1">
											<em>
												<input type="checkbox" value="${brand.brandId}" name="brandId"/>
											</em>
											<span id="brandName_${brand.brandId}" data-name="${brand.brandName}"><c:out value="${brand.brandName}"/></span>
										</label>
									</li>
								</c:forEach>
							</ul>
						</div>
					</div>
					
					<div class="optionItem optDetail age">
						<div class="optionTit"><strong>월령</strong></div>
						<div class="optionCont">
							<ul class="txtList">
								<c:forEach var="age" items="${searchApi.ageCodeList}">
									<li>
										<label class="chk_style1 option_style1">
											<em>
												<input type="checkbox" value="${age.cd}" name="ageTypeCode">
											</em>
											<span id="ageName_${age.cd}" data-name="${age.name}"><c:out value="${age.name}"/> </span>
										</label>
									</li>
								</c:forEach>
							</ul>
						</div>
					</div>
					
					<div class="optionItem optDetail gender">
						<div class="optionTit"><strong>성별</strong></div>
						<div class="optionCont">
							<ul class="txtList">
								<li>
									<label class="radio_style1 option_style1">
										<em>
											<input type="radio" value="ALL" name="inpGender">
										</em>
										<span id="gender_ALL">전체</span>
									</label>
								</li>
								<c:forEach var="gender" items="${searchApi.genderCodeList}">
									<li>
										<label class="radio_style1 option_style1">
											<em>
												<input type="radio" name="inpGender" value="${gender.cd}"}/>
											</em>
											<span id="gender_${gender.cd}" data-name="${gender.name}"><c:out value="${gender.name}"/> </span>
										</label>
									</li>
								</c:forEach>
							</ul>
						</div>
					</div>
		
					<div class="optionItem optDetail price">
						<div class="optionTit"><strong>가격</strong></div>
						<div class="optionCont">
							<input type="text" value="0" class="inputTxt_style1" id="lowPrice" OnKeyUp="comma(this)"/>
							<span class="swung">~</span>
							<input type="text" value="0" class="inputTxt_style1" id="highPrice" OnKeyUp="comma(this)"/>
							<a href="javascript:dms.search.option.priceApply();" class="btn_sStyle3  sWhite1">적용</a> 
						</div>
					</div>
					
					<c:if test="${not empty searchApi.colorList}">
						<div class="optionItem optDetail color">
							<div class="optionTit"><strong>컬러</strong></div>
							<div class="optionCont">
								<ul class="colorList">
									<c:forEach var="color" items="${searchApi.colorList}">
										<c:if test="${color.colorName ne 'null'}">
											<c:choose>
												<c:when test="${color.colorName eq '블랙'}">
													<c:set var="cssName" value="black"/>
												</c:when>	
												<c:when test="${color.colorName eq '그레이'}">
													<c:set var="cssName" value="gray"/>
												</c:when>	
												<c:when test="${color.colorName eq '화이트'}">
													<c:set var="cssName" value="white"/>
												</c:when>	
												<c:when test="${color.colorName eq '레드'}">
													<c:set var="cssName" value="red"/>
												</c:when>	
												<c:when test="${color.colorName eq '옐로우'}">
													<c:set var="cssName" value="yellow"/>
												</c:when>	
												<c:when test="${color.colorName eq '오렌지'}">
													<c:set var="cssName" value="orange"/>
												</c:when>	
												<c:when test="${color.colorName eq '블루 '}">
													<c:set var="cssName" value="blue"/>
												</c:when>	
												<c:when test="${color.colorName eq '베이지'}">
													<c:set var="cssName" value="beige"/>
												</c:when>	
												<c:when test="${color.colorName eq '그린'}">
													<c:set var="cssName" value="green"/>
												</c:when>	
												<c:when test="${color.colorName eq '핑크'}">
													<c:set var="cssName" value="pink"/>
												</c:when>
												<c:otherwise>
													<c:set var="cssName" value="${color.colorName}"/>
												</c:otherwise>	
											</c:choose>
											<li>
												<a href="#none" class="${cssName}" name="${color.colorName}"><span>${color.colorName}</span></a>
											</li>
										</c:if>
									</c:forEach>
								</ul>
							</div>
						</div>
					</c:if>
		
					<c:if test="${not empty searchApi.materialList}">
						<div class="optionItem optDetail material">
							<div class="optionTit"><strong>소재</strong></div>
							<div class="optionCont">
								<ul class="txtList col">
									<c:forEach var="meter" items="${searchApi.materialList}">
										<c:if test="${meter.materialName ne 'null'}">
											<li>
												<label class="chk_style1 option_style1">
													<em>
														<input type="checkbox" value="${meter.materialName}" name="material">
													</em>
													<span><c:out value="${meter.materialName}"/> </span>
												</label>
											</li>
										</c:if>
									</c:forEach>
								</ul>
							</div>
						</div>
					</c:if>
					
					<c:if test="${not empty searchApi.material1List}">
						<div class="optionItem optDetail material">
							<div class="optionTit"><strong>소재</strong></div>
							<div class="optionCont">
								<ul class="txtList col">
									<c:forEach var="meter" items="${searchApi.material1List}">
										<c:if test="${meter.material1Name ne 'null'}">
											<li>
												<label class="chk_style1 option_style1">
													<em>
														<input type="checkbox" value="${meter.material1Name}" name="material1">
													</em>
													<span><c:out value="${meter.material1Name}"/> </span>
												</label>
											</li>
										</c:if>
									</c:forEach>
								</ul>
							</div>
						</div>
					</c:if>
		
					<div class="optionItem optDetail benefit">
						<div class="optionTit"><strong>혜택</strong></div>
						<div class="optionCont">
							<ul class="txtList col">
								<li>
									<label class="chk_style1 option_style1">
										<em>
											<input type="checkbox" value=""  name="couponYn">
										</em>
										<span id="bene_coupon">쿠폰할인</span>
									</label>
								</li>
								<li>
									<label class="chk_style1 option_style1">
										<em>
											<input type="checkbox" value="" name="deliveryFreeYn">
										</em>
										<span id="bene_deliveryFree">무료배송</span>
									</label>
								</li>
								<li>
									<label class="chk_style1 option_style1">
										<em>
											<input type="checkbox" value="" name="pointSaveYn">
										</em>
										<span id="bene_pointSave">포인트</span>
									</label>
								</li>
								<li>
									<label class="chk_style1 option_style1">
										<em>
											<input type="checkbox" value="" name="presentYn">
										</em>
										<span id="bene_present">사은품</span>
									</label>
								</li>
								<li>
									<label class="chk_style1 option_style1">
										<em>
											<input type="checkbox" value="" name="regularDeliveryYn">
										</em>
										<span id="bene_regularDelivery">정기배송</span>
									</label>
								</li>
								<li>
									<label class="chk_style1 option_style1">
										<em>
											<input type="checkbox" value="" name="offshopPickupYn">
										</em>
										<span id="bene_offshopPickup">매장픽업</span>
									</label>
								</li>
							</ul>
						</div>
					</div>
					<button type="button" class="btnMore">전체목록 보기</button>
				</div>
				<div class="checkedTxt">
					<ul id="checkedOption">
			
					</ul>
					<a href="#none" class="btn_reset" onclick="dms.search.option.optionInit('all');">초기화</a>
				</div>
			</div>
			<!-- 옵션 필터링 //-->
		</c:when>
		<c:when test="${param.type eq 'brand'}">
			<ul class="srchBtns mo_only">
				<li><a href="#none" class="btnBrand"><span>카테고리</span></a></li>
				<li><a href="#none" class="btnDetail"><span>상세검색</span></a></li>
			</ul>
			<div class="optionBox">
				<div class="optionItemBox">
							
					<c:if test="${ not empty searchApi.categoryList}">
						<div class="optionItem brand first">
							<div class="optionTit"><strong>카테고리</strong></div>
							<div class="optionCont">
								<%-- <div class="resultCount">
									총 <em><c:out value="${searchApi.catgtotalCount}"/></em>개 카테고리
								</div> --%>
								<ul class="txtList">
									<c:forEach items="${searchApi.categoryList}" var="category" >
										<li>
											<label class="chk_style1">
												<em>
													<input type="checkbox" value="${category.categoryId}" name="categoryId"/>
												</em>
												<span id="categoryName_${category.categoryId}" data-name="${category.categoryName}" ><c:out value="${category.categoryName}"/>
													<%-- <i>(<c:out value="${category.categoryCount}"/>)</i> --%>
												</span>
											</label>
										</li>
									</c:forEach>
								</ul>
							</div>
						</div>
					</c:if>
					
					<div class="optionItem optDetail age ${empty searchApi.categoryList ?'first':'' }">
						<div class="optionTit"><strong>월령</strong></div>
						<div class="optionCont">
							<ul class="txtList">
								<c:forEach var="age" items="${searchApi.ageCodeList}">
									<li>
										<label class="chk_style1 option_style1">
											<em>
												<input type="checkbox" value="${age.cd}" name="ageTypeCode">
											</em>
											<span id="ageName_${age.cd}" data-name="${age.name}"><c:out value="${age.name}"/> </span>
										</label>
									</li>
								</c:forEach>
							</ul>
						</div>
					</div>
					
					<div class="optionItem optDetail gender">
						<div class="optionTit"><strong>성별</strong></div>
						<div class="optionCont">
							<ul class="txtList">
								<li>
									<label class="radio_style1 option_style1">
										<em>
											<input type="radio" value="ALL" name="inpGender">
										</em>
										<span id="gender_ALL">전체</span>
									</label>
								</li>
								<c:forEach var="gender" items="${searchApi.genderCodeList}">
									<li>
										<label class="radio_style1 option_style1">
											<em>
												<input type="radio" name="inpGender" value="${gender.cd}"}/>
											</em>
											<span id="gender_${gender.cd}" data-name="${gender.name}"><c:out value="${gender.name}"/> </span>
										</label>
									</li>
								</c:forEach>
							</ul>
						</div>
					</div>
	
					<div class="optionItem optDetail price">
						<div class="optionTit"><strong>가격</strong></div>
						<div class="optionCont">
							<input type="text" value="0" class="inputTxt_style1" id="lowPrice" OnKeyUp="comma(this)"/>
							<span class="swung">~</span>
							<input type="text" value="0" class="inputTxt_style1" id="highPrice" OnKeyUp="comma(this)"/>
							<a href="javascript:dms.search.option.priceApply();" class="btn_sStyle3  sWhite1">적용</a> 
						</div>
					</div>
	
					<c:if test="${not empty searchApi.colorList}">
						<div class="optionItem optDetail color">
							<div class="optionTit"><strong>컬러</strong></div>
							<div class="optionCont">
								<ul class="colorList">
									<c:forEach var="color" items="${searchApi.colorList}">
										<c:if test="${color.colorName ne 'null'}">
											<c:choose>
												<c:when test="${color.colorName eq '블랙'}">
													<c:set var="cssName" value="black"/>
												</c:when>	
												<c:when test="${color.colorName eq '그레이'}">
													<c:set var="cssName" value="gray"/>
												</c:when>	
												<c:when test="${color.colorName eq '화이트'}">
													<c:set var="cssName" value="white"/>
												</c:when>	
												<c:when test="${color.colorName eq '레드'}">
													<c:set var="cssName" value="red"/>
												</c:when>	
												<c:when test="${color.colorName eq '옐로우'}">
													<c:set var="cssName" value="yellow"/>
												</c:when>	
												<c:when test="${color.colorName eq '오렌지'}">
													<c:set var="cssName" value="orange"/>
												</c:when>	
												<c:when test="${color.colorName eq '블루 '}">
													<c:set var="cssName" value="blue"/>
												</c:when>	
												<c:when test="${color.colorName eq '베이지'}">
													<c:set var="cssName" value="beige"/>
												</c:when>	
												<c:when test="${color.colorName eq '그린'}">
													<c:set var="cssName" value="green"/>
												</c:when>	
												<c:when test="${color.colorName eq '핑크'}">
													<c:set var="cssName" value="pink"/>
												</c:when>
												<c:otherwise>
													<c:set var="cssName" value="${color.colorName}"/>
												</c:otherwise>	
											</c:choose>
											<li>
												<a href="#none" class="${cssName}" name="${color.colorName}"><span>${color.colorName}</span></a>
											</li>
										</c:if>
									</c:forEach>
								</ul>
							</div>
						</div>
					</c:if>
					
					<c:if test="${not empty searchApi.materialList}">
						<div class="optionItem optDetail material">
							<div class="optionTit"><strong>소재</strong></div>
							<div class="optionCont">
								<ul class="txtList col">
									<c:forEach var="meter" items="${searchApi.materialList}">
										<c:if test="${meter.materialName ne 'null'}">
											<li>
												<label class="chk_style1 option_style1">
													<em>
														<input type="checkbox" value="${meter.materialName}" name="material">
													</em>
													<span><c:out value="${meter.materialName}"/> </span>
												</label>
											</li>
										</c:if>
									</c:forEach>
								</ul>
							</div>
						</div>
					</c:if>
					
					<c:if test="${not empty searchApi.material1List}">
						<div class="optionItem optDetail material">
							<div class="optionTit"><strong>소재</strong></div>
							<div class="optionCont">
								<ul class="txtList col">
									<c:forEach var="meter" items="${searchApi.material1List}">
										<c:if test="${meter.material1Name ne 'null'}">
											<li>
												<label class="chk_style1 option_style1">
													<em>
														<input type="checkbox" value="${meter.material1Name}" name="material1">
													</em>
													<span><c:out value="${meter.material1Name}"/> </span>
												</label>
											</li>
										</c:if>
									</c:forEach>
								</ul>
							</div>
						</div>
					</c:if>
					
					<div class="optionItem optDetail benefit">
						<div class="optionTit"><strong>혜택</strong></div>
						<div class="optionCont">
							<ul class="txtList col">
								<li>
									<label class="chk_style1 option_style1">
										<em>
											<input type="checkbox" value=""  name="couponYn">
										</em>
										<span id="bene_coupon">쿠폰할인</span>
									</label>
								</li>
								<li>
									<label class="chk_style1 option_style1">
										<em>
											<input type="checkbox" value="" name="deliveryFreeYn">
										</em>
										<span id="bene_deliveryFree">무료배송</span>
									</label>
								</li>
								<li>
									<label class="chk_style1 option_style1">
										<em>
											<input type="checkbox" value="" name="pointSaveYn">
										</em>
										<span id="bene_pointSave">포인트</span>
									</label>
								</li>
								<li>
									<label class="chk_style1 option_style1">
										<em>
											<input type="checkbox" value="" name="presentYn">
										</em>
										<span id="bene_present">사은품</span>
									</label>
								</li>
								<li>
									<label class="chk_style1 option_style1">
										<em>
											<input type="checkbox" value="" name="regularDeliveryYn">
										</em>
										<span id="bene_regularDelivery">정기배송</span>
									</label>
								</li>
								<li>
									<label class="chk_style1 option_style1">
										<em>
											<input type="checkbox" value="" name="offshopPickupYn">
										</em>
										<span id="bene_offshopPickup">매장픽업</span>
									</label>
								</li>
							</ul>
						</div>
					</div>
	
					<button type="button" class="btnMore">전체목록 보기</button>
				</div>
				<div class="checkedTxt">
					<ul id="checkedOption">
			
					</ul>
					<a href="#none" class="btn_reset" onclick="dms.search.option.optionInit('all');">초기화</a>
				</div>
			</div>
		</c:when>
		<c:otherwise>
			<ul class="srchBtns mo_only">
				<li><a href="#none" class="btnCategory"><span>카테고리</span></a></li>
				<li><a href="#none" class="btnBrand"><span>브랜드</span></a></li>
				<li><a href="#none" class="btnDetail"><span>상세검색</span></a></li>
			</ul>
			
			<div class="optionBox">
				<div class="optionItemBox">
					<div class="optionItem category first">
						<div class="optionTit"><strong>카테고리</strong></div>
						<div class="optionCont">
							<%-- <div class="resultCount">총 <em><c:out value="${searchApi.catgtotalCount}"/></em>개 카테고리</div> --%>
							
							<%-- <div class="swiper_wrap">
								<div class="swiper-container searchResultSwiper_searchList_1">
									<div class="swiper-wrapper">
										
										<!-- 9개씩 묶어야 한다. -->
										<li class="swiper-slide">
											<ul class="txtList">
												<c:forEach items="${searchApi.categoryList}" var="category" >
													<li>
														<label class="chk_style1">
															<em>
																<input type="checkbox" value="${category.categoryId}" name="categoryId"/>
															</em>
															<span id="categoryName_${category.categoryId}" data-name="${category.categoryName}"><c:out value="${category.categoryName}"/>
																<i>(<c:out value="${category.categoryCount}"/>)</i>
															</span>
														</label>
													</li>
												</c:forEach>
											</ul>
										</li>
										
										<li class="swiper-slide">
										
										</li>
									</div>
								</div>
							
							</div> --%>
							
							<ul class="txtList">
								<c:forEach items="${searchApi.categoryList}" var="category" >
									<li>
										<label class="chk_style1">
											<em>
												<input type="checkbox" value="${category.categoryId}" name="categoryId"/>
											</em>
											<span id="categoryName_${category.categoryId}" data-name="${category.categoryName}"><c:out value="${category.categoryName}"/>
												<i>(<c:out value="${category.categoryCount}"/>)</i>
											</span>
										</label>
									</li>
								</c:forEach>
							</ul>
							
						</div>
					</div>
					<button type="button" class="btnMore">카테고리 열림/닫힘</button>
				</div>
	
				<div class="optionItemBox">
					<div class="optionItem brand first">
						<div class="optionTit"><strong>브랜드 선택</strong></div>
						<div class="optionCont">
							<%-- <div class="resultCount">총 <em><c:out value="${searchApi.brdtotalCount}"/></em>개 브랜드</div> --%>
							<ul class="txtList">
								<c:forEach var="brand" items="${searchApi.brandList}">
									<li>
										<label class="chk_style1">
											<em>
												<input type="checkbox" value="${brand.brandId}" name="brandId"/>
											</em>
											<span id="brandName_${brand.brandId}" data-name="${brand.brandName}"><c:out value="${brand.brandName}"/></span>
										</label>
									</li>
								</c:forEach>
							</ul>
						</div>
					</div>
					
					<c:if test="${isMobile}">
						<div class="optionItem optDetail re_search">
							<div class="optionTit"><strong>재검색</strong></div>
							<div class="optionCont">
								<div class="inpBox">
									<div class="inputTxt_place1">
										<label>결과내 재검색</label>
										<span>
											<input type="text" value="${search.keyword}" id="rebrowsingKey"/>
										</span>
									</div>
									<a href="javaScript:reSearchKey();" class="btn_style6 btnSrch">검색</a>
								</div>
							</div>	
						</div>
					</c:if>
									
					<c:if test="${param.type ne 'age'}">
						<div class="optionItem optDetail age">
							<div class="optionTit"><strong>월령</strong></div>
							<div class="optionCont">
								<ul class="txtList">
									<c:forEach var="age" items="${searchApi.ageCodeList}">
										<li>
											<label class="chk_style1 option_style1">
												<em>
													<input type="checkbox" value="${age.cd}" name="ageTypeCode">
												</em>
												<span id="ageName_${age.cd}" data-name="${age.name}"><c:out value="${age.name}"/> </span>
											</label>
										</li>
									</c:forEach>
								</ul>
							</div>
						</div>
					</c:if>
	
					<div class="optionItem optDetail gender">
						<div class="optionTit"><strong>성별</strong></div>
						<div class="optionCont">
							<ul class="txtList">
								<li>
									<label class="radio_style1 option_style1">
										<em>
											<input type="radio" value="ALL" name="inpGender">
										</em>
										<span id="gender_ALL">전체</span>
									</label>
								</li>
								<c:forEach var="gender" items="${searchApi.genderCodeList}">
									<li>
										<label class="radio_style1 option_style1">
											<em>
												<input type="radio" name="inpGender" value="${gender.cd}"}/>
											</em>
											<span id="gender_${gender.cd}" data-name="${gender.name}"><c:out value="${gender.name}"/> </span>
										</label>
									</li>
								</c:forEach>
							</ul>
						</div>
					</div>
	
					<div class="optionItem optDetail price">
						<div class="optionTit"><strong>가격</strong></div>
						<div class="optionCont">
							<input type="text" value="0" class="inputTxt_style1" id="lowPrice" OnKeyUp="comma(this)"/>
							<span class="swung">~</span>
							<input type="text" value="0" class="inputTxt_style1" id="highPrice" OnKeyUp="comma(this)"/>
							<a href="javascript:dms.search.option.priceApply();" class="btn_sStyle3  sWhite1">적용</a> 
						</div>
					</div>
	
					<c:if test="${not empty searchApi.colorList}">
						<div class="optionItem optDetail color">
							<div class="optionTit"><strong>컬러</strong></div>
							<div class="optionCont">
								<ul class="colorList">
									<c:forEach var="color" items="${searchApi.colorList}">
										<c:if test="${color.colorName ne 'null'}">
											<c:choose>
												<c:when test="${color.colorName eq '블랙'}">
													<c:set var="cssName" value="black"/>
												</c:when>	
												<c:when test="${color.colorName eq '그레이'}">
													<c:set var="cssName" value="gray"/>
												</c:when>	
												<c:when test="${color.colorName eq '화이트'}">
													<c:set var="cssName" value="white"/>
												</c:when>	
												<c:when test="${color.colorName eq '레드'}">
													<c:set var="cssName" value="red"/>
												</c:when>	
												<c:when test="${color.colorName eq '옐로우'}">
													<c:set var="cssName" value="yellow"/>
												</c:when>	
												<c:when test="${color.colorName eq '오렌지'}">
													<c:set var="cssName" value="orange"/>
												</c:when>	
												<c:when test="${color.colorName eq '블루 '}">
													<c:set var="cssName" value="blue"/>
												</c:when>	
												<c:when test="${color.colorName eq '베이지'}">
													<c:set var="cssName" value="beige"/>
												</c:when>	
												<c:when test="${color.colorName eq '그린'}">
													<c:set var="cssName" value="green"/>
												</c:when>	
												<c:when test="${color.colorName eq '핑크'}">
													<c:set var="cssName" value="pink"/>
												</c:when>
												<c:otherwise>
													<c:set var="cssName" value="${color.colorName}"/>
												</c:otherwise>	
											</c:choose>
											<li>
												<a href="#none" class="${cssName}" name="${color.colorName}"><span>${color.colorName}</span></a>
											</li>
										</c:if>
									</c:forEach>
								</ul>
							</div>
						</div>
					</c:if>
					
					<c:if test="${not empty searchApi.materialList}">
						<div class="optionItem optDetail material">
							<div class="optionTit"><strong>소재</strong></div>
							<div class="optionCont">
								<ul class="txtList col">
									<c:forEach var="meter" items="${searchApi.materialList}">
										<c:if test="${meter.materialName ne 'null'}">
											<li>
												<label class="chk_style1 option_style1">
													<em>
														<input type="checkbox" value="${meter.materialName}" name="material">
													</em>
													<span><c:out value="${meter.materialName}"/> </span>
												</label>
											</li>
										</c:if>
									</c:forEach>
								</ul>
							</div>
						</div>
					</c:if>
					
					<c:if test="${not empty searchApi.material1List}">
						<div class="optionItem optDetail material">
							<div class="optionTit"><strong>소재</strong></div>
							<div class="optionCont">
								<ul class="txtList col">
									<c:forEach var="meter" items="${searchApi.material1List}">
										<c:if test="${meter.material1Name ne 'null'}">
											<li>
												<label class="chk_style1 option_style1">
													<em>
														<input type="checkbox" value="${meter.material1Name}" name="material1">
													</em>
													<span><c:out value="${meter.material1Name}"/> </span>
												</label>
											</li>
										</c:if>
									</c:forEach>
								</ul>
							</div>
						</div>
					</c:if>
					
					<div class="optionItem optDetail benefit">
						<div class="optionTit"><strong>혜택</strong></div>
							<div class="optionCont">
								<ul class="txtList col">
									<li>
										<label class="chk_style1 option_style1">
											<em>
												<input type="checkbox" value=""  name="couponYn">
											</em>
											<span id="bene_coupon">쿠폰할인</span>
										</label>
									</li>
									<li>
										<label class="chk_style1 option_style1">
											<em>
												<input type="checkbox" value="" name="deliveryFreeYn">
											</em>
											<span id="bene_deliveryFree">무료배송</span>
										</label>
									</li>
									<li>
										<label class="chk_style1 option_style1">
											<em>
												<input type="checkbox" value="" name="pointSaveYn">
											</em>
											<span id="bene_pointSave">포인트</span>
										</label>
									</li>
									<li>
										<label class="chk_style1 option_style1">
											<em>
												<input type="checkbox" value="" name="presentYn">
											</em>
											<span id="bene_present">사은품</span>
										</label>
									</li>
									<li>
										<label class="chk_style1 option_style1">
											<em>
												<input type="checkbox" value="" name="regularDeliveryYn">
											</em>
											<span id="bene_regularDelivery">정기배송</span>
										</label>
									</li>
									<li>
										<label class="chk_style1 option_style1">
											<em>
												<input type="checkbox" value="" name="offshopPickupYn">
											</em>
											<span id="bene_offshopPickup">매장픽업</span>
										</label>
									</li>
								</ul>
							</div>
						</div>
					<button type="button" class="btnMore">전체목록 보기</button>
				</div>
				<div class="checkedTxt">
					<ul id="checkedOption">
			
					</ul>
					<a href="#none" class="btn_reset" onclick="dms.search.option.optionInit('all');">초기화</a>
				</div>
			</div>
		</c:otherwise>
	
	</c:choose>
</div>
			