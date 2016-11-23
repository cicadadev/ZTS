<%--
	화면명 : 상품상세(pc)
	작성자 : eddie
 --%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="intune.gsf.common.utils.Config" %>
<%@ page import="gcp.pms.model.PmsProduct" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="gcp.common.util.PmsUtil" %>
<%@ page import="gcp.common.util.FoSessionUtil" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<%@ taglib prefix="tags" uri="kr.co.intune.commerce.common.tags"%>
<head>
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
<script type="text/javascript" src="/resources/js/common/product.ui.pc.js"></script>
</head>
<!-- 
<script language='javascript'>
var _JV="AMZ2013010701";//script Version
var _UD='undefined';var _UN='unknown';
function _IDV(a){return (typeof a!=_UD)?1:0}
var _CRL='http://'+'gtc3.acecounter.com:8080/';
var _GCD='AS2A40251464922';
if( document.URL.substring(0,8) == 'https://' ){ _CRL = 'https://gtc3.acecounter.com/logecgather/' ;};
if(!_IDV(_A_i)) var _A_i = new Image() ;if(!_IDV(_A_i0)) var _A_i0 = new Image() ;if(!_IDV(_A_i1)) var _A_i1 = new Image() ;if(!_IDV(_A_i2)) var _A_i2 = new Image() ;if(!_IDV(_A_i3)) var _A_i3 = new Image() ;if(!_IDV(_A_i4)) var _A_i4 = new Image() ;
function _RP(s,m){if(typeof s=='string'){if(m==1){return s.replace(/[#&^@,]/g,'');}else{return s.replace(/[#&^@]/g,'');} }else{return s;} };
function _RPS(a,b,c){var d=a.indexOf(b),e=b.length>0?c.length:1; while(a&&d>=0){a=a.substring(0,d)+c+a.substring(d+b.length);d=a.indexOf(b,d+e);}return a};
function AEC_F_D(pd,md,cnum){var i=0,amt=0,num=0;var cat='',nm='';num=cnum;md=md.toLowerCase();if(md=='b'||md=='i'||md=='o'){for(i=0;i<_A_pl.length;i++){if(_A_nl[i]==''||_A_nl[i]==0)_A_nl[i]='1';if(num==0||num=='')num='1';if(_A_pl[i]==pd){nm=_RP(_A_pn[i]);amt=(parseInt(_RP(_A_amt[i],1))/parseInt(_RP(_A_nl[i],1)))*num;cat=_RP(_A_ct[i]);var _A_cart=_CRL+'?cuid='+_GCD;_A_cart+='&md='+md+'&ll='+_RPS(escape(cat+'@'+nm+'@'+amt+'@'+num+'^&'),'+','%2B');break;};};if(_A_cart.length>0)_A_i.src=_A_cart+"rn="+String(new Date().getTime());setTimeout("",2000);};};
if(!_IDV(_A_pl)) var _A_pl = Array(1) ;
if(!_IDV(_A_nl)) var _A_nl = Array(1) ;
if(!_IDV(_A_ct)) var _A_ct = Array(1) ;
if(!_IDV(_A_pn)) var _A_pn = Array(1) ;
if(!_IDV(_A_amt)) var _A_amt = Array(1) ;
if(!_IDV(_pd)) var _pd = '' ;
if(!_IDV(_ct)) var _ct = '' ;
if(!_IDV(_amt)) var _amt = '' ;
</script>
Function and Variables Definition Block End

AceCounter eCommerce (Product_Detail) v7.5 Start
Data Allocation (Product_Detail)
<script language='javascript'>
_pd =_RP("${product.name}");
_ct =_RP("제품카테고리");
_amt = _RP("${product.salePrice}",1); // _RP(1)-> 가격

_A_amt=Array('${product.salePrice}');
_A_nl=Array('수량');
_A_pl=Array('${product.productId}');
_A_pn=Array('${product.name}');
_A_ct=Array('제품카테고리');
</script>
 -->
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

	_LOGIN_CALLBACK = null;
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

	
	/* 레이어 팝업 위치 설정 : 2016.08.09 */
	function fnPopPosition(target_pop) {
		$(target_pop).show();
		$(target_pop).height( $(document).height() );

		var base_top = ($(window).height() - $(".pop_inner", target_pop).innerHeight()) / 2;
		$(".pop_inner", target_pop).css({"marginTop" : base_top + $(window).scrollTop() + "px"});
	}
	
	// 세트상품의 옵션콤보 변경
	var changeSetOption = function(obj){
		
		//$(obj).blur();
		//setSelectBoxFocusout();
		var targetId = '';
		var comboId = 'subproducts_';
		var id = $(obj).attr('id');
		if(id.indexOf('bottom_')>= 0){
			targetId = comboId + $(obj).attr('subProductId');
		}else{
			targetId = 'bottom_' + comboId+ $(obj).attr('subProductId');
		}
		
		copySelectbox($(obj).val(), $('#'+id+' option:selected').text(), targetId);
		
		
		var allSelected = true;// 모든구성상품의 단품 선택 여부
		$('[id^='+comboId+']').each(function(){
			if($(this).val()==''){
				allSelected = false;
			}
		});
		
		
		
		if(allSelected){
			
			// 최대 구매 가능 수량 구하기
			$('[id^='+comboId+']').each(function(){
				
				var selectedQty = Number($("option:selected", this).attr("maxQty"));
				if(_noOptionQty == 0 || _noOptionQty > selectedQty){
					_noOptionQty = selectedQty;
				}
				
			});
			
			var qtyNum = Number($('[id^=qty_]').val());//선택수량박스
			
			// 모든구성상품의 단품이 선택되어 있으면 수량을 체크하여 구매가능 수량 초과했는지 체크
			if(_noOptionQty < qtyNum ){
				
				alert('구매가능한 수량을 초과하였습니다.');
				$('[id^=qty_]').val(_noOptionQty);
				
				// 저장객체에 단품수량 저장
				pms.detail.setSaleproductQty(_saleproductId, _noOptionQty);
				
				// total price 변경
				pms.detail.makeTotalPriceNoOption(_noOptionQty);
				
			}
			
		}

	}
		
	// qna작성 팝업
	var openQnaPopup = function(){
		mms.common.loginCheck(function(success){
			
			if(success){
				fnPopPosition( $(".ly_qna") );
			}	
		});
		
	}
	
	var closeLayer = function(id){
		$('#'+id).hide();
	}

	// 픽업체크박스 선택
	var checkOffshopPickup = function(isPickupMode, no){
		
		if(isPickupMode){// 픽업선택
			
			pms.common.checkBoxChecked("btnStorePickup", true);
			pms.common.checkBoxChecked("btnStorePickup2", true);
			
			__price_mode__ = 3;
			$('[name=pickupBuyBtn]').show();
			$('[name=normalBuyBtn]').hide();
			
			
			if(_optionYn=='N'){//세트이거나, 옵션없을경우
				_noOptionQty = Number('${ product.pickupMaxQty}');//픽업재고
			}
		}else{
			
			pms.common.checkBoxChecked("btnStorePickup", false);
			pms.common.checkBoxChecked("btnStorePickup2", false);
			
			__price_mode__ = 1;
			$('[name=pickupBuyBtn]').hide();
			$('[name=normalBuyBtn]').show();
			
			
			if(_optionYn=='N'){//세트이거나, 옵션없을경우
				_noOptionQty = Number('${ product.realStockQty}');//픽업재고
			}
			
		}
		
		if(_regularDeliveryYn=='Y' && $('#regularDeliveryYn').is(':checked')){
			initRegularArea();
		}
		
		// 픽업상품용 옵션 조회
		pms.detail.selectPickupMode(isPickupMode, _productId_);
		
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
			console.log(_selectSaleproducts);
			// 장바구니 담기
			pms.detail.cart(_productId_,'${product.productTypeCd}','pickup', '${styleNo}');
			
		});
	}
	
	// 정기배송 체크
	var checkRegularDelivery = function(checked, no) {
		
		// 구매 버튼
		$('[name=pickupBuyBtn]').hide();
		
		// 총금액을 정기배송가로(이전상태가 픽업일때는 초기화)
		//pms.detail.setTotalHtml(_initSalePrice);
		
		if(_optionYn=='Y'){
			// 옵션콤보 초기화
			pms.detail.initOption();
				
			// 선택단품 초기화
			pms.detail.initSelectedProduct();
			
		}
			
		if(checked){
			
			__price_mode__ = 2;
			
			$('[name=normalBuyBtn]').hide();
			
			$('[name^=regularSelectArea]').show();	
			
			pms.common.checkBoxChecked("regularDeliveryYn", true);
			pms.common.checkBoxChecked("regularDeliveryYn2", true);
			
			// 픽업매장이 존재하면 일반옵션 모드로 전환
			if(_pickupYn=='Y' && $('#btnStorePickup'+no).is(':checked')){
				
				pms.common.checkBoxChecked("btnStorePickup", false);
				pms.common.checkBoxChecked("btnStorePickup2", false);
				
				pms.detail.selectPickupMode(false, _productId_);//일반옵션가 조회
				
				pms.detail.setTotalHtml(_initSalePrice);
			}
			
			if(_optionYn=='N'){//세트이거나, 옵션없을경우
				_noOptionQty = Number('${ product.realStockQty}');//픽업재고
			}
			
		}else{
			
			__price_mode__ = 1;
			
			pms.common.checkBoxChecked("regularDeliveryYn", false);
			pms.common.checkBoxChecked("regularDeliveryYn2", false);
			
			$('[name=normalBuyBtn]').show();
			
			// 정기배송영역 초기화
			initRegularArea();
			
			
		}
		
		// 객체에 수량 설정
		if('Y'!=_setProductYn && 'Y'==_optionYn){
			 pms.detail.setTotalSalePrice();
		}else{
			var qty = $('#qty_'+_saleproductId).val();
			pms.detail.makeTotalPriceNoOption(Number(qty));
		} 
		
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
	// 정기배송 초기화
	var initRegularArea = function(){
		
		pms.common.checkBoxChecked("regularDeliveryYn", false);
		pms.common.checkBoxChecked("regularDeliveryYn2", false);
		$('[name^=regularSelectArea]').hide();	
		
		// 정기배송영역 초기화
		$("[id^=deliveryCnt] option:eq(0)").prop("selected", true);
		$('[id=deliveryCntLabel]').html($("#deliveryCnt").val());
		$('[id=deliveryCnt2Label]').html($("#deliveryCnt2").val());
		
		$("[id^=deliveryPeriodCd] option:eq(0)").prop("selected", true);
		$('[id=deliveryPeriodCdLabel]').html("1주일에 한 번");
		$('[id^=deliveryPeriodCd2Label]').html("1주일에 한 번");
		
	}
	
	// 선택한 단품 중복 체크
	var duplSaleproduct = function(saleproduct) {
		for (var i = 0; i < _selectSaleproducts.length; i++) {
			if (_selectSaleproducts[i].saleproductId == saleproduct.saleproductId) {
				alert("이미 선택한 옵션입니다.");
				return false;
			}
		}
		return true;
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
		
		//_dealId = saleproduct.dealId;

		$('[name=selectedSaleproductArea1]').show();
		
		
		$('[name=selectedSaleproductArea2]').append(getOptionSelectHtml(saleproduct));
		
		
		$('[name=totalBlock]').show();
		
		// 콤보박스 포커스아웃(IE11버그때문)
		//$('select[name^=prdOption]').blur();
		//$('#selectedSaleproductArea2').focus();
		
		// 옵션콤보 초기화
		pms.detail.initOption();
		
		$('[name=selectedSaleproductArea2_bottom]').append(getOptionSelectHtml2(saleproduct));

	}
	
	var getOptionSelectHtml = function(saleproduct){
		
		var stockQty = saleproduct.realStockQty;
		if(__price_mode__== 3 ){// 매장픽업일경우
			stockQty = saleproduct.maxShopStockQty;
		}
		
		
		var html = '';
		html += '<li name="selectedLi_'+saleproduct.saleproductId + '">';
		html += '<div class="left" style="width:150px">' + saleproduct.name + '</div>';
		html += '<div class="right">';
		html += '	<div class="quantity">';
		html += '		<button type="button" class="btn_minus" onclick="qtyChange(\'qty_' + saleproduct.saleproductId + '\', \''+ stockQty + '\'' 
		html += ',\'N\');">수량빼기</button>';
		html += '		<input type="text" value="1" onkeydown="return common.chkNumKeyDwn(this, event);" maxlength="4" onchange="onchange="qtyInput1(this.id,this.value)";qtyChange(\'qty_'
			    + saleproduct.saleproductId + '\', \''+ stockQty + '\' )" saleproductId="'
				+ saleproduct.saleproductId
				+ '" id="qty_'
				+ saleproduct.saleproductId + '">';
		html += '		<button type="button" class="btn_plus" onclick="qtyChange(\'qty_'
				+ saleproduct.saleproductId + '\' ,\''+ stockQty + '\' ,\'Y\');">수량추가</button>';
		html += '	</div>';
		html += '	<div class="optionPrice">'+ common.priceFormat(saleproduct.getMinPrice(),true,false) +'</div>';
		html += '</div>';
		html += '<button type="button" class="btn_x btnDelete" onclick="pms.detail.delSaleproductOption('
				+ saleproduct.saleproductId + ')">x</button>';
		html += '</li>';
		
		return html;
		
	}
	
	var getOptionSelectHtml2 = function(saleproduct){
		
		var stockQty = saleproduct.realStockQty;
		if(__price_mode__== 3 ){// 매장픽업일경우
			stockQty = saleproduct.maxShopStockQty;
		}
		
		var html = '';
		html += '<li name="selectedLi_'+saleproduct.saleproductId + '">';
		html += '<div class="left" style="width:150px">' + saleproduct.name + '</div>';
		html += '<div class="right">';
		html += '	<div class="quantity">';
		html += '		<button type="button" class="btn_minus" onclick="qtyChange(\'qty_' + saleproduct.saleproductId + '\', \''+ stockQty + '\'' 
		html += ',\'N\');">수량빼기</button>';
		html += '		<input type="text" value="1" onkeydown="return common.chkNumKeyDwn(this, event);" onchange="qtyInput2(this.name,  \''+ stockQty + '\' , this.value)" maxlength="4" name="qty_'+ saleproduct.saleproductId + '">';
		html += '		<button type="button" class="btn_plus" onclick="qtyChange(\'qty_'
				+ saleproduct.saleproductId + '\' ,\''+ stockQty + '\' ,\'Y\');">수량추가</button>';
		html += '	</div>';
		html += '	<div class="optionPrice">'+ common.priceFormat(saleproduct.getMinPrice(),true,false) +'</div>';
		html += '</div>';
		html += '<button type="button" class="btn_x btnDelete" onclick="pms.detail.delSaleproductOption('
				+ saleproduct.saleproductId + ')">x</button>';
		html += '</li>';
		
		return html;
		
	}
	
	
	var setSelectBoxFocusout = function(){
		$('select').off().on("change", function(){
			$(this).blur();
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
			$('#qnaPopup').hide();
			
			//초기화
			$("#productQnaForm input[name=saleproductId]").val('');
			$("#productQnaForm input[name=title]").val('');
			$("#productQnaForm textarea[name=detail]").val('');
			$("#productQnaForm input[name=img]").val('');
			pms.common.checkBoxChecked("secretYn", false);
			
			
			//재조회
			pms.detail.getQnaList(null, _productId_);
		});
	}

	// QnA 상세 열기
	var openQna = function(tr) {
		$(tr).next('tr').removeClass("");
	}

	// Qna ajax 리스트 콜백
	var listCallback = function(html) {
		$("#qnaDiv").html(html);
		$('#qnaDiv tr').on("click", function(e) {
			if ($(this).next(".tr_cont").hasClass("tr_cont_hide")) {
				$(this).siblings(".tr_cont").addClass("tr_cont_hide");
				$(this).next(".tr_cont").removeClass("tr_cont_hide");
			} else {
				$(this).next(".tr_cont").addClass("tr_cont_hide");
			}
			//e.preventDefault();
		});
	}

	// 상품평 리뷰 목록 콜백
	var reviewCallback = function(html) {
		$("#reviewList").html(html);
		
		$(".pc .td_tit").off("click").on("click", function(e){
			if( $(this).parent("tr").next(".tr_cont").hasClass("tr_cont_hide") ){
				$(this).parent("tr").siblings(".tr_cont").addClass("tr_cont_hide");
				$(this).parent("tr").next(".tr_cont").removeClass("tr_cont_hide");
			}else{
				$(this).parent("tr").next(".tr_cont").addClass("tr_cont_hide");
			}
			//e.preventDefault();
		});
	}
	
	// 점수로 별 표시하기
	var setReviewStar = function(totalRatingAvg, starId){
		var totalAvg = Number(totalRatingAvg);
		var starPoint = totalAvg * 20;
		$('#'+starId).attr('style', 'width:'+totalAvg * 20+'%');
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
			$('#reviewTotalAvg2').html(totalAvg);
			//setReviewStar(totalAvg, reviewTotalStar);
			//var totalAvg = Number(totalRatingAvg);
			var starPoint = totalAvg * 20;
			$('#reviewTotalStar').attr('style', 'width:'+starPoint+'%');
			$('#reviewTotalStar2').attr('style', 'width:'+starPoint+'%');
			
			var ratingsHtml='';
			// 항목별 평점
			for(i = 0 ; i < data.ratingAvgList.length ; i++){
				
				var totalAvg = Number(data.ratingAvgList[i].ratingIdAvg);
				var starPoint = totalAvg * 20;
				
				var starHtml='';
				starHtml+='<dl>';
				starHtml+='<dt>'+data.ratingAvgList[i].ratingName+'</dt>';
				starHtml+='<dd>';
				starHtml+='<span class="rating"><em style="width: '+starPoint+'%;"></em></span>';
				starHtml+='</dd>';
				starHtml+='</dl>';
				ratingsHtml+=starHtml;
				
			}
			
			$('#reviewRatingStarArea').html(ratingsHtml);
			
		});
	}
	
	var snsShare = function(type){
		var currentUrl = location.href;
		var snsUrl = "";
		if(type=='facebook'){
			snsUrl = "http://www.facebook.com/sharer/sharer.php?u="+currentUrl;
		}else if(type='twitter'){
			snsUrl = 'https://twitter.com/intent/tweet?text=TEXT&url='+currentUrl;
		}
		
		var status =  "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,width=420,height=540";
		window.open(snsUrl, type, status);
		
	}

	var regularCartLoginCheck = function(){
		
		
		function regularCart(){
			
			pms.detail.cart('${product.productId}','${product.productTypeCd}','r', '${styleNo}');
			
		}
		
		mms.common.isLogin(function(result){
			
			if(result!=1){
				if(confirm("정기배송은 회원만 신청 가능합니다. 로그인 후 정기배송을 신청하시겠습니까?")){
					
					mms.common.loginCheck2(function(success){
						if(success){
							regularCart();
						}
					});
				}
			}
			
			if(result == 1){
				regularCart();
			}
			
		});
	}
	
	// 옵션 변경 이벤트
	var changeBottom = function(name, optionsize, productId, value){
		
		$('[name='+name+']').val(value);
		
		pms.detail.selectProductOption(name, optionsize, productId);
	}
	// 상/하단 옵션 수량 +- 클릭
	var qtyChange = function(qtyId, maxQty, plusYn){

		pms.detail.qtyChange(qtyId, maxQty, plusYn);
		
		$('[name='+qtyId+']').val($('[id='+qtyId+']').val());
	}
	
	
	
    // 상단 수량 입력 이벤트
	var qtyInput1 = function(name, value){
    	$('[name='+name+']').val(value);
	}
    
    
    // 하단 수량 입력 이벤트
   var qtyInput2 = function(name, qty, value){
    	$('[id='+name+']').val(value);
    	pms.detail.qtyChange(name, qty);
    	$('[name='+name+']').val($('[id='+name+']').val());
    }
    
	var copySelectbox = function(value, label, id){
		$('[id='+id+']').val(value);
		
	 	if(value==''){
    		$('[id='+id+'Label]').html("선택하세요.");
    	}else{
    		$('[id='+id+'Label]').html(label);
    	}
	}

	
	// 정기배송 신청하기 체크박스 클릭
	$(document).ready(function() {

		// QmA목록 조회
		pms.detail.getQnaList(null, _productId_);
		
		// 상품평 목록 조회
		pms.detail.getProductReview(null, '01', '${product.productId}');
		getProductReviewTotalAndRatings();
		
		
		var swiperFn = function(){
			swiperCon('prodDetailSwiper_bestProdList_1', 400, 5, 12, false, true, 5); //베스트 상품
		}
		pms.common.getRecommendationProductList($('[name=categoryBestArea]'), {recType:'a002', size : 10, iids : _productId_}, swiperFn);
		
		//베너 스와이프
		swiperCon('prodDetailSwiper_banner_1', 400, 1, 0, false, true); //프리미엄 멤버쉽 베너
		
		// 픽업 전용 상품
		if(_pickupOnly == 'Y'){
			
			$('#btnStorePickup').click();
			$('#btnStorePickup').prop('disabled', true);
			$('#btnStorePickup2').prop('disabled', true);
			
			//정기배송 버튼 삭제
			$('#regularCheckArea').remove();
			
			// 선물포장 안내영역 삭제
			$('#giftInfoArea').remove();
		}
		
		// 콤보박스 선택시 blur효과
		setSelectBoxFocusout();
		
		
		
		// 정기배송 변경시 하단 고정옵션여역도 변경
		$('#deliveryPeriodCd').change(function() {
			copySelectbox( $(this).val(), $('#deliveryPeriodCd option:selected').text(), 'deliveryPeriodCd2');
		});
		$('#deliveryPeriodCd2').change( function() {
			copySelectbox( $(this).val(), $('#deliveryPeriodCd2 option:selected').text(), 'deliveryPeriodCd');
		});
		
		$('#deliveryCnt').change(function() {
			copySelectbox( $(this).val(),$('#deliveryCnt option:selected').text(), 'deliveryCnt2');
		});
		$('#deliveryCnt2').change( function() {
			copySelectbox( $(this).val(), $('#deliveryCnt2 option:selected').text(), 'deliveryCnt');
		});
		
		
		
		// 옵션 변경하 하단 고정옵션도 함께 변경
	    $("[name^=prdOption_]").bind("DOMSubtreeModified", function() { 
	    	
	    	var name = $(this).attr('name');
	    	console.log(name);
	    	var value = $(this).val();
	    	
	    	$('[id='+name+']').val(value);
	    	$('[id='+name+']').html($(this).html());
	    	
	    	if(value==''){
	    		$('[id='+name+']').prev().html("선택하세요.");
	    	}else{
	    		$('[id='+name+']').prev().html(value);
	    	}
	    	
	    	
	    });
	    
	    // 상단 수량 입력 이벤트
	    $('[id^=qty_]').change(function() { 
	    	$('[name='+$(this).attr('id')+']').val($(this).val());
	    });
	    
	});
</script>
	<c:if test="${!isMobile}">
		<jsp:include page="/dms/category/navi" flush="false">
			<jsp:param name="categoryId" 	value="${displayCategoryId}" />
		</jsp:include>
	</c:if>	
	<!-- location_box -->
	<div class="inner">
		<!-- detail -->
		<div class="goods_detail_box">
			<!-- ### 상품 이름 : 2016.08.17 추가 ### -->
			<div class="prod_name">
				
				<!-- <span class="icon_type1 iconPurple2">BEST</span> 
				<span class="icon_type1 iconPurple2">NEW</span> --> 
				<c:if test="${product.presentYn eq 'Y'}">
					<span class="icon_type1 iconPurple2">사은품</span>
				</c:if>
				<c:if test="${product.optimalprice.deliveryFeeFreeYn eq 'Y'}">
					<span class="icon_type1 iconPurple2">무료배송</span>
				</c:if>
				<em>${product.adCopy}&nbsp;${product.name }</em>
				
				<div class="posR">
				<em class="prodCode">상품번호 : ${product.productId }</em>
				<ul class="sns">
					<li><button type="button" class="pc_btn_jjim  ${not empty product.wishlistNo ? 'active' : ''}" onclick="pms.updateWishlist('${product.productId}', this);"><span>찜</span></button>				
					<li><a href="#none" class="face" onclick="ccs.sns.share('facebook')">페이스북</a></li>
					<li><a href="#none" class="kakaoStory" onclick="ccs.sns.share('kakaoStory')">카카오스토리</a></li>
					<li><a href="#none" class="twitter" onclick="ccs.sns.share('twitter')">트위터</a></li>
					<li><a href="#none" class="nblog" onclick="ccs.sns.share('blog')">blog</a></li>
					<li><a href="#none" class="url" onclick="ccs.sns.share('link')">URL</a></li>
				</ul>
				</div>
								
			</div>
			<!-- ### //상품 이름 : 2016.08.17 추가 ### -->
			<div class="detail_inner">
				<!-- ### 상세 제품 이미지 : 2016.08.17 수정 ### -->
				<div class="detail_l">
					<div class="img_box">
						<div class="goods_img">
							<ul>
							<c:forEach items="${product.pmsProductimgs }" var="images">
								<li ${images.imgNo eq '0' ? 'class="on"' : ''}>
									<tags:prdImgTag productId="${product.productId}" size="500" className="" seq="${images.imgNo }" alt="${product.name }" />
						        </li>
					        </c:forEach>
							</ul>
						</div>
						<div class="package">
							<div class="dots">
							<c:forEach items="${product.pmsProductimgs }" var="images">
								<span ${images.imgNo eq '0' ? 'class="on"' : ''} ></span>
							</c:forEach>	
							</div>
							<button type="button" class="btn_large">큰 이미지 보기</button>
						</div>
					</div>
					<div class="evaluation">
						<div class="count"><strong>상품평</strong> <em id="reviewTotalCount2">(100건)</em></div> <!-- 1120 -->
						<div class="jumsu">
							<span class="info" id="reviewEmptyNotice" style="display:none">첫번째 상품평 작성 시 매일포인트 1000P 지급</span>
							
							<span class="tit" id="reviewEmptyTit"><strong>평점</strong> <em id="reviewTotalAvg2">6.0</em></span>
							
							<span class="rating" id="reviewEmptyRating"> <em style="width: 60%;" id="reviewTotalStar2">6점</em>
							</span>
						</div>
						<a href="#none" class="btn_more" id="productReviewMore">더보기</a>
					</div>
					<c:if test="${fn:length(product.exhibits) > 0}">
						<div class="promo_list">
							<!-- 2016.11.04 수정 시작 -->
							<div class="promo_list_header">
								<em>관련 기획전</em>
								<a href="#none" onclick="ccs.link.display.exhibit()" class="btn_more">더보기</a>
							</div>
							<!-- //2016.11.04 수정 끝 -->
							<ul><c:forEach var="exhibit" items="${product.exhibits }">
								<li><a href="/dms/exhibit/detail?exhibitId=${exhibit.exhibitId }">${exhibit.name }</a></li>
								</c:forEach>
							</ul>
						</div>						
					</c:if>
				</div>
				<!-- ### //상세 제품 이미지 : 2016.08.17 수정 ### -->
				<!-- ### 상세 제품 내용 : 2016.08.17 수정 ### -->
				<div class="detail_r">
					<c:if test="${not empty product.spsDeal.endDt and product.optimalprice.dealId eq '1' }">
						<div class="shocking_bar">
							<em>쇼킹제로</em>
							<dl>
								<dt>오늘오픈</dt>
								<dd id="shockDate">
									<script>
										pms.countdown("shockDate", "${product.spsDeal.endDt}")
									</script>
								</dd>
							</dl>
						</div>
					</c:if>
					
					<div class="price_box">
					<c:if test="${ minSalePrice < product.optimalprice.listPrice}">
						<span class="ori"><fmt:formatNumber value="${product.optimalprice.listPrice}" groupingUsed="true" />원</span>
					</c:if>
						<span class="sale">
							<fmt:formatNumber value="${minSalePrice}" groupingUsed="true" /><em>원</em>
						</span>
					<c:if test="${not empty product.unitQty and product.unitQty > 1 }">
						<c:set var="unitPrice" value="${ minSalePrice div (empty product.unitQty ? 1 : product.unitQty)}"/>
						<span class="indi">(1개당 <fmt:formatNumber value="${unitPrice}" groupingUsed="true" />원)</span>
					</c:if>
					<c:if test="${ not empty product.optimalprice.couponId or not empty product.optimalprice.dealId }">
						<a href="#none" class="btn_infor btn_priceGuide">?</a> <!-- 가격적용안내 -->
					</c:if>	
					<c:choose>
					<c:when test="${product.optimalprice.dealId eq '3'}"><span class="emph">프리미엄가</span></c:when>
					<c:when test="${product.optimalprice.dealId eq '4'}"><span class="emph">임직원가</span></c:when>
					<c:when test="${ not empty product.optimalprice.couponId }">
						<c:if test="${product.optimalprice.dcApplyTypeCd eq 'DC_APPLY_TYPE_CD.AMT' }"><c:set var="couponType" value="원"/></c:if>
						<c:if test="${product.optimalprice.dcApplyTypeCd eq 'DC_APPLY_TYPE_CD.RATE' }"><c:set var="couponType" value="%"/></c:if>
						<span class="emph">${ product.optimalprice.dcValue }${couponType} 쿠폰적용</span>
					</c:when>
					</c:choose>
					<c:if test="${ minSalePrice < product.optimalprice.listPrice and product.optimalprice.listPrice > 0 }">
						<div class="right">
								<i class="per">
									<tags:dcRate stdPrice="${product.optimalprice.listPrice }" dcPrice="${minSalePrice}" var="rate" />
									<c:if test="${rate != 0}">
										${rate}<small>%</small>
									</c:if>
								</i>
						</div>
					</c:if>
					</div>					
					<div class="info_block">
										
						<c:if test="${product.regularDeliveryYn eq 'Y' }">
							<dl>
								<dt><span class="ico_regular"></span>&nbsp;정기배송가</dt>
								<dd>
									<b><fmt:formatNumber value="${product.regularDeliveryPrice }" groupingUsed="true" />원</b> <a href="#none" class="btn_infor btn_regular">?</a>
									<div class="input_wrap">
											<label class="chk_style1">
												<em>
													<input type="checkbox" id="regularDeliveryYn" onclick="checkRegularDelivery(this.checked, '2')" class="btnStorePickup">
												</em>
												<span>정기배송 신청</span>
											</label>
										</div>
								</dd>
							</dl>
						</c:if>
						
						<c:if test="${product.offshopPickupYn eq 'Y' }">
						<dl>
							<dt><span class="ico_pick">매장픽업</span>&nbsp;매장픽업가</dt>
							<dd>
 								<b><tags:dcPrice price="${product.salePrice}" dcRate="${product.offshopPickupDcRate}" format="true"/>원</b><a href="#none" class="btn_infor btn_pickup">?</a>
								<div class="input_wrap">
									<label class="chk_style1">
										<em>
											<input type="checkbox"  value="Y" onclick="checkOffshopPickup(this.checked)" id="btnStorePickup" class="btnStorePickup">
										</em>
										<span>매장픽업 신청</span>
									</label>
								</div> 								
 								
							</dd>
						</dl>
						</c:if>						
						<dl>
							<dt>배송비</dt>
							<dd>
							<c:choose>
							<c:when test="${product.optimalprice.deliveryFeeFreeYn eq 'Y' or empty product.ccsDeliverypolicy.deliveryFee or product.ccsDeliverypolicy.deliveryFee eq '0'}">
							무료배송
							</c:when>
							<c:otherwise>
							<fmt:formatNumber value="${ product.ccsDeliverypolicy.deliveryFee }" groupingUsed="true" />원(<fmt:formatNumber value="${ product.ccsDeliverypolicy.minDeliveryFreeAmt }" groupingUsed="true" />원  이상 무료)
							</c:otherwise>
							</c:choose>
							 <a href="#none" class="btn_infor btn_freeDelivery">?</a> <a href="/dms/display/sellerShop?businessId=${product.businessId}" class="btn_sStyle3 btn_right">묶음배송상품보기</a>
							</dd>
						</dl>
					</div>
					<div class="info_block">
						<c:if test="${not empty product.totalPoint  and product.totalPoint  > 0 }">
						<dl>
							<dt>매일포인트</dt>
							<dd>
								최대 적립 <fmt:formatNumber value="${product.totalPoint }" groupingUsed="true" />원 <a href="#none" class="btn_infor btn_mpoint">?</a>
							</dd>
						</dl>
						</c:if>
						<c:if test="${not empty product.spsCardpromotion}">
						<dl>
							<dt>무이자혜택</dt>
							<dd>
								<a href="#none" class="link btn_cardbnf"> ${product.spsCardpromotion.name } </a>
							</dd>
						</dl>
						</c:if>
						<c:if test="${not empty product.origin}">
						<dl>
							<dt>원산지</dt>
							<dd>${product.origin }</dd>
						</dl>
						</c:if>						
<c:if test="${ not empty product.pmsBrand }">						
						<dl>
							<dt>브랜드</dt>
							<dd>
								${product.pmsBrand.name } <a href="javascript:brand.template.main('${product.pmsBrand.brandId}');" class="btn_sStyle3 btn_right">브랜드 더보기</a>
							</dd>
						</dl>
</c:if>						
						<c:if test="${product.wrapYn eq 'Y' }">
							<dl id="giftInfoArea">
								<dt>선물포장</dt>
								<dd>
									선물 포장이 가능합니다. <a href="#none" class="btn_infor btn_giftInfo">?</a>
								</dd>
							</dl>
						</c:if>
					</div>
				<c:choose>
<%--세트상품 or 옵션 없는 상품 --%>
					<c:when test="${product.optionYn eq 'N' or product.productTypeCd eq 'PRODUCT_TYPE_CD.SET' }">
						
						
						<div class="info_block">
						<c:if test="${product.productTypeCd eq 'PRODUCT_TYPE_CD.SET' }">
						
							<c:forEach var="setp" items="${product.pmsSetproducts }" varStatus="index">
								<dl ${fn:length(setp.pmsSaleproducts) < 2 ? 'style="display:none"': ''}>
									<%-- <dt class="mt" ${fn:length(setp.pmsSaleproducts) < 2 ? 'style="width:100%"': ''}>${setp.name}</dt> --%>
									<dd>
										<div class="select_box1" ${fn:length(setp.pmsSaleproducts) < 2 ? 'style="display:none"': ''} >
											<label id="subproducts_${setp.subProductId }Label">선택하세요</label> 
											<select onchange="changeSetOption(this)" id="subproducts_${setp.subProductId }" subProductId="${setp.subProductId }" >
												<option value="">선택하세요</option>
												<c:forEach var="saleproduct" items="${setp.pmsSaleproducts}">
													<option maxQty="${saleproduct.realStockQty }" ${fn:length(setp.pmsSaleproducts)==1 ? 'selected':''}  value="${saleproduct.saleproductId }" 
													        ${saleproduct.realStockQty eq 0 ? 'disabled' : '' }>${saleproduct.name }${saleproduct.realStockQty eq 0 ? ' (품절)' : '' } </option>
												</c:forEach>
											</select>
											<!-- 단일 옵션일 경우 -->
										</div>
									</dd>
								</dl>								
							</c:forEach>						
						</c:if>		
							<dl>
								<dt class="mt">수량</dt>
								<dd>
									<div class="quantity">
										<button type="button" class="btn_minus" onclick="qtyChange('qty_${product.saleproductId}', _noOptionQty, 'N');">수량빼기</button>
										<input type="text" value="1" onkeydown="return common.chkNumKeyDwn(this, event);" maxlength="4" onchange="qtyChange('qty_${product.saleproductId}', _noOptionQty)" 
										    price="${minSalePrice }" saleproductId="${product.saleproductId}" id="qty_${product.saleproductId}">
										<button type="button" class="btn_plus" onclick="qtyChange('qty_${product.saleproductId}', _noOptionQty, 'Y');">수량추가</button>
									</div>
								</dd>
							</dl>
						</div>								

					</c:when>
<%-- 일반상품 옵션있는 상품 --%>
					<c:when test="${product.optionYn eq 'Y' and product.productTypeCd ne 'PRODUCT_TYPE_CD.SET' }">
						<div class="info_block">
							<c:forEach var="optionName" items="${product.pmsProductoptions }" varStatus="index">
								<%--옵션목록 조회 --%>
								<dl>
									<dt class="mt">${optionName.optionName}</dt>
									<dd>
										<div class="select_box1">
											<label>선택하세요</label> 
												<select optionName="${ optionName.optionName}" name="prdOption_${index.count }"
													onchange="pms.detail.selectProductOption(this.name, ${fn:length(product.pmsProductoptions)} ,'${product.productId }', 'bottom')">
													<option value="">선택하세요</option>
												<c:forEach var="option" items="${optionName.optionValues}">
													<option value="${option.optionValue }" ${option.realStockQty==0 ? 'disabled' : '' }>${option.optionDispValue }</option>
												</c:forEach>
											</select>
										</div>
									</dd>
								</dl>
							</c:forEach>
							<p class="optionCmt">* 옵션별로 가격이 상이할 수 있습니다.</p>
							<div class="selectOption" name="selectedSaleproductArea1" style="display: none">
								<ul name="selectedSaleproductArea2">
								</ul>
							</div>
						</div>						
					</c:when>
				</c:choose>
					<div class="total_block" name="totalBlock" ${product.optionYn eq 'Y' ? 'style=display:none' : ''}>
						<ul>
							<li>
								<div class="sum">
									<span>총 금액</span><em name="totalPrice"><fmt:formatNumber value="${initSalePrice}" groupingUsed="true" /></em>원
								</div>
							</li>
						</ul>
					</div>
<%-- 장바구니/구매하기 버튼 영역 --%>
					<div class="btn_box">
						<div class="btnGroup" name="normalBuyBtn">
						<c:set value="100" var="btnWidth"/>
				<c:if test="${product.giftYn eq 'Y' }">
							<a href="#none" class="btn_style white btn_gift" onclick="pms.detail.order('${product.productId}','${product.productTypeCd}','Y', '${styleNo}')"><span>선물</span></a> 
				</c:if>
				<c:if test="${ not (not empty  childrenDealId and product.optimalprice.dealId eq childrenDealId ) }">
							<c:set value="${product.giftYn eq 'Y' ? 31.5 : 49 }" var="btnWidth"/>
							<a href="#none" style="width:${btnWidth}%" class="btn_style" onclick="pms.detail.cart('${product.productId}','${product.productTypeCd}', '', '${styleNo}')"><span>장바구니</span></a>
				</c:if>			 
							<a href="#none" style="width:${btnWidth}%" class="btn_style purple" onclick="pms.detail.order('${product.productId}','${product.productTypeCd}', '', '${styleNo}')"><span>바로구매</span></a>
						</div>
						<div class="btnGroup" name="pickupBuyBtn" style="display:none">
							<a href="#none" style="width:100%" class="btn_style" onclick="pickupCart()"><span>매장픽업 장바구니</span></a>
						</div>
					</div>
					<!-- ### //일반 옵션 ### -->
					<!-- ### 정기배송 옵션 ### -->
					<div class="btn_box">
						<div class="selectOption" name="regularSelectArea" style="display: none">
							<ul>
								<li>
									<div class="left" style="width: 28%;">배송주기/횟수 선택</div>
									<div class="right" style="width: 60%;">
										<div class="select_box1">
											<label id="deliveryPeriodCdLabel">1주일에 한 번</label> 
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
									</div>
								</li>
							</ul>
						</div>
						<div class="btnRegular" name="regularSelectArea2" style="display: none">
							<a href="#none" class="btn_style" onclick="regularCartLoginCheck()"><span>정기배송 장바구니</span></a>
						</div>
					</div>
					<!-- ### //정기배송 옵션 ### -->
				</div>
				<!-- ### //상세 제품 내용 ### -->
			</div>
		</div>
<c:if test="${not empty bannerList }">			
		<div class="goods_banner">
			<div class="swiper_wrap">
				<div class="swiper-container prodDetailSwiper_banner_1">
					<ul class="swiper-wrapper">
					<c:forEach items="${bannerList}" var="banner" varStatus="index">
						<li class="swiper-slide">
							<a href="${banner.url1}"><img style="width:1000px" src="${_IMAGE_DOMAIN_}${banner.img1}" alt="${banner.text1}"></a>
							</a>
						</li>
					</c:forEach>	
					</ul>

					<!-- Add Arrows -->
			        <div class="swiper-button-next btn_tp4"></div>
			        <div class="swiper-button-prev btn_tp4"></div>

					<!-- Add Pagination -->
  						<div class="swiper-pagination tp4"></div>
				</div>
			</div>

		</div>

</c:if>		
<%--카테고리 베스트 영역 --%>
		<div class="rolling_box prodCateBest">
			<h2 class="tit_style2">카테고리 베스트 상품</h2>

			<div class="swiper_wrap">
				<div class="swiper-container prodSwiper product_type1 prodType_4ea block prodDetailSwiper_bestProdList_1">
					<ul class="swiper-wrapper" name="categoryBestArea">
					</ul>
					<!-- Add Arrows -->
					<div class="swiper-button-next btn_tp2"></div>
					<div class="swiper-button-prev btn_tp2"></div>
				</div>
			</div>
		</div>
						
<%--하단 탭별 영역 --%>		
		<div id="goodsCont" class="goods_cont_box fixed">
			<!-- ### 탭 메뉴 ### -->
			<ul class="tabBox1 detail_tab">
				<li class="on"><a href="#none">상세정보</a></li>
				<li><a href="#none">구매/배송정보</a></li>
				<li><a href="#none" id="productReviewTab">상품평 <em name="reviewTotal" id="reviewTotalCount3">(0)</em></a></li>
				<li><a href="#none">Q&amp;A <em name="qnaTotal">(0)</em></a></li>
			</ul>
			<!-- ### //탭 메뉴 ### -->
			<!-- ### 상세정보 그룹 ### -->
			<div class="detail_cont cont_01 tab_con tab_conOn">
				<div class="detail_area">
					<c:if test="${not empty product.ccsNotice }">
						<div class="dBox">
							<h3 class="h_title2">MD공지</h3>
							${product.ccsNotice.detail }
						</div>
					</c:if>
					<c:if test="${not empty product.sellingPoint }">
						<div class="dBox">
							<h3 class="h_title2">꼼꼼체크✔</h3>
							<div class="grayBox">${product.sellingPoint }</div>
						</div>
					</c:if>
	<%--상품상세정보(기술서) --%>					
					<div class="dBox dBox_info">
						<h3 class="h_title1">상품상세정보</h3>
						<div class="productDsc">${product.detail }
						</div>
					</div>
	<%--카탈로그 영역 --%>
<!-- 					<div class="catalogue">
						<a href="#none"><img src="/resources/img/pc/temp/temp_detail_bnr2.jpg" alt=""></a>
					</div> -->
				</div>
			</div>
			<!-- ### //상세정보 그룹 ### -->
			<!-- ### 배송/교환/환불 그룹 ### -->
			<div class="detail_cont cont_02 tab_con">
				<h3 class="h_title2">상품정보제공고시</h3>
				<div class="tbl_box">
					<table>
						<caption>상품정보</caption>
						<colgroup>
							<col style="width: 175px">
							<col style="width: *">
						</colgroup>
						<tbody>
						<c:forEach var="notice" items="${product.pmsProductnotices}">
							<tr>
								<th scope="row">${notice.pmsProductnoticefield.title }</th>
								<td>${notice.detail }</td>
							</tr>
						</c:forEach>						
						</tbody>
					</table>
				</div>
<c:if test="${ not empty product.claimInfo}">				
				<h3 class="h_title2" style="margin-top: 25px;">배송/교환/반품 정보</h3>
				<div class="tbl_box">
					${product.claimInfo}
				</div>
</c:if>				
			</div>
			<!-- ### //배송/교환/환불 그룹 ### -->
			<!-- ### 상품평 그룹 ### -->
			<div class="detail_cont cont_03 tab_con">
				<div class="point_dsc">
					<ul>
						<li>첫번째 상품평 작성 시 <em>매일포인트 1000P</em> 지급
						</li>
						<li>일반상품평 작성 시 <em>매일포인트 100P + 당근500개</em> 지급
						</li>
						<li>포토상품평 작성 시 <em>매일포인트 100P + 당근 1,000개</em> 지급
						</li>
					</ul>
					<div class="help">
						첫상품평 작성 유의사항 <a href="#none" class="btn_infor btn_review_guide">안내</a>
					</div>
					<a href="javascript:ccs.link.mypage.activity.review();" class="btn_imgStyle1 btnReview" > <em>상품평 쓰기</em>
					</a>
				</div>
				<div class="score_box">
					<div class="left">
						<dl>
							<dt>총 평점</dt>
							<dd>
								<span class="rating big"><em style="width: 0%;" id="reviewTotalStar"></em></span> 
								<strong class="rating_num" id="reviewTotalAvg1">8.0</strong>
							</dd>
						</dl>
					</div>
					<div class="right rating_list" id="reviewRatingStarArea">
					</div>
				</div>
				<div class="tab_box">
					<div class="tabBox2">
						<input type="hidden" id="reviewSearchType">
						<ul>
							<li class="on"><a href="javascript:pms.detail.getProductReview('all','','${product.productId}');">전체<span id="reviewTotalCount1">(0)</span></a></li>
							<li><a href="javascript:pms.detail.getProductReview('img','','${product.productId}');">포토<span id="reviewPhotoCount">(0)</span></a></li>
							<li><a href="javascript:pms.detail.getProductReview('permit','','${product.productId}');">체험단 후기<span id="reviewRepermitCount">(0)</span></a></li>
						</ul>
						<div class="select_box1">
							<label>최근 등록순</label> 
							<select id="reviewSelectValue" onchange="javascript:pms.detail.getProductReview('type',this.value,'${product.productId}');">
								<option value="01">최근 등록순</option>
								<option value="02">평점 높은순</option>
								<option value="03">평점 낮은순</option>
							</select>
						</div>
					</div>
					<div class="tab_con tab_conOn" id="reviewList">
					</div>
				</div>
			</div>
<%--상품 QnA 영역 --%>
			<div class="detail_cont cont_04 tab_con">
				<div class="cont_header">
					<div class="state_wrap">
						<span class="state">전체 <em name="qnaTotal2">(0)</em></span>
						<span>상품에 대해 궁금한 점 있으시면 문의해주세요!</span>
					</div>
					<div class="chk_tit">
						<label class="chk_style1"> 
							<em><input type="checkbox" value="Y" onclick="pms.detail.getQnaList({secret : this.checked}, '${product.productId}')">
							</em><span>비밀글 제외</span>
						</label> 
						<a href="#none" class="btn_imgStyle1 btnQnA" onclick="openQnaPopup()"><em>Q&amp;A 쓰기</em>
						</a>
					</div>
				</div>
				
				<span id="qnaDiv"></span>
			</div>
			<!-- ### //QnA 그룹 ### -->
		</div>
	</div>
<%--본문영역 끝 --%>	
	
	
	
	
<%-- 상품문의 팝업 --%>
<jsp:include page="/WEB-INF/views/ccs/common/layer/qnaRegLayer.jsp" flush="true"/>

<div class="pop_wrap goods_img_detail">
	<div class="pop_content">
		<ul class="thum_img_list">
		<c:forEach items="${product.pmsProductimgs }" var="images">
			<li ${images.imgNo eq '0' ? 'class="on"' : ''}>
				<a href="#none"><tags:prdImgTag productId="${product.productId}" size="60" className="" seq="${images.imgNo }" alt="${product.name }" /></a>
				<div class="thum_img active">
				<tags:prdImgTag productId="${product.productId}" size="750" className="" seq="${images.imgNo }" alt="${product.name }" />
				</div>
	        </li>
        </c:forEach>		
		</ul>
	</div>
	<button type="button" class="btn_x pc_btn_close">닫기</button>
</div>
<div class="pop_wrap sLayer_delivery">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">배송비 안내</h3>
		</div>
		<div class="pop_content">
			<dl class="delivery_guide">
				<dt>배송비</dt>
				<dd>
				<c:choose>
				<c:when test="${product.optimalprice.deliveryFeeFreeYn eq 'Y' or empty product.ccsDeliverypolicy.deliveryFee or product.ccsDeliverypolicy.deliveryFee eq '0'}">
					- 무료배송<br>
				</c:when>
				<c:otherwise>
				    <fmt:formatNumber value="${ product.ccsDeliverypolicy.deliveryFee }" groupingUsed="true" />원 (<fmt:formatNumber value="${ product.ccsDeliverypolicy.minDeliveryFreeAmt }" groupingUsed="true" />원 이상 무료배송)<br>
				    <p>* 지역에 따라 추가배송비 발생할 수 있습니다.</p>
				</c:otherwise>
				</c:choose>

				</dd>
				<c:if test="${product.optimalprice.deliveryFeeFreeYn eq 'N'}">
				<dt>배송방법</dt>
				<dd>
					- 택배배송(<tags:codeName code='${product.ccsDeliverypolicy.deliveryServiceCd}'/>)<br>
				</dd>
				</c:if>
			</dl>
		</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>
		
<div class="pop_wrap sLayer_mpoint" style="display: none;">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">매일포인트 추가 적립 안내</h3>
		</div>
		<div class="pop_content">
			<div>
				<strong>상품 구매 시 현금처럼 사용 가능한 매일 포인트를 적립해 드립니다.</strong> 포인트는 결제 금액의 최대 1%까지 적립되며, 주문 시 결제 금액에 따라 표시 금액보다 적을 수 있습니다.
			</div>
		</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>

<div class="pop_wrap sLayer_regular" style="display: none;">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">정기배송 신청 안내 </h3>
		</div>
		<div class="pop_content">
			<div>
				<p>정기배송은 자주 구매하는 상품을 보다 저렴하게 정기적으로 원하는 요일에 알아서 배송해드리는 서비스입니다.<br>지정한 신용카드로 자동 결제되어 상품을 받아 볼 수 있고, 반품 및 해지가 가능합니다.</p>
			</div>
		</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>

<div class="pop_wrap sLayer_pickup" style="display: none;">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">매장픽업가 안내</h3>
		</div>
		<div class="pop_content">
			<ul class="notice notice_bgWhite">
				<li>매장픽업가는 매장픽업 신청 시에만 제공되는 가격입니다.</li>
				<li>매장픽업은 온라인으로 매장 상품을 구매 예약할 수 있는 서비스입니다.</li>
				<li>오후 4시 이전 주문 시 당일 픽업 가능합니다.</li>
			</ul>

		</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>


<div class="pop_wrap sLayer_gift" style="display: none;">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">선물포장 안내</h3>
		</div>
		<div class="pop_content">
			<ul class="notice notice_bgWhite">
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
				<img src="/resources/img/pc/bg/sLayer_gift.jpg" alt="">
			</div>
		</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>
<c:if test="${not empty product.spsCardpromotion.html1}">
<div class="pop_wrap sLayer_cardbnf">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">카드사별 무이자 혜택안내</h3>
		</div>
		<div class="pop_content">
			<strong class="tit">무이자카드 안내</strong>
			<div class="info">
				${product.spsCardpromotion.html1}
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

<div class="pop_wrap sLayer_priceGuide">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">가격적용 안내</h3>
		</div>
		<div class="pop_content">
			<dl class="price_guide">
				<dt>판매가</dt>
				<dd><fmt:formatNumber value="${product.optimalprice.listPrice}" groupingUsed="true" />원</dd>
				<dt>
			<c:choose>
				<c:when test="${not empty product.optimalprice.dealTypeCd }">
				<tags:codeName code='${product.optimalprice.dealTypeCd}'/>
				</c:when>
				<c:when test="${empty product.optimalprice.dealTypeCd and not empty product.optimalprice.couponId }">
				쿠폰적용가</c:when>									
			</c:choose>				
				</dt>
				<dd><fmt:formatNumber value="${minSalePrice}" groupingUsed="true" />원</dd>
			</dl>	
		</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>

<div class="pop_wrap sLayer_cart" id="sLayer_cart">
	<div class="pop_inner">
		<div class="pop_content">
			<p>상품이 장바구니에 담겼습니다.</p>
			<span>장바구니로 이동하시겠습니까?</span>
			<div class="btn_wrapC btn2ea ">	
				<a href="#none" class="btn_sStyle4 sGray2" onclick="goCart()">장바구니로 가기</a>
				<a href="#none" class="btn_sStyle4 sPurple1" onclick="$('#sLayer_cart-layer').remove()">쇼핑 계속하기</a>
			</div>
		</div>
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>

<div class="pop_wrap sLayer_review">
	<div class="pop_inner">
		<div class="pop_header type1">
			<h3 class="tit">상품평 작성 유의사항</h3>
		</div>
		<div class="pop_content">
			<ul class="notice notice_bgWhite">
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
		<button type="button" class="btn_x pc_btn_close">닫기</button>
	</div>
</div>

<div class="bottom_option bot_opOpen">
			<a href="#none" class="btn_opChoice">
				<em>옵션 선택</em>
			</a>

			<div class="box" style="display:none">
				<div class="columnL">
					<strong class="name txt_ellipsis">
						${product.name }
					</strong>
					<c:choose>
<%--세트상품 or 옵션 없는 상품 --%>
						<c:when test="${product.optionYn eq 'N' or product.productTypeCd eq 'PRODUCT_TYPE_CD.SET' }">
						<c:if test="${product.productTypeCd eq 'PRODUCT_TYPE_CD.SET' }">
						
							<c:forEach var="setp" items="${product.pmsSetproducts }" varStatus="index">
								<dl>
									<dd>
										<div class="select_box1" ${fn:length(setp.pmsSaleproducts) < 2 ? 'style="display:none"': ''} >
											<label id="bottom_subproducts_${setp.subProductId }Label">선택하세요</label> 
											<select onchange="changeSetOption(this, '')" id="bottom_subproducts_${setp.subProductId }" subProductId="${setp.subProductId }" >
												<option value="">선택하세요</option>
												<c:forEach var="saleproduct" items="${setp.pmsSaleproducts}">
													<option maxQty="${saleproduct.realStockQty }" ${fn:length(setp.pmsSaleproducts)==1 ? 'selected':''}  value="${saleproduct.saleproductId }" 
													        ${saleproduct.realStockQty eq 0 ? 'disabled' : '' }>${saleproduct.name }${saleproduct.realStockQty eq 0 ? ' (품절)' : '' } </option>
												</c:forEach>
											</select>
											<!-- 단일 옵션일 경우 -->
										</div>
									</dd>
								</dl>								
							</c:forEach>						
						</c:if>		
							<dl class="quantityArea">
								<dt>수량</dt>
								<dd>
									<div class="quantity">
										<button type="button" class="btn_minus" onclick="qtyChange('qty_${product.saleproductId}', _noOptionQty, 'N');">수량빼기</button>
										<input type="text" value="1" onkeydown="return common.chkNumKeyDwn(this, event);" onchange="qtyInput2(this.name, _noOptionQty, this.value)" name="qty_${product.saleproductId}"  maxlength="4" >
										<button type="button" class="btn_plus" onclick="qtyChange('qty_${product.saleproductId}', _noOptionQty, 'Y');">수량추가</button>
									</div>
								</dd>
							</dl>

						</c:when>
<%-- 일반상품 옵션있는 상품 --%>
						<c:when test="${product.optionYn eq 'Y' and product.productTypeCd ne 'PRODUCT_TYPE_CD.SET' }">
							<c:forEach var="optionName" items="${product.pmsProductoptions }" varStatus="index">
								<%--옵션목록 조회 --%>
								<dl>
									<dt>${optionName.optionName}</dt>
									<dd>
										<div class="select_box1">
											<label>선택하세요</label> 
												<select id="prdOption_${index.count }" onchange="changeBottom( 'prdOption_${index.count }', ${fn:length(product.pmsProductoptions)} ,'${product.productId }', this.value)">
													<option value="">선택하세요</option>
												<c:forEach var="option" items="${optionName.optionValues}">
													<option value="${option.optionValue }" ${option.realStockQty==0 ? 'disabled' : '' }>${option.optionDispValue }</option>
												</c:forEach>
											</select>
										</div>
									</dd>
								</dl>
							</c:forEach>
							<p style="text-align:right;margin-top:5px;">* 옵션별로 가격이 상이할 수 있습니다.</p>
							<div class="selectOption" name="selectedSaleproductArea1" style="display: none">
								<ul name="selectedSaleproductArea2_bottom">
								</ul>
							</div>
						</c:when>
					</c:choose>
			
				
					<!-- //16.09.23 : 세트 상품일 경우 -->
				</div>

				<div class="columnR">
					<div class="total_block"  name="totalBlock">
						총 <em name="totalPrice"><fmt:formatNumber value="${initSalePrice}" groupingUsed="true" /></em> 원
					</div>
<c:if test="${product.offshopPickupYn eq 'Y' or product.regularDeliveryYn eq 'Y' }">
					<div class="chk_box">
						<!-- 16.09.23 : 매장에서 픽업하기 추가 -->
						<c:if test="${product.offshopPickupYn eq 'Y' }">
						<label class="chk_style1">
							<em>
								<input type="checkbox"  value="Y" onclick="checkOffshopPickup(this.checked ,'')" id="btnStorePickup2" class="btnStorePickup">
							</em>
							<span>매장에서 픽업하기</span>
						</label>
						</c:if>
						<!-- //16.09.23 : 매장에서 픽업하기 추가 -->
						<c:if test="${product.regularDeliveryYn eq 'Y' }">
						<label class="chk_style1">
							<em>
								<input type="checkbox" id="regularDeliveryYn2" onclick="checkRegularDelivery(this.checked, '')" class="inp_repeat">
							</em>
							<span>정기배송 신청하기</span>
						</label>
						
						</c:if>
					</div>
</c:if>

					<div class="selectOption"  name="regularSelectArea" style="display:none">
						<ul>
							<li>
								<div class="left">
									<span>배송주기/횟수 선택</span> <!-- 16.09.23 : 텍스트 수정 -->
								</div>
								<div class="right">
									<div class="select_box1">
										<label id="deliveryPeriodCd2Label">1주일에 한 번</label> 
										<select id="deliveryPeriodCd2">
											<option value="1">1주일에 한 번</option>
											<option value="2">2주일에 한 번</option>
											<option value="3">3주일에 한 번</option>
											<option value="4">4주일에 한 번</option>
										</select>
									</div>
									<div class="select_box1">
										<label id="deliveryCnt2Label">${product.regularDeliveryMinCnt}</label> 
										<select id="deliveryCnt2" >
											<c:forEach var="cnt" begin="${product.regularDeliveryMinCnt}" end="${product.regularDeliveryMaxCnt}">
											<option value="${cnt }">${cnt }</option>
											</c:forEach>
										</select>
									</div>									
								</div>
							</li>

						</ul>
					</div>
					<div class="btnRegular" name="regularSelectArea2" style="display: none">
						<a href="#none" class="btn_style" style="width:100%" onclick="regularCartLoginCheck()"><span>정기배송 장바구니</span></a>
					</div>
					<div class="btnGroup" name="pickupBuyBtn" style="display:none">
							<a href="#none" style="width:100%" class="btn_style" onclick="pickupCart()"><span>매장픽업 장바구니</span></a>
						</div>	
					<div class="btnGroup" name="normalBuyBtn"> <!-- 버튼이 2개만 나올 경우 div class="btn2ea" 추가 -->
						<c:set value="100" var="btnWidth"/>
				<c:if test="${product.giftYn eq 'Y' }">
							<a href="#none" class="btn_style white btn_gift" onclick="pms.detail.order('${product.productId}','${product.productTypeCd}','Y', '${styleNo}')"><span>선물</span></a> 
				</c:if>
				<c:if test="${ not (not empty  childrenDealId and product.optimalprice.dealId eq childrenDealId ) }">
							<c:set value="${product.giftYn eq 'Y' ? 31.5 : 49 }" var="btnWidth"/>
							<a href="#none" style="width:${btnWidth}%" class="btn_style" onclick="pms.detail.cart('${product.productId}','${product.productTypeCd}', '', '${styleNo}')"><span>장바구니</span></a>
				</c:if>			 
							<a href="#none" style="width:${btnWidth}%" class="btn_style purple" onclick="pms.detail.order('${product.productId}','${product.productTypeCd}', '', '${styleNo}')"><span>바로구매</span></a>
					
					</div>
				</div>
			</div>
		</div>

