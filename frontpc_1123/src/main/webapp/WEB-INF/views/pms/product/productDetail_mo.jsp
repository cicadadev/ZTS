<%--
	화면명 : 상품상세(mobile)
	작성자 : eddie
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ page import="gcp.pms.model.PmsProduct" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="gcp.common.util.FoSessionUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<%--레코레 개인화추천 --%>
<meta property="rb:type" content="product" />
<meta property="rb:cuid" content="<%=Config.getString("recobell.cuid")%>" />
<meta property="rb:itemId" content="${product.productId}" />
<meta property="rb:itemName" content="${product.name }" />
<!-- <meta property="rb:itemImage" content="{$itemImage}" />
<meta property="rb:itemUrl" content="{$itemUrl}" /> -->
<meta property="rb:originalPrice" content="${product.salePrice }" />
<meta property="rb:salePrice" content="${minSalePrice }" />
<c:if test="${not empty product.displayCategoryIdPath}">
<c:set var="cateDepth" value="${fn:length(fn:split(product.displayCategoryIdPath,','))}"/>
<meta property="rb:category1" content="${fn:split(product.displayCategoryIdPath,',')[0]}" />
</c:if>
<c:if test="${cateDepth > 1}">
<meta property="rb:category2" content="${fn:split(product.displayCategoryIdPath,',')[1]}" />
</c:if>
<c:if test="${cateDepth > 2}">
<meta property="rb:category3" content="${fn:split(product.displayCategoryIdPath,',')[2]}" />
</c:if>
<meta property="rb:brandId" content="${empty product.pmsBrand.erpBrandId ?  product.pmsBrand.brandId : product.pmsBrand.erpBrandId}" />
<%-- <meta property="rb:brandName" content="${product.pmsBrand.name}" /> --%>
<%-- <meta property="rb:regDate" content="${product.pmsBrand.name}" />
<meta property="rb:updateDate" content="${product.pmsBrand.name}" /> --%>
<!-- <meta property="rb:stock" content="{$stock}" />
<meta property="rb:state" content="{$state}" />
<meta property="rb:description" content="{$description}" />
<meta property="rb:extraImage" content="{$extraImage}" />
<meta property="rb:locale" content="{$locale}" /> -->
<script type="text/javascript" src="/resources/js/jquery.countdown.min.js"></script>
<script type="text/javascript" src="/resources/js/common/product.ui.mo.js"></script>
</head>

 <script><%--레코벨 개인화 추천--%>
 
 
	if('${deviceTypeCd}' == 'DEVICE_TYPE_CD.PC'){
		device = 'PW';
	}else if('${deviceTypeCd}' == 'DEVICE_TYPE_CD.APP'){
		device = 'MI';
	}else if ('${deviceTypeCd}' == 'DEVICE_TYPE_CD.MW'){
		device = 'MW';
	}
	
   window._rblqueue = window._rblqueue || [];
  _rblqueue.push(['setVar','cuid','<%=Config.getString("recobell.cuid")%>']);
  _rblqueue.push(['setVar','device', device]);
  _rblqueue.push(['setVar','itemId','${product.productId}']);
   _rblqueue.push(['setVar','userId','${memberNo}']);		// optional
  _rblqueue.push(['setVar','searchTerm','${keyword}']);
  _rblqueue.push(['setVar','babyGender','<%=FoSessionUtil.getRecobellBabyGenderCd()%>']);
  _rblqueue.push(['setVar','babyAgeInMonths','<%=FoSessionUtil.getRecobellBabyMonthCd()%>']);	   
  _rblqueue.push(['track','view']);	
  _rblqueue.push(['track','product']);  /* -- IMPORTANT -- */
  
  
  setTimeout(function() {
    (function(s,x){s=document.createElement('script');s.type='text/javascript';
    s.async=true;s.defer=true;s.src=(('https:'==document.location.protocol)?'https':'http')+
    '://assets.recobell.io/rblc/js/rblc-apne1.min.js';
    x=document.getElementsByTagName('script')[0];x.parentNode.insertBefore(s, x);})();
  }, 0);
</script>  

<script>
var _productId_ = '${product.productId}';
var _initSalePrice = '${initSalePrice}'; //  전체주문금액 초기값

var _minSalePrice = Number('${minSalePrice}');// 상품 가격
var _regularPrice = Number('${product.regularDeliveryPrice}');// 정기배송가
var _pickupSalePrice = Number('${initPickupSalePrice}');// 픽업상품 가격
var _selectSaleproducts = {};// 선택된 단품
var _optionYn = '${product.optionYn}';// 옵션존재여부
var _setProductYn = ('${product.productTypeCd}' == 'PRODUCT_TYPE_CD.SET') ? 'Y' : 'N'; // 세트여부

var _pickupYn =  '${product.offshopPickupYn}'; // 픽업상품여부
var _pickupOnly = "${ product.saleStateCd != 'SALE_STATE_CD.SALE' and product.offshopPickupYn=='Y' ? 'Y' : 'N'}";//픽업 전용상품

var _regularDeliveryYn = '${product.regularDeliveryYn}';

var _couponId = '${product.optimalprice.couponId}';
var __price_mode__ = _pickupOnly=='Y' ? 3 : 1;//1일반가, 2정기배송, 3픽업
var _offshopid_ = '${offshopId}';//
var _dealId = '${product.optimalprice.dealId}';
var _dealStockQty = '${product.optimalprice.dealStockQty}';//딜 구매 가능 수량	
var _maxPersonQty = '${ empty product.personQty ? 0 : product.personQty}';

var _noOptionQty = 0;
var _salePrice = 0;

//옵션 없는 상품 초기값
if (_optionYn == 'N') {
	
	_salePrice = '${product.optimalprice.salePrice}',
	
	_selectSaleproducts['${product.saleproductId}'] = {
		saleproductId : '${product.saleproductId}',
		minSalePrice : _minSalePrice,
		salePrice : _salePrice,
		addSalePrice : Number('${product.optimalprice.addSalePrice}'),
		regularPrice : _regularPrice,
		pickupPrice : _pickupSalePrice,
		getMinPrice : function(){
			return __price_mode__==3 ? _pickupSalePrice : __price_mode__== 2 ? _regularPrice : this.minSalePrice;
		},
		getSalePrice : function(){
			return __price_mode__==3 ? _pickupSalePrice : __price_mode__== 2 ? _regularPrice : this.salePrice;
		}
	};
	// 일반, 옵션없는 상품의 재고
	//_setMaxQty = Number('${empty product.realStockQty ? 0 : product.realStockQty }');
	if(_pickupOnly=='Y'){// 픽업전용일경우 매장 재고.
		_noOptionQty = Number('${ product.pickupMaxQty}');
	}else{
		_noOptionQty = Number('${ product.realStockQty}');
		if( _noOptionQty > _maxPersonQty){// 세트, 일반, 정기배송등(온라인재고)
			_noOptionQty = _maxPersonQty;
		}
	}
}


