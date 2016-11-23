	
$(document).ready(function(){
	


	/* 고객센터 - FAQ카테고리박스 : 2016.08.30 */
	$(".faqCateBox a").off("click").on("click", function(e) {
		$(this).parent().addClass("on").siblings("li").removeClass("on");

		//e.preventDefault();
	});
	
	
	if($(".orderNBox").length>0){
		$(".inp_placeholder").find("input[type='text'], input[type='tel'], input[type='number'], input[type='password']").each(function(){
			$(this).focus(function(){
				$(this).addClass("focus");
				$(this).prev("label").hide();
			});

			$(this).blur(function(){
				$(this).removeClass("focus");
				if($(this).val() == ""){
					$(this).prev("label").show();
				}
			});
		});
	}
	
	
	// 고객센터 (주문상품 조회)
	$(".mobile .btn_order_search").off("click").on({
		"click" : function() {
			fnLayerOpen( $(".ly_order_search") );
		}
	});

	/* 고객센터 - 당첨자 팝업 : 2016.08.31 */
	$(".mobile .btn_winner").on("click", function(e){
		fnLayerOpen( $(".mobile .ly_winner") );

		//e.preventDefault();
	});
	/* 고객센터 - 비회원 주문 취소 팝업 : 2016.09.01 */
	$(".mobile .btn_order_cancel").on("click", function(e){
		fnLayerOpen( $(".mobile .ly_orcan") );

		//e.preventDefault();
	});
	/* 고객센터 - 비회원 반품,교환 팝업 : 2016.09.01 */
	$(".mobile .btn_order_return").off("click").on("click", function(e){
		fnLayerOpen( $(".mobile .ly_cha") );

		//e.preventDefault();
	});
	
	/* 고객센터 - 목록 펼치기 : 2016.08.29 */
	$(".mobile .csBox .btn_answer:not('.mo_noEvent')").off("click").on("click", function(e){
		// $(this).closest("li").toggleClass("on").siblings("li").removeClass('on');
		// if($(this).closest("li").hasClass("on")){
		// 	$(this).closest(".tr_box").next(".answer").show();
		// } else {
		// 	$(this).closest(".tr_box").next(".answer").hide();
		// }

		$(this).closest("li").toggleClass("on").siblings("li").removeClass("on");
	});
	
	/* 고객센터 - 당첨자 팝업 : 2016.08.31 */
	$(".pc .btn_winner").on("click", function(e){
		fnPopPosition( $(".ly_winner") );

		//e.preventDefault();
	});
	/* 고객센터 - 주문상품 조회 팝업 : 2016.08.31 */
	$(".pc .btn_order_search").off("click").on("click", function(e){
		fnPopPosition( $(".ly_order_search") );

		//e.preventDefault();
	});
	/* 고객센터 - 비회원 주문 취소 팝업 : 2016.09.01 */
	$(".pc .btn_order_cancel").off("click").on("click", function(e){
		fnPopPosition( $(".ly_orcan") );

		//e.preventDefault();
	});
	/* 고객센터 - 비회원 반품,교환 팝업 : 2016.09.01 */
	$(".pc .btn_order_return").off("click").on("click", function(e){
		fnPopPosition( $(".ly_cha") );

		//e.preventDefault();
	});
	
	/* 고객센터 - 목록 펼치기 : 2016.08.29 */
	$(".pc .csBox .btn_answer:not('.pc_noEvent')").off("click").on("click", function(e){
		// $(this).closest("li").toggleClass("on").siblings("li").removeClass('on');
		// $(".pc .csBox .answer").hide()
		// $(this).closest(".tr_box").next(".answer").toggle();
		$(this).closest("li").toggleClass("on").siblings("li").removeClass("on");
	});
});	