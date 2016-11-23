$(document).ready(function(){
	
	// 장바구니 품절상품 펼치기/닫기(모바일 전용)
	$(".cart_box .cart_disable_head1 .soldout a").off("click").on({
		"click" : function(e) {
			$(this).toggleClass("on");
			$(this).parent().next().toggle();
			$(this).closest(".cart_disable_head1").next().toggle();

			//e.preventDefault();
		}
	});

//	// 빈 테이블인지 체크
//	function fnEmptyTb(target_tag) {
//		if( $("li", target_tag).length == 0 ){
//			$(target_tag).prev(".cart_tb_thead1").remove();
//			$(target_tag).next(".cart_medium").remove();
//			$(target_tag).remove();
//		}
//
//		fnEmptyCart();
//	}

	// 전체 장바구니가 비었는지 체크
	function fnEmptyCart() {
		if( $(".tabcontShow .cart_tb_tbody1").length == 0 ){console.log('a');
			$(".tabcontShow .cart_total, .tabcontShow .cart_order").remove();

			if( $(".wrap").hasClass("pc") ){
				$(".tabcontShow .choice_del:eq(0)").after(emptyCart);
			}else if( $(".wrap").hasClass("mobile") ){
				$(".tabcontShow .choice_del:eq(0)").after(emptyCartMo);
			}
		}
	}

	// 장바구니 계속보관
	$(".cart_tb_tbody1 .btn_save").off("click").on({
		"click" : function() {
			if( $(this).hasClass("save_on") ){
				$(this).text("계속보관");
			}else{
				$(this).text("보관취소");
			}
			$(this).toggleClass("save_on");
		}
	});

	// 장바구니 옵션변경 버튼
	$(".cart_tb_tbody1 .btn_option").off("click").on({
		"click" : function(e) {
			$(".cart_box .option_box").hide();
			$(this).closest("em").next().toggle();

			//e.preventDefault();

			/*
			if( $(".wrap").hasClass("mobile") ){
				if( $(this).text() == "옵션변경" ){
					$(this).text("변경");
				}else{
					$(this).text("옵션변경");
				}
			}
			*/
		}
	});
	// 장바구니 사은품 보기 버튼
	$(".cart_tb_tbody1 .btn_gift_view, .orderBox .btn_gift_view, .od_repeatBox .btn_gift_view").off("click").on({
		"click" : function(e) {
			//$(".cart_box .option_box").hide();
			//$(".cart_tb_tbody1 .btn_gift_view").parent().removeClass("on");
			$(this).closest("u").toggleClass("on").next().toggle();

			//e.preventDefault();
		}
	});

	// 장바구니 매장 위치 레이어(pc용)
	$(".pc .btn_shop_map").off("click").on({
		"click" : function(e) {
			$(".pc .layer_shop").show().find(".layer_inner").css({"marginTop" : ( ( $(window).height() - $(".pc .layer_shop .layer_inner").innerHeight() ) / 2) + $(window).scrollTop() + "px"});

			//e.preventDefault();
		}
	});
	
	// 장바구니 작은 레이어 닫기
	$(".option_box .btn_close").off("click").on({
		"click" : function(e) {
			if( $(this).closest(".option_box").hasClass("gift_box") ){
				$(this).closest(".option_box").prev().removeClass("on");
			}

			$(this).closest(".option_box").hide();


			//e.preventDefault();
		}
	});
	

	/* 장바구니 - 빈 장바구니 체크 : 2016.08.25 */
	function fnEmptyCart( target_tag ) {
		var strEmpty = '\
						<div class="div_tb_thead3">\
							<div class="tr_box">\
								<span class="col1">\
									<label class="chk_style1">\
										<em>\
											<input type="checkbox" value="" class="chk_all" />\
										</em>\
										<span>전체선택</span>\
									</label>\
								</span>\
								<span class="col2">상품/옵션정보</span>\
								<span class="col3">수량</span>\
								<span class="col4">상품금액</span>\
								<span class="col5">상품금액</span>\
								<span class="col6">선택</span>\
							</div>\
						</div>\
						<ul class="div_tb_tbody3">\
							<li class="empty">\
								<div class="tr_box">\
									<div class="td_box col99">\
										장바구니에 담긴 상품이 없습니다.<br />\
										<a href="#none" class="btn_sStyle1">계속쇼핑하기</a>\
									</div>\
								</div>\
							</li>\
						</ul>';

		if( $(" li", target_tag).length == 0 ){
			$(target_tag).prev(".div_tb_thead3").remove()
				.end().next(".total_medium").remove()
				.end().remove();
		}

		if( $(".content .tab_conOn ul .tr_box").length - $(".tab_conOn .soldOut ul .tr_box").length == 0 ){
			if( $(" li", target_tag).length == 0){
				$(".tab_conOn .choice_del:eq(0)").after( strEmpty );
			}
		}

		fnSoldoutList();
	}

	/* 장바구니 - 품절상품 체크 : 2016.08.25 */
	function fnSoldoutList(){
		if( $(".tab_conOn .soldOut ul li").length == 0 ){
			$(".tab_conOn .soldOut").remove();
		}
	}

	/* 장바구니 - 전체상품 선택 : 2016.08.25 */
//	$(".tr_box .chk_all").on("change", function(e){
//		var target_tag = $(this).closest(".tr_box").parent().next();
//
//		if( $(this).prop("checked") ){
//			$(" .chk_style1 input", target_tag).each(function() {
//				$(this).prop("checked", true).parent().addClass("selected");
//			});
//		}else{
//			$(" .chk_style1 input", target_tag).each(function() {
//				$(this).prop("checked", false).parent().removeClass("selected");
//			});
//		}
//	});

	/* 장바구니 - 선택한 상품 삭제 기능 : 2016.08.25 */
//	function fnRemoveAct( target_this ) {
//		var target_tag = $(target_this).closest("ul");
//
//		if( $(target_this).closest("li").find(".tr_box").length > 1){
//			$(target_this).closest(".tr_box").remove();
//		}else{
//			$(target_this).closest("li").remove();
//			fnEmptyCart( $(target_tag) );
//		}
//	}

	/* 장바구니 - 선택상품 삭제 : 2016.08.25 */
//	$(".btn_choice_del").on("click", function(e){
//		$(".content .tab_conOn .tr_box .chk_style1 input:checked").each(function() {
//			fnRemoveAct( $(this) );
//		});
//
//		$(".content .tab_conOn .tr_box .chk_all:checked").prop("checked", false).parent().removeClass("selected");
//		$(".mobile .content .tab_conOn .chk_all:checked").prop("checked", false).parent().removeClass("selected");
//	});

	/* 장바구니 - 삭제버튼 : 2016.08.25 */
	$(".tr_box .btn_del").on("click", function(e){
		fnRemoveAct( $(this) );
	});

	/* 장바구니 - 품절상품 전체 삭제 : 2016.08.25 */
	$(".soldOut .btn_disable_allDel").on("click", function(e){
		$(".tab_conOn .soldOut").remove();
	});

	
	/* 주문결제 - 제목 펼치기&접기 : 2016.09.02 */
	function controlBoxM(){
		var _controlBox = $('.control_box'),
			_controlBtn = _controlBox.find('.btn_box_control');
			_controlBtn.on("click", function(e) {
				//e.preventDefault();
				var _this = $(this);
				if(!_this.parents('.control_box').hasClass('active')){
					_this.parents('.control_box').addClass('active').next().css('display', 'none');
				}else{
					_this.parents('.control_box').removeClass('active').next().css('display', '');
				}

			});
	}
	controlBoxM();
	
	
	/* 장바구니 - 계속보관 버튼 : 2016.08.26 */
	if( $(".tr_box .btn_save input").length ){
		$(".tr_box .btn_save input").each(function() {
			if( $(this).prop("checked") ){
				$(this).prev().text("보관취소").parent().addClass("sWhite1");
			}else{
				$(this).prev().text("계속보관").parent().removeClass("sWhite1");
			}
		});
	}
	$(".tr_box .btn_save input").on("change", function(e){
		if( $(this).prop("checked") ){
			$(this).prev().text("보관취소").parent().addClass("sWhite1");
		}else{
			$(this).prev().text("계속보관").parent().removeClass("sWhite1");
		}
	});
	
	
	// 장바구니 [선택 삭제하기]
	$(".cart_fixed .btn_choice_del").off("click").on({
		"click" : function() {
			$(".tabcontShow .cart_tb_tbody1 .inp_chk").each(function() {
				if( $(this).prop("checked") ){
					$(this).closest("li").remove();
				}
			});
		}
	});
	
	
	// 장바구니 매장 위치 팝업
	$(".mobile .cart_box .btn_shop_map").off("click").on({
		"click" : function() {
			fnLayerOpen( $(".layer_shop") );
		}
	});
	
	

	// 주문결제 (탭 :: 기본배송지, 배송지주소록, 새로운배송지)
	$(".mobile .orderBox .tab a").off("click").on({
		"click" : function(e) {
			var target_tag = $(this).closest(".tab");
			var num = $("a", target_tag).index( $(this) );

			$("li", target_tag).removeClass("on").eq(num).addClass("on");
			$(target_tag).siblings(".tab_con").removeClass("tab_on").eq(num).addClass("tab_on");

			//e.preventDefault();
		}
	});

	// 주문결제 (결제 수단)
	$(".mobile .orderBox .payment a").off("click").on({
		"click" : function(e) {
			var num = $(".mobile .orderBox .payment a").index( $(this) );

			$(".mobile .orderBox .payment li").removeClass("on").eq(num).addClass("on");
			$(".mobile .orderBox .pay_con").removeClass("pay_on").eq(num).addClass("pay_on");

			//e.preventDefault();
		}
	});

	// 주문결제 (비회원 본인인증 내용 보기)
	$(".mobile .orderBox .btn_view_agree").off("click").on({
		"click" : function(e) {
			$(this).toggleClass("view_on");

			$(this).parent().next().toggle();

			//e.preventDefault();
		}
	});

	// 주문결제 (배송지 선택 탭)
	$(".mobile .layer_order .tab_type2 a").off("click").on({
		"click" : function(e) {
			var target_tag = $(this).closest(".tab_type2");
			var num = $("a", target_tag).index( $(this) );

			$("li", target_tag).removeClass("on").eq(num).addClass("on");
			$(target_tag).siblings(".tab_con").removeClass("tab_on").eq(num).addClass("tab_on");

			//e.preventDefault();
		}
	});

	// 주문결제 (전체 사은품 선택하지 않기)
	$(".mobile .layer_order .btn_gift_allnot").off("change").on({
		"change" : function(e) {
			if( $(this).prop("checked") ){
				$(".mobile .layer_order .btn_gift_not").prop("checked", true);
			}else{
				$(".mobile .layer_order .btn_gift_not").prop("checked", false);
			}
		}
	});

	// 주문결제 팝업 (안심번호 안내)
	$(".mobile .save_num .btn_explan").off("click").on({
		"click" : function(e) {
			var target_tag = $(".mobile .layer_save_num");
			var target_marginTop = ($(window).height() - $(target_tag).innerHeight()) / 2;
			$(target_tag).show().css({"top" : $(window).scrollTop(), "marginTop" : target_marginTop });
		}
	});
	// 주문결제 팝업 (카드 청구 할인 안내)
	$(".mobile .pay_how .btn_cardPay").off("click").on({
		"click" : function(e) {
			var target_tag = $(".mobile .layer_cardPay");
			var target_marginTop = ($(window).height() - $(target_tag).innerHeight()) / 2;
			$(target_tag).show().css({"top" : $(window).scrollTop(), "marginTop" : target_marginTop });
		}
	});
	// 주문결제 팝업 (카드사별 혜택 안내)
	$(".mobile .pay_how .btn_cardFavor").off("click").on({
		"click" : function(e) {
			var target_tag = $(".mobile .layer_inst");
			var target_marginTop = ($(window).height() - $(target_tag).innerHeight()) / 2;
			$(target_tag).show().css({"top" : $(window).scrollTop(), "marginTop" : target_marginTop });
		}
	});

	/* 장바구니 - 매장위치 팝업 : 2016.08.28 */
	$(".mobile .btn_shopPosition").off("click").on({
		"click" : function(e) {
			fnLayerOpen( $(".mobile .layer_shop") );

			//e.preventDefault();
		}
	});
	
	/* 장바구니 - 선물포장 안내 버튼(작은 레이어) : 2016.08.22 */
	$(".mobile .cart_box .btn_giftInfo").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .sLayer_gift").not(".pop_wrap, .pop_wrap_sm") ); //16.11.01
		}
	});
	
	/* 안심번호 버튼(작은 레이어) : 2016.08.22 */
	$(".mobile .btn_save_num").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .sLayer_save_num") );
		}
	});

	$(".mobile .orderBox .btn_giftInfo").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .sLayer_gift").not(".pop_wrap") );
		}
	});
	

	/* 선물포장 안내 버튼(작은 레이어) : 2016.08.22 */
	$(".mobile .btn_gift_packing").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .sLayer_gift_packing") );
		}
	});
	
	/* 장바구니 - 전체선택하기 : 2016.08.26 */
	$(".mobile .chk_all").on("click", function(e){
		if( $(this).prop("checked") ){
			$(".mobile .tab_conOn .chk_style1:not('.this_disabled') input").each(function() {
				$(this).prop("checked", true).parent().addClass("selected");
			});
		}else{
			$(".mobile .tab_conOn .chk_style1:not('.this_disabled') input").each(function() {
				$(this).prop("checked", false).parent().removeClass("selected");
			});
		}
	});
	
	
	//input 삭제 예정
	$(".inp_placeholder").find("input[type='text'], input[type='password']").each(function(){
		$(this).focus(function(){
			$(this).addClass("focus");
			$(this).next("label").hide();
		});

		$(this).blur(function(){
			$(this).removeClass("focus");
			if($(this).val() == ""){
				$(this).next("label").show();
			}
		});
	});
	
	
	$(".pc .btn_gift_packing").on("click", function(e){
		fnPopPosition( $(".sLayer_gift_packing").not(".layer_style1") );

		//e.preventDefault();
	});
	
});