if(_setProductYn == 'Y' || _optionYn == 'N'){
	//  기본 단품	
	_saleproductId = '${product.saleproductId}';
	
}
if(_setProductYn == 'Y'){
	_selectedSetSaleproducts = {};// 선택된 세트 단품
}

	// 단품 선택
	var selectSaleproduct = function(saleproduct) {
		
		/* //중복체크
		if(!duplSaleproduct(saleproduct)){
			return;
		} */
		//_selectSaleproducts.push(saleproduct);
		saleproduct.qty = 1;// 기본수량 1
	
		if (!pms.detail.addSaleproductOption(saleproduct)) {
			return;
		}
		
		$('[name=selectedSaleproductArea1]').show();
		
		var stockQty = saleproduct.realStockQty;
		if(__price_mode__== 3 ){// 매장픽업일경우
			stockQty = saleproduct.maxShopStockQty;
		}
		
		var html = '';
		html += '<li name="selectedLi_'+saleproduct.saleproductId + '">';
		html += '<span class="op_name">' + saleproduct.name + '</span>';
		html += '<div class="quantity">';
		html += '		<button type="button" class="btn_minus" onclick="pms.detail.qtyChange(\'qty_'
			+ saleproduct.saleproductId + '\' , \''+ stockQty + '\' ,\'N\');">수량빼기</button>';
		html += '		<input type="text" value="1" onkeydown="return common.chkNumKeyDwn(this, event);" onchange="pms.detail.qtyChange(this.id, \''+ stockQty + '\')" saleproductId="'
				+ saleproduct.saleproductId
				+ '" id="qty_'
				+ saleproduct.saleproductId + '">';
		html += '		<button type="button" class="btn_plus" onclick="pms.detail.qtyChange(\'qty_'
					+ saleproduct.saleproductId + '\', \''+ stockQty + '\'  ,\'Y\');">수량추가</button>';
		html += '</div>';
		html += '<b class="price">'+ common.priceFormat(saleproduct.getMinPrice(),true,false) +'</b>';
		html += '<a href="#none" class="btn_del" onclick="pms.detail.delSaleproductOption('
			+ saleproduct.saleproductId + ')">삭제</a>';
		html += '</li>';
		$('[name=selectedSaleproductArea2]').append(html);
	
		$('[name=totalBlock]').show();
		
	}
	// Qna ajax 리스트 콜백
	var listCallback = function(html) {
		$("#qnaDiv").html(html);
		
		/* 상품상세 - Q&A 글 펼쳐보기 : 2016.08.23 */
		$(".mobile .prod_qa_box .list a").off("click").on({
			"click" : function(e) {
				if( $(this).next().css("display") == "none" ){
					$(this).next().show();
				}else{
					$(this).next().hide();
				}

				e.preventDefault();
			}
		});
		
	}
	// 상품평 리뷰 목록 콜백
	var reviewCallback = function(html) {
		$("#reviewList").html(html);
		
		$(".mobile .user_option .title").off("click").on({
			"click" : function() {
				$(this).parent().next().toggle();
			}
		});
	}	
	
	var goCart = function(){
		
		var target = "";
		if(__price_mode__==2){
			target = "CART_TYPE_CD.REGULARDELIVERY";
		}else if(__price_mode__==3){
			target = "CART_TYPE_CD.PICKUP";
		}
		
		ccs.link.order.cart(target);
	}
	
	//  상품평 total count, 평점 조회
	var getProductReviewTotalAndRatings = function(){
		
		var param = {
				productId : _productId_,
			};
		
		$.ajax({
			method : "get",
			url : "/api/pms/product/review/rating/ajax?productId=${product.productId}",
			contentType:"application/json; charset=UTF-8",
		}).done(function(data) {
			
			
			// 토탈 평점
			var totalAvg = Number(data.totalRatingAvg);
			$('#reviewTotalAvg1').html(totalAvg);
			var starPoint = totalAvg * 20;
			$('#reviewTotalStar').attr('style', 'width:'+starPoint+'%');
			
			var ratingsHtml='';
			// 항목별 평점
			for(i = 0 ; i < data.ratingAvgList.length ; i++){
				
				var totalAvg = Number(data.ratingAvgList[i].ratingIdAvg);
				var starPoint = totalAvg * 20;
				
				var starHtml='';
				starHtml+='<li>';
				starHtml+='<strong>'+data.ratingAvgList[i].ratingName+'</strong>';
				starHtml+='<div class="item_star bg_star">';
				starHtml+='<span style="width:'+starPoint+'%;"><em>'+starPoint+'</em></span>';
				starHtml+='</div>';
				starHtml+='</li>';
				ratingsHtml+=starHtml;
			}
			
			$('#reviewRatingStarArea').html(ratingsHtml);
			
		});
	}	
	
	
	var openBuyBoxRegular = function(){
			
		mms.common.loginCheck(function(success){
			if(success){
				openBuyBox('regular');
			}
		});
		
	}
	
	
	
	// 옵션 선택박스 열기
	var openBuyBox = function(type){
		$('.btn_buy_group').show();//닫기버튼 노출
		$('#buy_setup_area').show();
		$('#buy_fixed').removeClass("buy_option_open");
		$('#buy_base').hide();
		
		$('#regularBuyBox').hide();
		$('#normalBuyBox').hide();
		
		$('#pickupBuyBtn').hide();
		$('#normalBuyBtn').hide();
		$('#giftBuyBtn').hide();
		
		
		if(_optionYn=='N'){//세트이거나, 옵션없을경우
			_noOptionQty = Number('${ product.realStockQty}');//픽업재고
		}
		
		if(type=="normal" ){
			
			__price_mode__= 1;
			$('#normalBuyBox').show();
			$('#normalBuyBtn').show();
			
		}else if(type=="gift"){
			__price_mode__= 1;
			$('#normalBuyBox').show();
			$('#giftBuyBtn').show();
			
		}else if(type=="regular"){
			__price_mode__= 2;
			$('#regularBuyBox').show();
		}else if(type == 'pickup'){
			__price_mode__= 3;
			$('#normalBuyBox').show();
			$('#pickupBuyBtn').show();
			
			
			if(_optionYn=='N'){//세트이거나, 옵션없을경우
				_noOptionQty = Number('${ product.pickupMaxQty}');//픽업재고
			}
			
		}
		
		if(_optionYn=='Y'){
			// 옵션콤보 초기화
			pms.detail.initOption();
				
			// 선택단품 초기화
			pms.detail.initSelectedProduct();
			
		}
		
		
		// 객체에 수량 설정
		if('Y'!=_setProductYn && 'Y'==_optionYn){
			 pms.detail.setTotalSalePrice();
		}else{
			var qty = $('#qty_'+_saleproductId).val();
			pms.detail.makeTotalPriceNoOption(Number(qty));
		} 
		
		//옵션목록 조회
		pms.detail.selectPickupMode((type == 'pickup' ? true : false), _productId_);
		
		//구매옵션
		$('#btn_buy_group_Open').hide();
		$('#btn_buy_group_Close').show();
	}
	var closeBuyBox = function(){
		//$('#normalBuyBox').hide();
		//$('#regularBuyBox').hide();
		//
		$('#buy_setup_area').hide();
		//
		$('#buy_fixed').addClass("buy_option_open");;
		$('#buy_base').show();
		
		//구매옵션
		$('#btn_buy_group_Open').show();
		$('#btn_buy_group_Close').hide();
		
	}
	
	
	// 픽업 구매 버튼 클릭
	var pickupCart = function(){
		
		if(common.isEmptyObject(_selectSaleproducts)){
			alert("옵션을 선택해 주세요.");
			return;
		}
		
		var params = {};
		for(key in _selectSaleproducts){
			params[key]= {offshopId : null, selectedQty : _selectSaleproducts[key].qty };
		}
		
		ccs.layer.pickupLayer.open(params, function(data){
			// 콜백 : 모든 단품의 픽업매장 선택완료
		
			for(key in data){
				_selectSaleproducts[key].offshopId = data[key].offshopId;
			}
			// 장바구니 담기
			pms.detail.cart(_productId_,'${product.productTypeCd}','pickup','${styleNo}');
			
		});
	}
	// qna작성 팝업
	var openQnaPopup = function(){
		mms.common.loginCheck(function(success){
			
			if(success){
				$('#qnaPopup').show();
			}	
		});
		
	}
	
	// 상품문의 등록
	var insertProductQna = function() {
		
		if ($("#productQnaForm input[name=title]").val() == '') {
			alert("제목을 입력해 주세요");
			return;
		}
		if ($("#productQnaForm textarea[name=detail]").val() == '') {
			alert("내용을 입력해 주세요");
			return;
		}
		var formData = $("#productQnaForm").serialize();

		$.ajax({
			method : "POST",
			url : "/api/pms/product/qna/save",
			data : formData
		}).done(function(msg) {
			
			closeLayer('qnaPopup');
			
			//초기화
			$("#productQnaForm input[name=saleproductId]").val('');
			$("#productQnaForm input[name=title]").val('');
			$("#productQnaForm textarea[name=detail]").val('');
			pms.common.checkBoxChecked("secretYn", false);
			
			//재조회
			pms.detail.getQnaList(null, _productId_);
		});
	}
	
	var closeLayer = function(id){
		$('#'+id).hide();
		$('div .content').show();
	}
	
	
	/* 작은레이어 팝업 위치 설정 : 2016.08.22 */
	function fnLayerPosition(target_pop) {
		$(target_pop).show();
		$(target_pop).height( $(document).height() );

		var base_top = ($(window).height() - $(" > .box", target_pop).innerHeight()) / 2;
		$(" > .box", target_pop).css({"marginTop" : base_top + $(window).scrollTop() + "px"});
	}

	/* 모바일 스크롤 제어(상품평, QNA) 2016.11.16*/
	if(ccs.common.mobilecheck()) {
		
		$(window).bind("scroll", productInfoListScrollListener);
			
		function productInfoListScrollListener() {
			var target;
			var totalCount;
			if($(".mobile .prod_info_tab").find("li:eq(2)").hasClass("on")){
				target = $("#reviewList");
				totalCount = Number($("[name=reviewTotalCount]").val());
				console.log('test21', target);
			}
			else if($(".mobile .prod_info_tab").find("li:eq(3)").hasClass("on")){
				target = $("#qnaDiv");
				totalCount = Number($("[name=qnaTotalCount]").val());
				console.log('test22', target);
			}
			
			console.log('test2', target);
			
			if(common.isNotEmpty(target)){
				
				var rowCount = target.children("li").length;
				
				var maxPage = Math.ceil(totalCount/10);
				
				var scrollTop = $(window, document).scrollTop();
				var scrollHeight = $(document).height() - $(window).height();
				if (scrollTop >= scrollHeight - 200 && scrollHeight != 0) {
					if(rowCount !=0 && (rowCount < totalCount)){
						
						if ($("#tempLoadingBar").length > 0 ) {
							return;
						}
						
						pms.detail.pagingListCallback(_productId_);
					}
				}
			}
		}
	}
	
	$(document).ready(function() {
	
		// QmA목록 조회
		pms.detail.getQnaList(null, _productId_);
		
		// 상품평 목록 조회
		pms.detail.getProductReview(null, '01', _productId_);
		getProductReviewTotalAndRatings();
	
		// 추천상품
		var swiperFn = function(){
			swiperCon('cartSwiper_pordList', '2'); //
		}
		pms.common.getRecommendationProductList($('[name=categoryBestArea]'), {recType:'a002', size : 10, iids : _productId_}, swiperFn);
		
		swiperCon('prodDetailSwiper_list_1', '1'); // 상품 이미지 롤링
		swiperCon('prodDetailSwiper_banner_1', '1'); //배너 1
		swiperCon('prodDetailSwiper_banner_2', '1'); //배너 2
		
	});

