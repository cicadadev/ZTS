
	// 전문관 탭 상단 고정 : 2016.10.24[PC]
	function fnTabPosition() {
		var tabBoxT = $(".pc .tabFixW").offset().top;

		if( $(window).scrollTop() > tabBoxT ){
			$(".tabFix").addClass("fixedTab");
		} else if($(window).scrollTop() < tabBoxT) {
			$(".tabFix").removeClass("fixedTab");
		}
	}

$(document).ready(function(){
	
	// 16.10.24 : 전문관 탭 상단 고정
	if($(".pc .tabFixW").length>0){
		fnTabPosition();
	}
	
	// 전시 - 소카테고리 닫기
/*	$(".catePaginate").find(".rw_btnCateClose").off("click").on("click", function(){
		$(".mo_navi").find(".rw_btnMCate").removeClass("active");
		$(".rw_displayListBox").find(".categoryBox").hide();
	});*/



	// 전시 - 옵션 소재/혜택 선택
	$(".optionBox .btnList").find("button").off("click").on("click", function(){
		$(this).toggleClass("active");
	});

	// 전시 - 옵션 컬러 선택 : 주석 풀면 안됨. 페이지에 ready안에서 각각 써야 함. emily 2016.11.09
	/*$(".optionBox .colorList").find("a").off("click").on("click", function(e){
		//e.preventDefault();
		$(this).toggleClass("active");
	});*/

	// 전시 - 찜버튼
	$(".rw_btnWish").off("click").on("click", function(e){
		//e.preventDefault();
		$(this).toggleClass("active");
	});

	
		// 전시 - 베스트
	if($(".bestBox .categoryBox").length>0){
		if(!$(this).hasClass("depth1")){
			$(this).find("a").off("click").on("click", function(e){
				//e.preventDefault();
				$(this).parent().siblings('li').removeClass("active");
				$(this).parent().addClass("active");
			});
		}
	}
	
	
	// [전문관] 기프트관
	if($(".babyInfo").length>0){
		$(".btnBaby").off("click").on("click", function(){
			$(".babyInfo").toggleClass("open");
		});

		$(".babyCheck").find("a").off("click").on("click", function(e){
			//e.preventDefault();
			$(this).toggleClass("active");
		});
	}
	
	// 16.11.09 : 검색결과 카테고리 닫힘 디폴트 요청
	if($(".pc .optionItem.category").find("li").length>8){
		$(".optionItem.category").find("li:gt(7)").hide();
	}
	
	
	// 전시 - 옵션 펼치기/접기
	if($(".pc .optionItem.brand").find("li").length>8){
		$(".optionItem.brand").find("li:gt(7)").hide();
	}
	
	$(".pc .optionItemBox").find(".btnMore").off("click").on("click", function(){
		var $optionItm = $(this).parent(".optionItemBox");

		$optionItm.toggleClass("open");

		if($optionItm.hasClass("open") && $optionItm.find(".first li").length>8){
			$optionItm.find(".first li:gt(7)").show();
		} else if(!$optionItm.hasClass("open")) {
			$optionItm.find(".first li:gt(7)").hide();
		}
	});

	// 전시 - 옵션 소재/혜택 선택
	$(".optionBox .btnList").find("button").off("click").on("click", function(){
		$(this).toggleClass("active");
	});

	// 전시 - 옵션 컬러 선택 : 주석 풀면 안됨. 페이지에 ready안에서 각각 써야 함. emily 2016.11.09
/*	$(".optionBox .colorList").find("a").off("click").on("click", function(e){
		////e.preventDefault();
		$(this).toggleClass("active");
	});*/

	// 전시 - 찜버튼
	$(".rw_btnWish").off("click").on("click", function(e){
		////e.preventDefault();
		$(this).toggleClass("active");
	});

	// 전시 - 카테고리 메인 브랜드 더보기
	$(".btnMoreBrand").find("button").off("click").on("click", function(e){
		////e.preventDefault();
		$(this).parent().prev(".brandList").toggleClass("open");
	});
	
	// 전시 - 베스트
	if($(".bestBox .categoryBox").length>0){
		$(this).find("a").off("click").on("click", function(e){
			////e.preventDefault();
			$(this).parent().siblings('li').removeClass("active");
			$(this).parent().addClass("active");
		});
	}
	
	
	/* 기획전 탭 이동 : 2016.08.05 추가 */
	function fnTabNavi() {
		//var tab_tag = $(".pc .tabNavi_btns").prev(".tabBox");
		var tab_tag = $(".pc .tabNavi_btns").prev(".tabBox");
		var tab_left = parseInt( $(tab_tag).css("marginLeft") );
		var tab_num = $("li", tab_tag).length;
		var tab_li_width = $("li", tab_tag).width();
		var tab_max = tab_num - 6;
		var tab_now = Math.round( tab_left / tab_li_width );




		var iconTab = $(".pc .tab_outer .tabBox");
		var iconTabW = ($(" li", iconTab).length * $(" li:first", iconTab).width() );

		//$("ul", promoBrandd).width( promoBrandWidthd );

		$(".pc .tab_outer .tabBox").width( iconTabW );




		if( tab_num > tab_max ){
			$(".pc .tabNavi_btns .next").addClass("on");
		}

		$(".pc .tabNavi_btns a").off("click").on({
			"click" : function(e) {
				tab_left = parseInt( $(tab_tag).css("marginLeft") );
				//console.log( tab_now, tab_li_width );

				if( $(this).hasClass("prev") ){
					tab_now--;
					if( tab_now < 0 ){
						tab_now = 0;
					}
				}else{
					tab_now++;
					if( tab_now > tab_max ){
						tab_now = tab_max;
					}
				}
				$(tab_tag).css({"marginLeft" : -tab_now * tab_li_width + 1 + "px"});

				if( tab_now > 0 ){
					$(".pc .tabNavi_btns .prev").addClass("on");
				}else{
					$(".pc .tabNavi_btns .prev").removeClass("on");
				}

				if( tab_now < tab_max ){
					$(".pc .tabNavi_btns .next").addClass("on");
				}else{
					$(".pc .tabNavi_btns .next").removeClass("on");
				}

				////e.preventDefault();
			}
		});
	}
	fnTabNavi();
	
	/* 카테고리 열고 닫기 버튼 노출*/
	if($(".pc .pc_categoryS .miniTabBox1").find("li").length>5){
		$(".pc_categoryS").addClass("open");
	} else {
		$(".pc_categoryS").find(".btnMore").hide();
	}

	/* 카테고리 목록 펼치기 닫기 이벤트 */
	$(".pc .pc_categoryS").find(".btnMore").off("click").on("click", function(){
		$(this).parent(".pc_categoryS").toggleClass("open");

		if($(".pc_categoryS").hasClass("open") && $(".pc_categoryS").find(".miniTabBox1 li").length>5){
			$(".pc_categoryS").find(".miniTabBox1 li:gt(4)").show();
		} else {
			$(".pc_categoryS").find(".miniTabBox1 li:gt(4)").hide();
		}
	});
	
	
	/* 쇼킹제로 - 비주얼 영역 : 2016.09.02 */
	$(".pc .main_zero .zero_visual .thumb a").off("click").on("click", function(e) {
		$(this).parent().addClass("on").siblings("li").removeClass("on");
		$(".pc .zero_visual .view > li").eq( $(this).parent().index() ).addClass("on").siblings("li").removeClass("on");
	});

	// 전문관 - 출산준비관 토글
	$(".pc .special .chkList").find("dt").off("click").on("click", function(){
		$(this).parent().toggleClass("open").siblings("dl").removeClass("open");
	});

	// 전문관 - 맞춤정보 상세검색
	$(".pc .btnSrchDetail").off("click").on("click", function(){
		$(".detailOptBox").show();
	});
	$(".pc .detailOptBox .btnDetailClose").off("click").on("click", function(){
		$(".detailOptBox").hide();
	});
	
	/* 메인 브랜드배너 */
//	if( $(".pc .main .brandBox ul").length ){
//		var brandBox = $(".pc .main .brandBox ul");
//		var brandBox_width = $(" li", brandBox).length * 158;
//		var max_move = 948 - brandBox_width; //944
//
//		$(brandBox).width( brandBox_width );
//
//		$(".pc .main .brandBox .btn_next").off().on({
//			"click" : function() {
//				if( !$(brandBox).is(":animated") ){
//					var now_margin = parseInt( $(brandBox).css("marginLeft") );
//					var go_margin = now_margin - 158;
//
//					if( max_move < go_margin ){
//						$(brandBox).animate({"marginLeft" : go_margin}, 400);
//					}else{
//						$(brandBox).animate({"marginLeft" : max_move}, 400);
//					}
//				}
//			}
//		});
//
//		$(".pc .main .brandBox .btn_prev").off().on({
//			"click" : function() {
//				if( !$(brandBox).is(":animated") ){
//					var now_margin = parseInt( $(brandBox).css("marginLeft") );
//					var go_margin = now_margin + 158;
//
//					if( 0 > go_margin ){
//						$(brandBox).animate({"marginLeft" : go_margin}, 400);
//					}else{
//						$(brandBox).animate({"marginLeft" : 0}, 400);
//					}
//				}
//			}
//		});
//	}

	/* 메인 비주얼 Swiper 시작*/
	var firstSrc = $(".pc .main_visual .control span").next().find("li:first").find("a").attr("href");
	var firstColor = $(".pc .main_visual .control span").next().find("li:first").find("a").attr("data-color");
	var firstUrl = $(".pc .main_visual .control span").next().find("li:first").find("a").attr("data-url");

	$(".pc .main_visual .slide img").attr("src", firstSrc);
	$(".pc .main_visual .slide li span").css("background-color", firstColor);
	$(".pc .main_visual .slide li a").attr("href", firstUrl);

	var mVisual_auto = new Array();
	function fnAutomVisual(target_tag, idx) {
		var target_img = $("ul li", target_tag);
		var menuDepth = $(".control ul li", target_tag);
		var now_num = $(menuDepth).index( $(".control ul li.on", target_tag));
		var bigSrc = $(menuDepth).eq(now_num+1).find("a").attr("href");
		var bigcolor = $(menuDepth).eq(now_num+1).find("a").attr("data-color");
		var bigUrl = $(menuDepth).eq(now_num+1).find("a").attr("data-url");
		
		now_num = now_num + 1;
		//console.log(now_num);
		
		if( now_num >= $(menuDepth).length ){
			now_num = 0;
		}
		
		$(menuDepth).removeClass("on").eq(now_num).addClass("on");
		if ($(".control ul li:first-child").hasClass("on")) {
			$(menuDepth).parent().parent().removeClass("on");
			$(menuDepth).eq(now_num).parent().parent().addClass("on");
		}
		
		$(".pc .main_visual .slide img").attr("src", bigSrc);
		$(".pc .main_visual .slide li span").css("background-color", bigcolor);
		$(".pc .main_visual .slide li a").attr("href", bigUrl);
		
		mVisual_auto[idx] = setTimeout(function() {
			fnAutomVisual( target_tag, idx );
		}, 3000);
	}
	
	$(".pc .main_visual").each(function(index) {
		var target_this = $(this);

		mVisual_auto[index] = setTimeout(function() {
			fnAutomVisual( target_this, index );
		}, 3000);
		
		$(this).on({
			"mouseenter" : function(){
				clearTimeout(mVisual_auto[index]);
			},
			"mouseleave" : function(){
				mVisual_auto[index] = setTimeout(function() {
					fnAutomVisual( target_this, index );
				}, 3000);
			}
		});
	});
	
	$(".pc .main_visual .control span").off().on({
		"mouseenter" : function() {
			$(".pc .main_visual .control li").removeClass("on");
			$(this).closest("li").addClass("on").find("li:first").addClass("on");
			var bigSrc = $(this).next().find("li:first").find("a").attr("href");
			var bigcolor = $(this).next().find("li:first").find("a").data("color");
			var bigUrl = $(this).next().find("li:first").find("a").data("url");

			$(".pc .main_visual .slide img").attr("src", bigSrc);
			$(".pc .main_visual .slide li span").css("background-color", bigcolor);
			$(".pc .main_visual .slide li a").attr("href", bigUrl);
		}
	});

	$(".pc .main_visual .control ul a").off().on({
		"mouseenter" : function() {
			$(".pc .main_visual .control li").removeClass("on");
			$(this).parent().addClass("on").parent().parent().addClass("on");
			var bigSrc = $(this).attr("href");
			var bigcolor = $(this).attr("data-color");
			var bigUrl = $(this).attr("data-url");
			
			$(".pc .main_visual .slide img").attr("src", bigSrc);
			$(".pc .main_visual .slide li span").css("background-color", bigcolor);
			$(".pc .main_visual .slide li a").attr("href", bigUrl);
			
		},
		"click" : function(e){
			e.preventDefault();
		}
	});

	$(".pc .main_visual .control .btn_next").off().on({
		"click" : function() {
			var next_obj = $(".pc .main_visual .control ul li.on").next();
			var next_parent_obj = $(".pc .main_visual .control ol > li.on").next();

			if( $(next_obj).length ){
				$(" a", next_obj).trigger("mouseenter");
			}else{
				if( $(next_parent_obj).length ){
					$(" span", next_parent_obj).trigger("mouseenter");
				}else{
					$(".pc .main_visual .control ol > li:first span").trigger("mouseenter");
				}
			}
		}
	});

	$(".pc .main_visual .control .btn_prev").off().on({
		"click" : function() {
			var prev_obj = $(".pc .main_visual .control ul li.on").prev();
			var prev_parent_obj = $(".pc .main_visual .control ol > li.on").prev();

			if( $(prev_obj).length ){
				$(" a", prev_obj).trigger("mouseenter");
			}else{
				if( $(prev_parent_obj).length ){
					$(prev_parent_obj).find("li:last a").trigger("mouseenter");
				}else{
					$(".pc .main_visual .control ol > li:last li:last a").trigger("mouseenter");
				}
			}
		}
	});
	/* 메인 비주얼 Swiper 끝 */
	
});	

$(window).scroll(function() {
	// 16.10.24 : 전문관 탭 상단 고정
	if($(".pc .tabFixW").length>0){
		fnTabPosition();
	}
});

