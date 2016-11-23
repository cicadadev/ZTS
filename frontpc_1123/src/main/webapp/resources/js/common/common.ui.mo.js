var clickEventTypeMove = ((document.ontouchstart !== null) ? "mousemove" : "touchmove");
var clickEventTypeDown = ((document.ontouchstart !== null) ? "mousedown" : "touchstart");
var clickEventTypeUp = ((document.ontouchstart !== null) ? "mouseup" : "touchend");


/* 레이어팝업 띄우기 : 2016.08.22 */
function fnLayerOpen(target_tag) {
	$(target_tag).show();
	$(".header_mo, .mobile .content, .footer_mo").hide();
	$(window).scrollTop(0);
}
	
//모바일 top 버튼 이벤트 
function moSky_scraper() {
	var max_scroll = $(".wrap .mobile").innerHeight();
	var now_scroll = $(this).innerHeight() + $(this).scrollTop();
	var sky_scraper = $(".mobile .btn_up_mo");

	var win_scroll = $("html, body").innerHeight() + 100;
	
	sky_scraper.click( function(e) {
		$("html, body").stop().animate({ scrollTop: 0 }, 200);
	});

	if(now_scroll > win_scroll){
		sky_scraper.stop().fadeIn('fast').animate({bottom:"59px"},{duration:200, queue:true});
		
	} else {
		sky_scraper.stop().animate({bottom:"-3px"},{duration:200, queue:false}).fadeOut('fast');

	}

}



function btnListTypeEvt(){
	
	/* 블럭형/리스트형 선택 : 2016.08.08 */
	$(".mobile .btnListType").off("click").on("click", function(){
		var listLength;
		var target_tag;

		if( $(this).closest(".tit_style3").length ){
			// 제목 태그에 감싸여 있을 경우
			listLength = $(this).closest(".tit_style3").nextUntil(".list_group").length;
			target_tag = $(this).closest(".tit_style3");
		} else if( $(this).closest(".tabFixW").length ){ //16.10.25 전문관 탭 고정
			listLength = $(this).closest(".tabFixW").nextUntil(".list_group").length;
			target_tag = $(this).closest(".tabFixW");
		} else{
			// 일반 형제태그로 있을 경우
			listLength = $(this).closest(".sortBoxList").nextUntil(".list_group").length;
			target_tag = $(this).closest(".sortBoxList");
		}
		// 16.11.12 : str
		if($(this).hasClass("list")){
			$(this).removeClass("list").addClass("block");
			$(this).closest(".inner").find(".product_type1").removeClass("block").addClass("list");

		} else {
			$(this).removeClass("block").addClass("list");
			$(this).closest(".inner").find(".product_type1").removeClass("list").addClass("block");
		}
	});
}

$(window).scroll(function(e) {
	//moNaviPosition();
	moSky_scraper();
});
//2016.11.3 수정 end