</script>
<style>

.maxW img {max-width:100%;}

</style>
	<jsp:include page="/WEB-INF/views/gsf/layout/common/navi.jsp" flush="false">
		<jsp:param value="상품상세" name="pageNavi"/>
		<jsp:param value="productDetail" name="type"/>
	</jsp:include>	
	<div class="inner">
		<input type="hidden" name="currentReviewPage" value="1" />
		<input type="hidden" name="currentQnaPage" value="1" />
		<div class="productDetail">
		
			<!-- ### 상세 제품 이미지 : 2016.08.19 수정 ### -->
			<div class="prod_view" id="prod_view">
			<c:if test="${not empty product.spsDeal.endDt and product.optimalprice.dealId eq '1' }">
				<div class="prod_count_time">
					<span>쇼킹제로</span>
					<dl>
						<dt>오늘오픈</dt>
						<dd id="shockDate">
							<script>pms.countdown("shockDate", "${product.spsDeal.endDt}")</script>
						</dd>
					</dl>
				</div>
			</c:if>
				<ul class="sns">
					<li><a href="#none" onclick="pms.updateWishlist('${product.productId}', this);" class="btn_jjim ${not empty product.wishlistNo ? 'active' : ''}">찜</a></li>
					<li><button type="button" class="btn_snsInfo">sns 공유</button></li>
				</ul>
				<div class="swiper-container prodDetailSwiper_list_1">
					<ul class="swiper-wrapper">
						<c:forEach items="${product.pmsProductimgs }" var="images">
							<li class="swiper-slide">
							<tags:prdImgTag productId="${product.productId}" size="326" className="" seq="${images.imgNo }" alt="${product.name }" />
							</li>
						</c:forEach>
					</ul>

					<!-- Add Pagination -->
      					<div class="swiper-pagination tp1"></div>
				</div>				

				<ol class="photo_dot dot_box">
				<c:forEach items="${product.pmsProductimgs}" varStatus="index">
					<li ${index.count eq 1 ? 'class="on"' : '' }>
						<button type="button">${index.count}</button>
					</li>
				</c:forEach>	
				</ol>
			</div>
			<!-- ### //상세 제품 이미지 : 2016.08.19 수정 ### -->

			<!-- ### 상세 제품 내용 ### -->
			<div class="prod_info">
				<div class="detail_box">
					<strong class="name">
						<c:if test="${not empty product.pmsBrand.name}">[${product.pmsBrand.name }] </c:if>${product.name }
					</strong>
					<span class="price">
					<c:set value="${ minSalePrice < product.optimalprice.listPrice}" var="listPriceDisplay"/>
						<span class="price_info">
							<c:if test="${listPriceDisplay eq true}"><i><fmt:formatNumber value="${product.optimalprice.listPrice }" groupingUsed="true" /> 원</i><br></c:if>
							<strong><fmt:formatNumber value="${minSalePrice }" groupingUsed="true" /><em>원</em></strong>
						<c:if test="${ not empty product.optimalprice.couponId or not empty product.optimalprice.dealId }">
							<a href="#none" class="btn_infor btn_priceGuide">가격적용 안내</a>
						</c:if>
						<c:choose>
							<c:when test="${product.optimalprice.dealId eq '3'}"><span class="emph">프리미엄가</span></c:when>
							<c:when test="${product.optimalprice.dealId eq '4'}"><span class="emph">임직원가</span></c:when>
							<c:when test="${ not empty product.optimalprice.couponId }">
								<c:if test="${product.optimalprice.dcApplyTypeCd eq 'DC_APPLY_TYPE_CD.AMT' }"><c:set var="couponType" value="원"/></c:if>
								<c:if test="${product.optimalprice.dcApplyTypeCd eq 'DC_APPLY_TYPE_CD.RATE' }"><c:set var="couponType" value="%"/></c:if>
								<span class="emph">${ product.optimalprice.dcValue }${couponType}쿠폰적용</span>
							</c:when>
						</c:choose>							
						<c:if test="${not empty product.unitQty and product.unitQty > 1 }">
							<c:set var="unitPrice" value="${ minSalePrice div (empty product.unitQty ? 1 : product.unitQty)}"/>
							<br/><small>(1개당 <fmt:formatNumber value="${unitPrice}" groupingUsed="true" />원)</small>
						</c:if>
						</span>
						<c:if test="${minSalePrice < product.optimalprice.listPrice and product.optimalprice.listPrice > 0 }">
						<u>
							<tags:dcRate stdPrice="${product.optimalprice.listPrice }" dcPrice="${minSalePrice}" var="rate" />
							<c:if test="${rate != 0}">
								${rate}<em>%</em>
							</c:if>
						</u>
						</c:if>
					</span>					
				</div>
			</div>
			<!-- ### //상세 제품 내용 ### -->

			
