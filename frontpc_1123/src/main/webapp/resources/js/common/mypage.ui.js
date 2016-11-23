var startX = 0; // 시작점X.
var startY = 0; // 시작점Y.
var moveX = 0; // 좌우로 움직인 거리.
var moveY = 0; // 상하로 움직인 거리.

var clickEventTypeDown = ((document.ontouchstart !== null) ? "mousedown" : "touchstart");
var clickEventTypeMove = ((document.ontouchstart !== null) ? "mousemove" : "touchmove");
var clickEventTypeUp = ((document.ontouchstart !== null) ? "mouseup" : "touchend");

// 처음 클릭 이벤트 pc & mobile 체크
function fnStartClickType(event) {
	if(clickEventTypeDown == "touchstart"){
		var touch = event.originalEvent;
		startX = parseInt(touch.changedTouches[0].pageX);
		startY = parseInt(touch.changedTouches[0].pageY);
	}else{
		startX = event.pageX;
		startY = event.pageY;
	}
}

// 클릭 후 이동 좌표 이벤트 pc & mobile 체크
function fnMoveClickType(event) {
	if(clickEventTypeDown == "touchstart"){
		var touch = event.originalEvent;
		moveX = parseInt(touch.changedTouches[0].pageX);
		moveY = parseInt(touch.changedTouches[0].pageY);
	}else{
		moveX = event.pageX;
		moveY = event.pageY;
	}
}


$(document).on(clickEventTypeDown, ".mobile .menu_sort .btn_move", function(event) {
	fnStartClickType(event);

	var translateY = 0;
	var target_btn = $(this);
	var target_li = $(this).parent();
	var target_ul = $(this).closest(".menu_sort");
	var target_nextTop;
	var min_offsetTop;
	var max_offsetTop;

	$(this).parent().addClass("on");

	if( $(target_li).next().length == 0 ){
		$(target_ul).css({"padding-bottom" : "43px"});
	}else{
		$(target_ul).removeAttr("style");
	}

	$(document).on(clickEventTypeMove, function(event) {
		fnMoveClickType(event);

		if( $(target_li).prev().length == 0 ){
			min_offsetTop = $(target_ul).offset().top + 2;
		}else{
			min_offsetTop = $(target_li).prev().offset().top;
		}

		if( $(target_li).next().find("input").prop("checked") ){
			max_offsetTop = $(target_li).next().offset().top;
		}else if( $(target_li).next().length == 0 ){
			max_offsetTop = $(target_ul).offset().top + $(target_ul).height();
		}else{
			max_offsetTop = $(target_ul).find("input").not(":checked").eq(0).parent().offset().top;
		}

		if( $(target_li).offset().top < min_offsetTop && $(target_li).prev().length == 0 ){
			if( moveY - startY + translateY <= 0){
				$(target_li).css({"transform" : "translate3d(0px, 0px, 0px)"});
			}else{
				$(target_li).css({"transform" : "translate3d(0px, " + (moveY - startY + translateY) + "px, 0px)"});
			}
		}else if( $(target_li).offset().top >= max_offsetTop - 44 && !$(target_li).next().find("input").prop("checked") ){
			if( moveY - startY + translateY >= 0){
				$(target_li).css({"transform" : "translate3d(0px, 0px, 0px)"});
			}else{
				$(target_li).css({"transform" : "translate3d(0px, " + (moveY - startY + translateY) + "px, 0px)"});
			}
		}else{
			$(target_li).css({"transform" : "translate3d(0px, " + (moveY - startY + translateY) + "px, 0px)"});

			if( $(target_li).offset().top < min_offsetTop + ($(target_li).prev().height() / 2) ){
				translateY = translateY + $(target_li).prev().height();

				$(target_li).after( $(target_li).prev() );
				$(target_li).css({"transform" : "translate3d(0px, " + (moveY - startY + translateY) + "px, 0px)"});
			}else if( $(target_li).offset().top > max_offsetTop - ($(target_li).next().height() / 2) ){
				translateY = translateY - $(target_li).next().height();

				$(target_li).before( $(target_li).next() );
				$(target_li).css({"transform" : "translate3d(0px, " + (moveY - startY + translateY) + "px, 0px)"});
			}

			if( $(target_li).next().length == 0 ){
				$(target_ul).css({"padding-bottom" : "43px"});
			}else{
				$(target_ul).removeAttr("style");
			}
		}

		return false;
	});

	$(document).on(clickEventTypeUp, function(event) {
		fnMoveClickType(event);

		$(target_ul).removeAttr("style");
		$(target_li).removeClass("on").removeAttr("style");

		$(document).off("touchmove mousemove touchend mouseup");
	});

	return false;
});