$(document).ready(function() {

	var $body = $("html, body");
	
	function allMenu(e) {
		$(".mobile .all_menu .box, .mobile .com_brand .box").filter(function(){
			$(this).outerHeight($(window).height());
		});
	}
	
	allMenu();
	
	// mo 좌측 메뉴 열림/닫힘
	function fnMenuOpen(target_tag, className) {
		$(target_tag).addClass( className );
		$(".mobile .dim_fixed").show();
		$body.css({"overflow": "hidden"});
		$(".mobile .all_menu .box, .mobile .com_brand .box").scrollTop(0);
	}
	
	
	function fnMenuClose(target_tag, className) {
		$(target_tag).removeClass( className );
		$(".mobile .dim_fixed").hide();
		//$(".wrap").removeAttr("style");
		$body.removeAttr("style");
	}
	
	// 모바일 좌측 메뉴
	$(".mobile .btn_all").off("click").on({
		"click" : function(e) {
			fnMenuOpen( $(".mobile .all_menu"), "active_all" );
			
			//e.preventDefault();
		}
	});
	$(".mobile .all_menu .btn_all_close, .mobile .dim_fixed").off("click").on({
		"click" : function() {
			fnMenuClose( $(".mobile .all_menu"), "active_all" );
		}
	});
	
	// 모바일 우측 브랜드
	/* 상단 자사브랜드 : 2016.08.12 */
	$(".mobile .btn_brand").off("click").on({
		"click" : function(e) {
			fnMenuOpen( $(".mobile .com_brand"), "active_com" );

			//e.preventDefault();
		}
	});
	$(".mobile .com_brand .btn_com_close").off("click").on({
		"click" : function() {
			fnMenuClose( $(".mobile .com_brand"), "active_com" );
		}
	});


	// 모바일 검색(돋보기)
	/* 헤더 검색 : 2016.08.03 추가 */
	var hSearchStr = $(".layer_mo_search .hd_mo_search").val();

	$(".header_mo .etc_util .btn_search").off("click").on({
		"click" : function() {
			$(".layer_mo_search").show();
			$(".layer_mo_search .hd_mo_search").focus();
		}
	});
	$(".layer_mo_search .hd_mo_search").off().on({
		"focus" : function() {
			$(".layer_mo_search .srchWay").show();
			if( $(this).val() == hSearchStr ){
				$(this).val("");
			}

			if( $(this).val().length ){
				$(".layer_mo_search .word").hide();
				$(".layer_mo_search .similar").show();
			}else{
				$(".layer_mo_search .word").show();
				$(".layer_mo_search .similar").hide();
			}
		},
		"keyup" : function() {
			if( $(this).val().length ){
				$(this).siblings(".btn_strDel").show();
				$(".layer_mo_search .word").hide();
				$(".layer_mo_search .similar").show();
			}else{
				$(this).siblings(".btn_strDel").hide();
				$(".layer_mo_search .word").show();
				$(".layer_mo_search .similar").hide();
			}
		}
	});
	$(".layer_mo_search .btn_strDel").off("click").on({
		"click" : function(e) {
			if( $(this).parent().hasClass("fnBox") ){
				$(".layer_mo_search .hd_mo_search").val("");
				$(this).hide();
				$(".layer_mo_search .word").show();
				$(".layer_mo_search .similar").hide();
			}else{
				$(this).closest("li").remove();
				return false;
			}
		}
	});
	$(".layer_mo_search .util .btn_close").off("click").on({
		"click" : function(e) {
			$(".layer_mo_search .tabBox a").eq(0).trigger("click");
			$(".layer_mo_search .srchWay").hide();
			if( $(this).closest(".layer_mo_search").hasClass("showonly") ){
				$(this).closest(".layer_mo_search > div").removeAttr("style");
			}else{
				$(this).closest(".layer_mo_search").hide();
			}

			//e.preventDefault();
		}
	});
	
	// [모바일] 레이어팝업 닫기 (큰 레이어 형태) 
	$(".mobile .layer_type1 .layer_inner > .btn_close, .mobile .layer_type1 > .btn_close").off("click").on({
		"click" : function() {
			$(this).closest(".layer_type1").hide();
			$(".mobile .content_mo").removeClass("content_fixed").show();
			$(".header_mo").show();
			$(".mobile .content_mo, .content, .footer, .footer_mo").show();
			if( $(".mobile .prod_info_tab").length > 0 ){
				prod_info_tab_top = Math.floor($(".prod_info_tab").offset().top);
				$(window).scrollTop($(".mobile .prod_info_tab").offset().top - 40);
			}
			$(".mobile").removeAttr("style");

			// 상품상세 구매 옵션 버튼
			if($(".mobile .buy_fixed").length > 0 && !$(this).parent().hasClass("layer_pick_up") ){
				$(".mobile .buy_fixed").css({"marginBottom" : -$(".buy_fixed").innerHeight(), "bottom" : "0"});
			}
		}
	});


	// 셀렉트 박스
	$(".mobile .select_box select").off("change").on({
		"change" : function() {
			$(this).prev().text( $("option:selected", this).text() );
		}
	});

	// 퀵메뉴 보기
	$(".mobile .btn_quick").off("click").on({
		"click" : function() {
			if( $(this).parent().hasClass("quick_open") ){
				$(this).parent().removeClass("quick_open");
			}else{
				$(this).parent().addClass("quick_open");
			}
		}
	});

	// 로그인 레이아웃 열기
	$(".mobile .login_box .srch_pw").off("click").on({
		"click" : function() {
			fnLayerOpen( $(".ly_pw") );
		}
	});
	
	$(".mobile .btn_nonOrderNum").off("click").on({
		"click" : function() {
			//fnLayerOpen( $(".ly_findNum") );
			$(".ly_orderSearch").show();
			$(".ly_orderNum").hide();
		}
	});
	$(".mobile .btn_nonPw").off("click").on({
		"click" : function() {
			//fnLayerOpen( $(".ly_nonPw") );
			$(".ly_pwSearch").show();
			$(".ly_orderNum").hide();
		}
	});

	// 로그인 layer - 주문번호 찾기
	$(".mobile .srch_ordernum").off("click").on({
		"click" : function() {
			$(".header_pc, .header_mo").hide();
			$(".mobile .content_mo, .content, .footer_mo").hide();
			$(".mobile .ly_orderNum").show();
		}
	});



	// 로그인 layer - 내용없음
	$(".mobile .layer_nodetail .btn_close").off("click").on({
		"click" : function() {
			$(this).parents(".layer_nodetail").hide();
		}
	});
	
	
	// 16.10.15 : 옵션레이어 열고 닫기
	$(".mobile .stateBox .btn_tb_option").off("click").on("click", function(e){
		var optionH = $(this).next(".ly_box").outerHeight() + 10;
		$(this).toggleClass("on");
		if($(this).hasClass("on")){
			$(this).next(".ly_box").show();
			//$(this).closest(".stateBox").css("padding-bottom", optionH);

			if(!$(this).closest("mygiftView")){
				$(this).closest(".stateBox").css("padding-bottom", optionH);
			}

		} else if(!$(this).hasClass("on")) {
			$(this).next(".ly_box").hide();
			$(this).closest(".stateBox").removeAttr("style");
		}
	});

	
	// 팝업 - 관심매장지정 버튼
	$(".mobile .pop_wrap").find(".bookmark").off("click").on("click", function(){
		$(this).toggleClass("checked");
	});
	
	// 매장 찾기 주변매장보기 버튼
	$(".mobile .near").off("click").on("click", function(e){
		//e.preventDefault();
		$(this).addClass("on");
	});
	
	
	/* 헤더 검색 : 2016.08.03 추가 */
	var hSearchStr = $(".layer_mo_search .hd_mo_search").val();

	$(".header_mo .etc_util .btn_search").off("click").on({
		"click" : function() {
			$(".layer_mo_search").show();
			$(".layer_mo_search .hd_mo_search").focus();
		}
	});
	$(".layer_mo_search .hd_mo_search").off().on({
		"focus" : function() {
			$(".layer_mo_search .srchWay").show();
			if( $(this).val() == hSearchStr ){
				$(this).val("");
			}

			if( $(this).val().length ){
				$(".layer_mo_search .word").hide();
				$(".layer_mo_search .similar").show();
			}else{
				$(".layer_mo_search .word").show();
				$(".layer_mo_search .similar").hide();
			}
		},
		"keyup" : function() {
			if( $(this).val().length ){
				$(this).siblings(".btn_strDel").show();
				$(".layer_mo_search .word").hide();
				$(".layer_mo_search .similar").show();
			}else{
				$(this).siblings(".btn_strDel").hide();
				$(".layer_mo_search .word").show();
				$(".layer_mo_search .similar").hide();
			}
		}
	});
	$(".layer_mo_search .btn_strDel").off("click").on({
		"click" : function(e) {
			if( $(this).parent().hasClass("fnBox") ){
				$(".layer_mo_search .hd_mo_search").val("");
				$(this).hide();
				$(".layer_mo_search .word").show();
				$(".layer_mo_search .similar").hide();
			}else{
				$(this).closest("li").remove();
				return false;
			}
		}
	});
	$(".layer_mo_search .util .btn_close").off("click").on({
		"click" : function(e) {
			$(".layer_mo_search .tabBox a").eq(0).trigger("click");
			$(".layer_mo_search .srchWay").hide();
			if( $(this).closest(".layer_mo_search").hasClass("showonly") ){
				$(this).closest(".layer_mo_search > div").removeAttr("style");
			}else{
				$(this).closest(".layer_mo_search").hide();
			}

			//e.preventDefault();
		}
	});
	


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
	
	
	
	/* 옵션형 버튼 : 2016.08.04 추가 */
	function fnOptionStyle(){
		$(".mobile .option_style1 input").each(function() {
			if($(this).is(':checked')){
				$(this).parent().addClass("selected");
			}
		});

		$(".mobile .option_style1 input").off("change").on({
			"change" : function() {
				if( $(this).attr("type") == "checkbox" ){
					if($(this).is(':checked')){
						$(this).parent().addClass("selected");
					}else{
						$(this).parent().removeClass("selected");
					}
				}else if( $(this).attr("type") == "radio" ){
					var this_name = $(this).attr("name");
					$("input[name='" + this_name + "']").parent().removeClass("selected");
					$(this).parent().addClass("selected");
				}else{
				}
			}
		});
	}
	fnOptionStyle();
	
	// 전문관 리스트형 버튼
	btnListTypeEvt();
	
	/* 베스트 메인 블럭형/리스트형 선택 : 2016.09.06 */
	$(".mobile .tit_best .btnListType").off("click").on("click", function(){
		var listLength;
		var target_tag;

		listLength = $(this).closest(".tit_best").nextUntil(".list_group").length;
		target_tag = $(this).closest(".tit_best");
		$(this).toggleClass("block");

		if(listLength == 0){
			$(target_tag).next(".list_group").find(".product_type1").toggleClass("block");
		} else{
			$(target_tag).nextUntil(".list_group").next(".list_group").find(".product_type1").toggleClass("block");
		}
	});
	
	/* 하단 고정 메뉴 (더보기) : 2016.08.11 */
	$(".mobile .bottom_menu .btn_setup").off("click").on("click", function(){
		$(this).parent().toggleClass("on");
	});

	/* 좌측메뉴- 카테고리(아이템별) : 2016.08.12 */
	$(".mobile .category .item > li > a").off("click").on({
		"click" : function(e) {
			$(this).parent().addClass("on").siblings().removeClass("on");

			//e.preventDefault();
		}
	});
	
	
	/* 좌측메뉴 - 브랜드 스와이프 : 2016.08.12 */
	function fnBrandPosition() {
		var gnb_left = 0;

		$(".all_menu .choice_brand .sorting_on li").each(function(index) {
			$(this).css({"left" : gnb_left + "px"});
			gnb_left = gnb_left + $(this).innerWidth();
		});
	}
	if( $(".mobile .all_menu .choice_brand .sorting").length ){
		$(".all_menu .choice_brand .sorting").each(function(index) {
			//swipe_ani_type1( $(this) );
		});

		$(".all_menu .tabBox a").on({
			"click" : function(e) {
				if( $(".all_menu .tabBox a").index( $(this) ) == 1 ){
					fnBrandPosition();
				}

				//e.preventDefault();
			}
		});
	}

	/* 좌측메뉴 - ABC, ㄱㄴㄷ 브랜드 정렬 : 2016.08.12 */
	$(".mobile .all_menu .choice_brand .btn_abc").off("click").on({
		"click" : function(e) {
			var list = $(this).next();

			if( $(this).hasClass("btn_rse") ){
				$(this).removeClass("btn_rse").text("ABC");
				$(this).closest(".choice_brand").find(".sort_list ").hide().eq(0).show();
				swiperCon('brandMenuSwiper_sortingWord_kr', 'auto');
			}else{
				$(this).addClass("btn_rse").text("ㄱㄴㄷ");
				$(this).closest(".choice_brand").find(".sort_list ").hide().eq(1).show();
				swiperCon('brandMenuSwiper_sortingWord_en', 'auto');
			}
		}
	});
	
	/* 좌측메뉴 - 관심브랜드 초성 버튼 : 2016.08.16 */
//	$(".mobile .all_menu").on("click", ".sorting_on a", function(e) {
//		var all_menu = $(".mobile .all_menu");
//
//		$(" .btn_my_brand", all_menu).removeClass("btn_brandOn");
//		$(" .like_brand ul", all_menu).hide();
////		$(" .sc_brand", all_menu).show(); 검색 숨기기
//
//		$(" .like_brand .tit1", all_menu).addClass("tit1_on");
//
//		$(this).closest(".sorting").siblings(".sorting").find("li.on").removeClass("on");
//
//		//e.preventDefault();
//	});
	
	/* 좌측메뉴 - 관심브랜드 초성 버튼 */
	$(".mobile .all_menu").on("click", ".sorting_on a", function(e) {

		var all_menu = $(".mobile .all_menu");

		$(".btn_my_brand", all_menu).removeClass("btn_brandOn");
		$(".like_brand ul", all_menu).hide();
		$(".sc_brand", all_menu).show();

		$(".like_brand .tit1", all_menu).addClass("tit1_on");

		$(".sorting_on").find("li").removeClass("on");
		$(this).parent().addClass("on");

	});
	
	
	/* 좌측메뉴 - 관심브랜드 버튼 : 2016.08.16 */
	$(".mobile .all_menu .btn_my_brand").on({
		"click" : function(e) {
			var all_menu = $(".mobile .all_menu");

			$(this).toggleClass("btn_brandOn");
			$(" .like_brand ul", all_menu).toggle();
			$(" .like_brand .tit1", all_menu).toggleClass("tit1_on");
			$(" .sorting li", all_menu).removeClass("on");

			//e.preventDefault();
		}
	});

	/* 좌측메뉴 - 관심브랜드 목록 접기&펴기 : 2016.08.16 */
	$(".mobile .all_menu .like_brand .tit1").on({
		"click" : function(e) {
			$(this).toggleClass("tit1_on").next().toggle();
			$(".mobile .all_menu .btn_my_brand").toggleClass("btn_brandOn");

			//e.preventDefault();
		}
	});
	
	/* 상단 자사브랜드 : 2016.08.12 */
	$(".mobile .btn_brand").off("click").on({
		"click" : function(e) {
			fnMenuOpen( $(".mobile .com_brand"), "active_com" );

			//e.preventDefault();
		}
	});
	$(".mobile .com_brand .btn_com_close").off("click").on({
		"click" : function() {
			fnMenuClose( $(".mobile .com_brand"), "active_com" );
		}
	});


	
	/* 검색 결과 - 상세검색 열기/닫기 : 2016.08.17 */
	$(".mobile .srchBtns a").off("click").on("click", function(e){
		//e.preventDefault();

		$(this).parent().toggleClass('on').siblings("li").removeClass('on');

		if($(this).parent().hasClass("on")){
			$(".optionBox").show();
			if($(this).hasClass("btnCategory")){
				$(".optionItem.category").show();
				$(".optionItem.category").parent().show().siblings(".optionItemBox").hide();
				swiperCon('searchResultSwiper_searchList', '1', 30);
			} else if($(this).hasClass("btnBrand")){

				$(".optionItem.brand").show().siblings(".optionItem").hide();
				$(".optionItem.brand").parent().show().siblings(".optionItemBox").hide();
				swiperCon('searchResultSwiper_brandList', '1', 30);
			} else if($(this).hasClass("btnDetail")){
				$(".optionItem.brand").hide().siblings(".optionItem").show();
				$(".optionItem.brand").parent().show().siblings(".optionItemBox").hide();
			}
			$(".checkedTxt").show();
		} else {
			$(".mobile .optionBox").hide();
			$(".checkedTxt").hide();
		}
	});

	

	/* 레이어 팝업 닫기 : 2016.08.22 */
	$(".mobile .pop_wrap .btn_x").off("click").on({
		"click" : function() {
			$(this).closest(".pop_wrap").hide();
			$(".mobile .content").removeClass("content_fixed").show();
			$(".header_mo, .footer, .footer_mo").show();
			$(".mobile").removeAttr("style");
		}
	});

	
	/* 작은 레이어팝업 닫기 : 2016.08.22 */
	$(".mobile .layer_style1 .btn_close").off("click").on({
		"click" : function() {
			$(this).closest(".layer_style1").hide();
		}
	});
	
	
	//moNaviPosition();
	
	// 맨위로 버튼
	moSky_scraper();

	
	
	/* 전시 - 중카테고리 클릭하면 나오는 소카테고리 스와이프 (3뎁스) : 2016.08.18 */
	$(".mo_navi h2 a").off("click").on("click", function(e){
		$(this).toggleClass("active");

		if( $(this).hasClass("rw_btnMCate") ){
			$(".categoryS").toggle();

			if( $(".mobile .categoryS").css("display") != "none" && $(".content").hasClass("mo_navi_fixed") ){
				var cateH2 = $(".mobile .categoryS").height();
				$(".content").find(">.inner").css("padding-top", 40+cateH2);
			}else{
				$(".content").find(">.inner").removeAttr("style");
			}
		}else if( $(this).hasClass("btn_location") ){
			$(".mo_navi .layer_location").toggle();
		}

		//e.preventDefault();
	});
	
	$(".mobile .evt_tit").off("click").on("click", function(e){
		$(this).parent().toggleClass("slideHide");
	});

	
});