<c:if test="${not empty bannerList }">				
			<!-- 16.10.31 : 배너 슬라이드 -->
			<div class="swiper-container prodDetailSwiper_banner_1">
				<ul class="bridge_banner swiper-wrapper">
<c:forEach items="${bannerList}" var="banner" varStatus="index">		
<c:if test="${not empty banner.img2}">		
					<li class="swiper-slide">
						<a href="#none">
							<img src="${_IMAGE_DOMAIN_}${banner.img2}" alt="${banner.text2}">
						</a>
					</li>
</c:if>					
</c:forEach>					
				</ul>
			</div>
								
</c:if>
			<!-- ### 탭 메뉴 ### -->
			<ul class="tabBox1 prod_info_tab">
				<li class="on"><a href="#none">상세정보</a></li>
				<li>
					<a href="#none">구매/배송정보</a>
				</li>
				<li>
					<a href="#none">상품평<em id="reviewTotalCount1">(0)</em></a>
				</li>
				<li>
					<a href="#none">Q&amp;A<em name="qnaTotal">(0)</em></a>
				</li>
			</ul>
			<!-- ### //탭 메뉴 ### -->

			<!-- ### 상세정보 그룹 ### -->
			<div class="tab_con tab_conOn">
				<div class="infomation">
					<!-- 1120 :  수정 -->
					<div class="info_block first">
						<c:if test="${product.regularDeliveryYn eq 'Y' }">
							<dl>
								<dt>정기배송가</dt>
								<dd>
									<fmt:formatNumber value="${product.regularDeliveryPrice }" groupingUsed="true" />원 <a href="#none" class="btn_infor btn_repeatInfo">?</a>
								</dd>
							</dl>
						</c:if>
						<c:if test="${product.offshopPickupYn eq 'Y' }">
							<dl>
								<dt>매장픽업가</dt>
								<dd><tags:dcPrice price="${product.salePrice}" dcRate="${product.offshopPickupDcRate}" format="true"/>원
								</dd>
							</dl>
						</c:if>	

						<div class="delivery_info">
							<dl>
								<dt>배송비</dt>
								<dd>
									<c:choose>
										<c:when test="${product.optimalprice.deliveryFeeFreeYn eq 'Y' or empty product.ccsDeliverypolicy.deliveryFee or product.ccsDeliverypolicy.deliveryFee eq '0'}">
											무료배송
										</c:when>
										<c:otherwise>
											<fmt:formatNumber value="${ product.ccsDeliverypolicy.deliveryFee }" groupingUsed="true" />원 (<fmt:formatNumber value="${ product.ccsDeliverypolicy.minDeliveryFreeAmt }" groupingUsed="true" />원 이상 무료배송)<br>
										</c:otherwise>
									</c:choose>	
									<a href="#none" class="btn_delivery">더보기</a>
								</dd>
							</dl>
							<dl class="more">
								<dt>배송방법</dt>
								<dd>
									택배배송 (<tags:codeName code="${product.ccsDeliverypolicy.deliveryServiceCd }"/>)
									<br>
									<c:if test="${product.regularDeliveryYn eq 'Y' }">
										정기배송<br>
									</c:if>
									<c:if test="${product.offshopPickupYn eq 'Y' }">
										매장픽업<br>
									</c:if>
									<p>* 지역에 따라 추가배송비 발생할 수 있습니다.</p>
									<button type="button" class="btn_sStyle1 sWhite2">묶은배송상품보기</button>
								</dd>
							</dl>
						</div>
					</div>	
					
					<div class="info_block">
						
