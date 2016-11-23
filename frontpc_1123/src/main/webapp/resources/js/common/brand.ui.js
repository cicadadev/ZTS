

	function brandTemplate() {
		$(".mobile .mainCon .brand_home #mo_brandCategory").on("click", "li",function() {
			var chooseCategory = $(this).find("a").text();
			
			for (var i=0; i<$("#layer_brandCategory").find("[name=brand_category1]").length; i++) {
				var category1 = $("#layer_brandCategory").find("[name=brand_category1]:eq("+i+")");
				
				category1.removeClass("on");
				if (category1.find("a:eq(0)").text() == chooseCategory) {
					category1.addClass("on");
				}
			}
			
//			브랜드 팝업 위치 중앙 조정
			$(".mobile .layer_style1 .brand_menu .brand_category").siblings("li:eq(0)").addClass("on");
			var target_pop = $(".mobile .layer_style1.brand_menu");
			
			$(target_pop).show();
			$(target_pop).height( $(document).height() );

			var base_top = ($(window).height() - $(" > .box", target_pop).innerHeight()) / 2  - $(".header_mo").innerHeight();
			$(" > .box", target_pop).css({"marginTop" : base_top + $(window).scrollTop() + "px"});
		});		
	}
	
	function renderShowLookbook() {
		var cx = 0;
		var cy = 0;
		var max = 0;
		var marginTop = 2;
		var secondPosY = [85,70,0,0];
		var obj_compare = [0,0,0]
		var length = 2;

		$(".mobile .list_grid>li").each(function(n){
			var init_num = 0;
			var min = 0;
			if(n < length) {
				min = n;
				cy  = 0;
			} else {
				for(var i=0; i<length; i++) {
					if(i===0) {
						init_num = obj_compare[i];
					} else {
						if(init_num>obj_compare[i]) {
							init_num = obj_compare[i];
							min = i;
						}
					}
				}
				cy = obj_compare[min];
			}
			cx = Math.round(100/length*10)/10 * min;
			$(this).css({
				position : "absolute",
				top:cy+"px",
				left:cx+"%"
			});
			obj_compare[min] = marginTop + cy + parseInt($(this).height());
		});
		for(var i=0; i<length; i++) {
			if(i===0) {
				max = obj_compare[i];
			} else {
				if(max<obj_compare[i]) {
					max = obj_compare[i];
				}
			}
		}
		$(".mobile .list_grid").height(max);
	}	
	
	function lookbookListShow (){
		if($(".mobile .list_grid").size()>0) {
			var obj_flag = {};
			var resize_flag = "";
			var obj_compare = [];
			var imgTotal = $(".mobile .list_grid>li img").length;
			var count = 0;

			$(".mobile .list_grid>li img").each(function(n){
				var url = $(this).attr("src")+'?n='+Math.random()*1000;
				$(this).attr("src",url);
				$(this).load(function(){
					if(++count == imgTotal){
						renderShowLookbook();
					}
				})
			});
		}
	}	
	
	function brandNaviSwiper(){
		
		if( $(".mobile .mainCon").length ){
			
			var mainSwiper_header = new Swiper('.mo_mainNavi', {
				slidesPerView: 'auto', //가로 길이
		        paginationClickable: true,
		        spaceBetween: 0 //간격

			});
			
			//텝클릭시 스와이프 기능
			$('.gnb > li > a').off("click").on('click', function(e){
				$(this).parent().addClass('on').siblings().removeClass('on');
				var tabIdx = $(this).parent().index();
				//0 부터 시작 탭의 index번호
				//console.log('탭클릭 Idx : '+tabIdx);
				
				mainSwiper_body.slideTo(tabIdx + 1, 300 );

				e.preventDefault();
				
			});
			
			var tabLen = $('.gnb > li').length;
			
			// 스와이핑시 컨텐츠 조회
			var startSwipe = function(idx){
				
				
//				if(idx==0){
//				}else if(idx==1 && common.isEmpty($('#shockingzeroMainDiv').html())){
//				}else if(idx==2  && common.isEmpty($('#exhibitionMainDiv').html())){
//				}else if(idx==3 && common.isEmpty($('#exhibitionMainDiv').html())){
//				}else if(idx==4  && common.isEmpty($('#eventMainDiv').html())){
//				}else if(idx==5 && common.isEmpty($("#shopOnMainDiv").html())){
//				}else if(idx==6 && common.isEmpty($('[name=styleMainDiv]').html())){
//				}
				
			}
			//현재 인덱스
			var currentIdx = function(swiper){
				var idx = swiper.activeIndex-1;
				
				if( idx < 0 ) { 
					idx = tabLen - 1;
				} else if( idx == tabLen ){
					idx = 0;
				}
				
				return idx;
				
			}
			
			mainSwiper_body = new Swiper('.mainCon', {
				autoHeight: true,
				calculateHeight:true,
				slidesPerView: 1,
				spaceBetween: 0,
				pagination: false,
				loop: true,
				//lazyLoading : true,//
				//lazyLoadingInPrevNext : true,
				// 슬라이드 시작 시점
				onTransitionStart : function(swiper){
					startSwipe(currentIdx(swiper));
				},
				// 슬라이드 오른쪽 종료 시점
				onSlideNextEnd : function(swiper){
					
					var idx = currentIdx(swiper);
					
					if(idx != 0){// 홈일경우 오른쪽 가져오지 않음
						startSwipe(currentIdx(swiper) + 1);
					}
				},			
				// 슬라이드 왼쪽 종료 시점
				onSlidePrevEnd : function(swiper){
					startSwipe(currentIdx(swiper) - 1);
				},
				// 슬라이드 시작시 헤더 선택되게
				onSlideChangeStart: function(swiper){
					var idx = currentIdx(swiper);
					
					$('.gnb > li').removeClass('on').eq(idx).addClass('on');
					if( idx < tabLen ) {
						mainSwiper_header.slideTo(idx-1, 300);
					}
				
					$(window).scrollTop(0);	
				}
			});
		}		
		
			
		
	}
$(document).ready(function(){	
	

	
	// 메인 스와이프
	brandNaviSwiper();
	
	// 브랜드관 - home 카테고리 메뉴
	$(".mobile .brandType .brand_category").find("li").off("click").on({
		"click" : function() {
			fnLayerPosition( $(".mobile .layer_style1.brand_menu") );
		}
	});
	
	
	if($(".brand_outer .brand_category").size()>0){
		$(".brand_outer .brand_category >li >ul").each(function(){
			if($(this).size()>0) {
				$(this).closest("li").addClass("openDepth");
			};
		});
		
		if($(this).size()>1) {
			$(".brand_outer .brand_category >li:last-child()").addClass("last");
		}
		
	}
	
	// 브랜드관 토미티피 상세검색
	if($(".pc .brandType .optionItem.first").find("li").length>8){
		$(".optionItem.first").find("li:gt(7)").hide();
	}
	
	brandTemplate();
	
	// 브랜드관 - lookbook
	lookbookListShow();

	// 브랜드관 - 좋아요 버튼
	$(".pc .btnLike").off("click").on("click", function(){
		$(this).toggleClass("on");
	});


	ccs.mainSwipe.calculateHeight();
});