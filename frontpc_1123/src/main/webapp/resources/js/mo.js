

/* ################################ 공통 함수  ################################ */


function scrollState() {
	
	isScroll();
    st = $(window).scrollTop();

    if(srState == 'down') {
    	// console.log('down')
        mo_navi.stop().transition({ y: header_posY });
        if($(".content.premiumMember").length > 0) {
        	header_mo.stop().transition({ y: 0 });
        } else {
        	header_mo.stop().transition({ y: header_posY });
        }

        if(st >= mo_navi_h+specially_banner_h+10) {
        	tabFix.addClass("on");
        	tabFix.stop().transition({ y: mo_navi_posY });
        }

    } else if (srState == 'up'){
    	// console.log('up')
    	header_mo.stop().transition({ y: 0 });
        mo_navi.stop().transition({ y: 0 });
        tabFix.stop().transition({ y: header_mo_h+mo_navi_posY-6 });
        
    } else if(srState == 'top') {
    	tabFix.removeClass("on");
    	tabFix.stop().transition({ y: 0 });
    	header_mo.stop().transition({ y: 0 });

    }

}

// scroll up,down 여부
function isScroll() {

	var st = $(this).scrollTop(); //스크롤 위치
	
	if (st > lastScrollTop){
		// scroll down
		srState = 'down';
	} else if (st <= 0) {
		// scroll top
		srState = 'top';
	} else{
		// scroll up
		srState = 'up';
	} 
	
	
	if(lastScrollTop < 0) {
		lastScrollTop = 0;
	} else {
		lastScrollTop = st;
	}


	// if(st >= mo_navi_h) {
	// 	tabFix.addClass("on");
	// } else {
	// 	tabFix.removeClass("on");
	// }

}

// 메뉴 오픈
function fnMenuOpen(target_tag, className) {
	$(target_tag).addClass( className );
	$(".mobile .dim_fixed").show();
	$body.css({"overflow": "hidden"});
	$(".mobile .all_menu .box, .mobile .com_brand .box").scrollTop(0);
}

// 메뉴 닫기
function fnMenuClose(target_tag, className) {
	$(target_tag).removeClass( className );
	$(".mobile .dim_fixed").hide();
	//$(".wrap").removeAttr("style");
	$body.removeAttr("style");
}

//이미지 크기가 다를경우 이미지 리스트정렬
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
	

// 헤더 모션 끝 --------------------------------------------------------------- //



// ############################## Swiper 함수 ############################## //

// 전체 Swiper  시작 --------------------------------------------------------------- //

/*
 * 스와이프 높이 계산
 * .swiper-wrapper:eq(0) : gnb
 * .swiper-wrapper:eq(1) : mainCon > inner
 */
function calculateHeight() {
	for (var i=0; i<$(".mobile .swiper-wrapper:eq(1)").find(".inner").length; i++) {
		if ($(".mobile .swiper-wrapper:eq(1) .inner:eq(" + i + ")").is(".swiper-slide-active")) {
			var inner_height = $(".mobile .swiper-wrapper:eq(1) .inner:eq(" + i + ")").height();
			$(".mobile .swiper-wrapper:eq(1)").height( inner_height );
			$(".mobile .mainCon").height( inner_height );					
		}
	}
}

//Swiper typeB - 좌우버튼 없음, 하단 Step (1/23) 형태
function swiperCon(obj, cnt, spance, paginationType, loop) { // obj : 태그이름, cnt : 리스트 개수
	var objTag = "."+obj;
	var obj_pagination = "."+obj+" .swiper-pagination";

	obj = new Swiper(objTag, {
		pagination: obj_pagination,
        paginationType: paginationType,
        slidesPerView: cnt, //뿌려질 개수 설정
        spaceBetween: spance,
        loop: loop
    });
}