<c:if test="${product.totalPoint > 0}">
					<dl>
						<dt>매일포인트</dt>
						<dd>
							최대 <fmt:formatNumber value="${product.totalPoint }" groupingUsed="true" />P 적립
							<a href="#none" class="btn_infor btn_maeilPoint">안내</a>
						</dd>
					</dl>
</c:if>
<c:if test="${not empty product.spsCardpromotion.html1 }">
					<dl>
						<dt>무이자 할부</dt>
						<dd>
							${product.spsCardpromotion.name }
							<a href="#none" class="btn_installment">더보기</a>
						</dd>
					</dl>
</c:if>				
<c:if test="${not empty product.origin}">
					<dl>
						<dt>원산지</dt>
						<dd>${product.origin }</dd>
					</dl>
</c:if>		
					<dl>
						<dt>상품코드</dt>
						<dd>${product.productId }</dd>
					</dl>
<c:if test="${ not empty product.pmsBrand }">
					<dl>
						<dt>브랜드</dt>
						<dd>
							${product.pmsBrand.name }
							<button type="button"  onclick="brand.template.main('${product.pmsBrand.brandId}');" class="btn_sStyle1 btn_brandZone">브랜드 더보기</button>
						</dd>
					</dl>
</c:if>
<c:if test="${product.wrapYn eq 'Y' }">
					<dl class="dl_gift">
						<dt>
							<span class="icon_gift">
								선물가능
							</span>
						</dt>
						<dd>
							선물 포장이 가능합니다.
							<a href="#none" class="btn_infor btn_giftInfo">안내</a>
						</dd>
					</dl>
</c:if>
					</div>
					<div class="edit_box">
<%-- MD 공지 --%>					
<c:if test="${not empty product.ccsNotice }">
						<h3>MD공지</h3>
						<div class="md_notice">${product.ccsNotice.detail }</div>
</c:if>
<%-- 셀링 포인트 --%>						
					<c:if test="${not empty product.sellingPoint }">
						<h3>꼼꼼체크✔</h3>
						<div class="selling">${product.sellingPoint }</div>					
						
					</c:if>
<%--상품 기술서 --%>
						<div class="guide maxW">
							${product.detail }
						</div>
					</div>
				</div>
<%-- 기획전 배너 --%>
<c:if test="${not empty product.exhibits}">
				<div class="banner_zone swiper-container prodDetailSwiper_banner_2">
					<ul class="banner_list swiper-wrapper">
<c:forEach var="exhibit" items="${product.exhibits }">						
						<li class="swiper-slide">
							<a href="#none">
								<img src="${_IMAGE_DOMAIN_}${exhibit.img2 }" alt="${exhibit.name }">
							</a>
						</li>
</c:forEach>				
      				</ul>
      				<div class="swiper-pagination tp1"></div>

				</div>				
</c:if>
				<!-- ### 추천 상품 ### -->
				<div class="rolling_box">
					<h2 class="tit_style2">추천 상품</h2>
					<div class="prodSwiper swiper-container cartSwiper_pordList">
						<ul class="swiper-wrapper"  name="categoryBestArea">
						</ul>
					</div>

<!-- 					<div class="paginate">
						<button class="prev">이전</button>
						<span><em class="current">1</em>/<span class="total">0</span></span>
						<button class="next">다음</button>
					</div> -->
				</div>
				<!-- ### //추천 상품 ### -->
			</div>
			<!-- ### //상세정보 그룹 ### -->

			<!-- ### 구매/배송 정보 그룹 ### -->
			<div class="tab_con">
				<div class="prod_delivery">
					<div class="zone zone_open">
						<a href="#none" class="sub_title">
							상품정보 제공고시
						</a>

						<div class="article_box">
							<table class="tb_type1">
								<colgroup>
									<col class="col1">
									<col class="col2">
								</colgroup>
								<tbody>
								<c:forEach var="notice" items="${product.pmsProductnotices}">
									<tr>
										<th>${notice.pmsProductnoticefield.title }</th>
										<td>${notice.detail }</td>
									</tr>
								</c:forEach>	
								</tbody>
							</table>
						</div>
					</div>

					<div class="zone">
						<a href="#none" class="sub_title">
							배송/교환/반품정보
						</a>

						<div class="article_box">
						${product.claimInfo}
						</div>
					</div>
				</div>
			</div>
			<!-- ### //구매/배송 정보 그룹 ### -->

			<!-- ### 상품평 그룹 ### -->
			<div class="tab_con">
				<input type="hidden" id="reviewSearchType">
				<!-- ### 주의 사항 ### -->
				<div class="point_dsc">
					<ul>
						<li>첫번째 상품평 작성 시 <em>매일포인트 1000P</em> 지급</li>
						<li>일반상품평 작성 시 <em>매일포인트 100P + 당근500개</em> 지급</li>
						<li>포토상품평 작성 시 <em>매일포인트 100P + 당근 1,000개</em> 지급</li>
					</ul>
					<div class="help">
						첫상품평 작성 유의사항
						<a href="#none" class="btn_infor btn_reviewInfo">안내</a>
					</div>
					<a href="javascript:ccs.link.mypage.activity.review();" class="btn_imgStyle1 btnReview">
						<em>상품평 쓰기</em>
					</a>
				</div>
				<!-- ### //주의 사항 ### -->

				<!-- ### 상품평 탭 ### -->
				<!-- <ul class="tabBox prod_cmd_tab num3"> -->
				<ul class="tabBox tab3ea tp1">
					<li class="on">
						<a href="javascript:pms.detail.getProductReview('all','','${product.productId}');">
							전체 <em id="reviewTotalCount2">(0)</em>
						</a>
					</li>
					<li>
						<a href="javascript:pms.detail.getProductReview('img','','${product.productId}');">
							포토 <em id="reviewPhotoCount">(0)</em>
						</a>
					</li>
					<li>
						<a href="javascript:pms.detail.getProductReview('permit','','${product.productId}');">
						체험단 후기  <em id="reviewRepermitCount">(0)</em></a>
					</li>							
				</ul>
				<!-- ### //상품평 탭 ### -->

				<!-- ### 상품평 ### -->
				<div class="prod_review">
					<div class="box">
						<dl class="jumsu">
							<dt>
								총 평점 <em id="reviewTotalAvg1">(0)</em>
							</dt>
							<dd>
								<div class="total_star bg_star">
									<span style="width:0%;" id="reviewTotalStar">
										<em>0</em><!-- 반별 9.26 :: 9/2 = 4 :: 간격 2.24 :: 9.26 * 9 = 83.34 + (2.24*4) -->
									</span>
								</div>
							</dd>
						</dl>

						<ul class="total_item" id="reviewRatingStarArea">
						</ul>

						<div class="select_box1">
							<label>최근 등록순</label>
							<select onchange="javascript:pms.detail.getProductReview('type',this.value,'${product.productId}');">
								<option value="01">최근 등록순</option>
								<option value="02">평점 높은순</option>
								<option value="03">평점 낮은순</option>
							</select>
						</div>
						<ul class="comment cmd_on" id="reviewList">
						</ul>
					</div>
				</div>
				<!-- ### //상품평 ### -->
			</div>
			<!-- ### //상품평 그룹 ### -->

			<!-- ### Q&A 그룹 ### -->
			<div class="tab_con myqa">
				<div class="prod_qa_box">
					<div class="secret_chk">
						<label class="chk_style1">
							<em>
								<input type="checkbox" value="Y" id="secretCheck" onclick="pms.detail.getQnaList({secret : this.checked},'${product.productId}')">
							</em>
							<span>비밀글 제외</span>
						</label>

						<a href="#none" class="btn_imgStyle1 btn_qaWrite" onclick="openQnaPopup()">
							<em>Q&amp;A 쓰기</em>
						</a>
					</div>
					<ul class="div_tb_tbody3" id="qnaDiv">
					</ul>
				</div>
			</div>
			<!-- ### //Q&A 그룹 ### -->



			<!-- ### 구매하기 옵션 설정 ### -->
			<div class="buy_fixed buy_option_open" id="buy_fixed">
				<div class="zone">
					<button type="button" style="display:none" class="btn_buy_group" id="btn_buy_group_Close" onclick="closeBuyBox();">
						<em>구매 옵션</em>
					</button>
					<button type="button" style="display:block" class="btn_buy_group" id="btn_buy_group_Open" onclick="openBuyBox('normal')">
						<em>구매 옵션</em>
					</button>

					<!-- ### 품절 상품 ### -->
					<div class="sold_out" style="display:none;">
						이 상품은 품절입니다.
					</div>
					<!-- ### //품절 상품 ### -->
					<div class="buy_base on_base" id="buy_base">	
					
			
