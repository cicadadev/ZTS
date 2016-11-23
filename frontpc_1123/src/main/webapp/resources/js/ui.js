// 유저 기기 확인
var Script = document.createElement('script');
Script.type = "text/javascript";

function userDevice() {
	if(navigator.userAgent.match(/Android|Mobile|iP(hone|od|ad)|BlackBerry|IEMobile|Kindle|NetFront|Silk-Accelerated|(hpw|web)OS|Fennec|Minimo|Opera M(obi|ini)|Blazer|Dolfin|Dolphin|Skyfire|Zune/)){
		$(".wrap").addClass("mobile");
		//$("head").append( $('<link rel="stylesheet" type="text/css" />').attr('href', 'css/mobile.css') );

		Script.src = "js/mo.js";
		document.getElementsByTagName('head')[0].appendChild(Script);
	}else{
		$(".wrap").addClass("pc");

		Script.src = "js/pc.js";
		document.getElementsByTagName('head')[0].appendChild(Script);
	}
}


$(document).ready(function() {
	userDevice();

	// 상품상세 수량 제어
	$(".quantity_box .btn_minus").off("click").on({
		"click" : function() {
			var temp_ea = parseInt($(this).next().val()) - 1;

			if(temp_ea < 1){
				temp_ea = 1;
			}

			$(this).next().val(temp_ea);
		}
	});
	$(".quantity_box .btn_plus").off("click").on({
		"click" : function() {
			var temp_ea = parseInt($(this).prev().val()) + 1;

			if(temp_ea > 100){
				temp_ea = 100;
			}

			$(this).prev().val(temp_ea);
		}
	});

	// 디자인 select
	function fnSelectChange(selectTarget) {
		selectTarget.each(function(){
			var select_name = $(this).children('option:selected').text();
			$(this).siblings('label').text(select_name);
		});
		selectTarget.change(function(){
			var select_name = $(this).children('option:selected').text();
			$(this).siblings('label').text(select_name);
		});
	}
	fnSelectChange( $('.selectbox select') );
	fnSelectChange( $('.sel_box select') );

	/* 장바구니 상품 전체 선택
	$(".cart_tb_thead1 .chk_all").off("change").on({
		"change" : function() {
			if( $(".wrap").hasClass("pc") ){
				//var chk_tag = $(this).closest(".cart_tb_thead1").next().find("li").not(".disable"); //품절 상품 제외
				var chk_tag = $(this).closest(".cart_tb_thead1").next().find("li"); //품절 상품 포함

				if( $(this).prop("checked") ){
					//$(".custom-checkbox", chk_tag).addClass("selected").find("input").prop("checked" , true);
					$(".inp_chk", chk_tag).prop("checked" , true).parent(".custom-checkbox").addClass("selected");
				}else{
					//$(".custom-checkbox", chk_tag).removeClass("selected").find("input").prop("checked" , false);
					$(".inp_chk", chk_tag).prop("checked" , false).parent(".custom-checkbox").removeClass("selected");
				}
			}else if( $(".wrap").hasClass("mobile") ){
				if( $(this).prop("checked") ){
					$(".cart_tb_tbody1 .inp_chk").prop("checked" , true).parent(".custom-checkbox").addClass("selected");
				}else{
					$(".cart_tb_tbody1 .inp_chk").prop("checked" , false).parent(".custom-checkbox").removeClass("selected");
				}
			}
		}
	});

	var emptyCart = '\
							<div class="cart_tb_thead1">\
								<span class="col1">\
									<span class="custom-checkbox">\
										<input type="checkbox" value="" id="chk_all" class="chk_all inp_chk" />\
									</span>\
									<label for="chk_all">전체선택</label>\
								</span>\
								<span class="col2">상품/옵션정보</span>\
								<span class="col3">수량</span>\
								<span class="col4">상품금액</span>\
								<span class="col5">선택</span>\
							</div>\
							<ul class="cart_tb_tbody1">\
								<li class="empty">\
									<div class="tr_box">\
										<div class="td_box col99">\
											장바구니에 담긴 상품이 없습니다.\
										</div>\
									</div>\
								</li>\
							</ul>';

	var emptyCartMo = '\
							<ul class="cart_tb_tbody1">\
								<li class="empty">\
									<div class="tr_box">\
										<div class="td_box col99">\
											장바구니에 담긴 상품이 없습니다.\
										</div>\
									</div>\
								</li>\
							</ul>';

	// 장바구니 상품 전체 삭제(모바일 전용)
	$(".cart_tb_thead1 .btn_all_del").off("click").on({
		"click" : function(e) {
			$(".tabcontShow .cart_tb_thead1, .tabcontShow .cart_tb_tbody1").remove();
			$(".tabcontShow .cart_medium, .tabcontShow .cart_total, .tabcontShow .cart_order").remove();

			fnEmptyCart();
			//e.preventDefault();
		}
	});

	// 장바구니 상품 삭제
	$(".cart_tb_tbody1 .btn_del").off("click").on({
		"click" : function() {
			var target_tag = $(this).closest("ul");

			$(this).closest("li").remove();

			fnEmptyTb(target_tag);
		}
	});

	// 장바구니 품절상품 삭제
	$(".cart_box .btn_disable_del").off("click").on({
		"click" : function(e) {
			var target_tag = $(".tabcontShow .cart_tb_tbody1 .disable").closest("ul");

			$(".tabcontShow .cart_tb_tbody1 .disable").remove();
			fnEmptyTb(target_tag);

			//e.preventDefault();
		}
	});

	// 장바구니 품절상품 전체 삭제
	$(".cart_box .btn_disable_allDel a").off("click").on({
		"click" : function(e) {
			$(".tabcontShow .cart_disable_head1").remove();
			$(".tabcontShow .cart_tb_tbody1 .disable").remove();

			//e.preventDefault();
		}
	});

	// 장바구니 선택상품 삭제
	$(".cart_box .btn_choice_del").off("click").on({
		"click" : function(e) {
			$(".tabcontShow .cart_tb_tbody1 .inp_chk:checked").closest("li").remove();

			$(".tabcontShow .cart_tb_tbody1").each(function() {
				fnEmptyTb( $(this) );
			});

			//e.preventDefault();
		}
	});
	*/

	// 장바구니 품절상품 펼치기/닫기(모바일 전용)
	$(".cart_box .cart_disable_head1 .soldout a").off("click").on({
		"click" : function(e) {
			$(this).toggleClass("on");
			$(this).parent().next().toggle();
			$(this).closest(".cart_disable_head1").next().toggle();

			//e.preventDefault();
		}
	});

	// 빈 테이블인지 체크
	function fnEmptyTb(target_tag) {
		if( $("li", target_tag).length == 0 ){
			$(target_tag).prev(".cart_tb_thead1").remove();
			$(target_tag).next(".cart_medium").remove();
			$(target_tag).remove();
		}

		fnEmptyCart();
	}

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
	$(".pc .layer_type1 .btn_close").off("click").on({
		"click" : function(e) {
			$(this).closest(".layer_type1").hide();
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

	// 탭 - 로그인, 상품 상세, 장바구니 등..
	$(".tab_box").find(".tab li").off("click").on("click", function(e){
		//e.preventDefault();
		var idx = $(this).index();

		$(this).addClass("on").siblings("li").removeClass("on");
		$(this).closest(".tab_box").find("> .tabcont").eq(idx).addClass("tabcontShow").siblings(".tabcont").removeClass("tabcontShow");
	});
	//$(".tab_box").find(".tab li").eq(0).trigger( "click" );

	//마이페이지 - 1:1문의
	$(".qArea").find(">a").off("click").on("click", function(){
		if($(this).hasClass("on")){
			$(this).removeClass("on");
			$(this).closest(".tr_box").next(".aArea").hide();
		} else {
			$(".qArea").find(">a").removeClass("on");
			$(".aArea").hide();
			$(this).addClass("on");
			$(this).closest(".tr_box").next(".aArea").show();
		}
	});

	// 파일첨부
	$(".file_style1 input[type='file']").off("change").on("change", function(){
		var fileName = $(this).val();
		$(this).prev().val(fileName);
	});
	$(".file_style1 .btn_file").off("click").on({
		"click" : function(e) {
			$(this).prev().trigger("click");
			//e.preventDefault();
		}
	});









	/* 셀렉트박스 : 2016.07.26 추가 */
	function fnSelectStyle(){
		$(".select_box1 select").each(function() {
			$(this).siblings('label').text( $("option:selected", this).text() );
		});

		$(".wrap .content").off("change").on("change", ".select_box1 select", function() {
			$(this).siblings('label').text( $("option:selected", this).text() );
		});
	}
	fnSelectStyle();

	/* 텍스트 박스 [placeholder] : 2016.07.26 추가 */
	function fnPlaceholder(){
		$(".inputTxt_place1 input[type='text']").each(function() {
			if($(this).val().length > 0){
				$(this).parent().prev().hide();
			}else{
				$(this).parent().prev().show();
			}
		});

		$(".inputTxt_place1 input[type='text']").off("focus blur").on({
			"focus" : function() {
				$(this).parent().prev().hide();
				$(this).closest(".inputTxt_place1").addClass("place_hover");
			},
			"blur" : function() {
				$(this).closest(".inputTxt_place1").removeClass("place_hover");
				if($(this).val().length > 0){
					$(this).parent().prev().hide();
				}else{
					$(this).parent().prev().show();
				}
			}
		});
	}
	fnPlaceholder();

	/* 파일 첨부 : 2016.07.26 추가 */
	$(".inputFile_style1 input[type='file'], .inputFile_style2 input[type='file']").off("change").on("change", function(){
		var fileName = $(this).val();
		$(this).prev().val(fileName);
	});
	$(".inputFile_style1 .btn_file, .inputFile_style2 .btn_file").off("click").on({
		"click" : function(e) {
			$(this).prev().find("input[type='file']").trigger("click");
			//e.preventDefault();
		}
	});

	/* 탭 공통 : 2016.07.27 추가 */
	$(".tabBox a, .tabBox1 a, .tabBox2 a").off("click").on("click", function(e){
		var idx = $(this).parent().index();
		var parent_ul;

		if( $(this).closest("ul").hasClass("tabBox") ){
			parent_ul = $(this).closest(".tabBox");
		}else if( $(this).closest("ul").hasClass("tabBox1") ){
			parent_ul = $(this).closest(".tabBox1");
		}else{
			parent_ul = $(this).closest(".tabBox2");
		}

		$(this).parent().addClass("on").siblings("li").removeClass("on");
		$(parent_ul).siblings(".tab_con").eq(idx).addClass("tab_conOn").siblings(".tab_con").removeClass("tab_conOn");

		//e.preventDefault();
	});

	/* DIV 테이블 - 옵션 버튼 : 2016.07.27 추가 */
	// $(".btn_tb_option").off("click").on({
	// 	"click" : function(e) {
	// 		//$(this).closest("ul").find(".option_box").not( $(this).parent().next() ).hide();
	// 		$(this).closest("ul").find(".option_box").hide();
	// 		$(".tr_box .btn_tb_option").not( $(this) ).removeClass("on");
	// 		$(this).toggleClass("on").parent().siblings('.option_box').toggle();

	// 		//e.preventDefault();
	// 	}
	// });

	/* DIV 테이블 - 사은품 버튼 : 2016.07.27 추가
	$(".tr_box .btn_tb_gift").off("click").on({
		"click" : function(e) {
			$(this).closest("ul").find(".gift_box").not( $(this).parent().next() ).hide();
			$(this).closest("ul").find(".gift_txt").not( $(this).parent() ).removeClass("on");
			$(this).parent().toggleClass("on").next().toggle();

			//e.preventDefault();
		}
	});
	*/
	$(".ly_box .btn_close").off("click").on({
		"click" : function(e) {
			if( $(this).closest(".ly_box ").hasClass("gift_box") ){
				$(this).closest(".ly_box ").prev().removeClass("on");
			}

			$(this).closest(".ly_box ").hide();

			//e.preventDefault();
		}
	});

	/*수량 제어 : 2016.07.27 추가 */
	$(".quantity .btn_minus").off("click").on({
		"click" : function() {
			var temp_ea = parseInt($(this).next().val()) - 1;

			if(temp_ea < 1){
				temp_ea = 1;
			}

			$(this).next().val(temp_ea);
		}
	});
	$(".quantity .btn_plus").off("click").on({
		"click" : function() {
			var temp_ea = parseInt($(this).prev().val()) + 1;

			if(temp_ea > 100){
				temp_ea = 100;
			}

			$(this).prev().val(temp_ea);
		}
	});

	/* Textarea : 2016.08.10 추가 */
	function fnTextarea(){
		$(".txtarea_box textarea").each(function() {
			if($(this).val().length > 0){
				$(this).next().hide();
			}else{
				$(this).next().show();
			}
		});

		$(".txtarea_box textarea").off("focus blur").on({
			"focus" : function() {
				$(this).closest(".txtarea_box").addClass("txtarea_hover");
			},
			"blur" : function() {
				$(this).closest(".txtarea_box").removeClass("txtarea_hover");
				if($(this).val().length > 0){
					$(this).next().hide();
				}else{
					$(this).next().show();
				}
			}
		});
	}
	fnTextarea();

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
	$(".tr_box .chk_all").on("change", function(e){
		var target_tag = $(this).closest(".tr_box").parent().next();

		if( $(this).prop("checked") ){
			$(" .chk_style1 input", target_tag).each(function() {
				$(this).prop("checked", true).parent().addClass("selected");
			});
		}else{
			$(" .chk_style1 input", target_tag).each(function() {
				$(this).prop("checked", false).parent().removeClass("selected");
			});
		}
	});

	/* 장바구니 - 선택한 상품 삭제 기능 : 2016.08.25 */
	function fnRemoveAct( target_this ) {
		var target_tag = $(target_this).closest("ul");

		if( $(target_this).closest("li").find(".tr_box").length > 1){
			$(target_this).closest(".tr_box").remove();
		}else{
			$(target_this).closest("li").remove();
			fnEmptyCart( $(target_tag) );
		}
	}

	/* 장바구니 - 선택상품 삭제 : 2016.08.25 */
	$(".btn_choice_del").on("click", function(e){
		$(".content .tab_conOn .tr_box .chk_style1 input:checked").each(function() {
			fnRemoveAct( $(this) );
		});

		$(".content .tab_conOn .tr_box .chk_all:checked").prop("checked", false).parent().removeClass("selected");
		$(".mobile .content .tab_conOn .chk_all:checked").prop("checked", false).parent().removeClass("selected");
	});

	/* 장바구니 - 삭제버튼 : 2016.08.25 */
	$(".tr_box .btn_del").on("click", function(e){
		fnRemoveAct( $(this) );
	});

	/* 장바구니 - 품절상품 전체 삭제 : 2016.08.25 */
	$(".soldOut .btn_disable_allDel").on("click", function(e){
		$(".tab_conOn .soldOut").remove();
	});

	$(".selectGiftList input").on("change", function(e){
		if( $(this).prop("checked") ){
			$(this).parents('li').addClass("on");
		}else{
			$(this).parents('li').removeClass("on");
		}
	});
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

	/* 고객센터 - 셀렉트박스 브랜드 옵션 선택 : 2016.08.30 */
	/* 고객센터 - 셀렉트박스 브랜드 옵션 선택, 동적 브랜드설정을 위해 화면에서 처리: 2016.09.02 - ROY */
	/*var strAddBrand = '\
													<div class="select_box1 brand_add">\
														<label>브랜드명0</label>\
														<select>\
															<option selected="selected">브랜드명0</option>\
															<option >브랜드명1</option>\
															<option >브랜드명2</option>\
															<option >브랜드명3</option>\
															<option >브랜드명4</option>\
															<option >브랜드명5</option>\
															<option >브랜드명6</option>\
															<option >브랜드명7</option>\
															<option >브랜드명8</option>\
														</select>\
													</div>';
	$(".wrap .brand_add").on("change", function(e) {
		if( $("option:selected", this).text() == "브랜드" ){
			$(this).closest(".brand_add").after( strAddBrand );
		}else if( $("option:selected", this).text().match(/상품|예치금\/매일포인트\/쿠폰|이벤트\/혜택|회원정보|기타/) ){
			$(this).closest("li").next().hide();
		}else{
			$(this).closest(".brand_add").next().remove();
			$(this).closest("li").next().show();
		}
	});*/

	/* 고객센터 - FAQ카테고리박스 : 2016.08.30 */
	$(".faqCateBox a").off("click").on("click", function(e) {
		$(this).parent().addClass("on").siblings("li").removeClass("on");

		//e.preventDefault();
	});

	/* 정렬탭 클릭 : 2016.09.02 */
	$(".sortBox a, .sortBox1 a").off("click").on("click", function(e) {
		$(this).parent().addClass("active").siblings("li").removeClass("active");

		//e.preventDefault();
	});

	/* 마이페이지(기타) - 관심매장 순번 정렬 : 2016.09.02 */
	$(".mystore").on("click", ".storeAddr .btn_sorting .btn_up", function() {
		$(this).closest("li").prev().before( $(this).closest("li") );
	});
	$(".mystore").on("click", ".storeAddr .btn_sorting .btn_dw", function() {
		$(this).closest("li").next().after( $(this).closest("li") );
	});
	$(".mystore").on("click", ".storeAddr .btn_default", function() {
		$(".mystore .default_store").append( $(this).closest("li") );
		$(".mystore .favorite_store").prepend( $(".mystore .default_store li:first") );
	});

	/* 베스트(실시간,주간 탭) : 2016.09.05 */
	$(".best_type > li > a").off("click").on("click", function() {
		$(this).parent().addClass("on").siblings("li").removeClass("on");
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
});