$(document).ready(function(){
	
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
	
	

	/* 마이페이지(기타) - 관심매장 순번 정렬 : 2016.09.02 */
	$(".mystore").on("click", ".storeAddr .btn_sorting .btn_up", function() {
		$(this).closest("li").prev().before( $(this).closest("li") );
	});
	$(".mystore").on("click", ".storeAddr .btn_sorting .btn_dw", function() {
		$(this).closest("li").next().after( $(this).closest("li") );
	});
	$(".mystore").on("click", ".storeAddr .btn_default", function() {
		if (common.isNotEmpty($(".mystore .default_store li:first"))) {
			$(".mystore .favorite_store").prepend( $(".mystore .default_store li:first") );
		}
		$(".mystore .default_store").append( $(this).closest("li") );
		$(".mystore .favorite_store").prepend( $(".mystore .default_store li:first") );
	});
	
	

	// 마이페이지 기간설정 버튼
	if($(".periodBox").length>0){
		$(".mobile .btnPeriod").off("click").on("click", function(){
			$(this).closest(".periodBox").toggleClass("on");
		});
		// $(".mobile .calendarBox").find(".btn_close").off("click").on("click", function(){
		// 	$(this).closest(".periodBox").removeClass("on");
		// });
	}
	
	

	// 마이페이지 (기타) - 메인 lnb
	$(".mobile .mypageMain").find(".lnb dt").off("click").on("click", function(){

		$(this).parent("dl").toggleClass("open").siblings("dl").removeClass("open");

		// $(this).toggleClass("on")
		// $(this).next("dd").toggle();
	});


	// 마이페이지 (기타) - 매일포인트카드
	$(".mobile .mypageM, .mobile .coupon_list").find(".family_point").off("click").on("click", function(){
		fnLayerPosition($(".mobile .sLayer_maeilpoint"));
	});

	// 마이페이지 (주문/배송) - 배송주기변경버튼
	$(".mobile .regular_setting").find(".btn_schedule").off("click").on("click", function(){
		$(this).closest(".tr_box").find(".special").toggle();
	});


	// 마이페이지 (기타) - 내 쇼핑, 아이정보 toggle
	$(".mobile .mypage .btn_shopping, .mobile .mypage .btn_baby").off("click").on({
		"click" : function(e) {
			$(this).toggleClass("on").next().toggle();

			//e.preventDefault();
		}
	});

	//마이페이지 (기타) - 받은 선물 취소/환불 안내
	$(".mobile .mtoggleBox").find(".toggleBtn").off("click").on("click", function(){
		$(this).parent().toggleClass("on");
	});

	// 마이페이지 (기타) - 받은선물함 상세 옵션
	$(".mobile .mygiftView .btn_tb_option").off("click").on("click", function(e){
		//e.preventDefault();
		$(".ly_box").toggle();
	});
	
	
	// 마이페이지 (기타) - GIFT 쿠폰 적용상품 팝업
	$(".mobile .coupon_box .btn_apply").off("click").on("click", function(e){
		fnLayerOpen( $(".ly_coupon_prod") );

		//e.preventDefault();
	});

	// 마이페이지 (기타) - 예치금 환불신청
	$(".mobile .mymoney").find(".btnRefund").off("click").on("click", function(e){
		fnLayerOpen( $(".ly_moneyRefund") );
	});

	// 마이페이지 (기타) - 새배송지 추가
	$(".mobile .myaddr").find(".btnAdd").off("click").on("click", function(e){
		fnLayerOpen( $(".ly_add_address") );
	});

	// 마이페이지 (기타) - 배송지 수정
	$(".mobile .myaddr").find(".btnChange").off("click").on("click", function(e){
		fnLayerOpen( $(".ly_change_address") );
	});

	// 마이페이지 (기타) - 오프라인 구매내역 상세
	$(".mobile .myoffline").find(".st").off("click").on("click", function(e){
		fnLayerOpen( $(".ly_offline") );
	});

	// 마이페이지(기타) - 상품평
//	if($(".mobile .myreview .photoSwipe").length){
//		$(".mobile .myreview .photoSwipe").each(function() {
//			swipe_ani_type2( $(" ul", this), false);
//		});
//	}

	// 16.11.11 : 마이페이지(기타) - 내가 쓴 상품평
	$(".mobile .myreview .more_review").on("click", function(){
		$(this).closest("li").toggleClass("open").siblings("li").removeClass("open");
	});
	
	// 마이페이지 (기타) - GIFT 쿠폰 등록하기
	$(".mobile .coupon_box .btn_add_coupon").off("click").on("click", function(e){
		$(".mobile .layer_coupon").show().css({"top" : $(window).scrollTop()});

		//e.preventDefault();
	});

	
	// 매장 찾기, 16.11.03 마이페이지 (기타) - 쇼핑알림보관함
	$(".mobile .storeAddr, .mobile .historyBox").find(".btn_detail").off("click").on("click", function(){
		$(this).closest('li').toggleClass("on").siblings('li').removeClass("on");
	});
	
	
	// 마이페이지(기타) - 내가 쓴 상품평
	$(".mobile .myreview .tab_02 .div_tb_tbody3 .tr_box").on("click", function(){
		$(this).parent("li").toggleClass("open").siblings("li").removeClass("open");
	});
	
	

	// 마이페이지 (기타) - 받은선물함 상세 옵션
	$(".pc .giftViewBox").find(".btnOption").off("click").on("click", function(e){
		////e.preventDefault();
		$(this).toggleClass("on");
		$(".pc .giftViewBox").find(".giftOption").toggle();
	});

	// 마이페이지 (기타) - GIFT 쿠폰 적용상품 팝업
	$(".pc .coupon_box .btn_apply").off("click").on("click", function(e){
		$(".ly_coupon_prod").show();

		////e.preventDefault();
	});

	// mod(20160927) $(".pc .style_shop .product_type1").find("a").off("click").on("click", function(e){
	/* mod(20161009)
	$(".pc .style_shop .product_type1 .img, .pc .style_shop .product_type1 .info .title").off("click").on("click", function(e){
		fnPopPosition( $(".sLayer_style") );
	});
	*/
	// 마이페이지 (기타) - 예치금 환불신청
	$(".pc .mymoney").find(".btnRefund").off("click").on("click", function(e){
		fnPopPosition( $(".ly_moneyRefund") );
	});

	// 마이페이지 (기타) - 새배송지 추가
	$(".pc .myaddr").find(".btnAdd").off("click").on("click", function(e){
		fnPopPosition( $(".ly_add_address") );
	});

	// 마이페이지 (기타) - 배송지 수정
	$(".pc .myaddr").find(".btnChange").off("click").on("click", function(e){
		fnPopPosition( $(".ly_change_address") );
	});

	// 마이페이지 (기타) - 오프라인 구매내역 상세
	$(".pc .myoffline").find(".st").off("click").on("click", function(e){
		fnPopPosition( $(".ly_offline") );
	});

	// 마이페이지 (기타) - 최근본상품
	$(".pc .recent .btn_morePrd").off("click").on("click", function(){
		$(this).toggleClass("on")
		$(this).closest(".tr_box").children(".morePrd_list").toggle();
	});
	
	// 16.11.11 : 마이페이지(기타) - 내가 쓴 상품평
	$(".pc .myreview .more_review").on("click", function(){
		$(this).closest("li").toggleClass("open").siblings("li").removeClass("open");
	});

	// 상품평 별점 주기
	$(".rating_list .star_box input").on({
		"click" : function() {
			var target_tag = $(this).parents(".star_box");
			//alert($("input", target_tag).index( $(this) ));
			$("input", target_tag).removeClass("checked");
			$(this).addClass("checked");
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
				$(this).next().hide();
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
});