<c:if test="${product.saleStateCd !='SALE_STATE_CD.SALE' and product.offshopPickupYn eq 'Y'}">
	<c:set var="pickupOnly" value="Y"/>	
</c:if>							
						
						
<%--매장픽업 전용 --%>			
<c:choose>
<c:when test="${pickupOnly eq 'Y' }">
						<div class="btnR btn1ea">
							<div><a href="#none" class="btn_bStyle1 sWhite1" onclick="openBuyBox('pickup');">매장픽업</a></div>
						</div>
</c:when>
<c:otherwise>
<c:if test="${product.giftYn eq 'Y' }">
						<div class="btnL">
							<a href="#none" onclick="openBuyBox('gift');" class="btn_gift">선물</a>
						</div>
</c:if>
<c:set var="btnCnt" value="1" />					
<c:if test="${product.regularDeliveryYn eq 'Y' }"><%--정기배송 --%>
	<c:set var="btnCnt" value="${btnCnt+1}"/>	
</c:if>
<c:if test="${product.offshopPickupYn eq 'Y' }"><%--매장픽업 --%>
	<c:set var="btnCnt" value="${btnCnt+1 }"/>	
</c:if>					
						
						<div class="btnR btn${btnCnt}ea">
							
							<c:if test="${product.offshopPickupYn eq 'Y' }">
								<div><a href="#none" class="btn_bStyle1 sWhite1" onclick="openBuyBox('pickup');" >매장픽업</a></div>
							</c:if>	
							<c:if test="${product.regularDeliveryYn eq 'Y' }">
								<div><a href="#none" onclick="openBuyBoxRegular()" class="btn_bStyle1 btn_repeat">정기배송</a></div>
							</c:if>	
							<div><a href="#none" onclick="openBuyBox('normal')" class="btn_bStyle1 sPurple1 btn_buy">구매하기</a></div>
						</div>
</c:otherwise>
</c:choose>
						
					</div>

					<!-- ### 상세옵션 ### -->
					<div class="buy_setup" style="display:none;" id="buy_setup_area">
<c:choose>	
<%--일반상품 옵션 선택 영역 --%>
<c:when test="${product.optionYn eq 'Y' and product.productTypeCd ne 'PRODUCT_TYPE_CD.SET' }">	
						<div class="optionOuter">				
							<div class="sel_box">
<c:forEach var="optionName" items="${product.pmsProductoptions }" varStatus="index">						
								<div class="select_box1">
									<label>선택하세요</label>
									<select optionName="${ optionName.optionName}" name="prdOption_${index.count }"
											onchange="pms.detail.selectProductOption(this.name, ${fn:length(product.pmsProductoptions)},'${product.productId }')">
											<option value="">선택하세요</option>
										<c:forEach var="option" items="${optionName.optionValues}">
											<option value="${option.optionValue }" ${option.realStockQty==0 ? 'disabled' : '' }>${option.optionValue }</option>
										</c:forEach>											
									</select>
								</div>
</c:forEach>						
							</div>
							<div class="ea_box"  name="selectedSaleproductArea1" style="display: none">
								<ul name="selectedSaleproductArea2">
								</ul>
							</div>
						</div>
