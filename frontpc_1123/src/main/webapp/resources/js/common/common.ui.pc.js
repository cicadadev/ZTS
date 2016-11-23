

	/* 상품 목록 컨트롤러 (페이지 수 체크) : 2016.08.09 */
	function fnListMaxPage(targetList, num) {
		var target_list = $(".product_type1", targetList);
		var list_max = $("li", target_list).length / num;

		$(".total", targetList).text(list_max);
	}

	/* 상품 목록 컨트롤러 (클릭) : 2016.08.09 */
	function fnListControl(targetList, btn_this, num) {
		var target_list = $(".product_type1", targetList);
		var list_max = $("li", target_list).length / num;

		if( $(btn_this).hasClass("prev") ){
			for(var i = 0; i < num; i++){
				$("ul", target_list).prepend( $("li:last", target_list) );
			}

			var now_num = parseInt( $(".current", targetList).text() ) - 1;
			if(now_num == 0){
				now_num = list_max;
			}

			$(".current", targetList).text(now_num);
		}else{
			for(var i = 0; i < num; i++){
				$("ul", target_list).append( $("li:first", target_list) );
			}

			var now_num = parseInt( $(".current", targetList).text() ) + 1;
			if(now_num > list_max){
				now_num = 1;
			}

			$(".current", targetList).text(now_num);
		}
	}

	/* 상품 목록 컨트롤러 (자동) : 2016.08.09 */
	function fnListAuto(targetList){
		var auto_list;
		var targetName = $(".product_type1", targetList);
		if( targetName.length == 0){
			targetName = $(".product_type1", targetList);
		}

		function fnTrigger(){
			auto_list = setTimeout(function() {
				$(".next", targetList).trigger("click");
				fnTrigger();
			}, 1000);
		}
		fnTrigger();

		$(targetName).on({
			"mouseenter" : function() {
				clearTimeout(auto_list);
			},
			"mouseleave" : function() {
				fnTrigger();
			}
		});
		$(".paginate", targetList).on({
			"mouseenter" : function() {
				clearTimeout(auto_list);
			},
			"mouseleave" : function() {
				fnTrigger();
			}
		});
	}

	/* 목록이 몇개 단위인지 체크하기 : 2016.08.18 */
	function fnListConEa(target_name) {
		var num = 3;

		if( $(target_name).hasClass("prodType_5ea") ){
			num = 5;
		}else if( $(target_name).hasClass("prodType_4ea") ){
			num = 4;
		}else if( $(target_name).hasClass("prodType_2ea") ){
			num = 2;
		}

		return num;
	}
	

	function fnPopPosition(target_pop) {
		$(target_pop).show();
		$(target_pop).height( $(document).height() );

		var base_top = ($(window).height() - $(".pop_inner", target_pop).innerHeight()) / 2;
		var target_po = base_top + $(window).scrollTop() + $(".pop_inner", target_pop).innerHeight();
		var target_poMin = 10;
		var target_poMax = base_top + $(window).scrollTop() - (target_po - $(document).height() + 10);

		if(target_po > $(document).height()){
			$(".pop_inner", target_pop).css({"marginTop" : target_poMax + "px"});
		}else if(base_top + $(window).scrollTop() < 0){
			$(".pop_inner", target_pop).css({"marginTop" : target_poMin + "px"});
		}else{
			$(".pop_inner", target_pop).css({"marginTop" : base_top + $(window).scrollTop() + "px"});
		}
	}
	
	/* 버튼 아래 레이어 팝업 위치 설정 : 2016.11.01 */
	function fnsPopPosition(target_pop) {
		$(".pc .pop_wrap_sm").hide();
		$(target_pop).show();
	}

	/* 팝업 내 컨텐츠가 열릴 경우.. : 2016.08.09 */
	function fnPopReposition(target_pop) {
		$(target_pop).show();

		var popup_inner = $(target_pop).closest(".pop_wrap ").find(".pop_inner");
		var base_top = ($(window).height() - $(popup_inner).innerHeight()) / 2;

		$(popup_inner).css({"marginTop" : base_top + $(window).scrollTop() + "px"});
	}
	
	function showDim(){
		var $dim = "<div class='dim'></div>";
		$(".wrap").append($dim);
		$(".dim").show();
	}

	function hideDim(){
		$(".dim").remove();
	}
	

	/* 스카이 너비 가능 범위 : 2016.08.17 */
	function fnMinSky() {
		if($(window).width() < 1200){
			$(".sky_scraper").css({"right" : $(window).width() - 1200 + "px", "paddingRight" : Math.floor((1200 - $(window).width()) / 2) + "px"});
		}else{
			$(".sky_scraper").css({"right" : 0, "paddingRight" : 0});
		}
	}
	
	/* 상단 배너 너비 가능 범위 : 2016.08.12 */
	function fnMinHeaderBannerClose() {
		if($(window).width() < 1200){
			$(".header_pc .btn_close_box").css({"right" : $(window).width() - 1200 + "px", "paddingRight" : Math.floor((1200 - $(window).width()) / 2) + "px"});
		}else{
			$(".header_pc .btn_close_box").css({"right" : 0, "paddingRight" : 0});
		}
	}
	
	// 스카이 스크롤 가능 범위
	function fnSkyScroll() {
		var header_banner_h = $(".header_pc .banner").innerHeight();
		var header_inner_h = $(".header_pc .inner").innerHeight();
		var header_h = header_banner_h + header_inner_h;

		var top_position = header_h + $(".sky_scraper").innerHeight() + 59;
		var scroll_now = $(window).height() + $(window).scrollTop();
		var max_scroll = $(document).height() - (top_position + $(".footer").innerHeight());

		//console.log(top_position, $(window).scrollTop() + top_position, max_position);
		//console.log("scroll_now : " + scroll_now, ", max_scroll : " + max_scroll, ", $(window).scrollTop() : " + $(window).scrollTop());

		if($(window).scrollTop() > header_h){
			if($(window).scrollTop() - header_h > max_scroll){
				$(".sky_scraper").css({"position" : "absolute", "top" : header_h+"px", "marginTop" : max_scroll + "px"});
			}else{
				$(".sky_scraper").css({"position" : "fixed", "top" : "0px", "marginTop" : 0});
			}
		}else{
			$(".sky_scraper").css({"position" : "absolute", "top" : header_h+"px", "marginTop" : 0});
		}
	}
	