$(document).ready(function() {
	// 헤더 모션 시작 --------------------------------------------------------------- //
	lastScrollTop = 0; //스크롤 위치 위치 값
	header_inner_h = $('.header_mo .inner').innerHeight(); // 헤더 높이 값
	srState = '';

	header_mo = $(".mobile .header_mo"); //헤더 obj
	header_mo_h = $(".mobile .header_mo").innerHeight()+5; //헤더 높이

	mo_navi = $(".mobile .mo_navi");
	mo_navi_h = $(".mobile .mo_navi").innerHeight();
	mo_navi_posY = mo_navi_h + 1;
	header_posY = header_inner_h * -1; // 스크롤 내렸을때의 헤더 위치 값
	tabFix = $(".tabFixW .tabFix .tab_outer");
	specially_banner_h = $(".visual .img_outer").innerHeight();
	//naviSwiper();
	

	/* ###### Swiper 호출  시작 ###### */

	// left 메뉴
	// swiperCon('brandMenuSwiper_sortingWord', '6'); //left 메뉴 - 브랜드명 sorting
	
	
	// 메인 Swiper
//	swiperCon('mainBanner_swiper_1', '1', 0, 'fraction'); //메인 - 메인 배너
//	swiperCon('main_prodSwiper_1', '3'); //메인 - MD 특가
//	swiperCon('main_prodSwiper_2', '3'); //메인 - 상품 추천
//	swiperCon('main_prodSwiper_3', '3'); //메인 - HOT 상품
//	swiperCon('main_brandSwiper', '4'); //메인 - 제휴배너
//	swiperCon('mainSwiper_category', '5'); //메인 - 카테고리 탭
//	swiperCon('mainBanner_swiper_2', '1'); //메인 - 광고배너
//
//	//shopOn
//	swiperCon('shopOnSwiper_shopList', '1'); //샵on - 매장리스트
//	swiperCon('mainShopOn_banner_1', '1'); //샵on - 배너
//	swiperCon('shopOnSwiper_bestProd', '5'); //샵on - 매장픽업 BEST 상품
//
//	//랭킹베스트
//	swiperCon('linkBestSwiper_category', 'auto'); //베스트링크 - 실시간 클릭 BEST
//
//	// 스타일
//	swiperCon('styleSwiper_banner_1', '1'); //샵on - 배너
//	swiperCon('styleDetail_prodList_1', '5', 10);
//
//
//	//기획전
//	swiperCon('promotionSwiper_banner_1', '1', 0, 'fraction'); //메인 - 메인 배너
//
//	// 앱 이용방법
//	swiperCon('appGuide_swiper', '1'); //앱 이용방법
//
//	// 브랜드
//	swiperCon('brandSwiper_banner_1', '1', 0, 'fraction'); //배너
//	swiperCon('brandSwiper_realProdList_1', '3'); //실시간인기상품
//
//	// 중카테고리
//	swiperCon('displayCategorySwiper_cetegory', '1', 0, 'fraction'); //카테고리
//	swiperCon('displayCategorySwiper_banner_1', '1', 0, 'fraction'); //배너
//	swiperCon('displaySwiper_prodList_1', '3'); //중카테고리 - MD 추천
//
//
//	//멤버쉽관
//	swiperCon('premiumSwiper', '1'); //배너
//	swiperCon('premiumSwiper_prodList_1', '3'); //배너
//
//
//	//리미엄멤버십관
//	swiperCon('premiumMember_banner_1', '1'); //배너
//	swiperCon('premiumMember_prodList_1', '3'); // 상품리스트
//	
//
//	//상품상세
//	swiperCon('prodDetailSwiper_list_1', '1');
//	swiperCon('prodDetailSwiper_banner_1', '1'); //배너 1
//	swiperCon('prodDetailSwiper_banner_2', '1'); //배너 2
//	
//	//장바구니
//	lookbookListShow();
//	swiperCon('cartSwiper_pordList', '2'); //
//
//	//연관상품
//	swiperCon('relatedProdSwiper_1', '3'); // 연관상품
//
//	// 검색결과
//	swiperCon('searchResultSwiper_relatedSearches', 'auto'); // 뎐관검색어
//	swiperCon('searchResultSwiper_searchList_1', '1', 30);
//	swiperCon('searchResultSwiper_searchList_2', '1', 30);


	// 마이페이지
	
	/* ------------- ###### Swiper 호출  끝 ###### -------------  */
	
	$(window).scroll(function(e) {
		scrollState();
	});


	/* ######################## 마이페이지  ########################*/

	

	// mobile 브랜드관 룩북 슬라이더
	if( $('.mobile #imageGallery').length ){
		$('.mobile #imageGallery').lightSlider({
			item:1,
			vertical:true,
			verticalHeight:$(".mobile #imageGallery img:first").height(),
			slideMargin:0,
			controls:false,
			onSliderLoad: function() {
				$('.mobile .lSSlideOuter .lightSlider li').css("height", $(".mobile #imageGallery img:first").height());
			}  
		});
	}


});