</c:when>
<%--옵션 없거나,  세트상품 수량 선택 영역--%>		
<%--세트상품 단품 선택 영역 --%>
<c:otherwise>
						<div class="optionOuter set">
	<c:if test="${product.productTypeCd eq 'PRODUCT_TYPE_CD.SET' }">
		
	<c:forEach var="setp" items="${product.pmsSetproducts }" varStatus="index">
						
							<%-- <div class="mt">${setp.name}</div> --%>
							<div class="sel_box">
							<div class="select_box1" ${fn:length(setp.pmsSaleproducts) < 2 ? 'style="display:none"': ''} >
								<label>선택하세요</label> 
								<select onchange="" id="subproducts_${index.count}" subProductId="${setp.subProductId }" >
									<option value="">선택하세요</option>
									<c:forEach var="saleproduct" items="${setp.pmsSaleproducts}">
										<option maxQty="${saleproduct.realStockQty }" ${fn:length(setp.pmsSaleproducts)==1 ? 'selected':''} value="${saleproduct.saleproductId }"  
										 ${saleproduct.realStockQty eq 0 ? 'disabled' : '' } >${saleproduct.name }${saleproduct.realStockQty eq 0 ? ' (품절)' : '' }</option>
									</c:forEach>
								</select>
								<%--단일 옵션일 경우 --%>
							</div>
							</div>
														
	</c:forEach>
	</c:if>
							<div class="ea_box">
								<ul>	
								<li>
									<div class="quantity">
										<button type="button" class="btn_minus" onclick="pms.detail.qtyChange('qty_${product.saleproductId}', _noOptionQty, 'N');">수량빼기</button>
										<input value="1" type="text" onkeydown="return common.chkNumKeyDwn(this, event);" onchange="pms.detail.qtyChange('qty_${product.saleproductId}', _noOptionQty)" 
										     price="${minSalePrice }" saleproductId="${product.saleproductId}" id="qty_${product.saleproductId}">
										<button type="button" class="btn_plus" onclick="pms.detail.qtyChange('qty_${product.saleproductId}', _noOptionQty,'Y');">수량추가</button>
									</div>
									<b class="price" name="totalPrice"><fmt:formatNumber value="${initSalePrice }" groupingUsed="true" />원</b>
								</li>
								</ul>
							</div>
						</div>
</c:otherwise>
</c:choose>	
					<!-- ### 장바구니, 바로구매 ### -->
					<div class="buy_type1" id="normalBuyBox">
						<div class="total">
							<span class="price"  name="totalBlock" ${product.optionYn eq 'Y' ? 'style=display:none' : ''}>
								총 <em name="totalPrice"><fmt:formatNumber value="${initSalePrice }" groupingUsed="true" /></em> 원
							</span>
<c:if test="${product.offshopPickupYn eq 'Y' }">
<!-- 								<label class="chk_style1">
									<em>
										<input type="checkbox" value="" onclick="checkOffshopPickup(this.checked)" id="btnStorePickup" class="pick_up_check">
									</em>
									<span>매장에서 픽업하기</span>
								</label> -->
</c:if>								
						</div>
						<div class="btn_wrapC btn2ea" id="normalBuyBtn">
						<c:set value="100" var="btnWidth"/>
						<c:if test="${ not (not empty  childrenDealId and product.optimalprice.dealId eq childrenDealId )  }">
							<c:set value="49" var="btnWidth"/>
							<a href="#none" class="btn_bStyle1 sWhite1" onclick="pms.detail.cart('${product.productId}','${product.productTypeCd}', '', '${styleNo}')">장바구니</a>
						</c:if>	
							<a href="#none" style="width:${btnWidth}%" class="btn_bStyle1 sPurple1" onclick="pms.detail.order('${product.productId}','${product.productTypeCd}','','${styleNo}')">바로구매</a>
						</div>
						<div class="btn_wrapC btn1ea" id="pickupBuyBtn" style="display:none;margin-top:0px">
							<a href="#none" onclick="pickupCart()" class="btn_bStyle1 btnStoreCart">매장픽업 장바구니</a>
						</div>			
						<div class="btn_wrapC btn1ea" id="giftBuyBtn" style="display:none;margin-top:0px">
							<a href="#none" class="btn_bStyle1 sPurple1" onclick="pms.detail.order('${product.productId}','${product.productTypeCd}','Y','${styleNo}')">선물하기</a>
						</div>											
					</div>
						<!-- ### //장바구니, 바로구매 ### -->

					<!-- ### 정기배송 장바구니 ### -->
					<div class="buy_type2" id="regularBuyBox">
						<div class="total">
							<span class="price"  ${product.optionYn eq 'Y' ? 'style=display:none' : ''}>
								총 <em name="totalPrice">|<fmt:formatNumber value="${product.regularDeliveryPrice}" groupingUsed="true" />|</em> 원
							</span>
						</div>


						<div class="repeat">
							<dl>
								<dt>배송주기/횟수</dt> <!-- 16.09.23 : 텍스트 수정 -->
								<dd>
									<div class="select_box1">
										<label id="deliveryPeriodCdLabel">일주일에 두 번</label>
										<select id="deliveryPeriodCd">
											<option value="1">1주일에 한 번</option>
											<option value="2">2주일에 한 번</option>
											<option value="3">3주일에 한 번</option>
											<option value="4">4주일에 한 번</option>
										</select>
									</div>

									<div class="select_box1">
										<label id="deliveryCntLabel">${product.regularDeliveryMinCnt}</label> 
										<select id="deliveryCnt" >
											<c:forEach var="cnt" begin="${product.regularDeliveryMinCnt}" end="${product.regularDeliveryMaxCnt}">
											<option value="${cnt }">${cnt }</option>
											</c:forEach>
										</select>
									</div>
								</dd>
							</dl>

							
						</div>
									
						<div class="btn_wrapC btn1ea">
							<a href="#none" onclick="pms.detail.cart('${product.productId}','${product.productTypeCd}','r','${styleNo}')"class="btn_bStyle1">정기배송 장바구니</a>
						</div>
					</div>
					<!-- ### //정기배송 장바구니 ### -->
					<!-- ### //선물하기 ### -->
					</div>
					<!-- ### 상세옵션 ### -->
				</div>
			</div>
			<!-- ### //구매하기 옵션 설정 ### -->
		</div>
	</div>
<c:if test="${not empty product.spsCardpromotion.html1}">
	
		<div class="pop_wrap layer_card">
			<div class="pop_inner">
				<div class="pop_header">
					<h3 class="tit">카드사별 무이자 혜택 안내</h3>
				</div>
				<div class="pop_content">
					<strong class="tit">무이자카드 안내</strong>
					<div class="info">
						${product.spsCardpromotion.html1 }
					</div>
					<p class="txt">
						무이자 혜택은 쿠폰 및 포인트 결제를 제외한 신용카드로 
						결제하는 최종 결제금액 기준으로 제공됩니다. <br>
						기준금액 미만 결제 시, 무이자 적용이 안되는 상품과 
						함께 결제 시, 무이자 적용되지 않는 카드로 결제 시 혜택
						이 적용되지 않습니다.
					</p>
				</div>
				<button type="button" class="btn_x pc_btn_close">닫기</button>
			</div>
		</div>
</c:if>		

