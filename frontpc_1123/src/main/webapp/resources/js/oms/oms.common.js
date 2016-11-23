
//주문
oms = {
		url : {
			orderSheet : "/oms/order/sheet",	//주문서
			saveMobile : "/api/oms/order/saveMobile",	//mobile 주문데이터 저장
			saveSession : "/api/oms/order/saveSession",	//session 주문데이터 저장
			saveOrder : "/api/oms/order/save",			//주문 저장
			pgParam : "/api/oms/pg/param",				//LG param
			kakaoTxnid : "/api/oms/pg/kakao/txnid",		//kakao txnid
			saveRegular : "/api/oms/order/regular/save",	//정기배송 저장
			saveBilling : "/api/oms/order/billing/save",	//정기배송 빌링키 저장
			interestList : "/api/oms/pg/interestList",	//무이자정보 setting
			checkOrder : "/api/oms/order/checkOrder",		//주문서 check
			
			calcRegularDate : "/api/oms/order/calcRegularDate",	//정기배송 일자 계산
			saveCartMulti : "/api/oms/cart/saveMulti"	//장바구니 담기 multi
		},
		Cart : function(data){
//			var productId,saleproductId,omsCarts,cartTypeCd,offshopId,qty,dealId,couponId,deliveryCnt,deliveryPeriodCd,deliveryPeriodValue;
			
			this.productId = data.productId;
			this.saleproductId = data.saleproductId;
			this.omsCarts = data.omsCarts;
			this.cartTypeCd = data.cartTypeCd;
			this.orgOffshopId = data.offshopId;	//매장픽업 매장 or 추천매장
			this.offshopId = data.offshopId;	//매장픽업 매장 or 추천매장
			this.qty = data.qty;			
			this.dealId = data.dealId;
			this.salePrice = data.salePrice;
			this.addSalePrice = data.addSalePrice;
			this.couponId = data.couponId;
			this.deliveryCnt = data.deliveryCnt;
			this.deliveryPeriodCd = data.deliveryPeriodCd
			this.deliveryPeriodValue = data.deliveryPeriodValue
			this.styleNo = data.styleNo
		},
		saveCart : function(omsCart,callback){
			/**
			 * {productId : '', saleproductId : '', omsCarts :
			 * 											[
			 * 												{productId:'',saleproductId:''},
			 * 												{productId:'',saleproductId:''}
			 * 											]
			 *  cartTypeCd : '',dealId : '', offshopId : '', qty : '' ,couponId : '' 
			 * }
			 * 
			 * 
			 * productId,	(필수)
			 * saleproductId,	(필수)
			 * omsCart,	(SET일때 필수)
			 * cartTypeCd,	(default 일반)
			 * dealId,
			 * ,
			 * offshopId,
			 * couponId,	(최적가 적용쿠폰id)
			 * qty,	(default 1)
			 * 
			 * 정기배송
			 * deliveryCnt
			 * deliveryPeriodCd
			 * deliveryPeriodValue
			 */
			
			//console.log(omsCart);
			
			jData = omsCart;
			
			var cartTypeCd = "CART_TYPE_CD.GENERAL";
			var dealId = null;			
			var couponId = null;			
			var offshopId = jData.offshopId;
			var productId = jData.productId;
			var saleproductId = jData.saleproductId;
			var salePrice = null;
			var addSalePrice = null;
			var deliveryCnt = jData.deliveryCnt;
			var deliveryPeriodCd = jData.deliveryPeriodCd;
			var deliveryPeriodValue = jData.deliveryPeriodValue;
			var qty = "1";
			var subProducts = jData.omsCarts;
			var styleNo = null;
			
			if(common.isNotEmpty(jData.cartTypeCd)){
				cartTypeCd = jData.cartTypeCd;
			}
			if(common.isNotEmpty(jData.dealId)){
				dealId = jData.dealId;
			}			
			if(common.isNotEmpty(jData.couponId)){
				couponId = jData.couponId;
			}
			if(common.isNotEmpty(jData.qty)){
				qty = jData.qty;
			}
			
			if(common.isEmpty(jData.salePrice)){
				alert("상품 판매가가 없습니다.");
				return;
			}
			if(common.isEmpty(jData.addSalePrice)){
				alert("단품 판매가 없습니다.");
				return;
			}
			
			if(common.isNotEmpty(jData.salePrice)){
				salePrice = jData.salePrice;
			}
			if(common.isNotEmpty(jData.addSalePrice)){
				addSalePrice = jData.addSalePrice;
			}
			
			if(common.isNotEmpty(jData.deliveryCnt)){
				deliveryCnt = jData.deliveryCnt;
			}
			if(common.isNotEmpty(jData.deliveryPeriodCd)){
				deliveryPeriodCd = jData.deliveryPeriodCd;
			}
			if(common.isNotEmpty(jData.deliveryPeriodValue)){
				deliveryPeriodValue = jData.deliveryPeriodValue;
			}
			if(common.isNotEmpty(jData.styleNo)){
				styleNo = jData.styleNo;
			}
			if(common.isEmpty(jData.productId)){
				alert("상품ID가 없습니다.");
				return;
			}
			if(common.isEmpty(jData.saleproductId)){
				alert("단품ID가 없습니다.");
				return;
			}
			
			if(cartTypeCd == "CART_TYPE_CD.PICKUP"){
				if(common.isEmpty(offshopId)){
					alert("매장픽업은 매장이 선택되어야합니다.");
					return;
				}
			}
			
			if(cartTypeCd == "CART_TYPE_CD.REGULARDELIVERY"){
				if(common.isEmpty(deliveryCnt)){
					alert("정기배송은 정기배송횟수 선택되어야합니다.");
					return;
				}
				if(common.isEmpty(deliveryPeriodCd)){
					alert("정기배송은 정기배송주기가 선택되어야합니다.");
					return;
				}
				if(common.isEmpty(deliveryPeriodValue)){
					alert("정기배송은 정기배송요일이 선택되어야합니다.");
					return;
				}
			}
			
			var _saveCartForm = $('<form>',{
				'name' : '_saveCartForm'
			}).append($('<input>',{
				'name' : 'keepYn',
				'value' : 'N',
				'type' : 'hidden'
			})).append($('<input>',{
				'name' : 'cartTypeCd',
				'value' : cartTypeCd,
				'type' : 'hidden'
			})).append($('<input>',{
				'name' : 'dealId',
				'value' : dealId,
				'type' : 'hidden'
			})).append($('<input>',{
				'name' : 'orgOffshopId',
				'value' : offshopId,
				'type' : 'hidden'
			})).append($('<input>',{
				'name' : 'offshopId',
				'value' : offshopId,
				'type' : 'hidden'
			})).append($('<input>',{
				'name' : 'couponId',
				'value' : couponId,
				'type' : 'hidden'
			})).append($('<input>',{
				'name' : 'productId',
				'value' : productId,
				'type' : 'hidden'
			})).append($('<input>',{
				'name' : 'saleproductId',
				'value' : saleproductId,
				'type' : 'hidden'
			})).append($('<input>',{
				'name' : 'qty',
				'value' : qty,
				'type' : 'hidden'
			})).append($('<input>',{
				'name' : 'salePrice',
				'value' : salePrice,
				'type' : 'hidden'
			})).append($('<input>',{
				'name' : 'addSalePrice',
				'value' : addSalePrice,
				'type' : 'hidden'
			})).append($('<input>',{
				'name' : 'deliveryCnt',
				'value' : deliveryCnt,
				'type' : 'hidden'
			})).append($('<input>',{
				'name' : 'deliveryPeriodCd',
				'value' : deliveryPeriodCd,
				'type' : 'hidden'
			})).append($('<input>',{
				'name' : 'deliveryPeriodValue',
				'value' : deliveryPeriodValue,
				'type' : 'hidden'
			})).append($('<input>',{
				'name' : 'styleNo',
				'value' : styleNo,
				'type' : 'hidden'
			}))
			
			if(common.isNotEmpty(subProducts)){
				for(var i=0;i<subProducts.length;i++){
					var subProductId = subProducts[i].productId;
					var subSaleproductId = subProducts[i].saleproductId;
					
					_saveCartForm.append($('<input>',{
						'name' : 'omsCarts['+i+'].productId',
						'value' : subProductId
					})).append($('<input>',{
						'name' : 'omsCarts['+i+'].saleproductId',
						'value' : subSaleproductId
					}))								
				}
			}
			
//			
//			var form = '\
//						<form name="_saveCartForm" id="_saveCartForm">\
//							<input type="text" name="keepYn" value="N"/>\
//							<input type="text" name="cartTypeCd" value="'+cartTypeCd+'"/>\
//							<input type="text" name="dealId" value="'+dealId+'"/>\
//							<input type="text" name="offshopId" value="'+offshopId+'"/>\
//							<input type="text" name="productId" value="'+productId+'"/>\
//							<input type="text" name="saleproductId" value="'+saleproductId+'"/>\
//							<input type="text" name="qty" value="'+qty+'"/>\
//							'+setSub+'\
//						</form>\
//						';
			
			
			var data = _saveCartForm.serialize();						
			common.showLoadingBar();
			$.ajax({
				url : "/api/oms/cart/save",
				type : "POST",
				data : data
			}).done(function(response){
				if(response.RESULT == "SUCCESS"){
					callback(response);					
				}else{
					alert(response.MESSAGE);
				}				
				common.hideLoadingBar();
			}).fail(function(){common.hideLoadingBar();});
		},
		saveCartList : function(omsCartList,callback){
			/**
			 * 아래 object list
			 * {productId : '', saleproductId : '', omsCarts :
			 * 											[
			 * 												{productId:'',saleproductId:''},
			 * 												{productId:'',saleproductId:''}
			 * 											]
			 *  cartTypeCd : '',dealId : '', offshopId : '', qty : '' ,couponId : '' 
			 * }
			 * 
			 * 
			 * productId,	(필수)
			 * saleproductId,	(필수)
			 * omsCart,	(SET일때 필수)
			 * cartTypeCd,	(default 일반)
			 * dealId,
			 * ,
			 * offshopId,
			 * couponId,	(최적가 적용쿠폰id)
			 * qty,	(default 1)
			 * 
			 * 정기배송
			 * deliveryCnt
			 * deliveryPeriodCd
			 * deliveryPeriodValue
			 * styleNo
			 */
			
			for(var i=0;i<omsCartList.length;i++){
				
				jData = omsCartList[i];
				
				var cartTypeCd = "CART_TYPE_CD.GENERAL";
				var dealId = null;				
				var couponId = null;
				var offshopId = jData.offshopId;
				var productId = jData.productId;
				var saleproductId = jData.saleproductId;
				var deliveryCnt = jData.deliveryCnt;
				var deliveryPeriodCd = jData.deliveryPeriodCd;
				var deliveryPeriodValue = jData.deliveryPeriodValue;
				var qty = "1";
				var subProducts = jData.omsCarts;
				var styleNo = null;
				
				if(common.isNotEmpty(jData.cartTypeCd)){
					cartTypeCd = jData.cartTypeCd;
				}
				if(common.isNotEmpty(jData.dealId)){
					dealId = jData.dealId;
				}
				
				if(common.isEmpty(jData.salePrice)){
					alert("상품 판매가가 없습니다.");
					return;
				}
				if(common.isEmpty(jData.addSalePrice)){
					alert("단품 판매가가 없습니다.");
					return;
				}
				
				if(common.isNotEmpty(jData.salePrice)){
					salePrice = jData.salePrice;
				}
				if(common.isNotEmpty(jData.addSalePrice)){
					addSalePrice = jData.addSalePrice;
				}
				if(common.isNotEmpty(jData.couponId)){
					couponId = jData.couponId;
				}
				if(common.isNotEmpty(jData.qty)){
					qty = jData.qty;
				}
				if(common.isNotEmpty(jData.deliveryCnt)){
					deliveryCnt = jData.deliveryCnt;
				}
				if(common.isNotEmpty(jData.deliveryPeriodCd)){
					deliveryPeriodCd = jData.deliveryPeriodCd;
				}
				if(common.isNotEmpty(jData.deliveryPeriodValue)){
					deliveryPeriodValue = jData.deliveryPeriodValue;
				}
				if(common.isNotEmpty(jData.styleNo)){
					styleNo = jData.styleNo;
				}
				if(common.isEmpty(jData.productId)){
					alert("상품ID가 없습니다.");
					return;
				}
				if(common.isEmpty(jData.saleproductId)){
					alert("단품ID가 없습니다.");
					return;
				}
				
				if(cartTypeCd == "CART_TYPE_CD.PICKUP"){
					if(common.isEmpty(offshopId)){
						alert("매장픽업은 매장이 선택되어야합니다.");
						return;
					}
				}
				
				if(cartTypeCd == "CART_TYPE_CD.REGULARDELIVERY"){
					if(common.isEmpty(deliveryCnt)){
						alert("정기배송은 정기배송횟수 선택되어야합니다.");
						return;
					}
					if(common.isEmpty(deliveryPeriodCd)){
						alert("정기배송은 정기배송주기가 선택되어야합니다.");
						return;
					}
					if(common.isEmpty(deliveryPeriodValue)){
						alert("정기배송은 정기배송요일이 선택되어야합니다.");
						return;
					}
				}
				
				omsCartList[i].cartTypeCd = cartTypeCd;
				omsCartList[i].qty = qty;
			}
	 		console.log(omsCartList);
			var data = JSON.stringify(omsCartList)
			common.showLoadingBar();
			$.ajax({
				contentType : "application/json; charset=UTF-8",
				url : oms.url.saveCartMulti,
				type : "POST",
				data : data
			}).done(function(response){
				if(response.RESULT == "SUCCESS"){
					callback(response);					
				}else{
					alert(response.MESSAGE);
				}				
				common.hideLoadingBar();
			}).fail(function(){common.hideLoadingBar();});
		},
		Order : function(data){
//			productId,saleproductId,omsOrderproducts,orderQty,dealId,,giftYn,offshopId,couponId
			this.productId = data.productId;
			this.saleproductId = data.saleproductId;
			this.omsOrderproducts = data.omsOrderproducts;						
			this.orderQty = data.orderQty;			
			this.dealId = data.dealId;			
			this.giftYn = data.giftYn;
			this.offshopId = data.offshopId;	//추천매장
			this.couponId = data.couponId;
			this.styleNo = data.styleNo;
		},
		presentList : {			
			open : function(frm){
					common.showLoadingBar();
					$.ajax({
						url : "/oms/order/inner/presentList",
						type : "POST",
						data : frm.serialize()
					}).done(function(response){
						$(".content").after(response);
						
						$(".header_mo, .mobile .content, .footer_mo").hide();
						$(window).scrollTop(0);
						
		//				$("#presentDiv").html(response);		
						
						$(".mobile .pop_wrap .btn_x").off("click").on({
							"click" : function() {
								$(this).closest(".pop_wrap").hide();
								$(".mobile .content").removeClass("content_fixed").show();
								$(".header_mo, .footer, .footer_mo").show();
								$(".mobile").removeAttr("style");
							}
						});
						common.hideLoadingBar();
					}).fail(function(){common.hideLoadingBar();});
			},
			close : function(){				
				$(".header_mo, .mobile .content, .footer_mo").show();
//				$(window).scrollTop(100);
//				$("#presentListDiv").remove();				
			}
		},		
		submitOrder : function(){
			var _orderForm = $("#_orderForm");		
			common.showLoadingBar();
//			console.log(_orderForm);
//			return;
			$.ajax({
				url : oms.url.checkOrder,
				type : "POST",
				data : _orderForm.serialize()
			}).done(function(response){
				if(response.RESULT == "SUCCESS"){
					var view = response.view;
					if(view == "presentList"){
						//popup
//						var url = "/oms/order/presentList";
//						var title= "present_list";
//						_orderForm.attr("target",title);
//						_orderForm.attr("action","/oms/order/presentList");
//						var win = window.open("",title);
//						_orderForm.submit();
						
						//layer
						_orderForm.attr("action",oms.url.orderSheet);
						oms.presentList.open(_orderForm);
						common.hideLoadingBar();
					}else if(view == "sheet"){
						_orderForm.append($('<input>',{
							'name' : 'orderStat',
							'value' : 'ORDERSHEET',
							'type' : 'hidden'
						}));
						_orderForm.attr("action",oms.url.orderSheet);
						_orderForm.attr("target","");
						_orderForm.submit();
					}					
				}else{
					alert(response.MESSAGE);
					common.hideLoadingBar();
				}											
			}).fail(function(){
				common.hideLoadingBar();
			});
		},
		orderLogin : function(){
			var result = "";
			var isMobile = global.channel.isMobile;
//			alert(isApp);
			mms.common.isLogin(function(response){
				result = response;				
//				if(isApp != "true"){
				if (result == "1") {
					oms.submitOrder();
				} else {
					
//					if(isMobile == "true"){
						var _orderForm = $("#_orderForm");
						$.ajax({ 				
							url : oms.url.saveSession,
							type : "POST",		
							data : _orderForm.serialize()
						}).done(function(response){
							if(response.RESULT == "SUCCESS"){
								ccs.link.login({type:"ORDER"});
							}else{
								alert(response.MESSAGE);
								oms.showOrderLoading(false);
							}		
						}).fail(function (jqXHR, textStatus, errorThrown) { alert("주문 session 저장 실패.")});
//					}else{
//						ccs.link.login("","ORDER");					
//					}		
				}
								
			});			
		},
		cartOrder : function(cartProductNos){
			
			$("#_orderForm").remove();
			
			var _orderForm = $('<form>',{
				'name' : '_orderForm',
				'id' : '_orderForm',
				'style' : 'display:none'
			}).append($('<input>',{
				'name' : 'cartProductNos',
				'value' : cartProductNos,
				'type' : 'hidden'
			}))
			
			
			_orderForm.attr("method","post");	
			
			_orderForm.appendTo('body');
			
			oms.orderLogin();
						
		},
		directOrder : function(omsOrder){
			/**
			 * {productId : '', saleproductId : '', omsOrderproducts :
			 * 											[
			 * 												{productId:'',saleproductId:''},
			 * 												{productId:'',saleproductId:''}
			 * 											]
			 *  dealId : '',  orderQty : '', giftYn : '',couponId:'' 
			 * }
			 * 
			 * 
			 * productId,	(필수)
			 * saleproductId,	(필수)
			 * omsOrderproducts,	(SET일때 필수) 
			 * dealId,
			 * ,
			 * orderQty,	(default 1)
			 * giftYn
			 * offshopId : 추천매장
			 * couponId : 최적가 적용 쿠폰id
			 */
			jData = omsOrder;
			
			var dealId = null;			
			var giftYn = null;
			var offshopId = null;
			var couponId = null;
			var productId = jData.productId;
			var saleproductId = jData.saleproductId;
			var orderQty = "1";
			var subProducts = jData.omsOrderproducts;	
			var styleNo = null;
			
			if(common.isNotEmpty(jData.cartTypeCd)){
				cartTypeCd = jData.cartTypeCd;
			}
			if(common.isNotEmpty(jData.dealId)){
				dealId = jData.dealId;
			}
			if(common.isNotEmpty(jData.giftYn)){
				giftYn = jData.giftYn;
			}
			if(common.isNotEmpty(jData.offshopId)){
				offshopId = jData.offshopId;
			}
			if(common.isNotEmpty(jData.orderQty)){
				orderQty = jData.orderQty;
			}
			if(common.isNotEmpty(jData.couponId)){				
				couponId = jData.couponId;
			}
			if(common.isNotEmpty(jData.styleNo)){				
				styleNo = jData.styleNo;
			}
			if(common.isEmpty(jData.productId)){
				alert("상품ID가 없습니다.");
				return;
			}
			if(common.isEmpty(jData.saleproductId)){
				alert("단품ID가 없습니다.");
				return;
			}
			
			$("#_orderForm").remove();
			
			var _orderForm = $('<form>',{
				'name' : '_orderForm',
				'id' : '_orderForm',
				'style' : 'display:none'
			}).append($('<input>',{
				'name' : 'omsOrderproducts[0].dealId',
				'value' : dealId,
				'type' : 'hidden'
			})).append($('<input>',{
				'name' : 'giftYn',
				'value' : giftYn,
				'type' : 'hidden'
			})).append($('<input>',{
				'name' : 'omsOrderproducts[0].offshopId',
				'value' : offshopId,
				'type' : 'hidden'
			})).append($('<input>',{
				'name' : 'omsOrderproducts[0].couponId',
				'value' : couponId,
				'type' : 'hidden'
			})).append($('<input>',{
				'name' : 'omsOrderproducts[0].productId',
				'value' : productId,
				'type' : 'hidden'
			})).append($('<input>',{
				'name' : 'omsOrderproducts[0].saleproductId',
				'value' : saleproductId,
				'type' : 'hidden'
			})).append($('<input>',{
				'name' : 'omsOrderproducts[0].orderQty',
				'value' : orderQty,
				'type' : 'hidden'
			})).append($('<input>',{
				'name' : 'omsOrderproducts[0].styleNo',
				'value' : styleNo,
				'type' : 'hidden'
			}))
			
			if(common.isNotEmpty(subProducts)){
				for(var i=0;i<subProducts.length;i++){
					var subProductId = subProducts[i].productId;
					var subSaleproductId = subProducts[i].saleproductId;
					
					_orderForm.append($('<input>',{
						'name' : 'omsOrderproducts[0].omsOrderproducts['+i+'].productId',
						'value' : subProductId
					})).append($('<input>',{
						'name' : 'omsOrderproducts[0].omsOrderproducts['+i+'].saleproductId',
						'value' : subSaleproductId
					})).append($('<input>',{
						'name' : 'omsOrderproducts[0].omsOrderproducts['+i+'].orderQty',
						'value' : orderQty
					}))									
				}
			}
			
//			_orderForm.attr("action","/oms/order/sheet");
			_orderForm.attr("method","post");
			_orderForm.appendTo('body');
			
			
			oms.orderLogin();
						
			
//			console.log(_orderForm);
			
			//페이지일때
//			_orderForm.attr("action","/oms/order/sheet");
//			_orderForm.attr("method","post");
//			_orderForm.submit();			
		},
		directOrderList : function(omsOrderList){
			/**
			 * 아래 object list
			 * {productId : '', saleproductId : '', omsOrderproducts :
			 * 											[
			 * 												{productId:'',saleproductId:''},
			 * 												{productId:'',saleproductId:''}
			 * 											]
			 *  dealId : '', orderQty : '', giftYn : '',couponId:'' 
			 * }
			 * 
			 * 
			 * productId,	(필수)
			 * saleproductId,	(필수)
			 * omsOrderproducts,	(SET일때 필수) 
			 * dealId,
			 * ,
			 * orderQty,	(default 1)
			 * giftYn
			 * offshopId : 추천매장
			 * couponId : 최적가 적용 쿠폰id
			 * styleNo : 스타일번호
			 */
			$("#_orderForm").remove();
			
			var _orderForm = $('<form>',{
				'name' : '_orderForm',
				'id' : '_orderForm',
				'style' : 'display:none'
			});
			
			for(var k=0;k<omsOrderList.length;k++){
				
				jData = omsOrderList[k];
				
				var dealId = null;				
				var giftYn = null;
				var offshopId = null;
				var couponId = null;
				var productId = jData.productId;
				var saleproductId = jData.saleproductId;
				var orderQty = "1";
				var subProducts = jData.omsOrderproducts;			
				var styleNo = null;
				
				if(common.isNotEmpty(jData.cartTypeCd)){
					cartTypeCd = jData.cartTypeCd;
				}
				if(common.isNotEmpty(jData.dealId)){
					dealId = jData.dealId;
				}				
				if(common.isNotEmpty(jData.giftYn)){
					giftYn = jData.giftYn;
				}
				if(common.isNotEmpty(jData.offshopId)){
					offshopId = jData.offshopId;
				}
				if(common.isNotEmpty(jData.orderQty)){
					orderQty = jData.orderQty;
				}
				if(common.isNotEmpty(jData.couponId)){				
					couponId = jData.couponId;
				}
				if(common.isNotEmpty(jData.styleNo)){				
					styleNo = jData.styleNo;
				}
				if(common.isEmpty(jData.productId)){
					alert("상품ID가 없습니다.");
					return;
				}
				if(common.isEmpty(jData.saleproductId)){
					alert("단품ID가 없습니다.");
					return;
				}
				if(k == 0){
					
					_orderForm.append($('<input>',{
						'name' : 'giftYn',
						'value' : giftYn,
						'type' : 'hidden'
					}))
				}
				
				_orderForm.append($('<input>',{
								'name' : 'omsOrderproducts['+k+'].dealId',
								'value' : dealId,
								'type' : 'hidden'
							})).append($('<input>',{
								'name' : 'omsOrderproducts['+k+'].couponId',
								'value' : couponId,
								'type' : 'hidden'
							})).append($('<input>',{
								'name' : 'omsOrderproducts['+k+'].productId',
								'value' : productId,
								'type' : 'hidden'
							})).append($('<input>',{
								'name' : 'omsOrderproducts['+k+'].saleproductId',
								'value' : saleproductId,
								'type' : 'hidden'
							})).append($('<input>',{
								'name' : 'omsOrderproducts['+k+'].orderQty',
								'value' : orderQty,
								'type' : 'hidden'
							})).append($('<input>',{
								'name' : 'omsOrderproducts['+k+'].offshopId',
								'value' : offshopId,
								'type' : 'hidden'
							})).append($('<input>',{
								'name' : 'omsOrderproducts['+k+'].styleNo',
								'value' : styleNo,
								'type' : 'hidden'
							}))
//				alert(couponId);			
				if(common.isNotEmpty(subProducts)){
					for(var i=0;i<subProducts.length;i++){
						var subProductId = subProducts[i].productId;
						var subSaleproductId = subProducts[i].saleproductId;
						
						_orderForm.append($('<input>',{
							'name' : 'omsOrderproducts['+k+'].omsOrderproducts['+i+'].productId',
							'value' : subProductId
						})).append($('<input>',{
							'name' : 'omsOrderproducts['+k+'].omsOrderproducts['+i+'].saleproductId',
							'value' : subSaleproductId
						})).append($('<input>',{
							'name' : 'omsOrderproducts['+k+'].omsOrderproducts['+i+'].orderQty',
							'value' : orderQty
						}))									
					}
				}
			}
			
			
//			_orderForm.attr("action","/oms/order/sheet");
			_orderForm.attr("method","post");
			_orderForm.appendTo('body');
//			oms.submitOrder();
			oms.orderLogin();
			
//			console.log(_orderForm);
			
			//페이지일때
//			_orderForm.attr("action","/oms/order/sheet");
//			_orderForm.attr("method","post");
//			_orderForm.submit();			
		},
		//현금영수증.
		printCashReceipt : function(orderId,tid){
			var data = {orderId:orderId,lgd_TID:tid};
			$.ajax({
		 		contentType : "application/json; charset=UTF-8",
		 		url : "/api/oms/pg/receiptparam",
				type : "POST",		
				data : JSON.stringify(data)
			}).done(function(response){
				var pg = response;
//				console.log(pg);
//				console.log(pg.lgd_MID,orderId,pg.lgd_CASSEQNO,pg.ttype,pg.cst_PLATFORM);
//				showCashReceipts(pg.lgd_MID,pg.lgd_TID,pg.lgd_CASSEQNO,pg.ttype,pg.cst_PLATFORM);
				showCashReceiptsByTidWithHashdata(pg.lgd_MID,pg.lgd_TID,pg.lgd_CASSEQNO,pg.ttype,pg.cst_PLATFORM,pg.lgd_HASHDATA)
			});						
		},
		//영수증.
		printReceipt : function(orderId,tid){
			var data = {orderId:orderId,lgd_TID:tid};
			$.ajax({
		 		contentType : "application/json; charset=UTF-8",
		 		url : "/api/oms/pg/receiptparam",
				type : "POST",		
				data : JSON.stringify(data)
			}).done(function(response){
				var pg = response;
//				console.log(pg);
				showReceiptByTID(pg.lgd_MID,pg.lgd_TID,pg.lgd_HASHDATA);																
			});			
		},
		//kakao 카드전표.
		printReceiptKakao : function(tid){
			var status =  "toolbar=no,location=no,directories=no,status=yes,menubar=no,scrollbars=yes,resizable=yes,width=420,height=540"; var url = "https://mms.cnspay.co.kr/trans/retrieveIssueLoader.do?TID="+tid+"&type=0"; window.open(url,"popupIssue",status);
		},
		//개인통관부호link
		openPrivateUnipass : function(){
			window.open('https://unipass.customs.go.kr/csp/persIndex.do', '_blank');			
		},
		// 옵션 선택 레이어
		changeOptionLayer : {
			callback : function(data) {
				var form = "#cartForm"+formId;
				var qty = $(form).find("#qty").val();
				if(!$.isNumeric(qty)){
					alert("수량은 숫자만 입력가능합니다.");
					return;
				}
				chgSaleproduct(formId, data.productId, data.saleproductId, data.name);
				oms.changeOptionLayer.close()
			},
			callback_param : {},
			formId : '',
			/**
			 * 레이어 열기
			 * 
			 * ex) oms.chageOption.open({productId : '153', saleproductId : '271'});
			 */
			open : function(cartProductNo, productId, saleproductId, qty, pickupProduct, offshopId) {
				formId = cartProductNo;
				var param = {
					productId : productId,
					saleproductId : saleproductId,
					qty : qty,
					pickupProduct : pickupProduct,
					offshopId : offshopId
				};
				$.get("/pms/product/option/change/ajax", param).done(function(html) {
					var layerId = '#optionChangeLayer_' + cartProductNo;
					
					if(global.channel.isMobile == "true"){						
						var btnid = "#optionBtn"+cartProductNo;
						var stateBoxid = "#stateBox"+cartProductNo;
						var flag = $(btnid).hasClass("on");

						if(typeof flag == 'undefined'|| !flag){
							$(layerId).show();
							$(layerId).html(html);
							var optionH = $(layerId).outerHeight() + 10;
							$(stateBoxid).attr("style","padding-bottom :"+optionH+"px;");
							$(btnid).addClass("on");
						} else {
							oms.changeOptionLayer.close();							
						}										
					}else{
						$(layerId).show();
						$(layerId).html(html);
					}
					
				});
			},
			// 팝업 닫기
			close : function(){
				$('[id^=optionChangeLayer]').hide();
				$('[id^=optionChangeLayer]').html('');
				
				if(global.channel.isMobile == "true"){						
					var btnid = "#optionBtn"+formId;
					var stateBoxid = "#stateBox"+formId;
					$(stateBoxid).attr("style","");
					$(btnid).removeClass("on");
				}
			},
		    // '변경' 버튼 클릭
		    change:function(productId, commboName) {
				var form = '';
				if (typeof formId != 'undefined') {
					form = "#cartForm" + formId;
				}
		
				// 단품정보 조회를 위한 파라메터 세팅
				var param = pms.optioncombo.makeSelectedParam(commboName);
				// console.log(param)
				// 옵션 미선택
				if (!param) {
					alert("옵션을 선택해 주세요.");
					return;
				}
		
				param.productId = productId;
		
				// 단품정보 조회(ajax)
				pms.optioncombo.getSaleproductInfo(param, function(data) {
					if (typeof formId != 'undefined') {
						$(form).find("#qty").val($(form).find("#tempQty").val());
					}
					oms.changeOptionLayer.callback(data);
				});
			},
		    // 옵션 선택 : 콤보박스 선택
// selectOption : function(obj, next, productId){
//		    	// 파라메터 생성
//		    	var param = pms.common.selectOption($(obj).attr('name'), 'optionvalue', next, productId, '');
//				
//				if(!common.isEmptyObject(param) && !common.isEmpty(next)){// 하위 콤보 조회
//					// callback 
//					pms.common.getNextOptions(next, param, '');
//				}
//				
//			},
		    selectOption : function(selectedName, optionSize, productId ){
		    	
		    	var p = {
		    			selectedName : selectedName, 
		    			optionSize : optionSize,
		    			productId : productId 
		    	};
		    	
				pms.optioncombo.selectOption(p);
			},	
			maxStockCheck : function(form){
				var maxStockQty = $(form).find("#maxStockQty").val();
				var value = $(form).find("#tempQty").val();
				if(Number(value) > Number(maxStockQty)){
					$(form).find("#tempQty").val(maxStockQty);
				}				
			},
			//수량 -
			minusQty : function(){
				var form ="#cartForm"+formId;
				var qty = $(form).find("#tempQty");

				var minQty = $(form).find("#minQty");
				
				if(qty.val() == 1){
					alert("수량은 1이상입니다.");
					return;
				}else if(qty.val() == minQty){
					alert("주문 최소수량은 "+minQty+"이상입니다.");
					return;
				}

				ccs.common.quantityMinus($(form).find("#tempQty"));

			},
			//수량 +
			plusQty : function(){				
				var form = "#cartForm"+formId;
				var qty = $(form).find("#tempQty");
				
				var cartTypeCd = $("#selCartTypeCd").val();
				ccs.common.quantityPlus($(form).find("#tempQty"));
				
				oms.changeOptionLayer.maxStockCheck(form);
			},
			//수량 수정
			chgQty : function(value){
				var form = "#cartForm"+formId;
				var qty = $(form).find("#tempQty");	
				var minQty = $(form).find("#minQty");

				if(common.isEmpty(value)){
					alert("수량은 숫자로 입력하셔야합니다.");
					return;
				}
				if(!$.isNumeric(value)){
					qty.val(qty.val());
					return;
				}
				if(qty.val() <= 0){
					alert("수량은 1이상입니다.");
					qty.val("1");
					return;
				}else if(qty.val() < minQty.val()){
					alert("주문 최소수량은 "+minQty.val()+"이상입니다.");
					qty.val(minQty.val());
					return;
				}
				
				oms.changeOptionLayer.maxStockCheck(form);
				
				var cartTypeCd = $("#selCartTypeCd").val();
			}
		},
		//비회원 비밀번호 check
		checkPassword : function(upw){

			    if(!/^[a-zA-Z0-9]{4,8}$/.test(upw))

			    { 
			        alert('비밀번호는 숫자와 영문자 조합으로 4~8자리를 사용해야 합니다.'); 
			        return false;
			    }

			  
			    var chk_num = upw.search(/[0-9]/g); 
			    var chk_eng = upw.search(/[a-z]/ig);

			    if(chk_num < 0 || chk_eng < 0)

			    { 
			        alert('비밀번호는 숫자와 영문자를 혼용하여야 합니다.'); 
			        return false;
			    }
			    
			    if(/(\w)\1\1\1/.test(upw))

			    {
			        alert('비밀번호에 같은 문자를 4번 이상 사용하실 수 없습니다.'); 
			        return false;
			    }

//			    if(upw.search(uid)>-1)
//
//			    {
//			        alert('ID가 포함된 비밀번호는 사용하실 수 없습니다.'); 
//			        return false;
//			    }

			    return true;
			
		},
		//개인통관고유부호 check
		checkPersonalCustomsCode : function(pc){
				if('P' != pc.substr(0,1)){
					alert("개인통관고유부호는 대문자P로 시작해야합니다.");
					return false;
				}
				
				if(!/^[0-9]{11}$/.test(pc.substr(1))){
					alert("개인통관고유부호는 대문자P로 시작하는 숫자 12자리를 입력해야 합니다.");
					return false;
				}
				return true;
		},
		showOrderLoading : function(flag){
			if(flag){
				common.showLoadingBar();
				$("#payBtn").hide();
			}else{
				common.hideLoadingBar();
				$("#payBtn").show();
			}
		},
		validatePhoneNum : function(phonenumber) {
			var regExp = /^01([0|1|6|7|8|9]?)-?([0-9]{3,4})-?([0-9]{4})$/;
			if ( !regExp.test( phonenumber ) ) {
			    return false
			}else{
				return true;
			}
		},
		validateCardNum : function(cardnumber) {
			   
			
			if(typeof cardnumber == 'undefined' || common.isEmpty(cardnumber)){
				return false;
			}else{
				//빈칸과 대시 제거
		        cardnumber = cardnumber.replace(/[ -]/g,'');
		 
		        //카드 번호가 유효한지 검사
		        //정규식이 캡처 그룹들 중 하나에 들어있는 숫자를 캡처
		        var match = /^(?:(94[0-9]{14})|(4[0-9]{12}(?:[0-9]{3})?)|(5[1-5][0-9]{14})|(6(?:011|5[0-9]{2})[0-9]{12})|(3[47][0-9]{13})|(3(?:0[0-5]|[68][0-9])[0-9]{11})|((?:2131|1800|35[0-9]{3})[0-9]{11}))$/.exec(cardnumber);
		       
		        if(match) {
		        	return true;
		            //정규식 캡처 그룹과 같은 순서로 카드 종류 나열
//		            var types = ['BC', 'Visa', 'MasterCard', 'Discover', 'American Express', 'Diners Club', 'JCB'];
		 
		            //일치되는 캡처 그룹 검색
		            //일치부 배열의 0번째 요소 (전체 일치부중 첫 일치부)를 건너뜀
//		            for(var i = 1; i < match.length; i++) {
//		                if(match[i]) {
//		                    //해당 그룹에 대한 카드 종류를 표시
//		                    document.getElementById('notice').innerHTML = types[i-1];
//		                    break;
//		                }
//		            }
		 
		        } else {
		            return false;
		        }
			} 			
	        
	    },
		validateBizNum: function (bizID) {
			if(bizID.length == 11){
				return true;
			}else{
				return false;
			}
//			var checkID = new Array(1, 3, 7, 1, 3, 7, 1, 3, 5, 1);
//			var i, Sum = 0, c2, remander;
//
//			bizID = bizID.replace(/-/gi, '');
//
//			for (i = 0; i <= 7; i++) {
//				Sum += checkID[i] * bizID.charAt(i);
//			}
//
//			c2 = "0" + (checkID[8] * bizID.charAt(8));
//			c2 = c2.substring(c2.length - 2, c2.length);
//
//			Sum += Math.floor(c2.charAt(0)) + Math.floor(c2.charAt(1));
//
//			remander = (10 - (Sum % 10)) % 10;
//
//			if (bizID.length != 10) {
//				return false;
//			} else if (Math.floor(bizID.charAt(9)) != remander) {
//				return false;
//			} else {
//				return true;
//			}

		},
		checkTen : function(num){ 
			var curAmtChk = (Number(num) / 10);
			if(curAmtChk == parseInt(curAmtChk)){
				return true;
			}else{
				return false;
			}			
		},
		//무이자정보 조회
		interestList : function(cardCode,paymentAmt,callback){			
			var data = {cardCode : cardCode, paymentAmt : paymentAmt};
			$.ajax({
		 		contentType : "application/json; charset=UTF-8",
		 		url : oms.url.interestList,
				type : "POST",		
				data : JSON.stringify(data)
			}).done(function(response){
				callback(response);							
			});		
		}
		
}