$(document).ready(function() {

	var $body = $("html, body");

	// 스카이 스크래퍼 위치..
	fnSkyScroll();

	// [PC] 레이어 팝업 닫기
	$(".pc .layer_type1 .btn_close").off("click").on({
		"click" : function(e) {
			$(this).closest(".layer_type1").hide();
		}
	});
	
	$(".js_list2n > li").each(function(){
		if(($(this).index() + 0) % 2 == 0){
			$(this).addClass("first");
		}
		if($(this).index() < 1){
			$(this).addClass("first");
		}
	});
	
	
	//textarea
	$(".txtarea_box").find("textarea").each(function(){
		$(this).focus(function(){
			$(this).next("span").hide();
		});

		$(this).blur(function(){
			if($(this).val() == ""){
				$(this).next("span").show();
			}
		});
	});

	//input file
	$(".add_file").find("input[type='file']").off("change").on("change", function(){
		var fileName = $(this).val();
		$(this).parent().find(".input_file").val(fileName);
	});

	
	//radio
	function customRadiobox(radioName){
		var radioBox = $('input.inp_radio');
		$(radioBox).each(function(){
			$(this).wrap( "<span class='custom-radio'></span>" );
			if($(this).is(':checked')){
				$(this).parent().addClass("selected");
			}
		});
		$(radioBox).change(function(){
			var this_name = $(this).attr("name");
			$("input[name='" + this_name + "']").parent().removeClass("selected");
			$(this).parent().addClass("selected");
		});
	}
	customRadiobox("inp_radio");
	
	fnMinSky();
	fnMinHeaderBannerClose();
	
	$(".pc .check_box input").off("change").on({
		"change" : function() {
			if( $(this).prop("checked") ){
				$(this).parent().addClass("inp_checked");
			}else{
				$(this).parent().removeClass("inp_checked");
			}
		}
	});

	// popup close
	$(".pop_wrap .btn_close").off("click").on("click", function(){
		$(this).closest(".pop_wrap").hide();
	});



	$(".pc .srch_pw").off("click").on("click", function(){
		$(".ly_pw").show();
	});

	
	// 헤더 배너 닫기
	$(".header_pc > .banner .btn_close").off("click").on({
		"click" : function() {
			var obj_tx = $(this).parents(".banner");
			obj_tx.hide();
			obj_tx.css({"height":"0px"});

			fnSkyScroll();
		}
	});
	
	

	// 바로 방문 클릭
	$(".header_pc .btn_baro a").off("click").on({
		"click" : function(e) {
			$(this).parent().addClass("on");

			////e.preventDefault();
		}
	});
	$(".header_pc .btn_baro").on({
		"mouseleave" : function() {
			$(this).removeClass("on");
		}
	});

	// 내메뉴 클릭
	$(".header_pc .btn_my a").off("click").on({
		"click" : function(e) {
			$(this).parent().addClass("on");

			////e.preventDefault();
		}
	});
	$(".header_pc .btn_my").on({
		"mouseleave" : function() {
			$(this).removeClass("on");
		}
	});
	
	
	$(".header_pc .navi .info li").on({
		"mouseenter" : function() {
			$(this).addClass("on").siblings("li").removeClass("on");
		}
	});

	
	if($(".popular").length>0){
		var target_list = $(".popular .item_list");
		if( target_list.length == 0){
			target_list = $(".popular .itemList");
		}
		var list_max = $("li", target_list).length / 5;

		fnListMaxPage($(".popular"), 5);

		$(".popular .paginate button").off("click").on({
			"click" : function() {
				fnListControl($(".popular"), $(this), 5);
			}
		});

		for(var i=0; i<list_max; i++){
			var j = 1 + (5*i);
			var k = 2 + (5*i);
			$(".popular").find(".itemList li").eq(j).addClass("second");
			$(".popular").find(".itemList li").eq(k).addClass("big");
		}

		// 자동 롤링 추가하고 싶을때
		//fnListAuto( $(".popular") );

		//fnItemList($(".popular"), 5);
	}
	
	
	// 매장 찾기
	$(".pc .storeAddr").find(".btn_detail").off("click").on("click", function(){
		$(this).closest('li').addClass("on").siblings('li').removeClass("on");
	});

	$(".pc .storeList").find(".bookmark").off("click").on("click", function(){
		$(this).closest('li').find(".bookmark").toggleClass("checked");
	});

	// 팝업 - 관심매장지정 버튼
	$(".pc .pop_wrap").find(".bookmark").off("click").on("click", function(){
		$(this).toggleClass("checked");
	});
	

	/* 체크박스 : 2016.07.26 추가 */
	function fnChkStyle(){
		$(".pc .chk_style1 input").each(function() {
			if($(this).is(':checked')){
				$(this).parent().addClass("selected");
			}
		});

		$(".pc .chk_style1 input").on({
			"change" : function() {
				if($(this).is(':checked')){
					$(this).parent().addClass("selected");
				}else{
					$(this).parent().removeClass("selected");
				}
			}
		});
	}
	fnChkStyle();
	
	
	/* 라디오박스 : 2016.07.26 추가 */
	function fnRadioStyle(){
		$(".pc .radio_style1 input").each(function() {
			if($(this).is(':checked')){
				$(this).parent().addClass("selected");
			}
		});

		$(".pc .radio_style1 input").off("change").on({
			"change" : function() {
				var this_name = $(this).attr("name");
				$("input[name='" + this_name + "']").parent().removeClass("selected");
				$(this).parent().addClass("selected");
			}
		});
	}
	fnRadioStyle();
	
	
	/* 찜하기 버튼 : 2016.07.27 추가 */
	function fnDibs(){
		$(".pc .dibs_box input").each(function() {
			if($(this).is(':checked')){
				$(this).parent().addClass("dibs_on");
			}
		});

		$(".pc .dibs_box input").off("change").on({
			"change" : function(e) {
				if($(this).is(':checked')){
					$(this).parent().addClass("dibs_on");
				}else{
					$(this).parent().removeClass("dibs_on");
				}

				////e.preventDefault();
			}
		});
	}
	fnDibs();
	
	
	/* 헤더 검색 : 2016.08.03 추가 */
	var searchBestAuto = new Array();
	function fnAutoSearchBox(target_tag, idx) {
		var targetBest = $("label span", target_tag);
		var bestNum = $(targetBest).index( $("label span.active", target_tag) );

		bestNum = bestNum + 1;

		if( bestNum >= $(targetBest).length ){
			bestNum = 0;
		}

		$(targetBest).removeClass("active").eq(bestNum).addClass("active");
		
		//2016.10.27 emily 추가
		$('#textBannerUrl').val($("label span.active", target_tag).attr("data-url"));
		searchBestAuto[idx] = setTimeout(function() {
			fnAutoSearchBox( target_tag, idx );
		}, 3000);
	}
	
	var hSearchStr = $("#header_search").val();
	$(document).click(function(event) {
		if( $(document).find($(event).attr("target")).closest(".search_box").length == 0){
			$(".header_pc .search_box").removeClass("focus_on");
			$(".header_pc .search_box > div").removeAttr("style");
		}
	});
	
	if( $(".pc .search_box").length ){
		$(".pc .search_box").each(function(index) {
			var target_this = $(this);

			searchBestAuto[index] = setTimeout(function() {
				fnAutoSearchBox( target_this, index );
			}, 3000);

			$(this).off().on({
				"mouseenter" : function(){
					clearTimeout(searchBestAuto[index]);
				},
				"mouseleave" : function(){
					
					if($(".search_box").hasClass("focus_on")){
						clearTimeout(searchBestAuto[index]);
					} else{
						searchBestAuto[index] = setTimeout(function() {
							fnAutoSearchBox( target_this, index );
						}, 3000);
					}
				}
			});
			
			$("#header_search").off().on({
			"focus" : function() {
				
				$(this).siblings('label').fadeOut('fast');
				$(this).parent().addClass("focus_on");

				if( $(this).val().length > 0 ){
					$(".header_pc .search_box .word").hide();
					$(".header_pc .search_box .similar").show();
				}else{
					$(this).siblings('label').fadeOut('fast');
					$(".header_pc .search_box .word").show();
					$(".header_pc .search_box .similar").hide();
				}
			},
			"keyup" : function() {
				if( $(this).val().length > 0 ){
					$(this).siblings(".btn_strDel").show();
					$(".header_pc .search_box .word").hide();
					$(".header_pc .search_box .similar").show();
				}else{
					$(this).siblings('label').fadeOut('fast');
					$(this).siblings(".btn_strDel").hide();
					$(".header_pc .search_box .word").show();
					$(".header_pc .search_box .similar").hide();
				}
			},
			"blur" : function() {
				if($(this).val().length > 0){
					$(this).siblings('label').fadeOut('fast');
				}else{
					$(this).siblings('label').fadeIn('fast');
					searchBestAuto[index] = setTimeout(function() {
						fnAutoSearchBox( target_this, index );
					}, 3000);
				}
			}

		});
		
		
	});
		
	}
	
	$(".header_pc .word > ul > li > button").off("click").on({
		"click" : function() {
			var now_num = $(".header_pc .word > ul > li > button").index( $(this) );

			$(".header_pc .word > ul > li").removeClass("on").eq(now_num).addClass("on");
			$("#header_search").focus();
		}
	});
	$(".header_pc .search_box .btn_strDel").off("click").on({
		"click" : function(e) {
			if( $(this).parent().hasClass("search_box") ){
				$("#header_search").val("");
				$(this).hide();
				$(".header_pc .search_box").addClass("focus_on");
				$(".header_pc .search_box .word").show();
				$(".header_pc .search_box .similar").hide();
			}else{
				$(this).closest("li").remove();
				return false;
			}
		}
	});
	$(".header_pc .search_box .util .btn_close").off("click").on({
		"click" : function() {
			$(".header_pc .word > ul > li").removeClass("on").eq(0).addClass("on");
			$(".header_pc .word, .header_pc .similar").removeAttr("style");
			$(".header_pc .search_box").removeClass("focus_on");
		}
	});

	/* 미니탭 : 2016.08.04 추가 */
	$(".pc .miniTabBox1 a").off("click").on({
		"click" : function(e) {
			$(".pc .miniTabBox1 a").removeClass("on");
			$(this).addClass("on");

			////e.preventDefault();
		}
	});
	
// 에밀리 사용 확인 바람	
//	function fnAuto_cateVisual() {
//		auto_cateVisual = setTimeout(function() {
//			$(".visual .control .next").trigger("click");
//			fnAuto_cateVisual();
//		}, 3000);
//	}
//
//	$(".visual").on({
//		"mouseenter" : function() {
//			clearTimeout(auto_cateVisual);
//		},
//		"mouseleave" : function() {
//			fnAuto_cateVisual();
//		}
//	});
//	fnAuto_cateVisual();
	
	/* 팝업 닫기 : 2016.08.09 */
	$(".pc .pop_wrap .pc_btn_close").off("click").on("click", function(e){
		// $(this).siblings("div").removeAttr("style").closest(".pop_wrap").removeAttr("style").hide();
		$(this).siblings("div").removeAttr("style");
		$(this).closest(".pop_wrap").removeAttr("style").hide();


		////e.preventDefault();
	});
	
	/* 팝업 닫기 : 2016.11.01 */
	$(".pc .pop_wrap_sm .pc_btn_close").off("click").on("click", function(e){
		$(".pc .pop_wrap_sm").hide();
	});
	
	/* 로그인 - 비회원 주문조회 : 2016.08.09 */
	$(".pc .srch_ordernum").off("click").on("click", function(e){
		fnPopPosition( $(".ly_orderNum") );

		////e.preventDefault();
	});

	$(".pc .btn_nonOrderNum").off("click").on("click", function(e){
		fnPopPosition( $(".ly_orderSearch") );
		// fnPopReposition( $(".srchOrderNum") );

		////e.preventDefault();
	});
	$(".pc .btn_nonPw").off("click").on("click", function(e){
		$(".pc .pop_wrap .srchOrderNum").hide();
		$(".pc .pop_wrap .ly_orderSearch").hide();
		fnPopPosition( $(".ly_pwSearch") );
		// fnPopReposition( $(".srchpw") );

		////e.preventDefault();
	});
	
	/* 찜 : 2016.08.09 */
	$(".pc .pc_btn_jjim").off("click").on("click", function(e){
		$(this).toggleClass("active");

		////e.preventDefault();
	});
	
	/* 장바구니 - 매장 위치 팝업 : 2016.08.28 */
	$(".pc .btn_shopPosition").on("click", function(e){
		fnPopPosition( $(".layer_shop") );

		//e.preventDefault();
	});
	
	
	/* Gnb 전체메뉴 : 2016.08.11 */
	$(".header_pc .gnb .btn_all").off("click").on({
		"click" : function(e) {
			$(this).parent().toggleClass("all_hover");

			////e.preventDefault();
		}
	});
	$(".header_pc .menu .btn_close").off("click").on({
		"click" : function(e) {
			$(this).closest(".all").toggleClass("all_hover");

			////e.preventDefault();
		}
	});
	
	/* 상단 배너 너비 가능 범위 : 2016.08.12 */
	function fnMinHeaderBannerClose() {
		if($(window).width() < 1200){
			$(".header_pc .btn_close_box").css({"right" : $(window).width() - 1200 + "px", "paddingRight" : Math.floor((1200 - $(window).width()) / 2) + "px"});
		}else{
			$(".header_pc .btn_close_box").css({"right" : 0, "paddingRight" : 0});
		}
	}
	fnMinHeaderBannerClose();
	
	
	/* 블럭형&리스트형 선택 : 2016.08.12 */
	$(".pc .btnListType").off("click").on("click", function(){
		var listLength;
		var target_tag;

		if( $(this).closest(".tit_style3").length ){
			listLength = $(this).closest(".tit_style3").nextUntil(".list_group").length;
			target_tag = $(this).closest(".tit_style3");
		}else{
			listLength = $(this).closest(".sortBoxList").nextUntil(".list_group").length;
			target_tag = $(this).closest(".sortBoxList");
		}
		/*$(this).toggleClass("block");

		if(listLength == 0){
			$(target_tag).next(".list_group").find(".product_type1").toggleClass("list");
		} else{
			$(target_tag).nextUntil(".list_group").next(".list_group").find(".product_type1").toggleClass("list");
		}*/
		// 16.11.12 : str
		if($(this).hasClass("list")){
			$(this).removeClass("list").addClass("block");
			$(".product_type1").removeClass("block").addClass("list");

		} else {
			$(this).removeClass("block").addClass("list");
			$(".product_type1").removeClass("list").addClass("block");
		}
	});

	/* 스카이 영역 : 2016.08.17 */
	$(".sky_scraper .btn_show").off("click").on({
		"click" : function(e) {
			if( $(this).parent().hasClass("on") ){
				$(this).parent().removeClass("on");
			}else{
				$(this).parent().addClass("on");
			}

			////e.preventDefault();
		}
	});

	$(".sky_scraper .showroom button").off("click").on({
		"click" : function() {
			var target_tag = $(this).parent().siblings("ul");
			var target_length = $(" li", target_tag).length;
			var now_num = $(this).siblings("span").find("i").text();
			var max_num = Math.round(target_length / 2);

			if( $(this).hasClass("btn_prev") ){
				now_num--;
				if( now_num < 1){
					now_num = max_num;
				}
			}else if( $(this).hasClass("btn_next") ){
				now_num++;
				if( now_num > max_num){
					now_num = 1;
				}
			}

			$("li", target_tag).removeClass("on")
				.eq( now_num*2 - 2 ).addClass("on")
				.end().eq( now_num*2 - 1).addClass("on");

			$(this).siblings("span").find("i").text(now_num);
		}
	});

	$(".sky_scraper .showroom .control").each(function() {
		var target_tag = $(this).prev();
		var max_num = Math.round( $(" li", target_tag).length / 2 );
		$(" em", this).text(max_num);
	});
	$(".sky_scraper .showroom .control em").text();

	$(".sky_scraper .btn_up").off("click").on({
		"click" : function(e) {
			$(window).scrollTop(0);
		}
	});
	$(".sky_scraper .btn_down").off("click").on({
		"click" : function(e) {
			$(window).scrollTop( $(".footer_pc").offset().top );
		}
	});

	/* 스카이 너비 가능 범위 : 2016.08.17 */
	function fnMinSky() {
		if($(window).width() < 1200){
			$(".sky_scraper").css({"right" : $(window).width() - 1200 + "px", "paddingRight" : Math.floor((1200 - $(window).width()) / 2) + "px"});
		}else{
			$(".sky_scraper").css({"right" : 0, "paddingRight" : 0});
		}
	}
	fnMinSky();
	
	
	/* 메인 비주얼 */
//	var firstSrc = $(".pc .main_visual .control span").next().find("li:first").find("a").attr("href");
//	var firstColor = $(".pc .main_visual .control span").next().find("li:first").find("a").attr("data-color");
//	var firstUrl = $(".pc .main_visual .control span").next().find("li:first").find("a").attr("data-url");
//
//	$(".pc .main_visual .slide img").attr("src", firstSrc);
//	$(".pc .main_visual .slide li span").css("background-color", firstColor);
//	$(".pc .main_visual .slide li a").attr("href", firstUrl);
	
	
	/* 브랜드 더보기 : 2016.08.17 */
	if($(".pc .bnrBrand").find("li").length>6){
		$(".bnrBrand").find("li:gt(5)").hide();
		$(".bnrBrand").find(".btnMore").show();
	} else {
		$(".bnrBrand").find(".btnMore").hide();
	}
	
	$(".pc .bnrBrand").find("button").off("click").on("click", function(e){
		////e.preventDefault();
		$('.bnrBrand').toggleClass("open");

		if($(".bnrBrand").hasClass("open")){
			$(".bnrBrand").find("li:gt(5)").show();
		} else {
			$(".bnrBrand").find("li:gt(5)").hide();
		}
	});
	
	$(".pc .evt_tit").off("click").on("click", function(e){
		$(this).parent().toggleClass("slideHide");
	});
	
});



$(window).scroll(function() {
	// 스카이 스크롤 가능 범위
	fnSkyScroll();

});

$(window).resize(function() {
	fnMinSky();
	fnMinHeaderBannerClose();
});