<div class="layer_style1 sLayer_maeil">
	<div class="box">
		<div class="conArt">
			<strong class="title">매일포인트 추가 적립 안내</strong>

			<div class="conBox">
				<p class="txt_box">
					상품 구매 시 현금처럼 사용 가능한 매일 포인트를 적립해 드립니다.
				</p>
				<div class="txt_box">
					포인트는 결제 금액의 최대 1%까지 적립되며, 주문 시 결제 금액에 따라 표시 금액보다 적을 수 있습니다.
				</div>
			</div>
		</div>
		<button type="button" class="btn_close">레이어팝업 닫기</button>
	</div>
</div>

<div class="layer_style1 sLayer_review" >
	<div class="box">
		<div class="conArt">
			<strong class="title">상품평 작성 유의사항</strong>

			<div class="conBox">
				<ul class="txt_list txt_list_bgWhite">
					<li>
						모든 상품평은 구매자에 한해 작성 가능합니다.
					</li>
					<li>
						첫번째 상품평은 상품평 글자수 100자 이상인 경우에만 포인트가 지급됩니다.
					</li>
					<li>
						구매 상품평은 상품평 글자수 50자 이상인 경우만 포인트가 지급되며, 한 상품당 1회만 지급됩니다. (같은 상품을 2개 이상 구매하셔도 주문번호가 같은 경우 1회만 지급됩니다.)
					</li>
					<li>
						구매 상품평은 배송완료 후 작성 가능하며, 매일포인트는 상품평 작성 다음날 지급됩니다.
					</li>
					<li>
						포토 상품평 작성 시 블로그나 카페에서 글과 이미지를 복사해서 붙일 경우, 상품평에 제대로 등록되지 않으며, 이미지의 경우 엑박처리 되므로, 상품평 작성란에 직접 등록해주시기 바랍니다.
					</li>
					<li>
						게재하신 상품평의 저작권은 제로투세븐에 귀속되며, 상품과 관련 없는 내용은 임의 삭제될 수 있습니다.
					</li>
				</ul>
			</div>
		</div>
		<button type="button" class="btn_close">레이어팝업 닫기</button>
	</div>
</div>

<div class="layer_style1 sLayer_repeat">
	<div class="box">
		<div class="conArt">
			<strong class="title">정기배송 신청 안내</strong>

			<div class="conBox">
				<div class="txt_box">
					정기배송은 자주 구매하는 상품을 보다 저렴하게 정기적으로 원하는 요일에 알아서 배송 해드리는 서비스입니다.<br>
					지정한 신용카드로 자동 결제되어 상품을 받아 볼 수 있고, 반품 및 해지가 가능합니다.
				</div>
			</div>
		</div>
		<button type="button" class="btn_close">레이어팝업 닫기</button>
	</div>
</div>

	<!-- ### 멤버십 혜택 안내 레이어 : 2016.08.22 추가 ### -->
		<div class="layer_style1 sLayer_member">
			<div class="box">
				<div class="conArt">
					<strong class="title">멤버십 혜택가 안내</strong>

					<div class="conBox">
						<div class="txt_box">
							회원가입 후 고객정보 인증을 통해 입장 가능한 회원전용 혜택가로 등급에 따라 가격이 다를 수 있습니다.
						</div>
						<ul class="txt_list">
							<li>
								VIP 등급  : 31,900원
							</li>
							<li>
								Gold 등급 : 31,900원
							</li>
							<li>
								Silver 등급 : 31,990원
							</li>
							<li>
								Family 등급 : 33,990원
							</li>
						</ul>
					</div>
				</div>
				<button type="button" class="btn_close">레이어팝업 닫기</button>
			</div>
		</div>
		<!-- ### //멤버십 혜택 안내 레이어 : 2016.08.22 추가 ### -->


		<!-- ### 선물포장 안내 레이어 : 2016.08.22 추가 ### -->
		<div class="layer_style1 sLayer_gift">
			<div class="box">
				<div class="conArt">
					<strong class="title">선물포장 안내</strong>

					<div class="conBox">
						<ul class="txt_list txt_list_bgWhite">
							<li>
								선물포장은 포장 1건당 1,000원의 비용이 책정되는 유료서비스입니다.
							</li>
							<li>
								선물포장은 부피 기준으로 포장되므로,부피가 크거나 개별 케이스가 있어 포장이 용이하지 않은 상품에는 제공되지 않습니다.
							</li>
							<li>
								선물포장비는 박스당 비용이 부과되므로 여러 개 주문으로 기준부피 초과시 2개의 박스로 분리 포장되며, 포장비가 추가로 발생 할 수 있습니다.
							</li>
						</ul>
						<div class="exGift">
							<img src="/resources/img/mobile/bg/sLayer_gift.jpg" alt="" />
						</div>
					</div>
				</div>
				<button type="button" class="btn_close">레이어팝업 닫기</button>
			</div>
		</div>
		<%--가격안내  --%>
	<div class="layer_style1 sLayer_priceGuide">
		<div class="box">
			<div class="conArt">
				<strong class="title">가격적용 안내</strong>

				<div class="conBox">
					<dl class="price_guide">
						<dt>- 판매가  :</dt>
						<dd><fmt:formatNumber value="${product.optimalprice.listPrice}" groupingUsed="true" />원</dd>
						<dt>-
						<c:choose>
							<c:when test="${not empty product.optimalprice.dealTypeCd }">
							<tags:codeName code='${product.optimalprice.dealTypeCd}'/>
							</c:when>
							<c:when test="${empty product.optimalprice.dealTypeCd and not empty product.optimalprice.couponId }">
							쿠폰적용가</c:when>									
						</c:choose> :							
						</dt>
						<dd><fmt:formatNumber value="${minSalePrice}" groupingUsed="true" />원</dd>
					</dl>
				</div>
			</div>
			<button type="button" class="btn_close">레이어팝업 닫기</button>
		</div>
	</div>
	
	<div class="layer_style1 sLayer_cart" style="display:none;" id="sLayer_cart">
		<div class="box">
			<div class="conArt">
				<strong class="title">장바구니</strong>

				<div class="conBox">
					<p>상품이 장바구니에 담겼습니다.</p>
					<span>장바구니로 이동하시겠습니까?</span>
				</div>
			</div>
			<div class="btn_wrapC btn2ea">
				<a href="#none" class="btn_mStyle1 sGray1" onclick="goCart()">장바구니 이동</a>
				<a href="#none" class="btn_mStyle1 sPurple1" onclick="$('#sLayer_cart').hide()">쇼핑 계속하기</a>
			</div>
			<button type="button" class="btn_close">레이어팝업 닫기</button>
		</div>
	</div>	
