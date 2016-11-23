/**
 * 
 */


pms = {
	
	//쇼킹제로 남은 시간 countdown
	countdown : function(divName, endDt) {
		$("#" + divName).countdown(endDt, function(event) {
			var text = '쇼킹제로 남은시간';
			if (ccs.common.mobilecheck()) {
				text = " 남은시간";
			}
			$(this).text(event.strftime(text+ ' : %D일 %H : %M : %S'));
		});
	},
	
	//찜목록 추가
	updateWishlist : function(productId, obj){
		
		if($(obj).hasClass("active")){// 삭제
			mms.common.deleteWishlist(productId, function(){
				//ccs.link.product.detail(productId);
				$(obj).removeClass("active");
			});
		}else{//등록
			var wishlists = [];
			var mmsWishlist = new mms.common.Wishlist(productId, null, null);
			wishlists.push(mmsWishlist);
			mms.common.saveWishlist(wishlists, function(wishlistNo){
				$(obj).addClass("active");
				//ccs.link.product.detail(productId);
			});
		}
	},
	// 상품상세
	detail : {
		//pms.detail.setTotalHtml(price)
		setTotalHtml : function(price){
			$('[name=totalPrice]').html(common.priceFormat(price));
		},
		// 세트상품 선택 단품 세팅  pms.detail.makeSetproductObject()
		makeSetproductObject : function(){
		
			$('select[id^=subproducts_]').each(function(){
				_selectedSetSaleproducts[$(this).attr('subProductId')] = $(this).val();	
			});
				
		},
		// 장바구니 담기 pms.detail.cart(productId, productTypeCd, cartType);
		cart : function(productId, productTypeCd, cartType, styleNo){
			if(productTypeCd=='PRODUCT_TYPE_CD.SET'){
				pms.detail.cartSet(productId, cartType, styleNo);
			}else{
				pms.detail.cartGeneral(productId, cartType, styleNo);
			}
		}, 
		// 세트 상품 장바구니 pms.detail.cartSet(productId)
		cartSet : function(productId, cartType, styleNo){
			
			// 선택 단품을 객체에 담기
			pms.detail.makeSetproductObject();
			
			if(common.isEmptyObject(_selectedSetSaleproducts)){
				alert("옵션을 선택해 주세요.");
				return;
			}
			
			var omsCarts = [];
			
			for(key in _selectedSetSaleproducts){
				if(common.isEmpty(_selectedSetSaleproducts[key])){
					alert("옵션을 선택해 주세요.");
					return;
				}
				
				omsCarts.push({productId : key, saleproductId:_selectedSetSaleproducts[key]});
			}
			
			//console.log(_selectedSetSaleproducts);
			
			var setQty = $('[id^=qty_]').val();
			
			//정기배송관련
			var deliveryCnt = null;
			var deliveryPeriodCd = null;
			var deliveryPeriodValue = null;
			
			
			var cartTypeCd = "CART_TYPE_CD.GENERAL";
			
			if(cartType=='r'){// 정기배송
				
				cartTypeCd = 'CART_TYPE_CD.REGULARDELIVERY';
				deliveryCnt = $('#deliveryCnt').val();
				deliveryPeriodCd = $('#deliveryPeriodCd').val();
				deliveryPeriodCd = "DELIVERY_PERIOD_CD."+deliveryPeriodCd+"WEEK";
				deliveryPeriodValue = 3;// 기본값 화요일
				
			}else if(cartType=='pickup'){// 매장픽업
				cartTypeCd = 'CART_TYPE_CD.PICKUP';
			}
			
			//Cart(productId,saleproductId,omsCarts,cartTypeCd,offshopId,qty,dealId,channelId,couponId,deliveryCnt,deliveryPeriodCd,deliveryPeriodValue)
			var cartData = {productId : productId,
					saleproductId : _saleproductId,
					omsCarts : omsCarts,
					cartTypeCd : cartTypeCd,
					offshopId : _offshopid_,
					qty : setQty,
					dealId : null,					
					couponId : null,
					salePrice : _selectSaleproducts[_saleproductId].getSalePrice(),
					addSalePrice : _selectSaleproducts[_saleproductId].addSalePrice,
					deliveryCnt : deliveryCnt,
					deliveryPeriodCd : deliveryPeriodCd,
					deliveryPeriodValue : deliveryPeriodValue,
					styleNo : styleNo}
			
			//console.log(cartData);
			var cart = new oms.Cart(cartData);
			
			oms.saveCart(cart, function(response){

				
				if(response.RESULT=='SUCCESS'){
					if(global.channel.isMobile!='true'){
						// 상단 카트카운트
						if(cartType!='r'){// 정기배송 이닐때만
							dmspc.header.getCartCount();
						}
						ccs.layer.copyLayerToContentChild("sLayer_cart");
					}else{
						fnLayerPosition( $(".mobile .sLayer_cart") );
					}
				}else{
					alert(response.RESULT);
				}
				// 선택단품 해제
			});
		},
		// 일반상품 장바구니 담기 pms.detail.cartGeneral(productId, cartType)
		cartGeneral : function(productId, cartType, styleNo){
			
			
			var omsCarts = [];
			
			if(common.isEmptyObject(_selectSaleproducts)){
				alert("옵션을 선택해 주세요.");
				return;
			}
			
			var deliveryCnt = null;
			var deliveryPeriodCd = null;
			var deliveryPeriodValue = null;
			var cartTypeCd = "CART_TYPE_CD.GENERAL";
			var offshopid = _offshopid_;
			if(cartType=='r'){// 정기배송
				
				cartTypeCd = 'CART_TYPE_CD.REGULARDELIVERY';
				deliveryCnt = $('#deliveryCnt').val();
				deliveryPeriodCd = $('#deliveryPeriodCd').val();
				deliveryPeriodValue = 3;// 기본값 화요일
				deliveryPeriodCd = "DELIVERY_PERIOD_CD."+deliveryPeriodCd+"WEEK";
			}else if(cartType=='pickup'){// 매장픽업
				cartTypeCd = 'CART_TYPE_CD.PICKUP';
				//offshopid = _selectSaleproducts[key].offshopId;
			}
			
			
			
			var carts = [];
			// 단품별로 카트 담기
			for(key in _selectSaleproducts){

				//alert(cartTypeCd+'|'+deliveryCnt+'|'+deliveryPeriodCd+'|'+deliveryPeriodValue);
				//Cart(productId,saleproductId,omsCarts,cartTypeCd,offshopId,qty,dealId,channelId,couponId,deliveryCnt,deliveryPeriodCd,deliveryPeriodValue)
				var cartData = {productId : productId,
						saleproductId :  _selectSaleproducts[key].saleproductId,
						omsCarts : omsCarts,
						cartTypeCd : cartTypeCd,
						offshopId : _selectSaleproducts[key].offshopId,
						qty :  _selectSaleproducts[key].qty,
						dealId : _dealId,					
						couponId : _couponId,
						salePrice : _selectSaleproducts[key].getSalePrice(),
						addSalePrice : _selectSaleproducts[key].addSalePrice,
						deliveryCnt : deliveryCnt,
						deliveryPeriodCd : deliveryPeriodCd,
						deliveryPeriodValue : deliveryPeriodValue,
						styleNo : styleNo}
				
				//console.log(cartData);
				var cart = new oms.Cart(cartData);
				carts.push(cart);
			}
			
			oms.saveCartList(carts, function(response){
				
				if(response.RESULT=='SUCCESS'){
					
					if(global.channel.isMobile!='true'){
						
						// 상단 카트카운트
						if(cartType!='r'){// 정기배송 이닐때만
							dmspc.header.getCartCount();
						}
						ccs.layer.copyLayerToContentChild("sLayer_cart");
					}else{
						fnLayerPosition( $(".mobile .sLayer_cart") );
					}
					
				}else{
					alert(response.RESULT);
				}
			});
		},
		// 바로구매 pms.detail.order();
		order : function(productId, productTypeCd, giftYn, styleNo){
			if(productTypeCd=='PRODUCT_TYPE_CD.SET'){
				pms.detail.orderSet(productId, giftYn, styleNo);
			}else{
				pms.detail.orderGeneral(productId, giftYn, styleNo);
			}
		},
		// 바로구매
		orderGeneral : function(productId, giftYn, styleNo){
			
			if(common.isEmptyObject(_selectSaleproducts)){
				alert("옵션을 선택해 주세요.");
				return;
			}
			var orders = [];
			// 단품별로 주문 담기
			for(key in _selectSaleproducts){
				// oms.Order(productId, saleproductId, omsOrderproducts, orderQty, dealId, channelId, giftYn, offshopId, couponId)
				var couponId = __price_mode__ == 1 ? _couponId : "";
				var orderData = {	productId : productId,
								saleproductId : _selectSaleproducts[key].saleproductId,
								omsOrderproducts : [],
								orderQty : _selectSaleproducts[key].qty,
								dealId : _dealId,
								giftYn : giftYn,
								offshopId : _offshopid_,
								couponId : couponId,
								styleNo : styleNo
								}
				var order = new oms.Order(orderData);
				orders.push(order);
			}
			
			oms.directOrderList(orders);
			
		},
		orderSet : function(productId, giftYn, styleNo){
			
			// 선택 단품을 객체에 담기
			pms.detail.makeSetproductObject();
			
			if(common.isEmptyObject(_selectedSetSaleproducts)){
				alert("옵션을 선택해 주세요.");
				return;
			}
			
			var omsOrders = [];
			
			for(key in _selectedSetSaleproducts){
				if(common.isEmpty(_selectedSetSaleproducts[key])){
					alert("옵션을 선택해 주세요.");
					return;
				}
				
				omsOrders.push({productId : key, saleproductId:_selectedSetSaleproducts[key]});
			}
			
			//console.log(_selectedSetSaleproducts);
			var setQty = $('[id^=qty_]').val();
			var couponId = __price_mode__ == 1 ? _couponId : "";
		
			var orderData = {	productId : productId,
					saleproductId : _saleproductId,
					omsOrderproducts : omsOrders,
					orderQty : setQty,
					dealId : _dealId,
					giftYn : giftYn,
					offshopId : _offshopid_,
					couponId : couponId,
					styleNo : styleNo
					}
			
			var order = new oms.Order(orderData);
			
			oms.directOrder(order)
		},		
		// i번째 옵션부터 clear함
		clearOptions : function(index){
			
			if(common.isEmpty(index)){
				index = 1;
			}
			// 하위 미선택으로 변경
			for(var i = index ; i <= $('select[name^=prdOption_]').size() ; i++){
				// 옵션 초기화
				$("select[name^=prdOption_" + i + "]").prev().html("선택하세요");
				$("select[name^=prdOption_" + i + "]").html("<option value=''>선택하세요</option>");
				
				//pc 하단 고정 옵션
				$("select[id^=prdOption_" + i + "]").prev().html("선택하세요");
				$("select[id^=prdOption_" + i + "]").html("<option value=''>선택하세요</option>");
			}
		},		
		// 옵션을 초기화한다.
		initOption : function(){
			// 옵션 초기화
			$("select[name^=prdOption_1]").prev().html("선택하세요");
			$("select[name^=prdOption_1]").val("");
			
			//pc 하단 고정 옵션
			$("select[id^=prdOption_1]").prev().html("선택하세요");
			$("select[id^=prdOption_1]").val("");
			
			pms.detail.clearOptions(2);
		},
		
		// 일반상품일때 선택단품 초기화 pms.detail.initSelectedProduct()
		initSelectedProduct : function(){
			
			_selectSaleproducts = {};
			// 옵션 선택 하단 영역 초기화
			$('[name=selectedSaleproductArea1]').hide();
			$('[name^=selectedSaleproductArea2]').html('');
			
			$('[name=totalBlock]').hide();
			
		},
		// pms.detail.selectPickupMode(isPickupMode, productId)
		selectPickupMode : function(isPickupMode, productId){
			
			// 기존 선택 영역 삭제, 선택된 객체 삭제
			if(_optionYn=='Y'){
				
				// 옵션콤보 초기화
				pms.detail.initOption();
				
				// 선택단품 초기화
				pms.detail.initSelectedProduct();

				
			}else{
				// 수량 초기화 : 옵션 없을경우만 해당
				$('[id^=qty]').val(1);
				
				_selectSaleproducts[_saleproductId].qty = 1;
			}
			
			// 총금액 초기화
			if(isPickupMode){
				pms.detail.setTotalHtml(_pickupSalePrice);
			}else{
				pms.detail.setTotalHtml(_initSalePrice);
			}
			
			if(_optionYn=='Y'){
				var targetObject = $('[name=prdOption_1]');
				var optionName = targetObject.attr("optionName");
				var optionSize = $('[name^=prdOption_]').size();
				var pickupProduct = isPickupMode ? 'Y' : '';
				
				// 첫번째 옵션을 재조회, 하위 옵션 클리어
	    		var p = {
					targetOptionName :  optionName, 
					productId : productId,
					pickupProduct : pickupProduct
				} 
			
				// 옵션정보 조회 및 option 생성
				pms.optioncombo.getNextOptions(targetObject, p);
			}
			
		},
		// 옵션콤보 변경 이벤트 : pms.detail.selectProductOption(selectedName, optionSize, productId )
		selectProductOption : function(selectedName, optionSize, productId ){
			
			$('#normalBuyBtn').focus();
			
//			var pickupChecked = $('.btnStorePickup').is(":checked");
			var pickupProduct = __price_mode__ == 3 ? 'Y' : '';// 픽업여부
	    	var p = {
	    			selectedName : selectedName, 
	    			optionSize : optionSize,
	    			productId : productId,
	    			pickupProduct : pickupProduct
	    	};
			
	    	
			pms.optioncombo.selectOption(p, function(commboName){
		    	//단품정보 조회를 위한 파라메터 세팅
				var param = pms.optioncombo.makeSelectedParam(commboName);
				// 옵션 미선택
				if(!param){
					return;
				}
				//픽업여부
//				var pickupChecked = $('.btnStorePickup').is(":checked");
				var pickupProduct = __price_mode__ == 3 ? 'Y' : '';
				
				param.productId = productId;
				param.pickupProduct = pickupProduct;
				if (typeof _dealId !== 'undefined') {
					param.dealId = _dealId;
				}
				if (typeof _couponId !== 'undefined') {
					param.couponId = _couponId;
				}
				
				// 단품정보 조회(ajax)
				pms.optioncombo.getSaleproductInfo(param, function(data){
					selectSaleproduct(data);
				})
				
			});
		},
		selectQnaOption : function(selectedName, optionSize, productId ){
			
	    	var p = {
	    			selectedName : selectedName, 
	    			optionSize : optionSize,
	    			productId : productId 
	    	};
	    	
			pms.optioncombo.selectOption(p, function(commboName){
				
		    	//단품정보 조회를 위한 파라메터 세팅
				var param = pms.optioncombo.makeSelectedParam(commboName);
				// 옵션 미선택
				if(!param){
					return;
				}
				
				param.productId = productId;
				
				// 단품정보 조회(ajax)
				pms.optioncombo.getSaleproductInfo(param, function(data){
					$("#productQnaForm > input[name=saleproductId]").val(data.saleproductId);
				})
				
			});
		},
		// 단품 추가 pms.detail.addSaleproductOption(data)
		addSaleproductOption : function(data) {
			if (!_selectSaleproducts[data.saleproductId]) {

				//console.log(data)
				_selectSaleproducts[data.saleproductId] = data;
				
				_selectSaleproducts[data.saleproductId].getMinPrice = function(){
					return __price_mode__==3 ? _pickupSalePrice : __price_mode__== 2 ? _regularPrice : this.minSalePrice;
				}
				_selectSaleproducts[data.saleproductId].getSalePrice = function(){
					return __price_mode__==3 ? _pickupSalePrice : __price_mode__== 2 ? _regularPrice : this.salePrice;
				}
				
				//alert(_selectSaleproducts[data.saleproductId].getMinPrice());

			} else {

				alert("이미 선택한 옵션입니다.");
				return false;
			}
			
			pms.detail.setTotalSalePrice();

			return true;

		},
		// 전체 가격 pms.detail.setTotalSalePrice()
		setTotalSalePrice : function() {
			
			var total = pms.detail.getTotalPrice();
			pms.detail.setTotalHtml(total);
			
		},
		// 옵션없는 상품의 토탈가격 pms.detail.makeTotalPriceNoOption(qty)
		makeTotalPriceNoOption : function(qty) {
			var total = 0;
			if(__price_mode__==3){			//매장픽업
				total = Number(_pickupSalePrice) * qty;
			}else if(__price_mode__==2){	//정기배송
				total = Number(_regularPrice) * qty;
			}else{							//일반가
				total = Number(_minSalePrice) * qty;
			}
			pms.detail.setTotalHtml(total);
			
		},
		getTotalPrice : function() {
			// 
			var total = 0;
			for (key in _selectSaleproducts) {
				total += _selectSaleproducts[key].qty * _selectSaleproducts[key].getMinPrice();
				
			}
			return total;
		},
		// 수량변경 pms.detail.qtyChange(qtyId, maxQty, plusYn) 
		qtyChange : function(qtyId, maxQty, plusYn) {

			
			var qty = 0;
			var max = 9999;

			if(__price_mode__==3){//픽업선택이면
			}
			else if(!common.isEmpty(_dealId)){// 딜일경우 딜수량 우선
				if(_maxPersonQty==0){
					// 딜일때 최대구매수량 체크해야 하는가?
					maxQty = _dealStockQty;
				}else{
					// 딜일때 최대구매수량 체크해야 하는가?
					maxQty = _dealStockQty > _maxPersonQty ? _maxPersonQty : _dealStockQty;
				}
				
			}else if(_maxPersonQty > 0 && _maxPersonQty < maxQty){
				//딜 아닐경우 1인최대 구매수량 설정
				maxQty = _maxPersonQty;
			}
			
			// 파라메터 max값 설정
			if(!common.isEmpty(maxQty) && maxQty > 0){
				max = maxQty;
			}
			var min = 1;
			qty = Number($('#' + qtyId).val());
			
			// 직접입력시 값지웠을경우
			if(!plusYn && qty=='' || qty==0){
				qty = 1;
				$('#' + qtyId).val(qty);
			}
			
			qty = Number($('#' + qtyId).val());
			
			if(plusYn=='Y'){
				qty++;
			}else if(plusYn=='N'){
				qty--;
			}
			
			var isMax = false;
			// max체크
			if(qty > max){
				
				isMax = true;
				
				alert("최대 구매가능 수량을 초과하였습니다.");
				
				if(!plusYn ){
					$('#' + qtyId).val(1);
					qty = 1;
				}else if(plusYn=='Y'){
					
					//플러스 버튼이면 수량 제외
					qty = $('#' + qtyId).val();
					
				}
			}
			
			// 수량박스 현행화
			if (plusYn == 'Y' && !isMax) {
				qty = ccs.common.quantityPlus($('#' + qtyId));
			} else if (plusYn == 'N') {
				qty = ccs.common.quantityMinus($('#' + qtyId));
			}
			//alert(qty)
			
	
			// 저장객체에 단품수량 저장
			pms.detail.setSaleproductQty($('#' + qtyId).attr('saleproductId'), qty);
			
			// 객체에 수량 설정
			if('Y'!=_setProductYn && 'Y'==_optionYn){
				pms.detail.setTotalSalePrice();
			}else{
				pms.detail.makeTotalPriceNoOption(qty);
			}

			
		},
		// 단품 수량 설정
		setSaleproductQty : function(saleproductId, qty) {
			_selectSaleproducts[saleproductId].qty = qty;
		},
		//단품 삭제
		delSaleproductOption : function(salepproductId) {

			// 객체 삭제
			delete _selectSaleproducts[salepproductId];

			// 화면 삭제
			$('[name=selectedLi_' + salepproductId+']').remove();
			if (common.isEmptyObject(_selectSaleproducts)) {
				$('[name=selectedSaleproductArea1]').hide();
				$('[name=totalBlock]').hide();
			}

			pms.detail.setTotalSalePrice();

		},
		// QmA목록 조회
		getQnaList : function(p, productId) {

			var param = {
				productId : productId
			};

			if (p && p.secret) {
				param.secretYn = "N";
			}else{
				
			}
			
			$("input[name=currentQnaPage]").val(Number(1));
			
			$.get("/pms/product/qna/list/ajax", param).done(function(html) {
				listCallback(html);
			});

		},
		// 상품평 목록 조회
		getProductReview : function(type, orderBy, productId) {
			if(common.isEmpty(type)){
				$("#reviewSearchType").val('all');
			}else if(type == 'all'){
				$("#reviewSearchType").val(type);
			}else if(type == 'img'){
				$("#reviewSearchType").val(type);
			}else if(type == 'permit'){
				$("#reviewSearchType").val(type);
			}else if(type == 'type'){
				
			}
			
			var searchOrderBy;
			if(common.isEmpty(orderBy)){
				searchOrderBy = $("#reviewSelectValue").val();
			}else{
				searchOrderBy = orderBy;
			}
			
			var param = {
				productId : productId,
				type : $("#reviewSearchType").val(),
				orderBy : searchOrderBy
			};
			
			$("input[name=currentReviewPage]").val(Number(1));
			

			$.get("/pms/product/reviews/list/ajax", param).done(function(html) {
				reviewCallback(html);
			})

		},
//		상품상세 상품평, QNA 모바일 페이징
		pagingListCallback : function(productId){
			var data = {};
			
			if($(".mobile .content_fixed .prod_info_tab").find("li:eq(2)").hasClass("on")){
				
				$("#reviewList").append('<div id="lodingBar" align="center"><img src="/resources/img/mobile/Loading.gif" alt="" /></div>');
				
				$("input[name=currentReviewPage]").val(Number($("input[name=currentReviewPage]").val())+ 1);
				here = "#reviewList";
				url = "/pms/product/reviews/list/ajax";
				data = {productId : productId,
						type : $("#reviewSearchType").val(),
						orderBy : $("#reviewSelectValue").val(),
						currentPage: $("input[name=currentReviewPage]").val(), isScroll:'true'};
			}
			else if($(".mobile .content_fixed .prod_info_tab").find("li:eq(3)").hasClass("on")){
				
				$("#qnaDiv").append('<div id="lodingBar" align="center"><img src="/resources/img/mobile/Loading.gif" alt="" /></div>');
				
				$("input[name=currentQnaPage]").val(Number($("input[name=currentQnaPage]").val())+ 1);
				here = "#qnaDiv";
				url = "/pms/product/qna/list/ajax";
				if(document.getElementById("secretCheck").checked){
					data = {productId : productId, currentPage: $("input[name=currentQnaPage]").val(), isScroll:'true'};
				} 
				else{
					data = {productId : productId, secretYn : 'N', currentPage: $("input[name=currentQnaPage]").val(), isScroll:'true'};
				}
			}
			
			$.get( url, data ).done(function( html ) {
				console.log(html);
				$(here).append(html);
				common.hideLoadingBar();
			});
		}			
	},
	// 옵션선택 공통 처리
	optioncombo : {
		// pms.optioncombo.makeSelectedParam() 선택된 옵션들로 파라메터 생성
    	makeSelectedParam : function(commboName){
			var param = {};
			var selecteds = [];
			var notSelected = false;
			$('select[name^='+commboName+']').each(function(data){
				if(!common.isEmpty($(this).val())){
					selecteds.push({optionName : $(this).attr("optionName"), optionValue:$(this).val()});
					//console.log(selecteds);
				}else{
					notSelected = true;
				}
			});
			if(notSelected){
				return null;
			}
			param.selectedOptions = selecteds;
			return param;
	    }, 
	    //pms.optioncombo.initChildCombo() 하위 콤보박스들 초기화
	    initChildCombo : function(selectedName){
	    	
	    	var selectedComboObj = $('[name='+selectedName+']');
	    	
	    	// 옵션 콤보 이름
	    	var commboName = selectedName.split("_")[0];
	    	var currentOptionNo = Number(selectedName.split("_")[1]);
			var optionSize = $('select[name^='+commboName+']').size();
			
			// 현재 선택 라벨
			selectedComboObj.prev().html($('[name='+selectedName+'] option:selected').text());
			
			// 하위 미선택으로 변경
			for(var i = 1 ; i <= optionSize ; i++){
				if(i > currentOptionNo){// 하위 콤보이면
					// 옵션 라벨 초기화
					$("select[name="+commboName+"_" + i + "]").html("");
					$("select[name="+commboName+"_"+ i +"]").val("");	
					selectedComboObj.prev().html($('[name='+selectedName+'] option:selected').text());
					
					if(!common.isEmpty(selectedComboObj.val()) && i == currentOptionNo + 1 ){// 
						$("select[name="+commboName+"_"+ i +"]").prev().html("옵션 조회중..");
					}else{
						$("select[name="+commboName+"_"+ i +"]").prev().html("선택하세요");
					}
				}
			}
			
	    },
	    // pms.optioncombo.getOptionParam() 선택된 옵션들을 객체로 리턴
	    getOptionParam : function(commboName){
			
			// 파라메터 설정
			var selecteds = [];
			$('select[name^='+commboName+']').each(function(){
				
				if(!common.isEmpty($(this).val())){
					selecteds.push({optionName : $(this).attr("optionName"), optionValue:$(this).val()})
				}
			})
			
			return selecteds;
			
	    },
	    // pms.optioncombo.getNextOptions 다음 옵션목록 구성
	    getNextOptions : function(targetObj, param){
	    	
			$.ajax({
				  method: "post",
				  url: "/api/pms/product/optionvalue/list",
				  contentType:"application/json; charset=UTF-8",
				  data: JSON.stringify(param) 
				}).done(function( data ) {
					
					var options = "<option value=''>선택하세요</option>";
					for(var i = 0 ; i < data.length ; i++){
						var isSoldout = data[i].realStockQty==0 ? 'disabled ' : '';
						options += "<option "+isSoldout+" value=\""+data[i].optionValue+"\">"+data[i].optionDispValue+"</option>";
					}
					
					targetObj.html(options);
					targetObj.prev().html("선택하세요");
					
				});
	    },
	    // pms.optioncombo.selectOption() 옵션 선택 이벤트
	    selectOption : function(param, callback){
	    	
	    	var selectedName = param.selectedName;
	    	var optionSize = param.optionSize;
	    	var productId = param.productId;
	    	
	    	var selectedIndex = Number(selectedName.split("_")[1]);
	    	var commboName = selectedName.split("_")[0];
	    	var isLastOption = false;
	    	var nextOptionName;// 다움 옵션명
	    	var nextObj;//다음 오브젝트
	    	var selectValue = $('[name='+selectedName+']').val();
	    	
	    	if(selectedIndex == optionSize){
	    		//마지막 옵션
	    		isLastOption = true;
	    	}else{
	    		var nextIndex = selectedIndex + 1;
	    		nextObj = $('[name='+commboName+'_' + nextIndex + ']');
	    		nextOptionName = nextObj.attr("optionName");
	    	}
	    	
	    	if( !isLastOption){// 마직막 옵션이 아니면 
	    		
	    		// 하위 옵션 초기화
	    		pms.optioncombo.initChildCombo(selectedName);
	    		
	    		if(common.isEmpty(selectValue)){
	    			return;
	    		}
	    		// 하위 콤보 목록을 조회 하기 위한 파라메터 생성
	    		var selectedOptions = pms.optioncombo.getOptionParam(commboName);
	    		
	    		var p = {
	    				targetOptionName :  nextOptionName, 
	    				productId : productId, 
	    				selectedOptions : selectedOptions,
	    				pickupProduct : param.pickupProduct
	    			} 
	    		
	    		// 하위 옵션정보 조회 및 option 생성
				pms.optioncombo.getNextOptions(nextObj, p);
				
			}else if(isLastOption){ // 단품 선택
				if(callback){
					callback(commboName);
				}
			}
		},
    	// 단품정보 조회 pms.optioncombo.getSaleproductInfo(param, callback)
    	getSaleproductInfo : function(param, callback){
    		
			// 선택한 옵션의 단품정보 조회
			$.ajax({
				  method: "post",
				  url: "/api/pms/product/saleproduct",
				  contentType:"application/json; charset=UTF-8",
				  data: JSON.stringify(param) 
				}).done(function( data ) {

					callback(data);
					
				});
    	}
	},
    common : {
    	/**
    	 * 추천상품조회
    	 * 
    	 * @param  
    	 * 		appendedObj : append 될 태그dom
    	 * 		param :  http://assets.recobell.io/rb-api/doc/readme.html 참조
    	 *      callbackFn : 콜백함수
    	 */
    	getRecommendationProductList : function(appendedObj, param, callbackFn){
            $.ajax({
            	type: 'POST',
            	url: '/pms/product/recommenadation/ajax',
            	data: param,
            	success:function(data) {
            		
            		appendedObj.html(data);
            		
            		if(callbackFn){
            			callbackFn();
            		}
            	}
            });  
    
    	},  	
        // 체크박스 선택/해제  pms.common.checkBoxChecked(id, checked)
    	checkBoxChecked : function(id, checked){
    		$("input:checkbox[id="+id+"]").prop("checked", checked);
    		
    		if(checked){
    			$("input:checkbox[id="+id+"]").parent().addClass("selected");
    		}else{
    			$("input:checkbox[id="+id+"]").parent().removeClass("selected");
    		}
    		
    	}
    },
    
    brand : {
    	// 브랜드관 브랜드별 베스트 상품(템플릿B)
    	best : function(brandId, currentPage) {
    		if (common.isEmpty(currentPage)) {
    			currentPage = 1;
    		}
    		
    		if ($(".mainCon .brand_home #best_area_loading").is(":hidden")) {
    			$("[name=bestProduct]").hide();
    			$(".mainCon .brand_home #best_area_loading").show();
    		}
    		$.ajax({
    			contentType:"application/json; charset=UTF-8",
            	type: 'POST',
            	url: '/pms/product/brand/best',
            	data: JSON.stringify({"brandId" : brandId, "currentPage" : currentPage})
            }).done(function(html) {
            	$("[name=bestProduct]").html(html);
            	if (ccs.common.mobilecheck()) {
            		//ccs.mainSwipe.calculateHeight();
            		var calcHeight = setHeightInterval();
        			intervalCalcHeight(calcHeight);
        			
            		swiperCon('brandSwiper_realProdList_2', '3'); //베스트 상품 스와이프
            	} else {
            		
            		swiperCon('brandSwiper_realProdList_2', 400, 4, 12, false, true, 4);
            		$(".mainCon .brand_home #best_area_loading").hide();
            	}

            	$("[name=bestProduct]").show();
            });
    	},
    	
    	realtimeProduct : function(erpBrandId, div) {
    		var currentPage = $("#realtime_pagingForm #hid_current").val();
    		if (common.isEmpty(currentPage)) {
    			currentPage = 1;
    		}
    		
    		var moreFlag = false;
    		if (common.isEmpty(div)) {
    			moreFlag = true;
    		} else {
    			if (div == "prev") {
        			if (currentPage > 1) {
        				currentPage = Number(currentPage) - 1;
        				moreFlag = true;
        			}
        		} else if (div == "next") {
        			var realtimeTotal = $("#realtime_pagingForm #hid_totalCnt").val();
        			if (currentPage*4 < realtimeTotal) {
        				currentPage = Number(currentPage) + 1;
        				moreFlag = true;
        			}
        		}
    		}
    		    		
    		if (moreFlag) {
        		var data = {erpBrandId: erpBrandId, currentPage: currentPage};
        		
        		if ($(".mainCon .brand_home #realtime_area_loading").is(":hidden")) {
        			$("[name=realtimeProduct]").hide();
        			$(".mainCon .brand_home #realtime_area_loading").show();
        		}

        		$.ajax({
    				url : "/pms/product/brand/realtimeProduct",
    				type : "POST",
    				contentType:"application/json; charset=UTF-8",
    				data: JSON.stringify(data) 
    			}).done(function(html) {
    				$("[name=realtimeProduct]").html(html);
    	    		
    	    		if (ccs.common.mobilecheck()) {
    	    			//ccs.mainSwipe.calculateHeight();
    	    			var calcHeight = setHeightInterval();
            			intervalCalcHeight(calcHeight);
            			
    	    			swiperCon('brandSwiper_realProdList_1', '3'); //실시간인기상품   	    			
    	    		} else {
    	    			swiperCon('brandSwiper_realProdList_1', 400, 4, 12, false, true, 4);
    	    			$(".mainCon .brand_home #realtime_area_loading").hide();
    	    		}
    	    		
    	    		$("[name=realtimeProduct]").show();
    			});
    		}	
    	}
    }
};



